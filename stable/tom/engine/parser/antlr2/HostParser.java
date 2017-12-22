// $ANTLR 2.7.7 (20060906): "HostLanguage.g" -> "HostParser.java"$




package tom.engine.parser.antlr2;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

import java.util.*;
import java.util.logging.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;



import tom.engine.Tom;
import tom.engine.TomStreamManager;
import tom.engine.TomMessage;
import tom.engine.exception.*;
import tom.engine.tools.SymbolTable;
import tom.engine.parser.TomParserTool;



import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;



import tom.engine.tools.ASTFactory;
import antlr.TokenStreamSelector;
import tom.platform.OptionManager;

public class HostParser extends antlr.LLkParser       implements HostParserTokenTypes
 {

  
    private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;}private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;}private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;}private static boolean tom_equal_term_TomSymbolTable(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomSymbolTable(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TomSymbolTable) ;}private static boolean tom_equal_term_TomSymbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomSymbol(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TomSymbol) ;}private static boolean tom_equal_term_ElementaryTransformation(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ElementaryTransformation(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.ElementaryTransformation) ;}private static boolean tom_equal_term_TomStructureTable(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomStructureTable(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TomStructureTable) ;}private static boolean tom_equal_term_TextPosition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TextPosition(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TextPosition) ;}private static boolean tom_equal_term_ResolveStratBlockList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ResolveStratBlockList(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.ResolveStratBlockList) ;}private static boolean tom_equal_term_ElementaryTransformationList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ElementaryTransformationList(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.ElementaryTransformationList) ;}private static boolean tom_equal_term_ResolveStratElement(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ResolveStratElement(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.ResolveStratElement) ;}private static boolean tom_equal_term_TransfoStratInfo(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TransfoStratInfo(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TransfoStratInfo) ;}private static boolean tom_equal_term_TomEntryList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomEntryList(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TomEntryList) ;}private static boolean tom_equal_term_TomVisitList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomVisitList(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TomVisitList) ;}private static boolean tom_equal_term_TomEntry(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomEntry(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TomEntry) ;}private static boolean tom_equal_term_TomSymbolList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomSymbolList(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TomSymbolList) ;}private static boolean tom_equal_term_ResolveStratElementList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ResolveStratElementList(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.ResolveStratElementList) ;}private static boolean tom_equal_term_TomVisit(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomVisit(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.TomVisit) ;}private static boolean tom_equal_term_ResolveStratBlock(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ResolveStratBlock(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.ResolveStratBlock) ;}private static boolean tom_equal_term_KeyEntry(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_KeyEntry(Object t) {return  (t instanceof tom.engine.adt.tomsignature.types.KeyEntry) ;}private static boolean tom_equal_term_TypeConstraint(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeConstraint(Object t) {return  (t instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ;}private static boolean tom_equal_term_TypeConstraintList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeConstraintList(Object t) {return  (t instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ;}private static boolean tom_equal_term_Info(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Info(Object t) {return  (t instanceof tom.engine.adt.typeconstraints.types.Info) ;}private static boolean tom_equal_term_Expression(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Expression(Object t) {return  (t instanceof tom.engine.adt.tomexpression.types.Expression) ;}private static boolean tom_equal_term_TomList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomList(Object t) {return  (t instanceof tom.engine.adt.tomterm.types.TomList) ;}private static boolean tom_equal_term_TomTerm(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomTerm(Object t) {return  (t instanceof tom.engine.adt.tomterm.types.TomTerm) ;}private static boolean tom_equal_term_Declaration(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Declaration(Object t) {return  (t instanceof tom.engine.adt.tomdeclaration.types.Declaration) ;}private static boolean tom_equal_term_DeclarationList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_DeclarationList(Object t) {return  (t instanceof tom.engine.adt.tomdeclaration.types.DeclarationList) ;}private static boolean tom_equal_term_TypeOption(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeOption(Object t) {return  (t instanceof tom.engine.adt.tomtype.types.TypeOption) ;}private static boolean tom_equal_term_TomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomType(Object t) {return  (t instanceof tom.engine.adt.tomtype.types.TomType) ;}private static boolean tom_equal_term_TomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomTypeList(Object t) {return  (t instanceof tom.engine.adt.tomtype.types.TomTypeList) ;}private static boolean tom_equal_term_TypeOptionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeOptionList(Object t) {return  (t instanceof tom.engine.adt.tomtype.types.TypeOptionList) ;}private static boolean tom_equal_term_TargetLanguageType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TargetLanguageType(Object t) {return  (t instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ;}private static boolean tom_equal_term_CodeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CodeList(Object t) {return  (t instanceof tom.engine.adt.code.types.CodeList) ;}private static boolean tom_equal_term_CompositeMember(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CompositeMember(Object t) {return  (t instanceof tom.engine.adt.code.types.CompositeMember) ;}private static boolean tom_equal_term_BQTermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_BQTermList(Object t) {return  (t instanceof tom.engine.adt.code.types.BQTermList) ;}private static boolean tom_equal_term_BQTerm(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_BQTerm(Object t) {return  (t instanceof tom.engine.adt.code.types.BQTerm) ;}private static boolean tom_equal_term_TargetLanguage(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TargetLanguage(Object t) {return  (t instanceof tom.engine.adt.code.types.TargetLanguage) ;}private static boolean tom_equal_term_Code(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Code(Object t) {return  (t instanceof tom.engine.adt.code.types.Code) ;}private static boolean tom_equal_term_RuleInstruction(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleInstruction(Object t) {return  (t instanceof tom.engine.adt.tominstruction.types.RuleInstruction) ;}private static boolean tom_equal_term_ConstraintInstruction(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ConstraintInstruction(Object t) {return  (t instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ;}private static boolean tom_equal_term_RefClassTracelinkInstruction(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RefClassTracelinkInstruction(Object t) {return  (t instanceof tom.engine.adt.tominstruction.types.RefClassTracelinkInstruction) ;}private static boolean tom_equal_term_RefClassTracelinkInstructionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RefClassTracelinkInstructionList(Object t) {return  (t instanceof tom.engine.adt.tominstruction.types.RefClassTracelinkInstructionList) ;}private static boolean tom_equal_term_RuleInstructionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleInstructionList(Object t) {return  (t instanceof tom.engine.adt.tominstruction.types.RuleInstructionList) ;}private static boolean tom_equal_term_Instruction(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Instruction(Object t) {return  (t instanceof tom.engine.adt.tominstruction.types.Instruction) ;}private static boolean tom_equal_term_InstructionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_InstructionList(Object t) {return  (t instanceof tom.engine.adt.tominstruction.types.InstructionList) ;}private static boolean tom_equal_term_ConstraintInstructionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ConstraintInstructionList(Object t) {return  (t instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ;}private static boolean tom_equal_term_CstBQTerm(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstBQTerm(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstBQTerm) ;}private static boolean tom_equal_term_CstConstraint(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstConstraint(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstConstraint) ;}private static boolean tom_equal_term_CstSlotList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstSlotList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstSlotList) ;}private static boolean tom_equal_term_CstName(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstName(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstName) ;}private static boolean tom_equal_term_CstBlockList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstBlockList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstBlockList) ;}private static boolean tom_equal_term_CstOperator(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstOperator(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstOperator) ;}private static boolean tom_equal_term_CstVisit(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstVisit(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstVisit) ;}private static boolean tom_equal_term_CstPairSlotBQTerm(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstPairSlotBQTerm(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstPairSlotBQTerm) ;}private static boolean tom_equal_term_CstType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstType(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstType) ;}private static boolean tom_equal_term_CstProgram(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstProgram(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstProgram) ;}private static boolean tom_equal_term_CstPairSlotBQTermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstPairSlotBQTermList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstPairSlotBQTermList) ;}private static boolean tom_equal_term_CstOptionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstOptionList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstOptionList) ;}private static boolean tom_equal_term_CstPatternList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstPatternList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstPatternList) ;}private static boolean tom_equal_term_CstNameList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstNameList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstNameList) ;}private static boolean tom_equal_term_CstOperatorList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstOperatorList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstOperatorList) ;}private static boolean tom_equal_term_CstTermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstTermList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstTermList) ;}private static boolean tom_equal_term_CstBQTermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstBQTermList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstBQTermList) ;}private static boolean tom_equal_term_CstPattern(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstPattern(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstPattern) ;}private static boolean tom_equal_term_CstConstraintActionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstConstraintActionList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstConstraintActionList) ;}private static boolean tom_equal_term_CstVisitList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstVisitList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstVisitList) ;}private static boolean tom_equal_term_CstOption(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstOption(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstOption) ;}private static boolean tom_equal_term_CstConstraintAction(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstConstraintAction(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstConstraintAction) ;}private static boolean tom_equal_term_CstConstraintList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstConstraintList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstConstraintList) ;}private static boolean tom_equal_term_CstTerm(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstTerm(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstTerm) ;}private static boolean tom_equal_term_CstPairPatternList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstPairPatternList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstPairPatternList) ;}private static boolean tom_equal_term_CstPairPattern(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstPairPattern(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstPairPattern) ;}private static boolean tom_equal_term_CstBlock(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstBlock(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstBlock) ;}private static boolean tom_equal_term_CstSlot(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstSlot(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstSlot) ;}private static boolean tom_equal_term_CstSymbolList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstSymbolList(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstSymbolList) ;}private static boolean tom_equal_term_CstTheory(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstTheory(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstTheory) ;}private static boolean tom_equal_term_CstSymbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CstSymbol(Object t) {return  (t instanceof tom.engine.adt.cst.types.CstSymbol) ;}private static boolean tom_equal_term_TomNumber(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomNumber(Object t) {return  (t instanceof tom.engine.adt.tomname.types.TomNumber) ;}private static boolean tom_equal_term_TomNameList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomNameList(Object t) {return  (t instanceof tom.engine.adt.tomname.types.TomNameList) ;}private static boolean tom_equal_term_TomNumberList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomNumberList(Object t) {return  (t instanceof tom.engine.adt.tomname.types.TomNumberList) ;}private static boolean tom_equal_term_TomName(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TomName(Object t) {return  (t instanceof tom.engine.adt.tomname.types.TomName) ;}private static boolean tom_equal_term_Slot(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Slot(Object t) {return  (t instanceof tom.engine.adt.tomslot.types.Slot) ;}private static boolean tom_equal_term_BQSlotList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_BQSlotList(Object t) {return  (t instanceof tom.engine.adt.tomslot.types.BQSlotList) ;}private static boolean tom_equal_term_SlotList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_SlotList(Object t) {return  (t instanceof tom.engine.adt.tomslot.types.SlotList) ;}private static boolean tom_equal_term_BQSlot(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_BQSlot(Object t) {return  (t instanceof tom.engine.adt.tomslot.types.BQSlot) ;}private static boolean tom_equal_term_PairNameDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_PairNameDecl(Object t) {return  (t instanceof tom.engine.adt.tomslot.types.PairNameDecl) ;}private static boolean tom_equal_term_PairNameDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_PairNameDeclList(Object t) {return  (t instanceof tom.engine.adt.tomslot.types.PairNameDeclList) ;}private static boolean tom_equal_term_OptionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_OptionList(Object t) {return  (t instanceof tom.engine.adt.tomoption.types.OptionList) ;}private static boolean tom_equal_term_Option(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Option(Object t) {return  (t instanceof tom.engine.adt.tomoption.types.Option) ;}private static boolean tom_equal_term_NumericConstraintType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_NumericConstraintType(Object t) {return  (t instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ;}private static boolean tom_equal_term_Constraint(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Constraint(Object t) {return  (t instanceof tom.engine.adt.tomconstraint.types.Constraint) ;}private static boolean tom_equal_term_ConstraintList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ConstraintList(Object t) {return  (t instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ;}private static boolean tom_equal_term_Theory(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Theory(Object t) {return  (t instanceof tom.engine.adt.theory.types.Theory) ;}private static boolean tom_equal_term_ElementaryTheory(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ElementaryTheory(Object t) {return  (t instanceof tom.engine.adt.theory.types.ElementaryTheory) ;}private static  tom.engine.adt.tomsignature.types.TextPosition  tom_make_TextPosition( int  t0,  int  t1) { return  tom.engine.adt.tomsignature.types.textposition.TextPosition.make(t0, t1) ;}private static  tom.engine.adt.code.types.TargetLanguage  tom_make_TL( String  t0,  tom.engine.adt.tomsignature.types.TextPosition  t1,  tom.engine.adt.tomsignature.types.TextPosition  t2) { return  tom.engine.adt.code.types.targetlanguage.TL.make(t0, t1, t2) ;}private static  tom.engine.adt.code.types.TargetLanguage  tom_make_ITL( String  t0) { return  tom.engine.adt.code.types.targetlanguage.ITL.make(t0) ;}private static  tom.engine.adt.code.types.Code  tom_make_TargetLanguageToCode( tom.engine.adt.code.types.TargetLanguage  t0) { return  tom.engine.adt.code.types.code.TargetLanguageToCode.make(t0) ;}private static  tom.engine.adt.code.types.Code  tom_make_InstructionToCode( tom.engine.adt.tominstruction.types.Instruction  t0) { return  tom.engine.adt.code.types.code.InstructionToCode.make(t0) ;}private static  tom.engine.adt.code.types.Code  tom_make_DeclarationToCode( tom.engine.adt.tomdeclaration.types.Declaration  t0) { return  tom.engine.adt.code.types.code.DeclarationToCode.make(t0) ;}private static  tom.engine.adt.code.types.Code  tom_make_BQTermToCode( tom.engine.adt.code.types.BQTerm  t0) { return  tom.engine.adt.code.types.code.BQTermToCode.make(t0) ;}private static boolean tom_is_fun_sym_Tom( tom.engine.adt.code.types.Code  t) {return  (t instanceof tom.engine.adt.code.types.code.Tom) ;}private static  tom.engine.adt.code.types.Code  tom_make_Tom( tom.engine.adt.code.types.CodeList  t0) { return  tom.engine.adt.code.types.code.Tom.make(t0) ;}private static  tom.engine.adt.code.types.CodeList  tom_get_slot_Tom_CodeList( tom.engine.adt.code.types.Code  t) {return  t.getCodeList() ;}private static  tom.engine.adt.code.types.Code  tom_make_TomInclude( tom.engine.adt.code.types.CodeList  t0) { return  tom.engine.adt.code.types.code.TomInclude.make(t0) ;}private static  tom.engine.adt.tomname.types.TomName  tom_make_Name( String  t0) { return  tom.engine.adt.tomname.types.tomname.Name.make(t0) ;}private static  tom.engine.adt.tomoption.types.Option  tom_make_OriginTracking( tom.engine.adt.tomname.types.TomName  t0,  int  t1,  String  t2) { return  tom.engine.adt.tomoption.types.option.OriginTracking.make(t0, t1, t2) ;}private static boolean tom_is_fun_sym_concCode( tom.engine.adt.code.types.CodeList  t) {return  ((t instanceof tom.engine.adt.code.types.codelist.ConsconcCode) || (t instanceof tom.engine.adt.code.types.codelist.EmptyconcCode)) ;}private static  tom.engine.adt.code.types.CodeList  tom_empty_list_concCode() { return  tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;}private static  tom.engine.adt.code.types.CodeList  tom_cons_list_concCode( tom.engine.adt.code.types.Code  e,  tom.engine.adt.code.types.CodeList  l) { return  tom.engine.adt.code.types.codelist.ConsconcCode.make(e,l) ;}private static  tom.engine.adt.code.types.Code  tom_get_head_concCode_CodeList( tom.engine.adt.code.types.CodeList  l) {return  l.getHeadconcCode() ;}private static  tom.engine.adt.code.types.CodeList  tom_get_tail_concCode_CodeList( tom.engine.adt.code.types.CodeList  l) {return  l.getTailconcCode() ;}private static boolean tom_is_empty_concCode_CodeList( tom.engine.adt.code.types.CodeList  l) {return  l.isEmptyconcCode() ;}   private static   tom.engine.adt.code.types.CodeList  tom_append_list_concCode( tom.engine.adt.code.types.CodeList l1,  tom.engine.adt.code.types.CodeList  l2) {     if( l1.isEmptyconcCode() ) {       return l2;     } else if( l2.isEmptyconcCode() ) {       return l1;     } else if(  l1.getTailconcCode() .isEmptyconcCode() ) {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,l2) ;     } else {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,tom_append_list_concCode( l1.getTailconcCode() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.CodeList  tom_get_slice_concCode( tom.engine.adt.code.types.CodeList  begin,  tom.engine.adt.code.types.CodeList  end, tom.engine.adt.code.types.CodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcCode()  ||  (end==tom_empty_list_concCode()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.codelist.ConsconcCode.make( begin.getHeadconcCode() ,( tom.engine.adt.code.types.CodeList )tom_get_slice_concCode( begin.getTailconcCode() ,end,tail)) ;   }   
  

  private static Logger logger = Logger.getLogger("tom.engine.parser.HostParser");
  
  private TokenStreamSelector selector = null;

  
  private String currentFile = null;

  private HashSet<String> includedFileSet = null;
  private HashSet<String> alreadyParsedFileSet = null;
  

  
  TomLanguage tomparser;

  
  HostLexer targetlexer = null;

  BackQuoteParser bqparser;

  TomParserTool parserTool;

  
  private int currentLine = 1;
  private int currentColumn = 1;

  private boolean skipComment = false;

  public HostParser(TokenStreamSelector selector, String currentFile,
                    HashSet<String> includedFiles, HashSet<String> alreadyParsedFiles,
                    TomParserTool parserTool) {
    this(selector);
    this.selector = selector;
    this.currentFile = currentFile;
    this.parserTool = parserTool;
    this.targetlexer = (HostLexer) selector.getStream("targetlexer");
    targetlexer.setParser(this);
    this.includedFileSet = new HashSet<String>(includedFiles);
    this.alreadyParsedFileSet = alreadyParsedFiles;

    testIncludedFile(currentFile, includedFileSet);
    

    tomparser = new TomLanguage(getInputState(),this, parserTool.getOptionManager());
    bqparser = tomparser.bqparser;
  }

  private void setSkipComment() {
    skipComment = true;
	}
  public boolean isSkipComment() {
    return skipComment;
	}

  private synchronized TomParserTool getParserTool() {
    return parserTool;
  }

  private synchronized TomStreamManager getStreamManager() {
    return getParserTool().getStreamManager();
  }

  public synchronized TokenStreamSelector getSelector() {
    return selector;
  }

  public synchronized String getCurrentFile() {
    return currentFile;
  }

  public synchronized void updatePosition() {
    updatePosition(getLine(),getColumn());
  }

  public synchronized SymbolTable getSymbolTable() {
    return getStreamManager().getSymbolTable();
  }

  public void updatePosition(int i, int j) {
    currentLine = i;
    currentColumn = j;
  }

  public int currentLine(){
    return currentLine;
  }

  public int currentColumn(){
    return currentColumn;
  }

  
  private String cleanCode(String code){
    return code.substring(code.indexOf('{')+1,code.lastIndexOf('}'));
  }

  
  private String removeLastBrace(String code){
    return code.substring(0,code.lastIndexOf("}"));
  }

  
  private String getCode() {
    String result = targetlexer.target.toString();
    targetlexer.clearTarget();
    return result;
  }

  
  public void addTargetCode(Token t){
    targetlexer.target.append(t.getText());
  }

  private String pureCode(String code){
    return code.replace('\t',' ');
  }

  private boolean isCorrect(String code){
    return (! code.equals("") && ! code.matches("\\s*"));
  }

  
  public int getLine(){
    return targetlexer.getLine();
  }

  
  public int getColumn(){
    return targetlexer.getColumn();
  }

  private synchronized void includeFile(String fileName, List<Code> list)
    throws TomException, TomIncludeException {
    Code astTom;
    InputStream input;
    byte inputBuffer[];
    HostParser parser = null;
    File file;

    

    String fileCanonicalName = getParserTool().searchIncludeFile(currentFile, fileName,Integer.valueOf(getLine()));

    try {
      

      
      if(testIncludedFile(fileCanonicalName, alreadyParsedFileSet) ||
         testIncludedFile(fileCanonicalName, includedFileSet)) {
        if(!getStreamManager().isSilentDiscardImport(fileName)) {
          TomMessage.info(logger, currentFile, getLine(), TomMessage.includedFileAlreadyParsed,fileName);
        }
        return;
      }
      Reader fileReader = new BufferedReader(new FileReader(fileCanonicalName));

      parser = tom.engine.parser.TomParserPlugin.createHostParser(fileReader,fileCanonicalName,
          includedFileSet,alreadyParsedFileSet,
          getParserTool());
      parser.setSkipComment();
      astTom = parser.input();
      astTom = tom_make_TomInclude(astTom.getCodeList());
      list.add(astTom);
    } catch (Exception e) {
      if(e instanceof TomIncludeException) {
        throw (TomIncludeException)e;
      }
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      throw new TomException(TomMessage.errorWhileIncludingFile,
          new Object[]{e.getClass(),
            fileName,
            currentFile,
            Integer.valueOf(getLine()),
            sw.toString()
          });
    }
  }

  private boolean testIncludedFile(String fileName, HashSet<String> fileSet) {
    
    return !fileSet.add(fileName);
  }

  
  public String tomSplitter(String subject, List<Code> list) {

    String metaChar = "]%";
    String escapeChar = "@";

    
    subject = subject.replace(escapeChar+escapeChar,metaChar);
    
    String split[] = subject.split(escapeChar);
    boolean last = subject.endsWith(escapeChar);
    int numSeparator = split.length + 1 + (last ? 1 : 0);
    if(numSeparator%2==1) {
      TomMessage.error(logger, currentFile, getLine(), TomMessage.badNumberOfAt);
    }
    
    boolean metaMode = true;
    String res = "";
    for(int i=0 ; i<split.length ; i++) {
      if(metaMode) {
        
        String code = getParserTool().metaEncodeCode(split[i].replace(metaChar,escapeChar));
        metaMode = false;
        
        list.add(tom_make_TargetLanguageToCode(tom_make_ITL(code)));
      } else {
        String code = "+"+split[i]+"+";
        metaMode = true;
        
        try {
          Reader codeReader = new BufferedReader(new StringReader(code));
          HostParser parser;
          HashSet<String> includedFiles = new HashSet<String>();
          HashSet<String> alreadyParsedFiles = new HashSet<String>();
          parser = tom.engine.parser.TomParserPlugin.createHostParser(codeReader,getCurrentFile(),
              includedFiles, alreadyParsedFiles,
              getParserTool());
          Code astTom = parser.input();
          { /* unamed block */{ /* unamed block */if (tom_is_sort_Code(astTom)) {if (tom_is_fun_sym_Tom((( tom.engine.adt.code.types.Code )astTom))) { tom.engine.adt.code.types.CodeList  tomMatch1_1=tom_get_slot_Tom_CodeList((( tom.engine.adt.code.types.Code )astTom));if (tom_is_fun_sym_concCode((( tom.engine.adt.code.types.CodeList )tomMatch1_1))) { tom.engine.adt.code.types.CodeList  tomMatch1_end_7=tomMatch1_1;do {{ /* unamed block */if (!(tom_is_empty_concCode_CodeList(tomMatch1_end_7))) {

              list.add(tom_get_head_concCode_CodeList(tomMatch1_end_7));
            }if (tom_is_empty_concCode_CodeList(tomMatch1_end_7)) {tomMatch1_end_7=tomMatch1_1;} else {tomMatch1_end_7=tom_get_tail_concCode_CodeList(tomMatch1_end_7);}}} while(!(tom_equal_term_CodeList(tomMatch1_end_7, tomMatch1_1)));}}}}}

        } catch (IOException e) {
          throw new TomRuntimeException("IOException catched in tomSplitter");
        } catch (Exception e) {
          throw new TomRuntimeException("Exception catched in tomSplitter");
        }
      }
    }
    if(subject.endsWith(escapeChar)) {
      
      list.add(tom_make_TargetLanguageToCode(tom_make_ITL("\"\"")));
    }
    return res;
  }




protected HostParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public HostParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected HostParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public HostParser(TokenStream lexer) {
  this(lexer,1);
}

public HostParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final Code  input() throws RecognitionException, TokenStreamException, TomException {
		Code result;
		
		Token  t = null;
		
		result = null;
		List<Code> list = new LinkedList<Code>();
		
		
		blockList(list);
		t = LT(1);
		match(Token.EOF_TYPE);
		
		
		list.add(tom_make_TargetLanguageToCode(tom_make_TL(getCode(),tom_make_TextPosition(currentLine(),currentColumn()),tom_make_TextPosition(t.getLine(),t.getColumn())))
		
		);
		
		
		result = tom_make_Tom(ASTFactory.makeCodeList(list));
		
		return result;
	}
	
	public final void blockList(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		
		{
		_loop4:
		do {
			switch ( LA(1)) {
			case MATCH:
			{
				matchConstruct(list);
				break;
			}
			case STRATEGY:
			{
				strategyConstruct(list);
				break;
			}
			case TRANSFORMATION:
			{
				transformationConstruct(list);
				break;
			}
			case TRACELINK:
			{
				tracelinkConstruct(list);
				break;
			}
			case RESOLVE:
			{
				resolveConstruct(list);
				break;
			}
			case GOM:
			{
				gomsignature(list);
				break;
			}
			case BACKQUOTE:
			{
				backquoteTerm(list);
				break;
			}
			case OPERATOR:
			{
				operator(list);
				break;
			}
			case OPERATORLIST:
			{
				operatorList(list);
				break;
			}
			case OPERATORARRAY:
			{
				operatorArray(list);
				break;
			}
			case INCLUDE:
			{
				includeConstruct(list);
				break;
			}
			case TYPETERM:
			{
				typeTerm(list);
				break;
			}
			case CODE:
			{
				code(list);
				break;
			}
			case STRING:
			{
				match(STRING);
				break;
			}
			case LBRACE:
			{
				match(LBRACE);
				blockList(list);
				match(RBRACE);
				break;
			}
			default:
			{
				break _loop4;
			}
			}
		} while (true);
		}
	}
	
	public final void matchConstruct(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		TargetLanguage code = null;
		
		
		t = LT(1);
		match(MATCH);
		
		String textCode = getCode();
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		
		Option ot = tom_make_OriginTracking(tom_make_Name("Match"),t.getLine(),currentFile);
		
		Instruction match = tomparser.matchConstruct(ot);
		list.add(tom_make_InstructionToCode(match));
		
	}
	
	public final void strategyConstruct(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		TargetLanguage code = null;
		
		
		t = LT(1);
		match(STRATEGY);
		
		
		String textCode = getCode();
		
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		
		Option ot = tom_make_OriginTracking(tom_make_Name("Strategy"),t.getLine(),currentFile);
		
		
		Declaration strategy = tomparser.strategyConstruct(ot);
		list.add(tom_make_DeclarationToCode(strategy));
		
	}
	
	public final void transformationConstruct(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		TargetLanguage code = null;
		
		
		t = LT(1);
		match(TRANSFORMATION);
		
		
		String textCode = getCode();
		
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		
		Option ot = tom_make_OriginTracking(tom_make_Name("Transformation"),t.getLine(),currentFile);
		
		
		Declaration transformation = tomparser.transformationConstruct(ot);
		list.add(tom_make_DeclarationToCode(transformation));
		
	}
	
	public final void tracelinkConstruct(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		TargetLanguage code = null;
		
		
		t = LT(1);
		match(TRACELINK);
		
		String textCode = getCode();
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		Option ot = tom_make_OriginTracking(tom_make_Name("Tracelink"),t.getLine(),currentFile);
		Instruction tracelink = tomparser.tracelinkConstruct(ot);
		list.add(tom_make_InstructionToCode(tracelink));
		
	}
	
	public final void resolveConstruct(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		TargetLanguage code = null;
		
		
		t = LT(1);
		match(RESOLVE);
		
		String textCode = getCode();
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		Option ot = tom_make_OriginTracking(tom_make_Name("Resolve"),t.getLine(),currentFile);
		Instruction resolve = tomparser.resolveConstruct(ot);
		list.add(tom_make_InstructionToCode(resolve));
		
	}
	
	public final void gomsignature(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		int initialGomLine;
		TargetLanguage code = null;
		List<Code> blockList = new LinkedList<Code>();
		String gomCode = null;
		
		
		t = LT(1);
		match(GOM);
		
		initialGomLine = t.getLine();
		
		String textCode = getCode();
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		
		
		synchronized(Tom.getLock()) {
		BlockParser blockparser = BlockParser.makeBlockParser(targetlexer.getInputState());
		gomCode = cleanCode(blockparser.block().trim());
		
		
		String[] userOpts = new String[0];
		textCode = t.getText();
		if(textCode.length() > 6) {
		userOpts = textCode.substring(5,textCode.length()-1).split("\\s+");
		}
		
		String generatedMapping = getParserTool().parseGomFile(gomCode, initialGomLine, userOpts);
		includeFile(generatedMapping, list);
		
		updatePosition();
		} 
		
	}
	
	public final void backquoteTerm(
		List<Code> list
	) throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		TargetLanguage code = null;
		
		
		t = LT(1);
		match(BACKQUOTE);
		
		String textCode = getCode();
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		
		Option ot = tom_make_OriginTracking(tom_make_Name("Backquote"),t.getLine(),currentFile);
		BQTerm result = bqparser.beginBackquote();
		
		
		
		updatePosition();
		list.add(tom_make_BQTermToCode(result));
		
		
	}
	
	public final void operator(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		TargetLanguage code = null;
		
		
		t = LT(1);
		match(OPERATOR);
		
		String textCode = pureCode(getCode());
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		
		Declaration operatorDecl = tomparser.operator();
		list.add(tom_make_DeclarationToCode(operatorDecl));
		
	}
	
	public final void operatorList(
		List list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t1 = null;
		
		TargetLanguage code = null;
		int line = 0;
		int column = 0;
		
		
		{
		t1 = LT(1);
		match(OPERATORLIST);
		line=t1.getLine();column=t1.getColumn();
		}
		
		String textCode = pureCode(getCode());
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(line,column))
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		
		Declaration operatorListDecl = tomparser.operatorList();
		list.add(tom_make_DeclarationToCode(operatorListDecl));
		
	}
	
	public final void operatorArray(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		TargetLanguage code = null;
		
		
		t = LT(1);
		match(OPERATORARRAY);
		
		String textCode = pureCode(getCode());
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		
		Declaration operatorArrayDecl = tomparser.operatorArray();
		list.add(tom_make_DeclarationToCode(operatorArrayDecl));
		
	}
	
	public final void includeConstruct(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		TargetLanguage tlCode = null;
		List<Code> blockList = new LinkedList<Code>();
		
		
		t = LT(1);
		match(INCLUDE);
		
		TargetLanguage code = null;
		String textCode = getCode();
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		
		tlCode=goalLanguage(blockList);
		
		includeFile(tlCode.getCode(),list);
		updatePosition();
		
	}
	
	public final void typeTerm(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  tt = null;
		
		TargetLanguage code = null;
		int line, column;
		
		
		{
		tt = LT(1);
		match(TYPETERM);
		
		line = tt.getLine();
		column = tt.getColumn();
		
		}
		
		
		String textCode = getCode();
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(line,column))
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		Declaration termdecl = tomparser.typeTerm();
		
		if (termdecl != null) {
		list.add(tom_make_DeclarationToCode(termdecl));
		}
		
	}
	
	public final void code(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		
		Token  t = null;
		
		TargetLanguage code = null;
		
		
		t = LT(1);
		match(CODE);
		
		String textCode = getCode();
		if(isCorrect(textCode)) {
		code = tom_make_TL(textCode,tom_make_TextPosition(currentLine,currentColumn),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		
		;
		list.add(tom_make_TargetLanguageToCode(code));
		}
		textCode = t.getText();
		String metacode = textCode.substring(2,textCode.length()-2);
		tomSplitter(metacode, list);
		updatePosition(targetlexer.getInputState().getLine(),targetlexer.getInputState().getColumn());
		
	}
	
	public final TargetLanguage  goalLanguage(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		TargetLanguage result;
		
		Token  t1 = null;
		Token  t2 = null;
		
		result =  null;
		
		
		t1 = LT(1);
		match(LBRACE);
		
		updatePosition(t1.getLine(),t1.getColumn());
		
		blockList(list);
		t2 = LT(1);
		match(RBRACE);
		
		result = tom_make_TL(cleanCode(getCode()),tom_make_TextPosition(currentLine(),currentColumn()),tom_make_TextPosition(t2.getLine(),t2.getColumn()))
		
		
		;
		targetlexer.clearTarget();
		
		return result;
	}
	
	public final TargetLanguage  targetLanguage(
		List<Code> list
	) throws RecognitionException, TokenStreamException, TomException {
		TargetLanguage result;
		
		Token  t = null;
		
		result = null;
		
		
		blockList(list);
		t = LT(1);
		match(RBRACE);
		
		String code = removeLastBrace(getCode());
		
		
		
		
		result = tom_make_TL(code,tom_make_TextPosition(currentLine(),currentColumn()),tom_make_TextPosition(t.getLine(),t.getColumn()))
		
		
		;
		targetlexer.clearTarget();
		
		return result;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"STRING",
		"LBRACE",
		"RBRACE",
		"STRATEGY",
		"TRANSFORMATION",
		"RESOLVE",
		"TRACELINK",
		"MATCH",
		"GOM",
		"BACKQUOTE",
		"OPERATOR",
		"OPERATORLIST",
		"OPERATORARRAY",
		"INCLUDE",
		"CODE",
		"TYPETERM",
		"SUBTYPE",
		"ESC",
		"HEX_DIGIT",
		"WS",
		"COMMENT",
		"SL_COMMENT",
		"ML_COMMENT",
		"TARGET"
	};
	
	
	}
