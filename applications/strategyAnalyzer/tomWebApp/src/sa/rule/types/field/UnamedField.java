
package sa.rule.types.field;



public final class UnamedField extends sa.rule.types.Field implements tom.library.sl.Visitable  {
  
  private static String symbolName = "UnamedField";


  private UnamedField() {}
  private int hashCode;
  private static UnamedField gomProto = new UnamedField();
    private sa.rule.types.GomType _FieldType;

  /**
   * Constructor that builds a term rooted by UnamedField
   *
   * @return a term rooted by UnamedField
   */

  public static UnamedField make(sa.rule.types.GomType _FieldType) {

    // use the proto as a model
    gomProto.initHashCode( _FieldType);
    return (UnamedField) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _FieldType
   * @param hashCode hashCode of UnamedField
   */
  private void init(sa.rule.types.GomType _FieldType, int hashCode) {
    this._FieldType = _FieldType;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _FieldType
   */
  private void initHashCode(sa.rule.types.GomType _FieldType) {
    this._FieldType = _FieldType;

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
    return "UnamedField";
  }

  /**
   * Returns the arity of the symbol
   *
   * @return the arity of the symbol
   */
  private int getArity() {
    return 1;
  }

  /**
   * Copy the object and returns the copy
   *
   * @return a clone of the SharedObject
   */
  public shared.SharedObject duplicate() {
    UnamedField clone = new UnamedField();
    clone.init( _FieldType, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("UnamedField(");
    _FieldType.toStringBuilder(buffer);

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
    sa.rule.RuleAbstractType ao = (sa.rule.RuleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    UnamedField tco = (UnamedField) ao;
    int _FieldTypeCmp = (this._FieldType).compareToLPO(tco._FieldType);
    if(_FieldTypeCmp != 0) {
      return _FieldTypeCmp;
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
    sa.rule.RuleAbstractType ao = (sa.rule.RuleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    UnamedField tco = (UnamedField) ao;
    int _FieldTypeCmp = (this._FieldType).compareTo(tco._FieldType);
    if(_FieldTypeCmp != 0) {
      return _FieldTypeCmp;
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
   * @return true if obj is a UnamedField and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof UnamedField) {

      UnamedField peer = (UnamedField) obj;
      return _FieldType==peer._FieldType && true;
    }
    return false;
  }


   //Field interface
  /**
   * Returns true if the term is rooted by the symbol UnamedField
   *
   * @return true, because this is rooted by UnamedField
   */
  @Override
  public boolean isUnamedField() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.GomType
   *
   * @return the attribute sa.rule.types.GomType
   */
  @Override
  public sa.rule.types.GomType getFieldType() {
    return _FieldType;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Field
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.GomType which just has been set
   */
  @Override
  public sa.rule.types.Field setFieldType(sa.rule.types.GomType set_arg) {
    return make(set_arg);
  }
  
  /* Visitable */
  /**
   * Returns the number of children of the term
   *
   * @return the number of children of the term
   */
  public int getChildCount() {
    return 1;
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
      case 0: return _FieldType;
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
      case 0: return make((sa.rule.types.GomType) v);
      default: throw new IndexOutOfBoundsException();
 }
  }

  /**
   * Set children to the term
   *
   * @param children array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "children" is different than 1
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    if (children.length == getChildCount()  && children[0] instanceof sa.rule.types.GomType) {
      return make((sa.rule.types.GomType) children[0]);
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
    return new tom.library.sl.Visitable[] { _FieldType};
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
    b = (687017923<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_FieldType.hashCode());

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

  /**
    * function that returns functional version of the current operator
    * need for initializing the Enumerator
    */
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.Enumeration<sa.rule.types.Field>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.Enumeration<sa.rule.types.Field>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.Field> apply(final tom.library.enumerator.Enumeration<sa.rule.types.GomType> t1) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.GomType,sa.rule.types.Field>) 
        new tom.library.enumerator.F<sa.rule.types.GomType,sa.rule.types.Field>() {
          public sa.rule.types.Field apply(final sa.rule.types.GomType t1) {
            return make(t1);
          }
        }),t1).pay();
          }
        };
  }

}
