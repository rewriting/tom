package examples.lists;

import examples.lists.alist.types.*;
import examples.lists.alist.types.alist.*;
import examples.lists.alist.types.elem.*;

public class DemoAList {

	private static boolean tom_equal_term_boolean(boolean t1, boolean t2) {return  t1==t2 ;}private static boolean tom_is_sort_boolean(boolean t) {return  true ;} private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_AList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AList(Object t) {return  (t instanceof examples.lists.alist.types.AList) ;}private static boolean tom_equal_term_Elem(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Elem(Object t) {return  (t instanceof examples.lists.alist.types.Elem) ;}private static boolean tom_is_fun_sym_empty( examples.lists.alist.types.AList  t) {return  (t instanceof examples.lists.alist.types.alist.empty) ;}private static  examples.lists.alist.types.AList  tom_make_empty() { return  examples.lists.alist.types.alist.empty.make() ;}private static boolean tom_is_fun_sym_con( examples.lists.alist.types.AList  t) {return  (t instanceof examples.lists.alist.types.alist.con) ;}private static  examples.lists.alist.types.AList  tom_make_con( examples.lists.alist.types.Elem  t0,  examples.lists.alist.types.AList  t1) { return  examples.lists.alist.types.alist.con.make(t0, t1) ;}private static  examples.lists.alist.types.Elem  tom_get_slot_con_head( examples.lists.alist.types.AList  t) {return  t.gethead() ;}private static  examples.lists.alist.types.AList  tom_get_slot_con_tail( examples.lists.alist.types.AList  t) {return  t.gettail() ;}private static boolean tom_is_fun_sym_cs( examples.lists.alist.types.Elem  t) {return  (t instanceof examples.lists.alist.types.elem.cs) ;}private static  examples.lists.alist.types.Elem  tom_make_cs( int  t0) { return  examples.lists.alist.types.elem.cs.make(t0) ;}private static  int  tom_get_slot_cs_i( examples.lists.alist.types.Elem  t) {return  t.geti() ;}  

	/**
	 * Pretty prints
	 */
	public static String prettyAList(AList list) {  
		{{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_empty((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
  return "nil"; }}}}{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_con((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
 return prettyElem(tom_get_slot_con_head((( examples.lists.alist.types.AList )list)))+":"+prettyAList(tom_get_slot_con_tail((( examples.lists.alist.types.AList )list))); }}}}}

		return "";
	}

	public static String prettyElem(Elem elem) {  
		{{if (tom_is_sort_Elem(elem)) {if (tom_is_sort_Elem((( examples.lists.alist.types.Elem )elem))) {if (tom_is_fun_sym_cs((( examples.lists.alist.types.Elem )(( examples.lists.alist.types.Elem )elem)))) {
  return tom_get_slot_cs_i((( examples.lists.alist.types.Elem )elem))+""; }}}}}

		return "";
	}

	/**
	 * Returns size of a list
	 */
	public static int size(AList list) {  
		{{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_empty((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
  return 0; }}}}{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_con((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
 return 1 + size(tom_get_slot_con_tail((( examples.lists.alist.types.AList )list))); }}}}}

		return -1;
	}

	/**
	 * Adds an element as the first element of a list
	 */
	public static AList addFirst(AList list, Elem el) {
		return tom_make_con(el,list);
	}

	/**
	 * Returns index of an element
	 */
	public static int getIndexOf(AList list, Elem el) {
		int currIndex = 0;
		return getIndexOf(list, el, currIndex);
	}

	public static int getIndexOf(AList list, Elem e, int currIndex) {
		{{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_empty((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
 return -1; }}}}{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_con((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {if (tom_equal_term_Elem(e, tom_get_slot_con_head((( examples.lists.alist.types.AList )list)))) {
 return currIndex; }}}}}{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_con((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
 currIndex++; return getIndexOf(tom_get_slot_con_tail((( examples.lists.alist.types.AList )list)), e, currIndex); }}}}}

		return -1;
	}

	/**
	 * Returns true if the given list contains the given element
	 */
	public static boolean contains(AList list, Elem el) {
		{{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_empty((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
 return false; }}}}{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_con((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {if (tom_equal_term_Elem(el, tom_get_slot_con_head((( examples.lists.alist.types.AList )list)))) {
 return true; }}}}}{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_con((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
 return contains(tom_get_slot_con_tail((( examples.lists.alist.types.AList )list)), el); }}}}}

		return false;
	}

	/**
	 * Returns the element at the given index 
	 */
	public static Elem get(AList list, int index) {
		int currIndex = 0;
		return get(list, index, currIndex);
	}

	public static Elem get(AList list, int index, int currIndex) {
		{{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_empty((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
 return null; }}}}{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_con((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {

				if(index == currIndex) { return tom_get_slot_con_head((( examples.lists.alist.types.AList )list)); }
				else { currIndex++; return get(tom_get_slot_con_tail((( examples.lists.alist.types.AList )list)), index, currIndex); }
			}}}}}

		return null;
	}

	/**
	 * Checks whether the given element is null
	 */
	public static boolean isNull(Elem el) {
		return ( el == null );
	}

	/**
	 * Checks whether the given element is empty
	 */
	public static boolean isEmpty(AList list) {
		{{if (tom_is_sort_AList(list)) {if (tom_is_sort_AList((( examples.lists.alist.types.AList )list))) {if (tom_is_fun_sym_empty((( examples.lists.alist.types.AList )(( examples.lists.alist.types.AList )list)))) {
  return true; }}}}}

		return false;
	}

	/**
	 * Checks whether two given elements are equal
	 */
	public static boolean isEqual(Elem el1, Elem el2){

		// We have to treat null pointers separetely
		if(el1 == null && el2 == null) { return true; }
		else if(el1 == null && el2 != null) { return false; }
		else if(el1 != null && el2 == null) { return false; }

		// If either are not null
		else if( (el1).compareTo(el2) == 0 ) { return true; }
		else { return false; }
	}

	/**
	 * Returns zero
	 */
	public static int zero(){
		return 0;
	}

	/**
	 * Returns parameter + 1
	 */
	public static int plusOne(int size) {
		size = size + 1;
		return size;
	}

	/**
	 * Checks whether the parameter is negative 
	 */
	public static boolean isNegative(int index) {
		return (index < 0);
	}

	/**
	 * Checks whether index is less than size
	 */
	public static boolean isLess(int index, int size) {
		return (index < size);
	}

	public static void main(String args[]) {    

		AList alist1 = tom_make_con(tom_make_cs(4),tom_make_con(tom_make_cs(3),tom_make_con(tom_make_cs(5),tom_make_con(tom_make_cs(8),tom_make_empty()))));
		AList alist2 = tom_make_con(tom_make_cs(0),tom_make_empty());
		AList alist3 = tom_make_empty();
		Elem elem;
		int index	= 0;
		boolean b;

		System.out.print("\n");
		System.out.println("------------------- ALists ------------------------------------------");     
		System.out.println("alist1: " + alist1);
		System.out.println("alist2: " + alist2);
		System.out.println("alist3: " + alist3);

		System.out.println("------------------- Pretty Printer ----------------------------------");
		System.out.println("prettyAList(alist1): " + prettyAList(alist1));
		System.out.println("prettyAList(alist2): " + prettyAList(alist2));
		System.out.println("prettyAList(alist3): " + prettyAList(alist3));

		System.out.println("------------------- Size --------------------------------------------");
		System.out.println("size(alist1): " + size(alist1));
		System.out.println("size(alist2): " + size(alist2));
		System.out.println("size(alist3): " + size(alist3));

		System.out.println("------------------- Add First ---------------------------------------");
		elem = tom_make_cs(7);
		alist1 = addFirst(alist1,elem);
		alist2 = addFirst(alist2,elem);
		System.out.println("prettyAList(alist1): " + prettyAList(alist1));
		System.out.println("prettyAList(alist2): " + prettyAList(alist2));

		System.out.println("------------------- Size & Add First --------------------------------");
		b = ( size(alist1) + 1 == size(addFirst(alist1,tom_make_cs(9))) );
		System.out.println("Is size(alist1) + 1 == size(addFirst(alist1,`cs(9))) ?");
		System.out.println("Response: " + b);

		System.out.println("------------------- Get Index Of Element ----------------------------");
		elem = tom_make_cs(8);
		System.out.println("getIndexOf(alist1, " + elem + "):" + getIndexOf(alist1,elem));

		System.out.println("------------------- Contains Elem -----------------------------------");
		System.out.println("contains(alist1, " + elem + ") ?");
		System.out.println("Response: " +  contains(alist1, elem));

		System.out.println("------------------- Get ---------------------------------------------");
		index = 0;
		System.out.println( "get(alist1," + index + "): " + get(alist1,index) );
		System.out.println( "get(alist2," + index + "): " + get(alist2,index) );
		System.out.println( "get(alist3," + index + "): " + get(alist3,index) );


		index = 1;
		System.out.println( "get(alist1," + index + "): " + get(alist1,index) );
		System.out.println( "get(alist2," + index + "): " + get(alist2,index) );
		System.out.println( "get(alist3," + index + "): " + get(alist3,index) );

		index = 2;
		System.out.println( "get(alist1," + index + "): " + get(alist1,index) );
		System.out.println( "get(alist2," + index + "): " + get(alist2,index) );
		System.out.println( "get(alist3," + index + "): " + get(alist3,index) );

		System.out.println("------------------- Is Null -----------------------------------------");
		index = 0;
		System.out.println("isNull(get(alist1," + index + ")): " + isNull(get(alist1,index)));

		index = 2;
		System.out.println("isNull(get(alist2," + index + ")): " + isNull(get(alist2,index)));

		System.out.println("------------------- Get & Add First ---------------------------------");
		b = get(addFirst(alist1,tom_make_cs(7)),0) == tom_make_cs(7);
		System.out.println("Is get(addFirst(alist1,`cs(7)),0) == `cs(7) ?");
		System.out.println("Response: " + b);

		System.out.println("------------------- Is Empty ----------------------------------------");
		b = isEmpty(alist3);
		System.out.println("Is alist3 empty ? " + b);

		System.out.println("------------------- Is Equals ---------------------------------------");
		int index1 = 0, index2 = 0;
		b = isEqual( get(alist1,index1), get(alist2,index2) );
		System.out.println("Is " + get(alist1,index1) + " equal to " + get(alist2,index2) + "?");
		System.out.println("Response: " + b);

		System.out.print("\n");
	}

}
