import aterm.*;
import aterm.pure.*;
import jtom.runtime.*;

import java.util.*;

public class Poly3 extends GenericTraversal {
    
  private ATermFactory factory;
  public Poly3(ATermFactory factory) {
    this.factory = factory;
  }

  %include { Poly.signature }
  
    // Simplified version of differentiate
  public ATermAppl differentiate(ATermAppl poly, ATermAppl variable) {
    %match(term poly, term variable) {
      X(), X() | Y(), Y()          -> { return `one(); }
      plus(a1,a2), var  -> { return `plus(differentiate(a1, var),differentiate(a2, var)); }
      mult(a1,a2), var  -> { 
        ATermAppl res1, res2;
        res1 =`mult(a1, differentiate(a2, var));
        res2 =`mult(a2, differentiate(a1, var));
        return `plus(res1,res2);
      }
    }
    return `zero();
  }

    // Simplification using a traversal function
  public ATermAppl simplify(ATermAppl t) {
    Replace1 replace = new Replace1() {
        public ATerm apply(ATerm t) {
          %match(term t) {
            plus(zero,x) | plus(x,zero) |
            mult(one,x)  | mult(x,one)  -> { return x; }
            mult(zero,x) | mult(x,zero) -> { return `zero(); }
            _ -> { return genericTraversal(t,this); }
          }
        }
      };

    ATermAppl res = (ATermAppl)replace.apply(t);
    if(res != t) {
      res = simplify(res);
    }
    return res;
  }
  
  public void run() {
    ATermAppl t    = `mult(X(),plus(X(),a()));
    ATermAppl var1 = `X();
    ATermAppl var2 = `Y();
    ATermAppl res;
    res = differentiate(t,var1);
    System.out.println("Derivative form of " + t + " wrt. " + var1 + " is:\n\t" + res);
    res = simplify(res);
    System.out.println("Simplified form is:\n\t" + res);
    
    res = differentiate(t,var2);
    System.out.println("Derivative form of " + t + " wrt. " + var2 + " is:\n\t" + res);
    res = simplify(res);
    System.out.println("Simplified form is:\n\t" + res);
  }
  
  public final static void main(String[] args) {
    Poly3 test = new  Poly3(new PureFactory());
    test.run();
  }
}

