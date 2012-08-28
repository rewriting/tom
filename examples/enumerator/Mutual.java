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


  /*
     private static Enumeration<A> aEnum() {
     Enumeration<A> res = Enumeration.singleton((A) enumerator.mutual.types.a.a.make());
     return res;
     }

     private static Enumeration<A> fooEnum(F<B, A> foo, Enumeration<B> BEnum) {
     Enumeration<F<B, A>> singletonFoo = Enumeration.singleton(foo);
     Enumeration<A> res = Enumeration.apply(singletonFoo, BEnum);
     return res;
     }

     private static Enumeration<A> hooEnum(F<A, A> hoo, Enumeration<A> AEnum) {
     Enumeration<F<A, A>> singletonHoo = Enumeration.singleton(hoo);
     Enumeration<A> res = Enumeration.apply(singletonHoo, AEnum);
     return res;
     }

     private static Enumeration<B> bEnum() {
     Enumeration<B> res = Enumeration.singleton((B) enumerator.mutual.types.b.b.make());
     return res;
     }

     private static Enumeration<B> grrEnum(F<A, B> grr, Enumeration<A> AEnum) {
     Enumeration<F<A, B>> singletonGrr = Enumeration.singleton(grr);
     Enumeration<B> res = Enumeration.apply(singletonGrr, AEnum);
     return res;
     }


     private static abstract class A {

     public abstract int size();
     }

     private static class aa extends A {

     public String toString() {
     return "a";
     }

     public int size() {
     return 0;
     }
     }

     private static class foo extends A {

     private B b;

     public foo(B b){
     this.b = b;
     }

     public String toString() {
     return "foo("+this.b+")";
     }

     public int size() {
     return 1+b.size();
     }
     }

     private static class hoo extends A {

     private A a;

     public hoo(A a){
     this.a = a;
     }

public String toString() {
  return "hoo("+this.a+")";
}

public int size() {
  return 1+a.size();
}
     }


private static abstract class B {

  public abstract int size();
}

private static class bb extends B {

  public String toString() {
    return "b";
  }

  public int size() {
    return 0;
  }
}

private static class grr extends B {

  private A a;

  public grr(A a){
    this.a = a;
  }

  public String toString() {
    return "grr("+this.a+")";
  }

  public int size() {
    return 1+a.size();
  }
}
*/

}
