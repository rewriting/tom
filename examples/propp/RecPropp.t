/* From Propositional prover by H. Cirstea */
package Propp;

import aterm.*;
import aterm.pure.*;
import Propp.seq.*;
import Propp.seq.types.*;
import java.io.*;
import antlr.CommonAST;

public class RecPropp extends Propp {

	private Factory factory;

	// ------------------------------------------------------------  
	%include { seq.tom }
	// ------------------------------------------------------------  

	public RecPropp() {
		this(new Factory(new PureFactory()));
	}
		
	public RecPropp(Factory factory) {
		this.factory = factory;
	}

	//{{{ public void run(String query)
	public void run(String query) {
		Sequent initSeq = makeQuery(query);
		Sequent search    = `PROOF();

		long startChrono = System.currentTimeMillis();
		ATerm res = Step(initSeq);
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
		ListPair tex_proofs = `concPair();
		%match(ListProof proofTerm) {
			concProof(_*,p,_*) -> {
				tex_proofs = insertPair(`pair(1,proofToTex(p)),tex_proofs);
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

			// {{{	negd
			seq(concPred(X*),concPred(Y*,neg(Z),R*)) -> {
				Sequent prod = `seq(concPred(X*,Z),concPred(Y*,R*));
				rules_appl.add(`rappl(negd,subject,concSequent(prod)));
				return Step(prod);
			}
			// }}}

			//{{{ disjd
			seq(concPred(X*),concPred(Y*,vee(Z,R),S*)) -> {
				Sequent prod = `seq(concPred(X*),concPred(Y*,Z,R,S*));
				rules_appl.add(`rappl(disjd,subject,concSequent(prod)));
				return Step(prod);
			}
			//}}}			

			//{{{ impd
			seq(concPred(X*),concPred(S*,impl(Y,Z),R*)) -> {
				Sequent prod = `seq(concPred(X*,Y),concPred(S*,Z,R*));
				rules_appl.add(`rappl(impd,subject,concSequent(prod)));
				return Step(prod);
			}
			//}}}

			//{{{ negg
			seq(concPred(X*,neg(Y),S*),concPred(Z*)) -> {
				Sequent prod = `seq(concPred(X*,S*),concPred(Y,Z*));
				rules_appl.add(`rappl(negg,subject,concSequent(prod)));
				return Step(prod);
			}
			//}}}

			//{{{ conjg
			seq(concPred(X*,wedge(Y,Z),S*),concPred(R*)) -> {
				Sequent prod = `seq(concPred(X*,Y,Z,S*),concPred(R*));		
				rules_appl.add(`rappl(conjg,subject,concSequent(prod)));
				return Step(prod);
			}
			//}}}

			//{{{ disjg
			seq(concPred(X*,vee(Y,Z),S*),concPred(R*)) -> {
				Sequent s1 = `seq(concPred(X*,Y,S*),concPred(R*));
				Sequent s2 = `seq(concPred(X*,Z,S*),concPred(R*));
				ListSequent l1 = Step(s1);
				ListSequent l2 = Step(s2);
				rules_appl.add(`rappl(disjg,subject,concSequent(s1,s2)));
				return `concSequent(l1*,l2*);
			}
			//}}}

			//{{{ conjd
			seq(concPred(R*),concPred(X*,wedge(Y,Z),S*)) -> {
				Sequent s1 = `seq(concPred(R*),concPred(X*,Y,S*));
				Sequent s2 = `seq(concPred(R*),concPred(X*,Z,S*));
				ListSequent l1 = Step(s1);	
				ListSequent l2 = Step(s2);	
				rules_appl.add(`rappl(conjd,subject,concSequent(s1,s2)));
				return `concSequent(l1*,l2*);
			}
			//}}}

			//{{{ impg
			seq(concPred(X*,impl(Y,Z),S*),concPred(R*)) -> {
				Sequent s1 = `seq(concPred(X*,S*),concPred(R*,Y));
				Sequent s2 = `seq(concPred(X*,Z,S*),concPred(R*));
				ListSequent l1 = Step(s1);
				ListSequent l2 = Step(s2);
				rules_appl.add(`rappl(impg,subject,concSequent(s1,s2)));
				return `concSequent(l1*,l2*);
			}
			//}}}

			//{{{ axio
			seq(concPred(_*,X,_*),concPred(_*,X,_*)) -> {
				if (X != `EmptyP()) {
					Sequent prod = `PROOF();
					rules_appl.add(`rappl(axiom,subject,concSequent()));
					return `concSequent(prod);
				}
			}
			//}}}

			_ -> { return `concSequent(END()); }

		}// end %match

	}
	//}}}

	//{{{ public final static void main(String[] args)
	public static void main(String[] args) {
		RecPropp test = new RecPropp(new Factory(new PureFactory()));

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
