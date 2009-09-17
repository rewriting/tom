/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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
        private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }              


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
    Constraint result =  tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ; 
    {{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch120NameNumber_freshVar_1= (( tom.engine.adt.tomconstraint.types.Constraint )subject).getPattern() ;boolean tomMatch120NameNumber_freshVar_9= false ; tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch120NameNumber_freshVar_4= null ;if ( (tomMatch120NameNumber_freshVar_1 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch120NameNumber_freshVar_9= true ;tomMatch120NameNumber_freshVar_4= tomMatch120NameNumber_freshVar_1.getConstraints() ;}} else {if ( (tomMatch120NameNumber_freshVar_1 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch120NameNumber_freshVar_9= true ;tomMatch120NameNumber_freshVar_4= tomMatch120NameNumber_freshVar_1.getConstraints() ;}} else {if ( (tomMatch120NameNumber_freshVar_1 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {{tomMatch120NameNumber_freshVar_9= true ;tomMatch120NameNumber_freshVar_4= tomMatch120NameNumber_freshVar_1.getConstraints() ;}}}}if (tomMatch120NameNumber_freshVar_9) { tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraints=tomMatch120NameNumber_freshVar_4;boolean tomMatch120NameNumber_freshVar_8= false ;if ( ((tomMatch120NameNumber_freshVar_4 instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || (tomMatch120NameNumber_freshVar_4 instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if ( (tom_constraints==tomMatch120NameNumber_freshVar_4) ) {if ( tomMatch120NameNumber_freshVar_4.isEmptyconcConstraint() ) {tomMatch120NameNumber_freshVar_8= true ;}}}if (!(tomMatch120NameNumber_freshVar_8)) {{{if ( (tom_constraints instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) { tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch121NameNumber_end_4=(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints);do {{if (!( tomMatch121NameNumber_end_4.isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch121NameNumber_freshVar_8= tomMatch121NameNumber_end_4.getHeadconcConstraint() ;if ( (tomMatch121NameNumber_freshVar_8 instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {



            // add constraint to the list
            result =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make( tomMatch121NameNumber_freshVar_8.getVar() ,  (( tom.engine.adt.tomconstraint.types.Constraint )subject).getSubject() ) ,tom_append_list_AndConstraint(result, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;
          }}if ( tomMatch121NameNumber_end_4.isEmptyconcConstraint() ) {tomMatch121NameNumber_end_4=(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints);} else {tomMatch121NameNumber_end_4= tomMatch121NameNumber_end_4.getTailconcConstraint() ;}}} while(!( (tomMatch121NameNumber_end_4==(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints)) ));}}}}
// end match   
      }}}}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch120NameNumber_freshVar_11= (( tom.engine.adt.tomconstraint.types.Constraint )subject).getPattern() ;boolean tomMatch120NameNumber_freshVar_20= false ; tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch120NameNumber_freshVar_15= null ; tom.engine.adt.tomtype.types.TomType  tomMatch120NameNumber_freshVar_14= null ;if ( (tomMatch120NameNumber_freshVar_11 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch120NameNumber_freshVar_20= true ;tomMatch120NameNumber_freshVar_14= tomMatch120NameNumber_freshVar_11.getAstType() ;tomMatch120NameNumber_freshVar_15= tomMatch120NameNumber_freshVar_11.getConstraints() ;}} else {if ( (tomMatch120NameNumber_freshVar_11 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {{tomMatch120NameNumber_freshVar_20= true ;tomMatch120NameNumber_freshVar_14= tomMatch120NameNumber_freshVar_11.getAstType() ;tomMatch120NameNumber_freshVar_15= tomMatch120NameNumber_freshVar_11.getConstraints() ;}}}if (tomMatch120NameNumber_freshVar_20) { tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraints=tomMatch120NameNumber_freshVar_15;boolean tomMatch120NameNumber_freshVar_19= false ;if ( ((tomMatch120NameNumber_freshVar_15 instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || (tomMatch120NameNumber_freshVar_15 instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if ( (tom_constraints==tomMatch120NameNumber_freshVar_15) ) {if ( tomMatch120NameNumber_freshVar_15.isEmptyconcConstraint() ) {tomMatch120NameNumber_freshVar_19= true ;}}}if (!(tomMatch120NameNumber_freshVar_19)) {
        
        BQTerm freshVariable = getCompiler().getFreshVariableStar(tomMatch120NameNumber_freshVar_14);
        {{if ( (tom_constraints instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) { tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch122NameNumber_end_4=(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints);do {{if (!( tomMatch122NameNumber_end_4.isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch122NameNumber_freshVar_8= tomMatch122NameNumber_end_4.getHeadconcConstraint() ;if ( (tomMatch122NameNumber_freshVar_8 instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {

            result =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make( tomMatch122NameNumber_freshVar_8.getVar() , freshVariable) ,tom_append_list_AndConstraint(result, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;
          }}if ( tomMatch122NameNumber_end_4.isEmptyconcConstraint() ) {tomMatch122NameNumber_end_4=(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints);} else {tomMatch122NameNumber_end_4= tomMatch122NameNumber_end_4.getTailconcConstraint() ;}}} while(!( (tomMatch122NameNumber_end_4==(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints)) ));}}}}
// end match   
        result =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(TomBase.convertFromBQVarToVar(freshVariable),  (( tom.engine.adt.tomconstraint.types.Constraint )subject).getSubject() ) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tomMatch120NameNumber_freshVar_11.setConstraints( tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ), freshVariable) ,tom_append_list_AndConstraint(result, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ) 
;
      }}}}}}

    return result;
  }  
}
