/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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
import jtom.tools.*;
import jtom.runtime.*;
import jtom.adt.tomsignature.types.*;
import jtom.verifier.verifier.il.*;
import jtom.verifier.verifier.il.types.*;
import jtom.TomMessage;

public class Verifier extends TomBase {

	// ------------------------------------------------------------
 	%include { ../adt/TomSignature.tom }
	// ------------------------------------------------------------

	%vas {
		module il
    imports
			
		public sorts 
			Symbol Representation Variable Term Expr Instr 
			Substitution SubstitutionList Environment
			Seq TermList ExprList
			Deriv DerivTree

		abstract syntax
			fsymbol(name:String)                    -> Symbol
			
			var(name:String)                        -> Variable

			vtot(var:Variable)                      -> Term
			repr(term:String)                       -> Term
			subterm(symbol:Symbol,t:Term,index:Int) -> Term
			slot(symbol:Symbol,t:Term,name:String)  -> Term

			true                                    -> Expr
			false                                   -> Expr
			isfsym(t:Term,symbol:Symbol)            -> Expr
			eq(lt:Term,rt:Term)                     -> Expr
			tisfsym(t:Term,symbol:Symbol)            -> Expr
			teq(lt:Term,rt:Term)                     -> Expr

			accept                                  -> Instr
			refuse                                  -> Instr
			ITE(e:Expr,ift:Instr,iff:Instr)         -> Instr
			ILLet(var:Variable,t:Term,body:Instr)   -> Instr

			undefsubs                               -> Substitution
			is(var:Variable,term:Term)              -> Substitution
			subs(Substitution *)                    -> SubstitutionList
			env(subs:SubstitutionList,i:Instr)      -> Environment

			ebs(lhs:Environment,rhs:Environment)    -> Deriv
			endderiv                                -> DerivTree
			derivrule(name:String,post:Deriv,pre:DerivTree,cond:Seq) -> DerivTree

			seq                                     -> Seq
			tau(term:String)                        -> Term
			appSubsT(subs:SubstitutionList,t:Term)  -> Term
			appSubsE(subs:SubstitutionList,e:Expr)  -> Expr
			dedterm(terms:TermList)                 -> Seq
			concTerm(Term *)                        -> TermList
			dedexpr(exprs:ExprList)                 -> Seq
			concExpr(Expr *)                        -> ExprList
	}

	protected jtom.verifier.verifier.il.ilFactory factory;

	public Verifier() {
		super();
		factory = ilFactory.getInstance(getTomSignatureFactory().getPureFactory());
	}

	protected final ilFactory getIlFactory() {
		return factory;
	}

	public Term build_TermFromTomTerm(TomTerm tomterm) {
		%match(TomTerm tomterm) {
			ExpressionToTomTerm(expr) -> {
				return `build_TermFromExpression(expr);
			}
			_ -> {
				System.out.println("build_TermFromTomTerm don't know how to handle this: " + tomterm);
				return `repr("foirade");
			}
		}
	}
	
	public Term build_TermFromExpression(Expression expression) {
		%match(Expression expression) {
			GetSubterm(codomain,Variable[astName=name], Number(index)) -> {
				// we will need to find the head symbol
				return `subterm(fsymbol("empty"),vtot(var(name.toString())),index);
			}
			GetSlot(codomain,Name(symbolName),slotName,Variable[astName=name]) -> {
				return `slot(fsymbol(symbolName),vtot(var(name.toString())),slotName);
			}
			TomTermToExpression(Variable[astName=name]) -> {
				return `vtot(var(name.toString()));
			}
			_ -> {
				System.out.println("build_TermFromExpression don't know how to handle this: " + expression);
				return `repr("");
			}
		}
	}
	
	public String extract_Name(NameList nl) {
		%match(NameList nl) {
			(Name(name)) -> {
				return `name;
			}
		}
		return nl.toString();
	}

	public Expr build_ExprFromExpression(Expression expression) {
		%match(Expression expression) {
			TrueTL()  -> { return `true(); }
			FalseTL() -> { return `false(); }
			EqualFunctionSymbol(type,Variable[astName=name],Appl[nameList=symbolName]) -> {
				return `isfsym(vtot(var(name.toString())),fsymbol(extract_Name(symbolName)));
			}
			EqualFunctionSymbol(type,term1,Appl[nameList=symbolName]) -> {
				return `isfsym(build_TermFromTomTerm(term1),fsymbol(extract_Name(symbolName)));
			}
			EqualTerm(type,t1,t2) -> {
				// TODO, later
				return `false();
			}
			_ -> {
				System.out.println("build_ExprFromExpression don't know how to handle this: " + expression);
				return `false();
			}
		}
	}

	public Instr build_InstrFromAutomata(Instruction automata) {
		%match(Instruction automata) {
			TargetLanguageToInstruction(_) -> { 
				// This should be an action
				return `accept(); 
			}
			Return[] -> {
				return `accept();
			}
			IfThenElse(cond,ift,iff) -> {
				return `ITE(build_ExprFromExpression(cond),
										build_InstrFromAutomata(ift),
										build_InstrFromAutomata(iff));
			}
			Let(Variable[astName=avar],expr,body) -> {
				return `ILLet(var(avar.toString()),
											build_TermFromExpression(expr),
											build_InstrFromAutomata(body));
			}
			LetAssign(Variable[astName=avar],expr,body) -> {
				return `ILLet(var(avar.toString()),
											build_TermFromExpression(expr),
											build_InstrFromAutomata(body));
			}
			Nop() -> {
				// tom uses nop in the iffalse part of ITE
				return `refuse();
			}
			_ -> {
				System.out.println("build_InstrFromAutomata don't know how to handle this : " + automata);
				return `refuse();
			}
		}
	}

	public DerivTree build_tree(Instruction automata) {
		DerivTree tree = null;
		// System.out.println("Build derivation tree for: " + automata);

		Environment startingenv = `env(concSubstitution(),
																 build_InstrFromAutomata(automata));

		Deriv startingderiv = `ebs(startingenv,
															 env(concSubstitution(undefsubs()),accept));

		// System.out.println("The derivation: " + startingderiv);

		SubstRef output = new SubstRef();
		tree = apply_rules(startingderiv,output);
		// Propagate the computed output substitution
		SubstitutionList outputsubst = output.get();
		// System.out.println("The substitution: " + outputsubst);
		if (outputsubst != null) {
			tree = replaceUndefSubst(tree,outputsubst);
		}

		// System.out.println("The tree: " + tree);
		return tree;
	}

/**
 * The axioms the mapping has to verify
 */
	
	protected Seq build_dedterm(Term sp) {
		// TODO : implement the \mapequiv relation
		TermList ded = `concTerm(sp);
		%match(Term sp) {
			appSubsT[] -> { 
				TermList follow = apply_termRules(replaceVarsInTerm(sp));
				ded = `concTerm(ded*,follow*); 
			}
		}

		// System.out.println("dedterm gives : " + ded);
		return `dedterm(concTerm(ded*));
	}

	protected Seq build_dedexpr(Expr sp) {
		// TODO : implement the \mapequiv relation
		ExprList ded = `concExpr(sp);
		%match(Expr sp) {
			appSubsE[] -> { 
				ExprList follow = apply_exprRules(replaceVarsInExpr(sp));
				ded = `concExpr(ded*,follow*); 
			}
		}

		// System.out.println("dedexpr gives: " + ded);
		return `dedexpr(concExpr(ded*,true));
	}

  // need to be reworked : this IS a BAD way to do it !
	protected TermList apply_termRules(Term trm) {
		%match(Term trm) {
			subterm(s,t@subterm[],index) -> {
				// first reduce the argument
				TermList reduced = apply_termRules(`t);
				TermList res = `concTerm(trm);
				while(!reduced.isEmpty()) {
					Term head = reduced.getHead();
					if (head.isTau()) {
						TermList hl = apply_termRules(head);
						while(!hl.isEmpty()) {
							Term h = hl.getHead();
							res = `concTerm(res*,subterm(s,h,index));						
							hl = hl.getTail();
						}
					} else {
						res = `concTerm(res*,subterm(s,head,index));
					}
					reduced = reduced.getTail();
				}
				return `concTerm(res*);
			}
			slot(s,t@slot[],slotName) -> {
				// first reduce the argument
				TermList reduced = apply_termRules(`t);
				TermList res = `concTerm(trm);
				while(!reduced.isEmpty()) {
					Term head = reduced.getHead();
					if (head.isTau()) {
						TermList hl = apply_termRules(head);
						while(!hl.isEmpty()) {
							Term h = hl.getHead();
							res = `concTerm(res*,slot(s,h,slotName));						
							hl = hl.getTail();
						}
					} else {
						res = `concTerm(res*,slot(s,head,slotName));
					}
					reduced = reduced.getTail();
				}
				return `concTerm(res*);
			}
			subterm(s,tau(t),index) -> {
				// we shall test if term t has symbol s 
				String term = t + "_pos_" + index;
				return `concTerm(trm,tau(term));
			}
			slot(s,tau(t),slotName) -> {
				// we shall test if term t has symbol s 
				String term = t + "_slot_" + slotName;
				return `concTerm(trm,tau(term));
			}
			subterm(s,vtot(var(t)),index) -> {
				// we shall test if term t has symbol s 
				String term = t + "_pos_" + index;
				return `concTerm(trm,tau(term));
			}
			slot(s,vtot(var(t)),slotName) -> {
				// we shall test if term t has symbol s 
				String term = t + "_slot_" + slotName;
				return `concTerm(trm,tau(term));
			}
			_ -> { 
				System.out.println("apply TermRules : nothing applies to:" + trm);
				return `concTerm(trm); }
		}
	}

	protected ExprList apply_exprRules(Expr ex) {
		%match(Expr ex) {
			eq(tau(tl),tau(tr)) -> {
				return `concExpr(ex,teq(tau(tl),tau(tr)));
			}
			isfsym(tau(t),symbol) -> {
				return `concExpr(ex,tisfsym(tau(t),symbol));
			}
			eq(vtot(var(tl)),vtot(var(tr))) -> {
				return `concExpr(ex,teq(tau(tl),tau(tr)));
			}
			isfsym(vtot(var(t)),symbol) -> {
				return `concExpr(ex,tisfsym(tau(t),symbol));
			}
			eq(lt,rt) -> {
				// first reduce the argument
				Term reducedl = ((TermList)apply_termRules(`lt).reverse()).getHead();
				Term reducedr = ((TermList)apply_termRules(`rt).reverse()).getHead();

				ExprList taill = `apply_exprRules(eq(reducedl,reducedr));
				ExprList res = `concExpr(ex,taill*);
			}
			isfsym(t,symbol) -> {
				// first reduce the argument
				TermList reduced = apply_termRules(`t);
				ExprList res = `concExpr(ex);
				while(!reduced.isEmpty()) {
					Term head = reduced.getHead();
					res = `concExpr(res*,isfsym(head,symbol));
					reduced = reduced.getTail();
				}
				%match(ExprList res) {
					concExpr(hl*,tail) -> {
						ExprList taill = `apply_exprRules(tail);
						return `concExpr(hl*,taill*);
					}
				}
			}
			_ -> { 
				System.out.println("apply ExprRules : nothing applies to:" + ex);
				return `concExpr(ex); }
		}
	}

	protected DerivTree apply_rules(Deriv post, SubstRef outsubst) {
		%match(Deriv post) {
			// let rule
			ebs(env(e,ILLet(x,u,i)),env(concSubstitution(undefsubs()),ip)) -> {
				// build condition
				Seq cond = build_dedterm(`appSubsT(e,u));
				// find "t"
				Term t = null;
				%match(Seq cond) {
					dedterm(concTerm(_*,r)) -> { t = `r; }
					_ -> { if (t == null) { 
							System.out.println("build_dedterm has a problem with " + cond);
						}
					}
				}
				Deriv up = `ebs(
					env(concSubstitution(e*,is(x,t)),i),
					env(concSubstitution(undefsubs()),ip)
					);
				DerivTree pre = apply_rules(up,outsubst);
				return `derivrule("let",post,pre,cond);
				}
			// iftrue/iffalse rule
			ebs(env(e,ITE(exp,ift,iff)),env(concSubstitution(undefsubs()),ip)) -> {
				// build condition
				Seq cond = build_dedexpr(`appSubsE(e,exp));
				// true or false ?
				Expr res = null;
				%match(Seq cond) {
					dedexpr(concExpr(_*,x)) -> { res = `x; }
					_ -> { if (res == null) { 
							System.out.println("build_dedexpr has a problem with " + cond);
						}
					}
				}
				Deriv up = null;
				String rulename = "fail";
				if (res == `true()) {
					up = `ebs(env(e,ift),env(concSubstitution(undefsubs()),ip));
					rulename = "iftrue";
				} else if (res == `false()) {
					up = `ebs(env(e,iff),env(concSubstitution(undefsubs()),ip));
					rulename = "iffalse";
				}	else {
					System.out.println("How to conclude with: "+ res + " ?");
				}
				DerivTree pre = apply_rules(up,outsubst);
				return `derivrule(rulename,post,pre,cond);
			}
			// axiom !
			ebs(env(e,accept()),env(concSubstitution(undefsubs()),accept())) -> {
				outsubst.set(`e);
				return `derivrule("axiom",post,endderiv(),seq());
			}
			_ -> { 
				System.out.println("Ratai ! " + post);
				return `derivrule("problem",post,endderiv(),seq());
			}
		}
	}

/**
 * To replace undefsubst in tree by the computed value
 * which leads to axiom
 */

	Replace2 replace_undefsubs = new Replace2() {
			public ATerm apply(ATerm subject, Object arg1) {
				if (subject instanceof SubstitutionList) {
					%match(SubstitutionList subject) {
						(undefsubs()) -> {
							return (SubstitutionList)arg1;
						}
					}
				}
				/* Default case : Traversal */
				return traversal().genericTraversal(subject,this,arg1);
			} // end apply
		};

	private DerivTree replaceUndefSubst(DerivTree subject, 
																			SubstitutionList subs) {
		return (DerivTree) replace_undefsubs.apply(subject,subs);
	}

	private class SubstRef {
		private SubstitutionList sl;
		public SubstRef() {
			sl = null;
		}
		public void set(SubstitutionList ssl) {
			this.sl = ssl;
		}
		public SubstitutionList get() {
			return sl;
		}
	}

/**
 * These functions deals with substitution application
 */

	Replace2 replace_VarbyTerm = new Replace2() {
			public ATerm apply(ATerm subject, Object arg1) {
				if (subject instanceof Term) {
					%match(Term subject) {
						vtot(v@var(name)) -> {
							Map map = (Map) arg1;
							if (map.containsKey(v)) {
								return (Term)map.get(v);
							}
							return (Term)subject;
						}
					}
				}
				/* Default case : Traversal */
				return traversal().genericTraversal(subject,this,arg1);
			} // end apply
		};

	public Term replaceVarsInTerm(Term subject) {
		%match(Term subject) {
			appSubsT(sl,term) -> {
				Map map = build_varmap(sl, new HashMap());
				return (Term) replace_VarbyTerm.apply(term,map);
			}
		}
		return subject;
	}

	public Expr replaceVarsInExpr(Expr subject) {
		%match(Expr subject) {
			appSubsE(sl,term) -> {
				Map map = build_varmap(sl, new HashMap());
				return (Expr) replace_VarbyTerm.apply(term,map);
			}
		}
		return subject;
	}

	private Map build_varmap(SubstitutionList sl, Map map) {
		%match(SubstitutionList sl) {
			()                -> { return map; }
			(undefsubs(),t*)  -> { return build_varmap(`t,map);}
			(is(v,term),t*)   -> { 
				map.put(`v,`term);
				return build_varmap(`t,map);
			}
			_ -> { return null; }
		}
	}

}


