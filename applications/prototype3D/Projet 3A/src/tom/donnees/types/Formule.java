
package tom.donnees.types;        


public abstract class Formule extends tom.donnees.DonneesAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass 
   * constructors, typically implicit.)
   */
  protected Formule() {}



  /**
   * Returns true if the term is rooted by the symbol Input
   *
   * @return true if the term is rooted by the symbol Input
   */
  public boolean isInput() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol False
   *
   * @return true if the term is rooted by the symbol False
   */
  public boolean isFalse() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol True
   *
   * @return true if the term is rooted by the symbol True
   */
  public boolean isTrue() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol And
   *
   * @return true if the term is rooted by the symbol And
   */
  public boolean isAnd() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Or
   *
   * @return true if the term is rooted by the symbol Or
   */
  public boolean isOr() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot A
   *
   * @return the subterm corresponding to the slot A
   */
  public String getA() {
    throw new UnsupportedOperationException("This Formule has no A");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot A
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot A is replaced by _arg
   */
  public Formule setA(String _arg) {
    throw new UnsupportedOperationException("This Formule has no A");
  }

  /**
   * Returns the subterm corresponding to the slot f1
   *
   * @return the subterm corresponding to the slot f1
   */
  public tom.donnees.types.Formule getf1() {
    throw new UnsupportedOperationException("This Formule has no f1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot f1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot f1 is replaced by _arg
   */
  public Formule setf1(tom.donnees.types.Formule _arg) {
    throw new UnsupportedOperationException("This Formule has no f1");
  }

  /**
   * Returns the subterm corresponding to the slot f2
   *
   * @return the subterm corresponding to the slot f2
   */
  public tom.donnees.types.Formule getf2() {
    throw new UnsupportedOperationException("This Formule has no f2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot f2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot f2 is replaced by _arg
   */
  public Formule setf2(tom.donnees.types.Formule _arg) {
    throw new UnsupportedOperationException("This Formule has no f2");
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
   * Returns a tom.donnees.types.Formule from an ATerm without any conversion
   * 
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static tom.donnees.types.Formule fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /** 
   * Returns a tom.donnees.types.Formule from a String without any conversion
   * 
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static tom.donnees.types.Formule fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /** 
   * Returns a tom.donnees.types.Formule from a Stream without any conversion
   * 
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static tom.donnees.types.Formule fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /** 
   * Apply a conversion on the ATerm and returns a tom.donnees.types.Formule
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term 
   * @throws IllegalArgumentException
   */
  public static tom.donnees.types.Formule fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    tom.donnees.types.Formule tmp;
    java.util.ArrayList<tom.donnees.types.Formule> results = new java.util.ArrayList<tom.donnees.types.Formule>();

    tmp = tom.donnees.types.formule.Input.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = tom.donnees.types.formule.False.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = tom.donnees.types.formule.True.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = tom.donnees.types.formule.And.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = tom.donnees.types.formule.Or.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Formule");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Formule").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "tom.donnees.types.Formule", results.get(0).toString()});
        return results.get(0);
    }
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a tom.donnees.types.Formule from it
   * 
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static tom.donnees.types.Formule fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /** 
   * Apply a conversion on the ATerm contained in the Stream and returns a tom.donnees.types.Formule from it
   * 
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static tom.donnees.types.Formule fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public tom.donnees.types.Formule reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
}
