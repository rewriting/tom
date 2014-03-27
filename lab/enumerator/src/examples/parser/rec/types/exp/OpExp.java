
package examples.parser.rec.types.exp;



public final class OpExp extends examples.parser.rec.types.Exp implements tom.library.sl.Visitable  {
  
  private static String symbolName = "OpExp";


  private OpExp() {}
  private int hashCode;
  private static OpExp gomProto = new OpExp();
    private examples.parser.rec.types.Exp _Exp1;
  private examples.parser.rec.types.Op _Op;
  private examples.parser.rec.types.Exp _Exp2;

  /**
   * Constructor that builds a term rooted by OpExp
   *
   * @return a term rooted by OpExp
   */

  public static OpExp make(examples.parser.rec.types.Exp _Exp1, examples.parser.rec.types.Op _Op, examples.parser.rec.types.Exp _Exp2) {

    // use the proto as a model
    gomProto.initHashCode( _Exp1,  _Op,  _Exp2);
    return (OpExp) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Exp1
   * @param _Op
   * @param _Exp2
   * @param hashCode hashCode of OpExp
   */
  private void init(examples.parser.rec.types.Exp _Exp1, examples.parser.rec.types.Op _Op, examples.parser.rec.types.Exp _Exp2, int hashCode) {
    this._Exp1 = _Exp1;
    this._Op = _Op;
    this._Exp2 = _Exp2;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Exp1
   * @param _Op
   * @param _Exp2
   */
  private void initHashCode(examples.parser.rec.types.Exp _Exp1, examples.parser.rec.types.Op _Op, examples.parser.rec.types.Exp _Exp2) {
    this._Exp1 = _Exp1;
    this._Op = _Op;
    this._Exp2 = _Exp2;

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
    return "OpExp";
  }

  /**
   * Returns the arity of the symbol
   *
   * @return the arity of the symbol
   */
  private int getArity() {
    return 3;
  }

  /**
   * Copy the object and returns the copy
   *
   * @return a clone of the SharedObject
   */
  public shared.SharedObject duplicate() {
    OpExp clone = new OpExp();
    clone.init( _Exp1,  _Op,  _Exp2, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("OpExp(");
    _Exp1.toStringBuilder(buffer);
buffer.append(",");
    _Op.toStringBuilder(buffer);
buffer.append(",");
    _Exp2.toStringBuilder(buffer);

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
    OpExp tco = (OpExp) ao;
    int _Exp1Cmp = (this._Exp1).compareToLPO(tco._Exp1);
    if(_Exp1Cmp != 0) {
      return _Exp1Cmp;
    }

    int _OpCmp = (this._Op).compareToLPO(tco._Op);
    if(_OpCmp != 0) {
      return _OpCmp;
    }

    int _Exp2Cmp = (this._Exp2).compareToLPO(tco._Exp2);
    if(_Exp2Cmp != 0) {
      return _Exp2Cmp;
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
    OpExp tco = (OpExp) ao;
    int _Exp1Cmp = (this._Exp1).compareTo(tco._Exp1);
    if(_Exp1Cmp != 0) {
      return _Exp1Cmp;
    }

    int _OpCmp = (this._Op).compareTo(tco._Op);
    if(_OpCmp != 0) {
      return _OpCmp;
    }

    int _Exp2Cmp = (this._Exp2).compareTo(tco._Exp2);
    if(_Exp2Cmp != 0) {
      return _Exp2Cmp;
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
   * @return true if obj is a OpExp and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof OpExp) {

      OpExp peer = (OpExp) obj;
      return _Exp1==peer._Exp1 && _Op==peer._Op && _Exp2==peer._Exp2 && true;
    }
    return false;
  }


   //Exp interface
  /**
   * Returns true if the term is rooted by the symbol OpExp
   *
   * @return true, because this is rooted by OpExp
   */
  @Override
  public boolean isOpExp() {
    return true;
  }
  
  /**
   * Returns the attribute parser.rec.types.Exp
   *
   * @return the attribute parser.rec.types.Exp
   */
  @Override
  public examples.parser.rec.types.Exp getExp1() {
    return _Exp1;
  }

  /**
   * Sets and returns the attribute parser.rec.types.Exp
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.Exp which just has been set
   */
  @Override
  public examples.parser.rec.types.Exp setExp1(examples.parser.rec.types.Exp set_arg) {
    return make(set_arg, _Op, _Exp2);
  }
  
  /**
   * Returns the attribute parser.rec.types.Op
   *
   * @return the attribute parser.rec.types.Op
   */
  @Override
  public examples.parser.rec.types.Op getOp() {
    return _Op;
  }

  /**
   * Sets and returns the attribute parser.rec.types.Exp
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.Op which just has been set
   */
  @Override
  public examples.parser.rec.types.Exp setOp(examples.parser.rec.types.Op set_arg) {
    return make(_Exp1, set_arg, _Exp2);
  }
  
  /**
   * Returns the attribute parser.rec.types.Exp
   *
   * @return the attribute parser.rec.types.Exp
   */
  @Override
  public examples.parser.rec.types.Exp getExp2() {
    return _Exp2;
  }

  /**
   * Sets and returns the attribute parser.rec.types.Exp
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.Exp which just has been set
   */
  @Override
  public examples.parser.rec.types.Exp setExp2(examples.parser.rec.types.Exp set_arg) {
    return make(_Exp1, _Op, set_arg);
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
      new aterm.ATerm[] {getExp1().toATerm(), getOp().toATerm(), getExp2().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Exp from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Exp fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.parser.rec.types.Exp.fromTerm(appl.getArgument(0),atConv), examples.parser.rec.types.Op.fromTerm(appl.getArgument(1),atConv), examples.parser.rec.types.Exp.fromTerm(appl.getArgument(2),atConv)
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
    return 3;
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
      case 0: return _Exp1;
      case 1: return _Op;
      case 2: return _Exp2;
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
      case 0: return make((examples.parser.rec.types.Exp) v, _Op, _Exp2);
      case 1: return make(_Exp1, (examples.parser.rec.types.Op) v, _Exp2);
      case 2: return make(_Exp1, _Op, (examples.parser.rec.types.Exp) v);
      default: throw new IndexOutOfBoundsException();
 }
  }

  /**
   * Set children to the term
   *
   * @param children array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "children" is different than 3
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    if (children.length == getChildCount()  && children[0] instanceof examples.parser.rec.types.Exp && children[1] instanceof examples.parser.rec.types.Op && children[2] instanceof examples.parser.rec.types.Exp) {
      return make((examples.parser.rec.types.Exp) children[0], (examples.parser.rec.types.Op) children[1], (examples.parser.rec.types.Exp) children[2]);
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
    return new tom.library.sl.Visitable[] { _Exp1,  _Op,  _Exp2};
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
    b = (1845498456<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_Exp1.hashCode() << 16);
    a += (_Op.hashCode() << 8);
    a += (_Exp2.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Op>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Op>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Op>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>>> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Op>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.Op> t2) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>>() {
          public tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp> t3) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.parser.rec.types.Exp,tom.library.enumerator.F<examples.parser.rec.types.Op,tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Exp>>>) 
        new tom.library.enumerator.F<examples.parser.rec.types.Exp,tom.library.enumerator.F<examples.parser.rec.types.Op,tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Exp>>>() {
          public tom.library.enumerator.F<examples.parser.rec.types.Op,tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Exp>> apply(final examples.parser.rec.types.Exp t1) {
            return 
        new tom.library.enumerator.F<examples.parser.rec.types.Op,tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Exp>>() {
          public tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Exp> apply(final examples.parser.rec.types.Op t2) {
            return 
        new tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Exp>() {
          public examples.parser.rec.types.Exp apply(final examples.parser.rec.types.Exp t3) {
            return make(t1,t2,t3);
          }
        };
          }
        };
          }
        }),t1),t2),t3).pay();
          }
        };
          }
        };
          }
        };
  }

}
