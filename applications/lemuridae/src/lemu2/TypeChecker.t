package lemu2;

import lemu2.proofterms.types.*;
import lemu2.proofterms.types.fovarlist.fovarList;
import java.util.Collection;
import tom.library.sl.*;

public class TypeChecker {

  %include { proofterms/proofterms.tom } 
  %include { sl.tom } 

  private static Prop lookup(LCtx ctx, Name n) {
    %match(ctx) {
      lctx(_*,nprop(x,p),_*) && x << Name n -> { return `p; }
    }
    throw new RuntimeException("name " + n + " not in scope");
  }

  private static Prop lookup(RCtx ctx, CoName cn) {
    %match(ctx) {
      rctx(_*,cnprop(x,p),_*) && x << CoName cn -> { return `p; }
    }
    throw new RuntimeException("coname " + cn + " not in scope");
  }

  private static fovarList freeFoVars(TermList tl, fovarList ctx) {
    %match(tl) {
      termList() -> { return `fovarList(); }
      termList(t,ts*) -> {
        fovarList l1 = `freeFoVars(t,ctx);
        fovarList l2 = `freeFoVars(ts,ctx);
        return (fovarList) `fovarList(l1*,l2*);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static fovarList freeFoVars(Term t, fovarList ctx) {
    %match(t) {
      Var(v) -> { if (!ctx.contains(`v)) return `fovarList(v); }
      funApp(_,tl) -> { return `freeFoVars(tl,ctx); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static fovarList freeFoVars(Prop p, fovarList ctx) {
    %match(p) {
      relApp(_,tl) -> { return `freeFoVars(tl,ctx); }
      (and|or|implies)(p1,p2) -> { 
        FoVarList l1 = `freeFoVars(p1,ctx);
        FoVarList l2 = `freeFoVars(p2,ctx);
        return (fovarList) `fovarList(l1*,l2*);
      }
      forall(Fa(x,p1)) -> { return `freeFoVars(p1,fovarList(x,ctx*)); }
      exists(Ex(x,p1)) -> { return `freeFoVars(p1,fovarList(x,ctx*)); }
      (bottom|top)() -> { return `fovarList(); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  %strategy SubstFoVar(FoVar x, Term u) extends Identity() {
    visit Term {
      Var(y) && y << FoVar x -> { return `u; }
    }
  }

  private static Prop substFoVar(Prop p, FoVar x, Term u) {
    try { return (Prop) `TopDown(SubstFoVar(x,u)).visit(p); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  public static boolean typecheck(ProofTerm pt) {
    return typecheck(pt,`seq(lctx(),rctx()));
  }

  private static boolean typecheck(ProofTerm pt, Sequent se) {
    if(_typecheck(pt,se))
      return true;
    else {
      System.out.println(pt + "\n" + se + "\n"); 
      return false;
    }
  }


  private static boolean _typecheck(ProofTerm pt, Sequent se) {
    %match(se) {
      seq(gamma,delta) -> {
        %match(pt) {
          ax(n,cn) -> {
            return `lookup(gamma,n).equals(`lookup(delta,cn));
          }
          cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
            return `pa.equals(`px) 
              && `typecheck(M1,seq(gamma,rctx(cnprop(a,pa),delta*)))
              && `typecheck(M2,seq(lctx(nprop(x,px),gamma*),delta));
          }
          // left rules
          andL(AndLPrem1(x,px,y,py,M),n) -> {
            return `lookup(gamma,n).equals(`and(px,py))
              && `typecheck(M,seq(lctx(nprop(x,px),nprop(y,py),gamma*),delta));
          }
          orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
            return `lookup(gamma,n).equals(`or(px,py))
              && `typecheck(M1,seq(lctx(nprop(x,px),gamma*),delta))
              && `typecheck(M2,seq(lctx(nprop(y,py),gamma*),delta));
          }
          implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
            return `lookup(gamma,n).equals(`implies(pa,px))
              && `typecheck(M1,seq(lctx(nprop(x,px),gamma*),delta))
              && `typecheck(M2,seq(gamma,rctx(cnprop(a,pa),delta*)));
          }
          forallL(ForallLPrem1(x,px,M),t,n) -> {
            %match(lookup(gamma,n)) {
              forall(Fa(fx,p)) -> {
                return `px.equals(`substFoVar(p,fx,t)) 
                  && `typecheck(M,seq(lctx(nprop(x,px),gamma*),delta));
              }
            } return false;
          }
          existsL(ExistsLPrem1(x,px,fx,M),n) -> {
            return `lookup(gamma,n).equals(`exists(Ex(fx,px)))
                && `typecheck(M,seq(lctx(nprop(x,px),gamma*),delta)); 
          }
          rootL(RootLPrem1(x,px,M)) -> {
            return `typecheck(M,seq(lctx(nprop(x,px),gamma*),delta)); 
          }
          // right rules
          orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
            return `lookup(delta,cn).equals(`or(pa,pb))
              && `typecheck(M,seq(gamma,rctx(cnprop(a,pa),cnprop(b,pb),delta*)));
          }
          andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
            return `lookup(delta,cn).equals(`and(pa,pb))
              && `typecheck(M1,seq(gamma,rctx(cnprop(a,pa),delta*)))
              && `typecheck(M2,seq(gamma,rctx(cnprop(b,pb),delta*)));
          }
          implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
            return `lookup(delta,cn).equals(`implies(px,pa))
              && `typecheck(M,seq(lctx(nprop(x,px),gamma*),rctx(cnprop(a,pa),delta*)));
          }
          existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
            %match(lookup(delta,cn)) {
              exists(Ex(fx,p)) -> {
                return `pa.equals(`substFoVar(p,fx,t)) 
                  && `typecheck(M,seq(gamma,rctx(cnprop(a,pa),delta*)));
              }
            } return false;
          }
          forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
            return `lookup(delta,cn).equals(`exists(Ex(fx,pa)))
              && `typecheck(M,seq(gamma,rctx(cnprop(a,pa),delta*)));
          }
          rootR(RootRPrem1(a,pa,M)) -> {
            return `typecheck(M,seq(gamma,rctx(cnprop(a,pa),delta*)));
          }
        }
      }
    }
    return false;
  }
}
