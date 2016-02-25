
package sa.rule.types;


public abstract class Rule extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Rule() {}



  /**
   * Returns true if the term is rooted by the symbol Rule
   *
   * @return true if the term is rooted by the symbol Rule
   */
  public boolean isRule() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol ConditionalRule
   *
   * @return true if the term is rooted by the symbol ConditionalRule
   */
  public boolean isConditionalRule() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot rhs
   *
   * @return the subterm corresponding to the slot rhs
   */
  public sa.rule.types.Term getrhs() {
    throw new UnsupportedOperationException("This Rule has no rhs");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot rhs
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot rhs is replaced by _arg
   */
  public Rule setrhs(sa.rule.types.Term _arg) {
    throw new UnsupportedOperationException("This Rule has no rhs");
  }

  /**
   * Returns the subterm corresponding to the slot cond
   *
   * @return the subterm corresponding to the slot cond
   */
  public sa.rule.types.Condition getcond() {
    throw new UnsupportedOperationException("This Rule has no cond");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot cond
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot cond is replaced by _arg
   */
  public Rule setcond(sa.rule.types.Condition _arg) {
    throw new UnsupportedOperationException("This Rule has no cond");
  }

  /**
   * Returns the subterm corresponding to the slot lhs
   *
   * @return the subterm corresponding to the slot lhs
   */
  public sa.rule.types.Term getlhs() {
    throw new UnsupportedOperationException("This Rule has no lhs");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot lhs
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot lhs is replaced by _arg
   */
  public Rule setlhs(sa.rule.types.Term _arg) {
    throw new UnsupportedOperationException("This Rule has no lhs");
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
  public sa.rule.types.Rule reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Rule> enumRule = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Rule> tmpenumRule = new tom.library.enumerator.Enumeration<sa.rule.types.Rule>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Rule>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Rule> getEnumeration() {
    if(enumRule == null) {
      enumRule = sa.rule.types.rule.Rule.funMake().apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.Term.tmpenumTerm)
        .plus(sa.rule.types.rule.ConditionalRule.funMake().apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.Condition.tmpenumCondition));

      sa.rule.types.Condition.getEnumeration();
      sa.rule.types.Term.getEnumeration();

      tmpenumRule.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Rule>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Rule>> _1() { return enumRule.parts(); }
      };

    }
    return enumRule;
  }

}
