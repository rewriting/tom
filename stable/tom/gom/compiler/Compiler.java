/*
 * Gom
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

package tom.gom.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.library.sl.VisitFailure;

public class Compiler {

         private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {     if( l1.isEmptyConcSlotField() ) {       return l2;     } else if( l2.isEmptyConcSlotField() ) {       return l1;     } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlotField()  ||  (end== tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;   }      private static   tom.gom.adt.objects.types.GomClassList  tom_append_list_ConcGomClass( tom.gom.adt.objects.types.GomClassList l1,  tom.gom.adt.objects.types.GomClassList  l2) {     if( l1.isEmptyConcGomClass() ) {       return l2;     } else if( l2.isEmptyConcGomClass() ) {       return l1;     } else if(  l1.getTailConcGomClass() .isEmptyConcGomClass() ) {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,l2) ;     } else {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,tom_append_list_ConcGomClass( l1.getTailConcGomClass() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.GomClassList  tom_get_slice_ConcGomClass( tom.gom.adt.objects.types.GomClassList  begin,  tom.gom.adt.objects.types.GomClassList  end, tom.gom.adt.objects.types.GomClassList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomClass()  ||  (end== tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( begin.getHeadConcGomClass() ,( tom.gom.adt.objects.types.GomClassList )tom_get_slice_ConcGomClass( begin.getTailConcGomClass() ,end,tail)) ;   }      private static   tom.gom.adt.objects.types.ClassNameList  tom_append_list_ConcClassName( tom.gom.adt.objects.types.ClassNameList l1,  tom.gom.adt.objects.types.ClassNameList  l2) {     if( l1.isEmptyConcClassName() ) {       return l2;     } else if( l2.isEmptyConcClassName() ) {       return l1;     } else if(  l1.getTailConcClassName() .isEmptyConcClassName() ) {       return  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( l1.getHeadConcClassName() ,l2) ;     } else {       return  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( l1.getHeadConcClassName() ,tom_append_list_ConcClassName( l1.getTailConcClassName() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.ClassNameList  tom_get_slice_ConcClassName( tom.gom.adt.objects.types.ClassNameList  begin,  tom.gom.adt.objects.types.ClassNameList  end, tom.gom.adt.objects.types.ClassNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcClassName()  ||  (end== tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( begin.getHeadConcClassName() ,( tom.gom.adt.objects.types.ClassNameList )tom_get_slice_ConcClassName( begin.getTailConcClassName() ,end,tail)) ;   }      private static   tom.gom.adt.objects.types.HookList  tom_append_list_ConcHook( tom.gom.adt.objects.types.HookList l1,  tom.gom.adt.objects.types.HookList  l2) {     if( l1.isEmptyConcHook() ) {       return l2;     } else if( l2.isEmptyConcHook() ) {       return l1;     } else if(  l1.getTailConcHook() .isEmptyConcHook() ) {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,l2) ;     } else {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,tom_append_list_ConcHook( l1.getTailConcHook() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.HookList  tom_get_slice_ConcHook( tom.gom.adt.objects.types.HookList  begin,  tom.gom.adt.objects.types.HookList  end, tom.gom.adt.objects.types.HookList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHook()  ||  (end== tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( begin.getHeadConcHook() ,( tom.gom.adt.objects.types.HookList )tom_get_slice_ConcHook( begin.getTailConcHook() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.OperatorDeclList  tom_append_list_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList l1,  tom.gom.adt.gom.types.OperatorDeclList  l2) {     if( l1.isEmptyConcOperator() ) {       return l2;     } else if( l2.isEmptyConcOperator() ) {       return l1;     } else if(  l1.getTailConcOperator() .isEmptyConcOperator() ) {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,l2) ;     } else {       return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( l1.getHeadConcOperator() ,tom_append_list_ConcOperator( l1.getTailConcOperator() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.OperatorDeclList  tom_get_slice_ConcOperator( tom.gom.adt.gom.types.OperatorDeclList  begin,  tom.gom.adt.gom.types.OperatorDeclList  end, tom.gom.adt.gom.types.OperatorDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcOperator()  ||  (end== tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.operatordecllist.ConsConcOperator.make( begin.getHeadConcOperator() ,( tom.gom.adt.gom.types.OperatorDeclList )tom_get_slice_ConcOperator( begin.getTailConcOperator() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {     if( l1.isEmptyConcSort() ) {       return l2;     } else if( l2.isEmptyConcSort() ) {       return l1;     } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {     if( l1.isEmptyConcModule() ) {       return l2;     } else if( l2.isEmptyConcModule() ) {       return l1;     } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;   }    

  private GomEnvironment gomEnvironment;
  private Map<SortDecl,ClassName> sortClassNameForSortDecl;

  public Compiler(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
    sortClassNameForSortDecl = new HashMap<SortDecl,ClassName>(getGomEnvironment().builtinSortClassMap());
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomClassList compile(ModuleList moduleList, HookDeclList hookDecls) {
    GomClassList classList =  tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ;

    /* ModuleDecl -> (AbstractType) ClassName */
    Map<ModuleDecl,ClassName> abstractTypeNameForModule =
      new HashMap<ModuleDecl,ClassName>();
    Map<ModuleDecl,ClassName> tomMappingNameForModule =
      new HashMap<ModuleDecl,ClassName>();
    /* SortDecl -> SortClass */
    Map<SortDecl,GomClass> sortGomClassForSortDecl =
      new HashMap<SortDecl,GomClass>();
    /* OperatorDecl -> OperatorClass */
    Map<OperatorDecl,GomClass> classForOperatorDecl =
      new HashMap<OperatorDecl,GomClass>();
    /* For each module */
    {{if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch436__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch436__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch436_8= tomMatch436__end__4.getHeadConcModule() ;if ( (tomMatch436_8 instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tom_moduleDecl= tomMatch436_8.getMDecl() ;

        String moduleName = tom_moduleDecl.getModuleName().getName();

        /* create an AbstractType class */
        ClassName abstractTypeName =  tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl), moduleName+"AbstractType") 

;

        ClassName tomMappingName =  tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl), moduleName) 

;
        tomMappingNameForModule.put(tom_moduleDecl,tomMappingName);

        abstractTypeNameForModule.put(tom_moduleDecl,abstractTypeName);
      }}if ( tomMatch436__end__4.isEmptyConcModule() ) {tomMatch436__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch436__end__4= tomMatch436__end__4.getTailConcModule() ;}}} while(!( (tomMatch436__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}


    /* For each sort, create a sort implementation, and operator implementations
       (we don't need to do that per module, since each operator and sort knows
       to which module it belongs) */
    {{if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch437__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch437__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch437_8= tomMatch437__end__4.getHeadConcModule() ;if ( (tomMatch437_8 instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.SortList  tomMatch437_7= tomMatch437_8.getSorts() ;if ( ((tomMatch437_7 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch437_7 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch437__end__12=tomMatch437_7;do {{if (!( tomMatch437__end__12.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch437_16= tomMatch437__end__12.getHeadConcSort() ;if ( (tomMatch437_16 instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch437_15= tomMatch437_16.getDecl() ;if ( (tomMatch437_15 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {





        // get the class name for the sort
        ClassName sortClassName =  tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix( tomMatch437_15.getModuleDecl() )+".types",  tomMatch437_15.getName() ) ;
        sortClassNameForSortDecl.put(tomMatch437_15,sortClassName);
      }}}if ( tomMatch437__end__12.isEmptyConcSort() ) {tomMatch437__end__12=tomMatch437_7;} else {tomMatch437__end__12= tomMatch437__end__12.getTailConcSort() ;}}} while(!( (tomMatch437__end__12==tomMatch437_7) ));}}}if ( tomMatch437__end__4.isEmptyConcModule() ) {tomMatch437__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch437__end__4= tomMatch437__end__4.getTailConcModule() ;}}} while(!( (tomMatch437__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}{{if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch438__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch438__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch438_8= tomMatch438__end__4.getHeadConcModule() ;if ( (tomMatch438_8 instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.SortList  tomMatch438_7= tomMatch438_8.getSorts() ;if ( ((tomMatch438_7 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch438_7 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch438__end__12=tomMatch438_7;do {{if (!( tomMatch438__end__12.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch438_17= tomMatch438__end__12.getHeadConcSort() ;if ( (tomMatch438_17 instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch438_15= tomMatch438_17.getDecl() ;if ( (tomMatch438_15 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) { tom.gom.adt.gom.types.ModuleDecl  tom_moduleDecl= tomMatch438_15.getModuleDecl() ; tom.gom.adt.gom.types.SortDecl  tom_sortDecl=tomMatch438_15; tom.gom.adt.gom.types.OperatorDeclList  tom_oplist= tomMatch438_17.getOperatorDecls() ;








        // get the class name for the sort
        ClassName sortClassName = sortClassNameForSortDecl.get(tom_sortDecl);
        ClassName abstracttypeName = abstractTypeNameForModule.get(tom_moduleDecl);
        ClassName mappingName = tomMappingNameForModule.get(tom_moduleDecl);
        // create operator classes. Also, store a list of all operators for the sort class
        // use a Set to collect slots and avoid duplicates
        Set<SlotField> allSortSlots = new HashSet<SlotField>();
        ClassNameList allOperators =  tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ;
        ClassNameList allVariadicOperators =  tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ;
        {{if ( (tom_oplist instanceof tom.gom.adt.gom.types.OperatorDeclList) ) {if ( (((( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist) instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || ((( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist) instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch439__end__4=(( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist);do {{if (!( tomMatch439__end__4.isEmptyConcOperator() )) { tom.gom.adt.gom.types.OperatorDecl  tomMatch439_11= tomMatch439__end__4.getHeadConcOperator() ;if ( (tomMatch439_11 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.SortDecl  tomMatch439_8= tomMatch439_11.getSort() ; tom.gom.adt.gom.types.Option  tomMatch439_10= tomMatch439_11.getOption() ; String  tom_opname= tomMatch439_11.getName() ;if ( (tomMatch439_8 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) { tom.gom.adt.gom.types.TypedProduction  tom_typedproduction= tomMatch439_11.getProd() ;if ( (tomMatch439_10 instanceof tom.gom.adt.gom.types.option.Details) ) { String  tom_comments= tomMatch439_10.getComments() ;






            String sortNamePackage =  tomMatch439_8.getName() .toLowerCase();
            ClassName operatorClassName =
               tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl)+".types."+sortNamePackage, tom_opname) ;
            SlotFieldList slots =  tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ;
            ClassName variadicOpClassName = null;
            ClassName empty = null;
            {{if ( (tom_typedproduction instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tom_typedproduction) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {

                ClassName clsName = sortClassNameForSortDecl.get( (( tom.gom.adt.gom.types.TypedProduction )tom_typedproduction).getSort() );
                SlotField slotHead =  tom.gom.adt.objects.types.slotfield.SlotField.make("Head"+tom_opname, clsName) ;
                SlotField slotTail =  tom.gom.adt.objects.types.slotfield.SlotField.make("Tail"+tom_opname, sortClassName) ;
                allSortSlots.add(slotHead);
                allSortSlots.add(slotTail);
                slots =  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make(slotHead, tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make(slotTail, tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) ) ;
                // as the operator is variadic, add a Cons and an Empty
                variadicOpClassName =
                   tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl)+".types."+sortNamePackage, tom_opname) ;
                allVariadicOperators =  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make(variadicOpClassName,tom_append_list_ConcClassName(allVariadicOperators, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;
                empty =
                   tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl)+".types."+sortNamePackage, "Empty"+tom_opname) ;
                operatorClassName =
                   tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl)+".types."+sortNamePackage, "Cons"+tom_opname) ;

                allOperators =  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make(empty,tom_append_list_ConcClassName(allOperators, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;
              }}}{if ( (tom_typedproduction instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tom_typedproduction) instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tomMatch440_4= (( tom.gom.adt.gom.types.TypedProduction )tom_typedproduction).getSlots() ;if ( ((tomMatch440_4 instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || (tomMatch440_4 instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) { tom.gom.adt.gom.types.SlotList  tomMatch440__end__9=tomMatch440_4;do {{if (!( tomMatch440__end__9.isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch440_14= tomMatch440__end__9.getHeadConcSlot() ;if ( (tomMatch440_14 instanceof tom.gom.adt.gom.types.slot.Slot) ) {

                ClassName clsName = sortClassNameForSortDecl.get( tomMatch440_14.getSort() );
                SlotField slotfield =  tom.gom.adt.objects.types.slotfield.SlotField.make( tomMatch440_14.getName() , clsName) ;
                allSortSlots.add(slotfield);
                slots = tom_append_list_ConcSlotField(slots, tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make(slotfield, tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) );
              }}if ( tomMatch440__end__9.isEmptyConcSlot() ) {tomMatch440__end__9=tomMatch440_4;} else {tomMatch440__end__9= tomMatch440__end__9.getTailConcSlot() ;}}} while(!( (tomMatch440__end__9==tomMatch440_4) ));}}}}}

            GomClass operatorClass;
            allOperators =  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make(operatorClassName,tom_append_list_ConcClassName(allOperators, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;
            if(variadicOpClassName != null) {
              /* We just processed a variadic operator */
              GomClass consClass =  tom.gom.adt.objects.types.gomclass.OperatorClass.make(operatorClassName, abstracttypeName, variadicOpClassName, mappingName, sortClassName, slots,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() , tom_comments) 






;

              GomClass emptyClass =  tom.gom.adt.objects.types.gomclass.OperatorClass.make(empty, abstracttypeName, variadicOpClassName, mappingName, sortClassName,  tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() , tom_comments) 






;

              operatorClass =  tom.gom.adt.objects.types.gomclass.VariadicOperatorClass.make(variadicOpClassName, abstracttypeName, mappingName, sortClassName, emptyClass, consClass,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() , tom_comments) 






;
            } else {
              operatorClass =  tom.gom.adt.objects.types.gomclass.OperatorClass.make(operatorClassName, abstracttypeName, sortClassName, mappingName, sortClassName, slots,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() , tom_comments) 






;
            }
            classForOperatorDecl.put( tomMatch439__end__4.getHeadConcOperator() ,operatorClass);
            classList =  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(operatorClass,tom_append_list_ConcGomClass(classList, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;
          }}}}if ( tomMatch439__end__4.isEmptyConcOperator() ) {tomMatch439__end__4=(( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist);} else {tomMatch439__end__4= tomMatch439__end__4.getTailConcOperator() ;}}} while(!( (tomMatch439__end__4==(( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist)) ));}}}}

        // create the sort class and add it to the list
        GomClass sortClass =  tom.gom.adt.objects.types.gomclass.SortClass.make(sortClassName, abstracttypeName, mappingName, allOperators, allVariadicOperators, slotFieldListFromSet(allSortSlots),  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) 





;
        sortGomClassForSortDecl.put(tom_sortDecl,sortClass);
        classList =  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(sortClass,tom_append_list_ConcGomClass(classList, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;
      }}}if ( tomMatch438__end__12.isEmptyConcSort() ) {tomMatch438__end__12=tomMatch438_7;} else {tomMatch438__end__12= tomMatch438__end__12.getTailConcSort() ;}}} while(!( (tomMatch438__end__12==tomMatch438_7) ));}}}if ( tomMatch438__end__4.isEmptyConcModule() ) {tomMatch438__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch438__end__4= tomMatch438__end__4.getTailConcModule() ;}}} while(!( (tomMatch438__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}{{if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch441__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch441__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch441_8= tomMatch441__end__4.getHeadConcModule() ;if ( (tomMatch441_8 instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tom_moduleDecl= tomMatch441_8.getMDecl() ;




        String moduleName = tom_moduleDecl.getModuleName().getName();

        GomClassList allOperatorClasses =  tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ;
        GomClassList allSortClasses =  tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ;
        /* TODO improve this part : just for test */
        ModuleDeclList modlist = getGomEnvironment().getModuleDependency(tom_moduleDecl);
        while(!modlist.isEmptyConcModuleDecl()) {
          ModuleDecl imported = modlist.getHeadConcModuleDecl();
          modlist = modlist.getTailConcModuleDecl();
          SortList moduleSorts = getSortsForModule(imported,moduleList);
          SortList sortconsum = moduleSorts;
          while(!sortconsum.isEmptyConcSort()) {
            Sort sort = sortconsum.getHeadConcSort();
            sortconsum = sortconsum.getTailConcSort();
            {{if ( (sort instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )sort) instanceof tom.gom.adt.gom.types.sort.Sort) ) {

                GomClass sortClass = sortGomClassForSortDecl.get( (( tom.gom.adt.gom.types.Sort )sort).getDecl() );
                allSortClasses =  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(sortClass,tom_append_list_ConcGomClass(allSortClasses, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;
              }}}}

          }
          {{if ( (moduleSorts instanceof tom.gom.adt.gom.types.SortList) ) {if ( (((( tom.gom.adt.gom.types.SortList )moduleSorts) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )moduleSorts) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch443__end__4=(( tom.gom.adt.gom.types.SortList )moduleSorts);do {{if (!( tomMatch443__end__4.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch443_8= tomMatch443__end__4.getHeadConcSort() ;if ( (tomMatch443_8 instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch443_7= tomMatch443_8.getOperatorDecls() ;if ( ((tomMatch443_7 instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || (tomMatch443_7 instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) { tom.gom.adt.gom.types.OperatorDeclList  tomMatch443__end__12=tomMatch443_7;do {{if (!( tomMatch443__end__12.isEmptyConcOperator() )) {

              GomClass opClass = classForOperatorDecl.get( tomMatch443__end__12.getHeadConcOperator() );
              allOperatorClasses =  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(opClass,tom_append_list_ConcGomClass(allOperatorClasses, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;
              {{if ( (opClass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )opClass) instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ) {

                  allOperatorClasses =  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( (( tom.gom.adt.objects.types.GomClass )opClass).getEmpty() , tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( (( tom.gom.adt.objects.types.GomClass )opClass).getCons() ,tom_append_list_ConcGomClass(allOperatorClasses, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ) ;
                }}}}

            }if ( tomMatch443__end__12.isEmptyConcOperator() ) {tomMatch443__end__12=tomMatch443_7;} else {tomMatch443__end__12= tomMatch443__end__12.getTailConcOperator() ;}}} while(!( (tomMatch443__end__12==tomMatch443_7) ));}}}if ( tomMatch443__end__4.isEmptyConcSort() ) {tomMatch443__end__4=(( tom.gom.adt.gom.types.SortList )moduleSorts);} else {tomMatch443__end__4= tomMatch443__end__4.getTailConcSort() ;}}} while(!( (tomMatch443__end__4==(( tom.gom.adt.gom.types.SortList )moduleSorts)) ));}}}}

        }

        ClassName abstractTypeClassName = abstractTypeNameForModule.get(tom_moduleDecl);

        /* create a TomMapping */
        ClassName tomMappingName = tomMappingNameForModule.get(tom_moduleDecl);
        GomClass tommappingclass =  tom.gom.adt.objects.types.gomclass.TomMapping.make(tomMappingName, allSortClasses, allOperatorClasses) 

;
        classList =  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(tommappingclass,tom_append_list_ConcGomClass(classList, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;

        /* create the abstractType */
        ClassNameList classSortList = sortClassNames(moduleList);
        ClassName abstractTypeName = abstractTypeNameForModule.get(tom_moduleDecl);
        GomClass abstracttype =
           tom.gom.adt.objects.types.gomclass.AbstractTypeClass.make(abstractTypeName, tomMappingName, classSortList,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) 


;
        classList =  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(abstracttype,tom_append_list_ConcGomClass(classList, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;

      }}if ( tomMatch441__end__4.isEmptyConcModule() ) {tomMatch441__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch441__end__4= tomMatch441__end__4.getTailConcModule() ;}}} while(!( (tomMatch441__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}

    /* Call the hook processor here, to attach hooks to the correct classes */
    /* fist compute the mapping "Decl" -> "ClassName" */
    Map<GomAbstractType,ClassName> declToClassName =
      new HashMap<GomAbstractType,ClassName>();
    /* for ModuleDecl */
    declToClassName.putAll(abstractTypeNameForModule);
    /* for SortDecl */
    for (Map.Entry<SortDecl,GomClass> entry : sortGomClassForSortDecl.entrySet()) {
      GomClass sortClass = entry.getValue();
      declToClassName.put(entry.getKey(),sortClass.getClassName());
    }
    /* for OperatorDecl */
    for (Map.Entry<OperatorDecl,GomClass> entry : classForOperatorDecl.entrySet()) {
      GomClass sortClass = entry.getValue();
      declToClassName.put(entry.getKey(),sortClass.getClassName());
    }
    HookCompiler hcompiler = new HookCompiler(sortClassNameForSortDecl);
    classList = hcompiler.compile(hookDecls,classList,declToClassName);
    return classList;
  }

  private ClassNameList sortClassNames(ModuleList moduleList) {
    ClassNameList classNames =  tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ;
    {{if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch445__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch445__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch445_8= tomMatch445__end__4.getHeadConcModule() ;if ( (tomMatch445_8 instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.SortList  tomMatch445_7= tomMatch445_8.getSorts() ;if ( ((tomMatch445_7 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch445_7 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch445__end__12=tomMatch445_7;do {{if (!( tomMatch445__end__12.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch445_16= tomMatch445__end__12.getHeadConcSort() ;if ( (tomMatch445_16 instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch445_15= tomMatch445_16.getDecl() ;if ( (tomMatch445_15 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {





        classNames =  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix( tomMatch445_15.getModuleDecl() )+".types",  tomMatch445_15.getName() ) ,tom_append_list_ConcClassName(classNames, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;
      }}}if ( tomMatch445__end__12.isEmptyConcSort() ) {tomMatch445__end__12=tomMatch445_7;} else {tomMatch445__end__12= tomMatch445__end__12.getTailConcSort() ;}}} while(!( (tomMatch445__end__12==tomMatch445_7) ));}}}if ( tomMatch445__end__4.isEmptyConcModule() ) {tomMatch445__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch445__end__4= tomMatch445__end__4.getTailConcModule() ;}}} while(!( (tomMatch445__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}

    return classNames;
  }

  /*
   * Get all sort definitions for a given module
   */
  private SortList getSortsForModule(ModuleDecl module, ModuleList moduleList) {
    {{if ( (module instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )module) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) {if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch446__end__6=(( tom.gom.adt.gom.types.ModuleList )moduleList);do {{if (!( tomMatch446__end__6.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch446_11= tomMatch446__end__6.getHeadConcModule() ;if ( (tomMatch446_11 instanceof tom.gom.adt.gom.types.module.Module) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )module)== tomMatch446_11.getMDecl() ) ) {


        return  tomMatch446_11.getSorts() ;
      }}}if ( tomMatch446__end__6.isEmptyConcModule() ) {tomMatch446__end__6=(( tom.gom.adt.gom.types.ModuleList )moduleList);} else {tomMatch446__end__6= tomMatch446__end__6.getTailConcModule() ;}}} while(!( (tomMatch446__end__6==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));}}}}}}

    throw new RuntimeException("Module " + module + " not found");
  }

  public static String packagePrefix(ModuleDecl moduleDecl) {
    String pkgPrefix = "";
    {{if ( (moduleDecl instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )moduleDecl) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch447_1= (( tom.gom.adt.gom.types.ModuleDecl )moduleDecl).getModuleName() ;if ( (tomMatch447_1 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) { String  tom_name= tomMatch447_1.getName() ; String  tom_pkgopt= (( tom.gom.adt.gom.types.ModuleDecl )moduleDecl).getPkg() ;

        if(!tom_pkgopt.equals("")) {
          pkgPrefix = tom_pkgopt+ "." + tom_name;
        } else {
          pkgPrefix = tom_name;
        }
      }}}}}

    return pkgPrefix.toLowerCase();
  }

  private SlotFieldList slotFieldListFromSet(Set<SlotField> slotFieldSet) {
    SlotFieldList list =  tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ;
    for (SlotField slot : slotFieldSet ) {
      list = tom_append_list_ConcSlotField(list, tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make(slot, tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) );
    }
    return list;
  }

  private ClassNameList allClassForImports(
      Map classMap,
      ModuleDecl moduleDecl) {
    ClassNameList importedList =  tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ;
    ModuleDeclList importedModulelist = getGomEnvironment().getModuleDependency(moduleDecl);
    while(!importedModulelist.isEmptyConcModuleDecl()) {
      ModuleDecl imported = importedModulelist.getHeadConcModuleDecl();
      importedModulelist = importedModulelist.getTailConcModuleDecl();
      if (!imported.equals(moduleDecl)) {
        ClassName importedclass = (ClassName)classMap.get(imported);
        importedList =  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make(importedclass,tom_append_list_ConcClassName(importedList, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;
      }
    }
    return importedList;
  }
}
