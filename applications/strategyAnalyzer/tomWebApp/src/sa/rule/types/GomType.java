
package sa.rule.types;


public abstract class GomType extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected GomType() {}



  /**
   * Returns true if the term is rooted by the symbol GomType
   *
   * @return true if the term is rooted by the symbol GomType
   */
  public boolean isGomType() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Name
   *
   * @return the subterm corresponding to the slot Name
   */
  public String getName() {
    throw new UnsupportedOperationException("This GomType has no Name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Name is replaced by _arg
   */
  public GomType setName(String _arg) {
    throw new UnsupportedOperationException("This GomType has no Name");
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
  public sa.rule.types.GomType reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.GomType> enumGomType = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.GomType> tmpenumGomType = new tom.library.enumerator.Enumeration<sa.rule.types.GomType>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.GomType>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.GomType> getEnumeration() {
    if(enumGomType == null) {
      enumGomType = sa.rule.types.gomtype.GomType.funMake().apply(tom.library.enumerator.Combinators.makeString());


      tmpenumGomType.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.GomType>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.GomType>> _1() { return enumGomType.parts(); }
      };

    }
    return enumGomType;
  }

}
