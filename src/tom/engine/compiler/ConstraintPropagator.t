/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Radu Kopetz e-mail: Radu.Kopetz@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/
package tom.engine.compiler;

import java.util.ArrayList;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.compiler.propagator.*;
import tom.engine.exception.TomRuntimeException;
import tom.library.sl.*;

/**
 * This class is in charge with launching all the propagators,
 * until no more propagations can be made 
 */
public class ConstraintPropagator {

//------------------------------------------------------	
  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../adt/tomsignature/_TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom}
  %include { java/util/types/ArrayList.tom}
//------------------------------------------------------

  private static final String propagatorsPackage = "tom.engine.compiler.propagator.";

  private static final String[] propagatorsNames = {"SyntacticPropagator","VariadicPropagator","ArrayPropagator"};

  public static Constraint performPropagations(Constraint constraintToCompile) 
    throws ClassNotFoundException,InstantiationException,IllegalAccessException,VisitFailure{
    
    // counts the propagators that didn't change the expression
    int propCounter = 0;
    int propNb = propagatorsNames.length;    	

    // some preparations
    constraintToCompile = preparePropagations(constraintToCompile);
    // cache the propagators
    IBasePropagator[] prop = new IBasePropagator[propNb];
    for(int i=0 ; i < propNb ; i++) {
      prop[i] = (IBasePropagator)Class.forName(propagatorsPackage + propagatorsNames[i]).newInstance();
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
   * - make sure sublists in a list are managed
   * - make sure that all constraints attached to terms are handled
   */
  private static Constraint preparePropagations(Constraint constraintToCompile) throws VisitFailure {
    // detach sublists
    Constraint result = detach(constraintToCompile,"DetachSublists");
    // detach constraints
    result = detach(result,"DetachConstraints");  
    return result;
  }
  
  /**
   * Applies the strategyName strategy in a TopDown manner to constraintToCompile, giving a collection as an argument. 
   * Returns the conjunction between the result of the strategy and the constraints in the conjunction 
   * 
   */
  private static Constraint detach(Constraint constraintToCompile, String strategyName) throws VisitFailure {
    ArrayList<Constraint> constraintList = new ArrayList<Constraint>();
    Constraint newConstr = null;
match:%match(strategyName) {
      "DetachConstraints" -> { newConstr = (Constraint)`TopDown(DetachConstraints(constraintList)).visit(constraintToCompile); break match;}
      "DetachSublists" -> { newConstr = (Constraint)`TopDown(DetachSublists(constraintList)).visit(constraintToCompile); }
    }    
    Constraint andList = `AndConstraint();
    for(Constraint constr: constraintList) {
      andList = `AndConstraint(andList*,constr);
    }    
    return `AndConstraint(newConstr,andList*);
  }
  /**
   * Make sure that the sublists in a list are replaced by star variables - this is only happening 
   * when the lists and the sublists have the same name
   * 
   * conc(X*,conc(some_pattern),Y*) << t -> conc(X*,Z*,Y*) << t /\ conc(some_pattern) << Z*  
   * 
   */  
  %strategy DetachSublists(bag:ArrayList) extends Identity() {
    visit TomTerm {
      // we only look for lists ( the lists can only have one name ) 
      t@RecordAppl[NameList=nameList@(name@Name(tomName)),Slots=slots] -> {
        if (!TomBase.isListOperator(ConstraintCompiler.getSymbolTable().
            getSymbolFromName(`tomName))) {
          return `t;
        }
        SlotList newSlots = `concSlot();
        %match(slots) { 
          concSlot(_*,slot,_*) -> {
matchSlot:  %match(slot,TomName name) {
              // if we find a child with the same name, we abstract
              ps@PairSlotAppl[Appl=appl@RecordAppl[NameList=nameList@(childName)]],childName ->{
                TomTerm freshVariable = ConstraintCompiler.getFreshVariableStar(ConstraintCompiler.getTermTypeFromTerm(`t));
                // make sure to apply on its subterms also 
                bag.add(detach(`MatchConstraint(appl,freshVariable),"DetachSublists"));
                bag.add(`MatchConstraint(appl,freshVariable));
                newSlots = `concSlot(newSlots*,ps.setAppl(freshVariable));
                break matchSlot;
              }
              // else we just add the slot back to the list
              x,_ -> {
                newSlots = `concSlot(newSlots*,x);
              }
            }            
          }
        }
        return `t.setSlots(newSlots);
      }
    } // end visit
  } // end strategy  

  /**
  * Handle constraints' detachment
  *   
  * f(x,a@b@g(y)) << t -> f(x,z) << t /\ g(y) << z /\ a << z /\ b << z
  */ 
  %strategy DetachConstraints(bag:ArrayList) extends Identity() {
    // if the constraints  = empty list, then is nothing to do
    visit TomTerm {
      t@(RecordAppl|Variable|UnamedVariable|VariableStar|UnamedVariableStar)[Constraints=constraints@!concConstraint()] -> {
        return `performDetach(bag,t.setConstraints(concConstraint()),constraints,false);
      }
      AntiTerm(t@(RecordAppl|Variable|VariableStar)[Constraints=constraints@!concConstraint()]) -> {
        return `performDetach(bag,t.setConstraints(concConstraint()),constraints,true);
      }
    } // end visit
  } // end strategy
    
  /**
   * a@...b@g(y) << t -> g(y) << z /\ a << z /\ ... /\ b << z
   * a@...b@!g(y) << t -> !g(y) << z /\ a << z /\ ... /\ b << z
   *
   */
  private static TomTerm performDetach(ArrayList bag, TomTerm subject, ConstraintList constraints, boolean isAnti) throws VisitFailure {
    TomType freshVarType = ConstraintCompiler.getTermTypeFromTerm(subject);
    TomTerm freshVariable = null;
    // make sure that if we had a varStar, we replace with a varStar also
    if (needsVarStar(subject)){
      freshVariable = ConstraintCompiler.getFreshVariableStar(freshVarType);
    }else{
      freshVariable = ConstraintCompiler.getFreshVariable(freshVarType);
    }
    //make sure to apply on its subterms also    
    subject = isAnti ? `AntiTerm(subject) : subject;
    bag.add(detach(`MatchConstraint(subject,freshVariable),"DetachConstraints"));
    // for each constraint
    %match(constraints) {
      concConstraint(_*,AssignTo(var),_*) -> {
        // add constraint to bag and delete it from the term
        bag.add(`MatchConstraint(var,freshVariable));                                                                                                                       
      }
    }// end match                   
    return freshVariable;      
  }
  
  private static boolean needsVarStar(TomTerm subject) {
    %match(subject) {
      (VariableStar|UnamedVariableStar)[] -> {
        return true;
      }
      RecordAppl[NameList=nameList@(Name(tomName),_*)] -> {
        if (!TomBase.isSyntacticOperator(ConstraintCompiler.getSymbolTable().
            getSymbolFromName(`tomName))) {
          return true;  
        }
      }
    }
    return false;
  }
  
}
