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
 * Variadic Generator
 */
public class TomArrayGenerator implements TomIBaseGenerator{

  %include { adt/tomsignature/TomSignature.tom }
  %include { sl.tom }	

  public Expression generate(Expression expression){		
    return (Expression)`InnermostId(ArrayGenerator()).fire(expression);		
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  %strategy ArrayGenerator() extends Identity(){
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
      ConstraintToExpression(MatchConstraint(v@(VariableStar|UnamedVariableStar)[AstType=termType],VariableHeadArray(opName,subject,begin,end))) ->{        
        Expression doWhileTest = `Negation(GreaterThan(TomTermToExpression(Ref(end)),GetSize(opName,Ref(subject))));
        // expression at the end of the loop 
        Expression endExpression = `ConstraintToExpression(MatchConstraint(end,ExpressionToTomTerm(AddOne(Ref(end)))));        
        // if we have a varStar, then add its declaration also
        if (`v.isVariableStar()){
          Expression varDeclaration = `ConstraintToExpression(MatchConstraint(v,ExpressionToTomTerm(
                GetSliceArray(opName,Ref(subject),begin,Ref(end)))));
          return `And(DoWhileExpression(endExpression,doWhileTest),varDeclaration);
        }
        return `DoWhileExpression(endExpression,doWhileTest);		        		      
      }			
      // generate equal - this can come from variable's propagations
      ConstraintToExpression(MatchConstraint(e@ExpressionToTomTerm(GetElement[Codomain=termType]),t)) ->{				
        return `EqualTerm(termType,e,t);
      }
    } // end visit
  } // end strategy	
}
