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
        private static   tom.engine.adt.zenon.types.ZAxiomList  tom_append_list_zby( tom.engine.adt.zenon.types.ZAxiomList l1,  tom.engine.adt.zenon.types.ZAxiomList  l2) {     if( l1.isEmptyzby() ) {       return l2;     } else if( l2.isEmptyzby() ) {       return l1;     } else if(  l1.getTailzby() .isEmptyzby() ) {       return  tom.engine.adt.zenon.types.zaxiomlist.Conszby.make( l1.getHeadzby() ,l2) ;     } else {       return  tom.engine.adt.zenon.types.zaxiomlist.Conszby.make( l1.getHeadzby() ,tom_append_list_zby( l1.getTailzby() ,l2)) ;     }   }   private static   tom.engine.adt.zenon.types.ZAxiomList  tom_get_slice_zby( tom.engine.adt.zenon.types.ZAxiomList  begin,  tom.engine.adt.zenon.types.ZAxiomList  end, tom.engine.adt.zenon.types.ZAxiomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyzby()  ||  (end== tom.engine.adt.zenon.types.zaxiomlist.Emptyzby.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.zenon.types.zaxiomlist.Conszby.make( begin.getHeadzby() ,( tom.engine.adt.zenon.types.ZAxiomList )tom_get_slice_zby( begin.getTailzby() ,end,tail)) ;   }    
  // ------------------------------------------------------------

  private Verifier verifier; // is it useful ?
  private TomIlTools tomiltools;

  public ZenonBackend(Verifier verifier) {
    this.verifier = verifier;
    this.tomiltools = new TomIlTools(verifier);
  }

  public String genZSymbol(ZSymbol symbol) {
    {{if ( (symbol instanceof tom.engine.adt.zenon.types.ZSymbol) ) {if ( ((( tom.engine.adt.zenon.types.ZSymbol )symbol) instanceof tom.engine.adt.zenon.types.zsymbol.zsymbol) ) {

        // manage builtins
        String symbolName = tomiltools.replaceNumbersByString( (( tom.engine.adt.zenon.types.ZSymbol )symbol).getName() );
        return symbolName+ "_";
      }}}}

    return "errorZSymbol";
  }

  public String genZType(ZType type) {
    {{if ( (type instanceof tom.engine.adt.zenon.types.ZType) ) {if ( ((( tom.engine.adt.zenon.types.ZType )type) instanceof tom.engine.adt.zenon.types.ztype.ztype) ) {

        return  (( tom.engine.adt.zenon.types.ZType )type).getTname() ;
      }}}}

    return "errorZType";
  }

  public String genZTerm(ZTerm term) {
    {{if ( (term instanceof tom.engine.adt.zenon.types.ZTerm) ) {if ( ((( tom.engine.adt.zenon.types.ZTerm )term) instanceof tom.engine.adt.zenon.types.zterm.zvar) ) {
 return  (( tom.engine.adt.zenon.types.ZTerm )term).getVarname() ; }}}{if ( (term instanceof tom.engine.adt.zenon.types.ZTerm) ) {if ( ((( tom.engine.adt.zenon.types.ZTerm )term) instanceof tom.engine.adt.zenon.types.zterm.zappl) ) { tom.engine.adt.zenon.types.ZSymbol  tomMatch336NameNumber_freshVar_4= (( tom.engine.adt.zenon.types.ZTerm )term).getZsymb() ;if ( (tomMatch336NameNumber_freshVar_4 instanceof tom.engine.adt.zenon.types.zsymbol.zsymbol) ) {
 
        // manage builtins
        String realName = tomiltools.replaceNumbersByString( tomMatch336NameNumber_freshVar_4.getName() );
        return "(" + realName +" "+genZTermList( (( tom.engine.adt.zenon.types.ZTerm )term).getTermlist() )+")"; 
      }}}}{if ( (term instanceof tom.engine.adt.zenon.types.ZTerm) ) {if ( ((( tom.engine.adt.zenon.types.ZTerm )term) instanceof tom.engine.adt.zenon.types.zterm.zst) ) {
 
        return "(_"+ (( tom.engine.adt.zenon.types.ZTerm )term).getIndex() +" "+genZTerm( (( tom.engine.adt.zenon.types.ZTerm )term).getAbst() )+")";
      }}}{if ( (term instanceof tom.engine.adt.zenon.types.ZTerm) ) {if ( ((( tom.engine.adt.zenon.types.ZTerm )term) instanceof tom.engine.adt.zenon.types.zterm.zsl) ) {
 
        return "(_"+ (( tom.engine.adt.zenon.types.ZTerm )term).getName() +" "+genZTerm( (( tom.engine.adt.zenon.types.ZTerm )term).getAbst() )+")";
      }}}}

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
    {{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.ztrue) ) {
 return "True";}}}{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.zfalse) ) {
 return "False";}}}{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.zisfsym) ) {

        return "((symb "+genZTerm( (( tom.engine.adt.zenon.types.ZExpr )expr).getT() )+") = "+genZSymbol( (( tom.engine.adt.zenon.types.ZExpr )expr).getSymbol() )+")";
      }}}{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.zeq) ) {

        return "("+genZTerm( (( tom.engine.adt.zenon.types.ZExpr )expr).getLt() )+" = "+genZTerm( (( tom.engine.adt.zenon.types.ZExpr )expr).getRt() )+")";
      }}}{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.zforall) ) {

        return "forall "+genZTerm( (( tom.engine.adt.zenon.types.ZExpr )expr).getVar() )+" : "+genZType( (( tom.engine.adt.zenon.types.ZExpr )expr).getAztype() )+",\n "+genZExpr( (( tom.engine.adt.zenon.types.ZExpr )expr).getExpr() );
      }}}{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.zexists) ) {

        return "exists "+genZTerm( (( tom.engine.adt.zenon.types.ZExpr )expr).getVar() )+" : "+genZType( (( tom.engine.adt.zenon.types.ZExpr )expr).getAztype() )+", "+genZExpr( (( tom.engine.adt.zenon.types.ZExpr )expr).getExpr() );
      }}}{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.zand) ) { tom.engine.adt.zenon.types.ZExpr  tom_l= (( tom.engine.adt.zenon.types.ZExpr )expr).getLte() ; tom.engine.adt.zenon.types.ZExpr  tom_r= (( tom.engine.adt.zenon.types.ZExpr )expr).getRte() ;

        if(tom_l==  tom.engine.adt.zenon.types.zexpr.ztrue.make() ) {
          return "("+ genZExpr(tom_r) +")";
        }
        else if (tom_r==  tom.engine.adt.zenon.types.zexpr.ztrue.make() ) {
          return "("+ genZExpr(tom_l) +")";
        }
        return "("+genZExpr(tom_l)+") /\\ ("+genZExpr(tom_r)+")";
      }}}{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.zor) ) { tom.engine.adt.zenon.types.ZExpr  tom_l= (( tom.engine.adt.zenon.types.ZExpr )expr).getLte() ; tom.engine.adt.zenon.types.ZExpr  tom_r= (( tom.engine.adt.zenon.types.ZExpr )expr).getRte() ;

        if(tom_l==  tom.engine.adt.zenon.types.zexpr.zfalse.make() ) {
          return "("+ genZExpr(tom_r) +")";
        }
        else if (tom_r==  tom.engine.adt.zenon.types.zexpr.zfalse.make() ) {
          return "("+ genZExpr(tom_l) +")";
        }
        return "("+genZExpr(tom_l)+") \\/ ("+genZExpr(tom_r)+")";
      }}}{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.znot) ) {
 return "~("+genZExpr( (( tom.engine.adt.zenon.types.ZExpr )expr).getNex() )+")"; }}}{if ( (expr instanceof tom.engine.adt.zenon.types.ZExpr) ) {if ( ((( tom.engine.adt.zenon.types.ZExpr )expr) instanceof tom.engine.adt.zenon.types.zexpr.zequiv) ) {

        return "("+genZExpr( (( tom.engine.adt.zenon.types.ZExpr )expr).getLte() )+") <-> ("+genZExpr( (( tom.engine.adt.zenon.types.ZExpr )expr).getRte() )+")";
      }}}}

    return "errorZExpr";
  }

  public String genZAxiom(ZAxiom axiom) {
    {{if ( (axiom instanceof tom.engine.adt.zenon.types.ZAxiom) ) {if ( ((( tom.engine.adt.zenon.types.ZAxiom )axiom) instanceof tom.engine.adt.zenon.types.zaxiom.zaxiom) ) {

        // manage builtins
        String realName = tomiltools.replaceNumbersByString( (( tom.engine.adt.zenon.types.ZAxiom )axiom).getName() );
        return "Parameter " + realName +" :\n    " + genZExpr( (( tom.engine.adt.zenon.types.ZAxiom )axiom).getAx() ) + ".\n";
      }}}}

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
    {{if ( (spec instanceof tom.engine.adt.zenon.types.ZSpec) ) {if ( ((( tom.engine.adt.zenon.types.ZSpec )spec) instanceof tom.engine.adt.zenon.types.zspec.zthm) ) {

        return "\n" 
          + genZExpr( (( tom.engine.adt.zenon.types.ZSpec )spec).getThm() ) 
          + "\n" 
          + genZAxiomList( (( tom.engine.adt.zenon.types.ZSpec )spec).getBy() )+"\n";
      }}}}

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
      out.append(genZSymbol( tom.engine.adt.zenon.types.zsymbol.zsymbol.make(symbolName) ) +" ");
    }
    out.append(": S.\n");


    // Generates the axioms for coq
    ZAxiomList axiomsDef = tomiltools.symbolsDefinition(symbols);
    ZAxiomList axiomsSub = tomiltools.subtermsDefinition(symbols);
    ZAxiomList axioms = tom_append_list_zby(axiomsDef,tom_append_list_zby(axiomsSub, tom.engine.adt.zenon.types.zaxiomlist.Emptyzby.make() ));
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
