/*
 * Copyright (c) 2005-2006, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.  
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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

package antipattern;

import aterm.*;
import aterm.pure.*;

import java.io.*;
import java.util.*;

import antipattern.term.*;
import antipattern.term.types.*;

import tom.library.strategy.mutraveler.MuTraveler;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;


public class ApAndDisunification implements Matching{
	
	%include{ mutraveler.tom }	
	%include{ term/Term.tom }
	
	public static int varCounter = 0;
	
	public Constraint simplifyAndSolve(Constraint c,Collection solution) {
		
		Constraint transformedMatch = null;
		varCounter = 0;
		
		label:%match(Constraint c){
			Match(p,s) -> { 
				transformedMatch = `Equal(p,GenericGroundTerm(s.toString()));
				break label;
			}
			_ -> {
				throw new RuntimeException("Abnormal term received in simplifyAndSolve:" + c);
			}
		}
		
		VisitableVisitor simplifyRule = `SimplifyWithDisunification();
		Constraint noAnti = null;;
		try{
			noAnti = applyMainRule(transformedMatch);
		}catch(VisitFailure e){
			System.out.println("reduction failed on: " + c);
			e.printStackTrace();
		}
		
		System.out.println("Result: " + noAnti);
		
		try {		
			return (Constraint) MuTraveler.init(`InnermostId(simplifyRule)).visit(noAnti);			
		} catch (VisitFailure e) {
			System.out.println("reduction failed on: " + c);
			e.printStackTrace();
		}
		return `False();		
	}
	
	//	applies the main rule that transforms ap problems
	//	into dis-unification ones
	public Constraint applyMainRule(Constraint c) throws VisitFailure{
		
		VisitableVisitor elimAnti = `ElimAnti();
		VisitableVisitor replaceAnti = `ReplaceAnti();
		
		 %match(Constraint c){
			 Equal(pattern,subject) -> {
				 // first get the pattern withour the anti
				 Term pNoAnti =  (Term) MuTraveler.init(`OnceBottomUpId(elimAnti)).visit(`pattern);
				 //if nothing changed, time to exit
				 if (pNoAnti == `pattern){
					 return `Equal(pattern,subject);
				 }
				 // get the pattern with a variable instead of anti
				 Term pAntiReplaced =  (Term) MuTraveler.init(`OnceBottomUpId(replaceAnti)).visit(`pattern);
				 
				 // recursive call to itself
				 return `And(concConstraint(Exists(Variable("v" + (ApAndDisunification.varCounter)),
						 applyMainRule(Equal(pAntiReplaced,subject))),
						 	Neg(applyMainRule(Equal(pNoAnti,subject)))));
			 }
		 }	
		 
		 throw new RuntimeException("Abnormal term received in applyMainRule: " + c); 
	}
	
	//	returns a term without the first negation that it finds
	%strategy ElimAnti() extends `Identity(){
		
		visit Term {		 
			Anti(p) -> {
				return `p;
			}
		}
	}

	// returns a term with the first negation that it finds  
	// replaced by a fresh variable
	%strategy ReplaceAnti() extends `Identity(){
		
		visit Term {
			//main rule 
			Anti(_) -> {
				return `Variable("v" + (++ApAndDisunification.varCounter) );
			}
		}
	}
	
	%strategy SimplifyWithDisunification() extends `Identity(){
		
		visit Constraint {
			
			//simple rules with exists and forall
			Exists(Variable(a),Equal(Variable(a),GenericGroundTerm(_))) ->{
				return `True();
			}
			Exists(Variable(a),Neg(Equal(Variable(a),GenericGroundTerm(_)))) ->{
				return `True();
			}
			ForAll(Variable(a),Equal(Variable(a),GenericGroundTerm(_))) ->{
				return `False();
			}
			ForAll(Variable(a),Neg(Equal(Variable(a),GenericGroundTerm(_)))) ->{
				return `False();
			}
			
			//BooleanSimplification
			Neg(Neg(x)) -> { return `x; }
			Neg(True()) -> { return `False(); }
			Neg(False()) -> { return `True(); }
			
			// Decompose
			Equal(Appl(name,a1),g@GenericGroundTerm(_)) -> {
				
				ConstraintList l = `concConstraint();
				TermList args1 = `a1;
				
				int counter = 0;
				
				while(!args1.isEmptyconcTerm()) {
					l = `concConstraint(Equal(args1.getHeadconcTerm(),Subterm(++counter,g)),l*);
					args1 = args1.getTailconcTerm();										
				}
				
				l = `concConstraint(SymbolOf(g,name),l*);
				
				return `And(l);
			}
			
			// ground terms' equality
			Equal(p@Appl(_,concTerm()),g@GenericGroundTerm(_)) -> {
				return `g == `p ? `True() : `False();
			}
			
			// associativity for AND and OR
			And(concConstraint(X*,Or(orList),Y*)) ->{
				
				ConstraintList orTerms = `orList;
				ConstraintList l = `concConstraint();
				
				while(!orTerms.isEmptyconcConstraint()) {
					l = `concConstraint(And(concConstraint(X*,Y*,orTerms.getHeadconcConstraint())),l*);
					orTerms = orTerms.getTailconcConstraint();										
				}
				
				return `Or(l);
			}
			
//			// Replace
//			input@And(concConstraint(X*,match@Match(var@Variable(name),s),Y*)) -> {	            
//	            VisitableVisitor rule,ruleStrategy;            
//	            if (isIdentity){
//	            	rule = new ReplaceSystem(`var,`s, `Identity());
//	            	ruleStrategy = `InnermostId(rule);
//	            }else{
//	            	rule = new ReplaceSystem(`var,`s, `Fail());
//	            	ruleStrategy = `Innermost(rule);
//	            }            
//	            Constraint res = (Constraint) MuTraveler.init(ruleStrategy).visit(`And(concConstraint(X*,Y*)));
//	            if (res != `And(concConstraint(X*,Y*))){
//	            	return `And(concConstraint(match,res));
//	            }
//	        }
			
			// Delete
			And(concConstraint(_*,Equal(a,b),_*,Neg(Equal(a,b)),_*)) ->{
				return `False();
			}
			
			And(concConstraint(_*,Neg(Equal(a,b)),_*,Equal(a,b),_*)) ->{
				return `False();
			}
			
			// PropagateClash
			And(concConstraint(_*,False(),_*)) -> {
				return `False();
			}
			
			Or(concConstraint(X*,False(),Y*)) -> {
				return `Or(concConstraint(X*,Y*));
			}
			
			// PropagateSuccess
			And(concConstraint()) -> {
				return `True();
			}
			
			Or(concConstraint()) -> {
				return `True();
			}
			
			And(concConstraint(x)) -> {
				return `x;
			}
			
			Or(concConstraint(x)) -> {
				return `x;
			}
			
			And(concConstraint(X*,True(),Y*)) -> {
				return `And(concConstraint(X*,Y*));
			}
			
			Or(concConstraint(_*,True(),_*)) -> {
				return `True();
			}
			
		} // end visit
	} // end strategy

} //end class
