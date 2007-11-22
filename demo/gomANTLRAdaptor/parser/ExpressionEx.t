package parser;

import java.io.DataInputStream;
import parser.expression.types.*;
import tom.library.sl.*;
import java.util.HashMap;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import parser.expression.*;
import java.io.*;

public class ExpressionEx {

  %include { expression/Expression.tom }
  %include { sl.tom }
  %include { java/util/HashMap.tom }
  
  %strategy Evaluate(vars:HashMap) extends Identity() {
    visit Statement{
      Equal(Id(n),r) -> { vars.put(`n,`r); }
    }
    visit Expr {
      Id(n) -> { if (vars.containsKey(`(n))) return (Expr) vars.get(`(n)); }
      Plus(Num(n1),Num(n2)) -> { int i = `(n1)+`(n2); return `Num(i); }
      Minus(Num(n1),Num(n2)) -> { int i = `n1-`n2; return `Num(i); }
      Mult(Num(n1),Num(n2)) -> { int i = `n1 * `n2; return `Num(i); }
      Div(Num(n1),Num(n2)) -> { int i = `n1/`n2; return `Num(i); }
    }
  }


  public static void main(String[] args) throws VisitFailure{    
    String exprCode = "a = 3; b=a+2-3;\n";
    try{
      // Initialize parser
      ExpressionLexer lexer = new ExpressionLexer(new ANTLRInputStream(new ByteArrayInputStream(exprCode.getBytes("utf-8"))));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      ExpressionParser parser = new ExpressionParser(tokens);
      parser.setTreeAdaptor(new ExpressionAdaptor());
      // Parse the input expression
      ExpressionTree b = (ExpressionTree) parser.ruleBase().getTree();
      
      // apply evaluate strategy from leafs to root
      HashMap vars = new HashMap();
      RuleBase rb = (RuleBase)b.getTerm();
      System.out.println("Result = " + rb);
      rb = (RuleBase) `BottomUp(Evaluate(vars)).visitLight(rb);

      // display results
      %match(RuleBase rb) {
        (_*,Equal(Id(i),Num(n)),_*) -> {
          System.out.println(`i + " = " + `n);
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
