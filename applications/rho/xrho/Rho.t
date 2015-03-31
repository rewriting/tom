  /*  * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
  * 	- Neither the name of the Inria nor the names of its
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
	*
  * by Germain Faure
  */


package xrho;

import aterm.*;
import aterm.pure.*;
import xrho.rhoterm.*;
import xrho.rhoterm.types.*;

import tom.library.sl.*;

import java.io.*;

public class Rho {
	
	%include { sl.tom }
	%include { rhoterm/Rhoterm.tom }
	%include { boolean.tom}
	
	//%op Strategy Not_abs() {
//		make() {new Not_abs() }
//	}

	%op Strategy One_abs(strat:Strategy) {
		make(v) {`Sequence(Not_abs(),One(v)) }//new One_abs((Strategy)v)
	}
	%op Strategy All_abs(strat:Strategy) {
		make(v) {`Sequence(Not_abs(),All(v)) }//new One_abs((Strategy)v)
	}
	%op Strategy OnceTopDownWeak(strat:Strategy) {
		make(v) {`mu(MuVar("x"),Choice(v,One_abs(MuVar("x"))))}
	}

	Strategy rules=`Choice(RhoDelta(),Explicit());
	Strategy oneStepWeakNormalisation = `OnceTopDownWeak(rules);
	Strategy strategyStepsStrongPlain =`Repeat(Sequence(OnceBottomUp(Sequence(RhoDelta(),Innermost(Explicit()))),Try(Print())));
	Strategy strategyStepsStrongExplicit =`Repeat(Sequence(OnceBottomUp(rules),Try(Print())));
	Strategy strategyStepsWeakPlain = `Repeat(Sequence(OnceTopDownWeak(Sequence(RhoDelta(),Innermost(Explicit()))),Try(Print())));
	Strategy strategyResultStrongPlain = `Innermost(rules);
	Strategy strategyStepsWeakExplicit = `Repeat(Sequence(oneStepWeakNormalisation,Try(Print())));
	Strategy strategyResultStrongExplicit  = `Innermost(rules);
	Strategy strategyResultWeakPlain = `Repeat(oneStepWeakNormalisation);
	Strategy strategyResultWeakExplicit = `Repeat(oneStepWeakNormalisation);


	public final static void main(String[] args) {
		Rho rhoEngine = new Rho();
		rhoEngine.run();
	}
	

	public void run(){
		RTerm subject = `Const("undefined");
		Strategy currentStrategy = strategyResultWeakPlain;
		String s;
		System.out.println(" ******************************************************************\n xRho: an experimental implementation  in Tom of the explicit rho-calculus \n\n\n For weak normalisation type WEAK; for strong normalisation type STRONG; \n Strong normalisation is valid only for linear (variables at most once) terms respecting Barendregt's convention\n\n For getting the reductions with all explicit steps type \'EXPLICIT\' otherwise type \'PLAIN\'\n\n For getting only the normal form \'NF\' and for getting all the evaluation steps type \'DETAILS\'\n \n By default the strategy is \'NF;PLAIN;WEAK\'\n\n By Germain Faure\n\n It is under development and is definitevely not stable nor deliverable. \n ******************************************************************");
    RhoLexer lexer = new RhoLexer(System.in); // Create parser attached to lexer
    RhoParser parser = new RhoParser(lexer);
	boolean resultSteps=`false();
	boolean weakStrong=`false();
	boolean explicitPlain=`true();
		while(true){
			System.out.print("xr>");
      try {
				subject = parser.program();
			} catch (Exception e) {
				System.out.println(e);
				
			}
			try{
				%match(RTerm subject){
					result() -> {resultSteps=`false();}
					steps() -> {resultSteps=`true();}
					weak() -> {weakStrong=`false();}
					strong() -> {weakStrong=`true();}
					explicit() ->{explicitPlain=`false();}
					plain() ->{explicitPlain=`true();}
					_ -> 	 {
					%match(resultSteps,  weakStrong,  explicitPlain){
						true(), true(), true() -> {currentStrategy=strategyStepsStrongPlain;}
						true(), true(), false() -> {currentStrategy=strategyStepsStrongExplicit;}
						true(), false(), true() -> {currentStrategy=strategyStepsWeakPlain;}
						false(), true(), true() -> {currentStrategy=strategyResultStrongPlain;}
						true(), false(), false() -> {currentStrategy=strategyStepsWeakExplicit;}
						false(), true(), false() -> {currentStrategy=strategyResultStrongExplicit;}
						false(), false(), true() -> {currentStrategy=strategyResultWeakPlain;}
						false(), false(), false() -> {currentStrategy=strategyResultWeakExplicit;}
}
System.out.println(stringInfix((RTerm)currentStrategy.visit(subject)));}
				}

			} catch(VisitFailure e) {
				System.out.println("reduction failed on: " + subject);
			}
		}
	}


	public String test(String s,RhoParser parser){
//		System.out.println("s="+s);
		RTerm subject = `Const("undefined");

		try {
			subject = parser.program();
		} catch (Exception e) {
			return e.toString();
		}
		try{
			subject = (RTerm)strategyResultWeakPlain.visit(subject);
			return stringInfix(subject);
		} catch(VisitFailure e) {
			return "reduction failed on: " + subject ;
		}
	}

	public String test(String s){
		RTerm subject = `Const("undefined");
		StringReader sr = new StringReader(s);
    RhoLexer lexer = new RhoLexer(sr); // Create parser attached to lexer
    RhoParser parser = new RhoParser(lexer);
		try {
			subject = parser.program();
		} catch (Exception e) {
			return e.toString();
		}
		try{
			subject = (RTerm)strategyResultWeakPlain.visit(subject);
			return stringInfix(subject);
		} catch(VisitFailure e) {
			return "reduction failed on: " + subject ;
		}
	}

	%strategy Print() extends `Fail() {
		visit RTerm {
			x -> {System.out.println("=>"  + stringInfix(`x)); return `x;}
		}
	}
	
	%strategy RhoDelta() extends `Fail() {
		visit RTerm {
				/*Rho*/
				app(abs(P,M),N) -> {return `appC(andC(match(P,N)),M);}
				/*Delta*/
				app(struct(M1,M2),N) -> {return `struct(app(M1,N),app(M2,N));}
		}
	}
	%strategy Explicit() extends `Fail(){
		visit RTerm {
			/*Compose */
				appS(phi@andS(l*),appS(andS(L*),N)) -> {
					ListSubst result = `mapS(((ListSubst)(reverse(L))),phi,andS());
					return `appS(andS(l*,result*),N);}
				//ALPHA-CONV
				/* Garbage collector */
				appC(andC(_*,matchKO(),_*),_) -> {return `stk();}
				app(stk(),A) -> {return `stk();}
				struct(A,stk()) -> {return `A;}
				struct(stk(),A) -> {return `A;}
							/*ToSubst*/
				appC(andC(C*,match(X@var[],M),D*),N) -> {return `appC(andC(C*,D*),appS(andS(eq(X,M)),N));}
				//PATTERNS LINEAIRES
				/*Id*/ 
				appC(andC(),M) -> {return `M;}
				/*Replace*/
				appS(andS(_*,eq(var(X),M),_*),var(X)) -> {return `M;}
				/*Var*/
				appS(_,Y@var[]) -> {return `Y;}
				//cette regle est correcte sans condition de bord si
				//on sait que on essaye toujours d'appliquer la regle
				//Replace avant		
				/*Const*/
				appS(_,c@Const[]) -> {return `c;}
				/*Abs*/
				appS(phi,abs(P,M)) -> {return `abs(P,appS(phi,M));}
				//ALPHA-CONVERSION NECESSAIRE!
				/*App*/
				appS(phi,app(M,N)) -> {return `app(appS(phi,M),appS(phi,N));}
				/*Struct*/
				appS(phi,struct(M,N)) -> {return `struct(appS(phi,M),appS(phi,N));}
				/*Constraint*/
				appS(phi,appC(andC(L*),M)) -> {
					ListConstraint result = `mapC(((ListConstraint)(reverse(L))),phi,andC());
					return `appC(andC(result*),appS(phi,M));}
				/* La regle est correcte pour n is 0 */
				//ALPHA-CONV!!
				/* Patterns lineaires, pas besoin de la regle Idem */
		}
		visit ListConstraint {
	/*Decompose n = m = 0*/
				andC(X*,match(f@Const[],f),Y*) -> {return `andC(X*,Y*);}
				
				/*Decompose_ng n = m = 0*/
				//si j'arrive dans la regle suivant c'est que les Const sont diff
				andC(X*,match(Const[],Const[]),Y*) -> {return `andC(X*,matchKO(),Y*);}
				
				/*Decompose et Decompose_ng min(n,m) > 0 */
				l: andC(X*,m@match(app[],app[]),Y*) -> {
					ListConstraint head_is_constant = `headIsConstant(m);
					%match(ListConstraint head_is_constant) {
						andC(match[]) -> { break l; }
						andC(matchKO()) -> { return `andC(X*,matchKO(),Y*); }
					}
					ListConstraint result = `computeMatch(andC(m));
					return `andC(X*,result*,Y*);
				}
				l: andC(X*,m@match(app[],Const[]),Y*)  -> {
					ListConstraint head_is_constant = `headIsConstant(m);
					%match(ListConstraint head_is_constant) {
						andC(match[]) -> { break l; }
						andC(matchKO()) -> { return `andC(X*,matchKO(),Y*); }
					}
					ListConstraint result = `computeMatch(andC(m));
					return `andC(X*,result*,Y*);
				}
				l: andC(X*,m@match(Const[],app[]),Y*) -> {
					ListConstraint head_is_constant = `headIsConstant(m);
					%match(ListConstraint head_is_constant) {
						andC(match[]) -> { break l; }
						andC(matchKO()) -> { return `andC(X*,matchKO(),Y*); }
					}
					ListConstraint result = `computeMatch(andC(m));
					return `andC(X*,result*,Y*);
				}
				
				/*Decompose Struct */
				andC(X*,match(struct(M1,M2),struct(N1,N2)),Y*) ->{
					return `andC(X*,match(M1,N1),match(M2,N2),Y*);
				}
		}
	}	
	public  static String stringInfix(RTerm term){
		//suis les priorites donnes par RhoParser.g.t
		%match(RTerm term){
			appS(substs,term1) -> {return "["+ stringInfix(`substs)+"]" + "("+stringInfix(`term1)+")";}
			appC(constrainsts,term1) -> {return "["+ stringInfix(`constrainsts)+"]" + "("+stringInfix(`term1)+")";}
			app(term1,term2) -> {return "("+stringInfix(`term1)+"."+stringInfix(`term2)+")";}
			abs(term1,term2) -> {return "("+stringInfix(`term1)+"->"+stringInfix(`term2)+")";}
			struct(term1,term2) -> {return "("+stringInfix(`term1)+"|"+stringInfix(`term2)+")";}
			var(s) -> {return `s;}
			Const(s) -> {return `s;}
			stk() -> {return "stk";}
		}
     return "";
	}
	public static String stringInfix(ListSubst substs){
		%match(ListSubst substs){
			andS(eq(term1,term2),eq(term3,term4),Y*) -> {return stringInfix(`term1)+"="+stringInfix(`term2) +"^"+ stringInfix(`term3)+"="+stringInfix(`term4) +stringInfix(`Y);}
			andS(eq(term1,term2),X*) -> {return stringInfix(`term1)+"="+stringInfix(`term2);}
			_ -> {return "";}
		}
    return "";
	}

  %strategy Not_abs() extends Identity() {
    visit RTerm {
      abs[] -> {
        throw new VisitFailure();
      }
    } 
  }
/*
	public static class Not_abs extends RhotermBasicStrategy {
		public Not_abs() {
			super(`Identity());
		}
		
		public RTerm visit_RTerm(RTerm arg) throws jjtraveler.VisitFailure {
      %match(RTerm arg) {
        abs[] -> {
          throw new VisitFailure();
        }
      }
      return arg;
		}
		
	}
  */

	public static String stringInfix(ListConstraint constraints){
		%match(ListConstraint constraints){
			andC(match(term1,term2),match(term3,term4),Y*) -> {return stringInfix(`term1)+"<"+stringInfix(`term2) +"^"+ stringInfix(`term3)+"<"+stringInfix(`term4) +stringInfix(`Y);}
			andC(match(term1,term2),X*) -> {return stringInfix(`term1)+"<"+stringInfix(`term2);}
			andC(matchKO(),X*) -> {return "KO";}
		}
    return "";
	}	
	
	protected static ListConstraint headIsConstant (Constraint l){
		%match(Constraint l){
			match(app(A1,B1),app(A2,B2)) ->{ 
				return `headIsConstant(match(A1,A2));
			}
	     match(Const(f),Const(f)) -> {
				 return `andC();
	     }
	     //si j'arrive dans le cas de la regle suivante alors les constantes sont forcement differentes
	     match(Const[],Const[])   -> {
				 return `andC(matchKO());
	     }

	     match(Const[],app[]) || match(app[],Const[]) << l -> {
				 return `andC(matchKO());
	     }

		}
    return `andC(l);
	}
	protected static ListConstraint computeMatch(ListConstraint l){
		%match(ListConstraint l){
			andC(match(app(f@Const[],A),app(f,B))) -> {return `andC(match(A,B));}
			andC(match(app(A1,B1),app(A2,B2))) -> {
				ListConstraint result = `computeMatch(andC(match(A1,A2)));
				return `andC(result*,match(B1,B2));}
			_ -> {return `l;}
		}
    return `l;

	}
	protected static ListConstraint mapC(ListConstraint list, ListSubst phi, ListConstraint result){
		%match(ListConstraint list) {
 	    andC(match(P,M),_*) ->{
				return `mapC(list.getTailandC(),phi,andC(match(P,appS(phi,M)),result*));}
 	    _ -> {return `result;}
		}
    return `result;
	} 
	protected static ListSubst mapS(ListSubst list, ListSubst phi, ListSubst result){
		%match(ListSubst list) {
			andS(eq(X,M),_*) ->{
				return `mapS(list.getTailandS(),phi,andS(eq(X,appS(phi,M)),result*));}
 	    _ -> {return `result;}
    }	 
    return `result;
  }
  protected static ListSubst reverse(ListSubst l) {
    %match(ListSubst l) {
      andS() -> { return l; }
      andS(h,t*) -> {
        ListSubst rtail = reverse(`t*);
        return `andS(rtail*,h);
      }
    }
    return l;
  }
  protected static ListConstraint reverse(ListConstraint l) {
    %match(ListConstraint l) {
      andC() -> { return l; }
      andC(h,t*) -> {
        ListConstraint rtail = reverse(`t*);
        return `andC(rtail*,h);
      }
    }
    return l;
  }
}
