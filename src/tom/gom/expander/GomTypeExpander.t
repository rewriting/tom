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
    while(!consum.isEmpty()) {
      GomModule module = consum.getHead();
      consum = consum.getTail();

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
    while(!consum.isEmpty()) {
      GomModule module = consum.getHead();
      consum = consum.getTail();

      // iterate on the productions
      %match(GomModule module) {
        GomModule(_,concSection(_*,
              Public(concGrammar(_*,Grammar(concProduction(_*,prod@Production[],_*)),_*)),
              _*)) -> {
          // we may want to pass modulename to help resolve ambiguities with modules
         getOperatorDecl(`prod,sortDeclList,operatorsForSort);

        }
      }

      /*
       * now that we have the definitions of all operators, we can attache them
       * the hooks
       */
      %match(GomModule module) {
        GomModule(_,concSection(_*,
              Public(concGrammar(_*,Grammar(concProduction(_*,hook@Hook[],_*)),_*)),
              _*)) -> {
          // we may want to pass modulename to help resolve ambiguities with modules
         attachHook(`hook,operatorsForSort);
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
      sortList = `concSort(Sort(decl,opdecl),sortList*);
    }
    return sortList;
  }

  private void attachHook(Production prod,
      Map operatorsForSort) {
    /* Find the operator corresponding to the hook, and attach its hook */
    %match(Production prod) {
      Hook[name=hookName] -> {

        Iterator it = operatorsForSort.keySet().iterator();
        while(it.hasNext()) {
          SortDecl decl = (SortDecl) it.next();
          OperatorDeclList opdecl = (OperatorDeclList) operatorsForSort.get(decl);
          %match(OperatorDeclList opdecl) {
            concOperator(L1*,operator@OperatorDecl[name=opName],L2*) -> {
              if (`opName.equals(`hookName)) {
                OperatorDecl newOp = typeOperatorHook(`operator,prod);
                OperatorDeclList newList = `concOperator(L1*,newOp,L2*);
                operatorsForSort.put(decl,newList);
                return; // our job is finished
              }
            }
          }
        }
        getLogger().log(Level.SEVERE, GomMessage.orphanedHook.getMessage(), new Object[]{prod.getName()});
        return;
      }
    }
  }

  private OperatorDecl typeOperatorHook(OperatorDecl operator, Production prod) {
    OperatorDecl newOperator = operator;
    %match(Production prod) {
      Hook(hname,hkind,hargs,hcode) -> {
        %match(OperatorDecl operator) {
          OperatorDecl(oname,osort,oprod,ohooks) -> {
            SlotList typedArgs = typedArguments(`hargs,`hkind,`oprod, `osort);
            if (typedArgs == null) {
              String hookName = `hname;
              getLogger().log(Level.SEVERE, GomMessage.discardedHook.getMessage(),
                  new Object[]{hookName});
              return operator;
            }
            HookDecl newHook = null;
            %match(Hookkind `hkind) {
              (KindMakeHook| KindMakeinsertHook)[] -> {
                newHook = `MakeHookDecl(typedArgs,hcode);
              }
              KindMakeBeforeHook() -> {
                newHook = `MakeBeforeHookDecl(typedArgs,hcode);
              }
            }
            newOperator = `OperatorDecl(oname,osort,oprod,concHookDecl(newHook,ohooks*));
          }
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
      concArg(Arg[name=argName],ta*),concSlot(Slot[sort=slotSort],ts*) -> {
        SlotList tail = recArgSlots(`ta,`ts);
        return `concSlot(Slot(argName,slotSort),tail*);
      }
    }
    throw new GomRuntimeException("GomTypeExpander:recArgSlots failed "+args+" "+slots);
  }
  private SlotList typedArguments(ArgList args, Hookkind kind,
                                  TypedProduction tprod, SortDecl sort) {
    %match(Hookkind kind) {
      (KindMakeHook|KindMakeBeforeHook)[] -> {
        // the TypedProduction has to be Slots
        %match(TypedProduction tprod) {
          Slots(slotList) -> {
            if (args.getLength() != `slotList.getLength()) { // tests the arguments number
              SlotList slist = `slotList;
              getLogger().log(Level.SEVERE, GomMessage.mismatchedMakeArguments.getMessage(),
                  new Object[]{args,slist });
              return null;
            }
            return recArgSlots(args,`slotList);
          }
          _ -> {
            getLogger().log(Level.SEVERE, GomMessage.unsupportedHookAlgebraic.getMessage(),
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
                getLogger().log(Level.SEVERE, GomMessage.badMakeInsertArguments.getMessage(),
                    new Object[]{new Integer(args.getLength())});
                return null;
              }
            }
          }
          _ -> {
            getLogger().log(Level.SEVERE, GomMessage.unsupportedHookVariadic.getMessage(),
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
    if (environment().isBuiltin(typename)) {
      return environment().builtinSort(typename);
    }
    %match(SortDeclList sortDeclList) {
      concSortDecl(_*,sortdecl@SortDecl[name=name],_*) -> {
        if (typename.equals(`name)) {
          return `sortdecl;
        }
      }
    }
    throw new GomRuntimeException(
        "GomTypeExpander::declFromTypename: type name not found");
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
    // XXX: TODO whine about malformed production
    System.out.println("XXX: malformed production");
    return `concSlot();
  }

  /*
   * Get all sort declarations in a module
   */
  private Collection getSortDeclarations(GomModule module) {
    Collection result = new HashSet();
    %match(GomModule module) {
      GomModule(modulename,concSection(_*,
            Public(concGrammar(_*,Sorts(concGomType(_*,GomType(typeName),_*)),_*)),
            _*)) -> {
        result.add(`SortDecl(typeName,ModuleDecl(modulename,packagePath)));
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
      GomModule(modulename,concSection(_*,
            Public(concGrammar(_*,Grammar(concProduction(_*,Production(_,_,GomType(typeName)),_*)),_*)),
            _*)) -> {
        result.add(`SortDecl(typeName,ModuleDecl(modulename,packagePath)));
      }
    }
    return result;
  }

  // get directly imported modules. Skip builtins
  private Collection getImportedModules(GomModule module) {
    Set imports = new HashSet();
    %match(GomModule module) {
      GomModule(moduleName,sectionList) -> {
        imports.add(`moduleName);
        %match(SectionList sectionList) {
          concSection(_*,Imports(concImportedModule(_*,Import(modname@GomModuleName(name)),_*)),_*) -> {
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
      concGomModule(_*,module@GomModule[moduleName=name],_*) -> {
        if (`name.equals(modname)) {
          return `module;
        }
      }
    }
    throw new GomRuntimeException("Module "+ modname +" not present");
  }

  private Collection getTransitiveClosureImports(GomModule module, GomModuleList moduleList) {
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
      concGomModule(_*,module@GomModule[moduleName=name],_*) -> {
        Collection imports = getTransitiveClosureImports(`module,moduleList);
        environment().addModuleDependency(`ModuleDecl(name,packagePath),namesToModuleDecl(imports));
      }
    }
  }

  private ModuleDeclList namesToModuleDecl(Collection names) {
    ModuleDeclList res = `concModuleDecl();
    Iterator it = names.iterator();
    while(it.hasNext()) {
      GomModuleName name = (GomModuleName) it.next();
      res = `concModuleDecl(ModuleDecl(name,packagePath),res*);
    }
    return res;
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

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
