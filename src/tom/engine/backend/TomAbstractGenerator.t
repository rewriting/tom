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

import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import jtom.tools.OutputCode;
import jtom.tools.SingleLineOutputCode;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public abstract class TomAbstractGenerator {
  
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
  
  private void generate(int deep)
    throws IOException {
      //System.out.println("Generate: " + subject);
      //%variable
    TomTerm subject = input.getTerm();

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
        buildCompiledMatch(matchDeclarationList, namedBlockList, list);
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
        buildExpNot(exp);
        return;
      }

      And(exp1,exp2) -> {
        buildExpAnd(exp1, exp2);
        return;
      }

      Or(exp1,exp2) -> {
        buildExpOr(exp1, exp2);
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
        buildExpEmptyList(type1);
        return;
      }

      IsEmptyArray(varArray@Variable[astType=type1], varIndex@Variable[]) -> {
        buildExpEmptyArray(type1, varIndex);
        return;
      }

      EqualFunctionSymbol(var@Variable[astType=type1],
                          Appl(option,(Name(tomName)),l)) -> { // needs to be checked
        buildExpEqualFunctionVarAppl(var, type1, tomName);
        return;
      }
      
      EqualFunctionSymbol(var1@Variable[astType=type1],var2) -> {
          //System.out.println("EqualFunctionSymbol(...," + var2 + ")");
        buildExpEqualFunctionVarVar(type1, var2);
        return;
      }

      EqualTerm(var1@Variable[astType=type1],var2) -> {
        buildExpEqualTermVar(type1, var1, var2);
        return;
      }

      EqualTerm(var1@VariableStar[astType=type1],var2) -> {
        buildExpEqualTermVarStar(type1, var1, var2);
        return;
      }

      IsFsym(Name(opname), var@Variable(option1,PositionName(l1),type1)) -> {
        buildExpIsFsym(opname, var);
        return;
      }

      GetSubterm(var@Variable(option1,PositionName(l1),type1),Number(number)) -> {
        buildExpGetSubterm(var, type1);
        return;
      }

      GetSlot(Name(opname),slotName, var@Variable[]) -> {
        buildExpGetSlot(opname, slotName, var);
        return;
      }

      GetHead(var@Variable(option1,PositionName(l1),type1)) -> {
        buildExpGetHead(type1, var);
        return;
      }

      GetTail(var@Variable(option1,PositionName(l1),type1)) -> {
        buildExpGetTail(type1, var);
        return;
      }

      GetSize(var@Variable(option1,PositionName(l1),type1)) -> {
        buildExpGetSize(type1, var);
        return;
      }

      GetElement( varName@Variable(option1,PositionName(l1),type1),
                  varIndex@Variable(option2,PositionName(l2),type2)) -> {
        buildExpGetElement(type1, varName, varIndex);
        return;
      }

      GetSliceList(Name(name),
                   varBegin@Variable(option1,PositionName(l1),type1),
                   varEnd@Variable(option2,PositionName(l2),type2)) -> {
        
        buildExpGetSliceList(name, varBegin, varEnd);
        return;
      }

      GetSliceArray(Name(name),
                    varArray@Variable(option1,PositionName(l1),type1),
                    varBegin@Variable(option2,PositionName(l2),type2),
                    expEnd) -> {
        buildExpGetSliceArray(name, varArray, varBegin, expEnd);
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
        buildNamedBlock(blockName, instList);
        return;
      }
      
        //IfThenElse(exp,succesList,conc()) -> {
      IfThenElse(exp,succesList,emptyTomList()) -> {
        buildIfThenElse(exp,succesList);
        return;
      }

      IfThenElse(exp,succesList,failureList) -> {
        buildIfThenElseWithFailure(exp, succesList, failureList);
        return;
      }

      DoWhile(succesList,exp) -> {
        buildDoWhile(succesList,exp);
        return;
      }

      Assign(var@VariableStar(list,name1,
                              Type(ASTTomType(type),tlType@TLType[])),exp) -> {
        buildAssignVarExp(deep, var, tlType, exp);
        return;
      }

      Increment(var@Variable[]) -> {
        buildIncrement(var);
        return;
      }

      Action(l) -> {
        generateList(deep, l);
  /*
    while(!l.isEmpty()) {
    generate(out,deep,l.getHead());
    l = l.getTail();
    }
      //out.writeln("// ACTION: " + l);
      */
        return;
      }

      ExitAction(numberList) -> {
        buildExitAction(numberList);
        return;
      }

      Return(exp) -> {
        buildReturn(exp);
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
        buildSymbolDecl(tomName);
        return ;
      }
      
      ArraySymbolDecl(Name(tomName)) -> {
        buildArraySymbolDecl(tomName);
        return ;
      }

      ListSymbolDecl(Name(tomName)) -> {
        buildListSymbolDecl(tomName);
        return ;
      }

      GetFunctionSymbolDecl(Variable(option,Name(name),
                                     Type(ASTTomType(type),tlType@TLType[])),
                            tlCode, _) -> {
        buildGetFunctionSymbolDecl(type, name, tlType);
        return;
      }
      
      GetSubtermDecl(Variable(option1,Name(name1),
                              Type(ASTTomType(type1),tlType1@TLType[])),
                     Variable(option2,Name(name2),
                              Type(ASTTomType(type2),tlType2@TLType[])),
                     tlCode, _) -> {
        buildGetSubtermDecl(name1, name2, type1, tlType1, tlType2);
        return;
      }
      
      IsFsymDecl(Name(tomName),
		 Variable(option1,Name(name1), Type(ASTTomType(type1),tlType@TLType[])),
                 tlCode@TL[], _) -> {
        buildIsFsymDecl(tomName, name1, tlType);
        return;
      }
 
      GetSlotDecl[astName=Name(tomName),
                  slotName=slotName,
                  variable=Variable(option1,Name(name1), Type(ASTTomType(type1),tlType@TLType[])),
                  tlCode=tlCode@TL[]] -> {
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
        generateTargetLanguage(out,deep, genDecl(getTLType(returnType),
                                                 "tom_get_slot", opname  + "_" + slotName.getString(),
                                                 new String[] { argType, name1 },
                                                 tlCode));
        return;
      }

      CompareFunctionSymbolDecl(Variable(option1,Name(name1), Type(ASTTomType(type1),_)),
                                Variable(option2,Name(name2), Type(ASTTomType(type2),_)),
                                tlCode, _) -> {
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
        
        generateTargetLanguage(out,deep, genDecl(getTLType(getBoolType()), "tom_cmp_fun_sym", type1,
                                                 new String[] {
                                                   getTLType(argType1), name1,
                                                   getTLType(argType2), name2
                                                 },
                                                 tlCode));
        return;
      }

      TermsEqualDecl(Variable(option1,Name(name1), Type(ASTTomType(type1),_)),
                     Variable(option2,Name(name2), Type(ASTTomType(type2),_)),
                     tlCode, _) -> {
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

        generateTargetLanguage(out,deep, genDecl(getTLType(getBoolType()), "tom_terms_equal", type1,
                                                 new String[] {
                                                   getTLType(argType1), name1,
                                                   getTLType(argType2), name2
                                                 },
                                                 tlCode));
        return;
      }
      
      GetHeadDecl(Variable(option1,Name(name1), Type(ASTTomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        String argType;
        if(strictType) {
          argType = getTLCode(tlType);
        } else {
          argType = getTLType(getUniversalType());
        }

        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getUniversalType()),
                                       "tom_get_head", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      }

      GetTailDecl(Variable(option1,Name(name1), Type(ASTTomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        String returnType, argType;
        if(strictType) {
          returnType = getTLCode(tlType);
          argType = getTLCode(tlType);
        } else {
          returnType = getTLType(getUniversalType());
          argType = getTLType(getUniversalType());
        }
        
        generateTargetLanguage(out,deep,
                               genDecl(returnType, "tom_get_tail", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      }

      IsEmptyDecl(Variable(option1,Name(name1), Type(ASTTomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        String argType;
        if(strictType) {
          argType = getTLCode(tlType);
        } else {
          argType = getTLType(getUniversalType());
        }

        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getBoolType()),
                                       "tom_is_empty", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      }

      MakeEmptyList(Name(opname), tlCode@TL[], _) -> {
        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getUniversalType()),
                                       "tom_make_empty", opname,
                                       new String[] { },
                                       tlCode));
        return;
      }

      MakeAddList(Name(opname),
                  Variable(option1,Name(name1), fullEltType@Type(ASTTomType(type1),tlType1@TLType[])),
                  Variable(option2,Name(name2), fullListType@Type(ASTTomType(type2),tlType2@TLType[])),
                  tlCode@TL[], _) -> {
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
        
        generateTargetLanguage(out,deep, genDecl(returnType,
                                                 "tom_make_insert", opname,
                                                 new String[] {
                                                   argEltType, name1,
                                                   argListType, name2
                                                 },
                                                 tlCode));
        
        generateTargetLanguage(out,deep, genDeclList(opname, fullListType,fullEltType));
        return;
      }

      GetElementDecl(Variable(option1,Name(name1), Type(ASTTomType(type1),tlType1@TLType[])),
                     Variable(option2,Name(name2), Type(ASTTomType(type2),tlType2@TLType[])),
                     tlCode@TL[], _) -> {
        String returnType, argType;
        if(strictType) {
          returnType = getTLType(getUniversalType());
          argType = getTLCode(tlType1);
        } else {
          returnType = getTLType(getUniversalType());
          argType = getTLType(getUniversalType());
        }
      
        generateTargetLanguage(out,deep, genDecl(returnType,
                                                 "tom_get_element", type1,
                                                 new String[] {
                                                   argType, name1,
                                                   getTLType(getIntType()), name2
                                                 },
                                                 tlCode));
        return;
      }
      
      GetSizeDecl(Variable(option1,Name(name1), Type(ASTTomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        String argType;
        if(strictType) {
          argType = getTLCode(tlType);
        } else {
          argType = getTLType(getUniversalType());
        }
        
        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getIntType()),
                                       "tom_get_size", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      }
      
      MakeEmptyArray(Name(opname),
                     Variable(option1,Name(name1), Type(ASTTomType(type1),_)),
                     tlCode@TL[], _) -> {
        generateTargetLanguage(out,deep, genDecl(getTLType(getUniversalType()), "tom_make_empty", opname,
                                                 new String[] {
                                                   getTLType(getIntType()), name1,
                                                 },
                                                 tlCode));
        return;
      }

      MakeAddArray(Name(opname),
                   Variable(option1,Name(name1), fullEltType@Type(ASTTomType(type1),tlType1@TLType[])),
                   Variable(option2,Name(name2), fullArrayType@Type(ASTTomType(type2),tlType2@TLType[])),
                   tlCode@TL[], _) -> {

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

        generateTargetLanguage(out,deep,
                               genDecl(argListType,
                                       "tom_make_append", opname,
                                       new String[] {
                                         argEltType, name1,
                                         argListType, name2
                                       },
                                       tlCode));
        generateTargetLanguage(out,deep, genDeclArray(opname, fullArrayType, fullEltType));
        return;
      }

      MakeDecl(Name(opname), returnType, argList, tlCode@TL[], _) -> {
        generateTargetLanguage(out,deep, genDeclMake(opname, returnType, argList, tlCode));
        return;
      }
      
      TypeTermDecl[keywordList=declList] -> { 
        TomTerm term;
        while(!declList.isEmpty()) {
          term = declList.getHead();
          %match (TomTerm term){
            DeclarationToTomTerm(declaration) -> {generateDeclaration(out, deep, declaration);}
          }
          declList = declList.getTail();
        }
        return;
      }

      TypeListDecl[keywordList=declList] -> { 
        TomTerm term;
        while(!declList.isEmpty()) {
          term = declList.getHead();
          %match (TomTerm term){
            DeclarationToTomTerm(declaration) -> {generateDeclaration(out, deep, declaration);}
          }
          declList = declList.getTail();
        }
        return;
      }

      TypeArrayDecl[keywordList=declList] -> { 
        TomTerm term;
        while(!declList.isEmpty()) {
          term = declList.getHead();
          %match (TomTerm term){
            DeclarationToTomTerm(declaration) -> {generateDeclaration(out, deep, declaration);}
          }
          declList = declList.getTail();
        }
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
      //%variable

      //         while(!argList.isEmpty()) {
//           generate(out,deep,argList.getHead());
//           argList = argList.getTail();
//         }
    
    if(subject.isEmpty()) {
      return;
    }

    TomTerm t = subject.getHead();
    TomList l = subject.getTail(); 
    generate(deep,t);
    generateList(deep,l);
  }

  public void generateOptionList(int deep, OptionList subject)
    throws IOException {
      //%variable
    if(subject.isEmpty()) {
      return;
    }

    Option t = subject.getHead();
    OptionList l = subject.getTail(); 
    generateOption(out,deep,t);
    generateOptionList(out,deep,l);
  }

  public void generateSlotList(OutputCode out, int deep, SlotList slotList)
    throws IOException {
    while ( !slotList.isEmpty() ) {
      generateDeclaration(out, deep, slotList.getHead().getSlotDecl());
      slotList = slotList.getTail();
    }
  }
  
    // ------------------------------------------------------------
  private TargetLanguage genDecl(String returnType,
                                 String declName,
                                 String suffix,
                                 String args[],
                                 TargetLanguage tlCode) {
    String s = "";
    if(!genDecl) { return null; }
    String modifier ="";
    if(cCode || jCode) {
      if(staticFunction) {
        modifier += "static ";
      }
      if(jCode) {
        modifier += "public ";
      }
      s = modifier + returnType + " " + declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i] + " " + args[i+1];
        i+=2;
        if(i<args.length) {
          s+= ", ";
        }
      } 
      s += ") { return " + tlCode.getCode() + "; }";
    } else if(eCode) {
      s = declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i+1] + ": " + args[i];
        i+=2;
        if(i<args.length) {
          s+= "; ";
        }
      } 
      s += "): " + returnType + " is do Result := " + tlCode.getCode() + "end;";
    }
    if(tlCode.isTL())
      return `TL(s, tlCode.getStart(), tlCode.getEnd());
    else
      return `ITL(s);
  }

  
  private TargetLanguage genDeclMake(String opname, TomType returnType, 
                                     TomList argList, TargetLanguage tlCode) {
      //%variable
    String s = "";
    if(!genDecl) { return null; }
    String modifier = "";
    if(jCode || cCode) {
      if(staticFunction) {
        modifier += "static ";
      }
      if(jCode) {
        modifier += "public ";
      }
      
      s = modifier + getTLType(returnType) + " tom_make_" + opname + "(";
      while(!argList.isEmpty()) {
        TomTerm arg = argList.getHead();
        matchBlock: {
          %match(TomTerm arg) {
            Variable(option,Name(name), Type(ASTTomType(type),tlType@TLType[])) -> {
              s += getTLCode(tlType) + " " + name;
              break matchBlock;
            }
            
            _ -> {
              System.out.println("genDeclMake: strange term: " + arg);
              throw new TomRuntimeException(new Throwable("genDeclMake: strange term: " + arg));
            }
          }
        }
        argList = argList.getTail();
        if(!argList.isEmpty()) {
          s += ", ";
        }
      }
      s += ") { ";
      if (debugMode) {
        s += "\n"+getTLType(returnType)+ " debugVar = " + tlCode.getCode() +";\n";
        s += "jtom.debug.TomDebugger.debugger.termCreation(debugVar);\n";
        s += "return  debugVar;\n}";
      } else {
        s += "return " + tlCode.getCode() + "; }";
      }
    } else if(eCode) {
      boolean braces = !argList.isEmpty();
      s = "tom_make_" + opname;
      if(braces) {
        s = s + "(";
      }
      while(!argList.isEmpty()) {
        TomTerm arg = argList.getHead();
        matchBlock: {
          %match(TomTerm arg) {
            Variable(option,Name(name), Type(ASTTomType(type),tlType@TLType[])) -> {
              s += name + ": " + getTLCode(tlType);
              break matchBlock;
            }
            
            _ -> {
              System.out.println("genDeclMake: strange term: " + arg);
              throw new TomRuntimeException(new Throwable("genDeclMake: strange term: " + arg));
            }
          }
        }
        argList = argList.getTail();
        if(!argList.isEmpty()) {
          s += "; ";
        }
      }
      if(braces) {
        s = s + ")";
      }
      s += ": " + getTLType(returnType) + " is do Result := " + tlCode.getCode() + "end;";
    }
    return `TL(s, tlCode.getStart(), tlCode.getEnd());
  }
  
  private TargetLanguage genDeclList(String name, TomType listType, TomType eltType) {
      //%variable
    String s = "";
    if(!genDecl) { return null; }

    String tomType = getTomType(listType);
    String glType = getTLType(listType);
    String tlEltType = getTLType(eltType);

    String utype = glType;
    String modifier = "";
    if(eCode) {
      System.out.println("genDeclList: Eiffel code not yet implemented");
    } else if(jCode) {
      modifier = "public ";
      if(!strictType) {
        utype = getTLType(getUniversalType());
      }
    }

    if(staticFunction) {
      modifier += "static ";
    }

    String listCast = "(" + glType + ")";
    String eltCast = "(" + getTLType(eltType) + ")";
    String make_empty = listCast +  "tom_make_empty_" + name;
    String is_empty = "tom_is_empty_" + tomType;
    String make_insert = listCast + "tom_make_insert_" + name;
    String get_head = eltCast + "tom_get_head_" + tomType;
    String get_tail = listCast + "tom_get_tail_" + tomType;
    String reverse = listCast + "tom_reverse_" + name;

    s+= modifier + utype + " tom_reverse_" + name + "(" + utype + " l) {\n"; 
    s+= "   " + glType + " result = " + make_empty + "();\n"; 
    s+= "    while(!" + is_empty + "(l) ) {\n"; 
    s+= "      result = " + make_insert + "(" + get_head + "(l),result);\n";    
    s+= "      l = " + get_tail + "(l);\n";  
    s+= "    }\n";
    s+= "    return result;\n";
    s+= "  }\n";
    s+= "\n";

    s+= modifier + utype + " tom_insert_list_" + name +  "(" + utype + " l1, " + utype + " l2) {\n";
    s+= "   if(" + is_empty + "(l1)) {\n";
    s+= "    return l2;\n";  
    s+= "   } else if(" + is_empty + "(l2)) {\n";
    s+= "    return l1;\n";  
    s+= "   } else if(" + is_empty + "(" + get_tail + "(l1))) {\n";  
    s+= "    return " + make_insert + "(" + get_head + "(l1),l2);\n";
    s+= "   } else { \n";  
    s+= "    return " + make_insert + "(" + get_head + "(l1),tom_insert_list_" + name +  "(" + get_tail + "(l1),l2));\n";
    s+= "   }\n";
    s+= "  }\n";
    s+= "\n";


    
    s+= modifier + utype + " tom_get_slice_" + name + "(" + utype + " begin, " + utype + " end) {\n"; 
    s+= "   " + glType + " result = " + make_empty + "();\n"; 
    s+= "    while(!tom_terms_equal_" + tomType + "(begin,end)) {\n";
    s+= "      result = " + make_insert + "(" + get_head + "(begin),result);\n";
    s+= "      begin = " + get_tail + "(begin);\n";
    s+="     }\n";
    s+= "    result = " + reverse + "(result);\n";
    s+= "    return result;\n";
    s+= "  }\n";
    
    TargetLanguage resultTL = `ITL(s);
      //If necessary we remove \n code depending on --pretty option
    resultTL = ast().reworkTLCode(resultTL, pretty);
    return resultTL;
  }

  private TargetLanguage genDeclArray(String name, TomType listType, TomType eltType) {
      //%variable
    String s = "";
    if(!genDecl) { return null; }

    String tomType = getTomType(listType);
    String glType = getTLType(listType);
    String tlEltType = getTLType(eltType);
    String utype = glType;
    String modifier = "";
    if(eCode) {
      System.out.println("genDeclArray: Eiffel code not yet implemented");
    } else if(jCode) {
      modifier ="public ";
      if(!strictType) {
        utype =  getTLType(getUniversalType());
      }
    }

    if(staticFunction) {
      modifier += "static ";
    }
    
    String listCast = "(" + glType + ")";
    String eltCast = "(" + getTLType(eltType) + ")";
    String make_empty = listCast + "tom_make_empty_" + name;
    String make_append = listCast + "tom_make_append_" + name;
    String get_element = eltCast + "tom_get_element_" + tomType;

    
    s = modifier + utype + " tom_get_slice_" + name +  "(" + utype + " subject, int begin, int end) {\n";
    s+= "   " + glType + " result = " + make_empty + "(end - begin);\n";
    s+= "    while( begin != end ) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(subject, begin),result);\n";
    s+= "      begin++;\n";
    s+="     }\n";
    s+= "    return result;\n";
    s+= "  }\n";
    s+= "\n";
    
    s+= modifier + utype + " tom_append_array_" + name +  "(" + utype + " l2, " + utype + " l1) {\n";
    s+= "    int size1 = tom_get_size_" + tomType + "(l1);\n";
    s+= "    int size2 = tom_get_size_" + tomType + "(l2);\n";
    s+= "    int index;\n";
    s+= "   " + glType + " result = " + make_empty + "(size1+size2);\n";

    s+= "    index=size1;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(l1,(size1-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";

    s+= "    index=size2;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(l2,(size2-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";
   
    s+= "    return result;\n";
    s+= "  }\n";

    TargetLanguage resultTL = `ITL(s);
      //If necessary we remove \n code depending on --pretty option
    resultTL = ast().reworkTLCode(resultTL, pretty);
    return resultTL;
  }
 
  protected abstract void buildTerm(String name, TomList argList);
  protected abstract void buildList(String name, TomList argList);
  protected abstract void buildArray(String name, TomList argList);
  protected abstract void buildFunctionCall(String name, TomList argList);

  protected void buildCompiledMatch() {
    boolean generated = hasGeneratedMatch(list);
    boolean defaultPattern = hasDefaultCase(list);
    Option orgTrack = null;
    if(supportedBlock) {
      generateInstruction(out,deep,`OpenBlock());
    }
    if(debugMode && !generated) {
      orgTrack = findOriginTracking(list);
      debugKey = orgTrack.getFileName().getString() + orgTrack.getLine();
      out.write("jtom.debug.TomDebugger.debugger.enteringStructure(\""+debugKey+"\");\n");
    }
    generateList(out,deep+1,matchDeclarationList);
    generateList(out,deep+1,namedBlockList);
    if(debugMode && !generated && !defaultPattern) {
      out.write("jtom.debug.TomDebugger.debugger.leavingStructure(\""+debugKey+"\");\n");
    }
    if(supportedBlock) {
      generateInstruction(out,deep,`CloseBlock());
    }
  }
	
  protected abstract void buildDeclaration(TomTerm var, String name, String type, TomType tlType);
  protected abstract void buildDeclarationStar(TomTerm var, String name, String type, TomType tlType);
  protected abstract void buildFunctionBegin(String tomName, TomList varList); 
  protected abstract void buildFunctionEnd();
  protected abstract void buildExpNot(Expression exp);
  
  protected void buildExpAnd(Expression exp1, Expression exp2) {
    generateExpression(out,deep,exp1);
    out.write(" && ");
    generateExpression(out,deep,exp2);
  }
  protected void buildExpOr(Expression exp1, Expression exp2) {
    generateExpression(out,deep,exp1);
    out.write(" || ");
    generateExpression(out,deep,exp2);
  }

  protected abstract void buildExpTrue();

  protected abstract void buildExpFalse();

  protected void buildExpEmptyList(TomType type1) {
    out.write("tom_is_empty_" + getTomType(type1) + "(");
    generate(out,deep,var);
    out.write(")");
  }

  protected void buildExpEmptyArray(TomType type1, TomTerm varIndex) {
    generate(out,deep,varIndex);
    out.write(" >= ");
    out.write("tom_get_size_" + getTomType(type1) + "(");
    generate(out,deep,varArray);
    out.write(")");
  }

  protected void buildExpEqualFunctionVarAppl(TomTerm var, TomType type1, String tomName) {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    TomName termNameAST = tomSymbol.getAstName();
    OptionList termOptionList = tomSymbol.getOption();
    
    Declaration isFsymDecl = getIsFsymDecl(termOptionList);
    if(isFsymDecl != null) {
      generateExpression(out,deep,`IsFsym(termNameAST,var));
    } else {
      String s = (String)getFunSymMap.get(type1);
      if(s == null) {
        s = "tom_cmp_fun_sym_" + getTomType(type1) + "(tom_get_fun_sym_" + getTomType(type1) + "(";
        getFunSymMap.put(type1,s);
      }
      out.write(s);
      generate(out,deep,var);
      out.write(") , " + getSymbolCode(tomSymbol) + ")");
    }
  }

  protected void buildExpEqualFunctionVarVar(TomType type1, TomTerm var2) {
    out.write("tom_cmp_fun_sym_" + getTomType(type1) + "(");
    out.write("tom_get_fun_sym_" + getTomType(type1) + "(");
    generate(out,deep,var1);
    out.write(") , " + var2 + ")");///??????????????????????????????
  }

  protected void buildExpEqualTerm(TomType type1, TomTerm var1,TomTerm var2) {
    out.write("tom_terms_equal_" + getTomType(type1) + "(");
    generate(out,deep,var1);
    out.write(", ");
    generate(out,deep,var2);
    out.write(")");
  }

  protected void buildExpEqualTermVarStar(TomType type1, TomTerm var1, TomTerm var2) {
    out.write("tom_terms_equal_" + getTomType(type1) + "(");
    generate(out,deep,var1);
    out.write(", ");
    generate(out,deep,var2);
    out.write(")");
  }

  protected void buildExpIsFsym(String opname, TomTerm var) {
    String s = (String)isFsymMap.get(opname);
    if(s == null) {
      s = "tom_is_fun_sym_" + opname + "(";
      isFsymMap.put(opname,s);
    } 
    out.write(s);
    generate(out,deep,var);
    out.write(")");
  }

  protected void buildExpGetSubterm(TomTerm var, TomType type1) {
    String s = (String)getSubtermMap.get(type1);
    if(s == null) {
      s = "tom_get_subterm_" + getTomType(type1) + "(";
      getSubtermMap.put(type1,s);
    } 
    out.write(s);
    generate(out,deep,var);
    out.write(", " + number + ")");
  }

  protected void buildExpGetSlot(String opname, String slotName, TomTerm var) {
    out.write("tom_get_slot_" + opname + "_" + slotName + "(");
    generate(out,deep,var);
    out.write(")");
  }

  protected void buildExpGetHead(TomType type1, TomTerm var) {
    out.write("tom_get_head_" + getTomType(type1) + "(");
    generate(out,deep,var);
    out.write(")");
  }
  protected void buildExpGetTail(TomType type1, TomTerm var) {
    out.write("tom_get_tail_" + getTomType(type1) + "(");
    generate(out,deep,var); 
    out.write(")");
  }

  protected void buildExpGetSize(TomType type1, TomTerm var) {
    out.write("tom_get_size_" + getTomType(type1) + "(");
    generate(out,deep,var);
    out.write(")");
  }

  protected void buildExpGetElement(TomType type1, TomTerm varName, TomTerm varIndex) {
    out.write("tom_get_element_" + getTomType(type1) + "(");
    generate(out,deep,varName);
    out.write(",");
    generate(out,deep,varIndex);
    out.write(")");
  }

  protected void buildExpGetSliceList(String name, TomTerm varBegin, TomTerm varEnd) {
    out.write("tom_get_slice_" + name + "(");
    generate(out,deep,varBegin);
    out.write(",");
    generate(out,deep,varEnd);
    out.write(")");
  }

  protected void buildExpGetSliceArray(String name, TomTerm varArray, TomTerm varBegin, TomTerm expEnd) {
    out.write("tom_get_slice_" + name + "(");
    generate(out,deep,varArray);
    out.write(",");
    generate(out,deep,varBegin);
    out.write(",");
    generate(out,deep,expEnd);
    out.write(")");
  }

  protected abstract void buildAssignVar(int deep, TomTerm var, TomType type, TomType tlType);
  protected abstract void buildAssignMatch(int deep, TomTerm var, TomType type, TomType tlType);
  protected abstract void buildNamedBlock(String blockName, TomList instList);
  protected abstract void buildIfThenElse(Expression exp, TomList succesList);
  protected abstract void buildIfThenElseWithFailure(Expression exp, TomList succesList, TomList failureList);

  protected void buildDoWhile(TomList succesList, Expression exp) {
    out.writeln(deep,"do {");
    generateList(deep+1,succesList);
    out.write(deep,"} while("); generateExpression(out,deep,exp); out.writeln(");");
  }
  
  protected abstract void buildAssignVarExp(int deep, TomTerm var, TomType tlType, Expression exp);

  protected void buildIncrement(TomTerm var) {
    generate(out,deep,var);
    out.write(" = ");
    generate(out,deep,var);
    out.writeln(" + 1;");
  }

  protected abstract void buildExitAction(TomNumberList numberList);
  protected abstract void buildReturn(Expression exp);
  protected abstract void buildSymbolDecl(String tomName);
  protected abstract void buildArraySymbolDecl(String tomName);

  protected abstract void buildListSymbolDecl(int deep, String tomName);{
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    OptionList optionList = tomSymbol.getOption();
    SlotList slotList = tomSymbol.getSlotList();
    TomTypeList l = getSymbolDomain(tomSymbol);
    TomType type1 = getSymbolCodomain(tomSymbol);
    String name1 = tomSymbol.getAstName().getString();
    if(cCode) {
        // TODO: build an abstract declaration
      int argno=1;
      out.indent(deep);
      if(!l.isEmpty()) {
        out.write(getTLType(type1));
        out.writeSpace();
        out.write(name1);
        if(!l.isEmpty()) {
          out.writeOpenBrace();
          while (!l.isEmpty()) {
            out.write(getTLType(l.getHead()));
            out.writeUnderscore();
            out.write(argno);
            argno++;
            l = l.getTail() ;
            if(!l.isEmpty()) {
              out.writeComa();
            }
          }
          out.writeCloseBrace();
          out.writeSemiColon();
        }
      }
      out.writeln();
    } else if(jCode) {
        // do nothing
    } else if(eCode) {
        // do nothing
    }
    
      // inspect the optionList
    generateOptionList(out, deep, optionList);
      // inspect the slotlist
    generateSlotList(out, deep, slotList);
  }

  protected void buildGetFunctionSymbolDecl(TomType type, String name, TomType tlType) {
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

  protected abstract void buildGetSubtermDecl(String name1, String name2, TomType type1, TomType tlType1, TomType tlType2) {
    String args[];
    if(strictType || eCode) {
      args = new String[] { getTLCode(tlType1), name1,
                            getTLCode(tlType2), name2 };
    } else {
          args = new String[] { getTLType(getUniversalType()), name1,
                                getTLCode(tlType2), name2 };
    }
    generateTargetLanguage(out,deep, genDecl(getTLType(getUniversalType()), "tom_get_subterm", type1,
                                             args, tlCode));
  }

  protected void buildIsFsymDecl(TomName tomName, String name1, TomType tlType) {
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


} // class TomAbstractGenerator
