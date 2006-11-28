package tom.engine.compiler;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomconstraint.types.*;

import tom.engine.compiler.generator.*;

/**
 * This class is in charge with launching all the propagators,
 * until no more propagations can be made 
 */
public class TomInstructionGenerationManager extends TomBase {
	
	%include { adt/tomsignature/TomSignature.tom }	
	
	private static final String generatorsPackage = "tom.engine.compiler.generator";
	
	private static final String[] generatorsNames = {"",""};
	
	public Instruction performGenerations(Constraint constraint) 
			throws ClassNotFoundException,InstantiationException,IllegalAccessException{
		
		// counts the generators that didn't change the instruction
		short genCounter = 0;
		Instruction result = null;
		
		Instruction instruction = prepareGeneration(constraint);		
		
		// iterate until all propagators are applied and nothing was changed 
		mainLoop: while(true){		
			for(String i:generatorsNames){
				
				TomIBaseGenerator prop = (TomIBaseGenerator)Class.forName(generatorsPackage + i).newInstance();
				result = prop.generate(instruction);
				// if nothing was done, start counting 
				if (result == instruction){
					genCounter++;
				}else{
					// reset counter
					genCounter = 0;
				}				
				// if we applied all the propagators and nothing changed,
				// it's time to stop
				if (genCounter == generatorsNames.length) { break mainLoop; }
				// reinitialize
				instruction = result; 
			}
		} // end while
		return result;
	}
	
	/**
	 * Prepares the generation phase
	 */
	private Instruction prepareGeneration(Constraint constraint){
		// TODO
		return null;
	}
}