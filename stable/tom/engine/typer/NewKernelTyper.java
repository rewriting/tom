/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 * Jean-Christophe Bach e-mail: Jeanchristophe.Bach@inria.fr
 *
 **/

package tom.engine.typer;

import java.util.*;
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
  // bag used to efficiently check that a constraint is already in a list
  private Collection<TypeConstraint> constraintBag;

  // Set of pairs (freshVar,type)
  private Substitution substitutions;
  // Set of supertypes for each type
  private HashMap<String,TomTypeList> dependencies = new HashMap<String,TomTypeList>();

  private SymbolTable symbolTable;

  private String currentInputFileName;

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
   * The method <code>getType</code> gets the type of a term by consulting the
   * SymbolTable.
   * @param bqTerm  the BQTerm requesting a type
   * @return        the type of the BQTerm
   */
  protected TomType getType(BQTerm bqTerm) {
    {{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch400_6= false ; tom.engine.adt.code.types.BQTerm  tomMatch400_3= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_5= null ; tom.engine.adt.tomtype.types.TomType  tomMatch400_1= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch400_6= true ;tomMatch400_3=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_1= tomMatch400_3.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch400_6= true ;tomMatch400_4=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_1= tomMatch400_4.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) {{tomMatch400_6= true ;tomMatch400_5=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_1= tomMatch400_5.getAstType() ;}}}}}}}if (tomMatch400_6) {
 return tomMatch400_1; }}}{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch400_22= false ; tom.engine.adt.code.types.BQTerm  tomMatch400_10= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_16= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_17= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_13= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_15= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_11= null ; tom.engine.adt.tomname.types.TomName  tomMatch400_8= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_14= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_12= null ; tom.engine.adt.code.types.BQTerm  tomMatch400_18= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch400_22= true ;tomMatch400_10=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_8= tomMatch400_10.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {{tomMatch400_22= true ;tomMatch400_11=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_8= tomMatch400_11.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {{tomMatch400_22= true ;tomMatch400_12=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_8= tomMatch400_12.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {{tomMatch400_22= true ;tomMatch400_13=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_8= tomMatch400_13.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {{tomMatch400_22= true ;tomMatch400_14=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_8= tomMatch400_14.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {{tomMatch400_22= true ;tomMatch400_15=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_8= tomMatch400_15.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {{tomMatch400_22= true ;tomMatch400_16=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_8= tomMatch400_16.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {{tomMatch400_22= true ;tomMatch400_17=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_8= tomMatch400_17.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {{tomMatch400_22= true ;tomMatch400_18=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch400_8= tomMatch400_18.getAstName() ;}}}}}}}}}}}}}}}}}}}if (tomMatch400_22) {if ( (tomMatch400_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch400_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        TomSymbol tSymbol = getSymbolFromName( tomMatch400_8.getString() );
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
    {{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 return getType( (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getTomTerm() ); }}}}{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch401_9= false ; tom.engine.adt.tomtype.types.TomType  tomMatch401_5= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch401_7= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch401_8= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch401_9= true ;tomMatch401_7=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch401_5= tomMatch401_7.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch401_9= true ;tomMatch401_8=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch401_5= tomMatch401_8.getAstType() ;}}}}}if (tomMatch401_9) {
 return tomMatch401_5; }}}{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch401_11= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch401_11) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch401_11) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch401_11.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch401_18= tomMatch401_11.getHeadconcTomName() ;if ( (tomMatch401_18 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch401_18) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        TomSymbol tSymbol = getSymbolFromName( tomMatch401_18.getString() );
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
    {{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 return getInfoFromTomTerm( (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getTomTerm() ); }}}}{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch402_10= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch402_5= null ; tom.engine.adt.tomname.types.TomName  tomMatch402_6= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch402_8= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch402_9= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch402_10= true ;tomMatch402_8=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch402_5= tomMatch402_8.getOptions() ;tomMatch402_6= tomMatch402_8.getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch402_10= true ;tomMatch402_9=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch402_5= tomMatch402_9.getOptions() ;tomMatch402_6= tomMatch402_9.getAstName() ;}}}}}if (tomMatch402_10) {
 
        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch402_6, tomMatch402_5) ; 
      }}}{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch402_13= (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch402_13) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch402_13) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch402_13.isEmptyconcTomName() )) {
 
        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch402_13.getHeadconcTomName() ,  (( tom.engine.adt.tomterm.types.TomTerm )tTerm).getOptions() ) ; 
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
    {{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch403_7= false ; tom.engine.adt.code.types.BQTerm  tomMatch403_4= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch403_1= null ; tom.engine.adt.tomname.types.TomName  tomMatch403_2= null ; tom.engine.adt.code.types.BQTerm  tomMatch403_6= null ; tom.engine.adt.code.types.BQTerm  tomMatch403_5= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch403_7= true ;tomMatch403_4=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch403_1= tomMatch403_4.getOptions() ;tomMatch403_2= tomMatch403_4.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch403_7= true ;tomMatch403_5=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch403_1= tomMatch403_5.getOptions() ;tomMatch403_2= tomMatch403_5.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch403_7= true ;tomMatch403_6=(( tom.engine.adt.code.types.BQTerm )bqTerm);tomMatch403_1= tomMatch403_6.getOptions() ;tomMatch403_2= tomMatch403_6.getAstName() ;}}}}}}}if (tomMatch403_7) {
 
        return  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch403_2, tomMatch403_1) ; 
      }}}{if ( (bqTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqTerm)) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) { tom.engine.adt.code.types.BQTerm  tomMatch403_end_12=(( tom.engine.adt.code.types.BQTerm )bqTerm);do {{if (!( tomMatch403_end_12.isEmptyComposite() )) { tom.engine.adt.code.types.CompositeMember  tomMatch403_16= tomMatch403_end_12.getHeadComposite() ;if ( (tomMatch403_16 instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )tomMatch403_16) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) {


        return getInfoFromBQTerm( tomMatch403_16.getterm() );
      }}}if ( tomMatch403_end_12.isEmptyComposite() ) {tomMatch403_end_12=(( tom.engine.adt.code.types.BQTerm )bqTerm);} else {tomMatch403_end_12= tomMatch403_end_12.getTailComposite() ;}}} while(!( (tomMatch403_end_12==(( tom.engine.adt.code.types.BQTerm )bqTerm)) ));}}}}
 
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
    {{if ( (tType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tType) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )tType)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 return  tom.engine.adt.tomtype.types.tomtype.TypeVar.make( (( tom.engine.adt.tomtype.types.TomType )tType).getTomType() , getFreshTlTIndex()) ; }}}}}

    throw new TomRuntimeException("getUnknownFreshTypeVar: should not be here.");
  }


  protected TypeConstraintList addConstraintSlow(TypeConstraint tConstraint, TypeConstraintList tCList) {
    if(!containsConstraint(tConstraint,tCList)) {
      {{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch405_14= false ; tom.engine.adt.tomtype.types.TomType  tomMatch405_1= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch405_5= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch405_4= null ; tom.engine.adt.tomtype.types.TomType  tomMatch405_2= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch405_14= true ;tomMatch405_4=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch405_1= tomMatch405_4.getType1() ;tomMatch405_2= tomMatch405_4.getType2() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch405_14= true ;tomMatch405_5=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch405_1= tomMatch405_5.getType1() ;tomMatch405_2= tomMatch405_5.getType2() ;}}}}}if (tomMatch405_14) { tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch405_1; tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch405_2;boolean tomMatch405_13= false ;if ( (tomMatch405_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch405_2) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {if ( (tom_t2==tomMatch405_2) ) {tomMatch405_13= true ;}}}if (!(tomMatch405_13)) {boolean tomMatch405_12= false ;if ( (tomMatch405_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch405_1) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {if ( (tom_t1==tomMatch405_1) ) {tomMatch405_12= true ;}}}if (!(tomMatch405_12)) {if (!( (tom_t2==tom_t1) )) {
 
          tCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
        }}}}}}}

    }
    return tCList;
  }

  /**
   * The method <code>containsConstraint</code> checks if a given constraint
   * already exists in a constraint type list. The method considers symmetry for
   * equation constraints. 
   * slow code used only by addConstraintSlow (for compatibility reasons)
   * @param tConstraint the constraint to be considered
   * @param tCList      the type constraint list to be traversed
   * @return            'true' if the constraint already exists in the list
   *                    'false' otherwise            
   */
  protected boolean containsConstraint(TypeConstraint tConstraint, TypeConstraintList tCList) {
    {{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch406_end_9=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{if (!( tomMatch406_end_9.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch406_14= tomMatch406_end_9.getHeadconcTypeConstraint() ;boolean tomMatch406_19= false ; tom.engine.adt.tomtype.types.TomType  tomMatch406_13= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch406_16= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch406_15= null ; tom.engine.adt.tomtype.types.TomType  tomMatch406_12= null ;if ( (tomMatch406_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch406_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch406_19= true ;tomMatch406_15=tomMatch406_14;tomMatch406_12= tomMatch406_15.getType1() ;tomMatch406_13= tomMatch406_15.getType2() ;}} else {if ( (tomMatch406_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch406_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch406_19= true ;tomMatch406_16=tomMatch406_14;tomMatch406_12= tomMatch406_16.getType1() ;tomMatch406_13= tomMatch406_16.getType2() ;}}}}}if (tomMatch406_19) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ==tomMatch406_12) ) {if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ==tomMatch406_13) ) {
 return true; }}}}if ( tomMatch406_end_9.isEmptyconcTypeConstraint() ) {tomMatch406_end_9=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch406_end_9= tomMatch406_end_9.getTailconcTypeConstraint() ;}}} while(!( (tomMatch406_end_9==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tom_t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch406_end_29=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{if (!( tomMatch406_end_29.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch406_34= tomMatch406_end_29.getHeadconcTypeConstraint() ;if ( (tomMatch406_34 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch406_34) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tom_t3= tomMatch406_34.getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t4= tomMatch406_34.getType2() ;


        if((tom_t1==tom_t3&& tom_t2==tom_t4) || (tom_t1==tom_t4&& tom_t2==tom_t3)) {
          return true; 
        }
      }}}if ( tomMatch406_end_29.isEmptyconcTypeConstraint() ) {tomMatch406_end_29=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch406_end_29= tomMatch406_end_29.getTailconcTypeConstraint() ;}}} while(!( (tomMatch406_end_29==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}}

    return containsConstraintModuloEqDecoratedSort(tConstraint,tCList);
  } 
  
  /**
   * The method <code>containsConstraintModuloEqDecoratedSort</code> checks if a given constraint
   * already exists in a constraint type list. The method considers symmetry for
   * equation constraints and equality of decorated sorts in other to allow the
   * following:
   * insert((A = T^?),C)	->	C,              if (A = T^c) in C \/ (T^c = A) in C 
   *                      ->  {A = T^?} U C,  otherwise
   *
   * insert((T^? = A),C)	->	C,              if (A = T^c) in C \/ (T^c = A) in C 
   *                      ->  {T^? = A} U C,  otherwise
   * 
   * slow code used only by containsConstraint (for compatibility reasons)
   * @param tConstraint the constraint to be considered
   * @param tCList      the type constraint list to be traversed
   * @return            'true' if an equal constraint modulo EqOfDecSort already exists in the list
   *                    'false' otherwise            
   */
  protected boolean containsConstraintModuloEqDecoratedSort(TypeConstraint tConstraint, TypeConstraintList tCList) {
    {{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch407_4= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch407_5= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch407_4 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_4) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tomMatch407_5 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_5) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch407_5.getTypeOptions() ;if ( (tom_tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch407_end_17=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{if (!( tomMatch407_end_17.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch407_28= tomMatch407_end_17.getHeadconcTypeConstraint() ;if ( (tomMatch407_28 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch407_28) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch407_27= tomMatch407_28.getType2() ;if ( (tomMatch407_4== tomMatch407_28.getType1() ) ) {if ( (tomMatch407_27 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_27) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_decoratedtOptions= tomMatch407_27.getTypeOptions() ;if (  tomMatch407_5.getTomType() .equals( tomMatch407_27.getTomType() ) ) {if ( (tom_decoratedtOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch407_end_23=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions);do {{if (!( tomMatch407_end_23.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch407_34= tomMatch407_end_23.getHeadconcTypeOption() ;if ( (tomMatch407_34 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch407_34) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch407_46= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch407_end_39=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);do {{if (!( tomMatch407_end_39.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch407_42= tomMatch407_end_39.getHeadconcTypeOption() ;if ( (tomMatch407_42 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch407_42) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch407_46= true ;}}}if ( tomMatch407_end_39.isEmptyconcTypeOption() ) {tomMatch407_end_39=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);} else {tomMatch407_end_39= tomMatch407_end_39.getTailconcTypeOption() ;}}} while(!( (tomMatch407_end_39==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) ));}if (!(tomMatch407_46)) {



 return true; }}}}if ( tomMatch407_end_23.isEmptyconcTypeOption() ) {tomMatch407_end_23=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions);} else {tomMatch407_end_23= tomMatch407_end_23.getTailconcTypeOption() ;}}} while(!( (tomMatch407_end_23==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) ));}}}}}}}}}if ( tomMatch407_end_17.isEmptyconcTypeConstraint() ) {tomMatch407_end_17=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch407_end_17= tomMatch407_end_17.getTailconcTypeConstraint() ;}}} while(!( (tomMatch407_end_17==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch407_51= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch407_52= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch407_51 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_51) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tomMatch407_52 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_52) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch407_52.getTypeOptions() ;if ( (tom_tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch407_end_64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{if (!( tomMatch407_end_64.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch407_75= tomMatch407_end_64.getHeadconcTypeConstraint() ;if ( (tomMatch407_75 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch407_75) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch407_73= tomMatch407_75.getType1() ;if ( (tomMatch407_73 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_73) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_decoratedtOptions= tomMatch407_73.getTypeOptions() ;if (  tomMatch407_52.getTomType() .equals( tomMatch407_73.getTomType() ) ) {if ( (tomMatch407_51== tomMatch407_75.getType2() ) ) {if ( (tom_decoratedtOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch407_end_70=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions);do {{if (!( tomMatch407_end_70.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch407_81= tomMatch407_end_70.getHeadconcTypeOption() ;if ( (tomMatch407_81 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch407_81) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch407_93= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch407_end_86=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);do {{if (!( tomMatch407_end_86.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch407_89= tomMatch407_end_86.getHeadconcTypeOption() ;if ( (tomMatch407_89 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch407_89) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch407_93= true ;}}}if ( tomMatch407_end_86.isEmptyconcTypeOption() ) {tomMatch407_end_86=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);} else {tomMatch407_end_86= tomMatch407_end_86.getTailconcTypeOption() ;}}} while(!( (tomMatch407_end_86==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) ));}if (!(tomMatch407_93)) {




 return true; }}}}if ( tomMatch407_end_70.isEmptyconcTypeOption() ) {tomMatch407_end_70=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions);} else {tomMatch407_end_70= tomMatch407_end_70.getTailconcTypeOption() ;}}} while(!( (tomMatch407_end_70==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) ));}}}}}}}}}if ( tomMatch407_end_64.isEmptyconcTypeConstraint() ) {tomMatch407_end_64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch407_end_64= tomMatch407_end_64.getTailconcTypeConstraint() ;}}} while(!( (tomMatch407_end_64==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch407_98= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch407_99= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch407_98 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_98) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch407_98.getTypeOptions() ;if ( (tomMatch407_99 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_99) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch407_end_111=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{if (!( tomMatch407_end_111.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch407_122= tomMatch407_end_111.getHeadconcTypeConstraint() ;if ( (tomMatch407_122 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch407_122) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch407_121= tomMatch407_122.getType2() ;if ( (tomMatch407_99== tomMatch407_122.getType1() ) ) {if ( (tomMatch407_121 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_121) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_decoratedtOptions= tomMatch407_121.getTypeOptions() ;if (  tomMatch407_98.getTomType() .equals( tomMatch407_121.getTomType() ) ) {if ( (tom_decoratedtOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch407_end_117=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions);do {{if (!( tomMatch407_end_117.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch407_128= tomMatch407_end_117.getHeadconcTypeOption() ;if ( (tomMatch407_128 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch407_128) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch407_140= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch407_end_133=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);do {{if (!( tomMatch407_end_133.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch407_136= tomMatch407_end_133.getHeadconcTypeOption() ;if ( (tomMatch407_136 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch407_136) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch407_140= true ;}}}if ( tomMatch407_end_133.isEmptyconcTypeOption() ) {tomMatch407_end_133=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);} else {tomMatch407_end_133= tomMatch407_end_133.getTailconcTypeOption() ;}}} while(!( (tomMatch407_end_133==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) ));}if (!(tomMatch407_140)) {




 return true; }}}}if ( tomMatch407_end_117.isEmptyconcTypeOption() ) {tomMatch407_end_117=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions);} else {tomMatch407_end_117= tomMatch407_end_117.getTailconcTypeOption() ;}}} while(!( (tomMatch407_end_117==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) ));}}}}}}}}}if ( tomMatch407_end_111.isEmptyconcTypeConstraint() ) {tomMatch407_end_111=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch407_end_111= tomMatch407_end_111.getTailconcTypeConstraint() ;}}} while(!( (tomMatch407_end_111==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch407_145= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch407_146= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch407_145 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_145) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions= tomMatch407_145.getTypeOptions() ;if ( (tomMatch407_146 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_146) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_tOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch407_end_158=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{if (!( tomMatch407_end_158.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch407_169= tomMatch407_end_158.getHeadconcTypeConstraint() ;if ( (tomMatch407_169 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch407_169) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch407_167= tomMatch407_169.getType1() ;if ( (tomMatch407_167 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch407_167) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_decoratedtOptions= tomMatch407_167.getTypeOptions() ;if (  tomMatch407_145.getTomType() .equals( tomMatch407_167.getTomType() ) ) {if ( (tomMatch407_146== tomMatch407_169.getType2() ) ) {if ( (tom_decoratedtOptions instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch407_end_164=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions);do {{if (!( tomMatch407_end_164.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch407_175= tomMatch407_end_164.getHeadconcTypeOption() ;if ( (tomMatch407_175 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch407_175) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {boolean tomMatch407_187= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch407_end_180=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);do {{if (!( tomMatch407_end_180.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch407_183= tomMatch407_end_180.getHeadconcTypeOption() ;if ( (tomMatch407_183 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch407_183) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch407_187= true ;}}}if ( tomMatch407_end_180.isEmptyconcTypeOption() ) {tomMatch407_end_180=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions);} else {tomMatch407_end_180= tomMatch407_end_180.getTailconcTypeOption() ;}}} while(!( (tomMatch407_end_180==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions)) ));}if (!(tomMatch407_187)) {





 return true; }}}}if ( tomMatch407_end_164.isEmptyconcTypeOption() ) {tomMatch407_end_164=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions);} else {tomMatch407_end_164= tomMatch407_end_164.getTailconcTypeOption() ;}}} while(!( (tomMatch407_end_164==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_decoratedtOptions)) ));}}}}}}}}}if ( tomMatch407_end_158.isEmptyconcTypeConstraint() ) {tomMatch407_end_158=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch407_end_158= tomMatch407_end_158.getTailconcTypeConstraint() ;}}} while(!( (tomMatch407_end_158==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}}}}}}}}}

    return false;
  }


  /*
   * pem: use if(...==... && typeConstraints.contains(...))
   */
  /**
   * The method <code>addConstraint</code> insert a constraint (an equation or
   * a subtype constraint) into a given type constraint list only if this
   * constraint does not yet exist into the list and if it does not contains
   * "EmptyTypes". 
   * @param tConstraint the constraint to be inserted into the type constraint list
   * @param tCList      the constraint type list where the constraint will be
   *                    inserted
   * @return            the list resulting of the insertion
   */
  protected TypeConstraintList addConstraint(TypeConstraint tConstraint, TypeConstraintList tCList) {
    TypeConstraint constraint = null;
    TypeConstraint symmetryConstraint = constraint;
    TypeOptionList emptyOptionList =  tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ;
    TargetLanguageType emptyTargetLanguageType =  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ;
    Info emptyInfo =  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tom.engine.adt.tomname.types.tomname.EmptyName.make() ,  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ;

    {{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tom_type1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tom_type2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_type1, tom_type2, emptyInfo) ;
        symmetryConstraint =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_type2, tom_type1, emptyInfo) ;
      }}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch408_6= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch408_7= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch408_6 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_6) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch408_6;if ( (tomMatch408_7 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_7) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_tType= tomMatch408_7.getTomType() ;

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_tVar,  tom.engine.adt.tomtype.types.tomtype.Type.make(emptyOptionList, tom_tType, emptyTargetLanguageType) , emptyInfo) ;
        symmetryConstraint =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( tom.engine.adt.tomtype.types.tomtype.Type.make(emptyOptionList, tom_tType, emptyTargetLanguageType) , tom_tVar, emptyInfo) ;
      }}}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch408_16= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch408_17= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch408_16 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_16) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_tType= tomMatch408_16.getTomType() ;if ( (tomMatch408_17 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_17) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch408_17;

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( tom.engine.adt.tomtype.types.tomtype.Type.make(emptyOptionList, tom_tType, emptyTargetLanguageType) , tom_tVar, emptyInfo) ;
        symmetryConstraint =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_tVar,  tom.engine.adt.tomtype.types.tomtype.Type.make(emptyOptionList, tom_tType, emptyTargetLanguageType) , emptyInfo) ;
      }}}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch408_26= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch408_27= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch408_26 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_26) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_ol1= tomMatch408_26.getTypeOptions() ; String  tom_tType1= tomMatch408_26.getTomType() ;if ( (tomMatch408_27 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_27) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_ol2= tomMatch408_27.getTypeOptions() ; String  tom_tType2= tomMatch408_27.getTomType() ;


        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( tom.engine.adt.tomtype.types.tomtype.Type.make(tom_ol1, tom_tType1, emptyTargetLanguageType) ,  tom.engine.adt.tomtype.types.tomtype.Type.make(tom_ol2, tom_tType2, emptyTargetLanguageType) , emptyInfo) 
;
        symmetryConstraint =  tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( tom.engine.adt.tomtype.types.tomtype.Type.make(tom_ol2, tom_tType2, emptyTargetLanguageType) ,  tom.engine.adt.tomtype.types.tomtype.Type.make(tom_ol1, tom_tType1, emptyTargetLanguageType) , emptyInfo) 
;
      }}}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {


        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() , emptyInfo) ;
        symmetryConstraint = constraint;
      }}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch408_45= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch408_45 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_45) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch408_45.getTypeOptions() ,  tomMatch408_45.getTomType() , emptyTargetLanguageType) , emptyInfo) ;
        symmetryConstraint = constraint;
      }}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch408_53= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ;if ( (tomMatch408_53 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_53) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch408_53.getTypeOptions() ,  tomMatch408_53.getTomType() , emptyTargetLanguageType) ,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() , emptyInfo) ;
        symmetryConstraint = constraint;
      }}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch408_62= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch408_63= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch408_62 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_62) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( (tomMatch408_63 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_63) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

        constraint         =  tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch408_62.getTypeOptions() ,  tomMatch408_62.getTomType() , emptyTargetLanguageType) ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tomMatch408_63.getTypeOptions() ,  tomMatch408_63.getTomType() , emptyTargetLanguageType) , emptyInfo) 
;
        symmetryConstraint = constraint;
      }}}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch408_81= false ; tom.engine.adt.tomtype.types.TomType  tomMatch408_76= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch408_78= null ; tom.engine.adt.tomtype.types.TomType  tomMatch408_75= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch408_79= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch408_81= true ;tomMatch408_78=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch408_75= tomMatch408_78.getType1() ;tomMatch408_76= tomMatch408_78.getType2() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch408_81= true ;tomMatch408_79=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch408_75= tomMatch408_79.getType1() ;tomMatch408_76= tomMatch408_79.getType2() ;}}}}}if (tomMatch408_81) {if ( (tomMatch408_75==tomMatch408_76) ) {


        // do not add tautology
        constraint         = null;
        symmetryConstraint = constraint;
      }}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch408_90= false ; tom.engine.adt.tomtype.types.TomType  tomMatch408_84= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch408_86= null ; tom.engine.adt.tomtype.types.TomType  tomMatch408_83= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch408_87= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch408_90= true ;tomMatch408_86=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch408_83= tomMatch408_86.getType1() ;tomMatch408_84= tomMatch408_86.getType2() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch408_90= true ;tomMatch408_87=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch408_83= tomMatch408_87.getType1() ;tomMatch408_84= tomMatch408_87.getType2() ;}}}}}if (tomMatch408_90) {if ( (tomMatch408_83 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_83) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {

        // do not add tautology
        constraint         = null;
        symmetryConstraint = constraint;
      }}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch408_99= false ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch408_96= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch408_95= null ; tom.engine.adt.tomtype.types.TomType  tomMatch408_93= null ; tom.engine.adt.tomtype.types.TomType  tomMatch408_92= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch408_99= true ;tomMatch408_95=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch408_92= tomMatch408_95.getType1() ;tomMatch408_93= tomMatch408_95.getType2() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch408_99= true ;tomMatch408_96=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch408_92= tomMatch408_96.getType1() ;tomMatch408_93= tomMatch408_96.getType2() ;}}}}}if (tomMatch408_99) {if ( (tomMatch408_93 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch408_93) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {

        // do not add tautology
        constraint         = null;
        symmetryConstraint = constraint;
      }}}}}}



    if(constraint == null) {
      //System.out.println("tConstraint = " + tConstraint);
      //System.out.println("constraint = null");
    }


    TypeConstraintList result1 = null;
    TypeConstraintList result2 = null;

    
    if(constraint == null || constraintBag.contains(constraint)) {
      result1 = tCList;
    } else {
      constraintBag.add(constraint);
      constraintBag.add(symmetryConstraint);
      result1 =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
    }
   
    //----------------
    /*
    result2 = tCList;
    if (!containsConstraint(tConstraint,tCList)) {
      %match(tConstraint) {
        (Equation|Subtype)[Type1=t1@!EmptyType(),Type2=t2@!EmptyType()] && (t1 != t2) -> { 
            result2 = `concTypeConstraint(tConstraint,tCList*);
          }
      }
    }

    if(result1 != result2) {
      System.out.println("***");
      System.out.println("tConstraint = " + tConstraint);
      System.out.println("constraint = " + constraint);
      System.out.println("tCList = " + tCList);
      System.out.println("bag = " + constraintBag);
      System.out.println("result1 = " + result1);
      System.out.println("result2 = " + result2);
    }
    */
    //----------------

    return result1;
     
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
    for(TomType currentType:symbolTable.getUsedTypes()) {
      String currentTypeName = currentType.getTomType();
      TomTypeList superTypes =  tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
      {{if ( (currentType instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )currentType) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )currentType)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch409_1= (( tom.engine.adt.tomtype.types.TomType )currentType).getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch409_1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch409_1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch409_end_8=tomMatch409_1;do {{if (!( tomMatch409_end_8.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch409_12= tomMatch409_end_8.getHeadconcTypeOption() ;if ( (tomMatch409_12 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch409_12) instanceof tom.engine.adt.tomtype.types.typeoption.SubtypeDecl) ) { String  tom_supTypeName= tomMatch409_12.getTomType() ;


          //DEBUG System.out.println("In generateDependencies -- match : supTypeName = "
          //DEBUG     + `supTypeName + " and supType = " + supType);
          if(dependencies.containsKey(tom_supTypeName)) {
            superTypes = dependencies.get(tom_supTypeName); 
          }
          TomType supType = symbolTable.getType(tom_supTypeName);
          if(supType != null) {
            superTypes =  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(supType,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )) ;  

            /* STEP 2 */
            for(String subType:dependencies.keySet()) {
              TomTypeList supOfSubTypes = dependencies.get(subType);
              //DEBUG System.out.println("In generateDependencies -- for 2: supOfSubTypes = " +
              //DEBUG     supOfSubTypes);
              {{if ( (supOfSubTypes instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes)) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes)) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch410_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes);do {{if (!( tomMatch410_end_4.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch410_8= tomMatch410_end_4.getHeadconcTomType() ;if ( (tomMatch410_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch410_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if (  (( tom.engine.adt.tomtype.types.TomType )currentType).getTomType() .equals( tomMatch410_8.getTomType() ) ) {


                    /* 
                     * Replace list of superTypes of "subType" by a new one
                     * containing the superTypes of "currentType" which is also a
                     * superType 
                     */
                    dependencies.put(subType,tom_append_list_concTomType(supOfSubTypes,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )));
                  }}}}if ( tomMatch410_end_4.isEmptyconcTomType() ) {tomMatch410_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes);} else {tomMatch410_end_4= tomMatch410_end_4.getTailconcTomType() ;}}} while(!( (tomMatch410_end_4==(( tom.engine.adt.tomtype.types.TomTypeList )supOfSubTypes)) ));}}}}

            }
          } else {
            TomMessage.error(logger,getCurrentInputFileName(),0,
                TomMessage.typetermNotDefined,tom_supTypeName);
          }
        }}}if ( tomMatch409_end_8.isEmptyconcTypeOption() ) {tomMatch409_end_8=tomMatch409_1;} else {tomMatch409_end_8= tomMatch409_end_8.getTailconcTypeOption() ;}}} while(!( (tomMatch409_end_8==tomMatch409_1) ));}}}}}}

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
   * a BQTerm which is in the local context (see page 137 of Claudia's thesis) but is not 
   * in the global context, i.e. <code>varPatternList</code>\<code>globalVarPatternList</code>.
   * Then, this BQTerm is removed from <code>varList</code>.
   * e.g. :
   * gC = global context / lC = local context
   * Suppose the following match block
   *   %match {					// l1
   *     f(x) << v1 && (x == a()) -> { `x; }	// l2
   *     x << v2 && (x != b() -> { `x; }	// l3
   *   }
   * where A := a() and B := b()
   *  		gC	    |	lC
   * --------------------------------
   * l1		{}	    |	{}
   * l2		{v1}	  |	{x}
   * l3		{v1,v2}	|	{x}
   * But the "x" of lC at l2 is not the same of that of lC at l3. Although the
   * "x" occurring in the numeric condition of l2 is in varList, it must be
   * removed to avoid variable capture when the condition of l3 is considered.
   * Thus, instead of performing variable renaming, we assume that pattern
   * variables are only "valid" in the current rule cond -> action.
   * pem: remove from varList, all variables that belong to varPatternList, which are not in globalVarPatternList
   * @param globalVarPatternList   the TomList to be reset
   */
  protected void resetVarList(TomList globalVarPatternList) {
    for(TomTerm tTerm: varPatternList.getCollectionconcTomTerm()) {
      {{if ( (tTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch411_27= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch411_6= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch411_5= null ; tom.engine.adt.tomname.types.TomName  tomMatch411_3= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch411_27= true ;tomMatch411_5=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch411_3= tomMatch411_5.getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch411_27= true ;tomMatch411_6=(( tom.engine.adt.tomterm.types.TomTerm )tTerm);tomMatch411_3= tomMatch411_6.getAstName() ;}}}}}if (tomMatch411_27) {if ( (globalVarPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )varList)) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )varList)) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch411_end_10=(( tom.engine.adt.code.types.BQTermList )varList);do {{if (!( tomMatch411_end_10.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch411_14= tomMatch411_end_10.getHeadconcBQTerm() ;boolean tomMatch411_26= false ; tom.engine.adt.code.types.BQTerm  tomMatch411_16= null ; tom.engine.adt.tomname.types.TomName  tomMatch411_13= null ; tom.engine.adt.code.types.BQTerm  tomMatch411_15= null ;if ( (tomMatch411_14 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch411_14) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch411_26= true ;tomMatch411_15=tomMatch411_14;tomMatch411_13= tomMatch411_15.getAstName() ;}} else {if ( (tomMatch411_14 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch411_14) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch411_26= true ;tomMatch411_16=tomMatch411_14;tomMatch411_13= tomMatch411_16.getAstName() ;}}}}}if (tomMatch411_26) {if ( (tomMatch411_3==tomMatch411_13) ) {boolean tomMatch411_25= false ;if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )globalVarPatternList)) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )globalVarPatternList)) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch411_end_20=(( tom.engine.adt.tomterm.types.TomList )globalVarPatternList);do {{if (!( tomMatch411_end_20.isEmptyconcTomTerm() )) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tTerm)== tomMatch411_end_20.getHeadconcTomTerm() ) ) {tomMatch411_25= true ;}}if ( tomMatch411_end_20.isEmptyconcTomTerm() ) {tomMatch411_end_20=(( tom.engine.adt.tomterm.types.TomList )globalVarPatternList);} else {tomMatch411_end_20= tomMatch411_end_20.getTailconcTomTerm() ;}}} while(!( (tomMatch411_end_20==(( tom.engine.adt.tomterm.types.TomList )globalVarPatternList)) ));}if (!(tomMatch411_25)) {


            //System.out.println("*** resetVarList remove: " + `aName);
            varList = tom_append_list_concBQTerm(tom_get_slice_concBQTerm((( tom.engine.adt.code.types.BQTermList )varList),tomMatch411_end_10, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ),tom_append_list_concBQTerm( tomMatch411_end_10.getTailconcBQTerm() , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ));
          }}}}if ( tomMatch411_end_10.isEmptyconcBQTerm() ) {tomMatch411_end_10=(( tom.engine.adt.code.types.BQTermList )varList);} else {tomMatch411_end_10= tomMatch411_end_10.getTailconcBQTerm() ;}}} while(!( (tomMatch411_end_10==(( tom.engine.adt.code.types.BQTermList )varList)) ));}}}}}}}

    }
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
    constraintBag = new HashSet<TypeConstraint>();
    substitutions = new Substitution();
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
  public static class CollectKnownTypes extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public CollectKnownTypes( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )tom__arg)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch412_2= (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTlType() ; String  tom_tomType= (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() ;if ( (tomMatch412_2 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch412_2) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {


        TomType newType = nkt.symbolTable.getType(tom_tomType);
        if(newType == null) {
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
      return tom_make_TopDownStopOnSuccess(tom_make_inferTypes(contextType,this)).visitLight(term); 
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
  public static class inferTypes extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomtype.types.TomType  contextType;private  NewKernelTyper  nkt;public inferTypes( tom.engine.adt.tomtype.types.TomType  contextType,  NewKernelTyper  nkt) {super(( new tom.library.sl.Fail() ));this.contextType=contextType;this.nkt=nkt;}public  tom.engine.adt.tomtype.types.TomType  getcontextType() {return contextType;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {return ((T)visit_TomVisit((( tom.engine.adt.tomsignature.types.TomVisit )v),introspector));}if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.Code) ) {return ((T)visit_Code((( tom.engine.adt.code.types.Code )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.Code  _visit_Code( tom.engine.adt.code.types.Code  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.Code )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  _visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomsignature.types.TomVisit )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.code.types.BQTerm  tom_bqVar=(( tom.engine.adt.code.types.BQTerm )tom__arg);
















































































































        nkt.checkNonLinearityOfBQVariables(tom_bqVar);
        nkt.subtypeConstraints = nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() , contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ,  (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ) ) ,nkt.subtypeConstraints);  
        return tom_bqVar;
      }}}}{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.code.types.BQTerm  tom_bqVarStar=(( tom.engine.adt.code.types.BQTerm )tom__arg);


        nkt.checkNonLinearityOfBQVariables(tom_bqVarStar);
        nkt.equationConstraints =
          nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstType() , contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ,  (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ) ) ,nkt.equationConstraints);
        return tom_bqVarStar;
      }}}}{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) { tom.engine.adt.tomname.types.TomName  tomMatch413_14= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ; tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.code.types.BQTerm )tom__arg).getOptions() ;if ( (tomMatch413_14 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch413_14) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch413_14; tom.engine.adt.code.types.BQTermList  tom_bqTList= (( tom.engine.adt.code.types.BQTerm )tom__arg).getArgs() ;


        TomSymbol tSymbol = nkt.getSymbolFromName( tomMatch413_14.getString() );
        TomType codomain = contextType;
        if (tSymbol == null) {
          tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;
          //DEBUG System.out.println("name = " + `name);
          //DEBUG System.out.println("context = " + contextType);
          BQTermList newBQTList = nkt.inferBQTermList(tom_bqTList, tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ,contextType);
          /* PEM: why contextType ? */
          return  tom.engine.adt.code.types.bqterm.FunctionCall.make(tom_aName, contextType, newBQTList) ; 
        } else {
          {{if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol)) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch414_2= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;if ( (tomMatch414_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch414_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch414_8= tomMatch414_2.getCodomain() ;if ( (tomMatch414_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch414_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 
              codomain = tomMatch414_8;
              if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) {
                /* Apply decoration for types of list operators */
                TypeOptionList newTOptions =  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom_symName) ,tom_append_list_concTypeOption( tomMatch414_8.getTypeOptions() , tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;
                codomain =  tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch414_8.getTomType() ,  tomMatch414_8.getTlType() ) ;
                tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_symName,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tomMatch414_2.getDomain() , codomain) ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getPairNameDeclList() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getOptions() ) ; 
              }  
            }}}}}}}}}

          nkt.subtypeConstraints = nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,nkt.subtypeConstraints);

          BQTermList newBQTList = nkt.inferBQTermList(tom_bqTList,tSymbol,contextType);
          return  tom.engine.adt.code.types.bqterm.BQAppl.make(tom_optionList, tom_aName, newBQTList) ;
        }
      }}}}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ; tom.engine.adt.tomterm.types.TomTerm  tom_var=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);         nkt.checkNonLinearityOfVariables(tom_var);         nkt.subtypeConstraints =           nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_aType, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ) ) ,nkt.subtypeConstraints);           {{if ( (tom_cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList)) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList)) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch416_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getHeadconcConstraint() ;if ( (tomMatch416_4 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch416_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch416_4.getVar() ;if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getTailconcConstraint() .isEmptyconcConstraint() ) {             nkt.equationConstraints =               nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), tom_aType, nkt.getInfoFromTomTerm(tom_boundTerm)) ,nkt.equationConstraints);            }}}}}}}}         return tom_var.setConstraints(tom_cList);       }}}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ; tom.engine.adt.tomterm.types.TomTerm  tom_varStar=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);         nkt.checkNonLinearityOfVariables(tom_varStar);         nkt.equationConstraints =           nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ) ) ,nkt.equationConstraints);           {{if ( (tom_cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList)) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList)) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch417_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getHeadconcConstraint() ;if ( (tomMatch417_4 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch417_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch417_4.getVar() ;if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getTailconcConstraint() .isEmptyconcConstraint() ) {             nkt.equationConstraints =               nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), tom_aType, nkt.getInfoFromTomTerm(tom_boundTerm)) ,nkt.equationConstraints);            }}}}}}}}         return tom_varStar.setConstraints(tom_cList);       }}}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch415_16= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOptions() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch415_16) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch415_16) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch415_16.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch415_25= tomMatch415_16.getHeadconcTomName() ;if ( (tomMatch415_25 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch415_25) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomslot.types.SlotList  tom_sList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getSlots() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;         /* In case of a String, tomName is "" for ("a","b") */         TomSymbol tSymbol = nkt.getSymbolFromName( tomMatch415_25.getString() );         TomType codomain = contextType;          /* IF_3 */         if(tSymbol == null) {           tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;         } else {           /* This code cannot be moved to IF_2 because tSymbol may don't be            "null" since the begginning and then does not enter into neither IF_1 nor */            /* IF_2 */           {{if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol)) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch418_2= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;if ( (tomMatch418_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch418_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch418_8= tomMatch418_2.getCodomain() ;if ( (tomMatch418_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch418_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {                codomain = tomMatch418_8;               if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) {                 /* Apply decoration for types of list operators */                 TypeOptionList newTOptions =  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom_symName) ,tom_append_list_concTypeOption( tomMatch418_8.getTypeOptions() , tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;                 codomain =  tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch418_8.getTomType() ,  tomMatch418_8.getTlType() ) ;                 tSymbol =  tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_symName,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tomMatch418_2.getDomain() , codomain) ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getPairNameDeclList() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getOptions() ) ;                }               }}}}}}}}}           nkt.subtypeConstraints = nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch415_16.getHeadconcTomName() , tom_optionList) ) ,nkt.subtypeConstraints);         }          {{if ( (tom_cList instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList)) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList)) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).isEmptyconcConstraint() )) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch419_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getHeadconcConstraint() ;if ( (tomMatch419_4 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch419_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) { tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch419_4.getVar() ;if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )tom_cList).getTailconcConstraint() .isEmptyconcConstraint() ) {             nkt.equationConstraints =               nkt.addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), codomain, nkt.getInfoFromTomTerm(tom_boundTerm)) ,nkt.equationConstraints);            }}}}}}}}          SlotList newSList =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;         if(!tom_sList.isEmptyconcSlot()) {           newSList= nkt.inferSlotList(tom_sList,tSymbol,contextType);         }         return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_optionList, tomMatch415_16, newSList, tom_cList) ;       }}}}}}}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TomVisit  visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tom__arg) instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )(( tom.engine.adt.tomsignature.types.TomVisit )tom__arg)) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) {         BQTermList BQTList = nkt.varList;         ConstraintInstructionList newCIList = nkt.inferConstraintInstructionList( (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getAstConstraintInstructionList() );         nkt.varList = BQTList;         return  tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make( (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getVNode() , newCIList,  (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getOptions() ) ;       }}}}}return _visit_TomVisit(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )tom__arg)) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {         BQTermList BQTList = nkt.varList;         ConstraintInstructionList newCIList = nkt.inferConstraintInstructionList( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() );         nkt.varList = BQTList;         return  tom.engine.adt.tominstruction.types.instruction.Match.make(newCIList,  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOptions() ) ;       }}}}}return _visit_Instruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.Code  visit_Code( tom.engine.adt.code.types.Code  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.Code) ) {boolean tomMatch422_5= false ; tom.engine.adt.code.types.Code  tomMatch422_3= null ; tom.engine.adt.code.types.CodeList  tomMatch422_1= null ; tom.engine.adt.code.types.Code  tomMatch422_4= null ;if ( ((( tom.engine.adt.code.types.Code )tom__arg) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )tom__arg)) instanceof tom.engine.adt.code.types.code.Tom) ) {{tomMatch422_5= true ;tomMatch422_3=(( tom.engine.adt.code.types.Code )tom__arg);tomMatch422_1= tomMatch422_3.getCodeList() ;}} else {if ( ((( tom.engine.adt.code.types.Code )tom__arg) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )tom__arg)) instanceof tom.engine.adt.code.types.code.TomInclude) ) {{tomMatch422_5= true ;tomMatch422_4=(( tom.engine.adt.code.types.Code )tom__arg);tomMatch422_1= tomMatch422_4.getCodeList() ;}}}}}if (tomMatch422_5) {         nkt.generateDependencies();         CodeList newCList = nkt.inferCodeList(tomMatch422_1);         return (( tom.engine.adt.code.types.Code )tom__arg).setCodeList(newCList);       }}}}return _visit_Code(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_inferTypes( tom.engine.adt.tomtype.types.TomType  t0,  NewKernelTyper  t1) { return new inferTypes(t0,t1);}



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
    {{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch423_41= false ; tom.engine.adt.tomtype.types.TomType  tomMatch423_5= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch423_7= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch423_3= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch423_8= null ; tom.engine.adt.tomname.types.TomName  tomMatch423_4= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch423_41= true ;tomMatch423_7=(( tom.engine.adt.tomterm.types.TomTerm )var);tomMatch423_3= tomMatch423_7.getOptions() ;tomMatch423_4= tomMatch423_7.getAstName() ;tomMatch423_5= tomMatch423_7.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch423_41= true ;tomMatch423_8=(( tom.engine.adt.tomterm.types.TomTerm )var);tomMatch423_3= tomMatch423_8.getOptions() ;tomMatch423_4= tomMatch423_8.getAstName() ;tomMatch423_5= tomMatch423_8.getAstType() ;}}}}}if (tomMatch423_41) { tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch423_3; tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch423_4; tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch423_5;if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )varPatternList)) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )varPatternList)) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch423_end_12=(( tom.engine.adt.tomterm.types.TomList )varPatternList);do {{if (!( tomMatch423_end_12.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch423_23= tomMatch423_end_12.getHeadconcTomTerm() ;boolean tomMatch423_38= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch423_24= null ; tom.engine.adt.tomname.types.TomName  tomMatch423_21= null ; tom.engine.adt.tomtype.types.TomType  tomMatch423_22= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch423_25= null ;if ( (tomMatch423_23 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch423_23) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch423_38= true ;tomMatch423_24=tomMatch423_23;tomMatch423_21= tomMatch423_24.getAstName() ;tomMatch423_22= tomMatch423_24.getAstType() ;}} else {if ( (tomMatch423_23 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch423_23) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch423_38= true ;tomMatch423_25=tomMatch423_23;tomMatch423_21= tomMatch423_25.getAstName() ;tomMatch423_22= tomMatch423_25.getAstType() ;}}}}}if (tomMatch423_38) {if ( (tom_aName==tomMatch423_21) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch423_22;boolean tomMatch423_37= false ;if ( (tom_aType1==tomMatch423_22) ) {if ( (tom_aType2==tomMatch423_22) ) {tomMatch423_37= true ;}}if (!(tomMatch423_37)) {






          equationConstraints = addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,equationConstraints);
        }}}}if ( tomMatch423_end_12.isEmptyconcTomTerm() ) {tomMatch423_end_12=(( tom.engine.adt.tomterm.types.TomList )varPatternList);} else {tomMatch423_end_12= tomMatch423_end_12.getTailconcTomTerm() ;}}} while(!( (tomMatch423_end_12==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));}}if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )varList)) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )varList)) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch423_end_18=(( tom.engine.adt.code.types.BQTermList )varList);do {{if (!( tomMatch423_end_18.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch423_28= tomMatch423_end_18.getHeadconcBQTerm() ;boolean tomMatch423_40= false ; tom.engine.adt.code.types.BQTerm  tomMatch423_30= null ; tom.engine.adt.code.types.BQTerm  tomMatch423_29= null ; tom.engine.adt.tomname.types.TomName  tomMatch423_26= null ; tom.engine.adt.tomtype.types.TomType  tomMatch423_27= null ;if ( (tomMatch423_28 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch423_28) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch423_40= true ;tomMatch423_29=tomMatch423_28;tomMatch423_26= tomMatch423_29.getAstName() ;tomMatch423_27= tomMatch423_29.getAstType() ;}} else {if ( (tomMatch423_28 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch423_28) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch423_40= true ;tomMatch423_30=tomMatch423_28;tomMatch423_26= tomMatch423_30.getAstName() ;tomMatch423_27= tomMatch423_30.getAstType() ;}}}}}if (tomMatch423_40) {if ( (tom_aName==tomMatch423_26) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch423_27;boolean tomMatch423_39= false ;if ( (tom_aType1==tomMatch423_27) ) {if ( (tom_aType2==tomMatch423_27) ) {tomMatch423_39= true ;}}if (!(tomMatch423_39)) {           equationConstraints = addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,equationConstraints);         }}}}if ( tomMatch423_end_18.isEmptyconcBQTerm() ) {tomMatch423_end_18=(( tom.engine.adt.code.types.BQTermList )varList);} else {tomMatch423_end_18= tomMatch423_end_18.getTailconcBQTerm() ;}}} while(!( (tomMatch423_end_18==(( tom.engine.adt.code.types.BQTermList )varList)) ));}}}}}{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch423_63= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch423_48= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch423_47= null ; tom.engine.adt.tomname.types.TomName  tomMatch423_44= null ; tom.engine.adt.tomtype.types.TomType  tomMatch423_45= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch423_63= true ;tomMatch423_47=(( tom.engine.adt.tomterm.types.TomTerm )var);tomMatch423_44= tomMatch423_47.getAstName() ;tomMatch423_45= tomMatch423_47.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch423_63= true ;tomMatch423_48=(( tom.engine.adt.tomterm.types.TomTerm )var);tomMatch423_44= tomMatch423_48.getAstName() ;tomMatch423_45= tomMatch423_48.getAstType() ;}}}}}if (tomMatch423_63) { tom.engine.adt.tomtype.types.TomType  tom_aType=tomMatch423_45;if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )varPatternList)) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )varPatternList)) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch423_end_52=(( tom.engine.adt.tomterm.types.TomList )varPatternList);do {{if (!( tomMatch423_end_52.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch423_57= tomMatch423_end_52.getHeadconcTomTerm() ;boolean tomMatch423_62= false ; tom.engine.adt.tomname.types.TomName  tomMatch423_55= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch423_58= null ; tom.engine.adt.tomtype.types.TomType  tomMatch423_56= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch423_59= null ;if ( (tomMatch423_57 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch423_57) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch423_62= true ;tomMatch423_58=tomMatch423_57;tomMatch423_55= tomMatch423_58.getAstName() ;tomMatch423_56= tomMatch423_58.getAstType() ;}} else {if ( (tomMatch423_57 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch423_57) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch423_62= true ;tomMatch423_59=tomMatch423_57;tomMatch423_55= tomMatch423_59.getAstName() ;tomMatch423_56= tomMatch423_59.getAstType() ;}}}}}if (tomMatch423_62) {if ( (tomMatch423_44==tomMatch423_55) ) {if ( (tom_aType==tomMatch423_56) ) {



          //DEBUG System.out.println("Add type '" + `aType + "' of var '" + `var +"'\n");
          addPositiveTVar(tom_aType);
        }}}}if ( tomMatch423_end_52.isEmptyconcTomTerm() ) {tomMatch423_end_52=(( tom.engine.adt.tomterm.types.TomList )varPatternList);} else {tomMatch423_end_52= tomMatch423_end_52.getTailconcTomTerm() ;}}} while(!( (tomMatch423_end_52==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));}}}}}}

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
    {{if ( (bqvar instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch424_41= false ; tom.engine.adt.code.types.BQTerm  tomMatch424_7= null ; tom.engine.adt.code.types.BQTerm  tomMatch424_8= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch424_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch424_4= null ; tom.engine.adt.tomtype.types.TomType  tomMatch424_5= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqvar) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqvar)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch424_41= true ;tomMatch424_7=(( tom.engine.adt.code.types.BQTerm )bqvar);tomMatch424_3= tomMatch424_7.getOptions() ;tomMatch424_4= tomMatch424_7.getAstName() ;tomMatch424_5= tomMatch424_7.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqvar) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqvar)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch424_41= true ;tomMatch424_8=(( tom.engine.adt.code.types.BQTerm )bqvar);tomMatch424_3= tomMatch424_8.getOptions() ;tomMatch424_4= tomMatch424_8.getAstName() ;tomMatch424_5= tomMatch424_8.getAstType() ;}}}}}if (tomMatch424_41) { tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch424_3; tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch424_4; tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch424_5;if ( (varList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )varList)) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )varList)) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch424_end_12=(( tom.engine.adt.code.types.BQTermList )varList);do {{if (!( tomMatch424_end_12.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch424_23= tomMatch424_end_12.getHeadconcBQTerm() ;boolean tomMatch424_38= false ; tom.engine.adt.code.types.BQTerm  tomMatch424_25= null ; tom.engine.adt.code.types.BQTerm  tomMatch424_24= null ; tom.engine.adt.tomtype.types.TomType  tomMatch424_22= null ; tom.engine.adt.tomname.types.TomName  tomMatch424_21= null ;if ( (tomMatch424_23 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch424_23) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch424_38= true ;tomMatch424_24=tomMatch424_23;tomMatch424_21= tomMatch424_24.getAstName() ;tomMatch424_22= tomMatch424_24.getAstType() ;}} else {if ( (tomMatch424_23 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch424_23) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch424_38= true ;tomMatch424_25=tomMatch424_23;tomMatch424_21= tomMatch424_25.getAstName() ;tomMatch424_22= tomMatch424_25.getAstType() ;}}}}}if (tomMatch424_38) {if ( (tom_aName==tomMatch424_21) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch424_22;boolean tomMatch424_37= false ;if ( (tom_aType1==tomMatch424_22) ) {if ( (tom_aType2==tomMatch424_22) ) {tomMatch424_37= true ;}}if (!(tomMatch424_37)) {








          equationConstraints =
            addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,equationConstraints); 
        }}}}if ( tomMatch424_end_12.isEmptyconcBQTerm() ) {tomMatch424_end_12=(( tom.engine.adt.code.types.BQTermList )varList);} else {tomMatch424_end_12= tomMatch424_end_12.getTailconcBQTerm() ;}}} while(!( (tomMatch424_end_12==(( tom.engine.adt.code.types.BQTermList )varList)) ));}}if ( (varPatternList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )varPatternList)) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )varPatternList)) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch424_end_18=(( tom.engine.adt.tomterm.types.TomList )varPatternList);do {{if (!( tomMatch424_end_18.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch424_28= tomMatch424_end_18.getHeadconcTomTerm() ;boolean tomMatch424_40= false ; tom.engine.adt.tomtype.types.TomType  tomMatch424_27= null ; tom.engine.adt.tomname.types.TomName  tomMatch424_26= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch424_30= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch424_29= null ;if ( (tomMatch424_28 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch424_28) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch424_40= true ;tomMatch424_29=tomMatch424_28;tomMatch424_26= tomMatch424_29.getAstName() ;tomMatch424_27= tomMatch424_29.getAstType() ;}} else {if ( (tomMatch424_28 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch424_28) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch424_40= true ;tomMatch424_30=tomMatch424_28;tomMatch424_26= tomMatch424_30.getAstName() ;tomMatch424_27= tomMatch424_30.getAstType() ;}}}}}if (tomMatch424_40) {if ( (tom_aName==tomMatch424_26) ) { tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch424_27;boolean tomMatch424_39= false ;if ( (tom_aType1==tomMatch424_27) ) {if ( (tom_aType2==tomMatch424_27) ) {tomMatch424_39= true ;}}if (!(tomMatch424_39)) {           equationConstraints =             addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,equationConstraints);          }}}}if ( tomMatch424_end_18.isEmptyconcTomTerm() ) {tomMatch424_end_18=(( tom.engine.adt.tomterm.types.TomList )varPatternList);} else {tomMatch424_end_18= tomMatch424_end_18.getTailconcTomTerm() ;}}} while(!( (tomMatch424_end_18==(( tom.engine.adt.tomterm.types.TomList )varPatternList)) ));}}}}}}

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
    for(ConstraintInstruction cInst:ciList.getCollectionconcConstraintInstruction()) {
      try {
        {{if ( (cInst instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst) instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )(( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst)) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) { tom.engine.adt.tomconstraint.types.Constraint  tom_constraint= (( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst).getConstraint() ;

            // Store variable lists in new variables and reinitialize them
            BQTermList globalVarList = varList;
            TomList globalVarPatternList = varPatternList;

            tom_make_TopDownCollect(tom_make_CollectVars(this)).visitLight(tom_constraint);

            Constraint newConstraint = inferConstraint(tom_constraint);
            //DEBUG System.out.println("inferConstraintInstructionList: action " +
            //DEBUG     `action);
            Instruction newAction = inferAllTypes( (( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst).getAction() , tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );

            resetVarList(globalVarPatternList);
            varPatternList = globalVarPatternList;
            newCIList =
               tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(newConstraint, newAction,  (( tom.engine.adt.tominstruction.types.ConstraintInstruction )cInst).getOptions() ) ,tom_append_list_concConstraintInstruction(newCIList, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;
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
  public static class CollectVars extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public CollectVars( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch426_4= false ; tom.engine.adt.code.types.BQTerm  tomMatch426_2= null ; tom.engine.adt.code.types.BQTerm  tomMatch426_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch426_4= true ;tomMatch426_2=(( tom.engine.adt.code.types.BQTerm )tom__arg);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )tom__arg)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch426_4= true ;tomMatch426_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);}}}}}if (tomMatch426_4) {







 
        nkt.addBQTerm((( tom.engine.adt.code.types.BQTerm )tom__arg));
      }}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch427_4= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch427_2= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch427_3= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch427_4= true ;tomMatch427_2=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )tom__arg)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch427_4= true ;tomMatch427_3=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);}}}}}if (tomMatch427_4) {          nkt.addTomTerm((( tom.engine.adt.tomterm.types.TomTerm )tom__arg));       }}}}return _visit_TomTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectVars( NewKernelTyper  t0) { return new CollectVars(t0);}



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
    {{if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_pattern= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getPattern() ; tom.engine.adt.code.types.BQTerm  tom_subject= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getSubject() ; tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getAstType() ;
 
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
        {{if ( (tom_aType instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch429_4= false ; tom.engine.adt.tomtype.types.TomType  tomMatch429_3= null ; tom.engine.adt.tomtype.types.TomType  tomMatch429_2= null ;if ( ((( tom.engine.adt.tomtype.types.TomType )tom_aType) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )tom_aType)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch429_4= true ;tomMatch429_2=(( tom.engine.adt.tomtype.types.TomType )tom_aType);}} else {if ( ((( tom.engine.adt.tomtype.types.TomType )tom_aType) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )tom_aType)) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {{tomMatch429_4= true ;tomMatch429_3=(( tom.engine.adt.tomtype.types.TomType )tom_aType);}}}}}if (tomMatch429_4) {

            /* T_pattern = T_cast and T_cast <: T_subject */
            equationConstraints =
              addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tPattern, tom_aType, getInfoFromTomTerm(tom_pattern)) ,equationConstraints);
            subtypeConstraints = addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tPattern, tSubject, getInfoFromBQTerm(tom_subject)) ,subtypeConstraints);
          }}}}

        TomTerm newPattern = inferAllTypes(tom_pattern,tPattern);
        BQTerm newSubject = inferAllTypes(tom_subject,tSubject);
        return  tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(newPattern, newSubject, tom_aType) ;
      }}}}{if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) { tom.engine.adt.code.types.BQTerm  tom_left= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getLeft() ; tom.engine.adt.code.types.BQTerm  tom_right= (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getRight() ;


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

        // To represent the relationship between both argument types
        TomType lowerType = getUnknownFreshTypeVar();
        subtypeConstraints =
          addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tLeft, getInfoFromBQTerm(tom_left)) ,subtypeConstraints);
        subtypeConstraints =
          addConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tRight, getInfoFromBQTerm(tom_right)) ,subtypeConstraints);
        BQTerm newLeft = inferAllTypes(tom_left,tLeft);
        BQTerm newRight = inferAllTypes(tom_right,tRight);
        return  tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(newLeft, newRight,  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getType() ) ;
      }}}}{if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyAndConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {


        ConstraintList cList =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadAndConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
        Constraint newAConstraint =  tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;
        for (Constraint cArg : cList.getCollectionconcConstraint()) {
          cArg = inferConstraint(cArg);
          newAConstraint =  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(newAConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ;
        }
        return newAConstraint;
      }}}}{if ( (constraint instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )constraint).isEmptyOrConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )constraint)== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) {


        ConstraintList cList =  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getHeadOrConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )constraint))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )constraint) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )constraint).getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
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
    {{if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol)) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

        TomName argName;
        TomTerm argTerm;
        TomSymbol argSymb;
        for (Slot slot : sList.getCollectionconcSlot()) {
          argName = slot.getSlotName();
          argTerm = slot.getAppl();
          argSymb = getSymbolFromTerm(argTerm);
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) {
            {{if ( (argTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch431_3= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )argTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )argTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch431_3= true ;}}if (!(tomMatch431_3)) {
 
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
      }}}}{if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol)) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch430_5= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;if ( (tomMatch430_5 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch430_5) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch430_8= tomMatch430_5.getDomain() ; tom.engine.adt.tomtype.types.TomType  tomMatch430_9= tomMatch430_5.getCodomain() ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch430_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch430_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch430_8.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch430_8.getHeadconcTomType() ;if ( (tomMatch430_9 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch430_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch430_12= tomMatch430_9.getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch430_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch430_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch430_end_21=tomMatch430_12;do {{if (!( tomMatch430_end_21.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch430_24= tomMatch430_end_21.getHeadconcTypeOption() ;if ( (tomMatch430_24 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch430_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {



        TomTerm argTerm;
        TomSymbol argSymb;
        for (Slot slot : sList.getCollectionconcSlot()) {
          argTerm = slot.getAppl();
          argSymb = getSymbolFromTerm(argTerm);
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) {
            {{if ( (argTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )argTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )argTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {

                /* Case CT-STAR rule (applying to premises) */
                argType = tomMatch430_9;
              }}}}{if ( (argTerm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch432_6= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )argTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )argTerm)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch432_6= true ;}}if (!(tomMatch432_6)) {
 
                //DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
                /* Case CT-ELEM rule (applying to premises which are not lists) */
                argType = tom_headTTList;
              }}}}

          } else if ( (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() != argSymb.getAstName()) {
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
      }}}if ( tomMatch430_end_21.isEmptyconcTypeOption() ) {tomMatch430_end_21=tomMatch430_12;} else {tomMatch430_end_21= tomMatch430_end_21.getTailconcTypeOption() ;}}} while(!( (tomMatch430_end_21==tomMatch430_12) ));}}}}}}}}}}}{if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol)) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch430_27= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;if ( (tomMatch430_27 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch430_27) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch430_30= tomMatch430_27.getCodomain() ;if ( (tomMatch430_30 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch430_30) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch430_33= tomMatch430_30.getTypeOptions() ;boolean tomMatch430_44= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch430_33) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch430_33) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch430_end_39=tomMatch430_33;do {{if (!( tomMatch430_end_39.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch430_42= tomMatch430_end_39.getHeadconcTypeOption() ;if ( (tomMatch430_42 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch430_42) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch430_44= true ;}}}if ( tomMatch430_end_39.isEmptyconcTypeOption() ) {tomMatch430_end_39=tomMatch430_33;} else {tomMatch430_end_39= tomMatch430_end_39.getTailconcTypeOption() ;}}} while(!( (tomMatch430_end_39==tomMatch430_33) ));}if (!(tomMatch430_44)) {



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

    {{if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol)) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

        BQTermList newBQTList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
        for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
          argTerm = inferAllTypes(argTerm, tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
          newBQTList =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
        }
        return newBQTList.reverse(); 
      }}}}{if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol)) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch433_5= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ;if ( (tomMatch433_5 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch433_5) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch433_8= tomMatch433_5.getDomain() ; tom.engine.adt.tomtype.types.TomType  tomMatch433_9= tomMatch433_5.getCodomain() ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch433_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch433_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if (!( tomMatch433_8.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch433_8.getHeadconcTomType() ; tom.engine.adt.tomtype.types.TomTypeList  tom_tailTTList= tomMatch433_8.getTailconcTomType() ;if ( (tomMatch433_9 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch433_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch433_12= tomMatch433_9.getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch433_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch433_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch433_end_21=tomMatch433_12;do {{if (!( tomMatch433_end_21.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch433_24= tomMatch433_end_21.getHeadconcTypeOption() ;if ( (tomMatch433_24 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch433_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {




        if(!tom_tailTTList.isEmptyconcTomType()) {
          throw new TomRuntimeException("should be empty list: " + tom_tailTTList);
        }

        BQTermList newBQTList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
        for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
          TomSymbol argSymb = getSymbolFromTerm(argTerm);
          TomType argType = contextType;
          if(!(TomBase.isListOperator(argSymb) || TomBase.isArrayOperator(argSymb))) {
            {{if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )argTerm)) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )argTerm)) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {

                /*
                 * We don't know what is into the Composite
                 * It can be a BQVariableStar or a list operator or a list of
                 * CompositeBQTerm or something else
                 */
                argType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
              }}}{if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )argTerm)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {


                /* Case CT-STAR rule (applying to premises) */
                argType = tomMatch433_9;
              }}}}{if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch434_10= false ; tom.engine.adt.code.types.BQTerm  tomMatch434_8= null ; tom.engine.adt.code.types.BQTerm  tomMatch434_9= null ;if ( ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )argTerm)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch434_10= true ;tomMatch434_8=(( tom.engine.adt.code.types.BQTerm )argTerm);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )argTerm)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch434_10= true ;tomMatch434_9=(( tom.engine.adt.code.types.BQTerm )argTerm);}}}}}if (tomMatch434_10) {




                argType = tom_headTTList;
              }}}}

          } else if ( (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() != argSymb.getAstName()) {
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
      }}}if ( tomMatch433_end_21.isEmptyconcTypeOption() ) {tomMatch433_end_21=tomMatch433_12;} else {tomMatch433_end_21= tomMatch433_end_21.getTailconcTypeOption() ;}}} while(!( (tomMatch433_end_21==tomMatch433_12) ));}}}}}}}}}}}{if ( (tSymbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol)) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch433_28= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getTypesToType() ; tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getAstName() ;if ( (tomMatch433_28 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch433_28) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch433_34= tomMatch433_28.getCodomain() ;if ( (tomMatch433_34 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch433_34) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch433_37= tomMatch433_34.getTypeOptions() ; tom.engine.adt.tomslot.types.PairNameDeclList  tom_pNDList= (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getPairNameDeclList() ;boolean tomMatch433_48= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch433_37) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch433_37) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch433_end_43=tomMatch433_37;do {{if (!( tomMatch433_end_43.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch433_46= tomMatch433_end_43.getHeadconcTypeOption() ;if ( (tomMatch433_46 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch433_46) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch433_48= true ;}}}if ( tomMatch433_end_43.isEmptyconcTypeOption() ) {tomMatch433_end_43=tomMatch433_37;} else {tomMatch433_end_43= tomMatch433_end_43.getTailconcTypeOption() ;}}} while(!( (tomMatch433_end_43==tomMatch433_37) ));}if (!(tomMatch433_48)) {



        if(tom_pNDList.length() != bqTList.length()) {
          Option option = TomBase.findOriginTracking( (( tom.engine.adt.tomsignature.types.TomSymbol )tSymbol).getOptions() );
          {{if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )option)) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

              TomMessage.error(logger, (( tom.engine.adt.tomoption.types.Option )option).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
                  TomMessage.symbolNumberArgument,tom_symName.getString(),tom_pNDList.length(),bqTList.length());
            }}}}{if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )option)) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {

              TomMessage.error(logger,null,0,
                  TomMessage.symbolNumberArgument,tom_symName.getString(),tom_pNDList.length(),bqTList.length());
            }}}}}

        } else {
          TomTypeList symDomain =  tomMatch433_28.getDomain() ;
          BQTermList newBQTList =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
          for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
            TomType argType = symDomain.getHeadconcTomType();
            {{if ( (argTerm instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )argTerm)) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )argTerm)) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) { tom.engine.adt.code.types.BQTerm  tomMatch436_end_4=(( tom.engine.adt.code.types.BQTerm )argTerm);do {{if (!( tomMatch436_end_4.isEmptyComposite() )) { tom.engine.adt.code.types.BQTerm  tomMatch436_5= tomMatch436_end_4.getTailComposite() ; tom.engine.adt.code.types.BQTerm  tomMatch436_end_8=tomMatch436_5;do {{if (!( tomMatch436_end_8.isEmptyComposite() )) {
















 argType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ; }if ( tomMatch436_end_8.isEmptyComposite() ) {tomMatch436_end_8=tomMatch436_5;} else {tomMatch436_end_8= tomMatch436_end_8.getTailComposite() ;}}} while(!( (tomMatch436_end_8==tomMatch436_5) ));}if ( tomMatch436_end_4.isEmptyComposite() ) {tomMatch436_end_4=(( tom.engine.adt.code.types.BQTerm )argTerm);} else {tomMatch436_end_4= tomMatch436_end_4.getTailComposite() ;}}} while(!( (tomMatch436_end_4==(( tom.engine.adt.code.types.BQTerm )argTerm)) ));}}}}

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
   * constraints and generate a set of substitutions for type variables. Then
   * the substitutions are applied to the list of subtype constraints and the
   * method <code>solveEquationConstraints</code> to solve this list. 
   */
  private void solveConstraints() {
    //DEBUG System.out.println("\nsolveConstraints 1:");
    //DEBUG printGeneratedConstraints(equationConstraints);
    //DEBUG printGeneratedConstraints(subtypeConstraints);
    boolean errorFound = solveEquationConstraints(equationConstraints);
    if (!errorFound) {
      {{if ( (subtypeConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch437_2= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )subtypeConstraints)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )subtypeConstraints)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )subtypeConstraints).isEmptyconcTypeConstraint() ) {tomMatch437_2= true ;}}if (!(tomMatch437_2)) {

          TypeConstraintList simplifiedConstraints = replaceInSubtypingConstraints(subtypeConstraints);
          //DEBUG printGeneratedConstraints(simplifiedConstraints);
            solveSubtypingConstraints(simplifiedConstraints);
        }}}}

    }
    checkAllPatterns();
  }

  /**
   * The method <code>checkAllPatterns</code> check if all variables in patterns
   * have their types inferred. If it does not hold, then the
   * <code>equationConstraints</code> list is traversed to given the necessary
   * informations about the variables. Note that this verification is performed for
   * the <code>subtypeConstraints</code> list in the
   * <code>garbageCollecting</code> method
   */
  private void checkAllPatterns() {
    for (TomType pType : inputTVarList.getCollectionconcTomType()) {
      {{ tom.engine.adt.tomtype.types.TomType  tomMatch438_0=substitutions.get(pType);if ( (tomMatch438_0 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch438_0) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )tomMatch438_0)) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (equationConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch438_end_7=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints);do {{if (!( tomMatch438_end_7.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch438_12= tomMatch438_end_7.getHeadconcTypeConstraint() ;if ( (tomMatch438_12 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch438_12) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch438_0)== tomMatch438_12.getType1() ) ) {printErrorGuessMatch( tomMatch438_12.getInfo() )


;
          }}}}if ( tomMatch438_end_7.isEmptyconcTypeConstraint() ) {tomMatch438_end_7=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints);} else {tomMatch438_end_7= tomMatch438_end_7.getTailconcTypeConstraint() ;}}} while(!( (tomMatch438_end_7==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints)) ));}}}}}}{ tom.engine.adt.tomtype.types.TomType  tomMatch438_15=substitutions.get(pType);if ( (tomMatch438_15 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch438_15) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )tomMatch438_15)) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (equationConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch438_end_22=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints);do {{if (!( tomMatch438_end_22.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch438_27= tomMatch438_end_22.getHeadconcTypeConstraint() ;if ( (tomMatch438_27 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch438_27) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch438_15)== tomMatch438_27.getType2() ) ) {printErrorGuessMatch( tomMatch438_27.getInfo() )



;
          }}}}if ( tomMatch438_end_22.isEmptyconcTypeConstraint() ) {tomMatch438_end_22=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints);} else {tomMatch438_end_22= tomMatch438_end_22.getTailconcTypeConstraint() ;}}} while(!( (tomMatch438_end_22==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )equationConstraints)) ));}}}}}}}

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
        {{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch439_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch439_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_groundType1=tomMatch439_1; tom.engine.adt.tomtype.types.TomType  tom_groundType2=tomMatch439_2;boolean tomMatch439_12= false ;if ( (tomMatch439_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch439_1) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType1==tomMatch439_1) ) {tomMatch439_12= true ;}}}if (!(tomMatch439_12)) {boolean tomMatch439_11= false ;if ( (tomMatch439_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch439_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType2==tomMatch439_2) ) {tomMatch439_11= true ;}}}if (!(tomMatch439_11)) {if (!( (tom_groundType2==tom_groundType1) )) {



              //DEBUG System.out.println("In solveEquationConstraints:" + `groundType1 +
              //DEBUG     " = " + `groundType2);
              errorFound = (errorFound || detectFail((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)));
              break matchBlockAdd;
            }}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch439_14= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch439_15= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch439_14;if ( (tomMatch439_15 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch439_15) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch439_15;boolean tomMatch439_24= false ;if ( (tomMatch439_14 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch439_14) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType==tomMatch439_14) ) {tomMatch439_24= true ;}}}if (!(tomMatch439_24)) {



              if (substitutions.containsKey(tom_typeVar)) {
                TomType mapTypeVar = substitutions.get(tom_typeVar);
                if (!isTypeVar(mapTypeVar)) {
                  errorFound = (errorFound || 
                      detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_groundType, mapTypeVar,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) ));
                } else {
                  // if (isTypeVar(mapTypeVar))
                  substitutions.addSubstitution(mapTypeVar,tom_groundType);
                }
              } else {
                substitutions.addSubstitution(tom_typeVar,tom_groundType);
              }
              break matchBlockAdd;
            }}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch439_26= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch439_27= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch439_26 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch439_26) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch439_26; tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch439_27;boolean tomMatch439_36= false ;if ( (tomMatch439_27 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch439_27) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType==tomMatch439_27) ) {tomMatch439_36= true ;}}}if (!(tomMatch439_36)) {



            if (substitutions.containsKey(tom_typeVar)) {
              TomType mapTypeVar = substitutions.get(tom_typeVar);
              if (!isTypeVar(mapTypeVar)) {
                errorFound = (errorFound || 
                    detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar, tom_groundType,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) ));
              } else {
                // if (isTypeVar(mapTypeVar))
                substitutions.addSubstitution(mapTypeVar,tom_groundType);
              }
            } else {
              substitutions.addSubstitution(tom_typeVar,tom_groundType);
            }
            break matchBlockAdd;
          }}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch439_38= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch439_39= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch439_38 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch439_38) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar1=tomMatch439_38;if ( (tomMatch439_39 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch439_39) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar2=tomMatch439_39;if (!( (tom_typeVar2==tom_typeVar1) )) {




              TomType mapTypeVar1;
              TomType mapTypeVar2;
              if (substitutions.containsKey(tom_typeVar1) && substitutions.containsKey(tom_typeVar2)) {
                mapTypeVar1 = substitutions.get(tom_typeVar1);
                mapTypeVar2 = substitutions.get(tom_typeVar2);
                if (isTypeVar(mapTypeVar1)) {
                  substitutions.addSubstitution(mapTypeVar1,mapTypeVar2);
                } else {
                  if (isTypeVar(mapTypeVar2)) {
                    substitutions.addSubstitution(mapTypeVar2,mapTypeVar1);
                  } else {
                    errorFound = (errorFound || 
                        detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar1, mapTypeVar2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) ));
                  }
                }
                break matchBlockAdd;
              } else if (substitutions.containsKey(tom_typeVar1)) {
                mapTypeVar1 = substitutions.get(tom_typeVar1);
                substitutions.addSubstitution(tom_typeVar2,mapTypeVar1);
                break matchBlockAdd;
              } else if (substitutions.containsKey(tom_typeVar2)){
                mapTypeVar2 = substitutions.get(tom_typeVar2);
                substitutions.addSubstitution(tom_typeVar1,mapTypeVar2);
                break matchBlockAdd;
              } else {
                substitutions.addSubstitution(tom_typeVar1,tom_typeVar2);
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
    {{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch440_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch440_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch440_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch440_1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_tName1= tomMatch440_1.getTomType() ;if ( (tomMatch440_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch440_2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tomMatch440_8= tomMatch440_2.getTomType() ; String  tom_tName2=tomMatch440_8;boolean tomMatch440_13= false ;if ( tom_tName1.equals(tomMatch440_8) ) {if ( tom_tName2.equals(tomMatch440_8) ) {tomMatch440_13= true ;}}if (!(tomMatch440_13)) {if (!( "unknown type".equals(tom_tName1) )) {if (!( "unknown type".equals(tom_tName2) )) {



          printErrorIncompatibility(tConstraint);
          return true;
        }}}}}}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.tomtype.types.TomType  tomMatch440_17= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch440_18= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;if ( (tomMatch440_17 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch440_17) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions1= tomMatch440_17.getTypeOptions() ;if ( (tomMatch440_18 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch440_18) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch440_25= tomMatch440_18.getTypeOptions() ; tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions2=tomMatch440_25;if (  tomMatch440_17.getTomType() .equals( tomMatch440_18.getTomType() ) ) {if ( (tom_tOptions1 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch440_end_32=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1);do {{if (!( tomMatch440_end_32.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch440_43= tomMatch440_end_32.getHeadconcTypeOption() ;if ( (tomMatch440_43 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch440_43) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( (tom_tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch440_end_38=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);do {{if (!( tomMatch440_end_38.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch440_46= tomMatch440_end_38.getHeadconcTypeOption() ;if ( (tomMatch440_46 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch440_46) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) { tom.engine.adt.tomname.types.TomName  tomMatch440_45= tomMatch440_46.getRootSymbolName() ;boolean tomMatch440_53= false ;if ( ( tomMatch440_43.getRootSymbolName() ==tomMatch440_45) ) {if ( (tomMatch440_45==tomMatch440_45) ) {tomMatch440_53= true ;}}if (!(tomMatch440_53)) {boolean tomMatch440_52= false ;if ( (tom_tOptions1==tomMatch440_25) ) {if ( (tom_tOptions2==tomMatch440_25) ) {tomMatch440_52= true ;}}if (!(tomMatch440_52)) {






          printErrorIncompatibility(tConstraint);
          return true;
        }}}}}if ( tomMatch440_end_38.isEmptyconcTypeOption() ) {tomMatch440_end_38=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);} else {tomMatch440_end_38= tomMatch440_end_38.getTailconcTypeOption() ;}}} while(!( (tomMatch440_end_38==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) ));}}}}}if ( tomMatch440_end_32.isEmptyconcTypeOption() ) {tomMatch440_end_32=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1);} else {tomMatch440_end_32= tomMatch440_end_32.getTailconcTypeOption() ;}}} while(!( (tomMatch440_end_32==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) ));}}}}}}}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch440_56= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch440_56;boolean tomMatch440_61= false ;if ( (tom_t1==tomMatch440_56) ) {if ( (tom_t2==tomMatch440_56) ) {tomMatch440_61= true ;}}if (!(tomMatch440_61)) {


        if (!isSubtypeOf(tom_t1,tom_t2)) {
          printErrorIncompatibility(tConstraint);
          return true;
        }
      }}}}}}

    return false;
  }

  /**
   * The method <code>replaceInSubtypingConstraints</code> applies the
   * substitutions to a given type constraint list.
   * @param tCList the type constraint list to be replaced
   * @return       the type constraint list resulting of replacement
   */
  private TypeConstraintList replaceInSubtypingConstraints(TypeConstraintList tCList) {
    TypeConstraintList replacedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    TomType mapT1;
    TomType mapT2;
    for (TypeConstraint tConstraint: tCList.getCollectionconcTypeConstraint()) {
      {{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tom_t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;

          mapT1 = substitutions.get(tom_t1);
          mapT2 = substitutions.get(tom_t2); 
          if (mapT1 == null) {
            mapT1 = tom_t1;
          }
          if (mapT2 == null) {
            mapT2 = tom_t2;
          }
          if (mapT1 != mapT2) {
            replacedtCList = addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(mapT1, mapT2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getInfo() ) ,replacedtCList);
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
        solvedConstraints = tom_make_RepeatId(tom_make_simplificationAndClosure(this)).visitLight(solvedConstraints);
        //calculatePolarities(simplifiedConstraints);
        //solvedConstraints = simplifiedConstraints;
        //while (!solvedConstraints.isEmptyconcTypeConstraint()){
        while (solvedConstraints != simplifiedConstraints) {
          simplifiedConstraints = incompatibilityDetection(solvedConstraints);
          //System.out.println("list0.size = " + simplifiedConstraints.length());
          {{if ( (simplifiedConstraints instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch442_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints);do {{if (!( tomMatch442_end_4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch442_7= tomMatch442_end_4.getHeadconcTypeConstraint() ;if ( (tomMatch442_7 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch442_7) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint) ) {

              //DEBUG System.out.println("Error!!");
              break tryBlock;
            }}}if ( tomMatch442_end_4.isEmptyconcTypeConstraint() ) {tomMatch442_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints);} else {tomMatch442_end_4= tomMatch442_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch442_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )simplifiedConstraints)) ));}}}}


          TypeConstraintList tmpsimplifiedConstraints = simplifiedConstraints;
          //System.out.println("list1.size = " + simplifiedConstraints.length());
          simplifiedConstraints = computeCanonization(simplifiedConstraints);
          //System.out.println("list2.size = " + simplifiedConstraints.length());
          //System.out.println("list2 = " + simplifiedConstraints);

          //tmpsimplifiedConstraints = `RepeatId(applyCanonization(this)).visitLight(tmpsimplifiedConstraints);
          //System.out.println("list3.size = " + tmpsimplifiedConstraints.length());
          //System.out.println("list3 = " + tmpsimplifiedConstraints);

          //if(simplifiedConstraints.length() != tmpsimplifiedConstraints.length()) {
            //System.out.println("list2.size = " + simplifiedConstraints.length());
            //System.out.println("list2 = " + simplifiedConstraints);
            //System.out.println("list3.size = " + tmpsimplifiedConstraints.length());
            //System.out.println("list3 = " + tmpsimplifiedConstraints);
          //}

          solvedConstraints = generateSolutions(simplifiedConstraints);
          //System.out.println("list4.size = " + solvedConstraints.length());
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
  public static class simplificationAndClosure extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public simplificationAndClosure( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{if (!( tomMatch443_end_4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch443_14= tomMatch443_end_4.getHeadconcTypeConstraint() ;if ( (tomMatch443_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch443_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch443_14.getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch443_14.getType2() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_5= tomMatch443_end_4.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_end_8=tomMatch443_5;do {{if (!( tomMatch443_end_8.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch443_18= tomMatch443_end_8.getHeadconcTypeConstraint() ;if ( (tomMatch443_18 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch443_18) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tom_t2== tomMatch443_18.getType1() ) ) {if ( (tom_t1== tomMatch443_18.getType2() ) ) {



        nkt.solveEquationConstraints( tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_t1, tom_t2,  tomMatch443_14.getInfo() ) , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) );
        return
          nkt.tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch443_end_4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint(tomMatch443_5,tomMatch443_end_8, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch443_end_8.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
      }}}}}if ( tomMatch443_end_8.isEmptyconcTypeConstraint() ) {tomMatch443_end_8=tomMatch443_5;} else {tomMatch443_end_8= tomMatch443_end_8.getTailconcTypeConstraint() ;}}} while(!( (tomMatch443_end_8==tomMatch443_5) ));}}}if ( tomMatch443_end_4.isEmptyconcTypeConstraint() ) {tomMatch443_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch443_end_4= tomMatch443_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch443_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg); tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_end_27=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{if (!( tomMatch443_end_27.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch443_36= tomMatch443_end_27.getHeadconcTypeConstraint() ;if ( (tomMatch443_36 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch443_36) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch443_35= tomMatch443_36.getType2() ; tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch443_36.getType1() ;if ( (tomMatch443_35 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch443_35) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_28= tomMatch443_end_27.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_end_31=tomMatch443_28;do {{if (!( tomMatch443_end_31.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch443_43= tomMatch443_end_31.getHeadconcTypeConstraint() ;if ( (tomMatch443_43 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch443_43) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tomMatch443_35== tomMatch443_43.getType1() ) ) { tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch443_43.getType2() ;if ( (tom_tcl instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch443_58= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_end_48=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl);do {{if (!( tomMatch443_end_48.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch443_54= tomMatch443_end_48.getHeadconcTypeConstraint() ;if ( (tomMatch443_54 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch443_54) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tom_t1== tomMatch443_54.getType1() ) ) {if ( (tom_t2== tomMatch443_54.getType2() ) ) {tomMatch443_58= true ;}}}}}if ( tomMatch443_end_48.isEmptyconcTypeConstraint() ) {tomMatch443_end_48=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl);} else {tomMatch443_end_48= tomMatch443_end_48.getTailconcTypeConstraint() ;}}} while(!( (tomMatch443_end_48==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl)) ));}if (!(tomMatch443_58)) {








          //DEBUG System.out.println("\nsolve2a: tcl =" + `tcl);
          return
            nkt.addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2,  tomMatch443_43.getInfo() ) ,tom_tcl);
        }}}}}}if ( tomMatch443_end_31.isEmptyconcTypeConstraint() ) {tomMatch443_end_31=tomMatch443_28;} else {tomMatch443_end_31= tomMatch443_end_31.getTailconcTypeConstraint() ;}}} while(!( (tomMatch443_end_31==tomMatch443_28) ));}}}}}if ( tomMatch443_end_27.isEmptyconcTypeConstraint() ) {tomMatch443_end_27=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch443_end_27= tomMatch443_end_27.getTailconcTypeConstraint() ;}}} while(!( (tomMatch443_end_27==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg); tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_end_64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{if (!( tomMatch443_end_64.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch443_74= tomMatch443_end_64.getHeadconcTypeConstraint() ;if ( (tomMatch443_74 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch443_74) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch443_71= tomMatch443_74.getType1() ;if ( (tomMatch443_71 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch443_71) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch443_74.getType2() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_65= tomMatch443_end_64.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_end_68=tomMatch443_65;do {{if (!( tomMatch443_end_68.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch443_80= tomMatch443_end_68.getHeadconcTypeConstraint() ;if ( (tomMatch443_80 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch443_80) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch443_80.getType1() ;if ( (tomMatch443_71== tomMatch443_80.getType2() ) ) {if ( (tom_tcl instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch443_95= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch443_end_85=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl);do {{if (!( tomMatch443_end_85.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch443_91= tomMatch443_end_85.getHeadconcTypeConstraint() ;if ( (tomMatch443_91 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch443_91) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {if ( (tom_t1== tomMatch443_91.getType1() ) ) {if ( (tom_t2== tomMatch443_91.getType2() ) ) {tomMatch443_95= true ;}}}}}if ( tomMatch443_end_85.isEmptyconcTypeConstraint() ) {tomMatch443_end_85=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl);} else {tomMatch443_end_85= tomMatch443_end_85.getTailconcTypeConstraint() ;}}} while(!( (tomMatch443_end_85==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_tcl)) ));}if (!(tomMatch443_95)) {


          //DEBUG System.out.println("\nsolve2b: tcl = " + `tcl);
          return
            nkt.addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2,  tomMatch443_74.getInfo() ) ,tom_tcl);
        }}}}}}if ( tomMatch443_end_68.isEmptyconcTypeConstraint() ) {tomMatch443_end_68=tomMatch443_65;} else {tomMatch443_end_68= tomMatch443_end_68.getTailconcTypeConstraint() ;}}} while(!( (tomMatch443_end_68==tomMatch443_65) ));}}}}}if ( tomMatch443_end_64.isEmptyconcTypeConstraint() ) {tomMatch443_end_64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch443_end_64= tomMatch443_end_64.getTailconcTypeConstraint() ;}}} while(!( (tomMatch443_end_64==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}}return _visit_TypeConstraintList(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_simplificationAndClosure( NewKernelTyper  t0) { return new simplificationAndClosure(t0);}



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
    for(TypeConstraint tConstraint : tCList.getCollectionconcTypeConstraint()) {
matchBlock :
      {
        {{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch444_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch444_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint).getType2() ;boolean tomMatch444_10= false ;if ( (tomMatch444_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch444_1) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch444_10= true ;}}if (!(tomMatch444_10)) {boolean tomMatch444_9= false ;if ( (tomMatch444_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch444_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch444_9= true ;}}if (!(tomMatch444_9)) {

            //DEBUG System.out.println("\nsolve3: tConstraint=" + `tConstraint);
            errorFound = (errorFound || detectFail(tConstraint));
            break matchBlock;
          }}}}}}{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {

            simplifiedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(simplifiedtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
          }}}

      }
    }
    if (errorFound) { 
      simplifiedtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(simplifiedtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
    }
    return simplifiedtCList;
  }

  /**
   * The method <code>applyCanonization</code> is generated by a
   * strategy which simplifies a subtype constraints list by looking for exactly
   * one non-variable lower and one non-variable upper bound for each type
   * variable, i.e. put tCList in a canonical form.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * - Upper bound:
   * tCList = {A <: T1, A <:T2} U tCList' and Map 
   *      --> {A <: T1} U tCList' and Map
   *       if T1 <: T2 
   *      --> {A <: T2} U tCList' and Map
   *       if T2 <: T1
   *      --> {False} U tCList' and Map
   *       if T1 and T2 are not comparable
   * - Lower bound:
   * tCList = {T1 <: A, T2 <: A} U tCList' and Map
   *      --> {T <: A} U tCList' and Map
   *       if there exists T such that T is the lowest upper bound of T1 and T2
   *      --> {False} U tCList' and Map
   *       if there does not exist T such that T is the lowest upper bound of T1 and T2
   * <p>
   * @param nkt an instance of object NewKernelTyper
   * @return    the subtype constraint list resulting
   */

  private TypeConstraintList computeCanonization(TypeConstraintList subject) {
    if(subject.isEmptyconcTypeConstraint()) {
      return subject;
    }
    List<TypeConstraint> list = new ArrayList<TypeConstraint>();
    
    for(TypeConstraint tc: ((tom.engine.adt.typeconstraints.types.typeconstraintlist.concTypeConstraint)subject)) {
      list.add(tc);
    }

    Collections.sort(list,new ConstraintComparator());
    //for(TypeConstraint tc: list) { System.out.println(tc); }

    boolean finished = false;
    int index = 0;
    while(!finished) {
      if(list.size()>=index+2) {
        TypeConstraint first = list.get(index);
        TypeConstraint second = list.get(index+1);

        {{if ( (first instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )first) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )first)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch445_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch445_3= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getType2() ;if ( (tomMatch445_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch445_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch445_2; tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch445_3; tom.engine.adt.typeconstraints.types.Info  tom_info= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getInfo() ;if ( (second instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )second) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )second)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch445_10= (( tom.engine.adt.typeconstraints.types.TypeConstraint )second).getType2() ;if ( (tom_tVar== (( tom.engine.adt.typeconstraints.types.TypeConstraint )second).getType1() ) ) { tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch445_10;boolean tomMatch445_21= false ;if ( (tomMatch445_10 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch445_10) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t2==tomMatch445_10) ) {tomMatch445_21= true ;}}}if (!(tomMatch445_21)) {boolean tomMatch445_20= false ;if ( (tomMatch445_3 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch445_3) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t1==tomMatch445_3) ) {tomMatch445_20= true ;}}}if (!(tomMatch445_20)) {


            TomType lowerType = minType(tom_t1,tom_t2);
            if (lowerType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
              printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2, tom_info) );
              list.remove(index);
              list.set(index, tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() );
              //System.out.println("remove1: " + index);
            } else {
              list.remove(index);
              list.set(index, tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_tVar, lowerType, tom_info) );
              //System.out.println("remove2: " + index);
            }
          }}}}}}}}}}}}{if ( (first instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )first) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )first)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch445_24= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch445_25= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch445_24;if ( (tomMatch445_25 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch445_25) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch445_25; tom.engine.adt.typeconstraints.types.Info  tom_info= (( tom.engine.adt.typeconstraints.types.TypeConstraint )first).getInfo() ;if ( (second instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )second) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )second)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch445_31= (( tom.engine.adt.typeconstraints.types.TypeConstraint )second).getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch445_31;if ( (tom_tVar== (( tom.engine.adt.typeconstraints.types.TypeConstraint )second).getType2() ) ) {boolean tomMatch445_43= false ;if ( (tomMatch445_31 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch445_31) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t2==tomMatch445_31) ) {tomMatch445_43= true ;}}}if (!(tomMatch445_43)) {boolean tomMatch445_42= false ;if ( (tomMatch445_24 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch445_24) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t1==tomMatch445_24) ) {tomMatch445_42= true ;}}}if (!(tomMatch445_42)) {



            TomType supType = supType(tom_t1,tom_t2);
            if (supType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
              printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2, tom_info) );
              list.remove(index);
              list.set(index, tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() );
              //System.out.println("remove3: " + index);
            } else {
              list.remove(index);
              list.set(index, tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(supType, tom_tVar, tom_info) );
              //System.out.println("remove4: " + index);
            }
          }}}}}}}}}}}}}



        if(first == list.get(index)) {
          if(index+1<list.size() && second == list.get(index+1)) {
            index++;
            //System.out.println("index++: " + index);
          }
        }

      } else { 
        finished = true;
      }

    }

    TypeConstraintList result =  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
    for(TypeConstraint tc: list) {
      result =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tc,tom_append_list_concTypeConstraint(result, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
    }
    return result;
  }

  private class ConstraintComparator implements Comparator {

    public int compare(Object o1,Object o2) {
      TypeConstraint t1 = (TypeConstraint)o1;
      TypeConstraint t2 = (TypeConstraint)o2;
      int result = 0;
block: {
      {{if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t1)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_3= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ;if ( (tomMatch446_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_st1=tomMatch446_3;if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t2)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_10= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_11= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ;if ( (tomMatch446_10 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_10) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_st2=tomMatch446_11;boolean tomMatch446_25= false ;if ( (tomMatch446_11 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_11) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_st2==tomMatch446_11) ) {tomMatch446_25= true ;}}}if (!(tomMatch446_25)) {boolean tomMatch446_24= false ;if ( (tomMatch446_3 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_3) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_st1==tomMatch446_3) ) {tomMatch446_24= true ;}}}if (!(tomMatch446_24)) {


            int res =  tomMatch446_2.getTomType() .compareTo( tomMatch446_10.getTomType() );
            if(res==0) {
              result =  tomMatch446_10.getIndex() - tomMatch446_2.getIndex() ;
              if(result==0) {
                result = tom_st1.compareToLPO(tom_st2);
              }
              break block;
            } else {
              result = res;
              break block;
            }
          }}}}}}}}}}}}}{if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t1)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_28= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_29= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_st1=tomMatch446_28;if ( (tomMatch446_29 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_29) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t2)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_36= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_37= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_st2=tomMatch446_36;if ( (tomMatch446_37 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_37) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch446_51= false ;if ( (tomMatch446_28 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_28) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_st1==tomMatch446_28) ) {tomMatch446_51= true ;}}}if (!(tomMatch446_51)) {boolean tomMatch446_50= false ;if ( (tomMatch446_36 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_36) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_st2==tomMatch446_36) ) {tomMatch446_50= true ;}}}if (!(tomMatch446_50)) {



            int res =  tomMatch446_29.getTomType() .compareTo( tomMatch446_37.getTomType() );
            if(res==0) {
              result =  tomMatch446_37.getIndex() - tomMatch446_29.getIndex() ;
              if(result==0) {
                result = tom_st1.compareToLPO(tom_st2);
              }
              break block;
            } else {
              result = res;
              break block;
            }
          }}}}}}}}}}}}}{if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t1)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_54= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_55= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ;if ( (tomMatch446_54 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_54) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t2)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_60= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_61= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ;if ( (tomMatch446_60 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_60) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tomMatch446_61 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_61) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch446_70= false ;if ( (tomMatch446_55 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_55) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch446_70= true ;}}if (!(tomMatch446_70)) {



            result = -1;
            break block;
          }}}}}}}}}}}}}}{if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t1)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_73= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_74= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ;if ( (tomMatch446_73 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_73) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tomMatch446_74 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_74) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t2)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_81= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_82= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ;if ( (tomMatch446_81 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_81) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch446_89= false ;if ( (tomMatch446_82 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_82) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch446_89= true ;}}if (!(tomMatch446_89)) {


            result = +1;
            break block;
          }}}}}}}}}}}}}}{if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t1)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_92= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_93= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType2() ;if ( (tomMatch446_92 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_92) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t2)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_98= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ;boolean tomMatch446_107= false ;if ( (tomMatch446_98 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_98) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch446_107= true ;}}if (!(tomMatch446_107)) {boolean tomMatch446_106= false ;if ( (tomMatch446_93 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_93) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch446_106= true ;}}if (!(tomMatch446_106)) {



            result = -1;
            break block;
          }}}}}}}}}}}{if ( (t1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t1) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t1)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_110= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t1).getType1() ;if ( (t2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )t2) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )t2)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch446_114= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch446_115= (( tom.engine.adt.typeconstraints.types.TypeConstraint )t2).getType2() ;if ( (tomMatch446_114 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_114) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch446_125= false ;if ( (tomMatch446_110 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_110) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch446_125= true ;}}if (!(tomMatch446_125)) {boolean tomMatch446_124= false ;if ( (tomMatch446_115 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch446_115) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {tomMatch446_124= true ;}}if (!(tomMatch446_124)) {


            result = +1;
            break block;
          }}}}}}}}}}}}

      result = t1.compareToLPO(t2);
       } // end block

       //System.out.println(t1 + " <--> \n" + t2 + "  = " + result);
       return result;

    }
  }

  /*
   * this is dead code, just for debbuging purpose
   * compring results with computeCanonization
   */
  public static class applyCanonization extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public applyCanonization( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch447_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);do {{ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl1=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg),tomMatch447_end_4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch447_end_4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch447_11= tomMatch447_end_4.getHeadconcTypeConstraint() ;if ( (tomMatch447_11 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch447_11) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.typeconstraints.types.TypeConstraint  tom_c1= tomMatch447_end_4.getHeadconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch447_5= tomMatch447_end_4.getTailconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch447_end_8=tomMatch447_5;do {{ tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl2=tom_get_slice_concTypeConstraint(tomMatch447_5,tomMatch447_end_8, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );if (!( tomMatch447_end_8.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch447_13= tomMatch447_end_8.getHeadconcTypeConstraint() ;if ( (tomMatch447_13 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch447_13) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.typeconstraints.types.TypeConstraint  tom_c2= tomMatch447_end_8.getHeadconcTypeConstraint() ; tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl3= tomMatch447_end_8.getTailconcTypeConstraint() ;{{if ( (tom_c1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch448_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch448_3= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1).getType2() ;if ( (tomMatch448_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch448_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch448_2; tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch448_3; tom.engine.adt.typeconstraints.types.Info  tom_info= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1).getInfo() ;if ( (tom_c2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c2) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c2)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch448_10= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c2).getType2() ;if ( (tom_tVar== (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c2).getType1() ) ) { tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch448_10;boolean tomMatch448_21= false ;if ( (tomMatch448_10 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch448_10) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t2==tomMatch448_10) ) {tomMatch448_21= true ;}}}if (!(tomMatch448_21)) {boolean tomMatch448_20= false ;if ( (tomMatch448_3 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch448_3) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t1==tomMatch448_3) ) {tomMatch448_20= true ;}}}if (!(tomMatch448_20)) {







            TomType lowerType = nkt.minType(tom_t1,tom_t2);

            if (lowerType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
              nkt.printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2, tom_info) );
              return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) ; 
            }

            return nkt.addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_tVar, lowerType, tom_info) ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
          }}}}}}}}}}}}{if ( (tom_c1 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch448_24= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1).getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch448_25= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1).getType2() ; tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch448_24;if ( (tomMatch448_25 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch448_25) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch448_25; tom.engine.adt.typeconstraints.types.Info  tom_info= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c1).getInfo() ;if ( (tom_c2 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c2) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c2)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch448_31= (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c2).getType1() ; tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch448_31;if ( (tom_tVar== (( tom.engine.adt.typeconstraints.types.TypeConstraint )tom_c2).getType2() ) ) {boolean tomMatch448_43= false ;if ( (tomMatch448_31 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch448_31) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t2==tomMatch448_31) ) {tomMatch448_43= true ;}}}if (!(tomMatch448_43)) {boolean tomMatch448_42= false ;if ( (tomMatch448_24 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch448_24) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_t1==tomMatch448_24) ) {tomMatch448_42= true ;}}}if (!(tomMatch448_42)) {


            TomType supType = nkt.supType(tom_t1,tom_t2);

            if (supType ==  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
              nkt.printErrorIncompatibility( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2, tom_info) );
              return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) ; 
            }

            return nkt.addConstraintSlow( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(supType, tom_tVar, tom_info) ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));
          }}}}}}}}}}}}}

      }}}if ( tomMatch447_end_8.isEmptyconcTypeConstraint() ) {tomMatch447_end_8=tomMatch447_5;} else {tomMatch447_end_8= tomMatch447_end_8.getTailconcTypeConstraint() ;}}} while(!( (tomMatch447_end_8==tomMatch447_5) ));}}}if ( tomMatch447_end_4.isEmptyconcTypeConstraint() ) {tomMatch447_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg);} else {tomMatch447_end_4= tomMatch447_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch447_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom__arg)) ));}}}}return _visit_TypeConstraintList(tom__arg,introspector);}}




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
    {{if ( (t1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )t1) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )t1)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions1= (( tom.engine.adt.tomtype.types.TomType )t1).getTypeOptions() ; String  tom_tName1= (( tom.engine.adt.tomtype.types.TomType )t1).getTomType() ;if ( (t2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )t2) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )t2)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions2= (( tom.engine.adt.tomtype.types.TomType )t2).getTypeOptions() ; String  tom_tName2= (( tom.engine.adt.tomtype.types.TomType )t2).getTomType() ;


        TomTypeList supTypet1 = dependencies.get(tom_tName1);
        {{if ( (tom_tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {boolean tomMatch450_9= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch450_end_4=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);do {{if (!( tomMatch450_end_4.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch450_7= tomMatch450_end_4.getHeadconcTypeOption() ;if ( (tomMatch450_7 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch450_7) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch450_9= true ;}}}if ( tomMatch450_end_4.isEmptyconcTypeOption() ) {tomMatch450_end_4=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);} else {tomMatch450_end_4= tomMatch450_end_4.getTailconcTypeOption() ;}}} while(!( (tomMatch450_end_4==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) ));}if (!(tomMatch450_9)) {if ( tom_tName2.equals(tom_tName1) ) {


              //DEBUG System.out.println("isSubtypeOf: cases 1a and 3a - i");
              return true; 
            }}}}{if ( (tom_tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (supTypet1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1)) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1)) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch450_end_15=(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1);do {{if (!( tomMatch450_end_15.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch450_19= tomMatch450_end_15.getHeadconcTomType() ;if ( (tomMatch450_19 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch450_19) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {boolean tomMatch450_29= false ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch450_end_24=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);do {{if (!( tomMatch450_end_24.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch450_27= tomMatch450_end_24.getHeadconcTypeOption() ;if ( (tomMatch450_27 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch450_27) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {tomMatch450_29= true ;}}}if ( tomMatch450_end_24.isEmptyconcTypeOption() ) {tomMatch450_end_24=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);} else {tomMatch450_end_24= tomMatch450_end_24.getTailconcTypeOption() ;}}} while(!( (tomMatch450_end_24==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) ));}if (!(tomMatch450_29)) {if (  tomMatch450_19.getTomType() .equals(tom_tName2) ) {

              //DEBUG System.out.println("isSubtypeOf: cases 1a and 3a - ii");
              return true; 
            }}}}}if ( tomMatch450_end_15.isEmptyconcTomType() ) {tomMatch450_end_15=(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1);} else {tomMatch450_end_15= tomMatch450_end_15.getTailconcTomType() ;}}} while(!( (tomMatch450_end_15==(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1)) ));}}}}{if ( (tom_tOptions1 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch450_end_35=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1);do {{if (!( tomMatch450_end_35.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch450_45= tomMatch450_end_35.getHeadconcTypeOption() ;if ( (tomMatch450_45 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch450_45) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( (tom_tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch450_end_41=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);do {{if (!( tomMatch450_end_41.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch450_48= tomMatch450_end_41.getHeadconcTypeOption() ;if ( (tomMatch450_48 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch450_48) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( ( tomMatch450_45.getRootSymbolName() == tomMatch450_48.getRootSymbolName() ) ) {if ( tom_tName2.equals(tom_tName1) ) {



              //DEBUG System.out.println("isSubtypeOf: case 4a - i");
              return true; 
            }}}}}if ( tomMatch450_end_41.isEmptyconcTypeOption() ) {tomMatch450_end_41=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);} else {tomMatch450_end_41= tomMatch450_end_41.getTailconcTypeOption() ;}}} while(!( (tomMatch450_end_41==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) ));}}}}}if ( tomMatch450_end_35.isEmptyconcTypeOption() ) {tomMatch450_end_35=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1);} else {tomMatch450_end_35= tomMatch450_end_35.getTailconcTypeOption() ;}}} while(!( (tomMatch450_end_35==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) ));}}}{if ( (tom_tOptions1 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch450_end_57=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1);do {{if (!( tomMatch450_end_57.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch450_73= tomMatch450_end_57.getHeadconcTypeOption() ;if ( (tomMatch450_73 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch450_73) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( (tom_tOptions2 instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch450_end_63=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);do {{if (!( tomMatch450_end_63.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch450_76= tomMatch450_end_63.getHeadconcTypeOption() ;if ( (tomMatch450_76 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch450_76) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( ( tomMatch450_73.getRootSymbolName() == tomMatch450_76.getRootSymbolName() ) ) {if ( (supTypet1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1)) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1)) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch450_end_69=(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1);do {{if (!( tomMatch450_end_69.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tomMatch450_79= tomMatch450_end_69.getHeadconcTomType() ;if ( (tomMatch450_79 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch450_79) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if (  tomMatch450_79.getTomType() .equals(tom_tName2) ) {




              //DEBUG System.out.println("isSubtypeOf: case 4a - ii");
              return true; 
            }}}}if ( tomMatch450_end_69.isEmptyconcTomType() ) {tomMatch450_end_69=(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1);} else {tomMatch450_end_69= tomMatch450_end_69.getTailconcTomType() ;}}} while(!( (tomMatch450_end_69==(( tom.engine.adt.tomtype.types.TomTypeList )supTypet1)) ));}}}}}}if ( tomMatch450_end_63.isEmptyconcTypeOption() ) {tomMatch450_end_63=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2);} else {tomMatch450_end_63= tomMatch450_end_63.getTailconcTypeOption() ;}}} while(!( (tomMatch450_end_63==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions2)) ));}}}}}if ( tomMatch450_end_57.isEmptyconcTypeOption() ) {tomMatch450_end_57=(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1);} else {tomMatch450_end_57= tomMatch450_end_57.getTailconcTypeOption() ;}}} while(!( (tomMatch450_end_57==(( tom.engine.adt.tomtype.types.TypeOptionList )tom_tOptions1)) ));}}}}

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
    {{if ( (t1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )t1) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )t1)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch451_2= (( tom.engine.adt.tomtype.types.TomType )t1).getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch451_2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch451_2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch451_end_13=tomMatch451_2;do {{if (!( tomMatch451_end_13.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch451_24= tomMatch451_end_13.getHeadconcTypeOption() ;if ( (tomMatch451_24 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch451_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) { String  tom_tName= (( tom.engine.adt.tomtype.types.TomType )t1).getTomType() ;if ( (t2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )t2) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )t2)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch451_6= (( tom.engine.adt.tomtype.types.TomType )t2).getTypeOptions() ;if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch451_6) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch451_6) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) { tom.engine.adt.tomtype.types.TypeOptionList  tomMatch451_end_19=tomMatch451_6;do {{if (!( tomMatch451_end_19.isEmptyconcTypeOption() )) { tom.engine.adt.tomtype.types.TypeOption  tomMatch451_27= tomMatch451_end_19.getHeadconcTypeOption() ;if ( (tomMatch451_27 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch451_27) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {if ( tom_tName.equals( (( tom.engine.adt.tomtype.types.TomType )t2).getTomType() ) ) {boolean tomMatch451_30= false ;if ( ( tomMatch451_24.getRootSymbolName() == tomMatch451_27.getRootSymbolName() ) ) {tomMatch451_30= true ;}if (!(tomMatch451_30)) {




          //DEBUG System.out.println("\nIn supType: case 4.1");
          // Return the equivalent groudn type without decoration
          return symbolTable.getType(tom_tName); 
        }}}}}if ( tomMatch451_end_19.isEmptyconcTypeOption() ) {tomMatch451_end_19=tomMatch451_6;} else {tomMatch451_end_19= tomMatch451_end_19.getTailconcTypeOption() ;}}} while(!( (tomMatch451_end_19==tomMatch451_6) ));}}}}}}}if ( tomMatch451_end_13.isEmptyconcTypeOption() ) {tomMatch451_end_13=tomMatch451_2;} else {tomMatch451_end_13= tomMatch451_end_13.getTailconcTypeOption() ;}}} while(!( (tomMatch451_end_13==tomMatch451_2) ));}}}}}{if ( (supTypes1 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (supTypes2 instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {boolean tomMatch451_36= false ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supTypes1)) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supTypes1)) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if ( (( tom.engine.adt.tomtype.types.TomTypeList )supTypes1).isEmptyconcTomType() ) {tomMatch451_36= true ;}}if (!(tomMatch451_36)) {boolean tomMatch451_35= false ;if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supTypes2)) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )supTypes2)) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {if ( (( tom.engine.adt.tomtype.types.TomTypeList )supTypes2).isEmptyconcTomType() ) {tomMatch451_35= true ;}}if (!(tomMatch451_35)) {



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
       {{if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch452_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{if (!( tomMatch452_end_4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch452_9= tomMatch452_end_4.getHeadconcTypeConstraint() ;if ( (tomMatch452_9 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch452_9) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch452_7= tomMatch452_9.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch452_8= tomMatch452_9.getType2() ;if ( (tomMatch452_7 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch452_7) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch452_8;boolean tomMatch452_16= false ;if ( (tomMatch452_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch452_8) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType==tomMatch452_8) ) {tomMatch452_16= true ;}}}if (!(tomMatch452_16)) {


           // Same code of cases 7 and 8 of solveEquationConstraints
           substitutions.addSubstitution(tomMatch452_7,tom_groundType);
           newtCList = replaceInSubtypingConstraints(tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch452_end_4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch452_end_4.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
           break matchBlockSolve;
         }}}}}}if ( tomMatch452_end_4.isEmptyconcTypeConstraint() ) {tomMatch452_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch452_end_4= tomMatch452_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch452_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}{if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch452_end_21=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{if (!( tomMatch452_end_21.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch452_26= tomMatch452_end_21.getHeadconcTypeConstraint() ;if ( (tomMatch452_26 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch452_26) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch452_24= tomMatch452_26.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch452_25= tomMatch452_26.getType2() ; tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch452_24;if ( (tomMatch452_25 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch452_25) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {boolean tomMatch452_33= false ;if ( (tomMatch452_24 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch452_24) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tom_groundType==tomMatch452_24) ) {tomMatch452_33= true ;}}}if (!(tomMatch452_33)) {



           substitutions.addSubstitution(tomMatch452_25,tom_groundType);
           newtCList = replaceInSubtypingConstraints(tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList),tomMatch452_end_21, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch452_end_21.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
           break matchBlockSolve;
         }}}}}}if ( tomMatch452_end_21.isEmptyconcTypeConstraint() ) {tomMatch452_end_21=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch452_end_21= tomMatch452_end_21.getTailconcTypeConstraint() ;}}} while(!( (tomMatch452_end_21==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}

     }
     return newtCList;
   }

  private void garbageCollection(TypeConstraintList tCList) {
    TypeConstraintList newtCList = tCList;
    {{if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch453_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);do {{if (!( tomMatch453_end_4.isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch453_10= tomMatch453_end_4.getHeadconcTypeConstraint() ;if ( (tomMatch453_10 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch453_10) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.tomtype.types.TomType  tomMatch453_7= tomMatch453_10.getType1() ; tom.engine.adt.tomtype.types.TomType  tomMatch453_8= tomMatch453_10.getType2() ;if ( (tomMatch453_7 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch453_7) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {if ( (tomMatch453_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch453_8) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.typeconstraints.types.Info  tom_info= tomMatch453_10.getInfo() ;{{if ( (inputTVarList instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList)) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList)) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) { tom.engine.adt.tomtype.types.TomTypeList  tomMatch454_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList);do {{if (!( tomMatch454_end_4.isEmptyconcTomType() )) { tom.engine.adt.tomtype.types.TomType  tom_tVar= tomMatch454_end_4.getHeadconcTomType() ;if ( (tomMatch453_7==tom_tVar) ) {printErrorGuessMatch(tom_info)





;
            newtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(newtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
          }if ( (tomMatch453_8==tom_tVar) ) {printErrorGuessMatch(tom_info);             newtCList =  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(newtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;           }}if ( tomMatch454_end_4.isEmptyconcTomType() ) {tomMatch454_end_4=(( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList);} else {tomMatch454_end_4= tomMatch454_end_4.getTailconcTomType() ;}}} while(!( (tomMatch454_end_4==(( tom.engine.adt.tomtype.types.TomTypeList )inputTVarList)) ));}}}}

      }}}}}}}if ( tomMatch453_end_4.isEmptyconcTypeConstraint() ) {tomMatch453_end_4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList);} else {tomMatch453_end_4= tomMatch453_end_4.getTailconcTypeConstraint() ;}}} while(!( (tomMatch453_end_4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) ));}}}}

  }

  private void printErrorGuessMatch(Info info) {
    {{if ( (info instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )info) instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )(( tom.engine.adt.typeconstraints.types.Info )info)) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) { tom.engine.adt.tomname.types.TomName  tomMatch455_1= (( tom.engine.adt.typeconstraints.types.Info )info).getAstName() ;if ( (tomMatch455_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch455_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_termName= tomMatch455_1.getString() ;

        Option option = TomBase.findOriginTracking( (( tom.engine.adt.typeconstraints.types.Info )info).getOptions() );
        {{if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )option)) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

            TomMessage.error(logger, (( tom.engine.adt.tomoption.types.Option )option).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
                TomMessage.cannotGuessMatchType,tom_termName); 
          }}}}{if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )option)) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {

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
    {{if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )type)) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 return true; }}}}}

    return false;
  }

  /**
   * The method <code>printErrorIncompatibility</code> prints an 'incompatible types' message
   * enriched by informations about a given type constraint.
   * @param tConstraint  the type constraint to be printed
   */
  private void printErrorIncompatibility(TypeConstraint tConstraint) {
    {{if ( (tConstraint instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {boolean tomMatch458_23= false ; tom.engine.adt.tomtype.types.TomType  tomMatch458_4= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch458_8= null ; tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch458_9= null ; tom.engine.adt.tomtype.types.TomType  tomMatch458_5= null ; tom.engine.adt.typeconstraints.types.Info  tomMatch458_6= null ;if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {{tomMatch458_23= true ;tomMatch458_8=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch458_4= tomMatch458_8.getType1() ;tomMatch458_5= tomMatch458_8.getType2() ;tomMatch458_6= tomMatch458_8.getInfo() ;}} else {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint)) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {{tomMatch458_23= true ;tomMatch458_9=(( tom.engine.adt.typeconstraints.types.TypeConstraint )tConstraint);tomMatch458_4= tomMatch458_9.getType1() ;tomMatch458_5= tomMatch458_9.getType2() ;tomMatch458_6= tomMatch458_9.getInfo() ;}}}}}if (tomMatch458_23) { tom.engine.adt.tomtype.types.TomType  tom_tType1=tomMatch458_4; tom.engine.adt.tomtype.types.TomType  tom_tType2=tomMatch458_5; tom.engine.adt.typeconstraints.types.Info  tom_info=tomMatch458_6;if ( (tom_tType1 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom_tType1) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )tom_tType1)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_tName1= (( tom.engine.adt.tomtype.types.TomType )tom_tType1).getTomType() ;if ( (tom_tType2 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom_tType2) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )tom_tType2)) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_tName2= (( tom.engine.adt.tomtype.types.TomType )tom_tType2).getTomType() ;if ( (tom_info instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )tom_info) instanceof tom.engine.adt.typeconstraints.types.Info) ) {if ( ((( tom.engine.adt.typeconstraints.types.Info )(( tom.engine.adt.typeconstraints.types.Info )tom_info)) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) { tom.engine.adt.tomname.types.TomName  tomMatch458_16= (( tom.engine.adt.typeconstraints.types.Info )tom_info).getAstName() ;if ( (tomMatch458_16 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch458_16) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_termName= tomMatch458_16.getString() ;





          Option option = TomBase.findOriginTracking( (( tom.engine.adt.typeconstraints.types.Info )tom_info).getOptions() );
          {{if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )option)) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

              TomMessage.error(logger, (( tom.engine.adt.tomoption.types.Option )option).getFileName() ,  (( tom.engine.adt.tomoption.types.Option )option).getLine() ,
                  TomMessage.incompatibleTypes,tom_tName1,tom_tName2,tom_termName); 
            }}}}{if ( (option instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )option) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )option)) instanceof tom.engine.adt.tomoption.types.option.noOption) ) {

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
    try {
      code = tom_make_InnermostId(tom_make_replaceFreshTypeVar(this)).visitLight(code);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInCode: failure on " + code);
    }
    return code;
  }

  /**
   * The method <code>replaceInSymbolTable</code> calls the strategy
   * <code>replaceFreshTypeVar</code> to apply substitution on each type variable occuring in
   * the Symbol Table.
   */
  private void replaceInSymbolTable() {
    for(String tomName:symbolTable.keySymbolIterable()) {
      TomSymbol tSymbol = getSymbolFromName(tomName);
      try {
        tSymbol = tom_make_InnermostId(tom_make_replaceFreshTypeVar(this)).visitLight(tSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInSymbolTable: failure on " + tSymbol);
      }
      symbolTable.putSymbol(tomName,tSymbol);
    }
  }

  /**
   * The class <code>replaceFreshTypeVar</code> is generated from a strategy
   * which replace each type variable occurring in a given expression by its corresponding substitution. 
   * @param nkt an instance of object NewKernelTyper
   * @return    the expression resulting of a transformation
   */
  public static class replaceFreshTypeVar extends tom.library.sl.AbstractStrategyBasic {private  NewKernelTyper  nkt;public replaceFreshTypeVar( NewKernelTyper  nkt) {super(( new tom.library.sl.Identity() ));this.nkt=nkt;}public  NewKernelTyper  getnkt() {return nkt;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )tom__arg)) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) { tom.engine.adt.tomtype.types.TomType  tom_typeVar=(( tom.engine.adt.tomtype.types.TomType )tom__arg);


        if(nkt.substitutions.containsKey(tom_typeVar)) {
          return nkt.substitutions.get(tom_typeVar);
        } 
      }}}}}return _visit_TomType(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_replaceFreshTypeVar( NewKernelTyper  t0) { return new replaceFreshTypeVar(t0);}



  /**
   * The method <code>printGeneratedConstraints</code> prints braces and calls the method
   * <code>printEachConstraint</code> for a given list.
   * @param tCList the type constraint list to be printed
   */
  public void printGeneratedConstraints(TypeConstraintList tCList) {
    {{if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {boolean tomMatch461_2= false ;if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).isEmptyconcTypeConstraint() ) {tomMatch461_2= true ;}}if (!(tomMatch461_2)) {
 
        System.out.print("\n------ Type Constraints : \n {");
        printEachConstraint(tCList);
        System.out.print("}");
      }}}}

  }

  /**
   * The method <code>printEachConstraint</code> prints symbols '=' and '<:' and print
   * each type occurring in a given type constraint.
   * @param tCList the type constraint list to be printed
   */
  public void printEachConstraint(TypeConstraintList tCList) {
    {{if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch462_7= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getHeadconcTypeConstraint() ;if ( (tomMatch462_7 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch462_7) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getTailconcTypeConstraint() ;

        System.out.print( tomMatch462_7.getType1() );
        System.out.print(" = ");
        System.out.print( tomMatch462_7.getType2() );
        if(tom_tailtCList!=  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
          System.out.print(", "); 
          printEachConstraint(tom_tailtCList);
        }
      }}}}}}{if ( (tCList instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList)) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).isEmptyconcTypeConstraint() )) { tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch462_16= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getHeadconcTypeConstraint() ;if ( (tomMatch462_16 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch462_16) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) { tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )tCList).getTailconcTypeConstraint() ;


        System.out.print( tomMatch462_16.getType1() );
        System.out.print(" <: ");
        System.out.print( tomMatch462_16.getType2() );
        if(tom_tailtCList!=  tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
          System.out.print(", "); 
          printEachConstraint(tom_tailtCList);
        }
      }}}}}}}

  }

} // NewKernelTyper
