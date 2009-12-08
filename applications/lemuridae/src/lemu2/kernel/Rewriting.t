package lemu2.kernel;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.proofterms.types.fovarlist.fovarList;
import lemu2.util.*;

import java.util.Collection;
import tom.library.sl.*;

public class Rewriting {

  %include { kernel/proofterms/proofterms.tom } 
  %include { sl.tom } 

  /*-- first-order term matching --*/

  /* returns null if no match */
  private static FoSubst match(TermList subjects, TermList patterns, FoSubst subst) {
    %match(subjects,patterns) {
      termList(),termList() -> { return subst; }
      termList(x,xs*),termList(p,ps*) -> {
        FoSubst s = `match(x,p,subst);
        if (s!=null) return `match(xs,ps,s);
        else return null;
      }
    }
    return null;
  }

  /* returns null if no match */
  private static FoSubst match(Term subject, Term pattern, FoSubst subst) {
    %match(pattern,subject) {
      var(v), t -> {
        Term u = U.`lookup(subst,v);
        if (u!=null) return u == `t ? subst : null;
        else return `fosubst(fomap(v,t),subst*);
      }
      funApp(f,l1), funApp(f,l2) -> {
        return `match(l2,l1,subst);
      }
    }
    return null;
  }

  /* returns null if no match */
  public static FoSubst match(Term subject, Term pattern) {
    return `match(subject,pattern,fosubst());
  }

  /*-- propositions matching --*/

  %strategy SubstFoVar(FoVar x, Term u) extends Identity() {
    visit Term {
      var(y) -> { if(`y.equals(x)) return `u; }
    }
  }

  private static Prop substFoVar(Prop p, FoVar x, Term u) {
    try { return (Prop) `TopDown(SubstFoVar(x,u)).visit(p); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  /* returns null if no match */
  private static FoSubst match(Prop subject, Prop pattern, FoSubst subst) {
    %match(pattern,subject) {
      relApp(r,l1), relApp(r,l2) -> {
        return `match(l2,l1,subst);
      }
      and(p1,p2), and(q1,q2) -> {
        FoSubst s = `match(q1,p1,subst);
        return s==null ? null : `match(q2,p2,s);
      }
      or(p1,p2), or(q1,q2) -> {
        FoSubst s = `match(q1,p1,subst);
        return s==null ? null : `match(q2,p2,s);
      }
      implies(p1,p2), implies(q1,q2) -> {
        FoSubst s = `match(q1,p1,subst);
        return s==null ? null : `match(q2,p2,s);
      }
      forall(Fa(x,p)), forall(Fa(y,q)) -> {
        return `match(q,substFoVar(p,x,var(y)),subst);
      }
      exists(Ex(x,p)), exists(Ex(y,q)) -> {
        return `match(q,substFoVar(p,x,var(y)),subst);
      }
      bottom(), bottom() -> { return subst; }
      top(), top() -> { return subst; }
    }
    return null;
  }

  public static FoSubst match(Prop subject, Prop pattern) {
    return `match(subject,pattern,fosubst());
  }

  /*-- rewriting --*/

  %strategy SubstFoVars(FoSubst subst) extends Identity() {
    visit Term {
      var(v) -> { 
        Term t = U.`lookup(subst,v);
        if (t!=null) return t;
      }
    }
  }

  /* term rewriting */

  private static Term applySubst(Term t, FoSubst s) {
    try { return (Term) `TopDown(SubstFoVars(s)).visitLight(t); }
    catch (VisitFailure e) { throw new RuntimeException("nevers happens"); }
  }

  private static Term rewrite(Term t, TermRewriteRule r) {
    %match(r) {
      termrrule(_,trule(_,lhs,rhs)) -> {
        FoSubst s = `match(t,lhs);
        if(s!=null) return `applySubst(rhs,s);
        else return null;
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Term rewrite(Term t, TermRewriteRules rs) {
    %match(rs) {
      termrrules(_*,r,_*) -> { 
        Term u = `rewrite(t,r); 
        if(u!=null) return u;
      }
    }
    return null;
  }

  %strategy Rewrite(TermRewriteRules rs) extends Fail() {
    visit Term {
      t -> { 
        Term u = `rewrite(t,rs); 
        if(u!=null) return u;
      }
    }
  }

  public static Term normalize(Term t, TermRewriteRules rs) {
    try { return (Term) `Outermost(Rewrite(rs)).visitLight(t); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  /* prop rewriting */

  private static Prop applySubst(Prop p, FoSubst s) {
    try { return (Prop) `TopDown(SubstFoVars(s)).visitLight(p); }
    catch (VisitFailure e) { throw new RuntimeException("nevers happens"); }
  }

  /**
   * rewrites p using r
   **/
  public static Prop rewrite(Prop p, PropRewriteRule r) {
    %match(r) {
      proprrule(_,prule(_,lhs,rhs)) -> {
        FoSubst s = `match(p,lhs);
        if(s!=null) return `applySubst(rhs,s);
        else return null;
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  /**
   * returns null if no rule matches,
   * rewrite with one that matches otherwise
   **/
  public static Prop rewrite(Prop p, PropRewriteRules rs) {
    %match(rs) {
      proprrules(_*,r,_*) -> { 
        Prop q = `rewrite(p,r); 
        if(q!=null) return q;
      }
    }
    return null;
  }

  /**
   * returns null if no rule matches, one that matches otherwise
   **/
  public static PropRewriteRule rewrites(Prop p, PropRewriteRules rs) {
    %match(rs) {
      proprrules(_*,r@proprrule(_,prule(_,lhs,_)),_*) -> {
        FoSubst s = `match(p,lhs);
        if(s!=null) return `r;
      }
    }
    return null;
  }

  %strategy Rewrite2(TermRewriteRules trs, PropRewriteRules prs) extends Fail() {
    visit Term {
      t -> { 
        Term u = `rewrite(t,trs); 
        if(u!=null) return u;
      }
    }
    visit Prop {
      p -> {
        Prop q = `rewrite(p,prs);
        if(q!=null) return q;
      }
    }
  }

  public static Prop normalize(Prop p, TermRewriteRules trs, PropRewriteRules prs) {
    try { return (Prop) `Outermost(Rewrite2(trs,prs)).visitLight(p); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }
}
