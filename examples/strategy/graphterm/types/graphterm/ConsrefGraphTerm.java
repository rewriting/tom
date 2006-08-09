
package strategy.graphterm.types.graphterm;

public class ConsrefGraphTerm extends strategy.graphterm.types.graphterm.refGraphTerm implements tom.library.strategy.mutraveler.MuVisitable {
  private static ConsrefGraphTerm proto = new ConsrefGraphTerm();
  private int hashCode;
  private ConsrefGraphTerm() {}

  private int _HeadrefGraphTerm;
  private strategy.graphterm.types.GraphTerm _TailrefGraphTerm;



    /* static constructor */

  public static ConsrefGraphTerm make(int _HeadrefGraphTerm, strategy.graphterm.types.GraphTerm _TailrefGraphTerm) {
    proto.initHashCode( _HeadrefGraphTerm,  _TailrefGraphTerm);
    return (ConsrefGraphTerm) shared.SingletonSharedObjectFactory.getInstance().build(proto);
  }



  private void init(int _HeadrefGraphTerm, strategy.graphterm.types.GraphTerm _TailrefGraphTerm, int hashCode) {
    this._HeadrefGraphTerm = _HeadrefGraphTerm;
    this._TailrefGraphTerm = _TailrefGraphTerm;

    this.hashCode = hashCode;
  }

  private void initHashCode(int _HeadrefGraphTerm, strategy.graphterm.types.GraphTerm _TailrefGraphTerm) {
    this._HeadrefGraphTerm = _HeadrefGraphTerm;
    this._TailrefGraphTerm = _TailrefGraphTerm;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  public String symbolName() {
    return "ConsrefGraphTerm";
  }

  private int getArity() {
    return 2;
  }

  public String toString() {
    return "ConsrefGraphTerm("+_HeadrefGraphTerm+","+_TailrefGraphTerm.toString()+")";
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
    ConsrefGraphTerm tco = (ConsrefGraphTerm) ao;
    
    if( this._HeadrefGraphTerm != tco._HeadrefGraphTerm)
      return (this._HeadrefGraphTerm < tco._HeadrefGraphTerm)?-1:1;

    int _TailrefGraphTermCmp = (this._TailrefGraphTerm).compareToLPO(tco._TailrefGraphTerm);
    if(_TailrefGraphTermCmp != 0)
      return _TailrefGraphTermCmp;

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
    ConsrefGraphTerm tco = (ConsrefGraphTerm) ao;
    
    if( this._HeadrefGraphTerm != tco._HeadrefGraphTerm)
      return (this._HeadrefGraphTerm < tco._HeadrefGraphTerm)?-1:1;

    int _TailrefGraphTermCmp = (this._TailrefGraphTerm).compareTo(tco._TailrefGraphTerm);
    if(_TailrefGraphTermCmp != 0)
      return _TailrefGraphTermCmp;

    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  public final int hashCode() {
    return hashCode;
  }

  public shared.SharedObject duplicate() {
    ConsrefGraphTerm clone = new ConsrefGraphTerm();
    clone.init( _HeadrefGraphTerm,  _TailrefGraphTerm, hashCode);
    return clone;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsrefGraphTerm) {

      ConsrefGraphTerm peer = (ConsrefGraphTerm) obj;
      return _HeadrefGraphTerm==peer._HeadrefGraphTerm && _TailrefGraphTerm==peer._TailrefGraphTerm && true;
    }
    return false;
  }

  /* GraphTerm interface */
  public boolean isConsrefGraphTerm() {
    return true;
  }


  public int getHeadrefGraphTerm() {
    return _HeadrefGraphTerm;
  }
      
  public strategy.graphterm.types.GraphTerm setHeadrefGraphTerm(int set_arg) {
    return make(set_arg, _TailrefGraphTerm);
  }
  public strategy.graphterm.types.GraphTerm getTailrefGraphTerm() {
    return _TailrefGraphTerm;
  }
      
  public strategy.graphterm.types.GraphTerm setTailrefGraphTerm(strategy.graphterm.types.GraphTerm set_arg) {
    return make(_HeadrefGraphTerm, set_arg);
  }


  /* AbstractType */
  public aterm.ATerm toATerm() {
    return aterm.pure.SingletonFactory.getInstance().makeAppl(
      aterm.pure.SingletonFactory.getInstance().makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeInt(getHeadrefGraphTerm()), getTailrefGraphTerm().toATerm()});
  }

  public static strategy.graphterm.types.GraphTerm fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(
          ((aterm.ATermInt)appl.getArgument(0)).getInt(),           strategy.graphterm.types.GraphTerm.fromTerm(appl.getArgument(1))
        );
      }
    }
    return null;
  }


  /* jjtraveler.Visitable */
  public int getChildCount() {
    return 1;
  }

  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _TailrefGraphTerm;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    switch(index) {
      case 0: return make(getHeadrefGraphTerm(), (strategy.graphterm.types.GraphTerm) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChilds(jjtraveler.Visitable[] childs) {
    if (childs.length == 1) {
      return make(getHeadrefGraphTerm(), (strategy.graphterm.types.GraphTerm) childs[0]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }


      /* internal use */
  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (1303078579<<8);
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */

    a += (_HeadrefGraphTerm << 8);
    a += (_TailrefGraphTerm.hashCode());


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
