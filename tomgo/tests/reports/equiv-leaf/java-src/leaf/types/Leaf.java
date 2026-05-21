
package leaf.types;        


public abstract class Leaf extends leaf.LeafAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass 
   * constructors, typically implicit.)
   */
  protected Leaf() {}



  /**
   * Returns true if the term is rooted by the symbol Leaf
   *
   * @return true if the term is rooted by the symbol Leaf
   */
  public boolean isLeaf() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Label
   *
   * @return true if the term is rooted by the symbol Label
   */
  public boolean isLabel() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot s
   *
   * @return the subterm corresponding to the slot s
   */
  public String gets() {
    throw new UnsupportedOperationException("This Leaf has no s");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot s
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot s is replaced by _arg
   */
  public Leaf sets(String _arg) {
    throw new UnsupportedOperationException("This Leaf has no s");
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
   * Returns a leaf.types.Leaf from an ATerm without any conversion
   * 
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static leaf.types.Leaf fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /** 
   * Returns a leaf.types.Leaf from a String without any conversion
   * 
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static leaf.types.Leaf fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /** 
   * Returns a leaf.types.Leaf from a Stream without any conversion
   * 
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static leaf.types.Leaf fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /** 
   * Apply a conversion on the ATerm and returns a leaf.types.Leaf
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term 
   * @throws IllegalArgumentException
   */
  public static leaf.types.Leaf fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    leaf.types.Leaf tmp;
    java.util.ArrayList<leaf.types.Leaf> results = new java.util.ArrayList<leaf.types.Leaf>();

    tmp = leaf.types.leaf.Leaf.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = leaf.types.leaf.Label.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Leaf");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Leaf").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "leaf.types.Leaf", results.get(0).toString()});
        return results.get(0);
    }
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a leaf.types.Leaf from it
   * 
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static leaf.types.Leaf fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /** 
   * Apply a conversion on the ATerm contained in the Stream and returns a leaf.types.Leaf from it
   * 
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static leaf.types.Leaf fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public leaf.types.Leaf reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
  
}
