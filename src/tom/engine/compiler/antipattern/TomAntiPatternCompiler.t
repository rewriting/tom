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
				
				%match(TomTerm g) {
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
