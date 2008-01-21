/* Generated by TOM (version 2.6alpha): Do not edit this file *//*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2008, INRIA
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
  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */  /* Generated by TOM (version 2.6alpha): Do not edit this file */    private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if( (( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? l1.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ).isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? l1.getHeadAndConstraint() :l1),l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? l1.getHeadAndConstraint() :l1),tom_append_list_AndConstraint((( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? l1.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;     }   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;     }   }    /* Generated by TOM (version 2.6alpha): Do not edit this file */             /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */ /* Generated by TOM (version 2.6alpha): Do not edit this file */ /* Generated by TOM (version 2.6alpha): Do not edit this file */   /* Generated by TOM (version 2.6alpha): Do not edit this file */ 



//------------------------------------------------------

  private static final String propagatorsPackage = "tom.engine.compiler.propagator.";

  private static final String[] propagatorsNames = {"SyntacticPropagator","VariadicPropagator","ArrayPropagator","GeneralPurposePropagator"};

  public static Constraint performPropagations(Constraint constraintToCompile) 
    throws ClassNotFoundException,InstantiationException,IllegalAccessException,VisitFailure{
    
    // counts the propagators that didn't change the expression
    int propCounter = 0;
    int propNb = propagatorsNames.length;    	

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
   * Detaches the annotations
   * 
   * a@...b@g(y) << t -> g(y) << t /\ a << t /\ ... /\ b << t
   * 
   * For variableStars: a@...b@X* << t -> Z* << t /\ X* << Z* /\ a << Z* /\ ... /\ b << Z*  
   * This is because the varStars can have at the rhs something that will generate loops,
   * and we don't want to duplicate that to the constraints  
   */
  public static Constraint performDetach(Constraint subject) {    
    Constraint result =  tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ; 
    {if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {{  tom.engine.adt.tomconstraint.types.Constraint  tomMatch144NameNumberfreshSubject_1=(( tom.engine.adt.tomconstraint.types.Constraint )subject);if ( (tomMatch144NameNumberfreshSubject_1 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch144NameNumber_freshVar_0= tomMatch144NameNumberfreshSubject_1.getpattern() ;{  tom.engine.adt.tomterm.types.TomTerm  tomMatch144NameNumber_freshVar_1= tomMatch144NameNumberfreshSubject_1.getsubject() ;{ boolean tomMatch144NameNumber_freshVar_6= false ;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch144NameNumber_freshVar_2= null ;if ( (tomMatch144NameNumber_freshVar_0 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch144NameNumber_freshVar_6= true ;tomMatch144NameNumber_freshVar_2= tomMatch144NameNumber_freshVar_0.getConstraints() ;}} else {if ( (tomMatch144NameNumber_freshVar_0 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch144NameNumber_freshVar_6= true ;tomMatch144NameNumber_freshVar_2= tomMatch144NameNumber_freshVar_0.getConstraints() ;}} else {if ( (tomMatch144NameNumber_freshVar_0 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {{tomMatch144NameNumber_freshVar_6= true ;tomMatch144NameNumber_freshVar_2= tomMatch144NameNumber_freshVar_0.getConstraints() ;}}}}if ((tomMatch144NameNumber_freshVar_6 ==  true )) {{  tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraints=tomMatch144NameNumber_freshVar_2;{ boolean tomMatch144NameNumber_freshVar_5= false ;if ( ((tomMatch144NameNumber_freshVar_2 instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || (tomMatch144NameNumber_freshVar_2 instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch144NameNumber_freshVar_3=tomMatch144NameNumber_freshVar_2;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch144NameNumber_freshVar_4=tomMatch144NameNumber_freshVar_2;if ( tomMatch144NameNumber_freshVar_4.equals(tom_constraints) ) {if ( tomMatch144NameNumber_freshVar_3.isEmptyconcConstraint() ) {tomMatch144NameNumber_freshVar_5= true ;}}}}}if ((tomMatch144NameNumber_freshVar_5 ==  false )) {if ( true ) {{if ( (tom_constraints instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch145NameNumberfreshSubject_1=(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints);if ( ((tomMatch145NameNumberfreshSubject_1 instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || (tomMatch145NameNumberfreshSubject_1 instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch145NameNumber_freshVar_0=tomMatch145NameNumberfreshSubject_1;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch145NameNumber_begin_2=tomMatch145NameNumber_freshVar_0;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch145NameNumber_end_3=tomMatch145NameNumber_freshVar_0;do {{{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch145NameNumber_freshVar_1=tomMatch145NameNumber_end_3;if (!( tomMatch145NameNumber_freshVar_1.isEmptyconcConstraint() )) {if ( ( tomMatch145NameNumber_freshVar_1.getHeadconcConstraint()  instanceof tom.engine.adt.tomconstraint.types.constraint.AssignTo) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch145NameNumber_freshVar_6=  tomMatch145NameNumber_freshVar_1.getHeadconcConstraint() .getVariable() ;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch145NameNumber_freshVar_4= tomMatch145NameNumber_freshVar_1.getTailconcConstraint() ;if ( true ) {



            // add constraint to the list
            result =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tomMatch145NameNumber_freshVar_6, tomMatch144NameNumber_freshVar_1) ,tom_append_list_AndConstraint(result, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;                                                                                                                       
          }}}}}}if ( tomMatch145NameNumber_end_3.isEmptyconcConstraint() ) {tomMatch145NameNumber_end_3=tomMatch145NameNumber_begin_2;} else {tomMatch145NameNumber_end_3= tomMatch145NameNumber_end_3.getTailconcConstraint() ;}}} while(!( tomMatch145NameNumber_end_3.equals(tomMatch145NameNumber_begin_2) ));}}}}}}}
// end match   
      }}}}}}}}}}}}if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {{  tom.engine.adt.tomconstraint.types.Constraint  tomMatch144NameNumberfreshSubject_1=(( tom.engine.adt.tomconstraint.types.Constraint )subject);if ( (tomMatch144NameNumberfreshSubject_1 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch144NameNumber_freshVar_7= tomMatch144NameNumberfreshSubject_1.getpattern() ;{  tom.engine.adt.tomterm.types.TomTerm  tomMatch144NameNumber_freshVar_8= tomMatch144NameNumberfreshSubject_1.getsubject() ;{ boolean tomMatch144NameNumber_freshVar_14= false ;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch144NameNumber_freshVar_10= null ;{  tom.engine.adt.tomtype.types.TomType  tomMatch144NameNumber_freshVar_9= null ;if ( (tomMatch144NameNumber_freshVar_7 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch144NameNumber_freshVar_14= true ;tomMatch144NameNumber_freshVar_9= tomMatch144NameNumber_freshVar_7.getAstType() ;tomMatch144NameNumber_freshVar_10= tomMatch144NameNumber_freshVar_7.getConstraints() ;}} else {if ( (tomMatch144NameNumber_freshVar_7 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {{tomMatch144NameNumber_freshVar_14= true ;tomMatch144NameNumber_freshVar_9= tomMatch144NameNumber_freshVar_7.getAstType() ;tomMatch144NameNumber_freshVar_10= tomMatch144NameNumber_freshVar_7.getConstraints() ;}}}if ((tomMatch144NameNumber_freshVar_14 ==  true )) {{  tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraints=tomMatch144NameNumber_freshVar_10;{ boolean tomMatch144NameNumber_freshVar_13= false ;if ( ((tomMatch144NameNumber_freshVar_10 instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || (tomMatch144NameNumber_freshVar_10 instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch144NameNumber_freshVar_11=tomMatch144NameNumber_freshVar_10;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch144NameNumber_freshVar_12=tomMatch144NameNumber_freshVar_10;if ( tomMatch144NameNumber_freshVar_12.equals(tom_constraints) ) {if ( tomMatch144NameNumber_freshVar_11.isEmptyconcConstraint() ) {tomMatch144NameNumber_freshVar_13= true ;}}}}}if ((tomMatch144NameNumber_freshVar_13 ==  false )) {if ( true ) {
        
        TomTerm freshVariable = Compiler.getFreshVariableStar(tomMatch144NameNumber_freshVar_9);
        {if ( (tom_constraints instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch146NameNumberfreshSubject_1=(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraints);if ( ((tomMatch146NameNumberfreshSubject_1 instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || (tomMatch146NameNumberfreshSubject_1 instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch146NameNumber_freshVar_0=tomMatch146NameNumberfreshSubject_1;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch146NameNumber_begin_2=tomMatch146NameNumber_freshVar_0;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch146NameNumber_end_3=tomMatch146NameNumber_freshVar_0;do {{{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch146NameNumber_freshVar_1=tomMatch146NameNumber_end_3;if (!( tomMatch146NameNumber_freshVar_1.isEmptyconcConstraint() )) {if ( ( tomMatch146NameNumber_freshVar_1.getHeadconcConstraint()  instanceof tom.engine.adt.tomconstraint.types.constraint.AssignTo) ) {{  tom.engine.adt.tomterm.types.TomTerm  tomMatch146NameNumber_freshVar_6=  tomMatch146NameNumber_freshVar_1.getHeadconcConstraint() .getVariable() ;{  tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch146NameNumber_freshVar_4= tomMatch146NameNumber_freshVar_1.getTailconcConstraint() ;if ( true ) {

            result =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tomMatch146NameNumber_freshVar_6, freshVariable) ,tom_append_list_AndConstraint(result, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;                                                                                                                       
          }}}}}}if ( tomMatch146NameNumber_end_3.isEmptyconcConstraint() ) {tomMatch146NameNumber_end_3=tomMatch146NameNumber_begin_2;} else {tomMatch146NameNumber_end_3= tomMatch146NameNumber_end_3.getTailconcConstraint() ;}}} while(!( tomMatch146NameNumber_end_3.equals(tomMatch146NameNumber_begin_2) ));}}}}}}}
// end match   
        result =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(freshVariable, tomMatch144NameNumber_freshVar_8) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tomMatch144NameNumber_freshVar_7.setConstraints( tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ), freshVariable) ,tom_append_list_AndConstraint(result, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ) 
;
      }}}}}}}}}}}}}}

    return result;
  }  
}
