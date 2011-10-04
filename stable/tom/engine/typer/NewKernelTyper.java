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
 * Claudia Tavares  e-mail: Claudia.Tavares@loria.fr
 * Jean-Christophe Bach e-mail: Jeanchristophe.Bach@loria.fr
 *
 **/

package tom.engine.typer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
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
          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try( tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) ) )) ;}private static  tom.library.sl.Strategy  tom_make_TopDownIdStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) ) )) ;}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_InnermostId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), tom.library.sl.Sequence.make( tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) , null ) ) ) ) );}      private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_append_list_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList l1,  tom.engine.adt.typeconstraints.types.TypeConstraintList  l2) {     if( l1.isEmptyconcTypeConstraint() ) {       return l2;     } else if( l2.isEmptyconcTypeConstraint() ) {       return l1;     } else if(  l1.getTailconcTypeConstraint() .isEmptyconcTypeConstraint() ) {       return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,l2) ;     } else {       return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,tom_append_list_concTypeConstraint( l1.getTailconcTypeConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_get_slice_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList  begin,  tom.engine.adt.typeconstraints.types.TypeConstraintList  end, tom.engine.adt.typeconstraints.types.TypeConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeConstraint()  ||  (end== tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( begin.getHeadconcTypeConstraint() ,( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_get_slice_concTypeConstraint( begin.getTailconcTypeConstraint() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_append_list_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList l1,  tom.engine.adt.tomtype.types.TypeOptionList  l2) {     if( l1.isEmptyconcTypeOption() ) {       return l2;     } else if( l2.isEmptyconcTypeOption() ) {       return l1;     } else if(  l1.getTailconcTypeOption() .isEmptyconcTypeOption() ) {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,tom_append_list_concTypeOption( l1.getTailconcTypeOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_get_slice_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList  begin,  tom.engine.adt.tomtype.types.TypeOptionList  end, tom.engine.adt.tomtype.types.TypeOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeOption()  ||  (end== tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( begin.getHeadconcTypeOption() ,( tom.engine.adt.tomtype.types.TypeOptionList )tom_get_slice_concTypeOption( begin.getTailconcTypeOption() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.CodeList  tom_append_list_concCode( tom.engine.adt.code.types.CodeList l1,  tom.engine.adt.code.types.CodeList  l2) {     if( l1.isEmptyconcCode() ) {       return l2;     } else if( l2.isEmptyconcCode() ) {       return l1;     } else if(  l1.getTailconcCode() .isEmptyconcCode() ) {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,l2) ;     } else {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,tom_append_list_concCode( l1.getTailconcCode() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.CodeList  tom_get_slice_concCode( tom.engine.adt.code.types.CodeList  begin,  tom.engine.adt.code.types.CodeList  end, tom.engine.adt.code.types.CodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcCode()  ||  (end== tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.codelist.ConsconcCode.make( begin.getHeadconcCode() ,( tom.engine.adt.code.types.CodeList )tom_get_slice_concCode( begin.getTailconcCode() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {     if( l1.isEmptyComposite() ) {       return l2;     } else if( l2.isEmptyComposite() ) {       return l1;     } else if(  l1.getTailComposite() .isEmptyComposite() ) {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyAndConstraint() ) {       return l2;     } else if( l2.isEmptyAndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {       if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {     if( l1.isEmptyOrConstraint() ) {       return l2;     } else if( l2.isEmptyOrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {       if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;       } else {         return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }    


  private static Logger logger = Logger.getLogger("tom.engine.typer.NewKernelTyper");

  




  /*
   * pem: why use a state variable here ?
   */
  // List for variables of pattern (match constraints)
  private TomList varPatternList;
  // List for variables of subject and of numeric constraints
  private BQTermList varList;

  // List for type variables of patterns 
  private TomTypeList inputTVarList;
  // List for equation constraints (for fresh type variables)
  private TypeConstraintList equationConstraints;
  // List for subtype constraints (for fresh type variables)
  private TypeConstraintList subtypeConstraints;
  // Set of pairs (freshVar,type)
  private HashMap<TomType,TomType> substitutions;
  // Set of supertypes for each type
  private HashMap<String,TomTypeList> dependencies = new HashMap<String,TomTypeList>();

  private SymbolTable symbolTable;

  private String currentInputFileName;

  private boolean lazyType = false;
  protected void setLazyType() {
    lazyType = true;
  }

  protected void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
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
    /* STEP 1 */
    TomType newValue = value;
    if (substitutions.containsKey(value)) {
      newValue = substitutions.get(value); 
    } 
    substitutions.put(key,newValue);

    /* STEP 2 */
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
   * The method <code>getType</code> gets the type of a term by consulting the
   * SymbolTable.
   * @param bqTerm  the BQTerm requesting a type
   * @return        the type of the BQTerm
   */
  protected TomType getType(BQTerm bqTerm) {
    {{if ( (((Object)bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch371_6= false ; tom.engine.adt.tomtype.types.TomType  tomMatch371_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_3= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_5= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_4= null ;if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch371_6= true ;tomMatch371_3=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_1= tomMatch371_3.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch371_6= true ;tomMatch371_4=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_1= tomMatch371_4.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) {{tomMatch371_6= true ;tomMatch371_5=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_1= tomMatch371_5.getAstType() ;}}}}}}}if (tomMatch371_6) {
 return tomMatch371_1; }}}{if ( (((Object)bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch371_22= false ; tom.engine.adt.code.types.BQTerm  tomMatch371_15= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_17= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_10= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_18= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_12= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_13= null ; tom.engine.adt.tomname.types.TomName  tomMatch371_8= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_14= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_16= null ; tom.engine.adt.code.types.BQTerm  tomMatch371_11= null ;if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch371_22= true ;tomMatch371_10=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_8= tomMatch371_10.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {{tomMatch371_22= true ;tomMatch371_11=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_8= tomMatch371_11.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {{tomMatch371_22= true ;tomMatch371_12=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_8= tomMatch371_12.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {{tomMatch371_22= true ;tomMatch371_13=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_8= tomMatch371_13.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {{tomMatch371_22= true ;tomMatch371_14=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_8= tomMatch371_14.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {{tomMatch371_22= true ;tomMatch371_15=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_8= tomMatch371_15.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {{tomMatch371_22= true ;tomMatch371_16=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_8= tomMatch371_16.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {{tomMatch371_22= true ;tomMatch371_17=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_8= tomMatch371_17.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {{tomMatch371_22= true ;tomMatch371_18=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch371_8= tomMatch371_18.getAstName() ;}}}}}}}}}}}}}}}}}}}if (tomMatch371_22) {if ( (tomMatch371_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch371_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        TomSymbol tSymbol = getSymbolFromName( tomMatch371_8.getString() );
        //DEBUG System.out.println("In getType with BQAppl " + `bqTerm + "\n");
        //DEBUG System.out.println("In getType with type " + getCodomain(tSymbol) + "\n");
        return getCodomain(tSymbol);
      }}}}}}
 
    throw new TomRuntimeException("getType(BQTerm): should not be here.");
  }

  /**
   * The method <code>getType</code> gets the type of a term by consulting the
   * SymbolTable
   * @param tTerm the TomTerm requesting a type
   * @return      the type of the TomTerm
   */
  protected TomType getType(TomTerm tTerm) {
    {{if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 return getType( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getTomTerm() ); }}}}{if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch372_9= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch372_7= null ; tom.engine.adt.tomtype.types.TomType  tomMatch372_5= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch372_8= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch372_9= true ;tomMatch372_7=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));tomMatch372_5= tomMatch372_7.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch372_9= true ;tomMatch372_8=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));tomMatch372_5= tomMatch372_8.getAstType() ;}}}}}if (tomMatch372_9) {
 return tomMatch372_5; }}}{if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch372_11= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch372_11) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch372_11) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch372_11.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch372_18= tomMatch372_11.getHeadconcTomName() ;if ( (tomMatch372_18 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch372_18) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        TomSymbol tSymbol = getSymbolFromName( tomMatch372_18.getString() );
        return getCodomain(tSymbol);
      }}}}}}}}}
 
    throw new TomRuntimeException("getType(TomTerm): should not be here.");
  }

  /**
   * The method <code>getInfoFromTomTerm</code> creates a pair
   * (name,information) for a given term by consulting its attributes.
   * @param tTerm  the TomTerm requesting the informations
   * @return       the information about the TomTerm
   */
  protected Info getInfoFromTomTerm(TomTerm tTerm) {
    {{if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 return getInfoFromTomTerm( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getTomTerm() ); }}}}{if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch373_10= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch373_5= null ; tom.engine.adt.tomname.types.TomName  tomMatch373_6= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch373_8= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch373_9= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch373_10= true ;tomMatch373_8=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));tomMatch373_5= tomMatch373_8.getOptions() ;tomMatch373_6= tomMatch373_8.getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch373_10= true ;tomMatch373_9=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));tomMatch373_5= tomMatch373_9.getOptions() ;tomMatch373_6= tomMatch373_9.getAstName() ;}}}}}if (tomMatch373_10) {
 
        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch373_6, tomMatch373_5) ; 
      }}}{if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch373_13= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch373_13) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch373_13) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch373_13.isEmptyconcTomName() )) {
 
        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch373_13.getHeadconcTomName() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getOptions() ) ; 
      }}}}}}}
 
    return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tom.engine.adt.tomname.types.tomname.Name.make("") ,  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ; 
  }

  /**
   * The method <code>getInfoFromBQTerm</code> creates a pair
   * (name,information) for a given term by consulting its attributes.
   * @param bqTerm the BQTerm requesting the informations
   * @return       the information about the BQTerm
   */
  protected Info getInfoFromBQTerm(BQTerm bqTerm) {
    {{if ( (((Object)bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch374_7= false ; tom.engine.adt.code.types.BQTerm  tomMatch374_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch374_5= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch374_1= null ; tom.engine.adt.tomname.types.TomName  tomMatch374_2= null ; tom.engine.adt.code.types.BQTerm  tomMatch374_6= null ;if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch374_7= true ;tomMatch374_4=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch374_1= tomMatch374_4.getOptions() ;tomMatch374_2= tomMatch374_4.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch374_7= true ;tomMatch374_5=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch374_1= tomMatch374_5.getOptions() ;tomMatch374_2= tomMatch374_5.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch374_7= true ;tomMatch374_6=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));tomMatch374_1= tomMatch374_6.getOptions() ;tomMatch374_2= tomMatch374_6.getAstName() ;}}}}}}}if (tomMatch374_7) {
 
        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch374_2, tomMatch374_1) ; 
      }}}{if ( (((Object)bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) { tom.engine.adt.code.types.BQTerm  tomMatch374__end__12=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));do {{if (!( tomMatch374__end__12.isEmptyComposite() )) { tom.engine.adt.code.types.CompositeMember  tomMatch374_16= tomMatch374__end__12.getHeadComposite() ;if ( (tomMatch374_16 instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )tomMatch374_16) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) {


        return getInfoFromBQTerm( tomMatch374_16.getterm() );
      }}}if ( tomMatch374__end__12.isEmptyComposite() ) {tomMatch374__end__12=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));} else {tomMatch374__end__12= tomMatch374__end__12.getTailComposite() ;}}} while(!( (tomMatch374__end__12==(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) ));}}}}
 
    return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tom.engine.adt.tomname.types.tomname.Name.make("") ,  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ; 
  }

  /**
   * The method <code>setLimTVarSymbolTable</code> sets the lower bound of the
   * counter of type variables. This methods is called by the TyperPlugin
   * after replacing all unknown types of the SymbolTable by type variables and
   * before start the type inference.
   * @param freshTVarSymbolTable  the lower bound of the counter of type
   *                              variables
   */
  private int limTVarSymbolTable;
  protected void setLimTVarSymbolTable(int freshTVarSymbolTable) {
    limTVarSymbolTable = freshTVarSymbolTable;
    globalTypeVarCounter = freshTVarSymbolTable; 
  }

  // Counters for verbose mode 
  private int globalEqConstraintCounter = 0;
  private int globalSubConstraintCounter = 0;
  private int globalTypeVarCounter = 0;
  protected int getEqCounter() { return globalEqConstraintCounter; } 
  protected int getSubCounter() { return globalSubConstraintCounter; } 
  protected int getTVarCounter() { return globalTypeVarCounter; }

  /**
   * The method <code>getFreshTlTIndex</code> increments the counter of type variables. 
   * @return  the incremented counter of type variables
   */
  private int freshTypeVarCounter;
  private int getFreshTlTIndex() {
    globalTypeVarCounter++;
    return freshTypeVarCounter++;
  }

  /**
   * The method <code>getUnknownFreshTypeVar</code> generates a fresh type
   * variable (by considering the global counter of type variables)
   * @return  a new (fresh) type variable
   */
  protected TomType getUnknownFreshTypeVar() {
    TomType tType = symbolTable.TYPE_UNKNOWN;
    {{if ( (((Object)tType) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tType)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tType))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 return  tom.engine.adt.tomtype.types.tomtype.TypeVar.make( (( tom.engine.adt.tomtype.types.TomType )((Object)tType)).getTomType() , getFreshTlTIndex()) ; }}}}}

    throw new TomRuntimeException("getUnknownFreshTypeVar: should not be here.");
  }

  /**
   * The method <code>containsConstraintModuloEqDecoratedSort</code> checks if a given constraint
   * already exists in a constraint type list. The method considers symmetry for
   * equation constraints and equality of decorated sorts in other to allow teh
   * following:
   * insert((A = T^?),C)	->	C,              if (A = T^c) in C \/ (T^c = A) in C 
   *                      ->  {A = T^?} U C,  otherwise
   *
   * insert((T^? = A),C)	->	C,              if (A = T^c) in C \/ (T^c = A) in C 
   *                      ->  {T^? = A} U C,  otherwise
   * 
   * @param tConstraint the constraint to be considered
   * @param tCList      the type constraint list to be traversed
   * @return            'true' if an equal constraint modulo EqOfDecSort already exists in the list
   *                    'false' otherwise            
   */
  protected boolean containsConstraintModuloEqDecoratedSort(TypeConstraint tConstraint, TypeConstraintList
      tCList) {
    {{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch376_4= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch376_5= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;if ( (tomMatch376_4 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_4) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tomMatch376_5 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_5) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch376_5.getTypeOptions() ;if ( (((Object)tom_tOptions) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch376__end__17=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch376__end__17.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch376_28= tomMatch376__end__17.getHeadconcTypeConstraint() ;if ( (tomMatch376_28 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch376_28) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch376_27= tomMatch376_28.getType2() ;if ( (tomMatch376_4== tomMatch376_28.getType1() ) ) {if ( (tomMatch376_27 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_27) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_decoratedtOptions= tomMatch376_27.getTypeOptions() ;if (  tomMatch376_5.getTomType() .equals( tomMatch376_27.getTomType() ) ) {if ( (((Object)tom_decoratedtOptions) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__23=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions));do {{if (!( tomMatch376__end__23.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch376_34= tomMatch376__end__23.getHeadconcTypeOption() ;if ( (tomMatch376_34 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_34) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch376_46= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__39=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions));do {{if (!( tomMatch376__end__39.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch376_42= tomMatch376__end__39.getHeadconcTypeOption() ;if ( (tomMatch376_42 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_42) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch376_46= true ;}}}if ( tomMatch376__end__39.isEmptyconcTypeOption() ) {tomMatch376__end__39=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions));} else {tomMatch376__end__39= tomMatch376__end__39.getTailconcTypeOption() ;}}} while(!( (tomMatch376__end__39==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) ));}if (!(tomMatch376_46)) {



 return true; }}}}if ( tomMatch376__end__23.isEmptyconcTypeOption() ) {tomMatch376__end__23=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions));} else {tomMatch376__end__23= tomMatch376__end__23.getTailconcTypeOption() ;}}} while(!( (tomMatch376__end__23==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) ));}}}}}}}}}if ( tomMatch376__end__17.isEmptyconcTypeConstraint() ) {tomMatch376__end__17=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch376__end__17= tomMatch376__end__17.getTailconcTypeConstraint() ;}}} while(!( (tomMatch376__end__17==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}}}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch376_51= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch376_52= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;if ( (tomMatch376_51 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_51) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch376_51.getTypeOptions() ;if ( (tomMatch376_52 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_52) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (((Object)tom_tOptions) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch376__end__64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch376__end__64.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch376_75= tomMatch376__end__64.getHeadconcTypeConstraint() ;if ( (tomMatch376_75 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch376_75) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch376_74= tomMatch376_75.getType2() ;if ( (tomMatch376_52== tomMatch376_75.getType1() ) ) {if ( (tomMatch376_74 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_74) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_decoratedtOptions= tomMatch376_74.getTypeOptions() ;if (  tomMatch376_51.getTomType() .equals( tomMatch376_74.getTomType() ) ) {if ( (((Object)tom_decoratedtOptions) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__70=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions));do {{if (!( tomMatch376__end__70.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch376_81= tomMatch376__end__70.getHeadconcTypeOption() ;if ( (tomMatch376_81 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_81) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch376_93= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__86=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions));do {{if (!( tomMatch376__end__86.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch376_89= tomMatch376__end__86.getHeadconcTypeOption() ;if ( (tomMatch376_89 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_89) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch376_93= true ;}}}if ( tomMatch376__end__86.isEmptyconcTypeOption() ) {tomMatch376__end__86=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions));} else {tomMatch376__end__86= tomMatch376__end__86.getTailconcTypeOption() ;}}} while(!( (tomMatch376__end__86==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) ));}if (!(tomMatch376_93)) {




 return true; }}}}if ( tomMatch376__end__70.isEmptyconcTypeOption() ) {tomMatch376__end__70=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions));} else {tomMatch376__end__70= tomMatch376__end__70.getTailconcTypeOption() ;}}} while(!( (tomMatch376__end__70==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) ));}}}}}}}}}if ( tomMatch376__end__64.isEmptyconcTypeConstraint() ) {tomMatch376__end__64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch376__end__64= tomMatch376__end__64.getTailconcTypeConstraint() ;}}} while(!( (tomMatch376__end__64==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}}}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch376_98= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch376_99= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;if ( (tomMatch376_98 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_98) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tomMatch376_99 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_99) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch376_99.getTypeOptions() ;if ( (((Object)tom_tOptions) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch376__end__111=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch376__end__111.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch376_122= tomMatch376__end__111.getHeadconcTypeConstraint() ;if ( (tomMatch376_122 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch376_122) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch376_120= tomMatch376_122.getType1() ;if ( (tomMatch376_120 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_120) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_decoratedtOptions= tomMatch376_120.getTypeOptions() ;if (  tomMatch376_99.getTomType() .equals( tomMatch376_120.getTomType() ) ) {if ( (tomMatch376_98== tomMatch376_122.getType2() ) ) {if ( (((Object)tom_decoratedtOptions) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__117=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions));do {{if (!( tomMatch376__end__117.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch376_128= tomMatch376__end__117.getHeadconcTypeOption() ;if ( (tomMatch376_128 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_128) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch376_140= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__133=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions));do {{if (!( tomMatch376__end__133.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch376_136= tomMatch376__end__133.getHeadconcTypeOption() ;if ( (tomMatch376_136 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_136) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch376_140= true ;}}}if ( tomMatch376__end__133.isEmptyconcTypeOption() ) {tomMatch376__end__133=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions));} else {tomMatch376__end__133= tomMatch376__end__133.getTailconcTypeOption() ;}}} while(!( (tomMatch376__end__133==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) ));}if (!(tomMatch376_140)) {




 return true; }}}}if ( tomMatch376__end__117.isEmptyconcTypeOption() ) {tomMatch376__end__117=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions));} else {tomMatch376__end__117= tomMatch376__end__117.getTailconcTypeOption() ;}}} while(!( (tomMatch376__end__117==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) ));}}}}}}}}}if ( tomMatch376__end__111.isEmptyconcTypeConstraint() ) {tomMatch376__end__111=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch376__end__111= tomMatch376__end__111.getTailconcTypeConstraint() ;}}} while(!( (tomMatch376__end__111==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}}}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch376_145= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch376_146= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;if ( (tomMatch376_145 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_145) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch376_145.getTypeOptions() ;if ( (tomMatch376_146 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_146) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (((Object)tom_tOptions) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch376__end__158=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch376__end__158.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch376_169= tomMatch376__end__158.getHeadconcTypeConstraint() ;if ( (tomMatch376_169 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch376_169) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch376_167= tomMatch376_169.getType1() ;if ( (tomMatch376_167 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_167) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_decoratedtOptions= tomMatch376_167.getTypeOptions() ;if (  tomMatch376_145.getTomType() .equals( tomMatch376_167.getTomType() ) ) {if ( (tomMatch376_146== tomMatch376_169.getType2() ) ) {if ( (((Object)tom_decoratedtOptions) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__164=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions));do {{if (!( tomMatch376__end__164.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch376_175= tomMatch376__end__164.getHeadconcTypeOption() ;if ( (tomMatch376_175 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_175) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch376_187= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__180=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions));do {{if (!( tomMatch376__end__180.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch376_183= tomMatch376__end__180.getHeadconcTypeOption() ;if ( (tomMatch376_183 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_183) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch376_187= true ;}}}if ( tomMatch376__end__180.isEmptyconcTypeOption() ) {tomMatch376__end__180=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions));} else {tomMatch376__end__180= tomMatch376__end__180.getTailconcTypeOption() ;}}} while(!( (tomMatch376__end__180==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions))) ));}if (!(tomMatch376_187)) {




 return true; }}}}if ( tomMatch376__end__164.isEmptyconcTypeOption() ) {tomMatch376__end__164=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions));} else {tomMatch376__end__164= tomMatch376__end__164.getTailconcTypeOption() ;}}} while(!( (tomMatch376__end__164==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_decoratedtOptions))) ));}}}}}}}}}if ( tomMatch376__end__158.isEmptyconcTypeConstraint() ) {tomMatch376__end__158=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch376__end__158= tomMatch376__end__158.getTailconcTypeConstraint() ;}}} while(!( (tomMatch376__end__158==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}}}}}}}}}

    return false;
  }

  /**
   * The method <code>containsConstraint</code> checks if a given constraint
   * already exists in a constraint type list. The method considers symmetry for
   * equation constraints. 
   * @param tConstraint the constraint to be considered
   * @param tCList      the type constraint list to be traversed
   * @return            'true' if the constraint already exists in the list
   *                    'false' otherwise            
   */
  protected boolean containsConstraint(TypeConstraint tConstraint, TypeConstraintList
      tCList) {
    {{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch377__end__9=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch377__end__9.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch377_14= tomMatch377__end__9.getHeadconcTypeConstraint() ;boolean tomMatch377_19= false ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch377_16= null ; tom.engine.adt.tomtype.types.TomType  tomMatch377_12= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch377_15= null ; tom.engine.adt.tomtype.types.TomType  tomMatch377_13= null ;if ( (tomMatch377_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch377_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch377_19= true ;tomMatch377_15=tomMatch377_14;tomMatch377_12= tomMatch377_15.getType1() ;tomMatch377_13= tomMatch377_15.getType2() ;}} else {if ( (tomMatch377_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch377_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch377_19= true ;tomMatch377_16=tomMatch377_14;tomMatch377_12= tomMatch377_16.getType1() ;tomMatch377_13= tomMatch377_16.getType2() ;}}}}}if (tomMatch377_19) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ==tomMatch377_12) ) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ==tomMatch377_13) ) {


 return true; }}}}if ( tomMatch377__end__9.isEmptyconcTypeConstraint() ) {tomMatch377__end__9=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch377__end__9= tomMatch377__end__9.getTailconcTypeConstraint() ;}}} while(!( (tomMatch377__end__9==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch377__end__29=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch377__end__29.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch377_34= tomMatch377__end__29.getHeadconcTypeConstraint() ;if ( (tomMatch377_34 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch377_34) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() == tomMatch377_34.getType1() ) ) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() == tomMatch377_34.getType2() ) ) {



 return true; }}}}}if ( tomMatch377__end__29.isEmptyconcTypeConstraint() ) {tomMatch377__end__29=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch377__end__29= tomMatch377__end__29.getTailconcTypeConstraint() ;}}} while(!( (tomMatch377__end__29==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch377__end__47=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch377__end__47.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch377_52= tomMatch377__end__47.getHeadconcTypeConstraint() ;if ( (tomMatch377_52 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch377_52) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() == tomMatch377_52.getType1() ) ) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() == tomMatch377_52.getType2() ) ) {



 return true; }}}}}if ( tomMatch377__end__47.isEmptyconcTypeConstraint() ) {tomMatch377__end__47=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch377__end__47= tomMatch377__end__47.getTailconcTypeConstraint() ;}}} while(!( (tomMatch377__end__47==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}}}}

    return containsConstraintModuloEqDecoratedSort(tConstraint,tCList);
    //return false;
  } 

  /*
   * pem: use if(...==... && typeConstraints.contains(...))
   */
  /**
   * The method <code>addEqConstraint</code> insert an equation (i.e. a type
   * constraint) into a given type constraint list only if this constraint does
   * not yet exist into the list and if it does not contains "EmptyTypes". 
   * @param tConstraint the equation to be inserted into the type constraint list
   * @param tCList      the constraint type list where the constraint will be
   *                    inserted
   * @return            the list resulting of the insertion
   */
  protected TypeConstraintList addEqConstraint(TypeConstraint tConstraint,
      TypeConstraintList tCList) {
    if (!containsConstraint(tConstraint,tCList)) {
      {{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch378_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch378_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch378_1; tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch378_2;boolean tomMatch378_12= false ;if ( (tomMatch378_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch378_2) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {if ( (tom_t2==tomMatch378_2) ) {tomMatch378_12= true ;}}}if (!(tomMatch378_12)) {boolean tomMatch378_11= false ;if ( (tomMatch378_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch378_1) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {if ( (tom_t1==tomMatch378_1) ) {tomMatch378_11= true ;}}}if (!(tomMatch378_11)) {if (!( (tom_t2==tom_t1) )) {

 
            return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
          }}}}}}}}

    }
    return tCList;
  }

  /**
   * The method <code>addSubConstraint</code> insert a subtype constraint (i.e. a type
   * constraint) into a given type constraint list only if this constraint does
   * not yet exist into the list and if it does not contains "EmptyTypes". 
   * @param tConstraint the subtype constraint to be inserted into the type
   *                    constraint list
   * @param tCList      the constraint type list where the constraint will be
   *                    inserted
   * @return            the list resulting of the insertion
   */
  protected TypeConstraintList addSubConstraint(TypeConstraint tConstraint,
      TypeConstraintList tCList) {
    if (!containsConstraint(tConstraint,tCList)) {
      //DEBUG System.out.println("addSubConstraint: tCList = " + tCList);
      {{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch379_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch379_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch379_1; tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch379_2;boolean tomMatch379_12= false ;if ( (tomMatch379_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch379_2) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {if ( (tom_t2==tomMatch379_2) ) {tomMatch379_12= true ;}}}if (!(tomMatch379_12)) {boolean tomMatch379_11= false ;if ( (tomMatch379_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch379_1) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {if ( (tom_t1==tomMatch379_1) ) {tomMatch379_11= true ;}}}if (!(tomMatch379_11)) {if (!( (tom_t2==tom_t1) )) {

 
            return tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
          }}}}}}}}

    }
    return tCList;
  }

  /**
   * The method <code>generateDependencies</code> generates a
   * hashMap called "dependencies" having pairs (typeName,supertypesList), where
   * supertypeslist is a list with all the related proper supertypes for each
   * ground type used into a code. The list is obtained by reflexive and
   * transitive closure over the direct supertype relation defined by the user
   * when defining the mappings.
   * For example, to generate the supertypes of T2, where T2 is a ground type, we
   * follow two steps:
   * <p>
   * STEP 1:  a) get the list supertypes_T3, put(T2,({T3} U supertype_T3)) and go
   *          to step 2, if there exists a declaration of form T2<:T3
   *          b) put(T2,{}), otherwise 
   * <p>
   * STEP 2:  put(T1,(supertypes_T1 U supertypes_T2)), for each (T1,{...,T2,...}) in dependencies
   */
  // TODO: optimization 1 - add a test before adding a new pair in the hash map "dependencies" 
  // TODO: optimization 2 - generate the dependencies once at the beginning of
  // type inference and for all types of symbol table instead of only "used
  // types".
  protected void generateDependencies() {
    TomTypeList superTypes;
    TomTypeList supOfSubTypes;
    String currentTypeName;
    for(TomType currentType:symbolTable.getUsedTypes()) {
      currentTypeName = currentType.getTomType();
      superTypes =  tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
      //DEBUG System.out.println("In generateDependencies -- for 1 : currentType = " +
      //DEBUG    currentType);
      {{if ( (((Object)currentType) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)currentType)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)currentType))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch380_1= (( tom.engine.adt.tomtype.types.TomType )((Object)currentType)).getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch380_1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch380_1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch380__end__8=tomMatch380_1;do {{if (!( tomMatch380__end__8.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch380_12= tomMatch380__end__8.getHeadconcTypeOption() ;if ( (tomMatch380_12 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch380_12) instanceof tom.engine.adt.tomtype.types.typeoption.SubtypeDecl) ) { String  tom_supTypeName= tomMatch380_12.getTomType() ;


          //DEBUG System.out.println("In generateDependencies -- match : supTypeName = "
          //DEBUG     + `supTypeName + " and supType = " + supType);
          if (dependencies.containsKey(tom_supTypeName)) {
            superTypes = dependencies.get(tom_supTypeName); 
          }
          TomType supType = symbolTable.getType(tom_supTypeName);
          if (supType != null) {
            superTypes =  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(supType,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )) ;  

            /* STEP 2 */
            for(String subType:dependencies.keySet()) {
              supOfSubTypes = dependencies.get(subType);
              //DEBUG System.out.println("In generateDependencies -- for 2: supOfSubTypes = " +
              //DEBUG     supOfSubTypes);
              {{if ( (((Object)supOfSubTypes) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch381__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes));do {{if (!( tomMatch381__end__4.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch381_8= tomMatch381__end__4.getHeadconcTomType() ;if ( (tomMatch381_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch381_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if (  (( tom.engine.adt.tomtype.types.TomType )((Object)currentType)).getTomType() .equals( tomMatch381_8.getTomType() ) ) {



                    /* 
                     * Replace list of superTypes of "subType" by a new one
                     * containing the superTypes of "currentType" which is also a
                     * superType 
                     */
                    dependencies.put(subType,tom_append_list_concTomType(supOfSubTypes,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )));
                  }}}}if ( tomMatch381__end__4.isEmptyconcTomType() ) {tomMatch381__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes));} else {tomMatch381__end__4= tomMatch381__end__4.getTailconcTomType() ;}}} while(!( (tomMatch381__end__4==(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes))) ));}}}}

            }
          } else {
            TomMessage.error(logger,getCurrentInputFileName(),0,
                TomMessage.typetermNotDefined,tom_supTypeName);
          }
        }}}if ( tomMatch380__end__8.isEmptyconcTypeOption() ) {tomMatch380__end__8=tomMatch380_1;} else {tomMatch380__end__8= tomMatch380__end__8.getTailconcTypeOption() ;}}} while(!( (tomMatch380__end__8==tomMatch380_1) ));}}}}}}

      //DEBUG System.out.println("In generateDependencies -- end: superTypes = " +
      //DEBUG     superTypes);
      dependencies.put(currentTypeName,superTypes);
    }
  }


  protected void addPositiveTVar(TomType tType) {
    inputTVarList =  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(tType,tom_append_list_concTomType(inputTVarList, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )) ;
  }

  /**
   * The method <code>addTomTerm</code> insert a TomTerm into the global
   * <code>varPatternList</code>.
   * @param tTerm  the TomTerm to be inserted
   */
  protected void addTomTerm(TomTerm tTerm) {
    varPatternList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tTerm,tom_append_list_concTomTerm(varPatternList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
  }

  /**
   * The method <code>addBQTerm</code> insert a BQTerm into the global
   * <code>varList</code>.
   * @param bqTerm  the BQTerm to be inserted
   */
  protected void addBQTerm(BQTerm bqTerm) {
    varList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(bqTerm,tom_append_list_concBQTerm(varList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
  }

  /**
   * The method <code>resetVarList</code> checks if <code>varList</code> contains
   * a BQTerm which is in the <code>localVarPatternList</code> but is not in
   * <code>varPatternList</code> (i.e. the globalVarPatternList</code>. Then,
   * this BQTerm is removed from <code>varList</code>.
   * @param localVarPatternList   the TomList to be reset
   */
  protected void resetVarList(TomList localVarPatternList) {
    BQTermList bqTList = varList;
    for(TomTerm tTerm: varPatternList.getCollectionconcTomTerm()) {
      {{if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch382_27= false ; tom.engine.adt.tomname.types.TomName  tomMatch382_3= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch382_5= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch382_6= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch382_27= true ;tomMatch382_5=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));tomMatch382_3= tomMatch382_5.getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch382_27= true ;tomMatch382_6=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));tomMatch382_3= tomMatch382_6.getAstName() ;}}}}}if (tomMatch382_27) {if ( (((Object)localVarPatternList) instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((Object)bqTList) instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)bqTList))) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)bqTList))) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch382__end__10=(( tom.engine.adt.code.types.BQTermList )((Object)bqTList));do {{if (!( tomMatch382__end__10.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch382_14= tomMatch382__end__10.getHeadconcBQTerm() ;boolean tomMatch382_26= false ; tom.engine.adt.tomname.types.TomName  tomMatch382_13= null ; tom.engine.adt.code.types.BQTerm  tomMatch382_15= null ; tom.engine.adt.code.types.BQTerm  tomMatch382_16= null ;if ( (tomMatch382_14 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch382_14) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch382_26= true ;tomMatch382_15=tomMatch382_14;tomMatch382_13= tomMatch382_15.getAstName() ;}} else {if ( (tomMatch382_14 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch382_14) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch382_26= true ;tomMatch382_16=tomMatch382_14;tomMatch382_13= tomMatch382_16.getAstName() ;}}}}}if (tomMatch382_26) {if ( (tomMatch382_3==tomMatch382_13) ) {boolean tomMatch382_25= false ;if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch382__end__20=(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList));do {{if (!( tomMatch382__end__20.isEmptyconcTomTerm() )) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))== tomMatch382__end__20.getHeadconcTomTerm() ) ) {tomMatch382_25= true ;}}if ( tomMatch382__end__20.isEmptyconcTomTerm() ) {tomMatch382__end__20=(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList));} else {tomMatch382__end__20= tomMatch382__end__20.getTailconcTomTerm() ;}}} while(!( (tomMatch382__end__20==(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList))) ));}if (!(tomMatch382_25)) {


            bqTList = tom_append_list_concBQTerm(tom_get_slice_concBQTerm((( tom.engine.adt.code.types.BQTermList )((Object)bqTList)),tomMatch382__end__10, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ),tom_append_list_concBQTerm( tomMatch382__end__10.getTailconcBQTerm() , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ));
          }}}}if ( tomMatch382__end__10.isEmptyconcBQTerm() ) {tomMatch382__end__10=(( tom.engine.adt.code.types.BQTermList )((Object)bqTList));} else {tomMatch382__end__10= tomMatch382__end__10.getTailconcBQTerm() ;}}} while(!( (tomMatch382__end__10==(( tom.engine.adt.code.types.BQTermList )((Object)bqTList))) ));}}}}}}}

    }
    varList = bqTList;
  }

  /**
   * The method <code>init</code> reset the counter of type variables
   * <code>freshTypeVarCounter</code> and empties all global lists and hashMaps which means to
   * empty <code>varPatternList</code>, <code>varList</code>,
   * <code>equationConstraints</code>, <code>subtypeConstraints</code> and <code>substitutions</code>
   */
  private void init() {
    freshTypeVarCounter = limTVarSymbolTable;
    varPatternList =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
    varList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    inputTVarList =  tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
    equationConstraints =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    subtypeConstraints =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    substitutions = new HashMap<TomType,TomType>();
  }

  /**
   * The method <code>collectKnownTypesFromCode</code> creates an instance of
   * the class <code>CollectKnownTypes</code> and calls its method
   * <code>visitLight</code> to traverse a code. 
   * @param  subject the code to be traversed/transformed
   * @return         the code resulting of a transformation
   */
  private Code collectKnownTypesFromCode(Code subject) {
    try {
      return tom_make_TopDownIdStopOnSuccess(tom_make_CollectKnownTypes(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }

  /**
   * The class <code>CollectKnownTypes</code> is generated from a strategy which
   * initially types all terms by using their correspondent type in symbol table
   * or a fresh type variable :
   * CASE 1 : Type(name, EmptyTargetLanguageType()) -> Type(name, foundType) if
   * name is in TypeTable
   * CASE 2 : Type(name, EmptyTargetLanguageType()) -> TypeVar(name, Index(i))
   * if name is not in TypeTable
   * @param nkt an instance of object NewKernelTyper
   * @return    the code resulting of a transformation
   */
  public static class CollectKnownTypes extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public CollectKnownTypes( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch383_2= (( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg)).getTlType() ; String  tom_tomType= (( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg)).getTomType() ;if ( (tomMatch383_2 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch383_2) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {


        TomType newType = nkt.symbolTable.getType(tom_tomType);
        if (newType == null) {
          /*
           * This happens when :
           * tomType != unknown type AND (newType == null)
           * tomType == unknown type
           */
          newType =  tom.engine.adt.tomtype.types.tomtype.TypeVar.make(tom_tomType, nkt.getFreshTlTIndex()) ;
        }
        return newType;
      }}}}}}}return _visit_TomType(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectKnownTypes( NewKernelTyper  t0) { return new CollectKnownTypes(t0);}



  /**
   * The method <code>inferAllTypes</code> is the start-up of the inference
   * process. It is a generic method and it is called for the first time by the
   * NewTyper.
   * @param term        the expression needing type inference
   * @param contextType the type related to the current expression
   * @return            the resulting typed expression 
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
   * tries to infer types of all variables on a given expression
   * <p> 
   * It starts by searching for a Code <code>Tom</code> or
   * <code>TomInclude</code> and calling <code>inferCodeList</code> in order to
   * apply rule CT-BLOCK for each block of ConstraintInstruction.
   * <p>
   * Then it searches for a Instruction
   * <code>Match(constraintInstructionList,option)</code> and calls
   * <code>inferConstraintInstructionList</code> in order to apply rule CT-RULE
   * for each single constraintInstruction
   * <p>
   * Then it searches for variables and star variables (TomTerms and BQTerms) and
   * applies rules CT-ALIAS, CT-ANTI, CT-VAR, CT-SVAR, CT-FUN,
   * CT-EMPTY, CT-ELEM, CT-MERGE or CT-STAR to a "pattern" (a TomTerm) or a
   * "subject" (a BQTerm) in order to infer types of its variables.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * CT-ALIAS rule:
   * IF found "x@e:A and "x:T" already exists in context
   * THEN adds a type constraint "A = T" and infers type of "e:A"
   * <p>
   * CT-ANTI rule:
   * IF found "!(e):A"
   * THEN infers type of "e:A"
   * <p>
   * CT-VAR rule (resp. CT-SVAR): 
   * IF found "x:A" (resp. "x*:A") and "x:A1" (resp. "x*:A1") already exists in
   *    context 
   * THEN adds a type constraint "A1 <: A"
   * <p>
   * CT-FUN rule:
   * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
   * THEN infers type of arguments and add a type constraint "T <:A"
   * <p>
   * CT-EMPTY rule:
   * IF found "l():A" and "l:T1*->T" exists in SymbolTable
   * THEN adds a type constraint "T <: A"       
   * <p>
   * CT-ELEM rule:
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" does not represent a list with head symbol "l"
   * <p>
   * CT-MERGE rule:
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" represents a list with
   *      head symbol "l"
   * <p>
   * CT-STAR rule:
   * Equals to CT-MERGE but with a star variable "x*" instead of "e"
   * This rule is necessary because it differed from CT-MERGE in the
   * sense of the type of the last argument ("x*" here) is unknown 
   * @param contextType the fresh generated previously and attributed to the term
   * @param nkt an instance of object NewKernelTyper
   */
  public static class inferTypes extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomtype.types.TomType  contextType;private  NewKernelTyper  nkt;public inferTypes( tom.engine.adt.tomtype.types.TomType  contextType,  NewKernelTyper  nkt) {super(( new tom.library.sl.Fail() ));this.contextType=contextType;this.nkt=nkt;}public  tom.engine.adt.tomtype.types.TomType  getcontextType() {return contextType;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {return ((T)visit_TomVisit((( tom.engine.adt.tomsignature.types.TomVisit )v),introspector));}if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.Code) ) {return ((T)visit_Code((( tom.engine.adt.code.types.Code )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.Code  _visit_Code( tom.engine.adt.code.types.Code  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.Code )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  _visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomsignature.types.TomVisit )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.code.types.BQTerm  tom_bqVar=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));






















































































































        nkt.checkNonLinearityOfBQVariables(tom_bqVar);
        nkt.subtypeConstraints = nkt.addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstType() , contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstName() ,  (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getOptions() ) ) ,nkt.subtypeConstraints);  
        return tom_bqVar;
      }}}}{if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.code.types.BQTerm  tom_bqVarStar=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));


        nkt.checkNonLinearityOfBQVariables(tom_bqVarStar);
        nkt.equationConstraints =
          nkt.addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstType() , contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstName() ,  (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getOptions() ) ) ,nkt.equationConstraints);
        return tom_bqVarStar;
      }}}}{if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch384_14= (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getOptions() ;if ( (tomMatch384_14 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch384_14) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch384_14; tom.engine.adt.code.types.BQTermList  tom_bqTList= (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getArgs() ;


        TomSymbol tSymbol = nkt.getSymbolFromName( tomMatch384_14.getString() );
        TomType codomain = contextType;
        if (tSymbol == null) {
          tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;
          //DEBUG System.out.println("name = " + `name);
          //DEBUG System.out.println("context = " + contextType);
          BQTermList newBQTList = nkt.inferBQTermList(tom_bqTList, tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ,contextType);
          /* PEM: why contextType ? */
          return  tom.engine.adt.code.types.bqterm.FunctionCall.make(tom_aName, contextType, newBQTList) ; 
        } else {
          {{if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch385_2= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getAstName() ;if ( (tomMatch385_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch385_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch385_8= tomMatch385_2.getCodomain() ;if ( (tomMatch385_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch385_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 
              codomain = tomMatch385_8;
              if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) {
                /* Apply decoration for types of list operators */
                TypeOptionList newTOptions =  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom_symName) ,tom_append_list_concTypeOption( tomMatch385_8.getTypeOptions() , tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;
                codomain =  tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch385_8.getTomType() ,  tomMatch385_8.getTlType() ) ;
                tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_symName,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tomMatch385_2.getDomain() , codomain) ,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getPairNameDeclList() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getOptions() ) ; 
              }  
            }}}}}}}}}

          nkt.subtypeConstraints = nkt.addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,nkt.subtypeConstraints);

          BQTermList newBQTList =
            nkt.inferBQTermList(tom_bqTList,tSymbol,contextType);
          return  tom.engine.adt.code.types.bqterm.BQAppl.make(tom_optionList, tom_aName, newBQTList) ;
        }
      }}}}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) { nkt.inferAllTypes( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getTomTerm() ,contextType); }}}}{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstType() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() ; tom.engine.adt.tomterm.types.TomTerm  tom_var=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg));         nkt.checkNonLinearityOfVariables(tom_var);         nkt.subtypeConstraints =           nkt.addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_aType, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstName() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() ) ) ,nkt.subtypeConstraints);           ConstraintList newCList = tom_cList;         {{if ( (((Object)tom_cList) instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch387_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getHeadconcConstraint() ;if ( (tomMatch387_4 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch387_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch387_4.getVar() ;if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getTailconcConstraint() .isEmptyconcConstraint() ) {             nkt.equationConstraints =               nkt.addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), tom_aType, nkt.getInfoFromTomTerm(tom_boundTerm)) ,nkt.equationConstraints);            }}}}}}}}         return tom_var.setConstraints(newCList);       }}}}{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstType() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() ; tom.engine.adt.tomterm.types.TomTerm  tom_varStar=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg));         nkt.checkNonLinearityOfVariables(tom_varStar);         nkt.equationConstraints =           nkt.addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstName() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() ) ) ,nkt.equationConstraints);           ConstraintList newCList = tom_cList;         {{if ( (((Object)tom_cList) instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch388_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getHeadconcConstraint() ;if ( (tomMatch388_4 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch388_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch388_4.getVar() ;if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getTailconcConstraint() .isEmptyconcConstraint() ) {             nkt.equationConstraints =               nkt.addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), tom_aType, nkt.getInfoFromTomTerm(tom_boundTerm)) ,nkt.equationConstraints);            }}}}}}}}         return tom_varStar.setConstraints(newCList);       }}}}{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch386_20= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch386_20) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch386_20) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch386_20.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch386_29= tomMatch386_20.getHeadconcTomName() ;if ( (tomMatch386_29 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch386_29) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomslot.types.SlotList  tom_sList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getSlots() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() ;         /* In case of a String, tomName is "" for ("a","b") */         TomSymbol tSymbol = nkt.getSymbolFromName( tomMatch386_29.getString() );          TomType codomain = contextType;          /* IF_3 */         if (tSymbol == null) {           tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;         } else {           /* This code can not be moved to IF_2 because tSymbol may don't be            "null" since the begginning and then does not enter into neither IF_1 nor */           /* IF_2 */           TomSymbol newtSymbol = tSymbol;           {{if ( (((Object)newtSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch389_2= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getAstName() ;if ( (tomMatch389_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch389_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch389_8= tomMatch389_2.getCodomain() ;if ( (tomMatch389_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch389_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {                codomain = tomMatch389_8;               if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) {                 /* Apply decoration for types of list operators */                 TypeOptionList newTOptions =  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom_symName) ,tom_append_list_concTypeOption( tomMatch389_8.getTypeOptions() , tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;                 codomain =  tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch389_8.getTomType() ,  tomMatch389_8.getTlType() ) ;                 tSymbol =                    tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_symName,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tomMatch389_2.getDomain() , codomain) ,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getPairNameDeclList() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getOptions() ) ;                }               }}}}}}}}}           nkt.subtypeConstraints = nkt.addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch386_20.getHeadconcTomName() , tom_optionList) ) ,nkt.subtypeConstraints);         }          ConstraintList newCList = tom_cList;         {{if ( (((Object)tom_cList) instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch390_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getHeadconcConstraint() ;if ( (tomMatch390_4 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch390_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch390_4.getVar() ;if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getTailconcConstraint() .isEmptyconcConstraint() ) {             nkt.equationConstraints =               nkt.addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), codomain, nkt.getInfoFromTomTerm(tom_boundTerm)) ,nkt.equationConstraints);            }}}}}}}}          SlotList newSList =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;         if (!tom_sList.isEmptyconcSlot()) {           newSList=             nkt.inferSlotList(tom_sList,tSymbol,contextType);         }         return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_optionList, tomMatch386_20, newSList, newCList) ;       }}}}}}}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg)) instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )(( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg))) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) {          BQTermList BQTList = nkt.varList;         ConstraintInstructionList newCIList =           nkt.inferConstraintInstructionList( (( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg)).getAstConstraintInstructionList() );         nkt.varList = BQTList;         return  tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make( (( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg)).getVNode() , newCIList,  (( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg)).getOptions() ) ;       }}}}}return _visit_TomVisit(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)tom__arg)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)tom__arg))) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {         BQTermList BQTList = nkt.varList;         ConstraintInstructionList newCIList =           nkt.inferConstraintInstructionList( (( tom.engine.adt.tominstruction.types.Instruction )((Object)tom__arg)).getConstraintInstructionList() );         nkt.varList = BQTList;         return  tom.engine.adt.tominstruction.types.instruction.Match.make(newCIList,  (( tom.engine.adt.tominstruction.types.Instruction )((Object)tom__arg)).getOptions() ) ;       }}}}}return _visit_Instruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.Code  visit_Code( tom.engine.adt.code.types.Code  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.Code) ) {boolean tomMatch393_5= false ; tom.engine.adt.code.types.Code  tomMatch393_4= null ; tom.engine.adt.code.types.CodeList  tomMatch393_1= null ; tom.engine.adt.code.types.Code  tomMatch393_3= null ;if ( ((( tom.engine.adt.code.types.Code )((Object)tom__arg)) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)tom__arg))) instanceof tom.engine.adt.code.types.code.Tom) ) {{tomMatch393_5= true ;tomMatch393_3=(( tom.engine.adt.code.types.Code )((Object)tom__arg));tomMatch393_1= tomMatch393_3.getCodeList() ;}} else {if ( ((( tom.engine.adt.code.types.Code )((Object)tom__arg)) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)tom__arg))) instanceof tom.engine.adt.code.types.code.TomInclude) ) {{tomMatch393_5= true ;tomMatch393_4=(( tom.engine.adt.code.types.Code )((Object)tom__arg));tomMatch393_1= tomMatch393_4.getCodeList() ;}}}}}if (tomMatch393_5) {         nkt.generateDependencies();         CodeList newCList = nkt.inferCodeList(tomMatch393_1);         return (( tom.engine.adt.code.types.Code )((Object)tom__arg)).setCodeList(newCList);       }}}}return _visit_Code(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_inferTypes( tom.engine.adt.tomtype.types.TomType  t0,  NewKernelTyper  t1) { return new inferTypes(t0,t1);}



  /**
   * The method <code>checkNonLinearityOfVariables</code> searches for variables
   * occurring more than once in a condition.
   * <p>
   * For each variable of type
   * "TomTerm" that already exists in <code>varPatternList</code> or in
   * <code>varList</code>, a type constraint is added to
   * <code>equationConstraints</code> to ensure that  both
   * variables have same type (this happens in case of non-linearity).
   * <p>
   * OBS.: we also need to check the <code>varList</code> since a Variable/VariableStar can have
   * occurred in a previous condition as a BQVariable/BQVariableStar, in the
   * case of a composed condition
   * e.g. (x < 10 ) && f(x) << e -> { action }
   * @param var the variable to have the linearity checked
   */
  private void checkNonLinearityOfVariables(TomTerm var) {
    {{if ( (((Object)var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch394_41= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch394_3= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch394_8= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch394_7= null ; tom.engine.adt.tomname.types.TomName  tomMatch394_4= null ; tom.engine.adt.tomtype.types.TomType  tomMatch394_5= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch394_41= true ;tomMatch394_7=(( tom.engine.adt.tomterm.types.TomTerm )((Object)var));tomMatch394_3= tomMatch394_7.getOptions() ;tomMatch394_4= tomMatch394_7.getAstName() ;tomMatch394_5= tomMatch394_7.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch394_41= true ;tomMatch394_8=(( tom.engine.adt.tomterm.types.TomTerm )((Object)var));tomMatch394_3= tomMatch394_8.getOptions() ;tomMatch394_4= tomMatch394_8.getAstName() ;tomMatch394_5= tomMatch394_8.getAstType() ;}}}}}if (tomMatch394_41) { tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch394_3; tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch394_4; tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch394_5;if ( (((Object)varPatternList) instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch394__end__12=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));do {{if (!( tomMatch394__end__12.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch394_23= tomMatch394__end__12.getHeadconcTomTerm() ;boolean tomMatch394_38= false ; tom.engine.adt.tomname.types.TomName  tomMatch394_21= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch394_25= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch394_24= null ; tom.engine.adt.tomtype.types.TomType  tomMatch394_22= null ;if ( (tomMatch394_23 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch394_23) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch394_38= true ;tomMatch394_24=tomMatch394_23;tomMatch394_21= tomMatch394_24.getAstName() ;tomMatch394_22= tomMatch394_24.getAstType() ;}} else {if ( (tomMatch394_23 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch394_23) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch394_38= true ;tomMatch394_25=tomMatch394_23;tomMatch394_21= tomMatch394_25.getAstName() ;tomMatch394_22= tomMatch394_25.getAstType() ;}}}}}if (tomMatch394_38) {if ( (tom_aName==tomMatch394_21) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch394_22;boolean tomMatch394_37= false ;if ( (tom_aType1==tomMatch394_22) ) {if ( (tom_aType2==tomMatch394_22) ) {tomMatch394_37= true ;}}if (!(tomMatch394_37)) {






          equationConstraints = addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,equationConstraints);
        }}}}if ( tomMatch394__end__12.isEmptyconcTomTerm() ) {tomMatch394__end__12=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));} else {tomMatch394__end__12= tomMatch394__end__12.getTailconcTomTerm() ;}}} while(!( (tomMatch394__end__12==(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) ));}}if ( (((Object)varList) instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)varList))) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)varList))) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch394__end__18=(( tom.engine.adt.code.types.BQTermList )((Object)varList));do {{if (!( tomMatch394__end__18.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch394_28= tomMatch394__end__18.getHeadconcBQTerm() ;boolean tomMatch394_40= false ; tom.engine.adt.code.types.BQTerm  tomMatch394_30= null ; tom.engine.adt.tomname.types.TomName  tomMatch394_26= null ; tom.engine.adt.code.types.BQTerm  tomMatch394_29= null ; tom.engine.adt.tomtype.types.TomType  tomMatch394_27= null ;if ( (tomMatch394_28 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch394_28) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch394_40= true ;tomMatch394_29=tomMatch394_28;tomMatch394_26= tomMatch394_29.getAstName() ;tomMatch394_27= tomMatch394_29.getAstType() ;}} else {if ( (tomMatch394_28 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch394_28) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch394_40= true ;tomMatch394_30=tomMatch394_28;tomMatch394_26= tomMatch394_30.getAstName() ;tomMatch394_27= tomMatch394_30.getAstType() ;}}}}}if (tomMatch394_40) {if ( (tom_aName==tomMatch394_26) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch394_27;boolean tomMatch394_39= false ;if ( (tom_aType1==tomMatch394_27) ) {if ( (tom_aType2==tomMatch394_27) ) {tomMatch394_39= true ;}}if (!(tomMatch394_39)) {           equationConstraints = addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,equationConstraints);         }}}}if ( tomMatch394__end__18.isEmptyconcBQTerm() ) {tomMatch394__end__18=(( tom.engine.adt.code.types.BQTermList )((Object)varList));} else {tomMatch394__end__18= tomMatch394__end__18.getTailconcBQTerm() ;}}} while(!( (tomMatch394__end__18==(( tom.engine.adt.code.types.BQTermList )((Object)varList))) ));}}}}}{if ( (((Object)var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch394_63= false ; tom.engine.adt.tomtype.types.TomType  tomMatch394_45= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch394_47= null ; tom.engine.adt.tomname.types.TomName  tomMatch394_44= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch394_48= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch394_63= true ;tomMatch394_47=(( tom.engine.adt.tomterm.types.TomTerm )((Object)var));tomMatch394_44= tomMatch394_47.getAstName() ;tomMatch394_45= tomMatch394_47.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch394_63= true ;tomMatch394_48=(( tom.engine.adt.tomterm.types.TomTerm )((Object)var));tomMatch394_44= tomMatch394_48.getAstName() ;tomMatch394_45= tomMatch394_48.getAstType() ;}}}}}if (tomMatch394_63) { tom.engine.adt.tomtype.types.TomType  tom_aType=tomMatch394_45;if ( (((Object)varPatternList) instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch394__end__52=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));do {{if (!( tomMatch394__end__52.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch394_57= tomMatch394__end__52.getHeadconcTomTerm() ;boolean tomMatch394_62= false ; tom.engine.adt.tomname.types.TomName  tomMatch394_55= null ; tom.engine.adt.tomtype.types.TomType  tomMatch394_56= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch394_59= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch394_58= null ;if ( (tomMatch394_57 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch394_57) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch394_62= true ;tomMatch394_58=tomMatch394_57;tomMatch394_55= tomMatch394_58.getAstName() ;tomMatch394_56= tomMatch394_58.getAstType() ;}} else {if ( (tomMatch394_57 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch394_57) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch394_62= true ;tomMatch394_59=tomMatch394_57;tomMatch394_55= tomMatch394_59.getAstName() ;tomMatch394_56= tomMatch394_59.getAstType() ;}}}}}if (tomMatch394_62) {if ( (tomMatch394_44==tomMatch394_55) ) {if ( (tom_aType==tomMatch394_56) ) {



          //DEBUG System.out.println("Add type '" + `aType + "' of var '" + `var +"'\n");
          addPositiveTVar(tom_aType);
        }}}}if ( tomMatch394__end__52.isEmptyconcTomTerm() ) {tomMatch394__end__52=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));} else {tomMatch394__end__52= tomMatch394__end__52.getTailconcTomTerm() ;}}} while(!( (tomMatch394__end__52==(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) ));}}}}}}

  }

  /**
   * The method <code>checkNonLinearityOfBQVariables</code> searches for variables
   * occurring more than once in a condition.
   * <p>
   * For each variable of type
   * "BQTerm" that already exists in <code>varPatternList</code> or in
   * <code>varList</code>, a type constraint is added to
   * <code>equationConstraints</code> to ensure that  both
   * variables have same type (this happens in case of non-linearity).
   * <p>
   * OBS.: we also need to check the <code>varPatternList</code> since a
   * BQVariable/BQVariableStar can have occurred in a previous condition as a
   * Variable/VariableStar, in the case of a composed condition or of a inner match
   * e.g. f(x) << e && (x < 10 ) -> { action } 
   * @param bqvar the variable to have the linearity checked
   */
  private void checkNonLinearityOfBQVariables(BQTerm bqvar) {
    {{if ( (((Object)bqvar) instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch395_41= false ; tom.engine.adt.tomname.types.TomName  tomMatch395_4= null ; tom.engine.adt.tomtype.types.TomType  tomMatch395_5= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch395_3= null ; tom.engine.adt.code.types.BQTerm  tomMatch395_7= null ; tom.engine.adt.code.types.BQTerm  tomMatch395_8= null ;if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqvar)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqvar))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch395_41= true ;tomMatch395_7=(( tom.engine.adt.code.types.BQTerm )((Object)bqvar));tomMatch395_3= tomMatch395_7.getOptions() ;tomMatch395_4= tomMatch395_7.getAstName() ;tomMatch395_5= tomMatch395_7.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqvar)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqvar))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch395_41= true ;tomMatch395_8=(( tom.engine.adt.code.types.BQTerm )((Object)bqvar));tomMatch395_3= tomMatch395_8.getOptions() ;tomMatch395_4= tomMatch395_8.getAstName() ;tomMatch395_5= tomMatch395_8.getAstType() ;}}}}}if (tomMatch395_41) { tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch395_3; tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch395_4; tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch395_5;if ( (((Object)varList) instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)varList))) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)varList))) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch395__end__12=(( tom.engine.adt.code.types.BQTermList )((Object)varList));do {{if (!( tomMatch395__end__12.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch395_23= tomMatch395__end__12.getHeadconcBQTerm() ;boolean tomMatch395_38= false ; tom.engine.adt.tomtype.types.TomType  tomMatch395_22= null ; tom.engine.adt.code.types.BQTerm  tomMatch395_25= null ; tom.engine.adt.code.types.BQTerm  tomMatch395_24= null ; tom.engine.adt.tomname.types.TomName  tomMatch395_21= null ;if ( (tomMatch395_23 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch395_23) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch395_38= true ;tomMatch395_24=tomMatch395_23;tomMatch395_21= tomMatch395_24.getAstName() ;tomMatch395_22= tomMatch395_24.getAstType() ;}} else {if ( (tomMatch395_23 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch395_23) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch395_38= true ;tomMatch395_25=tomMatch395_23;tomMatch395_21= tomMatch395_25.getAstName() ;tomMatch395_22= tomMatch395_25.getAstType() ;}}}}}if (tomMatch395_38) {if ( (tom_aName==tomMatch395_21) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch395_22;boolean tomMatch395_37= false ;if ( (tom_aType1==tomMatch395_22) ) {if ( (tom_aType2==tomMatch395_22) ) {tomMatch395_37= true ;}}if (!(tomMatch395_37)) {





          equationConstraints =
            addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,equationConstraints); }}}}if ( tomMatch395__end__12.isEmptyconcBQTerm() ) {tomMatch395__end__12=(( tom.engine.adt.code.types.BQTermList )((Object)varList));} else {tomMatch395__end__12= tomMatch395__end__12.getTailconcBQTerm() ;}}} while(!( (tomMatch395__end__12==(( tom.engine.adt.code.types.BQTermList )((Object)varList))) ));}}if ( (((Object)varPatternList) instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch395__end__18=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));do {{if (!( tomMatch395__end__18.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch395_28= tomMatch395__end__18.getHeadconcTomTerm() ;boolean tomMatch395_40= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch395_29= null ; tom.engine.adt.tomname.types.TomName  tomMatch395_26= null ; tom.engine.adt.tomtype.types.TomType  tomMatch395_27= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch395_30= null ;if ( (tomMatch395_28 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch395_28) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch395_40= true ;tomMatch395_29=tomMatch395_28;tomMatch395_26= tomMatch395_29.getAstName() ;tomMatch395_27= tomMatch395_29.getAstType() ;}} else {if ( (tomMatch395_28 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch395_28) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch395_40= true ;tomMatch395_30=tomMatch395_28;tomMatch395_26= tomMatch395_30.getAstName() ;tomMatch395_27= tomMatch395_30.getAstType() ;}}}}}if (tomMatch395_40) {if ( (tom_aName==tomMatch395_26) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch395_27;boolean tomMatch395_39= false ;if ( (tom_aType1==tomMatch395_27) ) {if ( (tom_aType2==tomMatch395_27) ) {tomMatch395_39= true ;}}if (!(tomMatch395_39)) {           equationConstraints =             addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,equationConstraints); }}}}if ( tomMatch395__end__18.isEmptyconcTomTerm() ) {tomMatch395__end__18=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));} else {tomMatch395__end__18= tomMatch395__end__18.getTailconcTomTerm() ;}}} while(!( (tomMatch395__end__18==(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) ));}}}}}}

  }

  /**
   * The method <code>inferCodeList</code> applies rule CT-BLOCK. It starts
   * inference process which takes one code at a time
   * <ul>
   *  <li> all lists and hashMaps are reset
   *  <li> each code is typed with fresh type variables
   *  <li> each code is traversed in order to generate type constraints
   *  <li> the type constraints of "equationConstraints" and
   *        "subtypeConstraints" lists are solved at the end
   *        of the current code generating a mapping (a set of
   *        substitutions for each type variable)
   *  <li> the mapping is applied over the code and the symbol table
   * </ul>
   * <p>
   * CT-BLOCK rule:
   * IF found "(rule_1,...,rule_n)" where "rule_i" are ConstraintInstructions
   * THEN infers types of each ConstraintInstruction
   * @param cList the tom code list to be type inferred
   * @return      the tom typed code list
   */
  private CodeList inferCodeList(CodeList cList) {
    CodeList newCList =  tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;
    for (Code code : cList.getCollectionconcCode()) {
      init();
      code =  collectKnownTypesFromCode(code);
      //DEBUG System.out.println("------------- Code typed with typeVar:\n code = " +
      //DEBUG    `code);
      code = inferAllTypes(code, tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
      //DEBUG printGeneratedConstraints(subtypeConstraints);
      //DEBUG printGeneratedConstraints(equationConstraints);
      solveConstraints();
      //DEBUG System.out.println("substitutions = " + substitutions);
      code = replaceInCode(code);
      //DEBUG System.out.println("------------- Code typed with substitutions:\n code = " +
      //DEBUG `code);
      replaceInSymbolTable();
      newCList =  tom.engine.adt.code.types.codelist.ConsconcCode.make(code,tom_append_list_concCode(newCList, tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
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
   * IF found "cond --> action)" where "action" is a list of terms or a code to
   *    be type inferred
   * THEN infers types of condition (by calling <code>inferConstraint</code>
   *      method) and action (by calling <code>inferAllTypes</code>) 
   * @param ciList  the list of pairs "condition -> action" to be type inferred 
   * @return        the typed list resulting
   */
  private ConstraintInstructionList inferConstraintInstructionList(ConstraintInstructionList ciList) {
    ConstraintInstructionList newCIList =  tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ;
    for (ConstraintInstruction cInst :
        ciList.getCollectionconcConstraintInstruction()) {
      try {
        {{if ( (((Object)cInst) instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst)) instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )(( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst))) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) { tom.engine.adt.tomconstraint.types.Constraint  tom_constraint= (( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst)).getConstraint() ;

            // Store variable lists in new variables and reinitialize them
            BQTermList globalVarList = varList;
            TomList globalVarPatternList = varPatternList;

            tom_make_TopDownCollect(tom_make_CollectVars(this)).visitLight(tom_constraint);

            Constraint newConstraint = inferConstraint(tom_constraint);
            //DEBUG System.out.println("inferConstraintInstructionList: action " +
            //DEBUG     `action);
            Instruction newAction = inferAllTypes( (( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst)).getAction() , tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );

            varPatternList = globalVarPatternList;
            resetVarList(globalVarPatternList);
            newCIList =
               tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(newConstraint, newAction,  (( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst)).getOptions() ) ,tom_append_list_concConstraintInstruction(newCIList, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;
          }}}}}

      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("inferConstraintInstructionList: failure on " + cInst);
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
  public static class CollectVars extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public CollectVars( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch397_4= false ; tom.engine.adt.code.types.BQTerm  tomMatch397_2= null ; tom.engine.adt.code.types.BQTerm  tomMatch397_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch397_4= true ;tomMatch397_2=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch397_4= true ;tomMatch397_3=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));}}}}}if (tomMatch397_4) {








 
        nkt.addBQTerm((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)));
      }}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch398_4= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch398_2= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch398_3= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch398_4= true ;tomMatch398_2=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg));}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch398_4= true ;tomMatch398_3=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg));}}}}}if (tomMatch398_4) {          nkt.addTomTerm((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)));       }}}}return _visit_TomTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectVars( NewKernelTyper  t0) { return new CollectVars(t0);}



  /**
   * The method <code>inferConstraint</code> applies rule CT-MATCH, CT-NUM, CT-CONJ,
   * or CT-DISJ to a "condition" in order to infer the types of its variables.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * CT-MATCH rule:
   * IF found "e1 << [A] e2" 
   * THEN infers type of e1 and e2 and add the type constraints "T1 = A" and "A
   * <: T2"
   * <p>
   * CT-NUM rule:
   * IF found "e1 == e2", "e1 <= e2", "e1 < e2", "e1 >= e2" or "e1 > e2"
   * THEN infers type of e1 and e2 and add the type constraints "A <:T1" and "A
   * <: T2", where "A" is a fresh type variable   
   * <p>
   * CT-CONJ rule (resp. CT-DISJ):
   * IF found "cond1 && cond2" (resp. "cond1 || cond2")
   * THEN infers type of cond1 and cond2 (by calling
   * <code>inferConstraint</code> for each condition)
   * @param constraint  the "condition" to be type inferred 
   * @return            the typed condition resulting
   */
  private Constraint inferConstraint(Constraint constraint) {
    {{if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_pattern= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getPattern() ; tom.engine.adt.code.types.BQTerm  tom_subject= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getSubject() ; tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getAstType() ;
 
        //DEBUG System.out.println("inferConstraint l1 -- subject = " + `subject);
        TomType tPattern = getType(tom_pattern);
        TomType tSubject = getType(tom_subject);
        if (tPattern == null || tPattern ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
          tPattern = getUnknownFreshTypeVar();
        }
        if (tSubject == null || tSubject ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
          tSubject = getUnknownFreshTypeVar();
        }
        //DEBUG System.out.println("inferConstraint: match -- constraint " +
        //DEBUG     tPattern + " = " + tSubject);
        {{if ( (((Object)tom_aType) instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch400_4= false ; tom.engine.adt.tomtype.types.TomType  tomMatch400_2= null ; tom.engine.adt.tomtype.types.TomType  tomMatch400_3= null ;if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch400_4= true ;tomMatch400_2=(( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType));}} else {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType))) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {{tomMatch400_4= true ;tomMatch400_3=(( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType));}}}}}if (tomMatch400_4) {

            /* T_pattern = T_cast and T_cast <: T_subject */
            equationConstraints =
              addEqConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tPattern, tom_aType, getInfoFromTomTerm(tom_pattern)) ,equationConstraints);
            subtypeConstraints = addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_aType, tSubject, getInfoFromBQTerm(tom_subject)) ,subtypeConstraints);
          }}}}

        TomTerm newPattern = inferAllTypes(tom_pattern,tPattern);
        BQTerm newSubject = inferAllTypes(tom_subject,tSubject);
        return  tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(newPattern, newSubject, tom_aType) ;
      }}}}{if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) { tom.engine.adt.code.types.BQTerm  tom_left= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getLeft() ; tom.engine.adt.code.types.BQTerm  tom_right= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getRight() ;


        TomType tLeft = getType(tom_left);
        TomType tRight = getType(tom_right);
        if (tLeft == null || tLeft ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
          tLeft = getUnknownFreshTypeVar();
        }
        if (tRight == null || tRight ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
          tRight = getUnknownFreshTypeVar();
        }
        //DEBUG System.out.println("inferConstraint: match -- constraint " +
        //DEBUG     tLeft + " = " + tRight);

        // To represent the relationshipo between both argument types
        TomType lowerType = getUnknownFreshTypeVar();
        subtypeConstraints =
          addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tLeft, getInfoFromBQTerm(tom_left)) ,subtypeConstraints);
        subtypeConstraints =
          addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tRight, getInfoFromBQTerm(tom_right)) ,subtypeConstraints);
        BQTerm newLeft = inferAllTypes(tom_left,tLeft);
        BQTerm newRight = inferAllTypes(tom_right,tRight);
        return  tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(newLeft, newRight,  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getType() ) ;
      }}}}{if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).isEmptyAndConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {


        ConstraintList cList =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getHeadAndConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
        Constraint newAConstraint =  tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;
        for (Constraint cArg : cList.getCollectionconcConstraint()) {
          cArg = inferConstraint(cArg);
          newAConstraint =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(newAConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ;
        }
        return newAConstraint;
      }}}}{if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).isEmptyOrConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) {


        ConstraintList cList =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getHeadOrConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
        Constraint newOConstraint =  tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ;
        for (Constraint cArg : cList.getCollectionconcConstraint()) {
          cArg = inferConstraint(cArg);
          newOConstraint =  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(newOConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) ) ;
        }
        return newOConstraint;
      }}}}}

    return constraint;
  }

  /**
   * The method <code>inferSlotList</code> infers types of the arguments of
   * lists and functions (which are TomTerms) 
   * <p> 
   * It continues the application of rules CT-FUN, CT-ELEM, CT-MERGE or CT-STAR
   * to each argument in order to infer the types of its variables.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * Continuation of CT-STAR rule (applying to premises):
   * IF found "l(e1,...,en,x*):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "x", where "x" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-ELEM rule (applying to premises which are
   * not lists):
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" does not represent a list with head symbol "l"
   * <p>
   * Continuation of CT-MERGE rule (applying to premises which are lists with
   * the same operator):
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-FUN rule (applying to premises):
   * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
   * THEN infers type of arguments and add a type constraint "T <:A"
   * <p>
   * @param sList       a list of arguments of a list/function
   * @param tSymbol     the TomSymbol related to the list/function
   * @param contextType the codomain of the list/function 
   * @return            the typed list of arguments resulting
   */
  private SlotList inferSlotList(SlotList sList, TomSymbol tSymbol, TomType
      contextType) {
    TomType argType = contextType;
    SlotList newSList =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
    {{if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

        TomName argName;
        TomTerm argTerm;
        TomSymbol argSymb;
        for (Slot slot : sList.getCollectionconcSlot()) {
          argName = slot.getSlotName();
          argTerm = slot.getAppl();
          argSymb = getSymbolFromTerm(argTerm);
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) {
            {{if ( (((Object)argTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch402_3= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch402_3= true ;}}if (!(tomMatch402_3)) {
 
                //DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
                argType = getUnknownFreshTypeVar();
                //DEBUG System.out.println("InferSlotList getUnknownFreshTypeVar = " +
                //DEBUG     `argType);
              }}}}

          }
          argTerm = inferAllTypes(argTerm,argType);
          newSList =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(argName, argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
        }
        return newSList.reverse(); 
      }}}}{if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch401_5= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getTypesToType() ;if ( (tomMatch401_5 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch401_5) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch401_8= tomMatch401_5.getDomain() ; tom.engine.adt.tomtype.types.TomType  tomMatch401_9= tomMatch401_5.getCodomain() ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch401_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch401_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch401_8.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch401_8.getHeadconcTomType() ;if ( (tomMatch401_9 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch401_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch401_12= tomMatch401_9.getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch401_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch401_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch401__end__21=tomMatch401_12;do {{if (!( tomMatch401__end__21.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch401_24= tomMatch401__end__21.getHeadconcTypeOption() ;if ( (tomMatch401_24 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch401_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {



        TomTerm argTerm;
        TomSymbol argSymb;
        for (Slot slot : sList.getCollectionconcSlot()) {
          argTerm = slot.getAppl();
          argSymb = getSymbolFromTerm(argTerm);
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) {
            {{if ( (((Object)argTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {

                /* Case CT-STAR rule (applying to premises) */
                argType = tomMatch401_9;
              }}}}{if ( (((Object)argTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch403_6= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch403_6= true ;}}if (!(tomMatch403_6)) {
 
                //DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
                /* Case CT-ELEM rule (applying to premises which are not lists) */
                argType = tom_headTTList;
              }}}}

          } else if ( (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getAstName() != argSymb.getAstName()) {
            /*
             * Case CT-ELEM rule where premise is a list
             * A list with a sublist whose constructor is different
             * e.g. 
             * A = ListA(A*) | a() and B = ListB(A*)
             * ListB(ListA(a()))
             */
            argType = tom_headTTList;
          } 

          /* Case CT-MERGE rule (applying to premises) */
          argTerm = inferAllTypes(argTerm,argType);
          newSList =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(slot.getSlotName(), argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
        }
        return newSList.reverse(); 
      }}}if ( tomMatch401__end__21.isEmptyconcTypeOption() ) {tomMatch401__end__21=tomMatch401_12;} else {tomMatch401__end__21= tomMatch401__end__21.getTailconcTypeOption() ;}}} while(!( (tomMatch401__end__21==tomMatch401_12) ));}}}}}}}}}}}{if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch401_27= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getTypesToType() ;if ( (tomMatch401_27 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch401_27) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch401_30= tomMatch401_27.getCodomain() ;if ( (tomMatch401_30 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch401_30) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch401_33= tomMatch401_30.getTypeOptions() ;boolean tomMatch401_44= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch401_33) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch401_33) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch401__end__39=tomMatch401_33;do {{if (!( tomMatch401__end__39.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch401_42= tomMatch401__end__39.getHeadconcTypeOption() ;if ( (tomMatch401_42 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch401_42) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch401_44= true ;}}}if ( tomMatch401__end__39.isEmptyconcTypeOption() ) {tomMatch401__end__39=tomMatch401_33;} else {tomMatch401__end__39= tomMatch401__end__39.getTailconcTypeOption() ;}}} while(!( (tomMatch401__end__39==tomMatch401_33) ));}if (!(tomMatch401_44)) {



        TomTerm argTerm;
        TomName argName;
        for (Slot slot : sList.getCollectionconcSlot()) {
          argName = slot.getSlotName();
          argType = TomBase.getSlotType(tSymbol,argName);
          argTerm = inferAllTypes(slot.getAppl(),argType);
          newSList =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(argName, argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
          //DEBUG System.out.println("InferSlotList CT-FUN -- end of for with slotappl = " + `argTerm);
        }
        return newSList.reverse(); 
      }}}}}}}}}}

    throw new TomRuntimeException("inferSlotList: failure on " + sList);
  }

  /**
   * The method <code>inferBQTermList</code> infers types of the arguments of
   * lists, functions and calls of methods (which are BQTerms) 
   * <p> 
   * It continues the application of rules CT-FUN, CT-ELEM, CT-MERGE or CT-STAR
   * to each argument in order to infer the types of its variables.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * Continuation of CT-STAR rule (applying to premises):
   * IF found "l(e1,...,en,x*):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "x", where "x" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-ELEM rule (applying to premises which are
   * not lists):
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" does not represent a list with head symbol "l"
   * <p>
   * Continuation of CT-MERGE rule (applying to premises which are lists with
   * the same operator):
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-FUN rule (applying to premises):
   * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
   * THEN infers type of arguments and add a type constraint "T <:A"
   * <p>
   * @param bqList      a list of arguments of a list/function/method
   * @param tSymbol     the TomSymbol related to the list/function
   * @param contextType the codomain of the list/function 
   * @return            the typed list of arguments resulting
   */
  private BQTermList inferBQTermList(BQTermList bqTList, TomSymbol tSymbol, TomType contextType) {

    if(tSymbol == null) {
      throw new TomRuntimeException("inferBQTermList: null symbol");
    }

    if(bqTList.isEmptyconcBQTerm()) {
      return  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
    }

    {{if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

        BQTermList newBQTList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
        for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
          argTerm = inferAllTypes(argTerm, tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
          newBQTList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
        }
        return newBQTList.reverse(); 
      }}}}{if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch404_5= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getTypesToType() ;if ( (tomMatch404_5 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch404_5) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch404_8= tomMatch404_5.getDomain() ; tom.engine.adt.tomtype.types.TomType  tomMatch404_9= tomMatch404_5.getCodomain() ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch404_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch404_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch404_8.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch404_8.getHeadconcTomType() ; tom.engine.adt.tomtype.types.TomTypeList  tom_tailTTList= tomMatch404_8.getTailconcTomType() ;if ( (tomMatch404_9 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch404_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch404_12= tomMatch404_9.getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch404_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch404_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch404__end__21=tomMatch404_12;do {{if (!( tomMatch404__end__21.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch404_24= tomMatch404__end__21.getHeadconcTypeOption() ;if ( (tomMatch404_24 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch404_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {




        if(!tom_tailTTList.isEmptyconcTomType()) {
          throw new TomRuntimeException("should be empty list: " + tom_tailTTList);
        }

        BQTermList newBQTList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
        for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
          TomSymbol argSymb = getSymbolFromTerm(argTerm);
          TomType argType = contextType;
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) {
            {{if ( (((Object)argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {

                /*
                 * We don't know what is into the Composite
                 * It can be a BQVariableStar or a list operator or a list of
                 * CompositeBQTerm or something else
                 */
                argType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
              }}}{if ( (((Object)argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)argTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {


                /* Case CT-STAR rule (applying to premises) */
                argType = tomMatch404_9;
              }}}}{if ( (((Object)argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch405_10= false ; tom.engine.adt.code.types.BQTerm  tomMatch405_9= null ; tom.engine.adt.code.types.BQTerm  tomMatch405_8= null ;if ( ((( tom.engine.adt.code.types.BQTerm )((Object)argTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch405_10= true ;tomMatch405_8=(( tom.engine.adt.code.types.BQTerm )((Object)argTerm));}} else {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)argTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch405_10= true ;tomMatch405_9=(( tom.engine.adt.code.types.BQTerm )((Object)argTerm));}}}}}if (tomMatch405_10) {




                argType = tom_headTTList;
              }}}}

          } else if ( (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getAstName() != argSymb.getAstName()) {
            /*
             * Case CT-ELEM rule which premise is a list
             * A list with a sublist whose constructor is different
             * e.g. 
             * A = ListA(A*) and B = ListB(A*) | b()
             * ListB(ListA(a()))
             */
            argType = tom_headTTList;
          }

          /* Case CT-MERGE rule (applying to premises) */
          argTerm = inferAllTypes(argTerm,argType);
          newBQTList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
        }
        return newBQTList.reverse(); 
      }}}if ( tomMatch404__end__21.isEmptyconcTypeOption() ) {tomMatch404__end__21=tomMatch404_12;} else {tomMatch404__end__21= tomMatch404__end__21.getTailconcTypeOption() ;}}} while(!( (tomMatch404__end__21==tomMatch404_12) ));}}}}}}}}}}}{if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch404_28= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getAstName() ;if ( (tomMatch404_28 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch404_28) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch404_34= tomMatch404_28.getCodomain() ;if ( (tomMatch404_34 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch404_34) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch404_37= tomMatch404_34.getTypeOptions() ; tom.engine.adt.tomslot.types.PairNameDeclList  tom_pNDList= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getPairNameDeclList() ;boolean tomMatch404_48= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch404_37) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch404_37) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch404__end__43=tomMatch404_37;do {{if (!( tomMatch404__end__43.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch404_46= tomMatch404__end__43.getHeadconcTypeOption() ;if ( (tomMatch404_46 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch404_46) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch404_48= true ;}}}if ( tomMatch404__end__43.isEmptyconcTypeOption() ) {tomMatch404__end__43=tomMatch404_37;} else {tomMatch404__end__43= tomMatch404__end__43.getTailconcTypeOption() ;}}} while(!( (tomMatch404__end__43==tomMatch404_37) ));}if (!(tomMatch404_48)) {



        if(tom_pNDList.length() != bqTList.length()) {
          Option option = TomBase.findOriginTracking( (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getOptions() );
          {{if ( (((Object)option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )((Object)option)) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)option))) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

              TomMessage.error(logger, (( tom.engine.adt.tomoption.types.Option )((Object)option)).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )((Object)option)).getLine() ,
                  TomMessage.symbolNumberArgument,tom_symName.getString(),tom_pNDList.length(),bqTList.length());
            }}}}{if ( (((Object)option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )((Object)option)) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)option))) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {

              TomMessage.error(logger,null,0,
                  TomMessage.symbolNumberArgument,tom_symName.getString(),tom_pNDList.length(),bqTList.length());
            }}}}}

        } else {
          TomTypeList symDomain =  tomMatch404_28.getDomain() ;
          BQTermList newBQTList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
          for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
            TomType argType = symDomain.getHeadconcTomType();
            {{if ( (((Object)argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) { tom.engine.adt.code.types.BQTerm  tomMatch407__end__4=(( tom.engine.adt.code.types.BQTerm )((Object)argTerm));do {{if (!( tomMatch407__end__4.isEmptyComposite() )) { tom.engine.adt.code.types.BQTerm  tomMatch407_5= tomMatch407__end__4.getTailComposite() ; tom.engine.adt.code.types.BQTerm  tomMatch407__end__8=tomMatch407_5;do {{if (!( tomMatch407__end__8.isEmptyComposite() )) {
















 argType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ; }if ( tomMatch407__end__8.isEmptyComposite() ) {tomMatch407__end__8=tomMatch407_5;} else {tomMatch407__end__8= tomMatch407__end__8.getTailComposite() ;}}} while(!( (tomMatch407__end__8==tomMatch407_5) ));}if ( tomMatch407__end__4.isEmptyComposite() ) {tomMatch407__end__4=(( tom.engine.adt.code.types.BQTerm )((Object)argTerm));} else {tomMatch407__end__4= tomMatch407__end__4.getTailComposite() ;}}} while(!( (tomMatch407__end__4==(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) ));}}}}

            argTerm = inferAllTypes(argTerm,argType);
            newBQTList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
            symDomain = symDomain.getTailconcTomType();
            //DEBUG System.out.println("InferBQTermList CT-FUN -- end of for with bqappl = " + `argTerm);
          }
          return newBQTList.reverse(); 
        }
      }}}}}}}}}}

    throw new TomRuntimeException("inferBQTermList: failure on " + bqTList);
  }

  /**
   * The method <code>solveConstraints</code> calls
   * <code>solveEquationConstraints</code> to solve the list of equation
   * constraints and generate a set os substitutions for type variables. Then
   * the substitutions are applied to the list of subtype constraints and the
   * method <code>solveEquationConstraints</code> to solve this list. 
   */
  private void solveConstraints() {
    //DEBUG System.out.println("\nsolveConstraints 1:");
    //DEBUG printGeneratedConstraints(equationConstraints);
    //DEBUG printGeneratedConstraints(subtypeConstraints);
    boolean errorFound = solveEquationConstraints(equationConstraints);
    if (!errorFound) {
      {{if ( (((Object)subtypeConstraints) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch408_2= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)subtypeConstraints))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)subtypeConstraints))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)subtypeConstraints)).isEmptyconcTypeConstraint() ) {tomMatch408_2= true ;}}if (!(tomMatch408_2)) {

          TypeConstraintList simplifiedConstraints =
            replaceInSubtypingConstraints(subtypeConstraints);
          //DEBUG printGeneratedConstraints(simplifiedConstraints);
            solveSubtypingConstraints(simplifiedConstraints);
        }}}}

    }
  }

  /**
   * The method <code>solveEquationConstraints</code> tries to solve all
   * equations constraints collected during the inference process.
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
   *  a) Map(A1) does not exist   --> {(A,T1)} U Map 
   *  b) Map(A1) = T2             --> detectFail(T1 = T2)
   *  c) Map(A1) = A2             --> {(A2,T1)} U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 6: tCList = {(T1^c = A1),...)} and Map 
   *  a) Map(A1) does not exist   --> {(A1,T1^c)} U Map 
   *  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
   *  c) Map(A1) = A2             --> {(A2,T1^c)} U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 7: tCList = {(A1 = T1),...)} and Map 
   *  a) Map(A1) does not exist   --> {(A1,T1)} U Map 
   *  b) Map(A1) = T2             --> detectFail(T1 = T2)
   *  c) Map(A1) = A2             --> {(A2,A1)} U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 8: tCList = {(A1 = T1^c),...)} and Map 
   *  a) Map(A1) does not exist   --> {(A1,T1^c)} U Map 
   *  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
   *  c) Map(A1) = A2             --> {(A2,T1^c)} U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 9: tCList = {(A1 = A2),...)} and Map
   *  a) Map(A1) = T1 (or T1^a) and
   *    i)    Map(A2) does not exist    --> {(A2,T1)} U Map (or {(A2,T1^a)} U Map) 
   *    ii)   Map(A2) = T2 (or T2^b)    --> detectFail(T1 = T2) (or detectFail(T1^a = T2^b))
   *    iii)  Map(A2) = A3              --> {(A3,T1)} U Map (or {(A3,T1^a)} U Map), since Map is saturated and
   *                                        then Map(A3) does not exist 
   *
   *  b) Map(A1) = A3 and
   *    i)    Map(A2) does not exist    --> {(A2,A3)} U Map 
   *    ii)   Map(A2) = T2 (or T2^b)    --> {(A3,T2)} U Map, since Map is saturated and
   *                                        then Map(A3) does not exist 
   *    iii)  Map(A2) = A4              --> {(A3,A4)} U Map, since Map is saturated and
   *                                        then Map(A3) does not exist 
   *  c) Map(A1) does not exist and
   *    i)    Map(A2) does not exist    --> {(A1,A2)} U Map
   *    ii)   Map(A2) = T1 (or T1^a)    --> {(A1,T1)} U Map (or {(A1,T1^a)} U Map)
   *    iii)  Map(A2) = A3              --> {(A1,A3)} U Map 
   * @param tCList the equation constraint list to be replaced
   * @return       the status of the list, if errors were found or not
   */
  private boolean solveEquationConstraints(TypeConstraintList tCList) {
    boolean errorFound = false;
    for (TypeConstraint tConstraint :
        tCList.getCollectionconcTypeConstraint()) {
matchBlockAdd :
      {
        {{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch409_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch409_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_groundType1=tomMatch409_1; tom.engine.adt.tomtype.types.TomType  tom_groundType2=tomMatch409_2;boolean tomMatch409_12= false ;if ( (tomMatch409_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch409_1) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType1==tomMatch409_1) ) {tomMatch409_12= true ;}}}if (!(tomMatch409_12)) {boolean tomMatch409_11= false ;if ( (tomMatch409_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch409_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType2==tomMatch409_2) ) {tomMatch409_11= true ;}}}if (!(tomMatch409_11)) {if (!( (tom_groundType2==tom_groundType1) )) {



              //DEBUG System.out.println("In solveEquationConstraints:" + `groundType1 +
              //DEBUG     " = " + `groundType2);
              errorFound = (errorFound || detectFail((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))));
              break matchBlockAdd;
            }}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch409_14= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch409_15= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch409_14;if ( (tomMatch409_15 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch409_15) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch409_15;boolean tomMatch409_24= false ;if ( (tomMatch409_14 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch409_14) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType==tomMatch409_14) ) {tomMatch409_24= true ;}}}if (!(tomMatch409_24)) {



              if (substitutions.containsKey(tom_typeVar)) {
                TomType mapTypeVar = substitutions.get(tom_typeVar);
                if (!isTypeVar(mapTypeVar)) {
                  errorFound = (errorFound || 
                      detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_groundType, mapTypeVar,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getInfo() ) ));
                } else {
                  // if (isTypeVar(mapTypeVar))
                  addSubstitution(mapTypeVar,tom_groundType);
                }
              } else {
                addSubstitution(tom_typeVar,tom_groundType);
              }
              break matchBlockAdd;
            }}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch409_26= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch409_27= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;if ( (tomMatch409_26 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch409_26) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch409_26; tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch409_27;boolean tomMatch409_36= false ;if ( (tomMatch409_27 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch409_27) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType==tomMatch409_27) ) {tomMatch409_36= true ;}}}if (!(tomMatch409_36)) {



            if (substitutions.containsKey(tom_typeVar)) {
              TomType mapTypeVar = substitutions.get(tom_typeVar);
              if (!isTypeVar(mapTypeVar)) {
                errorFound = (errorFound || 
                    detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar, tom_groundType,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getInfo() ) ));
              } else {
                // if (isTypeVar(mapTypeVar))
                addSubstitution(mapTypeVar,tom_groundType);
              }
            } else {
              addSubstitution(tom_typeVar,tom_groundType);
            }
            break matchBlockAdd;
          }}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch409_38= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch409_39= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;if ( (tomMatch409_38 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch409_38) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar1=tomMatch409_38;if ( (tomMatch409_39 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch409_39) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar2=tomMatch409_39;if (!( (tom_typeVar2==tom_typeVar1) )) {




              TomType mapTypeVar1;
              TomType mapTypeVar2;
              if (substitutions.containsKey(tom_typeVar1) && substitutions.containsKey(tom_typeVar2)) {
                mapTypeVar1 = substitutions.get(tom_typeVar1);
                mapTypeVar2 = substitutions.get(tom_typeVar2);
                if (isTypeVar(mapTypeVar1)) {
                  addSubstitution(mapTypeVar1,mapTypeVar2);
                } else {
                  if (isTypeVar(mapTypeVar2)) {
                    addSubstitution(mapTypeVar2,mapTypeVar1);
                  } else {
                    errorFound = (errorFound || 
                        detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar1, mapTypeVar2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getInfo() ) ));
                  }
                }
                break matchBlockAdd;
              } else if (substitutions.containsKey(tom_typeVar1)) {
                mapTypeVar1 = substitutions.get(tom_typeVar1);
                addSubstitution(tom_typeVar2,mapTypeVar1);
                break matchBlockAdd;
              } else if (substitutions.containsKey(tom_typeVar2)){
                mapTypeVar2 = substitutions.get(tom_typeVar2);
                addSubstitution(tom_typeVar1,mapTypeVar2);
                break matchBlockAdd;
              } else {
                addSubstitution(tom_typeVar1,tom_typeVar2);
                break matchBlockAdd;
              }
            }}}}}}}}}}

      }
    }
    return errorFound;
  }

  /**
   * The method <code>detectFail</code> checks if a type constraint
   * relating two ground types has a solution. In the negative case, an
   * 'incompatible types' message error is printed.  
   * <p> 
   * There exists 2 kinds of ground types: ground types Ti and
   * ground types Ti^c which are decorated with a given symbol c. Considering
   * type constraints as pairs (type1,type2) representing an equation or
   * a subtype relation between two ground types, them the set of all possibilities of arrangement between
   * ground types is a sequence with repetition. Then, we have 4 possible cases (since
   * 2^2 = 4).
   * <p>
   * CASE 1: tCList = {(T1 = T2),...)} and Map
   *  a) --> true if T1 is different from T2
   *  b) --> false if T1 is equals to T2
   * <p>
   * CASE 2: tCList = {(T1 = T2^c),...)} and Map
   *  a) --> true if T1 is different from T2
   *  b) --> false if T1 is equals to T2
   * <p>
   * CASE 3: tCList = {(T1^c = T2),...)} and Map
   *  a) --> true if T1 is different from T2
   *  b) --> false if T1 is equals to T2
   * <p>
   * CASE 4: tCList = {(T1^a = T2^b),...)} and Map
   *  a) --> true if T1 is different from T2 or 'a' is different from 'b'
   *  b) --> false if T1 is equals to T2 and 'a' is equals to 'b'
   * <p>
   * CASE 5: tCList = {(T1 <: T2),...)} and Map
   *  a) --> true if T1 is not subtype of T2
   *  b) --> false if T1 is subtype of T2
   * <p>
   * CASE 6: tCList = {(T1 <: T2^c),...)} and Map
   *   --> true
   * <p>
   * CASE 7: tCList = {(T1^c <: T2),...)} and Map
   *  a) --> true if T1 is not subtype of T2
   *  b) --> false if T1 is subtype of T2
   * <p>
   * CASE 8: tCList = {(T1^a <: T2^b),...)} and Map
   *  a) --> true if T1 is not subtype of T2 or 'a' is different from 'b'
   *  b) --> false if T1 is subtype of T2 and 'a' is equals to 'b'
   * <p>
   * @param tConstraint the type constraint to be verified 
   * @return            the status of the list, if errors were found or not
   */
  private boolean detectFail(TypeConstraint tConstraint) {
    if (!lazyType) {
      {{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch410_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch410_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;if ( (tomMatch410_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch410_1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_tName1= tomMatch410_1.getTomType() ;if ( (tomMatch410_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch410_2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tomMatch410_8= tomMatch410_2.getTomType() ; String  tom_tName2=tomMatch410_8;boolean tomMatch410_13= false ;if ( tom_tName1.equals(tomMatch410_8) ) {if ( tom_tName2.equals(tomMatch410_8) ) {tomMatch410_13= true ;}}if (!(tomMatch410_13)) {if (!( "unknown type".equals(tom_tName1) )) {if (!( "unknown type".equals(tom_tName2) )) {



            printErrorIncompatibility(tConstraint);
            return true;
          }}}}}}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch410_17= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch410_18= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;if ( (tomMatch410_17 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch410_17) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions1= tomMatch410_17.getTypeOptions() ;if ( (tomMatch410_18 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch410_18) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch410_25= tomMatch410_18.getTypeOptions() ; tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions2=tomMatch410_25;if (  tomMatch410_17.getTomType() .equals( tomMatch410_18.getTomType() ) ) {if ( (((Object)tom_tOptions1) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch410__end__32=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));do {{if (!( tomMatch410__end__32.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch410_43= tomMatch410__end__32.getHeadconcTypeOption() ;if ( (tomMatch410_43 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch410_43) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch410__end__38=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));do {{if (!( tomMatch410__end__38.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch410_46= tomMatch410__end__38.getHeadconcTypeOption() ;if ( (tomMatch410_46 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch410_46) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) { tom.engine.adt.tomname.types.TomName  tomMatch410_45= tomMatch410_46.getRootSymbolName() ;boolean tomMatch410_53= false ;if ( ( tomMatch410_43.getRootSymbolName() ==tomMatch410_45) ) {if ( (tomMatch410_45==tomMatch410_45) ) {tomMatch410_53= true ;}}if (!(tomMatch410_53)) {boolean tomMatch410_52= false ;if ( (tom_tOptions1==tomMatch410_25) ) {if ( (tom_tOptions2==tomMatch410_25) ) {tomMatch410_52= true ;}}if (!(tomMatch410_52)) {






            printErrorIncompatibility(tConstraint);
            return true;
          }}}}}if ( tomMatch410__end__38.isEmptyconcTypeOption() ) {tomMatch410__end__38=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));} else {tomMatch410__end__38= tomMatch410__end__38.getTailconcTypeOption() ;}}} while(!( (tomMatch410__end__38==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));}}}}}if ( tomMatch410__end__32.isEmptyconcTypeOption() ) {tomMatch410__end__32=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));} else {tomMatch410__end__32= tomMatch410__end__32.getTailconcTypeOption() ;}}} while(!( (tomMatch410__end__32==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) ));}}}}}}}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch410_56= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch410_56;boolean tomMatch410_61= false ;if ( (tom_t1==tomMatch410_56) ) {if ( (tom_t2==tomMatch410_56) ) {tomMatch410_61= true ;}}if (!(tomMatch410_61)) {


          if (!isSubtypeOf(tom_t1,tom_t2)) {
            printErrorIncompatibility(tConstraint);
            return true;
          }
        }}}}}}

    } 
    return false;
  }

  /**
   * The method <code>replaceInSubtypingConstraints</code> applies the
   * substitutions to a given type constraint list.
   * @param tCList the type constraint list to be replaced
   * @return       the type constraint list resulting of replacement
   */
  private TypeConstraintList replaceInSubtypingConstraints(TypeConstraintList
      tCList) {
    TypeConstraintList replacedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    TomType mapT1;
    TomType mapT2;
    for (TypeConstraint tConstraint: tCList.getCollectionconcTypeConstraint()) {
      {{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tom_t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;

          mapT1 = substitutions.get(tom_t1);
          mapT2 = substitutions.get(tom_t2); 
          if (mapT1 == null) {
            mapT1 = tom_t1;
          }
          if (mapT2 == null) {
            mapT2 = tom_t2;
          }
          if (mapT1 != mapT2) {
            replacedtCList = addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(mapT1, mapT2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getInfo() ) ,replacedtCList);
          }
        }}}}}


    }
    return replacedtCList;
  }


  /**
   * The method <code>solveSubtypingConstraints</code> do constraint propagation
   * by calling simplification subtyping constraints algorithms to solve the list of
   * subtyping constraints. Then if no errors were found, another algorithm is
   * called in order to generate solutions for the list. This combination of
   * algorithms is done until the list is empty.
   * Otherwise, the simplification is stopped and all error messages are printed  
   * <p>
   * Propagation of constraints:
   *  - PHASE 1: Simplification in equations
   *  - PHASE 2: Reduction in closed form
   *  - PHASE 3: Verification of type inconsistencies 
   *  - PHASE 4: Canonization
   * Generation of solutions
   * Garbage collection: remove typeVars which are not input types 
   * @param tCList  the subtyping constraint list to be replaced
   * @return        the empty solved list or the list that has no solutions
   */
  private void solveSubtypingConstraints(TypeConstraintList tCList) {
    TypeConstraintList solvedConstraints = tCList;
    TypeConstraintList simplifiedConstraints =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
tryBlock:
    {
      try {
        solvedConstraints = 
          tom_make_RepeatId(tom_make_simplificationAndClosure(this)).visitLight(solvedConstraints);
        //calculatePolarities(simplifiedConstraints);
        //solvedConstraints = simplifiedConstraints;
        //while (!solvedConstraints.isEmptyconcTypeConstraint()){
        while (solvedConstraints != simplifiedConstraints){
          simplifiedConstraints = incompatibilityDetection(solvedConstraints);
          {{if ( (((Object)simplifiedConstraints) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch412__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints));do {{if (!( tomMatch412__end__4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch412_7= tomMatch412__end__4.getHeadconcTypeConstraint() ;if ( (tomMatch412_7 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch412_7) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint) ) {

              //DEBUG System.out.println("Error!!");
              break tryBlock;
            }}}if ( tomMatch412__end__4.isEmptyconcTypeConstraint() ) {tomMatch412__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints));} else {tomMatch412__end__4= tomMatch412__end__4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch412__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints))) ));}}}}

          simplifiedConstraints = tom_make_RepeatId(tom_make_applyCanonization(this)).visitLight(simplifiedConstraints);
          //System.out.println("\n\n###### Before ######");
          //printGeneratedConstraints(simplifiedConstraints);
          //simplifiedConstraints = garbageCollect(simplifiedConstraints);
          //System.out.println("\n\n###### After ######");
          //printGeneratedConstraints(simplifiedConstraints);
          solvedConstraints = generateSolutions(simplifiedConstraints);
        }
        garbageCollection(solvedConstraints);
        //DEBUG System.out.println("\nResulting subtype constraints!!");
        //DEBUG printGeneratedConstraints(solvedConstraints);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("solveSubtypingConstraints: failure on " +
            solvedConstraints);
      }
    }
  }

  /**
   * The method <code>simplificationAndClosure</code> is generated by a
   * strategy which simplifies a subtype constraints list.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * PHASE 1: Simplification in equations (apply anti-symetry of the partial
   * order "<:"): 
   * tCList = {T1 <: T2, T2 <: T1} U tCList' and Map 
   *  --> solveEquationConstraints(T1 = T2) U tCList' and Map
   * tCList = {A1 <: A2, A2 <: A1} U tCList' and Map 
   *  --> solveEquationConstraints(A1 = A2) U tCList' and Map
   * <p>
   * PHASE 2: Reduction in closed form (apply transitivity of the partial
   * order "<:"):
   * tCList = {T1 <: A,A <: T2} U tCList' and Map --> {T1 <: T2} U tCList and Map
   * tCList = {A1 <:A,A <: A2} U tCList' and Map --> {A1 <: A2} U tCList and Map
   * <p>
   * @param nkt an instance of object NewKernelTyper
   * @return    the subtype constraint list resulting
   */
  public static class simplificationAndClosure extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public simplificationAndClosure( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));do {{if (!( tomMatch413__end__4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch413_14= tomMatch413__end__4.getHeadconcTypeConstraint() ;if ( (tomMatch413_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch413_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch413_14.getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch413_14.getType2() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413_5= tomMatch413__end__4.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413__end__8=tomMatch413_5;do {{if (!( tomMatch413__end__8.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch413_18= tomMatch413__end__8.getHeadconcTypeConstraint() ;if ( (tomMatch413_18 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch413_18) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tom_t2== tomMatch413_18.getType1() ) ) {if ( (tom_t1== tomMatch413_18.getType2() ) ) {



        nkt.solveEquationConstraints( tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_t1, tom_t2,  tomMatch413_14.getInfo() ) , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) );
        return
          nkt.tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg)),tomMatch413__end__4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint(tomMatch413_5,tomMatch413__end__8, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch413__end__8.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
      }}}}}if ( tomMatch413__end__8.isEmptyconcTypeConstraint() ) {tomMatch413__end__8=tomMatch413_5;} else {tomMatch413__end__8= tomMatch413__end__8.getTailconcTypeConstraint() ;}}} while(!( (tomMatch413__end__8==tomMatch413_5) ));}}}if ( tomMatch413__end__4.isEmptyconcTypeConstraint() ) {tomMatch413__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));} else {tomMatch413__end__4= tomMatch413__end__4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch413__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));}}}{if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg)); tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413__end__27=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));do {{if (!( tomMatch413__end__27.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch413_36= tomMatch413__end__27.getHeadconcTypeConstraint() ;if ( (tomMatch413_36 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch413_36) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch413_35= tomMatch413_36.getType2() ; tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch413_36.getType1() ;if ( (tomMatch413_35 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch413_35) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413_28= tomMatch413__end__27.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413__end__31=tomMatch413_28;do {{if (!( tomMatch413__end__31.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch413_43= tomMatch413__end__31.getHeadconcTypeConstraint() ;if ( (tomMatch413_43 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch413_43) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tomMatch413_35== tomMatch413_43.getType1() ) ) { tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch413_43.getType2() ;if ( (((Object)tom_tcl) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch413_58= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413__end__48=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl));do {{if (!( tomMatch413__end__48.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch413_54= tomMatch413__end__48.getHeadconcTypeConstraint() ;if ( (tomMatch413_54 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch413_54) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tom_t1== tomMatch413_54.getType1() ) ) {if ( (tom_t2== tomMatch413_54.getType2() ) ) {tomMatch413_58= true ;}}}}}if ( tomMatch413__end__48.isEmptyconcTypeConstraint() ) {tomMatch413__end__48=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl));} else {tomMatch413__end__48= tomMatch413__end__48.getTailconcTypeConstraint() ;}}} while(!( (tomMatch413__end__48==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) ));}if (!(tomMatch413_58)) {








          //DEBUG System.out.println("\nsolve2a: tcl =" + `tcl);
          return
            nkt.addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2,  tomMatch413_43.getInfo() ) ,tom_tcl);
        }}}}}}if ( tomMatch413__end__31.isEmptyconcTypeConstraint() ) {tomMatch413__end__31=tomMatch413_28;} else {tomMatch413__end__31= tomMatch413__end__31.getTailconcTypeConstraint() ;}}} while(!( (tomMatch413__end__31==tomMatch413_28) ));}}}}}if ( tomMatch413__end__27.isEmptyconcTypeConstraint() ) {tomMatch413__end__27=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));} else {tomMatch413__end__27= tomMatch413__end__27.getTailconcTypeConstraint() ;}}} while(!( (tomMatch413__end__27==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));}}}{if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg)); tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413__end__64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));do {{if (!( tomMatch413__end__64.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch413_74= tomMatch413__end__64.getHeadconcTypeConstraint() ;if ( (tomMatch413_74 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch413_74) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch413_71= tomMatch413_74.getType1() ;if ( (tomMatch413_71 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch413_71) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch413_74.getType2() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413_65= tomMatch413__end__64.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413__end__68=tomMatch413_65;do {{if (!( tomMatch413__end__68.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch413_80= tomMatch413__end__68.getHeadconcTypeConstraint() ;if ( (tomMatch413_80 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch413_80) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch413_80.getType1() ;if ( (tomMatch413_71== tomMatch413_80.getType2() ) ) {if ( (((Object)tom_tcl) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch413_95= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch413__end__85=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl));do {{if (!( tomMatch413__end__85.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch413_91= tomMatch413__end__85.getHeadconcTypeConstraint() ;if ( (tomMatch413_91 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch413_91) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tom_t1== tomMatch413_91.getType1() ) ) {if ( (tom_t2== tomMatch413_91.getType2() ) ) {tomMatch413_95= true ;}}}}}if ( tomMatch413__end__85.isEmptyconcTypeConstraint() ) {tomMatch413__end__85=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl));} else {tomMatch413__end__85= tomMatch413__end__85.getTailconcTypeConstraint() ;}}} while(!( (tomMatch413__end__85==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) ));}if (!(tomMatch413_95)) {


          //DEBUG System.out.println("\nsolve2b: tcl = " + `tcl);
          return
            nkt.addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2,  tomMatch413_74.getInfo() ) ,tom_tcl);
        }}}}}}if ( tomMatch413__end__68.isEmptyconcTypeConstraint() ) {tomMatch413__end__68=tomMatch413_65;} else {tomMatch413__end__68= tomMatch413__end__68.getTailconcTypeConstraint() ;}}} while(!( (tomMatch413__end__68==tomMatch413_65) ));}}}}}if ( tomMatch413__end__64.isEmptyconcTypeConstraint() ) {tomMatch413__end__64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));} else {tomMatch413__end__64= tomMatch413__end__64.getTailconcTypeConstraint() ;}}} while(!( (tomMatch413__end__64==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));}}}}return _visit_TypeConstraintList(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_simplificationAndClosure( NewKernelTyper  t0) { return new simplificationAndClosure(t0);}



  /**
   * The method <code>incompatibilityDetection</code> verify if there are type
   * inconsistencies. This is done for each type constraint of tCList.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * tCList = {T1 <: T2} U tCList' and Map --> detectFail(T1 <: T2) U tCList and Map
   * Note that the constraint "{T1 <: T2}" must be keeped in tCList to avoid an
   * infinity loop when re-applying <code>simplificationAndClosure</code> method.
   * <p>
   * @param nkt an instance of object NewKernelTyper
   * @return    the subtype constraint list resulting
   */
  private TypeConstraintList incompatibilityDetection(TypeConstraintList tCList) {
    /* PHASE 3 */
    boolean errorFound = false;
    TypeConstraintList simplifiedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    for (TypeConstraint tConstraint :
        tCList.getCollectionconcTypeConstraint()) {
matchBlock :
      {
        {{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch414_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch414_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;boolean tomMatch414_10= false ;if ( (tomMatch414_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch414_1) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch414_10= true ;}}if (!(tomMatch414_10)) {boolean tomMatch414_9= false ;if ( (tomMatch414_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch414_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch414_9= true ;}}if (!(tomMatch414_9)) {

            //DEBUG System.out.println("\nsolve3: tConstraint=" + `tConstraint);
            errorFound = (errorFound || detectFail(tConstraint));
            break matchBlock;
          }}}}}}{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {

            simplifiedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(simplifiedtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
          }}}

      }
    }
    if (errorFound) { 
      simplifiedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(simplifiedtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
    }
    return simplifiedtCList;
  }

  /*
  private TypeConstraintList garbageCollect(TypeConstraintList tCList) {
    TypeConstraintList simplifiedtCList = `concTypeConstraint();
    for (TypeConstraint tConstraint :
        tCList.getCollectionconcTypeConstraint()) {
      %match {
        Subtype[Type1=t1,Type2=t2] << tConstraint &&
          (concTomType(_*,t1,_*) << inputTVarList || 
           concTomType(_*,t2,_*) << inputTVarList) -> {
            simplifiedtCList = `concTypeConstraint(tConstraint,simplifiedtCList*);
          }
      }
    }
    return simplifiedtCList;
  }
  */

  /**
   * The method <code>applyCanonization</code> is generated by a
   * strategy which simplifies a subtype constraints list by looking for exactly
   * one non-variable lower and one non-variable upper bound for each type
   * variable, i.e. put tCList in a canonical form.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * - Upper bound:
   * tCList = {A <: T1,A <:T2} U tCList' and Map 
   *   --> {A <: T1} U tCList' and Map
   *       if T1 <: T2 
   *   --> {A <: T2} U tCList' and Map
   *       if T2 <: T1
   *   --> {False} U tCList' and Map
   *       if T1 and T2 are not comparable
   * - Lower bound:
   * tCList = {T1 <: A,T2 <: A} U tCList' and Map
   *   --> {T <: A} U tCList' and Map
   *       if there exists T such that T is the lowest upper bound of T1 and T2
   *   --> {False} U tCList' and Map
   *       if there does not exist T such that T is the lowest upper bound of T1 and T2
   * <p>
   * @param nkt an instance of object NewKernelTyper
   * @return    the subtype constraint list resulting
   */
  public static class applyCanonization extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public applyCanonization( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch415__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));do {{ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl1=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg)),tomMatch415__end__4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch415__end__4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch415_14= tomMatch415__end__4.getHeadconcTypeConstraint() ;if ( (tomMatch415_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch415_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch415_11= tomMatch415_14.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch415_12= tomMatch415_14.getType2() ;if ( (tomMatch415_11 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch415_11) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch415_11; tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch415_12; tom.engine.adt.typeconstraints.types.Info  tom_info= tomMatch415_14.getInfo() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch415_5= tomMatch415__end__4.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch415__end__8=tomMatch415_5;do {{ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl2=tom_get_slice_concTypeConstraint(tomMatch415_5,tomMatch415__end__8, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch415__end__8.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch415_20= tomMatch415__end__8.getHeadconcTypeConstraint() ;if ( (tomMatch415_20 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch415_20) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch415_19= tomMatch415_20.getType2() ;if ( (tom_tVar== tomMatch415_20.getType1() ) ) { tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch415_19; tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl3= tomMatch415__end__8.getTailconcTypeConstraint() ;boolean tomMatch415_30= false ;if ( (tomMatch415_12 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch415_12) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t1==tomMatch415_12) ) {tomMatch415_30= true ;}}}if (!(tomMatch415_30)) {boolean tomMatch415_29= false ;if ( (tomMatch415_19 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch415_19) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t2==tomMatch415_19) ) {tomMatch415_29= true ;}}}if (!(tomMatch415_29)) {



        TomType lowerType = nkt.minType(tom_t1,tom_t2);

        if (lowerType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
          nkt.printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2, tom_info) );
          return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) ; 
        }

        return
          nkt.addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_tVar, lowerType, tom_info) ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
      }}}}}}if ( tomMatch415__end__8.isEmptyconcTypeConstraint() ) {tomMatch415__end__8=tomMatch415_5;} else {tomMatch415__end__8= tomMatch415__end__8.getTailconcTypeConstraint() ;}}} while(!( (tomMatch415__end__8==tomMatch415_5) ));}}}}}if ( tomMatch415__end__4.isEmptyconcTypeConstraint() ) {tomMatch415__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));} else {tomMatch415__end__4= tomMatch415__end__4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch415__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));}}}{if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch415__end__35=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));do {{ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl1=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg)),tomMatch415__end__35, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch415__end__35.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch415_45= tomMatch415__end__35.getHeadconcTypeConstraint() ;if ( (tomMatch415_45 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch415_45) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch415_42= tomMatch415_45.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch415_43= tomMatch415_45.getType2() ; tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch415_42;if ( (tomMatch415_43 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch415_43) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch415_43; tom.engine.adt.typeconstraints.types.Info  tom_info= tomMatch415_45.getInfo() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch415_36= tomMatch415__end__35.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch415__end__39=tomMatch415_36;do {{ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl2=tom_get_slice_concTypeConstraint(tomMatch415_36,tomMatch415__end__39, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch415__end__39.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch415_51= tomMatch415__end__39.getHeadconcTypeConstraint() ;if ( (tomMatch415_51 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch415_51) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch415_49= tomMatch415_51.getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch415_49;if ( (tom_tVar== tomMatch415_51.getType2() ) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl3= tomMatch415__end__39.getTailconcTypeConstraint() ;boolean tomMatch415_61= false ;if ( (tomMatch415_42 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch415_42) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t1==tomMatch415_42) ) {tomMatch415_61= true ;}}}if (!(tomMatch415_61)) {boolean tomMatch415_60= false ;if ( (tomMatch415_49 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch415_49) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t2==tomMatch415_49) ) {tomMatch415_60= true ;}}}if (!(tomMatch415_60)) {


        TomType supType = nkt.supType(tom_t1,tom_t2);

        if (supType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
          nkt.printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2, tom_info) );
          return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) ; 
        }

        return
          nkt.addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(supType, tom_tVar, tom_info) ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
      }}}}}}if ( tomMatch415__end__39.isEmptyconcTypeConstraint() ) {tomMatch415__end__39=tomMatch415_36;} else {tomMatch415__end__39= tomMatch415__end__39.getTailconcTypeConstraint() ;}}} while(!( (tomMatch415__end__39==tomMatch415_36) ));}}}}}if ( tomMatch415__end__35.isEmptyconcTypeConstraint() ) {tomMatch415__end__35=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));} else {tomMatch415__end__35= tomMatch415__end__35.getTailconcTypeConstraint() ;}}} while(!( (tomMatch415__end__35==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));}}}}return _visit_TypeConstraintList(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_applyCanonization( NewKernelTyper  t0) { return new applyCanonization(t0);}



  /**
   * The method <code>findVar</code> checks if a given type variable occurs in
   * a given type constraint list.
   * @param tVar   the type variable to be found
   * @param tCList the type constraint list to be checked
   * @return        'true' if the type variable occurs in the list
   *                'false' otherwise
   */
  private boolean findVar(TomType tVar, TypeConstraintList tCList) {
    {{if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch416__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch416__end__5.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch416_9= tomMatch416__end__5.getHeadconcTypeConstraint() ;boolean tomMatch416_13= false ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch416_11= null ; tom.engine.adt.tomtype.types.TomType  tomMatch416_8= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch416_10= null ;if ( (tomMatch416_9 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch416_9) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch416_13= true ;tomMatch416_10=tomMatch416_9;tomMatch416_8= tomMatch416_10.getType1() ;}} else {if ( (tomMatch416_9 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch416_9) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch416_13= true ;tomMatch416_11=tomMatch416_9;tomMatch416_8= tomMatch416_11.getType1() ;}}}}}if (tomMatch416_13) {if ( (((Object)tVar) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( (tomMatch416_8==(( tom.engine.adt.tomtype.types.TomType )((Object)tVar))) ) {

 return true; }}}}if ( tomMatch416__end__5.isEmptyconcTypeConstraint() ) {tomMatch416__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch416__end__5= tomMatch416__end__5.getTailconcTypeConstraint() ;}}} while(!( (tomMatch416__end__5==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}{if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch416__end__19=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch416__end__19.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch416_23= tomMatch416__end__19.getHeadconcTypeConstraint() ;boolean tomMatch416_27= false ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch416_24= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch416_25= null ; tom.engine.adt.tomtype.types.TomType  tomMatch416_22= null ;if ( (tomMatch416_23 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch416_23) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch416_27= true ;tomMatch416_24=tomMatch416_23;tomMatch416_22= tomMatch416_24.getType2() ;}} else {if ( (tomMatch416_23 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch416_23) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch416_27= true ;tomMatch416_25=tomMatch416_23;tomMatch416_22= tomMatch416_25.getType2() ;}}}}}if (tomMatch416_27) {if ( (((Object)tVar) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( (tomMatch416_22==(( tom.engine.adt.tomtype.types.TomType )((Object)tVar))) ) {


 return true; }}}}if ( tomMatch416__end__19.isEmptyconcTypeConstraint() ) {tomMatch416__end__19=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch416__end__19= tomMatch416__end__19.getTailconcTypeConstraint() ;}}} while(!( (tomMatch416__end__19==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}

    return false;
  }

  /**
   * The method <code>minType</code> tries to find the common lowertype
   * between two given ground types.
   * @param t1 a type
   * @param t2 another type
   * @return    the lowertype between 't1' and 't2' if the subtype relation
   *            holds between them (since a multiple inheritance is forbidden)
   *            the 'EmptyType()' otherwise
   */
  private TomType minType(TomType t1, TomType t2) {
    //DEBUG System.out.println("minType: t1 = " + t1 + " and t2 = " + t2);
    //DEBUG System.out.println("minType: isSubtypeOf(t1,t2) = " + isSubtypeOf(t1,t2));
    if (isSubtypeOf(t1,t2)) { return t1; } 
    if (isSubtypeOf(t2,t1)) { return t2; }
    //DEBUG System.out.println("case 3");
    return  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
  }

  /**
   * The method <code>isSubtypeOf</code> checks if type t1 is a subtype of type
   * t2 considering symmetry and type decorations of both t1 and t2.
   * <p> 
   * There exists 2 kinds of ground types: ground types Ti (represented by Ti^?) and
   * ground types Ti^c which are decorated with a given symbol c. Considering a
   * partial order over ground types "<:" as a binary relation where "T1^c1 <: T2^c2"
   * is equivalent to "T1 is subtype of T2 and (c1 == c2 or c2 == ?)", then the set of all possibilities of arrangement between
   * ground types is a sequence with repetition. Then, we have 4 possible cases (since
   * 2^2 = 4).
   * <p>
   * CASE 1: T1^? <: T2^?
   *  a) --> true if T1 == T2 or T1 is a proper subtype of T2
   *  b) --> false otherwise
   * <p>
   * CASE 2: T1^? <: T2^c
   *   --> false
   * <p>
   * CASE 3: T1^c <: T2^?
   *  a) --> true if T1 == T2 or T1 is a proper subtype of T2
   *  b) --> false otherwise
   * <p>
   * CASE 4: T1^c1 <: T2^c2
   *  a) --> true if 'a' is equals to 'b' and (T1 == T2 or T1 is a proper
   *  subtype of T2)
   *  b) --> false otherwise
   * <p>
   * @param t1 a ground (decorated) type
   * @param t2 another ground (decorated) type
   * @return   true if t1 <: t2 and false otherwise
   */
  private boolean isSubtypeOf(TomType t1, TomType t2) {
    //DEBUG System.out.println("isSubtypeOf: t1 = " + t1 + " and t2 = " + t2);
    {{if ( (((Object)t1) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)t1)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)t1))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions1= (( tom.engine.adt.tomtype.types.TomType )((Object)t1)).getTypeOptions() ; String  tom_tName1= (( tom.engine.adt.tomtype.types.TomType )((Object)t1)).getTomType() ;if ( (((Object)t2) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)t2)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)t2))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions2= (( tom.engine.adt.tomtype.types.TomType )((Object)t2)).getTypeOptions() ; String  tom_tName2= (( tom.engine.adt.tomtype.types.TomType )((Object)t2)).getTomType() ;


          TomTypeList supTypet1 = dependencies.get(tom_tName1);
          {{if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {boolean tomMatch418_9= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch418__end__4=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));do {{if (!( tomMatch418__end__4.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch418_7= tomMatch418__end__4.getHeadconcTypeOption() ;if ( (tomMatch418_7 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch418_7) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch418_9= true ;}}}if ( tomMatch418__end__4.isEmptyconcTypeOption() ) {tomMatch418__end__4=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));} else {tomMatch418__end__4= tomMatch418__end__4.getTailconcTypeOption() ;}}} while(!( (tomMatch418__end__4==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));}if (!(tomMatch418_9)) {if ( tom_tName2.equals(tom_tName1) ) {



                //DEBUG System.out.println("isSubtypeOf: cases 1a and 3a - i");
                return true; 
              }}}}{if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((Object)supTypet1) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch418__end__15=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1));do {{if (!( tomMatch418__end__15.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch418_19= tomMatch418__end__15.getHeadconcTomType() ;if ( (tomMatch418_19 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch418_19) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {boolean tomMatch418_29= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch418__end__24=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));do {{if (!( tomMatch418__end__24.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch418_27= tomMatch418__end__24.getHeadconcTypeOption() ;if ( (tomMatch418_27 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch418_27) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch418_29= true ;}}}if ( tomMatch418__end__24.isEmptyconcTypeOption() ) {tomMatch418__end__24=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));} else {tomMatch418__end__24= tomMatch418__end__24.getTailconcTypeOption() ;}}} while(!( (tomMatch418__end__24==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));}if (!(tomMatch418_29)) {if (  tomMatch418_19.getTomType() .equals(tom_tName2) ) {



                //DEBUG System.out.println("isSubtypeOf: cases 1a and 3a - ii");
                return true; 
              }}}}}if ( tomMatch418__end__15.isEmptyconcTomType() ) {tomMatch418__end__15=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1));} else {tomMatch418__end__15= tomMatch418__end__15.getTailconcTomType() ;}}} while(!( (tomMatch418__end__15==(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) ));}}}}{if ( (((Object)tom_tOptions1) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch418__end__35=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));do {{if (!( tomMatch418__end__35.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch418_45= tomMatch418__end__35.getHeadconcTypeOption() ;if ( (tomMatch418_45 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch418_45) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch418__end__41=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));do {{if (!( tomMatch418__end__41.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch418_48= tomMatch418__end__41.getHeadconcTypeOption() ;if ( (tomMatch418_48 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch418_48) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( ( tomMatch418_45.getRootSymbolName() == tomMatch418_48.getRootSymbolName() ) ) {if ( tom_tName2.equals(tom_tName1) ) {





                //DEBUG System.out.println("isSubtypeOf: case 4a - i");
                return true; 
              }}}}}if ( tomMatch418__end__41.isEmptyconcTypeOption() ) {tomMatch418__end__41=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));} else {tomMatch418__end__41= tomMatch418__end__41.getTailconcTypeOption() ;}}} while(!( (tomMatch418__end__41==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));}}}}}if ( tomMatch418__end__35.isEmptyconcTypeOption() ) {tomMatch418__end__35=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));} else {tomMatch418__end__35= tomMatch418__end__35.getTailconcTypeOption() ;}}} while(!( (tomMatch418__end__35==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) ));}}}{if ( (((Object)tom_tOptions1) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch418__end__57=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));do {{if (!( tomMatch418__end__57.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch418_73= tomMatch418__end__57.getHeadconcTypeOption() ;if ( (tomMatch418_73 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch418_73) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch418__end__63=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));do {{if (!( tomMatch418__end__63.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch418_76= tomMatch418__end__63.getHeadconcTypeOption() ;if ( (tomMatch418_76 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch418_76) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( ( tomMatch418_73.getRootSymbolName() == tomMatch418_76.getRootSymbolName() ) ) {if ( (((Object)supTypet1) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch418__end__69=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1));do {{if (!( tomMatch418__end__69.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch418_79= tomMatch418__end__69.getHeadconcTomType() ;if ( (tomMatch418_79 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch418_79) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if (  tomMatch418_79.getTomType() .equals(tom_tName2) ) {




                //DEBUG System.out.println("isSubtypeOf: case 4a - ii");
                return true; 
              }}}}if ( tomMatch418__end__69.isEmptyconcTomType() ) {tomMatch418__end__69=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1));} else {tomMatch418__end__69= tomMatch418__end__69.getTailconcTomType() ;}}} while(!( (tomMatch418__end__69==(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) ));}}}}}}if ( tomMatch418__end__63.isEmptyconcTypeOption() ) {tomMatch418__end__63=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));} else {tomMatch418__end__63= tomMatch418__end__63.getTailconcTypeOption() ;}}} while(!( (tomMatch418__end__63==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));}}}}}if ( tomMatch418__end__57.isEmptyconcTypeOption() ) {tomMatch418__end__57=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));} else {tomMatch418__end__57= tomMatch418__end__57.getTailconcTypeOption() ;}}} while(!( (tomMatch418__end__57==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) ));}}}}

          return false;
        }}}}}}}}

    // Remain cases
    return false;
  }

  /**
   * The method <code>supType</code> tries to find the lowest common uppertype
   * of two given ground types.
   * <p>
   * There exists 2 kinds of ground types: ground types Ti (represented by Ti^?) and
   * ground types Ti^c which are decorated with a given symbol c. Considering
   * them by pairs, then the set of all possibilities of arrangement between
   * ground types is a sequence with repetition. Then, we have 4 possible cases (since
   * 2^2 = 4).
   * <p>
   * CASE 1: T1^? and  T2^?
   *  a) --> T2^? if T1 is a subtype of T2
   *  b) --> T1^? if T2 is a subtype of T1
   *  c) --> emptyType otherwise
   * <p>
   * CASE 2: T1^? and T2^c
   *  a) --> T1^? if T2 is a subtype of T1
   *  b) --> T3^? if T3 is the join between T1 and T2
   *  c) --> emptyType otherwise
   * <p>
   * CASE 3: T1^c and T2^? 
   *  similar to CASE 2.
   * <p>
   * CASE 4: T1^c1 and T2^c2
   *  - 4.1 : T1 == T2
   *      --> T1^?
   *  - 4.2 : T1 != T2
   *      a) --> T2^c1 if T1 is a subtype of T2 and c1 == c2
   *      b) --> T1^c1 if T2 is a subtype of T1 and c1 == c2
   *      c) --> T3^? if T3 is the join between T1 and T2 
   *      d) --> emptyType otherwise
   * <p>
   * OBS.: when the subtype relation does not hold between two types 'T1' and
   * 'T2', the method considers the intersection of the supertypes lists
   * supertypes_T1 and supertypes_T2 and searches for the 'immediate' common
   * supertype 'Ti' (i.e. the one which has the bigger supertypes_Ti list) 
   * @param t1  a type
   * @param t2  another type
   * @return    the uppertype between 't1' and 't2' if the subtype relation
   *            holds between them or the lowest common uppertype of them if
   *            they are not in subtype relation 
   *            the 'EmptyType()' otherwise
   */
  private TomType supType(TomType t1, TomType t2) {
    /* CASES 1a, 1b, 2a, 3a, 4.2a and 4.2b */
    if (isSubtypeOf(t1,t2)) { return t2; } 
    if (isSubtypeOf(t2,t1)) { return t1; }
    TomTypeList supTypes1 = dependencies.get(t1.getTomType());
    TomTypeList supTypes2 = dependencies.get(t2.getTomType());
    {{if ( (((Object)t1) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)t1)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)t1))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch419_2= (( tom.engine.adt.tomtype.types.TomType )((Object)t1)).getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch419_2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch419_2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch419__end__13=tomMatch419_2;do {{if (!( tomMatch419__end__13.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch419_24= tomMatch419__end__13.getHeadconcTypeOption() ;if ( (tomMatch419_24 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch419_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) { String  tom_tName= (( tom.engine.adt.tomtype.types.TomType )((Object)t1)).getTomType() ;if ( (((Object)t2) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)t2)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)t2))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch419_6= (( tom.engine.adt.tomtype.types.TomType )((Object)t2)).getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch419_6) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch419_6) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch419__end__19=tomMatch419_6;do {{if (!( tomMatch419__end__19.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch419_27= tomMatch419__end__19.getHeadconcTypeOption() ;if ( (tomMatch419_27 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch419_27) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( tom_tName.equals( (( tom.engine.adt.tomtype.types.TomType )((Object)t2)).getTomType() ) ) {boolean tomMatch419_30= false ;if ( ( tomMatch419_24.getRootSymbolName() == tomMatch419_27.getRootSymbolName() ) ) {tomMatch419_30= true ;}if (!(tomMatch419_30)) {




          //DEBUG System.out.println("\nIn supType: case 4.1");
          // Return the equivalent groudn type without decoration
          return symbolTable.getType(tom_tName); 
        }}}}}if ( tomMatch419__end__19.isEmptyconcTypeOption() ) {tomMatch419__end__19=tomMatch419_6;} else {tomMatch419__end__19= tomMatch419__end__19.getTailconcTypeOption() ;}}} while(!( (tomMatch419__end__19==tomMatch419_6) ));}}}}}}}if ( tomMatch419__end__13.isEmptyconcTypeOption() ) {tomMatch419__end__13=tomMatch419_2;} else {tomMatch419__end__13= tomMatch419__end__13.getTailconcTypeOption() ;}}} while(!( (tomMatch419__end__13==tomMatch419_2) ));}}}}}{if ( (((Object)supTypes1) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((Object)supTypes2) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {boolean tomMatch419_36= false ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if ( (( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes1)).isEmptyconcTomType() ) {tomMatch419_36= true ;}}if (!(tomMatch419_36)) {boolean tomMatch419_35= false ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes2))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes2))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if ( (( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes2)).isEmptyconcTomType() ) {tomMatch419_35= true ;}}if (!(tomMatch419_35)) {



        int st1Size = supTypes1.length();
        int st2Size = supTypes2.length();

        int currentIntersectionSize = -1;
        int commonTypeIntersectionSize = -1;
        TomType lowestCommonType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
        //DEBUG System.out.println("\nIn supType: cases 2b, 3b, 4.2c");
        for (TomType currentType:supTypes1.getCollectionconcTomType()) {
          currentIntersectionSize = dependencies.get(currentType.getTomType()).length();
          if (supTypes2.getCollectionconcTomType().contains(currentType) &&
              (currentIntersectionSize > commonTypeIntersectionSize)) {
            commonTypeIntersectionSize = currentIntersectionSize;
            lowestCommonType = currentType;
          }
        }
        return lowestCommonType;
      }}}}}}

    /* Remaining CASES */
    return  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
  }

  /**
   * The method <code>generateSolutions</code> chooses a solution for a subtype
   * constraint list when many possibilities are available. The algorithm picks
   * the lowest possible upper bound proposed for each type variable. For this
   * reason, the order of rules is important.
   * <p>
   * A subtype constraint can be written by 3 formats:
   * F1 - A <: T
   * F2 - T <: A
   * F3 - A1 <: A2
   * where 'Ai' and 'Ti' are respectively type variables and ground types (and
   * the constraint 'T1 <: T2' was already handled by garbage collecting in
   * <code>solveSubtypeConstraints</code> method.
   * These constraints are enumerated by cases:
   * <p>
   * CASE 1:
   * tCList = {A <: T} U tCList' and Map 
   *  -->  tCList = tCList' and {(A,T)} U Map if A is not in Var(tCList')
   * <p>
   * CASE 2:
   * tCList = {T <: A} U tCList' and Map 
   *  -->  tCList = tCList' and {(A,T)} U Map if A is not in Var(tCList')
   * <p>
   * CASE 3:
   * tCList = {A1 <: A2} U tCList' and Map 
   *  -->  tCList = tCList' and {(A1,A2)} U Map if Ai are not in tCList'
   * <p>
   * Considering combination of {F1 x F3}, {F2 x F3}, {F3 x F3} and {F1 x F2} (since 
   * {F1 x F1} and {F2 x F2} are treated by PHASE 4 of
   * <code>solveSubtypingConstraints</code> or by previous cases):
   * <p>
   * CASE 4 {F1 x F3} and {F3 x F1}:
   *    a) tCList = {A <: T, A <: A1} U tCList' and Map
   *      -->  tCList = {T <: A1} U [A/T]tCList' and {(A,T)} U Map
   *    TODO : verify rule b
   *    b) tCList = {A <: T, A1 <: A} U tCList' and Map 
   *      --> tCList = tClist' and Map or do nothing?
   *    c) tCList = {A <: T, A1 <: A2} U tCList' and Map 
   *      --> CASE 1 for A <: T and CASE 3 for A1 <: A2 
   * <p>
   * CASE 5 {F2 x F3} and {F3 x F2}:
   *    a) tCList = {T <: A, A1 <: A} U tCList' and Map 
   *      -->  tCList = {A1 <: T} U [A/T]tCList' and {(A,T)} U Map
   *    TODO : verify rule b
   *    b) tCList = {T <: A, A <: A1} U tCList' and Map 
   *      --> tCList = tClist' and Map or do nothing?
   *    c) tCList = {T <: A, A1 <: A2} U tCList' and Map 
   *      --> CASE 2 for T <: A and CASE 3 for A1 <: A2
   * <p>
   * CASE 6 {F1 x F2} and {F2 x F1}:
   *    a) tCList = {A <: T, T <: A1} U tCList' and Map 
   *      --> CASE 1 for A <: T and CASE 2 for T <: A1 
   *    b) tCList = {A <: T, T1 <: A1} U tCList' and Map 
   *      --> CASE 1 for A <: T and CASE 2 for T1 <: A1 
   *    c) tCList = {A <: T, T1 <: A} U tCList' and Map 
   *      -->  tCList = {T1 <: T} U [A/T]tCList' and {(A,T)} U Map
   *     Note: in this case T1 is different from T (since the case where T1 is
   *     equals to T is treated by PHASE 1 of
   *     <code>solveSubtypingConstraints</code>.
   * <p>
   * CASE 7 {F3 x F3}:
   *    a) tCList = {A <: A1, A <: A2} U tCList' and Map 
   *      --> Nothing, java must infer the right type 
   *    b) tCList = {A <: A1, A2 <: A3} U tCList' and Map 
   *      --> Nothing, java must infer the right type 
   *    c) tCList = {A1 <: A, A2 <: A} U tCList' and Map 
   *      --> Nothing, java must infer the right type 
   *    d) tCList = {A1 <: A, A <: A2} U tCList' and Map 
   *      --> Nothing, java must infer the right type 
   * <p>
   * @param nkt an instance of object NewKernelTyper
  * @return    the subtype constraint list resulting
    */
    private TypeConstraintList generateSolutions(TypeConstraintList tCList) {
      TypeConstraintList newtCList = tCList;
matchBlockSolve :
      {
        {{if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch420__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch420__end__4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch420_9= tomMatch420__end__4.getHeadconcTypeConstraint() ;if ( (tomMatch420_9 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch420_9) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch420_7= tomMatch420_9.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch420_8= tomMatch420_9.getType2() ;if ( (tomMatch420_7 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch420_7) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch420_8;boolean tomMatch420_16= false ;if ( (tomMatch420_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch420_8) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType==tomMatch420_8) ) {tomMatch420_16= true ;}}}if (!(tomMatch420_16)) {


            //DEBUG System.out.println("\nenumerateSolutions1: " + `c1);
            //if (!`findVar(tVar,concTypeConstraint(leftTCL,rightTCL))) {
              // Same code of cases 7 and 8 of solveEquationConstraints
              addSubstitution(tomMatch420_7,tom_groundType);
              newtCList =
                replaceInSubtypingConstraints(tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)),tomMatch420__end__4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch420__end__4.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
              break matchBlockSolve;
            //}
          }}}}}}if ( tomMatch420__end__4.isEmptyconcTypeConstraint() ) {tomMatch420__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch420__end__4= tomMatch420__end__4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch420__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}{if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch420__end__21=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch420__end__21.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch420_26= tomMatch420__end__21.getHeadconcTypeConstraint() ;if ( (tomMatch420_26 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch420_26) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch420_24= tomMatch420_26.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch420_25= tomMatch420_26.getType2() ; tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch420_24;if ( (tomMatch420_25 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch420_25) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch420_33= false ;if ( (tomMatch420_24 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch420_24) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType==tomMatch420_24) ) {tomMatch420_33= true ;}}}if (!(tomMatch420_33)) {



            //DEBUG System.out.println("\nenumerateSolutions2: " + `c1);
            //if (!`findVar(tVar,concTypeConstraint(leftTCL,rightTCL))) {
              addSubstitution(tomMatch420_25,tom_groundType);
              newtCList =
                replaceInSubtypingConstraints(tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)),tomMatch420__end__21, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch420__end__21.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
              break matchBlockSolve;
            //}
          }}}}}}if ( tomMatch420__end__21.isEmptyconcTypeConstraint() ) {tomMatch420__end__21=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch420__end__21= tomMatch420__end__21.getTailconcTypeConstraint() ;}}} while(!( (tomMatch420__end__21==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}




























































































      }
      return newtCList;
    }

  private void garbageCollection(TypeConstraintList tCList) {
    TypeConstraintList newtCList = tCList;
    {{if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch421__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));do {{if (!( tomMatch421__end__4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch421_10= tomMatch421__end__4.getHeadconcTypeConstraint() ;if ( (tomMatch421_10 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch421_10) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch421_7= tomMatch421_10.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch421_8= tomMatch421_10.getType2() ;if ( (tomMatch421_7 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch421_7) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tomMatch421_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch421_8) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.typeconstraints.types.Info  tom_info= tomMatch421_10.getInfo() ;{{if ( (((Object)inputTVarList) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)inputTVarList))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)inputTVarList))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch422__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)inputTVarList));do {{if (!( tomMatch422__end__4.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom_tVar= tomMatch422__end__4.getHeadconcTomType() ;if ( (tomMatch421_7==tom_tVar) ) {printErrorGuessMatch(tom_info)





;
            newtCList =
               tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(newtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
          }if ( (tomMatch421_8==tom_tVar) ) {printErrorGuessMatch(tom_info);             newtCList =                tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(newtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;           }}if ( tomMatch422__end__4.isEmptyconcTomType() ) {tomMatch422__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)inputTVarList));} else {tomMatch422__end__4= tomMatch422__end__4.getTailconcTomType() ;}}} while(!( (tomMatch422__end__4==(( tom.engine.adt.tomtype.types.TomTypeList )((Object)inputTVarList))) ));}}}}

      }}}}}}}if ( tomMatch421__end__4.isEmptyconcTypeConstraint() ) {tomMatch421__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));} else {tomMatch421__end__4= tomMatch421__end__4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch421__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));}}}}

  }

  private void printErrorGuessMatch(Info info) {
    {{if ( (((Object)info) instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )((Object)info)) instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )(( tom.engine.adt.typeconstraints.types.Info )((Object)info))) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) { tom.engine.adt.tomname.types.TomName  tomMatch423_1= (( tom.engine.adt.typeconstraints.types.Info )((Object)info)).getAstName() ;if ( (tomMatch423_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch423_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_termName= tomMatch423_1.getString() ;

        Option option = TomBase.findOriginTracking( (( tom.engine.adt.typeconstraints.types.Info )((Object)info)).getOptions() );
        {{if ( (((Object)option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )((Object)option)) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)option))) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

            TomMessage.error(logger, (( tom.engine.adt.tomoption.types.Option )((Object)option)).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )((Object)option)).getLine() ,
                TomMessage.cannotGuessMatchType,tom_termName); 
          }}}}{if ( (((Object)option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )((Object)option)) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)option))) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {

            TomMessage.error(logger,null,0,
                TomMessage.cannotGuessMatchType,tom_termName); 
          }}}}}

      }}}}}}}

  }

  /**
   * The method <code>isTypeVar</code> checks if a given type is a type variable.
   * @param type the type to be checked
   * @return     'true' if teh type is a variable type
   *             'false' otherwise
   */
  private boolean isTypeVar(TomType type) {
    {{if ( (((Object)type) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)type)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)type))) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 return true; }}}}}

    return false;
  }

  /**
   * The method <code>printErrorIncompatibility</code> prints an 'incompatible types' message
   * enriched by informations about a given type constraint.
   * @param tConstraint  the type constraint to be printed
   */
  private void printErrorIncompatibility(TypeConstraint tConstraint) {
    {{if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch426_23= false ; tom.engine.adt.tomtype.types.TomType  tomMatch426_5= null ; tom.engine.adt.tomtype.types.TomType  tomMatch426_4= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch426_8= null ; tom.engine.adt.typeconstraints.types.Info  tomMatch426_6= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch426_9= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch426_23= true ;tomMatch426_8=(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint));tomMatch426_4= tomMatch426_8.getType1() ;tomMatch426_5= tomMatch426_8.getType2() ;tomMatch426_6= tomMatch426_8.getInfo() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch426_23= true ;tomMatch426_9=(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint));tomMatch426_4= tomMatch426_9.getType1() ;tomMatch426_5= tomMatch426_9.getType2() ;tomMatch426_6= tomMatch426_9.getInfo() ;}}}}}if (tomMatch426_23) { tom.engine.adt.tomtype.types.TomType  tom_tType1=tomMatch426_4; tom.engine.adt.tomtype.types.TomType  tom_tType2=tomMatch426_5; tom.engine.adt.typeconstraints.types.Info  tom_info=tomMatch426_6;if ( (((Object)tom_tType1) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType1)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType1))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_tName1= (( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType1)).getTomType() ;if ( (((Object)tom_tType2) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType2)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType2))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_tName2= (( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType2)).getTomType() ;if ( (((Object)tom_info) instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )((Object)tom_info)) instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )(( tom.engine.adt.typeconstraints.types.Info )((Object)tom_info))) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) { tom.engine.adt.tomname.types.TomName  tomMatch426_16= (( tom.engine.adt.typeconstraints.types.Info )((Object)tom_info)).getAstName() ;if ( (tomMatch426_16 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch426_16) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_termName= tomMatch426_16.getString() ;





          Option option = TomBase.findOriginTracking( (( tom.engine.adt.typeconstraints.types.Info )((Object)tom_info)).getOptions() );
          {{if ( (((Object)option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )((Object)option)) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)option))) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

              TomMessage.error(logger, (( tom.engine.adt.tomoption.types.Option )((Object)option)).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )((Object)option)).getLine() ,
                  TomMessage.incompatibleTypes,tom_tName1,tom_tName2,tom_termName); 
            }}}}{if ( (((Object)option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )((Object)option)) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)option))) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {

              TomMessage.error(logger,null,0,
                  TomMessage.incompatibleTypes,tom_tName1,tom_tName2,tom_termName); 
            }}}}}

        }}}}}}}}}}}}}}}

  }

  /**
   * The method <code>replaceInCode</code> calls the strategy
   * <code>replaceFreshTypeVar</code> to apply substitution on each type variable occuring in
   * a given Code.
   * @param code the code to be replaced
   * @return     the replaced code resulting
   */
  private Code replaceInCode(Code code) {
    Code replacedCode = code;
    try {
      replacedCode = tom_make_InnermostId(tom_make_replaceFreshTypeVar(this)).visitLight(code);
      //`InnermostId(checkTypeOfVariables(this)).visitLight(replacedCode);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInCode: failure on " +
          replacedCode);
    }
    return replacedCode;
  }

  /**
   * The method <code>replaceInSymbolTable</code> calls the strategy
   * <code>replaceFreshTypeVar</code> to apply substitution on each type variable occuring in
   * the Symbol Table.
   */
  private void replaceInSymbolTable() {
    for(String tomName:symbolTable.keySymbolIterable()) {
      //DEBUG System.out.println("replaceInSymboltable() - tomName : " + tomName);
      TomSymbol tSymbol = getSymbolFromName(tomName);
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol before strategy: "
      //DEBUG     + tSymbol);
      try {
        tSymbol = tom_make_InnermostId(tom_make_replaceFreshTypeVar(this)).visitLight(tSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInSymbolTable: failure on " +
            tSymbol);
      }
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol after strategy: "
      //DEBUG     + tSymbol);
      symbolTable.putSymbol(tomName,tSymbol);
    }
  }

  /**
   * The class <code>replaceFreshTypeVar</code> is generated from a strategy
   * which replace each type variable occurring in a given expression by its corresponding substitution. 
   * @param nkt an instance of object NewKernelTyper
   * @return    the expression resulting of a transformation
   */
  public static class replaceFreshTypeVar extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public replaceFreshTypeVar( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg))) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar=(( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg));


        if (nkt.substitutions.containsKey(tom_typeVar)) {
          return nkt.substitutions.get(tom_typeVar);
        } 
      }}}}}return _visit_TomType(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_replaceFreshTypeVar( NewKernelTyper  t0) { return new replaceFreshTypeVar(t0);}



  /**
   * The class <code>checkTypeOfVariables</code> is generated from a strategy
   * which checks if all cast type (in a MatchConstraint) have been inferred. In
   * the negative case, a 'canotGuessMatchType' message error is printed.
   * @param nkt an instance of object NewKernelTyper
   */
  /*
  %strategy checkTypeOfVariables(nkt:NewKernelTyper) extends Identity() {
    visit Constraint {
      MatchConstraint[Pattern=Variable[Options=oList,AstName=Name(name),AstType=TypeVar(_,_)],Subject=BQVariable[]] -> {
        Option option = TomBase.findOriginTracking(`oList);
        %match(option) {
          OriginTracking(_,line,fileName) -> {
            TomMessage.error(logger,`fileName, `line,
                TomMessage.cannotGuessMatchType,`name); 
          }
        }
      }
    }
  }*/

  /**
   * The method <code>printGeneratedConstraints</code> prints braces and calls the method
   * <code>printEachConstraint</code> for a given list.
   * @param tCList the type constraint list to be printed
   */
  public void printGeneratedConstraints(TypeConstraintList tCList) {
    {{if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch429_2= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).isEmptyconcTypeConstraint() ) {tomMatch429_2= true ;}}if (!(tomMatch429_2)) {
 
        System.out.print("\n------ Type Constraints : \n {");
        printEachConstraint(tCList);
        System.out.print("}");
      }}}}

  }

  /**
   * The method <code>printEachConstraint</code> prints symbols '=' and '<:' and calls the method
   * <code>printType</code> for each type occurring in a given type constraint.
   * @param tCList the type constraint list to be printed
   */
  public void printEachConstraint(TypeConstraintList tCList) {
    {{if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch430_7= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).getHeadconcTypeConstraint() ;if ( (tomMatch430_7 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch430_7) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).getTailconcTypeConstraint() ;

        printType( tomMatch430_7.getType1() );
        System.out.print(" = ");
        printType( tomMatch430_7.getType2() );
        if (tom_tailtCList!=  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
          System.out.print(", "); 
          printEachConstraint(tom_tailtCList);
        }
      }}}}}}{if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch430_16= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).getHeadconcTypeConstraint() ;if ( (tomMatch430_16 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch430_16) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).getTailconcTypeConstraint() ;


        printType( tomMatch430_16.getType1() );
        System.out.print(" <: ");
        printType( tomMatch430_16.getType2() );
        if (tom_tailtCList!=  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
          System.out.print(", "); 
          printEachConstraint(tom_tailtCList);
        }
      }}}}}}}

  }

  /**
   * The method <code>printType</code> prints a given type.
   * @param tCList the type to be printed
   */   
  public void printType(TomType type) {
    System.out.print(type);
  }
} // NewKernelTyper
