
package sa.rule.types;


public abstract class TermList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected TermList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsTermList
   *
   * @return true if the term is rooted by the symbol ConsTermList
   */
  public boolean isConsTermList() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyTermList
   *
   * @return true if the term is rooted by the symbol EmptyTermList
   */
  public boolean isEmptyTermList() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot HeadTermList
   *
   * @return the subterm corresponding to the slot HeadTermList
   */
  public sa.rule.types.Term getHeadTermList() {
    throw new UnsupportedOperationException("This TermList has no HeadTermList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadTermList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadTermList is replaced by _arg
   */
  public TermList setHeadTermList(sa.rule.types.Term _arg) {
    throw new UnsupportedOperationException("This TermList has no HeadTermList");
  }

  /**
   * Returns the subterm corresponding to the slot TailTermList
   *
   * @return the subterm corresponding to the slot TailTermList
   */
  public sa.rule.types.TermList getTailTermList() {
    throw new UnsupportedOperationException("This TermList has no TailTermList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailTermList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailTermList is replaced by _arg
   */
  public TermList setTailTermList(sa.rule.types.TermList _arg) {
    throw new UnsupportedOperationException("This TermList has no TailTermList");
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
  public sa.rule.types.TermList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.Term> getCollectionTermList() {
    throw new UnsupportedOperationException("This TermList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.TermList> enumTermList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.TermList> tmpenumTermList = new tom.library.enumerator.Enumeration<sa.rule.types.TermList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.TermList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.TermList> getEnumeration() {
    if(enumTermList == null) {
      enumTermList = sa.rule.types.termlist.EmptyTermList.funMake().apply(sa.rule.types.TermList.tmpenumTermList)
        .plus(sa.rule.types.termlist.ConsTermList.funMake().apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.TermList.tmpenumTermList));

      sa.rule.types.Term.getEnumeration();

      tmpenumTermList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.TermList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.TermList>> _1() { return enumTermList.parts(); }
      };

    }
    return enumTermList;
  }

}
