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
  
import jtom.TomBase;
import jtom.adt.tomsignature.types.*;
import jtom.runtime.Replace1;
import aterm.*;
import jtom.exception.TomRuntimeException;

public class TomKernelCompiler extends TomBase {

  private boolean debugMode = false;

  public TomKernelCompiler(jtom.TomEnvironment environment, boolean debugMode) {
    super(environment);
    this.debugMode = debugMode;
  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------
 
  private int matchNumber = 0;

  private OptionList option() {
    return ast().makeOption();
  }

  private TomName getLabel(OptionList list) {
    %match(OptionList list) {
      concOption(_*,Label(name@Name[]),_*) -> { return name; }
    }
    return null;
  }
  
    /* 
     * compileMatching:
     * compiles the Match construct into a matching automaton: CompiledMatch
     */

  private Replace1 replace_compileMatching = new Replace1() {
      public ATerm apply(ATerm subject) {
        if(subject instanceof Instruction) {
          %match(Instruction subject) {
            Match(SubjectList(l1),PatternList(l2), optionList)  -> {
              boolean generatedMatch = false;
              String currentDebugKey = "noDebug";
              if(debugMode) {
                generatedMatch = hasGeneratedMatch(optionList);
                Option orgTrack = findOriginTracking(optionList);
                currentDebugKey = orgTrack.getFileName().getString() + orgTrack.getLine();
              }
                
              TomList patternList = null;
              Instruction actionInst = null;
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
                  actionInst = `UnamedBlock(concInstruction(pa.getAction(),TargetLanguageToInstruction(tl)));
                } else {
                  actionInst = pa.getAction();
                }
                if(patternList==null || actionInst==null) {
                  System.out.println("TomKernelCompiler: null value");
                  throw new TomRuntimeException(new Throwable("TomKernelCompiler: null value"));
                }
                  
                  /*
                   * compile nested match constructs
                   * given a list of pattern: we build a matching automaton
                   */
                actionInst = (Instruction) this.apply(actionInst);
                Instruction matchingAutomata = genSyntacticMatchingAutomata(actionInst,
                                                                            patternList,rootpath,1);
                OptionList automataOptionList = `concOption(Debug(Name(currentDebugKey)));
                TomName label = getLabel(pa.getOption());
                if(label != null) {
                  automataOptionList = `manyOptionList(Label(label),automataOptionList);
                }
                if(defaultPA) {
                  automataOptionList = `manyOptionList(DefaultCase(),automataOptionList);
                }
                TomNumberList numberList = (TomNumberList) rootpath.append(`PatternNumber(makeNumber(actionNumber)));
                TomTerm automata = `Automata(automataOptionList,patternList,numberList,matchingAutomata);
                  //System.out.println("automata = " + automata);
                  
                automataList = append(automata,automataList);
                l2 = l2.getTail();
              }
                
                /*
                 * return the compiled Match construction
                 */
              InstructionList astAutomataList = automataListCompileMatchingList(automataList, generatedMatch);
              Instruction astAutomata = collectVariableFromSubjectList(l1,1,rootpath,`AbstractBlock(astAutomataList));
              return `CompiledMatch(astAutomata, optionList);
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

  public TomTerm compileMatching(TomTerm subject) {
    return (TomTerm) replace_compileMatching.apply(subject);
  }

    /*
     * collect match variables (from match(t1,...,tn))
     * create a list of declaration/assignement: v1=t1 ... vn=tn in body
     */
  private Instruction collectVariableFromSubjectList(TomList subjectList, int index, TomNumberList path, Instruction body) {
    %match(TomList subjectList) { 
      emptyTomList() -> { return body; }
      manyTomList(subjectVar@Variable[option=option,astType=variableType],tail) -> {
        body = collectVariableFromSubjectList(tail,index+1,path,body);
        TomTerm variable = `Variable(option,PositionName(appendNumber(index,path)),variableType,concConstraint());
        // the UnamedBlock encapsulation is needed for Caml
        return `Let(variable,Cast(variableType,TomTermToExpression(subjectVar)),UnamedBlock(concInstruction(body)));
      }

      manyTomList(subjectVar@(BuildTerm|BuildList|FunctionCall)(Name(tomName),_),tail) -> {
        body = collectVariableFromSubjectList(tail,index+1,path,body);
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomType tomType = getSymbolCodomain(tomSymbol);
        TomTerm variable = `Variable(option(),PositionName(appendNumber(index,path)),tomType, concConstraint());
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
  private InstructionList automataListCompileMatchingList(TomList automataList, boolean generatedMatch) {
    %match(TomList automataList) {
      emptyTomList() -> { return `emptyInstructionList(); }
      manyTomList(Automata(optionList,patternList,numberList,instruction),l)  -> {
        InstructionList newList = automataListCompileMatchingList(l, generatedMatch);

        if(!generatedMatch && debugMode) {
          String debugKey = getDebug(optionList);
          Instruction tl1 = `TargetLanguageToInstruction(ITL("jtom.debug.TomDebugger.debugger.enteringPattern(\""+debugKey+"\");\n"));
          Instruction tl2 = `TargetLanguageToInstruction(ITL("jtom.debug.TomDebugger.debugger.leavingPattern(\""+debugKey+"\");\n"));
          if(!hasDefaultCase(optionList)) {
            instruction = `UnamedBlock(concInstruction(tl1,instruction,tl2));
          } else {
            instruction = `UnamedBlock(concInstruction(tl1,instruction));
          }
        }
         
        if(getLabel(optionList) != null) {
            /*
             * if a label is assigned to a pattern (label:pattern -> action)
             * we generate corresponding labeled-block
             */
          instruction = `NamedBlock(getLabel(optionList).getString(),
                           concInstruction(instruction));
          					 
        }
        return `concInstruction(CompiledPattern(patternList,instruction), newList*);
      }
    }
    return null;
  }
  
    /*
     * ------------------------------------------------------------
     * Generate a matching automaton
     * ------------------------------------------------------------
     */
  
    /*
     * given a pattern, this function generatesthe discrimitation test on the root symbol
     * and recursively calls the algorithm on subterms
     */
  Instruction genSyntacticMatchingAutomata(Instruction action,
                                           TomList termList,
                                           TomNumberList rootpath,
                                           int indexTerm) {
    TomNumberList path = appendNumber(indexTerm,rootpath);
        
    %match(TomList termList) {
      emptyTomList() -> { 
        return action;
      } 
      
      manyTomList(var@Variable[option=optionList, astType=termType],termTail) |
      manyTomList(var@UnamedVariable(optionList, termType),termTail) -> {
        Instruction subAction = genSyntacticMatchingAutomata(action,termTail,rootpath,indexTerm+1);
        Expression source = `TomTermToExpression(Variable(option(),PositionName(path),termType, concConstraint()));
        return buildLet(var, source, subAction);
      }
      
      manyTomList(currentTerm@Appl[option=optionList,nameList=nameList@(Name(tomName),_*),args=termArgs],termTail) -> {
        Instruction subAction = genSyntacticMatchingAutomata(action,termTail,rootpath,indexTerm+1);
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomTypeList termTypeList = tomSymbol.getTypesToType().getDomain();
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        
        // SUCCES
        TomTerm subjectVariableAST =  `Variable(option(),PositionName(path),termType,concConstraint());
        Instruction automataInstruction;
        if(isListOperator(tomSymbol)) {
          /*
           * store the subject into an internal variable
           * call genListMatchingAutomata with the new internal variable
           */
          int indexSubterm = 1;
          TomNumberList newPath = (TomNumberList) path.append(`ListNumber(makeNumber(indexSubterm)));
          TomTerm newSubjectVariableAST =  `VariableStar(option(),PositionName(newPath),termType,concConstraint());
          Instruction automata = genListMatchingAutomata(new MatchingParameter(
                                                           tomSymbol,path,subAction,
                                                           newSubjectVariableAST,
                                                           newSubjectVariableAST),
                                                         termArgs,indexSubterm);
          automataInstruction = `LetRef(newSubjectVariableAST,
                                        TomTermToExpression(subjectVariableAST),
                                     automata);
        } else if(isArrayOperator(tomSymbol)) {
          int indexSubterm = 1;
          TomNumberList newPathList = (TomNumberList) path.append(`ListNumber(makeNumber(indexSubterm)));
          TomNumberList newPathIndex = (TomNumberList) path.append(`IndexNumber(makeNumber(indexSubterm)));
          TomTerm newVariableListAST = `VariableStar(option(),PositionName(newPathList),termType,concConstraint());
          TomTerm newVariableIndexAST = `Variable(option(),PositionName(newPathIndex),symbolTable().getIntType(),concConstraint());
          Instruction automata = genArrayMatchingAutomata(new MatchingParameter(
                                                            tomSymbol,path,subAction,
                                                            newVariableListAST, newVariableIndexAST),
                                                          termArgs,indexSubterm
                                                          );
          Expression glZero = `TomTermToExpression(TargetLanguageToTomTerm(ITL("0")));
          automataInstruction = `Let(newVariableIndexAST,glZero,
                                     Let(newVariableListAST,
                                         TomTermToExpression(subjectVariableAST),
                                         automata));
        } else {
          int indexSubterm = 0;
          Instruction automata = genSyntacticMatchingAutomata(subAction,termArgs,path,indexSubterm+1);
          automataInstruction = `collectSubtermFromSubjectList(termArgs,termTypeList,tomSymbol,subjectVariableAST,indexSubterm,path,automata); 
        }
        
        TomTerm annotedVariable = getAnnotedVariable(optionList);
        if(annotedVariable != null) {
          automataInstruction = buildLet(annotedVariable,`TomTermToExpression(subjectVariableAST),automataInstruction);
        }
        
        Expression cond = `expandDisjunction(EqualFunctionSymbol(termType,subjectVariableAST,currentTerm));
        Instruction test = `IfThenElse(cond,automataInstruction,Nop());
        return test;
      }
      
      _ -> {
        System.out.println("GenSyntacticMatchingAutomata strange term: " + termList);
        throw new TomRuntimeException(new Throwable("GenSyntacticMatchingAutomata strange term: " + termList));
      }
    }
  }

    /*
     * function which compiles list-matching
     * 
     * p:         parameters (which are not modified during the matching process)
     * termList:  list of subterms
     * indexTerm: index of the considered subterm (indexTerm=1 for the first call)
     */
  Instruction genListMatchingAutomata(MatchingParameter p,TomList termList,int indexTerm) {
    %match(TomList termList) {
      emptyTomList() -> {
        /*
         * generate:
         * ---------
         * if(IS_EMPTY_TomList(subjectList)) {
         *   ...
         * }
         */
        Instruction test = `IfThenElse(IsEmptyList(Ref(p.subjectListName)),
                                       p.action,
                                       Nop());
        return test;
      }
        
      manyTomList(var@Variable[option=optionList, astType=termType],termTail) |
      manyTomList(var@UnamedVariable(optionList, termType),termTail) -> {
        /*
         * generate:
         * ---------
         * if(!IS_EMPTY_TomList(subjectList)) {
         *   Let TomTerm x_j = (TomTerm) GET_HEAD_TomList(subjectList);
         *   subjectList = (TomList) GET_TAIL_TomList(subjectList);
         *   ...
         * }
         */
        Instruction subAction = genListMatchingAutomata(p,termTail,indexTerm+1);
     
        Instruction body = `UnamedBlock(concInstruction(Assign(p.subjectListName,GetTail(Ref(p.subjectListName))),subAction));
        Expression source = `GetHead(termType,Ref(p.subjectListName));
        Instruction let = buildLet(var, source, body);
        Instruction test = `IfThenElse(Not(IsEmptyList(Ref(p.subjectListName))),
                                       let, Nop());
        return test;
      }
        
      manyTomList(var@VariableStar[option=optionList, astType=termType],termTail) |
      manyTomList(var@UnamedVariableStar(optionList, termType),termTail) -> {
        if(termTail.isEmpty()) {
          /*
           * generate:
           * ---------
           * Let E_n = subjectList;
           * ...
           */
          Expression source = `TomTermToExpression(Ref(p.subjectListName));
          Instruction let = buildLet(var, source, p.action);
          return  let;
        } else {
          /*
           * generate:
           * ---------
           * Let begin_i = subjectList;
           * LetRef end_i   = subjectList;
           * do {
           *   * SUBSTITUTION: E_i
           *   TomList E_i = GET_SLICE_TomList(begin_i,end_i);
           *   ...
           *   if(!IS_EMPTY_TomList(end_i) )
           *     end_i = (TomList) GET_TAIL_TomList(end_i);
           *   subjectList = end_i;
           * } while( !IS_EMPTY_TomList(subjectList) )
           */
          Instruction subAction = genListMatchingAutomata(p,termTail,indexTerm+1);
          TomNumberList pathBegin = (TomNumberList) p.path.append(`Begin(makeNumber(indexTerm)));
          TomNumberList pathEnd = (TomNumberList) p.path.append(`End(makeNumber(indexTerm)));
          TomTerm variableBeginAST = `Variable(option(),PositionName(pathBegin),termType,concConstraint());
          TomTerm variableEndAST   = `Variable(option(),PositionName(pathEnd),termType,concConstraint());

          Expression source = `GetSliceList(p.symbol.getAstName(),variableBeginAST,Ref(variableEndAST));
          Instruction let = buildLet(var, source, subAction);

          Instruction test1 = `IfThenElse(Not(IsEmptyList(Ref(variableEndAST))),
                                          Assign(variableEndAST,GetTail(Ref(variableEndAST))),
                                          Nop());
          Instruction assign = `Assign(p.subjectListName,TomTermToExpression(Ref(variableEndAST)));
          Instruction doWhile = `DoWhile(UnamedBlock(concInstruction(let,test1,assign)),
                                         Not(IsEmptyList(Ref(p.subjectListName))));
         
          Instruction letEnd = `LetRef(variableEndAST,
                                       TomTermToExpression(Ref(p.subjectListName)),
                                         doWhile);
          Instruction letBegin = `Let(variableBeginAST,
                                         TomTermToExpression(Ref(p.subjectListName)),
                                         letEnd);
          return letBegin;
        }
      }

      manyTomList(term@Appl[option=optionList,nameList=nameList@(Name(tomName),_*),args=termArgs],termTail)  -> {
        /*
         * generate:
         * ---------
         * if(!IS_EMPTY_TomList(subjectList)) {
         *   Let subjectVariableAST = (TomTerm) GET_HEAD_TomList(subjectList);
         *   subjectList = (TomList) GET_TAIL_TomList(subjectList);
         *   syntactic matching
         *   ...
         * }
         */
        Instruction subAction = genListMatchingAutomata(p,termTail,indexTerm+1);

        subAction = `genSyntacticMatchingAutomata(subAction,concTomTerm(term),p.path,indexTerm);
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        TomNumberList newPath  = appendNumber(indexTerm,p.path);
        TomTerm var =  `Variable(option(),PositionName(newPath),termType,concConstraint());

        Instruction body = `UnamedBlock(concInstruction(Assign(p.subjectListName,GetTail(Ref(p.subjectListName))),subAction));
        Expression source = `GetHead(termType,Ref(p.subjectListName));
        Instruction let = buildLet(var, source, body);
        Instruction test = `IfThenElse(Not(IsEmptyList(Ref(p.subjectListName))),
                                       let, Nop());
        return test;
      }


      
      _ -> {
        System.out.println("GenListMatchingAutomata strange termList: " + termList);
        throw new TomRuntimeException(new Throwable("GenListMatchingAutomata strange termList: " + termList));
      }
    }
  }

    /*
     * function which compiles array-matching
     * 
     * p:         parameters (which are not modified during the matching process)
     * termList:  list of subterms
     * indexTerm: index of the considered subterm (indexTerm=1 for the first call)
     */
  Instruction genArrayMatchingAutomata(MatchingParameter p,TomList termList,int indexTerm) {
    %match(TomList termList) {
      emptyTomList() -> {
        /*
         * generate:
         * ---------
         * if(IS_EMPTY_TomList(subjectList,subjectIndex)) {
         *   ...
         * }
         */
        Expression cond = `IsEmptyArray(Ref(p.subjectListName),Ref(p.subjectListIndex));
        Instruction test = `IfThenElse(cond, p.action, Nop());
        return test;
      }

      manyTomList(var@Variable[option=optionList,astType=termType],termTail) |
      manyTomList(var@UnamedVariable(optionList, termType),termTail) -> {
          /*
           * generate:
           * ---------
           * if(!IS_EMPTY_TomList(subjectList,subjectIndex)) {
           *   Let TomTerm x_j = (TomTerm) GET_ELEMENT_L(subjectList,subjectIndex);
           *   subjectIndex++;
           *     ...
           * }
           */
        Instruction subAction = genArrayMatchingAutomata(p,termTail,indexTerm+1);

        Instruction body = `UnamedBlock(concInstruction(Increment(p.subjectListIndex),subAction));
        Expression source = `GetElement(termType,p.subjectListName,p.subjectListIndex);
        Instruction let = buildLet(var, source, body);
        Instruction test = `IfThenElse(Not(IsEmptyArray(Ref(p.subjectListName),Ref(p.subjectListIndex))),
                                       let, Nop());
        return test;
      }
      
      manyTomList(var@VariableStar[option=optionList,astType=termType],termTail) |
      manyTomList(var@UnamedVariableStar(optionList, termType),termTail) -> {
        if(termTail.isEmpty()) {
            /*
             * generate:
             * ---------
             * Let E_n = GET_SLICE_L(subjectList,subjectIndex,GET_SIZE_L(subjectList));
             * ...
             */
          Expression source = `GetSliceArray(p.symbol.getAstName(),
                                             Ref(p.subjectListName),
                                             Ref(p.subjectListIndex),
                                             ExpressionToTomTerm(GetSize(p.subjectListName))
                                             );
          Instruction let = buildLet(var, source, p.action);
          return let;
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
          Instruction subAction = genArrayMatchingAutomata(p,termTail,indexTerm+1);
          TomNumberList pathBegin = (TomNumberList) p.path.append(`Begin(makeNumber(indexTerm)));
          TomNumberList pathEnd = (TomNumberList) p.path.append(`End(makeNumber(indexTerm)));
            /* TODO: termType */
          TomTerm variableBeginAST = `Variable(option(),PositionName(pathBegin),symbolTable().getIntType(),concConstraint());
          TomTerm variableEndAST   = `Variable(option(),PositionName(pathEnd),symbolTable().getIntType(),concConstraint());

          Expression source = `GetSliceArray(p.symbol.getAstName(),
                                             Ref(p.subjectListName),
                                             variableBeginAST,
                                             Ref(variableEndAST));

          Instruction let = buildLet(var, source, subAction);
          Instruction increment = `Increment(variableEndAST);
          Instruction assign = `Assign(p.subjectListIndex,TomTermToExpression(variableEndAST));
          Instruction doWhile = `DoWhile(UnamedBlock(concInstruction(let,increment,assign)),
                                         Not(IsEmptyArray(Ref(p.subjectListName), Ref(p.subjectListIndex))));
          Instruction letEnd = `LetRef(variableEndAST,
                                       TomTermToExpression(Ref(p.subjectListIndex)),
                                         doWhile);
          Instruction letBegin = `Let(variableBeginAST,
                                         TomTermToExpression(Ref(p.subjectListIndex)),
                                         letEnd);
          return letBegin;
        }
      }


      manyTomList(term@Appl[option=optionList,nameList=nameList@(Name(tomName),_*),args=termArgs],termTail)  -> {
        /*
         * generate:
         * ---------
         * if(!IS_EMPTY_TomList(subjectList,subjectIndex)) {
         *   Let var = (TomTerm) GET_ELEMENT_L(subjectList,subjectIndex);
         *   subjectIndex++;
         *   syntactic matching
         *   ...
         * }
         */
        Instruction subAction = genArrayMatchingAutomata(p,termTail,indexTerm+1);

        subAction = `genSyntacticMatchingAutomata(subAction,concTomTerm(term),p.path,indexTerm);
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        TomNumberList newPath  = appendNumber(indexTerm,p.path);
        TomTerm var =  `Variable(option(),PositionName(newPath),termType,concConstraint());

        Instruction body = `UnamedBlock(concInstruction(Increment(p.subjectListIndex),subAction));
        Expression source = `GetElement(termType,p.subjectListName,p.subjectListIndex);
        Instruction let = buildLet(var, source, body);
        Instruction test = `IfThenElse(Not(IsEmptyArray(Ref(p.subjectListName),Ref(p.subjectListIndex))),
                                       let, Nop());
        return test;
      }
        
      _ -> {
        System.out.println("GenArrayMatchingAutomata strange termList: " + termList);
        throw new TomRuntimeException(new Throwable("GenArrayMatchingAutomata strange termList: " + termList));
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
          TomType subtermType = termTypeList.getHead();
          Expression getSubtermAST;
          TomName slotName = getSlotName(tomSymbol, indexSubterm);
          if(slotName == null) {
            getSubtermAST = `GetSubterm(subtermType,subjectVariableAST,makeNumber(indexSubterm));
          } else {
            getSubtermAST = `GetSlot(subtermType,termNameAST,slotName.getString(),subjectVariableAST);
          }
          TomNumberList newPath  = appendNumber(indexSubterm+1,path);
          TomTerm newVariableAST = `Variable(option(),PositionName(newPath),subtermType,concConstraint());
          return `Let(newVariableAST,getSubtermAST,body);
        }
      }

    }
    return `Nop();
  }

  private Expression expandDisjunction(Expression exp) {
    Expression cond = `FalseTL();
    %match(Expression exp) {
      EqualFunctionSymbol(termType,exp1,Appl[option=option,nameList=nameList,args=l]) -> {
        while(!nameList.isEmpty()) {
          TomName name = nameList.getHead();
          Expression check = `EqualFunctionSymbol(termType,exp1,Appl(option,concTomName(name),l,concConstraint()));
          cond = `Or(check,cond);
          nameList = nameList.getTail();
        }
      }
    }
    return cond;
  }

	private Instruction buildLet(TomTerm dest,
                               Expression source,
                               Instruction body) {
    TomTerm annotedVariable = getAnnotedVariable(dest.getOption());
    if(annotedVariable != null) {
      body = buildLet(annotedVariable,source,body);
    }
		// Take care of constraints
		%match(TomTerm dest) {
			var@(Variable|VariableStar)[constraints=conslist] -> {
				body = buildConstraint(conslist,dest,body);
				dest = var.setConstraints(`concConstraint());
      }
		}
		return `Let(dest,source,body);
  }

	private Instruction buildConstraint(ConstraintList constr, TomTerm var, Instruction body) {
		if (constr.isEmpty()) { return body; }
		Expression cond = contraintToExpression(constr.getHead(),var);
		constr = constr.getTail();
		while (!constr.isEmpty()) {
			cond = `And(cond,contraintToExpression(constr.getHead(),var));
			constr = constr.getTail();
		}	
		body = `IfThenElse(cond,body,Nop());
		return body;
	}

private Expression contraintToExpression(Constraint constr, TomTerm var) {
	%match(Constraint constr) {
		Equal(expr) -> {
			return `EqualTerm(var,expr);
		}
	}
	return `TrueTL();
}

  private class MatchingParameter {
      /*
       * This object is used by matching-algorithms to store common parameters
       * which are not modified during the matching process
       *
       * symbol:           root symbol
       * path:             path up-to the root symbol
       * action:           list of actions to be fired when matching
       * subjectListName:  name of the internal variable supposed to store the subject
       * subjectListIndex: name of the internal variable supposed to store the index
       */
    public TomSymbol symbol;
    public TomNumberList path;
    public Instruction action;
    public TomTerm subjectListName;
    public TomTerm subjectListIndex;

    MatchingParameter(TomSymbol symbol, 
                      TomNumberList path,
                      Instruction action,
                      TomTerm subjectListName,
                      TomTerm subjectListIndex) {
      this.symbol=symbol;
      this.path=path;
      this.action=action;
      this.subjectListName=subjectListName;
      this.subjectListIndex=subjectListIndex;
    }

  }
  
} // end of class
  


    
