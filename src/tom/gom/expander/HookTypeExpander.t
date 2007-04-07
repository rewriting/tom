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
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class HookTypeExpander {

  %include { ../adt/gom/Gom.tom}

  private ModuleList moduleList;

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
              HookDeclList newDeclList =
                makeHookDeclList(`hook,`CutModule(mdecl));
              hookList = `concHookDecl(newDeclList*,hookList*);
            } else {
              getLogger().log(Level.SEVERE,
                  "Hooks on module are authorised only on the current module");
            }
          }
          Hook[NameType=KindSort(),Name=sname] -> {
            SortDecl sdecl = getSortDecl(`sname,`moduleName,moduleList);
            HookDeclList newDeclList = makeHookDeclList(`hook,`CutSort(sdecl));
            hookList = `concHookDecl(newDeclList*,hookList*);
          }
          Hook[NameType=KindOperator(),Name=oname] -> {
            OperatorDecl odecl = getOperatorDecl(`oname,`moduleName,moduleList);
            HookDeclList newDeclList = makeHookDeclList(`hook,`CutOperator(odecl));
            hookList = `concHookDecl(newDeclList*,hookList*);
          }
        }
      }
    }
    return hookList;
  }

  HookDeclList makeHookDeclList(Production hook, Decl mdecl) {
    %match(hook) {
      Hook[HookType=hkind,
           Name=hName,
           Args=hookArgs,
           StringCode=scode] -> {
        HookDeclList newHookList = `concHookDecl();
        %match(HookKind `hkind) {
          HookKind("block") -> {
            newHookList = `concHookDecl(
                BlockHookDecl(mdecl,Code(trimBracket(scode))));
          }
          HookKind("interface") -> {
            newHookList = `concHookDecl(
                InterfaceHookDecl(mdecl,Code(trimBracket(scode))));
          }
          HookKind("import") -> {
            newHookList = `concHookDecl(
                ImportHookDecl(mdecl,Code(trimBracket(scode))));
          }
          HookKind("mapping") -> {
            newHookList = `concHookDecl(
                MappingHookDecl(mdecl,Code(trimBracket(scode))));
          }
          HookKind(("make"|"make_insert"|"make_empty")[]) -> {
            SlotList typedArgs = typeArguments(`hookArgs,`hkind,`mdecl);
            if (typedArgs == null) {
              getLogger().log(Level.SEVERE,
                  GomMessage.discardedHook.getMessage(),
                  new Object[]{ `(hName) });
              return `concHookDecl();
            }
            newHookList = `concHookDecl(
                MakeHookDecl(mdecl,typedArgs,Code(scode)));
          }
          HookKind("FL") -> {
            return `makeFLHookList(hName,mdecl,scode);
          }
          HookKind("AU") -> {
            return `makeAUHookList(hName,mdecl,scode);
          }
          HookKind("ACU") -> {
            return `makeACUHookList(hName,mdecl,scode);
          }
        }
        if (newHookList == `concHookDecl()) {
          throw new GomRuntimeException(
              "GomTypeExpander:typeModuleHook unknown HookKind: "+`hkind);
        }
        return newHookList;
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
      HookKind(hookName@"make_insert") -> {
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
                    GomMessage.badHookArguments.getMessage(),
                    new Object[]{ `(hookName), new Integer(args.length())});
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
      HookKind(hookName@"make_empty") -> {
        /*
         * The TypedProduction has to be Variadic
         * Then we get the codomain from the operatordecl
         */
        %match(decl) {
          CutOperator[
            ODecl=OperatorDecl[Sort=sort,Prod=Variadic(sortDecl)]] -> {
            // for a make_empty hook, there is no argument
            %match(ArgList args) {
              concArg() -> { return `concSlot(); }
              _ -> {
                getLogger().log(Level.SEVERE,
                    GomMessage.badHookArguments.getMessage(),
                    new Object[]{ `(hookName), new Integer(args.length())});
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

  private String trimBracket(String stringCode) {
    int start = `stringCode.indexOf('{')+1;
    int end = `stringCode.lastIndexOf('}');
    return `stringCode.substring(start,end).trim();
  }

  private SortDecl getSortAndCheck(Decl mdecl) {
    %match(mdecl) {
      CutOperator(OperatorDecl[Sort=domainsdecl,Prod=Variadic[Sort=sdecl]]) -> {
        if (`domainsdecl == `sdecl) {
          return `sdecl;
        } else {
          getLogger().log(Level.SEVERE,
              "Different domain and codomain");
        }
      }
      _ -> {
        getLogger().log(Level.SEVERE,
            "FL/AU/ACU hook can only be used on a variadic operator");
      }
    }
    return null;
  }

  /*
   * generate hooks for associative-commutative with neutral element
   */
  private HookDeclList makeACUHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain)
      return `concHookDecl();

    /* start with AU normalization */
    HookDeclList acHooks = makeAUHookList(opName, mdecl, scode);
    /*
     * add the following hooks:
     * if(tail.isConc) {
     *   if(head < tail.head) {
     *     tmp = head
     *     head = tail.head
     *     tail = cons(tmp,tail.tail)
     *   }
     * } else if(head < tail) {
     *   tmp = head
     *   head = tail
     *   tail = tmp
     * }
     * // in all cases:
     * return makeReal(head,tail)
     */
    acHooks = `concHookDecl(
        MakeHookDecl(
          mdecl,
          concSlot(Slot("head",domain),Slot("tail",domain)),
          CodeList(
            Code("if ("),
            IsCons("tail",mdecl.getODecl()),
            Code(") {\n"),
            Code("  if (0 < "),
            Compare(Code("head"),Code("tail.getHead" + opName + "()")),
            Code(") {\n"),
            Code("    "),FullSortClass(domain),Code(" tmpHd = head;\n"),
            Code("    head = tail.getHead" + opName + "();\n"),
            Code("    tail = `" + opName + "(tmpHd,tail.getTail" + opName + "());\n"),
            Code("  }\n"),
            Code("} else {\n"),
            Code("  if (0 < "),
            Compare(Code("head"),Code("tail")),
            Code(") {\n"),
            Code("    "),FullSortClass(domain),Code(" tmpHd = head;\n"),
            Code("    head = tail;\n"),
            Code("    tail = tmpHd;\n"),
            Code("  }\n"),
            Code("}\n")
            )),
        acHooks*);
    return acHooks;
  }

  /*
   * generate hooks for associative with neutral element
   */
  private HookDeclList makeAUHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain)
      return `concHookDecl();

    HookDeclList auHooks = `concHookDecl();
    String userNeutral = trimBracket(scode);
    if(userNeutral.length() > 0) {
      /* The hook body is the name of the neutral element */
      auHooks = `concHookDecl(
          MakeHookDecl(mdecl,concSlot(),Code("return "+userNeutral+";")),
          auHooks*);
      /* 
       * Remove neutral:
       * if(<head> == makeNeutral) { return <tail>; }
       * if(<tail> == makeNeutral) { return <head>; }
       */
      auHooks = `concHookDecl(
          MakeHookDecl(
            mdecl,
            concSlot(Slot("head",domain),Slot("tail",domain)),
            CodeList(
              Code("if (head == "+userNeutral+") { return tail; }\n"),
              Code("if (tail ==  "+userNeutral+") { return head; }\n")
              )),
          auHooks*);
    }
    /* getODecl call is safe here, since mdecl was checked by getSortAndCheck */
    /*
     * Remove neutral and flatten:
     * if(<head>.isEmpty<conc>()) { return <tail>; }
     * if(<tail>.isEmpty<conc>()) { return <head>; }
     * if(<head>.isCons<conc>()) { return make(head.head,make(head.tail,tail)); }
     */
    auHooks = `concHookDecl(
        MakeHookDecl(
          mdecl,
          concSlot(Slot("head",domain),Slot("tail",domain)),
          CodeList(
            Code("if ("),
            IsEmpty("head",mdecl.getODecl()),
            Code(") { return tail; }\n"),
            Code("if ("),
            IsEmpty("tail",mdecl.getODecl()),
            Code(") { return head; }\n"),
            Code("if ("),
            IsCons("head",mdecl.getODecl()),
            Code(") { return make(head.getHead" + opName + "(),make(head.getTail" + opName + "(),tail)); }\n")
          )),
        auHooks*);

    /* The mapping for AU operators has to be correct */
    %match(mdecl) {
      CutOperator(OperatorDecl[Sort=domainsdecl@SortDecl[Name=sortName],
                               Prod=Variadic[]]) -> {
        auHooks = `concHookDecl(
            MappingHookDecl(
              mdecl,
              CodeList(
                Code("%oplist " + sortName),
		 // generate a second %oplist mapping for opName'?'
                Code(" " + opName + "?"),
                Code("(" + sortName + "*) {\n"),
                Code("is_fsym(t) { t instanceof "),
                FullSortClass(domainsdecl),
                Code("}\n"),
                Code("make_empty() { "),
                Empty(mdecl.getODecl()),
                Code(".make() }\n"),
                Code("make_insert(e,l) { "),
                Cons(mdecl.getODecl()),
                Code(".make(e,l) }\n"),
                Code("get_head(l) { ("),
                IsCons("l",mdecl.getODecl()),
                Code(")?(l."),
                Code("getHead" + opName + "()"),
                Code("):(l) }\n"),
                Code("get_tail(l) { ("),
                IsCons("l",mdecl.getODecl()),
                Code(")?(l."),
                Code("getTail" + opName + "()"),
                Code("):("),
                Empty(mdecl.getODecl()),
                Code(".make()) }\n"),
                Code("is_empty(l) { "),
                Code("l == "),
                Empty(mdecl.getODecl()),
                Code(".make() }\n"),
                Code("}\n")
                )),
            auHooks*);
      }
    }
    return auHooks;
  }
 
  /*
   * generate hooks for flattened lists (with empty list as last element)
   */
  private HookDeclList makeFLHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if(null == domain) {
      return `concHookDecl();
    }

    String userNeutral = trimBracket(scode);
    if(userNeutral.length() > 0) {
        getLogger().log(Level.SEVERE,
            "FL hook does not allow the definition of a neutral element");
    }

    HookDeclList hooks = `concHookDecl();
    /* getODecl call is safe here, since mdecl was checked by getSortAndCheck */
    /*
     * Remove neutral and flatten:
     * if(<head>.isEmpty<conc>()) { return <tail>; }
     * if(<head>.isCons<conc>()) { return make(head.head,make(head.tail,tail)); }
     * if(!<tail>.isCons<conc>() && !<tail>.isEmpty<conc>()) { return make(<tail>,<empty>); }
     */
    hooks = `concHookDecl(
        MakeHookDecl(
          mdecl,
          concSlot(Slot("head",domain),Slot("tail",domain)),
          CodeList(
            Code("if ("),
            IsEmpty("head",mdecl.getODecl()),
            Code(") { return tail; }\n"),
            Code("if ("),
            IsCons("head",mdecl.getODecl()),
            Code(") { return make(head.getHead" + opName + "(),make(head.getTail" + opName + "(),tail)); }\n"),
            Code("if (!"),
            IsCons("tail",mdecl.getODecl()),
            Code(" && !"),
            IsEmpty("tail",mdecl.getODecl()),
	    Code(") { return make(head,make(tail,Empty" + opName + ".make())); }\n")
          )),
        hooks*);

    return hooks;
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
