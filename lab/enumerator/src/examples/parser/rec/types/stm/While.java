
package examples.parser.rec.types.stm;



public final class While extends examples.parser.rec.types.Stm implements tom.library.sl.Visitable  {
  
  private static String symbolName = "While";


  private While() {}
  private int hashCode;
  private static While gomProto = new While();
    private examples.parser.rec.types.Exp _cond;
  private examples.parser.rec.types.Stm _s1;

  /**
   * Constructor that builds a term rooted by While
   *
   * @return a term rooted by While
   */

  public static While make(examples.parser.rec.types.Exp _cond, examples.parser.rec.types.Stm _s1) {

    // use the proto as a model
    gomProto.initHashCode( _cond,  _s1);
    return (While) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _cond
   * @param _s1
   * @param hashCode hashCode of While
   */
  private void init(examples.parser.rec.types.Exp _cond, examples.parser.rec.types.Stm _s1, int hashCode) {
    this._cond = _cond;
    this._s1 = _s1;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _cond
   * @param _s1
   */
  private void initHashCode(examples.parser.rec.types.Exp _cond, examples.parser.rec.types.Stm _s1) {
    this._cond = _cond;
    this._s1 = _s1;

    this.hashCode = hashFunction();
  }

  /* name and arity */

  /**
   * Returns the name of the symbol
   *
   * @return the name of the symbol
   */
  @Override
  public String symbolName() {
    return "While";
  }

  /**
   * Returns the arity of the symbol
   *
   * @return the arity of the symbol
   */
  private int getArity() {
    return 2;
  }

  /**
   * Copy the object and returns the copy
   *
   * @return a clone of the SharedObject
   */
  public shared.SharedObject duplicate() {
    While clone = new While();
    clone.init( _cond,  _s1, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("While(");
    _cond.toStringBuilder(buffer);
buffer.append(",");
    _s1.toStringBuilder(buffer);

    buffer.append(")");
  }


  /**
   * Compares two terms. This functions implements a total lexicographic path ordering.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare children
   */
  @Override
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    examples.parser.rec.RecAbstractType ao = (examples.parser.rec.RecAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    While tco = (While) ao;
    int _condCmp = (this._cond).compareToLPO(tco._cond);
    if(_condCmp != 0) {
      return _condCmp;
    }

    int _s1Cmp = (this._s1).compareToLPO(tco._s1);
    if(_s1Cmp != 0) {
      return _s1Cmp;
    }

    throw new RuntimeException("Unable to compare");
  }

 /**
   * Compares two terms. This functions implements a total order.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare children
   */
  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    examples.parser.rec.RecAbstractType ao = (examples.parser.rec.RecAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    While tco = (While) ao;
    int _condCmp = (this._cond).compareTo(tco._cond);
    if(_condCmp != 0) {
      return _condCmp;
    }

    int _s1Cmp = (this._s1).compareTo(tco._s1);
    if(_s1Cmp != 0) {
      return _s1Cmp;
    }

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  /**
   * Returns hashCode
   *
   * @return hashCode
   */
  @Override
  public final int hashCode() {
    return hashCode;
  }

  /**
   * Checks if a SharedObject is equivalent to the current object
   *
   * @param obj SharedObject to test
   * @return true if obj is a While and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof While) {

      While peer = (While) obj;
      return _cond==peer._cond && _s1==peer._s1 && true;
    }
    return false;
  }


   //Stm interface
  /**
   * Returns true if the term is rooted by the symbol While
   *
   * @return true, because this is rooted by While
   */
  @Override
  public boolean isWhile() {
    return true;
  }
  
  /**
   * Returns the attribute parser.rec.types.Exp
   *
   * @return the attribute parser.rec.types.Exp
   */
  @Override
  public examples.parser.rec.types.Exp getcond() {
    return _cond;
  }

  /**
   * Sets and returns the attribute parser.rec.types.Stm
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.Exp which just has been set
   */
  @Override
  public examples.parser.rec.types.Stm setcond(examples.parser.rec.types.Exp set_arg) {
    return make(set_arg, _s1);
  }
  
  /**
   * Returns the attribute parser.rec.types.Stm
   *
   * @return the attribute parser.rec.types.Stm
   */
  @Override
  public examples.parser.rec.types.Stm gets1() {
    return _s1;
  }

  /**
   * Sets and returns the attribute parser.rec.types.Stm
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.Stm which just has been set
   */
  @Override
  public examples.parser.rec.types.Stm sets1(examples.parser.rec.types.Stm set_arg) {
    return make(_cond, set_arg);
  }
  
  /* AbstractType */
  /**
   * Returns an ATerm representation of this term.
   *
   * @return an ATerm representation of this term.
   */
  @Override
  public aterm.ATerm toATerm() {
    aterm.ATerm res = super.toATerm();
    if(res != null) {
      // the super class has produced an ATerm (may be a variadic operator)
      return res;
    }
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {getcond().toATerm(), gets1().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Stm from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Stm fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.parser.rec.types.Exp.fromTerm(appl.getArgument(0),atConv), examples.parser.rec.types.Stm.fromTerm(appl.getArgument(1),atConv)
        );
      }
    }
    return null;
  }

  /* Visitable */
  /**
   * Returns the number of children of the term
   *
   * @return the number of children of the term
   */
  public int getChildCount() {
    return 2;
  }

  /**
   * Returns the child at the specified index
   *
   * @param index index of the child to return; must be
             nonnegative and less than the childCount
   * @return the child at the specified index
   * @throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable getChildAt(int index) {
        switch(index) {
      case 0: return _cond;
      case 1: return _s1;
      default: throw new IndexOutOfBoundsException();
 }
 }

  /**
   * Set the child at the specified index
   *
   * @param index index of the child to set; must be
             nonnegative and less than the childCount
   * @param v child to set at the specified index
   * @return the child which was just set
   * @throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
        switch(index) {
      case 0: return make((examples.parser.rec.types.Exp) v, _s1);
      case 1: return make(_cond, (examples.parser.rec.types.Stm) v);
      default: throw new IndexOutOfBoundsException();
 }
  }

  /**
   * Set children to the term
   *
   * @param children array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "children" is different than 2
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    if (children.length == getChildCount()  && children[0] instanceof examples.parser.rec.types.Exp && children[1] instanceof examples.parser.rec.types.Stm) {
      return make((examples.parser.rec.types.Exp) children[0], (examples.parser.rec.types.Stm) children[1]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Returns the whole children of the term
   *
   * @return the children of the term
   */
  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] { _cond,  _s1};
  }

    /**
     * Compute a hashcode for this term.
     * (for internal use)
     *
     * @return a hash value
     */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-290463463<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_cond.hashCode() << 8);
    a += (_s1.hashCode());

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);
    /* ------------------------------------------- report the result */
    return c;
  }

  /**
    * function that returns functional version of the current operator
    * need for initializing the Enumerator
    */
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>>() {
          public tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.parser.rec.types.Exp,tom.library.enumerator.F<examples.parser.rec.types.Stm,examples.parser.rec.types.Stm>>) 
        new tom.library.enumerator.F<examples.parser.rec.types.Exp,tom.library.enumerator.F<examples.parser.rec.types.Stm,examples.parser.rec.types.Stm>>() {
          public tom.library.enumerator.F<examples.parser.rec.types.Stm,examples.parser.rec.types.Stm> apply(final examples.parser.rec.types.Exp t1) {
            return 
        new tom.library.enumerator.F<examples.parser.rec.types.Stm,examples.parser.rec.types.Stm>() {
          public examples.parser.rec.types.Stm apply(final examples.parser.rec.types.Stm t2) {
            return make(t1,t2);
          }
        };
          }
        }),t1),t2).pay();
          }
        };
          }
        };
  }

}
