/*
 * Copyright (c) 2004-2006, INRIA
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
import java.util.*;
import tom.library.traversal.*;
import propp.seq.*;
import propp.seq.types.*;
import java.io.*;
import antlr.CommonAST;

public class Propp1 {

	private GenericTraversal traversal;

	// ------------------------------------------------------------
	%include { seq/seq.tom }
	// ------------------------------------------------------------

	public Propp1() {
		this.traversal = new GenericTraversal();
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

		System.out.println("Build Proof Term");
		ListProof proofTerm = buildProofTerm(initSeq);
		ListProof pt = proofTerm;
		//System.out.println("Proof term = " + proofTerm);
		System.out.println("Build LaTeX, check validity");
		ListPair tex_proofs = `concPair();
		//we may want to use SortedSet instead
		boolean validity = true;
		int proof_number = 0;
		while(!pt.isEmpty()) {
			Proof p = pt.getHead();
			int nbr = rule_nbr(p);
			String tex = proofToTex(p)+"\n\n\nRules used for proof : "+nbr+"\n\n\n";
			proof_number += 1;
			tex_proofs = insertPair(`pair(nbr,tex),tex_proofs);
			if (!isValid(p)) { validity = false; }
			pt = pt.getTail();
		}
		System.out.println("Number of proofs : " + proof_number);
		System.out.println("Is input proved ? " + validity);

		write_proof_latex(tex_proofs,"proof.tex");
		//System.out.println("Latex : " + tex_proofs);
		System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");

	}
	//}}}

	//{{{ public Sequent makeQuery(String input)
	public Sequent makeQuery(String input) {
		Sequent query = Factory.getInstance(SingletonFactory.getInstance()).SequentFromString(input);
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
			//System.out.println(c1);
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
          // X |- Y, Z, R, S
          // -----------------
          // X |- Y, Z \/ R, S
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
					seq(concPred(R*),concPred(X*,wedge(Y,Z),S*)) -> {
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
						if (`X != `EmptyP()) {
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

	Map proofterm_pool = new HashMap();
	//{{{ public ListProof buildProofTerm(Sequent goal) {
	public ListProof buildProofTerm(Sequent goal) {
		ListProof tmpsol = `concProof();
		if (proofterm_pool.containsKey(goal)) {
			return (ListProof)proofterm_pool.get(goal);
		}
		Iterator iter = rules_appl.iterator();
		while (iter.hasNext()) {
			Trace item = (Trace)iter.next();
			//{{{ %match(Trace item)
			%match(Trace item) {

				rappl(r,s,concSequent()) -> {
					if (`s == goal) {
						tmpsol = `concProof(rule(r,s,concProof()),tmpsol*);
					}
				}

				rappl(r,s,concSequent(p)) -> {
					if (`s == goal) {
						ListProof proof_p = buildProofTerm(`p);
						while(!proof_p.isEmpty()) {
							tmpsol = `concProof(rule(r,goal,concProof(proof_p.getHead())),tmpsol*);
							proof_p = proof_p.getTail();
						}
						/*// the matching version uses too much memory !!!
						%match(ListProof proof_p) {
							concProof(_*,elem,_*) -> {
								tmpsol = `concProof(rule(r,goal,concProof(elem)),tmpsol*);
							}
						}
						*/
					}
				}

				rappl(r,s,concSequent(p,pp)) -> {
					if (`s == goal) {
						ListProof proof_p = buildProofTerm(`p);
						ListProof proof_pp = buildProofTerm(`pp);
            
						while(!proof_p.isEmpty()) {
							while(!proof_pp.isEmpty()) {
								tmpsol = `concProof(rule(r,goal,concProof(proof_p.getHead(),proof_pp.getHead())),tmpsol*);
								proof_pp = proof_pp.getTail();
							}
							proof_p = proof_p.getTail();
						}
						/*  // the matching version uses too much memory !!!
								%match(ListProof proof_p) {
								concProof(_*,elem,_*) -> {
								%match(ListProof proof_pp) {
								concProof(_*,elemn,_*) -> {
								tmpsol = `concProof(rule(r,goal,concProof(elem,elemn)),tmpsol*);
								}
								}
								}
								}
						*/
					}
				}
				
			}
			//}}}
		}

		proofterm_pool.put(goal,tmpsol);
		return tmpsol;
	}
	//}}}

	//{{{ public String seqToTex(Sequent s)
	public String seqToTex(Sequent s) {
		String latex= "";
		%match(Sequent s) {
			seq(l1,l2) -> {
				latex = listPredToTex(`l1) + "\\vdash " + listPredToTex(`l2);
			}
		}
		return latex;
	}
	//}}}

	//{{{ public String listPredToTex(ListPred l)
	public String listPredToTex(ListPred l) {
		%match(ListPred l) {
			concPred()     -> {return "";}
			concPred(a)    -> {return predToTex(`a);}
			concPred(h,t*) -> {return predToTex(`h) + " , " + listPredToTex(`t);}
		}
		return "";
	}
	//}}}

	//{{{ public String predToTex(Pred p)
	public String predToTex(Pred pred) {
		%match(Pred pred) {
			neg(p) -> {
				return "\\neg " + predToTex(`p);
			}
			equiv(p1,p2) -> {
				return predToTex(`p1) + "\\lra " + predToTex(`p2);
			}
			impl(p1,p2) -> {
				return predToTex(`p1) + "\\to " + predToTex(`p2);
			}
			vee(p1,p2) -> {
				return predToTex(`p1) + "\\vee " + predToTex(`p2);
			}
			wedge(p1,p2) -> {
				return predToTex(`p1) + "\\wedge " + predToTex(`p2);
			}
			mark(p) -> {
				return "\\textcolor{red}{" + predToTex(`p) + "}";
			}
		}
		return pred.toString();
	}
	//}}}

	//{{{ public String proofToTex(Proof proof)
	public String proofToTex(Proof proof) {
		String latex = "";

		//{{{ %match(Proof item)
		%match(Proof proof) {

			rule(r,g,concProof()) -> {
				latex = "\\infer[\\"+`r.toString()+"]{" + seqToTex(`g) + "}{\\mbox{}}";
			}

			rule(r,g,concProof(p)) -> {
				latex = "\\infer[\\"+`r.toString()+"]{" + seqToTex(`g) + "}{" + proofToTex(`p) + "}";
			}

			rule(r,g,concProof(p,pp)) -> {
				latex = "\\infer[\\"+`r.toString()+"]{" + seqToTex(`g) + "}{" + proofToTex(`p) + " & " + proofToTex(`pp) + "}";
			}

			// this is not a valid proof
			hyp(s) -> {
				latex = "{\\bf " + seqToTex(`s) + "}";
			}

		}
		//}}}

		return latex;
	}
	//}}}

	//{{{ public boolean isValid(Proof p)
	public boolean isValid(Proof p) {
		Collect2 collect = new Collect2() {
			public boolean apply(ATerm subjectAT, Object arg1) {
				if (subjectAT instanceof Proof) {
					Proof pt = (Proof)subjectAT;
					MyBoolean val = (MyBoolean) arg1;
					%match(Proof pt) {
						hyp(_) -> { val.setValue(false) ; }
					}
					return true;
				}
				return true;
			}//end apply
		};//end new
		
		MyBoolean result = new MyBoolean(true);
		traversal.genericCollect(p,collect,result);
		return result.getValue();
	}
	//}}}
	
	//{{{ public boolean rule_nbr(Proof p)
	public int rule_nbr(Proof p) {
		Collect2 collect = new Collect2() {
			public boolean apply(ATerm subjectAT, Object arg1) {
				if (subjectAT instanceof Proof) {
					Proof pt = (Proof)subjectAT;
					MyInt val = (MyInt) arg1;
					%match(Proof pt) {
						rule(_,_,_) -> { val.setValue(val.getValue() + 1) ; }
					}
					return true;
				}
				return true;
			}//end apply
		};//end new
		
		MyInt result = new MyInt(0);
		traversal.genericCollect(p,collect,result);
		return result.getValue();
	}
	//}}}

	//{{{ public class MyBoolean
	public class MyBoolean {
		private boolean value;
		public MyBoolean(boolean initValue) {
			value = initValue;
		}
		public void setValue(boolean val) {
			value = val;
		}
		public boolean getValue() {
			return value;
		}
	}
	//}}}
	
	//{{{ public class MyInt
	public class MyInt {
		private int value;
		public MyInt(int initValue) {
			value = initValue;
		}
		public void setValue(int val) {
			value = val;
		}
		public int getValue() {
			return value;
		}
	}
	//}}}
	
	//{{{ insertPair
	public int comparePair(Pair p1,Pair p2) {
		%match(Pair p1,Pair p2) {
			pair(i,_),pair(j,_) -> {
				if `(i < j) { return -1;}
				else {return 1;}
			}
		}
		return 0;
	}

	public ListPair insertPair(Pair p,ListPair l) {
    ListPair res = null;
    if(l.isEmpty()) {
      res = l.insert(p);
    } else if(comparePair(p, l.getHead()) < 0) {
      res = l.insert(p);
    } else {
      res = insertPair(p,l.getTail()).insert(l.getHead());
    }
    return res;
	}
	//}}}

	//{{{ public void write_proof_latex(Map tex_p,String file)
	public void write_proof_latex(ListPair tex_p,String file) {
		ListPair tmptex = tex_p;
		try {
			File target = new File(file);
			FileOutputStream out = new FileOutputStream(target);

			OutputStreamWriter osw = new OutputStreamWriter(out,"ISO-8859-1");

			osw.write("\\documentclass{article}\n\\usepackage{proof}\n\\usepackage{color}\n\\def\\negd{\\neg_D}\n\\def\\disjd{\\vee_D}\n\\def\\impd{\\to_D}\n\\def\\negg{\\neg_G}\n\\def\\conjg{\\wedge_G}\n\\def\\disjg{\\vee_G}\n\\def\\conjd{\\wedge_D}\n\\def\\impg{\\to_G}\n\\def\\axiom{Axiom}\n\n\\begin{document}");
			osw.write("\n\n\n");
			while(!tmptex.isEmpty()) {
				Pair p = (Pair)tmptex.getHead();
				osw.write((String)p.getRight());
				osw.write("\n\\vspace{3cm}\n\n");
				tmptex = tmptex.getTail();
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
		Propp1 test = new Propp1();

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



