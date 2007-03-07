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
public class TomVariadicGenerator implements TomIBaseGenerator{
	
	%include { adt/tomsignature/TomSignature.tom }
	%include { sl.tom }	
	
	public Expression generate(Expression expression){		
		return (Expression)`TopDown(VariadicGenerator()).fire(expression);		
	}

	// If we find ConstraintToExpression it means that this constraint was not processed	
	%strategy VariadicGenerator() extends Identity(){
		visit Expression{
			// generate pre-loop for X* = or _* = 
			ConstraintToExpression(MatchConstraint(v@(VariableStar|UnamedVariableStar)[],VariableHeadList(opName,begin,end@VariableStar[AstType=type]))) ->{
				Expression doWhileTest = `Negation(EqualTerm(type,end,begin));//`Negation(IsEmptyList(opName,end));
				Expression endExpression = `IfExpression(IsEmptyList(opName,end),EqualTerm(type,end,begin),
						EqualTerm(type,end,ExpressionToTomTerm(GetTail(opName,end))));				
				if ((`v) instanceof VariableStar){
					Expression varDeclaration = `ConstraintToExpression(MatchConstraint(v,ExpressionToTomTerm(GetSliceList(opName,begin,end))));
					return `And(DoWhileExpression(endExpression,doWhileTest),varDeclaration);
				}
				return `DoWhileExpression(endExpression,doWhileTest);		        		      
			}			
			// generate equal
			ConstraintToExpression(MatchConstraint(e@ExpressionToTomTerm(GetHead[Codomain=type]),t)) ->{				
				return `EqualTerm(type,e,t);
			}
			ConstraintToExpression(MatchConstraint(TestVarStar(v@VariableStar[AstType=type]),t)) ->{
				return `EqualTerm(type,v,t);
			}
		} // end visit
	} // end strategy	
}