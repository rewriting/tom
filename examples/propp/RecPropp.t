/* From Propositional prover by H. Cirstea */

import aterm.*;
import aterm.pure.*;
import java.util.*;
import jtom.runtime.*;
import adt.propp.*;
import java.io.*;
import antlr.CommonAST;

public class RecPropp {

	private TermFactory factory;
	private GenericTraversal traversal;

	// ------------------------------------------------------------  
	%include { adt/propp/term.tom }
	// ------------------------------------------------------------  

	public RecPropp(TermFactory factory) {
		this.factory = factory;
		this.traversal = new GenericTraversal();
	}

	public final TermFactory getTermFactory() {
		return factory;
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

		System.out.println("Traces = " + traces);
		System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");

	}
	//}}}

	//{{{ public Seq makeQuery(String input)
	public Sequent makeQuery(String input) {

		Sequent query = factory.SequentFromString(input);
		return query;
	}
	//}}}

	Map traces = new HashMap();
	Collection result = new HashSet();

	//{{{ public ListSequent Step(Sequent subject)
	public ListSequent Step(Sequent subject) {
		%match(Sequent subject) {

			// {{{	negd
			seq(concPred(X*),concPred(Y*,neg(Z),R*)) -> {
				Sequent prod = `seq(concPred(X*,Z),concPred(Y*,R*));
				traces.put(subject,`pair(negd,prod));
				return Step(prod);
			}
			// }}}

			//{{{ disjd
			seq(concPred(X*),concPred(Y*,vee(Z,R),S*)) -> {
				Sequent prod = `seq(concPred(X*),concPred(Y*,Z,R,S*));
				traces.put(subject,`pair(disjd,prod));
				return Step(prod);
			}
			//}}}			

			//{{{ impd
			seq(concPred(X*),concPred(S*,impl(Y,Z),R*)) -> {
				Sequent prod = `seq(concPred(X*,Y),concPred(S*,Z,R*));
				traces.put(subject,`pair(impd,prod));
				return Step(prod);
			}
			//}}}

			//{{{ negg
			seq(concPred(X*,neg(Y),S*),concPred(Z*)) -> {
				Sequent prod = `seq(concPred(X*,S*),concPred(Y,Z*));
				traces.put(subject,`pair(negg,prod));
				return Step(prod);
			}
			//}}}

			//{{{ conjg
			seq(concPred(X*,wedge(Y,Z),S*),concPred(R*)) -> {
				Sequent prod = `seq(concPred(X*,Y,Z,S*),concPred(R*));		
				traces.put(subject,`pair(conjg,prod));
				return Step(prod);
			}
			//}}}

			//{{{ disjg
			seq(concPred(X*,vee(Y,Z),S*),concPred(R*)) -> {
				Sequent s1 = `seq(concPred(X*,Y,S*),concPred(R*));
				traces.put(subject,`pair(disjg,s1));
				Sequent s2 = `seq(concPred(X*,Z,S*),concPred(R*));
				traces.put(subject,`pair(disjg,s2));
				ListSequent l1 = Step(s1);
				ListSequent l2 = Step(s2);
				return `concSequent(l1*,l2*);
			}
			//}}}

			//{{{ conjd
			seq(concPred(R*),concPred(X*,vee(Y,Z),S*)) -> {
				Sequent s1 = `seq(concPred(R*),concPred(X*,Y,S*));
				traces.put(subject,`pair(conjg,s1));
				Sequent s2 = `seq(concPred(R*),concPred(X*,Z,S*));
				traces.put(subject,`pair(conjg,s2));
				ListSequent l1 = Step(s1);	
				ListSequent l2 = Step(s2);	
				return `concSequent(l1*,l2*);
			}
			//}}}

			//{{{ impg
			seq(concPred(X*,impl(Y,Z),S*),concPred(R*)) -> {
				Sequent s1 = `seq(concPred(X*,S*),concPred(R*,Y));
				traces.put(subject,`pair(conjg,s1));
				Sequent s2 = `seq(concPred(X*,Z,S*),concPred(R*));
				traces.put(subject,`pair(conjg,s2));
				ListSequent l1 = Step(s1);
				ListSequent l2 = Step(s2);
				return `concSequent(l1*,l2*);
			}
			//}}}

			//{{{ axio
			seq(concPred(_*,X,_*),concPred(_*,X,_*)) -> {
				if (X != `EmptyP()) {
					Sequent prod = `PROOF();
					traces.put(subject,`pair(axiom,prod));
					return `concSequent(prod);
				}
			}
			//}}}

			_ -> { return `concSequent(END()); }

		}// end %match

	}
	//}}}

	//{{{ public final static void main(String[] args)
	public final static void main(String[] args) {
		RecPropp test = new RecPropp(new TermFactory(new PureFactory()));

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
