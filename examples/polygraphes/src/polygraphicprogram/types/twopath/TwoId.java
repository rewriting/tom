
package polygraphicprogram.types.twopath;


public final class TwoId extends polygraphicprogram.types.TwoPath implements tom.library.sl.Visitable  {

  private TwoId() {}

  private int hashCode;
  private static TwoId proto = new TwoId();
  private polygraphicprogram.types.OnePath _onePath;

    /* static constructor */

  public static TwoId make(polygraphicprogram.types.OnePath _onePath) {
    proto.initHashCode( _onePath);
    return (TwoId) factory.build(proto);
  }
  
  private void init(polygraphicprogram.types.OnePath _onePath, int hashCode) {
    this._onePath = _onePath;

    this.hashCode = hashCode;
  }

  private void initHashCode(polygraphicprogram.types.OnePath _onePath) {
    this._onePath = _onePath;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "TwoId";
  }

  private int getArity() {
    return 1;
  }

  public shared.SharedObject duplicate() {
    TwoId clone = new TwoId();
    clone.init( _onePath, hashCode);
    return clone;
  }


  @Override
  public void toStringBuffer(java.lang.StringBuffer buffer) {
    buffer.append("TwoId(");
    _onePath.toStringBuffer(buffer);

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
    polygraphicprogram.PolygraphicProgramAbstractType ao = (polygraphicprogram.PolygraphicProgramAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    TwoId tco = (TwoId) ao;
    int _onePathCmp = (this._onePath).compareToLPO(tco._onePath);
    if(_onePathCmp != 0)
      return _onePathCmp;

    throw new RuntimeException("Unable to compare");
  }

  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    polygraphicprogram.PolygraphicProgramAbstractType ao = (polygraphicprogram.PolygraphicProgramAbstractType) o;
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
    TwoId tco = (TwoId) ao;
    int _onePathCmp = (this._onePath).compareTo(tco._onePath);
    if(_onePathCmp != 0)
      return _onePathCmp;

    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof TwoId) {

      TwoId peer = (TwoId) obj;
      return _onePath==peer._onePath && true;
    }
    return false;
  }

  /* TwoPath interface */
  @Override
  public boolean isTwoId() {
    return true;
  }

  @Override
  public polygraphicprogram.types.OnePath getonePath() {
    return _onePath;
  }
      
  @Override
  public polygraphicprogram.types.TwoPath setonePath(polygraphicprogram.types.OnePath set_arg) {
    return make(set_arg);
  }
  /* AbstractType */
  @Override
  public aterm.ATerm toATerm() {
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {getonePath().toATerm()});
  }

  public static polygraphicprogram.types.TwoPath fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(
polygraphicprogram.types.OnePath.fromTerm(appl.getArgument(0))
        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 1;
  }

  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _onePath;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
      case 0: return make((polygraphicprogram.types.OnePath) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 1) {
      return make((polygraphicprogram.types.OnePath) childs[0]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  _onePath };
  }

    /* internal use */
  protected  int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (934126619<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_onePath.hashCode());

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
