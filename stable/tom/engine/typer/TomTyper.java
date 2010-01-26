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

package tom.engine.typer;

import java.util.Map;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.ArrayList;

import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;
import tom.engine.adt.code.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;
import tom.engine.xml.Constants;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

import tom.library.sl.*;

/**
 * The TomTyper plugin.
 * Perform syntax expansion and more.
 */
public class TomTyper extends TomGenericPlugin {

        private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );       } else {         return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.SequenceId) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) );       } else {         return ( (tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.SequenceId(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin):new tom.library.sl.SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.ChoiceId) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) );       } else {         return ( (tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.ChoiceId(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin):new tom.library.sl.ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)) );   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )) )) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  v) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownIdStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?v:new tom.library.sl.ChoiceId(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) )) )) ;}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ),( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.ChoiceId(v,( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}  





  /** some output suffixes */
  public static final String TYPED_SUFFIX       = ".tfix.typed";
  public static final String TYPED_TABLE_SUFFIX = ".tfix.typed.table";

  /** the declared options string */
  public static final String DECLARED_OPTIONS =
    "<options>" +
    "<boolean name='type' altName='' description='TomTyper (activated by default)' value='true'/>" +
    "</options>";

  /** the kernel typer acting at very low level */
  private KernelTomTyper kernelTomTyper;

  /** Constructor*/
  public TomTyper() {
    super("TomTyper");
    kernelTomTyper = new KernelTomTyper();
  }

  /**
   * The run() method performs expansion for tom syntax, variables,...
   */
  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    //System.out.println("(debug) I'm in the Tom typer : TSM"+getStreamManager().toString());
    
    //TomTerm typedTerm = null;
    Code typedCode = null;
    try {
      kernelTomTyper.setSymbolTable(getStreamManager().getSymbolTable());
      //no more necessary: realised by the desugarer
      //Code syntaxExpandedTerm = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this)).visitLight((Code)getWorkingTerm());

      updateSymbolTable();

      Code syntaxExpandedCode = expandType((Code)getWorkingTerm());
//System.out.println("(DEBUG) TomTyper --------------------------- 1 ");
//System.out.println("(DEBUG) TomTyper / SymbolTable, before =\n" + getStreamManager().getSymbolTable().getMapSymbolName());
//System.out.println("(DEBUG) TomTyper --------------------------- 2 ");
      Code variableExpandedCode = (Code) kernelTomTyper.typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() , syntaxExpandedCode);
//System.out.println("(DEBUG) TomTyper --------------------------- 3 ");
//System.out.println("(DEBUG) TomTyper / SymbolTable, after =\n" + getStreamManager().getSymbolTable().getMapSymbolName());
//System.out.println("(DEBUG) TomTyper --------------------------- 4 ");
     
//            System.out.println("(DEBUG) TomTyper / after expandType and typeVariable / ");
      Code stringExpandedCode = tom_make_TopDownIdStopOnSuccess(tom_make_typeString(this)).visitLight(variableExpandedCode);
//            System.out.println("(DEBUG) TomTyper / 1");
      typedCode = tom_make_TopDownIdStopOnSuccess(tom_make_updateCodomain(this)).visitLight(stringExpandedCode);
//            System.out.println("(DEBUG) TomTyper / 2");
      typedCode = kernelTomTyper.propagateVariablesTypes(typedCode);

//            System.out.println("(DEBUG) TomTyper / after propagate");
      /* transform each BackQuoteTerm into its compiled form */
      typedCode = tom_make_TopDownIdStopOnSuccess(tom_make_typeBQAppl(this)).visitLight(typedCode);
      
//            System.out.println("(DEBUG) TomTyper / before setWorkingTerm");
      setWorkingTerm(typedCode);      
//            System.out.println("(DEBUG) TomTyper / after setWorkingTerm ");
      // verbose
      getLogger().log(Level.INFO, TomMessage.tomTypingPhase.getMessage(),
          new Integer((int)(System.currentTimeMillis()-startChrono)));
    } catch (Exception e) {
      getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{getClass().getName(), getStreamManager().getInputFileName(), e.getMessage()} );
      e.printStackTrace();
      return;
    }
    if(intermediate) {
      Tools.generateOutput(getStreamManager().getOutputFileName()
          + TYPED_SUFFIX, typedCode);
      Tools.generateOutput(getStreamManager().getOutputFileName()
          + TYPED_TABLE_SUFFIX, symbolTable().toTerm());
    }
  }

  /*
   * Replace a TomTypeAlone by its expanded form (TomType)
   */
  private Code expandType(Code subject) {
    try {
      return tom_make_TopDownIdStopOnSuccess(tom_make_expandType(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeType: failure on " + subject);
    }
  }

  /*
   * Replace a TomTypeAlone by its expanded form (TomType)
   */
  private TomTerm expandType(TomTerm subject) {
    try {
      return tom_make_TopDownIdStopOnSuccess(tom_make_expandType(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeType: failure on " + subject);
    }
  }

  public static class expandType extends tom.library.sl.AbstractStrategyBasic {private  TomTyper  typer;public expandType( TomTyper  typer) {super(( new tom.library.sl.Identity() ));this.typer=typer;}public  TomTyper  gettyper() {return typer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTlType()  instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {


        TomType type = typer.symbolTable().getType( (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() );
        if(type != null) {
          return type;
        } else {
          return (( tom.engine.adt.tomtype.types.TomType )tom__arg); // useful for SymbolTable.TYPE_UNKNOWN
        }
      }}}}}return _visit_TomType(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_expandType( TomTyper  t0) { return new expandType(t0);}



  /*
   * updateSymbol is called after a first syntax expansion phase
   * this phase updates the symbolTable according to the typeTable
   * this is performed by recursively traversing each symbol
   * - backquote are typed
   * - each TomTypeAlone is replaced by the corresponding TomType
   * - default IsFsymDecl and MakeDecl are added
   */
  public void updateSymbolTable() {
    SymbolTable symbolTable = getStreamManager().getSymbolTable();
    Iterator<String> it = symbolTable.keySymbolIterator();
    //Strategy typeStrategy = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this));

    while(it.hasNext()) {
      String tomName = it.next();
      TomSymbol tomSymbol = getSymbolFromName(tomName);
      /*
       * add default IsFsymDecl unless it is a builtin type
       * add default IsFsymDecl and MakeDecl unless:
       *  - it is a builtin type
       *  - another option (if_sfsym, get_slot, etc) is already defined for this operator
       */
      if(!getStreamManager().getSymbolTable().isBuiltinType(TomBase.getTomType(TomBase.getSymbolCodomain(tomSymbol)))) {
        tomSymbol = addDefaultMake(tomSymbol);
        tomSymbol = addDefaultIsFsym(tomSymbol);
      }
      try {
        tomSymbol = tom_make_TopDownIdStopOnSuccess(tom_make_typeTermApplTomSyntax(this)).visitLight(tomSymbol);
        tomSymbol = expandType( tom.engine.adt.tomterm.types.tomterm.TomSymbolToTomTerm.make(tomSymbol) ).getAstSymbol();
//System.out.println("(DEBUG) TomTyper in updateSymbolTable() --------------------------- 1 ");
//System.out.println("(DEBUG) TomTyper / SymbolTable, before =\n" + getStreamManager().getSymbolTable().getMapSymbolName());
//System.out.println("(DEBUG) TomTyper in updateSymbolTable() --------------------------- 2 ");
        tomSymbol = ((TomTerm) kernelTomTyper.typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() , tom.engine.adt.tomterm.types.tomterm.TomSymbolToTomTerm.make(tomSymbol) )).getAstSymbol();
//System.out.println("(DEBUG) TomTyper in updateSymbolTable() --------------------------- 3 ");
//System.out.println("(DEBUG) TomTyper / SymbolTable, after =\n" + getStreamManager().getSymbolTable().getMapSymbolName());
//System.out.println("(DEBUG) TomTyper in updateSymbolTable() --------------------------- 4 ");
        tomSymbol = tom_make_TopDownIdStopOnSuccess(tom_make_typeBQAppl(this)).visitLight(tomSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  private TomSymbol addDefaultIsFsym(TomSymbol tomSymbol) {
    {{if ( (tomSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch309_1= (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getOption() ;if ( ((tomMatch309_1 instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || (tomMatch309_1 instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch309__end__6=tomMatch309_1;do {{if (!( tomMatch309__end__6.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch309_10= tomMatch309__end__6.getHeadconcOption() ;if ( (tomMatch309_10 instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) {if ( ( tomMatch309_10.getAstDeclaration()  instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) {

        return tomSymbol;
      }}}if ( tomMatch309__end__6.isEmptyconcOption() ) {tomMatch309__end__6=tomMatch309_1;} else {tomMatch309__end__6= tomMatch309__end__6.getTailconcOption() ;}}} while(!( (tomMatch309__end__6==tomMatch309_1) ));}}}}{if ( (tomSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch309_14= (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getTypesToType() ; tom.engine.adt.tomoption.types.OptionList  tomMatch309_16= (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getOption() ; tom.engine.adt.tomname.types.TomName  tom_name= (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getAstName() ;if ( (tomMatch309_14 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {if ( ((tomMatch309_16 instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || (tomMatch309_16 instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch309__end__24=tomMatch309_16;do {{if (!( tomMatch309__end__24.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch309_30= tomMatch309__end__24.getHeadconcOption() ;if ( (tomMatch309_30 instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) { int  tom_line= tomMatch309_30.getLine() ; String  tom_file= tomMatch309_30.getFileName() ;

        Declaration isfsym =  tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl.make(tom_name,  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("t") , tom_line, tom_file) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("t") ,  tomMatch309_14.getCodomain() ) ,  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("is_fsym") , tom_line, tom_file) ) ;
        return  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_name, tomMatch309_14,  (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getPairNameDeclList() , tom_append_list_concOption(tom_get_slice_concOption(tomMatch309_16,tomMatch309__end__24, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ), tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tomMatch309__end__24.getHeadconcOption() , tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(isfsym) ,tom_append_list_concOption( tomMatch309__end__24.getTailconcOption() , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ) )) ;
      }}if ( tomMatch309__end__24.isEmptyconcOption() ) {tomMatch309__end__24=tomMatch309_16;} else {tomMatch309__end__24= tomMatch309__end__24.getTailconcOption() ;}}} while(!( (tomMatch309__end__24==tomMatch309_16) ));}}}}}}

    return tomSymbol;
  }

  private TomSymbol addDefaultMake(TomSymbol tomSymbol) {
    {{if ( (tomSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch310_1= (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getOption() ;if ( ((tomMatch310_1 instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || (tomMatch310_1 instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch310__end__6=tomMatch310_1;do {{if (!( tomMatch310__end__6.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch310_10= tomMatch310__end__6.getHeadconcOption() ;if ( (tomMatch310_10 instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) { tom.engine.adt.tomdeclaration.types.Declaration  tomMatch310_9= tomMatch310_10.getAstDeclaration() ;boolean tomMatch310_12= false ;if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.GetImplementationDecl) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) {tomMatch310_12= true ;} else {if ( (tomMatch310_9 instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) {tomMatch310_12= true ;}}}}}}}}}}}}}if (tomMatch310_12) {

        return tomSymbol;
      }}}if ( tomMatch310__end__6.isEmptyconcOption() ) {tomMatch310__end__6=tomMatch310_1;} else {tomMatch310__end__6= tomMatch310__end__6.getTailconcOption() ;}}} while(!( (tomMatch310__end__6==tomMatch310_1) ));}}}}{if ( (tomSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch310_15= (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getTypesToType() ; tom.engine.adt.tomoption.types.OptionList  tomMatch310_17= (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getOption() ; tom.engine.adt.tomname.types.TomName  tom_name= (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getAstName() ;if ( (tomMatch310_15 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tom_codomain= tomMatch310_15.getCodomain() ;if ( ((tomMatch310_17 instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || (tomMatch310_17 instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch310__end__25=tomMatch310_17;do {{if (!( tomMatch310__end__25.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch310_31= tomMatch310__end__25.getHeadconcOption() ;if ( (tomMatch310_31 instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

        //build variables for make
        BQTermList argsAST =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
        int index = 0;
        for(TomType subtermType:(concTomType) tomMatch310_15.getDomain() ) {
          BQTerm variable =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("t"+index) , subtermType) ;
          argsAST = tom_append_list_concBQTerm(argsAST, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(variable, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
          index++;
        }
        BQTerm functionCall =  tom.engine.adt.code.types.bqterm.FunctionCall.make(tom_name, tom_codomain, argsAST) ;
        Declaration make =  tom.engine.adt.tomdeclaration.types.declaration.MakeDecl.make(tom_name, tom_codomain, argsAST,  tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction.make(functionCall) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("make") ,  tomMatch310_31.getLine() ,  tomMatch310_31.getFileName() ) ) 
;
        return  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_name, tomMatch310_15,  (( tom.engine.adt.tomsignature.types.TomSymbol )tomSymbol).getPairNameDeclList() , tom_append_list_concOption(tom_get_slice_concOption(tomMatch310_17,tomMatch310__end__25, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ), tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tomMatch310__end__25.getHeadconcOption() , tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(make) ,tom_append_list_concOption( tomMatch310__end__25.getTailconcOption() , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ) )) ;
      }}if ( tomMatch310__end__25.isEmptyconcOption() ) {tomMatch310__end__25=tomMatch310_17;} else {tomMatch310__end__25= tomMatch310__end__25.getTailconcOption() ;}}} while(!( (tomMatch310__end__25==tomMatch310_17) ));}}}}}}

    return tomSymbol;
  }
  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomTyper.DECLARED_OPTIONS);
  }

  /*
   * The 'typeTermApplTomSyntax' phase replaces:
   * - each 'TermAppl' by its typed record form:
   *    placeholders are not removed
   *    slotName are attached to arguments
   */
  public static class typeTermApplTomSyntax extends tom.library.sl.AbstractStrategyBasic {private  TomTyper  typer;public typeTermApplTomSyntax( TomTyper  typer) {super(( new tom.library.sl.Identity() ));this.typer=typer;}public  TomTyper  gettyper() {return typer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {


        return typer.typeTermAppl( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOption() , (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() , (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getArgs() , (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() );
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {


        //System.out.println("typeXML in:\n" + subject);
        return typer.typeXMLAppl( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOption() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAttrList() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getChildList() , (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() );
      }}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_typeTermApplTomSyntax( TomTyper  t0) { return new typeTermApplTomSyntax(t0);}



    /*
     * this post-processing phase replaces untyped (universalType) codomain
     * by their precise type (according to the symbolTable)
     */
    public static class updateCodomain extends tom.library.sl.AbstractStrategyBasic {private  TomTyper  typer;public updateCodomain( TomTyper  typer) {super(( new tom.library.sl.Identity() ));this.typer=typer;}public  TomTyper  gettyper() {return typer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) {


            throw new TomRuntimeException("updateCodomain");
        }}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch312_3= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ;if ( (tomMatch312_3 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


          TomSymbol tomSymbol = typer.getSymbolFromName( tomMatch312_3.getString() );
          TomTypeList codomain = TomBase.getSymbolDomain(tomSymbol);
          if(codomain.length()==1) {
            Declaration t = (Declaration)(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg);
            t = t.setCodomain(codomain.getHeadconcTomType());
            return t;
          } else {
            throw new TomRuntimeException("updateCodomain: bad codomain: " + codomain);
          }
        }}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch312_8= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getVariable() ;if ( (tomMatch312_8 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {


          TomSymbol tomSymbol = typer.getSymbolFromType( tomMatch312_8.getAstType() );
          if(tomSymbol != null) {
            TomTypeList codomain = TomBase.getSymbolDomain(tomSymbol);

            if(codomain.length()==1) {
              Declaration t = (Declaration)(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg);
              t = t.setCodomain(codomain.getHeadconcTomType());
              return t;
            } else {
              throw new TomRuntimeException("updateCodomain: bad codomain: " + codomain);
            }
          }
        }}}}}return _visit_Declaration(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_updateCodomain( TomTyper  t0) { return new updateCodomain(t0);}



    /*
     * replace conc('abc') by conc('a','b','c')
     */
    public static class typeString extends tom.library.sl.AbstractStrategyBasic {private  TomTyper  typer;public typeString( TomTyper  typer) {super(( new tom.library.sl.Identity() ));this.typer=typer;}public  TomTyper  gettyper() {return typer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch313_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ;if ( ((tomMatch313_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch313_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch313_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch313_8= tomMatch313_1.getHeadconcTomName() ;if ( (tomMatch313_8 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomslot.types.SlotList  tom_args= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getSlots() ;


          TomSymbol tomSymbol = typer.getSymbolFromName( tomMatch313_8.getString() );
          //System.out.println("appl = " + subject);
          if(tomSymbol != null) {
            if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
              //System.out.println("appl = " + subject);
              SlotList newArgs = typer.typeChar(tomSymbol,tom_args);
              if(newArgs!=tom_args) {
                return (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).setSlots(newArgs);
              }
            }
          }
        }}}}}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_typeString( TomTyper  t0) { return new typeString(t0);}



    /*
     * detect ill-formed char: 'abc'
     * and type it into a list of char: 'a','b','c'
     */
    private SlotList typeChar(TomSymbol tomSymbol,SlotList args) {
      if(args.isEmptyconcSlot()) {
        return args;
      } else {
        Slot head = args.getHeadconcSlot();
        SlotList tail = typeChar(tomSymbol,args.getTailconcSlot());
        {{if ( (head instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )head) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch314_2= (( tom.engine.adt.tomslot.types.Slot )head).getAppl() ; tom.engine.adt.tomname.types.TomName  tom_slotName= (( tom.engine.adt.tomslot.types.Slot )head).getSlotName() ;if ( (tomMatch314_2 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch314_5= tomMatch314_2.getNameList() ; tom.engine.adt.tomslot.types.SlotList  tomMatch314_6= tomMatch314_2.getSlots() ;if ( ((tomMatch314_5 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch314_5 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch314_5.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch314_13= tomMatch314_5.getHeadconcTomName() ;if ( (tomMatch314_13 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch314_13.getString() ;if (  tomMatch314_5.getTailconcTomName() .isEmptyconcTomName() ) {if ( ((tomMatch314_6 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch314_6 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if ( tomMatch314_6.isEmptyconcSlot() ) { tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraintList= tomMatch314_2.getConstraints() ;

            /*
             * ensure that the argument contains at least 1 character and 2 single quotes
             */
            TomSymbol stringSymbol = getSymbolFromName(tom_tomName);
            TomType termType = stringSymbol.getTypesToType().getCodomain();
            String type = termType.getTomType();
            if(symbolTable().isCharType(type) && tom_tomName.length()>3) {
              if(tom_tomName.charAt(0)=='\'' && tom_tomName.charAt(tom_tomName.length()-1)=='\'') {
                SlotList newArgs =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
                String substring = tom_tomName.substring(1,tom_tomName.length()-1);
                //System.out.println("bingo -> " + substring);
                substring = substring.replace("\\'","'"); // replace backslash-quote by quote
                substring = substring.replace("\\\\","\\"); // replace backslash-backslash by backslash
                //System.out.println("after encoding -> " + substring);

                for(int i=substring.length()-1 ; i>=0 ;  i--) {
                  char c = substring.charAt(i);
                  String newName = "'" + c + "'";
                  TomSymbol newSymbol = stringSymbol.setAstName( tom.engine.adt.tomname.types.tomname.Name.make(newName) );
                  symbolTable().putSymbol(newName,newSymbol);

                  Slot newHead =  tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName,  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make( tomMatch314_2.getOption() ,  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(newName) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) ,  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
                  newArgs =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make(newHead,tom_append_list_concSlot(newArgs, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
                  //System.out.println("newHead = " + newHead);
                  //System.out.println("newSymb = " + getSymbolFromName(newName));
                }
                ConstraintList newConstraintList =  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ;
                {{if ( (tom_constraintList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch315_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList).getHeadconcConstraint() ;if ( (tomMatch315_4 instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch315_3= tomMatch315_4.getVar() ;if ( (tomMatch315_3 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_constraintList).getTailconcConstraint() .isEmptyconcConstraint() ) {

                    if(symbolTable().isCharType(TomBase.getTomType( tomMatch315_3.getAstType() ))) {
                      newConstraintList =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( tom.engine.adt.tomconstraint.types.constraint.AliasTo.make(tomMatch315_3.setAstType(symbolTable().getStringType())) , tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
                    }
                  }}}}}}}}


                TomTerm newSublist =  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(tomSymbol.getAstName(), tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newArgs, newConstraintList) ;
                Slot newSlot =  tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName, newSublist) ;
                return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make(newSlot,tom_append_list_concSlot(tail, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
              } else {
                throw new TomRuntimeException("typeChar: strange char: " + tom_tomName);
              }
            }
          }}}}}}}}}}}

        return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make(head,tom_append_list_concSlot(tail, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
      }
    }

    /*
     * replaces 'TermAppl' by its 'RecordAppl' form
     * when no slotName exists, the position becomes the slotName
     */
    protected TomTerm typeTermAppl(OptionList option, TomNameList nameList, TomList args, ConstraintList constraints) {
      TomName headName = nameList.getHeadconcTomName();
      if(headName instanceof AntiName) {
        headName = ((AntiName)headName).getName();
      }
      String opName = headName.getString();
      TomSymbol tomSymbol = getSymbolFromName(opName);


      //System.out.println("typeTermAppl: " + tomSymbol);
      //System.out.println("  nameList = " + nameList);

      if(tomSymbol==null && args.isEmptyconcTomTerm()) {
        return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(option, nameList,  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() , constraints) ;
      }

      SlotList slotList =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
      Strategy typeStrategy = tom_make_TopDownIdStopOnSuccess(tom_make_typeTermApplTomSyntax(this));
      if(opName.equals("") || tomSymbol==null || TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
        for(TomTerm arg:(concTomTerm)args) {
          try {
            TomTerm subterm = typeStrategy.visitLight(arg);
            TomName slotName =  tom.engine.adt.tomname.types.tomname.EmptyName.make() ;
            /*
             * we cannot optimize when subterm.isUnamedVariable
             * since it can be constrained
             */	  
            slotList = tom_append_list_concSlot(slotList, tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(slotName, subterm) , tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) );
          } catch(tom.library.sl.VisitFailure e) {
            System.out.println("should not be there");
          }
        }
      } else {
        PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
        for(TomTerm arg:(concTomTerm)args) {
          try{
            TomTerm subterm = typeStrategy.visitLight(arg);
            TomName slotName = pairNameDeclList.getHeadconcPairNameDecl().getSlotName();
            /*
             * we cannot optimize when subterm.isUnamedVariable
             * since it can be constrained
             */	  
            slotList = tom_append_list_concSlot(slotList, tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(slotName, subterm) , tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) );
            pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
          } catch(tom.library.sl.VisitFailure e) {
            System.out.println("should not be there");
          }
        }
      }

      return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(option, nameList, slotList, constraints) ;
    }

    /*
     * transform a BQAppl into its compiled form
     */
    public static class typeBQAppl extends tom.library.sl.AbstractStrategyBasic {private  TomTyper  typer;public typeBQAppl( TomTyper  typer) {super(( new tom.library.sl.Identity() ));this.typer=typer;}public  TomTyper  gettyper() {return typer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch316_2= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOption() ;if ( (tomMatch316_2 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomname.types.TomName  tom_name=tomMatch316_2;


          TomSymbol tomSymbol = typer.getSymbolFromName( tomMatch316_2.getString() );
          BQTermList args  = tom_make_TopDownIdStopOnSuccess(tom_make_typeBQAppl(typer)).visitLight( (( tom.engine.adt.code.types.BQTerm )tom__arg).getArgs() );
          //System.out.println("BackQuoteTerm: " + `tomName);
          //System.out.println("tomSymbol: " + tomSymbol);
          if(TomBase.hasConstant(tom_optionList)) {
            return  tom.engine.adt.code.types.bqterm.BuildConstant.make(tom_name) ;
          } else if(tomSymbol != null) {
            if(TomBase.isListOperator(tomSymbol)) {
              return ASTFactory.buildList(tom_name,args,typer.symbolTable());
            } else if(TomBase.isArrayOperator(tomSymbol)) {
              return ASTFactory.buildArray(tom_name,args,typer.symbolTable());
            } else if(TomBase.isDefinedSymbol(tomSymbol)) {
              return  tom.engine.adt.code.types.bqterm.FunctionCall.make(tom_name, TomBase.getSymbolCodomain(tomSymbol), args) ;
            } else {
              String moduleName = TomBase.getModuleName(tom_optionList);
              if(moduleName==null) {
                moduleName = TomBase.DEFAULT_MODULE_NAME;
              }
              return  tom.engine.adt.code.types.bqterm.BuildTerm.make(tom_name, args, moduleName) ;
            }
          } else {
            return  tom.engine.adt.code.types.bqterm.FunctionCall.make(tom_name,  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() , args) ;
          }
        }}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_typeBQAppl( TomTyper  t0) { return new typeBQAppl(t0);}



    private TomList sortAttributeList(TomList attrList) {
      {{if ( (attrList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )attrList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )attrList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if ( (( tom.engine.adt.tomterm.types.TomList )attrList).isEmptyconcTomTerm() ) {
 return attrList; }}}}{if ( (attrList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )attrList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )attrList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch317__end__6=(( tom.engine.adt.tomterm.types.TomList )attrList);do {{ tom.engine.adt.tomterm.types.TomList  tom_X1=tom_get_slice_concTomTerm((( tom.engine.adt.tomterm.types.TomList )attrList),tomMatch317__end__6, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() );if (!( tomMatch317__end__6.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tom_e1= tomMatch317__end__6.getHeadconcTomTerm() ; tom.engine.adt.tomterm.types.TomList  tomMatch317_7= tomMatch317__end__6.getTailconcTomTerm() ; tom.engine.adt.tomterm.types.TomList  tomMatch317__end__10=tomMatch317_7;do {{ tom.engine.adt.tomterm.types.TomList  tom_X2=tom_get_slice_concTomTerm(tomMatch317_7,tomMatch317__end__10, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() );if (!( tomMatch317__end__10.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tom_e2= tomMatch317__end__10.getHeadconcTomTerm() ; tom.engine.adt.tomterm.types.TomList  tom_X3= tomMatch317__end__10.getTailconcTomTerm() ;{{if ( (tom_e1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e1) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch318_2= (( tom.engine.adt.tomterm.types.TomTerm )tom_e1).getArgs() ;if ( ((tomMatch318_2 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch318_2 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch318_2.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch318_13= tomMatch318_2.getHeadconcTomTerm() ;if ( (tomMatch318_13 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch318_12= tomMatch318_13.getNameList() ;if ( ((tomMatch318_12 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch318_12 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch318_12.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch318_21= tomMatch318_12.getHeadconcTomName() ;if ( (tomMatch318_21 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch318_12.getTailconcTomName() .isEmptyconcTomName() ) {if ( (tom_e2 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e2) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch318_4= (( tom.engine.adt.tomterm.types.TomTerm )tom_e2).getArgs() ;if ( ((tomMatch318_4 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch318_4 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch318_4.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch318_15= tomMatch318_4.getHeadconcTomTerm() ;if ( (tomMatch318_15 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch318_14= tomMatch318_15.getNameList() ;if ( ((tomMatch318_14 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch318_14 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch318_14.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch318_23= tomMatch318_14.getHeadconcTomName() ;if ( (tomMatch318_23 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch318_14.getTailconcTomName() .isEmptyconcTomName() ) {




                if( tomMatch318_21.getString() .compareTo( tomMatch318_23.getString() ) > 0) {
                  return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
                }
              }}}}}}}}}}}}}}}}}}}{if ( (tom_e1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e1) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch318_26= (( tom.engine.adt.tomterm.types.TomTerm )tom_e1).getArgs() ;if ( ((tomMatch318_26 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch318_26 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch318_26.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch318_37= tomMatch318_26.getHeadconcTomTerm() ;if ( (tomMatch318_37 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch318_36= tomMatch318_37.getNameList() ;if ( ((tomMatch318_36 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch318_36 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch318_36.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch318_45= tomMatch318_36.getHeadconcTomName() ;if ( (tomMatch318_45 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch318_36.getTailconcTomName() .isEmptyconcTomName() ) {if ( (tom_e2 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e2) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch318_28= (( tom.engine.adt.tomterm.types.TomTerm )tom_e2).getArgs() ;if ( ((tomMatch318_28 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch318_28 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch318_28.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch318_39= tomMatch318_28.getHeadconcTomTerm() ;if ( (tomMatch318_39 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch318_38= tomMatch318_39.getNameList() ;if ( ((tomMatch318_38 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch318_38 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch318_38.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch318_47= tomMatch318_38.getHeadconcTomName() ;if ( (tomMatch318_47 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch318_38.getTailconcTomName() .isEmptyconcTomName() ) {



                if( tomMatch318_45.getString() .compareTo( tomMatch318_47.getString() ) > 0) {
                  return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
                }
              }}}}}}}}}}}}}}}}}}}{if ( (tom_e1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e1) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch318_50= (( tom.engine.adt.tomterm.types.TomTerm )tom_e1).getSlots() ;if ( ((tomMatch318_50 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch318_50 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch318_50.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch318_62= tomMatch318_50.getHeadconcSlot() ;if ( (tomMatch318_62 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch318_61= tomMatch318_62.getAppl() ;if ( (tomMatch318_61 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch318_63= tomMatch318_61.getNameList() ;if ( ((tomMatch318_63 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch318_63 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch318_63.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch318_76= tomMatch318_63.getHeadconcTomName() ;if ( (tomMatch318_76 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch318_63.getTailconcTomName() .isEmptyconcTomName() ) {if ( (tom_e2 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e2) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch318_52= (( tom.engine.adt.tomterm.types.TomTerm )tom_e2).getSlots() ;if ( ((tomMatch318_52 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch318_52 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch318_52.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch318_67= tomMatch318_52.getHeadconcSlot() ;if ( (tomMatch318_67 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch318_66= tomMatch318_67.getAppl() ;if ( ( tomMatch318_62.getSlotName() == tomMatch318_67.getSlotName() ) ) {if ( (tomMatch318_66 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch318_68= tomMatch318_66.getNameList() ;if ( ((tomMatch318_68 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch318_68 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch318_68.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch318_78= tomMatch318_68.getHeadconcTomName() ;if ( (tomMatch318_78 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch318_68.getTailconcTomName() .isEmptyconcTomName() ) {



                if( tomMatch318_76.getString() .compareTo( tomMatch318_78.getString() ) > 0) {
                  return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
                }
              }}}}}}}}}}}}}}}}}}}}}}{if ( (tom_e1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e1) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch318_81= (( tom.engine.adt.tomterm.types.TomTerm )tom_e1).getSlots() ;if ( ((tomMatch318_81 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch318_81 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch318_81.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch318_93= tomMatch318_81.getHeadconcSlot() ;if ( (tomMatch318_93 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch318_92= tomMatch318_93.getAppl() ;if ( (tomMatch318_92 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch318_94= tomMatch318_92.getNameList() ;if ( ((tomMatch318_94 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch318_94 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch318_94.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch318_107= tomMatch318_94.getHeadconcTomName() ;if ( (tomMatch318_107 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch318_94.getTailconcTomName() .isEmptyconcTomName() ) {if ( (tom_e2 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e2) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch318_83= (( tom.engine.adt.tomterm.types.TomTerm )tom_e2).getSlots() ;if ( ((tomMatch318_83 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch318_83 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch318_83.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch318_98= tomMatch318_83.getHeadconcSlot() ;if ( (tomMatch318_98 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch318_97= tomMatch318_98.getAppl() ;if ( ( tomMatch318_93.getSlotName() == tomMatch318_98.getSlotName() ) ) {if ( (tomMatch318_97 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch318_99= tomMatch318_97.getNameList() ;if ( ((tomMatch318_99 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch318_99 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch318_99.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch318_109= tomMatch318_99.getHeadconcTomName() ;if ( (tomMatch318_109 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch318_99.getTailconcTomName() .isEmptyconcTomName() ) {



                if( tomMatch318_107.getString() .compareTo( tomMatch318_109.getString() ) > 0) {
                  return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
                }
              }}}}}}}}}}}}}}}}}}}}}}}
























        }if ( tomMatch317__end__10.isEmptyconcTomTerm() ) {tomMatch317__end__10=tomMatch317_7;} else {tomMatch317__end__10= tomMatch317__end__10.getTailconcTomTerm() ;}}} while(!( (tomMatch317__end__10==tomMatch317_7) ));}if ( tomMatch317__end__6.isEmptyconcTomTerm() ) {tomMatch317__end__6=(( tom.engine.adt.tomterm.types.TomList )attrList);} else {tomMatch317__end__6= tomMatch317__end__6.getTailconcTomTerm() ;}}} while(!( (tomMatch317__end__6==(( tom.engine.adt.tomterm.types.TomList )attrList)) ));}}}}

      return attrList;
    }

    private OptionList convertOriginTracking(String name,OptionList optionList) {
      Option originTracking = TomBase.findOriginTracking(optionList);
      {{if ( (originTracking instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )originTracking) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

          return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(name) ,  (( tom.engine.adt.tomoption.types.Option )originTracking).getLine() ,  (( tom.engine.adt.tomoption.types.Option )originTracking).getFileName() ) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ;
        }}}}

      System.out.println("Warning: no OriginTracking information");
      return  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
    }

    protected TomTerm typeXMLAppl(OptionList optionList, TomNameList nameList,
        TomList attrList, TomList childList, ConstraintList constraints) {
      boolean implicitAttribute = TomBase.hasImplicitXMLAttribut(optionList);
      boolean implicitChild     = TomBase.hasImplicitXMLChild(optionList);

      TomList newAttrList  =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
      TomList newChildList =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
      TomTerm star =  tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar.make(convertOriginTracking("_*",optionList), symbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      if(implicitAttribute) { newAttrList  =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(star,tom_append_list_concTomTerm(newAttrList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ; }
      if(implicitChild)     { newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(star,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ; }

      /*
       * the list of attributes should not be typed before the sort
       * the sortAttribute is extended to compare RecordAppl
       */

      //System.out.println("attrList = " + attrList);
      attrList = sortAttributeList(attrList);
      //System.out.println("sorted attrList = " + attrList);

      /*
       * Attributes: go from implicit notation to explicit notation
       */
      Strategy typeStrategy = tom_make_TopDownIdStopOnSuccess(tom_make_typeTermApplTomSyntax(this));
      for(TomTerm attr:(concTomTerm)attrList) {
        try {
          TomTerm newPattern = typeStrategy.visitLight(attr);
          newAttrList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(newPattern,tom_append_list_concTomTerm(newAttrList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
          if(implicitAttribute) {
            newAttrList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(star,tom_append_list_concTomTerm(newAttrList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
          }
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("should not be there");
        }
      }
      newAttrList = newAttrList.reverse();

      /*
       * Childs: go from implicit notation to explicit notation
       */
      for(TomTerm child:(concTomTerm)childList) {
        try {
          TomTerm newPattern = typeStrategy.visitLight(child);
          newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(newPattern,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
          if(implicitChild) {
            if(newPattern.isVariableStar()) {
              // remove the previously inserted pattern
              newChildList = newChildList.getTailconcTomTerm();
              if(newChildList.getHeadconcTomTerm().isUnamedVariableStar()) {
                // remove the previously inserted star
                newChildList = newChildList.getTailconcTomTerm();
              }
              // re-insert the pattern
              newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(newPattern,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
            } else {
              newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(star,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
            }
          }
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("should not be there");
        }
      }
      newChildList = newChildList.reverse();

      /*
       * encode the name and put it into the table of symbols
       */
      TomNameList newNameList =  tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ;
matchBlock: 
      {
        {{if ( (nameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )nameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )nameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( (( tom.engine.adt.tomname.types.TomNameList )nameList).isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch320_4= (( tom.engine.adt.tomname.types.TomNameList )nameList).getHeadconcTomName() ;if ( (tomMatch320_4 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "_".equals( tomMatch320_4.getString() ) ) {if (  (( tom.engine.adt.tomname.types.TomNameList )nameList).getTailconcTomName() .isEmptyconcTomName() ) {

            break matchBlock;
          }}}}}}}{if ( (nameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )nameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )nameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch320__end__10=(( tom.engine.adt.tomname.types.TomNameList )nameList);do {{if (!( tomMatch320__end__10.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch320_14= tomMatch320__end__10.getHeadconcTomName() ;if ( (tomMatch320_14 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


            newNameList = tom_append_list_concTomName(newNameList, tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(ASTFactory.encodeXMLString(symbolTable(), tomMatch320_14.getString() )) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) );
          }}if ( tomMatch320__end__10.isEmptyconcTomName() ) {tomMatch320__end__10=(( tom.engine.adt.tomname.types.TomNameList )nameList);} else {tomMatch320__end__10= tomMatch320__end__10.getTailconcTomName() ;}}} while(!( (tomMatch320__end__10==(( tom.engine.adt.tomname.types.TomNameList )nameList)) ));}}}}

      }

      /*
       * a single "_" is converted into an UnamedVariable to match
       * any XML node
       */
      TomTerm xmlHead;

      if(newNameList.isEmptyconcTomName()) {
        xmlHead =  tom.engine.adt.tomterm.types.tomterm.UnamedVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , symbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      } else {
        xmlHead =  tom.engine.adt.tomterm.types.tomterm.TermAppl.make(convertOriginTracking(newNameList.getHeadconcTomName().getString(),optionList), newNameList,  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }
      try {
        SlotList newArgs =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_NAME) , typeStrategy.visitLight(xmlHead)) , tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_ATTRLIST) , typeStrategy.visitLight( tom.engine.adt.tomterm.types.tomterm.TermAppl.make(convertOriginTracking("CONC_TNODE",optionList),  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.CONC_TNODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newAttrList,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) , tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_CHILDLIST) , typeStrategy.visitLight( tom.engine.adt.tomterm.types.tomterm.TermAppl.make(convertOriginTracking("CONC_TNODE",optionList),  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.CONC_TNODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newChildList,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) , tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) ) ) 





;

        TomTerm result =  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(optionList,  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.ELEMENT_NODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newArgs, constraints) ;

        //System.out.println("typeXML out:\n" + result);
        return result;
      } catch(tom.library.sl.VisitFailure e) {
        //must never be executed
        return star;
      }
    }
  }
