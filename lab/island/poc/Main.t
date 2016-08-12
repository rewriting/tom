import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import main.parsetree.types.*;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {

  %gom {
    module ParseTree
    imports int String
    abstract syntax

    PtCall = PT_call(methodName:PtMethodName, args:PtArglist)
    PtMethodName = PT_methodName_ID(stringValue:String)
    PtArglist = PT_concPtArg(PtArg*)
    PtArg = PT_arg(expr:PtExpr)
    PtExpr = PT_expr(add_expr:PtAddExpr)
    PtAddExpr = PT_add_expr(lhs:PtPrimaryExpr, rhs:PtAddOpPrimaryExprList)
    PtAddOpPrimaryExprList = PT_concPtAddOpPrimaryExpr(PtAddOpPrimaryExpr*)
    PtAddOpPrimaryExpr = PT_add_op_primary_expr(e1:PtAddOp, e2:PtPrimaryExpr)
    PtAddOp = PT_Plus() 
            | PT_Minus()
    PtPrimaryExpr = PT_primary_expr_STRING(stringValue:String)
                  | PT_primary_expr_ID(idValue:String)
                  | PT_primary_expr_INT(intValue:int)

  }

  public static class Translator extends SimplangBaseListener {
    ParseTreeProperty<Object> values = new ParseTreeProperty<Object>();

    public void exitStart(SimplangParser.StartContext ctx) {}
    public void exitStatements(SimplangParser.StatementsContext ctx) {}
    public void exitBlockStatement(SimplangParser.BlockStatementContext ctx) {}
    public void exitCallStatement(SimplangParser.CallStatementContext ctx) {}
    public void exitDeclStatement(SimplangParser.DeclStatementContext ctx) {}
    public void exitBlock(SimplangParser.BlockContext ctx) {}

    public void exitCall(SimplangParser.CallContext ctx) {
      String name = (String) values.get(ctx.methodName());
      if(ctx.args != null) {

      } else {

      }

      System.out.println("exitCall: " + name);
    }
    
    public void exitMethodName(SimplangParser.MethodNameContext ctx) {
      System.out.println("exitMethodName");
      values.put(ctx, ctx.ID().getText());
    }

    public void exitArglist(SimplangParser.ArglistContext ctx) {}
    public void exitArg(SimplangParser.ArgContext ctx) {}
    public void exitDecl(SimplangParser.DeclContext ctx) {}
    public void exitVariableName(SimplangParser.VariableNameContext ctx) {}
    public void exitExpr(SimplangParser.ExprContext ctx) {
      PtAddExpr t = (PtAddExpr) values.get(ctx.add_expr());
      values.put(ctx, `PT_expr(t));
    }

    public void exitAdd_expr(SimplangParser.Add_exprContext ctx) {
      PtPrimaryExpr lhs = (PtPrimaryExpr) values.get(ctx.lhs);
      PtAddOpPrimaryExprList list = `PT_concPtAddOpPrimaryExpr();
      values.put(ctx, `PT_add_expr(lhs, list));
    }

    public void exitAdd_op(SimplangParser.Add_opContext ctx) {}

    public void exitPrimary_expr(SimplangParser.Primary_exprContext ctx) {
      if(ctx.string != null) {
        values.put(ctx, `PT_primary_expr_STRING(ctx.string.getText()));
      } else if (ctx.id != null) {
        values.put(ctx, `PT_primary_expr_ID(ctx.id.getText()));
      } else {
        values.put(ctx, `PT_primary_expr_INT(Integer.parseInt(ctx.integer.getText())));
      }
    }

  }


  public static class DemoListener extends SimplangBaseListener {
    public void exitArg(SimplangParser.ArgContext ctx) {
      System.out.print(", ");
    }

    public void exitCall(SimplangParser.CallContext call) {
      System.out.print("})");
    }

    public void exitMethodName(SimplangParser.MethodNameContext ctx) {
      System.out.printf("call(\"%s\", new Object[]{", ctx.ID()
          .getText());
    }

    public void exitCallStatement(SimplangParser.CallStatementContext ctx) {
      System.out.println(";");
    }

    public void enterDecl(SimplangParser.DeclContext ctx) {
      System.out.print("define(");
    }

    public void exitVariableName(SimplangParser.VariableNameContext ctx) {
      System.out.printf("\"%s\", ", ctx.ID().getText());
    }

    public void exitDeclStatement(SimplangParser.DeclStatementContext ctx) {
      System.out.println(");");
    }

    public void exitAdd_op(SimplangParser.Add_opContext ctx) {
      if (ctx.MINUS() != null) {
        System.out.print(" - ");
      } else {
        System.out.print(" + ");
      }
    }

    public void exitPrimary_expr(SimplangParser.Primary_exprContext ctx) {
      if (ctx.string != null) {
        String value = ctx.string.getText();
        System.out.printf("\"%s\"", value.subSequence(1, value.length() - 1));
      } else if (ctx.id != null) {
        System.out.printf("read(\"%s\")", ctx.id.getText());
      } else {
        System.out.print(ctx.INT().getText());
      }
    }

  }

    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if( args.length>0 ) {
          inputFile = args[0];
        }
        InputStream is = System.in;
        if( inputFile!=null ) {
            is = new FileInputStream(inputFile);
        }
        ANTLRInputStream input = new ANTLRInputStream(is);
        SimplangLexer lexer = new SimplangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimplangParser parser = new SimplangParser(tokens);
        parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree
        ParseTree tree = parser.start(); // parse

        // show tree in text form
        System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        Translator translator = new Translator(); 
        walker.walk(translator, tree);
        System.out.println("properties result = " + translator.values.get(tree));
    }
}
