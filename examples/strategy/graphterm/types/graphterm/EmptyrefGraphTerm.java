
package strategy.graphterm.types.graphterm;

public class EmptyrefGraphTerm extends strategy.graphterm.types.graphterm.refGraphTerm implements tom.library.strategy.mutraveler.MuVisitable {
  private static EmptyrefGraphTerm proto = new EmptyrefGraphTerm();
  private int hashCode;
  private EmptyrefGraphTerm() {}




    /* static constructor */

  public static EmptyrefGraphTerm make() {
    proto.initHashCode();
    return (EmptyrefGraphTerm) shared.SingletonSharedObjectFactory.getInstance().build(proto);
  }



  private void init(int hashCode) {

    this.hashCode = hashCode;
  }

  private void initHashCode() {

  this.hashCode = hashFunction();
  }

  /* name and arity */
  public String symbolName() {
    return "EmptyrefGraphTerm";
  }

  private int getArity() {
    return 0;
  }

  public String toString() {
    return "EmptyrefGraphTerm()";
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
    EmptyrefGraphTerm tco = (EmptyrefGraphTerm) ao;
    
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
    EmptyrefGraphTerm tco = (EmptyrefGraphTerm) ao;
    
    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  public final int hashCode() {
    return hashCode;
  }

  public shared.SharedObject duplicate() {
    EmptyrefGraphTerm clone = new EmptyrefGraphTerm();
    clone.init(hashCode);
    return clone;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof EmptyrefGraphTerm) {

      return true;
    }
    return false;
  }

  /* GraphTerm interface */
  public boolean isEmptyrefGraphTerm() {
    return true;
  }




  /* AbstractType */
  public aterm.ATerm toATerm() {
    return aterm.pure.SingletonFactory.getInstance().makeAppl(
      aterm.pure.SingletonFactory.getInstance().makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {});
  }

  public static strategy.graphterm.types.GraphTerm fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(

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
      return make();
    } else {
      throw new IndexOutOfBoundsException();
    }
  }


      /* internal use */
  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (822080946<<8);
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */



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
