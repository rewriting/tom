
package sl.jjterm.types.nat;


public final class F extends sl.jjterm.types.Nat {
  
  private static String symbolName = "F";


  private F() {}
  private int hashCode;
  private static F proto = new F();
    private sl.jjterm.types.Nat _n1;
  private sl.jjterm.types.Nat _n2;
  private sl.jjterm.types.Nat _n3;
  private sl.jjterm.types.Nat _n4;
  private sl.jjterm.types.Nat _n5;

    /* static constructor */

  public static F make(sl.jjterm.types.Nat _n1, sl.jjterm.types.Nat _n2, sl.jjterm.types.Nat _n3, sl.jjterm.types.Nat _n4, sl.jjterm.types.Nat _n5) {

    // use the proto as a model
    proto.initHashCode( _n1,  _n2,  _n3,  _n4,  _n5);
    return (F) factory.build(proto);

  }

  private void init(sl.jjterm.types.Nat _n1, sl.jjterm.types.Nat _n2, sl.jjterm.types.Nat _n3, sl.jjterm.types.Nat _n4, sl.jjterm.types.Nat _n5, int hashCode) {
    this._n1 = _n1;
    this._n2 = _n2;
    this._n3 = _n3;
    this._n4 = _n4;
    this._n5 = _n5;

    this.hashCode = hashCode;
  }

  private void initHashCode(sl.jjterm.types.Nat _n1, sl.jjterm.types.Nat _n2, sl.jjterm.types.Nat _n3, sl.jjterm.types.Nat _n4, sl.jjterm.types.Nat _n5) {
    this._n1 = _n1;
    this._n2 = _n2;
    this._n3 = _n3;
    this._n4 = _n4;
    this._n5 = _n5;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "F";
  }

  private int getArity() {
    return 5;
  }

  public shared.SharedObject duplicate() {
    F clone = new F();
    clone.init( _n1,  _n2,  _n3,  _n4,  _n5, hashCode);
    return clone;
  }
  
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("F(");
    _n1.toStringBuilder(buffer);
buffer.append(",");
    _n2.toStringBuilder(buffer);
buffer.append(",");
    _n3.toStringBuilder(buffer);
buffer.append(",");
    _n4.toStringBuilder(buffer);
buffer.append(",");
    _n5.toStringBuilder(buffer);

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
    F tco = (F) ao;
    int _n1Cmp = (this._n1).compareToLPO(tco._n1);
    if(_n1Cmp != 0)
      return _n1Cmp;

    int _n2Cmp = (this._n2).compareToLPO(tco._n2);
    if(_n2Cmp != 0)
      return _n2Cmp;

    int _n3Cmp = (this._n3).compareToLPO(tco._n3);
    if(_n3Cmp != 0)
      return _n3Cmp;

    int _n4Cmp = (this._n4).compareToLPO(tco._n4);
    if(_n4Cmp != 0)
      return _n4Cmp;

    int _n5Cmp = (this._n5).compareToLPO(tco._n5);
    if(_n5Cmp != 0)
      return _n5Cmp;

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
    F tco = (F) ao;
    int _n1Cmp = (this._n1).compareTo(tco._n1);
    if(_n1Cmp != 0)
      return _n1Cmp;

    int _n2Cmp = (this._n2).compareTo(tco._n2);
    if(_n2Cmp != 0)
      return _n2Cmp;

    int _n3Cmp = (this._n3).compareTo(tco._n3);
    if(_n3Cmp != 0)
      return _n3Cmp;

    int _n4Cmp = (this._n4).compareTo(tco._n4);
    if(_n4Cmp != 0)
      return _n4Cmp;

    int _n5Cmp = (this._n5).compareTo(tco._n5);
    if(_n5Cmp != 0)
      return _n5Cmp;

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof F) {

      F peer = (F) obj;
      return _n1==peer._n1 && _n2==peer._n2 && _n3==peer._n3 && _n4==peer._n4 && _n5==peer._n5 && true;
    }
    return false;
  }


   //Nat interface
  @Override
  public boolean isF() {
    return true;
  }
  
  @Override
  public sl.jjterm.types.Nat getn1() {
    return _n1;
  }
  
  @Override
  public sl.jjterm.types.Nat setn1(sl.jjterm.types.Nat set_arg) {
    return make(set_arg, _n2, _n3, _n4, _n5);
  }
  @Override
  public sl.jjterm.types.Nat getn2() {
    return _n2;
  }
  
  @Override
  public sl.jjterm.types.Nat setn2(sl.jjterm.types.Nat set_arg) {
    return make(_n1, set_arg, _n3, _n4, _n5);
  }
  @Override
  public sl.jjterm.types.Nat getn3() {
    return _n3;
  }
  
  @Override
  public sl.jjterm.types.Nat setn3(sl.jjterm.types.Nat set_arg) {
    return make(_n1, _n2, set_arg, _n4, _n5);
  }
  @Override
  public sl.jjterm.types.Nat getn4() {
    return _n4;
  }
  
  @Override
  public sl.jjterm.types.Nat setn4(sl.jjterm.types.Nat set_arg) {
    return make(_n1, _n2, _n3, set_arg, _n5);
  }
  @Override
  public sl.jjterm.types.Nat getn5() {
    return _n5;
  }
  
  @Override
  public sl.jjterm.types.Nat setn5(sl.jjterm.types.Nat set_arg) {
    return make(_n1, _n2, _n3, _n4, set_arg);
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
      new aterm.ATerm[] {getn1().toATerm(), getn2().toATerm(), getn3().toATerm(), getn4().toATerm(), getn5().toATerm()});
  }

  public static sl.jjterm.types.Nat fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
        return make(
sl.jjterm.types.Nat.fromTerm(appl.getArgument(0)), sl.jjterm.types.Nat.fromTerm(appl.getArgument(1)), sl.jjterm.types.Nat.fromTerm(appl.getArgument(2)), sl.jjterm.types.Nat.fromTerm(appl.getArgument(3)), sl.jjterm.types.Nat.fromTerm(appl.getArgument(4))
        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 5;
  }

  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _n1;
      case 1: return _n2;
      case 2: return _n3;
      case 3: return _n4;
      case 4: return _n5;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    switch(index) {
      case 0: return make((sl.jjterm.types.Nat) v, _n2, _n3, _n4, _n5);
      case 1: return make(_n1, (sl.jjterm.types.Nat) v, _n3, _n4, _n5);
      case 2: return make(_n1, _n2, (sl.jjterm.types.Nat) v, _n4, _n5);
      case 3: return make(_n1, _n2, _n3, (sl.jjterm.types.Nat) v, _n5);
      case 4: return make(_n1, _n2, _n3, _n4, (sl.jjterm.types.Nat) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildren(jjtraveler.Visitable[] childs) {
    if (childs.length == 5) {
      return make((sl.jjterm.types.Nat) childs[0], (sl.jjterm.types.Nat) childs[1], (sl.jjterm.types.Nat) childs[2], (sl.jjterm.types.Nat) childs[3], (sl.jjterm.types.Nat) childs[4]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable[] getChildren() {
    return new jjtraveler.Visitable[] {  _n1,  _n2,  _n3,  _n4,  _n5 };
  }

    /* internal use */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (392399274<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    b += (_n1.hashCode());
    a += (_n2.hashCode() << 24);
    a += (_n3.hashCode() << 16);
    a += (_n4.hashCode() << 8);
    a += (_n5.hashCode());

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
