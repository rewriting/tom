  /*
  * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
  */


package matching;

import matching.lamterm.types.*;

import tom.library.sl.Strategy;
import tom.library.sl.Position;
import tom.library.sl.VisitFailure;

import java.io.*;
import java.util.*;

public class Matching {
	
	private int comptVariable = 0;

	%include { sl.tom }
	%include { lamterm/Lamterm.tom }
  %include { util/types/Collection.tom }

	public final static void main(String[] args) {
		Matching matchingEngine = new Matching();
		matchingEngine.run();
	}
	
	public void run(){
	System.out.println(" ******************************************************************\n Computing matching modulo superdevelopments. \n constants begin with a,b,c...w\n local variable begin with x,y or z\n matching variables are in capital letters\n  ******************************************************************\n");
    LamcalLexer lexer = new LamcalLexer(System.in); // Create parser attached to lexer
    LamcalParser parser = new LamcalParser(lexer);
//		Strategy reduce = new ReductionRules();
//		Strategy strategyNormalize =`Repeat(reduce);
	
		while(true){
//			Collection c=new HashSet();
			System.out.print("mSD>");
      try {
				Equation subject = parser.matchingEquation();
//				System.out.println("Resultat du parsing: " + subject);
				Systems s=`and(subject);
				System.out.println(// "Resultat de la normalisation"+
													  prettyPrinter(normalize_Systems(s)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	public String prettyPrinter(Collection c) {
		Iterator it=c.iterator();
		String result="";
		while(it.hasNext()) {
			Systems s=(Systems)it.next();
			result+="[";		
			result+=prettyPrinter(s);
			result+="]\n";
		}
		return result+"size of the bag: "+c.size();
	}
	public String prettyPrinter(Systems s) {
		String result="";

		%match(Systems s) {
			and(match(a,b),Y*)->{
				result+=prettyPrinter(`a)+":="+prettyPrinter(`b)+",";
				result+=`prettyPrinter(Y);
			}
		}
		return result;
	}

	public String prettyPrinter(LamTerm t){
		%match(t) {
			app(term1,term2) -> {return "("+prettyPrinter(`term1)+"."+prettyPrinter(`term2)+")";}
			abs(term1,term2) -> {return "("+prettyPrinter(`term1)+"->"+prettyPrinter(`term2)+")";}
			matchVar(s) -> {return `s;}
			localVar(s) -> {return `s;}
			constant(s) -> {return `s;}
		}
    return "";
	}
	public Collection reduce(Systems s) throws VisitFailure { 
		Collection c = new HashSet();
		%match(Systems s) {
			//Trivial
			and(X*,match(a@constant[],a),Y*) -> {
				c.add(`and(X*,Y*));}
			and(X*,match(x@localVar[],x),Y*) -> {
				c.add(`and(X*,Y*));}
			//Abs
			and(X*,match(abs(x@localVar[],A),abs(x@localVar[],B)),Y*) -> {
				c.add(`and(X*,match(A,B),Y*));}
			//Subst
			l:and(X*,match(Z@matchVar[],A),Y*) -> {
//				System.out.println("subst with X: "+X+"and Y: "+Y);
				boolean b1=belongsTo(`Z,`and(X*,Y*));
//				System.out.println("b1="+b1);
//				System.out.println("subst1");
				boolean b2=doesNotContainFreeLocalVar(`A);
				//			System.out.println("b2="+b2);
				//			System.out.println("subst2");
				if (b1 && b2){
//								System.out.println("subst in if");
								
					Systems s1=`substitute(Z,A,X*);
					//			System.out.println("subst in if1: Z: "+Z+"by A: "+A +"in: "+Y);
					Systems s2=`substitute(Z,A,Y*);
//					System.out.println("subst in if2"+2);
					c.add(`and(s1*,match(Z,A),s2*));
				} else {
					//			System.out.println("bye.bye.subst");
					break l;
				}
			}
			//Decompose
			and(X*,match(app(A1,B1),app(A2,B2)),Y*) -> {
				c.add(`and(X*,match(A1,A2),match(B1,B2),Y*));}
			//Proj
			and(X*,match(app(A1,B1),A2),Y*) -> {
				//			System.out.println("Proj");
				c.add(`and(X*,match(A1,abs(localVar("_x"+(++comptVariable)),A2)),Y*));
			}
			//Beta-exp
				and(X*,match(app(A1,B1),C),Y*) -> {
//				System.out.println("Beta-exp");
					//1. Collection of all the subterms of C
					Collection collSubTerm=getAllSubterm(`C);
					//2. For each subterm compute the set of position in which the considered subterm appears.
					Iterator itSubTerm=collSubTerm.iterator();
					while(itSubTerm.hasNext()) {
//								System.out.println("ici1");
						LamTerm B2=(LamTerm)itSubTerm.next();
						//							System.out.println("la2");
						Collection collPosition=getAllPos(`C,B2);
						List l=allSubCollection(collPosition);
						Iterator itListAllSubCollection=l.iterator();
						//3. For each subset of the previous set do the replacement of the subterm by a fresh variable and then do decomposition.
						while(itListAllSubCollection.hasNext()) {
							LamTerm x=`localVar("_x"+(++comptVariable));
							LamTerm A2=`C;
//							System.out.println("ici3");
							Collection subCollection=(Collection)itListAllSubCollection.next();
							Iterator itSubCollection=subCollection.iterator();
							while(itSubCollection.hasNext()){
								//							System.out.println("ici4");
								Strategy subsitute=(Strategy)((Position)itSubCollection.next()).getReplace(x);
//								System.out.println("ici4");
                                A2=(LamTerm)subsitute.visit(A2);
								//							System.out.println("ici5");
							}
							c.add(`and(X*,match(A1,abs(x,A2)),match(B1,B2),Y*));		
						}
					}

				}
		}
		if (c.isEmpty()){
			c.add(s);
		}
		return c;
	}
	
	public boolean testSolvedForm(Systems s){
		%match(Systems s){
			and(X*,match(matchVar[],A),Y*)->{
				boolean b1=`testSolvedForm(X*);
				boolean b2=`testSolvedForm(Y*);
				return b1&&doesNotContainFreeLocalVar(`A)&&b2;
			}
			and(X*,match[],Y*)->{
				return false;
			}
		}
		return true;
	}
	public Collection eraseUnsolvedForm(Systems s){
		Collection result=new HashSet();
		if(testSolvedForm(s)){
			result.add(s);
		}
		return result;
	}
	public boolean belongsTo(LamTerm var,Systems s){
		%match(Systems s){
			and() -> {return false;}
			and(X*,match(A,B),Y*)->{
				boolean resultX=`belongsTo(var,X*);
				boolean resultY=`belongsTo(var,Y*);
				return resultX ||belongsTo(var,`A)||resultY;
			}
		}
		return false;
	}
	public boolean belongsTo(LamTerm var,LamTerm term){
		%match(term) {
			localVar[] -> {return false;}
			constant[] -> {return false;}
			X@matchVar[] -> {
				if (`X == var){
					return true;
				}
				else{
					return false;
				}
			}
			abs(_,A) -> {return belongsTo(var,`A);}
			app(A,B) -> {return belongsTo(var,`A)||belongsTo(var,`B);}
		}
		return false;
	}
	public boolean doesNotContainFreeLocalVar(LamTerm t){
		return freeLocalVar(t).isEmpty();
	}
	public Collection freeLocalVar(LamTerm t){
		Collection c=new HashSet();
		freeLocalVarAux(t,c);
		return c;
	}
	public Collection freeLocalVar(Systems s){
		Collection c=new HashSet();
		freeLocalVarAux(s,c);
		return c;
	}
	public Collection freeLocalVarAux(Systems s,Collection c){
		%match(Systems s){
			and(X*,match(A,B),Y*) -> {
				freeLocalVarAux(`X,c);
				freeLocalVarAux(`Y,c);
				freeLocalVarAux(`A,c);
			}
		}
		return c;
	}
	public void freeLocalVarAux(LamTerm t,Collection c){
		%match(LamTerm t){
			x@localVar[] -> {
				if (!c.contains(t)) {
					c.add(`x);
				}
			}
			app(A1,A2)->{
				freeLocalVarAux(`A1,c);
				freeLocalVarAux(`A2,c);
			}
			abs(x,A1)->{
				freeLocalVarAux(`A1,c);
				c.remove(`x);
			}
		}
	}
	public Systems substitute(LamTerm var, LamTerm subject, Systems s){
		%match(Systems s){
			and(X*,match(A,B),Y*) -> {
//				System.out.println("premiere subst");
				Systems newX=`substitute(var,subject,X*);
//				System.out.println("deuxieme subst");
				Systems newY=`substitute(var,subject,Y*);
				//			System.out.println("troisieme subst"+var+subject+`A);
				LamTerm newA=substitute(var,subject,`A);
//				System.out.println("construction du resultat");
				return `and(newX*,match(newA,B),newY*);}
			_ -> {return `s;}
		}
    return `s;
	}
	//[X\subject]t
	public LamTerm substitute(LamTerm X, LamTerm subject, LamTerm t){
//		System.out.println("here");
		%match(LamTerm t){
			constant[] -> {return `t;}
			localVar[] -> {return `t;}
			matchVar(name) -> {
				%match(LamTerm X){
					matchVar(nameSubject)-> {
						if (`name.equals(`nameSubject)){
							return `subject;
						}
					}

				}
				return `t;
			}
			app(A1,A2) -> {return `app(substitute(X,subject,A1),substitute(X,subject,A2));}
			abs(x,A1) -> {
				LamTerm newx=`localVar("_x"+(++comptVariable));
				return `abs(newx,substitute(X,subject,(substituteLocalVar(x,newx,A1))));
			}
		}
		return `subject;
	}
	//[X\subject]t
	public LamTerm substituteLocalVar(LamTerm X, LamTerm subject, LamTerm t){
		%match(LamTerm t){
			constant[] -> {return `t;}
			matchVar[] -> {return `t;}
			Y@localVar[] -> {
				if (X == `Y){ return `subject;}
				else {return `t;}
			}
			app(A1,A2) -> {return `app(substitute(X,subject,A1),substitute(X,subject,A2));}
			abs(x,A1) -> {LamTerm newx=`localVar("_x"+(++comptVariable));
			return `abs(newx,substituteLocalVar(X,subject,(substituteLocalVar(x,newx,A1))));
			}
		}
		return `subject;
	}
	public Collection normalize_Systems(Systems s) throws  VisitFailure{
		Collection singletonS=new HashSet();singletonS.add(s);
		Collection oneStep=reduce(s);
		if (oneStep.equals(singletonS)){//if I do not reduce somethg
			return eraseUnsolvedForm(s);
		}
		else{
			Collection result=new HashSet();
			Iterator it=oneStep.iterator();
			while (it.hasNext()){
				Systems next=(Systems)it.next();
				result.addAll(normalize_Systems(next));
			}
			return result;//it must be the union of clean systems
		}
	}
// 	public Collection normalize_Collection(Collection start)  throws VisitFailure{
// //			System.out.println("etape1");
// 		Collection result = visit_Collection(start);
// //			System.out.println("etape2");
// 		if(!result.equals(start)){
// //			System.out.println("etape3");
// 			result=normalize_Collection(result);
// //			System.out.println("etape4");
// 		}
// 		//		System.out.println("etape5");
// 		return eraseUnsolvedForm(result);
// 	}
// 	public Collection visit_Collection(Collection start) throws VisitFailure{
// 			Collection c=new HashSet();
// 			Iterator it=start.iterator();
// //			System.out.println("1");
// 			while(it.hasNext()){
// 				//			System.out.println("2");
// 				Systems s=(Systems)it.next();
// //				if (!s.isEmpty()){
// //				System.out.println("3");
// 				//			System.out.println(s);
// 				Collection tmp=reduce(s);
// //				System.out.println("4");
// 				c.addAll(tmp);
// //				c.addAll(reduce(s));
// 				//			System.out.println("5");
// 			}
// 			return c;
// 		}

	//return a list of collection consisting of all the subCollection of a non-empty collection
	public List allSubCollection(Collection c){
		List result=new ArrayList();
		if (c.size() == 1){
			result.add(c);
			return result;
		}
		else{ 
			Iterator c_it=c.iterator();
			Object e=c_it.next();
			Collection h =new HashSet();
			h.add(e);
			result.add(new HashSet(h));
			c.remove(e);
			List l=allSubCollection(c);
			Iterator it=l.iterator();
			while(it.hasNext()){
				Collection s1=(Collection)it.next();
				result.add(new HashSet(s1));
				s1.add(e);
				result.add(new HashSet(s1));
			}
			return result;
		}
	}
	//return the collection of all substerm (strict or not) of subject
	public Collection getAllSubterm(LamTerm subject) throws VisitFailure {
    Collection bagSubterm = new HashSet();
		Strategy collect = `Collect(bagSubterm);
 		Strategy getAll = `mu(MuVar("x"),
 																	Sequence(collect,Try(All(MuVar("x")))));		
		getAll.visit(subject);
		return bagSubterm;
	}
	//return all the position of subject in which subTerm occurs
	public Collection getAllPos(LamTerm subject, LamTerm subTerm) throws VisitFailure {
    ArrayList posList = new ArrayList();
 		Strategy getPos=`GetPos(posList,subTerm);
 		Strategy getAllPos=`mu(MuVar("x"),
 																	Sequence(getPos,Try(All(MuVar("x")))));	 	
 		getAllPos.visit(subject);
		return posList;
	}
  %strategy Collect(bagSubTerm:Collection) extends Fail() {
		visit LamTerm {
      x -> {
        bagSubTerm.add(`x);
        return `x;
      }
    }
  }
  %strategy GetPos(bagPosition:Collection,lookingFor:LamTerm) extends Fail() {
		visit LamTerm { 
      x -> {
        if (`x == lookingFor) {
          bagPosition.add(getPosition());
        }
      }
    }
  }
}
