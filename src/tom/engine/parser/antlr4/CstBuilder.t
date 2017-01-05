/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2017, Universite de Lorraine
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
import org.antlr.v4.runtime.misc.*;
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
  %include { ../../adt/cst/CST.tom }

  private String filename;
  private BufferedTokenStream tokens;
  private Set<Token> usedToken; // used to add spaces before of after a given token

  public CstBuilder(String filename, BufferedTokenStream tokens) {
    this.filename = filename;
    this.tokens = tokens;
    this.usedToken = new HashSet<Token>();
  }

  public void cleanUsedToken() {
    this.usedToken = new HashSet<Token>();
  }

  private static Logger logger = Logger.getLogger("tom.engine.typer.CstConverter");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private ParseTreeProperty<Object> values = new ParseTreeProperty<Object>();
  private void setValue(ParseTree node, Object value) { values.put(node, value); } 
  public Object getValue(ParseTree node) { return values.get(node); }
  public void setStringValue(ParseTree node, String value) { setValue(node, value); } 
  public String getStringValue(ParseTree node) { return (String) getValue(node); }
  public CstBlockList getBlockListFromBlock(ParseTree node) { return ((CstBlock) getValue(node)).getblocks(); }

  private ParseTreeProperty<Object> values2 = new ParseTreeProperty<Object>();
  private void setValue2(ParseTree node, Object value) { values2.put(node, value); } 
  public Object getValue2(ParseTree node) { return values2.get(node); }

  private void setValue(String debug, ParseTree node, Object value) { 
    values.put(node, value);
    //System.out.println(debug + ": " + value);
  } 


  /*
   * start : (island | water)*? ;
   */
  public void exitStart(TomIslandParser.StartContext ctx) {
    CstBlockList bl = `ConcCstBlock();
    for(int i = 0 ; i<ctx.getChildCount() ; i++) {
      ParseTree child = ctx.getChild(i);
      if(child instanceof TomIslandParser.IslandContext) {
        bl = `ConcCstBlock((CstBlock)getValue(child), bl*);
      } else if(child instanceof TomIslandParser.WaterContext) {
        //ParserRuleContext prc = (ParserRuleContext)child;
        //CstOption ot = extractOption(prc.getStart());
        //bl = `ConcCstBlock(bl*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
        bl = `ConcCstBlock(buildHostblock((ParserRuleContext)child),bl*);
      }
    }
    setValue("exitStart",ctx, `Cst_Program(bl.reverse()));
  }

  /*
   * island 
   *   : matchStatement
   *   | strategyStatement
   *   | includeStatement
   *   | gomStatement
   *   | typeterm
   *   | operator
   *   | oplist
   *   | oparray
   *   | bqcomposite
   *   | metaquote
   *   ;
   */
  public void exitIsland(TomIslandParser.IslandContext ctx) {
    ParseTree child = ctx.getChild(0);
    setValue("exitIsland",ctx,getValue(child));
  }

  /*
   * water
   *   : .
   *   ;
   */
  public void exitWater(TomIslandParser.WaterContext ctx) {
    setStringValue(ctx,ctx.getText());
  }

  /* 
   * metaquote
   *   : METAQUOTE
   *   | LMETAQUOTE (AT (bqcomposite | composite) AT | water)*? RMETAQUOTE
   *   ;
   */
  public void exitMetaquote(TomIslandParser.MetaquoteContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBlockList bl = `ConcCstBlock();
    if(ctx.METAQUOTE() != null) {
      String code = ctx.METAQUOTE().getText();
      //System.out.println("METAQUOTE: '" + code + "'");
      bl = `ConcCstBlock(HOSTBLOCK(optionList, code),bl*);
    } else {
      Token previousToken = null;
      for(int i = 0 ; i<ctx.getChildCount() ; i++) {
        ParseTree child = ctx.getChild(i);
        if(child instanceof TomIslandParser.CompositeContext) {
          bl = `ConcCstBlock(Cst_BQTermToBlock((CstBQTerm)getValue(child)),bl*);
          previousToken = null;
        } else if(child instanceof TomIslandParser.BqcompositeContext) {
          bl = `ConcCstBlock((CstBlock)getValue(child),bl*);
          previousToken = null;
        } else if(child instanceof TomIslandParser.WaterContext) {
          bl = `ConcCstBlock(buildHostblock((ParserRuleContext)child),bl*);
          previousToken = null;
        } else if(child instanceof TerminalNodeImpl) {
          if(previousToken != null) {
            // this means that there no water, nor composite between the two tokens
            // there is only layout
            Token currentToken = ((TerminalNodeImpl)child).getSymbol();
            //System.out.println("between = '" + betweenToken(previousToken,currentToken) + "'");
            CstOption ot = extractOption(currentToken);
            bl = `ConcCstBlock(HOSTBLOCK(ConcCstOption(ot),betweenToken(previousToken,currentToken)),bl*);
          }
          previousToken = ((TerminalNodeImpl)child).getSymbol();
        }
      }
    }

    setValue("exitMetaquote", ctx,`Cst_Metaquote(optionList,bl.reverse()));
  }

  /*
   * matchStatement
   *   : MATCH (LPAREN (bqterm (COMMA bqterm)*)? RPAREN)? LBRACE actionRule* RBRACE 
   *   ;
   */
  public void exitMatchStatement(TomIslandParser.MatchStatementContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBQTermList subjectList = buildCstBQTermList(ctx.bqterm());
    CstConstraintActionList constraintActionList = buildCstConstraintActionList(ctx.actionRule());
    CstBlock res = `Cst_MatchConstruct(optionList,subjectList,constraintActionList);
    setValue("exitMatchStatement", ctx,res);
  }

  /*
   * strategyStatement
   *   : STRATEGY ID LPAREN slotList? RPAREN EXTENDS bqterm LBRACE visit* RBRACE
   *   ;
   */
  public void exitStrategyStatement(TomIslandParser.StrategyStatementContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstName name = `Cst_Name(ctx.ID().getText());
    CstSlotList argumentList = `ConcCstSlot();
    // if there are arguments
    if(ctx.slotList() != null) {
      argumentList = (CstSlotList) getValue(ctx.slotList());
    }
    CstVisitList visitList = buildCstVisitList(ctx.visit());

    CstBlock res = `Cst_StrategyConstruct(optionList,name,argumentList,(CstBQTerm)getValue(ctx.bqterm()),visitList);
    setValue("exitStrategy", ctx,res);
  }

  /*
   * includeStatement
   *   : INCLUDE LBRACE ID ((SLASH|BACKSLASH) ID)*  RBRACE 
   *   ;
   */
  public void exitIncludeStatement(TomIslandParser.IncludeStatementContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    String filename = "";
    for(int i = 2 ; i<ctx.getChildCount()-1 ; i++) {
      // skip %include {, and the last }
      ParseTree child = ctx.getChild(i);
      filename += child.getText();
    }
    setValue("exitIncludeStatement", ctx,`Cst_IncludeFile(optionList,filename));
  }

  /*
   * gomStatement
   *   : GOM gomOptions? block
   *   ;
   */
  public void exitGomStatement(TomIslandParser.GomStatementContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBlock block = (CstBlock) getValue(ctx.block());
    String text = getText(block.getoptionList());
    // remove starting '{' and ending '}'
    text = text.substring(1,text.length()-1).trim();
    CstNameList nameList = `ConcCstName();
    if(ctx.gomOptions() != null) {
      nameList = (CstNameList) getValue(ctx.gomOptions());
    }

    setValue("exitGomStatement", ctx,`Cst_GomConstruct(optionList,nameList,text));
  }

  /*
   * gomOptions
   *   : LPAREN DMINUSID (COMMA DMINUSID)* RPAREN
   *   ;
   */
  public void exitGomOptions(TomIslandParser.GomOptionsContext ctx) {
    CstNameList nameList = `ConcCstName();
    for(TerminalNode e:ctx.DMINUSID()) {
      nameList = `ConcCstName(nameList*, Cst_Name(e.getText()));
    }
    setValue("exitGomOptions", ctx, nameList);
  }

  /*
   * visit
   *   : VISIT ID LBRACE actionRule* RBRACE
   *   ;
   */
  public void exitVisit(TomIslandParser.VisitContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstConstraintActionList l = buildCstConstraintActionList(ctx.actionRule());
    CstVisit res = `Cst_VisitTerm( Cst_Type(ctx.ID().getText()), l, optionList);
    setValue("exitVisit", ctx,res);
  }

  /*
   * actionRule
   *   : patternlist ((AND | OR) constraint)? ARROW block
   *   | patternlist ((AND | OR) constraint)? ARROW bqterm
   *   | c=constraint ARROW block
   *   | c=constraint ARROW bqterm
   *   ;
   */
  public void exitActionRule(TomIslandParser.ActionRuleContext ctx) {
    CstConstraintAction res = null;
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBlockList action = null;
    if(ctx.block() != null) {
      action = getBlockListFromBlock(ctx.block());
    } else if(ctx.bqterm() != null) {
      action = `ConcCstBlock(Cst_ReturnBQTerm((CstBQTerm)getValue(ctx.bqterm())));
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

  /*
   * block 
   *   : LBRACE (island | block | water)*? RBRACE
   *   ;
   */
  public void exitBlock(TomIslandParser.BlockContext ctx) {
    CstBlockList bl = `ConcCstBlock();

    for(int i = 0 ; i<ctx.getChildCount() ; i++) {
      ParseTree child = ctx.getChild(i);

      if(child instanceof TomIslandParser.IslandContext) {
        bl = `ConcCstBlock((CstBlock)getValue(child),bl*);
      } else if(child instanceof TomIslandParser.BlockContext) {
        bl = `ConcCstBlock((CstBlock)getValue(child),bl*);
      } else if(child instanceof TomIslandParser.WaterContext) {
        //ParserRuleContext prc = (ParserRuleContext)child;
        //CstOption ot = extractOption(prc.getStart());
        //bl = `ConcCstBlock(bl*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
        bl = `ConcCstBlock(buildHostblock((ParserRuleContext)child),bl*);
      }
    }

    CstOption otext  = extractText(ctx);
    setValue(ctx,`Cst_UnamedBlock(ConcCstOption(otext),bl.reverse()));
  }

  /*
   * slotList
   *   : slot (COMMA slot)*
   *   ;
   */
  public void exitSlotList(TomIslandParser.SlotListContext ctx) {
    CstSlotList res = buildCstSlotList(ctx.slot());
    setValue("exitSlotList", ctx,res);
  }

  /*
   * slot
   *   : id1=ID COLON? id2=ID
   *   ;
   */
  public void exitSlot(TomIslandParser.SlotContext ctx) {
    CstSlot res = null;
    if(ctx.COLON() != null) {
      res = `Cst_Slot(Cst_Name(ctx.id1.getText()), Cst_Type(ctx.id2.getText()));
    } else {
      res = `Cst_Slot(Cst_Name(ctx.id2.getText()), Cst_Type(ctx.id1.getText()));
    }
    setValue("exitSlot",ctx,res);
  }

  /*
   * patternlist
   *   : pattern (COMMA pattern)* 
   *   ;
   */
  public void exitPatternlist(TomIslandParser.PatternlistContext ctx) {
    CstPatternList res = buildCstPatternList(ctx.pattern());
    setValue("exitPatternList", ctx,res);
  }

  /*
   * constraint
   *   : constraint AND constraint
   *   | constraint OR constraint
   *   | pattern MATCH_SYMBOL bqterm
   *   | term GREATERTHAN term
   *   | term GREATEROREQ term
   *   | term LOWERTHAN term
   *   | term LOWEROREQ term
   *   | term DOUBLEEQ term
   *   | term DIFFERENT term
   *   | LPAREN c=constraint RPAREN
   *   ;
   */
  public void exitConstraint(TomIslandParser.ConstraintContext ctx) {
    CstConstraint res = null;
    if(ctx.AND() != null || ctx.OR() != null) {
      CstConstraint lhs = (CstConstraint)getValue(ctx.constraint(0));
      CstConstraint rhs = (CstConstraint)getValue(ctx.constraint(1));
      res = (ctx.AND() != null)?`Cst_AndConstraint(lhs,rhs):`Cst_OrConstraint(lhs,rhs);
    } else if(ctx.MATCH_SYMBOL() != null) {
      CstPattern lhs = (CstPattern)getValue(ctx.pattern());
      CstBQTerm rhs = (CstBQTerm)getValue(ctx.bqterm());
      CstType rhs_type = (CstType)getValue2(ctx.bqterm());
      res = `Cst_MatchTermConstraint(lhs,rhs,rhs_type);
    } else if(ctx.LPAREN() != null && ctx.RPAREN() != null) {
      res = (CstConstraint)getValue(ctx.c);
    } else {
      CstTerm lhs = (CstTerm)getValue(ctx.term(0));
      CstTerm rhs = (CstTerm)getValue(ctx.term(1));
      if(ctx.GREATERTHAN() != null) { res = `Cst_NumGreaterThan(lhs,rhs); }
      else if(ctx.GREATEROREQ() != null) { res = `Cst_NumGreaterOrEqualThan(lhs,rhs); }
      else if(ctx.LOWERTHAN() != null) { res = `Cst_NumLessThan(lhs,rhs); }
      else if(ctx.LOWEROREQ() != null) { res = `Cst_NumLessOrEqualThan(lhs,rhs); }
      else if(ctx.DOUBLEEQ() != null) { res = `Cst_EqualTo(lhs,rhs); }
      else if(ctx.DIFFERENT() != null) { res = `Cst_Different(lhs,rhs); }
    }

    setValue("exitConstraint",ctx,res);
  }

  /*
   * term
   *   : var=ID STAR?
   *   | fsym=ID LPAREN (term (COMMA term)*)? RPAREN 
   *   | constant
   *   ;
   */
  public void exitTerm(TomIslandParser.TermContext ctx) {
    CstTerm res = null;
    if(ctx.var != null && ctx.STAR() == null) {
      res = `Cst_TermVariable(ctx.var.getText());
    } else if(ctx.var != null && ctx.STAR() != null) {
      res = `Cst_TermVariableStar(ctx.var.getText());
    } else if(ctx.fsym != null) {
      CstTermList args = buildCstTermList(ctx.term());
      res = `Cst_TermAppl(ctx.fsym.getText(),args);
    } else if(ctx.constant() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res = `Cst_TermConstant(cst.getvalue());
    } 
    setValue("exitTerm",ctx,res);
  }

  /*
   * bqterm
   *   : codomain=ID? BQUOTE? fsym=ID LPAREN (bqterm (COMMA bqterm)*)? RPAREN 
   *   | codomain=ID? BQUOTE? var=ID STAR?
   *   | codomain=ID? constant
   *   ;
   */
  public void exitBqterm(TomIslandParser.BqtermContext ctx) {
    CstBQTerm res = null;
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstType type = (ctx.codomain != null)?`Cst_Type(ctx.codomain.getText()):`Cst_TypeUnknown();

    if(ctx.fsym != null && ctx.LPAREN() != null) {
      CstBQTermList args = buildCstBQTermList(ctx.bqterm());
      res = `Cst_BQAppl(optionList,ctx.fsym.getText(),args);
    } else if(ctx.fsym != null && ctx.LSQUAREBR() != null) {
      CstPairSlotBQTermList args = buildCstPairSlotBQTermList(ctx.pairSlotBqterm());
      res = `Cst_BQRecordAppl(optionList,ctx.fsym.getText(),args);
    } else if(ctx.var != null && ctx.STAR() != null) {
      res = `Cst_BQVarStar(optionList,ctx.var.getText(),type);
    } else if(ctx.var != null && ctx.STAR() == null) {
      res = `Cst_BQVar(optionList,ctx.var.getText(),type);
    } else if(ctx.constant() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res = `Cst_BQConstant(optionList,cst.getvalue());
    } else if(ctx.UNDERSCORE() != null) {
      res = `Cst_BQUnderscore();
    }

    setValue2(ctx,type);
    setValue("exitBqterm",ctx,res);
  }

  /*
   * pairSlotBqterm
   *   : ID EQUAL bqterm
   *   ;
   */
  public void exitPairSlotBqterm(TomIslandParser.PairSlotBqtermContext ctx) {
    CstPairSlotBQTerm res = null;
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstName slotName = `Cst_Name(ctx.ID().getText());
    CstBQTerm bqterm = (CstBQTerm) getValue(ctx.bqterm());
    res = `Cst_PairSlotBQTerm(optionList,slotName,bqterm);
    setValue("exitPairSlotBqterm",ctx,res);
  }

  /*
   * bqcomposite
   *   : BQUOTE composite
   *   | BQUOTE fsym=ID LSQUAREBR (pairSlotBqterm (COMMA pairSlotBqterm)*)? RSQUAREBR 
   *   ;
   */
  public void exitBqcomposite(TomIslandParser.BqcompositeContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBlock res = null;
    if(ctx.fsym != null && ctx.LSQUAREBR() != null) {
      CstPairSlotBQTermList args = buildCstPairSlotBQTermList(ctx.pairSlotBqterm());
      res = `Cst_BQTermToBlock(Cst_BQRecordAppl(optionList,ctx.fsym.getText(),args));
    } else {
      res = `Cst_BQTermToBlock((CstBQTerm)getValue(ctx.composite()));
    }
    setValue("exitBqcomposite",ctx,res);
  }

  /*
   * composite
   *   : fsym=ID LPAREN composite*? RPAREN
   *   | LPAREN composite*? RPAREN
   *   | var=ID STAR?
   *   | constant
   *   | UNDERSCORE
   *   | water
   *   ;
   */
  public void exitComposite(TomIslandParser.CompositeContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBQTerm res = null;
    CstType type = `Cst_TypeUnknown();

    if(ctx.fsym != null) {
      CstBQTermList args = `ConcCstBQTerm();

      CstBQTermList accu = `ConcCstBQTerm();
      for(ParserRuleContext e:ctx.composite()) {
        CstBQTerm bq = (CstBQTerm)getValue(e);
        if(bq.isCst_ITL() && bq.getcode() == ",") {
          // put all elements of accu as a subterm
          CstBQTerm newComposite = `Cst_BQComposite(ConcCstOption(),accu);
          //CstBQTerm newComposite = flattenComposite(`Cst_BQComposite(ConcCstOption(),accu));
          //newComposite = mergeITL(newComposite);
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
          //CstBQTerm newComposite = flattenComposite(`Cst_BQComposite(ConcCstOption(),accu));
          CstBQTerm newComposite = `Cst_BQComposite(ConcCstOption(),accu);
          //newComposite = mergeITL(newComposite);
          args = `ConcCstBQTerm(args*,newComposite);
        }
      }

      res = `Cst_BQAppl(optionList,ctx.fsym.getText(),args);
    } else if(ctx.LPAREN() != null && ctx.RPAREN() != null) {
      CstOptionList optionList1 = `ConcCstOption(extractOption(ctx.LPAREN().getSymbol()));
      CstOptionList optionList2 = `ConcCstOption(extractOption(ctx.RPAREN().getSymbol()));
      CstBQTermList args = buildCstBQTermList(ctx.composite());
      res = `Cst_BQComposite(optionList, ConcCstBQTerm(
            Cst_ITL(optionList1,ctx.LPAREN().getText()),
            args*,
            Cst_ITL(optionList2,ctx.RPAREN().getText())
            ));

      //res = mergeITL(res);
    } else if(ctx.var != null && ctx.STAR() == null) {
      res = `Cst_BQVar(optionList,ctx.var.getText(),type);
    } else if(ctx.var != null && ctx.STAR() != null) {
      res = `Cst_BQVarStar(optionList,ctx.var.getText(),type);
    } else if(ctx.constant() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res = `Cst_BQConstant(optionList,cst.getvalue());
    } else if(ctx.UNDERSCORE() != null) {
      res = `Cst_BQUnderscore();
    } else if (ctx.water() != null) {
      //System.out.println("composite water");
      res = `Cst_ITL(optionList, getStringValue(ctx.water()));
    }

    setValue("exitComposite",ctx,res);
  }

  /*
   * pattern
   *   : ID AT pattern 
   *   | ANTI pattern
   *   | fsymbol explicitArgs
   *   | fsymbol implicitArgs
   *   | var=ID STAR?
   *   | UNDERSCORE STAR?
   *   | constant STAR?
   *   ;
   */
  public void exitPattern(TomIslandParser.PatternContext ctx) {
    CstPattern res = null;
    if(ctx.AT() != null) {
      res = `Cst_AnnotatedPattern((CstPattern)getValue(ctx.pattern()), ctx.ID().getText());
    } else if(ctx.ANTI() != null) {
      res = `Cst_Anti((CstPattern)getValue(ctx.pattern()));
    } else if(ctx.explicitArgs() != null) {
      res = `Cst_Appl((CstSymbolList)getValue(ctx.fsymbol()), (CstPatternList)getValue(ctx.explicitArgs()));
    } else if(ctx.implicitArgs() != null) {
      res = `Cst_RecordAppl((CstSymbolList)getValue(ctx.fsymbol()), (CstPairPatternList)getValue(ctx.implicitArgs()));
    } else if(ctx.var != null && ctx.STAR() == null) {
      res = `Cst_Variable(ctx.var.getText());
    } else if(ctx.var != null && ctx.STAR() != null) {
      res = `Cst_VariableStar(ctx.var.getText());
    } else if(ctx.UNDERSCORE() != null && ctx.STAR() == null) {
      res = `Cst_UnamedVariable();
    } else if(ctx.UNDERSCORE() != null && ctx.STAR() != null) {
      res = `Cst_UnamedVariableStar();
    } else if(ctx.constant() != null) {
      CstSymbolList symbolList = buildCstSymbolList(ctx.constant());
      res = `Cst_ConstantOr(symbolList);
      //CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      //res = `Cst_Constant(cst);
    }
    /*
    } else if(ctx.constant() != null && ctx.STAR() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res = `Cst_ConstantStar(cst);
    }
    */
    setValue("exitPattern",ctx,res);
  }

  /*
   * fsymbol 
   *   : headSymbol
   *   | LPAREN headSymbol (PIPE headSymbol)* RPAREN
   *   ;
   */
  public void exitFsymbol(TomIslandParser.FsymbolContext ctx) {
    CstSymbolList res = buildCstSymbolList(ctx.headSymbol());
    setValue("exitFsymbol",ctx,res);
  }

  /*
   * headSymbol
   *   : ID QMARK?
   *   | ID DQMARK?
   *   | constant
   *   ;
   */
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

  /*
   * constant
   *   : INTEGER
   *   | LONG
   *   | CHAR
   *   | DOUBLE
   *   | STRING
   *   ;
   */
  public void exitConstant(TomIslandParser.ConstantContext ctx) {
    CstSymbol res = null;
    if(ctx.INTEGER() != null) {
      res = `Cst_SymbolInt(ctx.INTEGER().getText());
    } else if(ctx.LONG() != null) {
      res = `Cst_SymbolLong(ctx.LONG().getText());
    } else if(ctx.CHAR() != null) {
      res = `Cst_SymbolChar(ctx.CHAR().getText());
    } else if(ctx.DOUBLE() != null) {
      res = `Cst_SymbolDouble(ctx.DOUBLE().getText());
    } else if(ctx.STRING() != null) {
      res = `Cst_SymbolString(ctx.STRING().getText());
    }
    setValue("exitConstant",ctx,res);
  }

  /*
   * explicitArgs
   *   : LPAREN (pattern (COMMA pattern)*)? RPAREN
   *   ;
   */
  public void exitExplicitArgs(TomIslandParser.ExplicitArgsContext ctx) {
    int n = ctx.pattern().size();
    CstPatternList res = `ConcCstPattern();
    for(int i=0 ; i<n ; i++) {
      res = `ConcCstPattern(res*, (CstPattern)getValue(ctx.pattern(i)));
    }
    setValue("exitExplicitArgs",ctx,res);
  }

  /*
   * implicitArgs
   *   : LSQUAREBR (ID EQUAL pattern (COMMA ID EQUAL pattern)*)? RSQUAREBR 
   *   ;
   */
  public void exitImplicitArgs(TomIslandParser.ImplicitArgsContext ctx) {
    int n = ctx.ID().size();
    CstPairPatternList res = `ConcCstPairPattern();
    for(int i=0 ; i<n ; i++) {
      res = `ConcCstPairPattern(res*, Cst_PairPattern(ctx.ID(i).getText(), (CstPattern)getValue(ctx.pattern(i))));
    }
    setValue("exitImplicitArgs",ctx,res);
  }

  /*
   * typeterm
   *   : TYPETERM type=ID (EXTENDS supertype=ID)? LBRACE 
   *     implement isSort? equalsTerm?
   *     RBRACE
   *   ;
   */
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

  /*
   * operator
   *   : OP codomain=ID opname=ID LPAREN slotList? RPAREN LBRACE 
   *     (isFsym | make | getSlot | getDefault)*
   *     RBRACE
   *   ;
   */
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

  /*
   * oplist
   *   : OPLIST codomain=ID opname=ID LPAREN domain=ID STAR RPAREN LBRACE 
   *     (isFsym | makeEmptyList | makeInsertList | getHead | getTail | isEmptyList)*
   *     RBRACE
   *   ;
   */
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
        `Cst_OpListConstruct(optionList,codomain,ctorName,domain,operatorList));
  }

  /*
   * oparray
   *   : OPARRAY codomain=ID opname=ID LPAREN domain=ID STAR RPAREN LBRACE 
   *     (isFsym | makeEmptyArray | makeAppendArray | getElement | getSize)*
   *     RBRACE
   *   ;
   */
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

  /*
   * implement
   *   : IMPLEMENT block
   *   ;
   */
  public void exitImplement(TomIslandParser.ImplementContext ctx) {
    setValue("exitImplement", ctx,
        `Cst_Implement(getBlockListFromBlock(ctx.block())));
  }

  /*
   * equalsTerm
   *   : EQUALS LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitEqualsTerm(TomIslandParser.EqualsTermContext ctx) {
    setValue("exitEquals", ctx,
        `Cst_Equals(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * isSort
   *   : IS_SORT LPAREN ID RPAREN block
   *   ;
   */
  public void exitIsSort(TomIslandParser.IsSortContext ctx) {
    setValue("exitIsSort", ctx,
        `Cst_IsSort(Cst_Name(ctx.ID().getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * isFsym
   *   : IS_FSYM LPAREN ID RPAREN block
   *   ;
   */
  public void exitIsFsym(TomIslandParser.IsFsymContext ctx) {
    setValue("exitIsFsym", ctx,
        `Cst_IsFsym(Cst_Name(ctx.ID().getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * make
   *   : MAKE LPAREN (ID (COMMA ID)*)? RPAREN block
   *   ;
   */
  public void exitMake(TomIslandParser.MakeContext ctx) {
    CstNameList nameList = `ConcCstName();
    for(TerminalNode e:ctx.ID()) {
      nameList = `ConcCstName(nameList*, Cst_Name(e.getText()));
    }
    setValue("exitMake", ctx,
        `Cst_Make(nameList, getBlockListFromBlock(ctx.block())));
  }

  /*
   * makeEmptyList
   *   : MAKE_EMPTY LPAREN RPAREN block
   *   ;
   */
  public void exitMakeEmptyList(TomIslandParser.MakeEmptyListContext ctx) {
    setValue("exitMakeEmptyList", ctx,
        `Cst_MakeEmptyList(getBlockListFromBlock(ctx.block())));
  }

  /*
   * makeEmptyArray
   *   : MAKE_EMPTY LPAREN ID RPAREN block
   *   ;
   */
  public void exitMakeEmptyArray(TomIslandParser.MakeEmptyArrayContext ctx) {
    setValue("exitMakeEmptyArray", ctx,
        `Cst_MakeEmptyArray(Cst_Name(ctx.ID().getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * makeAppendArray
   *   : MAKE_APPEND LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitMakeAppendArray(TomIslandParser.MakeAppendArrayContext ctx) {
    setValue("exitMakeAppendArray", ctx,
        `Cst_MakeAppend(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * makeInsertList
   *   : MAKE_INSERT LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitMakeInsertList(TomIslandParser.MakeInsertListContext ctx) {
    setValue("exitMakeInsertList", ctx,
        `Cst_MakeInsert(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * getSlot
   *   : GET_SLOT LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitGetSlot(TomIslandParser.GetSlotContext ctx) {
    setValue("exitGetSlot", ctx,
        `Cst_GetSlot(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * getHead
   *   : GET_HEAD LPAREN ID RPAREN block
   *   ;
   */
  public void exitGetHead(TomIslandParser.GetHeadContext ctx) {
    setValue("exitGetHead", ctx,
        `Cst_GetHead(Cst_Name(ctx.ID().getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * getTail
   *   : GET_TAIL LPAREN ID RPAREN block
   *   ;
   */
  public void exitGetTail(TomIslandParser.GetTailContext ctx) {
    setValue("exitGetTail", ctx,
        `Cst_GetTail(Cst_Name(ctx.ID().getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * getElement
   *   : GET_ELEMENT LPAREN id1=ID COMMA id2=ID RPAREN block
   *   ;
   */
  public void exitGetElement(TomIslandParser.GetElementContext ctx) {
    setValue("exitGetElement", ctx,
        `Cst_GetElement(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * isEmptyList
   *   : IS_EMPTY LPAREN ID RPAREN block
   *   ;
   */
  public void exitIsEmptyList(TomIslandParser.IsEmptyListContext ctx) {
    setValue("exitIsEmptyList", ctx,
        `Cst_IsEmpty(Cst_Name(ctx.ID().getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * getSize
   *   : GET_SIZE LPAREN ID RPAREN block
   *   ;
   */
  public void exitGetSize(TomIslandParser.GetSizeContext ctx) {
    setValue("exitGetSize", ctx,
        `Cst_GetSize(Cst_Name(ctx.ID().getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * getDefault
   *   : GET_DEFAULT LPAREN ID RPAREN block
   *   ;
   */
  public void exitGetDefault(TomIslandParser.GetDefaultContext ctx) {
    setValue("exitGetDefault", ctx,
        `Cst_GetDefault(Cst_Name(ctx.ID().getText()), getBlockListFromBlock(ctx.block())));
  }

  /*
   * End of grammar
   */

  private String getText(CstOptionList ol) {
    %match(ol) {
      ConcCstOption(_*,Cst_OriginText(text),_*) -> {
        return `text;
      }
    }
    return "";
  }

  /*
   * given a context corresponding to water token
   * search spaces and layouts in hidden channels to
   * build a HOSTBLOCK with spaces and layout
   */
  private CstBlock buildHostblock(ParserRuleContext ctx) {
    String s = getStringValue(ctx);

    Token token = ctx.getStart();
    int firstCharLine = token.getLine();
    int firstCharColumn = token.getCharPositionInLine()+1;
    int lastCharLine = firstCharLine;
    int lastCharColumn = firstCharColumn + token.getText().length();

    List<Token> left = tokens.getHiddenTokensToLeft(token.getTokenIndex());
    List<Token> right = tokens.getHiddenTokensToRight(token.getTokenIndex());

    String sleft = "";
    String sright = "";
    if(left != null) {
      Token firstToken = left.get(0);
      if(!usedToken.contains(firstToken)) {
        // a given token should be used only once
        firstCharLine = firstToken.getLine();
        firstCharColumn = firstToken.getCharPositionInLine()+1;
      }

      for(Token space:left) {
        if(!usedToken.contains(space)) {
          sleft += space.getText();
          usedToken.add(space);
        }
      }
    }

    if(right != null) {
      String newline = System.getProperty("line.separator");
      Token lastToken = right.get(right.size()-1);
      //System.out.println("lastToken: " + lastToken);
      if(!usedToken.contains(lastToken)) {
        if(lastToken.getText().equals(newline)) {
          lastCharColumn = 1;
          lastCharLine = firstCharLine + 1;
        } else {
          lastCharLine = lastToken.getLine();
          lastCharColumn = lastToken.getCharPositionInLine()+1 + lastToken.getText().length();
        }
      }

      for(Token space:right) {
        if(!usedToken.contains(space)) {
          sright += space.getText();
          usedToken.add(space);
        }
      }

    }
    s = sleft + s + sright;


    String source = token.getTokenSource().getSourceName();
    if(source == IntStream.UNKNOWN_SOURCE_NAME) {
      source = this.filename;
    }
    CstOption ot = `Cst_OriginTracking(source, firstCharLine, firstCharColumn, lastCharLine, lastCharColumn);  
    //System.out.println("ot: " + ot);
    //System.out.println("hb: " + `HOSTBLOCK(ConcCstOption(ot), s));
    return `HOSTBLOCK(ConcCstOption(ot), s);
  }

  /*
   * given a context
   * returns the string corresponding to the parsed source code
   * (including spaces and layout)
   */
  private CstOption extractText(ParserRuleContext ctx) {
    int a = ctx.start.getStartIndex();
    int b = ctx.stop.getStopIndex();
    Interval interval = new Interval(a,b);
    //System.out.println("interval1: " + interval1);
    //System.out.println("interval2: " + interval2);
    String text = ctx.getStart().getInputStream().getText(interval);
    //System.out.println("text: " + text);
    return `Cst_OriginText(text);
  }

  private CstOption extractOption(Token t) {
    String newline = System.getProperty("line.separator");
    String text = t.getText();
    //System.out.println("text: '" + text + "'");

    int firstCharLine = t.getLine();
    int firstCharColumn = t.getCharPositionInLine()+1;
    int lastCharLine;
    int lastCharColumn;

    if(text.equals(newline)) {
      lastCharColumn = 1;
      lastCharLine = firstCharLine + 1;
    } else {
      lastCharColumn = firstCharColumn + text.length();
      lastCharLine = firstCharLine;
    }

    //System.out.println(%[extractOption: '@text@' (@firstCharLine@,@firstCharColumn@) -- (@lastCharLine@,@lastCharColumn@)]%);

    String source = t.getTokenSource().getSourceName();
    if(source == IntStream.UNKNOWN_SOURCE_NAME) {
      source = this.filename;
    }
    return `Cst_OriginTracking(source, firstCharLine, firstCharColumn, lastCharLine, lastCharColumn);  
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

  /*
   * convert list of context into CstList 
   */
  private CstBQTermList buildCstBQTermList(List<? extends ParserRuleContext> ctx) {
    CstBQTermList res = `ConcCstBQTerm();
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = `ConcCstBQTerm(res*, (CstBQTerm)getValue(e));
      }
    }
    return res;
  }
  
  private CstPairSlotBQTermList buildCstPairSlotBQTermList(List<? extends ParserRuleContext> ctx) {
    CstPairSlotBQTermList res = `ConcCstPairSlotBQTerm();
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = `ConcCstPairSlotBQTerm(res*, (CstPairSlotBQTerm)getValue(e));
      }
    }
    return res;
  }

  private CstConstraintActionList buildCstConstraintActionList(List<? extends ParserRuleContext> ctx) {
    CstConstraintActionList res = `ConcCstConstraintAction();
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = `ConcCstConstraintAction(res*, (CstConstraintAction)getValue(e));
      }
    }
    return res;
  }

  private CstVisitList buildCstVisitList(List<? extends ParserRuleContext> ctx) {
    CstVisitList res = `ConcCstVisit();
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = `ConcCstVisit(res*, (CstVisit)getValue(e));
      }
    }
    return res;
  }

  private CstSlotList buildCstSlotList(List<? extends ParserRuleContext> ctx) {
    CstSlotList res = `ConcCstSlot();
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = `ConcCstSlot(res*, (CstSlot)getValue(e));
      }
    }
    return res;
  }

  private CstPatternList buildCstPatternList(List<? extends ParserRuleContext> ctx) {
    CstPatternList res = `ConcCstPattern();
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = `ConcCstPattern(res*, (CstPattern)getValue(e));
      }
    }
    return res;
  }

  private CstTermList buildCstTermList(List<? extends ParserRuleContext> ctx) {
    CstTermList res = `ConcCstTerm();
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = `ConcCstTerm(res*, (CstTerm)getValue(e));
      }
    }
    return res;
  }

  private CstSymbolList buildCstSymbolList(List<? extends ParserRuleContext> ctx) {
    CstSymbolList res = `ConcCstSymbol();
    if(ctx != null) {
      for(ParserRuleContext e:ctx) {
        res = `ConcCstSymbol(res*, (CstSymbol)getValue(e));
      }
    }
    return res;
  }

  /*
   * add missing spaces/newlines between two tokens
   */
  private static String betweenToken(Token t1, Token t2) {
    String newline = System.getProperty("line.separator");
    int l = t1.getLine();
    int c = t1.	getCharPositionInLine()+1;
    String res = "";
    while(l < t2.getLine()) {
      res += newline;
      l++;
      c = 1;
    }
    while(c < t2.	getCharPositionInLine()) {
      res += " ";
      c++;
    }
    return res;
  }
}

