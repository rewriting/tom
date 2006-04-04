package master;

import master.peano2.peano.types.*;

public class Peano2 {

  %gom {
    module Peano
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
        | plus(x1:Nat, x2:Nat)
   }

	public Nat evaluate(Nat n) {
		%match(Nat n) {
      plus(x, zero())    -> { return `x; }
      plus(x, suc(y))    -> { return `suc(plus(x,y));}

      plus(x,y) -> { return `plus(x,evaluate(y));}
      suc(x) -> { return `suc(evaluate(x));}
      zero() -> { return `zero();}
		}
		return null;
	}

//   %rule {
//     mult(x, zero())    -> zero()
//     mult(x, suc(y))    -> plus(x,mult(x,y))
//   } // rule
 
  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
		Nat one = `suc(zero());
		Nat two = `suc(one);

//     Nat init = `plus(two,two);
//     Nat init = `plus(two,two);
    Nat init = `plus(one,plus(one,plus(one,one)));
//     Nat init = `plus(plus(one,one),plus(one,one));
    boolean done=false;
    System.out.println("Start = " + init);
    while(!done){
      Nat res = `evaluate(init);
      System.out.println("Step = " + `res);
      if(res==init){
        done=true;
      } else {
        init=res;
      }
    }
  }
  
  public final static void main(String[] args) {
    Peano2 test = new Peano2();
    test.run();
  }

}
