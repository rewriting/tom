package master;

import master.matching4.peano.types.*;

public class Matching4 {

  %gom {
    module Peano
    imports 
      String
    public
      sorts Nat Mat MatList
      
    abstract syntax
      var(n:String) -> Nat
      zero()  -> Nat
      suc(pred:Nat) -> Nat
      plus(x1:Nat, x2:Nat) -> Nat
      True()   -> Mat
      False()  -> Mat
      Match(pattern:Nat, subject:Nat) 	-> Mat
      And(Mat*) -> MatList
   }

	
	public MatList solve(MatList p) {
    p = `And(p*,True());
		MatList res = oneStepSolve(p);
		if(p != res) {
			res = solve(res);
		}
		return res;
	}

	public MatList oneStepSolve(MatList p) {
		%match(MatList p) {
      // Delete
      And(X*,Match(zero(),zero()),Y*) -> { return `And(X*,True(),Y*);}
      
      // Decompose
      And(X*,Match(suc(x),suc(y)),Y*) -> { return `And(X*,Match(x,y),Y*);}
      And(X*,Match(plus(x1,x2),plus(y1,y2)),Y*) -> { 
        return `And(Match(x1,y1),Match(x2,y2));}
      
      // SymbolClash
      And(X*,Match(suc(_),zero()),Y*) -> { return `And(X*,False(),Y*) ;}
      And(X*,Match(zero(),suc(_)),Y*) -> { return `And(X*,False(),Y*) ;}
      And(X*,Match(plus(_,_),zero()),Y*) -> { return `And(X*,False(),Y*) ;} 
      And(X*,Match(zero(),plus(_,_)),Y*) -> { return `And(X*,False(),Y*) ;}
      And(X*,Match(suc(_),plus(_,_)),Y*) -> { return `And(X*,False(),Y*) ;}
      And(X*,Match(plus(_,_),suc(_)),Y*) -> { return `And(X*,False(),Y*) ;}

      // PropagateClash
      And(X*,False(),Y*) -> { return `And(X*,False(),Y*) ;}
      
      // PropagateSuccess
      And(X*,True(),Y*) -> { return `And(X*,Y*);}
      
      // Merging
      And(Z*,X,Y*,X,T*) -> { return `And(Z*,X,Y*,T*) ;}
      
      // MergingFail
      And(Z*,Match(var(x),X),U*,Match(var(x),Y),T*) -> { 
        if(`X!=`Y){
          return `And(Z*,False(),U*,T*) ;
        }
      }
      
      // congruence
			And(p1,p2*) -> { 
        MatList s1=`solve(And(p1,True()));
        MatList s2=`solve(p2);
        return `And(s1*,s2*); 
      }
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
    Nat px0 = `plus(xx,zero());
    Nat py0 = `plus(yy,zero());
    Nat p00 = `plus(zero(),zero());
    Nat p01 = `plus(zero(),one);
    Nat px2 = `plus(xx,two);
    Nat p22 = `plus(two,two);
    Nat p1p1p11 = `plus(one,plus(one,plus(one,one)));
    Nat pp11p11 = `plus(plus(one,one),plus(one,one));

//     Nat start = `Match(p22,p22);
//     Nat start = `Match(xx,p22);
    Nat t1 = `plus(pxy,pxy);
    Nat t2 = `plus(p00,p01);
    MatList start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(pxy,pzx);
    t2 = `plus(p00,p00);
    start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(yy,pxy);
    t2 = `plus(zero(),p00);
    start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(xx,pxy);
    t2 = `plus(zero(),p00);
    start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(xx,xx);
    t2 = `plus(zero(),one);
    start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
  }
  
  public final static void main(String[] args) {
    Matching4 test = new Matching4();
    test.run();
  }

}
