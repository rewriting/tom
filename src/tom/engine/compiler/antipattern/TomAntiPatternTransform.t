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

package tom.engine.compiler.antipattern;

import java.io.*;
import java.util.*;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.exception.*;

import tom.library.strategy.mutraveler.MuTraveler;

import jjtraveler.reflective.VisitableVisitor;
import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.VisitFailure;

/**
 * Contains methods for transforming an anti-pattern problem into a disunification one
 */
public class TomAntiPatternTransform {	

//	------------------------------------------------------------
	%include { adt/tomconstraint/TomConstraint.tom }
//	%include { adt/tomterm/tomterm.tom }
	%include { mutraveler.tom}	
//	------------------------------------------------------------
	
	public static int varCounter = 0;	
	
	private static int antiCounter = 0;
	
	// temporary list that holds variables that need to
	// be quantified 
	private static ArrayList quantifiedVarList = new ArrayList();
	
	/**
	 * transforms the anti-pattern problem received into a disunification one
	 * @param c the anti-pattern problem to transform 
	 * @return corresponding disunification problem 
	 */
	public static Constraint transform(Constraint c) {	
		
		varCounter = 0;		
				
		// eliminate anti
		Constraint noAnti = null;;
		try{
			noAnti = applyMainRule(c);
		}catch(VisitFailure e){
			throw new TomRuntimeException("Reduction failed on: " + c + 
					"\nException:" + e.getMessage());			
		}
		
		System.out.println("Result after main rule: " + TomAntiPatternUtils.formatConstraint(noAnti));
		
		// transform the problem into a disunification one		
		Constraint disunifProblem = null;
		try {		
			disunifProblem = (Constraint) MuTraveler.init(`InnermostId(TransformIntoDisunification())).visit(noAnti);			
		} catch (VisitFailure e) {
			throw new TomRuntimeException("Reduction failed on: " + noAnti + 
					"\nException:" + e.getMessage());
		}
		
	    System.out.println("Disunification problem: " + TomAntiPatternUtils.formatConstraint(disunifProblem));
		return disunifProblem;
	}	
	
	// applies the main rule that transforms ap problems
	// into dis-unification ones
	private static Constraint applyMainRule(Constraint c) throws VisitFailure{
		
		TomTerm pattern = null;
		TomTerm subject = null;
		
		%match(Constraint c){
			EqualConstraint(p,s) ->{
				pattern = `p;
				subject = `s;
			}
		}				
		
		// first get the constraint without the anti
		Constraint cNoAnti =  `EqualConstraint((TomTerm) MuTraveler.init(OnceTopDownId(ElimAnti())).visit(pattern),subject);
		// if nothing changed, time to exit
		if (cNoAnti == c){
			return c;
		}
		// get the constraint with a variable instead of anti
		Constraint cAntiReplaced =  `EqualConstraint((TomTerm) MuTraveler.init(OnceTopDownId(ReplaceAnti())).visit(pattern),subject);
		
		cAntiReplaced = `Exists(Variable(concOption(),Name("v" + varCounter),EmptyType(),concConstraint()),
				applyMainRule(cAntiReplaced));
		
		cNoAnti = applyMainRule(cNoAnti);
		
//		System.out.println("Asta e 'c':" + tools.formatConstraint(c));
		
		cNoAnti = `Neg(cNoAnti);
		
		quantifiedVarList.clear();
		
		MuTraveler.init(`OnceTopDownId(ApplyStrategy())).visit(c);
		
		Iterator it = quantifiedVarList.iterator();
		while(it.hasNext()){
			TomTerm t = (TomTerm)it.next();
			cNoAnti = `ForAll(t,cNoAnti);
		}
		
	//	System.out.println("antiCounter=" + antiCounter + " cNoAnti=" + tools.formatConstraint(cNoAnti));	

		return `AndConstraint(concAnd(cAntiReplaced,cNoAnti));
	}
	
	
	// quantifies the variables in positive positions
	%strategy AnalyzeTerm(subject:TomTerm) extends `Identity(){		
		
		visit TomTerm {
			v@Variable(_,_,_,_) ->{
				
				antiCounter = 0;
				
//				System.out.println("Analyzing " + `v + " position=" + getPosition() );
				
				MuStrategy useOmegaPath = (MuStrategy)getPosition().getOmegaPath(`CountAnti());				
				
				MuTraveler.init(useOmegaPath).visit(subject);
				
//				System.out.println("After analyzing counter=" + antiCounter);
				// if no anti-symbol found, than the variable can be quantified
				if (antiCounter == 0) {
					quantifiedVarList.add(`v);
				}
			}
		}		
	}
	
	// counts the anti symbols 
	%strategy CountAnti() extends `Identity(){
		visit TomTerm {
			AntiTerm(_) -> {
				antiCounter++;				
			}
		}
	}
	
	// returns a term without the first negation that it finds
	%strategy ElimAnti() extends `Identity(){		
		visit TomTerm {		 
			AntiTerm(p) -> {
				return `p;
			}
		}
	}
	
	// returns a term with the first negation that it finds
	// replaced by a fresh variable
	%strategy ReplaceAnti() extends `Identity(){
		
		visit TomTerm {
			// main rule
			a@AntiTerm(_) -> {
				return `Variable(concOption(),Name("v" + (++varCounter)),EmptyType(),concConstraint());
			}
		}
	}
	
	// the strategy that handles the variables inside an anti
	// symbol for beeing qunatified 
	%strategy ApplyStrategy() extends `Identity(){
		
		visit TomTerm {
			// main rule
			anti@AntiTerm(p) -> {
				TomTerm t = (TomTerm)MuTraveler.init(`InnermostId(AnalyzeTerm(p))).visit(`p);				
				// now it has to stop
				return `p;
			}
		}
	}
	
	// applies symple logic rules for eliminating 
	// the not and thus creating a real disunification problem
	%strategy TransformIntoDisunification() extends `Identity(){
		
		visit Constraint {
			
			// some simplification stuff
			Exists(a,Exists(a,b)) ->{
				return `Exists(a,b);
			}
			
			// producing or - de morgan 1
			Neg(AndConstraint(a)) ->{
				
				AConstraintList l = `a;
				OConstraintList result = `concOr();			
				
				while(!l.isEmptyconcAnd()){
					result = `concOr(Neg(l.getHeadconcAnd()),result*);
					l = l.getTailconcAnd();
				}				
				result = result.reverse();
				return `OrConstraint(result);
			}
			
			// make Neg go down - de morgan 2
			n@Neg(OrConstraint(a)) -> {
				
				OConstraintList l = `a;
				AConstraintList result = `concAnd();
				
				while(!l.isEmptyconcOr()){
					result = `concAnd(Neg(l.getHeadconcOr()),result*);
					l = l.getTailconcOr();
				}
				result = result.reverse();
				return `AndConstraint(result);
			}
			
			// BooleanSimplification
			Neg(Neg(x)) -> { return `x; }
			
			// replace Neg(EqualConstraint) with NEqualConstraint
			Neg(EqualConstraint(a,b)) -> { return `NEqualConstraint(a,b);}
			
			// replace Neg(NEqualConstraint) with EqualConstraint
			Neg(NEqualConstraint(a,b)) -> { return `EqualConstraint(a,b);}
			
			// replace Neg(Exists) with ForAll
			Neg(Exists(v,c)) ->{			
				return `ForAll(v,Neg(c));			
			}
			
			// replace Neg(ForAll) with Exists
			Neg(ForAll(v,c)) ->{			
				return `Exists(v,Neg(c));			
			}
		}
	}		
} // end class
