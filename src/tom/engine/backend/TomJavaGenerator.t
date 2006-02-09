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
import java.util.ArrayList;

import tom.engine.adt.tomsignature.types.Instruction;
import tom.engine.adt.tomsignature.types.Option;
import tom.engine.adt.tomsignature.types.InstructionList;
import tom.engine.adt.tomsignature.types.TomList;
import tom.engine.adt.tomsignature.types.Slot;
import tom.engine.adt.tomsignature.types.SlotList;
import tom.engine.adt.tomsignature.types.OptionList;
import tom.engine.adt.tomsignature.types.TomType;
import tom.engine.adt.tomsignature.types.TomForwardType;
import tom.engine.adt.tomsignature.types.TomTypeList;
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

  protected void buildExpBottom(int deep) throws IOException {
    output.write(" null ");
  }

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

  protected void buildClass(int deep, String tomName, TomForwardType extendsFwdType, Instruction instruction) throws IOException {
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(tomName);

    OptionList options = tomSymbol.getOption();
    Option op = options.getHead();
    TomTerm extendsTerm = op.getAstTerm();
    TomTypeList tomTypes = getSymbolDomain(tomSymbol);
    ArrayList names = new ArrayList();
    ArrayList types = new ArrayList();
    TomType type;
    //initialize arrayList with argument names
    int index = 0;
    while(!tomTypes.isEmpty()) {
	    type = tomTypes.getHead();
	    types.add(`type.getString());
      names.add(getSlotName(tomSymbol, index).getString());
	    tomTypes = tomTypes.getTail();
	    index++;
    }
    output.write(deep,"class " + tomName);
    //write extends
    if (extendsFwdType != `EmptyForward) {
      output.write(deep," extends " + extendsFwdType);
    }
    output.write(deep," {");
    int args = names.size();
    //write Declarations
    for (int i = 0 ; i < args ; i++){
	    output.write(deep, types.get(i) + " " + names.get(i) + "; ");
    }

    //write constructor
    output.write(deep, modifier + tomName + "(");
    //write constructor parameters
    for (int i = 0 ; i < args ; i++){
	    output.write(deep,types.get(i) + " " + names.get(i));
	    if (i+1<args) {//if many parameters
		    output.write(deep,", ");
	    }
    }

    //write constructor initialization
    output.write(deep,") { super(`" + extendsTerm.getNameList().getHead().getString() + "(");

    //write constructor parameters
    SlotList slots = extendsTerm.getSlots();
    Slot slot;
    while(!slots.isEmpty()) {
      slot = slots.getHead();
      output.write(deep, slot.getAppl().getNameList().getHead().getString());
      slots = slots.getTail();
      if (!slots.isEmpty()) {
        output.write(deep,",");
      }
    }
    output.write("));");

    //here index represents the parameter number
    String param;
    for (int i = 0 ; i < args ; i++) {
	    param = (String)names.get(i); 
	    output.write(deep, "this." + param + "=" + param + ";");
    }
    output.write(deep,"}");

    generateInstruction(deep,`instruction);
    output.write(deep,"}");
  }

  protected void buildFunctionDef(int deep, String tomName, TomList varList, TomType codomain, TomType throwsType, Instruction instruction) throws IOException {
    output.write(deep, modifier + " " + getTomType(`codomain) + " " + tomName + "(");
    TomTerm localVar;
    while(!varList.isEmpty()) {
      localVar = varList.getHead();
      matchBlock: {
        %match(TomTerm localVar) {
          v@Variable[astType=type2] -> {
            output.write(deep,getTomType(`type2) + " ");
            generate(deep,`v);
            break matchBlock;
          }
          v@TLVar[astType=type2] -> {
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
    output.writeln(deep,")"); 
    if (!`throwsType.isEmptyType()) {
      output.write(deep," throws " + `throwsType.getString());
    }
    output.writeln(" {");
    generateInstruction(deep,instruction);
    output.writeln(deep," }");
  }
} // class TomJavaGenerator
