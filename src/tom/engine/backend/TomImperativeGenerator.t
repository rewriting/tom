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

package tom.engine.backend;

import java.io.IOException;

import tom.engine.exception.TomRuntimeException;

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

import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.platform.OptionManager;

public abstract class TomImperativeGenerator extends TomGenericGenerator {

  protected String modifier = "";
  protected boolean nodeclMode;


  public TomImperativeGenerator(OutputCode output, OptionManager optionManager,
                                SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
    nodeclMode = ((Boolean)optionManager.getOptionValue("noDeclaration")).booleanValue();
  }

  // ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  /*
   * the method implementations are here common to C, Java, caml and python
   */



  protected void buildExpGetHead(int deep, TomName opNameAST, TomType domain, TomType codomain, TomTerm var, String moduleName) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_head_" + getTomType(domain) + "("); }
      Name(opName) -> { output.write("tom_get_head_" + `opName + "_" + getTomType(domain) + "("); }
    }
    generate(deep,var,moduleName);
    output.write(")");
  }
  
  protected void buildExpGetElement(int deep, TomName opNameAST, TomType domain, TomType codomain, TomTerm varName, TomTerm varIndex, String moduleName) throws IOException {
    %match(TomName opNameAST) {
      EmptyName() -> { output.write("tom_get_element_" + getTomType(domain) + "("); }
      Name(opName) -> { output.write("tom_get_element_" + `opName + "_" + getTomType(domain) + "("); }
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
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = getSymbolCodomain(tomSymbol);
    TomType eltType = getSymbolDomain(tomSymbol).getHeadconcTomType();

    String s = "";
    if(nodeclMode) {
      return;
    }

    String tomType = getTomType(listType);
    String glType = getTLType(listType);
    String tlEltType = getTLType(eltType);
    String utype = glType;
    if(lazyMode) {
      utype =  getTLType(getUniversalType());
    }
    
    String listCast = "(" + glType + ")";
    String eltCast = "(" + getTLType(eltType) + ")";
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
    TargetLanguage itl = ASTFactory.reworkTLCode(`ITL(s), prettyMode);
    output.write(itl.getCode()); 
  }

  
} // class TomImperativeGenerator
