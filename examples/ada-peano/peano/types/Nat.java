
package peano.types;


public abstract class Nat extends peano.PeanoAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Nat() {}



  /**
   * Returns true if the term is rooted by the symbol zero
   *
   * @return true if the term is rooted by the symbol zero
   */
  public boolean iszero() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol suc
   *
   * @return true if the term is rooted by the symbol suc
   */
  public boolean issuc() {
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
   * Returns the subterm corresponding to the slot pred
   *
   * @return the subterm corresponding to the slot pred
   */
  public peano.types.Nat getpred() {
    throw new UnsupportedOperationException("This Nat has no pred");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot pred
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot pred is replaced by _arg
   */
  public Nat setpred(peano.types.Nat _arg) {
    throw new UnsupportedOperationException("This Nat has no pred");
  }

  /**
   * Returns the subterm corresponding to the slot x1
   *
   * @return the subterm corresponding to the slot x1
   */
  public peano.types.Nat getx1() {
    throw new UnsupportedOperationException("This Nat has no x1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot x1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot x1 is replaced by _arg
   */
  public Nat setx1(peano.types.Nat _arg) {
    throw new UnsupportedOperationException("This Nat has no x1");
  }

  /**
   * Returns the subterm corresponding to the slot x2
   *
   * @return the subterm corresponding to the slot x2
   */
  public peano.types.Nat getx2() {
    throw new UnsupportedOperationException("This Nat has no x2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot x2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot x2 is replaced by _arg
   */
  public Nat setx2(peano.types.Nat _arg) {
    throw new UnsupportedOperationException("This Nat has no x2");
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
   * Returns a peano.types.Nat from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static peano.types.Nat fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a peano.types.Nat from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static peano.types.Nat fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a peano.types.Nat from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static peano.types.Nat fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a peano.types.Nat
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static peano.types.Nat fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    peano.types.Nat tmp;
    java.util.ArrayList<peano.types.Nat> results = new java.util.ArrayList<peano.types.Nat>();

    tmp = peano.types.nat.zero.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = peano.types.nat.suc.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = peano.types.nat.plus.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Nat");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Nat").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "peano.types.Nat", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a peano.types.Nat from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static peano.types.Nat fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a peano.types.Nat from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static peano.types.Nat fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public peano.types.Nat reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
}
