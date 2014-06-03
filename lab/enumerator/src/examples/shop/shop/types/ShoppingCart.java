
package examples.shop.shop.types;
import examples.shop.*;
	import java.util.*;


public abstract class ShoppingCart extends examples.shop.shop.ShopAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected ShoppingCart() {}

public examples.shop.Cart translateCart() {
		examples.shop.Cart cart = new examples.shop.Cart();
		buildCart(cart, this);
		return cart;
	}
	
	private void buildCart(examples.shop.Cart cart, ShoppingCart shoppingCart) {
		{{if (tom_is_sort_ShoppingCart(shoppingCart)) {if (tom_is_sort_ShoppingCart((( examples.shop.shop.types.ShoppingCart )shoppingCart))) {if (tom_is_fun_sym_EmptyCart((( examples.shop.shop.types.ShoppingCart )(( examples.shop.shop.types.ShoppingCart )shoppingCart)))) {
 return; }}}}{if (tom_is_sort_ShoppingCart(shoppingCart)) {if (tom_is_sort_ShoppingCart((( examples.shop.shop.types.ShoppingCart )shoppingCart))) {if (tom_is_fun_sym_Cart((( examples.shop.shop.types.ShoppingCart )(( examples.shop.shop.types.ShoppingCart )shoppingCart)))) {

				cart.addToCart(tom_get_slot_Cart_LineItem((( examples.shop.shop.types.ShoppingCart )shoppingCart)).translateLineItem()); 
				buildCart(cart, tom_get_slot_Cart_ShoppingCart((( examples.shop.shop.types.ShoppingCart )shoppingCart)));
			}}}}}

	}


  /**
   * Returns true if the term is rooted by the symbol EmptyCart
   *
   * @return true if the term is rooted by the symbol EmptyCart
   */
  public boolean isEmptyCart() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Cart
   *
   * @return true if the term is rooted by the symbol Cart
   */
  public boolean isCart() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot LineItem
   *
   * @return the subterm corresponding to the slot LineItem
   */
  public examples.shop.shop.types.LineItem getLineItem() {
    throw new UnsupportedOperationException("This ShoppingCart has no LineItem");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot LineItem
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot LineItem is replaced by _arg
   */
  public ShoppingCart setLineItem(examples.shop.shop.types.LineItem _arg) {
    throw new UnsupportedOperationException("This ShoppingCart has no LineItem");
  }

  /**
   * Returns the subterm corresponding to the slot ShoppingCart
   *
   * @return the subterm corresponding to the slot ShoppingCart
   */
  public examples.shop.shop.types.ShoppingCart getShoppingCart() {
    throw new UnsupportedOperationException("This ShoppingCart has no ShoppingCart");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot ShoppingCart
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot ShoppingCart is replaced by _arg
   */
  public ShoppingCart setShoppingCart(examples.shop.shop.types.ShoppingCart _arg) {
    throw new UnsupportedOperationException("This ShoppingCart has no ShoppingCart");
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
   * Returns a examples.shop.shop.types.ShoppingCart from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.shop.shop.types.ShoppingCart fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.shop.shop.types.ShoppingCart from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.shop.shop.types.ShoppingCart fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.shop.shop.types.ShoppingCart from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.shop.shop.types.ShoppingCart fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.shop.shop.types.ShoppingCart
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.shop.shop.types.ShoppingCart fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.shop.shop.types.ShoppingCart tmp;
    java.util.ArrayList<examples.shop.shop.types.ShoppingCart> results = new java.util.ArrayList<examples.shop.shop.types.ShoppingCart>();

    tmp = examples.shop.shop.types.shoppingcart.EmptyCart.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.shop.shop.types.shoppingcart.Cart.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a ShoppingCart");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("ShoppingCart").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.shop.shop.types.ShoppingCart", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.shop.shop.types.ShoppingCart from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.ShoppingCart fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.shop.shop.types.ShoppingCart from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.ShoppingCart fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.shop.shop.types.ShoppingCart reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_Inventory(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Inventory(Object t) {return  (t instanceof examples.shop.shop.types.Inventory) ;}private static boolean tom_equal_term_ShoppingCart(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ShoppingCart(Object t) {return  (t instanceof examples.shop.shop.types.ShoppingCart) ;}private static boolean tom_equal_term_LineItem(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_LineItem(Object t) {return  (t instanceof examples.shop.shop.types.LineItem) ;}private static boolean tom_equal_term_Item(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Item(Object t) {return  (t instanceof examples.shop.shop.types.Item) ;}private static boolean tom_is_fun_sym_EmptyCart( examples.shop.shop.types.ShoppingCart  t) {return  (t instanceof examples.shop.shop.types.shoppingcart.EmptyCart) ;}private static boolean tom_is_fun_sym_Cart( examples.shop.shop.types.ShoppingCart  t) {return  (t instanceof examples.shop.shop.types.shoppingcart.Cart) ;}private static  examples.shop.shop.types.LineItem  tom_get_slot_Cart_LineItem( examples.shop.shop.types.ShoppingCart  t) {return  t.getLineItem() ;}private static  examples.shop.shop.types.ShoppingCart  tom_get_slot_Cart_ShoppingCart( examples.shop.shop.types.ShoppingCart  t) {return  t.getShoppingCart() ;}







































































  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart> enumShoppingCart = null;
  public static final tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart> tmpenumShoppingCart = new tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.ShoppingCart>>) null);

  public static tom.library.enumerator.Enumeration<examples.shop.shop.types.ShoppingCart> getEnumeration() {
    if(enumShoppingCart == null) { 
      enumShoppingCart = examples.shop.shop.types.shoppingcart.EmptyCart.funMake().apply(examples.shop.shop.types.ShoppingCart.tmpenumShoppingCart)
        .plus(examples.shop.shop.types.shoppingcart.Cart.funMake().apply(examples.shop.shop.types.LineItem.tmpenumLineItem).apply(examples.shop.shop.types.ShoppingCart.tmpenumShoppingCart));

      examples.shop.shop.types.LineItem.getEnumeration();

      tmpenumShoppingCart.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.ShoppingCart>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.ShoppingCart>> _1() { return enumShoppingCart.parts(); }
      };

    }
    return enumShoppingCart;
  }

}
