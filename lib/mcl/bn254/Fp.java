/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.6
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package mcl.bn254;

public class Fp {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Fp(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Fp obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        BN254JNI.delete_Fp(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Fp)) return false;
    return equals((Fp)obj);
  }

  public Fp() {
    this(BN254JNI.new_Fp__SWIG_0(), true);
  }

  public Fp(Fp x) {
    this(BN254JNI.new_Fp__SWIG_1(Fp.getCPtr(x), x), true);
  }

  public Fp(int x) {
    this(BN254JNI.new_Fp__SWIG_2(x), true);
  }

  public Fp(String str) {
    this(BN254JNI.new_Fp__SWIG_3(str), true);
  }

  public void set(int x) {
    BN254JNI.Fp_set__SWIG_0(swigCPtr, this, x);
  }

  public void set(String str) {
    BN254JNI.Fp_set__SWIG_1(swigCPtr, this, str);
  }

  public String toString() {
    return BN254JNI.Fp_toString(swigCPtr, this);
  }

  public boolean equals(Fp rhs) {
    return BN254JNI.Fp_equals(swigCPtr, this, Fp.getCPtr(rhs), rhs);
  }

  public void add(Fp rhs) {
    BN254JNI.Fp_add(swigCPtr, this, Fp.getCPtr(rhs), rhs);
  }

  public void sub(Fp rhs) {
    BN254JNI.Fp_sub(swigCPtr, this, Fp.getCPtr(rhs), rhs);
  }

  public void mul(Fp rhs) {
    BN254JNI.Fp_mul(swigCPtr, this, Fp.getCPtr(rhs), rhs);
  }

  public void power(Mpz x) {
    BN254JNI.Fp_power(swigCPtr, this, Mpz.getCPtr(x), x);
  }

}
