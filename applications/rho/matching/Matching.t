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
		while(true){
			System.out.print("mSD>");
      try {
				Equation subject = parser.matchingEquation();
				System.out.println("Resultat du parsing: " + subject);
			} catch (Exception e) {
				System.out.println(e);
			}
		}	
	}

	public Collection reduce(Systems s) throws  VisitFailure { 
		Collection c = new HashSet();
		%match(Systems s){
			//Trivial
			(X*,match(a@const[],a),Y*)-> {
				c.add(`and(X*,Y*));
				return c;}
			(X*,match(x@localVar[],x),Y*)-> {
				c.add(`and(X*,Y*));
				return c;}
			//Abs
			(X*,match(abs(x@localVar[],A),abs(x@localVar[],B)),Y*) ->{
				c.add(`and(X*,match(A,B),Y*));
				return c;}
			//Subst
//					(X*,match(),Y*) ->{
			//					return `and(X*,Y*);}
			//Decompose
			(X*,match(app(A1,B1),app(A2,B2)),Y*) ->{
				c.add(`and(X*,match(A1,A2),match(B1,B2),Y*));
				return c;}
			//Proj
			(X*,match(app(A1,B1),A2),Y*) ->{
				c.add(`and(X*,match(A1,abs(localVar("_x"+(++comptVariable)),A2)),Y*));
				return c;
			}
			//Beta-exp
			//	(X*,match(),Y*) ->{
			//	return `and(X*,Y*);}
			
		}
		throw new VisitFailure();
	}
	class ReductionRules extends LamtermVisitableFwd {
		public ReductionRules() {
			super(`Fail());
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
 																	Sequence(collect,All(MuVar("x"))));		
		MuTraveler.init(getAll).visit(subject);
		return ((Collect)collect).getBagSubTerm();
	}
	//return all the position of subject in which subTerm occurs
	public Collection getAllPos(LamTerm subject, LamTerm subTerm) throws VisitFailure{
 		VisitableVisitor getPos=new GetPos(subTerm);
 		VisitableVisitor getAllPos=`mu(MuVar("x"),
 																	Sequence(getPos,All(MuVar("x"))));	 	
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
		public LamTerm visit_Lamterm(LamTerm arg) throws VisitFailure { 
			%match(LamTerm arg){
				_ -> {bagSubTerm.add(arg);}
			}
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
		public LamTerm visit_Term(LamTerm arg) throws VisitFailure { 
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
