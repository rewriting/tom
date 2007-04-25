package tom.engine.compiler;

import java.util.ArrayList;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

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
   * - make sure that the aps are detached
   */
  private static Constraint preparePropagations(Constraint constraintToCompile) {
    ArrayList<Constraint> constraintList = new ArrayList<Constraint>();
    // it is very important to have Outermost because we want the term first and only after its' subterms 
    Constraint newConstr = (Constraint)`OutermostId(Choice(IsAntiTerm(),DetachConstraints(constraintList))).fire(constraintToCompile);		
    Constraint andList = `AndConstraint();
    for(Constraint constr: constraintList) {
      andList = `AndConstraint(andList*,constr);
    }    
    return `AndConstraint(newConstr,andList*);
  }
  
  %strategy IsAntiTerm() extends Fail() {
    visit TomTerm {
      AntiTerm[] -> { }
    }
  }

  /**
   * f(x,a@b@g(y)) << t -> f(x,z) << t /\ g(y) << z /\ a << z /\ b << z
   */
  %strategy DetachConstraints(bag:ArrayList) extends Identity() {
    // if the constraints  = empty list, then is nothing to do
    visit TomTerm {
//      // anti-terms are a little bit special and constraint detachment is performed in propagators
//      // here we shouldn't do it because of the non-linearity ()
//      a@AntiTerm[] -> {
//        throw new VisitFailure();
//      }
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
        bag.add(`MatchConstraint(t,freshVariable));
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
