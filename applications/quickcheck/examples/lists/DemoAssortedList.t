package examples.lists;

import examples.lists.assortedlist.types.*;
import examples.lists.assortedlist.types.asslist.*;
import examples.lists.assortedlist.types.value.*;

public class DemoAssortedList {

  %include { assortedlist/AssortedList.tom }
  
  // ---------------- Size ------------------------------------------------------------------------
	public static int sizeAssList(AssList list) {
		%match (list){
			cons() -> { return 0; }
			cons(v) -> { return sizeValue(`v); }
			cons(v,t*) -> { return sizeValue(`v) + sizeAssList(`t); }
		}
		return 0;
	}

	public static int sizeValue(Value v) {	
		%match (v){	
			bl(_)   -> { return 1; }
			cst(_)  -> { return 1; }
			var(v1) -> { return 1 + sizeValue(`v1); }
			name(_) -> { return 1; }
		}
		return 0;
	}

  // ---------------- Get -------------------------------------------------------------------------
	public static Value get(AssList list, int index){
		int currIndex = 0;
		return getAssList(list, index, currIndex);
	}

	public static Value getAssList(AssList list, int index, int currIndex) {
		%match(list){
			cons() -> { return null; }
			cons(v) -> {
				if(currIndex == index) { return `v; }
				else { return getValue(`v, index, currIndex); }
			}
			// If v1 == var(v:Value)
			cons(v1@var(_), l*) -> {
				if(currIndex == index) { return `v1; }
				else {
					Value val = `getValue(v1, index, currIndex);
					if(`val != null) { return `val; }
					else {
						currIndex += sizeValue(`v1);
						return getAssList(`l, index, currIndex);
					}
				}
			}
			// Else If v1 != var(v:Value)
			cons(v2, l*) -> {
				if(currIndex == index) { return `v2; }
				else { currIndex++; return getAssList(`l, index, currIndex); }
			}
		}
		return null;
	}

	public static Value getValue(Value val, int index, int currIndex){
		currIndex++;
		%match(val){
			var(v3) -> {
				if(currIndex == index){ return `v3; }
				else { return getValue(`v3, index, currIndex); }
			}
		}
		return null;
	}

  // ---------------- isEmpty ---------------------------------------------------------------------
	public static boolean isEmpty(AssList list) {
		%match (list){
			cons() -> {  return true; }
		}
		return false;
	}

  // ---------------- Pretty Printer --------------------------------------------------------------
	public static String prettyAssList(AssList list){ //, boolean notImportant) {
		%match (list){
			cons() -> { return "nil"; }
			cons(v) -> {  return `prettyValue(v); }
			cons(v, t*) -> { return `prettyValue(v) + "," + `prettyAssList(t); }
		}
		return "nil";
	}

	public static String prettyValue(Value v) {
		%match (v){	
			bl(b) -> { return Boolean.toString(`b); }
			cst(i) -> { return Integer.toString(`i); }
			var(v1) -> { return prettyValue(`v1); }
			name(s) -> { return `s; }
		}
		return "nil";
	}

  // ---------------- Add Last --------------------------------------------------------------------
	public static AssList addLast(AssList l, Value v) {
		return `cons(l*,v);
	}

	// ---------------- Get Index Of ----------------------------------------------------------------
	public static int getIndexOf(AssList list, Value val) {
		int currIndex = 0;
		return getIndexOf(list, val, currIndex);
	}

	public static int getIndexOf(AssList list, Value val, int currIndex) {
		%match (list){
			cons() -> { return -1; }
			cons(v) && v==val -> { return currIndex; }
      cons(v,_) && v==val -> { return currIndex; }
      cons(_,t*) -> { currIndex++; return getIndexOf(`t, val, currIndex); }
		}
		return -1;
	}

	// ---------------- Contains --------------------------------------------------------------------
	public static boolean contains(AssList list, Value val) {
		%match (list){
			cons() -> { return false; }
			cons(v) && v == val -> { return true; }
      cons(_,t*) -> { return contains(`t, val); }
		}
		return false;
	}

	// ---------------- removeAllOccurr -------------------------------------------------------------
	public static AssList removeAllOccurr(AssList list, Value val) {
		%match(list) {
			cons(x1*,v,x2*) && v==val -> {
				list = `cons(x1*,x2*);
				return removeAllOccurr(list, val);
			}
		}
		return list;
	}

	// ---------------- Get Content of Value --------------------------------------------------------
	public static Object getContent(Value v, int index) {
		int currIndex = 0;
		return getContent(v, index, currIndex);
	}

	public static Object getContent(Value v, int index, int currIndex) {
		%match (v){
			bl(b) -> {
				if(currIndex == index) { return `b;}
			}
			cst(i) -> {
				if(currIndex == index) { return `i; }
			}
			name(s) -> {
				if(currIndex == index) { return `s; }
			}
			var(v1) -> {
				if(currIndex == index) { return `v1; }
				else { currIndex++; return getContent(`v1, index, currIndex); }
			}
		}
		return null;
	}

	// ---------------- Is Bigger ---------------------------------------------------------------------
	public static boolean isBigger(AssList list, int constant) {
		return ( sizeAssList(list) > constant );
	}

  // ---------------- Main ------------------------------------------------------------------------
  public static void main(String args[]) {    

    AssList list1 = `cons(name("Assorted_List"),var(var(var(bl(true)))),cst(5));
    AssList list2 = `cons(var(var(var(bl(true)))), cst(5));
    AssList list3 = `cons();
    AssList list4 = `cons(bl(true), cst(5), name("list"));
    Value val = `var(var(var(bl(true)))); //`cst(7);
		int index = 0;

		System.out.print("\n");

		System.out.println("---------- AssLists and Values ---------------------------------------");
    System.out.println("list1: " + list1);
		System.out.println("list2: " + list2);
		System.out.println("list3: " + list3);
		System.out.println("list4: " + list4);
		System.out.println("val: " + val);
		
		System.out.println("---------- Pretty Printer --------------------------------------------");
		System.out.println("prettyAssList(list1) = " + prettyAssList(list1));
		System.out.println("prettyAssList(list2) = " + prettyAssList(list2));
		System.out.println("prettyAssList(list3) = " + prettyAssList(list3));
		System.out.println("prettyAssList(list4) = " + prettyAssList(list4));
		System.out.println("prettyValue(val) = " + prettyValue(val));

		System.out.println("---------- Size ------------------------------------------------------");
    System.out.println("size of list1: " + sizeAssList(list1));
    System.out.println("size of list2: " + sizeAssList(list2));
    System.out.println("size of list3: " + sizeAssList(list3));
    System.out.println("size of list4: " + sizeAssList(list4));
    System.out.println("size of val: " + sizeValue(val));

		System.out.println("---------- Get -------------------------------------------------------");
		
		for(index=0; index <= sizeAssList(list1); index++){
		  System.out.println( "index " + index + " of list1: " + get(list1,index) );
		  System.out.println( "index " + index + " of list2: " + get(list2,index) );
		  System.out.println( "index " + index + " of list3: " + get(list3,index) );
		  System.out.println( "index " + index + " of list4: " + get(list4,index) );
	    System.out.print("\n");
		}

		System.out.println("---------- isEmpty ---------------------------------------------------");
    System.out.println("isEmpty list1: " + isEmpty(list1) );
    System.out.println("isEmpty list2: " + isEmpty(list2) );
    System.out.println("isEmpty list3: " + isEmpty(list3) );
    System.out.println("isEmpty list4: " + isEmpty(list4) );

		System.out.println("---------- addLast ---------------------------------------------------");
    list1 = addLast(list1,val);
    list2 = addLast(list2,val);
    list4 = addLast(list4,val);

		System.out.println("list1 = " + list1);
		System.out.println("list2 = " + list2);
		System.out.println("list4 = " + list4);

		System.out.println("---------- Contains --------------------------------------------------");
    System.out.println("list1 contains value " + val + "? : " + contains(list1,val));
    System.out.println("list2 contains value " + val + "? : " + contains(list2,val));
	  System.out.println("list3 contains value " + val + "? : " + contains(list3,val));
	  System.out.println("list4 contains value " + val + "? : " + contains(list4,val));

		System.out.println("---------- getIndexOf ------------------------------------------------");
    System.out.println("index of " + val + " in list1 is " + getIndexOf(list1, val));
    System.out.println("index of " + val + " in list2 is " + getIndexOf(list2, val));
    System.out.println("index of " + val + " in list3 is " + getIndexOf(list3, val));
    System.out.println("index of " + val + " in list4 is " + getIndexOf(list4, val));

		System.out.println("---------- Remove all occurrences of an element from a AssList -------");
		list1 = removeAllOccurr(list1,val);
		list2 = removeAllOccurr(list2,val);
		list3 = removeAllOccurr(list3,val);
		list4 = removeAllOccurr(list4,val);

		System.out.println("list1 = " + list1);
		System.out.println("list2 = " + list2);
		System.out.println("list3 = " + list3);
		System.out.println("list4 = " + list4);

		System.out.println("---------- Get Content of a Value ------------------------------------");
		
		Value val1 = `bl(true);
		Value val2 = `cst(3);
		Value val3 = `var(var(var(cst(10))));
		Value val4 = `name("val4_content");
		
		for(index=0; index < 5; index++){
			System.out.println( "getContent(" + val1 + "," + index + ") = " + getContent(val1,index) );
			System.out.println( "getContent(" + val2 + "," + index + ") = " + getContent(val2,index) );
			System.out.println( "getContent(" + val3 + "," + index + ") = " + getContent(val3,index) );
			System.out.println( "getContent(" + val4 + "," + index + ") = " + getContent(val4,index) );
			System.out.print("\n");
		}

		System.out.println("---------- is Bigger -------------------------------------------------");
		int constant = 1;		
		System.out.println( "isBigger(list1," + constant + ") = " + isBigger(list1,constant) );
		System.out.println( "isBigger(list2," + constant + ") = " + isBigger(list2,constant) );
		System.out.println( "isBigger(list3," + constant + ") = " + isBigger(list3,constant) );
		System.out.println( "isBigger(list4," + constant + ") = " + isBigger(list4,constant) );
		
		System.out.print("\n");
  }

}
