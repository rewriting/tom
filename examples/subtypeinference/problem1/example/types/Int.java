
package problem1.example.types;


public abstract class Int extends problem1.example.ExampleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Int() {}



  /**
   * Returns true if the term is rooted by the symbol uminus
   *
   * @return true if the term is rooted by the symbol uminus
   */
  public boolean isuminus() {
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
   * Returns the subterm corresponding to the slot n1
   *
   * @return the subterm corresponding to the slot n1
   */
  public problem1.example.types.Int getn1() {
    throw new UnsupportedOperationException("This Int has no n1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot n1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot n1 is replaced by _arg
   */
  public Int setn1(problem1.example.types.Int _arg) {
    throw new UnsupportedOperationException("This Int has no n1");
  }

  /**
   * Returns the subterm corresponding to the slot n2
   *
   * @return the subterm corresponding to the slot n2
   */
  public problem1.example.types.Int getn2() {
    throw new UnsupportedOperationException("This Int has no n2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot n2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot n2 is replaced by _arg
   */
  public Int setn2(problem1.example.types.Int _arg) {
    throw new UnsupportedOperationException("This Int has no n2");
  }

  /**
   * Returns the subterm corresponding to the slot n
   *
   * @return the subterm corresponding to the slot n
   */
  public problem1.example.types.Nat getn() {
    throw new UnsupportedOperationException("This Int has no n");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot n
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot n is replaced by _arg
   */
  public Int setn(problem1.example.types.Nat _arg) {
    throw new UnsupportedOperationException("This Int has no n");
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
   * Returns a problem1.example.types.Int from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static problem1.example.types.Int fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a problem1.example.types.Int from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static problem1.example.types.Int fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a problem1.example.types.Int from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static problem1.example.types.Int fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a problem1.example.types.Int
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static problem1.example.types.Int fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    problem1.example.types.Int tmp;
    java.util.ArrayList<problem1.example.types.Int> results = new java.util.ArrayList<problem1.example.types.Int>();

    tmp = problem1.example.types.int.uminus.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = problem1.example.types.int.plus.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Int");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Int").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "problem1.example.types.Int", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a problem1.example.types.Int from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static problem1.example.types.Int fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a problem1.example.types.Int from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static problem1.example.types.Int fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public problem1.example.types.Int reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
}
