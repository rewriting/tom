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

package tom.engine.parser.tomjava;

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
public class CstBuilder extends TomJavaParserBaseListener {
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

  public void exitCompilationUnit(TomJavaParser.CompilationUnitContext ctx) {
    setValue("exitCompilationUnit", ctx, buildBlockList(ctx));
  }
  
  public void exitDeclarationsUnit(TomJavaParser.DeclarationsUnitContext ctx) {
    setValue("exitDeclarationsUnit", ctx, buildBlockList(ctx));
  }
  
  public void exitExpressionUnit(TomJavaParser.ExpressionUnitContext ctx) {
    setValue("exitExpressionUnit", ctx, buildBlockList(ctx));
  }

  public void exitPackageDeclaration(TomJavaParser.PackageDeclarationContext ctx) {
    setValue("exitPackageDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitImportDeclaration(TomJavaParser.ImportDeclarationContext ctx) {
    setValue("exitImportDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitTypeDeclaration(TomJavaParser.TypeDeclarationContext ctx) {
    setValue("exitTypeDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitModifier(TomJavaParser.ModifierContext ctx) {
    setValue("exitModifier", ctx, buildBlockList(ctx));
  }

  public void exitClassOrInterfaceModifier(TomJavaParser.ClassOrInterfaceModifierContext ctx) {
    setValue("exitClassOrInterfaceModifier", ctx, buildBlockList(ctx));
  }

  public void exitVariableModifier(TomJavaParser.VariableModifierContext ctx) {
    setValue("exitVariableModifier", ctx, buildBlockList(ctx));
  }

  public void exitClassDeclaration(TomJavaParser.ClassDeclarationContext ctx) {
    setValue("exitClassDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitTypeParameters(TomJavaParser.TypeParametersContext ctx) {
    setValue("exitTypeParameters", ctx, buildBlockList(ctx));
  }

  public void exitTypeParameter(TomJavaParser.TypeParameterContext ctx) {
    setValue("exitTypeParameter", ctx, buildBlockList(ctx));
  }

  public void exitTypeBound(TomJavaParser.TypeBoundContext ctx) {
    setValue("exitTypeBound", ctx, buildBlockList(ctx));
  }

  public void exitEnumDeclaration(TomJavaParser.EnumDeclarationContext ctx) {
    setValue("exitEnumDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitEnumConstants(TomJavaParser.EnumConstantsContext ctx) {
    setValue("exitEnumConstants", ctx, buildBlockList(ctx));
  }

  public void exitEnumConstant(TomJavaParser.EnumConstantContext ctx) {
    setValue("exitEnumConstant", ctx, buildBlockList(ctx));
  }

  public void exitEnumBodyDeclarations(TomJavaParser.EnumBodyDeclarationsContext ctx) {
    setValue("exitEnumBodyDeclarations", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceDeclaration(TomJavaParser.InterfaceDeclarationContext ctx) {
    setValue("exitInterfaceDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitClassBody(TomJavaParser.ClassBodyContext ctx) {
    setValue("exitClassBody", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceBody(TomJavaParser.InterfaceBodyContext ctx) {
    setValue("exitInterfaceBody", ctx, buildBlockList(ctx));
  }

  public void exitClassBodyDeclaration(TomJavaParser.ClassBodyDeclarationContext ctx) {
    setValue("exitClassBodyDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitMemberDeclaration(TomJavaParser.MemberDeclarationContext ctx) {
    setValue("exitMemberDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitMethodDeclaration(TomJavaParser.MethodDeclarationContext ctx) {
    setValue("exitMethodDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitMethodBody(TomJavaParser.MethodBodyContext ctx) {
    setValue("exitMethodBody", ctx, buildBlockList(ctx));
  }

  public void exitTypeTypeOrVoid(TomJavaParser.TypeTypeOrVoidContext ctx) {
    setValue("exitTypeTypeOrVoid", ctx, buildBlockList(ctx));
  }

  public void exitGenericMethodDeclaration(TomJavaParser.GenericMethodDeclarationContext ctx) {
    setValue("exitGenericMethodDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitGenericConstructorDeclaration(TomJavaParser.GenericConstructorDeclarationContext ctx) {
    setValue("exitGenericConstructorDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitConstructorDeclaration(TomJavaParser.ConstructorDeclarationContext ctx) {
    setValue("exitConstructorDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitFieldDeclaration(TomJavaParser.FieldDeclarationContext ctx) {
    setValue("exitFieldDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceBodyDeclaration(TomJavaParser.InterfaceBodyDeclarationContext ctx) {
    setValue("exitInterfaceBodyDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceMemberDeclaration(TomJavaParser.InterfaceMemberDeclarationContext ctx) {
    setValue("exitInterfaceMemberDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitConstDeclaration(TomJavaParser.ConstDeclarationContext ctx) {
    setValue("exitConstDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitConstantDeclarator(TomJavaParser.ConstantDeclaratorContext ctx) {
    setValue("exitConstantDeclarator", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceMethodDeclaration(TomJavaParser.InterfaceMethodDeclarationContext ctx) {
    setValue("exitInterfaceMethodDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitInterfaceMethodModifier(TomJavaParser.InterfaceMethodModifierContext ctx) {
    setValue("exitInterfaceMethodModifier", ctx, buildBlockList(ctx));
  }

  public void exitGenericInterfaceMethodDeclaration(TomJavaParser.GenericInterfaceMethodDeclarationContext ctx) {
    setValue("exitGenericInterfaceMethodDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitVariableDeclarators(TomJavaParser.VariableDeclaratorsContext ctx) {
    setValue("exitVariableDeclarators", ctx, buildBlockList(ctx));
  }

  public void exitVariableDeclarator(TomJavaParser.VariableDeclaratorContext ctx) {
    setValue("exitVariableDeclarator", ctx, buildBlockList(ctx));
  }

  public void exitVariableDeclaratorId(TomJavaParser.VariableDeclaratorIdContext ctx) {
    setValue("exitVariableDeclaratorId", ctx, buildBlockList(ctx));
  }

  public void exitVariableInitializer(TomJavaParser.VariableInitializerContext ctx) {
    setValue("exitVariableInitializer", ctx, buildBlockList(ctx));
  }

  public void exitArrayInitializer(TomJavaParser.ArrayInitializerContext ctx) {
    setValue("exitArrayInitializer", ctx, buildBlockList(ctx));
  }

  public void exitClassOrInterfaceType(TomJavaParser.ClassOrInterfaceTypeContext ctx) {
    setValue("exitClassOrInterfaceType", ctx, buildBlockList(ctx));
  }

  public void exitTypeArgument(TomJavaParser.TypeArgumentContext ctx) {
    setValue("exitTypeArgument", ctx, buildBlockList(ctx));
  }

  public void exitQualifiedNameList(TomJavaParser.QualifiedNameListContext ctx) {
    setValue("exitQualifiedNameList", ctx, buildBlockList(ctx));
  }

  public void exitFormalParameters(TomJavaParser.FormalParametersContext ctx) {
    setValue("exitFormalParameters", ctx, buildBlockList(ctx));
  }

  public void exitFormalParameterList(TomJavaParser.FormalParameterListContext ctx) {
    setValue("exitFormalParameterList", ctx, buildBlockList(ctx));
  }

  public void exitFormalParameter(TomJavaParser.FormalParameterContext ctx) {
    setValue("exitFormalParameter", ctx, buildBlockList(ctx));
  }

  public void exitLastFormalParameter(TomJavaParser.LastFormalParameterContext ctx) {
    setValue("exitLastFormalParameter", ctx, buildBlockList(ctx));
  }

  public void exitQualifiedName(TomJavaParser.QualifiedNameContext ctx) {
    setValue("exitQualifiedName", ctx, buildBlockList(ctx));
  }

  public void exitLiteral(TomJavaParser.LiteralContext ctx) {
    setValue("exitLiteral", ctx, buildBlockList(ctx));
  }

  public void exitIntegerLiteral(TomJavaParser.IntegerLiteralContext ctx) {
    setValue("exitIntegerLiteral", ctx, buildBlockList(ctx));
  }

  public void exitFloatLiteral(TomJavaParser.FloatLiteralContext ctx) {
    setValue("exitFloatLiteral", ctx, buildBlockList(ctx));
  }

  public void exitAnnotation(TomJavaParser.AnnotationContext ctx) {
    setValue("exitAnnotation", ctx, buildBlockList(ctx));
  }

  public void exitElementValuePairs(TomJavaParser.ElementValuePairsContext ctx) {
    setValue("exitElementValuePairs", ctx, buildBlockList(ctx));
  }

  public void exitElementValuePair(TomJavaParser.ElementValuePairContext ctx) {
    setValue("exitElementValuePair", ctx, buildBlockList(ctx));
  }

  public void exitElementValue(TomJavaParser.ElementValueContext ctx) {
    setValue("exitElementValue", ctx, buildBlockList(ctx));
  }

  public void exitElementValueArrayInitializer(TomJavaParser.ElementValueArrayInitializerContext ctx) {
    setValue("exitElementValueArrayInitializer", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationTypeDeclaration(TomJavaParser.AnnotationTypeDeclarationContext ctx) {
    setValue("exitAnnotationTypeDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationTypeBody(TomJavaParser.AnnotationTypeBodyContext ctx) {
    setValue("exitAnnotationTypeBody", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationTypeElementDeclaration(TomJavaParser.AnnotationTypeElementDeclarationContext ctx) {
    setValue("exitAnnotationTypeElementDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationTypeElementRest(TomJavaParser.AnnotationTypeElementRestContext ctx) {
    setValue("exitAnnotationTypeElementRest", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationMethodOrConstantRest(TomJavaParser.AnnotationMethodOrConstantRestContext ctx) {
    setValue("exitAnnotationMethodOrConstantRest", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationMethodRest(TomJavaParser.AnnotationMethodRestContext ctx) {
    setValue("exitAnnotationMethodRest", ctx, buildBlockList(ctx));
  }

  public void exitAnnotationConstantRest(TomJavaParser.AnnotationConstantRestContext ctx) {
    setValue("exitAnnotationConstantRest", ctx, buildBlockList(ctx));
  }

  public void exitDefaultValue(TomJavaParser.DefaultValueContext ctx) {
    setValue("exitDefaultValue", ctx, buildBlockList(ctx));
  }

  public void exitBlock(TomJavaParser.BlockContext ctx) {
    setValue("exitBlock", ctx, buildBlockList(ctx));
  }

  public void exitBlockStatement(TomJavaParser.BlockStatementContext ctx) {
    setValue("exitBlockStatement", ctx, buildBlockList(ctx));
  }

  public void exitLocalVariableDeclaration(TomJavaParser.LocalVariableDeclarationContext ctx) {
    setValue("exitLocalVariableDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitLocalTypeDeclaration(TomJavaParser.LocalTypeDeclarationContext ctx) {
    setValue("exitLocalTypeDeclaration", ctx, buildBlockList(ctx));
  }

  public void exitStatement(TomJavaParser.StatementContext ctx) {
    setValue("exitStatement", ctx, buildBlockList(ctx));
  }

  public void exitCatchClause(TomJavaParser.CatchClauseContext ctx) {
    setValue("exitCatchClause", ctx, buildBlockList(ctx));
  }

  public void exitCatchType(TomJavaParser.CatchTypeContext ctx) {
    setValue("exitCatchType", ctx, buildBlockList(ctx));
  }

  public void exitFinallyBlock(TomJavaParser.FinallyBlockContext ctx) {
    setValue("exitFinallyBlock", ctx, buildBlockList(ctx));
  }

  public void exitResourceSpecification(TomJavaParser.ResourceSpecificationContext ctx) {
    setValue("exitResourceSpecification", ctx, buildBlockList(ctx));
  }

  public void exitResources(TomJavaParser.ResourcesContext ctx) {
    setValue("exitResources", ctx, buildBlockList(ctx));
  }

  public void exitResource(TomJavaParser.ResourceContext ctx) {
    setValue("exitResource", ctx, buildBlockList(ctx));
  }

  public void exitSwitchBlockStatementGroup(TomJavaParser.SwitchBlockStatementGroupContext ctx) {
    setValue("exitSwitchBlockStatementGroup", ctx, buildBlockList(ctx));
  }

  public void exitSwitchLabel(TomJavaParser.SwitchLabelContext ctx) {
    setValue("exitSwitchLabel", ctx, buildBlockList(ctx));
  }

  public void exitForControl(TomJavaParser.ForControlContext ctx) {
    setValue("exitForControl", ctx, buildBlockList(ctx));
  }

  public void exitForInit(TomJavaParser.ForInitContext ctx) {
    setValue("exitForInit", ctx, buildBlockList(ctx));
  }

  public void exitEnhancedForControl(TomJavaParser.EnhancedForControlContext ctx) {
    setValue("exitEnhancedForControl", ctx, buildBlockList(ctx));
  }

  public void exitParExpression(TomJavaParser.ParExpressionContext ctx) {
    setValue("exitParExpression", ctx, buildBlockList(ctx));
  }

  public void exitExpressionList(TomJavaParser.ExpressionListContext ctx) {
    setValue("exitExpressionList", ctx, buildBlockList(ctx));
  }

  public void exitExpression(TomJavaParser.ExpressionContext ctx) {
    setValue("exitExpression", ctx, buildBlockList(ctx));
  }
  
  public void exitFunTerm(TomJavaParser.FunTermContext ctx) {
    setValue("exitFunTerm", ctx, buildBlockList(ctx));
  }

  public void exitLambdaExpression(TomJavaParser.LambdaExpressionContext ctx) {
    setValue("exitLambdaExpression", ctx, buildBlockList(ctx));
  }

  public void exitLambdaParameters(TomJavaParser.LambdaParametersContext ctx) {
    setValue("exitLambdaParameters", ctx, buildBlockList(ctx));
  }

  public void exitLambdaBody(TomJavaParser.LambdaBodyContext ctx) {
    setValue("exitLambdaBody", ctx, buildBlockList(ctx));
  }

  public void exitPrimary(TomJavaParser.PrimaryContext ctx) {
    setValue("exitPrimary", ctx, buildBlockList(ctx));
  }

  public void exitClassType(TomJavaParser.ClassTypeContext ctx) {
    setValue("exitClassType", ctx, buildBlockList(ctx));
  }

  public void exitCreator(TomJavaParser.CreatorContext ctx) {
    setValue("exitCreator", ctx, buildBlockList(ctx));
  }

  public void exitCreatedName(TomJavaParser.CreatedNameContext ctx) {
    setValue("exitCreatedName", ctx, buildBlockList(ctx));
  }

  public void exitInnerCreator(TomJavaParser.InnerCreatorContext ctx) {
    setValue("exitInnerCreator", ctx, buildBlockList(ctx));
  }

  public void exitArrayCreatorRest(TomJavaParser.ArrayCreatorRestContext ctx) {
    setValue("exitArrayCreatorRest", ctx, buildBlockList(ctx));
  }

  public void exitClassCreatorRest(TomJavaParser.ClassCreatorRestContext ctx) {
    setValue("exitClassCreatorRest", ctx, buildBlockList(ctx));
  }

  public void exitExplicitGenericInvocation(TomJavaParser.ExplicitGenericInvocationContext ctx) {
    setValue("exitExplicitGenericInvocation", ctx, buildBlockList(ctx));
  }

  public void exitTypeArgumentsOrDiamond(TomJavaParser.TypeArgumentsOrDiamondContext ctx) {
    setValue("exitTypeArgumentsOrDiamond", ctx, buildBlockList(ctx));
  }

  public void exitNonWildcardTypeArgumentsOrDiamond(TomJavaParser.NonWildcardTypeArgumentsOrDiamondContext ctx) {
    setValue("exitNonWildcardTypeArgumentsOrDiamond", ctx, buildBlockList(ctx));
  }

  public void exitNonWildcardTypeArguments(TomJavaParser.NonWildcardTypeArgumentsContext ctx) {
    setValue("exitNonWildcardTypeArguments", ctx, buildBlockList(ctx));
  }

  public void exitTypeList(TomJavaParser.TypeListContext ctx) {
    setValue("exitTypeList", ctx, buildBlockList(ctx));
  }

  public void exitTypeType(TomJavaParser.TypeTypeContext ctx) {
    setValue("exitTypeType", ctx, buildBlockList(ctx));
  }

  public void exitPrimitiveType(TomJavaParser.PrimitiveTypeContext ctx) {
    setValue("exitPrimitiveType", ctx, buildBlockList(ctx));
  }

  public void exitTypeArguments(TomJavaParser.TypeArgumentsContext ctx) {
    setValue("exitTypeArguments", ctx, buildBlockList(ctx));
  }

  public void exitSuperSuffix(TomJavaParser.SuperSuffixContext ctx) {
    setValue("exitSuperSuffix", ctx, buildBlockList(ctx));
  }

  public void exitExplicitGenericInvocationSuffix(TomJavaParser.ExplicitGenericInvocationSuffixContext ctx) {
    setValue("exitExplicitGenericInvocationSuffix", ctx, buildBlockList(ctx));
  }

  public void exitArguments(TomJavaParser.ArgumentsContext ctx) {
    setValue("exitArguments", ctx, buildBlockList(ctx));
  }

//************************************TOM**************************************

  /*
   * tomDeclaration
   *  : strategyStatement
   *  | includeStatement
   *  | gomStatement
   *  | ruleStatement
   *  | typeterm
   *  | operator
   *  | oplist
   *  | oparray
   *  ;
   */
  public void exitTomDeclaration(TomJavaParser.TomDeclarationContext ctx) {
    setValue("exitTomDeclaration", ctx, getValue(ctx.getChild(0)));
  }
  
  /*
   * tomStatement
   *  : tomDeclaration
   *  | matchStatement
   *  ;
   */
  public void exitTomStatement(TomJavaParser.TomStatementContext ctx) {
    setValue("exitTomStatement", ctx, getValue(ctx.getChild(0)));
  }
  
  /*
   * tomTerm
   *  : bqcomposite
   *  | metaquote
   *  ;
   */
  public void exitTomTerm(TomJavaParser.TomTermContext ctx) {
    setValue("exitTomTerm", ctx, getValue(ctx.getChild(0)));
  }

  /* 
   * metaquote
   *   : METAQUOTE
   *   ;
   */
  public void exitMetaquote(TomJavaParser.MetaquoteContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    String code = ctx.METAQUOTE().getText();
    CstBlockList bl = `ConcCstBlock(HOSTBLOCK(optionList, code));

    setValue("exitMetaquote", ctx,`Cst_Metaquote(optionList,bl));
  }

  /*
   * matchStatement
   *   : MATCH (LPAREN (bqterm (COMMA bqterm)*)? RPAREN)? LBRACE actionRule* RBRACE 
   *   ;
   */
  public void exitMatchStatement(TomJavaParser.MatchStatementContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBQTermList subjectList = buildCstBQTermList(ctx.bqterm());
    CstConstraintActionList constraintActionList = buildCstConstraintActionList(ctx.actionRule());
    CstBlock res = `Cst_MatchConstruct(optionList,subjectList,constraintActionList);
    setValue("exitMatchStatement", ctx,res);
  }

  /*
   * strategyStatement
   *   : STRATEGY tomIdentifier LPAREN slotList? RPAREN EXTENDS bqterm LBRACE visit* RBRACE
   *   ;
   */
  public void exitStrategyStatement(TomJavaParser.StrategyStatementContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstName name = `Cst_Name(ctx.tomIdentifier().getText());
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
   *   : INCLUDE LBRACE (DOT* SLASH)? tomIdentifier ((DOT|SLASH|BACKSLASH) tomIdentifier)*  RBRACE 
   *   ;
   */
  public void exitIncludeStatement(TomJavaParser.IncludeStatementContext ctx) {
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
   *   : GOM gomOptions? unknownBlock
   *   ;
   */
  public void exitGomStatement(TomJavaParser.GomStatementContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBlock block = (CstBlock) getValue(ctx.unknownBlock());
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
   * ruleStatement
   *  : RULE unknownBlock
   *  ;
   */
  public void exitRuleStatement(TomJavaParser.RuleStatementContext ctx) {
    
  }
  
  /*
   * unknownBlock
   *  : LBRACE (tomBlock | unknownBlock | .)*? RBRACE
   *  ;
   */
  public void exitUnknownBlock(TomJavaParser.UnknownBlockContext ctx) {
    CstBlockList bl = `ConcCstBlock();

    for(int i = 0 ; i<ctx.getChildCount() ; i++) {
      ParseTree child = ctx.getChild(i);

      if(child instanceof TomJavaParser.UnknownBlockContext) {
        bl = `ConcCstBlock((CstBlock)getValue(child),bl*);
      } else if(child instanceof TomJavaParser.TomBlockContext) {
        bl = `ConcCstBlock((CstBlock)getValue(child),bl*);
      } else {
        //ParserRuleContext prc = (ParserRuleContext)child;
        //CstOption ot = extractOption(prc.getStart());
        //bl = `ConcCstBlock(bl*,HOSTBLOCK(ConcCstOption(ot), getStringValue(child)));
        bl = `ConcCstBlock(buildHostblock((Token)child.getPayload()),bl*);
      }
    }

    CstOption otext  = extractText(ctx);
    setValue(ctx,`Cst_UnamedBlock(ConcCstOption(otext),bl.reverse()));
  }

  /*
   * gomOptions
   *   : OPTIONSTART DMINUSID (COMMA DMINUSID)* OPTIONEND
   *   ;
   */
  public void exitGomOptions(TomJavaParser.GomOptionsContext ctx) {
    CstNameList nameList = `ConcCstName();
    for(TerminalNode e:ctx.DMINUSID()) {
      nameList = `ConcCstName(nameList*, Cst_Name(e.getText()));
    }
    setValue("exitGomOptions", ctx, nameList);
  }

  /*
   * visit
   *   : VISIT tomIdentifier LBRACE actionRule* RBRACE
   *   ;
   */
  public void exitVisit(TomJavaParser.VisitContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstConstraintActionList l = buildCstConstraintActionList(ctx.actionRule());
    CstVisit res = `Cst_VisitTerm( Cst_Type(ctx.tomIdentifier().getText()), l, optionList);
    setValue("exitVisit", ctx,res);
  }

  /*
   * actionRule
   *   : patternlist ((AND | OR) constraint)? ARROW tomBlock
   *   | patternlist ((AND | OR) constraint)? ARROW bqterm
   *   | c=constraint ARROW tomBlock
   *   | c=constraint ARROW bqterm
   *   ;
   */
  public void exitActionRule(TomJavaParser.ActionRuleContext ctx) {
    CstConstraintAction res = null;
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBlockList action = null;
    if(ctx.tomBlock() != null) {
      action = getBlockListFromBlock(ctx.tomBlock());
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
   * tomBlock 
   *   : LBRACE (tomBlock | blockStatement)*? RBRACE
   *   ;
   */
  public void exitTomBlock(TomJavaParser.TomBlockContext ctx) {
    CstBlockList blocks = `ConcCstBlock();
    CstBlock current = null;

    for(int i = 1; i < ctx.getChildCount() - 1; i++) {
      ParserRuleContext child = (ParserRuleContext) ctx.getChild(i);

      if(child instanceof TomJavaParser.TomBlockContext) {
        if(current != null) {
          blocks = `ConcCstBlock(current,blocks*);
          current = null;
        }
        blocks = `ConcCstBlock((CstBlock)getValue(child),blocks*);
      } else {
        for(CstBlock block:((CstBlockList)getValue(child)).getCollectionConcCstBlock()) {
          boolean isHostBlock = false;

          %match(block) {
            HOSTBLOCK(_, _) -> {
              current = merge(current, block);
              isHostBlock = true;
            }
          }

          if(!isHostBlock) {
            if(current != null) {
              blocks = `ConcCstBlock(current,blocks*);
              current = null;
            }
            blocks = `ConcCstBlock(block,blocks*);
          }
        }
      }
    }
    if (current != null)
    {
      blocks = `ConcCstBlock(current,blocks*);
    }

    CstOption otext  = extractText(ctx);
    setValue(ctx,`Cst_UnamedBlock(ConcCstOption(otext),blocks.reverse()));
  }

  /*
   * slotList
   *   : slot (COMMA slot)*
   *   ;
   */
  public void exitSlotList(TomJavaParser.SlotListContext ctx) {
    CstSlotList res = buildCstSlotList(ctx.slot());
    setValue("exitSlotList", ctx,res);
  }

  /*
   * slot
   *   : id1=tomIdentifier COLON? id2=tomIdentifier
   *   ;
   */
  public void exitSlot(TomJavaParser.SlotContext ctx) {
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
   *   : pattern (COMMA pattern)* (WHEN term (COMMA term)*)?
   *   ;
   */
  public void exitPatternlist(TomJavaParser.PatternlistContext ctx) {
    CstPatternList res = buildCstPatternList(ctx.pattern());
    setValue("exitPatternList", ctx,res);
  }

  /*
   * constraint
   *   : constraint AND constraint
   *   | constraint OR constraint
   *   | pattern match_symbol='<' '<' bqterm
   *   | term GT term
   *   | term GE term
   *   | term LT term
   *   | term LE term
   *   | term EQUAL term
   *   | term NOTEQUAL term
   *   | LPAREN c=constraint RPAREN
   *   ;
   */
  public void exitConstraint(TomJavaParser.ConstraintContext ctx) {
    CstConstraint res = null;
    if(ctx.AND() != null || ctx.OR() != null) {
      CstConstraint lhs = (CstConstraint)getValue(ctx.constraint(0));
      CstConstraint rhs = (CstConstraint)getValue(ctx.constraint(1));
      res = (ctx.AND() != null)?`Cst_AndConstraint(lhs,rhs):`Cst_OrConstraint(lhs,rhs);
    } else if(ctx.match_symbol != null) {
      CstPattern lhs = (CstPattern)getValue(ctx.pattern());
      CstBQTerm rhs = (CstBQTerm)getValue(ctx.bqterm());
      CstType rhs_type = (CstType)getValue2(ctx.bqterm());
      res = `Cst_MatchTermConstraint(lhs,rhs,rhs_type);
    } else if(ctx.LPAREN() != null && ctx.RPAREN() != null) {
      res = (CstConstraint)getValue(ctx.c);
    } else {
      CstTerm lhs = (CstTerm)getValue(ctx.term(0));
      CstTerm rhs = (CstTerm)getValue(ctx.term(1));
      if(ctx.GT() != null) { res = `Cst_NumGreaterThan(lhs,rhs); }
      else if(ctx.GE() != null) { res = `Cst_NumGreaterOrEqualThan(lhs,rhs); }
      else if(ctx.LT() != null) { res = `Cst_NumLessThan(lhs,rhs); }
      else if(ctx.LE() != null) { res = `Cst_NumLessOrEqualThan(lhs,rhs); }
      else if(ctx.EQUAL() != null) { res = `Cst_EqualTo(lhs,rhs); }
      else if(ctx.NOTEQUAL() != null) { res = `Cst_Different(lhs,rhs); }
    }

    setValue("exitConstraint",ctx,res);
  }

  /*
   * term
   *   : var=tomIdentifier STAR?
   *   | fsym=tomIdentifier LPAREN (term (COMMA term)*)? RPAREN 
   *   | constant
   *   ;
   */
  public void exitTerm(TomJavaParser.TermContext ctx) {
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
   *   : codomain=tomIdentifier? BQUOTE? fsym=tomIdentifier LPAREN (bqterm (COMMA bqterm)*)? RPAREN
   *   | codomain=tomIdentifier? BQUOTE? fsym=tomIdentifier LBRACK (pairSlotBqterm (COMMA pairSlotBqterm)*)? RBRACK 
   *   | codomain=tomIdentifier? BQUOTE? var=tomIdentifier STAR?
   *   | codomain=tomIdentifier? constant
   *   | UNDERSCORE
   *   ;
   */
  public void exitBqterm(TomJavaParser.BqtermContext ctx) {
    CstBQTerm res = null;
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstType type = (ctx.codomain != null)?`Cst_Type(ctx.codomain.getText()):`Cst_TypeUnknown();

    if(ctx.fsym != null && ctx.LPAREN() != null) {
      CstBQTermList args = buildCstBQTermList(ctx.bqterm());
      res = `Cst_BQAppl(optionList,ctx.fsym.getText(),args);
    } else if(ctx.fsym != null && ctx.LBRACK() != null) {
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
   *   : tomIdentifier EQUAL bqterm
   *   ;
   */
  public void exitPairSlotBqterm(TomJavaParser.PairSlotBqtermContext ctx) {
    CstPairSlotBQTerm res = null;
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstName slotName = `Cst_Name(ctx.tomIdentifier().getText());
    CstBQTerm bqterm = (CstBQTerm) getValue(ctx.bqterm());
    res = `Cst_PairSlotBQTerm(optionList,slotName,bqterm);
    setValue("exitPairSlotBqterm",ctx,res);
  }

  /*
   * bqcomposite
   *   : BQUOTE fsym=tomIdentifier LBRACK (pairSlotBqterm (COMMA pairSlotBqterm)*)? RBRACK 
   *   | BQUOTE fsym=tomIdentifier LPAREN (composite (COMMA composite)*)? RPAREN
   *   | BQUOTE LPAREN sub=composite RPAREN
   *   | BQUOTE var=tomIdentifier STAR?
   *   ;
   */
  public void exitBqcomposite(TomJavaParser.BqcompositeContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBlock res = null;
    if(ctx.fsym != null && ctx.LBRACK() != null) {
      CstPairSlotBQTermList args = buildCstPairSlotBQTermList(ctx.pairSlotBqterm());
      res = `Cst_BQTermToBlock(Cst_BQRecordAppl(optionList,ctx.fsym.getText(),args));
    } else if(ctx.fsym != null && ctx.LPAREN() != null) {
      CstBQTermList args = buildCstBQTermList(ctx.composite());
      res = `Cst_BQTermToBlock(Cst_BQAppl(optionList,ctx.fsym.getText(),args));
    } else if(ctx.var != null && ctx.STAR() == null) {
      res = `Cst_BQTermToBlock(Cst_BQVar(optionList,ctx.var.getText(),Cst_TypeUnknown()));
    } else if(ctx.var != null && ctx.STAR() != null) {
      res = `Cst_BQTermToBlock(Cst_BQVarStar(optionList,ctx.var.getText(),Cst_TypeUnknown()));
    } else if (ctx.sub != null){
      res = `Cst_BQTermToBlock((CstBQTerm)getValue(ctx.sub));
    }
    setValue("exitBqcomposite",ctx,res);
  }

  /*
   * composite
   *   : fsym=composite LPAREN (args+=composite (COMMA args+=composite)*)? RPAREN
   *   | LPAREN sub=composite RPAREN
   *   | var=tomIdentifier STAR?
   *   | constant
   *   | UNDERSCORE
   *   | [...]
   *   ;
   */
  public void exitComposite(TomJavaParser.CompositeContext ctx) {
    CstOptionList optionList = `ConcCstOption(extractOption(ctx.getStart()));
    CstBQTerm res = null;
    CstType type = `Cst_TypeUnknown();

    if(ctx.fsym != null) {
      CstBQTerm fsym = (CstBQTerm)getValue(ctx.fsym);
      CstBQTermList args = buildCstBQTermList(ctx.args);
      %match(fsym) {
        Cst_BQVar(varOptionList,var,_) -> {
          res = `Cst_BQAppl(varOptionList,var,args);
        }
      }
      if(res == null) {
        CstOptionList optionList1 = `ConcCstOption(extractOption(ctx.LPAREN().getSymbol()));
        CstOptionList optionList2 = `ConcCstOption(extractOption(ctx.RPAREN().getSymbol()));
        
        res = `Cst_BQComposite(optionList, ConcCstBQTerm(
            fsym,
            Cst_ITL(optionList1,ctx.LPAREN().getText()),
            args*,
            Cst_ITL(optionList2,ctx.RPAREN().getText())
            ));
      }
    } else if(ctx.sub != null) {
      CstOptionList optionList1 = `ConcCstOption(extractOption(ctx.LPAREN().getSymbol()));
      CstOptionList optionList2 = `ConcCstOption(extractOption(ctx.RPAREN().getSymbol()));
      CstBQTerm sub = (CstBQTerm)getValue(ctx.sub);

      res = `Cst_BQComposite(optionList, ConcCstBQTerm(
          Cst_ITL(optionList1,ctx.LPAREN().getText()),
          sub,
          Cst_ITL(optionList2,ctx.RPAREN().getText())
          ));
    } else if(ctx.var != null && ctx.STAR() == null) {
      res = `Cst_BQVar(optionList,ctx.var.getText(),type);
    } else if(ctx.var != null && ctx.STAR() != null) {
      res = `Cst_BQVarStar(optionList,ctx.var.getText(),type);
    } else if(ctx.constant() != null) {
      CstSymbol cst = (CstSymbol) getValue(ctx.constant());
      res = `Cst_BQConstant(optionList,cst.getvalue());
    } else if(ctx.UNDERSCORE() != null) {
      res = `Cst_BQUnderscore();
    } else {
      CstBQTermList bqList = `ConcCstBQTerm();

      for(int i = 0; i < ctx.getChildCount(); i++) {
        if(ctx.getChild(i) instanceof TomJavaParser.CompositeContext) {
          CstBQTerm child = (CstBQTerm)getValue(ctx.getChild(i));
          bqList = `ConcCstBQTerm(bqList*, child);
        } else if(ctx.getChild(i).getPayload() instanceof Token) {
          Token token = (Token) ctx.getChild(i).getPayload();
          CstOptionList tokenOptionList = `ConcCstOption(extractOption(token));
          bqList = `ConcCstBQTerm(bqList*, Cst_ITL(tokenOptionList, token.getText()));
        } else {
          ParserRuleContext child = (ParserRuleContext) ctx.getChild(i);
          CstOptionList childOptionList = `ConcCstOption(extractOption(child.getStart()));
          bqList = `ConcCstBQTerm(bqList*, Cst_ITL(childOptionList, child.getText()));
        }
      }
      
      res = `Cst_BQComposite(optionList, bqList*);
    }

    setValue("exitComposite",ctx,res);
  }

  /*
   * pattern
   *   : tomIdentifier AT pattern 
   *   | ANTI pattern
   *   | fsymbol explicitArgs
   *   | fsymbol implicitArgs
   *   | var=tomIdentifier STAR?
   *   | UNDERSCORE STAR?
   *   | constant (PIPE constant)*
   *   ;
   */
  public void exitPattern(TomJavaParser.PatternContext ctx) {
    CstPattern res = null;
    if(ctx.AT() != null) {
      res = `Cst_AnnotatedPattern((CstPattern)getValue(ctx.pattern()), ctx.tomIdentifier().getText());
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
  public void exitFsymbol(TomJavaParser.FsymbolContext ctx) {
    CstSymbolList res = buildCstSymbolList(ctx.headSymbol());
    setValue("exitFsymbol",ctx,res);
  }

  /*
   * headSymbol
   *   : tomIdentifier QMARK?
   *   | tomIdentifier DQMARK?
   *   | constant
   *   ;
   */
  public void exitHeadSymbol(TomJavaParser.HeadSymbolContext ctx) {
    CstSymbol res = null;
    if(ctx.QMARK() != null) {
      res = `Cst_Symbol(ctx.tomIdentifier().getText(), Cst_TheoryAU());
    } else if(ctx.DQMARK() != null) {
      res = `Cst_Symbol(ctx.tomIdentifier().getText(), Cst_TheoryAC());
    } else if(ctx.tomIdentifier() != null) {
      res = `Cst_Symbol(ctx.tomIdentifier().getText(), Cst_TheoryDEFAULT());
    } else if(ctx.constant() != null) {
      res = (CstSymbol) getValue(ctx.constant());
    } 
    setValue("exitHeadSymbol",ctx,res);
  }

  /*
   * constant
   *  : SUB? DECIMAL_LITERAL
   *  | SUB? FLOAT_LITERAL
   *  | CHAR_LITERAL
   *  | EXTENDED_CHAR_LITERAL
   *  | STRING_LITERAL
   *  ;
   */
  public void exitConstant(TomJavaParser.ConstantContext ctx) {
    CstSymbol res = null;
    if(ctx.DECIMAL_LITERAL() != null) {
      if(ctx.DECIMAL_LITERAL().getText().toLowerCase().endsWith("l")) {
        res = `Cst_SymbolLong(((ctx.SUB() == null)?"":"-") + ctx.DECIMAL_LITERAL().getText());
      } else {
        res = `Cst_SymbolInt(((ctx.SUB() == null)?"":"-") + ctx.DECIMAL_LITERAL().getText());
      }
    } else if(ctx.FLOAT_LITERAL() != null) {
      res = `Cst_SymbolDouble(((ctx.SUB() == null)?"":"-") + ctx.FLOAT_LITERAL().getText());
    } else if(ctx.CHAR_LITERAL() != null) {
      res = `Cst_SymbolChar(ctx.CHAR_LITERAL().getText());
    } else if(ctx.EXTENDED_CHAR_LITERAL() != null) {
      res = `Cst_SymbolChar(ctx.EXTENDED_CHAR_LITERAL().getText());
    } else if(ctx.STRING_LITERAL() != null) {
      res = `Cst_SymbolString(ctx.STRING_LITERAL().getText());
    }
    setValue("exitConstant",ctx,res);
  }

  /*
   * explicitArgs
   *   : LPAREN (pattern (COMMA pattern)*)? RPAREN
   *   ;
   */
  public void exitExplicitArgs(TomJavaParser.ExplicitArgsContext ctx) {
    int n = ctx.pattern().size();
    CstPatternList res = `ConcCstPattern();
    for(int i=0 ; i<n ; i++) {
      res = `ConcCstPattern(res*, (CstPattern)getValue(ctx.pattern(i)));
    }
    setValue("exitExplicitArgs",ctx,res);
  }

  /*
   * implicitArgs
   *   : LSQUAREBR (tomIdentifier ASSIGN pattern (COMMA tomIdentifier ASSIGN pattern)*)? RSQUAREBR 
   *   ;
   */
  public void exitImplicitArgs(TomJavaParser.ImplicitArgsContext ctx) {
    int n = ctx.tomIdentifier().size();
    CstPairPatternList res = `ConcCstPairPattern();
    for(int i=0 ; i<n ; i++) {
      res = `ConcCstPairPattern(res*, Cst_PairPattern(ctx.tomIdentifier(i).getText(), (CstPattern)getValue(ctx.pattern(i))));
    }
    setValue("exitImplicitArgs",ctx,res);
  }

  /*
   * typeterm
   *   : TYPETERM type=tomIdentifier (EXTENDS supertype=tomIdentifier)? LBRACE 
   *     implement isSort? equalsTerm?
   *     RBRACE
   *   ;
   */
  public void exitTypeterm(TomJavaParser.TypetermContext ctx) {
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
   *   : OP codomain=tomIdentifier opname=tomIdentifier LPAREN slotList? RPAREN LBRACE 
   *     (isFsym | make | getSlot | getDefault)*
   *     RBRACE
   *   ;
   */
  public void exitOperator(TomJavaParser.OperatorContext ctx) {
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
   *   : OPLIST codomain=tomIdentifier opname=tomIdentifier LPAREN domain=tomIdentifier STAR RPAREN LBRACE 
   *     (isFsym | makeEmptyList | makeInsertList | getHead | getTail | isEmptyList)*
   *     RBRACE
   *   ;
   */
  public void exitOplist(TomJavaParser.OplistContext ctx) {
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
   *   : OPARRAY codomain=tomIdentifier opname=tomIdentifier LPAREN domain=tomIdentifier STAR RPAREN LBRACE 
   *     (isFsym | makeEmptyArray | makeAppendArray | getElement | getSize)*
   *     RBRACE
   *   ;
   */
  public void exitOparray(TomJavaParser.OparrayContext ctx) {
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
   * termBlock 
   *   : LBRACE expression RBRACE
   *   ;
   */
  public void exitTermBlock(TomJavaParser.TermBlockContext ctx) {
    CstBlockList blocks = `ConcCstBlock();
    CstBlock current = null;

    for(CstBlock block:((CstBlockList)getValue(ctx.expression())).getCollectionConcCstBlock()) {
      boolean isHostBlock = false;

      %match(block) {
        HOSTBLOCK(_, _) -> {
          current = merge(current, block);
          isHostBlock = true;
        }
      }

      if(!isHostBlock) {
        if(current != null) {
          blocks = `ConcCstBlock(current,blocks*);
          current = null;
        }
        blocks = `ConcCstBlock(block,blocks*);
      }
    }
    if (current != null)
    {
      blocks = `ConcCstBlock(current,blocks*);
    }

    CstOption otext  = extractText(ctx);
    setValue(ctx,`Cst_UnamedBlock(ConcCstOption(otext),blocks.reverse()));
  }

  /*
   * implement
   *   : IMPLEMENT LBRACE typeType RBRACE
   *   ;
   */
  public void exitImplement(TomJavaParser.ImplementContext ctx) {
    setValue("exitImplement", ctx,
        `Cst_Implement((CstBlockList)getValue(ctx.typeType())));
  }

  /*
   * equalsTerm
   *   : EQUALS LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitEqualsTerm(TomJavaParser.EqualsTermContext ctx) {
    setValue("exitEquals", ctx,
        `Cst_Equals(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * isSort
   *   : IS_SORT LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitIsSort(TomJavaParser.IsSortContext ctx) {
    setValue("exitIsSort", ctx,
        `Cst_IsSort(Cst_Name(ctx.tomIdentifier().getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * isFsym
   *   : IS_FSYM LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitIsFsym(TomJavaParser.IsFsymContext ctx) {
    setValue("exitIsFsym", ctx,
        `Cst_IsFsym(Cst_Name(ctx.tomIdentifier().getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * make
   *   : MAKE LPAREN (tomIdentifier (COMMA tomIdentifier)*)? RPAREN termBlock
   *   ;
   */
  public void exitMake(TomJavaParser.MakeContext ctx) {
    CstNameList nameList = `ConcCstName();
    for(TomJavaParser.TomIdentifierContext e:ctx.tomIdentifier()) {
      nameList = `ConcCstName(nameList*, Cst_Name(e.getText()));
    }
    setValue("exitMake", ctx,
        `Cst_Make(nameList, getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * makeEmptyList
   *   : MAKE_EMPTY LPAREN RPAREN termBlock
   *   ;
   */
  public void exitMakeEmptyList(TomJavaParser.MakeEmptyListContext ctx) {
    setValue("exitMakeEmptyList", ctx,
        `Cst_MakeEmptyList(getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * makeEmptyArray
   *   : MAKE_EMPTY LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitMakeEmptyArray(TomJavaParser.MakeEmptyArrayContext ctx) {
    setValue("exitMakeEmptyArray", ctx,
        `Cst_MakeEmptyArray(Cst_Name(ctx.tomIdentifier().getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * makeAppendArray
   *   : MAKE_APPEND LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitMakeAppendArray(TomJavaParser.MakeAppendArrayContext ctx) {
    setValue("exitMakeAppendArray", ctx,
        `Cst_MakeAppend(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * makeInsertList
   *   : MAKE_INSERT LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitMakeInsertList(TomJavaParser.MakeInsertListContext ctx) {
    setValue("exitMakeInsertList", ctx,
        `Cst_MakeInsert(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * getSlot
   *   : GET_SLOT LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetSlot(TomJavaParser.GetSlotContext ctx) {
    setValue("exitGetSlot", ctx,
        `Cst_GetSlot(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * getHead
   *   : GET_HEAD LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetHead(TomJavaParser.GetHeadContext ctx) {
    setValue("exitGetHead", ctx,
        `Cst_GetHead(Cst_Name(ctx.tomIdentifier().getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * getTail
   *   : GET_TAIL LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetTail(TomJavaParser.GetTailContext ctx) {
    setValue("exitGetTail", ctx,
        `Cst_GetTail(Cst_Name(ctx.tomIdentifier().getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * getElement
   *   : GET_ELEMENT LPAREN id1=tomIdentifier COMMA id2=tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetElement(TomJavaParser.GetElementContext ctx) {
    setValue("exitGetElement", ctx,
        `Cst_GetElement(Cst_Name(ctx.id1.getText()), Cst_Name(ctx.id2.getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * isEmptyList
   *   : IS_EMPTY LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitIsEmptyList(TomJavaParser.IsEmptyListContext ctx) {
    setValue("exitIsEmptyList", ctx,
        `Cst_IsEmpty(Cst_Name(ctx.tomIdentifier().getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * getSize
   *   : GET_SIZE LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetSize(TomJavaParser.GetSizeContext ctx) {
    setValue("exitGetSize", ctx,
        `Cst_GetSize(Cst_Name(ctx.tomIdentifier().getText()), getBlockListFromBlock(ctx.termBlock())));
  }

  /*
   * getDefault
   *   : GET_DEFAULT LPAREN tomIdentifier RPAREN termBlock
   *   ;
   */
  public void exitGetDefault(TomJavaParser.GetDefaultContext ctx) {
    setValue("exitGetDefault", ctx,
        `Cst_GetDefault(Cst_Name(ctx.tomIdentifier().getText()), getBlockListFromBlock(ctx.termBlock())));
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
  
  private CstBlockList buildBlockList(ParserRuleContext ctx) {
    CstBlockList blocks = `ConcCstBlock();
    CstBlock current = null;
    
    for(int i = 0; i < ctx.getChildCount(); i++) {
      if(ctx.getChild(i).getPayload() instanceof Token) {
        if(!ctx.getChild(i).getText().equals("<EOF>")) {
          Token token = (Token) ctx.getChild(i).getPayload();
          current = merge(current, token);
        }
      } else {
        ParserRuleContext child = (ParserRuleContext) ctx.getChild(i);
        
        if(child instanceof TomJavaParser.JavaIdentifierContext) {
          current = merge(current, child.getStart());
        } else if(isTom(child)) {
          if(current != null) {
            blocks = `ConcCstBlock(current,blocks*);
            current = null;
          }
          blocks = `ConcCstBlock((CstBlock)getValue(child),blocks*);
        } else {
          for(CstBlock block:((CstBlockList)getValue(child)).getCollectionConcCstBlock()) {
            boolean isHostBlock = false;
            
            %match(block) {
              HOSTBLOCK(_, _) -> {
                current = merge(current, block);
                isHostBlock = true;
              }
            }
            
            if(!isHostBlock) {
              if(current != null) {
                blocks = `ConcCstBlock(current,blocks*);
                current = null;
              }
              blocks = `ConcCstBlock(block,blocks*);
            }
          }
        }
      }
    }
    if (current != null)
    {
      blocks = `ConcCstBlock(current,blocks*);
    }
    
    return blocks.reverse();
  }
  
  private CstBlock merge(CstBlock hostblock, Token token)
  {
    CstBlock block = buildHostblock(token);
    return merge(hostblock, block);
  }
  
  private CstBlock merge(CstBlock firstBlock, CstBlock lastBlock)
  {
    if(firstBlock == null)
    {
      return lastBlock;
    } else {
      String merge = new String();
      CstOption ot = null;
      %match(firstBlock, lastBlock) {
        HOSTBLOCK(ConcCstOption(Cst_OriginTracking(source, firstLine, firstColumn, _, _)), code1),
        HOSTBLOCK(ConcCstOption(Cst_OriginTracking(source, _, _, lastLine, lastColumn)), code2) -> {
          merge = `code1 + `code2;
          ot = `Cst_OriginTracking(source, firstLine, firstColumn, lastLine, lastColumn);
        }
      }
      return `HOSTBLOCK(ConcCstOption(ot), merge);
    }
  }
  
  private boolean isTom(Object o) {
    return o instanceof TomJavaParser.TomDeclarationContext
        || o instanceof TomJavaParser.TomStatementContext
        || o instanceof TomJavaParser.TomTermContext;
  }

  /*
   * given a context corresponding to water token
   * search spaces and layouts in hidden channels to
   * build a HOSTBLOCK with spaces and layout
   */
  private CstBlock buildHostblock(Token token) {
    String s = token.getText();
    
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
    int c = t1.getCharPositionInLine()+1;
    String res = "";
    while(l < t2.getLine()) {
      res += newline;
      l++;
      c = 1;
    }
    while(c < t2.getCharPositionInLine()) {
      res += " ";
      c++;
    }
    return res;
  }
}

