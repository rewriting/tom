
package sa.rule.types.production;



public final class SortType extends sa.rule.types.Production implements tom.library.sl.Visitable  {
  
  private static String symbolName = "SortType";


  private SortType() {}
  private int hashCode;
  private static SortType gomProto = new SortType();
    private sa.rule.types.GomType _Type;
  private sa.rule.types.AlternativeList _AlternativeList;

  /**
   * Constructor that builds a term rooted by SortType
   *
   * @return a term rooted by SortType
   */

  public static SortType make(sa.rule.types.GomType _Type, sa.rule.types.AlternativeList _AlternativeList) {

    // use the proto as a model
    gomProto.initHashCode( _Type,  _AlternativeList);
    return (SortType) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Type
   * @param _AlternativeList
   * @param hashCode hashCode of SortType
   */
  private void init(sa.rule.types.GomType _Type, sa.rule.types.AlternativeList _AlternativeList, int hashCode) {
    this._Type = _Type;
    this._AlternativeList = _AlternativeList;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Type
   * @param _AlternativeList
   */
  private void initHashCode(sa.rule.types.GomType _Type, sa.rule.types.AlternativeList _AlternativeList) {
    this._Type = _Type;
    this._AlternativeList = _AlternativeList;

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
    return "SortType";
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
    SortType clone = new SortType();
    clone.init( _Type,  _AlternativeList, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("SortType(");
    _Type.toStringBuilder(buffer);
buffer.append(",");
    _AlternativeList.toStringBuilder(buffer);

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
    SortType tco = (SortType) ao;
    int _TypeCmp = (this._Type).compareToLPO(tco._Type);
    if(_TypeCmp != 0) {
      return _TypeCmp;
    }

    int _AlternativeListCmp = (this._AlternativeList).compareToLPO(tco._AlternativeList);
    if(_AlternativeListCmp != 0) {
      return _AlternativeListCmp;
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
    SortType tco = (SortType) ao;
    int _TypeCmp = (this._Type).compareTo(tco._Type);
    if(_TypeCmp != 0) {
      return _TypeCmp;
    }

    int _AlternativeListCmp = (this._AlternativeList).compareTo(tco._AlternativeList);
    if(_AlternativeListCmp != 0) {
      return _AlternativeListCmp;
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
   * @return true if obj is a SortType and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof SortType) {

      SortType peer = (SortType) obj;
      return _Type==peer._Type && _AlternativeList==peer._AlternativeList && true;
    }
    return false;
  }


   //Production interface
  /**
   * Returns true if the term is rooted by the symbol SortType
   *
   * @return true, because this is rooted by SortType
   */
  @Override
  public boolean isSortType() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.GomType
   *
   * @return the attribute sa.rule.types.GomType
   */
  @Override
  public sa.rule.types.GomType getType() {
    return _Type;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Production
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.GomType which just has been set
   */
  @Override
  public sa.rule.types.Production setType(sa.rule.types.GomType set_arg) {
    return make(set_arg, _AlternativeList);
  }
  
  /**
   * Returns the attribute sa.rule.types.AlternativeList
   *
   * @return the attribute sa.rule.types.AlternativeList
   */
  @Override
  public sa.rule.types.AlternativeList getAlternativeList() {
    return _AlternativeList;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Production
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.AlternativeList which just has been set
   */
  @Override
  public sa.rule.types.Production setAlternativeList(sa.rule.types.AlternativeList set_arg) {
    return make(_Type, set_arg);
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
      case 0: return _Type;
      case 1: return _AlternativeList;
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
      case 0: return make((sa.rule.types.GomType) v, _AlternativeList);
      case 1: return make(_Type, (sa.rule.types.AlternativeList) v);
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
    if (children.length == getChildCount()  && children[0] instanceof sa.rule.types.GomType && children[1] instanceof sa.rule.types.AlternativeList) {
      return make((sa.rule.types.GomType) children[0], (sa.rule.types.AlternativeList) children[1]);
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
    return new tom.library.sl.Visitable[] { _Type,  _AlternativeList};
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
    b = (2005830336<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_Type.hashCode() << 8);
    a += (_AlternativeList.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>,tom.library.enumerator.Enumeration<sa.rule.types.Production>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>,tom.library.enumerator.Enumeration<sa.rule.types.Production>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>,tom.library.enumerator.Enumeration<sa.rule.types.Production>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.GomType> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>,tom.library.enumerator.Enumeration<sa.rule.types.Production>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.Production> apply(final tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.GomType,tom.library.enumerator.F<sa.rule.types.AlternativeList,sa.rule.types.Production>>) 
        new tom.library.enumerator.F<sa.rule.types.GomType,tom.library.enumerator.F<sa.rule.types.AlternativeList,sa.rule.types.Production>>() {
          public tom.library.enumerator.F<sa.rule.types.AlternativeList,sa.rule.types.Production> apply(final sa.rule.types.GomType t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.AlternativeList,sa.rule.types.Production>() {
          public sa.rule.types.Production apply(final sa.rule.types.AlternativeList t2) {
            return make(t1,t2);
          }
        };
          }
        }),t1),t2).pay();
          }
        };
          }
        };
  }

}
