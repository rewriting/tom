/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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

package jtom.backend;

import java.io.IOException;
import java.util.HashMap;

import jtom.adt.tomsignature.types.Declaration;
import jtom.adt.tomsignature.types.Expression;
import jtom.adt.tomsignature.types.Instruction;
import jtom.adt.tomsignature.types.InstructionList;
import jtom.adt.tomsignature.types.OptionList;
import jtom.adt.tomsignature.types.SlotList;
import jtom.adt.tomsignature.types.TargetLanguage;
import jtom.adt.tomsignature.types.TomList;
import jtom.adt.tomsignature.types.TomName;
import jtom.adt.tomsignature.types.TomSymbol;
import jtom.adt.tomsignature.types.TomTerm;
import jtom.adt.tomsignature.types.TomType;
import jtom.adt.tomsignature.types.TomTypeList;
import jtom.tools.OutputCode;
import jtom.tools.SymbolTable;
import tom.platform.OptionManager;

public abstract class TomGenericGenerator extends TomAbstractGenerator {
  
  protected HashMap getSubtermMap = new HashMap();
  protected HashMap getFunSymMap = new HashMap();
  protected HashMap isFsymMap = new HashMap();
  protected boolean lazyMode;

  public TomGenericGenerator(OutputCode output, OptionManager optionManager,
                                SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
    lazyMode = ((Boolean)optionManager.getOptionValue("lazyType")).booleanValue();
  }

// ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
// ------------------------------------------------------------

  protected abstract TargetLanguage genDecl(String returnType,
                                            String declName,
                                            String suffix,
                                            String args[],
                                            TargetLanguage tlCode);
  
  protected abstract TargetLanguage genDeclMake(String opname, TomType returnType, 
                                            TomList argList, TargetLanguage tlCode);
  
  protected abstract TargetLanguage genDeclList(String name, TomType listType, TomType eltType);

  protected abstract TargetLanguage genDeclArray(String name, TomType listType, TomType eltType);

//------------------------------------------------------------
 
  protected abstract void buildRef(int deep, TomTerm term) throws IOException;
  protected abstract void buildInstructionSequence(int deep, Instruction instruction) throws IOException;
  protected abstract void buildComment(int deep, String text) throws IOException;
  protected abstract void buildFunctionCall(int deep, String name, TomList argList)  throws IOException;
  protected abstract void buildFunctionBegin(int deep, String tomName, TomList varList) throws IOException; 
  protected abstract void buildFunctionEnd(int deep) throws IOException;
  protected abstract void buildExpNot(int deep, Expression exp) throws IOException;
  protected abstract void buildExpGetSubterm(int deep, TomType domain, TomType codomain, TomTerm exp, int number) throws IOException;
  protected abstract void buildExpGetHead(int deep, TomName opName, TomType domain, TomType codomain, TomTerm var) throws IOException;
  protected abstract void buildExpGetElement(int deep, TomType domain, TomType codomain, TomTerm varName, TomTerm varIndex) throws IOException;

  protected abstract void buildReturn(int deep, TomTerm exp) throws IOException ;
  protected abstract void buildExpTrue(int deep) throws IOException;
  protected abstract void buildExpFalse(int deep) throws IOException;
  protected abstract void buildAssignVar(int deep, TomTerm var, OptionList list, Expression exp) throws IOException ;
  protected abstract void buildLetAssign(int deep, TomTerm var, OptionList list, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildExpCast(int deep, TomType type, Expression exp) throws IOException;
  protected abstract void buildLet(int deep, TomTerm var, OptionList list, TomType tlType, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildLetRef(int deep, TomTerm var, OptionList list, TomType tlType, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildNamedBlock(int deep, String blockName, InstructionList instList) throws IOException ;
  protected abstract void buildUnamedBlock(int deep, InstructionList instList) throws IOException ;
  protected abstract void buildIfThenElse(int deep, Expression exp, Instruction succes) throws IOException ;
  protected abstract void buildIfThenElseWithFailure(int deep, Expression exp, Instruction succes, Instruction failure) throws IOException ;
  protected abstract void buildGetSubtermDecl(int deep, String name1, String name2, String type1,
TomType tlType1, TomType tlType2, TargetLanguage tlCode) throws IOException ;
  protected abstract void buildDoWhile(int deep, Instruction succes, Expression exp) throws IOException;
  protected abstract void buildWhileDo(int deep, Expression exp, Instruction succes) throws IOException;

  /*
   * Implementation of functions whose definition is
   * independant of the target language
   */
 
  protected void buildTerm(int deep, String name, TomList argList) throws IOException {
    buildFunctionCall(deep, "tom_make_"+name, argList);
  }

  protected void buildCheckStamp(int deep, TomType type, TomTerm variable) throws IOException {
    if(((Boolean)optionManager.getOptionValue("stamp")).booleanValue()) {
      output.write("tom_check_stamp_" + getTomType(type) + "(");
      generate(deep,variable);
      output.write(");");
    }
  }

  protected void buildSymbolDecl(int deep, String tomName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(tomName);
    OptionList optionList = tomSymbol.getOption();
    SlotList slotList = tomSymbol.getSlotList();
      // inspect the optionList
    generateOptionList(deep, optionList);
      // inspect the slotlist
    generateSlotList(deep, slotList);
  }

  protected void buildCompiledMatch(int deep, Instruction instruction) throws IOException {
    generateInstruction(deep+1,instruction);
  }
  
  protected void buildExpAnd(int deep, Expression exp1, Expression exp2) throws IOException {
    generateExpression(deep,exp1);
    output.write(" && ");
    generateExpression(deep,exp2);
  }
  protected void buildExpOr(int deep, Expression exp1, Expression exp2) throws IOException {
    generateExpression(deep,exp1);
    output.write(" || ");
    generateExpression(deep,exp2);
  }

  protected void buildExpGreaterThan(int deep, Expression exp1, Expression exp2) throws IOException {
    generateExpression(deep,exp1);
    output.write(" > ");
    generateExpression(deep,exp2);
  }

  protected void buildExpIsEmptyList(int deep, TomName opNameAST, TomType type, TomTerm expList) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_is_empty_" + getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_is_empty_" + `opName + "_" + getTomType(type) + "("); }
    }
    generate(deep,expList);
    output.write(")");
  }

  protected void buildExpIsEmptyArray(int deep, TomType type, TomTerm expIndex, TomTerm expArray) throws IOException {
    generate(deep,expIndex);
    output.write(" >= ");
    output.write("tom_get_size_" + getTomType(type) + "(");
    generate(deep,expArray);
    output.write(")");
  }

  protected void buildEqualFunctionSymbol(int deep, TomType type, TomTerm exp, String tomName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(tomName);
    TomName termNameAST = tomSymbol.getAstName();
    OptionList termOptionList = tomSymbol.getOption();
    
    Declaration isFsymDecl = getIsFsymDecl(termOptionList);
    if(isFsymDecl != null) {
      generateExpression(deep,`IsFsym(termNameAST,exp));
    } else {
      generateEqualFunctionSymbol(deep,type,exp,getSymbolCode(tomSymbol));
    }
  }

  private void generateEqualFunctionSymbol(int deep, TomType type, TomTerm exp1, String exp2) throws IOException {
    String s = (String)getFunSymMap.get(type);
    if(s == null) {
      s = "tom_cmp_fun_sym_" + getTomType(type) +
        "(tom_get_fun_sym_" + getTomType(type) + "(";
      getFunSymMap.put(type,s);
    } 
    output.write(s);
    generate(deep,exp1);
    output.write(") , " + exp2 + ")");
  }

  protected void buildExpEqualTerm(int deep, TomType type, TomTerm exp1,TomTerm exp2) throws IOException {
    output.write("tom_terms_equal_" + getTomType(type) + "(");
    generate(deep,exp1);
    output.write(", ");
    generate(deep,exp2);
    output.write(")");
  }

  protected void buildExpIsFsym(int deep, String opname, TomTerm exp) throws IOException {
    String s = (String)isFsymMap.get(opname);
    if(s == null) {
      s = "tom_is_fun_sym_" + opname + "(";
      isFsymMap.put(opname,s);
    } 
    output.write(s);
    generate(deep,exp);
    output.write(")");
  }

  protected void buildExpGetSlot(int deep, String opname, String slotName, TomTerm var) throws IOException {
      //output.write("tom_get_slot_" + opname + "_" + slotName + "(");
      //generate(deep,var);
      //output.write(")");
      output.write("tom_get_slot_");
      output.write(opname);
      output.writeUnderscore();
      output.write(slotName);
      output.writeOpenBrace();
      generate(deep,var);
      output.writeCloseBrace();
  }

  protected void buildExpGetTail(int deep, TomName opNameAST, TomType type, TomTerm var) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_tail_" + getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_get_tail_" + `opName + "_" + getTomType(type) + "("); }
    }
    generate(deep,var); 
    output.write(")");
  }

  protected void buildExpGetSize(int deep, TomType type1, TomTerm var) throws IOException {
    output.write("tom_get_size_" + getTomType(type1) + "(");
    generate(deep,var);
    output.write(")");
  }

  protected void buildExpGetSliceList(int deep, String name, TomTerm varBegin, TomTerm varEnd) throws IOException {
    output.write("tom_get_slice_" + name + "(");
    generate(deep,varBegin);
    output.write(",");
    generate(deep,varEnd);
    output.write(")");
  }

  protected void buildExpGetSliceArray(int deep, String name, TomTerm varArray, TomTerm varBegin, TomTerm expEnd) throws IOException {
    output.write("tom_get_slice_" + name + "(");
    generate(deep,varArray);
    output.write(",");
    generate(deep,varBegin);
    output.write(",");
    generate(deep,expEnd);
    output.write(")");
  }

  protected void buildAddOne(int deep, TomTerm var) throws IOException {
    generate(deep,var);
    output.write(" + 1");
  }

  protected void buildGetFunctionSymbolDecl(int deep, String type, String name,
TomType tlType, TargetLanguage tlCode) throws IOException {
    String args[];
    if(lazyMode) {
      TomType argType = getUniversalType();
      if(getSymbolTable().isBuiltinType(type)) {
        argType = getSymbolTable().getBuiltinType(type);
      }
      args = new String[] { getTLType(argType), name };
    } else {
      args = new String[] { getTLCode(tlType), name };
    }
    
    TomType returnType = getUniversalType();
      if(getSymbolTable().isBuiltinType(type)) {
        returnType = getSymbolTable().getBuiltinType(type);
      }
    generateTargetLanguage(deep,
                           genDecl(getTLType(returnType),
                                   "tom_get_fun_sym", type,args,tlCode));
  }

  protected void buildCheckStampDecl(int deep, String type, String name,
                                 TomType tlType, TargetLanguage tlCode) throws IOException {
    TomType returnType = getSymbolTable().getVoidType();
    String argType;
    if(!lazyMode) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep, genDecl(getTLType(returnType),
                                         "tom_check_stamp", type,
                                         new String[] { argType, name },
                                         tlCode));
  }

  protected void buildSetStampDecl(int deep, String type, String name,
                                 TomType tlType, TargetLanguage tlCode) throws IOException {
    String argType;
    if(!lazyMode) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    String returnType = argType; /* TODO: use stamp type */
    
    generateTargetLanguage(deep, genDecl(returnType,
                                         "tom_set_stamp", type,
                                         new String[] { argType, name },
                                         tlCode));
  }

  protected void buildGetImplementationDecl(int deep, String type, String name,
                                 TomType tlType, TargetLanguage tlCode) throws IOException {
    String argType;
    if(!lazyMode) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    String returnType = argType; /* TODO: use stamp type */
    
    generateTargetLanguage(deep, genDecl(returnType,
                                         "tom_get_implementation", type,
                                         new String[] { argType, name },
                                         tlCode));
  }

  protected void buildIsFsymDecl(int deep, String tomName, String name1,
                                 TomType tlType, TargetLanguage tlCode) throws IOException {
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();
    
    TomType returnType = getSymbolTable().getBoolType();
    String argType;
    if(!lazyMode) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep, genDecl(getTLType(returnType),
                                         "tom_is_fun_sym", opname,
                                         new String[] { argType, name1 },
                                         tlCode));
  }

  protected void buildGetSlotDecl(int deep, String tomName, String name1,
TomType tlType, TargetLanguage tlCode, TomName slotName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();
    TomTypeList typesList = tomSymbol.getTypesToType().getDomain();
    
    int slotIndex = getSlotIndex(tomSymbol.getSlotList(),slotName);
    TomTypeList l = typesList;
    for(int index = 0; !l.isEmpty() && index<slotIndex ; index++) {
      l = l.getTail();
    }
    TomType returnType = l.getHead();
    
    String argType;
    if(!lazyMode) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    generateTargetLanguage(deep, genDecl(getTLType(returnType),
                                             "tom_get_slot", opname  + "_" + slotName.getString(),
                                             new String[] { argType, name1 },
                                             tlCode));
  }

  protected void  buildCompareFunctionSymbolDecl(int deep, String name1,
String name2, String type1, String type2, TargetLanguage tlCode) throws IOException {
    TomType argType1 = getUniversalType();
    if(getSymbolTable().isBuiltinType(type1)) {
      argType1 = getSymbolTable().getBuiltinType(type1);
    }
    TomType argType2 = getUniversalType();
    if(getSymbolTable().isBuiltinType(type2)) {
      argType2 = getSymbolTable().getBuiltinType(type2);
    } 
    
    generateTargetLanguage(deep, genDecl(getTLType(getSymbolTable().getBoolType()), "tom_cmp_fun_sym", type1,
                                         new String[] {
                                           getTLType(argType1), name1,
                                           getTLType(argType2), name2
                                         },
                                         tlCode));
  }

  protected void buildTermsEqualDecl(int deep, String name1, String name2,
String type1, String type2, TargetLanguage tlCode) throws IOException {
    TomType argType1 = getUniversalType();
    if(getSymbolTable().isBuiltinType(type1)) {
      argType1 = getSymbolTable().getBuiltinType(type1);
    } 
    TomType argType2 = getUniversalType();
    if(getSymbolTable().isBuiltinType(type2)) {
      argType2 = getSymbolTable().getBuiltinType(type2);
    } 
    
    generateTargetLanguage(deep, genDecl(getTLType(getSymbolTable().getBoolType()), "tom_terms_equal", type1,
                                             new String[] {
                                               getTLType(argType1), name1,
                                               getTLType(argType2), name2
                                             },
                                             tlCode));
  }

  protected void buildGetHeadDecl(int deep, TomName opNameAST, String varName, String suffix, TomType domain, TomType codomain, TargetLanguage tlCode) 
    throws IOException {
    String returnType = null;
    String argType = null;
    String functionName = "tom_get_head";

    %match(TomName opNameAST) {
      Name(opName) -> { functionName = functionName + "_" + `opName; }
    }

    if(lazyMode) {
      returnType = getTLType(getUniversalType());
      argType = getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          returnType = getTLCode(codomain);
          argType = getTLCode(domain);
        }
        
        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          returnType = getTLType(getSymbolCodomain(tomSymbol));
          argType = getTLType(getSymbolDomain(tomSymbol).getHead());
        }
      }
    }
    generateTargetLanguage(deep,
                           genDecl(returnType, functionName, suffix,
                                   new String[] { argType, varName },
                                   tlCode));
  }

  protected void buildGetTailDecl(int deep, TomName opNameAST, String varName, String type, TomType tlType, TargetLanguage tlCode) 
    throws IOException {
    String returnType = null;
    String argType = null;
    String functionName = "tom_get_tail";

    %match(TomName opNameAST) {
      Name(opName) -> { functionName = functionName + "_" + `opName; }
    }

    if(lazyMode) {
      returnType = getTLType(getUniversalType());
      argType = getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          returnType = getTLCode(tlType);
          argType = returnType;
        }
        
        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          returnType = getTLType(getSymbolCodomain(tomSymbol));
          argType = returnType;
        }
      }
    }

    generateTargetLanguage(deep,
                           genDecl(returnType, functionName, type,
                                   new String[] { argType, varName },
                                   tlCode));
  }

  protected void buildIsEmptyDecl(int deep, TomName opNameAST, String varName, String type,
                                  TomType tlType, TargetLanguage tlCode) throws IOException {
    String argType = null;
    String functionName = "tom_is_empty";

    %match(TomName opNameAST) {
      Name(opName) -> { functionName = functionName + "_" + `opName; }
    }
    if(lazyMode) {
      argType = getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          argType = getTLCode(tlType);
        }
        
        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          argType = getTLType(getSymbolCodomain(tomSymbol));
        }
      }
    }

    generateTargetLanguage(deep,
                           genDecl(getTLType(getSymbolTable().getBoolType()),
                                   functionName, type,
                                   new String[] { argType, varName },
                                   tlCode));
  }

  protected void buildGetElementDecl(int deep, String name1, String name2,
                                     String type1, TomType tlType1, TargetLanguage tlCode) throws IOException {
    String returnType, argType;
    if(!lazyMode) {
      returnType = getTLType(getUniversalType());
      argType = getTLCode(tlType1);
    } else {
      returnType = getTLType(getUniversalType());
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep, genDecl(returnType,
                                         "tom_get_element", type1,
                                         new String[] {
                                           argType, name1,
                                           getTLType(getSymbolTable().getIntType()), name2
                                         },
                                         tlCode));
  }

  protected void buildGetSizeDecl(int deep, String name1, String type,
TomType tlType, TargetLanguage tlCode) throws IOException {
    String argType;
    if(!lazyMode) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep,
                           genDecl(getTLType(getSymbolTable().getIntType()),
                                   "tom_get_size", type,
                                   new String[] { argType, name1 },
                                   tlCode));
  }

  protected void buildTypeTermDecl(int deep, TomList declList) throws IOException {
    generateDeclarationFromList(deep, declList);
  }

  protected void buildTypeListDecl(int deep, TomList declList) throws IOException {
    generateDeclarationFromList(deep, declList);
  }

  protected void buildTypeArrayDecl(int deep, TomList declList) throws IOException {
    generateDeclarationFromList(deep, declList);
  }
  
  protected void generateDeclarationFromList(int deep, TomList declList) throws IOException {
    TomTerm term;
    while(!declList.isEmpty()) {
      term = declList.getHead();
      %match (TomTerm term){
        DeclarationToTomTerm(declaration) -> {generateDeclaration(deep, `declaration);}
      }
      declList = declList.getTail();
    }
  }
  
} // class TomGenericGenerator
