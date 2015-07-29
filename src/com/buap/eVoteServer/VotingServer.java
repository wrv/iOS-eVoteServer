package com.buap.eVoteServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VotingServer {
	private final ServerSocket serverSocket;

	//private final boolean debug;
	private int connectedUsers = 0;
	BilinearOperations curBOps;

	/**
	 * Make a VotingServer that listens for connections on port.
	 * 
	 * @param port
	 *            port number, requires 0 <= port <= 65535
	 */
	public VotingServer(int port, BilinearOperations cur, boolean debug) throws IOException {
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
		//IO connections to the client
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
		//Receive the Ballot
		try {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				String output = handleRequest(line);
				
				//Generated the JSON to send to the voter
				if (output != null) {
					JSONObject response = new JSONObject();
					//check for errors. If none then send acknowledgement
					if (output.equals("Invalid Vote")) {
						response.put("status", "invalid vote");
					}
					else if (output.equals("Error")) {
						response.put("status", "error");
					} else {
						response.put("status", "okay");
						response.put("ACK", output);
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
		try{
			JSONObject ballotContents = (JSONObject) parser.parse(input);
			String publicAlias = (String) ballotContents.get("publicAlias");
			String signedAlias = (String) ballotContents.get("signedAlias");
			String vote = (String) ballotContents.get("vote");
			String signedVote = (String) ballotContents.get("signedVote");
			
			boolean validVote = verifySignature(publicAlias, signedAlias, vote, signedVote);
			
			if(validVote){
				return generateAcknowledgement(publicAlias, signedAlias, vote, signedVote);
			}
			return "Invalid Vote";
		} catch(ParseException pe) {
			System.out.println("Parse Exception at: " + pe.getPosition());
			return "Error";
		}
	}
	
	/**
	 * verifySignature
	 * 
	 * Takes in our elements and verifies using bilinear pairings if the signature
	 * and vote are both valid
	 * 
	 * @param publicAlias
	 * @param signedAlias
	 * @param vote
	 * @param signedVote
	 * @return
	 */
	private boolean verifySignature(String publicAlias, String signedAlias, String vote, String signedVote){
		boolean validAS = false;
		boolean validVoteSign = false;
		
		//We first verify the signature of the AS
		validAS = curBOps.verifyPairing(signedAlias, curBOps.pk.toString(), curBOps.H1(curBOps.m2s(publicAlias)));
		
		//Now we verify the signature of the voter
		validVoteSign = curBOps.verifyPairing(signedVote, publicAlias, curBOps.H1(vote));
		
		return validAS && validVoteSign;
	}
	
	/**
	 * generateAcknowledgement
	 * 
	 * Generates a random value and returns the hash of all our elements
	 * 
	 * We use SHA-256 for this
	 * 
	 * @param publicAlias - the public alias of the voter
	 * @param signedAlias - the signed AS public alias of the voter
	 * @param vote - the vote that was given
	 * @param signedVote - the signed vote that was given
	 * @return
	 */
	private String generateAcknowledgement(String publicAlias, String signedAlias, String vote, String signedVote){
		String message = publicAlias + signedAlias + vote + signedVote + curBOps.randNum();
		
		return curBOps.H(message);
	}
}
