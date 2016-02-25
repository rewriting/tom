
package sa.rule.types;


public abstract class StratDeclList extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected StratDeclList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsConcStratDecl
   *
   * @return true if the term is rooted by the symbol ConsConcStratDecl
   */
  public boolean isConsConcStratDecl() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyConcStratDecl
   *
   * @return true if the term is rooted by the symbol EmptyConcStratDecl
   */
  public boolean isEmptyConcStratDecl() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot HeadConcStratDecl
   *
   * @return the subterm corresponding to the slot HeadConcStratDecl
   */
  public sa.rule.types.StratDecl getHeadConcStratDecl() {
    throw new UnsupportedOperationException("This StratDeclList has no HeadConcStratDecl");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadConcStratDecl
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadConcStratDecl is replaced by _arg
   */
  public StratDeclList setHeadConcStratDecl(sa.rule.types.StratDecl _arg) {
    throw new UnsupportedOperationException("This StratDeclList has no HeadConcStratDecl");
  }

  /**
   * Returns the subterm corresponding to the slot TailConcStratDecl
   *
   * @return the subterm corresponding to the slot TailConcStratDecl
   */
  public sa.rule.types.StratDeclList getTailConcStratDecl() {
    throw new UnsupportedOperationException("This StratDeclList has no TailConcStratDecl");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailConcStratDecl
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailConcStratDecl is replaced by _arg
   */
  public StratDeclList setTailConcStratDecl(sa.rule.types.StratDeclList _arg) {
    throw new UnsupportedOperationException("This StratDeclList has no TailConcStratDecl");
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
  public sa.rule.types.StratDeclList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<sa.rule.types.StratDecl> getCollectionConcStratDecl() {
    throw new UnsupportedOperationException("This StratDeclList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList> enumStratDeclList = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList> tmpenumStratDeclList = new tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.StratDeclList>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList> getEnumeration() {
    if(enumStratDeclList == null) {
      enumStratDeclList = sa.rule.types.stratdecllist.EmptyConcStratDecl.funMake().apply(sa.rule.types.StratDeclList.tmpenumStratDeclList)
        .plus(sa.rule.types.stratdecllist.ConsConcStratDecl.funMake().apply(sa.rule.types.StratDecl.tmpenumStratDecl).apply(sa.rule.types.StratDeclList.tmpenumStratDeclList));

      sa.rule.types.StratDecl.getEnumeration();

      tmpenumStratDeclList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.StratDeclList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.StratDeclList>> _1() { return enumStratDeclList.parts(); }
      };

    }
    return enumStratDeclList;
  }

}
