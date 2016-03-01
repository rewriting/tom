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
import java.io.StringWriter;
import java.util.HashMap;

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
import tom.engine.tools.ASTFactory;
import tom.engine.exception.TomRuntimeException;
import tom.platform.OptionManager;

public abstract class GenericGenerator extends AbstractGenerator {

  public static final String GENERIC_GENERATOR_BAD_CASE = "GenericGenerator: bad case: ";

  protected HashMap<String,String> isFsymMap = new HashMap<String,String>();
  protected boolean nodeclMode;
  protected boolean inline;
  protected boolean inlineplus; // perform inlining even if no substitution has been done
  protected String modifier = ""; // the value is instantiated when creating the real backend (TomJavaBackend for instance)

  public GenericGenerator(OutputCode output, OptionManager optionManager,
                             SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);

    nodeclMode = ((Boolean)optionManager.getOptionValue("noDeclaration")).booleanValue();
    boolean cCode = ((Boolean)optionManager.getOptionValue("cCode")).booleanValue();
    boolean jCode = ((Boolean)optionManager.getOptionValue("jCode")).booleanValue();
    inline = ((Boolean)optionManager.getOptionValue("inline")).booleanValue();
    inlineplus = ((Boolean)optionManager.getOptionValue("inlineplus")).booleanValue();
    inline |= inlineplus;
    inline &= (cCode || jCode);
  }

  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  /*
   * Implementation of functions whose definition is
   * independant of the target language
   */

  protected void buildTerm(int deep, String opname, BQTermList argList, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code
    String template = getSymbolTable(moduleName).getMake(opname);
    if(instantiateTemplate(deep,template,opname,argList,moduleName) == false)
    {// && !isResolveOp(opname, moduleName)) {
      String prefix = "tom_make_";
        output.write(prefix+opname);
        output.writeOpenBrace();
        int index=0;
        while(!argList.isEmptyconcBQTerm()) {
          BQTerm bqt = argList.getHeadconcBQTerm();
          //System.out.println("bqt = " + bqt);
          boolean generatebqt = true;
          %match(bqt) {
            Composite(CompositeBQTerm(BQDefault()),_*) -> {
              TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(opname);
              //System.out.println("name = " + opname);
              //System.out.println("symbol = " + tomSymbol);
              TomName slotName = TomBase.getSlotName(tomSymbol,index);
              //System.out.println("slotname = " + slotName);
              buildExpGetDefault(deep, opname, slotName.getString(), moduleName);
              generatebqt = false;
            }
          }

          if(generatebqt) {
            generateBQTerm(deep,bqt,moduleName);
          }

          argList = argList.getTailconcBQTerm();
          if(!argList.isEmptyconcBQTerm()) {
            output.writeComa();
          }
          index++;
        }
        output.writeCloseBrace();
      
    }
  }

  //ResolveMakeDecl
  protected boolean isResolveOp(String opname, String moduleName) {
    TomSymbol symbol = getSymbolTable(moduleName).getSymbolFromName(opname);
    //System.out.println("==DEBUG== symbol=\n"+symbol);
    return true;
  }

  protected void buildSymbolDecl(int deep, String tomName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
//    System.out.println("### DEBUG GenericGenerator ###");
//    System.out.println("tomName= "+tomName+"\nmoduleName= "+moduleName+"\ntomSymbol= "+tomSymbol);
//    System.out.println("### /DEBUG GenericGenerator ###");
    OptionList optionList = tomSymbol.getOptions();
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
    // inspect the optionList
    generateOptionList(deep, optionList, moduleName);
    // inspect the slotlist
    generatePairNameDeclList(deep, pairNameDeclList, moduleName);
  }

  protected void buildExpGreaterThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code  
    generateExpression(deep,exp1,moduleName);
    output.write(" > ");
    generateExpression(deep,exp2,moduleName);
  }

  protected void buildExpGreaterOrEqualThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    deep = 0; //to avoid usless spaces inside the code
    generateExpression(deep,exp1,moduleName);
    output.write(" >= ");
    generateExpression(deep,exp2,moduleName);
  }

  protected void buildExpLessThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    deep = 0; //to avoid usless spaces inside the code
    generateExpression(deep,exp1,moduleName);
    output.write(" < ");
    generateExpression(deep,exp2,moduleName);
  }

  protected void buildExpLessOrEqualThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    deep = 0; //to avoid usless spaces inside the code
    generateExpression(deep,exp1,moduleName);
    output.write(" <= ");
    generateExpression(deep,exp2,moduleName);
  }

  protected void buildExpIsEmptyArray(int deep, TomName opNameAST, TomType type, BQTerm expIndex, BQTerm expArray, String moduleName) throws IOException {
    deep = 0; //to avoid usless spaces inside the code
    generateBQTerm(deep,expIndex,moduleName);
    output.write(" >= ");
    %match(opNameAST) {
      EmptyName() -> {
        throw new TomRuntimeException(GenericGenerator.GENERIC_GENERATOR_BAD_CASE + opNameAST);
      }
    }
    String opName = opNameAST.getString();
    String sType = TomBase.getTomType(type);
    String template = getSymbolTable(moduleName).getGetSizeArray(opName);
    if(instantiateTemplate(deep,template,opName,`concBQTerm(expArray),moduleName) == false) {
      output.write("tom_get_size_" + `opName + "_" + sType + "(");
      generateBQTerm(deep,expArray,moduleName);
      output.write(")");
    }
  }

  /*
   * given a template, computes its instance
   * write the result and returns true if a substitution has been done
   * does nothing and returns false otherwise
   */
  protected boolean instantiateTemplate(int deep, String template, String opname, BQTermList termList, String moduleName) throws IOException {
    if(inline && template != null) {
      OutputCode oldOutput=output;
      String instance = template;
      
      //System.out.println("template: " + template);
      //System.out.println("termlist = " + termList);

      int index=0;
      while(!termList.isEmptyconcBQTerm()) {
        output = new OutputCode(new StringWriter());
        BQTerm bqt = termList.getHeadconcBQTerm();
        //System.out.println("bqt = " + bqt);
        boolean generatebqt = true;
        %match(bqt) {
          Composite(CompositeBQTerm(BQDefault()),_*) -> {
            TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(opname);
            TomName slotName = TomBase.getSlotName(tomSymbol,index);
            buildExpGetDefault(deep, opname, slotName.getString(), moduleName);
            generatebqt = false;
          }
        }

        if(generatebqt) {
          generateBQTerm(deep,bqt,moduleName);
        }
        String dump = output.stringDump();
        instance = instance.replace("{"+index+"}",dump);

        termList = termList.getTailconcBQTerm();
        index++;
      }

      /*
         %match(termList) {
        concBQTerm(_*,t,_*) -> {
          output = new OutputCode(new StringWriter());
          generateBQTerm(deep,`t,moduleName);
          String dump = output.stringDump();
          instance = instance.replace("{"+index+"}",dump);
          index++;
        }
      }
      */

      //System.out.println("template: " + template);
      //System.out.println("instance: " + instance);
      output=oldOutput;
      if(inlineplus || !instance.equals(template)) {
        // inline only if a substitution has been done
        output.write(instance);
        return true;
      }
    }
    return false;
  }

  protected String instantiateTemplate(String template, String subject) {
    if(template != null) {
      return template.replace("{0}",subject);
    }
    return null;
  }

  protected String instantiateTemplate(String template, String head, String tail) {
    if(template != null) {
      return template.replace("{0}",head).replace("{1}",tail);
    }
    return null;
  }

  protected void buildExpIsSort(int deep, String type, BQTerm exp, String moduleName) throws IOException {
    deep = 0; //to avoid usless spaces inside the code  
    if(getSymbolTable(moduleName).isBuiltinType(type)) {
      generateExpression(deep,`TrueTL(),moduleName);
      return;
    }

    String template = getSymbolTable(moduleName).getIsSort(type);
    String opname="";
    if(instantiateTemplate(deep,template,opname,`concBQTerm(exp),moduleName) == false) {
      output.write("tom_is_sort_" + type + "(");
      generateBQTerm(deep,exp,moduleName);
      output.write(")");
    }
  }

  protected void buildExpIsFsym(int deep, String opname, BQTerm exp, String moduleName) throws IOException {
    deep = 0; //to avoid usless spaces inside the code  
    String template = getSymbolTable(moduleName).getIsFsym(opname);
    if(instantiateTemplate(deep,template,opname,`concBQTerm(exp),moduleName) == false) {
      String s = isFsymMap.get(opname);
      if(s == null) {
        s = "tom_is_fun_sym_" + opname + "(";
      }
      output.write(s);
      //System.out.println("generate BQTerm for '" + exp + "'");
      generateBQTerm(deep,exp,moduleName);
      output.write(")");
    }
  }

  protected void buildExpGetSlot(int deep, String opname, String slotName, BQTerm var, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code 
    String template = getSymbolTable(moduleName).getGetSlot(opname,slotName);
    if(instantiateTemplate(deep,template,opname,`concBQTerm(var),moduleName) == false) {
      //output.write("tom_get_slot_" + opname + "_" + slotName + "(");
      //generateBQTerm(deep,var);
      //output.write(")");
      output.write("tom_get_slot_");
      output.write(opname);
      output.writeUnderscore();
      output.write(slotName);
      output.writeOpenBrace();
      /*
       * add a cast to ensure correct typing when using subtypes
       */

      generateBQTerm(deep,var,moduleName);
      output.writeCloseBrace();
    }
  }

  protected void buildExpGetDefault(int deep, String opname, String slotName, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code  
    output.write("tom_get_default_");
    output.write(opname);
    output.writeUnderscore();
    output.write(slotName);
    output.writeOpenBrace();
    output.writeCloseBrace();
  }

  protected void buildExpGetHead(int deep, String opName, TomType domain, TomType codomain, BQTerm var, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code  
    String template = getSymbolTable(moduleName).getGetHead(opName);
    if(instantiateTemplate(deep,template,opName,`concBQTerm(var),moduleName) == false) {
      output.write("tom_get_head_" + opName + "_" + TomBase.getTomType(domain) + "(");
      generateBQTerm(deep,var,moduleName);
      output.write(")");
    }
  }

  protected void buildExpGetTail(int deep, String opName, TomType type, BQTerm var, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code  
    String template = getSymbolTable(moduleName).getGetTail(opName);
    if(instantiateTemplate(deep,template,opName,`concBQTerm(var),moduleName) == false) {
      output.write("tom_get_tail_" + opName + "_" + TomBase.getTomType(type) + "(");
      generateBQTerm(deep,var,moduleName);
      output.write(")");
    }
  }

  protected void buildExpIsEmptyList(int deep, String opName, TomType type, BQTerm var, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code  
    String template = getSymbolTable(moduleName).getIsEmptyList(opName);
    if(instantiateTemplate(deep,template,opName,`concBQTerm(var),moduleName) == false) {
      output.write("tom_is_empty_" + `opName + "_" + TomBase.getTomType(type) + "(");
      generateBQTerm(deep,var,moduleName);
      output.write(")");
    }
  }

  protected void buildExpGetSize(int deep, TomName opNameAST, TomType type, BQTerm var, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code  
    %match(opNameAST) {
      EmptyName() -> {
        throw new TomRuntimeException(GenericGenerator.GENERIC_GENERATOR_BAD_CASE + opNameAST);
      }
    }
    String opName = opNameAST.getString();
    String sType = TomBase.getTomType(type);
    String template = getSymbolTable(moduleName).getGetSizeArray(opName);
    if(instantiateTemplate(deep,template,opName,`concBQTerm(var),moduleName) == false) {
      output.write("tom_get_size_" + `opName + "_" + sType + "(");
      generateBQTerm(deep,var,moduleName);
      output.write(")");
    }
  }

  protected void buildExpGetSliceList(int deep, String name, BQTerm varBegin, BQTerm varEnd, BQTerm tail, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code  
    output.write("tom_get_slice_" + name + "(");
    generateBQTerm(deep,varBegin,moduleName);
    output.write(",");
    generateBQTerm(deep,varEnd,moduleName);
    output.write(",");
    generateBQTerm(deep,tail,moduleName);
    output.write(")");
  }

  protected void buildExpGetSliceArray(int deep, String name, BQTerm varArray, BQTerm varBegin, BQTerm expEnd, String moduleName) throws IOException {
	deep = 0; //to avoid usless spaces inside the code  
    output.write("tom_get_slice_" + name + "(");
    generateBQTerm(deep,varArray,moduleName);
    output.write(",");
    generateBQTerm(deep,varBegin,moduleName);
    output.write(",");
    generateBQTerm(deep,expEnd,moduleName);
    output.write(")");
  }

  protected void buildAddOne(int deep, BQTerm var, String moduleName) throws IOException {
    generateBQTerm(deep,var,moduleName);
    output.write(" + 1");
  }

  /*
   * generate the function declaration when no substitution has been done
   */
  protected void buildIsFsymDecl(int deep, String tomName, String varname,
                                 TargetLanguageType tlType, Expression code, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();
    boolean inlined = inlineplus; //false;
    if(code.isCode()) {
      // perform the instantiation
      String ocode = code.getCode();
      String ncode = ocode.replace("{0}",varname);
      if(!ncode.equals(ocode)) {
        inlined = true;
        code = code.setCode(ncode);
      }
    }
    if(!inline || !code.isCode() || !inlined) {
      TomType returnType = getSymbolTable(moduleName).getBooleanType();
      // [02/12/2010 pem] precise type is no longer necessary
      // [28/01/2011 tavaresc] we need precise types for those methods
      // automatically generated for mappings (e.g. tom_is_fun_sym_toto) when
      // using builtin types (e.g. boolean)

      String argType = TomBase.getTLCode(tlType);
      genDeclInstr(TomBase.getTLType(returnType), "tom_is_fun_sym", opname,
          new String[] { argType, varname }, `Return(ExpressionToBQTerm(code)),deep,moduleName);
    }

  }


  //TODO
  protected void buildResolveIsFsymDecl(int deep, String tomName, String varname,
                                 TargetLanguageType tlType, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();

    //language specific
    Expression code = `Code(genResolveIsFsymCode(tomName, varname));

    if(!inline || !code.isCode()) {
      TomType returnType = getSymbolTable(moduleName).getBooleanType();
      String argType;
      argType = TomBase.getTLCode(tlType);

      genDeclInstr(TomBase.getTLType(returnType), "tom_is_fun_sym", opname,
          new String[] { argType, varname }, `Return(ExpressionToBQTerm(code)),deep,moduleName);
    }

  }
  //

  protected void buildGetSlotDecl(int deep, String tomName, String varname,
      TargetLanguageType tlType, Expression code, TomName slotName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();
    TomTypeList typesList = tomSymbol.getTypesToType().getDomain();

    /*
     * the code is inserted in the map during the parsing to allow
     * the use before the definition
     * the following code may be obsolete
     */
    boolean inlined = inlineplus;
    if(code.isCode()) {
      // perform the instantiation
      String ocode = code.getCode();
      String ncode = ocode.replace("{0}",varname);
      if(!ncode.equals(ocode)) {
        inlined = true;
        code = code.setCode(ncode);
      }
    }

    if(!inline || !code.isCode() || !inlined) {
      int slotIndex = TomBase.getSlotIndex(tomSymbol,slotName);
      TomTypeList l = typesList;
      for(int index = 0; !l.isEmptyconcTomType() && index<slotIndex ; index++) {
        l = l.getTailconcTomType();
      }
      TomType returnType = l.getHeadconcTomType();

      String argType;
      argType = TomBase.getTLCode(tlType);
      genDeclInstr(TomBase.getTLType(returnType),
          "tom_get_slot", opname  + "_" + slotName.getString(),
          new String[] { argType, varname },
          `Return(ExpressionToBQTerm(code)),deep,moduleName);
    }
  }

// <TRANSFORMATION>
  //TODO
  protected void buildResolveGetSlotDecl(int deep, String tomName, String varname,
      TargetLanguageType tlType, TomName slotName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();
    TomTypeList typesList = tomSymbol.getTypesToType().getDomain();
    
    //language specific : to change
    Expression code = `Code(genResolveGetSlotCode(tomName, varname, slotName.getString()));

    if(!inline || !code.isCode()) {
      int slotIndex = TomBase.getSlotIndex(tomSymbol,slotName);
      TomTypeList l = typesList;
      for(int index = 0; !l.isEmptyconcTomType() && index<slotIndex ; index++) {
        l = l.getTailconcTomType();
      }
      TomType returnType = l.getHeadconcTomType();

      String argType;
      argType = TomBase.getTLCode(tlType);
      genDeclInstr(TomBase.getTLType(returnType),
          "tom_get_slot", opname  + "_" + slotName.getString(),
          new String[] { argType, varname },
          `Return(ExpressionToBQTerm(code)),deep,moduleName);
    }
  }

  // </TRANSFORMATION>
  //////////////////////
  // <MASTER>
  protected void buildGetDefaultDecl(int deep, String tomName, 
      Expression code, TomName slotName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();
    TomTypeList typesList = tomSymbol.getTypesToType().getDomain();

    int slotIndex = TomBase.getSlotIndex(tomSymbol,slotName);
    TomTypeList l = typesList;
    for(int index = 0; !l.isEmptyconcTomType() && index<slotIndex ; index++) {
      l = l.getTailconcTomType();
    }
    TomType returnType = l.getHeadconcTomType();

    genDeclInstr(TomBase.getTLType(returnType),
        "tom_get_default", opname  + "_" + slotName.getString(),
        new String[] { },
        `Return(ExpressionToBQTerm(code)),deep,moduleName);
  }
  // </MASTER>

  protected void buildEqualTermDecl(int deep, String varname1, String varname2,
                                     String type1, String type2, Expression code, String moduleName) throws IOException {
    TomType argType1 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type1)) {
      argType1 = getSymbolTable(moduleName).getBuiltinType(type1);
    }
    TomType argType2 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type2)) {
      argType2 = getSymbolTable(moduleName).getBuiltinType(type2);
    }
    boolean inlined = inlineplus;
    if(code.isCode()) {
      // perform the instantiation
      String ocode = code.getCode();
      String ncode = ocode.replace("{0}",varname1).replace("{1}",varname2);
      if(!ncode.equals(ocode)) {
        inlined = true;
        code = code.setCode(ncode);
      }
    }
    if(!inline || !code.isCode() || !inlined) {
      genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_equal_term", type1,
          new String[] {
          TomBase.getTLType(argType1), varname1,
          TomBase.getTLType(argType2), varname2
          },
          `Return(ExpressionToBQTerm(code)),deep,moduleName);
    }
  }

  protected void buildIsSortDecl(int deep, String varName, String type, Expression code, String moduleName) throws IOException {
    boolean inlined = inlineplus;
    if(code.isCode()) {
      // perform the instantiation
      String ocode = code.getCode();
      String ncode = ocode.replace("{0}",varName);
      if(!ncode.equals(ocode)) {
        inlined = true;
        code = code.setCode(ncode);
      }
    }
    if(!inline || !code.isCode() || !inlined) {
      TomType argType = getUniversalType();
      if(getSymbolTable(moduleName).isBuiltinType(type)) {
        argType = getSymbolTable(moduleName).getBuiltinType(type);
      }
      genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_is_sort", type,
          new String[] { TomBase.getTLType(argType), varName },
          `Return(ExpressionToBQTerm(code)),deep,moduleName);
    }
  }

  protected void buildGetHeadDecl(int deep, TomName opNameAST, String varName, String suffix, TargetLanguageType domain, TargetLanguageType codomain, Expression code, String moduleName)
    throws IOException {
      String opname = opNameAST.getString();
      boolean inlined = inlineplus;
      if(code.isCode()) {
        // perform the instantiation
        String ocode = code.getCode();
        String ncode = ocode.replace("{0}",varName);
        if(!ncode.equals(ocode)) {
          inlined = true;
          code = code.setCode(ncode);
        }
      }
      if(!inline || !code.isCode() || !inlined) {
        String returnType = null;
        String argType = null;
        String functionName = "tom_get_head_" + opname;

        %match(opNameAST) {
          EmptyName() -> {
            returnType = TomBase.getTLCode(codomain);
            argType = TomBase.getTLCode(domain);
            throw new TomRuntimeException(GenericGenerator.GENERIC_GENERATOR_BAD_CASE + opNameAST);
          }

          Name(opName) -> {
            TomSymbol tomSymbol = getSymbolFromName(`opName);
            argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
            returnType = TomBase.getTLType(TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType());
          }
        }
        genDeclInstr(returnType, functionName, suffix,
            new String[] { argType, varName },
            `Return(ExpressionToBQTerm(code)),deep,moduleName);
      }
    }

  protected void buildGetTailDecl(int deep, TomName opNameAST, String varName, String type, TargetLanguageType tlType, Expression code, String moduleName)
        throws IOException {
          String opname = opNameAST.getString();
          boolean inlined = inlineplus;
          if(code.isCode()) {
            // perform the instantiation
            String ocode = code.getCode();
            String ncode = ocode.replace("{0}",varName);
            if(!ncode.equals(ocode)) {
              inlined = true;
              code = code.setCode(ncode);
            }
          }

          if(!inline || !code.isCode() || !inlined) {
            String returnType = null;
            String argType = null;
            String functionName = "tom_get_tail_" + opname;

            %match(opNameAST) {
              EmptyName() -> {
                returnType = TomBase.getTLCode(tlType);
                argType = returnType;
                throw new TomRuntimeException(GenericGenerator.GENERIC_GENERATOR_BAD_CASE + opNameAST);
              }

              Name(opName) -> {
                TomSymbol tomSymbol = getSymbolFromName(`opName);
                returnType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
                argType = returnType;
              }
            }

            genDeclInstr(returnType, functionName, type,
                new String[] { argType, varName },
                `Return(ExpressionToBQTerm(code)),deep,moduleName);
          }
        }

      protected void buildIsEmptyDecl(int deep, TomName opNameAST, String varName, String type,
          TargetLanguageType tlType, Expression code, String moduleName) throws IOException {
        String opname = opNameAST.getString();
        boolean inlined = inlineplus;
        if(code.isCode()) {
          // perform the instantiation
          String ocode = code.getCode();
          String ncode = ocode.replace("{0}",varName);
          if(!ncode.equals(ocode)) {
            inlined = true;
            code = code.setCode(ncode);
          }
        }
        if(!inline || !code.isCode() || !inlined) {
          String argType = null;
          String functionName = "tom_is_empty_" + opname;

          %match(opNameAST) {
            EmptyName() -> {
              argType = TomBase.getTLCode(tlType);
              throw new TomRuntimeException(GenericGenerator.GENERIC_GENERATOR_BAD_CASE + opNameAST);
            }

            Name(opName) -> {
              TomSymbol tomSymbol = getSymbolFromName(`opName);
              argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
            }
          }

          genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()),
              functionName, type,
              new String[] { argType, varName },
              `Return(ExpressionToBQTerm(code)),deep,moduleName);
        }
      }

      protected void buildGetElementDecl(int deep, TomName opNameAST, String name1, String name2, String type1, Expression code, String moduleName) throws IOException {

        String opname = opNameAST.getString();
        boolean inlined = inlineplus;
        if(code.isCode()) {
          // perform the instantiation
          String ocode = code.getCode();
          String ncode = ocode.replace("{0}",name1).replace("{1}",name2);
          if(!ncode.equals(ocode)) {
            inlined = true;
            code = code.setCode(ncode);
          }
        }
        if(!inline || !code.isCode() || !inlined) {
          String returnType = null;
          String argType = null;
          String functionName = "tom_get_element";

          %match(opNameAST) {
            EmptyName() -> {
              throw new TomRuntimeException(GenericGenerator.GENERIC_GENERATOR_BAD_CASE + opNameAST);
            }
          }

          TomSymbol tomSymbol = getSymbolFromName(opname);
          argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
          returnType = TomBase.getTLType(TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType());

          genDeclInstr(returnType,
              functionName+"_"+opname, type1,
              new String[] {
              argType, name1,
              TomBase.getTLType(getSymbolTable(moduleName).getIntType()), name2
              },
              `Return(ExpressionToBQTerm(code)),deep,moduleName);
        }
      }

      protected void buildGetSizeDecl(int deep, TomName opNameAST, String name1, String type, Expression code, String moduleName) throws IOException {
        String opname = opNameAST.getString();
        boolean inlined = inlineplus;
        if(code.isCode()) {
          // perform the instantiation
          String ocode = code.getCode();
          String ncode = ocode.replace("{0}",name1);
          if(!ncode.equals(ocode)) {
            inlined = true;
            code = code.setCode(ncode);
          }
        }
        if(!inline || !code.isCode() || !inlined) {
          String argType = null;
          String functionName = "tom_get_size";

          %match(opNameAST) {
            EmptyName() -> {
              throw new TomRuntimeException(GenericGenerator.GENERIC_GENERATOR_BAD_CASE + opNameAST);
            }

            Name(opName) -> {
              TomSymbol tomSymbol = getSymbolFromName(`opName);
              argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
            }
          }

          genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getIntType()),
              functionName+"_"+opname, type,
              new String[] { argType, name1 },
              `Return(ExpressionToBQTerm(code)),deep,moduleName);
        }
      }

      /*
       * the method implementations are here common to C, Java, C#, caml and python
       */

      protected void buildExpGetElement(int deep, TomName opNameAST, TomType domain, BQTerm varName, BQTerm varIndex, String moduleName) throws IOException {
		deep = 0; //to avoid usless spaces inside the code
        %match(opNameAST) {
          EmptyName() -> {
            throw new TomRuntimeException(GenericGenerator.GENERIC_GENERATOR_BAD_CASE + opNameAST);
          }
        }
        String opName = opNameAST.getString();
        String sType = TomBase.getTomType(domain);
        String template = getSymbolTable(moduleName).getGetElementArray(opName);
        if(instantiateTemplate(deep,template,opName,`concBQTerm(varName,varIndex),moduleName) == false) {
          output.write("tom_get_element_" + `opName + "_" + sType + "(");
          generateBQTerm(deep,varName,moduleName);
          output.write(",");
          generateBQTerm(deep,varIndex,moduleName);
          output.write(")");
        }

      }

      protected void buildListOrArray(int deep, BQTerm list, String moduleName) throws IOException {
		deep = 0; //to avoid usless spaces inside the code
        %match(list) {
          BuildEmptyList(Name(name)) -> {
            String prefix = "tom_empty_list_";
            String template = getSymbolTable(moduleName).getMakeEmptyList(`name);
            if(instantiateTemplate(deep,template,`name,`concBQTerm(),moduleName) == false) {
              output.write(prefix + `name + "()");
            }
            return;
          }

          BuildConsList(Name(name), headTerm, tailTerm) -> {
            String prefix = "tom_cons_list_";
            String template = getSymbolTable(moduleName).getMakeAddList(`name);
            if(instantiateTemplate(deep,template,`name,`concBQTerm(headTerm,tailTerm),moduleName) == false) {
              output.write(prefix + `name + "(");
              generateBQTerm(deep,`headTerm,moduleName);
              output.write(",");
              generateBQTerm(deep,`tailTerm,moduleName);
              output.write(")");
            }
            return;
          }

          BuildAppendList(Name(name), headTerm, tailTerm) -> {
            output.write("tom_append_list_" + `name + "(");
            generateBQTerm(deep,`headTerm,moduleName);
            output.write(",");
            generateBQTerm(deep,`tailTerm,moduleName);
            output.write(")");
            return;
          }

          BuildEmptyArray(Name(name),size) -> {
            String prefix = "tom_empty_array_";
            String template = getSymbolTable(moduleName).getMakeEmptyArray(`name);
            if(instantiateTemplate(deep,template,`name,`concBQTerm(size),moduleName) == false) {
              output.write(prefix + `name + "(");
              generateBQTerm(deep,`size,moduleName);
              output.write(")");
            }
            return;
          }

          BuildConsArray(Name(name), headTerm, tailTerm) -> {
            String template = getSymbolTable(moduleName).getMakeAddArray(`name);
            if(instantiateTemplate(deep,template,`name,`concBQTerm(headTerm,tailTerm),moduleName) == false) {
              String prefix = "tom_cons_array_";
              output.write(prefix + `name + "(");
              generateBQTerm(deep,`headTerm,moduleName);
              output.write(",");
              generateBQTerm(deep,`tailTerm,moduleName);
              output.write(")");
            }
            return;
          }

          BuildAppendArray(Name(name), headTerm, tailTerm) -> {
            output.write("tom_append_array_" + `name + "(");
            generateBQTerm(deep,`headTerm,moduleName);
            output.write(",");
            generateBQTerm(deep,`tailTerm,moduleName);
            output.write(")");
            return;
          }
        }
      }

      protected void buildFunctionCall(int deep, String name, BQTermList argList, String moduleName) throws IOException {
        output.write(deep, name);
        output.writeOpenBrace();
        while(!argList.isEmptyconcBQTerm()) {
          BQTerm bqt = argList.getHeadconcBQTerm();
          generateBQTerm(0,bqt,moduleName);
          argList = argList.getTailconcBQTerm();
          if(!argList.isEmptyconcBQTerm()) {
            output.writeComa();
          }
        }
        output.writeCloseBrace();
      }

      protected void genDeclArray(String name, String moduleName) throws IOException {
        if(nodeclMode) {
          return;
        }

        TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
        TomType listType = TomBase.getSymbolCodomain(tomSymbol);
        TomType eltType = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();

        String tomType = TomBase.getTomType(listType);
        String glType = TomBase.getTLType(listType);

        String utype = glType;

        String s = "";
        if(getSymbolTable(moduleName).isUsedSymbolDestructor(name)) {
  	           // add this test to avoid generating get_slice and append_array when
  	           // the constructor is not used in any matching
          s = %[
  @modifier@ @utype@ tom_get_slice_@name@(@utype@ subject, int begin, int end) {
    @glType@ result = @getMakeEmptyArray(name,"end-begin",moduleName)@;
    while(begin!=end) {
      result = @getMakeAddArray(name,getGetElementArray(name,tomType,"subject","begin",moduleName),"result",moduleName)@;
      begin++;
    }
    return result;
  }

  @modifier@ @utype@ tom_append_array_@name@(@utype@ l2, @utype@ l1) {
    int size1 = @getGetSizeArray(name,tomType,"l1",moduleName)@;
    int size2 = @getGetSizeArray(name,tomType,"l2",moduleName)@;
    int index;
    @glType@ result = @getMakeEmptyArray(name,"size1+size2",moduleName)@;
    index=size1;
    while(index >0) {
      result = @getMakeAddArray(name,getGetElementArray(name,tomType,"l1","size1-index",moduleName),"result",moduleName)@;
      index--;
    }

    index=size2;
    while(index > 0) {
      result = @getMakeAddArray(name,getGetElementArray(name,tomType,"l2","size2-index",moduleName),"result",moduleName)@;
      index--;
    }
    return result;
  }]%;
 }
        //If necessary we remove \n code depending on pretty option
        String res = ASTFactory.makeSingleLineCode(s, prettyMode);
        output.write(res);
      }


  private String getMakeEmptyArray(String name,String size,String moduleName) {
    String prefix = "tom_empty_array_";
    String template = getSymbolTable(moduleName).getMakeEmptyArray(name);
    String res = instantiateTemplate(template,size);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = prefix+name+"("+size+")";
    }
    return res;
  }

  private String getMakeAddArray(String name, String elt, String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getMakeAddArray(name);
    String res = instantiateTemplate(template,elt,subject);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = %[tom_cons_array_@name@(@elt@,@subject@)]%;
    }
    return res;
  }

  private String getGetElementArray(String name,String type,String subject,String index,String moduleName) {
    String template = getSymbolTable(moduleName).getGetElementArray(name);
    String res = instantiateTemplate(template,subject,index);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = %[tom_get_element_@name@_@type@(@subject@,@index@)]%;
    }
    return res;
  }

  private String getGetSizeArray(String name,String type,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getGetSizeArray(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = %[tom_get_size_@name@_@type@(@subject@)]%;
    }
    return res;
  }

  protected void buildSubstractOne(int deep, BQTerm var, String moduleName) throws IOException { 	 
     generateBQTerm(0,var,moduleName); 	 
     output.write(" - 1"); 	 
   } 	 
  	 
   protected void buildSubstract(int deep, BQTerm var1, BQTerm var2, String moduleName) throws IOException { 	 
     generateBQTerm(0, var1,moduleName); 	 
     output.write(" - "); 	 
     generateBQTerm(0,var2,moduleName); 	 
   }

}
