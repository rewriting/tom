
package sl.jjterm.types;        

public abstract class Nat extends sl.jjterm.termAbstractType  {

  public boolean isZero() {
    return false;
  }

  public boolean isSuc() {
    return false;
  }

  public boolean isC() {
    return false;
  }

  public boolean isF() {
    return false;
  }

  public boolean isM() {
    return false;
  }

  public boolean isN() {
    return false;
  }

  public boolean isP() {
    return false;
  }

  public sl.jjterm.types.Nat getn5() {
    throw new UnsupportedOperationException("This Nat has no n5");
  }

  public Nat setn5(sl.jjterm.types.Nat _arg) {
    throw new UnsupportedOperationException("This Nat has no n5");
  }

  public sl.jjterm.types.Nat getn3() {
    throw new UnsupportedOperationException("This Nat has no n3");
  }

  public Nat setn3(sl.jjterm.types.Nat _arg) {
    throw new UnsupportedOperationException("This Nat has no n3");
  }

  public sl.jjterm.types.Nat getn1() {
    throw new UnsupportedOperationException("This Nat has no n1");
  }

  public Nat setn1(sl.jjterm.types.Nat _arg) {
    throw new UnsupportedOperationException("This Nat has no n1");
  }

  public sl.jjterm.types.Nat getn4() {
    throw new UnsupportedOperationException("This Nat has no n4");
  }

  public Nat setn4(sl.jjterm.types.Nat _arg) {
    throw new UnsupportedOperationException("This Nat has no n4");
  }

  public sl.jjterm.types.Nat getn() {
    throw new UnsupportedOperationException("This Nat has no n");
  }

  public Nat setn(sl.jjterm.types.Nat _arg) {
    throw new UnsupportedOperationException("This Nat has no n");
  }

  public sl.jjterm.types.Nat getn2() {
    throw new UnsupportedOperationException("This Nat has no n2");
  }

  public Nat setn2(sl.jjterm.types.Nat _arg) {
    throw new UnsupportedOperationException("This Nat has no n2");
  }

  public aterm.ATerm toATerm() {
    // returns null to indicates sub-classes that they have to work
    return null;
  }

  public static sl.jjterm.types.Nat fromTerm(aterm.ATerm trm) {
    sl.jjterm.types.Nat tmp;

    tmp = sl.jjterm.types.nat.Zero.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = sl.jjterm.types.nat.Suc.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = sl.jjterm.types.nat.C.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = sl.jjterm.types.nat.F.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = sl.jjterm.types.nat.M.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = sl.jjterm.types.nat.N.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = sl.jjterm.types.nat.P.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    throw new IllegalArgumentException("This is not a Nat " + trm);
  }

  public static sl.jjterm.types.Nat fromString(String s) {
    return fromTerm(atermFactory.parse(s));
  }

  public static sl.jjterm.types.Nat fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream));
  }

  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public sl.jjterm.types.Nat reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  /**
   * Collection
   */
  /*
  public boolean add(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public void clear() {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean containsAll(java.util.Collection c) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean contains(Object o) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean equals(Object o) { return this == o; }

  public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return false; }

  public java.util.Iterator iterator() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean remove(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean removeAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean retainAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public int size() { return length(); }

  public Object[] toArray() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public Object[] toArray(Object[] a) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
  */
  
}
