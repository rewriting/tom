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

public class ZenonOutput {

	// ------------------------------------------------------------
	%include { il/il.tom }
	// ------------------------------------------------------------

	protected jtom.verifier.il.ilFactory factory;
  private GenericTraversal traversal;
	private Verifier verifier;

	public ZenonOutput(Verifier verifier) {
		factory = ilFactory.getInstance(SingletonFactory.getInstance());
    this.traversal = new GenericTraversal();
		this.verifier = verifier;
	}

  public GenericTraversal traversal() {
    return this.traversal;
  }
  
	protected final ilFactory getIlFactory() {
		return factory;
	}

	public String build_zenon(Collection derivationSet) {
		String result = "\n";
		Iterator it = derivationSet.iterator();
		while(it.hasNext()) {
			DerivTree tree = (DerivTree) it.next();
			result += build_zenon(tree);	
			result += "\n \\newpage\n";
		}
		return result;
	}

	public String build_zenon(DerivTree tree) {
		
		Map variableset = new HashMap();
		tree = collect_program_variables(tree,variableset);

		String result = "";

		// Use a TreeMap to have the conditions sorted
		Map conditions = new TreeMap();
    collect_constraints(tree,conditions);            
		Map conds = new TreeMap();

    // theorem to prove
		result += "\n%% The theorem to prove is:\n";
		%match(DerivTree tree) {
      derivrule(_,ebs(_,env(subsList@subs(is(_,t),_*),accept(positive,_))),_,_) -> {
          result += "" + verifier.pattern_to_string(positive, build_zenon_varmap(subsList, new HashMap())) + "";
        
        }
		}
		result += " <-> \n";
		// only the interesting conditions : dedexpr
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
      String value = (String) entry.getValue();
      result += value + " ";
      if (it.hasNext()) {
          result += " /\\ ";
      }
    }
		result += "\n";

		return result;
	}
	

    /**
     * collects all variable names in the DerivTree, and give a name to _'s
    */
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


	String build_zenon_from_var(Variable variable) {
		String result = "";
		%match(Variable variable) {
			var(name) -> {
				return " " + name + " ";
			}
		}
		return result;
	}

	String build_zenon_from_term(Term term) {
		String result = "";
		%match(Term term) {
			tau(absTerm) -> {
				return "(" + build_zenon_from_absterm(`absTerm) + ")";
			}
			repr(name) -> {
				return "(probleme " + `name + ")";
			}
			subterm(s,t,index) -> {
				result = "(_" + `index + " "+ build_zenon_from_term(`t)+")";
			}
			slot(s,t,name) -> {
				result = "(_" + `name + " "+ build_zenon_from_term(`t)+")";
			}
			appSubsT(subst,t) -> {
				result = "(probleme la substitution devrait etre appliquee" + `subst + ")";
			}
		}
		return result;
	}

	String build_zenon_from_Expr(Expr expr) {
		String result = "";
		%match(Expr expr) {
			true() -> { return "(true)";}
			false() -> { return "(false)";}
			isfsym(t,s) -> {
				result = "( " + expr + " )";
			}
			eq(lt,rt) -> {
				result = "( " + expr + " )";
			}
			tisfsym(absterm,s) -> {
				result = "((symb "+build_zenon_from_absterm(absterm)+") = "+build_zenon_from_symbol(s)+")";
			}
			teq(absterml,abstermr) -> {
				result = "("+build_zenon_from_absterm(absterml)+" = "+build_zenon_from_absterm(abstermr)+")";
			}
			appSubsE(subslist,e) -> {
				result = "( " + expr + " )";
			}
		}
		return result;
	}

	String build_zenon_from_symbol(Symbol symb) {
		String result = "";
		%match(Symbol symb) {
			fsymbol(name) -> {
				result = ""+name+"_";
			}
		}
		return result;
	}

	String build_zenon_from_Seq(Seq seq) {
		String result = "";
		%match(Seq seq) {
			seq() -> { result = "\n"; }
			dedterm(termlist) -> {
				%match(TermList termlist) {
					concTerm(X*,t,_*) -> {
							result += build_zenon_from_term(`t);
					}
				}
			}
			dedexpr(exprlist) -> {
				%match(ExprList exprlist) {
					concExpr(X*,t,_*) -> {
							result += build_zenon_from_Expr(`t);
					}
				}
			}
		}
		return result;
	}

	String build_zenon_from_absterm(AbsTerm absterm) {
		String result = "";
		%match(AbsTerm absterm) {
			absvar(var(name)) -> {
				return "" + `name + "";
			}
			st(s,t,index) -> {
				result = "(_"+`index + " " + build_zenon_from_absterm(`t) +")";
			}
			sl(s,t,name) -> {
				result = "(_"+ `name + " " + build_zenon_from_absterm(`t) +")";
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

  private Map build_zenon_varmap(SubstitutionList sublist, Map map) {
		%match(SubstitutionList sublist) {
			()                -> { return map; }
			(undefsubs(),t*)  -> { return build_zenon_varmap(`t,map);}
			(is(v,term),t*)   -> { 
        map.put(`v,build_zenon_from_term(`term));
				return build_zenon_varmap(`t,map);
			}
			_ -> { return null; }
		}
	}

	public void collect_constraints(DerivTree tree, Map conditions) {
		%match(DerivTree tree) {
			derivrule(name,post,pre,condition) -> {
				String condname = "\\fbox{" + (conditions.size()+1) + "}";
				conditions.put(condname,condition);
        collect_constraints(pre,conditions);
			}
		}
	}

}
