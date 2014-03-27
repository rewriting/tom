
package examples.parser.rec.types;


public abstract class Stm extends examples.parser.rec.RecAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Stm() {}



  /**
   * Returns true if the term is rooted by the symbol ConsSeq
   *
   * @return true if the term is rooted by the symbol ConsSeq
   */
  public boolean isConsSeq() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol EmptySeq
   *
   * @return true if the term is rooted by the symbol EmptySeq
   */
  public boolean isEmptySeq() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Assign
   *
   * @return true if the term is rooted by the symbol Assign
   */
  public boolean isAssign() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Print
   *
   * @return true if the term is rooted by the symbol Print
   */
  public boolean isPrint() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol If
   *
   * @return true if the term is rooted by the symbol If
   */
  public boolean isIf() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol While
   *
   * @return true if the term is rooted by the symbol While
   */
  public boolean isWhile() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot HeadSeq
   *
   * @return the subterm corresponding to the slot HeadSeq
   */
  public examples.parser.rec.types.Stm getHeadSeq() {
    throw new UnsupportedOperationException("This Stm has no HeadSeq");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot HeadSeq
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot HeadSeq is replaced by _arg
   */
  public Stm setHeadSeq(examples.parser.rec.types.Stm _arg) {
    throw new UnsupportedOperationException("This Stm has no HeadSeq");
  }

  /**
   * Returns the subterm corresponding to the slot Name
   *
   * @return the subterm corresponding to the slot Name
   */
  public String getName() {
    throw new UnsupportedOperationException("This Stm has no Name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Name is replaced by _arg
   */
  public Stm setName(String _arg) {
    throw new UnsupportedOperationException("This Stm has no Name");
  }

  /**
   * Returns the subterm corresponding to the slot TailSeq
   *
   * @return the subterm corresponding to the slot TailSeq
   */
  public examples.parser.rec.types.Stm getTailSeq() {
    throw new UnsupportedOperationException("This Stm has no TailSeq");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot TailSeq
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot TailSeq is replaced by _arg
   */
  public Stm setTailSeq(examples.parser.rec.types.Stm _arg) {
    throw new UnsupportedOperationException("This Stm has no TailSeq");
  }

  /**
   * Returns the subterm corresponding to the slot s2
   *
   * @return the subterm corresponding to the slot s2
   */
  public examples.parser.rec.types.Stm gets2() {
    throw new UnsupportedOperationException("This Stm has no s2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot s2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot s2 is replaced by _arg
   */
  public Stm sets2(examples.parser.rec.types.Stm _arg) {
    throw new UnsupportedOperationException("This Stm has no s2");
  }

  /**
   * Returns the subterm corresponding to the slot s1
   *
   * @return the subterm corresponding to the slot s1
   */
  public examples.parser.rec.types.Stm gets1() {
    throw new UnsupportedOperationException("This Stm has no s1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot s1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot s1 is replaced by _arg
   */
  public Stm sets1(examples.parser.rec.types.Stm _arg) {
    throw new UnsupportedOperationException("This Stm has no s1");
  }

  /**
   * Returns the subterm corresponding to the slot Exp
   *
   * @return the subterm corresponding to the slot Exp
   */
  public examples.parser.rec.types.Exp getExp() {
    throw new UnsupportedOperationException("This Stm has no Exp");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Exp
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Exp is replaced by _arg
   */
  public Stm setExp(examples.parser.rec.types.Exp _arg) {
    throw new UnsupportedOperationException("This Stm has no Exp");
  }

  /**
   * Returns the subterm corresponding to the slot cond
   *
   * @return the subterm corresponding to the slot cond
   */
  public examples.parser.rec.types.Exp getcond() {
    throw new UnsupportedOperationException("This Stm has no cond");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot cond
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot cond is replaced by _arg
   */
  public Stm setcond(examples.parser.rec.types.Exp _arg) {
    throw new UnsupportedOperationException("This Stm has no cond");
  }

  /**
   * Returns the subterm corresponding to the slot List
   *
   * @return the subterm corresponding to the slot List
   */
  public examples.parser.rec.types.ExpList getList() {
    throw new UnsupportedOperationException("This Stm has no List");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot List
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot List is replaced by _arg
   */
  public Stm setList(examples.parser.rec.types.ExpList _arg) {
    throw new UnsupportedOperationException("This Stm has no List");
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
   * Returns a parser.rec.types.Stm from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.parser.rec.types.Stm fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a parser.rec.types.Stm from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.parser.rec.types.Stm fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a parser.rec.types.Stm from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.parser.rec.types.Stm fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a parser.rec.types.Stm
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.parser.rec.types.Stm fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.parser.rec.types.Stm tmp;
    java.util.ArrayList<examples.parser.rec.types.Stm> results = new java.util.ArrayList<examples.parser.rec.types.Stm>();

    tmp = examples.parser.rec.types.stm.ConsSeq.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.stm.EmptySeq.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.stm.Assign.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.stm.Print.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.stm.If.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.stm.While.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.stm.Seq.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Stm");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Stm").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "parser.rec.types.Stm", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Stm from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Stm fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a parser.rec.types.Stm from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Stm fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.parser.rec.types.Stm reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /**
   * Returns a Collection extracted from the term
   *
   * @return the collection
   * @throws UnsupportedOperationException if the term is not a list
   */
  public java.util.Collection<examples.parser.rec.types.Stm> getCollectionSeq() {
    throw new UnsupportedOperationException("This Stm cannot be converted into a Collection");
  }
          
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm> enumStm = null;
  public static final tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm> tmpenumStm = new tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Stm>>) null);

  public static tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm> getEnumeration() {
    if(enumStm == null) { 
      enumStm = examples.parser.rec.types.stm.Assign.funMake().apply(tom.library.enumerator.Combinators.makeString()).apply(examples.parser.rec.types.Exp.tmpenumExp)
        .plus(examples.parser.rec.types.stm.Print.funMake().apply(examples.parser.rec.types.ExpList.tmpenumExpList))
        .plus(examples.parser.rec.types.stm.If.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp).apply(examples.parser.rec.types.Stm.tmpenumStm).apply(examples.parser.rec.types.Stm.tmpenumStm))
        .plus(examples.parser.rec.types.stm.While.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp).apply(examples.parser.rec.types.Stm.tmpenumStm))
        /*
         * TO GENERATE
         */
        .plus(examples.parser.rec.types.stm.EmptySeq.funMake().apply(examples.parser.rec.types.Stm.tmpenumStm))
        .plus(examples.parser.rec.types.stm.ConsSeq.funMake().apply(examples.parser.rec.types.Stm.tmpenumStm).apply(examples.parser.rec.types.Stm.tmpenumStm))
        ;
      
      examples.parser.rec.types.Exp.getEnumeration();
      examples.parser.rec.types.ExpList.getEnumeration();

      tmpenumStm.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Stm>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Stm>> _1() { return enumStm.parts(); }
      };

    }
    return enumStm;
  }

}
