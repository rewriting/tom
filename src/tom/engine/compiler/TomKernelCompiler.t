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

package jtom.compiler;
  
import jtom.TomBase;
import jtom.adt.tomsignature.types.*;
import tom.library.traversal.Replace1;
import aterm.*;
import jtom.exception.TomRuntimeException;

public class TomKernelCompiler extends TomBase {
  public TomKernelCompiler() {
    super();
  }

// ------------------------------------------------------------
  %include { adt/TomSignature.tom }
// ------------------------------------------------------------
 
  private int matchNumber = 0;

  private OptionList option() {
    return ast().makeOption();
  }

  private TomName getLabel(OptionList list) {
    %match(OptionList list) {
      concOption(_*,Label(name@Name[]),_*) -> { return `name; }
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
              OptionList newOptionList = `concOption(TomTermToOption(PatternList(l2)),optionList*);
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
              while(!`l2.isEmpty()) {
                actionNumber++;
                TomTerm pa = `l2.getHead();
                patternList = pa.getTermList().getTomList();
                actionInst = pa.getAction();
                if(patternList==null || actionInst==null) {
                  System.out.println("TomKernelCompiler: null value");
                  throw new TomRuntimeException("TomKernelCompiler: null value");
                }
                  
                  /*
                   * compile nested match constructs
                   * given a list of pattern: we build a matching automaton
                   */
                actionInst = (Instruction) this.apply(actionInst);
                Instruction matchingAutomata = genSyntacticMatchingAutomata(actionInst,
                                                                            patternList,rootpath,1);
                OptionList automataOptionList = `concOption();
                TomName label = getLabel(pa.getOption());
                if(label != null) {
                  automataOptionList = `manyOptionList(Label(label),automataOptionList);
                }
                TomNumberList numberList = (TomNumberList) rootpath.append(`PatternNumber(makeNumber(actionNumber)));
                TomTerm automata = `Automata(automataOptionList,patternList,numberList,matchingAutomata);
                  //System.out.println("automata = " + automata);
                  
                automataList = append(automata,automataList);
                `l2 = `l2.getTail();
              }
                
                /*
                 * return the compiled Match construction
                 */
              InstructionList astAutomataList = automataListCompileMatchingList(automataList);
              Instruction astAutomata = collectVariableFromSubjectList(`l1,1,rootpath,`AbstractBlock(astAutomataList));
              return `CompiledMatch(astAutomata, newOptionList);
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
        body = collectVariableFromSubjectList(`tail,index+1,path,body);
        TomTerm variable = `Variable(option,PositionName(appendNumber(index,path)),variableType,concConstraint());
          // the UnamedBlock encapsulation is needed for Caml
        return `Let(variable,Cast(variableType,TomTermToExpression(subjectVar)),UnamedBlock(concInstruction(body)));
      }

      manyTomList(subjectVar@(BuildTerm|FunctionCall)(Name(tomName),_),tail) -> {
        body = collectVariableFromSubjectList(`tail,index+1,path,body);
        TomSymbol tomSymbol = symbolTable().getSymbol(`tomName);
        TomType tomType = getSymbolCodomain(tomSymbol);
        TomTerm variable = `Variable(option(),PositionName(appendNumber(index,path)),tomType, concConstraint());
        return `Let(variable,TomTermToExpression(subjectVar),body);
      }

      manyTomList(subjectVar,_) -> {
        throw new TomRuntimeException("collectVariableFromSubjectList: strange term: " + `subjectVar);
      }

    }
    return `Nop();
  }

    /*
     * build a list of instructions from a list of automata
     */
  private InstructionList automataListCompileMatchingList(TomList automataList) {
    %match(TomList automataList) {
      emptyTomList() -> { return `emptyInstructionList(); }
      manyTomList(Automata(optionList,patternList,numberList,instruction),l)  -> {
        InstructionList newList = automataListCompileMatchingList(`l);
        if(getLabel(`optionList) != null) {
            /*
             * if a label is assigned to a pattern (label:pattern -> action)
             * we generate corresponding labeled-block
             */
          `instruction = `NamedBlock(getLabel(optionList).getString(),
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
      
      manyTomList(var@Variable[option=optionList, astType=termType,constraints=constraints],termTail) |
        manyTomList(var@UnamedVariable[option=optionList,astType=termType,constraints=constraints],termTail) -> {
        Instruction subAction = genSyntacticMatchingAutomata(action,`termTail,rootpath,indexTerm+1);
        Expression source = `TomTermToExpression(Variable(option(),PositionName(path),termType, concConstraint()));
        return buildLet(`var, source, subAction);
      }
      
      manyTomList(currentTerm@Appl[option=optionList,nameList=nameList@(Name(tomName),_*),args=termArgs,constraints=constraints],termTail) -> {
        Instruction subAction = genSyntacticMatchingAutomata(action,`termTail,rootpath,indexTerm+1);
        TomSymbol tomSymbol = symbolTable().getSymbol(`tomName);
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
          boolean ensureNotEmptyList = true;
          Instruction automata = genListMatchingAutomata(new MatchingParameter(
                                                           tomSymbol,path,subAction,
                                                           newSubjectVariableAST,
                                                           newSubjectVariableAST),
                                                         `termArgs,
                                                         indexSubterm,
                                                         ensureNotEmptyList);
          automataInstruction = `LetRef(newSubjectVariableAST,
                                        TomTermToExpression(subjectVariableAST),
                                        automata);
        } else if(isArrayOperator(tomSymbol)) {
          int indexSubterm = 1;
          TomNumberList newPathList = (TomNumberList) path.append(`ListNumber(makeNumber(indexSubterm)));
          TomNumberList newPathIndex = (TomNumberList) path.append(`IndexNumber(makeNumber(indexSubterm)));
          TomTerm newVariableListAST = `VariableStar(option(),PositionName(newPathList),termType,concConstraint());
          TomTerm newVariableIndexAST = `Variable(option(),PositionName(newPathIndex),symbolTable().getIntType(),concConstraint());
          boolean ensureNotEmptyList = true;
          Instruction automata = genArrayMatchingAutomata(new MatchingParameter(
                                                            tomSymbol,path,subAction,
                                                            newVariableListAST, newVariableIndexAST),
                                                          `termArgs,
                                                          indexSubterm,
                                                          ensureNotEmptyList);
          Expression glZero = `TomTermToExpression(TargetLanguageToTomTerm(ITL("0")));
          automataInstruction = `Let(newVariableIndexAST,glZero,
                                     Let(newVariableListAST,
                                         TomTermToExpression(subjectVariableAST),
                                         automata));
        } else {
          int indexSubterm = 0;
          Instruction automata = genSyntacticMatchingAutomata(subAction,`termArgs,path,indexSubterm+1);
          automataInstruction = `collectSubtermFromSubjectList(termArgs,termTypeList,tomSymbol,subjectVariableAST,indexSubterm,path,automata); 
        }
        
          /* TODO:remove old things
             TomTerm annotedVariable = getAnnotedVariable(optionList);
             if(annotedVariable != null) {
             automataInstruction = buildLet(annotedVariable,`TomTermToExpression(subjectVariableAST),automataInstruction);
             }
          */

        automataInstruction = compileConstraint(`currentTerm,`TomTermToExpression(subjectVariableAST),automataInstruction);

        Expression cond = `expandDisjunction(EqualFunctionSymbol(termType,subjectVariableAST,currentTerm));
        Instruction test = `IfThenElse(cond,automataInstruction,Nop());
        return test;
      }
      
      _ -> {
        System.out.println("GenSyntacticMatchingAutomata strange term: " + termList);
        throw new TomRuntimeException("GenSyntacticMatchingAutomata strange term: " + termList);
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
  Instruction genListMatchingAutomata(MatchingParameter p,
                                      TomList termList,
                                      int indexTerm,
                                      boolean ensureNotEmptyList) {
    %match(TomList termList) {
      emptyTomList() -> {
          /*
           * nothing to compile
           * just check that the subject is empty
           */
        return `genIsEmptyList(p.subjectListName, p.action, Nop());
      }
        
      manyTomList(var@Variable[astType=termType],termTail) |
      manyTomList(var@UnamedVariable[astType=termType],termTail) -> {
          /*
           * get an element and store it
           */
        Instruction subAction = genListMatchingAutomata(p,`termTail,indexTerm+1,true);
        return genGetElementList(p.subjectListName, `var, `termType, subAction, ensureNotEmptyList);
      }

      manyTomList(term@Appl[nameList=nameList@(Name(tomName),_*)],termTail)  -> {
          /*
           * get an element
           * perform syntactic matching
           */
        Instruction subAction = genListMatchingAutomata(p,`termTail,indexTerm+1,true);

        subAction = `genSyntacticMatchingAutomata(subAction,concTomTerm(term),p.path,indexTerm);
        TomSymbol tomSymbol = symbolTable().getSymbol(`tomName);
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        TomNumberList newPath  = appendNumber(indexTerm,p.path);
        TomTerm var =  `Variable(option(),PositionName(newPath),termType,concConstraint());
        return genGetElementList(p.subjectListName, var, termType, subAction, ensureNotEmptyList);
      }
      
      manyTomList(var@VariableStar[astType=termType],termTail) |
      manyTomList(var@UnamedVariableStar[astType=termType],termTail) -> {
          /*
           * 3 cases:
           * - tail = emptyList
           * - tail = only VariableStar or UnamedVariableStar
           * - tail = other
           */
        if(`termTail.isEmpty()) {
            /*
             * generate:
             * ---------
             * Let E_n = subjectList;
             * ...
             */
          Expression source = `TomTermToExpression(Ref(p.subjectListName));
          return buildLet(`var, source, p.action);
        } else {
            /*
             * generate:
             * ---------
             * Let begin_i = subjectList;
             * LetRef end_i   = subjectList;
             * ...
             */
          Instruction subAction = genListMatchingAutomata(p,`termTail,indexTerm+1,false);
          TomNumberList pathBegin = (TomNumberList) p.path.append(`Begin(makeNumber(indexTerm)));
          TomNumberList pathEnd = (TomNumberList) p.path.append(`End(makeNumber(indexTerm)));
          TomTerm variableBeginAST = `Variable(option(),PositionName(pathBegin),termType,concConstraint());
          TomTerm variableEndAST   = `Variable(option(),PositionName(pathEnd),termType,concConstraint());

          Expression source = `GetSliceList(p.symbol.getAstName(),variableBeginAST,Ref(variableEndAST));
          Instruction let = buildLet(`var, source, subAction);
          Instruction tailExp = `Assign(variableEndAST,GetTail(Ref(variableEndAST)));
          Instruction loop;
          if(containOnlyVariableStar(`termTail)) {
              /*
               * do {
               *   * SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(begin_i,end_i);
               *   ...
               *   if(!IS_EMPTY_TomList(end_i) )
               *     end_i = (TomList) GET_TAIL_TomList(end_i);
               *   subjectList = end_i;
               * } while( !IS_EMPTY_TomList(end_i) )
               */
            Instruction assign1 = `genIsEmptyList(Ref(variableEndAST),Nop(),tailExp);
            Instruction assign2 = `Assign(p.subjectListName,TomTermToExpression(Ref(variableEndAST)));
            loop = `DoWhile(UnamedBlock(concInstruction(let,assign1,assign2)),Not(IsEmptyList(Ref(variableEndAST))));
          } else {
              /*
               * case (X*,y,...)
               * no longer necessary to check if the list is not empty
               * to extract the 'y'
               */
            
              /*
               * while( !IS_EMPTY_TomList(end_i) ) {
               *   subjectList = end_i;
               *   * SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(begin_i,end_i);
               *   ...
               *   end_i = (TomList) GET_TAIL_TomList(end_i);
               * } 
               */

            Instruction assign1 = tailExp;
            Instruction letAssign = `LetAssign(p.subjectListName,TomTermToExpression(Ref(variableEndAST)),UnamedBlock(concInstruction(let,assign1)));
            loop = `WhileDo(Not(IsEmptyList(Ref(variableEndAST))),letAssign);
          }
          Instruction letEnd = `LetRef(variableEndAST,
                                       TomTermToExpression(Ref(p.subjectListName)),
                                       loop);
          Instruction letBegin = `Let(variableBeginAST,
                                      TomTermToExpression(Ref(p.subjectListName)),
                                      letEnd);
          return letBegin;
        }
      }
      
      _ -> {
        System.out.println("GenListMatchingAutomata strange termList: " + termList);
        throw new TomRuntimeException("GenListMatchingAutomata strange termList: " + termList);
      }
    }
  }

  private boolean containOnlyVariableStar(TomList termList) {
    %match(TomList termList) {
      emptyTomList() -> {
        return true;
      }

      manyTomList(VariableStar[],termTail) |
      manyTomList(UnamedVariableStar[],termTail) -> {
        return containOnlyVariableStar(`termTail);
      }
    }
    return false;
  }

  
  private Instruction genIsEmptyList(TomTerm subjectListName,
                                     Instruction succes, Instruction failure) {
      /*
       * generate:
       * ---------
       * if(IS_EMPTY_TomList(subjectList)) {
       *   ...
       * }
       */
    return `IfThenElse(IsEmptyList(Ref(subjectListName)),succes,failure);
  }


  private Instruction genGetElementList(TomTerm subjectListName, TomTerm var,
                                    TomType termType,
                                    Instruction subAction, boolean notEmptyList) {
      /*
       * generate:
       * ---------
       * if(!IS_EMPTY_TomList(subjectList)) {
       *   Let TomTerm var = (TomTerm) GET_HEAD_TomList(subjectList);
       *   subjectList = (TomList) GET_TAIL_TomList(subjectList);
       *   ...
       * }
       */
    Instruction body = `LetAssign(subjectListName,GetTail(Ref(subjectListName)),subAction);
    Expression source = `GetHead(termType,Ref(subjectListName));
    Instruction let = buildLet(var, source, body);
    if(notEmptyList) {
      return `genIsEmptyList(subjectListName,Nop(),let);
    } else {
      return let;
    }
  }
  
    /*
     * function which compiles array-matching
     * 
     * p:         parameters (which are not modified during the matching process)
     * termList:  list of subterms
     * indexTerm: index of the considered subterm (indexTerm=1 for the first call)
     */
  Instruction genArrayMatchingAutomata(MatchingParameter p,
                                       TomList termList,
                                       int indexTerm,
                                       boolean ensureNotEmptyList) {
    %match(TomList termList) {
      emptyTomList() -> {
          /*
           * nothing to compile
           * just check that the subject is empty
           */
        return `genIsEmptyArray(p.subjectListName, p.subjectListIndex, p.action, Nop());
      }

      manyTomList(var@Variable[option=optionList,astType=termType],termTail) |
      manyTomList(var@UnamedVariable[option=optionList,astType=termType],termTail) -> {
          /*
           * get an element and store it
           */
        Instruction subAction = genArrayMatchingAutomata(p,`termTail,indexTerm+1,true);
        return genGetElementArray(p.subjectListName, p.subjectListIndex, `var, `termType, subAction, ensureNotEmptyList);
      }

      manyTomList(term@Appl[nameList=nameList@(Name(tomName),_*)],termTail)  -> {
          /*
           * get an element
           * perform syntactic matching
           */
        Instruction subAction = genArrayMatchingAutomata(p,`termTail,indexTerm+1,true);

        subAction = `genSyntacticMatchingAutomata(subAction,concTomTerm(term),p.path,indexTerm);
        TomSymbol tomSymbol = symbolTable().getSymbol(`tomName);
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        TomNumberList newPath  = appendNumber(indexTerm,p.path);
        TomTerm var =  `Variable(option(),PositionName(newPath),termType,concConstraint());

        return genGetElementArray(p.subjectListName, p.subjectListIndex, var, termType, subAction, ensureNotEmptyList);
      }
        
      manyTomList(var@VariableStar[option=optionList,astType=termType],termTail) |
      manyTomList(var@UnamedVariableStar[option=optionList,astType=termType],termTail) -> {
          /*
           * 3 cases:
           * - tail = emptyList
           * - tail = only VariableStar or UnamedVariableStar
           * - tail = other
           */
        if(`termTail.isEmpty()) {
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
          Instruction let = buildLet(`var, source, p.action);
          return let;
        } else {
          /*
           * generate:
           * ---------
           * int begin_i = subjectIndex;
           * int end_i   = subjectIndex;
           * ...
           */
          Instruction subAction = genArrayMatchingAutomata(p,`termTail,indexTerm+1,false);
          TomNumberList pathBegin = (TomNumberList) p.path.append(`Begin(makeNumber(indexTerm)));
          TomNumberList pathEnd = (TomNumberList) p.path.append(`End(makeNumber(indexTerm)));
          TomTerm variableBeginAST = `Variable(option(),PositionName(pathBegin),symbolTable().getIntType(),concConstraint());
          TomTerm variableEndAST   = `Variable(option(),PositionName(pathEnd),symbolTable().getIntType(),concConstraint());

          Expression source = `GetSliceArray(p.symbol.getAstName(),
                                             Ref(p.subjectListName),
                                             variableBeginAST,
                                             Ref(variableEndAST));

          Instruction let = buildLet(`var, source, subAction);
          Instruction increment = `Assign(variableEndAST,AddOne(Ref(variableEndAST)));
          Instruction loop;
          if(containOnlyVariableStar(`termTail)) {
            /*
             * do {
             *   * SUBSTITUTION: E_i
             *   TomList E_i = GET_SLICE_TomList(subjectList,begin_i,end_i);
             *   ...
             *   end_i++;
             *   subjectIndex = end_i;
             * } while( !IS_EMPTY_TomList(subjectList) )
             */
            Instruction assign = `Assign(p.subjectListIndex,TomTermToExpression(Ref(variableEndAST)));
            
            loop = `DoWhile(UnamedBlock(concInstruction(let,increment,assign)),
                            Not(IsEmptyArray(Ref(p.subjectListName), Ref(p.subjectListIndex))));
          } else {
            /*
             * while( !IS_EMPTY_TomList(end_isubjectList) ) {
             *   * SUBSTITUTION: E_i
             *   TomList E_i = GET_SLICE_TomList(subjectList,begin_i,end_i);
             *   ...
             *   end_i++;
             *   subjectIndex = end_i;
             * } 
             */
            Instruction letAssign = `LetAssign(p.subjectListIndex,TomTermToExpression(Ref(variableEndAST)),UnamedBlock(concInstruction(let,increment)));
            loop = `WhileDo(Not(IsEmptyArray(Ref(p.subjectListName), Ref(variableEndAST))),
                            letAssign);
            
          }
          Instruction letEnd = `LetRef(variableEndAST,
                                       TomTermToExpression(Ref(p.subjectListIndex)),
                                       loop);
          Instruction letBegin = `Let(variableBeginAST,
                                      TomTermToExpression(Ref(p.subjectListIndex)),
                                      letEnd);
          return letBegin;
        }
      }


      _ -> {
        System.out.println("GenArrayMatchingAutomata strange termList: " + termList);
        throw new TomRuntimeException("GenArrayMatchingAutomata strange termList: " + termList);
      }
    }
  }

  private Instruction genIsEmptyArray(TomTerm subjectListName,
                                      TomTerm subjectListIndex,
                                     Instruction succes, Instruction failure) {
      /*
       * generate:
       * ---------
       * if(IS_EMPTY_TomList(subjectList,subjectIndex)) {
       *   ...
       * }
       */
    return `IfThenElse(IsEmptyArray(Ref(subjectListName),Ref(subjectListIndex)),succes,failure);
  }


  private Instruction genGetElementArray(TomTerm subjectListName, 
                                         TomTerm subjectListIndex, 
                                         TomTerm var,
                                         TomType termType,
                                         Instruction subAction, 
                                         boolean notEmptyList) {
      /*
       * generate:
       * ---------
       * if(!IS_EMPTY_TomList(subjectList,subjectIndex)) {
       *   Let TomTerm var = (TomTerm) GET_HEAD_TomList(subjectList);
       *   subjectList = (TomList) GET_TAIL_TomList(subjectList);
       *   ...
       * }
       */
    Instruction body = `LetAssign(subjectListIndex,AddOne(Ref(subjectListIndex)),subAction);
    Expression source = `GetElement(termType,subjectListName,subjectListIndex);
    Instruction let = buildLet(var, source, body);
    if(notEmptyList) {
      return `genIsEmptyArray(subjectListName,subjectListIndex,Nop(),let);
    } else {
      return let;
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
        body = collectSubtermFromSubjectList(`tail,termTypeList.getTail(),
                                             tomSymbol,subjectVariableAST,
                                             indexSubterm+1,path,body);
        if(`subtermArg.isUnamedVariable() && !isAnnotedVariable(`subtermArg)) {
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
        while(!`nameList.isEmpty()) {
          TomName name = `nameList.getHead();
          Expression check = `EqualFunctionSymbol(termType,exp1,Appl(option,concTomName(name),l,concConstraint()));
          cond = `Or(check,cond);
          `nameList = `nameList.getTail();
        }
      }
    }
    return cond;
  }

  private Instruction buildLet(TomTerm dest,
                               Expression source,
                               Instruction body) {
      // Take care of constraints
    body = compileConstraint(dest,source,body);
    return `Let(dest,source,body);
  }

  private Instruction compileConstraint(TomTerm subject, Expression source, Instruction body) {
    %match(TomTerm subject) {
      (Variable|VariableStar)[constraints=constraints] -> {
        return buildConstraint(`constraints,`TomTermToExpression(subject),body);
      }

      (UnamedVariable|UnamedVariableStar)[constraints=constraints] -> {
        return buildConstraint(`constraints,source,body);
      }

      Appl[constraints=constraints] -> {
        return buildConstraint(`constraints,source,body);
      }

      _ -> {
        throw new TomRuntimeException("compileConstraint: strange subject: " + subject);
      }
    }
  }

  private Instruction buildConstraint(ConstraintList constraints, Expression source, Instruction body) {
    %match(ConstraintList constraints) {
      concConstraint() -> {
        return body;
      }

      concConstraint(Equal(var) ,tail*) -> {
          //System.out.println("constraint: " + source + " EqualTo " + var);
        Instruction subBody = compileConstraint(`var,source,body);
        return `buildConstraint(tail,source,IfThenElse(EqualTerm(getTermType(var),var,ExpressionToTomTerm(source)),subBody,Nop()));
      }

      concConstraint(AssignTo(var@(Variable|VariableStar)[]) ,tail*) -> {
          //System.out.println("constraint: " + source + " AssignTo " + var);
        Instruction subBody = compileConstraint(`var,source,body);
        return `buildConstraint(tail,source,Let(var,source,subBody));
      }

      concConstraint(head,tail*) -> {
        throw new TomRuntimeException("buildConstraint: unknown constraint: " + `head);
      }
    }
    throw new TomRuntimeException("buildConstraint: unknown constraints: " + constraints);
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
