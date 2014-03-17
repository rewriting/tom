/*
 * Gom
 *
 * Copyright (c) 2006-2014, Universite de Lorraine, Inria
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

  %include { ../adt/objects/Objects.tom}

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
    GomClassList classList = `ConcGomClass();

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
    %match(moduleList) {
      ConcModule(_*,Module[MDecl=moduleDecl],_*) -> {
        String moduleName = `moduleDecl.getModuleName().getName();

        /* create an AbstractType class */
        ClassName abstractTypeName = `ClassName(
            packagePrefix(moduleDecl),
            moduleName+"AbstractType");

        ClassName tomMappingName = `ClassName(
            packagePrefix(moduleDecl),
            moduleName);
        tomMappingNameForModule.put(`moduleDecl,tomMappingName);

        abstractTypeNameForModule.put(`moduleDecl,abstractTypeName);
      }
    }

    /* For each sort, create a sort implementation, and operator implementations
       (we don't need to do that per module, since each operator and sort knows
       to which module it belongs) */
    %match(moduleList) {
      ConcModule(_*,
          Module[Sorts=ConcSort(_*,
            Sort[Decl=decl@SortDecl[Name=sortname,ModuleDecl=moduleDecl]],
            _*)],
          _*) -> {
        // get the class name for the sort
        ClassName sortClassName = `ClassName(packagePrefix(moduleDecl)+".types",sortname);
        sortClassNameForSortDecl.put(`decl,sortClassName);
      }
    }
    %match(moduleList) {
      ConcModule(_*,
          Module[Sorts=ConcSort(_*,
            Sort[Decl=sortDecl@SortDecl[ModuleDecl=moduleDecl],
            OperatorDecls=oplist],
            _*)],
          _*) -> {
        // get the class name for the sort
        ClassName sortClassName = sortClassNameForSortDecl.get(`sortDecl);
        ClassName abstracttypeName = abstractTypeNameForModule.get(`moduleDecl);
        ClassName mappingName = tomMappingNameForModule.get(`moduleDecl);
        // create operator classes. Also, store a list of all operators for the sort class
        // use a Set to collect slots and avoid duplicates
        Set<SlotField> allSortSlots = new HashSet<SlotField>();
        ClassNameList allOperators = `ConcClassName();
        ClassNameList allVariadicOperators = `ConcClassName();
        GomClassList allOperatorClasses = `ConcGomClass();
        %match(OperatorDeclList `oplist) {
          ConcOperator(_*,
              opdecl@OperatorDecl[Name=opname,
              Sort=SortDecl[Name=sortName],
              Prod=typedproduction,
              Option=option],
              _*) -> {
            String comments = getCommentsFromOption(`option);
            String sortNamePackage = `sortName.toLowerCase();
            ClassName operatorClassName =
              `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,opname);
            SlotFieldList slots = `ConcSlotField();
            ClassName variadicOpClassName = null;
            ClassName empty = null;
            %match(TypedProduction typedproduction) {
              Variadic[Sort=domain] -> {
                ClassName clsName = sortClassNameForSortDecl.get(`domain);
                SlotField slotHead = `SlotField("Head"+opname,clsName);
                SlotField slotTail = `SlotField("Tail"+opname,sortClassName);
                allSortSlots.add(`slotHead);
                allSortSlots.add(`slotTail);
                slots = `ConcSlotField(slotHead,slotTail);
                // as the operator is variadic, add a Cons and an Empty
                variadicOpClassName =
                  `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,opname);
                allVariadicOperators = `ConcClassName(variadicOpClassName,allVariadicOperators*);
                empty =
                  `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,"Empty"+opname);
                operatorClassName =
                  `ClassName(packagePrefix(moduleDecl)+".types."+sortNamePackage,"Cons"+opname);

                allOperators = `ConcClassName(empty,allOperators*);
              }
              Slots(ConcSlot(_*,Slot[Name=slotname,Sort=domain],_*)) -> {
                ClassName clsName = sortClassNameForSortDecl.get(`domain);
                SlotField slotfield = `SlotField(slotname,clsName);
                allSortSlots.add(slotfield);
                slots = `ConcSlotField(slots*,slotfield);
              }
            }
            GomClass operatorClass;
            allOperators = `ConcClassName(operatorClassName,allOperators*);
            if(variadicOpClassName != null) {
              /* We just processed a variadic operator */
              GomClass consClass = `OperatorClass(operatorClassName,
                  abstracttypeName,
                  variadicOpClassName,
                  mappingName,
                  sortClassName,
                  slots,
                  ConcHook(),
                  comments);

              GomClass emptyClass = `OperatorClass(empty,
                                                   abstracttypeName,
                                                   variadicOpClassName,
                                                   mappingName,
                                                   sortClassName,
                                                   ConcSlotField(),
                                                   ConcHook(),
                                                   comments);

              operatorClass = `VariadicOperatorClass(variadicOpClassName,
                                                     abstracttypeName,
                                                     mappingName,
                                                     sortClassName,
                                                     emptyClass,
                                                     consClass,
                                                     ConcHook(),
                                                     comments);
            } else {
              operatorClass = `OperatorClass(operatorClassName,
                                             abstracttypeName,
                                             sortClassName,
                                             mappingName,
                                             sortClassName,
                                             slots,
                                             ConcHook(),
                                             comments);
            }
            classForOperatorDecl.put(`opdecl,operatorClass);
            classList = `ConcGomClass(operatorClass,classList*);
            allOperatorClasses = `ConcGomClass(operatorClass,allOperatorClasses*);
          }
        }
        // create the sort class and add it to the list
        GomClass sortClass = `SortClass(sortClassName,
                                        abstracttypeName,
                                        mappingName,
                                        allOperators,
                                        allVariadicOperators,
                                        allOperatorClasses,
                                        slotFieldListFromSet(allSortSlots),
                                        ConcHook());
        sortGomClassForSortDecl.put(`sortDecl,sortClass);
        classList = `ConcGomClass(sortClass,classList*);
      }
    }

    %match(moduleList) {
      ConcModule(_*,Module[MDecl=moduleDecl],_*) -> {
        String moduleName = `moduleDecl.getModuleName().getName();

        GomClassList allOperatorClasses = `ConcGomClass();
        GomClassList allSortClasses = `ConcGomClass();
        /* TODO improve this part : just for test */
        ModuleDeclList modlist = getGomEnvironment().getModuleDependency(`moduleDecl);
        while(!modlist.isEmptyConcModuleDecl()) {
          ModuleDecl imported = modlist.getHeadConcModuleDecl();
          modlist = modlist.getTailConcModuleDecl();
          SortList moduleSorts = getSortsForModule(imported,moduleList);
          SortList sortconsum = moduleSorts;
          while(!sortconsum.isEmptyConcSort()) {
            Sort sort = sortconsum.getHeadConcSort();
            sortconsum = sortconsum.getTailConcSort();
            %match(sort) {
              Sort[Decl=sortDecl] -> {
                GomClass sortClass = sortGomClassForSortDecl.get(`sortDecl);
                allSortClasses = `ConcGomClass(sortClass,allSortClasses*);
              }
            }
          }
          %match(moduleSorts) {
            ConcSort(_*,Sort[OperatorDecls=ConcOperator(_*,opDecl,_*)],_*) -> {
              GomClass opClass = classForOperatorDecl.get(`opDecl);
              allOperatorClasses = `ConcGomClass(opClass,allOperatorClasses*);
              %match(GomClass opClass) {
                VariadicOperatorClass[Empty=emptyClass,Cons=consClass] -> {
                  allOperatorClasses = `ConcGomClass(emptyClass,consClass,allOperatorClasses*);
                }
              }
            }
          }
        }

        ClassName abstractTypeClassName = abstractTypeNameForModule.get(`moduleDecl);

        /* create a TomMapping */
        ClassName tomMappingName = tomMappingNameForModule.get(`moduleDecl);
        GomClass tommappingclass = `TomMapping(tomMappingName,
                                               allSortClasses,
                                               allOperatorClasses);
        classList = `ConcGomClass(tommappingclass,classList*);

        /* create the abstractType */
        ClassNameList classSortList = sortClassNames(`moduleList);
        ClassName abstractTypeName = abstractTypeNameForModule.get(`moduleDecl);
        GomClass abstracttype =
          `AbstractTypeClass(abstractTypeName,
                             tomMappingName,
                             classSortList,
                             ConcHook());
        classList = `ConcGomClass(abstracttype,classList*);

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
    ClassNameList classNames = `ConcClassName();
    %match(moduleList) {
      ConcModule(_*,Module[
          Sorts=ConcSort(_*,
                  Sort[Decl=SortDecl[Name=sortname,ModuleDecl=moduledecl]],
                _*)
          ],_*) -> {
        classNames = `ConcClassName(ClassName(packagePrefix(moduledecl)+".types",sortname),classNames*);
      }
    }
    return classNames;
  }

  /*
   * Get all sort definitions for a given module
   */
  private SortList getSortsForModule(ModuleDecl module, ModuleList moduleList) {
    %match(module, moduleList) {
      decl@ModuleDecl[],
      ConcModule(_*,Module[MDecl=decl,Sorts=sorts],_*) -> {
        return `sorts;
      }
    }
    throw new RuntimeException("Module " + module + " not found");
  }

  public static String packagePrefix(ModuleDecl moduleDecl) {
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

  private SlotFieldList slotFieldListFromSet(Set<SlotField> slotFieldSet) {
    SlotFieldList list = `ConcSlotField();
    for (SlotField slot : slotFieldSet ) {
      list = `ConcSlotField(list*,slot);
    }
    return list;
  }

  private ClassNameList allClassForImports(
      Map classMap,
      ModuleDecl moduleDecl) {
    ClassNameList importedList = `ConcClassName();
    ModuleDeclList importedModulelist = getGomEnvironment().getModuleDependency(moduleDecl);
    while(!importedModulelist.isEmptyConcModuleDecl()) {
      ModuleDecl imported = importedModulelist.getHeadConcModuleDecl();
      importedModulelist = importedModulelist.getTailConcModuleDecl();
      if (!imported.equals(moduleDecl)) {
        ClassName importedclass = (ClassName)classMap.get(imported);
        importedList = `ConcClassName(importedclass,importedList*);
      }
    }
    return importedList;
  }

  private static String getCommentsFromOption(Option option) {
    String comments = "";
    %match(option) {
      Details[Comments=cts] -> {
        comments = `cts;
      }
      OptionList(_*,Details[Comments=cts],_*) -> {
        comments += `cts;
      }
    }
    return comments;
  }
}
