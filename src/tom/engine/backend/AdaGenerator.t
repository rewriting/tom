/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2011, INPL, INRIA
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
import java.util.ArrayList;

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

public class AdaGenerator extends GenericGenerator {

  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  /* modifier associated to classes generated by %strategy */
  protected String stratmodifier = "";

  public AdaGenerator(OutputCode output, OptionManager optionManager,
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

  protected void buildExpIsSort(int deep, String type, BQTerm exp, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBuiltinType(type)) {
      generateExpression(deep,`TrueTL(),moduleName);
      return;
    }

    String template = getSymbolTable(moduleName).getIsSort(type);
    if(instantiateTemplate(deep,template,`concBQTerm(exp),moduleName) == false) {
      output.write("tom_is_sort_" + type + "(");
      generateBQTerm(deep,exp,moduleName);
      output.write("\'Address)");
    }
  }
  
  protected void buildIsSortDecl(int deep, String varName, String type, Expression code, String moduleName) throws IOException {
    boolean inlined = inlineplus;
    if(code.isCode()) {
      // perform the instantiation
      String ocode = code.getCode();
      String ncode = ocode.replace("{0}",varName);
      if(!ncode.equals(ocode)) {
        inlined = true;
        code = code.setCode(ncode);
      }
    }
    if(!inline || !code.isCode() || !inlined) {
      TomType argType = getUniversalType();
      genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_is_sort", type,
          new String[] { TomBase.getTLType(argType), varName },
          `Return(ExpressionToBQTerm(code)),deep,moduleName);
    }
  }

  protected void buildExpIsFsym(int deep, String opname, BQTerm exp, String moduleName) throws IOException {
    String template = getSymbolTable(moduleName).getIsFsym(opname);
    if(instantiateTemplate(deep,template,`concBQTerm(exp),moduleName) == false) {
      String s = isFsymMap.get(opname);
      if(s == null) {
        s = "tom_is_fun_sym_" + opname + "(";
        isFsymMap.put(opname,s);
      }
      output.write(s);
      generateBQTerm(deep,exp,moduleName);
      output.write(")");
    }
  }

  //OK
  protected void buildAssign(int deep, BQTerm var, OptionList optionList, Expression exp, String moduleName) throws IOException {
    //output.indent(deep);
    generateBQTerm(deep,var,moduleName);
    output.write(":=");
    generateExpression(deep,exp,moduleName);
    output.writeln(";");
  } 

  //OK
  protected void buildComment(int deep, String text) throws IOException {
    output.writeln("--" + text.replace("\n","\n--"));
  }
 
  //OK
  protected void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException {
    output.writeln(deep,"loop");
    generateInstruction(deep+1,succes,moduleName);
    output.write(deep+1,"exit when not(");
    generateExpression(deep,exp,moduleName);
    output.writeln(");");
    output.writeln(deep, "end loop;");
  }

  //OK
  protected void buildExpEqualTerm(int deep, TomType type, BQTerm exp1, TomTerm exp2, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBooleanType(TomBase.getTomType(
    type))) {
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

  //OK
  protected void buildExpEqualBQTerm(int deep, TomType type, BQTerm exp1, BQTerm exp2, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBooleanType(TomBase.getTomType(
    type))) {
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
  
  protected void buildEqualTermDecl(int deep, String varname1, String varname2,
                                     String type1, String type2, Expression code, String moduleName) throws IOException {
	TomType argType1 = null;
	if(getSymbolTable(moduleName).isBuiltinType(type1)) {
	  argType1 = getSymbolTable(moduleName).getBuiltinType(type1);
	}
	TomType argType2 = null;
	if(getSymbolTable(moduleName).isBuiltinType(type2)) {
	  argType2 = getSymbolTable(moduleName).getBuiltinType(type2);
	}
	boolean inlined = inlineplus;
	if(code.isCode()) {
	  // perform the instantiation
	  String ocode = code.getCode();
	  String ncode = ocode.replace("{0}",varname1).replace("{1}",varname2);
	  if(!ncode.equals(ocode)) {
		inlined = true;
		code = code.setCode(ncode);
	  }
	}
	if(!inline || !code.isCode() || !inlined) {
	  genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_equal_term", type1,
		  new String[] {
		  (argType1 != null) ? TomBase.getTLType(argType1) : type1, varname1,
		  (argType2 != null) ? TomBase.getTLType(argType2) : type2, varname2
		  },
		  `Return(ExpressionToBQTerm(code)),deep,moduleName);
	}
  }

  //OK
  protected void buildExpConditional(int deep, Expression cond,Expression exp1, Expression exp2, String moduleName) throws IOException {
	System.out.println("WARNING: The use of conditional expressions may be unsupported");
    output.write("if ");
    generateExpression(deep,cond,moduleName);
    output.write(" then ");
    generateExpression(deep,exp1,moduleName);
    output.write(" else ");
    generateExpression(deep,exp2,moduleName);
  }

  //OK
  protected void buildExpAnd(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write(" ( ");
    generateExpression(deep,exp1,moduleName);
    output.write(" and ");
    generateExpression(deep,exp2,moduleName);
    output.write(" ) ");
  }

  //OK
  protected void buildExpOr(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write(" ( ");
    generateExpression(deep,exp1,moduleName);
    output.write(" or ");
    generateExpression(deep,exp2,moduleName);
    output.write(" ) ");
  }

  //OK
  protected void buildExpCast(int deep, TargetLanguageType tlType, Expression exp, String moduleName) throws IOException {
    /* If cast are necessary: 
    output.write(TomBase.getTLCode(tlType) + "(");
    generateExpression(deep,exp,moduleName);
    output.write(")");*/

    generateExpression(deep,exp,moduleName);
  }

  //OK
  protected void buildExpNegation(int deep, Expression exp, String moduleName) throws IOException {
    output.write("not (");
    generateExpression(deep,exp,moduleName);
    output.write(")");
  }

  //OK
  protected void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write(deep,"if "); 
    generateExpression(deep,exp, moduleName); 
    output.write(" then\n");
    generateInstruction(deep+1,succes, moduleName);
    output.write(deep,"\nend if;\n"); 
  }

  //OK
  protected void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException {
    output.write(deep,"if "); 
    generateExpression(deep,exp,moduleName); 
    output.write(" then\n");
    generateInstruction(deep+1,succes,moduleName);
    output.write(deep,"\nelse\n");
    generateInstruction(deep+1,failure,moduleName);
    output.write(deep,"\nend if;\n"); 
  }

  //OK
  protected void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException {
    generateInstructionList(deep, instructionList, moduleName);
  }

  //OK
  protected void buildLet(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType, 
  Expression exp, Instruction body, String moduleName) throws IOException {
    output.write("declare\n");
    generateBQTerm(deep+1,var,moduleName);
    output.write(": ");
    output.write(deep+1,TomBase.getTLCode(tlType) + ":=");

    generateExpression(deep+1,exp,moduleName);
    output.write(";\n");
    output.write("begin\n");
    generateInstruction(deep+1,body,moduleName);
    output.write("end;\n");
  }

  //OK  
  protected void buildLetRef(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType, 
      Expression exp, Instruction body, String moduleName) throws IOException {
    buildLet(deep,var,optionList,tlType,exp,body, moduleName);
  }

  //OK  
  protected void buildReturn(int deep, BQTerm exp, String moduleName) throws IOException {
  output.write(deep,"return ");
  generateBQTerm(deep,exp,moduleName);
  output.write(deep,";");
  }

  /* FIXME */
  protected void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException {
    generateInstructionList(deep+1,instList, moduleName);
  }

  //OK  
  protected void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.writeln(deep,"while ");
    generateExpression(deep,exp,moduleName);
    output.write(" loop\n");
    generateInstruction(deep+1,succes,moduleName);
    output.writeln(deep, "end loop;\n");
  }
  
	private boolean isBuiltinType(String type) {
		return type.equals("Integer") || type.equals("Long") || type.equals("Character") ||
		type.equals("String") || type.equals("Boolean") || type.equals("Long_Float") ||
		type.equals("Float)");
	}
	
	private boolean isAccessType(String type) {
		return type.toLowerCase().startsWith("access");
	}
	
	private String getRawType(String type) {
		if (isAccessType(type))
			return type.substring(7);
		else
			return type;
	}
	
	private String getClassWideAccess(String type) {
		String rtype = getRawType(type.trim());
		if (isBuiltinType(rtype)) {
			return type;
		} else {
			return "access " + rtype + "\'Class";
		}
	}
	
  //OK
  protected void genDeclEqualTermString(String returnType,
                         String declName,
                         String suffix,
                         String args[],
                         Instruction instr,
                         int deep, String moduleName) throws IOException {
    StringBuilder s = new StringBuilder();
    s.append("function ");
    s.append(declName);
    s.append("_");
    s.append(suffix);

    s.append("(");
    for(int i=0 ; i<args.length ; ) {
		s.append(args[i+1]); // parameter name
		s.append(": ");

		s.append("access String");
		//s.append(args[i]); // parameter type
		i+=2;
		if(i<args.length) {
		s.append("; ");
		}
    }
    s.append(")");
    s.append(" return ");
    s.append(returnType);
    s.append(" is \n");

    output.write(s);
    output.write("begin\n ");
    generateInstruction(deep,instr,moduleName);
    output.write(" \nend " + declName + "_" + suffix + ";\n");	
    
	output.write("function tom_equal_term_String(t1: String; t2: access String)");
	output.write("return Boolean is\n"); 
	output.write("begin\n");
	output.write(" return t1 = t2.all; \n");
	output.write("end tom_equal_term_String;\n"); 
  }

  //OK
  protected void genDeclInstr(String returnType,
                         String declName,
                         String suffix,
                         String args[],
                         Instruction instr,
                         int deep, String moduleName) throws IOException {
	
	if ("tom_equal_term_String".equals(declName + "_" + suffix)) {
		genDeclEqualTermString(returnType, declName, suffix, args, instr, deep, moduleName);
		return;
	}
	
    StringBuilder s = new StringBuilder();
	boolean useClassWideAccess = "tom_get_slot".equals(declName) || "tom_is_fun_sym".equals(declName) || "tom_equal_term".equals(declName);
    s.append("function ");
    s.append(declName);
    s.append("_");
    s.append(suffix);
	if (args.length > 0) { s.append("("); }


    for(int i=0 ; i<args.length ; ) {
		s.append(args[i+1]); // parameter name
		s.append(": ");
		if (useClassWideAccess) {
			s.append(getClassWideAccess(args[i]));
		} else {
			s.append(args[i]); // parameter type
		}
		i+=2;
		if(i<args.length) {
		s.append("; ");
		}
    }
    if (args.length > 0) { s.append(")"); }
    s.append(" return ");
    if (useClassWideAccess) { s.append(getClassWideAccess(returnType)); } else { s.append(returnType); }
    s.append(" is \n");

    output.write(s);
    output.write("begin\n ");
    generateInstruction(deep,instr,moduleName);
    output.write(" \nend " + declName + "_" + suffix + ";\n");
  }

  //OK
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
    //if(lazyType) {
    //  utype = getTLType(getUniversalType());
    //}

    String is_empty = "tom_is_empty_" + name + "_" + tomType;
    String equal_term = "tom_equal_term_" + tomType;
    String make_insert = "tom_cons_list_" + name;
    String make_empty = "tom_empty_list_" + name;
    String get_head = "tom_get_head_" + name + "_" + tomType;
    String get_tail = "tom_get_tail_" + name + "_" + tomType;
    String get_slice = "tom_get_slice_" + name;

    s+= "function tom_append_list_" + name +  "(l1: "+ glType+"; l2: "+glType+") return "+ glType + " is\n ";
    s+= "begin\n";
    s+= " if " + is_empty + "(l1) then\n ";
    s+= "  return l2;\n ";  
    s+= " elsif " + is_empty + "(l2) then\n ";
    s+= "  return l1;\n ";  
    s+= " elsif " + is_empty + "(" + get_tail + "(l1)) then\n ";  
    s+= "  return " + make_insert + "(" + get_head + "(l1),l2);\n ";
    s+= " else\n ";  
    s+= "  return " + make_insert + "(" + get_head + "(l1),tom_append_list_" + name +  "(" + get_tail + "(l1),l2));\n ";
    s+= " end if;\n ";
    s+= "end tom_append_list_" + name + ";\n ";

	s+= "function tom_get_slice_" + name + "(begining: "+ glType+"; ending: "+ glType+"; tail: "+glType+") return "+ glType + " is\n ";
	s+= "begin\n";
	s+= " if " + equal_term + "(begining,ending) then\n";
	s+= "  return tail;\n ";
	s+= " else\n ";
	s+= "  return " +  make_insert + "(" + get_head + "(begining)," +  get_slice + "(" + get_tail + "(begining),ending,tail));\n ";
	s+= " end if;\n ";
	s+= "end tom_get_slice_" + name + ";\n ";
    output.write(s);
  }

  //OK
  protected void genDeclMake(String prefix,String funName, TomType returnType, 
      BQTermList argList, Instruction instr, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }
	boolean parenthesis = !argList.isEmptyconcBQTerm();
    boolean inlined = inlineplus;
    boolean isCode = false;
    %match(instr) {
      ExpressionToInstruction(Code(code)) -> {
        isCode = true;
        // perform the instantiation
        String ncode = `code;
        int index = 0;
        %match(argList) {
          concBQTerm(_*,BQVariable[AstName=Name(varname)],_*) -> {
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
      s.append(modifier + "function " + prefix + funName);
      if (parenthesis) { s.append("("); }
      while(!argList.isEmptyconcBQTerm()) {
        BQTerm arg = argList.getHeadconcBQTerm();
matchBlock: {
              %match(arg) {
                BQVariable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]] -> {
                  s.append(`name + ": " + getClassWideAccess( TomBase.getTLCode(`tlType) ));
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
              s.append("; ");
            }
      }
    if (parenthesis) {	s.append(")"); } 
    s.append(" return " + getClassWideAccess( TomBase.getTLType(returnType) ) + " is\nbegin\n");
    output.write(s);
    output.write(" return ");
    generateInstruction(0,instr,moduleName);
    output.write(";\n");
    output.write("end "+ prefix+funName + ";\n ");
    }
  }

  //OK
  protected void buildNamedBlock(int deep, String blockName, InstructionList instList, String moduleName) throws IOException {
    output.writeln("<<"+ blockName + ">>;");
    generateInstructionList(deep+1,instList,moduleName);
  }

//OK
  protected void buildExpTrue(int deep) throws IOException {
    output.write(" True ");
  }

//OK
  protected void buildExpFalse(int deep) throws IOException {
    output.write(" False ");
  }

//OK
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
      output.write(" null ");
    }
  }
  
  protected void buildListOrArray(int deep, BQTerm list, String moduleName) throws IOException {
	%match(list) {
	  BuildEmptyList(Name(name)) -> {
		String prefix = "tom_empty_list_";
		String template = getSymbolTable(moduleName).getMakeEmptyList(`name);
		if(instantiateTemplate(deep,template,`concBQTerm(),moduleName) == false) {
		  output.write(prefix + `name);
		}
		return;
	  }

	  BuildConsList(Name(name), headTerm, tailTerm) -> {
		String prefix = "tom_cons_list_";
		String template = getSymbolTable(moduleName).getMakeAddList(`name);
		if(instantiateTemplate(deep,template,`concBQTerm(headTerm,tailTerm),moduleName) == false) {
		  output.write(prefix + `name + "(");
		  generateBQTerm(deep,`headTerm,moduleName);
		  output.write(",");
		  generateBQTerm(deep,`tailTerm,moduleName);
		  output.write(")");
		}
		return;
	  }

	  BuildAppendList(Name(name), headTerm, tailTerm) -> {
		output.write("tom_append_list_" + `name + "(");
		generateBQTerm(deep,`headTerm,moduleName);
		output.write(",");
		generateBQTerm(deep,`tailTerm,moduleName);
		output.write(")");
		return;
	  }

	  BuildEmptyArray(Name(name),size) -> {
		String prefix = "tom_empty_array_";
		String template = getSymbolTable(moduleName).getMakeEmptyArray(`name);
		if(instantiateTemplate(deep,template,`concBQTerm(size),moduleName) == false) {
		  output.write(prefix + `name + "(");
		  generateBQTerm(deep,`size,moduleName);
		  output.write(")");
		}
		return;
	  }

	  BuildConsArray(Name(name), headTerm, tailTerm) -> {
		String template = getSymbolTable(moduleName).getMakeAddArray(`name);
		if(instantiateTemplate(deep,template,`concBQTerm(headTerm,tailTerm),moduleName) == false) {
		  String prefix = "tom_cons_array_";
		  output.write(prefix + `name + "(");
		  generateBQTerm(deep,`headTerm,moduleName);
		  output.write(",");
		  generateBQTerm(deep,`tailTerm,moduleName);
		  output.write(")");
		}
		return;
	  }

	  BuildAppendArray(Name(name), headTerm, tailTerm) -> {
		output.write("tom_append_array_" + `name + "(");
		generateBQTerm(deep,`headTerm,moduleName);
		output.write(",");
		generateBQTerm(deep,`tailTerm,moduleName);
		output.write(")");
		return;
	  }
	}
  }
  
  
//OK
	protected void buildFunctionCall(int deep, String name, BQTermList argList, String moduleName) throws IOException {
		output.write(name);
		boolean parenthesis = !argList.isEmptyconcBQTerm();
		if (parenthesis) { output.writeOpenBrace(); }
		while(!argList.isEmptyconcBQTerm()) {
			generateBQTerm(deep,argList.getHeadconcBQTerm(),moduleName);
			argList = argList.getTailconcBQTerm();
			if(!argList.isEmptyconcBQTerm()) {
				output.writeComa();
			}
		}
		if (parenthesis) { output.writeCloseBrace(); }
	}

  //OK
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
    s.append(modifier + "function " + declName + suffix);
    if (args.length > 0) { s.append("("); }
    for(int i=0 ; i<args.length ; ) {
    s.append(args[i+1]); // parameter name
    s.append(": ");
    s.append(args[i]); // parameter type
    i+=2;
    if(i<args.length) {
    s.append("; ");
    }
    }
    if (args.length > 0) { s.append(")"); }
    s.append(" return " + returnType + " is\nbegin\n");
    output.write(s);
    
    String returnValue = getSymbolTable(moduleName).isVoidType(returnType)?tlCode.getCode():"return " + tlCode.getCode();
    %match(tlCode) {
      TL(_,TextPosition[Line=startLine], TextPosition[Line=endLine]) -> {
        output.write(0,returnValue, `startLine, `endLine - `startLine);
        return;
      }

      ITL(_) -> {
        output.write(returnValue);
        return;
      }

    }
    output.write(";\n end\n");
  }

  //OK
  protected void buildAssignArray(int deep, BQTerm var, OptionList optionList, BQTerm index, 
      Expression exp, String moduleName) throws IOException {
    buildAssignArrayVar(deep,var,optionList, index, exp, moduleName);
  }

  //OK
  protected void buildAssignArrayVar(int deep, BQTerm var, OptionList optionList, BQTerm index, 
      Expression exp, String moduleName) throws IOException {    
    //output.indent(deep);
    generateArray(deep,var,index,moduleName);
    output.write(":=");
    generateExpression(deep,exp,moduleName);
    output.write(";\n");
  } 
  
  protected void buildFunctionDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,this.modifier);
  }

  protected void buildMethodDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,"public ");
  }
  
  private void buildMethod(int deep, String tomName, BQTermList varList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName, String methodModifier) throws IOException {
	boolean parenthesis = !varList.isEmptyconcBQTerm();
	output.write(deep, "function " + tomName);
	if (parenthesis) { output.write("("); }
	
    while(!varList.isEmptyconcBQTerm()) {
      BQTerm localVar = varList.getHeadconcBQTerm();
	matchBlock: {
              %match(localVar) {
                v@BQVariable[] -> {
                  //output.write(deep,getTLType(`type2) + " ");
                  generateBQTerm(deep,`v,moduleName);
                  break matchBlock;
                }
                _ -> {
                  System.out.println("MakeFunction: strange term: " + localVar);
                  throw new TomRuntimeException("MakeFunction: strange term: " + localVar);
                }
              }
            }
            varList = varList.getTailconcBQTerm();
            if(!varList.isEmptyconcBQTerm()) {
              output.write(deep,", ");

            }
    }
    if (parenthesis) { output.write(deep,")"); }

	output.write(" return " + TomBase.getTLType(codomain) + " is\nbegin\n");
	generateInstruction(deep,instruction,moduleName);
	output.writeln(deep,"\nend " + tomName);
  }

}
