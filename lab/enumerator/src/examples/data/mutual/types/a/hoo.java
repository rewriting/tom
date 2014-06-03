
package examples.data.mutual.types.a;



public final class hoo extends examples.data.mutual.types.A implements tom.library.sl.Visitable  {
  
  private static String symbolName = "hoo";


  private hoo() {}
  private int hashCode;
  private static hoo gomProto = new hoo();
    private examples.data.mutual.types.A _a;
  private int _v;

  /**
   * Constructor that builds a term rooted by hoo
   *
   * @return a term rooted by hoo
   */

  public static hoo make(examples.data.mutual.types.A _a, int _v) {

    // use the proto as a model
    gomProto.initHashCode( _a,  _v);
    return (hoo) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _a
   * @param _v
   * @param hashCode hashCode of hoo
   */
  private void init(examples.data.mutual.types.A _a, int _v, int hashCode) {
    this._a = _a;
    this._v = _v;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _a
   * @param _v
   */
  private void initHashCode(examples.data.mutual.types.A _a, int _v) {
    this._a = _a;
    this._v = _v;

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
    return "hoo";
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
    hoo clone = new hoo();
    clone.init( _a,  _v, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("hoo(");
    _a.toStringBuilder(buffer);
buffer.append(",");
    buffer.append(_v);

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
    examples.data.mutual.MutualAbstractType ao = (examples.data.mutual.MutualAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    hoo tco = (hoo) ao;
    int _aCmp = (this._a).compareToLPO(tco._a);
    if(_aCmp != 0) {
      return _aCmp;
    }

    if( this._v != tco._v) {
      return (this._v < tco._v)?-1:1;
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
    examples.data.mutual.MutualAbstractType ao = (examples.data.mutual.MutualAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    hoo tco = (hoo) ao;
    int _aCmp = (this._a).compareTo(tco._a);
    if(_aCmp != 0) {
      return _aCmp;
    }

    if( this._v != tco._v) {
      return (this._v < tco._v)?-1:1;
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
   * @return true if obj is a hoo and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof hoo) {

      hoo peer = (hoo) obj;
      return _a==peer._a && _v==peer._v && true;
    }
    return false;
  }


   //A interface
  /**
   * Returns true if the term is rooted by the symbol hoo
   *
   * @return true, because this is rooted by hoo
   */
  @Override
  public boolean ishoo() {
    return true;
  }
  
  /**
   * Returns the attribute examples.data.mutual.types.A
   *
   * @return the attribute examples.data.mutual.types.A
   */
  @Override
  public examples.data.mutual.types.A geta() {
    return _a;
  }

  /**
   * Sets and returns the attribute examples.data.mutual.types.A
   *
   * @param set_arg the argument to set
   * @return the attribute examples.data.mutual.types.A which just has been set
   */
  @Override
  public examples.data.mutual.types.A seta(examples.data.mutual.types.A set_arg) {
    return make(set_arg, _v);
  }
  
  /**
   * Returns the attribute int
   *
   * @return the attribute int
   */
  @Override
  public int getv() {
    return _v;
  }

  /**
   * Sets and returns the attribute examples.data.mutual.types.A
   *
   * @param set_arg the argument to set
   * @return the attribute int which just has been set
   */
  @Override
  public examples.data.mutual.types.A setv(int set_arg) {
    return make(_a, set_arg);
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
      new aterm.ATerm[] {geta().toATerm(), (aterm.ATerm) atermFactory.makeInt(getv())});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.data.mutual.types.A from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.data.mutual.types.A fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.data.mutual.types.A.fromTerm(appl.getArgument(0),atConv), convertATermToInt(appl.getArgument(1), atConv)
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
      case 0: return _a;
      case 1: return new tom.library.sl.VisitableBuiltin<java.lang.Integer>(_v);
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
      case 0: return make((examples.data.mutual.types.A) v, getv());
      case 1: return make(_a, getv());
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
    if (children.length == getChildCount()  && children[0] instanceof examples.data.mutual.types.A && children[1] instanceof tom.library.sl.VisitableBuiltin) {
      return make((examples.data.mutual.types.A) children[0], ((tom.library.sl.VisitableBuiltin<java.lang.Integer>)children[1]).getBuiltin());
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
    return new tom.library.sl.Visitable[] { _a,  new tom.library.sl.VisitableBuiltin<java.lang.Integer>(_v)};
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
    b = (-42209323<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_a.hashCode() << 8);
    a += (_v);

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.mutual.types.A>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.data.mutual.types.A>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.mutual.types.A>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.data.mutual.types.A>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.data.mutual.types.A>> apply(final tom.library.enumerator.Enumeration<examples.data.mutual.types.A> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.data.mutual.types.A>>() {
          public tom.library.enumerator.Enumeration<examples.data.mutual.types.A> apply(final tom.library.enumerator.Enumeration<java.lang.Integer> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.data.mutual.types.A,tom.library.enumerator.F<java.lang.Integer,examples.data.mutual.types.A>>) 
        new tom.library.enumerator.F<examples.data.mutual.types.A,tom.library.enumerator.F<java.lang.Integer,examples.data.mutual.types.A>>() {
          public tom.library.enumerator.F<java.lang.Integer,examples.data.mutual.types.A> apply(final examples.data.mutual.types.A t1) {
            return 
        new tom.library.enumerator.F<java.lang.Integer,examples.data.mutual.types.A>() {
          public examples.data.mutual.types.A apply(final java.lang.Integer t2) {
            return make(t1,java.lang.Integer.valueOf(t2));
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
