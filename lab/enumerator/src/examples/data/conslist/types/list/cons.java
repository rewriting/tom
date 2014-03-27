
package examples.data.conslist.types.list;



public final class cons extends examples.data.conslist.types.List implements tom.library.sl.Visitable  {
  
  private static String symbolName = "cons";


  private cons() {}
  private int hashCode;
  private static cons gomProto = new cons();
    private examples.data.conslist.types.Elem _e;
  private examples.data.conslist.types.List _l;

  /**
   * Constructor that builds a term rooted by cons
   *
   * @return a term rooted by cons
   */

  public static cons make(examples.data.conslist.types.Elem _e, examples.data.conslist.types.List _l) {

    // use the proto as a model
    gomProto.initHashCode( _e,  _l);
    return (cons) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _e
   * @param _l
   * @param hashCode hashCode of cons
   */
  private void init(examples.data.conslist.types.Elem _e, examples.data.conslist.types.List _l, int hashCode) {
    this._e = _e;
    this._l = _l;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _e
   * @param _l
   */
  private void initHashCode(examples.data.conslist.types.Elem _e, examples.data.conslist.types.List _l) {
    this._e = _e;
    this._l = _l;

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
    return "cons";
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
    cons clone = new cons();
    clone.init( _e,  _l, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("cons(");
    _e.toStringBuilder(buffer);
buffer.append(",");
    _l.toStringBuilder(buffer);

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
    examples.data.conslist.ConsListAbstractType ao = (examples.data.conslist.ConsListAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    cons tco = (cons) ao;
    int _eCmp = (this._e).compareToLPO(tco._e);
    if(_eCmp != 0) {
      return _eCmp;
    }

    int _lCmp = (this._l).compareToLPO(tco._l);
    if(_lCmp != 0) {
      return _lCmp;
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
    examples.data.conslist.ConsListAbstractType ao = (examples.data.conslist.ConsListAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    cons tco = (cons) ao;
    int _eCmp = (this._e).compareTo(tco._e);
    if(_eCmp != 0) {
      return _eCmp;
    }

    int _lCmp = (this._l).compareTo(tco._l);
    if(_lCmp != 0) {
      return _lCmp;
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
   * @return true if obj is a cons and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof cons) {

      cons peer = (cons) obj;
      return _e==peer._e && _l==peer._l && true;
    }
    return false;
  }


   //List interface
  /**
   * Returns true if the term is rooted by the symbol cons
   *
   * @return true, because this is rooted by cons
   */
  @Override
  public boolean iscons() {
    return true;
  }
  
  /**
   * Returns the attribute examples.conslist.types.Elem
   *
   * @return the attribute examples.conslist.types.Elem
   */
  @Override
  public examples.data.conslist.types.Elem gete() {
    return _e;
  }

  /**
   * Sets and returns the attribute examples.conslist.types.List
   *
   * @param set_arg the argument to set
   * @return the attribute examples.conslist.types.Elem which just has been set
   */
  @Override
  public examples.data.conslist.types.List sete(examples.data.conslist.types.Elem set_arg) {
    return make(set_arg, _l);
  }
  
  /**
   * Returns the attribute examples.conslist.types.List
   *
   * @return the attribute examples.conslist.types.List
   */
  @Override
  public examples.data.conslist.types.List getl() {
    return _l;
  }

  /**
   * Sets and returns the attribute examples.conslist.types.List
   *
   * @param set_arg the argument to set
   * @return the attribute examples.conslist.types.List which just has been set
   */
  @Override
  public examples.data.conslist.types.List setl(examples.data.conslist.types.List set_arg) {
    return make(_e, set_arg);
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
      new aterm.ATerm[] {gete().toATerm(), getl().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.conslist.types.List from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.conslist.types.List fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.data.conslist.types.Elem.fromTerm(appl.getArgument(0),atConv), examples.data.conslist.types.List.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _e;
      case 1: return _l;
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
      case 0: return make((examples.data.conslist.types.Elem) v, _l);
      case 1: return make(_e, (examples.data.conslist.types.List) v);
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
    if (children.length == getChildCount()  && children[0] instanceof examples.data.conslist.types.Elem && children[1] instanceof examples.data.conslist.types.List) {
      return make((examples.data.conslist.types.Elem) children[0], (examples.data.conslist.types.List) children[1]);
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
    return new tom.library.sl.Visitable[] { _e,  _l};
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
    b = (-510144754<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_e.hashCode() << 8);
    a += (_l.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.conslist.types.Elem>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.conslist.types.List>,tom.library.enumerator.Enumeration<examples.data.conslist.types.List>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.conslist.types.Elem>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.conslist.types.List>,tom.library.enumerator.Enumeration<examples.data.conslist.types.List>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.conslist.types.List>,tom.library.enumerator.Enumeration<examples.data.conslist.types.List>> apply(final tom.library.enumerator.Enumeration<examples.data.conslist.types.Elem> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.conslist.types.List>,tom.library.enumerator.Enumeration<examples.data.conslist.types.List>>() {
          public tom.library.enumerator.Enumeration<examples.data.conslist.types.List> apply(final tom.library.enumerator.Enumeration<examples.data.conslist.types.List> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.data.conslist.types.Elem,tom.library.enumerator.F<examples.data.conslist.types.List, examples.data.conslist.types.List>>)
        new tom.library.enumerator.F<examples.data.conslist.types.Elem,tom.library.enumerator.F<examples.data.conslist.types.List, examples.data.conslist.types.List>>() {
          public tom.library.enumerator.F<examples.data.conslist.types.List, examples.data.conslist.types.List> apply(final examples.data.conslist.types.Elem t1) {
            return 
        new tom.library.enumerator.F<examples.data.conslist.types.List, examples.data.conslist.types.List>() {
          public examples.data.conslist.types.List apply(final examples.data.conslist.types.List t2) {
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
