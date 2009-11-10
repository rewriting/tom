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

         private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GrammarList  tom_append_list_ConcGrammar( tom.gom.adt.gom.types.GrammarList l1,  tom.gom.adt.gom.types.GrammarList  l2) {     if( l1.isEmptyConcGrammar() ) {       return l2;     } else if( l2.isEmptyConcGrammar() ) {       return l1;     } else if(  l1.getTailConcGrammar() .isEmptyConcGrammar() ) {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,l2) ;     } else {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,tom_append_list_ConcGrammar( l1.getTailConcGrammar() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GrammarList  tom_get_slice_ConcGrammar( tom.gom.adt.gom.types.GrammarList  begin,  tom.gom.adt.gom.types.GrammarList  end, tom.gom.adt.gom.types.GrammarList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGrammar()  ||  (end== tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( begin.getHeadConcGrammar() ,( tom.gom.adt.gom.types.GrammarList )tom_get_slice_ConcGrammar( begin.getTailConcGrammar() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.OperatorDeclList  tom_append_list_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList l1,  tom.gom.adt.gom.types.OperatorDeclList  l2) {     if( l1.isEmptyConcOperator() ) {       return l2;     } else if( l2.isEmptyConcOperator() ) {       return l1;     } else if(  l1.getTailConcOperator() .isEmptyConcOperator() ) {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,l2) ;     } else {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,tom_append_list_ConcOperator( l1.getTailConcOperator() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.OperatorDeclList  tom_get_slice_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList  begin,  tom.gom.adt.gom.types.OperatorDeclList  end, tom.gom.adt.gom.types.OperatorDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcOperator()  ||  (end== tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( begin.getHeadConcOperator() ,( tom.gom.adt.gom.types.OperatorDeclList )tom_get_slice_ConcOperator( begin.getTailConcOperator() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortDeclList  tom_append_list_ConcSortDecl( tom.gom.adt.gom.types.SortDeclList l1,  tom.gom.adt.gom.types.SortDeclList  l2) {     if( l1.isEmptyConcSortDecl() ) {       return l2;     } else if( l2.isEmptyConcSortDecl() ) {       return l1;     } else if(  l1.getTailConcSortDecl() .isEmptyConcSortDecl() ) {       return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( l1.getHeadConcSortDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( l1.getHeadConcSortDecl() ,tom_append_list_ConcSortDecl( l1.getTailConcSortDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortDeclList  tom_get_slice_ConcSortDecl( tom.gom.adt.gom.types.SortDeclList  begin,  tom.gom.adt.gom.types.SortDeclList  end, tom.gom.adt.gom.types.SortDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSortDecl()  ||  (end== tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( begin.getHeadConcSortDecl() ,( tom.gom.adt.gom.types.SortDeclList )tom_get_slice_ConcSortDecl( begin.getTailConcSortDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GomTypeList  tom_append_list_ConcGomType( tom.gom.adt.gom.types.GomTypeList l1,  tom.gom.adt.gom.types.GomTypeList  l2) {     if( l1.isEmptyConcGomType() ) {       return l2;     } else if( l2.isEmptyConcGomType() ) {       return l1;     } else if(  l1.getTailConcGomType() .isEmptyConcGomType() ) {       return  tom.gom.adt.gom.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,tom_append_list_ConcGomType( l1.getTailConcGomType() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomTypeList  tom_get_slice_ConcGomType( tom.gom.adt.gom.types.GomTypeList  begin,  tom.gom.adt.gom.types.GomTypeList  end, tom.gom.adt.gom.types.GomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomType()  ||  (end== tom.gom.adt.gom.types.gomtypelist.EmptyConcGomType.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gomtypelist.ConsConcGomType.make( begin.getHeadConcGomType() ,( tom.gom.adt.gom.types.GomTypeList )tom_get_slice_ConcGomType( begin.getTailConcGomType() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ImportList  tom_append_list_ConcImportedModule( tom.gom.adt.gom.types.ImportList l1,  tom.gom.adt.gom.types.ImportList  l2) {     if( l1.isEmptyConcImportedModule() ) {       return l2;     } else if( l2.isEmptyConcImportedModule() ) {       return l1;     } else if(  l1.getTailConcImportedModule() .isEmptyConcImportedModule() ) {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,tom_append_list_ConcImportedModule( l1.getTailConcImportedModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ImportList  tom_get_slice_ConcImportedModule( tom.gom.adt.gom.types.ImportList  begin,  tom.gom.adt.gom.types.ImportList  end, tom.gom.adt.gom.types.ImportList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcImportedModule()  ||  (end== tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( begin.getHeadConcImportedModule() ,( tom.gom.adt.gom.types.ImportList )tom_get_slice_ConcImportedModule( begin.getTailConcImportedModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleDeclList  tom_append_list_ConcModuleDecl( tom.gom.adt.gom.types.ModuleDeclList l1,  tom.gom.adt.gom.types.ModuleDeclList  l2) {     if( l1.isEmptyConcModuleDecl() ) {       return l2;     } else if( l2.isEmptyConcModuleDecl() ) {       return l1;     } else if(  l1.getTailConcModuleDecl() .isEmptyConcModuleDecl() ) {       return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( l1.getHeadConcModuleDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( l1.getHeadConcModuleDecl() ,tom_append_list_ConcModuleDecl( l1.getTailConcModuleDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleDeclList  tom_get_slice_ConcModuleDecl( tom.gom.adt.gom.types.ModuleDeclList  begin,  tom.gom.adt.gom.types.ModuleDeclList  end, tom.gom.adt.gom.types.ModuleDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModuleDecl()  ||  (end== tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( begin.getHeadConcModuleDecl() ,( tom.gom.adt.gom.types.ModuleDeclList )tom_get_slice_ConcModuleDecl( begin.getTailConcModuleDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {     if( l1.isEmptyConcSort() ) {       return l2;     } else if( l2.isEmptyConcSort() ) {       return l1;     } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {     if( l1.isEmptyConcModule() ) {       return l2;     } else if( l2.isEmptyConcModule() ) {       return l1;     } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;   }    

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
      {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch493_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;if ( ((tomMatch493_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch493_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch493__end__7=tomMatch493_2;do {{if (!( tomMatch493__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch493_11= tomMatch493__end__7.getHeadConcSection() ;if ( (tomMatch493_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch493_10= tomMatch493_11.getGrammarList() ;if ( ((tomMatch493_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch493_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch493__end__15=tomMatch493_10;do {{if (!( tomMatch493__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch493_19= tomMatch493__end__15.getHeadConcGrammar() ;if ( (tomMatch493_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch493_18= tomMatch493_19.getProductionList() ;if ( ((tomMatch493_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch493_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch493__end__23=tomMatch493_18;do {{if (!( tomMatch493__end__23.isEmptyConcProduction() )) {if ( ( tomMatch493__end__23.getHeadConcProduction()  instanceof tom.gom.adt.gom.types.production.Production) ) {



          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl( tomMatch493__end__23.getHeadConcProduction() ,sortDeclList,operatorsForSort);

        }}if ( tomMatch493__end__23.isEmptyConcProduction() ) {tomMatch493__end__23=tomMatch493_18;} else {tomMatch493__end__23= tomMatch493__end__23.getTailConcProduction() ;}}} while(!( (tomMatch493__end__23==tomMatch493_18) ));}}}if ( tomMatch493__end__15.isEmptyConcGrammar() ) {tomMatch493__end__15=tomMatch493_10;} else {tomMatch493__end__15= tomMatch493__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch493__end__15==tomMatch493_10) ));}}}if ( tomMatch493__end__7.isEmptyConcSection() ) {tomMatch493__end__7=tomMatch493_2;} else {tomMatch493__end__7= tomMatch493__end__7.getTailConcSection() ;}}} while(!( (tomMatch493__end__7==tomMatch493_2) ));}}}}}{{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch494_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;if ( ((tomMatch494_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch494_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch494__end__7=tomMatch494_2;do {{if (!( tomMatch494__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch494_11= tomMatch494__end__7.getHeadConcSection() ;if ( (tomMatch494_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch494_10= tomMatch494_11.getGrammarList() ;if ( ((tomMatch494_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch494_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch494__end__15=tomMatch494_10;do {{if (!( tomMatch494__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch494_19= tomMatch494__end__15.getHeadConcGrammar() ;if ( (tomMatch494_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch494_18= tomMatch494_19.getProductionList() ;if ( ((tomMatch494_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch494_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch494__end__23=tomMatch494_18;do {{if (!( tomMatch494__end__23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch494_27= tomMatch494__end__23.getHeadConcProduction() ;if ( (tomMatch494_27 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.ProductionList  tomMatch494_26= tomMatch494_27.getProductionList() ;if ( ((tomMatch494_26 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch494_26 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch494__end__31=tomMatch494_26;do {{if (!( tomMatch494__end__31.isEmptyConcProduction() )) {if ( ( tomMatch494__end__31.getHeadConcProduction()  instanceof tom.gom.adt.gom.types.production.Production) ) {








          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl( tomMatch494__end__31.getHeadConcProduction() ,sortDeclList,operatorsForSort);

        }}if ( tomMatch494__end__31.isEmptyConcProduction() ) {tomMatch494__end__31=tomMatch494_26;} else {tomMatch494__end__31= tomMatch494__end__31.getTailConcProduction() ;}}} while(!( (tomMatch494__end__31==tomMatch494_26) ));}}}if ( tomMatch494__end__23.isEmptyConcProduction() ) {tomMatch494__end__23=tomMatch494_18;} else {tomMatch494__end__23= tomMatch494__end__23.getTailConcProduction() ;}}} while(!( (tomMatch494__end__23==tomMatch494_18) ));}}}if ( tomMatch494__end__15.isEmptyConcGrammar() ) {tomMatch494__end__15=tomMatch494_10;} else {tomMatch494__end__15= tomMatch494__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch494__end__15==tomMatch494_10) ));}}}if ( tomMatch494__end__7.isEmptyConcSection() ) {tomMatch494__end__7=tomMatch494_2;} else {tomMatch494__end__7= tomMatch494__end__7.getTailConcSection() ;}}} while(!( (tomMatch494__end__7==tomMatch494_2) ));}}}}}

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
      {{if ( (sdeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch495__end__4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);do {{if (!( tomMatch495__end__4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tom_sdecl= tomMatch495__end__4.getHeadConcSortDecl() ;

          OperatorDeclList opdecl = operatorsForSort.get(tom_sdecl);
          Sort fullSort =  tom.gom.adt.gom.types.sort.Sort.make(tom_sdecl, opdecl) ;
          if(checkSortValidity(fullSort)) {
            sortList =  tom.gom.adt.gom.types.sortlist.ConsConcSort.make(fullSort,tom_append_list_ConcSort(sortList, tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() )) ;
          }
        }if ( tomMatch495__end__4.isEmptyConcSortDecl() ) {tomMatch495__end__4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);} else {tomMatch495__end__4= tomMatch495__end__4.getTailConcSortDecl() ;}}} while(!( (tomMatch495__end__4==(( tom.gom.adt.gom.types.SortDeclList )sdeclList)) ));}}}}

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

    {{if ( (prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )prod) instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.GomType  tomMatch496_3= (( tom.gom.adt.gom.types.Production )prod).getCodomain() ; String  tom_name= (( tom.gom.adt.gom.types.Production )prod).getName() ;if ( (tomMatch496_3 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { tom.gom.adt.gom.types.Option  tom_options= (( tom.gom.adt.gom.types.Production )prod).getOption() ;



        SortDecl codomainSort = declFromTypename( tomMatch496_3.getName() ,sortDeclList);
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
    {{if ( (sortDeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch497__end__4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);do {{if (!( tomMatch497__end__4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tomMatch497_8= tomMatch497__end__4.getHeadConcSortDecl() ;if ( (tomMatch497_8 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        if (typename.equals( tomMatch497_8.getName() )) {
          return  tomMatch497__end__4.getHeadConcSortDecl() ;
        }
      }}if ( tomMatch497__end__4.isEmptyConcSortDecl() ) {tomMatch497__end__4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);} else {tomMatch497__end__4= tomMatch497__end__4.getTailConcSortDecl() ;}}} while(!( (tomMatch497__end__4==(( tom.gom.adt.gom.types.SortDeclList )sortDeclList)) ));}}}}

    
    getLogger().log(Level.SEVERE, GomMessage.unknownSort.getMessage(),
        new Object[]{typename});
    /* If the sort is not known, assume it is a builtin */
    return  tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl.make(typename) ;
  }

  private TypedProduction typedProduction(FieldList domain, SortDeclList sortDeclList) {
    {{if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )domain).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch498_5= (( tom.gom.adt.gom.types.FieldList )domain).getHeadConcField() ;if ( (tomMatch498_5 instanceof tom.gom.adt.gom.types.field.StarredField) ) { tom.gom.adt.gom.types.GomType  tomMatch498_3= tomMatch498_5.getFieldType() ;if ( (tomMatch498_3 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {if (  (( tom.gom.adt.gom.types.FieldList )domain).getTailConcField() .isEmptyConcField() ) {

        return  tom.gom.adt.gom.types.typedproduction.Variadic.make(declFromTypename( tomMatch498_3.getName() ,sortDeclList)) ;
      }}}}}}}{if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {

        return  tom.gom.adt.gom.types.typedproduction.Slots.make(typedSlotList((( tom.gom.adt.gom.types.FieldList )domain),sortDeclList)) ;
      }}}}

    // the error message could be more refined
    throw new GomRuntimeException("TypeExpander::typedProduction: illformed Production");
  }

  private SlotList typedSlotList(FieldList fields, SortDeclList sortDeclList) {
    {{if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if ( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() ) {

        return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
      }}}}{if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch499_9= (( tom.gom.adt.gom.types.FieldList )fields).getHeadConcField() ;if ( (tomMatch499_9 instanceof tom.gom.adt.gom.types.field.NamedField) ) { tom.gom.adt.gom.types.GomType  tomMatch499_8= tomMatch499_9.getFieldType() ;if ( (tomMatch499_8 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {

        SlotList newtail = typedSlotList( (( tom.gom.adt.gom.types.FieldList )fields).getTailConcField() ,sortDeclList);
        return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch499_9.getName() , declFromTypename( tomMatch499_8.getName() ,sortDeclList)) ,tom_append_list_ConcSlot(newtail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;
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
    {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch500_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch500_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch500_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch500__end__7=tomMatch500_2;do {{if (!( tomMatch500__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch500_11= tomMatch500__end__7.getHeadConcSection() ;if ( (tomMatch500_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch500_10= tomMatch500_11.getGrammarList() ;if ( ((tomMatch500_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch500_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch500__end__15=tomMatch500_10;do {{if (!( tomMatch500__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch500_19= tomMatch500__end__15.getHeadConcGrammar() ;if ( (tomMatch500_19 instanceof tom.gom.adt.gom.types.grammar.Sorts) ) { tom.gom.adt.gom.types.GomTypeList  tomMatch500_18= tomMatch500_19.getTypeList() ;if ( ((tomMatch500_18 instanceof tom.gom.adt.gom.types.gomtypelist.ConsConcGomType) || (tomMatch500_18 instanceof tom.gom.adt.gom.types.gomtypelist.EmptyConcGomType)) ) { tom.gom.adt.gom.types.GomTypeList  tomMatch500__end__23=tomMatch500_18;do {{if (!( tomMatch500__end__23.isEmptyConcGomType() )) { tom.gom.adt.gom.types.GomType  tomMatch500_28= tomMatch500__end__23.getHeadConcGomType() ;if ( (tomMatch500_28 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch500_28.getName() ;



        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          getLogger().log(Level.SEVERE, GomMessage.operatorOnBuiltin.getMessage(),
            new Object[]{(tom_typeName)});
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}if ( tomMatch500__end__23.isEmptyConcGomType() ) {tomMatch500__end__23=tomMatch500_18;} else {tomMatch500__end__23= tomMatch500__end__23.getTailConcGomType() ;}}} while(!( (tomMatch500__end__23==tomMatch500_18) ));}}}if ( tomMatch500__end__15.isEmptyConcGrammar() ) {tomMatch500__end__15=tomMatch500_10;} else {tomMatch500__end__15= tomMatch500__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch500__end__15==tomMatch500_10) ));}}}if ( tomMatch500__end__7.isEmptyConcSection() ) {tomMatch500__end__7=tomMatch500_2;} else {tomMatch500__end__7= tomMatch500__end__7.getTailConcSection() ;}}} while(!( (tomMatch500__end__7==tomMatch500_2) ));}}}}}{{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch501_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch501_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch501_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch501__end__7=tomMatch501_2;do {{if (!( tomMatch501__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch501_11= tomMatch501__end__7.getHeadConcSection() ;if ( (tomMatch501_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch501_10= tomMatch501_11.getGrammarList() ;if ( ((tomMatch501_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch501_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch501__end__15=tomMatch501_10;do {{if (!( tomMatch501__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch501_19= tomMatch501__end__15.getHeadConcGrammar() ;if ( (tomMatch501_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch501_18= tomMatch501_19.getProductionList() ;if ( ((tomMatch501_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch501_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch501__end__23=tomMatch501_18;do {{if (!( tomMatch501__end__23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch501_27= tomMatch501__end__23.getHeadConcProduction() ;if ( (tomMatch501_27 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch501_26= tomMatch501_27.getType() ;if ( (tomMatch501_26 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch501_26.getName() ;







        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          getLogger().log(Level.SEVERE, GomMessage.operatorOnBuiltin.getMessage(),
            new Object[]{(tom_typeName)});
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}}if ( tomMatch501__end__23.isEmptyConcProduction() ) {tomMatch501__end__23=tomMatch501_18;} else {tomMatch501__end__23= tomMatch501__end__23.getTailConcProduction() ;}}} while(!( (tomMatch501__end__23==tomMatch501_18) ));}}}if ( tomMatch501__end__15.isEmptyConcGrammar() ) {tomMatch501__end__15=tomMatch501_10;} else {tomMatch501__end__15= tomMatch501__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch501__end__15==tomMatch501_10) ));}}}if ( tomMatch501__end__7.isEmptyConcSection() ) {tomMatch501__end__7=tomMatch501_2;} else {tomMatch501__end__7= tomMatch501__end__7.getTailConcSection() ;}}} while(!( (tomMatch501__end__7==tomMatch501_2) ));}}}}}

    return result;
  }

  /*
   * Get all sort uses in a module (as codomain of an operator)
   */
  private Collection<SortDecl> getSortDeclarationInCodomain(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch502_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch502_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch502_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch502__end__7=tomMatch502_2;do {{if (!( tomMatch502__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch502_11= tomMatch502__end__7.getHeadConcSection() ;if ( (tomMatch502_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch502_10= tomMatch502_11.getGrammarList() ;if ( ((tomMatch502_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch502_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch502__end__15=tomMatch502_10;do {{if (!( tomMatch502__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch502_19= tomMatch502__end__15.getHeadConcGrammar() ;if ( (tomMatch502_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch502_18= tomMatch502_19.getProductionList() ;if ( ((tomMatch502_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch502_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch502__end__23=tomMatch502_18;do {{if (!( tomMatch502__end__23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch502_30= tomMatch502__end__23.getHeadConcProduction() ;if ( (tomMatch502_30 instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.GomType  tomMatch502_28= tomMatch502_30.getCodomain() ;if ( (tomMatch502_28 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch502_28.getName() ;











        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}}if ( tomMatch502__end__23.isEmptyConcProduction() ) {tomMatch502__end__23=tomMatch502_18;} else {tomMatch502__end__23= tomMatch502__end__23.getTailConcProduction() ;}}} while(!( (tomMatch502__end__23==tomMatch502_18) ));}}}if ( tomMatch502__end__15.isEmptyConcGrammar() ) {tomMatch502__end__15=tomMatch502_10;} else {tomMatch502__end__15= tomMatch502__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch502__end__15==tomMatch502_10) ));}}}if ( tomMatch502__end__7.isEmptyConcSection() ) {tomMatch502__end__7=tomMatch502_2;} else {tomMatch502__end__7= tomMatch502__end__7.getTailConcSection() ;}}} while(!( (tomMatch502__end__7==tomMatch502_2) ));}}}}}{{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch503_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch503_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch503_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch503__end__7=tomMatch503_2;do {{if (!( tomMatch503__end__7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch503_11= tomMatch503__end__7.getHeadConcSection() ;if ( (tomMatch503_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch503_10= tomMatch503_11.getGrammarList() ;if ( ((tomMatch503_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch503_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch503__end__15=tomMatch503_10;do {{if (!( tomMatch503__end__15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch503_19= tomMatch503__end__15.getHeadConcGrammar() ;if ( (tomMatch503_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch503_18= tomMatch503_19.getProductionList() ;if ( ((tomMatch503_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch503_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch503__end__23=tomMatch503_18;do {{if (!( tomMatch503__end__23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch503_27= tomMatch503__end__23.getHeadConcProduction() ;if ( (tomMatch503_27 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.ProductionList  tomMatch503_26= tomMatch503_27.getProductionList() ;if ( ((tomMatch503_26 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch503_26 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch503__end__31=tomMatch503_26;do {{if (!( tomMatch503__end__31.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch503_38= tomMatch503__end__31.getHeadConcProduction() ;if ( (tomMatch503_38 instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.GomType  tomMatch503_36= tomMatch503_38.getCodomain() ;if ( (tomMatch503_36 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch503_36.getName() ;













        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}}if ( tomMatch503__end__31.isEmptyConcProduction() ) {tomMatch503__end__31=tomMatch503_26;} else {tomMatch503__end__31= tomMatch503__end__31.getTailConcProduction() ;}}} while(!( (tomMatch503__end__31==tomMatch503_26) ));}}}if ( tomMatch503__end__23.isEmptyConcProduction() ) {tomMatch503__end__23=tomMatch503_18;} else {tomMatch503__end__23= tomMatch503__end__23.getTailConcProduction() ;}}} while(!( (tomMatch503__end__23==tomMatch503_18) ));}}}if ( tomMatch503__end__15.isEmptyConcGrammar() ) {tomMatch503__end__15=tomMatch503_10;} else {tomMatch503__end__15= tomMatch503__end__15.getTailConcGrammar() ;}}} while(!( (tomMatch503__end__15==tomMatch503_10) ));}}}if ( tomMatch503__end__7.isEmptyConcSection() ) {tomMatch503__end__7=tomMatch503_2;} else {tomMatch503__end__7= tomMatch503__end__7.getTailConcSection() ;}}} while(!( (tomMatch503__end__7==tomMatch503_2) ));}}}}}

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
        {{if ( (tom_sectionList instanceof tom.gom.adt.gom.types.SectionList) ) {if ( (((( tom.gom.adt.gom.types.SectionList )tom_sectionList) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tom_sectionList) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch505__end__4=(( tom.gom.adt.gom.types.SectionList )tom_sectionList);do {{if (!( tomMatch505__end__4.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch505_8= tomMatch505__end__4.getHeadConcSection() ;if ( (tomMatch505_8 instanceof tom.gom.adt.gom.types.section.Imports) ) { tom.gom.adt.gom.types.ImportList  tomMatch505_7= tomMatch505_8.getImportList() ;if ( ((tomMatch505_7 instanceof tom.gom.adt.gom.types.importlist.ConsConcImportedModule) || (tomMatch505_7 instanceof tom.gom.adt.gom.types.importlist.EmptyConcImportedModule)) ) { tom.gom.adt.gom.types.ImportList  tomMatch505__end__12=tomMatch505_7;do {{if (!( tomMatch505__end__12.isEmptyConcImportedModule() )) { tom.gom.adt.gom.types.ImportedModule  tomMatch505_16= tomMatch505__end__12.getHeadConcImportedModule() ;if ( (tomMatch505_16 instanceof tom.gom.adt.gom.types.importedmodule.Import) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch505_15= tomMatch505_16.getModuleName() ;if ( (tomMatch505_15 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {





            if (!getGomEnvironment().isBuiltin( tomMatch505_15.getName() )) {
              imports.add(tomMatch505_15);
            }
          }}}if ( tomMatch505__end__12.isEmptyConcImportedModule() ) {tomMatch505__end__12=tomMatch505_7;} else {tomMatch505__end__12= tomMatch505__end__12.getTailConcImportedModule() ;}}} while(!( (tomMatch505__end__12==tomMatch505_7) ));}}}if ( tomMatch505__end__4.isEmptyConcSection() ) {tomMatch505__end__4=(( tom.gom.adt.gom.types.SectionList )tom_sectionList);} else {tomMatch505__end__4= tomMatch505__end__4.getTailConcSection() ;}}} while(!( (tomMatch505__end__4==(( tom.gom.adt.gom.types.SectionList )tom_sectionList)) ));}}}}

      }}}}

    return imports;
  }

  private GomModule getModule(GomModuleName modname, GomModuleList list) {
    {{if ( (list instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch506__end__4=(( tom.gom.adt.gom.types.GomModuleList )list);do {{if (!( tomMatch506__end__4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch506_8= tomMatch506__end__4.getHeadConcGomModule() ;if ( (tomMatch506_8 instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) {

        if ( tomMatch506_8.getModuleName() .equals(modname)) {
          return  tomMatch506__end__4.getHeadConcGomModule() ;
        }
      }}if ( tomMatch506__end__4.isEmptyConcGomModule() ) {tomMatch506__end__4=(( tom.gom.adt.gom.types.GomModuleList )list);} else {tomMatch506__end__4= tomMatch506__end__4.getTailConcGomModule() ;}}} while(!( (tomMatch506__end__4==(( tom.gom.adt.gom.types.GomModuleList )list)) ));}}}}

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
    {{if ( (moduleList instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch507__end__4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);do {{if (!( tomMatch507__end__4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch507_8= tomMatch507__end__4.getHeadConcGomModule() ;if ( (tomMatch507_8 instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tom_moduleName= tomMatch507_8.getModuleName() ;

        ModuleDeclList importsModuleDeclList =  tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() ;
        Iterator it = getTransitiveClosureImports( tomMatch507__end__4.getHeadConcGomModule() ,moduleList).iterator();
        while(it.hasNext()) {
          GomModuleName importedModuleName = (GomModuleName) it.next();
          importsModuleDeclList = 
             tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(importedModuleName, getStreamManager().getPackagePath(importedModuleName.getName())) ,tom_append_list_ConcModuleDecl(importsModuleDeclList, tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() )) 
;
        }
        getGomEnvironment().addModuleDependency(
             tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ,importsModuleDeclList);
      }}if ( tomMatch507__end__4.isEmptyConcGomModule() ) {tomMatch507__end__4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);} else {tomMatch507__end__4= tomMatch507__end__4.getTailConcGomModule() ;}}} while(!( (tomMatch507__end__4==(( tom.gom.adt.gom.types.GomModuleList )moduleList)) ));}}}}

  }

  private boolean checkSortValidity(Sort sort) {
    boolean valid = true;
    // check if the same slot name is used with different types
    Map<String,SortDecl> mapNameType = new HashMap<String,SortDecl>();
    {{if ( (sort instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )sort) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch508_1= (( tom.gom.adt.gom.types.Sort )sort).getDecl() ; tom.gom.adt.gom.types.OperatorDeclList  tomMatch508_2= (( tom.gom.adt.gom.types.Sort )sort).getOperatorDecls() ;boolean tomMatch508_25= false ; String  tomMatch508_4= "" ;if ( (tomMatch508_1 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {{tomMatch508_25= true ;tomMatch508_4= tomMatch508_1.getName() ;}} else {if ( (tomMatch508_1 instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {{tomMatch508_25= true ;tomMatch508_4= tomMatch508_1.getName() ;}}}if (tomMatch508_25) {if ( ((tomMatch508_2 instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || (tomMatch508_2 instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch508__end__9=tomMatch508_2;do {{if (!( tomMatch508__end__9.isEmptyConcOperator() )) { tom.gom.adt.gom.types.OperatorDecl  tomMatch508_13= tomMatch508__end__9.getHeadConcOperator() ;if ( (tomMatch508_13 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch508_12= tomMatch508_13.getProd() ;if ( (tomMatch508_12 instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tomMatch508_14= tomMatch508_12.getSlots() ;if ( ((tomMatch508_14 instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || (tomMatch508_14 instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) { tom.gom.adt.gom.types.SlotList  tomMatch508__end__19=tomMatch508_14;do {{if (!( tomMatch508__end__19.isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch508_24= tomMatch508__end__19.getHeadConcSlot() ;if ( (tomMatch508_24 instanceof tom.gom.adt.gom.types.slot.Slot) ) { String  tom_slotName= tomMatch508_24.getName() ; tom.gom.adt.gom.types.SortDecl  tom_slotSort= tomMatch508_24.getSort() ;






        if(!mapNameType.containsKey(tom_slotName)) {
          mapNameType.put(tom_slotName,tom_slotSort);
        } else {
          SortDecl prevSort = mapNameType.get(tom_slotName);
          if (!prevSort.equals(tom_slotSort)) {
            getLogger().log(Level.SEVERE,
                GomMessage.slotIncompatibleTypes.getMessage(),
                new Object[]{tomMatch508_4,tom_slotName,prevSort.getName(),
                             (tom_slotSort).getName()});
            valid = false;
          }
        }
      }}if ( tomMatch508__end__19.isEmptyConcSlot() ) {tomMatch508__end__19=tomMatch508_14;} else {tomMatch508__end__19= tomMatch508__end__19.getTailConcSlot() ;}}} while(!( (tomMatch508__end__19==tomMatch508_14) ));}}}}if ( tomMatch508__end__9.isEmptyConcOperator() ) {tomMatch508__end__9=tomMatch508_2;} else {tomMatch508__end__9= tomMatch508__end__9.getTailConcOperator() ;}}} while(!( (tomMatch508__end__9==tomMatch508_2) ));}}}}}}

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
