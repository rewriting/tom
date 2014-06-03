
package examples.shop.shop.types;


public abstract class Item extends examples.shop.shop.ShopAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Item() {}

public examples.shop.Item translateItem() {
		{{if (tom_is_sort_Item(this)) {if (tom_is_sort_Item((( examples.shop.shop.types.Item )this))) {if (tom_is_fun_sym_Product((( examples.shop.shop.types.Item )(( examples.shop.shop.types.Item )this)))) {
 return new examples.shop.Item(tom_get_slot_Product_Id((( examples.shop.shop.types.Item )this)), tom_get_slot_Product_Price((( examples.shop.shop.types.Item )this))); }}}}}

		return null;
	}


  /**
   * Returns true if the term is rooted by the symbol Product
   *
   * @return true if the term is rooted by the symbol Product
   */
  public boolean isProduct() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Id
   *
   * @return the subterm corresponding to the slot Id
   */
  public int getId() {
    throw new UnsupportedOperationException("This Item has no Id");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Id
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Id is replaced by _arg
   */
  public Item setId(int _arg) {
    throw new UnsupportedOperationException("This Item has no Id");
  }

  /**
   * Returns the subterm corresponding to the slot Price
   *
   * @return the subterm corresponding to the slot Price
   */
  public int getPrice() {
    throw new UnsupportedOperationException("This Item has no Price");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Price
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Price is replaced by _arg
   */
  public Item setPrice(int _arg) {
    throw new UnsupportedOperationException("This Item has no Price");
  }

  protected static tom.library.utils.IdConverter idConv = new tom.library.utils.IdConverter();

  /**
   * Returns an ATerm representation of this term.
   *
   * @return null to indicate to sub-classes that they have to work
   */
  public aterm.ATerm toATerm() {
    // returns null to indicate sub-classes that they have to work
    return null;
  }

  /**
   * Returns a examples.shop.shop.types.Item from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.shop.shop.types.Item fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.shop.shop.types.Item from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.shop.shop.types.Item fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.shop.shop.types.Item from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.shop.shop.types.Item fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.shop.shop.types.Item
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.shop.shop.types.Item fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.shop.shop.types.Item tmp;
    java.util.ArrayList<examples.shop.shop.types.Item> results = new java.util.ArrayList<examples.shop.shop.types.Item>();

    tmp = examples.shop.shop.types.item.Product.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Item");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Item").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.shop.shop.types.Item", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.shop.shop.types.Item from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.Item fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.shop.shop.types.Item from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.Item fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),atConv);
  }

  /**
   * Returns the length of the list
   *
   * @return the length of the list
   * @throws IllegalArgumentException if the term is not a list
   */
  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  /**
   * Returns an inverted term
   *
   * @return the inverted list
   * @throws IllegalArgumentException if the term is not a list
   */
  public examples.shop.shop.types.Item reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_Inventory(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Inventory(Object t) {return  (t instanceof examples.shop.shop.types.Inventory) ;}private static boolean tom_equal_term_ShoppingCart(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ShoppingCart(Object t) {return  (t instanceof examples.shop.shop.types.ShoppingCart) ;}private static boolean tom_equal_term_LineItem(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_LineItem(Object t) {return  (t instanceof examples.shop.shop.types.LineItem) ;}private static boolean tom_equal_term_Item(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Item(Object t) {return  (t instanceof examples.shop.shop.types.Item) ;}private static boolean tom_is_fun_sym_Product( examples.shop.shop.types.Item  t) {return  (t instanceof examples.shop.shop.types.item.Product) ;}private static  int  tom_get_slot_Product_Id( examples.shop.shop.types.Item  t) {return  t.getId() ;}private static  int  tom_get_slot_Product_Price( examples.shop.shop.types.Item  t) {return  t.getPrice() ;}







































































  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.shop.shop.types.Item> enumItem = null;
  public static final tom.library.enumerator.Enumeration<examples.shop.shop.types.Item> tmpenumItem = new tom.library.enumerator.Enumeration<examples.shop.shop.types.Item>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.Item>>) null);

  public static tom.library.enumerator.Enumeration<examples.shop.shop.types.Item> getEnumeration() {
    if(enumItem == null) { 
      enumItem = examples.shop.shop.types.item.Product.funMake().apply(tom.library.enumerator.Combinators.makeint()).apply(tom.library.enumerator.Combinators.makeint());


      tmpenumItem.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.Item>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.Item>> _1() { return enumItem.parts(); }
      };

    }
    return enumItem;
  }

}
