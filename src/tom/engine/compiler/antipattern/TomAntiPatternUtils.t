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

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomexpression.types.expression.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomterm.types.tomlist.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;

import tom.engine.exception.*;
import tom.engine.compiler.*;
import tom.engine.tools.*;
import tom.engine.TomBase;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import jjtraveler.Visitable;

import java.util.*;

/**
 * Class that contains utility functions used for antipattern compilation
 */
public class TomAntiPatternUtils {
	
// ------------------------------------------------------------
	%include { adt/tomsignature/TomSignature.tom }
	%include { mustrategy.tom}
	%include { java/util/ArrayList.tom}
// ------------------------------------------------------------
	
	%typeterm SymbolTable {
		  implement           { SymbolTable }
		  equals(t1,t2)       { (t1.equals(t2)) }
	}
	
	// contains the assignments for each free variable of pattern (if any)	
	private static Instruction varAssignments = null;
	
	/**
	 * Checks to see if the parameter received contains antipatterns
	 * 
	 * @param tomTerm
	 *            The TomTerm to search
	 * @return true if tomTerm contains anti-symbols false otherwise
	 */	
	public static boolean hasAntiTerms(Visitable tomTerm){
		MuStrategy findAnti = `OnceTopDownId(FindAnti());
		if (tomTerm == findAnti.apply(tomTerm)){
			return false;
		}
		return true;    
	}
	
	/**
	 * search an anti symbol
	 */  
	%strategy FindAnti() extends `Identity(){
		visit TomTerm {
			AntiTerm[TomTerm=t] -> { return `t; }
		}		
	}
	
//	/**
//	 * Compiles the anti-pattern matching constraint that it received
//	 * 
//	 * 
//	 * @param constraint 
//	 * @param moduleName
//	 * @param symbolTable
//	 * @param kernelCompiler
//	 * @return compiled expresion
//	 */	
//	public static Expression compileAntiMatchConstraint( Constraint constraint,
//			String moduleName,
//			SymbolTable symbolTable,			
//			TomKernelCompiler kernelCompiler) {
//			
//		Collection quantifiedVarList = new ArrayList();
//		Collection freeVarList = new ArrayList();
//		//		((concTomTerm)((MatchConstraint)constraint).getGlobalFreeVarList()).toArray());
//		
//		TomList tomList = ((MatchConstraint)constraint).getGlobalFreeVarList();
//		while(!tomList.isEmptyconcTomTerm()){
//			freeVarList.add(tomList.getHeadconcTomTerm());
//			tomList = tomList.getTailconcTomTerm();
//		}
//		
//		Constraint transformedProblem = TomAntiPatternTransformNew.transform(
//				`EqualConstraint(((MatchConstraint)constraint).getPattern(),
//						((MatchConstraint)constraint).getSubject()),quantifiedVarList,freeVarList);
//		
//		// launch the antipattern compiler
//		Constraint compiledApProblem = TomAntiPatternCompiler.compile(transformedProblem,quantifiedVarList,freeVarList);					
//			
//		%match(compiledApProblem){
//			TrueConstraint() ->{
//				return `TrueTL();				 
//			}
//			FalseConstraint()->{
//				return `FalseTL();				
//			}
//		}
//		return getTomMappingForConstraint(compiledApProblem,symbolTable,moduleName,kernelCompiler);
//		
//	}
	
	/**
	 * Compiles the anti-pattern matching problem that it received
	 * 
	 * @param action
	 * @param tomTerm
	 * @param rootpath
	 * @param moduleName
	 * @return compiled expresion
	 */	
	public static Expression getAntiPatternMatchExpression(Instruction action,
			TomTerm tomTerm,
			TomNumberList rootpath,
			TomName slotName,
			String moduleName,
			SymbolTable symbolTable,
			Instruction subAction,
			TomKernelCompiler kernelCompiler) {
		
		// resent container 
		varAssignments = null;
				
		// subject to be matched
		TomTerm subject = null;		
		subject = getSubjectVariableAST(tomTerm,rootpath,symbolTable,slotName,moduleName);
		
		// transform the anti-pattern match problem into
		// a disunification one
//		Constraint disunificationProblem = TomAntiPatternTransform.transform(
//				`EqualConstraint(tomTerm,subject));
//		// launch the constraint compiler
//		Constraint compiledApProblem = TomConstraintCompiler.compile(disunificationProblem);
		
		Collection quantifiedVarList = new ArrayList();
		Collection freeVarList = new ArrayList();
		Constraint transformedProblem = TomAntiPatternTransformNew.transform(
				`EqualConstraint(tomTerm,subject),quantifiedVarList,freeVarList);
		
		// launch the antipattern compiler
		Constraint compiledApProblem = TomAntiPatternCompiler.compile(transformedProblem,quantifiedVarList,freeVarList);					
		
		ArrayList variablesList = new ArrayList();
		ArrayList assignedValues = new ArrayList();
		ArrayList constraintsList = new ArrayList();
		ArrayList constraintsListAssign = new ArrayList();
			
		compiledApProblem = (Constraint)`InnermostId(ExtractVariablesAndAssignments(variablesList,assignedValues,constraintsList,					
							constraintsListAssign,symbolTable,moduleName)).apply(compiledApProblem);
				
//		System.out.println("No variables constraint:" + /*TomAntiPatternUtils.formatConstraint(*/compiledApProblem);
		
		// builds the variables' assignment
		if (!variablesList.isEmpty()){
			varAssignments = buildVariableAssignment(variablesList,assignedValues,subAction);
		}
		// builds the assignment for the annotations
		if (!constraintsList.isEmpty()){
			varAssignments = buildVariableAssignment(constraintsList,constraintsListAssign,
					varAssignments == null ? subAction : varAssignments);
		}
		
		%match(compiledApProblem){
			TrueConstraint() ->{
				return `TrueTL();				 
			}
			FalseConstraint()->{
				return `FalseTL();				
			}
		}
		return getTomMappingForConstraint(compiledApProblem,symbolTable,moduleName,kernelCompiler);
		
	}	
	
	/**
	 * returns the AST variable to be matched
	 */
	private static TomTerm getSubjectVariableAST(TomTerm tomTerm, 
			TomNumberList rootpath,
			SymbolTable symbolTable,
			TomName slotName,
			String moduleName) {		
    TomType codomain = TomBase.getTermType(tomTerm,symbolTable);
    TomNumberList path  = `concTomNumber(rootpath*,NameNumber(slotName));
    TomTerm subjectVariableAST =  `Variable(concOption(),PositionName(path),codomain,concConstraint());
        
    return subjectVariableAST;
	}
	
	/**
	 * Extracts the variables and the annotated terms (for the assigments) 
	 */
	%strategy ExtractVariablesAndAssignments(varList:ArrayList,assignedValues:ArrayList,
			constraintsList:ArrayList,constraintsListAssign:ArrayList,symbolTable:SymbolTable,moduleName:String) extends `Identity(){
		
		visit Constraint{
			
			AndConstraint(concAnd(X*,EqualConstraint(v@Variable[Constraints=cons],t),Y*))->{
				varList.add(`v);
				Expression transTerm = transformTerm(`t,symbolTable,moduleName);
				assignedValues.add(transTerm);
				
				if (`cons != null && !`cons.isEmptyconcConstraint()){
					
					TomTerm assignedVar = `((AssignTo)cons.getHeadconcConstraint()).getVariable();
					
					if (!constraintsList.contains(assignedVar)){					
						constraintsList.add(assignedVar);
						constraintsListAssign.add(transTerm);					
					}
				}
				
				return `AndConstraint(concAnd(X*,Y*));
			}
			EqualConstraint(r@RecordAppl[Constraints=cons],t)->{
				
				if (`cons != null && !`cons.isEmptyconcConstraint()){
					
					TomTerm assignedVar = `((AssignTo)cons.getHeadconcConstraint()).getVariable();
					
					if (!constraintsList.contains(assignedVar)){
					
						constraintsList.add(assignedVar);
						if (`t 
								instanceof SymbolOf){
							constraintsListAssign.add(transformTerm(((SymbolOf)`t).getGroundTerm(),symbolTable,moduleName));
						}else{
							constraintsListAssign.add(transformTerm(`t,symbolTable,moduleName));
						}
					}					
				} 
			}
			OrConstraint(concOr(X*,EqualConstraint(v@Variable[Constraints=cons],t),Y*))->{
				varList.add(`v);
				Expression transTerm = transformTerm(`t,symbolTable,moduleName);
				assignedValues.add(transTerm);
				
				if (`cons != null && !`cons.isEmptyconcConstraint()){
					constraintsList.add(`((AssignTo)cons.getHeadconcConstraint()).getVariable());
					constraintsListAssign.add(transTerm);
				}
				
				return `TrueConstraint();
				//return `OrConstraint(concOr(X*,Y*));
			}
		}
	}
	
	private static Instruction buildVariableAssignment(ArrayList varList, 
			ArrayList varValues,			
			Instruction subAction){
		
		if (varList.isEmpty()) {
			return subAction;
		}
		
		TomTerm var = (TomTerm)varList.get(0);
		Expression expr = (Expression)varValues.get(0);
		varList.remove(0);
		varValues.remove(0);
		
		return `Let(var,expr,buildVariableAssignment(varList,varValues,subAction));
	}
	
	private static Expression getTomMappingForConstraint(Constraint c,
			SymbolTable symbolTable,
			String moduleName,
			TomKernelCompiler kernelCompiler) {
		
		%match(c) {
			AndConstraint(concAnd(x))->{
				return getTomMappingForConstraint(`x,symbolTable,moduleName,kernelCompiler);
		    }
			AndConstraint(concAnd(a,b*))->{
				return `And(getTomMappingForConstraint(a,symbolTable,moduleName,kernelCompiler),
						getTomMappingForConstraint(AndConstraint(concAnd(b*)),symbolTable,moduleName,kernelCompiler));
			}
//			AndConstraint(concAnd())->{
//				return `TrueTL();
//			}
			OrConstraint(concOr(a,b*))->{
				return `Or(getTomMappingForConstraint(a,symbolTable,moduleName,kernelCompiler),
						getTomMappingForConstraint(OrConstraint(concOr(b*)),symbolTable,moduleName,kernelCompiler));
			}
			OrConstraint(concOr())->{
				return `FalseTL();
			}
			pattern@(EqualConstraint|NEqualConstraint)(t,SymbolOf(term)) ->{
				
				TomType type = null;
				TomTerm transformedTerm = null;
				
				// if it is a Subterm
				if (`term 
						instanceof Subterm){
					Expression exprTrans = transformTerm(`term,symbolTable,moduleName);
					transformedTerm = `ExpressionToTomTerm(exprTrans);
					type = exprTrans.getCodomain();
				}else{ 
					type = TomBase.getTermType(`term,symbolTable);
					transformedTerm = `term;
				}
				
				Expression result = kernelCompiler.expandDisjunction(`EqualFunctionSymbol(type,transformedTerm,t),moduleName);
				
				return (`pattern 
						instanceof EqualConstraint) ? result : `Negation(result);				
			}
			Neg(constraint) ->{
				return `Negation(getTomMappingForConstraint(constraint,	symbolTable,
						moduleName,kernelCompiler));
							
			}
			pattern@(EqualConstraint|NEqualConstraint)(t1,t2) ->{
				
				Expression transformedT1 = transformTerm(`t1,symbolTable,moduleName);
				Expression transformedT2 = transformTerm(`t2,symbolTable,moduleName);
				
				TomType type = null;
				
				// if the term was transformed in a GetSlot,
				// retreive the type directly 
				if (transformedT1 instanceof GetSlot){
					type = ((GetSlot)transformedT1).getCodomain();
				}else{
					type = TomBase.getTermType(`t1,symbolTable);
				}
				
				// type	should be the same
				if (`pattern 
						instanceof EqualConstraint){
					return `EqualTerm(type,
								ExpressionToTomTerm(transformedT1),
								ExpressionToTomTerm(transformedT2));
				}
				
				return `Negation(EqualTerm(type,
						ExpressionToTomTerm(transformedT1),
						ExpressionToTomTerm(transformedT2)));
			}
			
		}
		
		throw new TomRuntimeException("Strange constraint:" + c);
	}
	
	/**
	 * Transforms from "Subterm" to "GetSlot"
	 * @param t term to transform
	 * @return corresponding "GetSlot"
	 */
	private static Expression transformTerm(TomTerm t, 
			SymbolTable symbolTable,
			String moduleName) {		
		
		%match(t) {			
			
           Subterm(constructorName,slotName, currentTerm)->{                 		        
        	   
        	   // get the transformed term 
        	   Expression transformedTerm = transformTerm(`currentTerm,symbolTable,moduleName);
        	   
        	   // get the type for the subterm
        	   String tomName = null;
        	   if (`constructorName 
        			   instanceof AntiName){
        		   tomName = ((AntiName)`constructorName).getName().getString(); 
        	   }else{
        		   tomName = ((TomName)`constructorName).getString();
        	   } 
        	   TomSymbol tomSymbol = symbolTable.getSymbolFromName(tomName);
        	   TomType subtermType = TomBase.getSlotType(tomSymbol, `slotName);
        	   
        	   TomTerm var = null;
        	   
        	   // if we find just a wrapper, throw it away
        	   var = (transformedTerm instanceof TomTermToExpression) ? transformedTerm.getAstTerm():
        		   `ExpressionToTomTerm(transformedTerm);
        	   
        	   return `GetSlot(subtermType,constructorName,
						slotName.getString(),var);
		   }
           term@(RecordAppl|Variable)[] ->{        	   
        	   return `TomTermToExpression(term);        	   
           }
		}
		throw new TomRuntimeException("Unable to transform term: " + t);
	}
	
	public static boolean containsVariable(Constraint c, TomTerm v){
		try {		
			MuTraveler.init(`OnceTopDown(ConstraintContainsVariable(v))).visit(c);
      return true;
		} catch(jjtraveler.VisitFailure e) {
      return false;
		}
	}
	
	%strategy ConstraintContainsVariable(v:TomTerm) extends `Fail(){
		visit Constraint {
			c@(EqualConstraint|NEqualConstraint)(p,_) -> {
				MuTraveler.init(`OnceTopDown(TermContainsVariable(v))).visit(`p);
        return `c;
			}
		}
	}
	
	%strategy TermContainsVariable(v:TomTerm) extends `Fail(){
		visit TomTerm {
			var@Variable[] ->{
				if (`var == v) {					
					return v;
				}
			}
		}
	}
	
	public static Instruction getVarAssignments(){
		return varAssignments;
	}
	
	/**
	 * Duplication of the method from TomKernelCompiler
	 *  - when it becomes static
	 */
	private Expression expandDisjunction(Expression exp, String moduleName) {
	    Expression cond = `FalseTL();
	    %match(exp) {
	      EqualFunctionSymbol(termType,exp1,RecordAppl[Option=option,NameList=nameList,Slots=l]) -> {
	        while(!`nameList.isEmptyconcTomName()) {
	          TomName name = `nameList.getHeadconcTomName();
	          Expression check = `EqualFunctionSymbol(termType,exp1,RecordAppl(option,concTomName(name),l,concConstraint()));
	          // to mark the symbol as alive
	          //getSymbolTable(moduleName).setUsedSymbolDestructor(name.getString());
	          cond = `Or(check,cond);
	          `nameList = `nameList.getTailconcTomName();
	        }
	      }
	    }
	    return cond;
	  }
	
	/**
	 * Given a constraint, it performs some formatting on it for better 
	 * readability (for debug purpose)
	 * 
	 * @param c 
	 * 			The constraint to format
	 * @return 
	 * 			Formatted constraint
	 */
	public static String formatConstraint(Constraint c){
		
		%match(c) {
			TrueConstraint() -> {
				return "T";	
			}
			FalseConstraint() ->{
				return "F";
			}
			Neg(cons) ->{
				return "Neg(" + formatConstraint(`cons) + ")";
			}
			AndConstraint(concAnd(x,Z*)) ->{
				
				AConstraintList l = `Z*;
				String result = formatConstraint(`x);
				
				while(!l.isEmptyconcAnd()){
					result ="(" + result + " and " + formatConstraint(l.getHeadconcAnd()) +")";
					l = l.getTailconcAnd();
				}
				
				return result; 
			}
			OrConstraint(concOr(x,Z*)) ->{
				
				OConstraintList l = `Z*;
				String result = formatConstraint(`x);
				
				while(!l.isEmptyconcOr()){
					result ="(" + result + " or " + formatConstraint(l.getHeadconcOr()) + ")";
					l = l.getTailconcOr();
				}
				
				return result; 
			}
			EqualConstraint(pattern, subject) ->{
				return formatTerm(`pattern) + "=" + formatTerm(`subject); 
			}
			NEqualConstraint(pattern, subject) ->{
				return formatTerm(`pattern) + "!=" + formatTerm(`subject); 
			}
			Exists(Variable(_,name,_,_),cons) -> {
				return "exists " + `name + ", ( " + formatConstraint(`cons) + " ) "; 
			}			
			ForAll(Variable(_,name,_,_),cons) -> {				
				return "for all " + `name + ", ( " + formatConstraint(`cons) + " ) ";				
			}
//			Match(pattern, subject) ->{
//				return formatTerm(`pattern) + " << " + formatTerm(`subject); 
//			}
		}
		
		return c.toString();
	}
	
	/**
	 * Given a TomTerm, it performs some formatting on it for better 
	 * readability (for debug purpose)
	 * 
	 * @param t 
	 * 			The term to format
	 * @return 
	 * 			Formatted term
	 */
	public static String formatTerm(TomTerm t){
		
		%match(t){
			Variable(_,Name(name),_,_) ->{
				return `name;
			}
			RecordAppl(_,concTomName(Name(name),_*), concSlot(),_)->{
				return `name;
			}
			AntiTerm(apl)->{
				return "!" + formatTerm(`apl);
			}
//			AntiTerm(name)->{
//				return "!" + `name;
//			}
//			GenericGroundTerm(name) ->{
//				return `name;
//			}
			RecordAppl(_,concTomName(Name(name),_*), concSlot(PairSlotAppl(_,x),Z*),_) ->{
				
				SlotList l = `Z*;
				String result = formatTerm(`x);
				
				while(!l.isEmptyconcSlot()){
					result = result + "," + formatTerm(l.getHeadconcSlot().getAppl());
					l = l.getTailconcSlot();
				}
				
				return `name + "(" + result + ")"; 
			}
			
		}
		
		return t.toString();
	}
	
}
