package tom.engine.compiler;

import java.util.ArrayList;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomtype.types.*;
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

  private static final String[] propagatorsNames = {"TomSyntacticPropagator","TomVariadicPropagator","TomArrayPropagator"};

  private static TomNumberList rootpath = null;
  private static short freshVarCounter = 0;

  public static Constraint performPropagations(Constraint constraintToCompile) 
  throws ClassNotFoundException,InstantiationException,IllegalAccessException{

    freshVarCounter = 0;		

    // counts the propagators that didn't change the expression
    short propCounter = 0;
    Constraint result = null;	

    // some preparations
    constraintToCompile = preparePropagations(constraintToCompile);

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
    // it is very important to have Outermost because we want the term first and only after its' subterms 
    Constraint newConstr = (Constraint)`OutermostId(DetachConstraints(constraintList)).fire(constraintToCompile);		
    Constraint andList = `AndConstraint();
    for(Constraint constr: constraintList){      
      andList = `AndConstraint(andList*,constr);
    }    
    return `AndConstraint(newConstr,andList*);
  }

  /**
   * f(x,a@b@g(y)) << t -> f(x,z) << t /\ g(y) << z /\ a << z /\ b << z
   */
  %strategy DetachConstraints(bag:Collection) extends Identity(){
    // if the constraints  = empty list, then is nothing to do
    visit TomTerm{
      t@(RecordAppl|Variable|UnamedVariable|VariableStar|UnamedVariableStar)[Constraints=constraints@!concConstraint()] ->{

        TomNumberList path = TomConstraintCompiler.getRootpath();
        TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name("fresh_"+ (++freshVarCounter)))));
        TomType freshVarType = TomConstraintCompiler.getTermTypeFromTerm(`t);
        TomTerm freshVariable = null;
        // make sure that if we had a varStar, we replace with a varStar also
match : %match(t) {
          (VariableStar|UnamedVariableStar)[] ->{
            freshVariable = `VariableStar(concOption(),freshVarName,freshVarType,concConstraint());
            break match;
          }
          _ -> {
            freshVariable = `Variable(concOption(),freshVarName,freshVarType,concConstraint());
          }
        }         

        bag.add(`MatchConstraint(t,freshVariable));
        // for each constraint
        %match(constraints){
          concConstraint(_*,AssignTo(var),_*) ->{
            // add constraint to bag and delete it from the term
            bag.add(`MatchConstraint(var,freshVariable));		    					    					    			
          }
        }// end match		    	
        return freshVariable;		    	
      }
    } // end visit
  } // end strategy
}