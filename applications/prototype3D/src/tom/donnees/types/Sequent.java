
package tom.donnees.types;        


public abstract class Sequent extends tom.donnees.DonneesAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass 
   * constructors, typically implicit.)
   */
  protected Sequent() {}



  /**
   * Returns true if the term is rooted by the symbol ConsList
   *
   * @return true if the term is rooted by the symbol ConsList
   */
  public boolean isConsList() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyList
   *
   * @return true if the term is rooted by the symbol EmptyList
   */
  public boolean isEmptyList() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot HeadList
   *
   * @return the subterm corresponding to the slot HeadList
   */
  public tom.donnees.types.Formule getHeadList() {
    throw new UnsupportedOperationException("This Sequent has no HeadList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadList is replaced by _arg
   */
  public Sequent setHeadList(tom.donnees.types.Formule _arg) {
    throw new UnsupportedOperationException("This Sequent has no HeadList");
  }

  /**
   * Returns the subterm corresponding to the slot TailList
   *
   * @return the subterm corresponding to the slot TailList
   */
  public tom.donnees.types.Sequent getTailList() {
    throw new UnsupportedOperationException("This Sequent has no TailList");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailList
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailList is replaced by _arg
   */
  public Sequent setTailList(tom.donnees.types.Sequent _arg) {
    throw new UnsupportedOperationException("This Sequent has no TailList");
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
   * Returns a tom.donnees.types.Sequent from an ATerm without any conversion
   * 
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static tom.donnees.types.Sequent fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /** 
   * Returns a tom.donnees.types.Sequent from a String without any conversion
   * 
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static tom.donnees.types.Sequent fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /** 
   * Returns a tom.donnees.types.Sequent from a Stream without any conversion
   * 
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static tom.donnees.types.Sequent fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /** 
   * Apply a conversion on the ATerm and returns a tom.donnees.types.Sequent
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term 
   * @throws IllegalArgumentException
   */
  public static tom.donnees.types.Sequent fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    tom.donnees.types.Sequent tmp;
    java.util.ArrayList<tom.donnees.types.Sequent> results = new java.util.ArrayList<tom.donnees.types.Sequent>();

    tmp = tom.donnees.types.sequent.ConsList.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = tom.donnees.types.sequent.EmptyList.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = tom.donnees.types.sequent.List.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Sequent");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Sequent").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "tom.donnees.types.Sequent", results.get(0).toString()});
        return results.get(0);
    }
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a tom.donnees.types.Sequent from it
   * 
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static tom.donnees.types.Sequent fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /** 
   * Apply a conversion on the ATerm contained in the Stream and returns a tom.donnees.types.Sequent from it
   * 
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static tom.donnees.types.Sequent fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public tom.donnees.types.Sequent reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
  /** 
   * Returns a Collection extracted from the term
   * 
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<tom.donnees.types.Formule> getCollectionList() {
    throw new UnsupportedOperationException("This Sequent cannot be converted into a Collection");
  }
          
}
