
package sa.rule.types;


public abstract class GomTypeList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected GomTypeList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsConcGomType
   *
   * @return true if the term is rooted by the symbol ConsConcGomType
   */
  public boolean isConsConcGomType() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyConcGomType
   *
   * @return true if the term is rooted by the symbol EmptyConcGomType
   */
  public boolean isEmptyConcGomType() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot TailConcGomType
   *
   * @return the subterm corresponding to the slot TailConcGomType
   */
  public sa.rule.types.GomTypeList getTailConcGomType() {
    throw new UnsupportedOperationException("This GomTypeList has no TailConcGomType");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailConcGomType
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailConcGomType is replaced by _arg
   */
  public GomTypeList setTailConcGomType(sa.rule.types.GomTypeList _arg) {
    throw new UnsupportedOperationException("This GomTypeList has no TailConcGomType");
  }

  /**
   * Returns the subterm corresponding to the slot HeadConcGomType
   *
   * @return the subterm corresponding to the slot HeadConcGomType
   */
  public sa.rule.types.GomType getHeadConcGomType() {
    throw new UnsupportedOperationException("This GomTypeList has no HeadConcGomType");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadConcGomType
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadConcGomType is replaced by _arg
   */
  public GomTypeList setHeadConcGomType(sa.rule.types.GomType _arg) {
    throw new UnsupportedOperationException("This GomTypeList has no HeadConcGomType");
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
  public sa.rule.types.GomTypeList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.GomType> getCollectionConcGomType() {
    throw new UnsupportedOperationException("This GomTypeList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList> enumGomTypeList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList> tmpenumGomTypeList = new tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.GomTypeList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList> getEnumeration() {
    if(enumGomTypeList == null) {
      enumGomTypeList = sa.rule.types.gomtypelist.EmptyConcGomType.funMake().apply(sa.rule.types.GomTypeList.tmpenumGomTypeList)
        .plus(sa.rule.types.gomtypelist.ConsConcGomType.funMake().apply(sa.rule.types.GomType.tmpenumGomType).apply(sa.rule.types.GomTypeList.tmpenumGomTypeList));

      sa.rule.types.GomType.getEnumeration();

      tmpenumGomTypeList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.GomTypeList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.GomTypeList>> _1() { return enumGomTypeList.parts(); }
      };

    }
    return enumGomTypeList;
  }

}
