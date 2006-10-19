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
import tom.engine.adt.tomconstraint.types.constraint.*;
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
		ArrayList replacedTerms = new ArrayList();
		Constraint andAntiCons = `AndAntiConstraint();		
				
		TomAntiPatternTransformNew.symbolTable = symbolTable;
		
		// existing constraints
		ConstraintList constraints = null;
		if(tomTerm instanceof AntiTerm){
			constraints = tomTerm.getTomTerm().getConstraints();
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
				// move the assign constraints from this terms to the variable
				// that replaces it and leave all the other constraints
				ConstraintList assignConstraints = `concConstraint();
				ConstraintList otherConstraints = `concConstraint();
				ConstraintList cList = `t.getConstraints();
				while(!cList.isEmptyconcConstraint()){
					Constraint head = cList.getHeadconcConstraint();
					if (head instanceof AssignTo ){
						assignConstraints = `concConstraint(assignConstraints*,head);
					}else{
						otherConstraints = `concConstraint(otherConstraints*,head);
					}
					cList = cList.getTailconcConstraint(); 
				}
				
				bag.add(`t.setConstraints(otherConstraints));				
				// return the variable with the correct type
				return variable.setAstType(TomBase.getTermType(`t,symbolTable)).setConstraints(assignConstraints);				
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
