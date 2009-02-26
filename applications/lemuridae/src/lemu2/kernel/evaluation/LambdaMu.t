package lemu2.kernel.evaluation;

import tom.library.sl.*;

import lemu2.kernel.U;
import lemu2.kernel.proofterms.types.*;

public class LambdaMu {

  %include { kernel/proofterms/proofterms.tom } 
  %include { sl.tom }

  %strategy Red() extends Fail() {
    visit LTerm {
      lapp(lam(Lam(x,px,M)),N) -> { return U.`substName(M,x,N); }
      lapp(activ(Act(a,pa,M)),N) -> { return `activ(Act(a,pa,structSubst(M,a,N))); }
      fapp(flam(FLam(fx,M)),ft) -> { return U.`substFoVar(M,fx,ft); }
      passiv(a,activ(Act(b,_,M))) -> { return U.`reconame(M,b,a); }
    }
  }

  private static LTerm structSubst(LTerm t, CoName a, LTerm u) {
    %match(t) {
      lvar(x) -> { return `lvar(x); }
      lam(Lam(x,px,M)) -> { return `lam(Lam(x,px,structSubst(M,a,u))); }
      flam(FLam(x,M)) -> { return `flam(FLam(x,structSubst(M,a,u))); }
      lapp(M,N) -> { return `lapp(structSubst(M,a,u),structSubst(N,a,u)); }
      fapp(M,ft) -> { return `fapp(structSubst(M,a,u),ft); }
      activ(Act(b,pb,M)) -> { return `activ(Act(b,pb,structSubst(M,a,u))); }
      passiv(c,M) -> { 
        if (`c == a) return `passiv(c,lapp(structSubst(M,a,u),u));
        else         return `passiv(c,structSubst(M,a,u));
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static LTerm norm(LTerm pt) {
    try { return `Outermost(Red()).visit(pt); }
    catch(VisitFailure e) { throw new RuntimeException("should never happen"); }
  }
}

