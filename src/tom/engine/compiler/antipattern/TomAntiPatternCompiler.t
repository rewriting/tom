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

import aterm.*;
import aterm.pure.*;

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

import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.VisitFailure;

public class TomAntiPatternCompiler{
	
//	------------------------------------------------------------
	%include { adt/tomconstraint/TomConstraint.tom }
	%include { mustrategy.tom}	
	%include { java/util/types/Collection.tom}	
//	------------------------------------------------------------
	
	
	public static Constraint compile(Constraint c, Collection quantifiedVarList,
			Collection freeVarList) {
		
		Constraint classicalMatch = null; 
		Constraint replacedVariables = null;
		Constraint quantifierFree = null;
		Constraint optimizedCode = null;
		Constraint result = null;		
		
		classicalMatch = (Constraint)`InnermostId(ClassicalPatternMatching()).apply(c);
//		System.out.println("After classical match: " +  tools.formatConstraint(classicalMatch));
		replacedVariables = (Constraint)`TopDown(ReplaceVariables()).apply(classicalMatch);
//		System.out.println("After variable replacement: " +  tools.formatConstraint(replacedVariables));
		quantifierFree = (Constraint)`TopDown(EliminateQuantifiedVars(quantifiedVarList,freeVarList)).apply(replacedVariables);
//		System.out.println("After quantified vars' elimination: " +  tools.formatConstraint(quantifierFree));
		optimizedCode = (Constraint)`TopDown(ReplaceEquation()).apply(quantifierFree);			
//		System.out.println("After optimization: " +  tools.formatConstraint(optimizedCode));
		result = (Constraint)`InnermostId(Cleaning()).apply(optimizedCode);			
		
//		System.out.println("Final result: " + result);
		
		return result;
	}	
	
	
	%strategy ClassicalPatternMatching() extends `Identity(){
		
		visit Constraint{
			
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
			
			// Merge
			AndConstraint(concAnd(X*,eq@EqualConstraint(Variable[AstName=z],t),Y*,EqualConstraint(Variable[AstName=z],u),Z*)) ->{				
				return `AndConstraint(concAnd(X*,eq,Y*,EqualConstraint(t,u),Z*));
			}
			
			// Delete
			EqualConstraint(a,a) ->{				
				return `TrueConstraint();
			}
			
			// SymbolClash
			EqualConstraint(RecordAppl[NameList=name1],RecordAppl[NameList=name2]) -> {
				if(`name1 != `name2) {					
					return `FalseConstraint();
				}
			}
			
			// PropagateClash
			AndConstraint(concAnd(_*,FalseConstraint(),_*)) -> {
				return `FalseConstraint();
			}		
			
			// PropagateSuccess
			AndConstraint(concAnd(X*,TrueConstraint(),Y*)) -> {
				return `AndConstraint(concAnd(X*,Y*));
			}
			
			// clean
			AndConstraint(concAnd()) -> {
				return `TrueConstraint();
			}
			AndConstraint(concAnd(t)) -> {
				return `t;
			}
			AndConstraint(concAnd(X*,AndConstraint(concAnd(Y*)),Z*)) ->{
				return `AndConstraint(concAnd(X*,Y*,Z*));
			}
		}
	}	
	
	%strategy ReplaceVariables() extends `Identity(){
		
		visit Constraint{
			// Replace 
			AndConstraint(concAnd(X*,eq@EqualConstraint(var@Variable[],s),Y*)) -> {
				
				Constraint consToSearchIn = `AndConstraint(concAnd(X*,Y*));
				
				Constraint res = (Constraint)`BottomUp(Replace(var,s,null)).apply(consToSearchIn);
				// if we replaced something
				if (res != consToSearchIn){
					return `AndConstraint(concAnd(eq,res));
				}
			}
		}
	}
	
	// replaces all equalities that contain 
	// quantified variables with true 
	%strategy EliminateQuantifiedVars(quantifiedVarList:Collection,
			freeVarList:Collection) extends `Identity(){
		
		visit Constraint{
			EqualConstraint(var@Variable[],s) -> {				
				if (quantifiedVarList.contains(`var) 
						&& !freeVarList.contains(`var)){
					return `TrueConstraint();
				}            
			}
		}
	}
	
	%strategy Cleaning() extends `Identity(){
		
		visit Constraint{
			
			// Delete - equalities created by replace
			EqualConstraint(a,a) ->{
				return `TrueConstraint();
			}
			
			// SymbolClash
			EqualConstraint(RecordAppl[NameList=name1],RecordAppl[NameList=name2]) -> {
				if(`name1 != `name2) {
					return `FalseConstraint();
				}
			}
			
			// clean		
			AndConstraint(concAnd()) -> {
				return `TrueConstraint();
			}
			
			// clean
			Neg(TrueConstraint()) ->{
				return `FalseConstraint();
			}
			
			// clean
			Neg(FalseConstraint()) ->{
				return `TrueConstraint();
			}
			
			// PropagateClash
			AndConstraint(concAnd(_*,FalseConstraint(),_*)) -> {
				return `FalseConstraint();
			}		
			
			// PropagateSuccess
			AndConstraint(concAnd(X*,TrueConstraint(),Y*)) -> {
				return `AndConstraint(concAnd(X*,Y*));
			}
		}
		
	}
	
	
	%strategy Replace(variable:TomTerm,value:TomTerm,constraint:Constraint) extends `Identity(){
		visit TomTerm {
			t ->{
				if (`t == variable){
					return value;
				}
			}
		}// end visit
		
		visit Constraint {
			t ->{
				if (`t == constraint){
					return `TrueConstraint();
				}
			}
		}// end visit
	}
		
	%strategy ReplaceEquation() extends `Identity(){
		
		visit Constraint {		
			AndConstraint(concAnd(eq@EqualConstraint[],Y*)) -> {
				
				Constraint consToSearchIn = `AndConstraint(concAnd(Y*));
				
				Constraint res = (Constraint)`TopDown(Replace(null,null,eq)).apply(consToSearchIn);
				// if we replaced something
				if (res != consToSearchIn){
					return `AndConstraint(concAnd(eq,res));
				}
			}
		} // end visit
	}
	
} // end class
