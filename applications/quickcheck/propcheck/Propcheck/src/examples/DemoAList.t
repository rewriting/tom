package examples.lists;

import examples.lists.alist.types.*;
import examples.lists.alist.types.alist.*;
import examples.lists.alist.types.elem.*;

public class DemoAList {

	%include { alist/AList.tom }

	/**
	 * Pretty prints
	 */
	public static String prettyAList(AList list) {  
		%match (list){
			empty() -> {  return "nil"; }
			con(head,tail) -> { return `prettyElem(head)+":"+`prettyAList(tail); }
		}
		return "";
	}

	public static String prettyElem(Elem elem) {  
		%match (elem){
			cs(n) -> {  return `n+""; }
		}
		return "";
	}

	/**
	 * Returns size of a list
	 */
	public static int size(AList list) {  
		%match (list){
			empty() -> {  return 0; }
			con(_,tail) -> { return 1 + `size(tail); }
		}
		return -1;
	}

	/**
	 * Adds an element as the first element of a list
	 */
	public static AList addFirst(AList list, Elem el) {
		return `con(el,list);
	}

	/**
	 * Returns index of an element
	 */
	public static int getIndexOf(AList list, Elem el) {
		int currIndex = 0;
		return getIndexOf(list, el, currIndex);
	}

	public static int getIndexOf(AList list, Elem e, int currIndex) {
		%match(list) {
			empty() -> { return -1; }
			con(el,_) && el == e -> { return currIndex; }
			con(_,tail) -> { currIndex++; return getIndexOf(`tail, e, currIndex); }
		}
		return -1;
	}

	/**
	 * Returns true if the given list contains the given element
	 */
	public static boolean contains(AList list, Elem el) {
		%match (list){
			empty() -> { return false; }
			con(e,_) && e==el -> { return true; }
			con(_,tail) -> { return contains(`tail, el); }
		}
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
		%match(list) {
			empty() -> { return null; }
			con(el,tail) -> {
				if(index == currIndex) { return `el; }
				else { currIndex++; return get(`tail, index, currIndex); }
			}
		}
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
		%match (list){
			empty() -> {  return true; }
		}
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

		AList alist1 = `con(cs(4),con(cs(3),con(cs(5),con(cs(8),empty()))));
		AList alist2 = `con(cs(0),empty());
		AList alist3 = `empty();
		Elem elem;
		int index	= 0;
		boolean b;

		System.out.print("\n");
		System.out.println("------------------- ALists ------------------------------------------");     
		System.out.println("alist1: " + alist1);
		System.out.println("alist2: " + alist2);
		System.out.println("alist3: " + alist3);

		System.out.println("------------------- Pretty Printer ----------------------------------");
		System.out.println("prettyAList(alist1): " + prettyAList(`alist1));
		System.out.println("prettyAList(alist2): " + prettyAList(`alist2));
		System.out.println("prettyAList(alist3): " + prettyAList(`alist3));

		System.out.println("------------------- Size --------------------------------------------");
		System.out.println("size(alist1): " + size(`alist1));
		System.out.println("size(alist2): " + size(`alist2));
		System.out.println("size(alist3): " + size(`alist3));

		System.out.println("------------------- Add First ---------------------------------------");
		elem = `cs(7);
		alist1 = `addFirst(alist1, elem);
		alist2 = `addFirst(alist2, elem);
		System.out.println("prettyAList(alist1): " + prettyAList(`alist1));
		System.out.println("prettyAList(alist2): " + prettyAList(`alist2));

		System.out.println("------------------- Size & Add First --------------------------------");
		b = ( size(alist1) + 1 == size(addFirst(alist1,`cs(9))) );
		System.out.println("Is size(alist1) + 1 == size(addFirst(alist1,`cs(9))) ?");
		System.out.println("Response: " + b);

		System.out.println("------------------- Get Index Of Element ----------------------------");
		elem = `cs(8);
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
		b = get(addFirst(alist1,`cs(7)),0) == `cs(7);
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
