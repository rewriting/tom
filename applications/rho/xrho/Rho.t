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


package xrho;

import aterm.*;
import aterm.pure.*;
import xrho.rhoterm.*;
import xrho.rhoterm.types.*;
import xrho.rhoterm.types.rterm.Abs;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;


//import tom.library.adt.mutraveleradt.*;
//import tom.library.adt.mutraveleradt.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;

import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

import java.io.*;

public class Rho {
	private RhotermFactory factory;
	
	public Rho(RhotermFactory factory) {
		this.factory = factory;
	}
	public RhotermFactory getRhotermFactory() {
		return factory;
	}
	
	
	%include { mutraveler.tom }
	%include { rhoterm/Rhoterm.tom }
	
	%op VisitableVisitor Not_abs {
		make() {new Not_abs() }
	}

	%op VisitableVisitor One_abs(strat:VisitableVisitor) {
		make(v) {`Sequence(Not_abs(),One(v)) }//new One_abs((VisitableVisitor)v)
	}
	%op VisitableVisitor All_abs(strat:VisitableVisitor) {
		make(v) {`Sequence(Not_abs(),All(v)) }//new One_abs((VisitableVisitor)v)
	}

	VisitableVisitor rules = new ReductionRules();
	VisitableVisitor onceBottomUp = `mu(MuVar("x"),Choice(One(MuVar("x")),rules));
	VisitableVisitor print = new Print();
	//STRATEGIE OUTERMOST
	VisitableVisitor oneStepWeakNormalisation = `mu(MuVar("x"),Choice(rules,One_abs(MuVar("x"))));
	VisitableVisitor myStrategy = MuTraveler.init(`Repeat(Sequence(oneStepWeakNormalisation,Try(print))));

//	VisitableVisitor myStrategy = `Repeat(Sequence(rules,print));
	//STRATEGIE INNERMOST (FAST)
//	VisitableVisitor myStrategy = `mu(MuVar("x"),Sequence(All_abs(MuVar("x")),Choice(Sequence(rules,MuVar("x")),Identity)));
	public final static void main(String[] args) {
		Rho rhoEngine = new Rho(RhotermFactory.getInstance(SingletonFactory.getInstance()));
		rhoEngine.run();
	}
	

	public void run(){
		RTerm subject = `const("undefined");
		String s;
		System.out.println(" ******************************************************************\n xRho: an implementation  in Tom of the explicit rho-calculus \n with weak normalization and linear first-order patterns\n By Germain Faure\n version Beta. Please use it with care. \n ******************************************************************");
    RhoLexer lexer = new RhoLexer(System.in); // Create parser attached to lexer
    RhoParser parser = new RhoParser(lexer);
		while(true){
			System.out.print("xRho>");
      try {
				subject = parser.program();
				System.out.println("Resultat du parsing: " + subject);
			} catch (Exception e) {
				System.out.println(e);
				
			}
			try{
				System.out.println(stringInfix((RTerm)myStrategy.visit(subject)));
			} catch(VisitFailure e) {
				System.out.println("reduction failed on: " + subject);
			}
		}
	}


	public String test(String s){
		RTerm subject = `const("undefined");
		StringReader sr = new StringReader(s);
    RhoLexer lexer = new RhoLexer(sr); // Create parser attached to lexer
    RhoParser parser = new RhoParser(lexer);
		try {
			subject = parser.program();
//				System.out.println("Resultat du parsing: " + subject);
		} catch (Exception e) {
			return e.toString();
		}
		try{
			subject = (RTerm)myStrategy.visit(subject);
			return stringInfix(subject);
		} catch(VisitFailure e) {
			return "reduction failed on: " + subject ;
		}
	}
	public String testWithParser(String s){
		String result = null;
		return result;
	}
	
	class Print extends RhotermVisitableFwd {
		public Print() {
			super(`Fail());
		}
		public RTerm visit_RTerm(RTerm arg) throws  VisitFailure { 
			System.out.println("|-->>" + arg);
			return arg;
		}
	}
	class ReductionRules extends RhotermVisitableFwd {
		public ReductionRules() {
			super(`Fail());
		}
		public RTerm visit_RTerm(RTerm arg) throws  VisitFailure { 
			%match(RTerm arg){
				/*Compose */
				appS(phi@andS(l*),appS(andS(L*),N)) -> {
					ListSubst result = `mapS(((ListSubst)(L.reverse())),phi,andS());
					return `appS(andS(l*,result*),N);}
				//ALPHA-CONV
				
				
				/* Garbage collector */
				appC((_*,matchKO(),_*),_) -> {return `stk();}
				app(stk(),A) -> {return `stk();}
				struct(A,stk()) -> {return `A;}
				struct(stk(),A) -> {return `A;}
				
				
				/*Rho*/
				app(abs(P,M),N) -> {return `appC(andC(match(P,N)),M);}
				
				/*Delta*/
				app(struct(M1,M2),N) -> {return `struct(app(M1,N),app(M2,N));}
				
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
				appS(_,c@const[]) -> {return `c;}
				
				/*Abs*/
				appS(phi,abs(P,M)) -> {return `abs(P,appS(phi,M));}
				//ALPHA-CONVERSION NECESSAIRE!
				
				
				/*App*/
				appS(phi,app(M,N)) -> {return `app(appS(phi,M),appS(phi,N));}
				
				/*Struct*/
				appS(phi,struct(M,N)) -> {return `struct(appS(phi,M),appS(phi,N));}
				
				/*Constraint*/
				appS(phi,appC(andC(L*),M)) -> {
					ListConstraint result = `mapC(((ListConstraint)(L.reverse())),phi,andC());
					return `appC(andC(result*),appS(phi,M));}
				/* La regle est correcte pour n is 0 */
				//ALPHA-CONV!!
				/* Patterns lineaires, pas besoin de la regle Idem */



			 }
			 throw new VisitFailure();

		 }
 		public ListConstraint visit_ListConstraint(ListConstraint l) throws VisitFailure {
 			%match(ListConstraint l){
				/*Decompose n = m = 0*/
				(X*,match(f@const[],f),Y*) -> {return `andC(X*,Y*);}
				
				/*Decompose_ng n = m = 0*/
				//si j'arrive dans la regle suivant c'est que les const sont diff
				(X*,match(const[],const[]),Y*) -> {return `andC(X*,matchKO(),Y*);}
				
				/*Decompose et Decompose_ng min(n,m) > 0 */
				l:(X*,m@match(app[],app[]),Y*) -> {
					ListConstraint head_is_constant = `headIsConstant(m);
					%match(ListConstraint head_is_constant) {
						(match[]) -> { break l; }
						(matchKO()) -> { return `andC(X*,matchKO(),Y*); }
					}
					ListConstraint result = `computeMatch(andC(m));
					return `andC(X*,result*,Y*);
				}
				l:(X*,m@match(app[],const[]),Y*)  -> {
					ListConstraint head_is_constant = `headIsConstant(m);
					%match(ListConstraint head_is_constant) {
						(match[]) -> { break l; }
						(matchKO()) -> { return `andC(X*,matchKO(),Y*); }
					}
					ListConstraint result = `computeMatch(andC(m));
					return `andC(X*,result*,Y*);
				}
				l:(X*,m@match(const[],app[]),Y*) -> {
					ListConstraint head_is_constant = `headIsConstant(m);
					%match(ListConstraint head_is_constant) {
						(match[]) -> { break l; }
						(matchKO()) -> { return `andC(X*,matchKO(),Y*); }
					}
					ListConstraint result = `computeMatch(andC(m));
					return `andC(X*,result*,Y*);
				}
				
				/*Decompose Struct */
				(X*,match(struct(M1,M2),struct(N1,N2)),Y*) ->{
					return `andC(X*,match(M1,N1),match(M2,N2),Y*);
				}
			}
			throw new VisitFailure();
 		}
 	}
	public  String stringInfix(RTerm term){
		//suis les priorites donnes par RhoParser.g.t
		%match(RTerm term){
			appS(substs,term1) -> {return "["+ stringInfix(substs)+"]" + "("+stringInfix(term1)+")";}
			appC(constrainsts,term1) -> {return "["+ stringInfix(constrainsts)+"]" + "("+stringInfix(term1)+")";}
			app(term1,term2) -> {return "("+stringInfix(term1)+"."+stringInfix(term2)+")";}
			abs(term1,term2) -> {return "("+stringInfix(term1)+"->"+stringInfix(term2)+")";}
			struct(term1,term2) -> {return "("+stringInfix(term1)+"|"+stringInfix(term2)+")";}
			var(s) -> {return s;}
			const(s) -> {return s;}
			stk() -> {return "stk";}
			_ -> {return "";}
		}
	}
	public String stringInfix(ListSubst substs){
		%match(ListSubst substs){
			andS(eq(term1,term2),eq(term3,term4),Y*) -> {return stringInfix(term1)+"="+stringInfix(term2) +"^"+ stringInfix(term3)+"="+stringInfix(term4) +stringInfix(Y);}
			andS(eq(term1,term2),X*) -> {return stringInfix(term1)+"="+stringInfix(term2);}
			_ -> {return "";}
		}
	}

	public class Not_abs extends RhotermVisitableFwd {
		public Not_abs() {
			super(`Identity());
		}
		
		public RTerm visit_RTerm_Abs(Abs arg) throws jjtraveler.VisitFailure {
			throw new VisitFailure();
		}
		
	}
	public String stringInfix(ListConstraint constraints){
		%match(ListConstraint constraints){
			andC(match(term1,term2),match(term3,term4),Y*) -> {return stringInfix(term1)+"<"+stringInfix(term2) +"^"+ stringInfix(term3)+"<"+stringInfix(term4) +stringInfix(Y);}
			andC(match(term1,term2),X*) -> {return stringInfix(term1)+"<"+stringInfix(term2);}
			_ -> {return "";}
		}
	}	
	
	protected ListConstraint headIsConstant (Constraint l){
		%match(Constraint l){
			match(app(A1,B1),app(A2,B2)) ->{ 
				return `headIsConstant(match(A1,A2));
			}
	     match(const(f),const(f)) -> {
				 return `andC();
	     }
	     //si j'arrive dans le cas de la regle suivante alors les constantes sont forcement differentes
	     match(const[],const[])   -> {
				 return `andC(matchKO());
	     }

	     match(const[],app[]) | match(app[],const[]) -> {
				 return `andC(matchKO());
	     }

	     _ -> {return `andC(l);}
		}
		
	}
	protected ListConstraint computeMatch(ListConstraint l){
		%match(ListConstraint l){
			(match(app(f@const[],A),app(f,B))) -> {return `andC(match(A,B));}
			(match(app(A1,B1),app(A2,B2))) -> {
				ListConstraint result = `computeMatch(andC(match(A1,A2)));
				return `andC(result*,match(B1,B2));}
			_ -> {return `l;}
		}
	}
	protected ListConstraint mapC(ListConstraint list, ListSubst phi, ListConstraint result){
		%match(ListConstraint list) {
 	    (match(P,M),_*) ->{
				return `mapC(list.getTail(),phi,andC(match(P,appS(phi,M)),result*));}
 	    _ -> {return `result;}
		}	
	}    
	protected ListSubst mapS(ListSubst list, ListSubst phi, ListSubst result){
		%match(ListSubst list) {
			(eq(X,M),_*) ->{
				return `mapS(list.getTail(),phi,andS(eq(X,appS(phi,M)),result*));}
 	    _ -> {return `result;}
		}	
	}
}
