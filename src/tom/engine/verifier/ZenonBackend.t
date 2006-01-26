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
import aterm.pure.*;
import java.util.*;
import tom.engine.adt.zenon.*;
import tom.engine.adt.zenon.types.*;

public class ZenonBackend {

  // ------------------------------------------------------------
  %include { adt/zenon/Zenon.tom }
  // ------------------------------------------------------------

  private Verifier verifier; // is it useful ?
  private TomIlTools tomiltools;

  public ZenonBackend(Verifier verifier) {
    this.verifier = verifier;
    this.tomiltools = new TomIlTools(verifier);
  }

  public String genZSymbol(ZSymbol symbol) {
    %match(ZSymbol symbol) {
      zsymbol(name) -> {
        // manage builtins
        String symbolName = tomiltools.replaceNumbersByString(`name);
        return `symbolName + "_";
      }
    }
    return "errorZSymbol";
  }

  public String genZType(ZType type) {
    %match(ZType type) {
      ztype(name) -> {
        return `name;
      }
    }
    return "errorZType";
  }

  public String genZTerm(ZTerm term) {
    %match(ZTerm term) {
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
    StringBuffer res = new StringBuffer();
    while (!tl.isEmpty()) {
      res.append(" "+genZTerm(tl.getHead()));
      tl = tl.getTail();
    }
    return res.toString();
  }

  public String genZExpr(ZExpr expr) {
    %match(ZExpr expr) {
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
    %match(ZAxiom axiom) {
      zaxiom(name,ax) -> {
        // manage builtins
        String realName = tomiltools.replaceNumbersByString(`name);
        return "Parameter " + realName +" :\n    " + genZExpr(`ax) + ".\n";
      }
    }
    return "errorZAxiom";
  }

  public String genZAxiomList(ZAxiomList axlist) {
    StringBuffer res = new StringBuffer();
    while (!axlist.isEmpty()) {
      res.append(genZAxiom(axlist.getHead()));
      axlist = axlist.getTail();
    }
    // XXX: Outputs the axiom for True (will disappear soon)
    res.append("Parameter true_is_true : True.\n");
    
    return res.toString();
  }

  public String genZSpec(ZSpec spec) {
    %match(ZSpec spec) {
      zthm(thm,by) -> {
        return "\n" 
          + genZExpr(`thm) 
          + "\n" 
          + genZAxiomList(`by)+"\n";
      }
    }
    return "errorZSpec";
  }

  public String genZSpecCollection(Collection collection) {
    StringBuffer out = new StringBuffer();

    out.append("\nRequire Import zenon8.\n");
    out.append("\nParameter T S : Set.\n");

    // collects all used symbols
    Collection symbols = new HashSet();
    Iterator it = collection.iterator();
    while(it.hasNext()) {
      ZSpec local = (ZSpec) it.next();
      symbols.addAll(tomiltools.collectSymbolsFromZSpec(local));
    }

    // Generates types for symbols
    it = symbols.iterator();
    while(it.hasNext()) {
      String symbolName = (String) it.next();
      out.append("Parameter " + tomiltools.replaceNumbersByString(symbolName) +" :");
      // arity of the symbol ?
      List names = tomiltools.subtermList(symbolName);
      for(int i = 0; i<names.size();i++) {
        out.append(" T ->");
      }
      out.append(" T.\n");
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
    it = symbols.iterator();
    out.append("Parameter ");
    while(it.hasNext()) {
      String symbolName = (String) it.next();
      out.append(genZSymbol(`zsymbol(symbolName)) +" ");
    }
    out.append(": S.\n");


    // Generates the axioms for coq
    ZAxiomList axiomsDef = tomiltools.symbolsDefinition(symbols);
    ZAxiomList axiomsSub = tomiltools.subtermsDefinition(symbols);
    ZAxiomList axioms = `zby(axiomsDef*,axiomsSub*);
    while (!axioms.isEmpty()) {
      out.append(genZAxiom(axioms.getHead()));
      axioms = axioms.getTail();
    }

    // Generates the different proof obligations
    int number=1;
    it = collection.iterator();
    while (it.hasNext()) {
      out.append("\n%%begin-auto-proof\n");
      out.append("%%location: []\n");
      out.append("%%name: theorem"+number+"\n");
      out.append("%%statement\n");
      out.append(genZSpec((ZSpec)it.next()));
      out.append("%%end-auto-proof\n");
      number++;
    }
    return out.toString();
  }
}
