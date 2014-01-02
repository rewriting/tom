/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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

import tom.engine.TomBase;
import tom.engine.tools.OutputCode;

import tom.engine.adt.tomsignature.*;
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
import tom.engine.adt.code.types.*;

import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;

public class CGenerator extends CFamilyGenerator {

  public CGenerator(OutputCode output, OptionManager optionManager,
                       SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
  }
  
  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  protected void buildExpBottom(int deep, TomType type, String moduleName) throws IOException {
    output.write(" NULL ");
  }

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" 1 ");
  }
  
  protected void buildExpFalse(int deep) throws IOException {
    output.write(" 0 ");
  }

  protected void buildNamedBlock(int deep, String blockName, InstructionList instList, String moduleName) throws IOException {
    output.writeln("{");
    generateInstructionList(deep+1,instList, moduleName);
    output.writeln("}" + blockName +  ":;");
  }

  protected void buildExitAction(int deep, TomNumberList numberList) throws IOException {
    output.writeln(deep,"goto matchlab" + TomBase.tomNumberListToString(numberList) + ";");
  }

  protected void buildSymbolDecl(int deep, String tomName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    OptionList optionList = tomSymbol.getOptions();
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
    TomTypeList l = TomBase.getSymbolDomain(tomSymbol);
    TomType type1 = TomBase.getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
    
    // inspect the optionList
    generateOptionList(deep, optionList, moduleName);
    // inspect the slotlist
    generatePairNameDeclList(deep, pairNameDeclList, moduleName);
  }


  protected void buildArraySymbolDecl(int deep, String tomName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    OptionList optionList = tomSymbol.getOptions();
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();        
    TomTypeList l = TomBase.getSymbolDomain(tomSymbol);
    TomType type1 = TomBase.getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
    
    // TODO: build an abstract declaration
    int argno=1;
    output.indent(deep);
    if(!l.isEmptyconcTomType()) {
      output.write(TomBase.getTLType(type1));
      output.writeSpace();
      output.write(name1);
      if(!l.isEmptyconcTomType()) {
        output.writeOpenBrace();
        while (!l.isEmptyconcTomType()) {
          output.write(TomBase.getTLType(l.getHeadconcTomType()));
          output.writeUnderscore();
          output.write(argno);
          argno++;
          l = l.getTailconcTomType() ;
          if(!l.isEmptyconcTomType()) {
            output.writeComa();
          }
        }
        output.writeCloseBrace();
        output.writeSemiColon();
      }
    }
    output.writeln();
    
    // inspect the optionList
    generateOptionList(deep, optionList, moduleName);
    // inspect the slotlist
    generatePairNameDeclList(deep, pairNameDeclList, moduleName);
  }


  protected void buildListSymbolDecl(int deep, String tomName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    OptionList optionList = tomSymbol.getOptions();
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
    TomTypeList l = TomBase.getSymbolDomain(tomSymbol);
    TomType type1 = TomBase.getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
    // TODO: build an abstract declaration
    int argno=1;
    output.indent(deep);
    if(!l.isEmptyconcTomType()) {
      output.write(TomBase.getTLType(type1));
      output.writeSpace();
      output.write(name1);
      if(!l.isEmptyconcTomType()) {
        output.writeOpenBrace();
        while (!l.isEmptyconcTomType()) {
          output.write(TomBase.getTLType(l.getHeadconcTomType()));
          output.writeUnderscore();
          output.write(argno);
          argno++;
          l = l.getTailconcTomType() ;
          if(!l.isEmptyconcTomType()) {
            output.writeComa();
          }
        }
        output.writeCloseBrace();
        output.writeSemiColon();
      }
    }
    output.writeln();
    
    // inspect the optionList
    generateOptionList(deep, optionList, moduleName);
    // inspect the slotlist
    generatePairNameDeclList(deep, pairNameDeclList, moduleName);
  }

  protected void buildFunctionDef(int deep, String tomName, BQTermList varList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    throw new TomRuntimeException("Function not yet supported in C");
  }

  protected String genResolveMakeCode(String funName, BQTermList argList) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveMakeCode) not yet supported in C");
  }

  protected void buildReferenceClass(int deep, String refname, RefClassTracelinkInstructionList refclassTInstructions, String  moduleName) {
    throw new TomRuntimeException("%transformation (ResolveReferenceClass) not yet supported in C");
  }

  protected String genResolveIsFsymCode(String tomName, String varname) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveIsFsym) not yet supported in C");
    //return "";
  }

  protected String genResolveGetSlotCode(String tomName, String varname, String slotName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveGetSlot) not yet supported in C");
    //return "";
  }
 
  protected void buildResolveClass(String wName, String tName, String extendsName, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveClass) not yet supported in C");
  }

  protected void buildResolveInverseLinks(int deep, String fileFrom, String fileTo, TomNameList resolveNameList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveInverseLinks) not yet supported in C");
  }

  protected void genResolveDeclMake(String prefix, String funName, TomType returnType, BQTermList argList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveMakeDecl) not yet supported in C");
  }

  protected void buildTracelink(int deep, String type, String name, Expression expr, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (Tracelink instruction) not yet supported in C");
  }
  
  protected void buildTracelinkPopulateResolve(int deep, String refClassName, TomNameList tracedLinks, BQTerm current, BQTerm link, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (TracelinkPopulateResolve instruction) not yet supported in C");
  }

  //tmp
  protected void buildResolve(int deep, BQTerm bqterm, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (Resolve2 instruction) not yet supported in C");
  }
}
