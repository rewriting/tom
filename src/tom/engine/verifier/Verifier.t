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

			// to be completed
			seq()                                   -> Seq
			tau(term:String)                        -> Term
			appSubsT(subs:SubstitutionList,t:Term)  -> Term
			appSubsE(subs:SubstitutionList,e:Expr)  -> Expr
			dedterm(terms:TermList)                 -> Seq
			concTerm(Term *)                        -> TermList
			dedexpr(exprs:ExprList)                 -> Seq
			concExpr(Expr *)                        -> ExprList
	}

	protected jtom.verifier.verifier.il.Factory factory;

	public Verifier() {
		super();
		factory = new Factory(getTomSignatureFactory().getPureFactory());
	}

	protected final Factory getIlFactory() {
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
		System.out.println("Build derivation tree for: " + automata);

		Environment startingenv = `env(concSubstitution(),
																 build_InstrFromAutomata(automata));

		Deriv startingderiv = `ebs(startingenv,env(concSubstitution(undefsubs()),accept));

		System.out.println("The derivation: " + startingderiv);

		SubstRef output = new SubstRef();
		tree = apply_rules(startingderiv,output);
		// Propagate the computed output substitution
		SubstitutionList outputsubst = output.get();
		System.out.println("The substitution: " + outputsubst);
		if (outputsubst != null) {
			tree = replaceUndefSubst(tree,outputsubst);
		}

		System.out.println("The tree: " + tree);
		return tree;
	}
	
	protected Seq build_dedterm(Term sp) {
		// TODO : implement the \mapequiv relation
		return `dedterm(concTerm(sp,tau("TODO")));
	}
	
	protected Seq build_dedexpr(Expr sp) {
		// TODO : implement the \mapequiv relation
		return `dedexpr(concExpr(sp,true));
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
					System.out.println("How to conclude with: "+ res);
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

}


