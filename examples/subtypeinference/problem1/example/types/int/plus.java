
package problem1.example.types.int;



public final class plus extends problem1.example.types.Int implements tom.library.sl.Visitable  {
  
  private static String symbolName = "plus";


  private plus() {}
  private int hashCode;
  private static plus proto = new plus();
    private problem1.example.types.Int _n1;
  private problem1.example.types.Int _n2;

  /**
   * Constructor that builds a term rooted by plus
   *
   * @return a term rooted by plus
   */

  public static plus make(problem1.example.types.Int _n1, problem1.example.types.Int _n2) {

    // use the proto as a model
    proto.initHashCode( _n1,  _n2);
    return (plus) factory.build(proto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _n1
   * @param _n2
   * @param hashCode hashCode of plus
   */
  private void init(problem1.example.types.Int _n1, problem1.example.types.Int _n2, int hashCode) {
    this._n1 = _n1;
    this._n2 = _n2;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _n1
   * @param _n2
   */
  private void initHashCode(problem1.example.types.Int _n1, problem1.example.types.Int _n2) {
    this._n1 = _n1;
    this._n2 = _n2;

    this.hashCode = hashFunction();
  }

  /* name and arity */

  /**
   * Returns the name of the symbol
   *
   * @return the name of the symbol
   */
  @Override
  public String symbolName() {
    return "plus";
  }

  /**
   * Returns the arity of the symbol
   *
   * @return the arity of the symbol
   */
  private int getArity() {
    return 2;
  }

  /**
   * Copy the object and returns the copy
   *
   * @return a clone of the SharedObject
   */
  public shared.SharedObject duplicate() {
    plus clone = new plus();
    clone.init( _n1,  _n2, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("plus(");
    _n1.toStringBuilder(buffer);
buffer.append(",");
    _n2.toStringBuilder(buffer);

    buffer.append(")");
  }


  /**
   * Compares two terms. This functions implements a total lexicographic path ordering.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare childs
   */
  @Override
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    problem1.example.ExampleAbstractType ao = (problem1.example.ExampleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the childs */
    plus tco = (plus) ao;
    int _n1Cmp = (this._n1).compareToLPO(tco._n1);
    if(_n1Cmp != 0)
      return _n1Cmp;

    int _n2Cmp = (this._n2).compareToLPO(tco._n2);
    if(_n2Cmp != 0)
      return _n2Cmp;

    throw new RuntimeException("Unable to compare");
  }

 /**
   * Compares two terms. This functions implements a total order.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare childs
   */
  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    problem1.example.ExampleAbstractType ao = (problem1.example.ExampleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the childs */
    plus tco = (plus) ao;
    int _n1Cmp = (this._n1).compareTo(tco._n1);
    if(_n1Cmp != 0)
      return _n1Cmp;

    int _n2Cmp = (this._n2).compareTo(tco._n2);
    if(_n2Cmp != 0)
      return _n2Cmp;

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  /**
   * Returns hashCode
   *
   * @return hashCode
   */
  @Override
  public final int hashCode() {
    return hashCode;
  }

  /**
   * Checks if a SharedObject is equivalent to the current object
   *
   * @param obj SharedObject to test
   * @return true if obj is a plus and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof plus) {

      plus peer = (plus) obj;
      return _n1==peer._n1 && _n2==peer._n2 && true;
    }
    return false;
  }


   //Int interface
  /**
   * Returns true if the term is rooted by the symbol plus
   *
   * @return true, because this is rooted by plus
   */
  @Override
  public boolean isplus() {
    return true;
  }
  
  /**
   * Returns the attribute problem1.example.types.Int
   *
   * @return the attribute problem1.example.types.Int
   */
  @Override
  public problem1.example.types.Int getn1() {
    return _n1;
  }

  /**
   * Sets and returns the attribute problem1.example.types.Int
   *
   * @param set_arg the argument to set
   * @return the attribute problem1.example.types.Int which just has been set
   */
  @Override
  public problem1.example.types.Int setn1(problem1.example.types.Int set_arg) {
    return make(set_arg, _n2);
  }
  
  /**
   * Returns the attribute problem1.example.types.Int
   *
   * @return the attribute problem1.example.types.Int
   */
  @Override
  public problem1.example.types.Int getn2() {
    return _n2;
  }

  /**
   * Sets and returns the attribute problem1.example.types.Int
   *
   * @param set_arg the argument to set
   * @return the attribute problem1.example.types.Int which just has been set
   */
  @Override
  public problem1.example.types.Int setn2(problem1.example.types.Int set_arg) {
    return make(_n1, set_arg);
  }
  
  /* AbstractType */
  /**
   * Returns an ATerm representation of this term.
   *
   * @return an ATerm representation of this term.
   */
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

  /**
   * Apply a conversion on the ATerm contained in the String and returns a problem1.example.types.Int from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static problem1.example.types.Int fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
problem1.example.types.Int.fromTerm(appl.getArgument(0),atConv), problem1.example.types.Int.fromTerm(appl.getArgument(1),atConv)
        );
      }
    }
    return null;
  }

  /* Visitable */
  /**
   * Returns the number of childs of the term
   *
   * @return the number of childs of the term
   */
  public int getChildCount() {
    return 2;
  }

  /**
   * Returns the child at the specified index
   *
   * @param index index of the child to return; must be
             nonnegative and less than the childCount
   * @return the child at the specified index
   * @throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _n1;
      case 1: return _n2;

      default: throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Set the child at the specified index
   *
   * @param index index of the child to set; must be
             nonnegative and less than the childCount
   * @param v child to set at the specified index
   * @return the child which was just set
   * @throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
      case 0: return make((problem1.example.types.Int) v, _n2);
      case 1: return make(_n1, (problem1.example.types.Int) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Set children to the term
   *
   * @param childs array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "childs" is different than 2
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 2  && childs[0] instanceof problem1.example.types.Int && childs[1] instanceof problem1.example.types.Int) {
      return make((problem1.example.types.Int) childs[0], (problem1.example.types.Int) childs[1]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Returns the whole children of the term
   *
   * @return the children of the term
   */
  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  _n1,  _n2 };
  }

    /**
     * Compute a hashcode for this term.
     * (for internal use)
     *
     * @return a hash value
     */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-1200896224<<8);
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
