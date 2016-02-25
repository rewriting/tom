
package sa.rule.types;


public abstract class Field extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Field() {}



  /**
   * Returns true if the term is rooted by the symbol UnamedField
   *
   * @return true if the term is rooted by the symbol UnamedField
   */
  public boolean isUnamedField() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot FieldType
   *
   * @return the subterm corresponding to the slot FieldType
   */
  public sa.rule.types.GomType getFieldType() {
    throw new UnsupportedOperationException("This Field has no FieldType");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot FieldType
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot FieldType is replaced by _arg
   */
  public Field setFieldType(sa.rule.types.GomType _arg) {
    throw new UnsupportedOperationException("This Field has no FieldType");
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
  public sa.rule.types.Field reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Field> enumField = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Field> tmpenumField = new tom.library.enumerator.Enumeration<sa.rule.types.Field>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Field>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Field> getEnumeration() {
    if(enumField == null) {
      enumField = sa.rule.types.field.UnamedField.funMake().apply(sa.rule.types.GomType.tmpenumGomType);

      sa.rule.types.GomType.getEnumeration();

      tmpenumField.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Field>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Field>> _1() { return enumField.parts(); }
      };

    }
    return enumField;
  }

}
