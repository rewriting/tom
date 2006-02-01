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


package matching;

import aterm.*;
import aterm.pure.*;
import matching.lamterm.*;
import matching.lamterm.types.*;

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
import java.util.*;

public class Matching {
	
	private int comptVariable = 0;
	private LamtermFactory factory;
	
	public Matching(LamtermFactory factory) {
		this.factory = factory;
	}
	public LamtermFactory getLamtermFactory() {
		return factory;
	}
	
	
	%include { mutraveler.tom }
	%include { lamterm/Lamterm.tom }

	public final static void main(String[] args) {
		Matching matchingEngine = new Matching(LamtermFactory.getInstance(SingletonFactory.getInstance()));
		matchingEngine.run();
	}
	

	public void run(){
	System.out.println(" ******************************************************************\n Computing matching modulo superdevelopments. \n ******************************************************************\n");
    LamcalLexer lexer = new LamcalLexer(System.in); // Create parser attached to lexer
    LamcalParser parser = new LamcalParser(lexer);
//		VisitableVisitor reduce = new ReductionRules();
//		VisitableVisitor strategyNormalize =MuTraveler.init(`Repeat(reduce));
	
		while(true){
			Collection c=new HashSet();
			System.out.print("mSD>");
      try {
				Equation subject = parser.matchingEquation();
//				System.out.println("Resultat du parsing: " + subject);
				c.add(`and(subject));
				System.out.println(// "Resultat de la normalisation"+
													  normalize_Collection(c));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}

	public Collection reduce(Systems s) throws  VisitFailure { 
		Collection c = new HashSet();
		%match(Systems s){
			//Trivial
			(X*,match(a@const[],a),Y*)-> {
				c.add(`and(X*,Y*));}
			(X*,match(x@localVar[],x),Y*)-> {
				c.add(`and(X*,Y*));}
			//Abs
			(X*,match(abs(x@localVar[],A),abs(x@localVar[],B)),Y*) ->{
				c.add(`and(X*,match(A,B),Y*));}
			//Subst
			l:(X*,match(Z@matchVar[],A),Y*) ->{
				if (!belongsTo(Z,`and(X*,Y*)) && doesNotContainFreeLocalVar(A)){
					Systems s1=`substitute(Z,A,X*);
					Systems s2=`substitute(Z,A,Y*);
					c.add(`and(s1*,match(Z,A),s2*));
				}
				else{
					break l;
				}
			}
			//Decompose
			(X*,match(app(A1,B1),app(A2,B2)),Y*) ->{
				c.add(`and(X*,match(A1,A2),match(B1,B2),Y*));}
			//Proj
			(X*,match(app(A1,B1),A2),Y*) ->{
				c.add(`and(X*,match(A1,abs(localVar("_x"+(++comptVariable)),A2)),Y*));
			}
			//Beta-exp
				(X*,match(app(A1,B1),C),Y*) ->{
					//1. Collection of all the subterms of C
					Collection collSubTerm=getAllSubterm(C);
					//2. For each subterm compute the set of position in which the considered subterm appears.
					Iterator itSubTerm=collSubTerm.iterator();
					while(itSubTerm.hasNext()){
//								System.out.println("ici");
						LamTerm B2=(LamTerm)itSubTerm.next();
								System.out.println("la");
						Collection collPosition=getAllPos(C,B2);
						List l=allSubCollection(collPosition);
						Iterator itListAllSubCollection=l.iterator();
						//3. For each subset of the previous set do the replacement of the subterm by a fresh variable and then do decomposition.
						while(itListAllSubCollection.hasNext()){
							LamTerm x=`localVar("_x"+(++comptVariable));
							LamTerm A2=C;
							//							System.out.println("ici");
							Collection subCollection=(Collection)itListAllSubCollection.next();
							Iterator itSubCollection=subCollection.iterator();
							while(itSubCollection.hasNext()){
//								System.out.println("ici");
								VisitableVisitor subsitute=((Position)itSubCollection.next()).getReplace(x);
								//							System.out.println("ici");
								A2=(LamTerm)MuTraveler.init(subsitute).visit(A2);
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
	public boolean belongsTo(LamTerm var,Systems s){
		return freeLocalVar(s).contains(var);
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
			(X*,match(A,B),Y*) -> {
				freeLocalVarAux(X,c);
				freeLocalVarAux(Y,c);
				freeLocalVarAux(A,c);
			}
		}
		return c;
	}
	public void freeLocalVarAux(LamTerm t,Collection c){
		%match(LamTerm t){
			x@localVar[] -> {
				if (!c.contains(t)) {
					c.add(x);
				}
			}
			app(A1,A2)->{
				freeLocalVarAux(A1,c);
				freeLocalVarAux(A2,c);
			}
			abs(x,A1)->{
				freeLocalVarAux(A1,c);
				c.remove(x);
			}
		}
	}
	public Systems substitute(LamTerm var, LamTerm subject, Systems s){
		%match(Systems s){
			(X*,match(A,B),Y*) -> {
				Systems newX=`substitute(var,subject,X*);
				Systems newY=`substitute(var,subject,Y*);
				LamTerm newA=`substitute(var,subject,A);
				return `and(newX*,match(newA,B),newY*);}
			_ -> {return `s;}

		}
	}
	public LamTerm substitute(LamTerm X, LamTerm subject, LamTerm t){
		%match(LamTerm subject){
			const[] -> {return `subject;}
			localVar[] -> {return `subject;}
			Y@matchVar[] -> {
				if (X == Y){ return `t;}
				else {return `subject;}
			}
			app(A1,A2) -> {return `app(substitute(X,subject,A1),substitute(X,subject,A2));}
			abs(x,A1) -> {
				LamTerm newx=`localVar("_x"+(++comptVariable));
				return `abs(newx,substitute(X,subject,(substituteLocalVar(x,newx,A1))));
			}
		}
		return `subject;
	}
	public LamTerm substituteLocalVar(LamTerm X, LamTerm subject, LamTerm t){
		%match(LamTerm subject){
			const[] -> {return `subject;}
			matchVar[] -> {return `subject;}
			Y@localVar[] -> {
				if (X == Y){ return `t;}
				else {return `subject;}
			}
			app(A1,A2) -> {return `app(substitute(X,subject,A1),substitute(X,subject,A2));}
			abs(x,A1) -> {LamTerm newx=`localVar("_x"+(++comptVariable));
			return `abs(newx,substituteLocalVar(X,subject,(substituteLocalVar(x,newx,A1))));
			}
		}
		return `subject;
	}
	public Collection normalize_Collection(Collection start)  throws VisitFailure{
		Collection result = visit_Collection(start);
		if(!result.equals(start)){
//			System.out.println(result);
			result=normalize_Collection(result);
		}
		return result;
	}
	public Collection visit_Collection(Collection start) throws VisitFailure{
			Collection c=new HashSet();
			Iterator it=start.iterator();
			while(it.hasNext()){
				Systems s=(Systems)it.next();
				c.addAll(reduce(s));
			}
			return c;
		}

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
	public Collection getAllSubterm(LamTerm subject) throws VisitFailure{
		VisitableVisitor collect = new Collect();
 		VisitableVisitor getAll = `mu(MuVar("x"),
 																	Sequence(collect,Try(All(MuVar("x")))));		
		MuTraveler.init(getAll).visit(subject);
		return ((Collect)collect).getBagSubTerm();
	}
	//return all the position of subject in which subTerm occurs
	public Collection getAllPos(LamTerm subject, LamTerm subTerm) throws VisitFailure{
 		VisitableVisitor getPos=new GetPos(subTerm);
 		VisitableVisitor getAllPos=`mu(MuVar("x"),
 																	Sequence(getPos,Try(All(MuVar("x")))));	 	
 		MuTraveler.init(getAllPos).visit(subject);
		return ((GetPos)getPos).getBagPosition();
	}
	class Collect extends LamtermVisitableFwd {
		private Collection bagSubTerm=new HashSet();
		public Collection getBagSubTerm(){
			return bagSubTerm;
		}
		public Collect() {
			super(`Fail());
		}
		public LamTerm visit_LamTerm(LamTerm arg) throws VisitFailure { 
			bagSubTerm.add(arg);
			return arg;
		}
	}
	class GetPos extends LamtermVisitableFwd {
		private Collection bagPosition=new HashSet();
		private LamTerm lookingFor;
		public Collection getBagPosition(){
			return bagPosition;
		}
		public GetPos(LamTerm a) {
			super(`Fail());
			lookingFor=a;
		}
		public LamTerm visit_LamTerm(LamTerm arg) throws VisitFailure { 
			%match(LamTerm arg){
				x -> {
					if (arg == lookingFor) {
						bagPosition.add(MuTraveler.getPosition(this));
					}
				}
			}
			return arg;
		}
	}
}
