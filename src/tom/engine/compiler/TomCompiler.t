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

public class TomCompiler extends TomBase {

  public TomCompiler(jtom.TomEnvironment environment) {
    super(environment);
  }

// ------------------------------------------------------------
  %include { Tom.signature }
// ------------------------------------------------------------

    /* 
     * pass2_1:
     *
     * replaces MakeTerm
     * transforms RuleSet into Function + Match + MakeTerm
     * abstract list-matching patterns
     * rename non-linear patterns
     */

  private static int matchNumber = 0;

  private Option option() {
    return ast().makeOption();
  }
  
  public TomTerm pass2_1(TomTerm subject) throws TomException {
      //%variable
      //System.out.println("pass2_1 subject: " + subject);
    Replace replace_pass2_1 = new Replace() {
        public ATerm apply(ATerm t) throws TomException { return pass2_1((TomTerm)t); }
      }; 
    Replace replace_pass2_1_makeTerm = new Replace() {
        public ATerm apply(ATerm t) throws TomException {
          TomTerm subject = (TomTerm)t;
          return pass2_1(`MakeTerm(subject));
        }
      }; 

    %match(TomTerm subject) {
      Tom(l) -> {
        return `Tom(tomListMap(l,replace_pass2_1));
      }
       
      MakeTerm(Variable[astName=name]) -> {
        statistics().numberMakeTermReplaced++;
        return `BuildVariable(name);
      }
      
      MakeTerm(VariableStar[astName=name]) -> {
        statistics().numberMakeTermReplaced++;
        return `BuildVariableStar(name);
      }

      MakeTerm(Appl(Option(optionList),name@Name(tomName),termArgs)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        statistics().numberMakeTermReplaced++;
        TomList newTermArgs = tomListMap(termArgs,replace_pass2_1_makeTerm);

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

      RuleSet(_, ruleList@Cons(
                RewriteRule[lhs=Term(Appl[astName=Name(tomName)])],tail)) -> {

        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomName name = tomSymbol.getAstName();
        TomList typesList = tomSymbol.getTypesToType().getList();
        
	statistics().numberRuleSetsTransformed++;

          //System.out.println("*** RuleSet");
        
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
            RewriteRule[lhs=Term(Appl[args=matchPatternsList]), rhs=Term(rhsTerm)] -> {
              
              TomTerm newRhs = pass2_1(`MakeTerm(rhsTerm));
              TomList rhsList = empty();
              if(Flags.supportedBlock) {
                rhsList = appendInstruction(`OpenBlock(),rhsList);
              }
              rhsList = appendInstruction(`Return(newRhs),rhsList);
              if(Flags.supportedBlock) {
                rhsList = appendInstruction(`CloseBlock(),rhsList);
              }
              patternActionList = append(`PatternAction(TermList(matchPatternsList),Tom(rhsList)),patternActionList);
            }
          } 
          ruleList = ruleList.getTail();
        }
        
        TomTerm subjectListAST = `SubjectList(matchArgumentsList);
        TomTerm makeFunctionBeginAST = `MakeFunctionBegin(name,subjectListAST);
        TomTerm matchAST = `Match(option(),
                                  SubjectList(matchArgumentsList),
                                  PatternList(patternActionList));
        Instruction buildAST = `Return(BuildTerm(name,tomListMap(matchArgumentsList,replace_pass2_1_makeTerm)));
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
        return pass2_1(`Tom(l));
      }
      
      PatternAction(tl@TermList(termList),tom@Tom(actionList)) -> {
        TomTerm newPatternAction = `PatternAction(tl,pass2_1(tom));
        return newPatternAction;
      }
      
      Match(option,SubjectList(l1),PatternList(l2)) -> {
        TomList newPatternList = empty();
        while(!l2.isEmpty()) {
          TomTerm elt = pass2_1(l2.getHead());
          TomTerm newPatternAction = elt;
          
          matchBlock: {
            %match(TomTerm elt) {
              PatternAction(TermList(termList),Tom(actionList)) -> {
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
                  newActionList = cons(`InstructionToTomTerm(IfThenElse(cond,actionList,empty())),empty());
                  newPatternAction = `PatternAction(TermList(renamedTermList),Tom(newActionList));        
                    //System.out.println("\nnewPatternAction = " + newPatternAction);
                }

                  // abstract patterns
                ArrayList abstractedPattern  = new ArrayList();
                ArrayList introducedVariable = new ArrayList();
                newTermList = abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);
                if(abstractedPattern.size() > 0) {
                    // generate a new match construct
                  TomTerm generatedPatternAction =
                    `PatternAction(TermList(ast().makeList(abstractedPattern)),Tom(newActionList));        
                  
                  TomTerm generatedMatch =
                    `Match(option(),
                           SubjectList(ast().makeList(introducedVariable)),
                           PatternList(cons(generatedPatternAction,empty())));
                  
                  generatedMatch = pass2_1(generatedMatch);
                  newPatternAction =
                    `PatternAction(TermList(newTermList),Tom(cons(generatedMatch,empty())));

                    //System.out.println("newPatternAction = " + newPatternAction);
                }
                  // do nothing
                break matchBlock;
              }
              
              _ -> {
                System.out.println("pass2_1: strange PatternAction: " + elt);
                System.exit(1);
              }
            }
          } // end matchBlock

          newPatternList = append(newPatternAction,newPatternList);
          l2 = l2.getTail();
        }

        TomTerm newMatch = `Match(option,
                                  SubjectList(l1),
                                  PatternList(newPatternList));
        return newMatch;
      }
      
        // default rule
      t -> {
          //System.out.println("pass2_1 default: " + t);
        return t;
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

  private TomList linearizePattern(TomList subject, ArrayList equalityCheck) throws TomException {

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
                path = append(`AbsVar(makeNumber(introducedVariable.size())),path);
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
  
    /* 
     * pass2_2:
     *
     * compiles Match into and automaton
     */
 
  public TomTerm pass2_2(TomTerm subject) throws TomException {
      //%variable
    Replace replace_pass2_2 = new Replace() {
        public ATerm apply(ATerm t) throws TomException { return pass2_2((TomTerm)t); }
      }; 

    %match(TomTerm subject) {
      Tom(l) -> {
        return `Tom(tomListMap(l,replace_pass2_2));
      }
      
      Match(optionMatch,SubjectList(l1),PatternList(l2)) -> {
        statistics().numberMatchCompiledIntoAutomaton++;

        TomList termList, actionList;
        TomList automataList = empty();
        ArrayList list;
        TomList path = empty();

        matchNumber++;
        path = append(`MatchNumber(makeNumber(matchNumber)),path);
        
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
          %match(TomTerm tlVariable) { 
            Variable(option,_,variableType) -> {
              TomTerm variable = `Variable(option,PositionName(append(makeNumber(index),path)),variableType);
              matchDeclarationList = append(`Declaration(variable),matchDeclarationList);
              matchAssignementList = appendInstruction(`Assign(variable,TomTermToExpression(tlVariable)),matchAssignementList);
            }
          } 
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
        while(!l2.isEmpty()) {
          actionNumber++;
          TomTerm pa = l2.getHead();
          termList = pa.getTermList().getList();
          actionList = pa.getTom().getList();

            //System.out.println("termList   = " + termList);
            //System.out.println("actionList = " + actionList);
          if(termList==null || actionList==null) {
            System.out.println("TomCompiler: null value");
            System.exit(1);
          }

            // compile nested match constructs
          actionList = tomListMap(actionList,replace_pass2_2);
              
            //System.out.println("termList      = " + termList);
            //System.out.println("actionNumber  = " + actionNumber);
            //System.out.println("action        = " + actionList);
          TomList patternsDeclarationList = empty();
          Collection variableCollection = new HashSet();
          collectVariable(variableCollection,`Tom(termList));
          
          Iterator it = variableCollection.iterator();
          while(it.hasNext()) {
            TomTerm tmpsubject = (TomTerm)it.next();
            patternsDeclarationList = append(`Declaration(tmpsubject),patternsDeclarationList);
              //System.out.println("*** " + patternsDeclarationList);
          }

          TomList numberList = append(`PatternNumber(makeNumber(actionNumber)),path);
          TomList instructionList;
          instructionList = genTermListMatchingAutomata(termList,path,1,actionList,true);
            //firstCall = false;
          TomList declarationInstructionList; 
          declarationInstructionList = concat(patternsDeclarationList,instructionList);
          TomTerm automata = `Automata(numberList,declarationInstructionList);
            //System.out.println("automata = " + automata);
    
          automataList = append(automata,automataList);
          l2 = l2.getTail();
        }

          /*
           * return the compiled MATCH construction
           */

        TomList astAutomataList = automataListPass2_2List(automataList);
        return `CompiledMatch(matchDeclarationList,astAutomataList);
      }

        // default rule
      t -> {
          //System.out.println("default: " + t);
        return t;
      }
    }
  }

  private TomList automataListPass2_2List(TomList automataList) {
      //%variable
    
    %match(TomList automataList) {
        //conc()      -> { return empty(); }
        //conc(Automata(numberList,instList),l*)  -> {
      Empty()      -> { return empty(); }
      Cons(Automata(numberList,instList),l)  -> {
        TomList newList = automataListPass2_2List(l);
        
        if(Flags.supportedGoto) {
          return cons(`InstructionToTomTerm(NamedBlock(getBlockName(numberList), instList)), newList);
        } else {
          TomList result = empty();
          TomTerm variableAST = getBlockVariable(numberList);
          result = append(`Declaration(variableAST),result);
          result = appendInstruction(`Assign(variableAST, TrueTL()),result);
          if(Flags.supportedBlock) { // Test
            result = appendInstruction(`OpenBlock(),result);
          }
          result = concat(result,instList);
          if(Flags.supportedBlock) { // Test
            result = appendInstruction(`CloseBlock(),result);
          }
          result = concat(result,newList);
          return result;
        }
      }
    }
    return null;
  }

    /* 
     * pass3: passCompiledTermTransformation
     *
     * transform a compiledTerm
     * 2 phases:
     *   - collection of Declaration
     *   - replace LocalVariable and remove Declaration
     */

  public TomTerm pass3(TomTerm subject) throws TomException {
    TomTerm res;
    ArrayList list = new ArrayList();
    traversalCollectDeclaration(list,subject);
      //System.out.println("list size = " + list.size());
    res = traversalReplaceLocalVariable(list,subject);
    return res;
  }
    
  private TomTerm traversalCollectDeclaration(ArrayList list, TomTerm subject) throws TomException{
      //%variable
    %match(TomTerm subject) {
      Tom(l) -> {
        return `Tom(traversalCollectDeclarationList(list, l));
      } 
      
      LocalVariable -> {
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
    

  private TomList traversalCollectDeclarationList(ArrayList list,TomList subject) throws TomException {
      //%variable
    if(subject.isEmpty()) {
      return subject;
    }
    TomTerm t = subject.getHead();
    TomList l = subject.getTail();
    return cons(traversalCollectDeclaration(list,t),
                traversalCollectDeclarationList(list,l));
  }

  private boolean removeDeclaration = false;
  private TomTerm traversalReplaceLocalVariable(ArrayList list, TomTerm subject) {
      //%variable
    %match(TomTerm subject) {
      Tom(l) -> {
        return `Tom(traversalReplaceLocalVariableList(list, l));
      } 
      
      LocalVariable -> {
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
  


  public void collectDeclaration(final Collection collection, TomTerm subject) throws TomException {
    Collect collect = new Collect() { 
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
    
    genericCollect(subject, collect); 
  } 

    // ------------------------------------------------------------
  
  public TomTerm removeDeclaration(TomTerm subject) {
    TomTerm res = subject;
      //System.out.println("*** removeDeclaration");
                  
    Replace replace = new Replace() { 
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
              return (TomTerm) genericTraversal(other,this);
            }
          }
        } 
      }; // end new
    
      //return genericReplace(subject, replace);
    try {
      res = (TomTerm) replace.apply(subject);
    } catch(Exception e) {
      System.out.println("removeDeclaration: error");
      System.exit(0);
    }
    return res;
  } 

  private String getBlockName(TomList numberList) {
    String name = "matchlab" + numberListToIdentifier(numberList);
    return name;
  }

  private TomTerm getBlockVariable(TomList numberList) {
    String name = "matchlab" + numberListToIdentifier(numberList);
    return `Variable(option(),Name(name),getBoolType());
  }
  
    /*
     * ------------------------------------------------------------
     * Generate a matching automaton
     * ------------------------------------------------------------
     */

  TomList genTermListMatchingAutomata(TomList termList,
                                      TomList path,
                                      int indexTerm,
                                      TomList actionList,
                                      boolean gsa) {
    TomList result = empty();
      //%variable
    if(termList.isEmpty()) {
      if(gsa) {
          // insert the semantic action
        Instruction action = `Action( actionList);
        result = appendInstruction(action,result);
      }
    } else {
      TomTerm head = termList.getHead();
      TomList tail = termList.getTail();
      TomList newSubActionList = genTermListMatchingAutomata(tail,path,indexTerm+1,actionList,gsa);
      TomList newPath          = append(makeNumber(indexTerm),path);
      TomList newActionList    = genTermMatchingAutomata(head,newPath,newSubActionList,gsa);
      result                   = concat(result,newActionList);
    }
    return result;
  }

  TomList genTermMatchingAutomata(TomTerm term,
                                  TomList path,
                                  TomList actionList,
                                  boolean gsa) {
    TomList result = empty();
      //%variable
    matchBlock: {
      %match(TomTerm term) {
        var@Variable(Option(optionList), _, termType) -> {
          TomTerm annotedVariable = getAnnotedVariable(optionList);
          Instruction assignement = `Assign(var,TomTermToExpression(Variable(option(),PositionName(path),termType)));
          result = appendInstruction(assignement,result);

          if(annotedVariable != null) {
            assignement = `Assign(annotedVariable,TomTermToExpression(var)); 
            result = appendInstruction(assignement,result);
          }
          if(gsa) {
            result = appendInstruction(`Action(actionList),result);
          }
          
          break matchBlock;
        }

        UnamedVariable(Option(optionList), termType) -> {
          TomTerm annotedVariable = getAnnotedVariable(optionList);
          if(annotedVariable != null) {
            Instruction assignement = `Assign(annotedVariable,TomTermToExpression(Variable(option(),PositionName(path),termType)));
            result = appendInstruction(assignement,result);
          }
          if(gsa) {
            result = appendInstruction(`Action(actionList),result);
          }

          break matchBlock;
        }

        Appl(Option(optionList),Name(tomName),termArgs) -> {
          TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
          TomName termNameAST = tomSymbol.getAstName();
          TomList termTypeList = tomSymbol.getTypesToType().getList();
          TomType termType = tomSymbol.getTypesToType().getCodomain();
          OptionList termOptionList = tomSymbol.getOption().getOptionList();
          
          TomTerm annotedVariable = getAnnotedVariable(optionList);

            // SUCCES
          TomList declarationList = empty();
          TomList assignementList = empty();
          TomList annotedAssignementList = empty();
          
          int indexSubterm = 0;
          TomTerm subjectVariableAST =  `Variable(option(),PositionName(path),termType); 
          while(!termTypeList.isEmpty()) {
            TomType subtermType = termTypeList.getHead().getAstType();
            TomList newPath = append(makeNumber(indexSubterm+1),path);
            TomTerm newVariableAST = `Variable(option(),PositionName(newPath),subtermType);
            TomTerm declaration    = `Declaration(newVariableAST);
            declarationList      = append(declaration,declarationList);
            
            Expression getSubtermAST;

            TomName slotName = getSlotName(tomSymbol, indexSubterm);
            if(slotName == null) {
              getSubtermAST = `GetSubterm(subjectVariableAST,makeNumber(indexSubterm));
            } else {
              getSubtermAST = `GetSlot(termNameAST,slotName.getString(),subjectVariableAST);
            }
            
            Instruction assignement  = `Assign(newVariableAST,getSubtermAST);
            assignementList      = appendInstruction(assignement,assignementList);
            
            indexSubterm++;
            termTypeList = termTypeList.getTail();
          }
          
            // generate an assignement for annoted variables
          if(annotedVariable != null) {
            Instruction assignement = `Assign(annotedVariable,TomTermToExpression(subjectVariableAST));
            annotedAssignementList   = appendInstruction(assignement,annotedAssignementList);
          }
          
          TomList automataList  = null;
          TomList succesList    = empty();
          TomList failureList   = empty();

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
            automataList = genTermListMatchingAutomata(termArgs,path,1,actionList,gsa);

            succesList = concat(succesList,declarationList);
            succesList = concat(succesList,assignementList);
          }
          succesList = concat(succesList,annotedAssignementList);
          succesList = concat(succesList,automataList);
          
          Expression cond = `EqualFunctionSymbol(subjectVariableAST,term);
          Instruction test = `IfThenElse(cond, succesList,failureList);
          result = appendInstruction(test,result);
          
          break matchBlock;
        }
        
        _ -> {
          System.out.println("GenTermMatchingAutomata strange term: " + term);
          System.exit(1);
        }
      }
    } // end matchBlock 

      //System.out.println("*** result = " + result);
    return result;
  }
  
  TomList genListMatchingAutomata(TomSymbol symbol,
                                  TomList termList,
                                  TomList oldPath,
                                  TomList actionList,
                                  boolean generateSemanticAction,
                                  TomTerm subjectListName,
                                  int indexTerm) {
    TomTerm term;
    TomList result = empty();
      //%variable

//     if(termList.isEmpty() && indexTerm >1) {
//       return result;
//     }
    
    TomTerm variableListAST = null;
    if(indexTerm > 1) {
      variableListAST = subjectListName;
    } else {
      TomList pathList = append(`ListNumber(makeNumber(indexTerm)),oldPath);
      
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

          //conc() -> {
        Empty() -> {
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
        
          //conc(var:Variable(option,_, termType),termTail*) -> {
        Cons(var@Variable(option,_, termType),termTail) -> {
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * if(!IS_EMPTY_TomList(subjectList)) {
               *   TomTerm x_j = (TomTerm) GET_HEAD_TomList(subjectList);
               *   subjectList =  (TomList) GET_TAIL_TomList(subjectList);
               *   if(IS_EMPTY_TomList(subjectList)) {
               *     ...
               *   }
               * }
               */
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = `Variable(option(),PositionName(path),termType);
            
            TomList declarationList = empty();
            TomList assignementList = empty();
            assignementList = appendInstruction(`Assign(var,GetHead(subjectListName)),assignementList);
            assignementList = appendInstruction(`Assign(subjectListName,GetTail(subjectListName)),assignementList);
            
            if(generateSemanticAction) {
              subList = appendInstruction(`Action(actionList),subList);
            }
            
            Expression cond = `IsEmptyList( subjectListName);
            Instruction test = `IfThenElse(cond, subList, empty());
            TomList succesList = appendInstruction(test,concat(declarationList,assignementList));
            
            cond = `Not(IsEmptyList(subjectListName));
            test = `IfThenElse(cond, succesList, empty());
            result = appendInstruction(test,result);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * if(!IS_EMPTY_TomList(subjectList)) {
               *   TomTerm x_j = (TomTerm) GET_HEAD_TomList(subjectList);
               *   subjectList =  (TomList) GET_TAIL_TomList(subjectList);
               *   ...
               * }
               */
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = `Variable(option(),PositionName(path),termType);
            
            TomList declarationList = empty();
            TomList assignementList = empty();
            assignementList = appendInstruction(`Assign(var,GetHead(subjectListName)),assignementList);
            assignementList = appendInstruction(`Assign(subjectListName,GetTail(subjectListName)),assignementList);
            
            TomList succesList = concat(concat(declarationList,assignementList),subList);
            Expression cond = `Not(IsEmptyList(subjectListName));
            Instruction test = `IfThenElse(cond, succesList, empty());
            result = appendInstruction(test,result);
            break matchBlock;
          }
        }
        
          //conc(UnamedVariable(option,_),termTail*) -> {
        Cons(UnamedVariable(option,_),termTail) -> {
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * TODO
               */
            System.out.println("cons(UnamedVariable(option,_), empty): not yet implemented");
            System.exit(1);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * TODO
               */
            System.out.println("cons(UnamedVariable(option,_), termTail): not yet implemented");
            System.exit(1);
            break matchBlock;
          }
        }
        
          //conc(var:VariableStar(option,_, termType),termTail*) -> {
        Cons(var@VariableStar(option,_, termType),termTail) -> {
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
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = `Variable(option(),PositionName(path),termType);
            Instruction assignement = `Assign(var,TomTermToExpression(subjectListName));
            result = concat(appendInstruction(assignement,result),subList);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * TomList begin_i = subjectList;
               * TomList end_i   = subjectList;
               * do {
               *   // SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(begin_i,end_i);
               *   ...
               *   if(!IS_EMPTY_TomList(end_i) )
               *     end_i = (TomList) GET_TAIL_TomList(end_i);
               *   subjectList = end_i;
               * } while( !IS_EMPTY_TomList(subjectList) )
               */
            TomList pathBegin = append(`Begin(makeNumber(indexTerm)),oldPath);
            TomList pathEnd = append(`End(makeNumber(indexTerm)),oldPath);
            TomTerm variableBeginAST = `Variable(option(),PositionName(pathBegin),termType);
            TomTerm variableEndAST   = `Variable(option(),PositionName(pathEnd),termType);
            TomList declarationList = empty();
            declarationList = append(`Declaration(variableBeginAST),declarationList);
            declarationList = append(`Declaration(variableEndAST),declarationList);
            TomList assignementList = empty();
            assignementList = appendInstruction(`Assign(variableBeginAST,TomTermToExpression(subjectListName)),assignementList);
            assignementList = appendInstruction(`Assign(variableEndAST,TomTermToExpression(subjectListName)),assignementList);
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = `Variable(option(),PositionName(path),termType);
            TomList doList = empty();
            doList = appendInstruction(`Assign(var,GetSliceList(symbol.getAstName(),variableBeginAST,variableEndAST)),doList);
            
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
          System.exit(1);
        }
      }
    } // end matchBlock
    return result;
  }

  TomList genArrayMatchingAutomata(TomSymbol symbol,
                                   TomList termList,
                                   TomList oldPath,
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
    String szero = "0";
    Expression glZero = `TomTermToExpression(TargetLanguageToTomTerm(ITL(szero)));
    if(indexTerm > 1) {
      variableListAST = subjectListName;
      variableIndexAST = subjectListIndex;
    } else {
      TomList pathList = append(`ListNumber(makeNumber(indexTerm)),oldPath);
      TomList pathIndex = append(`IndexNumber(makeNumber(indexTerm)),oldPath);

      matchBlock: {
        %match(TomTerm subjectListName) {
          Variable(option, _, termType) -> {
            variableListAST = `Variable(option(),PositionName(pathList),termType);
              // TODO: other termType
            variableIndexAST = `Variable(option(),PositionName(pathIndex),getIntType());
            break matchBlock;
          }
          _ -> {
            System.out.println("GenArrayMatchingAutomata strange subjectListName: " + subjectListName);
            System.exit(1);
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
        
          //conc(var:Variable(option, _, termType),termTail*) -> {
        Cons(var@Variable(option, _, termType),termTail) -> {
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
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = `Variable(option(),PositionName(path),termType);
            
            TomList declarationList = empty();
            TomList assignementList = empty();
            assignementList = appendInstruction(`Assign(var,GetElement(subjectListName,subjectListIndex)),assignementList);
            assignementList = appendInstruction(`Increment(subjectListIndex),assignementList);
            
            if(generateSemanticAction) {
              subList = appendInstruction(`Action( actionList),subList);
            }
            
            Expression cond = `IsEmptyArray( subjectListName,subjectListIndex);
            Instruction test = `IfThenElse(cond, subList, empty());
            TomList succesList = appendInstruction(test,concat(declarationList,assignementList));
            
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
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = `Variable(option(),PositionName(path),termType);
            
            TomList declarationList = empty();
            TomList assignementList = empty();
            TomList succesList      = empty();
            
            assignementList = appendInstruction(`Assign(var,GetElement(subjectListName,subjectListIndex)),assignementList);
            assignementList = appendInstruction(`Increment(subjectListIndex),assignementList);
            
            succesList = concat(concat(concat(succesList,declarationList),assignementList),subList);
            
            Expression cond = `Not(IsEmptyArray( subjectListName,subjectListIndex));
            Instruction test = `IfThenElse(cond, succesList, empty());
            
            result = appendInstruction(test,result);
            break matchBlock;
          }
        }
        
          //conc(UnamedVariable(option,_),termTail*) -> {
        Cons(UnamedVariable(option,_),termTail) -> {
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * TODO
               */
            System.out.println("cons(UnamedVariable(option,_), empty): not yet implemented");
            System.exit(1);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * TODO
               */
            System.out.println("cons(UnamedVariable(option,_), empty): not yet implemented");
            System.exit(1);
            break matchBlock;
          }
        }
        
          //conc(var:VariableStar(option,_, termType),termTail*) -> {
        Cons(var@VariableStar(option,_, termType),termTail) -> {
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
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = `Variable(option(),PositionName(path),termType);
            
            Instruction assignement = `Assign(var,GetSliceArray(
                                            symbol.getAstName(),subjectListName,
                                            subjectListIndex,
                                            ExpressionToTomTerm(GetSize(subjectListName))
                                         ));
            result = concat(appendInstruction(assignement,result),subList);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * int begin_i = subjectIndex;
               * int end_i   = subjectIndex;
               * do {
               *   // SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(subjectList,begin_i,end_i);
               *   ...
               *   end_i++;
               *   subjectIndex = end_i;
               * } while( !IS_EMPTY_TomList(subjectList) )
               */
            
            TomList pathBegin = append(`Begin(makeNumber(indexTerm)),oldPath);
            TomList pathEnd = append(`End(makeNumber(indexTerm)),oldPath);
              // TODO: termType
            TomTerm variableBeginAST = `Variable(option(),PositionName(pathBegin),getIntType());
            TomTerm variableEndAST   = `Variable(option(),PositionName(pathEnd),getIntType());
            TomList declarationList = empty();
            declarationList = append(`Declaration(variableBeginAST),declarationList);
            declarationList = append(`Declaration(variableEndAST),declarationList);
            TomList assignementList = empty();
            assignementList = appendInstruction(`Assign(variableBeginAST,TomTermToExpression(subjectListIndex)),assignementList);
            assignementList = appendInstruction(`Assign(variableEndAST,TomTermToExpression(subjectListIndex)),assignementList);
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = `Variable(option(),PositionName(path),termType);
            TomList doList = empty();
            doList = appendInstruction(`Assign(var,
                                    GetSliceArray(symbol.getAstName(),subjectListName,variableBeginAST,
                                                  variableEndAST)),doList);
            doList = concat(doList,subList);
            doList = appendInstruction(`Increment(variableEndAST),doList);
            doList = appendInstruction(`Assign(subjectListIndex,TomTermToExpression(variableEndAST)),doList); 
            
            Expression cond = `Not(IsEmptyArray(subjectListName, subjectListIndex));
            Instruction doWhile = `DoWhile(doList,cond);
            
            TomList tmpResult = empty();
            if(Flags.supportedBlock) {
              tmpResult = appendInstruction(`OpenBlock(),tmpResult);
            }
            tmpResult = concat(tmpResult,declarationList);
            tmpResult = concat(tmpResult,result);
            tmpResult = concat(tmpResult,assignementList);
            tmpResult = appendInstruction(doWhile,tmpResult);
            if(Flags.supportedBlock) {
              tmpResult = appendInstruction(`CloseBlock(),tmpResult);
            }
            result = tmpResult;
            break matchBlock;
          }
        }
        
        _ -> {
          System.out.println("GenArrayMatchingAutomata strange termList: " + termList);
          System.exit(1);
        }
      }
    } // end matchBlock
    return result;
  }
  
} // end of class
  
                  
    
