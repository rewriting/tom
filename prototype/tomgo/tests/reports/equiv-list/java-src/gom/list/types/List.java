
package gom.list.types;        


public abstract class List extends gom.list.ListAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass 
   * constructors, typically implicit.)
   */
  protected List() {}



  /**
   * Returns true if the term is rooted by the symbol Consconc
   *
   * @return true if the term is rooted by the symbol Consconc
   */
  public boolean isConsconc() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Emptyconc
   *
   * @return true if the term is rooted by the symbol Emptyconc
   */
  public boolean isEmptyconc() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Tailconc
   *
   * @return the subterm corresponding to the slot Tailconc
   */
  public gom.list.types.List getTailconc() {
    throw new UnsupportedOperationException("This List has no Tailconc");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Tailconc
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Tailconc is replaced by _arg
   */
  public List setTailconc(gom.list.types.List _arg) {
    throw new UnsupportedOperationException("This List has no Tailconc");
  }

  /**
   * Returns the subterm corresponding to the slot Headconc
   *
   * @return the subterm corresponding to the slot Headconc
   */
  public int getHeadconc() {
    throw new UnsupportedOperationException("This List has no Headconc");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Headconc
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Headconc is replaced by _arg
   */
  public List setHeadconc(int _arg) {
    throw new UnsupportedOperationException("This List has no Headconc");
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
   * Returns a gom.list.types.List from an ATerm without any conversion
   * 
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static gom.list.types.List fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /** 
   * Returns a gom.list.types.List from a String without any conversion
   * 
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static gom.list.types.List fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /** 
   * Returns a gom.list.types.List from a Stream without any conversion
   * 
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static gom.list.types.List fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /** 
   * Apply a conversion on the ATerm and returns a gom.list.types.List
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term 
   * @throws IllegalArgumentException
   */
  public static gom.list.types.List fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    gom.list.types.List tmp;
    java.util.ArrayList<gom.list.types.List> results = new java.util.ArrayList<gom.list.types.List>();

    tmp = gom.list.types.list.Consconc.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = gom.list.types.list.Emptyconc.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = gom.list.types.list.conc.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a List");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("List").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "gom.list.types.List", results.get(0).toString()});
        return results.get(0);
    }
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a gom.list.types.List from it
   * 
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static gom.list.types.List fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /** 
   * Apply a conversion on the ATerm contained in the Stream and returns a gom.list.types.List from it
   * 
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static gom.list.types.List fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public gom.list.types.List reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
  /** 
   * Returns a Collection extracted from the term
   * 
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<java.lang.Integer> getCollectionconc() {
    throw new UnsupportedOperationException("This List cannot be converted into a Collection");
  }
          
}
