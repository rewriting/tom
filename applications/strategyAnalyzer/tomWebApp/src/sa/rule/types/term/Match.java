
package sa.rule.types.term;



public final class Match extends sa.rule.types.Term implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Match";


  private Match() {}
  private int hashCode;
  private static Match gomProto = new Match();
    private sa.rule.types.Term _term1;
  private sa.rule.types.Term _term2;

  /**
   * Constructor that builds a term rooted by Match
   *
   * @return a term rooted by Match
   */

  public static Match make(sa.rule.types.Term _term1, sa.rule.types.Term _term2) {

    // use the proto as a model
    gomProto.initHashCode( _term1,  _term2);
    return (Match) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _term1
   * @param _term2
   * @param hashCode hashCode of Match
   */
  private void init(sa.rule.types.Term _term1, sa.rule.types.Term _term2, int hashCode) {
    this._term1 = _term1;
    this._term2 = _term2;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _term1
   * @param _term2
   */
  private void initHashCode(sa.rule.types.Term _term1, sa.rule.types.Term _term2) {
    this._term1 = _term1;
    this._term2 = _term2;

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
    return "Match";
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
    Match clone = new Match();
    clone.init( _term1,  _term2, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Match(");
    _term1.toStringBuilder(buffer);
buffer.append(",");
    _term2.toStringBuilder(buffer);

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
    Match tco = (Match) ao;
    int _term1Cmp = (this._term1).compareToLPO(tco._term1);
    if(_term1Cmp != 0) {
      return _term1Cmp;
    }

    int _term2Cmp = (this._term2).compareToLPO(tco._term2);
    if(_term2Cmp != 0) {
      return _term2Cmp;
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
    Match tco = (Match) ao;
    int _term1Cmp = (this._term1).compareTo(tco._term1);
    if(_term1Cmp != 0) {
      return _term1Cmp;
    }

    int _term2Cmp = (this._term2).compareTo(tco._term2);
    if(_term2Cmp != 0) {
      return _term2Cmp;
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
   * @return true if obj is a Match and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Match) {

      Match peer = (Match) obj;
      return _term1==peer._term1 && _term2==peer._term2 && true;
    }
    return false;
  }


   //Term interface
  /**
   * Returns true if the term is rooted by the symbol Match
   *
   * @return true, because this is rooted by Match
   */
  @Override
  public boolean isMatch() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Term
   *
   * @return the attribute sa.rule.types.Term
   */
  @Override
  public sa.rule.types.Term getterm1() {
    return _term1;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Term
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Term which just has been set
   */
  @Override
  public sa.rule.types.Term setterm1(sa.rule.types.Term set_arg) {
    return make(set_arg, _term2);
  }
  
  /**
   * Returns the attribute sa.rule.types.Term
   *
   * @return the attribute sa.rule.types.Term
   */
  @Override
  public sa.rule.types.Term getterm2() {
    return _term2;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Term
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Term which just has been set
   */
  @Override
  public sa.rule.types.Term setterm2(sa.rule.types.Term set_arg) {
    return make(_term1, set_arg);
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
      case 0: return _term1;
      case 1: return _term2;
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
      case 0: return make((sa.rule.types.Term) v, _term2);
      case 1: return make(_term1, (sa.rule.types.Term) v);
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
    return new tom.library.sl.Visitable[] { _term1,  _term2};
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
    b = (1341284516<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_term1.hashCode() << 8);
    a += (_term2.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.Enumeration<sa.rule.types.Term>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.Enumeration<sa.rule.types.Term>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.Enumeration<sa.rule.types.Term>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Term> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.Enumeration<sa.rule.types.Term>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.Term> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Term> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Term,tom.library.enumerator.F<sa.rule.types.Term,sa.rule.types.Term>>) 
        new tom.library.enumerator.F<sa.rule.types.Term,tom.library.enumerator.F<sa.rule.types.Term,sa.rule.types.Term>>() {
          public tom.library.enumerator.F<sa.rule.types.Term,sa.rule.types.Term> apply(final sa.rule.types.Term t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.Term,sa.rule.types.Term>() {
          public sa.rule.types.Term apply(final sa.rule.types.Term t2) {
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
