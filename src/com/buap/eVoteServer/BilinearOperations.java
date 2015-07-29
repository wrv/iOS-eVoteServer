package com.buap.eVoteServer;

/*Bilinear Operations 
 * All the bilinear operations needed to authenticate and verify voting
 * 
 * Example code source: 
 * 		https://github.com/herumi/ate-pairing/blob/master/java/BN254Test.java 
 */

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import mcl.bn254.*;

public class BilinearOperations {
	static {
		System.loadLibrary("bn254_if_wrap");
	}
	
	//private Ec1 P; //P point on Ec1
	public Ec2 Q; //Q point on EC2
	private Mpz r; //Order r of groups
	private Mpz sk; //Secret key of Authentication Server
	public Ec2 pk; //public Alias of authentication server
	
	public BilinearOperations(String ECval1, String ECval2, String ECval3, String ECval4, boolean test){
		try{
			BN254.SystemInit();
			r = BN254.GetParamR(); 
			sk = new Mpz(randNum());
			//P = new Ec1(new Fp(-1), new Fp(1));
			Q = new Ec2(new Fp2(new Fp(ECval1), new Fp(ECval2)), new Fp2(new Fp(ECval3), new Fp(ECval4)));
			pk = new Ec2(Q);
			pk.mul(sk);
			
			//Verify everything runs well
			if(test){
				testLibrary(ECval1, ECval2, ECval3, ECval4);
			}
		} catch (RuntimeException e) {
			System.out.println("unknown exception :" + e);
		}
	}
	
	public String randNum(){
		Random random = new Random();
		float curran = (float) random.nextDouble();

		BigDecimal rVal = new BigDecimal(r.toString());
		rVal = rVal.multiply(new BigDecimal(curran)).setScale(0, BigDecimal.ROUND_HALF_UP);
		return rVal.toString();
	}
	
	public boolean verifyPairing(String G1left, String G2right, String G1right){
		Fp12 ea1 = new Fp12(); //lhs pairing
		Fp12 ea2 = new Fp12(); //rhs pairing
		
		Ec1 g1a = new Ec1(); //lhs value that goes with Q
		Ec1 g1b = new Ec1(); //rhs G1 value
		Ec2 g2a = new Ec2(); //rhs G2 value
		
		g1a.set(G1left);
		g1b.set(G1right);
		g2a.set(G2right);
		
		ea1.pairing(Q, g1a);
		ea2.pairing(g2a, g1b);
		
		return ea1.equals(ea2);
	}
	
	public String signMessage(String Message){
		Ec1 SignedMessage = new Ec1();
		SignedMessage.set(Message);
		SignedMessage.mul(sk);
		
		return SignedMessage.toString();
	}
	
	public String m2s(String point){
		//TODO implement changing a point to a string of length 4l
		return "";
	}
	
	public String H1(String text){
		//TODO implement a function that hashes a string to a point in Ec1
		return new Ec1().toString();
	}
	
	public String H(String message){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update(message.getBytes());
	        byte byteData[] = md.digest();
	        
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        return sb.toString();
	        
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "Error";
		}
	}
	
	private void testLibrary(String ECval1, String ECval2, String ECval3, String ECval4){
		try{
			BN254.SystemInit();
			
			Fp aa = new Fp(ECval1);
			Fp ab = new Fp(ECval2);
			Fp ba = new Fp(ECval3);
			Fp bb = new Fp(ECval4);
			Ec1 g1 = new Ec1(new Fp(-1), new Fp(1));
			Ec2 g2 = new Ec2(new Fp2(aa, ab), new Fp2(ba, bb));
			System.out.println("g1=" + g1);
			System.out.println("g1=" + g1.toString());
			System.out.println("g2=" + g2);
			assertBool("g1 is on EC", g1.isValid());
			assertBool("g2 is on twist EC", g2.isValid());
			Mpz r = BN254.GetParamR();
			System.out.println("r=" + r);

			{
				Ec1 t = new Ec1(g1);
				System.out.println("t=" + t);
				t.mul(r);
				assertBool("order of g1 == r", t.isZero());
			}
			{
				Ec2 t = new Ec2(g2);
				System.out.println("t=" + t);
				t.mul(r);
				assertBool("order of g2 == r", t.isZero());
			}
			Mpz a = new Mpz("123456789012345");
			Mpz b = new Mpz("998752342342342342424242421");
			// scalar-multiplication sample
			{
				Mpz c = new Mpz(a);
				c.add(b);
				Ec1 Pa = new Ec1(g1); Pa.mul(a);
				Ec1 Pb = new Ec1(g1); Pb.mul(b);
				Ec1 Pc = new Ec1(g1); Pc.mul(c);
				Ec1 out = new Ec1(Pa);
				out.add(Pb);
				assertEqual("check g1 * c = g1 * a + g1 * b", Pc, out);
			}
			Fp12 e = new Fp12();
			// calc e : G2 x G1 -> G3 pairing
			e.pairing(g2, g1); // e = e(g2, g1)
			System.out.println("e=" + e);
			{
				Fp12 t = new Fp12(e);
				t.power(r);
				assertEqual("order of e == r", t, new Fp12(1));
			}
			Ec2 g2a = new Ec2(g2);
			g2a.mul(a);
			Fp12 ea1 = new Fp12();
			ea1.pairing(g2a, g1);
			Fp12 ea2 = new Fp12(e);
			ea2.power(a); // ea2 = e^a
			assertEqual("e(g2 * a, g1) = e(g2, g1)^a", ea1, ea2);

			Ec1 q1 = new Ec1(g1);
			q1.mul(new Mpz(12345));
			assertBool("q1 is on EC", q1.isValid());
			Fp12 e1 = new Fp12();
			Fp12 e2 = new Fp12();
			e1.pairing(g2, g1); // e1 = e(g2, g1)
			e2.pairing(g2, q1); // e2 = e(g2, q1)
			Ec1 q2 = new Ec1(g1);
			q2.add(q1);
			e.pairing(g2, q2); // e = e(g2, q2)
			e1.mul(e2);
			assertEqual("e = e1 * e2", e, e1);
		} catch (RuntimeException e) {
			System.out.println("unknown exception :" + e);
		}
	}
	
	public static void assertBool(String msg, Boolean b) {
		if (b) {
			System.out.println("OK : " + msg);
		} else {
			System.out.println("NG : " + msg);
		}
	}
	public static void assertEqual(String msg, Object lhs, Object rhs) {
		if (lhs.equals(rhs)) {
			System.out.println("OK : " + msg);
		} else {
			System.out.println("NG : " + msg + ", lhs = " + lhs + ", rhs = " + rhs);
		}
	}
	
}
