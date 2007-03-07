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
	private static final String[] generatorsNames = {"TomSyntacticGenerator","TomVariadicGenerator"};
	
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
		//System.out.println("result: " + result);
		return buildInstructionFromExpression(result,action);
	}
	
	/**
	 * Prepares the generation phase
	 * 1. replaces all constraints with ConstraintToExpression 
	 */
	private static Expression prepareGeneration(Constraint constraint){
		%match(constraint){
			AndConstraint(concConstraint(m,X*)) ->{
				return `And(prepareGeneration(m),
						prepareGeneration(AndConstraint(concConstraint(X*))));				
			}
			m@MatchConstraint[] ->{
				return `ConstraintToExpression(m);
			}
			Negate(c) ->{
				return `Negation(prepareGeneration(c));
			}
			EmptyListConstraint(opName,variable) ->{				
				return `IsEmptyList(opName,variable);
			}
		}			
		throw new TomRuntimeException("TomInstructionGenerationManager.prepareGeneration - strange constraint:" + constraint);
	}	
	
	/**
	 * Converts the resulted expression (after generation) into instructions
	 */
	private static Instruction buildInstructionFromExpression(Expression expression, Instruction action){		
		// it is done innermost because the expression is also simplified		
		expression = (Expression)`InnermostId(ReplaceSubterms()).fire(expression);		
		// generate automata
		Instruction automata = generateAutomata(expression,action);		
		// make sure that each variable is declared only once
		ArrayList<TomTerm> declaredVariables = new ArrayList<TomTerm>(); 		
		automata = (Instruction)`TopDown(ChangeVarDeclarations(declaredVariables)).fire(automata);
		return automata;
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
			ConstraintToExpression(MatchConstraint(v@(Variable|UnamedVariable|VariableStar)[],t)) ->{
				return `LetRef(v,TomTermToExpression(t),action);			
			}			
			// do while
			DoWhileExpression(expr,condition) ->{
				Instruction subInstruction = generateAutomata(`expr,`Nop());
				return `DoWhile(UnamedBlock(concInstruction(action,subInstruction)),condition);
			}
			// 'if'
			IfExpression(condition, EqualTerm[Kid1=left1,Kid2=right1], EqualTerm[Kid1=left2,Kid2=right2]) ->{
				return `If(condition,LetAssign(left1,TomTermToExpression(right1),Nop()),LetAssign(left2,TomTermToExpression(right2),Nop()));
			}
			// conditions			
			x ->{
				return `If(x,action,Nop());
			}			
		}
		throw new TomRuntimeException("TomInstructionGenerationManager.generateAutomata - strange expression:" + expression);
	}
	
	/**
	 * Makes sure that no variable is declared twice   
	 */
	%strategy ChangeVarDeclarations(declaredVariables:Collection) extends Identity(){
		visit Instruction{
			LetRef(var,source,instruction) ->{
				if (declaredVariables.contains(`var)){
					return `LetAssign(var,source,instruction);
				}else{
					declaredVariables.add(`var);
				}			
			}
		}// end visit
	}// end strategy
	
	/**
	 * Converts 'Subterm' to 'GetSlot'
	 */
	%strategy ReplaceSubterms() extends Identity(){
		visit TomTerm{
			s@Subterm(constructorName@Name(name), slotName, term) ->{
				TomSymbol tomSymbol = TomConstraintCompiler.getSymbolTable().getSymbolFromName(`name);
	        	TomType subtermType = TomBase.getSlotType(tomSymbol, `slotName);	        	
				return `ExpressionToTomTerm(GetSlot(subtermType, constructorName, slotName.getString(), term));
			}
		}
	}
}