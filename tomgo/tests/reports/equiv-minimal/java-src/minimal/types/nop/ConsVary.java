
package minimal.types.nop;


public final class ConsVary extends minimal.types.nop.Vary implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsVary";


  private ConsVary() {}
  private int hashCode;
  private static ConsVary proto = new ConsVary();
    private minimal.types.Nop _HeadVary;
  private minimal.types.Nop _TailVary;

    /**
     * Constructor that builds a term rooted by ConsVary
     *
     * @return a term rooted by ConsVary
     */

    public static minimal.types.Nop make(minimal.types.Nop head, minimal.types.Nop tail) {
  if (true) {if (head.isEmptyVary()) { return tail; }
if (head.isConsVary()) { return make(head.getHeadVary(),make(head.getTailVary(),tail)); }
if (!tail.isConsVary() && !tail.isEmptyVary()) { return make(head,make(tail,EmptyVary.make())); }
}
      return realMake( head,  tail);
    }
  
  private static ConsVary realMake(minimal.types.Nop _HeadVary, minimal.types.Nop _TailVary) {

    // use the proto as a model
    proto.initHashCode( _HeadVary,  _TailVary);
    return (ConsVary) factory.build(proto);

  }

  /**
   * Initializes attributes and hashcode of the class
   * 
   * @param  _HeadVary
   * @param _TailVary
   * @param hashCode hashCode of ConsVary
   */
  private void init(minimal.types.Nop _HeadVary, minimal.types.Nop _TailVary, int hashCode) {
    this._HeadVary = _HeadVary;
    this._TailVary = _TailVary;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   * 
   * @param  _HeadVary
   * @param _TailVary
   */
  private void initHashCode(minimal.types.Nop _HeadVary, minimal.types.Nop _TailVary) {
    this._HeadVary = _HeadVary;
    this._TailVary = _TailVary;

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
    return "ConsVary";
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
    ConsVary clone = new ConsVary();
    clone.init( _HeadVary,  _TailVary, hashCode);
    return clone;
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
    minimal.MinimalAbstractType ao = (minimal.MinimalAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    ConsVary tco = (ConsVary) ao;
    int _HeadVaryCmp = (this._HeadVary).compareToLPO(tco._HeadVary);
    if(_HeadVaryCmp != 0)
      return _HeadVaryCmp;

    int _TailVaryCmp = (this._TailVary).compareToLPO(tco._TailVary);
    if(_TailVaryCmp != 0)
      return _TailVaryCmp;

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
    minimal.MinimalAbstractType ao = (minimal.MinimalAbstractType) o;
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
    ConsVary tco = (ConsVary) ao;
    int _HeadVaryCmp = (this._HeadVary).compareTo(tco._HeadVary);
    if(_HeadVaryCmp != 0)
      return _HeadVaryCmp;

    int _TailVaryCmp = (this._TailVary).compareTo(tco._TailVary);
    if(_TailVaryCmp != 0)
      return _TailVaryCmp;

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
   * @return true if obj is a ConsVary and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsVary) {

      ConsVary peer = (ConsVary) obj;
      return _HeadVary==peer._HeadVary && _TailVary==peer._TailVary && true;
    }
    return false;
  }


   //Nop interface
  /** 
   * Returns true if the term is rooted by the symbol ConsVary
   *
   * @return true, because this is rooted by ConsVary
   */
  @Override
  public boolean isConsVary() {
    return true;
  }
  
  /** 
   * Returns the attribute minimal.types.Nop
   * 
   * @return the attribute minimal.types.Nop
   */
  @Override
  public minimal.types.Nop getHeadVary() {
    return _HeadVary;
  }
  
  /**
   * Sets and returns the attribute minimal.types.Nop
   * 
   * @param set_arg the argument to set
   * @return the attribute minimal.types.Nop which just has been set
   */
  @Override
  public minimal.types.Nop setHeadVary(minimal.types.Nop set_arg) {
    return make(set_arg, _TailVary);
  }
  
  /** 
   * Returns the attribute minimal.types.Nop
   * 
   * @return the attribute minimal.types.Nop
   */
  @Override
  public minimal.types.Nop getTailVary() {
    return _TailVary;
  }
  
  /**
   * Sets and returns the attribute minimal.types.Nop
   * 
   * @param set_arg the argument to set
   * @return the attribute minimal.types.Nop which just has been set
   */
  @Override
  public minimal.types.Nop setTailVary(minimal.types.Nop set_arg) {
    return make(_HeadVary, set_arg);
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
      new aterm.ATerm[] {getHeadVary().toATerm(), getTailVary().toATerm()});
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a minimal.types.Nop from it
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static minimal.types.Nop fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
minimal.types.Nop.fromTerm(appl.getArgument(0),atConv), minimal.types.Nop.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _HeadVary;
      case 1: return _TailVary;

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
      case 0: return make((minimal.types.Nop) v, _TailVary);
      case 1: return make(_HeadVary, (minimal.types.Nop) v);

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
    if (childs.length == 2  && childs[0] instanceof minimal.types.Nop && childs[1] instanceof minimal.types.Nop) {
      return make((minimal.types.Nop) childs[0], (minimal.types.Nop) childs[1]);
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
    return new tom.library.sl.Visitable[] {  _HeadVary,  _TailVary };
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
    b = (239063219<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadVary.hashCode() << 8);
    a += (_TailVary.hashCode());

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
