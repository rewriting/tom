
package examples.shop.shop.types;
import examples.shop.*;
	import java.util.*;


public abstract class LineItem extends examples.shop.shop.ShopAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected LineItem() {}

public examples.shop.LineItem translateLineItem() {
		{{if (tom_is_sort_LineItem(this)) {if (tom_is_sort_LineItem((( examples.shop.shop.types.LineItem )this))) {if (tom_is_fun_sym_Line((( examples.shop.shop.types.LineItem )(( examples.shop.shop.types.LineItem )this)))) {
 return new examples.shop.LineItem(tom_get_slot_Line_Item((( examples.shop.shop.types.LineItem )this)).translateItem(), tom_get_slot_Line_Quantity((( examples.shop.shop.types.LineItem )this))); }}}}}
 
		return null;
	}


  /**
   * Returns true if the term is rooted by the symbol Line
   *
   * @return true if the term is rooted by the symbol Line
   */
  public boolean isLine() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot Item
   *
   * @return the subterm corresponding to the slot Item
   */
  public examples.shop.shop.types.Item getItem() {
    throw new UnsupportedOperationException("This LineItem has no Item");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Item
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Item is replaced by _arg
   */
  public LineItem setItem(examples.shop.shop.types.Item _arg) {
    throw new UnsupportedOperationException("This LineItem has no Item");
  }

  /**
   * Returns the subterm corresponding to the slot Quantity
   *
   * @return the subterm corresponding to the slot Quantity
   */
  public int getQuantity() {
    throw new UnsupportedOperationException("This LineItem has no Quantity");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Quantity
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Quantity is replaced by _arg
   */
  public LineItem setQuantity(int _arg) {
    throw new UnsupportedOperationException("This LineItem has no Quantity");
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
   * Returns a examples.shop.shop.types.LineItem from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.shop.shop.types.LineItem fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.shop.shop.types.LineItem from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.shop.shop.types.LineItem fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.shop.shop.types.LineItem from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.shop.shop.types.LineItem fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.shop.shop.types.LineItem
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.shop.shop.types.LineItem fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.shop.shop.types.LineItem tmp;
    java.util.ArrayList<examples.shop.shop.types.LineItem> results = new java.util.ArrayList<examples.shop.shop.types.LineItem>();

    tmp = examples.shop.shop.types.lineitem.Line.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a LineItem");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("LineItem").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.shop.shop.types.LineItem", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.shop.shop.types.LineItem from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.LineItem fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.shop.shop.types.LineItem from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.LineItem fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.shop.shop.types.LineItem reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_Inventory(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Inventory(Object t) {return  (t instanceof examples.shop.shop.types.Inventory) ;}private static boolean tom_equal_term_ShoppingCart(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ShoppingCart(Object t) {return  (t instanceof examples.shop.shop.types.ShoppingCart) ;}private static boolean tom_equal_term_LineItem(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_LineItem(Object t) {return  (t instanceof examples.shop.shop.types.LineItem) ;}private static boolean tom_equal_term_Item(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Item(Object t) {return  (t instanceof examples.shop.shop.types.Item) ;}private static boolean tom_is_fun_sym_Line( examples.shop.shop.types.LineItem  t) {return  (t instanceof examples.shop.shop.types.lineitem.Line) ;}private static  examples.shop.shop.types.Item  tom_get_slot_Line_Item( examples.shop.shop.types.LineItem  t) {return  t.getItem() ;}private static  int  tom_get_slot_Line_Quantity( examples.shop.shop.types.LineItem  t) {return  t.getQuantity() ;}







































































  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem> enumLineItem = null;
  public static final tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem> tmpenumLineItem = new tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.LineItem>>) null);

  public static tom.library.enumerator.Enumeration<examples.shop.shop.types.LineItem> getEnumeration() {
    if(enumLineItem == null) { 
      enumLineItem = examples.shop.shop.types.lineitem.Line.funMake().apply(examples.shop.shop.types.Item.tmpenumItem).apply(tom.library.enumerator.Combinators.makeint());

      examples.shop.shop.types.Item.getEnumeration();

      tmpenumLineItem.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.LineItem>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.LineItem>> _1() { return enumLineItem.parts(); }
      };

    }
    return enumLineItem;
  }

}
