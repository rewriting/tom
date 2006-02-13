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

import tom.engine.TomBase;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;
 
public abstract class TomAbstractGenerator extends TomBase {
  
  protected OutputCode output;
  protected OptionManager optionManager;
  protected SymbolTable symbolTable;

  public TomAbstractGenerator(OutputCode output, OptionManager optionManager,
                              SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
    this.optionManager = optionManager;
    this.output = output;
  }
  
  protected SymbolTable getSymbolTable() {
    return symbolTable;
  }
  
  protected TomSymbol getSymbolFromName(String tomName) {
    return getSymbolFromName(tomName, symbolTable);
  }

  protected TomSymbol getSymbolFromType(TomType tomType) {
    return getSymbolFromType(tomType, symbolTable);
  }

  protected TomType getTermType(TomTerm t) {
    return  getTermType(t, symbolTable);
  }

  protected TomType getUniversalType() {
    return symbolTable.getUniversalType();
  }
// ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
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

      l@(BuildEmptyList|BuildEmptyArray|BuildConsList|BuildAppendList|BuildConsArray|BuildAppendArray)[] -> {
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

      /*TLVar[strName=name] -> {
	      output.write(`name);
	      return;
      }*/

      TargetLanguageToTomTerm(t) -> {
        generateTargetLanguage(deep,`t);
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

      DeclarationToTomTerm(t) -> {
        generateDeclaration(deep,`t);
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
      Negation(exp) -> {
        buildExpNegation(deep, `exp);
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

      GreaterThan(exp1,exp2) -> {
        buildExpGreaterThan(deep, `exp1, `exp2);
        return;
      }

      Bottom() -> {
        buildExpBottom(deep);
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

      IsEmptyList(opNameAST, expList) -> {
        buildExpIsEmptyList(deep, `opNameAST, getTermType(`expList), `expList);
        return;
      }

      IsEmptyArray(opNameAST, expArray, expIndex) -> {
        buildExpIsEmptyArray(deep, `opNameAST, getTermType(`expArray), `expIndex, `expArray);
        return;
      }

      EqualFunctionSymbol(type, exp, RecordAppl[nameList=(nameAST@Name(opName))]) -> { 
        if(getSymbolTable().isBuiltinType(getTomType(`type))) {
          TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(`opName);
          if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
            generateExpression(deep,`IsFsym(nameAST,exp));
          } else {
            generateExpression(deep,`EqualTerm(type,BuildVariable(nameAST,emptyTomList()),exp));
          }
        } else {
          generateExpression(deep,`IsFsym(nameAST,exp));
        }
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

      GetSlot(_,Name(opname),slotName, var@Variable[]) -> {
        `buildExpGetSlot(deep, opname, slotName, var);
        return;
      }

      GetHead(opNameAST, codomain,exp) -> {
        `buildExpGetHead(deep, opNameAST, getTermType(exp), codomain, exp);
        return;
      }

      GetTail(opNameAST, exp) -> {
        buildExpGetTail(deep, `opNameAST, getTermType(`exp), `exp);
        return;
      }

      AddOne(exp) -> {
        buildAddOne(deep, `exp);
        return;
      }

      GetSize(opNameAST,exp) -> {
        buildExpGetSize(deep,`opNameAST,getTermType(`exp), `exp);
        return;
      }

      GetElement(opNameAST,codomain, varName, varIndex) -> {
        buildExpGetElement(deep,`opNameAST,getTermType(`varName),`codomain, `varName, `varIndex);
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

      FunctionDef(Name(tomName),argList,codomain,throwsType,instruction) -> {
        `buildFunctionDef(deep, tomName, argList, codomain, throwsType, instruction);
        return;
      }

      Class(Name(tomName),extendsFwdType,instruction) -> {
        `buildClass(deep, tomName, extendsFwdType,instruction);
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
        //`generateInstructionList(deep, instList);
        `buildInstructionSequence(deep, instList);
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
      
      If(exp,succesList,Nop()) -> {
        `buildIf(deep, exp,succesList);
        return;
      }

      If(exp,Nop(), failureList) -> {
        `buildIf(deep, Negation(exp),failureList);
        return;
      }

      If(exp,succesList,failureList) -> {
        `buildIfWithFailure(deep, exp, succesList, failureList);
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

      TypedAction[astInstruction=AbstractBlock(instructionList)] -> {
        `generateInstructionList(deep, instructionList);
        return;
      }

      TypedAction[astInstruction=inst] -> {
        `generateInstruction(deep, inst);
        return;
      }

      Return(exp) -> {
        `buildReturn(deep, exp);
        return;
      }

      CompiledMatch(instruction, list) -> {
        `generateInstruction(deep, instruction);
        return;
      }
      
      CompiledPattern(_,instruction) -> {
        `generateInstruction(deep, instruction);
        return;
      }

      CheckStamp(variable) -> {
        `buildCheckStamp(deep, getTermType(variable), variable);
        return;
      }
      
			CheckInstance(Type[tomType=ASTTomType(typeName),tlType=tlType@TLType[]], exp, instruction) -> {
        `buildCheckInstance(deep,typeName,tlType,exp,instruction); 
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

      SymbolDecl(Name(tomName)) -> {
        `buildSymbolDecl(deep, tomName);
        return ;
      }

      ArraySymbolDecl(Name(tomName)) -> {
        `buildSymbolDecl(deep, tomName);
        `genDeclArray(tomName);
        return ;
      }

      ListSymbolDecl(Name(tomName)) -> {
        `buildSymbolDecl(deep, tomName);
        `genDeclList(tomName);
        return ;
      }

      CheckStampDecl(Variable[astName=Name(name), 
                              astType=Type(ASTTomType(type),tlType@TLType[])], 
                     //astType=Type((type),tlType@TLType[])], 
                     instr, _) -> {
        `buildCheckStampDecl(deep, type, name, tlType, instr);
        return;
      }
      
      SetStampDecl(Variable[astName=Name(name), 
                              astType=Type(ASTTomType(type),tlType@TLType[])], 
                     instr, _) -> {
        `buildSetStampDecl(deep, type, name, tlType, instr);
        return;
      }

      GetImplementationDecl(Variable[astName=Name(name), 
                              astType=Type(ASTTomType(type),tlType@TLType[])], 
                     instr, _) -> {
        `buildGetImplementationDecl(deep, type, name, tlType, instr);
        return;
      }

      IsFsymDecl(Name(tomName),
       Variable[astName=Name(name), astType=Type[tlType=tlType@TLType[]]], instr, _) -> {
        `buildIsFsymDecl(deep, tomName, name, tlType, instr);
        return;
      }

      GetSlotDecl[astName=Name(tomName),
                  slotName=slotName,
                  variable=Variable[astName=Name(name), astType=Type[tlType=tlType@TLType[]]],
                  instr=instr] -> {
        `buildGetSlotDecl(deep, tomName, name, tlType, instr, slotName);
        return;
      }

      TermsEqualDecl(Variable[astName=Name(name1), astType=Type(ASTTomType(type1),_)],
                     Variable[astName=Name(name2), astType=Type(ASTTomType(type2),_)],
                     instr, _) -> {
        `buildTermsEqualDecl(deep, name1, name2, type1, type2, instr);
        return;
      }
      
      GetHeadDecl[opname=opNameAST,
                  codomain=Type[tlType=codomain], 
                  variable=Variable[astName=Name(varName), astType=Type(ASTTomType(suffix),domain@TLType[])],
                  instr=instr] -> {
        `buildGetHeadDecl(deep, opNameAST, varName, suffix, domain, codomain, instr);
        return;
      }

      GetTailDecl[opname=opNameAST,
                  variable=Variable[astName=Name(varName), astType=Type(ASTTomType(type),tlType@TLType[])],
                  instr=instr] -> {
        `buildGetTailDecl(deep, opNameAST, varName, type, tlType, instr);
        return;
      }

      IsEmptyDecl[opname=opNameAST,
                  variable=Variable[astName=Name(varName), astType=Type(ASTTomType(type),tlType@TLType[])],
                  instr=instr] -> {
        `buildIsEmptyDecl(deep, opNameAST, varName, type, tlType, instr);
        return;
      }

      MakeEmptyList(Name(opname), instr, _) -> {
        TomType returnType = `getSymbolCodomain(getSymbolFromName(opname));
        `genDeclMake("tom_empty_list_" + opname, returnType, concTomTerm(), instr);
        return;
      }

      MakeAddList(Name(opname),
                  elt@Variable[astType=fullEltType@Type[tlType=tlType1@TLType[]]],
                  list@Variable[astType=fullListType@Type[tlType=tlType2@TLType[]]],
                  instr, _) -> {
        TomType returnType = `fullListType;
        `genDeclMake("tom_cons_list_" + opname, returnType, concTomTerm(elt,list), instr);
        return;
      }

      GetElementDecl[opname=opNameAST,
                     variable=Variable[astName=Name(name1), astType=Type[tomType=ASTTomType(type1),tlType=tlType1@TLType[]]],
                     index=Variable[astName=Name(name2)],
                     instr=instr] -> {
        `buildGetElementDecl(deep, opNameAST, name1, name2, type1, tlType1, instr);
        return;
      }
      
      GetSizeDecl[opname=opNameAST,
                  variable=Variable[astName=Name(name), astType=Type(ASTTomType(type),tlType@TLType[])],
                  instr=instr] -> {
        `buildGetSizeDecl(deep, opNameAST, name, type, tlType, instr);
        return;
      }
      
      MakeEmptyArray(Name(opname),
                     Variable[option=option,astName=name,astType=type, constraints=constraints],
                     instr, _) -> {
        TomType returnType = `getSymbolCodomain(getSymbolFromName(opname));
        TomTerm newVar = `Variable(option, name, getSymbolTable().getIntType(), constraints);
        `genDeclMake("tom_empty_array_" + opname, returnType, concTomTerm(newVar), instr);
        return;
      }

      MakeAddArray(Name(opname),
                   elt@Variable[astType=fullEltType@Type[tlType=tlType1@TLType[]]],
                   list@Variable[astType=fullArrayType@Type[tlType=tlType2@TLType[]]],
                   instr, _) -> {
        TomType returnType = `fullArrayType;
        `genDeclMake("tom_cons_array_" + opname, returnType, concTomTerm(elt,list), instr);
        return;
      }

      MakeDecl(Name(opname), returnType, argList, instr, _) -> {
        `genDeclMake("tom_make_" + opname, returnType, argList, instr);
        return;
      }
      
      TypeTermDecl[declarations=declList] -> {
        generateDeclarationList(deep, `declList);
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

  public void generateDeclarationList(int deep, DeclarationList subject)
    throws IOException {
    while(!subject.isEmpty()) {
      generateDeclaration(deep,subject.getHead());
      subject = subject.getTail();
    }
  }

  public void generatePairNameDeclList(int deep, PairNameDeclList pairNameDeclList)
    throws IOException {
    while ( !pairNameDeclList.isEmpty() ) {
      generateDeclaration(deep, pairNameDeclList.getHead().getSlotDecl());
      pairNameDeclList = pairNameDeclList.getTail();
    }
  }
   
  
    // ------------------------------------------------------------
  
  protected abstract void genDecl(String returnType,
                                  String declName,
                                  String suffix,
                                  String args[],
                                  TargetLanguage tlCode) throws IOException;

  protected abstract void genDeclInstr(String returnType,
                                       String declName,
                                       String suffix,
                                       String args[],
                                       Instruction instr,
                                       int deep) throws IOException;
  
  protected abstract void genDeclMake(String funName, TomType returnType, 
                                      TomList argList, Instruction instr) throws IOException;
  
  protected abstract void genDeclList(String name) throws IOException;

  protected abstract void genDeclArray(String name) throws IOException;
 
  // ------------------------------------------------------------
  
  protected abstract void buildInstructionSequence(int deep, InstructionList instructionList) throws IOException;
  protected abstract void buildSemiColon() throws IOException;
  protected abstract void buildComment(int deep, String text) throws IOException;
  protected abstract void buildTerm(int deep, String name, TomList argList) throws IOException;
  protected abstract void buildRef(int deep, TomTerm term) throws IOException;
  protected abstract void buildListOrArray(int deep, TomTerm list) throws IOException;

  protected abstract void buildFunctionCall(int deep, String name, TomList argList)  throws IOException;
  protected abstract void buildFunctionBegin(int deep, String tomName, TomList varList) throws IOException; 
  protected abstract void buildFunctionEnd(int deep) throws IOException;
  protected abstract void buildFunctionDef(int deep, String tomName, TomList argList, TomType codomain, TomType throwsType, Instruction instruction) throws IOException; 

  /*buildClass is not abstract since only Java backend supports class
    only backends that supports Class should overload buildClass  
   */
  protected void buildClass(int deep, String tomName, TomForwardType extendsFwdType, Instruction instruction) throws IOException {
    System.out.println("Backend does not support Class");
    throw new TomRuntimeException("Backend does not support Class");
  }

  protected abstract void buildExpNegation(int deep, Expression exp) throws IOException;

  protected abstract void buildExpAnd(int deep, Expression exp1, Expression exp2) throws IOException;
  protected abstract void buildExpOr(int deep, Expression exp1, Expression exp2) throws IOException;
  protected abstract void buildExpGreaterThan(int deep, Expression exp1, Expression exp2) throws IOException;
  protected abstract void buildExpBottom(int deep) throws IOException;
  protected abstract void buildExpTrue(int deep) throws IOException;
  protected abstract void buildExpFalse(int deep) throws IOException;
  protected abstract void buildExpIsEmptyList(int deep, TomName opName, TomType type, TomTerm expList) throws IOException;
  protected abstract void buildExpIsEmptyArray(int deep, TomName opName, TomType type, TomTerm expIndex, TomTerm expArray) throws IOException;
  protected abstract void buildExpEqualTerm(int deep, TomType type, TomTerm exp1,TomTerm exp2) throws IOException;
  protected abstract void buildExpIsFsym(int deep, String opname, TomTerm var) throws IOException;
  protected abstract void buildExpCast(int deep, TomType type, Expression exp) throws IOException;
  protected abstract void buildExpGetSlot(int deep, String opname, String slotName, TomTerm exp) throws IOException;
  protected abstract void buildExpGetHead(int deep, TomName opName, TomType domain, TomType codomain, TomTerm var) throws IOException;
  protected abstract void buildExpGetTail(int deep, TomName opName, TomType type1, TomTerm var) throws IOException;
  protected abstract void buildExpGetSize(int deep, TomName opNameAST, TomType type1, TomTerm var) throws IOException;
  protected abstract void buildExpGetElement(int deep, TomName opNameAST, TomType domain, TomType codomain, TomTerm varName, TomTerm varIndex) throws IOException;
  protected abstract void buildExpGetSliceList(int deep, String name, TomTerm varBegin, TomTerm varEnd) throws IOException;
  protected abstract void buildExpGetSliceArray(int deep, String name, TomTerm varArray, TomTerm varBegin, TomTerm expEnd) throws IOException;
  protected abstract void buildAssignVar(int deep, TomTerm var, OptionList list, Expression exp) throws IOException ;
  protected abstract void buildLetAssign(int deep, TomTerm var, OptionList list, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildLet(int deep, TomTerm var, OptionList list, TomType tlType, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildLetRef(int deep, TomTerm var, OptionList list, TomType tlType, Expression exp, Instruction body) throws IOException ;
  protected abstract void buildNamedBlock(int deep, String blockName, InstructionList instList) throws IOException ;
  protected abstract void buildUnamedBlock(int deep, InstructionList instList) throws IOException ;
  protected abstract void buildIf(int deep, Expression exp, Instruction succes) throws IOException ;
  protected abstract void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure) throws IOException ;
  protected abstract void buildDoWhile(int deep, Instruction succes, Expression exp) throws IOException;
  protected abstract void buildWhileDo(int deep, Expression exp, Instruction succes) throws IOException;
  protected abstract void buildAddOne(int deep, TomTerm var) throws IOException;
  protected abstract void buildReturn(int deep, TomTerm exp) throws IOException ;
  protected abstract void buildCheckStamp(int deep, TomType type, TomTerm variable) throws IOException ;
  protected abstract void buildCheckInstance(int deep, String typeName,TomType type, Expression exp, Instruction instruction) throws IOException ;
  protected abstract void buildSymbolDecl(int deep, String tomName) throws IOException ;
  protected abstract void buildCheckStampDecl(int deep, String type, String name,
                                              TomType tlType, Instruction instr) throws IOException;
  protected abstract void buildSetStampDecl(int deep, String type, String name,
                                              TomType tlType, Instruction instr) throws IOException;
  protected abstract void buildGetImplementationDecl(int deep, String type, String name,
                                              TomType tlType, Instruction instr) throws IOException;

  protected abstract void buildIsFsymDecl(int deep, String tomName, String name1,
                                          TomType tlType, Instruction instr) throws IOException;
  protected abstract void buildGetSlotDecl(int deep, String tomName, String name1,
                                           TomType tlType, Instruction instr, TomName slotName) throws IOException;
  protected abstract void buildTermsEqualDecl(int deep, String name1, String name2,
                                              String type1, String type2, Instruction instr) throws IOException;
  protected abstract void buildGetHeadDecl(int deep, TomName opNameAST, String varName, String suffix, TomType domain, TomType codomain, Instruction instr) throws IOException;
  protected abstract void buildGetTailDecl(int deep, TomName opNameAST, String varName, String type, TomType tlType, Instruction instr) throws IOException;
  protected abstract void buildIsEmptyDecl(int deep, TomName opNameAST, String varName, String type,
                                           TomType tlType, Instruction instr) throws IOException;
  protected abstract void buildGetElementDecl(int deep, TomName opNameAST, String name1, String name2,
                                              String type1, TomType tlType1, Instruction instr) throws IOException;
  protected abstract void buildGetSizeDecl(int deep, TomName opNameAST, String name1, String type,
                                           TomType tlType, Instruction instr) throws IOException;
 
} // class TomAbstractGenerator
