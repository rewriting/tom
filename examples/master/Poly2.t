// package master;

// import master.poly2.poly.*;
// import master.poly2.poly.types.*;
import poly2.poly.*;
import poly2.poly.types.*;

public class Poly2 {

  %vas {
    module Poly
    imports 
    public
      sorts Poly PolyList
      
    abstract syntax
      m(coef:int,name:String,exp:int) -> Poly
      plus(pl:PolyList) -> Poly
      mult(pl:PolyList) -> Poly
      conc(Poly*) -> PolyList
   }

	public Poly deriv(Poly p) {
    System.out.println("P= "+p);

		%match(Poly p) {
			m(_,"X",0) -> { return `m(0,"X",0); }
			m(c,"X",n) -> { 
				if(`n>0) {
					return `m(c * n,"X",n-1); 
				}
			}

			plus(conc(A*,p1@m(_,_,_),B*)) -> { 
        System.out.println("PP= "+p);
        return `plus(conc(deriv(p1),deriv(plus(conc(A*,B*))))); 
      }
			mult(conc(A*,p1@m(_,_,_),B*)) -> { 
        return `plus(conc(mult(conc(deriv(p1),mult(conc(A*,B*)))),
                          mult(conc(p1,deriv(mult(conc(A*,B*))))))); 
      }

// 			plus(conc(A*,p1,B*)) -> { 
//         return `plus(conc(deriv(p1),deriv(plus(conc(A*,B*))))); 
//       }
// 			mult(conc(A*,p1,B*)) -> { return `plus(conc(mult(conc(deriv(p1),mult(conc(A*,B*)))),
//                                                   mult(conc(p1,deriv(mult(conc(A*,B*))))))); }
		}
		return `p;
	}
	
	public Poly simplify(Poly p) {
		Poly res = oneStepSimplify(p);
		if(p != res) {
			res = simplify(res);
		}
		return res;
	}

	public Poly oneStepSimplify(Poly p) {
		%match(Poly p) {
			plus(conc(plus(conc(A*)),plus(conc(B*)))) -> { 
        return `plus(conc(A*,B*)); 
      }
			plus(conc(A*,m(c1,"X",n),B*,m(c2,"X",n),C*)) -> { 
        return `plus(conc(A*,m(c1+c2,"X",n),B*,C*)); 
      }
			plus(conc(A*,m1@m(c1,"X",n1),B*,m2@m(c2,"X",n2),C*)) -> {
					if(`n2>`n1) {
            return `plus(conc(A*,m2,B*,m1,C*));
					}
			}

      // congruence
			plus(conc(p1,p2)) -> { return `plus(conc(simplify(p1),simplify(p2))); }
		}
		return p;
	}
  
  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
		Poly X_2 = `m(1,"X",2);
    
		System.out.println("X_2 = " + X_2);
		System.out.println("X_2+3 = " + `plus(conc(X_2,m(3,"X",0))));
		System.out.println("deriv(X_2+3) = " + `deriv(plus(conc(X_2,m(3,"X",0)))));

		// 3*X2+X+7
		Poly P1 = `plus(conc(m(3,"X",2),m(1,"X",1),m(7,"X",0)));
		// 5*X2+3*X+4
		Poly P2 = `plus(conc(m(5,"X",2),m(3,"X",1),m(4,"X",0)));
		System.out.println("P1 = " + `P1);
		System.out.println("P2 = " + `P2);
		System.out.println("deriv(plus(P1,P2)) = " + `deriv(plus(conc(P1,P2))));
		System.out.println("deriv(plus(P1,P2)) = " + `simplify(deriv(plus(conc(P1,P2)))));

  }
  
  public final static void main(String[] args) {
    Poly2 test = new Poly2();
    test.run();
  }

}
