/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import jtom.adt.tomsignature.types.*;
import jtom.exception.TomRuntimeException;
import jtom.tools.TomFactory;
import jtom.tools.TomGenericPlugin;
import jtom.tools.Tools;
import tom.library.traversal.Replace1;
import tom.library.traversal.Replace3;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

/**
 * The TomCompiler plugin.
 */
public class TomCompiler extends TomGenericPlugin {

  %include { adt/tomsignature/TomSignature.tom }

  /** some output suffixes */
  public static final String COMPILED_SUFFIX = ".tfix.compiled";
  
  /** the declared options string*/
  public static final String DECLARED_OPTIONS = "<options><boolean name='compile' altName='' description='Compiler (activated by default)' value='true'/></options>";
  
  /** the tomfactory for creating intermediate terms */
  private TomFactory tomFactory;
  
  /** unicity var counter*/
  private int absVarNumber;
  
  /** Constructor*/
  public TomCompiler() {
    super("TomCompiler");
    this.tomFactory = new TomFactory();
  }
  
  public void run() {
    TomKernelCompiler tomKernelCompiler = new TomKernelCompiler(getStreamManager().getSymbolTable());
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    TomTerm compiledTerm = null;
    try {
      // renit absVarNumber to generate reproductable output
      absVarNumber = 0;
      TomTerm preCompiledTerm = preProcessing((TomTerm)getWorkingTerm());
      //System.out.println("preCompiledTerm = \n" + preCompiledTerm);
      compiledTerm = tomKernelCompiler.compileMatching(preCompiledTerm);
      // verbose
      getLogger().log( Level.INFO, "TomCompilationPhase",
                       new Integer((int)(System.currentTimeMillis()-startChrono)) );      
      setWorkingTerm(compiledTerm);
    } catch (Exception e) {
      getLogger().log( Level.SEVERE, "ExceptionMessage",
                       new Object[]{getStreamManager().getInputFile().getName(), "TomCompiler", e.getMessage()} );
      e.printStackTrace();
    }
    if(intermediate) {
      Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() + COMPILED_SUFFIX, compiledTerm);
    }
  }
  
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomCompiler.DECLARED_OPTIONS);
  }
  
  private OptionList option() {
    return getAstFactory().makeOption();
  }
  
  /* 
   * preProcessing:
   * replaces BuildReducedTerm by BuildList, BuildArray or BuildTerm
   *
   * transforms RuleSet into Function + Match + BuildReducedTerm
   * abstract list-matching patterns
   * rename non-linear patterns
   */
  
  Replace1 replace_preProcessing = new Replace1() {
      public ATerm apply(ATerm subject) {
        if(subject instanceof TomTerm) {
          %match(TomTerm subject) {
            BuildReducedTerm(var@(Variable|VariableStar)[]) -> {
              return `var;
            }    

            BuildReducedTerm(Appl[nameList=(name@Name(tomName)),args=termArgs]) -> {
              TomSymbol tomSymbol = symbolTable().getSymbolFromName(`tomName);
              TomList newTermArgs = (TomList) traversal().genericTraversal(`termArgs,replace_preProcessing_makeTerm);
              if(tomSymbol==null || isDefinedSymbol(tomSymbol)) {
                return `FunctionCall(name,newTermArgs);
              } else {
                if(isListOperator(tomSymbol)) {
                  return tomFactory.buildList(`name,newTermArgs);
                } else if(isArrayOperator(tomSymbol)) {
                  return tomFactory.buildArray(`name,newTermArgs);
                } else {
                  return `BuildTerm(name,newTermArgs);
                }
              }
            }

          } // end match
        } else if(subject instanceof Instruction) {
          %match(Instruction subject) {
            Match(SubjectList(l1),patternInstructionList, matchOptionList)  -> {
              Option orgTrack = findOriginTracking(`matchOptionList);
              PatternInstructionList newPatternInstructionList = `concPatternInstruction();
              while(!patternInstructionList.isEmpty()) {
                /*
                 * the call to preProcessing performs the recursive expansion
                 * of nested match constructs
                 */
                PatternInstruction elt = preProcessingPatternInstruction(patternInstructionList.getHead());
                PatternInstruction newPatternInstruction = elt;
              
                matchBlock: {
                  %match(PatternInstruction elt) {
                    PatternInstruction(Pattern(termList,guardList),actionInst, option) -> {
                      TomList newTermList = empty();
                      /* generate equality checks */
                      ArrayList equalityCheck = new ArrayList();
                      TomList renamedTermList = linearizePattern(`termList,equalityCheck);
                      newPatternInstruction = `PatternInstruction(Pattern(renamedTermList,guardList),actionInst, option);        
                      /* attach guards to variables or applications*/
                      TomList constrainedTermList = renamedTermList;
                      TomList l = `guardList;
                      while(!l.isEmpty()) {
                        TomTerm guard = l.getHead();
                        //System.out.println("try to attach "+guard+" to "+constrainedTermList);
                        constrainedTermList = attachConstraint(constrainedTermList,guard);
                        l = l.getTail();
                      }
                      TomList emptyGuardList = `empty();
                      newPatternInstruction = `PatternInstruction(Pattern(constrainedTermList,emptyGuardList),actionInst, option);        

                      /* abstract patterns */
                      ArrayList abstractedPattern  = new ArrayList();
                      ArrayList introducedVariable = new ArrayList();
                      newTermList = abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);

                      /* newPatternInstruction is overwritten when abstraction is performed */
                      if(abstractedPattern.size() > 0) {
                        /* generate a new match construct */
                      
                        PatternInstruction generatedPatternInstruction =
                          `PatternInstruction(Pattern(getAstFactory().makeList(abstractedPattern),emptyGuardList),actionInst, concOption());        
                        /* We reconstruct only a list of option with orgTrack and GeneratedMatch*/
                        OptionList generatedMatchOptionList = `concOption(orgTrack,GeneratedMatch());
                        Instruction generatedMatch =
                          `Match(SubjectList(getAstFactory().makeList(introducedVariable)),
                                 concPatternInstruction(generatedPatternInstruction),
                                 generatedMatchOptionList);
                        /*System.out.println("Generate new Match"+generatedMatch); */
                        generatedMatch = preProcessingInstruction(generatedMatch);
                        newPatternInstruction =
                          `PatternInstruction(Pattern(newTermList,emptyGuardList),generatedMatch, option);
                      
                        /*System.out.println("newPatternInstruction = " + newPatternInstruction); */
                      }
                      /* do nothing */
                      break matchBlock;
                    }
                  
                    _ -> {
                      System.out.println("preProcessing: strange PatternInstruction: " + elt);
                      //System.out.println("termList = " + elt.getPattern());
                      //System.out.println("tom      = " + elt.getTom()); 
                      throw new TomRuntimeException("preProcessing: strange PatternInstruction: " + elt);
                    }
                  }
                } // end matchBlock
              
                newPatternInstructionList = (PatternInstructionList) newPatternInstructionList.append(newPatternInstruction);
                patternInstructionList = patternInstructionList.getTail();
              }
            
              Instruction newMatch = `Match(SubjectList(l1),
                                            newPatternInstructionList,
                                            matchOptionList);
              return newMatch;
            }

            RuleSet(rl@manyTomRuleList(RewriteRule[lhs=Term(Appl[nameList=(Name(tomName))])],_), orgTrack) -> {
              TomRuleList ruleList = `rl;
              TomSymbol tomSymbol = symbolTable().getSymbolFromName(`tomName);
              TomName name = tomSymbol.getAstName();
              TomTypeList typesList = tomSymbol.getTypesToType().getDomain();        
              TomNumberList path = tsf().makeTomNumberList();
              TomList matchArgumentsList = empty();
              PatternInstructionList patternInstructionList  = `concPatternInstruction();
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
            
              //boolean hasDefaultCase = false;
              while(!ruleList.isEmpty()) {
                TomRule rule = ruleList.getHead();
                %match(TomRule rule) {
                  RewriteRule(Term(Appl[args=matchPatternsList]),
                              Term(rhsTerm),
                              condList,
                              option) -> {
                    TomTerm newRhs = preProcessing(`BuildReducedTerm(rhsTerm));
                    Instruction rhsInst = `IfThenElse(TrueTL(),Return(newRhs),Nop());
                    Instruction newRhsInst = `buildCondition(condList,rhsInst);
                    TomList guardList = empty();

                    patternInstructionList = (PatternInstructionList) patternInstructionList.append(`PatternInstruction(Pattern(matchPatternsList,guardList),newRhsInst, option));
                    //hasDefaultCase = hasDefaultCase || (isDefaultCase(matchPatternsList) && condList.isEmpty());
                  }
                } 
                ruleList = ruleList.getTail();
              }
            
              TomTerm subjectListAST = `SubjectList(matchArgumentsList);
              Instruction makeFunctionBeginAST = `MakeFunctionBegin(name,subjectListAST);
              ArrayList optionList = new ArrayList();
              optionList.add(`orgTrack);
              //optionList.add(tsf().makeOption_GeneratedMatch());
              OptionList generatedOptions = getAstFactory().makeOptionList(optionList);
              Instruction matchAST = `Match(SubjectList(matchArgumentsList),
                                            patternInstructionList,
                                            generatedOptions);

              Instruction buildAST;
              //if(hasDefaultCase) {
              //buildAST = `Nop();
              //} else {
                buildAST = `Return(BuildTerm(name,(TomList) traversal().genericTraversal(matchArgumentsList,replace_preProcessing_makeTerm)));
                //}

              InstructionList l;
              l = `concInstruction(
                                   makeFunctionBeginAST,
                                   matchAST,
                                   buildAST,
                                   MakeFunctionEnd()
                                   );
            
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

  /*  
  private boolean isDefaultCase(TomList l) {
    %match(TomList l) {
      emptyTomList() -> {
        return true;
      }
      manyTomList((UnamedVariable|UnamedVariableStar)[],tail) -> {
        return isDefaultCase(tail);
      }
      manyTomList((Variable|VariableStar)[],tail) -> {
        return isDefaultCase(tail);
      }
    }
    return false;
  }
  */

  Replace1 replace_preProcessing_makeTerm = new Replace1() {
      public ATerm apply(ATerm t) {
        return preProcessing(`BuildReducedTerm((TomTerm)t));
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

  private PatternInstruction preProcessingPatternInstruction(PatternInstruction subject) {
    return (PatternInstruction) replace_preProcessing.apply(subject); 
  }
 
  private Instruction buildCondition(InstructionList condList, Instruction action) {
    %match(InstructionList condList) {
      emptyInstructionList() -> { return action; }
       
      manyInstructionList(MatchingCondition[lhs=pattern,rhs=subject], tail) -> {
        Instruction newAction = `buildCondition(tail,action);

        TomType subjectType = getTermType(`pattern);
        TomNumberList path = tsf().makeTomNumberList();
        path = (TomNumberList) path.append(`RuleVar());
        TomTerm newSubject = preProcessing(`BuildReducedTerm(subject));
        TomTerm introducedVariable = newSubject;
        TomList guardList = empty();
        PatternInstruction generatedPatternInstruction =
          `PatternInstruction(Pattern(cons(pattern,empty()),guardList),newAction, option());        

          // Warning: The options are not good
        Instruction generatedMatch =
          `Match(SubjectList(cons(introducedVariable,empty())),
                 concPatternInstruction(generatedPatternInstruction),
                 option());
        return generatedMatch;
      }

      manyInstructionList(TypedEqualityCondition[tomType=type,lhs=lhs,rhs=rhs], tail) -> {
        Instruction newAction = `buildCondition(tail,action);

        TomTerm newLhs = preProcessing(`BuildReducedTerm(lhs));
        TomTerm newRhs = preProcessing(`BuildReducedTerm(rhs));
        Expression equality = `EqualTerm(type,newLhs,newRhs);
        Instruction generatedTest = `IfThenElse(equality,newAction,Nop());
        return generatedTest;
      }
      
      _ -> {
        throw new TomRuntimeException("buildCondition strange term: " + condList);
      }
    }
  }
  
  private TomTerm renameVariable(TomTerm subject,
                                 Map multiplicityMap,
                                 ArrayList equalityCheck) {
    TomTerm renamedTerm = subject;
    
    %match(TomTerm subject) {
      var@(UnamedVariable|UnamedVariableStar)[constraints=constraints] -> {
        ConstraintList newConstraintList = `renameVariableInConstraintList(constraints,multiplicityMap,equalityCheck);
        return `var.setConstraints(newConstraintList);
      }

      var@(Variable|VariableStar)[astName=name,constraints=clist] -> {
        ConstraintList newConstraintList = renameVariableInConstraintList(`clist,multiplicityMap,equalityCheck);
        if(!multiplicityMap.containsKey(`name)) {
          // We see this variable for the first time
          multiplicityMap.put(`name,new Integer(1));
          renamedTerm = `var.setConstraints(newConstraintList);
        } else {
          // We have already seen this variable
          Integer multiplicity = (Integer) multiplicityMap.get(`name);
          int mult = multiplicity.intValue(); 
          multiplicityMap.put(`name,new Integer(mult+1));
          
          TomNumberList path = tsf().makeTomNumberList();
          path = (TomNumberList) path.append(`RenamedVar(name));
          path = (TomNumberList) path.append(makeNumber(mult));

          renamedTerm = `var.setAstName(`PositionName(path));
          renamedTerm = renamedTerm.setConstraints(`concConstraint(Equal(var.setConstraints(concConstraint())),newConstraintList*));
        }

        return renamedTerm;
      }

      Appl[option=optionList, nameList=nameList, args=arguments, constraints=constraints] -> {
        TomList args = `arguments;
        TomList newArgs = empty();
        while(!args.isEmpty()) {
          TomTerm elt = args.getHead();
          TomTerm newElt = renameVariable(elt,multiplicityMap,equalityCheck);
          newArgs = append(newElt,newArgs);
          args = args.getTail();
        }
        ConstraintList newConstraintList = renameVariableInConstraintList(`constraints,multiplicityMap,equalityCheck);
        renamedTerm = `Appl(optionList,nameList,newArgs,newConstraintList);
        return renamedTerm;
      }
    }
    return renamedTerm;
  }

  private ConstraintList renameVariableInConstraintList(ConstraintList constraintList,
                                                Map multiplicityMap,
                                                ArrayList equalityCheck) {
    ArrayList list = new ArrayList();
    while(!constraintList.isEmpty()) {
      Constraint cstElt = constraintList.getHead();
      Constraint newCstElt = cstElt;
      %match(Constraint cstElt) {
        AssignTo(var@Variable[]) -> {
          newCstElt = `AssignTo(renameVariable(var,multiplicityMap,equalityCheck));
        }
      }
      list.add(newCstElt);
      constraintList = constraintList.getTail();
    }
    return getAstFactory().makeConstraintList(list);
  }

  private TomList linearizePattern(TomList subject, ArrayList equalityCheck) {
    Map multiplicityMap = new HashMap();
      // perform the renaming and generate equality checks
    TomList newList = empty();
    while(!subject.isEmpty()) {
      TomTerm elt = subject.getHead();
      TomTerm newElt = renameVariable(elt,multiplicityMap,equalityCheck);
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
      Appl[nameList=(Name(tomName),_*), args=arguments] -> {
        TomList args = `arguments;
        TomSymbol tomSymbol = symbolTable().getSymbolFromName(`tomName);
        
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
                TomSymbol tomSymbol2 = symbolTable().getSymbolFromName(`tomName2);
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
        abstractedTerm = subject.setArgs(newArgs);
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

  private TomList attachConstraint(TomList subjectList,
                                   TomTerm constraint) {
    HashSet patternVariable = new HashSet();
    HashSet constraintVariable = new HashSet();
    collectVariable(patternVariable,subjectList);
    collectVariable(constraintVariable,constraint);
    patternVariable.retainAll(constraintVariable);
    //System.out.println("attach constraint "+subjectList+" "+patternVariable+" "+constraint);
    TomList newSubjectList = (TomList) replace_attachConstraint.apply(subjectList,patternVariable,constraint); 

    //System.out.println("newSubjectList = " + newSubjectList);

    return newSubjectList;
  }

  protected Replace3 replace_attachConstraint = new Replace3() { 
      public ATerm apply(ATerm subject, Object arg1, Object arg2) {
        Set variableSet = (Set) arg1;
        TomTerm constraint = (TomTerm) arg2;

        if(subject instanceof TomTerm) {
          %match(TomTerm subject) {
            var@(Variable|VariableStar)[constraints=constraintList] -> {
              //System.out.println("var = " + var);
              //System.out.println("set1 = " + variableSet);
              variableSet.remove(`var);
              //System.out.println("set2 = " + variableSet);

              if(variableSet.isEmpty()) {
                ConstraintList newConstraintList = (ConstraintList)constraintList.append(`Ensure(preProcessing(BuildReducedTerm(constraint))));
                return var.setConstraints(newConstraintList);
              }
              //return var;
            }

            appl@Appl[constraints=constraintList] -> {
              if(variableSet.isEmpty()) {
                ConstraintList newConstraintList = (ConstraintList)constraintList.append(`Ensure(preProcessing(BuildReducedTerm(constraint))));
                return appl.setConstraints(newConstraintList);
              }
              //return appl;
            }
          }
        }

        return traversal().genericTraversal(subject,this,variableSet,constraint);
      } // end apply
    }; // end new


} // class TomCompiler
