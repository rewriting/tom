
package enumerator.mutual.types;


public abstract class B extends enumerator.mutual.MutualAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected B() {}



  /**
   * Returns true if the term is rooted by the symbol b
   *
   * @return true if the term is rooted by the symbol b
   */
  public boolean isb() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol grr
   *
   * @return true if the term is rooted by the symbol grr
   */
  public boolean isgrr() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot a
   *
   * @return the subterm corresponding to the slot a
   */
  public enumerator.mutual.types.A geta() {
    throw new UnsupportedOperationException("This B has no a");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot a
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot a is replaced by _arg
   */
  public B seta(enumerator.mutual.types.A _arg) {
    throw new UnsupportedOperationException("This B has no a");
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
   * Returns a enumerator.mutual.types.B from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static enumerator.mutual.types.B fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a enumerator.mutual.types.B from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static enumerator.mutual.types.B fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a enumerator.mutual.types.B from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static enumerator.mutual.types.B fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a enumerator.mutual.types.B
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static enumerator.mutual.types.B fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    enumerator.mutual.types.B tmp;
    java.util.ArrayList<enumerator.mutual.types.B> results = new java.util.ArrayList<enumerator.mutual.types.B>();

    tmp = enumerator.mutual.types.b.b.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = enumerator.mutual.types.b.grr.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a B");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("B").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "enumerator.mutual.types.B", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a enumerator.mutual.types.B from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static enumerator.mutual.types.B fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a enumerator.mutual.types.B from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static enumerator.mutual.types.B fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public enumerator.mutual.types.B reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
}
