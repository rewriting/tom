/*
 *
 * GOM
 *
 * Copyright (c) 2006-2016, Universite de Lorraine, Inria
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

  %include { ../adt/gom/Gom.tom}

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
    SortDeclList sortDeclList = `ConcSortDecl();
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
        return `ConcModule();
      }
      for (SortDecl decl : implicitdecls) {
        sortDeclList = `ConcSortDecl(decl,sortDeclList*);
      }
      /* Fills sortsForModule */
      SortDeclList declaredSorts = `ConcSortDecl();
      for (SortDecl decl : decls) {
        declaredSorts = `ConcSortDecl(decl,declaredSorts*);
      }
      GomModuleName moduleName = module.getModuleName();
      ModuleDecl mdecl = `ModuleDecl(moduleName,getStreamManager().getPackagePath(moduleName.getName()));
      sortsForModule.put(mdecl,declaredSorts);
    }

    /* now get all operators for each sort */
    Map<SortDecl,OperatorDeclList> operatorsForSort =
      new HashMap<SortDecl,OperatorDeclList>();
    for (GomModule module : moduleList) {
      // iterate through the productions
      %match(module) {
        GomModule(_,ConcSection(_*,
              Public(ConcProduction(_*,
                SortType[AlternativeList=ConcAlternative(_*,
                alt@Alternative[],_*)],
              _*)),
              _*)) -> {
          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl(`alt,sortDeclList,operatorsForSort);

        }
      }
    }

    /*
     * build the module list using the map
     * since we already checked that the declared and used sorts do match, we
     * can use the map alone
     */
    ModuleList resultModuleList = `ConcModule();
    for (Map.Entry<ModuleDecl,SortDeclList> entry : sortsForModule.entrySet()) {
      ModuleDecl mdecl = entry.getKey();
      SortDeclList sdeclList = entry.getValue();
      SortList sortList = `ConcSort();
      %match(sdeclList) {
        ConcSortDecl(_*,sdecl,_*) -> {
          OperatorDeclList opdecl = operatorsForSort.get(`sdecl);
          Sort fullSort = `Sort(sdecl,opdecl);
          if(checkSortValidity(fullSort)) {
            sortList = `ConcSort(fullSort,sortList*);
          }
        }
      }
      resultModuleList = `ConcModule(
          Module(mdecl,sortList),
          resultModuleList*);
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

    %match(alt) {
      Alternative(name,domain,GomType(_,codomain),options) -> {
        SortDecl codomainSort = declFromTypename(`codomain,sortDeclList);
        TypedProduction domainSorts = typedProduction(`domain,sortDeclList);

        OperatorDecl decl = `OperatorDecl(name,codomainSort,domainSorts,options);
        if (operatorsForSort.containsKey(codomainSort)) {
          OperatorDeclList list = operatorsForSort.get(codomainSort);
          operatorsForSort.put(codomainSort,`ConcOperator(decl,list*));
        } else {
          operatorsForSort.put(codomainSort,`ConcOperator(decl));
        }
        return decl;
      }

    }
    throw new GomRuntimeException(
        "TypeExpander::getOperatorDecl: wrong Alternative?");
  }

  private SortDecl declFromTypename(String typename,
                                    SortDeclList sortDeclList) {
    if (getGomEnvironment().isBuiltinSort(typename)) {
      return getGomEnvironment().builtinSort(typename);
    }
    %match(sortDeclList) {
      ConcSortDecl(_*,sortdecl@SortDecl[Name=name],_*) -> {
        if (typename.equals(`name)) {
          return `sortdecl;
        }
      }
    }

    GomMessage.error(getLogger(),null,0,
        GomMessage.unknownSort,
        new Object[]{typename});
    /* If the sort is not known, assume it is a builtin */
    return `BuiltinSortDecl(typename);
  }

  private TypedProduction typedProduction(FieldList domain, SortDeclList sortDeclList) {
    %match(domain) {
      ConcField(StarredField(GomType(_,typename),_)) -> {
        return `Variadic(declFromTypename(typename,sortDeclList));
      }
      ConcField(fieldList*) -> {
        return `Slots(typedSlotList(fieldList,sortDeclList));
      }
    }
    throw new GomRuntimeException("TypeExpander::typedProduction: illformed Alternative");
  }

  private SlotList typedSlotList(FieldList fields, SortDeclList sortDeclList) {
    %match(fields) {
      ConcField() -> {
        return `ConcSlot();
      }
      ConcField(NamedField(name,GomType(_,typename),_),tail*) -> {
        SlotList newtail = typedSlotList(`tail,sortDeclList);
        return `ConcSlot(Slot(name,declFromTypename(typename,sortDeclList)),newtail*);
      }
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.malformedProduction,
        new Object[]{fields.toString()});
    return `ConcSlot();
  }

  /*
   * Get all sort declarations in a module
   */
  private Collection<SortDecl> getSortDeclarations(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    %match(module) {
      GomModule(moduleName,ConcSection(_*,
            Public(ConcProduction(_*,
                SortType[Type=GomType(_,typeName)],
            _*)),
            _*)) -> {
        if (getGomEnvironment().isBuiltinSort(`typeName)) {
          GomMessage.error(getLogger(),null,0,
              GomMessage.operatorOnBuiltin,
              new Object[]{(`typeName)});
          result.add(getGomEnvironment().builtinSort(`typeName));
        } else {
          result.add(`SortDecl(typeName,ModuleDecl(moduleName,getStreamManager().getPackagePath(moduleName.getName()))));
        }
      }
    }
    return result;
  }

  /*
   * Get all sort uses in a module (as codomain of an operator)
   */
  private Collection<SortDecl> getSortDeclarationInCodomain(GomModule module) {
    Collection<SortDecl> result = new HashSet<SortDecl>();
    %match(module) {
      GomModule(
          moduleName,
          ConcSection(_*,
            Public(
              ConcProduction(_*,
                SortType[AlternativeList=ConcAlternative(_*,
                  Alternative(_,_,GomType(_,typeName),_option),
                  _*)],_*)
              ),
            _*)) -> {
        if (getGomEnvironment().isBuiltinSort(`typeName)) {
          result.add(getGomEnvironment().builtinSort(`typeName));
        } else {
          result.add(`SortDecl(typeName,ModuleDecl(moduleName,getStreamManager().getPackagePath(moduleName.getName()))));
        }
      }
    }
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
    %match(module) {
      GomModule(moduleName,sectionList) -> {
        imports.add(`moduleName);
        %match(sectionList) {
          ConcSection(_*,
              Imports(ConcImportedModule(_*,
                  modname@GomModuleName(name),
                  _*)),
              _*) -> {
            if (!getGomEnvironment().isBuiltin(`name)) {
              imports.add(`modname);
            }
          }
        }
      }
    }
    return imports;
  }

  private GomModule getModule(GomModuleName modname, GomModuleList list) {
    %match(list) {
      ConcGomModule(_*,module@GomModule[ModuleName=name],_*) -> {
        if (`name.equals(modname)) {
          return `module;
        }
      }
    }
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
    %match(moduleList) {
      ConcGomModule(_*,module@GomModule[ModuleName=moduleName],_*) -> {
        ModuleDeclList importsModuleDeclList = `ConcModuleDecl();
        for (GomModuleName importedModuleName :
            getTransitiveClosureImports(`module,moduleList)) {
          importsModuleDeclList =
            `ConcModuleDecl(
                ModuleDecl(importedModuleName,
                  getStreamManager().getPackagePath(
                    importedModuleName.getName())),
                importsModuleDeclList*);
        }
        getGomEnvironment().addModuleDependency(
            `ModuleDecl(moduleName,
              getStreamManager().getPackagePath(moduleName.getName())),
            importsModuleDeclList);
      }
    }
  }

  private boolean checkSortValidity(Sort sort) {
    boolean valid = true;
    // check if the same slot name is used with different types
    Map<String,SortDecl> mapNameType = new HashMap<String,SortDecl>();
    %match(sort) {
      Sort[Decl=(SortDecl|BuiltinSortDecl)[Name=sortName],
           OperatorDecls=ConcOperator(_*,
          OperatorDecl[Prod=Slots[Slots=ConcSlot(_*,
            Slot[Name=slotName,Sort=slotSort],
            _*)]],
          _*)] -> {
        if (!mapNameType.containsKey(`slotName)) {
          mapNameType.put(`slotName,`slotSort);
        } else {
          SortDecl prevSort = mapNameType.get(`slotName);
          if (!prevSort.equals(`slotSort)) {
            GomMessage.error(getLogger(),null,0,
                GomMessage.slotIncompatibleTypes,
                new Object[]{`sortName,`slotName,prevSort.getName(),
                             `(slotSort).getName()});
            valid = false;
          }
        }
      }
    }
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
