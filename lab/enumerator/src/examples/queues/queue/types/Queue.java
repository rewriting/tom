
package examples.queues.queue.types;


public abstract class Queue extends examples.queues.queue.QueueAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Queue() {}



  /**
   * Returns true if the term is rooted by the symbol empty
   *
   * @return true if the term is rooted by the symbol empty
   */
  public boolean isempty() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol add
   *
   * @return true if the term is rooted by the symbol add
   */
  public boolean isadd() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol remove
   *
   * @return true if the term is rooted by the symbol remove
   */
  public boolean isremove() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot q
   *
   * @return the subterm corresponding to the slot q
   */
  public examples.queues.queue.types.Queue getq() {
    throw new UnsupportedOperationException("This Queue has no q");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot q
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot q is replaced by _arg
   */
  public Queue setq(examples.queues.queue.types.Queue _arg) {
    throw new UnsupportedOperationException("This Queue has no q");
  }

  /**
   * Returns the subterm corresponding to the slot e
   *
   * @return the subterm corresponding to the slot e
   */
  public examples.queues.queue.types.Elem gete() {
    throw new UnsupportedOperationException("This Queue has no e");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot e
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot e is replaced by _arg
   */
  public Queue sete(examples.queues.queue.types.Elem _arg) {
    throw new UnsupportedOperationException("This Queue has no e");
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
   * Returns a examples.queues.queue.types.Queue from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.queues.queue.types.Queue fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.queues.queue.types.Queue from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.queues.queue.types.Queue fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.queues.queue.types.Queue from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.queues.queue.types.Queue fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.queues.queue.types.Queue
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.queues.queue.types.Queue fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.queues.queue.types.Queue tmp;
    java.util.ArrayList<examples.queues.queue.types.Queue> results = new java.util.ArrayList<examples.queues.queue.types.Queue>();

    tmp = examples.queues.queue.types.queue.empty.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.queues.queue.types.queue.add.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.queues.queue.types.queue.remove.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Queue");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Queue").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.queues.queue.types.Queue", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.queues.queue.types.Queue from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.queues.queue.types.Queue fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.queues.queue.types.Queue from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.queues.queue.types.Queue fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.queues.queue.types.Queue reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue> enumQueue = null;
  static final tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue> tmpenumQueue = new tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.queues.queue.types.Queue>>) null);

  public static tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue> getEnumeration() {
    if(enumQueue == null) { 
      enumQueue = examples.queues.queue.types.queue.empty.funMake().apply(examples.queues.queue.types.Queue.tmpenumQueue)
        .plus(examples.queues.queue.types.queue.add.funMake().apply(examples.queues.queue.types.Elem.tmpenumElem).apply(examples.queues.queue.types.Queue.tmpenumQueue))
        .plus(examples.queues.queue.types.queue.remove.funMake().apply(examples.queues.queue.types.Queue.tmpenumQueue));

      examples.queues.queue.types.Elem.getEnumeration();

      tmpenumQueue.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.queues.queue.types.Queue>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.queues.queue.types.Queue>> _1() { return enumQueue.parts(); }
      };

    }
    return enumQueue;
  }

}
