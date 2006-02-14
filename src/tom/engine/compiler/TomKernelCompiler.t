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

package tom.engine.compiler;

import tom.engine.TomBase;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.SymbolTable;
import tom.library.traversal.Replace1;
import aterm.ATerm;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TomKernelCompiler extends TomBase {

  private SymbolTable symbolTable;
  
  public TomKernelCompiler(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  private SymbolTable getSymbolTable() {
    return symbolTable;
  }

// ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
// ------------------------------------------------------------
 
  private int matchNumber = 0;

  private OptionList option() {
    return getAstFactory().makeOption();
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
            Match(SubjectList(l1),patternInstructionList, optionList)  -> {
              TomNumberList rootpath = tsf().makeTomNumberList();
              matchNumber++;
              rootpath = (TomNumberList) rootpath.append(`MatchNumber(makeNumber(matchNumber)));
                
                /*
                 * for each pattern action (<term>,...,<term> -> <action>)
                 * build a matching automata
                 */
              TomList automataList = empty();
              int actionNumber = 0;
              while(!`patternInstructionList.isEmpty()) {
                actionNumber++;
                PatternInstruction pa = `patternInstructionList.getHead();
                SlotList patternList = tomListToSlotList(pa.getPattern().getTomList());
                Instruction actionInst = pa.getAction();
                if(patternList==null || actionInst==null) {
                  System.out.println("TomKernelCompiler: null value");
                  throw new TomRuntimeException("TomKernelCompiler: null value");
                }
                  
                  /*
                   * compile nested match constructs
                   * given a list of pattern: we build a matching automaton
                   */
                actionInst = (Instruction) this.apply(actionInst);
                Instruction matchingAutomata = genSyntacticMatchingAutomata(actionInst,patternList,rootpath);
                OptionList automataOptionList = `concOption();
                TomName label = getLabel(pa.getOption());
                if(label != null) {
                  automataOptionList = `manyOptionList(Label(label),automataOptionList);
                }
                TomNumberList numberList = (TomNumberList) rootpath.append(`PatternNumber(makeNumber(actionNumber)));
                TomTerm automata = `Automata(automataOptionList,slotListToTomList(patternList),numberList,matchingAutomata);
                  //System.out.println("automata = " + automata);
                  
                automataList = append(automata,automataList);
                `patternInstructionList = `patternInstructionList.getTail();
              }
                
                /*
                 * return the compiled Match construction
                 */
              InstructionList astAutomataList = automataListCompileMatchingList(automataList);
              SlotList slots = tomListToSlotList(`l1);
              Instruction astAutomata = `collectVariableFromSubjectList(slots,rootpath,AbstractBlock(astAutomataList));
              return `CompiledMatch(astAutomata, optionList);
            }
          } // end match
        } 
				return traversal().genericTraversal(subject,this);
      } // end apply
    }; // end new

  public TomTerm compileMatching(TomTerm subject) {
    return (TomTerm) replace_compileMatching.apply(subject);
  }

    /*
     * collect match variables (from match(t1,...,tn))
     * create a list of declaration/assignement: v1=t1 ... vn=tn in body
     * generate a check_stamp
     */
  private Instruction collectVariableFromSubjectList(SlotList subjectList, TomNumberList path, Instruction body) {
    %match(SlotList subjectList) { 
      emptySlotList() -> { return body; }
      manySlotList(PairSlotAppl(slotName,subjectVar@Variable[option=option,astType=variableType]),tail) -> {
        body = collectVariableFromSubjectList(`tail,path,body);
        TomTerm variable = `Variable(option,PositionName(path.append(NameNumber(slotName))),variableType,concConstraint());
        Expression source = `Cast(variableType,TomTermToExpression(subjectVar));
          // the UnamedBlock encapsulation is needed for Caml
				Instruction let = `Let(variable,source,AbstractBlock(concatInstruction(CheckStamp(variable),body)));
				// If is the variable has the correct type
        return `CheckInstance(variableType,TomTermToExpression(subjectVar),let);
      }

      manySlotList(PairSlotAppl(slotName,subjectVar@(BuildTerm|FunctionCall)(Name(tomName),_)),tail) -> {
        body = collectVariableFromSubjectList(`tail,path,body);

        TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(`tomName);
        TomType tomType = getSymbolCodomain(tomSymbol);
        TomTerm variable = `Variable(option(),PositionName(path.append(NameNumber(slotName))),tomType, concConstraint());
        Expression source = `TomTermToExpression(subjectVar);
        Instruction checkStamp = `CheckStamp(variable);
        return `Let(variable,source,AbstractBlock(concatInstruction(checkStamp,body)));
      }
    }
    throw new TomRuntimeException("collectVariableFromSubjectList: strange term: " + `subjectList);
  }

  private InstructionList concatInstruction(Instruction i1, Instruction i2) {
    %match(Instruction i1, Instruction i2) {
      AbstractBlock(l1), AbstractBlock(l2) -> { return `concInstruction(l1*,l2*); }
      AbstractBlock(l1), y -> { return `concInstruction(l1*,y); }
      x, AbstractBlock(l2) -> { return `concInstruction(x,l2*); }
    }
      return `concInstruction(i1,i2);
  }

    /*
     * build a list of instructions from a list of automata
     */
  private InstructionList automataListCompileMatchingList(TomList automataList) {
    %match(TomList automataList) {
      emptyTomList() -> { return `emptyInstructionList(); }
      manyTomList(Automata(optionList,patternList,_,instruction),l)  -> {
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
     * given a pattern, this function generates the discrimitation test on the root symbol
     * and recursively calls the algorithm on subterms
     */
  Instruction genSyntacticMatchingAutomata(Instruction action,
                                           SlotList termList,
                                           TomNumberList rootpath) {
    %match(SlotList termList) {
      emptySlotList() -> { 
        return action;
      } 
      // X or _,...  
      manySlotList(PairSlotAppl(slotName,
                                var@(Variable|UnamedVariable)[astType=termType]),termTail) -> {
        Instruction subAction = genSyntacticMatchingAutomata(action,`termTail,rootpath);
        TomNumberList path  = (TomNumberList) rootpath.append(`NameNumber(slotName));

        Expression source = `TomTermToExpression(Variable(option(),PositionName(path),termType, concConstraint()));
        return buildLet(`var, source, subAction);
      }
     
      // (f|g)[...]
      manySlotList(PairSlotAppl(slotName,
                   currentTerm@RecordAppl[nameList=nameList@(Name(tomName),_*),
                                          slots=termArgs]),termTail) -> {
        // recursively call the algorithm on termTail
        Instruction subAction = genSyntacticMatchingAutomata(action,`termTail,rootpath);
        // find the codomain of (f|g) [* should be the same *]
        TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(`tomName);
        TomType codomain = tomSymbol.getTypesToType().getCodomain();
        
          // perform the compilation, according to 3 cases:
          // - (f|g) is a list operator
          // - (f|g) is an array operator
          // - (f|g) is a syntactic operator
        TomNumberList path  = (TomNumberList) rootpath.append(`NameNumber(slotName));
        TomTerm subjectVariableAST =  `Variable(option(),PositionName(path),codomain,concConstraint());
        Instruction automataInstruction;
        if(isListOperator(tomSymbol)) {
          // case: list operator
            /*
             * store the subject into an internal variable
             * call genListMatchingAutomata with the new internal variable
             */
          int indexSubterm = 1;
          TomNumberList newPath = (TomNumberList) path.append(`ListNumber(makeNumber(indexSubterm)));
          TomTerm newSubjectVariableAST =  `VariableStar(option(),PositionName(newPath),codomain,concConstraint());
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
          // case: array operator
          int indexSubterm = 1;
          TomNumberList newPathList = (TomNumberList) path.append(`ListNumber(makeNumber(indexSubterm)));
          TomNumberList newPathIndex = (TomNumberList) path.append(`IndexNumber(makeNumber(indexSubterm)));
          TomTerm newVariableListAST = `VariableStar(option(),PositionName(newPathList),codomain,concConstraint());
          TomTerm newVariableIndexAST = `Variable(option(),PositionName(newPathIndex),getSymbolTable().getIntType(),concConstraint());
          boolean ensureNotEmptyList = true;
          Instruction automata = genArrayMatchingAutomata(new MatchingParameter(
                                                            tomSymbol,path,subAction,
                                                            newVariableListAST, newVariableIndexAST),
                                                          `termArgs,
                                                          indexSubterm,
                                                          ensureNotEmptyList);
          Expression glZero = `TomTermToExpression(TargetLanguageToTomTerm(ITL("0")));
          automataInstruction = `LetRef(newVariableIndexAST,glZero,
                                     Let(newVariableListAST,
                                         TomTermToExpression(subjectVariableAST),
                                         automata));
        } else {
          // case: syntactic operator
          Instruction automata = genSyntacticMatchingAutomata(subAction,`termArgs,path);
          TomTypeList termTypeList = tomSymbol.getTypesToType().getDomain();
          if(`nameList.getLength()==1 || `termArgs.isEmpty()) {
              automataInstruction = `collectSubtermFromTomSymbol(termArgs,tomSymbol,subjectVariableAST,path,automata); 
          } else { 
            // generate is_fsym(t,f) || is_fsym(t,g)
            automataInstruction = `collectSubtermFromSubjectList(currentTerm,subjectVariableAST,path,automata); 
            automataInstruction = compileConstraint(`currentTerm,`TomTermToExpression(subjectVariableAST),automataInstruction);
            return automataInstruction;
          }
        }
        // generate is_fsym(t,f) || is_fsym(t,g)
        Expression cond = `expandDisjunction(EqualFunctionSymbol(codomain,subjectVariableAST,currentTerm));
        automataInstruction = compileConstraint(`currentTerm,`TomTermToExpression(subjectVariableAST),automataInstruction);
        return `If(cond,automataInstruction,Nop());
      }
    }
		System.out.println("GenSyntacticMatchingAutomata strange term: " + termList);
		throw new TomRuntimeException("GenSyntacticMatchingAutomata strange term: " + termList);
  }

    /*
     * function which compiles list-matching
     * 
     * p:         parameters (which are not modified during the matching process)
     * termList:  list of subterms
     * indexTerm: index of the considered subterm (indexTerm=1 for the first call)
     */
  Instruction genListMatchingAutomata(MatchingParameter p,
                                      SlotList termList,
                                      int indexTerm,
                                      boolean ensureNotEmptyList) {

    %match(SlotList termList) {
      emptySlotList() -> {
          /*
           * nothing to compile
           * just check that the subject is empty
           */
        return `genCheckEmptyList(p.symbol, p.subjectListName, p.action, Nop());
      }
        
      manySlotList(PairSlotAppl[appl=var@(Variable|UnamedVariable)[astType=termType]],termTail) -> {
          /*
           * get an element and store it
           */
        Instruction subAction = genListMatchingAutomata(p,`termTail,indexTerm+1,true);
        return genGetElementList(p.symbol, p.subjectListName, `var, `termType, subAction, ensureNotEmptyList);
      }

      manySlotList(PairSlotAppl[appl=term@RecordAppl[nameList=(Name(tomName),_*)]],termTail)  -> {
          /*
           * get an element
           * perform syntactic matching
           */
        Instruction subAction = genListMatchingAutomata(p,`termTail,indexTerm+1,true);

        TomName slotName = `PositionName(concTomNumber(Number(indexTerm)));
        subAction = `genSyntacticMatchingAutomata(subAction,concSlot(PairSlotAppl(slotName,term)),p.path);
        TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(`tomName);
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        TomNumberList newPath  = appendNumber(indexTerm,p.path);
        TomTerm var =  `Variable(option(),PositionName(newPath),termType,concConstraint());
        return genGetElementList(p.symbol, p.subjectListName, var, termType, subAction, ensureNotEmptyList);
      }
      
      manySlotList(PairSlotAppl[appl=var@(VariableStar|UnamedVariableStar)[astType=termType]],termTail) -> {
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
          Instruction tailExp = `Assign(variableEndAST,genGetTail(p.symbol,Ref(variableEndAST)));
          Instruction loop;
          if(containOnlyVariableStar(`termTail)) {
              /*
               * do {
               *   * SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(begin_i,end_i);
               *   ...
               *   if(!IS_EMPTY_TomList(end_i) )
               *     end_i = (TomList) GET_TAIL_TomList(end_i);
               *   else *** use this impossible value to indicate the end of the loop ***
               *     end_i = begin_i
               *   subjectList = end_i;
               * } while( end_i != begin_i )  
               * *** subjectList is reseted to begin_i when the loop stops
               */
            Instruction stopIter = `Assign(variableEndAST,TomTermToExpression(variableBeginAST));
            Instruction assign1 = `genCheckEmptyList(p.symbol, Ref(variableEndAST),stopIter,tailExp);
            Instruction assign2 = `Assign(p.subjectListName,TomTermToExpression(Ref(variableEndAST)));
            loop = `DoWhile(UnamedBlock(concInstruction(let,assign1,assign2)),Negation(EqualTerm(termType,Ref(variableEndAST),variableBeginAST)));
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
               * subjectList = begin_i 
               */

            Instruction assign1 = tailExp;
            Instruction letAssign = `LetAssign(p.subjectListName,TomTermToExpression(Ref(variableEndAST)),UnamedBlock(concInstruction(let,assign1)));
            loop = `WhileDo(Negation(genIsEmptyList(p.symbol,Ref(variableEndAST))),letAssign);
            loop = `UnamedBlock(concInstruction(loop,LetAssign(p.subjectListName,TomTermToExpression(variableBeginAST),Nop())));
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
    }
		System.out.println("GenListMatchingAutomata strange termList: " + termList);
		throw new TomRuntimeException("GenListMatchingAutomata strange termList: " + termList);
  }

  private boolean containOnlyVariableStar(SlotList termList) {
    %match(SlotList termList) {
      emptySlotList() -> {
        return true;
      }

      manySlotList(PairSlotAppl[appl=(VariableStar|UnamedVariableStar)[]],termTail) -> {
        return containOnlyVariableStar(`termTail);
      }
    }
    return false;
  }

  
  private Instruction genCheckEmptyList(TomSymbol tomSymbol, TomTerm subjectListName,
                                        Instruction succes, Instruction failure) {
      /*
       * generate:
       * ---------
       * if(IS_EMPTY_TomList(subjectList)) {
       *   ...
       * }
       */
    return `If(genIsEmptyList(tomSymbol, Ref(subjectListName)),succes,failure);
  }


  private Instruction genGetElementList(TomSymbol tomSymbol, TomTerm subjectListName, TomTerm var,
                                        TomType termType, Instruction subAction, boolean notEmptyList) {
      /*
       * generate:
       * ---------
       * if(!IS_EMPTY_TomList(subjectList)) {
       *   Let TomTerm var = (TomTerm) GET_HEAD_TomList(subjectList);
       *   subjectList = (TomList) GET_TAIL_TomList(subjectList);
       *   ...
       * }
       */
    Instruction body = `LetAssign(subjectListName,genGetTail(tomSymbol,Ref(subjectListName)),subAction);
    Expression source = genGetHead(tomSymbol,termType,`Ref(subjectListName));
    Instruction let = buildLet(var, source, body);
    if(notEmptyList) {
      return `genCheckEmptyList(tomSymbol, subjectListName,Nop(),let);
    } else {
      return let;
    }
  }
  
  private Expression genGetHead(TomSymbol tomSymbol, TomType type, TomTerm var) {
    TomName opNameAST = tomSymbol.getAstName();
    return `GetHead(opNameAST, type, var);
  }

  private Expression genGetTail(TomSymbol tomSymbol, TomTerm var) {
    TomName opNameAST = tomSymbol.getAstName();
    return `GetTail(opNameAST, var);
  }

  private Expression genIsEmptyList(TomSymbol tomSymbol, TomTerm var) {
    TomName opNameAST = tomSymbol.getAstName();
    return `IsEmptyList(opNameAST, var);
  }

    /*
     * function which compiles array-matching
     * 
     * p:         parameters (which are not modified during the matching process)
     * termList:  list of subterms
     * indexTerm: index of the considered subterm (indexTerm=1 for the first call)
     */
  Instruction genArrayMatchingAutomata(MatchingParameter p,
                                       SlotList termList,
                                       int indexTerm,
                                       boolean ensureNotEmptyList) {
    %match(SlotList termList) {
      emptySlotList() -> {
          /*
           * nothing to compile
           * just check that the subject is empty
           */
        return `genIsEmptyArray(p.symbol,p.subjectListName, p.subjectListIndex, p.action, Nop());
      }

      manySlotList(PairSlotAppl[appl=var@(Variable|UnamedVariable)[astType=termType]],termTail) -> {
          /*
           * get an element and store it
           */
        Instruction subAction = genArrayMatchingAutomata(p,`termTail,indexTerm+1,true);
        return genGetElementArray(p.symbol,p.subjectListName, p.subjectListIndex, `var, `termType, subAction, ensureNotEmptyList);
      }

      manySlotList(PairSlotAppl[appl=term@RecordAppl[nameList=(Name(tomName),_*)]],termTail)  -> {
          /*
           * get an element
           * perform syntactic matching
           */
        Instruction subAction = genArrayMatchingAutomata(p,`termTail,indexTerm+1,true);

        TomName slotName = `PositionName(concTomNumber(Number(indexTerm)));
        subAction = `genSyntacticMatchingAutomata(subAction,concSlot(PairSlotAppl(slotName,term)),p.path);
        TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(`tomName);
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        TomNumberList newPath  = appendNumber(indexTerm,p.path);
        TomTerm var =  `Variable(option(),PositionName(newPath),termType,concConstraint());

        return genGetElementArray(p.symbol,p.subjectListName, p.subjectListIndex, var, termType, subAction, ensureNotEmptyList);
      }
        
      manySlotList(PairSlotAppl[appl=var@(VariableStar|UnamedVariableStar)[]],termTail) -> {
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
                                             ExpressionToTomTerm(GetSize(p.symbol.getAstName(),p.subjectListName))
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
          TomTerm variableBeginAST = `Variable(option(),PositionName(pathBegin),getSymbolTable().getIntType(),concConstraint());
          TomTerm variableEndAST   = `Variable(option(),PositionName(pathEnd),getSymbolTable().getIntType(),concConstraint());

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
             * } while( subjectIndex <= GET_SIZE(subjectList) )
             * subjectIndex = begin_i
             *
             * *** we need <= instead of < to make the algorithm complete ***
             */
            Instruction assign = `Assign(p.subjectListIndex,TomTermToExpression(Ref(variableEndAST)));
            
            loop = `DoWhile(UnamedBlock(concInstruction(let,increment,assign)),
                            Negation(GreaterThan(TomTermToExpression(Ref(p.subjectListIndex)),GetSize(p.symbol.getAstName(),Ref(p.subjectListName)))));
            loop = `UnamedBlock(concInstruction(loop,LetAssign(p.subjectListIndex,TomTermToExpression(variableBeginAST),Nop())));
          } else {
            /*
             * while( !IS_EMPTY_TomList(end_i,subjectList) ) {
             *   subjectIndex = end_i;
             *   * SUBSTITUTION: E_i
             *   TomList E_i = GET_SLICE_TomList(subjectList,begin_i,end_i);
             *   ...
             *   end_i++;
             * } 
             * subjectIndex = begin_i
             */
            Instruction letAssign = `LetAssign(p.subjectListIndex,TomTermToExpression(Ref(variableEndAST)),UnamedBlock(concInstruction(let,increment)));
            loop = `WhileDo(Negation(IsEmptyArray(p.symbol.getAstName(),Ref(p.subjectListName), Ref(variableEndAST))),
                            letAssign);
            loop = `UnamedBlock(concInstruction(loop,LetAssign(p.subjectListIndex,TomTermToExpression(variableBeginAST),Nop())));
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
    }
		System.out.println("GenArrayMatchingAutomata strange termList: " + termList);
		throw new TomRuntimeException("GenArrayMatchingAutomata strange termList: " + termList);
  }

  private Instruction genIsEmptyArray(TomSymbol tomSymbol,
                                      TomTerm subjectListName,
                                      TomTerm subjectListIndex,
                                     Instruction succes, Instruction failure) {
      /*
       * generate:
       * ---------
       * if(IS_EMPTY_TomList(subjectList,subjectIndex)) {
       *   ...
       * }
       */
    TomName opNameAST = tomSymbol.getAstName();
    
    return `If(IsEmptyArray(opNameAST,Ref(subjectListName),Ref(subjectListIndex)),succes,failure);
  }


  private Instruction genGetElementArray(TomSymbol tomSymbol,
                                         TomTerm subjectListName, 
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
    Expression source = `GetElement(tomSymbol.getAstName(),termType,subjectListName,Ref(subjectListIndex));
    Instruction let = buildLet(var, source, body);
    if(notEmptyList) {
      return `genIsEmptyArray(tomSymbol,subjectListName,subjectListIndex,Nop(),let);
    } else {
      return let;
    }
  }


    /*
     * given a list of slots [s1=t1],...,[sn=tn]
     * declare/assign internal matching variables: match_path_i = ti
     */
  private Instruction collectSubtermFromSubjectList(TomTerm currentTerm,
                                                    TomTerm subjectVariableAST, 
                                                    TomNumberList path, Instruction body) {
    %match(TomTerm currentTerm) {
      RecordAppl[nameList=nameList@(Name(tomName),_*), slots=termArgList] -> {
        TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(`tomName);

        // check that variables are no longer Bottom 
        TomType booleanType = getSymbolTable().getBooleanType();
        TomTerm booleanVariable = `Variable(option(),PositionName(manyTomNumberList(NameNumber(Name("bool")),path)),booleanType,concConstraint());
        Instruction ifBody = `collectSubtermIf(nameList,booleanVariable,currentTerm,termArgList,subjectVariableAST,path);
        Instruction checkBody = `If(TomTermToExpression(booleanVariable),body,Nop()); 
        Instruction newBody = `collectSubtermLetRefBottom(termArgList,tomSymbol,path,AbstractBlock(concatInstruction(ifBody,checkBody)));

        return `LetRef(booleanVariable,FalseTL(),newBody);

      }
    }
    return body;
  }

    /*
     * given a list of symbol names
     * generated nested if, for each symbol name
     */
  private Instruction collectSubtermIf(NameList nameList,
                                       TomTerm booleanVariable,
                                       TomTerm currentTerm,
                                       SlotList termArgList,
                                       TomTerm subjectVariableAST, 
                                       TomNumberList path) {
    %match(NameList nameList) {  
      emptyNameList() -> {
        return `Nop();
      }

      manyNameList(name@Name(tomName),tail) -> {
        TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(`tomName);
        TomType codomain = tomSymbol.getTypesToType().getCodomain();
        Instruction elseBody = `collectSubtermIf(tail,booleanVariable,currentTerm,termArgList,subjectVariableAST,path);
        Instruction assign = `collectSubtermLetAssign(termArgList,tomSymbol,subjectVariableAST,path,Nop());
        Expression cond = `EqualFunctionSymbol(codomain,subjectVariableAST,currentTerm.setNameList(concTomName(name)));
        return  `If(cond,LetAssign(booleanVariable,TrueTL(),assign),elseBody);
      }
    }
    return `Nop();
  }

   /*
    * given a list of slots
    * generate assignements for each subterm
    */
  private Instruction collectSubtermLetAssign(SlotList termArgList,
                                              TomSymbol tomSymbol,
                                              TomTerm subjectVariableAST, 
                                              TomNumberList path, Instruction body) {
    TomName opNameAST = tomSymbol.getAstName();
    %match(SlotList termArgList) { 
      emptySlotList() -> { return body; }
     
      manySlotList(PairSlotAppl(slotName,_),tail) -> {
        body = collectSubtermLetAssign(`tail,tomSymbol,subjectVariableAST,path,body);
        TomType subtermType = getSlotType(tomSymbol,`slotName);

        if(!isDefinedGetSlot(tomSymbol,`slotName)) {
          Logger.getLogger(getClass().getName()).log( Level.SEVERE,
              "ErrorMissingSlotDecl",
              new Object[]{tomSymbol.getAstName().getString(),`slotName.getString()});
        }

        Expression getSlotAST = `GetSlot(subtermType,opNameAST,slotName.getString(),subjectVariableAST);
        TomNumberList newPath  = (TomNumberList) path.append(`NameNumber(slotName));
        TomTerm newVariableAST = `Variable(option(),PositionName(newPath),subtermType,concConstraint());
        return `LetAssign(newVariableAST,getSlotAST,body);
      }
    }
    return `Nop();
  }
 
  /*
   * given a list of slot
   * declare/initialize each slot to "bottom"
   */
  private Instruction collectSubtermLetRefBottom(SlotList termArgList,
                                                 TomSymbol tomSymbol,
                                                 TomNumberList path, Instruction body) {
    %match(SlotList termArgList) { 
      emptySlotList() -> {
        return body;
      }
     
      manySlotList(PairSlotAppl(slotName,_),tail) -> {
        body = collectSubtermLetRefBottom(`tail,tomSymbol,path,body);
        TomType subtermType = getSlotType(tomSymbol,`slotName);
        TomNumberList newPath  = (TomNumberList) path.append(`NameNumber(slotName));
        TomTerm newVariableAST = `Variable(option(),PositionName(newPath),subtermType,concConstraint());
        return `LetRef(newVariableAST,Bottom(),body);
      }
    }
    return `Nop();
  }

    /*
     * given a list of subject t1,...,tn
     * declare/assign internal matching variables: match_path_i = ti
     */
  private Instruction collectSubtermFromTomSymbol(SlotList termArgList, TomSymbol tomSymbol,TomTerm subjectVariableAST, 
                                                    TomNumberList path, Instruction body) {
    TomName opNameAST = tomSymbol.getAstName();
    %match(SlotList termArgList) { 
      emptySlotList() -> { return body; }
      
      manySlotList(PairSlotAppl(slotName,_),tail) -> {
        body = collectSubtermFromTomSymbol(`tail,tomSymbol,subjectVariableAST,path,body);
        TomType subtermType = getSlotType(tomSymbol,`slotName);
        if(!isDefinedGetSlot(tomSymbol,`slotName)) {
          Logger.getLogger(getClass().getName()).log( Level.SEVERE,
              "ErrorMissingSlotDecl",
              new Object[]{tomSymbol.getAstName().getString(),`slotName.getString()});
        }

        Expression getSubtermAST = `GetSlot(subtermType,opNameAST,slotName.getString(),subjectVariableAST);
        TomNumberList newPath  = (TomNumberList) path.append(`NameNumber(slotName));
        TomTerm newVariableAST = `Variable(option(),PositionName(newPath),subtermType,concConstraint());
        return `Let(newVariableAST,getSubtermAST,body);
      }
    }
    return `Nop();
  }
  private Expression expandDisjunction(Expression exp) {
    Expression cond = `FalseTL();
    %match(Expression exp) {
      EqualFunctionSymbol(termType,exp1,RecordAppl[option=option,nameList=nameList,slots=l]) -> {
        while(!`nameList.isEmpty()) {
          TomName name = `nameList.getHead();
          Expression check = `EqualFunctionSymbol(termType,exp1,RecordAppl(option,concTomName(name),l,concConstraint()));
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
    //return `Let(dest,source,body);
    /*
     * this optimisation is not good since it avoids some optimisations
     * in particular, f(x,y) and f(x,_) cannot be merged
     *
     */
    if(dest.isUnamedVariable() || dest.isUnamedVariableStar()) {
      // This is an optimisation 
      // Do not assign an UnamedVariable or an UnamedVariableStar
      return body;
    } else {
      return `Let(dest,source,body);
    }
    
  }

  private Instruction compileConstraint(TomTerm subject, Expression source, Instruction body) {
    %match(TomTerm subject) {
      (Variable|VariableStar)[constraints=constraints] -> {
        return buildConstraint(`constraints,`TomTermToExpression(subject),body);
      }

      (UnamedVariable|UnamedVariableStar)[constraints=constraints] -> {
        return buildConstraint(`constraints,source,body);
      }

      RecordAppl[constraints=constraints] -> {
        return buildConstraint(`constraints,source,body);
      }
    }
		throw new TomRuntimeException("compileConstraint: strange subject: " + subject);
  }

  private Instruction buildConstraint(ConstraintList constraints, Expression source, Instruction body) {
    %match(ConstraintList constraints) {
      concConstraint() -> {
        return body;
      }

      concConstraint(Equal(var) ,tail*) -> {
        //System.out.println("constraint: " + source + " EqualTo " + `var);
        Instruction subBody = compileConstraint(`var,source,body);
        return `buildConstraint(tail,source,If(EqualTerm(getTermType(var, getSymbolTable()),var,ExpressionToTomTerm(source)),subBody,Nop()));
      }

      concConstraint(AssignTo(var@(Variable|VariableStar)[]) ,tail*) -> {
        //System.out.println("constraint: " + source + " AssignTo " + `var);
        Instruction subBody = compileConstraint(`var,source,body);
        return `buildConstraint(tail,source,Let(var,source,subBody));
      }

      concConstraint(Ensure(exp) ,tail*) -> {
        //System.out.println("constraint: " + source + " Ensure " + `exp);
        //Instruction subBody = compileConstraint(`exp,source,body);
        TomType type = getSymbolTable().getBooleanType();
        Expression equality = `EqualTerm(type,ExpressionToTomTerm(TrueTL()),exp);
        Instruction generatedTest = `If(equality,body,Nop());
        return `buildConstraint(tail,source,generatedTest);
      }

      concConstraint(head,_*) -> {
        throw new TomRuntimeException("buildConstraint: unknown constraint: " + `head);
      }
    }
    throw new TomRuntimeException("buildConstraint: unknown constraints: " + constraints);
  }


  static private class MatchingParameter {
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
