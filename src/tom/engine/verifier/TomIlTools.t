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

  protected jtom.verifier.zenon.ZenonFactory zfactory;

  public TomIlTools(SymbolTable symbolTable) {
    super();
    this.symbolTable = symbolTable;
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
  public ZExpr pattern_to_ZExpr(ZTerm input, ATerm patternList, Map map) {
    return pattern_to_ZExpr(input, (PatternList) patternList, map);
  }

  public ZExpr pattern_to_ZExpr(ZTerm input, PatternList patternList, Map map) {
    // do everything match the empty pattern ?
    ZExpr result = `ztrue();
    Pattern h = null;
    PatternList tail = patternList;
    if(!tail.isEmpty()) {
      h = tail.getHead();
      tail = tail.getTail();
      result = pattern_to_ZExpr(input, h,map);
    }

    while(!tail.isEmpty()) {
      h = tail.getHead();
      result = `zor(result,pattern_to_ZExpr(input, h,map));
      tail = tail.getTail();
    }
    return result;
  }

  public ZExpr pattern_to_ZExpr(ZTerm input, Pattern pattern, Map map) {
    %match(Pattern pattern) {
      Pattern(tomList,guards) -> {
          return pattern_to_ZExpr(input, tomList, map);
      }
    }
    throw new TomRuntimeException("pattern_to_ZExpr : strange pattern " + pattern);
  }
  
  public ZExpr pattern_to_ZExpr(ZTerm input, TomList tomList, Map map) {
    /* for each TomTerm: builds a zeq : pattern = first var in map */
    ZExpr res = null;
    TomTerm h = null;
    TomList tail = tomList;    
    if(!tail.isEmpty()) {
      h = tail.getHead();
      tail = tail.getTail();
      res = `zeq(tomTerm_to_ZTerm(h,map),input);
    }
    while(!tail.isEmpty()) {
      h = tail.getHead();
      tail = tail.getTail();
      res = `zand(res,zeq(tomTerm_to_ZTerm(h,map),input));
    }
    return res;
  }
  
  public ZTerm tomTerm_to_ZTerm(TomTerm tomTerm, Map map) {
    %match(TomTerm tomTerm) {
      TermAppl(_,concTomName(Name(name),_*),childrens,_) -> {
        // builds children list
        ZTermList zchild = `concZTerm();
        TomTerm hd = null;
        while (!childrens.isEmpty()) {
          hd = childrens.getHead();
          childrens = childrens.getTail();
          zchild = `concZTerm(zchild*,tomTerm_to_ZTerm(hd,map));
        }
        // issue a warning here: this case is probably impossible
        return `zappl(zsymbol(name),zchild);
      }
      RecordAppl(_,concTomName(Name(name),_*),childrens,_) -> {
        // builds children list
        ZTermList zchild = `concZTerm();
        TomTerm hd = null;
        while (!childrens.isEmpty()) {
          hd = childrens.getHead().getAppl();
          childrens = childrens.getTail();
          zchild = `concZTerm(zchild*,tomTerm_to_ZTerm(hd,map));
        }
        return `zappl(zsymbol(name),zchild);
      }
      Variable(_,Name(name),_,_) -> {
        if (map.containsKey(name)) {
          //System.out.println("In map: "+ map.containsKey(name));
          return (ZTerm) map.get(name);
        } else {
          System.out.println("Not in map: " + `name);
          return `zvar(name);
        }
      }
      UnamedVariable[] -> {
        return `zvar("_");
      }
    }
    throw new TomRuntimeException("tomTerm_to_ZTerm Strange pattern: " + tomTerm);
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

