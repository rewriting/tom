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

	 private rhotermFactory factory;
	 private GenericTraversal traversal;

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
			 andS( Constraint* ) -> ListSubst
			 match(lhs:RTerm,rhs:RTerm) -> Constraint 
			 eq(var:RTerm,rhs:RTerm) -> Subst 

			 }

//All the reduction rules of the RhoXC-calculus. First, those transforming RTerms. Secondly, those transforming Constraints
// 	 Replace1 reductionRules = new Replace1() {
// 			 public ATerm apply(ATerm t) {
// //				 System.out.println("J'essaye de reduire: " +  t);

// 				if (t instanceof RTerm) {// Reduction rules for terms
// 					RTerm term = (RTerm) t;
// 					%match(RTerm term){
// 						//rho 
//	app(abs(A,B),C)-> {	return `appC(andC(),B);}		
// 						//delta
// 						app(struct(A,B),C) -> { return `struct(app(A,C),app(B,C));}
// 						//ToSubstVar
// 						appC(match(X@var[],A),B) -> {return `appSt(match(X,A),B);}
// 						//ToSubstAnd
// 						appC(and(g(),C),A) -> {return `appSt(and(g,C),A);}
// 						//Id
// 						appC(match(a@const[],a),A) -> {return `A;}
// 						//Replace
// 						appSt(match(X@var[],A),X) -> {return `A;}
// 						appSt(and(g(),(_*,match(X@var[],A),_*)),X) -> {return `A;} 
// 						//EliminateVar
// 						appSt(match[pa=var(x)],Y@var(y)) -> { if (`x !=  `y) return `Y;}
// 						appSt(and(g(),(C*,match[pa=var(x)],D*)),Y@var(y)) -> {if (`x != `y) return `appSt(and(g,concConstraint(C*,D*)),Y);} 
// 						//EliminateConst
// 						appSt[term=f@const[]] -> {return `f;}
// 						//ShareApp
// 						appSt(phi,app(B,C)) -> {return `app(appSt(phi,B),appSt(phi,C));}
// 						//ShareStruct
// 						appSt(phi,struct(A,B)) -> {return `struct(appSt(phi,A),appSt(phi,B));}
// 						//ShareAbs
// 						appSt(phi,abs(A,B)) -> {return `abs(A,appSt(phi,B));}
// 						//ShareAppC
// 						appSt(phi,appC(C,A)) -> {return `appC(appSc(phi,C),appSt(phi,A));}

// 					}
	 //			}
// 				else if(t instanceof Constraint) {//Reduction rules for constraints
// 					Constraint co = (Constraint)t;
// 					%match(Constraint co){
// 						//ComposeS
// 						appSc(phi,appSc(psi,theta)) -> {return `appSc(and(g,concConstraint(phi,appSc(phi,psi))),theta);}						
// 						//decompose Structure
// 						match(struct(A,B),struct(C,D)) -> {return  `and(dk,concConstraint(match(A,C),match(B,D)));}
// 						//decompose Constructors
// 						l:match(app1@app[],app2@app[]) -> {
// 							//On peut peut-etre optimiser ici: construire la liste de filtrage en meme tps que l'on teste
// 							//1. on teste si le symbole de tete est une constante
// 							Constraint isConstant = `matchH(app1,app2);
// 							//head is constant? --> headIsConstant
// 							isConstant = headIsConstant(isConstant);
// 							%match(Constraint isConstant){
// 								matchH[] -> {
// 									System.out.println("no, still one matchH");
// 									break l;}//no ,still one matchH
// 								//								_ -> {
// 								//								System.out.println("yes!");
// 								//	}
// 							}
// 							//2. on calcule le resultat a retourner.
// 							//							System.out.println("DANS MA CONTRAINTE J'AI" + co);
// 							//					System.out.println("APRES J'AI" + computeMatch.apply(co));
// 							return computeMatch.apply(co);
// 						}
// 						//simplify
// 						and(dk(),(X*,match(a@const[],a),Y*)) -> {return `and(dk(),concConstraint(X*,Y*));}
// 						and(ng(),(X*,match(a@const[],a),Y*)) -> {return `and(ng(),concConstraint(X*,Y*));}
// 						//We suppose the linearity of patterns
// //						and(dk(),c@(match[pa=var[]],match[pa=var[]])) -> {return `and(g,c);} 
// 						and(dk(),(X*,c@match[pa=var[]],Y*,d@match[pa=var[]],Z*)) -> {return `and(dk,concConstraint(X*,and(g,concConstraint(c,d)),Y*,Z*));} 

// 						//the following rule is duplicated because we have to take care of commutativity
// //						and(dk(),c@(match[pa=var[]],and[lab=g()])) -> {return `and(g,c);} 
// 						and(dk(),c@(match[pa=var[]],and[lab=g()])) -> {return `and(g,c);} 

// 						and(dk(),c@(and[lab=g()],match[pa=var[]])) -> {return `and(g,c);} 

// 						and(dk(),c@(and[lab=g()],and[lab=g()])) -> {return `and(g,c);} 
// 						//NGood!!!!!!!!!!!!!!!!: A AJOUTER
// 						//ShareMatch A Enrichir
// 						appSc(phi,match(B,C)) -> {return `match(B,appSt(phi,C));}
// 						//ShareAnd for all types of and
// 						appSc(phi,and(x,C)) -> {
// 							Replace2 r = new Replace2(){
// 									public ATerm apply(ATerm t,Object o){
// 										Constraint c = (Constraint)t;
// 										%match(Constraint c){
// 											x -> {return `appSc((Constraint)o,x);}
// 										}
// 									}
// 								};
// 							return `and(x,(ListConstraint)traversal.genericTraversal(C,r,phi));
// 						}
// 						and(l,(X*,and(l,(C*)),Y*)) -> {return `and(l,concConstraint(X*,C*,Y*));}
						
// 					}
// 				}
// 				//Particular strategies 
// 				if (t instanceof RTerm){
// 					RTerm term = (RTerm) t;
// 					%match(RTerm term){
// 						//do not reduce the right-hand side of an abstraction
// 						abs(A,B) -> {return `abs((RTerm)apply(A),B);}
// 					}
// 				}
// 				//PLEASE MODIFY!!!
// //				return traversal.genericOneStep(t,this);
// 				return traversal.genericTraversal(t,this);
				//		 }
// 		 };
	 
// //try to normalize
// 	 public  ATerm normalize(ATerm form, Replace1 r){
// 		 ATerm res = r.apply(form);
// 		 if (res != form) {
// 			 System.out.println("|--> " + res);
// 			 return normalize(res,r);
// 		 }
// 		 else {
// 			 return res;
// 		 }
//}
 public Rho(rhotermFactory factory) {
		 this.factory = factory;
		 this.traversal = new GenericTraversal();
	 }
  public rhotermFactory getRhotermFactory() {
    return factory;
  }

//	 public  void run(){

// 		 RTerm resu;
// 		 String s;
// 		 System.out.println(" ******************************************************************\n RomCal: an implementation of the explicit rho-calculus in Tom\n by Germain Faure and ...\n version 0.1 \n ******************************************************************");
// 		 Constraint c = `and(dk(),concConstraint(and(dk,concConstraint(match(var("X"),app(abs(var("X"),var("X")),const("a")))))));
// 		 System.out.println(normalize(c,reductionRules));

// 		 while(true){
// 			 System.out.print("RomCal>");
// 			 s = Clavier.lireLigne();
// 			 resu = factory.RTermFromString(s);
// 			 System.out.println(" " + printInfix(resu));
// 			 printInfix((RTerm)normalize(resu,reductionRules));
// 		 }

// 	 }
    
	 public static void main(String[] args) {
		 Rho rhoEngine = new Rho(rhotermFactory.getInstance(new PureFactory(16)));
//		 rhoEngine.run();
	 }


 }
 
