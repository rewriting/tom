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
import aterm.pure.*;
import java.util.*;
import tom.library.traversal.*;
import jtom.adt.tomsignature.types.*;
import jtom.verifier.il.*;
import jtom.verifier.il.types.*;
import jtom.verifier.zenon.*;
import jtom.verifier.zenon.types.*;


public class ZenonOutput {

  // ------------------------------------------------------------
  %include { il/il.tom }
  %include { zenon/Zenon.tom }
  // ------------------------------------------------------------

  protected jtom.verifier.il.ilFactory factory;
  protected jtom.verifier.zenon.ZenonFactory zfactory;
  private GenericTraversal traversal;
  private Verifier verifier;
  private TomIlTools tomiltools;

  public ZenonOutput(Verifier verifier) {
    factory = ilFactory.getInstance(SingletonFactory.getInstance());
    zfactory = ZenonFactory.getInstance(SingletonFactory.getInstance());
    this.traversal = new GenericTraversal();
    this.verifier = verifier;
    this.tomiltools = new TomIlTools(verifier.getSymbolTable());
  }

  public GenericTraversal traversal() {
    return this.traversal;
  }
  
  protected final ilFactory getIlFactory() {
    return factory;
  }

  protected final ZenonFactory getZenonFactory() {
    return zfactory;
  }

  public Collection build_zenon(Collection derivationSet) {
    Collection resset = new HashSet();
    Iterator it = derivationSet.iterator();
    while(it.hasNext()) {
      DerivTree tree = (DerivTree) it.next();
      ZSpec spec = build_zenon(tree);
      resset.add(spec);
    }
    return resset;
  }

  public ZSpec build_zenon(DerivTree tree) {
    
    Map variableset = new HashMap();
    tree = collectProgramVariables(tree,variableset);

    // Use a TreeMap to have the conditions sorted
    Map conditions = new TreeMap();
    collect_constraints(tree,conditions);            
    Map conds = new TreeMap();

    List subjectList = new LinkedList();
    ZExpr pattern = null;
    ZExpr negpattern = null;
    // theorem to prove
    %match(DerivTree tree) {
      derivrule(_,ebs(_,env(subsList,acc@accept(positive,negative))),_,_) -> {
        pattern = tomiltools.patternToZExpr((Pattern)positive,
                                              build_zenon_varmap(subsList, new HashMap()));
        tomiltools.getZTermSubjectListFromPattern((Pattern)positive,subjectList,new HashMap());
        negpattern = tomiltools.patternToZExpr((PatternList)negative,
                                                 build_zenon_varmap(subsList, new HashMap()));
      }
    }
    
    ZExpr constraints = `ztrue();
    // we consider only the interesting conditions : dedexpr
    Iterator it = conditions.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      Seq value = (Seq) entry.getValue();
      if (value.isDedexpr()) {
        conds.put(((String) entry.getKey()),
                  build_zenon_from_Seq(clean_Seq(value)));
      }
    }
    it = conds.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      ZExpr value = (ZExpr) entry.getValue();
      constraints = `zand(constraints,value);
    }
    ZExpr theorem = null;
    if (pattern != null && constraints != null) {
      if(verifier.isCamlSemantics() && negpattern != null) {
        theorem = `zequiv(zand(pattern,znot(negpattern)),constraints);
      } else {
        theorem = `zequiv(pattern,constraints);
      }
      // System.out.println(theorem);
    }

    // now we have to to build the axion list, starting from the
    // signature. Again, the TomIlTools will be useful, it has access
    // to TomSignature and Zenon signature
    
    // collects symbols in pattern
    Collection symbols = tomiltools.collectSymbols(pattern);
    // System.out.println("symbols: "+symbols);
    // generates the axioms for this set of symbols
    ZAxiomList symbolsAxioms = tomiltools.symbolsDefinition(symbols);
    // generates axioms for all subterm operations
    ZAxiomList subtermAxioms = tomiltools.subtermsDefinition(symbols);

    Iterator iter = subjectList.iterator();
    while(iter.hasNext()) {
      ZTerm input = (ZTerm)iter.next();
      theorem = `zforall(input,ztype("T"),theorem);  
    }
    ZSpec spec = `zthm(theorem,zby(symbolsAxioms*,subtermAxioms*));

    return spec;
  }
  

  /**
   * collects all variable names in the DerivTree, and give a name to _'s
   */
  DerivTree collectProgramVariables(DerivTree tree, Map variables) {
    return (DerivTree) collect_prog_vars.apply(tree,variables);
  }
  private Replace2 collect_prog_vars = new Replace2() {
      public ATerm apply(ATerm subject, Object astore) {
        Map store = (Map) astore;
        
        if (subject instanceof Variable) {
          %match(Variable subject) {
            var(name) -> {
              String newname = name;
              if (store.containsKey(name)){
                newname = (String) store.get(name);
              } else {
                if (name.startsWith("[") && name.endsWith("]")) {
                  newname = "X_" + store.size();
                }
                store.put(name,newname);
              }
              return `var(newname);
            }
          }
        }
        return traversal().genericTraversal(subject,this,astore);
      }
    };


  String build_zenon_from_var(Variable variable) {
    String result = "";
    %match(Variable variable) {
      var(name) -> {
        return " " + name + " ";
      }
    }
    return result;
  }

  ZTerm build_zenon_from_term(Term term) {
    %match(Term term) {
      tau(absTerm) -> {
        return build_zenon_from_absterm(`absTerm);
      }
      repr(name) -> {
        return `zvar("Error in build_zenon_from_term repr");
      }
      subterm(s,t,index) -> {
        return `zvar("Error in build_zenon_from_term subterm");
      }
      slot(s,t,name) -> {
        return `zvar("Error in build_zenon_from_term slot");
      }
      appSubsT(subst,t) -> {
        // probleme: la substitution devrait etre appliquee
        return `zvar("Error in build_zenon_from_term appsubsT ");
      }
    }
    return `zvar("match vide dans build_zenon_from_term");
  }

  ZExpr build_zenon_from_Expr(Expr expr) {
    %match(Expr expr) {
      true() -> { return `ztrue();}
      false() -> { return `zfalse();}
      isfsym(t,s) -> {
        // this should not occur
        return `zisfsym(zvar("Error in build_zenon_from_Expr"),zsymbol("isfsym"));
      }
      eq(lt,rt) -> {
        // this should not occur
        return `zeq(zvar("Error in build_zenon_from_Expr"),zvar("eq"));
      }
      tisfsym(absterm,s) -> {
        return `zisfsym(build_zenon_from_absterm(absterm),build_zenon_from_symbol(s));
      }
      teq(absterml,abstermr) -> {
        return `zeq(build_zenon_from_absterm(absterml),build_zenon_from_absterm(abstermr));
      }
      appSubsE(subslist,e) -> {
        // this should not occur
        return `zeq(zvar("Error in build_zenon_from_Expr"),zvar("appSubsE"));
      }
    }
    return `zeq(zvar("Error in build_zenon_from_Expr"),zvar("end"));
  }

  ZSymbol build_zenon_from_symbol(Symbol symb) {
    String n = "random";
    %match(Symbol symb) {
      fsymbol(name) -> {
        n = name;
      }
    }
    return `zsymbol(n);
  }

  ZExpr build_zenon_from_Seq(Seq seq) {
    ZExpr result = `ztrue();
    %match(Seq seq) {
      seq() -> { /* nothing */ }
      dedterm(termlist) -> {
        %match(TermList termlist) {
          concTerm(X*,tl,tr) -> {
              result = `zeq(build_zenon_from_term(tl),build_zenon_from_term(tr));
          }
        }
      }
      dedexpr(exprlist) -> {
        %match(ExprList exprlist) {
          concExpr(X*,t,true()) -> {
              result = build_zenon_from_Expr(`t);
          }
        }
      }
      dedexpr(exprlist) -> {
        %match(ExprList exprlist) {
          concExpr(X*,t,false()) -> {
              result = `znot(build_zenon_from_Expr(t));
          }
        }
      }
    }
    return result;
  }

  ZTerm build_zenon_from_absterm(AbsTerm absterm) {
    %match(AbsTerm absterm) {
      absvar(var(name)) -> {
        return `zvar(name);
      }
      st(s,t,index) -> {
        return `zst(build_zenon_from_absterm(t),index);
      }
      sl(s,t,name) -> {
        return `zsl(build_zenon_from_absterm(t),name);
      }
    }
    return `zvar("Error in build_zenon_from_absterm");
  }

  Seq clean_Seq(Seq seq) {
    %match(Seq seq) {
      seq() -> { return seq; }
      dedterm(concTerm(_*,t,v)) -> {
          return `dedterm(concTerm(t,v));
      }
      /* What happen in the "false" case ? */
      dedexpr(concExpr(_*,t,v)) -> {
        return `dedexpr(concExpr(t,v));
      }
    }
    return seq;
  }

  private Map build_zenon_varmap(SubstitutionList sublist, Map map) {
    %match(SubstitutionList sublist) {
      ()                -> { return map; }
      (undefsubs(),t*)  -> { return build_zenon_varmap(`t,map);}
      (is(var(name),term),t*)   -> { 
        map.put(`name,build_zenon_from_term(`term));
        return build_zenon_varmap(`t,map);
      }
      _ -> { return null; }
    }
  }

  public void collect_constraints(DerivTree tree, Map conditions) {
    %match(DerivTree tree) {
      derivrule(name,post,pre,condition) -> {
        String condname = "" + (conditions.size()+1) + "";
        conditions.put(condname,condition);
        collect_constraints(pre,conditions);
      }
      derivrule2(name,post,pre,pre2,condition) -> {
        String condname = "" + (conditions.size()+1) + "";
        conditions.put(condname,condition);
        collect_constraints(pre,conditions);
        collect_constraints(pre2,conditions);
      }
    }
  }

}
