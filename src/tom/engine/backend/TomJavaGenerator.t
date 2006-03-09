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

import tom.engine.adt.tomsignature.types.*;
import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;
import tom.engine.exception.TomRuntimeException;

public class TomJavaGenerator extends TomImperativeGenerator {
   
  public TomJavaGenerator(OutputCode output, OptionManager optionManager,
                       SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
    /* Even if this field is not used here, we /must/ initialize it correctly,
     * as it is used by ImperativeGenerator */
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
   
  protected void buildNamedBlock(int deep, String blockName, InstructionList instList, String moduleName) throws IOException {
    output.writeln(blockName + ": {");
    generateInstructionList(deep+1,instList,moduleName);
    output.writeln("}");
  }

  protected void buildClass(int deep, String tomName, TomForwardType extendsFwdType, Declaration declaration, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    OptionList options = tomSymbol.getOption();
    Option op = options.getHead();
    TomTerm extendsTerm = op.getAstTerm();
    TomTypeList tomTypes = getSymbolDomain(tomSymbol);
    ArrayList names = new ArrayList();
    ArrayList types = new ArrayList();
    //initialize arrayList with argument names
    int index = 0;
    while(!tomTypes.isEmpty()) {
	    TomType type = tomTypes.getHead();
	    types.add(getTomType(type));
      names.add(getSlotName(tomSymbol, index).getString());
	    tomTypes = tomTypes.getTail();
	    index++;
    }
    output.write(deep, modifier +"class " + tomName);
    //write extends
		%match(TomForwardType extendsFwdType) {
			TLForward(code) -> { 
				output.write(deep," extends " + `code);
			}
    }
    output.write(deep," {");
    int args = names.size();
    //write Declarations
    for (int i = 0 ; i < args ; i++){
	    output.write(deep, types.get(i) + " " + names.get(i) + "; ");
    }

    //write constructor
    output.write(deep, "public " + tomName + "(");
    //write constructor parameters
    for (int i = 0 ; i < args ; i++){
	    output.write(deep,types.get(i) + " " + names.get(i));
	    if (i+1<args) {//if many parameters
		    output.write(deep,", ");
	    }
    }

    //write constructor initialization
    output.write(deep,") { super(");
    generate(deep,extendsTerm,moduleName);
    output.write(deep,");");
    
    //here index represents the parameter number
    for (int i = 0 ; i < args ; i++) {
	    String param = (String)names.get(i); 
	    output.write(deep, "this." + param + "=" + param + ";");
    }
    output.write(deep,"}");

    generateDeclaration(deep,`declaration,moduleName);
    output.write(deep,"}");
  }

  protected void buildFunctionDef(int deep, String tomName, TomList varList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    output.write(deep,"public " + getTLType(`codomain) + " " + tomName + "(");
    while(!varList.isEmpty()) {
      TomTerm localVar = varList.getHead();
      matchBlock: {
        %match(TomTerm localVar) {
          v@Variable[astType=type2] -> {
            output.write(deep,getTLType(`type2) + " ");
            generate(deep,`v,moduleName);
            break matchBlock;
          }
          v@TLVar[astType=type2] -> {
            output.write(deep,getTLType(`type2) + " ");
            generate(deep,`v,moduleName);
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

    %match(TomType throwsType){
      TomTypeAlone(throws) -> {
        output.write(deep," throws " + `throws);
      }
    }

    output.writeln(" {");
    generateInstruction(deep,instruction,moduleName);
    output.writeln(deep," }");
  }
	
	protected void buildCheckInstance(int deep, String typeName, TomType tlType, Expression exp, Instruction instruction, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBuiltinType(typeName)) {
			generateInstruction(deep,instruction,moduleName);
		} else {
			output.write(deep,"if("); 
			generateExpression(deep,exp,moduleName); 
			output.writeln(" instanceof " + getTLCode(tlType) + ") {");
			generateInstruction(deep+1,instruction,moduleName);
			output.writeln(deep,"}");
		}
	}
} // class TomJavaGenerator
