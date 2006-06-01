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

import tom.engine.adt.tomsignature.types.Expression;
import tom.engine.adt.tomsignature.types.Instruction;
import tom.engine.adt.tomsignature.types.InstructionList;
import tom.engine.adt.tomsignature.types.OptionList;
import tom.engine.adt.tomsignature.types.TargetLanguage;
import tom.engine.adt.tomsignature.types.TomList;
import tom.engine.adt.tomsignature.types.TomNumberList;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.tomsignature.types.TomTerm;
import tom.engine.adt.tomsignature.types.TomType;
import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.platform.OptionManager;

public class TomCamlGenerator extends TomImperativeGenerator {

  public TomCamlGenerator(OutputCode output, OptionManager optionManager, SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
  }
  
  // ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------
  
  /*
   * the implementation of methods are here for caml 
   */
  
  protected void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException {
    Instruction head = instructionList.getHeadconcInstruction();
    if(!instructionList.isEmptyconcInstruction()) {
      generateInstruction(deep,head, moduleName);
      instructionList = instructionList.getTailconcInstruction();
    }

    while(!instructionList.isEmptyconcInstruction()) {
      if(!head.isTargetLanguageToInstruction()) {
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

  protected void buildSemiColon() throws IOException {
    // do nothing
  }

  protected void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException {
    if(instList.isSingle()) {
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

  protected void buildRef(int deep, TomTerm term, String moduleName) throws IOException {
    output.write("!");
    generate(deep,term,moduleName);
  }

  protected void buildExpCast(int deep, TomType tlType, Expression exp, String moduleName) throws IOException {
    generateExpression(deep,exp,moduleName);
  }

  protected void buildLet(int deep, TomTerm var, OptionList optionList,
                          TomType tlType, 
                          Expression exp, Instruction body, String moduleName) throws IOException {

    output.indent(deep);
    output.write("let ");
    generate(deep,var,moduleName);
    output.write(" = ");
    generateExpression(deep,exp,moduleName);
    output.writeln(" in ");
    generateInstruction(deep,body,moduleName);
  }

  protected void buildLetRef(int deep, TomTerm var, OptionList optionList,
                             TomType tlType, 
                             Expression exp, Instruction body, String moduleName) throws IOException {
    output.indent(deep);
    output.write("let ");
    generate(deep,var,moduleName);
    output.write(" = ref ");
    generateExpression(deep,exp,moduleName);
    output.writeln(" in ");
    generateInstruction(deep,body,moduleName);
  }

  protected void buildAssignVar(int deep, TomTerm var, OptionList list, Expression exp, String moduleName) throws IOException {
    output.indent(deep);
    generate(deep,var,moduleName);
    output.write(" := ");
    generateExpression(deep,exp,moduleName);
  }

  protected void buildLetAssign(int deep, TomTerm var, OptionList list, Expression exp, Instruction body, String moduleName) throws IOException {
    output.writeln(deep,"( (* begin let assign*)");
    generate(deep+1,var,moduleName);
    output.write(" := ");
    generateExpression(deep+1,exp,moduleName);
    output.writeln("; (* from let assign *)");
    generateInstruction(deep+1,body,moduleName);
    output.writeln(deep,") (* end let assign*)");

  }

  protected void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
		if(exp.isTrueTL()) {
			generateInstruction(deep,succes,moduleName);
		} else {
			output.write(deep,"(if "); 
			generateExpression(deep,exp,moduleName); 
			output.writeln(" then ");
			generateInstruction(deep+1,succes,moduleName);
			output.writeln(deep,")");
		}
  }

  protected void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException {
    output.write(deep,"if "); 
    generateExpression(deep,exp,moduleName); 
    output.writeln(" then ");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln(deep," else ");
    generateInstruction(deep+1,failure,moduleName);
    output.writeln(deep," (* endif *)");
  }

  protected void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException {
    output.writeln(deep,"let tom_internal_cond = ref true in");
    output.writeln(deep,"while !tom_internal_cond do");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln(deep+1,"; tom_internal_cond := ");
    generateExpression(deep,exp,moduleName);
    output.writeln();
    output.writeln(deep,"done");
  }

  protected void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write(deep,"while ");
    generateExpression(deep,exp,moduleName);
    output.writeln(" do");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln();
    output.writeln(deep,"done");
  }

  protected void buildCheckStamp(int deep, TomType type, TomTerm variable, String moduleName) throws IOException {
    if(((Boolean)optionManager.getOptionValue("stamp")).booleanValue()) {
      output.write("tom_check_stamp_" + getTomType(type) + "(");
      generate(deep,variable,moduleName);
      output.write(")");
    } else {
      output.write("(* checkstamp *) ()");
    }
  }

  protected void buildExpGetHead(int deep, TomType domain, TomType codomain, TomTerm var, String moduleName) throws IOException {
    output.write("tom_get_head_" + getTomType(domain) + "(");
    generate(deep,var,moduleName);
    output.write(")");
  }

  protected void buildExpGetElement(int deep, TomType domain, TomType codomain, TomTerm varName, TomTerm varIndex, String moduleName) throws IOException {
    output.write("tom_get_element_" + getTomType(domain) + "(");
    generate(deep,varName,moduleName);
    output.write(",");
    generate(deep,varIndex,moduleName);
    output.write(")");
  }


  protected void genDecl(String returnType,
                         String declName,
                         String suffix,
                         String args[],
                         TargetLanguage tlCode,
                         String moduleName)  throws IOException {
    StringBuffer s = new StringBuffer();
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

    %match(TargetLanguage tlCode) {
      TL(_,TextPosition[line=startLine], TextPosition[line=endLine]) -> {
        output.write(0,s, `startLine, `endLine - `startLine);
        return;
      }

      ITL(_) -> {  // pas de \n donc pas besoin de reworkTL
        output.write(s);
        return;
      }
    }
  }

  protected void genDeclMake(String funName, TomType returnType, 
                             TomList argList, Instruction instr, String moduleName)  throws IOException {
    StringBuffer s = new StringBuffer();
    if(nodeclMode) { 
      return;
    }
    s.append("let " + funName + "(");
    while(!argList.isEmptyconcTomTerm()) {
      TomTerm arg = argList.getHeadconcTomTerm();
      matchBlock: {
        %match(TomTerm arg) {
            // in caml, we are not interested in the type of arguments
          Variable[astName=Name(name)] -> {
            s.append(`name);
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
    s.append(") = ");
    output.write(s);
    generateInstruction(0,instr,moduleName);
    output.write(" ");
  }

  protected void genDeclList(String name, String moduleName)  throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = getSymbolCodomain(tomSymbol);
    TomType eltType = getSymbolDomain(tomSymbol).getHeadconcTomType();

    String s = "";
    if(nodeclMode) {
      return;
    }

    String tomType = getTomType(listType);
    String is_empty    = "tom_is_empty_" + name + "_" + tomType;
    String term_equal  = "tom_terms_equal_" + tomType;
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
    
    s+=  "let rec tom_get_slice_" + name + "(beginning, ending) =\n"; 
    s+= "   if " + term_equal + "(beginning,ending) then " + make_empty + "()\n";
    s+= "   else " +  make_insert + "(" + get_head + "(beginning)," + 
      get_slice + "(" + get_tail + "(beginning),ending))\n";
    s+= "\n";
    //If necessary we remove \n code depending on pretty option
    TargetLanguage itl = ASTFactory.reworkTLCode(`ITL(s), prettyMode);
    output.write(itl.getCode()); 
  }
  
  protected void buildDeclaration(int deep, TomTerm var, String type, TomType tlType, String moduleName) throws IOException {
    output.write(deep,"let ");
    generate(deep,var,moduleName);
    System.out.println("buildDeclaration : this is a deprecated code");
    output.writeln(" = ref None in");
  }

  protected void buildExpBottom(int deep) throws IOException {
    output.write(" None ");
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

  protected void buildFunctionDef(int deep, String tomName, TomList varList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
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
    StringBuffer s = new StringBuffer();
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
    /*
     * path to add a semi-colon for 'void instruction'
     * This is the case of CheckStampDecl
     */
    if(instr.isTargetLanguageToInstruction()) {
      buildSemiColon();
    }
    output.write(";;");
  }

  protected void buildReturn(int deep, TomTerm exp, String moduleName) throws IOException {
    generate(deep,exp,moduleName);
  }

} // class TomCamlGenerator
