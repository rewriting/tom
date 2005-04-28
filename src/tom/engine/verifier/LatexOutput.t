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

public class LatexOutput {

  // ------------------------------------------------------------
  %include { il/Il.tom }
  // ------------------------------------------------------------

  protected jtom.verifier.il.IlFactory factory;
  private GenericTraversal traversal;
  private TomVerifier verifier;

  public LatexOutput(TomVerifier verifier) {
    factory = IlFactory.getInstance(SingletonFactory.getInstance());
    this.traversal = new GenericTraversal();
    this.verifier = verifier;
  }

  public GenericTraversal traversal() {
    return this.traversal;
  }
  
  protected final IlFactory getIlFactory() {
    return factory;
  }

  public String build_latex(Collection derivationSet) {
    String result = "\n";
//    String result = "\\documentclass{article}\n";
//    result += "\\usepackage{proof}\n";
//    result += "\\usepackage{xspace}\n";
//    result += "\\usepackage{amssymb}\n";
//    result += "\\input{include.tex}\n\n";
//    result += "\\begin{document}\n";
    
    Iterator it = derivationSet.iterator();
    while(it.hasNext()) {
      DerivTree tree = (DerivTree) it.next();
      result += build_latex(tree);  
      result += "\n \\newpage\n";
    }
//    result += "\n\\end{document}";
    return result;
  }

  public String build_latex(DerivTree tree) {
    
    Map variableset = new HashMap();
    tree = collect_program_variables(tree,variableset);

    String result = "";

    // Use a TreeMap to have the conditions sorted
    Map conditions = new TreeMap();
    result += build_latex_from_tree(tree, conditions);

    Map conds = new TreeMap();
    // Raw list of conditions
    Iterator it = conditions.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      conds.put(
        ((String) entry.getKey()),
        build_latex_from_Seq((Seq) entry.getValue()));
    }
    result += make_two_cols_array(conds);

    // clears the map for reuse
    conds.clear();
    // only the conclusions
    it = conditions.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      conds.put(((String) entry.getKey()),build_latex_from_Seq(clean_Seq((Seq) entry.getValue())));
    }
    result += make_two_cols_array(conds);

    // clears the map for reuse
    conds.clear();
    result += "\nThe theorem to prove is:\n";
    result += "\n \\[\n";
    result += "\\begin{array}{c}\n";
    %match(DerivTree tree) {
      derivrule(_,ebs(_,env(subsList@subs(is(_,t),_*),accept(positive,_))),_,_) -> {
        result += "\\applysubs{" + build_latex_from_subslist(subsList);
        result += "}{" + verifier.patternToString(positive) + "}";
        result += " = " + build_latex_from_term(t) + "\\\\\n";
      }
    }
    result += "\\Leftrightarrow\\\\\n";
    result += "\\begin{array}{ll}\n";
    // only the interesting conditions : dedexpr
    it = conditions.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      Seq value = (Seq) entry.getValue();
      if (value.isDedexpr()) {
        conds.put(((String) entry.getKey()),
                  build_latex_from_Seq(clean_Seq(value)));
      }
    }
    result += make_two_cols_lines(conds);
    result += "\\end{array}\n";
    result += "\\end{array}\n";
    result += "\\]\n";

    return result;
  }
  
  /*
   * Take care to give a TreeMap if you want the output to be sorted
   */
  String make_two_cols_array(Map linesList) {
    String result = "";
    result += "\n \\[\n";
    result += "\\begin{array}{ll}\n";
    result += make_two_cols_lines(linesList);
    result += "\\end{array}\n";
    result += "\\]\n";
    return result;
  }
  String make_two_cols_lines(Map linesList) {
    String result = "";
    Iterator it = linesList.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      result += ((String) entry.getKey()) + ": & ";
      result += ((String) entry.getValue());
      result += "\\\\\n";
    }
    return result;
  }

  DerivTree collect_program_variables(DerivTree tree, Map variables) {
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

  public String build_latex_from_tree(DerivTree tree, Map conditions) {
    String result = "";
    %match(DerivTree tree) {
      derivrule(name,post,pre,condition) -> {
        String condname = "\\fbox{" + (conditions.size()+1) + "}";
        conditions.put(condname,condition);
        result = "\n\\infer[" 
          + condname + "]\n{"
          + build_latex_from_deriv(post) + "}\n{" 
          + build_latex_from_tree(pre,conditions) + "}\n";
      }
    }
    return result;
  }

  String build_latex_from_deriv(Deriv deriv) {
    String result = "";
    %match(Deriv deriv) {
      ebs(lhs,rhs) -> {
        result = "{"+build_latex_from_env(lhs)+"}"
          + " \\evaltobs "
          + "{"+build_latex_from_env(rhs)+"}";
      }
    }
    return result;
  }

  String build_latex_from_env(Environment env) {
    String result = "";
    %match(Environment env) {
      env(subslist,instr) -> {
        result = "\\tup{"+build_latex_from_subslist(subslist)+" }{ "+build_latex_from_Instr(instr)+"}";
      }
    }
    return result;
  }

  String build_latex_from_subslist(SubstitutionList subslist) {
    String result = "";
    %match(SubstitutionList subslist) {
      subs() -> { 
        return "\\env_{0}";
      }
      subs(h*,t) -> {
        %match(Substitution t) {
          undefsubs() -> { 
            result = "\\textit{empty}"+build_latex_from_subslist(`h); 
          }
          is(variable,term) -> {
            result = build_latex_from_subslist(`h)+"\\subs{"+build_latex_from_var(variable)+"}{"+build_latex_from_term(term) + "}";
          }
        }
      }
    }
    return result;
  }

  String build_latex_from_var(Variable variable) {
    String result = "";
    %match(Variable variable) {
      var(name) -> {
        return "{" + name + "}";
      }
    }
    return result;
  }

  String build_latex_from_term(Term term) {
    String result = "";
    %match(Term term) {
      tau(absTerm) -> {
        return "\\mapping{" + build_latex_from_absterm(`absTerm) + "}";
      }
      repr(name) -> {
        return "\\repr{" + `name + "}";
      }
      subterm(s,t,index) -> {
        result = "\\subterm_{" + `s + "}("+build_latex_from_term(`t)+","+`index+")";
      }
      slot(s,t,name) -> {
        result = "\\mathtt{slot}_{" + `s + "}("+build_latex_from_term(`t)+","+`name+")";
      }
      appSubsT(subst,t) -> {
        result = "\\applysubs{"+build_latex_from_subslist(`subst)+"}{"+build_latex_from_term(`t)+"}";
      }
    }
    return result;
  }

  String build_latex_from_Instr(Instr instr) {
    String result = "";
    %match(Instr instr) {
      accept(positive,negative) -> {
        result = "\\accept_{" + verifier.patternToString(positive) + "}";
      }
      refuse() -> {
        result = "\\refuse ";
      }
      ITE(e,ift,iff) -> {
        result = "\\ITE{"+build_latex_from_Expr(e)+"}{"+build_latex_from_Instr(ift)+"}{"+build_latex_from_Instr(iff)+"}";
      }
      ILLet(variable,t,body) -> {
        result = "\\Let{"+build_latex_from_var(variable)+"}{"+build_latex_from_term(t)+"}{"+build_latex_from_Instr(body)+"}";
      }
    }
    return result;
  }

  String build_latex_from_Expr(Expr expr) {
    String result = "";
    %match(Expr expr) {
      true() -> { return "\\true";}
      false() -> { return "\\false";}
      isfsym(t,s) -> {
        result = "\\isfsym{"+build_latex_from_term(t)+"}{"+build_latex_from_symbol(s)+"}";
      }
      eq(lt,rt) -> {
        result = "\\ileq{"+build_latex_from_term(lt)+"}{"+
          build_latex_from_term(rt)+"}";
      }
      tisfsym(absterm,s) -> {
        result = "\\mapping{\\Symb("+build_latex_from_absterm(absterm)+") = "+build_latex_from_symbol(s)+"}";
      }
      teq(absterml,abstermr) -> {
        result = "\\mapping{"+build_latex_from_absterm(absterml)+" = "+build_latex_from_absterm(abstermr)+"}";
      }
      appSubsE(subslist,e) -> {
        result = "\\applysubs{"+build_latex_from_subslist(subslist)+"}{"+build_latex_from_Expr(e)+"}";
      }
    }
    return result;
  }

  String build_latex_from_symbol(Symbol symb) {
    String result = "";
    %match(Symbol symb) {
      fsymbol(name) -> {
        result = "\\textrm{"+name+"}";
      }
    }
    return result;
  }

  String build_latex_from_absterm(AbsTerm absterm) {
    String result = "";
    %match(AbsTerm absterm) {
      absvar(var(name)) -> {
        return "{" + name + "}";
      }
      st(s,t,index) -> {
        result = "\\pos{"+build_latex_from_absterm(t)+"}{"+index+"}";
      }
      sl(s,t,name) -> {
        result = "\\pos{"+build_latex_from_absterm(t)+"}{\\texttt{"+name+"}}";
      }
    }
    return result;
  }

  String build_latex_from_Seq(Seq seq) {
    String result = "";
    %match(Seq seq) {
      seq() -> { result = "\\textrm{emptySeq}"; }
      dedterm(termlist) -> {
        %match(TermList termlist) {
          concTerm(X*,t,_*) -> {
            if (`X.isEmpty()) {
              result += build_latex_from_term(`t);
            } else {
              result += "\\mapequiv " + build_latex_from_term(`t);
            }
          }
        }
      }
      dedexpr(exprlist) -> {
        %match(ExprList exprlist) {
          concExpr(X*,t,_*) -> {
            if (`X.isEmpty()) {
              result += build_latex_from_Expr(`t);
            } else {
              result += "\\mapequiv " + build_latex_from_Expr(`t);
            }
          }
        }
      }
    }
    return result;
  }

  Seq clean_Seq(Seq seq) {
    %match(Seq seq) {
      seq() -> { return seq; }
      dedterm(concTerm(_*,t,v)) -> {
        return `dedterm(concTerm(t,v));
      }
      dedexpr(concExpr(_*,t,v)) -> {
        return `dedexpr(concExpr(t,v));
      }
    }
    return seq;
  }

}
