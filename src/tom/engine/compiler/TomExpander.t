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
  
import java.util.HashSet;

import jtom.TomBase;
import jtom.adt.*;
import jtom.runtime.Replace1;
import aterm.ATerm;
import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;

public class TomExpander extends TomBase implements TomTask {
  private TomTask nextTask;
  TomKernelExpander tomKernelExpander;
  
  public TomExpander(jtom.TomEnvironment environment,
                     TomKernelExpander tomKernelExpander) {
    super(environment);
    this.tomKernelExpander = tomKernelExpander;
  }

// ------------------------------------------------------------
  %include { Tom.signature }
// ------------------------------------------------------------

  public void addTask(TomTask task) {
  	this.nextTask = task;
  }
  public void process(TomTaskInput input) {
  	try {
  	  System.out.println("Processing TomExpander Task");
      TomTerm syntaxExpandedTerm = expandTomSyntax(input.getTerm());
      tomKernelExpander.updateSymbolTable();
      TomTerm context = null;
      TomTerm expandedTerm  = expandVariable(context, syntaxExpandedTerm);
      input.setTerm(expandedTerm);
    } catch (Exception e) {
    }
    if(nextTask != null) {
      nextTask.process(input);
    }
  }
  
  public TomTask getTask() {
  	return nextTask;
  }
  
    /*
     * The 'expandTomSyntax' phase replaces:
     * -each 'RecordAppl' by its expanded term form:
     *   (unused slots a replaced by placeholders)
     * - each BackQuoteTerm by its compiled form
     */
  
  public TomTerm expandTomSyntax(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {
              BackQuoteTerm[tomTerm=t] -> {
                return expandBackQuoteTerm(t);
              }
            
              RecordAppl(option,Name(tomName),args) -> {
                return expandRecordAppl(option,tomName,args);
              }
            
              _ -> {
                return traversal().genericTraversal(subject,this);
              }
            } // end match
          } else {
            return traversal().genericTraversal(subject,this);
          }
        } // end apply
      }; // end new
    
    return (TomTerm) replace.apply(subject); 
  }

  protected TomTerm expandRecordAppl(Option option, String tomName, TomList args) {
    TomSymbol tomSymbol = getSymbol(tomName);
    SlotList slotList = tomSymbol.getSlotList();
    TomList subtermList = empty();
      // For each slotName (from tomSymbol)
    while(!slotList.isEmpty()) {
      TomName slotName = slotList.getHead().getSlotName();
        //debugPrintln("\tslotName  = " + slotName);
      TomList pairList = args;
      TomTerm newSubterm = null;
      if(!slotName.isEmptyName()) {
          // look for a same name (from record)
        whileBlock: {
          while(newSubterm==null && !pairList.isEmpty()) {
            TomTerm pairSlotName = pairList.getHead();
            %match(TomName slotName, TomTerm pairSlotName) {
              Name[string=name], PairSlotAppl(Name[string=name],slotSubterm) -> {
                  // bingo
                statistics().numberSlotsExpanded++;
                newSubterm = expandTomSyntax(slotSubterm);
                break whileBlock;
              }
              _ , _ -> {pairList = pairList.getTail();}
            }
          }
        } // end whileBlock
      }
      
      if(newSubterm == null) {
        newSubterm = `Placeholder(ast().makeOption());
      }
      subtermList = append(newSubterm,subtermList);
      slotList = slotList.getTail();
    }
    
    return `Appl(option,Name(tomName),subtermList);
  }
  
  protected TomTerm expandBackQuoteTerm(TomTerm t) {
    Replace1 replaceSymbol = new Replace1() {
        public ATerm apply(ATerm t) {
          if(t instanceof TomTerm) {
            %match(TomTerm t) {
              Appl[option=Option(optionList),astName=name@Name(tomName),args=l] -> {
                TomSymbol tomSymbol = getSymbol(tomName);
                TomList args  = (TomList) traversal().genericTraversal(l,this);

                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol)) {
                    return `BuildList(name,args);
                  } else if(isArrayOperator(tomSymbol)) {
                    return `BuildArray(name,args);
                  } else if(isIntegerOperator(tomSymbol)) {
                    return `BuildBuiltin(name);
                  } else if(isStringOperator(tomSymbol)) {
                    return `BuildBuiltin(name);
                  } else {
                    return `BuildTerm(name,args);
                  }
                } else if(args.isEmpty() && !hasConstructor(optionList)) {
                  return `BuildVariable(name);
                } else {
                  return `FunctionCall(name,args);
                }
              }

              DotTerm(t1,t2) -> {
                TomTerm tt1 = (TomTerm) this.apply(t1);
                TomTerm tt2 = (TomTerm) this.apply(t2);
                return `DotTerm(tt1,tt2);
              }
              
              var@VariableStar[astName=name] -> {
                  return var;
              }
              
              _ -> {
                System.out.println("replaceSymbol: strange case: '" + t + "'");
                System.exit(1);
              }
            }
            return t;
          } else {
            return traversal().genericTraversal(t,this);
          }
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(t);
  }
  
    /*
     * At Tom expander level, we worry only about RewriteRule and
     *  their condlist
     * replace Name by Symbol
     * replace Name by Variable
     */
  public TomTerm expandVariable(TomTerm contextSubject, TomTerm subject) {
    if(!(subject instanceof TomTerm)) {
      return tomKernelExpander.expandVariable(contextSubject,subject);
    }

      //System.out.println("expandVariable is a tomTerm:\n\t" + subject );
    %match(TomTerm contextSubject, TomTerm subject) {

      context, TomRuleToTomTerm(RewriteRule(Term(lhs@Appl(Option(optionList),Name(tomName),l)),
                           Term(rhs),
                           condList,
                           option)) -> { 
          //debugPrintln("expandVariable.13: Rule(" + lhs + "," + rhs + ")");
        TomSymbol tomSymbol = getSymbol(tomName);
        TomType symbolType = getSymbolCodomain(tomSymbol);
        TomTerm newLhs = `Term(expandVariable(context,lhs));
        TomTerm newRhs = `Term(expandVariable(TomTypeToTomTerm(symbolType),rhs));
        
          // build the list of variables that occur in the lhs
        HashSet set = new HashSet();
        collectVariable(set,newLhs);
        TomList varList = ast().makeList(set);
        TomList newCondList = empty();
        while(!condList.isEmpty()) {
          TomTerm cond = condList.getHead();
          TomTerm newCond = expandVariable(`Tom(varList),cond);
          newCondList = append(newCond,newCondList);
          collectVariable(set,newCond);
          varList = ast().makeList(set);
          condList = condList.getTail();
        }
        
        return `TomRuleToTomTerm(RewriteRule(newLhs,newRhs,newCondList,option));
      }
        
      Tom(varList), MatchingCondition[lhs=lhs@Appl[astName=Name(lhsName)],
                                      rhs=rhs@Appl[astName=Name(rhsName)]] -> {
        TomSymbol lhsSymbol = getSymbol(lhsName);
        TomSymbol rhsSymbol = getSymbol(rhsName);
        TomType type;
        
        if(lhsSymbol != null) {
          type = getSymbolCodomain(lhsSymbol);
        } else if(rhsSymbol != null) {
          type = getSymbolCodomain(rhsSymbol);
        } else {
            // both lhs and rhs are variables
            // since lhs is a fresh variable, we look for rhs
          type = getTypeFromVariableList(`Name(rhsName),varList);
        }
        
        TomTerm newLhs = `expandVariable(TomTypeToTomTerm(type),lhs);
        TomTerm newRhs = `expandVariable(TomTypeToTomTerm(type),rhs);
        return `MatchingCondition(newLhs,newRhs);
      }
      
      Tom(varList), EqualityCondition[lhs=lhs@Appl[astName=Name(lhsName)],
                                      rhs=rhs@Appl[astName=Name(rhsName)]] -> {
        TomSymbol lhsSymbol = getSymbol(lhsName);
        TomSymbol rhsSymbol = getSymbol(rhsName);
        TomType type;
        
        if(lhsSymbol != null) {
          type = getSymbolCodomain(lhsSymbol);
        } else if(rhsSymbol != null) {
          type = getSymbolCodomain(rhsSymbol);
        } else {
            // both lhs and rhs are variables
          type = getTypeFromVariableList(`Name(lhsName),varList);
        }
        
          //System.out.println("EqualityCondition type = " + type);
        
        TomTerm newLhs = `expandVariable(TomTypeToTomTerm(type),lhs);
        TomTerm newRhs = `expandVariable(TomTypeToTomTerm(type),rhs);
        
          //System.out.println("lhs    = " + lhs);
          //System.out.println("newLhs = " + newLhs);
        
        return `EqualityCondition(newLhs,newRhs);
      }
      
        // default rule
      context, t -> {
        return tomKernelExpander.expandVariable(context,t);
      }
    } // end match
  }

  private TomType getTypeFromVariableList(TomName name, TomList list) {

      //System.out.println("name = " + name);
      //System.out.println("list = " + list);
    
    %match(TomName name,TomList list) {
      _,emptyTomList() -> {
        System.out.println("getTypeFromVariableList. Stange case '" + name + "' not found");
        System.exit(1);
      }

      varName, manyTomList(Variable[astName=varName,astType=type@Type[]],tail) -> { return type; }
      varName, manyTomList(VariableStar[astName=varName,astType=type@Type[]],tail) -> { return type; }
      _, manyTomList(t,tail) -> { return getTypeFromVariableList(name,tail); }
      
    }
    return null;
  }
 
} // Class TomExpander
