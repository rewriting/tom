/*
 *
 * GOM
 *
 * Copyright (c) 2006-2016, Universite de Lorraine, Inria
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

         private static   tom.gom.adt.code.types.Code  tom_append_list_CodeList( tom.gom.adt.code.types.Code  l1,  tom.gom.adt.code.types.Code  l2) {     if( l1.isEmptyCodeList() ) {       return l2;     } else if( l2.isEmptyCodeList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {       if(  l1.getTailCodeList() .isEmptyCodeList() ) {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,l2) ;       } else {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,tom_append_list_CodeList( l1.getTailCodeList() ,l2)) ;       }     } else {       return  tom.gom.adt.code.types.code.ConsCodeList.make(l1,l2) ;     }   }   private static   tom.gom.adt.code.types.Code  tom_get_slice_CodeList( tom.gom.adt.code.types.Code  begin,  tom.gom.adt.code.types.Code  end, tom.gom.adt.code.types.Code  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCodeList()  ||  (end== tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getHeadCodeList() :begin),( tom.gom.adt.code.types.Code )tom_get_slice_CodeList((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),end,tail)) ;   }      private static   tom.gom.adt.gom.types.ArgList  tom_append_list_ConcArg( tom.gom.adt.gom.types.ArgList l1,  tom.gom.adt.gom.types.ArgList  l2) {     if( l1.isEmptyConcArg() ) {       return l2;     } else if( l2.isEmptyConcArg() ) {       return l1;     } else if(  l1.getTailConcArg() .isEmptyConcArg() ) {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,l2) ;     } else {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,tom_append_list_ConcArg( l1.getTailConcArg() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ArgList  tom_get_slice_ConcArg( tom.gom.adt.gom.types.ArgList  begin,  tom.gom.adt.gom.types.ArgList  end, tom.gom.adt.gom.types.ArgList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcArg()  ||  (end== tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( begin.getHeadConcArg() ,( tom.gom.adt.gom.types.ArgList )tom_get_slice_ConcArg( begin.getTailConcArg() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.AlternativeList  tom_append_list_ConcAlternative( tom.gom.adt.gom.types.AlternativeList l1,  tom.gom.adt.gom.types.AlternativeList  l2) {     if( l1.isEmptyConcAlternative() ) {       return l2;     } else if( l2.isEmptyConcAlternative() ) {       return l1;     } else if(  l1.getTailConcAlternative() .isEmptyConcAlternative() ) {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,l2) ;     } else {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,tom_append_list_ConcAlternative( l1.getTailConcAlternative() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AlternativeList  tom_get_slice_ConcAlternative( tom.gom.adt.gom.types.AlternativeList  begin,  tom.gom.adt.gom.types.AlternativeList  end, tom.gom.adt.gom.types.AlternativeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAlternative()  ||  (end== tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( begin.getHeadConcAlternative() ,( tom.gom.adt.gom.types.AlternativeList )tom_get_slice_ConcAlternative( begin.getTailConcAlternative() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {     if( l1.isEmptyConcModule() ) {       return l2;     } else if( l2.isEmptyConcModule() ) {       return l1;     } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.HookDeclList  tom_append_list_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList l1,  tom.gom.adt.gom.types.HookDeclList  l2) {     if( l1.isEmptyConcHookDecl() ) {       return l2;     } else if( l2.isEmptyConcHookDecl() ) {       return l1;     } else if(  l1.getTailConcHookDecl() .isEmptyConcHookDecl() ) {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,tom_append_list_ConcHookDecl( l1.getTailConcHookDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.HookDeclList  tom_get_slice_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList  begin,  tom.gom.adt.gom.types.HookDeclList  end, tom.gom.adt.gom.types.HookDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHookDecl()  ||  (end== tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( begin.getHeadConcHookDecl() ,( tom.gom.adt.gom.types.HookDeclList )tom_get_slice_ConcHookDecl( begin.getTailConcHookDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.Option  tom_append_list_OptionList( tom.gom.adt.gom.types.Option  l1,  tom.gom.adt.gom.types.Option  l2) {     if( l1.isEmptyOptionList() ) {       return l2;     } else if( l2.isEmptyOptionList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) ) {       if(  l1.getTailOptionList() .isEmptyOptionList() ) {         return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,l2) ;       } else {         return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,tom_append_list_OptionList( l1.getTailOptionList() ,l2)) ;       }     } else {       return  tom.gom.adt.gom.types.option.ConsOptionList.make(l1,l2) ;     }   }   private static   tom.gom.adt.gom.types.Option  tom_get_slice_OptionList( tom.gom.adt.gom.types.Option  begin,  tom.gom.adt.gom.types.Option  end, tom.gom.adt.gom.types.Option  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOptionList()  ||  (end== tom.gom.adt.gom.types.option.EmptyOptionList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.option.ConsOptionList.make((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getHeadOptionList() :begin),( tom.gom.adt.gom.types.Option )tom_get_slice_OptionList((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getTailOptionList() : tom.gom.adt.gom.types.option.EmptyOptionList.make() ),end,tail)) ;   }      private static   tom.gom.adt.gom.types.OperatorDeclList  tom_append_list_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList l1,  tom.gom.adt.gom.types.OperatorDeclList  l2) {     if( l1.isEmptyConcOperator() ) {       return l2;     } else if( l2.isEmptyConcOperator() ) {       return l1;     } else if(  l1.getTailConcOperator() .isEmptyConcOperator() ) {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,l2) ;     } else {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,tom_append_list_ConcOperator( l1.getTailConcOperator() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.OperatorDeclList  tom_get_slice_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList  begin,  tom.gom.adt.gom.types.OperatorDeclList  end, tom.gom.adt.gom.types.OperatorDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcOperator()  ||  (end== tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( begin.getHeadConcOperator() ,( tom.gom.adt.gom.types.OperatorDeclList )tom_get_slice_ConcOperator( begin.getTailConcOperator() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {     if( l1.isEmptyConcSort() ) {       return l2;     } else if( l2.isEmptyConcSort() ) {       return l1;     } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;   }    

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
    {{if ( (gomModuleList instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )(( tom.gom.adt.gom.types.GomModuleList )gomModuleList)) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )(( tom.gom.adt.gom.types.GomModuleList )gomModuleList)) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch648_end_4=(( tom.gom.adt.gom.types.GomModuleList )gomModuleList);do {{if (!( tomMatch648_end_4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch648_9= tomMatch648_end_4.getHeadConcGomModule() ;if ( (tomMatch648_9 instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )tomMatch648_9) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch648_7= tomMatch648_9.getModuleName() ; tom.gom.adt.gom.types.SectionList  tomMatch648_8= tomMatch648_9.getSectionList() ;if ( (tomMatch648_7 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch648_7) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) { String  tom_moduleName= tomMatch648_7.getName() ;if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch648_8) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch648_8) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch648_end_17=tomMatch648_8;do {{if (!( tomMatch648_end_17.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch648_21= tomMatch648_end_17.getHeadConcSection() ;if ( (tomMatch648_21 instanceof tom.gom.adt.gom.types.Section) ) {if ( ((( tom.gom.adt.gom.types.Section )tomMatch648_21) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.ProductionList  tom_prodList= tomMatch648_21.getProductionList() ;{{if ( (tom_prodList instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch649_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);do {{if (!( tomMatch649_end_4.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tom_prod= tomMatch649_end_4.getHeadConcProduction() ;









            /* Process hooks attached to a module */
            {{if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )tom_prod)) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.IdKind  tomMatch650_1= (( tom.gom.adt.gom.types.Production )tom_prod).getNameType() ;if ( (tomMatch650_1 instanceof tom.gom.adt.gom.types.IdKind) ) {if ( ((( tom.gom.adt.gom.types.IdKind )tomMatch650_1) instanceof tom.gom.adt.gom.types.idkind.KindModule) ) { String  tom_mname= (( tom.gom.adt.gom.types.Production )tom_prod).getName() ;

                //Graph-rewrite rules hooks are only allowed for sorts
                if( (( tom.gom.adt.gom.types.Production )tom_prod).getHookType() .getkind().equals("graphrules")) {
                  GomMessage.error(getLogger(), null, 0, 
                      GomMessage.graphRulesHooksAuthOnSorts);
                }
                if(tom_mname.equals(tom_moduleName)) {
                  ModuleDecl mdecl = getModuleDecl(tom_mname,moduleList);
                  HookDeclList newDeclList =
                    makeHookDeclList((( tom.gom.adt.gom.types.Production )tom_prod), tom.gom.adt.gom.types.decl.CutModule.make(mdecl) );
                  hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
                } else {
                  GomMessage.error(getLogger(), null, 0, 
                      GomMessage.moduleHooksAuthOnCurrentModule);
                }
              }}}}}}{if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )tom_prod)) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.IdKind  tomMatch650_9= (( tom.gom.adt.gom.types.Production )tom_prod).getNameType() ;if ( (tomMatch650_9 instanceof tom.gom.adt.gom.types.IdKind) ) {if ( ((( tom.gom.adt.gom.types.IdKind )tomMatch650_9) instanceof tom.gom.adt.gom.types.idkind.KindSort) ) {

                SortDecl sdecl = getSortDecl( (( tom.gom.adt.gom.types.Production )tom_prod).getName() ,tom_moduleName,moduleList);
                HookDeclList newDeclList = makeHookDeclList((( tom.gom.adt.gom.types.Production )tom_prod), tom.gom.adt.gom.types.decl.CutSort.make(sdecl) );
                hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
              }}}}}}{if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )tom_prod)) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.IdKind  tomMatch650_16= (( tom.gom.adt.gom.types.Production )tom_prod).getNameType() ;if ( (tomMatch650_16 instanceof tom.gom.adt.gom.types.IdKind) ) {if ( ((( tom.gom.adt.gom.types.IdKind )tomMatch650_16) instanceof tom.gom.adt.gom.types.idkind.KindOperator) ) {

                //Graph-rewrite rules hooks are only allowed for sorts
                if( (( tom.gom.adt.gom.types.Production )tom_prod).getHookType() .getkind().equals("graphrules")) {
                  GomMessage.error(getLogger(), null, 0, 
                      GomMessage.graphRulesHooksAuthOnSorts);
                }
                OperatorDecl odecl = getOperatorDecl( (( tom.gom.adt.gom.types.Production )tom_prod).getName() ,tom_moduleName,moduleList);
                if(odecl!=null) {
                  HookDeclList newDeclList = makeHookDeclList((( tom.gom.adt.gom.types.Production )tom_prod), tom.gom.adt.gom.types.decl.CutOperator.make(odecl) );
                  hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
                }
              }}}}}}{if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )tom_prod)) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.IdKind  tomMatch650_24= (( tom.gom.adt.gom.types.Production )tom_prod).getNameType() ;if ( (tomMatch650_24 instanceof tom.gom.adt.gom.types.IdKind) ) {if ( ((( tom.gom.adt.gom.types.IdKind )tomMatch650_24) instanceof tom.gom.adt.gom.types.idkind.KindFutureOperator) ) {

                OperatorDecl odecl = getOperatorDecl( (( tom.gom.adt.gom.types.Production )tom_prod).getName() ,tom_moduleName,moduleList);
                if(odecl!=null) {
                  HookDeclList newDeclList =
                    makeHookDeclList((( tom.gom.adt.gom.types.Production )tom_prod), tom.gom.adt.gom.types.decl.CutFutureOperator.make(odecl,  tomMatch650_24.getConsOrNil() ) );
                  hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
                }
              }}}}}}}

          }if ( tomMatch649_end_4.isEmptyConcProduction() ) {tomMatch649_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);} else {tomMatch649_end_4= tomMatch649_end_4.getTailConcProduction() ;}}} while(!( (tomMatch649_end_4==(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) ));}}}}

        ArrayList<String> examinedOps = new ArrayList<String>();
        {{if ( (tom_prodList instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch651_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);do {{if (!( tomMatch651_end_4.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch651_8= tomMatch651_end_4.getHeadConcProduction() ;if ( (tomMatch651_8 instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tomMatch651_8) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch651_7= tomMatch651_8.getAlternativeList() ;if ( (((( tom.gom.adt.gom.types.AlternativeList )tomMatch651_7) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )tomMatch651_7) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch651_end_13=tomMatch651_7;do {{if (!( tomMatch651_end_13.isEmptyConcAlternative() )) {

            hookList = addDefaultTheoryHooks( tomMatch651_end_13.getHeadConcAlternative() ,hookList,examinedOps,tom_moduleName);
          }if ( tomMatch651_end_13.isEmptyConcAlternative() ) {tomMatch651_end_13=tomMatch651_7;} else {tomMatch651_end_13= tomMatch651_end_13.getTailConcAlternative() ;}}} while(!( (tomMatch651_end_13==tomMatch651_7) ));}}}}if ( tomMatch651_end_4.isEmptyConcProduction() ) {tomMatch651_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);} else {tomMatch651_end_4= tomMatch651_end_4.getTailConcProduction() ;}}} while(!( (tomMatch651_end_4==(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) ));}}}}

      }}}if ( tomMatch648_end_17.isEmptyConcSection() ) {tomMatch648_end_17=tomMatch648_8;} else {tomMatch648_end_17= tomMatch648_end_17.getTailConcSection() ;}}} while(!( (tomMatch648_end_17==tomMatch648_8) ));}}}}}}if ( tomMatch648_end_4.isEmptyConcGomModule() ) {tomMatch648_end_4=(( tom.gom.adt.gom.types.GomModuleList )gomModuleList);} else {tomMatch648_end_4= tomMatch648_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch648_end_4==(( tom.gom.adt.gom.types.GomModuleList )gomModuleList)) ));}}}}

    return hookList;
  }

  private HookDeclList addDefaultTheoryHooks(Alternative alt,
                                             HookDeclList hookList,
                                             ArrayList<String> examinedOps,
                                             String moduleName) {
    {{if ( (alt instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )alt) instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )(( tom.gom.adt.gom.types.Alternative )alt)) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { tom.gom.adt.gom.types.FieldList  tomMatch652_3= (( tom.gom.adt.gom.types.Alternative )alt).getDomainList() ; String  tom_opName= (( tom.gom.adt.gom.types.Alternative )alt).getName() ;if ( (((( tom.gom.adt.gom.types.FieldList )tomMatch652_3) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )tomMatch652_3) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( tomMatch652_3.isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch652_12= tomMatch652_3.getHeadConcField() ;if ( (tomMatch652_12 instanceof tom.gom.adt.gom.types.Field) ) {if ( ((( tom.gom.adt.gom.types.Field )tomMatch652_12) instanceof tom.gom.adt.gom.types.field.StarredField) ) {if (  tomMatch652_3.getTailConcField() .isEmptyConcField() ) {if ( ( tomMatch652_12.getFieldType() == (( tom.gom.adt.gom.types.Alternative )alt).getCodomain() ) ) {if ( (hookList instanceof tom.gom.adt.gom.types.HookDeclList) ) {boolean tomMatch652_31= false ;if ( (((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )hookList)) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )hookList)) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch652_end_17=(( tom.gom.adt.gom.types.HookDeclList )hookList);do {{if (!( tomMatch652_end_17.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tomMatch652_22= tomMatch652_end_17.getHeadConcHookDecl() ;if ( (tomMatch652_22 instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )tomMatch652_22) instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) { tom.gom.adt.gom.types.Decl  tomMatch652_21= tomMatch652_22.getPointcut() ;if ( (tomMatch652_21 instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )tomMatch652_21) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch652_24= tomMatch652_21.getODecl() ;if ( (tomMatch652_24 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch652_24) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {if ( tom_opName.equals( tomMatch652_24.getName() ) ) {tomMatch652_31= true ;}}}}}}}}if ( tomMatch652_end_17.isEmptyConcHookDecl() ) {tomMatch652_end_17=(( tom.gom.adt.gom.types.HookDeclList )hookList);} else {tomMatch652_end_17= tomMatch652_end_17.getTailConcHookDecl() ;}}} while(!( (tomMatch652_end_17==(( tom.gom.adt.gom.types.HookDeclList )hookList)) ));}if (!(tomMatch652_31)) {




        /* generate a FL hook for list-operators without other hook */
        String emptyCode = "{}";
        Production hook =  tom.gom.adt.gom.types.production.Hook.make( tom.gom.adt.gom.types.idkind.KindOperator.make() , tom_opName,  tom.gom.adt.gom.types.hookkind.HookKind.make("FL") ,  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() , emptyCode,  tom.gom.adt.gom.types.option.EmptyOptionList.make() ) ;
        OperatorDecl odecl = getOperatorDecl(tom_opName,moduleName,moduleList);
        if (odecl!=null) {
          HookDeclList newDeclList = makeHookDeclList(hook, tom.gom.adt.gom.types.decl.CutOperator.make(odecl) );
          hookList = tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
        }
      }}}}}}}}}}}}{if ( (alt instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )alt) instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )(( tom.gom.adt.gom.types.Alternative )alt)) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { tom.gom.adt.gom.types.FieldList  tomMatch652_35= (( tom.gom.adt.gom.types.Alternative )alt).getDomainList() ; String  tom_opName= (( tom.gom.adt.gom.types.Alternative )alt).getName() ;if ( (((( tom.gom.adt.gom.types.FieldList )tomMatch652_35) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )tomMatch652_35) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( tomMatch652_35.isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch652_50= tomMatch652_35.getHeadConcField() ;if ( (tomMatch652_50 instanceof tom.gom.adt.gom.types.Field) ) {if ( ((( tom.gom.adt.gom.types.Field )tomMatch652_50) instanceof tom.gom.adt.gom.types.field.StarredField) ) {if (  tomMatch652_35.getTailConcField() .isEmptyConcField() ) {if ( ( tomMatch652_50.getFieldType() == (( tom.gom.adt.gom.types.Alternative )alt).getCodomain() ) ) {if ( (hookList instanceof tom.gom.adt.gom.types.HookDeclList) ) {if ( (((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )hookList)) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )hookList)) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch652_end_45=(( tom.gom.adt.gom.types.HookDeclList )hookList);do {{if (!( tomMatch652_end_45.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tomMatch652_53= tomMatch652_end_45.getHeadConcHookDecl() ;if ( (tomMatch652_53 instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )tomMatch652_53) instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) { tom.gom.adt.gom.types.HookKind  tomMatch652_52= tomMatch652_53.getHookType() ;if ( (tomMatch652_52 instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tomMatch652_52) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch652_55= tomMatch652_52.getkind() ;boolean tomMatch652_63= false ; String  tomMatch652_59= "" ; String  tomMatch652_60= "" ; String  tomMatch652_61= "" ;if ( true ) {if ( "make_insert".equals(tomMatch652_55) ) {{tomMatch652_63= true ;tomMatch652_59=tomMatch652_55;}} else {if ( true ) {if ( "make_empty".equals(tomMatch652_55) ) {{tomMatch652_63= true ;tomMatch652_60=tomMatch652_55;}} else {if ( true ) {if ( "rules".equals(tomMatch652_55) ) {{tomMatch652_63= true ;tomMatch652_61=tomMatch652_55;}}}}}}}if (tomMatch652_63) {





        if (!examinedOps.contains(tom_opName)) {
          examinedOps.add(tom_opName);
          {{if ( (hookList instanceof tom.gom.adt.gom.types.HookDeclList) ) {if ( true ) {boolean tomMatch653_28= false ;if ( (((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )hookList)) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )hookList)) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch653_end_5=(( tom.gom.adt.gom.types.HookDeclList )hookList);do {{if (!( tomMatch653_end_5.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tomMatch653_10= tomMatch653_end_5.getHeadConcHookDecl() ;if ( (tomMatch653_10 instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )tomMatch653_10) instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) { tom.gom.adt.gom.types.Decl  tomMatch653_8= tomMatch653_10.getPointcut() ; tom.gom.adt.gom.types.HookKind  tomMatch653_9= tomMatch653_10.getHookType() ;if ( (tomMatch653_8 instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )tomMatch653_8) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch653_12= tomMatch653_8.getODecl() ;if ( (tomMatch653_12 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch653_12) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {if ( (( String )tom_opName).equals( tomMatch653_12.getName() ) ) {if ( (tomMatch653_9 instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tomMatch653_9) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch653_18= tomMatch653_9.getkind() ;boolean tomMatch653_29= false ; String  tomMatch653_22= "" ; String  tomMatch653_23= "" ; String  tomMatch653_25= "" ; String  tomMatch653_26= "" ; String  tomMatch653_24= "" ;if ( true ) {if ( "Free".equals(tomMatch653_18) ) {{tomMatch653_29= true ;tomMatch653_22=tomMatch653_18;}} else {if ( true ) {if ( "FL".equals(tomMatch653_18) ) {{tomMatch653_29= true ;tomMatch653_23=tomMatch653_18;}} else {if ( true ) {if ( "AU".equals(tomMatch653_18) ) {{tomMatch653_29= true ;tomMatch653_24=tomMatch653_18;}} else {if ( true ) {if ( "AC".equals(tomMatch653_18) ) {{tomMatch653_29= true ;tomMatch653_25=tomMatch653_18;}} else {if ( true ) {if ( "ACU".equals(tomMatch653_18) ) {{tomMatch653_29= true ;tomMatch653_26=tomMatch653_18;}}}}}}}}}}}if (tomMatch653_29) {tomMatch653_28= true ;}}}}}}}}}}}if ( tomMatch653_end_5.isEmptyConcHookDecl() ) {tomMatch653_end_5=(( tom.gom.adt.gom.types.HookDeclList )hookList);} else {tomMatch653_end_5= tomMatch653_end_5.getTailConcHookDecl() ;}}} while(!( (tomMatch653_end_5==(( tom.gom.adt.gom.types.HookDeclList )hookList)) ));}if (!(tomMatch653_28)) {


              /* generate an error to make users specify the theory */
              GomMessage.error(getLogger(), null, 0, 
                  GomMessage.mustSpecifyAssociatedTheoryForVarOp,tom_opName);
            }}}}}

        }
      }}}}}}if ( tomMatch652_end_45.isEmptyConcHookDecl() ) {tomMatch652_end_45=(( tom.gom.adt.gom.types.HookDeclList )hookList);} else {tomMatch652_end_45= tomMatch652_end_45.getTailConcHookDecl() ;}}} while(!( (tomMatch652_end_45==(( tom.gom.adt.gom.types.HookDeclList )hookList)) ));}}}}}}}}}}}}}

    return hookList;
  }

  private HookDeclList makeHookDeclList(Production hook, Decl mdecl) {
    {{if ( (hook instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )hook) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )hook)) instanceof tom.gom.adt.gom.types.production.Hook) ) { tom.gom.adt.gom.types.HookKind  tom_hkind= (( tom.gom.adt.gom.types.Production )hook).getHookType() ; String  tom_hName= (( tom.gom.adt.gom.types.Production )hook).getName() ; tom.gom.adt.gom.types.ArgList  tom_hookArgs= (( tom.gom.adt.gom.types.Production )hook).getArgs() ; String  tom_scode= (( tom.gom.adt.gom.types.Production )hook).getStringCode() ;




          HookDeclList newHookList =  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
          {{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_1= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "block".equals(tomMatch655_1) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ,  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_7= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "javablock".equals(tomMatch655_7) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ,  false ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_13= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "interface".equals(tomMatch655_13) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.InterfaceHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_19= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "import".equals(tomMatch655_19) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.ImportHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_25= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "mapping".equals(tomMatch655_25) ) {

              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MappingHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_31= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;boolean tomMatch655_38= false ; String  tomMatch655_36= "" ; String  tomMatch655_37= "" ; String  tomMatch655_35= "" ;if ( true ) {if ( "make".equals(tomMatch655_31) ) {{tomMatch655_38= true ;tomMatch655_35=tomMatch655_31;}} else {if ( true ) {if ( "make_insert".equals(tomMatch655_31) ) {{tomMatch655_38= true ;tomMatch655_36=tomMatch655_31;}} else {if ( true ) {if ( "make_empty".equals(tomMatch655_31) ) {{tomMatch655_38= true ;tomMatch655_37=tomMatch655_31;}}}}}}}if (tomMatch655_38) {

              SlotList typedArgs = typeArguments(tom_hookArgs,tom_hkind,mdecl);
              if (null == typedArgs) {
                GomMessage.error(getLogger(),null,0,
                    GomMessage.discardedHook, (tom_hName));
                return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
              }
              newHookList =  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl, typedArgs,  tom.gom.adt.code.types.code.Code.make(tom_scode) , (( tom.gom.adt.gom.types.HookKind )tom_hkind),  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_40= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "Free".equals(tomMatch655_40) ) {

              /* Even there is no code associated, we generate a MakeHook to
               * prevent FL hooks to be automatically generated */
              return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ,  tom.gom.adt.code.types.code.Code.make("") ,  tom.gom.adt.gom.types.hookkind.HookKind.make("Free") ,  false ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) 
;
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_46= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "FL".equals(tomMatch655_46) ) {

              /* FL: flattened list */
              return makeFLHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_52= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "AU".equals(tomMatch655_52) ) {

              return makeAUHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_58= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "AC".equals(tomMatch655_58) ) {

              return makeACHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_64= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "ACU".equals(tomMatch655_64) ) {

              return makeACUHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_70= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "rules".equals(tomMatch655_70) ) {

              return makeRulesHookList(tom_hName,mdecl,tom_scode);
            }}}}}}{if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )tom_hkind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch655_76= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;if ( true ) {if ( "graphrules".equals(tomMatch655_76) ) {

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
    {{if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )moduleList)) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )moduleList)) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch656_end_4=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch656_end_4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch656_8= tomMatch656_end_4.getHeadConcModule() ;if ( (tomMatch656_8 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch656_8) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch656_7= tomMatch656_8.getMDecl() ;if ( (tomMatch656_7 instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )tomMatch656_7) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch656_10= tomMatch656_7.getModuleName() ;if ( (tomMatch656_10 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch656_10) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {




        if ( tomMatch656_10.getName() .equals(mname)) {
          return tomMatch656_7;
        }
      }}}}}}}if ( tomMatch656_end_4.isEmptyConcModule() ) {tomMatch656_end_4=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch656_end_4= tomMatch656_end_4.getTailConcModule() ;}}} while(!( (tomMatch656_end_4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}

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
    {{if ( true ) {if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )moduleList)) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )moduleList)) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch657_end_5=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch657_end_5.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch657_10= tomMatch657_end_5.getHeadConcModule() ;if ( (tomMatch657_10 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch657_10) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch657_8= tomMatch657_10.getMDecl() ; tom.gom.adt.gom.types.SortList  tomMatch657_9= tomMatch657_10.getSorts() ;if ( (tomMatch657_8 instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )tomMatch657_8) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch657_12= tomMatch657_8.getModuleName() ;if ( (tomMatch657_12 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch657_12) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {if ( (( String )modName).equals( tomMatch657_12.getName() ) ) {if ( (((( tom.gom.adt.gom.types.SortList )tomMatch657_9) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )tomMatch657_9) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch657_end_21=tomMatch657_9;do {{if (!( tomMatch657_end_21.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch657_26= tomMatch657_end_21.getHeadConcSort() ;if ( (tomMatch657_26 instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )tomMatch657_26) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch657_25= tomMatch657_26.getDecl() ;if ( (tomMatch657_25 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch657_25) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {






        if ( tomMatch657_25.getName() .equals(sname)) {
          return tomMatch657_25;
        }
      }}}}}if ( tomMatch657_end_21.isEmptyConcSort() ) {tomMatch657_end_21=tomMatch657_9;} else {tomMatch657_end_21= tomMatch657_end_21.getTailConcSort() ;}}} while(!( (tomMatch657_end_21==tomMatch657_9) ));}}}}}}}}}if ( tomMatch657_end_5.isEmptyConcModule() ) {tomMatch657_end_5=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch657_end_5= tomMatch657_end_5.getTailConcModule() ;}}} while(!( (tomMatch657_end_5==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}}

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
    {{if ( true ) {if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )moduleList)) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )moduleList)) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch658_end_5=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch658_end_5.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch658_10= tomMatch658_end_5.getHeadConcModule() ;if ( (tomMatch658_10 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch658_10) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch658_8= tomMatch658_10.getMDecl() ; tom.gom.adt.gom.types.SortList  tomMatch658_9= tomMatch658_10.getSorts() ;if ( (tomMatch658_8 instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )tomMatch658_8) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch658_12= tomMatch658_8.getModuleName() ;if ( (tomMatch658_12 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch658_12) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {if ( (( String )modName).equals( tomMatch658_12.getName() ) ) {if ( (((( tom.gom.adt.gom.types.SortList )tomMatch658_9) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )tomMatch658_9) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch658_end_21=tomMatch658_9;do {{if (!( tomMatch658_end_21.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch658_26= tomMatch658_end_21.getHeadConcSort() ;if ( (tomMatch658_26 instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )tomMatch658_26) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch658_25= tomMatch658_26.getOperatorDecls() ;if ( (((( tom.gom.adt.gom.types.OperatorDeclList )tomMatch658_25) instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || ((( tom.gom.adt.gom.types.OperatorDeclList )tomMatch658_25) instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch658_end_31=tomMatch658_25;do {{if (!( tomMatch658_end_31.isEmptyConcOperator() )) { tom.gom.adt.gom.types.OperatorDecl  tomMatch658_35= tomMatch658_end_31.getHeadConcOperator() ;if ( (tomMatch658_35 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch658_35) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {








        if ( tomMatch658_35.getName() .equals(oname)) {
          return  tomMatch658_end_31.getHeadConcOperator() ;
        }
      }}}if ( tomMatch658_end_31.isEmptyConcOperator() ) {tomMatch658_end_31=tomMatch658_25;} else {tomMatch658_end_31= tomMatch658_end_31.getTailConcOperator() ;}}} while(!( (tomMatch658_end_31==tomMatch658_25) ));}}}}if ( tomMatch658_end_21.isEmptyConcSort() ) {tomMatch658_end_21=tomMatch658_9;} else {tomMatch658_end_21= tomMatch658_end_21.getTailConcSort() ;}}} while(!( (tomMatch658_end_21==tomMatch658_9) ));}}}}}}}}}if ( tomMatch658_end_5.isEmptyConcModule() ) {tomMatch658_end_5=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch658_end_5= tomMatch658_end_5.getTailConcModule() ;}}} while(!( (tomMatch658_end_5==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.orphanedHook, oname);
    return null;
  }

  private SlotList typeArguments(
      ArgList args,
      HookKind kind,
      Decl decl) {
    {{if ( (kind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )kind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )kind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch659_1= (( tom.gom.adt.gom.types.HookKind )kind).getkind() ;if ( true ) {if ( "make".equals(tomMatch659_1) ) {

        /*
         * The TypedProduction has to be Slots
         * A KindMakeHook is attached to an operator
         */
        {{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )decl)) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch660_1= (( tom.gom.adt.gom.types.Decl )decl).getODecl() ;if ( (tomMatch660_1 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch660_1) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch660_4= tomMatch660_1.getProd() ;if ( (tomMatch660_4 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch660_4) instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tom_slotList= tomMatch660_4.getSlots() ;

            /* tests the arguments number */
            if (args.length() != tom_slotList.length()) {
              SlotList slist = tom_slotList;
              GomMessage.error(getLogger(), null, 0,
                  GomMessage.mismatchedMakeArguments, args, slist);
              return null;
            }
            /* Then check the types */
            return recArgSlots(args,tom_slotList);
          }}}}}}}}{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {

            GomMessage.error(getLogger(), null, 0,
                GomMessage.unsupportedHookAlgebraic, kind);
            return null;
          }}}

      }}}}}}{if ( (kind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )kind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )kind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch659_7= (( tom.gom.adt.gom.types.HookKind )kind).getkind() ;if ( true ) {if ( "make_insert".equals(tomMatch659_7) ) {

        /*
         * The TypedProduction has to be Variadic
         * Then we get the codomain from the operatordecl
         */
        {{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )decl)) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch661_1= (( tom.gom.adt.gom.types.Decl )decl).getODecl() ;if ( (tomMatch661_1 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch661_1) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch661_5= tomMatch661_1.getProd() ;if ( (tomMatch661_5 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch661_5) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {


              // for a make_insert hook, there are two arguments: head, tail
              {{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if (!( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch662_5= (( tom.gom.adt.gom.types.ArgList )args).getHeadConcArg() ;if ( (tomMatch662_5 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch662_5) instanceof tom.gom.adt.gom.types.arg.Arg) ) { tom.gom.adt.gom.types.ArgList  tomMatch662_2= (( tom.gom.adt.gom.types.ArgList )args).getTailConcArg() ;if (!( tomMatch662_2.isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch662_8= tomMatch662_2.getHeadConcArg() ;if ( (tomMatch662_8 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch662_8) instanceof tom.gom.adt.gom.types.arg.Arg) ) {if (  tomMatch662_2.getTailConcArg() .isEmptyConcArg() ) {

                  return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch662_5.getName() ,  tomMatch661_5.getSort() ) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch662_8.getName() ,  tomMatch661_1.getSort() ) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ;
                }}}}}}}}}}{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {

                  GomMessage.error(getLogger(), null, 0,
                      GomMessage.badHookArguments, 
                      (tomMatch659_7), Integer.valueOf(args.length()));
                  return null;
                }}}

            }}}}}}}}{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {

            GomMessage.error(getLogger(),null, 0,
                GomMessage.unsupportedHookVariadic, kind);
            return null;
          }}}

      }}}}}}{if ( (kind instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )kind) instanceof tom.gom.adt.gom.types.HookKind) ) {if ( ((( tom.gom.adt.gom.types.HookKind )(( tom.gom.adt.gom.types.HookKind )kind)) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) { String  tomMatch659_13= (( tom.gom.adt.gom.types.HookKind )kind).getkind() ;if ( true ) {if ( "make_empty".equals(tomMatch659_13) ) {

        /*
         * The TypedProduction has to be Variadic
         * Then we get the codomain from the operatordecl
         */
        {{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )decl)) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch663_1= (( tom.gom.adt.gom.types.Decl )decl).getODecl() ;if ( (tomMatch663_1 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch663_1) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch663_4= tomMatch663_1.getProd() ;if ( (tomMatch663_4 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch663_4) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {


              // for a make_empty hook, there is no argument
              {{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if ( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() ) {
 return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ; }}}}{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {

                  GomMessage.error(getLogger(), null, 0,
                      GomMessage.badHookArguments,
                      (tomMatch659_13), Integer.valueOf(args.length()));
                  return null;
                }}}

            }}}}}}}}{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {

            GomMessage.error(getLogger(), null, 0,
                GomMessage.unsupportedHookVariadic, kind);
            return null;
          }}}

      }}}}}}}

    throw new GomRuntimeException("Hook kind \""+kind+"\" not supported");
  }

  private SlotList recArgSlots(ArgList args, SlotList slots) {
    {{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if ( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() ) {if ( (slots instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )(( tom.gom.adt.gom.types.SlotList )slots)) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )(( tom.gom.adt.gom.types.SlotList )slots)) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if ( (( tom.gom.adt.gom.types.SlotList )slots).isEmptyConcSlot() ) {

        return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
      }}}}}}}{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if (!( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch665_13= (( tom.gom.adt.gom.types.ArgList )args).getHeadConcArg() ;if ( (tomMatch665_13 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch665_13) instanceof tom.gom.adt.gom.types.arg.Arg) ) {if ( (slots instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )(( tom.gom.adt.gom.types.SlotList )slots)) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )(( tom.gom.adt.gom.types.SlotList )slots)) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if (!( (( tom.gom.adt.gom.types.SlotList )slots).isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch665_16= (( tom.gom.adt.gom.types.SlotList )slots).getHeadConcSlot() ;if ( (tomMatch665_16 instanceof tom.gom.adt.gom.types.Slot) ) {if ( ((( tom.gom.adt.gom.types.Slot )tomMatch665_16) instanceof tom.gom.adt.gom.types.slot.Slot) ) {

        SlotList tail = recArgSlots( (( tom.gom.adt.gom.types.ArgList )args).getTailConcArg() , (( tom.gom.adt.gom.types.SlotList )slots).getTailConcSlot() );
        return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch665_13.getName() ,  tomMatch665_16.getSort() ) ,tom_append_list_ConcSlot(tail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;
      }}}}}}}}}}}}

    throw new GomRuntimeException("GomTypeExpander:recArgSlots failed "+args+" "+slots);
  }

  private String trimBracket(String stringCode) {
    int start = stringCode.indexOf('{')+1;
    int end = stringCode.lastIndexOf('}');
    return stringCode.substring(start,end).trim();
  }

  private SortDecl getSortAndCheck(Decl mdecl) {
    {{if ( (mdecl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )mdecl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )mdecl)) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch666_1= (( tom.gom.adt.gom.types.Decl )mdecl).getODecl() ;if ( (tomMatch666_1 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch666_1) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch666_5= tomMatch666_1.getProd() ;if ( (tomMatch666_5 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch666_5) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) { tom.gom.adt.gom.types.SortDecl  tom_sdecl= tomMatch666_5.getSort() ;

        if ( tomMatch666_1.getSort() == tom_sdecl) {
          return tom_sdecl;
        } else {
          GomMessage.error(getLogger(), null, 0, GomMessage.differentDomainCodomain);
        }
      }}}}}}}}{if ( (mdecl instanceof tom.gom.adt.gom.types.Decl) ) {

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
    {{if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {if ( (((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )(( tom.gom.adt.gom.types.ArgList )args)) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {if (!( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch667_5= (( tom.gom.adt.gom.types.ArgList )args).getHeadConcArg() ;if ( (tomMatch667_5 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch667_5) instanceof tom.gom.adt.gom.types.arg.Arg) ) { String  tom_stratname= tomMatch667_5.getName() ; tom.gom.adt.gom.types.ArgList  tomMatch667_2= (( tom.gom.adt.gom.types.ArgList )args).getTailConcArg() ;if (!( tomMatch667_2.isEmptyConcArg() )) { tom.gom.adt.gom.types.Arg  tomMatch667_8= tomMatch667_2.getHeadConcArg() ;if ( (tomMatch667_8 instanceof tom.gom.adt.gom.types.Arg) ) {if ( ((( tom.gom.adt.gom.types.Arg )tomMatch667_8) instanceof tom.gom.adt.gom.types.arg.Arg) ) { String  tom_defaultstrat= tomMatch667_8.getName() ;if (  tomMatch667_2.getTailConcArg() .isEmptyConcArg() ) {

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
