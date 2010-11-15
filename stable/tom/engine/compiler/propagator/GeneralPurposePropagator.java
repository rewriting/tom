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
package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.code.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.*;
import tom.engine.TomBase;
import tom.engine.tools.SymbolTable;
import java.util.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.compiler.Compiler;

/**
* A propagator that contains rules that don't depend on the theory (or that are applicable for more than one)
*/
public class GeneralPurposePropagator implements IBasePropagator {

//--------------------------------------------------------


  private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {
    if( l1.isEmptyconcTomName() ) {
      return l2;
    } else if( l2.isEmptyconcTomName() ) {
      return l1;
    } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {
      return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;
    } else {
      return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {
    if( l1.isEmptyconcSlot() ) {
      return l2;
    } else if( l2.isEmptyconcSlot() ) {
      return l1;
    } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {
      return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;
    } else {
      return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;
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
  
  private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {
    if( l1.isEmptyconcConstraint() ) {
      return l2;
    } else if( l2.isEmptyconcConstraint() ) {
      return l1;
    } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {
      return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;
    } else {
      return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;
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


private static Strategy makeTopDownWhenConstraint(Strategy s) {
return 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( makeWhenConstraint(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?s:new tom.library.sl.Sequence(s,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) )) ); 
}

public static class WhenConstraint extends tom.library.sl.AbstractStrategyBasic {

private  tom.library.sl.Strategy  s;

public WhenConstraint( tom.library.sl.Strategy  s) {
super(( new tom.library.sl.Identity() ));
this.s=s;
}

public  tom.library.sl.Strategy  gets() {
return s;
}

public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
stratChilds[1] = gets();
return stratChilds;
}

public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
s = ( tom.library.sl.Strategy ) children[1];
return this;
}

public int getChildCount() {
return 2;
}

public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
case 1: return gets();
default: throw new IndexOutOfBoundsException();

}
}

public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
case 1: s = ( tom.library.sl.Strategy )child;
return this;
default: throw new IndexOutOfBoundsException();
}
}

public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
return s.visitLight(v,introspector);
}
return any.visitLight(v,introspector);
}

}


private static  tom.library.sl.Strategy  makeWhenConstraint( tom.library.sl.Strategy  t0) { return new WhenConstraint(t0);}    

//--------------------------------------------------------




private Compiler compiler;  
private ConstraintPropagator constraintPropagator; 

public GeneralPurposePropagator(Compiler myCompiler, ConstraintPropagator myConstraintPropagator) {
this.compiler = myCompiler;
this.constraintPropagator = myConstraintPropagator;
}

public Compiler getCompiler() {
return this.compiler;
}

public ConstraintPropagator getConstraintPropagator() {
return this.constraintPropagator;
}

public Constraint propagate(Constraint constraint) throws VisitFailure {
return 
( makeTopDownWhenConstraint(tom_make_GeneralPropagations(this)) ).visitLight(constraint);
}	


public static class GeneralPropagations extends tom.library.sl.AbstractStrategyBasic {
private  GeneralPurposePropagator  gpp;
public GeneralPropagations( GeneralPurposePropagator  gpp) {
super(( new tom.library.sl.Identity() ));
this.gpp=gpp;
}
public  GeneralPurposePropagator  getgpp() {
return gpp;
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
if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch166_1= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() ;
if ( (tomMatch166_1 instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch166_5= tomMatch166_1.getTomTerm() ;
boolean tomMatch166_8= false ;
if ( (tomMatch166_5 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch166_8= true ;
} else {
if ( (tomMatch166_5 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
tomMatch166_8= true ;
}
}
if (tomMatch166_8) {
 tom.engine.adt.tomterm.types.TomTerm  tom_term=tomMatch166_5;
 tom.engine.adt.code.types.BQTerm  tom_s= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() ;
 tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getAstType() ;

return 
 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.AntiMatchConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_term, tom_s, tom_aType) ) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(gpp.getConstraintPropagator().performDetach( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_term, tom_s, tom_aType) ), tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ;


}

}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch166__end__13=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);
do {
{
if (!( (  tomMatch166__end__13.isEmptyAndConstraint()  ||  (tomMatch166__end__13== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
if ( ((( ((tomMatch166__end__13 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__13 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__13.getHeadAndConstraint() ):(tomMatch166__end__13)) instanceof tom.engine.adt.tomconstraint.types.constraint.AntiMatchConstraint) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch166_14=(( ((tomMatch166__end__13 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__13 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__13.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch166__end__17=tomMatch166_14;
do {
{
if (!( (  tomMatch166__end__17.isEmptyAndConstraint()  ||  (tomMatch166__end__17== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
if ( ((( ((tomMatch166__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__17.getHeadAndConstraint() ):(tomMatch166__end__17)) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {

return 
tom_append_list_AndConstraint(tom_get_slice_AndConstraint((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg),tomMatch166__end__13, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),tom_append_list_AndConstraint(tom_get_slice_AndConstraint(tomMatch166_14,tomMatch166__end__17, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ), tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((tomMatch166__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__17.getHeadAndConstraint() ):(tomMatch166__end__17)), tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((tomMatch166__end__13 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__13 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__13.getHeadAndConstraint() ):(tomMatch166__end__13)),tom_append_list_AndConstraint((( ((tomMatch166__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__17.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ) ));        


}
}
if ( (  tomMatch166__end__17.isEmptyAndConstraint()  ||  (tomMatch166__end__17== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch166__end__17=tomMatch166_14;
} else {
tomMatch166__end__17=(( ((tomMatch166__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__17 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__17.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch166__end__17==tomMatch166_14) ));
}
}
if ( (  tomMatch166__end__13.isEmptyAndConstraint()  ||  (tomMatch166__end__13== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch166__end__13=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);
} else {
tomMatch166__end__13=(( ((tomMatch166__end__13 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__13 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__13.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch166__end__13==(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch166__end__26=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);
do {
{
if (!( (  tomMatch166__end__26.isEmptyAndConstraint()  ||  (tomMatch166__end__26== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch166_30=(( ((tomMatch166__end__26 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__26 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__26.getHeadAndConstraint() ):(tomMatch166__end__26));
if ( (tomMatch166_30 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch166_29= tomMatch166_30.getPattern() ;
boolean tomMatch166_36= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch166_31= null ;
if ( (tomMatch166_29 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch166_36= true ;
tomMatch166_31= tomMatch166_29.getAstName() ;

}
} else {
if ( (tomMatch166_29 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch166_36= true ;
tomMatch166_31= tomMatch166_29.getAstName() ;

}
}
}
if (tomMatch166_36) {
 tom.engine.adt.tomname.types.TomName  tom_varName=tomMatch166_31;
 tom.engine.adt.tomconstraint.types.Constraint  tom_Y=(( ((tomMatch166__end__26 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__26 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__26.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
boolean tomMatch166_35= false ;
if ( (tomMatch166_31 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {
if ( (tom_varName==tomMatch166_31) ) {
tomMatch166_35= true ;
}
}
if (!(tomMatch166_35)) {

// we cannot cache already renamed variables, because disjunctions have to be taken into account
// for example: g(x) || f(x,x) -> ...
Constraint res = (Constraint)
( makeTopDownWhenConstraint(tom_make_ReplaceMatchConstraint(tom_varName,gpp)) ).visitLight(
tom_Y);
if(res != 
tom_Y) {
return 
tom_append_list_AndConstraint(tom_get_slice_AndConstraint((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg),tomMatch166__end__26, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ), tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((tomMatch166__end__26 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__26 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__26.getHeadAndConstraint() ):(tomMatch166__end__26)),tom_append_list_AndConstraint(res, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) );
}


}

}

}
}
if ( (  tomMatch166__end__26.isEmptyAndConstraint()  ||  (tomMatch166__end__26== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) ) {
tomMatch166__end__26=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);
} else {
tomMatch166__end__26=(( ((tomMatch166__end__26 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (tomMatch166__end__26 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( tomMatch166__end__26.getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ));
}

}
} while(!( (tomMatch166__end__26==(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch166_38= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() ;
boolean tomMatch166_46= false ;
 tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch166_42= null ;
if ( (tomMatch166_38 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch166_46= true ;
tomMatch166_42= tomMatch166_38.getConstraints() ;

}
} else {
if ( (tomMatch166_38 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch166_46= true ;
tomMatch166_42= tomMatch166_38.getConstraints() ;

}
}
}
if (tomMatch166_46) {
 tom.engine.adt.tomterm.types.TomTerm  tom_term=tomMatch166_38;
boolean tomMatch166_45= false ;
if ( ((tomMatch166_42 instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || (tomMatch166_42 instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {
if ( tomMatch166_42.isEmptyconcConstraint() ) {
tomMatch166_45= true ;
}
}
if (!(tomMatch166_45)) {

Constraint result = gpp.getConstraintPropagator().performDetach(
(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg));
if(
tom_term.isVariable()) {
result =

 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_term.setConstraints( tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ),  (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() ,  (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getAstType() ) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(result, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ;
}
return result;


}

}

}
}

}


}
return _visit_Constraint(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_GeneralPropagations( GeneralPurposePropagator  t0) { 
return new GeneralPropagations(t0);
}
// end %strategy

/**
* Detach sublists
* 
* Make sure that the sublists in a list are replaced by star variables 
* this is only happening when the lists and the sublists have the same name
* 
* conc(X*,conc(some_pattern),Y*) << t -> conc(X*,Z*,Y*) << t /\ conc(some_pattern) << Z*  
* 
*/ 
public Constraint detachSublists(Constraint constraint) {
// will hold the new slots of t
SlotList newSlots = 
 tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
Constraint constraintList = 
 tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;

{
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch167_1= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getPattern() ;
if ( (tomMatch167_1 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch167_5= tomMatch167_1.getNameList() ;
 tom.engine.adt.tomslot.types.SlotList  tomMatch167_6= tomMatch167_1.getSlots() ;
if ( ((tomMatch167_5 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch167_5 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
if (!( tomMatch167_5.isEmptyconcTomName() )) {
if ( ( tomMatch167_5.getHeadconcTomName()  instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.tomname.types.TomName  tom_name= tomMatch167_5.getHeadconcTomName() ;
if (  tomMatch167_5.getTailconcTomName() .isEmptyconcTomName() ) {
 tom.engine.adt.tomslot.types.SlotList  tom_slots=tomMatch167_6;
 tom.engine.adt.tomterm.types.TomTerm  tom_t=tomMatch167_1;
 tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getAstType() ;
boolean tomMatch167_13= false ;
if ( ((tomMatch167_6 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch167_6 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {
if ( (tom_slots==tomMatch167_6) ) {
if ( tomMatch167_6.isEmptyconcSlot() ) {
tomMatch167_13= true ;
}
}
}
if (!(tomMatch167_13)) {
{
{
if ( (tom_slots instanceof tom.engine.adt.tomslot.types.SlotList) ) {
if ( (((( tom.engine.adt.tomslot.types.SlotList )tom_slots) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )tom_slots) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {
 tom.engine.adt.tomslot.types.SlotList  tomMatch168__end__4=(( tom.engine.adt.tomslot.types.SlotList )tom_slots);
do {
{
if (!( tomMatch168__end__4.isEmptyconcSlot() )) {
 tom.engine.adt.tomslot.types.Slot  tom_slot= tomMatch168__end__4.getHeadconcSlot() ;

matchSlot:  
{
{
if ( (tom_slot instanceof tom.engine.adt.tomslot.types.Slot) ) {
if ( ((( tom.engine.adt.tomslot.types.Slot )tom_slot) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_appl= (( tom.engine.adt.tomslot.types.Slot )tom_slot).getAppl() ;
 tom.engine.adt.tomslot.types.Slot  tom_ps=(( tom.engine.adt.tomslot.types.Slot )tom_slot);
if ( (tom_name instanceof tom.engine.adt.tomname.types.TomName) ) {
 tom.engine.adt.tomname.types.TomName  tom_childName=(( tom.engine.adt.tomname.types.TomName )tom_name);
if ( (tom_appl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_appl) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch169_6= (( tom.engine.adt.tomterm.types.TomTerm )tom_appl).getNameList() ;
if ( ((tomMatch169_6 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch169_6 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
if (!( tomMatch169_6.isEmptyconcTomName() )) {
if ( (tom_childName== tomMatch169_6.getHeadconcTomName() ) ) {
if (  tomMatch169_6.getTailconcTomName() .isEmptyconcTomName() ) {

BQTerm freshVariable = getCompiler().getFreshVariableStar(getCompiler().getTermTypeFromTerm(
tom_t));                
constraintList =

 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_appl, freshVariable, tom_aType) ,tom_append_list_AndConstraint(constraintList, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;
newSlots = 
tom_append_list_concSlot(newSlots, tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make(tom_ps.setAppl(TomBase.convertFromBQVarToVar(freshVariable)), tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) );
break matchSlot;


}
}
}
}
}
}
if ( (tom_appl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_appl) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch169_8= (( tom.engine.adt.tomterm.types.TomTerm )tom_appl).getTomTerm() ;
if ( (tomMatch169_8 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch169_10= tomMatch169_8.getNameList() ;
if ( ((tomMatch169_10 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch169_10 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
if (!( tomMatch169_10.isEmptyconcTomName() )) {
if ( (tom_childName== tomMatch169_10.getHeadconcTomName() ) ) {
if (  tomMatch169_10.getTailconcTomName() .isEmptyconcTomName() ) {

BQTerm freshVariable = getCompiler().getFreshVariableStar(getCompiler().getTermTypeFromTerm(
tom_t));                
constraintList =

 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_appl, freshVariable, tom_aType) ,tom_append_list_AndConstraint(constraintList, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;
newSlots = 
tom_append_list_concSlot(newSlots, tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make(tom_ps.setAppl(TomBase.convertFromBQVarToVar(freshVariable)), tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) );
break matchSlot;


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
if ( (tom_slot instanceof tom.engine.adt.tomslot.types.Slot) ) {
if ( (tom_name instanceof tom.engine.adt.tomname.types.TomName) ) {

newSlots = 
tom_append_list_concSlot(newSlots, tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make((( tom.engine.adt.tomslot.types.Slot )tom_slot), tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) );


}
}

}


}



}
if ( tomMatch168__end__4.isEmptyconcSlot() ) {
tomMatch168__end__4=(( tom.engine.adt.tomslot.types.SlotList )tom_slots);
} else {
tomMatch168__end__4= tomMatch168__end__4.getTailconcSlot() ;
}

}
} while(!( (tomMatch168__end__4==(( tom.engine.adt.tomslot.types.SlotList )tom_slots)) ));
}
}

}

}

return 
 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_t.setSlots(newSlots),  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getSubject() , tom_aType) ,tom_append_list_AndConstraint(constraintList, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )) ;   


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

// never gets here
throw new TomRuntimeException("GeneralPurposePropagator:detachSublists - unexpected result");
}

/*
* x << s -> fresh << s ^ fresh==x
*/

public static class ReplaceMatchConstraint extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomname.types.TomName  varName;
private  GeneralPurposePropagator  gpp;
public ReplaceMatchConstraint( tom.engine.adt.tomname.types.TomName  varName,  GeneralPurposePropagator  gpp) {
super(( new tom.library.sl.Identity() ));
this.varName=varName;
this.gpp=gpp;
}
public  tom.engine.adt.tomname.types.TomName  getvarName() {
return varName;
}
public  GeneralPurposePropagator  getgpp() {
return gpp;
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
if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch170_1= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() ;
boolean tomMatch170_8= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch170_6= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch170_5= null ;
if ( (tomMatch170_1 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch170_8= true ;
tomMatch170_5= tomMatch170_1.getAstName() ;
tomMatch170_6= tomMatch170_1.getAstType() ;

}
} else {
if ( (tomMatch170_1 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch170_8= true ;
tomMatch170_5= tomMatch170_1.getAstName() ;
tomMatch170_6= tomMatch170_1.getAstType() ;

}
}
}
if (tomMatch170_8) {
 tom.engine.adt.tomtype.types.TomType  tom_type=tomMatch170_6;
 tom.engine.adt.tomterm.types.TomTerm  tom_var=tomMatch170_1;
 tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getAstType() ;
if ( (varName==tomMatch170_5) ) {

BQTerm freshVar = 
tom_var.isVariable() ? gpp.getCompiler().getFreshVariable(
tom_type) : gpp.getCompiler().getFreshVariableStar(
tom_type);
return

 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(TomBase.convertFromBQVarToVar(freshVar),  (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() , tom_aType) , tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make( tom.engine.adt.tomterm.types.tomterm.TestVar.make(TomBase.convertFromBQVarToVar(freshVar)) , TomBase.convertFromVarToBQVar(tom_var), tom_aType) , tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ;


}
}

}
}

}

}
return _visit_Constraint(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_ReplaceMatchConstraint( tom.engine.adt.tomname.types.TomName  t0,  GeneralPurposePropagator  t1) { 
return new ReplaceMatchConstraint(t0,t1);
}


}
