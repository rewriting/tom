package examples.lists;

import examples.lists.assortedlist.types.*;

public class DemoAssortedList {

  %include { assortedlist/AssortedList.tom }
  
  // ---------------- Size -----------------------------------------------------------------------
	public static int sizeAssList(AssList list) {
		%match (list){
			cons() -> {  return 0; }
			cons(v) -> { return sizeValue(`v); }
			cons(v,t*) -> { return sizeValue(`v) + sizeAssList(`t); }
		}
		return 0;
	}

	public static int sizeValue(Value v) {	
		%match (v){	
			bl(_) -> {  return 1; }
			cst(_) -> { return 1; }
			var(v1) -> { return 1 + sizeValue(`v1); }
			name(_) -> { return 1; }
		}
		return 0;
	}

  // ---------------- Get -----------------------------------------------------------------------
	public static Object get(AssList list, int index) {
		int currIndex = 0;
		return get(list, index, currIndex);
	}
	
	public static Object get(AssList list, int index, int currIndex) {
		%match (list){
			cons() -> {  return null; }
			cons(v) -> {
				if(currIndex == index) { return `v; }
				else { return null; }
			}
			cons(v, t*) -> { 
				if(currIndex == index) { return `v; }
        else { currIndex++; return get(`t, index, currIndex); }
			}
		}
		return null;
	}

  // ---------------- isEmpty --------------------------------------------------------------------
	public static boolean isEmpty(AssList list) {
		%match (list){
			cons() -> {  return true; }
		}
		return false;
	}

  // ---------------- Pretty Printer -------------------------------------------------------------
	public static String prettyAssList(AssList list){ //, boolean notImportant) {
		%match (list){
			cons() -> { return ""; }
			cons(v) -> {  return `prettyValue(v); }
			cons(v, t*) -> { return `prettyValue(v) + "," + `prettyAssList(t); }
		}
		return null;
	}

	public static String prettyValue(Value v) {
		%match (v){	
			bl(b) -> { return Boolean.toString(`b); }
			cst(i) -> { return Integer.toString(`i); }
			var(v1) -> { return prettyValue(`v1); }
			name(s) -> { return `s; }
		}
		return "";
	}

  // ---------------- Add Last -----------------------------------------------------------------
	public static AssList addLast(AssList l, Value v) {
		return `cons(l*,v);
	}

  // ---------------- Remove All --------------------------------------------------------------
	public static AssList removeAll(AssList l, Value val) {
		%match(l) {
			cons(x1*,v,x2*) && v == val -> {AssList l2 = `cons(x1*,x2*); 
                                      return l2; }
		}
		return l;
	}



  // ---------------- Main --------------------------------------------------------------------
  public static void main(String args[]) {    

    AssList list1 = `cons(bl(false),bl(true),cst(5),var(cst(7)),name("Assorted_List"));
    AssList list2 = `cons(var(var(var(bl(true)))));
    AssList list3 = `cons();
    AssList list4 = `cons(bl(true), cst(5));
    Value val = `cst(7);

		System.out.println("\n------------------- AssLists and Values ----------------------------");    
    System.out.println("list1: " + list1);
		System.out.println("list2: " + list2);
		System.out.println("list3: " + list3);
		System.out.println("list4: " + list4);
		System.out.println("val: " + val);
		
		System.out.println("------------------- Pretty Printer -----------------------------------");
		System.out.println("prettyAssList(list1) = " + prettyAssList(list1));
		System.out.println("prettyAssList(list2) = " + prettyAssList(list2));
		System.out.println("prettyAssList(list3) = " + prettyAssList(list3));
		System.out.println("prettyAssList(list4) = " + prettyAssList(list4));
		System.out.println("prettyValue(val) = " + prettyValue(val));

		System.out.println("------------------- Size ---------------------------------------------");
    System.out.println("size of list1: " + sizeAssList(list1));
    System.out.println("size of list2: " + sizeAssList(list2));
    System.out.println("size of list3: " + sizeAssList(list3));
    System.out.println("size of list4: " + sizeAssList(list4));
    System.out.println("size of val: " + sizeValue(val));

		System.out.println("------------------- Get ----------------------------------------------");
		int index = 1;
    System.out.println( "index " + index + " of list1: " + get(list1,index) );
    System.out.println( "index " + index + " of list2: " + get(list2,index) );
    System.out.println( "index " + index + " of list3: " + get(list3,index) );
    System.out.println( "index " + index + " of list4: " + get(list4,index) );

		System.out.println("------------------- isEmpty ----------------------------------------------");
    System.out.println("isEmpty list1: " + isEmpty(list1) );
    System.out.println("isEmpty list2: " + isEmpty(list2) );
    System.out.println("isEmpty list3: " + isEmpty(list3) );
    System.out.println("isEmpty list4: " + isEmpty(list4) );

		System.out.println("------------------- addLast ---------------------------------------------");
    list1 = addLast(list1,val);
    list2 = addLast(list2,val);
    list3 = addLast(list3,val);
    list4 = addLast(list4,val);

		System.out.println("prettyAssList(list1) = " + prettyAssList(list1));
		System.out.println("prettyAssList(list2) = " + prettyAssList(list2));
		System.out.println("prettyAssList(list3) = " + prettyAssList(list3));
		System.out.println("prettyAssList(list4) = " + prettyAssList(list4));

    list1 = addLast(list1,val);
    list1 = addLast(list1,val);
    list1 = addLast(list1,val);
    list1 = addLast(list1,val);
		
		System.out.println("prettyAssList(list1) = " + prettyAssList(list1));

  }

}
