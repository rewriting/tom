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
			 matchH(pa:RTerm,rhs:RTerm) -> Constraint
//			 matchOK(pa:RTerm,rhs:RTerm) -> Constraint
			 }

//All the reduction rules of the RhoXC-calculus. First, those transforming RTerms. Secondly, those transforming Constraints
	 Replace1 reductionRules = new Replace1() {
			 public ATerm apply(ATerm t) {
				 ListConstraint res;RTerm tmp;
				if (t instanceof RTerm) {// Reduction rules for terms
					System.out.println("---> je rentre avec  " + t);
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
						//Decompose struct
						appC((Phi*,match(struct(M1,M2),struct(N1,N2)),Psi*),N) ->{
							return `appC(concConstraint(Phi*,match(M1,N1),match(M2,N2),Psi*),N);
							
						}
						//Decompose F n>0
						l: appC((Phi*,co@match(app1@app[],app2@app[]),Psi*),N) ->{
							//1. on teste si le symbole de tete est une constante
							Constraint isConstant = `matchH(app1,app2);
							//head is constant? --> headIsConstant
							isConstant = headIsConstant(isConstant);
							%match(Constraint isConstant){
								matchH[] -> {
									System.out.println("no, still one matchH");
									break l;}//no ,still one matchH
							}
							//2. on calcule le resultat a retourner.
							res= `concConstraint(co);
							res = computeMatch(res);
							tmp = `appC(concConstraint(Phi*,res*,Psi*),N);
							System.out.println("Je sors de decompose");
							return tmp;
						}
						//Decompose F n = 0
						appC((Phi*,match(f@const[],f),Psi*),N) -> {
							return `appC(concConstraint(Phi*,Psi*),N);
						}
						//ToSubst
						appC((Phi*,match(X@var[],M),Xsi*),N) -> {
							return `appC(concConstraint(Phi*,Xsi*),appC(concConstraint(eq(X,M)),N));
						}
						//Idem
						appC((Phi*,m@match(X@var[],M),Tau*,match(X@var[],M),Psi*),N) -> {
							return 	`appC(concConstraint(Phi*,m,Tau*,Psi*),N);
						}
						//Id
						appC((),M) -> { 
							return `M;
						}
						//Replace
						appC((Phi*,eq(X@var[],M),Psi*),X) -> {
							return `M;
						}
						//VarElim: comme les regles s'appliquent dans l'ordre pas possible d'appliquer Replace
						appC((Phi*,eq(var[],M),Psi*),Y@var[]) -> {
							return `Y;
						}
						//ConstElim
						appC((Phi*,eq(var[],M),Psi*),c@const[]) -> {
							return `c;
						}
						//AbsShare: ALPHA-CONVERSION
						appC((Phi*,e@eq(var[],_),Psi*),abs(P,M)) -> {
							return `abs(P,appC(concConstraint(Phi*,e,Psi*),M));
						}
						//AppShare
						appC((Phi*,e@eq(var[],_),Psi*),app(M,N)) -> {
							return `app(appC(concConstraint(Phi*,e,Psi*),M),appC(concConstraint(Phi*,e,Psi*),N));
						}
						//StructShare
						appC((Phi*,e@eq(var[],_),Psi*),struct(M,N)) -> {
							return `struct(appC(concConstraint(Phi*,e,Psi*),M),appC(concConstraint(Phi*,e,Psi*),N));
						}
						//MatchShare
						appC(l1@(Phi*,eq(var[],_),Psi*),appC(l2@(Phi1*,match(P,M),Psi1*),N)) -> {
							 res = mapC(l1,l2);
							return `appC(res,appC(l1,N));
						}
						//Compose
						appC(l1@(Phi*,e@eq(var[],M),Psi*),appC(l2@(Phi1*,eq(var[],M1),Psi1*),N)) -> {
							res = mapC(l1,l2);
							System.out.println("resultat de mapC   "+ res);
							return `appC(concConstraint(l1*,res*),N);
						}


					}
				
				}
				//Particular strategies 
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
	 private ListConstraint mapC(ListConstraint l1,ListConstraint l2){
		 ListConstraint tmp;
		 %match(ListConstraint l2){
			 () -> {
				 return `concConstraint();
			 }
			 (eq(X@var[],M),Phi*) -> {
				 tmp = mapC(l1,Phi);
				 return `concConstraint(eq(X,appC(l1,M)),tmp*);
			 }
			 (match(P,M),Phi*) -> {
				 tmp = mapC(l1,Phi);
				 return `concConstraint(match(P,appC(l1,M)),tmp*);
			 }
				 _ -> {
					 System.out.println("please extend the function mapC");
					 System.exit(0);
				 return `l2;
				 }

		 }
	 }
//	 try to normalize
	 public  ATerm normalize(ATerm form, Replace1 r){
		 ATerm res = r.apply(form);
		 if (res != form) {
//			 System.out.println("|--> " + res);
			 return normalize(res,r);
		 }
		 else {
			 return res;
		 }
	 }
	 //auxilary function for the decomposition of the constructors
	 public Constraint headIsConstant(Constraint c){
		 return (Constraint)normalize(c,headisConstant);
	 }
	 
	 Replace1 headisConstant = new Replace1() {
			 public ATerm apply(ATerm t) {
				 if (t instanceof Constraint){
					 %match(Constraint t){
						 matchH(app(t1@app[],A),app(t2@app[],B)) -> {
							 return `matchH(t1,t2);
						 }
						 matchH(app(f@const[],A),app(f,B)) -> {
							 return `match(A,B);
						 }
					 }
				 }
				 return traversal.genericTraversal(t,this);
			 }
		 };
	 ListConstraint computeMatch(ListConstraint t){
		 System.out.println("je rentre, je rentre");
		 ListConstraint res,res1,res2,res3,res1b;
		 %match(ListConstraint t){
			 (X*,match(app(const[],A),app(const[],B)),Y*) -> {
				 res = (ListConstraint)computeMatch(Y);							 
				 res1 = (ListConstraint)computeMatch(X);						
				 ListConstraint tmp = `concConstraint(match(A,B));
				 return `concConstraint(res1*,match(A,B),res*);
			 }
			 (X*,match(app(A1,A2),app(B1,B2)),Y*) -> {
				 res1 = (ListConstraint)computeMatch(Y);
				 res1b = (ListConstraint)computeMatch(X);
				 res = `concConstraint(match(A1,B1));
				 res2 = (ListConstraint)computeMatch(res);
				 res = `concConstraint(match(A2,B2));
				 res3 = (ListConstraint)computeMatch(res);
				 return `concConstraint(res1b*,res2*,res3*,res1*);
			 }
			 _ -> {//System.out.println("Please extend computeMatch!");
				 //System.exit(0);
			 return t;
			 }
		 }
	 }
	 public void run(){
		 
		 RTerm resu;
		 String s;
		 System.out.println(" ******************************************************************\n RomCal: an implementation of the explicit rho-calculus in Tom\n by Germain Faure and ...\n version 0.1 \n ******************************************************************");
//		 Constraint c = `and(dk(),concConstraint(and(dk,concConstraint(match(var("X"),app(abs(var("X"),var("X")),const("a")))))));
		 //	 System.out.println(normalize(c,reductionRules));

		 while(true){
			 System.out.print("RomCal>");
			 s = Clavier.lireLigne();
			 resu = factory.RTermFromString(s);
			 System.out.println(" " + (resu));
			 System.out.println((RTerm)normalize(resu,reductionRules));
		 }

	 }
	 public  String test(String s){
		 
		 RTerm resu;
		 resu = factory.RTermFromString(s);
		 return ((RTerm)normalize(resu,reductionRules)).toString();


	 }
	 public final static void main(String[] args) {
		 Rho rhoEngine = new Rho(rhotermFactory.getInstance(new PureFactory(16)));
		  rhoEngine.run();
	 }
 }
