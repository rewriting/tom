/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
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

package jtom.compiler;
  
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import jtom.TomBase;
import jtom.adt.*;
import jtom.runtime.Collect1;
import jtom.runtime.Replace1;
import aterm.ATerm;
import jtom.exception.TomRuntimeException;

public class TomKernelCompiler extends TomBase {

  private boolean supportedBlock = false, supportedGoto = false, debugMode = false;

  public TomKernelCompiler(jtom.TomEnvironment environment,
                           boolean supportedBlock,
                           boolean supportedGoto, 
                           boolean debugMode) {
    super(environment);
    this.supportedBlock = supportedBlock;
    this.supportedGoto = supportedGoto;
    this.debugMode = debugMode;
  }

// ------------------------------------------------------------
  %include { ../adt/TomSignature.tom }
// ------------------------------------------------------------
 
    /* 
     * preProcessing:
     *
     * replaces MakeTerm
     */

  private int matchNumber = 0;

  private OptionList option() {
    return ast().makeOption();
  }

  Replace1 replace_preProcessing = new Replace1() {
      public ATerm apply(ATerm t) { return preProcessing((TomTerm)t); }
    };
  
  Replace1 replace_preProcessing_makeTerm = new Replace1() {
      public ATerm apply(ATerm t) {
        TomTerm subject = (TomTerm)t;
        return preProcessing(`MakeTerm(subject));
      }
    }; 

  public TomTerm preProcessing(TomTerm subject) {
      //%variable
      //System.out.println("preProcessing subject: " + subject);
    %match(TomTerm subject) {
      Tom(l) -> {
        return `Tom(tomListMap(l,replace_preProcessing));
      }
       
      MakeTerm(var@Variable[astName=name]) -> {
        return var;
      }    

      MakeTerm(var@VariableStar[astName=name]) -> {
        return var;
      }

      MakeTerm(Appl(optionList,(name@Name(tomName)),termArgs)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomList newTermArgs = tomListMap(termArgs,replace_preProcessing_makeTerm);

        if(tomSymbol==null || isDefinedSymbol(tomSymbol)) {
          return `FunctionCall(name,newTermArgs);
        } else {
          if(isListOperator(tomSymbol)) {
            return `BuildList(name,newTermArgs);
          } else if(isArrayOperator(tomSymbol)) {
            return `BuildArray(name,newTermArgs);
          } else {
            return `BuildTerm(name,newTermArgs);
          }
        }
      }

        // default rule
      t -> {
          //System.out.println("preProcessing default: " + t);
        return t;
      }
    }
  }

  private TomName getLabel(OptionList list) {
    while(!list.isEmpty()) {
      Option subject = list.getHead();
      %match(Option subject) {
        Label(name@Name[]) -> { return name; }
        _ -> { list = list.getTail(); }
      }
    }
    return null;
  }
  
    /* 
     * compileMatching:
     *
     * compiles Match into and automaton
     */
 
  public TomTerm compileMatching(TomTerm subject) {
      //%variable
    Replace1 replace_compileMatching = new Replace1() {
        public ATerm apply(ATerm t) { return compileMatching((TomTerm)t); }
      }; 

    %match(TomTerm subject) {
      Tom(l) -> {
        return `Tom(tomListMap(l,replace_compileMatching));
      }

      TomInclude(l) -> {
        return `TomInclude(tomListMap(l,replace_compileMatching));
      }
      
      Match(SubjectList(l1),PatternList(l2), optionList)  -> {
        boolean generatedMatch = false;
        String currentDebugKey = "noDebug";
        if(debugMode) {
          generatedMatch = hasGeneratedMatch(optionList);
          Option orgTrack = findOriginTracking(optionList);
          currentDebugKey = orgTrack.getFileName().getString() + orgTrack.getLine();
        }
        
        TomList patternList, actionList;
        TomList automataList = empty();
        ArrayList list;
        TomNumberList path = tsf().makeTomNumberList();

        matchNumber++;
        path = (TomNumberList) path.append(`MatchNumber(makeNumber(matchNumber)));
        
          /*
           * create a list of declaration
           * collect and declare TOM variables (from patterns)
           * collect match variables (from match(t1,...,tn))
           * declare and assign intern variable (_1 = t1,..., _n=tn)
           */
        TomList matchDeclarationList = empty();
        TomList matchAssignementList = empty();
        int index = 1;

        while(!l1.isEmpty()) {
          TomTerm tlVariable = l1.getHead();
          matchBlock: {
            %match(TomTerm tlVariable) { 
              Variable(option,_,variableType) -> {
                TomTerm variable = `Variable(option,PositionName(appendNumber(index,path)),variableType);
                matchDeclarationList = append(`Declaration(variable),matchDeclarationList);
                if (!generatedMatch) {
                  matchAssignementList = appendInstruction(`AssignMatchSubject(variable,TomTermToExpression(tlVariable)),matchAssignementList);
                } else {
                  matchAssignementList = appendInstruction(`Assign(variable,TomTermToExpression(tlVariable)),matchAssignementList);
                }
                break matchBlock;
              }

              BuildTerm(Name(tomName),_) |
              BuildList(Name(tomName),_) |
              FunctionCall(Name(tomName),_) -> {
                TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
                TomType tomType = getSymbolCodomain(tomSymbol);
                TomTerm variable = `Variable(option(),PositionName(appendNumber(index,path)),tomType);
                matchDeclarationList = append(`Declaration(variable),matchDeclarationList);
                matchAssignementList = appendInstruction(`Assign(variable,TomTermToExpression(tlVariable)),matchAssignementList);
                break matchBlock;
              }

              _ -> {
                System.out.println("compileMatching: strange term: " + tlVariable);
                break matchBlock;
              }
            }
          } // end matchBlock
          index++;
          l1 = l1.getTail();
        }
        
        matchDeclarationList = concat(matchDeclarationList,matchAssignementList);
  
          /*
           * for each pattern action (<term> -> <action>)
           * build a matching automata
           */
        int actionNumber = 0;
        boolean firstCall=true;
        boolean defaultPA =false;
        while(!l2.isEmpty()) {
          actionNumber++;
          TomTerm pa = l2.getHead();
          defaultPA = hasDefaultCase(pa.getOption());
          patternList = pa.getTermList().getTomList();
          if (debugMode && defaultPA) {
              // replace success by leaving structure
            TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.patternSuccess(\""+currentDebugKey+"\");\njtom.debug.TomDebugger.debugger.leavingStructure(\""+currentDebugKey+"\");\n");
            TomList tail = pa.getTom().getTomList().getTail();
            actionList = `manyTomList(TargetLanguageToTomTerm(tl), tail);
          } else {
            actionList = pa.getTom().getTomList();
          }
          
          
            //System.out.println("patternList   = " + patternList);
            //System.out.println("actionList = " + actionList);
          if(patternList==null || actionList==null) {
            System.out.println("TomKernelCompiler: null value");
			throw new TomRuntimeException(new Throwable("TomKernelCompiler: null value"));
          }
          
            // compile nested match constructs
          actionList = tomListMap(actionList,replace_compileMatching);
            //System.out.println("patternList      = " + patternList);
            //System.out.println("actionNumber  = " + actionNumber);
            //System.out.println("action        = " + actionList);
          TomList patternsDeclarationList = empty();
          Collection variableCollection = new HashSet();
          collectVariable(variableCollection,`Tom(patternList));
          
          Iterator it = variableCollection.iterator();
          while(it.hasNext()) {
            TomTerm tmpsubject = (TomTerm)it.next();
            patternsDeclarationList = append(`Declaration(tmpsubject),patternsDeclarationList);
              //System.out.println("*** " + patternsDeclarationList);
          }


           TomNumberList numberList = (TomNumberList) path.append(`PatternNumber(makeNumber(actionNumber)));
         
          TomList instructionList;
          instructionList = genMatchingAutomataFromPatternList(patternList,path,1,actionList,true);
            //firstCall = false;
          TomList declarationInstructionList; 
          declarationInstructionList = concat(patternsDeclarationList,instructionList);
          OptionList automataOptionList = `concOption(Debug(Name(currentDebugKey)));

          TomName label = getLabel(pa.getOption());
          if(label != null) {
            automataOptionList = `manyOptionList(Label(label),automataOptionList);
          }
          if(defaultPA) {
            automataOptionList = `manyOptionList(DefaultCase(),automataOptionList);
          }
          
          TomTerm automata = `Automata(automataOptionList,numberList,declarationInstructionList);
            //System.out.println("automata = " + automata);
          
          automataList = append(automata,automataList);
          l2 = l2.getTail();
        }

          /*
           * return the compiled MATCH construction
           */

        TomList astAutomataList = automataListCompileMatchingList(automataList, generatedMatch);
        return `CompiledMatch(matchDeclarationList,astAutomataList, optionList);
      }

        // default rule
      t -> {
          //System.out.println("default: " + t);
        return t;
      }
    }
  }

  private TomList automataListCompileMatchingList(TomList automataList, boolean generatedMatch) {
      //%variable
    
    %match(TomList automataList) {
        //conc()      -> { return empty(); }
        //conc(Automata(numberList,instList),l*)  -> {
      emptyTomList()      -> { return empty(); }
      manyTomList(Automata(optionList,numberList,instList),l)  -> {
        TomList newList = automataListCompileMatchingList(l, generatedMatch);
        if(supportedGoto) {
          if(!generatedMatch && debugMode) {
            String debugKey = getDebug(optionList);
            boolean defaultCase = hasDefaultCase(optionList);
            TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.enteringPattern(\""+debugKey+"\");\n");
            instList = `cons(TargetLanguageToTomTerm(tl), instList);
            if(!defaultCase) {
              tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.leavingPattern(\""+debugKey+"\");\n");
              TomList list = `cons(TargetLanguageToTomTerm(tl), emptyTomList());
              instList = concat(instList, list);
            }
          }

          Instruction namedBlock = `NamedBlock(getBlockName(numberList), instList);
          TomName label = getLabel(optionList);
          if(label != null) {
            namedBlock = `NamedBlock(label.getString(),cons(InstructionToTomTerm(namedBlock),empty()));
          }
          TomList list = cons(`InstructionToTomTerm(namedBlock),empty());
          return cons(`CompiledPattern(list), newList);
        } else {
          TomList result = empty();
          TomTerm variableAST = getBlockVariable(numberList);
          result = append(`Declaration(variableAST),result);
          result = appendInstruction(`Assign(variableAST, TrueTL()),result);
          if(supportedBlock) { // Test
            result = appendInstruction(`OpenBlock(),result);
          }
          result = concat(result,instList);
          if(supportedBlock) { // Test
            result = appendInstruction(`CloseBlock(),result);
          }
          result = cons(`CompiledPattern(result),newList);
          return result;
        }
      }
    }
    return null;
  }

  private String getBlockName(TomNumberList numberList) {
    String name = numberListToIdentifier(numberList);
    return name;
  }

  private TomTerm getBlockVariable(TomNumberList numberList) {
    String name = numberListToIdentifier(numberList);
    return `Variable(option(),Name(name),getBoolType());
  }
  
    /*
     * ------------------------------------------------------------
     * Generate a matching automaton
     * ------------------------------------------------------------
     */

  TomList genMatchingAutomataFromPatternList(TomList termList,
                                             TomNumberList path,
                                             int indexTerm,
                                             TomList actionList,
                                             boolean gsa) {
    TomList result = empty();
      //%variable
    if(termList.isEmpty()) {
      if(gsa) {
          // insert the semantic action
        Instruction action = `Action(actionList);
        result = appendInstruction(action,result);
      }
    } else {
      TomTerm head = termList.getHead();
      TomList tail = termList.getTail();
      TomList newSubActionList = genMatchingAutomataFromPatternList(tail,path,indexTerm+1,actionList,gsa);
      TomNumberList newPath    = appendNumber(indexTerm,path);
      TomList newActionList    = genMatchingAutomataFromPattern(head,newPath,newSubActionList,gsa);
      result                   = concat(result,newActionList);
    }
    return result;
  }

  TomList genMatchingAutomataFromPattern(TomTerm term,
                                         TomNumberList path,
                                         TomList actionList,
                                         boolean gsa) {
    TomList result = empty();
      //%variable
    matchBlock: {
      %match(TomTerm term) {
        var@Variable(optionList, _, termType) |
        var@UnamedVariable(optionList, termType) -> {
          Expression source = `TomTermToExpression(Variable(option(),PositionName(path),termType));
          result = addAnnotedAssignement(optionList, source, var, result);
          if(gsa) {
            result = appendInstruction(`Action(actionList),result);
          }
          break matchBlock; 
        }

        Appl(optionList,(Name(tomName)),termArgs) -> {
          TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
          TomName termNameAST = tomSymbol.getAstName();
          TomTypeList termTypeList = tomSymbol.getTypesToType().getDomain();
          TomType termType = tomSymbol.getTypesToType().getCodomain();
          OptionList termOptionList = tomSymbol.getOption();
          
          TomTerm annotedVariable = getAnnotedVariable(optionList);

            // SUCCES
          TomList declarationList = empty();
          TomList assignementList = empty();
          TomList annotedAssignementList = empty();
          
          int indexSubterm = 0;
          TomTerm subjectVariableAST =  `Variable(option(),PositionName(path),termType);

          TomList automataList  = null;
          TomList succesList    = empty();
          
          if(isListOperator(tomSymbol)) {
            int tmpIndexSubterm = 1;
            automataList = genListMatchingAutomata(tomSymbol,
                                                   termArgs,path,actionList,
                                                   gsa,subjectVariableAST, tmpIndexSubterm);
          } else if(isArrayOperator(tomSymbol)) {
            int tmpIndexSubterm = 1;
            automataList = genArrayMatchingAutomata(tomSymbol,
                                                    termArgs,path,actionList,
                                                    gsa,subjectVariableAST, subjectVariableAST,
                                                    tmpIndexSubterm);
          } else {
            automataList = genMatchingAutomataFromPatternList(termArgs,path,1,actionList,gsa);
          
            TomList termArgList = termArgs;
            while(!termTypeList.isEmpty()) {
              TomType subtermType    = termTypeList.getHead();
              TomTerm subtermArg     = termArgList.getHead();

              if(subtermArg == null) {
                System.out.println("term         = " + term);
                System.out.println("termTypeList = " + termTypeList);
                System.out.println("termArgList  = " + termArgList);
              }
              
              if(subtermArg.isUnamedVariable() && !isAnnotedVariable(subtermArg)) {
                  /* This is an optimisation */
                  /* Do not assign the subterm: skip the subterm */
              } else {
                TomNumberList newPath  = appendNumber(indexSubterm+1,path);
                TomTerm newVariableAST = `Variable(option(),PositionName(newPath),subtermType);
                TomTerm declaration    = `Declaration(newVariableAST);
                declarationList        = append(declaration,declarationList);
                
                Expression getSubtermAST;
                TomName slotName = getSlotName(tomSymbol, indexSubterm);
                if(slotName == null) {
                  getSubtermAST = `GetSubterm(subjectVariableAST,makeNumber(indexSubterm));
                } else {
                  getSubtermAST = `GetSlot(termNameAST,slotName.getString(),subjectVariableAST);
                }
                
                Instruction assignement = `Assign(newVariableAST,getSubtermAST);
                assignementList = appendInstruction(assignement,assignementList);
              }
              indexSubterm++;
              termTypeList = termTypeList.getTail();
              termArgList = termArgList.getTail();
            }
            
            succesList = concat(succesList,declarationList);
            succesList = concat(succesList,assignementList);
          }
          
            // generate an assignement for annoted variables
          if(annotedVariable != null) {
            Instruction assignement = `Assign(annotedVariable,TomTermToExpression(subjectVariableAST));
            annotedAssignementList = appendInstruction(assignement,annotedAssignementList);
          }
          
          succesList = concat(succesList,annotedAssignementList);
          succesList = concat(succesList,automataList);
          
          Expression cond = `EqualFunctionSymbol(subjectVariableAST,term);
          Instruction test = `IfThenElse(cond, succesList, empty());
          result = appendInstruction(test,result);
          
          break matchBlock;
        }
        
        _ -> {
          System.out.println("GenTermMatchingAutomata strange term: " + term);
          throw new TomRuntimeException(new Throwable("GenTermMatchingAutomata strange term: " + term));
        }
      }
    } // end matchBlock 

      //System.out.println("*** result = " + result);
    return result;
  }

  private TomList addAnnotedAssignement(OptionList optionList,
                                        Expression source,
                                        TomTerm dest,
                                        TomList result) {
    TomTerm annotedVariable = getAnnotedVariable(optionList);
    if(annotedVariable != null) {
      result = appendInstruction(`Assign(annotedVariable,source),result);
    }
    result = appendInstruction(`Assign(dest,source),result);
    return result;
  }
  
  TomList genListMatchingAutomata(TomSymbol symbol,
                                  TomList termList,
                                  TomNumberList oldPath,
                                  TomList actionList,
                                  boolean generateSemanticAction,
                                  TomTerm subjectListName,
                                  int indexTerm) {
    TomTerm term;
    TomList result = empty();
    TomTerm variableListAST = null;
    if(indexTerm > 1) {
      variableListAST = subjectListName;
    } else {
      TomNumberList pathList = (TomNumberList) oldPath.append(`ListNumber(makeNumber(indexTerm)));
      
      %match(TomTerm subjectListName) {
        Variable(option,_, termType) -> {
          variableListAST = `Variable(option(),PositionName(pathList),termType);
        }
      }
      result = append(`Declaration(variableListAST),result);
      result = appendInstruction(`Assign(variableListAST,TomTermToExpression(subjectListName)),result);

      subjectListName = variableListAST;
    } 
    
    TomList subList;
    if(termList.isEmpty()) {
      subList = empty();
    } else {
      subList = genListMatchingAutomata(symbol,
                                        termList.getTail(), oldPath, actionList,
                                        generateSemanticAction,variableListAST,indexTerm+1);
    }
      //System.out.println("\ntermList = " + termList);
      //System.out.println("*** genListMatchingAutomata"); 
    matchBlock: {
      %match(TomList termList) {
        emptyTomList() -> {
            /*
             * generate:
             * ---------
             * if(IS_EMPTY_TomList(subjectList)) {
             *   ...
             * }
             */
          if(indexTerm > 1) {
            break matchBlock;
          } else {
            if(generateSemanticAction) {
              subList = appendInstruction(`Action( actionList),subList);
            }
            Expression cond = `IsEmptyList(subjectListName);
            Instruction test = `IfThenElse(cond, subList, empty());
            result = appendInstruction(test,result);
            break matchBlock;
          }
        }
        
        manyTomList(var@Variable(optionList,_, termType),termTail) |
        manyTomList(var@UnamedVariable(optionList, termType),termTail) -> {
            /*
             * generate:
             * ---------
             * if(!IS_EMPTY_TomList(subjectList)) {
             *   TomTerm x_j = (TomTerm) GET_HEAD_TomList(subjectList);
             *   subjectList =  (TomList) GET_TAIL_TomList(subjectList);
             *   ...
             * }
             */            
          Expression source = `GetHead(subjectListName);
          TomList assignementList = empty();
          assignementList = addAnnotedAssignement(optionList, source, var, assignementList);
          assignementList = appendInstruction(`Assign(subjectListName,GetTail(subjectListName)),assignementList);
          TomList succesList;
          if(termTail.isEmpty()) {
              /*
               *   if(IS_EMPTY_TomList(subjectList)) {
               *     ...
               *   }
               */
            if(generateSemanticAction) {
              subList = appendInstruction(`Action(actionList),subList);
            }
            Instruction test = `IfThenElse(IsEmptyList(subjectListName),
                                              subList, empty());
            succesList = appendInstruction(test,assignementList);
          } else {
            succesList = concat(assignementList,subList);
          }
          
          Instruction test = `IfThenElse(Not(IsEmptyList(subjectListName)),
                                         succesList, empty());
          result = appendInstruction(test,result);
          break matchBlock;
        }
        
        manyTomList(var@VariableStar(optionList,_, termType),termTail) |
        manyTomList(var@UnamedVariableStar(optionList, termType),termTail) -> {
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * E_n = subjectList;
               * ...
               */
            if(generateSemanticAction) {
              subList = appendInstruction(`Action( actionList),subList);
            }
            Expression source = `TomTermToExpression(subjectListName);
            result = addAnnotedAssignement(optionList, source, var, result);
            result = concat(result,subList);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * TomList begin_i = subjectList;
               * TomList end_i   = subjectList;
               * do {
               *   * SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(begin_i,end_i);
               *   ...
               *   if(!IS_EMPTY_TomList(end_i) )
               *     end_i = (TomList) GET_TAIL_TomList(end_i);
               *   subjectList = end_i;
               * } while( !IS_EMPTY_TomList(subjectList) )
               */
            TomNumberList pathBegin = (TomNumberList) oldPath.append(`Begin(makeNumber(indexTerm)));
            TomNumberList pathEnd = (TomNumberList) oldPath.append(`End(makeNumber(indexTerm)));
            TomTerm variableBeginAST = `Variable(option(),PositionName(pathBegin),termType);
            TomTerm variableEndAST   = `Variable(option(),PositionName(pathEnd),termType);
            TomList declarationList = empty();
            declarationList = append(`Declaration(variableBeginAST),declarationList);
            declarationList = append(`Declaration(variableEndAST),declarationList);
            TomList assignementList = empty();
            assignementList = appendInstruction(`Assign(variableBeginAST,TomTermToExpression(subjectListName)),assignementList);
            assignementList = appendInstruction(`Assign(variableEndAST,TomTermToExpression(subjectListName)),assignementList);
            
            Expression source = `GetSliceList(symbol.getAstName(),variableBeginAST,variableEndAST);
            TomList doList = empty();
            doList = addAnnotedAssignement(optionList, source, var, doList);
            doList = concat(doList,subList);

            Expression cond1 = `Not(IsEmptyList(variableEndAST));
            Instruction test1 = `IfThenElse(cond1, cons(InstructionToTomTerm(Assign(variableEndAST,GetTail(variableEndAST))),empty()), empty());
            doList = appendInstruction(test1,doList);
            doList = appendInstruction(`Assign(subjectListName,TomTermToExpression(variableEndAST)),doList);
            
            Expression cond2 = `Not(IsEmptyList( subjectListName));
            Instruction doWhile = `DoWhile(doList,cond2);
            
            result = appendInstruction(doWhile,concat(concat(declarationList,result),assignementList));

            break matchBlock;

          }

        }
        
        _ -> {
          System.out.println("GenListMatchingAutomata strange termList: " + termList);
          throw new TomRuntimeException(new Throwable("GenListMatchingAutomata strange termList: " + termList));
        }
      }
    } // end matchBlock
    return result;
  }

  TomList genArrayMatchingAutomata(TomSymbol symbol,
                                   TomList termList,
                                   TomNumberList oldPath,
                                   TomList actionList,
                                   boolean generateSemanticAction,
                                   TomTerm subjectListName,
                                   TomTerm subjectListIndex,
                                   int indexTerm) {
    TomTerm term;
    TomList result = empty();
      //%variable
    
    if(termList.isEmpty()) {
      return result;
    }
    
    TomTerm variableListAST = null;
    TomTerm variableIndexAST = null;
    Expression glZero = `TomTermToExpression(TargetLanguageToTomTerm(ITL("0")));
    if(indexTerm > 1) {
      variableListAST = subjectListName;
      variableIndexAST = subjectListIndex;
    } else {
      TomNumberList pathList = (TomNumberList) oldPath.append(`ListNumber(makeNumber(indexTerm)));
      TomNumberList pathIndex = (TomNumberList) oldPath.append(`IndexNumber(makeNumber(indexTerm)));

      matchBlock: {
        %match(TomTerm subjectListName) {
          Variable(option, _, termType) -> {
            variableListAST = `Variable(option(),PositionName(pathList),termType);
              /* TODO: other termType */
            variableIndexAST = `Variable(option(),PositionName(pathIndex),getIntType());
            break matchBlock;
          }
          _ -> {
            System.out.println("GenArrayMatchingAutomata strange subjectListName: " + subjectListName);
            throw new TomRuntimeException(new Throwable("GenArrayMatchingAutomata strange subjectListName: " + subjectListName));
          }
        }
      }
      result = append(`Declaration(variableListAST),result);
      result = append(`Declaration(variableIndexAST),result);
      result = appendInstruction(`Assign(variableListAST,TomTermToExpression(subjectListName)),result);
      result = appendInstruction(`Assign(variableIndexAST, glZero),result);

      subjectListName  = variableListAST;
      subjectListIndex = variableIndexAST;
    } 
    
    TomList subList = genArrayMatchingAutomata(symbol,
                                               termList.getTail(), oldPath, actionList,
                                               generateSemanticAction,
                                               variableListAST,variableIndexAST,indexTerm+1);
      //System.out.println("\ntermList = " + termList);
      //System.out.println("*** genArrayMatchingAutomata");
    matchBlock: {
      %match(TomList termList) {
        
        manyTomList(var@Variable(optionList,_, termType),termTail) |
        manyTomList(var@UnamedVariable(optionList, termType),termTail) -> {
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * if(_match1_1_index_1 < GET_SIZE_L(_match1_1_list_1)) {
               *   TomTerm x_j = (TomTerm) GET_ELEMENT_L(_match1_1_list_1,_match1_1_index_1);
               *   _match1_1_index_1++;;
               *   if(_match1_1_index_1 = GET_SIZE_L(_match1_1_list_1)) {
               *     ...
               *   }
               * }
               */
            Expression source = `GetElement(subjectListName,subjectListIndex);
            TomList assignementList = empty();
            assignementList = addAnnotedAssignement(optionList, source, var, assignementList);
            assignementList = appendInstruction(`Increment(subjectListIndex),assignementList);
            
            if(generateSemanticAction) {
              subList = appendInstruction(`Action(actionList),subList);
            }
            
            Expression cond = `IsEmptyArray( subjectListName,subjectListIndex);
            Instruction test = `IfThenElse(cond, subList, empty());
            TomList succesList = appendInstruction(test,assignementList);
            
            cond = `Not(IsEmptyArray( subjectListName,subjectListIndex));
            test = `IfThenElse(cond, succesList, empty());
            result = appendInstruction(test,result);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * if(!IS_EMPTY_TomList(subjectList,subjectIndex)) {
               *   TomTerm x_j = (TomTerm) GET_ELEMENT_TomList(subjectList,subjectIndex);
               *   subjectIndex++;
               *   ...
               * }
               */
            TomList assignementList = empty();
            TomList succesList      = empty();

            Expression source = `GetElement(subjectListName,subjectListIndex);
            assignementList = addAnnotedAssignement(optionList, source, var, assignementList);
            assignementList = appendInstruction(`Increment(subjectListIndex),assignementList);
            
            succesList = concat(concat(succesList,assignementList),subList);
            Expression cond = `Not(IsEmptyArray( subjectListName,subjectListIndex));
            Instruction test = `IfThenElse(cond, succesList, empty());
            
            result = appendInstruction(test,result);
            break matchBlock;
          }
        }
        
        manyTomList(var@VariableStar(optionList,_, termType),termTail) |
        manyTomList(var@UnamedVariableStar(optionList, termType),termTail) -> {
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * E_n = GET_SLICE_L(subjectList,subjectIndex,GET_SIZE_L(subjectList));
               * ...
               */
            if(generateSemanticAction) {
              subList = appendInstruction(`Action( actionList),subList);
            }
            
            Expression source = `GetSliceArray(
              symbol.getAstName(),subjectListName,
              subjectListIndex,
              ExpressionToTomTerm(GetSize(subjectListName))
              );
            result = addAnnotedAssignement(optionList, source, var, result);
            result = concat(result,subList);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * int begin_i = subjectIndex;
               * int end_i   = subjectIndex;
               * do {
               *   * SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(subjectList,begin_i,end_i);
               *   ...
               *   end_i++;
               *   subjectIndex = end_i;
               * } while( !IS_EMPTY_TomList(subjectList) )
               */

            TomNumberList pathBegin = (TomNumberList) oldPath.append(`Begin(makeNumber(indexTerm)));
            TomNumberList pathEnd = (TomNumberList) oldPath.append(`End(makeNumber(indexTerm)));
              /* TODO: termType */
            TomTerm variableBeginAST = `Variable(option(),PositionName(pathBegin),getIntType());
            TomTerm variableEndAST   = `Variable(option(),PositionName(pathEnd),getIntType());
            TomList declarationList = empty();
            declarationList = append(`Declaration(variableBeginAST),declarationList);
            declarationList = append(`Declaration(variableEndAST),declarationList);
            TomList assignementList = empty();
            assignementList = appendInstruction(`Assign(variableBeginAST,TomTermToExpression(subjectListIndex)),assignementList);
            assignementList = appendInstruction(`Assign(variableEndAST,TomTermToExpression(subjectListIndex)),assignementList);
            
            Expression source = `GetSliceArray(symbol.getAstName(),
                                              subjectListName,variableBeginAST,
                                              variableEndAST);
            TomList doList = empty();
            doList = addAnnotedAssignement(optionList, source, var, doList);
            doList = concat(doList,subList);
            doList = appendInstruction(`Increment(variableEndAST),doList);
            doList = appendInstruction(`Assign(subjectListIndex,TomTermToExpression(variableEndAST)),doList); 
            
            Expression cond = `Not(IsEmptyArray(subjectListName, subjectListIndex));
            Instruction doWhile = `DoWhile(doList,cond);
            
            TomList tmpResult = empty();
            if(supportedBlock) {
              tmpResult = appendInstruction(`OpenBlock(),tmpResult);
            }
            tmpResult = concat(tmpResult,declarationList);
            tmpResult = concat(tmpResult,result);
            tmpResult = concat(tmpResult,assignementList);
            tmpResult = appendInstruction(doWhile,tmpResult);
            if(supportedBlock) {
              tmpResult = appendInstruction(`CloseBlock(),tmpResult);
            }
            result = tmpResult;
            break matchBlock;
          }
        }
        
        _ -> {
          System.out.println("GenArrayMatchingAutomata strange termList: " + termList);
		  throw new TomRuntimeException(new Throwable("GenArrayMatchingAutomata strange termList: " + termList));
        }
      }
    } // end matchBlock
    return result;
  }


    /* 
     * postProcessing: passCompiledTermTransformation
     *
     * transform a compiledTerm
     * 2 phases:
     *   - collection of Declaration
     *   - replace LocalVariable and remove Declaration
     */
  
  public TomTerm postProcessing(TomTerm subject) {
    TomTerm res;
    ArrayList list = new ArrayList();
    traversalCollectDeclaration(list,subject);
      //System.out.println("list size = " + list.size());
    res = traversalReplaceLocalVariable(list,subject);
    return res;
  }
    
  private TomTerm traversalCollectDeclaration(ArrayList list, TomTerm subject) {
      //%variable
    %match(TomTerm subject) {
      Tom(l) -> {
        return `Tom(traversalCollectDeclarationList(list, l));
      } 
      
      LocalVariable() -> {
          //System.out.println("Detect LocalVariable");
        
        Collection c = new HashSet();
        list.add(c);
        collectDeclaration(c,subject);
        return null;
      }
      
      t -> {
          //System.out.println("default: " + t);
        if(!list.isEmpty()) {
          Collection c = (Collection) list.get(list.size()-1);
          collectDeclaration(c,subject);
        }
        return t;
      }
    }
  }

  public void collectDeclaration(final Collection collection, TomTerm subject) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
            //%variable
          %match(TomTerm t) {
            Declaration[] -> {
              collection.add(t);
              return false;
            }
            _ -> { return true; }
          }
        } 
      }; // end new
    
    traversal().genericCollect(subject, collect); 
  } 

  private boolean removeDeclaration = false;
  private TomTerm traversalReplaceLocalVariable(ArrayList list, TomTerm subject) {
      //%variable
    %match(TomTerm subject) {
      Tom(l) -> {
        return `Tom(traversalReplaceLocalVariableList(list, l));
      } 
      
      LocalVariable() -> {
          //System.out.println("Replace LocalVariable");

        Map map = (Map)list.get(0);
        list.remove(0);

        Collection c = map.values();
        Iterator it = c.iterator();
        TomList declarationList = empty();
        while(it.hasNext()) {
          declarationList = cons((TomTerm)it.next(),declarationList);
        }

          //System.out.println("declarationList = " + declarationList);
        removeDeclaration = true;
        return `Tom(declarationList);
      }

        //Declaration[] -> {
        //System.out.println("Remove Declaration");
        //return MAKE_Tom(empty());
        //}
      
      t -> {
        TomTerm res = t;
          //res = removeDeclaration(t);
        
        if(removeDeclaration) {
          res = removeDeclaration(t);
        }
        
          //System.out.println("\ndefault:\nt   = " + t + "\nres = " + res);
        return res;
      }
    }
  }

    private TomList traversalCollectDeclarationList(ArrayList list,TomList subject) {
      //%variable
    if(subject.isEmpty()) {
      return subject;
    }
    TomTerm t = subject.getHead();
    TomList l = subject.getTail();
    return cons(traversalCollectDeclaration(list,t),
                traversalCollectDeclarationList(list,l));
  }

  private TomList traversalReplaceLocalVariableList(ArrayList list,TomList subject) {
      //%variable
    if(subject.isEmpty()) {
      return subject;
    }
    TomTerm t = subject.getHead();
    TomList l = subject.getTail();
    return cons(traversalReplaceLocalVariable(list,t),
                traversalReplaceLocalVariableList(list,l));
  }
   
  public TomTerm removeDeclaration(TomTerm subject) {
    TomTerm res = subject;
      //System.out.println("*** removeDeclaration");
                  
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm t) {
            //%variable
          %match(TomTerm t) {
            Declaration[] -> {
                //System.out.println("Remove Declaration");
              return `Tom(empty());
            }

            other -> {
              System.out.println("removeDeclaration this = " + this);
                //return other;
              return (TomTerm) traversal().genericTraversal(other,this);
            }
          }
        } 
      }; // end new
    
      //return genericReplace(subject, replace);
    try {
      res = (TomTerm) replace.apply(subject);
    } catch(Exception e) {
      System.out.println("removeDeclaration: error");
      throw new TomRuntimeException(new Throwable("removeDeclaration: error"));
    }
    return res;
  }
  
} // end of class
  


    
