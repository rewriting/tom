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

	 private final static String AND_G = "g";
	 private final static String AND_NG = "ng";
	 private final static String AND = " ";

	 private rhotermFactory factory;
	 private GenericTraversal traversal;
	 private boolean hasBeenSimplified;

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
			 
			 appC(c:Constraint,rterm:RTerm) -> RTerm
			 appSt(c:Constraint,rterm:RTerm) -> RTerm
			 appSc(c:Constraint,c2:Constraint) -> Constraint

			 matchH(lhs:RTerm,rhs:RTerm) -> Constraint
			 match(lhs:RTerm,rhs:RTerm) -> Constraint
			 concConstraint( Constraint* ) -> ListConstraint
			 //		 ng() -> Label
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
						app(abs(A,B),C)-> {	return `appC(match(A,C),B) ;}		
						//delta
						app(struct(A,B),C) -> { return `struct(app(A,C),app(B,C));}
						
						//ToSubstVar
						appC(match(X@var(_),A),B) -> {return `appSt(match(X,A),B);}
						//ToSubstAnd
						appC(and("g",C),A) -> {return `appSt(and(AND_G,C),A);}
						//Id
						appC(match(a@constr(_),a),A) -> {return `A;}

						//Replace
						appSt(match(X@var(_),A),X) -> {return `A;}
						appSt(and("g",(_*,match(X@var(_),A),_*)),X) -> {return `A;} 

						//EliminateVar
						appSt(match(var(x),_),Y@var(y)) -> {if ( `x !=  `y ) return `Y;}
						appSt(and("g",(C*,match(var(x),_),D*)),Y@var(y)) -> {if (!(`x.equals(`y))) return `appSt(and("g",concConstraint(C*,D*)),Y);} 
						//EliminateConst
						appSt[rterm=f@constr[]] -> {return `f;}
						//ShareApp
						appSt(phi,app(B,C)) -> {return `app(appSt(phi,B),appSt(phi,C));}
						//ShareStruct
						appSt(phi,struct(A,B)) -> {return `struct(appSt(phi,A),appSt(phi,B));}
						//ShareAbs
						appSt(phi,abs(A,B)) -> {return `abs(A,appSt(phi,B));}
						//ShareAppC
						appSt(phi,appC(C,A)) -> {return `appC(appSc(phi,C),appSt(phi,A));}
						//Compose
						appSt(phi,appSt(psi,A)) -> {return `appSt(and("g",concConstraint(phi,appSc(phi,psi))),A);}
					}
				}
				//rewrite rules for constraints
				else if(t instanceof Constraint) {
					Constraint co = (Constraint)t;
					%match(Constraint co){
						//decompose Structure
						match(struct(A,B),struct(C,D)) -> {return  `and(AND,concConstraint(match(A,C),match(B,D)));}
						//decompose Constructors
						l:match(app1@app(_,_),app2@app(_,_)) -> {
							Constraint isConstant = `matchH(app1,app2);
							//head is constant? --> headIsConstant
							isConstant = `headIsConstant(and(AND,concConstraint(isConstant)));
							%match(Constraint isConstant){
								and(_,(_*,matchH[],_*)) -> {//no ,still one matchH --> head is not Constrant I do not apply the DecomposeF reduction rule
									break l;}
								_ -> {//yes, no matchH
									return isConstant;}
							}
						}
						//simplify
						and(" ",(X*,match(a@constr(_),a),Y*)) -> {return `concConstraint(X*,Y*);}
						and("ng",(X*,match(a@constr(_),a),Y*)) -> {return `concConstraint(X*,Y*);}
						//We suppose the linearity of patterns
						and(" ",c@(match(var(_),_),match(var(_),_))) -> {return `and("g",c);} 
						and(" ",c@(match(var(_),_),and("g",_))) -> {return `and("g",c);} 
						and(" ",c@(and("g",_),and("g",_))) -> {return `and("g",c);} 
						//NGood!!!!!!!!!!!!!!!!: A AJOUTER
						//ShareMatch A Enrichir
						appSc(phi,match(B,C)) -> {return `match(B,appSt(phi,C));}
						//ShareAnd for all types of and
						appSc(phi,and(x,C)) -> {Replace2 r = new Replace2(){
																																				public ATerm apply(ATerm t,Object o){
																																					Constraint c = (Constraint)t;
																																					%match(Constraint c){
																																						x -> {return `appSc((Constraint)o,x);}
																																						
																																					}
																																				}
							};
						return traversal.genericTraversal(`C,r,`phi);
						}
						//ComposeS
						appSc(phi,appSc(psi,theta)) -> {return `appSc(and("g",concConstraint(phi,appSc(phi,psi))),theta);}						
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
				
				return traversal.genericOneStep(t,this);
			}
    };

//on normalise un terme par rapport a un syst de reecri
			public RTerm normalize(RTerm form){
				RTerm res = simplify(form);
				if (res != form) {
//					System.out.println("Resultat intermediaire: " +res);
					return normalize(res);
			}
			else {
				//			System.out.println("Resultat intermediaire: " +res);
				return res;
			}
			
	}
			public Constraint headIsConstant(Constraint c){
				%match(Constraint c){
					and(_,(X*,matchH(app(t1@app(_,_),A3),app(t2@app(_,_),B3)),Y*)) -> {return `headIsConstant(and(AND,concConstraint(X*,matchH(t1,t2),match(A3,B3),Y*)));}
					and(_,(X*,matchH(app(f@constr(_),A),app(f,B)),Y*)) -> {return `and(AND,concConstraint(X*,match(A,B),Y*));}
					_ -> {return c;}

				}
			}
	 public Rho(rhotermFactory factory) {
		 this.factory = factory;
		 this.traversal = new GenericTraversal();
	 }

  public rhotermFactory getRhotermFactory() {
    return factory;
  }
	 public void run(){
		 RTerm res;
		 RTerm X = `var("X");
		 RTerm Y = `var("Y");
		 RTerm f = `constr("f");
		 RTerm trois = `constr("3");
		 RTerm cinq = `constr("5");
		 System.out.println("--1re REDUCTION: (X -> X) 3");
		 res = `app(abs(X,X),trois);
		 System.out.println("J'obtiens: " + normalize(res));
		 System.out.println("--2me REDUCTION: f((X->X)3)");
		 res = `app(f,app(abs(X,X),trois));
		 System.out.println("J'obtiens: " + normalize(res));
		 System.out.println("--3me REDUCTION: Y -> f((X->X)3)");
		 res = `abs(Y,app(f,app(abs(X,X),trois)));
		 System.out.println("J'obtiens: " + normalize(res));
		 System.out.println("--4me REDUCTION: (Y -> f((X->X)3))5");
		 res = `app(abs(Y,app(f,app(abs(X,X),trois))),cinq);;
		 System.out.println("J'obtiens apres normalize: " + normalize(res));
		 System.out.println("--Debut de la normalisation");
		 System.out.println("J'obtiens apres normalize: " + normalize(res));
		 System.out.println("--5eme REDUCTION: [X << A landg Y <<B ]X");
		 res = `appC(and(AND_G,concConstraint(match(X,trois),match(Y,cinq))),X);;
		 System.out.println("--Debut de la normalisation");
		 System.out.println("J'obtiens apres normalize: " + normalize(res));
		 System.out.println("--6eme REDUCTION: (f(X,Y) -> X) f(3,5)");
		 res = `app(abs(app(app(f,X),Y),X),app(app(f,trois),cinq));
		 System.out.println("--Debut de la normalisation");
		 System.out.println("J'obtiens apres normalize: " + normalize(res));
		 System.out.println("--7eme REDUCTION: (f(X,Y) -> Y) f(3,5)");
		 res = `app(abs(app(app(f,X),Y),Y),app(app(f,trois),cinq));
		 System.out.println("--Debut de la normalisation");
		 System.out.println("J'obtiens apres normalize: " + normalize(res));
		 
		 while(true){
//"app(abs(var(\"Y\"),app(constr(\"f\"),app(abs(var(\"X\"),var(\"X\")),constr(\"3\")))),constr(\"5\"))");
			 RTerm resu = factory.RTermFromString(Clavier.lireLigne());
			 System.out.println(normalize(resu));
		 }


	 }
	 public final static void main(String[] args) {
		 Rho rhoEngine = new Rho(rhotermFactory.getInstance(new PureFactory(16)));
		 rhoEngine.run();
	 }
 }
 
