
package examples.data.types.types;


public abstract class B extends examples.data.types.TypesAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected B() {}



  /**
   * Returns true if the term is rooted by the symbol b1
   *
   * @return true if the term is rooted by the symbol b1
   */
  public boolean isb1() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol b2
   *
   * @return true if the term is rooted by the symbol b2
   */
  public boolean isb2() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol b3
   *
   * @return true if the term is rooted by the symbol b3
   */
  public boolean isb3() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol goo
   *
   * @return true if the term is rooted by the symbol goo
   */
  public boolean isgoo() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot a
   *
   * @return the subterm corresponding to the slot a
   */
  public examples.data.types.types.A geta() {
    throw new UnsupportedOperationException("This B has no a");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot a
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot a is replaced by _arg
   */
  public B seta(examples.data.types.types.A _arg) {
    throw new UnsupportedOperationException("This B has no a");
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
   * Returns a examples.data.types.types.B from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.data.types.types.B fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.data.types.types.B from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.data.types.types.B fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.data.types.types.B from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.data.types.types.B fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.data.types.types.B
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.data.types.types.B fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.data.types.types.B tmp;
    java.util.ArrayList<examples.data.types.types.B> results = new java.util.ArrayList<examples.data.types.types.B>();

    tmp = examples.data.types.types.b.b1.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.data.types.types.b.b2.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.data.types.types.b.b3.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.data.types.types.b.goo.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a B");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("B").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.data.types.types.B", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.data.types.types.B from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.types.types.B fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.data.types.types.B from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.types.types.B fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.data.types.types.B reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.data.types.types.B> enumB = null;
  public static final tom.library.enumerator.Enumeration<examples.data.types.types.B> tmpenumB = new tom.library.enumerator.Enumeration<examples.data.types.types.B>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.types.types.B>>) null);

  public static tom.library.enumerator.Enumeration<examples.data.types.types.B> getEnumeration() {
    if(enumB == null) { 
      enumB = examples.data.types.types.b.b1.funMake().apply(examples.data.types.types.B.tmpenumB)
        .plus(examples.data.types.types.b.b2.funMake().apply(examples.data.types.types.B.tmpenumB))
        .plus(examples.data.types.types.b.b3.funMake().apply(examples.data.types.types.B.tmpenumB))
        .plus(examples.data.types.types.b.goo.funMake().apply(examples.data.types.types.A.tmpenumA));

      examples.data.types.types.A.getEnumeration();

      tmpenumB.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.types.types.B>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.types.types.B>> _1() { return enumB.parts(); }
      };

    }
    return enumB;
  }

}
