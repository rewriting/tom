
package sa.rule.types;


public abstract class FieldList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected FieldList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsConcField
   *
   * @return true if the term is rooted by the symbol ConsConcField
   */
  public boolean isConsConcField() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyConcField
   *
   * @return true if the term is rooted by the symbol EmptyConcField
   */
  public boolean isEmptyConcField() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot HeadConcField
   *
   * @return the subterm corresponding to the slot HeadConcField
   */
  public sa.rule.types.Field getHeadConcField() {
    throw new UnsupportedOperationException("This FieldList has no HeadConcField");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadConcField
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadConcField is replaced by _arg
   */
  public FieldList setHeadConcField(sa.rule.types.Field _arg) {
    throw new UnsupportedOperationException("This FieldList has no HeadConcField");
  }

  /**
   * Returns the subterm corresponding to the slot TailConcField
   *
   * @return the subterm corresponding to the slot TailConcField
   */
  public sa.rule.types.FieldList getTailConcField() {
    throw new UnsupportedOperationException("This FieldList has no TailConcField");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailConcField
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailConcField is replaced by _arg
   */
  public FieldList setTailConcField(sa.rule.types.FieldList _arg) {
    throw new UnsupportedOperationException("This FieldList has no TailConcField");
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
  public sa.rule.types.FieldList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.Field> getCollectionConcField() {
    throw new UnsupportedOperationException("This FieldList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.FieldList> enumFieldList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.FieldList> tmpenumFieldList = new tom.library.enumerator.Enumeration<sa.rule.types.FieldList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.FieldList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.FieldList> getEnumeration() {
    if(enumFieldList == null) {
      enumFieldList = sa.rule.types.fieldlist.EmptyConcField.funMake().apply(sa.rule.types.FieldList.tmpenumFieldList)
        .plus(sa.rule.types.fieldlist.ConsConcField.funMake().apply(sa.rule.types.Field.tmpenumField).apply(sa.rule.types.FieldList.tmpenumFieldList));

      sa.rule.types.Field.getEnumeration();

      tmpenumFieldList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.FieldList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.FieldList>> _1() { return enumFieldList.parts(); }
      };

    }
    return enumFieldList;
  }

}
