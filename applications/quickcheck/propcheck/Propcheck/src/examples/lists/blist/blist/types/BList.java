
package examples.lists.blist.blist.types;


public abstract class BList extends examples.lists.blist.blist.BListAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected BList() {}



  /**
   * Returns true if the term is rooted by the symbol empty
   *
   * @return true if the term is rooted by the symbol empty
   */
  public boolean isempty() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol con
   *
   * @return true if the term is rooted by the symbol con
   */
  public boolean iscon() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot head
   *
   * @return the subterm corresponding to the slot head
   */
  public examples.lists.blist.blist.types.Elem gethead() {
    throw new UnsupportedOperationException("This BList has no head");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot head
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot head is replaced by _arg
   */
  public BList sethead(examples.lists.blist.blist.types.Elem _arg) {
    throw new UnsupportedOperationException("This BList has no head");
  }

  /**
   * Returns the subterm corresponding to the slot tail
   *
   * @return the subterm corresponding to the slot tail
   */
  public examples.lists.blist.blist.types.BList gettail() {
    throw new UnsupportedOperationException("This BList has no tail");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot tail
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot tail is replaced by _arg
   */
  public BList settail(examples.lists.blist.blist.types.BList _arg) {
    throw new UnsupportedOperationException("This BList has no tail");
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
   * Returns a examples.lists.blist.blist.types.BList from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.lists.blist.blist.types.BList fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.lists.blist.blist.types.BList from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.lists.blist.blist.types.BList fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.lists.blist.blist.types.BList from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.lists.blist.blist.types.BList fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.lists.blist.blist.types.BList
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.lists.blist.blist.types.BList fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.lists.blist.blist.types.BList tmp;
    java.util.ArrayList<examples.lists.blist.blist.types.BList> results = new java.util.ArrayList<examples.lists.blist.blist.types.BList>();

    tmp = examples.lists.blist.blist.types.blist.empty.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.lists.blist.blist.types.blist.con.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a BList");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("BList").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.lists.blist.blist.types.BList", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.lists.blist.blist.types.BList from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.lists.blist.blist.types.BList fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.lists.blist.blist.types.BList from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.lists.blist.blist.types.BList fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.lists.blist.blist.types.BList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList> enumBList = null;
  public static final tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList> tmpenumBList = new tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.lists.blist.blist.types.BList>>) null);

  public static tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList> getEnumeration() {
    if(enumBList == null) { 
      enumBList = examples.lists.blist.blist.types.blist.empty.funMake().apply(examples.lists.blist.blist.types.BList.tmpenumBList)
        .plus(examples.lists.blist.blist.types.blist.con.funMake().apply(examples.lists.blist.blist.types.Elem.tmpenumElem).apply(examples.lists.blist.blist.types.BList.tmpenumBList));

      examples.lists.blist.blist.types.Elem.getEnumeration();

      tmpenumBList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.lists.blist.blist.types.BList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.lists.blist.blist.types.BList>> _1() { return enumBList.parts(); }
      };

    }
    return enumBList;
  }

}
