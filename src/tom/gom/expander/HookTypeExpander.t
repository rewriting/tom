/*
 *
 * GOM
 *
 * Copyright (c) 2006-2009, INRIA
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
import tom.gom.tools.GomEnvironment;
import tom.gom.expander.rule.RuleExpander;
import tom.gom.expander.rule.GraphRuleExpander;

public class HookTypeExpander {

  %include { ../adt/gom/Gom.tom}

  private ModuleList moduleList;
  private ArrayList<Decl> sortsWithGraphrules;
  private GomEnvironment gomEnvironment;

  public HookTypeExpander(ModuleList moduleList,GomEnvironment gomEnvironment) {
    this.moduleList = moduleList;
    sortsWithGraphrules = new ArrayList<Decl>();
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  /**
    * Get the correct types for hooks, and attach them the correct Decl
    */
  public HookDeclList expand(GomModuleList gomModuleList) {
    HookDeclList hookList = `ConcHookDecl();
    /* For each hook in the GomModuleList */
    %match(gomModuleList) {
      ConcGomModule(_*,
          GomModule[ModuleName=GomModuleName[Name=moduleName],
          SectionList=ConcSection(_*,
            Public(ConcGrammar(_*,
                Grammar(prodList),
                _*)),
            _*)],
          _*) -> {
        %match(prodList) {
          ConcProduction(_*, prod, _*) -> {

            /* Process hooks attached to a module */
            %match(prod) {
              hook@Hook[NameType=KindModule(),HookType=hkind,Name=mname] -> {
                //Graph-rewrite rules hooks are only allowed for sorts
                if(`hkind.getkind().equals("graphrules")) {
                  getLogger().log(Level.SEVERE,
                      "Grapphrules hooks are authorised only on sorts");

                }
                if(`mname.equals(`moduleName)) {
                  ModuleDecl mdecl = getModuleDecl(`mname,moduleList);
                  HookDeclList newDeclList =
                    makeHookDeclList(`hook,`CutModule(mdecl));
                  hookList = `ConcHookDecl(newDeclList*,hookList*);
                } else {
                  getLogger().log(Level.SEVERE,
                      "Hooks on module are authorised only on the current module");
                }
              }
              hook@Hook[NameType=KindSort(),Name=sname] -> {
                SortDecl sdecl = getSortDecl(`sname,`moduleName,moduleList);
                HookDeclList newDeclList = makeHookDeclList(`hook,`CutSort(sdecl));
                hookList = `ConcHookDecl(newDeclList*,hookList*);
              }
              hook@Hook[NameType=KindOperator(),HookType=hkind,Name=oname] -> {
                //Graph-rewrite rules hooks are only allowed for sorts
                if(`hkind.getkind().equals("graphrules")) {
                  getLogger().log(Level.SEVERE,
                      "Grapphrules hooks are authorised only on sorts");

                }
                OperatorDecl odecl = getOperatorDecl(`oname,`moduleName,moduleList);
                if(odecl!=null) {
                  HookDeclList newDeclList = makeHookDeclList(`hook,`CutOperator(odecl));
                  hookList = `ConcHookDecl(newDeclList*,hookList*);
                }
              }
              hook@Hook[NameType=KindFutureOperator(fut),Name=oname] -> {
                OperatorDecl odecl = getOperatorDecl(`oname,`moduleName,moduleList);
                if(odecl!=null) {
                  HookDeclList newDeclList = 
                    makeHookDeclList(`hook,`CutFutureOperator(odecl,fut));
                  hookList = `ConcHookDecl(newDeclList*,hookList*);
                }
              }
            }
          }
        }
        ArrayList<String> examinedOps = new ArrayList<String>();
        %match(prodList) {
          ConcProduction(_*, prod, _*) -> {
            hookList = addDefaultTheoryHooks(`prod,`hookList,examinedOps,`moduleName);
          }
        }
        %match(prodList) {
          ConcProduction(_*,SortType[ProductionList=ConcProduction(_*,prod,_*)],_*) -> {
            hookList = addDefaultTheoryHooks(`prod,`hookList,examinedOps,`moduleName);
          }
        }
      }
    }
    return hookList;
  }

  private HookDeclList addDefaultTheoryHooks(Production prod,
                                             HookDeclList hookList,
                                             ArrayList<String> examinedOps,
                                             String moduleName) {
    %match(prod, hookList) {
      /* check domain and codomain are equals */
      Production(opName,ConcField(StarredField(codomain,_)),codomain,_),
      //Production(_,opName,ConcField(StarredField(codomain,_)),codomain,_),
      /* check there is no other MakeHook attached to this operator */
      !ConcHookDecl(_*, MakeHookDecl[Pointcut=CutOperator[ODecl=OperatorDecl[Name=opName]]], _*) -> {
        /* generate a FL hook for list-operators without other hook */
        String emptyCode = "{}";
        Production hook = `Hook(KindOperator(),opName,HookKind("FL"),ConcArg(),emptyCode,OptionList());
        OperatorDecl odecl = getOperatorDecl(`opName,`moduleName,moduleList);
        if(odecl!=null) {
          HookDeclList newDeclList = makeHookDeclList(`hook,`CutOperator(odecl));
          hookList = `ConcHookDecl(newDeclList*,hookList*);
        }
      }
      /* check domain and codomain are equals */
      Production(opName,ConcField(StarredField(codomain,_)),codomain,_),
      //Production(_,opName,ConcField(StarredField(codomain,_)),codomain,_),
      /* check there is a make_insert or a rule hooks and no theory associated */
      ConcHookDecl(_*,MakeHookDecl[HookType=HookKind[kind="make_insert"|"make_empty"|"rules"]],_*) -> {
        if(! examinedOps.contains(`opName)) {
          examinedOps.add(`opName);
          %match(hookList) {
            /* check there is no associtated theory */
            !ConcHookDecl(_*, MakeHookDecl[Pointcut=CutOperator[ODecl=OperatorDecl[Name=opName]],HookType=HookKind[kind="Free"|"FL"|"AU"|"AC"|"ACU"]], _*) -> {
              /* generate an error to make users specify the theory */
              getLogger().log(Level.SEVERE,
                "As you use make_insert, make_empty or rules, specify the associated theory for the variadic operator "+`opName);
            }
          }
        }
      }
    }
    return hookList;
  }

  private HookDeclList makeHookDeclList(Production hook, Decl mdecl) {
    %match(hook) {
      Hook[HookType=hkind,
        Name=hName,
        Args=hookArgs,
        StringCode=scode] -> {
          HookDeclList newHookList = `ConcHookDecl();
          %match(hkind) {
            HookKind("block") -> {
              newHookList = `ConcHookDecl(
                  BlockHookDecl(mdecl,Code(trimBracket(scode)),true()));
            }
            HookKind("javablock") -> {
              newHookList = `ConcHookDecl(
                  BlockHookDecl(mdecl,Code(trimBracket(scode)),false()));
            }
            HookKind("interface") -> {
              newHookList = `ConcHookDecl(
                  InterfaceHookDecl(mdecl,Code(trimBracket(scode))));
            }
            HookKind("import") -> {
              newHookList = `ConcHookDecl(
                  ImportHookDecl(mdecl,Code(trimBracket(scode))));
            }
            HookKind("mapping") -> {
              newHookList = `ConcHookDecl(
                  MappingHookDecl(mdecl,Code(trimBracket(scode))));
            }
            kind@HookKind(("make"|"make_insert"|"make_empty")[]) -> {
              SlotList typedArgs = typeArguments(`hookArgs,`hkind,`mdecl);
              if (typedArgs == null) {
                getLogger().log(Level.SEVERE,
                    GomMessage.discardedHook.getMessage(),
                    new Object[]{ `(hName) });
                return `ConcHookDecl();
              }
              newHookList = `ConcHookDecl(
                  MakeHookDecl(mdecl,typedArgs,Code(scode),kind,true()));
            }
            HookKind("Free") -> {
              /* Even there is no code associated, we generate a MakeHook to prevent FL hooks to be automatically generated */
              return `ConcHookDecl(
                  MakeHookDecl(mdecl,ConcSlot(),Code(""),HookKind("Free"),false()));
            }
            HookKind("FL") -> {
              /* FL: flattened list */
              return `makeFLHookList(hName,mdecl,scode);
            }
            HookKind("AU") -> {
              return `makeAUHookList(hName,mdecl,scode);
            }
            HookKind("AC") -> {
              return `makeACHookList(hName,mdecl,scode);
            }
            HookKind("ACU") -> {
              return `makeACUHookList(hName,mdecl,scode);
            }
            HookKind("rules") -> {
              return `makeRulesHookList(hName,mdecl,scode);
            }
            HookKind("graphrules") -> {
              //TODO: verify if the option termgraph is on
              if(`hookArgs.length()!=2) {
                throw new GomRuntimeException(
                    "GomTypeExpander:graphrules hooks need two parameters: the name of the generated strategy and its default behaviour");
              }
              return `makeGraphRulesHookList(hName,hookArgs,mdecl,scode);
            }
          }
          if (newHookList == `ConcHookDecl()) {
            getLogger().log(Level.SEVERE, GomMessage.unknownHookKind.getMessage(), new Object[]{ `(hkind) });
          }
          return newHookList;
        }
    }
    throw new GomRuntimeException("HookTypeExpander: this hook is not a hook: "+ `hook );
  }

  /**
   * Finds the ModuleDecl corresponding to a module name.
   *
   * @param mname the module name
   * @param moduleList the queried ModuleList
   * @return the ModuleDecl for mname
   */
  private ModuleDecl getModuleDecl(String mname, ModuleList moduleList) {
    %match(moduleList) {
      ConcModule(_*,
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
  private SortDecl getSortDecl(String sname, String modName, ModuleList moduleList) {
    %match(String modName, moduleList) {
      mname,
        ConcModule(_*,
            Module[
            MDecl=ModuleDecl[ModuleName=GomModuleName[Name=mname]],
            Sorts=ConcSort(_*,Sort[Decl=sdecl@SortDecl[Name=sortName]],_*)],
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
  private OperatorDecl getOperatorDecl(
      String oname,
      String modName,
      ModuleList moduleList) {
    %match(String modName, moduleList) {
      mname,
      ConcModule(_*,
          Module[
          MDecl=ModuleDecl[ModuleName=GomModuleName[Name=mname]],
          Sorts=ConcSort(_*,Sort[
            OperatorDecls=ConcOperator(_*,odecl@OperatorDecl[Name=operName],_*)
            ],_*)],
          _*) -> {
        if (`operName.equals(oname)) {
          return `odecl;
        }
      }
    }
    getLogger().log(Level.SEVERE,
        GomMessage.orphanedHook.getMessage(),
        new Object[]{oname});
    return null;
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
              %match(args) {
                ConcArg(Arg(head),Arg(tail)) -> {
                  return `ConcSlot(Slot(head,sortDecl),Slot(tail,sort));
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
            ODecl=OperatorDecl[Prod=Variadic[]]] -> {
              // for a make_empty hook, there is no argument
              %match(args) {
                ConcArg() -> { return `ConcSlot(); }
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
    %match(args, slots) {
      ConcArg(),ConcSlot() -> {
        return `ConcSlot();
      }
      ConcArg(Arg[Name=argName],ta*),ConcSlot(Slot[Sort=slotSort],ts*) -> {
        SlotList tail = recArgSlots(`ta,`ts);
        return `ConcSlot(Slot(argName,slotSort),tail*);
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
            "FL/AU/AC/ACU hook can only be used on a variadic operator");
      }
    }
    return null;
  }

  /*
   * generate hooks for normalizing rules 
   */
  private HookDeclList makeRulesHookList(String opName, Decl mdecl, String scode) {
    RuleExpander rexpander = new RuleExpander(moduleList);
    return rexpander.expandRules(trimBracket(scode));
  }

  /*
   * generate hooks for term-graph rules 
   */
  private HookDeclList makeGraphRulesHookList(String sortname, ArgList args, Decl sdecl, String scode) {
    %match(args) {
      ConcArg(Arg[Name=stratname],Arg[Name=defaultstrat]) -> {
        if (!`defaultstrat.equals("Fail") && !`defaultstrat.equals("Identity")) {
          getLogger().log(Level.SEVERE,
              "In graphrules hooks, the default strategies authorized are only Fail and Identity");
        }
        GraphRuleExpander rexpander = new GraphRuleExpander(moduleList,getGomEnvironment());
        if (sortsWithGraphrules.contains(sdecl)) {
          return rexpander.expandGraphRules(sortname,`stratname,`defaultstrat,trimBracket(scode),sdecl);
        } else {
          sortsWithGraphrules.add(sdecl);
          return rexpander.expandFirstGraphRules(sortname,`stratname,`defaultstrat,trimBracket(scode),sdecl);
        }
      }
    }
    return null;
  }

  /*
   * generate hooks for associative-commutative without neutral element
   */
  private HookDeclList makeACHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain)
      return `ConcHookDecl();

    HookDeclList acHooks = `ConcHookDecl();
     /*
      * Remove neutral and flatten:
     * if(<head>.isEmpty<Conc>()) { return <tail>; }
     * if(<head>.isCons<Conc>()) { return make(head.head,make(head.tail,tail)); }
     * if(!<tail>.isCons<Conc>() && !<tail>.isEmpty<Conc>()) { return readMake(<tail>,<empty>); }
     */
    acHooks = `ConcHookDecl(
        MakeHookDecl(
          mdecl,
          ConcSlot(Slot("head",domain),Slot("tail",domain)),
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
            Code(") { return make(head,realMake(tail,Empty" + opName + ".make())); }\n")
            ),HookKind("FL"),false()),
        acHooks*);
    /*
     * add the following hooks:
     * if(tail.isConc) {
     *   if(head < tail.head) {
     *     tmp = head
     *     head = tail.head
     *     tail = cons(tmp,tail.tail)
     *   }
     * }
     * // in all cases:
     * return makeReal(head,tail)
     */
    acHooks = `ConcHookDecl(
        MakeHookDecl(
          mdecl,
          ConcSlot(Slot("head",domain),Slot("tail",domain)),
          CodeList(
            Code("if ("),
            IsCons("tail",mdecl.getODecl()),
            Code(") {\n"),
            Code("  if (0 < "),
            Compare(Code("head"),Code("tail.getHead" + opName + "()")),
            Code(") {\n"),
            Code("    "),FullSortClass(domain),Code(" tmpHd = head;\n"),
            Code("    head = tail.getHead" + opName + "();\n"),
            Code("    tail = `"+opName+"(tmpHd,tail.getTail" + opName + "());\n"),
            Code("  }\n"),
            Code("}\n")
            ),HookKind("AC"),true()),
            acHooks*);
    return acHooks;
  }
  /*
   * generate hooks for associative-commutative with neutral element
   */
  private HookDeclList makeACUHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain)
      return `ConcHookDecl();

    /* start with AU normalization */
    HookDeclList acuHooks = makeAUHookList(opName, mdecl, scode);
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
    acuHooks = `ConcHookDecl(
        MakeHookDecl(
          mdecl,
          ConcSlot(Slot("head",domain),Slot("tail",domain)),
          CodeList(
            Code("if ("),
            IsCons("tail",mdecl.getODecl()),
            Code(") {\n"),
            Code("  if (0 < "),
            Compare(Code("head"),Code("tail.getHead" + opName + "()")),
            Code(") {\n"),
            Code("    "),FullSortClass(domain),Code(" tmpHd = head;\n"),
            Code("    head = tail.getHead" + opName + "();\n"),
            Code("    tail = `"+opName+"(tmpHd,tail.getTail" + opName + "());\n"),
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
            ),HookKind("ACU"),true()),
            acuHooks*);
    return acuHooks;
  }

  /*
   * generate hooks for associative with neutral element
   */
  private HookDeclList makeAUHookList(String opName, Decl mdecl, String scode) {
    /* Can only be applied to a variadic operator, whose domain and codomain
     * are equals */
    SortDecl domain = getSortAndCheck(mdecl);
    if (null == domain)
      return `ConcHookDecl();

    HookDeclList auHooks = `ConcHookDecl();
    String userNeutral = trimBracket(scode);
    if(userNeutral.length() > 0) {
      /* The hook body is the name of the neutral element */
      auHooks = `ConcHookDecl(
          MakeHookDecl(mdecl,ConcSlot(),Code("return "+userNeutral+";"),HookKind("AU"),true()),
          auHooks*);
      /* 
       * Remove neutral:
       * if(<head> == makeNeutral) { return <tail>; }
       * if(<tail> == makeNeutral) { return <head>; }
       */
      auHooks = `ConcHookDecl(
          MakeHookDecl(
            mdecl,
            ConcSlot(Slot("head",domain),Slot("tail",domain)),
            CodeList(
              Code("if (head == "+userNeutral+") { return tail; }\n"),
              Code("if (tail ==  "+userNeutral+") { return head; }\n")
              ),HookKind("AU"),true()),
          auHooks*);
    }
    /* getODecl call is safe here, since mdecl was checked by getSortAndCheck */
    /*
     * Remove neutral and flatten:
     * if(<head>.isEmpty<conc>()) { return <tail>; }
     * if(<tail>.isEmpty<conc>()) { return <head>; }
     * if(<head>.isCons<conc>()) { return make(head.head,make(head.tail,tail)); }
     */
    auHooks = `ConcHookDecl(
        MakeHookDecl(
          mdecl,
          ConcSlot(Slot("head",domain),Slot("tail",domain)),
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
            ),HookKind("AU"),false()),
        auHooks*);

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
      return `ConcHookDecl();
    }

    String userNeutral = trimBracket(scode);
    if(userNeutral.length() > 0) {
      getLogger().log(Level.SEVERE,
          "FL hook does not allow the definition of a neutral element");
    }

    HookDeclList hooks = `ConcHookDecl();
    /* getODecl call is safe here, since mdecl was checked by getSortAndCheck */
    /*
     * Remove neutral and flatten:
     * if(<head>.isEmpty<Conc>()) { return <tail>; }
     * if(<head>.isCons<Conc>()) { return make(head.head,make(head.tail,tail)); }
     * if(!<tail>.isCons<Conc>() && !<tail>.isEmpty<Conc>()) { return make(<tail>,<empty>); }
     */
    hooks = `ConcHookDecl(
        MakeHookDecl(
          mdecl,
          ConcSlot(Slot("head",domain),Slot("tail",domain)),
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
            ),HookKind("FL"),false()),
        hooks*);

    return hooks;
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
