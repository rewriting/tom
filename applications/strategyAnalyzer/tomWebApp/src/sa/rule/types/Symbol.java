
package sa.rule.types;


public abstract class Symbol extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Symbol() {}



  /**
   * Returns true if the term is rooted by the symbol Symbol
   *
   * @return true if the term is rooted by the symbol Symbol
   */
  public boolean isSymbol() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot arity
   *
   * @return the subterm corresponding to the slot arity
   */
  public int getarity() {
    throw new UnsupportedOperationException("This Symbol has no arity");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot arity
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot arity is replaced by _arg
   */
  public Symbol setarity(int _arg) {
    throw new UnsupportedOperationException("This Symbol has no arity");
  }

  /**
   * Returns the subterm corresponding to the slot name
   *
   * @return the subterm corresponding to the slot name
   */
  public String getname() {
    throw new UnsupportedOperationException("This Symbol has no name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot name is replaced by _arg
   */
  public Symbol setname(String _arg) {
    throw new UnsupportedOperationException("This Symbol has no name");
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
  public sa.rule.types.Symbol reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Symbol> enumSymbol = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Symbol> tmpenumSymbol = new tom.library.enumerator.Enumeration<sa.rule.types.Symbol>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Symbol>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Symbol> getEnumeration() {
    if(enumSymbol == null) {
      enumSymbol = sa.rule.types.symbol.Symbol.funMake().apply(tom.library.enumerator.Combinators.makeString()).apply(tom.library.enumerator.Combinators.makeint());


      tmpenumSymbol.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Symbol>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Symbol>> _1() { return enumSymbol.parts(); }
      };

    }
    return enumSymbol;
  }

}
