
package examples.parser.rec.types.stm;



public final class ConsSeq extends examples.parser.rec.types.stm.Seq implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsSeq";


  private ConsSeq() {}
  private int hashCode;
  private static ConsSeq gomProto = new ConsSeq();
  
   private examples.parser.rec.types.Stm[] children;
                  private examples.parser.rec.types.Stm _HeadSeq;
  private examples.parser.rec.types.Stm _TailSeq;

  /**
   * Constructor that builds a term rooted by ConsSeq
   *
   * @return a term rooted by ConsSeq
   */

    public static examples.parser.rec.types.Stm make(examples.parser.rec.types.Stm head, examples.parser.rec.types.Stm tail) {
  if (true) {if (head.isEmptySeq()) { return tail; }
if (head.isConsSeq()) { return make(head.getHeadSeq(),make(head.getTailSeq(),tail)); }
if (!tail.isConsSeq() && !tail.isEmptySeq()) { return make(head,make(tail,EmptySeq.make())); }
}
      return realMake( head,  tail);
    }
  
  private static ConsSeq realMake(examples.parser.rec.types.Stm _HeadSeq, examples.parser.rec.types.Stm _TailSeq) {

    // use the proto as a model
    gomProto.initHashCode( _HeadSeq,  _TailSeq);
    return (ConsSeq) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadSeq
   * @param _TailSeq
   * @param hashCode hashCode of ConsSeq
   */
  private void init(examples.parser.rec.types.Stm _HeadSeq, examples.parser.rec.types.Stm _TailSeq, int hashCode) {
    this._HeadSeq = _HeadSeq;
    this._TailSeq = _TailSeq;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadSeq
   * @param _TailSeq
   */
  private void initHashCode(examples.parser.rec.types.Stm _HeadSeq, examples.parser.rec.types.Stm _TailSeq) {
    this._HeadSeq = _HeadSeq;
    this._TailSeq = _TailSeq;

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
    return "ConsSeq";
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
    ConsSeq clone = new ConsSeq();
    clone.init( _HeadSeq,  _TailSeq, hashCode);
    return clone;
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
    examples.parser.rec.RecAbstractType ao = (examples.parser.rec.RecAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    ConsSeq tco = (ConsSeq) ao;
    int _HeadSeqCmp = (this._HeadSeq).compareToLPO(tco._HeadSeq);
    if(_HeadSeqCmp != 0) {
      return _HeadSeqCmp;
    }

    int _TailSeqCmp = (this._TailSeq).compareToLPO(tco._TailSeq);
    if(_TailSeqCmp != 0) {
      return _TailSeqCmp;
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
    examples.parser.rec.RecAbstractType ao = (examples.parser.rec.RecAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    ConsSeq tco = (ConsSeq) ao;
    int _HeadSeqCmp = (this._HeadSeq).compareTo(tco._HeadSeq);
    if(_HeadSeqCmp != 0) {
      return _HeadSeqCmp;
    }

    int _TailSeqCmp = (this._TailSeq).compareTo(tco._TailSeq);
    if(_TailSeqCmp != 0) {
      return _TailSeqCmp;
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
   * @return true if obj is a ConsSeq and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsSeq) {

      ConsSeq peer = (ConsSeq) obj;
      return _HeadSeq==peer._HeadSeq && _TailSeq==peer._TailSeq && true;
    }
    return false;
  }


   //Stm interface
  /**
   * Returns true if the term is rooted by the symbol ConsSeq
   *
   * @return true, because this is rooted by ConsSeq
   */
  @Override
  public boolean isConsSeq() {
    return true;
  }
  
  /**
   * Returns the attribute parser.rec.types.Stm
   *
   * @return the attribute parser.rec.types.Stm
   */
  @Override
  public examples.parser.rec.types.Stm getHeadSeq() {
    return _HeadSeq;
  }

  /**
   * Sets and returns the attribute parser.rec.types.Stm
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.Stm which just has been set
   */
  @Override
  public examples.parser.rec.types.Stm setHeadSeq(examples.parser.rec.types.Stm set_arg) {
    return make(set_arg, _TailSeq);
  }
  
  /**
   * Returns the attribute parser.rec.types.Stm
   *
   * @return the attribute parser.rec.types.Stm
   */
  @Override
  public examples.parser.rec.types.Stm getTailSeq() {
    return _TailSeq;
  }

  /**
   * Sets and returns the attribute parser.rec.types.Stm
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.Stm which just has been set
   */
  @Override
  public examples.parser.rec.types.Stm setTailSeq(examples.parser.rec.types.Stm set_arg) {
    return make(_HeadSeq, set_arg);
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
      new aterm.ATerm[] {getHeadSeq().toATerm(), getTailSeq().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Stm from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Stm fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.parser.rec.types.Stm.fromTerm(appl.getArgument(0),atConv), examples.parser.rec.types.Stm.fromTerm(appl.getArgument(1),atConv)
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
    return getChildren().length;
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
    return getChildren()[index];
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
    
      tom.library.sl.Visitable[] children = getChildren();
      examples.parser.rec.types.Stm[] new_children = new examples.parser.rec.types.Stm[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((examples.parser.rec.types.Stm) children[i]); 
      }
     new_children[index] = (examples.parser.rec.types.Stm) v;
     return fromArray(new_children);
                  
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
    if (children.length == getChildCount() ) {
      
               examples.parser.rec.types.Stm[] typed_children = new examples.parser.rec.types.Stm[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (examples.parser.rec.types.Stm) children[i]; 
              }
              return fromArray(typed_children);
              
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
    
        if (children == null) {
          children = toArray(new examples.parser.rec.types.Stm[]{});
        }
        return java.util.Arrays.copyOf(children,children.length);
      
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
    b = (-419279007<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadSeq.hashCode() << 8);
    a += (_TailSeq.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>>() {
          public tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.parser.rec.types.Stm,tom.library.enumerator.F<examples.parser.rec.types.Stm,examples.parser.rec.types.Stm>>) 
        new tom.library.enumerator.F<examples.parser.rec.types.Stm,tom.library.enumerator.F<examples.parser.rec.types.Stm,examples.parser.rec.types.Stm>>() {
          public tom.library.enumerator.F<examples.parser.rec.types.Stm,examples.parser.rec.types.Stm> apply(final examples.parser.rec.types.Stm t1) {
            return 
        new tom.library.enumerator.F<examples.parser.rec.types.Stm,examples.parser.rec.types.Stm>() {
          public examples.parser.rec.types.Stm apply(final examples.parser.rec.types.Stm t2) {
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
