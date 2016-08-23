/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.parser.antlr4;

import java.util.logging.Logger;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

//import tom.engine.adt.code.types.*;
import tom.engine.adt.cst.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

//import tom.library.sl.*;

  /*
   * CST builder
   * traverse the ANTLR tree and generate a Gom Cst_Program, of sort CstProgram
   */
public class CstBuilder extends TomIslandParserBaseListener {
  // %include { ../../../library/mapping/java/sl.tom}
  //%include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../adt/cst/CST.tom }

  private static Logger logger = Logger.getLogger("tom.engine.typer.CstConverter");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

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

  public void exitStart(TomIslandParser.StartContext ctx) {
    CstBlockList blockList = `ConcCstBlock();
    for(int i = 0 ; i<ctx.getChildCount() ; i++) {
      ParseTree child = ctx.getChild(i);
      if(child instanceof TomIslandParser.IslandContext) {
        blockList = `ConcCstBlock(blockList*,(CstBlock)getValue(child));
      } else if(child instanceof TomIslandParser.WaterContext) {
        ParserRuleContext prc = (ParserRuleContext)child;
        CstOption ot = extractOption(prc.getStart());
        blockList = `ConcCstBlock(blockList*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
      }
    }
    setValue("exitStart",ctx, `Cst_Program(blockList));
  }

  public void exitIsland(TomIslandParser.IslandContext ctx) {
    ParseTree child = ctx.getChild(0);
    setValue("exitIsland",ctx,getValue(child));
  }

  public void exitWater(TomIslandParser.WaterContext ctx) {
    //System.out.println("exitWater: '" + ctx.getText() + "'");
    setStringValue(ctx,ctx.getText());
  }

  public void exitWaterexceptparen(TomIslandParser.WaterexceptparenContext ctx) {
    System.out.println("exitWaterexceptparen: '" + ctx.getText() + "'");
    setStringValue(ctx,ctx.getText());
  }

  public void exitMatchStatement(TomIslandParser.MatchStatementContext ctx) {
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

  public void exitStrategyStatement(TomIslandParser.StrategyStatementContext ctx) {
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

  public void exitIncludeStatement(TomIslandParser.IncludeStatementContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    String filename = "TODO";
    setValue("exitIncludeStatement", ctx,`Cst_IncludeConstruct(optionList,filename));
  }

  public void exitVisit(TomIslandParser.VisitContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstConstraintActionList l = `ConcCstConstraintAction();
    for(ParserRuleContext e:ctx.actionRule()) {
      l = `ConcCstConstraintAction(l*, (CstConstraintAction)getValue(e));
    }

    CstVisit res = `Cst_VisitTerm( Cst_Type(ctx.ID().getText()), l, optionList);
    setValue("exitVisit", ctx,res);
  }

  public void exitActionRule(TomIslandParser.ActionRuleContext ctx) {
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
        constraint = `Cst_AndConstraint(constraint, Cst_MatchArgumentConstraint(p));
      }
      if(ctx.AND() != null) {
        constraint = `Cst_AndConstraint(constraint,(CstConstraint)getValue(ctx.constraint()));
      } else if(ctx.OR() != null) {
        constraint = `Cst_OrConstraint(constraint,(CstConstraint)getValue(ctx.constraint()));
      }
    }

    res = `Cst_ConstraintAction(constraint,action,optionList);
    setValue("exitActionRule", ctx,res);
  }

  public void exitBlock(TomIslandParser.BlockContext ctx) {
    CstBlockList bl = `ConcCstBlock();

    for(int i = 0 ; i<ctx.getChildCount() ; i++) {
      ParseTree child = ctx.getChild(i);

      if(child instanceof TomIslandParser.IslandContext) {
        bl = `ConcCstBlock(bl*,(CstBlock)getValue(child));
      } else if(child instanceof TomIslandParser.BlockContext) {
        CstBlockList cbl = (CstBlockList)getValue(child);
        System.out.println("exitBlock cbl: " + cbl);
        bl = `ConcCstBlock(bl*,cbl*);
      } else if(child instanceof TomIslandParser.WaterContext) {
        ParserRuleContext prc = (ParserRuleContext)child;
        CstOption ot = extractOption(prc.getStart());
        bl = `ConcCstBlock(bl*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
      }
    }

    //System.out.println("exitBlock: " + bl);
    setValue(ctx,bl);
  }

  public void exitSlotList(TomIslandParser.SlotListContext ctx) {
    CstSlotList res = `ConcCstSlot();
    for(ParserRuleContext e:ctx.slot()) {
      res = `ConcCstSlot(res*, (CstSlot)getValue(e));
    }
    setValue("exitSlotList", ctx,res);
  }

  public void exitSlot(TomIslandParser.SlotContext ctx) {
    CstSlot res = null;
    if(ctx.COLON() != null) {
      res = `Cst_Slot(Cst_Name(ctx.id1.getText()), Cst_Type(ctx.id2.getText()));
    } else {
      res = `Cst_Slot(Cst_Name(ctx.id2.getText()), Cst_Type(ctx.id1.getText()));
    }
    setValue("exitSlot",ctx,res);
  }

  public void exitPatternlist(TomIslandParser.PatternlistContext ctx) {
    CstPatternList res = `ConcCstPattern();
    for(ParserRuleContext e:ctx.pattern()) {
      res = `ConcCstPattern(res*, (CstPattern)getValue(e));
    }
    setValue("exitPatternList", ctx,res);
  }

  public void exitConstraint(TomIslandParser.ConstraintContext ctx) {
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

  public void exitTerm(TomIslandParser.TermContext ctx) {
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

  public void exitBqterm(TomIslandParser.BqtermContext ctx) {
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

  public void exitBqcomposite(TomIslandParser.BqcompositeContext ctx) {
    setValue("exitBqcomposite",ctx,`Cst_BQTermToBlock((CstBQTerm)getValue(ctx.composite())));
  }

  public void exitComposite(TomIslandParser.CompositeContext ctx) {
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
      // flush the last accu
      %match(accu) {
        ConcCstBQTerm(bq) -> {
          // single element
          args = `ConcCstBQTerm(args*,bq);
        }

        ConcCstBQTerm(_,_,_*) -> {
          // multiple elements: build a composite
          CstBQTerm newComposite = flattenComposite(`Cst_BQComposite(ConcCstOption(),accu));
          newComposite = mergeITL(newComposite);
          args = `ConcCstBQTerm(args*,newComposite);
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

  public void exitCompositeplus(TomIslandParser.CompositeplusContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBQTermList list = `ConcCstBQTerm();
    for(ParserRuleContext e:ctx.composite()) {
      list = `ConcCstBQTerm(list*,(CstBQTerm)getValue(e));
    }
    CstBQTerm res = `Cst_BQComposite(optionList,list);
    setValue("exitCompositeplus",ctx,res);
  }

  public void exitPattern(TomIslandParser.PatternContext ctx) {
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

  public void exitFsymbol(TomIslandParser.FsymbolContext ctx) {
    CstSymbolList res = `ConcCstSymbol();
    for(ParserRuleContext e:ctx.headSymbol()) {
      res = `ConcCstSymbol(res*, (CstSymbol) getValue(e));
    }
    setValue("exitFsymbol",ctx,res);
  }

  public void exitHeadSymbol(TomIslandParser.HeadSymbolContext ctx) {
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

  public void exitConstant(TomIslandParser.ConstantContext ctx) {
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

  public void exitExplicitArgs(TomIslandParser.ExplicitArgsContext ctx) {
    int n = ctx.pattern().size();
    CstPatternList res = `ConcCstPattern();
    for(int i=0 ; i<n ; i++) {
      res = `ConcCstPattern(res*, (CstPattern)getValue(ctx.pattern(i)));
    }
    setValue("exitExplicitArgs",ctx,res);
  }

  public void exitImplicitArgs(TomIslandParser.ImplicitArgsContext ctx) {
    int n = ctx.ID().size();
    CstPairPatternList res = `ConcCstPairPattern();
    for(int i=0 ; i<n ; i++) {
      res = `ConcCstPairPattern(res*, Cst_PairPattern(ctx.ID(i).getText(), (CstPattern)getValue(ctx.pattern(i))));
    }
    setValue("exitImplicitArgs",ctx,res);
  }

  public void exitTypeterm(TomIslandParser.TypetermContext ctx) {
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

  public void exitOperator(TomIslandParser.OperatorContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstType codomain = `Cst_Type(ctx.codomain.getText());
    CstName ctorName = `Cst_Name(ctx.opname.getText());
    // fill arguments
    CstSlotList argumentList = `ConcCstSlot();
    if(ctx.slotList() != null) {
      argumentList = (CstSlotList) getValue(ctx.slotList());
    }
    // fill constructors
    CstOperatorList operatorList = `ConcCstOperator();
    operatorList = addCstOperator(operatorList, ctx.isFsym());
    operatorList = addCstOperator(operatorList, ctx.make());
    operatorList = addCstOperator(operatorList, ctx.getSlot());
    operatorList = addCstOperator(operatorList, ctx.getDefault());
    setValue("exitOperator", ctx,
        `Cst_OpConstruct(optionList,codomain,ctorName,argumentList,operatorList));
  }

  public void exitOplist(TomIslandParser.OplistContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstType codomain = `Cst_Type(ctx.codomain.getText());
    CstName ctorName = `Cst_Name(ctx.opname.getText());
    CstType domain = `Cst_Type(ctx.domain.getText());
    // fill constructors
    CstOperatorList operatorList = `ConcCstOperator();
    operatorList = addCstOperator(operatorList, ctx.isFsym());
    operatorList = addCstOperator(operatorList, ctx.makeEmptyList());
    operatorList = addCstOperator(operatorList, ctx.makeInsertList());
    operatorList = addCstOperator(operatorList, ctx.getHead());
    operatorList = addCstOperator(operatorList, ctx.getTail());
    operatorList = addCstOperator(operatorList, ctx.isEmptyList());
    setValue("exitOpList", ctx,
        `Cst_OpArrayConstruct(optionList,codomain,ctorName,domain,operatorList));
  }

  public void exitOparray(TomIslandParser.OparrayContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstType codomain = `Cst_Type(ctx.codomain.getText());
    CstName ctorName = `Cst_Name(ctx.opname.getText());
    CstType domain = `Cst_Type(ctx.domain.getText());
    // fill constructors
    CstOperatorList operatorList = `ConcCstOperator();
    operatorList = addCstOperator(operatorList, ctx.isFsym());
    operatorList = addCstOperator(operatorList, ctx.makeEmptyArray());
    operatorList = addCstOperator(operatorList, ctx.makeAppendArray());
    operatorList = addCstOperator(operatorList, ctx.getElement());
    operatorList = addCstOperator(operatorList, ctx.getSize());
    setValue("exitOpArray", ctx,
        `Cst_OpArrayConstruct(optionList,codomain,ctorName,domain,operatorList));
  }

  public void exitImplement(TomIslandParser.ImplementContext ctx) {
    setValue("exitImplement", ctx,
        `Cst_Implement((CstBlockList) getValue(ctx.block())));
  }

  public void exitEqualsTerm(TomIslandParser.EqualsTermContext ctx) {
    setValue("exitEquals", ctx,
        `Cst_Equals(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitIsSort(TomIslandParser.IsSortContext ctx) {
    setValue("exitIsSort", ctx,
        `Cst_IsSort(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitIsFsym(TomIslandParser.IsFsymContext ctx) {
    setValue("exitIsFsym", ctx,
        `Cst_IsFsym(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitMake(TomIslandParser.MakeContext ctx) {
    CstNameList nameList = `ConcCstName();
    for(TerminalNode e:ctx.ID()) {
      nameList = `ConcCstName(nameList*, Cst_Name(e.getText()));
    }
    setValue("exitMake", ctx,
        `Cst_Make(nameList,(CstBlockList) getValue(ctx.block())));
  }

  public void exitMakeEmptyList(TomIslandParser.MakeEmptyListContext ctx) {
    setValue("exitMakeEmptyList", ctx,
        `Cst_MakeEmptyList((CstBlockList) getValue(ctx.block())));
  }

  public void exitMakeEmptyArray(TomIslandParser.MakeEmptyArrayContext ctx) {
    setValue("exitMakeEmptyArray", ctx,
        `Cst_MakeEmptyArray(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitMakeAppendArray(TomIslandParser.MakeAppendArrayContext ctx) {
    setValue("exitMakeAppendArray", ctx,
        `Cst_MakeAppend(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitMakeInsertList(TomIslandParser.MakeInsertListContext ctx) {
    setValue("exitMakeInsertList", ctx,
        `Cst_MakeInsert(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitGetSlot(TomIslandParser.GetSlotContext ctx) {
    setValue("exitGetSlot", ctx,
        `Cst_GetSlot(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitGetHead(TomIslandParser.GetHeadContext ctx) {
    setValue("exitGetHead", ctx,
        `Cst_GetHead(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitGetTail(TomIslandParser.GetTailContext ctx) {
    setValue("exitGetTail", ctx,
        `Cst_GetTail(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitGetElement(TomIslandParser.GetElementContext ctx) {
    setValue("exitGetElement", ctx,
        `Cst_GetElement(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitIsEmptyList(TomIslandParser.IsEmptyListContext ctx) {
    setValue("exitIsEmptyList", ctx,
        `Cst_IsEmpty(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitGetSize(TomIslandParser.GetSizeContext ctx) {
    setValue("exitGetSize", ctx,
        `Cst_GetSize(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
  }

  public void exitGetDefault(TomIslandParser.GetDefaultContext ctx) {
    setValue("exitGetDefault", ctx,
        `Cst_GetDefault(Cst_Name(ctx.ID().getText()), (CstBlockList) getValue(ctx.block())));
  }




  // Composite(...Composite(x y z)...) -> Composite(... x y z ...)
  // do not do the full traversal
  private static CstBQTerm flattenComposite(CstBQTerm t) {
    %match(t) {
      Cst_BQComposite(option,ConcCstBQTerm(C1*,Cst_BQComposite(_,args),C2*)) -> { 
        return `flattenComposite(Cst_BQComposite(option,ConcCstBQTerm(C1*,args*,C2*)));
      }
    }
    return t;
  }

  // merge consecutive ITL in a BQTerm
  // ITL(...,"a") ITL(...,"b") -> ITL("a  b")
  private static CstBQTerm mergeITL(CstBQTerm t) {
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

        //System.out.println("accu = " + accu);
        return `Cst_BQComposite(optionList, accu);
      }
    }
    return t;
  }

  private static CstOption extractOption(Token t) {
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


}

