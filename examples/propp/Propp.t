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

		System.out.println("Traces = " + rules_appl);
		// Process Traces
		ListSequent proofTerm = buildProofTerm(initSeq,rules_appl);
		System.out.println("Proof term = " + proofTerm);
		Collection tex_proofs = new HashSet();
		%match(ListSequent proofTerm) {
			concSequent(_*,p,_*) -> {
				tex_proofs.add(proofToTex(p));
			}
		}
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

	Map traces = new HashMap();
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
						rules_appl.add(`negd(subject,prod));
					}
					// }}}

					//{{{ disjd
					seq(concPred(X*),concPred(Y*,vee(Z,R),S*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*),concPred(Y*,Z,R,S*));
						c.add(prod);
						//add_trace(subject,`pair(disjd,prod));
						rules_appl.add(`disjd(subject,prod));
					}
					//}}}			

					//{{{ impd
					seq(concPred(X*),concPred(S*,impl(Y,Z),R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Y),concPred(S*,Z,R*));
						c.add(prod);
						rules_appl.add(`impd(subject,prod));
					}
					//}}}

					//{{{ negg
					seq(concPred(X*,neg(Y),S*),Z) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,S*),concPred(Y,Z*));
						c.add(prod);
						rules_appl.add(`negg(subject,prod));
					}
					//}}}

					//{{{ conjg
					seq(concPred(X*,wedge(Y,Z),S*),concPred(R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Y,Z,S*),concPred(R*));
						c.add(prod);
						rules_appl.add(`conjg(subject,prod));
					}
					//}}}

					//{{{ disjg
					seq(concPred(X*,vee(Y,Z),S*),concPred(R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,Y,S*),concPred(R*));
						c.add(prod);

						Sequent prod2 = `seq(concPred(X*,Z,S*),concPred(R*));
						c.add(prod2);
						rules_appl.add(`disjg(subject,prod,prod2));
					}
					//}}}

					//{{{ conjd
					seq(concPred(R*),concPred(X*,vee(Y,Z),S*)) -> {
						match = true;
						Sequent prod = `seq(concPred(R*),concPred(X*,Y,S*));
						c.add(prod);

						Sequent prod2 = `seq(concPred(R*),concPred(X*,Z,S*));
						c.add(prod2);
						rules_appl.add(`conjd(subject,prod,prod2));
					}
					//}}}

					//{{{ impg
					seq(concPred(X*,impl(Y,Z),S*),concPred(R*)) -> {
						match = true;
						Sequent prod = `seq(concPred(X*,S*),concPred(R*,Y));
						c.add(prod);

						Sequent prod2 = `seq(concPred(X*,Z,S*),concPred(R*));
						c.add(prod2);
						rules_appl.add(`impg(subject,prod,prod2));
					}
					//}}}

					//{{{ axio
					seq(concPred(_*,X,_*),concPred(_*,X,_*)) -> {
						if (X != `EmptyP()) {
							match = true;
							Sequent prod = `PROOF();
							c.add(prod);
							rules_appl.add(`axiom(subject));
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

	//{{{ public void add_trace(Sequent subject, Sequent product)
	public void add_trace(Sequent subject, Sequent product) {
		if (!(traces.containsKey(subject))) {
			traces.put(subject,`concSequent(product));
		} else {
			ListSequent tmp = (ListSequent)traces.get(subject);
			traces.put(subject,`concSequent(product,tmp*));
		}
	}
	//}}}

	//{{{ public ListSequent buildProofTerm(Sequent goal, Collection trace) {
	public ListSequent buildProofTerm(Sequent goal, Collection trace) {
		ListSequent tmpsol = `concSequent();
		Iterator iter = trace.iterator();
		while (iter.hasNext()) {
			Sequent item = (Sequent)iter.next();
			//{{{ %match(Sequent item)
			%match(Sequent item) {
				negd(s,p) -> {
					if (s == goal) {
						ListSequent proof_p = buildProofTerm(p,trace);
						%match(ListSequent proof_p) {
							concSequent(_*,elem,_*) -> {
								tmpsol = `concSequent(negd(goal,elem),tmpsol*);
							}
						}
					}
				}

				disjd(s,p) -> {
					if (s == goal) {
					ListSequent proof_p = buildProofTerm(p,trace);
					%match(ListSequent proof_p) {
						concSequent(_*,elem,_*) -> {
							tmpsol = `concSequent(disjd(goal,elem),tmpsol*);
						}
					}
					}
				}

				impd(s,p) -> {
					if (s == goal) {
					ListSequent proof_p = buildProofTerm(p,trace);
					%match(ListSequent proof_p) {
						concSequent(_*,elem,_*) -> {
							tmpsol = `concSequent(impd(goal,elem),tmpsol*);
						}
					}
					}
				}

				negg(s,p) -> {
					if (s == goal) {
					ListSequent proof_p = buildProofTerm(p,trace);
					%match(ListSequent proof_p) {
						concSequent(_*,elem,_*) -> {
							tmpsol = `concSequent(negg(goal,elem),tmpsol*);
						}
					}
					}
				}

				conjg(s,p) -> {
					if (s == goal) {
					ListSequent proof_p = buildProofTerm(p,trace);
					%match(ListSequent proof_p) {
						concSequent(_*,elem,_*) -> {
							tmpsol = `concSequent(conjg(goal,elem),tmpsol*);
						}
					}
					}
				}

				disjg(s,p,pp) -> {
					if (s == goal) {
					ListSequent proof_p = buildProofTerm(p,trace);
					ListSequent proof_pp = buildProofTerm(pp,trace);
					%match(ListSequent proof_p) {
						concSequent(_*,elem,_*) -> {
							%match(ListSequent proof_pp) {
								concSequent(_*,elemn,_*) -> {
									tmpsol = `concSequent(disjg(goal,elem,elemn),tmpsol*);
								}
							}
						}
					}
					}
				}
				
				conjd(s,p,pp) -> {
					if (s == goal) {
					ListSequent proof_p = buildProofTerm(p,trace);
					ListSequent proof_pp = buildProofTerm(pp,trace);
					%match(ListSequent proof_p) {
						concSequent(_*,elem,_*) -> {
							%match(ListSequent proof_pp) {
								concSequent(_*,elemn,_*) -> {
									tmpsol = `concSequent(conjd(goal,elem,elemn),tmpsol*);
								}
							}
						}
					}
					}
				}
				
				impg(s,p,pp) -> {
					if (s == goal) {
					ListSequent proof_p = buildProofTerm(p,trace);
					ListSequent proof_pp = buildProofTerm(pp,trace);
					%match(ListSequent proof_p) {
						concSequent(_*,elem,_*) -> {
							%match(ListSequent proof_pp) {
								concSequent(_*,elemn,_*) -> {
									tmpsol = `concSequent(impg(goal,elem,elemn),tmpsol*);
								}
							}
						}
					}
					}
				}
				
				p@axiom(s) -> {
					if (s == goal) {
					tmpsol = `concSequent(p,tmpsol*);
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
		String latex= "";
		%match(ListPred l) {
			concPred(_*,p,_*) -> {
				latex += " , " + predToTex(p);
			}
		}
		return latex.substring(3);
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

	//{{{ public String proofToTex(Sequent proof)
	public String proofToTex(Sequent proof) {
		String latex = "";

			//{{{ %match(Sequent item)
			%match(Sequent proof) {
				negd(s,p) -> {
					latex = "\\infer[negd]{" + seqToTex(s) + "}{" + proofToTex(p) + "}";
				}

				disjd(s,p) -> {
					latex = "\\infer[disjd]{" + seqToTex(s) + "}{" + proofToTex(p) + "}";
				}

				impd(s,p) -> {
					latex = "\\infer[impd]{" + seqToTex(s) + "}{" + proofToTex(p) + "}";
				}

				negg(s,p) -> {
					latex = "\\infer[negg]{" + seqToTex(s) + "}{" + proofToTex(p) + "}";
				}

				conjg(s,p) -> {
					latex = "\\infer[conjg]{" + seqToTex(s) + "}{" + proofToTex(p) + "}";
				}

				disjg(s,p,pp) -> {
					latex = "\\infer[disjg]{" + seqToTex(s) + "}{" + proofToTex(p) + " & " + proofToTex(pp) + "}";
				}
				
				conjd(s,p,pp) -> {
					latex = "\\infer[conjd]{" + seqToTex(s) + "}{" + proofToTex(p) + " & " + proofToTex(pp) + "}";
				}
				
				impg(s,p,pp) -> {
					latex = "\\infer[impg]{" + seqToTex(s) + "}{" + proofToTex(p) + " & " + proofToTex(pp) + "}";
				}
				
				p@axiom(s) -> {
					latex = "\\infer[axiom]{" + seqToTex(s) + "}{\\mbox{}}";
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
