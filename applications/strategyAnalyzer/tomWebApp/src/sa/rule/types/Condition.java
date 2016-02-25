
package sa.rule.types;


public abstract class Condition extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Condition() {}



  /**
   * Returns true if the term is rooted by the symbol CondEquals
   *
   * @return true if the term is rooted by the symbol CondEquals
   */
  public boolean isCondEquals() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol CondAnd
   *
   * @return true if the term is rooted by the symbol CondAnd
   */
  public boolean isCondAnd() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol CondNot
   *
   * @return true if the term is rooted by the symbol CondNot
   */
  public boolean isCondNot() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol CondTrue
   *
   * @return true if the term is rooted by the symbol CondTrue
   */
  public boolean isCondTrue() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol CondFalse
   *
   * @return true if the term is rooted by the symbol CondFalse
   */
  public boolean isCondFalse() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot c2
   *
   * @return the subterm corresponding to the slot c2
   */
  public sa.rule.types.Condition getc2() {
    throw new UnsupportedOperationException("This Condition has no c2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot c2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot c2 is replaced by _arg
   */
  public Condition setc2(sa.rule.types.Condition _arg) {
    throw new UnsupportedOperationException("This Condition has no c2");
  }

  /**
   * Returns the subterm corresponding to the slot c1
   *
   * @return the subterm corresponding to the slot c1
   */
  public sa.rule.types.Condition getc1() {
    throw new UnsupportedOperationException("This Condition has no c1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot c1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot c1 is replaced by _arg
   */
  public Condition setc1(sa.rule.types.Condition _arg) {
    throw new UnsupportedOperationException("This Condition has no c1");
  }

  /**
   * Returns the subterm corresponding to the slot t1
   *
   * @return the subterm corresponding to the slot t1
   */
  public sa.rule.types.Term gett1() {
    throw new UnsupportedOperationException("This Condition has no t1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot t1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot t1 is replaced by _arg
   */
  public Condition sett1(sa.rule.types.Term _arg) {
    throw new UnsupportedOperationException("This Condition has no t1");
  }

  /**
   * Returns the subterm corresponding to the slot t2
   *
   * @return the subterm corresponding to the slot t2
   */
  public sa.rule.types.Term gett2() {
    throw new UnsupportedOperationException("This Condition has no t2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot t2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot t2 is replaced by _arg
   */
  public Condition sett2(sa.rule.types.Term _arg) {
    throw new UnsupportedOperationException("This Condition has no t2");
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
  public sa.rule.types.Condition reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Condition> enumCondition = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Condition> tmpenumCondition = new tom.library.enumerator.Enumeration<sa.rule.types.Condition>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Condition>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Condition> getEnumeration() {
    if(enumCondition == null) {
      enumCondition = sa.rule.types.condition.CondEquals.funMake().apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.Term.tmpenumTerm)
        .plus(sa.rule.types.condition.CondAnd.funMake().apply(sa.rule.types.Condition.tmpenumCondition).apply(sa.rule.types.Condition.tmpenumCondition))
        .plus(sa.rule.types.condition.CondNot.funMake().apply(sa.rule.types.Condition.tmpenumCondition))
        .plus(sa.rule.types.condition.CondTrue.funMake().apply(sa.rule.types.Condition.tmpenumCondition))
        .plus(sa.rule.types.condition.CondFalse.funMake().apply(sa.rule.types.Condition.tmpenumCondition));

      sa.rule.types.Term.getEnumeration();

      tmpenumCondition.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Condition>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Condition>> _1() { return enumCondition.parts(); }
      };

    }
    return enumCondition;
  }

}
