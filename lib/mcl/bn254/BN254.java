/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.6
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package mcl.bn254;

public class BN254 {
  public static void SystemInit() {
    BN254JNI.SystemInit();
  }

  public static Mpz GetParamR() {
    return new Mpz(BN254JNI.GetParamR(), false);
  }

}