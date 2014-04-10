
package examples.queues.queue.types;


public abstract class Elem extends examples.queues.queue.QueueAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Elem() {}



  /**
   * Returns true if the term is rooted by the symbol val
   *
   * @return true if the term is rooted by the symbol val
   */
  public boolean isval() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol top
   *
   * @return true if the term is rooted by the symbol top
   */
  public boolean istop() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot v
   *
   * @return the subterm corresponding to the slot v
   */
  public int getv() {
    throw new UnsupportedOperationException("This Elem has no v");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot v
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot v is replaced by _arg
   */
  public Elem setv(int _arg) {
    throw new UnsupportedOperationException("This Elem has no v");
  }

  /**
   * Returns the subterm corresponding to the slot q
   *
   * @return the subterm corresponding to the slot q
   */
  public examples.queues.queue.types.Queue getq() {
    throw new UnsupportedOperationException("This Elem has no q");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot q
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot q is replaced by _arg
   */
  public Elem setq(examples.queues.queue.types.Queue _arg) {
    throw new UnsupportedOperationException("This Elem has no q");
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
   * Returns a examples.queues.queue.types.Elem from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.queues.queue.types.Elem fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.queues.queue.types.Elem from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.queues.queue.types.Elem fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.queues.queue.types.Elem from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.queues.queue.types.Elem fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.queues.queue.types.Elem
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.queues.queue.types.Elem fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.queues.queue.types.Elem tmp;
    java.util.ArrayList<examples.queues.queue.types.Elem> results = new java.util.ArrayList<examples.queues.queue.types.Elem>();

    tmp = examples.queues.queue.types.elem.val.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.queues.queue.types.elem.top.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Elem");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Elem").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.queues.queue.types.Elem", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.queues.queue.types.Elem from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.queues.queue.types.Elem fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.queues.queue.types.Elem from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.queues.queue.types.Elem fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.queues.queue.types.Elem reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.queues.queue.types.Elem> enumElem = null;
  public static final tom.library.enumerator.Enumeration<examples.queues.queue.types.Elem> tmpenumElem = new tom.library.enumerator.Enumeration<examples.queues.queue.types.Elem>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.queues.queue.types.Elem>>) null);

  public static tom.library.enumerator.Enumeration<examples.queues.queue.types.Elem> getEnumeration() {
    if(enumElem == null) { 
      enumElem = examples.queues.queue.types.elem.val.funMake().apply(tom.library.enumerator.Combinators.makeint())
        .plus(examples.queues.queue.types.elem.top.funMake().apply(examples.queues.queue.types.Queue.tmpenumQueue));

      examples.queues.queue.types.Queue.getEnumeration();

      tmpenumElem.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.queues.queue.types.Elem>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.queues.queue.types.Elem>> _1() { return enumElem.parts(); }
      };

    }
    return enumElem;
  }

}
