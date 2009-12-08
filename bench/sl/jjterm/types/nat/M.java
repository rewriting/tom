
package sl.jjterm.types.nat;


public final class M extends sl.jjterm.types.Nat {
  
  private static String symbolName = "M";


  private M() {}
  private static int hashCode = hashFunction();
  private static M proto = (M) factory.build(new M());
  
    /* static constructor */

  public static M make() {

    return proto;

  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "M";
  }

  private static int getArity() {
    return 0;
  }


  public shared.SharedObject duplicate() {
    // the proto is a constant object: no need to clone it
    return this;
    //return new M();
  }

  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("M(");
    
    buffer.append(")");
  }


  /**
    * This method implements a lexicographic order
    */
  @Override
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    sl.jjterm.termAbstractType ao = (sl.jjterm.termAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    
    throw new RuntimeException("Unable to compare");
  }

  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    sl.jjterm.termAbstractType ao = (sl.jjterm.termAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* use the hash values to discriminate */
    
    if(hashCode != ao.hashCode())
      return (hashCode < ao.hashCode())?-1:1;

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* last resort: compare the childs */
    
    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof M) {

      return true;
    }
    return false;
  }


   //Nat interface
  @Override
  public boolean isM() {
    return true;
  }
  
  /* AbstractType */
  @Override
  public aterm.ATerm toATerm() {
    aterm.ATerm res = super.toATerm();
    if(res != null) {
      // the super class has produced an ATerm (may be a variadic operator)
      return res;
    }
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {});
  }

  public static sl.jjterm.types.Nat fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
        return make(

        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 0;
  }

  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    switch(index) {

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildren(jjtraveler.Visitable[] childs) {
    if (childs.length == 0) {
      return make();
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable[] getChildren() {
    return new jjtraveler.Visitable[] {  };
  }

    /* internal use */
  protected static int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-1162359008<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);
    /* ------------------------------------------- report the result */
    return c;
  }

}
