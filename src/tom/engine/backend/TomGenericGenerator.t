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

import tom.engine.TomBase;
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
import tom.engine.tools.ASTFactory;
import tom.platform.OptionManager;

public abstract class TomGenericGenerator extends TomAbstractGenerator {

  protected HashMap isFsymMap = new HashMap();
  protected boolean lazyMode;
  protected boolean nodeclMode;
  protected String modifier = "";

  public TomGenericGenerator(OutputCode output, OptionManager optionManager,
                             SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
    lazyMode = ((Boolean)optionManager.getOptionValue("lazyType")).booleanValue();
    nodeclMode = ((Boolean)optionManager.getOptionValue("noDeclaration")).booleanValue();
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
      EmptyName() -> { output.write("tom_is_empty_" + TomBase.getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_is_empty_" + `opName + "_" + TomBase.getTomType(type) + "("); }
    }
    generate(deep,expList,moduleName);
    output.write(")");
  }

  protected void buildExpIsEmptyArray(int deep, TomName opNameAST, TomType type, TomTerm expIndex, TomTerm expArray, String moduleName) throws IOException {
    generate(deep,expIndex,moduleName);
    output.write(" >= ");
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_size_" + TomBase.getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_get_size_" + `opName + "_" + TomBase.getTomType(type) + "("); }
    }
    generate(deep,expArray,moduleName);
    output.write(")");
  }

  protected void buildExpIsSort(int deep, TomType type, TomTerm exp1, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBuiltinType(TomBase.getTomType(`type))) {
      generateExpression(deep,`TrueTL(),moduleName);
    } else {
      output.write("tom_is_sort_" + TomBase.getTomType(type) + "(");
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
      EmptyName() -> { output.write("tom_get_tail_" + TomBase.getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_get_tail_" + `opName + "_" + TomBase.getTomType(type) + "("); }
    }
    generate(deep,var,moduleName);
    output.write(")");
  }

  protected void buildExpGetSize(int deep, TomName opNameAST, TomType type, TomTerm var, String moduleName) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_size_" + TomBase.getTomType(type) + "("); }
      Name(opName) -> { output.write("tom_get_size_" + `opName + "_" + TomBase.getTomType(type) + "("); }
    }
    generate(deep,var,moduleName);
    output.write(")");
  }

  protected void buildExpGetSliceList(int deep, String name, TomTerm varBegin, TomTerm varEnd, TomTerm tail, String moduleName) throws IOException {
    output.write("tom_get_slice_" + name + "(");
    generate(deep,varBegin,moduleName);
    output.write(",");
    generate(deep,varEnd,moduleName);
    output.write(",");
    generate(deep,tail,moduleName);
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
      args = new String[] { TomBase.getTLType(argType), name };
    } else {
      args = new String[] { TomBase.getTLCode(tlType), name };
    }

    TomType returnType = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type)) {
      returnType = getSymbolTable(moduleName).getBuiltinType(type);
    }
    genDecl(TomBase.getTLType(returnType),"tom_get_fun_sym", type,args,tlCode, moduleName);
  }

  protected void buildGetImplementationDecl(int deep, String type, String typename,
                                            TomType tlType, Instruction instr, String moduleName) throws IOException {
    String argType;
    if(!lazyMode) {
      argType = TomBase.getTLCode(tlType);
    } else {
      argType = TomBase.getTLType(getUniversalType());
    }
    String returnType = argType;

    genDeclInstr(returnType,
            "tom_get_implementation", type,
            new String[] { argType, typename },
            instr,deep,moduleName);
  }

  protected void buildIsFsymDecl(int deep, String tomName, String varname,
                                 TomType tlType, Instruction instr, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();

    TomType returnType = getSymbolTable(moduleName).getBooleanType();
    String argType;
    if(!lazyMode) {
      argType = TomBase.getTLCode(tlType);
    } else {
      argType = TomBase.getTLType(getUniversalType());
    }

    genDeclInstr(TomBase.getTLType(returnType), "tom_is_fun_sym", opname, 
        new String[] { argType, varname }, instr,deep,moduleName);
  }

  protected void buildGetSlotDecl(int deep, String tomName, String varname,
                                  TomType tlType, Instruction instr, TomName slotName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();
    TomTypeList typesList = tomSymbol.getTypesToType().getDomain();

    int slotIndex = TomBase.getSlotIndex(tomSymbol,slotName);
    TomTypeList l = typesList;
    for(int index = 0; !l.isEmptyconcTomType() && index<slotIndex ; index++) {
      l = l.getTailconcTomType();
    }
    TomType returnType = l.getHeadconcTomType();

    String argType;
    if(!lazyMode) {
      argType = TomBase.getTLCode(tlType);
    } else {
      argType = TomBase.getTLType(getUniversalType());
    }
    genDeclInstr(TomBase.getTLType(returnType),
            "tom_get_slot", opname  + "_" + slotName.getString(),
            new String[] { argType, varname },
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

    genDecl(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_cmp_fun_sym", type1,
            new String[] {
              TomBase.getTLType(argType1), name1,
              TomBase.getTLType(argType2), name2
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

    genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_equal_term", type1,
            new String[] {
              TomBase.getTLType(argType1), name1,
              TomBase.getTLType(argType2), name2
            },
            instr,deep,moduleName);
  }

  protected void buildIsSortDecl(int deep, String name1, String type1, Instruction instr, String moduleName) throws IOException {
    TomType argType1 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type1)) {
      argType1 = getSymbolTable(moduleName).getBuiltinType(type1);
    }
    genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_is_sort", type1,
        new String[] { TomBase.getTLType(argType1), name1 },
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
      returnType = TomBase.getTLType(getUniversalType());
      argType = TomBase.getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          returnType = TomBase.getTLCode(codomain);
          argType = TomBase.getTLCode(domain);
        }

        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
          returnType = TomBase.getTLType(TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType());
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
      returnType = TomBase.getTLType(getUniversalType());
      argType = TomBase.getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          returnType = TomBase.getTLCode(tlType);
          argType = returnType;
        }

        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          returnType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
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
      argType = TomBase.getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          argType = TomBase.getTLCode(tlType);
        }

        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
        }
      }
    }

    genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()),
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
      returnType = TomBase.getTLType(getUniversalType());
      argType = TomBase.getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          returnType = TomBase.getTLType(getUniversalType());
          argType = TomBase.getTLCode(domain);
        }

        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
          returnType = TomBase.getTLType(TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType());
        }
      }
    }

    genDeclInstr(returnType,
            functionName, type1,
            new String[] {
              argType, name1,
              TomBase.getTLType(getSymbolTable(moduleName).getIntType()), name2
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
      argType = TomBase.getTLType(getUniversalType());
    } else {
      %match(TomName opNameAST) {
        EmptyName() -> {
          argType = TomBase.getTLCode(domain);
        }

        Name(opName) -> {
          TomSymbol tomSymbol = getSymbolFromName(`opName);
          argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
        }
      }
    }

    genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getIntType()),
            functionName, type,
            new String[] { argType, name1 },
            instr,deep,moduleName);
  }

  /*
   * the method implementations are here common to C, Java, caml and python
   */
  protected void buildExpGetHead(int deep, TomName opNameAST, TomType domain, TomType codomain, TomTerm var, String moduleName) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_head_" + TomBase.getTomType(domain) + "("); }
      Name(opName) -> { output.write("tom_get_head_" + `opName + "_" + TomBase.getTomType(domain) + "("); }
    }
    generate(deep,var,moduleName);
    output.write(")");
  }
  
  protected void buildExpGetElement(int deep, TomName opNameAST, TomType domain, TomType codomain, TomTerm varName, TomTerm varIndex, String moduleName) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_element_" + TomBase.getTomType(domain) + "("); }
      Name(opName) -> { output.write("tom_get_element_" + `opName + "_" + TomBase.getTomType(domain) + "("); }
    }

    generate(deep,varName,moduleName);
    output.write(",");
    generate(deep,varIndex,moduleName);
    output.write(")");
  }
 
  protected void buildListOrArray(int deep, TomTerm list, String moduleName) throws IOException {
    %match(TomTerm list) {
      BuildEmptyList(Name(name)) -> {
        output.write("tom_empty_list_" + `name + "()");
        return;
      }

      BuildConsList(Name(name), headTerm, tailTerm) -> {
        output.write("tom_cons_list_" + `name + "(");
        generate(deep,`headTerm,moduleName);
        output.write(",");
        generate(deep,`tailTerm,moduleName);
        output.write(")");
        return;
      }

      BuildAppendList(Name(name), headTerm, tailTerm) -> {
        output.write("tom_append_list_" + `name + "(");
        generate(deep,`headTerm,moduleName);
        output.write(",");
        generate(deep,`tailTerm,moduleName);
        output.write(")");
        return;
      }

      BuildEmptyArray(Name(name),size) -> {
        output.write("tom_empty_array_" + `name + "(" + `size + ")");
        return;
      }

      BuildConsArray(Name(name), headTerm, tailTerm) -> {
        output.write("tom_cons_array_" + `name + "(");
        generate(deep,`headTerm,moduleName);
        output.write(",");
        generate(deep,`tailTerm,moduleName);
        output.write(")");
        return;
      }

      BuildAppendArray(Name(name), headTerm, tailTerm) -> {
        output.write("tom_append_array_" + `name + "(");
        generate(deep,`headTerm,moduleName);
        output.write(",");
        generate(deep,`tailTerm,moduleName);
        output.write(")");
        return;
      }
    }
  }

  protected void buildFunctionCall(int deep, String name, TomList argList, String moduleName) throws IOException {
    output.write(name);
    output.writeOpenBrace();
    while(!argList.isEmptyconcTomTerm()) {
      generate(deep,argList.getHeadconcTomTerm(),moduleName);
      argList = argList.getTailconcTomTerm();
      if(!argList.isEmptyconcTomTerm()) {
        output.writeComa();
      }
    }
    output.writeCloseBrace();
  }


  protected void genDeclArray(String name, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }

    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = TomBase.getSymbolCodomain(tomSymbol);
    TomType eltType = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();

    String s = "";
    String tomType = TomBase.getTomType(listType);
    String glType = TomBase.getTLType(listType);
    //String tlEltType = getTLType(eltType);
    String utype = glType;
    if(lazyMode) {
      utype =  TomBase.getTLType(getUniversalType());
    }
    
    String listCast = "(" + glType + ")";
    String eltCast = "(" + TomBase.getTLType(eltType) + ")";
    String make_empty = listCast + "tom_empty_array_" + name;
    String make_insert = listCast + "tom_cons_array_" + name;
    String get_element = eltCast + "tom_get_element_" + name +"_" + tomType;
    String get_size = "tom_get_size_" + name +"_" + tomType;
    
    s = modifier + utype + " tom_get_slice_" + name +  "(" + utype + " subject, int begin, int end) {\n";
    s+= "   " + glType + " result = " + make_empty + "(end - begin);\n";
    s+= "    while( begin != end ) {\n";
    s+= "      result = " + make_insert + "(" + get_element + "(subject, begin),result);\n";
    s+= "      begin++;\n";
    s+= "     }\n";
    s+= "    return result;\n";
    s+= "  }\n";
    s+= "\n";
    
    s+= modifier + utype + " tom_append_array_" + name +  "(" + utype + " l2, " + utype + " l1) {\n";
    s+= "    int size1 = " + get_size + "(l1);\n";
    s+= "    int size2 = " + get_size + "(l2);\n";
    s+= "    int index;\n";
    s+= "   " + glType + " result = " + make_empty + "(size1+size2);\n";

    s+= "    index=size1;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_insert + "(" + get_element + "(l1,(size1-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";

    s+= "    index=size2;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_insert + "(" + get_element + "(l2,(size2-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";
   
    s+= "    return result;\n";
    s+= "  }\n";

    //If necessary we remove \n code depending on pretty option
    String res = ASTFactory.makeSingleLineCode(s, prettyMode);
    output.write(res);
  }

} // class TomGenericGenerator
