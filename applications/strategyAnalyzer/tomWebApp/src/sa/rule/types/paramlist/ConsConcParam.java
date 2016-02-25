
package sa.rule.types.paramlist;



public final class ConsConcParam extends sa.rule.types.paramlist.ConcParam implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsConcParam";


  private ConsConcParam() {}
  private int hashCode;
  private static ConsConcParam gomProto = new ConsConcParam();
  
   private sa.rule.types.Param[] children;
                  private sa.rule.types.Param _HeadConcParam;
  private sa.rule.types.ParamList _TailConcParam;

  /**
   * Constructor that builds a term rooted by ConsConcParam
   *
   * @return a term rooted by ConsConcParam
   */

  public static ConsConcParam make(sa.rule.types.Param _HeadConcParam, sa.rule.types.ParamList _TailConcParam) {

    // use the proto as a model
    gomProto.initHashCode( _HeadConcParam,  _TailConcParam);
    return (ConsConcParam) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcParam
   * @param _TailConcParam
   * @param hashCode hashCode of ConsConcParam
   */
  private void init(sa.rule.types.Param _HeadConcParam, sa.rule.types.ParamList _TailConcParam, int hashCode) {
    this._HeadConcParam = _HeadConcParam;
    this._TailConcParam = _TailConcParam;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcParam
   * @param _TailConcParam
   */
  private void initHashCode(sa.rule.types.Param _HeadConcParam, sa.rule.types.ParamList _TailConcParam) {
    this._HeadConcParam = _HeadConcParam;
    this._TailConcParam = _TailConcParam;

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
    return "ConsConcParam";
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
    ConsConcParam clone = new ConsConcParam();
    clone.init( _HeadConcParam,  _TailConcParam, hashCode);
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
    ConsConcParam tco = (ConsConcParam) ao;
    int _HeadConcParamCmp = (this._HeadConcParam).compareToLPO(tco._HeadConcParam);
    if(_HeadConcParamCmp != 0) {
      return _HeadConcParamCmp;
    }

    int _TailConcParamCmp = (this._TailConcParam).compareToLPO(tco._TailConcParam);
    if(_TailConcParamCmp != 0) {
      return _TailConcParamCmp;
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
    ConsConcParam tco = (ConsConcParam) ao;
    int _HeadConcParamCmp = (this._HeadConcParam).compareTo(tco._HeadConcParam);
    if(_HeadConcParamCmp != 0) {
      return _HeadConcParamCmp;
    }

    int _TailConcParamCmp = (this._TailConcParam).compareTo(tco._TailConcParam);
    if(_TailConcParamCmp != 0) {
      return _TailConcParamCmp;
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
   * @return true if obj is a ConsConcParam and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsConcParam) {

      ConsConcParam peer = (ConsConcParam) obj;
      return _HeadConcParam==peer._HeadConcParam && _TailConcParam==peer._TailConcParam && true;
    }
    return false;
  }


   //ParamList interface
  /**
   * Returns true if the term is rooted by the symbol ConsConcParam
   *
   * @return true, because this is rooted by ConsConcParam
   */
  @Override
  public boolean isConsConcParam() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Param
   *
   * @return the attribute sa.rule.types.Param
   */
  @Override
  public sa.rule.types.Param getHeadConcParam() {
    return _HeadConcParam;
  }

  /**
   * Sets and returns the attribute sa.rule.types.ParamList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Param which just has been set
   */
  @Override
  public sa.rule.types.ParamList setHeadConcParam(sa.rule.types.Param set_arg) {
    return make(set_arg, _TailConcParam);
  }
  
  /**
   * Returns the attribute sa.rule.types.ParamList
   *
   * @return the attribute sa.rule.types.ParamList
   */
  @Override
  public sa.rule.types.ParamList getTailConcParam() {
    return _TailConcParam;
  }

  /**
   * Sets and returns the attribute sa.rule.types.ParamList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.ParamList which just has been set
   */
  @Override
  public sa.rule.types.ParamList setTailConcParam(sa.rule.types.ParamList set_arg) {
    return make(_HeadConcParam, set_arg);
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
      sa.rule.types.Param[] new_children = new sa.rule.types.Param[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.Param) children[i]);
      }
     new_children[index] = (sa.rule.types.Param) v;
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
      
               sa.rule.types.Param[] typed_children = new sa.rule.types.Param[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.Param) children[i];
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
          children = toArray(new sa.rule.types.Param[]{});
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
    b = (-47953769<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadConcParam.hashCode() << 8);
    a += (_TailConcParam.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Param>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ParamList>,tom.library.enumerator.Enumeration<sa.rule.types.ParamList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Param>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ParamList>,tom.library.enumerator.Enumeration<sa.rule.types.ParamList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ParamList>,tom.library.enumerator.Enumeration<sa.rule.types.ParamList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Param> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ParamList>,tom.library.enumerator.Enumeration<sa.rule.types.ParamList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.ParamList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.ParamList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Param,tom.library.enumerator.F<sa.rule.types.ParamList,sa.rule.types.ParamList>>) 
        new tom.library.enumerator.F<sa.rule.types.Param,tom.library.enumerator.F<sa.rule.types.ParamList,sa.rule.types.ParamList>>() {
          public tom.library.enumerator.F<sa.rule.types.ParamList,sa.rule.types.ParamList> apply(final sa.rule.types.Param t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.ParamList,sa.rule.types.ParamList>() {
          public sa.rule.types.ParamList apply(final sa.rule.types.ParamList t2) {
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
