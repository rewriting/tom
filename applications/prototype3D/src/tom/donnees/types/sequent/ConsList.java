
package tom.donnees.types.sequent;


public final class ConsList extends tom.donnees.types.sequent.List implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsList";


  private ConsList() {}
  private int hashCode;
  private static ConsList proto = new ConsList();
    private tom.donnees.types.Formule _HeadList;
  private tom.donnees.types.Sequent _TailList;

    /**
     * Constructor that builds a term rooted by ConsList
     *
     * @return a term rooted by ConsList
     */

  public static ConsList make(tom.donnees.types.Formule _HeadList, tom.donnees.types.Sequent _TailList) {

    // use the proto as a model
    proto.initHashCode( _HeadList,  _TailList);
    return (ConsList) factory.build(proto);

  }

  /**
   * Initializes attributes and hashcode of the class
   * 
   * @param  _HeadList
   * @param _TailList
   * @param hashCode hashCode of ConsList
   */
  private void init(tom.donnees.types.Formule _HeadList, tom.donnees.types.Sequent _TailList, int hashCode) {
    this._HeadList = _HeadList;
    this._TailList = _TailList;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   * 
   * @param  _HeadList
   * @param _TailList
   */
  private void initHashCode(tom.donnees.types.Formule _HeadList, tom.donnees.types.Sequent _TailList) {
    this._HeadList = _HeadList;
    this._TailList = _TailList;

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
    return "ConsList";
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
    ConsList clone = new ConsList();
    clone.init( _HeadList,  _TailList, hashCode);
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
    tom.donnees.DonneesAbstractType ao = (tom.donnees.DonneesAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    ConsList tco = (ConsList) ao;
    int _HeadListCmp = (this._HeadList).compareToLPO(tco._HeadList);
    if(_HeadListCmp != 0)
      return _HeadListCmp;

    int _TailListCmp = (this._TailList).compareToLPO(tco._TailList);
    if(_TailListCmp != 0)
      return _TailListCmp;

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
    ConsList tco = (ConsList) ao;
    int _HeadListCmp = (this._HeadList).compareTo(tco._HeadList);
    if(_HeadListCmp != 0)
      return _HeadListCmp;

    int _TailListCmp = (this._TailList).compareTo(tco._TailList);
    if(_TailListCmp != 0)
      return _TailListCmp;

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
   * @return true if obj is a ConsList and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsList) {

      ConsList peer = (ConsList) obj;
      return _HeadList==peer._HeadList && _TailList==peer._TailList && true;
    }
    return false;
  }


   //Sequent interface
  /** 
   * Returns true if the term is rooted by the symbol ConsList
   *
   * @return true, because this is rooted by ConsList
   */
  @Override
  public boolean isConsList() {
    return true;
  }
  
  /** 
   * Returns the attribute tom.donnees.types.Formule
   * 
   * @return the attribute tom.donnees.types.Formule
   */
  @Override
  public tom.donnees.types.Formule getHeadList() {
    return _HeadList;
  }
  
  /**
   * Sets and returns the attribute tom.donnees.types.Sequent
   * 
   * @param set_arg the argument to set
   * @return the attribute tom.donnees.types.Formule which just has been set
   */
  @Override
  public tom.donnees.types.Sequent setHeadList(tom.donnees.types.Formule set_arg) {
    return make(set_arg, _TailList);
  }
  
  /** 
   * Returns the attribute tom.donnees.types.Sequent
   * 
   * @return the attribute tom.donnees.types.Sequent
   */
  @Override
  public tom.donnees.types.Sequent getTailList() {
    return _TailList;
  }
  
  /**
   * Sets and returns the attribute tom.donnees.types.Sequent
   * 
   * @param set_arg the argument to set
   * @return the attribute tom.donnees.types.Sequent which just has been set
   */
  @Override
  public tom.donnees.types.Sequent setTailList(tom.donnees.types.Sequent set_arg) {
    return make(_HeadList, set_arg);
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
      new aterm.ATerm[] {getHeadList().toATerm(), getTailList().toATerm()});
  }

  /** 
   * Apply a conversion on the ATerm contained in the String and returns a tom.donnees.types.Sequent from it
   * 
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static tom.donnees.types.Sequent fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
tom.donnees.types.Formule.fromTerm(appl.getArgument(0),atConv), tom.donnees.types.Sequent.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _HeadList;
      case 1: return _TailList;

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
      case 0: return make((tom.donnees.types.Formule) v, _TailList);
      case 1: return make(_HeadList, (tom.donnees.types.Sequent) v);

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
    if (childs.length == 2  && childs[0] instanceof tom.donnees.types.Formule && childs[1] instanceof tom.donnees.types.Sequent) {
      return make((tom.donnees.types.Formule) childs[0], (tom.donnees.types.Sequent) childs[1]);
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
    return new tom.library.sl.Visitable[] {  _HeadList,  _TailList };
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
    b = (2050996338<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadList.hashCode() << 8);
    a += (_TailList.hashCode());

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
