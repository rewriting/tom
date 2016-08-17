import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
//import main.parsetree.types.*;

import java.io.FileInputStream;
import java.io.InputStream;

import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.typeconstraints.types.*;
import tom.engine.adt.cst.types.*;


public class Main {
  %include { ../../src/gen/tom/engine/adt/tomsignature/TomSignature.tom }

  public static class Translator extends Island5ParserBaseListener {
    ParseTreeProperty<Object> values = new ParseTreeProperty<Object>();
    public void setValue(ParseTree node, Object value) { values.put(node, value); } 
    public Object getValue(ParseTree node) { return values.get(node); }
    public void setStringValue(ParseTree node, String value) { setValue(node, value); } 
    public String getStringValue(ParseTree node) { return (String) getValue(node); }


    public void exitStart(Island5Parser.StartContext ctx) {
    }

    public void exitIsland(Island5Parser.IslandContext ctx) {
    }
 
    public void exitWater(Island5Parser.WaterContext ctx) {
      //System.out.println("exitWater: '" + ctx.getText() + "'");
      setStringValue(ctx,ctx.getText());
    }

    public void exitMatchStatement(Island5Parser.MatchStatementContext ctx) {
    }

    public void exitStrategyStatement(Island5Parser.StrategyStatementContext ctx) {
    }

    public void exitIncludeStatement(Island5Parser.IncludeStatementContext ctx) {
    }

    public void exitVisit(Island5Parser.VisitContext ctx) {
    }

    public void exitActionRule(Island5Parser.ActionRuleContext ctx) {
    }

    public void exitBlock(Island5Parser.BlockContext ctx) {
      CodeList res = `concCode();

      for(int i = 0 ; i<ctx.getChildCount() ; i++) {
        ParseTree child = ctx.getChild(i);
        System.out.println("class = " + child.getClass());

        if(child instanceof Island5Parser.IslandContext) {
        } else if(child instanceof Island5Parser.BlockContext) {
        } else if(child instanceof Island5Parser.WaterContext) {
          res = `concCode(res*, TargetLanguageToCode(ITL(getStringValue(child))));
        }
        
        //String ruleName = Island5Parser.ruleNames[ctx.getRuleIndex()];
        //System.out.println("ruleName = " + ruleName);
      }

      /*
      int nbIsland = ctx.island().size();
      int nbBlock = ctx.block().size();
      int nbWater = ctx.water().size();
      System.out.println("nbIsland = " + nbIsland);
      System.out.println("nbBlock  = " + nbBlock);
      System.out.println("nbWater    = " + nbWater);

      for(Island5Parser.WaterContext c:ctx.water()) {
        System.out.println(c.getText());
      }
      */

      System.out.println("exitBlock: " + res);
      setValue(ctx,res);
    }

    public void exitSlotList(Island5Parser.SlotListContext ctx) {
    }

    public void exitSlot(Island5Parser.SlotContext ctx) {
    }

    public void exitPatternlist(Island5Parser.PatternlistContext ctx) {
    }

    public void exitConstraint(Island5Parser.ConstraintContext ctx) {
    }

    public void exitTerm(Island5Parser.TermContext ctx) {
    }

    public void exitBqterm(Island5Parser.BqtermContext ctx) {
    }

    public void exitPattern(Island5Parser.PatternContext ctx) {
    }

    public void exitFsymbol(Island5Parser.FsymbolContext ctx) {
    }

    public void exitHeadSymbol(Island5Parser.HeadSymbolContext ctx) {
    }

    public void exitConstant(Island5Parser.ConstantContext ctx) {
    }

    public void exitExplicitArgs(Island5Parser.ExplicitArgsContext ctx) {
    }

    public void exitImplicitArgs(Island5Parser.ImplicitArgsContext ctx) {
    }

    public void exitTypeterm(Island5Parser.TypetermContext ctx) {
    }

    public void exitOperator(Island5Parser.OperatorContext ctx) {
    }

    public void exitOplist(Island5Parser.OplistContext ctx) {
    }

    public void exitOparray(Island5Parser.OparrayContext ctx) {
    }

    public void exitImplement(Island5Parser.ImplementContext ctx) {
    }

    public void exitEqualsTerm(Island5Parser.EqualsTermContext ctx) {
    }

    public void exitIsSort(Island5Parser.IsSortContext ctx) {
    }

    public void exitIsFsym(Island5Parser.IsFsymContext ctx) {
      String name = ctx.ID().getText();
      
    }

    public void exitMake(Island5Parser.MakeContext ctx) {
    }

    public void exitMakeEmptyList(Island5Parser.MakeEmptyListContext ctx) {
    }

    public void exitMakeEmptyArray(Island5Parser.MakeEmptyArrayContext ctx) {
    }

    public void exitMakeAppendArray(Island5Parser.MakeAppendArrayContext ctx) {
    }

    public void exitMakeInsertList(Island5Parser.MakeInsertListContext ctx) {
    }

    public void exitGetSlot(Island5Parser.GetSlotContext ctx) {
    }

    public void exitGetHead(Island5Parser.GetHeadContext ctx) {
    }

    public void exitGetTail(Island5Parser.GetTailContext ctx) {
    }

    public void exitGetElement(Island5Parser.GetElementContext ctx) {
    }

    public void exitIsEmptyList(Island5Parser.IsEmptyListContext ctx) {
    }

    public void exitGetSize(Island5Parser.GetSizeContext ctx) {
    }

    public void exitGetDefault(Island5Parser.GetDefaultContext ctx) {
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
        Island5Lexer lexer = new Island5Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Island5Parser parser = new Island5Parser(tokens);
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
