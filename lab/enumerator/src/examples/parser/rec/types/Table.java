
package examples.parser.rec.types;


public abstract class Table extends examples.parser.rec.RecAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Table() {}



  /**
   * Returns true if the term is rooted by the symbol Table
   *
   * @return true if the term is rooted by the symbol Table
   */
  public boolean isTable() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyTable
   *
   * @return true if the term is rooted by the symbol EmptyTable
   */
  public boolean isEmptyTable() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Value
   *
   * @return the subterm corresponding to the slot Value
   */
  public int getValue() {
    throw new UnsupportedOperationException("This Table has no Value");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Value
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Value is replaced by _arg
   */
  public Table setValue(int _arg) {
    throw new UnsupportedOperationException("This Table has no Value");
  }

  /**
   * Returns the subterm corresponding to the slot Name
   *
   * @return the subterm corresponding to the slot Name
   */
  public String getName() {
    throw new UnsupportedOperationException("This Table has no Name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Name is replaced by _arg
   */
  public Table setName(String _arg) {
    throw new UnsupportedOperationException("This Table has no Name");
  }

  /**
   * Returns the subterm corresponding to the slot Tail
   *
   * @return the subterm corresponding to the slot Tail
   */
  public examples.parser.rec.types.Table getTail() {
    throw new UnsupportedOperationException("This Table has no Tail");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Tail
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Tail is replaced by _arg
   */
  public Table setTail(examples.parser.rec.types.Table _arg) {
    throw new UnsupportedOperationException("This Table has no Tail");
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
   * Returns a parser.rec.types.Table from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.parser.rec.types.Table fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a parser.rec.types.Table from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.parser.rec.types.Table fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a parser.rec.types.Table from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.parser.rec.types.Table fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a parser.rec.types.Table
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.parser.rec.types.Table fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.parser.rec.types.Table tmp;
    java.util.ArrayList<examples.parser.rec.types.Table> results = new java.util.ArrayList<examples.parser.rec.types.Table>();

    tmp = examples.parser.rec.types.table.Table.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.table.EmptyTable.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Table");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Table").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "parser.rec.types.Table", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Table from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Table fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a parser.rec.types.Table from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Table fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.parser.rec.types.Table reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.parser.rec.types.Table> enumTable = null;
  public static final tom.library.enumerator.Enumeration<examples.parser.rec.types.Table> tmpenumTable = new tom.library.enumerator.Enumeration<examples.parser.rec.types.Table>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Table>>) null);

  public static tom.library.enumerator.Enumeration<examples.parser.rec.types.Table> getEnumeration() {
    if(enumTable == null) { 
      enumTable = examples.parser.rec.types.table.Table.funMake().apply(tom.library.enumerator.Combinators.makeString()).apply(tom.library.enumerator.Combinators.makeint()).apply(examples.parser.rec.types.Table.tmpenumTable)
        .plus(examples.parser.rec.types.table.EmptyTable.funMake().apply(examples.parser.rec.types.Table.tmpenumTable));


      tmpenumTable.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Table>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Table>> _1() { return enumTable.parts(); }
      };

    }
    return enumTable;
  }

}
