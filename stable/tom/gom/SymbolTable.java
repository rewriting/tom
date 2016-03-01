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
    {{if ( (gml instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )(( tom.gom.adt.gom.types.GomModuleList )gml)) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )(( tom.gom.adt.gom.types.GomModuleList )gml)) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch474_end_4=(( tom.gom.adt.gom.types.GomModuleList )gml);do {{if (!( tomMatch474_end_4.isEmptyConcGomModule() )) {fillFromGomModule( tomMatch474_end_4.getHeadConcGomModule() )
; }if ( tomMatch474_end_4.isEmptyConcGomModule() ) {tomMatch474_end_4=(( tom.gom.adt.gom.types.GomModuleList )gml);} else {tomMatch474_end_4= tomMatch474_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch474_end_4==(( tom.gom.adt.gom.types.GomModuleList )gml)) ));}}}}

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
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )desc) instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )(( tom.gom.adt.symboltable.types.SortDescription )desc)) instanceof tom.gom.adt.symboltable.types.sortdescription.SortDescription) ) { String  tom_m= (( tom.gom.adt.symboltable.types.SortDescription )desc).getModuleSymbol() ;

        String packageName = gomEnvironment.getStreamManager().getPackagePath(tom_m);
        return (packageName.equals("") ? "" : packageName + ".")
          + tom_m.toLowerCase() + ".types." + sort;
      }}}}}

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
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )desc)) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {

        return getFullSortClassName( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getSortSymbol() ).toLowerCase() + "." + cons;
      }}}}{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )desc)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

        return getFullSortClassName( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getSortSymbol() ).toLowerCase() + "." + cons;
      }}}}}

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
      {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )desc)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) { return true; }}}}}
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
      {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )desc)) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.GenerationInfo  tomMatch478_1= (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getGenerated() ;boolean tomMatch478_6= false ;if ( (tomMatch478_1 instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch478_1) instanceof tom.gom.adt.symboltable.types.generationinfo.No) ) {tomMatch478_6= true ;}}if (!(tomMatch478_6)) {

          return true;
        }}}}}}

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
      {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )desc)) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.GenerationInfo  tomMatch479_1= (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getGenerated() ;if ( (tomMatch479_1 instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch479_1) instanceof tom.gom.adt.symboltable.types.generationinfo.GenCons) ) {

          return true;
        }}}}}}}

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
      {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )desc)) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.GenerationInfo  tomMatch480_1= (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getGenerated() ;if ( (tomMatch480_1 instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch480_1) instanceof tom.gom.adt.symboltable.types.generationinfo.GenNil) ) {

          return true;
        }}}}}}}

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
    {{if ( (m instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )m) instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )(( tom.gom.adt.gom.types.GomModule )m)) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch481_1= (( tom.gom.adt.gom.types.GomModule )m).getModuleName() ; tom.gom.adt.gom.types.SectionList  tomMatch481_2= (( tom.gom.adt.gom.types.GomModule )m).getSectionList() ;if ( (tomMatch481_1 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch481_1) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch481_2) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch481_2) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch481_end_11=tomMatch481_2;do {{if (!( tomMatch481_end_11.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch481_15= tomMatch481_end_11.getHeadConcSection() ;if ( (tomMatch481_15 instanceof tom.gom.adt.gom.types.Section) ) {if ( ((( tom.gom.adt.gom.types.Section )tomMatch481_15) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.ProductionList  tomMatch481_14= tomMatch481_15.getProductionList() ;if ( (((( tom.gom.adt.gom.types.ProductionList )tomMatch481_14) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tomMatch481_14) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch481_end_20=tomMatch481_14;do {{if (!( tomMatch481_end_20.isEmptyConcProduction() )) {fillFromProduction( tomMatch481_1.getName() , tomMatch481_end_20.getHeadConcProduction() )


;
        }if ( tomMatch481_end_20.isEmptyConcProduction() ) {tomMatch481_end_20=tomMatch481_14;} else {tomMatch481_end_20= tomMatch481_end_20.getTailConcProduction() ;}}} while(!( (tomMatch481_end_20==tomMatch481_14) ));}}}}if ( tomMatch481_end_11.isEmptyConcSection() ) {tomMatch481_end_11=tomMatch481_2;} else {tomMatch481_end_11= tomMatch481_end_11.getTailConcSection() ;}}} while(!( (tomMatch481_end_11==tomMatch481_2) ));}}}}}}}}

  }

  private static StringList getConstructors(AlternativeList al) {
    StringList res =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
    {{if ( (al instanceof tom.gom.adt.gom.types.AlternativeList) ) {if ( (((( tom.gom.adt.gom.types.AlternativeList )(( tom.gom.adt.gom.types.AlternativeList )al)) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )(( tom.gom.adt.gom.types.AlternativeList )al)) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch482_end_4=(( tom.gom.adt.gom.types.AlternativeList )al);do {{if (!( tomMatch482_end_4.isEmptyConcAlternative() )) { tom.gom.adt.gom.types.Alternative  tomMatch482_8= tomMatch482_end_4.getHeadConcAlternative() ;if ( (tomMatch482_8 instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )tomMatch482_8) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) {

        res =  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( tomMatch482_8.getName() , res) ;
      }}}if ( tomMatch482_end_4.isEmptyConcAlternative() ) {tomMatch482_end_4=(( tom.gom.adt.gom.types.AlternativeList )al);} else {tomMatch482_end_4= tomMatch482_end_4.getTailConcAlternative() ;}}} while(!( (tomMatch482_end_4==(( tom.gom.adt.gom.types.AlternativeList )al)) ));}}}}

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
      {{ tom.gom.adt.symboltable.types.GenerationInfo  tomMatch483_0=getGenerated(c);if ( (tomMatch483_0 instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch483_0) instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )(( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch483_0)) instanceof tom.gom.adt.symboltable.types.generationinfo.GenCons) ) {
 return "ConsRaw" +  (( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch483_0).getBaseName() ; }}}}{ tom.gom.adt.symboltable.types.GenerationInfo  tomMatch483_4=getGenerated(c);if ( (tomMatch483_4 instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch483_4) instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )(( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch483_4)) instanceof tom.gom.adt.symboltable.types.generationinfo.GenNil) ) {
 return "EmptyRaw" +  (( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch483_4).getBaseName() ; }}}}}

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
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )desc) instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )(( tom.gom.adt.symboltable.types.SortDescription )desc)) instanceof tom.gom.adt.symboltable.types.sortdescription.SortDescription) ) { String  tom_m= (( tom.gom.adt.symboltable.types.SortDescription )desc).getModuleSymbol() ;

        String packageName = gomEnvironment.getStreamManager().getPackagePath(tom_m);
        return (packageName.equals("") ? "" : packageName + ".")
          + tom_m.toLowerCase() + ".types." + rawSort(sort);
      }}}}}

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
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )desc)) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {

        return qualifiedRawSortId( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getSortSymbol() ).toLowerCase() + "." + rawCons(cons);
      }}}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.nonExhaustiveMatch);
    return null;
  }

  private static StringList convertBoundAtoms(AtomList al) {
    StringList res =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
    {{if ( (al instanceof tom.gom.adt.gom.types.AtomList) ) {if ( (((( tom.gom.adt.gom.types.AtomList )(( tom.gom.adt.gom.types.AtomList )al)) instanceof tom.gom.adt.gom.types.atomlist.ConsConcAtom) || ((( tom.gom.adt.gom.types.AtomList )(( tom.gom.adt.gom.types.AtomList )al)) instanceof tom.gom.adt.gom.types.atomlist.EmptyConcAtom)) ) { tom.gom.adt.gom.types.AtomList  tomMatch486_end_4=(( tom.gom.adt.gom.types.AtomList )al);do {{if (!( tomMatch486_end_4.isEmptyConcAtom() )) {
 res =  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( tomMatch486_end_4.getHeadConcAtom() , res) ; }if ( tomMatch486_end_4.isEmptyConcAtom() ) {tomMatch486_end_4=(( tom.gom.adt.gom.types.AtomList )al);} else {tomMatch486_end_4= tomMatch486_end_4.getTailConcAtom() ;}}} while(!( (tomMatch486_end_4==(( tom.gom.adt.gom.types.AtomList )al)) ));}}}}

    return res;
  }

  private void fillFromProduction(String moduleName, Production p) {
    {{if ( (p instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )p) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )p)) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch487_1= (( tom.gom.adt.gom.types.Production )p).getType() ;if ( (tomMatch487_1 instanceof tom.gom.adt.gom.types.GomType) ) {if ( ((( tom.gom.adt.gom.types.GomType )tomMatch487_1) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { tom.gom.adt.gom.types.TypeSpec  tom_spe= tomMatch487_1.getSpecialization() ; String  tom_n= tomMatch487_1.getName() ; tom.gom.adt.gom.types.AlternativeList  tom_al= (( tom.gom.adt.gom.types.Production )p).getAlternativeList() ;


          // filling sorts (except AccessibleAtoms)
          StringList cons = getConstructors(tom_al);
          StringList bound = convertBoundAtoms( (( tom.gom.adt.gom.types.Production )p).getBinds() );
          StringList empty =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
          FreshSortInfo info = null;
          {{if ( (tom_spe instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )tom_spe) instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )(( tom.gom.adt.gom.types.TypeSpec )tom_spe)) instanceof tom.gom.adt.gom.types.typespec.ExpressionType) ) {
 info =  tom.gom.adt.symboltable.types.freshsortinfo.ExpressionTypeInfo.make(empty) ; }}}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )tom_spe) instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )(( tom.gom.adt.gom.types.TypeSpec )tom_spe)) instanceof tom.gom.adt.gom.types.typespec.PatternType) ) {
 info =  tom.gom.adt.symboltable.types.freshsortinfo.PatternTypeInfo.make(bound, empty) ; }}}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )tom_spe) instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )(( tom.gom.adt.gom.types.TypeSpec )tom_spe)) instanceof tom.gom.adt.gom.types.typespec.AtomType) ) {
 info =  tom.gom.adt.symboltable.types.freshsortinfo.AtomTypeInfo.make() ; }}}}}

          sorts.put(tom_n, tom.gom.adt.symboltable.types.sortdescription.SortDescription.make(cons, moduleName, info) );
          // filling constructors
          {{if ( (tom_al instanceof tom.gom.adt.gom.types.AlternativeList) ) {if ( (((( tom.gom.adt.gom.types.AlternativeList )(( tom.gom.adt.gom.types.AlternativeList )tom_al)) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )(( tom.gom.adt.gom.types.AlternativeList )tom_al)) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch489_end_4=(( tom.gom.adt.gom.types.AlternativeList )tom_al);do {{if (!( tomMatch489_end_4.isEmptyConcAlternative() )) {fillCons(tom_n, tomMatch489_end_4.getHeadConcAlternative() )
; }if ( tomMatch489_end_4.isEmptyConcAlternative() ) {tomMatch489_end_4=(( tom.gom.adt.gom.types.AlternativeList )tom_al);} else {tomMatch489_end_4= tomMatch489_end_4.getTailConcAlternative() ;}}} while(!( (tomMatch489_end_4==(( tom.gom.adt.gom.types.AlternativeList )tom_al)) ));}}}}

        }}}}}}}

  }

  private void fillCons(String codom, Alternative p)  {
    {{if ( (p instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )p) instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )(( tom.gom.adt.gom.types.Alternative )p)) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { String  tom_n= (( tom.gom.adt.gom.types.Alternative )p).getName() ; tom.gom.adt.gom.types.FieldList  tom_dl= (( tom.gom.adt.gom.types.Alternative )p).getDomainList() ;{{if ( (tom_dl instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )(( tom.gom.adt.gom.types.FieldList )tom_dl)) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )(( tom.gom.adt.gom.types.FieldList )tom_dl)) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )tom_dl).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch491_5= (( tom.gom.adt.gom.types.FieldList )tom_dl).getHeadConcField() ;if ( (tomMatch491_5 instanceof tom.gom.adt.gom.types.Field) ) {if ( ((( tom.gom.adt.gom.types.Field )tomMatch491_5) instanceof tom.gom.adt.gom.types.field.StarredField) ) { tom.gom.adt.gom.types.GomType  tomMatch491_3= tomMatch491_5.getFieldType() ;if ( (tomMatch491_3 instanceof tom.gom.adt.gom.types.GomType) ) {if ( ((( tom.gom.adt.gom.types.GomType )tomMatch491_3) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {if (  (( tom.gom.adt.gom.types.FieldList )tom_dl).getTailConcField() .isEmptyConcField() ) {





            constructors.put(tom_n,
                 tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription.make(codom,  tomMatch491_3.getName() ,  tomMatch491_5.getSpecifier() .isRefresh()) );
            return;
          }}}}}}}}}{if ( (tom_dl instanceof tom.gom.adt.gom.types.FieldList) ) {

            FieldDescriptionList fl = getFieldList(codom,tom_dl);
            constructors.put(tom_n, tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription.make(codom, fl,  tom.gom.adt.symboltable.types.generationinfo.No.make() ) );
          }}}

      }}}}}

  }

  private FieldDescriptionList getFieldList(String codom, FieldList dl) {
    FieldDescriptionList res =  tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ;
    {{if ( (dl instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )(( tom.gom.adt.gom.types.FieldList )dl)) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )(( tom.gom.adt.gom.types.FieldList )dl)) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) { tom.gom.adt.gom.types.FieldList  tomMatch492_end_4=(( tom.gom.adt.gom.types.FieldList )dl);do {{if (!( tomMatch492_end_4.isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch492_10= tomMatch492_end_4.getHeadConcField() ;if ( (tomMatch492_10 instanceof tom.gom.adt.gom.types.Field) ) {if ( ((( tom.gom.adt.gom.types.Field )tomMatch492_10) instanceof tom.gom.adt.gom.types.field.NamedField) ) { tom.gom.adt.gom.types.GomType  tomMatch492_9= tomMatch492_10.getFieldType() ; tom.gom.adt.gom.types.ScopeSpecifier  tom_spe= tomMatch492_10.getSpecifier() ;if ( (tomMatch492_9 instanceof tom.gom.adt.gom.types.GomType) ) {if ( ((( tom.gom.adt.gom.types.GomType )tomMatch492_9) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {



        Status st = null;
        {{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )(( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe)) instanceof tom.gom.adt.gom.types.scopespecifier.Outer) ) {
 st =  tom.gom.adt.symboltable.types.status.SOuter.make() ; }}}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )(( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe)) instanceof tom.gom.adt.gom.types.scopespecifier.Inner) ) {
 st =  tom.gom.adt.symboltable.types.status.SInner.make() ; }}}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )(( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe)) instanceof tom.gom.adt.gom.types.scopespecifier.Neutral) ) {
 st =  tom.gom.adt.symboltable.types.status.SNeutral.make() ; }}}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )(( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe)) instanceof tom.gom.adt.gom.types.scopespecifier.None) ) {
 st = isPatternType(codom) ?  tom.gom.adt.symboltable.types.status.SPattern.make()  :  tom.gom.adt.symboltable.types.status.SNone.make() ; }}}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )(( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe)) instanceof tom.gom.adt.gom.types.scopespecifier.Refresh) ) {
 st =  tom.gom.adt.symboltable.types.status.SRefreshPoint.make() ; }}}}}

        FieldDescription desc =  tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make( tomMatch492_10.getName() ,  tomMatch492_9.getName() , st) ;
        res = tom_append_list_concFieldDescription(res, tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make(desc, tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) );
      }}}}}if ( tomMatch492_end_4.isEmptyConcField() ) {tomMatch492_end_4=(( tom.gom.adt.gom.types.FieldList )dl);} else {tomMatch492_end_4= tomMatch492_end_4.getTailConcField() ;}}} while(!( (tomMatch492_end_4==(( tom.gom.adt.gom.types.FieldList )dl)) ));}}}}

    return res;
  }

  private void setAccessibleAtoms(String sort, StringList atoms) {
    SortDescription desc = sorts.get(sort);
    SortDescription ndesc = null;
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )desc) instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )(( tom.gom.adt.symboltable.types.SortDescription )desc)) instanceof tom.gom.adt.symboltable.types.sortdescription.SortDescription) ) {

        ndesc = desc.setFreshInfo( (( tom.gom.adt.symboltable.types.SortDescription )desc).getFreshInfo() .setAccessibleAtoms(atoms));
      }}}}}

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
      {{if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )(( tom.gom.adt.symboltable.types.FreshSortInfo )i)) instanceof tom.gom.adt.symboltable.types.freshsortinfo.ExpressionTypeInfo) ) { return true; }}}}}
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
      {{if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )(( tom.gom.adt.symboltable.types.FreshSortInfo )i)) instanceof tom.gom.adt.symboltable.types.freshsortinfo.PatternTypeInfo) ) { return true; }}}}}
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
      {{if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )(( tom.gom.adt.symboltable.types.FreshSortInfo )i)) instanceof tom.gom.adt.symboltable.types.freshsortinfo.AtomTypeInfo) ) { return true; }}}}}
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
      {{if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {boolean tomMatch498_3= false ;if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )(( tom.gom.adt.symboltable.types.FreshSortInfo )i)) instanceof tom.gom.adt.symboltable.types.freshsortinfo.NoFreshSort) ) {tomMatch498_3= true ;}}if (!(tomMatch498_3)) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return false;
    }
  }

  public static class SetRefreshPoints extends tom.library.sl.AbstractStrategyBasic {private  tom.gom.SymbolTable  st;private  String  codom;public SetRefreshPoints( tom.gom.SymbolTable  st,  String  codom) {super(( new tom.library.sl.Identity() ));this.st=st;this.codom=codom;}public  tom.gom.SymbolTable  getst() {return st;}public  String  getcodom() {return codom;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {return ((T)visit_ConstructorDescription((( tom.gom.adt.symboltable.types.ConstructorDescription )v),introspector));}if ( (v instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {return ((T)visit_FieldDescription((( tom.gom.adt.symboltable.types.FieldDescription )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.symboltable.types.FieldDescription  _visit_FieldDescription( tom.gom.adt.symboltable.types.FieldDescription  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.symboltable.types.FieldDescription )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.symboltable.types.ConstructorDescription  _visit_ConstructorDescription( tom.gom.adt.symboltable.types.ConstructorDescription  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.symboltable.types.ConstructorDescription )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.symboltable.types.ConstructorDescription  visit_ConstructorDescription( tom.gom.adt.symboltable.types.ConstructorDescription  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {










        if(st.isExpressionType(codom) && st.isPatternType( (( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg).getDomain() )) {
          return (( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg).setIsRefreshPoint(true);
        }
      }}}}}return _visit_ConstructorDescription(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.gom.adt.symboltable.types.FieldDescription  visit_FieldDescription( tom.gom.adt.symboltable.types.FieldDescription  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tom__arg) instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )(( tom.gom.adt.symboltable.types.FieldDescription )tom__arg)) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {         if(st.isExpressionType(codom) && st.isPatternType( (( tom.gom.adt.symboltable.types.FieldDescription )tom__arg).getSort() )) {           return (( tom.gom.adt.symboltable.types.FieldDescription )tom__arg).setStatusValue( tom.gom.adt.symboltable.types.status.SRefreshPoint.make() );         }       }}}}}return _visit_FieldDescription(tom__arg,introspector);}}



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

      {{if ( (c instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )c) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )c)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

          return  (( tom.gom.adt.symboltable.types.ConstructorDescription )c).getDomain() ;
        }}}}{if ( (c instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )c) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )c)) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch501_5= (( tom.gom.adt.symboltable.types.ConstructorDescription )c).getFields() ;if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch501_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch501_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch501_end_11=tomMatch501_5;do {{if (!( tomMatch501_end_11.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch501_15= tomMatch501_end_11.getHeadconcFieldDescription() ;if ( (tomMatch501_15 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch501_15) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {


          if (count==omega) {
            return  tomMatch501_15.getSort() ;
          } else {
            count++;
          }
        }}}if ( tomMatch501_end_11.isEmptyconcFieldDescription() ) {tomMatch501_end_11=tomMatch501_5;} else {tomMatch501_end_11= tomMatch501_end_11.getTailconcFieldDescription() ;}}} while(!( (tomMatch501_end_11==tomMatch501_5) ));}}}}}}

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
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch502_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch502_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch502_8= tomMatch502_end_4.getHeadconcFieldDescription() ;if ( (tomMatch502_8 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch502_8) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {

        result.add( tomMatch502_8.getFieldName() );
      }}}if ( tomMatch502_end_4.isEmptyconcFieldDescription() ) {tomMatch502_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch502_end_4= tomMatch502_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch502_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public List<String> getNeutralFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch503_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch503_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch503_9= tomMatch503_end_4.getHeadconcFieldDescription() ;if ( (tomMatch503_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch503_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch503_8= tomMatch503_9.getStatusValue() ;if ( (tomMatch503_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch503_8) instanceof tom.gom.adt.symboltable.types.status.SNeutral) ) {


        result.add( tomMatch503_9.getFieldName() );
      }}}}}if ( tomMatch503_end_4.isEmptyconcFieldDescription() ) {tomMatch503_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch503_end_4= tomMatch503_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch503_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public List<String> getPatternFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch504_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch504_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch504_9= tomMatch504_end_4.getHeadconcFieldDescription() ;if ( (tomMatch504_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch504_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch504_8= tomMatch504_9.getStatusValue() ;if ( (tomMatch504_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch504_8) instanceof tom.gom.adt.symboltable.types.status.SPattern) ) {


        result.add( tomMatch504_9.getFieldName() );
      }}}}}if ( tomMatch504_end_4.isEmptyconcFieldDescription() ) {tomMatch504_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch504_end_4= tomMatch504_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch504_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

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
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch505_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch505_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch505_9= tomMatch505_end_4.getHeadconcFieldDescription() ;if ( (tomMatch505_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch505_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch505_8= tomMatch505_9.getStatusValue() ;boolean tomMatch505_13= false ;if ( (tomMatch505_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch505_8) instanceof tom.gom.adt.symboltable.types.status.SPattern) ) {tomMatch505_13= true ;}}if (!(tomMatch505_13)) {


        result.add( tomMatch505_9.getFieldName() );
      }}}}if ( tomMatch505_end_4.isEmptyconcFieldDescription() ) {tomMatch505_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch505_end_4= tomMatch505_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch505_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public List<String> getOuterFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch506_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch506_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch506_9= tomMatch506_end_4.getHeadconcFieldDescription() ;if ( (tomMatch506_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch506_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch506_8= tomMatch506_9.getStatusValue() ;if ( (tomMatch506_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch506_8) instanceof tom.gom.adt.symboltable.types.status.SOuter) ) {


        result.add( tomMatch506_9.getFieldName() );
      }}}}}if ( tomMatch506_end_4.isEmptyconcFieldDescription() ) {tomMatch506_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch506_end_4= tomMatch506_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch506_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public List<String> getInnerFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch507_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch507_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch507_9= tomMatch507_end_4.getHeadconcFieldDescription() ;if ( (tomMatch507_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch507_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch507_8= tomMatch507_9.getStatusValue() ;if ( (tomMatch507_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch507_8) instanceof tom.gom.adt.symboltable.types.status.SInner) ) {


        result.add( tomMatch507_9.getFieldName() );
      }}}}}if ( tomMatch507_end_4.isEmptyconcFieldDescription() ) {tomMatch507_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch507_end_4= tomMatch507_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch507_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public String getSort(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch508_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch508_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch508_9= tomMatch508_end_4.getHeadconcFieldDescription() ;if ( (tomMatch508_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch508_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {

        if ( tomMatch508_9.getFieldName() .equals(field)) { return  tomMatch508_9.getSort() ; }
      }}}if ( tomMatch508_end_4.isEmptyconcFieldDescription() ) {tomMatch508_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch508_end_4= tomMatch508_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch508_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.shouldNeverHappen,
        new Object[]{cons,field});
    return null;
  }

  public boolean containsRefreshPoint(String cons) {
    ConstructorDescription d = constructors.get(cons);
    {{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )d)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

        return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getIsRefreshPoint() ;
      }}}}{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )d)) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch509_5= (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getFields() ;if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch509_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch509_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch509_end_11=tomMatch509_5;do {{if (!( tomMatch509_end_11.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch509_15= tomMatch509_end_11.getHeadconcFieldDescription() ;if ( (tomMatch509_15 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch509_15) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch509_14= tomMatch509_15.getStatusValue() ;if ( (tomMatch509_14 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch509_14) instanceof tom.gom.adt.symboltable.types.status.SRefreshPoint) ) {



          return true;
        }}}}}if ( tomMatch509_end_11.isEmptyconcFieldDescription() ) {tomMatch509_end_11=tomMatch509_5;} else {tomMatch509_end_11= tomMatch509_end_11.getTailconcFieldDescription() ;}}} while(!( (tomMatch509_end_11==tomMatch509_5) ));}}}}}}

    return false;
  }

  public boolean isOuter(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch510_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch510_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch510_9= tomMatch510_end_4.getHeadconcFieldDescription() ;if ( (tomMatch510_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch510_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch510_8= tomMatch510_9.getStatusValue() ;if ( (tomMatch510_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch510_8) instanceof tom.gom.adt.symboltable.types.status.SOuter) ) {


        if ( tomMatch510_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}}}if ( tomMatch510_end_4.isEmptyconcFieldDescription() ) {tomMatch510_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch510_end_4= tomMatch510_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch510_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isPattern(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch511_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch511_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch511_9= tomMatch511_end_4.getHeadconcFieldDescription() ;if ( (tomMatch511_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch511_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch511_8= tomMatch511_9.getStatusValue() ;if ( (tomMatch511_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch511_8) instanceof tom.gom.adt.symboltable.types.status.SPattern) ) {


        if ( tomMatch511_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}}}if ( tomMatch511_end_4.isEmptyconcFieldDescription() ) {tomMatch511_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch511_end_4= tomMatch511_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch511_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isInner(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch512_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch512_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch512_9= tomMatch512_end_4.getHeadconcFieldDescription() ;if ( (tomMatch512_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch512_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch512_8= tomMatch512_9.getStatusValue() ;if ( (tomMatch512_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch512_8) instanceof tom.gom.adt.symboltable.types.status.SInner) ) {


        if ( tomMatch512_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}}}if ( tomMatch512_end_4.isEmptyconcFieldDescription() ) {tomMatch512_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch512_end_4= tomMatch512_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch512_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isNeutral(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch513_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch513_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch513_9= tomMatch513_end_4.getHeadconcFieldDescription() ;if ( (tomMatch513_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch513_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch513_8= tomMatch513_9.getStatusValue() ;if ( (tomMatch513_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch513_8) instanceof tom.gom.adt.symboltable.types.status.SNeutral) ) {


        if( tomMatch513_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}}}if ( tomMatch513_end_4.isEmptyconcFieldDescription() ) {tomMatch513_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch513_end_4= tomMatch513_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch513_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }


  /**
   * for variadic operators
   */
  public boolean isRefreshPoint(String cons) {
    ConstructorDescription d = constructors.get(cons);
    {{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )d)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

        return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getIsRefreshPoint() ;
      }}}}}

    return false;
  }

  /**
   * for variadic operators
   */
  public String getDomain(String cons) {
    ConstructorDescription d = constructors.get(cons);
    {{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )d)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {
 return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getDomain() ; }}}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.nonVariadicOperator);
    return null;
  }

  /**
   * for variadic operators
   */
  public String getCoDomain(String cons) {
    ConstructorDescription d = constructors.get(cons);
    {{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )d)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {
 return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getSortSymbol() ; }}}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.nonVariadicOperator);
    return null;
  }


  public boolean isRefreshPoint(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch517_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch517_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch517_9= tomMatch517_end_4.getHeadconcFieldDescription() ;if ( (tomMatch517_9 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch517_9) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { tom.gom.adt.symboltable.types.Status  tomMatch517_8= tomMatch517_9.getStatusValue() ;if ( (tomMatch517_8 instanceof tom.gom.adt.symboltable.types.Status) ) {if ( ((( tom.gom.adt.symboltable.types.Status )tomMatch517_8) instanceof tom.gom.adt.symboltable.types.status.SRefreshPoint) ) {


        if ( tomMatch517_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}}}if ( tomMatch517_end_4.isEmptyconcFieldDescription() ) {tomMatch517_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch517_end_4= tomMatch517_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch517_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

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
        {{if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )cd)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

            StringList tyatoms = getAccessibleAtoms( (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getDomain() ,visited);
            res = tom_append_list_StringList(tyatoms,tom_append_list_StringList(res, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ));
          }}}}{if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )cd)) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch518_5= (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getFields() ;if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch518_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch518_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch518_end_11=tomMatch518_5;do {{if (!( tomMatch518_end_11.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch518_15= tomMatch518_end_11.getHeadconcFieldDescription() ;if ( (tomMatch518_15 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch518_15) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {


              StringList tyatoms = getAccessibleAtoms( tomMatch518_15.getSort() ,visited);
              res = tom_append_list_StringList(tyatoms,tom_append_list_StringList(res, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ));
            }}}if ( tomMatch518_end_11.isEmptyconcFieldDescription() ) {tomMatch518_end_11=tomMatch518_5;} else {tomMatch518_end_11= tomMatch518_end_11.getTailconcFieldDescription() ;}}} while(!( (tomMatch518_end_11==tomMatch518_5) ));}}}}}}

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
        {{if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )cd)) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) { String  tom_ty= (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getDomain() ;

            if(!getGomEnvironment().isBuiltin(tom_ty)) {
              sortDependences.addLink(sort,tom_ty);
            }
          }}}}{if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )(( tom.gom.adt.symboltable.types.ConstructorDescription )cd)) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch519_5= (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getFields() ;if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch519_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )tomMatch519_5) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch519_end_11=tomMatch519_5;do {{if (!( tomMatch519_end_11.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch519_15= tomMatch519_end_11.getHeadconcFieldDescription() ;if ( (tomMatch519_15 instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tomMatch519_15) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { String  tom_ty= tomMatch519_15.getSort() ;


              if(!getGomEnvironment().isBuiltin(tom_ty)) {
                sortDependences.addLink(sort,tom_ty);
              }
            }}}if ( tomMatch519_end_11.isEmptyconcFieldDescription() ) {tomMatch519_end_11=tomMatch519_5;} else {tomMatch519_end_11= tomMatch519_end_11.getTailconcFieldDescription() ;}}} while(!( (tomMatch519_end_11==tomMatch519_5) ));}}}}}}

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
