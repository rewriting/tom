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
    private void setValue(ParseTree node, Object value) { values.put(node, value); } 
    public Object getValue(ParseTree node) { return values.get(node); }
    public void setStringValue(ParseTree node, String value) { setValue(node, value); } 
    public String getStringValue(ParseTree node) { return (String) getValue(node); }

    ParseTreeProperty<Object> values2 = new ParseTreeProperty<Object>();
    private void setValue2(ParseTree node, Object value) { values2.put(node, value); } 
    public Object getValue2(ParseTree node) { return values2.get(node); }

    private void setValue(String debug, ParseTree node, Object value) { 
      values.put(node, value);
      System.out.println(debug + ": " + value);
    } 

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
      CstBlockList blockList = `ConcCstBlock();
      for(int i = 0 ; i<ctx.getChildCount() ; i++) {
        ParseTree child = ctx.getChild(i);
        if(child instanceof Island5Parser.IslandContext) {
          blockList = `ConcCstBlock(blockList*,(CstBlock)getValue(child));
        } else if(child instanceof Island5Parser.WaterContext) {
          ParserRuleContext prc = (ParserRuleContext)child;
          CstOption ot = extractOption(prc.getStart());
          blockList = `ConcCstBlock(blockList*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
        }
      }
      setValue("exitStart",ctx, `Cst_Program(blockList));
    }

    public void exitIsland(Island5Parser.IslandContext ctx) {
      ParseTree child = ctx.getChild(0);
      setValue("exitIsland",ctx,getValue(child));
    }

    public void exitWater(Island5Parser.WaterContext ctx) {
      System.out.println("exitWater: '" + ctx.getText() + "'");
      setStringValue(ctx,ctx.getText());
    }

    public void exitWaterexceptparen(Island5Parser.WaterexceptparenContext ctx) {
      System.out.println("exitWaterexceptparen: '" + ctx.getText() + "'");
      setStringValue(ctx,ctx.getText());
    }

    public void exitMatchStatement(Island5Parser.MatchStatementContext ctx) {
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstBQTermList subjectList = `ConcCstBQTerm();
      if(ctx.bqterm() != null) {
        for(ParserRuleContext e:ctx.bqterm()) {
          subjectList = `ConcCstBQTerm(subjectList*, (CstBQTerm)getValue(e));
        }
      }

      CstConstraintActionList constraintActionList = `ConcCstConstraintAction();
      for(ParserRuleContext e:ctx.actionRule()) {
        constraintActionList = `ConcCstConstraintAction(constraintActionList*, (CstConstraintAction)getValue(e));
      }
      CstBlock res = `Cst_MatchConstruct(optionList,subjectList,constraintActionList);
      setValue("exitMatchStatement", ctx,res);
    }

    public void exitStrategyStatement(Island5Parser.StrategyStatementContext ctx) {
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));

      CstName name = `Cst_Name(ctx.ID().getText());
      CstSlotList argumentList = `ConcCstSlot();
      // if there are arguments
      if(ctx.slotList() != null) {
        argumentList = (CstSlotList) getValue(ctx.slotList());
      }
      CstVisitList visitList = `ConcCstVisit();
      for(ParserRuleContext e:ctx.visit()) {
        visitList = `ConcCstVisit(visitList*, (CstVisit)getValue(e));
      }

      CstBlock res = `Cst_StrategyConstruct(optionList,name,argumentList,(CstBQTerm)getValue(ctx.bqterm()),visitList);
      setValue("exitStrategy", ctx,res);
    }

    public void exitIncludeStatement(Island5Parser.IncludeStatementContext ctx) {
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      String filename = "TODO";
      setValue("exitIncludeStatement", ctx,`Cst_IncludeConstruct(optionList,filename));
    }

    public void exitVisit(Island5Parser.VisitContext ctx) {
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstConstraintActionList l = `ConcCstConstraintAction();
      for(ParserRuleContext e:ctx.actionRule()) {
        l = `ConcCstConstraintAction(l*, (CstConstraintAction)getValue(e));
      }

      CstVisit res = `Cst_VisitTerm( Cst_Type(ctx.ID().getText()), l, optionList);
      setValue("exitVisit", ctx,res);
    }

    public void exitActionRule(Island5Parser.ActionRuleContext ctx) {
      CstConstraintAction res = null;
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstBlockList action = null;
      if(ctx.block() != null) {
        action = (CstBlockList) getValue(ctx.block());
      } else {
        action = `ConcCstBlock(Cst_BQTermToBlock((CstBQTerm)getValue(ctx.bqterm())));
      }
      CstConstraint constraint = `Cst_AndConstraint();
      if(ctx.c != null) {
        constraint = (CstConstraint)getValue(ctx.c);
      } else {
        for(CstPattern p:((CstPatternList)getValue(ctx.patternlist())).getCollectionConcCstPattern()) {
          constraint = `Cst_AndConstraint(constraint*, Cst_MatchArgumentConstraint(p));
        }
        if(ctx.AND() != null) {
          constraint = `Cst_AndConstraint(constraint*,(CstConstraint)getValue(ctx.constraint()));
        } else if(ctx.OR() != null) {
          constraint = `Cst_OrConstraint(constraint*,(CstConstraint)getValue(ctx.constraint()));
        }
      }

      res = `Cst_ConstraintAction(constraint,action,optionList);
      setValue("exitActionRule", ctx,res);
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
      for(ParserRuleContext e:ctx.slot()) {
        res = `ConcCstSlot(res*, (CstSlot)getValue(e));
      }
      setValue("exitSlotList", ctx,res);
    }

    public void exitSlot(Island5Parser.SlotContext ctx) {
      CstSlot res = null;
      if(ctx.COLON() != null) {
        res = `Cst_Slot(Cst_Name(ctx.id1.getText()), Cst_Type(ctx.id2.getText()));
      } else {
        res = `Cst_Slot(Cst_Name(ctx.id2.getText()), Cst_Type(ctx.id1.getText()));
      }
      setValue("exitSlot",ctx,res);
    }

    public void exitPatternlist(Island5Parser.PatternlistContext ctx) {
      CstPatternList res = `ConcCstPattern();
      for(ParserRuleContext e:ctx.pattern()) {
        res = `ConcCstPattern(res*, (CstPattern)getValue(e));
      }
      setValue("exitPatternList", ctx,res);
    }

    public void exitConstraint(Island5Parser.ConstraintContext ctx) {
      CstConstraint res = null;
      if(ctx.AND() != null) {
        res = `Cst_AndConstraint((CstConstraint)getValue(ctx.constraint(0)),(CstConstraint)getValue(ctx.constraint(1)));
      } else if(ctx.OR() != null) {
        res = `Cst_OrConstraint((CstConstraint)getValue(ctx.constraint(0)),(CstConstraint)getValue(ctx.constraint(1)));
      } else if(ctx.MATCH_SYMBOL() != null) {
        res = `Cst_MatchTermConstraint((CstPattern)getValue(ctx.pattern()),(CstBQTerm)getValue(ctx.bqterm()),
            (CstType)getValue2(ctx.bqterm()));
      } else if(ctx.LPAREN() != null && ctx.RPAREN() != null) {
        res = (CstConstraint)getValue(ctx.c);
      } else {
        CstTerm lhs = (CstTerm)getValue(ctx.term(0));
        CstTerm rhs = (CstTerm)getValue(ctx.term(1));
        if(ctx.GREATERTHAN() != null) { res = `Cst_NumGreaterThan(lhs,rhs); }
        else if(ctx.GREATEROREQ() != null) { res = `Cst_NumGreaterOrEqualTo(lhs,rhs); }
        else if(ctx.LOWERTHAN() != null) { res = `Cst_NumLessThan(lhs,rhs); }
        else if(ctx.LOWEROREQ() != null) { res = `Cst_NumLessOrEqualTo(lhs,rhs); }
        else if(ctx.DOUBLEEQ() != null) { res = `Cst_NumEqualTo(lhs,rhs); }
        else if(ctx.DIFFERENT() != null) { res = `Cst_NumDifferent(lhs,rhs); }

      }


      setValue("exitConstraint",ctx,res);
    }

    public void exitTerm(Island5Parser.TermContext ctx) {
      CstTerm res = null;
      if(ctx.var != null && ctx.STAR() == null) {
        res = `Cst_TermVariable(ctx.var.getText());
      } if(ctx.var != null && ctx.STAR() != null) {
        res = `Cst_TermVariableStar(ctx.var.getText());
      } if(ctx.fsym != null) {
        CstTermList args = `ConcCstTerm();
        for(ParserRuleContext e:ctx.term()) {
          args = `ConcCstTerm(args*,(CstTerm)getValue(e));
        }
        res = `Cst_TermAppl(ctx.fsym.getText(),args);
      }
      setValue("exitTerm",ctx,res);
    }

    public void exitBqterm(Island5Parser.BqtermContext ctx) {
      CstBQTerm res = null;
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstType type = (ctx.codomain != null)?`Cst_Type(ctx.codomain.getText()):`Cst_TypeUnknown();

      if(ctx.fsym != null) {
        CstBQTermList args = `ConcCstBQTerm();
        for(ParserRuleContext e:ctx.bqterm()) {
          args = `ConcCstBQTerm(args*,(CstBQTerm)getValue(e));
        }
        res = `Cst_BQAppl(optionList,ctx.fsym.getText(),args);
      } if(ctx.var != null && ctx.STAR() == null) {
        res = `Cst_BQVar(optionList,ctx.var.getText(),type);
      } if(ctx.var != null && ctx.STAR() != null) {
        res = `Cst_BQVarStar(optionList,ctx.var.getText(),type);
      } if(ctx.constant() != null) {
        CstSymbol cst = (CstSymbol) getValue(ctx.constant());
        res = `Cst_BQConstant(optionList,cst.getvalue());
      }

      setValue2(ctx,type);
      setValue("exitBqterm",ctx,res);
    }

    public void exitBqcomposite(Island5Parser.BqcompositeContext ctx) {
      setValue("exitBqcomposite",ctx,`Cst_BQTermToBlock((CstBQTerm)getValue(ctx.composite())));
    }

    public void exitComposite(Island5Parser.CompositeContext ctx) {
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstBQTerm res = null;
      CstType type = `Cst_TypeUnknown();

      if(ctx.fsym != null) {
        CstBQTermList args = `ConcCstBQTerm();
        /*
           if(ctx.compositeplus() != null) {
        // retrieve list of elements separated by COMMA
        CstBQTermList list = ((CstBQTerm)getValue(ctx.compositeplus())).getlist();
        for(CstBQTerm elt:list.getCollectionConcCstBQTerm()) {
        System.out.println("elt: " + elt);
        }
        }
         */

        CstBQTermList accu = `ConcCstBQTerm();
        for(ParserRuleContext e:ctx.composite()) {
          CstBQTerm bq = (CstBQTerm)getValue(e);
          if(bq.isCst_ITL() && bq.getcode() == ",") {
            // put all elements of accu as a subterm
            CstBQTerm newComposite = flattenComposite(`Cst_BQComposite(ConcCstOption(),accu));
            newComposite = mergeITL(newComposite);
            args = `ConcCstBQTerm(args*,newComposite);
            accu = `ConcCstBQTerm();
          } else {
            // retrieve elements separated by COMMA
            accu = `ConcCstBQTerm(accu*,bq);
          }
        }

        res = `Cst_BQAppl(optionList,ctx.fsym.getText(),args);
      } else if(ctx.LPAREN() != null && ctx.RPAREN() != null) {
        CstOptionList optionList1 = `ConcCstOption(extractOption(ctx.LPAREN().getSymbol()));
        CstOptionList optionList2 = `ConcCstOption(extractOption(ctx.RPAREN().getSymbol()));

        CstBQTermList args = `ConcCstBQTerm();
        for(ParserRuleContext e:ctx.composite()) {
          args = `ConcCstBQTerm(args*,(CstBQTerm)getValue(e));
        }
        res = `Cst_BQComposite(optionList, ConcCstBQTerm(
              Cst_ITL(optionList1,ctx.LPAREN().getText()),
              args*,
              Cst_ITL(optionList2,ctx.RPAREN().getText())
              ));

        res = mergeITL(res);
        /*
           res = `Cst_BQComposite(optionList, ConcCstBQTerm(
           Cst_ITL(optionList1,ctx.LPAREN().getText()),
           (CstBQTerm)getValue(ctx.compositeplus()),
           Cst_ITL(optionList2,ctx.RPAREN().getText())
           ));
         */
      } else if(ctx.var != null && ctx.STAR() == null) {
        res = `Cst_BQVar(optionList,ctx.var.getText(),type);
      } else if(ctx.var != null && ctx.STAR() != null) {
        res = `Cst_BQVarStar(optionList,ctx.var.getText(),type);
      } else if(ctx.constant() != null) {
        CstSymbol cst = (CstSymbol) getValue(ctx.constant());
        res = `Cst_BQConstant(optionList,cst.getvalue());
      } else if (ctx.waterexceptparen() != null) {
        System.out.println("composite water");
        res = `Cst_ITL(optionList, getStringValue(ctx.waterexceptparen()));

        /*
           System.out.println("#child: " + ctx.water().size());
           if(ctx.water().size() == 1) {
           CstOptionList optionList1 = `ConcCstOption(extractOption(ctx.getStart()));
           String water = getStringValue(ctx.water(0));
           res = `Cst_ITL(optionList1, water);
           } else {
           CstBQTermList list = `ConcCstBQTerm();
           for(ParserRuleContext e:ctx.water()) {
           String water = getStringValue(e);
           CstOptionList optionList1 = `ConcCstOption(extractOption(e.getStart()));
           list = `ConcCstBQTerm(list*, Cst_ITL(optionList1,  water));
           }
           res = `Cst_BQComposite(optionList,list);
           }
         */

      }

      setValue("exitComposite",ctx,res);
    }

    public void exitCompositeplus(Island5Parser.CompositeplusContext ctx) {
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstBQTermList list = `ConcCstBQTerm();
      for(ParserRuleContext e:ctx.composite()) {
        list = `ConcCstBQTerm(list*,(CstBQTerm)getValue(e));
      }
      CstBQTerm res = `Cst_BQComposite(optionList,list);
      setValue("exitCompositeplus",ctx,res);
    }

    public void exitPattern(Island5Parser.PatternContext ctx) {
      CstPattern res = null;
      if(ctx.AT() != null) {
        res = `Cst_AnnotatedPattern((CstPattern)getValue(ctx.pattern()), ctx.ID().getText());
      } if(ctx.ANTI() != null) {
        res = `Cst_Anti((CstPattern)getValue(ctx.pattern()));
      } if(ctx.explicitArgs() != null) {
        res = `Cst_Appl((CstSymbolList)getValue(ctx.fsymbol()), (CstPatternList)getValue(ctx.explicitArgs()));
      } if(ctx.implicitArgs() != null) {
        res = `Cst_RecordAppl((CstSymbolList)getValue(ctx.fsymbol()), (CstPairPatternList)getValue(ctx.implicitArgs()));
      } if(ctx.var != null && ctx.STAR() == null) {
        res = `Cst_Variable(ctx.var.getText());
      } if(ctx.var != null && ctx.STAR() != null) {
        res = `Cst_VariableStar(ctx.var.getText());
      } if(ctx.UNDERSCORE() != null && ctx.STAR() == null) {
        res = `Cst_UnamedVariable();
      } if(ctx.UNDERSCORE() != null && ctx.STAR() != null) {
        res = `Cst_UnamedVariableStar();
      } if(ctx.constant() != null && ctx.STAR() == null) {
        CstSymbol cst = (CstSymbol) getValue(ctx.constant());
        res = `Cst_Constant(cst.getvalue());
      } if(ctx.constant() != null && ctx.STAR() != null) {
        CstSymbol cst = (CstSymbol) getValue(ctx.constant());
        res = `Cst_ConstantStar(cst.getvalue());
      }
      setValue("exitPattern",ctx,res);
    }

    public void exitFsymbol(Island5Parser.FsymbolContext ctx) {
      CstSymbolList res = `ConcCstSymbol();
      for(ParserRuleContext e:ctx.headSymbol()) {
        res = `ConcCstSymbol(res*, (CstSymbol) getValue(e));
      }
      setValue("exitFsymbol",ctx,res);
    }

    public void exitHeadSymbol(Island5Parser.HeadSymbolContext ctx) {
      CstSymbol res = null;
      if(ctx.QMARK() != null) {
        res = `Cst_Symbol(ctx.ID().getText(), Cst_TheoryAU());
      } else if(ctx.DQMARK() != null) {
        res = `Cst_Symbol(ctx.ID().getText(), Cst_TheoryAC());
      } else if(ctx.ID() != null) {
        res = `Cst_Symbol(ctx.ID().getText(), Cst_TheoryDEFAULT());
      } else if(ctx.constant() != null) {
        res = (CstSymbol) getValue(ctx.constant());
      } 
      setValue("exitHeadSymbol",ctx,res);
    }

    public void exitConstant(Island5Parser.ConstantContext ctx) {
      CstSymbol res = null;
      if(ctx.INTEGER() != null) {
        res = `Cst_ConstantInt(ctx.INTEGER().getText());
      } else if(ctx.LONG() != null) {
        res = `Cst_ConstantLong(ctx.LONG().getText());
      } else if(ctx.CHAR() != null) {
        res = `Cst_ConstantChar(ctx.CHAR().getText());
      } else if(ctx.DOUBLE() != null) {
        res = `Cst_ConstantDouble(ctx.DOUBLE().getText());
      } else if(ctx.STRING() != null) {
        res = `Cst_ConstantString(ctx.STRING().getText());
      }
      setValue("exitConstant",ctx,res);
    }

    public void exitExplicitArgs(Island5Parser.ExplicitArgsContext ctx) {
      int n = ctx.pattern().size();
      CstPatternList res = `ConcCstPattern();
      for(int i=0 ; i<n ; i++) {
        res = `ConcCstPattern(res*, (CstPattern)getValue(ctx.pattern(i)));
      }
      setValue("exitExplicitArgs",ctx,res);
    }

    public void exitImplicitArgs(Island5Parser.ImplicitArgsContext ctx) {
      int n = ctx.ID().size();
      CstPairPatternList res = `ConcCstPairPattern();
      for(int i=0 ; i<n ; i++) {
        res = `ConcCstPairPattern(res*, Cst_PairPattern(ctx.ID(i).getText(), (CstPattern)getValue(ctx.pattern(i))));
      }
      setValue("exitImplicitArgs",ctx,res);
    }

    public void exitTypeterm(Island5Parser.TypetermContext ctx) {
      CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
      CstType typeName = `Cst_Type(ctx.type.getText());
      CstType extendsTypeName = `Cst_TypeUnknown();
      if(ctx.supertype != null) {
        extendsTypeName = `Cst_Type(ctx.supertype.getText());
      }
      CstOperatorList operatorList = `ConcCstOperator();
      operatorList = addCstOperator(operatorList, ctx.implement());
      operatorList = addCstOperator(operatorList, ctx.isSort());
      operatorList = addCstOperator(operatorList, ctx.equalsTerm());
      setValue("exitTypeterm", ctx,
          `Cst_TypetermConstruct(optionList,typeName,extendsTypeName,operatorList));
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
      setValue("exitOperator", ctx,
          `Cst_OpConstruct(optionList,codomain,ctorName,argumentList,operatorList));
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
      setValue("exitOpList", ctx,
          `Cst_OpArrayConstruct(optionList,codomain,ctorName,domain,operatorList));
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
      setValue("exitOpArray", ctx,
          `Cst_OpArrayConstruct(optionList,codomain,ctorName,domain,operatorList));
    }

    public void exitImplement(Island5Parser.ImplementContext ctx) {
      setValue("exitImplement", ctx,
          `Cst_Implement((CstBlockList) getValue(ctx.block())));
    }

    public void exitEqualsTerm(Island5Parser.EqualsTermContext ctx) {
      setValue("exitEquals", ctx,
          `Cst_Equals(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitIsSort(Island5Parser.IsSortContext ctx) {
      setValue("exitIsSort", ctx,
          `Cst_IsSort(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitIsFsym(Island5Parser.IsFsymContext ctx) {
      setValue("exitIsFsym", ctx,
          `Cst_IsFsym(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitMake(Island5Parser.MakeContext ctx) {
      CstNameList nameList = `ConcCstName();
      for(TerminalNode e:ctx.ID()) {
        nameList = `ConcCstName(nameList*, Cst_Name(e.getText()));
      }
      setValue("exitMake", ctx,
          `Cst_Make(nameList,(CstBlockList) getValue(ctx.block())));
    }

    public void exitMakeEmptyList(Island5Parser.MakeEmptyListContext ctx) {
      setValue("exitMakeEmptyList", ctx,
          `Cst_MakeEmptyList((CstBlockList) getValue(ctx.block())));
    }

    public void exitMakeEmptyArray(Island5Parser.MakeEmptyArrayContext ctx) {
      setValue("exitMakeEmptyArray", ctx,
          `Cst_MakeEmptyArray(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitMakeAppendArray(Island5Parser.MakeAppendArrayContext ctx) {
      setValue("exitMakeAppendArray", ctx,
          `Cst_MakeAppend(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitMakeInsertList(Island5Parser.MakeInsertListContext ctx) {
      setValue("exitMakeInsertList", ctx,
          `Cst_MakeInsert(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitGetSlot(Island5Parser.GetSlotContext ctx) {
      setValue("exitGetSlot", ctx,
          `Cst_GetSlot(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitGetHead(Island5Parser.GetHeadContext ctx) {
      setValue("exitGetHead", ctx,
          `Cst_GetHead(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitGetTail(Island5Parser.GetTailContext ctx) {
      setValue("exitGetTail", ctx,
          `Cst_GetTail(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitGetElement(Island5Parser.GetElementContext ctx) {
      setValue("exitGetElement", ctx,
          `Cst_GetElement(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitIsEmptyList(Island5Parser.IsEmptyListContext ctx) {
      setValue("exitIsEmptyList", ctx,
          `Cst_IsEmpty(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitGetSize(Island5Parser.GetSizeContext ctx) {
      setValue("exitGetSize", ctx,
          `Cst_GetSize(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
    }

    public void exitGetDefault(Island5Parser.GetDefaultContext ctx) {
      setValue("exitGetDefault", ctx,
          `Cst_GetDefault(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
    }


  }


  // Composite(...Composite(x y z)...) -> Composite(... x y z ...)
  // do not do the full traversal
  public static CstBQTerm flattenComposite(CstBQTerm t) {
    %match(t) {
      Cst_BQComposite(option,ConcCstBQTerm(C1*,Cst_BQComposite(_,args),C2*)) -> { 
        return `flattenComposite(Cst_BQComposite(option,ConcCstBQTerm(C1*,args*,C2*)));
      }
    }
    return t;
  }

  // ITL(...,"a") ITL(...,"b") -> ITL("a  b")
  public static CstBQTerm mergeITL(CstBQTerm t) {
    String newline = System.getProperty("line.separator");
    CstBQTermList accu = `ConcCstBQTerm();
    boolean activeITL = false;
    String filename = "";
    String s = "";
    int lmin = 0;
    int cmin = 0;
    int lmax = 0;
    int cmax = 0;

    %match(t) {
      Cst_BQComposite(optionList, args) -> {
        for(CstBQTerm e:`args.getCollectionConcCstBQTerm()) {
          if(e.isCst_ITL()) {
            %match(e) {
              Cst_ITL(ConcCstOption(Cst_OriginTracking(name,l1,c1,l2,c2)),text) -> {
                if(activeITL == false) {
                  s = `text;
                  filename = `name;
                  lmin = `l1;
                  cmin = `c1;
                  lmax = `l2;
                  cmax = `c2;
                  activeITL = true;
                } else {
                  if(lmax <= `l1 && cmax <= `c1 && filename == `name) {
                    while(lmax < `l1) {
                      s += newline;
                      lmax++;
                      cmax = 0;
                    }
                    while(cmax < `c1) {
                      s += " ";
                      cmax++;
                    }
                    s += `text;
                  }
                }
              }
            }
          } else {
            // e is not an ITL
            if(activeITL == false) {
              // no current ITL, just append e
              accu = `ConcCstBQTerm(accu*,e);
            } else {
              // flush the current ITL and append e
              accu = `ConcCstBQTerm(accu*,Cst_ITL(ConcCstOption(Cst_OriginTracking(filename,lmin,cmin,lmax,cmax)),s),e);
              activeITL = false;
              filename = "";
              s = "";
              lmin = 0;
              cmin = 0;
              lmax = 0;
              cmax = 0;
            }
          }
        }
        if(activeITL) {
          // flush the last accu
            accu = `ConcCstBQTerm(accu*,Cst_ITL(ConcCstOption(Cst_OriginTracking(filename,lmin,cmin,lmax,cmax)),s));
            activeITL = false;
            filename = "";
            s = "";
            lmin = 0;
            cmin = 0;
            lmax = 0;
            cmax = 0;
        }


        System.out.println("accu = " + accu);
        return `Cst_BQComposite(optionList, accu);
      }
    }
    return t;
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
