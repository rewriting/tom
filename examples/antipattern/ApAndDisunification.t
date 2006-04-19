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
				transformedMatch = `Equal(p,s/*GenericGroundTerm("SUBJECT")*/);
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
		
//		System.out.println("Result after main rule: " + formatConstraint(noAnti));
		
		Constraint compiledConstraint = null, solvedConstraint = null;
	
		VisitableVisitor decomposeTerms = `DecomposeTerms();
		VisitableVisitor solve = `SolveRes();
		
		try {		
			compiledConstraint = (Constraint) MuTraveler.init(`InnermostId(simplifyRule)).visit(noAnti);
			solvedConstraint = (Constraint) MuTraveler.init(`SequenceId(InnermostId(decomposeTerms),InnermostId(solve))).visit(compiledConstraint);
		} catch (VisitFailure e) {
			System.out.println("reduction failed on: " + c);
			e.printStackTrace();
		}
		
		System.out.println("Final result: " + formatConstraint(compiledConstraint));
		System.out.println("Final result solved: " + formatConstraint(solvedConstraint));
		
		return compiledConstraint;		
	}
	
	//	applies the main rule that transforms ap problems
	//	into dis-unification ones
	public Constraint applyMainRule(Constraint c) throws VisitFailure{
		
		VisitableVisitor elimAnti = `ElimAnti();
		VisitableVisitor replaceAnti = `ReplaceAnti();
		
		 %match(Constraint c){
			 Equal(Anti(p),subject) -> {
				 return `Neg(applyMainRule(Equal(p,subject)));
			 }
			 Equal(pattern,subject) -> {
				 // first get the pattern without the anti
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
			a@Anti(_) -> {
				return `Variable("v" + (++ApAndDisunification.varCounter) );
			}
		}
	}
	
	%strategy SimplifyWithDisunification() extends `Identity(){
		
		visit Constraint {			
			
			//simple rules with exists and forall
			Exists(Variable(a),Equal(Variable(a),_)) ->{				
				return `True();
			}						
			Exists(Variable(a),Neg(Equal(Variable(a),_))) ->{				
				return `True();
			}
			Exists(Variable(name),constr)->{
				// eliminates the cuantificator when the 
				// constraint does not contains the variable				
				
				//TODO - replace with a strategy				
				if (! (`constr.toString().indexOf(`name) > -1) ) {
					return `constr;
				}
				
			}
//			ForAll(Variable(a),Equal(Variable(a),_)) ->{
//				return `False();
//			}
//			ForAll(Variable(a),Neg(Equal(Variable(a),_))) ->{
//				return `False();
//			}			
			
			// distribution of exists in and and or 			
			Exists(v@Variable(var),And(list)) ->{
				
				ConstraintList l = `list;
				ConstraintList result = `concConstraint();
				
				while(!l.isEmptyconcConstraint()){
					result = `concConstraint(Exists(v,l.getHeadconcConstraint()),result*);
					l = l.getTailconcConstraint();
				}
				
				return `And(result);
			}			
			Exists(v@Variable(var),Or(list)) ->{
				
				ConstraintList l = `list;
				ConstraintList result = `concConstraint();
				
				while(!l.isEmptyconcConstraint()){
					result = `concConstraint(Exists(v,l.getHeadconcConstraint()),result*);
					l = l.getTailconcConstraint();
				}
				
				return `Or(result);
			}
			
			//BooleanSimplification
			Neg(Neg(x)) -> { return `x; }
			Neg(True()) -> { return `False(); }
			Neg(False()) -> { return `True(); }
			
			// Decompose
			e@Equal(Appl(name,a1),g) -> {
				
				%match(Term g){
					SymbolOf(_) -> {return `e;}
				}				
				
				ConstraintList l = `concConstraint();
				TermList args1 = `a1;
				
				int counter = 0;
				
				while(!args1.isEmptyconcTerm()) {
					l = `concConstraint(Equal(args1.getHeadconcTerm(),Subterm(++counter,g)),l*);					
					args1 = args1.getTailconcTerm();										
				}
				
				l = `concConstraint(Equal(Appl(name,concTerm()),SymbolOf(g)),l*);
				
//				System.out.println("Entered with :" + `e);
//				System.out.println("Left with :" + `l);
				
				return `And(l);
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
			
			// producing or - de morgan 1
			Neg(And(a)) ->{
				
				ConstraintList l = `a;
				ConstraintList result = `concConstraint();			
				
				while(!l.isEmptyconcConstraint()){
					result = `concConstraint(Neg(l.getHeadconcConstraint()),result*);
					l = l.getTailconcConstraint();
				}				
				
				return `Or(result);
			}
			
			// make Neg go down - de morgan 2
			n@Neg(Or(a)) -> {
				
				ConstraintList l = `a;
				ConstraintList result = `concConstraint();
				
				//System.out.println("Came with:" + formatConstraint(`n));
				
				while(!l.isEmptyconcConstraint()){
					result = `concConstraint(Neg(l.getHeadconcConstraint()),result*);
					l = l.getTailconcConstraint();
				}
				
				//System.out.println("Left with:" + formatConstraint(`And(result)));
				
				return `And(result);
			}
			
			
			// Delete
			and@And(concConstraint(_*,Equal(a,b),_*,Neg(Equal(a,b)),_*)) ->{
				
				%match(Term a){
					v@Variable(_) -> { System.out.println("and " + `and);} 
				}
				
				return `False();
			}
			
			and@And(concConstraint(_*,Neg(Equal(a,b)),_*,Equal(a,b),_*)) ->{				
				
				%match(Term a){
					v@Variable(_) -> { System.out.println("and_1 " + `and);} 
				}
				
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
			And(concConstraint(X*,True(),Y*)) -> {
				return `And(concConstraint(X*,Y*));
			}			
			
//			Or(c@concConstraint(_*,True(),_*)) -> {
//				System.out.println("8888 " + `c);
//				return `True();
//			}		
			
			// cleaning the result			
			And(concConstraint(And(concConstraint(X*)),Y*)) ->{
				return `And(concConstraint(X*,Y*));
			}
			
			And(concConstraint(X*,a,Y*,a,Z*)) -> {				
				return `And(concConstraint(X*,a,Y*,Z*));
			}
			
			Or(concConstraint(X*,a,Y*,a,Z*)) -> {				
				return `And(concConstraint(X*,a,Y*,Z*));
			}
			
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
			
		} // end visit
	} // end strategy
	
	
	%strategy DecomposeTerms() extends `Identity(){
		
		visit Term {
			
			SymbolOf(Appl(name,_)) ->{
				return `Appl(name,concTerm());
			}
			
			Subterm(no,Appl(name,list)) -> {
				
				TermList tl = `list;
				Term tmp = null;				
				
				try{								
					for (int i = 1; i <= `no; i++,tl=tl.getTailconcTerm()){
						tmp = tl.getHeadconcTerm();						
					}
				}catch(UnsupportedOperationException e){
					return `FalseTerm(); // decomposition not possible
				}

				return tmp;
			}
		}
	}
	
	%strategy SolveRes() extends `Identity(){
		
		visit Constraint {
			
			// ground terms' equality
			Equal(p@Appl(_,_),g@Appl(_,_)) -> {
//				System.out.println("p:" + `p + " g:" + `g + " result:" +  (`g == `p));
				return (`g == `p ? `True() : `False() );
			}
			
			Equal(_,FalseTerm()) ->{
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
			And(concConstraint(X*,True(),Y*)) -> {
				return `And(concConstraint(X*,Y*));
			}			
			
			Or(concConstraint(_*,True(),_*)) -> {
				return `True();
			}
			
			//BooleanSimplification
			Neg(Neg(x)) -> { return `x; }
			Neg(True()) -> { return `False(); }
			Neg(False()) -> { return `True(); }
			
			And(concConstraint()) -> {
				return `True();
			}
			
			Or(concConstraint()) -> {
				return `False();
			}
			
			// cleaning			
			And(concConstraint(X*,a,Y*,a,Z*)) -> {
				return `And(concConstraint(X*,a,Y*,Z*));
			}
			
			Or(concConstraint(X*,a,Y*,a,Z*)) -> {
				return `And(concConstraint(X*,a,Y*,Z*));
			}
			
			And(concConstraint(x)) -> {
				return `x;
			}
			
			Or(concConstraint(x)) -> {
				return `x;
			}
			
			// merging rules - Comon and Lescanne
			
			// m1
			And(concConstraint(X*,Equal(Variable(z),t),Y*,Equal(Variable(z),u),Z*)) ->{
				return `And(concConstraint(X*,Equal(Variable(z),t),Y*,Equal(t,u),Z*));
			}			
			// m2
			Or(concConstraint(X*,Neg(Equal(Variable(z),t)),Y*,Neg(Equal(Variable(z),u)),Z*)) ->{
				return `Or(concConstraint(X*,Neg(Equal(Variable(z),t)),Y*,Neg(Equal(t,u)),Z*));
			}
			// m3
			And(concConstraint(X*,Equal(Variable(z),t),Y*,Neg(Equal(Variable(z),u)),Z*)) ->{
				return `And(concConstraint(X*,Equal(Variable(z),t),Y*,Neg(Equal(t,u)),Z*));
			}
			And(concConstraint(X*,Neg(Equal(Variable(z),u)),Y*,Equal(Variable(z),t),Z*)) ->{
				return `And(concConstraint(X*,Equal(Variable(z),t),Y*,Neg(Equal(t,u)),Z*));
			}
			// m4
			Or(concConstraint(X*,Equal(Variable(z),t),Y*,Neg(Equal(Variable(z),u)),Z*)) ->{
				return `Or(concConstraint(X*,Equal(t,u),Y*,Neg(Equal(Variable(z),u)),Z*));
			}
			Or(concConstraint(X*,Neg(Equal(Variable(z),u)),Y*,Equal(Variable(z),t),Z*)) ->{
				return `Or(concConstraint(X*,Equal(t,u),Y*,Neg(Equal(Variable(z),u)),Z*));
			}
		}
	}
	
	private String formatConstraint(Constraint c){
		
		%match(Constraint c) {
			True() -> {
				return "T";	
			}
			False() ->{
				return "F";
			}
			Neg(cons) ->{
				return "Neg(" + formatConstraint(`cons) + ")";
			}
			And(concConstraint(x,Z*)) ->{
				
				ConstraintList l = `Z*;
				String result = formatConstraint(`x);
				
				while(!l.isEmptyconcConstraint()){
					result += " and " + formatConstraint(l.getHeadconcConstraint());
					l = l.getTailconcConstraint();
				}
				
				return result; 
			}
			Or(concConstraint(x,Z*)) ->{
				
				ConstraintList l = `Z*;
				String result = formatConstraint(`x);
				
				while(!l.isEmptyconcConstraint()){
					result += " or " + formatConstraint(l.getHeadconcConstraint());
					l = l.getTailconcConstraint();
				}
				
				return result; 
			}
			Equal(pattern, subject) ->{
				return formatTerm(`pattern) + "=" + formatTerm(`subject); 
			}
			Exists(Variable(name),cons) -> {
				return "exists " + `name + ", ( " + formatConstraint(`cons) + " ) "; 
			}			
			ForAll(Variable(name),cons) -> {				
				return "for all " + `name + ", ( " + formatConstraint(`cons) + " ) ";				
			}
		}
		
		return c.toString();
	}
	
	private String formatTerm(Term t){
		
		%match(Term t){
			Variable(name) ->{
				return `name;
			}
	        Appl(name, concTerm())->{
	        	return `name;
	        }	     
	        GenericGroundTerm(name) ->{
	    	   return `name;
	        }		
		}
	
		return t.toString();
	}
	
//	private boolean notContainsVar(Term a, Constraint constr) {
//		
//		//TODO - replace with a strategy
//		%match(Term a, Constraint constr){
//			Variable(vname),c ->{
//				return !c.toString().contains(`vname);
//			}
//		}		
//		
//		return true;
//	}	
	
	

} //end class
