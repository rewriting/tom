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
// import tom.library.traversal.*;
 import minirho.rho.rhoterm.*;
 import minirho.rho.rhoterm.types.*;

 public class Rho {

	 private Factory factory;
//	 private GenericTraversal traversal;
	 private boolean hasBeenSimplified;
		 //Signature de mon langage, utilise pour les tests.
	 private RTerm f = null;
	 private RTerm g = null;
	 private RTerm X = null;

		 //On suppose la conjonction de contraintes representee par des listes de contraintes (donc assoc). Dans les regles on se rend compte que en fait la commutativite du \land n'est pas utile.
	 %vas {
		 // extension of adt syntax
		 module rhoterm

		 public
			 sorts RTerm Constraint

		 abstract syntax
			 var(name:String) -> RTerm
			 constr(name:String) -> RTerm

			 abs(lhs:RTerm,rhs:RTerm) -> RTerm
			 app(lhs:RTerm,rhs:RTerm) -> RTerm
//structure theorie vide
			 struct(lhs:RTerm,rhs:RTerm) -> RTerm
			 
			 appC(constr:Constraint,rterm:RTerm) -> RTerm
			 appSt(constr:Constraint,rterm:RTerm) -> RTerm
			 appSc(constr:Constraint,constr2:Constraint) -> RTerm

//			 matchH(lhs:RTerm,rhs:RTerm) -> Constraint
			 match(lhs:RTerm,rhs:RTerm) -> Constraint
			 concListConstraint( Constraint* ) -> ListConstraint
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


	 public Rho(Factory factory) {
		 this.factory = factory;
//		 this.traversal = new GenericTraversal();
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
	
  public Factory getRhotermFactory() {
    return factory;
  }
    public void run(){
			RTerm essai = `appSt(simplifyConst(match(app(constr("f"),var("X")),app(constr("f"),constr("a")))),var("X"));
			System.out.println(simplifyTerm(essai));
    }
    public final static void main(String[] args) {
			Rho rhoEngine = new Rho(new Factory(new PureFactory()));
			rhoEngine.run();


  }
}
