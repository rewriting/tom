import matching3.peano.*;
import matching3.peano.types.*;

public class Matching3 {

  %vas {
    module Peano
    imports 
    public
      sorts Nat 
      
    abstract syntax
      var(n:String) -> Nat
      zero -> Nat
      suc(pred:Nat) -> Nat
      plus(x1:Nat, x2:Nat) -> Nat
      True  -> Nat
      False -> Nat
      Match(pattern:Nat, subject:Nat) 	-> Nat
      And(n1:Nat,n2:Nat) -> Nat
   }

	
	public Nat solve(Nat p) {
		Nat res = oneStepSolve(p);
		if(p != res) {
			res = solve(res);
		}
		return res;
	}

	public Nat oneStepSolve(Nat p) {
		%match(Nat p) {

      // Delete
      Match(zero(),zero()) -> { return `True();}
      
      // Decompose
      Match(suc(x),suc(y)) -> { return `Match(x,y);}
      Match(plus(x1,x2),plus(y1,y2)) -> { 
        return `And(Match(x1,y1),Match(x2,y2));}
        
    // SymbolClash
      Match(suc(_),zero()) -> { return `False() ;}
      Match(zero(),suc(_)) -> { return `False() ;}
      Match(plus(_,_),zero()) -> { return `False() ;} 
      Match(zero(),plus(_,_)) -> { return `False() ;}
      Match(suc(_),plus(_,_)) -> { return `False() ;}
      Match(plus(_,_),suc(_)) -> { return `False() ;}

    // PropagateClash
      And(False(),_) -> { return `False() ;}
      And(_,False()) -> { return `False() ;}

    // PropagateSuccess
      And(True(),X) -> { return `X ;}
      And(X,True()) -> { return `X ;}

    // Merging
      And(X,X) -> { return `X ;}
      And(X,And(X,Y)) -> { return  `And(X,Y);}

    // MergingFail
      And(Match(var(x),X),Match(var(x),Y)) -> { 
        if(X!=Y){
          return `False() ;
        }
      }
      And(Match(var(x),X),And(Match(var(x),Y),P)) -> { 
        if(X!=Y){
          return `False() ;
        }
      }

    // Sort
      And(Match(var(x),X),Match(var(y),Y)) -> { 
        if(x.compareTo(y)<0){
          return `And(Match(var(y),Y),Match(var(x),X)) ;
        }
      }
      And(Match(var(x),X),And(Match(var(y),Y),P)) -> { 
        if(x.compareTo(y)<0){
          return `And(Match(var(y),Y),And(Match(var(x),X),P)) ;
        }
      }

     // Assoc
        And(And(p1,p2),p3) -> { return `And(p1, And(p2,p3)); }

    // congruence
			And(p1,p2) -> { return `And(solve(p1),solve(p2)); }
		}
		return p;
	}


  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
    Nat xx=`var("x");
    Nat yy=`var("y");
    Nat zz=`var("z");

		Nat one = `suc(zero());
		Nat two = `suc(one);

    Nat pxy = `plus(xx,yy);
    Nat pzx = `plus(zz,xx);
    Nat px0 = `plus(xx,zero);
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
    Nat start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(pxy,pzx);
    t2 = `plus(p00,p00);
    start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(yy,pxy);
    t2 = `plus(zero,p00);
    start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(xx,pxy);
    t2 = `plus(zero,p00);
    start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(xx,xx);
    t2 = `plus(zero,one);
    start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
  }
  
  public final static void main(String[] args) {
    Matching3 test = new Matching3();
    test.run();
  }

}
