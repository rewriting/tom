package master;

import master.peanovas.peano.types.*;

class PeanoVas {
  %vas {
    // extension of adt syntax
    module Peano
    imports 
    public
      sorts Nat
      
    abstract syntax
      zero() -> Nat
      one() -> Nat
      two() -> Nat
      suc(pred:Nat) -> Nat
      plus(x1:Nat, x2:Nat) -> Nat
      fib(val:Nat)  -> Nat
      plusInt(x1:Nat, x2:Nat) -> int
   }

  %rule {
     one() -> suc(zero())
  }
  
  %rule {
     two() -> suc(suc(zero()))
  }

  private Nat vOne;
  private Nat vTwo;

  private Nat fvOne() { return vOne; }

    // rule fib
  %rule {
    fib(zero())        -> fvOne()
    fib(suc(zero()))   -> suc(zero())
    fib(suc(suc(x)))   -> plus(fib(x),fib(suc(x)))
  }

    // rule plus
  %rule {
    plus(x, zero())    -> x
    plus(x, suc(y))    -> suc(plus(x,y))
  } // rule
  
  %rule {
    plusInt(zero(), zero()) -> 0
    plusInt(x, suc(y))    -> increment(plusInt(x,y))
    plusInt(suc(x), y)    -> increment(plusInt(x,y))
  } // rule

  private int increment(int a) { return a+1; }

  //-------------------------------------------------------

  public void run() {
    vOne = `suc(zero());
    vTwo = `suc(suc(zero()));

    System.out.println("running...");
    System.out.println("one = " + one());
    System.out.println("two = " + two());
    System.out.println("plus(one,two) = " + plus(one(),two()));
    System.out.println("plusInt(one,two) = " + plusInt(one(),two()));
  }
  
  public final static void main(String[] args) {
    PeanoVas test = new PeanoVas();
    test.run();
  }


}
