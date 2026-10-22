
























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

     private static   tom.gom.adt.gom.types.AlternativeList  tom_append_list_ConcAlternative( tom.gom.adt.gom.types.AlternativeList l1,  tom.gom.adt.gom.types.AlternativeList  l2) {     if( l1.isEmptyConcAlternative() ) {       return l2;     } else if( l2.isEmptyConcAlternative() ) {       return l1;     } else if(  l1.getTailConcAlternative() .isEmptyConcAlternative() ) {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,l2) ;     } else {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,tom_append_list_ConcAlternative( l1.getTailConcAlternative() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AlternativeList  tom_get_slice_ConcAlternative( tom.gom.adt.gom.types.AlternativeList  begin,  tom.gom.adt.gom.types.AlternativeList  end, tom.gom.adt.gom.types.AlternativeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAlternative()  ||  (end== tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( begin.getHeadConcAlternative() ,( tom.gom.adt.gom.types.AlternativeList )tom_get_slice_ConcAlternative( begin.getTailConcAlternative() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {     if( l1.isEmptyConcModule() ) {       return l2;     } else if( l2.isEmptyConcModule() ) {       return l1;     } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ImportList  tom_append_list_ConcImportedModule( tom.gom.adt.gom.types.ImportList l1,  tom.gom.adt.gom.types.ImportList  l2) {     if( l1.isEmptyConcImportedModule() ) {       return l2;     } else if( l2.isEmptyConcImportedModule() ) {       return l1;     } else if(  l1.getTailConcImportedModule() .isEmptyConcImportedModule() ) {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,tom_append_list_ConcImportedModule( l1.getTailConcImportedModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ImportList  tom_get_slice_ConcImportedModule( tom.gom.adt.gom.types.ImportList  begin,  tom.gom.adt.gom.types.ImportList  end, tom.gom.adt.gom.types.ImportList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcImportedModule()  ||  (end== tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( begin.getHeadConcImportedModule() ,( tom.gom.adt.gom.types.ImportList )tom_get_slice_ConcImportedModule( begin.getTailConcImportedModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortDeclList  tom_append_list_ConcSortDecl( tom.gom.adt.gom.types.SortDeclList l1,  tom.gom.adt.gom.types.SortDeclList  l2) {     if( l1.isEmptyConcSortDecl() ) {       return l2;     } else if( l2.isEmptyConcSortDecl() ) {       return l1;     } else if(  l1.getTailConcSortDecl() .isEmptyConcSortDecl() ) {       return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( l1.getHeadConcSortDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( l1.getHeadConcSortDecl() ,tom_append_list_ConcSortDecl( l1.getTailConcSortDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortDeclList  tom_get_slice_ConcSortDecl( tom.gom.adt.gom.types.SortDeclList  begin,  tom.gom.adt.gom.types.SortDeclList  end, tom.gom.adt.gom.types.SortDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSortDecl()  ||  (end== tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make( begin.getHeadConcSortDecl() ,( tom.gom.adt.gom.types.SortDeclList )tom_get_slice_ConcSortDecl( begin.getTailConcSortDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleDeclList  tom_append_list_ConcModuleDecl( tom.gom.adt.gom.types.ModuleDeclList l1,  tom.gom.adt.gom.types.ModuleDeclList  l2) {     if( l1.isEmptyConcModuleDecl() ) {       return l2;     } else if( l2.isEmptyConcModuleDecl() ) {       return l1;     } else if(  l1.getTailConcModuleDecl() .isEmptyConcModuleDecl() ) {       return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( l1.getHeadConcModuleDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( l1.getHeadConcModuleDecl() ,tom_append_list_ConcModuleDecl( l1.getTailConcModuleDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleDeclList  tom_get_slice_ConcModuleDecl( tom.gom.adt.gom.types.ModuleDeclList  begin,  tom.gom.adt.gom.types.ModuleDeclList  end, tom.gom.adt.gom.types.ModuleDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModuleDecl()  ||  (end== tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( begin.getHeadConcModuleDecl() ,( tom.gom.adt.gom.types.ModuleDeclList )tom_get_slice_ConcModuleDecl( begin.getTailConcModuleDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.OperatorDeclList  tom_append_list_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList l1,  tom.gom.adt.gom.types.OperatorDeclList  l2) {     if( l1.isEmptyConcOperator() ) {       return l2;     } else if( l2.isEmptyConcOperator() ) {       return l1;     } else if(  l1.getTailConcOperator() .isEmptyConcOperator() ) {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,l2) ;     } else {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,tom_append_list_ConcOperator( l1.getTailConcOperator() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.OperatorDeclList  tom_get_slice_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList  begin,  tom.gom.adt.gom.types.OperatorDeclList  end, tom.gom.adt.gom.types.OperatorDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcOperator()  ||  (end== tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( begin.getHeadConcOperator() ,( tom.gom.adt.gom.types.OperatorDeclList )tom_get_slice_ConcOperator( begin.getTailConcOperator() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {     if( l1.isEmptyConcSort() ) {       return l2;     } else if( l2.isEmptyConcSort() ) {       return l1;     } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;   }   

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

  
  public ModuleList expand(GomModuleList gomModuleList) {
    if (!(gomModuleList instanceof ConcGomModule)) {
      throw new RuntimeException("A GomModuleList should be a list");
    }
    ConcGomModule moduleList = (ConcGomModule) gomModuleList;

    
    buildDependencyMap(moduleList);

    
    SortDeclList sortDeclList =  tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() ;
    
    Map<ModuleDecl,SortDeclList> sortsForModule =
      new HashMap<ModuleDecl,SortDeclList>();
    for (GomModule module : moduleList) {
      Collection<SortDecl> decls = getSortDeclarations(module);

      Collection<SortDecl> implicitdecls = getSortDeclarationInCodomain(module);

      
      if(!decls.containsAll(implicitdecls)) {
        
        Collection<SortDecl> undeclaredSorts = new HashSet<SortDecl>();
        undeclaredSorts.addAll(implicitdecls);
        undeclaredSorts.removeAll(decls);
        GomMessage.warning(getLogger(),null,0,
            GomMessage.undeclaredSorts,
            new Object[]{showSortList(undeclaredSorts)});
      }
      if(!implicitdecls.containsAll(decls)) {
        
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
      
      SortDeclList declaredSorts =  tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() ;
      for (SortDecl decl : decls) {
        declaredSorts =  tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl.make(decl,tom_append_list_ConcSortDecl(declaredSorts, tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl.make() )) ;
      }
      GomModuleName moduleName = module.getModuleName();
      ModuleDecl mdecl =  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(moduleName, getStreamManager().getPackagePath(moduleName.getName())) ;
      sortsForModule.put(mdecl,declaredSorts);
    }

    
    Map<SortDecl,OperatorDeclList> operatorsForSort =
      new HashMap<SortDecl,OperatorDeclList>();
    for (GomModule module : moduleList) {
      
      { /* unamed block */{ /* unamed block */if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch749_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch749_2) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch749_2) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch749_end_8=tomMatch749_2;do {{ /* unamed block */if (!( tomMatch749_end_8.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch749_12= tomMatch749_end_8.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch749_12) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.ProductionList  tomMatch749_11= tomMatch749_12.getProductionList() ;if ( (((( tom.gom.adt.gom.types.ProductionList )tomMatch749_11) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tomMatch749_11) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch749_end_17=tomMatch749_11;do {{ /* unamed block */if (!( tomMatch749_end_17.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch749_21= tomMatch749_end_17.getHeadConcProduction() ;if ( ((( tom.gom.adt.gom.types.Production )tomMatch749_21) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch749_20= tomMatch749_21.getAlternativeList() ;if ( (((( tom.gom.adt.gom.types.AlternativeList )tomMatch749_20) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )tomMatch749_20) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch749_end_26=tomMatch749_20;do {{ /* unamed block */if (!( tomMatch749_end_26.isEmptyConcAlternative() )) {if ( ((( tom.gom.adt.gom.types.Alternative ) tomMatch749_end_26.getHeadConcAlternative() ) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) {






          
          getOperatorDecl( tomMatch749_end_26.getHeadConcAlternative() ,sortDeclList,operatorsForSort);

        }}if ( tomMatch749_end_26.isEmptyConcAlternative() ) {tomMatch749_end_26=tomMatch749_20;} else {tomMatch749_end_26= tomMatch749_end_26.getTailConcAlternative() ;}}} while(!( (tomMatch749_end_26==tomMatch749_20) ));}}}if ( tomMatch749_end_17.isEmptyConcProduction() ) {tomMatch749_end_17=tomMatch749_11;} else {tomMatch749_end_17= tomMatch749_end_17.getTailConcProduction() ;}}} while(!( (tomMatch749_end_17==tomMatch749_11) ));}}}if ( tomMatch749_end_8.isEmptyConcSection() ) {tomMatch749_end_8=tomMatch749_2;} else {tomMatch749_end_8= tomMatch749_end_8.getTailConcSection() ;}}} while(!( (tomMatch749_end_8==tomMatch749_2) ));}}}}}

    }

    
    ModuleList resultModuleList =  tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ;
    for (Map.Entry<ModuleDecl,SortDeclList> entry : sortsForModule.entrySet()) {
      ModuleDecl mdecl = entry.getKey();
      SortDeclList sdeclList = entry.getValue();
      SortList sortList =  tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ;
      { /* unamed block */{ /* unamed block */if ( (sdeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sdeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch750_end_4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);do {{ /* unamed block */if (!( tomMatch750_end_4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tom___sdecl= tomMatch750_end_4.getHeadConcSortDecl() ;

          OperatorDeclList opdecl = operatorsForSort.get(tom___sdecl);
          Sort fullSort =  tom.gom.adt.gom.types.sort.Sort.make(tom___sdecl, opdecl) ;
          if(checkSortValidity(fullSort)) { /* unamed block */
            sortList =  tom.gom.adt.gom.types.sortlist.ConsConcSort.make(fullSort,tom_append_list_ConcSort(sortList, tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() )) ;
          }}if ( tomMatch750_end_4.isEmptyConcSortDecl() ) {tomMatch750_end_4=(( tom.gom.adt.gom.types.SortDeclList )sdeclList);} else {tomMatch750_end_4= tomMatch750_end_4.getTailConcSortDecl() ;}}} while(!( (tomMatch750_end_4==(( tom.gom.adt.gom.types.SortDeclList )sdeclList)) ));}}}}


      resultModuleList =  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( tom.gom.adt.gom.types.module.Module.make(mdecl, sortList) ,tom_append_list_ConcModule(resultModuleList, tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() )) 

;
    }
    return resultModuleList;
  }

  
  private OperatorDecl getOperatorDecl(Alternative alt,
      SortDeclList sortDeclList,
      Map<SortDecl,OperatorDeclList> operatorsForSort) {

    { /* unamed block */{ /* unamed block */if ( (alt instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )alt) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { tom.gom.adt.gom.types.GomType  tomMatch751_3= (( tom.gom.adt.gom.types.Alternative )alt).getCodomain() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch751_3) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {

        SortDecl codomainSort = declFromTypename( tomMatch751_3.getName() ,sortDeclList);
        TypedProduction domainSorts = typedProduction( (( tom.gom.adt.gom.types.Alternative )alt).getDomainList() ,sortDeclList);

        OperatorDecl decl =  tom.gom.adt.gom.types.operatordecl.OperatorDecl.make( (( tom.gom.adt.gom.types.Alternative )alt).getName() , codomainSort, domainSorts,  (( tom.gom.adt.gom.types.Alternative )alt).getOption() ) ;
        if (operatorsForSort.containsKey(codomainSort)) { /* unamed block */
          OperatorDeclList list = operatorsForSort.get(codomainSort);
          operatorsForSort.put(codomainSort, tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make(decl,tom_append_list_ConcOperator(list, tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() )) );
        } else { /* unamed block */
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
    { /* unamed block */{ /* unamed block */if ( (sortDeclList instanceof tom.gom.adt.gom.types.SortDeclList) ) {if ( (((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.ConsConcSortDecl) || ((( tom.gom.adt.gom.types.SortDeclList )sortDeclList) instanceof tom.gom.adt.gom.types.sortdecllist.EmptyConcSortDecl)) ) { tom.gom.adt.gom.types.SortDeclList  tomMatch752_end_4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);do {{ /* unamed block */if (!( tomMatch752_end_4.isEmptyConcSortDecl() )) { tom.gom.adt.gom.types.SortDecl  tomMatch752_8= tomMatch752_end_4.getHeadConcSortDecl() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch752_8) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        if (typename.equals( tomMatch752_8.getName() )) { /* unamed block */
          return  tomMatch752_end_4.getHeadConcSortDecl() ;
        }}}if ( tomMatch752_end_4.isEmptyConcSortDecl() ) {tomMatch752_end_4=(( tom.gom.adt.gom.types.SortDeclList )sortDeclList);} else {tomMatch752_end_4= tomMatch752_end_4.getTailConcSortDecl() ;}}} while(!( (tomMatch752_end_4==(( tom.gom.adt.gom.types.SortDeclList )sortDeclList)) ));}}}}



    GomMessage.error(getLogger(),null,0,
        GomMessage.unknownSort,
        new Object[]{typename});
    
    return  tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl.make(typename) ;
  }

  private TypedProduction typedProduction(FieldList domain, SortDeclList sortDeclList) {
    { /* unamed block */{ /* unamed block */if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )domain).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch753_5= (( tom.gom.adt.gom.types.FieldList )domain).getHeadConcField() ;if ( ((( tom.gom.adt.gom.types.Field )tomMatch753_5) instanceof tom.gom.adt.gom.types.field.StarredField) ) { tom.gom.adt.gom.types.GomType  tomMatch753_3= tomMatch753_5.getFieldType() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch753_3) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {if (  (( tom.gom.adt.gom.types.FieldList )domain).getTailConcField() .isEmptyConcField() ) {

        return  tom.gom.adt.gom.types.typedproduction.Variadic.make(declFromTypename( tomMatch753_3.getName() ,sortDeclList)) ;
      }}}}}}}{ /* unamed block */if ( (domain instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )domain) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {

        return  tom.gom.adt.gom.types.typedproduction.Slots.make(typedSlotList((( tom.gom.adt.gom.types.FieldList )domain),sortDeclList)) ;
      }}}}

    throw new GomRuntimeException("TypeExpander::typedProduction: illformed Alternative");
  }

  private SlotList typedSlotList(FieldList fields, SortDeclList sortDeclList) {
    { /* unamed block */{ /* unamed block */if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if ( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() ) {

        return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
      }}}}{ /* unamed block */if ( (fields instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )fields) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )fields).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch754_9= (( tom.gom.adt.gom.types.FieldList )fields).getHeadConcField() ;if ( ((( tom.gom.adt.gom.types.Field )tomMatch754_9) instanceof tom.gom.adt.gom.types.field.NamedField) ) { tom.gom.adt.gom.types.GomType  tomMatch754_7= tomMatch754_9.getFieldType() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch754_7) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {

        SlotList newtail = typedSlotList( (( tom.gom.adt.gom.types.FieldList )fields).getTailConcField() ,sortDeclList);
        return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make( tomMatch754_9.getName() , declFromTypename( tomMatch754_7.getName() ,sortDeclList)) ,tom_append_list_ConcSlot(newtail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;
      }}}}}}}

    GomMessage.error(getLogger(),null,0,
        GomMessage.malformedProduction,
        new Object[]{fields.toString()});
    return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
  }

  
  private Collection<SortDecl> getSortDeclarations(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    { /* unamed block */{ /* unamed block */if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch755_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom___moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch755_2) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch755_2) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch755_end_8=tomMatch755_2;do {{ /* unamed block */if (!( tomMatch755_end_8.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch755_12= tomMatch755_end_8.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch755_12) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.ProductionList  tomMatch755_11= tomMatch755_12.getProductionList() ;if ( (((( tom.gom.adt.gom.types.ProductionList )tomMatch755_11) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tomMatch755_11) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch755_end_17=tomMatch755_11;do {{ /* unamed block */if (!( tomMatch755_end_17.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch755_21= tomMatch755_end_17.getHeadConcProduction() ;if ( ((( tom.gom.adt.gom.types.Production )tomMatch755_21) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch755_20= tomMatch755_21.getType() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch755_20) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom___typeName= tomMatch755_20.getName() ;





        if (getGomEnvironment().isBuiltinSort(tom___typeName)) { /* unamed block */
          GomMessage.error(getLogger(),null,0,
              GomMessage.operatorOnBuiltin,
              new Object[]{ /* unamed block */(tom___typeName)});
          result.add(getGomEnvironment().builtinSort(tom___typeName));
        } else { /* unamed block */
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom___typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom___moduleName, getStreamManager().getPackagePath(tom___moduleName.getName())) ) );
        }}}}if ( tomMatch755_end_17.isEmptyConcProduction() ) {tomMatch755_end_17=tomMatch755_11;} else {tomMatch755_end_17= tomMatch755_end_17.getTailConcProduction() ;}}} while(!( (tomMatch755_end_17==tomMatch755_11) ));}}}if ( tomMatch755_end_8.isEmptyConcSection() ) {tomMatch755_end_8=tomMatch755_2;} else {tomMatch755_end_8= tomMatch755_end_8.getTailConcSection() ;}}} while(!( (tomMatch755_end_8==tomMatch755_2) ));}}}}}


    return result;
  }

  
  private Collection<SortDecl> getSortDeclarationInCodomain(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    { /* unamed block */{ /* unamed block */if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch756_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ; tom.gom.adt.gom.types.GomModuleName  tom___moduleName= (( tom.gom.adt.gom.types.GomModule )module).getModuleName() ;if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch756_2) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch756_2) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch756_end_8=tomMatch756_2;do {{ /* unamed block */if (!( tomMatch756_end_8.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch756_12= tomMatch756_end_8.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch756_12) instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.ProductionList  tomMatch756_11= tomMatch756_12.getProductionList() ;if ( (((( tom.gom.adt.gom.types.ProductionList )tomMatch756_11) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tomMatch756_11) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch756_end_17=tomMatch756_11;do {{ /* unamed block */if (!( tomMatch756_end_17.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch756_21= tomMatch756_end_17.getHeadConcProduction() ;if ( ((( tom.gom.adt.gom.types.Production )tomMatch756_21) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch756_20= tomMatch756_21.getAlternativeList() ;if ( (((( tom.gom.adt.gom.types.AlternativeList )tomMatch756_20) instanceof tom.gom.adt.gom.types.alternativelist.ConsConcAlternative) || ((( tom.gom.adt.gom.types.AlternativeList )tomMatch756_20) instanceof tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative)) ) { tom.gom.adt.gom.types.AlternativeList  tomMatch756_end_26=tomMatch756_20;do {{ /* unamed block */if (!( tomMatch756_end_26.isEmptyConcAlternative() )) { tom.gom.adt.gom.types.Alternative  tomMatch756_33= tomMatch756_end_26.getHeadConcAlternative() ;if ( ((( tom.gom.adt.gom.types.Alternative )tomMatch756_33) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) { tom.gom.adt.gom.types.GomType  tomMatch756_31= tomMatch756_33.getCodomain() ;if ( ((( tom.gom.adt.gom.types.GomType )tomMatch756_31) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom___typeName= tomMatch756_31.getName() ;










        if (getGomEnvironment().isBuiltinSort(tom___typeName)) { /* unamed block */
          result.add(getGomEnvironment().builtinSort(tom___typeName));
        } else { /* unamed block */
          result.add( tom.gom.adt.gom.types.sortdecl.SortDecl.make(tom___typeName,  tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom___moduleName, getStreamManager().getPackagePath(tom___moduleName.getName())) ) );
        }}}}if ( tomMatch756_end_26.isEmptyConcAlternative() ) {tomMatch756_end_26=tomMatch756_20;} else {tomMatch756_end_26= tomMatch756_end_26.getTailConcAlternative() ;}}} while(!( (tomMatch756_end_26==tomMatch756_20) ));}}}if ( tomMatch756_end_17.isEmptyConcProduction() ) {tomMatch756_end_17=tomMatch756_11;} else {tomMatch756_end_17= tomMatch756_end_17.getTailConcProduction() ;}}} while(!( (tomMatch756_end_17==tomMatch756_11) ));}}}if ( tomMatch756_end_8.isEmptyConcSection() ) {tomMatch756_end_8=tomMatch756_2;} else {tomMatch756_end_8= tomMatch756_end_8.getTailConcSection() ;}}} while(!( (tomMatch756_end_8==tomMatch756_2) ));}}}}}


    return result;
  }

  
  private Collection<GomModuleName> getImportedModules(GomModule module) {
    Set<GomModuleName> imports = new HashSet<GomModuleName>();
    { /* unamed block */{ /* unamed block */if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tom___sectionList= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;

        imports.add( (( tom.gom.adt.gom.types.GomModule )module).getModuleName() );
        { /* unamed block */{ /* unamed block */if ( (tom___sectionList instanceof tom.gom.adt.gom.types.SectionList) ) {if ( (((( tom.gom.adt.gom.types.SectionList )tom___sectionList) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tom___sectionList) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch758_end_4=(( tom.gom.adt.gom.types.SectionList )tom___sectionList);do {{ /* unamed block */if (!( tomMatch758_end_4.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch758_8= tomMatch758_end_4.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch758_8) instanceof tom.gom.adt.gom.types.section.Imports) ) { tom.gom.adt.gom.types.ImportList  tomMatch758_7= tomMatch758_8.getImportList() ;if ( (((( tom.gom.adt.gom.types.ImportList )tomMatch758_7) instanceof tom.gom.adt.gom.types.importlist.ConsConcImportedModule) || ((( tom.gom.adt.gom.types.ImportList )tomMatch758_7) instanceof tom.gom.adt.gom.types.importlist.EmptyConcImportedModule)) ) { tom.gom.adt.gom.types.ImportList  tomMatch758_end_13=tomMatch758_7;do {{ /* unamed block */if (!( tomMatch758_end_13.isEmptyConcImportedModule() )) { tom.gom.adt.gom.types.GomModuleName  tomMatch758_17= tomMatch758_end_13.getHeadConcImportedModule() ;if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch758_17) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {





            if (!getGomEnvironment().isBuiltin( tomMatch758_17.getName() )) { /* unamed block */
              imports.add( tomMatch758_end_13.getHeadConcImportedModule() );
            }}}if ( tomMatch758_end_13.isEmptyConcImportedModule() ) {tomMatch758_end_13=tomMatch758_7;} else {tomMatch758_end_13= tomMatch758_end_13.getTailConcImportedModule() ;}}} while(!( (tomMatch758_end_13==tomMatch758_7) ));}}}if ( tomMatch758_end_4.isEmptyConcSection() ) {tomMatch758_end_4=(( tom.gom.adt.gom.types.SectionList )tom___sectionList);} else {tomMatch758_end_4= tomMatch758_end_4.getTailConcSection() ;}}} while(!( (tomMatch758_end_4==(( tom.gom.adt.gom.types.SectionList )tom___sectionList)) ));}}}}}}}}




    return imports;
  }

  private GomModule getModule(GomModuleName modname, GomModuleList list) {
    { /* unamed block */{ /* unamed block */if ( (list instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )list) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch759_end_4=(( tom.gom.adt.gom.types.GomModuleList )list);do {{ /* unamed block */if (!( tomMatch759_end_4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch759_8= tomMatch759_end_4.getHeadConcGomModule() ;if ( ((( tom.gom.adt.gom.types.GomModule )tomMatch759_8) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) {

        if ( tomMatch759_8.getModuleName() .equals(modname)) { /* unamed block */
          return  tomMatch759_end_4.getHeadConcGomModule() ;
        }}}if ( tomMatch759_end_4.isEmptyConcGomModule() ) {tomMatch759_end_4=(( tom.gom.adt.gom.types.GomModuleList )list);} else {tomMatch759_end_4= tomMatch759_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch759_end_4==(( tom.gom.adt.gom.types.GomModuleList )list)) ));}}}}


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
    { /* unamed block */{ /* unamed block */if ( (moduleList instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )moduleList) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch760_end_4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);do {{ /* unamed block */if (!( tomMatch760_end_4.isEmptyConcGomModule() )) { tom.gom.adt.gom.types.GomModule  tomMatch760_8= tomMatch760_end_4.getHeadConcGomModule() ;if ( ((( tom.gom.adt.gom.types.GomModule )tomMatch760_8) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tom___moduleName= tomMatch760_8.getModuleName() ;

        ModuleDeclList importsModuleDeclList =  tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() ;
        for (GomModuleName importedModuleName :
            getTransitiveClosureImports( tomMatch760_end_4.getHeadConcGomModule() ,moduleList)) { /* unamed block */
          importsModuleDeclList =
             tom.gom.adt.gom.types.moduledecllist.ConsConcModuleDecl.make( tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(importedModuleName, getStreamManager().getPackagePath(importedModuleName.getName())) ,tom_append_list_ConcModuleDecl(importsModuleDeclList, tom.gom.adt.gom.types.moduledecllist.EmptyConcModuleDecl.make() )) 



;
        }
        getGomEnvironment().addModuleDependency(
             tom.gom.adt.gom.types.moduledecl.ModuleDecl.make(tom___moduleName, getStreamManager().getPackagePath(tom___moduleName.getName())) 
,
            importsModuleDeclList);
      }}if ( tomMatch760_end_4.isEmptyConcGomModule() ) {tomMatch760_end_4=(( tom.gom.adt.gom.types.GomModuleList )moduleList);} else {tomMatch760_end_4= tomMatch760_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch760_end_4==(( tom.gom.adt.gom.types.GomModuleList )moduleList)) ));}}}}

  }

  private boolean checkSortValidity(Sort sort) {
    boolean valid = true;
    
    Map<String,SortDecl> mapNameType = new HashMap<String,SortDecl>();
    { /* unamed block */{ /* unamed block */if ( (sort instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )sort) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch761_1= (( tom.gom.adt.gom.types.Sort )sort).getDecl() ; tom.gom.adt.gom.types.OperatorDeclList  tomMatch761_2= (( tom.gom.adt.gom.types.Sort )sort).getOperatorDecls() ;boolean tomMatch761_31= false ; tom.gom.adt.gom.types.SortDecl  tomMatch761_7= null ; String  tomMatch761_5= "" ; tom.gom.adt.gom.types.SortDecl  tomMatch761_8= null ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch761_1) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {{ /* unamed block */tomMatch761_31= true ;tomMatch761_7=tomMatch761_1;tomMatch761_5= tomMatch761_7.getName() ;}} else {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch761_1) instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {{ /* unamed block */tomMatch761_31= true ;tomMatch761_8=tomMatch761_1;tomMatch761_5= tomMatch761_8.getName() ;}}}if (tomMatch761_31) {if ( (((( tom.gom.adt.gom.types.OperatorDeclList )tomMatch761_2) instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || ((( tom.gom.adt.gom.types.OperatorDeclList )tomMatch761_2) instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch761_end_12=tomMatch761_2;do {{ /* unamed block */if (!( tomMatch761_end_12.isEmptyConcOperator() )) { tom.gom.adt.gom.types.OperatorDecl  tomMatch761_16= tomMatch761_end_12.getHeadConcOperator() ;if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch761_16) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch761_15= tomMatch761_16.getProd() ;if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch761_15) instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tomMatch761_18= tomMatch761_15.getSlots() ;if ( (((( tom.gom.adt.gom.types.SlotList )tomMatch761_18) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )tomMatch761_18) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) { tom.gom.adt.gom.types.SlotList  tomMatch761_end_24=tomMatch761_18;do {{ /* unamed block */if (!( tomMatch761_end_24.isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch761_29= tomMatch761_end_24.getHeadConcSlot() ;if ( ((( tom.gom.adt.gom.types.Slot )tomMatch761_29) instanceof tom.gom.adt.gom.types.slot.Slot) ) { String  tom___slotName= tomMatch761_29.getName() ; tom.gom.adt.gom.types.SortDecl  tom___slotSort= tomMatch761_29.getSort() ;






        if (!mapNameType.containsKey(tom___slotName)) { /* unamed block */
          mapNameType.put(tom___slotName,tom___slotSort);
        } else { /* unamed block */
          SortDecl prevSort = mapNameType.get(tom___slotName);
          if (!prevSort.equals(tom___slotSort)) { /* unamed block */
            GomMessage.error(getLogger(),null,0,
                GomMessage.slotIncompatibleTypes,
                new Object[]{ /* unamed block */tomMatch761_5,tom___slotName,prevSort.getName(),
                             (tom___slotSort).getName()});
            valid = false;
          }}}}if ( tomMatch761_end_24.isEmptyConcSlot() ) {tomMatch761_end_24=tomMatch761_18;} else {tomMatch761_end_24= tomMatch761_end_24.getTailConcSlot() ;}}} while(!( (tomMatch761_end_24==tomMatch761_18) ));}}}}if ( tomMatch761_end_12.isEmptyConcOperator() ) {tomMatch761_end_12=tomMatch761_2;} else {tomMatch761_end_12= tomMatch761_end_12.getTailConcOperator() ;}}} while(!( (tomMatch761_end_12==tomMatch761_2) ));}}}}}}



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
