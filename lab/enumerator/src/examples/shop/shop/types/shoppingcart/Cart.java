
package examples.shop.shop.types.shoppingcart;



public final class Cart extends examples.shop.shop.types.ShoppingCart implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Cart";


  private Cart() {}
  private int hashCode;
  private static Cart gomProto = new Cart();
    private examples.shop.shop.types.LineItem _LineItem;
  private examples.shop.shop.types.ShoppingCart _ShoppingCart;

  /**
   * Constructor that builds a term rooted by Cart
   *
   * @return a term rooted by Cart
   */

  public static Cart make(examples.shop.shop.types.LineItem _LineItem, examples.shop.shop.types.ShoppingCart _ShoppingCart) {

    // use the proto as a model
    gomProto.initHashCode( _LineItem,  _ShoppingCart);
    return (Cart) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _LineItem
   * @param _ShoppingCart
   * @param hashCode hashCode of Cart
   */
  private void init(examples.shop.shop.types.LineItem _LineItem, examples.shop.shop.types.ShoppingCart _ShoppingCart, int hashCode) {
    this._LineItem = _LineItem;
    this._ShoppingCart = _ShoppingCart;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _LineItem
   * @param _ShoppingCart
   */
  private void initHashCode(examples.shop.shop.types.LineItem _LineItem, examples.shop.shop.types.ShoppingCart _ShoppingCart) {
    this._LineItem = _LineItem;
    this._ShoppingCart = _ShoppingCart;

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
    return "Cart";
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
    Cart clone = new Cart();
    clone.init( _LineItem,  _ShoppingCart, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Cart(");
    _LineItem.toStringBuilder(buffer);
buffer.append(",");
    _ShoppingCart.toStringBuilder(buffer);

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
    Cart tco = (Cart) ao;
    int _LineItemCmp = (this._LineItem).compareToLPO(tco._LineItem);
    if(_LineItemCmp != 0) {
      return _LineItemCmp;
    }

    int _ShoppingCartCmp = (this._ShoppingCart).compareToLPO(tco._ShoppingCart);
    if(_ShoppingCartCmp != 0) {
      return _ShoppingCartCmp;
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
    Cart tco = (Cart) ao;
    int _LineItemCmp = (this._LineItem).compareTo(tco._LineItem);
    if(_LineItemCmp != 0) {
      return _LineItemCmp;
    }

    int _ShoppingCartCmp = (this._ShoppingCart).compareTo(tco._ShoppingCart);
    if(_ShoppingCartCmp != 0) {
      return _ShoppingCartCmp;
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
   * @return true if obj is a Cart and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Cart) {

      Cart peer = (Cart) obj;
      return _LineItem==peer._LineItem && _ShoppingCart==peer._ShoppingCart && true;
    }
    return false;
  }


   //ShoppingCart interface
  /**
   * Returns true if the term is rooted by the symbol Cart
   *
   * @return true, because this is rooted by Cart
   */
  @Override
  public boolean isCart() {
    return true;
  }
  
  /**
   * Returns the attribute examples.shop.shop.types.LineItem
   *
   * @return the attribute examples.shop.shop.types.LineItem
   */
  @Override
  public examples.shop.shop.types.LineItem getLineItem() {
    return _LineItem;
  }

  /**
   * Sets and returns the attribute examples.shop.shop.types.ShoppingCart
   *
   * @param set_arg the argument to set
   * @return the attribute examples.shop.shop.types.LineItem which just has been set
   */
  @Override
  public examples.shop.shop.types.ShoppingCart setLineItem(examples.shop.shop.types.LineItem set_arg) {
    return make(set_arg, _ShoppingCart);
  }
  
  /**
   * Returns the attribute examples.shop.shop.types.ShoppingCart
   *
   * @return the attribute examples.shop.shop.types.ShoppingCart
   */
  @Override
  public examples.shop.shop.types.ShoppingCart getShoppingCart() {
    return _ShoppingCart;
  }

  /**
   * Sets and returns the attribute examples.shop.shop.types.ShoppingCart
   *
   * @param set_arg the argument to set
   * @return the attribute examples.shop.shop.types.ShoppingCart which just has been set
   */
  @Override
  public examples.shop.shop.types.ShoppingCart setShoppingCart(examples.shop.shop.types.ShoppingCart set_arg) {
    return make(_LineItem, set_arg);
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
      new aterm.ATerm[] {getLineItem().toATerm(), getShoppingCart().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.shop.shop.types.ShoppingCart from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.ShoppingCart fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
examples.shop.shop.types.LineItem.fromTerm(appl.getArgument(0),atConv), examples.shop.shop.types.ShoppingCart.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _LineItem;
      case 1: return _ShoppingCart;
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
      case 0: return make((examples.shop.shop.types.LineItem) v, _ShoppingCart);
      case 1: return make(_LineItem, (examples.shop.shop.types.ShoppingCart) v);
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
    if (children.length == getChildCount()  && children[0] instanceof examples.shop.shop.types.LineItem && children[1] instanceof examples.shop.shop.types.ShoppingCart) {
      return make((examples.shop.shop.types.LineItem) children[0], (examples.shop.shop.types.ShoppingCart) children[1]);
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
    return new tom.library.sl.Visitable[] { _LineItem,  _ShoppingCart};
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
    b = (-569112153<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_LineItem.hashCode() << 8);
    a += (_ShoppingCart.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart>,tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart>,tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart>,tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart>> apply(final tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart>,tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart>>() {
          public tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart> apply(final tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<examples.shop.shop.types.LineItem,tom.library.enumerator.F<examples.shop.shop.types.ShoppingCart,examples.shop.shop.types.ShoppingCart>>) 
        new tom.library.enumerator.F<examples.shop.shop.types.LineItem,tom.library.enumerator.F<examples.shop.shop.types.ShoppingCart,examples.shop.shop.types.ShoppingCart>>() {
          public tom.library.enumerator.F<examples.shop.shop.types.ShoppingCart,examples.shop.shop.types.ShoppingCart> apply(final examples.shop.shop.types.LineItem t1) {
            return 
        new tom.library.enumerator.F<examples.shop.shop.types.ShoppingCart,examples.shop.shop.types.ShoppingCart>() {
          public examples.shop.shop.types.ShoppingCart apply(final examples.shop.shop.types.ShoppingCart t2) {
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
