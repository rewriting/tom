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
 * Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package jtom.backend;

import java.io.IOException;

import jtom.adt.tomsignature.types.*;
import jtom.tools.TomTaskInput;
import jtom.tools.OutputCode;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public class TomCamlGenerator extends TomImperativeGenerator {

  protected String modifier = "";
  public TomCamlGenerator(OutputCode output) {
    super(output);
  }

// ------------------------------------------------------------
  %include { ../adt/TomSignature.tom }
// ------------------------------------------------------------

    /*
     * the implementation of methods are here for caml 
     */

  protected void buildInstructionSequence(int deep, Instruction instruction) throws IOException {
    generateInstruction(deep,instruction);
    output.writeln(";");
    return;
  }

  protected void buildUnamedBlock(int deep, InstructionList instList) throws IOException {
    if(instList.isSingle()) {
      output.writeln(deep,"( (* begin unamed block*)");
      generateInstruction(deep+1,instList.getHead());
      output.writeln(deep,") (* end unamed block*)");
    } else {
      output.writeln(deep,"( (* begin unamed block*)");
      while(!instList.isEmpty()) {
        generateInstruction(deep+1,instList.getHead());
        output.writeln("; (* from unamed block*)");
        instList = instList.getTail();
      }
      output.writeln(deep,") (* end unamed block*)");
    }
  }

  protected void buildComment(int deep, String text) throws IOException {
    output.writeln(deep,"(* " + text + " *)");
    return;
  }

	protected void buildExpNot(int deep, Expression exp) throws IOException {
		output.write("not(");
		generateExpression(deep,exp);
		output.write(")");
	}

  protected void buildRef(int deep, TomTerm term) throws IOException {
    output.write("!");
    generate(deep,term);
  }

  protected void buildExpCast(int deep, TomType tlType, Expression exp) throws IOException {
    generateExpression(deep,exp);
  }

  protected void buildLet(int deep, TomTerm var, OptionList optionList,
                          TomType tlType, 
                          Expression exp, Instruction body) throws IOException {

    output.indent(deep);
    output.write("let ");
    generate(deep,var);
    output.write(" = ");
    generateExpression(deep,exp);
    output.writeln(" in ");
    generateInstruction(deep,body);
  }

  protected void buildLetRef(int deep, TomTerm var, OptionList optionList,
                             TomType tlType, 
                             Expression exp, Instruction body) throws IOException {
    output.indent(deep);
    output.write("let ");
    generate(deep,var);
    output.write(" = ref ");
    generateExpression(deep,exp);
    output.writeln(" in ");
    generateInstruction(deep,body);
  }

	protected void buildAssignVar(int deep, TomTerm var, OptionList list, Expression exp) throws IOException {
		output.indent(deep);
		generate(deep,var);
		output.write(" := ");
		generateExpression(deep,exp);
	}

	protected void buildLetAssign(int deep, TomTerm var, OptionList list, Expression exp, Instruction body) throws IOException {
    output.writeln(deep,"( (* begin let assign*)");
		generate(deep+1,var);
		output.write(" := ");
		generateExpression(deep+1,exp);
    output.writeln("; (* from let assign *)");
    generateInstruction(deep+1,body);
    output.writeln(deep,") (* end let assign*)");

	}

  protected void buildIfThenElse(int deep, Expression exp, Instruction succes) throws IOException {
    output.write(deep,"(if "); 
    generateExpression(deep,exp); 
    output.writeln(" then ");
    generateInstruction(deep+1,succes);
    output.writeln(deep,")");
  }

  protected void buildIfThenElseWithFailure(int deep, Expression exp, Instruction succes, Instruction failure) throws IOException {
    output.write(deep,"if "); 
    generateExpression(deep,exp); 
    output.writeln(" then ");
    generateInstruction(deep+1,succes);
    output.writeln(deep," else ");
    generateInstruction(deep+1,failure);
    output.writeln(deep," (* endif *)");
  }

  protected void buildDoWhile(int deep, Instruction succes, Expression exp) throws IOException {
    output.writeln(deep,"let tom_internal_cond = ref true in");
    output.writeln(deep,"while !tom_internal_cond do");
    generateInstruction(deep+1,succes);
    output.writeln(deep+1,"; tom_internal_cond := ");
    generateExpression(deep,exp);
    output.writeln();
    output.writeln(deep,"done");
  }

  protected void buildWhileDo(int deep, Expression exp, Instruction succes) throws IOException {
    output.write(deep,"while ");
    generateExpression(deep,exp);
    output.writeln(" do");
    generateInstruction(deep+1,succes);
    output.writeln();
    output.writeln(deep,"done");
  }

  protected void buildExpGetHead(int deep, TomType domain, TomType codomain, TomTerm var) throws IOException {
    output.write("tom_get_head_" + getTomType(domain) + "(");
    generate(deep,var);
    output.write(")");
  }

  protected void buildExpGetElement(int deep, TomType domain, TomType codomain, TomTerm varName, TomTerm varIndex) throws IOException {
    output.write("tom_get_element_" + getTomType(domain) + "(");
    generate(deep,varName);
    output.write(",");
    generate(deep,varIndex);
    output.write(")");
  }


  protected void buildExpGetSubterm(int deep, TomType domain, TomType codomain, TomTerm exp, int number) throws IOException {
    String s = (String)getSubtermMap.get(domain);
    if(s == null) {
      s = "tom_get_subterm_" + getTomType(domain) + "(";
      getSubtermMap.put(domain,s);
    }
    output.write(s);
    generate(deep,exp);
    output.write(", " + number + ")");
  }

  protected TargetLanguage genDecl(String returnType,
                                   String declName,
                                   String suffix,
                                   String args[],
                                   TargetLanguage tlCode) {
    String s = "";
    if(!getInput().isGenDecl()) { return null; }
    s =  "let " + declName + "_" + suffix + "(";
    for(int i=0 ; i<args.length ; ) {
        // the first argument is the type, second the name 
      s+= args[i+1];
      i+=2;
      if(i<args.length) {
        s+= ", ";
      }
    } 
    s += ") = " + tlCode.getCode() + " ";
    if(tlCode.isTL()) {
      return `TL(s, tlCode.getStart(), tlCode.getEnd());
    } else {
      return `ITL(s);
    }
  }

  protected TargetLanguage genDeclMake(String opname, TomType returnType, 
                                       TomList argList, TargetLanguage tlCode) {
    String s = "";
    if(!getInput().isGenDecl()) { return null; }
    s = "let tom_make_" + opname + "(";
    while(!argList.isEmpty()) {
      TomTerm arg = argList.getHead();
      matchBlock: {
        %match(TomTerm arg) {
            // in caml, we are not interested in the type of arguments
          Variable[astName=Name(name)] -> {
            s += `name;
            break matchBlock;
          }
          
          _ -> {
            System.out.println("genDeclMake: strange term: " + arg);
            throw new TomRuntimeException(new Throwable("genDeclMake: strange term: " + arg));
          }
        }
      }
      argList = argList.getTail();
      if(!argList.isEmpty()) {
        s += ", ";
      }
    }
    s += ") = ";
      // the debug mode will not work as it for caml
    if(getInput().isDebugMode()) {
      s += "\n"+getTLType(returnType)+ " debugVar = " + tlCode.getCode() +";\n";
      s += "jtom.debug.TomDebugger.debugger.termCreation(debugVar);\n";
      s += "return  debugVar;\n}";
    } else {
      s += tlCode.getCode() + " ";
    }
    return `TL(s, tlCode.getStart(), tlCode.getEnd());
  }

  protected TargetLanguage genDeclList(String name, TomType listType, TomType eltType) {
    String s = "";
    if(!getInput().isGenDecl()) {
      return `ITL("");
    }

    String tomType = getTomType(listType);
    String is_empty    = "tom_is_empty_" + tomType;
    String term_equal  = "tom_terms_equal_" + tomType;
    String make_insert = "tom_cons_list_" + name;
    String make_empty  = "tom_empty_list_" + name;
    String get_head    = "tom_get_head_" + tomType;
    String get_tail    = "tom_get_tail_" + tomType;
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
		//If necessary we remove \n code depending on --pretty option
    return ast().reworkTLCode(`ITL(s), getInput().isPretty());
  }
  
  protected void buildDeclaration(int deep, TomTerm var, String type, TomType tlType) throws IOException {
    output.write(deep,"let ");
    generate(deep,var);
    System.out.println("buildDeclaration : this is a deprecated code");
    output.writeln(" = ref None in");
  }

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" true ");
  }
  
  protected void buildExpFalse(int deep) throws IOException {
    output.write(" false ");
  }


  protected void buildNamedBlock(int deep, String blockName, InstructionList instList) throws IOException {
    System.out.println(" Named block not supported in Caml: ");
    buildUnamedBlock(deep,instList);
  }

  protected void buildExitAction(int deep, TomNumberList numberList) throws IOException {
    System.out.println(" Deprecated intermediate code : break is evil");
  }

} // class TomCamlGenerator
