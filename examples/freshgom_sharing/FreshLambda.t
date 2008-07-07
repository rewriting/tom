package freshgom_sharing;

import tom.library.sl.*;
import freshgom_sharing.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class FreshLambda {

  %include { sl.tom }
  %include { tweaked_lambda.tom }

  /* how the gom module would look like *

     module lambda
     imports int String
     abstract syntax
     
     atom LVar 
     
     LTerm =
       | App(t1:LTerm,t2:LTerm)
       | Abs(a:lam)
       | Let(b:<Letin>)
       | Var(x:<LVar>)
       | Constr(f:String, children:LTermList)
       | Case(subject:LTerm,rules:Rules)

     Rules = RList(<Clause>*)
     Clause binds LVar = Rule(p:Pattern, inner t:LTerm) 

     Pattern binds LVar =
       | PFun(f:String, children:PatternList)
       | PVar(x:LVar)

     LTermList binds LVar = LTList(LTerm*)
     PatternList binds LVar = PList(Pattern*)

     Lam binds LVar = lam(x:LVar,inner t:LTerm)
     Letin binds LVar = letin(x:LVar, outer t:LTerm, inner t:Lterm)

  */


  public static HashMap<LVar,LTerm> 
    match(HashMap<LVar,LTerm> m, Pattern p, LTerm t) {
      %match(p,t) {
        PVar(x), u -> { 
          if (m.containsKey(`x)) return m.get(`x).equals(`u) ? m : null; 
          else { m.put(`x,`u); return m; }
        }
        PFun(f,l1), Constr(f,l2) -> { return `match(m,l1,l2); }
      }
      return null;
    }

  public static HashMap<LVar,LTerm>
    match(HashMap<LVar,LTerm> m, PatternList l1, LTermList l2) {
      %match(l1,l2) {
        ConsPList(p,ps),ConsLTList(x,xs) -> {
          return `match(m,p,x) != null ? `match(m,ps,xs) : null;
        }
        EmptyPList(),EmptyLTList() -> { return m; }
      }
      return null;
    }

  // returns t[u/x]
  public static LTerm substitute(LTerm t, LVar x, LTerm u) {
    %match(t) {
      App(t1,t2) -> { return `App(substitute(t1,x,u),substitute(t2,x,u)); }
      Abs(lam(v,t1)) -> { return `Abs(lam(v,substitute(t1,x,u))); }
      Let(letin(v,t1,t2)) -> {
        return `Let(letin(v,substitute(t1,x,u),substitute(t2,x,u)));
      }
      Var(y) -> { if (`y.equals(x)) return u; else return t; }
      Constr(f, c) -> { return `Constr(f,substitute(c,x,u)); }
      Case(s,r) -> { return `Case(substitute(s,x,u),substitute(r,x,u)); }
    }
    throw new RuntimeException();
  }

  public static Rules substitute(Rules r, LVar x, LTerm u) {
    %match(r) {
      RList() -> { return r; }
      RList(t,ts*) -> {
        return `ConsRList(substitute(t,x,u),substitute(ts*,x,u));
      }
    }
    throw new RuntimeException();
  }

  public static Clause substitute(Clause c, LVar x, LTerm u) {
    %match(c) {
      Rule(p,t) -> { return `Rule(p,substitute(t,x,u)); }
    }
    throw new RuntimeException();
  }

  public static LTermList substitute(LTermList l, LVar x, LTerm u) {
    %match(l) {
      LTList() -> { return l; }
      LTList(t,ts*) -> {
        return `ConsLTList(substitute(t,x,u),substitute(ts*,x,u));
      }
    }
    throw new RuntimeException();
  }

  public static LTerm substitute(LTerm t, HashMap<LVar,LTerm> m) {
    LTerm res = t;
    for(Map.Entry<LVar,LTerm> e: m.entrySet()) {
      res = substitute(res,e.getKey(),e.getValue());
    }
    return res;
  }

  public static LTerm caseof(LTerm t, Rules r) {
    %match(t,r) {
      Constr[],RList(_*,Rule(lhs,rhs),_*) -> {
        HashMap<LVar,LTerm> m = match(new HashMap(),`lhs,t);
        if (m!=null) return `substitute(rhs,m);
      }
    }
    return null;
  }

  %strategy HeadBeta() extends Fail() {
    visit LTerm {
      App(Abs(lam(x,t)),u) -> { return `substitute(t,x,u); }
      Let(letin(x,u,t)) -> { return `substitute(t,x,u); }
      Case(t,r) -> { 
        LTerm res = `caseof(t,r);  
        if (res != null) return res;
        throw new VisitFailure();
      }
    }
  }

  %strategy Debug() extends Identity() {
    visit LTerm {
      x -> { 
        System.out.println(Printer.pretty(`x) + "\n") ;
        System.out.println(Printer.pretty(`x.export()) + "\n"); }
    }
  }

  public static LTerm beta(LTerm t) {
    try { return (LTerm) `Outermost(HeadBeta()).visit(t); }
    //try { return (LTerm) `Repeat(Sequence(OnceTopDown(HeadBeta()),Debug())).visit(t); }
    catch (VisitFailure e) { return t; }
  }

  public static void main(String[] args) {
    try{
      LambdaLexer lexer = new LambdaLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      LambdaParser parser = new LambdaParser(tokens);
      LTerm t = null, tmp = null;
      for(RLTerm rt:parser.toplevel()) {
        System.out.println("\nparsed: " + Printer.pretty(rt));
        tmp = t;
        System.out.println("converted : " + Printer.pretty(rt.convert().export()));
        t = beta(rt.convert());
        System.out.println(Printer.pretty(t.export()));
        System.out.println("\nequals previous modulo alpha: " + t.equals(tmp) + ";\n");
      }
    } catch(Exception e) {
      e.printStackTrace();
      //System.out.println(e);
    }
  }
}
