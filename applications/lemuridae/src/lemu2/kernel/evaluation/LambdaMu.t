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
      lapp(activ(Act(a,implies(pa,pb),M)),N) -> { return `activ(Act(a,pb,structSubst(M,a,N))); }
      fapp(flam(FLam(fx,M)),ft) -> { return U.`substFoVar(M,fx,ft); }
      proj1(pair(t,u)) -> { return `t; }
      proj2(pair(t,u)) -> { return `u; }
      caseof(left(M,pb),Alt(x,px,N),Alt(y,py,Q)) -> { return U.`substName(N,x,M); }
      caseof(right(M,pa),Alt(x,px,N),Alt(y,py,Q)) -> { return U.`substName(Q,y,M); }
      activ(Act(a,pa,passiv(b,activ(Act(c,_,M))))) -> { return `activ(Act(a,pa,U.reconame(M,c,b))); }
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

  %strategy MuEta() extends Fail() {
    visit LTerm {
      activ(Act(a,pa,passiv(a,M))) -> { if (!U.`freeIn(a,M)) return `M; }
    }
  }

  public static LTerm mueta(LTerm pt) {
    try { return `Outermost(MuEta()).visit(pt); }
    catch(VisitFailure e) { throw new RuntimeException("should never happen"); }
  }

}

