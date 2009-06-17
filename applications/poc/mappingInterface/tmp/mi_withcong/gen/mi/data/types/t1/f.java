
package mi.data.types.t1;


public final class f extends mi.data.types.T1 implements tom.library.sl.Visitable  {
  
  private static String symbolName = "f";


  private f() {}
  private int hashCode;
  private static f proto = new f();
    private mi.data.types.T1 _s1;
  private mi.data.types.T2 _s2;

    /**
     * Constructor that builds a term rooted by f
     *
     * @return a term rooted by f
     */

  public static f make(mi.data.types.T1 _s1, mi.data.types.T2 _s2) {

    // use the proto as a model
    proto.initHashCode( _s1,  _s2);
    return (f) factory.build(proto);

  }

  /**
   * Initializes attributes and hashcode of the class
   * 
   * @param  _s1
   * @param _s2
   * @param hashCode hashCode of f
   */
  private void init(mi.data.types.T1 _s1, mi.data.types.T2 _s2, int hashCode) {
    this._s1 = _s1;
    this._s2 = _s2;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   * 
   * @param  _s1
   * @param _s2
   */
  private void initHashCode(mi.data.types.T1 _s1, mi.data.types.T2 _s2) {
    this._s1 = _s1;
    this._s2 = _s2;

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
    return "f";
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
    f clone = new f();
    clone.init( _s1,  _s2, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("f(");
    _s1.toStringBuilder(buffer);
buffer.append(",");
    _s2.toStringBuilder(buffer);

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
    mi.data.dataAbstractType ao = (mi.data.dataAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    f tco = (f) ao;
    int _s1Cmp = (this._s1).compareToLPO(tco._s1);
    if(_s1Cmp != 0)
      return _s1Cmp;

    int _s2Cmp = (this._s2).compareToLPO(tco._s2);
    if(_s2Cmp != 0)
      return _s2Cmp;

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
    mi.data.dataAbstractType ao = (mi.data.dataAbstractType) o;
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
    f tco = (f) ao;
    int _s1Cmp = (this._s1).compareTo(tco._s1);
    if(_s1Cmp != 0)
      return _s1Cmp;

    int _s2Cmp = (this._s2).compareTo(tco._s2);
    if(_s2Cmp != 0)
      return _s2Cmp;

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
   * @return true if obj is a f and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof f) {

      f peer = (f) obj;
      return _s1==peer._s1 && _s2==peer._s2 && true;
    }
    return false;
  }


   //T1 interface
  /** 
   * Returns true if the term is rooted by the symbol f
   *
   * @return true, because this is rooted by f
   */
  @Override
  public boolean isf() {
    return true;
  }
  
  /** 
   * Returns the attribute mi.data.types.T1
   * 
   * @return the attribute mi.data.types.T1
   */
  @Override
  public mi.data.types.T1 gets1() {
    return _s1;
  }
  
  /**
   * Sets and returns the attribute mi.data.types.T1
   * 
   * @param set_arg the argument to set
   * @return the attribute mi.data.types.T1 which just has been set
   */
  @Override
  public mi.data.types.T1 sets1(mi.data.types.T1 set_arg) {
    return make(set_arg, _s2);
  }
  
  /** 
   * Returns the attribute mi.data.types.T2
   * 
   * @return the attribute mi.data.types.T2
   */
  @Override
  public mi.data.types.T2 gets2() {
    return _s2;
  }
  
  /**
   * Sets and returns the attribute mi.data.types.T1
   * 
   * @param set_arg the argument to set
   * @return the attribute mi.data.types.T2 which just has been set
   */
  @Override
  public mi.data.types.T1 sets2(mi.data.types.T2 set_arg) {
    return make(_s1, set_arg);
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
      new aterm.ATerm[] {gets1().toATerm(), gets2().toATerm()});
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a mi.data.types.T1 from it
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static mi.data.types.T1 fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
mi.data.types.T1.fromTerm(appl.getArgument(0),atConv), mi.data.types.T2.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _s1;
      case 1: return _s2;

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
      case 0: return make((mi.data.types.T1) v, _s2);
      case 1: return make(_s1, (mi.data.types.T2) v);

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
    if (childs.length == 2  && childs[0] instanceof mi.data.types.T1 && childs[1] instanceof mi.data.types.T2) {
      return make((mi.data.types.T1) childs[0], (mi.data.types.T2) childs[1]);
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
    return new tom.library.sl.Visitable[] {  _s1,  _s2 };
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
    b = (290568247<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_s1.hashCode() << 8);
    a += (_s2.hashCode());

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
