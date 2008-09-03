import tom.library.sl.*;
import lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Typer {

  %include { lambda/Lambda.tom }
  %include { sl.tom }

  public static class IllFormedTerm extends RuntimeException {};
  public static class UnificationError extends RuntimeException {};

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
    %match(recon(Context(),t)) {
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
    }
    return null;
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
