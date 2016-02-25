
package sa.rule.types;


public abstract class TypeEnvironment extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected TypeEnvironment() {}



  /**
   * Returns true if the term is rooted by the symbol EmptyEnvironment
   *
   * @return true if the term is rooted by the symbol EmptyEnvironment
   */
  public boolean isEmptyEnvironment() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol PushEnvironment
   *
   * @return true if the term is rooted by the symbol PushEnvironment
   */
  public boolean isPushEnvironment() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Env
   *
   * @return the subterm corresponding to the slot Env
   */
  public sa.rule.types.TypeEnvironment getEnv() {
    throw new UnsupportedOperationException("This TypeEnvironment has no Env");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Env
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Env is replaced by _arg
   */
  public TypeEnvironment setEnv(sa.rule.types.TypeEnvironment _arg) {
    throw new UnsupportedOperationException("This TypeEnvironment has no Env");
  }

  /**
   * Returns the subterm corresponding to the slot Name
   *
   * @return the subterm corresponding to the slot Name
   */
  public String getName() {
    throw new UnsupportedOperationException("This TypeEnvironment has no Name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Name is replaced by _arg
   */
  public TypeEnvironment setName(String _arg) {
    throw new UnsupportedOperationException("This TypeEnvironment has no Name");
  }

  /**
   * Returns the subterm corresponding to the slot Type
   *
   * @return the subterm corresponding to the slot Type
   */
  public sa.rule.types.GomType getType() {
    throw new UnsupportedOperationException("This TypeEnvironment has no Type");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Type
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Type is replaced by _arg
   */
  public TypeEnvironment setType(sa.rule.types.GomType _arg) {
    throw new UnsupportedOperationException("This TypeEnvironment has no Type");
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
  public sa.rule.types.TypeEnvironment reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment> enumTypeEnvironment = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment> tmpenumTypeEnvironment = new tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.TypeEnvironment>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment> getEnumeration() {
    if(enumTypeEnvironment == null) {
      enumTypeEnvironment = sa.rule.types.typeenvironment.EmptyEnvironment.funMake().apply(sa.rule.types.TypeEnvironment.tmpenumTypeEnvironment)
        .plus(sa.rule.types.typeenvironment.PushEnvironment.funMake().apply(tom.library.enumerator.Combinators.makeString()).apply(sa.rule.types.GomType.tmpenumGomType).apply(sa.rule.types.TypeEnvironment.tmpenumTypeEnvironment));

      sa.rule.types.GomType.getEnumeration();

      tmpenumTypeEnvironment.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.TypeEnvironment>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.TypeEnvironment>> _1() { return enumTypeEnvironment.parts(); }
      };

    }
    return enumTypeEnvironment;
  }

}
