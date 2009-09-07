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
      {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch541NameNumber_freshVar_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;if ( ((tomMatch541NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch541NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch541NameNumber_end_7=tomMatch541NameNumber_freshVar_2;do {{if (!( tomMatch541NameNumber_end_7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch541NameNumber_freshVar_11= tomMatch541NameNumber_end_7.getHeadConcSection() ;if ( (tomMatch541NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch541NameNumber_freshVar_10= tomMatch541NameNumber_freshVar_11.getGrammarList() ;if ( ((tomMatch541NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch541NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch541NameNumber_end_15=tomMatch541NameNumber_freshVar_10;do {{if (!( tomMatch541NameNumber_end_15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch541NameNumber_freshVar_19= tomMatch541NameNumber_end_15.getHeadConcGrammar() ;if ( (tomMatch541NameNumber_freshVar_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch541NameNumber_freshVar_18= tomMatch541NameNumber_freshVar_19.getProductionList() ;if ( ((tomMatch541NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch541NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch541NameNumber_end_23=tomMatch541NameNumber_freshVar_18;do {{if (!( tomMatch541NameNumber_end_23.isEmptyConcProduction() )) {if ( ( tomMatch541NameNumber_end_23.getHeadConcProduction()  instanceof tom.gom.adt.gom.types.production.Production) ) {



          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl( tomMatch541NameNumber_end_23.getHeadConcProduction() ,sortDeclList,operatorsForSort);

        }}if ( tomMatch541NameNumber_end_23.isEmptyConcProduction() ) {tomMatch541NameNumber_end_23=tomMatch541NameNumber_freshVar_18;} else {tomMatch541NameNumber_end_23= tomMatch541NameNumber_end_23.getTailConcProduction() ;}}} while(!( (tomMatch541NameNumber_end_23==tomMatch541NameNumber_freshVar_18) ));}}}if ( tomMatch541NameNumber_end_15.isEmptyConcGrammar() ) {tomMatch541NameNumber_end_15=tomMatch541NameNumber_freshVar_10;} else {tomMatch541NameNumber_end_15= tomMatch541NameNumber_end_15.getTailConcGrammar() ;}}} while(!( (tomMatch541NameNumber_end_15==tomMatch541NameNumber_freshVar_10) ));}}}if ( tomMatch541NameNumber_end_7.isEmptyConcSection() ) {tomMatch541NameNumber_end_7=tomMatch541NameNumber_freshVar_2;} else {tomMatch541NameNumber_end_7= tomMatch541NameNumber_end_7.getTailConcSection() ;}}} while(!( (tomMatch541NameNumber_end_7==tomMatch541NameNumber_freshVar_2) ));}}}}}{{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch542NameNumber_freshVar_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;if ( ((tomMatch542NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch542NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch542NameNumber_end_7=tomMatch542NameNumber_freshVar_2;do {{if (!( tomMatch542NameNumber_end_7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch542NameNumber_freshVar_11= tomMatch542NameNumber_end_7.getHeadConcSection() ;if ( (tomMatch542NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch542NameNumber_freshVar_10= tomMatch542NameNumber_freshVar_11.getGrammarList() ;if ( ((tomMatch542NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch542NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch542NameNumber_end_15=tomMatch542NameNumber_freshVar_10;do {{if (!( tomMatch542NameNumber_end_15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch542NameNumber_freshVar_19= tomMatch542NameNumber_end_15.getHeadConcGrammar() ;if ( (tomMatch542NameNumber_freshVar_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch542NameNumber_freshVar_18= tomMatch542NameNumber_freshVar_19.getProductionList() ;if ( ((tomMatch542NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch542NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch542NameNumber_end_23=tomMatch542NameNumber_freshVar_18;do {{if (!( tomMatch542NameNumber_end_23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch542NameNumber_freshVar_27= tomMatch542NameNumber_end_23.getHeadConcProduction() ;if ( (tomMatch542NameNumber_freshVar_27 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.ProductionList  tomMatch542NameNumber_freshVar_26= tomMatch542NameNumber_freshVar_27.getProductionList() ;if ( ((tomMatch542NameNumber_freshVar_26 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch542NameNumber_freshVar_26 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch542NameNumber_end_31=tomMatch542NameNumber_freshVar_26;do {{if (!( tomMatch542NameNumber_end_31.isEmptyConcProduction() )) {if ( ( tomMatch542NameNumber_end_31.getHeadConcProduction()  instanceof tom.gom.adt.gom.types.production.Production) ) {








          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl( tomMatch542NameNumber_end_31.getHeadConcProduction() ,sortDeclList,operatorsForSort);

        }}if ( tomMatch542NameNumber_end_31.isEmptyConcProduction() ) {tomMatch542NameNumber_end_31=tomMatch542NameNumber_freshVar_26;} else {tomMatch542NameNumber_end_31= tomMatch542NameNumber_end_31.getTailConcProduction() ;}}} while(!( (tomMatch542NameNumber_end_31==tomMatch542NameNumber_freshVar_26) ));}}}if ( tomMatch542NameNumber_end_23.isEmptyConcProduction() ) {tomMatch542NameNumber_end_23=tomMatch542NameNumber_freshVar_18;} else {tomMatch542NameNumber_end_23= tomMatch542NameNumber_end_23.getTailConcProduction() ;}}} while(!( (tomMatch542NameNumber_end_23==tomMatch542NameNumber_freshVar_18) ));}}}if ( tomMatch542NameNumber_end_15.isEmptyConcGrammar() ) {tomMatch542NameNumber_end_15=tomMatch542NameNumber_freshVar_10;} else {tomMatch542NameNumber_end_15= tomMatch542NameNumber_end_15.getTailConcGrammar() ;}}} while(!( (tomMatch542NameNumber_end_15==tomMatch542NameNumber_freshVar_10) ));}}}if ( tomMatch542NameNumber_end_7.isEmptyConcSection() ) {tomMatch542NameNumber_end_7=tomMatch542NameNumber_freshVar_2;} else {tomMatch542NameNumber_end_7= tomMatch542NameNumber_end_7.getTailConcSection() ;}}} while(!( (tomMatch542NameNumber_end_7==tomMatch542NameNumber_freshVar_2) ));}}}}}

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
      {{if ( (sdeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch543NameNumber_end_4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);do {{if (!( tomMatch543NameNumber_end_4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tom_sdecl= tomMatch543NameNumber_end_4.getHeadConcSortDecl() ;

          OperatorDeclList opdecl = operatorsForSort.get(tom_sdecl);
          Sort fullSort =  tom.gom.adt.gom.types.sort.Sort.make(tom_sdecl, opdecl) ;
          if(checkSortValidity(fullSort)) {
            sortList =  tom.gom.adt.gom.types.sortlist.ConsConcSort.make(fullSort,tom_append_list_ConcSort(sortList, tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() )) ;
          }
        }if ( tomMatch543NameNumber_end_4.isEmptyConcSortDecl() ) {tomMatch543NameNumber_end_4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);} else {tomMatch543NameNumber_end_4= tomMatch543NameNumber_end_4.getTailConcSortDecl() ;}}} while(!( (tomMatch543NameNumber_end_4==(( tom.gom.adt.gom.types.SortDeclList )sdeclList)) ));}}}}

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

    {{if ( (prod instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )prod) instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.GomType  tomMatch544NameNumber_freshVar_3= (( tom.gom.adt.gom.types.Production )prod).getCodomain() ; String  tom_name= (( tom.gom.adt.gom.types.Production )prod).getName() ;if ( (tomMatch544NameNumber_freshVar_3 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { tom.gom.adt.gom.types.Option  tom_options= (( tom.gom.adt.gom.types.Production )prod).getOption() ;



        SortDecl codomainSort = declFromTypename( tomMatch544NameNumber_freshVar_3.getName() ,sortDeclList);
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
    {{if ( (sortDeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch545NameNumber_end_4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);do {{if (!( tomMatch545NameNumber_end_4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tomMatch545NameNumber_freshVar_8= tomMatch545NameNumber_end_4.getHeadConcSortDecl() ;if ( (tomMatch545NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        if (typename.equals( tomMatch545NameNumber_freshVar_8.getName() )) {
          return  tomMatch545NameNumber_end_4.getHeadConcSortDecl() ;
        }
      }}if ( tomMatch545NameNumber_end_4.isEmptyConcSortDecl() ) {tomMatch545NameNumber_end_4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);} else {tomMatch545NameNumber_end_4= tomMatch545NameNumber_end_4.getTailConcSortDecl() ;}}} while(!( (tomMatch545NameNumber_end_4==(( tom.gom.adt.gom.types.SortDeclList )sortDeclList)) ));}}}}

    
    getLogger().log(Level.SEVERE, GomMessage.unknownSort.getMessage(),
        new Object[]{typename});
    /* If the sort is not known, assume it is a builtin */
    return  tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl.make(typename) ;
  }

  private TypedProduction typedProduction(FieldList domain, SortDeclList sortDeclList) {
    {{if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )domain).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch546NameNumber_freshVar_5= (( tom.gom.adt.gom.types.FieldList )domain).getHeadConcField() ;if ( (tomMatch546NameNumber_freshVar_5 instanceof tom.gom.adt.gom.types.field.StarredField) ) { tom.gom.adt.gom.types.GomType  tomMatch546NameNumber_freshVar_3= tomMatch546NameNumber_freshVar_5.getFieldType() ;if ( (tomMatch546NameNumber_freshVar_3 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {if (  (( tom.gom.adt.gom.types.FieldList )domain).getTailConcField() .isEmptyConcField() ) {

        return  tom.gom.adt.gom.types.typedproduction.Variadic.make(declFromTypename( tomMatch546NameNumber_freshVar_3.getName() ,sortDeclList)) ;
      }}}}}}}{if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {

        return  tom.gom.adt.gom.types.typedproduction.Slots.make(typedSlotList((( tom.gom.adt.gom.types.FieldList )domain),sortDeclList)) ;
      }}}}

    // the error message could be more refined
    throw new GomRuntimeException("TypeExpander::typedProduction: illformed Production");
  }

  private SlotList typedSlotList(FieldList fields, SortDeclList sortDeclList) {
    {{if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if ( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() ) {

        return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
      }}}}{if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch547NameNumber_freshVar_9= (( tom.gom.adt.gom.types.FieldList )fields).getHeadConcField() ;if ( (tomMatch547NameNumber_freshVar_9 instanceof tom.gom.adt.gom.types.field.NamedField) ) { tom.gom.adt.gom.types.GomType  tomMatch547NameNumber_freshVar_8= tomMatch547NameNumber_freshVar_9.getFieldType() ;if ( (tomMatch547NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {

        SlotList newtail = typedSlotList( (( tom.gom.adt.gom.types.FieldList )fields).getTailConcField() ,sortDeclList);
        return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch547NameNumber_freshVar_9.getName() , declFromTypename( tomMatch547NameNumber_freshVar_8.getName() ,sortDeclList)) ,tom_append_list_ConcSlot(newtail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;
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
    {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch548NameNumber_freshVar_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch548NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch548NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch548NameNumber_end_7=tomMatch548NameNumber_freshVar_2;do {{if (!( tomMatch548NameNumber_end_7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch548NameNumber_freshVar_11= tomMatch548NameNumber_end_7.getHeadConcSection() ;if ( (tomMatch548NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch548NameNumber_freshVar_10= tomMatch548NameNumber_freshVar_11.getGrammarList() ;if ( ((tomMatch548NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch548NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch548NameNumber_end_15=tomMatch548NameNumber_freshVar_10;do {{if (!( tomMatch548NameNumber_end_15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch548NameNumber_freshVar_19= tomMatch548NameNumber_end_15.getHeadConcGrammar() ;if ( (tomMatch548NameNumber_freshVar_19 instanceof tom.gom.adt.gom.types.grammar.Sorts) ) { tom.gom.adt.gom.types.GomTypeList  tomMatch548NameNumber_freshVar_18= tomMatch548NameNumber_freshVar_19.getTypeList() ;if ( ((tomMatch548NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.gomtypelist.ConsConcGomType) || (tomMatch548NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.gomtypelist.EmptyConcGomType)) ) { tom.gom.adt.gom.types.GomTypeList  tomMatch548NameNumber_end_23=tomMatch548NameNumber_freshVar_18;do {{if (!( tomMatch548NameNumber_end_23.isEmptyConcGomType() )) { tom.gom.adt.gom.types.GomType  tomMatch548NameNumber_freshVar_28= tomMatch548NameNumber_end_23.getHeadConcGomType() ;if ( (tomMatch548NameNumber_freshVar_28 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch548NameNumber_freshVar_28.getName() ;



        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          getLogger().log(Level.SEVERE, GomMessage.operatorOnBuiltin.getMessage(),
            new Object[]{(tom_typeName)});
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}if ( tomMatch548NameNumber_end_23.isEmptyConcGomType() ) {tomMatch548NameNumber_end_23=tomMatch548NameNumber_freshVar_18;} else {tomMatch548NameNumber_end_23= tomMatch548NameNumber_end_23.getTailConcGomType() ;}}} while(!( (tomMatch548NameNumber_end_23==tomMatch548NameNumber_freshVar_18) ));}}}if ( tomMatch548NameNumber_end_15.isEmptyConcGrammar() ) {tomMatch548NameNumber_end_15=tomMatch548NameNumber_freshVar_10;} else {tomMatch548NameNumber_end_15= tomMatch548NameNumber_end_15.getTailConcGrammar() ;}}} while(!( (tomMatch548NameNumber_end_15==tomMatch548NameNumber_freshVar_10) ));}}}if ( tomMatch548NameNumber_end_7.isEmptyConcSection() ) {tomMatch548NameNumber_end_7=tomMatch548NameNumber_freshVar_2;} else {tomMatch548NameNumber_end_7= tomMatch548NameNumber_end_7.getTailConcSection() ;}}} while(!( (tomMatch548NameNumber_end_7==tomMatch548NameNumber_freshVar_2) ));}}}}}{{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch549NameNumber_freshVar_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch549NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch549NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch549NameNumber_end_7=tomMatch549NameNumber_freshVar_2;do {{if (!( tomMatch549NameNumber_end_7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch549NameNumber_freshVar_11= tomMatch549NameNumber_end_7.getHeadConcSection() ;if ( (tomMatch549NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch549NameNumber_freshVar_10= tomMatch549NameNumber_freshVar_11.getGrammarList() ;if ( ((tomMatch549NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch549NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch549NameNumber_end_15=tomMatch549NameNumber_freshVar_10;do {{if (!( tomMatch549NameNumber_end_15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch549NameNumber_freshVar_19= tomMatch549NameNumber_end_15.getHeadConcGrammar() ;if ( (tomMatch549NameNumber_freshVar_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch549NameNumber_freshVar_18= tomMatch549NameNumber_freshVar_19.getProductionList() ;if ( ((tomMatch549NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch549NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch549NameNumber_end_23=tomMatch549NameNumber_freshVar_18;do {{if (!( tomMatch549NameNumber_end_23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch549NameNumber_freshVar_27= tomMatch549NameNumber_end_23.getHeadConcProduction() ;if ( (tomMatch549NameNumber_freshVar_27 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch549NameNumber_freshVar_26= tomMatch549NameNumber_freshVar_27.getType() ;if ( (tomMatch549NameNumber_freshVar_26 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch549NameNumber_freshVar_26.getName() ;







        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          getLogger().log(Level.SEVERE, GomMessage.operatorOnBuiltin.getMessage(),
            new Object[]{(tom_typeName)});
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}}if ( tomMatch549NameNumber_end_23.isEmptyConcProduction() ) {tomMatch549NameNumber_end_23=tomMatch549NameNumber_freshVar_18;} else {tomMatch549NameNumber_end_23= tomMatch549NameNumber_end_23.getTailConcProduction() ;}}} while(!( (tomMatch549NameNumber_end_23==tomMatch549NameNumber_freshVar_18) ));}}}if ( tomMatch549NameNumber_end_15.isEmptyConcGrammar() ) {tomMatch549NameNumber_end_15=tomMatch549NameNumber_freshVar_10;} else {tomMatch549NameNumber_end_15= tomMatch549NameNumber_end_15.getTailConcGrammar() ;}}} while(!( (tomMatch549NameNumber_end_15==tomMatch549NameNumber_freshVar_10) ));}}}if ( tomMatch549NameNumber_end_7.isEmptyConcSection() ) {tomMatch549NameNumber_end_7=tomMatch549NameNumber_freshVar_2;} else {tomMatch549NameNumber_end_7= tomMatch549NameNumber_end_7.getTailConcSection() ;}}} while(!( (tomMatch549NameNumber_end_7==tomMatch549NameNumber_freshVar_2) ));}}}}}

    return result;
  }

  /*
   * Get all sort uses in a module (as codomain of an operator)
   */
  private Collection<SortDecl> getSortDeclarationInCodomain(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    {{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch550NameNumber_freshVar_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch550NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch550NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch550NameNumber_end_7=tomMatch550NameNumber_freshVar_2;do {{if (!( tomMatch550NameNumber_end_7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch550NameNumber_freshVar_11= tomMatch550NameNumber_end_7.getHeadConcSection() ;if ( (tomMatch550NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch550NameNumber_freshVar_10= tomMatch550NameNumber_freshVar_11.getGrammarList() ;if ( ((tomMatch550NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch550NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch550NameNumber_end_15=tomMatch550NameNumber_freshVar_10;do {{if (!( tomMatch550NameNumber_end_15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch550NameNumber_freshVar_19= tomMatch550NameNumber_end_15.getHeadConcGrammar() ;if ( (tomMatch550NameNumber_freshVar_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch550NameNumber_freshVar_18= tomMatch550NameNumber_freshVar_19.getProductionList() ;if ( ((tomMatch550NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch550NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch550NameNumber_end_23=tomMatch550NameNumber_freshVar_18;do {{if (!( tomMatch550NameNumber_end_23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch550NameNumber_freshVar_30= tomMatch550NameNumber_end_23.getHeadConcProduction() ;if ( (tomMatch550NameNumber_freshVar_30 instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.GomType  tomMatch550NameNumber_freshVar_28= tomMatch550NameNumber_freshVar_30.getCodomain() ;if ( (tomMatch550NameNumber_freshVar_28 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch550NameNumber_freshVar_28.getName() ;











        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}}if ( tomMatch550NameNumber_end_23.isEmptyConcProduction() ) {tomMatch550NameNumber_end_23=tomMatch550NameNumber_freshVar_18;} else {tomMatch550NameNumber_end_23= tomMatch550NameNumber_end_23.getTailConcProduction() ;}}} while(!( (tomMatch550NameNumber_end_23==tomMatch550NameNumber_freshVar_18) ));}}}if ( tomMatch550NameNumber_end_15.isEmptyConcGrammar() ) {tomMatch550NameNumber_end_15=tomMatch550NameNumber_freshVar_10;} else {tomMatch550NameNumber_end_15= tomMatch550NameNumber_end_15.getTailConcGrammar() ;}}} while(!( (tomMatch550NameNumber_end_15==tomMatch550NameNumber_freshVar_10) ));}}}if ( tomMatch550NameNumber_end_7.isEmptyConcSection() ) {tomMatch550NameNumber_end_7=tomMatch550NameNumber_freshVar_2;} else {tomMatch550NameNumber_end_7= tomMatch550NameNumber_end_7.getTailConcSection() ;}}} while(!( (tomMatch550NameNumber_end_7==tomMatch550NameNumber_freshVar_2) ));}}}}}{{if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch551NameNumber_freshVar_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom_moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( ((tomMatch551NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch551NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch551NameNumber_end_7=tomMatch551NameNumber_freshVar_2;do {{if (!( tomMatch551NameNumber_end_7.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch551NameNumber_freshVar_11= tomMatch551NameNumber_end_7.getHeadConcSection() ;if ( (tomMatch551NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch551NameNumber_freshVar_10= tomMatch551NameNumber_freshVar_11.getGrammarList() ;if ( ((tomMatch551NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch551NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch551NameNumber_end_15=tomMatch551NameNumber_freshVar_10;do {{if (!( tomMatch551NameNumber_end_15.isEmptyConcGrammar() )) { tom.gom.adt.gom.types.Grammar  tomMatch551NameNumber_freshVar_19= tomMatch551NameNumber_end_15.getHeadConcGrammar() ;if ( (tomMatch551NameNumber_freshVar_19 instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch551NameNumber_freshVar_18= tomMatch551NameNumber_freshVar_19.getProductionList() ;if ( ((tomMatch551NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch551NameNumber_freshVar_18 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch551NameNumber_end_23=tomMatch551NameNumber_freshVar_18;do {{if (!( tomMatch551NameNumber_end_23.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch551NameNumber_freshVar_27= tomMatch551NameNumber_end_23.getHeadConcProduction() ;if ( (tomMatch551NameNumber_freshVar_27 instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.ProductionList  tomMatch551NameNumber_freshVar_26= tomMatch551NameNumber_freshVar_27.getProductionList() ;if ( ((tomMatch551NameNumber_freshVar_26 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch551NameNumber_freshVar_26 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch551NameNumber_end_31=tomMatch551NameNumber_freshVar_26;do {{if (!( tomMatch551NameNumber_end_31.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch551NameNumber_freshVar_38= tomMatch551NameNumber_end_31.getHeadConcProduction() ;if ( (tomMatch551NameNumber_freshVar_38 instanceof tom.gom.adt.gom.types.production.Production) ) { tom.gom.adt.gom.types.GomType  tomMatch551NameNumber_freshVar_36= tomMatch551NameNumber_freshVar_38.getCodomain() ;if ( (tomMatch551NameNumber_freshVar_36 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_typeName= tomMatch551NameNumber_freshVar_36.getName() ;













        if (getGomEnvironment().isBuiltinSort(tom_typeName)) {
          result.add(getGomEnvironment().builtinSort(tom_typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom_typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ) );
        }
      }}}if ( tomMatch551NameNumber_end_31.isEmptyConcProduction() ) {tomMatch551NameNumber_end_31=tomMatch551NameNumber_freshVar_26;} else {tomMatch551NameNumber_end_31= tomMatch551NameNumber_end_31.getTailConcProduction() ;}}} while(!( (tomMatch551NameNumber_end_31==tomMatch551NameNumber_freshVar_26) ));}}}if ( tomMatch551NameNumber_end_23.isEmptyConcProduction() ) {tomMatch551NameNumber_end_23=tomMatch551NameNumber_freshVar_18;} else {tomMatch551NameNumber_end_23= tomMatch551NameNumber_end_23.getTailConcProduction() ;}}} while(!( (tomMatch551NameNumber_end_23==tomMatch551NameNumber_freshVar_18) ));}}}if ( tomMatch551NameNumber_end_15.isEmptyConcGrammar() ) {tomMatch551NameNumber_end_15=tomMatch551NameNumber_freshVar_10;} else {tomMatch551NameNumber_end_15= tomMatch551NameNumber_end_15.getTailConcGrammar() ;}}} while(!( (tomMatch551NameNumber_end_15==tomMatch551NameNumber_freshVar_10) ));}}}if ( tomMatch551NameNumber_end_7.isEmptyConcSection() ) {tomMatch551NameNumber_end_7=tomMatch551NameNumber_freshVar_2;} else {tomMatch551NameNumber_end_7= tomMatch551NameNumber_end_7.getTailConcSection() ;}}} while(!( (tomMatch551NameNumber_end_7==tomMatch551NameNumber_freshVar_2) ));}}}}}

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
        {{if ( (tom_sectionList instanceof tom.gom.adt.gom.types.SectionList) ) {if ( (((( tom.gom.adt.gom.types.SectionList )tom_sectionList) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tom_sectionList) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch553NameNumber_end_4=(( tom.gom.adt.gom.types.SectionList )tom_sectionList);do {{if (!( tomMatch553NameNumber_end_4.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch553NameNumber_freshVar_8= tomMatch553NameNumber_end_4.getHeadConcSection() ;if ( (tomMatch553NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.section.Imports) ) { tom.gom.adt.gom.types.ImportList  tomMatch553NameNumber_freshVar_7= tomMatch553NameNumber_freshVar_8.getImportList() ;if ( ((tomMatch553NameNumber_freshVar_7 instanceof tom.gom.adt.gom.types.importlist.ConsConcImportedModule) || (tomMatch553NameNumber_freshVar_7 instanceof tom.gom.adt.gom.types.importlist.EmptyConcImportedModule)) ) { tom.gom.adt.gom.types.ImportList  tomMatch553NameNumber_end_12=tomMatch553NameNumber_freshVar_7;do {{if (!( tomMatch553NameNumber_end_12.isEmptyConcImportedModule() )) { tom.gom.adt.gom.types.ImportedModule  tomMatch553NameNumber_freshVar_16= tomMatch553NameNumber_end_12.getHeadConcImportedModule() ;if ( (tomMatch553NameNumber_freshVar_16 instanceof tom.gom.adt.gom.types.importedmodule.Import) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch553NameNumber_freshVar_15= tomMatch553NameNumber_freshVar_16.getModuleName() ;if ( (tomMatch553NameNumber_freshVar_15 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {





            if (!getGomEnvironment().isBuiltin( tomMatch553NameNumber_freshVar_15.getName() )) {
              imports.add(tomMatch553NameNumber_freshVar_15);
            }
          }}}if ( tomMatch553NameNumber_end_12.isEmptyConcImportedModule() ) {tomMatch553NameNumber_end_12=tomMatch553NameNumber_freshVar_7;} else {tomMatch553NameNumber_end_12= tomMatch553NameNumber_end_12.getTailConcImportedModule() ;}}} while(!( (tomMatch553NameNumber_end_12==tomMatch553NameNumber_freshVar_7) ));}}}if ( tomMatch553NameNumber_end_4.isEmptyConcSection() ) {tomMatch553NameNumber_end_4=(( tom.gom.adt.gom.types.SectionList )tom_sectionList);} else {tomMatch553NameNumber_end_4= tomMatch553NameNumber_end_4.getTailConcSection() ;}}} while(!( (tomMatch553NameNumber_end_4==(( tom.gom.adt.gom.types.SectionList )tom_sectionList)) ));}}}}

      }}}}

    return imports;
  }

  private GomModule getModule(GomModuleName modname, GomModuleList list) {
    {{if ( (list instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch554NameNumber_end_4=(( tom.gom.adt.gom.types.GomModuleList )list);do {{if (!( tomMatch554NameNumber_end_4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch554NameNumber_freshVar_8= tomMatch554NameNumber_end_4.getHeadConcGomModule() ;if ( (tomMatch554NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) {

        if ( tomMatch554NameNumber_freshVar_8.getModuleName() .equals(modname)) {
          return  tomMatch554NameNumber_end_4.getHeadConcGomModule() ;
        }
      }}if ( tomMatch554NameNumber_end_4.isEmptyConcGomModule() ) {tomMatch554NameNumber_end_4=(( tom.gom.adt.gom.types.GomModuleList )list);} else {tomMatch554NameNumber_end_4= tomMatch554NameNumber_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch554NameNumber_end_4==(( tom.gom.adt.gom.types.GomModuleList )list)) ));}}}}

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
    {{if ( (moduleList instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch555NameNumber_end_4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);do {{if (!( tomMatch555NameNumber_end_4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch555NameNumber_freshVar_8= tomMatch555NameNumber_end_4.getHeadConcGomModule() ;if ( (tomMatch555NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tom_moduleName= tomMatch555NameNumber_freshVar_8.getModuleName() ;

        ModuleDeclList importsModuleDeclList =  tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() ;
        Iterator it = getTransitiveClosureImports( tomMatch555NameNumber_end_4.getHeadConcGomModule() ,moduleList).iterator();
        while(it.hasNext()) {
          GomModuleName importedModuleName = (GomModuleName) it.next();
          importsModuleDeclList = 
             tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(importedModuleName, getStreamManager().getPackagePath(importedModuleName.getName())) ,tom_append_list_ConcModuleDecl(importsModuleDeclList, tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() )) 
;
        }
        getGomEnvironment().addModuleDependency(
             tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom_moduleName, getStreamManager().getPackagePath(tom_moduleName.getName())) ,importsModuleDeclList);
      }}if ( tomMatch555NameNumber_end_4.isEmptyConcGomModule() ) {tomMatch555NameNumber_end_4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);} else {tomMatch555NameNumber_end_4= tomMatch555NameNumber_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch555NameNumber_end_4==(( tom.gom.adt.gom.types.GomModuleList )moduleList)) ));}}}}

  }

  private boolean checkSortValidity(Sort sort) {
    boolean valid = true;
    // check if the same slot name is used with different types
    Map<String,SortDecl> mapNameType = new HashMap<String,SortDecl>();
    {{if ( (sort instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )sort) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch556NameNumber_freshVar_1= (( tom.gom.adt.gom.types.Sort )sort).getDecl() ; tom.gom.adt.gom.types.OperatorDeclList  tomMatch556NameNumber_freshVar_2= (( tom.gom.adt.gom.types.Sort )sort).getOperatorDecls() ;boolean tomMatch556NameNumber_freshVar_25= false ; String  tomMatch556NameNumber_freshVar_4= "" ;if ( (tomMatch556NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {{tomMatch556NameNumber_freshVar_25= true ;tomMatch556NameNumber_freshVar_4= tomMatch556NameNumber_freshVar_1.getName() ;}} else {if ( (tomMatch556NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {{tomMatch556NameNumber_freshVar_25= true ;tomMatch556NameNumber_freshVar_4= tomMatch556NameNumber_freshVar_1.getName() ;}}}if ( tomMatch556NameNumber_freshVar_25== true  ) {if ( ((tomMatch556NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || (tomMatch556NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch556NameNumber_end_9=tomMatch556NameNumber_freshVar_2;do {{if (!( tomMatch556NameNumber_end_9.isEmptyConcOperator() )) { tom.gom.adt.gom.types.OperatorDecl  tomMatch556NameNumber_freshVar_13= tomMatch556NameNumber_end_9.getHeadConcOperator() ;if ( (tomMatch556NameNumber_freshVar_13 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch556NameNumber_freshVar_12= tomMatch556NameNumber_freshVar_13.getProd() ;if ( (tomMatch556NameNumber_freshVar_12 instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tomMatch556NameNumber_freshVar_14= tomMatch556NameNumber_freshVar_12.getSlots() ;if ( ((tomMatch556NameNumber_freshVar_14 instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || (tomMatch556NameNumber_freshVar_14 instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) { tom.gom.adt.gom.types.SlotList  tomMatch556NameNumber_end_19=tomMatch556NameNumber_freshVar_14;do {{if (!( tomMatch556NameNumber_end_19.isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch556NameNumber_freshVar_24= tomMatch556NameNumber_end_19.getHeadConcSlot() ;if ( (tomMatch556NameNumber_freshVar_24 instanceof tom.gom.adt.gom.types.slot.Slot) ) { String  tom_slotName= tomMatch556NameNumber_freshVar_24.getName() ; tom.gom.adt.gom.types.SortDecl  tom_slotSort= tomMatch556NameNumber_freshVar_24.getSort() ;






        if(!mapNameType.containsKey(tom_slotName)) {
          mapNameType.put(tom_slotName,tom_slotSort);
        } else {
          SortDecl prevSort = mapNameType.get(tom_slotName);
          if (!prevSort.equals(tom_slotSort)) {
            getLogger().log(Level.SEVERE,
                GomMessage.slotIncompatibleTypes.getMessage(),
                new Object[]{tomMatch556NameNumber_freshVar_4,tom_slotName,prevSort.getName(),
                             (tom_slotSort).getName()});
            valid = false;
          }
        }
      }}if ( tomMatch556NameNumber_end_19.isEmptyConcSlot() ) {tomMatch556NameNumber_end_19=tomMatch556NameNumber_freshVar_14;} else {tomMatch556NameNumber_end_19= tomMatch556NameNumber_end_19.getTailConcSlot() ;}}} while(!( (tomMatch556NameNumber_end_19==tomMatch556NameNumber_freshVar_14) ));}}}}if ( tomMatch556NameNumber_end_9.isEmptyConcOperator() ) {tomMatch556NameNumber_end_9=tomMatch556NameNumber_freshVar_2;} else {tomMatch556NameNumber_end_9= tomMatch556NameNumber_end_9.getTailConcOperator() ;}}} while(!( (tomMatch556NameNumber_end_9==tomMatch556NameNumber_freshVar_2) ));}}}}}}

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
