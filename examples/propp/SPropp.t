/*
 * Copyright (c) 2004, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/* From Propositional prover by H. Cirstea */
package propp;

import aterm.*;
import aterm.pure.*;

import java.io.*;
import java.util.*;

import jjtraveler.TravelerFactory;
import jjtraveler.reflective.VisitableVisitor;

import propp.seq.*;
import propp.seq.types.*;

import antlr.CommonAST;

class SPropp extends Propp1 {

	private Factory factory;
  private TravelerFactory travelerFactory;

	// ------------------------------------------------------------  
	%include { seq.tom }
	// ------------------------------------------------------------  

	public SPropp() {
		this(Factory.getInstance(SingletonFactory.getInstance()));
    this.travelerFactory = new TravelerFactory();
	}
		
	public SPropp(Factory factory) {
		this.factory = factory;
    this.travelerFactory = new TravelerFactory();
	}
	
	//{{{ public void run(String query)
	public void run(String query) {
		Sequent initSeq = makeQuery(query);
		Proof initP = `hyp(initSeq);

		long startChrono = System.currentTimeMillis();
		Proof proofTerm = solve(initP);
		long stopChrono = System.currentTimeMillis();

		System.out.println("Proof term = " + proofTerm);
		ListPair tex_proofs = `concPair();
		/*
		%match(ListProof proofTerm) {
			concProof(_*,p,_*) -> {
				tex_proofs.add(proofToTex(p));
			}
		}
		*/
		System.out.println("Is input proved ? " + isValid(proofTerm));

		tex_proofs = `concPair(pair(1,proofToTex(proofTerm)));

		System.out.println("Build LaTeX");
		write_proof_latex(tex_proofs,"proof.tex");

		System.out.println("Latex : " + tex_proofs);
		System.out.println("res found in " + (stopChrono-startChrono) + " ms");

	}
	//}}}

	Collection result = new HashSet();

	public Proof solve(Proof init) {

		VisitableVisitor rule = new CalculusRules();
		VisitableVisitor bottomUp = travelerFactory.BottomUp(rule);

		Proof res = null;
		try {
			res = (Proof)bottomUp.visit(init);
		} catch (jjtraveler.VisitFailure e) {
			return init;
		}
		if (res != init) {
			res = solve(res);
		}
		return res;
	}

	class CalculusRules extends propp.seq.VisitableFwd {
		public CalculusRules() {
			super(new jjtraveler.Identity());
		}
		
		public Proof visit_Proof(Proof subject) throws jjtraveler.VisitFailure {
			%match(Proof subject) {
				hyp(arg) -> {
					%match(Sequent arg) {

						// {{{	negd
						seq(concPred(X*),concPred(Y*,neg(Z),R*)) -> {
							Proof prod = `hyp(seq(concPred(X*,Z),concPred(Y*,R*)));
							return `rule(
								negd,
								seq(concPred(X*),concPred(Y*,mark(neg(Z)),R*)),
								concProof(prod));
						}
						// }}}

						//{{{ disjd
						seq(concPred(X*),concPred(Y*,vee(Z,R),S*)) -> {
							Proof prod = `hyp(seq(concPred(X*),concPred(Y*,Z,R,S*)));
							return `rule(
								disjd,
								seq(concPred(X*),concPred(Y*,mark(vee(Z,R)),S*)),
								concProof(prod));
						}
						//}}}			

						//{{{ impd
						seq(concPred(X*),concPred(S*,impl(Y,Z),R*)) -> {
							Proof prod = `hyp(seq(concPred(X*,Y),concPred(S*,Z,R*)));
							return `rule(
								impd,
								seq(concPred(X*),concPred(S*,mark(impl(Y,Z)),R*)),
								concProof(prod));
						}
						//}}}

						//{{{ negg
						seq(concPred(X*,neg(Y),S*),concPred(Z*)) -> {
							Proof prod = `hyp(seq(concPred(X*,S*),concPred(Y,Z*)));
							return `rule(
								negg,
								seq(concPred(X*,mark(neg(Y)),S*),concPred(Z*)),
								concProof(prod));
						}
						//}}}

						//{{{ conjg
						seq(concPred(X*,wedge(Y,Z),S*),concPred(R*)) -> {
							Proof prod = `hyp(seq(concPred(X*,Y,Z,S*),concPred(R*)));
							return `rule(
								conjg,
								seq(concPred(X*,mark(wedge(Y,Z)),S*),concPred(R*)),
								concProof(prod));
						}
						//}}}

						//{{{ disjg
						seq(concPred(X*,vee(Y,Z),S*),concPred(R*)) -> {
							Proof s1 = `hyp(seq(concPred(X*,Y,S*),concPred(R*)));
							Proof s2 = `hyp(seq(concPred(X*,Z,S*),concPred(R*)));
							return `rule(
								disjg,
								seq(concPred(X*,mark(vee(Y,Z)),S*),concPred(R*)),
								concProof(s1,s2));
						}
						//}}}

						//{{{ conjd
						seq(concPred(R*),concPred(X*,wedge(Y,Z),S*)) -> {
							Proof s1 = `hyp(seq(concPred(R*),concPred(X*,Y,S*)));
							Proof s2 = `hyp(seq(concPred(R*),concPred(X*,Z,S*)));
							return `rule(
								conjd,
								seq(concPred(R*),concPred(X*,mark(wedge(Y,Z)),S*)),
								concProof(s1,s2));
						}
						//}}}

						//{{{ impg
						seq(concPred(X*,impl(Y,Z),S*),concPred(R*)) -> {
							Proof s1 = `hyp(seq(concPred(X*,S*),concPred(R*,Y)));
							Proof s2 = `hyp(seq(concPred(X*,Z,S*),concPred(R*)));
							return `rule(
								impg,
								seq(concPred(X*,mark(impl(Y,Z)),S*),concPred(R*)),
								concProof(s1,s2));
						}
						//}}}

						//{{{ axio
						seq(concPred(L1*,X,L2*),concPred(L3*,X,L4*)) -> {
							if (`X != `EmptyP()) {
								return `rule(
									axiom,
									seq(concPred(L1*,mark(X),L2*),concPred(L3*,mark(X),L4*)),
									concProof());
							}
						}
						//}}}

					}// end %match
				}
			}
			return subject;
		}
	}
 

	//{{{ public final static void main(String[] args)
	public static void main(String[] args) {
		SPropp test = new SPropp(Factory.getInstance(SingletonFactory.getInstance()));

		String query ="";
		try {
			//query = args[0];
			SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
			SeqParser parser = new SeqParser(lexer);
			// Parse the input expression
			parser.seq();
			CommonAST t = (CommonAST)parser.getAST();
			// Print the resulting tree out in LISP notation
			// System.out.println(t.toStringList());
			// System.out.println("Applying treewalker");
			SeqTreeWalker walker = new SeqTreeWalker();
			// Traverse the tree created by the parser
			query = walker.seq(t);
			System.out.println("Query : "+query);
		} catch (Exception e) {
			System.err.println("exception: "+e);
			return;
		}
		test.run(query);
	}
	//}}}

}
