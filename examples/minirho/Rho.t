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

	%vas{
		 module rhoterm
     imports 
		 public
			 sorts RTerm Constraint
		 abstract syntax
//TERMS
			 var(na:String) -> RTerm
			 const(na:String) -> RTerm
			 abs(lhs:RTerm,rhs:RTerm) -> RTerm
			 app(lhs:RTerm,rhs:RTerm) -> RTerm
			 struct(lhs:RTerm,rhs:RTerm) -> RTerm //structure theorie vide
			 appC(co:Constraint,term:RTerm) -> RTerm
			 appSt(co:Constraint,term:RTerm) -> RTerm //Substition application on terms
//CONSTRAINTS
			 appSc(subst:Constraint,co:Constraint) -> Constraint //Substition application on constraints
			 match(pa:RTerm,rhs:RTerm) -> Constraint

			 dk -> Label //label by defaut for conjonctions
			 g -> Label //label for solvable constraints
			 ng -> Label //label for not solvable constraints
			 and(lab:Label, constrList:ListConstraint ) -> Constraint
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

						/* Garbage collector */
						appC(matchKO[],_) -> {return `const("null");}
						app(const("null"),A) -> {return `const("null");}
						struct(A,const("null")) -> {return `A;}
						struct(const("null"),A) -> {return `A;} 
						/* */
							
						//\{id\}A = A
						appSt(and(_,()),A) -> {return `A;}
 						//Compose
							appSt(phi,appSt(psi,A)) -> {
										return `appSt(and(g,concConstraint(phi,appSc(phi,psi))),A);
							}
						//rho 
						app(abs(A,B),C)-> {	
						    return `appC(match(A,C),B) ;
						}		
						//delta
						app(struct(A,B),C) -> { 
							return `struct(app(A,C),app(B,C));
						}
						//ToSubstVar
						appC(match(X@var[],A),B) -> {
						    return `appSt(match(X,A),B);
						}
						//ToSubstAnd
						appC(and(g(),C),A) -> {
						    return `appSt(and(g,C),A);
						}
						//Id
						appC(match(a@const[],a),A) -> {
						    return `A;
						}
						//Replace
						appSt(match(X@var[],A),X) -> {
						    return `A;
						}
						appSt(and(g(),(_*,match(X@var[],A),_*)),X) -> {
						    return `A;
						} 
						//EliminateVar
						appSt(match[pa=var(x)],Y@var(y)) -> { 
						    if (`x !=  `y) { 
							return `Y;
						    }
						}
						appSt(and(g(),(C*,match[pa=var(x)],D*)),Y@var(y)) -> {
						    if (`x != `y) {
									return `appSt(and(g,concConstraint(C*,D*)),Y);
						    }
						} 
						//EliminateConst
						appSt[term=f@const[]] -> {
						    return `f;
						}
						//ShareApp
						appSt(phi,app(B,C)) -> {
						    return `app(appSt(phi,B),appSt(phi,C));
						}
						//ShareStruct
						appSt(phi,struct(A,B)) -> {
						    return `struct(appSt(phi,A),appSt(phi,B));
						}
						//ShareAbs
						appSt(phi,abs(A,B)) -> {
						    return `abs(A,appSt(phi,B));
						}
						//ShareAppC
						appSt(phi,appC(C,A)) -> {
						    return `appC(appSc(phi,C),appSt(phi,A));
						}

					}
				}
				else if(t instanceof Constraint) {//Reduction rules for constraints
					Constraint co = (Constraint)t;
					%match(Constraint co){

						//ComposeS
						appSc(phi,appSc(psi,theta)) -> {
								return `appSc(and(g,concConstraint(phi,appSc(phi,psi))),theta);
							}
						//decompose Structure
						match(struct(A,B),struct(C,D)) -> {
						    return  `and(dk,concConstraint(match(A,C),match(B,D)));
						}
						//decompose Constructors 
// on groupe les regles Decompose et NGood
						l:s@match(app1@app[],app2@app[]) -> {
							//On peut peut-etre optimiser ici: construire la liste de filtrage en meme tps que l'on teste: c pas sur qu'il faille optimiser ne rien creer quand on teste le symbole est peut etre pas mal

							//1. on teste si le symbole de tete est une constante
							//head is constant? --> headIsConstant
							Constraint isConstant = headIsConstant(s);
							%match(Constraint isConstant){
								match[] -> {
									break l;
								}
								matchKO[] -> {
//									System.out.println("J'ai trouve un match KO dans la decompose");
									return `matchKO(app1,app2);
								}
							}
							//2. on calcule le resultat a retourner.
							return normalize(co,computeMatch);
						}
						l:s@match(app1@app[],app2@const[]) -> {
							//On peut peut-etre optimiser ici: construire la liste de filtrage en meme tps que l'on teste: c pas sur qu'il faille optimiser ne rien creer quand on teste le symbole est peut etre pas mal

							//1. on teste si le symbole de tete est une constante
							//head is constant? --> headIsConstant
							Constraint isConstant = headIsConstant(s);
							%match(Constraint isConstant){
								match[] -> {
									break l;
								}
								matchKO[] -> {
//									System.out.println("J'ai trouve un match KO dans la decompose");
									return `matchKO(app1,app2);
								}
							}
							//2. on calcule le resultat a retourner.
							return normalize(co,computeMatch);
						}
						l:s@match(app1@const[],app2@app[]) -> {
							//On peut peut-etre optimiser ici: construire la liste de filtrage en meme tps que l'on teste: c pas sur qu'il faille optimiser ne rien creer quand on teste le symbole est peut etre pas mal

							//1. on teste si le symbole de tete est une constante
							//head is constant? --> headIsConstant
							Constraint isConstant = headIsConstant(s);
							%match(Constraint isConstant){
								match[] -> {
									break l;
								}
								matchKO[] -> {
//									System.out.println("J'ai trouve un match KO dans la decompose");
									return `matchKO(app1,app2);
								}
							}
							//2. on calcule le resultat a retourner.
							return normalize(co,computeMatch);
						}
						//simplify
						and(dk(),(X*,match(a@const[],a),Y*)) -> {
						    return `and(dk(),concConstraint(X*,Y*));
						}
						and(ng(),(X*,match(a@const[],a),Y*)) -> {
						    return `and(ng(),concConstraint(X*,Y*));
						}
						//We suppose the linearity of patterns
						and(dk(),(X*,c@match[pa=var[]],Y*,d@match[pa=var[]],Z*)) -> {
						    return `and(dk,concConstraint(X*,and(g,concConstraint(c,d)),Y*,Z*));
						} 
						//the following rule is duplicated because we have to take care of commutativity
						and(dk(),(X*,m@match[pa=var[]],Y*,and(g(),(c*)),Z*)) -> {
						    return `and(dk,concConstraint(X*,and(g,concConstraint(m,c*)),Y*,Z*));
						}
						and(dk(),(X*,and(g(),(c*)),Y*,m@match[pa=var[]],Z*)) -> {
						    return `and(dk,concConstraint(X*,and(g,concConstraint(m,c*)),Y*,Z*));
						}
						and(dk(),(X*,and(g(),(c*)),Y*,and(g(),(d*)),Z*)) -> {
						    return `and(dk,concConstraint(X*,and(g,concConstraint(c*,d*))));
						}
						//ShareMatch A Enrichir
						appSc(phi,match(B,C)) -> {
						    return `match(B,appSt(phi,C));
						}
						//ShareAnd for all types of "and"
						appSc(phi,and(x,C)) -> {

							Replace2 r = new Replace2(){
									public ATerm apply(ATerm t,Object o){
										Constraint c = (Constraint)t;
										%match(Constraint c){
											y -> {return `appSc((Constraint)o,y);}
										}
									}
								};
							return `and(x,(ListConstraint)traversal.genericTraversal(C,r,phi));
						}
						//two rules for dealing with the ands
						and(l,(X*,and(l,(C*)),Y*)) -> {
						    return `and(l,concConstraint(X*,C*,Y*));
						}
						and(l,(c)) -> {
						    return `c;
						}
					}
				}
				//Particular strategies 
 				if (t instanceof RTerm){
 					RTerm term = (RTerm) t;
 					%match(RTerm term){
 						//do not reduce the right-hand side of an abstraction
 						abs(A,B) -> {
 						    return `abs((RTerm)apply(A),B);
 						}
						appC(matchKO[],_) ->{
							return t;}
					}
				}			 				
				return traversal.genericOneStep(t,this);
				//				return traversal.genericTraversal(t,this);
			 }
	     };
	 
//try to normalize
	 public  ATerm normalize(ATerm form, Replace1 r){
		 ATerm res = null;
		 int nb_it = 0;
		 String s;
		 if ( r  == reductionRules) {//juste pour l'affichage
			 while(res != form){
//				 s = Clavier.lireLigne();
				 res = form;
				 form = r.apply(form);
				 nb_it++;
//			 			 System.out.println("|--> " +nb_it + printNoStuck(form));
				 //			 		 			 System.out.println("|--> " + form);
			 }
//			 		 System.out.println("|--> " + form);
//			 System.out.println("|--> " + printInfix(form));
//			 System.out.println("|--> " + printNoStuck(form));
			 System.out.println("Nb de reductions: " + (nb_it - 1));
			 System.out.println("The result is: " + printNoStuck(form));
			 System.out.println("with the ATerm syntax: " + form);

		 }
		 else {
			 while(res != form){
				 res = form;
				 form = r.apply(form);
			 }
		 }
		 return res;
	 }


// //try to normalize
// 	 public  ATerm normalize(ATerm form, Replace1 r){
// 		 ATerm res = r.apply(form);
// 		 	 if (res != form) {
// //			 System.out.println("|--> " + printInfix(res));
// //			 System.out.println("|--> " + res);
// 			 return normalize(res,r);
// 		 }
// 		 else {
// 			 return res;
// 		 }
// 	 }

	 public Rho(rhotermFactory factory) {
		 this.factory = factory;
		 this.traversal = new GenericTraversal();
	 }
  public rhotermFactory getRhotermFactory() {
    return factory;
  }
	 Replace1 headisConstant = new Replace1() {
			 public ATerm apply(ATerm t) {
				 if (t instanceof Constraint){
					 %match(Constraint t){
						 match(app(t1@app[],A),app(t2@app[],B)) -> {
							 return `match(t1,t2);
						 }
						 match(app(f@const[],A),app(f,B)) -> {
							 return `matchOK(A,B);
						 }
						 match(app(const[na=n1],A),app(const[na=n2],B)) -> {
							 if (n1 != n2){ 
								 return `matchKO(A,B);
							 }
						 }
						 //INCOMPLEEEEEEEEEEEEEEEEEEEEEET
						 match(A@const[],B) -> {
							 return `matchKO(A,B);
						 }

						 match(A,B@const[]) -> {
//							 System.out.println("J'ai trouve un matchKO dans headISConst");
							 return `matchKO(A,B);
						 }
						 _ -> {return t;}
					 }
				 }
				 return traversal.genericTraversal(t,this);
			 }
		 };
	 public Constraint headIsConstant(Constraint c){
		 return (Constraint)normalize(c,headisConstant);
	 }
	 Replace1  computeMatch = new Replace1(){
			 public ATerm apply(ATerm t){
				 if (t instanceof Constraint){
					 %match(Constraint t){
						 match(app(const[],A),app(const[],B)) -> {
							 return `match(A,B);
						 }
						 match(app(A1,A2),app(B1,B2)) -> {
							 return `and(dk(),concConstraint(match(A1,B1),match(A2,B2)));
						 }
					 }
				 }
				 return traversal.genericTraversal(t,this);
			 }};

	 //auxilary function for the decomposition of the constructors

	 public void run(){

		 RTerm resu;
		 String s;
		 System.out.println(" ******************************************************************\n RomCal: an implementation of the explicit rho-calculus in Tom\n by Germain Faure and ...\n version 0.1 under development not stable. Please use it with care \n ******************************************************************");
		 while(true){
			 System.out.print("RomCal>");
			 s = Clavier.lireLigne();
			 resu = factory.RTermFromString(s);
			 System.out.println(printNoStuck(resu));
			 resu = (RTerm)normalize(resu,reductionRules);
		 }

	 }
	 	 public String test(String s){
		 RTerm resu =  factory.RTermFromString(s);
		 return ((RTerm)normalize(resu,reductionRules)).toString();
	 }

    
	 public final static void main(String[] args) {
		 Rho rhoEngine = new Rho(rhotermFactory.getInstance(new PureFactory(16)));
		 rhoEngine.run();
	 }



//PRINT FUNCTIONS
	 public String printInfix(ATerm t){
		 if (t instanceof RTerm){
			 return printInfixTerm((RTerm)t);
		 }
		 else  return printInfixCons((Constraint)t);
	 }
	 public String printInfixTerm(RTerm r){
		 %match(RTerm r) {
			 var(s) -> {return s.toUpperCase();}
			 const(s) -> {return s.toLowerCase();}
			 abs(lhs,rhs) -> {return printInfix(lhs) + " -> "+ printInfix(rhs);}
			 app(lhs,rhs) -> {return "(" +  printInfix(lhs) + " )  "+ printInfix(rhs) ;}
			 struct(lhs,rhs) -> {return printInfix(lhs) + " , "+ printInfix(rhs);}
			 appC(co, term) -> {return "[" + printInfixCons(co) + "]" + "(" + printInfix(term) + ")";}
			 appSt(co, term) -> {return "{" + printInfixCons(co) + "}" + "(" + printInfix(term) + ")";}
			 _ -> {return "please extend printInfix";}
		 }
	 } 
	 public  String printInfixCons(Constraint c){
		 %match(Constraint c){
			 appSc(subst, co) -> {return "{" + printInfixCons(subst) + "}" + "(" + printInfixCons(co) + ")";}
			 match(pa,rhs) -> {return printInfix(pa) + " << " + printInfix(rhs);}
			 matchKO(pa,rhs) -> {return printInfix(pa) + " <<KO " + printInfix(rhs);}
 			 and(g(),l) -> {
 				 //code mauvais
 				 ListConstraint tmp = l;
 				 String s = "";
 				 while(!(tmp.isEmpty())){
 					 s = s + printInfixCons((Constraint)tmp.getFirst()) + " ^g ";
 					 tmp = (ListConstraint)tmp.getNext();
 				 }
 				 return s.substring(0,s.length() - 4);
 			 }
 			 and(ng(),l) -> {
				 //code mauvais
 				 ListConstraint tmp = l;
 				 String s = "";
 				 while(!(tmp.isEmpty())){
 					 s = s + printInfixCons((Constraint)tmp.getFirst()) + " ^ng ";
 					 tmp = (ListConstraint)tmp.getNext();
 				 }
 				 return s.substring(0,s.length() - 5);
 			 }
			 and(dk(),l) -> {
 				 //code mauvais
 				 ListConstraint tmp = l;
 				 String s = "";
 				 while(!(tmp.isEmpty())){
 					 s = s + printInfixCons((Constraint)tmp.getFirst()) + " ^ ";
 					 tmp = (ListConstraint)tmp.getNext();
 				 }
 				 return s.substring(0,s.length() - 3);
 			 }
		 _ -> {return "";}
		 }
	 }
	 public String printNoStuck(ATerm t){
		 if (t instanceof RTerm){
			 return printNoStuckTerm((RTerm)t);
		 }
		 else  return printNoStuckCons((Constraint)t);
	 }
	 public String printNoStuckTerm(RTerm r){
		 %match(RTerm r) {
			 var(s) -> {return s.toUpperCase();}
			 const(s) -> {return s.toLowerCase();}
			 abs(lhs,rhs) -> {return printNoStuck(lhs) + " -> "+ printNoStuck(rhs);}
			 app(lhs,rhs) -> {return  printNoStuck(lhs) + " (  "+ printNoStuck(rhs) + " )";}
			 struct(lhs,rhs) -> {return printNoStuck(lhs) + " , "+ printNoStuck(rhs);}
//A COMPLETER !!!!!!!!!!!!!!!!
			 appC(matchKO[], term)  -> {
				 return "[ << NG ]";// + "(" + printNoStuck(term) + ")";
				 
			 }
			 appC(co, term)  -> {
					 return "[" + printNoStuckCons(co) + "]" + "(" + printNoStuck(term) + ")";
				 
			 }
			 appSt(co, term) -> {return "{" + printNoStuckCons(co) + "}" + "(" + printNoStuck(term) + ")";}
			 _ -> {return "Extebd please!";}
		 }
	 } 
	 public  String printNoStuckCons(Constraint c){
		 %match(Constraint c){
			 appSc(subst, co) -> {return "{" + printNoStuckCons(subst) + "}" + "(" + printNoStuckCons(co) + ")";}
			 match(pa,rhs) -> {return printNoStuck(pa) + " << " + printNoStuck(rhs);}
 			 and(g(),l) -> {
 				 //code mauvais
 				 ListConstraint tmp = l;
 				 String s = "";
 				 while(!(tmp.isEmpty())){
 					 s = s + printNoStuckCons((Constraint)tmp.getFirst()) + " ^g ";
 					 tmp = (ListConstraint)tmp.getNext();
 				 }
 				 return s.substring(0,s.length());// - 4);
 			 }
 			 and(ng(),l) -> {
				 //code mauvais
 				 ListConstraint tmp = l;
 				 String s = "";
 				 while(!(tmp.isEmpty())){
 					 s = s + printNoStuckCons((Constraint)tmp.getFirst()) + " ^ng ";
 					 tmp = (ListConstraint)tmp.getNext();
 				 }
 				 return s.substring(0,s.length());// - 5);
 			 }
			 and(dk(),l) -> {
 				 //code mauvais
 				 ListConstraint tmp = l;
 				 String s = "";
 				 while(!(tmp.isEmpty())){
 					 s = s + printNoStuckCons((Constraint)tmp.getFirst()) + " ^ ";
 					 tmp = (ListConstraint)tmp.getNext();
 				 }
 				 return s.substring(0,s.length());// - 3);
 			 }
		 _ -> {return "Please extend printNoStuck";}
		 }
	 }

 }
		 
 
