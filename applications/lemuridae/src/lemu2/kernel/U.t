package lemu2.kernel;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.proofterms.types.fovarlist.fovarList;
import lemu2.kernel.proofterms.types.namelist.nameList;
import lemu2.kernel.proofterms.types.conamelist.conameList;
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
    try { return `TopDown(SubstFoVar(x,u)).visit(p); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  public static Term substFoVar(Term t, FoVar x, Term u) {
    try { return `TopDown(SubstFoVar(x,u)).visit(t); }
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
      orR(OrRPrem1(_,_,_,_,M),cn) -> {
        return `getFreeNames(c,M);
      }
      andR(AndRPrem1(_,_,M1),AndRPrem2(_,_,M2),cn) -> {
        NameList M1names = `getFreeNames(c,M1);
        NameList M2names = `getFreeNames(c,M2);
        return (nameList) `nameList(M1names*,M2names*); 
      }
      implyR(ImplyRPrem1(x,_,_,_,M),cn) -> {
        return `getFreeNames(nameList(x,c*),M);
      }
      existsR(ExistsRPrem1(_,_,M),_,cn) -> {
        return `getFreeNames(c,M);
      }
      forallR(ForallRPrem1(_,_,_,M),cn) -> {
        return `getFreeNames(c,M);
      }
      rootR(RootRPrem1(_,_,M)) -> {
        return `getFreeNames(c,M);
      }
      foldR(_,FoldRPrem1(_,_,M),cn) -> {
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
      andL(AndLPrem1(_,_,_,_,M),n) -> {
        return `getFreeCoNames(c,M);
      }
      orL(OrLPrem1(_,_,M1),OrLPrem2(_,_,M2),n) -> {
        CoNameList M1conames = `getFreeCoNames(c,M1);
        CoNameList M2conames = `getFreeCoNames(c,M2);
        return (conameList) `conameList(M1conames*,M2conames*); 
      }
      implyL(ImplyLPrem1(_,_,M1),ImplyLPrem2(a,_,M2),n) -> {
        CoNameList M1names = `getFreeCoNames(c,M1);
        CoNameList M2names = `getFreeCoNames(conameList(a,c*),M2);
        return (conameList) `conameList(M1names*,M2names*); 
      }
      forallL(ForallLPrem1(_,_,M),_,n) -> {
        return `getFreeCoNames(c,M);
      }
      existsL(ExistsLPrem1(_,_,_,M),n) -> {
        return `getFreeCoNames(c,M);
      }
      rootL(RootLPrem1(_,_,M)) -> {
        return `getFreeCoNames(c,M);
      }
      foldL(_,FoldLPrem1(_,_,M),n) -> {
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
      andL(AndLPrem1(x,_,y,_,M),n) -> { return `nameAppears(M,n); }
      orL(OrLPrem1(x,_,M1),OrLPrem2(y,_,M2),n) -> { return `nameAppears(M1,n) || `nameAppears(M2,n); }
      implyL(ImplyLPrem1(x,_,M1),ImplyLPrem2(a,_,M2),n) -> { return `nameAppears(M1,n) || `nameAppears(M2,n); }
      forallL(ForallLPrem1(x,_,M),_,n) -> { return `nameAppears(M,n); }
      existsL(ExistsLPrem1(x,_,_,M),n) -> { return `nameAppears(M,n); }
      foldL(_,FoldLPrem1(x,_,M),n) -> { return `nameAppears(M,n); }
      // right rules
      orR(OrRPrem1(a,_,b,_,M),cn) -> { return `conameAppears(M,cn); }
      andR(AndRPrem1(a,_,M1),AndRPrem2(b,_,M2),cn) -> { return `conameAppears(M1,cn) || `conameAppears(M2,cn); }
      implyR(ImplyRPrem1(x,_,a,_,M),cn) -> { return `conameAppears(M,cn); }
      existsR(ExistsRPrem1(a,_,M),_,cn) -> { return `conameAppears(M,cn); }
      forallR(ForallRPrem1(a,_,_,M),cn) -> { return `conameAppears(M,cn); }
      foldR(_,FoldRPrem1(a,_,M),cn) -> { return `conameAppears(M,cn); }
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
      passiv(mv,u) -> { return `passiv(mv,substName(u,x,t)); }
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
      passiv(mv,u) -> { return `passiv(mv,substFoVar(u,fx,ft)); }
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
      passiv(c,u) -> {
        if (`c == a) return `passiv(b,reconame(u,a,b)); 
        else         return `passiv(c,reconame(u,a,b)); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

}
