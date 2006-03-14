package master;
import master.matching1.peano.types.*;

public class Matching1 {

  %gom {
    module Peano
    imports 
    public
      sorts Nat 
      
    abstract syntax
//       xx -> Nat
//       yy -> Nat
      var(n:String) -> Nat
      zero() -> Nat
      suc(pred:Nat) -> Nat
      plus(x1:Nat, x2:Nat) -> Nat
      True()   -> Nat
      False()  -> Nat
      Match(pattern:Nat, subject:Nat) 	-> Nat
      And(n1:Nat,n2:Nat) -> Nat
   }

  %rule {
    // Delete
    Match(zero(),zero()) -> True()
      
    // Decompose
      Match(suc(x),suc(y)) -> Match(x,y)
      Match(plus(x1,x2),plus(y1,y2)) -> And(Match(x1,y1),Match(x2,y2))
        
    // SymbolClash
      Match(suc(_),zero()) -> False() 
      Match(zero(),suc(_)) -> False() 
      Match(plus(_,_),zero()) -> False() 
      Match(zero(),plus(_,_)) -> False() 
      Match(suc(_),plus(_,_)) -> False() 
      Match(plus(_,_),suc(_)) -> False() 
  }

  %rule {
    // PropagateClash
    And(False(),_) -> False()
      And(_,False()) -> False()

    // PropagateSuccess
      And(True(),X) -> X
      And(X,True()) -> X

    // Merging
    And(X,X) -> X

    // MergingFail
      And(Match(var(x),zero()),Match(var(x),suc(_))) -> False()
      And(Match(var(x),suc(_)),Match(var(x),zero())) -> False()
      And(Match(var(x),zero()),Match(var(x),plus(_,_))) -> False() 
      And(Match(var(x),plus(_,_)),Match(var(x),zero())) -> False() 
      And(Match(var(x),suc(_)),Match(var(x),plus(_,_))) -> False() 
      And(Match(var(x),plus(_,_)),Match(var(x),suc(_))) -> False() 
  }

  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
    Nat xx=`var("x");
    Nat yy=`var("y");

		Nat one = `suc(zero());
		Nat two = `suc(one);

    Nat pxy = `plus(xx,yy);
    Nat px0 = `plus(xx,zero);
    Nat px1 = `plus(xx,one);
    Nat py0 = `plus(yy,zero);
    Nat p00 = `plus(zero,zero);
    Nat p01 = `plus(zero,one);
    Nat px2 = `plus(xx,two);
    Nat p22 = `plus(two,two);
    Nat p1p1p11 = `plus(one,plus(one,plus(one,one)));
    Nat pp11p11 = `plus(plus(one,one),plus(one,one));

//     Nat start = `Match(p22,p22);
//     Nat start = `Match(xx,p22);
    Nat t1 = `plus(pxy,pxy);
    Nat t2 = `plus(p00,p01);
    Nat start = `Match(t1,t2);
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
  }
  
  public final static void main(String[] args) {
    Matching1 test = new Matching1();
    test.run();
  }

}
