
package strategy.graphterm.types.graphterm;

public class node extends strategy.graphterm.types.GraphTerm implements tom.library.strategy.mutraveler.MuVisitable {
  private static node proto = new node();
  private int hashCode;
  private node() {}

  private String _name;



    /* static constructor */

  public static node make(String _name) {
    proto.initHashCode( _name);
    return (node) shared.SingletonSharedObjectFactory.getInstance().build(proto);
  }



  private void init(String _name, int hashCode) {
    this._name = _name.intern();

    this.hashCode = hashCode;
  }

  private void initHashCode(String _name) {
    this._name = _name.intern();

  this.hashCode = hashFunction();
  }

  /* name and arity */
  public String symbolName() {
    return "node";
  }

  private int getArity() {
    return 1;
  }

  public String toString() {
    return "node(\""+_name+"\")";
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
    node tco = (node) ao;
    
    int _nameCmp = (this._name).compareTo(tco._name);
    if(_nameCmp != 0)
      return _nameCmp;
             

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
    node tco = (node) ao;
    
    int _nameCmp = (this._name).compareTo(tco._name);
    if(_nameCmp != 0)
      return _nameCmp;
             

    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  public final int hashCode() {
    return hashCode;
  }

  public shared.SharedObject duplicate() {
    node clone = new node();
    clone.init( _name, hashCode);
    return clone;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof node) {

      node peer = (node) obj;
      return _name==peer._name && true;
    }
    return false;
  }

  /* GraphTerm interface */
  public boolean isnode() {
    return true;
  }


  public String getname() {
    return _name;
  }
      
  public strategy.graphterm.types.GraphTerm setname(String set_arg) {
    return make(set_arg);
  }


  /* AbstractType */
  public aterm.ATerm toATerm() {
    return aterm.pure.SingletonFactory.getInstance().makeAppl(
      aterm.pure.SingletonFactory.getInstance().makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeAppl(aterm.pure.SingletonFactory.getInstance().makeAFun(getname() ,0 , true))});
  }

  public static strategy.graphterm.types.GraphTerm fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(
          (String)((aterm.ATermAppl)appl.getArgument(0)).getAFun().getName()
        );
      }
    }
    return null;
  }


  /* jjtraveler.Visitable */
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

  public jjtraveler.Visitable setChilds(jjtraveler.Visitable[] childs) {
    if (childs.length == 0) {
      return make(getname());
    } else {
      throw new IndexOutOfBoundsException();
    }
  }


      /* internal use */
  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-217364486<<8);
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */

    a += (shared.HashFunctions.stringHashFunction(_name, 0));


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
