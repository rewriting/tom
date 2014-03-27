package examples;

import java.math.BigInteger;

import tom.library.enumerator.*;
import examples.data.conslist.types.*;

// Enumerator for lists
public class ConsList {

  /*
        This example use the Gom generator for the signature

				List = nil()
						 | cons(e:Elem, l:List)

				Elem = a()
						 | b()
						 | nat(i:int)
 
  */

    private static void testEnumerator(Enumeration<?> e, String title, int n) {
        System.out.println("Enumerator for " + title);
        for (int i = 0; i < n; i++) {
            System.out.println("Get " + i + "th term: " + e.get(BigInteger.valueOf(i)));
        }
    }

    public static void main(String[] args) {

        testEnumerator(List.getEnumeration(), "List", 10);
        testEnumerator(Elem.getEnumeration(), "Elem", 10);

        Enumeration<List> enumList = List.getEnumeration();
        System.out.println("#Lists of size 10 = card(parts[10]) = " + enumList.parts().index(BigInteger.valueOf(10)).getCard());

        Enumeration<Elem> enumElem = Elem.getEnumeration();
        System.out.println("#Lists of size 10 = card(parts[10]) = " + enumElem.parts().index(BigInteger.valueOf(10)).getCard());

    }

}
