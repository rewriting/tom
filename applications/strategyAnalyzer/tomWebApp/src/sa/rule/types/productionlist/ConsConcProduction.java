
package sa.rule.types.productionlist;



public final class ConsConcProduction extends sa.rule.types.productionlist.ConcProduction implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsConcProduction";


  private ConsConcProduction() {}
  private int hashCode;
  private static ConsConcProduction gomProto = new ConsConcProduction();
  
   private sa.rule.types.Production[] children;
                  private sa.rule.types.Production _HeadConcProduction;
  private sa.rule.types.ProductionList _TailConcProduction;

  /**
   * Constructor that builds a term rooted by ConsConcProduction
   *
   * @return a term rooted by ConsConcProduction
   */

  public static ConsConcProduction make(sa.rule.types.Production _HeadConcProduction, sa.rule.types.ProductionList _TailConcProduction) {

    // use the proto as a model
    gomProto.initHashCode( _HeadConcProduction,  _TailConcProduction);
    return (ConsConcProduction) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcProduction
   * @param _TailConcProduction
   * @param hashCode hashCode of ConsConcProduction
   */
  private void init(sa.rule.types.Production _HeadConcProduction, sa.rule.types.ProductionList _TailConcProduction, int hashCode) {
    this._HeadConcProduction = _HeadConcProduction;
    this._TailConcProduction = _TailConcProduction;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcProduction
   * @param _TailConcProduction
   */
  private void initHashCode(sa.rule.types.Production _HeadConcProduction, sa.rule.types.ProductionList _TailConcProduction) {
    this._HeadConcProduction = _HeadConcProduction;
    this._TailConcProduction = _TailConcProduction;

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
    return "ConsConcProduction";
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
    ConsConcProduction clone = new ConsConcProduction();
    clone.init( _HeadConcProduction,  _TailConcProduction, hashCode);
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
    ConsConcProduction tco = (ConsConcProduction) ao;
    int _HeadConcProductionCmp = (this._HeadConcProduction).compareToLPO(tco._HeadConcProduction);
    if(_HeadConcProductionCmp != 0) {
      return _HeadConcProductionCmp;
    }

    int _TailConcProductionCmp = (this._TailConcProduction).compareToLPO(tco._TailConcProduction);
    if(_TailConcProductionCmp != 0) {
      return _TailConcProductionCmp;
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
    ConsConcProduction tco = (ConsConcProduction) ao;
    int _HeadConcProductionCmp = (this._HeadConcProduction).compareTo(tco._HeadConcProduction);
    if(_HeadConcProductionCmp != 0) {
      return _HeadConcProductionCmp;
    }

    int _TailConcProductionCmp = (this._TailConcProduction).compareTo(tco._TailConcProduction);
    if(_TailConcProductionCmp != 0) {
      return _TailConcProductionCmp;
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
   * @return true if obj is a ConsConcProduction and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsConcProduction) {

      ConsConcProduction peer = (ConsConcProduction) obj;
      return _HeadConcProduction==peer._HeadConcProduction && _TailConcProduction==peer._TailConcProduction && true;
    }
    return false;
  }


   //ProductionList interface
  /**
   * Returns true if the term is rooted by the symbol ConsConcProduction
   *
   * @return true, because this is rooted by ConsConcProduction
   */
  @Override
  public boolean isConsConcProduction() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Production
   *
   * @return the attribute sa.rule.types.Production
   */
  @Override
  public sa.rule.types.Production getHeadConcProduction() {
    return _HeadConcProduction;
  }

  /**
   * Sets and returns the attribute sa.rule.types.ProductionList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Production which just has been set
   */
  @Override
  public sa.rule.types.ProductionList setHeadConcProduction(sa.rule.types.Production set_arg) {
    return make(set_arg, _TailConcProduction);
  }
  
  /**
   * Returns the attribute sa.rule.types.ProductionList
   *
   * @return the attribute sa.rule.types.ProductionList
   */
  @Override
  public sa.rule.types.ProductionList getTailConcProduction() {
    return _TailConcProduction;
  }

  /**
   * Sets and returns the attribute sa.rule.types.ProductionList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.ProductionList which just has been set
   */
  @Override
  public sa.rule.types.ProductionList setTailConcProduction(sa.rule.types.ProductionList set_arg) {
    return make(_HeadConcProduction, set_arg);
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
      sa.rule.types.Production[] new_children = new sa.rule.types.Production[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.Production) children[i]);
      }
     new_children[index] = (sa.rule.types.Production) v;
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
      
               sa.rule.types.Production[] typed_children = new sa.rule.types.Production[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.Production) children[i];
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
          children = toArray(new sa.rule.types.Production[]{});
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
    b = (-2079144752<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadConcProduction.hashCode() << 8);
    a += (_TailConcProduction.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Production>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ProductionList>,tom.library.enumerator.Enumeration<sa.rule.types.ProductionList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Production>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ProductionList>,tom.library.enumerator.Enumeration<sa.rule.types.ProductionList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ProductionList>,tom.library.enumerator.Enumeration<sa.rule.types.ProductionList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Production> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ProductionList>,tom.library.enumerator.Enumeration<sa.rule.types.ProductionList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.ProductionList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.ProductionList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Production,tom.library.enumerator.F<sa.rule.types.ProductionList,sa.rule.types.ProductionList>>) 
        new tom.library.enumerator.F<sa.rule.types.Production,tom.library.enumerator.F<sa.rule.types.ProductionList,sa.rule.types.ProductionList>>() {
          public tom.library.enumerator.F<sa.rule.types.ProductionList,sa.rule.types.ProductionList> apply(final sa.rule.types.Production t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.ProductionList,sa.rule.types.ProductionList>() {
          public sa.rule.types.ProductionList apply(final sa.rule.types.ProductionList t2) {
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
