
package examples.shop.shop.types.item;



public final class Product extends examples.shop.shop.types.Item implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Product";


  private Product() {}
  private int hashCode;
  private static Product gomProto = new Product();
  
private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_Inventory(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Inventory(Object t) {return  (t instanceof examples.shop.shop.types.Inventory) ;}private static boolean tom_equal_term_ShoppingCart(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ShoppingCart(Object t) {return  (t instanceof examples.shop.shop.types.ShoppingCart) ;}private static boolean tom_equal_term_LineItem(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_LineItem(Object t) {return  (t instanceof examples.shop.shop.types.LineItem) ;}private static boolean tom_equal_term_Item(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Item(Object t) {return  (t instanceof examples.shop.shop.types.Item) ;}






































































  private int _Id;
  private int _Price;

  /**
   * Constructor that builds a term rooted by Product
   *
   * @return a term rooted by Product
   */

    public static examples.shop.shop.types.Item make(int id, int price) {
  if (true) {{
	int newId = 1;
	int newPrice = 1;
	if (id < 0) {
		newId = Math.abs(id);
	} else if (id > 0) {
		newId = id;
	} 
	if (price < 0) {
		newPrice = Math.abs(price);
	} else if (price > 0) {
		newPrice = price;
	} 
	return realMake(newId, newPrice);
}}
      return realMake( id,  price);
    }
  
  private static Product realMake(int _Id, int _Price) {

    // use the proto as a model
    gomProto.initHashCode( _Id,  _Price);
    return (Product) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Id
   * @param _Price
   * @param hashCode hashCode of Product
   */
  private void init(int _Id, int _Price, int hashCode) {
    this._Id = _Id;
    this._Price = _Price;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Id
   * @param _Price
   */
  private void initHashCode(int _Id, int _Price) {
    this._Id = _Id;
    this._Price = _Price;

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
    return "Product";
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
    Product clone = new Product();
    clone.init( _Id,  _Price, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Product(");
    buffer.append(_Id);
buffer.append(",");
    buffer.append(_Price);

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
    Product tco = (Product) ao;
    if( this._Id != tco._Id) {
      return (this._Id < tco._Id)?-1:1;
    }

    if( this._Price != tco._Price) {
      return (this._Price < tco._Price)?-1:1;
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
    Product tco = (Product) ao;
    if( this._Id != tco._Id) {
      return (this._Id < tco._Id)?-1:1;
    }

    if( this._Price != tco._Price) {
      return (this._Price < tco._Price)?-1:1;
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
   * @return true if obj is a Product and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Product) {

      Product peer = (Product) obj;
      return _Id==peer._Id && _Price==peer._Price && true;
    }
    return false;
  }


   //Item interface
  /**
   * Returns true if the term is rooted by the symbol Product
   *
   * @return true, because this is rooted by Product
   */
  @Override
  public boolean isProduct() {
    return true;
  }
  
  /**
   * Returns the attribute int
   *
   * @return the attribute int
   */
  @Override
  public int getId() {
    return _Id;
  }

  /**
   * Sets and returns the attribute examples.shop.shop.types.Item
   *
   * @param set_arg the argument to set
   * @return the attribute int which just has been set
   */
  @Override
  public examples.shop.shop.types.Item setId(int set_arg) {
    return make(set_arg, _Price);
  }
  
  /**
   * Returns the attribute int
   *
   * @return the attribute int
   */
  @Override
  public int getPrice() {
    return _Price;
  }

  /**
   * Sets and returns the attribute examples.shop.shop.types.Item
   *
   * @param set_arg the argument to set
   * @return the attribute int which just has been set
   */
  @Override
  public examples.shop.shop.types.Item setPrice(int set_arg) {
    return make(_Id, set_arg);
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
      new aterm.ATerm[] {(aterm.ATerm) atermFactory.makeInt(getId()), (aterm.ATerm) atermFactory.makeInt(getPrice())});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.shop.shop.types.Item from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.Item fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
convertATermToInt(appl.getArgument(0), atConv), convertATermToInt(appl.getArgument(1), atConv)
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
      case 0: return new tom.library.sl.VisitableBuiltin<java.lang.Integer>(_Id);
      case 1: return new tom.library.sl.VisitableBuiltin<java.lang.Integer>(_Price);
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
      case 0: return make(getId(), getPrice());
      case 1: return make(getId(), getPrice());
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
    if (children.length == getChildCount()  && children[0] instanceof tom.library.sl.VisitableBuiltin && children[1] instanceof tom.library.sl.VisitableBuiltin) {
      return make(((tom.library.sl.VisitableBuiltin<java.lang.Integer>)children[0]).getBuiltin(), ((tom.library.sl.VisitableBuiltin<java.lang.Integer>)children[1]).getBuiltin());
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
    return new tom.library.sl.Visitable[] { new tom.library.sl.VisitableBuiltin<java.lang.Integer>(_Id),  new tom.library.sl.VisitableBuiltin<java.lang.Integer>(_Price)};
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
    b = (1654039106<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_Id << 8);
    a += (_Price);

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.shop.shop.types.Item>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.shop.shop.types.Item>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.shop.shop.types.Item>> apply(final tom.library.enumerator.Enumeration<java.lang.Integer> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<java.lang.Integer>,tom.library.enumerator.Enumeration<examples.shop.shop.types.Item>>() {
          public tom.library.enumerator.Enumeration<examples.shop.shop.types.Item> apply(final tom.library.enumerator.Enumeration<java.lang.Integer> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<java.lang.Integer,tom.library.enumerator.F<java.lang.Integer,examples.shop.shop.types.Item>>) 
        new tom.library.enumerator.F<java.lang.Integer,tom.library.enumerator.F<java.lang.Integer,examples.shop.shop.types.Item>>() {
          public tom.library.enumerator.F<java.lang.Integer,examples.shop.shop.types.Item> apply(final java.lang.Integer t1) {
            return 
        new tom.library.enumerator.F<java.lang.Integer,examples.shop.shop.types.Item>() {
          public examples.shop.shop.types.Item apply(final java.lang.Integer t2) {
            return make(java.lang.Integer.valueOf(t1),java.lang.Integer.valueOf(t2));
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
