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
  
	public Nat evaluate(Nat n) {
		%match(Nat n) {
			plus(!x,!y) 		-> { System.out.println("Matched with: plus(!x,!y)");}
			zero()    			-> { System.out.println("Matched with: zero()");}
			plus(x,!suc(y))    	-> { System.out.println("Matched with: plus(x,!suc(y)), with x=" + `x);}
			plus(x,!suc(x))    	-> { System.out.println("Matched with: plus(x,!suc(x)), with x=" + `x);}
			plus(x,y)    		-> { System.out.println("Matched with: plus(x,y), with x=" + `x + " y=" + `y);}
			plus(x,x)    		-> { System.out.println("Matched with: plus(x,x), with x=" + `x);}
			plus(x,!x)    		-> { System.out.println("Matched with: plus(x,!x), with x=" + `x);}
			!plus(x,!suc(y))    -> { System.out.println("Matched with: !plus(x,!suc(y))");}
			!plus(x,!suc(suc(y)))    -> { System.out.println("Matched with: !plus(x,!suc(suc(y)))");}
			plus(x,suc(suc(plus(y,!x))))    -> { System.out.println("Matched with: plus(x,suc(suc(plus(y,!x)))), with x=" + `x + " y=" + `y);}
		}
		return null;
	}

  //-------------------------------------------------------

  public void run() {
    // System.out.println("running..."); 
    Nat one = `suc(zero());
	Nat two = `suc(one);
    Nat init = `plus(zero(),two);//`plus(one,two);

    
    System.out.println("Subject : " + init);
    Nat res = `evaluate(init);
    //System.out.println("Step = " + `res);
    
  }
  
  public final static void main(String[] args) {
	ApTest test = new ApTest();
    test.run();
  }

}
