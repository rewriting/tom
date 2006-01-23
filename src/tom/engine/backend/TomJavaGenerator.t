/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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

import tom.engine.adt.tomsignature.types.Instruction;
import tom.engine.adt.tomsignature.types.InstructionList;
import tom.engine.adt.tomsignature.types.TomList;
import tom.engine.adt.tomsignature.types.TomType;
import tom.engine.adt.tomsignature.types.TomTerm;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;
import tom.engine.exception.TomRuntimeException;

public class TomJavaGenerator extends TomImperativeGenerator {
   
  public TomJavaGenerator(OutputCode output, OptionManager optionManager,
                       SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
    if( ((Boolean)optionManager.getOptionValue("protected")).booleanValue() ) {
      this.modifier += "protected " ;
    } else {
      this.modifier += "private " ;
    }
  }

// ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
// ------------------------------------------------------------

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" true ");
  }
  
  protected void buildExpFalse(int deep) throws IOException {
    output.write(" false ");
  }
   
  protected void buildNamedBlock(int deep, String blockName, InstructionList instList) throws IOException {
    output.writeln(blockName + ": {");
    generateInstructionList(deep+1,instList);
    output.writeln("}");
  }

  protected void buildClass(int deep, String tomName, TomList varList, TomType extendsType, Instruction instruction) throws IOException {
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(tomName);
    String name = tomSymbol.getAstName().getString();
    output.write(deep,"class " + name + "(");
    TomTerm localVar;
    while(!varList.isEmpty()) {
      localVar = varList.getHead();
      matchBlock: {
        %match(TomTerm localVar) {
          v@Variable[astType=type2] -> {
            output.write(deep,getTLType(`type2) + " ");
            generate(deep,`v);
            break matchBlock;
          }
          _ -> {
            System.out.println("MakeFunction: strange term: " + localVar);
            throw new TomRuntimeException("MakeFunction: strange term: " + localVar);
          }
        }
      }
      varList = varList.getTail();
      if(!varList.isEmpty()) {
        output.write(deep,", ");
        
      }
    }
    output.write(deep,") {");
    generateInstruction(deep,`instruction);
    output.write(deep,"}");
  }
} // class TomJavaGenerator
