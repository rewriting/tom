package lazyml;

import tom.library.sl.*;
import lazyml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class PreProc {

  %include { lambda/lambda.tom }
  %include { sl.tom }

  %strategy Unfold() extends Identity() {
    visit LTerm {
      c@Case(_,RList(_*,Rule(PFun(_,PList(_*,PFun[],_*)),_),_*)) -> {
          return `unfoldCase(c);
      }
    }
  }

  private static PatternList freshPvarsFromPList(PatternList pl) {
    %match(pl) {
      PList() -> { return `PList(); }
      PList(_,ps*) -> {
        LVar fresh = LVar.freshLVar("p");
        PatternList nps = `freshPvarsFromPList(ps);
        return `PList(PVar(fresh),nps*); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static LTerm unfoldCase(LTerm t) {
    %match(t) {
      Case(s,rl) -> {
        Rules newrules = `RList();

        Set<String> ctors = new HashSet<String>();
        %match(rl) { RList(_*,Rule(PFun(f,_),_),_*) -> { ctors.`add(f); }}

        for(String f: ctors) {
          Rules rulesf = `RList();
          PatternList sample = null;
          %match(rl) {
            RList(_*,r@Rule(PFun(g,pl),_),_*) && g << String f -> { 
              sample = `pl;
              rulesf = `RList(rulesf*,r); 
            }
          }

          PatternList vars = `freshPvarsFromPList(sample);
          %match(vars) {
            PList() -> { newrules = `RList(rulesf*,newrules*); }
            PList(PVar(x),vs*) -> {
              Clause rulef = `Rule(PFun(f,vars),Case(Var(x),unfold(vs,rulesf)));
              newrules = `RList(rulef,newrules*);
            }
          }
        }
        return `Case(s,newrules);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static Rules unfold(PatternList vars, Rules rs) {
    %match(rs) {
      RList() -> { return `RList(); }
      RList(Rule(PFun(_,pl),rhs),rs1*) -> {
        Rules remain = `unfold(vars,rs1);
        Clause prule = `unfoldAux(pl,vars,rhs);
        return `RList(prule,remain*);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  /* pl   = p1 p2 p3 ...
     vars = x2 x3 x4 ...  */
  private static Clause unfoldAux(PatternList pl, PatternList vars, LTerm rhs) {
    %match(vars,pl) {
      PList(), PList(p_n) -> { 
        return `Rule(p_n,rhs); 
      }
      PList(PVar(x),vs*), PList(p,ps*) -> {
        Clause remain = `unfoldAux(ps,vs,rhs);
        return `Rule(p,Case(Var(x),RList(remain)));
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }


  public static LTerm unfoldCases(LTerm t) {
    try { return `TopDown(Unfold()).visitLight(t); }
    catch (VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  private static LTerm unit = `Constr("Unit",LTList());
  private static LVar freshvar() { return LVar.freshLVar("u"); }

  private static LTerm freeze(LTerm t) {
    LVar fresh = freshvar();
    return `Abs(lam(fresh,t));
  }

  private static LType freeze(LType ty) {
    %match(ty) {
      v@TypeVar[] -> { return `v; }
      t           -> { return `Arrow(Atom("Unit"),t); }
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

  private static Range freeze(Range rg) {
    %match(rg) {
      Range(Ra(vars,dom,codom)) -> { return `Range(Ra(vars,freeze(dom),codom)); }
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
}
