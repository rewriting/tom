/* From Propositional prover by H. Cirstea */

import aterm.*;
import aterm.pure.*;
import java.util.*;
import jtom.runtime.*;
import adt.propp.*;
import java.io.*;
import antlr.CommonAST;

public class Propp {

	private TermFactory factory;
	private GenericTraversal traversal;

	// ------------------------------------------------------------  
	%include { adt/propp/term.tom }
	// ------------------------------------------------------------  

	public Propp(TermFactory factory) {
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
		boolean res      = proofSearch(initSeq,search);
		//boolean res      = breadthSearch(initSeq,search);
		//boolean res      = depthSearch(initSeq,search);
		//boolean res      = depthSearch2(new HashSet(),initSeq,search);
		long stopChrono = System.currentTimeMillis();

		System.out.println("Traces = " + traces);
		System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");

	}
	//}}}

	//{{{ public Sequent makeQuery(String input)
	public Sequent makeQuery(String input) {

		Sequent query = factory.SequentFromString(input);
		return query;
	}
	//}}}

	//{{{ public boolean breadthSearch(Sequent start, Sequent end)
	public boolean breadthSearch(Sequent start, Sequent end) {
		Collection c1 = new HashSet();
		c1.add(start);

		int i = 0;
		while(!c1.isEmpty()) {
			Collection c2 = new HashSet();
			Iterator it = c1.iterator();
			while(it.hasNext()) {
				OneStep((Sequent)it.next(),c2);
			}

			System.out.print("iteration " + i + ":");
			System.out.print("\tc2: " + c2.size());
			//System.out.print("\tresult.size = " + result.size());
			System.out.println();

			c1 = c2;
			if(c2.contains(end)) {
				return true;
			}
			i++;
		}
		return false;
	}
	//}}}

	//{{{ public boolean proofSearch(Sequent start, Sequent end)
	public boolean proofSearch(Sequent start, Sequent end) {
		Collection c1 = new HashSet();
		c1.add(start);

		int i = 0;
		while(!c1.isEmpty()) {
			Collection c2 = new HashSet();
			System.out.println(c1);
			Iterator it = c1.iterator();
			while(it.hasNext()) {
				OneStep((Sequent)it.next(),c2);
			}

			System.out.print("iteration " + i + ":");
			System.out.print("\tc2: " + c2.size());
			System.out.println();

			c1 = c2;

			if(c2.contains(end) && c2.size() == 1 ) {
				return true;
			}
			if(c2.contains(`END()) && c2.size() <= 2 ) {
				return false;
			}
			i++;
		}
		return false;
	}
	//}}}

	Map traces = new HashMap();
	Collection result = new HashSet();

	//{{{ public void OneStep(Sequent subject, final Collection collection) 
	public void OneStep(Sequent subject, final Collection collection) {
		Collect2 collect = new Collect2() { 
			public boolean apply(ATerm subjectAT, Object arg1) {
				Sequent subject = (Sequent)subjectAT;
				Collection c = (Collection) arg1;
				boolean match = false;
				%match(Sequent subject) {

					// {{{	negd
					label: s@seq(concPred(X*),concPred(Y*,neg(Z),R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Z),concPred(Y*,R*));
						c.add(prod);
						traces.put(s,`pair(negd,prod));
					}
					// }}}

					//{{{ disjd
					seq(concPred(X*),concPred(Y*,vee(Z,R),S*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*),concPred(Y*,Z,R,S*));
						c.add(prod);
						traces.put(subject,`pair(disjd,prod));
					}
					//}}}			

					//{{{ impd
					seq(concPred(X*),concPred(S*,impl(Y,Z),R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Y),concPred(S*,Z,R*));
						c.add(prod);
						traces.put(subject,`pair(impd,prod));
					}
					//}}}

					//{{{ negg
					seq(concPred(X*,neg(Y),S*),Z) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,S*),concPred(Y,Z*));
						c.add(prod);
						traces.put(subject,`pair(negg,prod));
					}
					//}}}

					//{{{ conjg
					seq(concPred(X*,wedge(Y,Z),S*),concPred(R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Y,Z,S*),concPred(R*));
						c.add(prod);
						traces.put(subject,`pair(conjg,prod));
					}
					//}}}

					//{{{ disjg
					seq(concPred(X*,vee(Y,Z),S*),concPred(R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Y,S*),concPred(R*));
						c.add(prod);
						traces.put(subject,`pair(disjg,prod));

						Sequent prod2 = `seq(concPred(X*,Z,S*),concPred(R*));
						c.add(prod2);
						traces.put(subject,`pair(disjg,prod2));
					}
					//}}}

					//{{{ conjd
					seq(concPred(R*),concPred(X*,vee(Y,Z),S*)) -> {
						match = true;
						Sequent prod = `seq(concPred(R*),concPred(X*,Y,S*));
						c.add(prod);
						traces.put(subject,`pair(conjg,prod));

						Sequent prod2 = `seq(concPred(R*),concPred(X*,Z,S*));
						c.add(prod2);
						traces.put(subject,`pair(conjg,prod2));
					}
					//}}}

					//{{{ impg
					seq(concPred(X*,impl(Y,Z),S*),concPred(R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,S*),concPred(R*,Y));
						c.add(prod);
						traces.put(subject,`pair(conjg,prod));

						Sequent prod2 = `seq(concPred(X*,Z,S*),concPred(R*));
						c.add(prod2);
						traces.put(subject,`pair(conjg,prod2));
					}
					//}}}

					//{{{ axio
					seq(concPred(_*,X,_*),concPred(_*,X,_*)) -> {
						if (X != `EmptyP()) {
							match = true;
							Sequent prod = `PROOF();
							c.add(prod);
							traces.put(subject,`pair(axiom,prod));
						}
					}
					//}}}

					//{{{ keep
					PROOF -> {
						match = true;
						c.add(`PROOF());
					}
					//END   -> {
					//	match = true;
					//	c.add(`END());
					//}
					//}}}

				}// end %match

				if (match == false) {
					System.out.println("Pas de match pour :" + subject);
					c.add(`END());
				}

				return true;
			} //end apply
		}; // end new

		collect.apply(subject,collection);

		//traversal.genericCollect(subject, collect, collection); 
	}
	//}}}

	//{{{ public final static void main(String[] args)
	public final static void main(String[] args) {
		Propp test = new Propp(new TermFactory(new PureFactory()));

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
