
package sa.rule.types;


public abstract class Term extends sa.rule.RuleAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Term() {}



  /**
   * Returns true if the term is rooted by the symbol Appl
   *
   * @return true if the term is rooted by the symbol Appl
   */
  public boolean isAppl() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Var
   *
   * @return true if the term is rooted by the symbol Var
   */
  public boolean isVar() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol BuiltinInt
   *
   * @return true if the term is rooted by the symbol BuiltinInt
   */
  public boolean isBuiltinInt() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Anti
   *
   * @return true if the term is rooted by the symbol Anti
   */
  public boolean isAnti() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol At
   *
   * @return true if the term is rooted by the symbol At
   */
  public boolean isAt() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Add
   *
   * @return true if the term is rooted by the symbol Add
   */
  public boolean isAdd() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Sub
   *
   * @return true if the term is rooted by the symbol Sub
   */
  public boolean isSub() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Inter
   *
   * @return true if the term is rooted by the symbol Inter
   */
  public boolean isInter() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Match
   *
   * @return true if the term is rooted by the symbol Match
   */
  public boolean isMatch() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol TrueMatch
   *
   * @return true if the term is rooted by the symbol TrueMatch
   */
  public boolean isTrueMatch() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Empty
   *
   * @return true if the term is rooted by the symbol Empty
   */
  public boolean isEmpty() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot i
   *
   * @return the subterm corresponding to the slot i
   */
  public int geti() {
    throw new UnsupportedOperationException("This Term has no i");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot i
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot i is replaced by _arg
   */
  public Term seti(int _arg) {
    throw new UnsupportedOperationException("This Term has no i");
  }

  /**
   * Returns the subterm corresponding to the slot args
   *
   * @return the subterm corresponding to the slot args
   */
  public sa.rule.types.TermList getargs() {
    throw new UnsupportedOperationException("This Term has no args");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot args
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot args is replaced by _arg
   */
  public Term setargs(sa.rule.types.TermList _arg) {
    throw new UnsupportedOperationException("This Term has no args");
  }

  /**
   * Returns the subterm corresponding to the slot term
   *
   * @return the subterm corresponding to the slot term
   */
  public sa.rule.types.Term getterm() {
    throw new UnsupportedOperationException("This Term has no term");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot term
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot term is replaced by _arg
   */
  public Term setterm(sa.rule.types.Term _arg) {
    throw new UnsupportedOperationException("This Term has no term");
  }

  /**
   * Returns the subterm corresponding to the slot symbol
   *
   * @return the subterm corresponding to the slot symbol
   */
  public String getsymbol() {
    throw new UnsupportedOperationException("This Term has no symbol");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot symbol
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot symbol is replaced by _arg
   */
  public Term setsymbol(String _arg) {
    throw new UnsupportedOperationException("This Term has no symbol");
  }

  /**
   * Returns the subterm corresponding to the slot term2
   *
   * @return the subterm corresponding to the slot term2
   */
  public sa.rule.types.Term getterm2() {
    throw new UnsupportedOperationException("This Term has no term2");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot term2
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot term2 is replaced by _arg
   */
  public Term setterm2(sa.rule.types.Term _arg) {
    throw new UnsupportedOperationException("This Term has no term2");
  }

  /**
   * Returns the subterm corresponding to the slot name
   *
   * @return the subterm corresponding to the slot name
   */
  public String getname() {
    throw new UnsupportedOperationException("This Term has no name");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot name
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot name is replaced by _arg
   */
  public Term setname(String _arg) {
    throw new UnsupportedOperationException("This Term has no name");
  }

  /**
   * Returns the subterm corresponding to the slot addlist
   *
   * @return the subterm corresponding to the slot addlist
   */
  public sa.rule.types.AddList getaddlist() {
    throw new UnsupportedOperationException("This Term has no addlist");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot addlist
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot addlist is replaced by _arg
   */
  public Term setaddlist(sa.rule.types.AddList _arg) {
    throw new UnsupportedOperationException("This Term has no addlist");
  }

  /**
   * Returns the subterm corresponding to the slot term1
   *
   * @return the subterm corresponding to the slot term1
   */
  public sa.rule.types.Term getterm1() {
    throw new UnsupportedOperationException("This Term has no term1");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot term1
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot term1 is replaced by _arg
   */
  public Term setterm1(sa.rule.types.Term _arg) {
    throw new UnsupportedOperationException("This Term has no term1");
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
  public sa.rule.types.Term reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<sa.rule.types.Term> enumTerm = null;
  public static final tom.library.enumerator.Enumeration<sa.rule.types.Term> tmpenumTerm = new tom.library.enumerator.Enumeration<sa.rule.types.Term>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Term>>) null);

  public static tom.library.enumerator.Enumeration<sa.rule.types.Term> getEnumeration() {
    if(enumTerm == null) {
      enumTerm = sa.rule.types.term.Appl.funMake().apply(tom.library.enumerator.Combinators.makeString()).apply(sa.rule.types.TermList.tmpenumTermList)
        .plus(sa.rule.types.term.Var.funMake().apply(tom.library.enumerator.Combinators.makeString()))
        .plus(sa.rule.types.term.BuiltinInt.funMake().apply(tom.library.enumerator.Combinators.makeint()))
        .plus(sa.rule.types.term.Anti.funMake().apply(sa.rule.types.Term.tmpenumTerm))
        .plus(sa.rule.types.term.At.funMake().apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.Term.tmpenumTerm))
        .plus(sa.rule.types.term.Add.funMake().apply(sa.rule.types.AddList.tmpenumAddList))
        .plus(sa.rule.types.term.Sub.funMake().apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.Term.tmpenumTerm))
        .plus(sa.rule.types.term.Inter.funMake().apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.Term.tmpenumTerm))
        .plus(sa.rule.types.term.Match.funMake().apply(sa.rule.types.Term.tmpenumTerm).apply(sa.rule.types.Term.tmpenumTerm))
        .plus(sa.rule.types.term.TrueMatch.funMake().apply(sa.rule.types.Term.tmpenumTerm))
        .plus(sa.rule.types.term.Empty.funMake().apply(sa.rule.types.Term.tmpenumTerm));

      sa.rule.types.TermList.getEnumeration();
      sa.rule.types.AddList.getEnumeration();

      tmpenumTerm.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Term>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<sa.rule.types.Term>> _1() { return enumTerm.parts(); }
      };

    }
    return enumTerm;
  }

}
