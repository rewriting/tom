
package examples.queues.queue.types.queue;



public final class add extends examples.queues.queue.types.Queue implements tom.library.sl.Visitable  {
  
  private static String symbolName = "add";


  private add() {}
  private int hashCode;
  private static add gomProto = new add();
    private examples.queues.queue.types.Elem _e;
  private examples.queues.queue.types.Queue _q;

  /**
   * Constructor that builds a term rooted by add
   *
   * @return a term rooted by add
   */

  public static add make(examples.queues.queue.types.Elem _e, examples.queues.queue.types.Queue _q) {

    // use the proto as a model
    gomProto.initHashCode( _e,  _q);
    return (add) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _e
   * @param _q
   * @param hashCode hashCode of add
   */
  private void init(examples.queues.queue.types.Elem _e, examples.queues.queue.types.Queue _q, int hashCode) {
    this._e = _e;
    this._q = _q;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _e
   * @param _q
   */
  private void initHashCode(examples.queues.queue.types.Elem _e, examples.queues.queue.types.Queue _q) {
    this._e = _e;
    this._q = _q;

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
    return "add";
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
    add clone = new add();
    clone.init( _e,  _q, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("add(");
    _e.toStringBuilder(buffer);
buffer.append(",");
    _q.toStringBuilder(buffer);

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
    examples.queues.queue.QueueAbstractType ao = (examples.queues.queue.QueueAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    add tco = (add) ao;
    int _eCmp = (this._e).compareToLPO(tco._e);
    if(_eCmp != 0) {
      return _eCmp;
    }

    int _qCmp = (this._q).compareToLPO(tco._q);
    if(_qCmp != 0) {
      return _qCmp;
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
    examples.queues.queue.QueueAbstractType ao = (examples.queues.queue.QueueAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    add tco = (add) ao;
    int _eCmp = (this._e).compareTo(tco._e);
    if(_eCmp != 0) {
      return _eCmp;
    }

    int _qCmp = (this._q).compareTo(tco._q);
    if(_qCmp != 0) {
      return _qCmp;
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
   * @return true if obj is a add and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof add) {

      add peer = (add) obj;
      return _e==peer._e && _q==peer._q && true;
    }
    return false;
  }


   //Queue interface
  /**
   * Returns true if the term is rooted by the symbol add
   *
   * @return true, because this is rooted by add
   */
  @Override
  public boolean isadd() {
    return true;
  }
  
  /**
   * Returns the attribute examples.queues.queue.types.Elem
   *
   * @return the attribute examples.queues.queue.types.Elem
   */
  @Override
  public examples.queues.queue.types.Elem gete() {
    return _e;
  }

  /**
   * Sets and returns the attribute examples.queues.queue.types.Queue
   *
   * @param set_arg the argument to set
   * @return the attribute examples.queues.queue.types.Elem which just has been set
   */
  @Override
  public examples.queues.queue.types.Queue sete(examples.queues.queue.types.Elem set_arg) {
    return make(set_arg, _q);
  }
  
  /**
   * Returns the attribute examples.queues.queue.types.Queue
   *
   * @return the attribute examples.queues.queue.types.Queue
   */
  @Override
  public examples.queues.queue.types.Queue getq() {
    return _q;
  }

  /**
   * Sets and returns the attribute examples.queues.queue.types.Queue
   *
   * @param set_arg the argument to set
   * @return the attribute examples.queues.queue.types.Queue which just has been set
   */
  @Override
  public examples.queues.queue.types.Queue setq(examples.queues.queue.types.Queue set_arg) {
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
      new aterm.ATerm[] {gete().toATerm(), getq().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.queues.queue.types.Queue from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.queues.queue.types.Queue fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.queues.queue.types.Elem.fromTerm(appl.getArgument(0),atConv), examples.queues.queue.types.Queue.fromTerm(appl.getArgument(1),atConv)
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
      case 1: return _q;
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
      case 0: return make((examples.queues.queue.types.Elem) v, _q);
      case 1: return make(_e, (examples.queues.queue.types.Queue) v);
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
    if (children.length == getChildCount()  && children[0] instanceof examples.queues.queue.types.Elem && children[1] instanceof examples.queues.queue.types.Queue) {
      return make((examples.queues.queue.types.Elem) children[0], (examples.queues.queue.types.Queue) children[1]);
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
    return new tom.library.sl.Visitable[] { _e,  _q};
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
    b = (-1308154563<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_e.hashCode() << 8);
    a += (_q.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.queues.queue.types.Elem>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue>,tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.queues.queue.types.Elem>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue>,tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue>,tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue>> apply(final tom.library.enumerator.Enumeration<examples.queues.queue.types.Elem> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue>,tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue>>() {
          public tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue> apply(final tom.library.enumerator.Enumeration<examples.queues.queue.types.Queue> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.queues.queue.types.Elem,tom.library.enumerator.F<examples.queues.queue.types.Queue,examples.queues.queue.types.Queue>>) 
        new tom.library.enumerator.F<examples.queues.queue.types.Elem,tom.library.enumerator.F<examples.queues.queue.types.Queue,examples.queues.queue.types.Queue>>() {
          public tom.library.enumerator.F<examples.queues.queue.types.Queue,examples.queues.queue.types.Queue> apply(final examples.queues.queue.types.Elem t1) {
            return 
        new tom.library.enumerator.F<examples.queues.queue.types.Queue,examples.queues.queue.types.Queue>() {
          public examples.queues.queue.types.Queue apply(final examples.queues.queue.types.Queue t2) {
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
