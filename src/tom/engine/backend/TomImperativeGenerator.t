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

 	protected void buildTerm(String name, TomList argList) {
		out.write("tom_make_");
		out.write(name);
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
                out.write("tom_insert_list_" + name + "(");
                generate(out,deep,elt);
                out.write(",");
                break matchBlock;
	      }
              _ -> {
                out.write("tom_make_insert_" + name + "(");
                generate(out,deep,elt);
                out.write(",");
                break matchBlock;
              }
            }
          } // end matchBlock
          
          argList = argList.getTail();
          size++;
        }
        out.write("(" + listType + ") tom_make_empty_" + name + "()");
        for(int i=0; i<size; i++) {
          out.write(")");
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
						out.write("tom_append_array_" + name + "(");
						generate(out,deep,elt);
						out.write(",");
						break matchBlock;
              }
					
					_ -> {
						out.write("tom_make_append_" + name + "(");
						generate(out,deep,elt);
						out.write(",");
						break matchBlock;
					}
            }
			} // end matchBlock
      
			reverse = reverse.getTail();
			size++;
		}
		out.write("(" + listType + ") tom_make_empty_" + name + "(" + size + ")"); 
		for(int i=0; i<size; i++) { 
            //out.write("," + i + ")");
			out.write(")"); 
		}
	}

	protected void buildFunctionCall(String name, TomList argList) {
        out.write(name + "(");
        while(!argList.isEmpty()) {
          generate(out,deep,argList.getHead());
          argList = argList.getTail();
          if(!argList.isEmpty()) {
            out.write(", ");
          }
        }
        out.write(")");
	}

	protected abstract void buildDeclaration(TomTerm var, String name, String type, TomType tlType);

	protected void buildDeclarationStar(TomTerm var, String name, String type, TomType tlType) {
		out.write(deep,getTLCode(tlType) + " ");
		generate(out,deep,var);
		out.writeln(";");
	}

	protected void buildFunctionBegin(String tomName, TomList varList) {
		TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
		String glType = getTLType(getSymbolCodomain(tomSymbol));
		String name = tomSymbol.getAstName().getString();
    
		out.write(deep,glType + " " + name + "(");
		while(!varList.isEmpty()) {
			TomTerm localVar = varList.getHead();
			matchBlock: {
				%match(TomTerm localVar) {
					v@Variable(option2,name2,type2) -> {
						out.write(deep,getTLType(type2) + " ");
						generate(out,deep,v);
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
				out.write(deep,", ");
				
			}
		}
		out.writeln(deep,") {");
		//out.writeln(deep,"return null;");
	}
	
	protected void buildFunctionEnd() {
			out.writeln(deep,"}");
	}
	
	protected void buildExpNot(Expression exp) {
		out.write("!(");
		generateExpression(out,deep,exp);
		out.write(")");
	}

  protected abstract void buildExpTrue() {
    if(cCode) {
      out.write(" 1 ");
    } else if(jCode) {
      out.write(" true ");
    } else if(eCode) {
      out.write(" true ");
    }
  }
  
  protected abstract void buildExpFalse() {
    if(cCode) {
      out.write(" 0 ");
    } else if(jCode) {
      out.write(" false ");
    } else if(eCode) {
      out.write(" false ");
    }
  }
	
}
