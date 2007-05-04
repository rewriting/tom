/*
 * Copyright (c) 2004-2007, INRIA
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

import antlr.CommonAST;
import tom.library.sl.Strategy;

class SPropp {

	// ------------------------------------------------------------  
	%include { seq/Seq.tom }
  %include{ sl.tom }
	// ------------------------------------------------------------  

	//{{{ public void run(String query)
	public void run(Sequent query) {
		Proof initP = `hyp(query);

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

	public Proof solve(Proof init) {

		Strategy myrule = `CalculusRules();
		Strategy bottomUp = `BottomUp(myrule);

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

  Collection rules_appl = new HashSet();

	%strategy CalculusRules() extends `Identity() { 
    
	visit Proof {
				hyp(arg) -> {
					%match(Sequent arg) {

						// {{{	negd
						seq(concPred(X*),concPred(Y*,neg(Z),R*)) -> {
							Proof prod = `hyp(seq(concPred(X*,Z),concPred(Y*,R*)));
							return `rule(
								negd(),
								seq(concPred(X*),concPred(Y*,mark(neg(Z)),R*)),
								concProof(prod));
						}
						// }}}

						//{{{ disjd
						seq(concPred(X*),concPred(Y*,vee(Z,R),S*)) -> {
							Proof prod = `hyp(seq(concPred(X*),concPred(Y*,Z,R,S*)));
							return `rule(
								disjd(),
								seq(concPred(X*),concPred(Y*,mark(vee(Z,R)),S*)),
								concProof(prod));
						}
						//}}}			

						//{{{ impd
						seq(concPred(X*),concPred(S*,impl(Y,Z),R*)) -> {
							Proof prod = `hyp(seq(concPred(X*,Y),concPred(S*,Z,R*)));
							return `rule(
								impd(),
								seq(concPred(X*),concPred(S*,mark(impl(Y,Z)),R*)),
								concProof(prod));
						}
						//}}}

						//{{{ negg
						seq(concPred(X*,neg(Y),S*),concPred(Z*)) -> {
							Proof prod = `hyp(seq(concPred(X*,S*),concPred(Y,Z*)));
							return `rule(
								negg(),
								seq(concPred(X*,mark(neg(Y)),S*),concPred(Z*)),
								concProof(prod));
						}
						//}}}

						//{{{ conjg
						seq(concPred(X*,wedge(Y,Z),S*),concPred(R*)) -> {
							Proof prod = `hyp(seq(concPred(X*,Y,Z,S*),concPred(R*)));
							return `rule(
								conjg(),
								seq(concPred(X*,mark(wedge(Y,Z)),S*),concPred(R*)),
								concProof(prod));
						}
						//}}}

						//{{{ disjg
						seq(concPred(X*,vee(Y,Z),S*),concPred(R*)) -> {
							Proof s1 = `hyp(seq(concPred(X*,Y,S*),concPred(R*)));
							Proof s2 = `hyp(seq(concPred(X*,Z,S*),concPred(R*)));
							return `rule(
								disjg(),
								seq(concPred(X*,mark(vee(Y,Z)),S*),concPred(R*)),
								concProof(s1,s2));
						}
						//}}}

						//{{{ conjd
						seq(concPred(R*),concPred(X*,wedge(Y,Z),S*)) -> {
							Proof s1 = `hyp(seq(concPred(R*),concPred(X*,Y,S*)));
							Proof s2 = `hyp(seq(concPred(R*),concPred(X*,Z,S*)));
							return `rule(
								conjd(),
								seq(concPred(R*),concPred(X*,mark(wedge(Y,Z)),S*)),
								concProof(s1,s2));
						}
						//}}}

						//{{{ impg
						seq(concPred(X*,impl(Y,Z),S*),concPred(R*)) -> {
							Proof s1 = `hyp(seq(concPred(X*,S*),concPred(R*,Y)));
							Proof s2 = `hyp(seq(concPred(X*,Z,S*),concPred(R*)));
							return `rule(
								impg(),
								seq(concPred(X*,mark(impl(Y,Z)),S*),concPred(R*)),
								concProof(s1,s2));
						}
						//}}}

						//{{{ axio
						seq(concPred(L1*,X,L2*),concPred(L3*,X,L4*)) -> {
							if (`X != `EmptyP()) {
								return `rule(
									axiom(),
									seq(concPred(L1*,mark(X),L2*),concPred(L3*,mark(X),L4*)),
									concProof());
							}
						}
						//}}}

					}// end %match
				}
			}
	}

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
						while(!proof_p.isEmptyconcProof()) {
							tmpsol = `concProof(rule(r,goal,concProof(proof_p.getHeadconcProof())),tmpsol*);
							proof_p = proof_p.getTailconcProof();
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
            
						while(!proof_p.isEmptyconcProof()) {
							while(!proof_pp.isEmptyconcProof()) {
								tmpsol = `concProof(rule(r,goal,concProof(proof_p.getHeadconcProof(),proof_pp.getHeadconcProof())),tmpsol*);
								proof_pp = proof_pp.getTailconcProof();
							}
							proof_p = proof_p.getTailconcProof();
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
      hyp(_) -> { val.setValue(false) ; }
    }
  }
	//}}}
	
	//{{{ public boolean rule_nbr(Proof p)
	public int rule_nbr(Proof p) {
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
      rule(_,_,_) -> { val.setValue(val.getValue() + 1) ; }
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
      pair(i,_),pair(j,_) -> {
        if `(i < j) { return -1;}
        else {return 1;}
      }
    }
    return 0;
  }

  public ListPair insertPair(Pair p,ListPair l) {
    ListPair res = null;
    if(l.isEmptyconcPair()) {
      res = `concPair(p,l*);
    } else if(comparePair(p, l.getHeadconcPair()) < 0) {
      res = `concPair(p,l*);
    } else {
      ListPair newTail = l.getTailconcPair();
      res = insertPair(p,`concPair(l.getHeadconcPair(),newTail*));
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
			while(!tmptex.isEmptyconcPair()) {
				Pair p = (Pair)tmptex.getHeadconcPair();
				osw.write((String)p.getright());
				osw.write("\n\\vspace{3cm}\n\n");
				tmptex = tmptex.getTailconcPair();
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
			SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
			SeqParser parser = new SeqParser(lexer);
			// Parse the input expression
			query = parser.seq();
			System.out.println("Query : "+query);
		} catch (Exception e) {
			System.err.println("exception: "+e);
			return;
		}
		test.run(query);
	}
	//}}}

}
