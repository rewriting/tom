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

		//System.out.println("Traces = " + rules_appl);
		// Process Traces

		System.out.println("Build Proof Term");
		ListProof proofTerm = buildProofTerm(initSeq,rules_appl);
		System.out.println("Proof term = " + proofTerm);
		Collection tex_proofs = new HashSet();
		%match(ListProof proofTerm) {
			concProof(_*,p,_*) -> {
				tex_proofs.add(proofToTex(p));
			}
		}

		System.out.println("Build LaTeX");
		write_proof_latex(tex_proofs,"proof.tex");
		System.out.println("Latex : " + tex_proofs);
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

	Collection rules_appl = new HashSet();
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
					seq(concPred(X*),concPred(Y*,neg(Z),R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Z),concPred(Y*,R*));
						c.add(prod);
						rules_appl.add(`rappl(negd,subject,concSequent(prod)));
					}
					// }}}

					//{{{ disjd
					seq(concPred(X*),concPred(Y*,vee(Z,R),S*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*),concPred(Y*,Z,R,S*));
						c.add(prod);
						rules_appl.add(`rappl(disjd,subject,concSequent(prod)));
					}
					//}}}			

					//{{{ impd
					seq(concPred(X*),concPred(S*,impl(Y,Z),R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Y),concPred(S*,Z,R*));
						c.add(prod);
						rules_appl.add(`rappl(impd,subject,concSequent(prod)));
					}
					//}}}

					//{{{ negg
					seq(concPred(X*,neg(Y),S*),Z) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,S*),concPred(Y,Z*));
						c.add(prod);
						rules_appl.add(`rappl(negg,subject,concSequent(prod)));
					}
					//}}}

					//{{{ conjg
					seq(concPred(X*,wedge(Y,Z),S*),concPred(R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Y,Z,S*),concPred(R*));
						c.add(prod);
						rules_appl.add(`rappl(conjg,subject,concSequent(prod)));
					}
					//}}}

					//{{{ disjg
					seq(concPred(X*,vee(Y,Z),S*),concPred(R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Y,S*),concPred(R*));
						c.add(prod);

						Sequent prod2 = `seq(concPred(X*,Z,S*),concPred(R*));
						c.add(prod2);
						rules_appl.add(`rappl(disjg,subject,concSequent(prod,prod2)));
					}
					//}}}

					//{{{ conjd
					seq(concPred(R*),concPred(X*,vee(Y,Z),S*)) -> {
						match = true;
						Sequent prod = `seq(concPred(R*),concPred(X*,Y,S*));
						c.add(prod);

						Sequent prod2 = `seq(concPred(R*),concPred(X*,Z,S*));
						c.add(prod2);
						rules_appl.add(`rappl(conjd,subject,concSequent(prod,prod2)));
					}
					//}}}

					//{{{ impg
					seq(concPred(X*,impl(Y,Z),S*),concPred(R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,S*),concPred(R*,Y));
						c.add(prod);

						Sequent prod2 = `seq(concPred(X*,Z,S*),concPred(R*));
						c.add(prod2);
						rules_appl.add(`rappl(impg,subject,concSequent(prod,prod2)));
					}
					//}}}

					//{{{ axio
					seq(concPred(_*,X,_*),concPred(_*,X,_*)) -> {
						if (X != `EmptyP()) {
							match = true;
							Sequent prod = `PROOF();
							c.add(prod);
							rules_appl.add(`rappl(axiom,subject,concSequent()));
						}
					}
					//}}}

					//{{{ keep
					PROOF() -> {
						match = true;
						c.add(`PROOF());
					}
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

	//{{{ public ListProof buildProofTerm(Sequent goal, Collection trace) {
	public ListProof buildProofTerm(Sequent goal, Collection trace) {
		ListProof tmpsol = `concProof();
		Iterator iter = trace.iterator();
		while (iter.hasNext()) {
			Trace item = (Trace)iter.next();
			//{{{ %match(Trace item)
			%match(Trace item) {
				rappl(r,s,concSequent(p)) -> {
					if (s == goal) {
						ListProof proof_p = buildProofTerm(p,trace);
						%match(ListProof proof_p) {
							concProof(_*,elem,_*) -> {
								tmpsol = `concProof(rule(r,goal,concProof(elem)),tmpsol*);
							}
						}
					}
				}

				rappl(r,s,concSequent(p,pp)) -> {
					if (s == goal) {
						ListProof proof_p = buildProofTerm(p,trace);
						ListProof proof_pp = buildProofTerm(pp,trace);
						%match(ListProof proof_p) {
							concProof(_*,elem,_*) -> {
								%match(ListProof proof_pp) {
									concProof(_*,elemn,_*) -> {
										tmpsol = `concProof(rule(r,goal,concProof(elem,elemn)),tmpsol*);
									}
								}
							}
						}
					}
				}

				rappl(r,s,concSequent()) -> {
					if (s == goal) {
						tmpsol = `concProof(rule(r,s,concProof()),tmpsol*);
					}
				}

			}
			//}}}
		}

		return tmpsol;
	}
	//}}}

	//{{{ public String seqToTex(Sequent s)
	public String seqToTex(Sequent s) {
		String latex= "";
		%match(Sequent s) {
			seq(l1,l2) -> {
				latex = listPredToTex(l1) + "\\vdash " + listPredToTex(l2);
			}
		}
		return latex;
	}
	//}}}

	//{{{ public String listPredToTex(ListPred l)
	public String listPredToTex(ListPred l) {
		String latex = "";
		while(!l.isEmpty()) {
			latex += " , " + predToTex(l.getHead());
			l = l.getTail();
		}
		return latex;
	}
	//}}}

	//{{{ public String predToTex(Pred p)
	public String predToTex(Pred pred) {
		%match(Pred pred) {
			neg(p) -> {
				return "\\neg " + predToTex(p);
			}
			equiv(p1,p2) -> {
				return predToTex(p1) + "\\lra " + predToTex(p2);
			}
			impl(p1,p2) -> {
				return predToTex(p1) + "\\to " + predToTex(p2);
			}
			vee(p1,p2) -> {
				return predToTex(p1) + "\\vee " + predToTex(p2);
			}
			wedge(p1,p2) -> {
				return predToTex(p1) + "\\wedge " + predToTex(p2);
			}
			_ -> {
				return pred.toString();
			}
		}
	}
	//}}}

	//{{{ public String proofToTex(Proof proof)
	public String proofToTex(Proof proof) {
		String latex = "";

		//{{{ %match(Proof item)
		%match(Proof proof) {

			rule(r,g,concProof()) -> {
				latex = "\\infer[\\"+r.toString()+"]{" + seqToTex(g) + "}{\\mbox{}}";
			}

			rule(r,g,concProof(p)) -> {
				latex = "\\infer[\\"+r.toString()+"]{" + seqToTex(g) + "}{" + proofToTex(p) + "}";
			}

			rule(r,g,concProof(p,pp)) -> {
				latex = "\\infer[\\"+r.toString()+"]{" + seqToTex(g) + "}{" + proofToTex(p) + " & " + proofToTex(pp) + "}";
			}

		}
		//}}}

		return latex;
	}
	//}}}

	//{{{ public void write_proof_latex(Collection tex_p,String file)
	public void write_proof_latex(Collection tex_p,String file) {
		try {
			File target = new File(file);
			FileOutputStream out = new FileOutputStream(target);

			OutputStreamWriter osw = new OutputStreamWriter(out,"ISO-8859-1");

			osw.write("\\documentstyle[proof]{article}\n\\def\\negd{\\neg_D}\n\\def\\disjd{\\vee_D}\n\\def\\impd{\\to_D}\n\\def\\negg{\\neg_G}\n\\def\\conjg{\\wedge_G}\n\\def\\disjg{\\vee_G}\n\\def\\conjd{\\wedge_D}\n\\def\\impg{\\to_G}\n\\def\\axiom{Axiom}\n\n\\begin{document}");
			osw.write("\n\n\n");
			Iterator iter = tex_p.iterator();
			while(iter.hasNext()) {
				osw.write((String)iter.next());
				osw.write("\n\\vspace{3cm}\n\n");
			}
			osw.write("\n\\end{document}");

			osw.flush();
			out.flush();
			osw.close();
			out.close();
		} catch(Exception e) {
			System.out.println("Write failed : " + e);
		}
	}
	//}}}

	//{{{ public final static void main(String[] args)
	public static void main(String[] args) {
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
