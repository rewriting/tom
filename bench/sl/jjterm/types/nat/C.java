
package sl.jjterm.types.nat;


public final class C extends sl.jjterm.types.Nat {
  
  private static String symbolName = "C";


  private C() {}
  private int hashCode;
  private static C proto = new C();
    private sl.jjterm.types.Nat _n1;
  private sl.jjterm.types.Nat _n2;

    /* static constructor */

  public static C make(sl.jjterm.types.Nat _n1, sl.jjterm.types.Nat _n2) {

    // use the proto as a model
    proto.initHashCode( _n1,  _n2);
    return (C) factory.build(proto);

  }

  private void init(sl.jjterm.types.Nat _n1, sl.jjterm.types.Nat _n2, int hashCode) {
    this._n1 = _n1;
    this._n2 = _n2;

    this.hashCode = hashCode;
  }

  private void initHashCode(sl.jjterm.types.Nat _n1, sl.jjterm.types.Nat _n2) {
    this._n1 = _n1;
    this._n2 = _n2;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "C";
  }

  private int getArity() {
    return 2;
  }

  public shared.SharedObject duplicate() {
    C clone = new C();
    clone.init( _n1,  _n2, hashCode);
    return clone;
  }
  
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("C(");
    _n1.toStringBuilder(buffer);
buffer.append(",");
    _n2.toStringBuilder(buffer);

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
    C tco = (C) ao;
    int _n1Cmp = (this._n1).compareToLPO(tco._n1);
    if(_n1Cmp != 0)
      return _n1Cmp;

    int _n2Cmp = (this._n2).compareToLPO(tco._n2);
    if(_n2Cmp != 0)
      return _n2Cmp;

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
    C tco = (C) ao;
    int _n1Cmp = (this._n1).compareTo(tco._n1);
    if(_n1Cmp != 0)
      return _n1Cmp;

    int _n2Cmp = (this._n2).compareTo(tco._n2);
    if(_n2Cmp != 0)
      return _n2Cmp;

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof C) {

      C peer = (C) obj;
      return _n1==peer._n1 && _n2==peer._n2 && true;
    }
    return false;
  }


   //Nat interface
  @Override
  public boolean isC() {
    return true;
  }
  
  @Override
  public sl.jjterm.types.Nat getn1() {
    return _n1;
  }
  
  @Override
  public sl.jjterm.types.Nat setn1(sl.jjterm.types.Nat set_arg) {
    return make(set_arg, _n2);
  }
  @Override
  public sl.jjterm.types.Nat getn2() {
    return _n2;
  }
  
  @Override
  public sl.jjterm.types.Nat setn2(sl.jjterm.types.Nat set_arg) {
    return make(_n1, set_arg);
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
      new aterm.ATerm[] {getn1().toATerm(), getn2().toATerm()});
  }

  public static sl.jjterm.types.Nat fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
        return make(
sl.jjterm.types.Nat.fromTerm(appl.getArgument(0)), sl.jjterm.types.Nat.fromTerm(appl.getArgument(1))
        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 2;
  }

  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _n1;
      case 1: return _n2;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    switch(index) {
      case 0: return make((sl.jjterm.types.Nat) v, _n2);
      case 1: return make(_n1, (sl.jjterm.types.Nat) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildren(jjtraveler.Visitable[] childs) {
    if (childs.length == 2) {
      return make((sl.jjterm.types.Nat) childs[0], (sl.jjterm.types.Nat) childs[1]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable[] getChildren() {
    return new jjtraveler.Visitable[] {  _n1,  _n2 };
  }

    /* internal use */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (1804997859<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_n1.hashCode() << 8);
    a += (_n2.hashCode());

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
