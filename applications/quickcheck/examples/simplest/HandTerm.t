package examples.simplest;

import tom.library.enumerator.*;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.math.BigInteger;
import tomchecker.*;  

import examples.simplest.term.types.*;

public class HandTerm {

  %include{ term/Term.tom }

  public static String pretty(Term t) {
    %match(t) {
      a() -> { return "a()"; }
      b() -> { return "b()"; }
      c() -> { return "c()"; }
      f(x,y) -> { return "f(" + pretty(`x) + "," + `y + ")"; }
      g(x,y) -> { return "g(" + pretty(`x) + "," + `y + ")"; }
      h(x,y) -> { return "h(" + pretty(`x) + "," + pretty(`y) + ")"; }
    }
    return "error: " + t;
  }

  public static void main(String args[]) {
    System.out.println("check pretty");

    Set<F< Product1<Term>, Boolean>> preCond = new HashSet<F< Product1<Term>, Boolean>>();

    F< Product1<Term>, Boolean>  prop = new F< Product1<Term>, Boolean>() {
      public Boolean apply(Product1<Term> args) {
        Term t = args.proj1;
        //System.out.println("try: " + t);
        return Term.fromString(pretty(t)) == t;
      }
    };

    Enumeration< Product1<Term> > enumeration = Product1.enumerate( Term.getEnumeration());

    try { 
      System.out.println("--Random Check--");
      Checker.quickCheckProd1(10, enumeration, prop, 2, false, 2000, preCond, BigInteger.valueOf(1000), false); 
    } catch(AssertionError err) {
      System.out.println(err.getMessage());
    } 

  }
}
