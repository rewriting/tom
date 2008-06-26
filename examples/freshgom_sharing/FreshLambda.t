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
       | Abs(<lam>)
       | Let(<letin>)
       | Var(x:LVar)

     lam binds LVar = lam(x:LVar,inner t:LTerm)
     letin binds LVar = letin(x:LVar, outer t:LTerm, inner t:Lterm)

  */

  // returns t[u/x]
  public static LTerm substitute(LTerm t, LVar x, LTerm u) {
    %match(t) {
      Abs(lam(y,t1)) -> { return `Abs(lam(y,substitute(t1,x,u))); }
      Let(letin(y,t1,t2)) -> { 
        return `Let(letin(y,substitute(t1,x,u),substitute(t2,x,u))); 
      }
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
      RLTerm rt =  parser.lterm();
      System.out.println("\nafter parsing: " + Printer.pretty(rt));
      LTerm t = beta(rt.convert());
      System.out.println("\nafter evaluation: " + Printer.pretty(t.export()));

    } catch(Exception e) {
      System.out.println(e);
    }
  }
}
