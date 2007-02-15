/*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
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

public class Compiler {

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
    Map visitorNameForModule = new HashMap();
    Map visitableForwardNameForModule = new HashMap();
    Map tomMappingNameForModule = new HashMap();
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

      ClassName visitorName = `ClassName(packagePrefix(moduleDecl),moduleName+"Visitor");
      visitorNameForModule.put(moduleDecl,visitorName);

      ClassName visitablefwdName = `ClassName(packagePrefix(moduleDecl),moduleName+"BasicStrategy");
      visitableForwardNameForModule.put(moduleDecl,visitablefwdName);

      ClassName tomMappingName = `ClassName(packagePrefix(moduleDecl),moduleName);
      tomMappingNameForModule.put(moduleDecl,tomMappingName);

      abstractTypeNameForModule.put(moduleDecl,abstractTypeName);

    }

    /* For each sort, create a sort implementation, and operator implementations
       (we don't need to do that per module, since each operator and sort knows
       to which module it belongs) */
    SortList consum = sortList;
    while(!consum.isEmptyconcSort()) {
      Sort sort = consum.getHeadconcSort();
      consum=consum.getTailconcSort();
      // get the class name for the sort
      %match(Sort sort) {
        Sort[Decl=decl@SortDecl[Name=sortname,ModuleDecl=moduleDecl]] -> {
          ClassName sortClassName = `ClassName(packagePrefix(moduleDecl)+".types",sortname);
          sortClassNameForSortDecl.put(`decl,sortClassName);
        }
      }
    }
    consum = sortList;
    while(!consum.isEmptyconcSort()) {
      Sort sort = consum.getHeadconcSort();
      consum=consum.getTailconcSort();
      // get the class name for the sort
      %match(Sort sort) {
        Sort[Decl=sortDecl@SortDecl[ModuleDecl=moduleDecl,Hooks=sorthooks],Operators=oplist] -> {
          ClassName sortClassName = (ClassName)sortClassNameForSortDecl.get(`sortDecl);
          ClassName abstracttypeName = (ClassName)abstractTypeNameForModule.get(`moduleDecl);
          ClassName visitorName = (ClassName)visitorNameForModule.get(`moduleDecl);
          ClassName visitableforwardName = (ClassName)visitableForwardNameForModule.get(`moduleDecl);
          ClassName mappingName = (ClassName)tomMappingNameForModule.get(`moduleDecl);

          // create operator classes. Also, store a list of all operators for the sort class
          // use a Set to collect slots and avoid duplicates
          Set allSortSlots = new HashSet();
          ClassNameList allOperators = `concClassName();
          ClassNameList allVariadicOperators = `concClassName();
          %match(OperatorDeclList `oplist) {
            concOperator(_*,
                         opdecl@OperatorDecl[Name=opname,
                                             Sort=SortDecl[Name=sortName],
                                             Prod=typedproduction,
                                             Hooks=hookList],
                         _*) -> {
              String sortNamePackage = `sortName.toLowerCase();
              ClassName operatorClassName = `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,opname);
              SlotFieldList slots = `concSlotField();
              ClassName variadicOpClassName = null;
              ClassName empty = null;
              %match(TypedProduction typedproduction) {
                Variadic[Sort=domain] -> {
                  ClassName clsName = (ClassName)sortClassNameForSortDecl.get(`domain);
                  SlotField slotHead = `SlotField("Head"+opname,clsName);
                  SlotField slotTail = `SlotField("Tail"+opname,sortClassName);
                  allSortSlots.add(`slotHead);
                  allSortSlots.add(`slotTail);
                  slots = `concSlotField(slotHead,slotTail);
                  // as the operator is variadic, add a Cons and an Empty
                  variadicOpClassName = `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,opname);
                  allVariadicOperators = `concClassName(variadicOpClassName,allVariadicOperators*);
                  empty = `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,"Empty"+opname);
                  operatorClassName = `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,"Cons"+opname);

                  allOperators = `concClassName(empty,allOperators*);
                }
                Slots(concSlot(_*,Slot[Name=slotname,Sort=domain],_*)) -> {
                  ClassName clsName = (ClassName)sortClassNameForSortDecl.get(`domain);
                  SlotField slotfield = `SlotField(slotname,clsName);
                  allSortSlots.add(slotfield);
                  slots = `concSlotField(slots*,slotfield);
                }
              }
              GomClass operatorClass;
              allOperators = `concClassName(operatorClassName,allOperators*);
              HookList operatorHooks = makeHooksFromHookDecls(`hookList);
              if (variadicOpClassName != null) { // We just processed a variadic operator
                GomClass cons = `OperatorClass(operatorClassName,
                                               abstracttypeName,
                                               variadicOpClassName,
                                               mappingName,
                                               sortClassName,
                                               visitorName, slots,
                                               operatorHooks);
                classList = `concGomClass(cons,classList*);

                GomClass emptyClass = `OperatorClass(empty,
                                                     abstracttypeName,
                                                     variadicOpClassName,
                                                     mappingName,
                                                     sortClassName,
                                                     visitorName,
                                                     concSlotField(),
                                                     concHook());
                classList = `concGomClass(emptyClass,classList*);

                operatorClass = `VariadicOperatorClass(variadicOpClassName ,
                                                       abstracttypeName,
                                                       sortClassName,
                                                       emptyClass,
                                                       cons,
                                                       concHook());
              } else {
                operatorClass = `OperatorClass(operatorClassName,
                                               abstracttypeName,
                                               sortClassName,
                                               mappingName,
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
                                          abstracttypeName,
                                          visitorName,
                                          visitableforwardName,
                                          allOperators,
                                          allVariadicOperators,
                                          slotFieldListFromSet(allSortSlots),
                                          makeHooksFromHookDecls(sorthooks));
          sortGomClassForSortDecl.put(`sortDecl,sortClass);
          classList = `concGomClass(sortClass,classList*);
        }
      }
    }

    it = getModuleDeclSet(sortList).iterator();
    while(it.hasNext()) {
      ModuleDecl moduleDecl = (ModuleDecl) it.next();
      String moduleName = moduleDecl.getModuleName().getName();

      GomClassList allOperatorClasses = `concGomClass();
      GomClassList allSortClasses = `concGomClass();
      /* TODO improve this part : just for test */
      ModuleDecl moduleDeclWithoutHooks = `ModuleDecl(moduleDecl.getModuleName(),moduleDecl.getPkg(),concHookDecl());
      ModuleDeclList modlist = environment().getModuleDependency(moduleDeclWithoutHooks);
      while(!modlist.isEmptyconcModuleDecl()) {
        ModuleDecl imported = modlist.getHeadconcModuleDecl();
        modlist = modlist.getTailconcModuleDecl();
        SortList moduleSorts = getSortForModule(imported,sortList);
        SortList sortconsum = moduleSorts;
        while(!sortconsum.isEmptyconcSort()) {
          Sort sort = sortconsum.getHeadconcSort();
          sortconsum = sortconsum.getTailconcSort();
          %match(Sort sort) {
            Sort[Decl=sortDecl] -> {
              GomClass sortClass = (GomClass) sortGomClassForSortDecl.get(`sortDecl);
              allSortClasses = `concGomClass(sortClass,allSortClasses*);
            }
          }
        }
        %match(SortList moduleSorts) {
          concSort(_*,Sort[Operators=concOperator(_*,opDecl,_*)],_*) -> {
            GomClass opClass = (GomClass) classForOperatorDecl.get(`opDecl);
            allOperatorClasses = `concGomClass(opClass,allOperatorClasses*);
            %match(GomClass opClass) {
              VariadicOperatorClass[Empty=emptyClass,Cons=consClass] -> {
                allOperatorClasses = `concGomClass(emptyClass,consClass,allOperatorClasses*);
      
              }
            }
          }
        }
      }

      ClassName abstractTypeClassName = (ClassName)abstractTypeNameForModule.get(moduleDecl);

      // late creation of the visitors, since it has to know all operators
      ClassName visitorName = (ClassName) visitorNameForModule.get(moduleDecl);
      GomClass visitorclass = `VisitorClass(visitorName,allSortClasses,allOperatorClasses);
      classList = `concGomClass(visitorclass,classList*);

      /* create a Fwd class */
      ClassNameList importedVisitors = allClassForImports(visitorNameForModule,moduleDecl);
      ClassName fwdName = `ClassName(packagePrefix(moduleDecl),moduleName+"Forward");
      ClassNameList importedAbstractType = allClassForImports(abstractTypeNameForModule,moduleDecl);
      GomClass fwdclass = `FwdClass(fwdName,visitorName,importedVisitors,abstractTypeClassName,importedAbstractType,allSortClasses,allOperatorClasses);
      classList = `concGomClass(fwdclass,classList*);

      /* create a VisitableFwd class */
      ClassName visitablefwdName = (ClassName) visitableForwardNameForModule.get(moduleDecl);
      GomClass visitablefwdclass = `VisitableFwdClass(visitablefwdName,fwdclass);
      classList = `concGomClass(visitablefwdclass,classList*);

      /* create the abstractType */
      ClassNameList classSortList = sortClassNames(sortList);
      ClassName abstractTypeName = (ClassName) abstractTypeNameForModule.get(moduleDecl);
      GomClass abstracttype =
        `AbstractTypeClass(abstractTypeName,visitorName,classSortList,makeHooksFromHookDecls(moduleDecl.getHooks()));
      classList = `concGomClass(abstracttype,classList*);

      /* create a TomMapping */
      ClassName tomMappingName = (ClassName) tomMappingNameForModule.get(moduleDecl);
      GomClass tommappingclass = `TomMapping(tomMappingName,visitablefwdName,allSortClasses,allOperatorClasses);
      classList = `concGomClass(tommappingclass,classList*);

    }

    return classList;
  }

  private ClassNameList sortClassNames(SortList sortList) {
    ClassNameList classNames = `concClassName();
    %match(SortList sortList) {
      concSort(_*,
          Sort[Decl=SortDecl[Name=sortname,ModuleDecl=moduledecl]]
          ,_*) -> {
        classNames = `concClassName(ClassName(packagePrefix(moduledecl)+".types",sortname),classNames*);
      }
    }
    return classNames;
  }

  private Collection getModuleDeclSet(SortList sortList) {
    class CollectModuleDecls extends GomBasicStrategy {
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
        concSort(_*,s@Sort[Decl=SortDecl[ModuleDecl=mod]],_*) -> {
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
      ModuleDecl[ModuleName=GomModuleName[Name=name],Pkg=pkgopt] -> {
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
          MakeHookDecl[SlotArgs=slotArgs,Code=hookCode] -> {
            newArgs = makeSlotFieldListFromSlotList(`slotArgs);
            newHook = `MakeHook(newArgs,hookCode);
            if (newArgs == null) {
              throw new GomRuntimeException("Hook declaration "+`hook+" not processed");
            }
          }
          BlockHookDecl[Code=hookCode] -> {
            newHook = `BlockHook(hookCode);
          }
          InterfaceHookDecl[Code=hookCode] -> {
            newHook = `InterfaceHook(hookCode);
          }
          ImportHookDecl[Code=hookCode] -> {
            newHook = `ImportHook(hookCode);
          }

        }
        if (newHook == null) {
          throw new GomRuntimeException("Hook declaration "+`hook+" not processed");
        }
        list = `concHook(list*,newHook);
      }
    }
    return list;
  }

  private SlotFieldList makeSlotFieldListFromSlotList(SlotList args) {
    SlotFieldList newArgs = `concSlotField();
    while(!args.isEmptyconcSlot()) {
      Slot arg = args.getHeadconcSlot();
      args = args.getTailconcSlot();
      %match(Slot arg) {
        Slot[Name=slotName,Sort=sortDecl] -> {
          ClassName slotClassName = (ClassName) sortClassNameForSortDecl.get(`sortDecl);
          newArgs = `concSlotField(newArgs*,SlotField(slotName,slotClassName));
        }
      }
    }
    return newArgs;
  }

  private ClassNameList allClassForImports(Map classMap, ModuleDecl moduleDecl) {
    ClassNameList importedList = `concClassName();
    /* TODO improve this part : just for test */
    ModuleDecl moduleDeclWithoutHooks = `ModuleDecl(moduleDecl.getModuleName(),moduleDecl.getPkg(),concHookDecl());
    ModuleDeclList importedModulelist = environment().getModuleDependency(moduleDeclWithoutHooks);
    while(!importedModulelist.isEmptyconcModuleDecl()) {
      ModuleDecl imported = importedModulelist.getHeadconcModuleDecl();
      importedModulelist = importedModulelist.getTailconcModuleDecl();
      if (!imported.equals(moduleDeclWithoutHooks)) {
        ClassName importedclass = (ClassName)classMap.get(imported);
        importedList = `concClassName(importedclass,importedList*);
      }
    }
    return importedList;
  }
}
