package miniml;

import tom.library.sl.*;
import miniml.lambda.types.*;
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

  static <T extends miniml.lambda.LambdaAbstractType> T 
    applySubst(Substitution s, T t) {
      T res = t;
      %match(s) {
        MList(_*,MapsTo(i,ty),_*) -> {
          res = `substType(i,ty,res);
        }
      }
      return res;
    }

  static <T extends miniml.lambda.LambdaAbstractType> T 
    substType(int i, LType ty, T t) {
      try { return (T) `TopDown(SubstType(i,ty)).visitLight(t); }
      catch(VisitFailure e) { throw new RuntimeException("never happens"); }
  }

  private static ArrayList<Integer> getTypeVars(miniml.lambda.LambdaAbstractType t) {
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

  public static Typed typeOf(LTerm t) {
    counter = maxTypeVar(t)+1;
    // test context
    Context ctx = `Context(RangeOf("Z",Range(Domain(),Atom("Nat"))),
                           RangeOf("S",Range(Domain(Atom("Nat")),Atom("Nat"))),
                           RangeOf("ConsNList",Range(Domain(Atom("Nat"),Atom("NatList")),Atom("NatList"))),
                           RangeOf("EmptyNList",Range(Domain(),Atom("NatList"))),
                           RangeOf("ConsTList",Range(Domain(Atom("Term"),Atom("TermList")),Atom("TermList"))),
                           RangeOf("EmptyTList",Range(Domain(),Atom("TermList"))),
                           RangeOf("ConsTLList",Range(Domain(Atom("TermList"),Atom("TermListList")),Atom("TermListList"))),
                           RangeOf("EmptyTLList",Range(Domain(),Atom("TermListList"))),
                           RangeOf("ConsTLLList",Range(Domain(Atom("TermListList"),Atom("TermListListList")),Atom("TermListListList"))),
                           RangeOf("EmptyTLLList",Range(Domain(),Atom("TermListListList"))),
                           RangeOf("Fun",Range(Domain(Atom("Name"),Atom("TermList")),Atom("Term"))),
                           RangeOf("A",Range(Domain(),Atom("Name"))),
                           RangeOf("B",Range(Domain(),Atom("Name"))),
                           RangeOf("C",Range(Domain(),Atom("Name"))),
                           RangeOf("D",Range(Domain(),Atom("Name"))),
                           RangeOf("E",Range(Domain(),Atom("Name"))),
                           RangeOf("F",Range(Domain(),Atom("Name"))),
                           RangeOf("G",Range(Domain(),Atom("Name"))),
                           RangeOf("NoneTL",Range(Domain(),Atom("TermRes"))),
                           RangeOf("SomeTL",Range(Domain(Atom("TermList")),Atom("TermRes"))),
                           RangeOf("NoneTLL",Range(Domain(),Atom("TermListRes"))),
                           RangeOf("SomeTLL",Range(Domain(Atom("TermListList")),Atom("TermListRes"))),
                           RangeOf("NoneTLLL",Range(Domain(),Atom("TermListListRes"))),
                           RangeOf("SomeTLLL",Range(Domain(Atom("TermListListList")),Atom("TermListListRes"))));


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

  private static LTerm refreshTypeVars(ArrayList<Integer> vars, LTerm t) {
    for(int i: vars) {
      t = substType(i,freshTypeVar(),t);
    }
    return t;
  }

  private static ReconResult recon(Context c, LTerm t) {
    %match(t) {
      Var(x) -> { return `Pair(Typed(TyVar(x),assoc(c,x)),CList()); } 
      Abs(lam(x,ty1,t1)) -> {
        %match(recon(Context(Jugement(x,ty1),c*),t1)) {
          Pair(t1b@Typed(_,ty2),con) -> { 
            return `Pair(Typed(TyAbs(Tylam(x,ty1,t1b)),Arrow(ty1,ty2)),con); 
          }
        }
      }
      App(t1,t2) -> {
        %match(recon(c,t1),recon(c,t2)) {
          Pair(t1b@Typed(_,ty1),con1),Pair(t2b@Typed(_,ty2),con2) -> {
            LType ty3 = freshTypeVar();
            return `Pair(Typed(TyApp(t1b,t2b),ty3),
                CList(Constraint(ty1,Arrow(ty2,ty3)),con1*,con2*));
          }
        }
      }
      Let(letin(x,t1,t2)) -> {
        LTerm t2s = Eval.substitute(`t2,`x,`t1);
        %match(recon(c,t2s)) {
          Pair(t2b,con) -> {
            // compute {typevars t1} \ {typevars gamma}
            ArrayList<Integer> vt1 = getTypeVars(`t1);
            vt1.removeAll(getTypeVars(c));
            %match(recon(c,refreshTypeVars(vt1,t1))) {
              Pair(_,con1) -> { 
                return `Pair(t2b,CList(con*,con1*)); 
              }
            }
          }
        }
      }
      Constr(f,tl) -> {
        %match(assoc(c,f)) {
          Range(dom,codom) -> {
            ReconChildrenResult res = `recon(c,tl,dom);
            TyLTermList children = res.gettl();
            ConstraintList cl = res.getcl();
            return `Pair(Typed(TyConstr(f,children),codom),cl);
          }
        }
      }
      Case(s,rs) -> {
        %match(recon(c,s)) {
          Pair(sb@Typed(_,ty1),con1) -> {
            %match (recon(c,rs,ty1)) {
              Triple(rsb,ty2,con2) -> { 
                return `Pair(Typed(TyCase(sb,rsb),ty2),CList(con1*,con2*)); 
              }
            }
          }
        }
      }
      Fix(fixpoint(x,ty1,t1)) -> {
         %match(recon(Context(Jugement(x,ty1),c*),t1)) {
          Pair(t1b@Typed(_,ty2),con) -> { 
            return `Pair(Typed(TyFix(Tyfixpoint(x,ty1,t1b)),ty1),
                CList(Constraint(ty1,ty2),con*)); 
          }
        }
      }
    }
    throw new RuntimeException("Type reconstruction failed.");
  }

  private static ReconChildrenResult 
    recon(Context c, LTermList tl, Domain dom) {
      return reconRange(c,tl,dom,`TyLTList(),`CList());
  }

  private static ReconChildrenResult 
    reconRange(Context c, LTermList tl, Domain dom,
        TyLTermList res, ConstraintList cl) {
      %match(tl,dom) {
        LTList(), Domain() -> { return `Pair2(res,cl); }
        LTList(t,ts*), Domain(ty,tys*) -> { 
          %match(recon(c,t)) {
            Pair(tb@Typed(_,ty1),cl1) -> {
              return `reconRange(c,ts,tys,TyLTList(res*,tb),CList(Constraint(ty,ty1),cl1*,cl*));    
            }
          } 
        }
      }
      throw new RuntimeException("Type reconstruction failed.");
    }

  private static ReconRulesResult recon(Context c, Rules rl, LType subject) {
    LType fresh = freshTypeVar();
    return reconRules(c,rl,subject,fresh,`TyRList(),`CList());
  }

  private static ReconRulesResult
    reconRules(Context c, Rules rl, LType sub, LType rhs, 
        TyRules rls, ConstraintList cl) {
      %match(rl) {
        RList() -> { return `Triple(rls,rhs,cl); }
        RList(r,rs*) -> {
           %match(reconClause(c,r,sub,rhs)) {
             Pair3(rb,cl1) -> {
               return 
                 `reconRules(c,rs,sub,rhs,TyRList(rls*,rb),CList(cl1*,cl*));
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
                Pair(tb@Typed(_,ty2),cl2) -> {
                  TyClause res = `TyRule(pb,tb);
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
          Range(dom,codom) -> {
            %match(recon(c,pl,dom)) {
              Triple2(ctx,plb,con) -> { 
                return `Quadruple(ctx,TyPFun(f,plb),codom,con); 
              }
            }
          }
        }
      }
      PVar(x,ty) -> {
        TyPattern res = `TyPVar(x,ty); 
        return `Quadruple(Context(Jugement(x,ty)),res,ty,CList());
      }
    }
    throw new RuntimeException("Type reconstruction failed.");
  }

  private static ReconPatternListResult
    recon(Context c, PatternList pl, Domain dom) {
      return reconRange(c,pl,dom,`TyPList(),`Context(),`CList());
    }

  private static ReconPatternListResult 
    reconRange(Context c, PatternList pl, Domain dom, 
      TyPatternList typl, Context ctx, ConstraintList cl) {
      %match(pl,dom) {
        PList(), Domain() -> { return `Triple2(ctx,typl,cl); }
        PList(p,ps*), Domain(ty,tys*) -> { 
          %match(recon(c,p)) {
            Quadruple(ctx1,pb,ty1,cl1) -> {
              return `reconRange(c,ps,tys,
                  TyPList(typl*,pb),
                  Context(ctx1*,ctx*),
                  CList(Constraint(ty,ty1),cl1*,cl*));
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
