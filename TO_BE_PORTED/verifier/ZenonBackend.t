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
import aterm.*;
import aterm.pure.*;
import java.util.*;
import tom.engine.adt.zenon.*;
import tom.engine.adt.zenon.types.*;

public class ZenonBackend {

  // ------------------------------------------------------------
  %include { ../adt/zenon/Zenon.tom }
  // ------------------------------------------------------------

  private Verifier verifier; // is it useful ?
  private TomIlTools tomiltools;

  public ZenonBackend(Verifier verifier) {
    this.verifier = verifier;
    this.tomiltools = new TomIlTools(verifier);
  }

  public String genZSymbol(ZSymbol symbol) {
    %match(symbol) {
      zsymbol(name) -> {
        // manage builtins
        String symbolName = tomiltools.replaceNumbersByString(`name);
        return `symbolName + "_";
      }
    }
    return "errorZSymbol";
  }

  public String genZType(ZType type) {
    %match(type) {
      ztype(name) -> {
        return `name;
      }
    }
    return "errorZType";
  }

  public String genZTerm(ZTerm term) {
    %match(term) {
      zvar(name) -> { return `name; }
      zappl(zsymbol(name),tlist) -> { 
        // manage builtins
        String realName = tomiltools.replaceNumbersByString(`name);
        return "(" + realName +" "+genZTermList(`tlist)+")"; 
      }
      zst(t,idx) -> { 
        return "(_"+`idx+" "+genZTerm(`t)+")";
      }
      zsl(t,slot) -> { 
        return "(_"+`slot+" "+genZTerm(`t)+")";
      }
    }
    return "errorZTerm";
  }

  public String genZTermList(ZTermList tl) {
    StringBuilder res = new StringBuilder();
    while (!tl.isEmptyconcZTerm()) {
      res.append(" "+genZTerm(tl.getHeadconcZTerm()));
      tl = tl.getTailconcZTerm();
    }
    return res.toString();
  }

  public String genZExpr(ZExpr expr) {
    %match(expr) {
      ztrue()  -> { return "True";}
      zfalse() -> { return "False";}
      zisfsym(t,s) -> {
        return "((symb "+genZTerm(`t)+") = "+genZSymbol(`s)+")";
      }
      zeq(l,r) -> {
        return "("+genZTerm(`l)+" = "+genZTerm(`r)+")";
      }
      zforall(v,type,e) -> {
        return "forall "+genZTerm(`v)+" : "+genZType(`type)+",\n "+genZExpr(`e);
      }
      zexists(v,type,e) -> {
        return "exists "+genZTerm(`v)+" : "+genZType(`type)+", "+genZExpr(`e);
      }
      zand(l,r) -> {
        if(`l == `ztrue()) {
          return "("+ genZExpr(`r) +")";
        }
        else if (`r == `ztrue()) {
          return "("+ genZExpr(`l) +")";
        }
        return "("+genZExpr(`l)+") /\\ ("+genZExpr(`r)+")";
      }
      zor(l,r) -> {
        if(`l == `zfalse()) {
          return "("+ genZExpr(`r) +")";
        }
        else if (`r == `zfalse()) {
          return "("+ genZExpr(`l) +")";
        }
        return "("+genZExpr(`l)+") \\/ ("+genZExpr(`r)+")";
      }
      znot(e) -> { return "~("+genZExpr(`e)+")"; }
      zequiv(l,r) -> {
        return "("+genZExpr(`l)+") <-> ("+genZExpr(`r)+")";
      }
    }
    return "errorZExpr";
  }

  public String genZAxiom(ZAxiom axiom) {
    %match(axiom) {
      zaxiom(name,ax) -> {
        // manage builtins
        String realName = tomiltools.replaceNumbersByString(`name);
        return "Parameter " + realName +" :\n    " + genZExpr(`ax) + ".\n";
      }
    }
    return "errorZAxiom";
  }

  public String genZAxiomList(ZAxiomList axlist) {
    StringBuilder res = new StringBuilder();
    while (!axlist.isEmptyzby()) {
      res.append(genZAxiom(axlist.getHeadzby()));
      axlist = axlist.getTailzby();
    }
    return res.toString();
  }

  public String genZSpec(ZSpec spec) {
    %match(spec) {
      zthm(thm,by) -> {
        return "\n" 
          + genZExpr(`thm) 
          + "\n" 
          + genZAxiomList(`by)+"\n";
      }
    }
    return "errorZSpec";
  }

  public String genFunctionSymbolDeclaration(String symbolName) {
    StringBuilder res = new StringBuilder();
    res.append("Parameter ");
    res.append(tomiltools.replaceNumbersByString(symbolName)+" :");
    // take care of the arity
    List names = tomiltools.subtermList(symbolName);
    for(int i = 0; i<names.size();i++) {
      res.append(" T ->");
    }
    res.append(" T.\n");
    return res.toString();
  }

  public String genZSpecCollection(Collection<ZSpec> collection) {
    StringBuilder out = new StringBuilder();

    out.append("\nRequire Import zenon.\n");
    out.append("\nParameter T S : Set.\n");

    // collects all used symbols
    Collection<String> symbols = new HashSet<String>();
    for (ZSpec local : collection) {
      symbols.addAll(tomiltools.collectSymbolsFromZSpec(local));
    }

    // Generates types for symbol functions
    for (String symbolName : symbols) {
      out.append(genFunctionSymbolDeclaration(symbolName));
      // declares the subterm functions if necessary
      List names = tomiltools.subtermList(symbolName);
      if(names.size() != 0) {
        out.append("Parameter ");
        Iterator nameIt = names.iterator();
        while(nameIt.hasNext()) {
          String localName = (String) nameIt.next();
          out.append("_" + localName + " ");
        }
        out.append(": T -> T.\n");
      }
    }


    out.append("Parameter symb : T -> S.\n");
    // XXX: define True
    out.append("Parameter true_is_true : True.\n");
    // Generates types for symbols
    out.append("Parameter ");
    for (String symbolName : symbols) {
      out.append(genZSymbol(`zsymbol(symbolName)) +" ");
    }
    out.append(": S.\n");


    // Generates the axioms for coq
    ZAxiomList axiomsDef = tomiltools.symbolsDefinition(symbols);
    ZAxiomList axiomsSub = tomiltools.subtermsDefinition(symbols);
    ZAxiomList axioms = `zby(axiomsDef*,axiomsSub*);
    while (!axioms.isEmptyzby()) {
      out.append(genZAxiom(axioms.getHeadzby()));
      axioms = axioms.getTailzby();
    }

    // Generates the different proof obligations
    int number=1;
    for (ZSpec local : collection) {
      out.append("\n%%begin-auto-proof\n");
      //out.append("%%location: []\n");
      out.append("%%name: theorem"+number+"\n");
      //out.append("%%syntax: tom\n");
      //out.append("%%statement\n");
      out.append(genZSpec(local));

      // XXX: Outputs the axiom for True (Newer versions of zenon may remove this need)
      out.append("Parameter true_is_true : True.\n");

      // Generates types for symbol functions
      // (otherwise, zenon can not know T is not empty)
      // also adds a Parameter fake : T. to make sure zenon knows T is
      // not empty
      for (String symbolName : symbols) {
        out.append(genFunctionSymbolDeclaration(symbolName));
      }
      out.append("Parameter tom_fake : T.\n");
    
      out.append("%%end-auto-proof\n");
      number++;
    }
    return out.toString();
  }
}
