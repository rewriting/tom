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

  public TomList makeList(ArrayList argumentList) {
    TomList list = tsf().makeTomList_Empty();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      ATerm elt = (ATerm)argumentList.get(i);
      TomTerm term;
      if(elt instanceof TargetLanguage) {
        term = tsf().makeTomTerm_TargetLanguageToTomTerm((TargetLanguage)elt);
      } else if(elt instanceof TomType) {
        term = tsf().makeTomTerm_TomTypeToTomTerm((TomType)elt);
      } else if(elt instanceof Declaration) {
        term = tsf().makeTomTerm_DeclarationToTomTerm((Declaration)elt);
      } else if(elt instanceof Expression) {
        term = tsf().makeTomTerm_ExpressionToTomTerm((Expression)elt);
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

  public TomSymbol makeSymbol(String symbolName, String resultType, ArrayList typeList, String glString) {
    TomName name = tsf().makeTomName_Name(symbolName);
    TomType typesToType =  tsf().makeTomType_TypesToType(makeList(typeList), tsf().makeTomType_TomTypeAlone(resultType));
    Option options = makeOption();
    TargetLanguage code = tsf().makeTargetLanguage_ITL(glString);
    return tsf().makeTomSymbol_Symbol(name,typesToType,options,code);
  }

  public TomSymbol makeSymbol(String symbolName, String resultType, ArrayList typeList, ArrayList optionList, TargetLanguage glFsym) {
    TomName name = tsf().makeTomName_Name(symbolName);
    TomType typesToType =  tsf().makeTomType_TypesToType(makeList(typeList), tsf().makeTomType_TomTypeAlone(resultType));
    Option options = makeOption(makeOptionList(optionList));
    return tsf().makeTomSymbol_Symbol(name,typesToType,options,glFsym);
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

  public Option makeSlotList(ArrayList argList) {
    if(argList.isEmpty()) {
      return null;
    } else {
      return tsf().makeOption_SlotList(makeList(argList));
    }
  }

  public Option makeOriginTracking( String name, String line ) {
    return tsf().makeOption_OriginTracking(tsf().makeTomName_Name(name), tsf().makeTomTerm_Line(line));
  }

  public Option makeLRParen( String name ) {
    return tsf().makeOption_LRParen(tsf().makeTomName_Name(name));
  }

  public Declaration makeMakeDecl(String opname, TomType returnType, ArrayList argList, TargetLanguage tlcode) {
    TomName name = tsf().makeTomName_Name(opname);  
    return tsf().makeDeclaration_MakeDecl(name, returnType, makeList(argList), tlcode);
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

    TomSymbol astSymbol = makeSymbol(value,resultType,typeList,optionList,tlFsym);
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
      variable_t,tsf().makeTargetLanguage_ITL("t"));
    getSubterm = tsf().makeDeclaration_GetSubtermDecl(
      variable_t,variable_n,tsf().makeTargetLanguage_ITL("null"));
    cmpFunSym = tsf().makeDeclaration_CompareFunctionSymbolDecl(
      variable_t1,variable_t2,tsf().makeTargetLanguage_ITL("t1==t2"));
    equals = tsf().makeDeclaration_TermsEqualDecl(
      variable_t1,variable_t2,tsf().makeTargetLanguage_ITL("t1==t2"));
    list.add(getFunSym);
    list.add(getSubterm);
    list.add(cmpFunSym);
    list.add(equals);
  }
  
    /*
     * update the symbolTable wrt. to the typeTable
     */
  public void updateSymbol(SymbolTable symbolTable) {
    Iterator it = symbolTable.keySymbolIterator();
    while(it.hasNext()) {
      Object key = it.next();
      TomSymbol symbol = symbolTable.getSymbol((String)key);

        //System.out.println("\nsymbol = " + symbol);
      TomName name     = symbol.getAstName();
      TomList typeList = symbol.getTypesToType().getList();
      TomType type     = symbol.getTypesToType().getCodomain();
      Option  options  = symbol.getOption();
      TargetLanguage tlcode   = symbol.getTlCode();

      if(name==null || typeList==null || type==null || options==null || tlcode==null) {
        System.out.println("ASTFactory: null value");
        System.exit(1);
      }

      TomType newType = symbolTable.getType(type.getString());
      ArrayList newTypeList = new ArrayList();
      while (!typeList.isEmpty()) {
        TomType typeAlone = typeList.getHead().getAstType();
        TomType tmpType = symbolTable.getType(typeAlone.getString());
        TomTerm tmpTerm = tsf().makeTomTerm_TomTypeToTomTerm(tmpType);
        newTypeList.add(tmpTerm);
        typeList = typeList.getTail() ;
      }

      TomType typesToType = tsf().makeTomType_TypesToType(makeList(newTypeList), newType);
      TomSymbol newSymbol = tsf().makeTomSymbol_Symbol(name,typesToType,options,tlcode);
        //System.out.println("newSymbol = " + newSymbol);
      symbolTable.putSymbol((String)key,newSymbol);
    }
  }

    /*
     * update the root of lhs: it becomes a defined symbol
     */
  public void updateDefinedSymbol(SymbolTable symbolTable, TomTerm term) {
      //List result = term.match("Appl(Option(<term>),Name(<term>),<term>)");
    if(term.isAppl() || term.isRecordAppl()) {
      String key         = term.getAstName().getString();
      TomSymbol symbol   = symbolTable.getSymbol(key);
        //System.out.println("symbol = " + symbol);
      TomName name       = symbol.getAstName();
      TomType type       = symbol.getTypesToType();
      OptionList optionList = symbol.getOption().getOptionList();
      TargetLanguage tlcode     = symbol.getTlCode();

      optionList = append(tsf().makeOption_DefinedSymbol(),optionList);
      TomSymbol newSymbol = tsf().makeTomSymbol_Symbol(name,type,makeOption(optionList),tlcode);
        //System.out.println("newSymbol = " + newSymbol);
      symbolTable.putSymbol(key,newSymbol);
    }
  }

  public TargetLanguage reworkTLCode(TargetLanguage code) {
    if(!Flags.pretty){
      String tlCode = code.getCode();
      tlCode = tlCode.replace('\n', ' ');
      tlCode = tlCode.replace('\t', ' ');
      return tsf().makeTargetLanguage_ITL(tlCode);
    } else
      return code;
  }
  
}
