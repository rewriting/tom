
package sort.types;


public abstract class Surexpr extends sort.SortAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Surexpr() {}



  /**
   * Returns true if the term is rooted by the symbol rien
   *
   * @return true if the term is rooted by the symbol rien
   */
  public boolean isrien() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol wrapper
   *
   * @return true if the term is rooted by the symbol wrapper
   */
  public boolean iswrapper() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol rec
   *
   * @return true if the term is rooted by the symbol rec
   */
  public boolean isrec() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot s
   *
   * @return the subterm corresponding to the slot s
   */
  public sort.types.Surexpr gets() {
    throw new UnsupportedOperationException("This Surexpr has no s");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot s
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot s is replaced by _arg
   */
  public Surexpr sets(sort.types.Surexpr _arg) {
    throw new UnsupportedOperationException("This Surexpr has no s");
  }

  /**
   * Returns the subterm corresponding to the slot e
   *
   * @return the subterm corresponding to the slot e
   */
  public sort.types.Expr gete() {
    throw new UnsupportedOperationException("This Surexpr has no e");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot e
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot e is replaced by _arg
   */
  public Surexpr sete(sort.types.Expr _arg) {
    throw new UnsupportedOperationException("This Surexpr has no e");
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
   * Returns a sort.types.Surexpr from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static sort.types.Surexpr fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a sort.types.Surexpr from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static sort.types.Surexpr fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a sort.types.Surexpr from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static sort.types.Surexpr fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a sort.types.Surexpr
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static sort.types.Surexpr fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    sort.types.Surexpr tmp;
    java.util.ArrayList<sort.types.Surexpr> results = new java.util.ArrayList<sort.types.Surexpr>();

    tmp = sort.types.surexpr.rien.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = sort.types.surexpr.wrapper.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = sort.types.surexpr.rec.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Surexpr");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Surexpr").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "sort.types.Surexpr", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a sort.types.Surexpr from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static sort.types.Surexpr fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a sort.types.Surexpr from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static sort.types.Surexpr fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public sort.types.Surexpr reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
}
