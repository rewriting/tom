/*
 *
 * GOM
 *
 * Copyright (c) 2006-2011, INPL, INRIA
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.expander;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.tools.GomEnvironment;
import tom.gom.expander.rule.RuleExpander;
import tom.gom.expander.rule.GraphRuleExpander;

public class HookTypeExpander {

         private static   tom.gom.adt.code.types.Code  tom_append_list_CodeList( tom.gom.adt.code.types.Code  l1,  tom.gom.adt.code.types.Code  l2) {     if( l1.isEmptyCodeList() ) {       return l2;     } else if( l2.isEmptyCodeList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {       if(  l1.getTailCodeList() .isEmptyCodeList() ) {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,l2) ;       } else {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,tom_append_list_CodeList( l1.getTailCodeList() ,l2)) ;       }     } else {       return  tom.gom.adt.code.types.code.ConsCodeList.make(l1,l2) ;     }   }   private static   tom.gom.adt.code.types.Code  tom_get_slice_CodeList( tom.gom.adt.code.types.Code  begin,  tom.gom.adt.code.types.Code  end, tom.gom.adt.code.types.Code  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCodeList()  ||  (end== tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getHeadCodeList() :begin),( tom.gom.adt.code.types.Code )tom_get_slice_CodeList((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),end,tail)) ;   }      private static   tom.gom.adt.gom.types.ArgList  tom_append_list_ConcArg( tom.gom.adt.gom.types.ArgList l1,  tom.gom.adt.gom.types.ArgList  l2) {     if( l1.isEmptyConcArg() ) {       return l2;     } else if( l2.isEmptyConcArg() ) {       return l1;     } else if(  l1.getTailConcArg() .isEmptyConcArg() ) {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,l2) ;     } else {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,tom_append_list_ConcArg( l1.getTailConcArg() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ArgList  tom_get_slice_ConcArg( tom.gom.adt.gom.types.ArgList  begin,  tom.gom.adt.gom.types.ArgList  end, tom.gom.adt.gom.types.ArgList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcArg()  ||  (end== tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( begin.getHeadConcArg() ,( tom.gom.adt.gom.types.ArgList )tom_get_slice_ConcArg( begin.getTailConcArg() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.AlternativeList  tom_append_list_ConcAlternative( tom.gom.adt.gom.types.AlternativeList l1,  tom.gom.adt.gom.types.AlternativeList  l2) {     if( l1.isEmptyConcAlternative() ) {       return l2;     } else if( l2.isEmptyConcAlternative() ) {       return l1;     } else if(  l1.getTailConcAlternative() .isEmptyConcAlternative() ) {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,l2) ;     } else {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,tom_append_list_ConcAlternative( l1.getTailConcAlternative() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AlternativeList  tom_get_slice_ConcAlternative( tom.gom.adt.gom.types.AlternativeList  begin,  tom.gom.adt.gom.types.AlternativeList  end, tom.gom.adt.gom.types.AlternativeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAlternative()  ||  (end== tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( begin.getHeadConcAlternative() ,( tom.gom.adt.gom.types.AlternativeList )tom_get_slice_ConcAlternative( begin.getTailConcAlternative() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {     if( l1.isEmptyConcModule() ) {       return l2;     } else if( l2.isEmptyConcModule() ) {       return l1;     } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.HookDeclList  tom_append_list_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList l1,  tom.gom.adt.gom.types.HookDeclList  l2) {     if( l1.isEmptyConcHookDecl() ) {       return l2;     } else if( l2.isEmptyConcHookDecl() ) {       return l1;     } else if(  l1.getTailConcHookDecl() .isEmptyConcHookDecl() ) {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,tom_append_list_ConcHookDecl( l1.getTailConcHookDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.HookDeclList  tom_get_slice_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList  begin,  tom.gom.adt.gom.types.HookDeclList  end, tom.gom.adt.gom.types.HookDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHookDecl()  ||  (end== tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( begin.getHeadConcHookDecl() ,( tom.gom.adt.gom.types.HookDeclList )tom_get_slice_ConcHookDecl( begin.getTailConcHookDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.Option  tom_append_list_OptionList( tom.gom.adt.gom.types.Option  l1,  tom.gom.adt.gom.types.Option  l2) {     if( l1.isEmptyOptionList() ) {       return l2;     } else if( l2.isEmptyOptionList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) ) {       if(  l1.getTailOptionList() .isEmptyOptionList() ) {         return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,l2) ;       } else {         return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,tom_append_list_OptionList( l1.getTailOptionList() ,l2)) ;       }     } else {       return  tom.gom.adt.gom.types.option.ConsOptionList.make(l1,l2) ;     }   }   private static   tom.gom.adt.gom.types.Option  tom_get_slice_OptionList( tom.gom.adt.gom.types.Option  begin,  tom.gom.adt.gom.types.Option  end, tom.gom.adt.gom.types.Option  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOptionList()  ||  (end== tom.gom.adt.gom.types.option.EmptyOptionList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.option.ConsOptionList.make((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getHeadOptionList() :begin),( tom.gom.adt.gom.types.Option )tom_get_slice_OptionList((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getTailOptionList() : tom.gom.adt.gom.types.option.EmptyOptionList.make() ),end,tail)) ;   }      private static   tom.gom.adt.gom.types.OperatorDeclList  tom_append_list_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList l1,  tom.gom.adt.gom.types.OperatorDeclList  l2) {     if( l1.isEmptyConcOperator() ) {       return l2;     } else if( l2.isEmptyConcOperator() ) {       return l1;     } else if(  l1.getTailConcOperator() .isEmptyConcOperator() ) {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,l2) ;     } else {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,tom_append_list_ConcOperator( l1.getTailConcOperator() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.OperatorDeclList  tom_get_slice_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList  begin,  tom.gom.adt.gom.types.OperatorDeclList  end, tom.gom.adt.gom.types.OperatorDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcOperator()  ||  (end== tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( begin.getHeadConcOperator() ,( tom.gom.adt.gom.types.OperatorDeclList )tom_get_slice_ConcOperator( begin.getTailConcOperator() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GrammarList  tom_append_list_ConcGrammar( tom.gom.adt.gom.types.GrammarList l1,  tom.gom.adt.gom.types.GrammarList  l2) {     if( l1.isEmptyConcGrammar() ) {       return l2;     } else if( l2.isEmptyConcGrammar() ) {       return l1;     } else if(  l1.getTailConcGrammar() .isEmptyConcGrammar() ) {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,l2) ;     } else {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,tom_append_list_ConcGrammar( l1.getTailConcGrammar() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GrammarList  tom_get_slice_ConcGrammar( tom.gom.adt.gom.types.GrammarList  begin,  tom.gom.adt.gom.types.GrammarList  end, tom.gom.adt.gom.types.GrammarList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGrammar()  ||  (end== tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( begin.getHeadConcGrammar() ,( tom.gom.adt.gom.types.GrammarList )tom_get_slice_ConcGrammar( begin.getTailConcGrammar() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {     if( l1.isEmptyConcSort() ) {       return l2;     } else if( l2.isEmptyConcSort() ) {       return l1;     } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;   }    

  private final ModuleList moduleList;
  private ArrayList<Decl> sortsWithGraphrules;
  private GomEnvironment gomEnvironment;

  public HookTypeExpander(ModuleList moduleList,GomEnvironment gomEnvironment) {
    this.moduleList = moduleList;
    sortsWithGraphrules = new ArrayList<Decl>();
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  /**
    * Get the correct types for hooks, and attach them the correct Decl
    */
  public HookDeclList expand(GomModuleList gomModuleList) {
    HookDeclList hookList =  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
    /* For each hook in the GomModuleList */
    {{if ( (((Object)gomModuleList) instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )(( tom.gom.adt.gom.types.GomModuleList )((Object)gomModuleList))) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )(( tom.gom.adt.gom.types.GomModuleList )((Object)gomModuleList))) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch611__end__4=(( tom.gom.adt.gom.types.GomModuleList )((Object)gomModuleList));do {{if (!( tomMatch611__end__4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch611_9= tomMatch611__end__4.getHeadConcGomModule() ;if ( (tomMatch611_9 instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )tomMatch611_9) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch611_7= tomMatch611_9.getModuleName() ; tom.gom.adt.gom.types.SectionList  tomMatch611_8= tomMatch611_9.getSectionList() ;if ( (tomMatch611_7 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch611_7) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) { String  tom_moduleName= tomMatch611_7.getName() ;if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch611_8) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch611_8) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch611__end__17=tomMatch611_8;do {{if (!( tomMatch611__end__17.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch611_21= tomMatch611__end__17.getHeadConcSection() ;if ( (tomMatch611_21 instanceof tom.gom.adt.gom.types.Section) ) {if ( ((( tom.gom.adt.gom.types.Section )tomMatch611_21) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch611_20= tomMatch611_21.getGrammarList() ;if ( (((( tom.gom.adt.gom.types.GrammarList )tomMatch611_20) instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || ((( tom.gom.adt.gom.types.GrammarList )tomMatch611_20) instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch611__end__26=tomMatch611_20;do {{if (!( tomMatch611__end__26.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch611_30= tomMatch611__end__26.getHeadConcGrammar() ;if ( (tomMatch611_30 instanceof tom.gom.adt.gom.types.Grammar) ) {if ( ((( tom.gom.adt.gom.types.Grammar )tomMatch611_30) instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tom_prodList= tomMatch611_30.getProductionList() ;{{if ( (((Object)tom_prodList) instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList))) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList))) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch612__end__4=(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList));do {{if (!( tomMatch612__end__4.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tom_prod= tomMatch612__end__4.getHeadConcProduction() ;











            /* Process hooks attached to a module */
            {{if ( (((Object)tom_prod) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )((Object)tom_prod)) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )((Object)tom_prod))) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.IdKind  tomMatch613_1= (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getNameType() ;if ( (tomMatch613_1 instanceof tom.gom.adt.gom.types.IdKind) ) {if ( ((( tom.gom.adt.gom.types.IdKind )tomMatch613_1) instanceof tom.gom.adt.gom.types.idkind.KindModule) ) { String  tom_mname= (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getName() ;

                //Graph-rewrite rules hooks are only allowed for sorts
                if( (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getHookType() .getkind().equals("graphrules")) {
                  GomMessage.error(getLogger(), null, 0, 
                      GomMessage.graphRulesHooksAuthOnSorts);
                }
                if(tom_mname.equals(tom_moduleName)) {
                  ModuleDecl mdecl = getModuleDecl(tom_mname,moduleList);
                  HookDeclList newDeclList =
                    makeHookDeclList((( tom.gom.adt.gom.types.Production )((Object)tom_prod)), tom.gom.adt.gom.types.decl.CutModule.make(mdecl) );
                  hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
                } else {
                  GomMessage.error(getLogger(), null, 0, 
                      GomMessage.moduleHooksAuthOnCurrentModule);
                }
              }}}}}}{if ( (((Object)tom_prod) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )((Object)tom_prod)) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )((Object)tom_prod))) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.IdKind  tomMatch613_9= (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getNameType() ;if ( (tomMatch613_9 instanceof tom.gom.adt.gom.types.IdKind) ) {if ( ((( tom.gom.adt.gom.types.IdKind )tomMatch613_9) instanceof tom.gom.adt.gom.types.idkind.KindSort) ) {

                SortDecl sdecl = getSortDecl( (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getName() ,tom_moduleName,moduleList);
                HookDeclList newDeclList = makeHookDeclList((( tom.gom.adt.gom.types.Production )((Object)tom_prod)), tom.gom.adt.gom.types.decl.CutSort.make(sdecl) );
                hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
              }}}}}}{if ( (((Object)tom_prod) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )((Object)tom_prod)) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )((Object)tom_prod))) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.IdKind  tomMatch613_16= (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getNameType() ;if ( (tomMatch613_16 instanceof tom.gom.adt.gom.types.IdKind) ) {if ( ((( tom.gom.adt.gom.types.IdKind )tomMatch613_16) instanceof tom.gom.adt.gom.types.idkind.KindOperator) ) {

                //Graph-rewrite rules hooks are only allowed for sorts
                if( (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getHookType() .getkind().equals("graphrules")) {
                  GomMessage.error(getLogger(), null, 0, 
                      GomMessage.graphRulesHooksAuthOnSorts);
                }
                OperatorDecl odecl = getOperatorDecl( (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getName() ,tom_moduleName,moduleList);
                if(odecl!=null) {
                  HookDeclList newDeclList = makeHookDeclList((( tom.gom.adt.gom.types.Production )((Object)tom_prod)), tom.gom.adt.gom.types.decl.CutOperator.make(odecl) );
                  hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
                }
              }}}}}}{if ( (((Object)tom_prod) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )((Object)tom_prod)) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )((Object)tom_prod))) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.IdKind  tomMatch613_24= (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getNameType() ;if ( (tomMatch613_24 instanceof tom.gom.adt.gom.types.IdKind) ) {if ( ((( tom.gom.adt.gom.types.IdKind )tomMatch613_24) instanceof tom.gom.adt.gom.types.idkind.KindFutureOperator) ) {

                OperatorDecl odecl = getOperatorDecl( (( tom.gom.adt.gom.types.Production )((Object)tom_prod)).getName() ,tom_moduleName,moduleList);
                if(odecl!=null) {
                  HookDeclList newDeclList =
                    makeHookDeclList((( tom.gom.adt.gom.types.Production )((Object)tom_prod)), tom.gom.adt.gom.types.decl.CutFutureOperator.make(odecl,  tomMatch613_24.getConsOrNil() ) );
                  hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
                }
              }}}}}}}

          }if ( tomMatch612__end__4.isEmptyConcProduction() ) {tomMatch612__end__4=(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList));} else {tomMatch612__end__4= tomMatch612__end__4.getTailConcProduction() ;}}} while(!( (tomMatch612__end__4==(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList))) ));}}}}

        ArrayList<String> examinedOps = new ArrayList<String>();
        {{if ( (((Object)tom_prodList) instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList))) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList))) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch614__end__4=(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList));do {{if (!( tomMatch614__end__4.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch614_8= tomMatch614__end__4.getHeadConcProduction() ;if ( (tomMatch614_8 instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tomMatch614_8) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch614_7= tomMatch614_8.getAlternativeList() ;if ( (((( tom.gom.adt.gom.types.AlternativeList )tomMatch614_7) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )tomMatch614_7) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch614__end__13=tomMatch614_7;do {{if (!( tomMatch614__end__13.isEmptyConcAlternative() )) {

            hookList = addDefaultTheoryHooks( tomMatch614__end__13.getHeadConcAlternative() ,hookList,examinedOps,tom_moduleName);
          }if ( tomMatch614__end__13.isEmptyConcAlternative() ) {tomMatch614__end__13=tomMatch614_7;} else {tomMatch614__end__13= tomMatch614__end__13.getTailConcAlternative() ;}}} while(!( (tomMatch614__end__13==tomMatch614_7) ));}}}}if ( tomMatch614__end__4.isEmptyConcProduction() ) {tomMatch614__end__4=(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList));} else {tomMatch614__end__4= tomMatch614__end__4.getTailConcProduction() ;}}} while(!( (tomMatch614__end__4==(( tom.gom.adt.gom.types.ProductionList )((Object)tom_prodList))) ));}}}}

      }}}if ( tomMatch611__end__26.isEmptyConcGrammar() ) {tomMatch611__end__26=tomMatch611_20;} else {tomMatch611__end__26= tomMatch611__end__26.getTailConcGrammar() ;}}} while(!( (tomMatch611__end__26==tomMatch611_20) ));}}}}if ( tomMatch611__end__17.isEmptyConcSection() ) {tomMatch611__end__17=tomMatch611_8;} else {tomMatch611__end__17= tomMatch611__end__17.getTailConcSection() ;}}} while(!( (tomMatch611__end__17==tomMatch611_8) ));}}}}}}if ( tomMatch611__end__4.isEmptyConcGomModule() ) {tomMatch611__end__4=(( tom.gom.adt.gom.types.GomModuleList )((Object)gomModuleList));} else {tomMatch611__end__4= tomMatch611__end__4.getTailConcGomModule() ;}}} while(!( (tomMatch611__end__4==(( tom.gom.adt.gom.types.GomModuleList )((Object)gomModuleList))) ));}}}}

    return hookList;
  }

  private HookDeclList addDefaultTheoryHooks(Alternative alt,
                                             HookDeclList hookList,
                                             ArrayList<String> examinedOps,
                                             String moduleName) {
    {{if ( (((Object)alt) instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )((Object)alt)) instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )(( tom.gom.adt.gom.types.Alternative )((Object)alt))) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { tom.gom.adt.gom.types.FieldList  tomMatch615_3= (( tom.gom.adt.gom.types.Alternative )((Object)alt)).getDomainList() ; String  tom_opName= (( tom.gom.adt.gom.types.Alternative )((Object)alt)).getName() ;if ( (((( tom.gom.adt.gom.types.FieldList )tomMatch615_3) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )tomMatch615_3) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( tomMatch615_3.isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch615_12= tomMatch615_3.getHeadConcField() ;if ( (tomMatch615_12 instanceof tom.gom.adt.gom.types.Field) ) {if ( ((( tom.gom.adt.gom.types.Field )tomMatch615_12) instanceof tom.gom.adt.gom.types.field.StarredField) ) {if (  tomMatch615_3.getTailConcField() .isEmptyConcField() ) {if ( ( tomMatch615_12.getFieldType() == (( tom.gom.adt.gom.types.Alternative )((Object)alt)).getCodomain() ) ) {if ( (((Object)hookList) instanceof tom.gom.adt.gom.types.HookDeclList) ) {boolean tomMatch615_31= false ;if ( (((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList))) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList))) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch615__end__17=(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList));do {{if (!( tomMatch615__end__17.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tomMatch615_22= tomMatch615__end__17.getHeadConcHookDecl() ;if ( (tomMatch615_22 instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )tomMatch615_22) instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) { tom.gom.adt.gom.types.Decl  tomMatch615_21= tomMatch615_22.getPointcut() ;if ( (tomMatch615_21 instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )tomMatch615_21) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch615_24= tomMatch615_21.getODecl() ;if ( (tomMatch615_24 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch615_24) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {if ( tom_opName.equals( tomMatch615_24.getName() ) ) {tomMatch615_31= true ;}}}}}}}}if ( tomMatch615__end__17.isEmptyConcHookDecl() ) {tomMatch615__end__17=(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList));} else {tomMatch615__end__17= tomMatch615__end__17.getTailConcHookDecl() ;}}} while(!( (tomMatch615__end__17==(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList))) ));}if (!(tomMatch615_31)) {




        /* generate a FL hook for list-operators without other hook */
        String emptyCode = "{}";
        Production hook =  tom.gom.adt.gom.types.production.Hook.make( tom.gom.adt.gom.types.idkind.KindOperator.make() , tom_opName,  tom.gom.adt.gom.types.hookkind.HookKind.make("FL") ,  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() , emptyCode,  tom.gom.adt.gom.types.option.EmptyOptionList.make() ) ;
        OperatorDecl odecl = getOperatorDecl(tom_opName,moduleName,moduleList);
        if (odecl!=null) {
          HookDeclList newDeclList = makeHookDeclList(hook, tom.gom.adt.gom.types.decl.CutOperator.make(odecl) );
          hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
        }
      }}}}}}}}}}}}{if ( (((Object)alt) instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )((Object)alt)) instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )(( tom.gom.adt.gom.types.Alternative )((Object)alt))) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { tom.gom.adt.gom.types.FieldList  tomMatch615_35= (( tom.gom.adt.gom.types.Alternative )((Object)alt)).getDomainList() ; String  tom_opName= (( tom.gom.adt.gom.types.Alternative )((Object)alt)).getName() ;if ( (((( tom.gom.adt.gom.types.FieldList )tomMatch615_35) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )tomMatch615_35) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( tomMatch615_35.isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch615_50= tomMatch615_35.getHeadConcField() ;if ( (tomMatch615_50 instanceof tom.gom.adt.gom.types.Field) ) {if ( ((( tom.gom.adt.gom.types.Field )tomMatch615_50) instanceof tom.gom.adt.gom.types.field.StarredField) ) {if (  tomMatch615_35.getTailConcField() .isEmptyConcField() ) {if ( ( tomMatch615_50.getFieldType() == (( tom.gom.adt.gom.types.Alternative )((Object)alt)).getCodomain() ) ) {if ( (((Object)hookList) instanceof tom.gom.adt.gom.types.HookDeclList) ) {if ( (((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList))) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList))) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch615__end__45=(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList));do {{if (!( tomMatch615__end__45.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tomMatch615_53= tomMatch615__end__45.getHeadConcHookDecl() ;if ( (tomMatch615_53 instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )tomMatch615_53) instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) { tom.gom.adt.gom.types.HookKind  tomMatch615_52= tomMatch615_53.getHookType() ;if ( (tomMatch615_52 instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tomMatch615_52) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch615_55= tomMatch615_52.getkind() ;boolean tomMatch615_63= false ; String  tomMatch615_59= "" ; String  tomMatch615_61= "" ; String  tomMatch615_60= "" ;if ( true ) {if ( "make_insert".equals(tomMatch615_55) ) {{tomMatch615_63= true ;tomMatch615_59=tomMatch615_55;}} else {if ( true ) {if ( "make_empty".equals(tomMatch615_55) ) {{tomMatch615_63= true ;tomMatch615_60=tomMatch615_55;}} else {if ( true ) {if ( "rules".equals(tomMatch615_55) ) {{tomMatch615_63= true ;tomMatch615_61=tomMatch615_55;}}}}}}}if (tomMatch615_63) {





        if (!examinedOps.contains(tom_opName)) {
          examinedOps.add(tom_opName);
          {{if ( (((Object)hookList) instanceof tom.gom.adt.gom.types.HookDeclList) ) {if ( true ) {boolean tomMatch616_28= false ;if ( (((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList))) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList))) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch616__end__5=(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList));do {{if (!( tomMatch616__end__5.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tomMatch616_10= tomMatch616__end__5.getHeadConcHookDecl() ;if ( (tomMatch616_10 instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )tomMatch616_10) instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) { tom.gom.adt.gom.types.Decl  tomMatch616_8= tomMatch616_10.getPointcut() ; tom.gom.adt.gom.types.HookKind  tomMatch616_9= tomMatch616_10.getHookType() ;if ( (tomMatch616_8 instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )tomMatch616_8) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch616_12= tomMatch616_8.getODecl() ;if ( (tomMatch616_12 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch616_12) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {if ( (( String )tom_opName).equals( tomMatch616_12.getName() ) ) {if ( (tomMatch616_9 instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tomMatch616_9) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch616_18= tomMatch616_9.getkind() ;boolean tomMatch616_29= false ; String  tomMatch616_22= "" ; String  tomMatch616_23= "" ; String  tomMatch616_26= "" ; String  tomMatch616_24= "" ; String  tomMatch616_25= "" ;if ( true ) {if ( "Free".equals(tomMatch616_18) ) {{tomMatch616_29= true ;tomMatch616_22=tomMatch616_18;}} else {if ( true ) {if ( "FL".equals(tomMatch616_18) ) {{tomMatch616_29= true ;tomMatch616_23=tomMatch616_18;}} else {if ( true ) {if ( "AU".equals(tomMatch616_18) ) {{tomMatch616_29= true ;tomMatch616_24=tomMatch616_18;}} else {if ( true ) {if ( "AC".equals(tomMatch616_18) ) {{tomMatch616_29= true ;tomMatch616_25=tomMatch616_18;}} else {if ( true ) {if ( "ACU".equals(tomMatch616_18) ) {{tomMatch616_29= true ;tomMatch616_26=tomMatch616_18;}}}}}}}}}}}if (tomMatch616_29) {tomMatch616_28= true ;}}}}}}}}}}}if ( tomMatch616__end__5.isEmptyConcHookDecl() ) {tomMatch616__end__5=(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList));} else {tomMatch616__end__5= tomMatch616__end__5.getTailConcHookDecl() ;}}} while(!( (tomMatch616__end__5==(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList))) ));}if (!(tomMatch616_28)) {


              /* generate an error to make users specify the theory */
              GomMessage.error(getLogger(), null, 0, 
                  GomMessage.mustSpecifyAssociatedTheoryForVarOp,tom_opName);
            }}}}}

        }
      }}}}}}if ( tomMatch615__end__45.isEmptyConcHookDecl() ) {tomMatch615__end__45=(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList));} else {tomMatch615__end__45= tomMatch615__end__45.getTailConcHookDecl() ;}}} while(!( (tomMatch615__end__45==(( tom.gom.adt.gom.types.HookDeclList )((Object)hookList))) ));}}}}}}}}}}}}}

    return hookList;
  }

  private HookDeclList makeHookDeclList(Production hook, Decl mdecl) {
    {{if ( (((Object)hook) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )((Object)hook)) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )((Object)hook))) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.HookKind  tom_hkind= (( tom.gom.adt.gom.types.Production )((Object)hook)).getHookType() ; String  tom_hName= (( tom.gom.adt.gom.types.Production )((Object)hook)).getName() ; tom.gom.adt.gom.types.ArgList  tom_hookArgs= (( tom.gom.adt.gom.types.Production )((Object)hook)).getArgs() ; String  tom_scode= (( tom.gom.adt.gom.types.Production )((Object)hook)).getStringCode() ;




          HookDeclList newHookList =  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
          {{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_1= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "block".equals(tomMatch618_1) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ,  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_7= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "javablock".equals(tomMatch618_7) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ,  false ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_13= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "interface".equals(tomMatch618_13) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.InterfaceHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_19= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "import".equals(tomMatch618_19) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.ImportHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_25= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "mapping".equals(tomMatch618_25) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MappingHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_31= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;boolean tomMatch618_38= false ; String  tomMatch618_37= "" ; String  tomMatch618_35= "" ; String  tomMatch618_36= "" ;if ( true ) {if ( "make".equals(tomMatch618_31) ) {{tomMatch618_38= true ;tomMatch618_35=tomMatch618_31;}} else {if ( true ) {if ( "make_insert".equals(tomMatch618_31) ) {{tomMatch618_38= true ;tomMatch618_36=tomMatch618_31;}} else {if ( true ) {if ( "make_empty".equals(tomMatch618_31) ) {{tomMatch618_38= true ;tomMatch618_37=tomMatch618_31;}}}}}}}if (tomMatch618_38) {

              SlotList typedArgs = typeArguments(tom_hookArgs,tom_hkind,mdecl);
              if (null == typedArgs) {
                GomMessage.error(getLogger(),null,0,
                    GomMessage.discardedHook, (tom_hName));
                return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
              }
              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl, typedArgs,  tom.gom.adt.code.types.code.Code.make(tom_scode) , (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)),  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_40= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "Free".equals(tomMatch618_40) ) {

              /* Even there is no code associated, we generate a MakeHook to
               * prevent FL hooks to be automatically generated */
              return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ,  tom.gom.adt.code.types.code.Code.make("") ,  tom.gom.adt.gom.types.hookkind.HookKind.make("Free") ,  false ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_46= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "FL".equals(tomMatch618_46) ) {

              /* FL: flattened list */
              return makeFLHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_52= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "AU".equals(tomMatch618_52) ) {

              return makeAUHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_58= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "AC".equals(tomMatch618_58) ) {

              return makeACHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_64= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "ACU".equals(tomMatch618_64) ) {

              return makeACUHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_70= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "rules".equals(tomMatch618_70) ) {

              return makeRulesHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (((Object)tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch618_76= (( tom.gom.adt.gom.types.HookKind )((Object)tom_hkind)).getkind() ;if ( true ) {if ( "graphrules".equals(tomMatch618_76) ) {

              //TODO: verify if the option termgraph is on
              if(tom_hookArgs.length()!=2) {
                throw new GomRuntimeException(
                    "GomTypeExpander:graphrules hooks need two parameters: the name of the generated strategy and its default behaviour");
              }
              return makeGraphRulesHookList(tom_hName,tom_hookArgs,mdecl,tom_scode);
            }}}}}}}

          if (newHookList ==  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) {
            GomMessage.error(getLogger(),null,0,
                GomMessage.unknownHookKind, (tom_hkind));
          }
          return newHookList;
        }}}}}

    throw new GomRuntimeException("HookTypeExpander: this hook is not a hook: "+ hook);
  }

  /**
   * Finds the ModuleDecl corresponding to a module name.
   *
   * @param mname the module name
   * @param moduleList the queried ModuleList
   * @return the ModuleDecl for mname
   */
  private ModuleDecl getModuleDecl(String mname, ModuleList moduleList) {
    {{if ( (((Object)moduleList) instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch619__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));do {{if (!( tomMatch619__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch619_8= tomMatch619__end__4.getHeadConcModule() ;if ( (tomMatch619_8 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch619_8) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch619_7= tomMatch619_8.getMDecl() ;if ( (tomMatch619_7 instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )tomMatch619_7) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch619_10= tomMatch619_7.getModuleName() ;if ( (tomMatch619_10 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch619_10) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {




        if ( tomMatch619_10.getName() .equals(mname)) {
          return tomMatch619_7;
        }
      }}}}}}}if ( tomMatch619__end__4.isEmptyConcModule() ) {tomMatch619__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));} else {tomMatch619__end__4= tomMatch619__end__4.getTailConcModule() ;}}} while(!( (tomMatch619__end__4==(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) ));}}}}

    throw new GomRuntimeException(
        "HookTypeExpander: Module not found: "+mname);
  }

  /**
   * Finds the SortDecl corresponding to a sort name.
   *
   * @param sname the sort name
   * @param modName the module name that should contain the sort
   * @param moduleList the queried ModuleList
   * @return the SortDecl for sname
   */
  private SortDecl getSortDecl(String sname, String modName, ModuleList moduleList) {
    {{if ( true ) {if ( (((Object)moduleList) instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch620__end__5=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));do {{if (!( tomMatch620__end__5.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch620_10= tomMatch620__end__5.getHeadConcModule() ;if ( (tomMatch620_10 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch620_10) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch620_8= tomMatch620_10.getMDecl() ; tom.gom.adt.gom.types.SortList  tomMatch620_9= tomMatch620_10.getSorts() ;if ( (tomMatch620_8 instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )tomMatch620_8) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch620_12= tomMatch620_8.getModuleName() ;if ( (tomMatch620_12 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch620_12) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {if ( (( String )modName).equals( tomMatch620_12.getName() ) ) {if ( (((( tom.gom.adt.gom.types.SortList )tomMatch620_9) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )tomMatch620_9) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch620__end__21=tomMatch620_9;do {{if (!( tomMatch620__end__21.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch620_26= tomMatch620__end__21.getHeadConcSort() ;if ( (tomMatch620_26 instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )tomMatch620_26) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch620_25= tomMatch620_26.getDecl() ;if ( (tomMatch620_25 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch620_25) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {






        if ( tomMatch620_25.getName() .equals(sname)) {
          return tomMatch620_25;
        }
      }}}}}if ( tomMatch620__end__21.isEmptyConcSort() ) {tomMatch620__end__21=tomMatch620_9;} else {tomMatch620__end__21= tomMatch620__end__21.getTailConcSort() ;}}} while(!( (tomMatch620__end__21==tomMatch620_9) ));}}}}}}}}}if ( tomMatch620__end__5.isEmptyConcModule() ) {tomMatch620__end__5=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));} else {tomMatch620__end__5= tomMatch620__end__5.getTailConcModule() ;}}} while(!( (tomMatch620__end__5==(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) ));}}}}}

    throw new GomRuntimeException(
        "HookTypeExpander: Sort not found: "+sname);
  }

  /**
   * Finds the OperatorDecl corresponding to an operator name.
   *
   * @param oname the sort name
   * @param modName the module name that should contain the operator
   * @param moduleList the queried ModuleList
   * @return the OperatorDecl for sname
   */
  private OperatorDecl getOperatorDecl(
      String oname,
      String modName,
      ModuleList moduleList) {
    {{if ( true ) {if ( (((Object)moduleList) instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch621__end__5=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));do {{if (!( tomMatch621__end__5.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch621_10= tomMatch621__end__5.getHeadConcModule() ;if ( (tomMatch621_10 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch621_10) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch621_8= tomMatch621_10.getMDecl() ; tom.gom.adt.gom.types.SortList  tomMatch621_9= tomMatch621_10.getSorts() ;if ( (tomMatch621_8 instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )tomMatch621_8) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch621_12= tomMatch621_8.getModuleName() ;if ( (tomMatch621_12 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch621_12) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {if ( (( String )modName).equals( tomMatch621_12.getName() ) ) {if ( (((( tom.gom.adt.gom.types.SortList )tomMatch621_9) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )tomMatch621_9) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch621__end__21=tomMatch621_9;do {{if (!( tomMatch621__end__21.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch621_26= tomMatch621__end__21.getHeadConcSort() ;if ( (tomMatch621_26 instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )tomMatch621_26) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch621_25= tomMatch621_26.getOperatorDecls() ;if ( (((( tom.gom.adt.gom.types.OperatorDeclList )tomMatch621_25) instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || ((( tom.gom.adt.gom.types.OperatorDeclList )tomMatch621_25) instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch621__end__31=tomMatch621_25;do {{if (!( tomMatch621__end__31.isEmptyConcOperator() )) { tom.gom.adt.gom.types.OperatorDecl  tomMatch621_35= tomMatch621__end__31.getHeadConcOperator() ;if ( (tomMatch621_35 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch621_35) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {








        if ( tomMatch621_35.getName() .equals(oname)) {
          return  tomMatch621__end__31.getHeadConcOperator() ;
        }
      }}}if ( tomMatch621__end__31.isEmptyConcOperator() ) {tomMatch621__end__31=tomMatch621_25;} else {tomMatch621__end__31= tomMatch621__end__31.getTailConcOperator() ;}}} while(!( (tomMatch621__end__31==tomMatch621_25) ));}}}}if ( tomMatch621__end__21.isEmptyConcSort() ) {tomMatch621__end__21=tomMatch621_9;} else {tomMatch621__end__21= tomMatch621__end__21.getTailConcSort() ;}}} while(!( (tomMatch621__end__21==tomMatch621_9) ));}}}}}}}}}if ( tomMatch621__end__5.isEmptyConcModule() ) {tomMatch621__end__5=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));} else {tomMatch621__end__5= tomMatch621__end__5.getTailConcModule() ;}}} while(!( (tomMatch621__end__5==(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) ));}}}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.orphanedHook, oname);
    return null;
  }

  private SlotList typeArguments(
      ArgList args,
      HookKind kind,
      Decl decl) {
    {{if ( (((Object)kind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)kind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)kind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch622_1= (( tom.gom.adt.gom.types.HookKind )((Object)kind)).getkind() ;if ( true ) {if ( "make".equals(tomMatch622_1) ) {

        /*
         * The TypedProduction has to be Slots
         * A KindMakeHook is attached to an operator
         */
        {{if ( (((Object)decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )((Object)decl)) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )((Object)decl))) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch623_1= (( tom.gom.adt.gom.types.Decl )((Object)decl)).getODecl() ;if ( (tomMatch623_1 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch623_1) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch623_4= tomMatch623_1.getProd() ;if ( (tomMatch623_4 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch623_4) instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tom_slotList= tomMatch623_4.getSlots() ;

            /* tests the arguments number */
            if (args.length() != tom_slotList.length()) {
              SlotList slist = tom_slotList;
              GomMessage.error(getLogger(), null, 0,
                  GomMessage.mismatchedMakeArguments, args, slist);
              return null;
            }
            /* Then check the types */
            return recArgSlots(args,tom_slotList);
          }}}}}}}}{if ( (((Object)decl) instanceof tom.gom.adt.gom.types.Decl) ) {

            GomMessage.error(getLogger(), null, 0,
                GomMessage.unsupportedHookAlgebraic, kind);
            return null;
          }}}

      }}}}}}{if ( (((Object)kind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)kind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)kind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch622_7= (( tom.gom.adt.gom.types.HookKind )((Object)kind)).getkind() ;if ( true ) {if ( "make_insert".equals(tomMatch622_7) ) {

        /*
         * The TypedProduction has to be Variadic
         * Then we get the codomain from the operatordecl
         */
        {{if ( (((Object)decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )((Object)decl)) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )((Object)decl))) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch624_1= (( tom.gom.adt.gom.types.Decl )((Object)decl)).getODecl() ;if ( (tomMatch624_1 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch624_1) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch624_5= tomMatch624_1.getProd() ;if ( (tomMatch624_5 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch624_5) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {


              // for a make_insert hook, there are two arguments: head, tail
              {{if ( (((Object)args) instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if (!( (( tom.gom.adt.gom.types.ArgList )((Object)args)).isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch625_5= (( tom.gom.adt.gom.types.ArgList )((Object)args)).getHeadConcArg() ;if ( (tomMatch625_5 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch625_5) instanceof tom.gom.adt.gom.types.arg.Arg) ) { tom.gom.adt.gom.types.ArgList  tomMatch625_2= (( tom.gom.adt.gom.types.ArgList )((Object)args)).getTailConcArg() ;if (!( tomMatch625_2.isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch625_8= tomMatch625_2.getHeadConcArg() ;if ( (tomMatch625_8 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch625_8) instanceof tom.gom.adt.gom.types.arg.Arg) ) {if (  tomMatch625_2.getTailConcArg() .isEmptyConcArg() ) {

                  return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch625_5.getName() ,  tomMatch624_5.getSort() ) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch625_8.getName() ,  tomMatch624_1.getSort() ) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ;
                }}}}}}}}}}{if ( (((Object)args) instanceof tom.gom.adt.gom.types.ArgList) ) {

                  GomMessage.error(getLogger(), null, 0,
                      GomMessage.badHookArguments, 
                      (tomMatch622_7), Integer.valueOf(args.length()));
                  return null;
                }}}

            }}}}}}}}{if ( (((Object)decl) instanceof tom.gom.adt.gom.types.Decl) ) {

            GomMessage.error(getLogger(),null, 0,
                GomMessage.unsupportedHookVariadic, kind);
            return null;
          }}}

      }}}}}}{if ( (((Object)kind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )((Object)kind)) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )((Object)kind))) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch622_13= (( tom.gom.adt.gom.types.HookKind )((Object)kind)).getkind() ;if ( true ) {if ( "make_empty".equals(tomMatch622_13) ) {

        /*
         * The TypedProduction has to be Variadic
         * Then we get the codomain from the operatordecl
         */
        {{if ( (((Object)decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )((Object)decl)) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )((Object)decl))) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch626_1= (( tom.gom.adt.gom.types.Decl )((Object)decl)).getODecl() ;if ( (tomMatch626_1 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch626_1) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch626_4= tomMatch626_1.getProd() ;if ( (tomMatch626_4 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch626_4) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {


              // for a make_empty hook, there is no argument
              {{if ( (((Object)args) instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if ( (( tom.gom.adt.gom.types.ArgList )((Object)args)).isEmptyConcArg() ) {
 return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ; }}}}{if ( (((Object)args) instanceof tom.gom.adt.gom.types.ArgList) ) {

                  GomMessage.error(getLogger(), null, 0,
                      GomMessage.badHookArguments,
                      (tomMatch622_13), Integer.valueOf(args.length()));
                  return null;
                }}}

            }}}}}}}}{if ( (((Object)decl) instanceof tom.gom.adt.gom.types.Decl) ) {

            GomMessage.error(getLogger(), null, 0,
                GomMessage.unsupportedHookVariadic, kind);
            return null;
          }}}

      }}}}}}}

    throw new GomRuntimeException("Hook kind \""+kind+"\" not supported");
  }

  private SlotList recArgSlots(ArgList args, SlotList slots) {
    {{if ( (((Object)args) instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if ( (( tom.gom.adt.gom.types.ArgList )((Object)args)).isEmptyConcArg() ) {if ( (((Object)slots) instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )(( tom.gom.adt.gom.types.SlotList )((Object)slots))) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )(( tom.gom.adt.gom.types.SlotList )((Object)slots))) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if ( (( tom.gom.adt.gom.types.SlotList )((Object)slots)).isEmptyConcSlot() ) {

        return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
      }}}}}}}{if ( (((Object)args) instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if (!( (( tom.gom.adt.gom.types.ArgList )((Object)args)).isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch628_13= (( tom.gom.adt.gom.types.ArgList )((Object)args)).getHeadConcArg() ;if ( (tomMatch628_13 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch628_13) instanceof tom.gom.adt.gom.types.arg.Arg) ) {if ( (((Object)slots) instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )(( tom.gom.adt.gom.types.SlotList )((Object)slots))) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )(( tom.gom.adt.gom.types.SlotList )((Object)slots))) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if (!( (( tom.gom.adt.gom.types.SlotList )((Object)slots)).isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch628_16= (( tom.gom.adt.gom.types.SlotList )((Object)slots)).getHeadConcSlot() ;if ( (tomMatch628_16 instanceof tom.gom.adt.gom.types.Slot) ) {if ( ((( tom.gom.adt.gom.types.Slot )tomMatch628_16) instanceof tom.gom.adt.gom.types.slot.Slot) ) {

        SlotList tail = recArgSlots( (( tom.gom.adt.gom.types.ArgList )((Object)args)).getTailConcArg() , (( tom.gom.adt.gom.types.SlotList )((Object)slots)).getTailConcSlot() );
        return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch628_13.getName() ,  tomMatch628_16.getSort() ) ,tom_append_list_ConcSlot(tail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;
      }}}}}}}}}}}}

    throw new GomRuntimeException("GomTypeExpander:recArgSlots failed "+args+" "+slots);
  }

  private String trimBracket(String stringCode) {
    int start = stringCode.indexOf('{')+1;
    int end = stringCode.lastIndexOf('}');
    return stringCode.substring(start,end).trim();
  }

  private SortDecl getSortAndCheck(Decl mdecl) {
    {{if ( (((Object)mdecl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )((Object)mdecl)) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )((Object)mdecl))) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch629_1= (( tom.gom.adt.gom.types.Decl )((Object)mdecl)).getODecl() ;if ( (tomMatch629_1 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch629_1) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch629_5= tomMatch629_1.getProd() ;if ( (tomMatch629_5 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch629_5) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) { tom.gom.adt.gom.types.SortDecl  tom_sdecl= tomMatch629_5.getSort() ;

        if ( tomMatch629_1.getSort() == tom_sdecl) {
          return tom_sdecl;
        } else {
          GomMessage.error(getLogger(), null, 0, GomMessage.differentDomainCodomain);
        }
      }}}}}}}}{if ( (((Object)mdecl) instanceof tom.gom.adt.gom.types.Decl) ) {

        GomMessage.error(getLogger(), null, 0, GomMessage.hookFLAUACACUOnlyOnVarOp);
      }}}

    return null;
  }

  /*
   * generate hooks for normalizing rules
   */
  private HookDeclList makeRulesHookList(String opName, Decl mdecl, String scode) {
    RuleExpander rexpander = new RuleExpander(moduleList);
    return rexpander.expandRules(trimBracket(scode));
  }

  /*
   * generate hooks for term-graph rules
   */
  private HookDeclList makeGraphRulesHookList(String sortname, ArgList args, Decl sdecl, String scode) {
    {{if ( (((Object)args) instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )((Object)args))) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if (!( (( tom.gom.adt.gom.types.ArgList )((Object)args)).isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch630_5= (( tom.gom.adt.gom.types.ArgList )((Object)args)).getHeadConcArg() ;if ( (tomMatch630_5 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch630_5) instanceof tom.gom.adt.gom.types.arg.Arg) ) { String  tom_stratname= tomMatch630_5.getName() ; tom.gom.adt.gom.types.ArgList  tomMatch630_2= (( tom.gom.adt.gom.types.ArgList )((Object)args)).getTailConcArg() ;if (!( tomMatch630_2.isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch630_8= tomMatch630_2.getHeadConcArg() ;if ( (tomMatch630_8 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch630_8) instanceof tom.gom.adt.gom.types.arg.Arg) ) { String  tom_defaultstrat= tomMatch630_8.getName() ;if (  tomMatch630_2.getTailConcArg() .isEmptyConcArg() ) {

        if (!tom_defaultstrat.equals("Fail") && !tom_defaultstrat.equals("Identity")) {
          GomMessage.error(getLogger(), null, 0, GomMessage.graphrulHookAuthorizedStrat);
        }
        GraphRuleExpander rexpander = new GraphRuleExpander(moduleList,getGomEnvironment());
        if (sortsWithGraphrules.contains(sdecl)) {
          return rexpander.expandGraphRules(sortname,tom_stratname,tom_defaultstrat,trimBracket(scode),sdecl);
        } else {
          sortsWithGraphrules.add(sdecl);
          return rexpander.expandFirstGraphRules(sortname,tom_stratname,tom_defaultstrat,trimBracket(scode),sdecl);
        }
      }}}}}}}}}}}

    return null;
  }

  /*
   * generate hooks for associative-commutative without neutral element
   */
  private HookDeclList makeACHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain) {
      return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
    }

    HookDeclList acHooks =  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
     /*
      * Remove neutral and flatten:
     * if(<head>.isEmpty<Conc>()) { return <tail>; }
     * if(<head>.isCons<Conc>()) { return make(head.head,make(head.tail,tail)); }
     * if(!<tail>.isCons<Conc>() && !<tail>.isEmpty<Conc>()) { return readMake(<tail>,<empty>); }
     */
    acHooks =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return tail; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head.getHead" + opName+ "(),make(head.getTail" + opName+ "(),tail)); }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (!") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" && !") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head,realMake(tail,Empty" + opName+ ".make())); }\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("FL") ,  false ) ,tom_append_list_ConcHookDecl(acHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) 
















;
    /*
     * add the following hooks:
     * if(tail.isConc) {
     *   if(head < tail.head) {
     *     tmp = head
     *     head = tail.head
     *     tail = cons(tmp,tail.tail)
     *   }
     * }
     * // in all cases:
     * return makeReal(head,tail)
     */
    acHooks =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  if (0 < ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Compare.make( tom.gom.adt.code.types.code.Code.make("head") ,  tom.gom.adt.code.types.code.Code.make("tail.getHead" + opName+ "()") ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(domain) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" tmpHd = head;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    head = tail.getHead" + opName+ "();\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    tail = `"+opName+"(tmpHd,tail.getTail" + opName+ "());\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("}\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("AC") ,  true ) ,tom_append_list_ConcHookDecl(acHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) 
















;
    return acHooks;
  }
  /*
   * generate hooks for associative-commutative with neutral element
   */
  private HookDeclList makeACUHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain) {
      return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
  }

    /* start with AU normalization */
    HookDeclList acuHooks = makeAUHookList(opName, mdecl, scode);
    /*
     * add the following hooks:
     * if(tail.isConc) {
     *   if(head < tail.head) {
     *     tmp = head
     *     head = tail.head
     *     tail = cons(tmp,tail.tail)
     *   }
     * } else if(head < tail) {
     *   tmp = head
     *   head = tail
     *   tail = tmp
     * }
     * // in all cases:
     * return makeReal(head,tail)
     */
    acuHooks =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  if (0 < ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Compare.make( tom.gom.adt.code.types.code.Code.make("head") ,  tom.gom.adt.code.types.code.Code.make("tail.getHead" + opName+ "()") ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(domain) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" tmpHd = head;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    head = tail.getHead" + opName+ "();\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    tail = `"+opName+"(tmpHd,tail.getTail" + opName+ "());\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("} else {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  if (0 < ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Compare.make( tom.gom.adt.code.types.code.Code.make("head") ,  tom.gom.adt.code.types.code.Code.make("tail") ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(domain) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" tmpHd = head;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    head = tail;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    tail = tmpHd;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("}\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("ACU") ,  true ) ,tom_append_list_ConcHookDecl(acuHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) 
























;
    return acuHooks;
  }

  /*
   * generate hooks for associative with neutral element
   */
  private HookDeclList makeAUHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain) {
      return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
    }

    HookDeclList auHooks =  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
    String userNeutral = trimBracket(scode);
    if(userNeutral.length() > 0) {
      /* The hook body is the name of the neutral element */
      auHooks =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ,  tom.gom.adt.code.types.code.Code.make("return "+userNeutral+";") ,  tom.gom.adt.gom.types.hookkind.HookKind.make("AU") ,  true ) ,tom_append_list_ConcHookDecl(auHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) 

;
      /*
       * Remove neutral:
       * if(<head> == makeNeutral) { return <tail>; }
       * if(<tail> == makeNeutral) { return <head>; }
       */
      auHooks =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (head == "+userNeutral+") { return tail; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (tail ==  "+userNeutral+") { return head; }\n") 
              , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("AU") ,  true ) ,tom_append_list_ConcHookDecl(auHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) 







;
    }
    /* getODecl call is safe here, since mdecl was checked by getSortAndCheck */
    /*
     * Remove neutral and flatten:
     * if(<head>.isEmpty<conc>()) { return <tail>; }
     * if(<tail>.isEmpty<conc>()) { return <head>; }
     * if(<head>.isCons<conc>()) { return make(head.head,make(head.tail,tail)); }
     */
    auHooks =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return tail; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return head; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head.getHead" + opName+ "(),make(head.getTail" + opName+ "(),tail)); }\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("AU") ,  false ) ,tom_append_list_ConcHookDecl(auHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) 














;

    return auHooks;
  }

  /*
   * generate hooks for flattened lists (with empty list as last element)
   */
  private HookDeclList makeFLHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if(null == domain) {
      return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
    }

    String userNeutral = trimBracket(scode);
    if(userNeutral.length() > 0) {
      GomMessage.error(getLogger(), null, 0, GomMessage.neutralElmtDefNotAllowed);
    }

    HookDeclList hooks =  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
    /* getODecl call is safe here, since mdecl was checked by getSortAndCheck */
    /*
     * Remove neutral and flatten:
     * if(<head>.isEmpty<Conc>()) { return <tail>; }
     * if(<head>.isCons<Conc>()) { return make(head.head,make(head.tail,tail)); }
     * if(!<tail>.isCons<Conc>() && !<tail>.isEmpty<Conc>()) { return make(<tail>,<empty>); }
     */
    hooks =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return tail; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head.getHead" + opName+ "(),make(head.getTail" + opName+ "(),tail)); }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (!") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" && !") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head,make(tail,Empty" + opName+ ".make())); }\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("FL") ,  false ) ,tom_append_list_ConcHookDecl(hooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) 
















;

    return hooks;
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
