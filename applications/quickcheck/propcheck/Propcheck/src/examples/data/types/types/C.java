
package examples.data.types.types;


public abstract class C extends examples.data.types.TypesAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected C() {}



  /**
   * Returns true if the term is rooted by the symbol c1
   *
   * @return true if the term is rooted by the symbol c1
   */
  public boolean isc1() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol c2
   *
   * @return true if the term is rooted by the symbol c2
   */
  public boolean isc2() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol c3
   *
   * @return true if the term is rooted by the symbol c3
   */
  public boolean isc3() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol coo
   *
   * @return true if the term is rooted by the symbol coo
   */
  public boolean iscoo() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot b
   *
   * @return the subterm corresponding to the slot b
   */
  public examples.data.types.types.B getb() {
    throw new UnsupportedOperationException("This C has no b");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot b
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot b is replaced by _arg
   */
  public C setb(examples.data.types.types.B _arg) {
    throw new UnsupportedOperationException("This C has no b");
  }

  /**
   * Returns the subterm corresponding to the slot c
   *
   * @return the subterm corresponding to the slot c
   */
  public examples.data.types.types.C getc() {
    throw new UnsupportedOperationException("This C has no c");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot c
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot c is replaced by _arg
   */
  public C setc(examples.data.types.types.C _arg) {
    throw new UnsupportedOperationException("This C has no c");
  }

  /**
   * Returns the subterm corresponding to the slot a
   *
   * @return the subterm corresponding to the slot a
   */
  public examples.data.types.types.A geta() {
    throw new UnsupportedOperationException("This C has no a");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot a
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot a is replaced by _arg
   */
  public C seta(examples.data.types.types.A _arg) {
    throw new UnsupportedOperationException("This C has no a");
  }

  /**
   * Returns the subterm corresponding to the slot aa
   *
   * @return the subterm corresponding to the slot aa
   */
  public examples.data.types.types.A getaa() {
    throw new UnsupportedOperationException("This C has no aa");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot aa
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot aa is replaced by _arg
   */
  public C setaa(examples.data.types.types.A _arg) {
    throw new UnsupportedOperationException("This C has no aa");
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
   * Returns a examples.data.types.types.C from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.data.types.types.C fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.data.types.types.C from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.data.types.types.C fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.data.types.types.C from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.data.types.types.C fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.data.types.types.C
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.data.types.types.C fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.data.types.types.C tmp;
    java.util.ArrayList<examples.data.types.types.C> results = new java.util.ArrayList<examples.data.types.types.C>();

    tmp = examples.data.types.types.c.c1.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.data.types.types.c.c2.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.data.types.types.c.c3.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.data.types.types.c.coo.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a C");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("C").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.data.types.types.C", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.data.types.types.C from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.types.types.C fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.data.types.types.C from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.types.types.C fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.data.types.types.C reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.data.types.types.C> enumC = null;
  public static final tom.library.enumerator.Enumeration<examples.data.types.types.C> tmpenumC = new tom.library.enumerator.Enumeration<examples.data.types.types.C>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.types.types.C>>) null);

  public static tom.library.enumerator.Enumeration<examples.data.types.types.C> getEnumeration() {
    if(enumC == null) { 
      enumC = examples.data.types.types.c.c1.funMake().apply(examples.data.types.types.C.tmpenumC)
        .plus(examples.data.types.types.c.c2.funMake().apply(examples.data.types.types.C.tmpenumC))
        .plus(examples.data.types.types.c.c3.funMake().apply(examples.data.types.types.C.tmpenumC))
        .plus(examples.data.types.types.c.coo.funMake().apply(examples.data.types.types.A.tmpenumA).apply(examples.data.types.types.B.tmpenumB).apply(examples.data.types.types.C.tmpenumC).apply(examples.data.types.types.A.tmpenumA));

      examples.data.types.types.B.getEnumeration();
      examples.data.types.types.A.getEnumeration();

      tmpenumC.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.types.types.C>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.types.types.C>> _1() { return enumC.parts(); }
      };

    }
    return enumC;
  }

}
