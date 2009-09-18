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
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class TypeExpander {

         private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {     if( l1.isEmptyConcModule() ) {       return l2;     } else if( l2.isEmptyConcModule() ) {       return l1;     } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ImportList  tom_append_list_ConcImportedModule( tom.gom.adt.gom.types.ImportList l1,  tom.gom.adt.gom.types.ImportList  l2) {     if( l1.isEmptyConcImportedModule() ) {       return l2;     } else if( l2.isEmptyConcImportedModule() ) {       return l1;     } else if(  l1.getTailConcImportedModule() .isEmptyConcImportedModule() ) {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,tom_append_list_ConcImportedModule( l1.getTailConcImportedModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ImportList  tom_get_slice_ConcImportedModule( tom.gom.adt.gom.types.ImportList  begin,  tom.gom.adt.gom.types.ImportList  end, tom.gom.adt.gom.types.ImportList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcImportedModule()  ||  (end== tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( begin.getHeadConcImportedModule() ,( tom.gom.adt.gom.types.ImportList )tom_get_slice_ConcImportedModule( begin.getTailConcImportedModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GomTypeList  tom_append_list_ConcGomType( tom.gom.adt.gom.types.GomTypeList l1,  tom.gom.adt.gom.types.GomTypeList  l2) {     if( l1.isEmptyConcGomType() ) {       return l2;     } else if( l2.isEmptyConcGomType() ) {       return l1;     } else if(  l1.getTailConcGomType() .isEmptyConcGomType() ) {       return  tom.gom.adt.gom.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,tom_append_list_ConcGomType( l1.getTailConcGomType() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomTypeList  tom_get_slice_ConcGomType( tom.gom.adt.gom.types.GomTypeList  begin,  tom.gom.adt.gom.types.GomTypeList  end, tom.gom.adt.gom.types.GomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomType()  ||  (end== tom.gom.adt.gom.types.gomtypelist.EmptyConcGomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gomtypelist.ConsConcGomType.make( begin.getHeadConcGomType() ,( tom.gom.adt.gom.types.GomTypeList )tom_get_slice_ConcGomType( begin.getTailConcGomType() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortDeclList  tom_append_list_ConcSortDecl( tom.gom.adt.gom.types.SortDeclList l1,  tom.gom.adt.gom.types.SortDeclList  l2) {     if( l1.isEmptyConcSortDecl() ) {       return l2;     } else if( l2.isEmptyConcSortDecl() ) {       return l1;     } else if(  l1.getTailConcSortDecl() .isEmptyConcSortDecl() ) {       return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( l1.getHeadConcSortDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( l1.getHeadConcSortDecl() ,tom_append_list_ConcSortDecl( l1.getTailConcSortDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortDeclList  tom_get_slice_ConcSortDecl( tom.gom.adt.gom.types.SortDeclList  begin,  tom.gom.adt.gom.types.SortDeclList  end, tom.gom.adt.gom.types.SortDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSortDecl()  ||  (end== tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( begin.getHeadConcSortDecl() ,( tom.gom.adt.gom.types.SortDeclList )tom_get_slice_ConcSortDecl( begin.getTailConcSortDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleDeclList  tom_append_list_ConcModuleDecl( tom.gom.adt.gom.types.ModuleDeclList l1,  tom.gom.adt.gom.types.ModuleDeclList  l2) {     if( l1.isEmptyConcModuleDecl() ) {       return l2;     } else if( l2.isEmptyConcModuleDecl() ) {       return l1;     } else if(  l1.getTailConcModuleDecl() .isEmptyConcModuleDecl() ) {       return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( l1.getHeadConcModuleDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( l1.getHeadConcModuleDecl() ,tom_append_list_ConcModuleDecl( l1.getTailConcModuleDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleDeclList  tom_get_slice_ConcModuleDecl( tom.gom.adt.gom.types.ModuleDeclList  begin,  tom.gom.adt.gom.types.ModuleDeclList  end, tom.gom.adt.gom.types.ModuleDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModuleDecl()  ||  (end== tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( begin.getHeadConcModuleDecl() ,( tom.gom.adt.gom.types.ModuleDeclList )tom_get_slice_ConcModuleDecl( begin.getTailConcModuleDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.OperatorDeclList  tom_append_list_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList l1,  tom.gom.adt.gom.types.OperatorDeclList  l2) {     if( l1.isEmptyConcOperator() ) {       return l2;     } else if( l2.isEmptyConcOperator() ) {       return l1;     } else if(  l1.getTailConcOperator() .isEmptyConcOperator() ) {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,l2) ;     } else {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,tom_append_list_ConcOperator( l1.getTailConcOperator() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.OperatorDeclList  tom_get_slice_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList  begin,  tom.gom.adt.gom.types.OperatorDeclList  end, tom.gom.adt.gom.types.OperatorDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcOperator()  ||  (end== tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( begin.getHeadConcOperator() ,( tom.gom.adt.gom.types.OperatorDeclList )tom_get_slice_ConcOperator( begin.getTailConcOperator() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GrammarList  tom_append_list_ConcGrammar( tom.gom.adt.gom.types.GrammarList l1,  tom.gom.adt.gom.types.GrammarList  l2) {     if( l1.isEmptyConcGrammar() ) {       return l2;     } else if( l2.isEmptyConcGrammar() ) {       return l1;     } else if(  l1.getTailConcGrammar() .isEmptyConcGrammar() ) {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,l2) ;     } else {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,tom_append_list_ConcGrammar( l1.getTailConcGrammar() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GrammarList  tom_get_slice_ConcGrammar( tom.gom.adt.gom.types.GrammarList  begin,  tom.gom.adt.gom.types.GrammarList  end, tom.gom.adt.gom.types.GrammarList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGrammar()  ||  (end== tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( begin.getHeadConcGrammar() ,( tom.gom.adt.gom.types.GrammarList )tom_get_slice_ConcGrammar( begin.getTailConcGrammar() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {     if( l1.isEmptyConcSort() ) {       return l2;     } else if( l2.isEmptyConcSort() ) {       return l1;     } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;   }    

  private GomEnvironment gomEnvironment;

  public TypeExpander(GomStreamManager streamManager) {
    this.gomEnvironment.setStreamManager(streamManager);
  }
  
  public TypeExpander(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public TypeExpander(GomStreamManager streamManager, GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public GomStreamManager getStreamManager() {
    return this.gomEnvironment.getStreamManager();
  }

  /**
    * We try here to get full sort definitions for each constructs
    * Once the structure is correctly build, we can attach the hooks
    */
  public ModuleList expand(GomModuleList moduleList) {

    /* put a map giving all imported modules for each module in the path */
    buildDependencyMap(moduleList);

    /* collect all sort declarations */
    SortDeclList sortDeclList =  tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() ;
    /* The sorts declared in each module */
    Map<ModuleDecl,SortDeclList> sortsForModule =
      new HashMap<ModuleDecl,SortDeclList>();
    GomModuleList consum = moduleList;
    while(!consum.isEmptyConcGomModule()) {
      GomModule module = consum.getHeadConcGomModule();
      consum = consum.getTailConcGomModule();

      Collection<SortDecl> decls = getSortDeclarations(module);

      Collection<SortDecl> implicitdecls = getSortDeclarationInCodomain(module);

      /* Check that there are no implicit sort declarations
       * Also, check that declared sorts have at least an operator
       */
      if(!decls.containsAll(implicitdecls)) {
        // whine about non declared sorts
        Collection<SortDecl> undeclaredSorts = new HashSet<SortDecl>();
        undeclaredSorts.addAll(implicitdecls);
        undeclaredSorts.removeAll(decls);
        getLogger().log(Level.WARNING, GomMessage.undeclaredSorts.getMessage(),
            new Object[]{showSortList(undeclaredSorts)});
      }
      if(!implicitdecls.containsAll(decls)) {
        // whine about sorts without operators: this is a real error
        Collection<SortDecl> emptySorts = new HashSet<SortDecl>();
        emptySorts.addAll(decls);
        emptySorts.removeAll(implicitdecls);
        getLogger().log(Level.SEVERE, GomMessage.emptySorts.getMessage(),
            new Object[]{showSortList(emptySorts)});
        return  tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ;
      }
      for (SortDecl decl : implicitdecls) {
        sortDeclList =  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make(decl,tom_append_list_ConcSortDecl(sortDeclList, tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() )) ;
      }
      /* Fills sortsForModule */
      SortDeclList declaredSorts =  tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() ;
      for (SortDecl decl : decls) {
        declaredSorts =  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make(decl,tom_append_list_ConcSortDecl(declaredSorts, tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() )) ;
      }
      GomModuleName moduleName = module.getModuleName();
      ModuleDecl mdecl =  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(moduleName, getStreamManager().getPackagePath(moduleName.getName())) ;
      sortsForModule.put(mdecl,declaredSorts);
    }

    /* now get all operators for each sort */
    Map<SortDecl,OperatorDeclList> operatorsForSort =
      new HashMap<SortDecl,OperatorDeclList>();
    Map hooksForSort = new HashMap();
    consum = moduleList;
    while(!consum.isEmptyConcGomModule()) {
      GomModule module = consum.getHeadConcGomModule();
      consum = consum.getTailConcGomModule();

      // iterate through the productions
      {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch488_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;if ( ((tomMatch488_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch488_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch488__end__7=tomMatch488_2;do {{if (!( tomMatch488__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch488_11= tomMatch488__end__7.getHeadConcSection() ;if ( (tomMatch488_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch488_10= tomMatch488_11.getGrammarList() ;if ( ((tomMatch488_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch488_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch488__end__15=tomMatch488_10;do {{if (!( tomMatch488__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch488_19= tomMatch488__end__15.getHeadConcGrammar() ;if ( (tomMatch488_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch488_18= tomMatch488_19.getProductionList() ;if ( ((tomMatch488_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch488_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch488__end__23=tomMatch488_18;do {{if (!( tomMatch488__end__23.isEmptyConcProduction() )) {if ( ( tomMatch488__end__23.getHeadConcProduction()  instanceof tom.gom.adt.gom.types.production.Production) ) {



          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl( tomMatch488__end__23.getHeadConcProduction() ,sortDeclList,operatorsForSort);

        }}if ( tomMatch488__end__23.isEmptyConcProduction() ) {tomMatch488__end__23=tomMatch488_18;} else {tomMatch488__end__23= tomMatch488__end__23.getTailConcProduction() ;}}} while(!( (tomMatch488__end__23==tomMatch488_18) ));}}}if ( tomMatch488__end__15.isEmptyConcGrammar() ) {tomMatch488__end__15=tomMatch488_10;} else {tomMatch488__end__15= tomMatch488__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch488__end__15==tomMatch488_10) ));}}}if ( tomMatch488__end__7.isEmptyConcSection() ) {tomMatch488__end__7=tomMatch488_2;} else {tomMatch488__end__7= tomMatch488__end__7.getTailConcSection() ;}}} while(!( (tomMatch488__end__7==tomMatch488_2) ));}}}}}{{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch489_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;if ( ((tomMatch489_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch489_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch489__end__7=tomMatch489_2;do {{if (!( tomMatch489__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch489_11= tomMatch489__end__7.getHeadConcSection() ;if ( (tomMatch489_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch489_10= tomMatch489_11.getGrammarList() ;if ( ((tomMatch489_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch489_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch489__end__15=tomMatch489_10;do {{if (!( tomMatch489__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch489_19= tomMatch489__end__15.getHeadConcGrammar() ;if ( (tomMatch489_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch489_18= tomMatch489_19.getProductionList() ;if ( ((tomMatch489_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch489_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch489__end__23=tomMatch489_18;do {{if (!( tomMatch489__end__23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch489_27= tomMatch489__end__23.getHeadConcProduction() ;if ( (tomMatch489_27 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.ProductionList  tomMatch489_26= tomMatch489_27.getProductionList() ;if ( ((tomMatch489_26 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch489_26 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch489__end__31=tomMatch489_26;do {{if (!( tomMatch489__end__31.isEmptyConcProduction() )) {if ( ( tomMatch489__end__31.getHeadConcProduction()  instanceof tom.gom.adt.gom.types.production.Production) ) {








          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl( tomMatch489__end__31.getHeadConcProduction() ,sortDeclList,operatorsForSort);

        }}if ( tomMatch489__end__31.isEmptyConcProduction() ) {tomMatch489__end__31=tomMatch489_26;} else {tomMatch489__end__31= tomMatch489__end__31.getTailConcProduction() ;}}} while(!( (tomMatch489__end__31==tomMatch489_26) ));}}}if ( tomMatch489__end__23.isEmptyConcProduction() ) {tomMatch489__end__23=tomMatch489_18;} else {tomMatch489__end__23= tomMatch489__end__23.getTailConcProduction() ;}}} while(!( (tomMatch489__end__23==tomMatch489_18) ));}}}if ( tomMatch489__end__15.isEmptyConcGrammar() ) {tomMatch489__end__15=tomMatch489_10;} else {tomMatch489__end__15= tomMatch489__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch489__end__15==tomMatch489_10) ));}}}if ( tomMatch489__end__7.isEmptyConcSection() ) {tomMatch489__end__7=tomMatch489_2;} else {tomMatch489__end__7= tomMatch489__end__7.getTailConcSection() ;}}} while(!( (tomMatch489__end__7==tomMatch489_2) ));}}}}}

    }

    /*
     * build the module list using the map
     * since we already checked that the declared and used sorts do match, we
     * can use the map alone
     */
    ModuleList resultModuleList =  tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ;
    for (Map.Entry<ModuleDecl,SortDeclList> entry : sortsForModule.entrySet()) {
      ModuleDecl mdecl = entry.getKey();
      SortDeclList sdeclList = entry.getValue();
      SortList sortList =  tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ;
      {{if ( (sdeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch490__end__4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);do {{if (!( tomMatch490__end__4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tom_sdecl= tomMatch490__end__4.getHeadConcSortDecl() ;

          OperatorDeclList opdecl = operatorsForSort.get(tom_sdecl);
          Sort fullSort =  tom.gom.adt.gom.types.sort.Sort.make(tom_sdecl, opdecl) ;
          if(checkSortValidity(fullSort)) {
            sortList =  tom.gom.adt.gom.types.sortlist.ConsConcSort.make(fullSort,tom_append_list_ConcSort(sortList, tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() )) ;
          }
        }if ( tomMatch490__end__4.isEmptyConcSortDecl() ) {tomMatch490__end__4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);} else {tomMatch490__end__4= tomMatch490__end__4.getTailConcSortDecl() ;}}} while(!( (tomMatch490__end__4==(( tom.gom.adt.gom.types.SortDeclList )sdeclList)) ));}}}}

      resultModuleList =  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( tom.gom.adt.gom.types.module.Module.make(mdecl, sortList) ,tom_append_list_ConcModule(resultModuleList, tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() )) 

;
    }
    return resultModuleList;
  }

  /*
   * Get an OperatorDecl from a Production, using the list of sort declarations
   * XXX: There is huge room for efficiency improvement, as we could use a map
   * sortName -> sortDeclList instead of a simple list
   */
  private OperatorDecl getOperatorDecl(Production prod,
      SortDeclList sortDeclList,
      Map<SortDecl,OperatorDeclList> operatorsForSort) {

    {{if ( (prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )prod) instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.GomType  tomMatch491_3= (( tom.gom.adt.gom.types.Production )prod).getCodomain() ; String  tom_name= (( tom.gom.adt.gom.types.Production )prod).getName() ;if ( (tomMatch491_3 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { tom.gom.adt.gom.types.Option  tom_options= (( tom.gom.adt.gom.types.Production )prod).getOption() ;



        SortDecl codomainSort = declFromTypename( tomMatch491_3.getName() ,sortDeclList);
        TypedProduction domainSorts = typedProduction( (( tom.gom.adt.gom.types.Production )prod).getDomainList() ,sortDeclList);
///
        OperatorDecl decl =  tom.gom.adt.gom.types.operatordecl.OperatorDecl.make(tom_name, codomainSort, domainSorts,  tom.gom.adt.gom.types.option.Details.make("") ) ; //default case, when no comment is present
        if (tom_options.isConsOptionList()) { // usual case : 
          Object[] opts = ((tom.gom.adt.gom.types.option.OptionList)tom_options).toArray();
          for (int i=0;i<tom_options.length();i++) {
            if (opts[i] instanceof tom.gom.adt.gom.types.option.Details) {
              decl =  tom.gom.adt.gom.types.operatordecl.OperatorDecl.make(tom_name, codomainSort, domainSorts, (tom.gom.adt.gom.types.option.Details)opts[i]) ;
              break;
            }
          }
        } else if (tom_options.isDetails()) { // just in case, but for moment, it shouldn't be possible to have it
          decl =  tom.gom.adt.gom.types.operatordecl.OperatorDecl.make(tom_name, codomainSort, domainSorts, tom_options) ;
        }
///
        if (operatorsForSort.containsKey(codomainSort)) {
          OperatorDeclList list = operatorsForSort.get(codomainSort);
          operatorsForSort.put(codomainSort, tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make(decl,tom_append_list_ConcOperator(list, tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() )) );
        } else {
          operatorsForSort.put(codomainSort, tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make(decl, tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) );
        }
        return decl;
      }}}}}


    throw new GomRuntimeException(
        "TypeExpander::getOperatorDecl: wrong Production?");
  }

  private SortDecl declFromTypename(String typename,
                                    SortDeclList sortDeclList) {
    if (getGomEnvironment().isBuiltinSort(typename)) {
      return getGomEnvironment().builtinSort(typename);
    }
    {{if ( (sortDeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch492__end__4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);do {{if (!( tomMatch492__end__4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tomMatch492_8= tomMatch492__end__4.getHeadConcSortDecl() ;if ( (tomMatch492_8 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        if (typename.equals( tomMatch492_8.getName() )) {
          return  tomMatch492__end__4.getHeadConcSortDecl() ;
        }
      }}if ( tomMatch492__end__4.isEmptyConcSortDecl() ) {tomMatch492__end__4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);} else {tomMatch492__end__4= tomMatch492__end__4.getTailConcSortDecl() ;}}} while(!( (tomMatch492__end__4==(( tom.gom.adt.gom.types.SortDeclList )sortDeclList)) ));}}}}

    
    getLogger().log(Level.SEVERE, GomMessage.unknownSort.getMessage(),
        new Object[]{typename});
    /* If the sort is not known, assume it is a builtin */
    return  tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl.make(typename) ;
  }

  private TypedProduction typedProduction(FieldList domain, SortDeclList sortDeclList) {
    {{if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )domain).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch493_5= (( tom.gom.adt.gom.types.FieldList )domain).getHeadConcField() ;if ( (tomMatch493_5 instanceof tom.gom.adt.gom.types.field.StarredField) ) { tom.gom.adt.gom.types.GomType  tomMatch493_3= tomMatch493_5.getFieldType() ;if ( (tomMatch493_3 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {if (  (( tom.gom.adt.gom.types.FieldList )domain).getTailConcField() .isEmptyConcField() ) {

        return  tom.gom.adt.gom.types.typedproduction.Variadic.make(declFromTypename( tomMatch493_3.getName() ,sortDeclList)) ;
      }}}}}}}{if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {

        return  tom.gom.adt.gom.types.typedproduction.Slots.make(typedSlotList((( tom.gom.adt.gom.types.FieldList )domain),sortDeclList)) ;
      }}}}

    // the error message could be more refined
    throw new GomRuntimeException("TypeExpander::typedProduction: illformed Production");
  }

  private SlotList typedSlotList(FieldList fields, SortDeclList sortDeclList) {
    {{if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if ( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() ) {

        return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
      }}}}{if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch494_9= (( tom.gom.adt.gom.types.FieldList )fields).getHeadConcField() ;if ( (tomMatch494_9 instanceof tom.gom.adt.gom.types.field.NamedField) ) { tom.gom.adt.gom.types.GomType  tomMatch494_8= tomMatch494_9.getFieldType() ;if ( (tomMatch494_8 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {

        SlotList newtail = typedSlotList( (( tom.gom.adt.gom.types.FieldList )fields).getTailConcField() ,sortDeclList);
        return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch494_9.getName() , declFromTypename( tomMatch494_8.getName() ,sortDeclList)) ,tom_append_list_ConcSlot(newtail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;
      }}}}}}}

    getLogger().log(Level.SEVERE, GomMessage.malformedProduction.getMessage(),
        new Object[]{fields.toString()});
    return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
  }

  /*
   * Get all sort declarations in a module
   */
  private Collection<SortDecl> getSortDeclarations(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch495_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch495_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch495_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch495__end__7=tomMatch495_2;do {{if (!( tomMatch495__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch495_11= tomMatch495__end__7.getHeadConcSection() ;if ( (tomMatch495_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch495_10= tomMatch495_11.getGrammarList() ;if ( ((tomMatch495_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch495_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch495__end__15=tomMatch495_10;do {{if (!( tomMatch495__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch495_19= tomMatch495__end__15.getHeadConcGrammar() ;if ( (tomMatch495_19 instanceof tom.gom.adt.gom.types.grammar.Sorts) ) { tom.gom.adt.gom.types.GomTypeList  tomMatch495_18= tomMatch495_19.getTypeList() ;if ( ((tomMatch495_18 instanceof tom.gom.adt.gom.types.gomtypelist.ConsConcGomType) || (tomMatch495_18 instanceof tom.gom.adt.gom.types.gomtypelist.EmptyConcGomType)) ) { tom.gom.adt.gom.types.GomTypeList  tomMatch495__end__23=tomMatch495_18;do {{if (!( tomMatch495__end__23.isEmptyConcGomType() )) { tom.gom.adt.gom.types.GomType  tomMatch495_28= tomMatch495__end__23.getHeadConcGomType() ;if ( (tomMatch495_28 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch495_28.getName() ;



        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          getLogger().log(Level.SEVERE, GomMessage.operatorOnBuiltin.getMessage(),
            new Object[]{(tom_typeName)});
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}if ( tomMatch495__end__23.isEmptyConcGomType() ) {tomMatch495__end__23=tomMatch495_18;} else {tomMatch495__end__23= tomMatch495__end__23.getTailConcGomType() ;}}} while(!( (tomMatch495__end__23==tomMatch495_18) ));}}}if ( tomMatch495__end__15.isEmptyConcGrammar() ) {tomMatch495__end__15=tomMatch495_10;} else {tomMatch495__end__15= tomMatch495__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch495__end__15==tomMatch495_10) ));}}}if ( tomMatch495__end__7.isEmptyConcSection() ) {tomMatch495__end__7=tomMatch495_2;} else {tomMatch495__end__7= tomMatch495__end__7.getTailConcSection() ;}}} while(!( (tomMatch495__end__7==tomMatch495_2) ));}}}}}{{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch496_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch496_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch496_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch496__end__7=tomMatch496_2;do {{if (!( tomMatch496__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch496_11= tomMatch496__end__7.getHeadConcSection() ;if ( (tomMatch496_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch496_10= tomMatch496_11.getGrammarList() ;if ( ((tomMatch496_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch496_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch496__end__15=tomMatch496_10;do {{if (!( tomMatch496__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch496_19= tomMatch496__end__15.getHeadConcGrammar() ;if ( (tomMatch496_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch496_18= tomMatch496_19.getProductionList() ;if ( ((tomMatch496_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch496_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch496__end__23=tomMatch496_18;do {{if (!( tomMatch496__end__23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch496_27= tomMatch496__end__23.getHeadConcProduction() ;if ( (tomMatch496_27 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch496_26= tomMatch496_27.getType() ;if ( (tomMatch496_26 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch496_26.getName() ;







        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          getLogger().log(Level.SEVERE, GomMessage.operatorOnBuiltin.getMessage(),
            new Object[]{(tom_typeName)});
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}}if ( tomMatch496__end__23.isEmptyConcProduction() ) {tomMatch496__end__23=tomMatch496_18;} else {tomMatch496__end__23= tomMatch496__end__23.getTailConcProduction() ;}}} while(!( (tomMatch496__end__23==tomMatch496_18) ));}}}if ( tomMatch496__end__15.isEmptyConcGrammar() ) {tomMatch496__end__15=tomMatch496_10;} else {tomMatch496__end__15= tomMatch496__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch496__end__15==tomMatch496_10) ));}}}if ( tomMatch496__end__7.isEmptyConcSection() ) {tomMatch496__end__7=tomMatch496_2;} else {tomMatch496__end__7= tomMatch496__end__7.getTailConcSection() ;}}} while(!( (tomMatch496__end__7==tomMatch496_2) ));}}}}}

    return result;
  }

  /*
   * Get all sort uses in a module (as codomain of an operator)
   */
  private Collection<SortDecl> getSortDeclarationInCodomain(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch497_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch497_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch497_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch497__end__7=tomMatch497_2;do {{if (!( tomMatch497__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch497_11= tomMatch497__end__7.getHeadConcSection() ;if ( (tomMatch497_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch497_10= tomMatch497_11.getGrammarList() ;if ( ((tomMatch497_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch497_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch497__end__15=tomMatch497_10;do {{if (!( tomMatch497__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch497_19= tomMatch497__end__15.getHeadConcGrammar() ;if ( (tomMatch497_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch497_18= tomMatch497_19.getProductionList() ;if ( ((tomMatch497_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch497_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch497__end__23=tomMatch497_18;do {{if (!( tomMatch497__end__23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch497_30= tomMatch497__end__23.getHeadConcProduction() ;if ( (tomMatch497_30 instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.GomType  tomMatch497_28= tomMatch497_30.getCodomain() ;if ( (tomMatch497_28 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch497_28.getName() ;











        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}}if ( tomMatch497__end__23.isEmptyConcProduction() ) {tomMatch497__end__23=tomMatch497_18;} else {tomMatch497__end__23= tomMatch497__end__23.getTailConcProduction() ;}}} while(!( (tomMatch497__end__23==tomMatch497_18) ));}}}if ( tomMatch497__end__15.isEmptyConcGrammar() ) {tomMatch497__end__15=tomMatch497_10;} else {tomMatch497__end__15= tomMatch497__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch497__end__15==tomMatch497_10) ));}}}if ( tomMatch497__end__7.isEmptyConcSection() ) {tomMatch497__end__7=tomMatch497_2;} else {tomMatch497__end__7= tomMatch497__end__7.getTailConcSection() ;}}} while(!( (tomMatch497__end__7==tomMatch497_2) ));}}}}}{{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch498_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch498_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch498_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch498__end__7=tomMatch498_2;do {{if (!( tomMatch498__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch498_11= tomMatch498__end__7.getHeadConcSection() ;if ( (tomMatch498_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch498_10= tomMatch498_11.getGrammarList() ;if ( ((tomMatch498_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch498_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch498__end__15=tomMatch498_10;do {{if (!( tomMatch498__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch498_19= tomMatch498__end__15.getHeadConcGrammar() ;if ( (tomMatch498_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch498_18= tomMatch498_19.getProductionList() ;if ( ((tomMatch498_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch498_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch498__end__23=tomMatch498_18;do {{if (!( tomMatch498__end__23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch498_27= tomMatch498__end__23.getHeadConcProduction() ;if ( (tomMatch498_27 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.ProductionList  tomMatch498_26= tomMatch498_27.getProductionList() ;if ( ((tomMatch498_26 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch498_26 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch498__end__31=tomMatch498_26;do {{if (!( tomMatch498__end__31.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch498_38= tomMatch498__end__31.getHeadConcProduction() ;if ( (tomMatch498_38 instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.GomType  tomMatch498_36= tomMatch498_38.getCodomain() ;if ( (tomMatch498_36 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch498_36.getName() ;













        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}}if ( tomMatch498__end__31.isEmptyConcProduction() ) {tomMatch498__end__31=tomMatch498_26;} else {tomMatch498__end__31= tomMatch498__end__31.getTailConcProduction() ;}}} while(!( (tomMatch498__end__31==tomMatch498_26) ));}}}if ( tomMatch498__end__23.isEmptyConcProduction() ) {tomMatch498__end__23=tomMatch498_18;} else {tomMatch498__end__23= tomMatch498__end__23.getTailConcProduction() ;}}} while(!( (tomMatch498__end__23==tomMatch498_18) ));}}}if ( tomMatch498__end__15.isEmptyConcGrammar() ) {tomMatch498__end__15=tomMatch498_10;} else {tomMatch498__end__15= tomMatch498__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch498__end__15==tomMatch498_10) ));}}}if ( tomMatch498__end__7.isEmptyConcSection() ) {tomMatch498__end__7=tomMatch498_2;} else {tomMatch498__end__7= tomMatch498__end__7.getTailConcSection() ;}}} while(!( (tomMatch498__end__7==tomMatch498_2) ));}}}}}

    return result;
  }

  /**
   * Get directly imported modules. Skip builtins
   *
   * @param module the main module with imports
   * @return the Collection of imported GomModuleName
   */
  private Collection<GomModuleName> getImportedModules(GomModule module) {
    Set<GomModuleName> imports = new HashSet<GomModuleName>();
    {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tom_sectionList= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;

        imports.add( (( tom.gom.adt.gom.types.GomModule )module).getModuleName() );
        {{if ( (tom_sectionList instanceof tom.gom.adt.gom.types.SectionList) ) {if ( (((( tom.gom.adt.gom.types.SectionList )tom_sectionList) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tom_sectionList) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch500__end__4=(( tom.gom.adt.gom.types.SectionList )tom_sectionList);do {{if (!( tomMatch500__end__4.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch500_8= tomMatch500__end__4.getHeadConcSection() ;if ( (tomMatch500_8 instanceof tom.gom.adt.gom.types.section.Imports) ) { tom.gom.adt.gom.types.ImportList  tomMatch500_7= tomMatch500_8.getImportList() ;if ( ((tomMatch500_7 instanceof tom.gom.adt.gom.types.importlist.ConsConcImportedModule) || (tomMatch500_7 instanceof tom.gom.adt.gom.types.importlist.EmptyConcImportedModule)) ) { tom.gom.adt.gom.types.ImportList  tomMatch500__end__12=tomMatch500_7;do {{if (!( tomMatch500__end__12.isEmptyConcImportedModule() )) { tom.gom.adt.gom.types.ImportedModule  tomMatch500_16= tomMatch500__end__12.getHeadConcImportedModule() ;if ( (tomMatch500_16 instanceof tom.gom.adt.gom.types.importedmodule.Import) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch500_15= tomMatch500_16.getModuleName() ;if ( (tomMatch500_15 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {





            if (!getGomEnvironment().isBuiltin( tomMatch500_15.getName() )) {
              imports.add(tomMatch500_15);
            }
          }}}if ( tomMatch500__end__12.isEmptyConcImportedModule() ) {tomMatch500__end__12=tomMatch500_7;} else {tomMatch500__end__12= tomMatch500__end__12.getTailConcImportedModule() ;}}} while(!( (tomMatch500__end__12==tomMatch500_7) ));}}}if ( tomMatch500__end__4.isEmptyConcSection() ) {tomMatch500__end__4=(( tom.gom.adt.gom.types.SectionList )tom_sectionList);} else {tomMatch500__end__4= tomMatch500__end__4.getTailConcSection() ;}}} while(!( (tomMatch500__end__4==(( tom.gom.adt.gom.types.SectionList )tom_sectionList)) ));}}}}

      }}}}

    return imports;
  }

  private GomModule getModule(GomModuleName modname, GomModuleList list) {
    {{if ( (list instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch501__end__4=(( tom.gom.adt.gom.types.GomModuleList )list);do {{if (!( tomMatch501__end__4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch501_8= tomMatch501__end__4.getHeadConcGomModule() ;if ( (tomMatch501_8 instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) {

        if ( tomMatch501_8.getModuleName() .equals(modname)) {
          return  tomMatch501__end__4.getHeadConcGomModule() ;
        }
      }}if ( tomMatch501__end__4.isEmptyConcGomModule() ) {tomMatch501__end__4=(( tom.gom.adt.gom.types.GomModuleList )list);} else {tomMatch501__end__4= tomMatch501__end__4.getTailConcGomModule() ;}}} while(!( (tomMatch501__end__4==(( tom.gom.adt.gom.types.GomModuleList )list)) ));}}}}

    throw new GomRuntimeException("Module "+ modname +" not present");
  }

  private Collection<GomModuleName> getTransitiveClosureImports(
      GomModule module,
      GomModuleList moduleList) {
    Set<GomModuleName> imported = new HashSet<GomModuleName>();
    imported.addAll(getImportedModules(module));

    Set<GomModuleName> newSet = new HashSet<GomModuleName>();
    while(!newSet.equals(imported)) {
      newSet.addAll(imported);
      imported.addAll(newSet);
      for (GomModuleName modname : imported) {
        newSet.addAll(getImportedModules(getModule(modname,moduleList)));
      }
    }
    return newSet;
  }

  private void buildDependencyMap(GomModuleList moduleList) {
    {{if ( (moduleList instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch502__end__4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);do {{if (!( tomMatch502__end__4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch502_8= tomMatch502__end__4.getHeadConcGomModule() ;if ( (tomMatch502_8 instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tom_moduleName= tomMatch502_8.getModuleName() ;

        ModuleDeclList importsModuleDeclList =  tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() ;
        Iterator it = getTransitiveClosureImports( tomMatch502__end__4.getHeadConcGomModule() ,moduleList).iterator();
        while(it.hasNext()) {
          GomModuleName importedModuleName = (GomModuleName) it.next();
          importsModuleDeclList = 
             tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(importedModuleName, getStreamManager().getPackagePath(importedModuleName.getName())) ,tom_append_list_ConcModuleDecl(importsModuleDeclList, tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() )) 
;
        }
        getGomEnvironment().addModuleDependency(
             tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ,importsModuleDeclList);
      }}if ( tomMatch502__end__4.isEmptyConcGomModule() ) {tomMatch502__end__4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);} else {tomMatch502__end__4= tomMatch502__end__4.getTailConcGomModule() ;}}} while(!( (tomMatch502__end__4==(( tom.gom.adt.gom.types.GomModuleList )moduleList)) ));}}}}

  }

  private boolean checkSortValidity(Sort sort) {
    boolean valid = true;
    // check if the same slot name is used with different types
    Map<String,SortDecl> mapNameType = new HashMap<String,SortDecl>();
    {{if ( (sort instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )sort) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch503_1= (( tom.gom.adt.gom.types.Sort )sort).getDecl() ; tom.gom.adt.gom.types.OperatorDeclList  tomMatch503_2= (( tom.gom.adt.gom.types.Sort )sort).getOperatorDecls() ;boolean tomMatch503_25= false ; String  tomMatch503_4= "" ;if ( (tomMatch503_1 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {{tomMatch503_25= true ;tomMatch503_4= tomMatch503_1.getName() ;}} else {if ( (tomMatch503_1 instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {{tomMatch503_25= true ;tomMatch503_4= tomMatch503_1.getName() ;}}}if (tomMatch503_25) {if ( ((tomMatch503_2 instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || (tomMatch503_2 instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch503__end__9=tomMatch503_2;do {{if (!( tomMatch503__end__9.isEmptyConcOperator() )) { tom.gom.adt.gom.types.OperatorDecl  tomMatch503_13= tomMatch503__end__9.getHeadConcOperator() ;if ( (tomMatch503_13 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch503_12= tomMatch503_13.getProd() ;if ( (tomMatch503_12 instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tomMatch503_14= tomMatch503_12.getSlots() ;if ( ((tomMatch503_14 instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || (tomMatch503_14 instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) { tom.gom.adt.gom.types.SlotList  tomMatch503__end__19=tomMatch503_14;do {{if (!( tomMatch503__end__19.isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch503_24= tomMatch503__end__19.getHeadConcSlot() ;if ( (tomMatch503_24 instanceof tom.gom.adt.gom.types.slot.Slot) ) { String  tom_slotName= tomMatch503_24.getName() ; tom.gom.adt.gom.types.SortDecl  tom_slotSort= tomMatch503_24.getSort() ;






        if(!mapNameType.containsKey(tom_slotName)) {
          mapNameType.put(tom_slotName,tom_slotSort);
        } else {
          SortDecl prevSort = mapNameType.get(tom_slotName);
          if (!prevSort.equals(tom_slotSort)) {
            getLogger().log(Level.SEVERE,
                GomMessage.slotIncompatibleTypes.getMessage(),
                new Object[]{tomMatch503_4,tom_slotName,prevSort.getName(),
                             (tom_slotSort).getName()});
            valid = false;
          }
        }
      }}if ( tomMatch503__end__19.isEmptyConcSlot() ) {tomMatch503__end__19=tomMatch503_14;} else {tomMatch503__end__19= tomMatch503__end__19.getTailConcSlot() ;}}} while(!( (tomMatch503__end__19==tomMatch503_14) ));}}}}if ( tomMatch503__end__9.isEmptyConcOperator() ) {tomMatch503__end__9=tomMatch503_2;} else {tomMatch503__end__9= tomMatch503__end__9.getTailConcOperator() ;}}} while(!( (tomMatch503__end__9==tomMatch503_2) ));}}}}}}

    return valid;
  }

  private String showSortList(Collection<SortDecl> decls) {
    String sorts = "";
    Iterator<SortDecl> it = decls.iterator();
    if(it.hasNext()) {
      SortDecl decl = it.next();
      sorts += decl.getName();
    }
    while(it.hasNext()) {
      SortDecl decl = it.next();
      sorts += ", "+decl.getName();
    }
    return sorts;
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
