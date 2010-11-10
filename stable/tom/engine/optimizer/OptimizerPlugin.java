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
* Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
*
**/

package tom.engine.optimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.TomBase;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;

import tom.engine.exception.TomRuntimeException;
import tom.engine.TomMessage;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.PILFactory;
import tom.engine.tools.Tools;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.library.sl.*;

/**
* The TomOptimizer plugin.
*/
public class OptimizerPlugin extends TomGenericPlugin {



  private static   String  tom_append_list_concString( String l1,  String  l2) {
    if( l1.length()==0 ) {
      return l2;
    } else if( l2.length()==0 ) {
      return l1;
    } else if(  l1.substring(1) .length()==0 ) {
      return   l1.charAt(0) +l2 ;
    } else {
      return   l1.charAt(0) +tom_append_list_concString( l1.substring(1) ,l2) ;
    }
  }
  private static   String  tom_get_slice_concString( String  begin,  String  end, String  tail) {
    if( begin.equals(end) ) {
      return tail;
    } else if( end.equals(tail)  && ( end.length()==0  ||  end.equals( "" ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return   begin.charAt(0) +( String )tom_get_slice_concString( begin.substring(1) ,end,tail) ;
  }
  
  private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {
    if( l1.isEmptyconcInstruction() ) {
      return l2;
    } else if( l2.isEmptyconcInstruction() ) {
      return l1;
    } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {
      return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;
    } else {
      return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;
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
private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) ))) ))

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
private static  tom.library.sl.Strategy  tom_make_InnermostId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) ),( null )) )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) ),( null )) )) )) ))

;
}


@SuppressWarnings("unchecked")
private static java.util.ArrayList concArrayListAppend(Object o, java.util.ArrayList l) {
java.util.ArrayList res = (java.util.ArrayList)l.clone();
res.add(o);
return res;
}


/** some output suffixes */
private static final String OPTIMIZED_SUFFIX = ".tfix.optimized";

/** the declared options string*/
private static final String DECLARED_OPTIONS =
"<options>" +
"<boolean name='optimize' altName='O' description='Optimize generated code: perform inlining' value='true'/>" +
"<boolean name='optimize2' altName='O2' description='Optimize generated code: discrimination tree' value='false'/>" +
"<boolean name='prettyPIL' altName='pil' description='PrettyPrint IL' value='false'/>" +
"</options>";

public PlatformOptionList getDeclaredOptionList() {
return OptionParser.xmlToOptionList(OptimizerPlugin.DECLARED_OPTIONS);
}

public void optionChanged(String optionName, Object optionValue) {
if(optionName.equals("optimize2") && ((Boolean)optionValue).booleanValue() ) {
setOptionValue("pretty", Boolean.TRUE);
}
}

// this static field is necessary for %strategy instructions that generate static code
private static PILFactory factory = new PILFactory();
private static Logger logger = Logger.getLogger("tom.engine.optimizer.OptimizerPlugin");

/** Constructor */
public OptimizerPlugin() {
super("OptimizerPlugin");
}

public void run(Map informationTracker) {
//System.out.println("(debug) I'm in the Tom optimizer : TSM"+getStreamManager().toString());
if(getOptionBooleanValue("optimize") || getOptionBooleanValue("optimize2")) {
/* Initialize strategies */
long startChrono = System.currentTimeMillis();
boolean intermediate = getOptionBooleanValue("intermediate");
try {
Code renamedTerm = (Code)getWorkingTerm();
if(getOptionBooleanValue("optimize2")) {
Strategy optStrategy2 = 
( (( (( null )==null)?tom_make_InnermostId(( (( (( null )==null)?( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )==null)?( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ),( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )==null)?( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ),( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )) ),( (( null )==null)?( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ),( null )) )) )
                )
              :new tom.library.sl.Sequence(tom_make_InnermostId(( (( (( null )==null)?( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )==null)?( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ),( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )==null)?( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ),( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )) ),( (( null )==null)?( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ),( null )) )) )
                )
              ,( null )) )==null)?tom_make_InnermostId(( (( (( null )==null)?tom_make_NopElimAndFlatten():new tom.library.sl.ChoiceId(tom_make_NopElimAndFlatten(),( null )) )==null)?tom_make_NormExpr(this):new tom.library.sl.ChoiceId(tom_make_NormExpr(this),( (( null )==null)?tom_make_NopElimAndFlatten():new tom.library.sl.ChoiceId(tom_make_NopElimAndFlatten(),( null )) )) )):new tom.library.sl.Sequence(tom_make_InnermostId(( (( (( null )==null)?tom_make_NopElimAndFlatten():new tom.library.sl.ChoiceId(tom_make_NopElimAndFlatten(),( null )) )==null)?tom_make_NormExpr(this):new tom.library.sl.ChoiceId(tom_make_NormExpr(this),( (( null )==null)?tom_make_NopElimAndFlatten():new tom.library.sl.ChoiceId(tom_make_NopElimAndFlatten(),( null )) )) )),( (( null )==null)?tom_make_InnermostId(( (( (( null )==null)?( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )==null)?( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ),( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )==null)?( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ),( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )) ),( (( null )==null)?( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ),( null )) )) )
                )
              :new tom.library.sl.Sequence(tom_make_InnermostId(( (( (( null )==null)?( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )==null)?( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ),( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )==null)?( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ),( (( null )==null)?( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinRepeatId(( (( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )==null)?( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ):new tom.library.sl.SequenceId(( (( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )==null)?tom_make_BlockFusion():new tom.library.sl.ChoiceId(tom_make_BlockFusion(),( (( null )==null)?tom_make_IfFusion():new tom.library.sl.ChoiceId(tom_make_IfFusion(),( null )) )) ),( (( null )==null)?tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten()),( null )) )) )) ),( null )) )) ),( (( null )==null)?( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ):new tom.library.sl.ChoiceId(( (( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )==null)?tom_make_InterBlock(this):new tom.library.sl.SequenceId(tom_make_InterBlock(this),( (( null )==null)?tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )):new tom.library.sl.SequenceId(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten()) )),( null )) )) ),( null )) )) )
                )
              ,( null )) )) );
renamedTerm = optStrategy2.visitLight(renamedTerm);
renamedTerm = 
( new tom.library.sl.BuiltinBottomUp(tom_make_Inline( tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() )) ).visit(renamedTerm);
renamedTerm = optStrategy2.visitLight(renamedTerm);
//System.out.println("opt renamedTerm = " + renamedTerm);
} else if(getOptionBooleanValue("optimize")) {
Strategy optStrategy = 
( (( (( null )==null)?( new tom.library.sl.BuiltinBottomUp(tom_make_Inline( tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinBottomUp(tom_make_Inline( tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() )) ),( null )) )==null)?tom_make_InnermostId(( (( (( null )==null)?tom_make_NopElimAndFlatten():new tom.library.sl.ChoiceId(tom_make_NopElimAndFlatten(),( null )) )==null)?tom_make_NormExpr(this):new tom.library.sl.ChoiceId(tom_make_NormExpr(this),( (( null )==null)?tom_make_NopElimAndFlatten():new tom.library.sl.ChoiceId(tom_make_NopElimAndFlatten(),( null )) )) )):new tom.library.sl.Sequence(tom_make_InnermostId(( (( (( null )==null)?tom_make_NopElimAndFlatten():new tom.library.sl.ChoiceId(tom_make_NopElimAndFlatten(),( null )) )==null)?tom_make_NormExpr(this):new tom.library.sl.ChoiceId(tom_make_NormExpr(this),( (( null )==null)?tom_make_NopElimAndFlatten():new tom.library.sl.ChoiceId(tom_make_NopElimAndFlatten(),( null )) )) )),( (( null )==null)?( new tom.library.sl.BuiltinBottomUp(tom_make_Inline( tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() )) ):new tom.library.sl.Sequence(( new tom.library.sl.BuiltinBottomUp(tom_make_Inline( tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() )) ),( null )) )) );

renamedTerm = optStrategy.visit(renamedTerm);
}
setWorkingTerm(renamedTerm);

// verbose
TomMessage.info(logger,null,0,TomMessage.tomOptimizationPhase,Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
} catch (Exception e) {
TomMessage.error(logger,getStreamManager().getInputFileName(),0,TomMessage.exceptionMessage,e.getMessage());
e.printStackTrace();
return;
}
if(intermediate) {
Tools.generateOutput(getStreamManager().getOutputFileName() + OPTIMIZED_SUFFIX,
(Code)getWorkingTerm() );
}
} else {
// not active plugin
TomMessage.info(logger,null,0,TomMessage.optimizerNotActive);
}
if(getOptionBooleanValue("prettyPIL")) {
System.out.println(factory.prettyPrintCompiledMatch(factory.remove((Code)getWorkingTerm())));
}
}

private final static String PREFIX = "tom_";
private static String extractRealName(String name) {
if(name.startsWith(PREFIX)) {
return name.substring(PREFIX.length());
}
return name;
}


public static class Inline extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomconstraint.types.Constraint  context;
public Inline( tom.engine.adt.tomconstraint.types.Constraint  context) {
super(( new tom.library.sl.Identity() ));
this.context=context;
}
public  tom.engine.adt.tomconstraint.types.Constraint  getcontext() {
return context;
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
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch196_2= (( tom.engine.adt.code.types.BQTerm )tom__arg).getHeadTerm() ;
 tom.engine.adt.tomname.types.TomName  tom_name= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;
if ( (tomMatch196_2 instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch196_5= tomMatch196_2.getExp() ;
if ( (tomMatch196_5 instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) {
if ( (tom_name== tomMatch196_5.getAstName() ) ) {

return 
 tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetSliceList.make(tom_name,  tomMatch196_5.getVariableBeginAST() ,  tomMatch196_5.getVariableEndAST() ,  tom.engine.adt.code.types.bqterm.BuildAppendList.make(tom_name,  tomMatch196_5.getTail() ,  (( tom.engine.adt.code.types.BQTerm )tom__arg).getTailTerm() ) ) ) ;


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
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch197_3= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;
boolean tomMatch197_33= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch197_7= null ;
if ( (tomMatch197_3 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch197_33= true ;
tomMatch197_7= tomMatch197_3.getAstName() ;

}
} else {
if ( (tomMatch197_3 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch197_33= true ;
tomMatch197_7= tomMatch197_3.getAstName() ;

}
}
}
if (tomMatch197_33) {
 tom.engine.adt.tomname.types.TomName  tom_name=tomMatch197_7;
 tom.engine.adt.tomexpression.types.Expression  tom_exp= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSource() ;
 tom.engine.adt.tominstruction.types.Instruction  tom_body= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAstInstruction() ;
boolean tomMatch197_32= false ;
if ( (tomMatch197_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tomMatch197_17= tomMatch197_7.getString() ;
if ( tomMatch197_17!= null && tomMatch197_17 instanceof String ) {
if (!( tomMatch197_17.length()==0 )) {
if ( 't'== tomMatch197_17.charAt(0)  ) {
 String  tomMatch197_20= tomMatch197_17.substring(1) ;
if (!( tomMatch197_20.length()==0 )) {
if ( 'o'== tomMatch197_20.charAt(0)  ) {
 String  tomMatch197_21= tomMatch197_20.substring(1) ;
if (!( tomMatch197_21.length()==0 )) {
if ( 'm'== tomMatch197_21.charAt(0)  ) {
 String  tomMatch197_22= tomMatch197_21.substring(1) ;
if (!( tomMatch197_22.length()==0 )) {
if ( '_'== tomMatch197_22.charAt(0)  ) {
if ( (tom_name==tomMatch197_7) ) {
tomMatch197_32= true ;
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
if (!(tomMatch197_32)) {
if ( (tom_exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom_exp) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch197_9= (( tom.engine.adt.tomexpression.types.Expression )tom_exp).getAstTerm() ;
boolean tomMatch197_30= false ;
if ( (tomMatch197_9 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
tomMatch197_30= true ;
} else {
if ( (tomMatch197_9 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
tomMatch197_30= true ;
}
}
if (tomMatch197_30) {

return 
tom_make_TopDown(tom_make_replaceVariableByExpression(tom_name,tom_exp)).visitLight(
tom_body);


}

}
}
if ( (tom_exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom_exp) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch197_12= (( tom.engine.adt.tomexpression.types.Expression )tom_exp).getSource() ;
if ( (tomMatch197_12 instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch197_14= tomMatch197_12.getAstTerm() ;
boolean tomMatch197_31= false ;
if ( (tomMatch197_14 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
tomMatch197_31= true ;
} else {
if ( (tomMatch197_14 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
tomMatch197_31= true ;
}
}
if (tomMatch197_31) {

return 
tom_make_TopDown(tom_make_replaceVariableByExpression(tom_name,tom_exp)).visitLight(
tom_body);


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
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch197_35= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;
boolean tomMatch197_41= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch197_39= null ;
if ( (tomMatch197_35 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch197_41= true ;
tomMatch197_39= tomMatch197_35.getAstName() ;

}
} else {
if ( (tomMatch197_35 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch197_41= true ;
tomMatch197_39= tomMatch197_35.getAstName() ;

}
}
}
if (tomMatch197_41) {
 tom.engine.adt.tomname.types.TomName  tom_name=tomMatch197_39;
 tom.engine.adt.tomexpression.types.Expression  tom_exp= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSource() ;

String varName = ""; // real name of the variable (i.e. without the tom_ prefix)

{
{
if ( (tom_name instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tom_name) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
varName = 
extractRealName( (( tom.engine.adt.tomname.types.TomName )tom_name).getString() ); 

}
}

}

}


//System.out.println("varName = " + varName);
//System.out.println("body = " + `body);

// count the occurence of name
InfoVariable infoBody = new InfoVariable();
infoBody.setAssignment(
tom_exp,getPosition());
getEnvironment().down(3); // 3 = body

tom_make_computeOccurencesLet(tom_name,infoBody).visit(getEnvironment());
getEnvironment().up();

if(infoBody.modifiedAssignmentVariables) {
/* do nothing */
if(varName.length() > 0) {
TomMessage.info(logger,null,0,TomMessage.cannotInline,0,varName);
}
} else {
int mult = infoBody.readCount;
//System.out.println(`name + " --> " + mult);
if(mult == 0) { // name is not used
// suppress the Let
if(varName.length() > 0) {
// varName is not empty when the variable is a user defined variable (with a name)
// and not a variable generated by the compiler itself
// TODO: check variable occurence in RawAction
InfoVariable infoContext = new InfoVariable();

tom_make_computeOccurencesLet(tom_name,infoContext).visit(
context);
if(infoContext.readCount<=1 && !varName.startsWith("_")) {
// variables introduced by renaming starts with a '_'
// verify linearity in case of variables from the pattern
// warning to indicate that this var is unused in the rhs
Option orgTrack = TomBase.findOriginTracking(
tomMatch197_35.getOptions());
TomMessage.warning(logger,orgTrack.getFileName(), orgTrack.getLine(),
TomMessage.unusedVariable,varName);
TomMessage.info(logger,null,0,TomMessage.remove,mult,varName);
}
}
return 
 (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAstInstruction() ;
} else if(mult == 1) {
//test if variables contained in the exp to assign have not been
//modified between the last assignment and the read
if(varName.length() > 0) {
TomMessage.info(logger,null,0,TomMessage.inline,mult,varName);
}
Position current = getPosition();
getEnvironment().goToPosition(infoBody.usePosition);
getEnvironment().setSubject(
 tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make(tom_exp) );
getEnvironment().goToPosition(current);
Instruction newlet = (Instruction) getEnvironment().getSubject();
// return only the body
return (Instruction) newlet.getChildAt(2);
} else {
/* do nothing */
if(varName.length() > 0) {
TomMessage.info(logger,null,0,TomMessage.doNothing,mult,varName);
}
}
}


}

}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch197_43= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;
boolean tomMatch197_50= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch197_47= null ;
if ( (tomMatch197_43 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch197_50= true ;
tomMatch197_47= tomMatch197_43.getAstName() ;

}
} else {
if ( (tomMatch197_43 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch197_50= true ;
tomMatch197_47= tomMatch197_43.getAstName() ;

}
}
}
if (tomMatch197_50) {
if ( (tomMatch197_47 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.tomname.types.TomName  tom_name=tomMatch197_47;

/*
* do not optimize Variable(TomNumber...) because LetRef X*=GetTail(X*) in ...
* is not correctly handled
* we must check that X notin exp
*/
String varName = "";

{
{
if ( (tom_name instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tom_name) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
varName = 
extractRealName( (( tom.engine.adt.tomname.types.TomName )tom_name).getString() ); 

}
}

}

}


InfoVariable info = new InfoVariable();
info.setAssignment(
 (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSource() ,getPosition());
getEnvironment().down(3);
//computeOccurencesLetRef on the body

tom_make_computeOccurencesLetRef(tom_name,info).visit(getEnvironment());
getEnvironment().up();
int mult = info.readCount;
Position readPos = info.usePosition;

if(info.assignment==null) {
if(varName.length() > 0) {
TomMessage.info(logger,null,0,TomMessage.cannotInline,0,varName);
}
} else {
//System.out.println(`name + " --> " + mult);
// 0 -> unused variable
// suppress the letref and all the corresponding letassigns in the body
if(mult == 0) {
// why this test?
if(varName.length() > 0) {
// TODO: check variable occurence in RawAction
info = new InfoVariable();

tom_make_computeOccurencesLetRef(tom_name,info).visit(
context);
if(info.readCount<=1 && !varName.startsWith("_")) {
// verify linearity in case of variables from the pattern
// warning to indicate that this var is unused in the rhs
Option orgTrack = TomBase.findOriginTracking(
tomMatch197_43.getOptions());
TomMessage.warning(logger,orgTrack.getFileName(), orgTrack.getLine(),
TomMessage.unusedVariable,varName);
TomMessage.info(logger,null,0,TomMessage.remove,mult,varName);
}
}
return 
tom_make_CleanAssign(tom_name).visitLight(
 (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAstInstruction() );
} else if(mult == 1) {
//test if variables contained in the exp to assign have not been
//modified between the last assignment and the use
//test if the last assignment is not in a conditional sub-block
//relatively to the variable use
Position src = info.usePosition;
Position dest = info.assignmentPosition;

// find the positive part of src-dest
Position positivePart = (Position) dest.sub(src).getCanonicalPath();
while(positivePart.length()>0 && positivePart.getHead()<0) {
positivePart = (Position) positivePart.getTail();
}
// find the common ancestor of src and dest
Position commonAncestor = (Position) dest.add(positivePart.inverse()).getCanonicalPath();
Position current = getPosition();
getEnvironment().goToPosition(commonAncestor);
try {
//this strategy fails if from common ancestor and along the path
//positivePart there is an instruction If, WhileDo or DoWhile
positivePart.getOmegaPath(
( new tom.library.sl.Not(( (( (( (( null )==null)? new tom.engine.adt.tominstruction.strategy.instruction.Is_WhileDo():new tom.library.sl.Choice( new tom.engine.adt.tominstruction.strategy.instruction.Is_WhileDo(),( null )) )==null)? new tom.engine.adt.tominstruction.strategy.instruction.Is_DoWhile():new tom.library.sl.Choice( new tom.engine.adt.tominstruction.strategy.instruction.Is_DoWhile(),( (( null )==null)? new tom.engine.adt.tominstruction.strategy.instruction.Is_WhileDo():new tom.library.sl.Choice( new tom.engine.adt.tominstruction.strategy.instruction.Is_WhileDo(),( null )) )) )==null)? new tom.engine.adt.tominstruction.strategy.instruction.Is_If():new tom.library.sl.Choice( new tom.engine.adt.tominstruction.strategy.instruction.Is_If(),( (( (( null )==null)? new tom.engine.adt.tominstruction.strategy.instruction.Is_WhileDo():new tom.library.sl.Choice( new tom.engine.adt.tominstruction.strategy.instruction.Is_WhileDo(),( null )) )==null)? new tom.engine.adt.tominstruction.strategy.instruction.Is_DoWhile():new tom.library.sl.Choice( new tom.engine.adt.tominstruction.strategy.instruction.Is_DoWhile(),( (( null )==null)? new tom.engine.adt.tominstruction.strategy.instruction.Is_WhileDo():new tom.library.sl.Choice( new tom.engine.adt.tominstruction.strategy.instruction.Is_WhileDo(),( null )) )) )) )) )).visit(
getEnvironment());
if(varName.length() > 0) {
TomMessage.info(logger,null,0,TomMessage.inline,mult,varName);
}
getEnvironment().goToPosition(readPos);
BQTerm value = 
 tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make(info.assignment) ;
getEnvironment().setSubject(value);
getEnvironment().goToPosition(current);

tom_make_CleanAssign(tom_name).visit(getEnvironment());
Instruction newletref = (Instruction) getEnvironment().getSubject();
// return only the body
//System.out.println("inlineletref");
return (Instruction) newletref.getChildAt(2);
} catch(VisitFailure e) {
getEnvironment().goToPosition(current);
if(varName.length() > 0) {
TomMessage.info(logger,null,0,TomMessage.noInline,mult,varName);
}
}
} else {
/* do nothing: traversal() */
if(varName.length() > 0) {
TomMessage.info(logger,null,0,TomMessage.doNothing,mult,varName);
}
}
}


}
}

}
}

}


}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
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
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {
return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_Inline( tom.engine.adt.tomconstraint.types.Constraint  t0) { 
return new Inline(t0);
}


private static class InfoVariable {

private HashSet<TomName> assignmentVariables = new HashSet<TomName>();
public int readCount=0;
public boolean modifiedAssignmentVariables=false; // true when exp depends on a variable which is modified by Assign
public Position usePosition; // position where is used the variable
/*
* for LetRef
*/
private Position assignmentPosition;
private Expression assignment;

public InfoVariable() {}

public void setAssignment(Expression newAssignment,Position newAssignmentPosition) {
assignment = newAssignment;
assignmentPosition = newAssignmentPosition;
assignmentVariables.clear();
try {

tom_make_TopDownCollect(tom_make_CollectVariable(assignmentVariables)).visitLight(newAssignment);
} catch(VisitFailure e) {
throw new TomRuntimeException("Error during collecting variables in "+newAssignment);
}
}

public Set getAssignmentVariables() {
return assignmentVariables;
}

public void clear() {
assignment = null;
assignmentPosition = null;
assignmentVariables.clear();
}

}


public static class CollectVariable extends tom.library.sl.AbstractStrategyBasic {
private  java.util.HashSet<TomName>  set;
public CollectVariable( java.util.HashSet<TomName>  set) {
super(( new tom.library.sl.Identity() ));
this.set=set;
}
public  java.util.HashSet<TomName>  getset() {
return set;
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
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch200_3= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch200_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch200_3= true ;
tomMatch200_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch200_3= true ;
tomMatch200_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
}
}
if (tomMatch200_3) {

set.add(
tomMatch200_1);
//stop to visit this branch (like "return false" with traversal)
throw new VisitFailure();


}

}

}

}
return _visit_BQTerm(tom__arg,introspector);

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
}
private static  tom.library.sl.Strategy  tom_make_CollectVariable( java.util.HashSet<TomName>  t0) { 
return new CollectVariable(t0);
}


/* strategies for Let inlining (using cps) */
// comp = AssignCase(RawActionCase(comp,BaseCase(all(comp),fail())),fail())

private static  tom.library.sl.Strategy  tom_make_computeOccurencesLet( tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) { 
return tom_make_Try(( new tom.library.sl.Mu(( new tom.library.sl.MuVar("comp") ),tom_make_computeOccurenceLet_AssignCase(tom_make_computeOccurenceLet_RawActionCase(( new tom.library.sl.MuVar("comp") ),tom_make_computeOccurenceLet_BaseCase(( new tom.library.sl.All(( new tom.library.sl.MuVar("comp") )) ),variableName,info),variableName,info),info)
            ) )
          )


;
}


/*
* check if a variable appearing in an expression (in Let x <- exp ...)
* is modified (by applying Assign on the variables contained in exp)
*/

public static class computeOccurenceLet_AssignCase extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.sl.Strategy  defaultCase;
private  InfoVariable  info;
public computeOccurenceLet_AssignCase( tom.library.sl.Strategy  defaultCase,  InfoVariable  info) {
super(defaultCase);
this.defaultCase=defaultCase;
this.info=info;
}
public  tom.library.sl.Strategy  getdefaultCase() {
return defaultCase;
}
public  InfoVariable  getinfo() {
return info;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
stratChilds[1] = getdefaultCase();
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
defaultCase = ( tom.library.sl.Strategy ) children[1];
return this;
}
public int getChildCount() {
return 2;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
case 1: return getdefaultCase();
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
case 1: defaultCase = ( tom.library.sl.Strategy )child; return this;
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
boolean tomMatch201_5= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch201_1= null ;
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {
{
tomMatch201_5= true ;
tomMatch201_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;

}
} else {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {
{
tomMatch201_5= true ;
tomMatch201_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;

}
}
}
if (tomMatch201_5) {
if ( (tomMatch201_1 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

if(info.getAssignmentVariables().contains(
 tomMatch201_1.getAstName() )) {
info.modifiedAssignmentVariables=true;
throw new VisitFailure();
}


}
}

}

}

}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_computeOccurenceLet_AssignCase( tom.library.sl.Strategy  t0,  InfoVariable  t1) { 
return new computeOccurenceLet_AssignCase(t0,t1);
}
public static class computeOccurenceLet_RawActionCase extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.sl.Strategy  goOnCase;
private  tom.library.sl.Strategy  cutCase;
private  tom.engine.adt.tomname.types.TomName  variableName;
private  InfoVariable  info;
public computeOccurenceLet_RawActionCase( tom.library.sl.Strategy  goOnCase,  tom.library.sl.Strategy  cutCase,  tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) {
super(cutCase);
this.goOnCase=goOnCase;
this.cutCase=cutCase;
this.variableName=variableName;
this.info=info;
}
public  tom.library.sl.Strategy  getgoOnCase() {
return goOnCase;
}
public  tom.library.sl.Strategy  getcutCase() {
return cutCase;
}
public  tom.engine.adt.tomname.types.TomName  getvariableName() {
return variableName;
}
public  InfoVariable  getinfo() {
return info;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
stratChilds[1] = getgoOnCase();
stratChilds[2] = getcutCase();
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
goOnCase = ( tom.library.sl.Strategy ) children[1];
cutCase = ( tom.library.sl.Strategy ) children[2];
return this;
}
public int getChildCount() {
return 3;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
case 1: return getgoOnCase();
case 2: return getcutCase();
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
case 1: goOnCase = ( tom.library.sl.Strategy )child; return this;
case 2: cutCase = ( tom.library.sl.Strategy )child; return this;
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {


/* recursive call of the current strategy on the first child */
Environment current = getEnvironment();
current.down(1);
try {
goOnCase.visit(current);
current.up();
return (Instruction) current.getSubject();
} catch (VisitFailure e) {
current.upLocal();
throw new VisitFailure();
}

}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
boolean tomMatch202_7= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch202_3= null ;
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {
{
tomMatch202_7= true ;
tomMatch202_3= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;

}
} else {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {
{
tomMatch202_7= true ;
tomMatch202_3= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;

}
}
}
if (tomMatch202_7) {
if ( (tomMatch202_3 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

if(variableName.equals(
 tomMatch202_3.getAstName() )) {
throw new TomRuntimeException("OptimizerPlugin: Assignment cannot be done for the variable "+variableName+" declared in a let");
}


}
}

}

}


}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_computeOccurenceLet_RawActionCase( tom.library.sl.Strategy  t0,  tom.library.sl.Strategy  t1,  tom.engine.adt.tomname.types.TomName  t2,  InfoVariable  t3) { 
return new computeOccurenceLet_RawActionCase(t0,t1,t2,t3);
}


/*
* when variableName is encountered, its position is stored
* if it appears more than once, the computation is stopped because there is no possible inlining
*/

public static class computeOccurenceLet_BaseCase extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.sl.Strategy  defaultCase;
private  tom.engine.adt.tomname.types.TomName  variableName;
private  InfoVariable  info;
public computeOccurenceLet_BaseCase( tom.library.sl.Strategy  defaultCase,  tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) {
super(defaultCase);
this.defaultCase=defaultCase;
this.variableName=variableName;
this.info=info;
}
public  tom.library.sl.Strategy  getdefaultCase() {
return defaultCase;
}
public  tom.engine.adt.tomname.types.TomName  getvariableName() {
return variableName;
}
public  InfoVariable  getinfo() {
return info;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
stratChilds[1] = getdefaultCase();
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
defaultCase = ( tom.library.sl.Strategy ) children[1];
return this;
}
public int getChildCount() {
return 2;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
case 1: return getdefaultCase();
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
case 1: defaultCase = ( tom.library.sl.Strategy )child; return this;
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch203_3= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch203_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch203_3= true ;
tomMatch203_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch203_3= true ;
tomMatch203_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
}
}
if (tomMatch203_3) {

if(variableName == 
tomMatch203_1) {
info.usePosition = getPosition();
info.readCount++;
if(info.readCount==2) { throw new VisitFailure(); }
}


}

}

}

}
return _visit_BQTerm(tom__arg,introspector);

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
}
private static  tom.library.sl.Strategy  tom_make_computeOccurenceLet_BaseCase( tom.library.sl.Strategy  t0,  tom.engine.adt.tomname.types.TomName  t1,  InfoVariable  t2) { 
return new computeOccurenceLet_BaseCase(t0,t1,t2);
}
public static class Print extends tom.library.sl.AbstractStrategyBasic {
private  String  name;
public Print( String  name) {
super(( new tom.library.sl.Identity() ));
this.name=name;
}
public  String  getname() {
return name;
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
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {

System.out.println(name + " inst: " + 
(( tom.engine.adt.tominstruction.types.Instruction )tom__arg));


}

}

}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.InstructionList  visit_InstructionList( tom.engine.adt.tominstruction.types.InstructionList  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.InstructionList) ) {

System.out.println(name + " instlist: " + 
(( tom.engine.adt.tominstruction.types.InstructionList )tom__arg));


}

}

}
return _visit_InstructionList(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.InstructionList  _visit_InstructionList( tom.engine.adt.tominstruction.types.InstructionList  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.InstructionList )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if ( (v instanceof tom.engine.adt.tominstruction.types.InstructionList) ) {
return ((T)visit_InstructionList((( tom.engine.adt.tominstruction.types.InstructionList )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}


/* strategies for LetRef inlining (using cps) */
// comp = special( comp, Basecase(comp,fail()) )

private static  tom.library.sl.Strategy  tom_make_computeOccurencesLetRef( tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) { 
return (

tom_make_Try(( new tom.library.sl.Mu(( new tom.library.sl.MuVar("comp") ),tom_make_computeOccurencesLetRef_CutCase(( new tom.library.sl.MuVar("comp") ),tom_make_computeOccurencesLetRef_BaseCase(( new tom.library.sl.All(( new tom.library.sl.MuVar("comp") )) ),variableName,info),variableName,info)
            ) ))
) 

;
}


//cases where failure is used to cut branches

public static class computeOccurencesLetRef_CutCase extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.sl.Strategy  goOnCase;
private  tom.library.sl.Strategy  defaultCase;
private  tom.engine.adt.tomname.types.TomName  variableName;
private  InfoVariable  info;
public computeOccurencesLetRef_CutCase( tom.library.sl.Strategy  goOnCase,  tom.library.sl.Strategy  defaultCase,  tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) {
super(defaultCase);
this.goOnCase=goOnCase;
this.defaultCase=defaultCase;
this.variableName=variableName;
this.info=info;
}
public  tom.library.sl.Strategy  getgoOnCase() {
return goOnCase;
}
public  tom.library.sl.Strategy  getdefaultCase() {
return defaultCase;
}
public  tom.engine.adt.tomname.types.TomName  getvariableName() {
return variableName;
}
public  InfoVariable  getinfo() {
return info;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
stratChilds[1] = getgoOnCase();
stratChilds[2] = getdefaultCase();
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
goOnCase = ( tom.library.sl.Strategy ) children[1];
defaultCase = ( tom.library.sl.Strategy ) children[2];
return this;
}
public int getChildCount() {
return 3;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
case 1: return getgoOnCase();
case 2: return getdefaultCase();
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
case 1: goOnCase = ( tom.library.sl.Strategy )child; return this;
case 2: defaultCase = ( tom.library.sl.Strategy )child; return this;
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {

/* recursive call of the current strategy on the first child */
Environment current = getEnvironment();
current.down(1);
try {
goOnCase.visit(current);
current.up();
return (Instruction) current.getSubject();
} catch (VisitFailure e) {
current.upLocal();
throw new VisitFailure();
}

}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
boolean tomMatch206_8= false ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch206_4= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch206_3= null ;
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {
{
tomMatch206_8= true ;
tomMatch206_3= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;
tomMatch206_4= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSource() ;

}
} else {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {
{
tomMatch206_8= true ;
tomMatch206_3= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;
tomMatch206_4= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSource() ;

}
}
}
if (tomMatch206_8) {
if ( (tomMatch206_3 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tom_name= tomMatch206_3.getAstName() ;

if(variableName == 
tom_name) {
info.setAssignment(
tomMatch206_4,getPosition());
} else {
if(info.getAssignmentVariables().contains(
tom_name)) {
info.clear();
}
}
/* recursive call of the current strategy on src */
Environment current = getEnvironment();
try {
current.down(2);
goOnCase.visit(current);
current.up();
return (Instruction) current.getSubject();
} catch (VisitFailure e) {
current.upLocal();
throw new VisitFailure();
}


}
}

}

}


}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_computeOccurencesLetRef_CutCase( tom.library.sl.Strategy  t0,  tom.library.sl.Strategy  t1,  tom.engine.adt.tomname.types.TomName  t2,  InfoVariable  t3) { 
return new computeOccurencesLetRef_CutCase(t0,t1,t2,t3);
}
public static class computeOccurencesLetRef_BaseCase extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.sl.Strategy  defaultCase;
private  tom.engine.adt.tomname.types.TomName  variableName;
private  InfoVariable  info;
public computeOccurencesLetRef_BaseCase( tom.library.sl.Strategy  defaultCase,  tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) {
super(defaultCase);
this.defaultCase=defaultCase;
this.variableName=variableName;
this.info=info;
}
public  tom.library.sl.Strategy  getdefaultCase() {
return defaultCase;
}
public  tom.engine.adt.tomname.types.TomName  getvariableName() {
return variableName;
}
public  InfoVariable  getinfo() {
return info;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
stratChilds[1] = getdefaultCase();
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
defaultCase = ( tom.library.sl.Strategy ) children[1];
return this;
}
public int getChildCount() {
return 2;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
case 1: return getdefaultCase();
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
case 1: defaultCase = ( tom.library.sl.Strategy )child; return this;
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch207_3= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch207_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch207_3= true ;
tomMatch207_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch207_3= true ;
tomMatch207_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
}
}
if (tomMatch207_3) {

if(variableName == 
tomMatch207_1) {
info.readCount++;
info.usePosition = getPosition();
if(info.readCount==2) {
throw new VisitFailure();
}
}


}

}

}

}
return _visit_BQTerm(tom__arg,introspector);

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
}
private static  tom.library.sl.Strategy  tom_make_computeOccurencesLetRef_BaseCase( tom.library.sl.Strategy  t0,  tom.engine.adt.tomname.types.TomName  t1,  InfoVariable  t2) { 
return new computeOccurencesLetRef_BaseCase(t0,t1,t2);
}


/*
* rename variable1 into variable2
*/

private static  tom.library.sl.Strategy  tom_make_renameVariable( tom.engine.adt.tomname.types.TomName  variable1,  tom.engine.adt.tomname.types.TomName  variable2) { 
return (
tom_make_TopDown(tom_make_renameVariableOnce(variable1,variable2))) 

;
}
public static class renameVariableOnce extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomname.types.TomName  variable1;
private  tom.engine.adt.tomname.types.TomName  variable2;
public renameVariableOnce( tom.engine.adt.tomname.types.TomName  variable1,  tom.engine.adt.tomname.types.TomName  variable2) {
super(( new tom.library.sl.Identity() ));
this.variable1=variable1;
this.variable2=variable2;
}
public  tom.engine.adt.tomname.types.TomName  getvariable1() {
return variable1;
}
public  tom.engine.adt.tomname.types.TomName  getvariable2() {
return variable2;
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
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch208_3= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch208_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch208_3= true ;
tomMatch208_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch208_3= true ;
tomMatch208_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
}
}
if (tomMatch208_3) {

if(variable1 == 
tomMatch208_1) {
return 
(( tom.engine.adt.code.types.BQTerm )tom__arg).setAstName(variable2);
}


}

}

}

}
return _visit_BQTerm(tom__arg,introspector);

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
}
private static  tom.library.sl.Strategy  tom_make_renameVariableOnce( tom.engine.adt.tomname.types.TomName  t0,  tom.engine.adt.tomname.types.TomName  t1) { 
return new renameVariableOnce(t0,t1);
}
public static class replaceVariableByExpression extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomname.types.TomName  variable;
private  tom.engine.adt.tomexpression.types.Expression  exp;
public replaceVariableByExpression( tom.engine.adt.tomname.types.TomName  variable,  tom.engine.adt.tomexpression.types.Expression  exp) {
super(( new tom.library.sl.Identity() ));
this.variable=variable;
this.exp=exp;
}
public  tom.engine.adt.tomname.types.TomName  getvariable() {
return variable;
}
public  tom.engine.adt.tomexpression.types.Expression  getexp() {
return exp;
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
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch209_3= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch209_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch209_3= true ;
tomMatch209_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch209_3= true ;
tomMatch209_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
}
}
if (tomMatch209_3) {

if(variable == 
tomMatch209_1) {
return 
 tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make(exp) ;
}


}

}

}

}
return _visit_BQTerm(tom__arg,introspector);

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
}
private static  tom.library.sl.Strategy  tom_make_replaceVariableByExpression( tom.engine.adt.tomname.types.TomName  t0,  tom.engine.adt.tomexpression.types.Expression  t1) { 
return new replaceVariableByExpression(t0,t1);
}
private static  tom.library.sl.Strategy  tom_make_CleanAssign( tom.engine.adt.tomname.types.TomName  varname) { 
return (
tom_make_TopDown(tom_make_CleanAssignOnce(varname))) 

;
}
public static class CleanAssignOnce extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomname.types.TomName  varname;
public CleanAssignOnce( tom.engine.adt.tomname.types.TomName  varname) {
super(( new tom.library.sl.Identity() ));
this.varname=varname;
}
public  tom.engine.adt.tomname.types.TomName  getvarname() {
return varname;
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
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
boolean tomMatch210_6= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch210_1= null ;
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {
{
tomMatch210_6= true ;
tomMatch210_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;

}
} else {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {
{
tomMatch210_6= true ;
tomMatch210_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;

}
}
}
if (tomMatch210_6) {
boolean tomMatch210_5= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch210_3= null ;
if ( (tomMatch210_1 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch210_5= true ;
tomMatch210_3= tomMatch210_1.getAstName() ;

}
} else {
if ( (tomMatch210_1 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch210_5= true ;
tomMatch210_3= tomMatch210_1.getAstName() ;

}
}
}
if (tomMatch210_5) {

if(
tomMatch210_3.equals(varname)) { return 
 tom.engine.adt.tominstruction.types.instruction.Nop.make() ; }


}

}

}

}

}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_CleanAssignOnce( tom.engine.adt.tomname.types.TomName  t0) { 
return new CleanAssignOnce(t0);
}


private static boolean compare(tom.library.sl.Visitable term1, tom.library.sl.Visitable term2) {
return factory.remove(term1)==factory.remove(term2);
}


public static class NopElimAndFlatten extends tom.library.sl.AbstractStrategyBasic {
public NopElimAndFlatten() {
super(( new tom.library.sl.Identity() ));
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
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch211_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;
if ( ((tomMatch211_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch211_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch211__end__6=tomMatch211_1;
do {
{
if (!( tomMatch211__end__6.isEmptyconcInstruction() )) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch211_10= tomMatch211__end__6.getHeadconcInstruction() ;
if ( (tomMatch211_10 instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {

TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"flatten");
return 
 tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch211_1,tomMatch211__end__6, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ),tom_append_list_concInstruction( tomMatch211_10.getInstList() ,tom_append_list_concInstruction( tomMatch211__end__6.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )))) ;


}
}
if ( tomMatch211__end__6.isEmptyconcInstruction() ) {
tomMatch211__end__6=tomMatch211_1;
} else {
tomMatch211__end__6= tomMatch211__end__6.getTailconcInstruction() ;
}

}
} while(!( (tomMatch211__end__6==tomMatch211_1) ));
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch211_12= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;
if ( ((tomMatch211_12 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch211_12 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch211__end__17=tomMatch211_12;
do {
{
if (!( tomMatch211__end__17.isEmptyconcInstruction() )) {
if ( ( tomMatch211__end__17.getHeadconcInstruction()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {

TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"nop-elim");
return 
 tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch211_12,tomMatch211__end__17, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ),tom_append_list_concInstruction( tomMatch211__end__17.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ))) ;


}
}
if ( tomMatch211__end__17.isEmptyconcInstruction() ) {
tomMatch211__end__17=tomMatch211_12;
} else {
tomMatch211__end__17= tomMatch211__end__17.getTailconcInstruction() ;
}

}
} while(!( (tomMatch211__end__17==tomMatch211_12) ));
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch211_22= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;
if ( ((tomMatch211_22 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch211_22 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {
if ( tomMatch211_22.isEmptyconcInstruction() ) {

TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"abstractblock-elim1");
return 
 tom.engine.adt.tominstruction.types.instruction.Nop.make() ;


}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch211_26= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;
if ( ((tomMatch211_26 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch211_26 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {
if (!( tomMatch211_26.isEmptyconcInstruction() )) {
if (  tomMatch211_26.getTailconcInstruction() .isEmptyconcInstruction() ) {

TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"abstractblock-elim2");
return 
 tomMatch211_26.getHeadconcInstruction() ;


}
}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
if ( ( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSuccesInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {
if ( ( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getFailureInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {

TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"ifnopnop-elim");
return 
 tom.engine.adt.tominstruction.types.instruction.Nop.make() ;


}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
if ( ( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getCondition()  instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {

TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"iftrue-elim");
return 
 (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSuccesInst() ;


}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
if ( ( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getCondition()  instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {

TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"iffalse-elim");
return 
 (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getFailureInst() ;


}
}
}

}


}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_NopElimAndFlatten() { 
return new NopElimAndFlatten();
}


/*
* two expressions are incompatible when they cannot be true a the same time
*/
private boolean incompatible(Expression c1, Expression c2) {
try {
Expression res = 
tom_make_InnermostId(tom_make_NormExpr(this)).visitLight(
 tom.engine.adt.tomexpression.types.expression.And.make(c2, c2) );
return res ==
 tom.engine.adt.tomexpression.types.expression.FalseTL.make() ;
} catch(VisitFailure e) {
return false;
}
}


public static class IfSwapping extends tom.library.sl.AbstractStrategyBasic {
private  OptimizerPlugin  optimizer;
public IfSwapping( OptimizerPlugin  optimizer) {
super(( new tom.library.sl.Identity() ));
this.optimizer=optimizer;
}
public  OptimizerPlugin  getoptimizer() {
return optimizer;
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
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch212_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;
if ( ((tomMatch212_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch212_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch212__end__6=tomMatch212_1;
do {
{
if (!( tomMatch212__end__6.isEmptyconcInstruction() )) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch212_13= tomMatch212__end__6.getHeadconcInstruction() ;
if ( (tomMatch212_13 instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
 tom.engine.adt.tomexpression.types.Expression  tom_cond1= tomMatch212_13.getCondition() ;
if ( ( tomMatch212_13.getFailureInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch212_7= tomMatch212__end__6.getTailconcInstruction() ;
if (!( tomMatch212_7.isEmptyconcInstruction() )) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch212_18= tomMatch212_7.getHeadconcInstruction() ;
if ( (tomMatch212_18 instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
 tom.engine.adt.tomexpression.types.Expression  tom_cond2= tomMatch212_18.getCondition() ;
if ( ( tomMatch212_18.getFailureInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {

String s1 = factory.prettyPrint(factory.remove(
tom_cond1));
String s2 = factory.prettyPrint(factory.remove(
tom_cond2));
if(s1.compareTo(s2) < 0) {
/* swap two incompatible conditions */
if(optimizer.incompatible(
tom_cond1,
tom_cond2)) {
TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"if-swapping");
return 
 tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch212_1,tomMatch212__end__6, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ), tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tomMatch212_7.getHeadconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tomMatch212__end__6.getHeadconcInstruction() ,tom_append_list_concInstruction( tomMatch212_7.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) ) )) ;
}
}


}
}
}
}
}
}
if ( tomMatch212__end__6.isEmptyconcInstruction() ) {
tomMatch212__end__6=tomMatch212_1;
} else {
tomMatch212__end__6= tomMatch212__end__6.getTailconcInstruction() ;
}

}
} while(!( (tomMatch212__end__6==tomMatch212_1) ));
}
}
}

}

}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_IfSwapping( OptimizerPlugin  t0) { 
return new IfSwapping(t0);
}
public static class BlockFusion extends tom.library.sl.AbstractStrategyBasic {
public BlockFusion() {
super(( new tom.library.sl.Identity() ));
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
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch213_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;
if ( ((tomMatch213_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch213_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch213__end__6=tomMatch213_1;
do {
{
 tom.engine.adt.tominstruction.types.InstructionList  tom_X1=tom_get_slice_concInstruction(tomMatch213_1,tomMatch213__end__6, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() );
if (!( tomMatch213__end__6.isEmptyconcInstruction() )) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch213_13= tomMatch213__end__6.getHeadconcInstruction() ;
if ( (tomMatch213_13 instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch213_10= tomMatch213_13.getVariable() ;
boolean tomMatch213_23= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch213_14= null ;
if ( (tomMatch213_10 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch213_23= true ;
tomMatch213_14= tomMatch213_10.getAstName() ;

}
} else {
if ( (tomMatch213_10 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch213_23= true ;
tomMatch213_14= tomMatch213_10.getAstName() ;

}
}
}
if (tomMatch213_23) {
 tom.engine.adt.tomname.types.TomName  tom_name1=tomMatch213_14;
 tom.engine.adt.code.types.BQTerm  tom_var1=tomMatch213_10;
 tom.engine.adt.tomexpression.types.Expression  tom_term1= tomMatch213_13.getSource() ;
 tom.engine.adt.tominstruction.types.Instruction  tom_body1= tomMatch213_13.getAstInstruction() ;
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch213_7= tomMatch213__end__6.getTailconcInstruction() ;
if (!( tomMatch213_7.isEmptyconcInstruction() )) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch213_19= tomMatch213_7.getHeadconcInstruction() ;
if ( (tomMatch213_19 instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch213_16= tomMatch213_19.getVariable() ;
boolean tomMatch213_22= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch213_20= null ;
if ( (tomMatch213_16 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch213_22= true ;
tomMatch213_20= tomMatch213_16.getAstName() ;

}
} else {
if ( (tomMatch213_16 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch213_22= true ;
tomMatch213_20= tomMatch213_16.getAstName() ;

}
}
}
if (tomMatch213_22) {
 tom.engine.adt.tominstruction.types.Instruction  tom_body2= tomMatch213_19.getAstInstruction() ;
 tom.engine.adt.tominstruction.types.InstructionList  tom_X2= tomMatch213_7.getTailconcInstruction() ;
 tom.engine.adt.tominstruction.types.Instruction  tom_block=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);

/* Fusion de 2 blocs Let contigus instanciant deux variables egales */
if(
compare(tom_term1, tomMatch213_19.getSource() )) {
if(
compare(tom_var1,tomMatch213_16)) {
TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"block-fusion1");
return 
(tom_block.setInstList(tom_append_list_concInstruction(tom_X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Let.make(tom_var1, tom_term1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_body1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_body2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction(tom_X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));
} else {
InfoVariable info = new InfoVariable();

tom_make_computeOccurencesLet(tom_name1,info).visit(
tom_body2);
int mult = info.readCount;
if(mult==0) {
TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"block-fusion2");
Instruction newBody2 =  
tom_make_renameVariable(tomMatch213_20,tom_name1).visitLight(
tom_body2);
return 
(tom_block.setInstList(tom_append_list_concInstruction(tom_X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Let.make(tom_var1, tom_term1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_body1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(newBody2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction(tom_X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));
}
}
}


}

}
}
}

}
}
if ( tomMatch213__end__6.isEmptyconcInstruction() ) {
tomMatch213__end__6=tomMatch213_1;
} else {
tomMatch213__end__6= tomMatch213__end__6.getTailconcInstruction() ;
}

}
} while(!( (tomMatch213__end__6==tomMatch213_1) ));
}
}
}

}

}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_BlockFusion() { 
return new BlockFusion();
}
public static class IfFusion extends tom.library.sl.AbstractStrategyBasic {
public IfFusion() {
super(( new tom.library.sl.Identity() ));
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
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
boolean tomMatch214_18= false ;
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch214_1= null ;
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) {
{
tomMatch214_18= true ;
tomMatch214_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;

}
} else {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
{
tomMatch214_18= true ;
tomMatch214_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;

}
}
}
if (tomMatch214_18) {
if ( ((tomMatch214_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch214_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch214__end__6=tomMatch214_1;
do {
{
 tom.engine.adt.tominstruction.types.InstructionList  tom_X1=tom_get_slice_concInstruction(tomMatch214_1,tomMatch214__end__6, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() );
if (!( tomMatch214__end__6.isEmptyconcInstruction() )) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch214_13= tomMatch214__end__6.getHeadconcInstruction() ;
if ( (tomMatch214_13 instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
 tom.engine.adt.tomexpression.types.Expression  tom_cond1= tomMatch214_13.getCondition() ;
 tom.engine.adt.tominstruction.types.Instruction  tom_success1= tomMatch214_13.getSuccesInst() ;
 tom.engine.adt.tominstruction.types.Instruction  tom_failure1= tomMatch214_13.getFailureInst() ;
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch214_7= tomMatch214__end__6.getTailconcInstruction() ;
if (!( tomMatch214_7.isEmptyconcInstruction() )) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch214_17= tomMatch214_7.getHeadconcInstruction() ;
if ( (tomMatch214_17 instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
 tom.engine.adt.tominstruction.types.Instruction  tom_success2= tomMatch214_17.getSuccesInst() ;
 tom.engine.adt.tominstruction.types.Instruction  tom_failure2= tomMatch214_17.getFailureInst() ;
 tom.engine.adt.tominstruction.types.InstructionList  tom_X2= tomMatch214_7.getTailconcInstruction() ;
 tom.engine.adt.tominstruction.types.Instruction  tom_block=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);

Expression c1 = factory.remove(
tom_cond1);
Expression c2 = factory.remove(
 tomMatch214_17.getCondition() );
//System.out.println("c1 = " + c1);
//System.out.println("c2 = " + c2);

{
{
if ( (c1 instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( (c2 instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )c1)==(( tom.engine.adt.tomexpression.types.Expression )c2)) ) {

/* Merge 2 blocks whose conditions are equals */
if(
tom_failure1.isNop() && 
tom_failure2.isNop()) {
TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"if-fusion1");
Instruction res = 
(tom_block.setInstList(tom_append_list_concInstruction(tom_X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom_cond1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_success1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_success2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ,tom_append_list_concInstruction(tom_X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));
//System.out.println(res);

return res;
} else {
TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"if-fusion2");
return 
(tom_block.setInstList(tom_append_list_concInstruction(tom_X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom_cond1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_success1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_success2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_failure1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_failure2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction(tom_X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));
}


}
}
}

}
{
if ( (c1 instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )c1) instanceof tom.engine.adt.tomexpression.types.expression.Negation) ) {
if ( (c2 instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ( (( tom.engine.adt.tomexpression.types.Expression )c1).getArg() ==(( tom.engine.adt.tomexpression.types.Expression )c2)) ) {

/* Merge 2 blocks whose conditions are the negation of the other */
TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"if-fusion-not");
return 
(tom_block.setInstList(tom_append_list_concInstruction(tom_X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom_cond1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_success1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_failure2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_failure1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom_success2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction(tom_X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));


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
if ( tomMatch214__end__6.isEmptyconcInstruction() ) {
tomMatch214__end__6=tomMatch214_1;
} else {
tomMatch214__end__6= tomMatch214__end__6.getTailconcInstruction() ;
}

}
} while(!( (tomMatch214__end__6==tomMatch214_1) ));
}
}

}

}

}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_IfFusion() { 
return new IfFusion();
}
public static class InterBlock extends tom.library.sl.AbstractStrategyBasic {
private  OptimizerPlugin  optimizer;
public InterBlock( OptimizerPlugin  optimizer) {
super(( new tom.library.sl.Identity() ));
this.optimizer=optimizer;
}
public  OptimizerPlugin  getoptimizer() {
return optimizer;
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
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch216_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;
if ( ((tomMatch216_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch216_1 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch216__end__6=tomMatch216_1;
do {
{
if (!( tomMatch216__end__6.isEmptyconcInstruction() )) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch216_13= tomMatch216__end__6.getHeadconcInstruction() ;
if ( (tomMatch216_13 instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
 tom.engine.adt.tomexpression.types.Expression  tom_cond1= tomMatch216_13.getCondition() ;
 tom.engine.adt.tominstruction.types.InstructionList  tomMatch216_7= tomMatch216__end__6.getTailconcInstruction() ;
if (!( tomMatch216_7.isEmptyconcInstruction() )) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch216_17= tomMatch216_7.getHeadconcInstruction() ;
if ( (tomMatch216_17 instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
 tom.engine.adt.tomexpression.types.Expression  tom_cond2= tomMatch216_17.getCondition() ;
if ( ( tomMatch216_17.getFailureInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {

if(optimizer.incompatible(
tom_cond1,
tom_cond2)) {
TomMessage.info(logger,null,0,TomMessage.tomOptimizationType,"inter-block");
return 
 tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch216_1,tomMatch216__end__6, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ), tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom_cond1,  tomMatch216_13.getSuccesInst() ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tomMatch216_13.getFailureInst() , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom_cond2,  tomMatch216_17.getSuccesInst() ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction( tomMatch216_7.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )) ;
}


}
}
}
}
}
if ( tomMatch216__end__6.isEmptyconcInstruction() ) {
tomMatch216__end__6=tomMatch216_1;
} else {
tomMatch216__end__6= tomMatch216__end__6.getTailconcInstruction() ;
}

}
} while(!( (tomMatch216__end__6==tomMatch216_1) ));
}
}
}

}

}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_InterBlock( OptimizerPlugin  t0) { 
return new InterBlock(t0);
}
public static class NormExpr extends tom.library.sl.AbstractStrategyBasic {
private  OptimizerPlugin  optimizer;
public NormExpr( OptimizerPlugin  optimizer) {
super(( new tom.library.sl.Identity() ));
this.optimizer=optimizer;
}
public  OptimizerPlugin  getoptimizer() {
return optimizer;
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
public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {
if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2()  instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
return  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ;
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {
if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1()  instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
return  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ;
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {
if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2()  instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {
return  (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ;
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {
if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1()  instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {
return  (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ;
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {
if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1()  instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
return  (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ;
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {
if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2()  instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
return  (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ;
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {
if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1()  instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {
return  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ;
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {
if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1()  instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
return  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ;
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {

//System.out.println("kid1 = " + `kid1);
//System.out.println("kid2 = " + `kid2);
if(
compare( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getKid1() , (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getKid2() )) {
return 
 tom.engine.adt.tomexpression.types.expression.TrueTL.make() ;
} else {
return 
(( tom.engine.adt.tomexpression.types.Expression )tom__arg);
}


}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch217_46= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch217_47= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ;
if ( (tomMatch217_46 instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) {
 tom.engine.adt.tomname.types.TomName  tom_name1= tomMatch217_46.getAstName() ;
 tom.engine.adt.code.types.BQTerm  tom_term= tomMatch217_46.getVariable() ;
if ( (tomMatch217_47 instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) {
if ( (tom_term== tomMatch217_47.getVariable() ) ) {

if(
tom_name1==
 tomMatch217_47.getAstName() ) {
return 
 tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom_name1, tom_term) ;
}
/*
* may be true for list operator with domain=codomain
* two if_sym(f)==is_fsym(g) may be true due to mapping
*/
TomSymbol tomSymbol = optimizer.getSymbolTable().getSymbolFromName(
tom_name1.getString());
if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
if(domain!=codomain) {
return 
 tom.engine.adt.tomexpression.types.expression.FalseTL.make() ;
}
} else {
return 
 tom.engine.adt.tomexpression.types.expression.FalseTL.make() ;
}
return 
(( tom.engine.adt.tomexpression.types.Expression )tom__arg);


}
}
}
}
}

}


}
return _visit_Expression(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {
return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_NormExpr( OptimizerPlugin  t0) { 
return new NormExpr(t0);
}


}