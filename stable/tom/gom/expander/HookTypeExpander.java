/*
 *
 * GOM
 *
 * Copyright (c) 2006-2009, INRIA
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

         private static   tom.gom.adt.code.types.Code  tom_append_list_CodeList( tom.gom.adt.code.types.Code  l1,  tom.gom.adt.code.types.Code  l2) {     if( l1.isEmptyCodeList() ) {       return l2;     } else if( l2.isEmptyCodeList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {       if( (( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? l1.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ).isEmptyCodeList() ) {         return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? l1.getHeadCodeList() :l1),l2) ;       } else {         return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? l1.getHeadCodeList() :l1),tom_append_list_CodeList((( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? l1.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),l2)) ;       }     } else {       return  tom.gom.adt.code.types.code.ConsCodeList.make(l1,l2) ;     }   }   private static   tom.gom.adt.code.types.Code  tom_get_slice_CodeList( tom.gom.adt.code.types.Code  begin,  tom.gom.adt.code.types.Code  end, tom.gom.adt.code.types.Code  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCodeList()  ||  (end== tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getHeadCodeList() :begin),( tom.gom.adt.code.types.Code )tom_get_slice_CodeList((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),end,tail)) ;   }      private static   tom.gom.adt.gom.types.ArgList  tom_append_list_ConcArg( tom.gom.adt.gom.types.ArgList l1,  tom.gom.adt.gom.types.ArgList  l2) {     if( l1.isEmptyConcArg() ) {       return l2;     } else if( l2.isEmptyConcArg() ) {       return l1;     } else if(  l1.getTailConcArg() .isEmptyConcArg() ) {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,l2) ;     } else {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,tom_append_list_ConcArg( l1.getTailConcArg() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ArgList  tom_get_slice_ConcArg( tom.gom.adt.gom.types.ArgList  begin,  tom.gom.adt.gom.types.ArgList  end, tom.gom.adt.gom.types.ArgList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcArg()  ||  (end== tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( begin.getHeadConcArg() ,( tom.gom.adt.gom.types.ArgList )tom_get_slice_ConcArg( begin.getTailConcArg() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GrammarList  tom_append_list_ConcGrammar( tom.gom.adt.gom.types.GrammarList l1,  tom.gom.adt.gom.types.GrammarList  l2) {     if( l1.isEmptyConcGrammar() ) {       return l2;     } else if( l2.isEmptyConcGrammar() ) {       return l1;     } else if(  l1.getTailConcGrammar() .isEmptyConcGrammar() ) {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,l2) ;     } else {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,tom_append_list_ConcGrammar( l1.getTailConcGrammar() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GrammarList  tom_get_slice_ConcGrammar( tom.gom.adt.gom.types.GrammarList  begin,  tom.gom.adt.gom.types.GrammarList  end, tom.gom.adt.gom.types.GrammarList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGrammar()  ||  (end== tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( begin.getHeadConcGrammar() ,( tom.gom.adt.gom.types.GrammarList )tom_get_slice_ConcGrammar( begin.getTailConcGrammar() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.OperatorDeclList  tom_append_list_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList l1,  tom.gom.adt.gom.types.OperatorDeclList  l2) {     if( l1.isEmptyConcOperator() ) {       return l2;     } else if( l2.isEmptyConcOperator() ) {       return l1;     } else if(  l1.getTailConcOperator() .isEmptyConcOperator() ) {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,l2) ;     } else {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,tom_append_list_ConcOperator( l1.getTailConcOperator() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.OperatorDeclList  tom_get_slice_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList  begin,  tom.gom.adt.gom.types.OperatorDeclList  end, tom.gom.adt.gom.types.OperatorDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcOperator()  ||  (end== tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( begin.getHeadConcOperator() ,( tom.gom.adt.gom.types.OperatorDeclList )tom_get_slice_ConcOperator( begin.getTailConcOperator() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.HookDeclList  tom_append_list_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList l1,  tom.gom.adt.gom.types.HookDeclList  l2) {     if( l1.isEmptyConcHookDecl() ) {       return l2;     } else if( l2.isEmptyConcHookDecl() ) {       return l1;     } else if(  l1.getTailConcHookDecl() .isEmptyConcHookDecl() ) {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,tom_append_list_ConcHookDecl( l1.getTailConcHookDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.HookDeclList  tom_get_slice_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList  begin,  tom.gom.adt.gom.types.HookDeclList  end, tom.gom.adt.gom.types.HookDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHookDecl()  ||  (end== tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( begin.getHeadConcHookDecl() ,( tom.gom.adt.gom.types.HookDeclList )tom_get_slice_ConcHookDecl( begin.getTailConcHookDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {     if( l1.isEmptyConcSort() ) {       return l2;     } else if( l2.isEmptyConcSort() ) {       return l1;     } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {     if( l1.isEmptyConcModule() ) {       return l2;     } else if( l2.isEmptyConcModule() ) {       return l1;     } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.Option  tom_append_list_OptionList( tom.gom.adt.gom.types.Option  l1,  tom.gom.adt.gom.types.Option  l2) {     if( l1.isEmptyOptionList() ) {       return l2;     } else if( l2.isEmptyOptionList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) ) {       if( (( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? l1.getTailOptionList() : tom.gom.adt.gom.types.option.EmptyOptionList.make() ).isEmptyOptionList() ) {         return  tom.gom.adt.gom.types.option.ConsOptionList.make((( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? l1.getHeadOptionList() :l1),l2) ;       } else {         return  tom.gom.adt.gom.types.option.ConsOptionList.make((( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? l1.getHeadOptionList() :l1),tom_append_list_OptionList((( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? l1.getTailOptionList() : tom.gom.adt.gom.types.option.EmptyOptionList.make() ),l2)) ;       }     } else {       return  tom.gom.adt.gom.types.option.ConsOptionList.make(l1,l2) ;     }   }   private static   tom.gom.adt.gom.types.Option  tom_get_slice_OptionList( tom.gom.adt.gom.types.Option  begin,  tom.gom.adt.gom.types.Option  end, tom.gom.adt.gom.types.Option  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOptionList()  ||  (end== tom.gom.adt.gom.types.option.EmptyOptionList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.option.ConsOptionList.make((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getHeadOptionList() :begin),( tom.gom.adt.gom.types.Option )tom_get_slice_OptionList((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getTailOptionList() : tom.gom.adt.gom.types.option.EmptyOptionList.make() ),end,tail)) ;   }    

  private ModuleList moduleList;
  private ArrayList sortsWithGraphrules;
  private GomEnvironment gomEnvironment;

  public HookTypeExpander(ModuleList moduleList,GomEnvironment gomEnvironment) {
    this.moduleList = moduleList;
    sortsWithGraphrules = new ArrayList();
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
    {{if ( (gomModuleList instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )gomModuleList) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )gomModuleList) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch519NameNumber_end_4=(( tom.gom.adt.gom.types.GomModuleList )gomModuleList);do {{if (!( tomMatch519NameNumber_end_4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch519NameNumber_freshVar_9= tomMatch519NameNumber_end_4.getHeadConcGomModule() ;if ( (tomMatch519NameNumber_freshVar_9 instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch519NameNumber_freshVar_7= tomMatch519NameNumber_freshVar_9.getModuleName() ; tom.gom.adt.gom.types.SectionList  tomMatch519NameNumber_freshVar_8= tomMatch519NameNumber_freshVar_9.getSectionList() ;if ( (tomMatch519NameNumber_freshVar_7 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) { String  tom_moduleName= tomMatch519NameNumber_freshVar_7.getName() ;if ( ((tomMatch519NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch519NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch519NameNumber_end_15=tomMatch519NameNumber_freshVar_8;do {{if (!( tomMatch519NameNumber_end_15.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch519NameNumber_freshVar_19= tomMatch519NameNumber_end_15.getHeadConcSection() ;if ( (tomMatch519NameNumber_freshVar_19 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch519NameNumber_freshVar_18= tomMatch519NameNumber_freshVar_19.getGrammarList() ;if ( ((tomMatch519NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch519NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch519NameNumber_end_23=tomMatch519NameNumber_freshVar_18;do {{if (!( tomMatch519NameNumber_end_23.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch519NameNumber_freshVar_27= tomMatch519NameNumber_end_23.getHeadConcGrammar() ;if ( (tomMatch519NameNumber_freshVar_27 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tom_prodList= tomMatch519NameNumber_freshVar_27.getProductionList() ;{{if ( (tom_prodList instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch520NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);do {{if (!( tomMatch520NameNumber_end_4.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tom_prod= tomMatch520NameNumber_end_4.getHeadConcProduction() ;











            /* Process hooks attached to a module */
            {{if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.production.Hook) ) {if ( ( (( tom.gom.adt.gom.types.Production )tom_prod).getNameType()  instanceof tom.gom.adt.gom.types.idkind.KindModule) ) { String  tom_mname= (( tom.gom.adt.gom.types.Production )tom_prod).getName() ;

                //Graph-rewrite rules hooks are only allowed for sorts
                if( (( tom.gom.adt.gom.types.Production )tom_prod).getHookType() .getkind().equals("graphrules")) {
                  getLogger().log(Level.SEVERE,
                      "Grapphrules hooks are authorised only on sorts");

                }
                if(tom_mname.equals(tom_moduleName)) {
                  ModuleDecl mdecl = getModuleDecl(tom_mname,moduleList);
                  HookDeclList newDeclList =
                    makeHookDeclList((( tom.gom.adt.gom.types.Production )tom_prod), tom.gom.adt.gom.types.decl.CutModule.make(mdecl) );
                  hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
                } else {
                  getLogger().log(Level.SEVERE,
                      "Hooks on module are authorised only on the current module");
                }
              }}}}{if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.production.Hook) ) {if ( ( (( tom.gom.adt.gom.types.Production )tom_prod).getNameType()  instanceof tom.gom.adt.gom.types.idkind.KindSort) ) {

                SortDecl sdecl = getSortDecl( (( tom.gom.adt.gom.types.Production )tom_prod).getName() ,tom_moduleName,moduleList);
                HookDeclList newDeclList = makeHookDeclList((( tom.gom.adt.gom.types.Production )tom_prod), tom.gom.adt.gom.types.decl.CutSort.make(sdecl) );
                hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
              }}}}{if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.production.Hook) ) {if ( ( (( tom.gom.adt.gom.types.Production )tom_prod).getNameType()  instanceof tom.gom.adt.gom.types.idkind.KindOperator) ) {

                //Graph-rewrite rules hooks are only allowed for sorts
                if( (( tom.gom.adt.gom.types.Production )tom_prod).getHookType() .getkind().equals("graphrules")) {
                  getLogger().log(Level.SEVERE,
                      "Grapphrules hooks are authorised only on sorts");

                }
                OperatorDecl odecl = getOperatorDecl( (( tom.gom.adt.gom.types.Production )tom_prod).getName() ,tom_moduleName,moduleList);
                if(odecl!=null) {
                  HookDeclList newDeclList = makeHookDeclList((( tom.gom.adt.gom.types.Production )tom_prod), tom.gom.adt.gom.types.decl.CutOperator.make(odecl) );
                  hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
                }
              }}}}{if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.IdKind  tomMatch521NameNumber_freshVar_18= (( tom.gom.adt.gom.types.Production )tom_prod).getNameType() ;if ( (tomMatch521NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.idkind.KindFutureOperator) ) {

                OperatorDecl odecl = getOperatorDecl( (( tom.gom.adt.gom.types.Production )tom_prod).getName() ,tom_moduleName,moduleList);
                if(odecl!=null) {
                  HookDeclList newDeclList = 
                    makeHookDeclList((( tom.gom.adt.gom.types.Production )tom_prod), tom.gom.adt.gom.types.decl.CutFutureOperator.make(odecl,  tomMatch521NameNumber_freshVar_18.getConsOrNil() ) );
                  hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
                }
              }}}}}

          }if ( tomMatch520NameNumber_end_4.isEmptyConcProduction() ) {tomMatch520NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);} else {tomMatch520NameNumber_end_4= tomMatch520NameNumber_end_4.getTailConcProduction() ;}}} while(!( (tomMatch520NameNumber_end_4==(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) ));}}}}

        ArrayList examinedOps = new ArrayList();
        {{if ( (tom_prodList instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch522NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);do {{if (!( tomMatch522NameNumber_end_4.isEmptyConcProduction() )) {

            hookList = addDefaultTheoryHooks( tomMatch522NameNumber_end_4.getHeadConcProduction() ,hookList,examinedOps,tom_moduleName);
          }if ( tomMatch522NameNumber_end_4.isEmptyConcProduction() ) {tomMatch522NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);} else {tomMatch522NameNumber_end_4= tomMatch522NameNumber_end_4.getTailConcProduction() ;}}} while(!( (tomMatch522NameNumber_end_4==(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) ));}}}}{{if ( (tom_prodList instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch523NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);do {{if (!( tomMatch523NameNumber_end_4.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch523NameNumber_freshVar_8= tomMatch523NameNumber_end_4.getHeadConcProduction() ;if ( (tomMatch523NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.ProductionList  tomMatch523NameNumber_freshVar_7= tomMatch523NameNumber_freshVar_8.getProductionList() ;if ( ((tomMatch523NameNumber_freshVar_7 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch523NameNumber_freshVar_7 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch523NameNumber_end_12=tomMatch523NameNumber_freshVar_7;do {{if (!( tomMatch523NameNumber_end_12.isEmptyConcProduction() )) {



            hookList = addDefaultTheoryHooks( tomMatch523NameNumber_end_12.getHeadConcProduction() ,hookList,examinedOps,tom_moduleName);
          }if ( tomMatch523NameNumber_end_12.isEmptyConcProduction() ) {tomMatch523NameNumber_end_12=tomMatch523NameNumber_freshVar_7;} else {tomMatch523NameNumber_end_12= tomMatch523NameNumber_end_12.getTailConcProduction() ;}}} while(!( (tomMatch523NameNumber_end_12==tomMatch523NameNumber_freshVar_7) ));}}}if ( tomMatch523NameNumber_end_4.isEmptyConcProduction() ) {tomMatch523NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);} else {tomMatch523NameNumber_end_4= tomMatch523NameNumber_end_4.getTailConcProduction() ;}}} while(!( (tomMatch523NameNumber_end_4==(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) ));}}}}

      }}if ( tomMatch519NameNumber_end_23.isEmptyConcGrammar() ) {tomMatch519NameNumber_end_23=tomMatch519NameNumber_freshVar_18;} else {tomMatch519NameNumber_end_23= tomMatch519NameNumber_end_23.getTailConcGrammar() ;}}} while(!( (tomMatch519NameNumber_end_23==tomMatch519NameNumber_freshVar_18) ));}}}if ( tomMatch519NameNumber_end_15.isEmptyConcSection() ) {tomMatch519NameNumber_end_15=tomMatch519NameNumber_freshVar_8;} else {tomMatch519NameNumber_end_15= tomMatch519NameNumber_end_15.getTailConcSection() ;}}} while(!( (tomMatch519NameNumber_end_15==tomMatch519NameNumber_freshVar_8) ));}}}}if ( tomMatch519NameNumber_end_4.isEmptyConcGomModule() ) {tomMatch519NameNumber_end_4=(( tom.gom.adt.gom.types.GomModuleList )gomModuleList);} else {tomMatch519NameNumber_end_4= tomMatch519NameNumber_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch519NameNumber_end_4==(( tom.gom.adt.gom.types.GomModuleList )gomModuleList)) ));}}}}

    return hookList;
  }

  private HookDeclList addDefaultTheoryHooks(Production prod,
                                             HookDeclList hookList,
                                             ArrayList examinedOps,
                                             String moduleName) {
    {{if ( (prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )prod) instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.FieldList  tomMatch524NameNumber_freshVar_3= (( tom.gom.adt.gom.types.Production )prod).getDomainList() ; String  tom_opName= (( tom.gom.adt.gom.types.Production )prod).getName() ;if ( ((tomMatch524NameNumber_freshVar_3 instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || (tomMatch524NameNumber_freshVar_3 instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( tomMatch524NameNumber_freshVar_3.isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch524NameNumber_freshVar_11= tomMatch524NameNumber_freshVar_3.getHeadConcField() ;if ( (tomMatch524NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.field.StarredField) ) {if (  tomMatch524NameNumber_freshVar_3.getTailConcField() .isEmptyConcField() ) {if ( ( (( tom.gom.adt.gom.types.Production )prod).getCodomain() == tomMatch524NameNumber_freshVar_11.getFieldType() ) ) {if ( (hookList instanceof tom.gom.adt.gom.types.HookDeclList) ) {boolean tomMatch524NameNumber_freshVar_26= false ;if ( (((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch524NameNumber_end_15=(( tom.gom.adt.gom.types.HookDeclList )hookList);do {{if (!( tomMatch524NameNumber_end_15.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tomMatch524NameNumber_freshVar_20= tomMatch524NameNumber_end_15.getHeadConcHookDecl() ;if ( (tomMatch524NameNumber_freshVar_20 instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) { tom.gom.adt.gom.types.Decl  tomMatch524NameNumber_freshVar_19= tomMatch524NameNumber_freshVar_20.getPointcut() ;if ( (tomMatch524NameNumber_freshVar_19 instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch524NameNumber_freshVar_21= tomMatch524NameNumber_freshVar_19.getODecl() ;if ( (tomMatch524NameNumber_freshVar_21 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {if (  tomMatch524NameNumber_freshVar_21.getName() .equals(tom_opName) ) {tomMatch524NameNumber_freshVar_26= true ;}}}}}if ( tomMatch524NameNumber_end_15.isEmptyConcHookDecl() ) {tomMatch524NameNumber_end_15=(( tom.gom.adt.gom.types.HookDeclList )hookList);} else {tomMatch524NameNumber_end_15= tomMatch524NameNumber_end_15.getTailConcHookDecl() ;}}} while(!( (tomMatch524NameNumber_end_15==(( tom.gom.adt.gom.types.HookDeclList )hookList)) ));}if ( tomMatch524NameNumber_freshVar_26== false  ) {




        /* generate a FL hook for list-operators without other hook */
        String emptyCode = "{}";
        Production hook =  tom.gom.adt.gom.types.production.Hook.make( tom.gom.adt.gom.types.idkind.KindOperator.make() , tom_opName,  tom.gom.adt.gom.types.hookkind.HookKind.make("FL") ,  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() , emptyCode,  tom.gom.adt.gom.types.option.EmptyOptionList.make() ) ;
        OperatorDecl odecl = getOperatorDecl(tom_opName,moduleName,moduleList);
        if(odecl!=null) {
          HookDeclList newDeclList = makeHookDeclList(hook, tom.gom.adt.gom.types.decl.CutOperator.make(odecl) );
          hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
        }
      }}}}}}}}}}{if ( (prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )prod) instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.FieldList  tomMatch524NameNumber_freshVar_30= (( tom.gom.adt.gom.types.Production )prod).getDomainList() ; String  tom_opName= (( tom.gom.adt.gom.types.Production )prod).getName() ;if ( ((tomMatch524NameNumber_freshVar_30 instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || (tomMatch524NameNumber_freshVar_30 instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( tomMatch524NameNumber_freshVar_30.isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch524NameNumber_freshVar_44= tomMatch524NameNumber_freshVar_30.getHeadConcField() ;if ( (tomMatch524NameNumber_freshVar_44 instanceof tom.gom.adt.gom.types.field.StarredField) ) {if (  tomMatch524NameNumber_freshVar_30.getTailConcField() .isEmptyConcField() ) {if ( ( (( tom.gom.adt.gom.types.Production )prod).getCodomain() == tomMatch524NameNumber_freshVar_44.getFieldType() ) ) {if ( (hookList instanceof tom.gom.adt.gom.types.HookDeclList) ) {if ( (((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch524NameNumber_end_39=(( tom.gom.adt.gom.types.HookDeclList )hookList);do {{if (!( tomMatch524NameNumber_end_39.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tomMatch524NameNumber_freshVar_46= tomMatch524NameNumber_end_39.getHeadConcHookDecl() ;if ( (tomMatch524NameNumber_freshVar_46 instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) { tom.gom.adt.gom.types.HookKind  tomMatch524NameNumber_freshVar_45= tomMatch524NameNumber_freshVar_46.getHookType() ;if ( (tomMatch524NameNumber_freshVar_45 instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch524NameNumber_freshVar_47= tomMatch524NameNumber_freshVar_45.getkind() ;boolean tomMatch524NameNumber_freshVar_51= false ;if ( "make_insert".equals(tomMatch524NameNumber_freshVar_47) ) {tomMatch524NameNumber_freshVar_51= true ;} else {if ( "make_empty".equals(tomMatch524NameNumber_freshVar_47) ) {tomMatch524NameNumber_freshVar_51= true ;} else {if ( "rules".equals(tomMatch524NameNumber_freshVar_47) ) {tomMatch524NameNumber_freshVar_51= true ;}}}if ( tomMatch524NameNumber_freshVar_51== true  ) {




        if(! examinedOps.contains(tom_opName)) {
          examinedOps.add(tom_opName);
          {{if ( (hookList instanceof tom.gom.adt.gom.types.HookDeclList) ) {boolean tomMatch525NameNumber_freshVar_17= false ;if ( (((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch525NameNumber_end_4=(( tom.gom.adt.gom.types.HookDeclList )hookList);do {{if (!( tomMatch525NameNumber_end_4.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tomMatch525NameNumber_freshVar_9= tomMatch525NameNumber_end_4.getHeadConcHookDecl() ;if ( (tomMatch525NameNumber_freshVar_9 instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) { tom.gom.adt.gom.types.Decl  tomMatch525NameNumber_freshVar_7= tomMatch525NameNumber_freshVar_9.getPointcut() ; tom.gom.adt.gom.types.HookKind  tomMatch525NameNumber_freshVar_8= tomMatch525NameNumber_freshVar_9.getHookType() ;if ( (tomMatch525NameNumber_freshVar_7 instanceof tom.gom.adt.gom.types.decl.CutOperator) ) {if ( ( tomMatch525NameNumber_freshVar_7.getODecl()  instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {if ( (tomMatch525NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch525NameNumber_freshVar_14= tomMatch525NameNumber_freshVar_8.getkind() ;boolean tomMatch525NameNumber_freshVar_18= false ;if ( "Free".equals(tomMatch525NameNumber_freshVar_14) ) {tomMatch525NameNumber_freshVar_18= true ;} else {if ( "FL".equals(tomMatch525NameNumber_freshVar_14) ) {tomMatch525NameNumber_freshVar_18= true ;} else {if ( "AU".equals(tomMatch525NameNumber_freshVar_14) ) {tomMatch525NameNumber_freshVar_18= true ;} else {if ( "ACU".equals(tomMatch525NameNumber_freshVar_14) ) {tomMatch525NameNumber_freshVar_18= true ;}}}}if ( tomMatch525NameNumber_freshVar_18== true  ) {tomMatch525NameNumber_freshVar_17= true ;}}}}}}if ( tomMatch525NameNumber_end_4.isEmptyConcHookDecl() ) {tomMatch525NameNumber_end_4=(( tom.gom.adt.gom.types.HookDeclList )hookList);} else {tomMatch525NameNumber_end_4= tomMatch525NameNumber_end_4.getTailConcHookDecl() ;}}} while(!( (tomMatch525NameNumber_end_4==(( tom.gom.adt.gom.types.HookDeclList )hookList)) ));}if ( tomMatch525NameNumber_freshVar_17== false  ) {


              /* generate an error to make users specify the theory */
              getLogger().log(Level.SEVERE,
                "As you use make_insert, make_empty or rules, specify the associated theory for the variadic operator "+tom_opName);
            }}}}

        }
      }}}}if ( tomMatch524NameNumber_end_39.isEmptyConcHookDecl() ) {tomMatch524NameNumber_end_39=(( tom.gom.adt.gom.types.HookDeclList )hookList);} else {tomMatch524NameNumber_end_39= tomMatch524NameNumber_end_39.getTailConcHookDecl() ;}}} while(!( (tomMatch524NameNumber_end_39==(( tom.gom.adt.gom.types.HookDeclList )hookList)) ));}}}}}}}}}}}

    return hookList;
  }

  private HookDeclList makeHookDeclList(Production hook, Decl mdecl) {
    {{if ( (hook instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )hook) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.HookKind  tom_hkind= (( tom.gom.adt.gom.types.Production )hook).getHookType() ; String  tom_hName= (( tom.gom.adt.gom.types.Production )hook).getName() ; tom.gom.adt.gom.types.ArgList  tom_hookArgs= (( tom.gom.adt.gom.types.Production )hook).getArgs() ; String  tom_scode= (( tom.gom.adt.gom.types.Production )hook).getStringCode() ;




          HookDeclList newHookList =  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
          {{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "block".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ,  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "javablock".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ,  false ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "interface".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.InterfaceHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "import".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.ImportHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "mapping".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MappingHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch527NameNumber_freshVar_21= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;boolean tomMatch527NameNumber_freshVar_24= false ;if ( "make".equals(tomMatch527NameNumber_freshVar_21) ) {tomMatch527NameNumber_freshVar_24= true ;} else {if ( "make_insert".equals(tomMatch527NameNumber_freshVar_21) ) {tomMatch527NameNumber_freshVar_24= true ;} else {if ( "make_empty".equals(tomMatch527NameNumber_freshVar_21) ) {tomMatch527NameNumber_freshVar_24= true ;}}}if ( tomMatch527NameNumber_freshVar_24== true  ) {

              SlotList typedArgs = typeArguments(tom_hookArgs,tom_hkind,mdecl);
              if (typedArgs == null) {
                getLogger().log(Level.SEVERE,
                    GomMessage.discardedHook.getMessage(),
                    new Object[]{ (tom_hName) });
                return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
              }
              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl, typedArgs,  tom.gom.adt.code.types.code.Code.make(tom_scode) , (( tom.gom.adt.gom.types.HookKind )tom_hkind),  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "Free".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              /* Even there is no code associated, we generate a MakeHook to prevent FL hooks to be automatically generated */
              return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ,  tom.gom.adt.code.types.code.Code.make("") ,  tom.gom.adt.gom.types.hookkind.HookKind.make("Free") ,  false ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "FL".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              /* FL: flattened list */
              return makeFLHookList(tom_hName,mdecl,tom_scode);
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "AU".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              return makeAUHookList(tom_hName,mdecl,tom_scode);
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "ACU".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              return makeACUHookList(tom_hName,mdecl,tom_scode);
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "rules".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              return makeRulesHookList(tom_hName,mdecl,tom_scode);
            }}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "graphrules".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

              //TODO: verify if the option termgraph is on
              if(tom_hookArgs.length()!=2) {
                throw new GomRuntimeException(
                    "GomTypeExpander:graphrules hooks need two parameters: the name of the generated strategy and its default behaviour");
              }
              return makeGraphRulesHookList(tom_hName,tom_hookArgs,mdecl,tom_scode);
            }}}}}

          if (newHookList ==  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) {
            getLogger().log(Level.SEVERE, GomMessage.unknownHookKind.getMessage(), new Object[]{ (tom_hkind) });
          }
          return newHookList;
        }}}}

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
    {{if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch528NameNumber_end_4=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch528NameNumber_end_4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch528NameNumber_freshVar_8= tomMatch528NameNumber_end_4.getHeadConcModule() ;if ( (tomMatch528NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch528NameNumber_freshVar_7= tomMatch528NameNumber_freshVar_8.getMDecl() ;if ( (tomMatch528NameNumber_freshVar_7 instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch528NameNumber_freshVar_9= tomMatch528NameNumber_freshVar_7.getModuleName() ;if ( (tomMatch528NameNumber_freshVar_9 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {




        if ( tomMatch528NameNumber_freshVar_9.getName() .equals(mname)) {
          return tomMatch528NameNumber_freshVar_7;
        }
      }}}}if ( tomMatch528NameNumber_end_4.isEmptyConcModule() ) {tomMatch528NameNumber_end_4=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch528NameNumber_end_4= tomMatch528NameNumber_end_4.getTailConcModule() ;}}} while(!( (tomMatch528NameNumber_end_4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}

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
    {{if ( true ) {if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch529NameNumber_end_5=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch529NameNumber_end_5.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch529NameNumber_freshVar_10= tomMatch529NameNumber_end_5.getHeadConcModule() ;if ( (tomMatch529NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch529NameNumber_freshVar_8= tomMatch529NameNumber_freshVar_10.getMDecl() ; tom.gom.adt.gom.types.SortList  tomMatch529NameNumber_freshVar_9= tomMatch529NameNumber_freshVar_10.getSorts() ;if ( (tomMatch529NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch529NameNumber_freshVar_11= tomMatch529NameNumber_freshVar_8.getModuleName() ;if ( (tomMatch529NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {if (  tomMatch529NameNumber_freshVar_11.getName() .equals((( String )modName)) ) {if ( ((tomMatch529NameNumber_freshVar_9 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch529NameNumber_freshVar_9 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch529NameNumber_end_18=tomMatch529NameNumber_freshVar_9;do {{if (!( tomMatch529NameNumber_end_18.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch529NameNumber_freshVar_23= tomMatch529NameNumber_end_18.getHeadConcSort() ;if ( (tomMatch529NameNumber_freshVar_23 instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch529NameNumber_freshVar_22= tomMatch529NameNumber_freshVar_23.getDecl() ;if ( (tomMatch529NameNumber_freshVar_22 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {






          if ( tomMatch529NameNumber_freshVar_22.getName() .equals(sname)) {
            return tomMatch529NameNumber_freshVar_22;
          }
        }}}if ( tomMatch529NameNumber_end_18.isEmptyConcSort() ) {tomMatch529NameNumber_end_18=tomMatch529NameNumber_freshVar_9;} else {tomMatch529NameNumber_end_18= tomMatch529NameNumber_end_18.getTailConcSort() ;}}} while(!( (tomMatch529NameNumber_end_18==tomMatch529NameNumber_freshVar_9) ));}}}}}}if ( tomMatch529NameNumber_end_5.isEmptyConcModule() ) {tomMatch529NameNumber_end_5=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch529NameNumber_end_5= tomMatch529NameNumber_end_5.getTailConcModule() ;}}} while(!( (tomMatch529NameNumber_end_5==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}}

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
    {{if ( true ) {if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch530NameNumber_end_5=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch530NameNumber_end_5.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch530NameNumber_freshVar_10= tomMatch530NameNumber_end_5.getHeadConcModule() ;if ( (tomMatch530NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch530NameNumber_freshVar_8= tomMatch530NameNumber_freshVar_10.getMDecl() ; tom.gom.adt.gom.types.SortList  tomMatch530NameNumber_freshVar_9= tomMatch530NameNumber_freshVar_10.getSorts() ;if ( (tomMatch530NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch530NameNumber_freshVar_11= tomMatch530NameNumber_freshVar_8.getModuleName() ;if ( (tomMatch530NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {if (  tomMatch530NameNumber_freshVar_11.getName() .equals((( String )modName)) ) {if ( ((tomMatch530NameNumber_freshVar_9 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch530NameNumber_freshVar_9 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch530NameNumber_end_18=tomMatch530NameNumber_freshVar_9;do {{if (!( tomMatch530NameNumber_end_18.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch530NameNumber_freshVar_23= tomMatch530NameNumber_end_18.getHeadConcSort() ;if ( (tomMatch530NameNumber_freshVar_23 instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch530NameNumber_freshVar_22= tomMatch530NameNumber_freshVar_23.getOperatorDecls() ;if ( ((tomMatch530NameNumber_freshVar_22 instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || (tomMatch530NameNumber_freshVar_22 instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch530NameNumber_end_27=tomMatch530NameNumber_freshVar_22;do {{if (!( tomMatch530NameNumber_end_27.isEmptyConcOperator() )) { tom.gom.adt.gom.types.OperatorDecl  tomMatch530NameNumber_freshVar_31= tomMatch530NameNumber_end_27.getHeadConcOperator() ;if ( (tomMatch530NameNumber_freshVar_31 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {








        if ( tomMatch530NameNumber_freshVar_31.getName() .equals(oname)) {
          return  tomMatch530NameNumber_end_27.getHeadConcOperator() ;
        }
      }}if ( tomMatch530NameNumber_end_27.isEmptyConcOperator() ) {tomMatch530NameNumber_end_27=tomMatch530NameNumber_freshVar_22;} else {tomMatch530NameNumber_end_27= tomMatch530NameNumber_end_27.getTailConcOperator() ;}}} while(!( (tomMatch530NameNumber_end_27==tomMatch530NameNumber_freshVar_22) ));}}}if ( tomMatch530NameNumber_end_18.isEmptyConcSort() ) {tomMatch530NameNumber_end_18=tomMatch530NameNumber_freshVar_9;} else {tomMatch530NameNumber_end_18= tomMatch530NameNumber_end_18.getTailConcSort() ;}}} while(!( (tomMatch530NameNumber_end_18==tomMatch530NameNumber_freshVar_9) ));}}}}}}if ( tomMatch530NameNumber_end_5.isEmptyConcModule() ) {tomMatch530NameNumber_end_5=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch530NameNumber_end_5= tomMatch530NameNumber_end_5.getTailConcModule() ;}}} while(!( (tomMatch530NameNumber_end_5==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}}

    getLogger().log(Level.SEVERE,
        GomMessage.orphanedHook.getMessage(),
        new Object[]{oname});
    return null;
  }

  private SlotList typeArguments(
      ArgList args,
      HookKind kind,
      Decl decl) {
    {{if ( (kind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )kind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {if ( "make".equals( (( tom.gom.adt.gom.types.HookKind )kind).getkind() ) ) {

        /*
         * The TypedProduction has to be Slots
         * A KindMakeHook is attached to an operator
         */
        {{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch532NameNumber_freshVar_1= (( tom.gom.adt.gom.types.Decl )decl).getODecl() ;if ( (tomMatch532NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch532NameNumber_freshVar_3= tomMatch532NameNumber_freshVar_1.getProd() ;if ( (tomMatch532NameNumber_freshVar_3 instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tom_slotList= tomMatch532NameNumber_freshVar_3.getSlots() ;

            /* tests the arguments number */
            if (args.length() != tom_slotList.length()) {
              SlotList slist = tom_slotList;
              getLogger().log(Level.SEVERE,
                  GomMessage.mismatchedMakeArguments.getMessage(),
                  new Object[]{args,slist });
              return null;
            }
            /* Then check the types */
            return recArgSlots(args,tom_slotList);
          }}}}}{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {

            getLogger().log(Level.SEVERE,
                GomMessage.unsupportedHookAlgebraic.getMessage(),
                new Object[]{ kind });
            return null;
          }}}

      }}}}{if ( (kind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )kind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch531NameNumber_freshVar_5= (( tom.gom.adt.gom.types.HookKind )kind).getkind() ;if ( "make_insert".equals(tomMatch531NameNumber_freshVar_5) ) {

        /*
         * The TypedProduction has to be Variadic
         * Then we get the codomain from the operatordecl
         */
        {{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch533NameNumber_freshVar_1= (( tom.gom.adt.gom.types.Decl )decl).getODecl() ;if ( (tomMatch533NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch533NameNumber_freshVar_4= tomMatch533NameNumber_freshVar_1.getProd() ;if ( (tomMatch533NameNumber_freshVar_4 instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {


              // for a make_insert hook, there are two arguments: head, tail
              {{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if (!( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch534NameNumber_freshVar_5= (( tom.gom.adt.gom.types.ArgList )args).getHeadConcArg() ;if ( (tomMatch534NameNumber_freshVar_5 instanceof tom.gom.adt.gom.types.arg.Arg) ) { tom.gom.adt.gom.types.ArgList  tomMatch534NameNumber_freshVar_2= (( tom.gom.adt.gom.types.ArgList )args).getTailConcArg() ;if (!( tomMatch534NameNumber_freshVar_2.isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch534NameNumber_freshVar_7= tomMatch534NameNumber_freshVar_2.getHeadConcArg() ;if ( (tomMatch534NameNumber_freshVar_7 instanceof tom.gom.adt.gom.types.arg.Arg) ) {if (  tomMatch534NameNumber_freshVar_2.getTailConcArg() .isEmptyConcArg() ) {

                  return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch534NameNumber_freshVar_5.getName() ,  tomMatch533NameNumber_freshVar_4.getSort() ) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch534NameNumber_freshVar_7.getName() ,  tomMatch533NameNumber_freshVar_1.getSort() ) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ;
                }}}}}}}}{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {

                  getLogger().log(Level.SEVERE,
                      GomMessage.badHookArguments.getMessage(),
                      new Object[]{ (tomMatch531NameNumber_freshVar_5), new Integer(args.length())});
                  return null;
                }}}

            }}}}}{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {

            getLogger().log(Level.SEVERE,
                GomMessage.unsupportedHookVariadic.getMessage(),
                new Object[]{ kind });
            return null;
          }}}

      }}}}{if ( (kind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )kind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch531NameNumber_freshVar_9= (( tom.gom.adt.gom.types.HookKind )kind).getkind() ;if ( "make_empty".equals(tomMatch531NameNumber_freshVar_9) ) {

        /*
         * The TypedProduction has to be Variadic
         * Then we get the codomain from the operatordecl
         */
        {{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch535NameNumber_freshVar_1= (( tom.gom.adt.gom.types.Decl )decl).getODecl() ;if ( (tomMatch535NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {if ( ( tomMatch535NameNumber_freshVar_1.getProd()  instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {


              // for a make_empty hook, there is no argument
              {{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if ( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() ) {
 return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ; }}}}{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {

                  getLogger().log(Level.SEVERE,
                      GomMessage.badHookArguments.getMessage(),
                      new Object[]{ (tomMatch531NameNumber_freshVar_9), new Integer(args.length())});
                  return null;
                }}}

            }}}}}{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {

            getLogger().log(Level.SEVERE,
                GomMessage.unsupportedHookVariadic.getMessage(),
                new Object[]{ kind });
            return null;
          }}}

      }}}}}

    throw new GomRuntimeException("Hook kind \""+kind+"\" not supported");
  }

  private SlotList recArgSlots(ArgList args, SlotList slots) {
    {{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if ( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() ) {if ( (slots instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if ( (( tom.gom.adt.gom.types.SlotList )slots).isEmptyConcSlot() ) {

        return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
      }}}}}}}{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if (!( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch537NameNumber_freshVar_13= (( tom.gom.adt.gom.types.ArgList )args).getHeadConcArg() ;if ( (tomMatch537NameNumber_freshVar_13 instanceof tom.gom.adt.gom.types.arg.Arg) ) {if ( (slots instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if (!( (( tom.gom.adt.gom.types.SlotList )slots).isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch537NameNumber_freshVar_15= (( tom.gom.adt.gom.types.SlotList )slots).getHeadConcSlot() ;if ( (tomMatch537NameNumber_freshVar_15 instanceof tom.gom.adt.gom.types.slot.Slot) ) {

        SlotList tail = recArgSlots( (( tom.gom.adt.gom.types.ArgList )args).getTailConcArg() , (( tom.gom.adt.gom.types.SlotList )slots).getTailConcSlot() );
        return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch537NameNumber_freshVar_13.getName() ,  tomMatch537NameNumber_freshVar_15.getSort() ) ,tom_append_list_ConcSlot(tail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;
      }}}}}}}}}}

    throw new GomRuntimeException("GomTypeExpander:recArgSlots failed "+args+" "+slots);
  }

  private String trimBracket(String stringCode) {
    int start = stringCode.indexOf('{')+1;
    int end = stringCode.lastIndexOf('}');
    return stringCode.substring(start,end).trim();
  }

  private SortDecl getSortAndCheck(Decl mdecl) {
    {{if ( (mdecl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )mdecl) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch538NameNumber_freshVar_1= (( tom.gom.adt.gom.types.Decl )mdecl).getODecl() ;if ( (tomMatch538NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch538NameNumber_freshVar_4= tomMatch538NameNumber_freshVar_1.getProd() ;if ( (tomMatch538NameNumber_freshVar_4 instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) { tom.gom.adt.gom.types.SortDecl  tom_sdecl= tomMatch538NameNumber_freshVar_4.getSort() ;

        if ( tomMatch538NameNumber_freshVar_1.getSort() == tom_sdecl) {
          return tom_sdecl;
        } else {
          getLogger().log(Level.SEVERE,
              "Different domain and codomain");
        }
      }}}}}{if ( (mdecl instanceof tom.gom.adt.gom.types.Decl) ) {

        getLogger().log(Level.SEVERE,
            "FL/AU/ACU hook can only be used on a variadic operator");
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
    {{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if (!( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch539NameNumber_freshVar_5= (( tom.gom.adt.gom.types.ArgList )args).getHeadConcArg() ;if ( (tomMatch539NameNumber_freshVar_5 instanceof tom.gom.adt.gom.types.arg.Arg) ) { String  tom_stratname= tomMatch539NameNumber_freshVar_5.getName() ; tom.gom.adt.gom.types.ArgList  tomMatch539NameNumber_freshVar_2= (( tom.gom.adt.gom.types.ArgList )args).getTailConcArg() ;if (!( tomMatch539NameNumber_freshVar_2.isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch539NameNumber_freshVar_7= tomMatch539NameNumber_freshVar_2.getHeadConcArg() ;if ( (tomMatch539NameNumber_freshVar_7 instanceof tom.gom.adt.gom.types.arg.Arg) ) { String  tom_defaultstrat= tomMatch539NameNumber_freshVar_7.getName() ;if (  tomMatch539NameNumber_freshVar_2.getTailConcArg() .isEmptyConcArg() ) {

        if (!tom_defaultstrat.equals("Fail") && !tom_defaultstrat.equals("Identity")) {
          getLogger().log(Level.SEVERE,
              "In graphrules hooks, the default strategies authorized are only Fail and Identity");
        }
        GraphRuleExpander rexpander = new GraphRuleExpander(moduleList,getGomEnvironment());
        if (sortsWithGraphrules.contains(sdecl)) {
          return rexpander.expandGraphRules(sortname,tom_stratname,tom_defaultstrat,trimBracket(scode),sdecl);
        } else {
          sortsWithGraphrules.add(sdecl);
          return rexpander.expandFirstGraphRules(sortname,tom_stratname,tom_defaultstrat,trimBracket(scode),sdecl);
        }
      }}}}}}}}}

    return null;
  }

  /*
   * generate hooks for associative-commutative with neutral element
   */
  private HookDeclList makeACUHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain)
      return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;

    /* start with AU normalization */
    HookDeclList acHooks = makeAUHookList(opName, mdecl, scode);
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
    acHooks =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  if (0 < ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Compare.make( tom.gom.adt.code.types.code.Code.make("head") ,  tom.gom.adt.code.types.code.Code.make("tail.getHead" + opName+ "()") ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(domain) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" tmpHd = head;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    head = tail.getHead" + opName+ "();\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    tail = `"+opName+"(tmpHd,tail.getTail" + opName+ "());\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("} else {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  if (0 < ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Compare.make( tom.gom.adt.code.types.code.Code.make("head") ,  tom.gom.adt.code.types.code.Code.make("tail") ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(domain) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" tmpHd = head;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    head = tail;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    tail = tmpHd;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("}\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("ACU") ,  true ) ,tom_append_list_ConcHookDecl(acHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) 
























;
    return acHooks;
  }

  /*
   * generate hooks for associative with neutral element
   */
  private HookDeclList makeAUHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain)
      return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;

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
      getLogger().log(Level.SEVERE,
          "FL hook does not allow the definition of a neutral element");
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
