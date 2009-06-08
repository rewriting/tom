package lazyml;

import tom.library.sl.*;
import lazyml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Typer {

  %include { lambda/lambda.tom }
  %include { sl.tom }

  public static class IllFormedTerm extends RuntimeException {};
  public static class UnificationError extends RuntimeException {};
  public static class ConstructorNotDeclared extends RuntimeException {
    public ConstructorNotDeclared(String message) { super(message); }
  };

  %strategy SubstType(v:TVar,ty:LType) extends Identity() {
    visit LType { TypeVar(n) && n << TVar v -> { return ty; } }
  }

  static <T extends lazyml.lambda.lambdaAbstractType> T 
    applySubst(Substitution s, T t) {
      T res = t;
      %match(s) { MList(_*,MapsTo(v,ty),_*) -> { res = `substType(v,ty,res); } }
      return res;
    }

  static <T extends lazyml.lambda.lambdaAbstractType> T 
    substType(TVar v, LType ty, T t) {
      try { return (T) `TopDown(SubstType(v,ty)).visitLight(t); }
      catch(VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  private static lazyml.lambda.types.tvarlist.TVarList getFreeTypeVars(LType ty) {
    return nub(getFreeTypeVars(ty,(lazyml.lambda.types.tvarlist.TVarList)`TVarList()));
  }

  private static lazyml.lambda.types.tvarlist.TVarList nub(lazyml.lambda.types.tvarlist.TVarList vs) {
    lazyml.lambda.types.tvarlist.TVarList res = (lazyml.lambda.types.tvarlist.TVarList) `TVarList();
    for(TVar v: vs) {
      if (!res.contains(v)) res = (lazyml.lambda.types.tvarlist.TVarList) `TVarList(res*,v);
    }
    return res;
  }

  private static lazyml.lambda.types.tvarlist.TVarList getFreeTypeVars(
      LType ty,  
      lazyml.lambda.types.tvarlist.TVarList ctx) {
    %match(ty) {
       Atom(_) -> { return (lazyml.lambda.types.tvarlist.TVarList) `TVarList(); }
       Arrow(a,b) -> {
        TVarList f1 = `getFreeTypeVars(a,ctx);
        TVarList f2 = `getFreeTypeVars(b,ctx);
        return (lazyml.lambda.types.tvarlist.TVarList) `TVarList(f1*,f2*);
       }
       TypeVar(x) -> {
         return (lazyml.lambda.types.tvarlist.TVarList) (ctx.contains(`x) ? `TVarList() : `TVarList(x));
       }
       Forall(Fa(x,a)) -> {
         return `getFreeTypeVars(a,(lazyml.lambda.types.tvarlist.TVarList)TVarList(x,ctx*));
       }
       TyConstr(_,tyl) -> {
         return `getFreeTypeVars(tyl);
       }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static lazyml.lambda.types.tvarlist.TVarList getFreeTypeVars(TyList tyl) {
    %match(tyl) {
      TyList() -> { return (lazyml.lambda.types.tvarlist.TVarList) `TVarList(); }
      TyList(ty,tys*) -> { 
        TVarList f1 = `getFreeTypeVars(ty);
        TVarList f2 = `getFreeTypeVars(tys);
        return (lazyml.lambda.types.tvarlist.TVarList) `TVarList(f1*,f2*); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static TVar freshTVar() {
    return TVar.freshTVar("\u03B1");
  }

  private static LType freshTypeVar() {
    return `TypeVar(freshTVar());
  }

  public static LType freeze(LType ty) {
    return `Arrow(Atom("Unit"),ty);
  }

  private static TVar fresh1 = freshTVar();
  private static TVar fresh2 = freshTVar();

  public static TypeOfResult typeOf(Context ctx, LTerm t) {
    %match(recon(ctx,t)) {
      RR(ft,ty,con) -> { 
        Substitution subst = `unify(con);
        LType tty = `applySubst(subst,ty);
        FTerm fft = `applySubst(subst,ft);
        TVarList vs = `getFreeTypeVars(tty);
        return `Pair(abstractTVars(vs,fft), abstractTVars(vs,tty));
      } 
    }
    throw new RuntimeException("non exhaustive patterns");
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
    throw new ConstructorNotDeclared(cons);
  }

  private static instanciateForallsResult instanciateForalls(LType ty) {
    %match (ty) {
      Forall(Fa(x,a)) -> {
        TVar fresh = freshTVar();
        %match(instanciateForalls(substType(x,TypeVar(fresh),a))) {
          Pair4(vs,res) -> { return `Pair4(TVarList(fresh,vs*),res); }
        }
      }
      a -> { return `Pair4(TVarList(),a); }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static FTerm applyToTVars(FTerm t, TVarList vs) {
    %match(vs) {
      TVarList(v,vs1*) -> { return `applyToTVars(FTApp(t,TypeVar(v)),vs1); }
      TVarList() -> { return t; }
    }
    throw new RuntimeException("non exhaustive patterns");
  }
  
  private static FTerm abstractTVars(TVarList vs, FTerm t) {
    %match(vs) {
      TVarList(v,vs1*) -> { return `FTAbs(FTLam(v,abstractTVars(vs1,t))); }
      TVarList()      -> { return t; }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static LType abstractTVars(TVarList vs, LType ty) {
    %match(vs) {
      TVarList(v,vs1*) -> { return `Forall(Fa(v,abstractTVars(vs1,ty))); }
      TVarList()      -> { return ty; }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static LType tyInt = `Atom("Int");
  private static LType tyStr = `Atom("String");

  private static ReconResult recon(Context c, LTerm t) {
    %match(t) {
      Var(x) -> { 
        LType ty = `assoc(c,x);
        %match(instanciateForalls(ty)) {
          Pair4(vs,ty1) -> {
            FTerm res = `applyToTVars(FVar(x),vs);
            return `RR(res,ty1,CList());
          }
        }
      } 
      Abs(lam(x,t1)) -> {
        LType ty1 = freshTypeVar();
        %match(recon(Context(Jugement(x,ty1),c*),t1)) {
          RR(t1b,ty2,con) -> { 
            return `RR(FAbs(FLam(x,ty1,t1b)),Arrow(ty1,ty2),con); 
          }
        }
      }
      App(t1,t2) -> {
        %match(recon(c,t1),recon(c,t2)) {
          RR(t1b,ty1,con1),RR(t2b,ty2,con2) -> {
            LType ty3 = freshTypeVar();
            return `RR(FApp(t1b,t2b),ty3,
                CList(Constraint(ty1,Arrow(ty2,ty3)),con1*,con2*));
          }
        }
      }
      Let(letin(x,t1,t2)) -> {
        %match(recon(c,t1)) {
          RR(ft1,a,con1) -> {
            Substitution subst = `unify(con1);
            LType aa = `applySubst(subst,a);
            TVarList alphas = `getFreeTypeVars(aa);
            aa = `abstractTVars(alphas,aa);
            %match(recon(Context(Jugement(x,aa),c*),t2)) {
              RR(ft2,b,con2) -> {
                FTerm fft1 = `applySubst(subst,ft1);
                fft1 = `abstractTVars(alphas,fft1);
                return `RR(FLet(FLetin(x,aa,fft1,ft2)),b,CList(con1*,con2*));
              }
            }
          }
        }
      }
      Constr(f,tl) -> {
        %match(assoc(c,f)) {
          Range(Ra(_,dom,codom)) -> {
            ReconChildrenResult res = `recon(c,tl,dom);
            FTermList children = res.gettl();
            ConstraintList cl = res.getcl();
            return `RR(FConstr(f,children),codom,cl);
          }
        }
      }
      PrimFun(f,tl) -> {
        %match(assoc(c,f)) {
          Range(Ra(_,dom,codom)) -> {
            ReconChildrenResult res = `recon(c,tl,dom);
            FTermList children = res.gettl();
            ConstraintList cl = res.getcl();
            return `RR(FPrimFun(f,children),codom,cl);
          }
        }
      }
      Lit(i) -> {
        return `RR(FLit(i),tyInt,CList());
      }
      Str(s) -> {
        return `RR(FStr(s),tyStr,CList());
      }
      Case(s,rs) -> {
        %match(recon(c,s)) {
          RR(sb,ty1,con1) -> {
            %match (recon(c,rs,ty1)) {
              Triple(rsb,ty2,con2) -> { 
                return `RR(FCase(sb,rsb),ty2,CList(con1*,con2*)); 
              }
            }
          }
        }
      }
      Fix(fixpoint(x,t1)) -> {
        LType ty1 = freshTypeVar();
        %match(recon(Context(Jugement(x,ty1),c*),t1)) {
          RR(t1b,ty2,con) -> { 
            return `RR(FFix(FFixpoint(x,ty1,t1b)),ty1,
                CList(Constraint(ty1,ty2),con*)); 
          }
        }
      }
    }
    throw new RuntimeException("Type reconstruction failed.");
  }

  private static ReconChildrenResult 
    recon(Context c, LTermList tl, Domain dom) {
      return reconRange(c,tl,dom,`FTermList(),`CList());
    }

  private static ReconChildrenResult 
    reconRange(Context c, LTermList tl, Domain dom,
        FTermList res, ConstraintList cl) {
      %match(tl,dom) {
        LTList(), Domain() -> { return `Pair2(res,cl); }
        LTList(t,ts*), Domain(ty,tys*) -> { 
          %match(recon(c,t)) {
            RR(tb,ty1,cl1) -> {
              return `reconRange(c,ts,tys,FTermList(res*,tb),CList(Constraint(ty,ty1),cl1*,cl*));    
            }
          } 
        }
      }
      throw new RuntimeException("Type reconstruction failed.");
    }

  private static ReconRulesResult recon(Context c, Rules rl, LType subject) {
    LType fresh = freshTypeVar();
    return reconRules(c,rl,subject,fresh,`FRList(),`CList());
  }

  private static ReconRulesResult
    reconRules(Context c, Rules rl, LType sub, LType rhs, 
        FRules rls, ConstraintList cl) {
      %match(rl) {
        RList() -> { return `Triple(rls,rhs,cl); }
        RList(r,rs*) -> {
          %match(reconClause(c,r,sub,rhs)) {
            Pair3(rb,cl1) -> {
              return 
                `reconRules(c,rs,sub,rhs,FRList(rls*,rb),CList(cl1*,cl*));
            }
          }
        }
      }
      throw new RuntimeException("Type reconstruction failed.");
    }

  private static ReconClauseResult 
    reconClause(Context c, Clause r, LType sub, LType rhs) {
      %match(r) {
        Rule(p,t) -> {
          %match(recon(c,p)) {
            Quadruple(ctx,pb,ty1,cl1) -> {
              %match(recon(Context(ctx*,c*),t)) {
                RR(tb,ty2,cl2) -> {
                  FClause res = `FRule(pb,tb);
                  ConstraintList resl = `CList(
                      Constraint(sub,ty1),Constraint(rhs,ty2),cl1*,cl2*);
                  return `Pair3(res,resl);
                }
              }
            }
          }
        }
      }
      throw new RuntimeException("Type reconstruction failed.");
    }

  private static ReconPatternResult recon(Context c, Pattern p) {
    %match(p) {
      PFun(f,pl) -> {
        %match(assoc(c,f)) {
          Range(Ra(_,dom,codom)) -> {
            %match(recon(c,pl,dom)) {
              Triple2(ctx,plb,con) -> { 
                return `Quadruple(ctx,FPFun(f,plb),codom,con); 
              }
            }
          }
        }
      }
      PVar(x) -> {
        LType ty = freshTypeVar();
        FPattern res = `FPVar(x,ty); 
        return `Quadruple(Context(Jugement(x,ty)),res,ty,CList());
      }
    }
    throw new RuntimeException("Type reconstruction failed.");
  }

  private static ReconPatternListResult
    recon(Context c, PatternList pl, Domain dom) {
      return reconRange(c,pl,dom,`FPList(),`Context(),`CList());
    }

  private static ReconPatternListResult 
    reconRange(Context c, PatternList pl, Domain dom, 
        FPatternList typl, Context ctx, ConstraintList cl) {
      %match(pl,dom) {
        PList(), Domain() -> { return `Triple2(ctx,typl,cl); }
        PList(p,ps*), Domain(ty,tys*) -> { 
          %match(recon(c,p)) {
            Quadruple(ctx1,pb,ty1,cl1) -> {
              return `reconRange(c,ps,tys,
                  FPList(typl*,pb),
                  Context(ctx1*,ctx*),
                  CList(Constraint(ty,ty1),cl1*,cl*));
            }
          } 
        }
      }
      throw new RuntimeException("Type reconstruction failed.");
    }

  private static ConstraintList getConstraints(TyList l1, TyList l2) {
    %match(l1,l2) {
      TyList(), TyList() -> { return `CList(); }
      TyList(ty1,tys1*), TyList(ty2,tys2*) -> {
        ConstraintList cs = `getConstraints(tys1,tys2);
        return `CList(Constraint(ty1,ty2),cs*); 
      }
    }
    throw new UnificationError();
  }

  private static Substitution unify(ConstraintList cl) {
    %match(cl) {
      CList() -> { return `MList(); }
      CList(c,cs*) -> {
        %match(c) {
          Constraint(ty,ty) -> { return `unify(cs); }
          Constraint(TypeVar(x),ty) -> { 
            if (!getFreeTypeVars(`ty).contains(`x)) {
              Substitution rec = `unify(substType(x,ty,cs));
              return `MList(MapsTo(x,ty),rec*);
            }
          } 
          Constraint(ty,TypeVar(x)) -> { 
            if (!getFreeTypeVars(`ty).contains(`x)) {
              Substitution rec = `unify(substType(x,ty,cs));
              return `MList(MapsTo(x,ty),rec*);
            }
          }
          Constraint(Arrow(s1,s2),Arrow(t1,t2)) -> {
            return `unify(CList(Constraint(s1,t1),Constraint(s2,t2),cs*));
          }
          Constraint(TyConstr(C,tyl1),TyConstr(C,tyl2)) -> {
            ConstraintList cs1 = `getConstraints(tyl1,tyl2);
            return `unify(CList(cs1*,cs*));
          }
        }
      }
    }
    throw new UnificationError();
  }
} 
