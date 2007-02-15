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

public class GomTypeExpander {

  %include { ../adt/gom/Gom.tom}

  private String packagePath;
  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  public GomTypeExpander(String packagePath) {
    this.packagePath = packagePath;
  }
  /**
    * We try here to get full sort definitions for each constructs
    */
  public SortList expand(GomModuleList moduleList) {

    /* put a map giving all imported modules for each module in the path */
    buildDependencyMap(moduleList);

    /* collect all sort declarations, both explicit and implicit */
    SortDeclList sortDeclList = `concSortDecl();
    GomModuleList consum = moduleList;
    while(!consum.isEmptyconcGomModule()) {
      GomModule module = consum.getHeadconcGomModule();
      consum = consum.getTailconcGomModule();

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
        return `concSort();
      }
      Iterator it = implicitdecls.iterator();
      while(it.hasNext()) {
        SortDecl decl = (SortDecl)it.next();
        sortDeclList = `concSortDecl(decl,sortDeclList*);
      }
    }

    /* now get all operators for each sort */
    Map operatorsForSort = new HashMap();
    consum = moduleList;
    while(!consum.isEmptyconcGomModule()) {
      GomModule module = consum.getHeadconcGomModule();
      consum = consum.getTailconcGomModule();

      // iterate through the productions
      %match(GomModule module) {
        GomModule(_,concSection(_*,
              Public(concGrammar(_*,Grammar(concProduction(_*,prod@Production[],_*)),_*)),
              _*)) -> {
          // we may want to pass modulename to help resolve ambiguities with modules
         getOperatorDecl(`prod,sortDeclList,operatorsForSort);

        }
      }

      /*
       * now that we have the definitions of all operators, we can attach them
       * the hooks
       */
      %match(GomModule module) {
        GomModule(_,concSection(_*,
              Public(concGrammar(_*,Grammar(concProduction(_*,prod,_*)),_*)),
              _*)) -> {
          %match(Production prod){
            hook@Hook[NameType=KindOperator()] -> {
              // we may want to pass modulename to help resolve ambiguities with modules
              attachHookOperator(`hook,operatorsForSort);
            }
          }
        }
      }
    }
    // build the sort list using the map
    // since we already checked that the declared and used sorts do match, we
    // can use the map alone
    SortList sortList = `concSort();
    Iterator it = operatorsForSort.keySet().iterator();
    while(it.hasNext()) {
      SortDecl decl = (SortDecl) it.next();
      OperatorDeclList opdecl = (OperatorDeclList) operatorsForSort.get(decl);
      Sort fullSort = `Sort(decl,opdecl);
      if(checkSortValidity(fullSort)) {
        sortList = `concSort(fullSort,sortList*);
      }
    }
    return sortList;
  }

  private void attachHookOperator(Production prod,Map operatorsForSort) {
    /* Find the operator corresponding to the hook, and attach its hook */
    %match(Production prod) {
      Hook[NameType=KindOperator(),Name=hookName] -> {
        Iterator it = operatorsForSort.keySet().iterator();
        while(it.hasNext()) {
          SortDecl decl = (SortDecl) it.next();
          OperatorDeclList opdecl = (OperatorDeclList) operatorsForSort.get(decl);
          %match(OperatorDeclList opdecl) {
            concOperator(L1*,operator@OperatorDecl[Name=opName],L2*) -> {
              if (`opName.equals(`hookName)) {
                OperatorDecl newOp = typeOperatorHook(`operator,prod);
                OperatorDeclList newList = `concOperator(L1*,newOp,L2*);
                operatorsForSort.put(decl,newList);
                return; // our job is finished
              }
            }
          }
        }
      }
    }
    getLogger().log(Level.SEVERE, GomMessage.orphanedHook.getMessage(),
        new Object[]{prod.getName()});
    return;
  }


  private OperatorDecl typeOperatorHook(OperatorDecl operator, Production prod) {
    HookDecl newHook = null;
    OperatorDecl newOperator = operator;
    %match(Production prod) {
      Hook(hnametype,hname,hkind,hargs,hcode) -> {
        %match(OperatorDecl operator) {
          OperatorDecl(oname,osort,oprod,ohooks) -> {
            %match(HookKind hkind) {
              KindBlockHook[] -> {
                newHook = `BlockHookDecl(hcode);
                newOperator = `OperatorDecl(oname,osort,oprod,concHookDecl(newHook,ohooks*));
              }

              KindInterfaceHook[] -> {
                newHook = `InterfaceHookDecl(hcode);
                newOperator = `OperatorDecl(oname,osort,oprod,concHookDecl(newHook,ohooks*));
              }

              KindImportHook[] -> {
                newHook = `ImportHookDecl(hcode);
                newOperator = `OperatorDecl(oname,osort,oprod,concHookDecl(newHook,ohooks*));
              }

              (KindMakeHook| KindMakeinsertHook)[] -> {
                SlotList typedArgs = typedArguments(`hargs,`hkind,`oprod, `osort);
                if (typedArgs == null) {
                  String hookName = `hname;
                  getLogger().log(Level.SEVERE, GomMessage.discardedHook.getMessage(),
                      new Object[]{hookName});
                  return operator;
                }
                newHook = `MakeHookDecl(typedArgs,hcode);
                newOperator = `OperatorDecl(oname,osort,oprod,concHookDecl(newHook,ohooks*));
              }
            }
          }
        }
        if (newHook == null) {
          throw new GomRuntimeException(
              "GomTypeExpander:typeOperatorHook unknown HookKind: "+`hkind);
        }
      }
    }
    return newOperator;
  }

  private SlotList recArgSlots(ArgList args, SlotList slots) {
    %match(ArgList args, SlotList slots) {
      concArg(),concSlot() -> {
        return `concSlot();
      }
      concArg(Arg[Name=argName],ta*),concSlot(Slot[Sort=slotSort],ts*) -> {
        SlotList tail = recArgSlots(`ta,`ts);
        return `concSlot(Slot(argName,slotSort),tail*);
      }
    }
    throw new GomRuntimeException("GomTypeExpander:recArgSlots failed "+args+" "+slots);
  }
  private SlotList typedArguments(ArgList args, HookKind kind,
      TypedProduction tprod, SortDecl sort) {
    %match(HookKind kind) {
      KindMakeHook[] -> {
        // the TypedProduction has to be Slots
        %match(TypedProduction tprod) {
          Slots(slotList) -> {
            if (args.length() != `slotList.length()) { // tests the arguments number
              SlotList slist = `slotList;
              getLogger().log(Level.SEVERE,
                  GomMessage.mismatchedMakeArguments.getMessage(),
                  new Object[]{args,slist });
              return null;
            }
            return recArgSlots(args,`slotList);
          }
          _ -> {
            getLogger().log(Level.SEVERE,
                GomMessage.unsupportedHookAlgebraic.getMessage(),
                new Object[]{kind});
            return null;
          }
        }
      }
      KindMakeinsertHook[] -> {
        // the TypedProduction has to be Variadic
        %match(TypedProduction tprod) {
          Variadic(sortDecl) -> {
            // for a make_insert hook, there are two arguments: head, tail
            %match(ArgList args) {
              concArg(Arg(head),Arg(tail)) -> {
                return `concSlot(Slot(head,sortDecl),Slot(tail,sort));
              }
              _ -> {
                getLogger().log(Level.SEVERE,
                    GomMessage.badMakeInsertArguments.getMessage(),
                    new Object[]{new Integer(args.length())});
                return null;
              }
            }
          }
          _ -> {
            getLogger().log(Level.SEVERE,
                GomMessage.unsupportedHookVariadic.getMessage(),
                new Object[]{kind});
            return null;
          }
        }
      }
    }
    throw new GomRuntimeException("Hook kind \""+kind+"\" not supported");
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
      Production(name,domain,GomType(codomain)) -> {
        SortDecl codomainSort = declFromTypename(`codomain,sortDeclList);
        TypedProduction domainSorts = typedProduction(`domain,sortDeclList);
        OperatorDecl decl = `OperatorDecl(name,codomainSort, domainSorts, concHookDecl());
        if (operatorsForSort.containsKey(codomainSort)) {
          OperatorDeclList list = (OperatorDeclList) operatorsForSort.get(codomainSort);
          operatorsForSort.put(codomainSort,`concOperator(decl,list*));
        } else {
          operatorsForSort.put(codomainSort,`concOperator(decl));
        }
        return decl;
      }
    }
    throw new GomRuntimeException(
        "GomTypeExpander::getOperatorDecl: wrong Production?");
  }
  private SortDecl declFromTypename(String typename, SortDeclList sortDeclList) {
    if (environment().isBuiltinSort(typename)) {
      return environment().builtinSort(typename);
    }
    %match(SortDeclList sortDeclList) {
      concSortDecl(_*,sortdecl@SortDecl[Name=name],_*) -> {
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
  TypedProduction typedProduction(FieldList domain, SortDeclList sortDeclList) {
    %match(FieldList domain) {
      concField(StarredField(GomType(typename))) -> {
        return `Variadic(declFromTypename(typename,sortDeclList));
      }
      concField(fieldList*) -> {
        return `Slots(typedSlotList(fieldList,sortDeclList));
      }
    }
    // the error message could be more refined
    throw new GomRuntimeException("GomTypeExpander::typedProduction: illformed Production");
  }
  SlotList typedSlotList(FieldList fields, SortDeclList sortDeclList) {
    %match(FieldList fields) {
      concField() -> {
        return `concSlot();
      }
      concField(NamedField(name,GomType(typename)),tail*) -> {
        SlotList newtail = typedSlotList(`tail,sortDeclList);
        return `concSlot(Slot(name,declFromTypename(typename,sortDeclList)),newtail*);
      }
    }
    getLogger().log(Level.SEVERE, GomMessage.malformedProduction.getMessage(),
        new Object[]{fields.toString()});
    return `concSlot();
  }

  /*
   * Get all sort declarations in a module
   */
  private Collection getSortDeclarations(GomModule module) {
    Collection result = new HashSet();
    HookDeclList moduleHooks = getModuleHooks(module);
    %match(GomModule module) {
      GomModule(modulename,concSection(_*,
            Public(concGrammar(_*,Sorts(concGomType(_*,GomType(typeName),_*)),_*)),
            _*)) -> {
        HookDeclList sortHooks = getSortHooks(module,`typeName);
        result.add(`SortDecl(typeName,ModuleDecl(modulename,packagePath,moduleHooks),sortHooks));
      }
    }
    return result;
  }

  /*
   * Get all sort uses in a module (as codomain of an operator)
   */
  private Collection getSortDeclarationInCodomain(GomModule module) {
    Collection result = new HashSet();
    HookDeclList moduleHooks = getModuleHooks(module);
    %match(GomModule module) {
      GomModule(modulename,concSection(_*,Public(concGrammar(_*,Grammar(concProduction(_*,Production(_,_,GomType(typeName)),_*)),_*)),_*)) -> {
        HookDeclList sortHooks = getSortHooks(module,`typeName);
        result.add(`SortDecl(typeName,ModuleDecl(modulename,packagePath,moduleHooks),sortHooks));
      }
    }
    return result;
  }

  /*
     get all hooks for a module
   */
  private HookDeclList getModuleHooks(GomModule module) {
    HookDeclList hooks = `concHookDecl();
    %match(GomModule module) {
      GomModule(modulename,concSection(_*,Public(concGrammar(_*,Grammar(productions),_*)),_*)) -> {
        %match(ProductionList productions){
          concProduction(_*,hook@Hook[NameType=KindModule(),HookType=hkind,Code=hcode],_*) -> {
            if(`hook.getName().equals(`modulename.getName())){
              HookDecl newHook = null;
              %match(HookKind `hkind) {
                KindBlockHook[] -> {
                  newHook = `BlockHookDecl(hcode);
                }
                KindInterfaceHook[] -> {
                  newHook = `InterfaceHookDecl(hcode);
                }
                KindImportHook[] -> {
                  newHook = `ImportHookDecl(hcode);
                }
              }
              if (newHook == null) {
                throw new GomRuntimeException(
                    "GomTypeExpander:typeModuleHook unknown HookKind: "+`hkind);
              }
              hooks = `concHookDecl(hooks*,newHook);
            }
            else{
              getLogger().log(Level.SEVERE,"Hooks on module are authorised only on the current module");
            }
          }
        }
      }
    }
    return hooks;
  }
  /*
     get all hooks for a sort
   */
  private HookDeclList getSortHooks(GomModule module,String sortName) {
    HookDeclList hooks = `concHookDecl();
    %match(GomModule module) {
      GomModule(modulename,concSection(_*,Public(concGrammar(_*,Grammar(productions),_*)),_*)) -> {
        %match(ProductionList productions){
          concProduction(_*,hook@Hook[NameType=KindSort(),HookType=hkind,Code=hcode],_*) -> {
            if(`hook.getName().equals(`sortName)){
              HookDecl newHook = null;
              %match(HookKind `hkind) {
                KindBlockHook[] -> {
                  newHook = `BlockHookDecl(hcode);
                }
                KindInterfaceHook[] -> {
                  newHook = `InterfaceHookDecl(hcode);
                }
                KindImportHook[] -> {
                  newHook = `ImportHookDecl(hcode);
                }
              }
              if (newHook == null) {
                throw new GomRuntimeException(
                    "GomTypeExpander:typeModuleHook unknown HookKind: "+`hkind);
              }

              hooks = `concHookDecl(hooks*,newHook);
            }
          }
        }
      }
    }
    return hooks;
  }

  // get directly imported modules. Skip builtins
  private Collection getImportedModules(GomModule module) {
    Set imports = new HashSet();
    %match(GomModule module) {
      GomModule(moduleName,sectionList) -> {
        imports.add(`moduleName);
        %match(SectionList sectionList) {
          concSection(_*,
              Imports(concImportedModule(_*,
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
      concGomModule(_*,module@GomModule[ModuleName=name],_*) -> {
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
      concGomModule(_*,module@GomModule[ModuleName=name],_*) -> {
        ModuleDeclList importsModuleDeclList = `concModuleDecl();
        Iterator it = getTransitiveClosureImports(`module,moduleList).iterator();
        while(it.hasNext()) {
          GomModuleName importedModuleName = (GomModuleName) it.next();
          importsModuleDeclList = 
            `concModuleDecl(ModuleDecl(importedModuleName,packagePath,concHookDecl()),
                importsModuleDeclList*);
        }
        environment().addModuleDependency(
            `ModuleDecl(name,packagePath,concHookDecl()),importsModuleDeclList);
      }
    }
  }

  private boolean checkSortValidity(Sort sort) {
    boolean valid = true;
    // check if the same slot name is used with different types
    Map mapNameType = new HashMap();
    %match(Sort sort) {
      Sort[Operators=concOperator(_*,
          OperatorDecl[Prod=Slots[Slots=concSlot(_*,
            Slot[Name=slotName,Sort=slotSort],
            _*)]],
          _*)] -> {
        if(!mapNameType.containsKey(`slotName)) {
          mapNameType.put(`slotName,`slotSort);
        } else {
          SortDecl prevSort = (SortDecl) mapNameType.get(`slotName);
          if (!prevSort.equals(`slotSort)) {
            getLogger().log(Level.SEVERE, GomMessage.slotIncompatibleTypes.getMessage(),
                new Object[]{`(slotName),prevSort.getName(),`(slotSort).getName()});
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
