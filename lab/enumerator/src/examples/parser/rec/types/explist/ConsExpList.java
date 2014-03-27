
package examples.parser.rec.types.explist;



public final class ConsExpList extends examples.parser.rec.types.explist.ExpList implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsExpList";


  private ConsExpList() {}
  private int hashCode;
  private static ConsExpList gomProto = new ConsExpList();
  
   private examples.parser.rec.types.Exp[] children;
                  private examples.parser.rec.types.Exp _HeadExpList;
  private examples.parser.rec.types.ExpList _TailExpList;

  /**
   * Constructor that builds a term rooted by ConsExpList
   *
   * @return a term rooted by ConsExpList
   */

  public static ConsExpList make(examples.parser.rec.types.Exp _HeadExpList, examples.parser.rec.types.ExpList _TailExpList) {

    // use the proto as a model
    gomProto.initHashCode( _HeadExpList,  _TailExpList);
    return (ConsExpList) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadExpList
   * @param _TailExpList
   * @param hashCode hashCode of ConsExpList
   */
  private void init(examples.parser.rec.types.Exp _HeadExpList, examples.parser.rec.types.ExpList _TailExpList, int hashCode) {
    this._HeadExpList = _HeadExpList;
    this._TailExpList = _TailExpList;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _HeadExpList
   * @param _TailExpList
   */
  private void initHashCode(examples.parser.rec.types.Exp _HeadExpList, examples.parser.rec.types.ExpList _TailExpList) {
    this._HeadExpList = _HeadExpList;
    this._TailExpList = _TailExpList;

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
    return "ConsExpList";
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
    ConsExpList clone = new ConsExpList();
    clone.init( _HeadExpList,  _TailExpList, hashCode);
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
    ConsExpList tco = (ConsExpList) ao;
    int _HeadExpListCmp = (this._HeadExpList).compareToLPO(tco._HeadExpList);
    if(_HeadExpListCmp != 0) {
      return _HeadExpListCmp;
    }

    int _TailExpListCmp = (this._TailExpList).compareToLPO(tco._TailExpList);
    if(_TailExpListCmp != 0) {
      return _TailExpListCmp;
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
    ConsExpList tco = (ConsExpList) ao;
    int _HeadExpListCmp = (this._HeadExpList).compareTo(tco._HeadExpList);
    if(_HeadExpListCmp != 0) {
      return _HeadExpListCmp;
    }

    int _TailExpListCmp = (this._TailExpList).compareTo(tco._TailExpList);
    if(_TailExpListCmp != 0) {
      return _TailExpListCmp;
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
   * @return true if obj is a ConsExpList and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsExpList) {

      ConsExpList peer = (ConsExpList) obj;
      return _HeadExpList==peer._HeadExpList && _TailExpList==peer._TailExpList && true;
    }
    return false;
  }


   //ExpList interface
  /**
   * Returns true if the term is rooted by the symbol ConsExpList
   *
   * @return true, because this is rooted by ConsExpList
   */
  @Override
  public boolean isConsExpList() {
    return true;
  }
  
  /**
   * Returns the attribute parser.rec.types.Exp
   *
   * @return the attribute parser.rec.types.Exp
   */
  @Override
  public examples.parser.rec.types.Exp getHeadExpList() {
    return _HeadExpList;
  }

  /**
   * Sets and returns the attribute parser.rec.types.ExpList
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.Exp which just has been set
   */
  @Override
  public examples.parser.rec.types.ExpList setHeadExpList(examples.parser.rec.types.Exp set_arg) {
    return make(set_arg, _TailExpList);
  }
  
  /**
   * Returns the attribute parser.rec.types.ExpList
   *
   * @return the attribute parser.rec.types.ExpList
   */
  @Override
  public examples.parser.rec.types.ExpList getTailExpList() {
    return _TailExpList;
  }

  /**
   * Sets and returns the attribute parser.rec.types.ExpList
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.ExpList which just has been set
   */
  @Override
  public examples.parser.rec.types.ExpList setTailExpList(examples.parser.rec.types.ExpList set_arg) {
    return make(_HeadExpList, set_arg);
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
      new aterm.ATerm[] {getHeadExpList().toATerm(), getTailExpList().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.ExpList from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.ExpList fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.parser.rec.types.Exp.fromTerm(appl.getArgument(0),atConv), examples.parser.rec.types.ExpList.fromTerm(appl.getArgument(1),atConv)
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
      examples.parser.rec.types.Exp[] new_children = new examples.parser.rec.types.Exp[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((examples.parser.rec.types.Exp) children[i]); 
      }
     new_children[index] = (examples.parser.rec.types.Exp) v;
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
      
               examples.parser.rec.types.Exp[] typed_children = new examples.parser.rec.types.Exp[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (examples.parser.rec.types.Exp) children[i]; 
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
          children = toArray(new examples.parser.rec.types.Exp[]{});
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
    b = (-337429493<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadExpList.hashCode() << 8);
    a += (_TailExpList.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList>,tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList>,tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList>,tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList>> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList>,tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList>>() {
          public tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.ExpList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.parser.rec.types.Exp,tom.library.enumerator.F<examples.parser.rec.types.ExpList,examples.parser.rec.types.ExpList>>) 
        new tom.library.enumerator.F<examples.parser.rec.types.Exp,tom.library.enumerator.F<examples.parser.rec.types.ExpList,examples.parser.rec.types.ExpList>>() {
          public tom.library.enumerator.F<examples.parser.rec.types.ExpList,examples.parser.rec.types.ExpList> apply(final examples.parser.rec.types.Exp t1) {
            return 
        new tom.library.enumerator.F<examples.parser.rec.types.ExpList,examples.parser.rec.types.ExpList>() {
          public examples.parser.rec.types.ExpList apply(final examples.parser.rec.types.ExpList t2) {
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
