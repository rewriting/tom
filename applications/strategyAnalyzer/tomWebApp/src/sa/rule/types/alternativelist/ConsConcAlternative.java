
package sa.rule.types.alternativelist;



public final class ConsConcAlternative extends sa.rule.types.alternativelist.ConcAlternative implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsConcAlternative";


  private ConsConcAlternative() {}
  private int hashCode;
  private static ConsConcAlternative gomProto = new ConsConcAlternative();
  
   private sa.rule.types.Alternative[] children;
                  private sa.rule.types.Alternative _HeadConcAlternative;
  private sa.rule.types.AlternativeList _TailConcAlternative;

  /**
   * Constructor that builds a term rooted by ConsConcAlternative
   *
   * @return a term rooted by ConsConcAlternative
   */

  public static ConsConcAlternative make(sa.rule.types.Alternative _HeadConcAlternative, sa.rule.types.AlternativeList _TailConcAlternative) {

    // use the proto as a model
    gomProto.initHashCode( _HeadConcAlternative,  _TailConcAlternative);
    return (ConsConcAlternative) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcAlternative
   * @param _TailConcAlternative
   * @param hashCode hashCode of ConsConcAlternative
   */
  private void init(sa.rule.types.Alternative _HeadConcAlternative, sa.rule.types.AlternativeList _TailConcAlternative, int hashCode) {
    this._HeadConcAlternative = _HeadConcAlternative;
    this._TailConcAlternative = _TailConcAlternative;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcAlternative
   * @param _TailConcAlternative
   */
  private void initHashCode(sa.rule.types.Alternative _HeadConcAlternative, sa.rule.types.AlternativeList _TailConcAlternative) {
    this._HeadConcAlternative = _HeadConcAlternative;
    this._TailConcAlternative = _TailConcAlternative;

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
    return "ConsConcAlternative";
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
    ConsConcAlternative clone = new ConsConcAlternative();
    clone.init( _HeadConcAlternative,  _TailConcAlternative, hashCode);
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
    ConsConcAlternative tco = (ConsConcAlternative) ao;
    int _HeadConcAlternativeCmp = (this._HeadConcAlternative).compareToLPO(tco._HeadConcAlternative);
    if(_HeadConcAlternativeCmp != 0) {
      return _HeadConcAlternativeCmp;
    }

    int _TailConcAlternativeCmp = (this._TailConcAlternative).compareToLPO(tco._TailConcAlternative);
    if(_TailConcAlternativeCmp != 0) {
      return _TailConcAlternativeCmp;
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
    ConsConcAlternative tco = (ConsConcAlternative) ao;
    int _HeadConcAlternativeCmp = (this._HeadConcAlternative).compareTo(tco._HeadConcAlternative);
    if(_HeadConcAlternativeCmp != 0) {
      return _HeadConcAlternativeCmp;
    }

    int _TailConcAlternativeCmp = (this._TailConcAlternative).compareTo(tco._TailConcAlternative);
    if(_TailConcAlternativeCmp != 0) {
      return _TailConcAlternativeCmp;
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
   * @return true if obj is a ConsConcAlternative and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsConcAlternative) {

      ConsConcAlternative peer = (ConsConcAlternative) obj;
      return _HeadConcAlternative==peer._HeadConcAlternative && _TailConcAlternative==peer._TailConcAlternative && true;
    }
    return false;
  }


   //AlternativeList interface
  /**
   * Returns true if the term is rooted by the symbol ConsConcAlternative
   *
   * @return true, because this is rooted by ConsConcAlternative
   */
  @Override
  public boolean isConsConcAlternative() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Alternative
   *
   * @return the attribute sa.rule.types.Alternative
   */
  @Override
  public sa.rule.types.Alternative getHeadConcAlternative() {
    return _HeadConcAlternative;
  }

  /**
   * Sets and returns the attribute sa.rule.types.AlternativeList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Alternative which just has been set
   */
  @Override
  public sa.rule.types.AlternativeList setHeadConcAlternative(sa.rule.types.Alternative set_arg) {
    return make(set_arg, _TailConcAlternative);
  }
  
  /**
   * Returns the attribute sa.rule.types.AlternativeList
   *
   * @return the attribute sa.rule.types.AlternativeList
   */
  @Override
  public sa.rule.types.AlternativeList getTailConcAlternative() {
    return _TailConcAlternative;
  }

  /**
   * Sets and returns the attribute sa.rule.types.AlternativeList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.AlternativeList which just has been set
   */
  @Override
  public sa.rule.types.AlternativeList setTailConcAlternative(sa.rule.types.AlternativeList set_arg) {
    return make(_HeadConcAlternative, set_arg);
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
      sa.rule.types.Alternative[] new_children = new sa.rule.types.Alternative[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.Alternative) children[i]);
      }
     new_children[index] = (sa.rule.types.Alternative) v;
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
      
               sa.rule.types.Alternative[] typed_children = new sa.rule.types.Alternative[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.Alternative) children[i];
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
          children = toArray(new sa.rule.types.Alternative[]{});
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
    b = (-1386685857<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadConcAlternative.hashCode() << 8);
    a += (_TailConcAlternative.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Alternative>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>,tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Alternative>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>,tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>,tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Alternative> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>,tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.AlternativeList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Alternative,tom.library.enumerator.F<sa.rule.types.AlternativeList,sa.rule.types.AlternativeList>>) 
        new tom.library.enumerator.F<sa.rule.types.Alternative,tom.library.enumerator.F<sa.rule.types.AlternativeList,sa.rule.types.AlternativeList>>() {
          public tom.library.enumerator.F<sa.rule.types.AlternativeList,sa.rule.types.AlternativeList> apply(final sa.rule.types.Alternative t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.AlternativeList,sa.rule.types.AlternativeList>() {
          public sa.rule.types.AlternativeList apply(final sa.rule.types.AlternativeList t2) {
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
