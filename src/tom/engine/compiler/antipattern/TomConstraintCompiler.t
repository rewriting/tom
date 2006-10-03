/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Radu Kopetz  e-mail: Radu.Kopetz@loria.fr
 *
 **/

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
 * Tom constraint compiler
 */
public class TomConstraintCompiler{
	
//	------------------------------------------------------------
	%include { adt/tomconstraint/TomConstraint.tom }
	%include { mustrategy.tom}	
//	------------------------------------------------------------
	
	/**
	 * Receives a constraint and tries to compile it using the disunification
	 * rules
	 * 
	 * @param consToCompile
	 *            The constraint to compile
	 * @return The compiled constraint
	 */
	public static Constraint compile(Constraint consToCompile) {
		
		// apply the disunification rules
		Constraint compiledConstraint = null;		
		
		try {		
			compiledConstraint = (Constraint) MuTraveler.init(`InnermostId(SimplifyWithDisunification())).visit(consToCompile);
		} catch (VisitFailure e) {
			throw new TomRuntimeException("Compile failed on: " + consToCompile + 
					"\nException:" + e.getMessage());
		}
		
//		System.out.println("Compiled constraint: " + TomAntiPatternUtils.formatConstraint(compiledConstraint));
		
		return compiledConstraint;
	}
	
	/**
	 *  Strategy that contains the disunification rules
	 */
	%strategy SimplifyWithDisunification() extends `Identity(){
		
		visit Constraint {			
			
			// simple rules with exists and forall
			Exists(Variable[AstName=a],(EqualConstraint|NEqualConstraint)(Variable[AstName=a],_)) ->{				
				return `TrueConstraint();
			}						
			Exists(v@Variable[],constr)->{
				// eliminates the quantificator when the
				// constraint does not contains the variable
				
				if (!TomAntiPatternUtils.containsVariable(`constr,`v)){				
					return `constr;
				}
				
			}			
			ForAll(Variable[AstName=a],EqualConstraint(Variable[AstName=a],_)) ->{
				return `FalseConstraint();
			}
			ForAll(Variable[AstName=a],NEqualConstraint(Variable[AstName=a],_)) ->{
				return `FalseConstraint();
			}
			ForAll(v@Variable[],constr)->{
				// eliminates the quantificator when the
				// constraint does not contains the variable
				
				if (!TomAntiPatternUtils.containsVariable(`constr,`v)){
					return `constr;
				}				
			}
			
			// ///////////////////////////////////////////////////
			
			// distribution of exists/forall in and and or - allows
			// simplification with the rules above
			e@Exists(v@Variable[],AndConstraint(list)) ->{
				
				AConstraintList l = `list;
				AConstraintList nonQuantifiedConstraints = `concAnd();
				AConstraintList quantifiedConstraints = `concAnd();
				
				while(!l.isEmptyconcAnd()){
					Constraint c = l.getHeadconcAnd();
					
					// if the c doesn't contain the variable, we
					// can put it outside the expresion that is quantified
					if ( !TomAntiPatternUtils.containsVariable(`c,`v) ) {
						nonQuantifiedConstraints = `concAnd(c,nonQuantifiedConstraints*);
					}else{
						quantifiedConstraints = `concAnd(c,quantifiedConstraints*);
					}				
					
					// nonquantifiedConstraints = `concAnd(Exists(v,l.getHeadconcAnd()),nonquantifiedConstraints*);
					l = l.getTailconcAnd();
				}
				
				// if couldn't do anything, return the same thing
				if (nonQuantifiedConstraints.isEmptyconcAnd()){
					return `e;
				}
				
				nonQuantifiedConstraints = nonQuantifiedConstraints.reverse();
				quantifiedConstraints = quantifiedConstraints.reverse();
				
				// if all were separated
		        if (quantifiedConstraints.isEmptyconcAnd()){
		          return `AndConstraint(nonQuantifiedConstraints);
		        }
		        // we quantify and mix both lists
		        return `AndConstraint(concAnd(Exists(v,AndConstraint(quantifiedConstraints)),nonQuantifiedConstraints*));
			}			

			Exists(v@Variable[],OrConstraint(list)) ->{
				
				OConstraintList l = `list;
				OConstraintList result = `concOr();
				
				while(!l.isEmptyconcOr()){
					result = `concOr(Exists(v,l.getHeadconcOr()),result*);
					l = l.getTailconcOr();
				}
				result.reverse();
				return `OrConstraint(result);
        // return `OrConstraint(map(addExist).visit(list));
        // return `OrConstraint(map(?"x";!Exists(v,!"x")).visit(list));
			}

			ForAll(v@Variable[],AndConstraint(list)) ->{
				
				AConstraintList l = `list;
				AConstraintList result = `concAnd();
				
				while(!l.isEmptyconcAnd()){
					result = `concAnd(ForAll(v,l.getHeadconcAnd()),result*);
					l = l.getTailconcAnd();
				}
				result.reverse();
				return `AndConstraint(result);
			}			
			
			f@ForAll(v@Variable[],OrConstraint(list)) ->{
				
				OConstraintList l = `list;
				OConstraintList nonQuantifiedConstraints = `concOr();
				OConstraintList quantifiedConstraints = `concOr();
				
				while(!l.isEmptyconcOr()){
					
					Constraint c = l.getHeadconcOr();
					
					// if the c doesn't contain the variable, we
					// can put it outside the expresion that is quantified
					if ( !TomAntiPatternUtils.containsVariable(`c,`v) ) {					
						nonQuantifiedConstraints = `concOr(c,nonQuantifiedConstraints*);
					}else{
						quantifiedConstraints = `concOr(c,quantifiedConstraints*);
					}			
					
					l = l.getTailconcOr();
				}
				
				// if couldn't do anything, return the same thing
				if (nonQuantifiedConstraints.isEmptyconcOr()){
					return `f;
				}
				
				nonQuantifiedConstraints = nonQuantifiedConstraints.reverse();
				quantifiedConstraints = quantifiedConstraints.reverse();
				
				// if all were separated
				if (quantifiedConstraints.isEmptyconcOr()){
					return `OrConstraint(nonQuantifiedConstraints);	
				}
				
				// we quantify and mix both lists
				return `OrConstraint(concOr(ForAll(v,OrConstraint(quantifiedConstraints)),nonQuantifiedConstraints*));				
			}			
			
			// ///////////////////////////////////////////////////////////////////
			
			// Delete
			AndConstraint(concAnd(_*,EqualConstraint(a,b),_*,NEqualConstraint(a,b),_*)) ->{				
				return `FalseConstraint();
			}			
			AndConstraint(concAnd(_*,NEqualConstraint(a,b),_*,EqualConstraint(a,b),_*)) ->{				
				return `FalseConstraint();
			}			
			
			// Elimination of trivial equations and disequations			
			EqualConstraint(a,a) ->{
				return `TrueConstraint();
			}			
			NEqualConstraint(a,a) ->{
				return `FalseConstraint();
			}
			
			// Special cleaning
			OrConstraint(concOr(X*,NEqualConstraint(x,a),Y*,AndConstraint(concAnd(T*,EqualConstraint(x,a),U*)),Z*)) ->{
				return `OrConstraint(concOr(X*,NEqualConstraint(x,a),Y*,AndConstraint(concAnd(T*,U*)),Z*));
			}
			AndConstraint(concAnd(X*,EqualConstraint(x,a),Y*,OrConstraint(concOr(T*,NEqualConstraint(x,a),U*)),Z*)) ->{
				return `AndConstraint(concAnd(X*,EqualConstraint(x,a),Y*,OrConstraint(concOr(T*,U*)),Z*));
			}
			
			// PropagateClash
			AndConstraint(concAnd(_*,FalseConstraint(),_*)) -> {
				return `FalseConstraint();
			}
			
			OrConstraint(concOr(X*,FalseConstraint(),Y*)) -> {
				return `OrConstraint(concOr(X*,Y*));
			}
			
			// PropagateSuccess
			AndConstraint(concAnd(X*,TrueConstraint(),Y*)) -> {
				return `AndConstraint(concAnd(X*,Y*));
			}
			
			OrConstraint(concOr(_*,TrueConstraint(),_*)) -> {			
				return `TrueConstraint();
			}
			
			// cleaning the result			
			AndConstraint(concAnd(X*,AndConstraint(concAnd(Y*)),Z*)) ->{
				return `AndConstraint(concAnd(X*,Y*,Z*));
			}			

			OrConstraint(concOr(X*,OrConstraint(concOr(Y*)),Z*)) ->{
				return `OrConstraint(concOr(X*,Y*,Z*));
			}					

			
			AndConstraint(concAnd(X*,a,Y*,a,Z*)) -> {				
				return `AndConstraint(concAnd(X*,a,Y*,Z*));
			}
			
			OrConstraint(concOr(X*,a,Y*,a,Z*)) -> {				
				return `OrConstraint(concOr(X*,a,Y*,Z*));
			}
			
			AndConstraint(concAnd()) -> {
				return `TrueConstraint();
			}
			
			OrConstraint(concOr()) -> {
				return `FalseConstraint();
			}
			
			AndConstraint(concAnd(x)) -> {
				return `x;
			}
			
			OrConstraint(concOr(x)) -> {
				return `x;
			}
			
			//////////////////////////////////////////////////////
			// Decompose
			e@EqualConstraint(RecordAppl(options,name,a1,constraints),g) -> {
				
				%match(g) {
					SymbolOf(_) -> {return `e;}
				}				
				
				AConstraintList l = `concAnd();
				SlotList args1 = `a1;
				
				int counter = 0;
				
				while(!args1.isEmptyconcSlot()) {
					Slot headSlot = args1.getHeadconcSlot();
					l = `concAnd(EqualConstraint(headSlot.getAppl(),Subterm(name.getHeadconcTomName()
							,headSlot.getSlotName(),g)),l*);					
					args1 = args1.getTailconcSlot();										
				}
				
				l = l.reverse();
				
				l = `concAnd(EqualConstraint(RecordAppl(options,name,concSlot(),constraints),SymbolOf(g)),l*);
				
				return `AndConstraint(l);
			}
			
			e@NEqualConstraint(RecordAppl(options,name,a1,constraints),g) -> {
				
				%match(g){
					SymbolOf(_) -> {return `e;}
				}				
				
				OConstraintList l = `concOr();
				SlotList args1 = `a1;
				
				int counter = 0;
				
				while(!args1.isEmptyconcSlot()) {
					Slot headSlot = args1.getHeadconcSlot();
					l = `concOr(NEqualConstraint(headSlot.getAppl(),Subterm(name.getHeadconcTomName()
							,headSlot.getSlotName(),g)),l*);					
					args1 = args1.getTailconcSlot();										
				}
				
				l = l.reverse();
				
				l = `concOr(NEqualConstraint(RecordAppl(options,name,concSlot(),constraints),SymbolOf(g)),l*);
				
				return `OrConstraint(l);
			}
			
			////////////////////////
			
			// merging rules - Comon and Lescanne
			
			// m1
			AndConstraint(concAnd(X*,EqualConstraint(var@Variable[AstName=z],t),Y*,EqualConstraint(Variable[AstName=z],u),Z*)) ->{
				return `AndConstraint(concAnd(X*,EqualConstraint(var,t),Y*,EqualConstraint(t,u),Z*));
			}			
			// m2
			OrConstraint(concOr(X*,NEqualConstraint(var@Variable[AstName=z],t),Y*,NEqualConstraint(Variable[AstName=z],u),Z*)) ->{
				return `OrConstraint(concOr(X*,NEqualConstraint(var,t),Y*,NEqualConstraint(t,u),Z*));
			}
			// m3
			AndConstraint(concAnd(X*,EqualConstraint(var@Variable[AstName=z],t),Y*,NEqualConstraint(Variable[AstName=z],u),Z*)) ->{
				return `AndConstraint(concAnd(X*,EqualConstraint(var,t),Y*,NEqualConstraint(t,u),Z*));
			}
			AndConstraint(concAnd(X*,NEqualConstraint(var@Variable[AstName=z],u),Y*,EqualConstraint(Variable[AstName=z],t),Z*)) ->{
				return `AndConstraint(concAnd(X*,EqualConstraint(var,t),Y*,NEqualConstraint(t,u),Z*));
			}
			// m4
			OrConstraint(concOr(X*,EqualConstraint(var@Variable[AstName=z],t),Y*,NEqualConstraint(Variable[AstName=z],u),Z*)) ->{
				return `OrConstraint(concOr(X*,EqualConstraint(t,u),Y*,NEqualConstraint(var,u),Z*));
			}
			OrConstraint(concOr(X*,NEqualConstraint(var@Variable[AstName=z],u),Y*,EqualConstraint(Variable[AstName=z],t),Z*)) ->{
				return `OrConstraint(concOr(X*,EqualConstraint(t,u),Y*,NEqualConstraint(var,u),Z*));
			}
			
			/////////////////////////////////////////////////////
			
			// Replace
			AndConstraint(concAnd(X*,eq@EqualConstraint(var@Variable[],s),Y*)) -> {
			//And(concAnd(X*,eq@Equal(var,s),Y*)) -> {
				            
	            Constraint res = (Constraint) MuTraveler.init(
	            		`BottomUp(ReplaceTerm(var,s))).visit(`AndConstraint(concAnd(X*,Y*)));
	            if (res != `AndConstraint(concAnd(X*,Y*))){
	            	return `AndConstraint(concAnd(eq,res));
	            }
	        }
			
			// Replace 2
			OrConstraint(concOr(X*,eq@NEqualConstraint(var@Variable[],s),Y*)) -> {
				            
	            Constraint res = (Constraint) MuTraveler.init(
	            		`BottomUp(ReplaceTerm(var,s))).visit(`OrConstraint(concOr(X*,Y*)));
	            if (res != `OrConstraint(concOr(X*,Y*))){
	            	return `OrConstraint(concOr(eq,res));
	            }
	        }

			

			
		} // end visit
	} // end strategy
	
	/**
	 * Replaces the occurence of all the terms equal with 'variable'
	 * with 'value'
	 */
	%strategy ReplaceTerm(variable:TomTerm,value:TomTerm) extends `Identity(){
		visit TomTerm {
			t ->{
				if (`t == variable){
					return value;
				}
			}
		}// end visit
	}
	
} // end class
