/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
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
}
