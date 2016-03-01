/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 
 **/

package tom.engine.backend;

import java.io.IOException;

import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
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

public class PythonGenerator extends GenericGenerator {

  // ------------------------------------------------------------
      
  // ------------------------------------------------------------


  public PythonGenerator(OutputCode output, OptionManager optionManager,
                                SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
  }

  /*
  public void generateInstructionList(int deep, InstructionList subject, String moduleName)
    throws IOException {
    while(!subject.isEmptyconcInstruction()) {
      generateInstruction(deep,subject.getHeadconcInstruction(), moduleName);
      subject = subject.getTailconcInstruction();
    }
    output.writeln();
  }
  */

  protected void buildAssign(int deep, BQTerm var, OptionList optionList, Expression exp, String moduleName) throws IOException {
    //output.indent(deep);
    generateBQTerm(deep,var,moduleName);
    output.write("=");
    generateExpression(deep,exp,moduleName);
    output.write(";\n");
  } 

  protected void buildComment(int deep, String text) throws IOException {
    //output.writeln("#" + text.replace("\n","\n#"));
  }
 
  protected void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException {
    generateInstruction(deep,succes,moduleName);
    buildWhileDo(deep,exp,succes,moduleName);
  }

  protected void buildExpEqualTerm(int deep, TomType type, BQTerm exp1, TomTerm exp2, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBooleanType(TomBase.getTomType(type))) {
      output.write("(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(" == ");
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
    if(getSymbolTable(moduleName).isBooleanType(TomBase.getTomType(type))) {
      output.write("(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(" == ");
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
    output.write("((");
    generateExpression(deep,exp1,moduleName);
    output.write(") if (");
    generateExpression(deep,cond,moduleName);
    output.write(") else (");
    generateExpression(deep,exp2,moduleName);
    output.write("))");
  }

  protected void buildExpAnd(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write(" ( ");
    generateExpression(deep,exp1,moduleName);
    output.write(" and ");
    generateExpression(deep,exp2,moduleName);
    output.write(" ) ");
  }

  protected void buildExpOr(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write(" ( ");
    generateExpression(deep,exp1,moduleName);
    output.write(" or ");
    generateExpression(deep,exp2,moduleName);
    output.write(" ) ");
  }

  protected void buildExpCast(int deep, TargetLanguageType tlType, Expression exp, String moduleName) throws IOException {
    generateExpression(deep,exp,moduleName);
  }

  protected void buildExpNegation(int deep, Expression exp, String moduleName) throws IOException {
    output.write("not (");
    generateExpression(deep,exp,moduleName);
    output.write(")");
  }

  protected void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write(deep,"if "); 
    generateExpression(deep,exp, moduleName); 
    output.writeln(":\n");
    generateInstruction(deep+1,succes, moduleName);
    output.write(deep,"\n# end if\n"); 
  }

  protected void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException {
    output.write(deep,"if "); 
    generateExpression(deep,exp,moduleName); 
    output.writeln(":\n");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln(deep,"else:\n");
    generateInstruction(deep+1,failure,moduleName);
    output.write(deep,"\n# end if\n"); 
  }

  protected void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException {
    generateInstructionList(deep, instructionList, moduleName);
  }

  protected void buildLet(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType, 
      Expression exp, Instruction body, String moduleName) throws IOException {
    buildAssign(deep,var,optionList,exp,moduleName);
    generateInstruction(deep,body,moduleName);
  }
  
  protected void buildLetRef(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType, 
      Expression exp, Instruction body, String moduleName) throws IOException {
    buildLet(deep,var,optionList,tlType,exp,body, moduleName);
  }

  protected void buildReturn(int deep, BQTerm exp, String moduleName) throws IOException {
    output.write(deep,"return ");
    generateBQTerm(deep,exp,moduleName);
  }

  /* FIXME */
  protected void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException {
    generateInstructionList(deep+1,instList, moduleName);
  }

  protected void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write(deep,"while ");
    generateExpression(deep,exp,moduleName);
    output.writeln(":\n");
    generateInstruction(deep+1,succes,moduleName);
    output.write(deep,"\n# end while\n"); 
  }

  protected void genDeclInstr(String returnType,
                         String declName,
                         String suffix,
                         String args[],
                         Instruction instr,
                         int deep, String moduleName) throws IOException {
    StringBuilder s = new StringBuilder();
    if(nodeclMode) {
      return;
    }
    s.append("def " + declName + "_" + suffix + "(");
    for(int i=0 ; i<args.length ; ) {
      s.append(args[i+1]);
      i+=2;
      if(i<args.length) {
        s.append(", ");
      }
    } 

    s.append("): ");
    output.write(deep,s);
    generateInstruction(deep+1,instr,moduleName);
    output.writeln("\n# end def " + declName + "_" + suffix + "\n");
  }


  protected void genDeclList(String name, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = TomBase.getSymbolCodomain(tomSymbol);
    TomType eltType = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();

    String s = "";
    if(nodeclMode) {
      return;
    }

    String tomType = TomBase.getTomType(listType);
    String glType = TomBase.getTLType(listType);
    //String tlEltType = getTLType(eltType);

    //String utype = glType;
    
    String is_empty = "tom_is_empty_" + name + "_" + tomType;
    String equal_term = "tom_equal_term_" + tomType;
    String make_insert = "tom_cons_list_" + name;
    String make_empty = "tom_empty_list_" + name;
    String get_head = "tom_get_head_" + name + "_" + tomType;
    String get_tail = "tom_get_tail_" + name + "_" + tomType;
    String get_slice = "tom_get_slice_" + name;

    s+= "def tom_append_list_" + name +  "(l1, l2):\n";
    s+= "   if " + is_empty + "(l1):\n";
    s+= "     return l2\n";  
    s+= "   elif " + is_empty + "(l2):\n";
    s+= "     return l1\n";  
    s+= "   elif " + is_empty + "(" + get_tail + "(l1)): \n";  
    s+= "     return " + make_insert + "(" + get_head + "(l1),l2)\n";
    s+= "   else:\n";  
    s+= "     return " + make_insert + "(" + get_head + "(l1),tom_append_list_" + name +  "(" + get_tail + "(l1),l2));\n";
    s+= "# end if\n";
    s+= "# end def tom_append_list_" + name;
    s+= "\n";
    
    s+= "def tom_get_slice_" + name + "(begin, end,tail):\n"; 
    s+= "   if " + equal_term + "(begin,end):\n";
    s+= "      return tail\n";
    s+= "   else:\n";
    s+= "      return " +  make_insert + "(" + get_head + "(begin)," + 
      get_slice + "(" + get_tail + "(begin),end,tail));\n";
    s+= "# end if\n";
    s+= "# end def tom_get_slice_" + name;
    s+= "\n";
    //If necessary we remove \n code depending on pretty option
    //String res  = ASTFactory.makeSingleLineCode(s, prettyMode);
    output.write(s);
  }

  protected void genDeclMake(String prefix,String funName, TomType returnType, 
      BQTermList argList, Instruction instr, String moduleName) throws IOException {
    StringBuilder s = new StringBuilder();
    StringBuilder check = new StringBuilder();
    if( nodeclMode) {
      return;
    }

    s.append("def " + prefix+funName + "(");
    while(!argList.isEmptyconcBQTerm()) {
      BQTerm arg = argList.getHeadconcBQTerm();
matchBlock: {
              {{if ( (arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )arg)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch117_1= (( tom.engine.adt.code.types.BQTerm )arg).getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch117_2= (( tom.engine.adt.code.types.BQTerm )arg).getAstType() ;if ( (tomMatch117_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch117_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch117_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch117_2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch117_8= tomMatch117_2.getTlType() ;if ( (tomMatch117_8 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch117_8) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

                  s.append( tomMatch117_1.getString() );
                  break matchBlock;
                }}}}}}}}}}{if ( (arg instanceof tom.engine.adt.code.types.BQTerm) ) {


                  System.out.println("genDeclMake: strange term: " + arg);
                  throw new TomRuntimeException("genDeclMake: strange term: " + arg);
                }}}

            }
            argList = argList.getTailconcBQTerm();
            if(!argList.isEmptyconcBQTerm()) {
              s.append(", ");
            }
    }
    s.append("): ");
    s.append(check);

    output.write(s);
    output.write("return ");
    generateInstruction(0,instr,moduleName);
    output.write("\n # end def " + prefix+funName + "\n");
  }

  // FIXME
  protected void buildNamedBlock(int deep, String blockName, InstructionList instList, String moduleName) throws IOException {
    //output.writeln(blockName + ": {");
    generateInstructionList(deep+1,instList,moduleName);
    //output.writeln("}");
  }

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" True ");
  }

  protected void buildExpFalse(int deep) throws IOException {
    output.write(" False ");
  }

  protected void buildExpBottom(int deep, TomType type, String moduleName) throws IOException {
    if ((getSymbolTable(moduleName).getIntType() == type)
        || (getSymbolTable(moduleName).getCharType() == type)
        || (getSymbolTable(moduleName).getLongType() == type)
        || (getSymbolTable(moduleName).getFloatType() == type)
        || (getSymbolTable(moduleName).getDoubleType() == type)) {
      output.write(" 0 ");
    } else if (getSymbolTable(moduleName).getBooleanType() == type) {
      output.write(" False ");
    } else if (getSymbolTable(moduleName).getStringType() == type) {
      output.write(" \"\" ");
    } else {
      output.write(" None ");
    }
  }

  protected void buildFunctionDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,this.modifier);
  }

  protected void buildMethodDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,"public ");
  }

  private void buildMethod(int deep, String tomName, BQTermList varList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName, String methodModifier) throws IOException {
    output.write(deep, "def " + tomName + "(");
    while(!varList.isEmptyconcBQTerm()) {
      BQTerm localVar = varList.getHeadconcBQTerm();
matchBlock: {
              {{if ( (localVar instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )localVar) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )localVar)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

                  //output.write(deep,getTLType(`type2) + " ");
                  generateBQTerm(deep,(( tom.engine.adt.code.types.BQTerm )localVar),moduleName);
                  break matchBlock;
                }}}}{if ( (localVar instanceof tom.engine.adt.code.types.BQTerm) ) {

                  System.out.println("MakeFunction: strange term: " + localVar);
                  throw new TomRuntimeException("MakeFunction: strange term: " + localVar);
                }}}

            }
            varList = varList.getTailconcBQTerm();
            if(!varList.isEmptyconcBQTerm()) {
              output.write(deep,", ");

            }
    }
    output.writeln(deep,")");

    output.writeln(": ");
    generateInstruction(deep+1,instruction,moduleName);
    output.write(deep, "\n# end def " + tomName + "\n");
  }

  protected void genDecl(String returnType,
      String declName,
      String suffix,
      String args[],
      TargetLanguage tlCode,
      String moduleName) throws IOException {
    StringBuilder s = new StringBuilder();
    if(nodeclMode) {
      return;
    }
    s.append("def " + declName + "_" + suffix + "(");
    for(int i=0 ; i<args.length ; ) {
      s.append(args[i+1]);
      i+=2;
      if(i<args.length) {
        s.append(", ");
      }
    } 
    String returnValue = getSymbolTable(moduleName).isVoidType(returnType)?tlCode.getCode():"return " + tlCode.getCode();
    s.append("):\n " + returnValue + "\n");
    s.append("\n # end def " + declName + "_" + suffix + "\n");
    output.write(s);
  }

  protected void buildAssignArray(int deep, BQTerm var, OptionList optionList, BQTerm index, 
      Expression exp, String moduleName) throws IOException {
    buildAssignArrayVar(deep,var,optionList, index, exp, moduleName);
  }

  protected void buildAssignArrayVar(int deep, BQTerm var, OptionList optionList, BQTerm index, 
      Expression exp, String moduleName) throws IOException {    
    //output.indent(deep);
    generateArray(deep,var,index,moduleName);
    output.write("=");
    generateExpression(deep,exp,moduleName);
    output.write(";\n");
  } 

  //FIXME
  //TODO: to implement
  protected String genResolveIsSortCode(String varName, String
      resolveStringName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveIsSort) not yet supported in Python");
    //return "";
  }

  protected String genResolveIsFsymCode(String tomName, String varname) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveIsFsym) not yet supported in Python");
    //return "";
  }

  protected String genResolveGetSlotCode(String tomName, String varname, String slotName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveGetSlot) not yet supported in Python");
    //return "";
  }
  
  protected void buildResolveClass(String wName, String tName, String extendsName, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveClass) not yet supported in Python");
  }

  protected void buildResolveInverseLinks(int deep, String fileFrom, String fileTo, TomNameList resolveNameList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveInverseLinks) not yet supported in Python");
  }

  protected void genResolveDeclMake(String prefix, String funName, TomType returnType, BQTermList argList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveDeclMake) not yet supported in Python");
  }

  protected String genResolveMakeCode(String funName, BQTermList argList) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveMakeCode) not yet supported in Python");
  }

  protected void buildReferenceClass(int deep, String refname, RefClassTracelinkInstructionList refclassTInstructions, String  moduleName) {
    throw new TomRuntimeException("%transformation (ResolveReferenceClass) not yet supported in Python");
  }

  protected void buildTracelink(int deep, String type, String name, Expression expr, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (Tracelink instruction) not  yet supported in Python");
  }

  protected void buildTracelinkPopulateResolve(int deep, String refClassName, TomNameList tracedLinks, BQTerm current, BQTerm link, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (TracelinkPopulateResolve instruction) not yet supported in Python");
  }

  //tmp
  protected void buildResolve(int deep, BQTerm bqterm, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (Resolve2 instruction) not yet supported in Python");
  }

}
