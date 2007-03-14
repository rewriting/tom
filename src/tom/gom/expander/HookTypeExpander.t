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

public class HookTypeExpander {

  %include { ../adt/gom/Gom.tom}

  private ModuleList moduleList;
  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  public HookTypeExpander(ModuleList moduleList) {
    this.moduleList = moduleList;
  }
  /**
    * Get the correct types for hooks, and attach them the correct Decl
    */
  public HookDeclList expand(GomModuleList gomModuleList) {
    HookDeclList hookList = `concHookDecl();
    /* For each hook in the GomModuleList */
    %match(gomModuleList) {
      concGomModule(_*,
          GomModule[ModuleName=GomModuleName[Name=moduleName],
                    SectionList=concSection(_*,
                      Public(concGrammar(_*,
                          Grammar(concProduction(_*,hook@Hook[],_*)),
                          _*)),
                      _*)],
          _*) -> {
        %match(hook) {
          /* Hooks attached to a module */
          Hook[NameType=KindModule(),Name=mname] -> {
            if(`mname.equals(`moduleName)) {
              ModuleDecl mdecl = getModuleDecl(`mname,moduleList);
              HookDecl newDecl = makeHookDecl(`hook,`CutModule(mdecl));
              hookList = `concHookDecl(newDecl,hookList*);
            } else {
              getLogger().log(Level.SEVERE,
                  "Hooks on module are authorised only on the current module");
            }
          }
          Hook[NameType=KindSort(),Name=sname] -> {
            SortDecl sdecl = getSortDecl(`sname,`moduleName,moduleList);
            HookDecl newDecl = makeHookDecl(`hook,`CutSort(sdecl));
            hookList = `concHookDecl(newDecl,hookList*);
          }
          Hook[NameType=KindOperator(),Name=oname] -> {
            OperatorDecl odecl = getOperatorDecl(`oname,`moduleName,moduleList);
            HookDecl newDecl = makeHookDecl(`hook,`CutOperator(odecl));
            hookList = `concHookDecl(newDecl,hookList*);
          }
        }
      }
    }
    return hookList;
  }

  HookDecl makeHookDecl(Production hook, Decl mdecl) {
    %match(hook) {
      Hook[HookType=hkind,
           Name=hName,
           Args=hookArgs,
           Code=hcode] -> {
        HookDecl newHook = null;
        %match(HookKind `hkind) {
          HookKind("block") -> {
            newHook = `BlockHookDecl(mdecl,hcode);
          }
          HookKind("interface") -> {
            newHook = `InterfaceHookDecl(mdecl,hcode);
          }
          HookKind("import") -> {
            newHook = `ImportHookDecl(mdecl,hcode);
          }
          HookKind("make")|HookKind("make_insert") -> {
            SlotList typedArgs = typeArguments(`hookArgs,`hkind,`mdecl);
            if (typedArgs == null) {
              getLogger().log(Level.SEVERE,
                  GomMessage.discardedHook.getMessage(),
                  new Object[]{ `(hName) });
              return `MakeHookDecl(mdecl,concSlot(),hcode);
            }
            newHook = `MakeHookDecl(mdecl,typedArgs,hcode);
          }
        }
        if (newHook == null) {
          throw new GomRuntimeException(
              "GomTypeExpander:typeModuleHook unknown HookKind: "+`hkind);
        }
        return newHook;
      }
    }
    throw new GomRuntimeException(
        "HookTypeExpander: this hook is not a hook: "+`hook);
  }

  /**
    * Finds the ModuleDecl corresponding to a module name.
    *
    * @param mname the module name
    * @param moduleList the queried ModuleList
    * @return the ModuleDecl for mname
    */
  ModuleDecl getModuleDecl(String mname, ModuleList moduleList) {
    %match(moduleList) {
      concModule(_*,
        Module[MDecl=mdecl@ModuleDecl[
          ModuleName=GomModuleName[Name=moduleName]]],
        _*) -> {
        if (`moduleName.equals(mname)) {
          return `mdecl;
        }
      }
    }
    throw new GomRuntimeException(
        "HookTypeExpander: Module not found: "+`mname);
  }

  /**
    * Finds the SortDecl corresponding to a sort name.
    *
    * @param sname the sort name
    * @param modName the module name that should contain the sort
    * @param moduleList the queried ModuleList
    * @return the SortDecl for sname
    */
  SortDecl getSortDecl(String sname, String modName, ModuleList moduleList) {
    %match(String modName, moduleList) {
      mname,
      concModule(_*,
        Module[
          MDecl=ModuleDecl[ModuleName=GomModuleName[Name=mname]],
          Sorts=concSort(_*,Sort[Decl=sdecl@SortDecl[Name=sortName]],_*)],
        _*) -> {
        if (`sortName.equals(sname)) {
          return `sdecl;
        }
      }
    }
    throw new GomRuntimeException(
        "HookTypeExpander: Sort not found: "+`sname);
  }

  /**
    * Finds the OperatorDecl corresponding to an operator name.
    *
    * @param oname the sort name
    * @param modName the module name that should contain the operator
    * @param moduleList the queried ModuleList
    * @return the OperatorDecl for sname
    */
  OperatorDecl getOperatorDecl(
      String oname,
      String modName,
      ModuleList moduleList) {
    %match(String modName, moduleList) {
      mname,
      concModule(_*,
        Module[
          MDecl=ModuleDecl[ModuleName=GomModuleName[Name=mname]],
          Sorts=concSort(_*,Sort[
            Operators=concOperator(_*,odecl@OperatorDecl[Name=operName],_*)
            ],_*)],
        _*) -> {
        if (`operName.equals(oname)) {
          return `odecl;
        }
      }
    }
    throw new GomRuntimeException(
        "HookTypeExpander: Operator not found: "+`oname);
  }

  private SlotList typeArguments(
      ArgList args,
      HookKind kind,
      Decl decl) {
    %match(kind) {
      HookKind("make") -> {
        /*
         * The TypedProduction has to be Slots
         * A KindMakeHook is attached to an operator
         */
        %match(decl) {
          CutOperator[ODecl=OperatorDecl[Prod=Slots(slotList)]] -> {
            /* tests the arguments number */
            if (args.length() != `slotList.length()) {
              SlotList slist = `slotList;
              getLogger().log(Level.SEVERE,
                  GomMessage.mismatchedMakeArguments.getMessage(),
                  new Object[]{args,slist });
              return null;
            }
            /* Then check the types */
            return recArgSlots(args,`slotList);
          }
          _ -> {
            getLogger().log(Level.SEVERE,
                GomMessage.unsupportedHookAlgebraic.getMessage(),
                new Object[]{ kind });
            return null;
          }
        }
      }
      HookKind("make_insert") -> {
        /*
         * The TypedProduction has to be Variadic
         * Then we get the codomain from the operatordecl
         */
        %match(decl) {
          CutOperator[
            ODecl=OperatorDecl[Sort=sort,Prod=Variadic(sortDecl)]] -> {
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
                new Object[]{ kind });
            return null;
          }
        }
      }
    }
    throw new GomRuntimeException("Hook kind \""+kind+"\" not supported");
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

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
