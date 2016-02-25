
package sa.rule.types;


public abstract class Param extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Param() {}



  /**
   * Returns true if the term is rooted by the symbol Param
   *
   * @return true if the term is rooted by the symbol Param
   */
  public boolean isParam() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Name
   *
   * @return the subterm corresponding to the slot Name
   */
  public String getName() {
    throw new UnsupportedOperationException("This Param has no Name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Name is replaced by _arg
   */
  public Param setName(String _arg) {
    throw new UnsupportedOperationException("This Param has no Name");
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
  public sa.rule.types.Param reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Param> enumParam = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Param> tmpenumParam = new tom.library.enumerator.Enumeration<sa.rule.types.Param>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Param>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Param> getEnumeration() {
    if(enumParam == null) {
      enumParam = sa.rule.types.param.Param.funMake().apply(tom.library.enumerator.Combinators.makeString());


      tmpenumParam.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Param>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Param>> _1() { return enumParam.parts(); }
      };

    }
    return enumParam;
  }

}
