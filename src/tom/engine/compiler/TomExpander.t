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

  public TomExpander(jtom.TomEnvironment environment) {
    super(environment);
  }

// ------------------------------------------------------------
  %include { Tom.signature }
// ------------------------------------------------------------

    /*
     * The 'expandTomSyntax' phase replaces each 'RecordAppl' by its classical term form:
     *     => The unused slots a replaced by placeholders
     * but also the BackQuoteTerm
     */
  
  public TomTerm expandTomSyntax(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
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

  private TomTerm expandRecordAppl(Option option, String tomName, TomList args) {
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
                newSubterm = expandTomSyntax(slotSubterm);
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
  
  private TomTerm expandBackQuoteTerm(TomTerm t) {
    Replace1 replaceSymbol = new Replace1() {
        public ATerm apply(ATerm t) {
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
                  } else if(isIntegerOperator(tomSymbol)) {
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
  private Replace2 replaceVariable = new Replace2() { 
      public ATerm apply(ATerm subject, Object arg1) {
        TomTerm contextSubject = (TomTerm)arg1;

        if(!(subject instanceof TomTerm)) {
          //debugPrintln("expandVariable not a tomTerm: " );
            //System.out.println("expandVariable not a tomTerm:\n\t" + subject );
          
          if(subject instanceof TomType) {
            %match(TomType subject) {
              TomTypeAlone(tomType) -> { return getType(tomType); }
            }
          }
          return genericTraversal(subject,this,contextSubject);
        }

          //System.out.println("expandVariable is a tomTerm:\n\t" + subject );
        
        %match(TomTerm contextSubject, TomTerm subject) {
          TomTypeToTomTerm(type@Type(tomType,glType)) , appl@Appl(Option(optionList),name@Name(strName),Empty()) -> {
            //debugPrintln("expandVariable.1: Type(" + tomType + "," + glType + ")");
            //debugPrintln("appl = " + appl);
            Option orgTrack = findOriginTracking(optionList);
            Option option = `Option(replaceAnnotedName(optionList,type,orgTrack));
              // create a constant or a variable
            TomSymbol tomSymbol = getSymbol(strName);
            if(tomSymbol != null) {
              return `Appl(option,name,Empty());
            } else {
              statistics().numberVariablesDetected++;
              return `Variable(option,name,type);
            }
          }
          
          Variable(option1,name1,type1) , appl@Appl(Option(optionList),name@Name(strName),Empty()) -> {
            //debugPrintln("expandVariable.3: Variable(" + option1 + "," + name1 + "," + type1 + ")");
            //debugPrintln("appl = " + appl);
            Option orgTrack = findOriginTracking(optionList);
            Option option = `Option(replaceAnnotedName(optionList,type1,orgTrack));
              // under a match construct
              // create a constant or a variable
            TomSymbol tomSymbol = getSymbol(strName);
            if(tomSymbol != null) {
              return `Appl(option,name,Empty());
            } else {
              statistics().numberVariablesDetected++;
              return `Variable(option,name,type1);
            }
          } 

          TomTypeToTomTerm(type@Type(tomType,glType)) ,p@Placeholder(Option(optionList)) -> {
            Option orgTrack = findOriginTracking(optionList);
            Option option = `Option(replaceAnnotedName(optionList,type,orgTrack));
              // create an unamed variable
            return `UnamedVariable(option,type);
          } 
              
          Variable(option1,name1,type1) , p@Placeholder(Option(optionList)) -> {
            Option orgTrack = findOriginTracking(optionList);
            Option option = `Option(replaceAnnotedName(optionList,type1,orgTrack));
              // create an unamed variable
            return `UnamedVariable(option,type1);
          } 
              
          context, appl@Appl(Option(optionList),name@Name(tomName),l) -> {
            //debugPrintln("expandVariable.6: Appl(Name(" + tomName + ")," + l + ")");
            //debugPrintln("\tcontext = " + context);
                
              // create a  symbol
            TomSymbol tomSymbol = getSymbol(tomName);
            if(tomSymbol != null) {
              TomList subterm = expandVariableList(tomSymbol, l);
                //System.out.println("***** expandVariable.6: expandVariableList = " + subterm);
              Option orgTrack = findOriginTracking(optionList);
              Option option = `Option(replaceAnnotedName(optionList,getSymbolCodomain(tomSymbol),orgTrack));
              return `Appl(option,name,subterm);
            }
          }

          context, Variable[option=option,astName=Name(strName),astType=TomTypeAlone(tomType)] -> {
              // create a variable
            TomType localType = getType(tomType);
            return `Variable(option,Name(strName),localType);
          }
          
          context, TLVar(strName,TomTypeAlone(tomType)) -> {
              //debugPrintln("expandVariable.8: TLVar(" + strName + "," + tomType + ")");
              // create a variable: its type is ensured by checker
            TomType localType = getType(tomType);
            Option option = ast().makeOption();
            return `Variable(option,Name(strName),localType);
          }
              
          SubjectList(l1), TermList(subjectList) -> {
            //debugPrintln("expandVariable.9: TermList(" + subjectList + ")");
                
              // process a list of subterms
            ArrayList list = new ArrayList();
            while(!subjectList.isEmpty()) {
              list.add(expandVariable(l1.getHead(), subjectList.getHead()));
              subjectList = subjectList.getTail();
              l1 = l1.getTail();
            }
            return `TermList(ast().makeList(list));
          }
              
          context, Match(option,tomSubjectList,patternList) -> {
            //debugPrintln("expandVariable.10: Match(" + tomSubjectList + "," + patternList + ")");
            TomTerm newSubjectList = expandVariable(context,tomSubjectList);
            TomTerm newPatternList = expandVariable(newSubjectList,patternList);
            return `Match(option,newSubjectList,newPatternList);
          }

          context, RewriteRule(Term(lhs@Appl(Option(optionList),Name(tomName),l)),
                               Term(rhs),
                               condList,
                               orgTrack) -> { 
            //debugPrintln("expandVariable.13: Rule(" + lhs + "," + rhs + ")");
            TomSymbol tomSymbol = getSymbol(tomName);
            TomType symbolType = getSymbolCodomain(tomSymbol);
            TomTerm newLhs = `Term(expandVariable(context,lhs));
            TomTerm newRhs = `Term(expandVariable(TomTypeToTomTerm(symbolType),rhs));
            TomList newCondList = empty();
            return `RewriteRule(newLhs,newRhs,newCondList,orgTrack);
          }
              
            // default rule
          context, t -> {
            //debugPrintln("expandVariable.11 default: " );
              //System.out.println("expandVariable default:\n\t" + subject );
            return genericTraversal(subject,this,contextSubject);
          }
        } // end match
      } // end apply
    }; // end new

  public TomTerm expandVariable(TomTerm contextSubject, TomTerm subject) {
    return (TomTerm) replaceVariable.apply(subject,contextSubject); 
  }

  public TomList expandVariableList(TomSymbol subject, TomList subjectList) {
    //%variable
    %match(TomSymbol subject) {
      symb@Symbol[typesToType=TypesToType(typeList,codomainType)] -> {
        //debugPrintln("expandVariableList.1: " + subjectList);
          
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
                  list.add(expandVariable(domainType, subterm));
                  break matchBlock;
                }
              }
            }
            subjectList = subjectList.getTail();
          }
        } else {
          while(!subjectList.isEmpty()) {
            list.add(expandVariable(typeList.getHead(), subjectList.getHead()));
            subjectList = subjectList.getTail();
            typeList    = typeList.getTail();
          }
        }
       
        result = ast().makeList(list);
        return result;
      }

      _ -> {
        System.out.println("expandVariableList: strange case: '" + subject + "'");
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
    Iterator it = symbolTable().keySymbolIterator();
    while(it.hasNext()) {
      String tomName = (String)it.next();
      TomTerm emptyContext = null;
      TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
      tomSymbol = expandVariable(emptyContext,`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
      symbolTable().putSymbol(tomName,tomSymbol);
    }
  }
  
  private TomSymbol getSymbol(String tomName) {
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

  private OptionList replaceAnnotedName(OptionList subjectList, TomType type, Option orgTrack) {
    //%variable
    %match(OptionList subjectList) {
      EmptyOptionList() -> { return subjectList; }
      ConsOptionList(TomNameToOption(name@Name[]),l)   -> {
        return `ConsOptionList(
          TomTermToOption(Variable(ast().makeOption(orgTrack),name,type)),
          replaceAnnotedName(l,type,orgTrack));
      }
      ConsOptionList(t,l) -> {
        return `ConsOptionList(t,replaceAnnotedName(l,type, orgTrack));
      }
    }
    return null;
  }
  
} // Class Tom Expander
