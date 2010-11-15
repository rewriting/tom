/*
*
* TOM - To One Matching Compiler
*
* Copyright (c) 2000-2010, INPL, INRIA
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
    } else if(( (l1 instanceof tom.library.sl.Sequence) )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {
        return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );
      } else {
        return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );
      }
    } else {
      return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 ==null )) {
      return l2;
    } else if(( l2 ==null )) {
      return l1;
    } else if(( (l1 instanceof tom.library.sl.Choice) )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {
        return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );
      } else {
        return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );
      }
    } else {
      return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 == null )) {
      return l2;
    } else if(( l2 == null )) {
      return l1;
    } else if(( (l1 instanceof tom.library.sl.SequenceId) )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {
        return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) );
      } else {
        return ( (tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) );
      }
    } else {
      return ( (l2==null)?l1:new tom.library.sl.SequenceId(l1,l2) );
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return ( (( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin):new tom.library.sl.SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)) );
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 ==null )) {
      return l2;
    } else if(( l2 ==null )) {
      return l1;
    } else if(( (l1 instanceof tom.library.sl.ChoiceId) )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {
        return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) );
      } else {
        return ( (tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) );
      }
    } else {
      return ( (l2==null)?l1:new tom.library.sl.ChoiceId(l1,l2) );
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return ( (( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin):new tom.library.sl.ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)) );
  }
  private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { 
return ( 
( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?s:new tom.library.sl.Choice(s,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?s:new tom.library.sl.Sequence(s,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?s:new tom.library.sl.Sequence(s,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ),( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.ChoiceId(v,( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ))

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
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
boolean tomMatch145_12= false ;
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch145__end__7=(( tom.engine.adt.tomconstraint.types.Constraint )constraint);
do {
{
if (!( (  tomMatch145__end__7.isEmptyAndConstraint()  ||  (tomMatch145__end__7== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
if ( (((( ((tomMatch145__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch145__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch145__end__7.getHeadAndConstraint() ):(tomMatch145__end__7)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( ((tomMatch145__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch145__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch145__end__7.getHeadAndConstraint() ):(tomMatch145__end__7)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {
tomMatch145_12= true ;
}
}
if ( (  tomMatch145__end__7.isEmptyAndConstraint()  ||  (tomMatch145__end__7== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch145__end__7=(( tom.engine.adt.tomconstraint.types.Constraint )constraint);
} else {
tomMatch145__end__7=(( ((tomMatch145__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch145__end__7 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch145__end__7.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch145__end__7==(( tom.engine.adt.tomconstraint.types.Constraint )constraint)) ));
}
if (!(tomMatch145_12)) {

return orderAndConstraint(constraint);

}

}
}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch145__end__17=(( tom.engine.adt.tomconstraint.types.Constraint )constraint);
do {
{
if (!( (  tomMatch145__end__17.isEmptyAndConstraint()  ||  (tomMatch145__end__17== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
if ( (((( ((tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch145__end__17.getHeadAndConstraint() ):(tomMatch145__end__17)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( ((tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch145__end__17.getHeadAndConstraint() ):(tomMatch145__end__17)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {

return orderAndConstraint(
tom_append_list_AndConstraint(tom_get_slice_AndConstraint((( tom.engine.adt.tomconstraint.types.Constraint )constraint),tomMatch145__end__17, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ), tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(orderConstraints((( ((tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch145__end__17.getHeadAndConstraint() ):(tomMatch145__end__17))),tom_append_list_AndConstraint((( ((tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch145__end__17.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ));


}
}
if ( (  tomMatch145__end__17.isEmptyAndConstraint()  ||  (tomMatch145__end__17== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch145__end__17=(( tom.engine.adt.tomconstraint.types.Constraint )constraint);
} else {
tomMatch145__end__17=(( ((tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch145__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch145__end__17.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch145__end__17==(( tom.engine.adt.tomconstraint.types.Constraint )constraint)) ));
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
list = 
 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(array[i],tom_append_list_AndConstraint(list, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;
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
//    System.out.println("C = " + buildAndConstraintFromArray(array));
block: {
// start from 1 to ignore the first constraint which is only due to  
// renaming subjects 
for(int i=1 ; i<array.length-1 ; i++) {
loop_j: for(int j=i+1 ; j<array.length ; j++) {
Constraint first = array[i];
Constraint second = array[j];
//System.out.println("first  = " + first);
//System.out.println("second = " + second);

{
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_2= (( tom.engine.adt.tomconstraint.types.Constraint )first).getSubject() ;
if ( (tomMatch146_2 instanceof tom.engine.adt.code.types.bqterm.Subterm) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_6= (( tom.engine.adt.tomconstraint.types.Constraint )second).getSubject() ;
if ( (tomMatch146_6 instanceof tom.engine.adt.code.types.bqterm.SymbolOf) ) {
if ( ( tomMatch146_2.getGroundTerm() == tomMatch146_6.getGroundTerm() ) ) {

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
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_13= (( tom.engine.adt.tomconstraint.types.Constraint )first).getSubject() ;
if ( (tomMatch146_13 instanceof tom.engine.adt.code.types.bqterm.SymbolOf) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_15= tomMatch146_13.getGroundTerm() ;
if ( (tomMatch146_15 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_19= (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ;
if ( (tomMatch146_19 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
if ( ( tomMatch146_15.getAstName() == tomMatch146_19.getAstName() ) ) {

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
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_26= (( tom.engine.adt.tomconstraint.types.Constraint )first).getSubject() ;
if ( (tomMatch146_26 instanceof tom.engine.adt.code.types.bqterm.SymbolOf) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_28= tomMatch146_26.getGroundTerm() ;
if ( (tomMatch146_28 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_32= (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ;
if ( (tomMatch146_32 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
if ( ( tomMatch146_28.getAstName() == tomMatch146_32.getAstName() ) ) {

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
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.AntiMatchConstraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {

modification |= buildXYjiZ(array,i,j);
break block;

}
}
}
}

}
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_45= (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ;
if ( (tomMatch146_45 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tom_name= tomMatch146_45.getAstName() ;
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_49= (( tom.engine.adt.tomconstraint.types.Constraint )first).getVariable() ;
if ( (tomMatch146_49 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (tom_name== tomMatch146_49.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_53= (( tom.engine.adt.tomconstraint.types.Constraint )first).getc() ;
if ( (tomMatch146_53 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_55= tomMatch146_53.getVariable() ;
if ( (tomMatch146_55 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (tom_name== tomMatch146_55.getAstName() ) ) {

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
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_65= (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ;
if ( (tomMatch146_65 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tom_name= tomMatch146_65.getAstName() ;
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_69= (( tom.engine.adt.tomconstraint.types.Constraint )first).getIndex() ;
if ( (tomMatch146_69 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (tom_name== tomMatch146_69.getAstName() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_73= (( tom.engine.adt.tomconstraint.types.Constraint )first).getc() ;
if ( (tomMatch146_73 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_75= tomMatch146_73.getIndex() ;
if ( (tomMatch146_75 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (tom_name== tomMatch146_75.getAstName() ) ) {

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
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_85= (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ;
boolean tomMatch146_88= false ;
if ( (tomMatch146_85 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch146_88= true ;
} else {
if ( (tomMatch146_85 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch146_88= true ;
}
}
if (tomMatch146_88) {

try {

tom_make_TopDown(tom_make_HasTerm(tomMatch146_85)).visitLight(
 (( tom.engine.adt.tomconstraint.types.Constraint )first).getSubject() );
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
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )second).isEmptyOrConstraintDisjunction()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )second)== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_96=(( (((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )second).getHeadOrConstraintDisjunction() ):((( tom.engine.adt.tomconstraint.types.Constraint )second)));
if ( (tomMatch146_96 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146__end__99=tomMatch146_96;
do {
{
if (!( (  tomMatch146__end__99.isEmptyAndConstraint()  ||  (tomMatch146__end__99== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_103=(( ((tomMatch146__end__99 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch146__end__99 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch146__end__99.getHeadAndConstraint() ):(tomMatch146__end__99));
if ( (tomMatch146_103 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_102= tomMatch146_103.getPattern() ;
boolean tomMatch146_105= false ;
if ( (tomMatch146_102 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch146_105= true ;
} else {
if ( (tomMatch146_102 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch146_105= true ;
}
}
if (tomMatch146_105) {

try {

tom_make_TopDown(tom_make_HasTerm(tomMatch146_102)).visitLight(
 (( tom.engine.adt.tomconstraint.types.Constraint )first).getSubject() );
} catch(VisitFailure ex) {
modification |= buildXjiYZ(array,i,j);
break block;
}


}

}
}
if ( (  tomMatch146__end__99.isEmptyAndConstraint()  ||  (tomMatch146__end__99== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch146__end__99=tomMatch146_96;
} else {
tomMatch146__end__99=(( ((tomMatch146__end__99 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch146__end__99 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch146__end__99.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch146__end__99==tomMatch146_96) ));
}
}
}
}
}
}

}
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_110= (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ;
boolean tomMatch146_113= false ;
if ( (tomMatch146_110 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch146_113= true ;
} else {
if ( (tomMatch146_110 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch146_113= true ;
}
}
if (tomMatch146_113) {

try {

tom_make_TopDown(tom_make_HasTerm(tomMatch146_110)).visitLight(
 (( tom.engine.adt.tomconstraint.types.Constraint )first).getRight() );
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
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )second).isEmptyOrConstraintDisjunction()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )second)== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_121=(( (((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )second).getHeadOrConstraintDisjunction() ):((( tom.engine.adt.tomconstraint.types.Constraint )second)));
if ( (tomMatch146_121 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146__end__124=tomMatch146_121;
do {
{
if (!( (  tomMatch146__end__124.isEmptyAndConstraint()  ||  (tomMatch146__end__124== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_128=(( ((tomMatch146__end__124 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch146__end__124 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch146__end__124.getHeadAndConstraint() ):(tomMatch146__end__124));
if ( (tomMatch146_128 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_127= tomMatch146_128.getPattern() ;
boolean tomMatch146_130= false ;
if ( (tomMatch146_127 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch146_130= true ;
} else {
if ( (tomMatch146_127 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch146_130= true ;
}
}
if (tomMatch146_130) {

try {

tom_make_TopDown(tom_make_HasTerm(tomMatch146_127)).visitLight(
 (( tom.engine.adt.tomconstraint.types.Constraint )first).getRight() );
} catch(VisitFailure ex) {
modification |= buildXjiYZ(array,i,j);
break block;
}


}

}
}
if ( (  tomMatch146__end__124.isEmptyAndConstraint()  ||  (tomMatch146__end__124== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch146__end__124=tomMatch146_121;
} else {
tomMatch146__end__124=(( ((tomMatch146__end__124 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch146__end__124 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch146__end__124.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch146__end__124==tomMatch146_121) ));
}
}
}
}
}
}

}
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_135= (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ;
boolean tomMatch146_138= false ;
if ( (tomMatch146_135 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch146_138= true ;
} else {
if ( (tomMatch146_135 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch146_138= true ;
}
}
if (tomMatch146_138) {

try {

tom_make_TopDown(tom_make_HasTerm(tomMatch146_135)).visitLight(
 (( tom.engine.adt.tomconstraint.types.Constraint )first).getLeft() );
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
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )second).isEmptyOrConstraintDisjunction()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )second)== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_146=(( (((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )second).getHeadOrConstraintDisjunction() ):((( tom.engine.adt.tomconstraint.types.Constraint )second)));
if ( (tomMatch146_146 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146__end__149=tomMatch146_146;
do {
{
if (!( (  tomMatch146__end__149.isEmptyAndConstraint()  ||  (tomMatch146__end__149== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_153=(( ((tomMatch146__end__149 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch146__end__149 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch146__end__149.getHeadAndConstraint() ):(tomMatch146__end__149));
if ( (tomMatch146_153 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_152= tomMatch146_153.getPattern() ;
boolean tomMatch146_155= false ;
if ( (tomMatch146_152 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch146_155= true ;
} else {
if ( (tomMatch146_152 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch146_155= true ;
}
}
if (tomMatch146_155) {

try {

tom_make_TopDown(tom_make_HasTerm(tomMatch146_152)).visitLight(
 (( tom.engine.adt.tomconstraint.types.Constraint )first).getLeft() );
} catch(VisitFailure ex) {
modification |= buildXjiYZ(array,i,j);
break block;
}


}

}
}
if ( (  tomMatch146__end__149.isEmptyAndConstraint()  ||  (tomMatch146__end__149== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch146__end__149=tomMatch146_146;
} else {
tomMatch146__end__149=(( ((tomMatch146__end__149 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch146__end__149 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch146__end__149.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch146__end__149==tomMatch146_146) ));
}
}
}
}
}
}

}
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_160= (( tom.engine.adt.tomconstraint.types.Constraint )first).getSubject() ;
boolean tomMatch146_172= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch146_162= null ;
if ( (tomMatch146_160 instanceof tom.engine.adt.code.types.bqterm.ListHead) ) {
{
tomMatch146_172= true ;
tomMatch146_162= tomMatch146_160.getVariable() ;

}
} else {
if ( (tomMatch146_160 instanceof tom.engine.adt.code.types.bqterm.ListTail) ) {
{
tomMatch146_172= true ;
tomMatch146_162= tomMatch146_160.getVariable() ;

}
}
}
if (tomMatch146_172) {
 tom.engine.adt.code.types.BQTerm  tom_v=tomMatch146_162;
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_164= (( tom.engine.adt.tomconstraint.types.Constraint )second).getc() ;
if ( (tomMatch146_164 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {
if ( (tom_v== tomMatch146_164.getVariable() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {
if ( (tom_v== (( tom.engine.adt.tomconstraint.types.Constraint )second).getVariable() ) ) {

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
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_177= (( tom.engine.adt.tomconstraint.types.Constraint )first).getSubject() ;
if ( (tomMatch146_177 instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch146_179= tomMatch146_177.getExp() ;
if ( (tomMatch146_179 instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {
 tom.engine.adt.code.types.BQTerm  tom_v= tomMatch146_179.getVariable() ;
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch146_183= (( tom.engine.adt.tomconstraint.types.Constraint )second).getc() ;
if ( (tomMatch146_183 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {
if ( (tom_v== tomMatch146_183.getIndex() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
}
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {
if ( (tom_v== (( tom.engine.adt.tomconstraint.types.Constraint )second).getIndex() ) ) {

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
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_195= (( tom.engine.adt.tomconstraint.types.Constraint )first).getSubject() ;
if ( (tomMatch146_195 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
 tom.engine.adt.code.types.BQTerm  tom_v=tomMatch146_195;
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tom_subjectSecond= (( tom.engine.adt.tomconstraint.types.Constraint )second).getSubject() ;
if ( (tom_subjectSecond instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tom_subjectSecond) instanceof tom.engine.adt.code.types.bqterm.VariableHeadList) ) {
if ( (tom_v== (( tom.engine.adt.code.types.BQTerm )tom_subjectSecond).getEnd() ) ) {

modification |= swap(array,i,j);
break block;

}
}
}
if ( (tom_subjectSecond instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tom_subjectSecond) instanceof tom.engine.adt.code.types.bqterm.VariableHeadArray) ) {
if ( (tom_v== (( tom.engine.adt.code.types.BQTerm )tom_subjectSecond).getEndIndex() ) ) {

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
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_208= (( tom.engine.adt.tomconstraint.types.Constraint )first).getSubject() ;
if ( (tomMatch146_208 instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch146_210= tomMatch146_208.getExp() ;
if ( (tomMatch146_210 instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch146_212= tomMatch146_210.getSource() ;
if ( (tomMatch146_212 instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {
if ( ( tomMatch146_212.getAstTerm() == (( tom.engine.adt.tomconstraint.types.Constraint )second).getBQTerm() ) ) {

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
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_221= (( tom.engine.adt.tomconstraint.types.Constraint )first).getBQTerm() ;
if ( (tomMatch146_221 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_225= (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ;
if ( (tomMatch146_225 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
if ( ( tomMatch146_221.getAstName() == tomMatch146_225.getAstName() ) ) {

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
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch146_232= (( tom.engine.adt.tomconstraint.types.Constraint )first).getBQTerm() ;
if ( (tomMatch146_232 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_236= (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ;
if ( (tomMatch146_236 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
if ( ( tomMatch146_232.getAstName() == tomMatch146_236.getAstName() ) ) {

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
{
if ( (first instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )first) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch146_243= (( tom.engine.adt.tomconstraint.types.Constraint )first).getPattern() ;
if ( (tomMatch146_243 instanceof tom.engine.adt.tomterm.types.tomterm.TestVar) ) {
if ( (second instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )second) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
if ( ( tomMatch146_243.getVariable() == (( tom.engine.adt.tomconstraint.types.Constraint )second).getPattern() ) ) {

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
// end %match
}
}
return buildAndConstraintFromArray(array);
}// block
} while (modification == true);
return buildAndConstraintFromArray(array);
}

/**
* Checks to see if the term is inside
*/

public static class HasTerm extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomterm.types.TomTerm  term;
public HasTerm( tom.engine.adt.tomterm.types.TomTerm  term) {
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
if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
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
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
{
if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {

if (
 (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() == 
 (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() && 
 (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() == 
 (( tom.engine.adt.tomterm.types.TomTerm )term).getAstType() ) {
throw new VisitFailure();
}


}
}

}

}



}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
{
if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {

if (
 (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() == 
 (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() && 
 (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() == 
 (( tom.engine.adt.tomterm.types.TomTerm )term).getAstType() ) {
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
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {

if(
(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)== term) { throw new VisitFailure(); }


}

}

}
return _visit_TomTerm(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_HasTerm( tom.engine.adt.tomterm.types.TomTerm  t0) { 
return new HasTerm(t0);
}


/**
* Translates constraints into expressions
*/
private Expression constraintsToExpressions(Constraint constraint) {

{
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyAndConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {

return 
 tom.engine.adt.tomexpression.types.expression.And.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadAndConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint)))), constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )))) ;


}
}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyOrConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) {

return 
 tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadOrConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint)))), tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ))), tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ) ) ;


}
}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyOrConstraintDisjunction()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() )  ) )) {

return 
 tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadOrConstraintDisjunction() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint)))), tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make(constraintsToExpressions((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailOrConstraintDisjunction() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraintDisjunction.make() ))), tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ) ) ;


}
}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
boolean tomMatch151_14= false ;
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
tomMatch151_14= true ;
} else {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {
tomMatch151_14= true ;
}
}
if (tomMatch151_14) {

return 
 tom.engine.adt.tomexpression.types.expression.ConstraintToExpression.make((( tom.engine.adt.tomconstraint.types.Constraint )constraint)) ;


}

}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.AntiMatchConstraint) ) {

return 
 tom.engine.adt.tomexpression.types.expression.AntiMatchExpression.make(constraintsToExpressions( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getConstraint() )) ;


}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {

return 
 tom.engine.adt.tomexpression.types.expression.Negation.make(constraintsToExpressions( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getc() )) ;


}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {

return getConstraintGenerator().genIsEmptyList(
 (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getOpname() ,
 (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getVariable() );


}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {

return 
 tom.engine.adt.tomexpression.types.expression.IsEmptyArray.make( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getOpname() ,  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getVariable() ,  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getIndex() ) ;


}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {

return 
 tom.engine.adt.tomexpression.types.expression.IsSort.make( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getAstType() ,  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getBQTerm() ) ;


}
}

}


}

throw new TomRuntimeException("PreGenerator.constraintsToExpressions - strange constraint:" + constraint);
}
}
