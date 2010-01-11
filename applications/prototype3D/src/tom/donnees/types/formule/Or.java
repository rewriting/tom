
package tom.donnees.types.formule;


public final class Or extends tom.donnees.types.Formule implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Or";


  private Or() {}
  private int hashCode;
  private static Or proto = new Or();
    private tom.donnees.types.Formule _f1;
  private tom.donnees.types.Formule _f2;

    /**
     * Constructor that builds a term rooted by Or
     *
     * @return a term rooted by Or
     */

  public static Or make(tom.donnees.types.Formule _f1, tom.donnees.types.Formule _f2) {

    // use the proto as a model
    proto.initHashCode( _f1,  _f2);
    return (Or) factory.build(proto);

  }

  /**
   * Initializes attributes and hashcode of the class
   * 
   * @param  _f1
   * @param _f2
   * @param hashCode hashCode of Or
   */
  private void init(tom.donnees.types.Formule _f1, tom.donnees.types.Formule _f2, int hashCode) {
    this._f1 = _f1;
    this._f2 = _f2;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   * 
   * @param  _f1
   * @param _f2
   */
  private void initHashCode(tom.donnees.types.Formule _f1, tom.donnees.types.Formule _f2) {
    this._f1 = _f1;
    this._f2 = _f2;

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
    clone.init( _f1,  _f2, hashCode);
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
    _f1.toStringBuilder(buffer);
buffer.append(",");
    _f2.toStringBuilder(buffer);

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
    tom.donnees.DonneesAbstractType ao = (tom.donnees.DonneesAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    Or tco = (Or) ao;
    int _f1Cmp = (this._f1).compareToLPO(tco._f1);
    if(_f1Cmp != 0)
      return _f1Cmp;

    int _f2Cmp = (this._f2).compareToLPO(tco._f2);
    if(_f2Cmp != 0)
      return _f2Cmp;

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
    tom.donnees.DonneesAbstractType ao = (tom.donnees.DonneesAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* use the hash values to discriminate */
    
    if(hashCode != ao.hashCode())
      return (hashCode < ao.hashCode())?-1:1;

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* last resort: compare the childs */
    Or tco = (Or) ao;
    int _f1Cmp = (this._f1).compareTo(tco._f1);
    if(_f1Cmp != 0)
      return _f1Cmp;

    int _f2Cmp = (this._f2).compareTo(tco._f2);
    if(_f2Cmp != 0)
      return _f2Cmp;

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
      return _f1==peer._f1 && _f2==peer._f2 && true;
    }
    return false;
  }


   //Formule interface
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
   * Returns the attribute tom.donnees.types.Formule
   * 
   * @return the attribute tom.donnees.types.Formule
   */
  @Override
  public tom.donnees.types.Formule getf1() {
    return _f1;
  }
  
  /**
   * Sets and returns the attribute tom.donnees.types.Formule
   * 
   * @param set_arg the argument to set
   * @return the attribute tom.donnees.types.Formule which just has been set
   */
  @Override
  public tom.donnees.types.Formule setf1(tom.donnees.types.Formule set_arg) {
    return make(set_arg, _f2);
  }
  
  /** 
   * Returns the attribute tom.donnees.types.Formule
   * 
   * @return the attribute tom.donnees.types.Formule
   */
  @Override
  public tom.donnees.types.Formule getf2() {
    return _f2;
  }
  
  /**
   * Sets and returns the attribute tom.donnees.types.Formule
   * 
   * @param set_arg the argument to set
   * @return the attribute tom.donnees.types.Formule which just has been set
   */
  @Override
  public tom.donnees.types.Formule setf2(tom.donnees.types.Formule set_arg) {
    return make(_f1, set_arg);
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
      new aterm.ATerm[] {getf1().toATerm(), getf2().toATerm()});
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a tom.donnees.types.Formule from it
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static tom.donnees.types.Formule fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
tom.donnees.types.Formule.fromTerm(appl.getArgument(0),atConv), tom.donnees.types.Formule.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _f1;
      case 1: return _f2;

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
      case 0: return make((tom.donnees.types.Formule) v, _f2);
      case 1: return make(_f1, (tom.donnees.types.Formule) v);

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
    if (childs.length == 2  && childs[0] instanceof tom.donnees.types.Formule && childs[1] instanceof tom.donnees.types.Formule) {
      return make((tom.donnees.types.Formule) childs[0], (tom.donnees.types.Formule) childs[1]);
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
    return new tom.library.sl.Visitable[] {  _f1,  _f2 };
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
    b = (1636685040<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_f1.hashCode() << 8);
    a += (_f2.hashCode());

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
