/*
 * Gom
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
 * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom;

import tom.gom.adt.symboltable.types.*;
import tom.gom.adt.symboltable.*;
import tom.gom.adt.gom.types.*;
import tom.library.sl.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import tom.gom.tools.GomEnvironment;

public class SymbolTable {

         private static   tom.gom.adt.gom.types.AlternativeList  tom_append_list_ConcAlternative( tom.gom.adt.gom.types.AlternativeList l1,  tom.gom.adt.gom.types.AlternativeList  l2) {     if( l1.isEmptyConcAlternative() ) {       return l2;     } else if( l2.isEmptyConcAlternative() ) {       return l1;     } else if(  l1.getTailConcAlternative() .isEmptyConcAlternative() ) {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,l2) ;     } else {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,tom_append_list_ConcAlternative( l1.getTailConcAlternative() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AlternativeList  tom_get_slice_ConcAlternative( tom.gom.adt.gom.types.AlternativeList  begin,  tom.gom.adt.gom.types.AlternativeList  end, tom.gom.adt.gom.types.AlternativeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAlternative()  ||  (end== tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( begin.getHeadConcAlternative() ,( tom.gom.adt.gom.types.AlternativeList )tom_get_slice_ConcAlternative( begin.getTailConcAlternative() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.AtomList  tom_append_list_ConcAtom( tom.gom.adt.gom.types.AtomList l1,  tom.gom.adt.gom.types.AtomList  l2) {     if( l1.isEmptyConcAtom() ) {       return l2;     } else if( l2.isEmptyConcAtom() ) {       return l1;     } else if(  l1.getTailConcAtom() .isEmptyConcAtom() ) {       return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( l1.getHeadConcAtom() ,l2) ;     } else {       return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( l1.getHeadConcAtom() ,tom_append_list_ConcAtom( l1.getTailConcAtom() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AtomList  tom_get_slice_ConcAtom( tom.gom.adt.gom.types.AtomList  begin,  tom.gom.adt.gom.types.AtomList  end, tom.gom.adt.gom.types.AtomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAtom()  ||  (end== tom.gom.adt.gom.types.atomlist.EmptyConcAtom.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( begin.getHeadConcAtom() ,( tom.gom.adt.gom.types.AtomList )tom_get_slice_ConcAtom( begin.getTailConcAtom() ,end,tail)) ;   }       private static   tom.gom.adt.symboltable.types.FieldDescriptionList  tom_append_list_concFieldDescription( tom.gom.adt.symboltable.types.FieldDescriptionList l1,  tom.gom.adt.symboltable.types.FieldDescriptionList  l2) {     if( l1.isEmptyconcFieldDescription() ) {       return l2;     } else if( l2.isEmptyconcFieldDescription() ) {       return l1;     } else if(  l1.getTailconcFieldDescription() .isEmptyconcFieldDescription() ) {       return  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( l1.getHeadconcFieldDescription() ,l2) ;     } else {       return  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( l1.getHeadconcFieldDescription() ,tom_append_list_concFieldDescription( l1.getTailconcFieldDescription() ,l2)) ;     }   }   private static   tom.gom.adt.symboltable.types.FieldDescriptionList  tom_get_slice_concFieldDescription( tom.gom.adt.symboltable.types.FieldDescriptionList  begin,  tom.gom.adt.symboltable.types.FieldDescriptionList  end, tom.gom.adt.symboltable.types.FieldDescriptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcFieldDescription()  ||  (end== tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( begin.getHeadconcFieldDescription() ,( tom.gom.adt.symboltable.types.FieldDescriptionList )tom_get_slice_concFieldDescription( begin.getTailconcFieldDescription() ,end,tail)) ;   }      private static   tom.gom.adt.symboltable.types.StringList  tom_append_list_StringList( tom.gom.adt.symboltable.types.StringList l1,  tom.gom.adt.symboltable.types.StringList  l2) {     if( l1.isEmptyStringList() ) {       return l2;     } else if( l2.isEmptyStringList() ) {       return l1;     } else if(  l1.getTailStringList() .isEmptyStringList() ) {       return  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( l1.getHeadStringList() ,l2) ;     } else {       return  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( l1.getHeadStringList() ,tom_append_list_StringList( l1.getTailStringList() ,l2)) ;     }   }   private static   tom.gom.adt.symboltable.types.StringList  tom_get_slice_StringList( tom.gom.adt.symboltable.types.StringList  begin,  tom.gom.adt.symboltable.types.StringList  end, tom.gom.adt.symboltable.types.StringList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyStringList()  ||  (end== tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( begin.getHeadStringList() ,( tom.gom.adt.symboltable.types.StringList )tom_get_slice_StringList( begin.getTailStringList() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}   



  /** map sort-name -> SortDescription */
  private Map<String,SortDescription> sorts =
    new HashMap<String,SortDescription>();

  /** map constructor-name -> ConstructorDescription */
  private Map<String,ConstructorDescription> constructors =
    new HashMap<String,ConstructorDescription>();

  private Graph<String> sortDependences = new Graph<String>();

  private GomEnvironment gomEnvironment;

  public SymbolTable(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void fill(GomModuleList gml) {
    { /* unamed block */{ /* unamed block */if ( (gml instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )gml) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )gml) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch495_end_4=(( tom.gom.adt.gom.types.GomModuleList )gml);do {{ /* unamed block */if (!( tomMatch495_end_4.isEmptyConcGomModule() )) {fillFromGomModule( tomMatch495_end_4.getHeadConcGomModule() )
; }if ( tomMatch495_end_4.isEmptyConcGomModule() ) {tomMatch495_end_4=(( tom.gom.adt.gom.types.GomModuleList )gml);} else {tomMatch495_end_4= tomMatch495_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch495_end_4==(( tom.gom.adt.gom.types.GomModuleList )gml)) ));}}}}

    computeSortDependences();
    isolateFreshSorts();
    fillAccessibleAtoms();
    generateConsAndNils();
  }

  public void addConstructor(String symbol, String codomain, FieldDescriptionList fields) {
    constructors.put(symbol, tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription.make(codomain, fields,  tom.gom.adt.symboltable.types.generationinfo.No.make() ) );
    SortDescription s = sorts.get(codomain);
    StringList l = s.getConstructors();
    sorts.put(codomain,s.setConstructors(tom_append_list_StringList(l, tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(symbol, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) )));
  }

  public void addVariadicConstructor(String symbol, String domain, String codomain) {
    constructors.put(symbol, tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription.make(codomain, domain, false) );
    SortDescription s = sorts.get(codomain);
    StringList l = s.getConstructors();
    sorts.put(codomain,s.setConstructors(tom_append_list_StringList(l, tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(symbol, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) )));
  }

  /**
   * returns the set of all constructor symbols
   **/
  public Set<String> getConstructors() {
    return new HashSet<String>(constructors.keySet());
  }

  /**
   * returns the set of all sort symbols
   **/
  public Set<String> getSorts() {
    return new HashSet<String>(sorts.keySet());
  }

  /**
   * clear all entries (sorts and constructors)
   */
  public void clear() {
    sorts.clear();
    constructors.clear();
  }

  public String getFullModuleName(String moduleName) {
    String packageName = gomEnvironment.getStreamManager().getPackagePath(moduleName);
    return (packageName.equals("") ? "" : packageName + ".")+moduleName.toLowerCase();
  }

  public String getFullAbstractTypeClassName(String moduleName) {
    return getFullModuleName(moduleName)+"."+moduleName+"AbstractType";
  }

  public String getFullSortClassName(String sort) {
    SortDescription desc = sorts.get(sort);
    if (null == desc) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException,
          sort);
      return null;
    }
    { /* unamed block */{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )desc) instanceof tom.gom.adt.symboltable.types.sortdescription.SortDescription) ) { String  tom___m= (( tom.gom.adt.symboltable.types.SortDescription )desc).getModuleSymbol() ;

        String packageName = gomEnvironment.getStreamManager().getPackagePath(tom___m);
        return (packageName.equals("") ? "" : packageName + ".")
          + tom___m.toLowerCase() + ".types." + sort;
      }}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.nonExhaustiveMatch);
    return null;
  }

  public String getFullConstructorClassName(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    if (null == desc) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException, cons);
      return null;
    }
    { /* unamed block */{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {

        return getFullSortClassName( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getSortSymbol() ).toLowerCase() + "." + cons;
      }}}{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

        return getFullSortClassName( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getSortSymbol() ).toLowerCase() + "." + cons;
      }}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.nonExhaustiveMatch);
    return null;
  }

  public void generateConsAndNils() {
    for(String c: getConstructors()) {
      if(!isVariadic(c)) { continue; }
      String dom = getDomain(c);
      String codom = getCoDomain(c);
      FieldDescriptionList ConsFields = null; // head and tail
      FieldDescriptionList NilFields =  tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ;
      /* codom binds X = ... | c(dom*) | ...
         => codom binds X = ... | Consc(Headc:dom,Tailc:codom) | ... */
      if(isPatternType(codom)) {
        ConsFields =  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Head"+c, dom,  tom.gom.adt.symboltable.types.status.SPattern.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Tail"+c, codom,  tom.gom.adt.symboltable.types.status.SPattern.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) ) 

;
        /* codom = ... | c(<dom>*) | ...
           => codom = ... | Consc(Headc:<dom>,Tailc:codom) | ... */
      } else if(isRefreshPoint(c)) {
        ConsFields =  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Head"+c, dom,  tom.gom.adt.symboltable.types.status.SRefreshPoint.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Tail"+c, codom,  tom.gom.adt.symboltable.types.status.SNone.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) ) 

;
        /* codom = ... | c(dom*) | ...
           => codom = ... | Consc(Headc:dom,Tailc:codom) | ... */
      } else {
        ConsFields =  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Head"+c, dom,  tom.gom.adt.symboltable.types.status.SNone.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Tail"+c, codom,  tom.gom.adt.symboltable.types.status.SNone.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) ) 

;
      }

      // add the new constructors to constructors map
      String nilc = "Empty" + c;
      String consc = "Cons" + c;
      constructors.put(nilc,
           tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription.make(codom, NilFields,  tom.gom.adt.symboltable.types.generationinfo.GenNil.make(c) ) );
      constructors.put(consc,
           tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription.make(codom, ConsFields,  tom.gom.adt.symboltable.types.generationinfo.GenCons.make(c) ) );

      // modify the codomain description
      SortDescription sd = sorts.get(codom);
      StringList conslist = sd.getConstructors();
      conslist = tom_append_list_StringList(conslist, tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(nilc, tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(consc, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) ) );
      sd = sd.setConstructors(conslist);
      sorts.put(codom,sd);
    }
  }

  public boolean isVariadic(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      { /* unamed block */{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException,
          cons);
      return false;
    }
  }

  public boolean isGenerated(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      { /* unamed block */{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {boolean tomMatch499_6= false ;if ( ((( tom.gom.adt.symboltable.types.GenerationInfo ) (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getGenerated() ) instanceof tom.gom.adt.symboltable.types.generationinfo.No) ) {tomMatch499_6= true ;}if (!(tomMatch499_6)) {

          return true;
        }}}}}

      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException,
          cons);
      return false;
    }
  }

  public boolean isGeneratedCons(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      { /* unamed block */{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo ) (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getGenerated() ) instanceof tom.gom.adt.symboltable.types.generationinfo.GenCons) ) {

          return true;
        }}}}}

      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException,
          cons);
      return false;
    }
  }

  public boolean isGeneratedNil(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      { /* unamed block */{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo ) (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getGenerated() ) instanceof tom.gom.adt.symboltable.types.generationinfo.GenNil) ) {

          return true;
        }}}}}

      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException, cons);
      return false;
    }
  }

  public String toString() {
    StringBuffer buf = new StringBuffer();
    for (Map.Entry<String,SortDescription> e: sorts.entrySet()) {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try {
        tom.library.utils.Viewer.toTree(e.getValue(),swriter);
      } catch (java.io.IOException ex) {
        ex.printStackTrace();
      }
      buf.append("sort " + e.getKey() + ":\n" + swriter + "\n");
    }
    for (Map.Entry<String,ConstructorDescription> e: constructors.entrySet()) {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try {
        tom.library.utils.Viewer.toTree(e.getValue(),swriter);
      } catch(java.io.IOException ex) {
        ex.printStackTrace();
      }
      buf.append("constructor " + e.getKey() + ":\n" + swriter + "\n");
    }
    return buf.toString();
  }

  private void fillFromGomModule(GomModule m) {
    { /* unamed block */{ /* unamed block */if ( (m instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )m) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch502_1= (( tom.gom.adt.gom.types.GomModule )m).getModuleName() ; tom.gom.adt.gom.types.SectionList  tomMatch502_2= (( tom.gom.adt.gom.types.GomModule )m).getSectionList() ;if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch502_1) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch502_2) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch502_2) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch502_end_11=tomMatch502_2;do {{ /* unamed block */if (!( tomMatch502_end_11.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch502_15= tomMatch502_end_11.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch502_15) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.ProductionList  tomMatch502_14= tomMatch502_15.getProductionList() ;if ( (((( tom.gom.adt.gom.types.ProductionList )tomMatch502_14) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tomMatch502_14) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch502_end_20=tomMatch502_14;do {{ /* unamed block */if (!( tomMatch502_end_20.isEmptyConcProduction() )) {fillFromProduction( tomMatch502_1.getName() , tomMatch502_end_20.getHeadConcProduction() )


;
        }if ( tomMatch502_end_20.isEmptyConcProduction() ) {tomMatch502_end_20=tomMatch502_14;} else {tomMatch502_end_20= tomMatch502_end_20.getTailConcProduction() ;}}} while(!( (tomMatch502_end_20==tomMatch502_14) ));}}}if ( tomMatch502_end_11.isEmptyConcSection() ) {tomMatch502_end_11=tomMatch502_2;} else {tomMatch502_end_11= tomMatch502_end_11.getTailConcSection() ;}}} while(!( (tomMatch502_end_11==tomMatch502_2) ));}}}}}}

  }

  private static StringList getConstructors(AlternativeList al) {
    StringList res =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
    { /* unamed block */{ /* unamed block */if ( (al instanceof tom.gom.adt.gom.types.AlternativeList) ) {if ( (((( tom.gom.adt.gom.types.AlternativeList )al) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )al) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch503_end_4=(( tom.gom.adt.gom.types.AlternativeList )al);do {{ /* unamed block */if (!( tomMatch503_end_4.isEmptyConcAlternative() )) { tom.gom.adt.gom.types.Alternative  tomMatch503_8= tomMatch503_end_4.getHeadConcAlternative() ;if ( ((( tom.gom.adt.gom.types.Alternative )tomMatch503_8) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) {

        res =  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( tomMatch503_8.getName() , res) ;
      }}if ( tomMatch503_end_4.isEmptyConcAlternative() ) {tomMatch503_end_4=(( tom.gom.adt.gom.types.AlternativeList )al);} else {tomMatch503_end_4= tomMatch503_end_4.getTailConcAlternative() ;}}} while(!( (tomMatch503_end_4==(( tom.gom.adt.gom.types.AlternativeList )al)) ));}}}}

    return res;
  }

  /* Methods only used by freshgom */

  /**
   * returns only sorts concerned by freshGom
   */
  public Set<String> getFreshSorts() {
    Set<String> res = getSorts();
    Iterator<String> it = res.iterator();
    while (it.hasNext()) {
      if (!isFreshType(it.next())) {
        it.remove();
      }
    }
    return res;
  }


  /**
   * returns the set of all constructor symbols
   * concerned by freshgom
   **/
  public Set<String> getFreshConstructors() {
    HashSet<String> res = new HashSet<String>();
    for (String c: constructors.keySet()) {
      if (isFreshType(getSort(c))) {
        res.add(c);
      }
    }
    return res;
  }


  public static String rawSort(String s) {
    return "Raw" + s;
  }

  public String rawCons(String c) {
    if (isGenerated(c)) {
      { /* unamed block */{ /* unamed block */ tom.gom.adt.symboltable.types.GenerationInfo  tomMatch504_0=getGenerated(c);if ( (tomMatch504_0 instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch504_0) instanceof tom.gom.adt.symboltable.types.generationinfo.GenCons) ) {
 return "ConsRaw" +  (( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch504_0).getBaseName() ; }}}{ /* unamed block */ tom.gom.adt.symboltable.types.GenerationInfo  tomMatch504_4=getGenerated(c);if ( (tomMatch504_4 instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch504_4) instanceof tom.gom.adt.symboltable.types.generationinfo.GenNil) ) {
 return "EmptyRaw" +  (( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch504_4).getBaseName() ; }}}}

    }
    return "Raw" + c;
  }

  public String qualifiedRawSortId(String sort) {
    SortDescription desc = sorts.get(sort);
    if (null == desc) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return null;
    }
    { /* unamed block */{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )desc) instanceof tom.gom.adt.symboltable.types.sortdescription.SortDescription) ) { String  tom___m= (( tom.gom.adt.symboltable.types.SortDescription )desc).getModuleSymbol() ;

        String packageName = gomEnvironment.getStreamManager().getPackagePath(tom___m);
        return (packageName.equals("") ? "" : packageName + ".")
          + tom___m.toLowerCase() + ".types." + rawSort(sort);
      }}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.nonExhaustiveMatch);
    return null;
  }

  public String qualifiedRawConstructorId(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    if (null == desc) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException, cons);
      return null;
    }
    { /* unamed block */{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {

        return qualifiedRawSortId( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getSortSymbol() ).toLowerCase() + "." + rawCons(cons);
      }}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.nonExhaustiveMatch);
    return null;
  }

  private static StringList convertBoundAtoms(AtomList al) {
    StringList res =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
    { /* unamed block */{ /* unamed block */if ( (al instanceof tom.gom.adt.gom.types.AtomList) ) {if ( (((( tom.gom.adt.gom.types.AtomList )al) instanceof tom.gom.adt.gom.types.atomlist.ConsConcAtom) || ((( tom.gom.adt.gom.types.AtomList )al) instanceof tom.gom.adt.gom.types.atomlist.EmptyConcAtom)) ) { tom.gom.adt.gom.types.AtomList  tomMatch507_end_4=(( tom.gom.adt.gom.types.AtomList )al);do {{ /* unamed block */if (!( tomMatch507_end_4.isEmptyConcAtom() )) {
 res =  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( tomMatch507_end_4.getHeadConcAtom() , res) ; }if ( tomMatch507_end_4.isEmptyConcAtom() ) {tomMatch507_end_4=(( tom.gom.adt.gom.types.AtomList )al);} else {tomMatch507_end_4= tomMatch507_end_4.getTailConcAtom() ;}}} while(!( (tomMatch507_end_4==(( tom.gom.adt.gom.types.AtomList )al)) ));}}}}

    return res;
  }

  private void fillFromProduction(String moduleName, Production p) {
    { /* unamed block */{ /* unamed block */if ( (p instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )p) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch508_1= (( tom.gom.adt.gom.types.Production )p).getType() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch508_1) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { tom.gom.adt.gom.types.TypeSpec  tom___spe= tomMatch508_1.getSpecialization() ; String  tom___n= tomMatch508_1.getName() ; tom.gom.adt.gom.types.AlternativeList  tom___al= (( tom.gom.adt.gom.types.Production )p).getAlternativeList() ;


          // filling sorts (except AccessibleAtoms)
          StringList cons = getConstructors(tom___al);
          StringList bound = convertBoundAtoms( (( tom.gom.adt.gom.types.Production )p).getBinds() );
          StringList empty =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
          FreshSortInfo info = null;
          { /* unamed block */{ /* unamed block */if ( (tom___spe instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )tom___spe) instanceof tom.gom.adt.gom.types.typespec.ExpressionType) ) {
 info =  tom.gom.adt.symboltable.types.freshsortinfo.ExpressionTypeInfo.make(empty) ; }}}{ /* unamed block */if ( (tom___spe instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )tom___spe) instanceof tom.gom.adt.gom.types.typespec.PatternType) ) {
 info =  tom.gom.adt.symboltable.types.freshsortinfo.PatternTypeInfo.make(bound, empty) ; }}}{ /* unamed block */if ( (tom___spe instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )tom___spe) instanceof tom.gom.adt.gom.types.typespec.AtomType) ) {
 info =  tom.gom.adt.symboltable.types.freshsortinfo.AtomTypeInfo.make() ; }}}}

          sorts.put(tom___n, tom.gom.adt.symboltable.types.sortdescription.SortDescription.make(cons, moduleName, info) );
          // filling constructors
          { /* unamed block */{ /* unamed block */if ( (tom___al instanceof tom.gom.adt.gom.types.AlternativeList) ) {if ( (((( tom.gom.adt.gom.types.AlternativeList )tom___al) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )tom___al) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch510_end_4=(( tom.gom.adt.gom.types.AlternativeList )tom___al);do {{ /* unamed block */if (!( tomMatch510_end_4.isEmptyConcAlternative() )) {fillCons(tom___n, tomMatch510_end_4.getHeadConcAlternative() )
; }if ( tomMatch510_end_4.isEmptyConcAlternative() ) {tomMatch510_end_4=(( tom.gom.adt.gom.types.AlternativeList )tom___al);} else {tomMatch510_end_4= tomMatch510_end_4.getTailConcAlternative() ;}}} while(!( (tomMatch510_end_4==(( tom.gom.adt.gom.types.AlternativeList )tom___al)) ));}}}}

        }}}}}

  }

  private void fillCons(String codom, Alternative p)  {
    { /* unamed block */{ /* unamed block */if ( (p instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )p) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { String  tom___n= (( tom.gom.adt.gom.types.Alternative )p).getName() ; tom.gom.adt.gom.types.FieldList  tom___dl= (( tom.gom.adt.gom.types.Alternative )p).getDomainList() ;{ /* unamed block */{ /* unamed block */if ( (tom___dl instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )tom___dl) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )tom___dl) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )tom___dl).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch512_5= (( tom.gom.adt.gom.types.FieldList )tom___dl).getHeadConcField() ;if ( ((( tom.gom.adt.gom.types.Field )tomMatch512_5) instanceof tom.gom.adt.gom.types.field.StarredField) ) { tom.gom.adt.gom.types.GomType  tomMatch512_3= tomMatch512_5.getFieldType() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch512_3) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {if (  (( tom.gom.adt.gom.types.FieldList )tom___dl).getTailConcField() .isEmptyConcField() ) {





            constructors.put(tom___n,
                 tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription.make(codom,  tomMatch512_3.getName() ,  tomMatch512_5.getSpecifier() .isRefresh()) );
            return;
          }}}}}}}{ /* unamed block */if ( (tom___dl instanceof tom.gom.adt.gom.types.FieldList) ) {

            FieldDescriptionList fl = getFieldList(codom,tom___dl);
            constructors.put(tom___n, tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription.make(codom, fl,  tom.gom.adt.symboltable.types.generationinfo.No.make() ) );
          }}}

      }}}}

  }

  private FieldDescriptionList getFieldList(String codom, FieldList dl) {
    FieldDescriptionList res =  tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ;
    { /* unamed block */{ /* unamed block */if ( (dl instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )dl) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )dl) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) { tom.gom.adt.gom.types.FieldList  tomMatch513_end_4=(( tom.gom.adt.gom.types.FieldList )dl);do {{ /* unamed block */if (!( tomMatch513_end_4.isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch513_10= tomMatch513_end_4.getHeadConcField() ;if ( ((( tom.gom.adt.gom.types.Field )tomMatch513_10) instanceof tom.gom.adt.gom.types.field.NamedField) ) { tom.gom.adt.gom.types.GomType  tomMatch513_9= tomMatch513_10.getFieldType() ; tom.gom.adt.gom.types.ScopeSpecifier  tom___spe= tomMatch513_10.getSpecifier() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch513_9) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {



        Status st = null;
        { /* unamed block */{ /* unamed block */if ( (tom___spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom___spe) instanceof tom.gom.adt.gom.types.scopespecifier.Outer) ) {
 st =  tom.gom.adt.symboltable.types.status.SOuter.make() ; }}}{ /* unamed block */if ( (tom___spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom___spe) instanceof tom.gom.adt.gom.types.scopespecifier.Inner) ) {
 st =  tom.gom.adt.symboltable.types.status.SInner.make() ; }}}{ /* unamed block */if ( (tom___spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom___spe) instanceof tom.gom.adt.gom.types.scopespecifier.Neutral) ) {
 st =  tom.gom.adt.symboltable.types.status.SNeutral.make() ; }}}{ /* unamed block */if ( (tom___spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom___spe) instanceof tom.gom.adt.gom.types.scopespecifier.None) ) {
 st = isPatternType(codom) ?  tom.gom.adt.symboltable.types.status.SPattern.make()  :  tom.gom.adt.symboltable.types.status.SNone.make() ; }}}{ /* unamed block */if ( (tom___spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom___spe) instanceof tom.gom.adt.gom.types.scopespecifier.Refresh) ) {
 st =  tom.gom.adt.symboltable.types.status.SRefreshPoint.make() ; }}}}

        FieldDescription desc =  tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make( tomMatch513_10.getName() ,  tomMatch513_9.getName() , st) ;
        res = tom_append_list_concFieldDescription(res, tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make(desc, tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) );
      }}}if ( tomMatch513_end_4.isEmptyConcField() ) {tomMatch513_end_4=(( tom.gom.adt.gom.types.FieldList )dl);} else {tomMatch513_end_4= tomMatch513_end_4.getTailConcField() ;}}} while(!( (tomMatch513_end_4==(( tom.gom.adt.gom.types.FieldList )dl)) ));}}}}

    return res;
  }

  private void setAccessibleAtoms(String sort, StringList atoms) {
    SortDescription desc = sorts.get(sort);
    SortDescription ndesc = null;
    { /* unamed block */{ /* unamed block */if ( (desc instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )desc) instanceof tom.gom.adt.symboltable.types.sortdescription.SortDescription) ) {

        ndesc = desc.setFreshInfo( (( tom.gom.adt.symboltable.types.SortDescription )desc).getFreshInfo() .setAccessibleAtoms(atoms));
      }}}}

    sorts.put(sort,ndesc);
  }

  private void setFreshSortInfo(String sort, FreshSortInfo i) {
    SortDescription desc = sorts.get(sort);
    sorts.put(sort,desc.setFreshInfo(i));
  }

  private GenerationInfo getGenerated(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    return desc.getGenerated();
  }

  /**
   * precondition : isGenerated(cons)
   */
  public String getBaseName(String cons) {
    return getGenerated(cons).getBaseName();
  }

  public boolean isExpressionType(String sort) {
    if (getGomEnvironment().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      { /* unamed block */{ /* unamed block */if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.freshsortinfo.ExpressionTypeInfo) ) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return false;
    }
  }

  public boolean isPatternType(String sort) {
    if (getGomEnvironment().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      { /* unamed block */{ /* unamed block */if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.freshsortinfo.PatternTypeInfo) ) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return false;
    }
  }

  public boolean isAtomType(String sort) {
    if (getGomEnvironment().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      { /* unamed block */{ /* unamed block */if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.freshsortinfo.AtomTypeInfo) ) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return false;
    }
  }

  /**
   * true if sort is concerned by freshgom
   **/
  public boolean isFreshType(String sort) {
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      { /* unamed block */{ /* unamed block */if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {boolean tomMatch519_3= false ;if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.freshsortinfo.NoFreshSort) ) {tomMatch519_3= true ;}if (!(tomMatch519_3)) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return false;
    }
  }

  public static class SetRefreshPoints extends tom.library.sl.AbstractStrategyBasic {private  tom.gom.SymbolTable  st;private  String  codom;public SetRefreshPoints( tom.gom.SymbolTable  st,  String  codom) {super(( new tom.library.sl.Identity() ));this.st=st;this.codom=codom;}public  tom.gom.SymbolTable  getst() {return st;}public  String  getcodom() {return codom;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {return ((T)visit_ConstructorDescription((( tom.gom.adt.symboltable.types.ConstructorDescription )v),introspector));}if ( (v instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {return ((T)visit_FieldDescription((( tom.gom.adt.symboltable.types.FieldDescription )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.symboltable.types.FieldDescription  _visit_FieldDescription( tom.gom.adt.symboltable.types.FieldDescription  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.symboltable.types.FieldDescription )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.symboltable.types.ConstructorDescription  _visit_ConstructorDescription( tom.gom.adt.symboltable.types.ConstructorDescription  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.symboltable.types.ConstructorDescription )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.symboltable.types.ConstructorDescription  visit_ConstructorDescription( tom.gom.adt.symboltable.types.ConstructorDescription  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {










        if(st.isExpressionType(codom) && st.isPatternType( (( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg).getDomain() )) {
          return (( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg).setIsRefreshPoint(true);
        }
      }}}}return _visit_ConstructorDescription(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.gom.adt.symboltable.types.FieldDescription  visit_FieldDescription( tom.gom.adt.symboltable.types.FieldDescription  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tom__arg) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {         if(st.isExpressionType(codom) && st.isPatternType( (( tom.gom.adt.symboltable.types.FieldDescription )tom__arg).getSort() )) {           return (( tom.gom.adt.symboltable.types.FieldDescription )tom__arg).setStatusValue( tom.gom.adt.symboltable.types.status.SRefreshPoint.make() );         }       }}}}return _visit_FieldDescription(tom__arg,introspector);}}



  public tom.gom.adt.symboltable.types.stringlist.StringList
    getConstructors(String sort) {
      return (tom.gom.adt.symboltable.types.stringlist.StringList)
        sorts.get(sort).getConstructors();
    }

  public String getSort(String constructor) {
    try {
      return constructors.get(constructor).getSortSymbol();
    } catch(NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException, constructor);
      return null;
    }
  }

  public String getChildSort(String constructor,int omega) {
    try {
      ConstructorDescription c = constructors.get(constructor);
      int count=1;

      { /* unamed block */{ /* unamed block */if ( (c instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )c) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

          return  (( tom.gom.adt.symboltable.types.ConstructorDescription )c).getDomain() ;
        }}}{ /* unamed block */if ( (c instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )c) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch522_5= (( tom.gom.adt.symboltable.types.ConstructorDescription )c).getFields() ;if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch522_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch522_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch522_end_11=tomMatch522_5;do {{ /* unamed block */if (!( tomMatch522_end_11.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch522_15= tomMatch522_end_11.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch522_15) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {


          if (count==omega) {
            return  tomMatch522_15.getSort() ;
          } else {
            count++;
          }
        }}if ( tomMatch522_end_11.isEmptyconcFieldDescription() ) {tomMatch522_end_11=tomMatch522_5;} else {tomMatch522_end_11= tomMatch522_end_11.getTailconcFieldDescription() ;}}} while(!( (tomMatch522_end_11==tomMatch522_5) ));}}}}}

    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException,
          constructor);
      return null;
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.cannotAccessToChildConstructor,
        new Object[] {omega,constructor});
    return null;
  }

  public tom.gom.adt.symboltable.types.stringlist.StringList
    getAccessibleAtoms(String sort) {
      try {
        return (tom.gom.adt.symboltable.types.stringlist.StringList)
          sorts.get(sort).getFreshInfo().getAccessibleAtoms();
      } catch(NullPointerException e) {
        GomMessage.error(getLogger(),null,0,
            GomMessage.undeclaredSortException, sort);
        return null;
      }
    }

  public tom.gom.adt.symboltable.types.stringlist.StringList getBoundAtoms(String sort) {
    return (tom.gom.adt.symboltable.types.stringlist.StringList)
      sorts.get(sort).getFreshInfo().getBoundAtoms();
  }

  private FieldDescriptionList getFieldList(String constructor) {
    return constructors.get(constructor).getFields();
  }

  public List<String> getFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch523_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch523_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch523_8= tomMatch523_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch523_8) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {

        result.add( tomMatch523_8.getFieldName() );
      }}if ( tomMatch523_end_4.isEmptyconcFieldDescription() ) {tomMatch523_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch523_end_4= tomMatch523_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch523_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public List<String> getNeutralFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch524_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch524_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch524_9= tomMatch524_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch524_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch524_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SNeutral) ) {


        result.add( tomMatch524_9.getFieldName() );
      }}}if ( tomMatch524_end_4.isEmptyconcFieldDescription() ) {tomMatch524_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch524_end_4= tomMatch524_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch524_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public List<String> getPatternFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch525_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch525_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch525_9= tomMatch525_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch525_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch525_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SPattern) ) {


        result.add( tomMatch525_9.getFieldName() );
      }}}if ( tomMatch525_end_4.isEmptyconcFieldDescription() ) {tomMatch525_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch525_end_4= tomMatch525_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch525_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  /**
   * generates 'getfield()' except for HeadC and TailC
   * where it generates 'getHeadRawC()' and 'getTailRawC()'
   **/
  public String rawGetter(String cons, String field) {
    if (isGenerated(cons)) {
      String suffix = getBaseName(cons);
      if (field.startsWith("Head")) {
        return "getHeadRaw" + suffix + "()";
      } else if (field.startsWith("Tail")) {
        return "getTailRaw" + suffix + "()";
      }
    }
    return "get" + field + "()";
  }

  public List<String> getNonPatternFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch526_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch526_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch526_9= tomMatch526_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch526_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {boolean tomMatch526_13= false ;if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch526_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SPattern) ) {tomMatch526_13= true ;}if (!(tomMatch526_13)) {


        result.add( tomMatch526_9.getFieldName() );
      }}}if ( tomMatch526_end_4.isEmptyconcFieldDescription() ) {tomMatch526_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch526_end_4= tomMatch526_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch526_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public List<String> getOuterFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch527_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch527_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch527_9= tomMatch527_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch527_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch527_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SOuter) ) {


        result.add( tomMatch527_9.getFieldName() );
      }}}if ( tomMatch527_end_4.isEmptyconcFieldDescription() ) {tomMatch527_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch527_end_4= tomMatch527_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch527_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public List<String> getInnerFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch528_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch528_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch528_9= tomMatch528_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch528_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch528_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SInner) ) {


        result.add( tomMatch528_9.getFieldName() );
      }}}if ( tomMatch528_end_4.isEmptyconcFieldDescription() ) {tomMatch528_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch528_end_4= tomMatch528_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch528_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public String getSort(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch529_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch529_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch529_9= tomMatch529_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch529_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {

        if ( tomMatch529_9.getFieldName() .equals(field)) { return  tomMatch529_9.getSort() ; }
      }}if ( tomMatch529_end_4.isEmptyconcFieldDescription() ) {tomMatch529_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch529_end_4= tomMatch529_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch529_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.shouldNeverHappen,
        new Object[]{cons,field});
    return null;
  }

  public boolean containsRefreshPoint(String cons) {
    ConstructorDescription d = constructors.get(cons);
    { /* unamed block */{ /* unamed block */if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

        return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getIsRefreshPoint() ;
      }}}{ /* unamed block */if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch530_5= (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getFields() ;if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch530_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch530_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch530_end_11=tomMatch530_5;do {{ /* unamed block */if (!( tomMatch530_end_11.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch530_15= tomMatch530_end_11.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch530_15) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch530_15.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SRefreshPoint) ) {



          return true;
        }}}if ( tomMatch530_end_11.isEmptyconcFieldDescription() ) {tomMatch530_end_11=tomMatch530_5;} else {tomMatch530_end_11= tomMatch530_end_11.getTailconcFieldDescription() ;}}} while(!( (tomMatch530_end_11==tomMatch530_5) ));}}}}}

    return false;
  }

  public boolean isOuter(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch531_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch531_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch531_9= tomMatch531_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch531_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch531_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SOuter) ) {


        if ( tomMatch531_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch531_end_4.isEmptyconcFieldDescription() ) {tomMatch531_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch531_end_4= tomMatch531_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch531_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isPattern(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch532_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch532_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch532_9= tomMatch532_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch532_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch532_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SPattern) ) {


        if ( tomMatch532_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch532_end_4.isEmptyconcFieldDescription() ) {tomMatch532_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch532_end_4= tomMatch532_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch532_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isInner(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch533_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch533_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch533_9= tomMatch533_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch533_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch533_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SInner) ) {


        if ( tomMatch533_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch533_end_4.isEmptyconcFieldDescription() ) {tomMatch533_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch533_end_4= tomMatch533_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch533_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isNeutral(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch534_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch534_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch534_9= tomMatch534_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch534_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch534_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SNeutral) ) {


        if( tomMatch534_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch534_end_4.isEmptyconcFieldDescription() ) {tomMatch534_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch534_end_4= tomMatch534_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch534_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }


  /**
   * for variadic operators
   */
  public boolean isRefreshPoint(String cons) {
    ConstructorDescription d = constructors.get(cons);
    { /* unamed block */{ /* unamed block */if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

        return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getIsRefreshPoint() ;
      }}}}

    return false;
  }

  /**
   * for variadic operators
   */
  public String getDomain(String cons) {
    ConstructorDescription d = constructors.get(cons);
    { /* unamed block */{ /* unamed block */if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {
 return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getDomain() ; }}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.nonVariadicOperator);
    return null;
  }

  /**
   * for variadic operators
   */
  public String getCoDomain(String cons) {
    ConstructorDescription d = constructors.get(cons);
    { /* unamed block */{ /* unamed block */if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {
 return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getSortSymbol() ; }}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.nonVariadicOperator);
    return null;
  }


  public boolean isRefreshPoint(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch538_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{ /* unamed block */if (!( tomMatch538_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch538_9= tomMatch538_end_4.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch538_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.Status ) tomMatch538_9.getStatusValue() ) instanceof tom.gom.adt.symboltable.types.status.SRefreshPoint) ) {


        if ( tomMatch538_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch538_end_4.isEmptyconcFieldDescription() ) {tomMatch538_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch538_end_4= tomMatch538_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch538_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isBound(String cons, String field) {
    String sort = getSort(cons);
    return getBoundAtoms(sort).contains(getSort(cons,field));
  }


  public Set<String> getAtoms() {
    Set<String> res = getSorts();
    Iterator<String> it = res.iterator();
    while (it.hasNext()) {
      if (!isAtomType(it.next())) {
        it.remove();
      }
    }
    return res;
  }

  private StringList getAccessibleAtoms
    (String sort, HashSet<String> visited)  {
      if(getGomEnvironment().isBuiltin(sort) || visited.contains(sort)) {
        return  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
      }
      visited.add(sort);
      if(isAtomType(sort)) {
        return  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(sort, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) ;
      }
      StringList res =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
      for(String c: getConstructors(sort)) {
        ConstructorDescription cd = constructors.get(c);
        { /* unamed block */{ /* unamed block */if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

            StringList tyatoms = getAccessibleAtoms( (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getDomain() ,visited);
            res = tom_append_list_StringList(tyatoms,tom_append_list_StringList(res, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ));
          }}}{ /* unamed block */if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch539_5= (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getFields() ;if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch539_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch539_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch539_end_11=tomMatch539_5;do {{ /* unamed block */if (!( tomMatch539_end_11.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch539_15= tomMatch539_end_11.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch539_15) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {


              StringList tyatoms = getAccessibleAtoms( tomMatch539_15.getSort() ,visited);
              res = tom_append_list_StringList(tyatoms,tom_append_list_StringList(res, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ));
            }}if ( tomMatch539_end_11.isEmptyconcFieldDescription() ) {tomMatch539_end_11=tomMatch539_5;} else {tomMatch539_end_11= tomMatch539_end_11.getTailconcFieldDescription() ;}}} while(!( (tomMatch539_end_11==tomMatch539_5) ));}}}}}

      }
      return res;
    }

  private void fillAccessibleAtoms() {
    for(String s: getSorts()) {
      if(isExpressionType(s) || isPatternType(s)) {
        StringList atoms = getAccessibleAtoms(s,new HashSet<String>());
        setAccessibleAtoms(s,atoms);
      }
    }
  }

  private void computeSortDependences() {
    sortDependences.clear();
    for(String sort: getSorts()) {
      if (getGomEnvironment().isBuiltin(sort)) { continue; }
      for(String c: getConstructors(sort)) {
        ConstructorDescription cd = constructors.get(c);
        { /* unamed block */{ /* unamed block */if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) { String  tom___ty= (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getDomain() ;

            if(!getGomEnvironment().isBuiltin(tom___ty)) {
              sortDependences.addLink(sort,tom___ty);
            }
          }}}{ /* unamed block */if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch540_5= (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getFields() ;if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch540_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch540_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch540_end_11=tomMatch540_5;do {{ /* unamed block */if (!( tomMatch540_end_11.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch540_15= tomMatch540_end_11.getHeadconcFieldDescription() ;if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch540_15) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { String  tom___ty= tomMatch540_15.getSort() ;


              if(!getGomEnvironment().isBuiltin(tom___ty)) {
                sortDependences.addLink(sort,tom___ty);
              }
            }}if ( tomMatch540_end_11.isEmptyconcFieldDescription() ) {tomMatch540_end_11=tomMatch540_5;} else {tomMatch540_end_11= tomMatch540_end_11.getTailconcFieldDescription() ;}}} while(!( (tomMatch540_end_11==tomMatch540_5) ));}}}}}

      }
    }
  }

  /**
   * sets sorts that are not connected to any atom at NoFreshSort
   **/
  private void isolateFreshSorts() {
    Set<String> atoms = getAtoms();
    Set<String> sorts = getSorts();
    Set<String> connected = sortDependences.connected(atoms);
    sorts.removeAll(connected);
    for(String s: sorts) {
      setFreshSortInfo(s, tom.gom.adt.symboltable.types.freshsortinfo.NoFreshSort.make() );
    }
  }

  public Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
