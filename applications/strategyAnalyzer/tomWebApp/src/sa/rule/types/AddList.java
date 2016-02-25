
package sa.rule.types;


public abstract class AddList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected AddList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsConcAdd
   *
   * @return true if the term is rooted by the symbol ConsConcAdd
   */
  public boolean isConsConcAdd() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyConcAdd
   *
   * @return true if the term is rooted by the symbol EmptyConcAdd
   */
  public boolean isEmptyConcAdd() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot TailConcAdd
   *
   * @return the subterm corresponding to the slot TailConcAdd
   */
  public sa.rule.types.AddList getTailConcAdd() {
    throw new UnsupportedOperationException("This AddList has no TailConcAdd");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailConcAdd
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailConcAdd is replaced by _arg
   */
  public AddList setTailConcAdd(sa.rule.types.AddList _arg) {
    throw new UnsupportedOperationException("This AddList has no TailConcAdd");
  }

  /**
   * Returns the subterm corresponding to the slot HeadConcAdd
   *
   * @return the subterm corresponding to the slot HeadConcAdd
   */
  public sa.rule.types.Term getHeadConcAdd() {
    throw new UnsupportedOperationException("This AddList has no HeadConcAdd");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadConcAdd
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadConcAdd is replaced by _arg
   */
  public AddList setHeadConcAdd(sa.rule.types.Term _arg) {
    throw new UnsupportedOperationException("This AddList has no HeadConcAdd");
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
  public sa.rule.types.AddList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.Term> getCollectionConcAdd() {
    throw new UnsupportedOperationException("This AddList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.AddList> enumAddList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.AddList> tmpenumAddList = new tom.library.enumerator.Enumeration<sa.rule.types.AddList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.AddList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.AddList> getEnumeration() {
    if(enumAddList == null) {
      enumAddList = sa.rule.types.addlist.EmptyConcAdd.funMake().apply(sa.rule.types.AddList.tmpenumAddList)
        .plus(sa.rule.types.addlist.ConsConcAdd.funMake().apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.AddList.tmpenumAddList));

      sa.rule.types.Term.getEnumeration();

      tmpenumAddList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.AddList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.AddList>> _1() { return enumAddList.parts(); }
      };

    }
    return enumAddList;
  }

}
