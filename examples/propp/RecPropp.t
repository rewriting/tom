/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

import propp.seq.*;
import propp.seq.types.*;
import java.io.*;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

public class RecPropp extends SPropp {

	%include { seq/Seq.tom }

	//{{{ public void run(Sequent query)
	public void run(Sequent query) {
		Sequent initSeq = query;
		Sequent search    = `PROOF();

		long startChrono = System.currentTimeMillis();
		ListSequent res = Step(initSeq);
		//boolean res      = proofSearch(initSeq,search);
		//boolean res      = breadthSearch(initSeq,search);
		//boolean res      = depthSearch(initSeq,search);
		//boolean res      = depthSearch2(new HashSet(),initSeq,search);
		long stopChrono = System.currentTimeMillis();

		// System.out.println("Traces = " + traces);
		// Process Traces

		System.out.println("Build Proof Term");
		ListProof proofTerm = buildProofTerm(initSeq);
		System.out.println("Proof term = " + proofTerm);
		ListPair tex_proofs = `ConcPair();
		%match(ListProof proofTerm) {
			ConcProof(_*,p,_*) -> {
				tex_proofs = insertPair(`Pair(1,proofToTex(p)),tex_proofs);
			}
		}

		System.out.println("Build LaTeX");
		write_proof_latex(tex_proofs,"proof.tex");

		System.out.println("Latex : " + tex_proofs);
		System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");

	}
	//}}}

	//{{{ public ListSequent Step(Sequent subject)
	public ListSequent Step(Sequent subject) {
		%match(Sequent subject) {

			// {{{	Negd
			Seq(ConcPred(X*),ConcPred(Y*,Neg(Z),R*)) -> {
				Sequent prod = `Seq(ConcPred(X*,Z),ConcPred(Y*,R*));
				Rules_appl.add(`Rappl(Negd(),subject,ConcSequent(prod)));
				return Step(prod);
			}
			// }}}

			//{{{ Disjd
			Seq(ConcPred(X*),ConcPred(Y*,Vee(Z,R),S*)) -> {
				Sequent prod = `Seq(ConcPred(X*),ConcPred(Y*,Z,R,S*));
				Rules_appl.add(`Rappl(Disjd(),subject,ConcSequent(prod)));
				return Step(prod);
			}
			//}}}			

			//{{{ Impd
			Seq(ConcPred(X*),ConcPred(S*,Impl(Y,Z),R*)) -> {
				Sequent prod = `Seq(ConcPred(X*,Y),ConcPred(S*,Z,R*));
				Rules_appl.add(`Rappl(Impd(),subject,ConcSequent(prod)));
				return Step(prod);
			}
			//}}}

			//{{{ Negg
			Seq(ConcPred(X*,Neg(Y),S*),ConcPred(Z*)) -> {
				Sequent prod = `Seq(ConcPred(X*,S*),ConcPred(Y,Z*));
				Rules_appl.add(`Rappl(Negg(),subject,ConcSequent(prod)));
				return Step(prod);
			}
			//}}}

			//{{{ Conjg
			Seq(ConcPred(X*,Wedge(Y,Z),S*),ConcPred(R*)) -> {
				Sequent prod = `Seq(ConcPred(X*,Y,Z,S*),ConcPred(R*));
				Rules_appl.add(`Rappl(Conjg(),subject,ConcSequent(prod)));
				return Step(prod);
			}
			//}}}

			//{{{ Disjg
			Seq(ConcPred(X*,Vee(Y,Z),S*),ConcPred(R*)) -> {
				Sequent s1 = `Seq(ConcPred(X*,Y,S*),ConcPred(R*));
				Sequent s2 = `Seq(ConcPred(X*,Z,S*),ConcPred(R*));
				ListSequent l1 = Step(s1);
				ListSequent l2 = Step(s2);
				Rules_appl.add(`Rappl(Disjg(),subject,ConcSequent(s1,s2)));
				return `ConcSequent(l1*,l2*);
			}
			//}}}

			//{{{ Conjd
			Seq(ConcPred(R*),ConcPred(X*,Wedge(Y,Z),S*)) -> {
				Sequent s1 = `Seq(ConcPred(R*),ConcPred(X*,Y,S*));
				Sequent s2 = `Seq(ConcPred(R*),ConcPred(X*,Z,S*));
				ListSequent l1 = Step(s1);	
				ListSequent l2 = Step(s2);	
				Rules_appl.add(`Rappl(Conjd(),subject,ConcSequent(s1,s2)));
				return `ConcSequent(l1*,l2*);
			}
			//}}}

			//{{{ Impg
			Seq(ConcPred(X*,Impl(Y,Z),S*),ConcPred(R*)) -> {
				Sequent s1 = `Seq(ConcPred(X*,S*),ConcPred(R*,Y));
				Sequent s2 = `Seq(ConcPred(X*,Z,S*),ConcPred(R*));
				ListSequent l1 = Step(s1);
				ListSequent l2 = Step(s2);
				Rules_appl.add(`Rappl(Impg(),subject,ConcSequent(s1,s2)));
				return `ConcSequent(l1*,l2*);
			}
			//}}}

			//{{{ axio
			Seq(ConcPred(_*,X,_*),ConcPred(_*,X,_*)) -> {
				if (`X != `EmptyP()) {
					Sequent prod = `PROOF();
					Rules_appl.add(`Rappl(Axiom(),subject,ConcSequent()));
					return `ConcSequent(prod);
				}
			}
			//}}}

		}// end %match
		return `ConcSequent(END());

	}
	//}}}

	//{{{ public final static void main(String[] args)
	public static void main(String[] args) {
		RecPropp test = new RecPropp();

		Sequent query = null;
		try {
			//query = args[0];
			SeqLexer lexer = new SeqLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
			SeqParser parser = new SeqParser(tokens);
			Tree t = (Tree) parser.seq().getTree();
      query = (Sequent) SeqAdaptor.getTerm(t);
			System.out.println("Query : "+query);
		} catch (Exception e) {
			System.err.println("exception: "+e);
			return;
		}
		test.run(query);
	}
	//}}}

}
