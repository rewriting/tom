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



  private static   tom.gom.adt.code.types.Code  tom_append_list_CodeList( tom.gom.adt.code.types.Code  l1,  tom.gom.adt.code.types.Code  l2) {
    if( l1.isEmptyCodeList() ) {
      return l2;
    } else if( l2.isEmptyCodeList() ) {
      return l1;
    } else if( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {
      if(  l1.getTailCodeList() .isEmptyCodeList() ) {
        return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,l2) ;
      } else {
        return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,tom_append_list_CodeList( l1.getTailCodeList() ,l2)) ;
      }
    } else {
      return  tom.gom.adt.code.types.code.ConsCodeList.make(l1,l2) ;
    }
  }
  private static   tom.gom.adt.code.types.Code  tom_get_slice_CodeList( tom.gom.adt.code.types.Code  begin,  tom.gom.adt.code.types.Code  end, tom.gom.adt.code.types.Code  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyCodeList()  ||  (end== tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getHeadCodeList() :begin),( tom.gom.adt.code.types.Code )tom_get_slice_CodeList((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.ArgList  tom_append_list_ConcArg( tom.gom.adt.gom.types.ArgList l1,  tom.gom.adt.gom.types.ArgList  l2) {
    if( l1.isEmptyConcArg() ) {
      return l2;
    } else if( l2.isEmptyConcArg() ) {
      return l1;
    } else if(  l1.getTailConcArg() .isEmptyConcArg() ) {
      return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,tom_append_list_ConcArg( l1.getTailConcArg() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.ArgList  tom_get_slice_ConcArg( tom.gom.adt.gom.types.ArgList  begin,  tom.gom.adt.gom.types.ArgList  end, tom.gom.adt.gom.types.ArgList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcArg()  ||  (end== tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( begin.getHeadConcArg() ,( tom.gom.adt.gom.types.ArgList )tom_get_slice_ConcArg( begin.getTailConcArg() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.AlternativeList  tom_append_list_ConcAlternative( tom.gom.adt.gom.types.AlternativeList l1,  tom.gom.adt.gom.types.AlternativeList  l2) {
    if( l1.isEmptyConcAlternative() ) {
      return l2;
    } else if( l2.isEmptyConcAlternative() ) {
      return l1;
    } else if(  l1.getTailConcAlternative() .isEmptyConcAlternative() ) {
      return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,tom_append_list_ConcAlternative( l1.getTailConcAlternative() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.AlternativeList  tom_get_slice_ConcAlternative( tom.gom.adt.gom.types.AlternativeList  begin,  tom.gom.adt.gom.types.AlternativeList  end, tom.gom.adt.gom.types.AlternativeList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcAlternative()  ||  (end== tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( begin.getHeadConcAlternative() ,( tom.gom.adt.gom.types.AlternativeList )tom_get_slice_ConcAlternative( begin.getTailConcAlternative() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {
    if( l1.isEmptyConcModule() ) {
      return l2;
    } else if( l2.isEmptyConcModule() ) {
      return l1;
    } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {
      return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.HookDeclList  tom_append_list_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList l1,  tom.gom.adt.gom.types.HookDeclList  l2) {
    if( l1.isEmptyConcHookDecl() ) {
      return l2;
    } else if( l2.isEmptyConcHookDecl() ) {
      return l1;
    } else if(  l1.getTailConcHookDecl() .isEmptyConcHookDecl() ) {
      return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,tom_append_list_ConcHookDecl( l1.getTailConcHookDecl() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.HookDeclList  tom_get_slice_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList  begin,  tom.gom.adt.gom.types.HookDeclList  end, tom.gom.adt.gom.types.HookDeclList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcHookDecl()  ||  (end== tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( begin.getHeadConcHookDecl() ,( tom.gom.adt.gom.types.HookDeclList )tom_get_slice_ConcHookDecl( begin.getTailConcHookDecl() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {
    if( l1.isEmptyConcProduction() ) {
      return l2;
    } else if( l2.isEmptyConcProduction() ) {
      return l1;
    } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {
      return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {
    if( l1.isEmptyConcGomModule() ) {
      return l2;
    } else if( l2.isEmptyConcGomModule() ) {
      return l1;
    } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {
      return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {
    if( l1.isEmptyConcSection() ) {
      return l2;
    } else if( l2.isEmptyConcSection() ) {
      return l1;
    } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {
      return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {
    if( l1.isEmptyConcSlot() ) {
      return l2;
    } else if( l2.isEmptyConcSlot() ) {
      return l1;
    } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {
      return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.Option  tom_append_list_OptionList( tom.gom.adt.gom.types.Option  l1,  tom.gom.adt.gom.types.Option  l2) {
    if( l1.isEmptyOptionList() ) {
      return l2;
    } else if( l2.isEmptyOptionList() ) {
      return l1;
    } else if( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) ) {
      if(  l1.getTailOptionList() .isEmptyOptionList() ) {
        return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,l2) ;
      } else {
        return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,tom_append_list_OptionList( l1.getTailOptionList() ,l2)) ;
      }
    } else {
      return  tom.gom.adt.gom.types.option.ConsOptionList.make(l1,l2) ;
    }
  }
  private static   tom.gom.adt.gom.types.Option  tom_get_slice_OptionList( tom.gom.adt.gom.types.Option  begin,  tom.gom.adt.gom.types.Option  end, tom.gom.adt.gom.types.Option  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyOptionList()  ||  (end== tom.gom.adt.gom.types.option.EmptyOptionList.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.option.ConsOptionList.make((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getHeadOptionList() :begin),( tom.gom.adt.gom.types.Option )tom_get_slice_OptionList((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getTailOptionList() : tom.gom.adt.gom.types.option.EmptyOptionList.make() ),end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.OperatorDeclList  tom_append_list_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList l1,  tom.gom.adt.gom.types.OperatorDeclList  l2) {
    if( l1.isEmptyConcOperator() ) {
      return l2;
    } else if( l2.isEmptyConcOperator() ) {
      return l1;
    } else if(  l1.getTailConcOperator() .isEmptyConcOperator() ) {
      return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,tom_append_list_ConcOperator( l1.getTailConcOperator() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.OperatorDeclList  tom_get_slice_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList  begin,  tom.gom.adt.gom.types.OperatorDeclList  end, tom.gom.adt.gom.types.OperatorDeclList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcOperator()  ||  (end== tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( begin.getHeadConcOperator() ,( tom.gom.adt.gom.types.OperatorDeclList )tom_get_slice_ConcOperator( begin.getTailConcOperator() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {
    if( l1.isEmptyConcField() ) {
      return l2;
    } else if( l2.isEmptyConcField() ) {
      return l1;
    } else if(  l1.getTailConcField() .isEmptyConcField() ) {
      return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.GrammarList  tom_append_list_ConcGrammar( tom.gom.adt.gom.types.GrammarList l1,  tom.gom.adt.gom.types.GrammarList  l2) {
    if( l1.isEmptyConcGrammar() ) {
      return l2;
    } else if( l2.isEmptyConcGrammar() ) {
      return l1;
    } else if(  l1.getTailConcGrammar() .isEmptyConcGrammar() ) {
      return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,tom_append_list_ConcGrammar( l1.getTailConcGrammar() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.GrammarList  tom_get_slice_ConcGrammar( tom.gom.adt.gom.types.GrammarList  begin,  tom.gom.adt.gom.types.GrammarList  end, tom.gom.adt.gom.types.GrammarList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcGrammar()  ||  (end== tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( begin.getHeadConcGrammar() ,( tom.gom.adt.gom.types.GrammarList )tom_get_slice_ConcGrammar( begin.getTailConcGrammar() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {
    if( l1.isEmptyConcSort() ) {
      return l2;
    } else if( l2.isEmptyConcSort() ) {
      return l1;
    } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {
      return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;
    } else {
      return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;
    }
  }
  private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;
  }
  

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
HookDeclList hookList = 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
/* For each hook in the GomModuleList */

{
{
if ( (gomModuleList instanceof tom.gom.adt.gom.types.GomModuleList) ) {
if ( (((( tom.gom.adt.gom.types.GomModuleList )gomModuleList) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )gomModuleList) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) {
 tom.gom.adt.gom.types.GomModuleList  tomMatch1__end__4=(( tom.gom.adt.gom.types.GomModuleList )gomModuleList);
do {
{
if (!( tomMatch1__end__4.isEmptyConcGomModule() )) {
 tom.gom.adt.gom.types.GomModule  tomMatch1_9= tomMatch1__end__4.getHeadConcGomModule() ;
if ( (tomMatch1_9 instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) {
 tom.gom.adt.gom.types.GomModuleName  tomMatch1_7= tomMatch1_9.getModuleName() ;
 tom.gom.adt.gom.types.SectionList  tomMatch1_8= tomMatch1_9.getSectionList() ;
if ( (tomMatch1_7 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {
 String  tom_moduleName= tomMatch1_7.getName() ;
if ( ((tomMatch1_8 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch1_8 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) {
 tom.gom.adt.gom.types.SectionList  tomMatch1__end__15=tomMatch1_8;
do {
{
if (!( tomMatch1__end__15.isEmptyConcSection() )) {
 tom.gom.adt.gom.types.Section  tomMatch1_19= tomMatch1__end__15.getHeadConcSection() ;
if ( (tomMatch1_19 instanceof tom.gom.adt.gom.types.section.Public) ) {
 tom.gom.adt.gom.types.GrammarList  tomMatch1_18= tomMatch1_19.getGrammarList() ;
if ( ((tomMatch1_18 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch1_18 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) {
 tom.gom.adt.gom.types.GrammarList  tomMatch1__end__23=tomMatch1_18;
do {
{
if (!( tomMatch1__end__23.isEmptyConcGrammar() )) {
 tom.gom.adt.gom.types.Grammar  tomMatch1_27= tomMatch1__end__23.getHeadConcGrammar() ;
if ( (tomMatch1_27 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) {
 tom.gom.adt.gom.types.ProductionList  tom_prodList= tomMatch1_27.getProductionList() ;
{
{
if ( (tom_prodList instanceof tom.gom.adt.gom.types.ProductionList) ) {
if ( (((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) {
 tom.gom.adt.gom.types.ProductionList  tomMatch2__end__4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);
do {
{
if (!( tomMatch2__end__4.isEmptyConcProduction() )) {
 tom.gom.adt.gom.types.Production  tom_prod= tomMatch2__end__4.getHeadConcProduction() ;


/* Process hooks attached to a module */

{
{
if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {
if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.production.Hook) ) {
if ( ( (( tom.gom.adt.gom.types.Production )tom_prod).getNameType()  instanceof tom.gom.adt.gom.types.idkind.KindModule) ) {
 String  tom_mname= (( tom.gom.adt.gom.types.Production )tom_prod).getName() ;

//Graph-rewrite rules hooks are only allowed for sorts
if(
 (( tom.gom.adt.gom.types.Production )tom_prod).getHookType() .getkind().equals("graphrules")) {
GomMessage.error(getLogger(), null, 0, 
GomMessage.graphRulesHooksAuthOnSorts);
}
if(
tom_mname.equals(
tom_moduleName)) {
ModuleDecl mdecl = getModuleDecl(
tom_mname,moduleList);
HookDeclList newDeclList =
makeHookDeclList(
(( tom.gom.adt.gom.types.Production )tom_prod),
 tom.gom.adt.gom.types.decl.CutModule.make(mdecl) );
hookList = 
tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
} else {
GomMessage.error(getLogger(), null, 0, 
GomMessage.moduleHooksAuthOnCurrentModule);
}


}
}
}

}
{
if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {
if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.production.Hook) ) {
if ( ( (( tom.gom.adt.gom.types.Production )tom_prod).getNameType()  instanceof tom.gom.adt.gom.types.idkind.KindSort) ) {

SortDecl sdecl = getSortDecl(
 (( tom.gom.adt.gom.types.Production )tom_prod).getName() ,
tom_moduleName,moduleList);
HookDeclList newDeclList = makeHookDeclList(
(( tom.gom.adt.gom.types.Production )tom_prod),
 tom.gom.adt.gom.types.decl.CutSort.make(sdecl) );
hookList = 
tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));


}
}
}

}
{
if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {
if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.production.Hook) ) {
if ( ( (( tom.gom.adt.gom.types.Production )tom_prod).getNameType()  instanceof tom.gom.adt.gom.types.idkind.KindOperator) ) {

//Graph-rewrite rules hooks are only allowed for sorts
if(
 (( tom.gom.adt.gom.types.Production )tom_prod).getHookType() .getkind().equals("graphrules")) {
GomMessage.error(getLogger(), null, 0, 
GomMessage.graphRulesHooksAuthOnSorts);
}
OperatorDecl odecl = getOperatorDecl(
 (( tom.gom.adt.gom.types.Production )tom_prod).getName() ,
tom_moduleName,moduleList);
if(odecl!=null) {
HookDeclList newDeclList = makeHookDeclList(
(( tom.gom.adt.gom.types.Production )tom_prod),
 tom.gom.adt.gom.types.decl.CutOperator.make(odecl) );
hookList = 
tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
}


}
}
}

}
{
if ( (tom_prod instanceof tom.gom.adt.gom.types.Production) ) {
if ( ((( tom.gom.adt.gom.types.Production )tom_prod) instanceof tom.gom.adt.gom.types.production.Hook) ) {
 tom.gom.adt.gom.types.IdKind  tomMatch3_18= (( tom.gom.adt.gom.types.Production )tom_prod).getNameType() ;
if ( (tomMatch3_18 instanceof tom.gom.adt.gom.types.idkind.KindFutureOperator) ) {

OperatorDecl odecl = getOperatorDecl(
 (( tom.gom.adt.gom.types.Production )tom_prod).getName() ,
tom_moduleName,moduleList);
if(odecl!=null) {
HookDeclList newDeclList =
makeHookDeclList(
(( tom.gom.adt.gom.types.Production )tom_prod),
 tom.gom.adt.gom.types.decl.CutFutureOperator.make(odecl,  tomMatch3_18.getConsOrNil() ) );
hookList = 
tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
}


}
}
}

}


}



}
if ( tomMatch2__end__4.isEmptyConcProduction() ) {
tomMatch2__end__4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);
} else {
tomMatch2__end__4= tomMatch2__end__4.getTailConcProduction() ;
}

}
} while(!( (tomMatch2__end__4==(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) ));
}
}

}

}

ArrayList<String> examinedOps = new ArrayList<String>();

{
{
if ( (tom_prodList instanceof tom.gom.adt.gom.types.ProductionList) ) {
if ( (((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tom_prodList) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) {
 tom.gom.adt.gom.types.ProductionList  tomMatch4__end__4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);
do {
{
if (!( tomMatch4__end__4.isEmptyConcProduction() )) {
 tom.gom.adt.gom.types.Production  tomMatch4_8= tomMatch4__end__4.getHeadConcProduction() ;
if ( (tomMatch4_8 instanceof tom.gom.adt.gom.types.production.SortType) ) {
 tom.gom.adt.gom.types.AlternativeList  tomMatch4_7= tomMatch4_8.getAlternativeList() ;
if ( ((tomMatch4_7 instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || (tomMatch4_7 instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) {
 tom.gom.adt.gom.types.AlternativeList  tomMatch4__end__12=tomMatch4_7;
do {
{
if (!( tomMatch4__end__12.isEmptyConcAlternative() )) {

hookList = addDefaultTheoryHooks(
 tomMatch4__end__12.getHeadConcAlternative() ,
hookList,examinedOps,
tom_moduleName);


}
if ( tomMatch4__end__12.isEmptyConcAlternative() ) {
tomMatch4__end__12=tomMatch4_7;
} else {
tomMatch4__end__12= tomMatch4__end__12.getTailConcAlternative() ;
}

}
} while(!( (tomMatch4__end__12==tomMatch4_7) ));
}
}
}
if ( tomMatch4__end__4.isEmptyConcProduction() ) {
tomMatch4__end__4=(( tom.gom.adt.gom.types.ProductionList )tom_prodList);
} else {
tomMatch4__end__4= tomMatch4__end__4.getTailConcProduction() ;
}

}
} while(!( (tomMatch4__end__4==(( tom.gom.adt.gom.types.ProductionList )tom_prodList)) ));
}
}

}

}



}
}
if ( tomMatch1__end__23.isEmptyConcGrammar() ) {
tomMatch1__end__23=tomMatch1_18;
} else {
tomMatch1__end__23= tomMatch1__end__23.getTailConcGrammar() ;
}

}
} while(!( (tomMatch1__end__23==tomMatch1_18) ));
}
}
}
if ( tomMatch1__end__15.isEmptyConcSection() ) {
tomMatch1__end__15=tomMatch1_8;
} else {
tomMatch1__end__15= tomMatch1__end__15.getTailConcSection() ;
}

}
} while(!( (tomMatch1__end__15==tomMatch1_8) ));
}
}
}
}
if ( tomMatch1__end__4.isEmptyConcGomModule() ) {
tomMatch1__end__4=(( tom.gom.adt.gom.types.GomModuleList )gomModuleList);
} else {
tomMatch1__end__4= tomMatch1__end__4.getTailConcGomModule() ;
}

}
} while(!( (tomMatch1__end__4==(( tom.gom.adt.gom.types.GomModuleList )gomModuleList)) ));
}
}

}

}

return hookList;
}

private HookDeclList addDefaultTheoryHooks(Alternative alt,
HookDeclList hookList,
ArrayList<String> examinedOps,
String moduleName) {

{
{
if ( (alt instanceof tom.gom.adt.gom.types.Alternative) ) {
if ( ((( tom.gom.adt.gom.types.Alternative )alt) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) {
 tom.gom.adt.gom.types.FieldList  tomMatch5_3= (( tom.gom.adt.gom.types.Alternative )alt).getDomainList() ;
 String  tom_opName= (( tom.gom.adt.gom.types.Alternative )alt).getName() ;
if ( ((tomMatch5_3 instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || (tomMatch5_3 instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {
if (!( tomMatch5_3.isEmptyConcField() )) {
 tom.gom.adt.gom.types.Field  tomMatch5_11= tomMatch5_3.getHeadConcField() ;
if ( (tomMatch5_11 instanceof tom.gom.adt.gom.types.field.StarredField) ) {
if (  tomMatch5_3.getTailConcField() .isEmptyConcField() ) {
if ( ( tomMatch5_11.getFieldType() == (( tom.gom.adt.gom.types.Alternative )alt).getCodomain() ) ) {
if ( (hookList instanceof tom.gom.adt.gom.types.HookDeclList) ) {
boolean tomMatch5_26= false ;
if ( (((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) {
 tom.gom.adt.gom.types.HookDeclList  tomMatch5__end__15=(( tom.gom.adt.gom.types.HookDeclList )hookList);
do {
{
if (!( tomMatch5__end__15.isEmptyConcHookDecl() )) {
 tom.gom.adt.gom.types.HookDecl  tomMatch5_20= tomMatch5__end__15.getHeadConcHookDecl() ;
if ( (tomMatch5_20 instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) {
 tom.gom.adt.gom.types.Decl  tomMatch5_19= tomMatch5_20.getPointcut() ;
if ( (tomMatch5_19 instanceof tom.gom.adt.gom.types.decl.CutOperator) ) {
 tom.gom.adt.gom.types.OperatorDecl  tomMatch5_21= tomMatch5_19.getODecl() ;
if ( (tomMatch5_21 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
if ( tom_opName.equals( tomMatch5_21.getName() ) ) {
tomMatch5_26= true ;
}
}
}
}
}
if ( tomMatch5__end__15.isEmptyConcHookDecl() ) {
tomMatch5__end__15=(( tom.gom.adt.gom.types.HookDeclList )hookList);
} else {
tomMatch5__end__15= tomMatch5__end__15.getTailConcHookDecl() ;
}

}
} while(!( (tomMatch5__end__15==(( tom.gom.adt.gom.types.HookDeclList )hookList)) ));
}
if (!(tomMatch5_26)) {

/* generate a FL hook for list-operators without other hook */
String emptyCode = "{}";
Production hook = 
 tom.gom.adt.gom.types.production.Hook.make( tom.gom.adt.gom.types.idkind.KindOperator.make() , tom_opName,  tom.gom.adt.gom.types.hookkind.HookKind.make("FL") ,  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() , emptyCode,  tom.gom.adt.gom.types.option.EmptyOptionList.make() ) ;
OperatorDecl odecl = getOperatorDecl(
tom_opName,
moduleName,moduleList);
if (odecl!=null) {
HookDeclList newDeclList = makeHookDeclList(
hook,
 tom.gom.adt.gom.types.decl.CutOperator.make(odecl) );
hookList = 
tom_append_list_ConcHookDecl(newDeclList,tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
}


}

}
}
}
}
}
}
}
}

}
{
if ( (alt instanceof tom.gom.adt.gom.types.Alternative) ) {
if ( ((( tom.gom.adt.gom.types.Alternative )alt) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) {
 tom.gom.adt.gom.types.FieldList  tomMatch5_30= (( tom.gom.adt.gom.types.Alternative )alt).getDomainList() ;
 String  tom_opName= (( tom.gom.adt.gom.types.Alternative )alt).getName() ;
if ( ((tomMatch5_30 instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || (tomMatch5_30 instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {
if (!( tomMatch5_30.isEmptyConcField() )) {
 tom.gom.adt.gom.types.Field  tomMatch5_44= tomMatch5_30.getHeadConcField() ;
if ( (tomMatch5_44 instanceof tom.gom.adt.gom.types.field.StarredField) ) {
if (  tomMatch5_30.getTailConcField() .isEmptyConcField() ) {
if ( ( tomMatch5_44.getFieldType() == (( tom.gom.adt.gom.types.Alternative )alt).getCodomain() ) ) {
if ( (hookList instanceof tom.gom.adt.gom.types.HookDeclList) ) {
if ( (((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) {
 tom.gom.adt.gom.types.HookDeclList  tomMatch5__end__39=(( tom.gom.adt.gom.types.HookDeclList )hookList);
do {
{
if (!( tomMatch5__end__39.isEmptyConcHookDecl() )) {
 tom.gom.adt.gom.types.HookDecl  tomMatch5_46= tomMatch5__end__39.getHeadConcHookDecl() ;
if ( (tomMatch5_46 instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) {
 tom.gom.adt.gom.types.HookKind  tomMatch5_45= tomMatch5_46.getHookType() ;
if ( (tomMatch5_45 instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
 String  tomMatch5_47= tomMatch5_45.getkind() ;
boolean tomMatch5_51= false ;
if ( "make_insert".equals(tomMatch5_47) ) {
tomMatch5_51= true ;
} else {
if ( "make_empty".equals(tomMatch5_47) ) {
tomMatch5_51= true ;
} else {
if ( "rules".equals(tomMatch5_47) ) {
tomMatch5_51= true ;
}
}
}
if (tomMatch5_51) {

if (!examinedOps.contains(
tom_opName)) {
examinedOps.add(
tom_opName);

{
{
if ( (hookList instanceof tom.gom.adt.gom.types.HookDeclList) ) {
if ( true ) {
boolean tomMatch6_19= false ;
if ( (((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )hookList) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) {
 tom.gom.adt.gom.types.HookDeclList  tomMatch6__end__5=(( tom.gom.adt.gom.types.HookDeclList )hookList);
do {
{
if (!( tomMatch6__end__5.isEmptyConcHookDecl() )) {
 tom.gom.adt.gom.types.HookDecl  tomMatch6_10= tomMatch6__end__5.getHeadConcHookDecl() ;
if ( (tomMatch6_10 instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) {
 tom.gom.adt.gom.types.Decl  tomMatch6_8= tomMatch6_10.getPointcut() ;
 tom.gom.adt.gom.types.HookKind  tomMatch6_9= tomMatch6_10.getHookType() ;
if ( (tomMatch6_8 instanceof tom.gom.adt.gom.types.decl.CutOperator) ) {
 tom.gom.adt.gom.types.OperatorDecl  tomMatch6_11= tomMatch6_8.getODecl() ;
if ( (tomMatch6_11 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
if ( (( String )tom_opName).equals( tomMatch6_11.getName() ) ) {
if ( (tomMatch6_9 instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
 String  tomMatch6_15= tomMatch6_9.getkind() ;
boolean tomMatch6_20= false ;
if ( "Free".equals(tomMatch6_15) ) {
tomMatch6_20= true ;
} else {
if ( "FL".equals(tomMatch6_15) ) {
tomMatch6_20= true ;
} else {
if ( "AU".equals(tomMatch6_15) ) {
tomMatch6_20= true ;
} else {
if ( "AC".equals(tomMatch6_15) ) {
tomMatch6_20= true ;
} else {
if ( "ACU".equals(tomMatch6_15) ) {
tomMatch6_20= true ;
}
}
}
}
}
if (tomMatch6_20) {
tomMatch6_19= true ;
}

}
}
}
}
}
}
if ( tomMatch6__end__5.isEmptyConcHookDecl() ) {
tomMatch6__end__5=(( tom.gom.adt.gom.types.HookDeclList )hookList);
} else {
tomMatch6__end__5= tomMatch6__end__5.getTailConcHookDecl() ;
}

}
} while(!( (tomMatch6__end__5==(( tom.gom.adt.gom.types.HookDeclList )hookList)) ));
}
if (!(tomMatch6_19)) {

/* generate an error to make users specify the theory */
GomMessage.error(getLogger(), null, 0, 
GomMessage.mustSpecifyAssociatedTheoryForVarOp,
tom_opName);


}

}
}

}

}

}


}

}
}
}
if ( tomMatch5__end__39.isEmptyConcHookDecl() ) {
tomMatch5__end__39=(( tom.gom.adt.gom.types.HookDeclList )hookList);
} else {
tomMatch5__end__39= tomMatch5__end__39.getTailConcHookDecl() ;
}

}
} while(!( (tomMatch5__end__39==(( tom.gom.adt.gom.types.HookDeclList )hookList)) ));
}
}
}
}
}
}
}
}
}

}


}

return hookList;
}

private HookDeclList makeHookDeclList(Production hook, Decl mdecl) {

{
{
if ( (hook instanceof tom.gom.adt.gom.types.Production) ) {
if ( ((( tom.gom.adt.gom.types.Production )hook) instanceof tom.gom.adt.gom.types.production.Hook) ) {
 tom.gom.adt.gom.types.HookKind  tom_hkind= (( tom.gom.adt.gom.types.Production )hook).getHookType() ;
 String  tom_hName= (( tom.gom.adt.gom.types.Production )hook).getName() ;
 tom.gom.adt.gom.types.ArgList  tom_hookArgs= (( tom.gom.adt.gom.types.Production )hook).getArgs() ;
 String  tom_scode= (( tom.gom.adt.gom.types.Production )hook).getStringCode() ;

HookDeclList newHookList = 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;

{
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "block".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

newHookList = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ,  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) ;


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "javablock".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

newHookList = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ,  false ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) ;


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "interface".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

newHookList = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.InterfaceHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) ;


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "import".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

newHookList = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.ImportHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) ;


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "mapping".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

newHookList = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MappingHookDecl.make(mdecl,  tom.gom.adt.code.types.code.Code.make(trimBracket(tom_scode)) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) ;


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
 String  tomMatch8_21= (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ;
boolean tomMatch8_24= false ;
if ( "make".equals(tomMatch8_21) ) {
tomMatch8_24= true ;
} else {
if ( "make_insert".equals(tomMatch8_21) ) {
tomMatch8_24= true ;
} else {
if ( "make_empty".equals(tomMatch8_21) ) {
tomMatch8_24= true ;
}
}
}
if (tomMatch8_24) {

SlotList typedArgs = typeArguments(
tom_hookArgs,
tom_hkind,
mdecl);
if (null == typedArgs) {
GomMessage.error(getLogger(),null,0,
GomMessage.discardedHook, 
(tom_hName));
return 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
}
newHookList = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl, typedArgs,  tom.gom.adt.code.types.code.Code.make(tom_scode) , (( tom.gom.adt.gom.types.HookKind )tom_hkind),  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) ;


}

}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "Free".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

/* Even there is no code associated, we generate a MakeHook to
* prevent FL hooks to be automatically generated */
return 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ,  tom.gom.adt.code.types.code.Code.make("") ,  tom.gom.adt.gom.types.hookkind.HookKind.make("Free") ,  false ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) ;


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "FL".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

/* FL: flattened list */
return 
makeFLHookList(tom_hName,mdecl,tom_scode);


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "AU".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

return 
makeAUHookList(tom_hName,mdecl,tom_scode);


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "AC".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

return 
makeACHookList(tom_hName,mdecl,tom_scode);


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "ACU".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

return 
makeACUHookList(tom_hName,mdecl,tom_scode);


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "rules".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

return 
makeRulesHookList(tom_hName,mdecl,tom_scode);


}
}
}

}
{
if ( (tom_hkind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )tom_hkind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "graphrules".equals( (( tom.gom.adt.gom.types.HookKind )tom_hkind).getkind() ) ) {

//TODO: verify if the option termgraph is on
if(
tom_hookArgs.length()!=2) {
throw new GomRuntimeException(
"GomTypeExpander:graphrules hooks need two parameters: the name of the generated strategy and its default behaviour");
}
return 
makeGraphRulesHookList(tom_hName,tom_hookArgs,mdecl,tom_scode);


}
}
}

}


}

if (newHookList == 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) {
GomMessage.error(getLogger(),null,0,
GomMessage.unknownHookKind, 
(tom_hkind));
}
return newHookList;


}
}

}

}

throw new GomRuntimeException("HookTypeExpander: this hook is not a hook: "+ 
hook);
}

/**
* Finds the ModuleDecl corresponding to a module name.
*
* @param mname the module name
* @param moduleList the queried ModuleList
* @return the ModuleDecl for mname
*/
private ModuleDecl getModuleDecl(String mname, ModuleList moduleList) {

{
{
if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {
if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) {
 tom.gom.adt.gom.types.ModuleList  tomMatch9__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
do {
{
if (!( tomMatch9__end__4.isEmptyConcModule() )) {
 tom.gom.adt.gom.types.Module  tomMatch9_8= tomMatch9__end__4.getHeadConcModule() ;
if ( (tomMatch9_8 instanceof tom.gom.adt.gom.types.module.Module) ) {
 tom.gom.adt.gom.types.ModuleDecl  tomMatch9_7= tomMatch9_8.getMDecl() ;
if ( (tomMatch9_7 instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) {
 tom.gom.adt.gom.types.GomModuleName  tomMatch9_9= tomMatch9_7.getModuleName() ;
if ( (tomMatch9_9 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {

if (
 tomMatch9_9.getName() .equals(mname)) {
return 
tomMatch9_7;
}


}
}
}
}
if ( tomMatch9__end__4.isEmptyConcModule() ) {
tomMatch9__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
} else {
tomMatch9__end__4= tomMatch9__end__4.getTailConcModule() ;
}

}
} while(!( (tomMatch9__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));
}
}

}

}

throw new GomRuntimeException(
"HookTypeExpander: Module not found: "+
mname);
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

{
{
if ( true ) {
if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {
if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) {
 tom.gom.adt.gom.types.ModuleList  tomMatch10__end__5=(( tom.gom.adt.gom.types.ModuleList )moduleList);
do {
{
if (!( tomMatch10__end__5.isEmptyConcModule() )) {
 tom.gom.adt.gom.types.Module  tomMatch10_10= tomMatch10__end__5.getHeadConcModule() ;
if ( (tomMatch10_10 instanceof tom.gom.adt.gom.types.module.Module) ) {
 tom.gom.adt.gom.types.ModuleDecl  tomMatch10_8= tomMatch10_10.getMDecl() ;
 tom.gom.adt.gom.types.SortList  tomMatch10_9= tomMatch10_10.getSorts() ;
if ( (tomMatch10_8 instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) {
 tom.gom.adt.gom.types.GomModuleName  tomMatch10_11= tomMatch10_8.getModuleName() ;
if ( (tomMatch10_11 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {
if ( (( String )modName).equals( tomMatch10_11.getName() ) ) {
if ( ((tomMatch10_9 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch10_9 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) {
 tom.gom.adt.gom.types.SortList  tomMatch10__end__18=tomMatch10_9;
do {
{
if (!( tomMatch10__end__18.isEmptyConcSort() )) {
 tom.gom.adt.gom.types.Sort  tomMatch10_23= tomMatch10__end__18.getHeadConcSort() ;
if ( (tomMatch10_23 instanceof tom.gom.adt.gom.types.sort.Sort) ) {
 tom.gom.adt.gom.types.SortDecl  tomMatch10_22= tomMatch10_23.getDecl() ;
if ( (tomMatch10_22 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

if (
 tomMatch10_22.getName() .equals(sname)) {
return 
tomMatch10_22;
}


}
}
}
if ( tomMatch10__end__18.isEmptyConcSort() ) {
tomMatch10__end__18=tomMatch10_9;
} else {
tomMatch10__end__18= tomMatch10__end__18.getTailConcSort() ;
}

}
} while(!( (tomMatch10__end__18==tomMatch10_9) ));
}
}
}
}
}
}
if ( tomMatch10__end__5.isEmptyConcModule() ) {
tomMatch10__end__5=(( tom.gom.adt.gom.types.ModuleList )moduleList);
} else {
tomMatch10__end__5= tomMatch10__end__5.getTailConcModule() ;
}

}
} while(!( (tomMatch10__end__5==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));
}
}
}

}

}

throw new GomRuntimeException(
"HookTypeExpander: Sort not found: "+
sname);
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

{
{
if ( true ) {
if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {
if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) {
 tom.gom.adt.gom.types.ModuleList  tomMatch11__end__5=(( tom.gom.adt.gom.types.ModuleList )moduleList);
do {
{
if (!( tomMatch11__end__5.isEmptyConcModule() )) {
 tom.gom.adt.gom.types.Module  tomMatch11_10= tomMatch11__end__5.getHeadConcModule() ;
if ( (tomMatch11_10 instanceof tom.gom.adt.gom.types.module.Module) ) {
 tom.gom.adt.gom.types.ModuleDecl  tomMatch11_8= tomMatch11_10.getMDecl() ;
 tom.gom.adt.gom.types.SortList  tomMatch11_9= tomMatch11_10.getSorts() ;
if ( (tomMatch11_8 instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) {
 tom.gom.adt.gom.types.GomModuleName  tomMatch11_11= tomMatch11_8.getModuleName() ;
if ( (tomMatch11_11 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {
if ( (( String )modName).equals( tomMatch11_11.getName() ) ) {
if ( ((tomMatch11_9 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch11_9 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) {
 tom.gom.adt.gom.types.SortList  tomMatch11__end__18=tomMatch11_9;
do {
{
if (!( tomMatch11__end__18.isEmptyConcSort() )) {
 tom.gom.adt.gom.types.Sort  tomMatch11_23= tomMatch11__end__18.getHeadConcSort() ;
if ( (tomMatch11_23 instanceof tom.gom.adt.gom.types.sort.Sort) ) {
 tom.gom.adt.gom.types.OperatorDeclList  tomMatch11_22= tomMatch11_23.getOperatorDecls() ;
if ( ((tomMatch11_22 instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || (tomMatch11_22 instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) {
 tom.gom.adt.gom.types.OperatorDeclList  tomMatch11__end__27=tomMatch11_22;
do {
{
if (!( tomMatch11__end__27.isEmptyConcOperator() )) {
 tom.gom.adt.gom.types.OperatorDecl  tomMatch11_31= tomMatch11__end__27.getHeadConcOperator() ;
if ( (tomMatch11_31 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {

if (
 tomMatch11_31.getName() .equals(oname)) {
return 
 tomMatch11__end__27.getHeadConcOperator() ;
}


}
}
if ( tomMatch11__end__27.isEmptyConcOperator() ) {
tomMatch11__end__27=tomMatch11_22;
} else {
tomMatch11__end__27= tomMatch11__end__27.getTailConcOperator() ;
}

}
} while(!( (tomMatch11__end__27==tomMatch11_22) ));
}
}
}
if ( tomMatch11__end__18.isEmptyConcSort() ) {
tomMatch11__end__18=tomMatch11_9;
} else {
tomMatch11__end__18= tomMatch11__end__18.getTailConcSort() ;
}

}
} while(!( (tomMatch11__end__18==tomMatch11_9) ));
}
}
}
}
}
}
if ( tomMatch11__end__5.isEmptyConcModule() ) {
tomMatch11__end__5=(( tom.gom.adt.gom.types.ModuleList )moduleList);
} else {
tomMatch11__end__5= tomMatch11__end__5.getTailConcModule() ;
}

}
} while(!( (tomMatch11__end__5==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));
}
}
}

}

}

GomMessage.error(getLogger(),null,0,
GomMessage.orphanedHook, oname);
return null;
}

private SlotList typeArguments(
ArgList args,
HookKind kind,
Decl decl) {

{
{
if ( (kind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )kind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
if ( "make".equals( (( tom.gom.adt.gom.types.HookKind )kind).getkind() ) ) {

/*
* The TypedProduction has to be Slots
* A KindMakeHook is attached to an operator
*/

{
{
if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {
if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) {
 tom.gom.adt.gom.types.OperatorDecl  tomMatch13_1= (( tom.gom.adt.gom.types.Decl )decl).getODecl() ;
if ( (tomMatch13_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
 tom.gom.adt.gom.types.TypedProduction  tomMatch13_3= tomMatch13_1.getProd() ;
if ( (tomMatch13_3 instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) {
 tom.gom.adt.gom.types.SlotList  tom_slotList= tomMatch13_3.getSlots() ;

/* tests the arguments number */
if (args.length() != 
tom_slotList.length()) {
SlotList slist = 
tom_slotList;
GomMessage.error(getLogger(), null, 0,
GomMessage.mismatchedMakeArguments, args, slist);
return null;
}
/* Then check the types */
return recArgSlots(args,
tom_slotList);


}
}
}
}

}
{
if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {

GomMessage.error(getLogger(), null, 0,
GomMessage.unsupportedHookAlgebraic, kind);
return null;

}

}


}



}
}
}

}
{
if ( (kind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )kind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
 String  tomMatch12_5= (( tom.gom.adt.gom.types.HookKind )kind).getkind() ;
if ( "make_insert".equals(tomMatch12_5) ) {

/*
* The TypedProduction has to be Variadic
* Then we get the codomain from the operatordecl
*/

{
{
if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {
if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) {
 tom.gom.adt.gom.types.OperatorDecl  tomMatch14_1= (( tom.gom.adt.gom.types.Decl )decl).getODecl() ;
if ( (tomMatch14_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
 tom.gom.adt.gom.types.TypedProduction  tomMatch14_4= tomMatch14_1.getProd() ;
if ( (tomMatch14_4 instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {

// for a make_insert hook, there are two arguments: head, tail

{
{
if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {
if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {
if (!( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() )) {
 tom.gom.adt.gom.types.Arg  tomMatch15_5= (( tom.gom.adt.gom.types.ArgList )args).getHeadConcArg() ;
if ( (tomMatch15_5 instanceof tom.gom.adt.gom.types.arg.Arg) ) {
 tom.gom.adt.gom.types.ArgList  tomMatch15_2= (( tom.gom.adt.gom.types.ArgList )args).getTailConcArg() ;
if (!( tomMatch15_2.isEmptyConcArg() )) {
 tom.gom.adt.gom.types.Arg  tomMatch15_7= tomMatch15_2.getHeadConcArg() ;
if ( (tomMatch15_7 instanceof tom.gom.adt.gom.types.arg.Arg) ) {
if (  tomMatch15_2.getTailConcArg() .isEmptyConcArg() ) {

return 
 tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch15_5.getName() ,  tomMatch14_4.getSort() ) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch15_7.getName() ,  tomMatch14_1.getSort() ) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ;


}
}
}
}
}
}
}

}
{
if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {

GomMessage.error(getLogger(), null, 0,
GomMessage.badHookArguments, 

(tomMatch12_5), Integer.valueOf(args.length()));
return null;


}

}


}



}
}
}
}

}
{
if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {

GomMessage.error(getLogger(),null, 0,
GomMessage.unsupportedHookVariadic, kind);
return null;

}

}


}



}
}
}

}
{
if ( (kind instanceof tom.gom.adt.gom.types.HookKind) ) {
if ( ((( tom.gom.adt.gom.types.HookKind )kind) instanceof tom.gom.adt.gom.types.hookkind.HookKind) ) {
 String  tomMatch12_9= (( tom.gom.adt.gom.types.HookKind )kind).getkind() ;
if ( "make_empty".equals(tomMatch12_9) ) {

/*
* The TypedProduction has to be Variadic
* Then we get the codomain from the operatordecl
*/

{
{
if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {
if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) {
 tom.gom.adt.gom.types.OperatorDecl  tomMatch16_1= (( tom.gom.adt.gom.types.Decl )decl).getODecl() ;
if ( (tomMatch16_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
if ( ( tomMatch16_1.getProd()  instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {

// for a make_empty hook, there is no argument

{
{
if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {
if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {
if ( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() ) {
return 
 tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ; 

}
}
}

}
{
if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {

GomMessage.error(getLogger(), null, 0,
GomMessage.badHookArguments,

(tomMatch12_9), Integer.valueOf(args.length()));
return null;


}

}


}



}
}
}
}

}
{
if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {

GomMessage.error(getLogger(), null, 0,
GomMessage.unsupportedHookVariadic, kind);
return null;

}

}


}



}
}
}

}


}

throw new GomRuntimeException("Hook kind \""+kind+"\" not supported");
}

private SlotList recArgSlots(ArgList args, SlotList slots) {

{
{
if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {
if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {
if ( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() ) {
if ( (slots instanceof tom.gom.adt.gom.types.SlotList) ) {
if ( (((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {
if ( (( tom.gom.adt.gom.types.SlotList )slots).isEmptyConcSlot() ) {

return 
 tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;


}
}
}
}
}
}

}
{
if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {
if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {
if (!( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() )) {
 tom.gom.adt.gom.types.Arg  tomMatch18_13= (( tom.gom.adt.gom.types.ArgList )args).getHeadConcArg() ;
if ( (tomMatch18_13 instanceof tom.gom.adt.gom.types.arg.Arg) ) {
if ( (slots instanceof tom.gom.adt.gom.types.SlotList) ) {
if ( (((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {
if (!( (( tom.gom.adt.gom.types.SlotList )slots).isEmptyConcSlot() )) {
 tom.gom.adt.gom.types.Slot  tomMatch18_15= (( tom.gom.adt.gom.types.SlotList )slots).getHeadConcSlot() ;
if ( (tomMatch18_15 instanceof tom.gom.adt.gom.types.slot.Slot) ) {

SlotList tail = recArgSlots(
 (( tom.gom.adt.gom.types.ArgList )args).getTailConcArg() ,
 (( tom.gom.adt.gom.types.SlotList )slots).getTailConcSlot() );
return 
 tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch18_13.getName() ,  tomMatch18_15.getSort() ) ,tom_append_list_ConcSlot(tail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;


}
}
}
}
}
}
}
}

}


}

throw new GomRuntimeException("GomTypeExpander:recArgSlots failed "+args+" "+slots);
}

private String trimBracket(String stringCode) {
int start = 
stringCode.indexOf('{')+1;
int end = 
stringCode.lastIndexOf('}');
return 
stringCode.substring(start,end).trim();
}

private SortDecl getSortAndCheck(Decl mdecl) {

{
{
if ( (mdecl instanceof tom.gom.adt.gom.types.Decl) ) {
if ( ((( tom.gom.adt.gom.types.Decl )mdecl) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) {
 tom.gom.adt.gom.types.OperatorDecl  tomMatch19_1= (( tom.gom.adt.gom.types.Decl )mdecl).getODecl() ;
if ( (tomMatch19_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
 tom.gom.adt.gom.types.TypedProduction  tomMatch19_4= tomMatch19_1.getProd() ;
if ( (tomMatch19_4 instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {
 tom.gom.adt.gom.types.SortDecl  tom_sdecl= tomMatch19_4.getSort() ;

if (
 tomMatch19_1.getSort() == 
tom_sdecl) {
return 
tom_sdecl;
} else {
GomMessage.error(getLogger(), null, 0, GomMessage.differentDomainCodomain);
}


}
}
}
}

}
{
if ( (mdecl instanceof tom.gom.adt.gom.types.Decl) ) {

GomMessage.error(getLogger(), null, 0, GomMessage.hookFLAUACACUOnlyOnVarOp);

}

}


}

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

{
{
if ( (args instanceof tom.gom.adt.gom.types.ArgList) ) {
if ( (((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.ConsConcArg) || ((( tom.gom.adt.gom.types.ArgList )args) instanceof tom.gom.adt.gom.types.arglist.EmptyConcArg)) ) {
if (!( (( tom.gom.adt.gom.types.ArgList )args).isEmptyConcArg() )) {
 tom.gom.adt.gom.types.Arg  tomMatch20_5= (( tom.gom.adt.gom.types.ArgList )args).getHeadConcArg() ;
if ( (tomMatch20_5 instanceof tom.gom.adt.gom.types.arg.Arg) ) {
 String  tom_stratname= tomMatch20_5.getName() ;
 tom.gom.adt.gom.types.ArgList  tomMatch20_2= (( tom.gom.adt.gom.types.ArgList )args).getTailConcArg() ;
if (!( tomMatch20_2.isEmptyConcArg() )) {
 tom.gom.adt.gom.types.Arg  tomMatch20_7= tomMatch20_2.getHeadConcArg() ;
if ( (tomMatch20_7 instanceof tom.gom.adt.gom.types.arg.Arg) ) {
 String  tom_defaultstrat= tomMatch20_7.getName() ;
if (  tomMatch20_2.getTailConcArg() .isEmptyConcArg() ) {

if (!
tom_defaultstrat.equals("Fail") && !
tom_defaultstrat.equals("Identity")) {
GomMessage.error(getLogger(), null, 0, GomMessage.graphrulHookAuthorizedStrat);
}
GraphRuleExpander rexpander = new GraphRuleExpander(moduleList,getGomEnvironment());
if (sortsWithGraphrules.contains(sdecl)) {
return rexpander.expandGraphRules(sortname,
tom_stratname,
tom_defaultstrat,trimBracket(scode),sdecl);
} else {
sortsWithGraphrules.add(sdecl);
return rexpander.expandFirstGraphRules(sortname,
tom_stratname,
tom_defaultstrat,trimBracket(scode),sdecl);
}


}
}
}
}
}
}
}

}

}

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
return 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
}

HookDeclList acHooks = 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
/*
* Remove neutral and flatten:
* if(<head>.isEmpty<Conc>()) { return <tail>; }
* if(<head>.isCons<Conc>()) { return make(head.head,make(head.tail,tail)); }
* if(!<tail>.isCons<Conc>() && !<tail>.isEmpty<Conc>()) { return readMake(<tail>,<empty>); }
*/
acHooks = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return tail; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head.getHead" + opName+ "(),make(head.getTail" + opName+ "(),tail)); }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (!") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" && !") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head,realMake(tail,Empty" + opName+ ".make())); }\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("FL") ,  false ) ,tom_append_list_ConcHookDecl(acHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) ;
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
acHooks = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  if (0 < ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Compare.make( tom.gom.adt.code.types.code.Code.make("head") ,  tom.gom.adt.code.types.code.Code.make("tail.getHead" + opName+ "()") ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(domain) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" tmpHd = head;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    head = tail.getHead" + opName+ "();\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    tail = `"+opName+"(tmpHd,tail.getTail" + opName+ "());\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("}\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("AC") ,  true ) ,tom_append_list_ConcHookDecl(acHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) ;
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
return 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
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
acuHooks = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  if (0 < ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Compare.make( tom.gom.adt.code.types.code.Code.make("head") ,  tom.gom.adt.code.types.code.Code.make("tail.getHead" + opName+ "()") ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(domain) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" tmpHd = head;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    head = tail.getHead" + opName+ "();\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    tail = `"+opName+"(tmpHd,tail.getTail" + opName+ "());\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("} else {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  if (0 < ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Compare.make( tom.gom.adt.code.types.code.Code.make("head") ,  tom.gom.adt.code.types.code.Code.make("tail") ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(domain) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" tmpHd = head;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    head = tail;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("    tail = tmpHd;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("  }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("}\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("ACU") ,  true ) ,tom_append_list_ConcHookDecl(acuHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) ;
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
return 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
}

HookDeclList auHooks = 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
String userNeutral = trimBracket(scode);
if(userNeutral.length() > 0) {
/* The hook body is the name of the neutral element */
auHooks = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ,  tom.gom.adt.code.types.code.Code.make("return "+userNeutral+";") ,  tom.gom.adt.gom.types.hookkind.HookKind.make("AU") ,  true ) ,tom_append_list_ConcHookDecl(auHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) ;
/*
* Remove neutral:
* if(<head> == makeNeutral) { return <tail>; }
* if(<tail> == makeNeutral) { return <head>; }
*/
auHooks = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (head == "+userNeutral+") { return tail; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (tail ==  "+userNeutral+") { return head; }\n") 
              , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("AU") ,  true ) ,tom_append_list_ConcHookDecl(auHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) ;
}
/* getODecl call is safe here, since mdecl was checked by getSortAndCheck */
/*
* Remove neutral and flatten:
* if(<head>.isEmpty<conc>()) { return <tail>; }
* if(<tail>.isEmpty<conc>()) { return <head>; }
* if(<head>.isCons<conc>()) { return make(head.head,make(head.tail,tail)); }
*/
auHooks = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return tail; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return head; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head.getHead" + opName+ "(),make(head.getTail" + opName+ "(),tail)); }\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("AU") ,  false ) ,tom_append_list_ConcHookDecl(auHooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) ;

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
return 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
}

String userNeutral = trimBracket(scode);
if(userNeutral.length() > 0) {
GomMessage.error(getLogger(), null, 0, GomMessage.neutralElmtDefNotAllowed);
}

HookDeclList hooks = 
 tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
/* getODecl call is safe here, since mdecl was checked by getSortAndCheck */
/*
* Remove neutral and flatten:
* if(<head>.isEmpty<Conc>()) { return <tail>; }
* if(<head>.isCons<Conc>()) { return make(head.head,make(head.tail,tail)); }
* if(!<tail>.isCons<Conc>() && !<tail>.isEmpty<Conc>()) { return make(<tail>,<empty>); }
*/
hooks = 
 tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make(mdecl,  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head", domain) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", domain) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ,  tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return tail; }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("head", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head.getHead" + opName+ "(),make(head.getTail" + opName+ "(),tail)); }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("if (!") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsCons.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" && !") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.IsEmpty.make("tail", mdecl.getODecl()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") { return make(head,make(tail,Empty" + opName+ ".make())); }\n") 
            , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("FL") ,  false ) ,tom_append_list_ConcHookDecl(hooks, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() )) ;

return hooks;
}

private Logger getLogger() {
return Logger.getLogger(getClass().getName());
}
}
