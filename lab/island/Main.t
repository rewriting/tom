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
      CstBlockList bl = `ConcCstBlock();

      for(int i = 0 ; i<ctx.getChildCount() ; i++) {
        ParseTree child = ctx.getChild(i);

        if(child instanceof Island5Parser.IslandContext) {
          bl = `ConcCstBlock(bl*,(CstBlock)getValue(child));
        } else if(child instanceof Island5Parser.BlockContext) {
          bl = `ConcCstBlock(bl*,(CstBlock)getValue(child));
        } else if(child instanceof Island5Parser.WaterContext) {
          ParserRuleContext prc = (ParserRuleContext)child;
          Token start = prc.getStart();
          String filename = start.getInputStream().getSourceName();
          int line = start.getLine();
          int pos = start.getCharPositionInLine();
          String text = child.getText();
          int len = text.length();

          //System.out.println(start);
          //System.out.println(filename + ",line=" + line + ",pos=" + pos + "," + text + ",len=" + len );

          CstOption ot = `Cst_OriginTracking(filename,line,pos,line,pos+len);
          bl = `ConcCstBlock(bl*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
        }
      }


      //System.out.println("exitBlock: " + bl);
      setValue(ctx,bl);
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
      CstOperator res = `Cst_Implement((CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitImplement: " + res);
    }

    public void exitEqualsTerm(Island5Parser.EqualsTermContext ctx) {
      CstOperator res = `Cst_Equals(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitEquals: " + res);
    }

    public void exitIsSort(Island5Parser.IsSortContext ctx) {
      CstOperator res = `Cst_IsSort(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitIsSort: " + res);
    }

    public void exitIsFsym(Island5Parser.IsFsymContext ctx) {
      //String name = ctx.ID().getText();
      //CstBlockList block = (CstBlockList) getValue(ctx.block());
      //CstOperator res = `Cst_IsFsym(Cst_Name(name),block);
      CstOperator res = `Cst_IsFsym(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitIsFsym: " + res);
    }

    public void exitMake(Island5Parser.MakeContext ctx) {
    }

    public void exitMakeEmptyList(Island5Parser.MakeEmptyListContext ctx) {
      CstOperator res = `Cst_MakeEmptyList((CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitMakeEmptyList: " + res);
    }

    public void exitMakeEmptyArray(Island5Parser.MakeEmptyArrayContext ctx) {
      CstOperator res = `Cst_MakeEmptyArray(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitMakeEmptyArray: " + res);
    }

    public void exitMakeAppendArray(Island5Parser.MakeAppendArrayContext ctx) {
      CstOperator res = `Cst_MakeAppend(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitMakeAppendArray: " + res);
    }

    public void exitMakeInsertList(Island5Parser.MakeInsertListContext ctx) {
      CstOperator res = `Cst_MakeInsert(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitMakeInsertList: " + res);
    }

    public void exitGetSlot(Island5Parser.GetSlotContext ctx) {
      CstOperator res = `Cst_GetSlot(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitGetSlot: " + res);
    }

    public void exitGetHead(Island5Parser.GetHeadContext ctx) {
      CstOperator res = `Cst_GetHead(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitGetHead: " + res);
    }

    public void exitGetTail(Island5Parser.GetTailContext ctx) {
      CstOperator res = `Cst_GetTail(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitGetTail: " + res);
    }

    public void exitGetElement(Island5Parser.GetElementContext ctx) {
      CstOperator res = `Cst_GetElement(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitGetElement: " + res);
    }

    public void exitIsEmptyList(Island5Parser.IsEmptyListContext ctx) {
      CstOperator res = `Cst_IsEmpty(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitIsEmptyList: " + res);
    }

    public void exitGetSize(Island5Parser.GetSizeContext ctx) {
      CstOperator res = `Cst_GetSize(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitGetSize: " + res);
    }

    public void exitGetDefault(Island5Parser.GetDefaultContext ctx) {
      CstOperator res = `Cst_GetDefault(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitGetDefault: " + res);
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
        System.out.println("ast = " + translator.values.get(tree));
    }
}
