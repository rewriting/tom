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

package jtom.compiler;
  
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import jtom.TomBase;
import jtom.adt.tomsignature.types.*;
import jtom.runtime.Collect1;
import jtom.runtime.Replace1;
import aterm.*;
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
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------
 
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


    /* 
     * preProcessing:
     * replaces MakeTerm by BuildList, BuildArray or BuildTerm
     */

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
     * compiles the Match construct into a matching automaton: CompiledMatch
     */
 
  public TomTerm compileMatching(TomTerm subject) {
    Replace1 replace_compileMatching = new Replace1() {
        public ATerm apply(ATerm subject) {
          if(subject instanceof TomTerm) { 
            %match(TomTerm subject) {
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
                TomNumberList rootpath = tsf().makeTomNumberList();
                matchNumber++;
                rootpath = (TomNumberList) rootpath.append(`MatchNumber(makeNumber(matchNumber)));
                
                  /*
                   * for each pattern action (<term>,...,<term> -> <action>)
                   * build a matching automata
                   */
                int actionNumber = 0;
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
                  if(patternList==null || actionList==null) {
                    System.out.println("TomKernelCompiler: null value");
                    throw new TomRuntimeException(new Throwable("TomKernelCompiler: null value"));
                  }
                  
                  /*
                   * compile nested match constructs
                   * given a list of pattern: we build a matching automaton
                   */
                  actionList = tomListMap(actionList,this);
                  TomList instructionList = genMatchingAutomataFromPatternList(patternList,rootpath,1,actionList);
                  OptionList automataOptionList = `concOption(Debug(Name(currentDebugKey)));
                  TomName label = getLabel(pa.getOption());
                  if(label != null) {
                    automataOptionList = `manyOptionList(Label(label),automataOptionList);
                  }
                  if(defaultPA) {
                    automataOptionList = `manyOptionList(DefaultCase(),automataOptionList);
                  }
                  TomNumberList numberList = (TomNumberList) rootpath.append(`PatternNumber(makeNumber(actionNumber)));
                  TomTerm automata = `Automata(automataOptionList,numberList,instructionList);
                    //System.out.println("automata = " + automata);
                  
                  automataList = append(automata,automataList);
                  l2 = l2.getTail();
                }
                
                /*
                 * return the compiled Match construction
                 */
                TomList astAutomataList1 = automataListCompileMatchingList(automataList, generatedMatch);
                TomList astAutomataList2 = `concTomTerm(InstructionToTomTerm(collectVariableFromSubjectList(l1,1,rootpath,UnamedBlock(astAutomataList1))));

                return `CompiledMatch(astAutomataList2, optionList);
              }
              
                // default rule
              _ -> {
                return traversal().genericTraversal(subject,this);
              }
            } // end match
          } else { // not instance of TomTerm
            return traversal().genericTraversal(subject,this);
          }
        } // end apply
      }; // end new
    
    return (TomTerm) replace_compileMatching.apply(subject);
  }

    /*
     * collect match variables (from match(t1,...,tn))
     * create a list of declaration/assignement: v1=t1 ... vn=tn in body
     */
  private Instruction collectVariableFromSubjectList(TomList subjectList, int index, TomNumberList path, Instruction body) {
    %match(TomList subjectList) { 
      emptyTomList() -> { return body; }
      manyTomList(subjectVar@Variable(option,_,variableType),tail) -> {
        body = collectVariableFromSubjectList(tail,index+1,path,body);
        TomTerm variable = `Variable(option,PositionName(appendNumber(index,path)),variableType);
        return `Let(variable,TomTermToExpression(subjectVar),body);
      }

      manyTomList(subjectVar@(BuildTerm|BuildList|FunctionCall)(Name(tomName),_),tail) -> {
        body = collectVariableFromSubjectList(tail,index+1,path,body);
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomType tomType = getSymbolCodomain(tomSymbol);
        TomTerm variable = `Variable(option(),PositionName(appendNumber(index,path)),tomType);
        return `Let(variable,TomTermToExpression(subjectVar),body);
      }

      manyTomList(subjectVar,tail) -> {
        System.out.println("collectVariableFromSubjectList: strange term: " + subjectVar);
      }

    }
    return `Nop();
  }

  /*
   * build a list of instructions from a list of automata
   */
  private TomList automataListCompileMatchingList(TomList automataList, boolean generatedMatch) {
    %match(TomList automataList) {
      emptyTomList() -> { return empty(); }
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
              /*
               * if a label is assigned to a pattern (label:pattern -> action)
               * we generate corresponding labeled-block
               */
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
                                             TomList actionList) {
    %match(TomList termList) { 
      emptyTomList() -> { 
        // insert the semantic action
        return actionList;
      } 
      manyTomList(head,tail) -> {
        TomList newSubActionList = genMatchingAutomataFromPatternList(tail,path,indexTerm+1,actionList);
        TomNumberList newPath    = appendNumber(indexTerm,path);
        TomList newActionList    = genMatchingAutomataFromPattern(head,newPath,newSubActionList);
        return newActionList;
      }
    }
    return termList;
  }

  /*
   * given a pattern, this function generatesthe discrimitation test on the root symbol
   * and recursively calls the algorithm on subterms
   */
  TomList genMatchingAutomataFromPattern(TomTerm term,
                                         TomNumberList path,
                                         TomList actionList) {
    %match(TomTerm term) {
      var@Variable(optionList, _, termType) |
        var@UnamedVariable(optionList, termType) -> {
        Expression source = `TomTermToExpression(Variable(option(),PositionName(path),termType));
        Instruction body = `Action(actionList);
        return appendInstruction(buildAnnotedLet(optionList, source, var, body),empty());
      }
      
      Appl(optionList,nameList@(Name(tomName),_*),termArgs) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomTypeList termTypeList = tomSymbol.getTypesToType().getDomain();
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        
        // SUCCES
        TomTerm subjectVariableAST =  `Variable(option(),PositionName(path),termType);
        Instruction automataInstruction;
        if(isListOperator(tomSymbol)) {
          /*
           * store the subject into an internal variable
           * call genListMatchingAutomata with the new internal variable
           */
          int indexSubterm = 1;
          TomNumberList newPath = (TomNumberList) path.append(`ListNumber(makeNumber(indexSubterm)));
          TomTerm newSubjectVariableAST =  `Variable(option(),PositionName(newPath),termType);
          TomList automataList = genListMatchingAutomata(tomSymbol,
                                                         termArgs,path,actionList,
                                                         newSubjectVariableAST, indexSubterm);
          automataInstruction = `Let(newSubjectVariableAST,
                                     TomTermToExpression(subjectVariableAST),
                                     UnamedBlock(automataList));
        } else if(isArrayOperator(tomSymbol)) {
          int indexSubterm = 1;
          TomList automataList = genArrayMatchingAutomata(tomSymbol,
                                                          termArgs,path,actionList,
                                                          subjectVariableAST, subjectVariableAST,
                                                          indexSubterm);
          automataInstruction = `UnamedBlock(automataList);
        } else {
          int indexSubterm = 0;
          TomList automataList = genMatchingAutomataFromPatternList(termArgs,path,1,actionList);
          automataInstruction = `collectSubtermFromSubjectList(termArgs,termTypeList,tomSymbol,subjectVariableAST,indexSubterm,path,UnamedBlock(automataList)); 
        }
        
        TomTerm annotedVariable = getAnnotedVariable(optionList);
        if(annotedVariable != null) {
          automataInstruction = `Let(annotedVariable,TomTermToExpression(subjectVariableAST),automataInstruction);
        }
        
        TomList succesList = append(`InstructionToTomTerm(automataInstruction),empty());
        Expression cond = `expandDisjunction(EqualFunctionSymbol(subjectVariableAST,term));
        Instruction test = `IfThenElse(cond, succesList, empty());
        return appendInstruction(test,empty());
      }
      
      _ -> {
        System.out.println("GenTermMatchingAutomata strange term: " + term);
        throw new TomRuntimeException(new Throwable("GenTermMatchingAutomata strange term: " + term));
      }
    }
  }

    /*
     * given a list of subject t1,...,tn
     * declare/assign internal matching variables: match_path_i = ti
     */
  private Instruction collectSubtermFromSubjectList(TomList termArgList, TomTypeList termTypeList,
                                                    TomSymbol tomSymbol,TomTerm subjectVariableAST, 
                                                    int indexSubterm, TomNumberList path, Instruction body) {
    TomName termNameAST = tomSymbol.getAstName();
    %match(TomList termArgList) { 
      emptyTomList() -> { return body; }
      
      manyTomList(subtermArg,tail) -> {
        body = collectSubtermFromSubjectList(tail,termTypeList.getTail(),
                                             tomSymbol,subjectVariableAST,
                                             indexSubterm+1,path,body);
        if(subtermArg.isUnamedVariable() && !isAnnotedVariable(subtermArg)) {
            // This is an optimisation 
            // Do not assign the subterm: skip the subterm 
          return body;
        } else {
          TomType subtermType    = termTypeList.getHead();
          Expression getSubtermAST;
          TomName slotName = getSlotName(tomSymbol, indexSubterm);
          if(slotName == null) {
            getSubtermAST = `GetSubterm(subjectVariableAST,makeNumber(indexSubterm));
          } else {
            getSubtermAST = `GetSlot(termNameAST,slotName.getString(),subjectVariableAST);
          }
          TomNumberList newPath  = appendNumber(indexSubterm+1,path);
          TomTerm newVariableAST = `Variable(option(),PositionName(newPath),subtermType);
          return `Let(newVariableAST,getSubtermAST,body);
        }
      }

    }
    return `Nop();
  }

  private Expression expandDisjunction(Expression exp) {
    Expression cond = `FalseTL();
    %match(Expression exp) {
      EqualFunctionSymbol(var@Variable[],Appl(option,nameList,l)) -> {
        while(!nameList.isEmpty()) {
          TomName name = nameList.getHead();
          Expression check = `EqualFunctionSymbol(var,Appl(option,concTomName(name),l));
          cond = `Or(check,cond);
          nameList = nameList.getTail();
        }
      }
    }
    return cond;
  }

  private Instruction buildAnnotedLet(OptionList optionList,
                                      Expression source,
                                      TomTerm dest,
                                      Instruction body) {
    TomTerm annotedVariable = getAnnotedVariable(optionList);
    if(annotedVariable != null) {
      body = `Let(annotedVariable,source,body);
    }
    return `Let(dest,source,body);
  }

  /*
   * TODO: should be removed
   */
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

    /*
     * function which compiles list-matching
     * 
     * symbol:          root symbol
     * termList:        list of subterms
     * oldPath:         path up-to the root symbol
     * actionList:      list of actions to be fired when matching
     * subjectListName: name of the internal variable supposed to store the subject
     * indexTerm:       index of the considered subterm (indexTerm=1 for the first call)
     */
  TomList genListMatchingAutomata(TomSymbol symbol,
                                  TomList termList,
                                  TomNumberList oldPath,
                                  TomList actionList,
                                  TomTerm subjectListName,
                                  int indexTerm) {
    %match(TomList termList) {
      emptyTomList() -> {
        /*
         * generate:
         * ---------
         * if(IS_EMPTY_TomList(subjectList)) {
         *   ...
         * }
         */
        Expression cond = `IsEmptyList(subjectListName);
        Instruction test = `IfThenElse(cond, actionList, empty());
        return appendInstruction(test,empty());
      }
        
      manyTomList(var@Variable(optionList,_, termType),termTail) |
      manyTomList(var@UnamedVariable(optionList, termType),termTail) -> {
        /*
         * generate:
         * ---------
         * if(!IS_EMPTY_TomList(subjectList)) {
         *   Let TomTerm x_j = (TomTerm) GET_HEAD_TomList(subjectList);
         *   subjectList =  (TomList) GET_TAIL_TomList(subjectList);
         *   ...
         * }
         */       
        TomList subActionList = genListMatchingAutomata(symbol,
                                                        termTail, oldPath, actionList,
                                                        subjectListName,indexTerm+1);
     
        TomList assignementList = appendInstruction(`Assign(subjectListName,GetTail(subjectListName)),empty());
        TomList succesList = concat(assignementList,subActionList);
        Instruction test = `IfThenElse(Not(IsEmptyList(subjectListName)),
                                       succesList, empty());
        Expression source = `GetHead(subjectListName);
        return appendInstruction(buildAnnotedLet(optionList, source, var, test),empty());
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
          Expression source = `TomTermToExpression(subjectListName);
          Instruction let = buildAnnotedLet(optionList, source, var, `UnamedBlock(actionList));
          return  appendInstruction(let,empty());
        } else {
          TomList subActionList = genListMatchingAutomata(symbol,
                                                          termTail, oldPath, actionList,
                                                          subjectListName,indexTerm+1);
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
          TomList doList = appendInstruction(buildAnnotedLet(optionList, source, var, `Action(subActionList)),empty());

          Expression cond1 = `Not(IsEmptyList(variableEndAST));
          Instruction test1 = `IfThenElse(cond1, cons(InstructionToTomTerm(Assign(variableEndAST,GetTail(variableEndAST))),empty()), empty());
          doList = appendInstruction(test1,doList);
          doList = appendInstruction(`Assign(subjectListName,TomTermToExpression(variableEndAST)),doList);
          
          Expression cond2 = `Not(IsEmptyList( subjectListName));
          Instruction doWhile = `DoWhile(doList,cond2);
          
          return appendInstruction(doWhile,concat(declarationList,assignementList));
        }

      }
      
      _ -> {
        System.out.println("GenListMatchingAutomata strange termList: " + termList);
        throw new TomRuntimeException(new Throwable("GenListMatchingAutomata strange termList: " + termList));
      }
    }
  }

  TomList genArrayMatchingAutomata(TomSymbol symbol,
                                   TomList termList,
                                   TomNumberList oldPath,
                                   TomList actionList,
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
            
              subList = appendInstruction(`Action(actionList),subList);
            
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
              subList = appendInstruction(`Action( actionList),subList);
            
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
  


    
