
package sa.rule.types;


public abstract class StratDecl extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected StratDecl() {}



  /**
   * Returns true if the term is rooted by the symbol StratDecl
   *
   * @return true if the term is rooted by the symbol StratDecl
   */
  public boolean isStratDecl() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Body
   *
   * @return the subterm corresponding to the slot Body
   */
  public sa.rule.types.Strat getBody() {
    throw new UnsupportedOperationException("This StratDecl has no Body");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Body
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Body is replaced by _arg
   */
  public StratDecl setBody(sa.rule.types.Strat _arg) {
    throw new UnsupportedOperationException("This StratDecl has no Body");
  }

  /**
   * Returns the subterm corresponding to the slot Name
   *
   * @return the subterm corresponding to the slot Name
   */
  public String getName() {
    throw new UnsupportedOperationException("This StratDecl has no Name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Name is replaced by _arg
   */
  public StratDecl setName(String _arg) {
    throw new UnsupportedOperationException("This StratDecl has no Name");
  }

  /**
   * Returns the subterm corresponding to the slot ParamList
   *
   * @return the subterm corresponding to the slot ParamList
   */
  public sa.rule.types.ParamList getParamList() {
    throw new UnsupportedOperationException("This StratDecl has no ParamList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot ParamList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot ParamList is replaced by _arg
   */
  public StratDecl setParamList(sa.rule.types.ParamList _arg) {
    throw new UnsupportedOperationException("This StratDecl has no ParamList");
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
  public sa.rule.types.StratDecl reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.StratDecl> enumStratDecl = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.StratDecl> tmpenumStratDecl = new tom.library.enumerator.Enumeration<sa.rule.types.StratDecl>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.StratDecl>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.StratDecl> getEnumeration() {
    if(enumStratDecl == null) {
      enumStratDecl = sa.rule.types.stratdecl.StratDecl.funMake().apply(tom.library.enumerator.Combinators.makeString()).apply(sa.rule.types.ParamList.tmpenumParamList).apply(sa.rule.types.Strat.tmpenumStrat);

      sa.rule.types.ParamList.getEnumeration();
      sa.rule.types.Strat.getEnumeration();

      tmpenumStratDecl.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.StratDecl>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.StratDecl>> _1() { return enumStratDecl.parts(); }
      };

    }
    return enumStratDecl;
  }

}
