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
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.engine.verifier;

import tom.engine.*;
import aterm.*;
import java.util.*;
import tom.library.traversal.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.zenon.*;
import tom.engine.adt.zenon.types.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.SymbolTable;

public class TomIlTools extends TomBase {

  // ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
  %include { adt/zenon/Zenon.tom }
  // ------------------------------------------------------------

  private SymbolTable symbolTable;
  private Verifier verifier; 


  public TomIlTools(Verifier verifier) {
    super();
    this.verifier = verifier;
    this.symbolTable = verifier.getSymbolTable();
  }

  private SymbolTable getSymbolTable() {
    return symbolTable;
  }

  /**
   * Methods used to translate a pattern and conditions in zenon signature
   */
  public ZExpr patternToZExpr(PatternList patternList, Map map) {
    // do everything match the empty pattern ?
    ZExpr result = `zfalse();
    while(!patternList.isEmpty()) {
      Pattern h = patternList.getHead();
      result = `zor(result,patternToZExpr(h,map));
      patternList = patternList.getTail();
    }
    return result;
  }

  public void getZTermSubjectListFromPattern(Pattern pattern, List list, Map map) {
    Set unamedVarSet = new HashSet();
    %match(Pattern pattern) {
      Pattern(subjectList,tomList,guards) -> {
          while(!subjectList.isEmpty()) {
            TomTerm head = subjectList.getHead();
            subjectList = subjectList.getTail();
            list.add(tomTermToZTerm(head,map,unamedVarSet));
          }
      }
    }
  }

  public ZExpr patternToZExpr(Pattern pattern, Map map) {
    Set unamedVariableSet = new HashSet();
    %match(Pattern pattern) {
      Pattern(subjectList,tomList,guards) -> {
          ZExpr result = patternToZExpr(subjectList, tomList, map, unamedVariableSet);
          // insert existential quantifiers for the unamed variables
          Iterator it = unamedVariableSet.iterator();
          while (it.hasNext()) {
            ZTerm var = (ZTerm) it.next();
            result = `zexists(var,ztype("T"),result);
          }
          return result;
      }
    }
    throw new TomRuntimeException("patternToZExpr : strange pattern " + pattern);
  }
  
  public ZExpr patternToZExpr(TomList subjectList, TomList tomList, Map map, Set unamedVariableSet) {
    /* for each TomTerm: builds a zeq : pattern = subject */
    ZExpr res = `ztrue;
    while(!tomList.isEmpty()) {
      TomTerm h = tomList.getHead();
      TomTerm subject = subjectList.getHead();
      tomList = tomList.getTail();
      subjectList = subjectList.getTail();
      res = `zand(res,zeq(tomTermToZTerm(h,map,unamedVariableSet),
                          tomTermToZTerm(subject,map,unamedVariableSet)));
    }
    return res;
  }
  
  public ZTerm tomTermToZTerm(TomTerm tomTerm, Map map, Set unamedVariableSet) {
    %match(TomTerm tomTerm) {
      TermAppl[nameList=concTomName(Name(name),_*),args=childrens] -> {
        // builds children list
        ZTermList zchild = `concZTerm();
        TomTerm hd = null;
        while (!`childrens.isEmpty()) {
          hd = `childrens.getHead();
          `childrens = `childrens.getTail();
          zchild = `concZTerm(zchild*,tomTermToZTerm(hd,map,unamedVariableSet));
        }
        // issue a warning here: this case is probably impossible
        return `zappl(zsymbol(name),zchild);
      }
      RecordAppl[nameList=concTomName(Name(name),_*),slots=childrens] -> {
        // builds a map: slotName / TomTerm
        Map definedSlotMap = new HashMap();
        Slot hd = null;
        while (!`childrens.isEmpty()) {
          hd = `childrens.getHead();
          `childrens = `childrens.getTail();
          definedSlotMap.put(hd.getSlotName(),hd.getAppl());
        }
        // builds children list
        ZTermList zchild = `concZTerm();
        // take care to add unamedVariables for wildcards
        TomSymbol symbol = getSymbolFromName(name,getSymbolTable());
        // process all slots from symbol
        %match(TomSymbol symbol) {
          Symbol[pairNameDeclList=slots] -> {
            // process all slots. If the slot is in childrens, use it
            while(!`slots.isEmpty()) {
              Declaration decl= `slots.getHead().getSlotDecl();
              `slots = `slots.getTail();
              %match(Declaration decl) {
                GetSlotDecl[slotName=slotName] -> {
                  if (definedSlotMap.containsKey(slotName)) {
                    zchild = `concZTerm(zchild*,tomTermToZTerm((TomTerm)definedSlotMap.get(slotName),map,unamedVariableSet));
                  } 
                  else {
                    // fake an UnamedVariable
                    zchild = `concZTerm(zchild*,tomTermToZTerm(
                          UnamedVariable(concOption(),EmptyType(),concConstraint()),
                          map,unamedVariableSet));
                  }
                }
              }
            }
          }
        }
        return `zappl(zsymbol(name),zchild);
      }
      Variable[astName=Name(name)] -> {
        if (map.containsKey(`name)) {
          return (ZTerm) map.get(`name);
        } else {
          System.out.println("tomTermToZTerm 1 Not in map: " + `name + " map: " + map);
          return `zvar(name);
        }
      }
      Variable[astName=PositionName(numberList)] -> {
        String name = verifier.tomNumberListToString(`numberList);
        if (map.containsKey(name)) {
          return (ZTerm) map.get(name);
        } else {
          System.out.println("tomTermToZTerm 2 Not in map: " + name + " map: " + map);
          return `zvar(name);
        }
      }      
      UnamedVariable[] -> {
        // for unamed variables in a pattern, we generate an existential
        // quantifier for a dummy name
        ZTerm unamedVariable = `zvar(replaceNumbersByString("unamedVariable"+unamedVariableSet.size()));
        unamedVariableSet.add(unamedVariable);
        return unamedVariable;
      }
      TLVar[strName=name] -> {
        return `zvar(name);
      }
    }
    throw new TomRuntimeException("tomTermToZTerm Strange pattern: " + tomTerm);
  }

  private Collect2 collect_symbols = new Collect2() {
      public boolean apply(ATerm subject, Object astore) {
        Collection store = (Collection)astore;
				%match(ZSymbol subject) {
					zsymbol(name)  -> {
						store.add(`name);
					}
				}
				return true;
      }
    }; 

  public Collection collectSymbols(ZExpr subject) {
    Collection result = new HashSet();
    traversal().genericCollect(subject,collect_symbols,result);
    return result;
  }
  public Collection collectSymbolsFromZSpec(ZSpec subject) {
    Collection result = new HashSet();
    traversal().genericCollect(subject,collect_symbols,result);
    return result;
  }

  public ZAxiomList symbolsDefinition(Collection symbolnames) {
    ZAxiomList res = `zby();
    Iterator it = symbolnames.iterator();
    while (it.hasNext()) {
      String name = (String) it.next();
      TomSymbol symbol = getSymbolFromName(name,getSymbolTable());
      ZTermList list = `concZTerm();
      ZTerm abstractVariable = `zvar("t");
      //ZExpr exists = null;
      %match(TomSymbol symbol) {
        Symbol[pairNameDeclList=slots] -> {
          // process all slots
          while(!`slots.isEmpty()) {
            Declaration hd= `slots.getHead().getSlotDecl();
            `slots = `slots.getTail();
            %match(Declaration hd) {
              GetSlotDecl[slotName=Name(slotName)] -> {
                list = `concZTerm(list*,zsl(abstractVariable,slotName));
              }
            }
          }
        }
      }

      ZExpr axiom = `zforall(abstractVariable,ztype("T"),
                             zequiv(
                               zisfsym(abstractVariable,zsymbol(name)),
                               zeq(abstractVariable,zappl(zsymbol(name),list))));
      res=`zby(res*,zaxiom("symb_"+replaceNumbersByString(name),axiom));
    }
    return res;
  } 

  public ZAxiomList subtermsDefinition(Collection symbolnames) {
    ZAxiomList res = `zby();
    Iterator it = symbolnames.iterator();
    while (it.hasNext()) {
      String name = (String) it.next();
      TomSymbol symbol = getSymbolFromName(name,getSymbolTable());
      ZTermList list = `concZTerm();
      %match(TomSymbol symbol) {
        Symbol[pairNameDeclList=slots] -> {
          // process all slots
          int slotnumber = `slots.getLength();
          for (int i = 0; i < slotnumber;i++) {
            list = `concZTerm(list*,zvar("x"+i));
          }
          %match(PairNameDeclList slots) {
            concPairNameDecl(al*,PairNameDecl[slotName=Name(slname)],_*) -> {
              int index = `al.getLength();
              ZExpr axiom = `zeq(zvar("x"+index),
                                 zsl(zappl(zsymbol(name),list),slname));
              for (int j = 0; j < slotnumber;j++) {
                axiom = `zforall(zvar("x"+j),ztype("T"),axiom);
              }
              res=`zby(res*,zaxiom("st_"+slname+"_"+replaceNumbersByString(name),axiom));
            }
          }
        }
      }
    }
    return res;
  } 

  public List subtermList(String symbolName) {
    List nameList = new LinkedList();

    TomSymbol symbol = getSymbolFromName(symbolName,getSymbolTable());
    
    %match(TomSymbol symbol) {
      Symbol[pairNameDeclList=slots] -> {
        %match(PairNameDeclList slots) {
          concPairNameDecl(al*,PairNameDecl[slotName=Name(slname)],_*) -> {
            nameList.add(`slname);
          }
        }
      }
    }
    return nameList;
  } 

  public String replaceNumbersByString(String input) {
    String output = input;
    output = output.replaceAll("0","zero");
    output = output.replaceAll("1","one");
    output = output.replaceAll("2","two");
    output = output.replaceAll("3","three");
    output = output.replaceAll("4","four");
    output = output.replaceAll("5","five");
    output = output.replaceAll("6","six");
    output = output.replaceAll("7","seven");
    output = output.replaceAll("8","eight");
    output = output.replaceAll("9","nine");
    output = output.replaceAll("\\\"","_sd_");
    output = output.replaceAll("True","z_true");
    output = output.replaceAll("False","z_false");
    return output;
  }

}
