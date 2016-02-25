
package sa.rule.types;


public abstract class Program extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Program() {}



  /**
   * Returns true if the term is rooted by the symbol Program
   *
   * @return true if the term is rooted by the symbol Program
   */
  public boolean isProgram() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot productionList
   *
   * @return the subterm corresponding to the slot productionList
   */
  public sa.rule.types.ProductionList getproductionList() {
    throw new UnsupportedOperationException("This Program has no productionList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot productionList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot productionList is replaced by _arg
   */
  public Program setproductionList(sa.rule.types.ProductionList _arg) {
    throw new UnsupportedOperationException("This Program has no productionList");
  }

  /**
   * Returns the subterm corresponding to the slot trs
   *
   * @return the subterm corresponding to the slot trs
   */
  public sa.rule.types.Trs gettrs() {
    throw new UnsupportedOperationException("This Program has no trs");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot trs
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot trs is replaced by _arg
   */
  public Program settrs(sa.rule.types.Trs _arg) {
    throw new UnsupportedOperationException("This Program has no trs");
  }

  /**
   * Returns the subterm corresponding to the slot functionList
   *
   * @return the subterm corresponding to the slot functionList
   */
  public sa.rule.types.ProductionList getfunctionList() {
    throw new UnsupportedOperationException("This Program has no functionList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot functionList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot functionList is replaced by _arg
   */
  public Program setfunctionList(sa.rule.types.ProductionList _arg) {
    throw new UnsupportedOperationException("This Program has no functionList");
  }

  /**
   * Returns the subterm corresponding to the slot stratList
   *
   * @return the subterm corresponding to the slot stratList
   */
  public sa.rule.types.StratDeclList getstratList() {
    throw new UnsupportedOperationException("This Program has no stratList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot stratList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot stratList is replaced by _arg
   */
  public Program setstratList(sa.rule.types.StratDeclList _arg) {
    throw new UnsupportedOperationException("This Program has no stratList");
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
  public sa.rule.types.Program reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Program> enumProgram = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Program> tmpenumProgram = new tom.library.enumerator.Enumeration<sa.rule.types.Program>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Program>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Program> getEnumeration() {
    if(enumProgram == null) {
      enumProgram = sa.rule.types.program.Program.funMake().apply(sa.rule.types.ProductionList.tmpenumProductionList).apply(sa.rule.types.ProductionList.tmpenumProductionList).apply(sa.rule.types.StratDeclList.tmpenumStratDeclList).apply(sa.rule.types.Trs.tmpenumTrs);

      sa.rule.types.Trs.getEnumeration();
      sa.rule.types.ProductionList.getEnumeration();
      sa.rule.types.StratDeclList.getEnumeration();

      tmpenumProgram.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Program>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Program>> _1() { return enumProgram.parts(); }
      };

    }
    return enumProgram;
  }

}
