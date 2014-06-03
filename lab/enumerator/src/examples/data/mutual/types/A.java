
package examples.data.mutual.types;


public abstract class A extends examples.data.mutual.MutualAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected A() {}



  /**
   * Returns true if the term is rooted by the symbol a
   *
   * @return true if the term is rooted by the symbol a
   */
  public boolean isa() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol foo
   *
   * @return true if the term is rooted by the symbol foo
   */
  public boolean isfoo() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol hoo
   *
   * @return true if the term is rooted by the symbol hoo
   */
  public boolean ishoo() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot v
   *
   * @return the subterm corresponding to the slot v
   */
  public int getv() {
    throw new UnsupportedOperationException("This A has no v");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot v
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot v is replaced by _arg
   */
  public A setv(int _arg) {
    throw new UnsupportedOperationException("This A has no v");
  }

  /**
   * Returns the subterm corresponding to the slot b
   *
   * @return the subterm corresponding to the slot b
   */
  public examples.data.mutual.types.B getb() {
    throw new UnsupportedOperationException("This A has no b");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot b
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot b is replaced by _arg
   */
  public A setb(examples.data.mutual.types.B _arg) {
    throw new UnsupportedOperationException("This A has no b");
  }

  /**
   * Returns the subterm corresponding to the slot a
   *
   * @return the subterm corresponding to the slot a
   */
  public examples.data.mutual.types.A geta() {
    throw new UnsupportedOperationException("This A has no a");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot a
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot a is replaced by _arg
   */
  public A seta(examples.data.mutual.types.A _arg) {
    throw new UnsupportedOperationException("This A has no a");
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
   * Returns a examples.data.mutual.types.A from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.data.mutual.types.A fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.data.mutual.types.A from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.data.mutual.types.A fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.data.mutual.types.A from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.data.mutual.types.A fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.data.mutual.types.A
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.data.mutual.types.A fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.data.mutual.types.A tmp;
    java.util.ArrayList<examples.data.mutual.types.A> results = new java.util.ArrayList<examples.data.mutual.types.A>();

    tmp = examples.data.mutual.types.a.a.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.data.mutual.types.a.foo.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.data.mutual.types.a.hoo.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a A");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("A").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.data.mutual.types.A", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.data.mutual.types.A from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.mutual.types.A fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.data.mutual.types.A from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.mutual.types.A fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.data.mutual.types.A reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.data.mutual.types.A> enumA = null;
  public static final tom.library.enumerator.Enumeration<examples.data.mutual.types.A> tmpenumA = new tom.library.enumerator.Enumeration<examples.data.mutual.types.A>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.mutual.types.A>>) null);

  public static tom.library.enumerator.Enumeration<examples.data.mutual.types.A> getEnumeration() {
    if(enumA == null) { 
      enumA = examples.data.mutual.types.a.a.funMake().apply(examples.data.mutual.types.A.tmpenumA)
        .plus(examples.data.mutual.types.a.foo.funMake().apply(examples.data.mutual.types.B.tmpenumB))
        .plus(examples.data.mutual.types.a.hoo.funMake().apply(examples.data.mutual.types.A.tmpenumA).apply(tom.library.enumerator.Combinators.makeint()));

      examples.data.mutual.types.B.getEnumeration();

      tmpenumA.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.mutual.types.A>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.data.mutual.types.A>> _1() { return enumA.parts(); }
      };

    }
    return enumA;
  }

}
