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
          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );       } else {         return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.SequenceId) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) );       } else {         return ( (tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.SequenceId(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin):new tom.library.sl.SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.ChoiceId) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) );       } else {         return ( (tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.ChoiceId(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin):new tom.library.sl.ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)) );   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )) )) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?s:new tom.library.sl.Choice(s,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?s:new tom.library.sl.Sequence(s,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?s:new tom.library.sl.Sequence(s,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ),( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) ))) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.ChoiceId(v,( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}      private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_append_list_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList l1,  tom.engine.adt.typeconstraints.types.TypeConstraintList  l2) {     if( l1.isEmptyconcTypeConstraint() ) {       return l2;     } else if( l2.isEmptyconcTypeConstraint() ) {       return l1;     } else if(  l1.getTailconcTypeConstraint() .isEmptyconcTypeConstraint() ) {       return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,l2) ;     } else {       return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,tom_append_list_concTypeConstraint( l1.getTailconcTypeConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_get_slice_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList  begin,  tom.engine.adt.typeconstraints.types.TypeConstraintList  end, tom.engine.adt.typeconstraints.types.TypeConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeConstraint()  ||  (end== tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( begin.getHeadconcTypeConstraint() ,( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_get_slice_concTypeConstraint( begin.getTailconcTypeConstraint() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.CodeList  tom_append_list_concCode( tom.engine.adt.code.types.CodeList l1,  tom.engine.adt.code.types.CodeList  l2) {     if( l1.isEmptyconcCode() ) {       return l2;     } else if( l2.isEmptyconcCode() ) {       return l1;     } else if(  l1.getTailconcCode() .isEmptyconcCode() ) {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,l2) ;     } else {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,tom_append_list_concCode( l1.getTailconcCode() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.CodeList  tom_get_slice_concCode( tom.engine.adt.code.types.CodeList  begin,  tom.engine.adt.code.types.CodeList  end, tom.engine.adt.code.types.CodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcCode()  ||  (end== tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.codelist.ConsconcCode.make( begin.getHeadconcCode() ,( tom.engine.adt.code.types.CodeList )tom_get_slice_concCode( begin.getTailconcCode() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_append_list_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList l1,  tom.engine.adt.tomslot.types.PairNameDeclList  l2) {     if( l1.isEmptyconcPairNameDecl() ) {       return l2;     } else if( l2.isEmptyconcPairNameDecl() ) {       return l1;     } else if(  l1.getTailconcPairNameDecl() .isEmptyconcPairNameDecl() ) {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( l1.getHeadconcPairNameDecl() ,tom_append_list_concPairNameDecl( l1.getTailconcPairNameDecl() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.PairNameDeclList  tom_get_slice_concPairNameDecl( tom.engine.adt.tomslot.types.PairNameDeclList  begin,  tom.engine.adt.tomslot.types.PairNameDeclList  end, tom.engine.adt.tomslot.types.PairNameDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPairNameDecl()  ||  (end== tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.pairnamedecllist.ConsconcPairNameDecl.make( begin.getHeadconcPairNameDecl() ,( tom.engine.adt.tomslot.types.PairNameDeclList )tom_get_slice_concPairNameDecl( begin.getTailconcPairNameDecl() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyOrConstraint() ) {       return l2;     } else if( l2.isEmptyOrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {       if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }    







  private int freshTypeVarCounter = 0;
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

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  protected SymbolTable getSymbolTable() {
    return symbolTable;
  }

  protected TomType getCodomain(TomSymbol tSymbol) {
    //DEBUG System.out.println("getCodomain = " + TomBase.getSymbolCodomain(tSymbol));
    return TomBase.getSymbolCodomain(tSymbol);
  }

  protected TomSymbol getSymbolFromTerm(TomTerm tTerm) {
    return TomBase.getSymbolFromTerm(tTerm, getSymbolTable());
   }

  protected TomSymbol getSymbolFromTerm(BQTerm bqTerm) {
    return TomBase.getSymbolFromTerm(bqTerm,getSymbolTable());
  }

  protected TomSymbol getSymbolFromName(String tName) {
    return TomBase.getSymbolFromName(tName, getSymbolTable());
   }

  protected TomSymbol getSymbolFromType(TomType tType) {
    {{if ( (tType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {

         return TomBase.getSymbolFromType( tom.engine.adt.tomtype.types.tomtype.Type.make( (( tom.engine.adt.tomtype.types.TomType )tType).getTomType() ,  (( tom.engine.adt.tomtype.types.TomType )tType).getTlType() ) , getSymbolTable()); 
       }}}}

    return TomBase.getSymbolFromType(tType,getSymbolTable()); 
   }

  protected TomType getType(String tName) {
    return getSymbolTable().getType(tName); 
   }

  protected TomType getType(TomTerm tTerm) {
    {{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch2_3= false ; tom.engine.adt.tomtype.types.TomType  tomMatch2_1= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch2_3= true ;tomMatch2_1= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch2_3= true ;tomMatch2_1= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstType() ;}}}if (tomMatch2_3) {
 return tomMatch2_1; }}}{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch2_5= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getNameList() ;if ( ((tomMatch2_5 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch2_5 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch2_5.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch2_11= tomMatch2_5.getHeadconcTomName() ;if ( (tomMatch2_11 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch2_11.getString() ;


        if(tom_name.equals("")) {
          // Maybe we need to discover the symbol using the context type
          // information (i.e. the type of subject)
          throw new TomRuntimeException("No symbol found because there is no name.");
        }
        //DEBUG System.out.println("\n------- getType : name = " + `name +'\n');
        TomSymbol tSymbol = getSymbolFromName(tom_name);
        //DEBUG System.out.println("\n------- after getSymbolFromName");
        return getCodomain(tSymbol);
      }}}}}}{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 return getType( (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getTomTerm() ); }}}}
 
    throw new TomRuntimeException("getType(TomTerm): should not be here.");
  }

  protected TomType getType(BQTerm bqTerm) {
    {{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch3_3= false ; tom.engine.adt.tomtype.types.TomType  tomMatch3_1= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch3_3= true ;tomMatch3_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch3_3= true ;tomMatch3_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildReducedTerm) ) {{tomMatch3_3= true ;tomMatch3_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;}}}}if (tomMatch3_3) {

        return tomMatch3_1;
      }}}{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch3_9= false ; tom.engine.adt.tomname.types.TomName  tomMatch3_5= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch3_9= true ;tomMatch3_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) {{tomMatch3_9= true ;tomMatch3_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {{tomMatch3_9= true ;tomMatch3_5= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;}}}}if (tomMatch3_9) {if ( (tomMatch3_5 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch3_5.getString() ;



        if(tom_name.equals("")) {
          // Maybe we need to discover the symbol using the context type
          // information (i.e. the type of subject)
          throw new TomRuntimeException("No symbol found because there is no name.");
        }
        TomSymbol tSymbol = getSymbolFromName(tom_name);
        return getCodomain(tSymbol);
      }}}}}
 
    throw new TomRuntimeException("getType(BQTerm): should not be here.");
  }
/* TO REPLACE getFreshTypeVar()
  protected int getFreshTlTIndex() {
    return freshTypeVarCounter++;
   }
*/
  protected TargetLanguageType getFreshTypeVar() {
    return  tom.engine.adt.tomtype.types.targetlanguagetype.TypeVar.make(freshTypeVarCounter++) ;
  }

/* TO REPLACE getUnknownFreshTypeVar()  
   protected TomType getUnknownFreshTypeVar() {
    TomType tType = symbolTable.UNKNOWN_TYPE;
    %match(tType) {
      Type[TomType=tomType] -> { return `TypeVar(tomType,getFreshTlTIndex())}
    }
    throw new TomRuntimeException("getUnknownFreshTypeVar: should not be here.");
   }
*/

  protected TomType getUnknownFreshTypeVar() {
    return  tom.engine.adt.tomtype.types.tomtype.Type.make("unknown type",  tom.engine.adt.tomtype.types.targetlanguagetype.TypeVar.make(freshTypeVarCounter++) ) ;
  }

  /*
     * pem: use if(...==... && typeConstraints.contains(...))
     */
  protected void addConstraint(TypeConstraint tConstraint) {
    {{if ( (typeConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch4_9= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch4__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints);do {{if (!( tomMatch4__end__5.isEmptyconcTypeConstraint() )) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)== tomMatch4__end__5.getHeadconcTypeConstraint() ) ) {tomMatch4_9= true ;}}if ( tomMatch4__end__5.isEmptyconcTypeConstraint() ) {tomMatch4__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints);} else {tomMatch4__end__5= tomMatch4__end__5.getTailconcTypeConstraint() ;}}} while(!( (tomMatch4__end__5==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )typeConstraints)) ));}if (!(tomMatch4_9)) {


        typeConstraints =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(typeConstraints, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
      }}}}}

  }

  protected void addTomTerm(TomTerm tTerm) {
    varPatternList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tTerm,tom_append_list_concTomTerm(varPatternList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
   }

  protected void addBQTerm(BQTerm bqTerm) {
    varList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(bqTerm,tom_append_list_concBQTerm(varList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
   }

  /**
   * The method <code>resetVarPatternList</code> empties varPatternList after
   * checking if <code>varList</code> contains
   * a corresponding BQTerm in order to remove it from <code>varList</code. too
   */
  protected void resetVarPatternList() {
    for(TomTerm tTerm: varPatternList.getCollectionconcTomTerm()) {
      {{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch5_14= false ; tom.engine.adt.tomname.types.TomName  tomMatch5_2= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch5_14= true ;tomMatch5_2= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch5_14= true ;tomMatch5_2= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstName() ;}}}if (tomMatch5_14) {if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch5__end__7=(( tom.engine.adt.code.types.BQTermList )varList);do {{if (!( tomMatch5__end__7.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch5_11= tomMatch5__end__7.getHeadconcBQTerm() ;boolean tomMatch5_13= false ; tom.engine.adt.tomname.types.TomName  tomMatch5_10= null ;if ( (tomMatch5_11 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch5_13= true ;tomMatch5_10= tomMatch5_11.getAstName() ;}} else {if ( (tomMatch5_11 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch5_13= true ;tomMatch5_10= tomMatch5_11.getAstName() ;}}}if (tomMatch5_13) {if ( (tomMatch5_2==tomMatch5_10) ) {


            varList = tom_append_list_concBQTerm(tom_get_slice_concBQTerm((( tom.engine.adt.code.types.BQTermList )varList),tomMatch5__end__7, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ),tom_append_list_concBQTerm( tomMatch5__end__7.getTailconcBQTerm() , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ));
          }}}if ( tomMatch5__end__7.isEmptyconcBQTerm() ) {tomMatch5__end__7=(( tom.engine.adt.code.types.BQTermList )varList);} else {tomMatch5__end__7= tomMatch5__end__7.getTailconcBQTerm() ;}}} while(!( (tomMatch5__end__7==(( tom.engine.adt.code.types.BQTermList )varList)) ));}}}}}}

    }
    varPatternList =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
  }

  /**
   * The method <code>init</code> empties all global lists and hashMaps which means to
   * empty <code>varPatternList</code>, <code>varList</code>,
   * <code>typeConstraints</code> and <code>substitutions</code>
   */
  private void init() {
    varPatternList =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
    varList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    typeConstraints =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    substitutions = new HashMap<TomType,TomType>();
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
    {{if ( (code instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )code) instanceof tom.engine.adt.code.types.code.Tom) ) { tom.engine.adt.code.types.CodeList  tomMatch6_1= (( tom.engine.adt.code.types.Code )code).getCodeList() ;if ( ((tomMatch6_1 instanceof tom.engine.adt.code.types.codelist.ConsconcCode) || (tomMatch6_1 instanceof tom.engine.adt.code.types.codelist.EmptyconcCode)) ) {if (!( tomMatch6_1.isEmptyconcCode() )) {

        boolean flagInnerMatch = false;
        CodeList codeResult =  tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;
        for(Code headCodeList : tomMatch6_1.getCollectionconcCode()) {
          {{if ( (headCodeList instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )headCodeList) instanceof tom.engine.adt.code.types.code.InstructionToCode) ) { tom.engine.adt.tominstruction.types.Instruction  tomMatch7_1= (( tom.engine.adt.code.types.Code )headCodeList).getAstInstruction() ;if ( (tomMatch7_1 instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {

              try {
                flagInnerMatch = false;
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 1.");
                // Generate type constraints for a %match
                //DEBUG System.out.println("CIList avant = " + `constraintInstructionList);
                inferConstraintInstructionList( tomMatch7_1.getConstraintInstructionList() ,flagInnerMatch);
                //DEBUG System.out.println("CIList apr?s= " + result + "\n\n");
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 2.");
                //DEBUG System.out.println("\n typeConstraints before solve = " + typeConstraints);
                typeConstraints = tom_make_RepeatId(tom_make_solveConstraints(this)).visitLight(typeConstraints);
                //DEBUG System.out.println("\n typeConstraints aftersolve = " + typeConstraints);
                System.out.println("\n substitutions= " + substitutions);
                //DEBUG printGeneratedConstraints(typeConstraints);
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 5.");
              } catch(tom.library.sl.VisitFailure e) {
                throw new TomRuntimeException("inferCode: failure on " +
                    headCodeList);
              } 
            }}}}}

          Code headCodeResult = replaceInCode(headCodeList);
          codeResult = tom_append_list_concCode(codeResult, tom.engine.adt.code.types.codelist.ConsconcCode.make(headCodeResult, tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) );
          replaceInSymbolTable();
          init();
        }
        return  tom.engine.adt.code.types.code.Tom.make(codeResult) ;
      }}}}}}

    // If it is a ill-formed code (different from "Tom(...)")
    return code;
  }

  /**
   * The method <code>inferConstraintInstructionList</code> applies rule CT-RULE
   * to a pair "condition -> action" in order to collect all variables occurring
   * in the condition and put them into <code>varPatternList</code> (for those
   * variables occurring in match constraints) and <code>varList</code> (for
   * those variables occurring in numeric constraints) to be able to handle non-linearity.
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
  private void inferConstraintInstructionList(ConstraintInstructionList ciList,
      boolean flagInnerMatch) {
    //DEBUG System.out.println("\n Test pour inferConstraintInstructionList -- ligne 1.");
    {{if ( (ciList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) {if (!( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList).isEmptyconcConstraintInstruction() )) { tom.engine.adt.tominstruction.types.ConstraintInstruction  tomMatch8_7= (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList).getHeadconcConstraintInstruction() ;if ( (tomMatch8_7 instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) { tom.engine.adt.tominstruction.types.ConstraintInstruction  tom_headCIList= (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList).getHeadconcConstraintInstruction() ;

        try {
          //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 4.");
          //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 2.");
          // Collect variables and type them with fresh type variables
          // Rename variables of pattern that already exist in varPatternList
          //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 3.");
          //DEBUG System.out.println("\n varPatternList = " + `varPatternList);
          //DEBUG System.out.println("\n varList = " + `varList);
          //DEBUG System.out.println("\n Constraints = " + typeConstraints);
          inferConstraint( tomMatch8_7.getConstraint() );
          inferAction( tomMatch8_7.getAction() );
          if (!flagInnerMatch) {
            tom_make_TopDownCollect(tom_make_CollectVars(this)).visitLight(tom_headCIList);
            //DEBUG System.out.println("\n Test pour inferConstraintInstructionList apr?s reset.");
            //DEBUG System.out.println("\n varPatternList avant = " + `varPatternList);
            //DEBUG System.out.println("\n varList avant = " + `varList); 
            resetVarPatternList();
            //DEBUG System.out.println("\n varPatternList apr?s = " + `varPatternList);
            //DEBUG System.out.println("\n varList apr?s = " + `varList);
          }
          inferConstraintInstructionList( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )ciList).getTailconcConstraintInstruction() ,flagInnerMatch);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraintInstructionList: failure on " + tom_headCIList);
        }
      }}}}}}

  }

  private void inferAction(Instruction action) {
    {{if ( (action instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )action) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) { tom.engine.adt.tominstruction.types.Instruction  tomMatch9_1= (( tom.engine.adt.tominstruction.types.Instruction )action).getAstInstruction() ;if ( (tomMatch9_1 instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch9_3= tomMatch9_1.getInstList() ;if ( ((tomMatch9_3 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch9_3 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {if (!( tomMatch9_3.isEmptyconcInstruction() )) {

        boolean flagInnerMatch = true;
        for(Instruction headInstruction : tomMatch9_3.getCollectionconcInstruction()) {
          {{if ( (headInstruction instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )headInstruction) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {

              flagInnerMatch = true; 
              // Generate type constraints for a %match in action side
              inferConstraintInstructionList( (( tom.engine.adt.tominstruction.types.Instruction )headInstruction).getConstraintInstructionList() ,flagInnerMatch);
              //DEBUG printGeneratedConstraints(typeConstraints);
            }}}}


        }  
      }}}}}}}

  }



  /**
   * The class <code>CollectVars</code> is generated from a strategy which
   * search for variables occurring in a condition:
   * <ul>
   * <li> variables of type "TomTerm" are adedd to the <code>varPatternList</code> (for those
   * variables occurring in match constraints) if they have not yet been added
   * there; otherwise, a type constraint is added to
   * <code>typeConstraints</code> to ensure that both variables have same type
   * (this happens in case of non-linearity)
   * <li> variables of type "BQTerm" are added to the <code>varList</code> (for
   * those variables occurring in numeric constraints) if they have been added
   * neither in <code>varPatternList</code> nor <code>varList</code> (since a
   * BQVariable/BQVariableStar can have occurred in a previous match constraint
   * as a Variable/VariableStar, in the case of a composed condition);
   * otherwise, a type constraint is added to
   * <code>typeConstraints</code> to ensure that both variables have same type
   * (this happens in case of non-linearity)
   * </ul>
   */

  /*
   * pem: simplify the following code to do only one loop
   */
  public static class CollectVars extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public CollectVars( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tom_var=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);{{if ( (tom_var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomtype.types.TomType  tom_aType1= (( tom.engine.adt.tomterm.types.TomTerm )tom_var).getAstType() ;if ( (nkt.varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch12__end__8=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);do {{if (!( tomMatch12__end__8.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch12_13= tomMatch12__end__8.getHeadconcTomTerm() ;boolean tomMatch12_18= false ; tom.engine.adt.tomname.types.TomName  tomMatch12_11= null ; tom.engine.adt.tomtype.types.TomType  tomMatch12_12= null ;if ( (tomMatch12_13 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch12_18= true ;tomMatch12_11= tomMatch12_13.getAstName() ;tomMatch12_12= tomMatch12_13.getAstType() ;}} else {if ( (tomMatch12_13 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch12_18= true ;tomMatch12_11= tomMatch12_13.getAstName() ;tomMatch12_12= tomMatch12_13.getAstType() ;}}}if (tomMatch12_18) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_var).getAstName() ==tomMatch12_11) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch12_12;boolean tomMatch12_17= false ;if ( (tom_aType1==tomMatch12_12) ) {if ( (tom_aType2==tomMatch12_12) ) {tomMatch12_17= true ;}}if (!(tomMatch12_17)) {







              //maybe this is not necessary since the variable generated has aType2
              nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2) );
              //DEBUG System.out.println("CollectVars -- constraint = " + `aType1
              //+ " = " + `aType2);
            }}}}if ( tomMatch12__end__8.isEmptyconcTomTerm() ) {tomMatch12__end__8=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);} else {tomMatch12__end__8= tomMatch12__end__8.getTailconcTomTerm() ;}}} while(!( (tomMatch12__end__8==(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList)) ));}}}}}{if ( (tom_var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( (nkt.varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {boolean tomMatch12_32= false ;if ( (((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch12__end__26=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);do {{if (!( tomMatch12__end__26.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch12_30= tomMatch12__end__26.getHeadconcTomTerm() ;boolean tomMatch12_33= false ; tom.engine.adt.tomname.types.TomName  tomMatch12_29= null ;if ( (tomMatch12_30 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch12_33= true ;tomMatch12_29= tomMatch12_30.getAstName() ;}} else {if ( (tomMatch12_30 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch12_33= true ;tomMatch12_29= tomMatch12_30.getAstName() ;}}}if (tomMatch12_33) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_var).getAstName() ==tomMatch12_29) ) {tomMatch12_32= true ;}}}if ( tomMatch12__end__26.isEmptyconcTomTerm() ) {tomMatch12__end__26=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);} else {tomMatch12__end__26= tomMatch12__end__26.getTailconcTomTerm() ;}}} while(!( (tomMatch12__end__26==(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList)) ));}if (!(tomMatch12_32)) {





            nkt.addTomTerm(tom_var);
          }}}}}{if ( (tom_var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomtype.types.TomType  tom_aType1= (( tom.engine.adt.tomterm.types.TomTerm )tom_var).getAstType() ;if ( (nkt.varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch12__end__42=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);do {{if (!( tomMatch12__end__42.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch12_47= tomMatch12__end__42.getHeadconcTomTerm() ;boolean tomMatch12_52= false ; tom.engine.adt.tomtype.types.TomType  tomMatch12_46= null ; tom.engine.adt.tomname.types.TomName  tomMatch12_45= null ;if ( (tomMatch12_47 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch12_52= true ;tomMatch12_45= tomMatch12_47.getAstName() ;tomMatch12_46= tomMatch12_47.getAstType() ;}} else {if ( (tomMatch12_47 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch12_52= true ;tomMatch12_45= tomMatch12_47.getAstName() ;tomMatch12_46= tomMatch12_47.getAstType() ;}}}if (tomMatch12_52) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_var).getAstName() ==tomMatch12_45) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch12_46;boolean tomMatch12_51= false ;if ( (tom_aType1==tomMatch12_46) ) {if ( (tom_aType2==tomMatch12_46) ) {tomMatch12_51= true ;}}if (!(tomMatch12_51)) {





            nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2) );
            //DEBUG System.out.println("CollectVars -- constraint = " + `type1 + " = " + `type2);
          }}}}if ( tomMatch12__end__42.isEmptyconcTomTerm() ) {tomMatch12__end__42=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);} else {tomMatch12__end__42= tomMatch12__end__42.getTailconcTomTerm() ;}}} while(!( (tomMatch12__end__42==(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList)) ));}}}}}{if ( (tom_var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( (nkt.varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {boolean tomMatch12_66= false ;if ( (((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch12__end__60=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);do {{if (!( tomMatch12__end__60.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch12_64= tomMatch12__end__60.getHeadconcTomTerm() ;boolean tomMatch12_67= false ; tom.engine.adt.tomname.types.TomName  tomMatch12_63= null ;if ( (tomMatch12_64 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch12_67= true ;tomMatch12_63= tomMatch12_64.getAstName() ;}} else {if ( (tomMatch12_64 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch12_67= true ;tomMatch12_63= tomMatch12_64.getAstName() ;}}}if (tomMatch12_67) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_var).getAstName() ==tomMatch12_63) ) {tomMatch12_66= true ;}}}if ( tomMatch12__end__60.isEmptyconcTomTerm() ) {tomMatch12__end__60=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);} else {tomMatch12__end__60= tomMatch12__end__60.getTailconcTomTerm() ;}}} while(!( (tomMatch12__end__60==(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList)) ));}if (!(tomMatch12_66)) {




            nkt.addTomTerm(tom_var);
          }}}}}}

      }}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) { tom.engine.adt.code.types.BQTerm  tom_bqvar=(( tom.engine.adt.code.types.BQTerm )tom__arg);




        BQTerm newBQVar = tom_bqvar;
        {{if ( (tom_bqvar instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomoption.types.OptionList  tom_option= (( tom.engine.adt.code.types.BQTerm )tom_bqvar).getOption() ; tom.engine.adt.tomname.types.TomName  tom_aName= (( tom.engine.adt.code.types.BQTerm )tom_bqvar).getAstName() ; tom.engine.adt.tomtype.types.TomType  tom_aType1= (( tom.engine.adt.code.types.BQTerm )tom_bqvar).getAstType() ;if ( (nkt.varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )nkt.varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )nkt.varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch14__end__10=(( tom.engine.adt.code.types.BQTermList )nkt.varList);do {{if (!( tomMatch14__end__10.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch14_21= tomMatch14__end__10.getHeadconcBQTerm() ;boolean tomMatch14_32= false ; tom.engine.adt.tomname.types.TomName  tomMatch14_19= null ; tom.engine.adt.tomtype.types.TomType  tomMatch14_20= null ;if ( (tomMatch14_21 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch14_32= true ;tomMatch14_19= tomMatch14_21.getAstName() ;tomMatch14_20= tomMatch14_21.getAstType() ;}} else {if ( (tomMatch14_21 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch14_32= true ;tomMatch14_19= tomMatch14_21.getAstName() ;tomMatch14_20= tomMatch14_21.getAstType() ;}}}if (tomMatch14_32) {if ( (tom_aName==tomMatch14_19) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch14_20;boolean tomMatch14_31= false ;if ( (tom_aType1==tomMatch14_20) ) {if ( (tom_aType2==tomMatch14_20) ) {tomMatch14_31= true ;}}if (!(tomMatch14_31)) {






            nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2) );
            newBQVar =  tom.engine.adt.code.types.bqterm.BQVariable.make(tom_option, tom_aName, tom_aType2) ;
         }}}}if ( tomMatch14__end__10.isEmptyconcBQTerm() ) {tomMatch14__end__10=(( tom.engine.adt.code.types.BQTermList )nkt.varList);} else {tomMatch14__end__10= tomMatch14__end__10.getTailconcBQTerm() ;}}} while(!( (tomMatch14__end__10==(( tom.engine.adt.code.types.BQTermList )nkt.varList)) ));}}if ( (nkt.varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch14__end__16=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);do {{if (!( tomMatch14__end__16.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch14_24= tomMatch14__end__16.getHeadconcTomTerm() ;boolean tomMatch14_34= false ; tom.engine.adt.tomtype.types.TomType  tomMatch14_23= null ; tom.engine.adt.tomname.types.TomName  tomMatch14_22= null ;if ( (tomMatch14_24 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch14_34= true ;tomMatch14_22= tomMatch14_24.getAstName() ;tomMatch14_23= tomMatch14_24.getAstType() ;}} else {if ( (tomMatch14_24 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch14_34= true ;tomMatch14_22= tomMatch14_24.getAstName() ;tomMatch14_23= tomMatch14_24.getAstType() ;}}}if (tomMatch14_34) {if ( (tom_aName==tomMatch14_22) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch14_23;boolean tomMatch14_33= false ;if ( (tom_aType1==tomMatch14_23) ) {if ( (tom_aType2==tomMatch14_23) ) {tomMatch14_33= true ;}}if (!(tomMatch14_33)) {             nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2) );             newBQVar =  tom.engine.adt.code.types.bqterm.BQVariable.make(tom_option, tom_aName, tom_aType2) ;          }}}}if ( tomMatch14__end__16.isEmptyconcTomTerm() ) {tomMatch14__end__16=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);} else {tomMatch14__end__16= tomMatch14__end__16.getTailconcTomTerm() ;}}} while(!( (tomMatch14__end__16==(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList)) ));}}}}}{if ( (tom_bqvar instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tom_aName= (( tom.engine.adt.code.types.BQTerm )tom_bqvar).getAstName() ;if ( (nkt.varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (nkt.varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {boolean tomMatch14_60= false ;if ( (((( tom.engine.adt.code.types.BQTermList )nkt.varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )nkt.varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch14__end__43=(( tom.engine.adt.code.types.BQTermList )nkt.varList);do {{if (!( tomMatch14__end__43.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch14_53= tomMatch14__end__43.getHeadconcBQTerm() ;boolean tomMatch14_61= false ; tom.engine.adt.tomname.types.TomName  tomMatch14_52= null ;if ( (tomMatch14_53 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch14_61= true ;tomMatch14_52= tomMatch14_53.getAstName() ;}} else {if ( (tomMatch14_53 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch14_61= true ;tomMatch14_52= tomMatch14_53.getAstName() ;}}}if (tomMatch14_61) {if ( (tom_aName==tomMatch14_52) ) {tomMatch14_60= true ;}}}if ( tomMatch14__end__43.isEmptyconcBQTerm() ) {tomMatch14__end__43=(( tom.engine.adt.code.types.BQTermList )nkt.varList);} else {tomMatch14__end__43= tomMatch14__end__43.getTailconcBQTerm() ;}}} while(!( (tomMatch14__end__43==(( tom.engine.adt.code.types.BQTermList )nkt.varList)) ));}if (!(tomMatch14_60)) {boolean tomMatch14_58= false ;if ( (((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch14__end__49=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);do {{if (!( tomMatch14__end__49.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch14_55= tomMatch14__end__49.getHeadconcTomTerm() ;boolean tomMatch14_59= false ; tom.engine.adt.tomname.types.TomName  tomMatch14_54= null ;if ( (tomMatch14_55 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch14_59= true ;tomMatch14_54= tomMatch14_55.getAstName() ;}} else {if ( (tomMatch14_55 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch14_59= true ;tomMatch14_54= tomMatch14_55.getAstName() ;}}}if (tomMatch14_59) {if ( (tom_aName==tomMatch14_54) ) {tomMatch14_58= true ;}}}if ( tomMatch14__end__49.isEmptyconcTomTerm() ) {tomMatch14__end__49=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);} else {tomMatch14__end__49= tomMatch14__end__49.getTailconcTomTerm() ;}}} while(!( (tomMatch14__end__49==(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList)) ));}if (!(tomMatch14_58)) {





            nkt.addBQTerm(tom_bqvar);
          }}}}}}}{if ( (tom_bqvar instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.tomoption.types.OptionList  tom_option= (( tom.engine.adt.code.types.BQTerm )tom_bqvar).getOption() ; tom.engine.adt.tomname.types.TomName  tom_aName= (( tom.engine.adt.code.types.BQTerm )tom_bqvar).getAstName() ; tom.engine.adt.tomtype.types.TomType  tom_aType1= (( tom.engine.adt.code.types.BQTerm )tom_bqvar).getAstType() ;if ( (nkt.varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )nkt.varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )nkt.varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch14__end__72=(( tom.engine.adt.code.types.BQTermList )nkt.varList);do {{if (!( tomMatch14__end__72.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch14_83= tomMatch14__end__72.getHeadconcBQTerm() ;boolean tomMatch14_94= false ; tom.engine.adt.tomname.types.TomName  tomMatch14_81= null ; tom.engine.adt.tomtype.types.TomType  tomMatch14_82= null ;if ( (tomMatch14_83 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch14_94= true ;tomMatch14_81= tomMatch14_83.getAstName() ;tomMatch14_82= tomMatch14_83.getAstType() ;}} else {if ( (tomMatch14_83 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch14_94= true ;tomMatch14_81= tomMatch14_83.getAstName() ;tomMatch14_82= tomMatch14_83.getAstType() ;}}}if (tomMatch14_94) {if ( (tom_aName==tomMatch14_81) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch14_82;boolean tomMatch14_93= false ;if ( (tom_aType1==tomMatch14_82) ) {if ( (tom_aType2==tomMatch14_82) ) {tomMatch14_93= true ;}}if (!(tomMatch14_93)) {







            nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2) );
            newBQVar =  tom.engine.adt.code.types.bqterm.BQVariable.make(tom_option, tom_aName, tom_aType2) ;
          }}}}if ( tomMatch14__end__72.isEmptyconcBQTerm() ) {tomMatch14__end__72=(( tom.engine.adt.code.types.BQTermList )nkt.varList);} else {tomMatch14__end__72= tomMatch14__end__72.getTailconcBQTerm() ;}}} while(!( (tomMatch14__end__72==(( tom.engine.adt.code.types.BQTermList )nkt.varList)) ));}}if ( (nkt.varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch14__end__78=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);do {{if (!( tomMatch14__end__78.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch14_86= tomMatch14__end__78.getHeadconcTomTerm() ;boolean tomMatch14_96= false ; tom.engine.adt.tomtype.types.TomType  tomMatch14_85= null ; tom.engine.adt.tomname.types.TomName  tomMatch14_84= null ;if ( (tomMatch14_86 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch14_96= true ;tomMatch14_84= tomMatch14_86.getAstName() ;tomMatch14_85= tomMatch14_86.getAstType() ;}} else {if ( (tomMatch14_86 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch14_96= true ;tomMatch14_84= tomMatch14_86.getAstName() ;tomMatch14_85= tomMatch14_86.getAstType() ;}}}if (tomMatch14_96) {if ( (tom_aName==tomMatch14_84) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch14_85;boolean tomMatch14_95= false ;if ( (tom_aType1==tomMatch14_85) ) {if ( (tom_aType2==tomMatch14_85) ) {tomMatch14_95= true ;}}if (!(tomMatch14_95)) {             nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2) );             newBQVar =  tom.engine.adt.code.types.bqterm.BQVariable.make(tom_option, tom_aName, tom_aType2) ;           }}}}if ( tomMatch14__end__78.isEmptyconcTomTerm() ) {tomMatch14__end__78=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);} else {tomMatch14__end__78= tomMatch14__end__78.getTailconcTomTerm() ;}}} while(!( (tomMatch14__end__78==(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList)) ));}}}}}{if ( (tom_bqvar instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_bqvar) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tom_aName= (( tom.engine.adt.code.types.BQTerm )tom_bqvar).getAstName() ;if ( (nkt.varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (nkt.varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {boolean tomMatch14_122= false ;if ( (((( tom.engine.adt.code.types.BQTermList )nkt.varList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )nkt.varList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch14__end__105=(( tom.engine.adt.code.types.BQTermList )nkt.varList);do {{if (!( tomMatch14__end__105.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch14_115= tomMatch14__end__105.getHeadconcBQTerm() ;boolean tomMatch14_123= false ; tom.engine.adt.tomname.types.TomName  tomMatch14_114= null ;if ( (tomMatch14_115 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch14_123= true ;tomMatch14_114= tomMatch14_115.getAstName() ;}} else {if ( (tomMatch14_115 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch14_123= true ;tomMatch14_114= tomMatch14_115.getAstName() ;}}}if (tomMatch14_123) {if ( (tom_aName==tomMatch14_114) ) {tomMatch14_122= true ;}}}if ( tomMatch14__end__105.isEmptyconcBQTerm() ) {tomMatch14__end__105=(( tom.engine.adt.code.types.BQTermList )nkt.varList);} else {tomMatch14__end__105= tomMatch14__end__105.getTailconcBQTerm() ;}}} while(!( (tomMatch14__end__105==(( tom.engine.adt.code.types.BQTermList )nkt.varList)) ));}if (!(tomMatch14_122)) {boolean tomMatch14_120= false ;if ( (((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch14__end__111=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);do {{if (!( tomMatch14__end__111.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch14_117= tomMatch14__end__111.getHeadconcTomTerm() ;boolean tomMatch14_121= false ; tom.engine.adt.tomname.types.TomName  tomMatch14_116= null ;if ( (tomMatch14_117 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch14_121= true ;tomMatch14_116= tomMatch14_117.getAstName() ;}} else {if ( (tomMatch14_117 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch14_121= true ;tomMatch14_116= tomMatch14_117.getAstName() ;}}}if (tomMatch14_121) {if ( (tom_aName==tomMatch14_116) ) {tomMatch14_120= true ;}}}if ( tomMatch14__end__111.isEmptyconcTomTerm() ) {tomMatch14__end__111=(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList);} else {tomMatch14__end__111= tomMatch14__end__111.getTailconcTomTerm() ;}}} while(!( (tomMatch14__end__111==(( tom.engine.adt.tomterm.types.TomList )nkt.varPatternList)) ));}if (!(tomMatch14_120)) {





            nkt.addBQTerm(tom_bqvar);
          }}}}}}}}

        return newBQVar;
      }}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_CollectVars( NewKernelTyper  t0) { return new CollectVars(t0);}



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
  private void inferConstraint(Constraint constraint) {
    {{if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 
        //DEBUG System.out.println("inferConstraint l1 -- subject = " + `subject);
        TomType freshType1 = getUnknownFreshTypeVar();
        TomType freshType2 = getUnknownFreshTypeVar();
        //DEBUG System.out.println("inferConstraint : " + freshType1 + " = " + freshType2);
        addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(freshType1, freshType2) );
        inferTomTerm( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getPattern() ,freshType1);
        inferBQTerm( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getSubject() ,freshType2);
      }}}{if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {


        TomType freshType1 = getUnknownFreshTypeVar();
        TomType freshType2 = getUnknownFreshTypeVar();
        // It will be useful for subtyping
        // TomType freshType3 = getUnknownFreshTypeVar();
        //addConstraint(`Equation(freshType1,getType(left)));
        //DEBUG System.out.println("inferConstraint l1 - typeConstraints = " + typeConstraints);
        //addConstraint(`Equation(freshType2,getType(right)));
        //DEBUG System.out.println("inferConstraint l2 - typeConstraints = " + typeConstraints);
        addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(freshType1, freshType2) );
        inferBQTerm( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getLeft() ,freshType1);
        inferBQTerm( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getRight() ,freshType2);
      }}}{if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyAndConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {


        ConstraintList cList =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadAndConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
        while(!cList.isEmptyconcConstraint()) {
          inferConstraint(cList.getHeadconcConstraint());
          cList = cList.getTailconcConstraint();
        }
      }}}}{if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyOrConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) {


        ConstraintList cList =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadOrConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
        while(!cList.isEmptyconcConstraint()) {
          inferConstraint(cList.getHeadconcConstraint());
          cList = cList.getTailconcConstraint();
        }
      }}}}}

  }

  /**
   * The method <code>inferTomTerm</code> applies rule CT-ANTI, CT-VAR, CT-SVAR, CT-FUN,
   * CT-EMPTY, CT-ELEM, CT-MERGE or CT-STAR to a "pattern" (a TomTerm) in order
   * to infer its type.
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
   * @param tTerm      the TomTerm to be inferred
   * @param freshType the fresh generated previously and attributed to the TomTerm
   */
  private void inferTomTerm(TomTerm tTerm, TomType freshType) {
    //DEBUG System.out.println("inferTomTerm -- tTerm: " + tTerm);
    //DEBUG System.out.println("\n Test pour inferTomTerm. Term = " + tTerm);
    {{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 inferTomTerm( (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getTomTerm() ,freshType); }}}{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch16_7= false ; tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch16_5= null ; tom.engine.adt.tomtype.types.TomType  tomMatch16_4= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch16_7= true ;tomMatch16_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstType() ;tomMatch16_5= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getConstraints() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch16_7= true ;tomMatch16_4= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getAstType() ;tomMatch16_5= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getConstraints() ;}}}if (tomMatch16_7) { tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraintList=tomMatch16_5;


        addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tomMatch16_4, freshType) );  
        //DEBUG System.out.println("InferTomTerm -- constraint = " + `type + " = " + freshType);
        {{if ( (tom_constraintList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch17_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList).getHeadconcConstraint() ;if ( (tomMatch17_4 instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList).getTailconcConstraint() .isEmptyconcConstraint() ) {


            //DEBUG System.out.println("InferTomTerm -- constraint = " +
            //DEBUG     getType(`boundTerm) + " = " + `freshType);
            addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(getType( tomMatch17_4.getVar() ), freshType) ); }}}}}}}

      }}}{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch16_9= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getNameList() ;if ( ((tomMatch16_9 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch16_9 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch16_9.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch16_17= tomMatch16_9.getHeadconcTomName() ;if ( (tomMatch16_17 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomslot.types.SlotList  tom_slotList= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getSlots() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraintList= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getConstraints() ;




        // Do we need to test if the nameList is equal to ""???
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl. tomName = " + `tomName);
        TomSymbol tSymbol = getSymbolFromName( tomMatch16_17.getString() );
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl.
        //tSymbol = " + tSymbol);
  
        {{if ( (tom_constraintList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch18_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList).getHeadconcConstraint() ;if ( (tomMatch18_4 instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList).getTailconcConstraint() .isEmptyconcConstraint() ) {


            //DEBUG System.out.println("InferTomTerm -- constraint = " +
            //DEBUG     getType(`boundTerm) + " = " + freshType);
            addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(getType( tomMatch18_4.getVar() ), freshType) ); }}}}}}}


        TomType codomain = getCodomain(tSymbol);
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl. codomain = " + codomain);
        //DEBUG System.out.println("inferTomTerm: " + `codomain + " = " + freshType);
        addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(codomain, freshType) );
        //DEBUG System.out.println("InferTomTerm -- constraint = " + codomain + " = " + freshType);
        if (!tom_slotList.isEmptyconcSlot()) {
          inferSlotList(tom_slotList,tSymbol,freshType);
        }
      }}}}}}}

  }

  private void inferBQTerm(BQTerm bqTerm, TomType freshType) {
    //DEBUG System.out.println("inferBQTerm -- bqTerm: " + bqTerm);
    {{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch19_3= false ; tom.engine.adt.tomtype.types.TomType  tomMatch19_1= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch19_3= true ;tomMatch19_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch19_3= true ;tomMatch19_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;}}}if (tomMatch19_3) {

        //DEBUG System.out.println("inferBQTerm: " + `aType + " = " + freshType);
        addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tomMatch19_1, freshType) );  
        //DEBUG System.out.println("InferBQTerm -- constraint = " + `aType + " = " + freshType);
      }}}{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch19_6= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ; tom.engine.adt.code.types.BQTermList  tomMatch19_7= (( tom.engine.adt.code.types.BQTerm )bqTerm).getArgs() ;if ( (tomMatch19_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "realMake".equals( tomMatch19_6.getString() ) ) {if ( ((tomMatch19_7 instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || (tomMatch19_7 instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {if (!( tomMatch19_7.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch19_16= tomMatch19_7.getHeadconcBQTerm() ;if ( (tomMatch19_16 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch19_15= tomMatch19_16.getAstName() ;if ( (tomMatch19_15 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "head".equals( tomMatch19_15.getString() ) ) { tom.engine.adt.code.types.BQTermList  tomMatch19_13= tomMatch19_7.getTailconcBQTerm() ;if (!( tomMatch19_13.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch19_21= tomMatch19_13.getHeadconcBQTerm() ;if ( (tomMatch19_21 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch19_20= tomMatch19_21.getAstName() ;if ( (tomMatch19_20 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "tail".equals( tomMatch19_20.getString() ) ) {if (  tomMatch19_13.getTailconcBQTerm() .isEmptyconcBQTerm() ) {






          TomSymbol tSymbol = getSymbolFromName("realMake");
          if (tSymbol == null) {
            tSymbol =
               tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tomMatch19_6,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(freshType, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) , freshType) ,  tom.engine.adt.tomslot.types.pairnamedecllist.EmptyconcPairNameDecl.make() ,  (( tom.engine.adt.code.types.BQTerm )bqTerm).getOption() ) ;
            getSymbolTable().putSymbol("realMake",tSymbol);
          }
        }}}}}}}}}}}}}}}{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch19_26= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;if ( (tomMatch19_26 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTermList  tom_bqTList= (( tom.engine.adt.code.types.BQTerm )bqTerm).getArgs() ;


        //DEBUG System.out.println("\n Test pour inferBQTerm in BQAppl. tomName = " + `tomName);
        TomSymbol tSymbol = getSymbolFromName( tomMatch19_26.getString() );
        //DEBUG System.out.println("\n Test pour inferBQTerm in BQAppl. tSymbol=
        //" + `tSymbol);
        TomType codomain = getCodomain(tSymbol);
        //DEBUG System.out.println("\n Test pour inferBQTerm in BQAppl. codomain = " + codomain + '\n');
        //DEBUG System.out.println("\n Test pour inferBQTerm in BQAppl. freshType = " + freshType + '\n');
        //DEBUG System.out.println("inferBQTerm: " + `codomain + " = " + freshType);
        addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(codomain, freshType) );
        //DEBUG System.out.println("InferBQTerm -- constraint = " + `codomain + " = " + freshType);
        if (!tom_bqTList.isEmptyconcBQTerm()) {
          inferBQTermList(tom_bqTList,tSymbol,freshType);
        }
      }}}}}

  }

  private void inferSlotList(SlotList slotList, TomSymbol tSymbol, TomType freshType) {
    {{if ( (slotList instanceof tom.engine.adt.tomslot.types.SlotList) ) {if ( (((( tom.engine.adt.tomslot.types.SlotList )slotList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )slotList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( (( tom.engine.adt.tomslot.types.SlotList )slotList).isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch20_18= (( tom.engine.adt.tomslot.types.SlotList )slotList).getHeadconcSlot() ;if ( (tomMatch20_18 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tom_tTerm= tomMatch20_18.getAppl() ;if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch20_3= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;if ( (tomMatch20_3 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch20_5= tomMatch20_3.getDomain() ; tom.engine.adt.tomtype.types.TomType  tomMatch20_6= tomMatch20_3.getCodomain() ;if ( ((tomMatch20_5 instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || (tomMatch20_5 instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch20_5.isEmptyconcTomType() )) {if ( (tomMatch20_6 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

        TomType argFreshType = freshType;
        // In case of a list
        if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) {
          TomSymbol argSymb = getSymbolFromTerm(tom_tTerm);
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) {
            /**
             * Continuation of CT-ELEM rule (applying to premises which are
             * not lists):
             * IF found "l(e1,...en,e):AA" and "l:T*->TT" exists in SymbolTable
             * THEN infers type of both sublist "l(e1,...,en)" and last argument
             *      "e" and adds a type constraint "A = T" for the last
             *      argument, where "A" is a fresh type variable  and
             *      "e" does not represent a list with head symbol "l"
             */
            argFreshType = getUnknownFreshTypeVar();
            addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(argFreshType,  tomMatch20_5.getHeadconcTomType() ) );
            //DEBUG System.out.println("InferSlotList CT-ELEM -- constraint = " + argFreshType +
            //DEBUG     " = " + `headTTList);
          }
          
          /**
           * Continuation of CT-STAR rule (applying to premises):
           * IF found "l(e1,...,en,x*):AA" and "l:T*->TT" exists in SymbolTable
           * THEN infers type of both sublist "l(e1,...,en)" and last argument
           *      "x", where "x" represents a list with
           *      head symbol "l"
           */
          TomTerm newTerm = tom_tTerm;
          {{if ( (tom_tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_tTerm) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {

              newTerm =
                 tom.engine.adt.tomterm.types.tomterm.VariableStar.make( (( tom.engine.adt.tomterm.types.TomTerm )tom_tTerm).getOption() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom_tTerm).getAstName() ,  tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol.make( tomMatch20_6.getTomType() ,  tomMatch20_6.getTlType() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ) ,  (( tom.engine.adt.tomterm.types.TomTerm )tom_tTerm).getConstraints() ) ;  
            }}}}

          /**
           * Continuation of CT-MERGE rule (applying to premises):
           * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
           * THEN infers type of both sublist "l(e1,...,en)" and last argument
           *      "e", where "e" represents a list with
           *      head symbol "l"
           */
          inferTomTerm(newTerm,argFreshType);
          inferSlotList( (( tom.engine.adt.tomslot.types.SlotList )slotList).getTailconcSlot() ,tSymbol,freshType);
        } else {
          // In case of a function
          TomType argType;
          TomTerm argTerm;
          TomTypeList symDomain = tomMatch20_5;
          /**
           * Continuation of CT-FUN rule (applying to premises):
           * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
           * THEN infers type of arguments and adds a type constraint "Ai =
           *      Ti" for each argument, where "Ai" is a fresh type variable
           */
          for(Slot arg : slotList.getCollectionconcSlot()) {
            argTerm = arg.getAppl();
            argType = symDomain.getHeadconcTomType();
            argFreshType = getUnknownFreshTypeVar();
            addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(argFreshType, argType) );
            //DEBUG System.out.println("InferSlotList CT-FUN -- constraint = " + argFreshType +
            //DEBUG     " = " + argType);
            inferTomTerm(argTerm,argFreshType);
            symDomain = symDomain.getTailconcTomType();
          }
        }
      }}}}}}}}}}}}

  }

  private void inferBQTermList(BQTermList bqTList, TomSymbol tSymbol, TomType
      freshType) {
matchL:    {{if ( (bqTList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {if (!( (( tom.engine.adt.code.types.BQTermList )bqTList).isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch22_11= (( tom.engine.adt.code.types.BQTermList )bqTList).getHeadconcBQTerm() ;if ( (tomMatch22_11 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch22_10= tomMatch22_11.getAstName() ;if ( (tomMatch22_10 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "head".equals( tomMatch22_10.getString() ) ) { tom.engine.adt.code.types.BQTermList  tomMatch22_8= (( tom.engine.adt.code.types.BQTermList )bqTList).getTailconcBQTerm() ;if (!( tomMatch22_8.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch22_16= tomMatch22_8.getHeadconcBQTerm() ;if ( (tomMatch22_16 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch22_15= tomMatch22_16.getAstName() ;if ( (tomMatch22_15 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "tail".equals( tomMatch22_15.getString() ) ) {if (  tomMatch22_8.getTailconcBQTerm() .isEmptyconcBQTerm() ) {if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomname.types.TomName  tomMatch22_2= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;if ( (tomMatch22_2 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "realMake".equals( tomMatch22_2.getString() ) ) {



          //DEBUG System.out.println("What???? headBQTerm = " + `headBQTerm);
          inferBQTerm( (( tom.engine.adt.code.types.BQTermList )bqTList).getHeadconcBQTerm() ,freshType);
          //DEBUG System.out.println("What???? tailBQTerm = " + `tailBQTerm);
          inferBQTerm( tomMatch22_8.getHeadconcBQTerm() ,freshType);
          break matchL;
        }}}}}}}}}}}}}}}}{if ( (bqTList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )bqTList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {if (!( (( tom.engine.adt.code.types.BQTermList )bqTList).isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tom_bqTerm= (( tom.engine.adt.code.types.BQTermList )bqTList).getHeadconcBQTerm() ;if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch22_23= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;if ( (tomMatch22_23 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch22_25= tomMatch22_23.getDomain() ; tom.engine.adt.tomtype.types.TomType  tomMatch22_26= tomMatch22_23.getCodomain() ;if ( ((tomMatch22_25 instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || (tomMatch22_25 instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch22_25.isEmptyconcTomType() )) {if ( (tomMatch22_26 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {


        TomType argFreshType = freshType;
        // In case of a list
        if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) {
          TomSymbol argSymb = getSymbolFromTerm(tom_bqTerm);
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) {
            argFreshType = getUnknownFreshTypeVar();
            addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(argFreshType,  tomMatch22_25.getHeadconcTomType() ) );
            //DEBUG System.out.println("InferBQTermList -- constraint = " + argFreshType +
            //DEBUG     " = " + `headTTList);
          }
          BQTerm newTerm = tom_bqTerm;
          {{if ( (tom_bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom_bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {

              newTerm =
                 tom.engine.adt.code.types.bqterm.BQVariableStar.make( (( tom.engine.adt.code.types.BQTerm )tom_bqTerm).getOption() ,  (( tom.engine.adt.code.types.BQTerm )tom_bqTerm).getAstName() ,  tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol.make( tomMatch22_26.getTomType() ,  tomMatch22_26.getTlType() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ) ) ;  
            }}}}inferBQTerm(tom_bqTerm,argFreshType)

;
          inferBQTermList( (( tom.engine.adt.code.types.BQTermList )bqTList).getTailconcBQTerm() ,tSymbol,freshType);
        } else {
          // In case of a function
          TomType argType;
          TomTypeList symDomain = tomMatch22_25;
          for(BQTerm arg : bqTList.getCollectionconcBQTerm()) {
            argType = symDomain.getHeadconcTomType();
            argFreshType = getUnknownFreshTypeVar();
            addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(argFreshType, argType) );
            //DEBUG System.out.println("InferBQTermList -- constraint = " + argFreshType +
            //DEBUG     " = " + argType);
            inferBQTerm(arg,argFreshType);
            symDomain = symDomain.getTailconcTomType();
          }
        }
      }}}}}}}}}}}

  }


  public static class solveConstraints extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public solveConstraints( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch24__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{if (!( tomMatch24__end__4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch24_9= tomMatch24__end__4.getHeadconcTypeConstraint() ;if ( (tomMatch24_9 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch24_7= tomMatch24_9.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch24_8= tomMatch24_9.getType2() ;boolean tomMatch24_17= false ; String  tomMatch24_10= "" ;if ( (tomMatch24_7 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch24_17= true ;tomMatch24_10= tomMatch24_7.getTomType() ;}} else {if ( (tomMatch24_7 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch24_17= true ;tomMatch24_10= tomMatch24_7.getTomType() ;}}}if (tomMatch24_17) { String  tom_tName1=tomMatch24_10;if ( (tomMatch24_8 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tomMatch24_12= tomMatch24_8.getTomType() ; String  tom_tName2=tomMatch24_12;boolean tomMatch24_16= false ;if ( tom_tName1.equals(tomMatch24_12) ) {if ( tom_tName2.equals(tomMatch24_12) ) {tomMatch24_16= true ;}}if (!(tomMatch24_16)) {if (!( "unknown type".equals(tom_tName1) )) {if (!( "unknown type".equals(tom_tName2) )) {










        throw new RuntimeException("solveConstraints: failure on " + tomMatch24_7
+ " = " + tomMatch24_8);
      }}}}}}}if ( tomMatch24__end__4.isEmptyconcTypeConstraint() ) {tomMatch24__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch24__end__4= tomMatch24__end__4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch24__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch24__end__22=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{if (!( tomMatch24__end__22.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch24_27= tomMatch24__end__22.getHeadconcTypeConstraint() ;if ( (tomMatch24_27 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch24_25= tomMatch24_27.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch24_26= tomMatch24_27.getType2() ;if ( (tomMatch24_25 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_tName1= tomMatch24_25.getTomType() ;boolean tomMatch24_35= false ; String  tomMatch24_30= "" ;if ( (tomMatch24_26 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch24_35= true ;tomMatch24_30= tomMatch24_26.getTomType() ;}} else {if ( (tomMatch24_26 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch24_35= true ;tomMatch24_30= tomMatch24_26.getTomType() ;}}}if (tomMatch24_35) { String  tom_tName2=tomMatch24_30;boolean tomMatch24_34= false ;if ( tom_tName1.equals(tomMatch24_30) ) {if ( tom_tName2.equals(tomMatch24_30) ) {tomMatch24_34= true ;}}if (!(tomMatch24_34)) {if (!( "unknown type".equals(tom_tName1) )) {if (!( "unknown type".equals(tom_tName2) )) {










        throw new RuntimeException("solveConstraints: failure on " + tomMatch24_25
+ " = " + tomMatch24_26);
      }}}}}}}if ( tomMatch24__end__22.isEmptyconcTypeConstraint() ) {tomMatch24__end__22=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch24__end__22= tomMatch24__end__22.getTailconcTypeConstraint() ;}}} while(!( (tomMatch24__end__22==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch24__end__40=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{if (!( tomMatch24__end__40.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch24_45= tomMatch24__end__40.getHeadconcTypeConstraint() ;if ( (tomMatch24_45 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch24_43= tomMatch24_45.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch24_44= tomMatch24_45.getType2() ;boolean tomMatch24_54= false ; tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch24_46= null ;if ( (tomMatch24_43 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch24_54= true ;tomMatch24_46= tomMatch24_43.getTlType() ;}} else {if ( (tomMatch24_43 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch24_54= true ;tomMatch24_46= tomMatch24_43.getTlType() ;}}}if (tomMatch24_54) {if ( (tomMatch24_46 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {if ( (tomMatch24_44 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch24_50= tomMatch24_44.getTlType() ;if ( (tomMatch24_50 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {if (!( (tomMatch24_50==tomMatch24_46) )) {











        throw new RuntimeException("solveConstraints: failure on " + tomMatch24_43
+ " = " + tomMatch24_44);
      }}}}}}}if ( tomMatch24__end__40.isEmptyconcTypeConstraint() ) {tomMatch24__end__40=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch24__end__40= tomMatch24__end__40.getTailconcTypeConstraint() ;}}} while(!( (tomMatch24__end__40==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch24__end__59=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{if (!( tomMatch24__end__59.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch24_64= tomMatch24__end__59.getHeadconcTypeConstraint() ;if ( (tomMatch24_64 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch24_62= tomMatch24_64.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch24_63= tomMatch24_64.getType2() ;if ( (tomMatch24_62 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch24_65= tomMatch24_62.getTlType() ;if ( (tomMatch24_65 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {boolean tomMatch24_73= false ; tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch24_69= null ;if ( (tomMatch24_63 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch24_73= true ;tomMatch24_69= tomMatch24_63.getTlType() ;}} else {if ( (tomMatch24_63 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch24_73= true ;tomMatch24_69= tomMatch24_63.getTlType() ;}}}if (tomMatch24_73) {if ( (tomMatch24_69 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {if (!( (tomMatch24_69==tomMatch24_65) )) {











        throw new RuntimeException("solveConstraints: failure on " + tomMatch24_62
+ " = " + tomMatch24_63);
      }}}}}}}if ( tomMatch24__end__59.isEmptyconcTypeConstraint() ) {tomMatch24__end__59=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch24__end__59= tomMatch24__end__59.getTailconcTypeConstraint() ;}}} while(!( (tomMatch24__end__59==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch24__end__78=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{if (!( tomMatch24__end__78.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch24_83= tomMatch24__end__78.getHeadconcTypeConstraint() ;if ( (tomMatch24_83 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch24_81= tomMatch24_83.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch24_82= tomMatch24_83.getType2() ;if ( (tomMatch24_81 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) { tom.engine.adt.tomtype.types.TomType  tom_tLType1=tomMatch24_81;if ( (tomMatch24_82 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) { tom.engine.adt.tomtype.types.TomType  tom_tLType2=tomMatch24_82;if (!( (tom_tLType2==tom_tLType1) )) {









          throw new RuntimeException("solveConstraints: failure on " + tom_tLType1
+ " = " + tom_tLType2);
        }}}}}if ( tomMatch24__end__78.isEmptyconcTypeConstraint() ) {tomMatch24__end__78=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch24__end__78= tomMatch24__end__78.getTailconcTypeConstraint() ;}}} while(!( (tomMatch24__end__78==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch24__end__96=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_leftTCList=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch24__end__96, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch24__end__96.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch24_101= tomMatch24__end__96.getHeadconcTypeConstraint() ;if ( (tomMatch24_101 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch24_99= tomMatch24_101.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch24_100= tomMatch24_101.getType2() ;boolean tomMatch24_109= false ; tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch24_102= null ;if ( (tomMatch24_99 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch24_109= true ;tomMatch24_102= tomMatch24_99.getTlType() ;}} else {if ( (tomMatch24_99 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch24_109= true ;tomMatch24_102= tomMatch24_99.getTlType() ;}}}if (tomMatch24_109) {if ( (tomMatch24_102 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch24_99; tom.engine.adt.tomtype.types.TomType  tom_type=tomMatch24_100; tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_rightTCList= tomMatch24__end__96.getTailconcTypeConstraint() ;boolean tomMatch24_108= false ;if ( (tom_typeVar==tomMatch24_100) ) {if ( (tom_type==tomMatch24_100) ) {tomMatch24_108= true ;}}if (!(tomMatch24_108)) {










        nkt.substitutions.put(tom_typeVar,tom_type);
        {{if ( (tom_leftTCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch25_2= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList).isEmptyconcTypeConstraint() ) {tomMatch25_2= true ;}}if (!(tomMatch25_2)) {

            if(nkt.findTypeVars(tom_typeVar,tom_leftTCList)) {
              tom_leftTCList= nkt.applySubstitution(tom_typeVar,tom_type,tom_leftTCList);
            }
          }}}{if ( (tom_rightTCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch25_5= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList).isEmptyconcTypeConstraint() ) {tomMatch25_5= true ;}}if (!(tomMatch25_5)) {


            if(nkt.findTypeVars(tom_typeVar,tom_rightTCList)) {
              tom_rightTCList= nkt.applySubstitution(tom_typeVar,tom_type,tom_rightTCList);
            }
          }}}}

        return tom_append_list_concTypeConstraint(tom_leftTCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_type, tom_type) ,tom_append_list_concTypeConstraint(tom_rightTCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) );
      }}}}}if ( tomMatch24__end__96.isEmptyconcTypeConstraint() ) {tomMatch24__end__96=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch24__end__96= tomMatch24__end__96.getTailconcTypeConstraint() ;}}} while(!( (tomMatch24__end__96==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch24__end__114=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_leftTCList=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch24__end__114, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch24__end__114.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch24_119= tomMatch24__end__114.getHeadconcTypeConstraint() ;if ( (tomMatch24_119 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch24_117= tomMatch24_119.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch24_118= tomMatch24_119.getType2() ;boolean tomMatch24_130= false ; tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch24_120= null ;if ( (tomMatch24_117 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch24_130= true ;tomMatch24_120= tomMatch24_117.getTlType() ;}} else {if ( (tomMatch24_117 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch24_130= true ;tomMatch24_120= tomMatch24_117.getTlType() ;}}}if (tomMatch24_130) { tom.engine.adt.tomtype.types.TomType  tom_type=tomMatch24_117;boolean tomMatch24_129= false ; tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch24_122= null ;if ( (tomMatch24_118 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch24_129= true ;tomMatch24_122= tomMatch24_118.getTlType() ;}} else {if ( (tomMatch24_118 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch24_129= true ;tomMatch24_122= tomMatch24_118.getTlType() ;}}}if (tomMatch24_129) {if ( (tomMatch24_122 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch24_118; tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_rightTCList= tomMatch24__end__114.getTailconcTypeConstraint() ;boolean tomMatch24_128= false ;if ( (tomMatch24_120 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TypeVar) ) {tomMatch24_128= true ;}if (!(tomMatch24_128)) {


























        nkt.substitutions.put(tom_typeVar,tom_type);
        {{if ( (tom_leftTCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch26_2= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_leftTCList).isEmptyconcTypeConstraint() ) {tomMatch26_2= true ;}}if (!(tomMatch26_2)) {

            if(nkt.findTypeVars(tom_typeVar,tom_leftTCList)) {
              tom_leftTCList= nkt.applySubstitution(tom_typeVar,tom_type,tom_leftTCList);
            }
          }}}{if ( (tom_rightTCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch26_5= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_rightTCList).isEmptyconcTypeConstraint() ) {tomMatch26_5= true ;}}if (!(tomMatch26_5)) {


            if(nkt.findTypeVars(tom_typeVar,tom_rightTCList)) {
              tom_rightTCList= nkt.applySubstitution(tom_typeVar,tom_type,tom_rightTCList);
            }
          }}}}

        return tom_append_list_concTypeConstraint(tom_leftTCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_type, tom_type) ,tom_append_list_concTypeConstraint(tom_rightTCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) );
      }}}}}}if ( tomMatch24__end__114.isEmptyconcTypeConstraint() ) {tomMatch24__end__114=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch24__end__114= tomMatch24__end__114.getTailconcTypeConstraint() ;}}} while(!( (tomMatch24__end__114==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}}return _visit_TypeConstraintList(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_solveConstraints( NewKernelTyper  t0) { return new solveConstraints(t0);}



  private boolean findTypeVars(TomType typeVar, TypeConstraintList
      tcList) {
    //DEBUG System.out.println("\n Test pour findTypeVars -- ligne 1.");
    {{if ( (tcList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch27__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList);do {{if (!( tomMatch27__end__4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch27_9= tomMatch27__end__4.getHeadconcTypeConstraint() ;if ( (tomMatch27_9 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {if ( (typeVar== tomMatch27_9.getType1() ) ) {


        return true;
      }if ( (typeVar== tomMatch27_9.getType2() ) ) {         return true;       }}}if ( tomMatch27__end__4.isEmptyconcTypeConstraint() ) {tomMatch27__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList);} else {tomMatch27__end__4= tomMatch27__end__4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch27__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tcList)) ));}}}}

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

  public static class replaceTypeConstraints extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomtype.types.TomType  oldtt;private  tom.engine.adt.tomtype.types.TomType  newtt;public replaceTypeConstraints( tom.engine.adt.tomtype.types.TomType  oldtt,  tom.engine.adt.tomtype.types.TomType  newtt) {super(( new tom.library.sl.Identity() ));this.oldtt=oldtt;this.newtt=newtt;}public  tom.engine.adt.tomtype.types.TomType  getoldtt() {return oldtt;}public  tom.engine.adt.tomtype.types.TomType  getnewtt() {return newtt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if (((( tom.engine.adt.tomtype.types.TomType )tom__arg) == oldtt)) {


          return newtt; 
      }}}}return _visit_TomType(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_replaceTypeConstraints( tom.engine.adt.tomtype.types.TomType  t0,  tom.engine.adt.tomtype.types.TomType  t1) { return new replaceTypeConstraints(t0,t1);}


 
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
    for(String tomName:getSymbolTable().keySymbolIterable()) {
      //DEBUG System.out.println("replaceInSymboltable() - tomName : " + tomName);
      TomType type = getType(tomName);
      {{if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch29_2= (( tom.engine.adt.tomtype.types.TomType )type).getTlType() ; String  tom_typeName= (( tom.engine.adt.tomtype.types.TomType )type).getTomType() ;if ( (tomMatch29_2 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TypeVar) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tom_typeVar=tomMatch29_2;

          if (substitutions.containsKey(tom_typeVar)) {
            type = substitutions.get(tom_typeVar);
          } else {
            System.out.println("\n----- There is no mapping for " + tom_typeVar+'\n');
            type =  tom.engine.adt.tomtype.types.tomtype.Type.make(tom_typeName,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
          }    
          getSymbolTable().putType(tom_typeName,type);
        }}}}}

    }

    for(String tomName:symbolTable.keySymbolIterable()) {
      //DEBUG System.out.println("replaceInSymboltable() - tomName : " + tomName);
      TomSymbol tSymbol = getSymbolFromName(tomName);
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol before strategy: "
      //DEBUG     + tSymbol);
      try {
        tSymbol = tom_make_RepeatId(tom_make_TopDown(tom_make_replaceFreshTypeVar(this))).visitLight(tSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInSymbolTable: failure on " +
            tSymbol);
      }
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol after strategy: "
      //DEBUG     + tSymbol);
      getSymbolTable().putSymbol(tomName,tSymbol);
    }
  }

  public static class replaceFreshTypeVar extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public replaceFreshTypeVar( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar=(( tom.engine.adt.tomtype.types.TomType )tom__arg);if ( (tom_typeVar instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom_typeVar) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( (( tom.engine.adt.tomtype.types.TomType )tom_typeVar).getTlType()  instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TypeVar) ) {


        if (nkt.substitutions.containsKey(tom_typeVar)) {
          return nkt.substitutions.get(tom_typeVar);
        } else {
          System.out.println("\n----- There is no mapping for " + tom_typeVar+'\n');
          return  tom.engine.adt.tomtype.types.tomtype.Type.make( (( tom.engine.adt.tomtype.types.TomType )tom_typeVar).getTomType() ,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ;
        }    
      }}}}}}return _visit_TomType(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_replaceFreshTypeVar( NewKernelTyper  t0) { return new replaceFreshTypeVar(t0);}



  public void printGeneratedConstraints(TypeConstraintList TCList) {
    {{if ( (TCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch31_2= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList).isEmptyconcTypeConstraint() ) {tomMatch31_2= true ;}}if (!(tomMatch31_2)) {
 
        System.out.print("\n------ Type Constraints : \n {");
        printEachConstraint(TCList);
        System.out.print("}");
      }}}}

  }
  public void printEachConstraint(TypeConstraintList TCList) {
    {{if ( (TCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList).isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch32_6= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList).getHeadconcTypeConstraint() ;if ( (tomMatch32_6 instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailTCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )TCList).getTailconcTypeConstraint() ;

        printType( tomMatch32_6.getType1() );
        System.out.print(" = ");
        printType( tomMatch32_6.getType2() );
        if (tom_tailTCList!=  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
            System.out.print(", "); 
            printEachConstraint(tom_tailTCList);
        }
      }}}}}}

  }
    
  public void printType(TomType type) {
    System.out.print(type);
  }
} // NewKernelTyper
