package lazyml;

import tom.library.sl.*;
import lazyml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class PreProc {

  %include { lambda/lambda.tom }
  %include { sl.tom }

  private static PVarList freshPvarsFromPList(PatternList pl) {
    %match(pl) {
      PList() -> { return `PVarList(); }
      PList(_,ps*) -> {
        LVar fresh = LVar.freshLVar("p");
        PVarList nps = `freshPvarsFromPList(ps);
        return `PVarList(fresh,nps*); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Partition partitionVar(Equations vs, Equations qs) {
    %match(qs) {
      Equations() -> { return `Part(vs,Equations()); }
      Equations(eq,eqs*) -> {
        %match(eq) {
          Equation(PList(PVar[],_*),_) -> {
            return `partitionVar(Equations(vs*,eq),eqs*);
          }
          _ -> {
            return `Part(vs,qs);
          }
        }
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Partition partitionCon(Equations vs, Equations qs) {
    %match(qs) {
      Equations() -> { return `Part(vs,Equations()); }
      Equations(eq,eqs*) -> {
        %match(eq) {
          Equation(PList(PFun[],_*),_) -> {
            return `partitionCon(Equations(vs*,eq),eqs*);
          }
          _ -> {
            return `Part(vs,qs);
          }
        }
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static LTerm match(PVarList vars, Equations eqns, LTerm def) {
    %match(vars) {
      PVarList() -> { 
        %match(eqns) {
          Equations() -> { return def; }
          Equations(Equation(PList(),e)) -> { return `e; }
          _ -> { 
            throw new RuntimeException("redundant clauses " + eqns); 
          }
        }
      }
      us -> {
        %match(eqns) {
          Equations() -> { return `def; }
          Equations(Equation(PList(p,_*),_),_*) -> {
            %match(p) {
              PVar[] -> {
                %match(partitionVar(Equations(),eqns)) {
                  Part(same,rem) -> {
                    return `matchVar(us,same,match(us,rem,def));
                  }
                }
              }
              PFun[] -> {
                %match(partitionCon(Equations(),eqns)) {
                  Part(same,rem) -> {
                    return `matchCon(us,same,match(us,rem,def));
                  }
                }
              }
            }
          }
        }
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  /* preconditions
       length vars > 0 
       eqns = [(PVar v1:_,_),(PVar v2:_,_),...] 
   */
  private static LTerm matchVar(PVarList vars, Equations eqns, LTerm def) {
    %match(vars) {
      PVarList(u,us*) -> {
        Equations res = `Equations();
        %match(eqns) {
          Equations(_*,Equation(PList(PVar(v),ps*),e),_*) -> {
            res = `Equations(res*,Equation(ps,Eval.substitute(e,v,Var(u))));
          }
        }
        return `match(us,res,def);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  /* preconditions
       length vars > 0 
       eqns = [(PFun f1 c1:_,_),(PFun f2 c2:_,_),...] 
   */
  private static LTerm matchCon(PVarList vars, Equations eqns, LTerm def) {
    CtorList ctors = ctors(eqns);
    Rules res = `RList();
    %match(vars) {
      PVarList(u,us*) -> {
        %match(ctors) {
          CtorList(_*,c,_*) -> {
            Equations ceqns = `Equations();
            %match(eqns) {
              Equations(_*,e@Equation(PList(PFun(f,_),_*),_),_*) 
                && f << String c -> {
                  ceqns = `Equations(ceqns*,e);
                }
            }
            res = `RList(res*,matchClause(us,ceqns,def));
          }
        }
        return `Case(Var(u),RList(res*,Rule(PVar(LVar.freshLVar("DEFAULT")),def)));
      }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static CtorList nub(CtorList l) {
    %match(l) {
      CtorList(X*,x,Y*,x,Z*) -> {
        return `nub(CtorList(X*,x,Y*,Z*));
      }
      ok -> { return `ok; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  private static CtorList ctors(Equations qs) {
    CtorList res = `CtorList();
    %match(qs) {
      Equations(_*,Equation(PList(PFun(f,_),_*),_),_*) -> { 
        res = `CtorList(res*,f); 
      }
    }
    return nub(res);
  }

  /* preconditions
        length qs > 0 
        qs = [(PFun c ps1:_,_),(PFun c ps2:_,_),...]  (ie. same c everywhere)
   */
  private static Clause matchClause(PVarList vars, Equations qs, LTerm def) {
    String con = null;
    PVarList fresh = null;
    %match(qs) {
      Equations(Equation(PList(PFun(c,ps),_*),_),_*) -> {
        con = `c;
        fresh = `freshPvarsFromPList(ps);
      }
    }
    Equations nqs = `Equations();
    %match(qs) {
      Equations(_*,Equation(PList(PFun(_,ps1),ps*),e),_*) -> {
        nqs = `Equations(nqs*,Equation(PList(ps1*,ps*),e));
      }
    }
    return `Rule(PFun(con,wrap(fresh)),match(PVarList(fresh*,vars*),nqs,def));
  }

  /* map PVar vl */
  private static PatternList wrap(PVarList vl) {
    %match(vl) {
      PVarList() -> { return `PList(); }
      PVarList(v,vs*) -> { 
        PatternList pvs = `wrap(vs*);
        return `PList(PVar(v),pvs*);
      }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  %strategy Unfold() extends Identity() {
    visit LTerm {
      Case(s,rls) -> {
        LVar sub = LVar.freshLVar("sub");
        String message = "non exhaustive patterns";
        LTerm t = `match(PVarList(sub),convert(rls),Error(message));
        return `App(Abs(lam(sub,t)),s);
      }
    }
  }

  public static Equations convert(Rules rls) {
    %match(rls) {
      RList() -> { return `Equations(); }
      RList(Rule(p,e),rs*) -> {
        Equations qs = `convert(rs);
        return  `Equations(Equation(PList(p),e),qs*);
      }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }
  
  public static LTerm unfoldCases(LTerm t) {
    try { return `BottomUp(Unfold()).visitLight(t); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  /* --- freeze --- */

  private static LTerm unit = `Constr("Unit",LTList());
  private static LVar freshvar() { return LVar.freshLVar("__U"); }

  private static LTerm freeze(LTerm t) {
    LVar fresh = freshvar();
    return `Abs(lam(fresh,t));
  }


  private static LType suspend(LType ty) {
    return `Arrow(Atom("Unit"),ty);
  }

  private static LType freeze(LType ty) {
    %match(ty) {
      TypeVar[]     -> { return `ty; }
      Atom[]        -> { return `suspend(ty); }
      Arrow(a,b)    -> { return `Arrow(suspend(freeze(a)),freeze(b)); }
      TyConstr(c,l) -> { return `suspend(TyConstr(c,freeze(l))); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static TyList freeze(TyList tyl) {
    %match(tyl) {
      EmptyTyList() -> { return `EmptyTyList(); }
      ConsTyList(ty,tys) -> { return `ConsTyList(freeze(ty),freeze(tys)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Domain freeze(Domain dom) {
    %match(dom) {
      EmptyDomain() -> { return `EmptyDomain(); }
      ConsDomain(ty,tys) -> { return `ConsDomain(freeze(ty),freeze(tys)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static LType freezeCodom(LType codom) {
    %match(codom) {
      Atom(a) -> { return `Atom(a); }
      TyConstr(c,l) -> { return `TyConstr(c,freeze(l)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Range freeze(Range rg) {
    %match(rg) {
      Range(Ra(vars,dom,codom)) -> { return `Range(Ra(vars,freeze(dom),freezeCodom(codom))); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }
  
  public static Context freeze(Context ctx) {
    %match(ctx) {
      Context() -> { return `Context(); }
      Context(RangeOf(c,r),js*) -> {
        Context fjs = `freeze(js);
        return `Context(RangeOf(c,freeze(r)),fjs*);
      }
      Context(j,js*) -> {
        Context fjs = `freeze(js);
        return `Context(j,fjs*);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static LTerm force(LTerm t) {
    return `App(t,unit);
  }

  public static LTerm thunkify(LTerm t) {
    %match(t) {
      App(t1,t2) -> { return `App(thunkify(t1),freeze(thunkify(t2))); }
      Abs(lam(x,u)) -> { return `Abs(lam(x,thunkify(u))); }
      Let(letin(x,u,v)) -> { return `Let(letin(x,freeze(thunkify(u)),thunkify(v))); }
      Fix(fixpoint(x,u)) -> { return `Fix(fixpoint(x,freeze(x,thunkify(u)))); }
      Var(x) -> { return `force(Var(x)); }
      Constr(f, children) -> { return `freeze(Constr(f,thunkify(children))); }
      PrimFun(f, children) -> { return `freeze(PrimFun(f,thunkify(children))); }
      Case(subject,rules) -> { return `Case(force(thunkify(subject)),thunkify(rules)); }
      Lit(i) -> { return `freeze(Lit(i)); }
      Chr(c) -> { return `freeze(Chr(c)); }
      Str(s) -> { return `freeze(Str(s)); }
      Error(s) -> { return `Error(s); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static LTermList thunkify(LTermList tl) {
    %match(tl) {
      LTList() -> { return `LTList(); }
      LTList(x,xs*) -> { return `ConsLTList(thunkify(x),thunkify(xs)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Rules thunkify(Rules rls) {
    %match(rls) {
      RList() -> { return `RList(); }
      RList(x,xs*) -> { return `ConsRList(thunkify(x),thunkify(xs)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Clause thunkify(Clause c) {
    %match(c) {
      Rule(p,t) -> { return `Rule(p,freeze(p,thunkify(t))); }
      //Rule(p,t) -> { return `Rule(p,thunkify(t)); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static LTerm freeze(LVar x, LTerm t) {
    return Eval.`substitute(t,x,freeze(Var(x)));
  }

  private static LTerm freeze(Pattern p, LTerm t) {
    %match(p) {
      PVar(x) -> { return Eval.`substitute(t,x,freeze(freeze(Var(x)))); }
      PFun(_,vars) -> {
        LTerm res = t;
        %match(vars) {
          PList(_*,PVar(x),_*) -> {
            t = Eval.`substitute(t,x,freeze(Var(x)));
          }
        }
        return t;
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  /* deforestation */


  %strategy Candidate() extends Fail() {
    visit LTerm {
      v@Var(LVar(_,s)) -> {
        if (`s.equals("foldr") || `s.equals("build"))
          return `v;
      }
    }
  }

  private static boolean candidate(LTerm t) {
    try { `OnceTopDown(Candidate()).visitLight(t); return true; }
    catch (VisitFailure e) { return false; }
  }

  %strategy SmartInline() extends Identity() {
    visit LTerm {
      Let(letin(x@LVar(_,n),u@!Fix[],t)) -> { 
        if (`candidate(u) && (!`n.equals("foldr")) && (!`n.equals("build"))) {
          System.err.println("inlining " + `n);
          return Eval.`substitute(t,x,u);
        }
      }
      App(Abs(lam(x,t)),u) -> { return Eval.`substitute(t,x,u); }
      Case(t,r) -> { try {return Eval.`caseof(t,r);} catch (Exception e){} }
    }
  }

  %strategy Inline() extends Identity() {
    visit LTerm {
      Let(letin(x@LVar(_,n),u,t)) -> { 
        if (`n.equals("foldr") || `n.equals("build")) {
          System.err.println("inlining " + `n);
          return Eval.`substitute(t,x,u); 
        }
      }
      App(Abs(lam(x,t)),u) -> { return Eval.`substitute(t,x,u); }
      Case(t,r) -> { try{return Eval.`caseof(t,r);} catch (Exception e){} }
    }
  }

  %strategy Debug(s:String) extends Identity() {
    visit LTerm {
      _ -> { System.err.println(s); }
    }
  }

  %strategy Deforest() extends Identity() {
    visit LTerm {
      /*
      App(App(App(App(App(Var(LVar(_,"tfold")),f),g),a),b),
          App(Var(LVar(_,"tbuild")),h)) -> { 
        System.err.println("deforestation !!");
        return `App(App(App(App(h,f),g),a),b); 
      }
      */
      App(App(App(Var(LVar(_,"foldr")),c),n),App(Var(LVar(_,"build")),h)) -> {
        System.err.println("deforestation!");
        return `App(App(h,c),n); 
      }
    }
  }

  public static LTerm deforest(LTerm t) {
    try { 
      return `Sequence(
          RepeatId(
            Sequence(
              Debug("smart inline"),
              InnermostId(SmartInline()),
              Debug("deforest"),
              InnermostId(Deforest()))),
          Debug("inline fold and build"),
          InnermostId(Inline()),
          Debug("done")).visitLight(t); 
    }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }
}
