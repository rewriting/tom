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

  // returns t[u/x]
  public static LTerm substitute(LTerm t, LVar x, LTerm u) {
    %match(t) {
      Abs(lam(y,t1)) -> { return `Abs(lam(y,substitute(t1,x,u))); }
      Let(letin(y,t1,t2)) -> { return `Let(letin(y,substitute(t1,x,u),substitute(t2,x,u))); }
      App(t1,t2) -> { return `App(substitute(t1,x,u),substitute(t2,x,u)); }
      Var(y) -> { if (`y.equals(x)) return u; else return t; }
    }
    throw new RuntimeException();
  }

  %strategy HeadBeta() extends Fail() {
    visit LTerm {
      App(Abs(lam(x,t)),u) -> { return `substitute(t,x,u); }
      Let(letin(x,u,t)) -> { return `substitute(t,x,u); }
    }
  }

  public static LTerm beta(LTerm t) {
    try { return (LTerm) `Outermost(HeadBeta()).visit(t); }
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
        t = beta(rt.convert());
        System.out.println(Printer.pretty(t.export()));
        System.out.println("\nequals previous modulo alpha: " + t.equals(tmp) + ";\n");
      }
    } catch(Exception e) {
      System.out.println(e);
    }
  }
}
