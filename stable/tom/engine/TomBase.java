 /*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom;
  
import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import jtom.tools.*;
import jtom.exception.*;
import jtom.checker.*;
import jtom.adt.*;
import jtom.runtime.*;

public class TomBase extends GenericTraversal {
  private TomEnvironment tomEnvironment;
  private TomList empty;

  private static List emptyList = new ArrayList();
  protected final static boolean debug = false;

  public TomBase(TomEnvironment tomEnvironment) {
    this.tomEnvironment = tomEnvironment;
    this.empty = tsf().makeTomList_Empty();
  }

  protected ASTFactory ast() {
    return tomEnvironment.getASTFactory();
  }

  protected TomSignatureFactory tsf() {
    return tomEnvironment.getTomSignatureFactory();
  }

  protected TomSignatureFactory getTomSignatureFactory() {
    return tsf();
  }
  
  protected Statistics statistics() {
    return tomEnvironment.getStatistics();
  }

  protected SymbolTable symbolTable() {
    return tomEnvironment.getSymbolTable();
  }

  protected boolean isIntType(String type) {
    return type.equals("int");
  }

  protected boolean isBoolType(String type) {
    return type.equals("bool");
  }

  protected TomType getIntType() {
    return symbolTable().getType("int");
  }

  protected TomType getBoolType() {
    return symbolTable().getType("bool");
  }
  
  protected TomType getUniversalType() {
    return symbolTable().getType("universal");
  }

  protected TomTerm makeNumber(int n) {
    return tsf().makeTomTerm_Number(new Integer(n));
  }
  
  protected TomList empty() {
    return empty;
  }

  protected TomList cons(TomTerm t, TomList l) {
    if(t!=null) {
      return tsf().makeTomList_Cons(t,l);
    } else {
      return l;
    }
  }

  protected TomList appendInstruction(Instruction t, TomList l) {
    return append(tom_make_InstructionToTomTerm(t) , l);
  }
  
  protected TomList append(TomTerm t, TomList l) {
    if(l.isEmpty()) {
      return cons(t,l);
    } else {
      return cons(l.getHead(), append(t,l.getTail()));
    }
  }
  
  protected TomList concat(TomList l1, TomList l2) {
    if(l1.isEmpty()) {
      return l2;
    } else {
      return cons(l1.getHead(), concat(l1.getTail(),l2));
    }
  }

  protected TomList reverse(TomList l) {
    TomList reverse = empty();
    while(!l.isEmpty()){
      reverse = cons(l.getHead(),reverse);
      l = l.getTail();
    }
    return reverse;
  }

  protected int length(TomList l) {
    if(l.isEmpty()) {
      return 0;
    } else {
      return 1 + length(l.getTail());
    }
  }
  
// ------------------------------------------------------------
    public Object tom_get_fun_sym_String(String t) { return t; } public boolean tom_cmp_fun_sym_String(Object s1, Object s2) { return  s1.equals(s2); } public Object tom_get_subterm_String(String t, int n) { return null; } public boolean tom_terms_equal_String(Object t1, Object t2) { return t1.equals(t2); }  public Object tom_get_fun_sym_Integer(Integer t) { return t; } public boolean tom_cmp_fun_sym_Integer(Object s1, Object s2) { return  s1.equals(s2); } public Object tom_get_subterm_Integer(Integer t, int n) { return null; } public boolean tom_terms_equal_Integer(Object t1, Object t2) { return t1.equals(t2); }  public Object tom_get_fun_sym_Double(Double t) { return t; } public boolean tom_cmp_fun_sym_Double(Object s1, Object s2) { return  s1.equals(s2); } public Object tom_get_subterm_Double(Double t, int n) { return null; } public boolean tom_terms_equal_Double(Object t1, Object t2) { return t1.equals(t2); }  public Object tom_get_fun_sym_ATerm(ATerm t) { return ((t instanceof ATermAppl)?((ATermAppl)t).getAFun():null); } public boolean tom_cmp_fun_sym_ATerm(Object s1, Object s2) { return  s1==s2; } public Object tom_get_subterm_ATerm(ATerm t, int n) { return (((ATermAppl)t).getArgument(n)); } public boolean tom_terms_equal_ATerm(Object t1, Object t2) { return t1.equals(t2); }  public Object tom_get_fun_sym_Declaration(Declaration t) { return null; } public boolean tom_cmp_fun_sym_Declaration(Object s1, Object s2) { return  false; } public Object tom_get_subterm_Declaration(Declaration t, int n) { return null; } public boolean tom_terms_equal_Declaration(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_TypeTermDecl(Declaration t) { return  (t!= null) &&t.isTypeTermDecl(); } public Declaration tom_make_TypeTermDecl(TomName t0, TomList t1, Option t2) { return  getTomSignatureFactory().makeDeclaration_TypeTermDecl(t0, t1, t2); } public TomName tom_get_slot_TypeTermDecl_astName(Declaration t) { return  t.getAstName(); } public TomList tom_get_slot_TypeTermDecl_keywordList(Declaration t) { return  t.getKeywordList(); } public Option tom_get_slot_TypeTermDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_TypeListDecl(Declaration t) { return  (t!= null) &&t.isTypeListDecl(); } public Declaration tom_make_TypeListDecl(TomName t0, TomList t1, Option t2) { return  getTomSignatureFactory().makeDeclaration_TypeListDecl(t0, t1, t2); } public TomName tom_get_slot_TypeListDecl_astName(Declaration t) { return  t.getAstName(); } public TomList tom_get_slot_TypeListDecl_keywordList(Declaration t) { return  t.getKeywordList(); } public Option tom_get_slot_TypeListDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_TypeArrayDecl(Declaration t) { return  (t!= null) &&t.isTypeArrayDecl(); } public Declaration tom_make_TypeArrayDecl(TomName t0, TomList t1, Option t2) { return  getTomSignatureFactory().makeDeclaration_TypeArrayDecl(t0, t1, t2); } public TomName tom_get_slot_TypeArrayDecl_astName(Declaration t) { return  t.getAstName(); } public TomList tom_get_slot_TypeArrayDecl_keywordList(Declaration t) { return  t.getKeywordList(); } public Option tom_get_slot_TypeArrayDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_GetFunctionSymbolDecl(Declaration t) { return  (t!= null) &&t.isGetFunctionSymbolDecl(); } public Declaration tom_make_GetFunctionSymbolDecl(TomTerm t0, TargetLanguage t1, Option t2) { return  getTomSignatureFactory().makeDeclaration_GetFunctionSymbolDecl(t0, t1, t2); } public TomTerm tom_get_slot_GetFunctionSymbolDecl_termArg(Declaration t) { return  t.getTermArg(); } public TargetLanguage tom_get_slot_GetFunctionSymbolDecl_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_GetFunctionSymbolDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_GetSubtermDecl(Declaration t) { return  (t!= null) &&t.isGetSubtermDecl(); } public Declaration tom_make_GetSubtermDecl(TomTerm t0, TomTerm t1, TargetLanguage t2, Option t3) { return  getTomSignatureFactory().makeDeclaration_GetSubtermDecl(t0, t1, t2, t3); } public TomTerm tom_get_slot_GetSubtermDecl_termArg(Declaration t) { return  t.getTermArg(); } public TomTerm tom_get_slot_GetSubtermDecl_numberArg(Declaration t) { return  t.getNumberArg(); } public TargetLanguage tom_get_slot_GetSubtermDecl_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_GetSubtermDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_IsFsymDecl(Declaration t) { return  (t!= null) &&t.isIsFsymDecl(); } public Declaration tom_make_IsFsymDecl(TomName t0, TomTerm t1, TargetLanguage t2, Option t3) { return  getTomSignatureFactory().makeDeclaration_IsFsymDecl(t0, t1, t2, t3); } public TomName tom_get_slot_IsFsymDecl_astName(Declaration t) { return  t.getAstName(); } public TomTerm tom_get_slot_IsFsymDecl_term(Declaration t) { return  t.getTerm(); } public TargetLanguage tom_get_slot_IsFsymDecl_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_IsFsymDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_GetSlotDecl(Declaration t) { return  (t!= null) &&t.isGetSlotDecl(); } public Declaration tom_make_GetSlotDecl(TomName t0, TomName t1, TomTerm t2, TargetLanguage t3, Option t4) { return  getTomSignatureFactory().makeDeclaration_GetSlotDecl(t0, t1, t2, t3, t4); } public TomName tom_get_slot_GetSlotDecl_astName(Declaration t) { return  t.getAstName(); } public TomName tom_get_slot_GetSlotDecl_slotName(Declaration t) { return  t.getSlotName(); } public TomTerm tom_get_slot_GetSlotDecl_term(Declaration t) { return  t.getTerm(); } public TargetLanguage tom_get_slot_GetSlotDecl_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_GetSlotDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_CompareFunctionSymbolDecl(Declaration t) { return  (t!= null) &&t.isCompareFunctionSymbolDecl(); } public Declaration tom_make_CompareFunctionSymbolDecl(TomTerm t0, TomTerm t1, TargetLanguage t2, Option t3) { return  getTomSignatureFactory().makeDeclaration_CompareFunctionSymbolDecl(t0, t1, t2, t3); } public TomTerm tom_get_slot_CompareFunctionSymbolDecl_symbolArg1(Declaration t) { return  t.getSymbolArg1(); } public TomTerm tom_get_slot_CompareFunctionSymbolDecl_symbolArg2(Declaration t) { return  t.getSymbolArg2(); } public TargetLanguage tom_get_slot_CompareFunctionSymbolDecl_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_CompareFunctionSymbolDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_TermsEqualDecl(Declaration t) { return  (t!= null) &&t.isTermsEqualDecl(); } public Declaration tom_make_TermsEqualDecl(TomTerm t0, TomTerm t1, TargetLanguage t2, Option t3) { return  getTomSignatureFactory().makeDeclaration_TermsEqualDecl(t0, t1, t2, t3); } public TomTerm tom_get_slot_TermsEqualDecl_termArg1(Declaration t) { return  t.getTermArg1(); } public TomTerm tom_get_slot_TermsEqualDecl_termArg2(Declaration t) { return  t.getTermArg2(); } public TargetLanguage tom_get_slot_TermsEqualDecl_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_TermsEqualDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_GetHeadDecl(Declaration t) { return  (t!= null) &&t.isGetHeadDecl(); } public Declaration tom_make_GetHeadDecl(TomTerm t0, TargetLanguage t1, Option t2) { return  getTomSignatureFactory().makeDeclaration_GetHeadDecl(t0, t1, t2); } public TomTerm tom_get_slot_GetHeadDecl_var(Declaration t) { return  t.getVar(); } public TargetLanguage tom_get_slot_GetHeadDecl_tlcode(Declaration t) { return  t.getTlcode(); } public Option tom_get_slot_GetHeadDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_GetTailDecl(Declaration t) { return  (t!= null) &&t.isGetTailDecl(); } public Declaration tom_make_GetTailDecl(TomTerm t0, TargetLanguage t1, Option t2) { return  getTomSignatureFactory().makeDeclaration_GetTailDecl(t0, t1, t2); } public TomTerm tom_get_slot_GetTailDecl_var(Declaration t) { return  t.getVar(); } public TargetLanguage tom_get_slot_GetTailDecl_tlcode(Declaration t) { return  t.getTlcode(); } public Option tom_get_slot_GetTailDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_IsEmptyDecl(Declaration t) { return  (t!= null) &&t.isIsEmptyDecl(); } public Declaration tom_make_IsEmptyDecl(TomTerm t0, TargetLanguage t1, Option t2) { return  getTomSignatureFactory().makeDeclaration_IsEmptyDecl(t0, t1, t2); } public TomTerm tom_get_slot_IsEmptyDecl_var(Declaration t) { return  t.getVar(); } public TargetLanguage tom_get_slot_IsEmptyDecl_tlcode(Declaration t) { return  t.getTlcode(); } public Option tom_get_slot_IsEmptyDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_MakeEmptyList(Declaration t) { return  (t!= null) &&t.isMakeEmptyList(); } public Declaration tom_make_MakeEmptyList(TomName t0, TargetLanguage t1, Option t2) { return  getTomSignatureFactory().makeDeclaration_MakeEmptyList(t0, t1, t2); } public TomName tom_get_slot_MakeEmptyList_astName(Declaration t) { return  t.getAstName(); } public TargetLanguage tom_get_slot_MakeEmptyList_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_MakeEmptyList_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_MakeAddList(Declaration t) { return  (t!= null) &&t.isMakeAddList(); } public Declaration tom_make_MakeAddList(TomName t0, TomTerm t1, TomTerm t2, TargetLanguage t3, Option t4) { return  getTomSignatureFactory().makeDeclaration_MakeAddList(t0, t1, t2, t3, t4); } public TomName tom_get_slot_MakeAddList_astName(Declaration t) { return  t.getAstName(); } public TomTerm tom_get_slot_MakeAddList_varElt(Declaration t) { return  t.getVarElt(); } public TomTerm tom_get_slot_MakeAddList_varList(Declaration t) { return  t.getVarList(); } public TargetLanguage tom_get_slot_MakeAddList_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_MakeAddList_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_GetElementDecl(Declaration t) { return  (t!= null) &&t.isGetElementDecl(); } public Declaration tom_make_GetElementDecl(TomTerm t0, TomTerm t1, TargetLanguage t2, Option t3) { return  getTomSignatureFactory().makeDeclaration_GetElementDecl(t0, t1, t2, t3); } public TomTerm tom_get_slot_GetElementDecl_kid1(Declaration t) { return  t.getKid1(); } public TomTerm tom_get_slot_GetElementDecl_kid2(Declaration t) { return  t.getKid2(); } public TargetLanguage tom_get_slot_GetElementDecl_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_GetElementDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_GetSizeDecl(Declaration t) { return  (t!= null) &&t.isGetSizeDecl(); } public Declaration tom_make_GetSizeDecl(TomTerm t0, TargetLanguage t1, Option t2) { return  getTomSignatureFactory().makeDeclaration_GetSizeDecl(t0, t1, t2); } public TomTerm tom_get_slot_GetSizeDecl_kid1(Declaration t) { return  t.getKid1(); } public TargetLanguage tom_get_slot_GetSizeDecl_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_GetSizeDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_MakeEmptyArray(Declaration t) { return  (t!= null) &&t.isMakeEmptyArray(); } public Declaration tom_make_MakeEmptyArray(TomName t0, TomTerm t1, TargetLanguage t2, Option t3) { return  getTomSignatureFactory().makeDeclaration_MakeEmptyArray(t0, t1, t2, t3); } public TomName tom_get_slot_MakeEmptyArray_astName(Declaration t) { return  t.getAstName(); } public TomTerm tom_get_slot_MakeEmptyArray_varSize(Declaration t) { return  t.getVarSize(); } public TargetLanguage tom_get_slot_MakeEmptyArray_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_MakeEmptyArray_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_MakeAddArray(Declaration t) { return  (t!= null) &&t.isMakeAddArray(); } public Declaration tom_make_MakeAddArray(TomName t0, TomTerm t1, TomTerm t2, TargetLanguage t3, Option t4) { return  getTomSignatureFactory().makeDeclaration_MakeAddArray(t0, t1, t2, t3, t4); } public TomName tom_get_slot_MakeAddArray_astName(Declaration t) { return  t.getAstName(); } public TomTerm tom_get_slot_MakeAddArray_varElt(Declaration t) { return  t.getVarElt(); } public TomTerm tom_get_slot_MakeAddArray_varList(Declaration t) { return  t.getVarList(); } public TargetLanguage tom_get_slot_MakeAddArray_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_MakeAddArray_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_MakeDecl(Declaration t) { return  (t!= null) &&t.isMakeDecl(); } public Declaration tom_make_MakeDecl(TomName t0, TomType t1, TomList t2, TargetLanguage t3, Option t4) { return  getTomSignatureFactory().makeDeclaration_MakeDecl(t0, t1, t2, t3, t4); } public TomName tom_get_slot_MakeDecl_astName(Declaration t) { return  t.getAstName(); } public TomType tom_get_slot_MakeDecl_astType(Declaration t) { return  t.getAstType(); } public TomList tom_get_slot_MakeDecl_args(Declaration t) { return  t.getArgs(); } public TargetLanguage tom_get_slot_MakeDecl_tlCode(Declaration t) { return  t.getTlCode(); } public Option tom_get_slot_MakeDecl_orgTrack(Declaration t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_SymbolDecl(Declaration t) { return  (t!= null) &&t.isSymbolDecl(); } public Declaration tom_make_SymbolDecl(TomName t0) { return  getTomSignatureFactory().makeDeclaration_SymbolDecl(t0); } public TomName tom_get_slot_SymbolDecl_astName(Declaration t) { return  t.getAstName(); }  public boolean tom_is_fun_sym_ListSymbolDecl(Declaration t) { return  (t!= null) &&t.isListSymbolDecl(); } public Declaration tom_make_ListSymbolDecl(TomName t0) { return  getTomSignatureFactory().makeDeclaration_ListSymbolDecl(t0); } public TomName tom_get_slot_ListSymbolDecl_astName(Declaration t) { return  t.getAstName(); }  public boolean tom_is_fun_sym_ArraySymbolDecl(Declaration t) { return  (t!= null) &&t.isArraySymbolDecl(); } public Declaration tom_make_ArraySymbolDecl(TomName t0) { return  getTomSignatureFactory().makeDeclaration_ArraySymbolDecl(t0); } public TomName tom_get_slot_ArraySymbolDecl_astName(Declaration t) { return  t.getAstName(); }  public boolean tom_is_fun_sym_EmptyDeclaration(Declaration t) { return  (t!= null) &&t.isEmptyDeclaration(); } public Declaration tom_make_EmptyDeclaration() { return  getTomSignatureFactory().makeDeclaration_EmptyDeclaration(); }  public Object tom_get_fun_sym_OptionList(OptionList t) { return null; } public boolean tom_cmp_fun_sym_OptionList(Object s1, Object s2) { return  false; } public Object tom_get_subterm_OptionList(OptionList t, int n) { return null; } public boolean tom_terms_equal_OptionList(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_EmptyOptionList(OptionList t) { return  (t!= null) &&t.isEmptyOptionList(); } public OptionList tom_make_EmptyOptionList() { return  getTomSignatureFactory().makeOptionList_EmptyOptionList(); }  public boolean tom_is_fun_sym_ConsOptionList(OptionList t) { return  (t!= null) &&t.isConsOptionList(); } public OptionList tom_make_ConsOptionList(Option t0, OptionList t1) { return  getTomSignatureFactory().makeOptionList_ConsOptionList(t0, t1); } public Option tom_get_slot_ConsOptionList_head(OptionList t) { return  t.getHead(); } public OptionList tom_get_slot_ConsOptionList_tail(OptionList t) { return  t.getTail(); }  public Object tom_get_fun_sym_Option(Option t) { return null; } public boolean tom_cmp_fun_sym_Option(Object s1, Object s2) { return  false; } public Object tom_get_subterm_Option(Option t, int n) { return null; } public boolean tom_terms_equal_Option(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_DeclarationToOption(Option t) { return  (t!= null) &&t.isDeclarationToOption(); } public Option tom_make_DeclarationToOption(Declaration t0) { return  getTomSignatureFactory().makeOption_DeclarationToOption(t0); } public Declaration tom_get_slot_DeclarationToOption_astDeclaration(Option t) { return  t.getAstDeclaration(); }  public boolean tom_is_fun_sym_TomNameToOption(Option t) { return  (t!= null) &&t.isTomNameToOption(); } public Option tom_make_TomNameToOption(TomName t0) { return  getTomSignatureFactory().makeOption_TomNameToOption(t0); } public TomName tom_get_slot_TomNameToOption_astName(Option t) { return  t.getAstName(); }  public boolean tom_is_fun_sym_TomTermToOption(Option t) { return  (t!= null) &&t.isTomTermToOption(); } public Option tom_make_TomTermToOption(TomTerm t0) { return  getTomSignatureFactory().makeOption_TomTermToOption(t0); } public TomTerm tom_get_slot_TomTermToOption_astTerm(Option t) { return  t.getAstTerm(); }  public boolean tom_is_fun_sym_Option(Option t) { return  (t!= null) &&t.isOption(); } public Option tom_make_Option(OptionList t0) { return  getTomSignatureFactory().makeOption_Option(t0); } public OptionList tom_get_slot_Option_optionList(Option t) { return  t.getOptionList(); }  public boolean tom_is_fun_sym_DefinedSymbol(Option t) { return  (t!= null) &&t.isDefinedSymbol(); } public Option tom_make_DefinedSymbol() { return  getTomSignatureFactory().makeOption_DefinedSymbol(); }  public boolean tom_is_fun_sym_OriginTracking(Option t) { return  (t!= null) &&t.isOriginTracking(); } public Option tom_make_OriginTracking(TomName t0, Integer t1) { return  getTomSignatureFactory().makeOption_OriginTracking(t0, t1); } public TomName tom_get_slot_OriginTracking_astName(Option t) { return  t.getAstName(); } public Integer tom_get_slot_OriginTracking_line(Option t) { return  t.getLine(); }  public boolean tom_is_fun_sym_Constructor(Option t) { return  (t!= null) &&t.isConstructor(); } public Option tom_make_Constructor(TomName t0) { return  getTomSignatureFactory().makeOption_Constructor(t0); } public TomName tom_get_slot_Constructor_astName(Option t) { return  t.getAstName(); }  public Object tom_get_fun_sym_Expression(Expression t) { return null; } public boolean tom_cmp_fun_sym_Expression(Object s1, Object s2) { return  false; } public Object tom_get_subterm_Expression(Expression t, int n) { return null; } public boolean tom_terms_equal_Expression(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_TomTermToExpression(Expression t) { return  (t!= null) &&t.isTomTermToExpression(); } public Expression tom_make_TomTermToExpression(TomTerm t0) { return  getTomSignatureFactory().makeExpression_TomTermToExpression(t0); } public TomTerm tom_get_slot_TomTermToExpression_astTerm(Expression t) { return  t.getAstTerm(); }  public boolean tom_is_fun_sym_Not(Expression t) { return  (t!= null) &&t.isNot(); } public Expression tom_make_Not(Expression t0) { return  getTomSignatureFactory().makeExpression_Not(t0); } public Expression tom_get_slot_Not_arg(Expression t) { return  t.getArg(); }  public boolean tom_is_fun_sym_And(Expression t) { return  (t!= null) &&t.isAnd(); } public Expression tom_make_And(Expression t0, Expression t1) { return  getTomSignatureFactory().makeExpression_And(t0, t1); } public Expression tom_get_slot_And_arg1(Expression t) { return  t.getArg1(); } public Expression tom_get_slot_And_arg2(Expression t) { return  t.getArg2(); }  public boolean tom_is_fun_sym_TrueTL(Expression t) { return  (t!= null) &&t.isTrueTL(); } public Expression tom_make_TrueTL() { return  getTomSignatureFactory().makeExpression_TrueTL(); }  public boolean tom_is_fun_sym_FalseTL(Expression t) { return  (t!= null) &&t.isFalseTL(); } public Expression tom_make_FalseTL() { return  getTomSignatureFactory().makeExpression_FalseTL(); }  public boolean tom_is_fun_sym_IsEmptyList(Expression t) { return  (t!= null) &&t.isIsEmptyList(); } public Expression tom_make_IsEmptyList(TomTerm t0) { return  getTomSignatureFactory().makeExpression_IsEmptyList(t0); } public TomTerm tom_get_slot_IsEmptyList_kid1(Expression t) { return  t.getKid1(); }  public boolean tom_is_fun_sym_IsEmptyArray(Expression t) { return  (t!= null) &&t.isIsEmptyArray(); } public Expression tom_make_IsEmptyArray(TomTerm t0, TomTerm t1) { return  getTomSignatureFactory().makeExpression_IsEmptyArray(t0, t1); } public TomTerm tom_get_slot_IsEmptyArray_kid1(Expression t) { return  t.getKid1(); } public TomTerm tom_get_slot_IsEmptyArray_kid2(Expression t) { return  t.getKid2(); }  public boolean tom_is_fun_sym_EqualFunctionSymbol(Expression t) { return  (t!= null) &&t.isEqualFunctionSymbol(); } public Expression tom_make_EqualFunctionSymbol(TomTerm t0, TomTerm t1) { return  getTomSignatureFactory().makeExpression_EqualFunctionSymbol(t0, t1); } public TomTerm tom_get_slot_EqualFunctionSymbol_kid1(Expression t) { return  t.getKid1(); } public TomTerm tom_get_slot_EqualFunctionSymbol_kid2(Expression t) { return  t.getKid2(); }  public boolean tom_is_fun_sym_EqualTerm(Expression t) { return  (t!= null) &&t.isEqualTerm(); } public Expression tom_make_EqualTerm(TomTerm t0, TomTerm t1) { return  getTomSignatureFactory().makeExpression_EqualTerm(t0, t1); } public TomTerm tom_get_slot_EqualTerm_kid1(Expression t) { return  t.getKid1(); } public TomTerm tom_get_slot_EqualTerm_kid2(Expression t) { return  t.getKid2(); }  public boolean tom_is_fun_sym_GetSubterm(Expression t) { return  (t!= null) &&t.isGetSubterm(); } public Expression tom_make_GetSubterm(TomTerm t0, TomTerm t1) { return  getTomSignatureFactory().makeExpression_GetSubterm(t0, t1); } public TomTerm tom_get_slot_GetSubterm_term(Expression t) { return  t.getTerm(); } public TomTerm tom_get_slot_GetSubterm_number(Expression t) { return  t.getNumber(); }  public boolean tom_is_fun_sym_IsFsym(Expression t) { return  (t!= null) &&t.isIsFsym(); } public Expression tom_make_IsFsym(TomName t0, TomTerm t1) { return  getTomSignatureFactory().makeExpression_IsFsym(t0, t1); } public TomName tom_get_slot_IsFsym_astName(Expression t) { return  t.getAstName(); } public TomTerm tom_get_slot_IsFsym_term(Expression t) { return  t.getTerm(); }  public boolean tom_is_fun_sym_GetSlot(Expression t) { return  (t!= null) &&t.isGetSlot(); } public Expression tom_make_GetSlot(TomName t0, String t1, TomTerm t2) { return  getTomSignatureFactory().makeExpression_GetSlot(t0, t1, t2); } public TomName tom_get_slot_GetSlot_astName(Expression t) { return  t.getAstName(); } public String tom_get_slot_GetSlot_slotNameString(Expression t) { return  t.getSlotNameString(); } public TomTerm tom_get_slot_GetSlot_term(Expression t) { return  t.getTerm(); }  public boolean tom_is_fun_sym_GetHead(Expression t) { return  (t!= null) &&t.isGetHead(); } public Expression tom_make_GetHead(TomTerm t0) { return  getTomSignatureFactory().makeExpression_GetHead(t0); } public TomTerm tom_get_slot_GetHead_kid1(Expression t) { return  t.getKid1(); }  public boolean tom_is_fun_sym_GetTail(Expression t) { return  (t!= null) &&t.isGetTail(); } public Expression tom_make_GetTail(TomTerm t0) { return  getTomSignatureFactory().makeExpression_GetTail(t0); } public TomTerm tom_get_slot_GetTail_kid1(Expression t) { return  t.getKid1(); }  public boolean tom_is_fun_sym_GetSize(Expression t) { return  (t!= null) &&t.isGetSize(); } public Expression tom_make_GetSize(TomTerm t0) { return  getTomSignatureFactory().makeExpression_GetSize(t0); } public TomTerm tom_get_slot_GetSize_kid1(Expression t) { return  t.getKid1(); }  public boolean tom_is_fun_sym_GetElement(Expression t) { return  (t!= null) &&t.isGetElement(); } public Expression tom_make_GetElement(TomTerm t0, TomTerm t1) { return  getTomSignatureFactory().makeExpression_GetElement(t0, t1); } public TomTerm tom_get_slot_GetElement_kid1(Expression t) { return  t.getKid1(); } public TomTerm tom_get_slot_GetElement_kid2(Expression t) { return  t.getKid2(); }  public boolean tom_is_fun_sym_GetSliceList(Expression t) { return  (t!= null) &&t.isGetSliceList(); } public Expression tom_make_GetSliceList(TomName t0, TomTerm t1, TomTerm t2) { return  getTomSignatureFactory().makeExpression_GetSliceList(t0, t1, t2); } public TomName tom_get_slot_GetSliceList_astName(Expression t) { return  t.getAstName(); } public TomTerm tom_get_slot_GetSliceList_variableBeginAST(Expression t) { return  t.getVariableBeginAST(); } public TomTerm tom_get_slot_GetSliceList_variableEndAST(Expression t) { return  t.getVariableEndAST(); }  public boolean tom_is_fun_sym_GetSliceArray(Expression t) { return  (t!= null) &&t.isGetSliceArray(); } public Expression tom_make_GetSliceArray(TomName t0, TomTerm t1, TomTerm t2, TomTerm t3) { return  getTomSignatureFactory().makeExpression_GetSliceArray(t0, t1, t2, t3); } public TomName tom_get_slot_GetSliceArray_astName(Expression t) { return  t.getAstName(); } public TomTerm tom_get_slot_GetSliceArray_subjectListName(Expression t) { return  t.getSubjectListName(); } public TomTerm tom_get_slot_GetSliceArray_variableBeginAST(Expression t) { return  t.getVariableBeginAST(); } public TomTerm tom_get_slot_GetSliceArray_variableEndAST(Expression t) { return  t.getVariableEndAST(); }  public Object tom_get_fun_sym_TargetLanguage(TargetLanguage t) { return null; } public boolean tom_cmp_fun_sym_TargetLanguage(Object s1, Object s2) { return  false; } public Object tom_get_subterm_TargetLanguage(TargetLanguage t, int n) { return null; } public boolean tom_terms_equal_TargetLanguage(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_TL(TargetLanguage t) { return  (t!= null) &&t.isTL(); } public TargetLanguage tom_make_TL(String t0, Position t1, Position t2) { return  getTomSignatureFactory().makeTargetLanguage_TL(t0, t1, t2); } public String tom_get_slot_TL_code(TargetLanguage t) { return  t.getCode(); } public Position tom_get_slot_TL_start(TargetLanguage t) { return  t.getStart(); } public Position tom_get_slot_TL_end(TargetLanguage t) { return  t.getEnd(); }  public boolean tom_is_fun_sym_ITL(TargetLanguage t) { return  (t!= null) &&t.isITL(); } public TargetLanguage tom_make_ITL(String t0) { return  getTomSignatureFactory().makeTargetLanguage_ITL(t0); } public String tom_get_slot_ITL_code(TargetLanguage t) { return  t.getCode(); }  public Object tom_get_fun_sym_Position(Position t) { return null; } public boolean tom_cmp_fun_sym_Position(Object s1, Object s2) { return  false; } public Object tom_get_subterm_Position(Position t, int n) { return null; } public boolean tom_terms_equal_Position(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_Position(Position t) { return  (t!= null) &&t.isPosition(); } public Position tom_make_Position(Integer t0, Integer t1) { return  getTomSignatureFactory().makePosition_Position(t0, t1); } public Integer tom_get_slot_Position_line(Position t) { return  t.getLine(); } public Integer tom_get_slot_Position_column(Position t) { return  t.getColumn(); }  public Object tom_get_fun_sym_TomType(TomType t) { return null; } public boolean tom_cmp_fun_sym_TomType(Object s1, Object s2) { return  false; } public Object tom_get_subterm_TomType(TomType t, int n) { return null; } public boolean tom_terms_equal_TomType(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_Type(TomType t) { return  (t!= null) &&t.isType(); } public TomType tom_make_Type(TomType t0, TomType t1) { return  getTomSignatureFactory().makeTomType_Type(t0, t1); } public TomType tom_get_slot_Type_tomType(TomType t) { return  t.getTomType(); } public TomType tom_get_slot_Type_tlType(TomType t) { return  t.getTlType(); }  public boolean tom_is_fun_sym_TypesToType(TomType t) { return  (t!= null) &&t.isTypesToType(); } public TomType tom_make_TypesToType(TomList t0, TomType t1) { return  getTomSignatureFactory().makeTomType_TypesToType(t0, t1); } public TomList tom_get_slot_TypesToType_list(TomType t) { return  t.getList(); } public TomType tom_get_slot_TypesToType_codomain(TomType t) { return  t.getCodomain(); }  public boolean tom_is_fun_sym_TomType(TomType t) { return  (t!= null) &&t.isTomType(); } public TomType tom_make_TomType(String t0) { return  getTomSignatureFactory().makeTomType_TomType(t0); } public String tom_get_slot_TomType_string(TomType t) { return  t.getString(); }  public boolean tom_is_fun_sym_TomTypeAlone(TomType t) { return  (t!= null) &&t.isTomTypeAlone(); } public TomType tom_make_TomTypeAlone(String t0) { return  getTomSignatureFactory().makeTomType_TomTypeAlone(t0); } public String tom_get_slot_TomTypeAlone_string(TomType t) { return  t.getString(); }  public boolean tom_is_fun_sym_TLType(TomType t) { return  (t!= null) &&t.isTLType(); } public TomType tom_make_TLType(TargetLanguage t0) { return  getTomSignatureFactory().makeTomType_TLType(t0); } public TargetLanguage tom_get_slot_TLType_tl(TomType t) { return  t.getTl(); }  public boolean tom_is_fun_sym_EmptyType(TomType t) { return  (t!= null) &&t.isEmptyType(); } public TomType tom_make_EmptyType() { return  getTomSignatureFactory().makeTomType_EmptyType(); }  public Object tom_get_fun_sym_TomName(TomName t) { return null; } public boolean tom_cmp_fun_sym_TomName(Object s1, Object s2) { return  false; } public Object tom_get_subterm_TomName(TomName t, int n) { return null; } public boolean tom_terms_equal_TomName(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_Name(TomName t) { return  (t!= null) &&t.isName(); } public TomName tom_make_Name(String t0) { return  getTomSignatureFactory().makeTomName_Name(t0); } public String tom_get_slot_Name_string(TomName t) { return  t.getString(); }  public boolean tom_is_fun_sym_PositionName(TomName t) { return  (t!= null) &&t.isPositionName(); } public TomName tom_make_PositionName(TomList t0) { return  getTomSignatureFactory().makeTomName_PositionName(t0); } public TomList tom_get_slot_PositionName_numberList(TomName t) { return  t.getNumberList(); }  public boolean tom_is_fun_sym_EmptyName(TomName t) { return  (t!= null) &&t.isEmptyName(); } public TomName tom_make_EmptyName() { return  getTomSignatureFactory().makeTomName_EmptyName(); }  public Object tom_get_fun_sym_TomList(TomList t) { return null; } public boolean tom_cmp_fun_sym_TomList(Object s1, Object s2) { return  false; } public Object tom_get_subterm_TomList(TomList t, int n) { return null; } public boolean tom_terms_equal_TomList(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_Empty(TomList t) { return  (t!= null) &&t.isEmpty(); } public TomList tom_make_Empty() { return  getTomSignatureFactory().makeTomList_Empty(); }  public boolean tom_is_fun_sym_Cons(TomList t) { return  (t!= null) &&t.isCons(); } public TomList tom_make_Cons(TomTerm t0, TomList t1) { return  getTomSignatureFactory().makeTomList_Cons(t0, t1); } public TomTerm tom_get_slot_Cons_head(TomList t) { return  t.getHead(); } public TomList tom_get_slot_Cons_tail(TomList t) { return  t.getTail(); }  public Object tom_get_fun_sym_TomTerm(TomTerm t) { return null; } public boolean tom_cmp_fun_sym_TomTerm(Object s1, Object s2) { return  false; } public Object tom_get_subterm_TomTerm(TomTerm t, int n) { return null; } public boolean tom_terms_equal_TomTerm(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_TargetLanguageToTomTerm(TomTerm t) { return  (t!= null) &&t.isTargetLanguageToTomTerm(); } public TomTerm tom_make_TargetLanguageToTomTerm(TargetLanguage t0) { return  getTomSignatureFactory().makeTomTerm_TargetLanguageToTomTerm(t0); } public TargetLanguage tom_get_slot_TargetLanguageToTomTerm_tl(TomTerm t) { return  t.getTl(); }  public boolean tom_is_fun_sym_TomTypeToTomTerm(TomTerm t) { return  (t!= null) &&t.isTomTypeToTomTerm(); } public TomTerm tom_make_TomTypeToTomTerm(TomType t0) { return  getTomSignatureFactory().makeTomTerm_TomTypeToTomTerm(t0); } public TomType tom_get_slot_TomTypeToTomTerm_astType(TomTerm t) { return  t.getAstType(); }  public boolean tom_is_fun_sym_TomNameToTomTerm(TomTerm t) { return  (t!= null) &&t.isTomNameToTomTerm(); } public TomTerm tom_make_TomNameToTomTerm(TomName t0) { return  getTomSignatureFactory().makeTomTerm_TomNameToTomTerm(t0); } public TomName tom_get_slot_TomNameToTomTerm_astName(TomTerm t) { return  t.getAstName(); }  public boolean tom_is_fun_sym_TomSymbolToTomTerm(TomTerm t) { return  (t!= null) &&t.isTomSymbolToTomTerm(); } public TomTerm tom_make_TomSymbolToTomTerm(TomSymbol t0) { return  getTomSignatureFactory().makeTomTerm_TomSymbolToTomTerm(t0); } public TomSymbol tom_get_slot_TomSymbolToTomTerm_astSymbol(TomTerm t) { return  t.getAstSymbol(); }  public boolean tom_is_fun_sym_DeclarationToTomTerm(TomTerm t) { return  (t!= null) &&t.isDeclarationToTomTerm(); } public TomTerm tom_make_DeclarationToTomTerm(Declaration t0) { return  getTomSignatureFactory().makeTomTerm_DeclarationToTomTerm(t0); } public Declaration tom_get_slot_DeclarationToTomTerm_astDeclaration(TomTerm t) { return  t.getAstDeclaration(); }  public boolean tom_is_fun_sym_OptionToTomTerm(TomTerm t) { return  (t!= null) &&t.isOptionToTomTerm(); } public TomTerm tom_make_OptionToTomTerm(Option t0) { return  getTomSignatureFactory().makeTomTerm_OptionToTomTerm(t0); } public Option tom_get_slot_OptionToTomTerm_astOption(TomTerm t) { return  t.getAstOption(); }  public boolean tom_is_fun_sym_ExpressionToTomTerm(TomTerm t) { return  (t!= null) &&t.isExpressionToTomTerm(); } public TomTerm tom_make_ExpressionToTomTerm(Expression t0) { return  getTomSignatureFactory().makeTomTerm_ExpressionToTomTerm(t0); } public Expression tom_get_slot_ExpressionToTomTerm_astExpression(TomTerm t) { return  t.getAstExpression(); }  public boolean tom_is_fun_sym_InstructionToTomTerm(TomTerm t) { return  (t!= null) &&t.isInstructionToTomTerm(); } public TomTerm tom_make_InstructionToTomTerm(Instruction t0) { return  getTomSignatureFactory().makeTomTerm_InstructionToTomTerm(t0); } public Instruction tom_get_slot_InstructionToTomTerm_astInstruction(TomTerm t) { return  t.getAstInstruction(); }  public boolean tom_is_fun_sym_Tom(TomTerm t) { return  (t!= null) &&t.isTom(); } public TomTerm tom_make_Tom(TomList t0) { return  getTomSignatureFactory().makeTomTerm_Tom(t0); } public TomList tom_get_slot_Tom_list(TomTerm t) { return  t.getList(); }  public boolean tom_is_fun_sym_TomInclude(TomTerm t) { return  (t!= null) &&t.isTomInclude(); } public TomTerm tom_make_TomInclude(TomList t0) { return  getTomSignatureFactory().makeTomTerm_TomInclude(t0); } public TomList tom_get_slot_TomInclude_list(TomTerm t) { return  t.getList(); }  public boolean tom_is_fun_sym_MakeTerm(TomTerm t) { return  (t!= null) &&t.isMakeTerm(); } public TomTerm tom_make_MakeTerm(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_MakeTerm(t0); } public TomTerm tom_get_slot_MakeTerm_kid1(TomTerm t) { return  t.getKid1(); }  public boolean tom_is_fun_sym_BackQuoteTerm(TomTerm t) { return  (t!= null) &&t.isBackQuoteTerm(); } public TomTerm tom_make_BackQuoteTerm(TomTerm t0, Option t1) { return  getTomSignatureFactory().makeTomTerm_BackQuoteTerm(t0, t1); } public TomTerm tom_get_slot_BackQuoteTerm_term(TomTerm t) { return  t.getTerm(); } public Option tom_get_slot_BackQuoteTerm_option(TomTerm t) { return  t.getOption(); }  public boolean tom_is_fun_sym_FunctionCall(TomTerm t) { return  (t!= null) &&t.isFunctionCall(); } public TomTerm tom_make_FunctionCall(TomName t0, TomList t1) { return  getTomSignatureFactory().makeTomTerm_FunctionCall(t0, t1); } public TomName tom_get_slot_FunctionCall_astName(TomTerm t) { return  t.getAstName(); } public TomList tom_get_slot_FunctionCall_args(TomTerm t) { return  t.getArgs(); }  public boolean tom_is_fun_sym_MakeFunctionBegin(TomTerm t) { return  (t!= null) &&t.isMakeFunctionBegin(); } public TomTerm tom_make_MakeFunctionBegin(TomName t0, TomTerm t1) { return  getTomSignatureFactory().makeTomTerm_MakeFunctionBegin(t0, t1); } public TomName tom_get_slot_MakeFunctionBegin_astName(TomTerm t) { return  t.getAstName(); } public TomTerm tom_get_slot_MakeFunctionBegin_subjectListAST(TomTerm t) { return  t.getSubjectListAST(); }  public boolean tom_is_fun_sym_MakeFunctionEnd(TomTerm t) { return  (t!= null) &&t.isMakeFunctionEnd(); } public TomTerm tom_make_MakeFunctionEnd() { return  getTomSignatureFactory().makeTomTerm_MakeFunctionEnd(); }  public boolean tom_is_fun_sym_Appl(TomTerm t) { return  (t!= null) &&t.isAppl(); } public TomTerm tom_make_Appl(Option t0, TomName t1, TomList t2) { return  getTomSignatureFactory().makeTomTerm_Appl(t0, t1, t2); } public Option tom_get_slot_Appl_option(TomTerm t) { return  t.getOption(); } public TomName tom_get_slot_Appl_astName(TomTerm t) { return  t.getAstName(); } public TomList tom_get_slot_Appl_args(TomTerm t) { return  t.getArgs(); }  public boolean tom_is_fun_sym_RecordAppl(TomTerm t) { return  (t!= null) &&t.isRecordAppl(); } public TomTerm tom_make_RecordAppl(Option t0, TomName t1, TomList t2) { return  getTomSignatureFactory().makeTomTerm_RecordAppl(t0, t1, t2); } public Option tom_get_slot_RecordAppl_option(TomTerm t) { return  t.getOption(); } public TomName tom_get_slot_RecordAppl_astName(TomTerm t) { return  t.getAstName(); } public TomList tom_get_slot_RecordAppl_args(TomTerm t) { return  t.getArgs(); }  public boolean tom_is_fun_sym_PairSlotAppl(TomTerm t) { return  (t!= null) &&t.isPairSlotAppl(); } public TomTerm tom_make_PairSlotAppl(TomName t0, TomTerm t1) { return  getTomSignatureFactory().makeTomTerm_PairSlotAppl(t0, t1); } public TomName tom_get_slot_PairSlotAppl_slotName(TomTerm t) { return  t.getSlotName(); } public TomTerm tom_get_slot_PairSlotAppl_appl(TomTerm t) { return  t.getAppl(); }  public boolean tom_is_fun_sym_Match(TomTerm t) { return  (t!= null) &&t.isMatch(); } public TomTerm tom_make_Match(Option t0, TomTerm t1, TomTerm t2) { return  getTomSignatureFactory().makeTomTerm_Match(t0, t1, t2); } public Option tom_get_slot_Match_option(TomTerm t) { return  t.getOption(); } public TomTerm tom_get_slot_Match_subjectList(TomTerm t) { return  t.getSubjectList(); } public TomTerm tom_get_slot_Match_patternList(TomTerm t) { return  t.getPatternList(); }  public boolean tom_is_fun_sym_RuleSet(TomTerm t) { return  (t!= null) &&t.isRuleSet(); } public TomTerm tom_make_RuleSet(Option t0, TomList t1) { return  getTomSignatureFactory().makeTomTerm_RuleSet(t0, t1); } public Option tom_get_slot_RuleSet_option(TomTerm t) { return  t.getOption(); } public TomList tom_get_slot_RuleSet_list(TomTerm t) { return  t.getList(); }  public boolean tom_is_fun_sym_RewriteRule(TomTerm t) { return  (t!= null) &&t.isRewriteRule(); } public TomTerm tom_make_RewriteRule(TomTerm t0, TomTerm t1, Option t2) { return  getTomSignatureFactory().makeTomTerm_RewriteRule(t0, t1, t2); } public TomTerm tom_get_slot_RewriteRule_lhs(TomTerm t) { return  t.getLhs(); } public TomTerm tom_get_slot_RewriteRule_rhs(TomTerm t) { return  t.getRhs(); } public Option tom_get_slot_RewriteRule_orgTrack(TomTerm t) { return  t.getOrgTrack(); }  public boolean tom_is_fun_sym_SubjectList(TomTerm t) { return  (t!= null) &&t.isSubjectList(); } public TomTerm tom_make_SubjectList(TomList t0) { return  getTomSignatureFactory().makeTomTerm_SubjectList(t0); } public TomList tom_get_slot_SubjectList_list(TomTerm t) { return  t.getList(); }  public boolean tom_is_fun_sym_PatternList(TomTerm t) { return  (t!= null) &&t.isPatternList(); } public TomTerm tom_make_PatternList(TomList t0) { return  getTomSignatureFactory().makeTomTerm_PatternList(t0); } public TomList tom_get_slot_PatternList_list(TomTerm t) { return  t.getList(); }  public boolean tom_is_fun_sym_TermList(TomTerm t) { return  (t!= null) &&t.isTermList(); } public TomTerm tom_make_TermList(TomList t0) { return  getTomSignatureFactory().makeTomTerm_TermList(t0); } public TomList tom_get_slot_TermList_list(TomTerm t) { return  t.getList(); }  public boolean tom_is_fun_sym_Term(TomTerm t) { return  (t!= null) &&t.isTerm(); } public TomTerm tom_make_Term(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_Term(t0); } public TomTerm tom_get_slot_Term_kid1(TomTerm t) { return  t.getKid1(); }  public boolean tom_is_fun_sym_PatternAction(TomTerm t) { return  (t!= null) &&t.isPatternAction(); } public TomTerm tom_make_PatternAction(TomTerm t0, TomTerm t1) { return  getTomSignatureFactory().makeTomTerm_PatternAction(t0, t1); } public TomTerm tom_get_slot_PatternAction_termList(TomTerm t) { return  t.getTermList(); } public TomTerm tom_get_slot_PatternAction_tom(TomTerm t) { return  t.getTom(); }  public boolean tom_is_fun_sym_TLVar(TomTerm t) { return  (t!= null) &&t.isTLVar(); } public TomTerm tom_make_TLVar(String t0, TomType t1) { return  getTomSignatureFactory().makeTomTerm_TLVar(t0, t1); } public String tom_get_slot_TLVar_strName(TomTerm t) { return  t.getStrName(); } public TomType tom_get_slot_TLVar_astType(TomTerm t) { return  t.getAstType(); }  public boolean tom_is_fun_sym_Declaration(TomTerm t) { return  (t!= null) &&t.isDeclaration(); } public TomTerm tom_make_Declaration(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_Declaration(t0); } public TomTerm tom_get_slot_Declaration_kid1(TomTerm t) { return  t.getKid1(); }  public boolean tom_is_fun_sym_Variable(TomTerm t) { return  (t!= null) &&t.isVariable(); } public TomTerm tom_make_Variable(Option t0, TomName t1, TomType t2) { return  getTomSignatureFactory().makeTomTerm_Variable(t0, t1, t2); } public Option tom_get_slot_Variable_option(TomTerm t) { return  t.getOption(); } public TomName tom_get_slot_Variable_astName(TomTerm t) { return  t.getAstName(); } public TomType tom_get_slot_Variable_astType(TomTerm t) { return  t.getAstType(); }  public boolean tom_is_fun_sym_VariableStar(TomTerm t) { return  (t!= null) &&t.isVariableStar(); } public TomTerm tom_make_VariableStar(Option t0, TomName t1, TomType t2) { return  getTomSignatureFactory().makeTomTerm_VariableStar(t0, t1, t2); } public Option tom_get_slot_VariableStar_option(TomTerm t) { return  t.getOption(); } public TomName tom_get_slot_VariableStar_astName(TomTerm t) { return  t.getAstName(); } public TomType tom_get_slot_VariableStar_astType(TomTerm t) { return  t.getAstType(); }  public boolean tom_is_fun_sym_Placeholder(TomTerm t) { return  (t!= null) &&t.isPlaceholder(); } public TomTerm tom_make_Placeholder(Option t0) { return  getTomSignatureFactory().makeTomTerm_Placeholder(t0); } public Option tom_get_slot_Placeholder_option(TomTerm t) { return  t.getOption(); }  public boolean tom_is_fun_sym_UnamedVariable(TomTerm t) { return  (t!= null) &&t.isUnamedVariable(); } public TomTerm tom_make_UnamedVariable(Option t0, TomType t1) { return  getTomSignatureFactory().makeTomTerm_UnamedVariable(t0, t1); } public Option tom_get_slot_UnamedVariable_option(TomTerm t) { return  t.getOption(); } public TomType tom_get_slot_UnamedVariable_astType(TomTerm t) { return  t.getAstType(); }  public boolean tom_is_fun_sym_DotTerm(TomTerm t) { return  (t!= null) &&t.isDotTerm(); } public TomTerm tom_make_DotTerm(TomTerm t0, TomTerm t1) { return  getTomSignatureFactory().makeTomTerm_DotTerm(t0, t1); } public TomTerm tom_get_slot_DotTerm_kid1(TomTerm t) { return  t.getKid1(); } public TomTerm tom_get_slot_DotTerm_kid2(TomTerm t) { return  t.getKid2(); }  public boolean tom_is_fun_sym_LocalVariable(TomTerm t) { return  (t!= null) &&t.isLocalVariable(); } public TomTerm tom_make_LocalVariable() { return  getTomSignatureFactory().makeTomTerm_LocalVariable(); }  public boolean tom_is_fun_sym_EndLocalVariable(TomTerm t) { return  (t!= null) &&t.isEndLocalVariable(); } public TomTerm tom_make_EndLocalVariable() { return  getTomSignatureFactory().makeTomTerm_EndLocalVariable(); }  public boolean tom_is_fun_sym_BuildVariable(TomTerm t) { return  (t!= null) &&t.isBuildVariable(); } public TomTerm tom_make_BuildVariable(TomName t0) { return  getTomSignatureFactory().makeTomTerm_BuildVariable(t0); } public TomName tom_get_slot_BuildVariable_astName(TomTerm t) { return  t.getAstName(); }  public boolean tom_is_fun_sym_BuildVariableStar(TomTerm t) { return  (t!= null) &&t.isBuildVariableStar(); } public TomTerm tom_make_BuildVariableStar(TomName t0) { return  getTomSignatureFactory().makeTomTerm_BuildVariableStar(t0); } public TomName tom_get_slot_BuildVariableStar_astName(TomTerm t) { return  t.getAstName(); }  public boolean tom_is_fun_sym_BuildTerm(TomTerm t) { return  (t!= null) &&t.isBuildTerm(); } public TomTerm tom_make_BuildTerm(TomName t0, TomList t1) { return  getTomSignatureFactory().makeTomTerm_BuildTerm(t0, t1); } public TomName tom_get_slot_BuildTerm_astName(TomTerm t) { return  t.getAstName(); } public TomList tom_get_slot_BuildTerm_args(TomTerm t) { return  t.getArgs(); }  public boolean tom_is_fun_sym_BuildList(TomTerm t) { return  (t!= null) &&t.isBuildList(); } public TomTerm tom_make_BuildList(TomName t0, TomList t1) { return  getTomSignatureFactory().makeTomTerm_BuildList(t0, t1); } public TomName tom_get_slot_BuildList_astName(TomTerm t) { return  t.getAstName(); } public TomList tom_get_slot_BuildList_args(TomTerm t) { return  t.getArgs(); }  public boolean tom_is_fun_sym_BuildArray(TomTerm t) { return  (t!= null) &&t.isBuildArray(); } public TomTerm tom_make_BuildArray(TomName t0, TomList t1) { return  getTomSignatureFactory().makeTomTerm_BuildArray(t0, t1); } public TomName tom_get_slot_BuildArray_astName(TomTerm t) { return  t.getAstName(); } public TomList tom_get_slot_BuildArray_args(TomTerm t) { return  t.getArgs(); }  public boolean tom_is_fun_sym_BuildBuiltin(TomTerm t) { return  (t!= null) &&t.isBuildBuiltin(); } public TomTerm tom_make_BuildBuiltin(TomName t0) { return  getTomSignatureFactory().makeTomTerm_BuildBuiltin(t0); } public TomName tom_get_slot_BuildBuiltin_astName(TomTerm t) { return  t.getAstName(); }  public boolean tom_is_fun_sym_CompiledMatch(TomTerm t) { return  (t!= null) &&t.isCompiledMatch(); } public TomTerm tom_make_CompiledMatch(TomList t0, TomList t1) { return  getTomSignatureFactory().makeTomTerm_CompiledMatch(t0, t1); } public TomList tom_get_slot_CompiledMatch_decls(TomTerm t) { return  t.getDecls(); } public TomList tom_get_slot_CompiledMatch_automataList(TomTerm t) { return  t.getAutomataList(); }  public boolean tom_is_fun_sym_Automata(TomTerm t) { return  (t!= null) &&t.isAutomata(); } public TomTerm tom_make_Automata(TomList t0, TomList t1) { return  getTomSignatureFactory().makeTomTerm_Automata(t0, t1); } public TomList tom_get_slot_Automata_numberList(TomTerm t) { return  t.getNumberList(); } public TomList tom_get_slot_Automata_instList(TomTerm t) { return  t.getInstList(); }  public boolean tom_is_fun_sym_MatchNumber(TomTerm t) { return  (t!= null) &&t.isMatchNumber(); } public TomTerm tom_make_MatchNumber(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_MatchNumber(t0); } public TomTerm tom_get_slot_MatchNumber_number(TomTerm t) { return  t.getNumber(); }  public boolean tom_is_fun_sym_PatternNumber(TomTerm t) { return  (t!= null) &&t.isPatternNumber(); } public TomTerm tom_make_PatternNumber(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_PatternNumber(t0); } public TomTerm tom_get_slot_PatternNumber_number(TomTerm t) { return  t.getNumber(); }  public boolean tom_is_fun_sym_ListNumber(TomTerm t) { return  (t!= null) &&t.isListNumber(); } public TomTerm tom_make_ListNumber(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_ListNumber(t0); } public TomTerm tom_get_slot_ListNumber_number(TomTerm t) { return  t.getNumber(); }  public boolean tom_is_fun_sym_IndexNumber(TomTerm t) { return  (t!= null) &&t.isIndexNumber(); } public TomTerm tom_make_IndexNumber(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_IndexNumber(t0); } public TomTerm tom_get_slot_IndexNumber_number(TomTerm t) { return  t.getNumber(); }  public boolean tom_is_fun_sym_AbsVar(TomTerm t) { return  (t!= null) &&t.isAbsVar(); } public TomTerm tom_make_AbsVar(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_AbsVar(t0); } public TomTerm tom_get_slot_AbsVar_number(TomTerm t) { return  t.getNumber(); }  public boolean tom_is_fun_sym_RenamedVar(TomTerm t) { return  (t!= null) &&t.isRenamedVar(); } public TomTerm tom_make_RenamedVar(TomName t0) { return  getTomSignatureFactory().makeTomTerm_RenamedVar(t0); } public TomName tom_get_slot_RenamedVar_astName(TomTerm t) { return  t.getAstName(); }  public boolean tom_is_fun_sym_RuleVar(TomTerm t) { return  (t!= null) &&t.isRuleVar(); } public TomTerm tom_make_RuleVar() { return  getTomSignatureFactory().makeTomTerm_RuleVar(); }  public boolean tom_is_fun_sym_Number(TomTerm t) { return  (t!= null) &&t.isNumber(); } public TomTerm tom_make_Number(Integer t0) { return  getTomSignatureFactory().makeTomTerm_Number(t0); } public Integer tom_get_slot_Number_integer(TomTerm t) { return  t.getInteger(); }  public boolean tom_is_fun_sym_Begin(TomTerm t) { return  (t!= null) &&t.isBegin(); } public TomTerm tom_make_Begin(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_Begin(t0); } public TomTerm tom_get_slot_Begin_number(TomTerm t) { return  t.getNumber(); }  public boolean tom_is_fun_sym_End(TomTerm t) { return  (t!= null) &&t.isEnd(); } public TomTerm tom_make_End(TomTerm t0) { return  getTomSignatureFactory().makeTomTerm_End(t0); } public TomTerm tom_get_slot_End_number(TomTerm t) { return  t.getNumber(); }  public Object tom_get_fun_sym_Instruction(Instruction t) { return null; } public boolean tom_cmp_fun_sym_Instruction(Object s1, Object s2) { return  false; } public Object tom_get_subterm_Instruction(Instruction t, int n) { return null; } public boolean tom_terms_equal_Instruction(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_IfThenElse(Instruction t) { return  (t!= null) &&t.isIfThenElse(); } public Instruction tom_make_IfThenElse(Expression t0, TomList t1, TomList t2) { return  getTomSignatureFactory().makeInstruction_IfThenElse(t0, t1, t2); } public Expression tom_get_slot_IfThenElse_condition(Instruction t) { return  t.getCondition(); } public TomList tom_get_slot_IfThenElse_succesList(Instruction t) { return  t.getSuccesList(); } public TomList tom_get_slot_IfThenElse_failureList(Instruction t) { return  t.getFailureList(); }  public boolean tom_is_fun_sym_DoWhile(Instruction t) { return  (t!= null) &&t.isDoWhile(); } public Instruction tom_make_DoWhile(TomList t0, Expression t1) { return  getTomSignatureFactory().makeInstruction_DoWhile(t0, t1); } public TomList tom_get_slot_DoWhile_instList(Instruction t) { return  t.getInstList(); } public Expression tom_get_slot_DoWhile_condition(Instruction t) { return  t.getCondition(); }  public boolean tom_is_fun_sym_Assign(Instruction t) { return  (t!= null) &&t.isAssign(); } public Instruction tom_make_Assign(TomTerm t0, Expression t1) { return  getTomSignatureFactory().makeInstruction_Assign(t0, t1); } public TomTerm tom_get_slot_Assign_kid1(Instruction t) { return  t.getKid1(); } public Expression tom_get_slot_Assign_source(Instruction t) { return  t.getSource(); }  public boolean tom_is_fun_sym_Increment(Instruction t) { return  (t!= null) &&t.isIncrement(); } public Instruction tom_make_Increment(TomTerm t0) { return  getTomSignatureFactory().makeInstruction_Increment(t0); } public TomTerm tom_get_slot_Increment_kid1(Instruction t) { return  t.getKid1(); }  public boolean tom_is_fun_sym_Action(Instruction t) { return  (t!= null) &&t.isAction(); } public Instruction tom_make_Action(TomList t0) { return  getTomSignatureFactory().makeInstruction_Action(t0); } public TomList tom_get_slot_Action_instList(Instruction t) { return  t.getInstList(); }  public boolean tom_is_fun_sym_ExitAction(Instruction t) { return  (t!= null) &&t.isExitAction(); } public Instruction tom_make_ExitAction(TomList t0) { return  getTomSignatureFactory().makeInstruction_ExitAction(t0); } public TomList tom_get_slot_ExitAction_numberList(Instruction t) { return  t.getNumberList(); }  public boolean tom_is_fun_sym_Return(Instruction t) { return  (t!= null) &&t.isReturn(); } public Instruction tom_make_Return(TomTerm t0) { return  getTomSignatureFactory().makeInstruction_Return(t0); } public TomTerm tom_get_slot_Return_kid1(Instruction t) { return  t.getKid1(); }  public boolean tom_is_fun_sym_OpenBlock(Instruction t) { return  (t!= null) &&t.isOpenBlock(); } public Instruction tom_make_OpenBlock() { return  getTomSignatureFactory().makeInstruction_OpenBlock(); }  public boolean tom_is_fun_sym_CloseBlock(Instruction t) { return  (t!= null) &&t.isCloseBlock(); } public Instruction tom_make_CloseBlock() { return  getTomSignatureFactory().makeInstruction_CloseBlock(); }  public boolean tom_is_fun_sym_NamedBlock(Instruction t) { return  (t!= null) &&t.isNamedBlock(); } public Instruction tom_make_NamedBlock(String t0, TomList t1) { return  getTomSignatureFactory().makeInstruction_NamedBlock(t0, t1); } public String tom_get_slot_NamedBlock_blockName(Instruction t) { return  t.getBlockName(); } public TomList tom_get_slot_NamedBlock_instList(Instruction t) { return  t.getInstList(); }  public Object tom_get_fun_sym_TomSymbol(TomSymbol t) { return null; } public boolean tom_cmp_fun_sym_TomSymbol(Object s1, Object s2) { return  false; } public Object tom_get_subterm_TomSymbol(TomSymbol t, int n) { return null; } public boolean tom_terms_equal_TomSymbol(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_Symbol(TomSymbol t) { return  (t!= null) &&t.isSymbol(); } public TomSymbol tom_make_Symbol(TomName t0, TomType t1, SlotList t2, Option t3, TargetLanguage t4) { return  getTomSignatureFactory().makeTomSymbol_Symbol(t0, t1, t2, t3, t4); } public TomName tom_get_slot_Symbol_astName(TomSymbol t) { return  t.getAstName(); } public TomType tom_get_slot_Symbol_typesToType(TomSymbol t) { return  t.getTypesToType(); } public SlotList tom_get_slot_Symbol_slotList(TomSymbol t) { return  t.getSlotList(); } public Option tom_get_slot_Symbol_option(TomSymbol t) { return  t.getOption(); } public TargetLanguage tom_get_slot_Symbol_tlCode(TomSymbol t) { return  t.getTlCode(); }  public Object tom_get_fun_sym_SlotList(SlotList t) { return null; } public boolean tom_cmp_fun_sym_SlotList(Object s1, Object s2) { return  false; } public Object tom_get_subterm_SlotList(SlotList t, int n) { return null; } public boolean tom_terms_equal_SlotList(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_EmptySlotList(SlotList t) { return  (t!= null) &&t.isEmptySlotList(); } public SlotList tom_make_EmptySlotList() { return  getTomSignatureFactory().makeSlotList_EmptySlotList(); }  public boolean tom_is_fun_sym_ConsSlotList(SlotList t) { return  (t!= null) &&t.isConsSlotList(); } public SlotList tom_make_ConsSlotList(PairNameDecl t0, SlotList t1) { return  getTomSignatureFactory().makeSlotList_ConsSlotList(t0, t1); } public PairNameDecl tom_get_slot_ConsSlotList_headSlotList(SlotList t) { return  t.getHeadSlotList(); } public SlotList tom_get_slot_ConsSlotList_tailSlotList(SlotList t) { return  t.getTailSlotList(); }  public Object tom_get_fun_sym_PairNameDecl(PairNameDecl t) { return null; } public boolean tom_cmp_fun_sym_PairNameDecl(Object s1, Object s2) { return  false; } public Object tom_get_subterm_PairNameDecl(PairNameDecl t, int n) { return null; } public boolean tom_terms_equal_PairNameDecl(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_Slot(PairNameDecl t) { return  (t!= null) &&t.isSlot(); } public PairNameDecl tom_make_Slot(TomName t0, Declaration t1) { return  getTomSignatureFactory().makePairNameDecl_Slot(t0, t1); } public TomName tom_get_slot_Slot_slotName(PairNameDecl t) { return  t.getSlotName(); } public Declaration tom_get_slot_Slot_slotDecl(PairNameDecl t) { return  t.getSlotDecl(); }  public Object tom_get_fun_sym_TomSymbolTable(TomSymbolTable t) { return null; } public boolean tom_cmp_fun_sym_TomSymbolTable(Object s1, Object s2) { return  false; } public Object tom_get_subterm_TomSymbolTable(TomSymbolTable t, int n) { return null; } public boolean tom_terms_equal_TomSymbolTable(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_Table(TomSymbolTable t) { return  (t!= null) &&t.isTable(); } public TomSymbolTable tom_make_Table(TomEntryList t0) { return  getTomSignatureFactory().makeTomSymbolTable_Table(t0); } public TomEntryList tom_get_slot_Table_entryList(TomSymbolTable t) { return  t.getEntryList(); }  public Object tom_get_fun_sym_TomEntryList(TomEntryList t) { return null; } public boolean tom_cmp_fun_sym_TomEntryList(Object s1, Object s2) { return  false; } public Object tom_get_subterm_TomEntryList(TomEntryList t, int n) { return null; } public boolean tom_terms_equal_TomEntryList(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_EmptyEntryList(TomEntryList t) { return  (t!= null) &&t.isEmptyEntryList(); } public TomEntryList tom_make_EmptyEntryList() { return  getTomSignatureFactory().makeTomEntryList_EmptyEntryList(); }  public boolean tom_is_fun_sym_ConsEntryList(TomEntryList t) { return  (t!= null) &&t.isConsEntryList(); } public TomEntryList tom_make_ConsEntryList(TomEntry t0, TomEntryList t1) { return  getTomSignatureFactory().makeTomEntryList_ConsEntryList(t0, t1); } public TomEntry tom_get_slot_ConsEntryList_headEntryList(TomEntryList t) { return  t.getHeadEntryList(); } public TomEntryList tom_get_slot_ConsEntryList_tailEntryList(TomEntryList t) { return  t.getTailEntryList(); }  public Object tom_get_fun_sym_TomEntry(TomEntry t) { return null; } public boolean tom_cmp_fun_sym_TomEntry(Object s1, Object s2) { return  false; } public Object tom_get_subterm_TomEntry(TomEntry t, int n) { return null; } public boolean tom_terms_equal_TomEntry(Object t1, Object t2) { return t1.equals(t2); }  public boolean tom_is_fun_sym_Entry(TomEntry t) { return  (t!= null) &&t.isEntry(); } public TomEntry tom_make_Entry(String t0, TomSymbol t1) { return  getTomSignatureFactory().makeTomEntry_Entry(t0, t1); } public String tom_get_slot_Entry_strName(TomEntry t) { return  t.getStrName(); } public TomSymbol tom_get_slot_Entry_astSymbol(TomEntry t) { return  t.getAstSymbol(); }  
// ------------------------------------------------------------

  protected void debugPrintln(String s) {
    if(debug) {
      System.out.println(s);
    }
  }
  
  protected String getTomType(TomType type) {
     { TomType tom_match1_1 = null; tom_match1_1 = (TomType) type;matchlab_match1_pattern1: { String s = null; if(tom_is_fun_sym_TomType(tom_match1_1)) { String tom_match1_1_1 = null; tom_match1_1_1 = (String) tom_get_slot_TomType_string(tom_match1_1); s = (String) tom_match1_1_1;
 return s; }}matchlab_match1_pattern2: { String s = null; if(tom_is_fun_sym_TomTypeAlone(tom_match1_1)) { String tom_match1_1_1 = null; tom_match1_1_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match1_1); s = (String) tom_match1_1_1;
 return s; }}matchlab_match1_pattern3: { String s = null; if(tom_is_fun_sym_Type(tom_match1_1)) { TomType tom_match1_1_1 = null; TomType tom_match1_1_2 = null; tom_match1_1_1 = (TomType) tom_get_slot_Type_tomType(tom_match1_1); tom_match1_1_2 = (TomType) tom_get_slot_Type_tlType(tom_match1_1); if(tom_is_fun_sym_TomType(tom_match1_1_1)) { String tom_match1_1_1_1 = null; tom_match1_1_1_1 = (String) tom_get_slot_TomType_string(tom_match1_1_1); s = (String) tom_match1_1_1_1;
 return s; } }}matchlab_match1_pattern4: { if(tom_is_fun_sym_EmptyType(tom_match1_1)) {
 return null; }}matchlab_match1_pattern5: {
 System.out.println("getTomType error on term: " + type);
      System.exit(1);
      } }
 
    return null;
  }

  protected String getTLType(TomType type) {
    return getTLCode(type.getTlType());
  }

  protected String getTLCode(TomType type) {
     { TomType tom_match2_1 = null; tom_match2_1 = (TomType) type;matchlab_match2_pattern1: { String tlType = null; if(tom_is_fun_sym_TLType(tom_match2_1)) { TargetLanguage tom_match2_1_1 = null; tom_match2_1_1 = (TargetLanguage) tom_get_slot_TLType_tl(tom_match2_1); if(tom_is_fun_sym_TL(tom_match2_1_1)) { String tom_match2_1_1_1 = null; Position tom_match2_1_1_2 = null; Position tom_match2_1_1_3 = null; tom_match2_1_1_1 = (String) tom_get_slot_TL_code(tom_match2_1_1); tom_match2_1_1_2 = (Position) tom_get_slot_TL_start(tom_match2_1_1); tom_match2_1_1_3 = (Position) tom_get_slot_TL_end(tom_match2_1_1); tlType = (String) tom_match2_1_1_1;
  return tlType;  } }}matchlab_match2_pattern2: { String tlType = null; if(tom_is_fun_sym_TLType(tom_match2_1)) { TargetLanguage tom_match2_1_1 = null; tom_match2_1_1 = (TargetLanguage) tom_get_slot_TLType_tl(tom_match2_1); if(tom_is_fun_sym_ITL(tom_match2_1_1)) { String tom_match2_1_1_1 = null; tom_match2_1_1_1 = (String) tom_get_slot_ITL_code(tom_match2_1_1); tlType = (String) tom_match2_1_1_1;
  return tlType;  } }}matchlab_match2_pattern3: {
 
        System.out.println("getTLCode error on term: " + type);
        System.exit(1);
      } }
 
    return null;
  }

  
  protected TomType getSymbolCodomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getCodomain();
    } else {
        //System.out.println("getSymbolCodomain: symbol = " + symbol);
      return tom_make_EmptyType() ;
    }
  }   

  protected TomList getSymbolDomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getList();
    } else {
        //System.out.println("getSymbolDomain: symbol = " + symbol);
      return empty();
    }
  }

  protected String getSymbolCode(TomSymbol symbol) {
      //%variable
     { TomSymbol tom_match3_1 = null; tom_match3_1 = (TomSymbol) symbol;matchlab_match3_pattern1: { String tlCode = null; if(tom_is_fun_sym_Symbol(tom_match3_1)) { TomName tom_match3_1_1 = null; TomType tom_match3_1_2 = null; SlotList tom_match3_1_3 = null; Option tom_match3_1_4 = null; TargetLanguage tom_match3_1_5 = null; tom_match3_1_1 = (TomName) tom_get_slot_Symbol_astName(tom_match3_1); tom_match3_1_2 = (TomType) tom_get_slot_Symbol_typesToType(tom_match3_1); tom_match3_1_3 = (SlotList) tom_get_slot_Symbol_slotList(tom_match3_1); tom_match3_1_4 = (Option) tom_get_slot_Symbol_option(tom_match3_1); tom_match3_1_5 = (TargetLanguage) tom_get_slot_Symbol_tlCode(tom_match3_1); if(tom_is_fun_sym_TL(tom_match3_1_5)) { String tom_match3_1_5_1 = null; Position tom_match3_1_5_2 = null; Position tom_match3_1_5_3 = null; tom_match3_1_5_1 = (String) tom_get_slot_TL_code(tom_match3_1_5); tom_match3_1_5_2 = (Position) tom_get_slot_TL_start(tom_match3_1_5); tom_match3_1_5_3 = (Position) tom_get_slot_TL_end(tom_match3_1_5); tlCode = (String) tom_match3_1_5_1;
  return tlCode;  } }}matchlab_match3_pattern2: { String tlCode = null; if(tom_is_fun_sym_Symbol(tom_match3_1)) { TomName tom_match3_1_1 = null; TomType tom_match3_1_2 = null; SlotList tom_match3_1_3 = null; Option tom_match3_1_4 = null; TargetLanguage tom_match3_1_5 = null; tom_match3_1_1 = (TomName) tom_get_slot_Symbol_astName(tom_match3_1); tom_match3_1_2 = (TomType) tom_get_slot_Symbol_typesToType(tom_match3_1); tom_match3_1_3 = (SlotList) tom_get_slot_Symbol_slotList(tom_match3_1); tom_match3_1_4 = (Option) tom_get_slot_Symbol_option(tom_match3_1); tom_match3_1_5 = (TargetLanguage) tom_get_slot_Symbol_tlCode(tom_match3_1); if(tom_is_fun_sym_ITL(tom_match3_1_5)) { String tom_match3_1_5_1 = null; tom_match3_1_5_1 = (String) tom_get_slot_ITL_code(tom_match3_1_5); tlCode = (String) tom_match3_1_5_1;
  return tlCode;  } }}matchlab_match3_pattern3: {
 
        System.out.println("getSymbolCode error on term: " + symbol);
        System.exit(1);
      } }
 
    return null;
  }

  protected TomType getTermType(TomTerm t) {
      //%variable
     { TomTerm tom_match4_1 = null; tom_match4_1 = (TomTerm) t;matchlab_match4_pattern1: { TomList subterms = null; String tomName = null; Option option = null; if(tom_is_fun_sym_Appl(tom_match4_1)) { Option tom_match4_1_1 = null; TomName tom_match4_1_2 = null; TomList tom_match4_1_3 = null; tom_match4_1_1 = (Option) tom_get_slot_Appl_option(tom_match4_1); tom_match4_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match4_1); tom_match4_1_3 = (TomList) tom_get_slot_Appl_args(tom_match4_1); option = (Option) tom_match4_1_1; if(tom_is_fun_sym_Name(tom_match4_1_2)) { String tom_match4_1_2_1 = null; tom_match4_1_2_1 = (String) tom_get_slot_Name_string(tom_match4_1_2); tomName = (String) tom_match4_1_2_1; subterms = (TomList) tom_match4_1_3;
 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        return tomSymbol.getTypesToType().getCodomain();
       } }}matchlab_match4_pattern2: { TomName name = null; TomType type = null; Option option = null; if(tom_is_fun_sym_Variable(tom_match4_1)) { Option tom_match4_1_1 = null; TomName tom_match4_1_2 = null; TomType tom_match4_1_3 = null; tom_match4_1_1 = (Option) tom_get_slot_Variable_option(tom_match4_1); tom_match4_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match4_1); tom_match4_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match4_1); option = (Option) tom_match4_1_1; name = (TomName) tom_match4_1_2; type = (TomType) tom_match4_1_3;

  return type;  }}matchlab_match4_pattern3: { TomType type = null; TomName name = null; Option option = null; if(tom_is_fun_sym_VariableStar(tom_match4_1)) { Option tom_match4_1_1 = null; TomName tom_match4_1_2 = null; TomType tom_match4_1_3 = null; tom_match4_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match4_1); tom_match4_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match4_1); tom_match4_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match4_1); option = (Option) tom_match4_1_1; name = (TomName) tom_match4_1_2; type = (TomType) tom_match4_1_3;

  return type;  }}matchlab_match4_pattern4: { TomType type = null; Option option = null; if(tom_is_fun_sym_UnamedVariable(tom_match4_1)) { Option tom_match4_1_1 = null; TomType tom_match4_1_2 = null; tom_match4_1_1 = (Option) tom_get_slot_UnamedVariable_option(tom_match4_1); tom_match4_1_2 = (TomType) tom_get_slot_UnamedVariable_astType(tom_match4_1); option = (Option) tom_match4_1_1; type = (TomType) tom_match4_1_2;

  return type;  }}matchlab_match4_pattern5: {

 
        System.out.println("getTermType error on term: " + t);
        System.exit(1);
      } }
 
    return null;
  }

  private HashMap numberListToIdentifierMap = new HashMap();

  private String elementToIdentifier(TomTerm subject) {
     { TomTerm tom_match5_1 = null; tom_match5_1 = (TomTerm) subject;matchlab_match5_pattern1: { Integer i = null; if(tom_is_fun_sym_Begin(tom_match5_1)) { TomTerm tom_match5_1_1 = null; tom_match5_1_1 = (TomTerm) tom_get_slot_Begin_number(tom_match5_1); if(tom_is_fun_sym_Number(tom_match5_1_1)) { Integer tom_match5_1_1_1 = null; tom_match5_1_1_1 = (Integer) tom_get_slot_Number_integer(tom_match5_1_1); i = (Integer) tom_match5_1_1_1;
  return "begin" + i;  } }}matchlab_match5_pattern2: { Integer i = null; if(tom_is_fun_sym_End(tom_match5_1)) { TomTerm tom_match5_1_1 = null; tom_match5_1_1 = (TomTerm) tom_get_slot_End_number(tom_match5_1); if(tom_is_fun_sym_Number(tom_match5_1_1)) { Integer tom_match5_1_1_1 = null; tom_match5_1_1_1 = (Integer) tom_get_slot_Number_integer(tom_match5_1_1); i = (Integer) tom_match5_1_1_1;
  return "end" + i;  } }}matchlab_match5_pattern3: { Integer i = null; if(tom_is_fun_sym_MatchNumber(tom_match5_1)) { TomTerm tom_match5_1_1 = null; tom_match5_1_1 = (TomTerm) tom_get_slot_MatchNumber_number(tom_match5_1); if(tom_is_fun_sym_Number(tom_match5_1_1)) { Integer tom_match5_1_1_1 = null; tom_match5_1_1_1 = (Integer) tom_get_slot_Number_integer(tom_match5_1_1); i = (Integer) tom_match5_1_1_1;
  return "match" + i;  } }}matchlab_match5_pattern4: { Integer i = null; if(tom_is_fun_sym_PatternNumber(tom_match5_1)) { TomTerm tom_match5_1_1 = null; tom_match5_1_1 = (TomTerm) tom_get_slot_PatternNumber_number(tom_match5_1); if(tom_is_fun_sym_Number(tom_match5_1_1)) { Integer tom_match5_1_1_1 = null; tom_match5_1_1_1 = (Integer) tom_get_slot_Number_integer(tom_match5_1_1); i = (Integer) tom_match5_1_1_1;
  return "pattern" + i;  } }}matchlab_match5_pattern5: { Integer i = null; if(tom_is_fun_sym_ListNumber(tom_match5_1)) { TomTerm tom_match5_1_1 = null; tom_match5_1_1 = (TomTerm) tom_get_slot_ListNumber_number(tom_match5_1); if(tom_is_fun_sym_Number(tom_match5_1_1)) { Integer tom_match5_1_1_1 = null; tom_match5_1_1_1 = (Integer) tom_get_slot_Number_integer(tom_match5_1_1); i = (Integer) tom_match5_1_1_1;
  return "list" + i;  } }}matchlab_match5_pattern6: { Integer i = null; if(tom_is_fun_sym_IndexNumber(tom_match5_1)) { TomTerm tom_match5_1_1 = null; tom_match5_1_1 = (TomTerm) tom_get_slot_IndexNumber_number(tom_match5_1); if(tom_is_fun_sym_Number(tom_match5_1_1)) { Integer tom_match5_1_1_1 = null; tom_match5_1_1_1 = (Integer) tom_get_slot_Number_integer(tom_match5_1_1); i = (Integer) tom_match5_1_1_1;
  return "index" + i;  } }}matchlab_match5_pattern7: { Integer i = null; if(tom_is_fun_sym_AbsVar(tom_match5_1)) { TomTerm tom_match5_1_1 = null; tom_match5_1_1 = (TomTerm) tom_get_slot_AbsVar_number(tom_match5_1); if(tom_is_fun_sym_Number(tom_match5_1_1)) { Integer tom_match5_1_1_1 = null; tom_match5_1_1_1 = (Integer) tom_get_slot_Number_integer(tom_match5_1_1); i = (Integer) tom_match5_1_1_1;
  return "absvar" + i;  } }}matchlab_match5_pattern8: { String name = null; if(tom_is_fun_sym_RenamedVar(tom_match5_1)) { TomName tom_match5_1_1 = null; tom_match5_1_1 = (TomName) tom_get_slot_RenamedVar_astName(tom_match5_1); if(tom_is_fun_sym_Name(tom_match5_1_1)) { String tom_match5_1_1_1 = null; tom_match5_1_1_1 = (String) tom_get_slot_Name_string(tom_match5_1_1); name = (String) tom_match5_1_1_1;
  return "renamedvar_" + name;  } }}matchlab_match5_pattern9: { if(tom_is_fun_sym_RuleVar(tom_match5_1)) {
  return "rulevar";  }}matchlab_match5_pattern10: { Integer i = null; if(tom_is_fun_sym_Number(tom_match5_1)) { Integer tom_match5_1_1 = null; tom_match5_1_1 = (Integer) tom_get_slot_Number_integer(tom_match5_1); i = (Integer) tom_match5_1_1;
  return "" + i;  }}matchlab_match5_pattern11: {
  return subject.toString(); } }
 
  }
  
  protected String numberListToIdentifier(TomList l) {
    String res = (String)numberListToIdentifierMap.get(l);
    if(res == null) {
      TomList key = l;
      StringBuffer buf = new StringBuffer(30);
      while(!l.isEmpty()) {
        buf.append("_" + elementToIdentifier(l.getHead()));
        l = l.getTail();
      }
      res = buf.toString();
      numberListToIdentifierMap.put(key,res);
    }
    return res;
  }

  protected boolean isListOperator(TomSymbol subject) {
    //%variable
    if(subject==null) {
      return false;
    }
     { TomSymbol tom_match6_1 = null; tom_match6_1 = (TomSymbol) subject;matchlab_match6_pattern1: { OptionList optionList = null; if(tom_is_fun_sym_Symbol(tom_match6_1)) { TomName tom_match6_1_1 = null; TomType tom_match6_1_2 = null; SlotList tom_match6_1_3 = null; Option tom_match6_1_4 = null; TargetLanguage tom_match6_1_5 = null; tom_match6_1_1 = (TomName) tom_get_slot_Symbol_astName(tom_match6_1); tom_match6_1_2 = (TomType) tom_get_slot_Symbol_typesToType(tom_match6_1); tom_match6_1_3 = (SlotList) tom_get_slot_Symbol_slotList(tom_match6_1); tom_match6_1_4 = (Option) tom_get_slot_Symbol_option(tom_match6_1); tom_match6_1_5 = (TargetLanguage) tom_get_slot_Symbol_tlCode(tom_match6_1); if(tom_is_fun_sym_Option(tom_match6_1_4)) { OptionList tom_match6_1_4_1 = null; tom_match6_1_4_1 = (OptionList) tom_get_slot_Option_optionList(tom_match6_1_4); optionList = (OptionList) tom_match6_1_4_1;
 
        while(!optionList.isEmptyOptionList()) {
          Option opt = optionList.getHead();
           { Option tom_match7_1 = null; tom_match7_1 = (Option) opt;matchlab_match7_pattern1: { if(tom_is_fun_sym_DeclarationToOption(tom_match7_1)) { Declaration tom_match7_1_1 = null; tom_match7_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match7_1); if(tom_is_fun_sym_MakeEmptyList(tom_match7_1_1)) { TomName tom_match7_1_1_1 = null; TargetLanguage tom_match7_1_1_2 = null; Option tom_match7_1_1_3 = null; tom_match7_1_1_1 = (TomName) tom_get_slot_MakeEmptyList_astName(tom_match7_1_1); tom_match7_1_1_2 = (TargetLanguage) tom_get_slot_MakeEmptyList_tlCode(tom_match7_1_1); tom_match7_1_1_3 = (Option) tom_get_slot_MakeEmptyList_orgTrack(tom_match7_1_1);
  return true;  } }}matchlab_match7_pattern2: { if(tom_is_fun_sym_DeclarationToOption(tom_match7_1)) { Declaration tom_match7_1_1 = null; tom_match7_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match7_1); if(tom_is_fun_sym_MakeAddList(tom_match7_1_1)) { TomName tom_match7_1_1_1 = null; TomTerm tom_match7_1_1_2 = null; TomTerm tom_match7_1_1_3 = null; TargetLanguage tom_match7_1_1_4 = null; Option tom_match7_1_1_5 = null; tom_match7_1_1_1 = (TomName) tom_get_slot_MakeAddList_astName(tom_match7_1_1); tom_match7_1_1_2 = (TomTerm) tom_get_slot_MakeAddList_varElt(tom_match7_1_1); tom_match7_1_1_3 = (TomTerm) tom_get_slot_MakeAddList_varList(tom_match7_1_1); tom_match7_1_1_4 = (TargetLanguage) tom_get_slot_MakeAddList_tlCode(tom_match7_1_1); tom_match7_1_1_5 = (Option) tom_get_slot_MakeAddList_orgTrack(tom_match7_1_1);
  return true;  } }} }
 
          optionList = optionList.getTail();
        }
        return false;
       } }}matchlab_match6_pattern2: {

 
        System.out.println("isListOperator: strange case: '" + subject + "'");
        System.exit(1);
      } }
 
    return false;
  }

  protected boolean isArrayOperator(TomSymbol subject) {
    //%variable
    if(subject==null) {
      return false;
    }
     { TomSymbol tom_match8_1 = null; tom_match8_1 = (TomSymbol) subject;matchlab_match8_pattern1: { OptionList optionList = null; if(tom_is_fun_sym_Symbol(tom_match8_1)) { TomName tom_match8_1_1 = null; TomType tom_match8_1_2 = null; SlotList tom_match8_1_3 = null; Option tom_match8_1_4 = null; TargetLanguage tom_match8_1_5 = null; tom_match8_1_1 = (TomName) tom_get_slot_Symbol_astName(tom_match8_1); tom_match8_1_2 = (TomType) tom_get_slot_Symbol_typesToType(tom_match8_1); tom_match8_1_3 = (SlotList) tom_get_slot_Symbol_slotList(tom_match8_1); tom_match8_1_4 = (Option) tom_get_slot_Symbol_option(tom_match8_1); tom_match8_1_5 = (TargetLanguage) tom_get_slot_Symbol_tlCode(tom_match8_1); if(tom_is_fun_sym_Option(tom_match8_1_4)) { OptionList tom_match8_1_4_1 = null; tom_match8_1_4_1 = (OptionList) tom_get_slot_Option_optionList(tom_match8_1_4); optionList = (OptionList) tom_match8_1_4_1;
 
        while(!optionList.isEmptyOptionList()) {
          Option opt = optionList.getHead();
           { Option tom_match9_1 = null; tom_match9_1 = (Option) opt;matchlab_match9_pattern1: { if(tom_is_fun_sym_DeclarationToOption(tom_match9_1)) { Declaration tom_match9_1_1 = null; tom_match9_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match9_1); if(tom_is_fun_sym_MakeEmptyArray(tom_match9_1_1)) { TomName tom_match9_1_1_1 = null; TomTerm tom_match9_1_1_2 = null; TargetLanguage tom_match9_1_1_3 = null; Option tom_match9_1_1_4 = null; tom_match9_1_1_1 = (TomName) tom_get_slot_MakeEmptyArray_astName(tom_match9_1_1); tom_match9_1_1_2 = (TomTerm) tom_get_slot_MakeEmptyArray_varSize(tom_match9_1_1); tom_match9_1_1_3 = (TargetLanguage) tom_get_slot_MakeEmptyArray_tlCode(tom_match9_1_1); tom_match9_1_1_4 = (Option) tom_get_slot_MakeEmptyArray_orgTrack(tom_match9_1_1);
  return true;  } }}matchlab_match9_pattern2: { if(tom_is_fun_sym_DeclarationToOption(tom_match9_1)) { Declaration tom_match9_1_1 = null; tom_match9_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match9_1); if(tom_is_fun_sym_MakeAddArray(tom_match9_1_1)) { TomName tom_match9_1_1_1 = null; TomTerm tom_match9_1_1_2 = null; TomTerm tom_match9_1_1_3 = null; TargetLanguage tom_match9_1_1_4 = null; Option tom_match9_1_1_5 = null; tom_match9_1_1_1 = (TomName) tom_get_slot_MakeAddArray_astName(tom_match9_1_1); tom_match9_1_1_2 = (TomTerm) tom_get_slot_MakeAddArray_varElt(tom_match9_1_1); tom_match9_1_1_3 = (TomTerm) tom_get_slot_MakeAddArray_varList(tom_match9_1_1); tom_match9_1_1_4 = (TargetLanguage) tom_get_slot_MakeAddArray_tlCode(tom_match9_1_1); tom_match9_1_1_5 = (Option) tom_get_slot_MakeAddArray_orgTrack(tom_match9_1_1);
  return true;  } }} }
 
          optionList = optionList.getTail();
        }
        return false;
       } }}matchlab_match8_pattern2: {

 
        System.out.println("isArrayOperator: strange case: '" + subject + "'");
        System.exit(1);
      } }
 
    return false;
  }

  protected boolean isIntegerOperator(TomSymbol subject) {
    if(subject==null) {
      return false;
    }
    return isIntType(getTomType(getSymbolCodomain(subject)));
  }

  protected TomList tomListMap(TomList subject, Replace1 replace) {
    TomList res = subject;
    try {
      if(!subject.isEmpty()) {
        TomTerm term = (TomTerm) replace.apply(subject.getHead());
        TomList list = tomListMap(subject.getTail(),replace);
        res = cons(term,list);
      }
    } catch(Exception e) {
      System.out.println("tomListMap error: " + e);
      e.printStackTrace();
      System.exit(0);
    }
    return res;
  }
  
    // ------------------------------------------------------------
  protected void collectVariable(final Collection collection, TomTerm subject) {
    Collect collect = new Collect() { 
        public boolean apply(ATerm t) {
            //%variable
          if(t instanceof TomTerm) {
            TomTerm annotedVariable = null;
             { TomTerm tom_match10_1 = null; tom_match10_1 = (TomTerm) t;matchlab_match10_pattern1: { OptionList optionList = null; if(tom_is_fun_sym_Variable(tom_match10_1)) { Option tom_match10_1_1 = null; TomName tom_match10_1_2 = null; TomType tom_match10_1_3 = null; tom_match10_1_1 = (Option) tom_get_slot_Variable_option(tom_match10_1); tom_match10_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match10_1); tom_match10_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match10_1); if(tom_is_fun_sym_Option(tom_match10_1_1)) { OptionList tom_match10_1_1_1 = null; tom_match10_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match10_1_1); optionList = (OptionList) tom_match10_1_1_1;
 
                collection.add(t);
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match10_pattern2: { OptionList optionList = null; if(tom_is_fun_sym_VariableStar(tom_match10_1)) { Option tom_match10_1_1 = null; TomName tom_match10_1_2 = null; TomType tom_match10_1_3 = null; tom_match10_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match10_1); tom_match10_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match10_1); tom_match10_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match10_1); if(tom_is_fun_sym_Option(tom_match10_1_1)) { OptionList tom_match10_1_1_1 = null; tom_match10_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match10_1_1); optionList = (OptionList) tom_match10_1_1_1;

 
                collection.add(t);
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match10_pattern3: { OptionList optionList = null; if(tom_is_fun_sym_UnamedVariable(tom_match10_1)) { Option tom_match10_1_1 = null; TomType tom_match10_1_2 = null; tom_match10_1_1 = (Option) tom_get_slot_UnamedVariable_option(tom_match10_1); tom_match10_1_2 = (TomType) tom_get_slot_UnamedVariable_astType(tom_match10_1); if(tom_is_fun_sym_Option(tom_match10_1_1)) { OptionList tom_match10_1_1_1 = null; tom_match10_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match10_1_1); optionList = (OptionList) tom_match10_1_1_1;

 
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match10_pattern4: { OptionList optionList = null; TomList subterms = null; if(tom_is_fun_sym_Appl(tom_match10_1)) { Option tom_match10_1_1 = null; TomName tom_match10_1_2 = null; TomList tom_match10_1_3 = null; tom_match10_1_1 = (Option) tom_get_slot_Appl_option(tom_match10_1); tom_match10_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match10_1); tom_match10_1_3 = (TomList) tom_get_slot_Appl_args(tom_match10_1); if(tom_is_fun_sym_Option(tom_match10_1_1)) { OptionList tom_match10_1_1_1 = null; tom_match10_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match10_1_1); optionList = (OptionList) tom_match10_1_1_1; subterms = (TomList) tom_match10_1_3;


 
                collectVariable(collection,tom_make_Tom(subterms) );
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match10_pattern5: {

  return true; } }
 
          } else {
            return true;
          }
        } // end apply
      }; // end new
    
    genericCollect(subject, collect); 
  }

  protected TomTerm getAnnotedVariable(OptionList subjectList) {
      //%variable
    while(!subjectList.isEmptyOptionList()) {
      Option subject = subjectList.getHead();
       { Option tom_match11_1 = null; tom_match11_1 = (Option) subject;matchlab_match11_pattern1: { TomName name = null; TomTerm var = null; TomType type = null; Option option = null; if(tom_is_fun_sym_TomTermToOption(tom_match11_1)) { TomTerm tom_match11_1_1 = null; tom_match11_1_1 = (TomTerm) tom_get_slot_TomTermToOption_astTerm(tom_match11_1); if(tom_is_fun_sym_Variable(tom_match11_1_1)) { Option tom_match11_1_1_1 = null; TomName tom_match11_1_1_2 = null; TomType tom_match11_1_1_3 = null; tom_match11_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match11_1_1); tom_match11_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match11_1_1); tom_match11_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match11_1_1); var = (TomTerm) tom_match11_1_1; option = (Option) tom_match11_1_1_1; name = (TomName) tom_match11_1_1_2; type = (TomType) tom_match11_1_1_3;
 
          return var;
         } }} }
 
      subjectList = subjectList.getTail();
    }
    return null;
  }
  
  protected Declaration getIsFsymDecl(OptionList optionList) {
    //%variable
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
       { Option tom_match12_1 = null; tom_match12_1 = (Option) subject;matchlab_match12_pattern1: { Declaration decl = null; if(tom_is_fun_sym_DeclarationToOption(tom_match12_1)) { Declaration tom_match12_1_1 = null; tom_match12_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match12_1); if(tom_is_fun_sym_IsFsymDecl(tom_match12_1_1)) { TomName tom_match12_1_1_1 = null; TomTerm tom_match12_1_1_2 = null; TargetLanguage tom_match12_1_1_3 = null; Option tom_match12_1_1_4 = null; tom_match12_1_1_1 = (TomName) tom_get_slot_IsFsymDecl_astName(tom_match12_1_1); tom_match12_1_1_2 = (TomTerm) tom_get_slot_IsFsymDecl_term(tom_match12_1_1); tom_match12_1_1_3 = (TargetLanguage) tom_get_slot_IsFsymDecl_tlCode(tom_match12_1_1); tom_match12_1_1_4 = (Option) tom_get_slot_IsFsymDecl_orgTrack(tom_match12_1_1); decl = (Declaration) tom_match12_1_1;
  return decl;  } }} }
 
      optionList = optionList.getTail();
    }
    return null;
  }

  protected boolean hasConstructor(OptionList optionList) {
      //%variable
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
       { Option tom_match13_1 = null; tom_match13_1 = (Option) subject;matchlab_match13_pattern1: { if(tom_is_fun_sym_Constructor(tom_match13_1)) { TomName tom_match13_1_1 = null; tom_match13_1_1 = (TomName) tom_get_slot_Constructor_astName(tom_match13_1);
  return true;  }} }
 
      optionList = optionList.getTail();
    }
    return false;
    }

  protected TomName getSlotName(TomSymbol symbol, int number) {
    //%variable
    SlotList slotList = symbol.getSlotList();
    for(int index = 0; !slotList.isEmptySlotList() && index<number ; index++) {
      slotList = slotList.getTailSlotList();
    }
    if(slotList.isEmptySlotList()) {
      System.out.println("getSlotName: bad index error");
      System.exit(0);
    }

    Declaration decl = slotList.getHeadSlotList().getSlotDecl();
     { Declaration tom_match14_1 = null; tom_match14_1 = (Declaration) decl;matchlab_match14_pattern1: { TomName name = null; if(tom_is_fun_sym_GetSlotDecl(tom_match14_1)) { TomName tom_match14_1_1 = null; TomName tom_match14_1_2 = null; TomTerm tom_match14_1_3 = null; TargetLanguage tom_match14_1_4 = null; Option tom_match14_1_5 = null; tom_match14_1_1 = (TomName) tom_get_slot_GetSlotDecl_astName(tom_match14_1); tom_match14_1_2 = (TomName) tom_get_slot_GetSlotDecl_slotName(tom_match14_1); tom_match14_1_3 = (TomTerm) tom_get_slot_GetSlotDecl_term(tom_match14_1); tom_match14_1_4 = (TargetLanguage) tom_get_slot_GetSlotDecl_tlCode(tom_match14_1); tom_match14_1_5 = (Option) tom_get_slot_GetSlotDecl_orgTrack(tom_match14_1); name = (TomName) tom_match14_1_2;
  return name;  }} }
 
    return null;
  }
  
  protected int getSlotIndex(SlotList slotList, TomName slotName) {
    //%variable
    int index = 0;
    while(!slotList.isEmptySlotList()) {
      TomName name = slotList.getHeadSlotList().getSlotName();
        // System.out.println("index = " + index + " name = " + name);
      if(slotName.equals(name)) {
        return index; 
      }
      slotList = slotList.getTailSlotList();
      index++;
    }
    return -1;
  }

  protected boolean isDefinedSymbol(TomSymbol subject) {
      //%variable
    if(subject==null) {
      return false;
    }
     { TomSymbol tom_match15_1 = null; tom_match15_1 = (TomSymbol) subject;matchlab_match15_pattern1: { TomList typeList = null; TargetLanguage tlCode1 = null; TomType type1 = null; String name1 = null; OptionList optionList = null; if(tom_is_fun_sym_Symbol(tom_match15_1)) { TomName tom_match15_1_1 = null; TomType tom_match15_1_2 = null; SlotList tom_match15_1_3 = null; Option tom_match15_1_4 = null; TargetLanguage tom_match15_1_5 = null; tom_match15_1_1 = (TomName) tom_get_slot_Symbol_astName(tom_match15_1); tom_match15_1_2 = (TomType) tom_get_slot_Symbol_typesToType(tom_match15_1); tom_match15_1_3 = (SlotList) tom_get_slot_Symbol_slotList(tom_match15_1); tom_match15_1_4 = (Option) tom_get_slot_Symbol_option(tom_match15_1); tom_match15_1_5 = (TargetLanguage) tom_get_slot_Symbol_tlCode(tom_match15_1); if(tom_is_fun_sym_Name(tom_match15_1_1)) { String tom_match15_1_1_1 = null; tom_match15_1_1_1 = (String) tom_get_slot_Name_string(tom_match15_1_1); name1 = (String) tom_match15_1_1_1; if(tom_is_fun_sym_TypesToType(tom_match15_1_2)) { TomList tom_match15_1_2_1 = null; TomType tom_match15_1_2_2 = null; tom_match15_1_2_1 = (TomList) tom_get_slot_TypesToType_list(tom_match15_1_2); tom_match15_1_2_2 = (TomType) tom_get_slot_TypesToType_codomain(tom_match15_1_2); typeList = (TomList) tom_match15_1_2_1; type1 = (TomType) tom_match15_1_2_2; if(tom_is_fun_sym_Option(tom_match15_1_4)) { OptionList tom_match15_1_4_1 = null; tom_match15_1_4_1 = (OptionList) tom_get_slot_Option_optionList(tom_match15_1_4); optionList = (OptionList) tom_match15_1_4_1; tlCode1 = (TargetLanguage) tom_match15_1_5;
 
        while(!optionList.isEmptyOptionList()) {
          Option opt = optionList.getHead();
           { Option tom_match16_1 = null; tom_match16_1 = (Option) opt;matchlab_match16_pattern1: { if(tom_is_fun_sym_DefinedSymbol(tom_match16_1)) {
  return true;  }} }
 
          optionList = optionList.getTail();
        }
        return false;
       } } } }}matchlab_match15_pattern2: {

 
        System.out.println("isDefinedSymbol: strange case: '" + subject + "'");
        System.exit(1);
      } }
 
    return false;
  }
  
    // findOriginTracking(_) return the option containing OriginTracking information
  protected Option findOriginTracking(OptionList optionList) {
    if(optionList.isEmptyOptionList()) return ast().makeOption();
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
       { Option tom_match17_1 = null; tom_match17_1 = (Option) subject;matchlab_match17_pattern1: { Option orgTrack = null; if(tom_is_fun_sym_OriginTracking(tom_match17_1)) { TomName tom_match17_1_1 = null; Integer tom_match17_1_2 = null; tom_match17_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match17_1); tom_match17_1_2 = (Integer) tom_get_slot_OriginTracking_line(tom_match17_1); orgTrack = (Option) tom_match17_1;
 
          return orgTrack;
         }} }
 
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTracking:  not found"+optionList);
    System.exit(1);return null;
  }
}
