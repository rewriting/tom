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

  %strategy Substitute(x:LVar, u:LTerm) extends Identity() {
    visit LTerm { Var(y) -> { if (`y.equals(x)) return u; } }
  }

  %typeterm Map { implement { HashMap<LVar,LTerm> } }
  %strategy ApplySubstitution(m:Map) extends Identity() {
    visit LTerm { Var(x) -> { if (m.containsKey(`x)) return m.get(`x); } }
  }

  // returns t[u/x]
  public static LTerm substitute(LTerm t, LVar x, LTerm u) {
    try { return (LTerm) `TopDown(Substitute(x,u)).visit(t); } 
    catch (VisitFailure e) { throw new RuntimeException(); }
  }

  public static LTerm substitute(LTerm t, HashMap<LVar,LTerm> m) {
    try { return (LTerm) `TopDown(ApplySubstitution(m)).visit(t); } 
    catch (VisitFailure e) { throw new RuntimeException(); }
  }

  public static LTerm caseof(LTerm t, Rules r) {
    %match(t,r) {
      Constr[],RList(_*,Rule(lhs,rhs),_*) -> {
        HashMap<LVar,LTerm> m = match(new HashMap(),`lhs,t);
        if (m!=null) return `substitute(rhs,m);
      }
    }
    return `Case(t,r);
  }

  %strategy HeadBeta() extends Fail() {
    visit LTerm {
      App(Abs(lam(x,t)),u) -> { return `substitute(t,x,u); }
      Let(letin(x,u,t)) -> { return `substitute(t,x,u); }
      Case(t,r) -> { return `caseof(t,r); }
    }
  }


  %strategy Debug() extends Identity() {
    visit LTerm {
      x -> { System.out.println(Printer.pretty(`x.export()) + "\n"); }
    }
  }

  public static LTerm beta(LTerm t) {
    //try { return (LTerm) `Outermost(HeadBeta()).visit(t); }
    try { return (LTerm) `Repeat(OnceTopDown(Sequence(Debug(),HeadBeta()))).visit(t); }
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
