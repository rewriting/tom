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
import jtom.runtime.Replace2;
import aterm.ATerm;
import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import jtom.tools.Tools;
import jtom.xml.Constants;
import jtom.exception.TomRuntimeException;

public class TomExpander extends TomBase implements TomTask {
  private TomTask nextTask;
  TomKernelExpander tomKernelExpander;
  
  public TomExpander(jtom.TomEnvironment environment,
                     TomKernelExpander tomKernelExpander) {
    super(environment);
    this.tomKernelExpander = tomKernelExpander;
  }

// ------------------------------------------------------------
  %include { ../adt/TomSignature.tom }
// ------------------------------------------------------------

  public void addTask(TomTask task) {
  	this.nextTask = task;
  }
  public void process(TomTaskInput input) {
  	try {
	  long startChrono = 0;
	  boolean verbose = input.isVerbose(), intermediate = input.isIntermediate(), debugMode = input.isDebugMode();
	  if(verbose) {
		startChrono = System.currentTimeMillis();
	  }
	  TomTerm syntaxExpandedTerm = expandTomSyntax(input.getTerm());
      tomKernelExpander.updateSymbolTable();
      TomTerm context = null;
      TomTerm expandedTerm  = expandVariable(context, syntaxExpandedTerm);
      if(debugMode) {
        tomKernelExpander.expandMatchPattern(expandedTerm);
        //generateOutput(input.inputFileName + input.debugTableSuffix, tomParser.getStructTable());
      }
	  if(verbose) {
		System.out.println("TOM expansion phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
	  }
      if(intermediate) {
          Tools.generateOutput(input.getBaseInputFileName() + TomTaskInput.expandedSuffix, expandedTerm);
          Tools.generateOutput(input.getBaseInputFileName() + TomTaskInput.expandedTableSuffix, symbolTable().toTerm());
	  }
      input.setTerm(expandedTerm);
    } catch (Exception e) {
    	addError(input, "Exception occurs in TomExpander"+e.getMessage(), input.getInputFileName(), 0, 0);
      e.printStackTrace();
      return;
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
              backQuoteTerm@BackQuoteAppl[] -> {
                return expandBackQuoteAppl(backQuoteTerm);
              }

              RecordAppl(option,Name(tomName),args) -> {
                return expandRecordAppl(option,tomName,args);
              }

              XMLAppl(Option(optionList), Name(tomName), list1, list2) -> {
                return expandXMLAppl(optionList, tomName, list1, list2);
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

  protected TomTerm expandBackQuoteAppl(TomTerm t) {
    Replace1 replaceSymbol = new Replace1() {
        public ATerm apply(ATerm t) {
          if(t instanceof TomTerm) {
            %match(TomTerm t) {
              BackQuoteAppl[option=Option(optionList),astName=name@Name(tomName),args=l] -> {
                TomSymbol tomSymbol = getSymbol(tomName);
                TomList args  = (TomList) traversal().genericTraversal(l,this);
                
                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol)) {
                    return `BuildList(name,args);
                  } else if(isArrayOperator(tomSymbol)) {
                    return `BuildArray(name,args);
                  } else {
                    return `BuildTerm(name,args);
                  }
                } else if(args.isEmpty() && !hasConstructor(optionList)) {
                  return `BuildVariable(name);
                } else {
                  return `FunctionCall(name,args);
                }
              }
            } // end match 
          } else {
            return traversal().genericTraversal(t,this);
          }
          return traversal().genericTraversal(t,this);
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(t);
  }

  private TomList sortAttributeList(TomList attrList) {
    %match(TomList attrList) {
      concTomTerm() -> { return attrList; }
      concTomTerm(X1*,
                e1@Appl[args=manyTomList(Appl[astName=Name(name1)],_)],
                X2*,
                e2@Appl[args=manyTomList(Appl[astName=Name(name2)],_)],
                X3*) -> {
        if(name1.compareTo(name2) >= 0) {
            //System.out.println("swap: " + name1 + " <--> " + name2);
          return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
        }                
      }
    }
    return attrList;
  }

  protected TomTerm expandXMLAppl(OptionList optionList, String tomName,
                                  TomList attrList, TomList childList) {
    boolean implicitAttribute = hasImplicitXMLAttribut(optionList);
    boolean implicitChild     = hasImplicitXMLChild(optionList);
    
    TomList newAttrList  = `emptyTomList();
    TomList newChildList = `emptyTomList();

    TomTerm star = ast().makeUnamedVariableStar(ast().makeOption(),"unknown type");
    if(implicitAttribute) { newAttrList  = `manyTomList(star,newAttrList); }
    if(implicitChild)     { newChildList = `manyTomList(star,newChildList); }

    attrList = sortAttributeList(attrList);
    while(!attrList.isEmpty()) {
      TomTerm newPattern = expandTomSyntax(attrList.getHead());
      newAttrList = `manyTomList(newPattern,newAttrList);
      if(implicitAttribute) { newAttrList = `manyTomList(star,newAttrList); }
      attrList = attrList.getTail();
    }
    newAttrList = (TomList) newAttrList.reverse();
    
    while(!childList.isEmpty()) {
      TomTerm newPattern = expandTomSyntax(childList.getHead());
      newChildList = `manyTomList(newPattern,newChildList);
      if(implicitChild) {
        if(newPattern.isVariableStar()) {
            // remove the previously inserted pattern
          newChildList = newChildList.getTail();
          if(newChildList.getHead().isUnamedVariableStar()) {
            // remove the previously inserted star
            newChildList = newChildList.getTail();
          }
            // re-insert the pattern
          newChildList = `manyTomList(newPattern,newChildList);
        } else {
          newChildList = `manyTomList(star,newChildList);
        }
      }
      childList = childList.getTail();
    }
    newChildList = (TomList) newChildList.reverse();

    Option emptyOption = ast().makeOption();
      /*
       * encode the name and put it into the table of symbols
       */
    tomName = ast().encodeXMLString(symbolTable(),tomName);
    
    TomList newArgs = `concTomTerm(
      Appl(emptyOption,Name(tomName),empty()),
      Appl(emptyOption,Name(Constants.CONC_TNODE), newAttrList),
      Appl(emptyOption,Name(Constants.CONC_TNODE), newChildList));
    TomTerm result = `Appl(Option(optionList),Name(Constants.ELEMENT_NODE),newArgs);
      //System.out.println("expand:\n" + result);
    return result;
   
  }

    /*
     * At Tom expander level, we worry only about RewriteRule and
     *  their condlist
     * replace Name by Symbol
     * replace Name by Variable
     */

  Replace2 replace_expandVariable = new Replace2() {
      public ATerm apply(ATerm t, Object arg1) {
        TomTerm contextSubject = (TomTerm)arg1;
        return expandVariable(contextSubject, (TomTerm)t);
      }
    };
  
  public TomTerm expandVariable(TomTerm contextSubject, TomTerm subject) {

      //System.out.println("expandVariable:\n\t" + subject );
      
    %match(TomTerm contextSubject, TomTerm subject) {
      context, Tom(l) -> {
        TomList newL = (TomList) traversal().genericTraversal(l,replace_expandVariable,contextSubject);
        return `Tom(newL);
      }
      
      context, RuleSet(ruleList,orgTrack) -> { 
        TomRuleList newRuleList = `emptyTomRuleList();
        while(!ruleList.isEmpty()) {
          TomRule rule = ruleList.getHead();
          newRuleList = (TomRuleList) newRuleList.append(expandRewriteRule(context,rule));
          ruleList = ruleList.getTail();
        }
        
        return `RuleSet(newRuleList,orgTrack);
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


  public TomRule expandRewriteRule(TomTerm contextSubject, TomRule subject) {
      
    %match(TomTerm contextSubject, TomRule subject) {

      context, RewriteRule(Term(lhs@Appl(Option(optionList),Name(tomName),l)),
                           Term(rhs),
                           condList,
                           option) -> { 
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
        
        return `RewriteRule(newLhs,newRhs,newCondList,option);
      }
      
        // default rule
      context, t -> {
        System.out.println("expandRewriteRule. Stange case '" + t);
        return null;
      }
    } // end match
  }

  
  private TomType getTypeFromVariableList(TomName name, TomList list) {

      //System.out.println("name = " + name);
      //System.out.println("list = " + list);
    
    %match(TomName name,TomList list) {
      _,emptyTomList() -> {
        System.out.println("getTypeFromVariableList. Stange case '" + name + "' not found");
        throw new TomRuntimeException(new Throwable("getTypeFromVariableList. Stange case '" + name + "' not found"));
      }

      varName, manyTomList(Variable[astName=varName,astType=type@Type[]],tail) -> { return type; }
      varName, manyTomList(VariableStar[astName=varName,astType=type@Type[]],tail) -> { return type; }
      _, manyTomList(t,tail) -> { return getTypeFromVariableList(name,tail); }
      
    }
    return null;
  }
 
} // Class TomExpander


  
