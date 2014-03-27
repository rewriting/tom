
package examples.parser.rec.types;


public abstract class Pair extends examples.parser.rec.RecAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Pair() {}



  /**
   * Returns true if the term is rooted by the symbol Pair
   *
   * @return true if the term is rooted by the symbol Pair
   */
  public boolean isPair() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Value
   *
   * @return the subterm corresponding to the slot Value
   */
  public int getValue() {
    throw new UnsupportedOperationException("This Pair has no Value");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Value
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Value is replaced by _arg
   */
  public Pair setValue(int _arg) {
    throw new UnsupportedOperationException("This Pair has no Value");
  }

  /**
   * Returns the subterm corresponding to the slot Table
   *
   * @return the subterm corresponding to the slot Table
   */
  public examples.parser.rec.types.Table getTable() {
    throw new UnsupportedOperationException("This Pair has no Table");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Table
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Table is replaced by _arg
   */
  public Pair setTable(examples.parser.rec.types.Table _arg) {
    throw new UnsupportedOperationException("This Pair has no Table");
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
   * Returns a parser.rec.types.Pair from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.parser.rec.types.Pair fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a parser.rec.types.Pair from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.parser.rec.types.Pair fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a parser.rec.types.Pair from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.parser.rec.types.Pair fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a parser.rec.types.Pair
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.parser.rec.types.Pair fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.parser.rec.types.Pair tmp;
    java.util.ArrayList<examples.parser.rec.types.Pair> results = new java.util.ArrayList<examples.parser.rec.types.Pair>();

    tmp = examples.parser.rec.types.pair.Pair.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Pair");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Pair").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "parser.rec.types.Pair", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Pair from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Pair fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a parser.rec.types.Pair from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Pair fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.parser.rec.types.Pair reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.parser.rec.types.Pair> enumPair = null;
  public static final tom.library.enumerator.Enumeration<examples.parser.rec.types.Pair> tmpenumPair = new tom.library.enumerator.Enumeration<examples.parser.rec.types.Pair>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Pair>>) null);

  public static tom.library.enumerator.Enumeration<examples.parser.rec.types.Pair> getEnumeration() {
    if(enumPair == null) { 
      enumPair = examples.parser.rec.types.pair.Pair.funMake().apply(tom.library.enumerator.Combinators.makeint()).apply(examples.parser.rec.types.Table.tmpenumTable);

      examples.parser.rec.types.Table.getEnumeration();

      tmpenumPair.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Pair>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Pair>> _1() { return enumPair.parts(); }
      };

    }
    return enumPair;
  }

}
