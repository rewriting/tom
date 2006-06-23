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
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.library.strategy.mutraveler.MuTraveler;

import tom.engine.exception.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * Class that contains utility functions used for antipattern compilation
 */
public class TomAntiPatternUtils{
	
//	------------------------------------------------------------
	%include { adt/tomsignature/TomSignature.tom }
	%include { mutraveler.tom}
//	------------------------------------------------------------
	
	private static int antiCounter = 0;
	
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
	public static Expression getAntiPatternMatchInstruction(Instruction action,
			TomTerm tomTerm,
			TomNumberList rootpath,
			String moduleName) {
		
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
//		Contraint compiledApProblem = TomConstraintCompiler.compile(disunificationProblem);
		
//		return EqualTrueAntiPatternMatch("functionToBeCalled", compiledApProblem);
		
		System.out.println("Action:" + action);
		System.out.println("TomTerm:" + tomTerm);
		System.out.println("RootPath:" + rootpath);
		System.out.println("ModuleName:" + moduleName);
		
		return null;
	}
}