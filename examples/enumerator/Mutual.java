package enumerator;

import java.math.BigInteger;
import tom.library.enumerator.*;

import enumerator.mutual.types.*;

public class Mutual {

  public static void main(String args[]) {
    /**
     * module enumerator.Mutual
     * abstract syntax
     * 
     * A = a() | foo(b:B) | hoo(a:A)
     * B = b() | grr(a:A)
     */

    System.out.println("START");

    Enumeration<A> enumA = A.getEnumeration(); 
    System.out.println("#trees of size 500 = card(parts[500]) = " + enumA.parts().index(BigInteger.valueOf(500)).getCard());
    Enumeration<B> enumB = B.getEnumeration();
    System.out.println("#trees of size 500 = card(parts[500]) = " + enumB.parts().index(BigInteger.valueOf(500)).getCard());

    //listEnum.pay();
    System.out.println("Enumerator for " + "A");
    int n = 5;
    for (int i = 0; i < n; i++) {
      System.out.println("Get " + i + "th term: "
          + enumA.get(BigInteger.valueOf(i)));
    }
    System.out.println("Enumerator for " + "B");
    for (int i = 0; i < n; i++) {
      System.out.println("Get " + i + "th term: "
          + enumB.get(BigInteger.valueOf(i)));
    }

    LazyList<Finite<A>> parts = enumA.parts();
    for (int i = 0; i < 5 && !parts.isEmpty(); i++) {
      System.out.println(i + " --> " + parts.head());
      parts = parts.tail();
    }
    for (int p = 0; p < 10000; p = p + 10) {
      BigInteger i = java.math.BigInteger.TEN.pow(p);
      System.out.println("10^" + p + " --> " + enumA.get(i).toString().length());
      //listEnum.get(i);
    }


  }

}
