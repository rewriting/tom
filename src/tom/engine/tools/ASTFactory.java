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
import jtom.adt.*;

public class ASTFactory {
  private TomSignatureFactory tomSignatureFactory;
  
  public ASTFactory(TomSignatureFactory tomSignatureFactory) {
    this.tomSignatureFactory = tomSignatureFactory;
  }

  protected TomSignatureFactory tsf() {
    return tomSignatureFactory;
  }

  protected TomList cons(TomTerm t, TomList l) {
    return tsf().makeTomList_Cons(t,l);
  }

  protected TomList append(TomTerm t, TomList l) {
    if(l.isEmpty()) {
      return cons(t,l);
    } else {
      return cons(l.getHead(), append(t,l.getTail()));
    }
  }

  public TomList makeList(Collection c) {
    TomList list = tsf().makeTomList_Empty();
    Iterator it = c.iterator();
    while(it.hasNext()) {
      ATerm elt = (ATerm) it.next();
      TomTerm term;
      if(elt instanceof TargetLanguage) {
        term = tsf().makeTomTerm_TargetLanguageToTomTerm((TargetLanguage)elt);
      } else if(elt instanceof TomType) {
        term = tsf().makeTomTerm_TomTypeToTomTerm((TomType)elt);
      } else if(elt instanceof Declaration) {
        term = tsf().makeTomTerm_DeclarationToTomTerm((Declaration)elt);
      } else if(elt instanceof Expression) {
        term = tsf().makeTomTerm_ExpressionToTomTerm((Expression)elt);
      } else if(elt instanceof TomName) {
        term = tsf().makeTomTerm_TomNameToTomTerm((TomName)elt);
      } else {
        term = (TomTerm)elt;
      }
      list = cons(term,list);
    }
    return list;
  }

  protected OptionList cons(Option t, OptionList l) {
    return tsf().makeOptionList_ConsOptionList(t,l);
  }

  protected OptionList append(Option t, OptionList l) {
    if(l.isEmptyOptionList()) {
      return cons(t,l);
    } else {
      return cons(l.getHead(), append(t,l.getTail()));
    }
  }

  public OptionList makeOptionList(ArrayList argumentList) {
    OptionList list = tsf().makeOptionList_EmptyOptionList();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      ATerm elt = (ATerm)argumentList.get(i);
      Option term;
      if(elt instanceof TomName) {
        term = tsf().makeOption_TomNameToOption((TomName)elt);
      } else if(elt instanceof Declaration) {
        term = tsf().makeOption_DeclarationToOption((Declaration)elt);
      } else {
        term = (Option)elt;
      }
      list = cons(term,list);
    }
    return list;
  }
  
  public TomTerm makeVariable(String name, String type) {
    return makeVariable(makeOption(), name, type);      
  }

  public TomTerm makeVariable(Option option, String name, String type) {
    return tsf().makeTomTerm_Variable(option, tsf().makeTomName_Name(name), tsf().makeTomType_TomTypeAlone(type));  
  }

  public TomTerm makeVariableStar(Option option, String name, String type) {
    return tsf().makeTomTerm_VariableStar(option, tsf().makeTomName_Name(name), tsf().makeTomType_TomTypeAlone(type));  
  }

  public TomSymbol makeSymbol(String symbolName, String resultType, ArrayList typeList, SlotList slotList,
                              ArrayList optionList, TargetLanguage glFsym) {
    TomName name = tsf().makeTomName_Name(symbolName);
    TomType typesToType =  tsf().makeTomType_TypesToType(makeList(typeList), tsf().makeTomType_TomTypeAlone(resultType));
    Option options = makeOption(makeOptionList(optionList));
    return tsf().makeTomSymbol_Symbol(name,typesToType,slotList,options,glFsym);
  }

  public Option makeOption() {
    OptionList list = tsf().makeOptionList_EmptyOptionList();
    return makeOption(list);
  }

  public Option makeOption(Option arg) {
    OptionList list = tsf().makeOptionList_EmptyOptionList();
    if(arg!= null) {
      list = cons(arg,list);
    }
    return makeOption(list);
  }

  public Option makeOption(Option arg, Option info) {
    OptionList list = tsf().makeOptionList_EmptyOptionList();
    if(arg!= null) {
      list = cons(arg,list);
    }
    list = cons(info,list);
    return makeOption(list);
  }

  public Option makeOption(OptionList argList) {   
    return tsf().makeOption_Option(argList);
  }

  public Option makeOriginTracking( String name, String line ) {
    return tsf().makeOption_OriginTracking(tsf().makeTomName_Name(name), new Integer(line) );
  }
  
  public Declaration makeMakeDecl(String opname, TomType returnType, ArrayList argList, TargetLanguage tlcode, Option orgTrack) {
    TomName name = tsf().makeTomName_Name(opname);  
    return tsf().makeDeclaration_MakeDecl(name, returnType, makeList(argList), tlcode, orgTrack);
  }

  public TomType makeType(String typeNameTom, String typeNametGL) {
    return makeType(typeNameTom,tsf().makeTargetLanguage_ITL(typeNametGL));
  }

  public TomType makeType(String typeNameTom, TargetLanguage tlType) {
    TomType typeTom = tsf().makeTomType_TomType(typeNameTom);
    TomType sortTL  = tsf().makeTomType_TLType(tlType);
    return tsf().makeTomType_Type(typeTom,sortTL);
  }

    /*
     * create an integer symbol
     */
  public void makeIntegerSymbol(SymbolTable symbolTable,
                                String value, ArrayList optionList) {
    String resultType = "int";
    ArrayList typeList = new ArrayList();
    TargetLanguage tlFsym = tsf().makeTargetLanguage_ITL(value);
    SlotList slotList = tsf().makeSlotList_EmptySlotList();
    TomSymbol astSymbol = makeSymbol(value,resultType,typeList,slotList,optionList,tlFsym);
    symbolTable.putSymbol(value,astSymbol);
  }

  public void makeIntegerDecl(ArrayList list) {
    String typeString = "int";
    Declaration getFunSym, getSubterm;
    Declaration cmpFunSym, equals;
    Option option = makeOption();
    TomTerm variable_t = makeVariable(option,"t",typeString);
    TomTerm variable_t1 = makeVariable(option,"t1",typeString);
    TomTerm variable_t2 = makeVariable(option,"t2",typeString);
    TomTerm variable_n = makeVariable(option,"n",typeString);
    getFunSym = tsf().makeDeclaration_GetFunctionSymbolDecl(
      variable_t,tsf().makeTargetLanguage_ITL("t"), option);
    getSubterm = tsf().makeDeclaration_GetSubtermDecl(
      variable_t,variable_n,tsf().makeTargetLanguage_ITL("null"), option);
    cmpFunSym = tsf().makeDeclaration_CompareFunctionSymbolDecl(
      variable_t1,variable_t2,tsf().makeTargetLanguage_ITL("t1==t2"), option);
    equals = tsf().makeDeclaration_TermsEqualDecl(
      variable_t1,variable_t2,tsf().makeTargetLanguage_ITL("t1==t2"), option);
    list.add(getFunSym);
    list.add(getSubterm);
    list.add(cmpFunSym);
    list.add(equals);
  }
  
    /*
     * update the root of lhs: it becomes a defined symbol
     */
  public void updateDefinedSymbol(SymbolTable symbolTable, TomTerm term) {
      //List result = term.match("Appl(Option(<term>),Name(<term>),<term>)");
    if(term.isAppl() || term.isRecordAppl()) {
      String key = term.getAstName().getString();
      TomSymbol symbol = symbolTable.getSymbol(key);
      if (symbol != null) {
        OptionList optionList = symbol.getOption().getOptionList();
        optionList = append(tsf().makeOption_DefinedSymbol(),optionList);
        symbolTable.putSymbol(key,symbol.setOption(makeOption(optionList)));
      }
    }
  }

  public TargetLanguage reworkTLCode(TargetLanguage code) {
    if(!Flags.pretty){
      String tlCode = code.getCode();
      tlCode = tlCode.trim();
      tlCode = tlCode.replace('\n', ' ');
      return tsf().makeTargetLanguage_ITL(tlCode);
    } else
      return code;
  }

  public TomName makeName(String slotName) {
    if(slotName.length()>0)
      return tsf().makeTomName_Name(slotName);
    else
      return tsf().makeTomName_EmptyName();
  }
}
