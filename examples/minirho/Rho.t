/*
 * Copyright (c) 2004, INRIA
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

	 private final String AND_G = "g";
	 private final String AND_NG = "ng";
	 private final String AND = " ";


	 private rhotermFactory factory;
	 private GenericTraversal traversal;
	 private boolean hasBeenSimplified;
		 //Signature de mon langage, utilise pour les tests.
	 private RTerm f = null;
	 private RTerm g = null;
	 private RTerm X = null;

		 //On suppose la conjonction de contraintes representee par des listes de contraintes (donc assoc). Dans les regles on se rend compte que en fait la commutativite du \land n'est pas utile.
	%vas{
		 module rhoterm
     imports 
		 public
			 sorts RTerm Constraint

		 abstract syntax
			 var(na:String) -> RTerm
			 constr(na:String) -> RTerm

			 abs(lhs:RTerm,rhs:RTerm) -> RTerm
			 app(lhs:RTerm,rhs:RTerm) -> RTerm
//structure theorie vide
			 struct(lhs:RTerm,rhs:RTerm) -> RTerm
			 
			 appC(constr:Constraint,rterm:RTerm) -> RTerm
			 appSt(constr:Constraint,rterm:RTerm) -> RTerm
			 appSc(constr:Constraint,constr2:Constraint) -> RTerm

//			 matchH(lhs:RTerm,rhs:RTerm) -> Constraint
			 match(lhs:RTerm,rhs:RTerm) -> Constraint
			 concConstraint( Constraint* ) -> ListConstraint
			 and(l:String, constr:ListConstraint ) -> Constraint
			 }
//
			 
		 
			 // 			matchng(lhs:RTerm,rhs:RTerm) -> Constraint
			 // 			matchg(lhs:RTerm,rhs:RTerm) -> Constraint
//			 g(constraint:ListConstraint) -> Constraint
//			 ng(constraint:ListConstraint) -> Constraint
			 
			 // concConstraint( MatchEquation* ) -> Constraint
//			 concConstraintg( MatchConstraint* ) -> Constraint
//			 concConstraintng( MatchConstraint* ) -> Constraint

//fonction qui fait un pas de reecriture a n'importe quelle position
	 public RTerm simplify(RTerm t){
		 return (RTerm)replace.apply(t);
	 }

  Replace1 replace = new Replace1() {
			// Rewrite rules for terms
      public ATerm apply(ATerm t) {
				if (t instanceof RTerm) {
					RTerm term = (RTerm) t;
					%match(RTerm term){
						//rho 
						app(abs(A,B),C)-> {return `appC(match(A,C),B) ;}		
					}
				}
				//rewrite rules for constraints
				else if(t instanceof Constraint) {
					Constraint co = (Constraint)t;
					%match(Constraint co){
						//decompose Structure
						match(struct(A,B),struct(C,D)) -> {return  `and(AND,concConstraint(match(A,C),match(B,D)));}
					}
				}
				
				//2. Strategies to go deeper
				if (t instanceof RTerm){
					RTerm term = (RTerm) t;
					%match(RTerm term){
						//do not reduce the right-hand side of an abstraction
						abs(A,B) -> {return `abs((RTerm)apply(A),B);}
					}
				}
				return traversal.genericTraversal(t,this);
			}
    };

//on normalise un terme par rapport a un syst de reecri
	public RTerm normalize(RTerm form){
		RTerm res = simplify(form);
			if (res != form) {
				System.out.println("Resultat intermediaire: " +res);
				return normalize(res);
			}
			else {
				System.out.println("Resultat intermediaire: " +res);
				return res;
			}
		
	}
	 public Rho(rhotermFactory factory) {
		 this.factory = factory;
		 this.traversal = new GenericTraversal();
	 }

	 public RTerm simplifyTerm(RTerm t){
		 %match(RTerm t){
			 //replace
			 appSt(match(Z@var(_),A),Z) -> { return A;}
				_ -> {return t;}
		 }
	 }
	 public Constraint simplifyConst(Constraint c){
		 %match(Constraint c){
			 match(app(f@constr(_),A1) , app(f,B1)) -> {return `match(A1,B1);}
			 _ -> {return c;}
		 }
	 }
	
  public rhotermFactory getRhotermFactory() {
    return factory;
  }
    public void run(){
			System.out.println("PREMIERE REDUCTION");
			RTerm essai = `app(abs(var("X"),abs(var("X"),(appSt(match(app(constr("f"),var("X")),app(constr("f"),constr(2))),var("X"))))),constr(2));
			System.out.println(simplify(essai));

			System.out.println("DEUXIEME REDUCTION");
			RTerm essai2 = `abs(var("X"),app(abs(var("X"),appSt(match(app(constr("f"),var("X")),app(constr("f"),constr(2))),var("X"))),constr(2)));
			System.out.println(simplify(essai2));

			System.out.println("TROISIEME REDUCTION");
			RTerm essai3 = `app(abs(var("X"),appSt(match(app(constr("f"),var("X")),app(constr("f"),constr(2))),var("X"))),constr(2));
			System.out.println(simplify(essai3));

    }
	 public final static void main(String[] args) {
		 Rho rhoEngine = new Rho(rhotermFactory.getInstance(new PureFactory(16)));
		 rhoEngine.run();
	 }
 }
