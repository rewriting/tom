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
 * Radu Kopetz e-mail: Radu.Kopetz@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/
package tom.engine.compiler;

import java.util.ArrayList;
import java.util.Iterator;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.generator.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.library.sl.*;


/**
 * This class is in charge with all the pre treatments for generation needed after the propagation process
 * 1. make sure that the constraints are in the good order 
 * 2. translate constraints into expressions 
 * ... 
 */
public class PreGenerator {
	
//------------------------------------------------------------  
  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom}
//------------------------------------------------------------  
 
  public static Expression performPreGenerationTreatment(Constraint constraint) 
       throws VisitFailure{
    constraint = (Constraint)`InnermostId(OrderConstraints()).visit(constraint);    
    return constraintsToExpressions(constraint);
  }
  
  /**
   * Puts the constraints in the good order 
   * 
   */
  %strategy OrderConstraints() extends Identity(){
    visit Constraint {
      /*
       * SwitchSymbolOf
       * 
       *  z <<  subterm(i,g) /\ S /\ f = SymbolOf(g)
       *  -> f = SymbolOf(g) /\ S /\ z <<  subterm(i,g) 
       */
      AndConstraint(X*,subterm@MatchConstraint(_,Subterm[GroundTerm=g]),Y*,symbolOf@MatchConstraint(_,SymbolOf(g)),Z*) -> {
        return `AndConstraint(X*,symbolOf,Y*,subterm,Z*);        
      }
      /*
       * SwitchSymbolOf2
       * 
       * A = SymbolOf(g) /\ S /\ g <<  B ->
       *  g <<  B /\ S /\ A = SymbolOf(g)
       * 
       */
      AndConstraint(X*,first@MatchConstraint(_,SymbolOf(g)),Y*,second@MatchConstraint(g,_),Z*) -> {
        return `AndConstraint(X*,second,Y*,first,Z*);        
      }
      /*
       * SwitchAnti
       * 
       * an antimatch should be always at the end, after the match constraints
       * ex: for f(!x,x) << t -> we should generate x << t_2 /\ !x << t_1 and
       * not !x << t_1 /\ x << t_2 because at the generation the free x should
       * be propagated and not the other one
       */
      AndConstraint(X*,antiMatch@AntiMatchConstraint[],Y*,match@MatchConstraint[],Z*) -> {
        return `AndConstraint(X*,Y*,match,antiMatch,Z*);        
      }
      /*
       * SwitchVar
       * 
       *  p << z /\ S /\ z <<  t -> z << t /\ S /\ p << z
       *  
       *  p << ListHead(z) /\ S /\ z <<  t -> z << t /\ S /\ p << ListHead(z)
       *   
       */
      AndConstraint(X*,first@MatchConstraint(_,rhs@(Variable|VariableStar|ListHead)[]),Y*,second@MatchConstraint(v,_),Z*) -> {
        TomTerm varFound = null;
  match:%match(rhs) {
          var@(Variable|VariableStar)[] -> { varFound = `var;break match; }
          ListHead[Variable=var@(Variable|VariableStar)[]] -> { varFound = `var;}
        }
        if (varFound == `v) { return `AndConstraint(X*,second,Y*,first,Z*); }        
      } 
    } // end visit
  }// end strategy
  
  /**
   * Translates constraints into expressions
   */
  private static Expression constraintsToExpressions(Constraint constraint){    
    %match(constraint){
      AndConstraint(m,X*) -> {        
          return `And(constraintsToExpressions(m),
              constraintsToExpressions(AndConstraint(X*)));
      }      
      OrConstraintDisjunction(m,X*) -> {        
        return `OrExpressionDisjunction(constraintsToExpressions(m),
            constraintsToExpressions(OrConstraintDisjunction(X*)));
      }
      m@MatchConstraint[] -> {        
        return `ConstraintToExpression(m);
      }
      AntiMatchConstraint(constr) -> {
        return `AntiMatchExpression(constraintsToExpressions(constr));
      }
      Negate(c) -> {
        return `Negation(constraintsToExpressions(c));
      }
      EmptyListConstraint(opName,variable) ->{                          
        return ConstraintGenerator.genIsEmptyList(`opName,`variable);
      }
      EmptyArrayConstraint(opName,variable,index) ->{                          
        return `IsEmptyArray(opName,variable,index);
      }
    }                   
    throw new TomRuntimeException("ConstraintGenerator.prepareGeneration - strange constraint:" + constraint);
  }     
}
