
package sa.rule.types;


public abstract class RuleList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected RuleList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsConcRule
   *
   * @return true if the term is rooted by the symbol ConsConcRule
   */
  public boolean isConsConcRule() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyConcRule
   *
   * @return true if the term is rooted by the symbol EmptyConcRule
   */
  public boolean isEmptyConcRule() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot TailConcRule
   *
   * @return the subterm corresponding to the slot TailConcRule
   */
  public sa.rule.types.RuleList getTailConcRule() {
    throw new UnsupportedOperationException("This RuleList has no TailConcRule");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailConcRule
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailConcRule is replaced by _arg
   */
  public RuleList setTailConcRule(sa.rule.types.RuleList _arg) {
    throw new UnsupportedOperationException("This RuleList has no TailConcRule");
  }

  /**
   * Returns the subterm corresponding to the slot HeadConcRule
   *
   * @return the subterm corresponding to the slot HeadConcRule
   */
  public sa.rule.types.Rule getHeadConcRule() {
    throw new UnsupportedOperationException("This RuleList has no HeadConcRule");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadConcRule
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadConcRule is replaced by _arg
   */
  public RuleList setHeadConcRule(sa.rule.types.Rule _arg) {
    throw new UnsupportedOperationException("This RuleList has no HeadConcRule");
  }

  /**
   * Returns the length of the list
   *
   * @return the length of the list
   * @throws IllegalArgumentException if the term is not a list
   */
  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  /**
   * Returns an inverted term
   *
   * @return the inverted list
   * @throws IllegalArgumentException if the term is not a list
   */
  public sa.rule.types.RuleList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.Rule> getCollectionConcRule() {
    throw new UnsupportedOperationException("This RuleList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.RuleList> enumRuleList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.RuleList> tmpenumRuleList = new tom.library.enumerator.Enumeration<sa.rule.types.RuleList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.RuleList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.RuleList> getEnumeration() {
    if(enumRuleList == null) {
      enumRuleList = sa.rule.types.rulelist.EmptyConcRule.funMake().apply(sa.rule.types.RuleList.tmpenumRuleList)
        .plus(sa.rule.types.rulelist.ConsConcRule.funMake().apply(sa.rule.types.Rule.tmpenumRule).apply(sa.rule.types.RuleList.tmpenumRuleList));

      sa.rule.types.Rule.getEnumeration();

      tmpenumRuleList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.RuleList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.RuleList>> _1() { return enumRuleList.parts(); }
      };

    }
    return enumRuleList;
  }

}
