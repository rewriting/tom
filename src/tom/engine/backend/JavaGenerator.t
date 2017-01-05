/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

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
import tom.engine.transformer.TransformerPlugin;

import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.BasicEList;

public class JavaGenerator extends CFamilyGenerator {

  /* modifier associated to classes generated by %strategy */
  protected String stratmodifier = "";

  public JavaGenerator(OutputCode output, OptionManager optionManager,
                       SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
    /* Even if this field is not used here, we /must/ initialize it correctly,
     * as it is used by CFamilyGenerator */
    this.modifier += "private " ;
    if( ((Boolean)optionManager.getOptionValue("protected")).booleanValue() ) {
      this.stratmodifier += "protected " ;
    } else {
      this.stratmodifier += "public " ; // public by default to make constructor of strategies reusable
    }

    if(!((Boolean)optionManager.getOptionValue("noStatic")).booleanValue()) {
      this.modifier += "static " ;
      this.stratmodifier += "static " ;
    }
  }

// ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  %include{ emf/ecore.tom }
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
  
  protected void buildIntrospectorClass(int deep, String tomName, Declaration declaration, String moduleName) throws IOException {
    output.write("public static class " + tomName+ " implements tom.library.sl.Introspector {");
    generateDeclaration(deep,`declaration,moduleName);
    output.write(deep,"}");
  }

  protected String genResolveMakeCode(String funName,
                                      BQTermList argList) throws IOException {
    String args = "";
    while(!argList.isEmptyconcBQTerm()) {
      BQTerm arg = argList.getHeadconcBQTerm();
matchBlock: {
              %match(arg) {
                BQVariable[AstName=Name(name)] -> {
                  args = args+`name;
                  break matchBlock;
                }

                _ -> {
                  System.out.println("genResolveMakeCode: strange term: " + arg);
                  throw new TomRuntimeException("genResolveMakeCode: strange term: " + arg);
                }
              }
            }
            argList = argList.getTailconcBQTerm();
            if(!argList.isEmptyconcBQTerm()) {
              args = args + ", ";
            }
    }
    return "new "+funName+"("+args+")";
  }

  protected void genResolveDeclMake(String prefix, String funName, TomType
      returnType, BQTermList argList, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }
    Instruction instr = `ExpressionToInstruction(Code(genResolveMakeCode(funName, argList)));
    if(!inline) {
      StringBuilder s = new StringBuilder();
      s.append(modifier + TomBase.getTLType(returnType) + " " + prefix + funName + "(");
      while(!argList.isEmptyconcBQTerm()) {
        BQTerm arg = argList.getHeadconcBQTerm();
matchBlock: {
              %match(arg) {
                BQVariable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]] -> {
                  s.append(TomBase.getTLCode(`tlType) + " " + `name);
                  break matchBlock;
                }

                _ -> {
                  System.out.println("genResolveDeclMake: strange term: " + arg);
                  throw new TomRuntimeException("genResolveDeclMake: strange term: " + arg);
                }
              }
            }
            argList = argList.getTailconcBQTerm();
            if(!argList.isEmptyconcBQTerm()) {
              s.append(", ");
            }
      }
      s.append(") { ");
      output.writeln(s);
      output.write("return ");
      generateInstruction(0,instr,moduleName);
      output.writeln(";");
      output.writeln("}");
    }
  }

  //TODO: to move in JavaGenerator
  /*protected String genResolveIsSortCode(String resolveStringName,
                                        String varName) throws IOException {
    return " "+varName+" instanceof "+resolveStringName+" ";
  }*/

  protected String genResolveIsFsymCode(String resolveStringName,
                                        String varName) throws IOException {
    return " ( "+varName+" instanceof "+resolveStringName+" ) ";
  }

  protected String genResolveGetSlotCode(String tomName,
                                         String varName,
                                         String slotName) throws IOException {
    return " (("+tomName+")"+varName+")."+slotName+" ";
  }

  protected String getFullQualifiedNameFromTypeName(String name, String moduleName) {
    String result = null;
    TomType type = getSymbolTable(moduleName).getType(name);
    return getFullQualifiedNameFromType(type);
  }

  protected String getFullQualifiedNameFromType(TomType type) {
    String result = null;
    %match(type) {
      Type[TlType=TLType[String=s]] -> { return result = `s; }
    }
    throw new RuntimeException("Should not be there: full qualified name of "+type+" is null");
  }

  //TODO: retrieve the information about the FQN of wName and extends
  protected void buildResolveClass(String wName, String tName, String extendsName, String moduleName) throws
    IOException {
      String resolveStringName = "Resolve"+wName+tName;
      //String fqnwName = wName;
      //TomType type = getSymbolTable(moduleName).getType(wName);
      //fqnwName = getFullQualifiedNameFromType(type);
      String fqnwName = getFullQualifiedNameFromTypeName(wName, moduleName);
      //TODO change this part of code generation: get attributes names from
      //mappings? ; we currently use predefined strings (in TransformerPlugin)
      output.write(%[private static class @resolveStringName@ extends @extendsName@ {
  public String @TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_NAME@;
  public @fqnwName@ @TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_O@;

  public @resolveStringName@(@fqnwName@ o, String name) {
    this.@TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_NAME@ = name;
    this.@TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_O@ = o;
  }
}
]%);
    }

  //TODO: add attributes, get/set + EObject get(String)
  //protected void buildReferenceClass(int deep, String refname,
  //InstructionList instructions, String moduleName)a
  protected void buildReferenceClass(int deep, String refname, RefClassTracelinkInstructionList refclassTInstructions, String moduleName)
  throws IOException {
    output.write(%[
public static class @refname@ implements tom.library.utils.ReferenceClass {
  ]%);
    //RuleInstruction(TypeName:String,Term:TomTerm,Action:InstructionList,Options:OptionList)
    //ReferenceClass(RefName:TomName,Fields:InstructionList)
    //Tracelink(Type:TomName,Name:TomName,ElementaryTransfoName:TomName,Expr:Expression,OrgTrack:Option)//BQTerm, then blocklist
    String getfunctionbody = "";
    %match(refclassTInstructions) {
      concRefClassTracelinkInstruction(_*,RefClassTracelinkInstruction[Type=Name(type),Name=Name(name)],_*) -> {
      output.write(%[
  private @`type@ @`name@;
  public @`type@ get@`name@() { return @`name@; }
  public void set@`name@(@`type@ value) { this.@`name@ = value; }
]%);
      getfunctionbody = getfunctionbody+"if(name.equals(\""+`name+"\")) {\n        return get"+`name+"();\n    } else ";
      }
    }

    output.write(%[
  public Object get(String name) {
    @getfunctionbody@ {
      throw new RuntimeException("This field does not exist:" + name);
    }
  }

}

]%);

  }

  protected void buildTracelink(int deep, String type, String name, Expression expr, String moduleName) throws IOException {
   output.write(type+" "+name+" = ");
   generateExpression(deep,expr,moduleName);
   output.writeln(";");
  }

  //TODO: update this procedure (tom__linkClass)
  protected void buildTracelinkPopulateResolve(int deep, String refClassName, TomNameList tracedLinks, BQTerm current, BQTerm link, String moduleName) throws IOException {
    String refVar = "var_"+refClassName.toLowerCase();
    //create the ReferenceClass instance
    output.write(refClassName+" "+refVar+" = new "+refClassName+"();");
    //populate the newly created ReferenceClass by setting attributes
    for(TomName name : tracedLinks.getCollectionconcTomName()){
      String namestr = name.getString();
      output.write(refVar+".set"+namestr+"("+namestr+");");
    }
    //save the ReferenceClass into  the LinkClass
    //TODO: change this to be less specific (and add tom__linkClass:LinkClass
    //as first parameter of all strategies)
    //LinkClass class could be in the Tom library
    generateBQTerm(deep, link, moduleName);
    output.write(".put(");
    generateBQTerm(deep, current, moduleName);
    output.write(","+refVar+");");
  }

  protected void buildResolve(int deep, BQTerm bqterm, String moduleName) throws IOException {
    generateBQTerm(deep, bqterm, moduleName);
  }

  //TODO: parameters are problematic
  //TODO: specific to EMf
  protected void buildResolveInverseLinks(int deep, String fileFrom, String fileTo, TomNameList resolveNameList, String moduleName) throws IOException {
    //List<String> resolveList = getResolveNameList(resolveNameList);
    //String toSet = genToSetFromResolveNameList(resolveNameList);

    //result/accumulator to change ("acc")

    //TODO:
    //Work in progress: optimization of resolve phase. trying to hook the
    //EMF adapter by adding custom data structure or custom functions
    output.write(%[
  public static void resolveInverseLinks(EObject resolveNode, EObject newNode, EObject acc) {
    //Work in progress: optimization of generated resolve phase
    //trying to hook the EMF adapter
    ECrossReferenceAdapter adapter = new ECrossReferenceAdapter();
    //MyECrossReferenceAdapter adapter = new tom.library.utils.MyECrossReferenceAdapter();

    acc.eAdapters().add(adapter);
    Collection<EStructuralFeature.Setting> references = adapter.getInverseReferences(resolveNode);

        boolean toSet = (false
        @genToSetFromResolveNameList(resolveNameList)@
        );

    for (EStructuralFeature.Setting setting:references) {
      EObject current = setting.getEObject();
]%);
    genTargetElementBlock(fileTo,moduleName);
    output.write(%[
    }
  }
]%);
  }

  //TODO: find another way to generate these blocks. It would be better to
  //avoid EMF dependancies
  //protected void genTargetElementBlock(String fileTo) throws IOException  {
  protected void genTargetElementBlock(String fileTo, String moduleName) throws IOException  {
    XMIResourceImpl resource = new XMIResourceImpl();
    Map opts = new HashMap();
    opts.put(XMIResource.OPTION_SCHEMA_LOCATION, java.lang.Boolean.TRUE);
    File input = new File(fileTo);
    try {
      resource.load(new FileInputStream(input),opts);//new HashMap());
    } catch (Exception e) {
      e.printStackTrace();
    }

    //org.eclipse.emf.ecore.impl.EPackageImpl
    EPackage tmp = (EPackage)resource.getContents().get(0);
    EList<EPackage> epkgs = tmp.getESubpackages();
    if(epkgs.size()<0) {
      epkgs = getEPackages(epkgs);
    }
    epkgs.add(tmp);
    EList<EClassifier> ecls = new BasicEList<EClassifier>();
    //retrieve all EClassifiers in the MM
    for(EPackage epk : epkgs) {
      ecls.addAll(epk.getEClassifiers());
    }
    //generate code for each EClassifier which is an EClass
    for(EClassifier ecl : ecls) {
      if(ecl instanceof EClass ) {//EEnum should not produce a block
        if(!((EClass)ecl).isAbstract()) {
          //String eclName = ecl.getName();
          String eclFQName = getFullQualifiedNameFromTypeName(ecl.getName(),moduleName);
          output.write(%[if (current instanceof @eclFQName@) {
        @eclFQName@ newCurrent = (@eclFQName@)current;
      ]%);

          EList<EStructuralFeature> sfs = ((EClass)ecl).getEAllStructuralFeatures();
          int sfCounter = 0; //to avoid to gen blocks with only an exception
          for(EStructuralFeature sf : sfs) {//many and builtin should not produce a block
            if(sf.isChangeable() && !sf.isMany() && !isPrimitiveEMFType(sf) && !isEEnumType(sf)) {
              if(sfCounter>0) {
                output.write(%[ else ]%);
              }
              output.write(%[if(newCurrent.get@firstToUpperCase(sf.getName())@().equals(resolveNode) && toSet) {
          newCurrent.set@firstToUpperCase(sf.getName())@((@getTypeFromEStructuralFeature(sf)@)newNode); 
        } ]%);
              sfCounter++;
            }
          }
          if(sfCounter>0) {
            output.write(%[else { throw new RuntimeException("should not be there"); }]%);
          }
          output.write(%[} else ]%);
        }
      }
    }
    output.write(%[ { throw new RuntimeException("should not be there"); }]%);
  }

  //TODO: change that!
  private final static Set<String> primitiveEMFTypes = new HashSet<String>();
  static {
    primitiveEMFTypes.add("EInt");
    primitiveEMFTypes.add("EBoolean");
    primitiveEMFTypes.add("EString");
    primitiveEMFTypes.add("EDouble");
    primitiveEMFTypes.add("EDate");
    primitiveEMFTypes.add("BigInteger");
    primitiveEMFTypes.add("BigDecimal");
    primitiveEMFTypes.add("Date");
    primitiveEMFTypes.add("java.lang.String");
    primitiveEMFTypes.add("byte");
    primitiveEMFTypes.add("double");
    primitiveEMFTypes.add("int");
    primitiveEMFTypes.add("float");
    primitiveEMFTypes.add("double");
    primitiveEMFTypes.add("boolean");
  }
  protected boolean isPrimitiveEMFType(EStructuralFeature sf) {
    return (primitiveEMFTypes.contains(sf.getName()) || primitiveEMFTypes.contains(sf.getEType().getInstanceClassName()));
  }

  protected boolean isEEnumType(EStructuralFeature sf) {
    return (sf.getEType() instanceof EEnum);
  }

  protected String getTypeFromEStructuralFeature(EStructuralFeature sf) {
    String result = sf.getEType().getInstanceClassName();
    return ((result==null)?sf.getEType().getName():result);
  }

  protected String firstToUpperCase(String s) {
    if(s.length()>1) {
      return (s.substring(0,1).toUpperCase()+s.substring(1,s.length()));
    } else {
      return s.toUpperCase();
    }
  }

  protected EList<org.eclipse.emf.ecore.EPackage> getEPackages(EList<org.eclipse.emf.ecore.EPackage> epkgs) {
    for(EPackage epkg : epkgs ) {
      EList<EPackage> sub = epkg.getESubpackages();
      if(sub.size()>0) {
        epkgs.addAll(getEPackages(sub));
      }
    }
    return epkgs;
  }


  protected String genToSetFromResolveNameList(TomNameList resolveNameList) {
    //List<String> result = new LinkedList<String>();
    String result = "";
    for(TomName tname : resolveNameList.getCollectionconcTomName()) {
      result = result+" | resolveNode instanceof "+tname.getString();
    }
    return result;
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
      %match(type) {
        Type[TomType="Strategy"] -> {
          stratChild.add(Integer.valueOf(index));
        }
      }

	    tomTypes = tomTypes.getTailconcTomType();
	    index++;
    }
    output.write(deep, stratmodifier + "class " + tomName);
    //write extends
matchblock: {
              %match(extendsType) {
			Type[TlType=TLType(code)] -> {
				output.write(deep," extends " + `code);
        break matchblock;
			}

			Type[TomType=code,TlType=EmptyTargetLanguageType()] -> {
				output.write(deep," extends " + `code);
        break matchblock;
			}
    }
            }
    output.writeln(deep," {");
    int args = names.size();
    //write Declarations
    for(int i = 0 ; i < args ; i++) {
      output.writeln(deep, "private " + types.get(i) + " " + names.get(i) + ";");
    }

    //write constructor
    output.write(deep, "public " + tomName + "(");
    //write constructor parameters
    for(int i = 0 ; i < args ; i++) {
	    output.write(deep,types.get(i) + " " + names.get(i));
	    if(i+1<args) {//if many parameters
		    output.write(deep,", ");
	    }
    }

    //write constructor initialization
    output.writeln(deep,") {");
    output.write(deep+1,"super(");
    generateBQTerm(deep,superTerm,moduleName);
    output.writeln(");");

    //here index represents the parameter number
    for(int i = 0 ; i < args ; i++) {
	    String param = names.get(i);
	    output.writeln(deep+1, "this." + param + "=" + param + ";");
    }
    output.writeln(deep,"}");

    // write getters
    for(int i = 0 ; i < args ; i++) {
      output.writeln(deep, "public " + types.get(i) + " get" + names.get(i) + "() {");
      output.writeln(deep+1,"return " + names.get(i) + ";");
      output.writeln(deep,"}");
    }

    // write getChildCount (= 1 + stratChildCount because of the %strategy `extends' which is the first child)
    int stratChildCount = stratChild.size();

    output.writeln(deep, "public tom.library.sl.Visitable[] getChildren() {");
    output.writeln(deep, "tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];");
    output.writeln(deep, "stratChildren[0] = super.getChildAt(0);");
    for(int i = 0; i < stratChildCount; i++) {
      int j = (stratChild.get(i)).intValue();
      output.writeln(deep, "stratChildren[" + (i+1) + "] = get" + names.get(j) + "();");
    }
    output.writeln(deep, "return stratChildren;}");

    output.writeln(deep, "public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {");
    output.writeln(deep,"super.setChildAt(0, children[0]);");
    for(int i = 0; i < stratChildCount; i++) {
      int j = (stratChild.get(i)).intValue();
      output.writeln(deep, names.get(j) + " = (" + types.get(j) + ") children[" + (i+1) + "];");
    }
    output.writeln(deep, "return this;");
    output.writeln(deep, "}");

    output.writeln(deep, "public int getChildCount() {");
    output.writeln(deep, "return " + (stratChildCount + 1) + ";");
    output.writeln(deep, "}");

    // write getChildAt
    output.writeln(deep, "public tom.library.sl.Visitable getChildAt(int index) {");
    output.writeln(deep, "switch (index) {");
    output.writeln(deep, "case 0: return super.getChildAt(0);");
    for (int i = 0; i < stratChildCount; i++) {
      int j = (stratChild.get(i)).intValue();
      output.writeln(deep, "case " + (i+1) + ": return get" + names.get(j) + "();");
    }
    output.writeln(deep, "default: throw new IndexOutOfBoundsException();");
    output.writeln(deep, "}");
    output.writeln(deep, "}");

    // write setChildAt
    output.writeln(deep, "public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {");
    output.writeln(deep, "switch (index) {");
    output.writeln(deep, "case 0: return super.setChildAt(0, child);");
    for (int i = 0; i < stratChildCount; i++) {
      int j = (stratChild.get(i)).intValue();
      output.writeln(deep, "case " + (i+1) + ": " + names.get(j) + " = (" + types.get(j) + ")child; return this;");
    }
    output.writeln(deep, "default: throw new IndexOutOfBoundsException();");
    output.writeln(deep, "}");
    output.writeln(deep, "}");

    generateDeclaration(deep,`declaration,moduleName);
    output.writeln(deep,"}");
  }

  protected void buildFunctionDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,this.modifier);
  }

  protected void buildMethodDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,throwsType,instruction,moduleName,"public ");
  }

  private void buildMethod(int deep, String tomName, BQTermList varList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName, String methodModifier) throws IOException {
    output.writeln(deep, "@SuppressWarnings(\"unchecked\")");
    output.write(deep, methodModifier + TomBase.getTLType(`codomain) + " " + tomName + "(");
    while(!varList.isEmptyconcBQTerm()) {
      BQTerm localVar = varList.getHeadconcBQTerm();
      matchBlock: {
        %match(localVar) {
          v@BQVariable[AstType=type2] -> {
            output.write(deep,TomBase.getTLType(`type2) + " ");
            generateBQTerm(deep,`v,moduleName);
            break matchBlock;
          }
          _ -> {
            System.out.println("MakeFunction: strange term: " + localVar);
            throw new TomRuntimeException("MakeFunction: strange term: " + localVar);
          }
        }
      }
      varList = varList.getTailconcBQTerm();
      if(!varList.isEmptyconcBQTerm()) {
        output.write(deep,", ");
      }
    }
    output.writeln(deep,")");
matchblock: {
    %match(throwsType) {
			Type[TlType=TLType(code)] -> {
				output.write(deep," throws " + `code);
        break matchblock;
			}

			Type[TomType=code,TlType=EmptyTargetLanguageType()] -> {
				output.write(deep," throws " + `code);
        break matchblock;
			}
    }
  }

    output.writeln(" {");
    generateInstruction(deep,instruction,moduleName);
    output.writeln(deep,"}");
  }

}
