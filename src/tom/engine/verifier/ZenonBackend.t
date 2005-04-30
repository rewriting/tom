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
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 *
 **/

package jtom.verifier;

import jtom.*;
import aterm.*;
import aterm.pure.*;
import java.util.*;
import jtom.verifier.zenon.*;
import jtom.verifier.zenon.types.*;

public class ZenonBackend {

  // ------------------------------------------------------------
  %include { zenon/Zenon.tom }
  // ------------------------------------------------------------

  protected ZenonFactory zfactory;

  public ZenonBackend() {
    zfactory = ZenonFactory.getInstance(SingletonFactory.getInstance());
  }

  protected final ZenonFactory getZenonFactory() {
    return zfactory;
  }

  public String genZSymbol(ZSymbol symbol) {
    %match(ZSymbol symbol) {
      zsymbol(name) -> {
        return `name+"_";
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
        return "("+`name+" "+genZTermList(`tlist)+")"; 
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
    String res = "";
    while (!tl.isEmpty()) {
      res += " "+genZTerm(tl.getHead());
      tl = tl.getTail();
    }
    return res;
  }

  public String genZExpr(ZExpr expr) {
    %match(ZExpr expr) {
      ztrue()  -> { return "true";}
      zfalse() -> { return "false";}
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
        return "  " + `name+" :\n    " + genZExpr(`ax) + "\n";
      }
    }
    return "errorZAxiom";
  }

  public String genZAxiomList(ZAxiomList axlist) {
    String res = "By ";
    while (!axlist.isEmpty()) {
      res += genZAxiom(axlist.getHead());
      axlist = axlist.getTail();
    }
    return res;
  }

  public String genZSpec(ZSpec spec) {
    %match(ZSpec spec) {
      zthm(thm,by) -> {
        return "\n" + genZExpr(`thm) + "\n" + genZAxiomList(`by);
      }
    }
    return "errorZSpec";
  }
}