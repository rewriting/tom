package antiptest;

import antiptest.aptest.peano.types.*;

public class ApTest {

  %gom {
    module Peano
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
        | plus(x1:Nat, x2:Nat)
   }

  //TODO:radu - vezi ce se intampla cand sunt mai multi termeni in match
  // de ce e bug-ul
  
	public Nat evaluate(Nat n) {
		%match(Nat n) {
//	      !plus(!x, zero())    -> { return `x; }
//	      plus(x, suc(y))    -> { System.out.println("1");}
			plus(!x,!y) -> { System.out.println("XXXX");}
			zero()    -> { System.out.println("2");}
			plus(x,!suc(y))    -> { System.out.println("3 x=" + `x);}
			plus(x,y)    -> { System.out.println("33 x=" + `x + " y=" + `y);}
			plus(x,x)    -> { System.out.println("333 x=" + `x);}
//			!plus(x,!suc(y))    -> { System.out.println("4");}
			!plus(x,!suc(suc(y)))    -> { System.out.println("4");}
		}
		return null;
	}

  //-------------------------------------------------------

  public void run() {
    System.out.println("running..."); 
    Nat one = `suc(zero());
	Nat two = `suc(one);
    Nat init = `plus(zero(),two);//`plus(one,two);

    
    System.out.println("Start = " + init);
    Nat res = `evaluate(init);
    System.out.println("Step = " + `res);
    
  }
  
  public final static void main(String[] args) {
	ApTest test = new ApTest();
    test.run();
  }

}
