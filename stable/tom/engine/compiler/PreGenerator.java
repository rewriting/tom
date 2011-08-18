/*
*
* TOM - To One Matching Compiler
*
* Copyright (c) 2000-2011, INPL, INRIA
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
import java.util.Iterator;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.TomConstraintPrettyPrinter;
import tom.engine.compiler.generator.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.library.sl.*;


/**
* This class is in charge with all the pre treatments for generation needed
* after the propagation process
* 1. make sure that the constraints are in the good order
* 2. translate constraints into expressions ...
*/
public class PreGenerator {

// ------------------------------------------------------------


  private static   tom.engine.adt.tomexpression.types.Expression  tom_append_list_OrExpressionDisjunction( tom.engine.adt.tomexpression.types.Expression  l1,  tom.engine.adt.tomexpression.types.Expression  l2) {
    if( l1.isEmptyOrExpressionDisjunction() ) {
      return l2;
    } else if( l2.isEmptyOrExpressionDisjunction() ) {
      return l1;
    } else if( ((l1 instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || (l1 instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {
      if(  l1.getTailOrExpressionDisjunction() .isEmptyOrExpressionDisjunction() ) {
        return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make( l1.getHeadOrExpressionDisjunction() ,l2) ;
      } else {
        return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make( l1.getHeadOrExpressionDisjunction() ,tom_append_list_OrExpressionDisjunction( l1.getTailOrExpressionDisjunction() ,l2)) ;
      }
    } else {
      return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make(l1,l2) ;
    }
  }
  private static   tom.engine.adt.tomexpression.types.Expression  tom_get_slice_OrExpressionDisjunction( tom.engine.adt.tomexpression.types.Expression  begin,  tom.engine.adt.tomexpression.types.Expression  end, tom.engine.adt.tomexpression.types.Expression  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyOrExpressionDisjunction()  ||  (end== tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )? begin.getHeadOrExpressionDisjunction() :begin),( tom.engine.adt.tomexpression.types.Expression )tom_get_slice_OrExpressionDisjunction((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )? begin.getTailOrExpressionDisjunction() : tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ),end,tail)) ;
  }
  
  private static   tom.engine.adt.tomexpression.types.Expression  tom_append_list_OrConnector( tom.engine.adt.tomexpression.types.Expression  l1,  tom.engine.adt.tomexpression.types.Expression  l2) {
    if( l1.isEmptyOrConnector() ) {
      return l2;
    } else if( l2.isEmptyOrConnector() ) {
      return l1;
    } else if( ((l1 instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || (l1 instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) ) {
      if(  l1.getTailOrConnector() .isEmptyOrConnector() ) {
        return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make( l1.getHeadOrConnector() ,l2) ;
      } else {
        return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make( l1.getHeadOrConnector() ,tom_append_list_OrConnector( l1.getTailOrConnector() ,l2)) ;
      }
    } else {
      return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make(l1,l2) ;
    }
  }
  private static   tom.engine.adt.tomexpression.types.Expression  tom_get_slice_OrConnector( tom.engine.adt.tomexpression.types.Expression  begin,  tom.engine.adt.tomexpression.types.Expression  end, tom.engine.adt.tomexpression.types.Expression  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyOrConnector()  ||  (end== tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )? begin.getHeadOrConnector() :begin),( tom.engine.adt.tomexpression.types.Expression )tom_get_slice_OrConnector((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )? begin.getTailOrConnector() : tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ),end,tail)) ;
  }
  
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {
    if( l1.isEmptyAndConstraint() ) {
      return l2;
    } else if( l2.isEmptyAndConstraint() ) {
      return l1;
    } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
      if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;
      } else {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;
      }
    } else {
      return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;
    }
  }
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;
  }
  
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {
    if( l1.isEmptyOrConstraint() ) {
      return l2;
    } else if( l2.isEmptyOrConstraint() ) {
      return l1;
    } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {
      if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;
      } else {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;
      }
    } else {
      return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;
    }
  }
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;
  }
  
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraintDisjunction( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {
    if( l1.isEmptyOrConstraintDisjunction() ) {
      return l2;
    } else if( l2.isEmptyOrConstraintDisjunction() ) {
      return l1;
    } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) ) {
      if(  l1.getTailOrConstraintDisjunction() .isEmptyOrConstraintDisjunction() ) {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction.make( l1.getHeadOrConstraintDisjunction() ,l2) ;
      } else {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction.make( l1.getHeadOrConstraintDisjunction() ,tom_append_list_OrConstraintDisjunction( l1.getTailOrConstraintDisjunction() ,l2)) ;
      }
    } else {
      return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction.make(l1,l2) ;
    }
  }
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraintDisjunction( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyOrConstraintDisjunction()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )? begin.getHeadOrConstraintDisjunction() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraintDisjunction((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )? begin.getTailOrConstraintDisjunction() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() ),end,tail)) ;
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 == null )) {
      return l2;
    } else if(( l2 == null )) {
      return l1;
    } else if(( l1 instanceof tom.library.sl.Sequence )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {
        return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;
      } else {
        return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;
      }
    } else {
      return  tom.library.sl.Sequence.make(l1,l2) ;
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 ==null )) {
      return l2;
    } else if(( l2 ==null )) {
      return l1;
    } else if(( l1 instanceof tom.library.sl.Choice )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {
        return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;
      } else {
        return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;
      }
    } else {
      return  tom.library.sl.Choice.make(l1,l2) ;
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 == null )) {
      return l2;
    } else if(( l2 == null )) {
      return l1;
    } else if(( l1 instanceof tom.library.sl.SequenceId )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {
        return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;
      } else {
        return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;
      }
    } else {
      return  tom.library.sl.SequenceId.make(l1,l2) ;
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 ==null )) {
      return l2;
    } else if(( l2 ==null )) {
      return l1;
    } else if(( l1 instanceof tom.library.sl.ChoiceId )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {
        return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;
      } else {
        return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;
      }
    } else {
      return  tom.library.sl.ChoiceId.make(l1,l2) ;
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;
  }
  private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { 
return ( 
 tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) )

;
}
private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ))

;
}

// ------------------------------------------------------------

private ConstraintGenerator constraintGenerator;

public PreGenerator(ConstraintGenerator myConstraintGenerator) {
this.constraintGenerator = myConstraintGenerator;
} 

public ConstraintGenerator getConstraintGenerator() {
return this.constraintGenerator;
}

public Expression performPreGenerationTreatment(Constraint constraint) throws VisitFailure {
constraint = orderConstraints(constraint);
return constraintsToExpressions(constraint);
}

private Constraint orderConstraints(Constraint constraint) {

{
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
boolean tomMatch194_12= false ;
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch194__end__7=(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint));
do {
{
if (!( (  tomMatch194__end__7.isEmptyAndConstraint()  ||  (tomMatch194__end__7== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( ((tomMatch194__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch194__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch194__end__7.getHeadAndConstraint() ):(tomMatch194__end__7))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( ((tomMatch194__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch194__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch194__end__7.getHeadAndConstraint() ):(tomMatch194__end__7))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {
tomMatch194_12= true ;
}
}
if ( (  tomMatch194__end__7.isEmptyAndConstraint()  ||  (tomMatch194__end__7== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch194__end__7=(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint));
} else {
tomMatch194__end__7=(( ((tomMatch194__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch194__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch194__end__7.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch194__end__7==(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) ));
}
if (!(tomMatch194_12)) {

return orderAndConstraint(constraint);

}

}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch194__end__17=(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint));
do {
{
if (!( (  tomMatch194__end__17.isEmptyAndConstraint()  ||  (tomMatch194__end__17== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( ((tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch194__end__17.getHeadAndConstraint() ):(tomMatch194__end__17))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( ((tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch194__end__17.getHeadAndConstraint() ):(tomMatch194__end__17))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {

return orderAndConstraint(
tom_append_list_AndConstraint(tom_get_slice_AndConstraint((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)),tomMatch194__end__17, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ), tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(orderConstraints((( ((tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch194__end__17.getHeadAndConstraint() ):(tomMatch194__end__17))),tom_append_list_AndConstraint((( ((tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch194__end__17.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ));


}
}
if ( (  tomMatch194__end__17.isEmptyAndConstraint()  ||  (tomMatch194__end__17== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch194__end__17=(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint));
} else {
tomMatch194__end__17=(( ((tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch194__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch194__end__17.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch194__end__17==(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) ));
}
}

}


}

return constraint;
}

/*
* swap array[i] and array[j]
* @return true if a modification has been performed
*/
private boolean swap(Constraint[] array,int i,int j) {
boolean res = array[i]!=array[j];
Constraint tmp = array[i];
array[i] = array[j];
array[j] = tmp;
return res;
}

/*
* (X*,i,Y*,j,Z*) -> (X*,Y*,j,i,Z*)
* @return true if a modification has been performed
*/
private boolean buildXYjiZ(Constraint[] array,int i,int j) {
boolean res = array[i]!=array[j];
Constraint tmp = array[i];
for(int k=i+1 ; k<j+1 ; k++) {
array[k-1] = array[k];
res |= (array[k]!= tmp);
}
array[j] = tmp;
return res;
}

/*
* (X*,i,Y*,j,Z*) -> (X*,j,i,Y*,Z*)
* @return true if a modification has been performed
*/
private boolean buildXjiYZ(Constraint[] array,int i,int j) {
boolean res = array[i]!=array[j];
Constraint tmp = array[i];
array[i]=array[j];
for(int k=j-1 ; k>i ; k--) {
array[k+1] = array[k];
res |= (array[k]!= tmp);
}
array[i+1]=tmp;
return res;
}

/*
* (X*,i,Y*,j,Z*) -> (X*,i,j,Y*,Z*)
* @return true if a modification has been performed
*/
private boolean buildXijYZ(Constraint[] array,int i,int j) {
boolean res = array[i]!=array[j];
Constraint tmp = array[j];
for(int k=j-1 ; k>i ; k--) {
array[k+1] = array[k];
res |= (array[k]!= tmp);
}
array[i+1]=tmp;
return res;
}

private Constraint buildAndConstraintFromArray(Constraint[] array) {
Constraint list = 
 tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;
for(int i=array.length-1; i>=0 ; i--) {
Constraint tmp = array[i];
list = 
 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(tmp,tom_append_list_AndConstraint(list, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;
}
return list;
}

/**
* Puts the constraints in the good order
* We use a loop and two nested match to be more efficient
*
*/
private Constraint orderAndConstraint(Constraint constraint) {
Constraint[] array = new Constraint[constraint.length()];
array = ((tom.engine.adt.tomconstraint.types.constraint.AndConstraint)constraint).toArray(array);
boolean modification = false;
do {
//System.out.println("C = " + buildAndConstraintFromArray(array));
//System.out.println("C = " + tom.engine.tools.TomConstraintPrettyPrinter.prettyPrint(buildAndConstraintFromArray(array)));
block: {
/*
* first version: 
* start from 1 to ignore the first constraint which is only due to renaming subjects
* not correct because some %match do not have a subject
*
* second version:
* start from 0
*/
for(int i=0 ; i<array.length-1 ; i++) {
loop_j: for(int j=i+1 ; j<array.length ; j++) {
Constraint first = array[i];
Constraint second = array[j];
//System.out.println("first  = " + first);
//System.out.println("second = " + second);

{
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_2= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getSubject() ;
if ( (tomMatch195_2 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_2) instanceof tom.engine.adt.code.types.bqterm.Subterm) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_8= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getSubject() ;
if ( (tomMatch195_8 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_8) instanceof tom.engine.adt.code.types.bqterm.SymbolOf) ) {
if ( ( tomMatch195_2.getGroundTerm() == tomMatch195_8.getGroundTerm() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_17= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getSubject() ;
if ( (tomMatch195_17 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_17) instanceof tom.engine.adt.code.types.bqterm.SymbolOf) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_20= tomMatch195_17.getGroundTerm() ;
if ( (tomMatch195_20 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_20) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_26= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getPattern() ;
if ( (tomMatch195_26 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_26) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
if ( ( tomMatch195_20.getAstName() == tomMatch195_26.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
}
}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_35= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getSubject() ;
if ( (tomMatch195_35 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_35) instanceof tom.engine.adt.code.types.bqterm.SymbolOf) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_38= tomMatch195_35.getGroundTerm() ;
if ( (tomMatch195_38 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_38) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_44= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getPattern() ;
if ( (tomMatch195_44 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_44) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
if ( ( tomMatch195_38.getAstName() == tomMatch195_44.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
}
}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.AntiMatchConstraint) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {

modification |= buildXYjiZ(array,i,j);
break block;

}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_61= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getPattern() ;
if ( (tomMatch195_61 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_61) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tom_name= tomMatch195_61.getAstName() ;
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_67= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getVariable() ;
if ( (tomMatch195_67 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_67) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (tom_name== tomMatch195_67.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195_73= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getc() ;
if ( (tomMatch195_73 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch195_73) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_76= tomMatch195_73.getVariable() ;
if ( (tomMatch195_76 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_76) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (tom_name== tomMatch195_76.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
}
}

}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_88= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getPattern() ;
if ( (tomMatch195_88 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_88) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tom_name= tomMatch195_88.getAstName() ;
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_94= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getIndex() ;
if ( (tomMatch195_94 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_94) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (tom_name== tomMatch195_94.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195_100= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getc() ;
if ( (tomMatch195_100 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch195_100) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_103= tomMatch195_100.getIndex() ;
if ( (tomMatch195_103 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_103) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (tom_name== tomMatch195_103.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
}
}

}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_113= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getPattern() ;
boolean tomMatch195_119= false ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_117= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_118= null ;
if ( (tomMatch195_113 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_113) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch195_119= true ;
tomMatch195_117=tomMatch195_113;

}
} else {
if ( (tomMatch195_113 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_113) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch195_119= true ;
tomMatch195_118=tomMatch195_113;

}
}
}
}
}
if (tomMatch195_119) {

try {

tom_make_TopDown(tom_make_HasBQTerm(tomMatch195_113)).visitLight(
(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)));
} catch(VisitFailure ex) {
modification |= buildXjiYZ(array,i,j);
break block;
}


}

}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).isEmptyOrConstraintDisjunction()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195_125=(( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getHeadOrConstraintDisjunction() ):((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))));
if ( (tomMatch195_125 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195__end__128=tomMatch195_125;
do {
{
if (!( (  tomMatch195__end__128.isEmptyAndConstraint()  ||  (tomMatch195__end__128== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195_132=(( ((tomMatch195__end__128 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch195__end__128 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch195__end__128.getHeadAndConstraint() ):(tomMatch195__end__128));
if ( (tomMatch195_132 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch195_132) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_131= tomMatch195_132.getPattern() ;
boolean tomMatch195_137= false ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_135= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_136= null ;
if ( (tomMatch195_131 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_131) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch195_137= true ;
tomMatch195_135=tomMatch195_131;

}
} else {
if ( (tomMatch195_131 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_131) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch195_137= true ;
tomMatch195_136=tomMatch195_131;

}
}
}
}
}
if (tomMatch195_137) {

try {

tom_make_TopDown(tom_make_HasBQTerm(tomMatch195_131)).visitLight(
(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)));
} catch(VisitFailure ex) {
modification |= buildXjiYZ(array,i,j);
break block;
}


}

}
}
}
if ( (  tomMatch195__end__128.isEmptyAndConstraint()  ||  (tomMatch195__end__128== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch195__end__128=tomMatch195_125;
} else {
tomMatch195__end__128=(( ((tomMatch195__end__128 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch195__end__128 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch195__end__128.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch195__end__128==tomMatch195_125) ));
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).isEmptyOrConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195_143=(( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getHeadOrConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))));
if ( (tomMatch195_143 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195__end__146=tomMatch195_143;
do {
{
if (!( (  tomMatch195__end__146.isEmptyAndConstraint()  ||  (tomMatch195__end__146== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195_150=(( ((tomMatch195__end__146 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch195__end__146 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch195__end__146.getHeadAndConstraint() ):(tomMatch195__end__146));
if ( (tomMatch195_150 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch195_150) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_149= tomMatch195_150.getPattern() ;
boolean tomMatch195_155= false ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_153= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_154= null ;
if ( (tomMatch195_149 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_149) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch195_155= true ;
tomMatch195_153=tomMatch195_149;

}
} else {
if ( (tomMatch195_149 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_149) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch195_155= true ;
tomMatch195_154=tomMatch195_149;

}
}
}
}
}
if (tomMatch195_155) {

try {

tom_make_TopDown(tom_make_HasBQTerm(tomMatch195_149)).visitLight(
(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)));
} catch(VisitFailure ex) {
modification |= buildXjiYZ(array,i,j);
break block;
}


}

}
}
}
if ( (  tomMatch195__end__146.isEmptyAndConstraint()  ||  (tomMatch195__end__146== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch195__end__146=tomMatch195_143;
} else {
tomMatch195__end__146=(( ((tomMatch195__end__146 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch195__end__146 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch195__end__146.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch195__end__146==tomMatch195_143) ));
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_160= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getSubject() ;
boolean tomMatch195_178= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch195_165= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch195_163= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch195_166= null ;
if ( (tomMatch195_160 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_160) instanceof tom.engine.adt.code.types.bqterm.ListHead) ) {
{
tomMatch195_178= true ;
tomMatch195_165=tomMatch195_160;
tomMatch195_163= tomMatch195_165.getVariable() ;

}
} else {
if ( (tomMatch195_160 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_160) instanceof tom.engine.adt.code.types.bqterm.ListTail) ) {
{
tomMatch195_178= true ;
tomMatch195_166=tomMatch195_160;
tomMatch195_163= tomMatch195_166.getVariable() ;

}
}
}
}
}
if (tomMatch195_178) {
 tom.engine.adt.code.types.BQTerm  tom_v=tomMatch195_163;
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195_167= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getc() ;
if ( (tomMatch195_167 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch195_167) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {
if ( (tom_v== tomMatch195_167.getVariable() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {
if ( (tom_v== (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getVariable() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}

}
}

}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_183= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getSubject() ;
if ( (tomMatch195_183 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_183) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch195_186= tomMatch195_183.getExp() ;
if ( (tomMatch195_186 instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch195_186) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {
 tom.engine.adt.code.types.BQTerm  tom_v= tomMatch195_186.getVariable() ;
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch195_192= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getc() ;
if ( (tomMatch195_192 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch195_192) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {
if ( (tom_v== tomMatch195_192.getIndex() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {
if ( (tom_v== (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getIndex() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}

}
}
}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_207= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getSubject() ;
if ( (tomMatch195_207 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_207) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
 tom.engine.adt.code.types.BQTerm  tom_v=tomMatch195_207;
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tom_subjectSecond= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getSubject() ;
if ( (((Object)tom_subjectSecond) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom_subjectSecond)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom_subjectSecond))) instanceof tom.engine.adt.code.types.bqterm.VariableHeadList) ) {
if ( (tom_v== (( tom.engine.adt.code.types.BQTerm )((Object)tom_subjectSecond)).getEnd() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
if ( (((Object)tom_subjectSecond) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom_subjectSecond)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom_subjectSecond))) instanceof tom.engine.adt.code.types.bqterm.VariableHeadArray) ) {
if ( (tom_v== (( tom.engine.adt.code.types.BQTerm )((Object)tom_subjectSecond)).getEndIndex() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}

}
}
}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_225= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getSubject() ;
if ( (tomMatch195_225 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_225) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch195_228= tomMatch195_225.getExp() ;
if ( (tomMatch195_228 instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch195_228) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch195_231= tomMatch195_228.getSource() ;
if ( (tomMatch195_231 instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch195_231) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {
if ( ( tomMatch195_231.getAstTerm() == (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getBQTerm() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
}
}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_243= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getBQTerm() ;
if ( (tomMatch195_243 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_243) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_249= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getPattern() ;
if ( (tomMatch195_249 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_249) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
if ( ( tomMatch195_243.getAstName() == tomMatch195_249.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch195_258= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getBQTerm() ;
if ( (tomMatch195_258 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch195_258) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_264= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getPattern() ;
if ( (tomMatch195_264 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_264) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
if ( ( tomMatch195_258.getAstName() == tomMatch195_264.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
}
}
}
}
}

}
{
if ( (((Object)first) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)first))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch195_273= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)first)).getPattern() ;
if ( (tomMatch195_273 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch195_273) instanceof tom.engine.adt.tomterm.types.tomterm.TestVar) ) {
if ( (((Object)second) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)second))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
if ( ( tomMatch195_273.getVariable() == (( tom.engine.adt.tomconstraint.types.Constraint )((Object)second)).getPattern() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
}
}
}
}
}

}


}
// end %match
}
}
//System.out.println("after ordering = " + tom.engine.tools.TomConstraintPrettyPrinter.prettyPrint(buildAndConstraintFromArray(array)));

return buildAndConstraintFromArray(array);
}// block
} while (modification == true);
return buildAndConstraintFromArray(array);
}

/**
* Checks to see if the term is inside
*/


public static class HasBQTerm extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomterm.types.TomTerm  term;
public HasBQTerm( tom.engine.adt.tomterm.types.TomTerm  term) {
super(( new tom.library.sl.Identity() ));
this.term=term;
}
public  tom.engine.adt.tomterm.types.TomTerm  getterm() {
return term;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {
return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch196_5= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch196_4= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch196_3= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch196_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch196_5= true ;
tomMatch196_3=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));
tomMatch196_1= tomMatch196_3.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch196_5= true ;
tomMatch196_4=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));
tomMatch196_1= tomMatch196_4.getAstName() ;

}
}
}
}
}
if (tomMatch196_5) {
{
{
if ( (((Object)term) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch197_5= false ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch197_4= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch197_3= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch197_1= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)term)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)term))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch197_5= true ;
tomMatch197_3=(( tom.engine.adt.tomterm.types.TomTerm )((Object)term));
tomMatch197_1= tomMatch197_3.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)term)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)term))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch197_5= true ;
tomMatch197_4=(( tom.engine.adt.tomterm.types.TomTerm )((Object)term));
tomMatch197_1= tomMatch197_4.getAstName() ;

}
}
}
}
}
if (tomMatch197_5) {

if(
tomMatch196_1== 
tomMatch197_1) {
throw new VisitFailure();
}


}

}

}

}



}

}

}

}
return _visit_BQTerm(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_HasBQTerm( tom.engine.adt.tomterm.types.TomTerm  t0) { 
return new HasBQTerm(t0);
}


/**
* Translates constraints into expressions
*/
private Expression constraintsToExpressions(Constraint constraint) {

{
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).isEmptyAndConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {

return 
 tom.engine.adt.tomexpression.types.expression.And.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getHeadAndConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))))), constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )))) ;


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).isEmptyOrConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) {

return 
 tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getHeadOrConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))))), tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ))), tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ) ) ;


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).isEmptyOrConstraintDisjunction()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() )  ) )) {

return 
 tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getHeadOrConstraintDisjunction() ):((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))))), tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getTailOrConstraintDisjunction() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() ))), tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ) ) ;


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
boolean tomMatch198_16= false ;
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch198_14= null ;
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch198_15= null ;
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
{
tomMatch198_16= true ;
tomMatch198_14=(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint));

}
} else {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {
{
tomMatch198_16= true ;
tomMatch198_15=(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint));

}
}
}
}
}
if (tomMatch198_16) {

return 
 tom.engine.adt.tomexpression.types.expression.ConstraintToExpression.make((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) ;


}

}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.AntiMatchConstraint) ) {

return 
 tom.engine.adt.tomexpression.types.expression.AntiMatchExpression.make(constraintsToExpressions( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getConstraint() )) ;


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {

return 
 tom.engine.adt.tomexpression.types.expression.Negation.make(constraintsToExpressions( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getc() )) ;


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {

return getConstraintGenerator().genIsEmptyList(
 (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getOpname() ,
 (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getVariable() );


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {

return 
 tom.engine.adt.tomexpression.types.expression.IsEmptyArray.make( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getOpname() ,  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getVariable() ,  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getIndex() ) ;


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {

return 
 tom.engine.adt.tomexpression.types.expression.IsSort.make( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getAstType() ,  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getBQTerm() ) ;


}
}
}

}


}

throw new TomRuntimeException("PreGenerator.constraintsToExpressions - strange constraint:" + constraint);
}
}
