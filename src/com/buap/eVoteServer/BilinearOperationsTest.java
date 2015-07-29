package com.buap.eVoteServer;

import static org.junit.Assert.*;
import mcl.bn254.Ec1;
import mcl.bn254.Ec2;
import mcl.bn254.Fp;
import mcl.bn254.Fp12;
import mcl.bn254.Mpz;

import org.junit.Before;
import org.junit.Test;

public class BilinearOperationsTest {
	
	public BilinearOperations bops;
	
	@Before
	public void setUp() throws Exception {
		String ECval1 = "12723517038133731887338407189719511622662176727675373276651903807414909099441";
		String ECval2 = "4168783608814932154536427934509895782246573715297911553964171371032945126671";
		String ECval3 = "13891744915211034074451795021214165905772212241412891944830863846330766296736";
		String ECval4 = "7937318970632701341203597196594272556916396164729705624521405069090520231616";
		
		bops = new BilinearOperations(ECval1, ECval2, ECval3, ECval4, false);
	}

	@Test
	public void testRandNum() {
		String rand1 = bops.randNum();
		String rand2 = bops.randNum();

		assertFalse(rand1.equals(rand2));
	}

	@Test
	public void testVerifyPairing() {
		
		Ec1 g1 = new Ec1(new Fp(-1), new Fp(1));
		Ec2 g2 = new Ec2(bops.Q);
		
		Fp12 e = new Fp12();
		Mpz a = new Mpz("123456789012345");
		e.pairing(g2, g1); // e = e(g2, g1)
		Ec2 g2a = new Ec2(g2);
		g2a.mul(a);
		Fp12 ea1 = new Fp12();
		ea1.pairing(g2a, g1);
		Fp12 ea2 = new Fp12(e);
		ea2.power(a); // ea2 = e^a
		
		assertTrue(ea2.equals(ea1));
		
	}

	@Test
	public void testSignMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testM2s() {
		fail("Not yet implemented");
	}

	@Test
	public void testH1() {
		fail("Not yet implemented");
	}

	@Test
	public void testH() {
		String correctHash = "a9c43be948c5cabd56ef2bacffb77cdaa5eec49dd5eb0cc4129cf3eda5f0e74c";
		String response = bops.H("dragon");
		
		assertTrue(correctHash.equals(response));
	}

}
