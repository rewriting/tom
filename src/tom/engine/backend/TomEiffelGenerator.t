/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003 INRIA
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

import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.util.HashMap;

import jtom.adt.tomsignature.types.*;

import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import jtom.tools.OutputCode;
import jtom.tools.SingleLineOutputCode;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public class TomEiffelGenerator extends TomImperativeGenerator {
  
  public TomEiffelGenerator(TomEnvironment environment, OutputCode output, TomTaskInput input) {
		super(environment, output, input);
  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------

 	protected void buildTerm(int deep, String name, TomList argList) {
		out.write("tom_make_");
		out.write(name);
		if(argList.isEmpty()) { // strange ?
		} else {
			out.writeOpenBrace();
			while(!argList.isEmpty()) {
				generate(out,deep,argList.getHead());
				argList = argList.getTail();
				if(!argList.isEmpty()) {
              out.writeComa();
				}
			}
			out.writeCloseBrace();
		}
	}

	protected void buildDeclaration(int deep, TomTerm var, String name, String type, TomType tlType) {
		generate(out,deep,var);
		out.write(deep,": " + getTLCode(tlType));
		out.writeln(";");
	}
	
	protected void buildDeclarationStar(int deep, TomTerm var, String name, String type, TomType tlType) {
		generate(out,deep,var);
		out.write(deep,": " + getTLCode(tlType));
		out.writeln(";");
	}

	protected void buildFunctionBegin(int deep, String tomName, TomList varList) {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        String glType = getTLType(getSymbolCodomain(tomSymbol));
        String name = tomSymbol.getAstName().getString();
				out.write(deep,name + "(");
				while(!varList.isEmpty()) {
          TomTerm localVar = varList.getHead();
          matchBlock: {
            %match(TomTerm localVar) {
              v@Variable(option2,name2,type2) -> {
                if(cCode || jCode) {
                  out.write(deep,getTLType(type2) + " ");
                  generate(out,deep,v);
                } else if(eCode) {
                  generate(out,deep,v);
                  out.write(deep,": " + getTLType(type2));
                }
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
            if(cCode || jCode) {
              out.write(deep,", ");
            } else if(eCode) {
              out.write(deep,"; ");
            }
          }
        }
				out.writeln(deep,"): " + glType + " is");
				out.writeln(deep,"local ");
				//out.writeln(deep,"return null;");
	}
	
	protected void buildFunctionEnd(int deep) {
			out.writeln(deep,"end;");
	}
	
	protected void buildExpNot(int deep, Expression exp) {
		out.write("not ");
		generateExpression(out,deep,exp);
	}

  protected void buildExpTrue(int deep) {
		out.write(" true ");
  }
  
  protected void buildExpFalse(int deep) {
		out.write(" false ");
  }

  protected void buildAssignVar(int deep, TomTerm var, TomType type, TomType tlType) {
    out.indent(deep);
    generate(out,deep,var);
		if(isBoolType(type) || isIntType(type) || isDoubleType(type)) {
			out.write(" := ");
		} else {
			//out.write(" ?= ");
			String assignSign = " := ";
			%match(Expression exp) {
				GetSubterm[] -> {
					assignSign = " ?= ";
				}
			}
			out.write(assignSign);
		}
    generateExpression(deep,exp);
    out.writeln(";");
    if(debugMode && !list.isEmpty()) {
      out.write("jtom.debug.TomDebugger.debugger.addSubstitution(\""+debugKey+"\",\"");
      generate(out,deep,var);
      out.write("\", ");
      generate(out,deep,var); // generateExpression(out,deep,exp);
      out.write(");\n");
    }
  }
	
	protected void buildAssignMatch(int deep, TomTerm var, TomType type, TomType tlType) {
    out.indent(deep);
    generate(deep,var);
		if(isBoolType(type) || isIntType(type) || isDoubleType(type)) {
			out.write(" := ");
		} else {
			//out.write(" ?= ");
			String assignSign = " := ";
			%match(Expression exp) {
				GetSubterm[] -> {
					assignSign = " ?= ";
				}
			}
			out.write(assignSign);
    }
    generateExpression(out,deep,exp);
    out.writeln(";");
    if (debugMode) {
      out.write("jtom.debug.TomDebugger.debugger.specifySubject(\""+debugKey+"\",\"");
      generateExpression(out,deep,exp);
      out.write("\",");
      generateExpression(out,deep,exp);
      out.writeln(");");
    }
  }

  protected void buildNamedBlock(int deep, String blockName, TomList instList) {
		System.out.println("NamedBlock: Eiffel code not yet implemented");
		throw new TomRuntimeException(new Throwable("NamedBlock: Eiffel code not yet implemented"));
  }

  protected void buildIfThenElse(int deep, Expression exp, TomList succesList) {
		out.write(deep,"if "); generateExpression(deep,exp); out.writeln(" then ");
		generateList(deep+1,succesList);
		out.writeln(deep,"end;");
  }

  protected void buildIfThenElseWithFailure(int deep, Expression exp, TomList succesList, TomList failureList) {
		out.write(deep,"if "); generateExpression(out,deep,exp); out.writeln(" then ");
		generateList(out,deep+1,succesList);
		out.writeln(deep," else ");
		generateList(out,deep+1,failureList);
		out.writeln(deep,"end;");
  }

  protected void buildAssignVarExp(int deep, TomTerm var, TomType tlType, Expression exp) {
    out.indent(deep);
    generate(deep,var);
		out.write(" := ");
    generateExpression(deep,exp);
    out.writeln(";");
    if(debugMode && !list.isEmpty()) {
      out.write("jtom.debug.TomDebugger.debugger.addSubstitution(\""+debugKey+"\",\"");
      generate(deep,var);
      out.write("\", ");
      generate(deep,var); // generateExpression(out,deep,exp);
      out.write(");\n");
    }
  }

  protected void buildExitActionint(int deep, TomNumberList numberList) {
      System.out.println("ExitAction: Eiffel code not yet implemented");
      throw new TomRuntimeException(new Throwable("ExitAction: Eiffel code not yet implemented"));
  }

  protected void buildReturn(int deep, Expression exp) {
		out.writeln(deep,"if Result = Void then");
		out.write(deep+1,"Result := ");
		generate(out,deep+1,exp);
		out.writeln(deep+1,";");
		out.writeln(deep,"end;");
	}

	protected void buildGetSubtermDecl(int deep, String name1, String name2, TomType type1, TomType tlType1, TomType tlType2) {
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

  protected void TargetLanguage genDeclMake(int deep, String opname, TomType returnType, 
                                            TomList argList, TargetLanguage tlCode) {
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
					Variable(option,Name(name), Type(ASTTomType(type),tlType@TLType[])) -> {
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

  protected void TargetLanguage genDeclList(int deep, String name, TomType listType, TomType eltType) {
      //%variable
    String s = "";
    if(!genDecl) { return null; }
		System.out.println("genDeclList: Eiffel code not yet implemented");
		return null;
  }


  protected void TargetLanguage genDeclArray(int deep, String name, TomType listType, TomType eltType) {
		//%variable
    String s = "";
    if(!genDecl) { return null; }
		System.out.println("genDeclArray: Eiffel code not yet implemented");
    return null;
  }



}