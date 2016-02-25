
package sa.rule.types;


public abstract class ParamList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected ParamList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsConcParam
   *
   * @return true if the term is rooted by the symbol ConsConcParam
   */
  public boolean isConsConcParam() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyConcParam
   *
   * @return true if the term is rooted by the symbol EmptyConcParam
   */
  public boolean isEmptyConcParam() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot HeadConcParam
   *
   * @return the subterm corresponding to the slot HeadConcParam
   */
  public sa.rule.types.Param getHeadConcParam() {
    throw new UnsupportedOperationException("This ParamList has no HeadConcParam");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadConcParam
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadConcParam is replaced by _arg
   */
  public ParamList setHeadConcParam(sa.rule.types.Param _arg) {
    throw new UnsupportedOperationException("This ParamList has no HeadConcParam");
  }

  /**
   * Returns the subterm corresponding to the slot TailConcParam
   *
   * @return the subterm corresponding to the slot TailConcParam
   */
  public sa.rule.types.ParamList getTailConcParam() {
    throw new UnsupportedOperationException("This ParamList has no TailConcParam");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailConcParam
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailConcParam is replaced by _arg
   */
  public ParamList setTailConcParam(sa.rule.types.ParamList _arg) {
    throw new UnsupportedOperationException("This ParamList has no TailConcParam");
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
  public sa.rule.types.ParamList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.Param> getCollectionConcParam() {
    throw new UnsupportedOperationException("This ParamList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.ParamList> enumParamList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.ParamList> tmpenumParamList = new tom.library.enumerator.Enumeration<sa.rule.types.ParamList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.ParamList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.ParamList> getEnumeration() {
    if(enumParamList == null) {
      enumParamList = sa.rule.types.paramlist.EmptyConcParam.funMake().apply(sa.rule.types.ParamList.tmpenumParamList)
        .plus(sa.rule.types.paramlist.ConsConcParam.funMake().apply(sa.rule.types.Param.tmpenumParam).apply(sa.rule.types.ParamList.tmpenumParamList));

      sa.rule.types.Param.getEnumeration();

      tmpenumParamList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.ParamList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.ParamList>> _1() { return enumParamList.parts(); }
      };

    }
    return enumParamList;
  }

}
