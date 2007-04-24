/* Generated by TOM (version 2.5alpha): Do not edit this file *//*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2007, INRIA
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

import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;
import tom.engine.exception.TomRuntimeException;

public class TomJavaGenerator extends TomCFamilyGenerator {

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

    if(!((Boolean)optionManager.getOptionValue("noStatic")).booleanValue()) {
      this.modifier += "static " ;
    }
  }

// ------------------------------------------------------------
  /* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file */ private static boolean tom_equal_term_String(String t1, String t2) { return  (t1.equals(t2)) ;}private static boolean tom_is_sort_String(String t) { return  t instanceof String ;}  /* Generated by TOM (version 2.5alpha): Do not edit this file */ private static boolean tom_equal_term_TomType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomType(Object t) { return  t instanceof tom.engine.adt.tomtype.types.TomType ;}private static boolean tom_equal_term_TomForwardType(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomForwardType(Object t) { return  t instanceof tom.engine.adt.tomtype.types.TomForwardType ;}private static boolean tom_equal_term_TomName(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomName(Object t) { return  t instanceof tom.engine.adt.tomname.types.TomName ;}private static boolean tom_equal_term_TomTerm(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TomTerm(Object t) { return  t instanceof tom.engine.adt.tomterm.types.TomTerm ;}private static boolean tom_equal_term_OptionList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_OptionList(Object t) { return  t instanceof tom.engine.adt.tomoption.types.OptionList ;}private static boolean tom_equal_term_ConstraintList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_ConstraintList(Object t) { return  t instanceof tom.engine.adt.tomconstraint.types.ConstraintList ;}private static boolean tom_is_fun_sym_Type( tom.engine.adt.tomtype.types.TomType  t) { return  t instanceof tom.engine.adt.tomtype.types.tomtype.Type ;}private static  tom.engine.adt.tomtype.types.TomType  tom_get_slot_Type_TomType( tom.engine.adt.tomtype.types.TomType  t) { return  t.getTomType() ;}private static  tom.engine.adt.tomtype.types.TomType  tom_get_slot_Type_TlType( tom.engine.adt.tomtype.types.TomType  t) { return  t.getTlType() ;}private static boolean tom_is_fun_sym_TomTypeAlone( tom.engine.adt.tomtype.types.TomType  t) { return  t instanceof tom.engine.adt.tomtype.types.tomtype.TomTypeAlone ;}private static  String  tom_get_slot_TomTypeAlone_String( tom.engine.adt.tomtype.types.TomType  t) { return  t.getString() ;}private static boolean tom_is_fun_sym_ASTTomType( tom.engine.adt.tomtype.types.TomType  t) { return  t instanceof tom.engine.adt.tomtype.types.tomtype.ASTTomType ;}private static  String  tom_get_slot_ASTTomType_String( tom.engine.adt.tomtype.types.TomType  t) { return  t.getString() ;}private static boolean tom_is_fun_sym_TLForward( tom.engine.adt.tomtype.types.TomForwardType  t) { return  t instanceof tom.engine.adt.tomtype.types.tomforwardtype.TLForward ;}private static  String  tom_get_slot_TLForward_String( tom.engine.adt.tomtype.types.TomForwardType  t) { return  t.getString() ;}private static boolean tom_is_fun_sym_Variable( tom.engine.adt.tomterm.types.TomTerm  t) { return  t instanceof tom.engine.adt.tomterm.types.tomterm.Variable ;}private static  tom.engine.adt.tomoption.types.OptionList  tom_get_slot_Variable_Option( tom.engine.adt.tomterm.types.TomTerm  t) { return  t.getOption() ;}private static  tom.engine.adt.tomname.types.TomName  tom_get_slot_Variable_AstName( tom.engine.adt.tomterm.types.TomTerm  t) { return  t.getAstName() ;}private static  tom.engine.adt.tomtype.types.TomType  tom_get_slot_Variable_AstType( tom.engine.adt.tomterm.types.TomTerm  t) { return  t.getAstType() ;}private static  tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slot_Variable_Constraints( tom.engine.adt.tomterm.types.TomTerm  t) { return  t.getConstraints() ;} 
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

  protected void buildNamedBlock(int deep, String blockName, InstructionList instList, String moduleName) throws IOException {
    output.writeln(blockName + ": {");
    generateInstructionList(deep+1,instList,moduleName);
    output.writeln("}");
  }

  protected void buildClass(int deep, String tomName, TomForwardType extendsFwdType, TomTerm superTerm, Declaration declaration, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    TomTypeList tomTypes = TomBase.getSymbolDomain(tomSymbol);
    ArrayList names = new ArrayList();
    ArrayList types = new ArrayList();
    ArrayList stratChild = new ArrayList(); // child of type Strategy.

    //initialize arrayList with argument names
    int index = 0;
    while(!tomTypes.isEmptyconcTomType()) {
	    TomType type = tomTypes.getHeadconcTomType();
	    types.add(TomBase.getTLType(type));
      String name = TomBase.getSlotName(tomSymbol, index).getString();
      names.add(name);

      // test if the argument is a Strategy
      if (tom_is_sort_TomType(type)) {{  tom.engine.adt.tomtype.types.TomType  tomMatch1Position1=(( tom.engine.adt.tomtype.types.TomType )type);if ( ( tom_is_fun_sym_Type(tomMatch1Position1) ||  false  ) ) {{  tom.engine.adt.tomtype.types.TomType  tomMatch1Position1NameNumberTomType=tom_get_slot_Type_TomType(tomMatch1Position1);if ( ( tom_is_fun_sym_ASTTomType(tomMatch1Position1NameNumberTomType) ||  false  ) ) {if ( ( tom_equal_term_String("Strategy", tom_get_slot_ASTTomType_String(tomMatch1Position1NameNumberTomType)) ||  false  ) ) {if ( true ) {

          stratChild.add(new Integer(index));
        }}}}}}}


	    tomTypes = tomTypes.getTailconcTomType();
	    index++;
    }
    output.write(deep, modifier + "class " + tomName);
    //write extends
		if (tom_is_sort_TomForwardType(extendsFwdType)) {{  tom.engine.adt.tomtype.types.TomForwardType  tomMatch2Position1=(( tom.engine.adt.tomtype.types.TomForwardType )extendsFwdType);if ( ( tom_is_fun_sym_TLForward(tomMatch2Position1) ||  false  ) ) {if ( true ) {

				output.write(deep," extends " + tom_get_slot_TLForward_String(tomMatch2Position1));
			}}}}

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
    output.write(deep,") { super(");
    generate(deep,superTerm,moduleName);
    output.write(deep,");");

    //here index represents the parameter number
    for (int i = 0 ; i < args ; i++) {
	    String param = (String)names.get(i);
	    output.write(deep, "this." + param + "=" + param + ";");
    }
    output.write(deep,"}");

    // write getters
    for (int i = 0 ; i < args ; i++) {
      output.write(deep, "public " + types.get(i) + " get" + names.get(i) + "() { return " + names.get(i) + ";}");
    }

    // write getChildCount (= 1 + stratChildCount because of the %strategy `extends' which is the first child)
    int stratChildCount = stratChild.size();
    output.write(deep, "public int getChildCount() { return " + (stratChildCount + 1) + "; }");

    // write getChildAt
    output.write(deep, "public jjtraveler.Visitable getChildAt(int i) {");
    output.write(deep, "switch (i) {");
    output.write(deep, "case 0: return super.getChildAt(0);");
    for (int i = 0; i < stratChildCount; i++) {
      output.write(deep, "case " + (i+1) + ": return get" + names.get(((Integer)stratChild.get(i)).intValue()) + "();");
    }
    output.write(deep, "default: throw new IndexOutOfBoundsException();");
    output.write(deep, "}}");

    // write setChildAt
    output.write(deep, "public jjtraveler.Visitable setChildAt(int i, jjtraveler.Visitable child) {");
    output.write(deep, "switch (i) {");
    output.write(deep, "case 0: return super.setChildAt(0, child);");
    for (int i = 0; i < stratChildCount; i++) {
      int j = ((Integer)stratChild.get(i)).intValue();
      output.write(deep, "case " + (i+1) + ": " + names.get(j) + " = (" + types.get(j) + ")child; return this;");
    }
    output.write(deep, "default: throw new IndexOutOfBoundsException();");
    output.write(deep, "}}");

    generateDeclaration(deep,declaration,moduleName);
    output.write(deep,"}");
  }

  protected void buildFunctionDef(int deep, String tomName, TomList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,this.modifier);
  }

  protected void buildMethodDef(int deep, String tomName, TomList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,"public ");
  }

  private void buildMethod(int deep, String tomName, TomList varList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName, String methodModifier) throws IOException {
    output.write(deep, methodModifier + TomBase.getTLType(codomain) + " " + tomName + "(");
    while(!varList.isEmptyconcTomTerm()) {
      TomTerm localVar = varList.getHeadconcTomTerm();
      matchBlock: {
        if (tom_is_sort_TomTerm(localVar)) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch3Position1=(( tom.engine.adt.tomterm.types.TomTerm )localVar);if ( ( tom_is_fun_sym_Variable(tomMatch3Position1) ||  false  ) ) {if ( true ) {

            output.write(deep,TomBase.getTLType(tom_get_slot_Variable_AstType(tomMatch3Position1)) + " ");
            generate(deep,tomMatch3Position1,moduleName);
            break matchBlock;
          }}if ( true ) {

            System.out.println("MakeFunction: strange term: " + localVar);
            throw new TomRuntimeException("MakeFunction: strange term: " + localVar);
          }}}

      }
      varList = varList.getTailconcTomTerm();
      if(!varList.isEmptyconcTomTerm()) {
        output.write(deep,", ");

      }
    }
    output.writeln(deep,")");

    if (tom_is_sort_TomType(throwsType)) {{  tom.engine.adt.tomtype.types.TomType  tomMatch4Position1=(( tom.engine.adt.tomtype.types.TomType )throwsType);if ( ( tom_is_fun_sym_TomTypeAlone(tomMatch4Position1) ||  false  ) ) {if ( true ) {

        output.write(deep," throws " + tom_get_slot_TomTypeAlone_String(tomMatch4Position1));
      }}}}


    output.writeln(" {");
    generateInstruction(deep,instruction,moduleName);
    output.writeln(deep," }");
  }
	
}
