import aterm.*;
import aterm.pure.*;

import java.util.*;

public class Poly2 extends PolyCommon {
    
  private ATermFactory factory;
  public Poly2(ATermFactory factory) {
    this.factory = factory;
  }

  %include { Poly.signature }
  
    // Simplified version of differentiate
  public ATermAppl differentiate(ATermAppl poly, ATermAppl variable) {
    %match(term poly, term variable) {
      X(), X()          -> { return `one(); }
      Y(), Y()          -> { return `one(); }
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

  public ATermAppl simplifyFunction(ATermAppl t) {
    %match(term t) {
      plus(zero,x) -> { return x; }
      plus(x,zero) -> { return x; }
      mult(one,x)  -> { return x; }
      mult(x,one)  -> { return x; }
      mult(zero,x) -> { return `zero(); }
      mult(x,zero) -> { return `zero(); }
      _            -> { return (ATermAppl) genericTraversal(t,replace); }
    }
  }

  Replace replace = new Replace() {
      public ATerm apply(ATerm t) {
        return simplifyFunction((ATermAppl)t);
      }
    };

    // Simplification using a traversal function
  public ATermAppl simplify(ATermAppl t) {
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
    Poly2 test = new  Poly2(new PureFactory());
    test.run();
  }
}

