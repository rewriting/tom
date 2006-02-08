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

import java.util.*;
import java.util.logging.Level;

import tom.engine.adt.tomsignature.types.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.TomMessage;
import tom.engine.tools.TomFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
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
      getLogger().log( Level.INFO, TomMessage.tomCompilationPhase.getMessage(),
                       new Integer((int)(System.currentTimeMillis()-startChrono)) );      
      setWorkingTerm(compiledTerm);
    } catch (Exception e) {
      getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
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

            BuildReducedTerm(RecordAppl[option=optionList,nameList=(name@Name(tomName)),slots=termArgs]) -> {
              TomSymbol tomSymbol = symbolTable().getSymbolFromName(`tomName);
              SlotList newTermArgs = (SlotList) traversal().genericTraversal(`termArgs,replace_preProcessing_makeTerm);
              TomList tomListArgs = slotListToTomList(newTermArgs);

              if(tomSymbol != null) {
                if(isListOperator(tomSymbol)) {
                  return tomFactory.buildList(`name,tomListArgs);
                } else if(isArrayOperator(tomSymbol)) {
                  return tomFactory.buildArray(`name,tomListArgs);
                } else if(symbolTable().isBuiltinType(getTomType(getSymbolCodomain(tomSymbol))) && 
                          `termArgs.isEmpty() && 
                          !hasConstructor(`optionList)) {
                  return `BuildVariable(name,emptyTomList());
                } else if(isDefinedSymbol(tomSymbol)) {
                  return `FunctionCall(name,tomListArgs);
                } else {
                  return `BuildTerm(name,tomListArgs);
                }
              } else if(`termArgs.isEmpty() && !hasConstructor(`optionList)) {
                return `BuildVariable(name,emptyTomList());
              } else {
                return `FunctionCall(name,tomListArgs);
              }

            }

          } // end match
        } else if(subject instanceof Instruction) {
          %match(Instruction subject) {
            Match(SubjectList(l1),patternInstructionList, matchOptionList)  -> {
              Option orgTrack = findOriginTracking(`matchOptionList);
              PatternInstructionList newPatternInstructionList = `concPatternInstruction();
              PatternList negativePattern = `concPattern();
              while(!`patternInstructionList.isEmpty()) {
                /*
                 * the call to preProcessing performs the recursive expansion
                 * of nested match constructs
                 */
                PatternInstruction elt = preProcessingPatternInstruction(`patternInstructionList.getHead());
                PatternInstruction newPatternInstruction = elt;

                matchBlock: {
                  %match(PatternInstruction elt) {
                    PatternInstruction(pattern@Pattern[subjectList=subjectList,tomList=termList,guards=guardList],actionInst, option) -> {
                      Instruction newAction = `actionInst;
                      /* expansion of RawAction into TypedAction */
                      %match(Instruction actionInst) {
                        RawAction(x) -> { 
                          newAction=`TypedAction(x,pattern,negativePattern);
                        }
                      }
                      negativePattern = (PatternList) negativePattern.append(`pattern);

                      /* generate equality checks */
                      ArrayList equalityCheck = new ArrayList();
                      TomList renamedTermList = linearizePattern(`termList,equalityCheck);
                      newPatternInstruction = `PatternInstruction(Pattern(subjectList,renamedTermList,guardList),newAction, option);        
                      /* attach guards to variables or applications*/
                      TomList constrainedTermList = renamedTermList;
                      TomList l = `guardList;
                      while(!l.isEmpty()) {
                        TomTerm guard = l.getHead();
                        //System.out.println("try to attach "+guard+"\nto "+constrainedTermList);
                        constrainedTermList = attachConstraint(constrainedTermList,guard);
                        l = l.getTail();
                      }
                      TomList emptyGuardList = `empty();
                      newPatternInstruction = `PatternInstruction(Pattern(subjectList,constrainedTermList,emptyGuardList),newAction, option);        

                      /* abstract patterns */
                      ArrayList abstractedPattern  = new ArrayList();
                      ArrayList introducedVariable = new ArrayList();
                      TomList newTermList = abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);

                      /* newPatternInstruction is overwritten when abstraction is performed */
                      if(abstractedPattern.size() > 0) {
                        /* generate a new match construct */
                      
                        TomList generatedSubjectList = `getAstFactory().makeList(introducedVariable);
                        PatternInstruction generatedPatternInstruction =
                          `PatternInstruction(Pattern(generatedSubjectList, getAstFactory().makeList(abstractedPattern),emptyGuardList),newAction, concOption());        
                        /* We reconstruct only a list of option with orgTrack and GeneratedMatch*/
                        OptionList generatedMatchOptionList = `concOption(orgTrack,GeneratedMatch());
                        Instruction generatedMatch =
                          `Match(SubjectList(generatedSubjectList),
                                 concPatternInstruction(generatedPatternInstruction),
                                 generatedMatchOptionList);
                        /*System.out.println("Generate new Match"+generatedMatch); */
                        generatedMatch = preProcessingInstruction(generatedMatch);
                        newPatternInstruction =
                          `PatternInstruction(Pattern(subjectList,newTermList,emptyGuardList),generatedMatch, option);
                      
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
                `patternInstructionList = `patternInstructionList.getTail();
              }
            
              Instruction newMatch = `Match(SubjectList(l1),
                                            newPatternInstructionList,
                                            matchOptionList);
              return newMatch;
            }

            RuleSet(rl@manyTomRuleList(RewriteRule[lhs=Term(RecordAppl[nameList=(Name(tomName))])],_), orgTrack) -> {
              TomSymbol tomSymbol = symbolTable().getSymbolFromName(`tomName);
              TomName name = tomSymbol.getAstName();
              PatternInstructionList patternInstructionList  = `concPatternInstruction();

              //build variables list for lhs symbol
              TomTypeList typesList = getSymbolDomain(tomSymbol);
              TomList subjectListAST = empty();
              TomNumberList path = `concTomNumber(RuleVar());
              TomType subtermType;
              TomTerm variable;
              int index = 0; 
              while(!typesList.isEmpty()) {
                subtermType = typesList.getHead();
                variable = `Variable(option(),PositionName(appendNumber(index,path)),subtermType,concConstraint());
                subjectListAST = append(variable,subjectListAST);
                typesList = typesList.getTail();
                index++;
              }

              TomRuleList ruleList = `rl;
              TomRule rule;
              TomTerm newRhs;
              Instruction rhsInst,newRhsInst;
              Pattern pattern;
              TomList guardList = empty();//no guardlist in pattern
              while(!ruleList.isEmpty()) {
                rule = ruleList.getHead();
                %match(TomRule rule) {
                  RewriteRule(Term(RecordAppl[slots=matchPatternsList]),//lhsTerm
                              Term(rhsTerm),
                              condList,
                              option) -> {
                    //transform rhsTerm into Instruction to build PatternInstructionList
                    newRhs = preProcessing(`BuildReducedTerm(rhsTerm));
                    rhsInst = `If(TrueTL(),Return(newRhs),Nop());
                    newRhsInst = `buildCondition(condList,rhsInst);
                    pattern = `Pattern(subjectListAST,slotListToTomList(matchPatternsList),guardList);
                    patternInstructionList = (PatternInstructionList) patternInstructionList.append(`PatternInstruction(pattern,RawAction(newRhsInst),option));
                  }
                } 
                ruleList = ruleList.getTail();
              }
            
              Instruction makeFunctionBeginAST = `MakeFunctionBegin(name,SubjectList(subjectListAST));
              Instruction matchAST = `Match(SubjectList(subjectListAST),
                                            patternInstructionList,
                                            concOption(orgTrack));
              //return type `name(subjectListAST)
              Instruction buildAST = `Return(BuildTerm(name,(TomList) traversal().genericTraversal(subjectListAST,replace_preProcessing_makeTerm)));

              InstructionList l = `concInstruction(makeFunctionBeginAST,matchAST,buildAST,MakeFunctionEnd());
              return preProcessingInstruction(`AbstractBlock(l));
            }

           Strategy(name,visitList,orgTrack) -> {
             InstructionList l = `concInstruction();//represents compiled Strategy
            TomList subjectListAST;
            TomVisit visit;
            TomVisitList jVisitList = `visitList;
            TomTerm arg;//arg = subjectList
            String funcName;
            TomForwardType visitorFwd = null;
            while (!jVisitList.isEmpty()){
              subjectListAST = empty();
              visit = jVisitList.getHead();
              %match(TomVisit visit) {
                VisitTerm(visitType,patternInstructionList) -> {
                  if (visitorFwd == null) {//first time in loop
                    visitorFwd = symbolTable().getForwardType(`visitType.getTomType().getString());//do the job only once
                  }
                  arg = `Variable(option(),Name("arg"),visitType,concConstraint());//one argument only in visit_Term
                  subjectListAST = append(arg,subjectListAST);
                  funcName = "visit_" + getTomType(`visitType);//function signature is visit_Term(Term arg) throws VisitFailure.
                  l = `concInstruction(l*,FunctionDef(Name(funcName),concTomTerm(arg),visitType,TomTypeAlone("VisitFailure"),Match(SubjectList(subjectListAST),
                          patternInstructionList, 
                          concOption(orgTrack))));
                }
              }
              jVisitList = jVisitList.getTail();
            }
            return `Class(name,visitorFwd,preProcessingInstruction(AbstractBlock(l)));
           }

          } // end match

        } // end instanceof Instruction

          /*
           * Default case: traversal
           */
        return traversal().genericTraversal(subject,this);
      } // end apply
    };

  private Replace1 replace_preProcessing_makeTerm = new Replace1() {
      public ATerm apply(ATerm t) {
        if(t instanceof TomTerm) {
          //System.out.println("replace_preProcessing_makeTerm: " + t);
          return preProcessing(`BuildReducedTerm((TomTerm)t));
        } else {
          //System.out.println("replace_preProcessing_makeTerm: *** " + t);
          return traversal().genericTraversal(t,replace_preProcessing_makeTerm);
        }
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
        TomList generatedSubjectList = `cons(introducedVariable,empty()); 
        /*
         * we do not use RawAction nor TypedAction here because the generated match should not
         * produce any proof obligation for the verifier
         */
        PatternInstruction generatedPatternInstruction =
          `PatternInstruction(Pattern(generatedSubjectList, cons(pattern,empty()),guardList),newAction, option());        

          // Warning: The options are not good
        Instruction generatedMatch =
          `Match(SubjectList(generatedSubjectList),
                 concPatternInstruction(generatedPatternInstruction),
                 option());
        return generatedMatch;
      }

      manyInstructionList(TypedEqualityCondition[tomType=type,lhs=lhs,rhs=rhs], tail) -> {
        Instruction newAction = `buildCondition(tail,action);

        TomTerm newLhs = preProcessing(`BuildReducedTerm(lhs));
        TomTerm newRhs = preProcessing(`BuildReducedTerm(rhs));
        Expression equality = `EqualTerm(type,newLhs,newRhs);
        Instruction generatedTest = `If(equality,newAction,Nop());
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

      RecordAppl[option=optionList, nameList=nameList, slots=arguments, constraints=constraints] -> {
        SlotList args = `arguments;
        SlotList newArgs = `emptySlotList();
        while(!args.isEmpty()) {
          Slot elt = args.getHead();
          TomTerm newElt = renameVariable(elt.getAppl(),multiplicityMap,equalityCheck);
          newArgs = (SlotList) newArgs.append(`PairSlotAppl(elt.getSlotName(),newElt));
          args = args.getTail();
        }
        ConstraintList newConstraintList = renameVariableInConstraintList(`constraints,multiplicityMap,equalityCheck);
        renamedTerm = `RecordAppl(optionList,nameList,newArgs,newConstraintList);
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
      RecordAppl[nameList=(Name(tomName),_*), slots=arguments] -> {
        TomSymbol tomSymbol = symbolTable().getSymbolFromName(`tomName);
        
        SlotList newArgs = `emptySlotList();
        if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
          SlotList args = `arguments;
          while(!args.isEmpty()) {
            Slot elt = args.getHead();
            TomTerm newElt = elt.getAppl();
            %match(TomTerm newElt) {
              appl@RecordAppl[nameList=(Name(tomName2),_*)] -> {
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
            newArgs = (SlotList) newArgs.append(`PairSlotAppl(elt.getSlotName(),newElt));
            args = args.getTail();
          }
        } else {
          newArgs = mergeTomListWithSlotList(abstractPatternList(slotListToTomList(`arguments),abstractedPattern,introducedVariable),`arguments);
        }
        abstractedTerm = subject.setSlots(newArgs);
      }
    } // end match
    return abstractedTerm;
  }

  private TomList abstractPatternList(TomList subjectList,
                                      ArrayList abstractedPattern,
                                      ArrayList introducedVariable)  {
    %match(TomList subjectList) {
      emptyTomList() -> { return subjectList; }
      manyTomList(head,tail) -> {
        TomTerm newElt = abstractPattern(`head,abstractedPattern,introducedVariable);
        return `manyTomList(newElt,abstractPatternList(tail,abstractedPattern,introducedVariable));
      }
    }
    throw new TomRuntimeException("abstractPatternList: " + subjectList);
  }


  /***********************************/
  /* functions related to the 'when' */
  /***********************************/


  /*
   * attach the when contraint to the right variable
  */
  private TomList attachConstraint(TomList subjectList,
                                   TomTerm constraint) {
    HashSet patternVariable = new HashSet();
    HashSet constraintVariable = new HashSet();

    collectVariable(patternVariable,subjectList);
    collectVariable(constraintVariable,constraint);
    Set variableSet = intersection(patternVariable,constraintVariable);

    //System.out.println("attach constraint "+subjectList+" "+patternVariable+" "+constraint);
    TomList newSubjectList = (TomList) replace_attachConstraint.apply(subjectList,variableSet,constraint); 

    return newSubjectList;
  }

  /*
   * build a set with all the variables in the intersection of two sets
   * used by the when
  */
  private Set intersection(Set patternVariable, Set constraintVariable) {
    Set res = new HashSet();
    for(Iterator it1 = patternVariable.iterator(); it1.hasNext() ; ) {
      TomTerm patternTerm = (TomTerm) it1.next();
      itBlock: {
        for(Iterator it2 = constraintVariable.iterator(); it2.hasNext() ; ) {
          TomTerm constraintTerm = (TomTerm) it2.next();
          %match(TomTerm patternTerm, TomTerm constraintTerm) {
            var@Variable[astName=name], Variable[astName=name] -> {
              res.add(`var);
              //break itBlock;
            }
            var@VariableStar[astName=name], VariableStar[astName=name] -> {
              res.add(`var);
              //break itBlock;
            }
          }
        }
      }
    }
    return res;
  }

  /*
   * find the variable on which we should attach the constraint
   * used by the when
  */
  protected Replace3 replace_attachConstraint = new Replace3() { 
      public ATerm apply(ATerm subject, Object arg1, Object arg2) {
        Set variableSet = (Set) arg1;
        TomTerm constraint = (TomTerm) arg2;

        if(subject instanceof TomTerm) {
          %match(TomTerm subject) {
            var@(Variable|VariableStar)[constraints=constraintList] -> {
              if(variableSet.remove(`var) && variableSet.isEmpty()) {
                ConstraintList newConstraintList = (ConstraintList)`constraintList.append(`Ensure(preProcessing(BuildReducedTerm(constraint))));
                return `var.setConstraints(newConstraintList);
              }
            }

            appl@RecordAppl[constraints=constraintList] -> {
              if(variableSet.isEmpty()) {
                ConstraintList newConstraintList = (ConstraintList)`constraintList.append(`Ensure(preProcessing(BuildReducedTerm(constraint))));
                return `appl.setConstraints(newConstraintList);
              }
            }
          }
        }

        return traversal().genericTraversal(subject,this,variableSet,constraint);
      } // end apply
    }; // end new


} // class TomCompiler
