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

import jtom.TomBase;
import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.runtime.Replace1;
import jtom.runtime.Replace2;
import aterm.*;
import jtom.exception.TomRuntimeException;

public class TomKernelExpander extends TomBase {

  public TomKernelExpander(jtom.TomEnvironment environment) {
    super(environment);
  }

// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom } 
// ------------------------------------------------------------
  
    /*
     * replace Name by Symbol
     * replace Name by Variable
     */
  private Replace2 replaceVariable = new Replace2() { 
      public ATerm apply(ATerm subject, Object arg1) {
        TomTerm contextSubject = (TomTerm)arg1;

        if(contextSubject == null) {
          throw new TomRuntimeException(new Throwable("expandVariable: null contextSubject"));
        }

          //System.out.println("expandVariable:\n\t" + subject );

        if(!(subject instanceof TomTerm)) {
            //debugPrintln("expandVariable not a tomTerm: " );
            //System.out.println("expandVariable not a tomTerm:\n\t" + subject );
          if(subject instanceof TomType) {
            %match(TomType subject) {
              TomTypeAlone(tomType) -> {
                TomType type = getType(tomType);
                if(type != null) {
                  return type;
                } else {
                  return subject; // useful for TomTypeAlone("unknown type")
                }
              }
            }
          }
          return traversal().genericTraversal(subject,this,contextSubject);
        }

          //System.out.println("expandVariable is a tomTerm:\n\t" + subject );
        
        %match(TomTerm contextSubject, TomTerm subject) {
          TomTypeToTomTerm(type@Type[]) , Appl(optionList,nameList@(Name(strName),_*),l) -> {
              //debugPrintln("expandVariable.1: Type(" + tomType + "," + glType + ")");
            Option orgTrack = findOriginTracking(optionList);
            OptionList option = `replaceAnnotedName(optionList,type,orgTrack);
              // create a constant or a variable

              //TomSymbol tomSymbol = getSymbol(strName);
            TomSymbol tomSymbol;
            if(strName.equals("")) {
              tomSymbol = getSymbol(type);
              nameList = `concTomName(tomSymbol.getAstName());
            } else {
              tomSymbol = getSymbol(strName);
            }

            if(tomSymbol != null) {
              TomList subterm = expandVariableList(tomSymbol, l);
              return `Appl(option,nameList,subterm);
            } else {
              if(l.isEmpty()  && !hasConstructor(optionList)) {
                return `Variable(option,nameList.getHead(),type);
              } else {
                TomList subterm = expandVariableList(`emptySymbol(), l);
                return `Appl(option,nameList,subterm);
              }
            }

            
          }
          
          Variable(option1,name1,type1) , Appl(optionList,nameList@(Name(strName),_*),l) -> {
              //System.out.println("expandVariable.3: Variable(" + option1 + "," + name1 + "," + type1 + ")");
            Option orgTrack = findOriginTracking(optionList);
            OptionList option = `replaceAnnotedName(optionList,type1,orgTrack);
              // under a match construct
              // create a constant or a variable
              //TomSymbol tomSymbol = getSymbol(strName);
            TomSymbol tomSymbol;
            if(strName.equals("")) {
              tomSymbol = getSymbol(type1);
              nameList = `concTomName(tomSymbol.getAstName());
            } else {
              tomSymbol = getSymbol(strName);
            }

            if(tomSymbol != null) {
              TomList subterm = expandVariableList(tomSymbol, l);
              return `Appl(option,nameList,subterm);
            } else {
              if(l.isEmpty()  && !hasConstructor(optionList)) {
                return `Variable(option,nameList.getHead(),type1);
              } else {
                TomList subterm = expandVariableList(`emptySymbol(), l);
                return `Appl(option,nameList,subterm);
              }
            }

              /*
            if(tomSymbol != null) {
            	//System.out.println("----->Symbol:"+tomSymbol+ "\n-->args:"+l);
              TomList subterm = expandVariableList(tomSymbol, l);
              return `Appl(option,nameList,subterm);
            } else if(l.isEmpty()) {
              return `Variable(option,nameList.getHead(),type1);
            }
              */
            
          } 

          TomTypeToTomTerm(type@Type(tomType,glType)) ,p@Placeholder(optionList) -> {
            Option orgTrack = findOriginTracking(optionList);
            OptionList option = `replaceAnnotedName(optionList,type,orgTrack);
              // create an unamed variable
            return `UnamedVariable(option,type);
          } 
              
          Variable(option1,name1,type1) , p@Placeholder(optionList) -> {
            Option orgTrack = findOriginTracking(optionList);
            OptionList option = `replaceAnnotedName(optionList,type1,orgTrack);
              // create an unamed variable
            return `UnamedVariable(option,type1);
          } 

          context, appl@Appl(optionList,nameList@(Name(tomName),_*),l) -> {
               //System.out.println("expandVariable.6: Appl(Name(" + tomName + ")," + l + ")");
              // create a  symbol
            TomSymbol tomSymbol = getSymbol(tomName);
            if(tomSymbol != null) {
              TomList subterm = expandVariableList(tomSymbol, l);
                //System.out.println("***** expandVariable.6: expandVariableList = " + subterm);
              Option orgTrack = findOriginTracking(optionList);
              OptionList option = `replaceAnnotedName(optionList,getSymbolCodomain(tomSymbol),orgTrack);
              return `Appl(option,nameList,subterm);
            } else {
                // do nothing
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
            OptionList option = ast().makeOption();
            return `Variable(option,Name(strName),localType);
          }
              
          SubjectList(l1), TermList(subjectList) -> {
            //System.out.println("expandVariable.9: "+l1+"(" + subjectList + ")");
                
              // process a list of subterms
            ArrayList list = new ArrayList();
            while(!subjectList.isEmpty()) {
              list.add(expandVariable(l1.getHead(), subjectList.getHead()));
              subjectList = subjectList.getTail();
              l1 = l1.getTail();
            }
            return `TermList(ast().makeList(list));
          }
              
          context, Match(tomSubjectList,patternList, option) -> {
            //debugPrintln("expandVariable.10: Match(" + tomSubjectList + "," + patternList + ")");
            TomTerm newSubjectList = expandVariable(context,tomSubjectList);
            TomTerm newPatternList = expandVariable(newSubjectList,patternList);
            return `Match(newSubjectList,newPatternList, option);
          }
              
            // default rule
          context, t -> {
            //debugPrintln("expandVariable.11 default: " );
            //System.out.println("expandVariable default:\n\t" + subject );
            return traversal().genericTraversal(subject,this,contextSubject);
          }
        } // end match
      } // end apply
    }; // end new

  public TomTerm expandVariable(TomTerm contextSubject, TomTerm subject) {
    return (TomTerm) replaceVariable.apply(subject,contextSubject); 
  }

  public TomList expandVariableList(TomSymbol subject, TomList subjectList) {
    //%variable
    if(subject == null) {
      throw new TomRuntimeException(new Throwable("expandVariableList: null subject"));
    }
    
    %match(TomSymbol subject) {

      emptySymbol() -> {
          /*
           * If the top symbol is unknown, the subterms
           * are expanded in an empty context
           */
        ArrayList list = new ArrayList();
        while(!subjectList.isEmpty()) {
          list.add(expandVariable(`emptyTerm(), subjectList.getHead()));
          subjectList = subjectList.getTail();
        }
        return ast().makeList(list);
      }

      symb@Symbol[typesToType=TypesToType(typeList,codomainType)] -> {
          
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
          //System.out.println("subjectList: " + subjectList);
          
          TomType domainType = typeList.getHead();
          while(!subjectList.isEmpty()) {
            TomTerm subterm = subjectList.getHead();
              //System.out.println("subterm:\n" + subterm);
            matchBlock: {
              %match(TomTerm subterm) {
                VariableStar(optionList,name,_) -> {
                  Option orgTrack = findOriginTracking(optionList);
                  OptionList option = `replaceAnnotedName(optionList,codomainType,orgTrack);
                  list.add(`VariableStar(option,name,codomainType));
                    //System.out.println("*** break: " + subterm);
                  break matchBlock;
                }
                
                UnamedVariableStar(optionList,_) -> {
                  Option orgTrack = findOriginTracking(optionList);
                  OptionList option = `replaceAnnotedName(optionList,codomainType,orgTrack);
                  list.add(`UnamedVariableStar(option,codomainType));
                  break matchBlock;
                }
                
                _ -> {
                  list.add(expandVariable(`TomTypeToTomTerm(domainType), subterm));
                  break matchBlock;
                }
              }
            }
            subjectList = subjectList.getTail();
          }
        } else {
          while(!subjectList.isEmpty()) {
            list.add(expandVariable(`TomTypeToTomTerm(typeList.getHead()), subjectList.getHead()));
            subjectList = subjectList.getTail();
            typeList    = typeList.getTail();
          }
        }
       
        result = ast().makeList(list);
        return result;
      }

      _ -> {
        System.out.println("expandVariableList: strange case: '" + subject + "'");
        throw new TomRuntimeException(new Throwable("expandVariableList: strange case: '" + subject + "'"));
      }
    }
  }

    /*
     * updateSymbol is called after a first syntax expansion phase
     * this phase updates the symbolTable according to the typeTable
     * this is performed by recursively traversing each symbol
     * each TomTypeAlone is replace by the corresponding TomType
     */
  public void updateSymbolTable() {
    Iterator it = symbolTable().keySymbolIterator();
    while(it.hasNext()) {
      String tomName = (String)it.next();
      TomTerm emptyContext = `emptyTerm();
      TomSymbol tomSymbol = getSymbol(tomName);
      tomSymbol = expandVariable(emptyContext,`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
      symbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  private TomType getType(String tomName) {
    TomType tomType = symbolTable().getType(tomName);
    return tomType;
  }


  private OptionList replaceAnnotedName(OptionList subjectList, TomType type, Option orgTrack) {
    //%variable
    %match(OptionList subjectList) {
      emptyOptionList() -> { return subjectList; }
      manyOptionList(TomNameToOption(name@Name[]),l)   -> {
        return `manyOptionList(
          TomTermToOption(Variable(ast().makeOption(orgTrack),name,type)),
          replaceAnnotedName(l,type,orgTrack));
      }
      manyOptionList(t,l) -> {
        return `manyOptionList(t,replaceAnnotedName(l,type, orgTrack));
      }
    }
    return null;
  }
  
    /*
     * Replace pattern with only variables or underscore (UnamedVariables)
     * By DefaultPattern
     */
  public TomTerm expandMatchPattern(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {
              m@Match(subjectList,patternList, option) -> {
                  // find other match in PA list
                TomTerm newPatternList = expandMatchPattern(patternList);
                return expandPattern(`Match(subjectList, newPatternList, option)); 
              } 
              _  -> {
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

  private TomTerm expandPattern(TomTerm match) {
    %match(TomTerm match) {
      Match(subjectList,PatternList(list), option) -> {
        boolean needModification = false;
        TomList newPatternList = empty();
        while(!list.isEmpty()) {
          TomTerm pa = list.getHead();
          if( isDefaultPattern(pa.getTermList().getTomList()) ) {
            OptionList newPatternActionOption =  `manyOptionList(DefaultCase(),pa.getOption());
            newPatternList = cons(`PatternAction(pa.getTermList(), pa.getTom(), newPatternActionOption), newPatternList);
            needModification = true;
            if(!list.getTail().isEmpty()) {
                // the default pattern is not the latest one!!
              System.out.println("Default pattern issue"+pa.getOption());
            }
          } else {
              // we keep the PA
            newPatternList = cons(list.getHead(), newPatternList);
          }
          list = list.getTail();
        }
        if(needModification) {
          newPatternList = reverse(newPatternList);
          OptionList newMatchOption =`manyOptionList(DefaultCase(),option);
          return `Match(subjectList, PatternList(newPatternList), newMatchOption);
        } else {
          return match;
        }
      }
      _ -> {
        System.out.println("Strange Match in expandMatchPattern"+match);
		throw new TomRuntimeException(new Throwable("Strange Match in expandMatchPattern"+match));
      }
    }
  }

  private boolean isDefaultPattern(TomList pList) {
    TomTerm term;
    while(!pList.isEmpty()) {
      term = pList.getHead();
      %match(TomTerm term) {
        Appl[] -> {
          return false;
        }
      }
      pList = pList.getTail();
    }
    
    ArrayList variableList = new ArrayList();
    collectVariable(variableList,`PatternList(pList));
    
      // compute multiplicities
    HashMap multiplicityMap = new HashMap();
    Iterator it = variableList.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
      if(multiplicityMap.containsKey(name)) {
        Integer value = (Integer)multiplicityMap.get(name);
        return false;
      } else {
        multiplicityMap.put(name, new Integer(1));
      }
    }
    return true;
  }
  
} // Class TomKernelExpander
