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

import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;

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

import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;

import aterm.*;

public abstract class TomAbstractGenerator {

  protected OutputCode output;
  protected OptionManager optionManager;
  protected SymbolTable symbolTable;
  protected boolean prettyMode;

  public TomAbstractGenerator(OutputCode output, OptionManager optionManager,
                              SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
    this.optionManager = optionManager;
    this.output = output;
    this.prettyMode = ((Boolean)optionManager.getOptionValue("pretty")).booleanValue();
  }

  protected SymbolTable getSymbolTable(String moduleName) {
    //TODO//
    //Using of the moduleName
    ////////
    return symbolTable;
  }

  protected TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, symbolTable);
  }

  protected TomSymbol getSymbolFromType(TomType tomType) {
    return TomBase.getSymbolFromType(tomType, symbolTable);
  }

  protected TomType getTermType(TomTerm t) {
    return TomBase.getTermType(t, symbolTable);
  }

  protected TomType getUniversalType() {
    return symbolTable.getUniversalType();
  }
// ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
// ------------------------------------------------------------

  protected TomTerm operatorsTogenerate(TomTerm subject)throws IOException {
    //System.out.println("Subject "+subject);
    //collectMake(subject);
    return subject;
  }

  /**
   * Generate the goal language
   * 
   * @param deep 
   * 		The distance from the right side (allows the computation of the column number)
   */
  protected void generate(int deep, TomTerm subject, String moduleName) throws IOException {
    %match(subject) {

      Tom(l) -> {
        generateList(deep,`l, moduleName);
        return;
      }

      TomInclude(l) -> {
        generateListInclude(deep,`l, moduleName);
        return;
      }

      BuildConstant[AstName=Name(name)] -> {
        if(`name.charAt(0)=='\'' && `name.charAt(`name.length()-1)=='\'') {
          String substring = `name.substring(1,`name.length()-1);
          //System.out.println("BuildConstant: " + substring);
          substring = substring.replace("\\","\\\\"); // replace backslash by backslash-backslash
          substring = substring.replace("'","\\'"); // replace quote by backslash-quote
          output.write("'" + substring + "'");
          return;
        }
        output.write(`name);
        return;
      }

      BuildTerm(Name(name), argList, myModuleName) -> {
        `buildTerm(deep, name, argList, myModuleName);
        return;
      }

      l@(BuildEmptyList|BuildEmptyArray|BuildConsList|BuildAppendList|BuildConsArray|BuildAppendArray)[] -> {
        buildListOrArray(deep, `l, moduleName);
        return;
      }

      FunctionCall[AstName=Name(name), Args=argList] -> {
        buildFunctionCall(deep,`name, `argList, moduleName);
        return;
      }

      Composite(argList) -> {
        generateList(deep,`argList, moduleName);
        return;
      }
      
      var@(Variable|VariableStar)[] -> {
        output.write(deep,getVariableName(`var));
        return;
      }

      TargetLanguageToTomTerm(t) -> {
        generateTargetLanguage(deep,`t, moduleName);
        return;
      }

      ExpressionToTomTerm(t) -> {
        generateExpression(deep,`t, moduleName);
        return;
      }

      InstructionToTomTerm(t) -> {
        generateInstruction(deep,`t, moduleName);
        return;
      }

      DeclarationToTomTerm(t) -> {
        generateDeclaration(deep,`t, moduleName);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for: " + `t);
        throw new TomRuntimeException("Cannot generate code for: " + `t);
      }
    }
  }

  protected String getVariableName(TomTerm var) {
    %match(var) {
      Variable[AstName=PositionName(l)] -> {
        return ("tom" + TomBase.tomNumberListToString(`l));
      }

      Variable[AstName=Name(name)] -> {
        return `name;
      }

      VariableStar[AstName=PositionName(l)] -> {
        return ("tom" + TomBase.tomNumberListToString(`l));
      }

      VariableStar[AstName=Name(name)] -> {
        return `name;
      }
    }
    return null;
  }
  public void generateExpression(int deep, Expression subject, String moduleName) throws IOException {
    %match(subject) {
      Code(t) -> {
        output.write(`t);
        return;
      }

      Integer(n) -> {
        output.write(`n);
        return;
      }

      Negation(exp) -> {
        buildExpNegation(deep, `exp, moduleName);
        return;
      }

      Conditional(cond,exp1,exp2) -> {
        buildExpConditional(deep, `cond, `exp1, `exp2, moduleName);
        return;
      }

      And(exp1,exp2) -> {
        buildExpAnd(deep, `exp1, `exp2, moduleName);
        return;
      }

      Or(exp1,exp2) -> {
        buildExpOr(deep, `exp1, `exp2, moduleName);
        return;
      }

      GreaterThan(exp1,exp2) -> {
        buildExpGreaterThan(deep, `exp1, `exp2, moduleName);
        return;
      }
      
      GreaterOrEqualThan(exp1,exp2) -> {
        buildExpGreaterOrEqualThan(deep, `exp1, `exp2, moduleName);
        return;
      }
      
      LessThan(exp1,exp2) -> {
        buildExpLessThan(deep, `exp1, `exp2, moduleName);
        return;
      }
      
      LessOrEqualThan(exp1,exp2) -> {
        buildExpLessOrEqualThan(deep, `exp1, `exp2, moduleName);
        return;
      }


      Bottom(tomType) -> {
        buildExpBottom(deep,`tomType, moduleName);
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

      IsEmptyList(Name(opName), expList) -> {
        `buildExpIsEmptyList(deep, opName, getTermType(expList), expList, moduleName);
        return;
      }

      IsEmptyArray(opNameAST, expArray, expIndex) -> {
        buildExpIsEmptyArray(deep, `opNameAST, getTermType(`expArray), `expIndex, `expArray, moduleName);
        return;
      }

      EqualTerm(type,exp1,exp2) -> {
        `buildExpEqualTerm(deep, type, exp1, exp2, moduleName);
        return;
      }

      IsSort((Type|TypeWithSymbol)[TomType=type], exp) -> {
        `buildExpIsSort(deep,type,exp,moduleName);
        return;
      }

      IsFsym(Name(opname), exp) -> {
        buildExpIsFsym(deep, `opname, `exp, moduleName);
        return;
      }

      Cast((Type|TypeWithSymbol)[TlType=tlType@TLType[]],exp) -> {
        buildExpCast(deep, `tlType, `exp, moduleName);
        return;
      }

      Cast(tlType@TLType[],exp) -> {
        buildExpCast(deep, `tlType, `exp, moduleName);
        return;
      }

      GetSlot(_,Name(opname),slotName, var@(Variable|BuildTerm|ExpressionToTomTerm)[]) -> {    	  
        `buildExpGetSlot(deep, opname, slotName, var, moduleName);
        return;
      }
      GetSlot(_,Name(opname),slotName, var@ExpressionToTomTerm(GetSlot[])) -> {    	  
        `buildExpGetSlot(deep, opname, slotName, var, moduleName);
        return;
      }

      GetHead(Name(opName),codomain,exp) -> {
        `buildExpGetHead(deep, opName, getTermType(exp), codomain, exp, moduleName);
        return;
      }

      GetTail(Name(opName), exp) -> {
        `buildExpGetTail(deep, opName, getTermType(exp), exp, moduleName);
        return;
      }

      AddOne(exp) -> {
        buildAddOne(deep, `exp, moduleName);
        return;
      }

      SubstractOne(exp) -> {
        buildSubstractOne(deep, `exp, moduleName);
        return;
      }

      Substract(exp1,exp2) -> {
        buildSubstract(deep, `exp1, `exp2, moduleName);
        return;
      }

      GetSize(opNameAST,exp) -> {
        buildExpGetSize(deep,`opNameAST,getTermType(`exp), `exp, moduleName);
        return;
      }

      GetElement(opNameAST,codomain, varName, varIndex) -> {
        buildExpGetElement(deep,`opNameAST,getTermType(`varName),`codomain, `varName, `varIndex, moduleName);
        return;
      }

      GetSliceList(Name(name), varBegin, varEnd, tailSlice) -> {
        buildExpGetSliceList(deep, `name, `varBegin, `varEnd, `tailSlice,moduleName);
        return;
      }
      
      GetSliceArray(Name(name),varArray,varBegin,expEnd) -> {
        buildExpGetSliceArray(deep, `name, `varArray, `varBegin, `expEnd, moduleName);
        return;
      }

      TomTermToExpression(t) -> {
        generate(deep,`t, moduleName);
        return;
      }      
      
      TomInstructionToExpression(t) -> {
        generateInstruction(deep, `t, moduleName);
        return;
      }
      
      t -> {
        System.out.println("Cannot generate code for expression: " + `t);
        throw new TomRuntimeException("Cannot generate code for expression: " + `t);
      }
    }
  }

  /**
   * generates var[index] 
   */
  protected void generateArray(int deep, TomTerm subject, TomTerm index, String moduleName) throws IOException {
    %match(TomTerm subject) {
      Variable[AstName=PositionName(l)] -> {        
        output.write("tom" + TomBase.tomNumberListToString(`l));        
      }
      Variable[AstName=Name(name)] -> {
        output.write(`name);        
      }
    }
    %match(TomTerm index) {
      Variable[AstName=PositionName(l)] -> {
        output.write("[");
        output.write("tom" + TomBase.tomNumberListToString(`l));
        output.write("]");        
      }
      Variable[AstName=Name(name)] -> {
        output.write("[");
        output.write(`name);
        output.write("]");
      }
      ExpressionToTomTerm(Integer(x)) -> {
        output.write("[");
        output.write(`x);
        output.write("]");  
      }
    }    
  } 

  public void generateInstruction(int deep, Instruction subject, String moduleName) throws IOException {
    %match(subject) {

      TargetLanguageToInstruction(t) -> {
        `generateTargetLanguage(deep, t, moduleName);
        return;
      }

      TomTermToInstruction(t) -> {
        `generate(deep, t, moduleName);
        return;
      }

      ExpressionToInstruction(t) -> {
        `generateExpression(deep, t, moduleName);
        return;
      }

      Nop() -> {
        return;
      }

      Assign(var@(Variable|VariableStar)[Option=option],exp) -> {
        `buildAssign(deep, var, option, exp, moduleName);
        return;
      }

      Assign((UnamedVariable|UnamedVariableStar)[],_) -> {
        return;
      }

      AssignArray(var@Variable[Option=list],index,exp) -> {
        `buildAssignArray(deep, var, list, index, exp, moduleName);
        return;
      }  

      (Let|LetRef)((UnamedVariable|UnamedVariableStar)[],_,body) -> {
        `generateInstruction(deep, body, moduleName);
        return;
      }

      Let(var@(Variable|VariableStar)[Option=list,AstType=(Type|TypeWithSymbol)[TlType=tlType]],exp,body) -> {
        `buildLet(deep, var, list, tlType, exp, body, moduleName);
        return;
      }

      LetRef(var@(Variable|VariableStar)[Option=list,AstType=(Type|TypeWithSymbol)[TlType=tlType]],exp,body) -> {
        `buildLetRef(deep, var, list, tlType, exp, body, moduleName);
        return;
      }

      AbstractBlock(instList) -> {
        //`generateInstructionList(deep, instList);
        `buildInstructionSequence(deep, instList, moduleName);
        return;
      }

      UnamedBlock(instList) -> {
        `buildUnamedBlock(deep, instList, moduleName);
        return;
      }

      NamedBlock(blockName,instList) -> {
        `buildNamedBlock(deep, blockName, instList, moduleName);
        return;
      }

      If(exp,succesList,Nop()) -> {
        `buildIf(deep, exp,succesList, moduleName);
        return;
      }

      If(exp,Nop(), failureList) -> {
        `buildIf(deep, Negation(exp),failureList, moduleName);
        return;
      }

      If(exp,succesList,failureList) -> {
        `buildIfWithFailure(deep, exp, succesList, failureList, moduleName);
        return;
      }

      DoWhile(succes,exp) -> {
        `buildDoWhile(deep, succes,exp, moduleName);
        return;
      }

      WhileDo(exp,succes) -> {
        `buildWhileDo(deep, exp, succes, moduleName);
        return;
      }

      TypedAction[AstInstruction=AbstractBlock(instructionList)] -> {
        `generateInstructionList(deep, instructionList, moduleName);
        return;
      }

      TypedAction[AstInstruction=inst] -> {
        `generateInstruction(deep, inst, moduleName);
        return;
      }

      Return(exp) -> {
        `buildReturn(deep, exp, moduleName);
        return;
      }

      CompiledMatch(instruction, _) -> {
        //TODO moduleName
        `generateInstruction(deep, instruction, moduleName);
        return;
      }

      CompiledPattern(_,instruction) -> {
        `generateInstruction(deep, instruction, moduleName);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for instruction: " + `t);
        throw new TomRuntimeException("Cannot generate code for instruction: " + `t);
      }
    }
  }

  public void generateTargetLanguage(int deep, TargetLanguage subject, String moduleName) throws IOException {
    %match(subject) {
      TL(t,TextPosition[Line=startLine], TextPosition[Line=endLine]) -> {
        output.write(deep, `t, `startLine, `endLine - `startLine);
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

  public void generateOption(int deep, Option subject, String moduleName) throws IOException {
    %match(subject) {
      DeclarationToOption(decl) -> {
        //System.out.println("generateOption: " + `decl);
        `generateDeclaration(deep, decl, moduleName);
        return;
      }
      
      OriginTracking[] -> { return; }

      t -> {
        throw new TomRuntimeException("Cannot generate code for option: " + `t);
      }
    }
  }

  public void generateDeclaration(int deep, Declaration subject, String moduleName) throws IOException {
    %match(subject) {
      EmptyDeclaration() -> {
        return;
      }

      AbstractDecl(declList) -> {
        //`generateInstructionList(deep, instList);
        `generateDeclarationList(deep, declList, moduleName);
        return;
      }

      FunctionDef(Name(tomName),argList,codomain,throwsType,instruction) -> {    	  
        `buildFunctionDef(deep, tomName, argList, codomain, throwsType, instruction, moduleName);
        return;
      }

      MethodDef(Name(tomName),argList,codomain,throwsType,instruction) -> {    	  
        `buildMethodDef(deep, tomName, argList, codomain, throwsType, instruction, moduleName);
        return;
      }

      Class(Name(tomName),extendsType,superTerm,declaration) -> {
        `buildClass(deep, tomName, extendsType, superTerm, declaration, moduleName);
        return;
      }

      IntrospectorClass(Name(tomName),declaration) -> {
        `buildIntrospectorClass(deep, tomName, declaration, moduleName);
        return;
      }

      SymbolDecl(Name(tomName)) -> {
        `buildSymbolDecl(deep, tomName, moduleName);
        return ;
      }

      ArraySymbolDecl(Name(tomName)) -> {
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`tomName) 
        || getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
          `buildSymbolDecl(deep, tomName, moduleName);
          `genDeclArray(tomName, moduleName);
        }
        //if(getSymbolTable(moduleName).isUsedSymbolAC(`tomName)) {
        //  `genDeclAC(tomName, moduleName);
       // }
        return ;
      }

      ListSymbolDecl(Name(tomName)) -> {
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`tomName) 
        || getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
          `buildSymbolDecl(deep, tomName, moduleName);
          `genDeclList(tomName, moduleName);
        }
        //if(getSymbolTable(moduleName).isUsedSymbolAC(`tomName)) {
        //  `genDeclAC(tomName, moduleName);
        //}
        return ;
      }

      GetImplementationDecl(Variable[AstName=Name(name),
                            AstType=Type(type,tlType@TLType[])],
                            instr, _) -> {
        if(getSymbolTable(moduleName).isUsedSymbolDestructor(`name)) {
          `buildGetImplementationDecl(deep, type, name, tlType, instr, moduleName);
        }
        return;
      }

      IsFsymDecl(Name(tomName),
          Variable[AstName=Name(varname), AstType=Type[TlType=tlType@TLType[]]], code, _) -> {
        if(getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
          `buildIsFsymDecl(deep, tomName, varname, tlType, code, moduleName);
        }
        return;
      }

      GetSlotDecl[AstName=Name(tomName),
        SlotName=slotName,
        Variable=Variable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]],
        Expr=code] -> {
          if(getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
            `buildGetSlotDecl(deep, tomName, name, tlType, code, slotName, moduleName);
          }
          return;
        }

     EqualTermDecl(Variable[AstName=Name(name1), AstType=Type(type1,_)],
                     Variable[AstName=Name(name2), AstType=Type(type2,_)],
                     code, _) -> {
        if(getSymbolTable(moduleName).isUsedType(`type1)) {
          `buildEqualTermDecl(deep, name1, name2, type1, type2, code, moduleName);
        }
        return;
      }

      IsSortDecl(Variable[AstName=Name(varName), AstType=Type(type,_)], expr, _) -> {
        if(getSymbolTable(moduleName).isUsedType(`type)) {
          `buildIsSortDecl(deep, varName, type, expr, moduleName);
        }
        return;
      }

      GetHeadDecl[Opname=opNameAST@Name(opname),
        Codomain=Type[TlType=codomain],
        Variable=Variable[AstName=Name(varName), AstType=Type(suffix,domain@TLType[])],
        Expr=expr] -> {
          //System.out.println("option GetHead: " + `opname);
          if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname) 
          || getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
            `buildGetHeadDecl(deep, opNameAST, varName, suffix, domain, codomain, expr, moduleName);
          }
          return;
        }

      GetTailDecl[Opname=opNameAST@Name(opname),
        Variable=Variable[AstName=Name(varName), AstType=Type(type,tlType@TLType[])],
        Expr=expr] -> {
          if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname) 
          || getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
            `buildGetTailDecl(deep, opNameAST, varName, type, tlType, expr, moduleName);
          }
          return;
        }

      IsEmptyDecl[Opname=opNameAST@Name(opname),
        Variable=Variable[AstName=Name(varName), AstType=Type(type,tlType@TLType[])],
        Expr=expr] -> {
          if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname) 
              ||getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
            `buildIsEmptyDecl(deep, opNameAST, varName, type, tlType, expr, moduleName);
          }
          return;
        }

      MakeEmptyList(Name(opname), instr, _) -> {
        TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(`opname));
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname) 
            || getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
          `genDeclMake("tom_empty_list_", opname, returnType, concTomTerm(), instr, moduleName);
        }
        return;
      }

      MakeAddList(Name(opname),
          elt@Variable[AstType=Type[TlType=TLType[]]],
          list@Variable[AstType=fullListType@Type[TlType=TLType[]]],
          instr, _) -> {
        TomType returnType = `fullListType;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
          `genDeclMake("tom_cons_list_", opname, returnType, concTomTerm(elt,list), instr, moduleName);
        }
        return;
      }

      GetElementDecl[Opname=opNameAST@Name(opname),
        Variable=Variable[AstName=Name(name1), AstType=Type[TomType=type1,TlType=tlType1@TLType[]]],
        Index=Variable[AstName=Name(name2)],
        Expr=code] -> {
          if(getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
            `buildGetElementDecl(deep, opNameAST, name1, name2, type1, tlType1, code, moduleName);
          }
          return;
        }

      GetSizeDecl[Opname=opNameAST@Name(opname),
        Variable=Variable[AstName=Name(name),
        AstType=Type(type,tlType@TLType[])],
        Expr=code] -> {
          if(getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
            `buildGetSizeDecl(deep, opNameAST, name, type, tlType, code, moduleName);
          }
          return;
        }

      MakeEmptyArray(Name(opname),
          Variable[Option=option,AstName=name,Constraints=constraints],
          instr, _) -> {
        TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(`opname));
        TomTerm newVar = `Variable(option, name, getSymbolTable(moduleName).getIntType(), constraints);
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname)) {
          `genDeclMake("tom_empty_array_", opname, returnType, concTomTerm(newVar), instr, moduleName);
        }
        return;
      }

      MakeAddArray(Name(opname),
          elt@Variable[AstType=Type[TlType=TLType[]]],
          list@Variable[AstType=fullArrayType@Type[TlType=TLType[]]],
          instr, _) -> {
        TomType returnType = `fullArrayType;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname)) {
          `genDeclMake("tom_cons_array_", opname, returnType, concTomTerm(elt,list), instr, moduleName);
        }
        return;
      }

      MakeDecl(Name(opname), returnType, argList, instr, _) -> {
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname)) {
          `genDeclMake("tom_make_", opname, returnType, argList, instr, moduleName);
        }
        return;
      }

      TypeTermDecl[Declarations=declList] -> {
        generateDeclarationList(deep, `declList, moduleName);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for declaration: " + `t);
        throw new TomRuntimeException("Cannot generate code for declaration: " + `t);
      }
    }
  }

  public void generateListInclude(int deep, TomList subject, String moduleName) throws IOException {
    output.setSingleLine();
    generateList(deep, subject, moduleName);
    output.unsetSingleLine();
  }

  public void generateList(int deep, TomList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcTomTerm()) {
        generate(deep, subject.getHeadconcTomTerm(), moduleName);
        subject = subject.getTailconcTomTerm();
      }
    }

  public void generateOptionList(int deep, OptionList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcOption()) {
        generateOption(deep,subject.getHeadconcOption(), moduleName);
        subject = subject.getTailconcOption();
      }
    }

  public void generateInstructionList(int deep, InstructionList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcInstruction()) {
        generateInstruction(deep,subject.getHeadconcInstruction(), moduleName);
        subject = subject.getTailconcInstruction();
      }
      if(prettyMode) {
        output.writeln();
      }
    }

  public void generateDeclarationList(int deep, DeclarationList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcDeclaration()) {
        generateDeclaration(deep,subject.getHeadconcDeclaration(), moduleName);
        subject = subject.getTailconcDeclaration();
      }
    }

  public void generatePairNameDeclList(int deep, PairNameDeclList pairNameDeclList, String moduleName)
    throws IOException {
      while ( !pairNameDeclList.isEmptyconcPairNameDecl() ) {
        generateDeclaration(deep, pairNameDeclList.getHeadconcPairNameDecl().getSlotDecl(), moduleName);
        pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
      }
    }

  // ------------------------------------------------------------

  protected abstract void genDecl(String returnType,
      String declName,
      String suffix,
      String args[],
      TargetLanguage tlCode,
      String moduleName) throws IOException;

  protected abstract void genDeclInstr(String returnType,
      String declName,
      String suffix,
      String args[],
      Instruction instr,
      int deep,
      String moduleName) throws IOException;

  protected abstract void genDeclMake(String prefix, String funName, TomType returnType,
      TomList argList, Instruction instr, String moduleName) throws IOException;

  protected abstract void genDeclList(String name, String moduleName) throws IOException;
  protected abstract void genDeclArray(String name, String moduleName) throws IOException;
  //protected abstract void genDeclAC(String name, String moduleName) throws IOException;

  // ------------------------------------------------------------

  protected abstract void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException;
  protected abstract void buildComment(int deep, String text) throws IOException;
  protected abstract void buildTerm(int deep, String name, TomList argList, String moduleName) throws IOException;
  protected abstract void buildListOrArray(int deep, TomTerm list, String moduleName) throws IOException;

  protected abstract void buildFunctionCall(int deep, String name, TomList argList, String moduleName)  throws IOException;
  protected abstract void buildFunctionDef(int deep, String tomName, TomList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException;
  protected void buildMethodDef(int deep, String tomName, TomList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    throw new TomRuntimeException("Backend "+getClass()+" does not support Methods");
  }

  /*buildClass is not abstract since only Java backend supports class
    only backends that supports Class should overload buildClass
   */
  protected void buildClass(int deep, String tomName, TomType extendsType, TomTerm superTerm, Declaration declaration, String moduleName) throws IOException {
    throw new TomRuntimeException("Backend does not support Class");
  }

  /*buildIntrospectorClass is not abstract since only Java backend supports class
    only backends that supports Class should overload buildIntrospectorClass
   */
  protected void buildIntrospectorClass(int deep, String tomName, Declaration declaration, String moduleName) throws IOException {
    throw new TomRuntimeException("Backend does not support Class");
  }

  protected abstract void buildExpNegation(int deep, Expression exp, String moduleName) throws IOException;

  protected abstract void buildExpConditional(int deep, Expression cond,Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpAnd(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpOr(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpGreaterThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpGreaterOrEqualThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpLessThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpLessOrEqualThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpBottom(int deep, TomType type, String moduleName) throws IOException;
  protected abstract void buildExpTrue(int deep) throws IOException;
  protected abstract void buildExpFalse(int deep) throws IOException;
  protected abstract void buildExpIsEmptyList(int deep, String opName, TomType type, TomTerm expList, String moduleName) throws IOException;
  protected abstract void buildExpIsEmptyArray(int deep, TomName opName, TomType type, TomTerm expIndex, TomTerm expArray, String moduleName) throws IOException;
  protected abstract void buildExpEqualTerm(int deep, TomType type, TomTerm exp1,TomTerm exp2, String moduleName) throws IOException;
  protected abstract void buildExpIsSort(int deep, String type, TomTerm exp, String moduleName) throws IOException;
  protected abstract void buildExpIsFsym(int deep, String opname, TomTerm var, String moduleName) throws IOException;
  protected abstract void buildExpCast(int deep, TomType type, Expression exp, String moduleName) throws IOException;
  protected abstract void buildExpGetSlot(int deep, String opname, String slotName, TomTerm exp, String moduleName) throws IOException;
  protected abstract void buildExpGetHead(int deep, String opName, TomType domain, TomType codomain, TomTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetTail(int deep, String opName, TomType type1, TomTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetSize(int deep, TomName opNameAST, TomType type1, TomTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetElement(int deep, TomName opNameAST, TomType domain, TomType codomain, TomTerm varName, TomTerm varIndex, String moduleName) throws IOException;
  protected abstract void buildExpGetSliceList(int deep, String name, TomTerm varBegin, TomTerm varEnd, TomTerm tailSlice, String moduleName) throws IOException;
  protected abstract void buildExpGetSliceArray(int deep, String name, TomTerm varArray, TomTerm varBegin, TomTerm expEnd, String moduleName) throws IOException;
  protected abstract void buildAssign(int deep, TomTerm var, OptionList list, Expression exp, String moduleName) throws IOException ;
  protected abstract void buildAssignArray(int deep, TomTerm var, OptionList list, TomTerm index, Expression exp, String moduleName) throws IOException ;
  protected abstract void buildLet(int deep, TomTerm var, OptionList list, TomType tlType, Expression exp, Instruction body, String moduleName) throws IOException ;
  protected abstract void buildLetRef(int deep, TomTerm var, OptionList list, TomType tlType, Expression exp, Instruction body, String moduleName) throws IOException ;
  protected abstract void buildNamedBlock(int deep, String blockName, InstructionList instList, String modulename) throws IOException ;
  protected abstract void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException ;
  protected abstract void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException ;
  protected abstract void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException ;
  protected abstract void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException;
  protected abstract void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException;
  protected abstract void buildAddOne(int deep, TomTerm var, String moduleName) throws IOException;
  protected abstract void buildSubstractOne(int deep, TomTerm var, String moduleName) throws IOException;
  protected abstract void buildSubstract(int deep, TomTerm var1, TomTerm var2, String moduleName) throws IOException;
  protected abstract void buildReturn(int deep, TomTerm exp, String moduleName) throws IOException ;
  protected abstract void buildSymbolDecl(int deep, String tomName, String moduleName) throws IOException ;
  protected abstract void buildGetImplementationDecl(int deep, String type, String name,
      TomType tlType, Instruction instr, String moduleName) throws IOException;

  protected abstract void buildIsFsymDecl(int deep, String tomName, String name1,
      TomType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetSlotDecl(int deep, String tomName, String name1,
      TomType tlType, Expression code, TomName slotName, String moduleName) throws IOException;
  protected abstract void buildEqualTermDecl(int deep, String name1, String name2, String type1, String type2, Expression code, String moduleName) throws IOException;
  protected abstract void buildIsSortDecl(int deep, String name1, 
      String type1, Expression expr, String moduleName) throws IOException;
  protected abstract void buildGetHeadDecl(int deep, TomName opNameAST, String varName, String suffix, TomType domain, TomType codomain, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetTailDecl(int deep, TomName opNameAST, String varName, String type, TomType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildIsEmptyDecl(int deep, TomName opNameAST, String varName, String type,
      TomType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetElementDecl(int deep, TomName opNameAST, String name1, String name2, String type1, TomType tlType1, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetSizeDecl(int deep, TomName opNameAST, String name1, String type, TomType tlType, Expression code, String moduleName) throws IOException;

} // class TomAbstractGenerator
