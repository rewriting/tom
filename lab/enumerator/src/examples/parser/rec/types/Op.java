
package examples.parser.rec.types;


public abstract class Op extends examples.parser.rec.RecAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Op() {}



  /**
   * Returns true if the term is rooted by the symbol Plus
   *
   * @return true if the term is rooted by the symbol Plus
   */
  public boolean isPlus() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Minus
   *
   * @return true if the term is rooted by the symbol Minus
   */
  public boolean isMinus() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Times
   *
   * @return true if the term is rooted by the symbol Times
   */
  public boolean isTimes() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Div
   *
   * @return true if the term is rooted by the symbol Div
   */
  public boolean isDiv() {
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
   * Returns true if the term is rooted by the symbol Equal
   *
   * @return true if the term is rooted by the symbol Equal
   */
  public boolean isEqual() {
    return false;
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
   * Returns a parser.rec.types.Op from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.parser.rec.types.Op fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a parser.rec.types.Op from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.parser.rec.types.Op fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a parser.rec.types.Op from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.parser.rec.types.Op fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a parser.rec.types.Op
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.parser.rec.types.Op fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.parser.rec.types.Op tmp;
    java.util.ArrayList<examples.parser.rec.types.Op> results = new java.util.ArrayList<examples.parser.rec.types.Op>();

    tmp = examples.parser.rec.types.op.Plus.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.op.Minus.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.op.Times.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.op.Div.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.op.And.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.op.Or.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.op.Equal.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Op");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Op").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "parser.rec.types.Op", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Op from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Op fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a parser.rec.types.Op from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Op fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.parser.rec.types.Op reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.parser.rec.types.Op> enumOp = null;
  public static final tom.library.enumerator.Enumeration<examples.parser.rec.types.Op> tmpenumOp = new tom.library.enumerator.Enumeration<examples.parser.rec.types.Op>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Op>>) null);

  public static tom.library.enumerator.Enumeration<examples.parser.rec.types.Op> getEnumeration() {
    if(enumOp == null) { 
      enumOp = examples.parser.rec.types.op.Plus.funMake().apply(examples.parser.rec.types.Op.tmpenumOp)
        .plus(examples.parser.rec.types.op.Minus.funMake().apply(examples.parser.rec.types.Op.tmpenumOp))
        .plus(examples.parser.rec.types.op.Times.funMake().apply(examples.parser.rec.types.Op.tmpenumOp))
        .plus(examples.parser.rec.types.op.Div.funMake().apply(examples.parser.rec.types.Op.tmpenumOp))
        .plus(examples.parser.rec.types.op.And.funMake().apply(examples.parser.rec.types.Op.tmpenumOp))
        .plus(examples.parser.rec.types.op.Or.funMake().apply(examples.parser.rec.types.Op.tmpenumOp))
        .plus(examples.parser.rec.types.op.Equal.funMake().apply(examples.parser.rec.types.Op.tmpenumOp));


      tmpenumOp.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Op>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Op>> _1() { return enumOp.parts(); }
      };

    }
    return enumOp;
  }

}
