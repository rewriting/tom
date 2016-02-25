
package sa.rule.types;


public abstract class AlternativeList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected AlternativeList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsConcAlternative
   *
   * @return true if the term is rooted by the symbol ConsConcAlternative
   */
  public boolean isConsConcAlternative() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyConcAlternative
   *
   * @return true if the term is rooted by the symbol EmptyConcAlternative
   */
  public boolean isEmptyConcAlternative() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot HeadConcAlternative
   *
   * @return the subterm corresponding to the slot HeadConcAlternative
   */
  public sa.rule.types.Alternative getHeadConcAlternative() {
    throw new UnsupportedOperationException("This AlternativeList has no HeadConcAlternative");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadConcAlternative
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadConcAlternative is replaced by _arg
   */
  public AlternativeList setHeadConcAlternative(sa.rule.types.Alternative _arg) {
    throw new UnsupportedOperationException("This AlternativeList has no HeadConcAlternative");
  }

  /**
   * Returns the subterm corresponding to the slot TailConcAlternative
   *
   * @return the subterm corresponding to the slot TailConcAlternative
   */
  public sa.rule.types.AlternativeList getTailConcAlternative() {
    throw new UnsupportedOperationException("This AlternativeList has no TailConcAlternative");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailConcAlternative
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailConcAlternative is replaced by _arg
   */
  public AlternativeList setTailConcAlternative(sa.rule.types.AlternativeList _arg) {
    throw new UnsupportedOperationException("This AlternativeList has no TailConcAlternative");
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
  public sa.rule.types.AlternativeList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.Alternative> getCollectionConcAlternative() {
    throw new UnsupportedOperationException("This AlternativeList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList> enumAlternativeList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList> tmpenumAlternativeList = new tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.AlternativeList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList> getEnumeration() {
    if(enumAlternativeList == null) {
      enumAlternativeList = sa.rule.types.alternativelist.EmptyConcAlternative.funMake().apply(sa.rule.types.AlternativeList.tmpenumAlternativeList)
        .plus(sa.rule.types.alternativelist.ConsConcAlternative.funMake().apply(sa.rule.types.Alternative.tmpenumAlternative).apply(sa.rule.types.AlternativeList.tmpenumAlternativeList));

      sa.rule.types.Alternative.getEnumeration();

      tmpenumAlternativeList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.AlternativeList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.AlternativeList>> _1() { return enumAlternativeList.parts(); }
      };

    }
    return enumAlternativeList;
  }

}
