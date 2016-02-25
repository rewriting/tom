
package sa.rule.types;


public abstract class Strat extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Strat() {}



  /**
   * Returns true if the term is rooted by the symbol StratName
   *
   * @return true if the term is rooted by the symbol StratName
   */
  public boolean isStratName() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratRule
   *
   * @return true if the term is rooted by the symbol StratRule
   */
  public boolean isStratRule() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratSequence
   *
   * @return true if the term is rooted by the symbol StratSequence
   */
  public boolean isStratSequence() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratChoice
   *
   * @return true if the term is rooted by the symbol StratChoice
   */
  public boolean isStratChoice() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratIdentity
   *
   * @return true if the term is rooted by the symbol StratIdentity
   */
  public boolean isStratIdentity() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratFail
   *
   * @return true if the term is rooted by the symbol StratFail
   */
  public boolean isStratFail() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratAll
   *
   * @return true if the term is rooted by the symbol StratAll
   */
  public boolean isStratAll() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratOne
   *
   * @return true if the term is rooted by the symbol StratOne
   */
  public boolean isStratOne() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratTrs
   *
   * @return true if the term is rooted by the symbol StratTrs
   */
  public boolean isStratTrs() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratMu
   *
   * @return true if the term is rooted by the symbol StratMu
   */
  public boolean isStratMu() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol StratAppl
   *
   * @return true if the term is rooted by the symbol StratAppl
   */
  public boolean isStratAppl() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot trs
   *
   * @return the subterm corresponding to the slot trs
   */
  public sa.rule.types.Trs gettrs() {
    throw new UnsupportedOperationException("This Strat has no trs");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot trs
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot trs is replaced by _arg
   */
  public Strat settrs(sa.rule.types.Trs _arg) {
    throw new UnsupportedOperationException("This Strat has no trs");
  }

  /**
   * Returns the subterm corresponding to the slot rule
   *
   * @return the subterm corresponding to the slot rule
   */
  public sa.rule.types.Rule getrule() {
    throw new UnsupportedOperationException("This Strat has no rule");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot rule
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot rule is replaced by _arg
   */
  public Strat setrule(sa.rule.types.Rule _arg) {
    throw new UnsupportedOperationException("This Strat has no rule");
  }

  /**
   * Returns the subterm corresponding to the slot name
   *
   * @return the subterm corresponding to the slot name
   */
  public String getname() {
    throw new UnsupportedOperationException("This Strat has no name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot name is replaced by _arg
   */
  public Strat setname(String _arg) {
    throw new UnsupportedOperationException("This Strat has no name");
  }

  /**
   * Returns the subterm corresponding to the slot s1
   *
   * @return the subterm corresponding to the slot s1
   */
  public sa.rule.types.Strat gets1() {
    throw new UnsupportedOperationException("This Strat has no s1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot s1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot s1 is replaced by _arg
   */
  public Strat sets1(sa.rule.types.Strat _arg) {
    throw new UnsupportedOperationException("This Strat has no s1");
  }

  /**
   * Returns the subterm corresponding to the slot s
   *
   * @return the subterm corresponding to the slot s
   */
  public sa.rule.types.Strat gets() {
    throw new UnsupportedOperationException("This Strat has no s");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot s
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot s is replaced by _arg
   */
  public Strat sets(sa.rule.types.Strat _arg) {
    throw new UnsupportedOperationException("This Strat has no s");
  }

  /**
   * Returns the subterm corresponding to the slot args
   *
   * @return the subterm corresponding to the slot args
   */
  public sa.rule.types.StratList getargs() {
    throw new UnsupportedOperationException("This Strat has no args");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot args
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot args is replaced by _arg
   */
  public Strat setargs(sa.rule.types.StratList _arg) {
    throw new UnsupportedOperationException("This Strat has no args");
  }

  /**
   * Returns the subterm corresponding to the slot s2
   *
   * @return the subterm corresponding to the slot s2
   */
  public sa.rule.types.Strat gets2() {
    throw new UnsupportedOperationException("This Strat has no s2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot s2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot s2 is replaced by _arg
   */
  public Strat sets2(sa.rule.types.Strat _arg) {
    throw new UnsupportedOperationException("This Strat has no s2");
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
  public sa.rule.types.Strat reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Strat> enumStrat = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Strat> tmpenumStrat = new tom.library.enumerator.Enumeration<sa.rule.types.Strat>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Strat>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Strat> getEnumeration() {
    if(enumStrat == null) {
      enumStrat = sa.rule.types.strat.StratName.funMake().apply(tom.library.enumerator.Combinators.makeString())
        .plus(sa.rule.types.strat.StratRule.funMake().apply(sa.rule.types.Rule.tmpenumRule))
        .plus(sa.rule.types.strat.StratSequence.funMake().apply(sa.rule.types.Strat.tmpenumStrat).apply(sa.rule.types.Strat.tmpenumStrat))
        .plus(sa.rule.types.strat.StratChoice.funMake().apply(sa.rule.types.Strat.tmpenumStrat).apply(sa.rule.types.Strat.tmpenumStrat))
        .plus(sa.rule.types.strat.StratIdentity.funMake().apply(sa.rule.types.Strat.tmpenumStrat))
        .plus(sa.rule.types.strat.StratFail.funMake().apply(sa.rule.types.Strat.tmpenumStrat))
        .plus(sa.rule.types.strat.StratAll.funMake().apply(sa.rule.types.Strat.tmpenumStrat))
        .plus(sa.rule.types.strat.StratOne.funMake().apply(sa.rule.types.Strat.tmpenumStrat))
        .plus(sa.rule.types.strat.StratTrs.funMake().apply(sa.rule.types.Trs.tmpenumTrs))
        .plus(sa.rule.types.strat.StratMu.funMake().apply(tom.library.enumerator.Combinators.makeString()).apply(sa.rule.types.Strat.tmpenumStrat))
        .plus(sa.rule.types.strat.StratAppl.funMake().apply(tom.library.enumerator.Combinators.makeString()).apply(sa.rule.types.StratList.tmpenumStratList));

      sa.rule.types.Trs.getEnumeration();
      sa.rule.types.StratList.getEnumeration();
      sa.rule.types.Rule.getEnumeration();

      tmpenumStrat.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Strat>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Strat>> _1() { return enumStrat.parts(); }
      };

    }
    return enumStrat;
  }

}
