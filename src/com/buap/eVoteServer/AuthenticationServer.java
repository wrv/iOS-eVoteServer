package com.buap.eVoteServer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AuthenticationServer {
	private final ServerSocket serverSocket;
	
	//private final boolean debug;
	private int connectedUsers = 0;
	BilinearOperations curBOps;

	/**
	 * Make a AuthenticationServer that listens for connections on port.
	 * 
	 * @param port
	 *            port number, requires 0 <= port <= 65535
	 */
	public AuthenticationServer(int port, BilinearOperations cur, boolean debug) throws IOException {
		serverSocket = new ServerSocket(port);
		curBOps = cur;
		//this.debug = debug;
	}

	/**
	 * Run the server, listening for client connections and handling them. Never
	 * returns unless an exception is thrown.
	 * 
	 * @throws IOException
	 *             if the main server socket is broken (IOExceptions from
	 *             individual clients do *not* terminate serve())
	 */
	public void serve() throws IOException {
		while (true) {
			// block until a client connects
			final Socket socket = serverSocket.accept();

			// handle the client
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						connectedUsers++;
						System.out.println("Number of connected Users: " + connectedUsers);
						handleConnection(socket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();

		}
	}

	/**
	 * Handle a single client connection. Returns when client disconnects.
	 * 
	 * @param socket
	 *            socket where the client is connected
	 * @throws IOException
	 *             if connection has an error or terminates unexpectedly
	 */
	@SuppressWarnings("unchecked")
	private void handleConnection(Socket socket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		//Authenticate the User
		try {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				String output = handleRequest(line);
				
				//Generated the JSON to send to the voter
				if (output != null) {
					JSONObject response = new JSONObject();
					//check for errors. If none then send signed blinded message
					if (output.equals("Invalid Authentication")) {
						response.put("status", "invalid auth");
					}
					else if (output.equals("Error")) {
						response.put("status", "error");
					} else {
						response.put("status", "okay");
						response.put("signed", output);
					}
					//send response JSON
					out.println(response.toJSONString());
					break;
				}
			}
		} finally {
			connectedUsers--;
			out.close();
			in.close();
		}
	}

	/**
	 * Handler for client input, performing requested operations and returning
	 * an output message.
	 * 
	 * @param input
	 *            message from client
	 * @return message to client
	 */
	private String handleRequest(String input) {
		JSONParser parser = new JSONParser();
		//We will now verify the signature and sign the psuedonymous ballot
		try{
			JSONObject ballotContents = (JSONObject) parser.parse(input);
			String voterID = (String) ballotContents.get("voterID");
			//String timestamp = (String) ballotContents.get("timestamp"); //Not included in reference paper
			String blindedMessage = (String) ballotContents.get("blindedMessage");
			String signedBMessage = (String) ballotContents.get("signedBMessage");
			
			//From some database, retrieve the users actual public key
			String voterPublicKey = retrieveVoterPK(voterID); //something with voterID
			
			if(!voterPublicKey.equals("dne")){
				boolean validVote = curBOps.verifyPairing(signedBMessage, voterPublicKey, blindedMessage);
				
				if(validVote){
					return curBOps.signMessage(blindedMessage);
				}
			}
			return "Invalid Authentication";
		} catch(ParseException pe) {
			System.out.println("Parse Exception at: " + pe.getPosition());
			return "Error";
		}
	}

	private String retrieveVoterPK(String voterID){
		JSONParser parser = new JSONParser();
		try{
			JSONObject voters = (JSONObject) parser.parse(new FileReader("C:\\db.json"));
			JSONObject curVoter = (JSONObject) voters.get(voterID);
			return (String) curVoter.get("pk");
			
		} catch(ParseException pe){
			System.out.println("Parse Exception at: " + pe.getPosition());
			return "dne";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "dne";
		} catch (IOException e) {
			e.printStackTrace();
			return "dne";
		}
	}
}
