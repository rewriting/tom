package examples.lists;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class GenericList {

  // ---------------- Return the first element ----------------------------------------------------
  public static <T> T first(List<T> list) {
	  if( !list.isEmpty() ) {
		  return list.get(0);
	  } else {
		  return null;
	  }
	}

  // ---------------- size ------------------------------------------------------------------------
	public static <T> int size(List<T> list) {
	  int size = 0;
	  if( !list.isEmpty() ){
			for(T iterator : list ){
				size++;
			}
		}
		return size;
	}

  // ---------------- get element by index --------------------------------------------------------
	public static <T> T get(List<T> list, int index) {
		if( !list.isEmpty() ) {
			int currIndex = 0;
			for(T iterator : list) {
				if(currIndex == index) {
					return iterator;
				}
				currIndex++;
			}
		}
		return null;
	}


  // ---------------- get index of element --------------------------------------------------------
	public static <T> int indexOf(List<T> list, T elem) {
		if( !list.isEmpty() ) {
			int currIndex = 0;
			for(T iterator : list ){
				if(iterator == elem){
					return currIndex;
				}
				currIndex++;
			}
		}
		return -1;
	}
	
  // ---------------- check if a list contains a given element ------------------------------------
	public static <T> boolean contains(List<T> list, T elem) {
		if( !list.isEmpty() ) {
			for(T iterator : list ){
				if(iterator == elem){
					return true;
				}
			}
		}
		return false;
	}

  // ---------------- remove a given element (specified by its index) from a list -----------------
	public static <T> List<T> remove(List<T> list, int index) {
		if( !list.isEmpty() && index < size(list) && index >= 0 ) {
			try{
				list.remove(index);
			}catch(UnsupportedOperationException e) {
				List copyList = new ArrayList<T>(list);
        copyList.remove(index);
				return copyList;
			}
		}
		return list;
	}

  // ---------------- reverseList -----------------------------------------------------------------
	public static <T> List<T> reverseList(List<T> list) {
		Collections.reverse(list);
		return list;
	}

  // ---------------- main ------------------------------------------------------------------------
  public static void main(String args[]) {

		List<String> listStr = new ArrayList<String>();
		listStr.add("zero");
		listStr.add("one");
		listStr.add("two");
		listStr.add("three");
	
		List<Integer> listInt = new ArrayList<Integer>();
		listInt.add(0);
		listInt.add(1);
		listInt.add(2);
		listInt.add(3);
		listInt.add(4);
		listInt.add(5);

		int index, n;
		String s;
	
		System.out.print("\n");

		System.out.println("------------------- First element --------------------------------------");
		System.out.println( "first" + listStr + "= " + first(listStr) );
		System.out.println( "first" + listInt + "= " + first(listInt) );

		System.out.println("------------------- Size -----------------------------------------------");
		System.out.println( "size" + listStr + "= " + size(listStr) );
		System.out.println( "size" + listInt + "= " + size(listInt) );

		System.out.println("------------------- Get element by index -------------------------------");

		for(index=0; index<size(listStr); index++){
			System.out.println( "get(" + listStr + "," + index + ") = " + get(listStr, index) );		
		}

		for(index=0; index<size(listInt); index++){
			System.out.println( "get(" + listInt + "," + index + ") = " + get(listInt, index) );
		}

		System.out.println("------------------- Get index of element -------------------------------");

		s = "four";
		System.out.println( "indexOf(" + listStr + "," + s + ") = " + indexOf(listStr,s) );

		n = 3;
		System.out.println( "indexOf(" + listInt + "," + n + ") = " + indexOf(listInt,n) );

		System.out.println("------------------- Contains -------------------------------------------");
		
		s = "three";
		System.out.println( "contains(" + listStr + "," + s + ") = " + contains(listStr,s) );

		n = 9;
		System.out.println( "contains(" + listInt + "," + n + ") = " + contains(listInt,n) );

		System.out.println("------------------- Remove ---------------------------------------------");

		index = 2;
		System.out.println( "remove(" + listStr + "," + index + ") = " + remove(listStr,index) );

		index = 1;
		System.out.println( "remove(" + listInt + "," + index + ") = " + remove(listInt,index) );

		System.out.println("------------------- ReverseList ----------------------------------------");

		System.out.println( "reverseList(" + listStr + ") = " + reverseList(listStr) );
		System.out.println( "List: " + listStr);

		System.out.println( "reverseList(" + listInt + ") = " + reverseList(listInt) );
		System.out.println( "List: " + listInt);

		System.out.print("\n");
  }

}
