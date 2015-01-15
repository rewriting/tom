/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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
import java.util.LinkedList;

import tom.engine.exception.TomRuntimeException;

import tom.engine.TomBase;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.platform.OptionManager;

public class CamlGenerator extends GenericGenerator {
  protected LinkedList<BQTerm> env = new LinkedList<BQTerm>();

  public CamlGenerator(OutputCode output, OptionManager optionManager, SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
  }
  
  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------
  
  /*
   * the implementation of methods are here for caml 
   */
 
  protected void buildExpEqualTerm(int deep, TomType type, BQTerm exp1, TomTerm exp2, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBooleanType(TomBase.getTomType(`type))) {
      output.write("(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(" = ");
      generateTomTerm(deep,exp2,moduleName);
      output.write(")");
    } else {
      output.write("tom_equal_term_" + TomBase.getTomType(type) + "(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(", ");
      generateTomTerm(deep,exp2,moduleName);
      output.write(")");
    }
  }

  protected void buildExpEqualBQTerm(int deep, TomType type, BQTerm exp1, BQTerm exp2, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBooleanType(TomBase.getTomType(`type))) {
      output.write("(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(" = ");
      generateBQTerm(deep,exp2,moduleName);
      output.write(")");
    } else {
      output.write("tom_equal_term_" + TomBase.getTomType(type) + "(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(", ");
      generateBQTerm(deep,exp2,moduleName);
      output.write(")");
    }
  }

  protected void buildExpConditional(int deep, Expression cond,Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write("if(");
    generateExpression(deep,cond,moduleName);
    output.write(") then (");
    generateExpression(deep,exp1,moduleName);
    output.write(") else (");
    generateExpression(deep,exp2,moduleName);
    output.write(")");
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

  
/* ----- old ---- */
  
  protected void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException {
    Instruction head = instructionList.getHeadconcInstruction();
    if(!instructionList.isEmptyconcInstruction()) {
      generateInstruction(deep,head, moduleName);
      instructionList = instructionList.getTailconcInstruction();
    }

    while(!instructionList.isEmptyconcInstruction()) {
      if(!head.isCodeToInstruction()) {
        output.write("(* end InstructionSequence *) ");
        output.writeln(";");
      }
      generateInstruction(deep,instructionList.getHeadconcInstruction(), moduleName);
      /*
       * buildInstructionSequence is used for CompiledPattern.
       * Since a pattern should have type unit, we have to put a ";"
       */

      instructionList = instructionList.getTailconcInstruction();
    }
    return;
  }

  protected void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException {
    if(instList.length()==1) {
      output.writeln(deep,"( (* begin unamed block*)");
      generateInstruction(deep+1,instList.getHeadconcInstruction(), moduleName);
      output.writeln(deep,") (* end unamed block*)");
    } else {
      output.writeln(deep,"( (* begin unamed block*)");
      while(!instList.isEmptyconcInstruction()) {
        generateInstruction(deep+1,instList.getHeadconcInstruction(), moduleName);
        output.writeln("; (* from unamed block*)");
        instList = instList.getTailconcInstruction();
      }
      output.writeln(deep,") (* end unamed block*)");
    }
  }

  protected void buildComment(int deep, String text) throws IOException {
    output.writeln(deep,"(* " + text + " *)");
    return;
  }

  protected void buildExpNegation(int deep, Expression exp, String moduleName) throws IOException {
    output.write("not(");
    generateExpression(deep,exp,moduleName);
    output.write(")");
  }

  protected void buildExpCast(int deep, TargetLanguageType tlType, Expression exp, String moduleName) throws IOException {
    generateExpression(deep,exp,moduleName);
  }

  protected void buildLet(int deep, BQTerm var, OptionList optionList,
                          TargetLanguageType tlType, 
                          Expression exp, Instruction body, String moduleName) throws IOException {

    output.indent(deep);
    output.write("let ");
    generateBQTerm(deep,var,moduleName);
    output.write(" = ");
    generateExpression(deep,exp,moduleName);
    output.writeln(" in ");
    generateInstruction(deep,body,moduleName);
  }
 

  protected void buildLetRef(int deep, BQTerm var, OptionList optionList,
                             TargetLanguageType tlType, 
                             Expression exp, Instruction body, String moduleName) throws IOException {
    output.indent(deep);
    output.write("let ");
    generateBQTerm(deep,var,moduleName);
    output.write(" = ref (");
    generateExpression(deep,exp,moduleName);
    output.writeln(") in ");
    env.addFirst(var);
    generateInstruction(deep,body,moduleName);
    env.removeFirst();
  }

  /*
   * redefinition of AbstractGenerator.getVariableName
   * add a ! for variables under a LetRef
   */
  protected String getVariableName(BQTerm var) {
    String varname = super.getVariableName(var);
    if(env.contains(var)) {
      return "!" + varname;
    }
    return varname;
  }

  protected void buildAssign(int deep, BQTerm var, OptionList list, Expression exp, String moduleName) throws IOException {
    output.write(" ( ");
    output.write(deep+1,super.getVariableName(var));
    output.write(" := ");
    generateExpression(deep+1,exp,moduleName);
    output.writeln("; ");
    output.write(" ) ");
  }

  protected void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
		if(exp.isTrueTL()) {
			generateInstruction(deep,succes,moduleName);
		} else {
			output.write(deep,"(if "); 
			generateExpression(deep,exp,moduleName); 
			output.writeln(" then begin ");
			generateInstruction(deep+1,succes,moduleName);
			output.writeln(deep," end)");
		}
  }

  protected void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException {
    output.write(deep,"if "); 
    generateExpression(deep,exp,moduleName); 
    output.writeln(" then begin ");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln(deep," end else begin ");
    generateInstruction(deep+1,failure,moduleName);
    output.writeln(deep," end (* endif *)");
  }

  protected void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException {
    output.writeln(deep,"let tom_internal_cond = ref true in ");
    output.writeln(deep,"while !tom_internal_cond do ");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln(deep+1," ; tom_internal_cond := ");
    generateExpression(deep,exp,moduleName);
    output.writeln();
    output.writeln(deep," done");
  }

  protected void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write(deep,"while ");
    generateExpression(deep,exp,moduleName);
    output.writeln(" do ");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln();
    output.writeln(deep," done");
  }

  protected void genDecl(String returnType,
                         String declName,
                         String suffix,
                         String args[],
                         TargetLanguage tlCode,
                         String moduleName)  throws IOException {
    StringBuilder s = new StringBuilder();
    if(nodeclMode) { 
      return; 
    }
    s.append("let " + declName + "_" + suffix + "(");
    for(int i=0 ; i<args.length ; ) {
        // the first argument is the type, second the name 
      s.append(args[i+1]);
      i+=2;
      if(i<args.length) {
        s.append(", ");
      }
    } 
    s.append(") = " + tlCode.getCode() + " ");

    %match(tlCode) {
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

  protected void genDeclMake(String prefix,String funName, TomType returnType, 
                             BQTermList argList, Instruction instr, String moduleName)  throws IOException {
    StringBuilder s = new StringBuilder();
    if(nodeclMode) { 
      return;
    }
    s.append("let " + prefix+funName + "(");
    while(!argList.isEmptyconcBQTerm()) {
      BQTerm arg = argList.getHeadconcBQTerm();
      matchBlock: {
        %match(arg) {
            // in caml, we are not interested in the type of arguments
          BQVariable[AstName=Name(name)] -> {
            s.append(`name);
            break matchBlock;
          }
          
          _ -> {
            System.out.println("genDeclMake: strange term: " + arg);
            throw new TomRuntimeException("genDeclMake: strange term: " + arg);
          }
        }
      }
      argList = argList.getTailconcBQTerm();
      if(!argList.isEmptyconcBQTerm()) {
        s.append(", ");
      }
    }
    s.append(") = ");
    output.write(s);
    generateInstruction(0,instr,moduleName);
    output.write(" ");
  }

  protected void genDeclList(String name, String moduleName)  throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = TomBase.getSymbolCodomain(tomSymbol);
    //TomType eltType = getSymbolDomain(tomSymbol).getHeadconcTomType();

    String s = "";
    if(nodeclMode) {
      return;
    }

    String tomType = TomBase.getTomType(listType);
    String is_empty    = "tom_is_empty_" + name + "_" + tomType;
    String equal_term  = "tom_equal_term_" + tomType;
    String make_insert = "tom_cons_list_" + name;
    String make_empty  = "tom_empty_list_" + name;
    String get_head    = "tom_get_head_" + name + "_" + tomType;
    String get_tail    = "tom_get_tail_" + name + "_" + tomType;
    String get_slice   = "tom_get_slice_" + name;
    
    s+= "let rec tom_append_list_" + name +  "(l1,l2) =\n";
    s+= "   if " + is_empty + "(l1) then l2\n";
    s+= "   else if " + is_empty + "(l2) then l1\n";
    s+= "        else if " + is_empty + "(" + get_tail + "(l1)) then \n";  
    s+= "         " + make_insert + "(" + get_head + "(l1),l2)\n";
    s+= "             else \n";  
    s+= "              " + make_insert + "(" + get_head + "(l1),tom_append_list_" + name +  "(" + get_tail + "(l1),l2))\n";
    s+= "\n";
    
    s+=  "let rec tom_get_slice_" + name + "(beginning, ending,tail) =\n"; 
    s+= "   if " + equal_term + "(beginning,ending) then tail\n";
    s+= "   else " +  make_insert + "(" + get_head + "(beginning)," + 
      get_slice + "(" + get_tail + "(beginning),ending,tail))\n";
    s+= "\n";
    //If necessary we remove \n code depending on pretty option
    s = ASTFactory.makeSingleLineCode(s, prettyMode);
    output.write(s);
  }
  
  protected void buildDeclaration(int deep, BQTerm var, String type, TargetLanguageType tlType, String moduleName) throws IOException {
    output.write(deep,"let ");
    generateBQTerm(deep,var,moduleName);
    System.out.println("buildDeclaration : this is a deprecated code");
    output.writeln(" = ref None in");
  }

  protected void buildExpBottom(int deep, TomType type, String moduleName) throws IOException {
    output.write(" (Obj.magic \"Hopefully nobody will notice\") ");
  }

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" true ");
  }
  
  protected void buildExpFalse(int deep) throws IOException {
    output.write(" false ");
  }


  protected void buildNamedBlock(int deep, String blockName, InstructionList instList, String moduleName) throws IOException {
    System.out.println(" Named block not supported in Caml: ");
    buildUnamedBlock(deep,instList,moduleName);
  }

  protected void buildFunctionDef(int deep, String tomName, BQTermList varList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    System.out.println("Function not yet supported in Caml");
    throw new TomRuntimeException("Function not yet supported in Caml");
  }

  protected void buildExitAction(int deep, TomNumberList numberList) throws IOException {
    System.out.println(" Deprecated intermediate code : break is evil");
  }

  protected void genDeclInstr(String returnType,
                         String declName,
                         String suffix,
                         String args[],
                         Instruction instr,
                         int deep,
                         String moduleName) throws IOException {
    StringBuilder s = new StringBuilder();
    if(nodeclMode) {
      return;
    }
    s.append("let " + modifier + " " + declName + "_" + suffix + "(");
    for(int i=0 ; i<args.length ; ) {
      // forget the type, caml will infer it
      s.append(args[i+1]);
      i+=2;
      if(i<args.length) {
        s.append(", ");
      }
    } 
    s.append(") = ");
    output.write(s);
    generateInstruction(deep,instr,moduleName);
    output.write(";;");
  }

  protected void buildReturn(int deep, BQTerm exp, String moduleName) throws IOException {
    generateBQTerm(deep,exp,moduleName);
  }


  public void generateInstruction(int deep, Instruction subject, String moduleName) throws IOException {
    %match(subject) {

      Nop() -> {
        `buildNop();
        return;
      }

      _ -> {
        super.generateInstruction(deep, subject, moduleName);
      }
    }
  }

  protected void buildNop() throws IOException {
    output.write(" () ");
  }

  protected void buildAssignArray(int deep, BQTerm var, OptionList optionList,
      BQTerm index, 
      Expression exp, String moduleName) throws IOException {
    output.indent(deep);
    //  arrays are used in the AC algorithm;
    // and the AC maching is only supported for AC operators comming from GOM (which isn't available anyway for caml)   
    throw new RuntimeException("Arrays NOT SUPPORTED in Caml !"); 
    
  }

  //FIXME
  //TODO: to implement
  protected String genResolveIsSortCode(String varName, String resolveStringName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveIsSort) not yet supported in Caml");
    //return "";
  }

  protected String genResolveIsFsymCode(String tomName, String varname) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveIsFsym) not yet supported in Caml");
    //return "";
  }

  protected String genResolveGetSlotCode(String tomName, String varname, String slotName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveGetSlot) not yet supported in Caml");
    //return "";
  }
  
  protected void buildResolveClass(String wName, String tName, String extendsName, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveClass) not yet supported in Caml");
  }

  protected void buildResolveInverseLinks(int deep, String fileFrom, String fileTo, TomNameList resolveNameList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveInverseLinks) not yet supported in Caml");
  }

  protected void genResolveDeclMake(String prefix, String funName, TomType returnType, BQTermList argList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveMakeDecl) not yet supported in Caml");
  }

  protected String genResolveMakeCode(String funName, BQTermList argList) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveMakeCode) not yet supported in Caml");
  }

  protected void buildReferenceClass(int deep, String refname, RefClassTracelinkInstructionList refclassTInstructions, String  moduleName) {
    throw new TomRuntimeException("%transformation (ResolveReferenceClass) not yet supported in Caml");
  }

  protected void buildTracelink(int deep, String type, String name, Expression expr, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (Tracelink instruction) not yet supported in Caml");
  }

  protected void buildTracelinkPopulateResolve(int deep, String refClassName, TomNameList tracedLinks, BQTerm current, BQTerm link, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (TracelinkPopulateResolve instruction) not yet supported in Caml");
  }

  //tmp
  protected void buildResolve(int deep, BQTerm bqterm, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (Resolve2 instruction) not yet supported in Caml");
  }

///
}
