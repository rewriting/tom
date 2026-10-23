/*
 *
 * GOM
 *
 * Copyright (c) 2006-2017, Universite de Lorraine, Inria
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
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.adt.gom.types.gommodulelist.ConcGomModule;
import tom.gom.tools.error.GomRuntimeException;

public class TypeExpander {

     private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {     if( l1.isEmptyConcModule() ) {       return l2;     } else if( l2.isEmptyConcModule() ) {       return l1;     } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.AlternativeList  tom_append_list_ConcAlternative( tom.gom.adt.gom.types.AlternativeList l1,  tom.gom.adt.gom.types.AlternativeList  l2) {     if( l1.isEmptyConcAlternative() ) {       return l2;     } else if( l2.isEmptyConcAlternative() ) {       return l1;     } else if(  l1.getTailConcAlternative() .isEmptyConcAlternative() ) {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,l2) ;     } else {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,tom_append_list_ConcAlternative( l1.getTailConcAlternative() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AlternativeList  tom_get_slice_ConcAlternative( tom.gom.adt.gom.types.AlternativeList  begin,  tom.gom.adt.gom.types.AlternativeList  end, tom.gom.adt.gom.types.AlternativeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAlternative()  ||  (end== tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( begin.getHeadConcAlternative() ,( tom.gom.adt.gom.types.AlternativeList )tom_get_slice_ConcAlternative( begin.getTailConcAlternative() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleDeclList  tom_append_list_ConcModuleDecl( tom.gom.adt.gom.types.ModuleDeclList l1,  tom.gom.adt.gom.types.ModuleDeclList  l2) {     if( l1.isEmptyConcModuleDecl() ) {       return l2;     } else if( l2.isEmptyConcModuleDecl() ) {       return l1;     } else if(  l1.getTailConcModuleDecl() .isEmptyConcModuleDecl() ) {       return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( l1.getHeadConcModuleDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( l1.getHeadConcModuleDecl() ,tom_append_list_ConcModuleDecl( l1.getTailConcModuleDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleDeclList  tom_get_slice_ConcModuleDecl( tom.gom.adt.gom.types.ModuleDeclList  begin,  tom.gom.adt.gom.types.ModuleDeclList  end, tom.gom.adt.gom.types.ModuleDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModuleDecl()  ||  (end== tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( begin.getHeadConcModuleDecl() ,( tom.gom.adt.gom.types.ModuleDeclList )tom_get_slice_ConcModuleDecl( begin.getTailConcModuleDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortDeclList  tom_append_list_ConcSortDecl( tom.gom.adt.gom.types.SortDeclList l1,  tom.gom.adt.gom.types.SortDeclList  l2) {     if( l1.isEmptyConcSortDecl() ) {       return l2;     } else if( l2.isEmptyConcSortDecl() ) {       return l1;     } else if(  l1.getTailConcSortDecl() .isEmptyConcSortDecl() ) {       return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( l1.getHeadConcSortDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( l1.getHeadConcSortDecl() ,tom_append_list_ConcSortDecl( l1.getTailConcSortDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortDeclList  tom_get_slice_ConcSortDecl( tom.gom.adt.gom.types.SortDeclList  begin,  tom.gom.adt.gom.types.SortDeclList  end, tom.gom.adt.gom.types.SortDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSortDecl()  ||  (end== tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( begin.getHeadConcSortDecl() ,( tom.gom.adt.gom.types.SortDeclList )tom_get_slice_ConcSortDecl( begin.getTailConcSortDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ImportList  tom_append_list_ConcImportedModule( tom.gom.adt.gom.types.ImportList l1,  tom.gom.adt.gom.types.ImportList  l2) {     if( l1.isEmptyConcImportedModule() ) {       return l2;     } else if( l2.isEmptyConcImportedModule() ) {       return l1;     } else if(  l1.getTailConcImportedModule() .isEmptyConcImportedModule() ) {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,tom_append_list_ConcImportedModule( l1.getTailConcImportedModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ImportList  tom_get_slice_ConcImportedModule( tom.gom.adt.gom.types.ImportList  begin,  tom.gom.adt.gom.types.ImportList  end, tom.gom.adt.gom.types.ImportList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcImportedModule()  ||  (end== tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( begin.getHeadConcImportedModule() ,( tom.gom.adt.gom.types.ImportList )tom_get_slice_ConcImportedModule( begin.getTailConcImportedModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {     if( l1.isEmptyConcSort() ) {       return l2;     } else if( l2.isEmptyConcSort() ) {       return l1;     } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.OperatorDeclList  tom_append_list_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList l1,  tom.gom.adt.gom.types.OperatorDeclList  l2) {     if( l1.isEmptyConcOperator() ) {       return l2;     } else if( l2.isEmptyConcOperator() ) {       return l1;     } else if(  l1.getTailConcOperator() .isEmptyConcOperator() ) {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,l2) ;     } else {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,tom_append_list_ConcOperator( l1.getTailConcOperator() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.OperatorDeclList  tom_get_slice_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList  begin,  tom.gom.adt.gom.types.OperatorDeclList  end, tom.gom.adt.gom.types.OperatorDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcOperator()  ||  (end== tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( begin.getHeadConcOperator() ,( tom.gom.adt.gom.types.OperatorDeclList )tom_get_slice_ConcOperator( begin.getTailConcOperator() ,end,tail)) ;   }   



  private final GomEnvironment gomEnvironment;

  public TypeExpander(GomEnvironment gomEnvironment) {
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
  public ModuleList expand(GomModuleList gomModuleList) {
    if (!(gomModuleList instanceof ConcGomModule)) {
      throw new RuntimeException("A GomModuleList should be a list");
    }
    ConcGomModule moduleList = (ConcGomModule) gomModuleList;

    /* put a map giving all imported modules for each module in the path */
    buildDependencyMap(moduleList);

    /* collect all sort declarations */
    SortDeclList sortDeclList =  tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() ;
    /* The sorts declared in each module */
    Map<ModuleDecl,SortDeclList> sortsForModule =
      new HashMap<ModuleDecl,SortDeclList>();
    for (GomModule module : moduleList) {
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
        GomMessage.warning(getLogger(),null,0,
            GomMessage.undeclaredSorts,
            new Object[]{showSortList(undeclaredSorts)});
      }
      if(!implicitdecls.containsAll(decls)) {
        // whine about sorts without operators: this is a real error
        Collection<SortDecl> emptySorts = new HashSet<SortDecl>();
        emptySorts.addAll(decls);
        emptySorts.removeAll(implicitdecls);
        GomMessage.error(getLogger(),null,0,
            GomMessage.emptySorts,
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
    for (GomModule module : moduleList) {
      // iterate through the productions
      { /* unamed block */{ /* unamed block */if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch768_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch768_2) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch768_2) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch768_end_8=tomMatch768_2;do {{ /* unamed block */if (!( tomMatch768_end_8.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch768_12= tomMatch768_end_8.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch768_12) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.ProductionList  tomMatch768_11= tomMatch768_12.getProductionList() ;if ( (((( tom.gom.adt.gom.types.ProductionList )tomMatch768_11) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tomMatch768_11) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch768_end_17=tomMatch768_11;do {{ /* unamed block */if (!( tomMatch768_end_17.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch768_21= tomMatch768_end_17.getHeadConcProduction() ;if ( ((( tom.gom.adt.gom.types.Production )tomMatch768_21) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch768_20= tomMatch768_21.getAlternativeList() ;if ( (((( tom.gom.adt.gom.types.AlternativeList )tomMatch768_20) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )tomMatch768_20) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch768_end_26=tomMatch768_20;do {{ /* unamed block */if (!( tomMatch768_end_26.isEmptyConcAlternative() )) {if ( ((( tom.gom.adt.gom.types.Alternative ) tomMatch768_end_26.getHeadConcAlternative() ) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) {







          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl( tomMatch768_end_26.getHeadConcAlternative() ,sortDeclList,operatorsForSort);

        }}if ( tomMatch768_end_26.isEmptyConcAlternative() ) {tomMatch768_end_26=tomMatch768_20;} else {tomMatch768_end_26= tomMatch768_end_26.getTailConcAlternative() ;}}} while(!( (tomMatch768_end_26==tomMatch768_20) ));}}}if ( tomMatch768_end_17.isEmptyConcProduction() ) {tomMatch768_end_17=tomMatch768_11;} else {tomMatch768_end_17= tomMatch768_end_17.getTailConcProduction() ;}}} while(!( (tomMatch768_end_17==tomMatch768_11) ));}}}if ( tomMatch768_end_8.isEmptyConcSection() ) {tomMatch768_end_8=tomMatch768_2;} else {tomMatch768_end_8= tomMatch768_end_8.getTailConcSection() ;}}} while(!( (tomMatch768_end_8==tomMatch768_2) ));}}}}}



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
      { /* unamed block */{ /* unamed block */if ( (sdeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch769_end_4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);do {{ /* unamed block */if (!( tomMatch769_end_4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tom___sdecl= tomMatch769_end_4.getHeadConcSortDecl() ;


          OperatorDeclList opdecl = operatorsForSort.get(tom___sdecl);
          Sort fullSort =  tom.gom.adt.gom.types.sort.Sort.make(tom___sdecl, opdecl) ;
          if(checkSortValidity(fullSort)) {
            sortList =  tom.gom.adt.gom.types.sortlist.ConsConcSort.make(fullSort,tom_append_list_ConcSort(sortList, tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() )) ;
          }
        }if ( tomMatch769_end_4.isEmptyConcSortDecl() ) {tomMatch769_end_4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);} else {tomMatch769_end_4= tomMatch769_end_4.getTailConcSortDecl() ;}}} while(!( (tomMatch769_end_4==(( tom.gom.adt.gom.types.SortDeclList )sdeclList)) ));}}}}


      resultModuleList =  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( tom.gom.adt.gom.types.module.Module.make(mdecl, sortList) ,tom_append_list_ConcModule(resultModuleList, tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() )) 

;
    }
    return resultModuleList;
  }

  /*
   * Get an OperatorDecl from an Alternative, using the list of sort declarations
   * XXX: There is huge room for efficiency improvement, as we could use a map
   * sortName -> sortDeclList instead of a simple list
   */
  private OperatorDecl getOperatorDecl(Alternative alt,
      SortDeclList sortDeclList,
      Map<SortDecl,OperatorDeclList> operatorsForSort) {

    { /* unamed block */{ /* unamed block */if ( (alt instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )alt) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { tom.gom.adt.gom.types.GomType  tomMatch770_3= (( tom.gom.adt.gom.types.Alternative )alt).getCodomain() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch770_3) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {



        SortDecl codomainSort = declFromTypename( tomMatch770_3.getName() ,sortDeclList);
        TypedProduction domainSorts = typedProduction( (( tom.gom.adt.gom.types.Alternative )alt).getDomainList() ,sortDeclList);

        OperatorDecl decl =  tom.gom.adt.gom.types.operatordecl.OperatorDecl.make( (( tom.gom.adt.gom.types.Alternative )alt).getName() , codomainSort, domainSorts,  (( tom.gom.adt.gom.types.Alternative )alt).getOption() ) ;
        if (operatorsForSort.containsKey(codomainSort)) {
          OperatorDeclList list = operatorsForSort.get(codomainSort);
          operatorsForSort.put(codomainSort, tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make(decl,tom_append_list_ConcOperator(list, tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() )) );
        } else {
          operatorsForSort.put(codomainSort, tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make(decl, tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) );
        }
        return decl;
      }}}}}



    throw new GomRuntimeException(
        "TypeExpander::getOperatorDecl: wrong Alternative?");
  }

  private SortDecl declFromTypename(String typename,
                                    SortDeclList sortDeclList) {
    if (getGomEnvironment().isBuiltinSort(typename)) {
      return getGomEnvironment().builtinSort(typename);
    }
    { /* unamed block */{ /* unamed block */if ( (sortDeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch771_end_4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);do {{ /* unamed block */if (!( tomMatch771_end_4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tomMatch771_8= tomMatch771_end_4.getHeadConcSortDecl() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch771_8) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {


        if (typename.equals( tomMatch771_8.getName() )) {
          return  tomMatch771_end_4.getHeadConcSortDecl() ;
        }
      }}if ( tomMatch771_end_4.isEmptyConcSortDecl() ) {tomMatch771_end_4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);} else {tomMatch771_end_4= tomMatch771_end_4.getTailConcSortDecl() ;}}} while(!( (tomMatch771_end_4==(( tom.gom.adt.gom.types.SortDeclList )sortDeclList)) ));}}}}



    GomMessage.error(getLogger(),null,0,
        GomMessage.unknownSort,
        new Object[]{typename});
    /* If the sort is not known, assume it is a builtin */
    return  tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl.make(typename) ;
  }

  private TypedProduction typedProduction(FieldList domain, SortDeclList sortDeclList) {
    { /* unamed block */{ /* unamed block */if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )domain).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch772_5= (( tom.gom.adt.gom.types.FieldList )domain).getHeadConcField() ;if ( ((( tom.gom.adt.gom.types.Field )tomMatch772_5) instanceof tom.gom.adt.gom.types.field.StarredField) ) { tom.gom.adt.gom.types.GomType  tomMatch772_3= tomMatch772_5.getFieldType() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch772_3) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {if (  (( tom.gom.adt.gom.types.FieldList )domain).getTailConcField() .isEmptyConcField() ) {


        return  tom.gom.adt.gom.types.typedproduction.Variadic.make(declFromTypename( tomMatch772_3.getName() ,sortDeclList)) ;
      }}}}}}}{ /* unamed block */if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {


        return  tom.gom.adt.gom.types.typedproduction.Slots.make(typedSlotList((( tom.gom.adt.gom.types.FieldList )domain),sortDeclList)) ;
      }}}}


    throw new GomRuntimeException("TypeExpander::typedProduction: illformed Alternative");
  }

  private SlotList typedSlotList(FieldList fields, SortDeclList sortDeclList) {
    { /* unamed block */{ /* unamed block */if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if ( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() ) {


        return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
      }}}}{ /* unamed block */if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch773_9= (( tom.gom.adt.gom.types.FieldList )fields).getHeadConcField() ;if ( ((( tom.gom.adt.gom.types.Field )tomMatch773_9) instanceof tom.gom.adt.gom.types.field.NamedField) ) { tom.gom.adt.gom.types.GomType  tomMatch773_7= tomMatch773_9.getFieldType() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch773_7) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {


        SlotList newtail = typedSlotList( (( tom.gom.adt.gom.types.FieldList )fields).getTailConcField() ,sortDeclList);
        return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch773_9.getName() , declFromTypename( tomMatch773_7.getName() ,sortDeclList)) ,tom_append_list_ConcSlot(newtail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;
      }}}}}}}


    GomMessage.error(getLogger(),null,0,
        GomMessage.malformedProduction,
        new Object[]{fields.toString()});
    return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
  }

  /*
   * Get all sort declarations in a module
   */
  private Collection<SortDecl> getSortDeclarations(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    { /* unamed block */{ /* unamed block */if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch774_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom___moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch774_2) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch774_2) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch774_end_8=tomMatch774_2;do {{ /* unamed block */if (!( tomMatch774_end_8.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch774_12= tomMatch774_end_8.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch774_12) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.ProductionList  tomMatch774_11= tomMatch774_12.getProductionList() ;if ( (((( tom.gom.adt.gom.types.ProductionList )tomMatch774_11) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tomMatch774_11) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch774_end_17=tomMatch774_11;do {{ /* unamed block */if (!( tomMatch774_end_17.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch774_21= tomMatch774_end_17.getHeadConcProduction() ;if ( ((( tom.gom.adt.gom.types.Production )tomMatch774_21) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch774_20= tomMatch774_21.getType() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch774_20) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom___typeName= tomMatch774_20.getName() ;






        if (getGomEnvironment().isBuiltinSort(tom___typeName)) {
          GomMessage.error(getLogger(),null,0,
              GomMessage.operatorOnBuiltin,
              new Object[]{(tom___typeName)});
          result.add(getGomEnvironment().builtinSort(tom___typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom___typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom___moduleName, getStreamManager().getPackagePath(tom___moduleName.getName())) ) );
        }
      }}}if ( tomMatch774_end_17.isEmptyConcProduction() ) {tomMatch774_end_17=tomMatch774_11;} else {tomMatch774_end_17= tomMatch774_end_17.getTailConcProduction() ;}}} while(!( (tomMatch774_end_17==tomMatch774_11) ));}}}if ( tomMatch774_end_8.isEmptyConcSection() ) {tomMatch774_end_8=tomMatch774_2;} else {tomMatch774_end_8= tomMatch774_end_8.getTailConcSection() ;}}} while(!( (tomMatch774_end_8==tomMatch774_2) ));}}}}}


    return result;
  }

  /*
   * Get all sort uses in a module (as codomain of an operator)
   */
  private Collection<SortDecl> getSortDeclarationInCodomain(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    { /* unamed block */{ /* unamed block */if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch775_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom___moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch775_2) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch775_2) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch775_end_8=tomMatch775_2;do {{ /* unamed block */if (!( tomMatch775_end_8.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch775_12= tomMatch775_end_8.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch775_12) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.ProductionList  tomMatch775_11= tomMatch775_12.getProductionList() ;if ( (((( tom.gom.adt.gom.types.ProductionList )tomMatch775_11) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tomMatch775_11) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch775_end_17=tomMatch775_11;do {{ /* unamed block */if (!( tomMatch775_end_17.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch775_21= tomMatch775_end_17.getHeadConcProduction() ;if ( ((( tom.gom.adt.gom.types.Production )tomMatch775_21) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch775_20= tomMatch775_21.getAlternativeList() ;if ( (((( tom.gom.adt.gom.types.AlternativeList )tomMatch775_20) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )tomMatch775_20) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch775_end_26=tomMatch775_20;do {{ /* unamed block */if (!( tomMatch775_end_26.isEmptyConcAlternative() )) { tom.gom.adt.gom.types.Alternative  tomMatch775_33= tomMatch775_end_26.getHeadConcAlternative() ;if ( ((( tom.gom.adt.gom.types.Alternative )tomMatch775_33) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { tom.gom.adt.gom.types.GomType  tomMatch775_31= tomMatch775_33.getCodomain() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch775_31) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom___typeName= tomMatch775_31.getName() ;











        if (getGomEnvironment().isBuiltinSort(tom___typeName)) {
          result.add(getGomEnvironment().builtinSort(tom___typeName));
        } else {
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom___typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom___moduleName, getStreamManager().getPackagePath(tom___moduleName.getName())) ) );
        }
      }}}if ( tomMatch775_end_26.isEmptyConcAlternative() ) {tomMatch775_end_26=tomMatch775_20;} else {tomMatch775_end_26= tomMatch775_end_26.getTailConcAlternative() ;}}} while(!( (tomMatch775_end_26==tomMatch775_20) ));}}}if ( tomMatch775_end_17.isEmptyConcProduction() ) {tomMatch775_end_17=tomMatch775_11;} else {tomMatch775_end_17= tomMatch775_end_17.getTailConcProduction() ;}}} while(!( (tomMatch775_end_17==tomMatch775_11) ));}}}if ( tomMatch775_end_8.isEmptyConcSection() ) {tomMatch775_end_8=tomMatch775_2;} else {tomMatch775_end_8= tomMatch775_end_8.getTailConcSection() ;}}} while(!( (tomMatch775_end_8==tomMatch775_2) ));}}}}}


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
    { /* unamed block */{ /* unamed block */if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tom___sectionList= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;


        imports.add( (( tom.gom.adt.gom.types.GomModule )module).getModuleName() );
        { /* unamed block */{ /* unamed block */if ( (tom___sectionList instanceof tom.gom.adt.gom.types.SectionList) ) {if ( (((( tom.gom.adt.gom.types.SectionList )tom___sectionList) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tom___sectionList) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch777_end_4=(( tom.gom.adt.gom.types.SectionList )tom___sectionList);do {{ /* unamed block */if (!( tomMatch777_end_4.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch777_8= tomMatch777_end_4.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch777_8) instanceof tom.gom.adt.gom.types.section.Imports) ) { tom.gom.adt.gom.types.ImportList  tomMatch777_7= tomMatch777_8.getImportList() ;if ( (((( tom.gom.adt.gom.types.ImportList )tomMatch777_7) instanceof tom.gom.adt.gom.types.importlist.ConsConcImportedModule) || ((( tom.gom.adt.gom.types.ImportList )tomMatch777_7) instanceof tom.gom.adt.gom.types.importlist.EmptyConcImportedModule)) ) { tom.gom.adt.gom.types.ImportList  tomMatch777_end_13=tomMatch777_7;do {{ /* unamed block */if (!( tomMatch777_end_13.isEmptyConcImportedModule() )) { tom.gom.adt.gom.types.GomModuleName  tomMatch777_17= tomMatch777_end_13.getHeadConcImportedModule() ;if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch777_17) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {






            if (!getGomEnvironment().isBuiltin( tomMatch777_17.getName() )) {
              imports.add( tomMatch777_end_13.getHeadConcImportedModule() );
            }
          }}if ( tomMatch777_end_13.isEmptyConcImportedModule() ) {tomMatch777_end_13=tomMatch777_7;} else {tomMatch777_end_13= tomMatch777_end_13.getTailConcImportedModule() ;}}} while(!( (tomMatch777_end_13==tomMatch777_7) ));}}}if ( tomMatch777_end_4.isEmptyConcSection() ) {tomMatch777_end_4=(( tom.gom.adt.gom.types.SectionList )tom___sectionList);} else {tomMatch777_end_4= tomMatch777_end_4.getTailConcSection() ;}}} while(!( (tomMatch777_end_4==(( tom.gom.adt.gom.types.SectionList )tom___sectionList)) ));}}}}}}}}




    return imports;
  }

  private GomModule getModule(GomModuleName modname, GomModuleList list) {
    { /* unamed block */{ /* unamed block */if ( (list instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch778_end_4=(( tom.gom.adt.gom.types.GomModuleList )list);do {{ /* unamed block */if (!( tomMatch778_end_4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch778_8= tomMatch778_end_4.getHeadConcGomModule() ;if ( ((( tom.gom.adt.gom.types.GomModule )tomMatch778_8) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) {


        if ( tomMatch778_8.getModuleName() .equals(modname)) {
          return  tomMatch778_end_4.getHeadConcGomModule() ;
        }
      }}if ( tomMatch778_end_4.isEmptyConcGomModule() ) {tomMatch778_end_4=(( tom.gom.adt.gom.types.GomModuleList )list);} else {tomMatch778_end_4= tomMatch778_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch778_end_4==(( tom.gom.adt.gom.types.GomModuleList )list)) ));}}}}


    throw new GomRuntimeException("Module "+ modname +" not present");
  }

  private Collection<GomModuleName> getTransitiveClosureImports(
      GomModule module,
      GomModuleList moduleList) {
    Set<GomModuleName> imported = new HashSet<GomModuleName>();
    imported.addAll(getImportedModules(module));

    Set<GomModuleName> newSet = new HashSet<GomModuleName>();
    while (!newSet.equals(imported)) {
      newSet.addAll(imported);
      imported.addAll(newSet);
      for (GomModuleName modname : imported) {
        newSet.addAll(getImportedModules(getModule(modname,moduleList)));
      }
    }
    return newSet;
  }

  private void buildDependencyMap(GomModuleList moduleList) {
    { /* unamed block */{ /* unamed block */if ( (moduleList instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch779_end_4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);do {{ /* unamed block */if (!( tomMatch779_end_4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch779_8= tomMatch779_end_4.getHeadConcGomModule() ;if ( ((( tom.gom.adt.gom.types.GomModule )tomMatch779_8) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tom___moduleName= tomMatch779_8.getModuleName() ;


        ModuleDeclList importsModuleDeclList =  tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() ;
        for (GomModuleName importedModuleName :
            getTransitiveClosureImports( tomMatch779_end_4.getHeadConcGomModule() ,moduleList)) {
          importsModuleDeclList =
             tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(importedModuleName, getStreamManager().getPackagePath(importedModuleName.getName())) ,tom_append_list_ConcModuleDecl(importsModuleDeclList, tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() )) 




;
        }
        getGomEnvironment().addModuleDependency(
             tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom___moduleName, getStreamManager().getPackagePath(tom___moduleName.getName())) 

,
            importsModuleDeclList);
      }}if ( tomMatch779_end_4.isEmptyConcGomModule() ) {tomMatch779_end_4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);} else {tomMatch779_end_4= tomMatch779_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch779_end_4==(( tom.gom.adt.gom.types.GomModuleList )moduleList)) ));}}}}


  }

  private boolean checkSortValidity(Sort sort) {
    boolean valid = true;
    // check if the same slot name is used with different types
    Map<String,SortDecl> mapNameType = new HashMap<String,SortDecl>();
    { /* unamed block */{ /* unamed block */if ( (sort instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )sort) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch780_1= (( tom.gom.adt.gom.types.Sort )sort).getDecl() ; tom.gom.adt.gom.types.OperatorDeclList  tomMatch780_2= (( tom.gom.adt.gom.types.Sort )sort).getOperatorDecls() ;boolean tomMatch780_31= false ; String  tomMatch780_5= "" ; tom.gom.adt.gom.types.SortDecl  tomMatch780_8= null ; tom.gom.adt.gom.types.SortDecl  tomMatch780_7= null ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch780_1) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {{ /* unamed block */tomMatch780_31= true ;tomMatch780_7=tomMatch780_1;tomMatch780_5= tomMatch780_7.getName() ;}} else {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch780_1) instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {{ /* unamed block */tomMatch780_31= true ;tomMatch780_8=tomMatch780_1;tomMatch780_5= tomMatch780_8.getName() ;}}}if (tomMatch780_31) {if ( (((( tom.gom.adt.gom.types.OperatorDeclList )tomMatch780_2) instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || ((( tom.gom.adt.gom.types.OperatorDeclList )tomMatch780_2) instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch780_end_12=tomMatch780_2;do {{ /* unamed block */if (!( tomMatch780_end_12.isEmptyConcOperator() )) { tom.gom.adt.gom.types.OperatorDecl  tomMatch780_16= tomMatch780_end_12.getHeadConcOperator() ;if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch780_16) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch780_15= tomMatch780_16.getProd() ;if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch780_15) instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tomMatch780_18= tomMatch780_15.getSlots() ;if ( (((( tom.gom.adt.gom.types.SlotList )tomMatch780_18) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )tomMatch780_18) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) { tom.gom.adt.gom.types.SlotList  tomMatch780_end_24=tomMatch780_18;do {{ /* unamed block */if (!( tomMatch780_end_24.isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch780_29= tomMatch780_end_24.getHeadConcSlot() ;if ( ((( tom.gom.adt.gom.types.Slot )tomMatch780_29) instanceof tom.gom.adt.gom.types.slot.Slot) ) { String  tom___slotName= tomMatch780_29.getName() ; tom.gom.adt.gom.types.SortDecl  tom___slotSort= tomMatch780_29.getSort() ;







        if (!mapNameType.containsKey(tom___slotName)) {
          mapNameType.put(tom___slotName,tom___slotSort);
        } else {
          SortDecl prevSort = mapNameType.get(tom___slotName);
          if (!prevSort.equals(tom___slotSort)) {
            GomMessage.error(getLogger(),null,0,
                GomMessage.slotIncompatibleTypes,
                new Object[]{tomMatch780_5,tom___slotName,prevSort.getName(),
                             tom___slotSort
.getName()});
            valid = false;
          }
        }
      }}if ( tomMatch780_end_24.isEmptyConcSlot() ) {tomMatch780_end_24=tomMatch780_18;} else {tomMatch780_end_24= tomMatch780_end_24.getTailConcSlot() ;}}} while(!( (tomMatch780_end_24==tomMatch780_18) ));}}}}if ( tomMatch780_end_12.isEmptyConcOperator() ) {tomMatch780_end_12=tomMatch780_2;} else {tomMatch780_end_12= tomMatch780_end_12.getTailConcOperator() ;}}} while(!( (tomMatch780_end_12==tomMatch780_2) ));}}}}}}


    return valid;
  }

  private String showSortList(Collection<SortDecl> decls) {
    StringBuilder sorts = new StringBuilder();
    for (SortDecl decl : decls) {
      if (0==sorts.length()) {
        sorts.append(", ");
      }
      sorts.append(decl.getName());
    }
    return sorts.toString();
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
