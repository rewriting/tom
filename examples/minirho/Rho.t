 /*
  * Copyright (c) 2004-2005, INRIA
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


 package minirho;

  import aterm.*;
  import aterm.pure.*;
  import minirho.rho.rhoterm.*;
  import minirho.rho.rhoterm.types.*;

  import jjtraveler.reflective.VisitableVisitor;
  import jjtraveler.VisitFailure;

 public class Rho {
     private rhotermFactory factory;

     public Rho(rhotermFactory factory) {
	 this.factory = factory;
     }
     public rhotermFactory getRhotermFactory() {
	 return factory;
     }

     %include { mutraveler.tom }
     %vas{
	 module rhoterm
	     imports 
	     public
	     sorts RTerm Constraint Subst ListConstraint ListSubst
	     abstract syntax
	     var(na:String) -> RTerm
	     const(na:String) -> RTerm
	     abs(lhs:RTerm,rhs:RTerm) -> RTerm
	     app(lhs:RTerm,rhs:RTerm) -> RTerm
	     struct(lhs:RTerm,rhs:RTerm) -> RTerm //structure is couple
	     appC(co:ListConstraint,term:RTerm) -> RTerm
	     appS(su:ListSubst,term:RTerm) -> RTerm

	     andC( Constraint* ) -> ListConstraint
	     andS( Subst* ) -> ListSubst
	     match(lhs:RTerm,rhs:RTerm) -> Constraint 
	     eq(var:RTerm,rhs:RTerm) -> Subst 

	     }  
     public final static void main(String[] args) {
	 Rho rhoEngine = new Rho(rhotermFactory.getInstance(new PureFactory(16)));
	 rhoEngine.run();
     }
     public void run(){
      	 RTerm subject;
     // 	 Visitable temp,sub;
     VisitableVisitor rules = new ReductionRules();
     VisitableVisitor print = new Print();
     
     String s;
     System.out.println(" ******************************************************************\n RomCal: an implementation of the explicit rho-calculus in Tom\\n by Germain Faure and ...\n version 0.1. Devolp in few hours.Please use it with care \n ******************************************************************");
     while(true){

 	     System.out.print("RomCal>");
 	     s = Clavier.lireLigne();
 	     subject = factory.RTermFromString(s);
	     try{
		 `Repeat(Sequence(OnceBottomUp(rules),Try(print))).visit(subject);
	     } catch (VisitFailure e) {
		 System.out.println("reduction failed on: " + subject);
	     }
     }
 }

     public String test(String s){
	 RTerm subject = factory.RTermFromString(s);
	 VisitableVisitor rules = new ReductionRules();

	 try{
	     return "" + `(Innermost(rules).visit(subject));
	 } catch (VisitFailure e) {
	     return ("reduction failed on: " + subject);
	 }
	 
     }
   class Print extends rhotermVisitableFwd {
	 public Print() {
	     super(`Fail());
	 }
       public RTerm visit_RTerm(RTerm arg) throws  VisitFailure { 
	   System.out.println(arg);
	     return arg;
	 }
   }
     class ReductionRules extends rhotermVisitableFwd {
	 public ReductionRules() {
	     super(`Fail());
	 }
	 public RTerm visit_RTerm(RTerm arg) throws  VisitFailure { 
	     %match(RTerm arg){
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
		 appS(andS(_*,eq(X@var[],M),_*),X) -> {return `M;}

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
		 //ATTENTION AU CAS n is 0
		 //ALPHA-CONV!!

		 /*Compose */
		 appS(phi@andS(l*),appS(andS(L*),N)) -> {
		     ListSubst result = `mapS(((ListSubst)(L.reverse())),phi,andS());
		     return `appS(andS(l*,result*),N);}
		 //ATTENTION AU CAS n is 0
		 //ALPHA-CONV!!
		 appC((X*,match(f@const[],f),Y*),M) -> {return `appC(andC(X*,Y*),M);}

	     }	    
	     throw new VisitFailure();
	 }
	 public ListConstraint visit_ListConstraint(ListConstraint l) throws VisitFailure {
	     %match(ListConstraint l){
		 /*Decompose Struct */
		 (X*,match(struct(M1,M2),struct(N1,N2)),Y*) ->{
		     return `andC(X*,match(M1,N1),match(M2,N2),Y*);}

		 /* Patterns lineaires, pas besoin de la regle Idem */

		 /* Decompose Algebriques n = 0 */
		 (X*,match(f@const[],f),Y*) -> {return `andC(X*,Y*);}

		 /* Decompose Algebriques n > 0 */
		 l:(X*,m@match(app(A1,A2),app(B1,B2)),Y*) -> {
		     ListConstraint head_is_constant = `headIsConstant(andC(m));
		     %match(ListConstraint head_is_constant){
			 (match[]) -> {break l;}
		     }
		     ListConstraint result = `computeMatch(andC(m));
		     return `andC(X*,result*,Y*);
		}
		
	    }
	    throw new VisitFailure();
	}
    }
     private ListConstraint headIsConstant (ListConstraint l){
	 %match(ListConstraint l){
	     (match(app(app(A1,B1),C1),app(app(A2,B2),C2))) -> {return `headIsConstant(andC(match(app(A1,B1),app(A2,B2))));}
	     (match(app(f@const[],A1),app(f,A2))) -> {return `andC();}
	     _ -> {return `l;}
	 }

     }
     private ListConstraint computeMatch(ListConstraint l){
	 %match(ListConstraint l){
	     (match(app(f@const[],A),app(f,B))) -> {return `andC(match(A,B));}
	     (match(app(A1,B1),app(A2,B2))) -> {
		 ListConstraint result = `computeMatch(andC(match(A1,A2)));
		 return `andC(result*,match(B1,B2));}
	     _ -> {return `l;}
	 }
     }
     public ListConstraint mapC(ListConstraint list, ListSubst phi, ListConstraint result){
 	%match(ListConstraint list) {
 	    (match(P,M),_*) ->{
 		return `mapC(list.getTail(),phi,andC(match(P,appS(phi,M)),result*));}
 	    _ -> {return `result;}
 	}	
     }    
     public ListSubst mapS(ListSubst list, ListSubst phi, ListSubst result){
 	%match(ListSubst list) {
 	    (eq(X,M),_*) ->{
 		return `mapS(list.getTail(),phi,andS(eq(X,appS(phi,M)),result*));}
 	    _ -> {return `result;}
 	}	
     }    
}
 
