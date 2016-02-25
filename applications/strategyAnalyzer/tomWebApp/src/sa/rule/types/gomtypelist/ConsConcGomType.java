
package sa.rule.types.gomtypelist;



public final class ConsConcGomType extends sa.rule.types.gomtypelist.ConcGomType implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsConcGomType";


  private ConsConcGomType() {}
  private int hashCode;
  private static ConsConcGomType gomProto = new ConsConcGomType();
  
   private sa.rule.types.GomType[] children;
                  private sa.rule.types.GomType _HeadConcGomType;
  private sa.rule.types.GomTypeList _TailConcGomType;

  /**
   * Constructor that builds a term rooted by ConsConcGomType
   *
   * @return a term rooted by ConsConcGomType
   */

  public static ConsConcGomType make(sa.rule.types.GomType _HeadConcGomType, sa.rule.types.GomTypeList _TailConcGomType) {

    // use the proto as a model
    gomProto.initHashCode( _HeadConcGomType,  _TailConcGomType);
    return (ConsConcGomType) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcGomType
   * @param _TailConcGomType
   * @param hashCode hashCode of ConsConcGomType
   */
  private void init(sa.rule.types.GomType _HeadConcGomType, sa.rule.types.GomTypeList _TailConcGomType, int hashCode) {
    this._HeadConcGomType = _HeadConcGomType;
    this._TailConcGomType = _TailConcGomType;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcGomType
   * @param _TailConcGomType
   */
  private void initHashCode(sa.rule.types.GomType _HeadConcGomType, sa.rule.types.GomTypeList _TailConcGomType) {
    this._HeadConcGomType = _HeadConcGomType;
    this._TailConcGomType = _TailConcGomType;

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
    return "ConsConcGomType";
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
    ConsConcGomType clone = new ConsConcGomType();
    clone.init( _HeadConcGomType,  _TailConcGomType, hashCode);
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
    ConsConcGomType tco = (ConsConcGomType) ao;
    int _HeadConcGomTypeCmp = (this._HeadConcGomType).compareToLPO(tco._HeadConcGomType);
    if(_HeadConcGomTypeCmp != 0) {
      return _HeadConcGomTypeCmp;
    }

    int _TailConcGomTypeCmp = (this._TailConcGomType).compareToLPO(tco._TailConcGomType);
    if(_TailConcGomTypeCmp != 0) {
      return _TailConcGomTypeCmp;
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
    ConsConcGomType tco = (ConsConcGomType) ao;
    int _HeadConcGomTypeCmp = (this._HeadConcGomType).compareTo(tco._HeadConcGomType);
    if(_HeadConcGomTypeCmp != 0) {
      return _HeadConcGomTypeCmp;
    }

    int _TailConcGomTypeCmp = (this._TailConcGomType).compareTo(tco._TailConcGomType);
    if(_TailConcGomTypeCmp != 0) {
      return _TailConcGomTypeCmp;
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
   * @return true if obj is a ConsConcGomType and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsConcGomType) {

      ConsConcGomType peer = (ConsConcGomType) obj;
      return _HeadConcGomType==peer._HeadConcGomType && _TailConcGomType==peer._TailConcGomType && true;
    }
    return false;
  }


   //GomTypeList interface
  /**
   * Returns true if the term is rooted by the symbol ConsConcGomType
   *
   * @return true, because this is rooted by ConsConcGomType
   */
  @Override
  public boolean isConsConcGomType() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.GomType
   *
   * @return the attribute sa.rule.types.GomType
   */
  @Override
  public sa.rule.types.GomType getHeadConcGomType() {
    return _HeadConcGomType;
  }

  /**
   * Sets and returns the attribute sa.rule.types.GomTypeList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.GomType which just has been set
   */
  @Override
  public sa.rule.types.GomTypeList setHeadConcGomType(sa.rule.types.GomType set_arg) {
    return make(set_arg, _TailConcGomType);
  }
  
  /**
   * Returns the attribute sa.rule.types.GomTypeList
   *
   * @return the attribute sa.rule.types.GomTypeList
   */
  @Override
  public sa.rule.types.GomTypeList getTailConcGomType() {
    return _TailConcGomType;
  }

  /**
   * Sets and returns the attribute sa.rule.types.GomTypeList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.GomTypeList which just has been set
   */
  @Override
  public sa.rule.types.GomTypeList setTailConcGomType(sa.rule.types.GomTypeList set_arg) {
    return make(_HeadConcGomType, set_arg);
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
      sa.rule.types.GomType[] new_children = new sa.rule.types.GomType[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.GomType) children[i]);
      }
     new_children[index] = (sa.rule.types.GomType) v;
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
      
               sa.rule.types.GomType[] typed_children = new sa.rule.types.GomType[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.GomType) children[i];
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
          children = toArray(new sa.rule.types.GomType[]{});
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
    b = (1017297400<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadConcGomType.hashCode() << 8);
    a += (_TailConcGomType.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList>,tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList>,tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList>,tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.GomType> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList>,tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.GomTypeList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.GomType,tom.library.enumerator.F<sa.rule.types.GomTypeList,sa.rule.types.GomTypeList>>) 
        new tom.library.enumerator.F<sa.rule.types.GomType,tom.library.enumerator.F<sa.rule.types.GomTypeList,sa.rule.types.GomTypeList>>() {
          public tom.library.enumerator.F<sa.rule.types.GomTypeList,sa.rule.types.GomTypeList> apply(final sa.rule.types.GomType t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.GomTypeList,sa.rule.types.GomTypeList>() {
          public sa.rule.types.GomTypeList apply(final sa.rule.types.GomTypeList t2) {
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
