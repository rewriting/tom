
package sa.rule.types;


public abstract class Production extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Production() {}



  /**
   * Returns true if the term is rooted by the symbol SortType
   *
   * @return true if the term is rooted by the symbol SortType
   */
  public boolean isSortType() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot AlternativeList
   *
   * @return the subterm corresponding to the slot AlternativeList
   */
  public sa.rule.types.AlternativeList getAlternativeList() {
    throw new UnsupportedOperationException("This Production has no AlternativeList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot AlternativeList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot AlternativeList is replaced by _arg
   */
  public Production setAlternativeList(sa.rule.types.AlternativeList _arg) {
    throw new UnsupportedOperationException("This Production has no AlternativeList");
  }

  /**
   * Returns the subterm corresponding to the slot Type
   *
   * @return the subterm corresponding to the slot Type
   */
  public sa.rule.types.GomType getType() {
    throw new UnsupportedOperationException("This Production has no Type");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Type
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Type is replaced by _arg
   */
  public Production setType(sa.rule.types.GomType _arg) {
    throw new UnsupportedOperationException("This Production has no Type");
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
  public sa.rule.types.Production reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Production> enumProduction = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Production> tmpenumProduction = new tom.library.enumerator.Enumeration<sa.rule.types.Production>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Production>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Production> getEnumeration() {
    if(enumProduction == null) {
      enumProduction = sa.rule.types.production.SortType.funMake().apply(sa.rule.types.GomType.tmpenumGomType).apply(sa.rule.types.AlternativeList.tmpenumAlternativeList);

      sa.rule.types.AlternativeList.getEnumeration();
      sa.rule.types.GomType.getEnumeration();

      tmpenumProduction.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Production>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Production>> _1() { return enumProduction.parts(); }
      };

    }
    return enumProduction;
  }

}
