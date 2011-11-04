
package peano.types.nat;



public final class plus extends peano.types.Nat implements tom.library.sl.Visitable  {
  
  private static String symbolName = "plus";


  private plus() {}
  private int hashCode;
  private static plus gomProto = new plus();
    private peano.types.Nat _x1;
  private peano.types.Nat _x2;

  /**
   * Constructor that builds a term rooted by plus
   *
   * @return a term rooted by plus
   */

  public static plus make(peano.types.Nat _x1, peano.types.Nat _x2) {

    // use the proto as a model
    gomProto.initHashCode( _x1,  _x2);
    return (plus) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _x1
   * @param _x2
   * @param hashCode hashCode of plus
   */
  private void init(peano.types.Nat _x1, peano.types.Nat _x2, int hashCode) {
    this._x1 = _x1;
    this._x2 = _x2;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _x1
   * @param _x2
   */
  private void initHashCode(peano.types.Nat _x1, peano.types.Nat _x2) {
    this._x1 = _x1;
    this._x2 = _x2;

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
    return "plus";
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
    plus clone = new plus();
    clone.init( _x1,  _x2, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("plus(");
    _x1.toStringBuilder(buffer);
buffer.append(",");
    _x2.toStringBuilder(buffer);

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
    peano.PeanoAbstractType ao = (peano.PeanoAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the childs */
    plus tco = (plus) ao;
    int _x1Cmp = (this._x1).compareToLPO(tco._x1);
    if(_x1Cmp != 0)
      return _x1Cmp;

    int _x2Cmp = (this._x2).compareToLPO(tco._x2);
    if(_x2Cmp != 0)
      return _x2Cmp;

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
    peano.PeanoAbstractType ao = (peano.PeanoAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the childs */
    plus tco = (plus) ao;
    int _x1Cmp = (this._x1).compareTo(tco._x1);
    if(_x1Cmp != 0)
      return _x1Cmp;

    int _x2Cmp = (this._x2).compareTo(tco._x2);
    if(_x2Cmp != 0)
      return _x2Cmp;

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
   * @return true if obj is a plus and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof plus) {

      plus peer = (plus) obj;
      return _x1==peer._x1 && _x2==peer._x2 && true;
    }
    return false;
  }


   //Nat interface
  /**
   * Returns true if the term is rooted by the symbol plus
   *
   * @return true, because this is rooted by plus
   */
  @Override
  public boolean isplus() {
    return true;
  }
  
  /**
   * Returns the attribute peano.types.Nat
   *
   * @return the attribute peano.types.Nat
   */
  @Override
  public peano.types.Nat getx1() {
    return _x1;
  }

  /**
   * Sets and returns the attribute peano.types.Nat
   *
   * @param set_arg the argument to set
   * @return the attribute peano.types.Nat which just has been set
   */
  @Override
  public peano.types.Nat setx1(peano.types.Nat set_arg) {
    return make(set_arg, _x2);
  }
  
  /**
   * Returns the attribute peano.types.Nat
   *
   * @return the attribute peano.types.Nat
   */
  @Override
  public peano.types.Nat getx2() {
    return _x2;
  }

  /**
   * Sets and returns the attribute peano.types.Nat
   *
   * @param set_arg the argument to set
   * @return the attribute peano.types.Nat which just has been set
   */
  @Override
  public peano.types.Nat setx2(peano.types.Nat set_arg) {
    return make(_x1, set_arg);
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
      new aterm.ATerm[] {getx1().toATerm(), getx2().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a peano.types.Nat from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static peano.types.Nat fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
peano.types.Nat.fromTerm(appl.getArgument(0),atConv), peano.types.Nat.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _x1;
      case 1: return _x2;

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
      case 0: return make((peano.types.Nat) v, _x2);
      case 1: return make(_x1, (peano.types.Nat) v);

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
    if (childs.length == 2  && childs[0] instanceof peano.types.Nat && childs[1] instanceof peano.types.Nat) {
      return make((peano.types.Nat) childs[0], (peano.types.Nat) childs[1]);
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
    return new tom.library.sl.Visitable[] {  _x1,  _x2 };
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
    b = (2000940979<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_x1.hashCode() << 8);
    a += (_x2.hashCode());

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
