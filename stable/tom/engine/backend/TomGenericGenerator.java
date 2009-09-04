/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2009, INRIA
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

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.engine.exception.TomRuntimeException;
import tom.platform.OptionManager;

public abstract class TomGenericGenerator extends TomAbstractGenerator {

  protected HashMap<String,String> isFsymMap = new HashMap<String,String>();
  protected boolean lazyMode;
  protected boolean nodeclMode;
  protected boolean inline;
  protected boolean inlineplus; // perform inlining even if no substitution has been done
  protected String modifier = ""; // the value is instantiated when creating the real backend (TomJavaBackend for instance)

  public TomGenericGenerator(OutputCode output, OptionManager optionManager,
                             SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);

    lazyMode = ((Boolean)optionManager.getOptionValue("lazyType")).booleanValue();
    nodeclMode = ((Boolean)optionManager.getOptionValue("noDeclaration")).booleanValue();
    boolean cCode = ((Boolean)optionManager.getOptionValue("cCode")).booleanValue();
    boolean jCode = ((Boolean)optionManager.getOptionValue("jCode")).booleanValue();
    inline = ((Boolean)optionManager.getOptionValue("inline")).booleanValue();
    inlineplus = ((Boolean)optionManager.getOptionValue("inlineplus")).booleanValue();
    inline |= inlineplus;
    inline &= (cCode || jCode);
  }

  // ------------------------------------------------------------
        private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }    
  // ------------------------------------------------------------

  /*
   * Implementation of functions whose definition is
   * independant of the target language
   */

  protected void buildTerm(int deep, String opname, TomList argList, String moduleName) throws IOException {
    String prefix = "tom_make_";
    String template = getSymbolTable(moduleName).getMake(opname);
    if(instantiateTemplate(deep,template,argList,moduleName) == false) {
      buildFunctionCall(deep, prefix+opname, argList, moduleName);
    }
  }

  protected void buildSymbolDecl(int deep, String tomName, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    OptionList optionList = tomSymbol.getOption();
    PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
    // inspect the optionList
    generateOptionList(deep, optionList, moduleName);
    // inspect the slotlist
    generatePairNameDeclList(deep, pairNameDeclList, moduleName);
  }

  protected void buildExpGreaterThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    generateExpression(deep,exp1,moduleName);
    output.write(" > ");
    generateExpression(deep,exp2,moduleName);
  }

  protected void buildExpGreaterOrEqualThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    generateExpression(deep,exp1,moduleName);
    output.write(" >= ");
    generateExpression(deep,exp2,moduleName);
  }

  protected void buildExpLessThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    generateExpression(deep,exp1,moduleName);
    output.write(" < ");
    generateExpression(deep,exp2,moduleName);
  }

  protected void buildExpLessOrEqualThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    generateExpression(deep,exp1,moduleName);
    output.write(" <= ");
    generateExpression(deep,exp2,moduleName);
  }

  protected void buildExpIsEmptyArray(int deep, TomName opNameAST, TomType type, TomTerm expIndex, TomTerm expArray, String moduleName) throws IOException {
    generate(deep,expIndex,moduleName);
    output.write(" >= ");
    {{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

        throw new TomRuntimeException("TomGenericGenerator: bad case: " + opNameAST);
      }}}}

    String opName = opNameAST.getString();
    String sType = TomBase.getTomType(type);
    String template = getSymbolTable(moduleName).getGetSizeArray(opName);
    if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(expArray, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ,moduleName) == false) {
      output.write("tom_get_size_" + opName+ "_" + sType + "(");
      generate(deep,expArray,moduleName);
      output.write(")");
    }
  }

  /*
   * given a template, computes its instance
   * write the result and returns true if a substitution has been done
   * does nothing and returns false otherwise
   */
  protected boolean instantiateTemplate(int deep, String template, TomList termList, String moduleName) throws IOException {
    if(inline && template != null) {
      OutputCode oldOutput=output;
      String instance = template;
      int index = 0;
      {{if ( (termList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )termList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )termList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch2NameNumber_end_4=(( tom.engine.adt.tomterm.types.TomList )termList);do {{if (!( tomMatch2NameNumber_end_4.isEmptyconcTomTerm() )) {

          output = new OutputCode(new StringWriter());
          generate(deep, tomMatch2NameNumber_end_4.getHeadconcTomTerm() ,moduleName);
          String dump = output.stringDump();
          instance = instance.replace("{"+index+"}",dump);
          index++;
        }if ( tomMatch2NameNumber_end_4.isEmptyconcTomTerm() ) {tomMatch2NameNumber_end_4=(( tom.engine.adt.tomterm.types.TomList )termList);} else {tomMatch2NameNumber_end_4= tomMatch2NameNumber_end_4.getTailconcTomTerm() ;}}} while(!( (tomMatch2NameNumber_end_4==(( tom.engine.adt.tomterm.types.TomList )termList)) ));}}}}

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
      //System.out.println("head: " + head);
      //System.out.println("tail: " + tail);
      return template.replace("{0}",head).replace("{1}",tail);
    }
    return null;
  }

  protected void buildExpIsSort(int deep, String type, TomTerm exp, String moduleName) throws IOException {
    if(getSymbolTable(moduleName).isBuiltinType(type)) {
      generateExpression(deep, tom.engine.adt.tomexpression.types.expression.TrueTL.make() ,moduleName);
      return;
    }

    String template = getSymbolTable(moduleName).getIsSort(type);
    if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(exp, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ,moduleName) == false) {
      output.write("tom_is_sort_" + type + "(");
      generate(deep,exp,moduleName);
      output.write(")");
    }
  }

  protected void buildExpIsFsym(int deep, String opname, TomTerm exp, String moduleName) throws IOException {
    String template = getSymbolTable(moduleName).getIsFsym(opname);
    if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(exp, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ,moduleName) == false) {
      String s = isFsymMap.get(opname);
      if(s == null) {
        s = "tom_is_fun_sym_" + opname + "(";
        isFsymMap.put(opname,s);
      }
      output.write(s);
      generate(deep,exp,moduleName);
      output.write(")");
    }
  }

  protected void buildExpGetSlot(int deep, String opname, String slotName, TomTerm var, String moduleName) throws IOException {
    String template = getSymbolTable(moduleName).getGetSlot(opname,slotName);
    if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(var, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ,moduleName) == false) {
      //output.write("tom_get_slot_" + opname + "_" + slotName + "(");
      //generate(deep,var);
      //output.write(")");
      output.write("tom_get_slot_");
      output.write(opname);
      output.writeUnderscore();
      output.write(slotName);
      output.writeOpenBrace();
      generate(deep,var,moduleName);
      output.writeCloseBrace();
    }
  }

  protected void buildExpGetHead(int deep, String opName, TomType domain, TomType codomain, TomTerm var, String moduleName) throws IOException {
    //System.out.println("ExpGetHead: " + opName);
    String template = getSymbolTable(moduleName).getGetHead(opName);
    //System.out.println("template = " + template);
    if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(var, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ,moduleName) == false) {
      output.write("tom_get_head_" + opName + "_" + TomBase.getTomType(domain) + "(");
      generate(deep,var,moduleName);
      output.write(")");
    }
  }

  protected void buildExpGetTail(int deep, String opName, TomType type, TomTerm var, String moduleName) throws IOException {
    String template = getSymbolTable(moduleName).getGetTail(opName);
    if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(var, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ,moduleName) == false) {
      output.write("tom_get_tail_" + opName + "_" + TomBase.getTomType(type) + "(");
      generate(deep,var,moduleName);
      output.write(")");
    }
  }

  protected void buildExpIsEmptyList(int deep, String opName, TomType type, TomTerm var, String moduleName) throws IOException {
    String template = getSymbolTable(moduleName).getIsEmptyList(opName);
    if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(var, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ,moduleName) == false) {
      output.write("tom_is_empty_" + opName+ "_" + TomBase.getTomType(type) + "(");
      generate(deep,var,moduleName);
      output.write(")");
    }
  }

  protected void buildExpGetSize(int deep, TomName opNameAST, TomType type, TomTerm var, String moduleName) throws IOException {
    {{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

        throw new TomRuntimeException("TomGenericGenerator: bad case: " + opNameAST);
      }}}}

    String opName = opNameAST.getString();
    String sType = TomBase.getTomType(type);
    String template = getSymbolTable(moduleName).getGetSizeArray(opName);
    if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(var, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ,moduleName) == false) {
      output.write("tom_get_size_" + opName+ "_" + sType + "(");
      generate(deep,var,moduleName);
      output.write(")");
    }
  }

  protected void buildExpGetSliceList(int deep, String name, TomTerm varBegin, TomTerm varEnd, TomTerm tail, String moduleName) throws IOException {
    output.write("tom_get_slice_" + name + "(");
    generate(deep,varBegin,moduleName);
    output.write(",");
    generate(deep,varEnd,moduleName);
    output.write(",");
    generate(deep,tail,moduleName);
    output.write(")");
  }

  protected void buildExpGetSliceArray(int deep, String name, TomTerm varArray, TomTerm varBegin, TomTerm expEnd, String moduleName) throws IOException {
    output.write("tom_get_slice_" + name + "(");
    generate(deep,varArray,moduleName);
    output.write(",");
    generate(deep,varBegin,moduleName);
    output.write(",");
    generate(deep,expEnd,moduleName);
    output.write(")");
  }

  protected void buildAddOne(int deep, TomTerm var, String moduleName) throws IOException {
    generate(deep,var,moduleName);
    output.write(" + 1");
  }

  protected void buildGetFunctionSymbolDecl(int deep, String type, String name,
                                            TomType tlType, TargetLanguage tlCode, String moduleName) throws IOException {
    String args[];
    if(lazyMode) {
      TomType argType = getUniversalType();
      if(getSymbolTable(moduleName).isBuiltinType(type)) {
        argType = getSymbolTable(moduleName).getBuiltinType(type);
      }
      args = new String[] { TomBase.getTLType(argType), name };
    } else {
      args = new String[] { TomBase.getTLCode(tlType), name };
    }

    TomType returnType = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type)) {
      returnType = getSymbolTable(moduleName).getBuiltinType(type);
    }
    genDecl(TomBase.getTLType(returnType),"tom_get_fun_sym", type,args,tlCode, moduleName);
  }

  protected void buildGetImplementationDecl(int deep, String type, String typename,
                                            TomType tlType, Instruction instr, String moduleName) throws IOException {
    String argType;
    if(!lazyMode) {
      argType = TomBase.getTLCode(tlType);
    } else {
      argType = TomBase.getTLType(getUniversalType());
    }
    String returnType = argType;

    genDeclInstr(returnType,
            "tom_get_implementation", type,
            new String[] { argType, typename },
            instr,deep,moduleName);
  }

  /*
   * generate the function declaration when no substituion has been done
   */
  protected void buildIsFsymDecl(int deep, String tomName, String varname,
                                 TomType tlType, Expression code, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    String opname = tomSymbol.getAstName().getString();
    boolean inlined = inlineplus; //false;
    if(code.isCode()) {
      // perform the instantiation
      String ocode = code.getCode();
      String ncode = ocode.replace("{0}",varname);
      if(!ncode.equals(ocode)) {
        inlined = true;
        //getSymbolTable(moduleName).putIsFsym(opname,ocode);
        code = code.setCode(ncode);
      }
    }
    if(!inline || !code.isCode() || !inlined) {
      TomType returnType = getSymbolTable(moduleName).getBooleanType();
      String argType;
      if(!lazyMode) {
        argType = TomBase.getTLCode(tlType);
      } else {
        argType = TomBase.getTLType(getUniversalType());
      }

      genDeclInstr(TomBase.getTLType(returnType), "tom_is_fun_sym", opname,
          new String[] { argType, varname },  tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make(code) ) ,deep,moduleName);
    }

  }

  protected void buildGetSlotDecl(int deep, String tomName, String varname,
      TomType tlType, Expression code, TomName slotName, String moduleName) throws IOException {
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
        //getSymbolTable(moduleName).putGetSlot(opname,slotName.getString(),ocode);
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
      if(!lazyMode) {
        argType = TomBase.getTLCode(tlType);
      } else {
        argType = TomBase.getTLType(getUniversalType());
      }
      genDeclInstr(TomBase.getTLType(returnType),
          "tom_get_slot", opname  + "_" + slotName.getString(),
          new String[] { argType, varname },
           tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make(code) ) ,deep,moduleName);
    }
  }

  protected void buildCompareFunctionSymbolDecl(int deep, String name1, String name2,
                                                 String type1, String type2, TargetLanguage tlCode, String moduleName) throws IOException {
    TomType argType1 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type1)) {
      argType1 = getSymbolTable(moduleName).getBuiltinType(type1);
    }
    TomType argType2 = getUniversalType();
    if(getSymbolTable(moduleName).isBuiltinType(type2)) {
      argType2 = getSymbolTable(moduleName).getBuiltinType(type2);
    }

    genDecl(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_cmp_fun_sym", type1,
            new String[] {
              TomBase.getTLType(argType1), name1,
              TomBase.getTLType(argType2), name2
            },
            tlCode, moduleName);
  }

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
        getSymbolTable(moduleName).putEqualTerm(type1,ocode);
        code = code.setCode(ncode);
      }
    }
    if(!inline || !code.isCode() || !inlined) {
      genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()), "tom_equal_term", type1,
          new String[] {
          TomBase.getTLType(argType1), varname1,
          TomBase.getTLType(argType2), varname2
          },
           tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make(code) ) ,deep,moduleName);
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
        getSymbolTable(moduleName).putIsSort(type,ocode);
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
           tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make(code) ) ,deep,moduleName);
    }
  }

  protected void buildGetHeadDecl(int deep, TomName opNameAST, String varName, String suffix, TomType domain, TomType codomain, Expression code, String moduleName)
    throws IOException {
      String opname = opNameAST.getString();
      boolean inlined = inlineplus;
      if(code.isCode()) {
        // perform the instantiation
        String ocode = code.getCode();
        String ncode = ocode.replace("{0}",varName);
        if(!ncode.equals(ocode)) {
          inlined = true;
          /*
           * WARNING
           * this is too late to put some information in the SymbolTable
           * GetHead may have been used before this declaration
           * a possible bug is that GetHead is not inlined
           * but the following code does not generate the declaration of GetHead
           */
          getSymbolTable(moduleName).putGetHead(opname,ocode);
          code = code.setCode(ncode);
          //System.out.println("code = " + code);
        }
      }
      if(!inline || !code.isCode() || !inlined) {
        String returnType = null;
        String argType = null;
        String functionName = "tom_get_head_" + opname;

        if(lazyMode) {
          returnType = TomBase.getTLType(getUniversalType());
          argType = TomBase.getTLType(getUniversalType());
        } else {
          {{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

              returnType = TomBase.getTLCode(codomain);
              argType = TomBase.getTLCode(domain);
              throw new TomRuntimeException("TomGenericGenerator: bad case: " + opNameAST);
            }}}{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


              TomSymbol tomSymbol = getSymbolFromName( (( tom.engine.adt.tomname.types.TomName )opNameAST).getString() );
              argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
              returnType = TomBase.getTLType(TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType());
            }}}}

        }
        //System.out.println("gen = " + code);
        genDeclInstr(returnType, functionName, suffix,
            new String[] { argType, varName },
             tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make(code) ) ,deep,moduleName);
      }
    }

  protected void buildGetTailDecl(int deep, TomName opNameAST, String varName, String type, TomType tlType, Expression code, String moduleName)
        throws IOException {
          String opname = opNameAST.getString();
          boolean inlined = inlineplus;
          if(code.isCode()) {
            // perform the instantiation
            String ocode = code.getCode();
            String ncode = ocode.replace("{0}",varName);
            if(!ncode.equals(ocode)) {
              inlined = true;
              getSymbolTable(moduleName).putGetTail(opname,ocode);
              code = code.setCode(ncode);
            }
          }

          if(!inline || !code.isCode() || !inlined) {
            String returnType = null;
            String argType = null;
            String functionName = "tom_get_tail_" + opname;

            if(lazyMode) {
              returnType = TomBase.getTLType(getUniversalType());
              argType = TomBase.getTLType(getUniversalType());
            } else {
              {{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

                  returnType = TomBase.getTLCode(tlType);
                  argType = returnType;
                  throw new TomRuntimeException("TomGenericGenerator: bad case: " + opNameAST);
                }}}{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


                  TomSymbol tomSymbol = getSymbolFromName( (( tom.engine.adt.tomname.types.TomName )opNameAST).getString() );
                  returnType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
                  argType = returnType;
                }}}}

            }

            genDeclInstr(returnType, functionName, type,
                new String[] { argType, varName },
                 tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make(code) ) ,deep,moduleName);
          }
        }

      protected void buildIsEmptyDecl(int deep, TomName opNameAST, String varName, String type,
          TomType tlType, Expression code, String moduleName) throws IOException {
        String opname = opNameAST.getString();
        boolean inlined = inlineplus;
        if(code.isCode()) {
          // perform the instantiation
          String ocode = code.getCode();
          String ncode = ocode.replace("{0}",varName);
          if(!ncode.equals(ocode)) {
            inlined = true;
            getSymbolTable(moduleName).putIsEmptyList(opname,ocode);
            code = code.setCode(ncode);
          }
        }
        if(!inline || !code.isCode() || !inlined) {
          String argType = null;
          String functionName = "tom_is_empty_" + opname;

          if(lazyMode) {
            argType = TomBase.getTLType(getUniversalType());
          } else {
            {{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

                argType = TomBase.getTLCode(tlType);
                throw new TomRuntimeException("TomGenericGenerator: bad case: " + opNameAST);
              }}}{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


                TomSymbol tomSymbol = getSymbolFromName( (( tom.engine.adt.tomname.types.TomName )opNameAST).getString() );
                argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
              }}}}

          }

          genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getBooleanType()),
              functionName, type,
              new String[] { argType, varName },
               tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make(code) ) ,deep,moduleName);
        }
      }

      protected void buildGetElementDecl(int deep, TomName opNameAST, String name1, String name2, String type1, TomType domain, Expression code, String moduleName) throws IOException {

        String opname = opNameAST.getString();
        boolean inlined = inlineplus;
        if(code.isCode()) {
          // perform the instantiation
          String ocode = code.getCode();
          String ncode = ocode.replace("{0}",name1).replace("{1}",name2);
          if(!ncode.equals(ocode)) {
            inlined = true;
            getSymbolTable(moduleName).putGetElementArray(opname,ocode);
            code = code.setCode(ncode);
          }
        }
        if(!inline || !code.isCode() || !inlined) {
          String returnType = null;
          String argType = null;
          String functionName = "tom_get_element";

          if(lazyMode) {
            returnType = TomBase.getTLType(getUniversalType());
            argType = TomBase.getTLType(getUniversalType());
          } else {
            {{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

                throw new TomRuntimeException("TomGenericGenerator: bad case: " + opNameAST);
              }}}}


            TomSymbol tomSymbol = getSymbolFromName(opname);
            argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
            returnType = TomBase.getTLType(TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType());
          }

          genDeclInstr(returnType,
              functionName+"_"+opname, type1,
              new String[] {
              argType, name1,
              TomBase.getTLType(getSymbolTable(moduleName).getIntType()), name2
              },
               tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make(code) ) ,deep,moduleName);
        }
      }

      protected void buildGetSizeDecl(int deep, TomName opNameAST, String name1, String type, TomType domain, Expression code, String moduleName) throws IOException {
        String opname = opNameAST.getString();
        boolean inlined = inlineplus;
        if(code.isCode()) {
          // perform the instantiation
          String ocode = code.getCode();
          String ncode = ocode.replace("{0}",name1);
          if(!ncode.equals(ocode)) {
            inlined = true;
            getSymbolTable(moduleName).putGetSizeArray(opname,ocode);
            code = code.setCode(ncode);
          }
        }
        if(!inline || !code.isCode() || !inlined) {
          String argType = null;
          String functionName = "tom_get_size";

          if(lazyMode) {
            argType = TomBase.getTLType(getUniversalType());
          } else {
            {{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

                throw new TomRuntimeException("TomGenericGenerator: bad case: " + opNameAST);
              }}}{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


                TomSymbol tomSymbol = getSymbolFromName( (( tom.engine.adt.tomname.types.TomName )opNameAST).getString() );
                argType = TomBase.getTLType(TomBase.getSymbolCodomain(tomSymbol));
              }}}}

          }

          genDeclInstr(TomBase.getTLType(getSymbolTable(moduleName).getIntType()),
              functionName+"_"+opname, type,
              new String[] { argType, name1 },
               tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make(code) ) ,deep,moduleName);
        }
      }

      /*
       * the method implementations are here common to C, Java, C#, caml and python
       */

      protected void buildExpGetElement(int deep, TomName opNameAST, TomType domain, TomType codomain, TomTerm varName, TomTerm varIndex, String moduleName) throws IOException {
        {{if ( (opNameAST instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opNameAST) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

            throw new TomRuntimeException("TomGenericGenerator: bad case: " + opNameAST);
          }}}}

        String opName = opNameAST.getString();
        String sType = TomBase.getTomType(domain);
        String template = getSymbolTable(moduleName).getGetElementArray(opName);
        if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(varName, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(varIndex, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ) ,moduleName) == false) {
          output.write("tom_get_element_" + opName+ "_" + sType + "(");
          generate(deep,varName,moduleName);
          output.write(",");
          generate(deep,varIndex,moduleName);
          output.write(")");
        }

      }

      protected void buildListOrArray(int deep, TomTerm list, String moduleName) throws IOException {
        {{if ( (list instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )list) instanceof tom.engine.adt.tomterm.types.tomterm.BuildEmptyList) ) { tom.engine.adt.tomname.types.TomName  tomMatch10NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )list).getAstName() ;if ( (tomMatch10NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch10NameNumber_freshVar_1.getString() ;

            String prefix = "tom_empty_list_";
            String template = getSymbolTable(moduleName).getMakeEmptyList(tom_name);
            if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ,moduleName) == false) {
              output.write(prefix + tom_name+ "()");
            }
            return;
          }}}}{if ( (list instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )list) instanceof tom.engine.adt.tomterm.types.tomterm.BuildConsList) ) { tom.engine.adt.tomname.types.TomName  tomMatch10NameNumber_freshVar_6= (( tom.engine.adt.tomterm.types.TomTerm )list).getAstName() ;if ( (tomMatch10NameNumber_freshVar_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch10NameNumber_freshVar_6.getString() ; tom.engine.adt.tomterm.types.TomTerm  tom_headTerm= (( tom.engine.adt.tomterm.types.TomTerm )list).getHeadTerm() ; tom.engine.adt.tomterm.types.TomTerm  tom_tailTerm= (( tom.engine.adt.tomterm.types.TomTerm )list).getTailTerm() ;


            String prefix = "tom_cons_list_";
            String template = getSymbolTable(moduleName).getMakeAddList(tom_name);
            if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_headTerm, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_tailTerm, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ) ,moduleName) == false) {
              output.write(prefix + tom_name+ "(");
              generate(deep,tom_headTerm,moduleName);
              output.write(",");
              generate(deep,tom_tailTerm,moduleName);
              output.write(")");
            }
            return;
          }}}}{if ( (list instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )list) instanceof tom.engine.adt.tomterm.types.tomterm.BuildAppendList) ) { tom.engine.adt.tomname.types.TomName  tomMatch10NameNumber_freshVar_13= (( tom.engine.adt.tomterm.types.TomTerm )list).getAstName() ;if ( (tomMatch10NameNumber_freshVar_13 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


            output.write("tom_append_list_" +  tomMatch10NameNumber_freshVar_13.getString() + "(");
            generate(deep, (( tom.engine.adt.tomterm.types.TomTerm )list).getHeadTerm() ,moduleName);
            output.write(",");
            generate(deep, (( tom.engine.adt.tomterm.types.TomTerm )list).getTailTerm() ,moduleName);
            output.write(")");
            return;
          }}}}{if ( (list instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )list) instanceof tom.engine.adt.tomterm.types.tomterm.BuildEmptyArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch10NameNumber_freshVar_20= (( tom.engine.adt.tomterm.types.TomTerm )list).getAstName() ;if ( (tomMatch10NameNumber_freshVar_20 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch10NameNumber_freshVar_20.getString() ; tom.engine.adt.tomterm.types.TomTerm  tom_size= (( tom.engine.adt.tomterm.types.TomTerm )list).getSize() ;


            String prefix = "tom_empty_array_";
            String template = getSymbolTable(moduleName).getMakeEmptyArray(tom_name);
            if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_size, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ,moduleName) == false) {
              output.write(prefix + tom_name+ "(");
              generate(deep,tom_size,moduleName);
              output.write(")");
            }
            return;
          }}}}{if ( (list instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )list) instanceof tom.engine.adt.tomterm.types.tomterm.BuildConsArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch10NameNumber_freshVar_26= (( tom.engine.adt.tomterm.types.TomTerm )list).getAstName() ;if ( (tomMatch10NameNumber_freshVar_26 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch10NameNumber_freshVar_26.getString() ; tom.engine.adt.tomterm.types.TomTerm  tom_headTerm= (( tom.engine.adt.tomterm.types.TomTerm )list).getHeadTerm() ; tom.engine.adt.tomterm.types.TomTerm  tom_tailTerm= (( tom.engine.adt.tomterm.types.TomTerm )list).getTailTerm() ;


            String template = getSymbolTable(moduleName).getMakeAddArray(tom_name);
            if(instantiateTemplate(deep,template, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_headTerm, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_tailTerm, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) ) ,moduleName) == false) {
              String prefix = "tom_cons_array_";
              output.write(prefix + tom_name+ "(");
              generate(deep,tom_headTerm,moduleName);
              output.write(",");
              generate(deep,tom_tailTerm,moduleName);
              output.write(")");
            }
            return;
          }}}}{if ( (list instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )list) instanceof tom.engine.adt.tomterm.types.tomterm.BuildAppendArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch10NameNumber_freshVar_33= (( tom.engine.adt.tomterm.types.TomTerm )list).getAstName() ;if ( (tomMatch10NameNumber_freshVar_33 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


            output.write("tom_append_array_" +  tomMatch10NameNumber_freshVar_33.getString() + "(");
            generate(deep, (( tom.engine.adt.tomterm.types.TomTerm )list).getHeadTerm() ,moduleName);
            output.write(",");
            generate(deep, (( tom.engine.adt.tomterm.types.TomTerm )list).getTailTerm() ,moduleName);
            output.write(")");
            return;
          }}}}}

      }

      protected void buildFunctionCall(int deep, String name, TomList argList, String moduleName) throws IOException {
        output.write(name);
        output.writeOpenBrace();
        while(!argList.isEmptyconcTomTerm()) {
          generate(deep,argList.getHeadconcTomTerm(),moduleName);
          argList = argList.getTailconcTomTerm();
          if(!argList.isEmptyconcTomTerm()) {
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
        if(lazyMode) {
          utype =  TomBase.getTLType(getUniversalType());
        }

        String s = "";
        if(getSymbolTable(moduleName).isUsedSymbolDestructor(name)) {
          // add this test to avoid generating get_slice and append_array when
          // the constructor is not used in any matching
        s = "\n  "+modifier+" "+utype+" tom_get_slice_"+name+"("+utype+" subject, int begin, int end) {\n    "+glType+" result = "+getMakeEmptyArray(name,"end-begin",moduleName)+";\n    while(begin!=end) {\n      result = "+getMakeAddArray(name,getGetElementArray(name,tomType,"subject","begin",moduleName),"result",moduleName)+";\n      begin++;\n    }\n    return result;\n  }\n\n  "+modifier+" "+utype+" tom_append_array_"+name+"("+utype+" l2, "+utype+" l1) {\n    int size1 = "+getGetSizeArray(name,tomType,"l1",moduleName)+";\n    int size2 = "+getGetSizeArray(name,tomType,"l2",moduleName)+";\n    int index;\n    "+glType+" result = "+getMakeEmptyArray(name,"size1+size2",moduleName)+";\n    index=size1;\n    while(index >0) {\n      result = "+getMakeAddArray(name,getGetElementArray(name,tomType,"l1","size1-index",moduleName),"result",moduleName)+";\n      index--;\n    }\n\n    index=size2;\n    while(index > 0) {\n      result = "+getMakeAddArray(name,getGetElementArray(name,tomType,"l2","size2-index",moduleName),"result",moduleName)+";\n      index--;\n    }\n    return result;\n  }"


























;
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
      res = "tom_cons_array_"+name+"("+elt+","+subject+")";
    }
    return res;
  }

  private String getGetElementArray(String name,String type,String subject,String index,String moduleName) {
    String template = getSymbolTable(moduleName).getGetElementArray(name);
    String res = instantiateTemplate(template,subject,index);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = "tom_get_element_"+name+"_"+type+"("+subject+","+index+")";
    }
    return res;
  }

  private String getGetSizeArray(String name,String type,String subject,String moduleName) {
    String template = getSymbolTable(moduleName).getGetSizeArray(name);
    String res = instantiateTemplate(template,subject);
    if(res==null || (!inlineplus && res.equals(template))) {
      res = "tom_get_size_"+name+"_"+type+"("+subject+")";
    }
    return res;
  }

  protected void buildSubstractOne(int deep, TomTerm var, String moduleName) throws IOException { 	 
     generate(deep,var,moduleName); 	 
     output.write(" - 1"); 	 
   } 	 
  	 
   protected void buildSubstract(int deep, TomTerm var1, TomTerm var2, String moduleName) throws IOException { 	 
     generate(deep,var1,moduleName); 	 
     output.write(" - "); 	 
     generate(deep,var2,moduleName); 	 
   }


}
