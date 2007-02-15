package tom.engine.compiler;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.generator.*;

/**
 * This class is in charge with launching all the generators,
 * until no more propagations can be made 
 */
public class TomInstructionGenerationManager extends TomBase {
	
	%include { adt/tomsignature/TomSignature.tom }	
	
	private static final String generatorsPackage = "tom.engine.compiler.generator";
	// the list of all generators
	private static final String[] generatorsNames = {"",""};
	
	private static SymbolTable symbolTable; 
	
	public static Instruction performGenerations(Constraint constraint, SymbolTable symbolTable) 
			throws ClassNotFoundException,InstantiationException,IllegalAccessException{
		
		TomInstructionGenerationManager.symbolTable = symbolTable;
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
		return buildInstructionFromExpression(result);
	}
	
	/**
	 * Prepares the generation phase
	 */
	private static Expression prepareGeneration(Constraint constraint){
		// TODO
		// 1. replace all constraints with ConstraintToExpression 
		// 2. put the variables assignments at the end of conjunction
		return null;
	}
	
	/**
	 * Converts the expression into instructions
	 */
	private static Instruction buildInstructionFromExpression(Expression expression){
		// TODO
		return null;
	}
	
	public static SymbolTable getSymbolTable(){
		return symbolTable;	
	}
}