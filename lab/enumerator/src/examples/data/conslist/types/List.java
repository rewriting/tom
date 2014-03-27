
package examples.data.conslist.types;


public abstract class List extends examples.data.conslist.ConsListAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected List() {}



  /**
   * Returns true if the term is rooted by the symbol nil
   *
   * @return true if the term is rooted by the symbol nil
   */
  public boolean isnil() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol cons
   *
   * @return true if the term is rooted by the symbol cons
   */
  public boolean iscons() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot l
   *
   * @return the subterm corresponding to the slot l
   */
  public examples.data.conslist.types.List getl() {
    throw new UnsupportedOperationException("This List has no l");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot l
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot l is replaced by _arg
   */
  public List setl(examples.data.conslist.types.List _arg) {
    throw new UnsupportedOperationException("This List has no l");
  }

  /**
   * Returns the subterm corresponding to the slot e
   *
   * @return the subterm corresponding to the slot e
   */
  public examples.data.conslist.types.Elem gete() {
    throw new UnsupportedOperationException("This List has no e");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot e
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot e is replaced by _arg
   */
  public List sete(examples.data.conslist.types.Elem _arg) {
    throw new UnsupportedOperationException("This List has no e");
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
   * Returns a examples.conslist.types.List from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.data.conslist.types.List fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.conslist.types.List from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.data.conslist.types.List fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.conslist.types.List from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.data.conslist.types.List fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.conslist.types.List
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.data.conslist.types.List fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.data.conslist.types.List tmp;
    java.util.ArrayList<examples.data.conslist.types.List> results = new java.util.ArrayList<examples.data.conslist.types.List>();

    tmp = examples.data.conslist.types.list.nil.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.data.conslist.types.list.cons.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a List");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("List").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.conslist.types.List", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.conslist.types.List from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.conslist.types.List fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.conslist.types.List from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.conslist.types.List fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.data.conslist.types.List reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.data.conslist.types.List> enumList = null;
  public static final tom.library.enumerator.Enumeration<examples.data.conslist.types.List> tmpenumList = new tom.library.enumerator.Enumeration<examples.data.conslist.types.List>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.conslist.types.List>>) null);

  public static tom.library.enumerator.Enumeration<examples.data.conslist.types.List> getEnumeration() {
    if(enumList == null) { 
      enumList = examples.data.conslist.types.list.nil.funMake().apply(examples.data.conslist.types.List.tmpenumList)
        .plus(examples.data.conslist.types.list.cons.funMake().apply(examples.data.conslist.types.Elem.tmpenumElem).apply(examples.data.conslist.types.List.tmpenumList));

      examples.data.conslist.types.Elem.getEnumeration();

      tmpenumList.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.conslist.types.List>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.conslist.types.List>> _1() { return enumList.parts(); }
      };

    }
    return enumList;
  }

}
