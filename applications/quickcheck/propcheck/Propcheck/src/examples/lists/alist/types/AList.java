
package examples.lists.alist.types;


public abstract class AList extends examples.lists.alist.AListAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected AList() {}



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
  public examples.lists.alist.types.Elem gethead() {
    throw new UnsupportedOperationException("This AList has no head");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot head
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot head is replaced by _arg
   */
  public AList sethead(examples.lists.alist.types.Elem _arg) {
    throw new UnsupportedOperationException("This AList has no head");
  }

  /**
   * Returns the subterm corresponding to the slot tail
   *
   * @return the subterm corresponding to the slot tail
   */
  public examples.lists.alist.types.AList gettail() {
    throw new UnsupportedOperationException("This AList has no tail");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot tail
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot tail is replaced by _arg
   */
  public AList settail(examples.lists.alist.types.AList _arg) {
    throw new UnsupportedOperationException("This AList has no tail");
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
   * Returns a examples.lists.alist.types.AList from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.lists.alist.types.AList fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.lists.alist.types.AList from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.lists.alist.types.AList fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.lists.alist.types.AList from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.lists.alist.types.AList fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.lists.alist.types.AList
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.lists.alist.types.AList fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.lists.alist.types.AList tmp;
    java.util.ArrayList<examples.lists.alist.types.AList> results = new java.util.ArrayList<examples.lists.alist.types.AList>();

    tmp = examples.lists.alist.types.alist.empty.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.lists.alist.types.alist.con.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a AList");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("AList").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.lists.alist.types.AList", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.lists.alist.types.AList from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.lists.alist.types.AList fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.lists.alist.types.AList from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.lists.alist.types.AList fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.lists.alist.types.AList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.lists.alist.types.AList> enumAList = null;
  public static final tom.library.enumerator.Enumeration<examples.lists.alist.types.AList> tmpenumAList = new tom.library.enumerator.Enumeration<examples.lists.alist.types.AList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.lists.alist.types.AList>>) null);

  public static tom.library.enumerator.Enumeration<examples.lists.alist.types.AList> getEnumeration() {
    if(enumAList == null) { 
      enumAList = examples.lists.alist.types.alist.empty.funMake().apply(examples.lists.alist.types.AList.tmpenumAList)
        .plus(examples.lists.alist.types.alist.con.funMake().apply(examples.lists.alist.types.Elem.tmpenumElem).apply(examples.lists.alist.types.AList.tmpenumAList));

      examples.lists.alist.types.Elem.getEnumeration();

      tmpenumAList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.lists.alist.types.AList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.lists.alist.types.AList>> _1() { return enumAList.parts(); }
      };

    }
    return enumAList;
  }

}
