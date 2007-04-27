package tom.engine.compiler;

import java.util.ArrayList;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.compiler.propagator.*;
import tom.library.sl.*;

/**
 * This class is in charge with launching all the propagators,
 * until no more propagations can be made 
 */
public class TomPropagationManager {

//------------------------------------------------------	
  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../adt/tomsignature/_TomSignature.tom }
  %include { sl.tom }
  %include { java/util/types/ArrayList.tom}
//------------------------------------------------------

  private static final String propagatorsPackage = "tom.engine.compiler.propagator.";

  private static final String[] propagatorsNames = {"TomSyntacticPropagator","TomVariadicPropagator","TomArrayPropagator"};

  private static TomNumberList rootpath = null;  

  public static Constraint performPropagations(Constraint constraintToCompile) 
    throws ClassNotFoundException,InstantiationException,IllegalAccessException{
    
    // counts the propagators that didn't change the expression
    int propCounter = 0;
    int propNb = propagatorsNames.length;    	

    // some preparations
    constraintToCompile = preparePropagations(constraintToCompile);
    // cache the propagators
    TomIBasePropagator[] prop = new TomIBasePropagator[propNb];
    for(int i=0 ; i < propNb ; i++) {
      prop[i] = (TomIBasePropagator)Class.forName(propagatorsPackage + propagatorsNames[i]).newInstance();
    }
    
    Constraint result= null;
    mainLoop: while(true) {
      for(int i=0 ; i < propNb ; i++) {
        result = prop[i].propagate(constraintToCompile);
        // if nothing was done, start counting 
        propCounter = (result == constraintToCompile) ? (propCounter+1) : 0;        
        // if we applied all the propagators and nothing changed,
        // it's time to stop
        if(propCounter == propNb) { break mainLoop; }
        // reinitialize
        constraintToCompile = result;
      }
    } // end while    
    return result;
  }

  /**
   * Before propagations
   * - make sure that all constraints attached to terms are handled
   * - do nothing for the anti-patterns
   */
  private static Constraint preparePropagations(Constraint constraintToCompile) {
    ArrayList<Constraint> constraintList = new ArrayList<Constraint>(); 
    /* anti-terms are a little bit special and constraint detachment is performed in propagators
     * here we shouldn't do it because of the non-linearity ()
     * 
     * The strategy makes a TopDown, and when finding an AntiTerm, it doesn't traverse its children
     */    
    Constraint newConstr = (Constraint)`mu(MuVar("xx"),IfThenElse(Is_AntiTerm(),Identity(),
        Sequence(DetachConstraints(constraintList),All(MuVar("xx"))))).fire(constraintToCompile);    
    Constraint andList = `AndConstraint();
    for(Constraint constr: constraintList) {
      andList = `AndConstraint(andList*,constr);
    }    
    return `AndConstraint(newConstr,andList*);    
  }

  // TODO - wouldn't it be better to do this in propagators ?
  
  /**
   * f(x,a@b@g(y)) << t -> f(x,z) << t /\ g(y) << z /\ a << z /\ b << z
   */
  %strategy DetachConstraints(bag:ArrayList) extends Identity() {
    // if the constraints  = empty list, then is nothing to do
    visit TomTerm {
      t@(RecordAppl|Variable|UnamedVariable|VariableStar|UnamedVariableStar)[Constraints=constraints@!concConstraint()] -> {
        TomType freshVarType = TomConstraintCompiler.getTermTypeFromTerm(`t);
        TomTerm freshVariable = null;
        // make sure that if we had a varStar, we replace with a varStar also
match : %match(t) {
          (VariableStar|UnamedVariableStar)[] -> {
            freshVariable = TomConstraintCompiler.getFreshVariableStar(freshVarType);
            break match;
          }
          _ -> {
            freshVariable = TomConstraintCompiler.getFreshVariable(freshVarType);
          }
        }// end match
        //make sure to apply on its subterms also
        bag.add(preparePropagations(`MatchConstraint(t.setConstraints(concConstraint()),freshVariable)));
        // for each constraint
        %match(constraints) {
          concConstraint(_*,AssignTo(var),_*) -> {
            // add constraint to bag and delete it from the term
            bag.add(`MatchConstraint(var,freshVariable));                                                                                                                       
          }
        }// end match                   
        return freshVariable;                   
      }      
    } // end visit
  } // end strategy

}
