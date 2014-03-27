package examples;

import java.math.BigInteger;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import examples.data.mutual.types.A;
import examples.data.mutual.types.B;

public class Mutual {

	public static void main(String args[]) {
		/**
		 * this example use the Gom generator for the signature
		 * A = a() | foo(b:B) | hoo(a:A, b:B)
		 * B = b() | grr(a:A)
		 * 
		 * just call A.getEnumeration() to enumerate sort A
		 */

		LazyList<Finite<Integer>> sparts = Combinators.makeInteger().parts();
		for (int i = 0; i < 5 && !sparts.isEmpty(); i++) {
			System.out.println(i + " --> " + sparts.head());
			sparts = sparts.tail();
		}

		testEnumerator(Combinators.makeInteger(), "int", 5);
		testEnumerator(Combinators.makeString(), "String", 5);
		testEnumerator(A.getEnumeration(), "A", 10);
		testEnumerator(B.getEnumeration(), "B", 10);

		Enumeration<A> enumA = A.getEnumeration(); 
		System.out.println("#trees of size 500 = card(parts[500]) = " + enumA.parts().index(BigInteger.valueOf(500)).getCard());
		Enumeration<B> enumB = B.getEnumeration();
		System.out.println("#trees of size 500 = card(parts[500]) = " + enumB.parts().index(BigInteger.valueOf(500)).getCard());

	}

	private static void testEnumerator(Enumeration<?> e,String title, int n) {
		System.out.println("Enumerator for " + title);
		for (int i = 0; i < n; i++) {
			System.out.println("Get " + i + "th term: " + e.get(BigInteger.valueOf(i)));
		}
	}

}
