
package minimal.types;        


public abstract class Nop extends minimal.MinimalAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass 
   * constructors, typically implicit.)
   */
  protected Nop() {}



  /**
   * Returns true if the term is rooted by the symbol EmptyNop
   *
   * @return true if the term is rooted by the symbol EmptyNop
   */
  public boolean isEmptyNop() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol UnaryNop
   *
   * @return true if the term is rooted by the symbol UnaryNop
   */
  public boolean isUnaryNop() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol BinaryNop
   *
   * @return true if the term is rooted by the symbol BinaryNop
   */
  public boolean isBinaryNop() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol TertiaryNop
   *
   * @return true if the term is rooted by the symbol TertiaryNop
   */
  public boolean isTertiaryNop() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol ConsVary
   *
   * @return true if the term is rooted by the symbol ConsVary
   */
  public boolean isConsVary() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptyVary
   *
   * @return true if the term is rooted by the symbol EmptyVary
   */
  public boolean isEmptyVary() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot rs
   *
   * @return the subterm corresponding to the slot rs
   */
  public minimal.types.Nop getrs() {
    throw new UnsupportedOperationException("This Nop has no rs");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot rs
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot rs is replaced by _arg
   */
  public Nop setrs(minimal.types.Nop _arg) {
    throw new UnsupportedOperationException("This Nop has no rs");
  }

  /**
   * Returns the subterm corresponding to the slot slot
   *
   * @return the subterm corresponding to the slot slot
   */
  public minimal.types.Nop getslot() {
    throw new UnsupportedOperationException("This Nop has no slot");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot slot
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot slot is replaced by _arg
   */
  public Nop setslot(minimal.types.Nop _arg) {
    throw new UnsupportedOperationException("This Nop has no slot");
  }

  /**
   * Returns the subterm corresponding to the slot TailVary
   *
   * @return the subterm corresponding to the slot TailVary
   */
  public minimal.types.Nop getTailVary() {
    throw new UnsupportedOperationException("This Nop has no TailVary");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailVary
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailVary is replaced by _arg
   */
  public Nop setTailVary(minimal.types.Nop _arg) {
    throw new UnsupportedOperationException("This Nop has no TailVary");
  }

  /**
   * Returns the subterm corresponding to the slot cs
   *
   * @return the subterm corresponding to the slot cs
   */
  public minimal.types.Nop getcs() {
    throw new UnsupportedOperationException("This Nop has no cs");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot cs
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot cs is replaced by _arg
   */
  public Nop setcs(minimal.types.Nop _arg) {
    throw new UnsupportedOperationException("This Nop has no cs");
  }

  /**
   * Returns the subterm corresponding to the slot HeadVary
   *
   * @return the subterm corresponding to the slot HeadVary
   */
  public minimal.types.Nop getHeadVary() {
    throw new UnsupportedOperationException("This Nop has no HeadVary");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadVary
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadVary is replaced by _arg
   */
  public Nop setHeadVary(minimal.types.Nop _arg) {
    throw new UnsupportedOperationException("This Nop has no HeadVary");
  }

  /**
   * Returns the subterm corresponding to the slot ls
   *
   * @return the subterm corresponding to the slot ls
   */
  public minimal.types.Nop getls() {
    throw new UnsupportedOperationException("This Nop has no ls");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot ls
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot ls is replaced by _arg
   */
  public Nop setls(minimal.types.Nop _arg) {
    throw new UnsupportedOperationException("This Nop has no ls");
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
   * Returns a minimal.types.Nop from an ATerm without any conversion
   * 
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static minimal.types.Nop fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /** 
   * Returns a minimal.types.Nop from a String without any conversion
   * 
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static minimal.types.Nop fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /** 
   * Returns a minimal.types.Nop from a Stream without any conversion
   * 
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static minimal.types.Nop fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /** 
   * Apply a conversion on the ATerm and returns a minimal.types.Nop
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term 
   * @throws IllegalArgumentException
   */
  public static minimal.types.Nop fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    minimal.types.Nop tmp;
    java.util.ArrayList<minimal.types.Nop> results = new java.util.ArrayList<minimal.types.Nop>();

    tmp = minimal.types.nop.EmptyNop.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = minimal.types.nop.UnaryNop.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = minimal.types.nop.BinaryNop.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = minimal.types.nop.TertiaryNop.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = minimal.types.nop.ConsVary.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = minimal.types.nop.EmptyVary.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = minimal.types.nop.Vary.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Nop");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Nop").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "minimal.types.Nop", results.get(0).toString()});
        return results.get(0);
    }
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a minimal.types.Nop from it
   * 
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static minimal.types.Nop fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /** 
   * Apply a conversion on the ATerm contained in the Stream and returns a minimal.types.Nop from it
   * 
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static minimal.types.Nop fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public minimal.types.Nop reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
  /** 
   * Returns a Collection extracted from the term
   * 
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<minimal.types.Nop> getCollectionVary() {
    throw new UnsupportedOperationException("This Nop cannot be converted into a Collection");
  }
          
}
