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

import tom.engine.TomBase;
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

public abstract class TomCFamilyGenerator extends TomGenericGenerator {

  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------


  public TomCFamilyGenerator(OutputCode output, OptionManager optionManager,
                                SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
  }
  
   protected void buildAssignVar(int deep, TomTerm var, OptionList list, Expression exp, String moduleName) throws IOException {
    //output.indent(deep);
    generate(deep,var,moduleName);
    output.write("=");
    generateExpression(deep,exp,moduleName);
    output.writeln(";");
  } 

  protected void buildComment(int deep, String text) throws IOException {
    output.writeln("/* " + text + " */");
    return;
  }

  protected void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException {
    output.writeln(deep,"do {");
    generateInstruction(deep+1,succes,moduleName);
    output.write(deep,"} while(");
    generateExpression(deep,exp,moduleName);
    output.writeln(");");
  }

  protected void buildExpEqualTerm(int deep, TomType type, TomTerm exp1,TomTerm exp2, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBooleanType(TomBase.getTomType(`type))) {
      output.write("(");
      generate(deep,exp1,moduleName);
      output.write(" == ");
      generate(deep,exp2,moduleName);
      output.write(")");
    } else {
      output.write("tom_equal_term_" + TomBase.getTomType(type) + "(");
      generate(deep,exp1,moduleName);
      output.write(", ");
      generate(deep,exp2,moduleName);
      output.write(")");
    }
  }

  protected void buildExpConditional(int deep, Expression cond,Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write("((");
    generateExpression(deep,cond,moduleName);
    output.write(")?(");
    generateExpression(deep,exp1,moduleName);
    output.write("):(");
    generateExpression(deep,exp2,moduleName);
    output.write("))");
  }

  protected void buildExpAnd(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
	output.write(" ( ");
	generateExpression(deep,exp1,moduleName);
    output.write(" && ");
    generateExpression(deep,exp2,moduleName);
    output.write(" ) ");
  }

  protected void buildExpOr(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
	output.write(" ( ");  
    generateExpression(deep,exp1,moduleName);
    output.write(" || ");
    generateExpression(deep,exp2,moduleName);
    output.write(" ) ");
  }
 
  protected void buildExpCast(int deep, TomType tlType, Expression exp, String moduleName) throws IOException {
    output.write("((" + TomBase.getTLCode(tlType) + ")");
    generateExpression(deep,exp,moduleName);
    output.write(")");
  }
 
  protected void buildExpNegation(int deep, Expression exp, String moduleName) throws IOException {
    output.write("!(");
    generateExpression(deep,exp,moduleName);
    output.write(")");
  }

  protected void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write(deep,"if ("); 
    generateExpression(deep,exp, moduleName); 
    output.writeln(") {");
    generateInstruction(deep+1,succes, moduleName);
    output.writeln(deep,"}");
  }
  
  protected void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException {
    output.write(deep,"if ("); 
    generateExpression(deep,exp,moduleName); 
    output.writeln(") {");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln(deep,"} else {");
    generateInstruction(deep+1,failure,moduleName);
    output.writeln(deep,"}");
  }

  protected void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException {
    generateInstructionList(deep, instructionList, moduleName);
    return;
  }

  protected void buildLet(int deep, TomTerm var, OptionList optionList, TomType tlType, 
                          Expression exp, Instruction body, String moduleName) throws IOException {
    output.write(deep,"{ " + TomBase.getTLCode(tlType) + " ");
    buildAssignVar(deep,var,optionList,exp,moduleName);
    generateInstruction(deep,body,moduleName);
    output.writeln(deep,"}");
  }
  
  protected void buildLetRef(int deep, TomTerm var, OptionList optionList, TomType tlType, 
                             Expression exp, Instruction body, String moduleName) throws IOException {
    buildLet(deep,var,optionList,tlType,exp,body, moduleName);
  }
 
  protected void buildLetAssign(int deep, TomTerm var, OptionList list, Expression exp, Instruction body, String moduleName) throws IOException {
    buildAssignVar(deep, var, list, exp, moduleName);
    generateInstruction(deep,body, moduleName);
  }

  protected void buildRef(int deep, TomTerm term, String moduleName) throws IOException {
    generate(deep,term,moduleName);
  }

  protected void buildReturn(int deep, TomTerm exp, String moduleName) throws IOException {
    output.write(deep,"return ");
    generate(deep,exp,moduleName);
    output.writeln(deep,";");
  }

  protected void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException {
    output.writeln(deep, "{");
    generateInstructionList(deep+1,instList, moduleName);
    output.writeln(deep, "}");
  }
 
  protected void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write(deep,"while (");
    generateExpression(deep,exp,moduleName);
    output.writeln(") {");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln(deep,"}");
  }

  protected void genDecl(String returnType,
                         String declName,
                         String suffix,
                         String args[],
                         TargetLanguage tlCode,
                         String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }

    StringBuffer s = new StringBuffer();
    s.append(modifier);
    s.append(returnType);
    s.append(" ");
    s.append(declName);
    s.append("_");
    s.append(suffix);
    s.append("(");
    for(int i=0 ; i<args.length ; ) {
      s.append(args[i]); // parameter type
      s.append(" ");
      s.append(args[i+1]); // parameter name
      i+=2;
      if(i<args.length) {
        s.append(", ");
      }
    } 
    String returnValue = getSymbolTable(moduleName).isVoidType(returnType)?tlCode.getCode():"return " + tlCode.getCode();
    s.append(") { " + returnValue + "; }");

    %match(TargetLanguage tlCode) {
      TL(_,TextPosition[Line=startLine], TextPosition[Line=endLine]) -> {
        output.write(0,s, `startLine, `endLine - `startLine);
        return;
      }

      ITL(_) -> {
        output.write(s);
        return;
      }

    }
  }

  protected void genDeclInstr(String returnType,
                         String declName,
                         String suffix,
                         String args[],
                         Instruction instr,
                         int deep, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }

    StringBuffer s = new StringBuffer();
    s.append(modifier);
    s.append(returnType);
    s.append(" ");
    s.append(declName);
    s.append("_");
    s.append(suffix);
    s.append("(");
    for(int i=0 ; i<args.length ; ) {
      s.append(args[i]); // parameter type
      s.append(" ");
      s.append(args[i+1]); // parameter name
      i+=2;
      if(i<args.length) {
        s.append(", ");
      }
    } 
    s.append(") { ");
    output.write(s);
    generateInstruction(deep,instr,moduleName);
    output.write("}");
    output.writeln();
  }

  private String genDeclGetHead(String name, TomType domain, TomType codomain, String subject) {
    String tomType = TomBase.getTomType(codomain);
    String get = %[tom_get_head_@name@_@tomType@(@subject@)]%;
    String is_conc = %[tom_is_fun_sym_@name@(@subject@)]%;
    String cast = "";// "(" + TomBase.getTLType(domain) + ")";
    if(domain==codomain) { 
      return cast+%[((@is_conc@)?@get@:@subject@)]%;
    }
    return cast+get;
  }
  private String genDeclGetTail(String name, TomType domain, TomType codomain, String subject) {
    String tomType = TomBase.getTomType(codomain);
    String get= %[tom_get_tail_@name@_@tomType@(@subject@)]%;
    String is_conc = %[tom_is_fun_sym_@name@(@subject@)]%;
    String empty = %[tom_empty_list_@name@()]%;
    String cast = ""; //"(" + TomBase.getTLType(codomain)+ ")";
    if(domain==codomain) { 
      return cast+%[((@is_conc@)?@get@:@empty@)]%;
    }
    return cast+get;
  }

  protected void genDeclList(String name, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }

    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = TomBase.getSymbolCodomain(tomSymbol);
    TomType eltType = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    String tomType = TomBase.getTomType(listType);
    String glType = TomBase.getTLType(listType);

    String utype = glType;
    if(lazyMode) {
      utype = TomBase.getTLType(getUniversalType());
    }
    
    String listCast = "(" + glType + ")";
    String is_empty = "tom_is_empty_" + name + "_" + tomType;
    String is_conc = "tom_is_fun_sym_" + name;
    String equal_term = "tom_equal_term_" + tomType;
    String make_insert = listCast + "tom_cons_list_" + name;
    String make_empty = listCast + "tom_empty_list_" + name;
    String get_slice = listCast + "tom_get_slice_" + name;
    String get_index = "tom_get_index_" + name;

    String s = "";
    if(listType == eltType) {
s = %[
  @modifier@ @utype@ tom_append_list_@name@(@utype@l1, @utype@ l2) {
    if(@is_empty@(l1)) {
      return l2;
    } else if(@is_empty@(l2)) {
      return l1;
    } else if(@is_conc@(l1)) {
      if(@is_empty@(@genDeclGetTail(name,eltType,listType,"l1")@)) {
        return @make_insert@(@genDeclGetHead(name,eltType,listType,"l1")@,l2);
      } else {
        return @make_insert@(@genDeclGetHead(name,eltType,listType,"l1")@,tom_append_list_@name@(@genDeclGetTail(name,eltType,listType,"l1")@,l2));
      }
    } else {
      return @make_insert@(l1, l2);
    }
  }]%;

    } else {

s = %[
  @modifier@ @utype@ tom_append_list_@name@(@utype@l1, @utype@ l2) {
    if(@is_empty@(l1)) {
      return l2;
    } else if(@is_empty@(l2)) {
      return l1;
    } else if(@is_empty@(@genDeclGetTail(name,eltType,listType,"l1")@)) {
      return @make_insert@(@genDeclGetHead(name,eltType,listType,"l1")@,l2);
    } else {
      return @make_insert@(@genDeclGetHead(name,eltType,listType,"l1")@,tom_append_list_@name@(@genDeclGetTail(name,eltType,listType,"l1")@,l2));
    }
  }]%;

    }

    s+= %[
  @modifier@ @utype@ tom_get_slice_@name@(@utype@ begin, @utype@ end,@utype@ tail) {
    if(@equal_term@(begin,end)) {
      return tail;
    } else {
      return @make_insert@(@genDeclGetHead(name,eltType,listType,"begin")@,@get_slice@(@genDeclGetTail(name,eltType,listType,"begin")@,end,tail));
    }
  }
  ]%;
   
    //If necessary we remove \n code depending on pretty option
    s = ASTFactory.makeSingleLineCode(s, prettyMode);
    output.write(s);
  }

  protected void genDeclMake(String funName, TomType returnType, 
                             TomList argList, Instruction instr, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }

    StringBuffer s = new StringBuffer();
    s.append(modifier + TomBase.getTLType(returnType) + " " + funName + "(");
    while(!argList.isEmptyconcTomTerm()) {
      TomTerm arg = argList.getHeadconcTomTerm();
      matchBlock: {
        %match(TomTerm arg) {
          Variable[AstName=Name(name), AstType=Type[TomType=tomType,TlType=tlType@TLType[]]] -> {
            s.append(TomBase.getTLCode(`tlType) + " " + `name);
            break matchBlock;
          }
            
          _ -> {
            System.out.println("genDeclMake: strange term: " + arg);
            throw new TomRuntimeException("genDeclMake: strange term: " + arg);
          }
        }
      }
      argList = argList.getTailconcTomTerm();
      if(!argList.isEmptyconcTomTerm()) {
        s.append(", ");
      }
    }
    s.append(") { ");
    output.write(s);
    output.write("return ");
    generateInstruction(0,instr,moduleName);
    output.write("; }");
  }
}
