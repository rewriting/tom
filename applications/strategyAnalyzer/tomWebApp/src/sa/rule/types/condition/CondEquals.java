
package sa.rule.types.condition;



public final class CondEquals extends sa.rule.types.Condition implements tom.library.sl.Visitable  {
  
  private static String symbolName = "CondEquals";


  private CondEquals() {}
  private int hashCode;
  private static CondEquals gomProto = new CondEquals();
    private sa.rule.types.Term _t1;
  private sa.rule.types.Term _t2;

  /**
   * Constructor that builds a term rooted by CondEquals
   *
   * @return a term rooted by CondEquals
   */

  public static CondEquals make(sa.rule.types.Term _t1, sa.rule.types.Term _t2) {

    // use the proto as a model
    gomProto.initHashCode( _t1,  _t2);
    return (CondEquals) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _t1
   * @param _t2
   * @param hashCode hashCode of CondEquals
   */
  private void init(sa.rule.types.Term _t1, sa.rule.types.Term _t2, int hashCode) {
    this._t1 = _t1;
    this._t2 = _t2;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _t1
   * @param _t2
   */
  private void initHashCode(sa.rule.types.Term _t1, sa.rule.types.Term _t2) {
    this._t1 = _t1;
    this._t2 = _t2;

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
    return "CondEquals";
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
    CondEquals clone = new CondEquals();
    clone.init( _t1,  _t2, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("CondEquals(");
    _t1.toStringBuilder(buffer);
buffer.append(",");
    _t2.toStringBuilder(buffer);

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
    CondEquals tco = (CondEquals) ao;
    int _t1Cmp = (this._t1).compareToLPO(tco._t1);
    if(_t1Cmp != 0) {
      return _t1Cmp;
    }

    int _t2Cmp = (this._t2).compareToLPO(tco._t2);
    if(_t2Cmp != 0) {
      return _t2Cmp;
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
    CondEquals tco = (CondEquals) ao;
    int _t1Cmp = (this._t1).compareTo(tco._t1);
    if(_t1Cmp != 0) {
      return _t1Cmp;
    }

    int _t2Cmp = (this._t2).compareTo(tco._t2);
    if(_t2Cmp != 0) {
      return _t2Cmp;
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
   * @return true if obj is a CondEquals and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof CondEquals) {

      CondEquals peer = (CondEquals) obj;
      return _t1==peer._t1 && _t2==peer._t2 && true;
    }
    return false;
  }


   //Condition interface
  /**
   * Returns true if the term is rooted by the symbol CondEquals
   *
   * @return true, because this is rooted by CondEquals
   */
  @Override
  public boolean isCondEquals() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Term
   *
   * @return the attribute sa.rule.types.Term
   */
  @Override
  public sa.rule.types.Term gett1() {
    return _t1;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Condition
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Term which just has been set
   */
  @Override
  public sa.rule.types.Condition sett1(sa.rule.types.Term set_arg) {
    return make(set_arg, _t2);
  }
  
  /**
   * Returns the attribute sa.rule.types.Term
   *
   * @return the attribute sa.rule.types.Term
   */
  @Override
  public sa.rule.types.Term gett2() {
    return _t2;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Condition
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Term which just has been set
   */
  @Override
  public sa.rule.types.Condition sett2(sa.rule.types.Term set_arg) {
    return make(_t1, set_arg);
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
      case 0: return _t1;
      case 1: return _t2;
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
      case 0: return make((sa.rule.types.Term) v, _t2);
      case 1: return make(_t1, (sa.rule.types.Term) v);
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
    if (children.length == getChildCount()  && children[0] instanceof sa.rule.types.Term && children[1] instanceof sa.rule.types.Term) {
      return make((sa.rule.types.Term) children[0], (sa.rule.types.Term) children[1]);
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
    return new tom.library.sl.Visitable[] { _t1,  _t2};
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
    b = (244163114<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_t1.hashCode() << 8);
    a += (_t2.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.Enumeration<sa.rule.types.Condition>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.Enumeration<sa.rule.types.Condition>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.Enumeration<sa.rule.types.Condition>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Term> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.Enumeration<sa.rule.types.Condition>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.Condition> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Term> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Term,tom.library.enumerator.F<sa.rule.types.Term,sa.rule.types.Condition>>) 
        new tom.library.enumerator.F<sa.rule.types.Term,tom.library.enumerator.F<sa.rule.types.Term,sa.rule.types.Condition>>() {
          public tom.library.enumerator.F<sa.rule.types.Term,sa.rule.types.Condition> apply(final sa.rule.types.Term t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.Term,sa.rule.types.Condition>() {
          public sa.rule.types.Condition apply(final sa.rule.types.Term t2) {
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
