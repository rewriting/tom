/*
 *
 * GOM
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

  %include { ../adt/gom/Gom.tom}

  private GomStreamManager streamManager;
  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  public TypeExpander(GomStreamManager streamManager) {
    this.streamManager = streamManager;
  }
  /**
    * We try here to get full sort definitions for each constructs
    * Once the structure is correctly build, we can attach the hooks
    */
  public ModuleList expand(GomModuleList moduleList) {

    /* put a map giving all imported modules for each module in the path */
    buildDependencyMap(moduleList);

    /* collect all sort declarations */
    SortDeclList sortDeclList = `ConcSortDecl();
    /* The sorts declared in each module */
    Map sortsForModule = new HashMap();
    GomModuleList consum = moduleList;
    while(!consum.isEmptyConcGomModule()) {
      GomModule module = consum.getHeadConcGomModule();
      consum = consum.getTailConcGomModule();

      Collection decls = getSortDeclarations(module);
      Collection implicitdecls = getSortDeclarationInCodomain(module);

      /* Check that there are no implicit sort declarations
       * Also, check that declared sorts have at least an operator
       */
      if(!decls.containsAll(implicitdecls)) {
        // whine about non declared sorts
        Collection undeclaredSorts = new HashSet();
        undeclaredSorts.addAll(implicitdecls);
        undeclaredSorts.removeAll(decls);
        getLogger().log(Level.WARNING, GomMessage.undeclaredSorts.getMessage(),
            new Object[]{showSortList(undeclaredSorts)});
      }
      if(!implicitdecls.containsAll(decls)) {
        // whine about sorts without operators: this is a real error
        Collection emptySorts = new HashSet();
        emptySorts.addAll(decls);
        emptySorts.removeAll(implicitdecls);
        getLogger().log(Level.SEVERE, GomMessage.emptySorts.getMessage(),
            new Object[]{showSortList(emptySorts)});
        return `ConcModule();
      }
      Iterator it = implicitdecls.iterator();
      while(it.hasNext()) {
        SortDecl decl = (SortDecl)it.next();
        sortDeclList = `ConcSortDecl(decl,sortDeclList*);
      }
      /* Fills sortsForModule */
      it = decls.iterator();
      SortDeclList declaredSorts = `ConcSortDecl();
      while(it.hasNext()) {
        SortDecl decl = (SortDecl)it.next();
        declaredSorts = `ConcSortDecl(decl,declaredSorts*);
      }
      GomModuleName moduleName = module.getModuleName();
      ModuleDecl mdecl = `ModuleDecl(moduleName,streamManager.getPackagePath(moduleName.getName()));
      sortsForModule.put(mdecl,declaredSorts);
    }

    /* now get all operators for each sort */
    Map operatorsForSort = new HashMap();
    Map hooksForSort = new HashMap();
    consum = moduleList;
    while(!consum.isEmptyConcGomModule()) {
      GomModule module = consum.getHeadConcGomModule();
      consum = consum.getTailConcGomModule();

      // iterate through the productions
      %match(GomModule module) {
        GomModule(_,ConcSection(_*,
              Public(ConcGrammar(_*,Grammar(ConcProduction(_*,prod@Production[],_*)),_*)),
              _*)) -> {
          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl(`prod,sortDeclList,operatorsForSort);

        }
      }
      %match(GomModule module) {
        GomModule(_,ConcSection(_*,
              Public(ConcGrammar(_*,Grammar(ConcProduction(_*,
                SortType[ProductionList=ConcProduction(_*,
                prod@Production[],_*)],
              _*)),_*)),
              _*)) -> {
          // we may want to pass moduleName to help resolve ambiguities with modules
          getOperatorDecl(`prod,sortDeclList,operatorsForSort);

        }
      }
    }

    /*
     * build the module list using the map
     * since we already checked that the declared and used sorts do match, we
     * can use the map alone
     */
    ModuleList resultModuleList = `ConcModule();
    Iterator it = sortsForModule.entrySet().iterator();
    while(it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      ModuleDecl mdecl = (ModuleDecl) entry.getKey();
      SortDeclList sdeclList = (SortDeclList) entry.getValue();
      SortList sortList = `ConcSort();
      %match(sdeclList) {
        ConcSortDecl(_*,sdecl,_*) -> {
          OperatorDeclList opdecl = (OperatorDeclList)
            operatorsForSort.get(`sdecl);
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
   * Get an OperatorDecl from a Production, using the list of sort declarations
   * XXX: There is huge room for efficiency improvement, as we could use a map
   * sortName -> sortDeclList instead of a simple list
   */
  private OperatorDecl getOperatorDecl(Production prod,
      SortDeclList sortDeclList,
      Map operatorsForSort) {

    %match(Production prod) {
      Production(name,domain,GomType(codomain),_) -> {
        SortDecl codomainSort = declFromTypename(`codomain,sortDeclList);
        TypedProduction domainSorts = typedProduction(`domain,sortDeclList);
        OperatorDecl decl = `OperatorDecl(name,codomainSort, domainSorts);
        if (operatorsForSort.containsKey(codomainSort)) {
          OperatorDeclList list = (OperatorDeclList) operatorsForSort.get(codomainSort);
          operatorsForSort.put(codomainSort,`ConcOperator(decl,list*));
        } else {
          operatorsForSort.put(codomainSort,`ConcOperator(decl));
        }
        return decl;
      }
    }
    throw new GomRuntimeException(
        "TypeExpander::getOperatorDecl: wrong Production?");
  }

  private SortDecl declFromTypename(String typename,
                                    SortDeclList sortDeclList) {
    if (environment().isBuiltinSort(typename)) {
      return environment().builtinSort(typename);
    }
    %match(SortDeclList sortDeclList) {
      ConcSortDecl(_*,sortdecl@SortDecl[Name=name],_*) -> {
        if (typename.equals(`name)) {
          return `sortdecl;
        }
      }
    }
    getLogger().log(Level.SEVERE, GomMessage.unknownSort.getMessage(),
        new Object[]{typename});
    /* If the sort is not known, assume it is a builtin */
    return `BuiltinSortDecl(typename);
  }

  private TypedProduction typedProduction(FieldList domain, SortDeclList sortDeclList) {
    %match(FieldList domain) {
      ConcField(StarredField(GomType(typename))) -> {
        return `Variadic(declFromTypename(typename,sortDeclList));
      }
      ConcField(fieldList*) -> {
        return `Slots(typedSlotList(fieldList,sortDeclList));
      }
    }
    // the error message could be more refined
    throw new GomRuntimeException("TypeExpander::typedProduction: illformed Production");
  }

  private SlotList typedSlotList(FieldList fields, SortDeclList sortDeclList) {
    %match(FieldList fields) {
      ConcField() -> {
        return `ConcSlot();
      }
      ConcField(NamedField(name,GomType(typename)),tail*) -> {
        SlotList newtail = typedSlotList(`tail,sortDeclList);
        return `ConcSlot(Slot(name,declFromTypename(typename,sortDeclList)),newtail*);
      }
    }
    getLogger().log(Level.SEVERE, GomMessage.malformedProduction.getMessage(),
        new Object[]{fields.toString()});
    return `ConcSlot();
  }

  /*
   * Get all sort declarations in a module
   */
  private Collection getSortDeclarations(GomModule module) {
    Collection result = new HashSet();
    %match(GomModule module) {
      GomModule(moduleName,ConcSection(_*,
            Public(ConcGrammar(_*,Sorts(ConcGomType(_*,GomType(typeName),_*)),_*)),
            _*)) -> {
        result.add(`SortDecl(typeName,ModuleDecl(moduleName,streamManager.getPackagePath(moduleName.getName()))));
      }
    }
    %match(GomModule module) {
      GomModule(moduleName,ConcSection(_*,
            Public(ConcGrammar(_*,Grammar(ConcProduction(_*,
                SortType[Type=GomType(typeName)],
            _*)),_*)),
            _*)) -> {
        result.add(`SortDecl(typeName,ModuleDecl(moduleName,streamManager.getPackagePath(moduleName.getName()))));
      }
    }
    return result;
  }

  /*
   * Get all sort uses in a module (as codomain of an operator)
   */
  private Collection getSortDeclarationInCodomain(GomModule module) {
    Collection result = new HashSet();
    %match(GomModule module) {
      GomModule(
          moduleName,
          ConcSection(_*,
            Public(
              ConcGrammar(_*,
                Grammar(
                  ConcProduction(_*,
                    Production(_,_,GomType(typeName),_option),
                    _*)),
                _*)),
            _*)) -> {
        result.add(`SortDecl(typeName,ModuleDecl(moduleName,streamManager.getPackagePath(moduleName.getName()))));
      }
    }
    %match(GomModule module) {
      GomModule(
          moduleName,
          ConcSection(_*,
            Public(
              ConcGrammar(_*,
                Grammar(ConcProduction(_*,
                  SortType[ProductionList=ConcProduction(_*,
                    Production(_,_,GomType(typeName),_option),
                    _*)],_*)),
                _*)),
            _*)) -> {
        result.add(`SortDecl(typeName,ModuleDecl(moduleName,streamManager.getPackagePath(moduleName.getName()))));
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
  private Collection getImportedModules(GomModule module) {
    Set imports = new HashSet();
    %match(GomModule module) {
      GomModule(moduleName,sectionList) -> {
        imports.add(`moduleName);
        %match(SectionList sectionList) {
          ConcSection(_*,
              Imports(ConcImportedModule(_*,
                  Import(modname@GomModuleName(name)),
                  _*)),
              _*) -> {
            if (!environment().isBuiltin(`name)) {
              imports.add(`modname);
            }
          }
        }
      }
    }
    return imports;
  }

  private GomModule getModule(GomModuleName modname, GomModuleList list) {
    %match(GomModuleList list) {
      ConcGomModule(_*,module@GomModule[ModuleName=name],_*) -> {
        if (`name.equals(modname)) {
          return `module;
        }
      }
    }
    throw new GomRuntimeException("Module "+ modname +" not present");
  }

  private Collection getTransitiveClosureImports(GomModule module,
      GomModuleList moduleList) {
    Set imported = new HashSet();
    imported.addAll(getImportedModules(module));

    Set newSet = new HashSet();
    while(!newSet.equals(imported)) {
      newSet.addAll(imported);
      imported.addAll(newSet);
      Iterator it = imported.iterator();
      while(it.hasNext()) {
        GomModuleName modname = (GomModuleName) it.next();
        newSet.addAll(getImportedModules(getModule(modname,moduleList)));
      }
    }
    return newSet;
  }

  private void buildDependencyMap(GomModuleList moduleList) {
    %match(GomModuleList moduleList) {
      ConcGomModule(_*,module@GomModule[ModuleName=moduleName],_*) -> {
        ModuleDeclList importsModuleDeclList = `ConcModuleDecl();
        Iterator it = getTransitiveClosureImports(`module,moduleList).iterator();
        while(it.hasNext()) {
          GomModuleName importedModuleName = (GomModuleName) it.next();


          importsModuleDeclList = 
            `ConcModuleDecl(ModuleDecl(importedModuleName,streamManager.getPackagePath(importedModuleName.getName())),
                importsModuleDeclList*);
        }
        environment().addModuleDependency(
            `ModuleDecl(moduleName,streamManager.getPackagePath(moduleName.getName())),importsModuleDeclList);
      }
    }
  }

  private boolean checkSortValidity(Sort sort) {
    boolean valid = true;
    // check if the same slot name is used with different types
    Map mapNameType = new HashMap();
    %match(Sort sort) {
      Sort[OperatorDecls=ConcOperator(_*,
          OperatorDecl[Prod=Slots[Slots=ConcSlot(_*,
            Slot[Name=slotName,Sort=slotSort],
            _*)]],
          _*)] -> {
        if(!mapNameType.containsKey(`slotName)) {
          mapNameType.put(`slotName,`slotSort);
        } else {
          SortDecl prevSort = (SortDecl) mapNameType.get(`slotName);
          if (!prevSort.equals(`slotSort)) {
            getLogger().log(Level.SEVERE,
                GomMessage.slotIncompatibleTypes.getMessage(),
                new Object[]{`(slotName),prevSort.getName(),
                             `(slotSort).getName()});
            valid = false;
          }
        }
      }
    }
    return valid;
  }

  private String showSortList(Collection decls) {
    String sorts = "";
    Iterator it = decls.iterator();
    if(it.hasNext()) {
      SortDecl decl = (SortDecl)it.next();
      sorts += decl.getName();
    }
    while(it.hasNext()) {
      SortDecl decl = (SortDecl)it.next();
      sorts += ", "+decl.getName();
    }
    return sorts;
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
