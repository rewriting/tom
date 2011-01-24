
package gates.logic.types;


public abstract class Bool extends gates.logic.LogicAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Bool() {}



  /**
   * Returns true if the term is rooted by the symbol Input
   *
   * @return true if the term is rooted by the symbol Input
   */
  public boolean isInput() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol True
   *
   * @return true if the term is rooted by the symbol True
   */
  public boolean isTrue() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol False
   *
   * @return true if the term is rooted by the symbol False
   */
  public boolean isFalse() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Not
   *
   * @return true if the term is rooted by the symbol Not
   */
  public boolean isNot() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Or
   *
   * @return true if the term is rooted by the symbol Or
   */
  public boolean isOr() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol And
   *
   * @return true if the term is rooted by the symbol And
   */
  public boolean isAnd() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Nand
   *
   * @return true if the term is rooted by the symbol Nand
   */
  public boolean isNand() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Xor
   *
   * @return true if the term is rooted by the symbol Xor
   */
  public boolean isXor() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot b
   *
   * @return the subterm corresponding to the slot b
   */
  public gates.logic.types.Bool getb() {
    throw new UnsupportedOperationException("This Bool has no b");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot b
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot b is replaced by _arg
   */
  public Bool setb(gates.logic.types.Bool _arg) {
    throw new UnsupportedOperationException("This Bool has no b");
  }

  /**
   * Returns the subterm corresponding to the slot b1
   *
   * @return the subterm corresponding to the slot b1
   */
  public gates.logic.types.Bool getb1() {
    throw new UnsupportedOperationException("This Bool has no b1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot b1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot b1 is replaced by _arg
   */
  public Bool setb1(gates.logic.types.Bool _arg) {
    throw new UnsupportedOperationException("This Bool has no b1");
  }

  /**
   * Returns the subterm corresponding to the slot n
   *
   * @return the subterm corresponding to the slot n
   */
  public int getn() {
    throw new UnsupportedOperationException("This Bool has no n");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot n
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot n is replaced by _arg
   */
  public Bool setn(int _arg) {
    throw new UnsupportedOperationException("This Bool has no n");
  }

  /**
   * Returns the subterm corresponding to the slot b2
   *
   * @return the subterm corresponding to the slot b2
   */
  public gates.logic.types.Bool getb2() {
    throw new UnsupportedOperationException("This Bool has no b2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot b2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot b2 is replaced by _arg
   */
  public Bool setb2(gates.logic.types.Bool _arg) {
    throw new UnsupportedOperationException("This Bool has no b2");
  }

  protected static tom.library.utils.IdConverter idConv = new tom.library.utils.IdConverter();

  /**
   * Returns an ATerm representation of this term.
   *
   * @return null to indicate to sub-classes that they have to work
   */
  public aterm.ATerm toATerm() {
    // returns null to indicate sub-classes that they have to work
    return null;
  }

  /**
   * Returns a gates.logic.types.Bool from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static gates.logic.types.Bool fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a gates.logic.types.Bool from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static gates.logic.types.Bool fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a gates.logic.types.Bool from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static gates.logic.types.Bool fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a gates.logic.types.Bool
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static gates.logic.types.Bool fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    gates.logic.types.Bool tmp;
    java.util.ArrayList<gates.logic.types.Bool> results = new java.util.ArrayList<gates.logic.types.Bool>();

    tmp = gates.logic.types.bool.Input.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = gates.logic.types.bool.True.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = gates.logic.types.bool.False.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = gates.logic.types.bool.Not.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = gates.logic.types.bool.Or.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = gates.logic.types.bool.And.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = gates.logic.types.bool.Nand.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = gates.logic.types.bool.Xor.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Bool");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Bool").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "gates.logic.types.Bool", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a gates.logic.types.Bool from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static gates.logic.types.Bool fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a gates.logic.types.Bool from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static gates.logic.types.Bool fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),atConv);
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
  public gates.logic.types.Bool reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
}
