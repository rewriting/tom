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
 * Contains methods for transforming an anti-pattern problem into a disunification one
 */
public class TomAntiPatternTransform {	

//	------------------------------------------------------------
//	%include { adt/tomconstraint/TomConstraint.tom }
  %include { adt/tomsignature/TomSignature.tom }
	%include { mustrategy.tom}	
	%include { java/util/types/Collection.tom}	
//	------------------------------------------------------------

	private static int varCounter = 0;
	
	/**
	 * transforms the anti-pattern problem received into a disunification one
	 * @param c the anti-pattern problem to transform 
	 * @return corresponding disunification problem 
	 */
	public static Constraint transform(Constraint c) {	
    // eliminate anti
    Constraint noAnti = applyMainRule(c);    
    // transform the problem into a disunification one		
    return (Constraint) `InnermostId(TransformIntoDisunification()).apply(noAnti);			
	}	
	
	/**
   * applies the main rule that transforms ap problems
   * into dis-unification ones
   */
  private static Constraint applyMainRule(Constraint c) {
    /*
     * to improve the efficiency, we should
     * first, abstract anti symbols, and get cAntiReplaced
     * store the tuple (abstractedTerm, variable) during the abstraction
     * then, re-instantiate the abstractedVariables to deduce cNoAnti
     * this would avoid the double recursive traversal
     */
	
    // first get the constraint without the anti
    Constraint cNoAnti = (Constraint) `OnceTopDownId(ElimAnti()).apply(c);
    // if nothing changed, time to exit
    if(cNoAnti == c) {
      return c;
    } 
    cNoAnti = `Neg(applyMainRule(cNoAnti));

    /*
     * find an Anti(...)
     * an then collect (under this Anti) variables which are not under another Anti
     * TODO: this is strange since we do not collect variables which are in other branches
     */
    Collection quantifiedVarList = new ArrayList();
    `OnceTopDownId(SequenceId(ElimAnti(),TopDownCollect(CollectPositiveVariable(quantifiedVarList)))).apply(c);

    //`OnceTopDownId(_Anti(TopDownCollect(CollectPositiveVariable(quantifiedVarList)))).apply(c);

    //System.out.println("quantifiedVarList = " + quantifiedVarList);
    Iterator it = quantifiedVarList.iterator();
    while(it.hasNext()) {
      TomTerm t = (TomTerm)it.next();
      cNoAnti = `ForAll(t,cNoAnti);
    }

    // get the constraint with a variable instead of anti
    String varName = "v" + (varCounter++);
    TomTerm abstractVariable = `Variable(concOption(),Name(varName),EmptyType(),concConstraint());
    //Constraint cAntiReplaced = (Constraint) `OnceTopDownId(SequenceId(ElimAnti(),AbstractTerm(abstractVariable))).apply(c);
    Constraint cAntiReplaced = (Constraint) `OnceTopDownId(AbstractTerm(abstractVariable)).apply(c);
    cAntiReplaced = applyMainRule(cAntiReplaced);

    return `AndConstraint(concAnd(Exists(abstractVariable,cAntiReplaced),cNoAnti));
  }

  // collect variables, a do not inspect under an AntiTerm
	%strategy CollectPositiveVariable(bag:Collection) extends `Identity() {		
    visit TomTerm {
      AntiTerm[] -> {
        throw new VisitFailure();
      }

      v@Variable[] -> {
        bag.add(`v);
        throw new VisitFailure();
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
	%strategy AbstractTerm(variable:TomTerm) extends `Identity() {
		visit TomTerm {
			AntiTerm[] -> { return variable; }
		}
	}
	
	// applies symple logic rules for eliminating 
	// the not and thus creating a real disunification problem
	%strategy TransformIntoDisunification() extends `Identity(){
		visit Constraint {
			// some simplification stuff
			Exists(a,e@Exists(a,b)) ->{ return `e; }
			
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
			Neg(Exists(v,c)) -> { return `ForAll(v,Neg(c)); }
			
			// replace Neg(ForAll) with Exists
			Neg(ForAll(v,c)) -> { return `Exists(v,Neg(c)); }
		}
	}		
} // end class
