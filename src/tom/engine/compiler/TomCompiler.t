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
  
import java.util.*;

import jtom.adt.tomsignature.types.*;

import jtom.runtime.Replace1;
import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import jtom.tools.Tools;
import jtom.checker.TomCheckerMessage;
import aterm.*;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public class TomCompiler extends TomTask {
  TomKernelCompiler tomKernelCompiler;
  private String debugKey = null;
  private boolean debugMode = false, eCode = false;
  private int absVarNumber = 0;
  
  public TomCompiler(TomEnvironment environment,
                     TomKernelCompiler tomKernelCompiler) {
    super("Tom Compiler", environment);
    this.tomKernelCompiler = tomKernelCompiler;
  }
  
// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------

  public void initProcess() {
    debugMode = getInput().isDebugMode();
    eCode = getInput().isECode();
  }
	
  public void process() {
    try {
      long startChrono = 0;
      boolean verbose = getInput().isVerbose(), intermediate = getInput().isIntermediate();
      if(verbose) { startChrono = System.currentTimeMillis();}
      
      TomTerm preCompiledTerm = preProcessing(getInput().getTerm());
        //System.out.println("preCompiledTerm = \n" + preCompiledTerm);
      TomTerm compiledTerm = tomKernelCompiler.compileMatching(preCompiledTerm);
      
      if(verbose) {
        System.out.println("TOM compilation phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
      }
      if(intermediate) {
        Tools.generateOutput(getInput().getBaseInputFileName() + TomTaskInput.compiledSuffix, compiledTerm);
      }
      getInput().setTerm(compiledTerm);
      
    } catch (Exception e) {
      addError("Exception occurs in TomCompiler: "+e.getMessage(), getInput().getInputFileName(), TomCheckerMessage.DEFAULT_ERROR_LINE_NUMBER, TomCheckerMessage.TOM_ERROR);
      e.printStackTrace();
      return;
    }
  }

  private OptionList option() {
    return ast().makeOption();
  }

    /* 
     * preProcessing:
     * replaces MakeTerm by BuildList, BuildArray or BuildTerm
     *
     * transforms RuleSet into Function + Match + MakeTerm
     * abstract list-matching patterns
     * rename non-linear patterns
     */

  Replace1 replace_preProcessing = new Replace1() {
      public ATerm apply(ATerm subject) {
        if(subject instanceof TomTerm) {
          %match(TomTerm subject) {
            MakeTerm(var@(Variable|VariableStar)[]) -> {
              return `var;
            }    

            MakeTerm(Appl[nameList=(name@Name(tomName)),args=termArgs]) -> {
              TomSymbol tomSymbol = symbolTable().getSymbol(`tomName);
              TomList newTermArgs = (TomList) traversal().genericTraversal(`termArgs,replace_preProcessing_makeTerm);
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

          } // end match
        } else if(subject instanceof Instruction) {
          %match(Instruction subject) {
            Match(SubjectList(l1),PatternList(l2), matchOptionList)  -> {
              TomList patternList = `l2;
              Option orgTrack = findOriginTracking(`matchOptionList);
              if(debugMode) {
                debugKey = orgTrack.getFileName().getString() + orgTrack.getLine();
              }
              TomList newPatternList = empty();
              while(!patternList.isEmpty()) {
                  /*
                   * the call to preProcessing performs the recursive expansion
                   * of nested match constructs
                   */
                TomTerm elt = preProcessing(patternList.getHead());
                TomTerm newPatternAction = elt;
              
                matchBlock: {
                  %match(TomTerm elt) {
                    PatternAction(TermList(termList),actionInst, option) -> {
                      TomList newTermList = empty();
                      Instruction newActionInst = actionInst;
                        /* generate equality checks */
                      ArrayList equalityCheck = new ArrayList();
                      TomList renamedTermList = linearizePattern(`termList,equalityCheck);
											newPatternAction = `PatternAction(TermList(renamedTermList),actionInst, option);        
                    
                        /* abstract patterns */
                      ArrayList abstractedPattern  = new ArrayList();
                      ArrayList introducedVariable = new ArrayList();
                      newTermList = abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);

                      if(abstractedPattern.size() > 0) {
                          /* generate a new match construct */
                      
                        TomTerm generatedPatternAction =
                          `PatternAction(TermList(ast().makeList(abstractedPattern)),newActionInst, concOption());        
                          /* We reconstruct only a list of option with orgTrack and GeneratedMatch*/
                        OptionList generatedMatchOptionList = `concOption(orgTrack,GeneratedMatch());
                        Instruction generatedMatch =
                          `Match(SubjectList(ast().makeList(introducedVariable)),
                                 PatternList(cons(generatedPatternAction,empty())),
                                 generatedMatchOptionList);
                          /*System.out.println("Generate new Match"+generatedMatch); */
                        generatedMatch = preProcessingInstruction(generatedMatch);
                        newPatternAction =
                          `PatternAction(TermList(newTermList),generatedMatch, option);
                      
                          /*System.out.println("newPatternAction = " + newPatternAction); */
                      }
                        /* do nothing */
                      break matchBlock;
                    }
                  
                    _ -> {
                      System.out.println("preProcessing: strange PatternAction: " + elt);
                        //System.out.println("termList = " + elt.getTermList());
                        //System.out.println("tom      = " + elt.getTom()); 
                      throw new TomRuntimeException(new Throwable("preProcessing: strange PatternAction: " + elt));
                    }
                  }
                } // end matchBlock
              
                newPatternList = append(newPatternAction,newPatternList);
                patternList = patternList.getTail();
              }
            
              Instruction newMatch = `Match(SubjectList(l1),
                                            PatternList(newPatternList),
                                            matchOptionList);
              return newMatch;
            }

            RuleSet(rl@manyTomRuleList(
                    RewriteRule[lhs=Term(Appl[nameList=(Name(tomName))])],_), orgTrack) -> {
              TomRuleList ruleList = `rl;
              if(debugMode) {
                debugKey = `orgTrack.getFileName().getString() + `orgTrack.getLine();
              }
              TomSymbol tomSymbol = symbolTable().getSymbol(`tomName);
              TomName name = tomSymbol.getAstName();
              TomTypeList typesList = tomSymbol.getTypesToType().getDomain();        
              TomNumberList path = tsf().makeTomNumberList();
              TomList matchArgumentsList = empty();
              TomList patternActionList  = empty();
              TomTerm variable;
              int index = 0;
            
              path = (TomNumberList) path.append(`RuleVar());
            
              while(!typesList.isEmpty()) {
                TomType subtermType = typesList.getHead();
                variable = `Variable(option(),PositionName(appendNumber(index,path)),subtermType,concConstraint());
                matchArgumentsList = append(variable,matchArgumentsList);
                typesList = typesList.getTail();
                index++;
              }
            
              while(!ruleList.isEmpty()) {
                TomRule rule = ruleList.getHead();
                %match(TomRule rule) {
                  RewriteRule(Term(Appl[args=matchPatternsList]),
                              Term(rhsTerm),
                              condList,
                              option) -> {
                  
                    TomTerm newRhs = preProcessing(`MakeTerm(rhsTerm));
                    Instruction rhsInst = `Return(newRhs);
                    if(debugMode) {
                      TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.patternSuccess(\""+debugKey+"\");\n");
                      rhsInst = `UnamedBlock(concInstruction(TargetLanguageToInstruction(tl), rhsInst));
                    }
                    Instruction newRhsInst = `buildCondition(condList,rhsInst);
                  
                    patternActionList = append(`PatternAction(TermList(matchPatternsList),newRhsInst, option),patternActionList);
                  }
                } 
                ruleList = ruleList.getTail();
              }
            
              TomTerm subjectListAST = `SubjectList(matchArgumentsList);
              Instruction makeFunctionBeginAST = `MakeFunctionBegin(name,subjectListAST);
              ArrayList optionList = new ArrayList();
              optionList.add(`orgTrack);
                //optionList.add(tsf().makeOption_GeneratedMatch());
              OptionList generatedOptions = ast().makeOptionList(optionList);
              Instruction matchAST = `Match(SubjectList(matchArgumentsList),
                                            PatternList(patternActionList),
                                            generatedOptions);
              Instruction buildAST = `Return(BuildTerm(name,(TomList) traversal().genericTraversal(matchArgumentsList,replace_preProcessing_makeTerm)));
              InstructionList l;
              if(eCode) {
                l = `concInstruction(
                  makeFunctionBeginAST,
                  LocalVariable(),
                  EndLocalVariable(),
                  matchAST,
                  buildAST,
                  MakeFunctionEnd()
                  );
              } else {
                l = `concInstruction(
                  makeFunctionBeginAST,
                  matchAST,
                  buildAST,
                  MakeFunctionEnd()
                  );
              }
            
              return preProcessingInstruction(`AbstractBlock(l));
            }

          } // end match

        } // end instanceof Instruction

          /*
           * Defaul case: traversal
           */
        return traversal().genericTraversal(subject,this);
      } // end apply
    };
  
  Replace1 replace_preProcessing_makeTerm = new Replace1() {
      public ATerm apply(ATerm t) {
        return preProcessing(`MakeTerm((TomTerm)t));
      }
    }; 

  private TomTerm preProcessing(TomTerm subject) {
      //System.out.println("preProcessing subject: " + subject);
    return (TomTerm) replace_preProcessing.apply(subject); 
  }
  
  private Instruction preProcessingInstruction(Instruction subject) {
      //System.out.println("preProcessing subject: " + subject);
    return (Instruction) replace_preProcessing.apply(subject); 
  }
 
  private Instruction buildCondition(InstructionList condList, Instruction action) {
    %match(InstructionList condList) {
      emptyInstructionList() -> { return action; }
       
      manyInstructionList(MatchingCondition[lhs=pattern,rhs=subject], tail) -> {
        Instruction newAction = `buildCondition(tail,action);

        TomType subjectType = getTermType(pattern);
        TomNumberList path = tsf().makeTomNumberList();
        path = (TomNumberList) path.append(`RuleVar());
        TomTerm newSubject = preProcessing(`MakeTerm(subject));
        TomTerm introducedVariable = newSubject;
        TomTerm generatedPatternAction =
          `PatternAction(TermList(cons(pattern,empty())),newAction, option());        

          // Warning: The options are not good
        Instruction generatedMatch =
          `Match(SubjectList(cons(introducedVariable,empty())),
                 PatternList(cons(generatedPatternAction,empty())),
                 option());
        return generatedMatch;
      }

      manyInstructionList(EqualityCondition[lhs=lhs,rhs=rhs], tail) -> {
        Instruction newAction = `buildCondition(tail,action);

        TomTerm newLhs = preProcessing(`MakeTerm(lhs));
        TomTerm newRhs = preProcessing(`MakeTerm(rhs));
        Expression equality = `EqualTerm(newLhs,newRhs);
        Instruction generatedTest = `IfThenElse(equality,newAction,Nop());
        return generatedTest;
      }
      
      _ -> {
        System.out.println("buildCondition strange term: " + condList);
        throw new TomRuntimeException(new Throwable("buildCondition strange term: " + condList));
      }
    }
  }
  
  private TomTerm renameVariable(TomTerm subject,
                                 Map multiplicityMap,
                                 Map maxmultiplicityMap,
                                 ArrayList equalityCheck) {
    TomTerm renamedTerm = subject;
    
    %match(TomTerm subject) {
      UnamedVariable[option=optionList,astType=type,constraints=constraint] -> {
        //OptionList newOptionList = `renameVariableInOptionList(optionList,multiplicityMap,maxmultiplicityMap,equalityCheck);
        ConstraintList newConstraintList = `renameVariableInConstraintList(constraint,multiplicityMap,maxmultiplicityMap,equalityCheck);
        return `UnamedVariable(optionList,type,newConstraintList);
      }
      
      UnamedVariableStar[option=optionList,astType=type,constraints=constraint] -> {
        //OptionList newOptionList = `renameVariableInOptionList(optionList,multiplicityMap,maxmultiplicityMap,equalityCheck);
        ConstraintList newConstraintList = `renameVariableInConstraintList(constraint,multiplicityMap,maxmultiplicityMap,equalityCheck);
        return `UnamedVariableStar(optionList,type,newConstraintList);
      }

			Variable[option=optionList,astName=name,astType=type,constraints=clist] -> {
				Integer multiplicity = (Integer) multiplicityMap.get(name);
				int mult = multiplicity.intValue();
				Integer maxmultiplicity = (Integer) maxmultiplicityMap.get(name);
				int maxmult = maxmultiplicity.intValue();
				if(mult > 1) {
					mult = mult-1;
					multiplicityMap.put(name,new Integer(mult));

					TomNumberList path = tsf().makeTomNumberList();
					path = (TomNumberList) path.append(`RenamedVar(name));
					path = (TomNumberList) path.append(makeNumber(mult));
					//OptionList newOptionList = renameVariableInOptionList(optionList,multiplicityMap,maxmultiplicityMap,equalityCheck);
					ConstraintList newConstraintList = renameVariableInConstraintList(clist,multiplicityMap,maxmultiplicityMap,equalityCheck);
					if (mult < maxmult -1) {
						// add the constraint renamedVariable = renamedVariable+1
						TomTerm oldVar;
						TomNumberList oldpath = tsf().makeTomNumberList();
						oldpath = (TomNumberList) oldpath.append(`RenamedVar(name));
						oldpath = (TomNumberList) oldpath.append(makeNumber(mult + 1));
						oldVar = `Variable(optionList,PositionName(oldpath),type,concConstraint());
						renamedTerm = `Variable(optionList,PositionName(path),type,concConstraint(Equal(oldVar),newConstraintList*));
					} else {
						// No constraint for the first renamed variable
						renamedTerm = `Variable(optionList,PositionName(path),type,newConstraintList);
					}
					// System.out.println("renamedTerm = " + renamedTerm);

					Expression newEquality = `EqualTerm(subject,renamedTerm);
					equalityCheck.add(newEquality);
				}
				else if (maxmult > 1) {
					TomNumberList oldpath = tsf().makeTomNumberList();
					oldpath = (TomNumberList) oldpath.append(`RenamedVar(name));
					oldpath = (TomNumberList) oldpath.append(makeNumber(mult));
					TomTerm oldVar = `Variable(optionList,PositionName(oldpath),type,concConstraint());
					renamedTerm = `Variable(optionList,name,type,concConstraint(Equal(oldVar),clist*));
				}
				return renamedTerm;
			}

			VariableStar[option=optionList,astName=name,astType=type,constraints=clist] -> {
				Integer multiplicity = (Integer)multiplicityMap.get(name);
				int mult = multiplicity.intValue();
				Integer maxmultiplicity = (Integer) maxmultiplicityMap.get(name);
				int maxmult = maxmultiplicity.intValue();
				if(mult > 1) {
					mult = mult-1;
					multiplicityMap.put(name,new Integer(mult));

					TomNumberList path = tsf().makeTomNumberList();
					path = (TomNumberList) path.append(`RenamedVar(name));
					path = appendNumber(mult,path);
					//OptionList newOptionList = renameVariableInOptionList(optionList,multiplicityMap,maxmultiplicityMap,equalityCheck);
					ConstraintList newConstraintList = renameVariableInConstraintList(clist,multiplicityMap,maxmultiplicityMap,equalityCheck);
					if (mult < maxmult -1) {
						// add the constraint renamedVariable = renamedVariable+1
						TomTerm oldVar;
						TomNumberList oldpath = tsf().makeTomNumberList();
						oldpath = (TomNumberList) oldpath.append(`RenamedVar(name));
						oldpath = (TomNumberList) oldpath.append(makeNumber(mult + 1));
						oldVar = `VariableStar(optionList,PositionName(oldpath),type,concConstraint());
						renamedTerm = `VariableStar(optionList,PositionName(path),type,concConstraint(Equal(oldVar),newConstraintList*));
					} else {
						// No constraint for the first renamed variable
						renamedTerm = `VariableStar(optionList,PositionName(path),type,newConstraintList);
					}
					//System.out.println("renamedTerm = " + renamedTerm);

					Expression newEquality = `EqualTerm(subject,renamedTerm);
					equalityCheck.add(newEquality);
				} 
				else if (maxmult > 1) {
					TomNumberList oldpath = tsf().makeTomNumberList();
					oldpath = (TomNumberList) oldpath.append(`RenamedVar(name));
					oldpath = (TomNumberList) oldpath.append(makeNumber(mult));
					TomTerm oldVar = `VariableStar(optionList,PositionName(oldpath),type,concConstraint());
					renamedTerm =`VariableStar(optionList,name,type,concConstraint(Equal(oldVar),clist*));
				}
				return renamedTerm;
			}

      Appl[option=optionList, nameList=nameList, args=arguments, constraints=constraints] -> {
        TomList args = `arguments;
        TomList newArgs = empty();
        while(!args.isEmpty()) {
          TomTerm elt = args.getHead();
          TomTerm newElt = renameVariable(elt,multiplicityMap,maxmultiplicityMap,equalityCheck);
          newArgs = append(newElt,newArgs);
          args = args.getTail();
        }
        //OptionList newOptionList = renameVariableInOptionList(`optionList,multiplicityMap,maxmultiplicityMap,equalityCheck);
        ConstraintList newConstraintList = renameVariableInConstraintList(`constraints,multiplicityMap,maxmultiplicityMap,equalityCheck);
        renamedTerm = `Appl(optionList,nameList,newArgs,newConstraintList);
        return renamedTerm;
      }
    }
    return renamedTerm;
  }

  private ConstraintList renameVariableInConstraintList(ConstraintList constraintList,
                                                Map multiplicityMap,
                                                Map maxmultiplicityMap,
                                                ArrayList equalityCheck) {
		System.out.println("slist : " + constraintList);
    ArrayList list = new ArrayList();
    while(!constraintList.isEmpty()) {
      Constraint cstElt = constraintList.getHead();
      Constraint newCstElt = cstElt;
      %match(Constraint cstElt) {
        AssignTo(var@Variable[]) -> {
          newCstElt = `AssignTo(renameVariable(var,multiplicityMap,maxmultiplicityMap,equalityCheck));
        }
      }
      list.add(newCstElt);
      constraintList = constraintList.getTail();
    }
		System.out.println(" gives : " + list);
    return ast().makeConstraintList(list);
  }
/*
  private OptionList renameVariableInOptionList(OptionList optionList,
                                                Map multiplicityMap,
                                                Map maxmultiplicityMap,
                                                ArrayList equalityCheck) {
    ArrayList list = new ArrayList();
    while(!optionList.isEmpty()) {
      Option optElt = optionList.getHead();
      Option newOptElt = optElt;
      %match(Option optElt) {
        TomTermToOption(var@Variable[]) -> {
          newOptElt = `TomTermToOption(renameVariable(var,multiplicityMap,maxmultiplicityMap,equalityCheck));
        }
      }
      list.add(newOptElt);
      optionList = optionList.getTail();
    }
    return ast().makeOptionList(list);
  }
*/
  
  private TomList linearizePattern(TomList subject, ArrayList equalityCheck) {
    Map multiplicityMap = collectMultiplicity(subject);
    Map maxmultiplicityMap = new HashMap();
		// maxMultMap must be a deep clone of multMap
		Iterator it = multiplicityMap.keySet().iterator();
		Integer value;
		while (it.hasNext()) {
			Object key = it.next();
			value = new Integer(((Integer)multiplicityMap.get(key)).intValue());
			maxmultiplicityMap.put(key,value);
		}
      // perform the renaming and generate equality checks
    TomList newList = empty();
    while(!subject.isEmpty()) {
      TomTerm elt = subject.getHead();
      TomTerm newElt = renameVariable(elt,multiplicityMap,maxmultiplicityMap,equalityCheck);
      newList = append(newElt,newList);
      subject = subject.getTail();
    }
    return newList;
  }
  
  private TomTerm abstractPattern(TomTerm subject,
                                  ArrayList abstractedPattern,
                                  ArrayList introducedVariable)  {
    TomTerm abstractedTerm = subject;
    %match(TomTerm subject) {
      Appl[option=option, nameList=nameList@(Name(tomName),_*), args=arguments, constraints=constraints] -> {
        TomList args = `arguments;
        TomSymbol tomSymbol = symbolTable().getSymbol(`tomName);
        
        TomList newArgs = empty();
        if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
          while(!args.isEmpty()) {
            TomTerm elt = args.getHead();
            TomTerm newElt = elt;
            %match(TomTerm elt) {
              appl@Appl[nameList=(Name(tomName2),_*)] -> {
                /*
                 * we no longer abstract syntactic subterm
                 * they are compiled by the TomKernelCompiler
                 */

                  //System.out.println("Abstract: " + appl);
                TomSymbol tomSymbol2 = symbolTable().getSymbol(`tomName2);
                if(isListOperator(tomSymbol2) || isArrayOperator(tomSymbol2)) {
                  TomType type2 = tomSymbol2.getTypesToType().getCodomain();
                  abstractedPattern.add(`appl);
                  
                  TomNumberList path = tsf().makeTomNumberList();
                  //path = append(`AbsVar(makeNumber(introducedVariable.size())),path);
                  absVarNumber++;
                  path = (TomNumberList) path.append(`AbsVar(makeNumber(absVarNumber)));
                  
                  TomTerm newVariable = `Variable(option(),PositionName(path),type2,concConstraint());
                  
                  //System.out.println("newVariable = " + newVariable);
                  
                  introducedVariable.add(newVariable);
                  newElt = newVariable;
                }
              }
            }
            newArgs = append(newElt,newArgs);
            args = args.getTail();
          }
        } else {
          newArgs = abstractPatternList(args,abstractedPattern,introducedVariable);
        }
        abstractedTerm = `Appl(option,nameList,newArgs,constraints);
      }
    } // end match
    return abstractedTerm;
  }

  private TomList abstractPatternList(TomList subjectList,
                                      ArrayList abstractedPattern,
                                      ArrayList introducedVariable)  {
    TomList newList = empty();
    while(!subjectList.isEmpty()) {
      TomTerm elt = subjectList.getHead();
      TomTerm newElt = abstractPattern(elt,abstractedPattern,introducedVariable);
      newList = append(newElt,newList);
      subjectList = subjectList.getTail();
    }
    return newList;
  }
  
} //class TomCompiler
