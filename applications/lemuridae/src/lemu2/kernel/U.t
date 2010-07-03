package lemu2.kernel;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.proofterms.types.fovarlist.fovarList;
import lemu2.kernel.proofterms.types.namelist.nameList;
import lemu2.kernel.proofterms.types.conamelist.conameList;
import lemu2.util.*;
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

  public static NamedAx lookup(Theory th, String id) {
    %match(th) {
      theory(_*,r@namedAx(n,_),_*) && n == id -> { return `r; }
    }
    throw new RuntimeException("rule " + id + " not in scope");
  }

  /* returns null if v not in dom(s) */
  public static Term lookup(FoSubst s, FoVar v) {
    %match(s) {
      fosubst(_*,fomap(x,t),_*) && x << FoVar v -> { return `t; }
    }
    return null;
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

  %strategy SubstFoVarInTerm(FoVar x, Term u) extends Identity() {
    visit Term {
     var(y) && y == x -> { return `u; }
    }
  }

  public static Term substFoVar(Term t, FoVar x, Term u) {
    try { return (Term) `TopDown(SubstFoVarInTerm(x,u)).visit(t); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  private static TermList substFoVar(TermList tl, FoVar x, Term u) {
    %match(tl) {
      termList() -> { return `EmptytermList(); }
      termList(t,ts*) -> { return `ConstermList(substFoVar(t,x,u),substFoVar(ts,x,u)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static Prop substFoVar(Prop prop, FoVar x, Term t) {
    %match(prop) {
      relApp(r,tl) -> { return `relApp(r,substFoVar(tl,x,t)); }
      and(p,q) -> { return `and(substFoVar(p,x,t),substFoVar(q,x,t)); }
      or(p,q) -> { return `or(substFoVar(p,x,t),substFoVar(q,x,t)); }
      implies(p,q) -> { return `implies(substFoVar(p,x,t),substFoVar(q,x,t)); }
      forall(Fa(y,p)) -> { return `forall(Fa(y,substFoVar(p,x,t))); }
      exists(Ex(y,p)) -> { return `exists(Ex(y,substFoVar(p,x,t))); }
      bottom() -> { return `bottom(); }
      top() -> { return `top(); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static ProofTerm substFoVar(ProofTerm pt, FoVar x1, Term t1) {
    %match(pt) { 
      ax(n,cn) -> {
        return `ax(n,cn); 
      }
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        return `cut(
            CutPrem1(a,substFoVar(pa,x1,t1),substFoVar(M1,x1,t1)),
            CutPrem2(x,substFoVar(px,x1,t1),substFoVar(M2,x1,t1)));
      }
      // left rules
      falseL(x) -> { return `falseL(x); }
      andL(AndLPrem1(x,px,y,py,M),n) -> {
        return `andL(AndLPrem1( x,substFoVar(px,x1,t1), y,substFoVar(py,x1,t1), substFoVar(M,x1,t1)),n); 
      }
      orL(OrLPrem1(x,px,M1),OrLPrem2(y,py,M2),n) -> {
        return `orL(
            OrLPrem1(x,substFoVar(px,x1,t1),substFoVar(M1,x1,t1)),
            OrLPrem2(y,substFoVar(py,x1,t1),substFoVar(M2,x1,t1)),n);
      }
      implyL(ImplyLPrem1(x,px,M1),ImplyLPrem2(a,pa,M2),n) -> {
        return `implyL(
            ImplyLPrem1(x,substFoVar(px,x1,t1),substFoVar(M1,x1,t1)),
            ImplyLPrem2(a,substFoVar(pa,x1,t1),substFoVar(M2,x1,t1)),n);
      }
      forallL(ForallLPrem1(x,px,M),t,n) -> {
        return `forallL(ForallLPrem1(x,substFoVar(px,x1,t1),substFoVar(M,x1,t1)),substFoVar(t,x1,t1),n);
      }
      existsL(ExistsLPrem1(x,px,fx,M),n) -> {
        return `existsL(ExistsLPrem1(x,substFoVar(px,x1,t1),fx,substFoVar(M,x1,t1)),n);
      }
      foldL(id,FoldLPrem1(x,px,M),n) -> {
        return `foldL(id,FoldLPrem1(x,substFoVar(px,x1,t1),substFoVar(M,x1,t1)),n);
      }
      // right rules
      trueR(a) -> { return `trueR(a); }
      orR(OrRPrem1(a,pa,b,pb,M),cn) -> {
        return `orR(OrRPrem1(a,substFoVar(pa,x1,t1),b,substFoVar(pb,x1,t1),substFoVar(M,x1,t1)),cn);
      }
      andR(AndRPrem1(a,pa,M1),AndRPrem2(b,pb,M2),cn) -> {
        return `andR(
            AndRPrem1(a,substFoVar(pa,x1,t1),substFoVar(M1,x1,t1)),
            AndRPrem2(b,substFoVar(pb,x1,t1),substFoVar(M2,x1,t1)),cn);
      }
      implyR(ImplyRPrem1(x,px,a,pa,M),cn) -> {
        return `implyR(ImplyRPrem1(x,substFoVar(px,x1,t1),a,substFoVar(pa,x1,t1),substFoVar(M,x1,t1)),cn);
      }
      existsR(ExistsRPrem1(a,pa,M),t,cn) -> {
        return `existsR(ExistsRPrem1(a,substFoVar(pa,x1,t1),substFoVar(M,x1,t1)),substFoVar(t,x1,t1),cn);
      }
      forallR(ForallRPrem1(a,pa,fx,M),cn) -> {
        return `forallR(ForallRPrem1(a,substFoVar(pa,x1,t1),fx,substFoVar(M,x1,t1)),cn);
      }
      foldR(id,FoldRPrem1(x,px,M),cn) -> {
        return `foldR(id,FoldRPrem1(x,substFoVar(px,x1,t1),substFoVar(M,x1,t1)),cn);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
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
        return (`n == n1) ? `ax(n2,cn) : `ax(n,cn);
      }
      cut(CutPrem1(a,pa,M1),CutPrem2(x,px,M2)) -> {
        return `cut(CutPrem1(a,pa,rename(M1,n1,n2)),CutPrem2(x,px,rename(M2,n1,n2)));
      }
      // left rules
      falseL(n) -> {
        return (`n == n1) ? `falseL(n2) : `falseL(n);
      }
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
      trueR(cn) -> {
        return `trueR(cn);
      }
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
      falseL(n) -> {
        return `falseL(n);
      }
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
      trueR(cn) -> {
        return `cn == cn1 ? `trueR(cn2) : `trueR(cn); 
      }
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

  private static boolean topIntroducedName(ProofTerm pt, Name name) { 
    %match (pt) {
      ax(n,_) -> { return `name == `n; }
      falseL(n) -> { return `name == `n; }
      andL(_,n) -> { return `name == `n; }
      orL(_,_,n) -> { return `name == `n; }
      implyL(_,_,n) -> { return `name == `n; }
      forallL(_,_,n) -> { return `name == `n; }
      existsL(_,n) -> { return `name == `n; }
      foldL(_,_,n) -> { return `name == `n; }
    }
    return false;
  }

  private static boolean topIntroducedCoName(ProofTerm pt, CoName coname) { 
    %match (pt) {
      ax(_,cn) -> { return `coname == `cn; }
      trueR(cn) -> { return `coname == `cn; }
      orR(_,cn) -> { return `coname == `cn; }
      andR(_,_,cn) -> { return `coname == `cn; }
      implyR(_,cn) -> { return `coname == `cn; }
      existsR(_,_,cn) -> { return `coname == `cn; }
      forallR(_,cn) -> { return `coname == `cn; }
      foldR(_,_,cn) -> { return `coname == `cn; }
    }
    return false;
  }

  private static nameList getFreeNames(NameList ctx, ProofTerm pt) {
    nameList c = (nameList) ctx;
    %match (pt) {
      ax(n,_) -> {
        return (nameList) (c.contains(`n) ? `nameList() : `nameList(n));
      }
      cut(CutPrem1(_,_,M1),CutPrem2(x,_,M2)) -> {
        NameList M1names = `getFreeNames(c,M1);
        NameList M2names = `getFreeNames(nameList(x,c*),M2);
        return (nameList) `nameList(M1names*,M2names*); 
      }
      // left rules
      falseL(n) -> {
        return (nameList) (c.contains(`n) ? `nameList() : `nameList(n)); 
      }
      andL(AndLPrem1(x,_,y,_,M),n) -> {
        NameList Mnames = `getFreeNames(nameList(x,y,c*),M);
        return (nameList) (c.contains(`n) ? Mnames : `nameList(n,Mnames*)); 
      }
      orL(OrLPrem1(x,_,M1),OrLPrem2(y,_,M2),n) -> {
        NameList M1names = `getFreeNames(nameList(x,c*),M1);
        NameList M2names = `getFreeNames(nameList(y,c*),M2);
        return (nameList) (c.contains(`n) ? `nameList(M1names*,M2names*) : `nameList(n,M1names*,M2names*));
      }
      implyL(ImplyLPrem1(x,_,M1),ImplyLPrem2(_,_,M2),n) -> {
        NameList M1names = `getFreeNames(nameList(x,c*),M1);
        NameList M2names = `getFreeNames(c,M2);
        return (nameList) (c.contains(`n) ? `nameList(M1names*,M2names*) : `nameList(n,M1names*,M2names*)); 
      }
      forallL(ForallLPrem1(x,_,M),_,n) -> {
        NameList Mnames = `getFreeNames(nameList(x,c*),M);
        return (nameList) (c.contains(`n) ? Mnames : `nameList(n,Mnames*)); 
      }
      existsL(ExistsLPrem1(x,_,_,M),n) -> {
        NameList Mnames = `getFreeNames(nameList(x,c*),M);
        return (nameList) (c.contains(`n) ? Mnames : `nameList(n,Mnames*)); 
      }
      rootL(RootLPrem1(x,_,M)) -> {
        return `getFreeNames(nameList(x,c*),M);
      }
      foldL(_,FoldLPrem1(x,_,M),n) -> {
        NameList Mnames = `getFreeNames(nameList(x,c*),M);
        return (nameList) (c.contains(`n) ? Mnames : `nameList(n,Mnames*)); 
      }
      // right rules
      orR(OrRPrem1(_,_,_,_,M),_) -> {
        return `getFreeNames(c,M);
      }
      andR(AndRPrem1(_,_,M1),AndRPrem2(_,_,M2),_) -> {
        NameList M1names = `getFreeNames(c,M1);
        NameList M2names = `getFreeNames(c,M2);
        return (nameList) `nameList(M1names*,M2names*); 
      }
      implyR(ImplyRPrem1(x,_,_,_,M),_) -> {
        return `getFreeNames(nameList(x,c*),M);
      }
      existsR(ExistsRPrem1(_,_,M),_,_) -> {
        return `getFreeNames(c,M);
      }
      forallR(ForallRPrem1(_,_,_,M),_) -> {
        return `getFreeNames(c,M);
      }
      rootR(RootRPrem1(_,_,M)) -> {
        return `getFreeNames(c,M);
      }
      foldR(_,FoldRPrem1(_,_,M),_) -> {
        return `getFreeNames(c,M);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static nameList getFreeNames(ProofTerm pt) {
    return `getFreeNames(nameList(),pt);
  }

  private static boolean nameAppears(ProofTerm pt, Name n) {
    return getFreeNames(pt).contains(n);
  }

  public static boolean freshlyIntroducedName(ProofTerm pt, Name n) {
    return topIntroducedName(pt,n) && !isImplicitContraction(pt);
  }

  private static conameList getFreeCoNames(CoNameList ctx, ProofTerm pt) {
    conameList c = (conameList) ctx;
    %match (pt) {
      ax(_,cn) -> {
        return (conameList) (c.contains(`cn) ? `conameList() : `conameList(cn));
      }
      cut(CutPrem1(a,_,M1),CutPrem2(_,_,M2)) -> {
        CoNameList M1conames = `getFreeCoNames(conameList(a,c*),M1);
        CoNameList M2conames = `getFreeCoNames(c,M2);
        return (conameList) `conameList(M1conames*,M2conames*); 
      }
      // left rules
      falseL(_) -> {
        return (conameList) `conameList();
      }
      andL(AndLPrem1(_,_,_,_,M),_) -> {
        return `getFreeCoNames(c,M);
      }
      orL(OrLPrem1(_,_,M1),OrLPrem2(_,_,M2),_) -> {
        CoNameList M1conames = `getFreeCoNames(c,M1);
        CoNameList M2conames = `getFreeCoNames(c,M2);
        return (conameList) `conameList(M1conames*,M2conames*); 
      }
      implyL(ImplyLPrem1(_,_,M1),ImplyLPrem2(a,_,M2),_) -> {
        CoNameList M1names = `getFreeCoNames(c,M1);
        CoNameList M2names = `getFreeCoNames(conameList(a,c*),M2);
        return (conameList) `conameList(M1names*,M2names*); 
      }
      forallL(ForallLPrem1(_,_,M),_,_) -> {
        return `getFreeCoNames(c,M);
      }
      existsL(ExistsLPrem1(_,_,_,M),_) -> {
        return `getFreeCoNames(c,M);
      }
      rootL(RootLPrem1(_,_,M)) -> {
        return `getFreeCoNames(c,M);
      }
      foldL(_,FoldLPrem1(_,_,M),_) -> {
        return `getFreeCoNames(c,M);
      }
      // right rules
      orR(OrRPrem1(a,_,b,_,M),cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,b,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
      }
      andR(AndRPrem1(a,_,M1),AndRPrem2(b,_,M2),cn) -> {
        CoNameList M1conames = `getFreeCoNames(conameList(a,c*),M1);
        CoNameList M2conames = `getFreeCoNames(conameList(b,c*),M2);
        return (conameList) (c.contains(`cn)  ? `conameList(M1conames*,M2conames*) : `conameList(cn,M1conames*,M2conames*)); 
      }
      implyR(ImplyRPrem1(_,_,a,_,M),cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
      }
      existsR(ExistsRPrem1(a,_,M),_,cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
      }
      forallR(ForallRPrem1(a,_,_,M),cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
      }
      rootR(RootRPrem1(a,_,M)) -> {
        return `getFreeCoNames(conameList(a,c*),M);
      }
      foldR(_,FoldRPrem1(a,_,M),cn) -> {
        CoNameList Mconames = `getFreeCoNames(conameList(a,c*),M);
        return (conameList) (c.contains(`cn) ? Mconames : `conameList(cn,Mconames*)); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static conameList getFreeCoNames(ProofTerm pt) {
    return `getFreeCoNames(conameList(),pt);
  }

  private static boolean conameAppears(ProofTerm pt, CoName cn) {
    return getFreeCoNames(pt).contains(cn);
  }

  public static boolean freshlyIntroducedCoName(ProofTerm pt, CoName cn) {
    return topIntroducedCoName(pt,cn) && !isImplicitContraction(pt);
  }

  public static boolean isImplicitContraction(ProofTerm pt) {
    %match (pt) {
      // left rules
      andL(AndLPrem1(_,_,_,_,M),n) -> { return `nameAppears(M,n); }
      orL(OrLPrem1(_,_,M1),OrLPrem2(_,_,M2),n) -> { return `nameAppears(M1,n) || `nameAppears(M2,n); }
      implyL(ImplyLPrem1(_,_,M1),ImplyLPrem2(_,_,M2),n) -> { return `nameAppears(M1,n) || `nameAppears(M2,n); }
      forallL(ForallLPrem1(_,_,M),_,n) -> { return `nameAppears(M,n); }
      existsL(ExistsLPrem1(_,_,_,M),n) -> { return `nameAppears(M,n); }
      foldL(_,FoldLPrem1(_,_,M),n) -> { return `nameAppears(M,n); }
      // right rules
      orR(OrRPrem1(_,_,_,_,M),cn) -> { return `conameAppears(M,cn); }
      andR(AndRPrem1(_,_,M1),AndRPrem2(_,_,M2),cn) -> { return `conameAppears(M1,cn) || `conameAppears(M2,cn); }
      implyR(ImplyRPrem1(_,_,_,_,M),cn) -> { return `conameAppears(M,cn); }
      existsR(ExistsRPrem1(_,_,M),_,cn) -> { return `conameAppears(M,cn); }
      forallR(ForallRPrem1(_,_,_,M),cn) -> { return `conameAppears(M,cn); }
      foldR(_,FoldRPrem1(_,_,M),cn) -> { return `conameAppears(M,cn); }
    }
    return false;
  }

  /**
   * s[x:=t]
   */
  public static LTerm substName(LTerm s, Name x, LTerm t) {
    %match(s) {
      lvar(v) -> { return (`v == x) ? t : `lvar(v); }
      lam(Lam(y,py,u)) -> { return `lam(Lam(y,py,substName(u,x,t))); }
      flam(FLam(fx,u)) -> { return `flam(FLam(fx,substName(u,x,t))); }
      activ(Act(a,pa,u)) -> { return `activ(Act(a,pa,substName(u,x,t))); }
      lapp(u,v) -> { return `lapp(substName(u,x,t), substName(v,x,t)); }
      fapp(u,ft) -> { return `fapp(substName(u,x,t), ft); }
      pair(u,v) -> { return `pair(substName(u,x,t), substName(v,x,t)); }
      proj1(u) -> { return `proj1(substName(u,x,t)); }
      proj2(u) -> { return `proj2(substName(u,x,t)); }
      caseof(u,Alt(y,py,v),Alt(z,pz,w)) -> { 
        return `caseof(substName(u,x,t),
            Alt(y,py,substName(v,x,t)),
            Alt(z,pz,substName(w,x,t)));
      }
      left(u,p) -> { return `left(substName(u,x,t),p); }
      right(u,p) -> { return `right(substName(u,x,t),p); }
      //letin(Letin(fx,y,
      passiv(mv,u) -> { return `passiv(mv,substName(u,x,t)); }
      unit() -> { return `unit(); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  /**
   * s[fx:=ft]
   */
  public static LTerm substFoVar(LTerm s, FoVar fx, Term ft) {
    %match(s) {
      lvar(v) -> { return `lvar(v); }
      lam(Lam(y,py,u)) -> { return `lam(Lam(y,py,substFoVar(u,fx,ft))); }
      flam(FLam(fy,u)) -> { return `flam(FLam(fy,substFoVar(u,fx,ft))); }
      activ(Act(a,pa,u)) -> { return `activ(Act(a,pa,substFoVar(u,fx,ft))); }
      lapp(u,v) -> { return `lapp(substFoVar(u,fx,ft), substFoVar(v,fx,ft)); }
      fapp(u,fu) -> { return `fapp(substFoVar(u,fx,ft), substFoVar(fu,fx,ft)); }
      pair(u,v) -> { return `pair(substFoVar(u,fx,ft), substFoVar(v,fx,ft)); }
      proj1(u) -> { return `proj1(substFoVar(u,fx,ft)); }
      proj2(u) -> { return `proj2(substFoVar(u,fx,ft)); }
      caseof(u,Alt(x,px,v),Alt(y,py,w)) -> { 
        return `caseof(substFoVar(u,fx,ft),
            Alt(x,px,substFoVar(v,fx,ft)),
            Alt(y,py,substFoVar(w,fx,ft)));
      }
      left(u,p) -> { return `left(substFoVar(u,fx,ft),p); }
      right(u,p) -> { return `right(substFoVar(u,fx,ft),p); }
      passiv(mv,u) -> { return `passiv(mv,substFoVar(u,fx,ft)); }
      unit() -> { return `unit(); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }


  /**
   * s[a:=b]
   */
  public static LTerm reconame(LTerm s, CoName a, CoName b) {
    %match(s) {
      lvar(v) -> { return `lvar(v); }
      lam(Lam(y,py,u)) -> { return `lam(Lam(y,py,reconame(u,a,b))); }
      flam(FLam(fx,u)) -> { return `flam(FLam(fx,reconame(u,a,b))); }
      activ(Act(c,pc,u)) -> { return `activ(Act(c,pc,reconame(u,a,b))); }
      lapp(u,v) -> { return `lapp(reconame(u,a,b), reconame(v,a,b)); }
      fapp(u,ft) -> { return `fapp(reconame(u,a,b), ft); }
      pair(u,v) -> { return `pair(reconame(u,a,b), reconame(v,a,b)); }
      proj1(u) -> { return `proj1(reconame(u,a,b)); }
      proj2(u) -> { return `proj2(reconame(u,a,b)); }
      caseof(u,Alt(x,px,v),Alt(y,py,w)) -> { 
        return `caseof(reconame(u,a,b),
            Alt(x,px,reconame(v,a,b)),
            Alt(y,py,reconame(w,a,b)));
      }
      left(u,p) -> { return `left(reconame(u,a,b),p); }
      right(u,p) -> { return `right(reconame(u,a,b),p); }
      passiv(c,u) -> {
        if (`c == a) return `passiv(b,reconame(u,a,b)); 
        else         return `passiv(c,reconame(u,a,b)); 
      }
      unit() -> { return `unit(); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static conameList getFreeCoNames(conameList ctx, LTerm pt) {
    %match(pt) {
      (lvar|unit)[] -> { return (conameList) `conameList(); }
      lam(Lam(_,_,u)) -> { return `getFreeCoNames(ctx,u); }
      flam(FLam(_,u)) -> { return `getFreeCoNames(ctx,u); }
      activ(Act(c,_,u)) -> { return `getFreeCoNames(conameList(c,ctx*),u); }
      lapp(u,v) -> {
        conameList ucn = `getFreeCoNames(ctx,u); 
        conameList vcn = `getFreeCoNames(ctx,v); 
        return (conameList) `conameList(ucn*,vcn*); 
      }
      fapp(u,_) -> { return `getFreeCoNames(ctx,u); }
      pair(u,v) -> { 
        conameList ucn = `getFreeCoNames(ctx,u); 
        conameList vcn = `getFreeCoNames(ctx,v); 
        return (conameList) `conameList(ucn*,vcn*); 
      }
      proj1(u) -> { return `getFreeCoNames(ctx,u); }
      proj2(u) -> { return `getFreeCoNames(ctx,u); }
      caseof(u,Alt(_,_,v),Alt(_,_,w)) -> { 
        conameList ucn = `getFreeCoNames(ctx,u); 
        conameList vcn = `getFreeCoNames(ctx,v); 
        conameList wcn = `getFreeCoNames(ctx,w); 
        return (conameList) `conameList(ucn*,vcn*,wcn*); 
      }
      left(u,_) -> { return `getFreeCoNames(ctx,u); }
      right(u,_) -> { return `getFreeCoNames(ctx,u); }
      witness(_,u,_) -> { return `getFreeCoNames(ctx,u); }
      letin(Letin(_,_,_,u,v)) -> {
        conameList ucn = `getFreeCoNames(ctx,u); 
        conameList vcn = `getFreeCoNames(ctx,v); 
        return (conameList) `conameList(ucn*,vcn*);
      }
      passiv(c,u) -> {
        conameList ucn = `getFreeCoNames(ctx,u); 
        return ctx.contains(`c) ? ucn : `conameList(c,ucn*); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  public static conameList getFreeCoNames(LTerm pt) {
    return `getFreeCoNames(conameList(),pt);
  }
  
  public static boolean freeIn(CoName a, LTerm pt) {
    return getFreeCoNames(pt).contains(a);
  }

  /* lambda-bar mu mu~ */

  private static nameList getFreeNames(nameList ctx, LMMTerm pt) {
    %match(pt) {
      lmmVar(x) -> { return ctx.`contains(x) ? ctx : `nameList(x,ctx*); }
      lmmMu(LmmMu(_,_,u)) -> { return `getFreeNames(ctx,u); }
      lmmLam(LmmLam(x,_,u)) -> { return `getFreeNames(nameList(x,ctx*),u); }
      lmmFLam(LmmFLam(_,u)) -> { return `getFreeNames(ctx,u); }
      lmmFoldR(_,u) -> { return `getFreeNames(ctx,u); }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static nameList getFreeNames(LMMTerm pt) {
    return `getFreeNames(nameList(),pt);
  }

  public static boolean freeIn(Name x, LMMTerm pt) {
    return getFreeNames(pt).contains(x);
  }

  private static nameList getFreeNames(nameList ctx, LMMContext e) {
    %match(e) {
      lmmCoVar(_) -> { return ctx; }
      lmmMuT(LmmMuT(x,_,e1)) -> { 
        return `getFreeNames(nameList(x,ctx*),e1); 
      }
      lmmDot(v,e1) -> { 
        NameList l1 = `getFreeNames(ctx,v);
        NameList l2 = `getFreeNames(ctx,e1);
        return (nameList) `nameList(l1*,l2*);
      }
      lmmFDot(_,e1) -> { return `getFreeNames(ctx,e1); }
      lmmFoldL(_,e1) -> { return `getFreeNames(ctx,e1); }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static nameList getFreeNames(LMMContext e) {
    return `getFreeNames(nameList(),e);
  }

  public static boolean freeIn(Name x, LMMContext e) {
    return getFreeNames(e).contains(x);
  }

  private static nameList getFreeNames(nameList ctx, LMMCommand c) {
    %match(c) {
      lmmCommand(v,e) -> {
        NameList l1 = `getFreeNames(ctx,v);
        NameList l2 = `getFreeNames(ctx,e);
        return (nameList) `nameList(l1*,l2*);
      }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static nameList getFreeNames(LMMCommand c) {
    return `getFreeNames(nameList(),c);
  }

  public static boolean freeIn(Name x, LMMCommand c) {
    return getFreeNames(c).contains(x);
  }

  private static conameList getFreeCoNames(conameList ctx, LMMTerm pt) {
    %match(pt) {
      lmmVar(_) -> { return  ctx; }
      lmmMu(LmmMu(a,_,u)) -> { return `getFreeCoNames(conameList(a,ctx*),u); }
      lmmLam(LmmLam(_,_,u)) -> { return `getFreeCoNames(ctx,u); }
      lmmFLam(LmmFLam(_,u)) -> { return `getFreeCoNames(ctx,u); }
      lmmFoldR(_,u) -> { return `getFreeCoNames(ctx,u); }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static conameList getFreeCoNames(LMMTerm pt) {
    return `getFreeCoNames(conameList(),pt);
  }

  public static boolean freeIn(CoName x, LMMTerm pt) {
    return getFreeCoNames(pt).contains(x);
  }

  private static conameList getFreeCoNames(conameList ctx, LMMContext e) {
    %match(e) {
      lmmCoVar(a) -> { return ctx.`contains(a) ? ctx : `conameList(a,ctx*); }
      lmmMuT(LmmMuT(_,_,e1)) -> { 
        return `getFreeCoNames(ctx,e1); 
      }
      lmmDot(v,e1) -> { 
        CoNameList l1 = `getFreeCoNames(ctx,v);
        CoNameList l2 = `getFreeCoNames(ctx,e1);
        return (conameList) `conameList(l1*,l2*);
      }
      lmmFDot(_,e1) -> { return `getFreeCoNames(ctx,e1); }
      lmmFoldL(_,e1) -> { return `getFreeCoNames(ctx,e1); }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static conameList getFreeCoNames(LMMContext e) {
    return `getFreeCoNames(conameList(),e);
  }

  public static boolean freeIn(CoName x, LMMContext e) {
    return getFreeCoNames(e).contains(x);
  }

  private static conameList getFreeCoNames(conameList ctx, LMMCommand c) {
    %match(c) {
      lmmCommand(v,e) -> {
        CoNameList l1 = `getFreeCoNames(ctx,v);
        CoNameList l2 = `getFreeCoNames(ctx,e);
        return (conameList) `conameList(l1*,l2*);
      }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static conameList getFreeCoNames(LMMCommand c) {
    return `getFreeCoNames(conameList(),c);
  }

  public static boolean freeIn(CoName x, LMMCommand c) {
    return getFreeCoNames(c).contains(x);
  }
}
