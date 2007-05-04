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
package tom.engine.compiler.generator;

import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomexpression.types.expression.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomBase;

import tom.engine.compiler.*;
/**
 * Array Generator
 */
public class ArrayGenerator implements IBaseGenerator{

  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom}	

  public Expression generate(Expression expression) {
    try {
      return (Expression)`InnermostId(Generator()).visit(expression);		
    } catch (tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("Unexpected strategy failure!");
    }
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  %strategy Generator() extends Identity() {
    visit Expression{
      // generate pre-loop for X* = or _* = 
      /*
       * do {
       *   ...
       *   end_i++;
       * } while( subjectIndex <= GET_SIZE(subjectList) )
       *
       * *** we need <= instead of < to make the algorithm complete ***
       */
      ConstraintToExpression(MatchConstraint(v@(VariableStar|UnamedVariableStar)[AstType=termType],VariableHeadArray(opName,subject,begin,end))) -> {
        Expression doWhileTest = `Negation(GreaterThan(TomTermToExpression(Ref(end)),GetSize(opName,Ref(subject))));
        // expression at the end of the loop 
        Expression endExpression = `ConstraintToExpression(MatchConstraint(end,ExpressionToTomTerm(AddOne(Ref(end)))));        
        // if we have a varStar, then add its declaration also
        if (`v.isVariableStar()) {
          Expression varDeclaration = `ConstraintToExpression(MatchConstraint(v,ExpressionToTomTerm(
                GetSliceArray(opName,Ref(subject),begin,Ref(end)))));
          return `And(DoWhileExpression(endExpression,doWhileTest),varDeclaration);
        }
        return `DoWhileExpression(endExpression,doWhileTest);		        		      
      }			
      // generate equal - this can come from variable's propagations
      ConstraintToExpression(MatchConstraint(e@ExpressionToTomTerm(GetElement[Codomain=termType]),t)) -> {
        return `EqualTerm(termType,e,t);
      }
    } // end visit

// [pem] GetElement should be modified to handle AU theory, like in VariadicGenerator
  } // end strategy	
}
