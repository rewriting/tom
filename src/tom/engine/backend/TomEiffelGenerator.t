/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2004 INRIA
			    Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.backend;
 
import aterm.*;

import java.io.IOException;

import jtom.adt.tomsignature.types.*;

import jtom.tools.TomTaskInput;
import jtom.tools.OutputCode;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public class TomEiffelGenerator extends TomImperativeGenerator {
  
  public TomEiffelGenerator(TomEnvironment environment, OutputCode output, TomTaskInput input) {
		super(environment, output, input);
  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------

  protected void buildComment(int deep, String text) throws IOException {
    output.writeln("-- " + text);
    return;
  }

 	protected void buildTerm(int deep, String name, TomList argList) throws IOException {
		output.write("tom_make_");
		output.write(name);
		if(argList.isEmpty()) { // strange ?
		} else {
			output.writeOpenBrace();
			while(!argList.isEmpty()) {
				generate(deep,argList.getHead());
				argList = argList.getTail();
				if(!argList.isEmpty()) {
              output.writeComa();
				}
			}
			output.writeCloseBrace();
		}
	}

	protected void buildFunctionBegin(int deep, String tomName, TomList varList) throws IOException {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        String glType = getTLType(getSymbolCodomain(tomSymbol));
        String name = tomSymbol.getAstName().getString();
				output.write(deep,name + "(");
				while(!varList.isEmpty()) {
          TomTerm localVar = varList.getHead();
          matchBlock: {
            %match(TomTerm localVar) {
              v@Variable[option=option2,astName=name2,astType=type2] -> {
                  generate(deep,v);
                  output.write(deep,": " + getTLType(type2));
                break matchBlock;
              }
              _ -> {
                System.out.println("MakeFunction: strange term: " + localVar);
                throw new TomRuntimeException(new Throwable("MakeFunction: strange term: " + localVar));
              }
            }
          }
          varList = varList.getTail();
          if(!varList.isEmpty()) {
              output.write(deep,"; ");
          }
        }
				output.writeln(deep,"): " + glType + " is");
				output.writeln(deep,"local ");
				//out.writeln(deep,"return null;");
	}
	
	protected void buildFunctionEnd(int deep) throws IOException {
			output.writeln(deep,"end;");
	}
	
	protected void buildExpNot(int deep, Expression exp) throws IOException {
		output.write("not ");
		generateExpression(deep,exp);
	}

  protected void buildExpTrue(int deep) throws IOException {
		output.write(" true ");
  }
  
  protected void buildExpFalse(int deep) throws IOException {
		output.write(" false ");
  }

  protected void buildAssignVar(int deep, TomTerm var, OptionList list, String type, TomType tlType, Expression exp) throws IOException {
    output.indent(deep);
    generate(deep,var);
    if(symbolTable().isBoolType(type) || symbolTable().isIntType(type) || symbolTable().isDoubleType(type)) {
      output.write(" := ");
    } else {
      //out.write(" ?= ");
      String assignSign = " := ";
      %match(Expression exp) {
        GetSubterm[] -> {
          assignSign = " ?= ";
        }
      }
      output.write(assignSign);
    }
    generateExpression(deep,exp);
    output.writeln(";");
    if(debugMode && !list.isEmpty()) {
      output.write("jtom.debug.TomDebugger.debugger.addSubstitution(\""+debugKey+"\",\"");
      generate(deep,var);
      output.write("\", ");
      generate(deep,var); // generateExpression(out,deep,exp);
      output.writeln(");");
    }
  }

  protected void buildExpCast(int deep, TomType tlType, Expression exp) throws IOException {
    generateExpression(deep,exp);
  }

  protected void buildLet(int deep, TomTerm var, OptionList list, String type, TomType tlType, 
                          Expression exp, Instruction body) throws IOException {
    System.out.println("buildLet code not yet implemented");
    throw new TomRuntimeException(new Throwable("buildLet: Eiffel code not yet implemented"));
  }

  protected void buildLetRef(int deep, TomTerm var, OptionList list,
                             String type, TomType tlType, 
                             Expression exp, Instruction body) throws IOException {
    System.out.println("buildLetRef code not yet implemented");
    throw new TomRuntimeException(new Throwable("buildLetRef: Eiffel code not yet implemented"));
  }

	
	protected void buildAssignMatch(int deep, TomTerm var, String type, TomType tlType, Expression exp) throws IOException {
    output.indent(deep);
    generate(deep,var);
		if(symbolTable().isBoolType(type) || symbolTable().isIntType(type) || symbolTable().isDoubleType(type)) {
                  output.write(" := ");
		} else {
                    //out.write(" ?= ");
                  String assignSign = " := ";
                  %match(Expression exp) {
                    GetSubterm[] -> {
                      assignSign = " ?= ";
                    }
                  }
                  output.write(assignSign);
    }
    generateExpression(deep,exp);
    output.writeln(";");
    if (debugMode) {
      output.write("jtom.debug.TomDebugger.debugger.specifySubject(\""+debugKey+"\",\"");
      generateExpression(deep,exp);
      output.write("\",");
      generateExpression(deep,exp);
      output.writeln(");");
    }
  }

  protected void buildNamedBlock(int deep, String blockName, InstructionList instList) throws IOException {
		System.out.println("NamedBlock: Eiffel code not yet implemented");
		throw new TomRuntimeException(new Throwable("NamedBlock: Eiffel code not yet implemented"));
  }

  protected void buildUnamedBlock(int deep, InstructionList instList) throws IOException {
		System.out.println("UnamedBlock: Eiffel code not yet implemented");
		throw new TomRuntimeException(new Throwable("NamedBlock: Eiffel code not yet implemented"));
  }

  protected void buildIfThenElse(int deep, Expression exp, TomList succesList) throws IOException {
		output.write(deep,"if "); generateExpression(deep,exp); output.writeln(" then ");
		generateList(deep+1,succesList);
		output.writeln(deep,"end;");
  }

  protected void buildIfThenElseWithFailure(int deep, Expression exp, TomList succesList, TomList failureList) throws IOException {
		output.write(deep,"if "); generateExpression(deep,exp); output.writeln(" then ");
		generateList(deep+1,succesList);
		output.writeln(deep," else ");
		generateList(deep+1,failureList);
		output.writeln(deep,"end;");
  }

  protected void buildExitAction(int deep, TomNumberList numberList) throws IOException {
      System.out.println("ExitAction: Eiffel code not yet implemented");
      throw new TomRuntimeException(new Throwable("ExitAction: Eiffel code not yet implemented"));
  }

  protected void buildReturn(int deep, TomTerm exp) throws IOException {
		output.writeln(deep,"if Result = Void then");
		output.write(deep+1,"Result := ");
		generate(deep+1,exp);
		output.writeln(deep+1,";");
		output.writeln(deep,"end;");
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

	protected void buildGetSubtermDecl(int deep, String name1, String name2, String type1, TomType tlType1, TomType tlType2, TargetLanguage tlCode) throws IOException {
    String args[];
    if(strictType) {
      args = new String[] { getTLCode(tlType1), name1,
                            getTLCode(tlType2), name2 };
    } else {
			args = new String[] { getTLType(getUniversalType()), name1,
														getTLCode(tlType2), name2 };
    }
    generateTargetLanguage(deep, genDecl(getTLType(getUniversalType()), "tom_get_subterm", type1,
																				 args, tlCode));
	}
	
	protected TargetLanguage genDecl(int deep, String returnType,
																	 String declName,
																	 String suffix,
																	 String args[],
																	 TargetLanguage tlCode) {
    String s = "";
    if(!genDecl) { return null; }
		s = declName + "_" + suffix + "(";
		for(int i=0 ; i<args.length ; ) {
			s+= args[i+1] + ": " + args[i];
			i+=2;
			if(i<args.length) {
				s+= "; ";
			}
		} 
		s += "): " + returnType + " is do Result := " + tlCode.getCode() + "end;";
    if(tlCode.isTL())
      return `TL(s, tlCode.getStart(), tlCode.getEnd());
    else
      return `ITL(s);
  }

  protected TargetLanguage genDeclMake(int deep, String opname, TomType returnType, 
                                            TomList argList, TargetLanguage tlCode) throws IOException {
      //%variable
    String s = "";
    if(!genDecl) { return null; }
		boolean braces = !argList.isEmpty();
		s = "tom_make_" + opname;
		if(braces) {
			s = s + "(";
		}
		while(!argList.isEmpty()) {
			TomTerm arg = argList.getHead();
			matchBlock: {
				%match(TomTerm arg) {
					Variable[option=option,astName=Name(name), astType=Type(ASTTomType(type),tlType@TLType[])] -> {
						s += name + ": " + getTLCode(tlType);
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
				s += "; ";
			}
		}
      if(braces) {
        s = s + ")";
      }
      s += ": " + getTLType(returnType) + " is do Result := " + tlCode.getCode() + "end;";
			return `TL(s, tlCode.getStart(), tlCode.getEnd());
  }

  protected TargetLanguage genDeclList(int deep, String name, TomType listType, TomType eltType) throws IOException {
      //%variable
    String s = "";
    if(!genDecl) { return null; }
		System.out.println("genDeclList: Eiffel code not yet implemented");
		return null;
  }


  protected TargetLanguage genDeclArray(int deep, String name, TomType listType, TomType eltType) throws IOException {
		//%variable
    String s = "";
    if(!genDecl) { return null; }
		System.out.println("genDeclArray: Eiffel code not yet implemented");
    return null;
  }

  protected TargetLanguage genDecl(String returnType,
                        String declName,
                        String suffix,
                        String args[],
                        TargetLanguage tlCode) {
    String s = "";
    if(!genDecl) { return null; }
		s = declName + "_" + suffix + "(";
		for(int i=0 ; i<args.length ; ) {
			s+= args[i+1] + ": " + args[i];
			i+=2;
			if(i<args.length) {
				s+= "; ";
			}
		} 
		s += "): " + returnType + " is do Result := " + tlCode.getCode() + "end;";
    if(tlCode.isTL())
      return `TL(s, tlCode.getStart(), tlCode.getEnd());
    else
      return `ITL(s);
  }

}
