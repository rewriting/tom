/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2004 INRIA
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

import java.io.IOException;

import jtom.adt.tomsignature.types.*;
import jtom.TomBase;
import jtom.tools.TomTaskInput;
import jtom.tools.OutputCode;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public abstract class TomAbstractGenerator extends TomBase {
  
  protected OutputCode output;
  protected TomTaskInput input;
  protected String debugKey;
  protected boolean debugMode = false, strictType = false,
    staticFunction = false, genDecl = false, pretty = false, verbose = false;

  public TomAbstractGenerator(TomEnvironment environment, OutputCode output, TomTaskInput input) {
    super(environment);
	  this.output = output;
    this.input = input;
		this.debugMode = input.isDebugMode();
		this.strictType = input.isStrictType();
		this.staticFunction = input.isStaticFunction();
		this.genDecl = input.isGenDecl();
		this.pretty = input.isPretty();
  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------

    /*
     * Generate the goal language
     */
 
  protected void generate(int deep, TomTerm subject)throws IOException {
    %match(TomTerm subject) {
      
      Tom(l) -> {
        generateList(deep,l);
        return;
      }

      TomInclude(l) -> {
        generateListInclude(deep,l);
        return;
      }
     
      Ref(term) -> {
        buildRef(deep, term);
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
        buildTerm(deep, name, argList);
        return;
      }

      BuildList(Name(name), argList) -> {
        buildList(deep, name, argList);
        return;
      }

      BuildArray(Name(name), argList) -> {
        buildArray(deep, name, argList);
        return;
      }

      FunctionCall(Name(name), argList) -> {
        buildFunctionCall(deep, name, argList);
        return;
      }

      Composite(argList) -> {
        generateList(deep,argList);
        return;
      }
      
      Variable[option=option1,astName=PositionName(l1),astType=type1] -> {
          /*
           * sans type: re-definition lorsque %variable est utilise
           * avec type: probleme en cas de filtrage dynamique
           */
        output.write("tom" + numberListToIdentifier(l1));
        return;
      }

      Variable[option=option1,astName=Name(name1),astType=type1] -> {
        output.write(name1);
        return;
      }

      VariableStar[option=option1,astName=PositionName(l1),astType=type1] -> {
        output.write("tom" + numberListToIdentifier(l1));
        return;  
      }

      VariableStar[option=option1,astName=Name(name1),astType=type1] -> {
        output.write(name1);
        return;
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

  public void generateExpression(int deep, Expression subject) throws IOException {
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
        buildExpTrue(deep);
        return;
      }
      
      FalseTL() -> {
        buildExpFalse(deep);
        return;
      }

      IsEmptyList(var@Ref(Variable[astType=type1])) -> {
        buildExpEmptyList(deep, type1, var);
        return;
      }

      IsEmptyArray(varArray@Ref(Variable[astType=type1]), varIndex@Ref(Variable[])) -> {
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
        String s = var2.toString();
        buildExpEqualFunctionVarVar(deep, type1, var1, s);
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

      IsFsym(Name(opname), var@Variable[option=option1,astName=PositionName(l1),astType=type1]) -> {
        buildExpIsFsym(deep, opname, var);
        return;
      }

      GetSubterm(var@Variable[option=option1,astName=PositionName(l1),astType=type1],Number(number)) -> {
        buildExpGetSubterm(deep, var, type1, number);
        return;
      }

      GetSlot(Name(opname),slotName, var@Variable[]) -> {
        buildExpGetSlot(deep, opname, slotName, var);
        return;
      }

      GetHead(var@Ref(Variable[option=option1,astName=PositionName(l1),astType=type1])) -> {
        buildExpGetHead(deep, type1, var);
        return;
      }

      GetTail(var@Ref(Variable[option=option1,astName=PositionName(l1),astType=type1])) -> {
        buildExpGetTail(deep, type1, var);
        return;
      }

      GetSize(var@Variable[option=option1,astName=PositionName(l1),astType=type1]) -> {
        buildExpGetSize(deep, type1, var);
        return;
      }

      GetElement( varName@Variable[option=option1,astName=PositionName(l1),astType=type1],
                  varIndex@Variable[option=option2,astName=PositionName(l2),astType=type2]) -> {
        buildExpGetElement(deep, type1, varName, varIndex);
        return;
      }

      GetSliceList(Name(name),
                   varBegin@Variable[option=option1,astName=PositionName(l1),astType=type1],
                   varEnd@Ref(Variable[option=option2,astName=PositionName(l2),astType=type2])) -> {
        
        buildExpGetSliceList(deep, name, varBegin, varEnd);
        return;
      }

      GetSliceArray(Name(name),
                    varArray@Ref(Variable[option=option1,astName=PositionName(l1),astType=type1]),
                    varBegin@Variable[option=option2,astName=PositionName(l2),astType=type2],
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

  public void generateInstruction(int deep, Instruction subject) throws IOException {
    %match(Instruction subject) {

      TargetLanguageToInstruction(t) -> {
        generateTargetLanguage(deep,t);
        return;
      }

      TomTermToInstruction(t) -> {
        generate(deep,t);
        return;
      }

      Nop() -> {
        return;
      }

      MakeFunctionBegin(Name(tomName),SubjectList(varList)) -> {
        buildFunctionBegin(deep, tomName, varList);
        return;
      }

      MakeFunctionEnd() -> {
        buildFunctionEnd(deep);
        return;
      }

      EndLocalVariable() -> {
        output.writeln(deep,"do");
        return;
      }
      
      Assign(var@(Variable|VariableStar)[option=option,astName=name1,
                          astType=Type(ASTTomType(type),tlType@TLType[])],exp) -> {
        buildAssignVar(deep, var, option, type, tlType, exp);
        return;
      }

			AssignMatchSubject(var@Variable[option=option,astName=name1,
																			astType=Type(tomType@ASTTomType(type),tlType@TLType[])],exp) -> {
				buildAssignVar(deep, var, option, type, tlType, exp);
				return;
			}

      Assign((UnamedVariable|UnamedVariableStar)[],exp) -> {
        return;
      }

      (Let|LetRef)((UnamedVariable|UnamedVariableStar)[],exp,body) -> {
        generateInstruction(deep,body);
        return;
      }

      Let(var@(Variable|VariableStar)[option=list,astName=name1,astType=Type(ASTTomType(type),tlType@TLType[])],exp,body) -> {
        buildLet(deep, var, list, type, tlType, exp, body);
        return;
      }

      LetRef(var@(Variable|VariableStar)[option=list,astName=name1,astType=Type(ASTTomType(type),tlType@TLType[])],exp,body) -> {
        buildLetRef(deep, var, list, type, tlType, exp, body);
        return;
      }

      AbstractBlock(instList) -> {
        generateInstructionList(deep, instList);
        return;
      }

      UnamedBlock(instList) -> {
        buildUnamedBlock(deep, instList);
        return;
      }

      NamedBlock(blockName,instList) -> {
        buildNamedBlock(deep, blockName, instList);
        return;
      }
      
        //IfThenElse(exp,succesList,conc()) -> {
      IfThenElse(exp,succesList,Nop()) -> {
        buildIfThenElse(deep, exp,succesList);
        return;
      }

      IfThenElse(exp,succesList,failureList) -> {
        buildIfThenElseWithFailure(deep, exp, succesList, failureList);
        return;
      }

      DoWhile(succes,exp) -> {
        buildDoWhile(deep, succes,exp);
        return;
      }

      Increment(var@Variable[]) -> {
        buildIncrement(deep, var);
        return;
      }

      Action(l) -> {
        generateList(deep, l);
        return;
      }

      Return(exp) -> {
        buildReturn(deep, exp);
        return;
      }

      CompiledMatch(instruction, list) -> {
        buildCompiledMatch(deep, instruction, list);
        return;
      }
      
      CompiledPattern(instruction) -> {
        buildInstructionSequence(deep,instruction);
        return;
      }
      
      t -> {
        System.out.println("Cannot generate code for instruction: " + t);
        throw new TomRuntimeException(new Throwable("Cannot generate code for instruction: " + t));
      }
    }
  }
  
  public void generateTargetLanguage(int deep, TargetLanguage subject) throws IOException {
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
        buildComment(deep,t);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for TL: " + t);
        throw new TomRuntimeException(new Throwable("Cannot generate code for TL: " + t));
      }
    }
  }

  public void generateOption(int deep, Option subject) throws IOException {
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
  
  public void generateDeclaration(int deep, Declaration subject) throws IOException {
    %match(Declaration subject) {
      EmptyDeclaration() -> {
        return;
      }
      (SymbolDecl|ArraySymbolDecl|ListSymbolDecl)(Name(tomName)) -> {
        buildSymbolDecl(deep, tomName);
        return ;
      }

      GetFunctionSymbolDecl(Variable[option=option,astName=Name(name),
                                     astType=Type(ASTTomType(type),tlType@TLType[])],
                            tlCode, _) -> {
        buildGetFunctionSymbolDecl(deep, type, name, tlType, tlCode);
        return;
      }
      
      GetSubtermDecl(Variable[option=option1,astName=Name(name1),
                              astType=Type(ASTTomType(type1),tlType1@TLType[])],
                     Variable[option=option2,astName=Name(name2),
                              astType=Type(ASTTomType(type2),tlType2@TLType[])],
                     tlCode, _) -> {
        buildGetSubtermDecl(deep, name1, name2, type1, tlType1, tlType2, tlCode);
        return;
      }
      
      IsFsymDecl(Name(tomName),
		   Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type1),tlType@TLType[])],
                 tlCode@TL[], _) -> {
        buildIsFsymDecl(deep, tomName, name1, tlType, tlCode);
        return;
      }
 
      GetSlotDecl[astName=Name(tomName),
                  slotName=slotName,
                  variable=Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type1),tlType@TLType[])],
                  tlCode=tlCode@TL[]] -> {
        buildGetSlotDecl(deep, tomName, name1, tlType, tlCode, slotName);
        return;
      }

      CompareFunctionSymbolDecl(Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type1),_)],
                                Variable[option=option2,astName=Name(name2), astType=Type(ASTTomType(type2),_)],
                                tlCode, _) -> {
        buildCompareFunctionSymbolDecl(deep, name1, name2, type1, type2, tlCode);
        return;
      }

      TermsEqualDecl(Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type1),_)],
                     Variable[option=option2,astName=Name(name2), astType=Type(ASTTomType(type2),_)],
                     tlCode, _) -> {
        buildTermsEqualDecl(deep, name1, name2, type1, type2, tlCode);
        return;
      }
      
      GetHeadDecl(Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type),tlType@TLType[])],
                  tlCode@TL[], _) -> {
        buildGetHeadDecl(deep, name1, type, tlType, tlCode);
        return;
      }

      GetTailDecl(Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type),tlType@TLType[])],
                  tlCode@TL[], _) -> {
        buildGetTailDecl(deep, name1, type, tlType, tlCode);
        return;
      }

      IsEmptyDecl(Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type),tlType@TLType[])],
                  tlCode@TL[], _) -> {
        buildIsEmptyDecl(deep, name1, type, tlType, tlCode);
        return;
      }

      MakeEmptyList(Name(opname), tlCode@TL[], _) -> {
        //System.out.println("symbol = " + getSymbol(opname));
        TomType codomain = getSymbolCodomain(getSymbol(opname));
        buildMakeEmptyList(deep, opname, codomain, tlCode);
        return;
      }

      MakeAddList(Name(opname),
                  Variable[option=option1,astName=Name(name1), astType=fullEltType@Type(ASTTomType(type1),tlType1@TLType[])],
                  Variable[option=option2,astName=Name(name2), astType=fullListType@Type(ASTTomType(type2),tlType2@TLType[])],
                  tlCode@TL[], _) -> {
        buildMakeAddList(deep, opname, name1, name2, tlType1, tlType2, fullEltType, fullListType, tlCode);
        return;
      }

      GetElementDecl(Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type1),tlType1@TLType[])],
                     Variable[option=option2,astName=Name(name2), astType=Type(ASTTomType(type2),tlType2@TLType[])],
                     tlCode@TL[], _) -> {
        buildGetElementDecl(deep, name1, name2, type1, tlType1, tlCode);
        return;
      }
      
      GetSizeDecl(Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type),tlType@TLType[])],
                  tlCode@TL[], _) -> {
        buildGetSizeDecl(deep, name1, type, tlType, tlCode);
        return;
      }
      
      MakeEmptyArray(Name(opname),
                     Variable[option=option1,astName=Name(name1), astType=Type(ASTTomType(type1),_)],
                     tlCode@TL[], _) -> {
        TomType codomain = getSymbolCodomain(getSymbol(opname));
        buildMakeEmptyArray(deep, opname, codomain, name1, tlCode);
        return;
      }

      MakeAddArray(Name(opname),
                   Variable[option=option1,astName=Name(name1), astType=fullEltType@Type(ASTTomType(type1),tlType1@TLType[])],
                   Variable[option=option2,astName=Name(name2), astType=fullArrayType@Type(ASTTomType(type2),tlType2@TLType[])],
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
	
	public void generateListInclude(int deep, TomList subject) throws IOException {
 		output.setSingleLine();
		generateList(deep, subject);
		output.unsetSingleLine();
	}

  public void generateList(int deep, TomList subject)
    throws IOException {
    while(!subject.isEmpty()) {
      generate(deep,subject.getHead());
      subject = subject.getTail();
    }
  }
  
  public void generateOptionList(int deep, OptionList subject)
    throws IOException {
    while(!subject.isEmpty()) {
      generateOption(deep,subject.getHead());
      subject = subject.getTail();
    }
  }

  public void generateInstructionList(int deep, InstructionList subject)
    throws IOException {
    while(!subject.isEmpty()) {
      generateInstruction(deep,subject.getHead());
      subject = subject.getTail();
    }
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
 
	// ------------------------------------------------------------
  
  protected abstract void buildInstructionSequence(int deep, Instruction instruction) throws IOException;
  protected abstract void buildComment(int deep, String text) throws IOException;
  protected abstract void buildTerm(int deep, String name, TomList argList) throws IOException;
  protected abstract void buildRef(int deep, TomTerm term) throws IOException;
  protected abstract void buildList(int deep, String name, TomList argList) throws IOException;
  protected abstract void buildArray(int deep,String name, TomList argList) throws IOException;
  protected abstract void buildFunctionCall(int deep, String name, TomList argList)  throws IOException;
  protected abstract void buildFunctionBegin(int deep, String tomName, TomList varList) throws IOException; 
  protected abstract void buildFunctionEnd(int deep) throws IOException;
  protected abstract void buildExpNot(int deep, Expression exp) throws IOException;

  protected abstract void buildCompiledMatch(int deep, Instruction instruction, OptionList list) throws IOException;
	protected abstract void buildExpAnd(int deep, Expression exp1, Expression exp2) throws IOException;
  protected abstract void buildExpOr(int deep, Expression exp1, Expression exp2) throws IOException;
  protected abstract void buildExpTrue(int deep) throws IOException;
  protected abstract void buildExpFalse(int deep) throws IOException;
  protected abstract void buildExpEmptyList(int deep, TomType type1, TomTerm var) throws IOException;
  protected abstract void buildExpEmptyArray(int deep, TomType type1, TomTerm varIndex, TomTerm varArray) throws IOException;
  protected abstract void buildExpEqualFunctionVarAppl(int deep, TomTerm var, TomType type1, String tomName) throws IOException;
  protected abstract void buildExpEqualFunctionVarVar(int deep, TomType type1, TomTerm var1, String var2) throws IOException;
  protected abstract void buildExpEqualTermVar(int deep, TomType type1, TomTerm var1,TomTerm var2) throws IOException;
  protected abstract void buildExpEqualTermVarStar(int deep, TomType type1, TomTerm var1, TomTerm var2) throws IOException;
  protected abstract void buildExpIsFsym(int deep, String opname, TomTerm var) throws IOException;
  protected abstract void buildExpGetSubterm(int deep, TomTerm var, TomType type1, int number) throws IOException;
  protected abstract void buildExpGetSlot(int deep, String opname, String slotName, TomTerm var) throws IOException;
  protected abstract void buildExpGetHead(int deep, TomType type1, TomTerm var) throws IOException;
  protected abstract void buildExpGetTail(int deep, TomType type1, TomTerm var) throws IOException;
  protected abstract void buildExpGetSize(int deep, TomType type1, TomTerm var) throws IOException;
  protected abstract void buildExpGetElement(int deep, TomType type1, TomTerm varName, TomTerm varIndex) throws IOException;
  protected abstract void buildExpGetSliceList(int deep, String name, TomTerm varBegin, TomTerm varEnd) throws IOException;
  protected abstract void buildExpGetSliceArray(int deep, String name, TomTerm varArray, TomTerm varBegin, TomTerm expEnd) throws IOException;
  protected abstract void buildAssignVar(int deep, TomTerm var, OptionList list, String type, TomType tlType, Expression exp) throws IOException ;
  protected abstract void buildLet(int deep, TomTerm var, OptionList list, String type, TomType tlType, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildLetRef(int deep, TomTerm var, OptionList list, String type, TomType tlType, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildNamedBlock(int deep, String blockName, InstructionList instList) throws IOException ;
  protected abstract void buildUnamedBlock(int deep, InstructionList instList) throws IOException ;
  protected abstract void buildIfThenElse(int deep, Expression exp, Instruction succes) throws IOException ;
  protected abstract void buildIfThenElseWithFailure(int deep, Expression exp, Instruction succes, Instruction failure) throws IOException ;
  protected abstract void buildDoWhile(int deep, Instruction succes, Expression exp) throws IOException;
  protected abstract void buildIncrement(int deep, TomTerm var) throws IOException;
  protected abstract void buildReturn(int deep, TomTerm exp) throws IOException ;
  protected abstract void buildSymbolDecl(int deep, String tomName) throws IOException ;
  protected abstract void buildGetFunctionSymbolDecl(int deep, String type, String name,
TomType tlType, TargetLanguage tlCode) throws IOException;
  protected abstract void buildGetSubtermDecl(int deep, String name1, String name2, String type1,
TomType tlType1, TomType tlType2, TargetLanguage tlCode) throws IOException ;
  protected abstract void buildIsFsymDecl(int deep, String tomName, String name1,
TomType tlType, TargetLanguage tlCode) throws IOException;
  protected abstract void buildGetSlotDecl(int deep, String tomName, String name1,
TomType tlType, TargetLanguage tlCode, TomName slotName) throws IOException;
  protected abstract void  buildCompareFunctionSymbolDecl(int deep, String name1,
String name2, String type1, String type2, TargetLanguage tlCode) throws IOException;
  protected abstract void buildTermsEqualDecl(int deep, String name1, String name2,
String type1, String type2, TargetLanguage tlCode) throws IOException;
  protected abstract void buildGetHeadDecl(int deep, String name1, String type, TomType tlType,TargetLanguage tlCode) throws IOException;
  protected abstract void buildGetTailDecl(int deep, String name1, String type, TomType tlType, TargetLanguage tlCode) throws IOException;
  protected abstract void buildIsEmptyDecl(int deep, String name1, String type,
TomType tlType, TargetLanguage tlCode) throws IOException;
  protected abstract void buildMakeEmptyList(int deep, String opname, TomType codomain, TargetLanguage tlCode) throws IOException;
  protected abstract void buildMakeAddList(int deep, String opname, String name1,
String name2, TomType tlType1, TomType tlType2, TomType fullEltType,
TomType fullListType, TargetLanguage tlCode) throws IOException;
  protected abstract void buildGetElementDecl(int deep, String name1, String name2,
String type1, TomType tlType1, TargetLanguage tlCode) throws IOException;
  protected abstract void buildGetSizeDecl(int deep, String name1, String type,
TomType tlType, TargetLanguage tlCode) throws IOException;
  protected abstract void buildMakeEmptyArray(int deep, String opname, TomType codomain,String name1, TargetLanguage tlCode) throws IOException;
  protected abstract void buildMakeAddArray(int deep, String opname, String name1, String name2, TomType tlType1,
TomType tlType2, TomType fullEltType, TomType fullArrayType, TargetLanguage tlCode) throws IOException;
  protected abstract void buildTypeTermDecl(int deep, TomList declList) throws IOException;
  protected abstract void buildTypeListDecl(int deep, TomList declList) throws IOException;
  protected abstract void buildTypeArrayDecl(int deep, TomList declList) throws IOException;
  protected abstract void generateDeclarationFromList(int deep, TomList declList) throws IOException;
} // class TomAbstractGenerator
