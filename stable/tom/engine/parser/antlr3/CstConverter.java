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
 **/

package tom.engine.parser.antlr3;

import java.util.logging.Logger;
import java.util.*;

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
import tom.engine.adt.cst.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class CstConverter {
          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_append_list_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList l1,  tom.engine.adt.tomdeclaration.types.DeclarationList  l2) {     if( l1.isEmptyconcDeclaration() ) {       return l2;     } else if( l2.isEmptyconcDeclaration() ) {       return l1;     } else if(  l1.getTailconcDeclaration() .isEmptyconcDeclaration() ) {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,l2) ;     } else {       return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( l1.getHeadconcDeclaration() ,tom_append_list_concDeclaration( l1.getTailconcDeclaration() ,l2)) ;     }   }   private static   tom.engine.adt.tomdeclaration.types.DeclarationList  tom_get_slice_concDeclaration( tom.engine.adt.tomdeclaration.types.DeclarationList  begin,  tom.engine.adt.tomdeclaration.types.DeclarationList  end, tom.engine.adt.tomdeclaration.types.DeclarationList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcDeclaration()  ||  (end== tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make( begin.getHeadconcDeclaration() ,( tom.engine.adt.tomdeclaration.types.DeclarationList )tom_get_slice_concDeclaration( begin.getTailconcDeclaration() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {     if( l1.isEmptyconcTomType() ) {       return l2;     } else if( l2.isEmptyconcTomType() ) {       return l1;     } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;   }      private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_append_list_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList l1,  tom.engine.adt.tomtype.types.TypeOptionList  l2) {     if( l1.isEmptyconcTypeOption() ) {       return l2;     } else if( l2.isEmptyconcTypeOption() ) {       return l1;     } else if(  l1.getTailconcTypeOption() .isEmptyconcTypeOption() ) {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,l2) ;     } else {       return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,tom_append_list_concTypeOption( l1.getTailconcTypeOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_get_slice_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList  begin,  tom.engine.adt.tomtype.types.TypeOptionList  end, tom.engine.adt.tomtype.types.TypeOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTypeOption()  ||  (end== tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( begin.getHeadconcTypeOption() ,( tom.engine.adt.tomtype.types.TypeOptionList )tom_get_slice_concTypeOption( begin.getTailconcTypeOption() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.CodeList  tom_append_list_concCode( tom.engine.adt.code.types.CodeList l1,  tom.engine.adt.code.types.CodeList  l2) {     if( l1.isEmptyconcCode() ) {       return l2;     } else if( l2.isEmptyconcCode() ) {       return l1;     } else if(  l1.getTailconcCode() .isEmptyconcCode() ) {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,l2) ;     } else {       return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,tom_append_list_concCode( l1.getTailconcCode() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.CodeList  tom_get_slice_concCode( tom.engine.adt.code.types.CodeList  begin,  tom.engine.adt.code.types.CodeList  end, tom.engine.adt.code.types.CodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcCode()  ||  (end== tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.codelist.ConsconcCode.make( begin.getHeadconcCode() ,( tom.engine.adt.code.types.CodeList )tom_get_slice_concCode( begin.getTailconcCode() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {     if( l1.isEmptyComposite() ) {       return l2;     } else if( l2.isEmptyComposite() ) {       return l1;     } else if(  l1.getTailComposite() .isEmptyComposite() ) {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_AndConstraint() ) {       return l2;     } else if( l2.isEmptyCst_AndConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) ) {       if(  l1.getTailCst_AndConstraint() .isEmptyCst_AndConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make( l1.getHeadCst_AndConstraint() ,tom_append_list_Cst_AndConstraint( l1.getTailCst_AndConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_AndConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_AndConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getHeadCst_AndConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_AndConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) )? begin.getTailCst_AndConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraint  tom_append_list_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  l1,  tom.engine.adt.cst.types.CstConstraint  l2) {     if( l1.isEmptyCst_OrConstraint() ) {       return l2;     } else if( l2.isEmptyCst_OrConstraint() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (l1 instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) ) {       if(  l1.getTailCst_OrConstraint() .isEmptyCst_OrConstraint() ) {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,l2) ;       } else {         return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make( l1.getHeadCst_OrConstraint() ,tom_append_list_Cst_OrConstraint( l1.getTailCst_OrConstraint() ,l2)) ;       }     } else {       return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make(l1,l2) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraint  tom_get_slice_Cst_OrConstraint( tom.engine.adt.cst.types.CstConstraint  begin,  tom.engine.adt.cst.types.CstConstraint  end, tom.engine.adt.cst.types.CstConstraint  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCst_OrConstraint()  ||  (end== tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint.make((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getHeadCst_OrConstraint() :begin),( tom.engine.adt.cst.types.CstConstraint )tom_get_slice_Cst_OrConstraint((( ((begin instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || (begin instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) )? begin.getTailCst_OrConstraint() : tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() ),end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSlotList  tom_append_list_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList l1,  tom.engine.adt.cst.types.CstSlotList  l2) {     if( l1.isEmptyConcCstSlot() ) {       return l2;     } else if( l2.isEmptyConcCstSlot() ) {       return l1;     } else if(  l1.getTailConcCstSlot() .isEmptyConcCstSlot() ) {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,tom_append_list_ConcCstSlot( l1.getTailConcCstSlot() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSlotList  tom_get_slice_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList  begin,  tom.engine.adt.cst.types.CstSlotList  end, tom.engine.adt.cst.types.CstSlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSlot()  ||  (end== tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( begin.getHeadConcCstSlot() ,( tom.engine.adt.cst.types.CstSlotList )tom_get_slice_ConcCstSlot( begin.getTailConcCstSlot() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBlockList  tom_append_list_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList l1,  tom.engine.adt.cst.types.CstBlockList  l2) {     if( l1.isEmptyConcCstBlock() ) {       return l2;     } else if( l2.isEmptyConcCstBlock() ) {       return l1;     } else if(  l1.getTailConcCstBlock() .isEmptyConcCstBlock() ) {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,tom_append_list_ConcCstBlock( l1.getTailConcCstBlock() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBlockList  tom_get_slice_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList  begin,  tom.engine.adt.cst.types.CstBlockList  end, tom.engine.adt.cst.types.CstBlockList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBlock()  ||  (end== tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( begin.getHeadConcCstBlock() ,( tom.engine.adt.cst.types.CstBlockList )tom_get_slice_ConcCstBlock( begin.getTailConcCstBlock() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_append_list_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList l1,  tom.engine.adt.cst.types.CstPairSlotBQTermList  l2) {     if( l1.isEmptyConcCstPairSlotBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstPairSlotBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstPairSlotBQTerm() .isEmptyConcCstPairSlotBQTerm() ) {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( l1.getHeadConcCstPairSlotBQTerm() ,tom_append_list_ConcCstPairSlotBQTerm( l1.getTailConcCstPairSlotBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPairSlotBQTermList  tom_get_slice_ConcCstPairSlotBQTerm( tom.engine.adt.cst.types.CstPairSlotBQTermList  begin,  tom.engine.adt.cst.types.CstPairSlotBQTermList  end, tom.engine.adt.cst.types.CstPairSlotBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPairSlotBQTerm()  ||  (end== tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm.make( begin.getHeadConcCstPairSlotBQTerm() ,( tom.engine.adt.cst.types.CstPairSlotBQTermList )tom_get_slice_ConcCstPairSlotBQTerm( begin.getTailConcCstPairSlotBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOptionList  tom_append_list_ConcCstOption( tom.engine.adt.cst.types.CstOptionList l1,  tom.engine.adt.cst.types.CstOptionList  l2) {     if( l1.isEmptyConcCstOption() ) {       return l2;     } else if( l2.isEmptyConcCstOption() ) {       return l1;     } else if(  l1.getTailConcCstOption() .isEmptyConcCstOption() ) {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,tom_append_list_ConcCstOption( l1.getTailConcCstOption() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOptionList  tom_get_slice_ConcCstOption( tom.engine.adt.cst.types.CstOptionList  begin,  tom.engine.adt.cst.types.CstOptionList  end, tom.engine.adt.cst.types.CstOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOption()  ||  (end== tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( begin.getHeadConcCstOption() ,( tom.engine.adt.cst.types.CstOptionList )tom_get_slice_ConcCstOption( begin.getTailConcCstOption() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstPatternList  tom_append_list_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList l1,  tom.engine.adt.cst.types.CstPatternList  l2) {     if( l1.isEmptyConcCstPattern() ) {       return l2;     } else if( l2.isEmptyConcCstPattern() ) {       return l1;     } else if(  l1.getTailConcCstPattern() .isEmptyConcCstPattern() ) {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( l1.getHeadConcCstPattern() ,tom_append_list_ConcCstPattern( l1.getTailConcCstPattern() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstPatternList  tom_get_slice_ConcCstPattern( tom.engine.adt.cst.types.CstPatternList  begin,  tom.engine.adt.cst.types.CstPatternList  end, tom.engine.adt.cst.types.CstPatternList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstPattern()  ||  (end== tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern.make( begin.getHeadConcCstPattern() ,( tom.engine.adt.cst.types.CstPatternList )tom_get_slice_ConcCstPattern( begin.getTailConcCstPattern() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstNameList  tom_append_list_ConcCstName( tom.engine.adt.cst.types.CstNameList l1,  tom.engine.adt.cst.types.CstNameList  l2) {     if( l1.isEmptyConcCstName() ) {       return l2;     } else if( l2.isEmptyConcCstName() ) {       return l1;     } else if(  l1.getTailConcCstName() .isEmptyConcCstName() ) {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,tom_append_list_ConcCstName( l1.getTailConcCstName() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstNameList  tom_get_slice_ConcCstName( tom.engine.adt.cst.types.CstNameList  begin,  tom.engine.adt.cst.types.CstNameList  end, tom.engine.adt.cst.types.CstNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstName()  ||  (end== tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( begin.getHeadConcCstName() ,( tom.engine.adt.cst.types.CstNameList )tom_get_slice_ConcCstName( begin.getTailConcCstName() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOperatorList  tom_append_list_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList l1,  tom.engine.adt.cst.types.CstOperatorList  l2) {     if( l1.isEmptyConcCstOperator() ) {       return l2;     } else if( l2.isEmptyConcCstOperator() ) {       return l1;     } else if(  l1.getTailConcCstOperator() .isEmptyConcCstOperator() ) {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( l1.getHeadConcCstOperator() ,tom_append_list_ConcCstOperator( l1.getTailConcCstOperator() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOperatorList  tom_get_slice_ConcCstOperator( tom.engine.adt.cst.types.CstOperatorList  begin,  tom.engine.adt.cst.types.CstOperatorList  end, tom.engine.adt.cst.types.CstOperatorList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOperator()  ||  (end== tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator.make( begin.getHeadConcCstOperator() ,( tom.engine.adt.cst.types.CstOperatorList )tom_get_slice_ConcCstOperator( begin.getTailConcCstOperator() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBQTermList  tom_append_list_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList l1,  tom.engine.adt.cst.types.CstBQTermList  l2) {     if( l1.isEmptyConcCstBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstBQTerm() .isEmptyConcCstBQTerm() ) {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,tom_append_list_ConcCstBQTerm( l1.getTailConcCstBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBQTermList  tom_get_slice_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList  begin,  tom.engine.adt.cst.types.CstBQTermList  end, tom.engine.adt.cst.types.CstBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBQTerm()  ||  (end== tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( begin.getHeadConcCstBQTerm() ,( tom.engine.adt.cst.types.CstBQTermList )tom_get_slice_ConcCstBQTerm( begin.getTailConcCstBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_append_list_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList l1,  tom.engine.adt.cst.types.CstConstraintActionList  l2) {     if( l1.isEmptyConcCstConstraintAction() ) {       return l2;     } else if( l2.isEmptyConcCstConstraintAction() ) {       return l1;     } else if(  l1.getTailConcCstConstraintAction() .isEmptyConcCstConstraintAction() ) {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( l1.getHeadConcCstConstraintAction() ,tom_append_list_ConcCstConstraintAction( l1.getTailConcCstConstraintAction() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstConstraintActionList  tom_get_slice_ConcCstConstraintAction( tom.engine.adt.cst.types.CstConstraintActionList  begin,  tom.engine.adt.cst.types.CstConstraintActionList  end, tom.engine.adt.cst.types.CstConstraintActionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstConstraintAction()  ||  (end== tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction.make( begin.getHeadConcCstConstraintAction() ,( tom.engine.adt.cst.types.CstConstraintActionList )tom_get_slice_ConcCstConstraintAction( begin.getTailConcCstConstraintAction() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstSymbolList  tom_append_list_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList l1,  tom.engine.adt.cst.types.CstSymbolList  l2) {     if( l1.isEmptyConcCstSymbol() ) {       return l2;     } else if( l2.isEmptyConcCstSymbol() ) {       return l1;     } else if(  l1.getTailConcCstSymbol() .isEmptyConcCstSymbol() ) {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( l1.getHeadConcCstSymbol() ,tom_append_list_ConcCstSymbol( l1.getTailConcCstSymbol() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSymbolList  tom_get_slice_ConcCstSymbol( tom.engine.adt.cst.types.CstSymbolList  begin,  tom.engine.adt.cst.types.CstSymbolList  end, tom.engine.adt.cst.types.CstSymbolList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSymbol()  ||  (end== tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol.make( begin.getHeadConcCstSymbol() ,( tom.engine.adt.cst.types.CstSymbolList )tom_get_slice_ConcCstSymbol( begin.getTailConcCstSymbol() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.BQSlotList  tom_append_list_concBQSlot( tom.engine.adt.tomslot.types.BQSlotList l1,  tom.engine.adt.tomslot.types.BQSlotList  l2) {     if( l1.isEmptyconcBQSlot() ) {       return l2;     } else if( l2.isEmptyconcBQSlot() ) {       return l1;     } else if(  l1.getTailconcBQSlot() .isEmptyconcBQSlot() ) {       return  tom.engine.adt.tomslot.types.bqslotlist.ConsconcBQSlot.make( l1.getHeadconcBQSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.bqslotlist.ConsconcBQSlot.make( l1.getHeadconcBQSlot() ,tom_append_list_concBQSlot( l1.getTailconcBQSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.BQSlotList  tom_get_slice_concBQSlot( tom.engine.adt.tomslot.types.BQSlotList  begin,  tom.engine.adt.tomslot.types.BQSlotList  end, tom.engine.adt.tomslot.types.BQSlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQSlot()  ||  (end== tom.engine.adt.tomslot.types.bqslotlist.EmptyconcBQSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.bqslotlist.ConsconcBQSlot.make( begin.getHeadconcBQSlot() ,( tom.engine.adt.tomslot.types.BQSlotList )tom_get_slice_concBQSlot( begin.getTailconcBQSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }    


  private static Logger logger = Logger.getLogger("tom.engine.typer.CstConverter");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private SymbolTable symbolTable;

  public CstConverter(SymbolTable st) {
    this.symbolTable = st;
  }

  public Code convert(CstProgram cst) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstProgram) ) {if ( ((( tom.engine.adt.cst.types.CstProgram )cst) instanceof tom.engine.adt.cst.types.CstProgram) ) {if ( ((( tom.engine.adt.cst.types.CstProgram )(( tom.engine.adt.cst.types.CstProgram )cst)) instanceof tom.engine.adt.cst.types.cstprogram.Cst_Program) ) {
 
        return  tom.engine.adt.code.types.code.Tom.make(convert( (( tom.engine.adt.cst.types.CstProgram )cst).getblocks() )) ;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Code convert(CstBlock cst) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )(( tom.engine.adt.cst.types.CstBlock )cst)) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {

        CstOption ot = getOriginTracking( (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() );
        return  tom.engine.adt.code.types.code.TargetLanguageToCode.make( tom.engine.adt.code.types.targetlanguage.TL.make( (( tom.engine.adt.cst.types.CstBlock )cst).getcontent() ,  tom.engine.adt.tomsignature.types.textposition.TextPosition.make(ot.getstartLine(), ot.getstartColumn()) ,  tom.engine.adt.tomsignature.types.textposition.TextPosition.make(ot.getendLine(), ot.getendColumn()) ) ) 

;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )(( tom.engine.adt.cst.types.CstBlock )cst)) instanceof tom.engine.adt.cst.types.cstblock.Cst_BQTermToBlock) ) {


        return  tom.engine.adt.code.types.code.BQTermToCode.make(convert( (( tom.engine.adt.cst.types.CstBlock )cst).getbqterm() )) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )(( tom.engine.adt.cst.types.CstBlock )cst)) instanceof tom.engine.adt.cst.types.cstblock.Cst_MatchConstruct) ) {


        ConstraintInstructionList cil = convert( (( tom.engine.adt.cst.types.CstBlock )cst).getconstraintActionList() , (( tom.engine.adt.cst.types.CstBlock )cst).getsubjectList() );
          return  tom.engine.adt.code.types.code.InstructionToCode.make( tom.engine.adt.tominstruction.types.instruction.Match.make(cil, addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() ,"Match"))) ) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )(( tom.engine.adt.cst.types.CstBlock )cst)) instanceof tom.engine.adt.cst.types.cstblock.Cst_TypetermConstruct) ) { tom.engine.adt.cst.types.CstType  tomMatch289_17= (( tom.engine.adt.cst.types.CstBlock )cst).gettypeName() ;if ( (tomMatch289_17 instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tomMatch289_17) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom_typeName= tomMatch289_17.gettype() ; tom.engine.adt.cst.types.CstType  tom_extendsTypeName= (( tom.engine.adt.cst.types.CstBlock )cst).getextendsTypeName() ; tom.engine.adt.cst.types.CstOperatorList  tom_operatorList= (( tom.engine.adt.cst.types.CstBlock )cst).getoperatorList() ;


        CstOption ot = getOriginTracking( (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() );
        TypeOptionList typeoptionList =  tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ;
        DeclarationList declarationList =  tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ;
        {{if ( (tom_extendsTypeName instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tom_extendsTypeName) instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )(( tom.engine.adt.cst.types.CstType )tom_extendsTypeName)) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) {
 typeoptionList =  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.SubtypeDecl.make( (( tom.engine.adt.cst.types.CstType )tom_extendsTypeName).gettype() ) , tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) ; }}}}}{{if ( (tom_operatorList instanceof tom.engine.adt.cst.types.CstOperatorList) ) {if ( (((( tom.engine.adt.cst.types.CstOperatorList )(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList)) instanceof tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator) || ((( tom.engine.adt.cst.types.CstOperatorList )(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList)) instanceof tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator)) ) { tom.engine.adt.cst.types.CstOperatorList  tomMatch291_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList);do {{if (!( tomMatch291_end_4.isEmptyConcCstOperator() )) { tom.engine.adt.cst.types.CstOperator  tom_operator= tomMatch291_end_4.getHeadConcCstOperator() ;{{if ( (tom_operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom_operator) instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )(( tom.engine.adt.cst.types.CstOperator )tom_operator)) instanceof tom.engine.adt.cst.types.cstoperator.Cst_Equals) ) { tom.engine.adt.cst.types.CstName  tomMatch292_1= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getargName1() ; tom.engine.adt.cst.types.CstName  tomMatch292_2= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getargName2() ; tom.engine.adt.cst.types.CstBlockList  tomMatch292_3= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getblock() ;if ( (tomMatch292_1 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch292_1) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom_name1= tomMatch292_1.getname() ;if ( (tomMatch292_2 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch292_2) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom_name2= tomMatch292_2.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch292_3) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch292_3) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch292_3.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch292_16= tomMatch292_3.getHeadConcCstBlock() ;if ( (tomMatch292_16 instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch292_16) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom_optionList2= tomMatch292_16.getoptionList() ;if (  tomMatch292_3.getTailConcCstBlock() .isEmptyConcCstBlock() ) {





                CstOption ot2 = getOriginTracking(tom_optionList2);
                String code = ASTFactory.abstractCode( tomMatch292_16.getcontent() ,tom_name1,tom_name2);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl.make(makeBQVariableFromName(tom_name1,tom_typeName,tom_optionList2), makeBQVariableFromName(tom_name2,tom_typeName,tom_optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_typeName) , ot2.getstartLine(), ot2.getfileName()) 
                    ) 




;
                declarationList =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make(attribute,tom_append_list_concDeclaration(declarationList, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ; 
              }}}}}}}}}}}}}{if ( (tom_operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom_operator) instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )(( tom.engine.adt.cst.types.CstOperator )tom_operator)) instanceof tom.engine.adt.cst.types.cstoperator.Cst_IsSort) ) { tom.engine.adt.cst.types.CstName  tomMatch292_19= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch292_20= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getblock() ;if ( (tomMatch292_19 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch292_19) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom_name= tomMatch292_19.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch292_20) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch292_20) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch292_20.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch292_30= tomMatch292_20.getHeadConcCstBlock() ;if ( (tomMatch292_30 instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch292_30) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom_optionList2= tomMatch292_30.getoptionList() ;if (  tomMatch292_20.getTailConcCstBlock() .isEmptyConcCstBlock() ) {


                CstOption ot2 = getOriginTracking(tom_optionList2);
                String code = ASTFactory.abstractCode( tomMatch292_30.getcontent() ,tom_name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl.make(makeBQVariableFromName(tom_name,tom_typeName,tom_optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_typeName) , ot2.getstartLine(), ot2.getfileName()) 
                    ) 



;
                declarationList =  tom.engine.adt.tomdeclaration.types.declarationlist.ConsconcDeclaration.make(attribute,tom_append_list_concDeclaration(declarationList, tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() )) ; 
              }}}}}}}}}}}{if ( (tom_operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom_operator) instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )(( tom.engine.adt.cst.types.CstOperator )tom_operator)) instanceof tom.engine.adt.cst.types.cstoperator.Cst_Implement) ) { tom.engine.adt.cst.types.CstBlockList  tomMatch292_33= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getblock() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch292_33) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch292_33) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch292_33.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch292_40= tomMatch292_33.getHeadConcCstBlock() ;if ( (tomMatch292_40 instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch292_40) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {if (  tomMatch292_33.getTailConcCstBlock() .isEmptyConcCstBlock() ) {


                TomType astType =  tom.engine.adt.tomtype.types.tomtype.Type.make(typeoptionList, tom_typeName,  tom.engine.adt.tomtype.types.targetlanguagetype.TLType.make( tomMatch292_40.getcontent() ) ) ;
                symbolTable.putType(tom_typeName, astType);
              }}}}}}}}}}


          }if ( tomMatch291_end_4.isEmptyConcCstOperator() ) {tomMatch291_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList);} else {tomMatch291_end_4= tomMatch291_end_4.getTailConcCstOperator() ;}}} while(!( (tomMatch291_end_4==(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList)) ));}}}}


        return  tom.engine.adt.code.types.code.DeclarationToCode.make( tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_typeName) , declarationList,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_typeName) , ot.getstartLine(), ot.getfileName()) 
              ) ) 



;
      }}}}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )cst) instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )(( tom.engine.adt.cst.types.CstBlock )cst)) instanceof tom.engine.adt.cst.types.cstblock.Cst_OpConstruct) ) { tom.engine.adt.cst.types.CstType  tomMatch289_27= (( tom.engine.adt.cst.types.CstBlock )cst).getcodomain() ; tom.engine.adt.cst.types.CstName  tomMatch289_28= (( tom.engine.adt.cst.types.CstBlock )cst).getctorName() ;if ( (tomMatch289_27 instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tomMatch289_27) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom_codomain= tomMatch289_27.gettype() ;if ( (tomMatch289_28 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch289_28) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom_opName= tomMatch289_28.getname() ; tom.engine.adt.cst.types.CstSlotList  tom_slotList= (( tom.engine.adt.cst.types.CstBlock )cst).getargumentList() ; tom.engine.adt.cst.types.CstOperatorList  tom_operatorList= (( tom.engine.adt.cst.types.CstBlock )cst).getoperatorList() ;


        CstOption ot = getOriginTracking( (( tom.engine.adt.cst.types.CstBlock )cst).getoptionList() );
        DeclarationList declarationList =  tom.engine.adt.tomdeclaration.types.declarationlist.EmptyconcDeclaration.make() ;
        List<PairNameDecl> pairNameDeclList = new LinkedList<PairNameDecl>();
        List<TomName> slotNameList = new LinkedList<TomName>();
        List<Option> options = new LinkedList<Option>();
        TomTypeList types =  tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;

        options.add( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_opName) , ot.getstartLine(), ot.getfileName()) );

        {{if ( (tom_slotList instanceof tom.engine.adt.cst.types.CstSlotList) ) {if ( (((( tom.engine.adt.cst.types.CstSlotList )(( tom.engine.adt.cst.types.CstSlotList )tom_slotList)) instanceof tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot) || ((( tom.engine.adt.cst.types.CstSlotList )(( tom.engine.adt.cst.types.CstSlotList )tom_slotList)) instanceof tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot)) ) { tom.engine.adt.cst.types.CstSlotList  tomMatch293_end_4=(( tom.engine.adt.cst.types.CstSlotList )tom_slotList);do {{if (!( tomMatch293_end_4.isEmptyConcCstSlot() )) { tom.engine.adt.cst.types.CstSlot  tomMatch293_9= tomMatch293_end_4.getHeadConcCstSlot() ;if ( (tomMatch293_9 instanceof tom.engine.adt.cst.types.CstSlot) ) {if ( ((( tom.engine.adt.cst.types.CstSlot )tomMatch293_9) instanceof tom.engine.adt.cst.types.cstslot.Cst_Slot) ) { tom.engine.adt.cst.types.CstName  tomMatch293_7= tomMatch293_9.getslotName() ; tom.engine.adt.cst.types.CstType  tomMatch293_8= tomMatch293_9.getslotTypeName() ;if ( (tomMatch293_7 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch293_7) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom_slotName= tomMatch293_7.getname() ;if ( (tomMatch293_8 instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tomMatch293_8) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) { String  tom_slotType= tomMatch293_8.gettype() ;

            TomName astName = ASTFactory.makeName(tom_slotName);
            if(slotNameList.indexOf(astName) != -1) {
              TomMessage.error(getLogger(),ot.getfileName(), ot.getstartLine(),
                  TomMessage.repeatedSlotName,
                  tom_slotName);
            }
            slotNameList.add(astName);
            pairNameDeclList.add( tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl.make(astName,  tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration.make() ) );
            types = tom_append_list_concTomType(types, tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , tom_slotType,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) , tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) );
            String typeOfSlot = getSlotType(tom_codomain,tom_slotName);
            if(typeOfSlot != null && !typeOfSlot.equals(tom_slotType)) {
              TomMessage.warning(getLogger(),ot.getfileName(), ot.getstartLine(),
                  TomMessage.slotIncompatibleTypes,tom_slotName,tom_slotType,typeOfSlot);
            } else {
              putSlotType(tom_codomain,tom_slotName,tom_slotType);
            }

          }}}}}}}if ( tomMatch293_end_4.isEmptyConcCstSlot() ) {tomMatch293_end_4=(( tom.engine.adt.cst.types.CstSlotList )tom_slotList);} else {tomMatch293_end_4= tomMatch293_end_4.getTailConcCstSlot() ;}}} while(!( (tomMatch293_end_4==(( tom.engine.adt.cst.types.CstSlotList )tom_slotList)) ));}}}}{{if ( (tom_operatorList instanceof tom.engine.adt.cst.types.CstOperatorList) ) {if ( (((( tom.engine.adt.cst.types.CstOperatorList )(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList)) instanceof tom.engine.adt.cst.types.cstoperatorlist.ConsConcCstOperator) || ((( tom.engine.adt.cst.types.CstOperatorList )(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList)) instanceof tom.engine.adt.cst.types.cstoperatorlist.EmptyConcCstOperator)) ) { tom.engine.adt.cst.types.CstOperatorList  tomMatch294_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList);do {{if (!( tomMatch294_end_4.isEmptyConcCstOperator() )) { tom.engine.adt.cst.types.CstOperator  tom_operator= tomMatch294_end_4.getHeadConcCstOperator() ;{{if ( (tom_operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom_operator) instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )(( tom.engine.adt.cst.types.CstOperator )tom_operator)) instanceof tom.engine.adt.cst.types.cstoperator.Cst_IsFsym) ) { tom.engine.adt.cst.types.CstName  tomMatch295_1= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getargName() ; tom.engine.adt.cst.types.CstBlockList  tomMatch295_2= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getblock() ;if ( (tomMatch295_1 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch295_1) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom_name= tomMatch295_1.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch295_2) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch295_2) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch295_2.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch295_12= tomMatch295_2.getHeadConcCstBlock() ;if ( (tomMatch295_12 instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch295_12) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom_optionList2= tomMatch295_12.getoptionList() ;if (  tomMatch295_2.getTailConcCstBlock() .isEmptyConcCstBlock() ) {







                CstOption ot2 = getOriginTracking(tom_optionList2);
                String code = ASTFactory.abstractCode( tomMatch295_12.getcontent() ,tom_name);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_opName) , makeBQVariableFromName(tom_name,tom_codomain,tom_optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_opName) , ot2.getstartLine(), ot2.getfileName()) 
                    ) 




;
                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}}}}{if ( (tom_operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom_operator) instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )(( tom.engine.adt.cst.types.CstOperator )tom_operator)) instanceof tom.engine.adt.cst.types.cstoperator.Cst_GetSlot) ) { tom.engine.adt.cst.types.CstName  tomMatch295_15= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getargName1() ; tom.engine.adt.cst.types.CstName  tomMatch295_16= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getargName2() ; tom.engine.adt.cst.types.CstBlockList  tomMatch295_17= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getblock() ;if ( (tomMatch295_15 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch295_15) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) {if ( (tomMatch295_16 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch295_16) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom_argName= tomMatch295_16.getname() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch295_17) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch295_17) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch295_17.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch295_30= tomMatch295_17.getHeadConcCstBlock() ;if ( (tomMatch295_30 instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch295_30) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tom_optionList2= tomMatch295_30.getoptionList() ;if (  tomMatch295_17.getTailConcCstBlock() .isEmptyConcCstBlock() ) {


                CstOption ot2 = getOriginTracking(tom_optionList2);
                String code = ASTFactory.abstractCode( tomMatch295_30.getcontent() ,tom_argName);
                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_opName) ,  tom.engine.adt.tomname.types.tomname.Name.make( tomMatch295_15.getname() ) , makeBQVariableFromName(tom_argName,tom_codomain,tom_optionList2),  tom.engine.adt.tomexpression.types.expression.Code.make(code) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_opName) , ot2.getstartLine(), ot2.getfileName()) 
                    ) 





;

                TomName sName = attribute.getSlotName();
                int index = slotNameList.indexOf(sName);
                /*
                 * ensure that sName appears in slotNameList, only once
                 * ensure that sName has not already been generated
                 */
                TomMessage msg = null;
                if(index == -1) {
                  msg = TomMessage.errorIncompatibleSlotDecl;
                } else {
                  PairNameDecl pair = pairNameDeclList.get(index);
                  {{if ( (pair instanceof tom.engine.adt.tomslot.types.PairNameDecl) ) {if ( ((( tom.engine.adt.tomslot.types.PairNameDecl )pair) instanceof tom.engine.adt.tomslot.types.PairNameDecl) ) {if ( ((( tom.engine.adt.tomslot.types.PairNameDecl )(( tom.engine.adt.tomslot.types.PairNameDecl )pair)) instanceof tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl) ) {

                      if( (( tom.engine.adt.tomslot.types.PairNameDecl )pair).getSlotDecl() !=  tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration.make() ) {
                        msg = TomMessage.errorTwoSameSlotDecl;
                      }
                    }}}}}

                }
                if(msg != null) {
                  TomMessage.error(getLogger(),ot.getfileName(), ot.getstartLine(),
                      msg,
                      ot2.getfileName(), ot2.getstartLine(),
                      "%op "+ tom_codomain, ot.getstartLine(),sName.getString());
                } else {
                  pairNameDeclList.set(index, tom.engine.adt.tomslot.types.pairnamedecl.PairNameDecl.make(sName, attribute) );
                }
              }}}}}}}}}}}}}{if ( (tom_operator instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )tom_operator) instanceof tom.engine.adt.cst.types.CstOperator) ) {if ( ((( tom.engine.adt.cst.types.CstOperator )(( tom.engine.adt.cst.types.CstOperator )tom_operator)) instanceof tom.engine.adt.cst.types.cstoperator.Cst_Make) ) { tom.engine.adt.cst.types.CstBlockList  tomMatch295_34= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getblock() ; tom.engine.adt.cst.types.CstNameList  tom_nameList= (( tom.engine.adt.cst.types.CstOperator )tom_operator).getnameList() ;if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch295_34) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch295_34) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch295_34.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch295_41= tomMatch295_34.getHeadConcCstBlock() ;if ( (tomMatch295_41 instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch295_41) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) {if (  tomMatch295_34.getTailConcCstBlock() .isEmptyConcCstBlock() ) {


                CstOption ot2 = getOriginTracking( tomMatch295_41.getoptionList() );
                ArrayList<String> varnameList = new ArrayList<String>();
                BQTermList args =  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
                int index = 0;

                {{if ( (tom_nameList instanceof tom.engine.adt.cst.types.CstNameList) ) {if ( (((( tom.engine.adt.cst.types.CstNameList )(( tom.engine.adt.cst.types.CstNameList )tom_nameList)) instanceof tom.engine.adt.cst.types.cstnamelist.ConsConcCstName) || ((( tom.engine.adt.cst.types.CstNameList )(( tom.engine.adt.cst.types.CstNameList )tom_nameList)) instanceof tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName)) ) { tom.engine.adt.cst.types.CstNameList  tomMatch297_end_4=(( tom.engine.adt.cst.types.CstNameList )tom_nameList);do {{if (!( tomMatch297_end_4.isEmptyConcCstName() )) { tom.engine.adt.cst.types.CstName  tomMatch297_8= tomMatch297_end_4.getHeadConcCstName() ;if ( (tomMatch297_8 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch297_8) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom_argName= tomMatch297_8.getname() ;

                    varnameList.add(tom_argName);
                    TomType type = (types.length()>0)?TomBase.elementAt(types,index++): tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
                    args = tom_append_list_concBQTerm(args, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make(tom_argName) , type) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) );
                  }}}if ( tomMatch297_end_4.isEmptyConcCstName() ) {tomMatch297_end_4=(( tom.engine.adt.cst.types.CstNameList )tom_nameList);} else {tomMatch297_end_4= tomMatch297_end_4.getTailConcCstName() ;}}} while(!( (tomMatch297_end_4==(( tom.engine.adt.cst.types.CstNameList )tom_nameList)) ));}}}}


                String[] vars = new String[varnameList.size()]; // used only to give a type
                String code = ASTFactory.abstractCode( tomMatch295_41.getcontent() ,varnameList.toArray(vars));

                Declaration attribute =  tom.engine.adt.tomdeclaration.types.declaration.MakeDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_opName) ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , tom_codomain,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) , args,  tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction.make( tom.engine.adt.tomexpression.types.expression.Code.make(code) ) ,  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_opName) , ot2.getstartLine(), ot2.getfileName()) 
                    ) 





;

                options.add( tom.engine.adt.tomoption.types.option.DeclarationToOption.make(attribute) ); 
              }}}}}}}}}}



          }if ( tomMatch294_end_4.isEmptyConcCstOperator() ) {tomMatch294_end_4=(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList);} else {tomMatch294_end_4= tomMatch294_end_4.getTailConcCstOperator() ;}}} while(!( (tomMatch294_end_4==(( tom.engine.adt.cst.types.CstOperatorList )tom_operatorList)) ));}}}}

        TomSymbol astSymbol = ASTFactory.makeSymbol(tom_opName,  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , tom_codomain,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) , types, ASTFactory.makePairNameDeclList(pairNameDeclList), options);
        symbolTable.putSymbol(tom_opName,astSymbol);
        return  tom.engine.adt.code.types.code.DeclarationToCode.make( tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl.make( tom.engine.adt.tomname.types.tomname.Name.make(tom_opName) ) ) ;
      }}}}}}}}}

 // end %match
   
    return  tom.engine.adt.code.types.code.Tom.make( tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) ;
    //throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQTerm convert(CstBQTerm cst) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )(( tom.engine.adt.cst.types.CstBQTerm )cst)) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQDefault) ) {

 
        return  tom.engine.adt.code.types.bqterm.BQDefault.make() ; 
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )(( tom.engine.adt.cst.types.CstBQTerm )cst)) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQAppl) ) { String  tom_name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;


        return  tom.engine.adt.code.types.bqterm.BQAppl.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom_name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) , convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getbqTermList() )) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )(( tom.engine.adt.cst.types.CstBQTerm )cst)) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQRecordAppl) ) { String  tom_name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;


        return  tom.engine.adt.code.types.bqterm.BQRecordAppl.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom_name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) , convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getslotList() )) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )(( tom.engine.adt.cst.types.CstBQTerm )cst)) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQVar) ) { tom.engine.adt.cst.types.CstType  tomMatch298_18= (( tom.engine.adt.cst.types.CstBQTerm )cst).gettype() ; String  tom_name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;if ( (tomMatch298_18 instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tomMatch298_18) instanceof tom.engine.adt.cst.types.csttype.Cst_TypeUnknown) ) {


        return  tom.engine.adt.code.types.bqterm.BQVariable.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom_name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) , SymbolTable.TYPE_UNKNOWN) ;
      }}}}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )(( tom.engine.adt.cst.types.CstBQTerm )cst)) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQVar) ) { tom.engine.adt.cst.types.CstType  tomMatch298_26= (( tom.engine.adt.cst.types.CstBQTerm )cst).gettype() ; String  tom_name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;if ( (tomMatch298_26 instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tomMatch298_26) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) {


        return  tom.engine.adt.code.types.bqterm.BQVariable.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom_name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ,  tomMatch298_26.gettype() ,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ) ;
      }}}}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )(( tom.engine.adt.cst.types.CstBQTerm )cst)) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQVarStar) ) { tom.engine.adt.cst.types.CstType  tomMatch298_35= (( tom.engine.adt.cst.types.CstBQTerm )cst).gettype() ; String  tom_name= (( tom.engine.adt.cst.types.CstBQTerm )cst).getname() ;if ( (tomMatch298_35 instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tomMatch298_35) instanceof tom.engine.adt.cst.types.csttype.Cst_TypeUnknown) ) {


        return  tom.engine.adt.code.types.bqterm.BQVariableStar.make(addDefaultModule(convert( (( tom.engine.adt.cst.types.CstBQTerm )cst).getoptionList() ,tom_name)),  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) , SymbolTable.TYPE_UNKNOWN) ;
      }}}}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )cst) instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )(( tom.engine.adt.cst.types.CstBQTerm )cst)) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite) ) { tom.engine.adt.cst.types.CstBQTermList  tom_argList= (( tom.engine.adt.cst.types.CstBQTerm )cst).getlist() ;


        BQTerm composite =  tom.engine.adt.code.types.bqterm.EmptyComposite.make() ;
        {{if ( (tom_argList instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )(( tom.engine.adt.cst.types.CstBQTermList )tom_argList)) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )(( tom.engine.adt.cst.types.CstBQTermList )tom_argList)) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) { tom.engine.adt.cst.types.CstBQTermList  tomMatch299_end_4=(( tom.engine.adt.cst.types.CstBQTermList )tom_argList);do {{if (!( tomMatch299_end_4.isEmptyConcCstBQTerm() )) { tom.engine.adt.cst.types.CstBQTerm  tom_bqterm= tomMatch299_end_4.getHeadConcCstBQTerm() ;{{if ( (tom_bqterm instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )tom_bqterm) instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )(( tom.engine.adt.cst.types.CstBQTerm )tom_bqterm)) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_ITL) ) {


 composite = tom_append_list_Composite(composite, tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeTL.make( tom.engine.adt.code.types.targetlanguage.ITL.make( (( tom.engine.adt.cst.types.CstBQTerm )tom_bqterm).getcode() ) ) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ); }}}}{if ( (tom_bqterm instanceof tom.engine.adt.cst.types.CstBQTerm) ) {boolean tomMatch300_8= false ;if ( ((( tom.engine.adt.cst.types.CstBQTerm )tom_bqterm) instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )(( tom.engine.adt.cst.types.CstBQTerm )tom_bqterm)) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_ITL) ) {tomMatch300_8= true ;}}if (!(tomMatch300_8)) {
 composite = tom_append_list_Composite(composite, tom.engine.adt.code.types.bqterm.ConsComposite.make( tom.engine.adt.code.types.compositemember.CompositeBQTerm.make(convert(tom_bqterm)) , tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) ); }}}}

          }if ( tomMatch299_end_4.isEmptyConcCstBQTerm() ) {tomMatch299_end_4=(( tom.engine.adt.cst.types.CstBQTermList )tom_argList);} else {tomMatch299_end_4= tomMatch299_end_4.getTailConcCstBQTerm() ;}}} while(!( (tomMatch299_end_4==(( tom.engine.adt.cst.types.CstBQTermList )tom_argList)) ));}}}}

        return composite;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQSlot convert(CstPairSlotBQTerm cst) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstPairSlotBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstPairSlotBQTerm )cst) instanceof tom.engine.adt.cst.types.CstPairSlotBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstPairSlotBQTerm )(( tom.engine.adt.cst.types.CstPairSlotBQTerm )cst)) instanceof tom.engine.adt.cst.types.cstpairslotbqterm.Cst_PairSlotBQTerm) ) { tom.engine.adt.cst.types.CstName  tomMatch301_2= (( tom.engine.adt.cst.types.CstPairSlotBQTerm )cst).getslotName() ;if ( (tomMatch301_2 instanceof tom.engine.adt.cst.types.CstName) ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch301_2) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) {

        return  tom.engine.adt.tomslot.types.bqslot.PairSlotBQTerm.make( tom.engine.adt.tomname.types.tomname.Name.make( tomMatch301_2.getname() ) , convert( (( tom.engine.adt.cst.types.CstPairSlotBQTerm )cst).getterm() )) ;
      }}}}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Option convert(CstOption cst, String subject) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstOption) ) {if ( ((( tom.engine.adt.cst.types.CstOption )cst) instanceof tom.engine.adt.cst.types.CstOption) ) {if ( ((( tom.engine.adt.cst.types.CstOption )(( tom.engine.adt.cst.types.CstOption )cst)) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {

        return  tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(subject) ,  (( tom.engine.adt.cst.types.CstOption )cst).getstartLine() ,  (( tom.engine.adt.cst.types.CstOption )cst).getfileName() ) ;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public ConstraintInstruction convert(CstConstraintAction cst, CstBQTermList subjectList) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstConstraintAction) ) {if ( ((( tom.engine.adt.cst.types.CstConstraintAction )cst) instanceof tom.engine.adt.cst.types.CstConstraintAction) ) {if ( ((( tom.engine.adt.cst.types.CstConstraintAction )(( tom.engine.adt.cst.types.CstConstraintAction )cst)) instanceof tom.engine.adt.cst.types.cstconstraintaction.Cst_ConstraintAction) ) {

        int currentIndex = 0; // index of the current subject of subjectList
        OptionList newoptionList = convert( (( tom.engine.adt.cst.types.CstConstraintAction )cst).getoptionList() ,"ConstraintAction");
        //newoptionList = `concOption(newoptionList*);
        return  tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(convert( (( tom.engine.adt.cst.types.CstConstraintAction )cst).getconstraint() ,subjectList,currentIndex),  tom.engine.adt.tominstruction.types.instruction.CodeToInstruction.make( tom.engine.adt.code.types.code.Tom.make(convert( (( tom.engine.adt.cst.types.CstConstraintAction )cst).getaction() )) ) , newoptionList) 


;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public Constraint convert(CstConstraint cst, CstBQTermList subjectList, int subjectIndex) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )(( tom.engine.adt.cst.types.CstConstraint )cst)) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_MatchArgumentConstraint) ) {

        CstBQTermList l = subjectList;
        if (0 > subjectIndex || subjectIndex > subjectList.length()) {
          throw new IllegalArgumentException("illegal list index: " + subjectIndex);
        }
        for (int i = 0; i < subjectIndex; i++) {
          l = l.getTailConcCstBQTerm();
        }
        BQTerm currentSubject = convert(l.getHeadConcCstBQTerm());
        TomType type = getTomType(currentSubject);
        return  tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getpattern() ), currentSubject, type) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )cst) instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( ((( tom.engine.adt.cst.types.CstConstraint )(( tom.engine.adt.cst.types.CstConstraint )cst)) instanceof tom.engine.adt.cst.types.cstconstraint.Cst_MatchTermConstraint) ) { tom.engine.adt.cst.types.CstType  tom_typeConstraint= (( tom.engine.adt.cst.types.CstConstraint )cst).gettype() ;


        BQTerm currentSubject = convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getsubject() );
        TomType type = null;
        {{if ( (tom_typeConstraint instanceof tom.engine.adt.cst.types.CstType) ) {
 type = getTomType(currentSubject); }}{if ( (tom_typeConstraint instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )tom_typeConstraint) instanceof tom.engine.adt.cst.types.CstType) ) {if ( ((( tom.engine.adt.cst.types.CstType )(( tom.engine.adt.cst.types.CstType )tom_typeConstraint)) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) {
 
            TomSymbol symbol = symbolTable.getSymbolFromName( (( tom.engine.adt.cst.types.CstType )tom_typeConstraint).gettype() );
            if(symbol!=null) {
              type = symbol.getTypesToType().getCodomain();
            }
          }}}}}

        return  tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(convert( (( tom.engine.adt.cst.types.CstConstraint )cst).getpattern() ), currentSubject, type) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( (((( tom.engine.adt.cst.types.CstConstraint )(( tom.engine.adt.cst.types.CstConstraint )cst)) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_AndConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )(( tom.engine.adt.cst.types.CstConstraint )cst)) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint)) ) {if (!( (  (( tom.engine.adt.cst.types.CstConstraint )cst).isEmptyCst_AndConstraint()  ||  ((( tom.engine.adt.cst.types.CstConstraint )cst)== tom.engine.adt.cst.types.cstconstraint.EmptyCst_AndConstraint.make() )  ) )) {


        TomType type = SymbolTable.TYPE_UNKNOWN;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstConstraint) ) {if ( (((( tom.engine.adt.cst.types.CstConstraint )(( tom.engine.adt.cst.types.CstConstraint )cst)) instanceof tom.engine.adt.cst.types.cstconstraint.ConsCst_OrConstraint) || ((( tom.engine.adt.cst.types.CstConstraint )(( tom.engine.adt.cst.types.CstConstraint )cst)) instanceof tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint)) ) {if (!( (  (( tom.engine.adt.cst.types.CstConstraint )cst).isEmptyCst_OrConstraint()  ||  ((( tom.engine.adt.cst.types.CstConstraint )cst)== tom.engine.adt.cst.types.cstconstraint.EmptyCst_OrConstraint.make() )  ) )) {


        TomType type = SymbolTable.TYPE_UNKNOWN;
      }}}}}


    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomTerm convert(CstPattern cst) {
    // TODO: define option
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )(( tom.engine.adt.cst.types.CstPattern )cst)) instanceof tom.engine.adt.cst.types.cstpattern.Cst_Variable) ) {

        return  tom.engine.adt.tomterm.types.tomterm.Variable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstPattern )cst).getname() ) , SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )(( tom.engine.adt.cst.types.CstPattern )cst)) instanceof tom.engine.adt.cst.types.cstpattern.Cst_VariableStar) ) {

        return  tom.engine.adt.tomterm.types.tomterm.VariableStar.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstPattern )cst).getname() ) , SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )(( tom.engine.adt.cst.types.CstPattern )cst)) instanceof tom.engine.adt.cst.types.cstpattern.Cst_UnamedVariable) ) {

        return  tom.engine.adt.tomterm.types.tomterm.Variable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.EmptyName.make() , SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )(( tom.engine.adt.cst.types.CstPattern )cst)) instanceof tom.engine.adt.cst.types.cstpattern.Cst_UnamedVariableStar) ) {

        return  tom.engine.adt.tomterm.types.tomterm.VariableStar.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.EmptyName.make() , SymbolTable.TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )cst) instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )(( tom.engine.adt.cst.types.CstPattern )cst)) instanceof tom.engine.adt.cst.types.cstpattern.Cst_Appl) ) {


        OptionList optionList =  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
        TomNameList nameList = convert( (( tom.engine.adt.cst.types.CstPattern )cst).getheadSymbolList() ); 
        TomList argList = convert( (( tom.engine.adt.cst.types.CstPattern )cst).getpatternList() ); 
        ConstraintList constraintList =  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ;
        return  tom.engine.adt.tomterm.types.tomterm.TermAppl.make(optionList, nameList, argList, constraintList) ;
      }}}}}



    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomName convert(CstSymbol cst) {
    List optionList = new LinkedList();
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )(( tom.engine.adt.cst.types.CstSymbol )cst)) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_Symbol) ) {

        return  tom.engine.adt.tomname.types.tomname.Name.make( (( tom.engine.adt.cst.types.CstSymbol )cst).getname() ) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )(( tom.engine.adt.cst.types.CstSymbol )cst)) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_ConstantInt) ) { String  tom_name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;


        ASTFactory.makeIntegerSymbol(symbolTable,tom_name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )(( tom.engine.adt.cst.types.CstSymbol )cst)) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_ConstantLong) ) { String  tom_name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;

        ASTFactory.makeLongSymbol(symbolTable,tom_name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )(( tom.engine.adt.cst.types.CstSymbol )cst)) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_ConstantChar) ) { String  tom_name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;

        ASTFactory.makeCharSymbol(symbolTable,tom_name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )(( tom.engine.adt.cst.types.CstSymbol )cst)) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_ConstantDouble) ) { String  tom_name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;

        ASTFactory.makeDoubleSymbol(symbolTable,tom_name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )cst) instanceof tom.engine.adt.cst.types.CstSymbol) ) {if ( ((( tom.engine.adt.cst.types.CstSymbol )(( tom.engine.adt.cst.types.CstSymbol )cst)) instanceof tom.engine.adt.cst.types.cstsymbol.Cst_ConstantString) ) { String  tom_name= (( tom.engine.adt.cst.types.CstSymbol )cst).getvalue() ;

        ASTFactory.makeStringSymbol(symbolTable,tom_name,optionList);
        return  tom.engine.adt.tomname.types.tomname.Name.make(tom_name) ;
      }}}}}



    throw new TomRuntimeException("convert: strange term: " + cst);
  }


  /*
   * List conversion
   */

  public CodeList convert(CstBlockList cst) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )cst)) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )cst)) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if ( (( tom.engine.adt.cst.types.CstBlockList )cst).isEmptyConcCstBlock() ) {
 
        return  tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )cst)) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )(( tom.engine.adt.cst.types.CstBlockList )cst)) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( (( tom.engine.adt.cst.types.CstBlockList )cst).isEmptyConcCstBlock() )) {

        return  tom.engine.adt.code.types.codelist.ConsconcCode.make(convert( (( tom.engine.adt.cst.types.CstBlockList )cst).getHeadConcCstBlock() ),tom_append_list_concCode(convert( (( tom.engine.adt.cst.types.CstBlockList )cst).getTailConcCstBlock() ), tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQTermList convert(CstBQTermList cst) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )(( tom.engine.adt.cst.types.CstBQTermList )cst)) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )(( tom.engine.adt.cst.types.CstBQTermList )cst)) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) {if ( (( tom.engine.adt.cst.types.CstBQTermList )cst).isEmptyConcCstBQTerm() ) {
 
        return  tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )(( tom.engine.adt.cst.types.CstBQTermList )cst)) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )(( tom.engine.adt.cst.types.CstBQTermList )cst)) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) {if (!( (( tom.engine.adt.cst.types.CstBQTermList )cst).isEmptyConcCstBQTerm() )) {

        return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(convert( (( tom.engine.adt.cst.types.CstBQTermList )cst).getHeadConcCstBQTerm() ),tom_append_list_concBQTerm(convert( (( tom.engine.adt.cst.types.CstBQTermList )cst).getTailConcCstBQTerm() ), tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public BQSlotList convert(CstPairSlotBQTermList cst) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstPairSlotBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstPairSlotBQTermList )(( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst)) instanceof tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm) || ((( tom.engine.adt.cst.types.CstPairSlotBQTermList )(( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst)) instanceof tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm)) ) {if ( (( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst).isEmptyConcCstPairSlotBQTerm() ) {
 
        return  tom.engine.adt.tomslot.types.bqslotlist.EmptyconcBQSlot.make() ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstPairSlotBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstPairSlotBQTermList )(( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst)) instanceof tom.engine.adt.cst.types.cstpairslotbqtermlist.ConsConcCstPairSlotBQTerm) || ((( tom.engine.adt.cst.types.CstPairSlotBQTermList )(( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst)) instanceof tom.engine.adt.cst.types.cstpairslotbqtermlist.EmptyConcCstPairSlotBQTerm)) ) {if (!( (( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst).isEmptyConcCstPairSlotBQTerm() )) {

        return  tom.engine.adt.tomslot.types.bqslotlist.ConsconcBQSlot.make(convert( (( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst).getHeadConcCstPairSlotBQTerm() ),tom_append_list_concBQSlot(convert( (( tom.engine.adt.cst.types.CstPairSlotBQTermList )cst).getTailConcCstPairSlotBQTerm() ), tom.engine.adt.tomslot.types.bqslotlist.EmptyconcBQSlot.make() )) ;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public OptionList convert(CstOptionList cst, String subject) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstOptionList) ) {if ( (((( tom.engine.adt.cst.types.CstOptionList )(( tom.engine.adt.cst.types.CstOptionList )cst)) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )(( tom.engine.adt.cst.types.CstOptionList )cst)) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if ( (( tom.engine.adt.cst.types.CstOptionList )cst).isEmptyConcCstOption() ) {
 
        return  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstOptionList) ) {if ( (((( tom.engine.adt.cst.types.CstOptionList )(( tom.engine.adt.cst.types.CstOptionList )cst)) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )(( tom.engine.adt.cst.types.CstOptionList )cst)) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( (( tom.engine.adt.cst.types.CstOptionList )cst).isEmptyConcCstOption() )) {

        OptionList ol = convert( (( tom.engine.adt.cst.types.CstOptionList )cst).getTailConcCstOption() ,subject);
        return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make(convert( (( tom.engine.adt.cst.types.CstOptionList )cst).getHeadConcCstOption() ,subject),tom_append_list_concOption(ol, tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() )) ;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public ConstraintInstructionList convert(CstConstraintActionList cst, CstBQTermList subjectList) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstConstraintActionList) ) {if ( (((( tom.engine.adt.cst.types.CstConstraintActionList )(( tom.engine.adt.cst.types.CstConstraintActionList )cst)) instanceof tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction) || ((( tom.engine.adt.cst.types.CstConstraintActionList )(( tom.engine.adt.cst.types.CstConstraintActionList )cst)) instanceof tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction)) ) {if ( (( tom.engine.adt.cst.types.CstConstraintActionList )cst).isEmptyConcCstConstraintAction() ) {
 
        return  tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstConstraintActionList) ) {if ( (((( tom.engine.adt.cst.types.CstConstraintActionList )(( tom.engine.adt.cst.types.CstConstraintActionList )cst)) instanceof tom.engine.adt.cst.types.cstconstraintactionlist.ConsConcCstConstraintAction) || ((( tom.engine.adt.cst.types.CstConstraintActionList )(( tom.engine.adt.cst.types.CstConstraintActionList )cst)) instanceof tom.engine.adt.cst.types.cstconstraintactionlist.EmptyConcCstConstraintAction)) ) {if (!( (( tom.engine.adt.cst.types.CstConstraintActionList )cst).isEmptyConcCstConstraintAction() )) {

        ConstraintInstructionList ol = convert( (( tom.engine.adt.cst.types.CstConstraintActionList )cst).getTailConcCstConstraintAction() ,subjectList);
        return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make(convert( (( tom.engine.adt.cst.types.CstConstraintActionList )cst).getHeadConcCstConstraintAction() ,subjectList),tom_append_list_concConstraintInstruction(ol, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomNameList convert(CstSymbolList cst) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstSymbolList) ) {if ( (((( tom.engine.adt.cst.types.CstSymbolList )(( tom.engine.adt.cst.types.CstSymbolList )cst)) instanceof tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol) || ((( tom.engine.adt.cst.types.CstSymbolList )(( tom.engine.adt.cst.types.CstSymbolList )cst)) instanceof tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol)) ) {if ( (( tom.engine.adt.cst.types.CstSymbolList )cst).isEmptyConcCstSymbol() ) {
 
        return  tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstSymbolList) ) {if ( (((( tom.engine.adt.cst.types.CstSymbolList )(( tom.engine.adt.cst.types.CstSymbolList )cst)) instanceof tom.engine.adt.cst.types.cstsymbollist.ConsConcCstSymbol) || ((( tom.engine.adt.cst.types.CstSymbolList )(( tom.engine.adt.cst.types.CstSymbolList )cst)) instanceof tom.engine.adt.cst.types.cstsymbollist.EmptyConcCstSymbol)) ) {if (!( (( tom.engine.adt.cst.types.CstSymbolList )cst).isEmptyConcCstSymbol() )) {

        return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(convert( (( tom.engine.adt.cst.types.CstSymbolList )cst).getHeadConcCstSymbol() ),tom_append_list_concTomName(convert( (( tom.engine.adt.cst.types.CstSymbolList )cst).getTailConcCstSymbol() ), tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() )) ;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  public TomList convert(CstPatternList cst) {
    {{if ( (cst instanceof tom.engine.adt.cst.types.CstPatternList) ) {if ( (((( tom.engine.adt.cst.types.CstPatternList )(( tom.engine.adt.cst.types.CstPatternList )cst)) instanceof tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern) || ((( tom.engine.adt.cst.types.CstPatternList )(( tom.engine.adt.cst.types.CstPatternList )cst)) instanceof tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern)) ) {if ( (( tom.engine.adt.cst.types.CstPatternList )cst).isEmptyConcCstPattern() ) {
 
        return  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
      }}}}{if ( (cst instanceof tom.engine.adt.cst.types.CstPatternList) ) {if ( (((( tom.engine.adt.cst.types.CstPatternList )(( tom.engine.adt.cst.types.CstPatternList )cst)) instanceof tom.engine.adt.cst.types.cstpatternlist.ConsConcCstPattern) || ((( tom.engine.adt.cst.types.CstPatternList )(( tom.engine.adt.cst.types.CstPatternList )cst)) instanceof tom.engine.adt.cst.types.cstpatternlist.EmptyConcCstPattern)) ) {if (!( (( tom.engine.adt.cst.types.CstPatternList )cst).isEmptyConcCstPattern() )) {

        return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(convert( (( tom.engine.adt.cst.types.CstPatternList )cst).getHeadConcCstPattern() ),tom_append_list_concTomTerm(convert( (( tom.engine.adt.cst.types.CstPatternList )cst).getTailConcCstPattern() ), tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
      }}}}}

    throw new TomRuntimeException("convert: strange term: " + cst);
  }

  /*
   * Utilities
   */
  private CstOption getOriginTracking(CstOptionList optionlist) {
    {{if ( (optionlist instanceof tom.engine.adt.cst.types.CstOptionList) ) {if ( (((( tom.engine.adt.cst.types.CstOptionList )(( tom.engine.adt.cst.types.CstOptionList )optionlist)) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )(( tom.engine.adt.cst.types.CstOptionList )optionlist)) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch315_end_4=(( tom.engine.adt.cst.types.CstOptionList )optionlist);do {{if (!( tomMatch315_end_4.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch315_7= tomMatch315_end_4.getHeadConcCstOption() ;if ( (tomMatch315_7 instanceof tom.engine.adt.cst.types.CstOption) ) {if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch315_7) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {
 return  tomMatch315_end_4.getHeadConcCstOption() ; }}}if ( tomMatch315_end_4.isEmptyConcCstOption() ) {tomMatch315_end_4=(( tom.engine.adt.cst.types.CstOptionList )optionlist);} else {tomMatch315_end_4= tomMatch315_end_4.getTailConcCstOption() ;}}} while(!( (tomMatch315_end_4==(( tom.engine.adt.cst.types.CstOptionList )optionlist)) ));}}}}

    throw new TomRuntimeException("info not found: " + optionlist);
  }

  private BQTerm makeBQVariableFromName(String name, String type, CstOptionList optionList) {
    //if more information is needed: concOption(OriginTracking(Name(name),getStartLine(optionList),getFileName(optionList))),
    return  tom.engine.adt.code.types.bqterm.BQVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make(name) ,  tom.engine.adt.tomtype.types.tomtype.Type.make( tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() , type,  tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType.make() ) ) 


;
  }

  private HashMap<String,String> usedSlots = new HashMap<String,String>();
  private void putSlotType(String codomain, String slotName, String slotType) {
    String key = codomain+slotName;
    usedSlots.put(key,slotType);
  }

  private String getSlotType(String codomain, String slotName) {
    String key = codomain+slotName;
    return usedSlots.get(key);
  }

  private OptionList addDefaultModule(OptionList ol) {
    return tom_append_list_concOption(ol, tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.ModuleName.make("default") , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) );
  }

  private TomType getTomType(BQTerm bqt) {
    {{if ( (bqt instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch316_5= false ; tom.engine.adt.code.types.BQTerm  tomMatch316_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch316_3= null ; tom.engine.adt.tomtype.types.TomType  tomMatch316_1= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqt) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqt)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch316_5= true ;tomMatch316_3=(( tom.engine.adt.code.types.BQTerm )bqt);tomMatch316_1= tomMatch316_3.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqt) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqt)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch316_5= true ;tomMatch316_4=(( tom.engine.adt.code.types.BQTerm )bqt);tomMatch316_1= tomMatch316_4.getAstType() ;}}}}}if (tomMatch316_5) {
 
        return tomMatch316_1; 
      }}}{if ( (bqt instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch316_14= false ; tom.engine.adt.code.types.BQTerm  tomMatch316_9= null ; tom.engine.adt.code.types.BQTerm  tomMatch316_10= null ; tom.engine.adt.tomname.types.TomName  tomMatch316_7= null ;if ( ((( tom.engine.adt.code.types.BQTerm )bqt) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqt)) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {{tomMatch316_14= true ;tomMatch316_9=(( tom.engine.adt.code.types.BQTerm )bqt);tomMatch316_7= tomMatch316_9.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )bqt) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )bqt)) instanceof tom.engine.adt.code.types.bqterm.BQRecordAppl) ) {{tomMatch316_14= true ;tomMatch316_10=(( tom.engine.adt.code.types.BQTerm )bqt);tomMatch316_7= tomMatch316_10.getAstName() ;}}}}}if (tomMatch316_14) {if ( (tomMatch316_7 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch316_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        TomSymbol symbol = symbolTable.getSymbolFromName( tomMatch316_7.getString() );
        if(symbol!=null) {
          return symbol.getTypesToType().getCodomain();
        }
      }}}}}}


    return symbolTable.TYPE_UNKNOWN;
  }

}
