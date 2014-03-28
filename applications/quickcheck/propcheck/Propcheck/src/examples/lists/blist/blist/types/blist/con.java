
package examples.lists.blist.blist.types.blist;



public final class con extends examples.lists.blist.blist.types.BList implements tom.library.sl.Visitable  {
  
  private static String symbolName = "con";


  private con() {}
  private int hashCode;
  private static con gomProto = new con();
    private examples.lists.blist.blist.types.Elem _head;
  private examples.lists.blist.blist.types.BList _tail;

  /**
   * Constructor that builds a term rooted by con
   *
   * @return a term rooted by con
   */

  public static con make(examples.lists.blist.blist.types.Elem _head, examples.lists.blist.blist.types.BList _tail) {

    // use the proto as a model
    gomProto.initHashCode( _head,  _tail);
    return (con) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _head
   * @param _tail
   * @param hashCode hashCode of con
   */
  private void init(examples.lists.blist.blist.types.Elem _head, examples.lists.blist.blist.types.BList _tail, int hashCode) {
    this._head = _head;
    this._tail = _tail;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _head
   * @param _tail
   */
  private void initHashCode(examples.lists.blist.blist.types.Elem _head, examples.lists.blist.blist.types.BList _tail) {
    this._head = _head;
    this._tail = _tail;

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
    return "con";
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
    con clone = new con();
    clone.init( _head,  _tail, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("con(");
    _head.toStringBuilder(buffer);
buffer.append(",");
    _tail.toStringBuilder(buffer);

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
    examples.lists.blist.blist.BListAbstractType ao = (examples.lists.blist.blist.BListAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    con tco = (con) ao;
    int _headCmp = (this._head).compareToLPO(tco._head);
    if(_headCmp != 0) {
      return _headCmp;
    }

    int _tailCmp = (this._tail).compareToLPO(tco._tail);
    if(_tailCmp != 0) {
      return _tailCmp;
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
    examples.lists.blist.blist.BListAbstractType ao = (examples.lists.blist.blist.BListAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    con tco = (con) ao;
    int _headCmp = (this._head).compareTo(tco._head);
    if(_headCmp != 0) {
      return _headCmp;
    }

    int _tailCmp = (this._tail).compareTo(tco._tail);
    if(_tailCmp != 0) {
      return _tailCmp;
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
   * @return true if obj is a con and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof con) {

      con peer = (con) obj;
      return _head==peer._head && _tail==peer._tail && true;
    }
    return false;
  }


   //BList interface
  /**
   * Returns true if the term is rooted by the symbol con
   *
   * @return true, because this is rooted by con
   */
  @Override
  public boolean iscon() {
    return true;
  }
  
  /**
   * Returns the attribute examples.lists.blist.blist.types.Elem
   *
   * @return the attribute examples.lists.blist.blist.types.Elem
   */
  @Override
  public examples.lists.blist.blist.types.Elem gethead() {
    return _head;
  }

  /**
   * Sets and returns the attribute examples.lists.blist.blist.types.BList
   *
   * @param set_arg the argument to set
   * @return the attribute examples.lists.blist.blist.types.Elem which just has been set
   */
  @Override
  public examples.lists.blist.blist.types.BList sethead(examples.lists.blist.blist.types.Elem set_arg) {
    return make(set_arg, _tail);
  }
  
  /**
   * Returns the attribute examples.lists.blist.blist.types.BList
   *
   * @return the attribute examples.lists.blist.blist.types.BList
   */
  @Override
  public examples.lists.blist.blist.types.BList gettail() {
    return _tail;
  }

  /**
   * Sets and returns the attribute examples.lists.blist.blist.types.BList
   *
   * @param set_arg the argument to set
   * @return the attribute examples.lists.blist.blist.types.BList which just has been set
   */
  @Override
  public examples.lists.blist.blist.types.BList settail(examples.lists.blist.blist.types.BList set_arg) {
    return make(_head, set_arg);
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
      new aterm.ATerm[] {gethead().toATerm(), gettail().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.lists.blist.blist.types.BList from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.lists.blist.blist.types.BList fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.lists.blist.blist.types.Elem.fromTerm(appl.getArgument(0),atConv), examples.lists.blist.blist.types.BList.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _head;
      case 1: return _tail;
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
      case 0: return make((examples.lists.blist.blist.types.Elem) v, _tail);
      case 1: return make(_head, (examples.lists.blist.blist.types.BList) v);
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
    if (children.length == getChildCount()  && children[0] instanceof examples.lists.blist.blist.types.Elem && children[1] instanceof examples.lists.blist.blist.types.BList) {
      return make((examples.lists.blist.blist.types.Elem) children[0], (examples.lists.blist.blist.types.BList) children[1]);
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
    return new tom.library.sl.Visitable[] { _head,  _tail};
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
    b = (-1677847396<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_head.hashCode() << 8);
    a += (_tail.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.Elem>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList>,tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.Elem>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList>,tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList>,tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList>> apply(final tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.Elem> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList>,tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList>>() {
          public tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList> apply(final tom.library.enumerator.Enumeration<examples.lists.blist.blist.types.BList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.lists.blist.blist.types.Elem,tom.library.enumerator.F<examples.lists.blist.blist.types.BList,examples.lists.blist.blist.types.BList>>) 
        new tom.library.enumerator.F<examples.lists.blist.blist.types.Elem,tom.library.enumerator.F<examples.lists.blist.blist.types.BList,examples.lists.blist.blist.types.BList>>() {
          public tom.library.enumerator.F<examples.lists.blist.blist.types.BList,examples.lists.blist.blist.types.BList> apply(final examples.lists.blist.blist.types.Elem t1) {
            return 
        new tom.library.enumerator.F<examples.lists.blist.blist.types.BList,examples.lists.blist.blist.types.BList>() {
          public examples.lists.blist.blist.types.BList apply(final examples.lists.blist.blist.types.BList t2) {
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
