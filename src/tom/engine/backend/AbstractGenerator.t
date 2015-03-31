/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;

import aterm.*;

public abstract class AbstractGenerator {

  protected OutputCode output;
  protected OptionManager optionManager;
  protected SymbolTable symbolTable;
  protected boolean prettyMode;

  public AbstractGenerator(OutputCode output, OptionManager optionManager,
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

  protected TomType getTermType(TomTerm t) {
    return TomBase.getTermType(t, symbolTable);
  }

  protected TomType getTermType(BQTerm t) {
    return TomBase.getTermType(t, symbolTable);
  }

  protected TomType getUniversalType() {
    return symbolTable.getUniversalType();
  }

  protected TomType getCodomain(TomSymbol tSymbol) {
    return TomBase.getSymbolCodomain(tSymbol);
  }

// ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
// ------------------------------------------------------------

  /**
   * Generate the goal language
   * 
   * @param deep 
   * 		The distance from the right side (allows the computation of the column number)
   */
  protected void generate(int deep, Code subject, String moduleName) throws IOException {
    %match(subject) {

      Tom(l) -> {
        generateList(deep,`l, moduleName);
        return;
      }

      TomInclude(l) -> {
        generateListInclude(deep,`l, moduleName);
        return;
      }

      BQTermToCode(t) -> {
        generateBQTerm(deep,`t, moduleName);
        return;
      }

      TargetLanguageToCode(t) -> {
        generateTargetLanguage(deep,`t, moduleName);
        return;
      }

      InstructionToCode(t) -> {
        generateInstruction(deep,`t, moduleName);
        return;
      }

      DeclarationToCode(t) -> {
        generateDeclaration(deep,`t, moduleName);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for: " + `t);
        throw new TomRuntimeException("Cannot generate code for: " + `t);
      }
    }
  }
  
  /**
   * Generate the goal language
   * 
   * @param deep 
   * 		The distance from the right side (allows the computation of the column number)
   */
  protected void generateTomTerm(int deep, TomTerm subject, String moduleName) throws IOException {
    //TODO: complete with each constructor used in the baclend input term
    %match (subject) {
      var@(Variable|VariableStar)[] -> {
        output.write(deep,getVariableName(`var));
        return;
      }
    } 
  }

  /**
   * Generate the goal language
   * 
   * @param deep 
   * 		The distance from the right side (allows the computation of the column number)
   */
  protected void generateBQTerm(int deep, BQTerm subject, String moduleName) throws IOException {
    %match (subject) {
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

      var@(BQVariable|BQVariableStar)[] -> {
        output.write(deep,getVariableName(`var));
        return;
      }

      ExpressionToBQTerm(t) -> {
        generateExpression(deep,`t, moduleName);
        return;
      }

      Composite(_*,t,_*) -> {
        %match(t) {
          CompositeTL(target) -> {
            generateTargetLanguage(deep,`target, moduleName);
          }
          CompositeBQTerm(term) -> {
            generateBQTerm(deep,`term, moduleName);
          }
        }
      }

      t@!Composite(_*) -> {
        throw new TomRuntimeException("Cannot generate code for bqterm "+`t);
      }
    }
  }

  protected String getVariableName(BQTerm var) {
    %match(var) {
      BQVariable[AstName=PositionName(l)] -> {
        return ("tom" + TomBase.tomNumberListToString(`l));
      }

      BQVariable[AstName=Name(name)] -> {
        return `name;
      }

      BQVariableStar[AstName=PositionName(l)] -> {
        return ("tom" + TomBase.tomNumberListToString(`l));
      }

      BQVariableStar[AstName=Name(name)] -> {
        return `name;
      }
    }
    throw new RuntimeException("cannot generate the name of the variable "+var);
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
    throw new RuntimeException("cannot generate the name of the variable "+var);
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
      EqualBQTerm(type,exp1,exp2) -> {
        `buildExpEqualBQTerm(deep, type, exp1, exp2, moduleName);
        return;
      }
      IsSort(Type[TomType=type], exp) -> {
        `buildExpIsSort(deep,type,exp,moduleName);
        return;
      }

      IsFsym(Name(opname), exp) -> {
        buildExpIsFsym(deep, `opname, `exp, moduleName);
        return;
      }

      Cast(Type[TlType=tlType@TLType[]],exp) -> {
        buildExpCast(deep, `tlType, `exp, moduleName);
        return;
      }

      GetSlot(_,Name(opname),slotName, var@(BQVariable|BuildTerm|ExpressionToBQTerm)[]) -> {    	  
        `buildExpGetSlot(deep, opname, slotName, var, moduleName);
        return;
      }
      GetSlot(_,Name(opname),slotName, var@ExpressionToBQTerm(GetSlot[])) -> {    	  
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

      GetElement(opNameAST, _, varName, varIndex) -> {
        buildExpGetElement(deep,`opNameAST,getTermType(`varName),`varName, `varIndex, moduleName);
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

      BQTermToExpression(t) -> {
        generateBQTerm(deep,`t, moduleName);
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
  protected void generateArray(int deep, BQTerm subject, BQTerm index, String moduleName) throws IOException {
    %match(subject) {
      BQVariable[AstName=PositionName(l)] -> {        
        output.write("tom" + TomBase.tomNumberListToString(`l));        
      }
      BQVariable[AstName=Name(name)] -> {
        output.write(`name);        
      }
    }
    %match(index) {
      BQVariable[AstName=PositionName(l)] -> {
        output.write("[");
        output.write("tom" + TomBase.tomNumberListToString(`l));
        output.write("]");        
      }
      BQVariable[AstName=Name(name)] -> {
        output.write("[");
        output.write(`name);
        output.write("]");
      }
      ExpressionToBQTerm(Integer(x)) -> {
        output.write("[");
        output.write(`x);
        output.write("]");  
      }
    }    
  } 

  public void generateInstruction(int deep, Instruction subject, String moduleName) throws IOException {
    %match(subject) {

      CodeToInstruction(c) -> {
        `generate(deep, c, moduleName);
        return;
      }

      BQTermToInstruction(t) -> {
        `generateBQTerm(deep, t, moduleName);
        return;
      }

      ExpressionToInstruction(t) -> {
        `generateExpression(deep, t, moduleName);
        return;
      }

      Nop() -> {
        return;
      }

      Assign(var@(BQVariable|BQVariableStar)[Options=optionList],exp) -> {
        `buildAssign(deep, var, optionList, exp, moduleName);
        return;
      }

      AssignArray(var@BQVariable[Options=optionList],index,exp) -> {
        `buildAssignArray(deep, var, optionList, index, exp, moduleName);
        return;
      }

      Let(var@(BQVariable|BQVariableStar)[Options=optionList,AstType=Type[TlType=tlType]],exp,body) -> {
        `buildLet(deep, var, optionList, tlType, exp, body, moduleName);
        return;
      }

      LetRef(var@(BQVariable|BQVariableStar)[Options=optionList,AstType=Type[TlType=tlType]],exp,body) -> {
        `buildLetRef(deep, var, optionList, tlType, exp, body, moduleName);
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

      RawAction[AstInstruction=AbstractBlock(instructionList)] -> {
        `generateInstructionList(deep, instructionList, moduleName);
        return;
      }

      RawAction[AstInstruction=inst] -> {
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

      //Tracelink(Type:TomName,Name:TomName,ElementaryTransfoName:TomName,Expr:Expression,OrgTrack:Option)//BQTerm, then blocklist
      Tracelink(Name(type), Name(name), _, expr, _) -> {
        `buildTracelink(deep, type, name, expr, moduleName);
        return;
      }

      TracelinkPopulateResolve[RefClassName=Name(refClassName),TracedLinks=tracedLinks,Current=current,Link=link] -> {
        `buildTracelinkPopulateResolve(deep, refClassName, tracedLinks, current, link, moduleName);
        return;
      }

      //resolve construct
      Resolve[ResolveBQTerm=resolveBQTerm] -> {
        //why?
        //buildResolve(deep, `bqterm, moduleName);
        generateBQTerm(deep, `resolveBQTerm, moduleName);
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
        `generateDeclaration(deep, decl, moduleName);
        return;
      }
      OriginTracking[] -> { return; }
      ACSymbol() -> {
        // TODO RK: here add the declarations for intarray
        return; 
      }
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
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
          `buildSymbolDecl(deep, tomName, moduleName);
          `genDeclArray(tomName, moduleName);
        }
        return ;
      }

      (ListSymbolDecl|ACSymbolDecl)(Name(tomName)) -> {
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`tomName) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
          `buildSymbolDecl(deep, tomName, moduleName);
          `genDeclList(tomName, moduleName);
        }
        return ;
      }

      IsFsymDecl(Name(tomName),
          BQVariable[AstName=Name(varname), AstType=Type[TlType=tlType@TLType[]]], code, _) -> {
        if(getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
          `buildIsFsymDecl(deep, tomName, varname, tlType, code, moduleName);
        }
        return;
      }

      GetSlotDecl[AstName=Name(tomName),
        SlotName=slotName,
        Variable=BQVariable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]],
        Expr=code] -> {
          if(getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
            `buildGetSlotDecl(deep, tomName, name, tlType, code, slotName, moduleName);
          }
          return;
        }

      //FIXME: what are the consequences on the code generation ? 
      ImplementDecl[AstName=Name(name), Expr=code] -> {
        //nothing for the moment
        return;
      }

      GetDefaultDecl[AstName=Name(tomName), SlotName=slotName, Expr=code] -> {
          if(getSymbolTable(moduleName).isUsedSymbolConstructor(`tomName)) {
            `buildGetDefaultDecl(deep, tomName, code, slotName, moduleName);
          }
          return;
        }

      EqualTermDecl(BQVariable[AstName=Name(name1), AstType=Type[TomType=type1]],
          BQVariable[AstName=Name(name2), AstType=Type[TomType=type2]],
          code, _) -> {
        if(getSymbolTable(moduleName).isUsedType(`type1)) {
          `buildEqualTermDecl(deep, name1, name2, type1, type2, code, moduleName);
        }
        return;
      }

      IsSortDecl(BQVariable[AstName=Name(varName), AstType=Type[TomType=type]], expr, _) -> {
        if(getSymbolTable(moduleName).isUsedType(`type)) {
          `buildIsSortDecl(deep, varName, type, expr, moduleName);
        }
        return;
      }
      
      BQTermToDeclaration(bqterm) -> {
        generateBQTerm(deep,`bqterm,moduleName);
        return;
      }

      //why?
      ResolveIsFsymDecl(Name(tomName),
          BQVariable[AstName=Name(varname), AstType=Type[TlType=tlType@TLType[]]], _) -> {
        if(getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
          `buildResolveIsFsymDecl(deep, tomName, varname, tlType, moduleName);
        }
        return;
      }

      ResolveGetSlotDecl[AstName=Name(tomName),
        SlotName=slotName,
        Variable=BQVariable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]]] -> {
          if(getSymbolTable(moduleName).isUsedSymbolDestructor(`tomName)) {
            `buildResolveGetSlotDecl(deep, tomName, name, tlType, slotName, moduleName);
          }
          return;
        }

      //why?
      ResolveMakeDecl(Name(opname), returnType, argList, _) -> {
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname)) {
          `genResolveDeclMake("tom_make_", opname, returnType, argList, moduleName);
        }
        return;
      }

      ResolveClassDecl[WithName=wName,ToName=tName,Extends=eName] -> {
        `buildResolveClass(wName,tName, eName, moduleName);
        return;
      }

      ResolveInverseLinksDecl[FileFrom=fileFrom,FileTo=fileTo,ResolveNameList=resolveNameList] -> {
        `buildResolveInverseLinks(deep, fileFrom, fileTo, resolveNameList, moduleName);
        return;
      }

      //complete with specialized backquotes
      //ReferenceClass[RefName=Name(refname),Fields=instructions] -> {
      ReferenceClass[RefName=Name(refname),Fields=refclassTInstructions] -> {
        //no test in order to generate the skeletton even if the class is not
        //populated
        //if(!`refclassTInstructions.isEmptyconcRefClassTracelinkInstruction()) {
        `buildReferenceClass(deep, refname, refclassTInstructions, moduleName);
        //}
        return;
      }

//////

      GetHeadDecl[Opname=opNameAST@Name(opname),
        Codomain=Type[TlType=codomain],
        Variable=BQVariable[AstName=Name(varName),
        AstType=Type[TomType=suffix,TlType=domain@TLType[]]],
        Expr=expr] -> {
          if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname) 
           ||getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
            `buildGetHeadDecl(deep, opNameAST, varName, suffix, domain, codomain, expr, moduleName);
          }
          return;
        }

      GetTailDecl[Opname=opNameAST@Name(opname),
        Variable=BQVariable[AstName=Name(varName),
        AstType=Type[TomType=type,TlType=tlType@TLType[]]],
        Expr=expr] -> {
          if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname) 
              ||getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
            `buildGetTailDecl(deep, opNameAST, varName, type, tlType, expr, moduleName);
          }
          return;
        }

      IsEmptyDecl[Opname=opNameAST@Name(opname),
        Variable=BQVariable[AstName=Name(varName),
        AstType=Type[TomType=type,TlType=tlType@TLType[]]],
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
          `genDeclMake("tom_empty_list_", opname, returnType, concBQTerm(), instr, moduleName);
        }
        return;
      }

      MakeAddList(Name(opname),
          elt@BQVariable[AstType=Type[TlType=TLType[]]],
          list@BQVariable[AstType=fullListType@Type[TlType=TLType[]]],
          instr, _) -> {
        TomType returnType = `fullListType;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
          `genDeclMake("tom_cons_list_", opname, returnType, concBQTerm(elt,list), instr, moduleName);
        }
        return;
      }

      GetElementDecl[Opname=opNameAST@Name(opname),
        Variable=BQVariable[AstName=Name(name1), AstType=Type[TomType=type1,TlType=TLType[]]],
        Index=BQVariable[AstName=Name(name2)],
        Expr=code] -> {
          if(getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
            `buildGetElementDecl(deep, opNameAST, name1, name2, type1, code, moduleName);
          }
          return;
        }

      GetSizeDecl[Opname=opNameAST@Name(opname),
        Variable=BQVariable[AstName=Name(name),
        AstType=Type[TomType=type,TlType=TLType[]]],
        Expr=code] -> {
          if(getSymbolTable(moduleName).isUsedSymbolDestructor(`opname)) {
            `buildGetSizeDecl(deep, opNameAST, name, type, code, moduleName);
          }
          return;
        }

      MakeEmptyArray(Name(opname),
          BQVariable[Options=optionList,AstName=name],
          instr, _) -> {
        TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(`opname));
        BQTerm newVar = `BQVariable(optionList, name, getSymbolTable(moduleName).getIntType());
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname)) {
          `genDeclMake("tom_empty_array_", opname, returnType, concBQTerm(newVar), instr, moduleName);
        }
        return;
      }

      MakeAddArray(Name(opname),
          elt@BQVariable[AstType=Type[TlType=TLType[]]],
          list@BQVariable[AstType=fullArrayType@Type[TlType=TLType[]]],
          instr, _) -> {
        TomType returnType = `fullArrayType;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(`opname)) {
          `genDeclMake("tom_cons_array_", opname, returnType, concBQTerm(elt,list), instr, moduleName);
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
        //FIXME: consequences of a %op_implement? 
        generateDeclarationList(deep, `declList, moduleName);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for declaration: " + `t);
        throw new TomRuntimeException("Cannot generate code for declaration: " + `t);
      }
    }
  }

  public void generateListInclude(int deep, CodeList subject, String moduleName) throws IOException {
    output.setSingleLine();
    generateList(deep, subject, moduleName);
    output.unsetSingleLine();
  }

  public void generateList(int deep, CodeList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcCode()) {
        generate(deep, subject.getHeadconcCode(), moduleName);
        subject = subject.getTailconcCode();
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
      /* this is not pretty ! there is a ";" right after the new line
      if(prettyMode) {
        output.writeln();
      }*/
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
      BQTermList argList, Instruction instr, String moduleName) throws IOException;

  protected abstract void genDeclList(String name, String moduleName) throws IOException;

  protected abstract void genDeclArray(String name, String moduleName) throws IOException;

  // ------------------------------------------------------------

  protected abstract void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException;
  protected abstract void buildComment(int deep, String text) throws IOException;
  protected abstract void buildTerm(int deep, String name, BQTermList argList, String moduleName) throws IOException;
  protected abstract void buildListOrArray(int deep, BQTerm list, String moduleName) throws IOException;

  protected abstract void buildFunctionCall(int deep, String name, BQTermList argList, String moduleName)  throws IOException;
  protected abstract void buildFunctionDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException;
  protected void buildMethodDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    throw new TomRuntimeException("Backend "+getClass()+" does not support Methods");
  }

  /*buildClass is not abstract since only Java backend supports class
    only backends that supports Class should overload buildClass
   */
  protected void buildClass(int deep, String tomName, TomType extendsType, BQTerm superTerm, Declaration declaration, String moduleName) throws IOException {
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
  protected abstract void buildExpIsEmptyList(int deep, String opName, TomType type, BQTerm expList, String moduleName) throws IOException;
  protected abstract void buildExpIsEmptyArray(int deep, TomName opName, TomType type, BQTerm expIndex, BQTerm expArray, String moduleName) throws IOException;
  protected abstract void buildExpEqualTerm(int deep, TomType type, BQTerm exp1,TomTerm exp2, String moduleName) throws IOException;
  protected abstract void buildExpEqualBQTerm(int deep, TomType type, BQTerm exp1,BQTerm exp2, String moduleName) throws IOException;
  protected abstract void buildExpIsSort(int deep, String type, BQTerm exp, String moduleName) throws IOException;
  protected abstract void buildExpIsFsym(int deep, String opname, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpCast(int deep, TargetLanguageType tlType, Expression exp, String moduleName) throws IOException;
  protected abstract void buildExpGetSlot(int deep, String opname, String slotName, BQTerm exp, String moduleName) throws IOException;
  protected abstract void buildExpGetHead(int deep, String opName, TomType domain, TomType codomain, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetTail(int deep, String opName, TomType type1, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetSize(int deep, TomName opNameAST, TomType type1, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetElement(int deep, TomName opNameAST, TomType domain, BQTerm varName, BQTerm varIndex, String moduleName) throws IOException;
  protected abstract void buildExpGetSliceList(int deep, String name, BQTerm varBegin, BQTerm varEnd, BQTerm tailSlice, String moduleName) throws IOException;
  protected abstract void buildExpGetSliceArray(int deep, String name, BQTerm varArray, BQTerm varBegin, BQTerm expEnd, String moduleName) throws IOException;
  protected abstract void buildAssign(int deep, BQTerm var, OptionList optionList, Expression exp, String moduleName) throws IOException ;
  protected abstract void buildAssignArray(int deep, BQTerm var, OptionList
      optionList, BQTerm index, Expression exp, String moduleName) throws IOException ;
  protected abstract void buildLet(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType, Expression exp, Instruction body, String moduleName) throws IOException ;
  protected abstract void buildLetRef(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType, Expression exp, Instruction body, String moduleName) throws IOException ;
  protected abstract void buildNamedBlock(int deep, String blockName, InstructionList instList, String modulename) throws IOException ;
  protected abstract void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException ;
  protected abstract void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException ;
  protected abstract void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException ;
  protected abstract void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException;
  protected abstract void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException;
  protected abstract void buildAddOne(int deep, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildSubstractOne(int deep, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildSubstract(int deep, BQTerm var1, BQTerm var2, String moduleName) throws IOException;
  protected abstract void buildReturn(int deep, BQTerm exp, String moduleName) throws IOException ;
  protected abstract void buildSymbolDecl(int deep, String tomName, String moduleName) throws IOException ;

  protected abstract void buildIsFsymDecl(int deep, String tomName, String name1,
      TargetLanguageType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetSlotDecl(int deep, String tomName, String name1,
      TargetLanguageType tlType, Expression code, TomName slotName, String moduleName) throws IOException;
  protected abstract void buildGetDefaultDecl(int deep, String tomName, 
       Expression code, TomName slotName, String moduleName) throws IOException;
  protected abstract void buildEqualTermDecl(int deep, String name1, String name2, String type1, String type2, Expression code, String moduleName) throws IOException;
  protected abstract void buildIsSortDecl(int deep, String name1, 
      String type1, Expression expr, String moduleName) throws IOException;
  protected abstract void buildGetHeadDecl(int deep, TomName opNameAST, String varName, String suffix, TargetLanguageType domain, TargetLanguageType codomain, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetTailDecl(int deep, TomName opNameAST, String varName, String type, TargetLanguageType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildIsEmptyDecl(int deep, TomName opNameAST, String varName, String type,
      TargetLanguageType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetElementDecl(int deep, TomName opNameAST, String name1, String name2, String type1, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetSizeDecl(int deep, TomName opNameAST, String name1, String type, Expression code, String moduleName) throws IOException;

  
  //TODO: Resolve*
  protected abstract void buildResolveIsFsymDecl(int deep, String tomName, String name1, TargetLanguageType tlType, String moduleName) throws IOException;
  protected abstract void buildResolveGetSlotDecl(int deep, String tomName, String name1, TargetLanguageType tlType, TomName slotName, String moduleName) throws IOException;
  protected abstract void buildResolveClass(String wName, String tName, String extendsName, String moduleName) throws IOException;
  protected abstract void buildResolveInverseLinks(int deep, String fileFrom, String fileTo, TomNameList resolveNameList, String moduleName) throws IOException;
  protected abstract void buildReferenceClass(int deep, String refname, RefClassTracelinkInstructionList refclassTInstructions, String moduleName) throws IOException;
  protected abstract void buildTracelink(int deep, String type, String name, Expression expr, String moduleName) throws IOException;
  protected abstract void buildResolve(int deep, BQTerm bqterm, String moduleName) throws IOException;
  protected abstract void buildTracelinkPopulateResolve(int deep, String refClassName, TomNameList tracedLinks, BQTerm current, BQTerm link, String moduleName) throws IOException;
  protected abstract void genResolveDeclMake(String prefix, String funName, TomType returnType, BQTermList argList, String moduleName) throws IOException;
  protected abstract String genResolveMakeCode(String funName, BQTermList argList) throws IOException;
  protected abstract String genResolveIsFsymCode(String tomName, String varname) throws IOException;
  protected abstract String genResolveGetSlotCode(String tomName, String varname, String slotName) throws IOException;

}
