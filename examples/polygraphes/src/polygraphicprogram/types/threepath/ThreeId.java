
package polygraphicprogram.types.threepath;


public final class ThreeId extends polygraphicprogram.types.ThreePath implements tom.library.sl.Visitable  {

  private ThreeId() {}

  private int hashCode;
  private static ThreeId proto = new ThreeId();
  private polygraphicprogram.types.TwoPath _twoPath;

    /* static constructor */

  public static ThreeId make(polygraphicprogram.types.TwoPath _twoPath) {
    proto.initHashCode( _twoPath);
    return (ThreeId) factory.build(proto);
  }
  
  private void init(polygraphicprogram.types.TwoPath _twoPath, int hashCode) {
    this._twoPath = _twoPath;

    this.hashCode = hashCode;
  }

  private void initHashCode(polygraphicprogram.types.TwoPath _twoPath) {
    this._twoPath = _twoPath;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "ThreeId";
  }

  private int getArity() {
    return 1;
  }

  public shared.SharedObject duplicate() {
    ThreeId clone = new ThreeId();
    clone.init( _twoPath, hashCode);
    return clone;
  }


  @Override
  public void toStringBuffer(java.lang.StringBuffer buffer) {
    buffer.append("ThreeId(");
    _twoPath.toStringBuffer(buffer);

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
    ThreeId tco = (ThreeId) ao;
    int _twoPathCmp = (this._twoPath).compareToLPO(tco._twoPath);
    if(_twoPathCmp != 0)
      return _twoPathCmp;

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
    ThreeId tco = (ThreeId) ao;
    int _twoPathCmp = (this._twoPath).compareTo(tco._twoPath);
    if(_twoPathCmp != 0)
      return _twoPathCmp;

    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ThreeId) {

      ThreeId peer = (ThreeId) obj;
      return _twoPath==peer._twoPath && true;
    }
    return false;
  }

  /* ThreePath interface */
  @Override
  public boolean isThreeId() {
    return true;
  }

  @Override
  public polygraphicprogram.types.TwoPath gettwoPath() {
    return _twoPath;
  }
      
  @Override
  public polygraphicprogram.types.ThreePath settwoPath(polygraphicprogram.types.TwoPath set_arg) {
    return make(set_arg);
  }
  /* AbstractType */
  @Override
  public aterm.ATerm toATerm() {
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {gettwoPath().toATerm()});
  }

  public static polygraphicprogram.types.ThreePath fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(
polygraphicprogram.types.TwoPath.fromTerm(appl.getArgument(0))
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
      case 0: return _twoPath;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
      case 0: return make((polygraphicprogram.types.TwoPath) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 1) {
      return make((polygraphicprogram.types.TwoPath) childs[0]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  _twoPath };
  }

    /* internal use */
  protected  int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-879924460<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_twoPath.hashCode());

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
