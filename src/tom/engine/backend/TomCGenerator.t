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

public class TomCGenerator extends TomImperativeGenerator {
  
  public TomCGenerator(TomEnvironment environment, OutputCode output, TomTaskInput input) {
		super(environment, output, input);
		if (staticFunction) {
			this.modifier += "static " ;
		}
  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------

	protected void buildDeclaration(int deep, TomTerm var, String name, String type, TomType tlType) throws IOException {
		output.write(deep,getTLCode(tlType) + " ");
		generate(deep,var);
		output.writeln(";");
	}
	
  protected void buildExpTrue(int deep) throws IOException {
		output.write(" 1 ");
  }
  
  protected void buildExpFalse(int deep) throws IOException {
		output.write(" 0 ");
  }

  protected void buildNamedBlock(int deep, String blockName, TomList instList) throws IOException {
		output.writeln("{");
		generateList(deep+1,instList);
		output.writeln("}" + blockName +  ":;");
  }

  protected void buildExitAction(int deep, TomNumberList numberList) throws IOException {
		output.writeln(deep,"goto matchlab" + numberListToIdentifier(numberList) + ";");
  }

	protected void buildSymbolDecl(int deep, String tomName) throws IOException {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    OptionList optionList = tomSymbol.getOption();
    SlotList slotList = tomSymbol.getSlotList();
    TomTypeList l = getSymbolDomain(tomSymbol);
    TomType type1 = getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
    
    if(isDefinedSymbol(tomSymbol)) {
        // TODO: build an abstract declaration
      int argno=1;
        /*
          String s = "";
              if(!l.isEmpty()) {
              s = getTLType(type1) + " " + name1;
              
              if(!l.isEmpty()) {
              s += "(";
              while (!l.isEmpty()) {
              s += getTLType(l.getHead()) + " _" + argno;
              argno++;
              l = l.getTail() ;
              if(!l.isEmpty()) {
              s += ",";
              }
              }
              s += ");";
              }
              }
              generate(out,deep,makeTL(s));
        */
      
      output.indent(deep);
      if(!l.isEmpty()) {
        output.write(getTLType(type1));
        output.writeSpace();
        output.write(name1);
        if(!l.isEmpty()) {
          output.writeOpenBrace();
          while (!l.isEmpty()) {
            output.write(getTLType(l.getHead()));
              //out.writeUnderscore();
              //out.write(argno);
            argno++;
            l = l.getTail() ;
            if(!l.isEmpty()) {
              output.writeComa();
            }
          }
          output.writeCloseBrace();
          output.writeSemiColon();
        }
      }
      output.writeln();
    } 

		// inspect the optionList
    generateOptionList(deep, optionList);
		// inspect the slotlist
    generateSlotList(deep, slotList);
  }


  protected void buildArraySymbolDecl(int deep, String tomName) throws IOException {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    OptionList optionList = tomSymbol.getOption();
    SlotList slotList = tomSymbol.getSlotList();        
    TomTypeList l = getSymbolDomain(tomSymbol);
    TomType type1 = getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
    
		// TODO: build an abstract declaration
		int argno=1;
		output.indent(deep);
		if(!l.isEmpty()) {
			output.write(getTLType(type1));
			output.writeSpace();
			outpput.write(name1);
			if(!l.isEmpty()) {
				output.writeOpenBrace();
				while (!l.isEmpty()) {
					output.write(getTLType(l.getHead()));
					output.writeUnderscore();
					output.write(argno);
					argno++;
					l = l.getTail() ;
					if(!l.isEmpty()) {
						output.writeComa();
					}
				}
				output.writeCloseBrace();
				output.writeSemiColon();
			}
		}
		output.writeln();
    
		// inspect the optionList
    generateOptionList(deep, optionList);
		// inspect the slotlist
    generateSlotList(deep, slotList);
  }


  protected void buildListSymbolDecl(int deep, String tomName) throws IOException {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    OptionList optionList = tomSymbol.getOption();
    SlotList slotList = tomSymbol.getSlotList();
    TomTypeList l = getSymbolDomain(tomSymbol);
    TomType type1 = getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
		// TODO: build an abstract declaration
		int argno=1;
		output.indent(deep);
		if(!l.isEmpty()) {
			output.write(getTLType(type1));
			output.writeSpace();
			output.write(name1);
			if(!l.isEmpty()) {
				output.writeOpenBrace();
				while (!l.isEmpty()) {
					output.write(getTLType(l.getHead()));
					output.writeUnderscore();
					output.write(argno);
					argno++;
					l = l.getTail() ;
					if(!l.isEmpty()) {
						output.writeComa();
					}
				}
				output.writeCloseBrace();
				output.writeSemiColon();
			}
		}
		output.writeln();
    
		// inspect the optionList
    generateOptionList(deep, optionList);
		// inspect the slotlist
    generateSlotList(deep, slotList);
  }

}
