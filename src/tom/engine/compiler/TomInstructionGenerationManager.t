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
	
	private static SymbolTable symbolTable; 
	
	public static Instruction performGenerations(Constraint constraint, Instruction action, SymbolTable symbolTable) 
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
		}			
		return result;
	}	
	
	/**
	 * Converts the resulted expression (after generation) into instructions
	 */
	private static Instruction buildInstructionFromExpression(Expression expression, Instruction action){

		// collect all the variables and generate declarations
		ArrayList vars = new ArrayList();
		ArrayList varsValues = new ArrayList();
		System.out.println("Expresion before:" + `expression);
		// it is done innermost because the expression is also simplified  
		expression = (Expression)`InnermostId(CollectVariables(vars,varsValues)).fire(expression);
		System.out.println("Expresion after:" + `expression);
		action = genVariablesDeclaration(vars,varsValues,action);
		
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
				return `If(left,subInstruction,Nop());
			}			
			x ->{
				return `If(x,action,Nop());
			}			
		}
		throw new TomRuntimeException("generateAutomata - strange expression:" + expression);
	}
	
	/**
	 * 1. Collects all the variables, and eliminates them from expression
	 * 2. Converts 'Subterm' to 'GetSlot'
	 */
	%strategy CollectVariables(vars:Collection,varsValues:Collection) extends Identity(){
		visit Expression{
			And(ConstraintToExpression(MatchConstraint(v@Variable[],t)),r) ->{
				vars.add(`v);
				varsValues.add(`TomTermToExpression(t));
				return `r;
			}
			And(l,ConstraintToExpression(MatchConstraint(v@Variable[],t))) ->{
				vars.add(`v);
				varsValues.add(`TomTermToExpression(t));
				return `l;
			}			
		}// end visit
		
		visit TomTerm{
			Subterm(constructorName, slotName, term) ->{
				TomSymbol tomSymbol = symbolTable.getSymbolFromName(((TomName)`constructorName).getString());
	        	TomType subtermType = TomBase.getSlotType(tomSymbol, `slotName);
				return `ExpressionToTomTerm(GetSlot(subtermType, constructorName, slotName.getString(), term));
			}
		}
	}
	
	/**
	 * Generates variables' declarations
	 */
	private static Instruction genVariablesDeclaration(ArrayList vars, ArrayList varsValues, Instruction action){
		Iterator vvIt = varsValues.iterator();
		// generate declarations		
		for(Object var: vars){
			action = `LetRef((TomTerm)var,(Expression)vvIt.next(),action);
		}		
		return action;
	}
	
	public static SymbolTable getSymbolTable(){
		return symbolTable;	
	}
}