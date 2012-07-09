
package sort.types.expr;



public final class plus extends sort.types.Expr implements tom.library.sl.Visitable  {
  
  private static String symbolName = "plus";


  private plus() {}
  private int hashCode;
  private static plus gomProto = new plus();
    private sort.types.Expr _a;
  private sort.types.Expr _b;

  /**
   * Constructor that builds a term rooted by plus
   *
   * @return a term rooted by plus
   */

  public static plus make(sort.types.Expr _a, sort.types.Expr _b) {

    // use the proto as a model
    gomProto.initHashCode( _a,  _b);
    return (plus) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _a
   * @param _b
   * @param hashCode hashCode of plus
   */
  private void init(sort.types.Expr _a, sort.types.Expr _b, int hashCode) {
    this._a = _a;
    this._b = _b;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _a
   * @param _b
   */
  private void initHashCode(sort.types.Expr _a, sort.types.Expr _b) {
    this._a = _a;
    this._b = _b;

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
    clone.init( _a,  _b, hashCode);
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
    _a.toStringBuilder(buffer);
buffer.append(",");
    _b.toStringBuilder(buffer);

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
    plus tco = (plus) ao;
    int _aCmp = (this._a).compareToLPO(tco._a);
    if(_aCmp != 0) {
      return _aCmp;
    }

    int _bCmp = (this._b).compareToLPO(tco._b);
    if(_bCmp != 0) {
      return _bCmp;
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
    plus tco = (plus) ao;
    int _aCmp = (this._a).compareTo(tco._a);
    if(_aCmp != 0) {
      return _aCmp;
    }

    int _bCmp = (this._b).compareTo(tco._b);
    if(_bCmp != 0) {
      return _bCmp;
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
   * @return true if obj is a plus and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof plus) {

      plus peer = (plus) obj;
      return _a==peer._a && _b==peer._b && true;
    }
    return false;
  }


   //Expr interface
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
   * Returns the attribute sort.types.Expr
   *
   * @return the attribute sort.types.Expr
   */
  @Override
  public sort.types.Expr geta() {
    return _a;
  }

  /**
   * Sets and returns the attribute sort.types.Expr
   *
   * @param set_arg the argument to set
   * @return the attribute sort.types.Expr which just has been set
   */
  @Override
  public sort.types.Expr seta(sort.types.Expr set_arg) {
    return make(set_arg, _b);
  }
  
  /**
   * Returns the attribute sort.types.Expr
   *
   * @return the attribute sort.types.Expr
   */
  @Override
  public sort.types.Expr getb() {
    return _b;
  }

  /**
   * Sets and returns the attribute sort.types.Expr
   *
   * @param set_arg the argument to set
   * @return the attribute sort.types.Expr which just has been set
   */
  @Override
  public sort.types.Expr setb(sort.types.Expr set_arg) {
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
      new aterm.ATerm[] {geta().toATerm(), getb().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a sort.types.Expr from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static sort.types.Expr fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
sort.types.Expr.fromTerm(appl.getArgument(0),atConv), sort.types.Expr.fromTerm(appl.getArgument(1),atConv)
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
      case 1: return _b;

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
      case 0: return make((sort.types.Expr) v, _b);
      case 1: return make(_a, (sort.types.Expr) v);

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
    if (children.length == 2  && children[0] instanceof sort.types.Expr && children[1] instanceof sort.types.Expr) {
      return make((sort.types.Expr) children[0], (sort.types.Expr) children[1]);
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
    return new tom.library.sl.Visitable[] {  _a,  _b };
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
    b = (989372470<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_a.hashCode() << 8);
    a += (_b.hashCode());

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
