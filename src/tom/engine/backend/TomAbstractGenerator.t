/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

package jtom.backend;

import java.io.IOException;

import jtom.adt.tomsignature.types.*;
import jtom.TomBase;
import jtom.tools.OutputCode;
import jtom.exception.TomRuntimeException;
 
public abstract class TomAbstractGenerator extends TomBase {
  
  protected OutputCode output;
  protected String debugKey;
  public TomAbstractGenerator(OutputCode output) {
    super();
    this.output = output;
  }

// ------------------------------------------------------------
  %include { adt/TomSignature.tom }
// ------------------------------------------------------------

    /*
     * Generate the goal language     */
 
  protected void generate(int deep, TomTerm subject)throws IOException {
    %match(TomTerm subject) {
      
      Tom(l) -> {
        generateList(deep,`l);
        return;
      }

      TomInclude(l) -> {
        generateListInclude(deep,`l);
        return;
      }
     
      Ref(term@(Variable|VariableStar)[]) -> {
        buildRef(deep, `term);
        return;
      }

      Ref(term) -> {
        generate(deep, `term);
        return;
      }

      BuildVariable[astName=Name(name)] -> {
        output.write(`name);
        return;
      }

      BuildVariable[astName=PositionName(l)] -> {
        output.write("tom" + numberListToIdentifier(`l));
        return;
      }
  
      BuildTerm(Name(name), argList) -> {
        buildTerm(deep, `name, `argList);
        return;
      }

      l@BuildEmptyList[] |
      l@BuildEmptyArray[] |
      l@(BuildConsList|BuildAppendList|BuildConsArray|BuildAppendArray)[] -> {
        buildListOrArray(deep, `l);
        return;
      }

      FunctionCall(Name(name), argList) -> {
        buildFunctionCall(deep,`name, `argList);
        return;
      }

      Composite(argList) -> {
        generateList(deep,`argList);
        return;
      }
      
      Variable[astName=PositionName(l)] -> {
          /*
           * sans type: re-definition lorsque %variable est utilise
           * avec type: probleme en cas de filtrage dynamique
           */
        output.write("tom" + numberListToIdentifier(`l));
        return;
      }

      Variable[astName=Name(name)] -> {
        output.write(`name);
        return;
      }

      VariableStar[astName=PositionName(l)] -> {
        output.write("tom" + numberListToIdentifier(`l));
        return;  
      }

      VariableStar[astName=Name(name)] -> {
        output.write(`name);
        return;
      }

      TargetLanguageToTomTerm(t) -> {
        generateTargetLanguage(deep,`t);
        return;
      }

      DeclarationToTomTerm(t) -> {
        generateDeclaration(deep,`t);
        return;
      }

      ExpressionToTomTerm(t) -> {
        generateExpression(deep,`t);
        return;
      }

      InstructionToTomTerm(t) -> {
        generateInstruction(deep,`t);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for: " + `t);
        throw new TomRuntimeException("Cannot generate code for: " + `t);
      }
    }
  }

  public void generateExpression(int deep, Expression subject) throws IOException {
    %match(Expression subject) {
      Not(exp) -> {
        buildExpNot(deep, `exp);
        return;
      }

      And(exp1,exp2) -> {
        buildExpAnd(deep, `exp1, `exp2);
        return;
      }

      Or(exp1,exp2) -> {
        buildExpOr(deep, `exp1, `exp2);
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

      IsEmptyList(expList) -> {
        buildExpEmptyList(deep, getTermType(`expList), `expList);
        return;
      }

      IsEmptyArray(expArray, expIndex) -> {
        buildExpEmptyArray(deep, getTermType(`expArray), `expIndex, `expArray);
        return;
      }

      EqualFunctionSymbol(type, exp,
                          Appl[nameList=(Name(tomName))]) -> { // needs to be checked
        buildEqualFunctionSymbol(deep, `type, `exp, `tomName);
        return;
      }

      EqualTerm(type,exp1,exp2) -> {
        `buildExpEqualTerm(deep, type, exp1, exp2);
        return;
      }

      IsFsym(Name(opname), exp) -> {
        buildExpIsFsym(deep, `opname, `exp);
        return;
      }

      Cast(Type(_,tlType@TLType[]),exp) -> {
        buildExpCast(deep, `tlType, `exp);
        return;
      }

      GetSubterm(codomain, exp, Number(number)) -> {
        buildExpGetSubterm(deep, getTermType(`exp), `codomain, `exp, `number);
        return;
      }

      GetSlot(_,Name(opname),slotName, var@Variable[]) -> {
        `buildExpGetSlot(deep, opname, slotName, var);
        return;
      }

      GetHead(codomain,exp) -> {
        `buildExpGetHead(deep, getTermType(exp), codomain, exp);
        return;
      }

      GetTail(exp) -> {
        buildExpGetTail(deep, getTermType(`exp), `exp);
        return;
      }

      AddOne(exp) -> {
        buildAddOne(deep, `exp);
        return;
      }


      GetSize(exp) -> {
        buildExpGetSize(deep, getTermType(`exp), `exp);
        return;
      }

      GetElement(codomain, varName, varIndex) -> {
        buildExpGetElement(deep,getTermType(`varName),`codomain, `varName, `varIndex);
        return;
      }

      GetSliceList(Name(name), varBegin, varEnd) -> {
        buildExpGetSliceList(deep, `name, `varBegin, `varEnd);
        return;
      }

      GetSliceArray(Name(name),varArray,varBegin,expEnd) -> {
        buildExpGetSliceArray(deep, `name, `varArray, `varBegin, `expEnd);
        return;
      }

      TomTermToExpression(t) -> {
        generate(deep,`t);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for expression: " + `t);
        throw new TomRuntimeException("Cannot generate code for expression: " + `t);
      }
    }
  }

  public void generateInstruction(int deep, Instruction subject) throws IOException {
    %match(Instruction subject) {

      TargetLanguageToInstruction(t) -> {
        `generateTargetLanguage(deep,t);
        return;
      }

      TomTermToInstruction(t) -> {
        `generate(deep,t);
        return;
      }

      Nop() -> {
        return;
      }

      MakeFunctionBegin(Name(tomName),SubjectList(varList)) -> {
        `buildFunctionBegin(deep, tomName, varList);
        return;
      }

      MakeFunctionEnd() -> {
        `buildFunctionEnd(deep);
        return;
      }

      EndLocalVariable() -> {
        output.writeln(deep,"do");
        return;
      }
      
      AssignMatchSubject(var@Variable[option=option],exp) -> {
        `buildAssignVar(deep, var, option, exp);
        return;
      }

      Assign((UnamedVariable|UnamedVariableStar)[],_) -> {
        return;
      }

      Assign(var@(Variable|VariableStar)[option=option],exp) -> {
        `buildAssignVar(deep, var, option, exp);
        return;
      }

      LetAssign(var@(Variable|VariableStar)[option=option],exp,body) -> {
        `buildLetAssign(deep, var, option, exp, body);
        return;
      }
      
      (Let|LetRef|LetAssign)((UnamedVariable|UnamedVariableStar)[],_,body) -> {
        `generateInstruction(deep,body);
        return;
      }

      Let(var@(Variable|VariableStar)[option=list,astType=Type[tlType=tlType@TLType[]]],exp,body) -> {
        `buildLet(deep, var, list, tlType, exp, body);
        return;
      }

      LetRef(var@(Variable|VariableStar)[option=list,astType=Type[tlType=tlType@TLType[]]],exp,body) -> {
        `buildLetRef(deep, var, list, tlType, exp, body);
        return;
      }

      AbstractBlock(instList) -> {
        `generateInstructionList(deep, instList);
        return;
      }

      UnamedBlock(instList) -> {
        `buildUnamedBlock(deep, instList);
        return;
      }

      NamedBlock(blockName,instList) -> {
        `buildNamedBlock(deep, blockName, instList);
        return;
      }
      
      IfThenElse(exp,succesList,Nop()) -> {
        `buildIfThenElse(deep, exp,succesList);
        return;
      }

      IfThenElse(exp,Nop(), failureList) -> {
        `buildIfThenElse(deep, Not(exp),failureList);
        return;
      }

      IfThenElse(exp,succesList,failureList) -> {
        `buildIfThenElseWithFailure(deep, exp, succesList, failureList);
        return;
      }

      DoWhile(succes,exp) -> {
        `buildDoWhile(deep, succes,exp);
        return;
      }

      WhileDo(exp,succes) -> {
        `buildWhileDo(deep, exp, succes);
        return;
      }

      /*
      Action(l) -> {
        generateList(deep,l);
        return;
      }

      GuardedAction(l) -> {
        `buildIfThenElse(deep, TrueTL(),Action(l));
        return;
      }
      */

      Return(exp) -> {
        `buildReturn(deep, exp);
        return;
      }

      CompiledMatch(instruction, list) -> {
        `buildCompiledMatch(deep, instruction);
        return;
      }
      
      CompiledPattern(_,instruction) -> {
        `buildInstructionSequence(deep,instruction);
        return;
      }
      
      t -> {
        System.out.println("Cannot generate code for instruction: " + `t);
        throw new TomRuntimeException("Cannot generate code for instruction: " + `t);
      }
    }
  }
  
  public void generateTargetLanguage(int deep, TargetLanguage subject) throws IOException {
    %match(TargetLanguage subject) {
      TL(t,TextPosition[line=startLine], TextPosition[line=endLine]) -> {
	  // output.write(" ");
        output.write(`t, `startLine, `endLine - `startLine);
        return;
      }
      
      ITL(t) -> {
        output.write(`t);
        return;
      }

      Comment(t) -> {
        `buildComment(deep,t);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for TL: " + `t);
        throw new TomRuntimeException("Cannot generate code for TL: " + `t);
      }
    }
  }

  public void generateOption(int deep, Option subject) throws IOException {
    %match(Option subject) {
      DeclarationToOption(decl) -> {
        `generateDeclaration(deep,decl);
        return;
      }
      OriginTracking[] -> { return; }
      DefinedSymbol() -> { return; }
      Constructor[] -> { return; }

      t -> {
        System.out.println("Cannot generate code for option: " + `t);
        throw new TomRuntimeException("Cannot generate code for option: " + `t);
      }
    }
  }
  
  public void generateDeclaration(int deep, Declaration subject) throws IOException {
    %match(Declaration subject) {
      EmptyDeclaration() -> {
        return;
      }
      (SymbolDecl|ArraySymbolDecl|ListSymbolDecl)(Name(tomName)) -> {
        `buildSymbolDecl(deep, tomName);
        return ;
      }

      GetFunctionSymbolDecl(Variable[astName=Name(name),
                                     astType=Type(ASTTomType(type),tlType@TLType[])],
                            tlCode, _) -> {
        `buildGetFunctionSymbolDecl(deep, type, name, tlType, tlCode);
        return;
      }
      
      GetSubtermDecl(Variable[astName=Name(name1), astType=Type(ASTTomType(type1),tlType1@TLType[])],
                     Variable[astName=Name(name2), astType=Type[tlType=tlType2@TLType[]]],
                     tlCode, _) -> {
        `buildGetSubtermDecl(deep, name1, name2, type1, tlType1, tlType2, tlCode);
        return;
      }
      
      IsFsymDecl(Name(tomName),
       Variable[astName=Name(name), astType=Type[tlType=tlType@TLType[]]], tlCode@TL[], _) -> {
        `buildIsFsymDecl(deep, tomName, name, tlType, tlCode);
        return;
      }
 
      GetSlotDecl[astName=Name(tomName),
                  slotName=slotName,
                  variable=Variable[astName=Name(name), astType=Type[tlType=tlType@TLType[]]],
                  tlCode=tlCode@TL[]] -> {
        `buildGetSlotDecl(deep, tomName, name, tlType, tlCode, slotName);
        return;
      }

      CompareFunctionSymbolDecl(Variable[astName=Name(name1), astType=Type(ASTTomType(type1),_)],
                                Variable[astName=Name(name2), astType=Type(ASTTomType(type2),_)],
                                tlCode, _) -> {
        `buildCompareFunctionSymbolDecl(deep, name1, name2, type1, type2, tlCode);
        return;
      }

      TermsEqualDecl(Variable[astName=Name(name1), astType=Type(ASTTomType(type1),_)],
                     Variable[astName=Name(name2), astType=Type(ASTTomType(type2),_)],
                     tlCode, _) -> {
        `buildTermsEqualDecl(deep, name1, name2, type1, type2, tlCode);
        return;
      }
      
      GetHeadDecl[codomain=Type[tlType=codomain], 
                  variable=Variable[astName=Name(name1), astType=Type(ASTTomType(suffix),domain@TLType[])],
                  tlcode=tlCode@TL[]] -> {
        `buildGetHeadDecl(deep, name1, suffix, domain, codomain, tlCode);
        return;
      }

      GetTailDecl(Variable[astName=Name(name1), astType=Type(ASTTomType(type),tlType@TLType[])],
                  tlCode@TL[], _) -> {
        `buildGetTailDecl(deep, name1, type, tlType, tlCode);
        return;
      }

      IsEmptyDecl(Variable[astName=Name(name1), astType=Type(ASTTomType(type),tlType@TLType[])],
                  tlCode@TL[], _) -> {
        `buildIsEmptyDecl(deep, name1, type, tlType, tlCode);
        return;
      }

      MakeEmptyList(Name(opname), tlCode@TL[], _) -> {
        //System.out.println("symbol = " + getSymbol(opname));
        TomType codomain = `getSymbolCodomain(getSymbol(opname));
        `buildMakeEmptyList(deep, opname, codomain, tlCode);
        return;
      }

      MakeAddList(Name(opname),
                  Variable[astName=Name(name1), astType=fullEltType@Type[tlType=tlType1@TLType[]]],
                  Variable[astName=Name(name2), astType=fullListType@Type[tlType=tlType2@TLType[]]],
                  tlCode@TL[], _) -> {
        `buildMakeAddList(deep, opname, name1, name2, tlType1, tlType2, fullEltType, fullListType, tlCode);
        return;
      }

      GetElementDecl(Variable[astName=Name(name1), astType=Type[tomType=ASTTomType(type1),tlType=tlType1@TLType[]]],
                     Variable[astName=Name(name2)],
                     tlCode@TL[], _) -> {
        `buildGetElementDecl(deep, name1, name2, type1, tlType1, tlCode);
        return;
      }
      
      GetSizeDecl(Variable[astName=Name(name), astType=Type(ASTTomType(type),tlType@TLType[])],
                  tlCode@TL[], _) -> {
        `buildGetSizeDecl(deep, name, type, tlType, tlCode);
        return;
      }
      
      MakeEmptyArray(Name(opname),
                     Variable[astName=Name(name)],
                     tlCode@TL[], _) -> {
        TomType codomain = `getSymbolCodomain(getSymbol(opname));
        `buildMakeEmptyArray(deep, opname, codomain, name, tlCode);
        return;
      }

      MakeAddArray(Name(opname),
                   Variable[astName=Name(name1), astType=fullEltType@Type[tlType=tlType1@TLType[]]],
                   Variable[astName=Name(name2), astType=fullArrayType@Type[tlType=tlType2@TLType[]]],
                   tlCode@TL[], _) -> {
        `buildMakeAddArray(deep, opname, name1, name2, tlType1, tlType2, fullEltType, fullArrayType, tlCode);
        return;
      }

      MakeDecl(Name(opname), returnType, argList, tlCode@TL[], _) -> {
        `generateTargetLanguage(deep, genDeclMake(opname, returnType, argList, tlCode));
        return;
      }
      
      TypeTermDecl[keywordList=declList] -> {
        `buildTypeTermDecl(deep, declList);
        return;
      }

      TypeListDecl[keywordList=declList] -> { 
        `buildTypeListDecl(deep, declList);
        return;
      }

      TypeArrayDecl[keywordList=declList] -> { 
        `buildTypeArrayDecl(deep, declList);
        return;
      }
      
      t -> {
        System.out.println("Cannot generate code for declaration: " + `t);
        throw new TomRuntimeException("Cannot generate code for declaration: " + `t);
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
  protected abstract void buildListOrArray(int deep, TomTerm list) throws IOException;

  protected abstract void buildFunctionCall(int deep, String name, TomList argList)  throws IOException;
  protected abstract void buildFunctionBegin(int deep, String tomName, TomList varList) throws IOException; 
  protected abstract void buildFunctionEnd(int deep) throws IOException;
  protected abstract void buildExpNot(int deep, Expression exp) throws IOException;

  protected abstract void buildCompiledMatch(int deep, Instruction instruction) throws IOException;
  protected abstract void buildExpAnd(int deep, Expression exp1, Expression exp2) throws IOException;
  protected abstract void buildExpOr(int deep, Expression exp1, Expression exp2) throws IOException;
  protected abstract void buildExpTrue(int deep) throws IOException;
  protected abstract void buildExpFalse(int deep) throws IOException;
  protected abstract void buildExpEmptyList(int deep, TomType type, TomTerm expList) throws IOException;
  protected abstract void buildExpEmptyArray(int deep, TomType type, TomTerm expIndex, TomTerm expArray) throws IOException;
  protected abstract void buildEqualFunctionSymbol(int deep, TomType type1, TomTerm var, String tomName) throws IOException;
  protected abstract void buildExpEqualTerm(int deep, TomType type, TomTerm exp1,TomTerm exp2) throws IOException;
  protected abstract void buildExpIsFsym(int deep, String opname, TomTerm var) throws IOException;
  protected abstract void buildExpCast(int deep, TomType type, Expression exp) throws IOException;
  protected abstract void buildExpGetSubterm(int deep, TomType domain, TomType codomain, TomTerm exp, int number) throws IOException;
  protected abstract void buildExpGetSlot(int deep, String opname, String slotName, TomTerm exp) throws IOException;
  protected abstract void buildExpGetHead(int deep, TomType doamin, TomType codomain, TomTerm var) throws IOException;
  protected abstract void buildExpGetTail(int deep, TomType type1, TomTerm var) throws IOException;
  protected abstract void buildExpGetSize(int deep, TomType type1, TomTerm var) throws IOException;
  protected abstract void buildExpGetElement(int deep, TomType domain, TomType codomain, TomTerm varName, TomTerm varIndex) throws IOException;
  protected abstract void buildExpGetSliceList(int deep, String name, TomTerm varBegin, TomTerm varEnd) throws IOException;
  protected abstract void buildExpGetSliceArray(int deep, String name, TomTerm varArray, TomTerm varBegin, TomTerm expEnd) throws IOException;
  protected abstract void buildAssignVar(int deep, TomTerm var, OptionList list, Expression exp) throws IOException ;
  protected abstract void buildLetAssign(int deep, TomTerm var, OptionList list, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildLet(int deep, TomTerm var, OptionList list, TomType tlType, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildLetRef(int deep, TomTerm var, OptionList list, TomType tlType, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildNamedBlock(int deep, String blockName, InstructionList instList) throws IOException ;
  protected abstract void buildUnamedBlock(int deep, InstructionList instList) throws IOException ;
  protected abstract void buildIfThenElse(int deep, Expression exp, Instruction succes) throws IOException ;
  protected abstract void buildIfThenElseWithFailure(int deep, Expression exp, Instruction succes, Instruction failure) throws IOException ;
  protected abstract void buildDoWhile(int deep, Instruction succes, Expression exp) throws IOException;
  protected abstract void buildWhileDo(int deep, Expression exp, Instruction succes) throws IOException;
  protected abstract void buildAddOne(int deep, TomTerm var) throws IOException;
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
  protected abstract void buildGetHeadDecl(int deep, String name1, String suffix, TomType domain, TomType codomain,TargetLanguage tlCode) throws IOException;
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
