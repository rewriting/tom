
package sa.rule.types.strat;



public final class StratSequence extends sa.rule.types.Strat implements tom.library.sl.Visitable  {
  
  private static String symbolName = "StratSequence";


  private StratSequence() {}
  private int hashCode;
  private static StratSequence gomProto = new StratSequence();
    private sa.rule.types.Strat _s1;
  private sa.rule.types.Strat _s2;

  /**
   * Constructor that builds a term rooted by StratSequence
   *
   * @return a term rooted by StratSequence
   */

  public static StratSequence make(sa.rule.types.Strat _s1, sa.rule.types.Strat _s2) {

    // use the proto as a model
    gomProto.initHashCode( _s1,  _s2);
    return (StratSequence) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _s1
   * @param _s2
   * @param hashCode hashCode of StratSequence
   */
  private void init(sa.rule.types.Strat _s1, sa.rule.types.Strat _s2, int hashCode) {
    this._s1 = _s1;
    this._s2 = _s2;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _s1
   * @param _s2
   */
  private void initHashCode(sa.rule.types.Strat _s1, sa.rule.types.Strat _s2) {
    this._s1 = _s1;
    this._s2 = _s2;

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
    return "StratSequence";
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
    StratSequence clone = new StratSequence();
    clone.init( _s1,  _s2, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("StratSequence(");
    _s1.toStringBuilder(buffer);
buffer.append(",");
    _s2.toStringBuilder(buffer);

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
    StratSequence tco = (StratSequence) ao;
    int _s1Cmp = (this._s1).compareToLPO(tco._s1);
    if(_s1Cmp != 0) {
      return _s1Cmp;
    }

    int _s2Cmp = (this._s2).compareToLPO(tco._s2);
    if(_s2Cmp != 0) {
      return _s2Cmp;
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
    StratSequence tco = (StratSequence) ao;
    int _s1Cmp = (this._s1).compareTo(tco._s1);
    if(_s1Cmp != 0) {
      return _s1Cmp;
    }

    int _s2Cmp = (this._s2).compareTo(tco._s2);
    if(_s2Cmp != 0) {
      return _s2Cmp;
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
   * @return true if obj is a StratSequence and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof StratSequence) {

      StratSequence peer = (StratSequence) obj;
      return _s1==peer._s1 && _s2==peer._s2 && true;
    }
    return false;
  }


   //Strat interface
  /**
   * Returns true if the term is rooted by the symbol StratSequence
   *
   * @return true, because this is rooted by StratSequence
   */
  @Override
  public boolean isStratSequence() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Strat
   *
   * @return the attribute sa.rule.types.Strat
   */
  @Override
  public sa.rule.types.Strat gets1() {
    return _s1;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Strat
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Strat which just has been set
   */
  @Override
  public sa.rule.types.Strat sets1(sa.rule.types.Strat set_arg) {
    return make(set_arg, _s2);
  }
  
  /**
   * Returns the attribute sa.rule.types.Strat
   *
   * @return the attribute sa.rule.types.Strat
   */
  @Override
  public sa.rule.types.Strat gets2() {
    return _s2;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Strat
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Strat which just has been set
   */
  @Override
  public sa.rule.types.Strat sets2(sa.rule.types.Strat set_arg) {
    return make(_s1, set_arg);
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
      case 0: return _s1;
      case 1: return _s2;
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
      case 0: return make((sa.rule.types.Strat) v, _s2);
      case 1: return make(_s1, (sa.rule.types.Strat) v);
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
    if (children.length == getChildCount()  && children[0] instanceof sa.rule.types.Strat && children[1] instanceof sa.rule.types.Strat) {
      return make((sa.rule.types.Strat) children[0], (sa.rule.types.Strat) children[1]);
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
    return new tom.library.sl.Visitable[] { _s1,  _s2};
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
    b = (388748703<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_s1.hashCode() << 8);
    a += (_s2.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.Strat>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.Strat>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.Strat>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Strat> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.Strat>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.Strat> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Strat> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Strat,tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.Strat>>) 
        new tom.library.enumerator.F<sa.rule.types.Strat,tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.Strat>>() {
          public tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.Strat> apply(final sa.rule.types.Strat t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.Strat>() {
          public sa.rule.types.Strat apply(final sa.rule.types.Strat t2) {
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
