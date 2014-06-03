
package examples.shop.shop.types.lineitem;



public final class Line extends examples.shop.shop.types.LineItem implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Line";


  private Line() {}
  private int hashCode;
  private static Line gomProto = new Line();
  
private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_Inventory(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Inventory(Object t) {return  (t instanceof examples.shop.shop.types.Inventory) ;}private static boolean tom_equal_term_ShoppingCart(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ShoppingCart(Object t) {return  (t instanceof examples.shop.shop.types.ShoppingCart) ;}private static boolean tom_equal_term_LineItem(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_LineItem(Object t) {return  (t instanceof examples.shop.shop.types.LineItem) ;}private static boolean tom_equal_term_Item(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Item(Object t) {return  (t instanceof examples.shop.shop.types.Item) ;}






































































  private examples.shop.shop.types.Item _Item;
  private int _Quantity;

  /**
   * Constructor that builds a term rooted by Line
   *
   * @return a term rooted by Line
   */

    public static examples.shop.shop.types.LineItem make(examples.shop.shop.types.Item item, int quantity) {
  if (true) {{
	int newQuantity = 1;
	if (quantity < 0) {
		newQuantity = Math.abs(quantity);
	} else if (quantity > 0) {
		newQuantity = quantity;
	} 
	return realMake(item, newQuantity);
}}
      return realMake( item,  quantity);
    }
  
  private static Line realMake(examples.shop.shop.types.Item _Item, int _Quantity) {

    // use the proto as a model
    gomProto.initHashCode( _Item,  _Quantity);
    return (Line) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Item
   * @param _Quantity
   * @param hashCode hashCode of Line
   */
  private void init(examples.shop.shop.types.Item _Item, int _Quantity, int hashCode) {
    this._Item = _Item;
    this._Quantity = _Quantity;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Item
   * @param _Quantity
   */
  private void initHashCode(examples.shop.shop.types.Item _Item, int _Quantity) {
    this._Item = _Item;
    this._Quantity = _Quantity;

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
    return "Line";
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
    Line clone = new Line();
    clone.init( _Item,  _Quantity, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Line(");
    _Item.toStringBuilder(buffer);
buffer.append(",");
    buffer.append(_Quantity);

    buffer.append(")");
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
    examples.shop.shop.ShopAbstractType ao = (examples.shop.shop.ShopAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    Line tco = (Line) ao;
    int _ItemCmp = (this._Item).compareToLPO(tco._Item);
    if(_ItemCmp != 0) {
      return _ItemCmp;
    }

    if( this._Quantity != tco._Quantity) {
      return (this._Quantity < tco._Quantity)?-1:1;
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
    examples.shop.shop.ShopAbstractType ao = (examples.shop.shop.ShopAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    Line tco = (Line) ao;
    int _ItemCmp = (this._Item).compareTo(tco._Item);
    if(_ItemCmp != 0) {
      return _ItemCmp;
    }

    if( this._Quantity != tco._Quantity) {
      return (this._Quantity < tco._Quantity)?-1:1;
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
   * @return true if obj is a Line and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Line) {

      Line peer = (Line) obj;
      return _Item==peer._Item && _Quantity==peer._Quantity && true;
    }
    return false;
  }


   //LineItem interface
  /**
   * Returns true if the term is rooted by the symbol Line
   *
   * @return true, because this is rooted by Line
   */
  @Override
  public boolean isLine() {
    return true;
  }
  
  /**
   * Returns the attribute examples.shop.shop.types.Item
   *
   * @return the attribute examples.shop.shop.types.Item
   */
  @Override
  public examples.shop.shop.types.Item getItem() {
    return _Item;
  }

  /**
   * Sets and returns the attribute examples.shop.shop.types.LineItem
   *
   * @param set_arg the argument to set
   * @return the attribute examples.shop.shop.types.Item which just has been set
   */
  @Override
  public examples.shop.shop.types.LineItem setItem(examples.shop.shop.types.Item set_arg) {
    return make(set_arg, _Quantity);
  }
  
  /**
   * Returns the attribute int
   *
   * @return the attribute int
   */
  @Override
  public int getQuantity() {
    return _Quantity;
  }

  /**
   * Sets and returns the attribute examples.shop.shop.types.LineItem
   *
   * @param set_arg the argument to set
   * @return the attribute int which just has been set
   */
  @Override
  public examples.shop.shop.types.LineItem setQuantity(int set_arg) {
    return make(_Item, set_arg);
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
      new aterm.ATerm[] {getItem().toATerm(), (aterm.ATerm) atermFactory.makeInt(getQuantity())});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.shop.shop.types.LineItem from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.LineItem fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.shop.shop.types.Item.fromTerm(appl.getArgument(0),atConv), convertATermToInt(appl.getArgument(1), atConv)
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
    return 2;
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
        switch(index) {
      case 0: return _Item;
      case 1: return new tom.library.sl.VisitableBuiltin<java.lang.Integer>(_Quantity);
      default: throw new IndexOutOfBoundsException();
 }
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
        switch(index) {
      case 0: return make((examples.shop.shop.types.Item) v, getQuantity());
      case 1: return make(_Item, getQuantity());
      default: throw new IndexOutOfBoundsException();
 }
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
    if (children.length == getChildCount()  && children[0] instanceof examples.shop.shop.types.Item && children[1] instanceof tom.library.sl.VisitableBuiltin) {
      return make((examples.shop.shop.types.Item) children[0], ((tom.library.sl.VisitableBuiltin<java.lang.Integer>)children[1]).getBuiltin());
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
    return new tom.library.sl.Visitable[] { _Item,  new tom.library.sl.VisitableBuiltin<java.lang.Integer>(_Quantity)};
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
    b = (56120970<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_Item.hashCode() << 8);
    a += (_Quantity);

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.shop.shop.types.Item>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.shop.shop.types.Item>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem>> apply(final tom.library.enumerator.Enumeration<examples.shop.shop.types.Item> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem>>() {
          public tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem> apply(final tom.library.enumerator.Enumeration<java.lang.Integer> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.shop.shop.types.Item,tom.library.enumerator.F<java.lang.Integer,examples.shop.shop.types.LineItem>>) 
        new tom.library.enumerator.F<examples.shop.shop.types.Item,tom.library.enumerator.F<java.lang.Integer,examples.shop.shop.types.LineItem>>() {
          public tom.library.enumerator.F<java.lang.Integer,examples.shop.shop.types.LineItem> apply(final examples.shop.shop.types.Item t1) {
            return 
        new tom.library.enumerator.F<java.lang.Integer,examples.shop.shop.types.LineItem>() {
          public examples.shop.shop.types.LineItem apply(final java.lang.Integer t2) {
            return make(t1,java.lang.Integer.valueOf(t2));
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
