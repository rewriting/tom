
package examples.parser.rec.types;


public abstract class ExpList extends examples.parser.rec.RecAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected ExpList() {}



  /**
   * Returns true if the term is rooted by the symbol ConsExpList
   *
   * @return true if the term is rooted by the symbol ConsExpList
   */
  public boolean isConsExpList() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyExpList
   *
   * @return true if the term is rooted by the symbol EmptyExpList
   */
  public boolean isEmptyExpList() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot HeadExpList
   *
   * @return the subterm corresponding to the slot HeadExpList
   */
  public examples.parser.rec.types.Exp getHeadExpList() {
    throw new UnsupportedOperationException("This ExpList has no HeadExpList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadExpList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadExpList is replaced by _arg
   */
  public ExpList setHeadExpList(examples.parser.rec.types.Exp _arg) {
    throw new UnsupportedOperationException("This ExpList has no HeadExpList");
  }

  /**
   * Returns the subterm corresponding to the slot TailExpList
   *
   * @return the subterm corresponding to the slot TailExpList
   */
  public examples.parser.rec.types.ExpList getTailExpList() {
    throw new UnsupportedOperationException("This ExpList has no TailExpList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailExpList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailExpList is replaced by _arg
   */
  public ExpList setTailExpList(examples.parser.rec.types.ExpList _arg) {
    throw new UnsupportedOperationException("This ExpList has no TailExpList");
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
   * Returns a parser.rec.types.ExpList from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.parser.rec.types.ExpList fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a parser.rec.types.ExpList from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.parser.rec.types.ExpList fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a parser.rec.types.ExpList from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.parser.rec.types.ExpList fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a parser.rec.types.ExpList
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.parser.rec.types.ExpList fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.parser.rec.types.ExpList tmp;
    java.util.ArrayList<examples.parser.rec.types.ExpList> results = new java.util.ArrayList<examples.parser.rec.types.ExpList>();

    tmp = examples.parser.rec.types.explist.ConsExpList.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.explist.EmptyExpList.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.explist.ExpList.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a ExpList");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("ExpList").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "parser.rec.types.ExpList", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.ExpList from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.ExpList fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a parser.rec.types.ExpList from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.ExpList fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.parser.rec.types.ExpList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<examples.parser.rec.types.Exp> getCollectionExpList() {
    throw new UnsupportedOperationException("This ExpList cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList> enumExpList = null;
  public static final tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList> tmpenumExpList = new tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.ExpList>>) null);

  public static tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList> getEnumeration() {
    if(enumExpList == null) { 
      enumExpList = null;


      tmpenumExpList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.ExpList>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.ExpList>> _1() { return enumExpList.parts(); }
      };

    }
    return enumExpList;
  }

}
