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

import tom.engine.adt.tomsignature.types.*;
import tom.engine.tools.OutputCode;
import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;

public class TomCGenerator extends TomImperativeGenerator {
  
  public TomCGenerator(OutputCode output, OptionManager optionManager,
                       SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
  }
  
  // ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" 1 ");
  }
  
  protected void buildExpFalse(int deep) throws IOException {
    output.write(" 0 ");
  }

  protected void buildNamedBlock(int deep, String blockName, InstructionList instList) throws IOException {
    output.writeln("{");
    generateInstructionList(deep+1,instList);
    output.writeln("}" + blockName +  ":;");
  }

  protected void buildExitAction(int deep, TomNumberList numberList) throws IOException {
    output.writeln(deep,"goto matchlab" + numberListToIdentifier(numberList) + ";");
  }

  protected void buildSymbolDecl(int deep, String tomName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(tomName);
    OptionList optionList = tomSymbol.getOption();
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
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
    generatePairNameDeclList(deep, pairNameDeclList);
  }


  protected void buildArraySymbolDecl(int deep, String tomName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(tomName);
    OptionList optionList = tomSymbol.getOption();
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();        
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
    generatePairNameDeclList(deep, pairNameDeclList);
  }


  protected void buildListSymbolDecl(int deep, String tomName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(tomName);
    OptionList optionList = tomSymbol.getOption();
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
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
    generatePairNameDeclList(deep, pairNameDeclList);
  }

  protected void buildFunctionDef(int deep, String tomName, TomList varList, TomType codomain, TomType throwsType, Instruction instruction) throws IOException {
    System.out.println("Function not yet supported in Caml");
    throw new TomRuntimeException("Function not yet supported in Caml");
  }
} // class TomCGenerator
