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
 import tom.library.traversal.*;
 import minirho.rho.rhoterm.*;
 import minirho.rho.rhoterm.types.*;

 public class Rho {

	 private rhotermFactory factory;
	 private GenericTraversal traversal;


   public Rho(rhotermFactory factory) {
     this.factory = factory;
     this.traversal = new GenericTraversal();
   }
  public rhotermFactory getRhotermFactory() {
    return factory;
  }

	%vas{
		 module rhoterm
     imports 
		 public
			 sorts RTerm Constraint ListConstraint
		 abstract syntax
//TERMS
			 var(na:String) -> RTerm
			 const(na:String) -> RTerm
			 abs(lhs:RTerm,rhs:RTerm) -> RTerm
			 app(lhs:RTerm,rhs:RTerm) -> RTerm
			 struct(lhs:RTerm,rhs:RTerm) -> RTerm //structure theorie vide
			 appC(co:ListConstraint,term:RTerm) -> RTerm
//CONSTRAINTS
			 appSc(subst:Constraint,co:Constraint) -> Constraint //Substition application on constraints
			 match(pa:RTerm,rhs:RTerm) -> Constraint
			 eq(var:RTerm,rhs:RTerm) -> Constraint
			 concConstraint( Constraint* ) -> ListConstraint

//AUXILARY: not strictly speaking needed: use to check if the head of an application tree is a constructor
			 matchKO(pa:RTerm,rhs:RTerm) -> Constraint
			 matchOK(pa:RTerm,rhs:RTerm) -> Constraint
			 }

//All the reduction rules of the RhoXC-calculus. First, those transforming RTerms. Secondly, those transforming Constraints
	 Replace1 reductionRules = new Replace1() {
			 public ATerm apply(ATerm t) {
				if (t instanceof RTerm) {// Reduction rules for terms
					RTerm term = (RTerm) t;
					%match(RTerm term){

						/* Garbage collector 
							 appC(matchKO[],_) -> {return `const("null");}
							 app(const("null"),A) -> {return `const("null");}
							 struct(A,const("null")) -> {return `A;}
							 struct(const("null"),A) -> {return `A;} 
						*/
							
						//rho 
						app(abs(P,M),N)-> {	
						    return `appC(concConstraint(match(P,N)),M) ;
						}		
						//delta
						app(struct(M1,M2),N) -> { 
							return `struct(app(M1,N),app(M2,N));
						}
						//ToSubstVar
						appC((match(X@var[],A)),B) -> {
						    return `appC(concAnd(eq(X,A)),B);
						}
						//ToSubstAnd
						appC((Phi*,match(X@var[],M),Xsi*),N) -> {
							return `appC(concAnd(Phi,Xsi),appC(eq(X,M),N));
						}
					}
				}
			 }
		 };
	 public final static void main(String[] args) {
		 Rho rhoEngine = new Rho(rhotermFactory.getInstance(new PureFactory(16)));
	 }
 }
