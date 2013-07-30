package examples.lists;

import examples.lists.stringlist.types.*;

public class DemoStringList {

	%include{ stringlist/StringList.tom }

  // ---------------- prettyPrinter ---------------------------------------------------------------
  public static String prettyElem(Elem elem){
    %match(elem){
      single(str) -> { return `str; }
      many(str, list) -> { return `str + "(" + prettyListStr(`list) + ")"; }
    }
    return "";
  }

  public static String prettyListStr(ListStr list){
    %match(list){
			cons(e) -> { return prettyElem(`e); }
      cons(e, L*) -> { return prettyElem(`e) + "," + prettyListStr(`L); }
    }
    return "";
  }

  // ---------------- main ------------------------------------------------------------------------
  public static void main(String args[]) {

	  Elem e1 = `many("abc", cons(single("def")));
	  Elem e2 = `single("ghi");
		
		ListStr list1 = `cons(e1,e2);
		ListStr list2 = `cons(e2);

	  System.out.print("\n");

		System.out.println("------------------- Elem ------------------------------------------");
	  System.out.println("Elem e1: " + e1);
	  System.out.println("Elem e2: " + e2);

		System.out.println("------------------- Pretty Printer Elem ---------------------------");
    System.out.println( "prettyElem(e1): " + prettyElem(e1) );
    System.out.println( "prettyElem(e2): " + prettyElem(e2) );

		System.out.println("------------------- ListStr ---------------------------------------");
	  System.out.println("ListStr list1: " + list1);
	  System.out.println("ListStr list2: " + list2);

		System.out.println("------------------- Pretty Printer ListStr ------------------------");
    System.out.println( "prettyListStr(list1): " + prettyListStr(list1) );
    System.out.println( "prettyListStr(list2): " + prettyListStr(list2) );

//    Does not working

//		System.out.println("------------------- Use of the enumerator library -----------------");

//    Enumeration<ListStr> enumLS = ListStr.getEnumeration();
//    Enumeration<Elem> enumE = Elem.getEnumeration();

//		System.out.println("Enumerator for ListStr:");
//		int i = 0;
//		for (i = 0; i < 10; i++) {
//	  	System.out.println("Get " + i + "th term: " + enumLS.get(BigInteger.valueOf(i)));
//	  }

//		System.out.println("Enumerator for Elem:");
//		for (i = 0; i < 10; i++) {
//	  	System.out.println("Get " + i + "th term: " + enumE.get(BigInteger.valueOf(i)));
//	  }

	  System.out.print("\n");
  }

}
