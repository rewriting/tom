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

public class TomImperativeGenerator extends TomAbstractGenerator {
  
  public TomImperativeGenerator(TomEnvironment environment, OutputCode output, TomTaskInput input) {
		super(environment, output, input);
  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------

	/*
	 * the method implementations are here common to C and Java
	 */
 	protected void buildTerm(String name, TomList argList) {
		output.write("tom_make_");
		output.write(name);
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
	
	protected void buildList(String name, TomList argList) {
		TomSymbol tomSymbol = symbolTable().getSymbol(name);
		String listType = getTLType(getSymbolCodomain(tomSymbol));
		int size = 0;
		while(!argList.isEmpty()) {
			TomTerm elt = argList.getHead();

			matchBlock: {
				%match(TomTerm elt) {
					Composite(concTomTerm(VariableStar[])) | VariableStar[] |
						Composite(concTomTerm(ExpressionToTomTerm(GetSliceList[]))) |
						Composite(concTomTerm(Variable[])) -> {
						output.write("tom_insert_list_" + name + "(");
						generate(deep,elt);
						output.write(",");
						break matchBlock;
					}
					_ -> {
						output.write("tom_make_insert_" + name + "(");
						generate(deep,elt);
						output.write(",");
						break matchBlock;
					}
				}
			} // end matchBlock
          
			argList = argList.getTail();
			size++;
		}
		output.write("(" + listType + ") tom_make_empty_" + name + "()");
		for(int i=0; i<size; i++) {
			output.write(")");
		}
	} 

	protected void buildArray(String name, TomList argList) {
		TomSymbol tomSymbol = symbolTable().getSymbol(name);
		String listType = getTLType(getSymbolCodomain(tomSymbol));
		int size = 0;
		TomList reverse = reverse(argList);
		while(!reverse.isEmpty()) {
			TomTerm elt = reverse.getHead();
			
			matchBlock: {
				%match(TomTerm elt) {
					Composite(concTomTerm(VariableStar[])) | VariableStar[] -> {
						output.write("tom_append_array_" + name + "(");
						generate(deep,elt);
						output.write(",");
						break matchBlock;
					}
					
					_ -> {
						output.write("tom_make_append_" + name + "(");
						generate(deep,elt);
						output.write(",");
						break matchBlock;
					}
				}
			} // end matchBlock
      
			reverse = reverse.getTail();
			size++;
		}
		output.write("(" + listType + ") tom_make_empty_" + name + "(" + size + ")"); 
		for(int i=0; i<size; i++) { 
			//out.write("," + i + ")");
			output.write(")"); 
		}
	}

	protected void buildFunctionCall(String name, TomList argList) {
		output.write(name + "(");
		while(!argList.isEmpty()) {
			generate(deep,argList.getHead());
			argList = argList.getTail();
			if(!argList.isEmpty()) {
				output.write(", ");
			}
		}
		output.write(")");
	}

	protected abstract void buildDeclaration(TomTerm var, String name, String type, TomType tlType);

	protected void buildDeclarationStar(TomTerm var, String name, String type, TomType tlType) {
		output.write(deep,getTLCode(tlType) + " ");
		generate(deep,var);
		output.writeln(";");
	}

	protected void buildFunctionBegin(String tomName, TomList varList) {
		TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
		String glType = getTLType(getSymbolCodomain(tomSymbol));
		String name = tomSymbol.getAstName().getString();
    
		output.write(deep,glType + " " + name + "(");
		while(!varList.isEmpty()) {
			TomTerm localVar = varList.getHead();
			matchBlock: {
				%match(TomTerm localVar) {
					v@Variable(option2,name2,type2) -> {
						output.write(deep,getTLType(type2) + " ");
						generate(deep,v);
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
				output.write(deep,", ");
				
			}
		}
		output.writeln(deep,") {");
		//out.writeln(deep,"return null;");
	}
	
	protected void buildFunctionEnd() {
		output.writeln(deep,"}");
	}
	
	protected void buildExpNot(Expression exp) {
		out.write("!(");
		generateExpression(out,deep,exp);
		out.write(")");
	}

  protected abstract void buildExpTrue();
  protected abstract void buildExpFalse();
  
  protected void buildAssignVar(int deep, TomTerm var, TomType type, TomType tlType) {
    output.indent(deep);
    generate(deep,var);
		output.write(" = (" + getTLCode(tlType) + ") ");
		generateExpression(deep,exp);
    output.writeln(";");
    if(debugMode && !list.isEmpty()) {
      output.write("jtom.debug.TomDebugger.debugger.addSubstitution(\""+debugKey+"\",\"");
      generate(deep,var);
      output.write("\", ");
      generate(deep,var); // generateExpression(out,deep,exp);
      output.write(");\n");
    }
  }

	protected void buildAssignMatch(int deep, TomTerm var, TomType type, TomType tlType) {
    output.indent(deep);
    generate(deep,var);
		output.write(" = (" + getTLCode(tlType) + ") ");
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

  protected abstract void buildNamedBlock(String blockName, TomList instList);

  protected void buildIfThenElse(Expression exp, TomList succesList) {
		output.write(deep,"if("); 
		generateExpression(deep,exp); 
		output.writeln(") {");
		generateList(deep+1,succesList);
		output.writeln(deep,"}");
  }

  protected void buildIfThenElseWithFailure(Expression exp, TomList succesList, TomList failureList) {
		output.write(deep,"if("); 
		generateExpression(deep,exp); 
		output.writeln(") {");
		generateList(deep+1,succesList);
		output.writeln(deep,"} else {");
		generateList(deep+1,failureList);
		output.writeln(deep,"}");
  }

  protected void buildAssignVarExp(int deep, TomTerm var, TomType tlType, Expression exp) {
    output.indent(deep);
    generate(deep,var);
		output.write(" = (" + getTLCode(tlType) + ") ");
    generateExpression(deep,exp);
    output.writeln(";");
    if(debugMode && !list.isEmpty()) {
      output.write("jtom.debug.TomDebugger.debugger.addSubstitution(\""+debugKey+"\",\"");
      generate(deep,var);
      output.write("\", ");
      generate(deep,var); // generateExpression(out,deep,exp);
      output.write(");\n");
    }
  }

  protected abstract void buildExitAction(TomNumberList numberList);

  protected void buildReturn(Expression exp) {
		output.write(deep,"return ");
		generate(deep,exp);
		output.writeln(deep,";");
  }


	protected void buildSymbolDecl(String tomName) {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    OptionList optionList = tomSymbol.getOption();
    SlotList slotList = tomSymbol.getSlotList();
    TomTypeList l = getSymbolDomain(tomSymbol);
    TomType type1 = getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
    
		// inspect the optionList
    generateOptionList(deep, optionList);
		// inspect the slotlist
    generateSlotList(deep, slotList);
  }

  protected void buildArraySymbolDecl(String tomName) {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    OptionList optionList = tomSymbol.getOption();
    SlotList slotList = tomSymbol.getSlotList();        
    TomTypeList l = getSymbolDomain(tomSymbol);
    TomType type1 = getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
    
		// inspect the optionList
    generateOptionList(deep, optionList);
		// inspect the slotlist
    generateSlotList(deep, slotList);
  }

  protected void buildListSymbolDecl(int deep, String tomName) {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    OptionList optionList = tomSymbol.getOption();
    SlotList slotList = tomSymbol.getSlotList();
    TomTypeList l = getSymbolDomain(tomSymbol);
    TomType type1 = getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
    
		// inspect the optionList
    generateOptionList(deep, optionList);
		// inspect the slotlist
    generateSlotList(deep, slotList);
  }

  protected void buildGetSubtermDecl(String name1, String name2, TomType type1, TomType tlType1, TomType tlType2) {
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

    protected TargetLanguage genDecl(String returnType,
                                            String declName,
                                            String suffix,
                                            String args[],
                                            TargetLanguage tlCode) {
    String s = "";
    if(!genDecl) { return null; }
    String modifier ="";
    if(cCode || jCode) {
      if(staticFunction) {
        modifier += "static ";
      }
      if(jCode) {
        modifier += "public ";
      }
      s = modifier + returnType + " " + declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i] + " " + args[i+1];
        i+=2;
        if(i<args.length) {
          s+= ", ";
        }
      } 
      s += ") { return " + tlCode.getCode() + "; }";
    } else if(eCode) {
      s = declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i+1] + ": " + args[i];
        i+=2;
        if(i<args.length) {
          s+= "; ";
        }
      } 
      s += "): " + returnType + " is do Result := " + tlCode.getCode() + "end;";
    }
    if(tlCode.isTL())
      return `TL(s, tlCode.getStart(), tlCode.getEnd());
    else
      return `ITL(s);
  }

  protected void TargetLanguage genDeclMake(String opname, TomType returnType, 
                                            TomList argList, TargetLanguage tlCode) {
      //%variable
    String s = "";
    if(!genDecl) { return null; }
    String modifier = "";
    if(jCode || cCode) {
      if(staticFunction) {
        modifier += "static ";
      }
      if(jCode) {
        modifier += "public ";
      }
      
      s = modifier + getTLType(returnType) + " tom_make_" + opname + "(";
      while(!argList.isEmpty()) {
        TomTerm arg = argList.getHead();
        matchBlock: {
          %match(TomTerm arg) {
            Variable(option,Name(name), Type(ASTTomType(type),tlType@TLType[])) -> {
              s += getTLCode(tlType) + " " + name;
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
      s += ") { ";
      if (debugMode) {
        s += "\n"+getTLType(returnType)+ " debugVar = " + tlCode.getCode() +";\n";
        s += "jtom.debug.TomDebugger.debugger.termCreation(debugVar);\n";
        s += "return  debugVar;\n}";
      } else {
        s += "return " + tlCode.getCode() + "; }";
      }
    } else if(eCode) {
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
    }
    return `TL(s, tlCode.getStart(), tlCode.getEnd());
  }

  protected void TargetLanguage genDeclList(String name, TomType listType, TomType eltType) {
      //%variable
    String s = "";
    if(!genDecl) { return null; }

    String tomType = getTomType(listType);
    String glType = getTLType(listType);
    String tlEltType = getTLType(eltType);

    String utype = glType;
    String modifier = "";
    if(eCode) {
      System.out.println("genDeclList: Eiffel code not yet implemented");
    } else if(jCode) {
      modifier = "public ";
      if(!strictType) {
        utype = getTLType(getUniversalType());
      }
    }

    if(staticFunction) {
      modifier += "static ";
    }

    String listCast = "(" + glType + ")";
    String eltCast = "(" + getTLType(eltType) + ")";
    String make_empty = listCast +  "tom_make_empty_" + name;
    String is_empty = "tom_is_empty_" + tomType;
    String make_insert = listCast + "tom_make_insert_" + name;
    String get_head = eltCast + "tom_get_head_" + tomType;
    String get_tail = listCast + "tom_get_tail_" + tomType;
    String reverse = listCast + "tom_reverse_" + name;

    s+= modifier + utype + " tom_reverse_" + name + "(" + utype + " l) {\n"; 
    s+= "   " + glType + " result = " + make_empty + "();\n"; 
    s+= "    while(!" + is_empty + "(l) ) {\n"; 
    s+= "      result = " + make_insert + "(" + get_head + "(l),result);\n";    
    s+= "      l = " + get_tail + "(l);\n";  
    s+= "    }\n";
    s+= "    return result;\n";
    s+= "  }\n";
    s+= "\n";

    s+= modifier + utype + " tom_insert_list_" + name +  "(" + utype + " l1, " + utype + " l2) {\n";
    s+= "   if(" + is_empty + "(l1)) {\n";
    s+= "    return l2;\n";  
    s+= "   } else if(" + is_empty + "(l2)) {\n";
    s+= "    return l1;\n";  
    s+= "   } else if(" + is_empty + "(" + get_tail + "(l1))) {\n";  
    s+= "    return " + make_insert + "(" + get_head + "(l1),l2);\n";
    s+= "   } else { \n";  
    s+= "    return " + make_insert + "(" + get_head + "(l1),tom_insert_list_" + name +  "(" + get_tail + "(l1),l2));\n";
    s+= "   }\n";
    s+= "  }\n";
    s+= "\n";


    
    s+= modifier + utype + " tom_get_slice_" + name + "(" + utype + " begin, " + utype + " end) {\n"; 
    s+= "   " + glType + " result = " + make_empty + "();\n"; 
    s+= "    while(!tom_terms_equal_" + tomType + "(begin,end)) {\n";
    s+= "      result = " + make_insert + "(" + get_head + "(begin),result);\n";
    s+= "      begin = " + get_tail + "(begin);\n";
    s+="     }\n";
    s+= "    result = " + reverse + "(result);\n";
    s+= "    return result;\n";
    s+= "  }\n";
    
    TargetLanguage resultTL = `ITL(s);
      //If necessary we remove \n code depending on --pretty option
    resultTL = ast().reworkTLCode(resultTL, pretty);
    return resultTL;
  }

  protected void TargetLanguage genDeclArray(String name, TomType listType, TomType eltType) {
      //%variable
    String s = "";
    if(!genDecl) { return null; }

    String tomType = getTomType(listType);
    String glType = getTLType(listType);
    String tlEltType = getTLType(eltType);
    String utype = glType;
    String modifier = "";
    if(eCode) {
      System.out.println("genDeclArray: Eiffel code not yet implemented");
    } else if(jCode) {
      modifier ="public ";
      if(!strictType) {
        utype =  getTLType(getUniversalType());
      }
    }

    if(staticFunction) {
      modifier += "static ";
    }
    
    String listCast = "(" + glType + ")";
    String eltCast = "(" + getTLType(eltType) + ")";
    String make_empty = listCast + "tom_make_empty_" + name;
    String make_append = listCast + "tom_make_append_" + name;
    String get_element = eltCast + "tom_get_element_" + tomType;

    
    s = modifier + utype + " tom_get_slice_" + name +  "(" + utype + " subject, int begin, int end) {\n";
    s+= "   " + glType + " result = " + make_empty + "(end - begin);\n";
    s+= "    while( begin != end ) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(subject, begin),result);\n";
    s+= "      begin++;\n";
    s+="     }\n";
    s+= "    return result;\n";
    s+= "  }\n";
    s+= "\n";
    
    s+= modifier + utype + " tom_append_array_" + name +  "(" + utype + " l2, " + utype + " l1) {\n";
    s+= "    int size1 = tom_get_size_" + tomType + "(l1);\n";
    s+= "    int size2 = tom_get_size_" + tomType + "(l2);\n";
    s+= "    int index;\n";
    s+= "   " + glType + " result = " + make_empty + "(size1+size2);\n";

    s+= "    index=size1;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(l1,(size1-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";

    s+= "    index=size2;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(l2,(size2-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";
   
    s+= "    return result;\n";
    s+= "  }\n";

    TargetLanguage resultTL = `ITL(s);
      //If necessary we remove \n code depending on --pretty option
    resultTL = ast().reworkTLCode(resultTL, pretty);
    return resultTL;
  }
  
} // class TomImperativeGenerator
