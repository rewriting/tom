/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
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

package tom.engine.compiler;

import java.util.*;
import java.util.logging.Level;

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

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.library.sl.*;

/**
 * The Compiler plugin.
 */
public class Compiler extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../../library/mapping/java/util/types/Map.tom}
  %include { ../../library/mapping/java/util/types/Collection.tom}

  %typeterm Compiler {
    implement { Compiler }
    is_sort(t) { t instanceof Compiler }
  }

  %op Strategy ChoiceTopDown(s1:Strategy) {
    make(v) { `mu(MuVar("x"),ChoiceId(v,All(MuVar("x")))) }
  }

  /** some output suffixes */
  public static final String COMPILED_SUFFIX = ".tfix.compiled";

  /** the declared options string*/
  public static final String DECLARED_OPTIONS = "<options><boolean name='compile' altName='' description='Compiler (activated by default)' value='true'/></options>";

  /** unicity var counter*/
  private static int absVarNumber;

  /** Constructor*/
  public Compiler() {
    super("Compiler");
  }

  public void run() {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    try {
      // reinit absVarNumber to generate reproducible output
      absVarNumber = 0;
      TomTerm preCompiledTerm = (TomTerm) `preProcessing(this).visitLight((TomTerm)getWorkingTerm());
      //System.out.println("preCompiledTerm = \n" + preCompiledTerm);
      TomTerm compiledTerm = ConstraintCompiler.compile(preCompiledTerm,getStreamManager().getSymbolTable());
      //System.out.println("compiledTerm = \n" + compiledTerm);            
      Collection hashSet = new HashSet();
      TomTerm renamedTerm = (TomTerm) `TopDown(findRenameVariable(hashSet)).visitLight(compiledTerm);
      //System.out.println("renamedTerm = \n" + renamedTerm);
      // verbose
      getLogger().log(Level.INFO, TomMessage.tomCompilationPhase.getMessage(),
          new Integer((int)(System.currentTimeMillis()-startChrono)) );
      setWorkingTerm(renamedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() + COMPILED_SUFFIX, (TomTerm)getWorkingTerm());
      }
    } catch (Exception e) {
      getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{getStreamManager().getInputFileName(), "Compiler", e.getMessage()} );
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(Compiler.DECLARED_OPTIONS);
  }

  /*
   * preProcessing:
   * replaces BuildReducedTerm by BuildList, BuildArray or BuildTerm
   *
   * abstract list-matching patterns
   */

  %op Strategy preProcessing(compiler:Compiler){
    make(compiler) { `ChoiceTopDown(preProcessing_once(compiler)) }
  }

  %strategy preProcessing_once(compiler:Compiler) extends `Identity(){
    visit TomTerm {
      BuildReducedTerm[TomTerm=var@(Variable|VariableStar)[]] -> {
        return `var;
      }

      BuildReducedTerm[TomTerm=RecordAppl[Option=optionList,NameList=(name@Name(tomName)),Slots=termArgs],AstType=astType] -> {
        TomSymbol tomSymbol = compiler.symbolTable().getSymbolFromName(`tomName);
        SlotList newTermArgs = (SlotList) `preProcessing_makeTerm(compiler).visitLight(`termArgs);
        TomList tomListArgs = TomBase.slotListToTomList(newTermArgs);

        if(TomBase.hasConstant(`optionList)) {
          return `BuildConstant(name);
        } else if(tomSymbol != null) {
          if(TomBase.isListOperator(tomSymbol)) {
            return ASTFactory.buildList(`name,tomListArgs,compiler.symbolTable());
          } else if(TomBase.isArrayOperator(tomSymbol)) {
            return ASTFactory.buildArray(`name,tomListArgs,compiler.symbolTable());
          } else if(TomBase.isDefinedSymbol(tomSymbol)) {
            return `FunctionCall(name,TomBase.getSymbolCodomain(tomSymbol),tomListArgs);
          } else {
            String moduleName = TomBase.getModuleName(`optionList);
            if(moduleName==null) {
              moduleName = TomBase.DEFAULT_MODULE_NAME;
            }
            return `BuildTerm(name,tomListArgs,moduleName);
          }
        } else {
          return `FunctionCall(name,astType,tomListArgs);
        }

      }

    } // end match

    visit Instruction {
      Match(matchSubjectList,patternInstructionList, matchOptionList)  -> {
        Option orgTrack = TomBase.findOriginTracking(`matchOptionList);
        PatternInstructionList newPatternInstructionList = `concPatternInstruction();
        PatternList negativePattern = `concPattern();
        TomTerm newMatchSubjectList = (TomTerm) `preProcessing(compiler).visitLight(`matchSubjectList);
        while(!`patternInstructionList.isEmptyconcPatternInstruction()) {
          /*
           * the call to preProcessing performs the recursive expansion
           * of nested match constructs
           */
          PatternInstruction newPatternInstruction = (PatternInstruction) `preProcessing(compiler).visitLight(`patternInstructionList.getHeadconcPatternInstruction());

matchBlock: {
              %match(newPatternInstruction) {
                PatternInstruction(pattern@Pattern[SubjectList=subjectList,TomList=termList],actionInst, option) -> {
                  Instruction newAction = `actionInst;
                  /* expansion of RawAction into TypedAction */
                  %match(actionInst) {
                    RawAction(x) -> {
                      newAction=`TypedAction(If(TrueTL(),x,Nop()),pattern,negativePattern);
                    }
                  }
                  negativePattern = `concPattern(negativePattern*,pattern);

                  /* generate equality checks */
                  newPatternInstruction = `PatternInstruction(Pattern(subjectList,termList,concTomTerm()),newAction, option);
                  /* do nothing */
                  break matchBlock;
                }

                _ -> {
                  System.out.println("preProcessing: strange PatternInstruction: " + `newPatternInstruction);
                  throw new TomRuntimeException("preProcessing: strange PatternInstruction: " + `newPatternInstruction);
                }
              }
            } // end matchBlock

            newPatternInstructionList = `concPatternInstruction(newPatternInstructionList*,newPatternInstruction);
            `patternInstructionList = `patternInstructionList.getTailconcPatternInstruction();
        }

        Instruction newMatch = `Match(newMatchSubjectList, newPatternInstructionList, matchOptionList);
        return newMatch;
      }

    } // end match

    visit Declaration {
      Strategy(name,extendsTerm,visitList,orgTrack) -> {
        DeclarationList l = `concDeclaration();//represents compiled Strategy
        TomVisitList jVisitList = `visitList;
        TomForwardType visitorFwd = null;
        while(!jVisitList.isEmptyconcTomVisit()) {
          TomList subjectListAST = `concTomTerm();
          TomVisit visit = jVisitList.getHeadconcTomVisit();
          %match(visit) {
            VisitTerm(vType@Type[TomType=ASTTomType(type)],patternInstructionList,_) -> {
              if(visitorFwd == null) {//first time in loop
                visitorFwd = compiler.symbolTable().getForwardType(`type);//do the job only once
              }
              TomTerm arg = `Variable(concOption(),Name("tom__arg"),vType,concConstraint());//arg subjectList
              subjectListAST = `concTomTerm(subjectListAST*,arg);
              String funcName = "visit_" + `type;//function name
              Instruction matchStatement = `Match(SubjectList(subjectListAST),patternInstructionList, concOption(orgTrack));
              //return default strategy.visitLight(arg)
              Instruction returnStatement = `Return(FunctionCall(Name("super." + funcName),vType,subjectListAST));
              InstructionList instructions = `concInstruction(matchStatement, returnStatement);
              l = `concDeclaration(l*,MethodDef(Name(funcName),concTomTerm(arg),vType,TomTypeAlone("tom.library.sl.VisitFailure"),AbstractBlock(instructions)));
            }
          }
          jVisitList = jVisitList.getTailconcTomVisit();
        }
        return (Declaration) `preProcessing(compiler).visitLight(`Class(name,visitorFwd,extendsTerm,AbstractDecl(l)));
      }
    }//end match
  } // end strategy

  %op Strategy preProcessing_makeTerm(compiler:Compiler){
     make(compiler) { `ChoiceTopDown(preProcessing_makeTerm_once(compiler)) }
  }

  %strategy preProcessing_makeTerm_once(compiler:Compiler) extends `Identity()  {
    visit TomTerm {
      t -> {return (TomTerm) `preProcessing(compiler).visitLight(`BuildReducedTerm(t,compiler.getTermType(t)));}
    }
  }

  private TomTerm abstractPattern(TomTerm subject, ArrayList abstractedPattern, ArrayList introducedVariable)  {
    TomTerm abstractedTerm = subject;
    %match(subject) {
      RecordAppl[NameList=(Name(tomName),_*), Slots=arguments] -> {
        TomSymbol tomSymbol = symbolTable().getSymbolFromName(`tomName);

        SlotList newArgs = `concSlot();
        if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
          SlotList args = `arguments;
          while(!args.isEmptyconcSlot()) {
            Slot elt = args.getHeadconcSlot();
            TomTerm newElt = elt.getAppl();
            %match(newElt) {
              appl@RecordAppl[NameList=(Name(tomName2),_*)] -> {
                /*
                 * we no longer abstract syntactic subterm
                 * they are compiled by the KernelCompiler
                 */

                //System.out.println("Abstract: " + appl);
                TomSymbol tomSymbol2 = symbolTable().getSymbolFromName(`tomName2);
                if(TomBase.isListOperator(tomSymbol2) || TomBase.isArrayOperator(tomSymbol2)) {
                  TomType type2 = tomSymbol2.getTypesToType().getCodomain();
                  abstractedPattern.add(`appl);

                  TomNumberList path = `concTomNumber();
                  absVarNumber++;
                  path = `concTomNumber(path*,AbsVar(absVarNumber));

                  TomTerm newVariable = `Variable(concOption(),PositionName(path),type2,concConstraint());

                  //System.out.println("newVariable = " + newVariable);

                  introducedVariable.add(newVariable);
                  newElt = newVariable;
                }
              }
            }
            newArgs = `concSlot(newArgs*,PairSlotAppl(elt.getSlotName(),newElt));
            args = args.getTailconcSlot();
          }
        } else {
          newArgs = TomBase.mergeTomListWithSlotList(abstractPatternList(TomBase.slotListToTomList(`arguments),abstractedPattern,introducedVariable),`arguments);
        }
        abstractedTerm = subject.setSlots(newArgs);
      }
    } // end match
    return abstractedTerm;
  }

  private TomList abstractPatternList(TomList subjectList, ArrayList abstractedPattern, ArrayList introducedVariable)  {
    %match(subjectList) {
      concTomTerm() -> { return subjectList; }
      concTomTerm(head,tail*) -> {
        TomTerm newElt = abstractPattern(`head,abstractedPattern,introducedVariable);
        TomList tl = abstractPatternList(`tail,abstractedPattern,introducedVariable);
        return `concTomTerm(newElt,tl*);
      }
    }
    throw new TomRuntimeException("abstractPatternList: " + subjectList);
  }  
  
  /*
   * add a prefix (tom_) to back-quoted variables which comes from the lhs
   */
  %strategy findRenameVariable(context:Collection) extends `Identity() {
    visit TomTerm {
      var@(Variable|VariableStar)[AstName=astName@Name(name)] -> {
        if(context.contains(`astName)) {
          return `var.setAstName(`Name(ASTFactory.makeTomVariableName(name)));
        }
      }
    }

    visit Instruction {
      CompiledPattern(patternList,instruction) -> {
        Map map = TomBase.collectMultiplicity(`patternList);
        Collection newContext = new HashSet(map.keySet());
        newContext.addAll(context);
        //System.out.println("newContext = " + newContext);
        return (Instruction)`TopDown(findRenameVariable(newContext)).visitLight(`instruction);
      }
    }
  }
}
