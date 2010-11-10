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
* Claudia Tavares  e-mail: Claudia.Tavares@loria.fr
* Jean-Christophe Bach e-mail: Jeanchristophe.Bach@loria.fr
*
**/



package tom.engine.typer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;

import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.typeconstraints.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class NewKernelTyper {


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
private static  tom.library.sl.Strategy  tom_make_TopDownStopOnSuccess( tom.library.sl.Strategy  v) { 
return (
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Choice(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Choice(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) )) )) 

;
}
private static  tom.library.sl.Strategy  tom_make_TopDownIdStopOnSuccess( tom.library.sl.Strategy  v) { 
return (
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?v:new tom.library.sl.ChoiceId(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) )) )) 

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

  private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_append_list_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList l1,  tom.engine.adt.typeconstraints.types.TypeConstraintList  l2) {
    if( l1.isEmptyconcTypeConstraint() ) {
      return l2;
    } else if( l2.isEmptyconcTypeConstraint() ) {
      return l1;
    } else if(  l1.getTailconcTypeConstraint() .isEmptyconcTypeConstraint() ) {
      return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,l2) ;
    } else {
      return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,tom_append_list_concTypeConstraint( l1.getTailconcTypeConstraint() ,l2)) ;
    }
  }
  private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_get_slice_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList  begin,  tom.engine.adt.typeconstraints.types.TypeConstraintList  end, tom.engine.adt.typeconstraints.types.TypeConstraintList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTypeConstraint()  ||  (end== tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( begin.getHeadconcTypeConstraint() ,( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_get_slice_concTypeConstraint( begin.getTailconcTypeConstraint() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {
    if( l1.isEmptyconcTomTerm() ) {
      return l2;
    } else if( l2.isEmptyconcTomTerm() ) {
      return l1;
    } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {
      return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;
    } else {
      return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {
    if( l1.isEmptyconcTomType() ) {
      return l2;
    } else if( l2.isEmptyconcTomType() ) {
      return l1;
    } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {
      return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;
    } else {
      return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_append_list_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList l1,  tom.engine.adt.tomtype.types.TypeOptionList  l2) {
    if( l1.isEmptyconcTypeOption() ) {
      return l2;
    } else if( l2.isEmptyconcTypeOption() ) {
      return l1;
    } else if(  l1.getTailconcTypeOption() .isEmptyconcTypeOption() ) {
      return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,l2) ;
    } else {
      return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,tom_append_list_concTypeOption( l1.getTailconcTypeOption() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_get_slice_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList  begin,  tom.engine.adt.tomtype.types.TypeOptionList  end, tom.engine.adt.tomtype.types.TypeOptionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTypeOption()  ||  (end== tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( begin.getHeadconcTypeOption() ,( tom.engine.adt.tomtype.types.TypeOptionList )tom_get_slice_concTypeOption( begin.getTailconcTypeOption() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.code.types.CodeList  tom_append_list_concCode( tom.engine.adt.code.types.CodeList l1,  tom.engine.adt.code.types.CodeList  l2) {
    if( l1.isEmptyconcCode() ) {
      return l2;
    } else if( l2.isEmptyconcCode() ) {
      return l1;
    } else if(  l1.getTailconcCode() .isEmptyconcCode() ) {
      return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,l2) ;
    } else {
      return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,tom_append_list_concCode( l1.getTailconcCode() ,l2)) ;
    }
  }
  private static   tom.engine.adt.code.types.CodeList  tom_get_slice_concCode( tom.engine.adt.code.types.CodeList  begin,  tom.engine.adt.code.types.CodeList  end, tom.engine.adt.code.types.CodeList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcCode()  ||  (end== tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.code.types.codelist.ConsconcCode.make( begin.getHeadconcCode() ,( tom.engine.adt.code.types.CodeList )tom_get_slice_concCode( begin.getTailconcCode() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {
    if( l1.isEmptyconcBQTerm() ) {
      return l2;
    } else if( l2.isEmptyconcBQTerm() ) {
      return l1;
    } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {
      return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;
    } else {
      return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;
    }
  }
  private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {
    if( l1.isEmptyComposite() ) {
      return l2;
    } else if( l2.isEmptyComposite() ) {
      return l1;
    } else if(  l1.getTailComposite() .isEmptyComposite() ) {
      return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;
    } else {
      return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;
    }
  }
  private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {
    if( l1.isEmptyconcConstraintInstruction() ) {
      return l2;
    } else if( l2.isEmptyconcConstraintInstruction() ) {
      return l1;
    } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {
      return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;
    } else {
      return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;
  }
  
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
  
  private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {
    if( l1.isEmptyconcOption() ) {
      return l2;
    } else if( l2.isEmptyconcOption() ) {
      return l1;
    } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {
      return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;
    } else {
      return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;
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
  

private static Logger logger = Logger.getLogger("tom.engine.typer.NewKernelTyper");




private int freshTypeVarCounter;
private int limTVarSymbolTable;

/*
* pem: why use a state variable here ?
*/
// List for variables of pattern (match constraints)
private TomList varPatternList;
// List for variables of subject and of numeric constraints
private BQTermList varList;

/*
* pem: why use a state variable here ?
*/
// List for type constraints (for fresh type variables)
private TypeConstraintList equationConstraints;
private TypeConstraintList subtypingConstraints;
// Set of pairs (freshVar,groundVar)
private HashMap<TomType,TomType> substitutions;
// Set of supertypes for each type
private HashMap<TomType,TomTypeList> dependencies = new
HashMap<TomType,TomTypeList>();

private SymbolTable symbolTable;

private String currentInputFileName;
private boolean lazyType = false;

protected void setLazyType() {
lazyType = true;
}

protected void setSymbolTable(SymbolTable symbolTable) {
this.symbolTable = symbolTable;
}

protected SymbolTable getSymbolTable() {
return symbolTable;
}

protected void setCurrentInputFileName(String currentInputFileName) {
this.currentInputFileName = currentInputFileName;
}

protected String getCurrentInputFileName() {
return currentInputFileName;
}
protected TomType getCodomain(TomSymbol tSymbol) {
return TomBase.getSymbolCodomain(tSymbol);
}

protected TomSymbol getSymbolFromTerm(TomTerm tTerm) {
return TomBase.getSymbolFromTerm(tTerm, symbolTable);
}

protected TomSymbol getSymbolFromTerm(BQTerm bqTerm) {
return TomBase.getSymbolFromTerm(bqTerm,symbolTable);
}

protected TomSymbol getSymbolFromName(String tName) {
return TomBase.getSymbolFromName(tName, symbolTable);
}

protected TomSymbol getSymbolFromType(TomType tType) {
return TomBase.getSymbolFromType(tType,symbolTable); 
}

/**
* The method <code>addSubstitution</code> adds a substitutions (i.e. a pair
* (type1,type2) where type2 is the substitution for type1) into the
* global list "substitutions" and saturate it.
* For example, to add a pair (X,Y) where X is a type variable and Y is a type
* which can be a type variable or a ground type, we follow two steps:
* <p>
* STEP 1:  a) put(X,Z) if (Y,Z) is in substitutions or
*          b) put(X,Y) otherwise
* <p>
* STEP 2:  a) put(W,Z) after step 1.a or put(W,Y) after step 1.b
*             if there exist (W,X) in substitutions for each (W,X) in substitutions
*          b) do nothing otherwise
* @param key   the first argument of the pair to be inserted (i.e. the type1) 
* @param value the second argument of the pair to be inserted (i.e. the type2) 
*/
private void addSubstitution(TomType key, TomType value) {
// STEP 1  
TomType newValue = value;
if (substitutions.containsKey(value)) {
newValue = substitutions.get(value); 
} 
substitutions.put(key,newValue);

// STEP 2
if (substitutions.containsValue(key)) {
TomType valueOfCurrentKey;
for (TomType currentKey : substitutions.keySet()) {
valueOfCurrentKey = substitutions.get(currentKey);
if (valueOfCurrentKey == key) {
substitutions.put(currentKey,newValue);
}
}
}
}

/**
* The method <code>hasUndeclaredType</code> checks if a term has an
* undeclared type by verifying if a term has type
* Type(name,EmptyTargetLanguage()), where name is not
* UNKNOWN_TYPE.
*/
protected void hasUndeclaredType(String typeName, OptionList oList) {
String fileName = currentInputFileName;
int line = 0;
//DEBUG System.out.println("hasUndeclaredType: subject = " + subject);
if (typeName != symbolTable.TYPE_UNKNOWN.getTomType()) {
Option option = TomBase.findOriginTracking(
oList);

{
{
if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

TomMessage.error(logger,
 (( tom.engine.adt.tomoption.types.Option )option).getFileName() , 
 (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
TomMessage.unknownSymbol,
typeName); 


}
}

}

}

}
}

/*
protected void hasUndeclaredType(BQTerm subject) {
String fileName = currentInputFileName;
int line = 0;
//DEBUG System.out.println("hasUndeclaredType: subject = " + subject);
%match {
(BQVariable|BQVariableStar)[Options=oList,AstType=aType] << subject && 
TypeVar(tomType,_) << aType &&
(tomType != symbolTable.TYPE_UNKNOWN.getTomType()) -> {
Option option = TomBase.findOriginTracking(`oList);
%match(option) {
OriginTracking(_,line,fileName) -> {
TomMessage.error(logger,`fileName, `line,
TomMessage.unknownSymbol,`tomType); 
}
}
}
}
}
*/

protected TomType getType(BQTerm bqTerm) {

{
{
if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch2_3= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch2_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch2_3= true ;
tomMatch2_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch2_3= true ;
tomMatch2_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) {
{
tomMatch2_3= true ;
tomMatch2_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;

}
}
}
}
if (tomMatch2_3) {
return 
tomMatch2_1; 

}

}

}
{
if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch2_9= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch2_5= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
{
tomMatch2_9= true ;
tomMatch2_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {
{
tomMatch2_9= true ;
tomMatch2_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {
{
tomMatch2_9= true ;
tomMatch2_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {
{
tomMatch2_9= true ;
tomMatch2_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {
{
tomMatch2_9= true ;
tomMatch2_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {
{
tomMatch2_9= true ;
tomMatch2_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {
{
tomMatch2_9= true ;
tomMatch2_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {
{
tomMatch2_9= true ;
tomMatch2_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {
{
tomMatch2_9= true ;
tomMatch2_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

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
if (tomMatch2_9) {
if ( (tomMatch2_5 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

TomSymbol tSymbol = getSymbolFromName(
 tomMatch2_5.getString() );
//DEBUG System.out.println("In getType with BQAppl " + `bqTerm + "\n");
//DEBUG System.out.println("In getType with type " + getCodomain(tSymbol) + "\n");
return getCodomain(tSymbol);


}
}

}

}


}

throw new TomRuntimeException("getType(BQTerm): should not be here.");
}

protected TomType getType(TomTerm tTerm) {

{
{
if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
return getType(
 (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getTomTerm() ); 

}
}

}
{
if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch3_6= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch3_4= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch3_6= true ;
tomMatch3_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch3_6= true ;
tomMatch3_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstType() ;

}
}
}
if (tomMatch3_6) {
return 
tomMatch3_4; 

}

}

}
{
if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch3_8= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getNameList() ;
if ( ((tomMatch3_8 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch3_8 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
if (!( tomMatch3_8.isEmptyconcTomName() )) {
 tom.engine.adt.tomname.types.TomName  tomMatch3_14= tomMatch3_8.getHeadconcTomName() ;
if ( (tomMatch3_14 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

TomSymbol tSymbol = getSymbolFromName(
 tomMatch3_14.getString() );
return getCodomain(tSymbol);


}
}
}
}
}

}


}

throw new TomRuntimeException("getType(TomTerm): should not be here.");
}

protected Info getInfoFromTomTerm(TomTerm tTerm) {

{
{
if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
return getInfoFromTomTerm(
 (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getTomTerm() ); 

}
}

}
{
if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch4_7= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch4_5= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch4_4= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch4_7= true ;
tomMatch4_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getOptions() ;
tomMatch4_5= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch4_7= true ;
tomMatch4_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getOptions() ;
tomMatch4_5= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;

}
}
}
if (tomMatch4_7) {

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch4_5, tomMatch4_4) ; 


}

}

}
{
if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch4_10= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getNameList() ;
if ( ((tomMatch4_10 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch4_10 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
if (!( tomMatch4_10.isEmptyconcTomName() )) {

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch4_10.getHeadconcTomName() ,  (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getOptions() ) ; 


}
}
}
}

}


}

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tom.engine.adt.tomname.types.tomname.Name.make("") ,  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ; 
}

protected Info getInfoFromBQTerm(BQTerm bqTerm) {

{
{
if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch5_4= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch5_2= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch5_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch5_4= true ;
tomMatch5_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getOptions() ;
tomMatch5_2= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch5_4= true ;
tomMatch5_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getOptions() ;
tomMatch5_2= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
{
tomMatch5_4= true ;
tomMatch5_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getOptions() ;
tomMatch5_2= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
}
}
}
if (tomMatch5_4) {

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch5_2, tomMatch5_1) ; 


}

}

}

}

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tom.engine.adt.tomname.types.tomname.Name.make("") ,  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ; 
}

protected void setLimTVarSymbolTable(int freshTVarSymbolTable) {
limTVarSymbolTable = freshTVarSymbolTable;
}

protected int getFreshTlTIndex() {
return freshTypeVarCounter++;
}

protected TomType getUnknownFreshTypeVar() {
TomType tType = symbolTable.TYPE_UNKNOWN;

{
{
if ( (tType instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
return 
 tom.engine.adt.tomtype.types.tomtype.TypeVar.make( (( tom.engine.adt.tomtype.types.TomType )tType).getTomType() , getFreshTlTIndex()) ; 

}
}

}

}

throw new TomRuntimeException("getUnknownFreshTypeVar: should not be here.");
}

protected boolean containsConstraint(TypeConstraint tConstraint, TypeConstraintList
tCList) {

{
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch7__end__8=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
if (!( tomMatch7__end__8.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch7_13= tomMatch7__end__8.getHeadconcTypeConstraint() ;
boolean tomMatch7_16= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch7_12= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch7_11= null ;
if ( (tomMatch7_13 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
{
tomMatch7_16= true ;
tomMatch7_11= tomMatch7_13.getType1() ;
tomMatch7_12= tomMatch7_13.getType2() ;

}
} else {
if ( (tomMatch7_13 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
{
tomMatch7_16= true ;
tomMatch7_11= tomMatch7_13.getType1() ;
tomMatch7_12= tomMatch7_13.getType2() ;

}
}
}
if (tomMatch7_16) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ==tomMatch7_11) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ==tomMatch7_12) ) {
return true; 
}
}
}

}
if ( tomMatch7__end__8.isEmptyconcTypeConstraint() ) {
tomMatch7__end__8=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch7__end__8= tomMatch7__end__8.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch7__end__8==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}
}
}

}
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch7__end__25=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
if (!( tomMatch7__end__25.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch7_30= tomMatch7__end__25.getHeadconcTypeConstraint() ;
if ( (tomMatch7_30 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() == tomMatch7_30.getType1() ) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() == tomMatch7_30.getType2() ) ) {
return true; 
}
}
}
}
if ( tomMatch7__end__25.isEmptyconcTypeConstraint() ) {
tomMatch7__end__25=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch7__end__25= tomMatch7__end__25.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch7__end__25==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}
}
}

}
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch7__end__41=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
if (!( tomMatch7__end__41.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch7_46= tomMatch7__end__41.getHeadconcTypeConstraint() ;
if ( (tomMatch7_46 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() == tomMatch7_46.getType1() ) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() == tomMatch7_46.getType2() ) ) {
return true; 
}
}
}
}
if ( tomMatch7__end__41.isEmptyconcTypeConstraint() ) {
tomMatch7__end__41=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch7__end__41= tomMatch7__end__41.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch7__end__41==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}
}
}

}


}

return false;
} 

/*
* pem: use if(...==... && typeConstraints.contains(...))
*/
/**
* The method <code>addEqConstraint</code> adds an equation (i.e. a type constraint) into the
* global list "TypeConstraints" if this equation does not contains
* "EmptyTypes". The global list is ordered inserting equations
* containing (one or both) ground type(s) into the beginning of the list.
* @param tConstraint the equation to be inserted into the list "TypeConstraints"
*/
protected TypeConstraintList addEqConstraint(TypeConstraint tConstraint,
TypeConstraintList tCList) {
if (!containsConstraint(tConstraint,tCList)) {

{
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch8_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch8_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch8_1;
 tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch8_2;
boolean tomMatch8_9= false ;
if ( (tomMatch8_2 instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
if ( (tom_t2==tomMatch8_2) ) {
tomMatch8_9= true ;
}
}
if (!(tomMatch8_9)) {
boolean tomMatch8_8= false ;
if ( (tomMatch8_1 instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
if ( (tom_t1==tomMatch8_1) ) {
tomMatch8_8= true ;
}
}
if (!(tomMatch8_8)) {
if (!( (tom_t2==tom_t1) )) {

return 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;


}
}

}

}
}

}

}

}
return tCList;
}

protected TypeConstraintList addSubConstraint(TypeConstraint tConstraint,
TypeConstraintList tCList) {
if (!containsConstraint(tConstraint,tCList)) {

{
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch9_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch9_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch9_1;
 tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch9_2;
boolean tomMatch9_9= false ;
if ( (tomMatch9_2 instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
if ( (tom_t2==tomMatch9_2) ) {
tomMatch9_9= true ;
}
}
if (!(tomMatch9_9)) {
boolean tomMatch9_8= false ;
if ( (tomMatch9_1 instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
if ( (tom_t1==tomMatch9_1) ) {
tomMatch9_8= true ;
}
}
if (!(tomMatch9_8)) {
if (!( (tom_t2==tom_t1) )) {

if (
tom_t1== 
tom_t2) {
System.out.println("In addSubConstraint with t1 = " + 
tom_t1+ " and t2 = " + 
tom_t2);
}
return
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;


}
}

}

}
}

}

}

}
return tCList;
}

protected void generateDependencies() {
TomTypeList superTypes;
TomTypeList supOfSubTypes;
for(TomType currentType:symbolTable.getUsedTypes()) {
superTypes = 
 tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
//DEBUG System.out.println("In generateDependencies -- for 1 : currentType = " +
//DEBUG    currentType);

{
{
if ( (currentType instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )currentType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch10_1= (( tom.engine.adt.tomtype.types.TomType )currentType).getTypeOptions() ;
if ( ((tomMatch10_1 instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || (tomMatch10_1 instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch10__end__6=tomMatch10_1;
do {
{
if (!( tomMatch10__end__6.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch10_10= tomMatch10__end__6.getHeadconcTypeOption() ;
if ( (tomMatch10_10 instanceof tom.engine.adt.tomtype.types.typeoption.SubtypeDecl) ) {

TomType supType = symbolTable.getType(
 tomMatch10_10.getTomType() );
//DEBUG System.out.println("In generateDependencies -- match : supTypeName = "
//DEBUG     + `supTypeName + " and supType = " +
//DEBUG    supType);
if (dependencies.containsKey(supType)) {
//DEBUG System.out.println("In generateDependencies -- if : supType = " +
//DEBUG     supType);
superTypes = dependencies.get(supType); 
}
superTypes = 
 tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(supType,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )) ;  

for(TomType subType:dependencies.keySet()) {
supOfSubTypes = dependencies.get(
subType);
//DEBUG System.out.println("In generateDependencies -- for 2: supOfSubTypes = " +
//DEBUG     supOfSubTypes);

{
{
if ( (supOfSubTypes instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch11__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes);
do {
{
if (!( tomMatch11__end__4.isEmptyconcTomType() )) {
if ( (currentType== tomMatch11__end__4.getHeadconcTomType() ) ) {

// Replace list of superTypes of "subType" by a new one
// containing the superTypes of "currentType" which is also a
// superType
dependencies.put(subType,
tom_append_list_concTomType(supOfSubTypes,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )));


}
}
if ( tomMatch11__end__4.isEmptyconcTomType() ) {
tomMatch11__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes);
} else {
tomMatch11__end__4= tomMatch11__end__4.getTailconcTomType() ;
}

}
} while(!( (tomMatch11__end__4==(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes)) ));
}
}

}

}

}


}
}
if ( tomMatch10__end__6.isEmptyconcTypeOption() ) {
tomMatch10__end__6=tomMatch10_1;
} else {
tomMatch10__end__6= tomMatch10__end__6.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch10__end__6==tomMatch10_1) ));
}
}
}

}

}

//DEBUG System.out.println("In generateDependencies -- end: superTypes = " +
//DEBUG     superTypes);
dependencies.put(
currentType,superTypes);
}
}

protected void addTomTerm(TomTerm tTerm) {
varPatternList = 
 tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tTerm,tom_append_list_concTomTerm(varPatternList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
}

protected void addBQTerm(BQTerm bqTerm) {
varList = 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(bqTerm,tom_append_list_concBQTerm(varList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
}

/**
* The method <code>resetVarPatternList</code> empties varPatternList after
* checking if <code>varList</code> contains
* a corresponding BQTerm in order to remove it from <code>varList</code. too
*/
protected void resetVarPatternList() {
for(TomTerm tTerm: varPatternList.getCollectionconcTomTerm()) {

{
{
if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch12_14= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch12_2= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch12_14= true ;
tomMatch12_2= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch12_14= true ;
tomMatch12_2= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;

}
}
}
if (tomMatch12_14) {
if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch12__end__7=(( tom.engine.adt.code.types.BQTermList )varList);
do {
{
if (!( tomMatch12__end__7.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch12_11= tomMatch12__end__7.getHeadconcBQTerm() ;
boolean tomMatch12_13= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch12_10= null ;
if ( (tomMatch12_11 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch12_13= true ;
tomMatch12_10= tomMatch12_11.getAstName() ;

}
} else {
if ( (tomMatch12_11 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch12_13= true ;
tomMatch12_10= tomMatch12_11.getAstName() ;

}
}
}
if (tomMatch12_13) {
if ( (tomMatch12_2==tomMatch12_10) ) {

varList = 
tom_append_list_concBQTerm(tom_get_slice_concBQTerm((( tom.engine.adt.code.types.BQTermList )varList),tomMatch12__end__7, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ),tom_append_list_concBQTerm( tomMatch12__end__7.getTailconcBQTerm() , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ));


}
}

}
if ( tomMatch12__end__7.isEmptyconcBQTerm() ) {
tomMatch12__end__7=(( tom.engine.adt.code.types.BQTermList )varList);
} else {
tomMatch12__end__7= tomMatch12__end__7.getTailconcBQTerm() ;
}

}
} while(!( (tomMatch12__end__7==(( tom.engine.adt.code.types.BQTermList )varList)) ));
}
}
}

}

}

}

}
varPatternList = 
 tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
}

/**
* The method <code>init</code> empties all global lists and hashMaps which means to
* empty <code>varPatternList</code>, <code>varList</code>,
* <code>equationConstraints</code>, <code>subtypingConstraints</code> and <code>substitutions</code>
*/
private void init() {
freshTypeVarCounter = limTVarSymbolTable;
varPatternList = 
 tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
varList = 
 tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
equationConstraints = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
subtypingConstraints = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
substitutions = new HashMap<TomType,TomType>();
}

private Code collectKnownTypesFromCode(Code subject) {
try {
return 
tom_make_TopDownIdStopOnSuccess(tom_make_CollectKnownTypes(this)).visitLight(subject);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
}
}

// TODO: keep only one CollectKnownTypes method (instead of that from NewTyper
// and this one)
/**
* The class <code>CollectKnownTypes</code> is generated from a strategy which
* initially types all terms by using their correspondent type in symbol table
* or a fresh type variable :
* CASE 1 : Type(name, EmptyTargetLanguageType()) -> Type(name, foundType) if
* name is in TypeTable
* CASE 2 : Type(name, EmptyTargetLanguageType()) -> TypeVar(name, Index(i))
* if name is not in TypeTable
* @param nkt an instance of object NewKernelTyper
*/

public static class CollectKnownTypes extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public CollectKnownTypes( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
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
if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {
return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tom_tomType= (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() ;
if ( ( (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTlType()  instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {

TomType newType = nkt.symbolTable.getType(
tom_tomType);
if (newType == null) {
// This happens when :
// * tomType != unknown type AND (newType == null)
// * tomType == unknown type
newType = 
 tom.engine.adt.tomtype.types.tomtype.TypeVar.make(tom_tomType, nkt.getFreshTlTIndex()) ;
}
return newType;


}
}
}

}

}
return _visit_TomType(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_CollectKnownTypes( NewKernelTyper  t0) { 
return new CollectKnownTypes(t0);
}


/**
* The method <code>inferAllTypes</code> is the start-up of the inference
* process. It is a generic method and it is called for the first time by the
* NewTyper
*/
public <T extends tom.library.sl.Visitable> T inferAllTypes(T term, TomType contextType) {
try {
return

tom_make_TopDownStopOnSuccess(tom_make_inferTypes(contextType,this)).visitLight(term); 
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("inferAllTypes: failure on " + term);
}
}

/**
* The class <code>inferTypes</code> is generated from a strategy which
* tries to infer types of all variables
* <p> 
* It starts by searching for a Instruction
* <code>Match(constraintInstructionList,option)</code> and calling
* <code>inferConstraintInstructionList</code> in order to apply rule CT-RULE
* for each single constraintInstruction
* <p>
* It searches for variables and star variables (TomTerms and BQTerms) and
* applies rule CT-ANTI, CT-VAR, CT-SVAR, CT-FUN,
* CT-EMPTY, CT-ELEM, CT-MERGE or CT-STAR to a "pattern" (a TomTerm) or a
* "subject" (a BQTerm) in order to infer its type.
* <p>
* CT-ANTI rule:
* IF found "!(e):A"
* THEN infers type of e
* <p>
* CT-VAR rule (resp. CT-SVAR): 
* IF found "x:A" (resp. "x*:A") and "x:T" (resp. "x*:T") already exists in SymbolTable 
* THEN add a type constraint "A = T"
* <p>
* CT-FUN rule:
* IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
* THEN infers type of arguments and add a type constraint "A = T" and
*      calls the TypeVariableList method which adds a type constraint "Ai =
*      Ti" for each argument, where Ai is a fresh type variable
* <p>
* CT-EMPTY rule:
* IF found "l():AA" and "l:T*->TT" exists in SymbolTable
* THEN add a type constraint "AA = TT"       
* <p>
* CT-ELEM rule:
* IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "AA = TT" and calls the
*      TypeVariableList method which adds a type constraint "A =T"
*      for the last argument, where A is a fresh type variable and
*      "e" does not represent a list with head symbol "l"
* <p>
* CT-MERGE rule:
* IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "AA = TT" and calls the
*      TypeVariableList method, where "e" represents a list with
*      head symbol "l"
* <p>
* CT-STAR rule:
* Equals to CT-MERGE but with a star variable "x*" instead of "e"
* This rule is necessary because it differed from CT-MERGE in the
* sense of the type of the last argument ("x*" here) is unknown 
* @param contextType the fresh generated previously and attributed to the term
* @param nkt an instance of object NewKernelTyper
*/

public static class inferTypes extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomtype.types.TomType  contextType;
private  NewKernelTyper  nkt;
public inferTypes( tom.engine.adt.tomtype.types.TomType  contextType,  NewKernelTyper  nkt) {
super(( new tom.library.sl.Fail() ));
this.contextType=contextType;
this.nkt=nkt;
}
public  tom.engine.adt.tomtype.types.TomType  getcontextType() {
return contextType;
}
public  NewKernelTyper  getnkt() {
return nkt;
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
if ( (v instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {
return ((T)visit_TomVisit((( tom.engine.adt.tomsignature.types.TomVisit )v),introspector));
}
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {
return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));
}
if ( (v instanceof tom.engine.adt.code.types.Code) ) {
return ((T)visit_Code((( tom.engine.adt.code.types.Code )v),introspector));
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
public  tom.engine.adt.code.types.Code  _visit_Code( tom.engine.adt.code.types.Code  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.code.types.Code )any.visit(environment,introspector));
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
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomsignature.types.TomVisit  _visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomsignature.types.TomVisit )any.visit(environment,introspector));
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
boolean tomMatch14_5= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch14_2= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch14_3= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch14_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch14_5= true ;
tomMatch14_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ;
tomMatch14_2= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;
tomMatch14_3= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch14_5= true ;
tomMatch14_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ;
tomMatch14_2= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;
tomMatch14_3= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() ;

}
}
}
if (tomMatch14_5) {
 tom.engine.adt.code.types.BQTerm  tom_bqVar=(( tom.engine.adt.code.types.BQTerm )tom__arg);

//DEBUG System.out.println("InferTypes:BQTerm bqVar -- contextType = " +
//DEBUG     contextType);
nkt.checkNonLinearityOfBQVariables(
tom_bqVar);
TypeConstraintList newSubConstraints = nkt.subtypingConstraints;
nkt.subtypingConstraints = nkt.addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tomMatch14_3, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch14_2, tomMatch14_1) ) ,newSubConstraints);  
//DEBUG System.out.println("InferTypes:BQTerm bqVar -- constraint = " +
//DEBUG `aType + " = " + contextType);
return 
tom_bqVar;


}

}

}
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch14_8= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;
 tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ;
if ( (tomMatch14_8 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_name= tomMatch14_8.getString() ;
 tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch14_8;
 tom.engine.adt.code.types.BQTermList  tom_bqTList= (( tom.engine.adt.code.types.BQTerm )tom__arg).getArgs() ;

//DEBUG System.out.println("\n Test pour BQTerm-inferTypes in BQAppl. tomName = " + `name);
TomSymbol tSymbol = nkt.getSymbolFromName(
tom_name);
if (tSymbol == null) {
//The contextType is used here, so it must be a ground type, not a
//type variable
//DEBUG System.out.println("visit contextType = " + contextType);
tSymbol = nkt.getSymbolFromType(contextType);
if (tSymbol != null && 
tom_name.equals("")) {
// In case of contextType is "TypeVar(name,i)"

tom_aName= tSymbol.getAstName();
}
}

TomType codomain = contextType;
if (tSymbol == null) {
tSymbol = 
 tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;
} else {
codomain = nkt.getCodomain(tSymbol);
TypeConstraintList newSubConstraints = nkt.subtypingConstraints;
nkt.subtypingConstraints = nkt.addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newSubConstraints);
//DEBUG System.out.println("InferTypes:BQTerm bqappl -- constraint = "
//DEBUG + `codomain + " = " + contextType);
}

BQTermList newBQTList = 
tom_bqTList;
if (!
tom_bqTList.isEmptyconcBQTerm()) {
//DEBUG System.out.println("\n Test pour BQTerm-inferTypes in BQAppl. bqTList = " + `bqTList);
// TODO : verify if we pass codomain or contextType
newBQTList =
nkt.inferBQTermList(
tom_bqTList,
tSymbol,codomain);
}

// TO VERIFY

{
{
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

return 
 tom.engine.adt.code.types.bqterm.FunctionCall.make(tom_aName, contextType, newBQTList) ; 


}
}

}

}

return 
 tom.engine.adt.code.types.bqterm.BQAppl.make(tom_optionList, tom_aName, newBQTList) ;


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
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
nkt.inferAllTypes(
 (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getTomTerm() ,contextType); 

}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch16_9= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch16_5= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch16_4= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch16_6= null ;
 tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch16_7= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch16_9= true ;
tomMatch16_4= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ;
tomMatch16_5= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;
tomMatch16_6= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;
tomMatch16_7= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch16_9= true ;
tomMatch16_4= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ;
tomMatch16_5= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;
tomMatch16_6= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;
tomMatch16_7= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;

}
}
}
if (tomMatch16_9) {
 tom.engine.adt.tomtype.types.TomType  tom_aType=tomMatch16_6;
 tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList=tomMatch16_7;
 tom.engine.adt.tomterm.types.TomTerm  tom_var=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);

//DEBUG System.out.println("InferTypes:TomTerm var = " + `var);
nkt.checkNonLinearityOfVariables(
tom_var);
TypeConstraintList newSubConstraints = nkt.subtypingConstraints;
nkt.subtypingConstraints =
nkt.addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_aType, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch16_5, tomMatch16_4) ) ,newSubConstraints);  
//DEBUG System.out.println("InferTypes:TomTerm var -- constraint = " +
//DEBUG `aType + " = " + contextType);
ConstraintList newCList = 
tom_cList;

{
{
if ( (tom_cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {
if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {
if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).isEmptyconcConstraint() )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch17_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getHeadconcConstraint() ;
if ( (tomMatch17_4 instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch17_4.getVar() ;
if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getTailconcConstraint() .isEmptyconcConstraint() ) {

//DEBUG System.out.println("InferTypes:TomTerm aliasvar -- constraint = " +
//DEBUG   nkt.getType(`boundTerm) + " = " + `contextType);
//nkt.addConstraint(`Equation(nkt.getType(boundTerm),contextType,nkt.getInfoFromTomTerm(boundTerm))); 
TypeConstraintList newEqConstraints = nkt.equationConstraints;
nkt.equationConstraints =
nkt.addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), tom_aType, nkt.getInfoFromTomTerm(tom_boundTerm)) ,newEqConstraints); 


}
}
}
}
}

}

}

return 
tom_var.setConstraints(newCList);


}

}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch16_12= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ;
 tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ;
if ( ((tomMatch16_12 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch16_12 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
 tom.engine.adt.tomname.types.TomNameList  tom_nList=tomMatch16_12;
if (!( tomMatch16_12.isEmptyconcTomName() )) {
 tom.engine.adt.tomname.types.TomName  tomMatch16_20= tomMatch16_12.getHeadconcTomName() ;
if ( (tomMatch16_20 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.tomslot.types.SlotList  tom_sList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getSlots() ;
 tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;

// In case of a String, tomName is "" for ("a","b")
TomSymbol tSymbol = nkt.getSymbolFromName(
 tomMatch16_20.getString() );
// IF_1
if (tSymbol == null) {
//The contextType is used here, so it must be a ground type, not a
//type variable
tSymbol = nkt.getSymbolFromType(contextType);

// IF_2
if (tSymbol != null) {
// In case of contextType is "TypeVar(name,i)"

tom_nList= 
 tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(tSymbol.getAstName(), tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) ;
} 
}
//DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl. tSymbol = " + `tSymbol);
//DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl. astName = " +`concTomName(tSymbol.getAstName()));
//DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl.
//tSymbol = " + tSymbol);

TomType codomain = contextType;

// IF_3
if (tSymbol == null) {
//DEBUG System.out.println("tSymbol is still null!");
tSymbol = 
 tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;
} else {
// This code can not be moved to IF_2 because tSymbol may don't be
// "null" since the begginning and then does not enter into neither IF_1 nor
// IF_2
codomain = nkt.getCodomain(tSymbol);
//DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl. codomain = " + codomain);
TypeConstraintList newSubConstraints = nkt.subtypingConstraints;
nkt.subtypingConstraints = nkt.addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch16_12.getHeadconcTomName() , tom_optionList) ) ,newSubConstraints);
//DEBUG System.out.println("InferTypes:TomTerm recordappl -- constraint" + codomain + " = " + contextType);
}

ConstraintList newCList = 
tom_cList;

{
{
if ( (tom_cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {
if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {
if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).isEmptyconcConstraint() )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch18_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getHeadconcConstraint() ;
if ( (tomMatch18_4 instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch18_4.getVar() ;
if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getTailconcConstraint() .isEmptyconcConstraint() ) {

//DEBUG System.out.println("InferTypes:TomTerm aliasrecordappl -- constraint = " +
//DEBUG     nkt.getType(`boundTerm) + " = " + contextType);
//nkt.addConstraint(`Equation(nkt.getType(boundTerm),contextType,nkt.getInfoFromTomTerm(boundTerm))); 
TypeConstraintList newEqConstraints = nkt.equationConstraints;
nkt.equationConstraints =
nkt.addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), codomain, nkt.getInfoFromTomTerm(tom_boundTerm)) ,newEqConstraints); 


}
}
}
}
}

}

}


SlotList newSList = 
 tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
if (!
tom_sList.isEmptyconcSlot()) {
// TODO : verify if we pass codomain or contextType

newSList=
nkt.inferSlotList(
tom_sList,tSymbol,codomain);
}
return 
 tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_optionList, tom_nList, newSList, newCList) ;


}
}
}
}
}

}


}
return _visit_TomTerm(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomsignature.types.TomVisit  visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tom__arg) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) {

BQTermList BQTList = nkt.varList;
ConstraintInstructionList newCIList =
nkt.inferConstraintInstructionList(
 (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getAstConstraintInstructionList() );
nkt.varList = BQTList;
return 
 tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make( (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getVNode() , newCIList,  (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getOptions() ) ;


}
}

}

}
return _visit_TomVisit(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {

BQTermList BQTList = nkt.varList;
ConstraintInstructionList newCIList =
nkt.inferConstraintInstructionList(
 (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() );
nkt.varList = BQTList;
return 
 tom.engine.adt.tominstruction.types.instruction.Match.make(newCIList,  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOptions() ) ;


}
}

}

}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.Code  visit_Code( tom.engine.adt.code.types.Code  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.Code) ) {
boolean tomMatch21_3= false ;
 tom.engine.adt.code.types.CodeList  tomMatch21_1= null ;
if ( ((( tom.engine.adt.code.types.Code )tom__arg) instanceof tom.engine.adt.code.types.code.Tom) ) {
{
tomMatch21_3= true ;
tomMatch21_1= (( tom.engine.adt.code.types.Code )tom__arg).getCodeList() ;

}
} else {
if ( ((( tom.engine.adt.code.types.Code )tom__arg) instanceof tom.engine.adt.code.types.code.TomInclude) ) {
{
tomMatch21_3= true ;
tomMatch21_1= (( tom.engine.adt.code.types.Code )tom__arg).getCodeList() ;

}
}
}
if (tomMatch21_3) {

nkt.generateDependencies();
System.out.println("Dependencies: " + nkt.dependencies);
//DEBUG System.out.println("Code with term = " + `code + " and contextType = " +
//DEBUG     contextType);
CodeList newCList = nkt.inferCodeList(
tomMatch21_1);
return 
(( tom.engine.adt.code.types.Code )tom__arg).setCodeList(newCList);


}

}

}

}
return _visit_Code(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_inferTypes( tom.engine.adt.tomtype.types.TomType  t0,  NewKernelTyper  t1) { 
return new inferTypes(t0,t1);
}


/**
* The method <code>checkNonLinearityOfVariables</code> searches for variables
* occurring more than once in a condition.
* <p>
* For each variable of type
* "TomTerm" that already exists in varPatternList or in varList, a type
* constraint is added to <code>equationConstraints</code> to ensure that  both
* variables have same type (this happens in case of non-linearity).
* <p>
* OBS.: we also need to check the varList since a Variable/VariableStar can have
* occurred in a previous condition as a BQVariable/BQVariableStar, in the
* case of a composed condition
* e.g. (x < 10 ) && f(x) << e -> { action }
* @param var the variable to have the linearity checked
*/
private void checkNonLinearityOfVariables(TomTerm var) {

{
{
if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch22_35= false ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch22_3= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch22_5= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch22_4= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch22_35= true ;
tomMatch22_3= (( tom.engine.adt.tomterm.types.TomTerm )var).getOptions() ;
tomMatch22_4= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;
tomMatch22_5= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch22_35= true ;
tomMatch22_3= (( tom.engine.adt.tomterm.types.TomTerm )var).getOptions() ;
tomMatch22_4= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;
tomMatch22_5= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstType() ;

}
}
}
if (tomMatch22_35) {
 tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch22_3;
 tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch22_4;
 tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch22_5;
if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {
if ( (((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {
 tom.engine.adt.tomterm.types.TomList  tomMatch22__end__10=(( tom.engine.adt.tomterm.types.TomList )varPatternList);
do {
{
if (!( tomMatch22__end__10.isEmptyconcTomTerm() )) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch22_21= tomMatch22__end__10.getHeadconcTomTerm() ;
boolean tomMatch22_32= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch22_20= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch22_19= null ;
if ( (tomMatch22_21 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch22_32= true ;
tomMatch22_19= tomMatch22_21.getAstName() ;
tomMatch22_20= tomMatch22_21.getAstType() ;

}
} else {
if ( (tomMatch22_21 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch22_32= true ;
tomMatch22_19= tomMatch22_21.getAstName() ;
tomMatch22_20= tomMatch22_21.getAstType() ;

}
}
}
if (tomMatch22_32) {
if ( (tom_aName==tomMatch22_19) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch22_20;
boolean tomMatch22_31= false ;
if ( (tom_aType1==tomMatch22_20) ) {
if ( (tom_aType2==tomMatch22_20) ) {
tomMatch22_31= true ;
}
}
if (!(tomMatch22_31)) {

TypeConstraintList newEqConstraints = equationConstraints;
equationConstraints = addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newEqConstraints);

}

}
}

}
if ( tomMatch22__end__10.isEmptyconcTomTerm() ) {
tomMatch22__end__10=(( tom.engine.adt.tomterm.types.TomList )varPatternList);
} else {
tomMatch22__end__10= tomMatch22__end__10.getTailconcTomTerm() ;
}

}
} while(!( (tomMatch22__end__10==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));
}
}
if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch22__end__16=(( tom.engine.adt.code.types.BQTermList )varList);
do {
{
if (!( tomMatch22__end__16.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch22_24= tomMatch22__end__16.getHeadconcBQTerm() ;
boolean tomMatch22_34= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch22_22= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch22_23= null ;
if ( (tomMatch22_24 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch22_34= true ;
tomMatch22_22= tomMatch22_24.getAstName() ;
tomMatch22_23= tomMatch22_24.getAstType() ;

}
} else {
if ( (tomMatch22_24 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch22_34= true ;
tomMatch22_22= tomMatch22_24.getAstName() ;
tomMatch22_23= tomMatch22_24.getAstType() ;

}
}
}
if (tomMatch22_34) {
if ( (tom_aName==tomMatch22_22) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch22_23;
boolean tomMatch22_33= false ;
if ( (tom_aType1==tomMatch22_23) ) {
if ( (tom_aType2==tomMatch22_23) ) {
tomMatch22_33= true ;
}
}
if (!(tomMatch22_33)) {

TypeConstraintList newEqConstraints = equationConstraints;
equationConstraints = addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newEqConstraints);

}

}
}

}
if ( tomMatch22__end__16.isEmptyconcBQTerm() ) {
tomMatch22__end__16=(( tom.engine.adt.code.types.BQTermList )varList);
} else {
tomMatch22__end__16= tomMatch22__end__16.getTailconcBQTerm() ;
}

}
} while(!( (tomMatch22__end__16==(( tom.engine.adt.code.types.BQTermList )varList)) ));
}
}

}

}

}

}

}

/**
* The method <code>checkNonLinearityOfBQVariables</code> searches for variables
* occurring more than once in a condition.
* <p>
* For each variable of type
* "BQTerm" that already exists in varPatternList or in varList, a type
* constraint is added to <code>equationConstraints</code> to ensure that  both
* variables have same type (this happens in case of non-linearity).
* <p>
* OBS.: we also need to check the varPatternList since a BQVariable/BQVariableStar can have
* occurred in a previous condition as a Variable/VariableStar, in the
* case of a composed condition or of a inner match
* e.g. f(x) << e && (x < 10 ) -> { action } 
* @param bqvar the variable to have the linearity checked
*/
private void checkNonLinearityOfBQVariables(BQTerm bqvar) {

{
{
if ( (bqvar instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch23_35= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch23_5= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch23_4= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch23_3= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch23_35= true ;
tomMatch23_3= (( tom.engine.adt.code.types.BQTerm )bqvar).getOptions() ;
tomMatch23_4= (( tom.engine.adt.code.types.BQTerm )bqvar).getAstName() ;
tomMatch23_5= (( tom.engine.adt.code.types.BQTerm )bqvar).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch23_35= true ;
tomMatch23_3= (( tom.engine.adt.code.types.BQTerm )bqvar).getOptions() ;
tomMatch23_4= (( tom.engine.adt.code.types.BQTerm )bqvar).getAstName() ;
tomMatch23_5= (( tom.engine.adt.code.types.BQTerm )bqvar).getAstType() ;

}
}
}
if (tomMatch23_35) {
 tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch23_3;
 tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch23_4;
 tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch23_5;
if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch23__end__10=(( tom.engine.adt.code.types.BQTermList )varList);
do {
{
if (!( tomMatch23__end__10.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch23_21= tomMatch23__end__10.getHeadconcBQTerm() ;
boolean tomMatch23_32= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch23_19= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch23_20= null ;
if ( (tomMatch23_21 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch23_32= true ;
tomMatch23_19= tomMatch23_21.getAstName() ;
tomMatch23_20= tomMatch23_21.getAstType() ;

}
} else {
if ( (tomMatch23_21 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch23_32= true ;
tomMatch23_19= tomMatch23_21.getAstName() ;
tomMatch23_20= tomMatch23_21.getAstType() ;

}
}
}
if (tomMatch23_32) {
if ( (tom_aName==tomMatch23_19) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch23_20;
boolean tomMatch23_31= false ;
if ( (tom_aType1==tomMatch23_20) ) {
if ( (tom_aType2==tomMatch23_20) ) {
tomMatch23_31= true ;
}
}
if (!(tomMatch23_31)) {

TypeConstraintList newEqConstraints = equationConstraints;
equationConstraints =
addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newEqConstraints); 

}

}
}

}
if ( tomMatch23__end__10.isEmptyconcBQTerm() ) {
tomMatch23__end__10=(( tom.engine.adt.code.types.BQTermList )varList);
} else {
tomMatch23__end__10= tomMatch23__end__10.getTailconcBQTerm() ;
}

}
} while(!( (tomMatch23__end__10==(( tom.engine.adt.code.types.BQTermList )varList)) ));
}
}
if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {
if ( (((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {
 tom.engine.adt.tomterm.types.TomList  tomMatch23__end__16=(( tom.engine.adt.tomterm.types.TomList )varPatternList);
do {
{
if (!( tomMatch23__end__16.isEmptyconcTomTerm() )) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch23_24= tomMatch23__end__16.getHeadconcTomTerm() ;
boolean tomMatch23_34= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch23_22= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch23_23= null ;
if ( (tomMatch23_24 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch23_34= true ;
tomMatch23_22= tomMatch23_24.getAstName() ;
tomMatch23_23= tomMatch23_24.getAstType() ;

}
} else {
if ( (tomMatch23_24 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch23_34= true ;
tomMatch23_22= tomMatch23_24.getAstName() ;
tomMatch23_23= tomMatch23_24.getAstType() ;

}
}
}
if (tomMatch23_34) {
if ( (tom_aName==tomMatch23_22) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch23_23;
boolean tomMatch23_33= false ;
if ( (tom_aType1==tomMatch23_23) ) {
if ( (tom_aType2==tomMatch23_23) ) {
tomMatch23_33= true ;
}
}
if (!(tomMatch23_33)) {

TypeConstraintList newEqConstraints = equationConstraints;
equationConstraints =
addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newEqConstraints); 

}

}
}

}
if ( tomMatch23__end__16.isEmptyconcTomTerm() ) {
tomMatch23__end__16=(( tom.engine.adt.tomterm.types.TomList )varPatternList);
} else {
tomMatch23__end__16= tomMatch23__end__16.getTailconcTomTerm() ;
}

}
} while(!( (tomMatch23__end__16==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));
}
}

}

}

}

}

}

/**
* The method <code>inferCodeList</code> starts inference process which takes one
* code at a time
* <ul>
*  <li> all lists and hashMaps are reset
*  <li> each code is typed with fresh type variables
*  <li> each code is traversed in order to generate type constraints
*  <li> the type constraints of "equationConstraints" and
*        "subtypingConstraints" lists are solved at the end
*        of the current code generating a mapping (a set of
*        substitutions for each type variable)
*  <li> the mapping is applied over the code and the symbol table
* </ul>
* @param cList the tom code list to be type inferred
* @return      the tom typed code list
*/
private CodeList inferCodeList(CodeList cList) {
CodeList newCList = 
 tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;
for (Code code : cList.getCollectionconcCode()) {
init();
code =  collectKnownTypesFromCode(
code);
System.out.println("------------- Code typed with typeVar:\n code = " +

code);
code = inferAllTypes(code,
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
//DEBUG printGeneratedConstraints(subtypingConstraints);
solveConstraints();
System.out.println("substitutions = " + substitutions);
code = replaceInCode(code);
System.out.println("------------- Code typed with substitutions:\n code = " +

code);
replaceInSymbolTable();
newCList = 
 tom.engine.adt.code.types.codelist.ConsconcCode.make(code,tom_append_list_concCode(newCList, tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
}
return newCList.reverse();
}

/**
* The method <code>inferConstraintInstructionList</code> applies rule CT-RULE
* to a pair "condition -> action" in order to collect all variables occurring
* in the condition and put them into <code>varPatternList</code> (for those
* variables occurring in match constraints) and <code>varList</code> (for
* those variables occurring in numeric constraints) to be able to handle
* non-linearity.
* <p>
* The condition (left-hand side) is traversed and then the action (right-hand
* side) is traversed in order to generate type constraints.
* <p>
* CT-RULE rule:
* IF found "cond --> (e1,...,en)" where "ei" are backquote terms composing
* the action
* THEN infers types of condition (by calling <code>inferConstraint</code>
* method) and action (by calling <code>inferConstraintList</code> for each
* match constraint occurring in the action) 
* @param ciList  the pair "condition -> action" to be type inferred 
*/
private ConstraintInstructionList inferConstraintInstructionList(ConstraintInstructionList ciList) {
ConstraintInstructionList newCIList = 
 tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ;
for (ConstraintInstruction cInst :
ciList.getCollectionconcConstraintInstruction()) {
try {

{
{
if ( (cInst instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tom_constraint= (( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst).getConstraint() ;

TomList TTList = varPatternList;

tom_make_TopDownCollect(tom_make_CollectVars(this)).visitLight(
tom_constraint);
Constraint newConstraint = inferConstraint(
tom_constraint);
//DEBUG System.out.println("inferConstraintInstructionList: action " +
//DEBUG     `action);
Instruction newAction = 
inferAllTypes( (( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst).getAction() , tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
varPatternList = TTList;
newCIList =

 tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(newConstraint, newAction,  (( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst).getOptions() ) ,tom_append_list_concConstraintInstruction(newCIList, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;


}
}

}

}

} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("inferConstraintInstructionList: failure on " + 
cInst);
}
}
return newCIList.reverse();
}

/**
* The class <code>CollectVars</code> is generated from a strategy which
* collect all variables (Variable, VariableStar, BQVariable, BQVariableStar
* occurring in a condition.
* @param nkt an instance of object NewKernelTyper
*/

public static class CollectVars extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public CollectVars( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
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
boolean tomMatch25_2= false ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
tomMatch25_2= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
tomMatch25_2= true ;
}
}
if (tomMatch25_2) {
nkt.addBQTerm(
(( tom.engine.adt.code.types.BQTerm )tom__arg)); 

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
boolean tomMatch26_2= false ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch26_2= true ;
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch26_2= true ;
}
}
if (tomMatch26_2) {
nkt.addTomTerm(
(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)); 

}

}

}

}
return _visit_TomTerm(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_CollectVars( NewKernelTyper  t0) { 
return new CollectVars(t0);
}


/**
* The method <code>inferConstraint</code> applies rule CT-MATCH, CT-EQ, CT-CONJ,
* or CT-DISJ to a "condition" in order to infer its type.
* <p>
* CT-MATCH rule:
* IF found "e1 << [T] e2" 
* THEN infers type of e1 and e2 and add a type constraint "T1 = T2", where
* "Ti" is a fresh type variable generated to "ei" (by
* <code>inferBQTerm</code>, a type constraint "T = T2" will be added)
* <p>
* CT-EQ rule:
* IF found "e1 == e2", "e1 <= e2", "e1 < e2", "e1 >= e2" or "e1 > e2"
* THEN infers type of e1 and e2 and add a type constraint "T1 = T2", where
* "Ti" is a fresh type variable generated to "ei"  
* <p>
* CT-CONJ rule (resp. CT-DISJ):
* IF found "cond1 && cond2" (resp. "cond1 || cond2")
* THEN infers type of cond1 and cond2 (by calling
* <code>inferConstraint</code> for each condition)
* @param constraint the "condition" to be type inferred 
*/
private Constraint inferConstraint(Constraint constraint) {

{
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_pattern= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getPattern() ;
 tom.engine.adt.code.types.BQTerm  tom_subject= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getSubject() ;
 tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getAstType() ;

//DEBUG System.out.println("inferConstraint l1 -- subject = " + `subject);
TomType tPattern = getType(
tom_pattern);
TomType tSubject = getType(
tom_subject);
if (tPattern == null || tPattern == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
tPattern = getUnknownFreshTypeVar();
}
if (tSubject == null || tSubject == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
tSubject = getUnknownFreshTypeVar();
}
//DEBUG System.out.println("inferConstraint: match -- constraint " +
//DEBUG     tPattern + " = " + tSubject);

{
{
if ( (tom_aType instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_aType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
hasUndeclaredType( (( tom.engine.adt.tomtype.types.TomType )tom_aType).getTomType() ,getInfoFromTomTerm(tom_pattern).getOptions()); 
/* There is no explicit type, so T_pattern = T_subject */
// TODO verify if we replace it by same code of case "Type[]"
TypeConstraintList newEqConstraints = equationConstraints;
equationConstraints =
addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tPattern, tSubject, getInfoFromTomTerm(tom_pattern)) ,newEqConstraints);


}
}

}
{
if ( (tom_aType instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_aType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

/* T_pattern = T_cast and T_cast <: T_subject */
TypeConstraintList newEqConstraints = equationConstraints;
TypeConstraintList newSubConstraints = subtypingConstraints;
equationConstraints =
addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tPattern, tom_aType, getInfoFromTomTerm(tom_pattern)) ,newEqConstraints);
subtypingConstraints = addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_aType, tSubject, getInfoFromBQTerm(tom_subject)) ,newSubConstraints);


}
}

}


}

TomTerm newPattern = 
inferAllTypes(tom_pattern,tPattern);
BQTerm newSubject = 
inferAllTypes(tom_subject,tSubject);
//hasUndeclaredType(newSubject);
return 
 tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(newPattern, newSubject, tom_aType) ;


}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tom_left= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getLeft() ;
 tom.engine.adt.code.types.BQTerm  tom_right= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getRight() ;

TomType tLeft = getType(
tom_left);
TomType tRight = getType(
tom_right);
if (tLeft == null || tLeft == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
tLeft = getUnknownFreshTypeVar();
}
if (tRight == null || tRight == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
tRight = getUnknownFreshTypeVar();
}
//DEBUG System.out.println("inferConstraint: match -- constraint " +
//DEBUG     tLeft + " = " + tRight);

// To represent the relationshipo between both argument types
TomType lowerType = getUnknownFreshTypeVar();
TypeConstraintList newSubConstraints = subtypingConstraints;
newSubConstraints =
addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tLeft, getInfoFromBQTerm(tom_left)) ,newSubConstraints);
subtypingConstraints =
addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tRight, getInfoFromBQTerm(tom_right)) ,newSubConstraints);
BQTerm newLeft = inferAllTypes(
tom_left,tLeft);
BQTerm newRight = inferAllTypes(
tom_right,tRight);
//hasUndeclaredType(newLeft);
//hasUndeclaredType(newRight);
return 
 tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(newLeft, newRight,  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getType() ) ;


}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyAndConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {

ConstraintList cList = 
 tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadAndConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
Constraint newAConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;
for (Constraint cArg : cList.getCollectionconcConstraint()) {
cArg = inferConstraint(cArg);
newAConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(newAConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ;
}
return newAConstraint;


}
}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyOrConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) {

ConstraintList cList = 
 tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadOrConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
Constraint newOConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ;
for (Constraint cArg : cList.getCollectionconcConstraint()) {
cArg = inferConstraint(cArg);
newOConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(newOConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) ) ;
}
return newOConstraint;


}
}
}

}


}

return constraint;
}

/**
* The method <code>inferSlotList</code> infers types of the arguments of
* lists and functions (which are TomTerms) 
* <p> 
* It continues the application of rules CT-FUN, CT-ELEM, CT-MERGE or CT-STAR
* to each argument in order to infer its type.
* <p>
* Continuation of CT-STAR rule (applying to premises):
* IF found "l(e1,...,en,x*):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "x", where "x" represents a list with
*      head symbol "l"
* <p>
* Continuation of CT-ELEM rule (applying to premises which are
* not lists):
* IF found "l(e1,...en,e):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "A = T" for the last
*      argument, where "A" is a fresh type variable  and
*      "e" does not represent a list with head symbol "l"
* <p>
* Continuation of CT-MERGE rule (applying to premises which are lists with
* the same operator):
* IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e", where "e" represents a list with
*      head symbol "l"
* <p>
* Continuation of CT-FUN rule (applying to premises):
* IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
* THEN infers type of arguments and adds a type constraint "Ai =
*      Ti" for each argument, where "Ai" is a fresh type variable
* <p>
* @param sList a list of arguments of a list/function
* @param tSymbol the TomSymbol related to the list/function
* @param contextType the codomain of the list/function 
*/
private SlotList inferSlotList(SlotList sList, TomSymbol tSymbol, TomType
contextType) {
TomType argType = contextType;
SlotList newSList = 
 tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;

{
{
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

TomName argName;
TomTerm argTerm;
TomSymbol argSymb;
for (Slot slot : sList.getCollectionconcSlot()) {
argName = slot.getSlotName();
argTerm = slot.getAppl();
argSymb = getSymbolFromTerm(argTerm);
if(!(TomBase.isListOperator(
argSymb) || TomBase.isArrayOperator(
argSymb))) {

{
{
if ( (argTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch30_2= false ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )argTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch30_2= true ;
}
if (!(tomMatch30_2)) {

//DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
argType = getUnknownFreshTypeVar();

}

}

}

}

}
argTerm = 
inferAllTypes(argTerm,argType);
newSList = 
 tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(argName, argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
}
return newSList.reverse(); 


}
}

}
{
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch29_4= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;
 tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;
if ( (tomMatch29_4 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch29_6= tomMatch29_4.getDomain() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch29_7= tomMatch29_4.getCodomain() ;
if ( ((tomMatch29_6 instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || (tomMatch29_6 instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if (!( tomMatch29_6.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch29_6.getHeadconcTomType() ;
if ( (tomMatch29_7 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch29_7.getTypeOptions() ;

TomTerm argTerm;
if(TomBase.isListOperator(
tSymbol) || TomBase.isArrayOperator(
tSymbol)) {
TomSymbol argSymb;
for (Slot slot : sList.getCollectionconcSlot()) {
argTerm = slot.getAppl();
argSymb = getSymbolFromTerm(argTerm);
if(!(TomBase.isListOperator(
argSymb) || TomBase.isArrayOperator(
argSymb))) {

{
{
if ( (argTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )argTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {

TypeOptionList newTOptions = 
tom_tOptions;
// Is this test really necessary?

{
{
if ( (tom_tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch32__end__4=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);
do {
{
if (!( tomMatch32__end__4.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch32_8= tomMatch32__end__4.getHeadconcTypeOption() ;
if ( (tomMatch32_8 instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if (!( (tom_symName== tomMatch32_8.getRootSymbolName() ) )) {

throw new TomRuntimeException("typeVariableList: symbol '"
+ 
tSymbol+ "' with more than one constructor (rootsymbolname)");


}
}
}
if ( tomMatch32__end__4.isEmptyconcTypeOption() ) {
tomMatch32__end__4=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);
} else {
tomMatch32__end__4= tomMatch32__end__4.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch32__end__4==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) ));
}
}

}
{
if ( (tom_tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch32__end__13=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);
do {
{
if (!( tomMatch32__end__13.isEmptyconcTypeOption() )) {
boolean tomMatch32_17= false ;
if ( ( tomMatch32__end__13.getHeadconcTypeOption()  instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
tomMatch32_17= true ;
}
if (!(tomMatch32_17)) {

newTOptions =

 tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom_symName) ,tom_append_list_concTypeOption(tom_tOptions, tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;


}

}
if ( tomMatch32__end__13.isEmptyconcTypeOption() ) {
tomMatch32__end__13=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);
} else {
tomMatch32__end__13= tomMatch32__end__13.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch32__end__13==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) ));
}
}

}


}


// Case CT-STAR rule (applying to premises):
argType = 
 tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch29_7.getTomType() ,  tomMatch29_7.getTlType() ) ;


}
}

}
{
if ( (argTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch31_4= false ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )argTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch31_4= true ;
}
if (!(tomMatch31_4)) {

// Case CT-ELEM rule (applying to premises which are not lists)
//argType = getUnknownFreshTypeVar();
argType = 
tom_headTTList;
//DEBUG System.out.println("inferSlotList: !VariableStar -- constraint "
//DEBUG     + `headTTList + " = " + argType);
//addConstraint(`Equation(argType,headTTList,getInfoFromTomTerm(argTerm)));


}

}

}


}

} else if (
tom_symName!= argSymb.getAstName()) {
/*
* Case CT-ELEM rule which premise is a list
* A list with a sublist whose constructor is different
* e.g. 
* A = ListA(A*) and B = ListB(A*) | b()
* ListB(ListA(a()))
*/
//argType = getUnknownFreshTypeVar();
argType = 
tom_headTTList;
//DEBUG System.out.println("inferSlotList: symName != argSymbName -- constraint "
//DEBUG     + `headTTList + " = " + argType);
//addConstraint(`Equation(argType,headTTList,getInfoFromTomTerm(argTerm)));
}

// Case CT-MERGE rule (applying to premises):
argTerm = 
inferAllTypes(argTerm,argType);
newSList = 
 tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(slot.getSlotName(), argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
}
} else {
// In case of a function
// Case CT-FUN rule (applying to premises):

TomName argName;
for (Slot slot : sList.getCollectionconcSlot()) {
argName = slot.getSlotName();
argType = TomBase.getSlotType(tSymbol,argName);
argTerm = 
inferAllTypes(slot.getAppl(),argType);
newSList = 
 tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(argName, argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
//DEBUG System.out.println("InferSlotList CT-FUN -- end of for with slotappl = " + `argTerm);
}
}
return newSList.reverse(); 


}
}
}
}
}
}

}


}

throw new TomRuntimeException("inferSlotList: failure on " + 
sList);
}

/**
* The method <code>inferBQTermList</code> infers types of the arguments of
* lists, functions and calls of methods (which are BQTerms) 
* <p> 
* It continues the application of rules CT-FUN, CT-ELEM, CT-MERGE or CT-STAR
* to each argument in order to infer its type.
* <p>
* Continuation of CT-STAR rule (applying to premises):
* IF found "l(e1,...,en,x*):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "x", where "x" represents a list with
*      head symbol "l"
* <p>
* Continuation of CT-ELEM rule (applying to premises which are
* not lists):
* IF found "l(e1,...en,e):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "A = T" for the last
*      argument, where "A" is a fresh type variable  and
*      "e" does not represent a list with head symbol "l"
* <p>
* Continuation of CT-MERGE rule (applying to premises which are lists with
* the same operator):
* IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e", where "e" represents a list with
*      head symbol "l"
* <p>
* Continuation of CT-FUN rule (applying to premises):
* IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
* THEN infers type of arguments and adds a type constraint "Ai =
*      Ti" for each argument, where "Ai" is a fresh type variable
* <p>
* @param bqList a list of arguments of a list/function/method
* @param tSymbol the TomSymbol related to the list/function
* @param contextType the codomain of the list/function 
*/
private BQTermList inferBQTermList(BQTermList bqTList, TomSymbol tSymbol, TomType
contextType) {
TomType argType = contextType;
BQTermList newBQTList = 
 tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;

{
{
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
argTerm = 
inferAllTypes(argTerm, tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
newBQTList = 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
}
return newBQTList.reverse(); 


}
}

}
{
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch33_4= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;
 tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;
if ( (tomMatch33_4 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch33_8= tomMatch33_4.getDomain() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch33_9= tomMatch33_4.getCodomain() ;
if ( ((tomMatch33_8 instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || (tomMatch33_8 instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if (!( tomMatch33_8.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch33_8.getHeadconcTomType() ;
if ( (tomMatch33_9 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch33_9.getTypeOptions() ;
 tom.engine.adt.tomslot.types.PairNameDeclList  tom_pNDList= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getPairNameDeclList() ;

TomTypeList symDomain = 
tomMatch33_8;
TomSymbol argSymb;
if(TomBase.isListOperator(
tSymbol) || TomBase.isArrayOperator(
tSymbol)) {
for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
argSymb = getSymbolFromTerm(argTerm);
if(!(TomBase.isListOperator(
argSymb) || TomBase.isArrayOperator(
argSymb))) {

{
{
if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( (((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {

// We don't know what is into the Composite
// It can be a BQVariableStar or a list operator or a list of
// CompositeBQTerm or something else
argType = 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;


}
}

}
{
if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {

TypeOptionList newTOptions = 
tom_tOptions;
// Is this test really necessary?

{
{
if ( (tom_tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch35__end__4=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);
do {
{
if (!( tomMatch35__end__4.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch35_8= tomMatch35__end__4.getHeadconcTypeOption() ;
if ( (tomMatch35_8 instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if (!( (tom_symName== tomMatch35_8.getRootSymbolName() ) )) {

throw new TomRuntimeException("typeVariableList: symbol '"
+ 
tSymbol+ "' with more than one constructor (rootsymbolname)");


}
}
}
if ( tomMatch35__end__4.isEmptyconcTypeOption() ) {
tomMatch35__end__4=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);
} else {
tomMatch35__end__4= tomMatch35__end__4.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch35__end__4==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) ));
}
}

}
{
if ( (tom_tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch35__end__13=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);
do {
{
if (!( tomMatch35__end__13.isEmptyconcTypeOption() )) {
boolean tomMatch35_17= false ;
if ( ( tomMatch35__end__13.getHeadconcTypeOption()  instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
tomMatch35_17= true ;
}
if (!(tomMatch35_17)) {

newTOptions =

 tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom_symName) ,tom_append_list_concTypeOption(tom_tOptions, tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;


}

}
if ( tomMatch35__end__13.isEmptyconcTypeOption() ) {
tomMatch35__end__13=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);
} else {
tomMatch35__end__13= tomMatch35__end__13.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch35__end__13==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) ));
}
}

}


}




// Case CT-STAR rule (applying to premises):
argType = 
 tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch33_9.getTomType() ,  tomMatch33_9.getTlType() ) ;


}
}

}
{
if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch34_7= false ;
if ( ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
tomMatch34_7= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
tomMatch34_7= true ;
}
}
if (tomMatch34_7) {

// Case CT-ELEM rule (applying to premises which are not lists)
//argType = getUnknownFreshTypeVar();
argType = 
tom_headTTList;
//DEBUG System.out.println("inferBQTermList: !BQVariableStar -- constraint "
//DEBUG     + argType + " = " + `headTTList);
//addConstraint(`Equation(argType,headTTList,getInfoFromBQTerm(argTerm)));


}

}

}


}

} else if (
tom_symName!= argSymb.getAstName()) {
/*
* Case CT-ELEM rule which premise is a list
* A list with a sublist whose constructor is different
* e.g. 
* A = ListA(A*) and B = ListB(A*) | b()
* ListB(ListA(a()))
*/
//argType = getUnknownFreshTypeVar();
argType = 
tom_headTTList;
//DEBUG System.out.println("inferSlotList: symName != argSymbName -- constraint "
//DEBUG     + `headTTList + " = " + argType);
//addConstraint(`Equation(argType,headTTList,getInfoFromBQTerm(argTerm)));
}

// Case CT-MERGE rule (applying to premises):
argTerm = 
inferAllTypes(argTerm,argType);
newBQTList = 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
}
} else {
// In case of a function
// Case CT-FUN rule (applying to premises):
if(
tom_pNDList.length() != bqTList.length()) {
Option option = TomBase.findOriginTracking(
 (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getOptions() );

{
{
if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

TomMessage.error(logger,
 (( tom.engine.adt.tomoption.types.Option )option).getFileName() , 
 (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
TomMessage.symbolNumberArgument,
tom_symName.getString(),
tom_pNDList.length(),bqTList.length());


}
}

}

}

} else {
for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
argType = symDomain.getHeadconcTomType();

{
{
if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( (((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch37__end__4=(( tom.engine.adt.code.types.BQTerm )argTerm);
do {
{
if (!( tomMatch37__end__4.isEmptyComposite() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch37_5= tomMatch37__end__4.getTailComposite() ;
 tom.engine.adt.code.types.BQTerm  tomMatch37__end__8=tomMatch37_5;
do {
{
if (!( tomMatch37__end__8.isEmptyComposite() )) {
argType = 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ; 

}
if ( tomMatch37__end__8.isEmptyComposite() ) {
tomMatch37__end__8=tomMatch37_5;
} else {
tomMatch37__end__8= tomMatch37__end__8.getTailComposite() ;
}

}
} while(!( (tomMatch37__end__8==tomMatch37_5) ));
}
if ( tomMatch37__end__4.isEmptyComposite() ) {
tomMatch37__end__4=(( tom.engine.adt.code.types.BQTerm )argTerm);
} else {
tomMatch37__end__4= tomMatch37__end__4.getTailComposite() ;
}

}
} while(!( (tomMatch37__end__4==(( tom.engine.adt.code.types.BQTerm )argTerm)) ));
}
}

}

}

argTerm = 
inferAllTypes(argTerm,argType);
newBQTList = 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
symDomain = symDomain.getTailconcTomType();
//DEBUG System.out.println("InferBQTermList CT-FUN -- end of for with bqappl = " + `argTerm);
}
}
}
return newBQTList.reverse(); 


}
}
}
}
}
}

}


}

throw new TomRuntimeException("inferBQTermList: failure on " + 
bqTList);
}

private void solveConstraints() {
try {
//DEBUG System.out.println("\nsolveConstraints 1:");
//DEBUG printGeneratedConstraints(equationConstraints);
//DEBUG printGeneratedConstraints(subtypingConstraints);
solveEquationConstraints(equationConstraints);
TypeConstraintList simplifiedConstraints =
replaceInSubtypingConstraints(subtypingConstraints);
printGeneratedConstraints(simplifiedConstraints);
simplifiedConstraints = 

tom_make_RepeatId(tom_make_solveSubtypingConstraints(this)).visitLight(simplifiedConstraints);
//DEBUG System.out.println("\nsolveConstraints 3:");
//DEBUG printGeneratedConstraints(simplifiedConstraints);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("solveConstraints: failure on " +
subtypingConstraints);
}
}


/**
* The method <code>solveEquationConstraints</code> tries to solve all type
* constraints collected during the inference
* <p> 
* There exists 3 kinds of types : variable types Ai, ground types Ti and
* ground types Ti^c which are decorated with a given symbol c. Since a type
* constraints is a pair (type1,type2) representing an equation relation
* between two types, them the set of all possibilities of arrangement between
* types is a sequence with repetition. Then, we have 9 possible cases (since
* 3^2 = 9).
* <p>
* CASE 1: tCList = {(T1 = T2),...)} and Map
*  --> detectFail(T1 = T2) to verify if T1 is equals to T2 
* <p>
* CASE 2: tCList = {(T1 = T2^c),...)} and Map
*  --> detectFail(T1 = T2^c) to verify if T1 is equals to T2 
* <p>
* CASE 3: tCList = {(T1^c = T2),...)} and Map
*  --> detectFail(T1^c = T2) to verify if T1 is equals to T2 
* <p>
* CASE 4: tCList = {(T1^a = T2^b),...)} and Map
*  --> detectFail(T1^a = T2^b) to verify if T1^a is equals to T2^b 
* <p>
* CASE 5: tCList = {(T1 = A1),...)} and Map 
*  a) Map(A1) does not exist   --> (A,T1) U Map 
*  b) Map(A1) = T2             --> detectFail(T1 = T2)
*  c) Map(A1) = A2             --> (A2,T1) U Map, since Map is saturated and
*                                  then Map(A2) does not exist 
* <p>
* CASE 6: tCList = {(T1^c = A1),...)} and Map 
*  a) Map(A1) does not exist   --> (A1,T1^c) U Map 
*  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
*  c) Map(A1) = A2             --> (A2,T1^c) U Map, since Map is saturated and
*                                  then Map(A2) does not exist 
* <p>
* CASE 7: tCList = {(A1 = T1),...)} and Map 
*  a) Map(A1) does not exist   --> (A1,T1) U Map 
*  b) Map(A1) = T2             --> detectFail(T1 = T2)
*  c) Map(A1) = A2             --> (A2,A1) U Map, since Map is saturated and
*                                  then Map(A2) does not exist 
* <p>
* CASE 8: tCList = {(A1 = T1^c),...)} and Map 
*  a) Map(A1) does not exist   --> (A1,T1^c) U Map 
*  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
*  c) Map(A1) = A2             --> (A2,T1^c) U Map, since Map is saturated and
*                                  then Map(A2) does not exist 
* <p>
* CASE 9: tCList = {(A1 = A2),...)} and Map
*  a) Map(A1) = T1 (or T1^a) and
*    i)    Map(A2) does not exist    --> (A2,T1) U Map (or (A2,T1^a) U Map) 
*    ii)   Map(A2) = T2 (or T2^b)    --> detectFail(T1 = T2) (or detectFail(T1^a = T2^b))
*    iii)  Map(A2) = A3              --> (A3,T1) U Map (or (A3,T1^a) U Map), since Map is saturated and
*                                        then Map(A3) does not exist 
*
*  b) Map(A1) = A3 and
*    i)    Map(A2) does not exist    --> (A2,A3) U Map 
*    ii)   Map(A2) = T2 (or T2^b)    --> (A3,T2) U Map, since Map is saturated and
*                                        then Map(A3) does not exist 
*    iii)  Map(A2) = A4              --> (A3,A4) U Map, since Map is saturated and
*                                        then Map(A3) does not exist 
*  c) Map(A1) does not exist and
*    i)    Map(A2) does not exist    --> (A1,A2) U Map
*    ii)   Map(A2) = T1 (or T1^a)    --> (A1,T1) U Map (or (A1,T1^a) U Map)
*    iii)  Map(A2) = A3              --> (A1,A3) U Map 
*/
private TypeConstraintList solveEquationConstraints(TypeConstraintList tCList) {
for (TypeConstraint tConstraint :
tCList.getCollectionconcTypeConstraint()) {
matchBlockAdd :
{

{
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch38_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch38_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_groundType1=tomMatch38_1;
 tom.engine.adt.tomtype.types.TomType  tom_groundType2=tomMatch38_2;
boolean tomMatch38_9= false ;
if ( (tomMatch38_1 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType1==tomMatch38_1) ) {
tomMatch38_9= true ;
}
}
if (!(tomMatch38_9)) {
boolean tomMatch38_8= false ;
if ( (tomMatch38_2 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType2==tomMatch38_2) ) {
tomMatch38_8= true ;
}
}
if (!(tomMatch38_8)) {
if (!( (tom_groundType2==tom_groundType1) )) {
detectFail((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint));
break matchBlockAdd;


}
}

}

}
}

}
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch38_11= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch38_12= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch38_11;
if ( (tomMatch38_12 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch38_12;
boolean tomMatch38_18= false ;
if ( (tomMatch38_11 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch38_11) ) {
tomMatch38_18= true ;
}
}
if (!(tomMatch38_18)) {

if (substitutions.containsKey(
tom_typeVar)) {
TomType mapTypeVar = substitutions.get(
tom_typeVar);
if (!isTypeVar(mapTypeVar)) {

detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_groundType, mapTypeVar,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) );
} else {
// if (isTypeVar(mapTypeVar))
addSubstitution(mapTypeVar,
tom_groundType);
}
} else {
addSubstitution(
tom_typeVar,
tom_groundType);
}
break matchBlockAdd;


}

}
}
}

}
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch38_20= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch38_21= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
if ( (tomMatch38_20 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch38_20;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch38_21;
boolean tomMatch38_27= false ;
if ( (tomMatch38_21 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch38_21) ) {
tomMatch38_27= true ;
}
}
if (!(tomMatch38_27)) {

if (substitutions.containsKey(
tom_typeVar)) {
TomType mapTypeVar = substitutions.get(
tom_typeVar);
if (!isTypeVar(mapTypeVar)) {

detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar, tom_groundType,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) );
} else {
// if (isTypeVar(mapTypeVar))
addSubstitution(mapTypeVar,
tom_groundType);
}
} else {
addSubstitution(
tom_typeVar,
tom_groundType);
}
break matchBlockAdd;


}

}
}
}

}
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch38_29= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch38_30= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
if ( (tomMatch38_29 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar1=tomMatch38_29;
if ( (tomMatch38_30 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar2=tomMatch38_30;
if (!( (tom_typeVar2==tom_typeVar1) )) {

TomType mapTypeVar1;
TomType mapTypeVar2;
if (substitutions.containsKey(
tom_typeVar1) && substitutions.containsKey(
tom_typeVar2)) {
mapTypeVar1 = substitutions.get(
tom_typeVar1);
mapTypeVar2 = substitutions.get(
tom_typeVar2);
if (isTypeVar(mapTypeVar1)) {
addSubstitution(mapTypeVar1,mapTypeVar2);
} else {
if (isTypeVar(mapTypeVar2)) {
addSubstitution(mapTypeVar2,mapTypeVar1);
} else {

detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar1, mapTypeVar2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) );
}
}
break matchBlockAdd;
} else if (substitutions.containsKey(
tom_typeVar1)) {
mapTypeVar1 = substitutions.get(
tom_typeVar1);
addSubstitution(
tom_typeVar2,mapTypeVar1);
break matchBlockAdd;
} else if (substitutions.containsKey(
tom_typeVar2)){
mapTypeVar2 = substitutions.get(
tom_typeVar2);
addSubstitution(
tom_typeVar1,mapTypeVar2);
break matchBlockAdd;
} else {
addSubstitution(
tom_typeVar1,
tom_typeVar2);
break matchBlockAdd;
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
return tCList;
}

/**
* The method <code>detectFail</code> 
* tries to solve all type constraints collected during the inference
* <p> 
* There exists 3 kinds of types : variable types Ai, ground types Ti and
* ground types Ti^c which are decorated with a given symbol c. Since a type
* constraints is a pair (type1,type2) representing an equation relation
* between two types, them the set of all possibilities of arrangement between
* ground types is a sequence with repetition. Then, we have 4 possible cases (since
* 2^2 = 4).
* <p>
* CASE 1: tCList = {(T1 = T2),...)} and Map
*  a) --> Fail if T1 is different from T2
*  b) --> Nothing if T1 is equals to T2
* <p>
* CASE 2: tCList = {(T1 = T2^c),...)} and Map
*  a) --> Fail if T1 is different from T2
*  b) --> Nothing if T1 is equals to T2
* <p>
* CASE 3: tCList = {(T1^c = T2),...)} and Map
*  a) --> Fail if T1 is different from T2
*  b) --> Nothing if T1 is equals to T2
* <p>
* CASE 4: tCList = {(T1^a = T2^b),...)} and Map
*  a) --> Fail if T1 is different from T2 and/or "a" is different from "b"
*  b) --> Nothing if T1 is equals to T2
* <p>
* @param tConstraint the type constraint to be verified 
*/
private void detectFail(TypeConstraint tConstraint) {
matchBlockFail : 
{

{
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch39_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch39_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
if ( (tomMatch39_1 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tom_tName1= tomMatch39_1.getTomType() ;
if ( (tomMatch39_2 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tomMatch39_6= tomMatch39_2.getTomType() ;
 String  tom_tName2=tomMatch39_6;
boolean tomMatch39_10= false ;
if ( tom_tName1.equals(tomMatch39_6) ) {
if ( tom_tName2.equals(tomMatch39_6) ) {
tomMatch39_10= true ;
}
}
if (!(tomMatch39_10)) {
if (!( "unknown type".equals(tom_tName1) )) {
if (!( "unknown type".equals(tom_tName2) )) {

//DEBUG System.out.println("In solveConstraints 1a/3a -- tConstraint  = " + `tConstraint);
printError(
tConstraint);
break matchBlockFail;


}
}
}

}
}
}
}

}
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch39_14= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch39_15= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
if ( (tomMatch39_14 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions1= tomMatch39_14.getTypeOptions() ;
if ( (tomMatch39_15 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch39_20= tomMatch39_15.getTypeOptions() ;
 tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions2=tomMatch39_20;
if (  tomMatch39_14.getTomType() .equals( tomMatch39_15.getTomType() ) ) {
if ( (tom_tOptions1 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch39__end__26=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1);
do {
{
if (!( tomMatch39__end__26.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch39_37= tomMatch39__end__26.getHeadconcTypeOption() ;
if ( (tomMatch39_37 instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if ( (tom_tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch39__end__32=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);
do {
{
if (!( tomMatch39__end__32.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch39_39= tomMatch39__end__32.getHeadconcTypeOption() ;
if ( (tomMatch39_39 instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if ( ( tomMatch39_37.getRootSymbolName() == tomMatch39_39.getRootSymbolName() ) ) {
boolean tomMatch39_43= false ;
if ( (tom_tOptions1==tomMatch39_20) ) {
if ( (tom_tOptions2==tomMatch39_20) ) {
tomMatch39_43= true ;
}
}
if (!(tomMatch39_43)) {

//DEBUG System.out.println("In solveConstraints 4a -- tConstraint  = " + `tConstraint);
printError(
tConstraint);
break matchBlockFail;


}

}
}
}
if ( tomMatch39__end__32.isEmptyconcTypeOption() ) {
tomMatch39__end__32=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);
} else {
tomMatch39__end__32= tomMatch39__end__32.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch39__end__32==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) ));
}
}
}
}
if ( tomMatch39__end__26.isEmptyconcTypeOption() ) {
tomMatch39__end__26=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1);
} else {
tomMatch39__end__26= tomMatch39__end__26.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch39__end__26==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) ));
}
}
}
}
}
}
}

}
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;

TomTypeList superTypesT1 = dependencies.get(
 (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() );

{
{
if ( (superTypesT1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (tom_t2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
boolean tomMatch40_9= false ;
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )superTypesT1) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )superTypesT1) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch40__end__5=(( tom.engine.adt.tomtype.types.TomTypeList )superTypesT1);
do {
{
if (!( tomMatch40__end__5.isEmptyconcTomType() )) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_t2)== tomMatch40__end__5.getHeadconcTomType() ) ) {
tomMatch40_9= true ;
}
}
if ( tomMatch40__end__5.isEmptyconcTomType() ) {
tomMatch40__end__5=(( tom.engine.adt.tomtype.types.TomTypeList )superTypesT1);
} else {
tomMatch40__end__5= tomMatch40__end__5.getTailconcTomType() ;
}

}
} while(!( (tomMatch40__end__5==(( tom.engine.adt.tomtype.types.TomTypeList )superTypesT1)) ));
}
if (!(tomMatch40_9)) {

System.out.println("detectFail, superTypesT1 = " + superTypesT1);
printError(
tConstraint);
break matchBlockFail;


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

private TypeConstraintList replaceInSubtypingConstraints(TypeConstraintList
tCList) {
TypeConstraintList replacedtCList = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
TomType mapT1;
TomType mapT2;
for (TypeConstraint tConstraint: tCList.getCollectionconcTypeConstraint()) {

{
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tom_t2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;

mapT1 = substitutions.get(
tom_t1);
mapT2 = substitutions.get(
tom_t2); 
if (mapT1 == null) {
mapT1 = 
tom_t1;
}
if (mapT2 == null) {
mapT2 = 
tom_t2;
}
if (mapT1 != mapT2) {
replacedtCList =

addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(mapT1, mapT2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) ,replacedtCList);
}


}
}

}

}


}
return replacedtCList;
}

/**
* The method <code>solveSubtypingConstraints</code> is generated by a
* strategy which simplifies subtyping constraints replacing them by equations
* or detecting type inconsistency.
* <p>
* PHASE 1: Simplification in equations: 
* tCList = {T1 <: T2, T2 <: T1} U tCList' and Map -->  {T1 = T2} U tCList' and Map
* tCList = {A1 <: A2, A2 <: A1} U tCList' and Map -->  {A1 = A2} U tCList' and Map
* <p>
* PHASE 2: Reduction in closed form:
* tCList = {T1 <: A,A <: T2} U tCList' and Map --> {T1 <: T2} U tCList and Map
* tCList = {A1 <:A,A <: A2} U tCList' and Map --> {A1 <: A2} U tCList and Map
* <p>
* PHASE 3: Garbage collection:
* tCList = {T1 <: T2} U tCList' and Map --> detectFail(T1 <: T2)
* tCList = {A <: T} U tCList' and Map
*   --> {A = T} U tCList' and Map if A is not in Var(tCList')
* tCList = {T <: A} U tCList' and Map
*   --> {A = T} U tCList' and Map if A is not in Var(tCList')
* <p>
* PHASE 4: Reduction in canonical form:
* tCList = {A <: T1,A <:T2} U tCList' and Map 
*   --> {A <: lowerType(T1,T2)} U tCList' and Map
* tCList = {T1 <: A,T2 <: A} U tCList' and Map
*   --> {upperType(T1,T2) <: A} U tCList' and Map
*/

public static class solveSubtypingConstraints extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public solveSubtypingConstraints( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
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
if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
if (!( tomMatch42__end__4.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_14= tomMatch42__end__4.getHeadconcTypeConstraint() ;
if ( (tomMatch42_14 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch42_14.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch42_14.getType2() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42_5= tomMatch42__end__4.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__8=tomMatch42_5;
do {
{
if (!( tomMatch42__end__8.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_17= tomMatch42__end__8.getHeadconcTypeConstraint() ;
if ( (tomMatch42_17 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (tom_t2== tomMatch42_17.getType1() ) ) {
if ( (tom_t1== tomMatch42_17.getType2() ) ) {

System.out.println("\nsolve1: " + 
(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg));
nkt.solveEquationConstraints(
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_t1, tom_t2,  tomMatch42_14.getInfo() ) , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) );
return
nkt.
tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch42__end__4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint(tomMatch42_5,tomMatch42__end__8, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch42__end__8.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));


}
}
}
}
if ( tomMatch42__end__8.isEmptyconcTypeConstraint() ) {
tomMatch42__end__8=tomMatch42_5;
} else {
tomMatch42__end__8= tomMatch42__end__8.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__8==tomMatch42_5) ));
}
}
if ( tomMatch42__end__4.isEmptyconcTypeConstraint() ) {
tomMatch42__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch42__end__4= tomMatch42__end__4.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__25=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
if (!( tomMatch42__end__25.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_35= tomMatch42__end__25.getHeadconcTypeConstraint() ;
if ( (tomMatch42_35 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch42_33= tomMatch42_35.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch42_35.getType1() ;
if ( (tomMatch42_33 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42_26= tomMatch42__end__25.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__29=tomMatch42_26;
do {
{
if (!( tomMatch42__end__29.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_39= tomMatch42__end__29.getHeadconcTypeConstraint() ;
if ( (tomMatch42_39 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (tomMatch42_33== tomMatch42_39.getType1() ) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch42_39.getType2() ;
if ( (tom_tcl instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch42_52= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__43=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl);
do {
{
if (!( tomMatch42__end__43.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_49= tomMatch42__end__43.getHeadconcTypeConstraint() ;
if ( (tomMatch42_49 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (tom_t1== tomMatch42_49.getType1() ) ) {
if ( (tom_t2== tomMatch42_49.getType2() ) ) {
tomMatch42_52= true ;
}
}
}
}
if ( tomMatch42__end__43.isEmptyconcTypeConstraint() ) {
tomMatch42__end__43=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl);
} else {
tomMatch42__end__43= tomMatch42__end__43.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__43==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl)) ));
}
if (!(tomMatch42_52)) {

System.out.println("\nsolve2a: " + 
tom_tcl);
return
nkt.
addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2,  tomMatch42_35.getInfo() ) ,tom_tcl);


}

}
}
}
}
if ( tomMatch42__end__29.isEmptyconcTypeConstraint() ) {
tomMatch42__end__29=tomMatch42_26;
} else {
tomMatch42__end__29= tomMatch42__end__29.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__29==tomMatch42_26) ));
}
}
}
if ( tomMatch42__end__25.isEmptyconcTypeConstraint() ) {
tomMatch42__end__25=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch42__end__25= tomMatch42__end__25.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__25==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__58=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
if (!( tomMatch42__end__58.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_68= tomMatch42__end__58.getHeadconcTypeConstraint() ;
if ( (tomMatch42_68 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch42_68.getType2() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42_59= tomMatch42__end__58.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__62=tomMatch42_59;
do {
{
if (!( tomMatch42__end__62.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_71= tomMatch42__end__62.getHeadconcTypeConstraint() ;
if ( (tomMatch42_71 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch42_70= tomMatch42_71.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch42_71.getType1() ;
if ( (tomMatch42_70 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( ( tomMatch42_68.getType1() ==tomMatch42_70) ) {
if ( (tom_tcl instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch42_85= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__76=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl);
do {
{
if (!( tomMatch42__end__76.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_82= tomMatch42__end__76.getHeadconcTypeConstraint() ;
if ( (tomMatch42_82 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (tom_t1== tomMatch42_82.getType1() ) ) {
if ( (tom_t2== tomMatch42_82.getType2() ) ) {
tomMatch42_85= true ;
}
}
}
}
if ( tomMatch42__end__76.isEmptyconcTypeConstraint() ) {
tomMatch42__end__76=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl);
} else {
tomMatch42__end__76= tomMatch42__end__76.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__76==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl)) ));
}
if (!(tomMatch42_85)) {

System.out.println("\nsolve2b: " + 
tom_tcl);
return
nkt.
addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2,  tomMatch42_68.getInfo() ) ,tom_tcl);


}

}
}
}
}
}
if ( tomMatch42__end__62.isEmptyconcTypeConstraint() ) {
tomMatch42__end__62=tomMatch42_59;
} else {
tomMatch42__end__62= tomMatch42__end__62.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__62==tomMatch42_59) ));
}
}
if ( tomMatch42__end__58.isEmptyconcTypeConstraint() ) {
tomMatch42__end__58=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch42__end__58= tomMatch42__end__58.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__58==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__90=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
if (!( tomMatch42__end__90.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_95= tomMatch42__end__90.getHeadconcTypeConstraint() ;
if ( (tomMatch42_95 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
boolean tomMatch42_99= false ;
if ( ( tomMatch42_95.getType2()  instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
tomMatch42_99= true ;
}
if (!(tomMatch42_99)) {
boolean tomMatch42_98= false ;
if ( ( tomMatch42_95.getType1()  instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
tomMatch42_98= true ;
}
if (!(tomMatch42_98)) {

System.out.println("\nsolve3: " + 
(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg));
nkt.detectFail(
 tomMatch42__end__90.getHeadconcTypeConstraint() );


}

}

}
}
if ( tomMatch42__end__90.isEmptyconcTypeConstraint() ) {
tomMatch42__end__90=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch42__end__90= tomMatch42__end__90.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__90==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__104=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl1=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch42__end__104, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch42__end__104.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_114= tomMatch42__end__104.getHeadconcTypeConstraint() ;
if ( (tomMatch42_114 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch42_111= tomMatch42_114.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch42_112= tomMatch42_114.getType2() ;
if ( (tomMatch42_111 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch42_111;
 tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch42_112;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tom_constraint= tomMatch42__end__104.getHeadconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42_105= tomMatch42__end__104.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__108=tomMatch42_105;
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl2=tom_get_slice_concTypeConstraint(tomMatch42_105,tomMatch42__end__108, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch42__end__108.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_118= tomMatch42__end__108.getHeadconcTypeConstraint() ;
if ( (tomMatch42_118 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch42_117= tomMatch42_118.getType2() ;
if ( (tom_tVar== tomMatch42_118.getType1() ) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch42_117;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl3= tomMatch42__end__108.getTailconcTypeConstraint() ;
boolean tomMatch42_125= false ;
if ( (tomMatch42_112 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_t1==tomMatch42_112) ) {
tomMatch42_125= true ;
}
}
if (!(tomMatch42_125)) {
boolean tomMatch42_124= false ;
if ( (tomMatch42_117 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_t2==tomMatch42_117) ) {
tomMatch42_124= true ;
}
}
if (!(tomMatch42_124)) {

System.out.println("\nsolve6: " + 
tom_constraint+ " and " + 
 tomMatch42__end__108.getHeadconcTypeConstraint() );
TomType lowerType = nkt.
minType(tom_t1,tom_t2);
System.out.println("\nminType(" + 
tom_t1.getTomType() + "," +

tom_t2.getTomType() + ") = " + lowerType);

if (lowerType == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
// TODO fix print (bad message and arguments)
nkt.printError(
tom_constraint);
return 
tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))); 
}

return
nkt.
addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_tVar, lowerType,  tomMatch42_114.getInfo() ) ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));


}

}

}
}
}
if ( tomMatch42__end__108.isEmptyconcTypeConstraint() ) {
tomMatch42__end__108=tomMatch42_105;
} else {
tomMatch42__end__108= tomMatch42__end__108.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__108==tomMatch42_105) ));
}
}
}
if ( tomMatch42__end__104.isEmptyconcTypeConstraint() ) {
tomMatch42__end__104=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch42__end__104= tomMatch42__end__104.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__104==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__130=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl1=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch42__end__130, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch42__end__130.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_140= tomMatch42__end__130.getHeadconcTypeConstraint() ;
if ( (tomMatch42_140 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch42_137= tomMatch42_140.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch42_138= tomMatch42_140.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch42_137;
if ( (tomMatch42_138 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch42_138;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tom_constraint= tomMatch42__end__130.getHeadconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42_131= tomMatch42__end__130.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch42__end__134=tomMatch42_131;
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl2=tom_get_slice_concTypeConstraint(tomMatch42_131,tomMatch42__end__134, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch42__end__134.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch42_144= tomMatch42__end__134.getHeadconcTypeConstraint() ;
if ( (tomMatch42_144 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch42_142= tomMatch42_144.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch42_142;
if ( (tom_tVar== tomMatch42_144.getType2() ) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl3= tomMatch42__end__134.getTailconcTypeConstraint() ;
boolean tomMatch42_151= false ;
if ( (tomMatch42_137 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_t1==tomMatch42_137) ) {
tomMatch42_151= true ;
}
}
if (!(tomMatch42_151)) {
boolean tomMatch42_150= false ;
if ( (tomMatch42_142 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_t2==tomMatch42_142) ) {
tomMatch42_150= true ;
}
}
if (!(tomMatch42_150)) {

System.out.println("\nsolve7: " + 
tom_constraint+ " and " + 
 tomMatch42__end__134.getHeadconcTypeConstraint() );
TomType upperType = nkt.
maxType(tom_t1,tom_t2);
System.out.println("\nmaxType(" + 
tom_t1.getTomType() + "," +

tom_t2.getTomType() + ") = " + upperType);

if (upperType == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
// TODO fix print (bad message and arguments)
nkt.printError(
tom_constraint);
return 
tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))); 
}

return
nkt.
addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(upperType, tom_tVar,  tomMatch42_140.getInfo() ) ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));


}

}

}
}
}
if ( tomMatch42__end__134.isEmptyconcTypeConstraint() ) {
tomMatch42__end__134=tomMatch42_131;
} else {
tomMatch42__end__134= tomMatch42__end__134.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__134==tomMatch42_131) ));
}
}
}
if ( tomMatch42__end__130.isEmptyconcTypeConstraint() ) {
tomMatch42__end__130=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch42__end__130= tomMatch42__end__130.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch42__end__130==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);

System.out.println("\nsolve8: " + 
tom_tcl);
return nkt.enumerateSolutions(
tom_tcl);


}

}


}
return _visit_TypeConstraintList(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_solveSubtypingConstraints( NewKernelTyper  t0) { 
return new solveSubtypingConstraints(t0);
}


private boolean findVar(TomType tVar, TypeConstraintList tCList) {

{
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch43__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
if (!( tomMatch43__end__5.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch43_9= tomMatch43__end__5.getHeadconcTypeConstraint() ;
boolean tomMatch43_11= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch43_8= null ;
if ( (tomMatch43_9 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
{
tomMatch43_11= true ;
tomMatch43_8= tomMatch43_9.getType1() ;

}
} else {
if ( (tomMatch43_9 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
{
tomMatch43_11= true ;
tomMatch43_8= tomMatch43_9.getType1() ;

}
}
}
if (tomMatch43_11) {
if ( (tVar instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( (tomMatch43_8==(( tom.engine.adt.tomtype.types.TomType )tVar)) ) {
return true; 
}
}
}

}
if ( tomMatch43__end__5.isEmptyconcTypeConstraint() ) {
tomMatch43__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch43__end__5= tomMatch43__end__5.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch43__end__5==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}

}
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch43__end__17=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
if (!( tomMatch43__end__17.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch43_21= tomMatch43__end__17.getHeadconcTypeConstraint() ;
boolean tomMatch43_23= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch43_20= null ;
if ( (tomMatch43_21 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
{
tomMatch43_23= true ;
tomMatch43_20= tomMatch43_21.getType2() ;

}
} else {
if ( (tomMatch43_21 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
{
tomMatch43_23= true ;
tomMatch43_20= tomMatch43_21.getType2() ;

}
}
}
if (tomMatch43_23) {
if ( (tVar instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( (tomMatch43_20==(( tom.engine.adt.tomtype.types.TomType )tVar)) ) {
return true; 
}
}
}

}
if ( tomMatch43__end__17.isEmptyconcTypeConstraint() ) {
tomMatch43__end__17=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch43__end__17= tomMatch43__end__17.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch43__end__17==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}

}


}

return false;
}

private TomType minType(TomType t1, TomType t2) {
TomTypeList supTypes1 = dependencies.get(t1);
TomTypeList supTypes2 = dependencies.get(t2);

{
{
if ( (supTypes1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypes1) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypes1) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch44__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )supTypes1);
do {
{
if (!( tomMatch44__end__4.isEmptyconcTomType() )) {
if ( (t2== tomMatch44__end__4.getHeadconcTomType() ) ) {

return t1;

}
}
if ( tomMatch44__end__4.isEmptyconcTomType() ) {
tomMatch44__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )supTypes1);
} else {
tomMatch44__end__4= tomMatch44__end__4.getTailconcTomType() ;
}

}
} while(!( (tomMatch44__end__4==(( tom.engine.adt.tomtype.types.TomTypeList )supTypes1)) ));
}
}

}
{
if ( (supTypes2 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypes2) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypes2) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch44__end__11=(( tom.engine.adt.tomtype.types.TomTypeList )supTypes2);
do {
{
if (!( tomMatch44__end__11.isEmptyconcTomType() )) {
if ( (t1== tomMatch44__end__11.getHeadconcTomType() ) ) {

return t2;

}
}
if ( tomMatch44__end__11.isEmptyconcTomType() ) {
tomMatch44__end__11=(( tom.engine.adt.tomtype.types.TomTypeList )supTypes2);
} else {
tomMatch44__end__11= tomMatch44__end__11.getTailconcTomType() ;
}

}
} while(!( (tomMatch44__end__11==(( tom.engine.adt.tomtype.types.TomTypeList )supTypes2)) ));
}
}

}


}

return 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
}

private TomType maxType(TomType t1, TomType t2) {
TomTypeList supTypes1 = dependencies.get(t1);
TomTypeList supTypes2 = dependencies.get(t2);

{
{
if ( (supTypes1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypes1) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypes1) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch45__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )supTypes1);
do {
{
if (!( tomMatch45__end__4.isEmptyconcTomType() )) {
if ( (t2== tomMatch45__end__4.getHeadconcTomType() ) ) {

return t2;

}
}
if ( tomMatch45__end__4.isEmptyconcTomType() ) {
tomMatch45__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )supTypes1);
} else {
tomMatch45__end__4= tomMatch45__end__4.getTailconcTomType() ;
}

}
} while(!( (tomMatch45__end__4==(( tom.engine.adt.tomtype.types.TomTypeList )supTypes1)) ));
}
}

}
{
if ( (supTypes2 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypes2) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypes2) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch45__end__11=(( tom.engine.adt.tomtype.types.TomTypeList )supTypes2);
do {
{
if (!( tomMatch45__end__11.isEmptyconcTomType() )) {
if ( (t1== tomMatch45__end__11.getHeadconcTomType() ) ) {

return t1;

}
}
if ( tomMatch45__end__11.isEmptyconcTomType() ) {
tomMatch45__end__11=(( tom.engine.adt.tomtype.types.TomTypeList )supTypes2);
} else {
tomMatch45__end__11= tomMatch45__end__11.getTailconcTomType() ;
}

}
} while(!( (tomMatch45__end__11==(( tom.engine.adt.tomtype.types.TomTypeList )supTypes2)) ));
}
}

}
{
if ( (supTypes1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (supTypes2 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
boolean tomMatch45_19= false ;
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypes1) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypes1) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if ( (( tom.engine.adt.tomtype.types.TomTypeList )supTypes1).isEmptyconcTomType() ) {
tomMatch45_19= true ;
}
}
if (!(tomMatch45_19)) {
boolean tomMatch45_18= false ;
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )supTypes2) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )supTypes2) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if ( (( tom.engine.adt.tomtype.types.TomTypeList )supTypes2).isEmptyconcTomType() ) {
tomMatch45_18= true ;
}
}
if (!(tomMatch45_18)) {

System.out.println("supTypes1 = " + supTypes1);
Set<TomType> setSupTypes1 = (Set) supTypes1;
boolean result = setSupTypes1.retainAll((Collection) supTypes2);
int intersectionSize = setSupTypes1.size();
for (TomType currentSupType:setSupTypes1) {
// The size test is enough since tom doesn't accept multiple
// inheritance
if (dependencies.get(currentSupType).length() == (intersectionSize-1)) {
return currentSupType;
}
}

}

}

}
}

}


}

return 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
}

private TypeConstraintList enumerateSolutions(TypeConstraintList tCList) {
matchBlockSolve :
{

{
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_leftTCL=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch46__end__4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch46__end__4.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_9= tomMatch46__end__4.getHeadconcTypeConstraint() ;
if ( (tomMatch46_9 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_7= tomMatch46_9.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch46_8= tomMatch46_9.getType2() ;
if ( (tomMatch46_7 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch46_7;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch46_8;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_rightTCL= tomMatch46__end__4.getTailconcTypeConstraint() ;
boolean tomMatch46_13= false ;
if ( (tomMatch46_8 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch46_8) ) {
tomMatch46_13= true ;
}
}
if (!(tomMatch46_13)) {

System.out.println("\nenumerateSolutions1: " + 
 tomMatch46__end__4.getHeadconcTypeConstraint() );
TypeConstraintList newLeftTCL = 
tom_leftTCL;
TypeConstraintList newRightTCL = 
tom_rightTCL;
if (!
findVar(tom_tVar,tom_append_list_concTypeConstraint(tom_leftTCL,tom_append_list_concTypeConstraint(tom_rightTCL, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) {
// Same code of cases 7 and 8 of solveEquationConstraints
addSubstitution(
tom_tVar,
tom_groundType);
tCList = 
tom_append_list_concTypeConstraint(newLeftTCL,tom_append_list_concTypeConstraint(newRightTCL, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ));
break matchBlockSolve;
}


}

}
}
}
if ( tomMatch46__end__4.isEmptyconcTypeConstraint() ) {
tomMatch46__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch46__end__4= tomMatch46__end__4.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}

}
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__18=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_leftTCL=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch46__end__18, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch46__end__18.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_23= tomMatch46__end__18.getHeadconcTypeConstraint() ;
if ( (tomMatch46_23 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_21= tomMatch46_23.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch46_22= tomMatch46_23.getType2() ;
if ( (tomMatch46_21 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar1=tomMatch46_21;
if ( (tomMatch46_22 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar2=tomMatch46_22;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_rightTCL= tomMatch46__end__18.getTailconcTypeConstraint() ;

System.out.println("\nenumerateSolutions1: " + 
 tomMatch46__end__18.getHeadconcTypeConstraint() );
TypeConstraintList newLeftTCL = 
tom_leftTCL;
TypeConstraintList newRightTCL = 
tom_rightTCL;
if (!
findVar(tom_tVar1,tom_append_list_concTypeConstraint(tom_leftTCL,tom_append_list_concTypeConstraint(tom_rightTCL, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))&&
!
findVar(tom_tVar2,tom_append_list_concTypeConstraint(tom_leftTCL,tom_append_list_concTypeConstraint(tom_rightTCL, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) {
// Same code of cases 7 and 8 of solveEquationConstraints
addSubstitution(
tom_tVar1,
tom_tVar2);
tCList = 
tom_append_list_concTypeConstraint(newLeftTCL,tom_append_list_concTypeConstraint(newRightTCL, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ));
break matchBlockSolve;
}


}
}
}
}
if ( tomMatch46__end__18.isEmptyconcTypeConstraint() ) {
tomMatch46__end__18=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch46__end__18= tomMatch46__end__18.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__18==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}

}
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__30=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_leftTCL=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch46__end__30, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch46__end__30.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_35= tomMatch46__end__30.getHeadconcTypeConstraint() ;
if ( (tomMatch46_35 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_33= tomMatch46_35.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch46_34= tomMatch46_35.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch46_33;
if ( (tomMatch46_34 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch46_34;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_rightTCL= tomMatch46__end__30.getTailconcTypeConstraint() ;
boolean tomMatch46_39= false ;
if ( (tomMatch46_33 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch46_33) ) {
tomMatch46_39= true ;
}
}
if (!(tomMatch46_39)) {

System.out.println("\nenumerateSolutions2: " + 
 tomMatch46__end__30.getHeadconcTypeConstraint() );
TypeConstraintList newLeftTCL = 
tom_leftTCL;
TypeConstraintList newRightTCL = 
tom_rightTCL;
if (!
findVar(tom_tVar,tom_append_list_concTypeConstraint(tom_leftTCL,tom_append_list_concTypeConstraint(tom_rightTCL, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) {
addSubstitution(
tom_tVar,
tom_groundType);
tCList = 
tom_append_list_concTypeConstraint(newLeftTCL,tom_append_list_concTypeConstraint(newRightTCL, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ));
break matchBlockSolve;
}


}

}
}
}
if ( tomMatch46__end__30.isEmptyconcTypeConstraint() ) {
tomMatch46__end__30=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch46__end__30= tomMatch46__end__30.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__30==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}

}
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__44=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
if (!( tomMatch46__end__44.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_53= tomMatch46__end__44.getHeadconcTypeConstraint() ;
if ( (tomMatch46_53 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_51= tomMatch46_53.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch46_52= tomMatch46_53.getType2() ;
if ( (tomMatch46_51 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar1=tomMatch46_51;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch46_52;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46_45= tomMatch46__end__44.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__48=tomMatch46_45;
do {
{
if (!( tomMatch46__end__48.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_58= tomMatch46__end__48.getHeadconcTypeConstraint() ;
if ( (tomMatch46_58 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_56= tomMatch46_58.getType2() ;
if ( (tom_tVar1== tomMatch46_58.getType1() ) ) {
if ( (tomMatch46_56 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
boolean tomMatch46_63= false ;
if ( (tomMatch46_52 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch46_52) ) {
tomMatch46_63= true ;
}
}
if (!(tomMatch46_63)) {

System.out.println("\nenumerateSolutions3a: " + 
 tomMatch46__end__44.getHeadconcTypeConstraint() + " and " + 
 tomMatch46__end__48.getHeadconcTypeConstraint() );
addSubstitution(
tom_tVar1,
tom_groundType);
tCList =

addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_groundType, tomMatch46_56,  tomMatch46_58.getInfo() ) ,tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch46__end__44, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint(tomMatch46_45,tomMatch46__end__48, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch46__end__48.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
break matchBlockSolve;


}

}
}
}
}
if ( tomMatch46__end__48.isEmptyconcTypeConstraint() ) {
tomMatch46__end__48=tomMatch46_45;
} else {
tomMatch46__end__48= tomMatch46__end__48.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__48==tomMatch46_45) ));
}
}
}
if ( tomMatch46__end__44.isEmptyconcTypeConstraint() ) {
tomMatch46__end__44=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch46__end__44= tomMatch46__end__44.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__44==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}

}
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__68=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
if (!( tomMatch46__end__68.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_78= tomMatch46__end__68.getHeadconcTypeConstraint() ;
if ( (tomMatch46_78 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_76= tomMatch46_78.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_tVar1= tomMatch46_78.getType1() ;
if ( (tomMatch46_76 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46_69= tomMatch46__end__68.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__72=tomMatch46_69;
do {
{
if (!( tomMatch46__end__72.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_82= tomMatch46__end__72.getHeadconcTypeConstraint() ;
if ( (tomMatch46_82 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_80= tomMatch46_82.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch46_81= tomMatch46_82.getType2() ;
if ( (tomMatch46_80 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_tVar1==tomMatch46_80) ) {
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch46_81;
boolean tomMatch46_87= false ;
if ( (tomMatch46_81 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch46_81) ) {
tomMatch46_87= true ;
}
}
if (!(tomMatch46_87)) {

System.out.println("\nenumerateSolutions3b: " + 
 tomMatch46__end__68.getHeadconcTypeConstraint() + " and " + 
 tomMatch46__end__72.getHeadconcTypeConstraint() );
addSubstitution(
tom_tVar1,
tom_groundType);
tCList =

addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_groundType, tomMatch46_76,  tomMatch46_78.getInfo() ) ,tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch46__end__68, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint(tomMatch46_69,tomMatch46__end__72, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch46__end__72.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
break matchBlockSolve;


}

}
}
}
}
if ( tomMatch46__end__72.isEmptyconcTypeConstraint() ) {
tomMatch46__end__72=tomMatch46_69;
} else {
tomMatch46__end__72= tomMatch46__end__72.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__72==tomMatch46_69) ));
}
}
}
if ( tomMatch46__end__68.isEmptyconcTypeConstraint() ) {
tomMatch46__end__68=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch46__end__68= tomMatch46__end__68.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__68==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}

}
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__92=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
if (!( tomMatch46__end__92.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_101= tomMatch46__end__92.getHeadconcTypeConstraint() ;
if ( (tomMatch46_101 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_99= tomMatch46_101.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch46_100= tomMatch46_101.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch46_99;
if ( (tomMatch46_100 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar1=tomMatch46_100;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46_93= tomMatch46__end__92.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__96=tomMatch46_93;
do {
{
if (!( tomMatch46__end__96.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_106= tomMatch46__end__96.getHeadconcTypeConstraint() ;
if ( (tomMatch46_106 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_103= tomMatch46_106.getType1() ;
if ( (tomMatch46_103 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_tVar1== tomMatch46_106.getType2() ) ) {
boolean tomMatch46_111= false ;
if ( (tomMatch46_99 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch46_99) ) {
tomMatch46_111= true ;
}
}
if (!(tomMatch46_111)) {

System.out.println("\nenumerateSolutions4a: " + 
 tomMatch46__end__92.getHeadconcTypeConstraint() + " and " + 
 tomMatch46__end__96.getHeadconcTypeConstraint() );
addSubstitution(
tom_tVar1,
tom_groundType);
tCList =

addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tomMatch46_103, tom_groundType,  tomMatch46_106.getInfo() ) ,tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch46__end__92, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint(tomMatch46_93,tomMatch46__end__96, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch46__end__96.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
break matchBlockSolve;


}

}
}
}
}
if ( tomMatch46__end__96.isEmptyconcTypeConstraint() ) {
tomMatch46__end__96=tomMatch46_93;
} else {
tomMatch46__end__96= tomMatch46__end__96.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__96==tomMatch46_93) ));
}
}
}
if ( tomMatch46__end__92.isEmptyconcTypeConstraint() ) {
tomMatch46__end__92=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch46__end__92= tomMatch46__end__92.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__92==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}

}
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__116=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
do {
{
if (!( tomMatch46__end__116.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_126= tomMatch46__end__116.getHeadconcTypeConstraint() ;
if ( (tomMatch46_126 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_123= tomMatch46_126.getType1() ;
if ( (tomMatch46_123 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar1= tomMatch46_126.getType2() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46_117= tomMatch46__end__116.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch46__end__120=tomMatch46_117;
do {
{
if (!( tomMatch46__end__120.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch46_130= tomMatch46__end__120.getHeadconcTypeConstraint() ;
if ( (tomMatch46_130 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch46_128= tomMatch46_130.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch46_129= tomMatch46_130.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch46_128;
if ( (tomMatch46_129 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_tVar1==tomMatch46_129) ) {
boolean tomMatch46_135= false ;
if ( (tomMatch46_128 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch46_128) ) {
tomMatch46_135= true ;
}
}
if (!(tomMatch46_135)) {

System.out.println("\nenumerateSolutions4b: " + 
 tomMatch46__end__116.getHeadconcTypeConstraint() + " and " + 
 tomMatch46__end__120.getHeadconcTypeConstraint() );
addSubstitution(
tom_tVar1,
tom_groundType);
tCList =

addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tomMatch46_123, tom_groundType,  tomMatch46_126.getInfo() ) ,tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch46__end__116, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint(tomMatch46_117,tomMatch46__end__120, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch46__end__120.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
break matchBlockSolve;


}

}
}
}
}
if ( tomMatch46__end__120.isEmptyconcTypeConstraint() ) {
tomMatch46__end__120=tomMatch46_117;
} else {
tomMatch46__end__120= tomMatch46__end__120.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__120==tomMatch46_117) ));
}
}
}
if ( tomMatch46__end__116.isEmptyconcTypeConstraint() ) {
tomMatch46__end__116=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);
} else {
tomMatch46__end__116= tomMatch46__end__116.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch46__end__116==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));
}
}

}


}

}
return tCList;
}

private boolean isTypeVar(TomType type) {

{
{
if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
return true; 
}
}

}

}

return false;
}

private void printError(TypeConstraint tConstraint) {

{
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
boolean tomMatch48_17= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch48_4= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch48_5= null ;
 tom.engine.adt.typeconstraints.types.Info  tomMatch48_6= null ;
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
{
tomMatch48_17= true ;
tomMatch48_4= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
tomMatch48_5= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
tomMatch48_6= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ;

}
} else {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
{
tomMatch48_17= true ;
tomMatch48_4= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
tomMatch48_5= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
tomMatch48_6= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ;

}
}
}
if (tomMatch48_17) {
 tom.engine.adt.tomtype.types.TomType  tom_tType1=tomMatch48_4;
 tom.engine.adt.tomtype.types.TomType  tom_tType2=tomMatch48_5;
 tom.engine.adt.typeconstraints.types.Info  tom_info=tomMatch48_6;
if ( (tom_tType1 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_tType1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( (tom_tType2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_tType2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( (tom_info instanceof tom.engine.adt.typeconstraints.types.Info) ) {
if ( ((( tom.engine.adt.typeconstraints.types.Info )tom_info) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch48_12= (( tom.engine.adt.typeconstraints.types.Info )tom_info).getAstName() ;
if ( (tomMatch48_12 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

Option option = TomBase.findOriginTracking(
 (( tom.engine.adt.typeconstraints.types.Info )tom_info).getOptions() );

{
{
if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

if(lazyType==false) {
TomMessage.error(logger,
 (( tom.engine.adt.tomoption.types.Option )option).getFileName() , 
 (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
TomMessage.incompatibleTypes,
 (( tom.engine.adt.tomtype.types.TomType )tom_tType1).getTomType() ,
 (( tom.engine.adt.tomtype.types.TomType )tom_tType2).getTomType() ,
 tomMatch48_12.getString() ); 
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

}

}

private Code replaceInCode(Code code) {
Code replacedCode = code;
try {
replacedCode = 
tom_make_InnermostId(tom_make_replaceFreshTypeVar(this)).visitLight(code);

tom_make_InnermostId(tom_make_checkTypeOfBQVariables(this)).visitLight(replacedCode);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("replaceInCode: failure on " +
replacedCode);
}
return replacedCode;
}

private void replaceInSymbolTable() {
for(String tomName:symbolTable.keySymbolIterable()) {
//DEBUG System.out.println("replaceInSymboltable() - tomName : " + tomName);
TomSymbol tSymbol = getSymbolFromName(tomName);
//DEBUG System.out.println("replaceInSymboltable() - tSymbol before strategy: "
//DEBUG     + tSymbol);
try {
tSymbol = 
tom_make_InnermostId(tom_make_replaceFreshTypeVar(this)).visitLight(tSymbol);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("replaceInSymbolTable: failure on " +
tSymbol);
}
//DEBUG System.out.println("replaceInSymboltable() - tSymbol after strategy: "
//DEBUG     + tSymbol);
symbolTable.putSymbol(tomName,tSymbol);
}
}


public static class replaceFreshTypeVar extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public replaceFreshTypeVar( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
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
if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {
return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar=(( tom.engine.adt.tomtype.types.TomType )tom__arg);

if (nkt.substitutions.containsKey(
tom_typeVar)) {
return nkt.substitutions.get(
tom_typeVar);
} 


}
}

}

}
return _visit_TomType(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_replaceFreshTypeVar( NewKernelTyper  t0) { 
return new replaceFreshTypeVar(t0);
}
public static class checkTypeOfBQVariables extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public checkTypeOfBQVariables( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
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
 tom.engine.adt.code.types.BQTerm  tomMatch51_2= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() ;
if ( ( (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern()  instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
if ( (tomMatch51_2 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch51_6= tomMatch51_2.getAstName() ;
if ( (tomMatch51_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( ( tomMatch51_2.getAstType()  instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {

Option option = TomBase.findOriginTracking(
 tomMatch51_2.getOptions() );

{
{
if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

TomMessage.error(logger,
 (( tom.engine.adt.tomoption.types.Option )option).getFileName() , 
 (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
TomMessage.cannotGuessMatchType,
 tomMatch51_6.getString() ); 


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
return _visit_Constraint(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_checkTypeOfBQVariables( NewKernelTyper  t0) { 
return new checkTypeOfBQVariables(t0);
}


public void printGeneratedConstraints(TypeConstraintList tCList) {

{
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch53_2= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).isEmptyconcTypeConstraint() ) {
tomMatch53_2= true ;
}
}
if (!(tomMatch53_2)) {

System.out.print("\n------ Type Constraints : \n {");
printEachConstraint(tCList);
System.out.print("}");

}

}

}

}

}

public void printEachConstraint(TypeConstraintList tCList) {

{
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch54_7= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getHeadconcTypeConstraint() ;
if ( (tomMatch54_7 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getTailconcTypeConstraint() ;

printType(
 tomMatch54_7.getType1() );
System.out.print(" = ");
printType(
 tomMatch54_7.getType2() );
if (
tom_tailtCList!= 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
System.out.print(", "); 
printEachConstraint(
tom_tailtCList);
}


}
}
}
}

}
{
if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch54_15= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getHeadconcTypeConstraint() ;
if ( (tomMatch54_15 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getTailconcTypeConstraint() ;

printType(
 tomMatch54_15.getType1() );
System.out.print(" <: ");
printType(
 tomMatch54_15.getType2() );
if (
tom_tailtCList!= 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
System.out.print(", "); 
printEachConstraint(
tom_tailtCList);
}


}
}
}
}

}


}

}

public void printType(TomType type) {
System.out.print(type);
}
} // NewKernelTyper
