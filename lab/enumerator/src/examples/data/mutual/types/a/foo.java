
package examples.data.mutual.types.a;



public final class foo extends examples.data.mutual.types.A implements tom.library.sl.Visitable  {
  
  private static String symbolName = "foo";


  private foo() {}
  private int hashCode;
  private static foo gomProto = new foo();
    private examples.data.mutual.types.B _b;

  /**
   * Constructor that builds a term rooted by foo
   *
   * @return a term rooted by foo
   */

  public static foo make(examples.data.mutual.types.B _b) {

    // use the proto as a model
    gomProto.initHashCode( _b);
    return (foo) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _b
   * @param hashCode hashCode of foo
   */
  private void init(examples.data.mutual.types.B _b, int hashCode) {
    this._b = _b;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _b
   */
  private void initHashCode(examples.data.mutual.types.B _b) {
    this._b = _b;

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
    return "foo";
  }

  /**
   * Returns the arity of the symbol
   *
   * @return the arity of the symbol
   */
  private int getArity() {
    return 1;
  }

  /**
   * Copy the object and returns the copy
   *
   * @return a clone of the SharedObject
   */
  public shared.SharedObject duplicate() {
    foo clone = new foo();
    clone.init( _b, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("foo(");
    _b.toStringBuilder(buffer);

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
    foo tco = (foo) ao;
    int _bCmp = (this._b).compareToLPO(tco._b);
    if(_bCmp != 0) {
      return _bCmp;
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
    foo tco = (foo) ao;
    int _bCmp = (this._b).compareTo(tco._b);
    if(_bCmp != 0) {
      return _bCmp;
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
   * @return true if obj is a foo and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof foo) {

      foo peer = (foo) obj;
      return _b==peer._b && true;
    }
    return false;
  }


   //A interface
  /**
   * Returns true if the term is rooted by the symbol foo
   *
   * @return true, because this is rooted by foo
   */
  @Override
  public boolean isfoo() {
    return true;
  }
  
  /**
   * Returns the attribute examples.data.mutual.types.B
   *
   * @return the attribute examples.data.mutual.types.B
   */
  @Override
  public examples.data.mutual.types.B getb() {
    return _b;
  }

  /**
   * Sets and returns the attribute examples.data.mutual.types.A
   *
   * @param set_arg the argument to set
   * @return the attribute examples.data.mutual.types.B which just has been set
   */
  @Override
  public examples.data.mutual.types.A setb(examples.data.mutual.types.B set_arg) {
    return make(set_arg);
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
      new aterm.ATerm[] {getb().toATerm()});
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
examples.data.mutual.types.B.fromTerm(appl.getArgument(0),atConv)
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
    return 1;
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
      case 0: return _b;
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
      case 0: return make((examples.data.mutual.types.B) v);
      default: throw new IndexOutOfBoundsException();
 }
  }

  /**
   * Set children to the term
   *
   * @param children array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "children" is different than 1
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    if (children.length == getChildCount()  && children[0] instanceof examples.data.mutual.types.B) {
      return make((examples.data.mutual.types.B) children[0]);
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
    return new tom.library.sl.Visitable[] { _b};
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
    b = (-1136406804<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_b.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.mutual.types.B>,tom.library.enumerator.Enumeration<examples.data.mutual.types.A>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.data.mutual.types.B>,tom.library.enumerator.Enumeration<examples.data.mutual.types.A>>() {
          public tom.library.enumerator.Enumeration<examples.data.mutual.types.A> apply(final tom.library.enumerator.Enumeration<examples.data.mutual.types.B> t1) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.data.mutual.types.B,examples.data.mutual.types.A>) 
        new tom.library.enumerator.F<examples.data.mutual.types.B,examples.data.mutual.types.A>() {
          public examples.data.mutual.types.A apply(final examples.data.mutual.types.B t1) {
            return make(t1);
          }
        }),t1).pay();
          }
        };
  }

}
