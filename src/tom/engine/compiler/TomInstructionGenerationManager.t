package tom.engine.compiler;

import java.util.ArrayList;
import java.util.Iterator;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.generator.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;


/**
 * This class is in charge with launching all the generators,
 * until no more propagations can be made 
 */
public class TomInstructionGenerationManager extends TomBase {
	
//	------------------------------------------------------------	
	%include { adt/tomsignature/TomSignature.tom }	
	%include { java/util/types/Collection.tom}
	%include { sl.tom}
//	------------------------------------------------------------	
	
	private static final String generatorsPackage = "tom.engine.compiler.generator.";
	// the list of all generators
	private static final String[] generatorsNames = {"TomSyntacticGenerator"};
	
	public static Instruction performGenerations(Constraint constraint, Instruction action) 
			throws ClassNotFoundException,InstantiationException,IllegalAccessException{
		
		// counts the generators that didn't change the instruction
		short genCounter = 0;
		Expression result = null;
		
		Expression expression = prepareGeneration(constraint);		
		
		// iterate until all propagators are applied and nothing was changed 
		mainLoop: while(true){		
			for(String i:generatorsNames){
				
				TomIBaseGenerator gen = (TomIBaseGenerator)Class.forName(generatorsPackage + i).newInstance();
				result = gen.generate(expression);
				// if nothing was done, start counting 
				if (result == expression){
					genCounter++;
				}else{
					// reset counter
					genCounter = 0;
				}				
				// if we applied all the propagators and nothing changed,
				// it's time to stop
				if (genCounter == generatorsNames.length) { break mainLoop; }
				// reinitialize
				expression = result; 
			}
		} // end while
		return buildInstructionFromExpression(result,action);
	}
	
	/**
	 * Prepares the generation phase
	 * 1. replaces all constraints with ConstraintToExpression 
	 */
	private static Expression prepareGeneration(Constraint constraint){		
		Expression result = null;
		%match(constraint){
			AndConstraint(concConstraint(_*,m@MatchConstraint[],_*)) ->{
				result = (result == null ? `ConstraintToExpression(m) : `And(result,ConstraintToExpression(m)));
			}
			m@MatchConstraint[] ->{
				result = `ConstraintToExpression(m);
			}
			NotEmptyListConstraint(opName,variable) ->{
				result = `Negation(IsEmptyList(opName,variable));
			}
		}			
		return result;
	}	
	
	/**
	 * Converts the resulted expression (after generation) into instructions
	 */
	private static Instruction buildInstructionFromExpression(Expression expression, Instruction action){
		// it is done innermost because the expression is also simplified  
		expression = (Expression)`InnermostId(ReplaceSubterms()).fire(expression);		
		// generate automata
		return generateAutomata(expression,action);
	}
	
	/**
	 * Generates the automata from the expression
	 */
	private static Instruction generateAutomata(Expression expression, Instruction action){
		%match(expression){
			And(left,right) ->{
				Instruction subInstruction = generateAutomata(`right,action);
				return `generateAutomata(left,subInstruction);
			}
			// variables' assignments
			ConstraintToExpression(MatchConstraint(v@(Variable|UnamedVariable|VariableStar|UnamedVariableStar)[],t)) ->{
				return `LetRef(v,TomTermToExpression(t),action);
			}
			// while
			WhileExpression(condition,EqualTerm(type,end,ExpressionToTomTerm(expr))) ->{
				Instruction varAssign = `LetRef(end,expr,Nop());
				return `WhileDo(condition,UnamedBlock(concInstruction(action,varAssign)));
			}
			// 'if' conditions 
			x ->{
				return `If(x,action,Nop());
			}			
		}
		throw new TomRuntimeException("generateAutomata - strange expression:" + expression);
	}
	
	/**
	 * Converts 'Subterm' to 'GetSlot'
	 */
	%strategy ReplaceSubterms() extends Identity(){
		visit TomTerm{
			Subterm(constructorName, slotName, term) ->{
				TomSymbol tomSymbol = TomConstraintCompiler.getSymbolTable().getSymbolFromName(((TomName)`constructorName).getString());
	        	TomType subtermType = TomBase.getSlotType(tomSymbol, `slotName);
				return `ExpressionToTomTerm(GetSlot(subtermType, constructorName, slotName.getString(), term));
			}
		}
	}
}