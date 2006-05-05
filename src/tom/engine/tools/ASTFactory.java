/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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

package tom.engine.tools;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import tom.engine.adt.tomsignature.TomSignatureFactory;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.xml.Constants;
import aterm.ATerm;

public class ASTFactory {
   // Suppresses default constructor, ensuring non-instantiability.
  private ASTFactory() {
  }

  protected static TomSignatureFactory tsf() {
		return tom.engine.adt.tomsignature.TomSignatureFactory.getInstance(aterm.pure.SingletonFactory.getInstance());
  }

  protected static TomList cons(TomTerm t, TomList l) {
    return tsf().makeTomList(t,l);
  }

  protected static TomList append(TomTerm t, TomList l) {
    return (TomList) l.append(t);
  }

  public static TomList makeList(Collection c) {
    Object array[] = c.toArray();
    TomList list = tsf().makeTomList();
    for(int i=array.length-1; i>=0 ; i--) {
      ATerm elt = (ATerm)array[i];
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
      } else if(elt instanceof Instruction) {
        term = tsf().makeTomTerm_InstructionToTomTerm((Instruction)elt);
      } else {
        term = (TomTerm)elt;
      }
      list = cons(term,list);
    }
    return list;
  }

  public static InstructionList makeInstructionList(Collection c) {
    Object array[] = c.toArray();
    InstructionList list = tsf().makeInstructionList();
    for(int i=array.length-1; i>=0 ; i--) {
      ATerm elt = (ATerm)array[i];
      Instruction term;
      if(elt instanceof TargetLanguage) {
        term = tsf().makeInstruction_TargetLanguageToInstruction((TargetLanguage)elt);
      } else if(elt instanceof TomTerm) {
        term = tsf().makeInstruction_TomTermToInstruction((TomTerm)elt);
          //System.out.println("term   = " + term);
      } else if(elt instanceof Instruction) {
        term = (Instruction)elt;
      } else {
        System.out.println("elt   = " + elt);
        term = (Instruction)elt;
      }
      list = tsf().makeInstructionList(term,list);
    }
    return list;
  }

  public static OptionList makeOptionList(List argumentList) {
    OptionList list = tsf().makeOptionList();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      ATerm elt = (ATerm)argumentList.get(i);
      Option term;
      if(elt instanceof TomName) {
        term = tsf().makeOption_TomNameToOption((TomName)elt);
      } else if(elt instanceof Declaration) {
        term = tsf().makeOption_DeclarationToOption((Declaration)elt);
      } else if(elt instanceof TomTerm) {
        term = tsf().makeOption_TomTermToOption((TomTerm)elt);
      } else {
        term = (Option)elt;
      }
      list = tsf().makeOptionList(term,list);
    }
    return list;
  }

  public static ConstraintList makeConstraintList(List argumentList) {
    ConstraintList list = tsf().makeConstraintList();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      ATerm elt = (ATerm)argumentList.get(i);
      Constraint term;
      term = (Constraint)elt;
      list = tsf().makeConstraintList(term,list);
    }
    return list;
  }

  public static NameList makeNameList(List argumentList) {
    NameList list = tsf().makeNameList();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      ATerm elt = (ATerm)argumentList.get(i);
      TomName term = (TomName) elt;
      list = tsf().makeNameList(term,list);
    }
    return list;
  }

  public static SlotList makeSlotList(List argumentList) {
    SlotList list = tsf().makeSlotList();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      ATerm elt = (ATerm)argumentList.get(i);
      Slot term = (Slot) elt;
      list = tsf().makeSlotList(term,list);
    }
    return list;
  }

  public static PairNameDeclList makePairNameDeclList(List argumentList) {
    PairNameDeclList list = tsf().makePairNameDeclList();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      ATerm elt = (ATerm)argumentList.get(i);
      PairNameDecl term = (PairNameDecl) elt;
      list = tsf().makePairNameDeclList(term,list);
    }
    return list;
  }

  public static PatternInstructionList makePatternInstructionList(List argumentList) {
    PatternInstructionList list = tsf().makePatternInstructionList();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      ATerm elt = (ATerm)argumentList.get(i);
      PatternInstruction term;
      term = (PatternInstruction)elt;
      list = tsf().makePatternInstructionList(term,list);
    }
    return list;
  }

  public static TomVisitList makeTomVisitList(List argumentList) {
    TomVisitList list = tsf().makeTomVisitList();
    for(int i=argumentList.size()-1; i>=0 ; i--) {
      ATerm elt = (ATerm)argumentList.get(i);
      TomVisit term;
      term = (TomVisit)elt;
      list = tsf().makeTomVisitList(term,list);
    }
    return list;
  }
  
  public static TomTerm makeVariable(String name, String type) {
    return makeVariable(makeOption(), name, type);      
  }

  public static TomTerm makeVariable(OptionList option, String name, String type) {
    return tsf().makeTomTerm_Variable(option, tsf().makeTomName_Name(name), tsf().makeTomType_TomTypeAlone(type), tsf().makeConstraintList());  
  }

  public static TomTerm makeVariable(OptionList option, TomName name, TomType type) {
    return tsf().makeTomTerm_Variable(option, name, type, tsf().makeConstraintList());  
  }

  public static TomTerm makeVariableStar(OptionList option, String name, String type) {
    return tsf().makeTomTerm_VariableStar(option, tsf().makeTomName_Name(name), tsf().makeTomType_TomTypeAlone(type), tsf().makeConstraintList());  
  }

  public static TomTerm makeVariableStar(OptionList option, TomName name, TomType type) {
    return tsf().makeTomTerm_VariableStar(option, name, type, tsf().makeConstraintList());  
  }

  public static TomTerm makeVariableStar(OptionList option, String name, String type, ConstraintList constraintList) {
    return tsf().makeTomTerm_VariableStar(option, tsf().makeTomName_Name(name), tsf().makeTomType_TomTypeAlone(type), constraintList);  
  }

  public static TomTerm makeVariableStar(OptionList option, TomName name, TomType type, ConstraintList constraintList) {
    return tsf().makeTomTerm_VariableStar(option, name, type, constraintList);  
  }

  public static TomTerm makeUnamedVariableStar(OptionList option, String type, ConstraintList constraintList) {
    return tsf().makeTomTerm_UnamedVariableStar(option, tsf().makeTomType_TomTypeAlone(type),constraintList);  
  }

  public static TomSymbol makeSymbol(String symbolName, TomType resultType, TomTypeList typeList, PairNameDeclList pairNameDeclList,
                              List optionList) {
    TomType type;
    TomName name = tsf().makeTomName_Name(symbolName);
    type = resultType;
    TomType typesToType =  tsf().makeTomType_TypesToType(typeList,type); 
    OptionList options = makeOptionList(optionList);
    return tsf().makeTomSymbol_Symbol(name,typesToType,pairNameDeclList,options);
  }

  public static OptionList makeOption() {
    return tsf().makeOptionList();
  }

  public static OptionList makeOption(Option arg) {
    OptionList list = tsf().makeOptionList();
    if(arg!= null) {
      list = tsf().makeOptionList(arg,list);
    }
    return list;
  }

  public static ConstraintList makeConstraint() {
    return tsf().makeConstraintList();
  }

  public static ConstraintList makeConstraint(Constraint arg) {
    ConstraintList list = tsf().makeConstraintList();
    if(arg!= null) {
      list = tsf().makeConstraintList(arg,list);
    }
    return list;
  }

  public static Constraint makeAssignTo(TomName name,int line, String fileName) {
    return tsf().makeConstraint_AssignTo(tsf().makeTomTerm_Variable(makeOption(makeOriginTracking(name.getString(), line , fileName)),
          name,
          tsf().makeTomType_TomTypeAlone("unknown type"),
          tsf().makeConstraintList()));
  }
  
  public static OptionList makeOption(Option arg, Option info) {
    OptionList list = tsf().makeOptionList();
    if(arg!= null) {
      list = tsf().makeOptionList(arg,list);
    }
    list = tsf().makeOptionList(info,list);
    return list;
  }

  
  private static Option makeOriginTracking(String name, int line , String fileName) {
    return tsf().makeOption_OriginTracking(tsf().makeTomName_Name(name), line, tsf().makeTomName_Name( fileName));
  }

  
  protected static TomType makeType(String typeNameTom, String typeNametGL) {
    TomType typeTom = tsf().makeTomType_ASTTomType(typeNameTom);
    TomType sortTL  = tsf().makeTomType_TLType(tsf().makeTargetLanguage_ITL(typeNametGL));
    return tsf().makeTomType_Type(typeTom,sortTL);
  }
  
    /*
     * create an <sort> symbol
     * where <sort> could be int. double or String  
     */
  private static void makeSortSymbol(SymbolTable symbolTable,
                             String sort,
                             String value, List optionList) {
    TomTypeList typeList = tsf().makeTomTypeList();
    PairNameDeclList pairSlotDeclList = tsf().makePairNameDeclList();
    TomSymbol astSymbol = makeSymbol(value,tsf().makeTomType_TomTypeAlone(sort),typeList,pairSlotDeclList,optionList);
    symbolTable.putSymbol(value,astSymbol);
  }
  
    /*
     * create an integer symbol
     */
  public static void makeIntegerSymbol(SymbolTable symbolTable,
                                String value, List optionList) {
    String sort = "int";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * create a long symbol
     */
  public static void makeLongSymbol(SymbolTable symbolTable,
                             String value, List optionList) {
    String sort = "long";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * create a char symbol
     */
  public static void makeCharSymbol(SymbolTable symbolTable,
                             String value, List optionList) {
    String sort = "char";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }
    /*
     * create a double symbol
     */
  public static void makeDoubleSymbol(SymbolTable symbolTable,
                               String value, List optionList) {
    String sort = "double";
    makeSortSymbol(symbolTable, sort, value, optionList);
  }

    /*
     * create a string symbol
     */
  public static void makeStringSymbol(SymbolTable symbolTable,
                               String value, List optionList) {
    String sort = "String";
    makeSortSymbol(symbolTable, sort, value, optionList);
  } 
  
    /*
     * update the root of lhs: it becomes a defined symbol
     */
  public static TomSymbol updateDefinedSymbol(SymbolTable symbolTable, TomTerm term) {
    if(term.isTermAppl() || term.isRecordAppl()) {
      String key = term.getNameList().getHead().getString();
      TomSymbol symbol = symbolTable.getSymbolFromName(key);
      if (symbol != null) {
        OptionList optionList = symbol.getOption();
        optionList = (OptionList) optionList.append(tsf().makeOption_DefinedSymbol());
        symbolTable.putSymbol(key,symbol.setOption(optionList));
        return symbol;
      }
    }
    return null;
  }

  public static TargetLanguage reworkTLCode(TargetLanguage code, boolean pretty) {
    if(!pretty){
      String newTlCode = code.getCode();      
      newTlCode = newTlCode.replace('\n', ' ');
      newTlCode = newTlCode.replace('\t', ' ');
      newTlCode = newTlCode.replace('\r', ' ');
        //System.out.println("reworking"+newTlCode);
      return code.setCode(newTlCode);
    } else
      return code;
  }

  public static TomName makeName(String slotName) {
    if(slotName.length()>0)
      return tsf().makeTomName_Name(slotName);
    else
      return tsf().makeTomName_EmptyName();
  }

  public static String encodeXMLString(SymbolTable symbolTable, String name) {
    name = "\"" + name + "\"";
    makeStringSymbol(symbolTable,name, new LinkedList());
    return name;
  }

  public static TomTerm encodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * encode a String into a quoted-string
       * Appl(...,Name("string"),...) becomes
       * Appl(...,Name("\"string\""),...)
       */
    if(term.isTermAppl()) {
      TomName astName = term.getAstName();
      if(astName.isName()) {
        String tomName = encodeXMLString(symbolTable,astName.getString());
        term = term.setAstName(tsf().makeTomName_Name(tomName));
          //System.out.println("encodeXMLAppl = " + term);
      }
    }
      return term;
  }

  public static TomTerm metaEncodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * meta-encode a String into a TextNode
       * Appl(...,Name("\"string\""),...) becomes
       * Appl(...,Name("TextNode"),[Appl(...,Name("\"string\""),...)],...)
       */
    if(term.isTermAppl()) {
      TomName astName = term.getAstName();
      if(astName.isName()) {
        String tomName = astName.getString();
        TomSymbol tomSymbol = symbolTable.getSymbolFromName(tomName);
        if(tomSymbol != null) {
          TomType type = tomSymbol.getTypesToType().getCodomain();
            //System.out.println("type = " + type);
          if(type.isTomTypeAlone() && type.getString().equals("String")) {
            Option info = makeOriginTracking(Constants.TEXT_NODE,-1,"??");
            TomList list = tsf().makeTomList();
            list = tsf().makeTomList(term,list);
            NameList nameList = tsf().makeNameList();
            nameList = (NameList)nameList.append(tsf().makeTomName_Name(Constants.TEXT_NODE));
            term = tsf().makeTomTerm_TermAppl(
              makeOption(info),
              nameList,
              list,
              tsf().makeConstraintList());
              //System.out.println("metaEncodeXmlAppl = " + term);
          }
        }
      }
    }
    return term;
  }

  public static String makeTomVariableName(String name) {
    return "tom_" + name;
  }
  
}
