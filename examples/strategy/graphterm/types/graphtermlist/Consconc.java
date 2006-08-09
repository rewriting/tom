
package strategy.graphterm.types.graphtermlist;

public class Consconc extends strategy.graphterm.types.graphtermlist.conc implements tom.library.strategy.mutraveler.MuVisitable {
  private static Consconc proto = new Consconc();
  private int hashCode;
  private Consconc() {}

  private strategy.graphterm.types.GraphTerm _Headconc;
  private strategy.graphterm.types.GraphTermList _Tailconc;



    /* static constructor */

  public static Consconc make(strategy.graphterm.types.GraphTerm _Headconc, strategy.graphterm.types.GraphTermList _Tailconc) {
    proto.initHashCode( _Headconc,  _Tailconc);
    return (Consconc) shared.SingletonSharedObjectFactory.getInstance().build(proto);
  }



  private void init(strategy.graphterm.types.GraphTerm _Headconc, strategy.graphterm.types.GraphTermList _Tailconc, int hashCode) {
    this._Headconc = _Headconc;
    this._Tailconc = _Tailconc;

    this.hashCode = hashCode;
  }

  private void initHashCode(strategy.graphterm.types.GraphTerm _Headconc, strategy.graphterm.types.GraphTermList _Tailconc) {
    this._Headconc = _Headconc;
    this._Tailconc = _Tailconc;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  public String symbolName() {
    return "Consconc";
  }

  private int getArity() {
    return 2;
  }

  public String toString() {
    return "Consconc("+_Headconc.toString()+","+_Tailconc.toString()+")";
  }

  /**
    * This method implements a lexicographic order
    */
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    strategy.graphterm.GraphTermAbstractType ao = (strategy.graphterm.GraphTermAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    Consconc tco = (Consconc) ao;
    
    int _HeadconcCmp = (this._Headconc).compareToLPO(tco._Headconc);
    if(_HeadconcCmp != 0)
      return _HeadconcCmp;

    int _TailconcCmp = (this._Tailconc).compareToLPO(tco._Tailconc);
    if(_TailconcCmp != 0)
      return _TailconcCmp;

    throw new RuntimeException("Unable to compare");
  }

  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    strategy.graphterm.GraphTermAbstractType ao = (strategy.graphterm.GraphTermAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* use the hash values to discriminate */
    
    if(this.hashCode != ao.hashCode())
      return  (this.hashCode < ao.hashCode())?-1:1;

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* last resort: compare the childs */
    Consconc tco = (Consconc) ao;
    
    int _HeadconcCmp = (this._Headconc).compareTo(tco._Headconc);
    if(_HeadconcCmp != 0)
      return _HeadconcCmp;

    int _TailconcCmp = (this._Tailconc).compareTo(tco._Tailconc);
    if(_TailconcCmp != 0)
      return _TailconcCmp;

    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  public final int hashCode() {
    return hashCode;
  }

  public shared.SharedObject duplicate() {
    Consconc clone = new Consconc();
    clone.init( _Headconc,  _Tailconc, hashCode);
    return clone;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Consconc) {

      Consconc peer = (Consconc) obj;
      return _Headconc==peer._Headconc && _Tailconc==peer._Tailconc && true;
    }
    return false;
  }

  /* GraphTermList interface */
  public boolean isConsconc() {
    return true;
  }


  public strategy.graphterm.types.GraphTerm getHeadconc() {
    return _Headconc;
  }
      
  public strategy.graphterm.types.GraphTermList setHeadconc(strategy.graphterm.types.GraphTerm set_arg) {
    return make(set_arg, _Tailconc);
  }
  public strategy.graphterm.types.GraphTermList getTailconc() {
    return _Tailconc;
  }
      
  public strategy.graphterm.types.GraphTermList setTailconc(strategy.graphterm.types.GraphTermList set_arg) {
    return make(_Headconc, set_arg);
  }


  /* AbstractType */
  public aterm.ATerm toATerm() {
    return aterm.pure.SingletonFactory.getInstance().makeAppl(
      aterm.pure.SingletonFactory.getInstance().makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {getHeadconc().toATerm(), getTailconc().toATerm()});
  }

  public static strategy.graphterm.types.GraphTermList fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(
          strategy.graphterm.types.GraphTerm.fromTerm(appl.getArgument(0)),           strategy.graphterm.types.GraphTermList.fromTerm(appl.getArgument(1))
        );
      }
    }
    return null;
  }


  /* jjtraveler.Visitable */
  public int getChildCount() {
    return 2;
  }

  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _Headconc;
      case 1: return _Tailconc;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    switch(index) {
      case 0: return make((strategy.graphterm.types.GraphTerm) v, _Tailconc);
      case 1: return make(_Headconc, (strategy.graphterm.types.GraphTermList) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChilds(jjtraveler.Visitable[] childs) {
    if (childs.length == 2) {
      return make((strategy.graphterm.types.GraphTerm) childs[0], (strategy.graphterm.types.GraphTermList) childs[1]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }


      /* internal use */
  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (771459600<<8);
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */

    a += (_Headconc.hashCode() << 8);
    a += (_Tailconc.hashCode());


    a -= b;
    a -= c;
    a ^= (c >> 13);
    b -= c;
    b -= a;
    b ^= (a << 8);
    c -= a;
    c -= b;
    c ^= (b >> 13);
    a -= b;
    a -= c;
    a ^= (c >> 12);
    b -= c;
    b -= a;
    b ^= (a << 16);
    c -= a;
    c -= b;
    c ^= (b >> 5);
    a -= b;
    a -= c;
    a ^= (c >> 3);
    b -= c;
    b -= a;
    b ^= (a << 10);
    c -= a;
    c -= b;
    c ^= (b >> 15);

    /*-------------------------------------------- report the result */
    return c;
  }


}
