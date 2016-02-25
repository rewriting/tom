
package sa.rule.types.rulelist;



public final class ConsConcRule extends sa.rule.types.rulelist.ConcRule implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsConcRule";


  private ConsConcRule() {}
  private int hashCode;
  private static ConsConcRule gomProto = new ConsConcRule();
  
   private sa.rule.types.Rule[] children;
                  private sa.rule.types.Rule _HeadConcRule;
  private sa.rule.types.RuleList _TailConcRule;

  /**
   * Constructor that builds a term rooted by ConsConcRule
   *
   * @return a term rooted by ConsConcRule
   */

  public static ConsConcRule make(sa.rule.types.Rule _HeadConcRule, sa.rule.types.RuleList _TailConcRule) {

    // use the proto as a model
    gomProto.initHashCode( _HeadConcRule,  _TailConcRule);
    return (ConsConcRule) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcRule
   * @param _TailConcRule
   * @param hashCode hashCode of ConsConcRule
   */
  private void init(sa.rule.types.Rule _HeadConcRule, sa.rule.types.RuleList _TailConcRule, int hashCode) {
    this._HeadConcRule = _HeadConcRule;
    this._TailConcRule = _TailConcRule;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcRule
   * @param _TailConcRule
   */
  private void initHashCode(sa.rule.types.Rule _HeadConcRule, sa.rule.types.RuleList _TailConcRule) {
    this._HeadConcRule = _HeadConcRule;
    this._TailConcRule = _TailConcRule;

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
    return "ConsConcRule";
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
    ConsConcRule clone = new ConsConcRule();
    clone.init( _HeadConcRule,  _TailConcRule, hashCode);
    return clone;
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
    ConsConcRule tco = (ConsConcRule) ao;
    int _HeadConcRuleCmp = (this._HeadConcRule).compareToLPO(tco._HeadConcRule);
    if(_HeadConcRuleCmp != 0) {
      return _HeadConcRuleCmp;
    }

    int _TailConcRuleCmp = (this._TailConcRule).compareToLPO(tco._TailConcRule);
    if(_TailConcRuleCmp != 0) {
      return _TailConcRuleCmp;
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
    ConsConcRule tco = (ConsConcRule) ao;
    int _HeadConcRuleCmp = (this._HeadConcRule).compareTo(tco._HeadConcRule);
    if(_HeadConcRuleCmp != 0) {
      return _HeadConcRuleCmp;
    }

    int _TailConcRuleCmp = (this._TailConcRule).compareTo(tco._TailConcRule);
    if(_TailConcRuleCmp != 0) {
      return _TailConcRuleCmp;
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
   * @return true if obj is a ConsConcRule and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsConcRule) {

      ConsConcRule peer = (ConsConcRule) obj;
      return _HeadConcRule==peer._HeadConcRule && _TailConcRule==peer._TailConcRule && true;
    }
    return false;
  }


   //RuleList interface
  /**
   * Returns true if the term is rooted by the symbol ConsConcRule
   *
   * @return true, because this is rooted by ConsConcRule
   */
  @Override
  public boolean isConsConcRule() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Rule
   *
   * @return the attribute sa.rule.types.Rule
   */
  @Override
  public sa.rule.types.Rule getHeadConcRule() {
    return _HeadConcRule;
  }

  /**
   * Sets and returns the attribute sa.rule.types.RuleList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Rule which just has been set
   */
  @Override
  public sa.rule.types.RuleList setHeadConcRule(sa.rule.types.Rule set_arg) {
    return make(set_arg, _TailConcRule);
  }
  
  /**
   * Returns the attribute sa.rule.types.RuleList
   *
   * @return the attribute sa.rule.types.RuleList
   */
  @Override
  public sa.rule.types.RuleList getTailConcRule() {
    return _TailConcRule;
  }

  /**
   * Sets and returns the attribute sa.rule.types.RuleList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.RuleList which just has been set
   */
  @Override
  public sa.rule.types.RuleList setTailConcRule(sa.rule.types.RuleList set_arg) {
    return make(_HeadConcRule, set_arg);
  }
  
  /* Visitable */
  /**
   * Returns the number of children of the term
   *
   * @return the number of children of the term
   */
  public int getChildCount() {
    return getChildren().length;
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
    return getChildren()[index];
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
    
      tom.library.sl.Visitable[] children = getChildren();
      sa.rule.types.Rule[] new_children = new sa.rule.types.Rule[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.Rule) children[i]);
      }
     new_children[index] = (sa.rule.types.Rule) v;
     return fromArray(new_children);
                  
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
    if (children.length == getChildCount() ) {
      
               sa.rule.types.Rule[] typed_children = new sa.rule.types.Rule[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.Rule) children[i];
              }
              return fromArray(typed_children);
              
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
    
        if (children == null) {
          children = toArray(new sa.rule.types.Rule[]{});
        }
        return java.util.Arrays.copyOf(children,children.length);
      
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
    b = (64845584<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadConcRule.hashCode() << 8);
    a += (_TailConcRule.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Rule>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.RuleList>,tom.library.enumerator.Enumeration<sa.rule.types.RuleList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Rule>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.RuleList>,tom.library.enumerator.Enumeration<sa.rule.types.RuleList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.RuleList>,tom.library.enumerator.Enumeration<sa.rule.types.RuleList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Rule> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.RuleList>,tom.library.enumerator.Enumeration<sa.rule.types.RuleList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.RuleList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.RuleList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Rule,tom.library.enumerator.F<sa.rule.types.RuleList,sa.rule.types.RuleList>>) 
        new tom.library.enumerator.F<sa.rule.types.Rule,tom.library.enumerator.F<sa.rule.types.RuleList,sa.rule.types.RuleList>>() {
          public tom.library.enumerator.F<sa.rule.types.RuleList,sa.rule.types.RuleList> apply(final sa.rule.types.Rule t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.RuleList,sa.rule.types.RuleList>() {
          public sa.rule.types.RuleList apply(final sa.rule.types.RuleList t2) {
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
