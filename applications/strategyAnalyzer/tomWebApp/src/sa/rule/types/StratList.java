
package sa.rule.types;


public abstract class StratList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected StratList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsConcStrat
   *
   * @return true if the term is rooted by the symbol ConsConcStrat
   */
  public boolean isConsConcStrat() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyConcStrat
   *
   * @return true if the term is rooted by the symbol EmptyConcStrat
   */
  public boolean isEmptyConcStrat() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot HeadConcStrat
   *
   * @return the subterm corresponding to the slot HeadConcStrat
   */
  public sa.rule.types.Strat getHeadConcStrat() {
    throw new UnsupportedOperationException("This StratList has no HeadConcStrat");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadConcStrat
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadConcStrat is replaced by _arg
   */
  public StratList setHeadConcStrat(sa.rule.types.Strat _arg) {
    throw new UnsupportedOperationException("This StratList has no HeadConcStrat");
  }

  /**
   * Returns the subterm corresponding to the slot TailConcStrat
   *
   * @return the subterm corresponding to the slot TailConcStrat
   */
  public sa.rule.types.StratList getTailConcStrat() {
    throw new UnsupportedOperationException("This StratList has no TailConcStrat");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailConcStrat
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailConcStrat is replaced by _arg
   */
  public StratList setTailConcStrat(sa.rule.types.StratList _arg) {
    throw new UnsupportedOperationException("This StratList has no TailConcStrat");
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
  public sa.rule.types.StratList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.Strat> getCollectionConcStrat() {
    throw new UnsupportedOperationException("This StratList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.StratList> enumStratList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.StratList> tmpenumStratList = new tom.library.enumerator.Enumeration<sa.rule.types.StratList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.StratList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.StratList> getEnumeration() {
    if(enumStratList == null) {
      enumStratList = sa.rule.types.stratlist.EmptyConcStrat.funMake().apply(sa.rule.types.StratList.tmpenumStratList)
        .plus(sa.rule.types.stratlist.ConsConcStrat.funMake().apply(sa.rule.types.Strat.tmpenumStrat).apply(sa.rule.types.StratList.tmpenumStratList));

      sa.rule.types.Strat.getEnumeration();

      tmpenumStratList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.StratList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.StratList>> _1() { return enumStratList.parts(); }
      };

    }
    return enumStratList;
  }

}
