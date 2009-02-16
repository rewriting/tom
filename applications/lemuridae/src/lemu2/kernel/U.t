package lemu2.kernel;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.proofterms.types.fovarlist.fovarList;
import tom.library.freshgom.*;
import tom.library.sl.*;

public class U {

  %include { kernel/proofterms/proofterms.tom } 
  %include { sl.tom } 

  /* checks p1 and p2 alpha-equivalence with free the set of free vars */
  public static boolean alpha(Prop p1, Prop p2, FoVarList free) {
    AlphaMap<FoVar> map = new AlphaMap<FoVar>();
    %match(free) {
      fovarList(_*,v,_*) -> { map.`put(v,v,FoVar.freshFoVar("free")); }
    }
    try { p1.alpha(p2,map); return true; } 
    catch (AlphaMap.AlphaException e) { return false; }
  }

  public static Prop lookup(LCtx ctx, Name n) {
    %match(ctx) {
      lctx(_*,nprop(x,p),_*) && x == n -> { return `p; }
    }
    throw new RuntimeException("name " + n + " not in scope");
  }

  public static Prop lookup(RCtx ctx, CoName cn) {
    %match(ctx) {
      rctx(_*,cnprop(x,p),_*) && x == cn -> { return `p; }
    }
    throw new RuntimeException("coname " + cn + " not in scope");
  }

  public static PropRewriteRule lookup(PropRewriteRules pfrules, String id) {
    %match(pfrules) {
      proprrules(_*,r@proprrule(n,_),_*) && n == id -> { return `r; }
    }
    throw new RuntimeException("rule " + id + " not in scope");
  }

  /*
  public static fovarList freeFoVars(TermList tl, fovarList ctx) {
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

  public static fovarList freeFoVars(Term t, fovarList ctx) {
    %match(t) {
      var(v) -> { if (!ctx.contains(`v)) return `fovarList(v); }
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
  */

  %strategy SubstFoVar(FoVar x, Term u) extends Identity() {
    visit Term {
      var(y) && y == x -> { return `u; }
    }
  }

  public static Prop substFoVar(Prop p, FoVar x, Term u) {
    try { return (Prop) `TopDown(SubstFoVar(x,u)).visit(p); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  public static Prop norm(Prop p, TermRewriteRules trs, PropRewriteRules prs) {
    return Rewriting.normalize(p,trs,prs);
  }

  public static Prop unfold(Prop p, PropRewriteRule pfr) {
    return Rewriting.rewrite(p,pfr);
  }

  public static ProofTerm rename(ProofTerm pt, Name n1, Name n2) { 
    %match(pt) {
      ax(n,cn) -> {
        if (`n == n1) 
          return `ax(n2,cn);
        else 
          return `ax(n,cn); 
      }
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        return `cut(CutPrem1(a,pa,rename(M1,n1,n2)),CutPrem2(x,px,rename(M2,n1,n2)));
      }
      // left rules
      andL(AndLPrem1(x,px,y,py,M),n) -> {
        if (`n == n1)
          return `andL(AndLPrem1(x,px,y,py,rename(M,n1,n2)),n2); 
        else
          return `andL(AndLPrem1(x,px,y,py,rename(M,n1,n2)),n); 
      }
      orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
        if (`n == n1)
          return `orL(OrLPrem1(x,px,rename(M1,n1,n2)),OrLPrem2(y,py,rename(M2,n1,n2)),n2);
        else
          return `orL(OrLPrem1(x,px,rename(M1,n1,n2)),OrLPrem2(y,py,rename(M2,n1,n2)),n);
      }
      implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
        if (`n == n1)
          return `implyL(ImplyLPrem1(x,px,rename(M1,n1,n2)),ImplyLPrem2(a,pa,rename(M2,n1,n2)),n2);
        else
          return `implyL(ImplyLPrem1(x,px,rename(M1,n1,n2)),ImplyLPrem2(a,pa,rename(M2,n1,n2)),n);
      }
      forallL(ForallLPrem1(x,px,M),t,n) -> {
        if (`n == n1)
          return `forallL(ForallLPrem1(x,px,rename(M,n1,n2)),t,n2);
        else
          return `forallL(ForallLPrem1(x,px,rename(M,n1,n2)),t,n);
      }
      existsL(ExistsLPrem1(x,px,fx,M),n) -> {
        if (`n == n1)
          return `existsL(ExistsLPrem1(x,px,fx,rename(M,n1,n2)),n2);
        else
          return `existsL(ExistsLPrem1(x,px,fx,rename(M,n1,n2)),n);
      }
      rootL(RootLPrem1(x,px,M)) -> {
        return `rootL(RootLPrem1(x,px,rename(M,n1,n2)));
      }
      foldL(id,FoldLPrem1(x,px,M),n) -> {
        if (`n == n1)
          return `foldL(id,FoldLPrem1(x,px,rename(M,n1,n2)),n2);
        else
          return `foldL(id,FoldLPrem1(x,px,rename(M,n1,n2)),n);
      }
      // right rules
      orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
        return `orR(OrRPrem1(a,pa,b,pb,rename(M,n1,n2)),cn);
      }
      andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
        return `andR(AndRPrem1(a,pa,rename(M1,n1,n2)),AndRPrem2(b,pb,rename(M2,n1,n2)),cn);
      }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
        return `implyR(ImplyRPrem1(x,px,a,pa,rename(M,n1,n2)),cn);
      }
      existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
        return `existsR(ExistsRPrem1(a,pa,rename(M,n1,n2)),t,cn);
      }
      forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
        return `forallR(ForallRPrem1(a,pa,fx,rename(M,n1,n2)),cn);
      }
      rootR(RootRPrem1(a,pa,M)) -> {
        return `rootR(RootRPrem1(a,pa,rename(M,n1,n2)));
      }
      foldR(id,FoldRPrem1(a,pa,M),cn) -> {
        return `foldR(id,FoldRPrem1(a,pa,rename(M,n1,n2)),cn); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static ProofTerm reconame(ProofTerm pt, CoName cn1, CoName cn2) { 
    %match(pt) {
      ax(n,cn) -> {
        return `cn == cn1 ? `ax(n,cn2) : `ax(n,cn); 
      }
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        return `cut(CutPrem1(a,pa,reconame(M1,cn1,cn2)),CutPrem2(x,px,reconame(M2,cn1,cn2)));
      }
      // left rules
      andL(AndLPrem1(x,px,y,py,M),n) -> {
        return `andL(AndLPrem1(x,px,y,py,reconame(M,cn1,cn2)),n); 
      }
      orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
        return `orL(OrLPrem1(x,px,reconame(M1,cn1,cn2)),OrLPrem2(y,py,reconame(M2,cn1,cn2)),n);
      }
      implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
        return `implyL(ImplyLPrem1(x,px,reconame(M1,cn1,cn2)),ImplyLPrem2(a,pa,reconame(M2,cn1,cn2)),n);
      }
      forallL(ForallLPrem1(x,px,M),t,n) -> {
        return `forallL(ForallLPrem1(x,px,reconame(M,cn1,cn2)),t,n);
      }
      existsL(ExistsLPrem1(x,px,fx,M),n) -> {
        return `existsL(ExistsLPrem1(x,px,fx,reconame(M,cn1,cn2)),n);
      }
      rootL(RootLPrem1(x,px,M)) -> {
        return `rootL(RootLPrem1(x,px,reconame(M,cn1,cn2)));
      }
      foldL(id,FoldLPrem1(x,px,M),n) -> {
        return `foldL(id,FoldLPrem1(x,px,reconame(M,cn1,cn2)),n);
      }
      // right rules
      orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
        if (`cn == cn1)
          return `orR(OrRPrem1(a,pa,b,pb,reconame(M,cn1,cn2)),cn2);
        else
          return `orR(OrRPrem1(a,pa,b,pb,reconame(M,cn1,cn2)),cn);
      }
      andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
        if (`cn == cn1)
          return `andR(AndRPrem1(a,pa,reconame(M1,cn1,cn2)),AndRPrem2(b,pb,reconame(M2,cn1,cn2)),cn2);
        else
          return `andR(AndRPrem1(a,pa,reconame(M1,cn1,cn2)),AndRPrem2(b,pb,reconame(M2,cn1,cn2)),cn);
      }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
        if (`cn == cn1)
          return `implyR(ImplyRPrem1(x,px,a,pa,reconame(M,cn1,cn2)),cn2);
        else
          return `implyR(ImplyRPrem1(x,px,a,pa,reconame(M,cn1,cn2)),cn);
      }
      existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
        if (`cn == cn1)
          return `existsR(ExistsRPrem1(a,pa,reconame(M,cn1,cn2)),t,cn2);
        else
          return `existsR(ExistsRPrem1(a,pa,reconame(M,cn1,cn2)),t,cn);
      }
      forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
        if (`cn == cn1)
          return `forallR(ForallRPrem1(a,pa,fx,reconame(M,cn1,cn2)),cn2);
        else
          return `forallR(ForallRPrem1(a,pa,fx,reconame(M,cn1,cn2)),cn);
      }
      rootR(RootRPrem1(a,pa,M)) -> {
        return `rootR(RootRPrem1(a,pa,reconame(M,cn1,cn2)));
      }
      foldR(id,FoldRPrem1(a,pa,M),cn) -> {
        if (`cn == cn1)
          return `foldR(id,FoldRPrem1(a,pa,reconame(M,cn1,cn2)),cn2); 
        else
          return `foldR(id,FoldRPrem1(a,pa,reconame(M,cn1,cn2)),cn); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }
}
