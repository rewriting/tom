
package sa.rule.types;


public abstract class Alternative extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Alternative() {}



  /**
   * Returns true if the term is rooted by the symbol Alternative
   *
   * @return true if the term is rooted by the symbol Alternative
   */
  public boolean isAlternative() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Codomain
   *
   * @return the subterm corresponding to the slot Codomain
   */
  public sa.rule.types.GomType getCodomain() {
    throw new UnsupportedOperationException("This Alternative has no Codomain");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Codomain
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Codomain is replaced by _arg
   */
  public Alternative setCodomain(sa.rule.types.GomType _arg) {
    throw new UnsupportedOperationException("This Alternative has no Codomain");
  }

  /**
   * Returns the subterm corresponding to the slot Name
   *
   * @return the subterm corresponding to the slot Name
   */
  public String getName() {
    throw new UnsupportedOperationException("This Alternative has no Name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Name is replaced by _arg
   */
  public Alternative setName(String _arg) {
    throw new UnsupportedOperationException("This Alternative has no Name");
  }

  /**
   * Returns the subterm corresponding to the slot DomainList
   *
   * @return the subterm corresponding to the slot DomainList
   */
  public sa.rule.types.FieldList getDomainList() {
    throw new UnsupportedOperationException("This Alternative has no DomainList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot DomainList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot DomainList is replaced by _arg
   */
  public Alternative setDomainList(sa.rule.types.FieldList _arg) {
    throw new UnsupportedOperationException("This Alternative has no DomainList");
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
  public sa.rule.types.Alternative reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Alternative> enumAlternative = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Alternative> tmpenumAlternative = new tom.library.enumerator.Enumeration<sa.rule.types.Alternative>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Alternative>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Alternative> getEnumeration() {
    if(enumAlternative == null) {
      enumAlternative = sa.rule.types.alternative.Alternative.funMake().apply(tom.library.enumerator.Combinators.makeString()).apply(sa.rule.types.FieldList.tmpenumFieldList).apply(sa.rule.types.GomType.tmpenumGomType);

      sa.rule.types.FieldList.getEnumeration();
      sa.rule.types.GomType.getEnumeration();

      tmpenumAlternative.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Alternative>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Alternative>> _1() { return enumAlternative.parts(); }
      };

    }
    return enumAlternative;
  }

}
