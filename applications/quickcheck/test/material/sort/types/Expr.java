
package sort.types;


public abstract class Expr extends sort.SortAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Expr() {}



  /**
   * Returns true if the term is rooted by the symbol zero
   *
   * @return true if the term is rooted by the symbol zero
   */
  public boolean iszero() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol un
   *
   * @return true if the term is rooted by the symbol un
   */
  public boolean isun() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol plus
   *
   * @return true if the term is rooted by the symbol plus
   */
  public boolean isplus() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol mult
   *
   * @return true if the term is rooted by the symbol mult
   */
  public boolean ismult() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot b
   *
   * @return the subterm corresponding to the slot b
   */
  public sort.types.Expr getb() {
    throw new UnsupportedOperationException("This Expr has no b");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot b
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot b is replaced by _arg
   */
  public Expr setb(sort.types.Expr _arg) {
    throw new UnsupportedOperationException("This Expr has no b");
  }

  /**
   * Returns the subterm corresponding to the slot a
   *
   * @return the subterm corresponding to the slot a
   */
  public sort.types.Expr geta() {
    throw new UnsupportedOperationException("This Expr has no a");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot a
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot a is replaced by _arg
   */
  public Expr seta(sort.types.Expr _arg) {
    throw new UnsupportedOperationException("This Expr has no a");
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
   * Returns a sort.types.Expr from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static sort.types.Expr fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a sort.types.Expr from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static sort.types.Expr fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a sort.types.Expr from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static sort.types.Expr fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a sort.types.Expr
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static sort.types.Expr fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    sort.types.Expr tmp;
    java.util.ArrayList<sort.types.Expr> results = new java.util.ArrayList<sort.types.Expr>();

    tmp = sort.types.expr.zero.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = sort.types.expr.un.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = sort.types.expr.plus.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = sort.types.expr.mult.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Expr");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Expr").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "sort.types.Expr", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a sort.types.Expr from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static sort.types.Expr fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a sort.types.Expr from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static sort.types.Expr fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public sort.types.Expr reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
}
