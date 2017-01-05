/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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

import java.lang.reflect.*;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.compiler.*;
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
  %include { ../../library/mapping/java/sl.tom}
  %include { java/util/types/ArrayList.tom}
//------------------------------------------------------

  private Compiler compiler;

  public ConstraintPropagator(Compiler myCompiler) {
    this.compiler = myCompiler; 
  } 

  public Compiler getCompiler() {
    return this.compiler;
  }

  private static final String propagatorsPackage = "tom.engine.compiler.propagator.";

  private static final String[] propagatorsNames = {"SyntacticPropagator","VariadicPropagator","ArrayPropagator","GeneralPurposePropagator","ACPropagator"};

  public Constraint performPropagations(Constraint constraintToCompile) 
    throws ClassNotFoundException,InstantiationException,IllegalAccessException,VisitFailure,InvocationTargetException,NoSuchMethodException{
    
    // counts the propagators that didn't change the expression
    int propCounter = 0;
    int propNb = propagatorsNames.length;    	

    // cache the propagators
    IBasePropagator[] prop = new IBasePropagator[propNb];
    Class[] classTab = new Class[]{Class.forName("tom.engine.compiler.Compiler"), Class.forName("tom.engine.compiler.ConstraintPropagator")};
    for(int i=0 ; i < propNb ; i++) {
      Class myClass = Class.forName(propagatorsPackage + propagatorsNames[i]);
      java.lang.reflect.Constructor constructor = myClass.getConstructor(classTab);
      prop[i] = (IBasePropagator)constructor.newInstance(this.getCompiler(),this);
    }
    
    Constraint result= null;
    //constraintToCompile = new ACPropagator(this.getCompiler(),this).propagate(constraintToCompile);
    mainLoop: while(true) {
      for(int i=0 ; i < propNb ; i++) {
        result = prop[i].propagate(constraintToCompile);
        //DEBUG System.out.println("*** prop[i] = " + prop[i] + '\n');
        //DEBUG System.out.println("*** result = " + result + '\n');
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
   * Detaches the annotations
   *  
   * a@...b@g(y) << t -> g(y) << t /\ a << t /\ ... /\ b << t
   * 
   * For variableStars: a@...b@X* << t -> Z* << t /\ X* << Z* /\ a << Z* /\ ... /\ b << Z*  
   * This is because the varStars can have at the rhs something that will generate loops,
   * and we don't want to duplicate that to the constraints  
   */
  public Constraint performDetach(Constraint subject) {    
    Constraint result = `AndConstraint(); 
    %match(subject){
      MatchConstraint[Pattern=(RecordAppl|Variable)[Constraints=constraints@!concConstraint()],Subject=g,AstType=aType] -> {
        %match(constraints) {
          concConstraint(_*,AliasTo(var),_*) -> {
            // add constraint to the list
            result = `AndConstraint(MatchConstraint(var,g,aType),result*);
          }
        }// end match   
      }      
      MatchConstraint[Pattern=t@VariableStar[AstType=type,Constraints=constraints@!concConstraint()],Subject=g,AstType=aType] -> {        
        BQTerm freshVariable = getCompiler().getFreshVariableStar(`type);
        %match(constraints) {
          concConstraint(_*,AliasTo(var),_*) -> {
            result = `AndConstraint(MatchConstraint(var,freshVariable,aType),result*);
          }
        }// end match   
        result =
          `AndConstraint(MatchConstraint(TomBase.convertFromBQVarToVar(freshVariable),g,aType),
            MatchConstraint(t.setConstraints(concConstraint()),freshVariable,aType),result*);
      }      
    }
    return result;
  }  
}
