
package strategy.graphterm.types.graphterm;

public class graph extends strategy.graphterm.types.GraphTerm implements tom.library.strategy.mutraveler.MuVisitable {
  private static graph proto = new graph();
  private int hashCode;
  private graph() {}

  private String _name;
  private strategy.graphterm.types.GraphTermList _subterms;



    /* static constructor */

  public static graph make(String _name, strategy.graphterm.types.GraphTermList _subterms) {
    proto.initHashCode( _name,  _subterms);
    return (graph) shared.SingletonSharedObjectFactory.getInstance().build(proto);
  }



  private void init(String _name, strategy.graphterm.types.GraphTermList _subterms, int hashCode) {
    this._name = _name.intern();
    this._subterms = _subterms;

    this.hashCode = hashCode;
  }

  private void initHashCode(String _name, strategy.graphterm.types.GraphTermList _subterms) {
    this._name = _name.intern();
    this._subterms = _subterms;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  public String symbolName() {
    return "graph";
  }

  private int getArity() {
    return 2;
  }

  public String toString() {
    return "graph(\""+_name+"\","+_subterms.toString()+")";
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
    graph tco = (graph) ao;
    
    int _nameCmp = (this._name).compareTo(tco._name);
    if(_nameCmp != 0)
      return _nameCmp;
             

    int _subtermsCmp = (this._subterms).compareToLPO(tco._subterms);
    if(_subtermsCmp != 0)
      return _subtermsCmp;

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
    graph tco = (graph) ao;
    
    int _nameCmp = (this._name).compareTo(tco._name);
    if(_nameCmp != 0)
      return _nameCmp;
             

    int _subtermsCmp = (this._subterms).compareTo(tco._subterms);
    if(_subtermsCmp != 0)
      return _subtermsCmp;

    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  public final int hashCode() {
    return hashCode;
  }

  public shared.SharedObject duplicate() {
    graph clone = new graph();
    clone.init( _name,  _subterms, hashCode);
    return clone;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof graph) {

      graph peer = (graph) obj;
      return _name==peer._name && _subterms==peer._subterms && true;
    }
    return false;
  }

  /* GraphTerm interface */
  public boolean isgraph() {
    return true;
  }


  public String getname() {
    return _name;
  }
      
  public strategy.graphterm.types.GraphTerm setname(String set_arg) {
    return make(set_arg, _subterms);
  }
  public strategy.graphterm.types.GraphTermList getsubterms() {
    return _subterms;
  }
      
  public strategy.graphterm.types.GraphTerm setsubterms(strategy.graphterm.types.GraphTermList set_arg) {
    return make(_name, set_arg);
  }


  /* AbstractType */
  public aterm.ATerm toATerm() {
    return aterm.pure.SingletonFactory.getInstance().makeAppl(
      aterm.pure.SingletonFactory.getInstance().makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeAppl(aterm.pure.SingletonFactory.getInstance().makeAFun(getname() ,0 , true)), getsubterms().toATerm()});
  }

  public static strategy.graphterm.types.GraphTerm fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(
          (String)((aterm.ATermAppl)appl.getArgument(0)).getAFun().getName(),           strategy.graphterm.types.GraphTermList.fromTerm(appl.getArgument(1))
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
      case 0: return _subterms;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    switch(index) {
      case 0: return make(getname(), (strategy.graphterm.types.GraphTermList) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChilds(jjtraveler.Visitable[] childs) {
    if (childs.length == 1) {
      return make(getname(), (strategy.graphterm.types.GraphTermList) childs[0]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }


      /* internal use */
  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-1217323524<<8);
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */

    a += (shared.HashFunctions.stringHashFunction(_name, 1) << 8);
    a += (_subterms.hashCode());


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
