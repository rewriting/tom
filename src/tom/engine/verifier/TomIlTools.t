/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2009, INRIA
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
import java.util.*;

import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.tools.ASTFactory;
import tom.library.sl.*;

import tom.engine.adt.zenon.types.*;

import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;

public class TomIlTools {

  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../adt/zenon/Zenon.tom }
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Set.tom}
  %include { ../../library/mapping/java/util/types/Map.tom}
  %include { ../../library/mapping/java/util/types/List.tom}
  %typeterm StringCollection {
    implement { java.util.Collection<String> }
    is_sort(t) { ($t instanceof java.util.Collection) }
  }
  %typeterm TomTermList {
    implement { java.util.List<TomTerm> }
    is_sort(t) { $t instanceof java.util.List }  
    equals(l1,l2) { $l1.equals($l2) }
  }
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
  public ZExpr constraintToZExpr(ConstraintList constraintList, Map<String,ZTerm> map) {
    // do everything match the empty pattern ?
    ZExpr result = `zfalse();
    while(!constraintList.isEmptyconcConstraint()) {
      Constraint h = constraintList.getHeadconcConstraint();
      result = `zor(result,constraintToZExpr(h,map));
      constraintList = constraintList.getTailconcConstraint();
    }
    return result;
  }

  public void getZTermSubjectListFromConstraint(Constraint constraint, List<ZTerm> list, Map<String,ZTerm> map) {
    Set<ZTerm> unamedVarSet = new HashSet<ZTerm>();
    try{
      ArrayList<TomTerm> tmpList = new ArrayList<TomTerm>();
      `TopDown(CollectSubjects(tmpList)).visitLight(constraint);
      for(TomTerm t:tmpList){
        list.add(tomTermToZTerm(t,map,unamedVarSet));
      }
    } catch(VisitFailure e) {
      throw new TomRuntimeException("VisiFailure in TomIlTools.getZTermSubjectListFromConstraint: " + e.getMessage());
    }
  }
  
  %strategy CollectSubjects(TomTermList list) extends Identity(){
    visit Constraint {
      MatchConstraint(_,s) -> {
        list.add(`s);
      }
    }
  }

  public ZExpr constraintToZExpr(Constraint constraint, Map<String,ZTerm> map) {
    Set<ZTerm> unamedVariableSet = new HashSet<ZTerm>();
    ArrayList<TomTerm> subjectList = new ArrayList<TomTerm>();
    ArrayList<TomTerm> patternList = new ArrayList<TomTerm>();
    try{
      `TopDown(CollectSubjectsAndPatterns(subjectList,patternList)).visitLight(constraint);
    } catch(VisitFailure e) {
      throw new TomRuntimeException("VisiFailure in TomIlTools.constraintToZExpr: " + e.getMessage());
    }
    ZExpr result = `constraintToZExpr(ASTFactory.makeList(subjectList), ASTFactory.makeList(patternList), map, unamedVariableSet);
    // insert existential quantifiers for the unamed variables
    for (ZTerm var : unamedVariableSet) {
      result = `zexists(var,ztype("T"),result);
    }
    return result;

  }
  
  %strategy CollectSubjectsAndPatterns(TomTermList subjectlist, TomTermList patternList) extends Identity(){
    visit Constraint {
      MatchConstraint(p,s) -> {
        subjectlist.add(`s);
        patternList.add(`p);
      }
    }
  }

  public ZExpr constraintToZExpr(TomList subjectList, TomList tomList, Map<String,ZTerm> map, Set<ZTerm> unamedVariableSet) {
    /* for each TomTerm: builds a zeq : pattern = subject */
    ZExpr res = `ztrue();
    while(!tomList.isEmptyconcTomTerm()) {
      TomTerm h = tomList.getHeadconcTomTerm();
      TomTerm subject = subjectList.getHeadconcTomTerm();
      tomList = tomList.getTailconcTomTerm();
      subjectList = subjectList.getTailconcTomTerm();
      res = `zand(res,zeq(tomTermToZTerm(h,map,unamedVariableSet),
                          tomTermToZTerm(subject,map,unamedVariableSet)));
    }
    return res;
  }

  public ZTerm tomTermToZTerm(TomTerm tomTerm, Map<String,ZTerm> map, Set<ZTerm> unamedVariableSet) {
    %match(tomTerm) {
      TermAppl[NameList=concTomName(Name(name),_*),Args=childrens] -> {
        // builds children list
        ZTermList zchild = `concZTerm();
        TomTerm hd = null;
        while (!`childrens.isEmptyconcTomTerm()) {
          hd = `childrens.getHeadconcTomTerm();
          `childrens = `childrens.getTailconcTomTerm();
          zchild = `concZTerm(zchild*,tomTermToZTerm(hd,map,unamedVariableSet));
        }
        // issue a warning here: this case is probably impossible
        return `zappl(zsymbol(name),zchild);
      }
      RecordAppl[NameList=concTomName(Name(name),_*),Slots=childrens] -> {
        // builds a map: slotName / TomTerm
        Map<TomName,TomTerm> definedSlotMap = new HashMap<TomName,TomTerm>();
        Slot hd = null;
        while (!`childrens.isEmptyconcSlot()) {
          hd = `childrens.getHeadconcSlot();
          `childrens = `childrens.getTailconcSlot();
          definedSlotMap.put(hd.getSlotName(),hd.getAppl());
        }
        // builds children list
        ZTermList zchild = `concZTerm();
        // take care to add unamedVariables for wildcards
        TomSymbol symbol = TomBase.getSymbolFromName(`name,getSymbolTable());
        // process all slots from symbol
        %match(symbol) {
          Symbol[PairNameDeclList=slots] -> {
            // process all slots. If the slot is in childrens, use it
            while(!`slots.isEmptyconcPairNameDecl()) {
              Declaration decl= `slots.getHeadconcPairNameDecl().getSlotDecl();
              `slots = `slots.getTailconcPairNameDecl();
              %match(decl) {
                GetSlotDecl[SlotName=slotName] -> {
                  if (definedSlotMap.containsKey(`slotName)) {
                    zchild = `concZTerm(zchild*,tomTermToZTerm(definedSlotMap.get(slotName),map,unamedVariableSet));
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
      Variable[AstName=Name(name)] -> {
        if (map.containsKey(`name)) {
          return map.get(`name);
        } else {
          System.out.println("tomTermToZTerm 1 Not in map: " + `name + " map: " + map);
          return `zvar(name);
        }
      }
      Variable[AstName=PositionName(numberList)] -> {
        String name = TomBase.tomNumberListToString(`numberList);
        if (map.containsKey(name)) {
          return map.get(name);
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
    }
    throw new TomRuntimeException("tomTermToZTerm Strange pattern: " + tomTerm);
  }

  %strategy collect_symbols(store:StringCollection) extends `Identity() {
    visit ZSymbol {
      zsymbol(name)  -> {
        store.add(`name);
      }
    }
  }

  public Collection<String> collectSymbols(ZExpr subject) {
    Collection<String> result = new HashSet<String>();
    try {
      `TopDown(collect_symbols(result)).visitLight(subject);
    } catch (tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("Strategy collect_symbols failed");
    }
    return result;
  }
  public Collection<String> collectSymbolsFromZSpec(ZSpec subject) {
    Collection<String> result = new HashSet<String>();
    try {
      `TopDown(collect_symbols(result)).visitLight(subject);
    } catch (tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("Strategy collect_symbols failed");
    }
    return result;
  }

  public ZAxiomList symbolsDefinition(Collection<String> symbolnames) {
    ZAxiomList res = `zby();
    for (String name : symbolnames) {
      TomSymbol symbol = TomBase.getSymbolFromName(name,getSymbolTable());
      ZTermList list = `concZTerm();
      ZTerm abstractVariable = `zvar("t");
      //ZExpr exists = null;
      %match(symbol) {
        Symbol[PairNameDeclList=slots] -> {
          // process all slots
          while(!`slots.isEmptyconcPairNameDecl()) {
            Declaration hd= `slots.getHeadconcPairNameDecl().getSlotDecl();
            `slots = `slots.getTailconcPairNameDecl();
            %match(hd) {
              GetSlotDecl[SlotName=Name(slotName)] -> {
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

  public ZAxiomList subtermsDefinition(Collection<String> symbolnames) {
    ZAxiomList res = `zby();
    for (String name : symbolnames) {
      TomSymbol symbol = TomBase.getSymbolFromName(name,getSymbolTable());
      ZTermList list = `concZTerm();
      %match(symbol) {
        Symbol[PairNameDeclList=slots] -> {
          // process all slots
          int slotnumber =`slots.length();
          for (int i = 0; i < slotnumber;i++) {
            list = `concZTerm(list*,zvar("x"+i));
          }
          %match(slots) {
            concPairNameDecl(al*,PairNameDecl[SlotName=Name(slname)],_*) -> {
              int index = `al.length();
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

  public List<String> subtermList(String symbolName) {
    List<String> nameList = new LinkedList<String>();

    TomSymbol symbol = TomBase.getSymbolFromName(symbolName,getSymbolTable());

    %match(symbol) {
      Symbol[PairNameDeclList=slots] -> {
        %match(slots) {
          concPairNameDecl(_*,PairNameDecl[SlotName=Name(slname)],_*) -> {
            nameList.add(`slname);
          }
        }
      }
    }
    return nameList;
  }

  public String replaceNumbersByString(String input) {
    String output = input;
    output = output.replace("0","zero");
    output = output.replace("1","one");
    output = output.replace("2","two");
    output = output.replace("3","three");
    output = output.replace("4","four");
    output = output.replace("5","five");
    output = output.replace("6","six");
    output = output.replace("7","seven");
    output = output.replace("8","eight");
    output = output.replace("9","nine");
    output = output.replace("\\\"","_sd_");
    output = output.replace("True","z_true");
    output = output.replace("False","z_false");
    return output;
  }

}
