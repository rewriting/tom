/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2008, INRIA
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

  protected void buildExpEqualTerm(int deep, TomType type, TomTerm begin,TomTerm end, String moduleName) throws IOException {
    String sType = TomBase.getTomType(type);    
    String template = getSymbolTable(moduleName).getEqualTerm(sType);
    if(instantiateTemplate(deep,template,`concTomTerm(begin,end),moduleName) == false) {
      // if the type is null, it means that this is from Java
      if(sType == null || getSymbolTable(moduleName).isUnknownType(sType) || getSymbolTable(moduleName).isBooleanType(sType)) {
        output.write("(");
        generate(deep,begin,moduleName);
        output.write(" == ");
        generate(deep,end,moduleName);
        output.write(")");
      } else {
        output.write("tom_equal_term_" + sType + "(");
        generate(deep,begin,moduleName);
        output.write(", ");
        generate(deep,end,moduleName);
        output.write(")");
      }
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

    StringBuilder s = new StringBuilder();
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
    s.append(") {");
    output.writeln(s);
    
    String returnValue = getSymbolTable(moduleName).isVoidType(returnType)?tlCode.getCode():"return " + tlCode.getCode();
    %match(TargetLanguage tlCode) {
      TL(_,TextPosition[Line=startLine], TextPosition[Line=endLine]) -> {
        output.write(0,returnValue, `startLine, `endLine - `startLine);
        return;
      }

      ITL(_) -> {
        output.write(returnValue);
        return;
      }

    }
    output.write("}");
    output.writeln();
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

    StringBuilder s = new StringBuilder();
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
    s.append(") {");
    output.writeln(s);
    generateInstruction(deep,instr,moduleName);
    output.write("}");
    output.writeln();
  }

  private String getIsConcList(String name,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getIsFsym(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = %[tom_is_fun_sym_@name@(@subject@)]%;
    }
    return res;
  }

  private String getGetHead(String name,String type,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getGetHead(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = %[tom_get_head_@name@_@type@(@subject@)]%;
    }
    return res;
  }

  private String getGetTail(String name,String type,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getGetTail(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = %[tom_get_tail_@name@_@type@(@subject@)]%;
    }
    return res;
  }

  private String getIsEmptyList(String name,String type,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getIsEmptyList(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = %[tom_is_empty_@name@_@type@(@subject@)]%;
    }
    return res;
  }

  private String getMakeAddList(String name,String head, String tail,String moduleName) {
    String template = getSymbolTable(moduleName).getMakeAddList(name);
    String res = instantiateTemplate(template,head,tail);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = %[tom_cons_list_@name@(@head@,@tail@)]%;
    }
    return res;
  }

  private String getMakeEmptyList(String name,String moduleName) {
    String res = getSymbolTable(moduleName).getMakeEmptyList(name);
    if(!inlineplus) {
      String prefix = "tom_empty_list_";
      res = %[tom_empty_list_@name@()]%;
    }
    return res;
  }

  private String genDeclGetHead(String name, TomType domain, TomType codomain, String subject, String moduleName) {
    String tomType = TomBase.getTomType(codomain);
    String get = getGetHead(name,tomType,subject,moduleName);
    String is_conc = getIsConcList(name,subject,moduleName);
    if(domain==codomain) {
      return %[((@is_conc@)?@get@:@subject@)]%;
    }
    return get;
  }

  private String genDeclGetTail(String name, TomType domain, TomType codomain, String subject,String moduleName) {
    String tomType = TomBase.getTomType(codomain);
    String get= getGetTail(name,tomType,subject,moduleName);
    String is_conc = getIsConcList(name,subject,moduleName);
    String empty = getMakeEmptyList(name,moduleName);
    if(domain==codomain) {
      return %[((@is_conc@)?@get@:@empty@)]%;
    }
    return get;
  }

  private String getEqualTerm(String type,String arg1, String arg2,String moduleName) {
    String template = getSymbolTable(moduleName).getEqualTerm(type);
    String res = instantiateTemplate(template,arg1,arg2);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = %[tom_equal_term_@type@(@arg1@,@arg2@)]%;
    }
    return res;
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
    String get_slice = listCast + "tom_get_slice_" + name;

    String s = "";
    if(listType == eltType) {
s = %[
  @modifier@ @utype@ tom_append_list_@name@(@utype@ l1, @utype@ l2) {
    if(@getIsEmptyList(name,tomType,"l1",moduleName)@) {
      return l2;
    } else if(@getIsEmptyList(name,tomType,"l2",moduleName)@) {
      return l1;
    } else if(@getIsConcList(name,"l1",moduleName)@) {
      if(@getIsEmptyList(name,tomType,genDeclGetTail(name,eltType,listType,"l1",moduleName),moduleName)@) {
        return @getMakeAddList(name,genDeclGetHead(name,eltType,listType,"l1",moduleName),"l2",moduleName)@;
      } else {
        return @getMakeAddList(name,genDeclGetHead(name,eltType,listType,"l1",moduleName),
                              "tom_append_list_"+name+"("+genDeclGetTail(name,eltType,listType,"l1",moduleName)+",l2)",moduleName)@;
      }
    } else {
      return @getMakeAddList(name,"l1", "l2",moduleName)@;
    }
  }]%;

    } else {

s = %[
  @modifier@ @utype@ tom_append_list_@name@(@utype@l1, @utype@ l2) {
    if(@getIsEmptyList(name,tomType,"l1",moduleName)@) {
      return l2;
    } else if(@getIsEmptyList(name,tomType,"l2",moduleName)@) {
      return l1;
    } else if(@getIsEmptyList(name,tomType,genDeclGetTail(name,eltType,listType,"l1",moduleName),moduleName)@) {
      return @getMakeAddList(name,genDeclGetHead(name,eltType,listType,"l1",moduleName),"l2",moduleName)@;
    } else {
      return @getMakeAddList(name,genDeclGetHead(name,eltType,listType,"l1",moduleName),
                                 "tom_append_list_"+name+"("+genDeclGetTail(name,eltType,listType,"l1",moduleName)+",l2)",moduleName)@;
    }
  }]%;

    }

    int deep=0;
    s+= %[
  @modifier@ @utype@ tom_get_slice_@name@(@utype@ begin, @utype@ end,@utype@ tail) {
    if(@getEqualTerm(tomType,"begin","end",moduleName)@) {
      return tail;
    } else if(@getEqualTerm(tomType,"end","tail",moduleName)@ && (@getIsEmptyList(name,tomType,"end",moduleName)@ || @getEqualTerm(tomType,"end",getMakeEmptyList(name,moduleName),moduleName)@)) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return @getMakeAddList(name,genDeclGetHead(name,eltType,listType,"begin",moduleName),
                  get_slice+"("+genDeclGetTail(name,eltType,listType,"begin",moduleName)+",end,tail)",moduleName)@;
  }
  ]%;

    //If necessary we remove \n code depending on pretty option
    s = ASTFactory.makeSingleLineCode(s, prettyMode);
    output.write(s);
  }

  
  /**
   * Generates a function that given a list, it returns an array with the multiplicity of the elements
   * Ex:
   * 
   * f(a,a,a,b,b) -> the function computes the multiplicities [3,2] and also the array [a,b]
   * 
   * NOTE: we are sure to have the same domain and codomain
   * TODO ? lazymode
   */
  protected void genGetMultiplicityFunction(String name, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }

    SymbolTable symbolTable = getSymbolTable(moduleName);
    
    TomSymbol tomSymbol = symbolTable.getSymbolFromName(name);
    TomType elemType = TomBase.getSymbolCodomain(tomSymbol);
    String tomType = TomBase.getTomType(elemType);
    String glType = TomBase.getTLType(elemType);
    // the array type
    TomType elemArrayType = `ArrayType(elemType,1);
    String tlElemArrayType = TomBase.getTLType(elemArrayType);    
    // the int[] type
    TomType intArrayType = `ArrayType(symbolTable.getIntType(),1);
    String tlIntArrayType = `TomBase.getTLType(intArrayType);
   
    String listCast = "(" + glType + ")";
    String get_slice = listCast + "tom_get_slice_" + name;

    String s = "";
    s+= %[
  @modifier@ @tlIntArrayType@ tom_get_multiplicity_@name@(@glType@ subjTerm, @tlElemArrayType@ terms) {
    @tlIntArrayType@ multiplicities 
    
    
    
    
    if(@getEqualTerm(tomType,"begin","end",moduleName)@) {
      return tail;
    } else if(@getEqualTerm(tomType,"end","tail",moduleName)@ && (@getIsEmptyList(name,tomType,"end",moduleName)@ || @getEqualTerm(tomType,"end",getMakeEmptyList(name,moduleName),moduleName)@)) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return @getMakeAddList(name,genDeclGetHead(name,eltType,listType,"begin",moduleName),
                  get_slice+"("+genDeclGetTail(name,eltType,listType,"begin",moduleName)+",end,tail)",moduleName)@;
  }
  ]%;

    //If necessary we remove \n code depending on pretty option
    s = ASTFactory.makeSingleLineCode(s, prettyMode);
    output.write(s);
  }
  
  //TODO - the computeLenght

  protected void genDeclMake(String prefix, String funName, TomType returnType,
                             TomList argList, Instruction instr, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }

    boolean inlined = inlineplus;
    boolean isCode = false;
    %match(instr) {
      ExpressionToInstruction(Code(code)) -> {
        isCode = true;
        // perform the instantiation
        String ncode = `code;
        int index = 0;
        %match(argList) {
          concTomTerm(_*,Variable[AstName=Name(varname)],_*) -> {
            ncode = ncode.replace("{"+index+"}",`varname);
            index++;
          }
        }

        if(!ncode.equals(`code)) {
          inlined = true;
          instr = `ExpressionToInstruction(Code(ncode));
        }
      }
    }
    if(!inline || !isCode || !inlined) {
      StringBuilder s = new StringBuilder();
      s.append(modifier + TomBase.getTLType(returnType) + " " + prefix + funName + "(");
      while(!argList.isEmptyconcTomTerm()) {
        TomTerm arg = argList.getHeadconcTomTerm();
matchBlock: {
              %match(TomTerm arg) {
                Variable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]] -> {
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
      output.writeln(s);
      output.write("return ");
      generateInstruction(0,instr,moduleName);
      output.writeln(";");
      output.writeln("}");
    }
  }
}
