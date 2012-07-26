
package sort.types.intlist;



public final class consList extends sort.types.IntList implements tom.library.sl.Visitable  {
  
  private static String symbolName = "consList";


  private consList() {}
  private int hashCode;
  private static consList gomProto = new consList();
    private sort.types.Leaf _a;
  private sort.types.IntList _tail;

  /**
   * Constructor that builds a term rooted by consList
   *
   * @return a term rooted by consList
   */

  public static consList make(sort.types.Leaf _a, sort.types.IntList _tail) {

    // use the proto as a model
    gomProto.initHashCode( _a,  _tail);
    return (consList) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _a
   * @param _tail
   * @param hashCode hashCode of consList
   */
  private void init(sort.types.Leaf _a, sort.types.IntList _tail, int hashCode) {
    this._a = _a;
    this._tail = _tail;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _a
   * @param _tail
   */
  private void initHashCode(sort.types.Leaf _a, sort.types.IntList _tail) {
    this._a = _a;
    this._tail = _tail;

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
    return "consList";
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
    consList clone = new consList();
    clone.init( _a,  _tail, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("consList(");
    _a.toStringBuilder(buffer);
buffer.append(",");
    _tail.toStringBuilder(buffer);

    buffer.append(")");
  }


  /**
   * Compares two terms. This functions implements a total lexicographic path ordering.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare children
   */
  @Override
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    sort.SortAbstractType ao = (sort.SortAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    consList tco = (consList) ao;
    int _aCmp = (this._a).compareToLPO(tco._a);
    if(_aCmp != 0) {
      return _aCmp;
    }

    int _tailCmp = (this._tail).compareToLPO(tco._tail);
    if(_tailCmp != 0) {
      return _tailCmp;
    }

    throw new RuntimeException("Unable to compare");
  }

 /**
   * Compares two terms. This functions implements a total order.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare children
   */
  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    sort.SortAbstractType ao = (sort.SortAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    consList tco = (consList) ao;
    int _aCmp = (this._a).compareTo(tco._a);
    if(_aCmp != 0) {
      return _aCmp;
    }

    int _tailCmp = (this._tail).compareTo(tco._tail);
    if(_tailCmp != 0) {
      return _tailCmp;
    }

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
   * @return true if obj is a consList and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof consList) {

      consList peer = (consList) obj;
      return _a==peer._a && _tail==peer._tail && true;
    }
    return false;
  }


   //IntList interface
  /**
   * Returns true if the term is rooted by the symbol consList
   *
   * @return true, because this is rooted by consList
   */
  @Override
  public boolean isconsList() {
    return true;
  }
  
  /**
   * Returns the attribute sort.types.Leaf
   *
   * @return the attribute sort.types.Leaf
   */
  @Override
  public sort.types.Leaf geta() {
    return _a;
  }

  /**
   * Sets and returns the attribute sort.types.IntList
   *
   * @param set_arg the argument to set
   * @return the attribute sort.types.Leaf which just has been set
   */
  @Override
  public sort.types.IntList seta(sort.types.Leaf set_arg) {
    return make(set_arg, _tail);
  }
  
  /**
   * Returns the attribute sort.types.IntList
   *
   * @return the attribute sort.types.IntList
   */
  @Override
  public sort.types.IntList gettail() {
    return _tail;
  }

  /**
   * Sets and returns the attribute sort.types.IntList
   *
   * @param set_arg the argument to set
   * @return the attribute sort.types.IntList which just has been set
   */
  @Override
  public sort.types.IntList settail(sort.types.IntList set_arg) {
    return make(_a, set_arg);
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
      new aterm.ATerm[] {geta().toATerm(), gettail().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a sort.types.IntList from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static sort.types.IntList fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
sort.types.Leaf.fromTerm(appl.getArgument(0),atConv), sort.types.IntList.fromTerm(appl.getArgument(1),atConv)
        );
      }
    }
    return null;
  }

  /* Visitable */
  /**
   * Returns the number of children of the term
   *
   * @return the number of children of the term
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
      case 0: return _a;
      case 1: return _tail;

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
      case 0: return make((sort.types.Leaf) v, _tail);
      case 1: return make(_a, (sort.types.IntList) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Set children to the term
   *
   * @param children array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "children" is different than 2
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    if (children.length == 2  && children[0] instanceof sort.types.Leaf && children[1] instanceof sort.types.IntList) {
      return make((sort.types.Leaf) children[0], (sort.types.IntList) children[1]);
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
    return new tom.library.sl.Visitable[] {  _a,  _tail };
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
    b = (549483933<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_a.hashCode() << 8);
    a += (_tail.hashCode());

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
