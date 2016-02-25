
package sa.rule.types;


public abstract class ProductionList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected ProductionList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsConcProduction
   *
   * @return true if the term is rooted by the symbol ConsConcProduction
   */
  public boolean isConsConcProduction() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyConcProduction
   *
   * @return true if the term is rooted by the symbol EmptyConcProduction
   */
  public boolean isEmptyConcProduction() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot TailConcProduction
   *
   * @return the subterm corresponding to the slot TailConcProduction
   */
  public sa.rule.types.ProductionList getTailConcProduction() {
    throw new UnsupportedOperationException("This ProductionList has no TailConcProduction");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailConcProduction
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailConcProduction is replaced by _arg
   */
  public ProductionList setTailConcProduction(sa.rule.types.ProductionList _arg) {
    throw new UnsupportedOperationException("This ProductionList has no TailConcProduction");
  }

  /**
   * Returns the subterm corresponding to the slot HeadConcProduction
   *
   * @return the subterm corresponding to the slot HeadConcProduction
   */
  public sa.rule.types.Production getHeadConcProduction() {
    throw new UnsupportedOperationException("This ProductionList has no HeadConcProduction");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadConcProduction
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadConcProduction is replaced by _arg
   */
  public ProductionList setHeadConcProduction(sa.rule.types.Production _arg) {
    throw new UnsupportedOperationException("This ProductionList has no HeadConcProduction");
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
  public sa.rule.types.ProductionList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.Production> getCollectionConcProduction() {
    throw new UnsupportedOperationException("This ProductionList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.ProductionList> enumProductionList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.ProductionList> tmpenumProductionList = new tom.library.enumerator.Enumeration<sa.rule.types.ProductionList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.ProductionList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.ProductionList> getEnumeration() {
    if(enumProductionList == null) {
      enumProductionList = sa.rule.types.productionlist.EmptyConcProduction.funMake().apply(sa.rule.types.ProductionList.tmpenumProductionList)
        .plus(sa.rule.types.productionlist.ConsConcProduction.funMake().apply(sa.rule.types.Production.tmpenumProduction).apply(sa.rule.types.ProductionList.tmpenumProductionList));

      sa.rule.types.Production.getEnumeration();

      tmpenumProductionList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.ProductionList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.ProductionList>> _1() { return enumProductionList.parts(); }
      };

    }
    return enumProductionList;
  }

}
