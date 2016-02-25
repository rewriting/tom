
package sa.rule.types.stratlist;



public final class ConsConcStrat extends sa.rule.types.stratlist.ConcStrat implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsConcStrat";


  private ConsConcStrat() {}
  private int hashCode;
  private static ConsConcStrat gomProto = new ConsConcStrat();
  
   private sa.rule.types.Strat[] children;
                  private sa.rule.types.Strat _HeadConcStrat;
  private sa.rule.types.StratList _TailConcStrat;

  /**
   * Constructor that builds a term rooted by ConsConcStrat
   *
   * @return a term rooted by ConsConcStrat
   */

  public static ConsConcStrat make(sa.rule.types.Strat _HeadConcStrat, sa.rule.types.StratList _TailConcStrat) {

    // use the proto as a model
    gomProto.initHashCode( _HeadConcStrat,  _TailConcStrat);
    return (ConsConcStrat) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcStrat
   * @param _TailConcStrat
   * @param hashCode hashCode of ConsConcStrat
   */
  private void init(sa.rule.types.Strat _HeadConcStrat, sa.rule.types.StratList _TailConcStrat, int hashCode) {
    this._HeadConcStrat = _HeadConcStrat;
    this._TailConcStrat = _TailConcStrat;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcStrat
   * @param _TailConcStrat
   */
  private void initHashCode(sa.rule.types.Strat _HeadConcStrat, sa.rule.types.StratList _TailConcStrat) {
    this._HeadConcStrat = _HeadConcStrat;
    this._TailConcStrat = _TailConcStrat;

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
    return "ConsConcStrat";
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
    ConsConcStrat clone = new ConsConcStrat();
    clone.init( _HeadConcStrat,  _TailConcStrat, hashCode);
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
    ConsConcStrat tco = (ConsConcStrat) ao;
    int _HeadConcStratCmp = (this._HeadConcStrat).compareToLPO(tco._HeadConcStrat);
    if(_HeadConcStratCmp != 0) {
      return _HeadConcStratCmp;
    }

    int _TailConcStratCmp = (this._TailConcStrat).compareToLPO(tco._TailConcStrat);
    if(_TailConcStratCmp != 0) {
      return _TailConcStratCmp;
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
    ConsConcStrat tco = (ConsConcStrat) ao;
    int _HeadConcStratCmp = (this._HeadConcStrat).compareTo(tco._HeadConcStrat);
    if(_HeadConcStratCmp != 0) {
      return _HeadConcStratCmp;
    }

    int _TailConcStratCmp = (this._TailConcStrat).compareTo(tco._TailConcStrat);
    if(_TailConcStratCmp != 0) {
      return _TailConcStratCmp;
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
   * @return true if obj is a ConsConcStrat and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsConcStrat) {

      ConsConcStrat peer = (ConsConcStrat) obj;
      return _HeadConcStrat==peer._HeadConcStrat && _TailConcStrat==peer._TailConcStrat && true;
    }
    return false;
  }


   //StratList interface
  /**
   * Returns true if the term is rooted by the symbol ConsConcStrat
   *
   * @return true, because this is rooted by ConsConcStrat
   */
  @Override
  public boolean isConsConcStrat() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Strat
   *
   * @return the attribute sa.rule.types.Strat
   */
  @Override
  public sa.rule.types.Strat getHeadConcStrat() {
    return _HeadConcStrat;
  }

  /**
   * Sets and returns the attribute sa.rule.types.StratList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Strat which just has been set
   */
  @Override
  public sa.rule.types.StratList setHeadConcStrat(sa.rule.types.Strat set_arg) {
    return make(set_arg, _TailConcStrat);
  }
  
  /**
   * Returns the attribute sa.rule.types.StratList
   *
   * @return the attribute sa.rule.types.StratList
   */
  @Override
  public sa.rule.types.StratList getTailConcStrat() {
    return _TailConcStrat;
  }

  /**
   * Sets and returns the attribute sa.rule.types.StratList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.StratList which just has been set
   */
  @Override
  public sa.rule.types.StratList setTailConcStrat(sa.rule.types.StratList set_arg) {
    return make(_HeadConcStrat, set_arg);
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
      sa.rule.types.Strat[] new_children = new sa.rule.types.Strat[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.Strat) children[i]);
      }
     new_children[index] = (sa.rule.types.Strat) v;
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
      
               sa.rule.types.Strat[] typed_children = new sa.rule.types.Strat[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.Strat) children[i];
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
          children = toArray(new sa.rule.types.Strat[]{});
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
    b = (-1931598671<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadConcStrat.hashCode() << 8);
    a += (_TailConcStrat.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratList>,tom.library.enumerator.Enumeration<sa.rule.types.StratList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratList>,tom.library.enumerator.Enumeration<sa.rule.types.StratList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratList>,tom.library.enumerator.Enumeration<sa.rule.types.StratList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Strat> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratList>,tom.library.enumerator.Enumeration<sa.rule.types.StratList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.StratList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.StratList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Strat,tom.library.enumerator.F<sa.rule.types.StratList,sa.rule.types.StratList>>) 
        new tom.library.enumerator.F<sa.rule.types.Strat,tom.library.enumerator.F<sa.rule.types.StratList,sa.rule.types.StratList>>() {
          public tom.library.enumerator.F<sa.rule.types.StratList,sa.rule.types.StratList> apply(final sa.rule.types.Strat t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.StratList,sa.rule.types.StratList>() {
          public sa.rule.types.StratList apply(final sa.rule.types.StratList t2) {
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
