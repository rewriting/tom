
package gates.logic.types.bool;



public final class Or extends gates.logic.types.Bool implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Or";


  private Or() {}
  private int hashCode;
  private static Or proto = new Or();
  
private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_Bool(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Bool(Object t) {return  (t instanceof gates.logic.types.Bool) ;}private static  gates.logic.types.Bool  tom_make_Not( gates.logic.types.Bool  t0) { return  gates.logic.types.bool.Not.make(t0) ;}private static  gates.logic.types.Bool  tom_make_Nand( gates.logic.types.Bool  t0,  gates.logic.types.Bool  t1) { return  gates.logic.types.bool.Nand.make(t0, t1) ;}


























































  private gates.logic.types.Bool _b1;
  private gates.logic.types.Bool _b2;

  /**
   * Constructor that builds a term rooted by Or
   *
   * @return a term rooted by Or
   */

    public static gates.logic.types.Bool make(gates.logic.types.Bool arg_1, gates.logic.types.Bool arg_2) {
  if (true) {    {{if (tom_is_sort_Bool(arg_1)) {if (tom_is_sort_Bool(arg_2)) {
 return tom_make_Nand(tom_make_Not((( gates.logic.types.Bool )arg_1)),tom_make_Not((( gates.logic.types.Bool )arg_2))); }}}}

}
      return realMake( arg_1,  arg_2);
    }
  
  private static Or realMake(gates.logic.types.Bool _b1, gates.logic.types.Bool _b2) {

    // use the proto as a model
    proto.initHashCode( _b1,  _b2);
    return (Or) factory.build(proto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _b1
   * @param _b2
   * @param hashCode hashCode of Or
   */
  private void init(gates.logic.types.Bool _b1, gates.logic.types.Bool _b2, int hashCode) {
    this._b1 = _b1;
    this._b2 = _b2;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _b1
   * @param _b2
   */
  private void initHashCode(gates.logic.types.Bool _b1, gates.logic.types.Bool _b2) {
    this._b1 = _b1;
    this._b2 = _b2;

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
    return "Or";
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
    Or clone = new Or();
    clone.init( _b1,  _b2, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Or(");
    _b1.toStringBuilder(buffer);
buffer.append(",");
    _b2.toStringBuilder(buffer);

    buffer.append(")");
  }


  /**
   * Compares two terms. This functions implements a total lexicographic path ordering.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare childs
   */
  @Override
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    gates.logic.LogicAbstractType ao = (gates.logic.LogicAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the childs */
    Or tco = (Or) ao;
    int _b1Cmp = (this._b1).compareToLPO(tco._b1);
    if(_b1Cmp != 0)
      return _b1Cmp;

    int _b2Cmp = (this._b2).compareToLPO(tco._b2);
    if(_b2Cmp != 0)
      return _b2Cmp;

    throw new RuntimeException("Unable to compare");
  }

 /**
   * Compares two terms. This functions implements a total order.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare childs
   */
  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    gates.logic.LogicAbstractType ao = (gates.logic.LogicAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the childs */
    Or tco = (Or) ao;
    int _b1Cmp = (this._b1).compareTo(tco._b1);
    if(_b1Cmp != 0)
      return _b1Cmp;

    int _b2Cmp = (this._b2).compareTo(tco._b2);
    if(_b2Cmp != 0)
      return _b2Cmp;

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
   * @return true if obj is a Or and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Or) {

      Or peer = (Or) obj;
      return _b1==peer._b1 && _b2==peer._b2 && true;
    }
    return false;
  }


   //Bool interface
  /**
   * Returns true if the term is rooted by the symbol Or
   *
   * @return true, because this is rooted by Or
   */
  @Override
  public boolean isOr() {
    return true;
  }
  
  /**
   * Returns the attribute gates.logic.types.Bool
   *
   * @return the attribute gates.logic.types.Bool
   */
  @Override
  public gates.logic.types.Bool getb1() {
    return _b1;
  }

  /**
   * Sets and returns the attribute gates.logic.types.Bool
   *
   * @param set_arg the argument to set
   * @return the attribute gates.logic.types.Bool which just has been set
   */
  @Override
  public gates.logic.types.Bool setb1(gates.logic.types.Bool set_arg) {
    return make(set_arg, _b2);
  }
  
  /**
   * Returns the attribute gates.logic.types.Bool
   *
   * @return the attribute gates.logic.types.Bool
   */
  @Override
  public gates.logic.types.Bool getb2() {
    return _b2;
  }

  /**
   * Sets and returns the attribute gates.logic.types.Bool
   *
   * @param set_arg the argument to set
   * @return the attribute gates.logic.types.Bool which just has been set
   */
  @Override
  public gates.logic.types.Bool setb2(gates.logic.types.Bool set_arg) {
    return make(_b1, set_arg);
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
      new aterm.ATerm[] {getb1().toATerm(), getb2().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a gates.logic.types.Bool from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static gates.logic.types.Bool fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
gates.logic.types.Bool.fromTerm(appl.getArgument(0),atConv), gates.logic.types.Bool.fromTerm(appl.getArgument(1),atConv)
        );
      }
    }
    return null;
  }

  /* Visitable */
  /**
   * Returns the number of childs of the term
   *
   * @return the number of childs of the term
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
      case 0: return _b1;
      case 1: return _b2;

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
      case 0: return make((gates.logic.types.Bool) v, _b2);
      case 1: return make(_b1, (gates.logic.types.Bool) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Set children to the term
   *
   * @param childs array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "childs" is different than 2
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 2  && childs[0] instanceof gates.logic.types.Bool && childs[1] instanceof gates.logic.types.Bool) {
      return make((gates.logic.types.Bool) childs[0], (gates.logic.types.Bool) childs[1]);
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
    return new tom.library.sl.Visitable[] {  _b1,  _b2 };
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
    b = (-1179782697<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_b1.hashCode() << 8);
    a += (_b2.hashCode());

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

}
