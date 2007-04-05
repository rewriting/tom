/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2007, INRIA
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

package tom.engine.backend;

import java.io.IOException;
import java.util.HashMap;

import tom.engine.tools.OutputCode;

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

import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;

public abstract class TomGenericGenerator extends TomAbstractGenerator {

  protected HashMap isFsymMap = new HashMap();
  protected boolean lazyMode;

  public TomGenericGenerator(OutputCode output, OptionManager optionManager,
                             SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
    lazyMode = ((Boolean)optionManager.getOptionValue("lazyType")).booleanValue();
  }

  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  /*
   * Implementation of functions whose definition is
   * independant of the target language
   */

  protected void buildTerm(int deep, String name, TomList argList, String moduleName) throws IOException {
    buildFunctionCall(deep, "tom_make_"+name, argList, moduleName);
  }

  protected void buildSymbolDecl(int deep, String tomName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    OptionList optionList = tomSymbol.getOption();
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
    // inspect the optionList
    generateOptionList(deep, optionList, moduleName);
    // inspect the slotlist
    generatePairNameDeclList(deep, pairNameDeclList, moduleName);
  }

  protected void buildExpGreaterThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    generateExpression(deep,exp1,moduleName);
    output.write(" > ");
    generateExpression(deep,exp2,moduleName);
  }

  protected void buildExpIsEmptyList(int deep, TomName opNameAST, TomType type, TomTerm expList, String moduleName) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_is_empty_" + getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_is_empty_" + `opName + "_" + getTomType(type) + "("); }
    }
    generate(deep,expList,moduleName);
    output.write(")");
  }

  protected void buildExpIsEmptyArray(int deep, TomName opNameAST, TomType type, TomTerm expIndex, TomTerm expArray, String moduleName) throws IOException {
    generate(deep,expIndex,moduleName);
    output.write(" >= ");
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_size_" + getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_get_size_" + `opName + "_" + getTomType(type) + "("); }
    }
    generate(deep,expArray,moduleName);
    output.write(")");
  }

  protected void buildExpIsSort(int deep, TomType type, TomTerm exp1, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBuiltinType(getTomType(`type))) {
      generateExpression(deep,`TrueTL(),moduleName);
    } else {
      output.write("tom_is_sort_" + getTomType(type) + "(");
      generate(deep,exp1,moduleName);
      output.write(")");
    }
  }

  protected void buildExpIsFsym(int deep, String opname, TomTerm exp, String moduleName) throws IOException {
    String s = (String)isFsymMap.get(opname);
    if(s == null) {
      s = "tom_is_fun_sym_" + opname + "(";
      isFsymMap.put(opname,s);
    }
    output.write(s);
    generate(deep,exp,moduleName);
    output.write(")");
  }

  protected void buildExpGetSlot(int deep, String opname, String slotName, TomTerm var, String moduleName) throws IOException {
    //output.write("tom_get_slot_" + opname + "_" + slotName + "(");
    //generate(deep,var);
    //output.write(")");
    output.write("tom_get_slot_");
    output.write(opname);
    output.writeUnderscore();
    output.write(slotName);
    output.writeOpenBrace();
    generate(deep,var,moduleName);
    output.writeCloseBrace();
  }

  protected void buildExpGetTail(int deep, TomName opNameAST, TomType type, TomTerm var, String moduleName) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_tail_" + getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_get_tail_" + `opName + "_" + getTomType(type) + "("); }
    }
    generate(deep,var,moduleName);
    output.write(")");
  }

  protected void buildExpGetSize(int deep, TomName opNameAST, TomType type, TomTerm var, String moduleName) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_size_" + getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_get_size_" + `opName + "_" + getTomType(type) + "("); }
    }
    generate(deep,var,moduleName);
    output.write(")");
  }

  protected void buildExpGetSliceList(int deep, String name, TomTerm varBegin, TomTerm varEnd, String moduleName) throws IOException {
    output.write("tom_get_slice_" + name + "(");
    generate(deep,varBegin,moduleName);
    output.write(",");
    generate(deep,varEnd,moduleName);
    output.write(")");
  }
  
  protected void buildExpGetSliceArray(int deep, String name, TomTerm varArray, TomTerm varBegin, TomTerm expEnd, String moduleName) throws IOException {
    output.write("tom_get_slice_" + name + "(");
    generate(deep,varArray,moduleName);
    output.write(",");
    generate(deep,varBegin,moduleName);
    output.write(",");
    generate(deep,expEnd,moduleName);
    output.write(")");
  }

  protected void buildAddOne(int deep, TomTerm var, String moduleName) throws IOException {
    generate(deep,var,moduleName);
    output.write(" + 1");
  }

  protected void buildGetFunctionSymbolDecl(int deep, String type, String name,
                                            TomType tlType, TargetLanguage tlCode, String moduleName) throws IOException {
    String args[];
    if(lazyMode) {
      TomType argType = getUniversalType();
      if(getSymbolTable(moduleName).isBuiltinType(type)) {
        argType = getSymbolTable(moduleName).getBuiltinType(type);
      }
      args = new String[] { getTLType(argType), name };
    } else {
      args = new String[] { getTLCode(tlType), name };
    }

    TomType returnType = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type)) {
      returnType = getSymbolTable(moduleName).getBuiltinType(type);
    }
    genDecl(getTLType(returnType),"tom_get_fun_sym", type,args,tlCode, moduleName);
  }

  protected void buildGetImplementationDecl(int deep, String type, String name,
                                            TomType tlType, Instruction instr, String moduleName) throws IOException {
    String argType;
    if(!lazyMode) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    String returnType = argType;

    genDeclInstr(returnType,
            "tom_get_implementation", type,
            new String[] { argType, name },
            instr,deep,moduleName);
  }

  protected void buildIsFsymDecl(int deep, String tomName, String name1,
                                 TomType tlType, Instruction instr, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();

    TomType returnType = getSymbolTable(moduleName).getBooleanType();
    String argType;
    if(!lazyMode) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }

    genDeclInstr(getTLType(returnType),
            "tom_is_fun_sym", opname,
            new String[] { argType, name1 },
            instr,deep,moduleName);
  }

  protected void buildGetSlotDecl(int deep, String tomName, String name1,
                                  TomType tlType, Instruction instr, TomName slotName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();
    TomTypeList typesList = tomSymbol.getTypesToType().getDomain();

    int slotIndex = getSlotIndex(tomSymbol,slotName);
    TomTypeList l = typesList;
    for(int index = 0; !l.isEmptyconcTomType() && index<slotIndex ; index++) {
      l = l.getTailconcTomType();
    }
    TomType returnType = l.getHeadconcTomType();

    String argType;
    if(!lazyMode) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    genDeclInstr(getTLType(returnType),
            "tom_get_slot", opname  + "_" + slotName.getString(),
            new String[] { argType, name1 },
            instr,deep,moduleName);
  }

  protected void  buildCompareFunctionSymbolDecl(int deep, String name1, String name2,
                                                 String type1, String type2, TargetLanguage tlCode, String moduleName) throws IOException {
    TomType argType1 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type1)) {
      argType1 = getSymbolTable(moduleName).getBuiltinType(type1);
    }
    TomType argType2 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type2)) {
      argType2 = getSymbolTable(moduleName).getBuiltinType(type2);
    }

    genDecl(getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_cmp_fun_sym", type1,
            new String[] {
              getTLType(argType1), name1,
              getTLType(argType2), name2
            },
            tlCode, moduleName);
  }

  protected void buildEqualTermDecl(int deep, String name1, String name2,
                                     String type1, String type2, Instruction instr, String moduleName) throws IOException {
    TomType argType1 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type1)) {
      argType1 = getSymbolTable(moduleName).getBuiltinType(type1);
    }
    TomType argType2 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type2)) {
      argType2 = getSymbolTable(moduleName).getBuiltinType(type2);
    }

    genDeclInstr(getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_equal_term", type1,
            new String[] {
              getTLType(argType1), name1,
              getTLType(argType2), name2
            },
            instr,deep,moduleName);
  }

  protected void buildIsSortDecl(int deep, String name1, String type1, Instruction instr, String moduleName) throws IOException {
    TomType argType1 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type1)) {
      argType1 = getSymbolTable(moduleName).getBuiltinType(type1);
    }
    genDeclInstr(getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_is_sort", type1,
        new String[] { getTLType(argType1), name1 },
        instr,deep,moduleName);
  }

  protected void buildGetHeadDecl(int deep, TomName opNameAST, String varName, String suffix, TomType domain, TomType codomain, Instruction instr, String moduleName)
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
          argType = getTLType(getSymbolCodomain(tomSymbol));
          returnType = getTLType(getSymbolDomain(tomSymbol).getHeadconcTomType());
        }
      }
    }
    genDeclInstr(returnType, functionName, suffix,
            new String[] { argType, varName },
            instr,deep,moduleName);
  }

  protected void buildGetTailDecl(int deep, TomName opNameAST, String varName, String type, TomType tlType, Instruction instr, String moduleName)
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

    genDeclInstr(returnType, functionName, type,
            new String[] { argType, varName },
            instr,deep,moduleName);
  }

  protected void buildIsEmptyDecl(int deep, TomName opNameAST, String varName, String type,
                                  TomType tlType, Instruction instr, String moduleName) throws IOException {
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

    genDeclInstr(getTLType(getSymbolTable(moduleName).getBooleanType()),
            functionName, type,
            new String[] { argType, varName },
            instr,deep,moduleName);
  }

  protected void buildGetElementDecl(int deep, TomName opNameAST, String name1, String name2,
                                     String type1, TomType domain, Instruction instr, String moduleName) throws IOException {
    String returnType = null;
    String argType = null;
    String functionName = "tom_get_element";

    %match(TomName opNameAST) {
      Name(opName) -> { functionName = functionName + "_" + `opName; }
    }

    if(lazyMode) {
      returnType = getTLType(getUniversalType());
      argType = getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          returnType = getTLType(getUniversalType());
          argType = getTLCode(domain);
        }

        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          argType = getTLType(getSymbolCodomain(tomSymbol));
          returnType = getTLType(getSymbolDomain(tomSymbol).getHeadconcTomType());
        }
      }
    }

    genDeclInstr(returnType,
            functionName, type1,
            new String[] {
              argType, name1,
              getTLType(getSymbolTable(moduleName).getIntType()), name2
            },
            instr,deep,moduleName);
  }

  protected void buildGetSizeDecl(int deep, TomName opNameAST, String name1, String type,
                                  TomType domain, Instruction instr, String moduleName) throws IOException {
    String argType = null;
    String functionName = "tom_get_size";

    %match(TomName opNameAST) {
      Name(opName) -> { functionName = functionName + "_" + `opName; }
    }

    if(lazyMode) {
      argType = getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          argType = getTLCode(domain);
        }

        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          argType = getTLType(getSymbolCodomain(tomSymbol));
        }
      }
    }

    genDeclInstr(getTLType(getSymbolTable(moduleName).getIntType()),
            functionName, type,
            new String[] { argType, name1 },
            instr,deep,moduleName);
  }

} // class TomGenericGenerator
