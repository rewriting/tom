
package sa.rule.types.termlist;



public final class ConsTermList extends sa.rule.types.termlist.TermList implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsTermList";


  private ConsTermList() {}
  private int hashCode;
  private static ConsTermList gomProto = new ConsTermList();
  
   private sa.rule.types.Term[] children;
                  private sa.rule.types.Term _HeadTermList;
  private sa.rule.types.TermList _TailTermList;

  /**
   * Constructor that builds a term rooted by ConsTermList
   *
   * @return a term rooted by ConsTermList
   */

  public static ConsTermList make(sa.rule.types.Term _HeadTermList, sa.rule.types.TermList _TailTermList) {

    // use the proto as a model
    gomProto.initHashCode( _HeadTermList,  _TailTermList);
    return (ConsTermList) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadTermList
   * @param _TailTermList
   * @param hashCode hashCode of ConsTermList
   */
  private void init(sa.rule.types.Term _HeadTermList, sa.rule.types.TermList _TailTermList, int hashCode) {
    this._HeadTermList = _HeadTermList;
    this._TailTermList = _TailTermList;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadTermList
   * @param _TailTermList
   */
  private void initHashCode(sa.rule.types.Term _HeadTermList, sa.rule.types.TermList _TailTermList) {
    this._HeadTermList = _HeadTermList;
    this._TailTermList = _TailTermList;

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
    return "ConsTermList";
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
    ConsTermList clone = new ConsTermList();
    clone.init( _HeadTermList,  _TailTermList, hashCode);
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
    sa.rule.RuleAbstractType ao = (sa.rule.RuleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    ConsTermList tco = (ConsTermList) ao;
    int _HeadTermListCmp = (this._HeadTermList).compareToLPO(tco._HeadTermList);
    if(_HeadTermListCmp != 0) {
      return _HeadTermListCmp;
    }

    int _TailTermListCmp = (this._TailTermList).compareToLPO(tco._TailTermList);
    if(_TailTermListCmp != 0) {
      return _TailTermListCmp;
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
    sa.rule.RuleAbstractType ao = (sa.rule.RuleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    ConsTermList tco = (ConsTermList) ao;
    int _HeadTermListCmp = (this._HeadTermList).compareTo(tco._HeadTermList);
    if(_HeadTermListCmp != 0) {
      return _HeadTermListCmp;
    }

    int _TailTermListCmp = (this._TailTermList).compareTo(tco._TailTermList);
    if(_TailTermListCmp != 0) {
      return _TailTermListCmp;
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
   * @return true if obj is a ConsTermList and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsTermList) {

      ConsTermList peer = (ConsTermList) obj;
      return _HeadTermList==peer._HeadTermList && _TailTermList==peer._TailTermList && true;
    }
    return false;
  }


   //TermList interface
  /**
   * Returns true if the term is rooted by the symbol ConsTermList
   *
   * @return true, because this is rooted by ConsTermList
   */
  @Override
  public boolean isConsTermList() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Term
   *
   * @return the attribute sa.rule.types.Term
   */
  @Override
  public sa.rule.types.Term getHeadTermList() {
    return _HeadTermList;
  }

  /**
   * Sets and returns the attribute sa.rule.types.TermList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Term which just has been set
   */
  @Override
  public sa.rule.types.TermList setHeadTermList(sa.rule.types.Term set_arg) {
    return make(set_arg, _TailTermList);
  }
  
  /**
   * Returns the attribute sa.rule.types.TermList
   *
   * @return the attribute sa.rule.types.TermList
   */
  @Override
  public sa.rule.types.TermList getTailTermList() {
    return _TailTermList;
  }

  /**
   * Sets and returns the attribute sa.rule.types.TermList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.TermList which just has been set
   */
  @Override
  public sa.rule.types.TermList setTailTermList(sa.rule.types.TermList set_arg) {
    return make(_HeadTermList, set_arg);
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
      sa.rule.types.Term[] new_children = new sa.rule.types.Term[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.Term) children[i]);
      }
     new_children[index] = (sa.rule.types.Term) v;
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
      
               sa.rule.types.Term[] typed_children = new sa.rule.types.Term[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.Term) children[i];
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
          children = toArray(new sa.rule.types.Term[]{});
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
    b = (-1681314835<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadTermList.hashCode() << 8);
    a += (_TailTermList.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TermList>,tom.library.enumerator.Enumeration<sa.rule.types.TermList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Term>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TermList>,tom.library.enumerator.Enumeration<sa.rule.types.TermList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TermList>,tom.library.enumerator.Enumeration<sa.rule.types.TermList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Term> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TermList>,tom.library.enumerator.Enumeration<sa.rule.types.TermList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.TermList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.TermList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Term,tom.library.enumerator.F<sa.rule.types.TermList,sa.rule.types.TermList>>) 
        new tom.library.enumerator.F<sa.rule.types.Term,tom.library.enumerator.F<sa.rule.types.TermList,sa.rule.types.TermList>>() {
          public tom.library.enumerator.F<sa.rule.types.TermList,sa.rule.types.TermList> apply(final sa.rule.types.Term t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.TermList,sa.rule.types.TermList>() {
          public sa.rule.types.TermList apply(final sa.rule.types.TermList t2) {
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
