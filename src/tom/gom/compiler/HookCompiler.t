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

import tom.library.sl.*;

public class HookCompiler {

  %include { ../adt/objects/Objects.tom}
  %include { ../../library/mapping/java/sl.tom }

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  private static Map sortClassNameForSortDecl;
  HookCompiler(Map sortClassNameForSortDecl) {
    this.sortClassNameForSortDecl = sortClassNameForSortDecl;
  }
  /**
    * Process the hooks, and attach them to the correct classes.
    */
  public GomClassList compile(
      HookDeclList declList,
      GomClassList classes,
      Map declToClassName) {
    /* for each hook, find the class, and attach the hook */
    %match(declList) {
      concHookDecl(_*,hook,_*) -> {
        Decl decl = `hook.getPointcut();
        %match(decl) {
          CutModule(mdecl) -> {
            ClassName clsName = (ClassName) declToClassName.get(`mdecl);
            try {
              classes = (GomClassList)
                `TopDown(AttachModuleHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException("Unexpected strategy failure!");
            }
          }
          CutSort(sdecl) -> {
            ClassName clsName = (ClassName) declToClassName.get(`sdecl);
            try {
              classes = (GomClassList)
                `TopDown(AttachSortHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException("Unexpected strategy failure!");
            }
          }
          CutOperator(odecl) -> {
            ClassName clsName = (ClassName) declToClassName.get(`odecl);
            try {
              classes = (GomClassList)
                `TopDown(AttachOperatorHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException("Unexpected strategy failure!");
            }     
          }
        }
      }
    }
    return classes;
  }

  %strategy AttachModuleHook(cName:ClassName,hook:HookDecl)
    extends Identity() {
      visit GomClass {
        obj@AbstractTypeClass[ClassName=className,Hooks=oldHooks] -> {
          if (`className == `cName) {
            return
              `obj.setHooks(`concHook(makeHooksFromHookDecl(hook),oldHooks*));
          }
        }
      }
    }

  %strategy AttachSortHook(cName:ClassName,hook:HookDecl)
    extends Identity() {
      visit GomClass {
        obj@SortClass[ClassName=className,Hooks=oldHooks] -> {
          if (`className == `cName) {
            return
              `obj.setHooks(`concHook(makeHooksFromHookDecl(hook),oldHooks*));
          }
        }
      }
    }

  %strategy AttachOperatorHook(cName:ClassName,hook:HookDecl)
    extends Identity() {
      visit GomClass {
        obj@VariadicOperatorClass[ClassName=className,Hooks=oldHooks,
          Empty=emptyClass,Cons=consClass] -> {
            if (`className == `cName) {
              /* We may want to attach the hook to the cons or empty */
              if (hook.isMakeHookDecl()) {
                if (hook.getSlotArgs() != `concSlot()) {
                  HookList oldConsHooks = `consClass.getHooks();
                  GomClass newCons =
                    `consClass.setHooks(
                        `concHook(makeHooksFromHookDecl(hook),oldConsHooks*));
                  return `obj.setCons(newCons);
                } else if (hook.getSlotArgs() == `concSlot()) {
                  HookList oldEmptyHooks = `emptyClass.getHooks();
                  GomClass newEmpty =
                    `emptyClass.setHooks(
                        `concHook(makeHooksFromHookDecl(hook),oldEmptyHooks*));
                  return `obj.setEmpty(newEmpty);
                }
              } else {
                return
                  `obj.setHooks(`concHook(makeHooksFromHookDecl(hook),oldHooks*));
              }
            }
          }
        obj@OperatorClass[ClassName=className,Hooks=oldHooks] -> {
          if (`className == `cName) {
            return
              `obj.setHooks(`concHook(makeHooksFromHookDecl(hook),oldHooks*));
          }
        }
      }
    }

  private static Hook makeHooksFromHookDecl(HookDecl hookDecl) {
    %match(hookDecl) {
      MakeHookDecl[SlotArgs=slotArgs,Code=hookCode] -> {
        SlotFieldList newArgs = makeSlotFieldListFromSlotList(`slotArgs);
        return `MakeHook(newArgs,hookCode);
      }
      BlockHookDecl[Code=hookCode] -> {
        return `BlockHook(hookCode);
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

  private static SlotFieldList makeSlotFieldListFromSlotList(SlotList args) {
    SlotFieldList newArgs = `concSlotField();
    while(!args.isEmptyconcSlot()) {
      Slot arg = args.getHeadconcSlot();
      args = args.getTailconcSlot();
      %match(Slot arg) {
        Slot[Name=slotName,Sort=sortDecl] -> {
          ClassName slotClassName = (ClassName)
            sortClassNameForSortDecl.get(`sortDecl);
          newArgs = `concSlotField(newArgs*,SlotField(slotName,slotClassName));
        }
      }
    }
    return newArgs;
  }
}
