/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003 INRIA
			    Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.backend;
 
import aterm.*;

import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.util.HashMap;

import jtom.adt.tomsignature.types.*;

import jtom.TomBase;
import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import jtom.tools.OutputCode;
import jtom.tools.SingleLineOutputCode;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public abstract class TomAbstractGenerator extends TomBase {
  
  protected OutputCode output;
  protected TomTaskInput input;
  protected String debugKey;
  protected boolean supportedGoto = false, 
    supportedBlock = false, debugMode = false, strictType = false,
    staticFunction = false, genDecl = false, pretty = false, verbose = false;

  private HashMap getSubtermMap = new HashMap();
  private HashMap getFunSymMap = new HashMap();
  private HashMap isFsymMap = new HashMap();
  
  public TomAbstractGenerator(TomEnvironment environment, OutputCode output, TomTaskInput input) {
    this.output = output;
    this.input = input;

    supportedGoto = input.isSupportedGoto(); 
    supportedBlock = input.isSupportedBlock();
    debugMode = input.isDebugMode();
    strictType = input.isStrictType();
    staticFunction = input.isStaticFunction();
    genDecl = input.isGenDecl();
    pretty = input.isPretty();

  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------

    /*
     * Generate the goal language
     */
  
  private void generate(int deep, TomTerm subject)
    throws IOException {
      //System.out.println("Generate: " + subject);

    %match(TomTerm subject) {
      
      Tom(l) -> {
        generateList(deep,l);
        return;
      }

      TomInclude(l) -> {
        generateList(deep,l);
        return;
      }
     
      BuildVariable(Name(name)) -> {
        output.write(name);
        return;
      }

      BuildVariable(PositionName(l1)) -> {
        output.write("tom" + numberListToIdentifier(l1));
        return;
      }
  
      BuildTerm(Name(name), argList) -> {
        buildTerm(name,argList);
        return;
      }

      BuildList(Name(name), argList) -> {
        buildList(name,argList);
        return;
      }

      BuildArray(Name(name), argList) -> {
        buildArray(name,argList);
        return;
      }

      FunctionCall(Name(name), argList) -> {
        buildFunctionCall(name,argList);
        return;
      }

      Composite(argList) -> {
        generateList(deep,argList);
        return;
      }
      
      CompiledMatch(matchDeclarationList, namedBlockList, list) -> {
        buildCompiledMatch(deep, matchDeclarationList, namedBlockList, list);
        return;
      }

      CompiledPattern(instList) -> {
        generateList(deep,instList);
        return;
      }

      Variable(option1,PositionName(l1),type1) -> {
          /*
           * sans type: re-definition lorsque %variable est utilise
           * avec type: probleme en cas de filtrage dynamique
           */
        output.write("tom" + numberListToIdentifier(l1));
        return;
      }

      Variable(option1,Name(name1),type1) -> {
        output.write(name1);
        return;
      }

      VariableStar(option1,PositionName(l1),type1) -> {
        output.write("tom" + numberListToIdentifier(l1));
        return;  
      }

      VariableStar(option1,Name(name1),type1) -> {
        output.write(name1);
        return;
      }

      Declaration(var@Variable(option1,name1,
                               Type(ASTTomType(type),tlType@TLType[]))) -> {
        buildDeclaration(var, name1, type, tlType);
        return;
      }

      Declaration(var@VariableStar(option1,name1,
                                   Type(ASTTomType(type),tlType@TLType[]))) -> {
        buildDeclarationStar(var, name1, type, tlType);
        return;
      }

      MakeFunctionBegin(Name(tomName),SubjectList(varList)) -> {
        buildFunctionBegin(tomName, varList);
        return;
      }

      MakeFunctionEnd() -> {
        buildFunctionEnd();
        return;
      }

      EndLocalVariable() -> {
        output.writeln(deep,"do"); return;
      }
      
      TargetLanguageToTomTerm(t) -> {
        generateTargetLanguage(deep,t);
        return;
      }

      DeclarationToTomTerm(t) -> {
        generateDeclaration(deep,t);
        return;
      }

      ExpressionToTomTerm(t) -> {
        generateExpression(deep,t);
        return;
      }

      InstructionToTomTerm(t) -> {
        generateInstruction(deep,t);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for: " + t);
        throw new TomRuntimeException(new Throwable("Cannot generate code for: " + t));
      }
    }
  }





  public void generateExpression(int deep, Expression subject)
    throws IOException {
    if(subject==null) { return; }
    
    %match(Expression subject) {
      Not(exp) -> {
        buildExpNot(deep, exp);
        return;
      }

      And(exp1,exp2) -> {
        buildExpAnd(deep, exp1, exp2);
        return;
      }

      Or(exp1,exp2) -> {
        buildExpOr(deep, exp1, exp2);
        return;
      }

      TrueTL() -> {
        buildExpTrue();
        return;
      }
      
      FalseTL() -> {
        buildExpFalse();
        return;
      }

      IsEmptyList(var@Variable[astType=type1]) -> {
        buildExpEmptyList(deep, type1, var);
        return;
      }

      IsEmptyArray(varArray@Variable[astType=type1], varIndex@Variable[]) -> {
        buildExpEmptyArray(deep, type1, varIndex, varArray);
        return;
      }

      EqualFunctionSymbol(var@Variable[astType=type1],
                          Appl(option,(Name(tomName)),l)) -> { // needs to be checked
        buildExpEqualFunctionVarAppl(deep, var, type1, tomName);
        return;
      }
      
      EqualFunctionSymbol(var1@Variable[astType=type1],var2) -> {
          //System.out.println("EqualFunctionSymbol(...," + var2 + ")");
        buildExpEqualFunctionVarVar(deep, type1, var2);
        return;
      }

      EqualTerm(var1@Variable[astType=type1],var2) -> {
        buildExpEqualTermVar(deep, type1, var1, var2);
        return;
      }

      EqualTerm(var1@VariableStar[astType=type1],var2) -> {
        buildExpEqualTermVarStar(deep, type1, var1, var2);
        return;
      }

      IsFsym(Name(opname), var@Variable(option1,PositionName(l1),type1)) -> {
        buildExpIsFsym(deep, opname, var);
        return;
      }

      GetSubterm(var@Variable(option1,PositionName(l1),type1),Number(number)) -> {
        buildExpGetSubterm(deep, var, type1);
        return;
      }

      GetSlot(Name(opname),slotName, var@Variable[]) -> {
        buildExpGetSlot(deep, opname, slotName, var);
        return;
      }

      GetHead(var@Variable(option1,PositionName(l1),type1)) -> {
        buildExpGetHead(deep, type1, var);
        return;
      }

      GetTail(var@Variable(option1,PositionName(l1),type1)) -> {
        buildExpGetTail(deep, type1, var);
        return;
      }

      GetSize(var@Variable(option1,PositionName(l1),type1)) -> {
        buildExpGetSize(deep, type1, var);
        return;
      }

      GetElement( varName@Variable(option1,PositionName(l1),type1),
                  varIndex@Variable(option2,PositionName(l2),type2)) -> {
        buildExpGetElement(deep, type1, varName, varIndex);
        return;
      }

      GetSliceList(Name(name),
                   varBegin@Variable(option1,PositionName(l1),type1),
                   varEnd@Variable(option2,PositionName(l2),type2)) -> {
        
        buildExpGetSliceList(deep, name, varBegin, varEnd);
        return;
      }

      GetSliceArray(Name(name),
                    varArray@Variable(option1,PositionName(l1),type1),
                    varBegin@Variable(option2,PositionName(l2),type2),
                    expEnd) -> {
        buildExpGetSliceArray(deep, name, varArray, varBegin, expEnd);
        return;
      }

      TomTermToExpression(t) -> {
        generate(deep,t);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for expression: " + t);
        throw new TomRuntimeException(new Throwable("Cannot generate code for expression: " + t));
      }
    }
  }

  public void generateInstruction(int deep, Instruction subject)
    throws IOException {
    if(subject==null) { return; }
    
    %match(Instruction subject) {

      Assign(var@Variable(list,name1,
                          Type(tomType@ASTTomType(type),tlType@TLType[])),exp) -> {
        buildAssignVar(deep, var, type, tlType);
        return;
      }

      Assign(UnamedVariable[],exp) |  
        Assign(UnamedVariableStar[],exp) -> {
        return;
      }

      AssignMatchSubject(var@Variable(option1,name1,
                                      Type(tomType@ASTTomType(type),tlType@TLType[])),exp) -> {
        buildAssignMatch(deep, var, type, tlType);
        return;
      }
      
      NamedBlock(blockName,instList) -> {
        buildNamedBlock(deep, blockName, instList);
        return;
      }
      
        //IfThenElse(exp,succesList,conc()) -> {
      IfThenElse(exp,succesList,emptyTomList()) -> {
        buildIfThenElse(deep, exp,succesList);
        return;
      }

      IfThenElse(exp,succesList,failureList) -> {
        buildIfThenElseWithFailure(deep, exp, succesList, failureList);
        return;
      }

      DoWhile(succesList,exp) -> {
        buildDoWhile(deep, succesList,exp);
        return;
      }

      Assign(var@VariableStar(list,name1,
                              Type(ASTTomType(type),tlType@TLType[])),exp) -> {
        buildAssignVarExp(deep, var, tlType, exp);
        return;
      }

      Increment(var@Variable[]) -> {
        buildIncrement(deep, var);
        return;
      }

      Action(l) -> {
        generateList(deep, l);
  /*
    while(!l.isEmpty()) {
    generate(deep,l.getHead());
    l = l.getTail();
    }
      //out.writeln("// ACTION: " + l);
      */
        return;
      }

      ExitAction(numberList) -> {
        buildExitAction(deep, numberList);
        return;
      }

      Return(exp) -> {
        buildReturn(deep, exp);
        return;
      }

      OpenBlock()  -> { output.writeln(deep,"{"); return; }
      CloseBlock() -> { output.writeln(deep,"}"); return; }

      
      t -> {
        System.out.println("Cannot generate code for instruction: " + t);
        throw new TomRuntimeException(new Throwable("Cannot generate code for instruction: " + t));
      }
    }
  }

      
  
  public void generateTargetLanguage(int deep, TargetLanguage subject)
    throws IOException {
    if(subject==null) { return; }
    %match(TargetLanguage subject) {
      TL(t,TextPosition[line=startLine], TextPosition[line=endLine]) -> {
        output.write(deep,t, startLine, endLine-startLine);
        return;
      }
      
      ITL(t) -> {
        output.write(deep,t);
        return;
      }

      Comment(t) -> {
        output.write("/* ");
        output.write(deep,t);
        output.write(" */ ");
        if(pretty) {
          output.writeln();
        }
        return;
      }

      t -> {
        System.out.println("Cannot generate code for TL: " + t);
        throw new TomRuntimeException(new Throwable("Cannot generate code for TL: " + t));
      }
    }
  }

  public void generateOption(int deep, Option subject)
    throws IOException {
    if(subject==null) { return; }
    
    %match(Option subject) {
      DeclarationToOption(decl) -> {
        generateDeclaration(deep,decl);
        return;
      }
      OriginTracking[] -> { return; }
      DefinedSymbol() -> { return; }
      Constructor[] -> { return; }

      t -> {
        System.out.println("Cannot generate code for option: " + t);
        throw new TomRuntimeException(new Throwable("Cannot generate code for option: " + t));
      }
    }
  }
  
  public void generateDeclaration(int deep, Declaration subject)
    throws IOException {
    if(subject==null) { return; }
    
    %match(Declaration subject) {
      EmptyDeclaration() -> {
        return;
      }
      SymbolDecl(Name(tomName)) -> {
        buildSymbolDecl(deep, tomName);
        return ;
      }
      
      ArraySymbolDecl(Name(tomName)) -> {
        buildArraySymbolDecl(deep, tomName);
        return ;
      }

      ListSymbolDecl(Name(tomName)) -> {
        buildListSymbolDecl(deep, tomName);
        return ;
      }

      GetFunctionSymbolDecl(Variable(option,Name(name),
                                     Type(ASTTomType(type),tlType@TLType[])),
                            tlCode, _) -> {
        buildGetFunctionSymbolDecl(deep, type, name, tlType, tlCode);
        return;
      }
      
      GetSubtermDecl(Variable(option1,Name(name1),
                              Type(ASTTomType(type1),tlType1@TLType[])),
                     Variable(option2,Name(name2),
                              Type(ASTTomType(type2),tlType2@TLType[])),
                     tlCode, _) -> {
        buildGetSubtermDecl(deep, name1, name2, type1, tlType1, tlType2, tlCode);
        return;
      }
      
      IsFsymDecl(Name(tomName),
		 Variable(option1,Name(name1), Type(ASTTomType(type1),tlType@TLType[])),
                 tlCode@TL[], _) -> {
        buildIsFsymDecl(deep, tomName, name1, tlType, tlCode);
        return;
      }
 
      GetSlotDecl[astName=Name(tomName),
                  slotName=slotName,
                  variable=Variable(option1,Name(name1), Type(ASTTomType(type1),tlType@TLType[])),
                  tlCode=tlCode@TL[]] -> {
        buildGetSlotDecl(deep, tomName, name1, tlType, tlCode);
        return;
      }

      CompareFunctionSymbolDecl(Variable(option1,Name(name1), Type(ASTTomType(type1),_)),
                                Variable(option2,Name(name2), Type(ASTTomType(type2),_)),
                                tlCode, _) -> {
        buildCompareFunctionSymbolDecl(deep, name1, name2, type1, type2, tlCode);
        return;
      }

      TermsEqualDecl(Variable(option1,Name(name1), Type(ASTTomType(type1),_)),
                     Variable(option2,Name(name2), Type(ASTTomType(type2),_)),
                     tlCode, _) -> {
        buildTermsEqualDecl(deep, name1, name2, type1, type2, tlCode);
        return;
      }
      
      GetHeadDecl(Variable(option1,Name(name1), Type(ASTTomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        buildGetHeadDecl(deep, name1, type, tlType, tlCode);
        return;
      }

      GetTailDecl(Variable(option1,Name(name1), Type(ASTTomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        buildGetTailDecl(deep, name1, type, tlType, tlCode);
        return;
      }

      IsEmptyDecl(Variable(option1,Name(name1), Type(ASTTomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        buildIsEmptyDecl(deep, name1, type, tlType, tlCode);
        return;
      }

      MakeEmptyList(Name(opname), tlCode@TL[], _) -> {
        buildMakeEmptyList(deep, opname, tlCode);
        return;
      }

      MakeAddList(Name(opname),
                  Variable(option1,Name(name1), fullEltType@Type(ASTTomType(type1),tlType1@TLType[])),
                  Variable(option2,Name(name2), fullListType@Type(ASTTomType(type2),tlType2@TLType[])),
                  tlCode@TL[], _) -> {
        buildMakeAddList(deep, opname, name1, name2, tlType1, tlType2, fullEltType, fullListType, tlCode);
        return;
      }

      GetElementDecl(Variable(option1,Name(name1), Type(ASTTomType(type1),tlType1@TLType[])),
                     Variable(option2,Name(name2), Type(ASTTomType(type2),tlType2@TLType[])),
                     tlCode@TL[], _) -> {
        buildGetElementDecl(deep, name1, name2, type1, tlType1, tlCode);
        return;
      }
      
      GetSizeDecl(Variable(option1,Name(name1), Type(ASTTomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        buildGetSizeDecl(deep, name1, type, tlType, tlCode);
        return;
      }
      
      MakeEmptyArray(Name(opname),
                     Variable(option1,Name(name1), Type(ASTTomType(type1),_)),
                     tlCode@TL[], _) -> {
        buildMakeEmptyArray(deep, opname, name1, tlCode);
        return;
      }

      MakeAddArray(Name(opname),
                   Variable(option1,Name(name1), fullEltType@Type(ASTTomType(type1),tlType1@TLType[])),
                   Variable(option2,Name(name2), fullArrayType@Type(ASTTomType(type2),tlType2@TLType[])),
                   tlCode@TL[], _) -> {
        buildMakeAddArray(deep, opname, name1, name2, tlType1, tlType2, fullEltType, fullArrayType, tlCode);
        return;
      }

      MakeDecl(Name(opname), returnType, argList, tlCode@TL[], _) -> {
        generateTargetLanguage(deep, genDeclMake(opname, returnType, argList, tlCode));
        return;
      }
      
      TypeTermDecl[keywordList=declList] -> {
        buildTypeTermDecl(deep, declList);
        return;
      }

      TypeListDecl[keywordList=declList] -> { 
        buildTypeListDecl(deep, declList);
        return;
      }

      TypeArrayDecl[keywordList=declList] -> { 
        buildTypeArrayDecl(deep, declList);
        return;
      }
      
      t -> {
        System.out.println("Cannot generate code for declaration: " + t);
        throw new TomRuntimeException(new Throwable("Cannot generate code for declaration: " + t));
      }
    }
  }
  
  public void generateList(int deep, TomList subject)
    throws IOException {
    while(!subject.isEmpty()) {
      generate(deep,subject.getHead());
      subject = subject.getTail();
    }
    // if(subject.isEmpty()) {
//       return;
//     }

//     TomTerm t = subject.getHead();
//     TomList l = subject.getTail(); 
//     generate(deep,t);
//     generateList(deep,l);
  }
  
  public void generateOptionList(int deep, OptionList subject)
    throws IOException {
    while(!subject.isEmpty()) {
      generateOption(deep,subject.getHead());
      subject = subject.getTail();
    }

    // if(subject.isEmpty()) {
//       return;
//     }

//     Option t = subject.getHead();
//     OptionList l = subject.getTail(); 
//     generateOption(out,deep,t);
//     generateOptionList(out,deep,l);
  }

  public void generateSlotList(int deep, SlotList slotList)
    throws IOException {
    while ( !slotList.isEmpty() ) {
      generateDeclaration(deep, slotList.getHead().getSlotDecl());
      slotList = slotList.getTail();
    }
  }
  
    // ------------------------------------------------------------
  protected abstract TargetLanguage genDecl(String returnType,
                                            String declName,
                                            String suffix,
                                            String args[],
                                            TargetLanguage tlCode);
  
  protected abstract TargetLanguage genDeclMake(String opname, TomType returnType, 
                                            TomList argList, TargetLanguage tlCode);
  
  protected abstract TargetLanguage genDeclList(String name, TomType listType, TomType eltType);

  protected abstract TargetLanguage genDeclArray(String name, TomType listType, TomType eltType);
 
  protected abstract void buildTerm(String name, TomList argList);
  protected abstract void buildList(String name, TomList argList);
  protected abstract void buildArray(String name, TomList argList);
  protected abstract void buildFunctionCall(String name, TomList argList);

  protected void buildCompiledMatch(int deep, TomList matchDeclarationList, TomList namedBlockList, OptionList list) {
    boolean generated = hasGeneratedMatch(list);
    boolean defaultPattern = hasDefaultCase(list);
    Option orgTrack = null;
    if(supportedBlock) {
      generateInstruction(deep,`OpenBlock());
    }
    if(debugMode && !generated) {
      orgTrack = findOriginTracking(list);
      debugKey = orgTrack.getFileName().getString() + orgTrack.getLine();
      output.write("jtom.debug.TomDebugger.debugger.enteringStructure(\""+debugKey+"\");\n");
    }
    generateList(deep+1,matchDeclarationList);
    generateList(deep+1,namedBlockList);
    if(debugMode && !generated && !defaultPattern) {
      output.write("jtom.debug.TomDebugger.debugger.leavingStructure(\""+debugKey+"\");\n");
    }
    if(supportedBlock) {
      generateInstruction(deep,`CloseBlock());
    }
  }
	
  protected abstract void buildDeclaration(TomTerm var, TomName name, String type, TomType tlType);
  protected abstract void buildDeclarationStar(TomTerm var, TomName name, String type, TomType tlType);
  protected abstract void buildFunctionBegin(String tomName, TomList varList); 
  protected abstract void buildFunctionEnd();
  protected abstract void buildExpNot(int deep, Expression exp);
  
  protected void buildExpAnd(int deep, Expression exp1, Expression exp2) {
    generateExpression(deep,exp1);
    output.write(" && ");
    generateExpression(deep,exp2);
  }
  protected void buildExpOr(int deep, Expression exp1, Expression exp2) {
    generateExpression(deep,exp1);
    output.write(" || ");
    generateExpression(deep,exp2);
  }

  protected abstract void buildExpTrue();

  protected abstract void buildExpFalse();

  protected void buildExpEmptyList(int deep, TomType type1, TomTerm var) {
    output.write("tom_is_empty_" + getTomType(type1) + "(");
    generate(deep,var);
    output.write(")");
  }

  protected void buildExpEmptyArray(int deep, TomType type1, TomTerm varIndex, TomTerm varArray) {
    generate(deep,varIndex);
    output.write(" >= ");
    output.write("tom_get_size_" + getTomType(type1) + "(");
    generate(deep,varArray);
    output.write(")");
  }

  protected void buildExpEqualFunctionVarAppl(int deep, TomTerm var, TomType type1, String tomName) {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    TomName termNameAST = tomSymbol.getAstName();
    OptionList termOptionList = tomSymbol.getOption();
    
    Declaration isFsymDecl = getIsFsymDecl(termOptionList);
    if(isFsymDecl != null) {
      generateExpression(deep,`IsFsym(termNameAST,var));
    } else {
      String s = (String)getFunSymMap.get(type1);
      if(s == null) {
        s = "tom_cmp_fun_sym_" + getTomType(type1) + "(tom_get_fun_sym_" + getTomType(type1) + "(";
        getFunSymMap.put(type1,s);
      }
      output.write(s);
      generate(deep,var);
      output.write(") , " + getSymbolCode(tomSymbol) + ")");
    }
  }

  protected void buildExpEqualFunctionVarVar(int deep, TomType type1, TomTerm var2) {
    output.write("tom_cmp_fun_sym_" + getTomType(type1) + "(");
    output.write("tom_get_fun_sym_" + getTomType(type1) + "(");
    generate(deep,var1);
    output.write(") , " + var2 + ")");///??????????????????????????????
  }

  protected void buildExpEqualTermVar(int deep, TomType type1, TomTerm var1,TomTerm var2) {
    output.write("tom_terms_equal_" + getTomType(type1) + "(");
    generate(deep,var1);
    output.write(", ");
    generate(deep,var2);
    output.write(")");
  }

  protected void buildExpEqualTermVarStar(int deep, TomType type1, TomTerm var1, TomTerm var2) {
    output.write("tom_terms_equal_" + getTomType(type1) + "(");
    generate(deep,var1);
    output.write(", ");
    generate(deep,var2);
    output.write(")");
  }

  protected void buildExpIsFsym(int deep, String opname, TomTerm var) {
    String s = (String)isFsymMap.get(opname);
    if(s == null) {
      s = "tom_is_fun_sym_" + opname + "(";
      isFsymMap.put(opname,s);
    } 
    output.write(s);
    generate(deep,var);
    output.write(")");
  }

  protected void buildExpGetSubterm(int deep, TomTerm var, TomType type1) {
    String s = (String)getSubtermMap.get(type1);
    if(s == null) {
      s = "tom_get_subterm_" + getTomType(type1) + "(";
      getSubtermMap.put(type1,s);
    } 
    output.write(s);
    generate(deep,var);
    output.write(", " + number + ")");
  }

  protected void buildExpGetSlot(int deep, String opname, String slotName, TomTerm var) {
    output.write("tom_get_slot_" + opname + "_" + slotName + "(");
    generate(deep,var);
    output.write(")");
  }

  protected void buildExpGetHead(int deep, TomType type1, TomTerm var) {
    output.write("tom_get_head_" + getTomType(type1) + "(");
    generate(deep,var);
    output.write(")");
  }
  protected void buildExpGetTail(int deep, TomType type1, TomTerm var) {
    output.write("tom_get_tail_" + getTomType(type1) + "(");
    generate(deep,var); 
    output.write(")");
  }

  protected void buildExpGetSize(int deep, TomType type1, TomTerm var) {
    output.write("tom_get_size_" + getTomType(type1) + "(");
    generate(deep,var);
    output.write(")");
  }

  protected void buildExpGetElement(int deep, TomType type1, TomTerm varName, TomTerm varIndex) {
    output.write("tom_get_element_" + getTomType(type1) + "(");
    generate(deep,varName);
    output.write(",");
    generate(deep,varIndex);
    output.write(")");
  }

  protected void buildExpGetSliceList(int deep, String name, TomTerm varBegin, TomTerm varEnd) {
    output.write("tom_get_slice_" + name + "(");
    generate(deep,varBegin);
    output.write(",");
    generate(deep,varEnd);
    output.write(")");
  }

  protected void buildExpGetSliceArray(int deep, String name, TomTerm varArray, TomTerm varBegin, TomTerm expEnd) {
    output.write("tom_get_slice_" + name + "(");
    generate(deep,varArray);
    output.write(",");
    generate(deep,varBegin);
    output.write(",");
    generate(deep,expEnd);
    output.write(")");
  }

  protected abstract void buildAssignVar(int deep, TomTerm var, String type, TomType tlType);
  protected abstract void buildAssignMatch(int deep, TomTerm var, String type, TomType tlType);
  protected abstract void buildNamedBlock(int deep, String blockName, TomList instList);
  protected abstract void buildIfThenElse(int deep, Expression exp, TomList succesList);
  protected abstract void buildIfThenElseWithFailure(int deep, Expression exp, TomList succesList, TomList failureList);

  protected void buildDoWhile(int deep, TomList succesList, Expression exp) {
    output.writeln(deep,"do {");
    generateList(deep+1,succesList);
    output.write(deep,"} while(");
    generateExpression(deep,exp);
	output.writeln(");");
  }
  
  protected abstract void buildAssignVarExp(int deep, TomTerm var, TomType tlType, Expression exp);

  protected void buildIncrement(int deep, TomTerm var) {
    generate(deep,var);
    output.write(" = ");
    generate(deep,var);
    output.writeln(" + 1;");
  }

  
  protected abstract void buildExitAction(int deep, TomNumberList numberList);
  protected abstract void buildReturn(int deep, TomTerm exp);
  protected abstract void buildSymbolDecl(int deep, String tomName);
  protected abstract void buildArraySymbolDecl(int deep, String tomName);
  protected abstract void buildListSymbolDecl(int deep, String tomName);

  protected void buildGetFunctionSymbolDecl(int deep, String type, String name, TomType tlType, TargetLanguage tlCode) {
    String args[];
    if(!strictType) {
      TomType argType = getUniversalType();
      if(isIntType(type)) {
        argType = getIntType();
      } else if(isDoubleType(type)) {
        argType = getDoubleType();
      }
      args = new String[] { getTLType(argType), name };
    } else {
      args = new String[] { getTLCode(tlType), name };
    }
    
    TomType returnType = getUniversalType();
    if(isIntType(type)) {
      returnType = getIntType();
    } else if(isDoubleType(type)) {
      returnType = getDoubleType();
    }
    generateTargetLanguage(deep,
                           genDecl(getTLType(returnType),
                                   "tom_get_fun_sym", type,args,tlCode));
  }


  protected abstract void buildGetSubtermDecl(int deep, String name1, String name2, String type1, TomType tlType1, TomType tlType2, TargetLanguage tlCode) {
    String args[];
    if(strictType || eCode) {
      args = new String[] { getTLCode(tlType1), name1,
                            getTLCode(tlType2), name2 };
    } else {
          args = new String[] { getTLType(getUniversalType()), name1,
                                getTLCode(tlType2), name2 };
    }
    generateTargetLanguage(deep, genDecl(getTLType(getUniversalType()), "tom_get_subterm", type1,
                                             args, tlCode));
  }

  protected void buildIsFsymDecl(int deep, String tomName, String name1, TomType tlType, TargetLanguage tlCode) {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    String opname = tomSymbol.getAstName().getString();
    
    TomType returnType = getBoolType();
    String argType;
    if(strictType) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep, genDecl(getTLType(returnType),
                                         "tom_is_fun_sym", opname,
                                         new String[] { argType, name1 },
                                         tlCode));
  }

  protected void buildGetSlotDecl(int deep, String tomName, String name1, TomType tlType, TargetLanguage tlCode) {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    String opname = tomSymbol.getAstName().getString();
    TomTypeList typesList = tomSymbol.getTypesToType().getDomain();
    
    int slotIndex = getSlotIndex(tomSymbol.getSlotList(),slotName);
    TomTypeList l = typesList;
    for(int index = 0; !l.isEmpty() && index<slotIndex ; index++) {
      l = l.getTail();
    }
    TomType returnType = l.getHead();
    
    String argType;
    if(strictType) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    generateTargetLanguage(deep, genDecl(getTLType(returnType),
                                             "tom_get_slot", opname  + "_" + slotName.getString(),
                                             new String[] { argType, name1 },
                                             tlCode));
  }

  protected void  buildCompareFunctionSymbolDecl(int deep, String name1, String name2, String type1, String type2, TargetLanguage tlCode) {
    TomType argType1 = getUniversalType();
    if(isIntType(type1)) {
      argType1 = getIntType();
    } else if(isDoubleType(type2)) {
      argType1 = getDoubleType();
    }
    TomType argType2 = getUniversalType();
    if(isIntType(type2)) {
      argType2 = getIntType();
    } else if(isDoubleType(type2)) {
      argType2 = getDoubleType();
    }
    
    generateTargetLanguage(deep, genDecl(getTLType(getBoolType()), "tom_cmp_fun_sym", type1,
                                         new String[] {
                                           getTLType(argType1), name1,
                                           getTLType(argType2), name2
                                         },
                                         tlCode));
  }

  protected void buildTermsEqualDecl(int deep, String name1, String name2, String type1, String type2, TargetLanguage tlCode) {
    TomType argType1 = getUniversalType();
    if(isIntType(type1)) {
      argType1 = getIntType();
    } else if(isDoubleType(type2)) {
      argType1 = getDoubleType();
    }
    TomType argType2 = getUniversalType();
    if(isIntType(type2)) {
      argType2 = getIntType();
    } else if(isDoubleType(type2)) {
      argType2 = getDoubleType();
    }
    
    generateTargetLanguage(deep, genDecl(getTLType(getBoolType()), "tom_terms_equal", type1,
                                             new String[] {
                                               getTLType(argType1), name1,
                                               getTLType(argType2), name2
                                             },
                                             tlCode));
  }

  protected void buildGetHeadDecl(int deep, String name1, String type, TomType tlType, TargetLanguage tlCode) {
    String argType;
    if(strictType) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep,
                           genDecl(getTLType(getUniversalType()),
                                   "tom_get_head", type,
                                   new String[] { argType, name1 },
                                   tlCode));
  }

  protected void buildGetTailDecl(int deep, String name1, String type, TomType tlType, TargetLanguage tlCode) {
    String returnType, argType;
    if(strictType) {
      returnType = getTLCode(tlType);
      argType = getTLCode(tlType);
    } else {
      returnType = getTLType(getUniversalType());
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep,
                           genDecl(returnType, "tom_get_tail", type,
                                   new String[] { argType, name1 },
                                   tlCode));
  }

  protected void buildIsEmptyDecl(int deep, String name1, String type, TomType tlType, TargetLanguage tlCode) {
    String argType;
    if(strictType) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep,
                           genDecl(getTLType(getBoolType()),
                                   "tom_is_empty", type,
                                   new String[] { argType, name1 },
                                   tlCode));
  }

  protected void buildMakeEmptyList(int deep, String opname, TargetLanguage tlCode) {
    generateTargetLanguage(deep,
    	                           genDecl(getTLType(getUniversalType()),
                                       "tom_make_empty", opname,
                                       new String[] { },
                                       tlCode));
}

  protected void buildMakeAddList(int deep, String opname, String name1, String name2, TomType tlType1, TomType tlType2, TomType fullEltType, TomType fullListType, TargetLanguage tlCode) {
    String returnType, argListType,argEltType;
    if(strictType) {
      argEltType = getTLCode(tlType1);
      argListType = getTLCode(tlType2);
      returnType = argListType;
    } else {
      argEltType  = getTLType(getUniversalType());
      argListType = getTLType(getUniversalType());
      returnType  = argListType;
    }
    
    generateTargetLanguage(deep, genDecl(returnType,
                                             "tom_make_insert", opname,
                                             new String[] {
                                               argEltType, name1,
                                               argListType, name2
                                             },
                                             tlCode));
    
    generateTargetLanguage(deep, genDeclList(deep, opname, fullListType,fullEltType));
  }

  protected void buildGetElementDecl(int deep, String name1, String name2, String type1, TomType tlType1, TargetLanguage tlCode) {
    String returnType, argType;
    if(strictType) {
      returnType = getTLType(getUniversalType());
      argType = getTLCode(tlType1);
    } else {
      returnType = getTLType(getUniversalType());
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep, genDecl(returnType,
                                         "tom_get_element", type1,
                                         new String[] {
                                           argType, name1,
                                           getTLType(getIntType()), name2
                                         },
                                         tlCode));
  }

  protected void buildGetSizeDecl(int deep, String name1, String type, TomType tlType, TargetLanguage tlCode) {
    String argType;
    if(strictType) {
      argType = getTLCode(tlType);
    } else {
      argType = getTLType(getUniversalType());
    }
    
    generateTargetLanguage(deep,
                           genDecl(getTLType(getIntType()),
                                   "tom_get_size", type,
                                   new String[] { argType, name1 },
                                   tlCode));
  }

  protected void buildMakeEmptyArray(int deep, String opname, String name1, TargetLanguage tlCode) {
    generateTargetLanguage(deep, genDecl(getTLType(getUniversalType()), "tom_make_empty", opname,
                                             new String[] {
                                               getTLType(getIntType()), name1,
                                             },
                                             tlCode));
  }

  protected void buildMakeAddArray(int deep, String opname, String name1, String name2, TomType tlType1, TomType tlType2, TomType fullEltType, TomType fullArrayType, TargetLanguage tlCode) {
    String returnType, argListType,argEltType;
    if(strictType) {
      argEltType  = getTLCode(tlType1);
      argListType = getTLCode(tlType2);
      returnType  = argListType;
      
    } else {
      argEltType  = getTLType(getUniversalType());
      argListType = getTLType(getUniversalType());
      returnType  = argListType;
    }
    
    generateTargetLanguage(deep,
                           genDecl(argListType,
                                   "tom_make_append", opname,
                                   new String[] {
                                     argEltType, name1,
                                     argListType, name2
                                   },
                                   tlCode));
    generateTargetLanguage(deep, genDeclArray(deep, opname, fullArrayType, fullEltType));
  }

  protected void buildTypeTermDecl(int deep, TomList declList) {
    generateDeclarationFromList(deep, declList);
  }

  protected void buildTypeListDecl(int deep, TomList declList) {
    generateDeclarationFromList(deep, declList);
  }

  protected void buildTypeArrayDecl(int deep, TomList declList) {
    generateDeclarationFromList(deep, declList);
  }
  
  protected final void generateDeclarationFromList(int deep, TomList declList) {
    TomTerm term;
    while(!declList.isEmpty()) {
      term = declList.getHead();
      %match (TomTerm term){
        DeclarationToTomTerm(declaration) -> {generateDeclaration(deep, declaration);}
      }
      declList = declList.getTail();
    }
  }

  
} // class TomAbstractGenerator
