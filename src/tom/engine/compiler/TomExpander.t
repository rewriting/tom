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

public class TomExpander extends TomBase {

  private Option optionMatchTypeVariable;
  
  public TomExpander(jtom.TomEnvironment environment) {
	super(environment);
  }

  protected interface ReplaceContext {
    ATerm apply(ATerm context,ATerm t) throws TomException;
  }

    /*
     * Apply a function to each element of a list
     */
  protected ATermList genericMapContext(ATerm context, ATermList subject, ReplaceContext replace) {
    ATermList res = subject;
    try {
      if(!subject.isEmpty()) {
        ATerm term = replace.apply(context,subject.getFirst());
        ATermList list = genericMapContext(context,subject.getNext(),replace);
        res = list.insert(term);
      }
    } catch(Exception e) {
      System.out.println("genericMapContext error: " + e);
      System.exit(0);
    }
    return res;
  }

    /*
     * Apply a function to each subterm of a term
     */
  protected ATermAppl genericMaptermContext(ATerm context, ATermAppl subject, ReplaceContext replace) {
    try {
      ATerm newSubterm;
      for(int i=0 ; i<subject.getArity() ; i++) {
        newSubterm = replace.apply(context,subject.getArgument(i));
        if(newSubterm != subject.getArgument(i)) {
          subject = subject.setArgument(newSubterm,i);
        }
      }
    } catch(Exception e) {
      System.out.println("genericMaptermContext error: " + e);
      e.printStackTrace();
      System.exit(0);
    }

    return subject;
  }

    /*
     * Traverse a subject and replace
     */
  private int counter = 0;
  protected ATerm genericTraversalContext(ATerm context, ATerm subject, ReplaceContext replace) {
    ATerm res = subject;
    try {
      if(subject instanceof ATermAppl) {
        res = genericMaptermContext(context,(ATermAppl) subject,replace);
      } else if(subject instanceof ATermList) {
        res = genericMapContext(context,(ATermList) subject,replace);
      }
    } catch(Exception e) {
      System.out.println("genericTraversalContext error: " + e);
      System.exit(0);
    }
    return res;
  } 
  

// ------------------------------------------------------------
  %include { Tom.signature }
// ------------------------------------------------------------

    /*
     * The 'expand' phase replaces a 'RecordAppl' by its classical term form:
     * The unused slots a replaced by placeholders
     */
  
  public TomTerm expandSyntax(TomTerm subject) throws TomException {
    Replace replace = new Replace() { 
        public ATerm apply(ATerm subject) throws TomException {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {
              BackQuoteTerm[term=t] -> {
                return expandBackQuoteTerm(t);
              }
            
              RecordAppl(option,Name(tomName),args) -> {
                return expandRecordAppl(option,tomName,args);
              }
            
              _ -> {
                  //System.out.println("expand this = " + this);
                return genericTraversal(subject,this);
              }
            } // end match
          } else {
            return genericTraversal(subject,this);
          }
        } // end apply
      }; // end new
    
    return (TomTerm) replace.apply(subject); 
  }

  private TomTerm expandRecordAppl(Option option, String tomName, TomList args)
    throws TomException {
    TomSymbol tomSymbol = getSymbol(tomName);
    SlotList slotList = tomSymbol.getSlotList();
    TomList subtermList = empty();
      // For each slotName (from tomSymbol)
    while(!slotList.isEmptySlotList()) {
      TomName slotName = slotList.getHeadSlotList().getSlotName();
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
                newSubterm = expandSyntax(slotSubterm);
                break whileBlock;
              }
            }
            pairList = pairList.getTail();
          }
        } // end whileBlock
      }
      
      if(newSubterm == null) {
        newSubterm = `Placeholder(ast().makeOption());
      }
      subtermList = append(newSubterm,subtermList);
      slotList = slotList.getTailSlotList();
    }
    
    return `Appl(option,Name(tomName),subtermList);
  }
  
  private TomTerm expandBackQuoteTerm(TomTerm t) throws TomException {
    Replace replaceSymbol = new Replace() {
        public ATerm apply(ATerm t) throws TomException {
          if(t instanceof TomTerm) {
            %match(TomTerm t) {
              Appl[option=Option(optionList),astName=name@Name(tomName),args=l] -> {
                TomSymbol tomSymbol = getSymbol(tomName);
                  //TomList args  = tomListMap(l,this);
                TomList args  = (TomList) genericTraversal(l,this);

                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol)) {
                    return `BuildList(name,args);
                  } else if(isArrayOperator(tomSymbol)) {
                    return `BuildArray(name,args);
                  } else {
                    return `BuildTerm(name,args);
                  }
                } else if(args.isEmpty() && getLRParen(optionList)==null) {
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

              VariableStar[astName=name] -> {
                return `BuildVariableStar(name);
              }
              
              _ -> {
                System.out.println("replaceSymbol: strange case: '" + t + "'");
                System.exit(1);
              }
            }
            return t;
          } else {
            return genericTraversal(t,this);
          }
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(t);
  }
  
    /*
     * replace Name by Symbol
     * replace Name by Variable
     */

  private ReplaceContext replacePass1 = new ReplaceContext() { 
      public ATerm apply(ATerm contextSubject,ATerm subject) throws TomException {

        if(!(subject instanceof TomTerm)) {
          //debugPrintln("pass1 not a tomTerm: " );
            //System.out.println("pass1 not a tomTerm:\n\t" + subject );
          
          if(subject instanceof TomType) {
            %match(TomType subject) {
              TomTypeAlone(tomType) -> { return getType(tomType); }
            }
          }
          return genericTraversalContext(contextSubject,subject,this);
        }

          //System.out.println("pass1 is a tomTerm:\n\t" + subject );
        
        %match(TomTerm contextSubject, TomTerm subject) {
          TomTypeToTomTerm(type@Type(tomType,glType)) , appl@Appl(Option(optionList),name@Name(strName),Empty()) -> {
            //debugPrintln("pass1.1: Type(" + tomType + "," + glType + ")");
            //debugPrintln("appl = " + appl);
                
            Option option = `Option(replaceAnnotedName(optionList,type));
              // create a constant or a variable
            TomSymbol tomSymbol = getSymbol(strName);
            if(tomSymbol != null) {
              return `Appl(option,name,Empty());
            } else {
              statistics().numberVariablesDetected++;
              testVariableWithoutParen(option,strName);
              return `Variable(option,name,type);
            }
          } 
              
          Variable(option1,name1,type1) , appl@Appl(Option(optionList),name@Name(strName),Empty()) -> {
            //debugPrintln("pass1.3: Variable(" + option1 + "," + name1 + "," + type1 + ")");
            //debugPrintln("appl = " + appl);
                
            Option option = `Option(replaceAnnotedName(optionList,type1));
              // under a match construct
              // create a constant or a variable
            TomSymbol tomSymbol = getSymbol(strName);
            if(tomSymbol != null) {
              return `Appl(option,name,Empty());
            } else {
              statistics().numberVariablesDetected++;
              testVariableWithoutParen(option,strName);
              return `Variable(option,name,type1);
            }
          } 
              
          TomTypeToTomTerm(type@Type(tomType,glType)) , Placeholder(Option(optionList)) -> {
            Option option = `Option(replaceAnnotedName(optionList,type));
              // create an unamed variable
            return `UnamedVariable(option,type);
          } 
              
          Variable(option1,name1,type1) , Placeholder(Option(optionList)) -> {
            Option option = `Option(replaceAnnotedName(optionList,type1));
              // create an unamed variable
            return `UnamedVariable(option,type1);
          } 
              
          context, Appl(Option(optionList),name@Name(tomName),l) -> {
            //debugPrintln("pass1.6: Appl(Name(" + tomName + ")," + l + ")");
            //debugPrintln("\tcontext = " + context);
                
              // create a  symbol
            TomSymbol tomSymbol = getSymbol(tomName);
            if(tomSymbol != null) {
              TomList subterm = pass1List(tomSymbol, l);
                //System.out.println("***** pass1.6: pass1List = " + subterm);
              Option option = `Option(replaceAnnotedName(optionList,getSymbolCodomain(tomSymbol)));
              return `Appl(option,name,subterm);
            }
          }

          context, Variable[option=option,astName=Name(strName),astType=TomTypeAlone(tomType)] -> {
              // create a variable
            System.out.println("pass1.8: Variable(" + strName + "," + tomType + ")");
            TomType localType = getType(tomType);
            if(localType == null) { 
              messageMatchTypeVariableError(strName, tomType);
            } else {
              return `Variable(option,Name(strName),localType);
            }
          }
          
          context, TLVar(strName,TomTypeAlone(tomType)) -> {
            System.out.println("pass1.8: TLVar(" + strName + "," + tomType + ")");
                
              // create a variable
            TomType localType = getType(tomType);
            if ( localType == null ) { 
              messageMatchTypeVariableError(strName, tomType);
            } else {
              Option option = ast().makeOption();
              return `Variable(option,Name(strName),localType);
            }
          }
              
          SubjectList(l1), TermList(subjectList) -> {
            //debugPrintln("pass1.9: TermList(" + subjectList + ")");
                
              // process a list of subterms
            ArrayList list = new ArrayList();
            while(!subjectList.isEmpty()) {
              list.add(pass1(l1.getHead(), subjectList.getHead()));
              subjectList = subjectList.getTail();
              l1 = l1.getTail();
            }
            return `TermList(ast().makeList(list));
          }
              
          context, Match(option,tomSubjectList,patternList) -> {
            //debugPrintln("pass1.10: Match(" + tomSubjectList + "," + patternList + ")");
            optionMatchTypeVariable = option;
            TomTerm newSubjectList = pass1(context,tomSubjectList);
            TomTerm newPatternList = pass1(newSubjectList,patternList);
            return `Match(option,newSubjectList,newPatternList);
          }

            /*
          context, TomTypeToTomTerm(TomTypeAlone(tomType)) -> {
            //debugPrintln("pass1.12: TomTypeAlone(" + tomType + ")");
              // get a type
            return `TomTypeToTomTerm(getType(tomType));
          }
            */
          
          context, RewriteRule(Term(lhs@Appl(Option(optionList),Name(tomName),l)),Term(rhs), orgTrack) -> { 
            //debugPrintln("pass1.13: Rule(" + lhs + "," + rhs + ")");
            TomSymbol tomSymbol = getSymbol(tomName);
            if(tomSymbol != null) {
              TomType symbolType = getSymbolCodomain(tomSymbol);
              TomTerm newLhs = `Term(pass1(context,lhs));
              TomTerm newRhs = `Term(pass1(TomTypeToTomTerm(symbolType),rhs));
              return `RewriteRule(newLhs,newRhs,orgTrack);
            } else {
                //verifier().messageRuleSymbolError(tomName, optionList);
            }
          }
              
            // default rule
          context, t -> {
            //debugPrintln("pass1.11 default: " );
              //System.out.println("pass1 default:\n\t" + subject );
            return genericTraversalContext(contextSubject,subject,this);
          }
        } // end match
      } // end apply
    }; // end new

  public TomTerm pass1(TomTerm contextSubject, TomTerm subject) throws TomException {
    return (TomTerm) replacePass1.apply(contextSubject,subject); 
  }

  public TomList pass1List(TomSymbol subject, TomList subjectList) throws TomException {
    //%variable
    %match(TomSymbol subject) {
      symb@Symbol[typesToType=TypesToType(typeList,codomainType)] -> {
        //debugPrintln("pass1List.1: " + subjectList);
          
          // process a list of subterms and a list of types
        TomList result = null;
        ArrayList list = new ArrayList();
        if(isListOperator(symb) || isArrayOperator(symb)) {
          /*
           * TODO:
           * when the symbol is an associative operator,
           * the signature has the form: List conc( Element* )
           * the list of types is reduced to the singleton { Element }
           *
           * consider a pattern: conc(E1*,x,E2*,y,E3*)
           *  assign the type "Element" to each subterm: x and y
           *  assign the type "List" to each subtermList: E1*,E2* and E3*
           */

          //System.out.println("listOperator: " + symb);
          
          TomTerm domainType = typeList.getHead();
          while(!subjectList.isEmpty()) {
            TomTerm subterm = subjectList.getHead();

            matchBlock: {
              %match(TomTerm subterm) {
                VariableStar(voption,name,type) -> {
                  list.add(`VariableStar(voption,name,codomainType));
                    //System.out.println("*** break: " + subterm);
                  break matchBlock;
                }
                
                _ -> {
                  list.add(pass1(domainType, subterm));
                  break matchBlock;
                }
              }
            }
            subjectList = subjectList.getTail();
          }
        } else {
          while(!subjectList.isEmpty()) {
            list.add(pass1(typeList.getHead(), subjectList.getHead()));
            subjectList = subjectList.getTail();
            typeList    = typeList.getTail();
          }
        }
       
        result = ast().makeList(list);
        return result;
      }

      _ -> {
        System.out.println("pass1List: strange case: '" + subject + "'");
        System.exit(1);
      }
    }
    return null;
  }

    /*
     * updateSymbol is called after the expansion phase
     * this phase updates the symbolTable according to the typeTable
     * this is performed by recursively traversing each symbol
     * each TomTypeAlone is replace by the corresponding TomType
     */
  public void updateSymbol() {
    try {
      Iterator it = symbolTable().keySymbolIterator();
      while(it.hasNext()) {
        String tomName = (String)it.next();
        TomTerm emptyContext = null;
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        tomSymbol = pass1(emptyContext,`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
        symbolTable().putSymbol(tomName,tomSymbol);
      }
    } catch(TomException e) {
      System.out.println("updateSymbolPass1 error");
      System.exit(1);
    }
  }
  
  private TomSymbol getSymbol(String tomName) throws TomException {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    return tomSymbol;
  }

  private TomType getType(String tomName) {
    TomType tomType = symbolTable().getType(tomName);
    return tomType;
  }

    /*
     * TODO: X1*,name@Name(_),X2* -> { return name; }
     */
  private TomName getAnnotedName(TomList subjectList) {
    //%variable
    while(!subjectList.isEmpty()) {
      TomTerm subject = subjectList.getHead();
      %match(TomTerm subject) {
        TomNameToTomTerm(name@Name[]) -> { return name; }
      }
      subjectList = subjectList.getTail();
    }
    return null;
  }

  private OptionList replaceAnnotedName(OptionList subjectList, TomType type) {
    //%variable
    %match(OptionList subjectList) {
      EmptyOptionList() -> { return subjectList; }
      ConsOptionList(TomNameToOption(name@Name[]),l)   -> {
        return `ConsOptionList(
          TomTermToOption(Variable(ast().makeOption(),name,type)),
          replaceAnnotedName(l,type));
      }
      ConsOptionList(t,l) -> {
        return `ConsOptionList(t,replaceAnnotedName(l,type));
      }
    }
    return null;
  }
  
    /*
      testVariableWithoutParen is used in 'pass1' method of TomChecker.t.
      It is called before to transform Appl into Variable. Indeed when we create an Appl
      in 'PlainTerm' method of TomParser.jj, we do not known if it will be a variable or not.
      But variable with () is not a recommanded structure for a variable. So we add informations
      thanks to 'ast().makeLRParen(name.image))' which is added to options in
      the case of () [in 'PlainTerm' method of TomParser.jj]. When we transform Appl in Variable (when
      it is necessary), we test if 'LRParen(_)' is in the option structure. 
    */
  private void testVariableWithoutParen(Option option, String name) throws TomException {
    if(!Flags.doVerify) return;
    
    OptionList optionList = option.getOptionList();
    Option lrParen = getLRParen(optionList);
    if(lrParen!=null) {
      String nameLrParen = lrParen.getAstName().getString();
      if(name.equals(nameLrParen)) {
        Integer line = findOriginTrackingLine(name, optionList);
        messageVariableWithParenError(name,line);
      }
    }
  }

  private void messageMatchTypeVariableError(String name, String type) throws TomException {
    OptionList optionList = optionMatchTypeVariable.getOptionList();
    Integer line = findOriginTrackingLine("Match", optionList);
    String s = "Variable '" + name + "' has a wrong type:  '" + type + "' in %match construct";
    messageError(line,s);
  }
  private void messageVariableWithParenError( String  name, Integer line ) {
    if(Flags.noWarning) return;
    System.out.println("\n *** Warning *** Variable with () is not recommanded");
    System.out.println(" *** Variable '"+name+"' has () - Line : "+line);
  }
}
