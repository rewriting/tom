package tom.engine.compiler;

import java.util.ArrayList;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomconstraint.types.*;

import tom.engine.compiler.propagator.*;

/**
 * This class is in charge with launching all the propagators,
 * until no more propagations can be made 
 */
public class TomPropagationManager extends TomBase {
	
//------------------------------------------------------	
	%include { adt/tomsignature/TomSignature.tom }
	%include { sl.tom }
	%include { java/util/types/Collection.tom}
//------------------------------------------------------
	
	private static final String propagatorsPackage = "tom.engine.compiler.propagator.";
	
	private static final String[] propagatorsNames = {"TomSyntacticPropagator"};
	
	public static Constraint performPropagations(Constraint constraintToCompile) 
			throws ClassNotFoundException,InstantiationException,IllegalAccessException{
		
		// counts the propagators that didn't change the expression
		short propCounter = 0;
		Constraint result = null;		
		
		// iterate until all propagators are applied and nothing was changed 
		mainLoop: while(true){		
			for(String i:propagatorsNames){
				
				TomIBasePropagator prop = (TomIBasePropagator)Class.forName(propagatorsPackage + i).newInstance();
				result = prop.propagate(constraintToCompile);
				// if nothing was done, start counting 
				if (result == constraintToCompile){
					propCounter++;
				}else{
					// reset counter
					propCounter = 0;
				}
				
				// if we applied all the propagators and nothing changed,
				// it's time to stop
				if (propCounter == propagatorsNames.length) { break mainLoop; }
				// reinitialize
				constraintToCompile = result;
			}
		} // end while
		return result;
	}
	
	/**
	 * Before propagations
	 * - make sure that all constraints attached to terms are handled
	 */
	private static Constraint preparePropagations(Constraint constraintToCompile){
		ArrayList<Constraint> constraintList = new ArrayList<Constraint>();
		Constraint newConstr = (Constraint)`TopDown(DetachConstraints(constraintList)).fire(constraintToCompile);
		ConstraintList concConsList = `concConstraint();
		for(Constraint constr: constraintList){
			concConsList = `concConstraint(constr,concConsList*);
		}
		return `AndConstraint(concConstraint(newConstr,concConsList*));
	}
	
	%strategy DetachConstraints(bag:Collection) extends Identity(){
		visit TomTerm{
			t@(RecordAppl|Variable)[Constraints=constraints] ->{
		    	// for each constraint
		    	%match(constraints){
		    		concConstraint(_*,AssignTo(var),_*) ->{
		    			// add constraint to bag and delete it from the term
		    			bag.add(`MatchConstraint(var,t));
		    			return `t.setConstraints(`concConstraint());		    					    			
		    		}
		    	}// end match
		    }
		} // end visit
	} // end strategy
}