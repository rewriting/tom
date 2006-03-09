/*
 *
 * GOM
 *
 * Copyright (C) 2006 INRIA
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

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

public class GomCompiler {

  %include { ../adt/gom/Gom.tom}
  %include { ../adt/objects/Objects.tom}

  %include { mutraveler.tom }

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  Map sortClassNameForSortDecl = environment().builtinSortClassMap();

  public GomClassList compile(SortList sortList) {
    GomClassList classList = `concGomClass();

    Map abstractTypeNameForModule = new HashMap();
    Map factoryNameForModule = new HashMap();
    Map visitorNameForModule = new HashMap();
    Map sortGomClassForSortDecl = new HashMap();
    Map classForOperatorDecl = new HashMap();
    /* For each module */
    Iterator it = getModuleDeclSet(sortList).iterator();
    while(it.hasNext()) {
      ModuleDecl moduleDecl = (ModuleDecl) it.next();
      String moduleName = moduleDecl.getModuleName().getName();

      /* create an AbstractType class */
      ClassName abstractTypeName = `ClassName(
          packagePrefix(moduleDecl),
          moduleName+"AbstractType");
      ClassName factoryName = `ClassName(
          packagePrefix(moduleDecl),
          moduleName+"Factory");
      factoryNameForModule.put(moduleDecl,factoryName);

      // get all classnames for all sorts (in the module or imported)
      ClassNameList classSortList = sortClassNames(sortList);

      /* create a Visitor class name */
      ClassName visitorName = `ClassName(packagePrefix(moduleDecl),moduleName+"Visitor");
      visitorNameForModule.put(moduleDecl,visitorName);

      abstractTypeNameForModule.put(moduleDecl,abstractTypeName);
      GomClass abstracttype = `AbstractTypeClass(abstractTypeName,factoryName,visitorName,classSortList);
      classList = `concGomClass(abstracttype,classList*);

    }

    /* For each sort, create a sort implementation, and operator implementations
       (we don't need to do that per module, since each operator and sort knows
       to which module it belongs) */
    SortList consum = sortList;
    while(!consum.isEmpty()) {
      Sort sort = consum.getHead();
      consum=consum.getTail();
      // get the class name for the sort
      %match(Sort sort) {
        Sort[decl=decl@SortDecl[name=sortname,moduleDecl=moduleDecl]] -> {
          ClassName sortClassName = `ClassName(packagePrefix(moduleDecl)+".types",sortname);
          sortClassNameForSortDecl.put(`decl,sortClassName);
        }
      }
    }
    consum = sortList;
    while(!consum.isEmpty()) {
      Sort sort = consum.getHead();
      consum=consum.getTail();
      // get the class name for the sort
      %match(Sort sort) {
        Sort[decl=sortDecl@SortDecl[moduleDecl=moduleDecl],operators=oplist] -> {
          ClassName sortClassName = (ClassName)sortClassNameForSortDecl.get(`sortDecl);
          ClassName abstracttypeName = (ClassName)abstractTypeNameForModule.get(`moduleDecl);
          ClassName factoryName = (ClassName)factoryNameForModule.get(`moduleDecl);
          ClassName visitorName = (ClassName)visitorNameForModule.get(`moduleDecl);

          // create operator classes. Also, store a list of all operators for the sort class
          // use a Set to collect slots and avoid duplicates
          Set allSortSlots = new HashSet();
          ClassNameList allOperators = `concClassName();
          %match(OperatorDeclList `oplist) {
            concOperator(_*,
                         opdecl@OperatorDecl[name=opname,
                                             sort=SortDecl[name=sortName],
                                             prod=typedproduction,
                                             hooks=hookList],
                         _*) -> {
              String sortNamePackage = `sortName.toLowerCase();
              ClassName operatorClassName = `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,opname);
              SlotFieldList slots = `concSlotField();
              ClassName empty = null;
              %match(TypedProduction typedproduction) {
                Variadic[sort=domain] -> {
                  ClassName clsName = (ClassName)sortClassNameForSortDecl.get(`domain);
                  SlotField slotHead = `SlotField(opname+"Head",clsName);
                  SlotField slotTail = `SlotField(opname+"Tail",sortClassName);
                  allSortSlots.add(`slotHead);
                  allSortSlots.add(`slotTail);
                  slots = `concSlotField(slotHead,slotTail);
                  // as the operator is varyadic, add a Cons and an Empty
                  empty = `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,opname+"Empty");
                  operatorClassName = `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,opname+"Cons");

                  allOperators = `concClassName(empty,allOperators*);
                  GomClass emptyClass = `OperatorClass(empty,factoryName,abstracttypeName,sortClassName,visitorName,concSlotField(),concHook());
                  classForOperatorDecl.put(`opdecl,emptyClass); 
                  classList = `concGomClass(emptyClass,classList*);
                }
                Slots(concSlot(_*,Slot[name=slotname,sort=domain],_*)) -> {
                  ClassName clsName = (ClassName)sortClassNameForSortDecl.get(`domain);
                  SlotField slotfield = `SlotField(slotname,clsName);
                  allSortSlots.add(slotfield);
                  slots = `concSlotField(slots*,slotfield);
                }
              }
              GomClass operatorClass;
              allOperators = `concClassName(operatorClassName,allOperators*);
              HookList operatorHooks = makeHooksFromHookDecls(`hookList);
              if (empty!= null) { // We just processed a variadic operator
                operatorClass = `VariadicOperatorClass(operatorClassName,
                                                       factoryName,
                                                       abstracttypeName,
                                                       sortClassName,
                                                       visitorName,
                                                       slots,empty,opname,
                                                       operatorHooks);
              } else {
                operatorClass = `OperatorClass(operatorClassName,
                                               factoryName,
                                               abstracttypeName,
                                               sortClassName,
                                               visitorName,slots,
                                               operatorHooks);
              }
              classForOperatorDecl.put(`opdecl,operatorClass);
              classList = `concGomClass(operatorClass,classList*);
            }
          }
          // create the sort class and add it to the list
          GomClass sortClass = `SortClass(sortClassName,
                                          factoryName,
                                          abstracttypeName,
                                          visitorName,
                                          allOperators,
                                          slotFieldListFromSet(allSortSlots));
          sortGomClassForSortDecl.put(`sortDecl,sortClass);
          classList = `concGomClass(sortClass,classList*);
        }
      }
    }

    // Create the factories in the end, so that we know all names
    // For each module
    it = getModuleDeclSet(sortList).iterator(); 
    while(it.hasNext()) {
      ModuleDecl moduleDecl = (ModuleDecl) it.next();
      String moduleName = moduleDecl.getModuleName().getName();

      // We get all Operator Classes for this module, and all modules imported by this one
      ClassNameList importedFactories = `concClassName();
      ModuleDeclList modlist = environment().getModuleDependency(moduleDecl);
      while(!modlist.isEmpty()) {
        ModuleDecl imported = modlist.getHead();
        modlist = modlist.getTail();
        if (!imported.equals(moduleDecl)) {
          ClassName importedclass = (ClassName)factoryNameForModule.get(imported);
          importedFactories = `concClassName(importedclass,importedFactories*);
        }
      }
      GomClassList allOperatorClasses = `concGomClass();
      GomClassList allSortClasses = `concGomClass();
      modlist = environment().getModuleDependency(moduleDecl);
      while(!modlist.isEmpty()) {
        ModuleDecl imported = modlist.getHead();
        modlist = modlist.getTail();
        SortList moduleSorts = getSortForModule(imported,sortList);
        SortList sortconsum = moduleSorts;
        while(!sortconsum.isEmpty()) {
          Sort sort = sortconsum.getHead();
          sortconsum = sortconsum.getTail();
          %match(Sort sort) {
            Sort[decl=sortDecl] -> {
              GomClass sortClass = (GomClass) sortGomClassForSortDecl.get(`sortDecl);
              allSortClasses = `concGomClass(sortClass,allSortClasses*);
            }
          }
        }
        %match(SortList moduleSorts) {
          concSort(_*,Sort[operators=concOperator(_*,opDecl,_*)],_*) -> {
            GomClass opClass = (GomClass) classForOperatorDecl.get(`opDecl);
            allOperatorClasses = `concGomClass(opClass,allOperatorClasses*);
          }
        }
      }

      ClassName factoryClassName = (ClassName)factoryNameForModule.get(moduleDecl);
      ClassName abstractTypeClassName = (ClassName)abstractTypeNameForModule.get(moduleDecl);
      GomClass factoryClass = `FactoryClass(factoryClassName,importedFactories,allSortClasses,allOperatorClasses);
      classList = `concGomClass(factoryClass,classList*);

      // late creation of the visitors, since it has to know all operators
      ClassName visitorName = (ClassName) visitorNameForModule.get(moduleDecl);
      GomClass visitorclass = `VisitorClass(visitorName,allSortClasses,allOperatorClasses);
      classList = `concGomClass(visitorclass,classList*);

      /* create a Fwd class */
      ClassName fwdName = `ClassName(packagePrefix(moduleDecl),moduleName+"Fwd");
      GomClass fwdclass = `FwdClass(fwdName,visitorName,abstractTypeClassName,allSortClasses,allOperatorClasses);
      classList = `concGomClass(fwdclass,classList*);

      /* create a VoidFwd class */
      ClassName voidfwdName = `ClassName(packagePrefix(moduleDecl),moduleName+"FwdVoid");
      GomClass voidfwdclass = `VoidFwdClass(voidfwdName,visitorName,abstractTypeClassName,allSortClasses,allOperatorClasses);
      classList = `concGomClass(voidfwdclass,classList*);

      /* create a VisitableFwd class */
      ClassName visitablefwdName = `ClassName(packagePrefix(moduleDecl),moduleName+"VisitableFwd");
      GomClass visitablefwdclass = `VisitableFwdClass(visitablefwdName,fwdclass);
      classList = `concGomClass(visitablefwdclass,classList*);

      /* create a TomMapping */
      ClassName tomMappingName = `ClassName(packagePrefix(moduleDecl),moduleName);
      GomClass tommappingclass = `TomMapping(tomMappingName,allSortClasses,allOperatorClasses);
      classList = `concGomClass(tommappingclass,classList*);

    }

    return classList;
  }

  private ClassNameList sortClassNames(SortList sortList) {
    ClassNameList classNames = `concClassName();
    %match(SortList sortList) {
      concSort(_*,
          Sort[decl=SortDecl[name=sortname,moduleDecl=moduledecl]]
          ,_*) -> {
        classNames = `concClassName(ClassName(packagePrefix(moduledecl)+".types",sortname),classNames*);
      }
    }
    return classNames;
  }

  private Collection getModuleDeclSet(SortList sortList) {
    class CollectModuleDecls extends GomVisitableFwd {
      Collection bag;
      CollectModuleDecls(Collection bag) {
        super(`Identity());
        this.bag = bag;
      }
      public ModuleDecl visit_ModuleDecl(ModuleDecl arg) {
        bag.add(arg);
        return arg;
      }
    }
    Collection res = new HashSet();
    try {
      VisitableVisitor getModule = new CollectModuleDecls(res);
      MuTraveler.init(`BottomUp(getModule)).visit(sortList);
    } catch (VisitFailure e) {
      throw new GomRuntimeException("Failed to get the set of module names");
    }
    return res;
  }

  /*
   * Get all sort definitions for a given module
   */
  Map mapModuleToSort = new HashMap();
  private SortList getSortForModule(ModuleDecl module, SortList sortList) {
    if (mapModuleToSort.containsKey(module)) {
      return (SortList)mapModuleToSort.get(module);
    } else {
      // Build the sort list for this module
      SortList sorts = `concSort();
      %match(SortList sortList) {
        concSort(_*,s@Sort[decl=SortDecl[moduleDecl=mod]],_*) -> {
          if (`mod.equals(module)) {
            sorts = `concSort(s,sorts*);
          }
        }
      }
      mapModuleToSort.put(module,sorts);
      return sorts;
    }
  }

  private String packagePrefix(ModuleDecl moduleDecl) {
    String pkgPrefix = "";
    %match(ModuleDecl moduleDecl) {
      ModuleDecl[moduleName=GomModuleName[name=name],pkg=pkgopt] -> {
        if(!`pkgopt.equals("")) {
          pkgPrefix = `pkgopt + "." + `name;
        } else {
          pkgPrefix = `name;
        }
      }
    }
    return pkgPrefix.toLowerCase();
  }

  private SlotFieldList slotFieldListFromSet(Set slotFieldSet) {
    Iterator it = slotFieldSet.iterator();
    SlotFieldList list = `concSlotField();
    while(it.hasNext()) {
      SlotField slot = (SlotField) it.next();
      list = `concSlotField(list*,slot);
    }
    return list;
  }

  private HookList makeHooksFromHookDecls(HookDeclList declList) {
    HookList list = `concHook();
    %match(HookDeclList declList) {
      concHookDecl(_*,
          hook,
          _*) -> {
        SlotFieldList newArgs = null;
        Hook newHook = null;
        %match(HookDecl `hook) {
          MakeHookDecl[slotargs=slotArgs,code=hookCode] -> {
            newArgs = makeSlotFieldListFromSlotList(`slotArgs);
            newHook = `MakeHook(newArgs,hookCode);
          }
          MakeBeforeHookDecl[slotargs=slotArgs,code=hookCode] -> {
            newArgs = makeSlotFieldListFromSlotList(`slotArgs);
            newHook = `MakeBeforeHook(newArgs,hookCode);
          }
        }
        if (newHook == null || newArgs == null) {
          throw new GomRuntimeException("Hook declaration "+`hook+" not processed");
        }
        list = `concHook(list*,newHook);
      }
    }
    return list;
  }
  private SlotFieldList makeSlotFieldListFromSlotList(SlotList args) {
    SlotFieldList newArgs = `concSlotField();
    while(!args.isEmpty()) {
      Slot arg = args.getHead();
      args = args.getTail();
      %match(Slot arg) {
        Slot[name=slotName,sort=sortDecl] -> {
          ClassName slotClassName = (ClassName) sortClassNameForSortDecl.get(`sortDecl);
          newArgs = `concSlotField(newArgs*,SlotField(slotName,slotClassName));
        }

      }
    }
    return newArgs;
  }
}
