/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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
 * Antoine Reilles
 *
 **/

package jtom.verifier;

import jtom.*;
import aterm.*;
import java.util.*;
import tom.library.traversal.*;
import jtom.adt.tomsignature.types.*;
import jtom.verifier.zenon.*;
import jtom.verifier.zenon.types.*;
import jtom.exception.TomRuntimeException;
import jtom.tools.SymbolTable;

public class TomIlTools extends TomBase {

  // ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
  %include { zenon/Zenon.tom }
  // ------------------------------------------------------------

  private SymbolTable symbolTable;
  private Verifier verifier; 

  protected jtom.verifier.zenon.ZenonFactory zfactory;

  public TomIlTools(Verifier verifier) {
    super();
    this.verifier = verifier;
    this.symbolTable = verifier.getSymbolTable();
    zfactory = ZenonFactory.getInstance(getTomSignatureFactory().getPureFactory());
  }

  protected final ZenonFactory getZenonFactory() {
    return zfactory;
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
    %match(Pattern pattern) {
      Pattern(subjectList,tomList,guards) -> {
          while(!subjectList.isEmpty()) {
            TomTerm head = subjectList.getHead();
            subjectList = subjectList.getTail();
            list.add(tomTermToZTerm(head,map));
          }
      }
    }
  }

  public ZExpr patternToZExpr(Pattern pattern, Map map) {
    %match(Pattern pattern) {
      Pattern(subjectList,tomList,guards) -> {
          return patternToZExpr(subjectList, tomList, map);
      }
    }
    throw new TomRuntimeException("patternToZExpr : strange pattern " + pattern);
  }
  
  public ZExpr patternToZExpr(TomList subjectList, TomList tomList, Map map) {
    /* for each TomTerm: builds a zeq : pattern = subject */
    ZExpr res = `ztrue;
    while(!tomList.isEmpty()) {
      TomTerm h = tomList.getHead();
      TomTerm subject = subjectList.getHead();
      tomList = tomList.getTail();
      subjectList = subjectList.getTail();
      res = `zand(res,zeq(tomTermToZTerm(h,map),tomTermToZTerm(subject,map)));
    }
    return res;
  }
  
  public ZTerm tomTermToZTerm(TomTerm tomTerm, Map map) {
    %match(TomTerm tomTerm) {
      TermAppl[nameList=concTomName(Name(name),_*),args=childrens] -> {
        // builds children list
        ZTermList zchild = `concZTerm();
        TomTerm hd = null;
        while (!childrens.isEmpty()) {
          hd = childrens.getHead();
          childrens = childrens.getTail();
          zchild = `concZTerm(zchild*,tomTermToZTerm(hd,map));
        }
        // issue a warning here: this case is probably impossible
        return `zappl(zsymbol(name),zchild);
      }
      RecordAppl[nameList=concTomName(Name(name),_*),slots=childrens] -> {
        // builds children list
        ZTermList zchild = `concZTerm();
        TomTerm hd = null;
        while (!childrens.isEmpty()) {
          hd = childrens.getHead().getAppl();
          childrens = childrens.getTail();
          zchild = `concZTerm(zchild*,tomTermToZTerm(hd,map));
        }
        return `zappl(zsymbol(name),zchild);
      }
      Variable[astName=Name(name)] -> {
        if (map.containsKey(name)) {
          return (ZTerm) map.get(name);
        } else {
          System.out.println("Not in map: " + `name + " map: " + map);
          return `zvar(name);
        }
      }
      Variable[astName=PositionName(numberList)] -> {
        String name = verifier.tomNumberListToString(numberList);
        if (map.containsKey(name)) {
          return (ZTerm) map.get(name);
        } else {
          System.out.println("Not in map: " + `name + " map: " + map);
          return `zvar(name);
        }
      }      
      UnamedVariable[] -> {
        return `zvar("_");
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
        if (subject instanceof ZSymbol) {
          %match(ZSymbol subject) {
            zsymbol(name)  -> {
              store.add(name);
            }
            _ -> { return true; }
          }
        } else { 
          return true;
        }
      }
    }; 
  public Collection collectSymbols(ZExpr subject) {
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
      ZExpr exists = null;
      %match(TomSymbol symbol) {
        Symbol[pairNameDeclList=slots] -> {
          // process all slots
          int slotnumber = slots.getLength();
          for (int i = 0; i < slotnumber;i++) {
            list = `concZTerm(list*,zvar("x"+i));
          }
          exists = `zeq(zvar("t"),zappl(zsymbol(name),list));
          for (int i = 0; i < slotnumber;i++) {
            exists = `zexists(zvar("x"+i),ztype("T"),exists);
          }
        }
      }
      ZExpr axiom = `zforall(zvar("t"),ztype("T"),
                             zequiv(
                               zisfsym(zvar("t"),
                               zsymbol(name)),exists));
      res=`zby(res*,zaxiom("symb_"+name,axiom));
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
          int slotnumber = slots.getLength();
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
              res=`zby(res*,zaxiom("st_"+slname+"_"+name,axiom));
            }
          }
        }
      }
    }
    return res;
  } 

}

