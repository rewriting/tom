/*
* Gom
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

package tom.gom.compiler;

import java.util.*;

import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;

import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;

public class Compiler {



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
  
  private static   tom.gom.adt.objects.types.HookList  tom_append_list_ConcHook( tom.gom.adt.objects.types.HookList l1,  tom.gom.adt.objects.types.HookList  l2) {
    if( l1.isEmptyConcHook() ) {
      return l2;
    } else if( l2.isEmptyConcHook() ) {
      return l1;
    } else if(  l1.getTailConcHook() .isEmptyConcHook() ) {
      return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,l2) ;
    } else {
      return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,tom_append_list_ConcHook( l1.getTailConcHook() ,l2)) ;
    }
  }
  private static   tom.gom.adt.objects.types.HookList  tom_get_slice_ConcHook( tom.gom.adt.objects.types.HookList  begin,  tom.gom.adt.objects.types.HookList  end, tom.gom.adt.objects.types.HookList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcHook()  ||  (end== tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( begin.getHeadConcHook() ,( tom.gom.adt.objects.types.HookList )tom_get_slice_ConcHook( begin.getTailConcHook() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.objects.types.GomClassList  tom_append_list_ConcGomClass( tom.gom.adt.objects.types.GomClassList l1,  tom.gom.adt.objects.types.GomClassList  l2) {
    if( l1.isEmptyConcGomClass() ) {
      return l2;
    } else if( l2.isEmptyConcGomClass() ) {
      return l1;
    } else if(  l1.getTailConcGomClass() .isEmptyConcGomClass() ) {
      return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,l2) ;
    } else {
      return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,tom_append_list_ConcGomClass( l1.getTailConcGomClass() ,l2)) ;
    }
  }
  private static   tom.gom.adt.objects.types.GomClassList  tom_get_slice_ConcGomClass( tom.gom.adt.objects.types.GomClassList  begin,  tom.gom.adt.objects.types.GomClassList  end, tom.gom.adt.objects.types.GomClassList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcGomClass()  ||  (end== tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( begin.getHeadConcGomClass() ,( tom.gom.adt.objects.types.GomClassList )tom_get_slice_ConcGomClass( begin.getTailConcGomClass() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {
    if( l1.isEmptyConcSlotField() ) {
      return l2;
    } else if( l2.isEmptyConcSlotField() ) {
      return l1;
    } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {
      return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;
    } else {
      return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;
    }
  }
  private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcSlotField()  ||  (end== tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.objects.types.ClassNameList  tom_append_list_ConcClassName( tom.gom.adt.objects.types.ClassNameList l1,  tom.gom.adt.objects.types.ClassNameList  l2) {
    if( l1.isEmptyConcClassName() ) {
      return l2;
    } else if( l2.isEmptyConcClassName() ) {
      return l1;
    } else if(  l1.getTailConcClassName() .isEmptyConcClassName() ) {
      return  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( l1.getHeadConcClassName() ,l2) ;
    } else {
      return  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( l1.getHeadConcClassName() ,tom_append_list_ConcClassName( l1.getTailConcClassName() ,l2)) ;
    }
  }
  private static   tom.gom.adt.objects.types.ClassNameList  tom_get_slice_ConcClassName( tom.gom.adt.objects.types.ClassNameList  begin,  tom.gom.adt.objects.types.ClassNameList  end, tom.gom.adt.objects.types.ClassNameList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcClassName()  ||  (end== tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( begin.getHeadConcClassName() ,( tom.gom.adt.objects.types.ClassNameList )tom_get_slice_ConcClassName( begin.getTailConcClassName() ,end,tail)) ;
  }
  

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
GomClassList classList = 
 tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ;

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

{
{
if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {
if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) {
 tom.gom.adt.gom.types.ModuleList  tomMatch550__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
do {
{
if (!( tomMatch550__end__4.isEmptyConcModule() )) {
 tom.gom.adt.gom.types.Module  tomMatch550_8= tomMatch550__end__4.getHeadConcModule() ;
if ( (tomMatch550_8 instanceof tom.gom.adt.gom.types.module.Module) ) {
 tom.gom.adt.gom.types.ModuleDecl  tom_moduleDecl= tomMatch550_8.getMDecl() ;

String moduleName = 
tom_moduleDecl.getModuleName().getName();

/* create an AbstractType class */
ClassName abstractTypeName = 
 tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl), moduleName+"AbstractType") ;

ClassName tomMappingName = 
 tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl), moduleName) ;
tomMappingNameForModule.put(
tom_moduleDecl,tomMappingName);

abstractTypeNameForModule.put(
tom_moduleDecl,abstractTypeName);


}
}
if ( tomMatch550__end__4.isEmptyConcModule() ) {
tomMatch550__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
} else {
tomMatch550__end__4= tomMatch550__end__4.getTailConcModule() ;
}

}
} while(!( (tomMatch550__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));
}
}

}

}


/* For each sort, create a sort implementation, and operator implementations
(we don't need to do that per module, since each operator and sort knows
to which module it belongs) */

{
{
if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {
if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) {
 tom.gom.adt.gom.types.ModuleList  tomMatch551__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
do {
{
if (!( tomMatch551__end__4.isEmptyConcModule() )) {
 tom.gom.adt.gom.types.Module  tomMatch551_8= tomMatch551__end__4.getHeadConcModule() ;
if ( (tomMatch551_8 instanceof tom.gom.adt.gom.types.module.Module) ) {
 tom.gom.adt.gom.types.SortList  tomMatch551_7= tomMatch551_8.getSorts() ;
if ( ((tomMatch551_7 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch551_7 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) {
 tom.gom.adt.gom.types.SortList  tomMatch551__end__12=tomMatch551_7;
do {
{
if (!( tomMatch551__end__12.isEmptyConcSort() )) {
 tom.gom.adt.gom.types.Sort  tomMatch551_16= tomMatch551__end__12.getHeadConcSort() ;
if ( (tomMatch551_16 instanceof tom.gom.adt.gom.types.sort.Sort) ) {
 tom.gom.adt.gom.types.SortDecl  tomMatch551_15= tomMatch551_16.getDecl() ;
if ( (tomMatch551_15 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

// get the class name for the sort
ClassName sortClassName = 
 tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix( tomMatch551_15.getModuleDecl() )+".types",  tomMatch551_15.getName() ) ;
sortClassNameForSortDecl.put(
tomMatch551_15,sortClassName);


}
}
}
if ( tomMatch551__end__12.isEmptyConcSort() ) {
tomMatch551__end__12=tomMatch551_7;
} else {
tomMatch551__end__12= tomMatch551__end__12.getTailConcSort() ;
}

}
} while(!( (tomMatch551__end__12==tomMatch551_7) ));
}
}
}
if ( tomMatch551__end__4.isEmptyConcModule() ) {
tomMatch551__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
} else {
tomMatch551__end__4= tomMatch551__end__4.getTailConcModule() ;
}

}
} while(!( (tomMatch551__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));
}
}

}

}
{
{
if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {
if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) {
 tom.gom.adt.gom.types.ModuleList  tomMatch552__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
do {
{
if (!( tomMatch552__end__4.isEmptyConcModule() )) {
 tom.gom.adt.gom.types.Module  tomMatch552_8= tomMatch552__end__4.getHeadConcModule() ;
if ( (tomMatch552_8 instanceof tom.gom.adt.gom.types.module.Module) ) {
 tom.gom.adt.gom.types.SortList  tomMatch552_7= tomMatch552_8.getSorts() ;
if ( ((tomMatch552_7 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch552_7 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) {
 tom.gom.adt.gom.types.SortList  tomMatch552__end__12=tomMatch552_7;
do {
{
if (!( tomMatch552__end__12.isEmptyConcSort() )) {
 tom.gom.adt.gom.types.Sort  tomMatch552_17= tomMatch552__end__12.getHeadConcSort() ;
if ( (tomMatch552_17 instanceof tom.gom.adt.gom.types.sort.Sort) ) {
 tom.gom.adt.gom.types.SortDecl  tomMatch552_15= tomMatch552_17.getDecl() ;
if ( (tomMatch552_15 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {
 tom.gom.adt.gom.types.ModuleDecl  tom_moduleDecl= tomMatch552_15.getModuleDecl() ;
 tom.gom.adt.gom.types.SortDecl  tom_sortDecl=tomMatch552_15;
 tom.gom.adt.gom.types.OperatorDeclList  tom_oplist= tomMatch552_17.getOperatorDecls() ;

// get the class name for the sort
ClassName sortClassName = sortClassNameForSortDecl.get(
tom_sortDecl);
ClassName abstracttypeName = abstractTypeNameForModule.get(
tom_moduleDecl);
ClassName mappingName = tomMappingNameForModule.get(
tom_moduleDecl);
// create operator classes. Also, store a list of all operators for the sort class
// use a Set to collect slots and avoid duplicates
Set<SlotField> allSortSlots = new HashSet<SlotField>();
ClassNameList allOperators = 
 tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ;
ClassNameList allVariadicOperators = 
 tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ;

{
{
if ( (tom_oplist instanceof tom.gom.adt.gom.types.OperatorDeclList) ) {
if ( (((( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist) instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || ((( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist) instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) {
 tom.gom.adt.gom.types.OperatorDeclList  tomMatch553__end__4=(( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist);
do {
{
if (!( tomMatch553__end__4.isEmptyConcOperator() )) {
 tom.gom.adt.gom.types.OperatorDecl  tomMatch553_11= tomMatch553__end__4.getHeadConcOperator() ;
if ( (tomMatch553_11 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
 tom.gom.adt.gom.types.SortDecl  tomMatch553_8= tomMatch553_11.getSort() ;
 String  tom_opname= tomMatch553_11.getName() ;
if ( (tomMatch553_8 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {
 tom.gom.adt.gom.types.TypedProduction  tom_typedproduction= tomMatch553_11.getProd() ;

String comments = getCommentsFromOption(
 tomMatch553_11.getOption() );
String sortNamePackage = 
 tomMatch553_8.getName() .toLowerCase();
ClassName operatorClassName =

 tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl)+".types."+sortNamePackage, tom_opname) ;
SlotFieldList slots = 
 tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ;
ClassName variadicOpClassName = null;
ClassName empty = null;

{
{
if ( (tom_typedproduction instanceof tom.gom.adt.gom.types.TypedProduction) ) {
if ( ((( tom.gom.adt.gom.types.TypedProduction )tom_typedproduction) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {

ClassName clsName = sortClassNameForSortDecl.get(
 (( tom.gom.adt.gom.types.TypedProduction )tom_typedproduction).getSort() );
SlotField slotHead = 
 tom.gom.adt.objects.types.slotfield.SlotField.make("Head"+tom_opname, clsName) ;
SlotField slotTail = 
 tom.gom.adt.objects.types.slotfield.SlotField.make("Tail"+tom_opname, sortClassName) ;
allSortSlots.add(
slotHead);
allSortSlots.add(
slotTail);
slots = 
 tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make(slotHead, tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make(slotTail, tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) ) ;
// as the operator is variadic, add a Cons and an Empty
variadicOpClassName =

 tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl)+".types."+sortNamePackage, tom_opname) ;
allVariadicOperators = 
 tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make(variadicOpClassName,tom_append_list_ConcClassName(allVariadicOperators, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;
empty =

 tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl)+".types."+sortNamePackage, "Empty"+tom_opname) ;
operatorClassName =

 tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix(tom_moduleDecl)+".types."+sortNamePackage, "Cons"+tom_opname) ;

allOperators = 
 tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make(empty,tom_append_list_ConcClassName(allOperators, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;


}
}

}
{
if ( (tom_typedproduction instanceof tom.gom.adt.gom.types.TypedProduction) ) {
if ( ((( tom.gom.adt.gom.types.TypedProduction )tom_typedproduction) instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) {
 tom.gom.adt.gom.types.SlotList  tomMatch554_4= (( tom.gom.adt.gom.types.TypedProduction )tom_typedproduction).getSlots() ;
if ( ((tomMatch554_4 instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || (tomMatch554_4 instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {
 tom.gom.adt.gom.types.SlotList  tomMatch554__end__9=tomMatch554_4;
do {
{
if (!( tomMatch554__end__9.isEmptyConcSlot() )) {
 tom.gom.adt.gom.types.Slot  tomMatch554_14= tomMatch554__end__9.getHeadConcSlot() ;
if ( (tomMatch554_14 instanceof tom.gom.adt.gom.types.slot.Slot) ) {

ClassName clsName = sortClassNameForSortDecl.get(
 tomMatch554_14.getSort() );
SlotField slotfield = 
 tom.gom.adt.objects.types.slotfield.SlotField.make( tomMatch554_14.getName() , clsName) ;
allSortSlots.add(slotfield);
slots = 
tom_append_list_ConcSlotField(slots, tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make(slotfield, tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) );


}
}
if ( tomMatch554__end__9.isEmptyConcSlot() ) {
tomMatch554__end__9=tomMatch554_4;
} else {
tomMatch554__end__9= tomMatch554__end__9.getTailConcSlot() ;
}

}
} while(!( (tomMatch554__end__9==tomMatch554_4) ));
}
}
}

}


}

GomClass operatorClass;
allOperators = 
 tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make(operatorClassName,tom_append_list_ConcClassName(allOperators, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;
if(variadicOpClassName != null) {
/* We just processed a variadic operator */
GomClass consClass = 
 tom.gom.adt.objects.types.gomclass.OperatorClass.make(operatorClassName, abstracttypeName, variadicOpClassName, mappingName, sortClassName, slots,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() , comments) ;

GomClass emptyClass = 
 tom.gom.adt.objects.types.gomclass.OperatorClass.make(empty, abstracttypeName, variadicOpClassName, mappingName, sortClassName,  tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() , comments) ;

operatorClass = 
 tom.gom.adt.objects.types.gomclass.VariadicOperatorClass.make(variadicOpClassName, abstracttypeName, mappingName, sortClassName, emptyClass, consClass,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() , comments) ;
} else {
operatorClass = 
 tom.gom.adt.objects.types.gomclass.OperatorClass.make(operatorClassName, abstracttypeName, sortClassName, mappingName, sortClassName, slots,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() , comments) ;
}
classForOperatorDecl.put(
 tomMatch553__end__4.getHeadConcOperator() ,operatorClass);
classList = 
 tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(operatorClass,tom_append_list_ConcGomClass(classList, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;


}
}
}
if ( tomMatch553__end__4.isEmptyConcOperator() ) {
tomMatch553__end__4=(( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist);
} else {
tomMatch553__end__4= tomMatch553__end__4.getTailConcOperator() ;
}

}
} while(!( (tomMatch553__end__4==(( tom.gom.adt.gom.types.OperatorDeclList )tom_oplist)) ));
}
}

}

}

// create the sort class and add it to the list
GomClass sortClass = 
 tom.gom.adt.objects.types.gomclass.SortClass.make(sortClassName, abstracttypeName, mappingName, allOperators, allVariadicOperators, slotFieldListFromSet(allSortSlots),  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) ;
sortGomClassForSortDecl.put(
tom_sortDecl,sortClass);
classList = 
 tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(sortClass,tom_append_list_ConcGomClass(classList, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;


}
}
}
if ( tomMatch552__end__12.isEmptyConcSort() ) {
tomMatch552__end__12=tomMatch552_7;
} else {
tomMatch552__end__12= tomMatch552__end__12.getTailConcSort() ;
}

}
} while(!( (tomMatch552__end__12==tomMatch552_7) ));
}
}
}
if ( tomMatch552__end__4.isEmptyConcModule() ) {
tomMatch552__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
} else {
tomMatch552__end__4= tomMatch552__end__4.getTailConcModule() ;
}

}
} while(!( (tomMatch552__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));
}
}

}

}
{
{
if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {
if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) {
 tom.gom.adt.gom.types.ModuleList  tomMatch555__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
do {
{
if (!( tomMatch555__end__4.isEmptyConcModule() )) {
 tom.gom.adt.gom.types.Module  tomMatch555_8= tomMatch555__end__4.getHeadConcModule() ;
if ( (tomMatch555_8 instanceof tom.gom.adt.gom.types.module.Module) ) {
 tom.gom.adt.gom.types.ModuleDecl  tom_moduleDecl= tomMatch555_8.getMDecl() ;

String moduleName = 
tom_moduleDecl.getModuleName().getName();

GomClassList allOperatorClasses = 
 tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ;
GomClassList allSortClasses = 
 tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ;
/* TODO improve this part : just for test */
ModuleDeclList modlist = getGomEnvironment().getModuleDependency(
tom_moduleDecl);
while(!modlist.isEmptyConcModuleDecl()) {
ModuleDecl imported = modlist.getHeadConcModuleDecl();
modlist = modlist.getTailConcModuleDecl();
SortList moduleSorts = getSortsForModule(imported,moduleList);
SortList sortconsum = moduleSorts;
while(!sortconsum.isEmptyConcSort()) {
Sort sort = sortconsum.getHeadConcSort();
sortconsum = sortconsum.getTailConcSort();

{
{
if ( (sort instanceof tom.gom.adt.gom.types.Sort) ) {
if ( ((( tom.gom.adt.gom.types.Sort )sort) instanceof tom.gom.adt.gom.types.sort.Sort) ) {

GomClass sortClass = sortGomClassForSortDecl.get(
 (( tom.gom.adt.gom.types.Sort )sort).getDecl() );
allSortClasses = 
 tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(sortClass,tom_append_list_ConcGomClass(allSortClasses, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;


}
}

}

}

}

{
{
if ( (moduleSorts instanceof tom.gom.adt.gom.types.SortList) ) {
if ( (((( tom.gom.adt.gom.types.SortList )moduleSorts) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )moduleSorts) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) {
 tom.gom.adt.gom.types.SortList  tomMatch557__end__4=(( tom.gom.adt.gom.types.SortList )moduleSorts);
do {
{
if (!( tomMatch557__end__4.isEmptyConcSort() )) {
 tom.gom.adt.gom.types.Sort  tomMatch557_8= tomMatch557__end__4.getHeadConcSort() ;
if ( (tomMatch557_8 instanceof tom.gom.adt.gom.types.sort.Sort) ) {
 tom.gom.adt.gom.types.OperatorDeclList  tomMatch557_7= tomMatch557_8.getOperatorDecls() ;
if ( ((tomMatch557_7 instanceof tom.gom.adt.gom.types.operatordecllist.ConsConcOperator) || (tomMatch557_7 instanceof tom.gom.adt.gom.types.operatordecllist.EmptyConcOperator)) ) {
 tom.gom.adt.gom.types.OperatorDeclList  tomMatch557__end__12=tomMatch557_7;
do {
{
if (!( tomMatch557__end__12.isEmptyConcOperator() )) {

GomClass opClass = classForOperatorDecl.get(
 tomMatch557__end__12.getHeadConcOperator() );
allOperatorClasses = 
 tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(opClass,tom_append_list_ConcGomClass(allOperatorClasses, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;

{
{
if ( (opClass instanceof tom.gom.adt.objects.types.GomClass) ) {
if ( ((( tom.gom.adt.objects.types.GomClass )opClass) instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ) {

allOperatorClasses = 
 tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( (( tom.gom.adt.objects.types.GomClass )opClass).getEmpty() , tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( (( tom.gom.adt.objects.types.GomClass )opClass).getCons() ,tom_append_list_ConcGomClass(allOperatorClasses, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ) ;


}
}

}

}



}
if ( tomMatch557__end__12.isEmptyConcOperator() ) {
tomMatch557__end__12=tomMatch557_7;
} else {
tomMatch557__end__12= tomMatch557__end__12.getTailConcOperator() ;
}

}
} while(!( (tomMatch557__end__12==tomMatch557_7) ));
}
}
}
if ( tomMatch557__end__4.isEmptyConcSort() ) {
tomMatch557__end__4=(( tom.gom.adt.gom.types.SortList )moduleSorts);
} else {
tomMatch557__end__4= tomMatch557__end__4.getTailConcSort() ;
}

}
} while(!( (tomMatch557__end__4==(( tom.gom.adt.gom.types.SortList )moduleSorts)) ));
}
}

}

}

}

ClassName abstractTypeClassName = abstractTypeNameForModule.get(
tom_moduleDecl);

/* create a TomMapping */
ClassName tomMappingName = tomMappingNameForModule.get(
tom_moduleDecl);
GomClass tommappingclass = 
 tom.gom.adt.objects.types.gomclass.TomMapping.make(tomMappingName, allSortClasses, allOperatorClasses) ;
classList = 
 tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(tommappingclass,tom_append_list_ConcGomClass(classList, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;

/* create the abstractType */
ClassNameList classSortList = sortClassNames(
moduleList);
ClassName abstractTypeName = abstractTypeNameForModule.get(
tom_moduleDecl);
GomClass abstracttype =

 tom.gom.adt.objects.types.gomclass.AbstractTypeClass.make(abstractTypeName, tomMappingName, classSortList,  tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) ;
classList = 
 tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make(abstracttype,tom_append_list_ConcGomClass(classList, tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() )) ;



}
}
if ( tomMatch555__end__4.isEmptyConcModule() ) {
tomMatch555__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
} else {
tomMatch555__end__4= tomMatch555__end__4.getTailConcModule() ;
}

}
} while(!( (tomMatch555__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));
}
}

}

}

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
ClassNameList classNames = 
 tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ;

{
{
if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {
if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) {
 tom.gom.adt.gom.types.ModuleList  tomMatch559__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
do {
{
if (!( tomMatch559__end__4.isEmptyConcModule() )) {
 tom.gom.adt.gom.types.Module  tomMatch559_8= tomMatch559__end__4.getHeadConcModule() ;
if ( (tomMatch559_8 instanceof tom.gom.adt.gom.types.module.Module) ) {
 tom.gom.adt.gom.types.SortList  tomMatch559_7= tomMatch559_8.getSorts() ;
if ( ((tomMatch559_7 instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || (tomMatch559_7 instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) {
 tom.gom.adt.gom.types.SortList  tomMatch559__end__12=tomMatch559_7;
do {
{
if (!( tomMatch559__end__12.isEmptyConcSort() )) {
 tom.gom.adt.gom.types.Sort  tomMatch559_16= tomMatch559__end__12.getHeadConcSort() ;
if ( (tomMatch559_16 instanceof tom.gom.adt.gom.types.sort.Sort) ) {
 tom.gom.adt.gom.types.SortDecl  tomMatch559_15= tomMatch559_16.getDecl() ;
if ( (tomMatch559_15 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

classNames = 
 tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( tom.gom.adt.objects.types.classname.ClassName.make(packagePrefix( tomMatch559_15.getModuleDecl() )+".types",  tomMatch559_15.getName() ) ,tom_append_list_ConcClassName(classNames, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;


}
}
}
if ( tomMatch559__end__12.isEmptyConcSort() ) {
tomMatch559__end__12=tomMatch559_7;
} else {
tomMatch559__end__12= tomMatch559__end__12.getTailConcSort() ;
}

}
} while(!( (tomMatch559__end__12==tomMatch559_7) ));
}
}
}
if ( tomMatch559__end__4.isEmptyConcModule() ) {
tomMatch559__end__4=(( tom.gom.adt.gom.types.ModuleList )moduleList);
} else {
tomMatch559__end__4= tomMatch559__end__4.getTailConcModule() ;
}

}
} while(!( (tomMatch559__end__4==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));
}
}

}

}

return classNames;
}

/*
* Get all sort definitions for a given module
*/
private SortList getSortsForModule(ModuleDecl module, ModuleList moduleList) {

{
{
if ( (module instanceof tom.gom.adt.gom.types.ModuleDecl) ) {
if ( ((( tom.gom.adt.gom.types.ModuleDecl )module) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) {
if ( (moduleList instanceof tom.gom.adt.gom.types.ModuleList) ) {
if ( (((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )moduleList) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) {
 tom.gom.adt.gom.types.ModuleList  tomMatch560__end__6=(( tom.gom.adt.gom.types.ModuleList )moduleList);
do {
{
if (!( tomMatch560__end__6.isEmptyConcModule() )) {
 tom.gom.adt.gom.types.Module  tomMatch560_11= tomMatch560__end__6.getHeadConcModule() ;
if ( (tomMatch560_11 instanceof tom.gom.adt.gom.types.module.Module) ) {
if ( ((( tom.gom.adt.gom.types.ModuleDecl )module)== tomMatch560_11.getMDecl() ) ) {

return 
 tomMatch560_11.getSorts() ;


}
}
}
if ( tomMatch560__end__6.isEmptyConcModule() ) {
tomMatch560__end__6=(( tom.gom.adt.gom.types.ModuleList )moduleList);
} else {
tomMatch560__end__6= tomMatch560__end__6.getTailConcModule() ;
}

}
} while(!( (tomMatch560__end__6==(( tom.gom.adt.gom.types.ModuleList )moduleList)) ));
}
}
}
}

}

}

throw new RuntimeException("Module " + module + " not found");
}

public static String packagePrefix(ModuleDecl moduleDecl) {
String pkgPrefix = "";

{
{
if ( (moduleDecl instanceof tom.gom.adt.gom.types.ModuleDecl) ) {
if ( ((( tom.gom.adt.gom.types.ModuleDecl )moduleDecl) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) {
 tom.gom.adt.gom.types.GomModuleName  tomMatch561_1= (( tom.gom.adt.gom.types.ModuleDecl )moduleDecl).getModuleName() ;
if ( (tomMatch561_1 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {
 String  tom_name= tomMatch561_1.getName() ;
 String  tom_pkgopt= (( tom.gom.adt.gom.types.ModuleDecl )moduleDecl).getPkg() ;

if(!
tom_pkgopt.equals("")) {
pkgPrefix = 
tom_pkgopt+ "." + 
tom_name;
} else {
pkgPrefix = 
tom_name;
}


}
}
}

}

}

return pkgPrefix.toLowerCase();
}

private SlotFieldList slotFieldListFromSet(Set<SlotField> slotFieldSet) {
SlotFieldList list = 
 tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ;
for (SlotField slot : slotFieldSet ) {
list = 
tom_append_list_ConcSlotField(list, tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make(slot, tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) );
}
return list;
}

private ClassNameList allClassForImports(
Map classMap,
ModuleDecl moduleDecl) {
ClassNameList importedList = 
 tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ;
ModuleDeclList importedModulelist = getGomEnvironment().getModuleDependency(moduleDecl);
while(!importedModulelist.isEmptyConcModuleDecl()) {
ModuleDecl imported = importedModulelist.getHeadConcModuleDecl();
importedModulelist = importedModulelist.getTailConcModuleDecl();
if (!imported.equals(moduleDecl)) {
ClassName importedclass = (ClassName)classMap.get(imported);
importedList = 
 tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make(importedclass,tom_append_list_ConcClassName(importedList, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() )) ;
}
}
return importedList;
}

private static String getCommentsFromOption(Option option) {
String comments = "";

{
{
if ( (option instanceof tom.gom.adt.gom.types.Option) ) {
if ( ((( tom.gom.adt.gom.types.Option )option) instanceof tom.gom.adt.gom.types.option.Details) ) {

comments = 
 (( tom.gom.adt.gom.types.Option )option).getComments() ;


}
}

}
{
if ( (option instanceof tom.gom.adt.gom.types.Option) ) {
if ( (((( tom.gom.adt.gom.types.Option )option) instanceof tom.gom.adt.gom.types.option.ConsOptionList) || ((( tom.gom.adt.gom.types.Option )option) instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) ) {
 tom.gom.adt.gom.types.Option  tomMatch562__end__7=(( tom.gom.adt.gom.types.Option )option);
do {
{
if (!( (  tomMatch562__end__7.isEmptyOptionList()  ||  (tomMatch562__end__7== tom.gom.adt.gom.types.option.EmptyOptionList.make() )  ) )) {
 tom.gom.adt.gom.types.Option  tomMatch562_11=(( ((tomMatch562__end__7 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (tomMatch562__end__7 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )?( tomMatch562__end__7.getHeadOptionList() ):(tomMatch562__end__7));
if ( (tomMatch562_11 instanceof tom.gom.adt.gom.types.option.Details) ) {

comments += 
 tomMatch562_11.getComments() ;


}
}
if ( (  tomMatch562__end__7.isEmptyOptionList()  ||  (tomMatch562__end__7== tom.gom.adt.gom.types.option.EmptyOptionList.make() )  ) ) {
tomMatch562__end__7=(( tom.gom.adt.gom.types.Option )option);
} else {
tomMatch562__end__7=(( ((tomMatch562__end__7 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (tomMatch562__end__7 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )?( tomMatch562__end__7.getTailOptionList() ):( tom.gom.adt.gom.types.option.EmptyOptionList.make() ));
}

}
} while(!( (tomMatch562__end__7==(( tom.gom.adt.gom.types.Option )option)) ));
}
}

}


}

return comments;
}
}
