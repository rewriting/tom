package termgraph;

import tom.library.sl.*;
import termgraph.freshlambda.types.*;
import org.antlr.runtime.*;
import java.util.*;


public class FreshLambda {

  %include { sl.tom }
  %include { freshlambda/freshlambda.tom }

  public static LTerm beta(LTerm t) {
    try { 
      return `Outermost(LTerm.HeadBeta()).visit(t); 
    }
    catch (VisitFailure e) { 
      return t; 
    }
  }


  public static void main(String[] args) {
    try{
      LambdaLexer lexer = new LambdaLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      LambdaParser parser = new LambdaParser(tokens);
      LTerm t = null, tmp = null;
      for(LTerm rt:parser.toplevel()) {
        System.out.println("\nparsed: " + pretty(rt));
        t = beta(rt);
        System.out.println("\nnormal form : " + pretty(t));
        tom.library.utils.Viewer.display(`t);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static String pretty(LTerm t) {
    %match (t) {
      Var(Name(x)) -> { return `x ; }
      Plus(u,v) -> { return %[(@`pretty(u)@ + @`pretty(v)@)]%; }
      Let(Name(x),u,v) -> { 
        return %[(let @`x@ = @`pretty(u)@ in @`pretty(v)@)]%; }
      Num(Val(i)) -> { return ""+`i; }
      SubstLTerm(u,Name(x),v) -> { return "("+`pretty(u)+"["+`x+":="+`pretty(v)+"])"; }
      SubstLTerm(u,x,v) -> { return "("+`pretty(u)+"["+`x+":="+`pretty(v)+"])"; } // in case of paths
    }
    return t.toString();
  }

}
