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
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;

import tom.library.sl.*;

public class HookCompiler {

  %include { ../adt/objects/Objects.tom}
  %include { ../../library/mapping/java/sl.tom }

  %typeterm HookCompiler { implement { tom.gom.compiler.HookCompiler } }

  public static final String UNEXPECTED_STRATEGY_FAILURE = "Unexpected strategy failure!";

  // myadd-begin
  private GomEnvironment gomEnvironment;

  public HookCompiler(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  /* private GomEnvironment environment() {
  //  return GomEnvironment.getInstance();
  }*/

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }
  // myadd-end

  // Map sortClassNameForSortDecl = environment().builtinSortClassMap();
  //mymodif
  // private static Map sortClassNameForSortDecl;
  private Map sortClassNameForSortDecl;
  HookCompiler(Map sortClassNameForSortDecl) {
    this.sortClassNameForSortDecl = sortClassNameForSortDecl;
  }

  // myadd-begin
  public Map getSortClassNameForSortDecl() {
    return sortClassNameForSortDecl;
  }

  public void setSortClassNameForSortDecl(Map mySortClassNameForSortDecl) {
    this.sortClassNameForSortDecl = mySortClassNameForSortDecl;
  }
  // myadd-end

  /**
    * Process the hooks, and attach them to the correct classes.
    */
  public GomClassList compile(
      HookDeclList declList,
      GomClassList classes,
      Map declToClassName) {
    /* for each hook, find the class, and attach the hook */
    %match(declList) {
      ConcHookDecl(_*,hook,_*) -> {
        Decl decl = `hook.getPointcut();
        %match(decl) {
          CutModule(mdecl) -> {
            ClassName clsName = (ClassName) declToClassName.get(`mdecl);
            try {
              classes = `TopDown(AttachModuleHook(clsName,hook,this)).visit(classes);
                //`TopDown(AttachModuleHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException(HookCompiler.UNEXPECTED_STRATEGY_FAILURE);
            }
          }
          CutSort(sdecl) -> {
            ClassName clsName = (ClassName) declToClassName.get(`sdecl);
            try {
              classes = `TopDown(AttachSortHook(clsName,hook,this)).visit(classes);
                //`TopDown(AttachSortHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException(HookCompiler.UNEXPECTED_STRATEGY_FAILURE);
            }
          }
          CutOperator(odecl) -> {
            ClassName clsName = (ClassName) declToClassName.get(`odecl);
            try {
              classes = `TopDown(AttachOperatorHook(clsName,hook,this)).visit(classes);
                //`TopDown(AttachOperatorHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException(HookCompiler.UNEXPECTED_STRATEGY_FAILURE);
            }
          }
          CutFutureOperator(odecl,consornil) -> {
            ClassName clsName = (ClassName) declToClassName.get(`odecl);
            String prefix = "";
            %match(consornil) {
              FutureNil() -> { prefix = "Empty"; }
              FutureCons() -> { prefix = "Cons"; }
            }
            clsName = clsName.setName(prefix + clsName.getName());
            try {
              classes = `TopDown(AttachOperatorHook(clsName,hook,this)).visit(classes);
                //`TopDown(AttachOperatorHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException(HookCompiler.UNEXPECTED_STRATEGY_FAILURE);
            }
          }
        }
      }
    }
    return classes;
  }

  //%strategy AttachModuleHook(cName:ClassName,hook:HookDecl)
  %strategy AttachModuleHook(cName:ClassName,hook:HookDecl,hc:HookCompiler)
    extends Identity() {
      visit GomClass {
        obj@AbstractTypeClass[ClassName=className,Hooks=oldHooks] -> {
          if (`className == `cName) {
            return
              `obj.setHooks(`ConcHook(hc.makeHooksFromHookDecl(hook),oldHooks*));
              //`obj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
          }
        }
      }
    }

  //%strategy AttachSortHook(cName:ClassName,hook:HookDecl)
  %strategy AttachSortHook(cName:ClassName,hook:HookDecl,hc:HookCompiler)
    extends Identity() {
      visit GomClass {
        obj@SortClass[ClassName=className,Hooks=oldHooks] -> {
          if (`className == `cName) {
            return
              `obj.setHooks(`ConcHook(hc.makeHooksFromHookDecl(hook),oldHooks*));
              //`obj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
          }
        }
      }
    }

  %strategy AttachOperatorHook(cName:ClassName,hook:HookDecl,hc:HookCompiler)
    extends Identity() {
      visit GomClass {
        obj@VariadicOperatorClass[ClassName=className,Hooks=oldHooks,
          Empty=emptyClass,Cons=consClass] -> {
            if (`className == `cName) {
              /* We may want to attach the hook to the cons or empty */
              if (hook.isMakeHookDecl()) {
                if (hook.getSlotArgs() != `ConcSlot()) {
                  HookList oldConsHooks = `consClass.getHooks();
                  GomClass newCons =
                    `consClass.setHooks(
                        `ConcHook(hc.makeHooksFromHookDecl(hook),oldConsHooks*));
                        //`ConcHook(makeHooksFromHookDecl(hook),oldConsHooks*));
                  return `obj.setCons(newCons);
                } else if (hook.getSlotArgs() == `ConcSlot()) {
                  HookList oldEmptyHooks = `emptyClass.getHooks();
                  GomClass newEmpty =
                    `emptyClass.setHooks(
                        `ConcHook(hc.makeHooksFromHookDecl(hook),oldEmptyHooks*));
                        //`ConcHook(makeHooksFromHookDecl(hook),oldEmptyHooks*));
                  return `obj.setEmpty(newEmpty);
                }
              } else if (hook.isImportHookDecl()) {
                /* We will want to attach the hook directly to the 3 classes */
                /* in case we use these imports for the corresponding Make hooks */
                HookList oldConsHooks = `consClass.getHooks();
                GomClass newCons =
                  `consClass.setHooks(
                      `ConcHook(hc.makeHooksFromHookDecl(hook),oldConsHooks*));
                      //`ConcHook(makeHooksFromHookDecl(hook),oldConsHooks*));
                HookList oldEmptyHooks = `emptyClass.getHooks();
                GomClass newEmpty =
                  `emptyClass.setHooks(
                      `ConcHook(hc.makeHooksFromHookDecl(hook),oldEmptyHooks*));
                      //`ConcHook(makeHooksFromHookDecl(hook),oldEmptyHooks*));
                GomClass newobj = `obj.setEmpty(newEmpty);
                newobj = newobj.setCons(newCons);
                return `newobj.setHooks(`ConcHook(hc.makeHooksFromHookDecl(hook),oldHooks*));
                //return `newobj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
              } else {
                return
                  `obj.setHooks(`ConcHook(hc.makeHooksFromHookDecl(hook),oldHooks*));
                  //`obj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
              }
            }
          }
        obj@OperatorClass[ClassName=className,Hooks=oldHooks] -> {
          if (`className == `cName) {
            return
              `obj.setHooks(`ConcHook(hc.makeHooksFromHookDecl(hook),oldHooks*));
              //`obj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
          }
        }
      }
    }

  // private static Hook makeHooksFromHookDecl(HookDecl hookDecl) {
  private Hook makeHooksFromHookDecl(HookDecl hookDecl) {
    %match(hookDecl) {
      MakeHookDecl[SlotArgs=slotArgs,Code=hookCode,HasTomCode=HasTomCode] -> {
        SlotFieldList newArgs = makeSlotFieldListFromSlotList(`slotArgs);
        return `MakeHook(newArgs,hookCode,HasTomCode);
      }
      BlockHookDecl[Code=hookCode,HasTomCode=HasTomCode] -> {
        return `BlockHook(hookCode,HasTomCode);
      }
      InterfaceHookDecl[Code=hookCode] -> {
        return `InterfaceHook(hookCode);
      }
      ImportHookDecl[Code=hookCode] -> {
        return `ImportHook(hookCode);
      }
      MappingHookDecl[Code=hookCode] -> {
        return `MappingHook(hookCode);
      }
    }
    throw new GomRuntimeException(
        "Hook declaration " + `hookDecl + " not processed");
  }

  // private static SlotFieldList makeSlotFieldListFromSlotList(SlotList args) {
  private SlotFieldList makeSlotFieldListFromSlotList(SlotList args) {
    SlotFieldList newArgs = `ConcSlotField();
    while(!args.isEmptyConcSlot()) {
      Slot arg = args.getHeadConcSlot();
      args = args.getTailConcSlot();
      %match(Slot arg) {
        Slot[Name=slotName,Sort=sortDecl] -> {
          ClassName slotClassName = (ClassName)
            // sortClassNameForSortDecl.get(`sortDecl);
            getSortClassNameForSortDecl().get(`sortDecl);
          newArgs = `ConcSlotField(newArgs*,SlotField(slotName,slotClassName));
        }
      }
    }
    return newArgs;
  }
}
