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


	protected buildSymbolDecl(String tomName) {
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

  protected buildArraySymbolDecl(String tomName) {
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
    if(cCode) {
        // TODO: build an abstract declaration
      int argno=1;
      out.indent(deep);
      if(!l.isEmpty()) {
        out.write(getTLType(type1));
        out.writeSpace();
        out.write(name1);
        if(!l.isEmpty()) {
          out.writeOpenBrace();
          while (!l.isEmpty()) {
            out.write(getTLType(l.getHead()));
            out.writeUnderscore();
            out.write(argno);
            argno++;
            l = l.getTail() ;
            if(!l.isEmpty()) {
              out.writeComa();
            }
          }
          out.writeCloseBrace();
          out.writeSemiColon();
        }
      }
      out.writeln();
    } else if(jCode) {
        // do nothing
    } else if(eCode) {
        // do nothing
    }
    
      // inspect the optionList
    generateOptionList(out, deep, optionList);
      // inspect the slotlist
    generateSlotList(out, deep, slotList);
  }

    protected void buildGetSubtermDecl(String name1, String name2, TomType type1, TomType tlType1, TomType tlType2) {
    String args[];
    if(strictType || eCode) {
      args = new String[] { getTLCode(tlType1), name1,
                            getTLCode(tlType2), name2 };
    } else {
          args = new String[] { getTLType(getUniversalType()), name1,
                                getTLCode(tlType2), name2 };
    }
    generateTargetLanguage(out,deep, genDecl(getTLType(getUniversalType()), "tom_get_subterm", type1,
                                             args, tlCode));
  }
  
} // class TomImperativeGenerator
