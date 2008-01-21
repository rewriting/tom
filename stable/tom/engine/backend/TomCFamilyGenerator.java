/* Generated by TOM (version 2.6alpha): Do not edit this file *//*
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
  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */  /* Generated by TOM (version 2.6alpha): Do not edit this file */    private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;     }   }    
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
    if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(begin, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(end, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ) ,moduleName) == false) {
      if(getSymbolTable(moduleName).isBooleanType(sType)) {
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
    String returnValue = getSymbolTable(moduleName).isVoidType(returnType)?tlCode.getCode():"return " + tlCode.getCode();
    s.append(") { " + returnValue + "; }");

    {if ( (tlCode instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {{  tom.engine.adt.tomsignature.types.TargetLanguage  tomMatch49NameNumberfreshSubject_1=(( tom.engine.adt.tomsignature.types.TargetLanguage )tlCode);if ( (tomMatch49NameNumberfreshSubject_1 instanceof tom.engine.adt.tomsignature.types.targetlanguage.TL) ) {{  String  tomMatch49NameNumber_freshVar_0= tomMatch49NameNumberfreshSubject_1.getCode() ;{  tom.engine.adt.tomsignature.types.Position  tomMatch49NameNumber_freshVar_1= tomMatch49NameNumberfreshSubject_1.getStart() ;{  tom.engine.adt.tomsignature.types.Position  tomMatch49NameNumber_freshVar_2= tomMatch49NameNumberfreshSubject_1.getEnd() ;if ( (tomMatch49NameNumber_freshVar_1 instanceof tom.engine.adt.tomsignature.types.position.TextPosition) ) {{  int  tomMatch49NameNumber_freshVar_3= tomMatch49NameNumber_freshVar_1.getLine() ;{  int  tom_startLine=tomMatch49NameNumber_freshVar_3;if ( (tomMatch49NameNumber_freshVar_2 instanceof tom.engine.adt.tomsignature.types.position.TextPosition) ) {{  int  tomMatch49NameNumber_freshVar_4= tomMatch49NameNumber_freshVar_2.getLine() ;if ( true ) {

        output.write(0,s, tom_startLine, tomMatch49NameNumber_freshVar_4- tom_startLine);
        return;
      }}}}}}}}}}}}if ( (tlCode instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {{  tom.engine.adt.tomsignature.types.TargetLanguage  tomMatch49NameNumberfreshSubject_1=(( tom.engine.adt.tomsignature.types.TargetLanguage )tlCode);if ( (tomMatch49NameNumberfreshSubject_1 instanceof tom.engine.adt.tomsignature.types.targetlanguage.ITL) ) {{  String  tomMatch49NameNumber_freshVar_5= tomMatch49NameNumberfreshSubject_1.getCode() ;if ( true ) {


        output.write(s);
        return;
      }}}}}}


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
    s.append(") { ");
    output.write(s);
    generateInstruction(deep,instr,moduleName);
    output.write("}");
    output.writeln();
  }

  private String getIsConcList(String name,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getIsFsym(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res == template)) {
      res = "tom_is_fun_sym_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+name+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+subject+")";
    }
    return res;
  }

  private String getGetHead(String name,String type,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getGetHead(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res == template)) {
      res = "tom_get_head_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+name+"_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+type+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+subject+")";
    }
    return res;
  }

  private String getGetTail(String name,String type,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getGetTail(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res == template)) {
      res = "tom_get_tail_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+name+"_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+type+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+subject+")";
    }
    return res;
  }

  private String getIsEmptyList(String name,String type,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getIsEmptyList(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res == template)) {
      res = "tom_is_empty_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+name+"_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+type+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+subject+")";
    }
    return res;
  }

  private String getMakeAddList(String name,String head, String tail,String moduleName) {
    String template = getSymbolTable(moduleName).getMakeAddList(name);
    String res = instantiateTemplate(template,head,tail);
    if(res==null || (!inlineplus && res == template)) {
      res = "tom_cons_list_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+name+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+head+","/* Generated by TOM (version 2.6alpha): Do not edit this file */+tail+")";
    }
    return res;
  }

  private String getMakeEmptyList(String name,String moduleName) {
    String res = getSymbolTable(moduleName).getMakeEmptyList(name);
    if(!inlineplus) {
      String prefix = "tom_empty_list_";
      res = "tom_empty_list_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+name+"()";
    }
    return res;
  }

  private String genDeclGetHead(String name, TomType domain, TomType codomain, String subject, String moduleName) {
    String tomType = TomBase.getTomType(codomain);
    String get = getGetHead(name,tomType,subject,moduleName);
    String is_conc = getIsConcList(name,subject,moduleName);
    if(domain==codomain) { 
      return "(("/* Generated by TOM (version 2.6alpha): Do not edit this file */+is_conc+")?"/* Generated by TOM (version 2.6alpha): Do not edit this file */+get+":"/* Generated by TOM (version 2.6alpha): Do not edit this file */+subject+")";
    }
    return get;
  }

  private String genDeclGetTail(String name, TomType domain, TomType codomain, String subject,String moduleName) {
    String tomType = TomBase.getTomType(codomain);
    String get= getGetTail(name,tomType,subject,moduleName);
    String is_conc = getIsConcList(name,subject,moduleName);
    String empty = getMakeEmptyList(name,moduleName);
    if(domain==codomain) { 
      return "(("/* Generated by TOM (version 2.6alpha): Do not edit this file */+is_conc+")?"/* Generated by TOM (version 2.6alpha): Do not edit this file */+get+":"/* Generated by TOM (version 2.6alpha): Do not edit this file */+empty+")";
    }
    return get;
  }
  
  private String getEqualTerm(String type,String arg1, String arg2,String moduleName) {
    String template = getSymbolTable(moduleName).getEqualTerm(type);
    String res = instantiateTemplate(template,arg1,arg2);
    if(res==null || (!inlineplus && res == template)) {
      res = "tom_equal_term_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+type+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+arg1+","/* Generated by TOM (version 2.6alpha): Do not edit this file */+arg2+")";
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
s = "\n  "/* Generated by TOM (version 2.6alpha): Do not edit this file */+modifier+" "/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+" tom_append_list_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+name+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+" l1, "/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+" l2) {\n    if("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getIsEmptyList(name,tomType,"l1",moduleName)+") {\n      return l2;\n    } else if("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getIsEmptyList(name,tomType,"l2",moduleName)+") {\n      return l1;\n    } else if("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getIsConcList(name,"l1",moduleName)+") {\n      if("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getIsEmptyList(name,tomType,genDeclGetTail(name,eltType,listType,"l1",moduleName),moduleName)+") {\n        return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getMakeAddList(name,genDeclGetHead(name,eltType,listType,"l1",moduleName),"l2",moduleName)+";\n      } else {\n        return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getMakeAddList(name,genDeclGetHead(name,eltType,listType,"l1",moduleName),                               "tom_append_list_"+name+"("+genDeclGetTail(name,eltType,listType,"l1",moduleName)+",l2)",moduleName)+";\n      }\n    } else {\n      return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getMakeAddList(name,"l1", "l2",moduleName)+";\n    }\n  }"















;

    } else {

s = "\n  "/* Generated by TOM (version 2.6alpha): Do not edit this file */+modifier+" "/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+" tom_append_list_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+name+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+"l1, "/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+" l2) {\n    if("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getIsEmptyList(name,tomType,"l1",moduleName)+") {\n      return l2;\n    } else if("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getIsEmptyList(name,tomType,"l2",moduleName)+") {\n      return l1;\n    } else if("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getIsEmptyList(name,tomType,genDeclGetTail(name,eltType,listType,"l1",moduleName),moduleName)+") {\n      return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getMakeAddList(name,genDeclGetHead(name,eltType,listType,"l1",moduleName),"l2",moduleName)+";\n    } else {\n      return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getMakeAddList(name,genDeclGetHead(name,eltType,listType,"l1",moduleName),                                  "tom_append_list_"+name+"("+genDeclGetTail(name,eltType,listType,"l1",moduleName)+",l2)",moduleName)+";\n    }\n  }"











;

    }

    int deep=0;
    s+= "\n  "/* Generated by TOM (version 2.6alpha): Do not edit this file */+modifier+" "/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+" tom_get_slice_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+name+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+" begin, "/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+" end,"/* Generated by TOM (version 2.6alpha): Do not edit this file */+utype+" tail) {\n    if("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getEqualTerm(tomType,"begin","end",moduleName)+") {\n      return tail;\n    } else if("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getEqualTerm(tomType,"end","tail",moduleName)+" && ("/* Generated by TOM (version 2.6alpha): Do not edit this file */+getIsEmptyList(name,tomType,"end",moduleName)+" || "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getEqualTerm(tomType,"end",getMakeEmptyList(name,moduleName),moduleName)+")) {\n      /* code to avoid a call to make, and thus to avoid looping during list-matching */\n      return begin;\n    }\n    return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getMakeAddList(name,genDeclGetHead(name,eltType,listType,"begin",moduleName),                   get_slice+"("+genDeclGetTail(name,eltType,listType,"begin",moduleName)+",end,tail)",moduleName)+";\n  }\n  "










;
   
    //If necessary we remove \n code depending on pretty option
    s = ASTFactory.makeSingleLineCode(s, prettyMode);
    output.write(s);
  }

  protected void genDeclMake(String prefix, String funName, TomType returnType, 
                             TomList argList, Instruction instr, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }

    boolean inlined = inlineplus;
    boolean isCode = false;
    {if ( (instr instanceof tom.engine.adt.tominstruction.types.Instruction) ) {{  tom.engine.adt.tominstruction.types.Instruction  tomMatch50NameNumberfreshSubject_1=(( tom.engine.adt.tominstruction.types.Instruction )instr);if ( (tomMatch50NameNumberfreshSubject_1 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {{  tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_0= tomMatch50NameNumberfreshSubject_1.getExpr() ;if ( (tomMatch50NameNumber_freshVar_0 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {{  String  tomMatch50NameNumber_freshVar_1= tomMatch50NameNumber_freshVar_0.getCode() ;{  String  tom_code=tomMatch50NameNumber_freshVar_1;if ( true ) {

        isCode = true;
        // perform the instantiation
        String ncode = tom_code;
        int index = 0;
        {if ( (argList instanceof tom.engine.adt.tomterm.types.TomList) ) {{  tom.engine.adt.tomterm.types.TomList  tomMatch51NameNumberfreshSubject_1=(( tom.engine.adt.tomterm.types.TomList )argList);if ( ((tomMatch51NameNumberfreshSubject_1 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch51NameNumberfreshSubject_1 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {{  tom.engine.adt.tomterm.types.TomList  tomMatch51NameNumber_freshVar_0=tomMatch51NameNumberfreshSubject_1;{  tom.engine.adt.tomterm.types.TomList  tomMatch51NameNumber_begin_2=tomMatch51NameNumber_freshVar_0;{  tom.engine.adt.tomterm.types.TomList  tomMatch51NameNumber_end_3=tomMatch51NameNumber_freshVar_0;do {{{  tom.engine.adt.tomterm.types.TomList  tomMatch51NameNumber_freshVar_1=tomMatch51NameNumber_end_3;if (!( tomMatch51NameNumber_freshVar_1.isEmptyconcTomTerm() )) {if ( ( tomMatch51NameNumber_freshVar_1.getHeadconcTomTerm()  instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{  tom.engine.adt.tomname.types.TomName  tomMatch51NameNumber_freshVar_6=  tomMatch51NameNumber_freshVar_1.getHeadconcTomTerm() .getAstName() ;if ( (tomMatch51NameNumber_freshVar_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {{  String  tomMatch51NameNumber_freshVar_7= tomMatch51NameNumber_freshVar_6.getString() ;{  tom.engine.adt.tomterm.types.TomList  tomMatch51NameNumber_freshVar_4= tomMatch51NameNumber_freshVar_1.getTailconcTomTerm() ;if ( true ) {

            ncode = ncode.replace("{"+index+"}",tomMatch51NameNumber_freshVar_7); 
            index++;
          }}}}}}}}if ( tomMatch51NameNumber_end_3.isEmptyconcTomTerm() ) {tomMatch51NameNumber_end_3=tomMatch51NameNumber_begin_2;} else {tomMatch51NameNumber_end_3= tomMatch51NameNumber_end_3.getTailconcTomTerm() ;}}} while(!( tomMatch51NameNumber_end_3.equals(tomMatch51NameNumber_begin_2) ));}}}}}}}


        if(!ncode.equals(tom_code)) {
          inlined = true;
          instr =  tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction.make( tom.engine.adt.tomexpression.types.expression.Code.make(ncode) ) ;
        }
      }}}}}}}}}

    if(!inline || !isCode || !inlined) {
      StringBuilder s = new StringBuilder();
      s.append(modifier + TomBase.getTLType(returnType) + " " + prefix + funName + "(");
      while(!argList.isEmptyconcTomTerm()) {
        TomTerm arg = argList.getHeadconcTomTerm();
matchBlock: {
              {if ( (arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch52NameNumberfreshSubject_1=(( tom.engine.adt.tomterm.types.TomTerm )arg);if ( (tomMatch52NameNumberfreshSubject_1 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{  tom.engine.adt.tomname.types.TomName  tomMatch52NameNumber_freshVar_0= tomMatch52NameNumberfreshSubject_1.getAstName() ;{  tom.engine.adt.tomtype.types.TomType  tomMatch52NameNumber_freshVar_1= tomMatch52NameNumberfreshSubject_1.getAstType() ;if ( (tomMatch52NameNumber_freshVar_0 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {{  String  tomMatch52NameNumber_freshVar_2= tomMatch52NameNumber_freshVar_0.getString() ;if ( (tomMatch52NameNumber_freshVar_1 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{  tom.engine.adt.tomtype.types.TomType  tomMatch52NameNumber_freshVar_3= tomMatch52NameNumber_freshVar_1.getTlType() ;if ( (tomMatch52NameNumber_freshVar_3 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {if ( true ) {

                  s.append(TomBase.getTLCode(tomMatch52NameNumber_freshVar_3) + " " + tomMatch52NameNumber_freshVar_2);
                  break matchBlock;
                }}}}}}}}}}}if ( (arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch52NameNumberfreshSubject_1=(( tom.engine.adt.tomterm.types.TomTerm )arg);if ( true ) {


                  System.out.println("genDeclMake: strange term: " + arg);
                  throw new TomRuntimeException("genDeclMake: strange term: " + arg);
                }}}}

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
}
