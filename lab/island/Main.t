import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
//import main.parsetree.types.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

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

  public static CstOption extractOption(Token t) {
    String newline = System.getProperty("line.separator");
    String lines[] = t.getText().split(newline);

    int firstCharLine = t.getLine();
    int firstCharColumn = t.getCharPositionInLine()+1;
    int lastCharLine = firstCharLine+lines.length-1;
    int lastCharColumn;
    if(lines.length==1) {
      lastCharColumn = firstCharColumn + lines[0].length();
    } else {
      lastCharColumn = lines[lines.length-1].length();
    }

    return `Cst_OriginTracking(t.getInputStream().getSourceName(), firstCharLine, firstCharColumn, lastCharLine, lastCharColumn);  
  }


  public static class Translator extends Island5ParserBaseListener {
    ParseTreeProperty<Object> values = new ParseTreeProperty<Object>();
    public void setValue(ParseTree node, Object value) { values.put(node, value); } 
    public Object getValue(ParseTree node) { return values.get(node); }
    public void setStringValue(ParseTree node, String value) { setValue(node, value); } 
    public String getStringValue(ParseTree node) { return (String) getValue(node); }

    public CstOperatorList addCstOperator(CstOperatorList operatorList, ParserRuleContext ctx) {
      if(ctx != null) {
        operatorList = `ConcCstOperator(operatorList*, (CstOperator)getValue(ctx));
      }
      return operatorList;
    }

    public CstOperatorList addCstOperator(CstOperatorList operatorList, List<? extends ParserRuleContext> ctxList) {
      for(ParserRuleContext e:ctxList) {
        operatorList = addCstOperator(operatorList, e);
      }
      return operatorList;
    }



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
          CstOption ot = extractOption(prc.getStart());
          bl = `ConcCstBlock(bl*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
        }
      }


      //System.out.println("exitBlock: " + bl);
      setValue(ctx,bl);
    }

    public void exitSlotList(Island5Parser.SlotListContext ctx) {
      CstSlotList res = `ConcCstSlot();
      for(Island5Parser.SlotContext e:ctx.slot()) {
        res = `ConcCstSlot((CstSlot)getValue(e),res*);
      }
      setValue(ctx,res);
      System.out.println("exitSlotList: " + res);
    }

    public void exitSlot(Island5Parser.SlotContext ctx) {
      CstSlot res = null;
      if(ctx.COLON() != null) {
        res = `Cst_Slot(Cst_Name(ctx.id1.getText()), Cst_Type(ctx.id2.getText()));
      } else {
        res = `Cst_Slot(Cst_Name(ctx.id2.getText()), Cst_Type(ctx.id1.getText()));
      }
      setValue(ctx,res);
      System.out.println("exitSlot: " + res);
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
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstType codomain = `Cst_Type(ctx.codomain.getText());
      CstName ctorName = `Cst_Name(ctx.opname.getText());
      CstSlotList argumentList = `ConcCstSlot();
      CstOperatorList operatorList = `ConcCstOperator();
      // if there are arguments
      if(ctx.slotList() != null) {
        argumentList = (CstSlotList) getValue(ctx.slotList());
      }
      // fill constructors
      operatorList = addCstOperator(operatorList, ctx.isFsym());
      operatorList = addCstOperator(operatorList, ctx.make());
      operatorList = addCstOperator(operatorList, ctx.getSlot());
      operatorList = addCstOperator(operatorList, ctx.getDefault());
      CstBlock res = `Cst_OpConstruct(optionList,codomain,ctorName,argumentList,operatorList);
      setValue(ctx,res);
      System.out.println("exitOperator: " + res);
    }

    public void exitOplist(Island5Parser.OplistContext ctx) {
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstType codomain = `Cst_Type(ctx.codomain.getText());
      CstName ctorName = `Cst_Name(ctx.opname.getText());
      CstType domain = `Cst_Type(ctx.domain.getText());
      CstOperatorList operatorList = `ConcCstOperator();
      // fill constructors
      operatorList = addCstOperator(operatorList, ctx.isFsym());
      operatorList = addCstOperator(operatorList, ctx.makeEmptyList());
      operatorList = addCstOperator(operatorList, ctx.makeInsertList());
      operatorList = addCstOperator(operatorList, ctx.getHead());
      operatorList = addCstOperator(operatorList, ctx.getTail());
      operatorList = addCstOperator(operatorList, ctx.isEmptyList());
      CstBlock res = `Cst_OpListConstruct(optionList,codomain,ctorName,domain,operatorList);
      setValue(ctx,res);
      System.out.println("exitOpList: " + res);
    }

    public void exitOparray(Island5Parser.OparrayContext ctx) {
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstType codomain = `Cst_Type(ctx.codomain.getText());
      CstName ctorName = `Cst_Name(ctx.opname.getText());
      CstType domain = `Cst_Type(ctx.domain.getText());
      CstOperatorList operatorList = `ConcCstOperator();
      // fill constructors
      operatorList = addCstOperator(operatorList, ctx.isFsym());
      operatorList = addCstOperator(operatorList, ctx.makeEmptyArray());
      operatorList = addCstOperator(operatorList, ctx.makeAppendArray());
      operatorList = addCstOperator(operatorList, ctx.getElement());
      operatorList = addCstOperator(operatorList, ctx.getSize());
      CstBlock res = `Cst_OpArrayConstruct(optionList,codomain,ctorName,domain,operatorList);
      setValue(ctx,res);
      System.out.println("exitOpArray: " + res);
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
      CstOperator res = `Cst_IsFsym(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitIsFsym: " + res);
    }

    public void exitMake(Island5Parser.MakeContext ctx) {
      CstNameList nameList = `ConcCstName();
      for(TerminalNode e:ctx.ID()) {
        nameList = `ConcCstName(Cst_Name(e.getText()),nameList*);
      }
      CstOperator res = `Cst_Make(nameList,(CstBlockList) getValue(ctx.block()));
      setValue(ctx,res);
      System.out.println("exitMake: " + res);
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
