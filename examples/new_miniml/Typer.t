import tom.library.sl.*;
import lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Typer {

  %include { lambda/Lambda.tom }
  %include { sl.tom }

  public static class IllFormedTerm extends RuntimeException {};
  public static class UnificationError extends RuntimeException {};
  public static class ConstructorNotDeclared extends RuntimeException {};

  %typeterm IntList { implement { ArrayList<Integer> } }

  %strategy CollectTypeVars(c:IntList) extends Identity() {
    visit LType {
      TypeVar(i) -> { c.add(`i); }
    }
  }

  %strategy SubstType(i:int,ty:LType) extends Identity() {
    visit LType {
      TypeVar(n) && n << int i -> { return ty; }
    }
  }

  static <T extends lambda.LambdaAbstractType> T 
    applySubst(Substitution s, T t) {
      T res = t;
      %match(s) {
        MList(_*,MapsTo(i,ty),_*) -> {
          res = `substType(i,ty,res);
        }
      }
      return res;
    }

  static <T extends lambda.LambdaAbstractType> T 
    substType(int i, LType ty, T t) {
      try { return (T) `TopDown(SubstType(i,ty)).visitLight(t); }
      catch(VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  private static ArrayList<Integer> getTypeVars(lambda.LambdaAbstractType t) {
    ArrayList<Integer> l = new ArrayList<Integer>();
    try { `TopDown(CollectTypeVars(l)).visitLight(t); }
    catch(VisitFailure e) { throw new RuntimeException("never happens"); }
    return l;
  }

  private static int maxTypeVar(LTerm t) {
    return Collections.max(getTypeVars(t));
  }

  private static int counter = 0; 
  private static LType freshTypeVar() {
    return `TypeVar(counter++);
  }

  public static LType typeOf(LTerm t) {
    counter = maxTypeVar(t)+1;
    // test context
    Context ctx = `Context(RangeOf("Z",Range(Domain(),Atom("nat"))),
                           RangeOf("S",Range(Domain(Atom("nat")),Atom("nat"))));
    %match(recon(ctx,t)) {
      Pair(ty,con) -> { 
        Substitution subst = `unify(con);
        return `applySubst(subst,ty);
      } 
    }
    return null;
  }

  private static LType assoc(Context c, LVar x) {
    %match(c) {
      Context(_*,Jugement(v,ty),_*) && v << LVar x -> { return `ty; }
    }
    throw new IllFormedTerm();
  }

  private static Range assoc(Context c, String cons) {
    %match(c) {
      Context(_*,RangeOf(v,r),_*) && v << String cons -> { return `r; }
    }
    throw new ConstructorNotDeclared();
  }


  private static ReconResult recon(Context c, LTerm t) {
    %match(t) {
      Var(x) -> { return `Pair(assoc(c,x),CList()); } 
      Abs(lam(x,ty1,t1)) -> {
        %match(recon(Context(Jugement(x,ty1),c*),t1)) {
          Pair(ty2,con) -> { return `Pair(Arrow(ty1,ty2),con); }
        }
      }
      App(t1,t2) -> {
        %match(recon(c,t1),recon(c,t2)) {
          Pair(ty1,con1),Pair(ty2,con2) -> {
            LType ty3 = freshTypeVar();
            return `Pair(ty3,CList(Constraint(ty1,Arrow(ty2,ty3)),con1*,con2*));
          }
        }
      }
      Let(letin(x,t1,t2)) -> {
        LTerm t2s = Eval.substitute(`t2,`x,`t1);
        %match(recon(c,t2s)) {
          Pair(ty2,con) -> {
            `recon(c,t1); // to avoid ill-typed terms
            return `Pair(ty2,con);
          }
        }
      }
      Fix(fixpoint(x,ty1,t1)) -> {
         %match(recon(Context(Jugement(x,ty1),c*),t1)) {
          Pair(ty2,con) -> { return `Pair(ty1,CList(Constraint(ty1,ty2),con*)); }
        }
      }
      Constr(f,tl) -> {
        %match(assoc(c,f)) {
          Range(dom,codom) -> {
            ConstraintList con = `recon(c,tl,dom);
            return `Pair(codom,con);
          }
        }
      }
      Case(s,rs) -> {
        %match(recon(c,s)) {
          Pair(ty1,con1) -> {
            %match (recon(c,rs,ty1)) {
              Pair(ty2,con2) -> { return `Pair(ty2,CList(con1*,con2*)); }
            }
          }
        }
      }
    }
    throw new RuntimeException("Type reconstruction failed.");
  }

  private static ReconResult recon(Context c, Rules rl, LType subject) {
    LType fresh = freshTypeVar();
    ConstraintList cl = reconRules(c,rl,subject,fresh,`CList());
    return `Pair(fresh,cl);
  }

  private static ConstraintList
    reconRules(Context c, Rules rl, LType sub, LType rhs, ConstraintList cl) {
      %match(rl) {
        RList() -> { return cl; }
        RList(r,rs*) -> {
          ConstraintList cl1 = `reconClause(c,r,sub,rhs);
          return `reconRules(c,rs,sub,rhs,CList(cl1*,cl*));
        }
      }
      throw new RuntimeException("Type reconstruction failed.");
    }

  private static ConstraintList 
    reconClause(Context c, Clause r, LType sub, LType rhs) {
      %match(r) {
        Rule(p,t) -> {
          %match(recon(c,p)) {
            CRPair(ctx,Pair(ty1,cl1)) -> {
              %match(recon(Context(ctx*,c*),t)) {
                Pair(ty2,cl2) -> {
                  return `CList(Constraint(sub,ty1),Constraint(rhs,ty2),cl1*,cl2*);
                }
              }
            }
          }
        }
      }
      throw new RuntimeException("Type reconstruction failed.");
    }

  private static ContextAndResult recon(Context c, Pattern p) {
    %match(p) {
      PFun(f,pl) -> {
        %match(assoc(c,f)) {
          Range(dom,codom) -> {
            %match(recon(c,pl,dom)) {
              CCPair(ctx,con) -> { return `CRPair(ctx,Pair(codom,con)); }
            }
          }
        }
      }
      PVar(x,ty) -> {
        return `CRPair(Context(Jugement(x,ty)),Pair(ty,CList()));
      }
    }
    throw new RuntimeException("Type reconstruction failed.");
  }

  private static ContextAndConstraints
    recon(Context c, PatternList pl, Domain dom) {
      return reconRange(c,pl,dom,`Context(),`CList());
    }

  private static ContextAndConstraints 
    reconRange(Context c, PatternList pl, Domain dom, Context ctx, ConstraintList cl) {
      %match(pl,dom) {
        PList(), Domain() -> { return `CCPair(ctx,cl); }
        PList(p,ps*), Domain(ty,tys*) -> { 
          %match(recon(c,p)) {
            CRPair(ctx1,Pair(ty1,cl1)) -> {
              return `reconRange(c,ps,tys,Context(ctx1*,ctx*),
                  CList(Constraint(ty,ty1),cl1*,cl*));
            }
          } 
        }
      }
      throw new RuntimeException("Type reconstruction failed.");
    }

  private static ConstraintList recon(Context c, LTermList tl, Domain dom) {
    return reconRange(c,tl,dom,`CList());
  }

  private static ConstraintList 
    reconRange(Context c, LTermList tl, Domain dom, ConstraintList cl) {
      %match(tl,dom) {
        LTList(), Domain() -> { return cl; }
        LTList(t,ts*), Domain(ty,tys*) -> { 
          %match(recon(c,t)) {
            Pair(ty1,cl1) -> {
              return `reconRange(c,ts,tys,CList(Constraint(ty,ty1),cl1*,cl*));    
            }
          } 
        }
      }
      throw new RuntimeException("Type reconstruction failed.");
    }

  private static Substitution unify(ConstraintList cl) {
    %match(cl) {
      CList() -> { return `MList(); }
      CList(c,cs*) -> {
        %match(c) {
          Constraint(ty,ty) -> { return `unify(cs); }
          Constraint(TypeVar(x),ty) -> { 
            if (!getTypeVars(`ty).contains(`x)) {
              Substitution rec = `unify(substType(x,ty,cs));
              return `MList(MapsTo(x,ty),rec*);
            }
          } 
          Constraint(ty,TypeVar(x)) -> { 
            if (!getTypeVars(`ty).contains(`x)) {
              Substitution rec = `unify(substType(x,ty,cs));
              return `MList(MapsTo(x,ty),rec*);
            }
          }
          Constraint(Arrow(s1,s2),Arrow(t1,t2)) -> {
            return `unify(CList(Constraint(s1,t1),Constraint(s2,t2),cs*));
          }
        }
      }
    }
    throw new UnificationError();
  }
} 
