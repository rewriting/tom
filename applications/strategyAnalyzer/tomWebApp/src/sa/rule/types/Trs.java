
package sa.rule.types;


public abstract class Trs extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Trs() {}



  /**
   * Returns true if the term is rooted by the symbol Otrs
   *
   * @return true if the term is rooted by the symbol Otrs
   */
  public boolean isOtrs() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Trs
   *
   * @return true if the term is rooted by the symbol Trs
   */
  public boolean isTrs() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyTrs
   *
   * @return true if the term is rooted by the symbol EmptyTrs
   */
  public boolean isEmptyTrs() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot list
   *
   * @return the subterm corresponding to the slot list
   */
  public sa.rule.types.RuleList getlist() {
    throw new UnsupportedOperationException("This Trs has no list");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot list
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot list is replaced by _arg
   */
  public Trs setlist(sa.rule.types.RuleList _arg) {
    throw new UnsupportedOperationException("This Trs has no list");
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
  public sa.rule.types.Trs reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Trs> enumTrs = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Trs> tmpenumTrs = new tom.library.enumerator.Enumeration<sa.rule.types.Trs>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Trs>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Trs> getEnumeration() {
    if(enumTrs == null) {
      enumTrs = sa.rule.types.trs.Otrs.funMake().apply(sa.rule.types.RuleList.tmpenumRuleList)
        .plus(sa.rule.types.trs.Trs.funMake().apply(sa.rule.types.RuleList.tmpenumRuleList))
        .plus(sa.rule.types.trs.EmptyTrs.funMake().apply(sa.rule.types.Trs.tmpenumTrs));

      sa.rule.types.RuleList.getEnumeration();

      tmpenumTrs.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Trs>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Trs>> _1() { return enumTrs.parts(); }
      };

    }
    return enumTrs;
  }

}
