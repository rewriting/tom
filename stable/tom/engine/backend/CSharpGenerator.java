/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;
import tom.engine.exception.TomRuntimeException;

public class CSharpGenerator extends CFamilyGenerator {
  
  protected String stratmodifier = "";

  public CSharpGenerator(OutputCode output, OptionManager optionManager,
                       SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
    /* Even if this field is not used here, we /must/ initialize it correctly,
     * as it is used by ImperativeGenerator */
    if( ((Boolean)optionManager.getOptionValue("protected")).booleanValue() ) {
      this.stratmodifier += "protected " ;
    } else {
      this.stratmodifier += "private " ;
    }

    if(!((Boolean)optionManager.getOptionValue("noStatic")).booleanValue()) {
      this.modifier += "static " ;
      this.stratmodifier += "static " ;
    }
  }

// ------------------------------------------------------------
      
// ------------------------------------------------------------

  protected void buildExpBottom(int deep, TomType type, String moduleName) throws IOException {
    String typeName = TomBase.getTomType(type);
    if(getSymbolTable(moduleName).isIntType(typeName)
        || getSymbolTable(moduleName).isCharType(typeName)
        || getSymbolTable(moduleName).isLongType(typeName)
        || getSymbolTable(moduleName).isFloatType(typeName)
        || getSymbolTable(moduleName).isDoubleType(typeName)
        ) {
      output.write(" 0 ");
    } else if(getSymbolTable(moduleName).isBooleanType(typeName)) {
      output.write(" false ");
    } else if(getSymbolTable(moduleName).isStringType(typeName)) {
      output.write(" \"\" ");
    } else {
      output.write(" null ");
    }
  }

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" true ");
  }

  protected void buildExpFalse(int deep) throws IOException {
    output.write(" false ");
  }

  //FIXME !
  protected void buildNamedBlock(int deep, String blockName, InstructionList instList, String moduleName) throws IOException {
    output.writeln(blockName + ": {");
    generateInstructionList(deep+1,instList,moduleName);
    output.writeln("}");
  }

  protected void buildClass(int deep, String tomName, TomType extendsType, BQTerm superTerm, Declaration declaration, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    TomTypeList tomTypes = TomBase.getSymbolDomain(tomSymbol);
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> types = new ArrayList<String>();
    ArrayList<Integer> stratChild = new ArrayList<Integer>(); // child of type Strategy.

    //initialize arrayList with argument names
    int index = 0;
    while(!tomTypes.isEmptyconcTomType()) {
	    TomType type = tomTypes.getHeadconcTomType();
	    types.add(TomBase.getTLType(type));
      String name = TomBase.getSlotName(tomSymbol, index).getString();
      names.add(name);

      // test if the argument is a Strategy
      { /* unamed block */{ /* unamed block */if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( "Strategy".equals( (( tom.engine.adt.tomtype.types.TomType )type).getTomType() ) ) {

          stratChild.add(Integer.valueOf(index));
        }}}}}


	    tomTypes = tomTypes.getTailconcTomType();
	    index++;
    }
    output.write(deep, modifier + "class " + tomName);
    //write extends
		{ /* unamed block */{ /* unamed block */if ( (extendsType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )extendsType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) (( tom.engine.adt.tomtype.types.TomType )extendsType).getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {

				output.write(deep," : " +  (( tom.engine.adt.tomtype.types.TomType )extendsType).getTomType() );
			}}}}}

    output.write(deep," {");
    int args = names.size();
    //write Declarations
    for (int i = 0 ; i < args ; i++) {
	    output.write(deep, "private " + types.get(i) + " " + names.get(i) + "; ");
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
    output.write(deep,") : base(");
    generateBQTerm(deep,superTerm,moduleName);
    output.write(deep,") {");

    //here index represents the parameter number
    for (int i = 0 ; i < args ; i++) {
	    String param = names.get(i);
	    output.write(deep, "this." + param + "=" + param + ";");
    }
    output.write(deep,"}");

    // write getters
    for (int i = 0 ; i < args ; i++) {
      output.write(deep, "public " + types.get(i) + " get" + names.get(i) + "() { return " + names.get(i) + ";}");
    }

    // write getChildCount (= 1 + stratChildCount because of the %strategy `extends' which is the first child)
    int stratChildCount = stratChild.size();

    output.write(deep, "override public tom.library.sl.Visitable[] getChildren() {");
    output.write(deep, "tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];");
    output.write(deep, "for (int i = 0; i < getChildCount(); i++) {");
    output.write(deep, "stratChildren[i]=getChildAt(i);}");
    //for (int i = 0; i < stratChildCount; i++) {
    //  int j = (stratChild.get(i)).intValue();
    //  output.write(deep, "stratChildren[" + i + "] = get" + names.get(j) + "();");
    //}
    output.write(deep, "return stratChildren;}");

    output.write(deep, "override public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {");
    output.write(deep, "for (int i = 0; i < getChildCount(); i++) {");
    output.write(deep, "setChildAt(i,children[i]);}");
    //for (int i = 0; i < stratChildCount; i++) {
    //  int j = (stratChild.get(i)).intValue();
    //  output.write(deep, names.get(j) + " = (" + types.get(j) + ") children[" + i + "];");
    //}
    output.write(deep, "return this;}");

    output.write(deep, "override public int getChildCount() { return " + (stratChildCount + 1) + "; }");

    // write getChildAt
    output.write(deep, "override public tom.library.sl.Visitable getChildAt(int index) {");
    output.write(deep, "switch (index) {");
    output.write(deep, "case 0: return base.getChildAt(0);");
    for (int i = 0; i < stratChildCount; i++) {
      int j = (stratChild.get(i)).intValue();
      output.write(deep, "case " + (i+1) + ": return get" + names.get(j) + "();");
    }
    output.write(deep, "default: throw new IndexOutOfRangeException();");
    output.write(deep, "}}");

    // write setChildAt
    output.write(deep, "override public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {");
    output.write(deep, "switch (index) {");
    output.write(deep, "case 0: return base.setChildAt(0, child);");
    for (int i = 0; i < stratChildCount; i++) {
      int j = (stratChild.get(i)).intValue();
      output.write(deep, "case " + (i+1) + ": " + names.get(j) + " = (" + types.get(j) + ")child; return this;");
    }
    output.write(deep, "default: throw new IndexOutOfRangeException();");
    output.write(deep, "}}");

    output.write(deep, "override ");
    generateDeclaration(deep,declaration,moduleName);
    output.write(deep,"}");
  }

  protected void buildFunctionDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,this.modifier);
  }

  protected void buildMethodDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,"public ");
  }

  private void buildMethod(int deep, String tomName, BQTermList varList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName, String methodModifier) throws IOException {
    output.write(deep, methodModifier + TomBase.getTLType(codomain) + " " + tomName + "(");
    while(!varList.isEmptyconcBQTerm()) {
      BQTerm localVar = varList.getHeadconcBQTerm();
      matchBlock: {
        { /* unamed block */{ /* unamed block */if ( (localVar instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )localVar) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

            output.write(deep,TomBase.getTLType( (( tom.engine.adt.code.types.BQTerm )localVar).getAstType() ) + " ");
            generateBQTerm(deep,(( tom.engine.adt.code.types.BQTerm )localVar),moduleName);
            break matchBlock;
          }}}{ /* unamed block */if ( (localVar instanceof tom.engine.adt.code.types.BQTerm) ) {

            System.out.println("MakeFunction: strange term: " + localVar);
            throw new TomRuntimeException("MakeFunction: strange term: " + localVar);
          }}}

      }
      varList = varList.getTailconcBQTerm();
      if(!varList.isEmptyconcBQTerm()) {
        output.write(deep,", ");

      }
    }
    output.writeln(deep,")");

    output.writeln(" {");
    generateInstruction(deep,instruction,moduleName);
    output.writeln(deep," }");
  }
	
  protected String genResolveMakeCode(String funName, BQTermList argList) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveMakeCode) not yet supported in CSharp");
  }

  protected void buildReferenceClass(int deep, String refname, RefClassTracelinkInstructionList refclassTInstructions, String  moduleName) {
    throw new TomRuntimeException("%transformation (ResolveReferenceClass) not yet supported in CSharp");
  }

  protected String genResolveIsFsymCode(String tomName, String varname) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveIsFsym) not yet supported in CSharp");
    //return "";
  }

  protected String genResolveGetSlotCode(String tomName, String varname, String slotName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveGetSlot) not yet supported in CSharp");
    //return "";
  }
  
  protected void buildResolveClass(String wName, String tName, String extendsName, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveClass) not yet supported in CSharp");
  }

  protected void buildResolveInverseLinks(int deep, String fileFrom, String fileTo, TomNameList resolveNameList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveInverseLinks) not yet supported in CSharp");
  }

  protected void genResolveDeclMake(String prefix, String funName, TomType returnType, BQTermList argList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (ResolveMakeDecl) not yet supported in CSharp");
  }

  protected void buildTracelink(int deep, String type, String name, Expression expr, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (Tracelink instruction) not yet supported in CSharp");
  }

  protected void buildTracelinkPopulateResolve(int deep, String refClassName, TomNameList tracedLinks, BQTerm current, BQTerm link, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (TracelinkPopulateResolve instruction) not yet supported in CSharp");
  }

  //tmp
  protected void buildResolve(int deep, BQTerm bqterm, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation (Resolve2 instruction) not yet supported in CSharp");
  }
}
