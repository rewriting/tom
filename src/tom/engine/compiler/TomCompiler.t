/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003 INRIA
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
import java.util.HashMap;
import java.util.Iterator;

import jtom.adt.tomsignature.types.*;

import jtom.runtime.Replace1;
import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import jtom.tools.Tools;
import aterm.*;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public class TomCompiler extends TomTask {
  TomKernelCompiler tomKernelCompiler;
  private String debugKey = null;
  private boolean supportedBlock = false, debugMode = false, eCode = false;
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
		supportedBlock = getInput().isSupportedBlock();
		debugMode = getInput().isDebugMode();
		eCode = getInput().isECode();
	}
	
  public void process() {
    try {
      long startChrono = 0;
      boolean verbose = getInput().isVerbose(), intermediate = getInput().isIntermediate();
      if(verbose) { startChrono = System.currentTimeMillis();}
      
      TomTerm preCompiledTerm = preProcessing(getInput().getTerm());
      TomTerm compiledTerm = tomKernelCompiler.compileMatching(preCompiledTerm);
      compiledTerm = tomKernelCompiler.postProcessing(compiledTerm);
      
      if(verbose) {
        System.out.println("TOM compilation phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
      }
      if(intermediate) {
          Tools.generateOutput(getInput().getBaseInputFileName() + TomTaskInput.compiledSuffix, compiledTerm);
      }
			getInput().setTerm(compiledTerm);
      
    } catch (Exception e) {
    	addError("Exception occurs in TomCompiler: "+e.getMessage(), getInput().getInputFileName(), 0, 0);
      e.printStackTrace();
      return;
    }
  }
  
    /* 
     * preProcessing:
     *
     * transforms RuleSet into Function + Match + MakeTerm
     * abstract list-matching patterns
     * rename non-linear patterns
     */

  private OptionList option() {
    return ast().makeOption();
  }

  Replace1 replace_preProcessing = new Replace1() {
      public ATerm apply(ATerm t)  { 
      	return preProcessing((TomTerm)t);
      }
    };
  
  Replace1 replace_preProcessing_makeTerm = new Replace1() {
      public ATerm apply(ATerm t) {
        TomTerm subject = (TomTerm)t;
        return preProcessing(`MakeTerm(subject));
      }
    }; 

  private TomTerm preProcessing(TomTerm subject) {
      //%variable
      //System.out.println("preProcessing subject: " + subject);

    %match(TomTerm subject) {
      Tom(l) -> {
        return `Tom(tomListMap(l,replace_preProcessing));
      }
      
      RuleSet(ruleList@manyTomRuleList(
                RewriteRule[lhs=Term(Appl[nameList=(Name(tomName))])],tail), orgTrack) -> {
        if(debugMode) {
          debugKey = orgTrack.getFileName().getString() + orgTrack.getLine();
        }
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
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
          variable = `Variable(option(),PositionName(appendNumber(index,path)),subtermType);
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
              TomList rhsList = empty();
              if(supportedBlock) {
                rhsList = appendInstruction(`OpenBlock(),rhsList);
              }
              if(debugMode) {
                TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.patternSuccess(\""+debugKey+"\");\n");
                rhsList = append(`TargetLanguageToTomTerm(tl), rhsList);
              }
              
              rhsList = appendInstruction(`Return(newRhs),rhsList);
              if(supportedBlock) {
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
        OptionList generatedOptions = ast().makeOptionList(optionList);
        TomTerm matchAST = `Match(SubjectList(matchArgumentsList),
                                  PatternList(patternActionList),
                                  generatedOptions);
        Instruction buildAST = `Return(BuildTerm(name,tomListMap(matchArgumentsList,replace_preProcessing_makeTerm)));
        TomList l = empty();
        if(eCode) {
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
      
      PatternAction(tl,tom, option) -> {
        return `PatternAction(tl,preProcessing(tom), option);
      }
      
      Match(SubjectList(l1),PatternList(l2), matchOptionList)  -> {
        Option orgTrack = findOriginTracking(matchOptionList);
        if(debugMode) {
          debugKey = orgTrack.getFileName().getString() + orgTrack.getLine();
        }
        TomList newPatternList = empty();
        while(!l2.isEmpty()) {
          TomTerm elt = preProcessing(l2.getHead());
          TomTerm newPatternAction = elt;
          
          matchBlock: {
            %match(TomTerm elt) {
              PatternAction(TermList(termList),Tom(actionList), option) -> {
                TomList newTermList = empty();
                TomList newActionList = tomListMap(actionList,replace_preProcessing);
                  /* generate equality checks */
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
                  if(debugMode) {
                    TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.linearizationFail(\""+debugKey+"\");\n");
                    elsePart   = `cons(InstructionToTomTerm(Action(cons(TargetLanguageToTomTerm(tl), empty()))), empty());
                  }
                    
                  newActionList = cons(`InstructionToTomTerm(IfThenElse(cond,actionList,elsePart)),empty());
                  newPatternAction = `PatternAction(TermList(renamedTermList),Tom(newActionList), option);        
                    /*System.out.println("\nnewPatternAction = " + newPatternAction);*/
                }

                  /* abstract patterns */
                ArrayList abstractedPattern  = new ArrayList();
                ArrayList introducedVariable = new ArrayList();
                newTermList = abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);
                if(abstractedPattern.size() > 0) {
                    /* generate a new match construct */

                  TomTerm generatedPatternAction =
                    `PatternAction(TermList(ast().makeList(abstractedPattern)),Tom(newActionList), concOption());        
                    /* We reconstruct only a list of option with orgTrack and GeneratedMatch*/
                  OptionList generatedMatchOptionList = `concOption(orgTrack,GeneratedMatch());
                  TomTerm generatedMatch =
                    `Match(SubjectList(ast().makeList(introducedVariable)),
                           PatternList(cons(generatedPatternAction,empty())),
                           generatedMatchOptionList);
                    /*System.out.println("Generate new Match"+generatedMatch); */
                  generatedMatch = preProcessing(generatedMatch);
                  newPatternAction =
                    `PatternAction(TermList(newTermList),Tom(cons(generatedMatch,empty())), option);

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
          l2 = l2.getTail();
        }

        TomTerm newMatch = `Match(SubjectList(l1),
                                  PatternList(newPatternList),
                                  matchOptionList);
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
      emptyTomList() -> { return actionList; }
        
      manyTomList(MatchingCondition[lhs=pattern,rhs=subject], tail) -> {
          //System.out.println("pattern: " + pattern);
        TomType subjectType = getTermType(pattern);
        TomNumberList path = tsf().makeTomNumberList();
        path = (TomNumberList) path.append(`RuleVar());
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

      manyTomList(EqualityCondition[lhs=lhs,rhs=rhs], tail) -> {
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
        throw new TomRuntimeException(new Throwable("buildCondition strange term: " + condList));
      }
        
    }
  }
  
  private TomTerm renameVariable(TomTerm subject,
                                 HashMap multiplicityMap,
                                 ArrayList equalityCheck) {
    TomTerm renamedTerm = subject;
    
    %match(TomTerm subject) {
      UnamedVariable[option=optionList,astType=type] -> {
        OptionList newOptionList = renameVariableInOptionList(optionList,multiplicityMap,equalityCheck);
        return `UnamedVariable(newOptionList,type);
      }
      
      UnamedVariableStar[option=optionList,astType=type] -> {
        OptionList newOptionList = renameVariableInOptionList(optionList,multiplicityMap,equalityCheck);
        return `UnamedVariableStar(newOptionList,type);
      }

      Variable[option=optionList,astName=name,astType=type] -> {
        Integer multiplicity = (Integer) multiplicityMap.get(name);
        int mult = multiplicity.intValue();
        if(mult > 1) {
          mult = mult-1;
          multiplicityMap.put(name,new Integer(mult));
          
          TomNumberList path = tsf().makeTomNumberList();
          path = (TomNumberList) path.append(`RenamedVar(name));
          path = (TomNumberList) path.append(makeNumber(mult));
          OptionList newOptionList = renameVariableInOptionList(optionList,multiplicityMap,equalityCheck);
          renamedTerm = `Variable(newOptionList,PositionName(path),type);
            //System.out.println("renamedTerm = " + renamedTerm);

          Expression newEquality = `EqualTerm(subject,renamedTerm);
          equalityCheck.add(newEquality);
        }
        return renamedTerm;
      }

      VariableStar[option=optionList,astName=name,astType=type] -> {
        Integer multiplicity = (Integer)multiplicityMap.get(name);
        int mult = multiplicity.intValue();
        if(mult > 1) {
          mult = mult-1;
          multiplicityMap.put(name,new Integer(mult));
          
          TomNumberList path = tsf().makeTomNumberList();
          path = (TomNumberList) path.append(`RenamedVar(name));
          path = appendNumber(mult,path);
          OptionList newOptionList = renameVariableInOptionList(optionList,multiplicityMap,equalityCheck);
          renamedTerm = `VariableStar(newOptionList,PositionName(path),type);

            //System.out.println("renamedTerm = " + renamedTerm);

          Expression newEquality = `EqualTerm(subject,renamedTerm);
          equalityCheck.add(newEquality);
        }
        return renamedTerm;
      }
      
      Appl[option=optionList, nameList=nameList, args=args] -> {
        TomList newArgs = empty();
        while(!args.isEmpty()) {
          TomTerm elt = args.getHead();
          TomTerm newElt = renameVariable(elt,multiplicityMap,equalityCheck);
          newArgs = append(newElt,newArgs);
          args = args.getTail();
        }
        OptionList newOptionList = renameVariableInOptionList(optionList,multiplicityMap,equalityCheck);
        renamedTerm = `Appl(newOptionList,nameList,newArgs);
        return renamedTerm;
      }
    }
    return renamedTerm;
  }

  private OptionList renameVariableInOptionList(OptionList optionList,
                                                HashMap multiplicityMap,
                                                ArrayList equalityCheck) {
    ArrayList list = new ArrayList();
    while(!optionList.isEmpty()) {
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
    return ast().makeOptionList(list);
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
  
  
  private TomTerm abstractPattern(TomTerm subject,
                                  ArrayList abstractedPattern,
                                  ArrayList introducedVariable)  {
    TomTerm abstractedTerm = subject;
    %match(TomTerm subject) {
      Appl[option=option, nameList=nameList@(Name(tomName),_*), args=args] -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        
        TomList newArgs = empty();
        if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
          while(!args.isEmpty()) {
            TomTerm elt = args.getHead();
            TomTerm newElt = elt;
            %match(TomTerm elt) {
              appl@Appl[nameList=(Name(tomName2),_*)] -> {
                  //System.out.println("Abstract: " + appl);
                TomSymbol tomSymbol2 = symbolTable().getSymbol(tomName2);
                TomType type2 = tomSymbol2.getTypesToType().getCodomain();
                abstractedPattern.add(appl);

                TomNumberList path = tsf().makeTomNumberList();
                  //path = append(`AbsVar(makeNumber(introducedVariable.size())),path);
                absVarNumber++;
                path = (TomNumberList) path.append(`AbsVar(makeNumber(absVarNumber)));
                  
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
        abstractedTerm = `Appl(option,nameList,newArgs);
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
