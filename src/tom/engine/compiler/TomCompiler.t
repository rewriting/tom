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
  
import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;
import jtom.exception.*;
import jtom.adt.*;
import jtom.runtime.*;

public class TomCompiler extends TomBase {
  TomKernelCompiler tomKernelCompiler;
  private String debugKey = null;
  public TomCompiler(jtom.TomEnvironment environment,
                     TomKernelCompiler tomKernelCompiler) {
    super(environment);
    this.tomKernelCompiler = tomKernelCompiler;
  }

// ------------------------------------------------------------
  %include { Tom.signature }
// ------------------------------------------------------------

    /* 
     * preProcessing:
     *
     * transforms RuleSet into Function + Match + MakeTerm
     * abstract list-matching patterns
     * rename non-linear patterns
     */

  private Option option() {
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
      
      RuleSet(ruleList@Cons(
                RewriteRule[lhs=Term(Appl[astName=Name(tomName)])],tail), orgTrack) -> {
        statistics().numberRuleSetsTransformed++;
        if(Flags.debugMode) {
          debugKey = orgTrack.getFileName().getString() + orgTrack.getLine().toString();
        }
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomName name = tomSymbol.getAstName();
        TomList typesList = tomSymbol.getTypesToType().getList();        
        TomList path = empty();
        TomList matchArgumentsList = empty();
        TomList patternActionList  = empty();
        TomTerm variable;
        int index = 0;

        path = append(`RuleVar(),path);
        
        while(!typesList.isEmpty()) {
          TomType subtermType = typesList.getHead().getAstType();
          variable = `Variable(option(),PositionName(append(makeNumber(index),path)),subtermType);
          matchArgumentsList = append(variable,matchArgumentsList);
          typesList = typesList.getTail();
          index++;
        }
        
        while(!ruleList.isEmpty()) {
          TomTerm rule = ruleList.getHead();
          %match(TomTerm rule) {
            RewriteRule(Term(Appl[args=matchPatternsList]),
                        Term(rhsTerm),
                        condList,
                        option) -> {
              
              TomTerm newRhs = preProcessing(`MakeTerm(rhsTerm));
              TomList rhsList = empty();
              if(Flags.supportedBlock) {
                rhsList = appendInstruction(`OpenBlock(),rhsList);
              }
              if(Flags.debugMode) {
                TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.patternSuccess(\""+debugKey+"\");\n");
                rhsList = append(`TargetLanguageToTomTerm(tl), rhsList);
              }
              
              rhsList = appendInstruction(`Return(newRhs),rhsList);
              if(Flags.supportedBlock) {
                rhsList = appendInstruction(`CloseBlock(),rhsList);
              }
              
              TomList newRhsList = buildCondition(condList,rhsList);
             
              patternActionList = append(`PatternAction(TermList(matchPatternsList),Tom(newRhsList), option),patternActionList);
            }
          } 
          ruleList = ruleList.getTail();
        }
        
        TomTerm subjectListAST = `SubjectList(matchArgumentsList);
        TomTerm makeFunctionBeginAST = `MakeFunctionBegin(name,subjectListAST);
        ArrayList optionList = new ArrayList();
        optionList.add(orgTrack);
          //optionList.add(tsf().makeOption_GeneratedMatch());
        Option generatedOptions = ast().makeOption(ast().makeOptionList(optionList));
        TomTerm matchAST = `Match(SubjectList(matchArgumentsList),
                                  PatternList(patternActionList),
                                  generatedOptions);
        Instruction buildAST = `Return(BuildTerm(name,tomListMap(matchArgumentsList,replace_preProcessing_makeTerm)));
        TomList l = empty();
        if(Flags.eCode) {
          l = append(makeFunctionBeginAST,l);
          l = append(`LocalVariable(),l);
          l = append(`EndLocalVariable(),l);
          l = append(matchAST,l);
          l = appendInstruction(buildAST,l);
          l = append(`MakeFunctionEnd(),l);
        } else {
          l = append(makeFunctionBeginAST,l);
          l = append(matchAST,l);
          l = appendInstruction(buildAST,l);
          l = append(`MakeFunctionEnd(),l);
        }
        return preProcessing(`Tom(l));
      }
      
      PatternAction(tl@TermList(termList),tom@Tom(actionList), option) -> {
        return `PatternAction(tl,preProcessing(tom), option);
      }
      
      Match(SubjectList(l1),PatternList(l2), matchOption@Option(list))  -> {
        Option orgTrack = findOriginTracking(list);
        if(Flags.debugMode) {
          debugKey = orgTrack.getFileName().getString() + orgTrack.getLine().toString();
        }
        TomList newPatternList = empty();
        while(!l2.isEmpty()) {
          TomTerm elt = preProcessing(l2.getHead());
          TomTerm newPatternAction = elt;
          
          matchBlock: {
            %match(TomTerm elt) {
              PatternAction(TermList(termList),Tom(actionList), option) -> {
                TomList newTermList = empty();
                TomList newActionList = actionList;

                  // generate equality checks
                ArrayList equalityCheck = new ArrayList();
                TomList renamedTermList = linearizePattern(termList,equalityCheck);
                if(equalityCheck.size() > 0) {
                  Expression cond = `TrueTL();
                  Iterator it = equalityCheck.iterator();
                  while(it.hasNext()) {
                    Expression equality = (Expression)it.next();
                    cond = `And(equality,cond);
                  }
                  TomList elsePart = empty();
                  if(Flags.debugMode) {
                    TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.linearizationFail(\""+debugKey+"\");\n");
                    elsePart   = `cons(InstructionToTomTerm(Action(cons(TargetLanguageToTomTerm(tl), empty()))), empty());
                  }
                    
                  newActionList = cons(`InstructionToTomTerm(IfThenElse(cond,actionList,elsePart)),empty());
                  newPatternAction = `PatternAction(TermList(renamedTermList),Tom(newActionList), option);        
                    //System.out.println("\nnewPatternAction = " + newPatternAction);
                }

                  // abstract patterns
                ArrayList abstractedPattern  = new ArrayList();
                ArrayList introducedVariable = new ArrayList();
                newTermList = abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);
                if(abstractedPattern.size() > 0) {
                    // generate a new match construct

                  TomTerm generatedPatternAction =
                    `PatternAction(TermList(ast().makeList(abstractedPattern)),Tom(newActionList), option);        
                    // We reconstruct only a list of option with orgTrack and GeneratedMatch
                  ArrayList optionList = new ArrayList();
                  optionList.add(orgTrack);
                  optionList.add(tsf().makeOption_GeneratedMatch());
                  Option generatedOptions = ast().makeOption(ast().makeOptionList(optionList));
                  TomTerm generatedMatch =
                    `Match(SubjectList(ast().makeList(introducedVariable)),
                           PatternList(cons(generatedPatternAction,empty())),
                           generatedOptions);
                    //System.out.println("Generate new Match"+generatedMatch);
                  generatedMatch = preProcessing(generatedMatch);
                  newPatternAction =
                    `PatternAction(TermList(newTermList),Tom(cons(generatedMatch,empty())), option());

                    //System.out.println("newPatternAction = " + newPatternAction);
                }
                  // do nothing
                break matchBlock;
              }
              
              _ -> {
                System.out.println("preProcessing: strange PatternAction: " + elt);
                System.out.println("termList = " + elt.getTermList());
                System.out.println("tom      = " + elt.getTom()); 
                System.exit(1);
              }
            }
          } // end matchBlock

          newPatternList = append(newPatternAction,newPatternList);
          l2 = l2.getTail();
        }

        TomTerm newMatch = `Match(SubjectList(l1),
                                  PatternList(newPatternList),
                                  matchOption);
        return newMatch;
      }
      
        // default rule
      t -> {
        return tomKernelCompiler.preProcessing(t);
      }
    }
  }

  private TomList buildCondition(TomList condList, TomList actionList) {
    %match(TomList condList) {
      Empty() -> { return actionList; }
        
      Cons(MatchingCondition[lhs=pattern,rhs=subject], tail) -> {
        System.out.println(pattern);
        TomType subjectType = getTermType(pattern);
        TomList path = empty();
        path = append(`RuleVar(),path);
        TomTerm newSubject = preProcessing(`MakeTerm(subject));
    
          //TomTerm introducedVariable = `Variable(option(),PositionName(path),subjectType);
        TomTerm introducedVariable = newSubject;
        
          // introducedVariable = subject
          // Declare and Assign 

        TomList newActionList = buildCondition(tail,actionList);

        TomTerm generatedPatternAction =
          `PatternAction(TermList(cons(pattern,empty())),Tom(newActionList), option());        

          // Warning: The options are not good
        TomTerm generatedMatch =
          `Match(SubjectList(cons(introducedVariable,empty())),
                 PatternList(cons(generatedPatternAction,empty())),
                 option());
    
    
          //System.out.println("buildCondition: generatedMatch =\n\t" + generatedMatch);
        TomList conditionList = cons(generatedMatch,empty());
    
        return conditionList;

      }

      Cons(EqualityCondition[lhs=lhs,rhs=rhs], tail) -> {
        TomTerm newLhs = preProcessing(`MakeTerm(lhs));
        TomTerm newRhs = preProcessing(`MakeTerm(rhs));

        Expression equality = `EqualTerm(newLhs,newRhs);
        TomList newActionList = buildCondition(tail,actionList);
        TomTerm generatedTest = `InstructionToTomTerm(IfThenElse(equality,newActionList,empty()));
        TomList conditionList = cons(generatedTest,empty());
        return conditionList;
      }
      
      _ -> {
        System.out.println("buildCondition strange term: " + condList);
        System.exit(1);
        return null;
      }
        
    }
  }
  
  private TomTerm renameVariable(TomTerm subject,
                                 HashMap multiplicityMap,
                                 ArrayList equalityCheck) {
    TomTerm renamedTerm = subject;
    
    %match(TomTerm subject) {
      Variable[astName=name,astType=type] -> {
        Integer multiplicity = (Integer)multiplicityMap.get(name);
        int mult = multiplicity.intValue();
        if(mult > 1) {
          mult = mult-1;
          multiplicityMap.put(name,new Integer(mult));
          
          TomList path = empty();
          path = append(`RenamedVar(name),path);
          path = append(makeNumber(mult),path);
          renamedTerm = `Variable(option(),PositionName(path),type);

            //System.out.println("renamedTerm = " + renamedTerm);

          Expression newEquality = `EqualTerm(subject,renamedTerm);
          equalityCheck.add(newEquality);
        }
        return renamedTerm;
      }

      VariableStar[astName=name,astType=type] -> {
        Integer multiplicity = (Integer)multiplicityMap.get(name);
        int mult = multiplicity.intValue();
        if(mult > 1) {
          mult = mult-1;
          multiplicityMap.put(name,new Integer(mult));
          
          TomList path = empty();
          path = append(`RenamedVar(name),path);
          path = append(makeNumber(mult),path);
          renamedTerm = `VariableStar(option(),PositionName(path),type);

            //System.out.println("renamedTerm = " + renamedTerm);

          Expression newEquality = `EqualTerm(subject,renamedTerm);
          equalityCheck.add(newEquality);
        }
        return renamedTerm;
      }
      
      Appl[option=Option(optionList), astName=name, args=args] -> {
        TomList newArgs = empty();
        while(!args.isEmpty()) {
          TomTerm elt = args.getHead();
          TomTerm newElt = renameVariable(elt,multiplicityMap,equalityCheck);
          newArgs = append(newElt,newArgs);
          args = args.getTail();
        }

        ArrayList list = new ArrayList();
        while(!optionList.isEmptyOptionList()) {
          Option optElt = optionList.getHead();
          Option newOptElt = optElt;
          %match(Option optElt) {
            TomTermToOption(var@Variable[]) -> {
              newOptElt = `TomTermToOption(renameVariable(var,multiplicityMap,equalityCheck));
            }
          }
          list.add(newOptElt);
          optionList = optionList.getTail();
        }
        OptionList newOptionList = ast().makeOptionList(list);
        renamedTerm = `Appl(Option(newOptionList),name,newArgs);
        return renamedTerm;
      }
    }
    return renamedTerm;
  }

  private TomList linearizePattern(TomList subject, ArrayList equalityCheck) {

      // collect variables
    ArrayList variableList = new ArrayList();
    collectVariable(variableList,`PatternList(subject));

      // compute multiplicities
    HashMap multiplicityMap = new HashMap();
    Iterator it = variableList.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
      if(multiplicityMap.containsKey(name)) {
        Integer value = (Integer)multiplicityMap.get(name);
        multiplicityMap.put(name, new Integer(1+value.intValue()));
      } else {
        multiplicityMap.put(name, new Integer(1));
      }
    }

      // perform the renaming and generate equality checks
    TomList newList = empty();
    while(!subject.isEmpty()) {
      TomTerm elt = subject.getHead();
      TomTerm newElt = renameVariable(elt,multiplicityMap,equalityCheck);

        // System.out.println("\nelt = " + elt + "\n --> newELt = " + newElt);
    
      newList = append(newElt,newList);
      subject = subject.getTail();
    }
    return newList;
  }

  private static int absVarNumber = 0;
  private TomTerm abstractPattern(TomTerm subject,
                                  ArrayList abstractedPattern,
                                  ArrayList introducedVariable) {
    TomTerm abstractedTerm = subject;
    %match(TomTerm subject) {
      Appl[option=option, astName=name@Name(tomName), args=args] -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        
        TomList newArgs = empty();
        if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
          while(!args.isEmpty()) {
            TomTerm elt = args.getHead();
            TomTerm newElt = elt;
            %match(TomTerm elt) {
              appl@Appl[astName=Name(tomName2)] -> {
                TomSymbol tomSymbol2 = symbolTable().getSymbol(tomName2);
                TomType type2 = tomSymbol2.getTypesToType().getCodomain();
                  //System.out.println("Abstract: " + appl);
                abstractedPattern.add(appl);

                TomList path = empty();
                  //path = append(`AbsVar(makeNumber(introducedVariable.size())),path);
                absVarNumber++;
                path = append(`AbsVar(makeNumber(absVarNumber)),path);
                  
                TomTerm newVariable = `Variable(option(),PositionName(path),type2);

                  //System.out.println("newVariable = " + newVariable);
                                
                introducedVariable.add(newVariable);
                newElt = newVariable;
              }
            }
            newArgs = append(newElt,newArgs);
            args = args.getTail();
          }
        } else {
          newArgs = abstractPatternList(args,abstractedPattern,introducedVariable);
        }
        abstractedTerm = `Appl(option,name,newArgs);
      }
    } // end match
    return abstractedTerm;
  }

  private TomList abstractPatternList(TomList subjectList,
                                      ArrayList abstractedPattern,
                                      ArrayList introducedVariable) {
    TomList newList = empty();
    while(!subjectList.isEmpty()) {
      TomTerm elt = subjectList.getHead();
      TomTerm newElt = abstractPattern(elt,abstractedPattern,introducedVariable);
      newList = append(newElt,newList);
      subjectList = subjectList.getTail();
    }
    return newList;
  }
  
} // end of class
  
                  
    
