package master;

import master.fibonacci2.peano.types.*;

public class Fibonacci2 {

  %gom {
    module Peano
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
        | pred(suc:Nat)
        | plus(x1:Nat, x2:Nat)
        | mult(x1:Nat, x2:Nat)
        | fib(x:Nat)
   }

  // rule pred
  %rule {
    pred(suc(x))    -> x
  }  // rule
  // rule pred
  %rule {
    suc(pred(x))    -> x
  } // rule
  // rule plus
  %rule {
    plus(x, zero())    -> x
    plus(x, suc(y))    -> suc(plus(x,y))
    plus(x, pred(y))    -> pred(plus(x,y))
  } // rule
  %rule {
    mult(x, zero())    -> zero()
    mult(x, suc(y))    -> plus(x,mult(x,y))
  } // rule

  %rule {
    fib(zero())        -> suc(zero())
    fib(suc(zero()))   -> suc(zero())
    fib(x)   -> plus(fib(pred(x)),fib(pred(pred(x))))
  }

  public int fibint(int n) {
    %match(int n) {
      0 -> { return 1; }
      1 -> { return 1; }
      x -> {
        if(`x>1) {
          return `fibint(x-1) + `fibint(x-2);
        }
      }
    }
    return -1;
  }

  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
    Nat one = `suc(zero());
    Nat two = `suc(one);
    Nat minusone = `pred(zero());

    System.out.println("plus(one,minusone) = " + `plus(one,minusone));
    System.out.println("plus(one,two) = " + `plus(one,two));
    System.out.println("mult(two,two) = " + `mult(two,two));
    System.out.println("fibint(4) = " + `fibint(4));
    System.out.println("fib(four) = " + `fib(mult(two,two)));
  }

  public final static void main(String[] args) {
    Fibonacci2 test = new Fibonacci2();
    test.run();
  }

}
