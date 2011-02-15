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

import aterm.*;
import aterm.pure.*;

import java.io.*;
import java.util.*;

import propp.seq.*;
import propp.seq.types.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import tom.library.sl.*;

class SPropp {

	// ------------------------------------------------------------  
	%include { seq/Seq.tom }
  %include{ sl.tom }
	// ------------------------------------------------------------  

	//{{{ public void run(String query)
	public void run(Sequent query) {
		Proof initP = `Hyp(query);

		long startChrono = System.currentTimeMillis();
		Proof proofTerm = solve(initP);
		long stopChrono = System.currentTimeMillis();

		System.out.println("Proof term = " + proofTerm);
		ListPair tex_proofs = `ConcPair();
		/*
		%match(ListProof proofTerm) {
			ConcProof(_*,p,_*) -> {
				tex_proofs.add(proofToTex(p));
			}
		}
		*/
		System.out.println("Is input proved ? " + isValid(proofTerm));

		tex_proofs = `ConcPair(Pair(1,proofToTex(proofTerm)));

		System.out.println("Build LaTeX");
		write_proof_latex(tex_proofs,"proof.tex");

		System.out.println("Latex : " + tex_proofs);
		System.out.println("res found in " + (stopChrono-startChrono) + " ms");

	}
	//}}}

	public Proof solve(Proof init) {

		Strategy myRule = `CalculusRules();
		Strategy bottomUp = `BottomUp(myRule);

		Proof res = null;
		try {
			res = (Proof)bottomUp.visitLight(init);
		} catch (VisitFailure e) {
			return init;
		}
		if (res != init) {
			res = solve(res);
		}
		return res;
	}

  Collection<Trace> Rules_appl = new HashSet<Trace>();

	%strategy CalculusRules() extends `Identity() { 
    
	visit Proof {
				Hyp(arg) -> {
					%match(Sequent arg) {

						// {{{	Negd
						Seq(ConcPred(X*),ConcPred(Y*,Neg(Z),R*)) -> {
							Proof prod = `Hyp(Seq(ConcPred(X*,Z),ConcPred(Y*,R*)));
							return `Rule(
								Negd(),
								Seq(ConcPred(X*),ConcPred(Y*,Mark(Neg(Z)),R*)),
								ConcProof(prod));
						}
						// }}}

						//{{{ Disjd
						Seq(ConcPred(X*),ConcPred(Y*,Vee(Z,R),S*)) -> {
							Proof prod = `Hyp(Seq(ConcPred(X*),ConcPred(Y*,Z,R,S*)));
							return `Rule(
								Disjd(),
								Seq(ConcPred(X*),ConcPred(Y*,Mark(Vee(Z,R)),S*)),
								ConcProof(prod));
						}
						//}}}			

						//{{{ Impd
						Seq(ConcPred(X*),ConcPred(S*,Impl(Y,Z),R*)) -> {
							Proof prod = `Hyp(Seq(ConcPred(X*,Y),ConcPred(S*,Z,R*)));
							return `Rule(
								Impd(),
								Seq(ConcPred(X*),ConcPred(S*,Mark(Impl(Y,Z)),R*)),
								ConcProof(prod));
						}
						//}}}

						//{{{ Negg
						Seq(ConcPred(X*,Neg(Y),S*),ConcPred(Z*)) -> {
							Proof prod = `Hyp(Seq(ConcPred(X*,S*),ConcPred(Y,Z*)));
							return `Rule(
								Negg(),
								Seq(ConcPred(X*,Mark(Neg(Y)),S*),ConcPred(Z*)),
								ConcProof(prod));
						}
						//}}}

						//{{{ Conjg
						Seq(ConcPred(X*,Wedge(Y,Z),S*),ConcPred(R*)) -> {
							Proof prod = `Hyp(Seq(ConcPred(X*,Y,Z,S*),ConcPred(R*)));
							return `Rule(
								Conjg(),
								Seq(ConcPred(X*,Mark(Wedge(Y,Z)),S*),ConcPred(R*)),
								ConcProof(prod));
						}
						//}}}

						//{{{ Disjg
						Seq(ConcPred(X*,Vee(Y,Z),S*),ConcPred(R*)) -> {
							Proof s1 = `Hyp(Seq(ConcPred(X*,Y,S*),ConcPred(R*)));
							Proof s2 = `Hyp(Seq(ConcPred(X*,Z,S*),ConcPred(R*)));
							return `Rule(
								Disjg(),
								Seq(ConcPred(X*,Mark(Vee(Y,Z)),S*),ConcPred(R*)),
								ConcProof(s1,s2));
						}
						//}}}

						//{{{ Conjd
						Seq(ConcPred(R*),ConcPred(X*,Wedge(Y,Z),S*)) -> {
							Proof s1 = `Hyp(Seq(ConcPred(R*),ConcPred(X*,Y,S*)));
							Proof s2 = `Hyp(Seq(ConcPred(R*),ConcPred(X*,Z,S*)));
							return `Rule(
								Conjd(),
								Seq(ConcPred(R*),ConcPred(X*,Mark(Wedge(Y,Z)),S*)),
								ConcProof(s1,s2));
						}
						//}}}

						//{{{ Impg
						Seq(ConcPred(X*,Impl(Y,Z),S*),ConcPred(R*)) -> {
							Proof s1 = `Hyp(Seq(ConcPred(X*,S*),ConcPred(R*,Y)));
							Proof s2 = `Hyp(Seq(ConcPred(X*,Z,S*),ConcPred(R*)));
							return `Rule(
								Impg(),
								Seq(ConcPred(X*,Mark(Impl(Y,Z)),S*),ConcPred(R*)),
								ConcProof(s1,s2));
						}
						//}}}

						//{{{ axio
						Seq(ConcPred(L1*,X,L2*),ConcPred(L3*,X,L4*)) -> {
							if (`X != `EmptyP()) {
								return `Rule(
									Axiom(),
									Seq(ConcPred(L1*,Mark(X),L2*),ConcPred(L3*,Mark(X),L4*)),
									ConcProof());
							}
						}
						//}}}

					}// end %match
				}
			}
	}

	Map<Sequent,ListProof> proofterm_pool = new HashMap<Sequent,ListProof>();
	//{{{ public ListProof buildProofTerm(Sequent goal) {
	public ListProof buildProofTerm(Sequent goal) {
		ListProof tmpsol = `ConcProof();
		if (proofterm_pool.containsKey(goal)) {
			return proofterm_pool.get(goal);
		}
    for (Trace item : Rules_appl) {
			//{{{ %match(Trace item)
			%match(Trace item) {

				Rappl(r,s,ConcSequent()) -> {
					if (`s == goal) {
						tmpsol = `ConcProof(Rule(r,s,ConcProof()),tmpsol*);
					}
				}

				Rappl(r,s,ConcSequent(p)) -> {
					if (`s == goal) {
						ListProof proof_p = buildProofTerm(`p);
						while(!proof_p.isEmptyConcProof()) {
							tmpsol = `ConcProof(Rule(r,goal,ConcProof(proof_p.getHeadConcProof())),tmpsol*);
							proof_p = proof_p.getTailConcProof();
						}
						/*// the matching version uses too much memory !!!
						%match(ListProof proof_p) {
							ConcProof(_*,elem,_*) -> {
								tmpsol = `ConcProof(Rule(r,goal,ConcProof(elem)),tmpsol*);
							}
						}
						*/
					}
				}

				Rappl(r,s,ConcSequent(p,pp)) -> {
					if (`s == goal) {
						ListProof proof_p = buildProofTerm(`p);
						ListProof proof_pp = buildProofTerm(`pp);
            
						while(!proof_p.isEmptyConcProof()) {
							while(!proof_pp.isEmptyConcProof()) {
								tmpsol = `ConcProof(Rule(r,goal,ConcProof(proof_p.getHeadConcProof(),proof_pp.getHeadConcProof())),tmpsol*);
								proof_pp = proof_pp.getTailConcProof();
							}
							proof_p = proof_p.getTailConcProof();
						}
						/*  // the matching version uses too much memory !!!
								%match(ListProof proof_p) {
								ConcProof(_*,elem,_*) -> {
								%match(ListProof proof_pp) {
								ConcProof(_*,elemn,_*) -> {
								tmpsol = `ConcProof(Rule(r,goal,ConcProof(elem,elemn)),tmpsol*);
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

	//{{{ public String SeqToTex(Sequent s)
	public String SeqToTex(Sequent s) {
		String latex= "";
		%match(Sequent s) {
			Seq(l1,l2) -> {
				latex = listPredToTex(`l1) + "\\vdash " + listPredToTex(`l2);
			}
		}
		return latex;
	}
	//}}}

	//{{{ public String listPredToTex(ListPred l)
	public String listPredToTex(ListPred l) {
		%match(ListPred l) {
			ConcPred()     -> {return "";}
			ConcPred(a)    -> {return PredToTex(`a);}
			ConcPred(h,t*) -> {return PredToTex(`h) + " , " + listPredToTex(`t);}
		}
		return "";
	}
	//}}}

	//{{{ public String PredToTex(Pred p)
	public String PredToTex(Pred Pred) {
		%match(Pred Pred) {
			Neg(p) -> {
				return "\\Neg " + PredToTex(`p);
			}
			Equiv(p1,p2) -> {
				return PredToTex(`p1) + "\\lra " + PredToTex(`p2);
			}
			Impl(p1,p2) -> {
				return PredToTex(`p1) + "\\to " + PredToTex(`p2);
			}
			Vee(p1,p2) -> {
				return PredToTex(`p1) + "\\Vee " + PredToTex(`p2);
			}
			Wedge(p1,p2) -> {
				return PredToTex(`p1) + "\\Wedge " + PredToTex(`p2);
			}
			Mark(p) -> {
				return "\\textcolor{red}{" + PredToTex(`p) + "}";
			}
		}
		return Pred.toString();
	}
	//}}}

	//{{{ public String proofToTex(Proof proof)
	public String proofToTex(Proof proof) {
		String latex = "";

		//{{{ %match(Proof item)
		%match(Proof proof) {

			Rule(r,g,ConcProof()) -> {
				latex = "\\infer[\\"+`r.toString()+"]{" + SeqToTex(`g) + "}{\\mbox{}}";
			}

			Rule(r,g,ConcProof(p)) -> {
				latex = "\\infer[\\"+`r.toString()+"]{" + SeqToTex(`g) + "}{" + proofToTex(`p) + "}";
			}

			Rule(r,g,ConcProof(p,pp)) -> {
				latex = "\\infer[\\"+`r.toString()+"]{" + SeqToTex(`g) + "}{" + proofToTex(`p) + " & " + proofToTex(`pp) + "}";
			}

			// this is not a valid proof
			Hyp(s) -> {
				latex = "{\\bf " + SeqToTex(`s) + "}";
			}

		}
		//}}}

		return latex;
	}
	//}}}

	//{{{ public boolean isValid(Proof p)
	public boolean isValid(Proof p) {
		MyBoolean result = new MyBoolean(true);
    try {
      `BottomUp(IsValid(result)).visitLight(p);
    } catch (VisitFailure f) {
      throw new RuntimeException("Failed to check validity");
    }
		return result.getValue();
	}
  %typeterm MyBoolean { 
    implement { MyBoolean } 
    is_sort(t)     { t instanceof MyBoolean }
  }
  %strategy IsValid(val:MyBoolean) extends `Identity() {
    visit Proof {
      Hyp(_) -> { val.setValue(false) ; }
    }
  }
	//}}}
	
	//{{{ public boolean Rule_nbr(Proof p)
	public int Rule_nbr(Proof p) {
		MyInt result = new MyInt(0);
    try {
      `BottomUp(CountRules(result)).visitLight(p);
    } catch (VisitFailure f) {
      throw new RuntimeException("Failed to check validity");
    }
		return result.getValue();
	}
  %strategy CountRules(val:MyInt) extends `Identity() {
    visit Proof {
      Rule(_,_,_) -> { val.setValue(val.getValue() + 1) ; }
    }
  }
	//}}}

	//{{{ public class MyBoolean
	public static class MyBoolean {
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
  %typeterm MyInt { 
    implement { MyInt } 
    is_sort(t)     { t instanceof MyInt }
  }
  public static class MyInt {
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
      Pair(i,_),Pair(j,_) -> {
        if `(i < j) { return -1;}
        else {return 1;}
      }
    }
    return 0;
  }

  public ListPair insertPair(Pair p,ListPair l) {
    ListPair res = null;
    if(l.isEmptyConcPair()) {
      res = `ConcPair(p,l*);
    } else if(comparePair(p, l.getHeadConcPair()) < 0) {
      res = `ConcPair(p,l*);
    } else {
      ListPair newTail = l.getTailConcPair();
      res = insertPair(p,`ConcPair(l.getHeadConcPair(),newTail*));
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

			osw.write("\\documentclass{article}\n\\usepackage{proof}\n\\usepackage{color}\n\\def\\Negd{\\Neg_D}\n\\def\\Disjd{\\Vee_D}\n\\def\\Impd{\\to_D}\n\\def\\Negg{\\Neg_G}\n\\def\\Conjg{\\Wedge_G}\n\\def\\Disjg{\\Vee_G}\n\\def\\Conjd{\\Wedge_D}\n\\def\\Impg{\\to_G}\n\\def\\Axiom{Axiom}\n\n\\begin{document}");
			osw.write("\n\n\n");
			while(!tmptex.isEmptyConcPair()) {
				Pair p = (Pair)tmptex.getHeadConcPair();
				osw.write((String)p.getright());
				osw.write("\n\\vspace{3cm}\n\n");
				tmptex = tmptex.getTailConcPair();
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
		SPropp test = new SPropp();

		Sequent query = null;
		try {
			//query = args[0];
			SeqLexer lexer = new SeqLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
			SeqParser parser = new SeqParser(tokens);
			// Parse the input expression
      Tree t = null;
      do {
        t = (Tree) parser.seq().getTree();
        query = (Sequent) SeqAdaptor.getTerm(t);
        System.out.println("Query : "+query);
        test.run(query);
      } while (null != t);
		} catch (Exception e) {
			System.err.println("exception: "+e);
			return;
		}
	}
	//}}}

}
