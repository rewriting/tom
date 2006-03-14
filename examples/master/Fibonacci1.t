package master;

import master.fibonacci1.peano.types.*;

public class Fibonacci1 {

  %gom {
    module Peano
    imports 
    public
      sorts Nat
      
    abstract syntax
      zero() -> Nat
      suc(pred:Nat) -> Nat
      plus(x1:Nat, x2:Nat) -> Nat
      mult(x1:Nat, x2:Nat) -> Nat
      
			fib(x:Nat) -> Nat

			//fibint(n:int) -> int
   }

    // rule plus
  %rule {
    plus(x, zero())    -> x
    plus(x, suc(y))    -> suc(plus(x,y))
  } // rule
  %rule {
    mult(x, zero())    -> zero()
    mult(x, suc(y))    -> plus(x,mult(x,y))
  } // rule
 
	/*
	%rule {
    fibint(0) -> 1
    fib(1)    -> 1
    fib(n)    -> fibint(n - 1) + fibint(n - 2) 
  } 
	*/

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

	%rule {
    fib(zero())        -> suc(zero())
    fib(suc(zero()))   -> suc(zero())
    fib(suc(suc(x)))   -> plus(fib(x),fib(suc(x)))
  }
  
  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
		Nat one = `suc(zero());
		Nat two = `suc(one);

    System.out.println("plus(one,two) = " + `plus(one,two));
    System.out.println("mult(two,two) = " + `mult(two,two));
    System.out.println("fibint(4) = " + `fibint(4));
    System.out.println("fib(four) = " + `fib(mult(two,two)));
  }
  
  public final static void main(String[] args) {
    Fibonacci1 test = new Fibonacci1();
    test.run();
  }

}
