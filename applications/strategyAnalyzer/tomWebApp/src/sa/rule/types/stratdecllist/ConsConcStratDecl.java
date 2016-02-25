
package sa.rule.types.stratdecllist;



public final class ConsConcStratDecl extends sa.rule.types.stratdecllist.ConcStratDecl implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsConcStratDecl";


  private ConsConcStratDecl() {}
  private int hashCode;
  private static ConsConcStratDecl gomProto = new ConsConcStratDecl();
  
   private sa.rule.types.StratDecl[] children;
                  private sa.rule.types.StratDecl _HeadConcStratDecl;
  private sa.rule.types.StratDeclList _TailConcStratDecl;

  /**
   * Constructor that builds a term rooted by ConsConcStratDecl
   *
   * @return a term rooted by ConsConcStratDecl
   */

  public static ConsConcStratDecl make(sa.rule.types.StratDecl _HeadConcStratDecl, sa.rule.types.StratDeclList _TailConcStratDecl) {

    // use the proto as a model
    gomProto.initHashCode( _HeadConcStratDecl,  _TailConcStratDecl);
    return (ConsConcStratDecl) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcStratDecl
   * @param _TailConcStratDecl
   * @param hashCode hashCode of ConsConcStratDecl
   */
  private void init(sa.rule.types.StratDecl _HeadConcStratDecl, sa.rule.types.StratDeclList _TailConcStratDecl, int hashCode) {
    this._HeadConcStratDecl = _HeadConcStratDecl;
    this._TailConcStratDecl = _TailConcStratDecl;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcStratDecl
   * @param _TailConcStratDecl
   */
  private void initHashCode(sa.rule.types.StratDecl _HeadConcStratDecl, sa.rule.types.StratDeclList _TailConcStratDecl) {
    this._HeadConcStratDecl = _HeadConcStratDecl;
    this._TailConcStratDecl = _TailConcStratDecl;

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
    return "ConsConcStratDecl";
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
    ConsConcStratDecl clone = new ConsConcStratDecl();
    clone.init( _HeadConcStratDecl,  _TailConcStratDecl, hashCode);
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
    ConsConcStratDecl tco = (ConsConcStratDecl) ao;
    int _HeadConcStratDeclCmp = (this._HeadConcStratDecl).compareToLPO(tco._HeadConcStratDecl);
    if(_HeadConcStratDeclCmp != 0) {
      return _HeadConcStratDeclCmp;
    }

    int _TailConcStratDeclCmp = (this._TailConcStratDecl).compareToLPO(tco._TailConcStratDecl);
    if(_TailConcStratDeclCmp != 0) {
      return _TailConcStratDeclCmp;
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
    ConsConcStratDecl tco = (ConsConcStratDecl) ao;
    int _HeadConcStratDeclCmp = (this._HeadConcStratDecl).compareTo(tco._HeadConcStratDecl);
    if(_HeadConcStratDeclCmp != 0) {
      return _HeadConcStratDeclCmp;
    }

    int _TailConcStratDeclCmp = (this._TailConcStratDecl).compareTo(tco._TailConcStratDecl);
    if(_TailConcStratDeclCmp != 0) {
      return _TailConcStratDeclCmp;
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
   * @return true if obj is a ConsConcStratDecl and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsConcStratDecl) {

      ConsConcStratDecl peer = (ConsConcStratDecl) obj;
      return _HeadConcStratDecl==peer._HeadConcStratDecl && _TailConcStratDecl==peer._TailConcStratDecl && true;
    }
    return false;
  }


   //StratDeclList interface
  /**
   * Returns true if the term is rooted by the symbol ConsConcStratDecl
   *
   * @return true, because this is rooted by ConsConcStratDecl
   */
  @Override
  public boolean isConsConcStratDecl() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.StratDecl
   *
   * @return the attribute sa.rule.types.StratDecl
   */
  @Override
  public sa.rule.types.StratDecl getHeadConcStratDecl() {
    return _HeadConcStratDecl;
  }

  /**
   * Sets and returns the attribute sa.rule.types.StratDeclList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.StratDecl which just has been set
   */
  @Override
  public sa.rule.types.StratDeclList setHeadConcStratDecl(sa.rule.types.StratDecl set_arg) {
    return make(set_arg, _TailConcStratDecl);
  }
  
  /**
   * Returns the attribute sa.rule.types.StratDeclList
   *
   * @return the attribute sa.rule.types.StratDeclList
   */
  @Override
  public sa.rule.types.StratDeclList getTailConcStratDecl() {
    return _TailConcStratDecl;
  }

  /**
   * Sets and returns the attribute sa.rule.types.StratDeclList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.StratDeclList which just has been set
   */
  @Override
  public sa.rule.types.StratDeclList setTailConcStratDecl(sa.rule.types.StratDeclList set_arg) {
    return make(_HeadConcStratDecl, set_arg);
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
      sa.rule.types.StratDecl[] new_children = new sa.rule.types.StratDecl[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.StratDecl) children[i]);
      }
     new_children[index] = (sa.rule.types.StratDecl) v;
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
      
               sa.rule.types.StratDecl[] typed_children = new sa.rule.types.StratDecl[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.StratDecl) children[i];
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
          children = toArray(new sa.rule.types.StratDecl[]{});
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
    b = (1675428038<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadConcStratDecl.hashCode() << 8);
    a += (_TailConcStratDecl.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratDecl>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList>,tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratDecl>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList>,tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList>,tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.StratDecl> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList>,tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.StratDeclList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.StratDecl,tom.library.enumerator.F<sa.rule.types.StratDeclList,sa.rule.types.StratDeclList>>) 
        new tom.library.enumerator.F<sa.rule.types.StratDecl,tom.library.enumerator.F<sa.rule.types.StratDeclList,sa.rule.types.StratDeclList>>() {
          public tom.library.enumerator.F<sa.rule.types.StratDeclList,sa.rule.types.StratDeclList> apply(final sa.rule.types.StratDecl t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.StratDeclList,sa.rule.types.StratDeclList>() {
          public sa.rule.types.StratDeclList apply(final sa.rule.types.StratDeclList t2) {
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
