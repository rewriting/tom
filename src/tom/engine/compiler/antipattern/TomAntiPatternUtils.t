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

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomexpression.types.expression.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.library.strategy.mutraveler.MuTraveler;

import tom.engine.exception.*;
import tom.engine.compiler.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

import java.util.*;

/**
 * Class that contains utility functions used for antipattern compilation
 */
public class TomAntiPatternUtils {
	
// ------------------------------------------------------------
	%include { adt/tomsignature/TomSignature.tom }
	%include { mutraveler.tom}
	%include { java/util/ArrayList.tom}
// ------------------------------------------------------------
	
	%typeterm TomKernelCompiler {
		  implement           { TomKernelCompiler }
		  equals(t1,t2)       { (t1.equals(t2)) }
	}
	
	// helping variables used inside strategies
	private static boolean foundVariable = false;
	private static int antiCounter = 0;
	
	// contains the assignments for each free variable of pattern (if any)	
	public static Instruction varAssignments = null;
	
	/**
	 * Checks to see if the parameter received contains antipatterns
	 * 
	 * @param tomTerm
	 *            The TomTerm to search
	 * @return true if tomTerm contains anti-symbols false otherwise
	 */	
	public static boolean hasAntiTerms(TomTerm tomTerm){
		
		antiCounter = 0;
		
		try{		
			VisitableVisitor countAnti = `OnceTopDownId(CountAnti());
			MuTraveler.init(countAnti).visit(tomTerm);
		}catch(Exception e){
			throw new TomRuntimeException("Cannot count the number of anti symbols in : " + tomTerm 
					+ "\n Exception:" + e.getMessage());
		}
		
		return (antiCounter > 0 ? true:false);
	}
	
	/**
	 * counts the anti symbols
	 */  
	%strategy CountAnti() extends `Identity(){
		visit TomTerm {
			AntiTerm(t) -> {
				antiCounter++;
				return `t;
			}
		}
	}
	
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
			String moduleName,
			TomKernelCompiler compiler) {
		
//		System.out.println("Action:" + action);
//		System.out.println("TomTerm:" + tomTerm);
//		System.out.println("RootPath:" + rootpath);
//		System.out.println("ModuleName:" + moduleName);
		
		// resent container 
		varAssignments = null;
		
		// subject to be matched
		TomTerm subject = null;		
		
		// extract the subject
		// TODO - the guards
		%match(Instruction action){
			TypedAction(_,Pattern(concTomTerm(subject,_*),_,_),_)->{
				subject = `subject;				
			}
		}		
		
		// transform the anti-pattern match problem into
		// a disunification one
		Constraint disunificationProblem = TomAntiPatternTransform.transform(
				`EqualConstraint(tomTerm,subject));
		// launch the constraint compiler
		Constraint compiledApProblem = TomConstraintCompiler.compile(disunificationProblem);		
		
		ArrayList variablesList = new ArrayList();
		ArrayList assignedValues = new ArrayList();
		try{		
			compiledApProblem = (Constraint)MuTraveler.init(`InnermostId(
					ExtractVariables(variablesList,assignedValues,compiler,moduleName))).visit(compiledApProblem);
		}catch(VisitFailure e){
			throw new RuntimeException("VisitFailure occured:" + e);
		}
		
//		System.out.println("No variables constraint:" + TomAntiPatternUtils.formatConstraint(compiledApProblem));
		
		// builds the assignment
		if (!variablesList.isEmpty()){
			varAssignments = buildVariableAssignment(variablesList,assignedValues);
		}
		
		%match(Constraint compiledApProblem){
			TrueConstraint() ->{
				return `TrueTL();				 
			}
			FalseConstraint()->{
				return `FalseTL();				
			}
		}
		return getTomMappingForConstraint(compiledApProblem,compiler,moduleName);
		
	}	
	
	%strategy ExtractVariables(varList:ArrayList,assignedValues:ArrayList,
			compiler:TomKernelCompiler,moduleName:String) extends `Identity(){
		
		visit Constraint{
			
			AndConstraint(concAnd(X*,EqualConstraint(v@Variable[],t),Y*))->{
				varList.add(`v);
				assignedValues.add(transformTerm(`t,compiler,moduleName));
				return `AndConstraint(concAnd(X*,Y*));
			}
			OrConstraint(concOr(X*,EqualConstraint(v@Variable[],t),Y*))->{
				varList.add(`v);
				assignedValues.add(transformTerm(`t,compiler,moduleName));
				return `TrueConstraint();
				//return `OrConstraint(concOr(X*,Y*));
			}
		}
	}
	
	private static Instruction buildVariableAssignment(ArrayList varList, ArrayList varValues){
		
		if (varList.isEmpty()) return `Nop();
		
		TomTerm var = (TomTerm)varList.get(0);
		Expression expr = (Expression)varValues.get(0);
		varList.remove(0);
		varValues.remove(0);
		
		return `Let(var,expr,buildVariableAssignment(varList,varValues));
	}
	
	private static Expression getTomMappingForConstraint(Constraint c,
			TomKernelCompiler compiler,
			String moduleName) {
		
		%match(Constraint c) {
			AndConstraint(concAnd(a,b*))->{
				return `And(getTomMappingForConstraint(a,compiler,moduleName),
						getTomMappingForConstraint(AndConstraint(concAnd(b*)),compiler,moduleName));
			}
			AndConstraint(concAnd())->{
				return `TrueTL();
			}
			OrConstraint(concOr(a,b*))->{
				return `Or(getTomMappingForConstraint(a,compiler,moduleName),
						getTomMappingForConstraint(OrConstraint(concOr(b*)),compiler,moduleName));
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
					Expression exprTrans = transformTerm(`term,compiler,moduleName);
					transformedTerm = `ExpressionToTomTerm(exprTrans);
					type = exprTrans.getCodomain();
				}else{ 
					type = compiler.getTermType(`term,moduleName);
					transformedTerm = `term;
				}
				
				return (`pattern 
						instanceof EqualConstraint) ? `EqualFunctionSymbol(type,transformedTerm,t)
						: `Negation(EqualFunctionSymbol(type,transformedTerm,t));				
			}
			pattern@(EqualConstraint|NEqualConstraint)(t1,t2) ->{
				
				Expression transformedT1 = transformTerm(`t1,compiler,moduleName);
				Expression transformedT2 = transformTerm(`t2,compiler,moduleName);
				// type	should be the same
				if (`pattern 
						instanceof EqualConstraint){
					return `EqualTerm(compiler.getTermType(t1,moduleName),
								ExpressionToTomTerm(transformedT1),
								ExpressionToTomTerm(transformedT2));
				}
				
				return `Negation(EqualTerm(compiler.getTermType(t1,moduleName),
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
			TomKernelCompiler compiler,
			String moduleName) {
		
		%match(TomTerm t) {
			
           Subterm(constructorName,slotName, currentTerm)->{                 		        
          
        	   // get the transformed term 
        	   Expression transformedTerm = transformTerm(`currentTerm,compiler,moduleName);
        	   
        	   // get the type for the subterm        	           	   
        	   TomType subtermType = compiler.getSlotType(`constructorName.getString(),`slotName,moduleName);
        	   
        	   TomTerm var = null;
        	   
        	   // if we find just a wrapper, throw it away
        	   var = (transformedTerm instanceof TomTermToExpression) ? transformedTerm.getAstTerm():
        		   `ExpressionToTomTerm(transformedTerm);
        	   
        	   return `GetSlot(subtermType,constructorName,
						slotName.getString(),var);
		   }
           term@(RecordAppl|TLVar)[] ->{
        	   return `TomTermToExpression(term);
           }
		}
		throw new TomRuntimeException("Unable to transform term: " + t);
	}
	
	public static boolean containsVariable(Constraint c, TomTerm v){
		
		foundVariable = false;
		
		try{		
			MuTraveler.init(`InnermostId(ConstraintContainsVariable(v))).visit(c);
		}catch(VisitFailure e){
			throw new RuntimeException("VisitFailure occured:" + e);
		}
		
		return foundVariable;
	}
	
	%strategy ConstraintContainsVariable(v:TomTerm) extends `Identity(){
		
		visit Constraint {
			EqualConstraint(p,_) ->{
				MuTraveler.init(`InnermostId(TermContainsVariable(v))).visit(`p);
			}
			NEqualConstraint(p,_) ->{
				MuTraveler.init(`InnermostId(TermContainsVariable(v))).visit(`p);
			}
		}
	}
	
	%strategy TermContainsVariable(v:TomTerm) extends `Identity(){
		
		visit TomTerm {
			var@Variable(_,_,_,_) ->{
				
				if (`var == v){					
					foundVariable = true;
				}
			}
		}
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
		
		%match(Constraint c) {
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
		
		%match(TomTerm t){
			Variable(_,Name(name),_,_) ->{
				return `name;
			}
			RecordAppl(_,concTomName(Name(name),_*), concSlot(),_)->{
				return `name;
			}
			AntiTerm(apl@RecordAppl(_,_,_,_))->{
				return "!" + formatTerm(`apl);
			}
			AntiTerm(name)->{
				return "!" + `name;
			}
//			GenericGroundTerm(name) ->{
//				return `name;
//			}
			RecordAppl(_,name, concSlot(PairSlotAppl(_,x),Z*),_) ->{
				
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
