/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

import jtom.adt.tomsignature.types.*;
import jtom.tools.OutputCode;

public abstract class TomGenericGenerator extends TomAbstractGenerator {
  
  protected HashMap getSubtermMap = new HashMap();
  protected HashMap getFunSymMap = new HashMap();
  protected HashMap isFsymMap = new HashMap();
  
  public TomGenericGenerator(OutputCode output) {
    super(output);
  }

// ------------------------------------------------------------
  %include { ../adt/TomSignature.tom }
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
  protected abstract void buildExpGetHead(int deep, TomType domain, TomType codomain, TomTerm var) throws IOException;
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

  protected void buildSymbolDecl(int deep, String tomName) throws IOException {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
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
  protected void buildExpEmptyList(int deep, TomType type, TomTerm expList) throws IOException {
    output.write("tom_is_empty_" + getTomType(type) + "(");
    generate(deep,expList);
    output.write(")");
  }

  protected void buildExpEmptyArray(int deep, TomType type, TomTerm expIndex, TomTerm expArray) throws IOException {
    generate(deep,expIndex);
    output.write(" >= ");
    output.write("tom_get_size_" + getTomType(type) + "(");
    generate(deep,expArray);
    output.write(")");
  }

  protected void buildEqualFunctionSymbol(int deep, TomType type, TomTerm exp, String tomName) throws IOException {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
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

  protected void buildExpGetTail(int deep, TomType type1, TomTerm var) throws IOException {
    output.write("tom_get_tail_" + getTomType(type1) + "(");
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
    if( ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
      TomType argType = getUniversalType();
      if(symbolTable().isBuiltinType(type)) {
        argType = symbolTable().getBuiltinType(type);
      }
      args = new String[] { getTLType(argType), name };
    } else {
      args = new String[] { getTLCode(tlType), name };
    }
    
    TomType returnType = getUniversalType();
      if(symbolTable().isBuiltinType(type)) {
        returnType = symbolTable().getBuiltinType(type);
      }
    generateTargetLanguage(deep,
                           genDecl(getTLType(returnType),
                                   "tom_get_fun_sym", type,args,tlCode));
  }



  protected void buildIsFsymDecl(int deep, String tomName, String name1,
                                 TomType tlType, TargetLanguage tlCode) throws IOException {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    String opname = tomSymbol.getAstName().getString();
    
    TomType returnType = symbolTable().getBoolType();
    String argType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
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
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    String opname = tomSymbol.getAstName().getString();
    TomTypeList typesList = tomSymbol.getTypesToType().getDomain();
    
    int slotIndex = getSlotIndex(tomSymbol.getSlotList(),slotName);
    TomTypeList l = typesList;
    for(int index = 0; !l.isEmpty() && index<slotIndex ; index++) {
      l = l.getTail();
    }
    TomType returnType = l.getHead();
    
    String argType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
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
    if(symbolTable().isBuiltinType(type1)) {
      argType1 = symbolTable().getBuiltinType(type1);
    }
    TomType argType2 = getUniversalType();
    if(symbolTable().isBuiltinType(type2)) {
      argType2 = symbolTable().getBuiltinType(type2);
    } 
    
    generateTargetLanguage(deep, genDecl(getTLType(symbolTable().getBoolType()), "tom_cmp_fun_sym", type1,
                                         new String[] {
                                           getTLType(argType1), name1,
                                           getTLType(argType2), name2
                                         },
                                         tlCode));
  }

  protected void buildTermsEqualDecl(int deep, String name1, String name2,
String type1, String type2, TargetLanguage tlCode) throws IOException {
    TomType argType1 = getUniversalType();
    if(symbolTable().isBuiltinType(type1)) {
      argType1 = symbolTable().getBuiltinType(type1);
    } 
    TomType argType2 = getUniversalType();
    if(symbolTable().isBuiltinType(type2)) {
      argType2 = symbolTable().getBuiltinType(type2);
    } 
    
    generateTargetLanguage(deep, genDecl(getTLType(symbolTable().getBoolType()), "tom_terms_equal", type1,
                                             new String[] {
                                               getTLType(argType1), name1,
                                               getTLType(argType2), name2
                                             },
                                             tlCode));
  }

  protected void buildGetHeadDecl(int deep, String name1, String suffix, TomType domain, TomType codomain, TargetLanguage tlCode) 
    throws IOException {
    String returnType,argType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
      returnType = getTLCode(codomain);
      argType = getTLCode(domain);
    } else {
      returnType = getTLType(getUniversalType());
      argType = getTLType(getUniversalType());
    }

    generateTargetLanguage(deep,
                           genDecl(returnType, "tom_get_head", suffix,
                                   new String[] { argType, name1 },
                                   tlCode));
  }

  protected void buildGetTailDecl(int deep, String name1, String type, TomType tlType, TargetLanguage tlCode) 
    throws IOException {
    String returnType, argType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
      returnType = getTLCode(tlType);
      argType = getTLCode(tlType);
    } else {
      returnType = getTLType(getUniversalType());
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep,
                           genDecl(returnType, "tom_get_tail", type,
                                   new String[] { argType, name1 },
                                   tlCode));
  }

  protected void buildIsEmptyDecl(int deep, String name1, String type,
                                  TomType tlType, TargetLanguage tlCode) throws IOException {
    String argType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep,
                           genDecl(getTLType(symbolTable().getBoolType()),
                                   "tom_is_empty", type,
                                   new String[] { argType, name1 },
                                   tlCode));
  }

  protected void buildMakeEmptyList(int deep, String opname, TomType codomain, TargetLanguage tlCode) throws IOException {
    String returnType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
      returnType = getTLType(codomain);
    } else {
      returnType = getTLType(getUniversalType());
    }

    generateTargetLanguage(deep,
                           genDecl(returnType,
                                   "tom_empty_list", opname,
                                   new String[] { },
                                   tlCode));
  }

  protected void buildMakeAddList(int deep, String opname, String name1,
String name2, TomType tlType1, TomType tlType2, TomType fullEltType,
TomType fullListType, TargetLanguage tlCode) throws IOException {
    String returnType, argListType,argEltType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
      argEltType = getTLCode(tlType1);
      argListType = getTLCode(tlType2);
      returnType = argListType;
    } else {
      argEltType  = getTLType(getUniversalType());
      argListType = getTLType(getUniversalType());
      returnType  = argListType;
    }
    
    generateTargetLanguage(deep, genDecl(returnType,
                                             "tom_cons_list", opname,
                                             new String[] {
                                               argEltType, name1,
                                               argListType, name2
                                             },
                                             tlCode));
    
    generateTargetLanguage(deep, genDeclList(opname, fullListType,fullEltType));
  }

  protected void buildGetElementDecl(int deep, String name1, String name2,
String type1, TomType tlType1, TargetLanguage tlCode) throws IOException {
    String returnType, argType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
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
                                           getTLType(symbolTable().getIntType()), name2
                                         },
                                         tlCode));
  }

  protected void buildGetSizeDecl(int deep, String name1, String type,
TomType tlType, TargetLanguage tlCode) throws IOException {
    String argType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep,
                           genDecl(getTLType(symbolTable().getIntType()),
                                   "tom_get_size", type,
                                   new String[] { argType, name1 },
                                   tlCode));
  }

  protected void buildMakeEmptyArray(int deep, String opname, TomType codomain,String name1, TargetLanguage tlCode) throws IOException {
    String returnType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
      returnType = getTLType(codomain);
    } else {
      returnType = getTLType(getUniversalType());
    }

    generateTargetLanguage(deep, genDecl(returnType, "tom_empty_array", opname,
                                             new String[] {
                                               getTLType(symbolTable().getIntType()), name1,
                                             },
                                             tlCode));
  }

  protected void buildMakeAddArray(int deep, String opname, String name1, String name2, TomType tlType1,
TomType tlType2, TomType fullEltType, TomType fullArrayType, TargetLanguage tlCode) throws IOException {
    String returnType, argListType,argEltType;
    if(! ((Boolean)getServer().getOptionValue("lazyType")).booleanValue()) {
      argEltType  = getTLCode(tlType1);
      argListType = getTLCode(tlType2);
      returnType  = argListType;
      
    } else {
      argEltType  = getTLType(getUniversalType());
      argListType = getTLType(getUniversalType());
      returnType  = argListType;
    }
    
    generateTargetLanguage(deep,
                           genDecl(argListType,
                                   "tom_cons_array", opname,
                                   new String[] {
                                     argEltType, name1,
                                     argListType, name2
                                   },
                                   tlCode));
    generateTargetLanguage(deep, genDeclArray(opname, fullArrayType, fullEltType));
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
  
} // class TomAbstractGenerator
