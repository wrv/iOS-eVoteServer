/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.6
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package mcl.bn254;

public class Ec2 {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Ec2(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Ec2 obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        BN254JNI.delete_Ec2(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Ec2)) return false;
    return equals((Ec2)obj);
  }

  public Ec2() {
    this(BN254JNI.new_Ec2__SWIG_0(), true);
  }

  public Ec2(Ec2 x) {
    this(BN254JNI.new_Ec2__SWIG_1(Ec2.getCPtr(x), x), true);
  }

  public Ec2(Fp2 x, Fp2 y) {
    this(BN254JNI.new_Ec2__SWIG_2(Fp2.getCPtr(x), x, Fp2.getCPtr(y), y), true);
  }

  public Ec2(Fp2 x, Fp2 y, Fp2 z) {
    this(BN254JNI.new_Ec2__SWIG_3(Fp2.getCPtr(x), x, Fp2.getCPtr(y), y, Fp2.getCPtr(z), z), true);
  }

  public boolean isValid() {
    return BN254JNI.Ec2_isValid(swigCPtr, this);
  }

  public void set(Fp2 x, Fp2 y) {
    BN254JNI.Ec2_set__SWIG_0(swigCPtr, this, Fp2.getCPtr(x), x, Fp2.getCPtr(y), y);
  }

  public void set(Fp2 x, Fp2 y, Fp2 z) {
    BN254JNI.Ec2_set__SWIG_1(swigCPtr, this, Fp2.getCPtr(x), x, Fp2.getCPtr(y), y, Fp2.getCPtr(z), z);
  }

  public void set(String str) {
    BN254JNI.Ec2_set__SWIG_2(swigCPtr, this, str);
  }

  public String toString() {
    return BN254JNI.Ec2_toString(swigCPtr, this);
  }

  public boolean equals(Ec2 rhs) {
    return BN254JNI.Ec2_equals(swigCPtr, this, Ec2.getCPtr(rhs), rhs);
  }

  public boolean isZero() {
    return BN254JNI.Ec2_isZero(swigCPtr, this);
  }

  public void clear() {
    BN254JNI.Ec2_clear(swigCPtr, this);
  }

  public void dbl() {
    BN254JNI.Ec2_dbl(swigCPtr, this);
  }

  public void neg() {
    BN254JNI.Ec2_neg(swigCPtr, this);
  }

  public void add(Ec2 rhs) {
    BN254JNI.Ec2_add(swigCPtr, this, Ec2.getCPtr(rhs), rhs);
  }

  public void sub(Ec2 rhs) {
    BN254JNI.Ec2_sub(swigCPtr, this, Ec2.getCPtr(rhs), rhs);
  }

  public void mul(Mpz rhs) {
    BN254JNI.Ec2_mul(swigCPtr, this, Mpz.getCPtr(rhs), rhs);
  }

  public Fp2 getX() {
    return new Fp2(BN254JNI.Ec2_getX(swigCPtr, this), false);
  }

  public Fp2 getY() {
    return new Fp2(BN254JNI.Ec2_getY(swigCPtr, this), false);
  }

  public Fp2 getZ() {
    return new Fp2(BN254JNI.Ec2_getZ(swigCPtr, this), false);
  }

}
