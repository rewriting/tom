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

package jtom.tools;

import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

public class CSTFactory {
  private ATermFactory atermFactory;

  public CSTFactory(ATermFactory atermFactory) {
    this.atermFactory = atermFactory;
  }

  public ATermFactory getFactory() {
    return atermFactory;
  }

   public static AFun
       afun_conc,
       afun_empty, afun_cons, afun_Tom, afun_GLCode, 
       afun_GetFunctionSymbolDecl, afun_GetSubtermDecl, afun_GetSlotDecl,
       afun_IsFsym, afun_IsFsymDecl,
       afun_CompareFunctionSymbolDecl, afun_TermsEqualDecl, afun_GetHeadDecl,
       afun_GetTailDecl, afun_IsEmptyDecl, afun_MakeEmptyList,
       afun_MakeAddList, afun_GetElementDecl, afun_GetSizeDecl,
       afun_MakeEmptyArray, afun_MakeAddArray, afun_MakeDecl,
       afun_MakeTerm, afun_BackQuoteTerm,
       afun_LocalVariable, afun_EndLocalVariable,
       afun_BuildVariable, afun_BuildTerm, afun_FunctionCall,
       afun_Appl, afun_RecordAppl, afun_Pair, afun_Name, afun_SlotName,
       afun_SlotList, afun_DefinedSymbol, afun_Symbol, afun_SymbolDecl,
       afun_Type, afun_TypesToType, afun_Option, afun_Match, 
       afun_MakeFunctionBegin, afun_MakeFunctionEnd, afun_Rule,
       afun_RuleSet, afun_RewriteRule, afun_SubjectList, afun_PatternList,
       afun_TermList, afun_Term, afun_PatternAction, afun_GLVar,
       afun_TomType, afun_TomTypeAlone, afun_GLType, afun_Variable, 
       afun_VariableStar, afun_Placeholder, afun_UnamedVariable,
       afun_CompiledMatch, afun_Automata, afun_IfThenElse,
       afun_DoWhile, afun_Assign, afun_Declaration, afun_Position,
       afun_GetSubterm, afun_GetSlot, afun_GetHead, afun_GetTail,
       afun_GetSize, afun_GetElement, afun_GetSliceList, afun_GetSliceArray,
       afun_EqualFunctionSymbol, afun_Not, afun_And, afun_TrueGL,
       afun_FalseGL, afun_Increment, afun_IsEmptyList, afun_IsEmptyArray,
       afun_Action, afun_ExitAction, afun_Return, afun_OpenBlock,
       afun_CloseBlock, afun_NamedBlock, afun_OriginTracking, afun_Line,
       afun_LRParen, afun_DotTerm;

    public void init() {
	afun_conc = getFactory().makeAFun("conc", 1, false);
	afun_empty = getFactory().makeAFun("empty", 0, false);
	afun_cons = getFactory().makeAFun("cons", 2, false);
	afun_Tom = getFactory().makeAFun("Tom",1,false);
	afun_GLCode = getFactory().makeAFun("GLCode", 1, false);
	afun_GetFunctionSymbolDecl = getFactory().makeAFun("GetFunctionSymbolDecl", 2, false) ;
	afun_GetSubtermDecl = getFactory().makeAFun("GetSubtermDecl", 3, false);
	afun_IsFsym = getFactory().makeAFun("IsFsym", 2, false);
	afun_IsFsymDecl = getFactory().makeAFun("IsFsymDecl", 3, false);
	afun_GetSlotDecl = getFactory().makeAFun("GetSlotDecl", 4, false);
	afun_CompareFunctionSymbolDecl = getFactory().makeAFun("CompareFunctionSymbolDecl", 3, false);
	afun_TermsEqualDecl = getFactory().makeAFun("TermsEqualDecl", 3, false);
	afun_GetHeadDecl = getFactory().makeAFun("GetHeadDecl", 2, false)     ;
	afun_GetTailDecl = getFactory().makeAFun("GetTailDecl", 2, false) ;
	afun_IsEmptyDecl = getFactory().makeAFun("IsEmptyDecl", 2, false) ;
	afun_MakeEmptyList = getFactory().makeAFun("MakeEmptyList", 2, false) ;
	afun_MakeAddList = getFactory().makeAFun("MakeAddList", 4, false) ;
	afun_GetElementDecl = getFactory().makeAFun("GetElementDecl", 3, false) ;
	afun_GetSizeDecl = getFactory().makeAFun("GetSizeDecl", 2, false) ;
	afun_MakeEmptyArray = getFactory().makeAFun("MakeEmptyArray", 3, false) ;
	afun_MakeAddArray = getFactory().makeAFun("MakeAddArray", 5, false);
	afun_MakeDecl = getFactory().makeAFun("MakeDecl", 4, false);
	afun_MakeTerm = getFactory().makeAFun("MakeTerm", 1, false);
	afun_BackQuoteTerm = getFactory().makeAFun("BackQuoteTerm", 1, false);
	afun_LocalVariable = getFactory().makeAFun("LocalVariable", 0, false);
	afun_EndLocalVariable = getFactory().makeAFun("EndLocalVariable", 0, false);
	afun_BuildVariable = getFactory().makeAFun("BuildVariable", 1, false);
	afun_BuildTerm = getFactory().makeAFun("BuildTerm", 2, false);
	afun_FunctionCall = getFactory().makeAFun("FunctionCall", 2, false);
	afun_Appl = getFactory().makeAFun("Appl", 3, false);
	afun_RecordAppl = getFactory().makeAFun("RecordAppl", 3, false);
	afun_Pair = getFactory().makeAFun("Pair", 2, false);
	afun_Name = getFactory().makeAFun("Name", 1, false);
	afun_SlotName = getFactory().makeAFun("SlotName", 1, false);
	afun_SlotList = getFactory().makeAFun("SlotList", 1, false);
	afun_DefinedSymbol = getFactory().makeAFun("DefinedSymbol", 0, false);
	afun_Symbol = getFactory().makeAFun("Symbol", 4, false);
	afun_SymbolDecl = getFactory().makeAFun("SymbolDecl", 1, false);
	afun_Type = getFactory().makeAFun("Type", 2, false);
	afun_TypesToType = getFactory().makeAFun("TypesToType", 2, false);
	afun_Option = getFactory().makeAFun("Option", 1, false);
	afun_Match = getFactory().makeAFun("Match", 2, false);
	afun_MakeFunctionBegin = getFactory().makeAFun("MakeFunctionBegin", 2, false);
	afun_MakeFunctionEnd = getFactory().makeAFun("MakeFunctionEnd", 0, false);
	afun_Rule = getFactory().makeAFun("Rule", 2, false);
	afun_RuleSet = getFactory().makeAFun("RuleSet", 1, false);
	afun_RewriteRule = getFactory().makeAFun("RewriteRule", 2, false);
	afun_SubjectList = getFactory().makeAFun("SubjectList", 1, false);
	afun_PatternList = getFactory().makeAFun("PatternList", 1, false);
	afun_TermList = getFactory().makeAFun("TermList", 1, false);
	afun_Term = getFactory().makeAFun("Term", 1, false);
	afun_PatternAction = getFactory().makeAFun("PatternAction", 2, false);
	afun_GLVar = getFactory().makeAFun("GLVar", 2, false);
	afun_TomType = getFactory().makeAFun("TomType", 1, false);
	afun_TomTypeAlone = getFactory().makeAFun("TomTypeAlone", 1, false);
	afun_GLType = getFactory().makeAFun("GLType", 1, false);
	afun_Variable = getFactory().makeAFun("Variable", 3, false);
	afun_VariableStar = getFactory().makeAFun("VariableStar", 3, false);
	afun_Placeholder = getFactory().makeAFun("Placeholder", 1, false);
	afun_UnamedVariable = getFactory().makeAFun("UnamedVariable", 2, false);
	afun_CompiledMatch = getFactory().makeAFun("CompiledMatch", 2, false);
	afun_Automata = getFactory().makeAFun("Automata", 2, false);
	afun_IfThenElse = getFactory().makeAFun("IfThenElse", 3, false)  ;
	afun_DoWhile = getFactory().makeAFun("DoWhile", 2, false)  ;
	afun_Assign = getFactory().makeAFun("Assign", 2, false)   ;
	afun_Declaration = getFactory().makeAFun("Declaration", 1, false) ;
	afun_Position = getFactory().makeAFun("Position", 1, false)  ;
	afun_GetSubterm = getFactory().makeAFun("GetSubterm", 2, false);
	afun_GetSlot = getFactory().makeAFun("GetSlot", 3, false);
	afun_GetHead = getFactory().makeAFun("GetHead", 1, false);
	afun_GetTail = getFactory().makeAFun("GetTail", 1, false);
	afun_GetSize = getFactory().makeAFun("GetSize", 1, false);
	afun_GetElement = getFactory().makeAFun("GetElement", 2, false);
	afun_GetSliceList = getFactory().makeAFun("GetSliceList", 3, false);
	afun_GetSliceArray = getFactory().makeAFun("GetSliceArray", 4, false);
	afun_EqualFunctionSymbol = getFactory().makeAFun("EqualFunctionSymbol", 2, false) ;
	afun_Not = getFactory().makeAFun("Not", 1, false) ;
	afun_And = getFactory().makeAFun("And", 2, false) ;
	afun_TrueGL = getFactory().makeAFun("TrueGL", 0, false) ;
	afun_FalseGL = getFactory().makeAFun("FalseGL", 0, false) ;
	afun_Increment = getFactory().makeAFun("Increment", 1, false) ;
	afun_IsEmptyList = getFactory().makeAFun("IsEmptyList", 1, false) ;
	afun_IsEmptyArray = getFactory().makeAFun("IsEmptyArray", 2, false) ;
	afun_Action = getFactory().makeAFun("Action", 1, false)   ;
	afun_ExitAction = getFactory().makeAFun("ExitAction", 1, false) ;
	afun_Return = getFactory().makeAFun("Return", 1, false) ;
	afun_OpenBlock = getFactory().makeAFun("OpenBlock", 0, false) ;
	afun_CloseBlock = getFactory().makeAFun("CloseBlock", 0, false) ;
	afun_NamedBlock = getFactory().makeAFun("NamedBlock", 2, false) ;
	afun_OriginTracking = getFactory().makeAFun("OriginTracking", 2, false);
	afun_Line = getFactory().makeAFun("Line", 1, false);
	afun_LRParen = getFactory().makeAFun("LRParen", 1, false);
	afun_DotTerm = getFactory().makeAFun("DotTerm", 2, false);
    }



}
