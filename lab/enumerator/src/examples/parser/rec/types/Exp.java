
package examples.parser.rec.types;


public abstract class Exp extends examples.parser.rec.RecAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Exp() {}



  /**
   * Returns true if the term is rooted by the symbol Id
   *
   * @return true if the term is rooted by the symbol Id
   */
  public boolean isId() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Num
   *
   * @return true if the term is rooted by the symbol Num
   */
  public boolean isNum() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol OpExp
   *
   * @return true if the term is rooted by the symbol OpExp
   */
  public boolean isOpExp() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol SeqExp
   *
   * @return true if the term is rooted by the symbol SeqExp
   */
  public boolean isSeqExp() {
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
   * Returns true if the term is rooted by the symbol False
   *
   * @return true if the term is rooted by the symbol False
   */
  public boolean isFalse() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol NotExp
   *
   * @return true if the term is rooted by the symbol NotExp
   */
  public boolean isNotExp() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Value
   *
   * @return the subterm corresponding to the slot Value
   */
  public int getValue() {
    throw new UnsupportedOperationException("This Exp has no Value");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Value
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Value is replaced by _arg
   */
  public Exp setValue(int _arg) {
    throw new UnsupportedOperationException("This Exp has no Value");
  }

  /**
   * Returns the subterm corresponding to the slot Exp1
   *
   * @return the subterm corresponding to the slot Exp1
   */
  public examples.parser.rec.types.Exp getExp1() {
    throw new UnsupportedOperationException("This Exp has no Exp1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Exp1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Exp1 is replaced by _arg
   */
  public Exp setExp1(examples.parser.rec.types.Exp _arg) {
    throw new UnsupportedOperationException("This Exp has no Exp1");
  }

  /**
   * Returns the subterm corresponding to the slot Name
   *
   * @return the subterm corresponding to the slot Name
   */
  public String getName() {
    throw new UnsupportedOperationException("This Exp has no Name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Name is replaced by _arg
   */
  public Exp setName(String _arg) {
    throw new UnsupportedOperationException("This Exp has no Name");
  }

  /**
   * Returns the subterm corresponding to the slot Stm
   *
   * @return the subterm corresponding to the slot Stm
   */
  public examples.parser.rec.types.Stm getStm() {
    throw new UnsupportedOperationException("This Exp has no Stm");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Stm
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Stm is replaced by _arg
   */
  public Exp setStm(examples.parser.rec.types.Stm _arg) {
    throw new UnsupportedOperationException("This Exp has no Stm");
  }

  /**
   * Returns the subterm corresponding to the slot Exp2
   *
   * @return the subterm corresponding to the slot Exp2
   */
  public examples.parser.rec.types.Exp getExp2() {
    throw new UnsupportedOperationException("This Exp has no Exp2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Exp2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Exp2 is replaced by _arg
   */
  public Exp setExp2(examples.parser.rec.types.Exp _arg) {
    throw new UnsupportedOperationException("This Exp has no Exp2");
  }

  /**
   * Returns the subterm corresponding to the slot Op
   *
   * @return the subterm corresponding to the slot Op
   */
  public examples.parser.rec.types.Op getOp() {
    throw new UnsupportedOperationException("This Exp has no Op");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Op
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Op is replaced by _arg
   */
  public Exp setOp(examples.parser.rec.types.Op _arg) {
    throw new UnsupportedOperationException("This Exp has no Op");
  }

  /**
   * Returns the subterm corresponding to the slot Exp
   *
   * @return the subterm corresponding to the slot Exp
   */
  public examples.parser.rec.types.Exp getExp() {
    throw new UnsupportedOperationException("This Exp has no Exp");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Exp
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Exp is replaced by _arg
   */
  public Exp setExp(examples.parser.rec.types.Exp _arg) {
    throw new UnsupportedOperationException("This Exp has no Exp");
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
   * Returns a parser.rec.types.Exp from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.parser.rec.types.Exp fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a parser.rec.types.Exp from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.parser.rec.types.Exp fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a parser.rec.types.Exp from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.parser.rec.types.Exp fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a parser.rec.types.Exp
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.parser.rec.types.Exp fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.parser.rec.types.Exp tmp;
    java.util.ArrayList<examples.parser.rec.types.Exp> results = new java.util.ArrayList<examples.parser.rec.types.Exp>();

    tmp = examples.parser.rec.types.exp.Id.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.exp.Num.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.exp.OpExp.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.exp.SeqExp.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.exp.True.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.exp.False.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.parser.rec.types.exp.NotExp.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Exp");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Exp").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "parser.rec.types.Exp", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Exp from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Exp fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a parser.rec.types.Exp from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Exp fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.parser.rec.types.Exp reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp> enumExp = null;
  public static final tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp> tmpenumExp = new tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Exp>>) null);

  public static tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp> getEnumeration() {
    if(enumExp == null) { 
      enumExp = examples.parser.rec.types.exp.Id.funMake().apply(tom.library.enumerator.Combinators.makeString())
    		  
        .plus(examples.parser.rec.types.exp.Num.funMake().apply(tom.library.enumerator.Combinators.makeint()))
        .plus(examples.parser.rec.types.exp.OpExp.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp).apply(examples.parser.rec.types.Op.tmpenumOp).apply(examples.parser.rec.types.Exp.tmpenumExp))
        //.plus(examples.parser.rec.types.exp.SeqExp.funMake().apply(examples.parser.rec.types.Stm.tmpenumStm).apply(examples.parser.rec.types.Exp.tmpenumExp))
        .plus(examples.parser.rec.types.exp.True.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp))
        //.plus(examples.parser.rec.types.exp.False.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp))
        //.plus(examples.parser.rec.types.exp.NotExp.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp));
;
      
      /*
       * BUG : plus not commutative
       * True should be BEFORE Num
       */
      enumExp = 
    		  
    		  examples.parser.rec.types.exp.Num.funMake().apply(tom.library.enumerator.Combinators.makeint()).plus(
    		  examples.parser.rec.types.exp.True.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp))

    		  //examples.parser.rec.types.exp.True.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp)
    		  //examples.parser.rec.types.exp.Id.funMake().apply(tom.library.enumerator.Combinators.makeString())
    		  //.plus(examples.parser.rec.types.exp.Num.funMake().apply(tom.library.enumerator.Combinators.makeint()))
    		  //.plus(examples.parser.rec.types.exp.False.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp))
    		  //.plus(examples.parser.rec.types.exp.NotExp.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp))
    		  //.plus(examples.parser.rec.types.exp.Id.funMake().apply(tom.library.enumerator.Combinators.makeString()))
    		  //.plus(examples.parser.rec.types.exp.OpExp.funMake().apply(examples.parser.rec.types.Exp.tmpenumExp).apply(examples.parser.rec.types.Op.tmpenumOp).apply(examples.parser.rec.types.Exp.tmpenumExp))
;

      
      examples.parser.rec.types.Stm.getEnumeration();
      examples.parser.rec.types.Op.getEnumeration();

      tmpenumExp.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Exp>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.parser.rec.types.Exp>> _1() { return enumExp.parts(); }
      };

    }
    return enumExp;
  }

}
