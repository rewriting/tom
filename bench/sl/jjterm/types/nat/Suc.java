
package sl.jjterm.types.nat;


public final class Suc extends sl.jjterm.types.Nat {
  
  private static String symbolName = "Suc";


  private Suc() {}
  private int hashCode;
  private static Suc proto = new Suc();
    private sl.jjterm.types.Nat _n;

    /* static constructor */

  public static Suc make(sl.jjterm.types.Nat _n) {

    // use the proto as a model
    proto.initHashCode( _n);
    return (Suc) factory.build(proto);

  }

  private void init(sl.jjterm.types.Nat _n, int hashCode) {
    this._n = _n;

    this.hashCode = hashCode;
  }

  private void initHashCode(sl.jjterm.types.Nat _n) {
    this._n = _n;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "Suc";
  }

  private int getArity() {
    return 1;
  }

  public shared.SharedObject duplicate() {
    Suc clone = new Suc();
    clone.init( _n, hashCode);
    return clone;
  }
  
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Suc(");
    _n.toStringBuilder(buffer);

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
    Suc tco = (Suc) ao;
    int _nCmp = (this._n).compareToLPO(tco._n);
    if(_nCmp != 0)
      return _nCmp;

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
    Suc tco = (Suc) ao;
    int _nCmp = (this._n).compareTo(tco._n);
    if(_nCmp != 0)
      return _nCmp;

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Suc) {

      Suc peer = (Suc) obj;
      return _n==peer._n && true;
    }
    return false;
  }


   //Nat interface
  @Override
  public boolean isSuc() {
    return true;
  }
  
  @Override
  public sl.jjterm.types.Nat getn() {
    return _n;
  }
  
  @Override
  public sl.jjterm.types.Nat setn(sl.jjterm.types.Nat set_arg) {
    return make(set_arg);
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
      new aterm.ATerm[] {getn().toATerm()});
  }

  public static sl.jjterm.types.Nat fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
        return make(
sl.jjterm.types.Nat.fromTerm(appl.getArgument(0))
        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 1;
  }

  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _n;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    switch(index) {
      case 0: return make((sl.jjterm.types.Nat) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildren(jjtraveler.Visitable[] childs) {
    if (childs.length == 1) {
      return make((sl.jjterm.types.Nat) childs[0]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable[] getChildren() {
    return new jjtraveler.Visitable[] {  _n };
  }

    /* internal use */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-1117885413<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_n.hashCode());

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
