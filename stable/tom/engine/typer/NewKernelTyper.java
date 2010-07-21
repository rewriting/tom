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
import java.util.logging.Logger;
import java.lang.Class;
import java.lang.reflect.*;

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
  
  private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_append_list_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList l1,  tom.engine.adt.tomslot.types.PairNameDeclList  l2) {
    if( l1.isEmptyconcPairNameDecl() ) {
      return l2;
    } else if( l2.isEmptyconcPairNameDecl() ) {
      return l1;
    } else if(  l1.getTailconcPairNameDecl() .isEmptyconcPairNameDecl() ) {
      return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,l2) ;
    } else {
      return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,tom_append_list_concPairNameDecl( l1.getTailconcPairNameDecl() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_get_slice_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList  begin,  tom.engine.adt.tomslot.types.PairNameDeclList  end, tom.engine.adt.tomslot.types.PairNameDeclList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcPairNameDecl()  ||  (end== tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( begin.getHeadconcPairNameDecl() ,( tom.engine.adt.tomslot.types.PairNameDeclList )tom_get_slice_concPairNameDecl( begin.getTailconcPairNameDecl() ,end,tail)) ;
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
private TypeConstraintList typeConstraints;
// Set of pairs (freshVar,groundVar)
private HashMap<TomType,TomType> substitutions;

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

{
{
if ( (tType instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {

return TomBase.getSymbolFromType(
 tom.engine.adt.tomtype.types.tomtype.Type.make( (( tom.engine.adt.tomtype.types.TomType )tType).getTomType() ,  (( tom.engine.adt.tomtype.types.TomType )tType).getTlType() ) , symbolTable); 


}
}

}

}

return TomBase.getSymbolFromType(tType,symbolTable); 
}

// TO VERIFY: how to test if a term has an undeclared type? Maybe verifying if
// a term has type Type(name,EmptyTargetLanguage()), where name is not
// UNKNOWN_TYPE. So we verify the subjects of %match but, which BQTerm we need to
// treat?

protected void hasUndeclaredType(BQTerm subject) {
String fileName = currentInputFileName;
int line = 0;
//DEBUG System.out.println("hasUndeclaredType: subject = " + subject);

{
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch284_8= false ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch284_2= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch284_3= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch284_8= true ;
tomMatch284_2= (( tom.engine.adt.code.types.BQTerm )subject).getOptions() ;
tomMatch284_3= (( tom.engine.adt.code.types.BQTerm )subject).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch284_8= true ;
tomMatch284_2= (( tom.engine.adt.code.types.BQTerm )subject).getOptions() ;
tomMatch284_3= (( tom.engine.adt.code.types.BQTerm )subject).getAstType() ;

}
}
}
if (tomMatch284_8) {
 tom.engine.adt.tomtype.types.TomType  tom_aType=tomMatch284_3;
if ( (tom_aType instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_aType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 String  tom_tomType= (( tom.engine.adt.tomtype.types.TomType )tom_aType).getTomType() ;
if (!( symbolTable.TYPE_UNKNOWN.getTomType().equals(tom_tomType) )) {

Option option = TomBase.findOriginTracking(
tomMatch284_2);

{
{
if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {
boolean tomMatch285_2= false ;
if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {
tomMatch285_2= true ;
}
if (!(tomMatch285_2)) {

fileName = option.getFileName();
line = option.getLine();
TomMessage.error(logger, fileName, 
line, TomMessage.unknownSymbol,
tom_tomType);


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


//TO VERIFY : if we can replace the pattern by a "x" and do
//"x.getAstName().getString()"
protected TomType getType(BQTerm bqTerm) {

{
{
if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch286_3= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch286_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch286_3= true ;
tomMatch286_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch286_3= true ;
tomMatch286_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) {
{
tomMatch286_3= true ;
tomMatch286_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;

}
}
}
}
if (tomMatch286_3) {
return 
tomMatch286_1; 

}

}

}
{
if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch286_9= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch286_5= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
{
tomMatch286_9= true ;
tomMatch286_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {
{
tomMatch286_9= true ;
tomMatch286_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {
{
tomMatch286_9= true ;
tomMatch286_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {
{
tomMatch286_9= true ;
tomMatch286_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {
{
tomMatch286_9= true ;
tomMatch286_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {
{
tomMatch286_9= true ;
tomMatch286_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {
{
tomMatch286_9= true ;
tomMatch286_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {
{
tomMatch286_9= true ;
tomMatch286_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {
{
tomMatch286_9= true ;
tomMatch286_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

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
if (tomMatch286_9) {
if ( (tomMatch286_5 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

TomSymbol tSymbol = getSymbolFromName(
 tomMatch286_5.getString() );
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
boolean tomMatch287_6= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch287_4= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch287_6= true ;
tomMatch287_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch287_6= true ;
tomMatch287_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstType() ;

}
}
}
if (tomMatch287_6) {
return 
tomMatch287_4; 

}

}

}
{
if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch287_8= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getNameList() ;
if ( ((tomMatch287_8 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch287_8 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
if (!( tomMatch287_8.isEmptyconcTomName() )) {
 tom.engine.adt.tomname.types.TomName  tomMatch287_14= tomMatch287_8.getHeadconcTomName() ;
if ( (tomMatch287_14 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

TomSymbol tSymbol = getSymbolFromName(
 tomMatch287_14.getString() );
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

//TO VERIFY : getAstName and getOption
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
boolean tomMatch288_7= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch288_5= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch288_4= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch288_7= true ;
tomMatch288_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getOptions() ;
tomMatch288_5= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch288_7= true ;
tomMatch288_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getOptions() ;
tomMatch288_5= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;

}
}
}
if (tomMatch288_7) {

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch288_5, tomMatch288_4) ; 


}

}

}
{
if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch288_10= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getNameList() ;
if ( ((tomMatch288_10 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch288_10 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
if (!( tomMatch288_10.isEmptyconcTomName() )) {

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch288_10.getHeadconcTomName() ,  (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getOptions() ) ; 


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
boolean tomMatch289_4= false ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch289_1= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch289_2= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch289_4= true ;
tomMatch289_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getOptions() ;
tomMatch289_2= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch289_4= true ;
tomMatch289_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getOptions() ;
tomMatch289_2= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
{
tomMatch289_4= true ;
tomMatch289_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getOptions() ;
tomMatch289_2= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;

}
}
}
}
if (tomMatch289_4) {

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch289_2, tomMatch289_1) ; 


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

/*
* pem: use if(...==... && typeConstraints.contains(...))
*/
protected void addConstraint(TypeConstraint tConstraint) {

{
{
if ( (typeConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tom_typeConstraint=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);
boolean tomMatch291_9= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch291__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints);
do {
{
if (!( tomMatch291__end__5.isEmptyconcTypeConstraint() )) {
if ( (tom_typeConstraint== tomMatch291__end__5.getHeadconcTypeConstraint() ) ) {
tomMatch291_9= true ;
}
}
if ( tomMatch291__end__5.isEmptyconcTypeConstraint() ) {
tomMatch291__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints);
} else {
tomMatch291__end__5= tomMatch291__end__5.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch291__end__5==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints)) ));
}
if (!(tomMatch291_9)) {
{
{
if ( (tom_typeConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_typeConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
boolean tomMatch292_7= false ;
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_typeConstraint).getType1()  instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
tomMatch292_7= true ;
}
if (!(tomMatch292_7)) {
boolean tomMatch292_6= false ;
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_typeConstraint).getType2()  instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
tomMatch292_6= true ;
}
if (!(tomMatch292_6)) {

typeConstraints = 
tom_append_list_concTypeConstraint(typeConstraints, tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) );


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

protected void addTomTerm(TomTerm tTerm) {
varPatternList = 
 tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tTerm,tom_append_list_concTomTerm(varPatternList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
}

protected void addTomList(TomList TTList) {
varPatternList = 
tom_append_list_concTomTerm(TTList,tom_append_list_concTomTerm(varPatternList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ));
}

protected void addBQTerm(BQTerm bqTerm) {
varList = 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(bqTerm,tom_append_list_concBQTerm(varList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
}

protected void addBQTermList(BQTermList BQTList) {
varList = 
tom_append_list_concBQTerm(BQTList,tom_append_list_concBQTerm(varList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ));
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
boolean tomMatch293_14= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch293_2= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch293_14= true ;
tomMatch293_2= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch293_14= true ;
tomMatch293_2= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;

}
}
}
if (tomMatch293_14) {
if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch293__end__7=(( tom.engine.adt.code.types.BQTermList )varList);
do {
{
if (!( tomMatch293__end__7.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch293_11= tomMatch293__end__7.getHeadconcBQTerm() ;
boolean tomMatch293_13= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch293_10= null ;
if ( (tomMatch293_11 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch293_13= true ;
tomMatch293_10= tomMatch293_11.getAstName() ;

}
} else {
if ( (tomMatch293_11 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch293_13= true ;
tomMatch293_10= tomMatch293_11.getAstName() ;

}
}
}
if (tomMatch293_13) {
if ( (tomMatch293_2==tomMatch293_10) ) {

varList = 
tom_append_list_concBQTerm(tom_get_slice_concBQTerm((( tom.engine.adt.code.types.BQTermList )varList),tomMatch293__end__7, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ),tom_append_list_concBQTerm( tomMatch293__end__7.getTailconcBQTerm() , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ));


}
}

}
if ( tomMatch293__end__7.isEmptyconcBQTerm() ) {
tomMatch293__end__7=(( tom.engine.adt.code.types.BQTermList )varList);
} else {
tomMatch293__end__7= tomMatch293__end__7.getTailconcBQTerm() ;
}

}
} while(!( (tomMatch293__end__7==(( tom.engine.adt.code.types.BQTermList )varList)) ));
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
* <code>typeConstraints</code> and <code>substitutions</code>
*/
private void init() {
freshTypeVarCounter = limTVarSymbolTable;
varPatternList = 
 tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
varList = 
 tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
typeConstraints = 
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

/*
* Type(name, EmptyTargetLanguageType()) -> Type(name, foundType) if name in TypeTable
* Type(name, EmptyTargetLanguageType()) -> TypeVar(name, Index(i)) if name not in TypeTable
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
public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tom_tomType= (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() ;
if ( ( (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTlType()  instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {

TomType newType = null;
newType = nkt.symbolTable.getType(
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
}
private static  tom.library.sl.Strategy  tom_make_CollectKnownTypes( NewKernelTyper  t0) { 
return new CollectKnownTypes(t0);
}


/**
* The method <code>inferCode</code> starts inference process which takes one
* <code>%match</code> instruction ("InstructionToCode(Match(...))") at a time
* <ul>
*  <li> each "constraintInstructionList" element corresponds to a pair
*  "condition -> action"
*  <li> each pair is traversed in order to generate type constraints
*  <li> the type constraints of "typeConstraints" list are solved at the end
*        of the current <code>%match</code> instruction generating a mapping (a set of
*        substitutions for each type variable)
*  <li> the mapping is applied over the whole <code>%match</code> instruction
*  <li> all lists and hashMaps are reset
* </ul>
* @param code  the tom code to be type inferred
* @return      the tom typed code
*/
public Code inferCode(Code code) {
init();
//DEBUG System.out.println("\n Original Code = \n" + code + '\n');

{
{
if ( (code instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )code) instanceof tom.engine.adt.code.types.code.Tom) ) {
 tom.engine.adt.code.types.CodeList  tomMatch295_1= (( tom.engine.adt.code.types.Code )code).getCodeList() ;
if ( ((tomMatch295_1 instanceof tom.engine.adt.code.types.codelist.ConsconcCode) || (tomMatch295_1 instanceof tom.engine.adt.code.types.codelist.EmptyconcCode)) ) {
if (!( tomMatch295_1.isEmptyconcCode() )) {

CodeList codeResult = 
 tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;
for(Code headCodeList : 
tomMatch295_1.getCollectionconcCode()) {
//DEBUG System.out.println("In inferCode -- for! headCodeList = " +
//DEBUG     headCodeList);
headCodeList = collectKnownTypesFromCode(headCodeList);

{
{
if ( (headCodeList instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )headCodeList) instanceof tom.engine.adt.code.types.code.InstructionToCode) ) {
 tom.engine.adt.tominstruction.types.Instruction  tom_instruction= (( tom.engine.adt.code.types.Code )headCodeList).getAstInstruction() ;

try{

tom_instruction= inferAllTypes(
tom_instruction,getUnknownFreshTypeVar());

headCodeList= 
 tom.engine.adt.code.types.code.InstructionToCode.make(tom_instruction) ;
typeConstraints = 
tom_make_RepeatId(tom_make_solveConstraints(this)).visitLight(typeConstraints);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("inferCode: failure on " +
headCodeList);
} 


}
}

}
{
if ( (headCodeList instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )headCodeList) instanceof tom.engine.adt.code.types.code.DeclarationToCode) ) {
 tom.engine.adt.tomdeclaration.types.Declaration  tom_declaration= (( tom.engine.adt.code.types.Code )headCodeList).getAstDeclaration() ;

try{

tom_declaration= inferAllTypes(
tom_declaration,getUnknownFreshTypeVar());

headCodeList= 
 tom.engine.adt.code.types.code.DeclarationToCode.make(tom_declaration) ;
typeConstraints = 
tom_make_RepeatId(tom_make_solveConstraints(this)).visitLight(typeConstraints);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("inferCode: failure on " +
headCodeList);
} 


}
}

}


}
headCodeList= replaceInCode(
headCodeList);
codeResult = 
tom_append_list_concCode(codeResult, tom.engine.adt.code.types.codelist.ConsconcCode.make(headCodeList, tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) );
replaceInSymbolTable();
init();
}
return 
 tom.engine.adt.code.types.code.Tom.make(codeResult) ;


}
}
}
}

}

}

// If it is a ill-formed code (different from "Tom(...)")
return code;
}

private <T extends tom.library.sl.Visitable> T inferAllTypes(T term, TomType
contextType) {
try {
//DEBUG System.out.println("In inferALLTypes with term = " + term);
return 
tom_make_TopDownStopOnSuccess(tom_make_inferTypes(contextType,this)).visitLight(term); 
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("inferAllTypes: failure on " + term);
}

}

/**
* The class <code>inferTomTerm</code> is generated from a strategy wich
* tries to infer types of all variables
* <p> 
* It starts by searching for a Instruction
* <code>Match(constraintInstructionList,option)</code> and calling
* <code>inferConstraintInstructionList</code> in order to applu rule CT-RULE
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
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {

//DEBUG System.out.println("Instruction with term = " + `match);
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
public  tom.engine.adt.tomsignature.types.TomVisit  visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tom__arg) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) {

//DEBUG System.out.println("TomVisit with term = " + `vTerm);
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
boolean tomMatch299_9= false ;
 tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch299_7= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch299_6= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch299_4= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch299_5= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch299_9= true ;
tomMatch299_4= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ;
tomMatch299_5= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;
tomMatch299_6= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;
tomMatch299_7= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch299_9= true ;
tomMatch299_4= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ;
tomMatch299_5= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;
tomMatch299_6= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;
tomMatch299_7= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;

}
}
}
if (tomMatch299_9) {
 tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList=tomMatch299_7;
 tom.engine.adt.tomterm.types.TomTerm  tom_var=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);

//DEBUG System.out.println("InferTypes:TomTerm var = " + `var);
nkt.checkNonLinearityOfVariables(
tom_var);
nkt.addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tomMatch299_6, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch299_5, tomMatch299_4) ) );  
//DEBUG System.out.println("InferTypes:TomTerm var -- constraint = " +
//DEBUG `aType + " = " + contextType);

{
{
if ( (tom_cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {
if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {
if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).isEmptyconcConstraint() )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch300_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getHeadconcConstraint() ;
if ( (tomMatch300_4 instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch300_4.getVar() ;
if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getTailconcConstraint() .isEmptyconcConstraint() ) {

//DEBUG System.out.println("InferTypes:TomTerm aliasvar -- constraint = " +
//DEBUG   nkt.getType(`boundTerm) + " = " + `contextType);
// TODO : boundTerm.getAstType()
nkt.addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), contextType, nkt.getInfoFromTomTerm(tom_boundTerm)) ); 

}
}
}
}
}

}

}

return 
tom_var;


}

}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch299_12= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ;
 tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ;
if ( ((tomMatch299_12 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch299_12 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
 tom.engine.adt.tomname.types.TomNameList  tom_nList=tomMatch299_12;
if (!( tomMatch299_12.isEmptyconcTomName() )) {
 tom.engine.adt.tomname.types.TomName  tomMatch299_20= tomMatch299_12.getHeadconcTomName() ;
if ( (tomMatch299_20 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.tomslot.types.SlotList  tom_sList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getSlots() ;
 tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;

// In case of a String, tomName is "" for ("a","b")
TomSymbol tSymbol = nkt.getSymbolFromName(
 tomMatch299_20.getString() );


// IF_1
if (tSymbol == null) {
//The contextType is used here, so it must be a ground type, not a
//type variable
//DEBUG System.out.println("visit contextType = " + contextType);
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


{
{
if ( (tom_cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {
if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {
if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).isEmptyconcConstraint() )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch301_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getHeadconcConstraint() ;
if ( (tomMatch301_4 instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch301_4.getVar() ;
if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getTailconcConstraint() .isEmptyconcConstraint() ) {

//DEBUG System.out.println("InferTypes:TomTerm aliasrecordappl -- constraint = " +
//DEBUG     nkt.getType(`boundTerm) + " = " + contextType);
nkt.addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), contextType, nkt.getInfoFromTomTerm(tom_boundTerm)) ); 

}
}
}
}
}

}

}


TomType codomain = contextType;

// IF_3
if (tSymbol == null) {
//DEBUG System.out.println("tSymbol is still null!");
tSymbol = 
 tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;
} else {
// This code can not be moved to IF_2 because tSymbol may be not "null
// since the begginning and then does not enter into neither IF_1 nor
// IF_2
codomain = nkt.getCodomain(tSymbol);
//DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl. codomain = " + codomain);
nkt.addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch299_12.getHeadconcTomName() , tom_optionList) ) );
//DEBUG System.out.println("InferTypes:TomTerm recordappl -- constraint" + codomain + " = " + contextType);
}

SlotList newSList = 
 tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
if (!
tom_sList.isEmptyconcSlot()) {

newSList= nkt.inferSlotList(
tom_sList,tSymbol,codomain);
}
return 
 tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_optionList, tom_nList, newSList, tom_cList) ;


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
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( (((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {
contextType = 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ; 

}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch302_8= false ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch302_4= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch302_5= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch302_6= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch302_8= true ;
tomMatch302_4= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ;
tomMatch302_5= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;
tomMatch302_6= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch302_8= true ;
tomMatch302_4= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ;
tomMatch302_5= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;
tomMatch302_6= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() ;

}
}
}
if (tomMatch302_8) {
 tom.engine.adt.code.types.BQTerm  tom_bqVar=(( tom.engine.adt.code.types.BQTerm )tom__arg);

//DEBUG System.out.println("InferTypes:BQTerm bqVar -- contextType = " +
//DEBUG     contextType);
nkt.checkNonLinearityOfBQVariables(
tom_bqVar);
nkt.addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tomMatch302_6, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch302_5, tomMatch302_4) ) );  
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
 tom.engine.adt.tomname.types.TomName  tomMatch302_11= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;
 tom.engine.adt.code.types.BQTermList  tomMatch302_12= (( tom.engine.adt.code.types.BQTerm )tom__arg).getArgs() ;
if ( (tomMatch302_11 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( "realMake".equals( tomMatch302_11.getString() ) ) {
if ( ((tomMatch302_12 instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || (tomMatch302_12 instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
if (!( tomMatch302_12.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch302_21= tomMatch302_12.getHeadconcBQTerm() ;
if ( (tomMatch302_21 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch302_20= tomMatch302_21.getAstName() ;
if ( (tomMatch302_20 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( "head".equals( tomMatch302_20.getString() ) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch302_18= tomMatch302_12.getTailconcBQTerm() ;
if (!( tomMatch302_18.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch302_26= tomMatch302_18.getHeadconcBQTerm() ;
if ( (tomMatch302_26 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch302_25= tomMatch302_26.getAstName() ;
if ( (tomMatch302_25 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( "tail".equals( tomMatch302_25.getString() ) ) {
if (  tomMatch302_18.getTailconcBQTerm() .isEmptyconcBQTerm() ) {

TomSymbol tSymbol = nkt.getSymbolFromName("realMake");
if (tSymbol == null) {
tSymbol =

 tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tomMatch302_11,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(contextType, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) , contextType) ,  tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ,  (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ) ;
nkt.symbolTable.putSymbol("realMake",tSymbol);
}
return 
(( tom.engine.adt.code.types.BQTerm )tom__arg);


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
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch302_32= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;
 tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ;
if ( (tomMatch302_32 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_name= tomMatch302_32.getString() ;
 tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch302_32;
 tom.engine.adt.code.types.BQTermList  tom_bqTList= (( tom.engine.adt.code.types.BQTerm )tom__arg).getArgs() ;

//DEBUG System.out.println("\n Test pour BQTerm-inferTypes in BQAppl. tomName = " + `name);
TomSymbol tSymbol = nkt.getSymbolFromName(
tom_name);
//DEBUG System.out.println("\n Test pour BQTerm-inferTypes in BQAppl. tSymbol = "+ tSymbol);
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
nkt.addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) );
//DEBUG System.out.println("InferTypes:BQTerm bqappl -- constraint = "
//DEBUG + `codomain + " = " + contextType);
}

BQTermList newBQTList = 
tom_bqTList;
if (!
tom_bqTList.isEmptyconcBQTerm()) {
//DEBUG System.out.println("\n Test pour BQTerm-inferTypes in BQAppl. bqTList = " + `bqTList);
newBQTList = nkt.inferBQTermList(
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
public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));
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
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {
return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));
}
if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));
}
if ( (v instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {
return ((T)visit_TomVisit((( tom.engine.adt.tomsignature.types.TomVisit )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

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
* constraint is added to <code>typeConstraints</code> to ensure that  both
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
boolean tomMatch304_35= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch304_5= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch304_3= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch304_4= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch304_35= true ;
tomMatch304_3= (( tom.engine.adt.tomterm.types.TomTerm )var).getOptions() ;
tomMatch304_4= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;
tomMatch304_5= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch304_35= true ;
tomMatch304_3= (( tom.engine.adt.tomterm.types.TomTerm )var).getOptions() ;
tomMatch304_4= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;
tomMatch304_5= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstType() ;

}
}
}
if (tomMatch304_35) {
 tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch304_3;
 tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch304_4;
 tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch304_5;
if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {
if ( (((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {
 tom.engine.adt.tomterm.types.TomList  tomMatch304__end__10=(( tom.engine.adt.tomterm.types.TomList )varPatternList);
do {
{
if (!( tomMatch304__end__10.isEmptyconcTomTerm() )) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch304_21= tomMatch304__end__10.getHeadconcTomTerm() ;
boolean tomMatch304_32= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch304_19= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch304_20= null ;
if ( (tomMatch304_21 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch304_32= true ;
tomMatch304_19= tomMatch304_21.getAstName() ;
tomMatch304_20= tomMatch304_21.getAstType() ;

}
} else {
if ( (tomMatch304_21 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch304_32= true ;
tomMatch304_19= tomMatch304_21.getAstName() ;
tomMatch304_20= tomMatch304_21.getAstType() ;

}
}
}
if (tomMatch304_32) {
if ( (tom_aName==tomMatch304_19) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch304_20;
boolean tomMatch304_31= false ;
if ( (tom_aType1==tomMatch304_20) ) {
if ( (tom_aType2==tomMatch304_20) ) {
tomMatch304_31= true ;
}
}
if (!(tomMatch304_31)) {

addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) );

}

}
}

}
if ( tomMatch304__end__10.isEmptyconcTomTerm() ) {
tomMatch304__end__10=(( tom.engine.adt.tomterm.types.TomList )varPatternList);
} else {
tomMatch304__end__10= tomMatch304__end__10.getTailconcTomTerm() ;
}

}
} while(!( (tomMatch304__end__10==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));
}
}
if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch304__end__16=(( tom.engine.adt.code.types.BQTermList )varList);
do {
{
if (!( tomMatch304__end__16.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch304_24= tomMatch304__end__16.getHeadconcBQTerm() ;
boolean tomMatch304_34= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch304_22= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch304_23= null ;
if ( (tomMatch304_24 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch304_34= true ;
tomMatch304_22= tomMatch304_24.getAstName() ;
tomMatch304_23= tomMatch304_24.getAstType() ;

}
} else {
if ( (tomMatch304_24 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch304_34= true ;
tomMatch304_22= tomMatch304_24.getAstName() ;
tomMatch304_23= tomMatch304_24.getAstType() ;

}
}
}
if (tomMatch304_34) {
if ( (tom_aName==tomMatch304_22) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch304_23;
boolean tomMatch304_33= false ;
if ( (tom_aType1==tomMatch304_23) ) {
if ( (tom_aType2==tomMatch304_23) ) {
tomMatch304_33= true ;
}
}
if (!(tomMatch304_33)) {

addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) );

}

}
}

}
if ( tomMatch304__end__16.isEmptyconcBQTerm() ) {
tomMatch304__end__16=(( tom.engine.adt.code.types.BQTermList )varList);
} else {
tomMatch304__end__16= tomMatch304__end__16.getTailconcBQTerm() ;
}

}
} while(!( (tomMatch304__end__16==(( tom.engine.adt.code.types.BQTermList )varList)) ));
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
* constraint is added to <code>typeConstraints</code> to ensure that  both
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
boolean tomMatch305_35= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch305_5= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch305_4= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch305_3= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch305_35= true ;
tomMatch305_3= (( tom.engine.adt.code.types.BQTerm )bqvar).getOptions() ;
tomMatch305_4= (( tom.engine.adt.code.types.BQTerm )bqvar).getAstName() ;
tomMatch305_5= (( tom.engine.adt.code.types.BQTerm )bqvar).getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch305_35= true ;
tomMatch305_3= (( tom.engine.adt.code.types.BQTerm )bqvar).getOptions() ;
tomMatch305_4= (( tom.engine.adt.code.types.BQTerm )bqvar).getAstName() ;
tomMatch305_5= (( tom.engine.adt.code.types.BQTerm )bqvar).getAstType() ;

}
}
}
if (tomMatch305_35) {
 tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch305_3;
 tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch305_4;
 tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch305_5;
if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch305__end__10=(( tom.engine.adt.code.types.BQTermList )varList);
do {
{
if (!( tomMatch305__end__10.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch305_21= tomMatch305__end__10.getHeadconcBQTerm() ;
boolean tomMatch305_32= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch305_19= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch305_20= null ;
if ( (tomMatch305_21 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch305_32= true ;
tomMatch305_19= tomMatch305_21.getAstName() ;
tomMatch305_20= tomMatch305_21.getAstType() ;

}
} else {
if ( (tomMatch305_21 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch305_32= true ;
tomMatch305_19= tomMatch305_21.getAstName() ;
tomMatch305_20= tomMatch305_21.getAstType() ;

}
}
}
if (tomMatch305_32) {
if ( (tom_aName==tomMatch305_19) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch305_20;
boolean tomMatch305_31= false ;
if ( (tom_aType1==tomMatch305_20) ) {
if ( (tom_aType2==tomMatch305_20) ) {
tomMatch305_31= true ;
}
}
if (!(tomMatch305_31)) {

addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ); 

}

}
}

}
if ( tomMatch305__end__10.isEmptyconcBQTerm() ) {
tomMatch305__end__10=(( tom.engine.adt.code.types.BQTermList )varList);
} else {
tomMatch305__end__10= tomMatch305__end__10.getTailconcBQTerm() ;
}

}
} while(!( (tomMatch305__end__10==(( tom.engine.adt.code.types.BQTermList )varList)) ));
}
}
if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {
if ( (((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {
 tom.engine.adt.tomterm.types.TomList  tomMatch305__end__16=(( tom.engine.adt.tomterm.types.TomList )varPatternList);
do {
{
if (!( tomMatch305__end__16.isEmptyconcTomTerm() )) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch305_24= tomMatch305__end__16.getHeadconcTomTerm() ;
boolean tomMatch305_34= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch305_22= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch305_23= null ;
if ( (tomMatch305_24 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch305_34= true ;
tomMatch305_22= tomMatch305_24.getAstName() ;
tomMatch305_23= tomMatch305_24.getAstType() ;

}
} else {
if ( (tomMatch305_24 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch305_34= true ;
tomMatch305_22= tomMatch305_24.getAstName() ;
tomMatch305_23= tomMatch305_24.getAstType() ;

}
}
}
if (tomMatch305_34) {
if ( (tom_aName==tomMatch305_22) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch305_23;
boolean tomMatch305_33= false ;
if ( (tom_aType1==tomMatch305_23) ) {
if ( (tom_aType2==tomMatch305_23) ) {
tomMatch305_33= true ;
}
}
if (!(tomMatch305_33)) {

addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ); 

}

}
}

}
if ( tomMatch305__end__16.isEmptyconcTomTerm() ) {
tomMatch305__end__16=(( tom.engine.adt.tomterm.types.TomList )varPatternList);
} else {
tomMatch305__end__16= tomMatch305__end__16.getTailconcTomTerm() ;
}

}
} while(!( (tomMatch305__end__16==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));
}
}

}

}

}

}

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
//DEBUG System.out.println("\n Test pour inferConstraintInstructionList -- ligne 1.");

{
{
if ( (ciList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {
if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) {
if ( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList).isEmptyconcConstraintInstruction() ) {
return 
ciList; 

}
}
}

}
{
if ( (ciList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {
if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) {
if (!( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList).isEmptyconcConstraintInstruction() )) {
 tom.engine.adt.tominstruction.types.ConstraintInstruction  tomMatch306_9= (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList).getHeadconcConstraintInstruction() ;
if ( (tomMatch306_9 instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tom_constraint= tomMatch306_9.getConstraint() ;
 tom.engine.adt.tominstruction.types.Instruction  tom_action= tomMatch306_9.getAction() ;
 tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_tailCIList= (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList).getTailconcConstraintInstruction() ;

try {
//DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 4.");
//DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 2.");
//DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 3.");
//DEBUG System.out.println("\n varPatternList = " + `varPatternList);
//DEBUG System.out.println("\n varList = " + `varList);
//DEBUG System.out.println("\n Constraints = " + typeConstraints);
TomList TTList = varPatternList;

tom_make_TopDownCollect(tom_make_CollectVars(this)).visitLight(
tom_constraint);
//DEBUG System.out.println("\n varPatternList apres = " + `varPatternList);
//DEBUG System.out.println("\n varList apres = " + `varList);

tom_constraint= inferConstraint(
tom_constraint);

tom_action= 
inferAllTypes(tom_action, tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
varPatternList = TTList;
//DEBUG System.out.println("\n varList apr?s = " + `varList);

tom_tailCIList= inferConstraintInstructionList(
tom_tailCIList);
return

 tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(tom_constraint, tom_action,  tomMatch306_9.getOptions() ) ,tom_append_list_concConstraintInstruction(tom_tailCIList, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("inferConstraintInstructionList: failure on " + 
 (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList).getHeadconcConstraintInstruction() );
}


}
}
}
}

}


}

throw new TomRuntimeException("inferConstraintInstructionList: failure on "
+ 
ciList);
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
public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch307_2= false ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch307_2= true ;
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch307_2= true ;
}
}
if (tomMatch307_2) {
nkt.addTomTerm(
(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)); 

}

}

}

}
return _visit_TomTerm(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch308_2= false ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
tomMatch308_2= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
tomMatch308_2= true ;
}
}
if (tomMatch308_2) {
nkt.addBQTerm(
(( tom.engine.adt.code.types.BQTerm )tom__arg)); 

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
public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));
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
if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

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
addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tPattern, tSubject, getInfoFromTomTerm(tom_pattern)) );

tom_pattern= 
inferAllTypes(tom_pattern,tPattern);

tom_subject= 
inferAllTypes(tom_subject,tSubject);
hasUndeclaredType(
tom_subject);
return 
 tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_pattern, tom_subject) ;


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
addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tLeft, tRight, getInfoFromBQTerm(tom_left)) );

tom_left= inferAllTypes(
tom_left,tLeft);

tom_right= inferAllTypes(
tom_right,tRight);
hasUndeclaredType(
tom_left);
hasUndeclaredType(
tom_right);
return 
 tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(tom_left, tom_right,  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getType() ) ;


}
}

}
{
if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyAndConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {

ConstraintList cList = 
 tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadAndConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
Constraint cArg;
Constraint newAConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;
while(!cList.isEmptyconcConstraint()) {
cArg = inferConstraint(cList.getHeadconcConstraint());
newAConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(newAConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ;
cList = cList.getTailconcConstraint();
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
Constraint cArg;
Constraint newOConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ;
while(!cList.isEmptyconcConstraint()) {
cArg = inferConstraint(cList.getHeadconcConstraint());
newOConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(newOConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) ) ;
cList = cList.getTailconcConstraint();
}
return newOConstraint;


}
}
}

}


}

return constraint;
}

private SlotList inferSlotList(SlotList sList, TomSymbol tSymbol, TomType
contextType) {

{
{
if ( (sList instanceof tom.engine.adt.tomslot.types.SlotList) ) {
if ( (((( tom.engine.adt.tomslot.types.SlotList )sList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )sList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {
if ( (( tom.engine.adt.tomslot.types.SlotList )sList).isEmptyconcSlot() ) {
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
return 
sList; 

}
}
}
}
}

}
{
if ( (sList instanceof tom.engine.adt.tomslot.types.SlotList) ) {
if ( (((( tom.engine.adt.tomslot.types.SlotList )sList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )sList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {
if ( (( tom.engine.adt.tomslot.types.SlotList )sList).isEmptyconcSlot() ) {
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {
return 
sList; 

}
}
}
}
}

}
{
if ( (sList instanceof tom.engine.adt.tomslot.types.SlotList) ) {
if ( (((( tom.engine.adt.tomslot.types.SlotList )sList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )sList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {
if (!( (( tom.engine.adt.tomslot.types.SlotList )sList).isEmptyconcSlot() )) {
 tom.engine.adt.tomslot.types.Slot  tomMatch310_16= (( tom.engine.adt.tomslot.types.SlotList )sList).getHeadconcSlot() ;
if ( (tomMatch310_16 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_tTerm= tomMatch310_16.getAppl() ;
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

//DEBUG System.out.println("InferSlotList -- sList =" + sList);
//DEBUG System.out.println("InferSlotList -- tSymbol =" + tSymbol);
/*
* if the top symbol is unknown, the subterms
* are typed in an empty context
*/
TomType argType = contextType;
TomSymbol argSymb = getSymbolFromTerm(
tom_tTerm);
if(!(TomBase.isListOperator(
argSymb) || TomBase.isArrayOperator(
argSymb))) {

{
{
if ( (tom_tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch311_2= false ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch311_2= true ;
}
if (!(tomMatch311_2)) {

//DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
argType = getUnknownFreshTypeVar();

}

}

}

}

}

tom_tTerm= 
inferAllTypes(tom_tTerm,argType);
SlotList newTail = 
inferSlotList( (( tom.engine.adt.tomslot.types.SlotList )sList).getTailconcSlot() ,tSymbol,contextType);
return 
 tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tomMatch310_16.getSlotName() , tom_tTerm) ,tom_append_list_concSlot(newTail, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;


}
}
}
}
}
}

}
{
if ( (sList instanceof tom.engine.adt.tomslot.types.SlotList) ) {
if ( (((( tom.engine.adt.tomslot.types.SlotList )sList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )sList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {
if (!( (( tom.engine.adt.tomslot.types.SlotList )sList).isEmptyconcSlot() )) {
 tom.engine.adt.tomslot.types.Slot  tomMatch310_36= (( tom.engine.adt.tomslot.types.SlotList )sList).getHeadconcSlot() ;
if ( (tomMatch310_36 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_tTerm= tomMatch310_36.getAppl() ;
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch310_20= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;
 tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;
if ( (tomMatch310_20 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch310_22= tomMatch310_20.getDomain() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch310_23= tomMatch310_20.getCodomain() ;
if ( ((tomMatch310_22 instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || (tomMatch310_22 instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if (!( tomMatch310_22.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch310_22.getHeadconcTomType() ;
if ( (tomMatch310_23 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

TomType argType = contextType;
// In case of a list
if(TomBase.isListOperator(
tSymbol) || TomBase.isArrayOperator(
tSymbol)) {
TomSymbol argSymb = getSymbolFromTerm(
tom_tTerm);
TomTerm newTerm = 
tom_tTerm;
if(!(TomBase.isListOperator(
argSymb) || TomBase.isArrayOperator(
argSymb))) {

{
{
if ( (tom_tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {

/**
* Continuation of CT-STAR rule (applying to premises):
* IF found "l(e1,...,en,x*):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "x", where "x" represents a list with
*      head symbol "l"
*/
argType = 
 tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol.make( tomMatch310_23.getTomType() ,  tomMatch310_23.getTlType() , tom_symName) ;


}
}

}
{
if ( (tom_tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch312_4= false ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch312_4= true ;
}
if (!(tomMatch312_4)) {

//DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
/**
* Continuation of CT-ELEM rule (applying to premises which are
* not lists):
* IF found "l(e1,...en,e):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "A = T" for the last
*      argument, where "A" is a fresh type variable  and
*      "e" does not represent a list with head symbol "l"
*/
argType = getUnknownFreshTypeVar();
//DEBUG System.out.println("inferSlotList: !VariableStar -- constraint "
//DEBUG     + `headTTList + " = " + argType);

//TODO create a method getAstName which takes an AstName of a
//RecordAppl
addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(argType, tom_headTTList, getInfoFromTomTerm(tom_tTerm)) );


}

}

}


}

} else if (
tom_symName!= argSymb.getAstName()) {
// TODO: improve this code! It is like CT-ELEM
/*
* A list with a sublist whose constructor is different
* e.g. 
* A = ListA(A*) and B = ListB(A*) | b()
* ListB(b(),ListA(a()),b())
*/
argType = getUnknownFreshTypeVar();
//DEBUG System.out.println("inferSlotList: symName != argSymbName -- constraint "
//DEBUG     + `headTTList + " = " + argType);
addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(argType, tom_headTTList, getInfoFromTomTerm(tom_tTerm)) );
}

/**
* Continuation of CT-MERGE rule (applying to premises):
* IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e", where "e" represents a list with
*      head symbol "l"
*/

newTerm= 
inferAllTypes(newTerm,argType);
SlotList newTail = 
inferSlotList( (( tom.engine.adt.tomslot.types.SlotList )sList).getTailconcSlot() ,tSymbol,contextType);
return 
 tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tomMatch310_36.getSlotName() , newTerm) ,tom_append_list_concSlot(newTail, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
} else {
// In case of a function
TomTerm argTerm;
TomName argName;
SlotList newSList = 
 tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
/**
* Continuation of CT-FUN rule (applying to premises):
* IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
* THEN infers type of arguments and adds a type constraint "Ai =
*      Ti" for each argument, where "Ai" is a fresh type variable
*/
for(Slot arg : 
sList.getCollectionconcSlot()) {
argTerm = arg.getAppl();
argName = arg.getSlotName();
//DEBUG System.out.println("InferSlotList CT-FUN -- slotappl in for = " +
//DEBUG     `argTerm);
argType = TomBase.getSlotType(tSymbol,argName);
addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(getUnknownFreshTypeVar(), argType, getInfoFromTomTerm(argTerm)) );

argTerm= 
inferAllTypes(argTerm,argType);
newSList = 
tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(argName, argTerm) , tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) );
//DEBUG System.out.println("InferSlotList CT-FUN -- end of for with slotappl = " + `argTerm);
}
return newSList;
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

throw new TomRuntimeException("inferSlotList: failure on " + 
sList);
}

private BQTermList inferBQTermList(BQTermList bqTList, TomSymbol tSymbol, TomType
contextType) {
//DEBUG System.out.println("begin of InferBQTermList -- tSymbol'" + tSymbol +
//DEBUG    "'");

{
{
if ( (bqTList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
if ( (( tom.engine.adt.code.types.BQTermList )bqTList).isEmptyconcBQTerm() ) {
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
return bqTList; 
}
}
}
}
}

}
{
if ( (bqTList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
if (!( (( tom.engine.adt.code.types.BQTermList )bqTList).isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch313_15= (( tom.engine.adt.code.types.BQTermList )bqTList).getHeadconcBQTerm() ;
if ( (tomMatch313_15 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch313_14= tomMatch313_15.getAstName() ;
if ( (tomMatch313_14 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( "head".equals( tomMatch313_14.getString() ) ) {
 tom.engine.adt.code.types.BQTerm  tom_headBQTerm= (( tom.engine.adt.code.types.BQTermList )bqTList).getHeadconcBQTerm() ;
 tom.engine.adt.code.types.BQTermList  tomMatch313_12= (( tom.engine.adt.code.types.BQTermList )bqTList).getTailconcBQTerm() ;
if (!( tomMatch313_12.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch313_20= tomMatch313_12.getHeadconcBQTerm() ;
if ( (tomMatch313_20 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch313_19= tomMatch313_20.getAstName() ;
if ( (tomMatch313_19 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( "tail".equals( tomMatch313_19.getString() ) ) {
 tom.engine.adt.code.types.BQTerm  tom_tailBQTerm= tomMatch313_12.getHeadconcBQTerm() ;
if (  tomMatch313_12.getTailconcBQTerm() .isEmptyconcBQTerm() ) {
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch313_6= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;
if ( (tomMatch313_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( "realMake".equals( tomMatch313_6.getString() ) ) {

//DEBUG System.out.println("What???? headBQTerm = " + `headBQTerm);

tom_headBQTerm= inferAllTypes(
tom_headBQTerm,contextType);
//DEBUG System.out.println("What???? tailBQTerm = " + `tailBQTerm);

tom_tailBQTerm= inferAllTypes(
tom_tailBQTerm,contextType);
return 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tom_headBQTerm, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tom_tailBQTerm, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ;


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
{
if ( (bqTList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
if (!( (( tom.engine.adt.code.types.BQTermList )bqTList).isEmptyconcBQTerm() )) {
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

/*
* if the top symbol is unknown, the subterms
* are typed in an empty context
*/
BQTermList newBQTList = 
 tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
for(BQTerm arg : 
bqTList.getCollectionconcBQTerm()) {
//DEBUG System.out.println("InferBQTermList will call inferAllTypes with = "
//DEBUG     + `arg);
arg = 
inferAllTypes(arg, tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
newBQTList = 
tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(arg, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
}
return newBQTList;


}
}
}
}
}

}
{
if ( (bqTList instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
if (!( (( tom.engine.adt.code.types.BQTermList )bqTList).isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tom_bqTerm= (( tom.engine.adt.code.types.BQTermList )bqTList).getHeadconcBQTerm() ;
if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch313_33= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;
 tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;
if ( (tomMatch313_33 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch313_35= tomMatch313_33.getDomain() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch313_36= tomMatch313_33.getCodomain() ;
if ( ((tomMatch313_35 instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || (tomMatch313_35 instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if (!( tomMatch313_35.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch313_35.getHeadconcTomType() ;
if ( (tomMatch313_36 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

TomType argType = contextType;
// In case of a list
if(TomBase.isListOperator(
tSymbol) || TomBase.isArrayOperator(
tSymbol)) {
TomSymbol argSymb = getSymbolFromTerm(
tom_bqTerm);
BQTerm newTerm = 
tom_bqTerm;
//DEBUG System.out.println("InferBQTermList -- bqTerm= " + `bqTerm);
//DEBUG System.out.println("\n\n" + `bqTerm + " is a list? " +
//DEBUG     TomBase.isListOperator(`argSymb) + " and " +
//DEBUG     TomBase.isArrayOperator(`argSymb) + '\n');
if(!(TomBase.isListOperator(
argSymb) || TomBase.isArrayOperator(
argSymb))) {

{
{
if ( (tom_bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tom_bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {

argType = 
 tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol.make( tomMatch313_36.getTomType() ,  tomMatch313_36.getTlType() , tom_symName) ;


}
}

}
{
if ( (tom_bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch314_4= false ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom_bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
tomMatch314_4= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom_bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
tomMatch314_4= true ;
}
}
if (tomMatch314_4) {

//DEBUG System.out.println("inferBQTermList: bqTerm = " + `bqTerm);
argType = getUnknownFreshTypeVar();
//DEBUG System.out.println("inferBQTermList: !BQVariableStar -- constraint "
//DEBUG     + argType + " = " + `headTTList);
addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(argType, tom_headTTList, getInfoFromBQTerm(tom_bqTerm)) );
//addConstraint(`Equation(getUnknownFreshTypeVar(),argType));


}

}

}


}

} else if (
tom_symName!= argSymb.getAstName()) {
// TODO: improve this code! It is like CT-ELEM
/*
* A list with a sublist whose constructor is different
* e.g. 
* A = ListA(A*) and B = ListB(A*) | b()
* ListB(b(),ListA(a()),b())
*/
argType = getUnknownFreshTypeVar();
//DEBUG System.out.println("inferBQTermList: symName != argSymb.getAstName() -- constraint "
//DEBUG     + argType + " = " + `headTTList);
addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(argType, tom_headTTList, getInfoFromBQTerm(tom_bqTerm)) );
}

newTerm= 
inferAllTypes(newTerm,argType);
BQTermList newTail = 
inferBQTermList( (( tom.engine.adt.code.types.BQTermList )bqTList).getTailconcBQTerm() ,tSymbol,contextType);
return 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(newTerm,tom_append_list_concBQTerm(newTail, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
} else {
// In case of a function
BQTermList newBQTList = 
 tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
TomTypeList symDomain = 
tomMatch313_35;
for(BQTerm arg : 
bqTList.getCollectionconcBQTerm()) {
argType = symDomain.getHeadconcTomType();
addConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(getUnknownFreshTypeVar(), argType, getInfoFromBQTerm(arg)) );
arg = 
inferAllTypes(arg,argType);
newBQTList = 
tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(arg, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
symDomain = symDomain.getTailconcTomType();
}
return newBQTList;
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

throw new TomRuntimeException("inferBQTermList: failure on " + 
bqTList);
}

/**
* The class <code>solveConstraints</code> is generated from a strategy wich
* tries to solve all type constraints collected during the inference
* <p> 
* There exists 3 kinds of types : variable types Ai, ground types Ti and
* ground types Ti^c which are decorated with a given symbol c. Since a type
* constraints is a pair (type1,type2) representing an equation relation
* between two types, them the set of all possibilities of arrangement between
* types is a sequence with repetition. Then, we have 9 possible case (since
* 3^2 = 9).
* <p>
* CASE 1: Equation(T1 = T2) U TCList and Map
*  a) --> Fail if T1 is different from T2
*  b) --> Nothing if T1 is equals to T2
* <p>
* CASE 2: Equation(T1 = T2^c) U TCList and Map
*  a) --> Fail if T1 is different from T2
*  b) --> Nothing if T1 is equals to T2
* <p>
* CASE 3: Equation(T1^c = T2) U TCList and Map
*  a) --> Fail if T1 is different from T2
*  b) --> Nothing if T1 is equals to T2
* <p>
* CASE 4: Equation(T1^a = T2^b) U TCList and Map
*  a) --> Fail if T1 is different from T2 and/or "a" is different from "b"
*  b) --> Nothing if T1 is equals to T2
* <p>
* CASE 5: Equation(T = A) U TCList and Map 
*  --> Equation(T = T) U [A/T]TCList and [A/T]Map
* <p>
* CASE 6: Equation(T^c = A) U TCList and Map 
*  --> Equation(T^c = T^c) U [A/T^c]TCList and [A/T^c]Map
* <p>
* CASE 7: Equation(A = T) U TCList and Map 
*  --> Equation(T = T) U [A/T]TCList and [A/T]Map
* <p>
* CASE 8: Equation(A = T^c) U TCList and Map 
*  --> Equation(T^c = T^c) U [A/T^c]TCList and [A/T^c]Map
* <p>
* CASE 9: Equation(A1 = A2) U TCList and Map 
*  --> Nothing 
*/   

public static class solveConstraints extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public solveConstraints( NewKernelTyper  nkt) {
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
public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch315__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
if (!( tomMatch315__end__4.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch315_10= tomMatch315__end__4.getHeadconcTypeConstraint() ;
if ( (tomMatch315_10 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch315_7= tomMatch315_10.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch315_8= tomMatch315_10.getType2() ;
boolean tomMatch315_18= false ;
 String  tomMatch315_11= "" ;
if ( (tomMatch315_7 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
{
tomMatch315_18= true ;
tomMatch315_11= tomMatch315_7.getTomType() ;

}
} else {
if ( (tomMatch315_7 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {
{
tomMatch315_18= true ;
tomMatch315_11= tomMatch315_7.getTomType() ;

}
}
}
if (tomMatch315_18) {
 String  tom_tName1=tomMatch315_11;
if ( (tomMatch315_8 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tomMatch315_13= tomMatch315_8.getTomType() ;
 String  tom_tName2=tomMatch315_13;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tom_tc= tomMatch315__end__4.getHeadconcTypeConstraint() ;
boolean tomMatch315_17= false ;
if ( tom_tName1.equals(tomMatch315_13) ) {
if ( tom_tName2.equals(tomMatch315_13) ) {
tomMatch315_17= true ;
}
}
if (!(tomMatch315_17)) {
if (!( "unknown type".equals(tom_tName1) )) {
if (!( "unknown type".equals(tom_tName2) )) {

//DEBUG System.out.println("In solveConstraints 1a/3a -- tc = " + `tc);
nkt.printError(
tom_tc);
return 
tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch315__end__4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ), tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tom_tc,tom_append_list_concTypeConstraint( tomMatch315__end__4.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) );


}
}
}

}
}

}
}
if ( tomMatch315__end__4.isEmptyconcTypeConstraint() ) {
tomMatch315__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch315__end__4= tomMatch315__end__4.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch315__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch315__end__23=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
if (!( tomMatch315__end__23.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch315_29= tomMatch315__end__23.getHeadconcTypeConstraint() ;
if ( (tomMatch315_29 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch315_26= tomMatch315_29.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch315_27= tomMatch315_29.getType2() ;
if ( (tomMatch315_26 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tom_tName1= tomMatch315_26.getTomType() ;
if ( (tomMatch315_27 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {
 String  tomMatch315_32= tomMatch315_27.getTomType() ;
 String  tom_tName2=tomMatch315_32;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tom_tc= tomMatch315__end__23.getHeadconcTypeConstraint() ;
boolean tomMatch315_36= false ;
if ( tom_tName1.equals(tomMatch315_32) ) {
if ( tom_tName2.equals(tomMatch315_32) ) {
tomMatch315_36= true ;
}
}
if (!(tomMatch315_36)) {
if (!( "unknown type".equals(tom_tName1) )) {
if (!( "unknown type".equals(tom_tName2) )) {

//DEBUG System.out.println("In solveConstraints 2a -- tc = " + `tc);
nkt.printError(
tom_tc);
return 
tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch315__end__23, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ), tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tom_tc,tom_append_list_concTypeConstraint( tomMatch315__end__23.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) );


}
}
}

}
}
}
}
if ( tomMatch315__end__23.isEmptyconcTypeConstraint() ) {
tomMatch315__end__23=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch315__end__23= tomMatch315__end__23.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch315__end__23==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch315__end__41=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
if (!( tomMatch315__end__41.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch315_47= tomMatch315__end__41.getHeadconcTypeConstraint() ;
if ( (tomMatch315_47 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch315_44= tomMatch315_47.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch315_45= tomMatch315_47.getType2() ;
if ( (tomMatch315_44 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {
if ( (tomMatch315_45 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tom_tc= tomMatch315__end__41.getHeadconcTypeConstraint() ;
if (!( (tomMatch315_45==tomMatch315_44) )) {

//DEBUG System.out.println("In solveConstraints 4a -- tc = " + `tc);
nkt.printError(
tom_tc);
return 
tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch315__end__41, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ), tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tom_tc,tom_append_list_concTypeConstraint( tomMatch315__end__41.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) );


}
}
}
}
}
if ( tomMatch315__end__41.isEmptyconcTypeConstraint() ) {
tomMatch315__end__41=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch315__end__41= tomMatch315__end__41.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch315__end__41==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch315__end__60=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_leftTCList=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch315__end__60, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch315__end__60.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch315_66= tomMatch315__end__60.getHeadconcTypeConstraint() ;
if ( (tomMatch315_66 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch315_63= tomMatch315_66.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch315_64= tomMatch315_66.getType2() ;
if ( (tomMatch315_63 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch315_63;
 tom.engine.adt.tomtype.types.TomType  tom_type=tomMatch315_64;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_rightTCList= tomMatch315__end__60.getTailconcTypeConstraint() ;
boolean tomMatch315_72= false ;
if ( (tom_typeVar==tomMatch315_64) ) {
if ( (tom_type==tomMatch315_64) ) {
tomMatch315_72= true ;
}
}
if (!(tomMatch315_72)) {

nkt.substitutions.put(
tom_typeVar,
tom_type);
//DEBUG System.out.println("In solveConstraints 7/8 -- tc = " + `tc);

{
{
if ( (tom_leftTCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch316_2= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList).isEmptyconcTypeConstraint() ) {
tomMatch316_2= true ;
}
}
if (!(tomMatch316_2)) {

if(nkt.findTypeVars(
tom_typeVar,
tom_leftTCList)) {

tom_leftTCList= nkt.applySubstitution(
tom_typeVar,
tom_type,
tom_leftTCList);
}


}

}

}
{
if ( (tom_rightTCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch316_5= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList).isEmptyconcTypeConstraint() ) {
tomMatch316_5= true ;
}
}
if (!(tomMatch316_5)) {

if(nkt.findTypeVars(
tom_typeVar,
tom_rightTCList)) {

tom_rightTCList= nkt.applySubstitution(
tom_typeVar,
tom_type,
tom_rightTCList);
}


}

}

}


}

return 
tom_append_list_concTypeConstraint(tom_leftTCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_type, tom_type,  tomMatch315_66.getInfo() ) ,tom_append_list_concTypeConstraint(tom_rightTCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) );


}

}
}
}
if ( tomMatch315__end__60.isEmptyconcTypeConstraint() ) {
tomMatch315__end__60=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch315__end__60= tomMatch315__end__60.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch315__end__60==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch315__end__77=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_leftTCList=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch315__end__77, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch315__end__77.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch315_83= tomMatch315__end__77.getHeadconcTypeConstraint() ;
if ( (tomMatch315_83 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch315_80= tomMatch315_83.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch315_81= tomMatch315_83.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch315_80;
if ( (tomMatch315_81 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch315_81;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_rightTCList= tomMatch315__end__77.getTailconcTypeConstraint() ;
boolean tomMatch315_91= false ;
if ( (tomMatch315_80 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch315_80) ) {
tomMatch315_91= true ;
}
}
if (!(tomMatch315_91)) {

nkt.substitutions.put(
tom_typeVar,
tom_groundType);
//DEBUG System.out.println("In solveConstraints 5/6 -- tc = " + `tc);

{
{
if ( (tom_leftTCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch317_2= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList).isEmptyconcTypeConstraint() ) {
tomMatch317_2= true ;
}
}
if (!(tomMatch317_2)) {

if(nkt.findTypeVars(
tom_typeVar,
tom_leftTCList)) {

tom_leftTCList= nkt.applySubstitution(
tom_typeVar,
tom_groundType,
tom_leftTCList);
}


}

}

}
{
if ( (tom_rightTCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch317_5= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList).isEmptyconcTypeConstraint() ) {
tomMatch317_5= true ;
}
}
if (!(tomMatch317_5)) {

if(nkt.findTypeVars(
tom_typeVar,
tom_rightTCList)) {

tom_rightTCList= nkt.applySubstitution(
tom_typeVar,
tom_groundType,
tom_rightTCList);
}


}

}

}


}

return

tom_append_list_concTypeConstraint(tom_leftTCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_groundType, tom_groundType,  tomMatch315_83.getInfo() ) ,tom_append_list_concTypeConstraint(tom_rightTCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) );


}

}
}
}
if ( tomMatch315__end__77.isEmptyconcTypeConstraint() ) {
tomMatch315__end__77=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);
} else {
tomMatch315__end__77= tomMatch315__end__77.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch315__end__77==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));
}
}

}


}
return _visit_TypeConstraintList(tom__arg,introspector);

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
}
private static  tom.library.sl.Strategy  tom_make_solveConstraints( NewKernelTyper  t0) { 
return new solveConstraints(t0);
}


private void printError(TypeConstraint tConstraint) {
String fileName = "";
int line = 0;

{
{
if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tType1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tom_tType2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;
 tom.engine.adt.typeconstraints.types.Info  tom_info= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ;
if ( (tom_tType1 instanceof tom.engine.adt.tomtype.types.TomType) ) {
boolean tomMatch318_18= false ;
 String  tomMatch318_8= "" ;
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_tType1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
{
tomMatch318_18= true ;
tomMatch318_8= (( tom.engine.adt.tomtype.types.TomType )tom_tType1).getTomType() ;

}
} else {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_tType1) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {
{
tomMatch318_18= true ;
tomMatch318_8= (( tom.engine.adt.tomtype.types.TomType )tom_tType1).getTomType() ;

}
}
}
if (tomMatch318_18) {
if ( (tom_tType2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
boolean tomMatch318_17= false ;
 String  tomMatch318_10= "" ;
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_tType2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
{
tomMatch318_17= true ;
tomMatch318_10= (( tom.engine.adt.tomtype.types.TomType )tom_tType2).getTomType() ;

}
} else {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom_tType2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {
{
tomMatch318_17= true ;
tomMatch318_10= (( tom.engine.adt.tomtype.types.TomType )tom_tType2).getTomType() ;

}
}
}
if (tomMatch318_17) {
if ( (tom_info instanceof tom.engine.adt.typeconstraints.types.Info) ) {
if ( ((( tom.engine.adt.typeconstraints.types.Info )tom_info) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch318_12= (( tom.engine.adt.typeconstraints.types.Info )tom_info).getAstName() ;
if ( (tomMatch318_12 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

Option option = TomBase.findOriginTracking(
 (( tom.engine.adt.typeconstraints.types.Info )tom_info).getOptions() );

{
{
if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {
boolean tomMatch319_2= false ;
if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {
tomMatch319_2= true ;
}
if (!(tomMatch319_2)) {

fileName = option.getFileName();
line = option.getLine();

}

}

}

}

if(lazyType==false) {
TomMessage.error(logger,fileName, line,
TomMessage.incompatibleTypes,
tomMatch318_8,
tomMatch318_10,
 tomMatch318_12.getString() ); 
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


private boolean findTypeVars(TomType typeVar, TypeConstraintList
tcList) {
//DEBUG System.out.println("\n Test pour findTypeVars -- ligne 1.");

{
{
if ( (tcList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch320__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList);
do {
{
if (!( tomMatch320__end__4.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch320_10= tomMatch320__end__4.getHeadconcTypeConstraint() ;
if ( (tomMatch320_10 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
if ( (typeVar== tomMatch320_10.getType1() ) ) {

return true;

}
if ( (typeVar== tomMatch320_10.getType2() ) ) {

return true;

}

}
}
if ( tomMatch320__end__4.isEmptyconcTypeConstraint() ) {
tomMatch320__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList);
} else {
tomMatch320__end__4= tomMatch320__end__4.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch320__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList)) ));
}
}

}

}

return false;
}

private TypeConstraintList applySubstitution(TomType oldtt, TomType newtt,
TypeConstraintList tcList) {
try {
return (TypeConstraintList)

tom_make_TopDown(tom_make_replaceTypeConstraints(oldtt,newtt)).visitLight(tcList);
} catch(tom.library.sl.VisitFailure e) {
throw new RuntimeException("applySubstitution: should not be here.");
}
}


public static class replaceTypeConstraints extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomtype.types.TomType  oldtt;
private  tom.engine.adt.tomtype.types.TomType  newtt;
public replaceTypeConstraints( tom.engine.adt.tomtype.types.TomType  oldtt,  tom.engine.adt.tomtype.types.TomType  newtt) {
super(( new tom.library.sl.Identity() ));
this.oldtt=oldtt;
this.newtt=newtt;
}
public  tom.engine.adt.tomtype.types.TomType  getoldtt() {
return oldtt;
}
public  tom.engine.adt.tomtype.types.TomType  getnewtt() {
return newtt;
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
public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {
if (((( tom.engine.adt.tomtype.types.TomType )tom__arg) == oldtt)) {

return newtt; 

}
}

}

}
return _visit_TomType(tom__arg,introspector);

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
}
private static  tom.library.sl.Strategy  tom_make_replaceTypeConstraints( tom.engine.adt.tomtype.types.TomType  t0,  tom.engine.adt.tomtype.types.TomType  t1) { 
return new replaceTypeConstraints(t0,t1);
}


private Code replaceInCode(Code code) {
Code replacedCode = code;
try {
replacedCode =

tom_make_RepeatId(tom_make_TopDown(tom_make_replaceFreshTypeVar(this))).visitLight(code);
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
tom_make_RepeatId(tom_make_TopDown(tom_make_replaceFreshTypeVar(this))).visitLight(tSymbol);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("replaceInSymbolTable: failure on " +
tSymbol);
}
//DEBUG System.out.println("replaceInSymboltable() - tSymbol after strategy: "
//DEBUG     + tSymbol);
/*
%match {
Symbol[AstName=aName@Name(""),TypesToType=TypesToType(concTomType(contextType),contextType),PairNameDeclList=pndList,Options=optionList] << tSymbol -> {

}  
}*/
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
} else {
//DEBUG System.out.println("\n----- There is no mapping for " + `typeVar +'\n');
return 
 tom.engine.adt.tomtype.types.tomtype.Type.make( (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() ,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
}    


}
}

}

}
return _visit_TomType(tom__arg,introspector);

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
}
private static  tom.library.sl.Strategy  tom_make_replaceFreshTypeVar( NewKernelTyper  t0) { 
return new replaceFreshTypeVar(t0);
}


public void printGeneratedConstraints(TypeConstraintList TCList) {

{
{
if ( (TCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch323_2= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList).isEmptyconcTypeConstraint() ) {
tomMatch323_2= true ;
}
}
if (!(tomMatch323_2)) {

System.out.print("\n------ Type Constraints : \n {");
printEachConstraint(TCList);
System.out.print("}");

}

}

}

}

}
public void printEachConstraint(TypeConstraintList TCList) {

{
{
if ( (TCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList).isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch324_7= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList).getHeadconcTypeConstraint() ;
if ( (tomMatch324_7 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailTCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList).getTailconcTypeConstraint() ;

printType(
 tomMatch324_7.getType1() );
System.out.print(" = ");
printType(
 tomMatch324_7.getType2() );
if (
tom_tailTCList!= 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
System.out.print(", "); 
printEachConstraint(
tom_tailTCList);
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
