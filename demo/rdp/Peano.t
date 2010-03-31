import peano.peano.*;
import peano.peano.types.*;
public class Peano {
 %gom {
    module Peano
    imports int
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
        | plus(x1:Nat, x2:Nat)
        | mult(x1:Nat, x2:Nat)
        | fib(n:Nat)
        | one()

    module Peano:rules() {
      plus(x, zero())    -> x 
      plus(x, suc(y))    -> suc(plus(x,y))
      mult(x, zero())    -> zero() 
      mult(x, suc(y))    -> plus(x,mult(x,y))
      fib(zero()) -> suc(zero())
      
      fib(suc(zero())) -> suc(zero())      
      fib(suc(suc(x))) -> plus(fib(x),fib(suc(x)))

    } 
  }
  
  //-------------------------------------------------------
  private static int entier(Nat x) {
    %match(Nat x) {
      zero() -> { return 0; }
      suc(y) -> { return 1+entier(`y); }
    }
    return -1;
  }

  public static void main(String[] args) {
    System.out.println("running...");

    Nat one = `suc(zero());
    Nat two = `suc(one);
    System.out.println("one = " + one);
    System.out.println("two = " + two);
    Nat t = peano.peano.types.nat.plus.make(one,`zero());
    System.out.println("t = " + t);

    System.out.println("plus(one,two) = " + `plus(one,two));
    //System.out.println("plus(one,two) = " + `plus(suc(zero()),suc(suc(zero()))));
    System.out.println("mult(two,two) = " + `mult(suc(suc(zero())),suc(suc(zero()))));
    System.out.println("fib(10) = " + `entier(fib(suc(suc(suc(suc(suc(suc(suc(suc(suc(suc(zero())))))))))))));
  }

}