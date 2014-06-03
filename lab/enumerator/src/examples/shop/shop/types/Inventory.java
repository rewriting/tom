
package examples.shop.shop.types;
import examples.shop.*;
	import java.util.*;


public abstract class Inventory extends examples.shop.shop.ShopAbstractType  {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected Inventory() {}

private static boolean tom_equal_term_Collection(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_Collection(Object t) {return  t instanceof java.util.Collection ;} 
	
	public examples.shop.Inventory translateInventory() {
		examples.shop.Inventory inv = new examples.shop.Inventory();
		buildInventory(this, inv);
		return inv;
	}
	
	private void buildInventory(Inventory inventory, examples.shop.Inventory inv) {
		{{if (tom_is_sort_Inventory(inventory)) {if (tom_is_sort_Inventory((( examples.shop.shop.types.Inventory )inventory))) {if (tom_is_fun_sym_Inv((( examples.shop.shop.types.Inventory )(( examples.shop.shop.types.Inventory )inventory)))) {

				examples.shop.LineItem li = tom_get_slot_Inv_LineItem((( examples.shop.shop.types.Inventory )inventory)).translateLineItem(); 
				inv.add(li.getItem(), li.getQuantity());
				buildInventory(tom_get_slot_Inv_Inventory((( examples.shop.shop.types.Inventory )inventory)), inv);
			}}}}}

		return;
	}


  /**
   * Returns true if the term is rooted by the symbol EmptyInv
   *
   * @return true if the term is rooted by the symbol EmptyInv
   */
  public boolean isEmptyInv() {
    return false;
  }

  /**
   * Returns true if the term is rooted by the symbol Inv
   *
   * @return true if the term is rooted by the symbol Inv
   */
  public boolean isInv() {
    return false;
  }

  /**
   * Returns the subterm corresponding to the slot LineItem
   *
   * @return the subterm corresponding to the slot LineItem
   */
  public examples.shop.shop.types.LineItem getLineItem() {
    throw new UnsupportedOperationException("This Inventory has no LineItem");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot LineItem
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot LineItem is replaced by _arg
   */
  public Inventory setLineItem(examples.shop.shop.types.LineItem _arg) {
    throw new UnsupportedOperationException("This Inventory has no LineItem");
  }

  /**
   * Returns the subterm corresponding to the slot Inventory
   *
   * @return the subterm corresponding to the slot Inventory
   */
  public examples.shop.shop.types.Inventory getInventory() {
    throw new UnsupportedOperationException("This Inventory has no Inventory");
  }

  /**
   * Returns a new term where the subterm corresponding to the slot Inventory
   * is replaced by the term given in argument.
   * Note that there is no side-effect: a new term is returned and the original term is left unchanged
   *
   * @param _arg the value of the new subterm
   * @return a new term where the subterm corresponding to the slot Inventory is replaced by _arg
   */
  public Inventory setInventory(examples.shop.shop.types.Inventory _arg) {
    throw new UnsupportedOperationException("This Inventory has no Inventory");
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
   * Returns a examples.shop.shop.types.Inventory from an ATerm without any conversion
   *
   * @param trm ATerm to handle to retrieve a Gom term
   * @return the term from the ATerm
   */
  public static examples.shop.shop.types.Inventory fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  /**
   * Returns a examples.shop.shop.types.Inventory from a String without any conversion
   *
   * @param s String containing the ATerm
   * @return the term from the String
   */
  public static examples.shop.shop.types.Inventory fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  /**
   * Returns a examples.shop.shop.types.Inventory from a Stream without any conversion
   *
   * @param stream stream containing the ATerm
   * @return the term from the Stream
   * @throws java.io.IOException if a problem occurs with the stream
   */
  public static examples.shop.shop.types.Inventory fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  /**
   * Apply a conversion on the ATerm and returns a examples.shop.shop.types.Inventory
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATermConverter used to convert the ATerm
   * @return the Gom term
   * @throws IllegalArgumentException
   */
  public static examples.shop.shop.types.Inventory fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    aterm.ATerm convertedTerm = atConv.convert(trm);
    examples.shop.shop.types.Inventory tmp;
    java.util.ArrayList<examples.shop.shop.types.Inventory> results = new java.util.ArrayList<examples.shop.shop.types.Inventory>();

    tmp = examples.shop.shop.types.inventory.EmptyInv.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    tmp = examples.shop.shop.types.inventory.Inv.fromTerm(convertedTerm,atConv);
    if(tmp!=null) {
      results.add(tmp);
    }
    switch(results.size()) {
      case 0:
        throw new IllegalArgumentException(trm + " is not a Inventory");
      case 1:
        return results.get(0);
      default:
        java.util.logging.Logger.getLogger("Inventory").log(java.util.logging.Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {results.toString(), "examples.shop.shop.types.Inventory", results.get(0).toString()});
        return results.get(0);
    }
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a examples.shop.shop.types.Inventory from it
   *
   * @param s String containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.Inventory fromString(String s, tom.library.utils.ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  /**
   * Apply a conversion on the ATerm contained in the Stream and returns a examples.shop.shop.types.Inventory from it
   *
   * @param stream stream containing the ATerm
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.shop.shop.types.Inventory fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {
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
  public examples.shop.shop.types.Inventory reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  
private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_Inventory(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Inventory(Object t) {return  (t instanceof examples.shop.shop.types.Inventory) ;}private static boolean tom_equal_term_ShoppingCart(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ShoppingCart(Object t) {return  (t instanceof examples.shop.shop.types.ShoppingCart) ;}private static boolean tom_equal_term_LineItem(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_LineItem(Object t) {return  (t instanceof examples.shop.shop.types.LineItem) ;}private static boolean tom_equal_term_Item(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Item(Object t) {return  (t instanceof examples.shop.shop.types.Item) ;}private static boolean tom_is_fun_sym_Inv( examples.shop.shop.types.Inventory  t) {return  (t instanceof examples.shop.shop.types.inventory.Inv) ;}private static  examples.shop.shop.types.LineItem  tom_get_slot_Inv_LineItem( examples.shop.shop.types.Inventory  t) {return  t.getLineItem() ;}private static  examples.shop.shop.types.Inventory  tom_get_slot_Inv_Inventory( examples.shop.shop.types.Inventory  t) {return  t.getInventory() ;}







































































  /*
   * Initialize the (cyclic) data-structure
   * in order to generate/enumerate terms
   */

  protected static tom.library.enumerator.Enumeration<examples.shop.shop.types.Inventory> enumInventory = null;
  public static final tom.library.enumerator.Enumeration<examples.shop.shop.types.Inventory> tmpenumInventory = new tom.library.enumerator.Enumeration<examples.shop.shop.types.Inventory>((tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.Inventory>>) null);

  public static tom.library.enumerator.Enumeration<examples.shop.shop.types.Inventory> getEnumeration() {
    if(enumInventory == null) { 
      enumInventory = examples.shop.shop.types.inventory.EmptyInv.funMake().apply(examples.shop.shop.types.Inventory.tmpenumInventory)
        .plus(examples.shop.shop.types.inventory.Inv.funMake().apply(examples.shop.shop.types.LineItem.tmpenumLineItem).apply(examples.shop.shop.types.Inventory.tmpenumInventory));

      examples.shop.shop.types.LineItem.getEnumeration();

      tmpenumInventory.p1 = new tom.library.enumerator.P1<tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.Inventory>>>() {
        public tom.library.enumerator.LazyList<tom.library.enumerator.Finite<examples.shop.shop.types.Inventory>> _1() { return enumInventory.parts(); }
      };

    }
    return enumInventory;
  }

}
