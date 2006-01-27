//import peano1.peano.*;
//import peano1.peano.types.*;

public class Peano1 {

  %vas {
    module Peano
    imports 
    public
      sorts Nat
      
    abstract syntax
      zero -> Nat
      suc(pred:Nat) -> Nat
      plus(x1:Nat, x2:Nat) -> Nat
   }

    // rule plus
  %rule {
    plus(x, zero())    -> x
    plus(x, suc(y))    -> suc(plus(x,y))
  } // rule
  
  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
		//Nat one = `suc(zero());
		//Nat two = `suc(one);

    //System.out.println("plus(one,two) = " + plus(one,two));
    System.out.println("plus(one,two) = " + `plus(suc(zero()),suc(suc(zero()))));
  }
  
  public final static void main(String[] args) {
    Peano1 test = new Peano1();
    test.run();
  }

}
