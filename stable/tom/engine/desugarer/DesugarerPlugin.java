/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2012, INPL, INRIA
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

package tom.engine.desugarer;

import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;

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
 * The Desugarer plugin.
 * Perform syntax expansion and more.
 *
 * replaces  _  by a fresh variable _* by a fresh varstar 
 * replaces 'TermAppl' by its 'RecordAppl' form
 * replaces 'XMLAppl' by its 'RecordAppl' form
 *   when no slotName exits, the position becomes the slotName
 */
public class DesugarerPlugin extends TomGenericPlugin {

        private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {     if( l1.isEmptyComposite() ) {       return l2;     } else if( l2.isEmptyComposite() ) {       return l1;     } else if(  l1.getTailComposite() .isEmptyComposite() ) {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownIdStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) ) )) ;}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}   



  /** some output suffixes */
  public static final String DESUGARED_SUFFIX = ".tfix.desugared";

  private static Logger logger = Logger.getLogger("tom.engine.desugarer.DesugarerPlugin");
  private SymbolTable symbolTable;
  private int freshCounter = 0;

  // FIXME : generate truly fresh variables
  private TomName getFreshVariable() {
    freshCounter++;
    return  tom.engine.adt.tomname.types.tomname.Name.make("_f_r_e_s_h_v_a_r_" + freshCounter) ;
  }

  public SymbolTable getSymbolTable() {
    return this.symbolTable;
  } 

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  public DesugarerPlugin() {
    super("DesugarerPlugin");
  }

  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    try {
      setSymbolTable(getStreamManager().getSymbolTable());
      updateSymbolTable();
      Code code = (Code) getWorkingTerm();

      // replace underscores by fresh variables
      code = tom_make_TopDown(tom_make_DesugarUnderscore(this)).visitLight(code);

      // replace TermAppl and XmlAppl by RecordAppl
      code = tom_make_TopDownIdStopOnSuccess(tom_make_replaceXMLApplTomSyntax(this)).visitLight(code);
      code = tom_make_TopDownIdStopOnSuccess(tom_make_replaceTermApplTomSyntax(this)).visitLight(code);
      // replace BQRecordAppl by BQTermAppl
      code = tom_make_TopDown(tom_make_replaceBQRecordApplTomSyntax(this)).visitLight(code);

      setWorkingTerm(code);      

      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() +
            DESUGARED_SUFFIX, code);
      }
      // verbose
      TomMessage.info(logger,null,0,TomMessage.tomDesugaringPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
    } catch (Exception e) {
      TomMessage.error(logger,
          getStreamManager().getInputFileName(), 0,
          TomMessage.exceptionMessage, e.getMessage());
      e.printStackTrace();
      return;
    }
  }

  /* 
   * replaces  _  by a fresh variable _* by a fresh varstar    
   */
  public static class DesugarUnderscore extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.desugarer.DesugarerPlugin  desugarer;public DesugarUnderscore( tom.engine.desugarer.DesugarerPlugin  desugarer) {super(( new tom.library.sl.Identity() ));this.desugarer=desugarer;}public  tom.engine.desugarer.DesugarerPlugin  getdesugarer() {return desugarer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch230_2= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstName() ;if ( (tomMatch230_2 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch230_2) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {


        return  tom.engine.adt.tomterm.types.tomterm.Variable.make( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() , desugarer.getFreshVariable(),  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstType() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() ) ;
      }}}}}}{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch230_11= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstName() ;if ( (tomMatch230_11 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch230_11) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

        return  tom.engine.adt.tomterm.types.tomterm.VariableStar.make( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() , desugarer.getFreshVariable(),  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstType() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() ) ;
      }}}}}}}return _visit_TomTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_DesugarUnderscore( tom.engine.desugarer.DesugarerPlugin  t0) { return new DesugarUnderscore(t0);}



  /**
   * updateSymbol is called before a first syntax expansion phase
   * this phase updates the symbolTable 
   * this is performed by recursively traversing each symbol
   * - default IsFsymDecl and MakeDecl are added
   * - TermAppl are transformed into RecordAppl
   * - BQRecordAppl are transformed into BQAppl
   */
  public void updateSymbolTable() {
    for(String tomName:getSymbolTable().keySymbolIterable()) {
      TomSymbol tomSymbol = getSymbolFromName(tomName);
      /*
       * add default IsFsymDecl unless it is a builtin type
       * add default IsFsymDecl and MakeDecl unless:
       *  - it is a builtin type
       *  - another option (if_sfsym, get_slot, etc) is already defined for this operator
       */
      if(!getSymbolTable().isBuiltinType(TomBase.getTomType(TomBase.getSymbolCodomain(tomSymbol)))) {
        tomSymbol = addDefaultMake(tomSymbol);
        tomSymbol = addDefaultIsFsym(tomSymbol);
      }
      try {
        tomSymbol = tom_make_TopDownIdStopOnSuccess(tom_make_replaceTermApplTomSyntax(this)).visitLight(tomSymbol);
        tomSymbol = tom_make_TopDown(tom_make_replaceBQRecordApplTomSyntax(this)).visitLight(tomSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  private TomSymbol addDefaultIsFsym(TomSymbol tomSymbol) {
    {{if ( (((Object)tomSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch231_1= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getOptions() ;if ( (((( tom.engine.adt.tomoption.types.OptionList )tomMatch231_1) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tomMatch231_1) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch231__end__7=tomMatch231_1;do {{if (!( tomMatch231__end__7.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch231_11= tomMatch231__end__7.getHeadconcOption() ;if ( (tomMatch231_11 instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch231_11) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) { tom.engine.adt.tomdeclaration.types.Declaration  tomMatch231_10= tomMatch231_11.getAstDeclaration() ;if ( (tomMatch231_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch231_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) {

        return tomSymbol;
      }}}}}if ( tomMatch231__end__7.isEmptyconcOption() ) {tomMatch231__end__7=tomMatch231_1;} else {tomMatch231__end__7= tomMatch231__end__7.getTailconcOption() ;}}} while(!( (tomMatch231__end__7==tomMatch231_1) ));}}}}}{if ( (((Object)tomSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch231_17= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getTypesToType() ; tom.engine.adt.tomoption.types.OptionList  tomMatch231_19= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getOptions() ; tom.engine.adt.tomname.types.TomName  tom_name= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getAstName() ;if ( (tomMatch231_17 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch231_17) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )tomMatch231_19) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tomMatch231_19) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch231__end__29=tomMatch231_19;do {{if (!( tomMatch231__end__29.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch231_35= tomMatch231__end__29.getHeadconcOption() ;if ( (tomMatch231_35 instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch231_35) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) { int  tom_line= tomMatch231_35.getLine() ; String  tom_file= tomMatch231_35.getFileName() ;


        Declaration isfsym =  tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl.make(tom_name,  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("t") , tom_line, tom_file) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ,  tom.engine.adt.tomname.types.tomname.Name.make("t") ,  tomMatch231_17.getCodomain() ) ,  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("is_fsym") , tom_line, tom_file) ) ;
        return  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_name, tomMatch231_17,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getPairNameDeclList() , tom_append_list_concOption(tom_get_slice_concOption(tomMatch231_19,tomMatch231__end__29, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ), tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tomMatch231__end__29.getHeadconcOption() , tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(isfsym) ,tom_append_list_concOption( tomMatch231__end__29.getTailconcOption() , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ) )) ;
      }}}if ( tomMatch231__end__29.isEmptyconcOption() ) {tomMatch231__end__29=tomMatch231_19;} else {tomMatch231__end__29= tomMatch231__end__29.getTailconcOption() ;}}} while(!( (tomMatch231__end__29==tomMatch231_19) ));}}}}}}}}

    return tomSymbol;
  }

  private TomSymbol addDefaultMake(TomSymbol tomSymbol) {
    {{if ( (((Object)tomSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch232_1= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getOptions() ;if ( (((( tom.engine.adt.tomoption.types.OptionList )tomMatch232_1) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tomMatch232_1) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch232__end__7=tomMatch232_1;do {{if (!( tomMatch232__end__7.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch232_11= tomMatch232__end__7.getHeadconcOption() ;if ( (tomMatch232_11 instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch232_11) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) { tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_10= tomMatch232_11.getAstDeclaration() ;boolean tomMatch232_27= false ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_20= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_15= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_18= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_16= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_19= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_14= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_17= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_24= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_23= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_26= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_22= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_21= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch232_25= null ;if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) {{tomMatch232_27= true ;tomMatch232_14=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) {{tomMatch232_27= true ;tomMatch232_15=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) {{tomMatch232_27= true ;tomMatch232_16=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) {{tomMatch232_27= true ;tomMatch232_17=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) {{tomMatch232_27= true ;tomMatch232_18=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) {{tomMatch232_27= true ;tomMatch232_19=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) {{tomMatch232_27= true ;tomMatch232_20=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetDefaultDecl) ) {{tomMatch232_27= true ;tomMatch232_21=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) {{tomMatch232_27= true ;tomMatch232_22=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) {{tomMatch232_27= true ;tomMatch232_23=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) {{tomMatch232_27= true ;tomMatch232_24=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) {{tomMatch232_27= true ;tomMatch232_25=tomMatch232_10;}} else {if ( (tomMatch232_10 instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tomMatch232_10) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) {{tomMatch232_27= true ;tomMatch232_26=tomMatch232_10;}}}}}}}}}}}}}}}}}}}}}}}}}}}if (tomMatch232_27) {

        return tomSymbol;
      }}}}if ( tomMatch232__end__7.isEmptyconcOption() ) {tomMatch232__end__7=tomMatch232_1;} else {tomMatch232__end__7= tomMatch232__end__7.getTailconcOption() ;}}} while(!( (tomMatch232__end__7==tomMatch232_1) ));}}}}}{if ( (((Object)tomSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch232_30= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getTypesToType() ; tom.engine.adt.tomoption.types.OptionList  tomMatch232_32= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getOptions() ; tom.engine.adt.tomname.types.TomName  tom_name= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getAstName() ;if ( (tomMatch232_30 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch232_30) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tom_codomain= tomMatch232_30.getCodomain() ;if ( (((( tom.engine.adt.tomoption.types.OptionList )tomMatch232_32) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )tomMatch232_32) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch232__end__42=tomMatch232_32;do {{if (!( tomMatch232__end__42.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch232_48= tomMatch232__end__42.getHeadconcOption() ;if ( (tomMatch232_48 instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tomMatch232_48) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

        //build variables for make
        BQTermList argsAST =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
        int index = 0;
        for(TomType subtermType:(concTomType) tomMatch232_30.getDomain() ) {
          BQTerm variable =  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("t"+index) , subtermType) ;
          argsAST = tom_append_list_concBQTerm(argsAST, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(variable, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
          index++;
        }
        BQTerm functionCall =  tom.engine.adt.code.types.bqterm.FunctionCall.make(tom_name, tom_codomain, argsAST) ;
        Declaration make =  tom.engine.adt.tomdeclaration.types.declaration.MakeDecl.make(tom_name, tom_codomain, argsAST,  tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction.make(functionCall) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make("make") ,  tomMatch232_48.getLine() ,  tomMatch232_48.getFileName() ) ) 
;
        return  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_name, tomMatch232_30,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tomSymbol)).getPairNameDeclList() , tom_append_list_concOption(tom_get_slice_concOption(tomMatch232_32,tomMatch232__end__42, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ), tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tomMatch232__end__42.getHeadconcOption() , tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(make) ,tom_append_list_concOption( tomMatch232__end__42.getTailconcOption() , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ) )) ;
      }}}if ( tomMatch232__end__42.isEmptyconcOption() ) {tomMatch232__end__42=tomMatch232_32;} else {tomMatch232__end__42= tomMatch232__end__42.getTailconcOption() ;}}} while(!( (tomMatch232__end__42==tomMatch232_32) ));}}}}}}}}

    return tomSymbol;
  }

  /**
   * The 'replaceTermApplTomSyntax' phase replaces:
   * - each 'TermAppl' by its typed record form:
   *    placeholders are not removed
   *    slotName are attached to arguments
   */
  public static class replaceTermApplTomSyntax extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.desugarer.DesugarerPlugin  desugarer;public replaceTermApplTomSyntax( tom.engine.desugarer.DesugarerPlugin  desugarer) {super(( new tom.library.sl.Identity() ));this.desugarer=desugarer;}public  tom.engine.desugarer.DesugarerPlugin  getdesugarer() {return desugarer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {


        return desugarer.replaceTermAppl( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() , (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getNameList() , (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getArgs() , (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() );
      }}}}}return _visit_TomTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_replaceTermApplTomSyntax( tom.engine.desugarer.DesugarerPlugin  t0) { return new replaceTermApplTomSyntax(t0);}



  /**
   * The 'replaceXMLApplTomSyntax' phase replaces:
   * - each 'XMLAppl' by its typed record form:
   */
  public static class replaceXMLApplTomSyntax extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.desugarer.DesugarerPlugin  desugarer;public replaceXMLApplTomSyntax( tom.engine.desugarer.DesugarerPlugin  desugarer) {super(( new tom.library.sl.Identity() ));this.desugarer=desugarer;}public  tom.engine.desugarer.DesugarerPlugin  getdesugarer() {return desugarer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {


        //System.out.println("replaceXML in:\n" + `subject);
        return desugarer.replaceXMLAppl( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getNameList() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAttrList() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getChildList() , (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() );
      }}}}}return _visit_TomTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_replaceXMLApplTomSyntax( tom.engine.desugarer.DesugarerPlugin  t0) { return new replaceXMLApplTomSyntax(t0);}


  
  /**
   * The 'replaceBQRecordApplTomSyntax' phase replaces:
   * - each 'BQRecordAppl' by its BQTermAppl form:
   *    BQDefault are added
   */
  public static class replaceBQRecordApplTomSyntax extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.desugarer.DesugarerPlugin  desugarer;public replaceBQRecordApplTomSyntax( tom.engine.desugarer.DesugarerPlugin  desugarer) {super(( new tom.library.sl.Identity() ));this.desugarer=desugarer;}public  tom.engine.desugarer.DesugarerPlugin  getdesugarer() {return desugarer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQRecordAppl) ) {


        return desugarer.replaceBQRecordAppl( (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getOptions() , (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstName() , (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getSlots() );
      }}}}}return _visit_BQTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_replaceBQRecordApplTomSyntax( tom.engine.desugarer.DesugarerPlugin  t0) { return new replaceBQRecordApplTomSyntax(t0);}



  /**
   * Replace 'TermAppl' by its 'RecordAppl' form
   * when no slotName exits, the position becomes the slotName
   */
  protected TomTerm replaceTermAppl(OptionList option, TomNameList nameList, TomList args, ConstraintList constraints) {
    TomName headName = nameList.getHeadconcTomName();
    if(headName instanceof AntiName) {
      headName = ((AntiName)headName).getName();
    }
    String opName = headName.getString();
    TomSymbol tomSymbol = getSymbolFromName(opName);

    //System.out.println("replaceTermAppl: " + tomSymbol);
    //System.out.println("  nameList = " + nameList);

    /*
     * may be for constant patterns: f(1) for instance
     */
    if(tomSymbol==null && args.isEmptyconcTomTerm()) {
      return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(option, nameList,  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() , constraints) ;
    }

    SlotList slotList =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
    if(opName.equals("") || tomSymbol==null || TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
      for(TomTerm arg:(concTomTerm)args) {
        try {
          TomTerm subterm = tom_make_TopDownIdStopOnSuccess(tom_make_replaceTermApplTomSyntax(this)).visitLight(arg);
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
        try {
          TomTerm subterm = tom_make_TopDownIdStopOnSuccess(tom_make_replaceTermApplTomSyntax(this)).visitLight(arg);
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

  /**
   * Replace 'XMLAppl' by its 'RecordAppl' form
   * when no slotName exits, the position becomes the slotName
   */
  protected TomTerm replaceXMLAppl(OptionList optionList, TomNameList nameList,
      TomList attrList, TomList childList, ConstraintList constraints) {
    boolean implicitAttribute = TomBase.hasImplicitXMLAttribut(optionList);
    boolean implicitChild     = TomBase.hasImplicitXMLChild(optionList);

    TomList newAttrList  =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
    TomList newChildList =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;

    if(implicitAttribute) { newAttrList  =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( tom.engine.adt.tomterm.types.tomterm.VariableStar.make(convertOriginTracking("_*",optionList), getFreshVariable(), getSymbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ,tom_append_list_concTomTerm(newAttrList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ; }
    if(implicitChild)     { newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( tom.engine.adt.tomterm.types.tomterm.VariableStar.make(convertOriginTracking("_*",optionList), getFreshVariable(), getSymbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ; }

    /*
     * the list of attributes should not be typed before the sort
     * the sortAttribute is extended to compare RecordAppl
     */

    attrList = sortAttributeList(attrList);

    /*
     * Attributes: go from implicit notation to explicit notation
     */
    Strategy typeStrategy = tom_make_TopDownIdStopOnSuccess(tom_make_replaceXMLApplTomSyntax(this));
    for(TomTerm attr:(concTomTerm)attrList) {
      try {
        TomTerm newPattern = typeStrategy.visitLight(attr);
        newAttrList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(newPattern,tom_append_list_concTomTerm(newAttrList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
        if(implicitAttribute) {
          newAttrList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( tom.engine.adt.tomterm.types.tomterm.VariableStar.make(convertOriginTracking("_*",optionList), getFreshVariable(), getSymbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ,tom_append_list_concTomTerm(newAttrList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
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
            if(newChildList.getHeadconcTomTerm().isVariableStar()) {
              // remove the previously inserted star
              newChildList = newChildList.getTailconcTomTerm();
            }
            // re-insert the pattern
            newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(newPattern,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
          } else {
            newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( tom.engine.adt.tomterm.types.tomterm.VariableStar.make(convertOriginTracking("_*",optionList), getFreshVariable(), getSymbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
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
      {{if ( (((Object)nameList) instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )((Object)nameList))) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )((Object)nameList))) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( (( tom.engine.adt.tomname.types.TomNameList )((Object)nameList)).isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch236_4= (( tom.engine.adt.tomname.types.TomNameList )((Object)nameList)).getHeadconcTomName() ;if ( (tomMatch236_4 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch236_4) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tomMatch236_3= tomMatch236_4.getString() ;if ( true ) {if ( "_".equals(tomMatch236_3) ) {if (  (( tom.engine.adt.tomname.types.TomNameList )((Object)nameList)).getTailconcTomName() .isEmptyconcTomName() ) {

          break matchBlock;
        }}}}}}}}}{if ( (((Object)nameList) instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )((Object)nameList))) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )((Object)nameList))) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch236__end__12=(( tom.engine.adt.tomname.types.TomNameList )((Object)nameList));do {{if (!( tomMatch236__end__12.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch236_16= tomMatch236__end__12.getHeadconcTomName() ;if ( (tomMatch236_16 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch236_16) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


          newNameList = tom_append_list_concTomName(newNameList, tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(ASTFactory.encodeXMLString(getSymbolTable(), tomMatch236_16.getString() )) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) );
        }}}if ( tomMatch236__end__12.isEmptyconcTomName() ) {tomMatch236__end__12=(( tom.engine.adt.tomname.types.TomNameList )((Object)nameList));} else {tomMatch236__end__12= tomMatch236__end__12.getTailconcTomName() ;}}} while(!( (tomMatch236__end__12==(( tom.engine.adt.tomname.types.TomNameList )((Object)nameList))) ));}}}}

    }

    /*
     * any XML node
     */
    TomTerm xmlHead;

    if(newNameList.isEmptyconcTomName()) {
      xmlHead =  tom.engine.adt.tomterm.types.tomterm.Variable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , getFreshVariable(), getSymbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
    } else {
      xmlHead =  tom.engine.adt.tomterm.types.tomterm.TermAppl.make(convertOriginTracking(newNameList.getHeadconcTomName().getString(),optionList), newNameList,  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
    }
    try {
      SlotList newArgs =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_NAME) , typeStrategy.visitLight(xmlHead)) , tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_ATTRLIST) , typeStrategy.visitLight( tom.engine.adt.tomterm.types.tomterm.TermAppl.make(convertOriginTracking("CONC_TNODE",optionList),  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.CONC_TNODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newAttrList,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) , tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_CHILDLIST) , typeStrategy.visitLight( tom.engine.adt.tomterm.types.tomterm.TermAppl.make(convertOriginTracking("CONC_TNODE",optionList),  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.CONC_TNODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newChildList,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) , tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) ) ) 





;

      TomTerm result =  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(optionList,  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.ELEMENT_NODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newArgs, constraints) ;

      //System.out.println("replaceXML out:\n" + result);
      return result;
    } catch(tom.library.sl.VisitFailure e) {
      //must never be executed
    TomTerm star =  tom.engine.adt.tomterm.types.tomterm.VariableStar.make(convertOriginTracking("_*",optionList), getFreshVariable(), getSymbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      return star;
    }
  }

  /* auxilliary methods */

  private TomList sortAttributeList(TomList attrList) {
    {{if ( (((Object)attrList) instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)attrList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)attrList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if ( (( tom.engine.adt.tomterm.types.TomList )((Object)attrList)).isEmptyconcTomTerm() ) {
 return attrList; }}}}{if ( (((Object)attrList) instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)attrList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)attrList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch237__end__6=(( tom.engine.adt.tomterm.types.TomList )((Object)attrList));do {{ tom.engine.adt.tomterm.types.TomList  tom_X1=tom_get_slice_concTomTerm((( tom.engine.adt.tomterm.types.TomList )((Object)attrList)),tomMatch237__end__6, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() );if (!( tomMatch237__end__6.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tom_e1= tomMatch237__end__6.getHeadconcTomTerm() ; tom.engine.adt.tomterm.types.TomList  tomMatch237_7= tomMatch237__end__6.getTailconcTomTerm() ; tom.engine.adt.tomterm.types.TomList  tomMatch237__end__10=tomMatch237_7;do {{ tom.engine.adt.tomterm.types.TomList  tom_X2=tom_get_slice_concTomTerm(tomMatch237_7,tomMatch237__end__10, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() );if (!( tomMatch237__end__10.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tom_e2= tomMatch237__end__10.getHeadconcTomTerm() ; tom.engine.adt.tomterm.types.TomList  tom_X3= tomMatch237__end__10.getTailconcTomTerm() ;{{if ( (((Object)tom_e1) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1))) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch238_2= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1)).getArgs() ;if ( (((( tom.engine.adt.tomterm.types.TomList )tomMatch238_2) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )tomMatch238_2) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch238_2.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch238_15= tomMatch238_2.getHeadconcTomTerm() ;if ( (tomMatch238_15 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch238_15) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch238_14= tomMatch238_15.getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_14) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_14) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch238_14.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch238_25= tomMatch238_14.getHeadconcTomName() ;if ( (tomMatch238_25 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch238_25) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch238_14.getTailconcTomName() .isEmptyconcTomName() ) {if ( (((Object)tom_e2) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2))) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch238_5= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2)).getArgs() ;if ( (((( tom.engine.adt.tomterm.types.TomList )tomMatch238_5) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )tomMatch238_5) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch238_5.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch238_18= tomMatch238_5.getHeadconcTomTerm() ;if ( (tomMatch238_18 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch238_18) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch238_17= tomMatch238_18.getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_17) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_17) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch238_17.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch238_28= tomMatch238_17.getHeadconcTomName() ;if ( (tomMatch238_28 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch238_28) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch238_17.getTailconcTomName() .isEmptyconcTomName() ) {




              if( tomMatch238_25.getString() .compareTo( tomMatch238_28.getString() ) > 0) {
                return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
              }
            }}}}}}}}}}}}}}}}}}}}}}}}}{if ( (((Object)tom_e1) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1))) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch238_32= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1)).getArgs() ;if ( (((( tom.engine.adt.tomterm.types.TomList )tomMatch238_32) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )tomMatch238_32) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch238_32.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch238_45= tomMatch238_32.getHeadconcTomTerm() ;if ( (tomMatch238_45 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch238_45) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch238_44= tomMatch238_45.getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_44) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_44) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch238_44.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch238_55= tomMatch238_44.getHeadconcTomName() ;if ( (tomMatch238_55 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch238_55) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch238_44.getTailconcTomName() .isEmptyconcTomName() ) {if ( (((Object)tom_e2) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2))) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch238_35= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2)).getArgs() ;if ( (((( tom.engine.adt.tomterm.types.TomList )tomMatch238_35) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )tomMatch238_35) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch238_35.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch238_48= tomMatch238_35.getHeadconcTomTerm() ;if ( (tomMatch238_48 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch238_48) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch238_47= tomMatch238_48.getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_47) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_47) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch238_47.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch238_58= tomMatch238_47.getHeadconcTomName() ;if ( (tomMatch238_58 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch238_58) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch238_47.getTailconcTomName() .isEmptyconcTomName() ) {



              if( tomMatch238_55.getString() .compareTo( tomMatch238_58.getString() ) > 0) {
                return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
              }
            }}}}}}}}}}}}}}}}}}}}}}}}}{if ( (((Object)tom_e1) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch238_62= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1)).getSlots() ;if ( (((( tom.engine.adt.tomslot.types.SlotList )tomMatch238_62) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )tomMatch238_62) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch238_62.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch238_76= tomMatch238_62.getHeadconcSlot() ;if ( (tomMatch238_76 instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )tomMatch238_76) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch238_75= tomMatch238_76.getAppl() ;if ( (tomMatch238_75 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch238_75) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch238_78= tomMatch238_75.getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_78) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_78) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch238_78.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch238_94= tomMatch238_78.getHeadconcTomName() ;if ( (tomMatch238_94 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch238_94) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch238_78.getTailconcTomName() .isEmptyconcTomName() ) {if ( (((Object)tom_e2) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch238_65= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2)).getSlots() ;if ( (((( tom.engine.adt.tomslot.types.SlotList )tomMatch238_65) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )tomMatch238_65) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch238_65.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch238_83= tomMatch238_65.getHeadconcSlot() ;if ( (tomMatch238_83 instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )tomMatch238_83) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch238_82= tomMatch238_83.getAppl() ;if ( ( tomMatch238_76.getSlotName() == tomMatch238_83.getSlotName() ) ) {if ( (tomMatch238_82 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch238_82) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch238_85= tomMatch238_82.getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_85) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_85) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch238_85.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch238_97= tomMatch238_85.getHeadconcTomName() ;if ( (tomMatch238_97 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch238_97) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch238_85.getTailconcTomName() .isEmptyconcTomName() ) {



              if( tomMatch238_94.getString() .compareTo( tomMatch238_97.getString() ) > 0) {
                return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
              }
            }}}}}}}}}}}}}}}}}}}}}}}}}}}}}}{if ( (((Object)tom_e1) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch238_101= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e1)).getSlots() ;if ( (((( tom.engine.adt.tomslot.types.SlotList )tomMatch238_101) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )tomMatch238_101) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch238_101.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch238_115= tomMatch238_101.getHeadconcSlot() ;if ( (tomMatch238_115 instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )tomMatch238_115) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch238_114= tomMatch238_115.getAppl() ;if ( (tomMatch238_114 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch238_114) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch238_117= tomMatch238_114.getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_117) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_117) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch238_117.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch238_133= tomMatch238_117.getHeadconcTomName() ;if ( (tomMatch238_133 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch238_133) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch238_117.getTailconcTomName() .isEmptyconcTomName() ) {if ( (((Object)tom_e2) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch238_104= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom_e2)).getSlots() ;if ( (((( tom.engine.adt.tomslot.types.SlotList )tomMatch238_104) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )tomMatch238_104) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch238_104.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch238_122= tomMatch238_104.getHeadconcSlot() ;if ( (tomMatch238_122 instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )tomMatch238_122) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch238_121= tomMatch238_122.getAppl() ;if ( ( tomMatch238_115.getSlotName() == tomMatch238_122.getSlotName() ) ) {if ( (tomMatch238_121 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch238_121) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch238_124= tomMatch238_121.getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_124) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch238_124) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch238_124.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch238_136= tomMatch238_124.getHeadconcTomName() ;if ( (tomMatch238_136 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch238_136) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch238_124.getTailconcTomName() .isEmptyconcTomName() ) {



              if( tomMatch238_133.getString() .compareTo( tomMatch238_136.getString() ) > 0) {
                return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
              }
            }}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}

      }if ( tomMatch237__end__10.isEmptyconcTomTerm() ) {tomMatch237__end__10=tomMatch237_7;} else {tomMatch237__end__10= tomMatch237__end__10.getTailconcTomTerm() ;}}} while(!( (tomMatch237__end__10==tomMatch237_7) ));}if ( tomMatch237__end__6.isEmptyconcTomTerm() ) {tomMatch237__end__6=(( tom.engine.adt.tomterm.types.TomList )((Object)attrList));} else {tomMatch237__end__6= tomMatch237__end__6.getTailconcTomTerm() ;}}} while(!( (tomMatch237__end__6==(( tom.engine.adt.tomterm.types.TomList )((Object)attrList))) ));}}}}

    return attrList;
  }

  private OptionList convertOriginTracking(String name,OptionList optionList) {
    Option originTracking = TomBase.findOriginTracking(optionList);
    {{if ( (((Object)originTracking) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )((Object)originTracking)) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)originTracking))) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

        return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(name) ,  (( tom.engine.adt.tomoption.types.Option )((Object)originTracking)).getLine() ,  (( tom.engine.adt.tomoption.types.Option )((Object)originTracking)).getFileName() ) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ;
      }}}}}

    System.out.println("Warning: no OriginTracking information");
    return  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
  }

  /**
   * Replace 'BQRecordAppl' by its 'BQTermAppl' form
   * BQDefault are automatically added
   */
  protected BQTerm replaceBQRecordAppl(OptionList option, TomName symbolName, BQSlotList slots) {
    String opName = symbolName.getString();
    TomSymbol tomSymbol = getSymbolFromName(opName);

    //System.out.println("replaceBQRecordAppl: " + tomSymbol);
    int arity = TomBase.getArity(tomSymbol);
    //System.out.println("arity: " + arity);
    ArrayList<BQTerm> termArray = new ArrayList<BQTerm>(arity);
    for(int i=0; i<arity ; i++) {
      termArray.add( tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make( tom.engine.adt.code.types.bqterm.BQDefault.make() ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) );
    }
    for(BQSlot slot:slots.getCollectionconcBQSlot()) {
      {{if ( (((Object)slot) instanceof tom.engine.adt.tomslot.types.BQSlot) ) {if ( ((( tom.engine.adt.tomslot.types.BQSlot )((Object)slot)) instanceof tom.engine.adt.tomslot.types.BQSlot) ) {if ( ((( tom.engine.adt.tomslot.types.BQSlot )(( tom.engine.adt.tomslot.types.BQSlot )((Object)slot))) instanceof tom.engine.adt.tomslot.types.bqslot.PairSlotBQTerm) ) {

          int slotIndex = TomBase.getSlotIndex(tomSymbol, (( tom.engine.adt.tomslot.types.BQSlot )((Object)slot)).getSlotName() );
          //System.out.println("termArray.size(): " + termArray.size());
          //System.out.println("slotName: " + `slotName);
          //System.out.println("slotIndex: " + slotIndex);
          termArray.set(slotIndex, (( tom.engine.adt.tomslot.types.BQSlot )((Object)slot)).getBqterm() );
        }}}}}

    }
    //System.out.println("argList = " +  `ASTFactory.makeBQTermList(termArray));
    return  tom.engine.adt.code.types.bqterm.BQAppl.make(option, symbolName, ASTFactory.makeBQTermList(termArray)) ;
  }    
}
