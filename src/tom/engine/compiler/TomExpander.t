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
  
import java.util.HashSet;

import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.runtime.Replace1;
import jtom.runtime.Replace2;
import aterm.*;
import jtom.tools.*;
import jtom.xml.Constants;
import jtom.exception.TomRuntimeException;
import jtom.TomEnvironment;

public class TomExpander extends TomTask {
	
  private TomKernelExpander tomKernelExpander;
  private TomFactory tomFactory;
  
  public TomExpander(TomEnvironment environment,
                     TomKernelExpander tomKernelExpander) {
    super("Tom Expander", environment);
    this.tomKernelExpander = tomKernelExpander;
    this.tomFactory = new TomFactory(environment);
  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------
		
  public void process() {
  	try {
		  long startChrono = 0;
		  boolean verbose = getInput().isVerbose(), intermediate = getInput().isIntermediate(),
		  						debugMode = getInput().isDebugMode();
	  	if(verbose) { startChrono = System.currentTimeMillis(); }
	  	
	  	TomTerm syntaxExpandedTerm = expandTomSyntax(getInput().getTerm());
      tomKernelExpander.updateSymbolTable();
      TomTerm context = `emptyTerm();
      TomTerm expandedTerm  = expandVariable(context, syntaxExpandedTerm);
      
      if(debugMode) {
        tomKernelExpander.expandMatchPattern(expandedTerm);
      }
	  if(verbose) {
		System.out.println("TOM expansion phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
	  }
      if(intermediate) {
          Tools.generateOutput(getInput().getBaseInputFileName() + TomTaskInput.expandedSuffix, expandedTerm);
          Tools.generateOutput(getInput().getBaseInputFileName() + TomTaskInput.expandedTableSuffix, symbolTable().toTerm());
	  }
		getInput().setTerm(expandedTerm);
		
    } catch (Exception e) {
    	addError("Exception occurs in TomExpander: "+e.getMessage(), getInput().getInputFileName(), 0, 0);
      e.printStackTrace();
      return;
    }
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
              DoubleBackQuote(backQuoteTerm) -> {
                TomTerm t = expandTomSyntax(backQuoteTerm);
                  //System.out.println("t1 = " + t);
                t = expandBackQuoteXMLAppl(t);
                  //System.out.println("t2 = " + t);
                return t;
              }
              
              backQuoteTerm@BackQuoteAppl[] -> {
                TomTerm t = expandBackQuoteAppl(backQuoteTerm);
                  //System.out.println("t = " + t);
                return t;
              }

              RecordAppl(option,nameList,args) -> {
                return expandRecordAppl(option,nameList,args);
              }

              XMLAppl(optionList,nameList,list1,list2) -> {
								//System.out.println("expandXML in:\n" + subject);
                return expandXMLAppl(optionList, nameList, list1, list2);
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

  protected TomTerm expandRecordAppl(OptionList option, NameList nameList, TomList args) {
    TomSymbol tomSymbol = getSymbol(nameList.getHead().getString());
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
        newSubterm = `Placeholder(emptyOption());
      }
      subtermList = append(newSubterm,subtermList);
      slotList = slotList.getTail();
    }
    
    return `Appl(option,nameList,subtermList);
  }

  protected TomTerm expandBackQuoteAppl(TomTerm t) {
    Replace1 replaceSymbol = new Replace1() {
        public ATerm apply(ATerm t) {
          if(t instanceof TomTerm) {
            %match(TomTerm t) {
              BackQuoteAppl[option=optionList,astName=name@Name(tomName),args=l] -> {
                TomSymbol tomSymbol = getSymbol(tomName);
                TomList args  = (TomList) traversal().genericTraversal(l,this);
                
                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol)) {
                    return `BuildList(name,args);
                  } else if(isArrayOperator(tomSymbol)) {
                    return `BuildArray(name,args);
                  } else if(isStringOperator(tomSymbol)) {
                    return `BuildVariable(name);
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
          }
          return traversal().genericTraversal(t,this);
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(t);
  }

  private TomList sortAttributeList(TomList attrList) {
    %match(TomList attrList) {
      concTomTerm() -> { return attrList; }
      concTomTerm(X1*,e1,X2*,e2,X3*) -> {
        %match(TomTerm e1, TomTerm e2) {
          Appl[args=manyTomList(Appl[nameList=(Name(name1))],_)],
          Appl[args=manyTomList(Appl[nameList=(Name(name2))],_)] -> {
            if(name1.compareTo(name2) >= 0) {
              return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
            }
          }
          BackQuoteAppl[args=manyTomList(Appl[nameList=(Name(name1))],_)],
          BackQuoteAppl[args=manyTomList(Appl[nameList=(Name(name2))],_)] -> {
            if(name1.compareTo(name2) >= 0) {
              return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
            }
          }
        }
      }
    }
    return attrList;
  }

  private OptionList convertOriginTracking(String name,OptionList optionList) {
    Option originTracking = findOriginTracking(optionList);
    %match(Option originTracking) {
      OriginTracking[line=line, fileName=fileName] -> {
        return `concOption(OriginTracking(Name(name),line,fileName));
      }
    }
    System.out.println("Warning: no OriginTracking information");
    return emptyOption();
  }

  
  protected TomTerm expandXMLAppl(OptionList optionList, NameList nameList,
                                  TomList attrList, TomList childList) {
    boolean implicitAttribute = hasImplicitXMLAttribut(optionList);
    boolean implicitChild     = hasImplicitXMLChild(optionList);
    
    TomList newAttrList  = `emptyTomList();
    TomList newChildList = `emptyTomList();

    TomTerm star = ast().makeUnamedVariableStar(convertOriginTracking("_*",optionList),"unknown type");
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

      /*
       * encode the name and put it into the table of symbols
       */
    NameList newNameList = `concTomName();
    %match(NameList nameList) {
      (_*,Name(name),_*) -> {
        newNameList = (NameList)newNameList.append(`Name(tomFactory.encodeXMLString(symbolTable(),name)));
      }
    }
    String tomName = newNameList.getHead().getString();
    TomList newArgs = `concTomTerm(
      Appl(convertOriginTracking(tomName,optionList),newNameList,empty()),
      Appl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newAttrList),
      Appl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newChildList));
    TomTerm result = `Appl(optionList,concTomName(Name(Constants.ELEMENT_NODE)),newArgs);
      //System.out.println("expandXML out:\n" + result);
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

  protected TomTerm expandBackQuoteXMLAppl(TomTerm subject) {
    Replace1 replaceSymbol = new Replace1() {
        public ATerm apply(ATerm t) {
          if(t instanceof TomTerm) {
            %match(TomTerm t) {
              Composite(list) -> {
                list = parseBackQuoteXMLAppl(list);
                list = (TomList) traversal().genericTraversal(list,this);
                return `Composite(list);
              }
            } // end match 
          } 
          return traversal().genericTraversal(t,this);
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(subject);
  }

  private TomList parseBackQuoteXMLAppl(TomList list) {
    %match(TomList list) {
      concTomTerm(
        TargetLanguageToTomTerm(ITL("#TEXT")),
        TargetLanguageToTomTerm(ITL("(")),
        value*,
        TargetLanguageToTomTerm(ITL(")")),
        tail*
        ) -> {
        TomTerm newBackQuoteAppl = `BackQuoteAppl(emptyOption(),Name(Constants.TEXT_NODE),concTomTerm(Composite(value*)));
        TomList newTail = parseBackQuoteXMLAppl(tail);
        return `concTomTerm(newBackQuoteAppl,newTail*);
      }

      concTomTerm(
        BuildVariable(name),
        TargetLanguageToTomTerm(ITL("*")),
        tail*
        ) -> {
        TomTerm term = `VariableStar(emptyOption(),name,TomTypeAlone("unknown type"));
        TomList newTail = parseBackQuoteXMLAppl(tail);
        return `concTomTerm(term,newTail*);
      }

      label2:concTomTerm(
        TargetLanguageToTomTerm(ITL("<")),
        BuildVariable[astName=name],
        Attributes*,
        TargetLanguageToTomTerm(ITL(">")),
        Body*,
        TargetLanguageToTomTerm(ITL("</")),
        BuildVariable[astName=name],
        TargetLanguageToTomTerm(ITL(">")),
        tail*
        ) -> {
        if(containClosingBracket(Attributes)) {
          break label2;
        }

          //System.out.println("Attributes = " + Attributes);
          //System.out.println("Body = " + Body);
        
        TomTerm newName = `BackQuoteAppl(emptyOption(),encodeName(name),empty());
        TomTerm newAttribute = metaEncodeTNodeList(aggregateXMLAttribute(Attributes));
        TomTerm newBody = metaEncodeTNodeList(aggregateXMLBody(Body));
        TomTerm newBackQuoteAppl = `BackQuoteAppl(emptyOption(),Name(Constants.ELEMENT_NODE),concTomTerm(newName,newAttribute,newBody));
          //System.out.println("newBackQuoteAppl1 = " + newBackQuoteAppl);
        newBackQuoteAppl = expandTomSyntax(newBackQuoteAppl);
          //System.out.println("newBackQuoteAppl2 = " + newBackQuoteAppl);
          //TomList newTail = aggregateXMLBody(tail);
        TomList newTail = parseBackQuoteXMLAppl(tail);
        return `concTomTerm(newBackQuoteAppl,newTail*);
      }
      
      label3:concTomTerm(
        TargetLanguageToTomTerm(ITL("<")),
        BuildVariable[astName=name],
        Attributes*,
        TargetLanguageToTomTerm(ITL("/>")),
        tail*
        ) -> {
        if(containClosingBracket(Attributes)) {
          break label3;
        }
          //System.out.println("SingleNode(" + name +")");
          //System.out.println("Attributes = " + Attributes);

        TomTerm newName = `BackQuoteAppl(emptyOption(),encodeName(name),empty());
        TomTerm newAttribute = metaEncodeTNodeList(aggregateXMLAttribute(Attributes));
        TomTerm newBody = metaEncodeTNodeList(`concTomTerm());
        TomTerm newBackQuoteAppl = `BackQuoteAppl(emptyOption(),Name(Constants.ELEMENT_NODE),concTomTerm(newName,newAttribute,newBody));
          //System.out.println("newBackQuoteAppl1 = " + newBackQuoteAppl);
        newBackQuoteAppl = expandTomSyntax(newBackQuoteAppl);
          //System.out.println("newBackQuoteAppl2 = " + newBackQuoteAppl);
          //TomList newTail = aggregateXMLBody(tail);
        TomList newTail = parseBackQuoteXMLAppl(tail);
        return `concTomTerm(newBackQuoteAppl,newTail*);

      }

      concTomTerm() -> {
        return `concTomTerm();
      }

      concTomTerm(head,tail*) -> {
        TomList newTail = parseBackQuoteXMLAppl(tail);
        return `concTomTerm(head,newTail*);
      }

      
    }
    return list;
  }

  private boolean containClosingBracket(TomList list) {
    %match(TomList list) {
      concTomTerm(_*,TargetLanguageToTomTerm(ITL(">")),_*) -> {
        return true;
      }
      concTomTerm(_*,TargetLanguageToTomTerm(ITL("/>")),_*) -> {
        return true;
      }
    }
    return false;
  }
  
  private TomTerm metaEncodeTNodeList(TomList list) {
    return `BackQuoteAppl(emptyOption(),Name(Constants.CONC_TNODE),list);
  }
  
  private TomList aggregateXMLBody(TomList subjectList) {
    TomTerm composite = expandBackQuoteXMLAppl(`Composite(subjectList));
      //System.out.println("composite = " + composite);
    return composite.getArgs();
  }

  private TomName encodeName(TomName name) {
    return `Name("\"" + name.getString() + "\"");
  }
  
  private TomList aggregateXMLAttribute(TomList subjectList) {
    TomList list = `concTomTerm();
    %match(TomList subjectList) {
      concTomTerm(
        X1*,
        BuildVariable(name),TargetLanguageToTomTerm(ITL("=")),value,
        X2*) -> {
        TomTerm newValue = `BackQuoteAppl(emptyOption(),Name(Constants.TEXT_NODE),concTomTerm(value));
        TomList args = `concTomTerm(
          BackQuoteAppl(emptyOption(),encodeName(name),concTomTerm()),
          BackQuoteAppl(emptyOption(),Name("\"true\""),concTomTerm()),
          newValue);
        TomTerm attributeNode = `BackQuoteAppl(emptyOption(),
                                               Name(Constants.ATTRIBUTE_NODE),
                                               args);
        list = `manyTomList(attributeNode,list);
      }

      concTomTerm(
        X1*,BuildVariable(name),
        TargetLanguageToTomTerm(ITL("*")),
        X2*
        ) -> {
        TomTerm attributeNode = `VariableStar(emptyOption(),name,TomTypeAlone("unknown type"));
        list = `manyTomList(attributeNode,list);
      }
    }
    list = (TomList) sortAttributeList(list).reverse();
    return list;
  }

  
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
        
      Tom(varList), MatchingCondition[lhs=lhs@Appl[nameList=(Name(lhsName),_*)],
                                      rhs=rhs@Appl[nameList=(Name(rhsName))]] -> {
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
      
      Tom(varList), EqualityCondition[lhs=lhs@Appl[nameList=(Name(lhsName))],
                                      rhs=rhs@Appl[nameList=(Name(rhsName))]] -> {
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

      context, RewriteRule(Term(lhs@Appl(optionList,(Name(tomName)),_)),
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


  
