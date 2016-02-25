
package sa.rule.types.fieldlist;



public final class ConsConcField extends sa.rule.types.fieldlist.ConcField implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsConcField";


  private ConsConcField() {}
  private int hashCode;
  private static ConsConcField gomProto = new ConsConcField();
  
   private sa.rule.types.Field[] children;
                  private sa.rule.types.Field _HeadConcField;
  private sa.rule.types.FieldList _TailConcField;

  /**
   * Constructor that builds a term rooted by ConsConcField
   *
   * @return a term rooted by ConsConcField
   */

  public static ConsConcField make(sa.rule.types.Field _HeadConcField, sa.rule.types.FieldList _TailConcField) {

    // use the proto as a model
    gomProto.initHashCode( _HeadConcField,  _TailConcField);
    return (ConsConcField) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcField
   * @param _TailConcField
   * @param hashCode hashCode of ConsConcField
   */
  private void init(sa.rule.types.Field _HeadConcField, sa.rule.types.FieldList _TailConcField, int hashCode) {
    this._HeadConcField = _HeadConcField;
    this._TailConcField = _TailConcField;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadConcField
   * @param _TailConcField
   */
  private void initHashCode(sa.rule.types.Field _HeadConcField, sa.rule.types.FieldList _TailConcField) {
    this._HeadConcField = _HeadConcField;
    this._TailConcField = _TailConcField;

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
    return "ConsConcField";
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
    ConsConcField clone = new ConsConcField();
    clone.init( _HeadConcField,  _TailConcField, hashCode);
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
    ConsConcField tco = (ConsConcField) ao;
    int _HeadConcFieldCmp = (this._HeadConcField).compareToLPO(tco._HeadConcField);
    if(_HeadConcFieldCmp != 0) {
      return _HeadConcFieldCmp;
    }

    int _TailConcFieldCmp = (this._TailConcField).compareToLPO(tco._TailConcField);
    if(_TailConcFieldCmp != 0) {
      return _TailConcFieldCmp;
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
    ConsConcField tco = (ConsConcField) ao;
    int _HeadConcFieldCmp = (this._HeadConcField).compareTo(tco._HeadConcField);
    if(_HeadConcFieldCmp != 0) {
      return _HeadConcFieldCmp;
    }

    int _TailConcFieldCmp = (this._TailConcField).compareTo(tco._TailConcField);
    if(_TailConcFieldCmp != 0) {
      return _TailConcFieldCmp;
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
   * @return true if obj is a ConsConcField and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsConcField) {

      ConsConcField peer = (ConsConcField) obj;
      return _HeadConcField==peer._HeadConcField && _TailConcField==peer._TailConcField && true;
    }
    return false;
  }


   //FieldList interface
  /**
   * Returns true if the term is rooted by the symbol ConsConcField
   *
   * @return true, because this is rooted by ConsConcField
   */
  @Override
  public boolean isConsConcField() {
    return true;
  }
  
  /**
   * Returns the attribute sa.rule.types.Field
   *
   * @return the attribute sa.rule.types.Field
   */
  @Override
  public sa.rule.types.Field getHeadConcField() {
    return _HeadConcField;
  }

  /**
   * Sets and returns the attribute sa.rule.types.FieldList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Field which just has been set
   */
  @Override
  public sa.rule.types.FieldList setHeadConcField(sa.rule.types.Field set_arg) {
    return make(set_arg, _TailConcField);
  }
  
  /**
   * Returns the attribute sa.rule.types.FieldList
   *
   * @return the attribute sa.rule.types.FieldList
   */
  @Override
  public sa.rule.types.FieldList getTailConcField() {
    return _TailConcField;
  }

  /**
   * Sets and returns the attribute sa.rule.types.FieldList
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.FieldList which just has been set
   */
  @Override
  public sa.rule.types.FieldList setTailConcField(sa.rule.types.FieldList set_arg) {
    return make(_HeadConcField, set_arg);
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
      sa.rule.types.Field[] new_children = new sa.rule.types.Field[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((sa.rule.types.Field) children[i]);
      }
     new_children[index] = (sa.rule.types.Field) v;
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
      
               sa.rule.types.Field[] typed_children = new sa.rule.types.Field[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (sa.rule.types.Field) children[i];
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
          children = toArray(new sa.rule.types.Field[]{});
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
    b = (-2116867649<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadConcField.hashCode() << 8);
    a += (_TailConcField.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Field>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.FieldList>,tom.library.enumerator.Enumeration<sa.rule.types.FieldList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Field>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.FieldList>,tom.library.enumerator.Enumeration<sa.rule.types.FieldList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.FieldList>,tom.library.enumerator.Enumeration<sa.rule.types.FieldList>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Field> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.FieldList>,tom.library.enumerator.Enumeration<sa.rule.types.FieldList>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.FieldList> apply(final tom.library.enumerator.Enumeration<sa.rule.types.FieldList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<sa.rule.types.Field,tom.library.enumerator.F<sa.rule.types.FieldList,sa.rule.types.FieldList>>) 
        new tom.library.enumerator.F<sa.rule.types.Field,tom.library.enumerator.F<sa.rule.types.FieldList,sa.rule.types.FieldList>>() {
          public tom.library.enumerator.F<sa.rule.types.FieldList,sa.rule.types.FieldList> apply(final sa.rule.types.Field t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.FieldList,sa.rule.types.FieldList>() {
          public sa.rule.types.FieldList apply(final sa.rule.types.FieldList t2) {
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
