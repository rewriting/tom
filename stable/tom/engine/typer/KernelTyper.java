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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.typer;

import java.util.*;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
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
import tom.engine.adt.typeconstraints.types.*;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class KernelTyper {
          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );       } else {         return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.SequenceId) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) );       } else {         return ( (tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.SequenceId(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin):new tom.library.sl.SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.ChoiceId) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) );       } else {         return ( (tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.ChoiceId(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin):new tom.library.sl.ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)) );   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )) )) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  v) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Choice(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Choice(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) )) )) ;}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ),( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.ChoiceId(v,( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}       private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.typeconstraints.types.MappingList  tom_append_list_concMapping( tom.engine.adt.typeconstraints.types.MappingList l1,  tom.engine.adt.typeconstraints.types.MappingList  l2) {     if( l1.isEmptyconcMapping() ) {       return l2;     } else if( l2.isEmptyconcMapping() ) {       return l1;     } else if(  l1.getTailconcMapping() .isEmptyconcMapping() ) {       return  tom.engine.adt.typeconstraints.types.mappinglist.ConsconcMapping.make( l1.getHeadconcMapping() ,l2) ;     } else {       return  tom.engine.adt.typeconstraints.types.mappinglist.ConsconcMapping.make( l1.getHeadconcMapping() ,tom_append_list_concMapping( l1.getTailconcMapping() ,l2)) ;     }   }   private static   tom.engine.adt.typeconstraints.types.MappingList  tom_get_slice_concMapping( tom.engine.adt.typeconstraints.types.MappingList  begin,  tom.engine.adt.typeconstraints.types.MappingList  end, tom.engine.adt.typeconstraints.types.MappingList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcMapping()  ||  (end== tom.engine.adt.typeconstraints.types.mappinglist.EmptyconcMapping.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.typeconstraints.types.mappinglist.ConsconcMapping.make( begin.getHeadconcMapping() ,( tom.engine.adt.typeconstraints.types.MappingList )tom_get_slice_concMapping( begin.getTailconcMapping() ,end,tail)) ;   }      private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_append_list_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList l1,  tom.engine.adt.typeconstraints.types.TypeConstraintList  l2) {     if( l1.isEmptyconcTypeConstraint() ) {       return l2;     } else if( l2.isEmptyconcTypeConstraint() ) {       return l1;     } else if(  l1.getTailconcTypeConstraint() .isEmptyconcTypeConstraint() ) {       return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,l2) ;     } else {       return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,tom_append_list_concTypeConstraint( l1.getTailconcTypeConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_get_slice_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList  begin,  tom.engine.adt.typeconstraints.types.TypeConstraintList  end, tom.engine.adt.typeconstraints.types.TypeConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeConstraint()  ||  (end== tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( begin.getHeadconcTypeConstraint() ,( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_get_slice_concTypeConstraint( begin.getTailconcTypeConstraint() ,end,tail)) ;   }     









  private TypeConstraintList constraintsToTypeVariable =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
  private SymbolTable symbolTable;
  private int freshTypeVarCounter = 0;

  public KernelTyper() {
    super();
  }

  public SymbolTable getSymbolTable() {
    return this.symbolTable;
  }

  public void setSymbolTable(SymbolTable newSymbolTable) {
    this.symbolTable = newSymbolTable;
  }

  public TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, getSymbolTable());
  }

  /*public*/ protected TomSymbol getSymbolFromType(TomType type) {
    {{if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {

        return TomBase.getSymbolFromType( tom.engine.adt.tomtype.types.tomtype.Type.make( (( tom.engine.adt.tomtype.types.TomType )type).getTomType() ,  (( tom.engine.adt.tomtype.types.TomType )type).getTlType() ) , getSymbolTable()); 
      }}}}

    return TomBase.getSymbolFromType(type, getSymbolTable()); 
  }

  protected TomType getFreshTypeVar() {
    return  tom.engine.adt.tomtype.types.tomtype.TypeVar.make(freshTypeVarCounter++) ;
  }

  public TypeConstraintList getConstraints() {
    return this.constraintsToTypeVariable;
  }

  public void addConstraints(TypeConstraint newConstraint) {
    TypeConstraintList auxList = this.constraintsToTypeVariable;
    this.constraintsToTypeVariable=
       tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(newConstraint,tom_append_list_concTypeConstraint(auxList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ; 
  }

  /**
   * The "typeVariable" phase types RecordAppl into Variable
   * we focus on
   * - Match
   * The types of subjects are inferred from the patterns
   * Variable is typed in the TomTerm case
   */

  public <T extends tom.library.sl.Visitable> T typeVariable(TomType
      contextType, T subject) {
    if(contextType == null) {
      throw new TomRuntimeException("typeVariable: null contextType");
    }
    try {
      //System.out.println("typeVariable: " + contextType);
      //System.out.println("typeVariable subject: " + subject);
      T res =
        tom_make_TopDownStopOnSuccess(tom_make_typeVariable(contextType,this)).visitLight(subject);
      //System.out.println("res: " + res);
      return res;
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeVariable: failure on " + subject);
    }
  }

  public static class typeVariable extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomtype.types.TomType  contextType;private  KernelTyper  kernelTyper;public typeVariable( tom.engine.adt.tomtype.types.TomType  contextType,  KernelTyper  kernelTyper) {super(( new tom.library.sl.Fail() ));this.contextType=contextType;this.kernelTyper=kernelTyper;}public  tom.engine.adt.tomtype.types.TomType  getcontextType() {return contextType;}public  KernelTyper  getkernelTyper() {return kernelTyper;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tom__arg) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) { tom.engine.adt.tomtype.types.TomType  tom_type= (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getVNode() ; tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_constraintInstructionList= (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getAstConstraintInstructionList() ;



        //HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        // Collect all match and numeric match (with explicit declaration of
        // type)
        //`TopDownCollect(CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(`constraintInstructionList);
        TypeConstraintList typeConstraints =
          kernelTyper.reconConstraintInstructionList(tom_type,tom_constraintInstructionList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
        // Call type constraint resolution algorithm
        MappingList listOfMappings =
          kernelTyper.resolveTypeConstraints( tom.engine.adt.typeconstraints.types.mappinglist.EmptyconcMapping.make() ,typeConstraints);
        // Type a list of matchs
        return  tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make(tom_type, kernelTyper.typeConstraintInstructionList(tom_constraintInstructionList,listOfMappings),  (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getOption() ) 
;
      }}}}return _visit_TomVisit(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_constraintInstructionList= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() ;










        TomType newType = contextType;
        //HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        // Collect all match and numeric match (with explicit declaration of
        // type)
        //`TopDownCollect(CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(`constraintInstructionList);
        TypeConstraintList typeConstraints = kernelTyper.reconConstraintInstructionList(newType,tom_constraintInstructionList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
        // Call type constraint resolution algorithm
        MappingList listOfMappings =
          kernelTyper.resolveTypeConstraints( tom.engine.adt.typeconstraints.types.mappinglist.EmptyconcMapping.make() ,typeConstraints);

        // Type a list of matchs
        return
           tom.engine.adt.tominstruction.types.instruction.Match.make(kernelTyper.typeConstraintInstructionList(tom_constraintInstructionList,listOfMappings),  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOption() ) ;
      }}}}return _visit_Instruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TermToInfer  visit_TermToInfer( tom.engine.adt.typeconstraints.types.TermToInfer  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TermToInfer) ) {if ( ((( tom.engine.adt.typeconstraints.types.TermToInfer )tom__arg) instanceof tom.engine.adt.typeconstraints.types.termtoinfer.NewTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tom_tomTerm= (( tom.engine.adt.typeconstraints.types.TermToInfer )tom__arg).getTerm() ; tom.engine.adt.tomtype.types.TomType  tom_typeVar= (( tom.engine.adt.typeconstraints.types.TermToInfer )tom__arg).getTypeVar() ;








        // TODO: to put code to treat constraints, because this is important
        // when using an alias ("@")


        // Type a function or a list
        {{if ( (tom_tomTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch275_2= (( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_option= (( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm).getOption() ;if ( ((tomMatch275_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch275_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tom_nameList=tomMatch275_2;if (!( tomMatch275_2.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch275_10= tomMatch275_2.getHeadconcTomName() ;if ( (tomMatch275_10 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch275_10.getString() ; tom.engine.adt.tomslot.types.SlotList  tom_slotList= (( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm).getSlots() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraints= (( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm).getConstraints() ;

            // Take the symbol with type represented by "contextType", but,
            // why?!? What really is contextType?? Who generates this??
            // Example: in a match, if the type of the subject is known, then
            // this is taken as the contextType to help to infer the type of
            // the pattern 

            TomSymbol tomSymbol = null;
            if(tom_tomName.equals("")) {
              tomSymbol = kernelTyper.getSymbolFromType(contextType);
              if(tomSymbol==null) {
                throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
              } 
              // Add the name found to the name list. But why the "symbolname" (which
              // is equals to "") is not removed of the nameList???
              tom_nameList=  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(tomSymbol.getAstName(), tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) ;
            } else {
              tomSymbol = kernelTyper.getSymbolFromName(tom_tomName);
            }

          /**
           * CT-FUN rule:
           * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
           * THEN infers type of arguments and add a type constraint "A = T" and
           *      calls the TypeVariableList method which adds a type constraint "Ai =
           *      Ti" for each argument, where Ai is a fresh type variable
           *
           * CT-ELEM rule:
           * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
           * THEN infers type of both sublist "l(e1,...,en)" and last argument
           *      "e" and adds a type constraint "AA = TT" and calls the
           *      TypeVariableList method which adds a type constraint "A =T"
           *      for the last argument, where A is a fresh type variable and
           *      "e" does not represent a list with head symbol "l"
           *
           * CT-MERGE rule:
           * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
           * THEN infers type of both sublist "l(e1,...,en)" and last argument
           *      "e" and adds a type constraint "AA = TT" and calls the
           *      TypeVariableList method, where "e" represents a list with
           *      head symbol "l"
           *
           * CT-STAR rule:
           * Equals to CT-MERGE but with a star variable "x*" instead of "e"
           * This rule is necessary because it differed from CT-MERGE in the
           * sense of the type of the last argument ("x*" here) is unknown 
           */
            if(tomSymbol != null) {
              kernelTyper.addConstraints( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_typeVar, TomBase.getSymbolCodomain(tomSymbol)) );
              TomTypeList domainType = TomBase.getSymbolDomain(tomSymbol);
              // Typing the arguments 
              SlotList subterm =
                kernelTyper.typeVariableList(tomSymbol,domainType,tom_slotList);
              //Typing the term after a "@" symbol
              ConstraintList newConstraints =
                kernelTyper.typeVariable(TomBase.getSymbolCodomain(tomSymbol),tom_constraints);
              return
                 tom.engine.adt.typeconstraints.types.termtoinfer.NewTerm.make( tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_option, tom_nameList, subterm, newConstraints) , tom_typeVar) ;
            } else {
              System.out.println("contextType when tomSymbol is 'null' = " + contextType);

              // This case happens when tom takes a java function, since the
              // type of this java function is unknown
              // Then the type is infered by the information of the contextType
              {{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch276_2= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {tomMatch276_2= true ;} else {if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {tomMatch276_2= true ;}}if (tomMatch276_2) {

                  //kernelTyper.addConstraints(`Equation(typeVar,contextType));
                  SlotList subterm =
                    kernelTyper.typeVariableList( tom.engine.adt.tomsignature.types.tomsymbol.emptySymbol.make() , tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ,tom_slotList);
                  ConstraintList newConstraints = kernelTyper.typeVariable((( tom.engine.adt.tomtype.types.TomType )contextType),tom_constraints);
                  return
                     tom.engine.adt.typeconstraints.types.termtoinfer.NewTerm.make( tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_option, tom_nameList, subterm, newConstraints) , tom_typeVar) ;
                }}}}

            }
          }}}}}}{if ( (tom_tomTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch275_19= false ; tom.engine.adt.tomtype.types.TomType  tomMatch275_12= null ; tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch275_13= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch275_19= true ;tomMatch275_12= (( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm).getAstType() ;tomMatch275_13= (( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm).getConstraints() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {{tomMatch275_19= true ;tomMatch275_12= (( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm).getAstType() ;tomMatch275_13= (( tom.engine.adt.tomterm.types.TomTerm )tom_tomTerm).getConstraints() ;}}}if (tomMatch275_19) {if ( (tomMatch275_12 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( tomMatch275_12.getTlType()  instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {










            //The variable will always have a type: a primitive (Type) or an unknown
            //(TypeVar) type (a fresh type variable)
            TomType globalType = kernelTyper.getType( tomMatch275_12.getTomType() );
            kernelTyper.addConstraints( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_typeVar, globalType) );
          }}}}}}

      }}}}return _visit_TermToInfer(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  _visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomsignature.types.TomVisit )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TermToInfer  _visit_TermToInfer( tom.engine.adt.typeconstraints.types.TermToInfer  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.typeconstraints.types.TermToInfer )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {return ((T)visit_TomVisit((( tom.engine.adt.tomsignature.types.TomVisit )v),introspector));}if ( (v instanceof tom.engine.adt.typeconstraints.types.TermToInfer) ) {return ((T)visit_TermToInfer((( tom.engine.adt.typeconstraints.types.TermToInfer )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_typeVariable( tom.engine.adt.tomtype.types.TomType  t0,  KernelTyper  t1) { return new typeVariable(t0,t1);}



  /**
   * ConstraintInstructionList
   * @param contextType
   * @param constraintInstructionList a list of ConstraintInstruction
   * @param matchAndNumericConstraints a collection of MatchConstraint and NumericConstraint
   * @param typeConstraints a list of TypeConstraint to by sended for the type constraint
   * resolution algorithm
   */
  private TypeConstraintList reconConstraintInstructionList(TomType
      contextType, ConstraintInstructionList constraintInstructionList,
      TypeConstraintList typeConstraints) {
    {{if ( (constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) {if ( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).isEmptyconcConstraintInstruction() ) {

        return typeConstraints;
      }}}}{if ( (constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) {if (!( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).isEmptyconcConstraintInstruction() )) { tom.engine.adt.tominstruction.types.ConstraintInstruction  tomMatch277_9= (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).getHeadconcConstraintInstruction() ;if ( (tomMatch277_9 instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {


        try {
          /**
           * CT-RULE rule:
           * IF found "(constraint -> (e1,...,en)):wt" and "forall ei, ei has
           * type Ai" in SymbolTable
           * THEN calls the reconConstraint method to infer types of the variables in
           * the constraint and calls the typeVariable to set types Ai for each ei
           */
          Collection<TomTerm> lhsVariable = new HashSet<TomTerm>();
          // 1 - Infer types of a condition and collect both variables and type
          // constraints
          tom_make_TopDownStopOnSuccess(tom_make_reconConstraint(contextType,lhsVariable,typeConstraints,this)).visitLight( tomMatch277_9.getConstraint() );
          TomList varList = ASTFactory.makeTomList(lhsVariable);
          // 2 - If a variable 'x' is instantiated by 'a()', replace all variables 'x'
          // found in the code succeeding by 'a()' 
          Instruction newAction = (Instruction) replaceInstantiatedVariable(varList, tomMatch277_9.getAction() );
          // 3 - Infer types of an action
          newAction = typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,newAction);
          // 4 - Infer types for the rest of the rules (constraint -> action)
          TypeConstraintList newTypeConstraints =
            reconConstraintInstructionList(contextType,  (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).getTailconcConstraintInstruction() , typeConstraints);
          // 5 - Return all collected type constraints 
          return tom_append_list_concTypeConstraint(newTypeConstraints,tom_append_list_concTypeConstraint(typeConstraints, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ));
        } catch(VisitFailure e) {
          System.out.println("Error in 'reconConstraintInstructionList'");
        }
      }}}}}}

    throw new TomRuntimeException("Bad ConstraintInstruction: " + constraintInstructionList);
 }

  // TOCHECK
  public static class reconConstraint extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomtype.types.TomType  contextType;private  java.util.Collection  lhsVariable;private  tom.engine.adt.typeconstraints.types.TypeConstraintList  typeConstraints;private  KernelTyper  kernelTyper;public reconConstraint( tom.engine.adt.tomtype.types.TomType  contextType,  java.util.Collection  lhsVariable,  tom.engine.adt.typeconstraints.types.TypeConstraintList  typeConstraints,  KernelTyper  kernelTyper) {super(( new tom.library.sl.Fail() ));this.contextType=contextType;this.lhsVariable=lhsVariable;this.typeConstraints=typeConstraints;this.kernelTyper=kernelTyper;}public  tom.engine.adt.tomtype.types.TomType  getcontextType() {return contextType;}public  java.util.Collection  getlhsVariable() {return lhsVariable;}public  tom.engine.adt.typeconstraints.types.TypeConstraintList  gettypeConstraints() {return typeConstraints;}public  KernelTyper  getkernelTyper() {return kernelTyper;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {


}}}{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {


        //lundi: il faut que j'appele la fonction getTypeBQTermInConstraint pour
        //recuperer le type et pouvoir ajouter la contrainte lhsTermType =
        //rhsTermType

        //TomType typeVar = kernelTyper.getFreshTypeVar();
        //kernelTyper.addConstraints(`Equation(typeVar,globalType));
        BQTerm newLhs = kernelTyper.typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,  (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getLeft() );                  
        BQTerm newRhs = kernelTyper.typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,  (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getRight() );                  
        return (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).setLeft(newLhs).setRight(newRhs);               
      }}}}return _visit_Constraint(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_reconConstraint( tom.engine.adt.tomtype.types.TomType  t0,  java.util.Collection  t1,  tom.engine.adt.typeconstraints.types.TypeConstraintList  t2,  KernelTyper  t3) { return new reconConstraint(t0,t1,t2,t3);}



  private TomType getTypeBQTermInConstraint(BQTerm bqTerm) {
    {{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch279_5= false ; tom.engine.adt.tomtype.types.TomType  tomMatch279_3= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch279_1= null ; tom.engine.adt.tomname.types.TomName  tomMatch279_2= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch279_5= true ;tomMatch279_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getOption() ;tomMatch279_2= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;tomMatch279_3= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch279_5= true ;tomMatch279_1= (( tom.engine.adt.code.types.BQTerm )bqTerm).getOption() ;tomMatch279_2= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstName() ;tomMatch279_3= (( tom.engine.adt.code.types.BQTerm )bqTerm).getAstType() ;}}}if (tomMatch279_5) {

        return tomMatch279_3;
      }}}{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {


        //lundi: il faut creer une fonction pour recuperer le tom type 
        //return term.getType(astName);
      }}}}

      throw new TomRuntimeException("Bad term in constraint: " + bqTerm);
  }

  /**
   * Replace variables that are already instantiated before. So, this happens
   * when having a %match inside the action (right hand-side) of another
   * external %match and both use variables with the same name
   */
  private tom.library.sl.Visitable replaceInstantiatedVariable(TomList instantiatedVariable, tom.library.sl.Visitable subject) {
    try {
      return tom_make_TopDownStopOnSuccess(tom_make_replaceInstantiatedVariable(instantiatedVariable)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInstantiatedVariable: failure on " + instantiatedVariable);
    }
  }

  public static class replaceInstantiatedVariable extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomterm.types.TomList  instantiatedVariable;public replaceInstantiatedVariable( tom.engine.adt.tomterm.types.TomList  instantiatedVariable) {super(( new tom.library.sl.Fail() ));this.instantiatedVariable=instantiatedVariable;}public  tom.engine.adt.tomterm.types.TomList  getinstantiatedVariable() {return instantiatedVariable;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tom_subject=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);{{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch281_2= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getNameList() ; tom.engine.adt.tomslot.types.SlotList  tomMatch281_3= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getSlots() ;if ( ((tomMatch281_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch281_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch281_2.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tom_opNameAST= tomMatch281_2.getHeadconcTomName() ;if (  tomMatch281_2.getTailconcTomName() .isEmptyconcTomName() ) {if ( ((tomMatch281_3 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch281_3 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if ( tomMatch281_3.isEmptyconcSlot() ) {if ( (instantiatedVariable instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch281__end__11=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);do {{if (!( tomMatch281__end__11.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch281_15= tomMatch281__end__11.getHeadconcTomTerm() ;boolean tomMatch281_17= false ; tom.engine.adt.tomname.types.TomName  tomMatch281_14= null ;if ( (tomMatch281_15 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch281_17= true ;tomMatch281_14= tomMatch281_15.getAstName() ;}} else {if ( (tomMatch281_15 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch281_17= true ;tomMatch281_14= tomMatch281_15.getAstName() ;}}}if (tomMatch281_17) {if ( (tom_opNameAST==tomMatch281_14) ) {




            System.out.println("RecordAppl, opNameAST = " + tom_opNameAST);
            return  tomMatch281__end__11.getHeadconcTomTerm() ;
          }}}if ( tomMatch281__end__11.isEmptyconcTomTerm() ) {tomMatch281__end__11=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);} else {tomMatch281__end__11= tomMatch281__end__11.getTailconcTomTerm() ;}}} while(!( (tomMatch281__end__11==(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable)) ));}}}}}}}}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tom_opNameAST= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstName() ;if ( (instantiatedVariable instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch281__end__25=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);do {{if (!( tomMatch281__end__25.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch281_29= tomMatch281__end__25.getHeadconcTomTerm() ;boolean tomMatch281_31= false ; tom.engine.adt.tomname.types.TomName  tomMatch281_28= null ;if ( (tomMatch281_29 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch281_31= true ;tomMatch281_28= tomMatch281_29.getAstName() ;}} else {if ( (tomMatch281_29 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch281_31= true ;tomMatch281_28= tomMatch281_29.getAstName() ;}}}if (tomMatch281_31) {if ( (tom_opNameAST==tomMatch281_28) ) {

            System.out.println("Variable, opNameAST = " + tom_opNameAST);
            return  tomMatch281__end__25.getHeadconcTomTerm() ;
          }}}if ( tomMatch281__end__25.isEmptyconcTomTerm() ) {tomMatch281__end__25=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);} else {tomMatch281__end__25= tomMatch281__end__25.getTailconcTomTerm() ;}}} while(!( (tomMatch281__end__25==(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable)) ));}}}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tom_opNameAST= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstName() ;if ( (instantiatedVariable instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch281__end__39=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);do {{if (!( tomMatch281__end__39.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch281_43= tomMatch281__end__39.getHeadconcTomTerm() ;if ( (tomMatch281_43 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( (tom_opNameAST== tomMatch281_43.getAstName() ) ) {

            System.out.println("VariableStar, opNameAST = " + tom_opNameAST);
            return  tomMatch281__end__39.getHeadconcTomTerm() ;
          }}}if ( tomMatch281__end__39.isEmptyconcTomTerm() ) {tomMatch281__end__39=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);} else {tomMatch281__end__39= tomMatch281__end__39.getTailconcTomTerm() ;}}} while(!( (tomMatch281__end__39==(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable)) ));}}}}}}

      }}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_replaceInstantiatedVariable( tom.engine.adt.tomterm.types.TomList  t0) { return new replaceInstantiatedVariable(t0);}



  // TOCHECK
  private MappingList resolveTypeConstraints(MappingList
      emptyMapping,TypeConstraintList typeConstraints) {
    return  tom.engine.adt.typeconstraints.types.mappinglist.EmptyconcMapping.make() ;
  }

  // TOCHECK
  private ConstraintInstructionList
    typeConstraintInstructionList(ConstraintInstructionList
        constraintInstructionList, MappingList listOfMappings) {
    return  tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ;
    }

  /**
   * If a variable with a type X is found, then all the variables that have the same name and 
   * with type 'unknown' get this type
   *  - apply this for each rhs
   */
  // TOCHECK
  protected Code propagateVariablesTypes(Code workingTerm){
    try{
      return tom_make_TopDown(tom_make_ProcessRhsForVarTypePropagation()).visitLight(workingTerm);  
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("propagateVariablesTypes: failure on " + workingTerm);
    }
  }

  // TOCHECK
  public static class ProcessRhsForVarTypePropagation extends tom.library.sl.AbstractStrategyBasic {public ProcessRhsForVarTypePropagation() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) { tom.engine.adt.tomconstraint.types.Constraint  tom_constr= (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getConstraint() ;


        HashMap<String,TomType> varTypes = new HashMap<String,TomType>();
        tom_make_TopDown(tom_make_CollectAllVariablesTypes(varTypes)).visitLight(tom_constr);        
        Constraint c = tom_make_TopDown(tom_make_PropagateVariablesTypes(varTypes)).visitLight(tom_constr);        
        return  tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(c,  (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getAction() ,  (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getOption() ) ;
      }}}}return _visit_ConstraintInstruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.ConstraintInstruction  _visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.ConstraintInstruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {return ((T)visit_ConstraintInstruction((( tom.engine.adt.tominstruction.types.ConstraintInstruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_ProcessRhsForVarTypePropagation() { return new ProcessRhsForVarTypePropagation();}

  

  // TOCHECK
  public static class CollectAllVariablesTypes extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashMap  map;public CollectAllVariablesTypes( java.util.HashMap  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.HashMap  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch283_3= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;if ( (tomMatch283_3 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomtype.types.TomType  tom_type= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;if ( (tom_type instanceof tom.engine.adt.tomtype.types.TomType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch283_freshSubject_1=(( tom.engine.adt.tomtype.types.TomType )tom_type);if ( (tom_type instanceof tom.engine.adt.tomtype.types.TomType) ) {tomMatch283_freshSubject_1=(( tom.engine.adt.tomtype.types.TomType )tom_type);boolean tomMatch283_11= false ;if ( (tomMatch283_freshSubject_1 instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {tomMatch283_11= true ;}if (!(tomMatch283_11)) {boolean tomMatch283_10= false ;if ( (tomMatch283_freshSubject_1 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch283_10= true ;}if (!(tomMatch283_10)) {




          map.put( tomMatch283_3.getString() ,tom_type);
      }}}}}}}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_CollectAllVariablesTypes( java.util.HashMap  t0) { return new CollectAllVariablesTypes(t0);}



  // TOCHECK
  public static class PropagateVariablesTypes extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashMap  map;public PropagateVariablesTypes( java.util.HashMap  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.HashMap  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch284_3= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;if ( (tomMatch284_3 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch284_3.getString() ; tom.engine.adt.tomtype.types.TomType  tom_type= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() ;if ( (tom_type instanceof tom.engine.adt.tomtype.types.TomType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch284_freshSubject_1=(( tom.engine.adt.tomtype.types.TomType )tom_type);if ( (tom_type instanceof tom.engine.adt.tomtype.types.TomType) ) {tomMatch284_freshSubject_1=(( tom.engine.adt.tomtype.types.TomType )tom_type);if ( (tomMatch284_freshSubject_1 instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {if ( (tomMatch284_freshSubject_1 instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {




        if (map.containsKey(tom_name)) {
          return (( tom.engine.adt.code.types.BQTerm )tom__arg).setAstType((TomType)map.get(tom_name)); 
        }
      }}}}}}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_PropagateVariablesTypes( java.util.HashMap  t0) { return new PropagateVariablesTypes(t0);}


  
  private TomType getType(String tomName) {
    TomType tomType = getSymbolTable().getType(tomName);
    return tomType;
  }

  /**
   * Collect the constraints (match and numeric)
   * NumericConstraint are %match with explicit type declaration
   */
  /*
  %strategy CollectMatchAndNumericConstraints(constrList:Collection) extends
    Fail() {
    visit Constraint {
      c@(MatchConstraint|NumericConstraint)[] -> {        
        constrList.add(`c);
      }      
    }
  }
*/
  private TomSymbol findSymbol(TomType contextType, String symbolName) {
    TomSymbol tomSymbol = null;
    if(symbolName.equals("")) {
      tomSymbol = this.getSymbolFromType(contextType);
      if(tomSymbol==null) {
        throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
      } 
      // Add the name found to the name list. But why the "symbolname" (which
      // is equals to "") is not removed of the nameList???
    } else {
      tomSymbol = this.getSymbolFromName(symbolName);
    }
    return tomSymbol;
  }

  private SlotList typeVariableList(TomSymbol symbol, TomTypeList domainType, SlotList subtermList) {
    if(symbol == null) {
      throw new TomRuntimeException("typeVariableList: null symbol");
    }

    if(subtermList.isEmptyconcSlot()) {
      return  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
    }

    {{if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.emptySymbol) ) {if ( (domainType instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )domainType) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )domainType) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( (( tom.engine.adt.tomtype.types.TomTypeList )domainType).isEmptyconcTomType() )) {if ( (subtermList instanceof tom.engine.adt.tomslot.types.SlotList) ) {if ( (((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( (( tom.engine.adt.tomslot.types.SlotList )subtermList).isEmptyconcSlot() )) {if ( ( (( tom.engine.adt.tomslot.types.SlotList )subtermList).getHeadconcSlot()  instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {



        /*
         * if the top symbol is unknown, the subterms
         * are typed in an empty context
         */
        SlotList sl = typeVariableList((( tom.engine.adt.tomsignature.types.TomSymbol )symbol), (( tom.engine.adt.tomtype.types.TomTypeList )domainType).getTailconcTomType() , (( tom.engine.adt.tomslot.types.SlotList )subtermList).getTailconcSlot() );
        TomType typeVar = this.getFreshTypeVar();
        this.addConstraints( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(typeVar,  (( tom.engine.adt.tomtype.types.TomTypeList )domainType).getHeadconcTomType() ) );
        //return
        //  `concSlot(PairSlotAppl(slotName,(TomTerm)typeVariable(EmptyType(),NewTerm(slotAppl,typeVar))),sl*);
      }}}}}}}}}}{if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch285_17= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getTypesToType() ;if ( (tomMatch285_17 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch285_20= tomMatch285_17.getCodomain() ;if ( (tomMatch285_20 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tom_codomain=tomMatch285_20; tom.engine.adt.tomsignature.types.TomSymbol  tom_symb=(( tom.engine.adt.tomsignature.types.TomSymbol )symbol);if ( (domainType instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )domainType) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )domainType) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( (( tom.engine.adt.tomtype.types.TomTypeList )domainType).isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom_firstDomainType= (( tom.engine.adt.tomtype.types.TomTypeList )domainType).getHeadconcTomType() ;if ( (subtermList instanceof tom.engine.adt.tomslot.types.SlotList) ) {if ( (((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( (( tom.engine.adt.tomslot.types.SlotList )subtermList).isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch285_33= (( tom.engine.adt.tomslot.types.SlotList )subtermList).getHeadconcSlot() ;if ( (tomMatch285_33 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tom_slotAppl= tomMatch285_33.getAppl() ; tom.engine.adt.tomslot.types.SlotList  tom_tail2= (( tom.engine.adt.tomslot.types.SlotList )subtermList).getTailconcSlot() ;



          TomType typeVar = this.getFreshTypeVar();
          if(TomBase.isListOperator(tom_symb) || TomBase.isArrayOperator(tom_symb)) {
            /*
             * todo
             * when the symbol is an associative operator,
             * the signature has the form: list conc( element* )
             * the list of types is reduced to the singleton { element }
             *
             * consider a pattern: conc(e1*,x,e2*,y,e3*)
             * assign the type "element" to each subterm: x and y
             * assign the type "list" to each subtermlist: e1*,e2* and e3*
             * assign the type "list" to each subtermlist: conc(...)
             */
            {{if ( (tom_slotAppl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {









                this.addConstraints( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(typeVar, tom_codomain) );
                SlotList sl = typeVariableList(tom_symb,domainType,tom_tail2);
                return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tomMatch285_33.getSlotName() ,  tom.engine.adt.tomterm.types.tomterm.VariableStar.make( (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getOption() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getAstName() ,  tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol.make( tomMatch285_20.getTomType() ,  tomMatch285_20.getTlType() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getAstName() ) ,  (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getConstraints() ) ) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
              }}}{if ( (tom_slotAppl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {


                this.addConstraints( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(typeVar, tom_codomain) );
                SlotList sl = typeVariableList(tom_symb,domainType,tom_tail2);
                //return `concSlot(PairSlotAppl(slotName,UnamedVariableStar(option,codomain,constraints)),sl*);
              }}}{if ( (tom_slotAppl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch286_11= (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getNameList() ;if ( ((tomMatch286_11 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch286_11 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch286_11.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch286_19= tomMatch286_11.getHeadconcTomName() ;if ( (tomMatch286_19 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch286_16= tomMatch286_11.getTailconcTomName() ;if (!( tomMatch286_16.isEmptyconcTomName() )) {if (  tomMatch286_16.getTailconcTomName() .isEmptyconcTomName() ) {










                TomSymbol tomSymbol = this.findSymbol(tom_firstDomainType, tomMatch286_19.getString() );
                if (tom_symb== tomSymbol) {
                  SlotList sl = typeVariableList(tom_symb,domainType,tom_tail2);
                  //return 
                  //  `concSlot(PairSlotAppl(slotName,(TomTerm)typeVariable(firstDomainType,NewTerm(slotAppl,typeVar))),sl*);
                }
              }}}}}}}}{if ( (tom_slotAppl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {











                this.addConstraints( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(typeVar, tom_firstDomainType) );
                SlotList sl = typeVariableList(tom_symb,domainType,tom_tail2);
                //return 
                //  `concSlot(PairSlotAppl(slotName,(TomTerm)typeVariable(firstDomainType,NewTerm(slotAppl,typeVar))),sl*);
              }}}

          } 
          /*
           * Continuation of CT-FUN rule (applying to premises):
           * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
           * THEN infers type of arguments and adds a type constraint "Ai =
           *      Ti" for each argument, where "Ai" is a fresh type variable
           */
          else {
            this.addConstraints( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(typeVar, tom_firstDomainType) );
            SlotList sl = typeVariableList(tom_symb, (( tom.engine.adt.tomtype.types.TomTypeList )domainType).getTailconcTomType() ,tom_tail2);
            //return
            //  `concSlot(PairSlotAppl(slotName,(TomTerm)typeVariable(TomBase.getSlotType(symb,slotName),NewTerm(slotAppl,typeVar))),sl*);
          }
        }}}}}}}}}}}}}

    throw new TomRuntimeException("typeVariableList: strange case: '" + symbol + "'");
  }
} 
