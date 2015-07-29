package com.buap.eVoteServer;

import java.io.IOException;

public class RunServers {

	public static void main(String[] args) {
		// Generate the domain parameters
		String ECval1 = "12723517038133731887338407189719511622662176727675373276651903807414909099441";
		String ECval2 = "4168783608814932154536427934509895782246573715297911553964171371032945126671";
		String ECval3 = "13891744915211034074451795021214165905772212241412891944830863846330766296736";
		String ECval4 = "7937318970632701341203597196594272556916396164729705624521405069090520231616";
		BilinearOperations bops = new BilinearOperations(ECval1, ECval2, ECval3, ECval4, true);
		
		// begin the servers
		
		VotingServer vs;
		AuthenticationServer as;
		try {
			vs = new VotingServer(4001, bops, true);
			as = new AuthenticationServer(4002, bops, true);
			as.serve();
			vs.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
