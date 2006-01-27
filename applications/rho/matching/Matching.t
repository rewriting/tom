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
		class ReductionRules extends LamtermVisitableFwd {
			public ReductionRules() {
				super(`Fail());
			}
			public Systems visit_System(Systems s) throws  VisitFailure { 
				%match(Systems s){
					//Trivial
					(X*,match(a@const[],a),Y*)-> {
						return `and(X*,Y*);}
					(X*,match(x@localVar[],x),Y*)-> {
						return `and(X*,Y*);}
					//Abs
					(X*,match(abs(x@localVar[],A),abs(x@localVar[],B)),Y*) ->{
						return `and(X*,match(A,B),Y*);}
					//Subst
//					(X*,match(),Y*) ->{
					//					return `and(X*,Y*);}
					//Decompose
					(X*,match(app(A1,B1),app(A2,B2)),Y*) ->{
						return `and(X*,match(A1,A2),match(B1,B2),Y*);}
					//Proj
					(X*,match(app(A1,B1),A2),Y*) ->{
						return `and(X*,match(A1,abs(localVar("_x"+(++comptVariable)),A2)),Y*);
					}
					//Beta-exp
					//	(X*,match(),Y*) ->{
					//	return `and(X*,Y*);}

				}
				throw new VisitFailure();
			}
		}
}
