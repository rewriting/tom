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
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.exception.*;
import tom.engine.tools.SymbolTable;
import tom.engine.TomBase;

import tom.library.strategy.mutraveler.MuTraveler;

import jjtraveler.reflective.VisitableVisitor;
import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.VisitFailure;

/**
 * Contains methods for transforming an anti-pattern problem into a
 * disunification one
 */
public class TomAntiPatternTransformNew {	
	
//	------------------------------------------------------------
	%include { adt/tomsignature/TomSignature.tom }
	%include { mustrategy.tom}	
	%include { java/util/types/Collection.tom}	
//	------------------------------------------------------------
	
	// TODO - change this
	// flag that specifies if the action will be performed on the 'if then' 
	// or on the 'else' branch
	// - if one not is above, than action is on 'else'; if two, action on 'if then' 
	// and so on
	private static int actionOnIf = 0;
	private static int varCounter = 0;
	private static final String FRESH_VAR_NAME = "_tom_fresh_var_";
	private static SymbolTable symbolTable = null;
	
	/**
	 * for the given term, abstracts all anti terms contained
	 * 
	 * @return a term in which all the anti terms are abstracted with variables
	 * 			and with one match constraint for each anti-term that was abstracted 
	 */
	public static TomTerm getConstrainedTerm(TomTerm tomTerm,
			SymbolTable symbolTable){
		
		TomTerm termAntiReplaced = null;		
		ArrayList globalFreeVarList = new ArrayList();
		TomList tomGlobalFreeVarList = `concTomTerm();
		ArrayList replacedTerms = new ArrayList();
		Constraint andAntiCons = `AndAntiConstraint();
		int isList = 0;
				
		TomAntiPatternTransformNew.symbolTable = symbolTable;
		
		// existing constraints
		ConstraintList constraints = null;
		if(tomTerm instanceof AntiTerm){
			constraints = `concConstraint(); // no constraints for a term that begins with an anti
		}else{
			constraints = tomTerm.getConstraints();
		}
		
		int termLine = 0;
		String fileName = null;
		
		// get the file name and line number
		TomTerm tmpTomTerm = tomTerm;		
		if (tmpTomTerm instanceof AntiTerm){
			tmpTomTerm = ((AntiTerm)tmpTomTerm).getTomTerm();
		}		
		%match(tmpTomTerm){
			(Variable|RecordAppl)[Option = concOption(_*,OriginTracking[Line=termLine,FileName=fileName],_*)] ->{
				termLine = `termLine;
				fileName = `fileName;
			}			
		}
		
		//System.out.println("Entered with: " + tomTerm);
		
		while(true){		
			// get the term with a variable instead of anti
			String varName = FRESH_VAR_NAME + (varCounter++);
			TomTerm abstractVariable = `Variable(concOption(OriginTracking(Name(varName),
					termLine,fileName)),Name(varName),EmptyType(),concConstraint());		
			termAntiReplaced = (TomTerm) `OnceTopDownId(AbstractTerm(abstractVariable,replacedTerms)).apply(tomTerm);
			// if nothing was done
			if (termAntiReplaced == tomTerm){
				break;
			}			
			TomTerm replacedTerm = (TomTerm)replacedTerms.get(0);			
			// give the variable the correct type
			TomType type = TomBase.getTermType(replacedTerm,symbolTable);
			abstractVariable = abstractVariable.setAstType(type);
			// see if it is a list			
			TomSymbol symbol = null;
			match:%match(replacedTerm){
				RecordAppl[NameList=(Name(tomName),_*)] ->{
					symbol = symbolTable.getSymbolFromName(`tomName);
					break match;
				}
				Variable[AstName=Name(tomName)]->{
					symbol = symbolTable.getSymbolFromName(`tomName);
					break match;
				}
				x ->{
					System.out.println("No symbol can be detected for:" + `x);
				}
			}			
			isList = TomBase.isListOperator(symbol) ? 1:0;
			System.out.println("IsList:" + isList);
			
			// add the new anti constraint
			andAntiCons = `AndAntiConstraint(andAntiCons*,
					AntiMatchConstraint(replacedTerm,abstractVariable,actionOnIf));
			
			// reinitialize
			replacedTerms.clear();
			tomTerm = termAntiReplaced;
		}		
				
		// add the newly created constraints
		if (!andAntiCons.isEmptyAndAntiConstraint()){
			andAntiCons = andAntiCons.reverse();
			constraints = `concConstraint(constraints*,andAntiCons);
		}
		termAntiReplaced = termAntiReplaced.setConstraints(constraints);
		
		//System.out.println("Finished with: " + termAntiReplaced);
		
		// change the level
		actionOnIf = actionOnIf == 0 ? 1:0;
		
		return termAntiReplaced; 		
	}
	
	
	/**
	 * transforms the anti-pattern problem received 
	 * in order to eliminate the anti symbols
	 * 
	 * @param c
	 *            the anti-pattern problem to transform
	 * @return corresponding disunification problem
	 */
	public static Constraint transform(Constraint c, Collection quantifiedVarList,
			Collection freeVarList) {	

		// replace all unamed variables by named ones
		c = (Constraint)`TopDown(ReplaceUnamedVariables()).apply(c);
		
		// get the free variables of the pattern
		`TopDownCollect(CollectPositiveVariable(freeVarList)).apply(c);
		
		// eliminate anti
		return applyMainRule(c, quantifiedVarList);	
	}
	
	/**
	 * applies the main rule that transforms ap problems into dis-unification
	 * ones
	 */
	private static Constraint applyMainRule(Constraint c, Collection quantifiedVarList) {
		/*
		 * to improve the efficiency, we should first, abstract anti symbols,
		 * and get cAntiReplaced store the tuple (abstractedTerm, variable)
		 * during the abstraction then, re-instantiate the abstractedVariables
		 * to deduce cNoAnti this would avoid the double recursive traversal
		 */
		
		// first get the constraint without the anti
		Constraint cNoAnti = (Constraint) `OnceTopDownId(ElimAnti()).apply(c);
		// if nothing changed, time to exit
		if(cNoAnti == c) {
			return c;
		} 
		cNoAnti = `Neg(applyMainRule(cNoAnti,quantifiedVarList));
		
		/*
		 * find an Anti(...) an then collect (under this Anti) variables which
		 * are not under another Anti 
		 * TODO: this is strange since we do not
		 * collect variables which are in other branches
		 */
		`OnceTopDownId(SequenceId(ElimAnti(),TopDownCollect(CollectPositiveVariable(quantifiedVarList)))).apply(c);
		
		// System.out.println("quantifiedVarList = " + quantifiedVarList);				
		
		// get the constraint with a variable instead of anti
		String varName = FRESH_VAR_NAME + (varCounter++);
		TomTerm abstractVariable = `Variable(concOption(),Name(varName),EmptyType(),concConstraint());
		// we need to have all quantified variables stored in the list 
		quantifiedVarList.add(abstractVariable);

		ArrayList tmp = new ArrayList();
		
		Constraint cAntiReplaced = (Constraint) `OnceTopDownId(AbstractTerm(abstractVariable, tmp)).apply(c);
		cAntiReplaced = applyMainRule(cAntiReplaced,quantifiedVarList);
		
		return `AndConstraint(concAnd(cAntiReplaced,cNoAnti));
	}
	
	// collect variables, a do not inspect under an AntiTerm
	%strategy CollectPositiveVariable(bag:Collection) extends `Identity() {		
		visit TomTerm {
			AntiTerm[] -> {
				throw new VisitFailure();
			}
			
			v@Variable[] -> {
				if (!bag.contains(`v)){
					bag.add(`v);
				}
				throw new VisitFailure();
			}
		}
	}

	// replaces all unamed variables with named ones
	%strategy ReplaceUnamedVariables() extends `Identity(){		
		visit TomTerm {		 
			UnamedVariable(option,astType,constraints) -> { 
				return `Variable(option,Name(FRESH_VAR_NAME + (varCounter++)),astType,constraints); 
			}
		}
	}
	
	
	// remove an anti-symbol
	%strategy ElimAnti() extends `Identity(){		
		visit TomTerm {		 
			AntiTerm(p) -> { return `p; }
		}
	}
	
	// replace a term by another (a variable)
	%strategy AbstractTerm(variable:TomTerm, bag:Collection) extends `Identity() {
		visit TomTerm {
			AntiTerm(t) -> { 
				bag.add(`t);				
				// return the variable with the correct type
				return variable.setAstType(TomBase.getTermType(`t,symbolTable)); 
			}
		}
	}	

	public static int getActionOnIf(){
		return actionOnIf;
	}
	
	public static void initialize(){
		actionOnIf = 0;
		varCounter = 0;
	}
} // end class
