/*
 * Gom
 * 
 * Copyright (c) 2000-2008, INRIA
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
 * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
 * 
 **/

package tom.gom.expander;

import java.util.logging.Level;
import tom.gom.GomStreamManager;
import tom.gom.SymbolTable;
import tom.gom.adt.gom.types.*;
import tom.library.sl.*;
import java.util.ArrayList;
import java.util.Set;
import tom.gom.SymbolTable.*;
import tom.gom.tools.GomEnvironment;

public class FreshExpander {

  %include { ../adt/gom/Gom.tom}
  %include { util/ArrayList.tom }
  %include { sl.tom }  

  %typeterm SymbolTable { implement { tom.gom.SymbolTable } }
  %typeterm FreshExpander { implement { tom.gom.expander.FreshExpander } }

  private GomEnvironment gomEnvironment;

  public FreshExpander(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomModuleList expand(GomModuleList m, String pack) {
    try {
      ArrayList list = new ArrayList();
      GomModuleList res = (GomModuleList) 
        `Sequence(
            TopDown(ExpandAtoms(list)),
            TopDown(UpdateSpecialization(list))).visitLight(m);
      getGomEnvironment().getSymbolTable().setPackage(pack);
      getGomEnvironment().getSymbolTable().fill(res);
      res = addRawSortsAndConstructors(res);
      res = addAtomHooks(res);
      res = addSortHooks(res);
      res = addRawSortHooks(res);
      res = addConstructorHooks(res);
      res = addRawConstructorHooks(res);
      res = addMappingHooks(res);
      return res;
    } catch (VisitFailure e) {
      throw new RuntimeException("should not happen");
    } catch (SymbolTable.SortException e) {
      e.printStackTrace();
      throw new RuntimeException("should not happen");
    } catch (SymbolTable.ConstructorException e) {
      e.printStackTrace();
      throw new RuntimeException("should not happen");
    }
  }
  

  /* -- export, convert and alpha maps argument lists -- */

  private String alphamapArgList(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
        if (getGomEnvironment().getSymbolTable().getBoundAtoms(sort).contains(a)) {
          buf.append(%[,tom.library.freshgom.AlphaMap<@aid@> @a@OuterMap]%);
          buf.append(%[,tom.library.freshgom.AlphaMap<@aid@> @a@InnerMap]%);
        } else 
          buf.append(%[,tom.library.freshgom.AlphaMap<@aid@> @a@Map]%);
      } else {
        buf.append(%[,tom.library.freshgom.AlphaMap<@aid@> @a@Map]%);
      }
    }
    return buf.toString();
  }

  private String newAlphamapList(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      buf.append(%[,new tom.library.freshgom.AlphaMap<@aid@>()]%);
      if(getGomEnvironment().getSymbolTable().isPatternType(sort) && getGomEnvironment().getSymbolTable().getBoundAtoms(sort).contains(a)) 
        buf.append(%[,new tom.library.freshgom.AlphaMap<@aid@>()]%);
    }
    return buf.toString();
  }

  private String exportmapArgList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true; 
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      if(!first) buf.append(", ");
      else first = false;
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
        if (getGomEnvironment().getSymbolTable().getBoundAtoms(sort).contains(a)) {
          buf.append(%[tom.library.freshgom.ExportMap<@aid@> @a@OuterMap]%);
          buf.append(%[,tom.library.freshgom.ExportMap<@aid@> @a@InnerMap]%);
        } else 
          buf.append(%[tom.library.freshgom.ExportMap<@aid@> @a@Map]%);
      } else {
        buf.append(%[tom.library.freshgom.ExportMap<@aid@> @a@Map]%);
      }
    }
    return buf.toString();
  }

  private String newExportmapList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true; 
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      if(!first) buf.append(", ");
      else first = false;
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      buf.append(%[new tom.library.freshgom.ExportMap<@aid@>()]%);
      if(getGomEnvironment().getSymbolTable().isPatternType(sort) && getGomEnvironment().getSymbolTable().getBoundAtoms(sort).contains(a)) 
        buf.append(%[,new tom.library.freshgom.ExportMap<@aid@>()]%);
    }
    return buf.toString();
  }

  private String convertmapArgList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true; 
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      if(!first) buf.append(", ");
      else first = false;
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
        if (getGomEnvironment().getSymbolTable().getBoundAtoms(sort).contains(a)) {
          buf.append(%[tom.library.freshgom.ConvertMap<@aid@> @a@OuterMap]%);
          buf.append(%[,tom.library.freshgom.ConvertMap<@aid@> @a@InnerMap]%);
        } else 
          buf.append(%[tom.library.freshgom.ConvertMap<@aid@> @a@Map]%);
      } else {
        buf.append(%[tom.library.freshgom.ConvertMap<@aid@> @a@Map]%);
      }
    }
    return buf.toString();
  }

  private String newConvertmapList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true; 
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      if(!first) buf.append(", ");
      else first = false;
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      buf.append(%[new tom.library.freshgom.ConvertMap<@aid@>()]%);
      if(getGomEnvironment().getSymbolTable().isPatternType(sort) && getGomEnvironment().getSymbolTable().getBoundAtoms(sort).contains(a)) 
        buf.append(%[,new tom.library.freshgom.ConvertMap<@aid@>()]%);
    }
    return buf.toString();
  }

  /* call to expression field inside expression type */
  private String alphaRecCall1(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      buf.append(%[,@a@Map]%);
    }
    return buf.toString();
  }

  /* call to pattern field inside expression type */
  private String alphaRecCall2(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      buf.append(%[,@a@Map]%);
      if(getGomEnvironment().getSymbolTable().getBoundAtoms(sort).contains(a))
        buf.append(%[,@a@InnerMap]%);
    }
    return buf.toString();
  }

  /* call to inner field inside pattern type */
  private String alphaRecCall3(String tosort, String fromsort) {
    StringBuffer buf = new StringBuffer();
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(tosort)) {
      if(getGomEnvironment().getSymbolTable().getBoundAtoms(fromsort).contains(a)) 
        buf.append(%[,@a@InnerMap]%);
      else
        buf.append(%[,@a@Map]%);
    }
    return buf.toString();
  }

  /* call to outer field inside pattern type */
  private String alphaRecCall4(String tosort, String fromsort) {
    StringBuffer buf = new StringBuffer();
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(tosort)) {
      if(getGomEnvironment().getSymbolTable().getBoundAtoms(fromsort).contains(a)) 
        buf.append(%[,@a@OuterMap]%);
      else
        buf.append(%[,@a@Map]%);
    }
    return buf.toString();
  }

  /* call to neutral field inside pattern type */
  private String alphaRecCall5(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      buf.append(%[,@a@Map]%);
    }
    return buf.toString();
  }

  /* call to pattern field inside pattern type */
  private String alphaRecCall6(String tosort) {
    StringBuffer buf = new StringBuffer();
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(tosort)) {
      // bound atoms have to be the same
      if(getGomEnvironment().getSymbolTable().getBoundAtoms(tosort).contains(a)) {
        buf.append(%[,@a@OuterMap]%);
        buf.append(%[,@a@InnerMap]%);
      } else buf.append(%[,@a@Map]%);
    }
    return buf.toString();
  }


  /* call to expression field inside expression type */
  private String recCall1(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      if(!first) buf.append(",");
      else first = false;
      buf.append(%[@a@Map]%);
    }
    return buf.toString();
  }

  /* call to pattern field inside expression type */
  private String recCall2(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      if(!first) buf.append(",");
      else first = false;
      buf.append(%[@a@Map]%);
      if(getGomEnvironment().getSymbolTable().getBoundAtoms(sort).contains(a))
        buf.append(%[,@a@InnerMap]%);
    }
    return buf.toString();
  }

  /* call to inner field inside pattern type */
  private String recCall3(String tosort, String fromsort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    // for(String a: st.getAccessibleAtoms(tosort)) {
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(tosort)) {
      if(!first) buf.append(",");
      else first = false;
      // if(st.getBoundAtoms(fromsort).contains(a)) 
      if(getGomEnvironment().getSymbolTable().getBoundAtoms(fromsort).contains(a)) 
        buf.append(%[@a@InnerMap]%);
      else
        buf.append(%[@a@Map]%);
    }
    return buf.toString();
  }

  /* call to outer field inside pattern type */
  private String recCall4(String tosort, String fromsort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(tosort)) {
      if(!first) buf.append(",");
      else first = false;
      if(getGomEnvironment().getSymbolTable().getBoundAtoms(fromsort).contains(a)) 
        buf.append(%[@a@OuterMap]%);
      else
        buf.append(%[@a@Map]%);
    }
    return buf.toString();
  }

  /* call to neutral field inside pattern type */
  private String recCall5(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      if(!first) buf.append(",");
      else first = false;
      buf.append(%[@a@Map]%);
    }
    return buf.toString();
  }

  /* call to pattern field inside pattern type */
  private String recCall6(String tosort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(tosort)) {
      if(!first) buf.append(",");
      else first = false;
      // bound atoms have to be the same
      if(getGomEnvironment().getSymbolTable().getBoundAtoms(tosort).contains(a)) {
        buf.append(%[@a@OuterMap]%);
        buf.append(%[,@a@InnerMap]%);
      } else buf.append(%[@a@Map]%);
    }
    return buf.toString();
  }

  /* hashTables for 'refresh' methods" */
  private String hashtableArgList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(sort)) {
      if(!first) buf.append(", ");
      else first = false;
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      buf.append( %[java.util.Hashtable<@aid@,@aid@> @a@Map]% );
    }
    return buf.toString();
  }

  /* hashTables for 'refresh' methods" */
  private String newHashtableList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(sort)) {
      if(!first) buf.append(", ");
      else first = false;
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      buf.append(%[new java.util.Hashtable<@aid@,@aid@>()]%);
    }
    return buf.toString();
  }

  /* generates "A1Map,A2Map,...,AnMap" */
  private String hashtableRecursiveCall(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(sort)) {
      if(!first) buf.append(", ");
      else first = false;
      buf.append(%[@a@Map]%);
    }
    return buf.toString();
  }

  /* generates "raw_f1,raw_f2,...,raw_fn" for fields of cons
   except for builtins where no new is added */
  private String rawArgList(String cons) {
    String sort = getGomEnvironment().getSymbolTable().getSort(cons);
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String f: getGomEnvironment().getSymbolTable().getFields(cons)) {
      if(!first) buf.append(",");
      else first = false;
      buf.append(getGomEnvironment().isBuiltin(getGomEnvironment().getSymbolTable().getSort(cons,f))? %[get@f@()]%:%[raw_@f@]%);
    }
    return buf.toString();
  }

  /* generates "f1,f2,...,fn" for fields of cons
   except for builtins where no new is added */
  private String convertArgList(String cons) {
    String sort = getGomEnvironment().getSymbolTable().getSort(cons);
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String f: getGomEnvironment().getSymbolTable().getFields(cons)) {
      if(!first) buf.append(",");
      else first = false;
      buf.append(getGomEnvironment().isBuiltin(getGomEnvironment().getSymbolTable().getSort(cons,f))? getGomEnvironment().getSymbolTable().rawGetter(cons,f) :f);
    }
    return buf.toString();
  }


  /* -- hooks utilities -- */

  %strategy AddHook(sort:String,hook:Production) extends Fail() {
    visit ProductionList {
      (p@SortType[Type=GomType[Name=n]],ps*) -> {
        if(`n.equals(sort)) 
          return `ConcProduction(p,hook,ps*);
      }
    }
  }

  /* add the hook next to sort declaration */
  private GomModuleList addSortBlockHook
    (GomModuleList ml, String sort, String code) {
      Production hook = 
        `Hook(KindSort(),sort,HookKind("block"),
            ConcArg(),code,OptionList());
      try { 
        return (GomModuleList)
          `OnceTopDown(AddHook(sort,hook)).visitLight(ml); 
      }
      catch (VisitFailure e) { 
        throw new RuntimeException("Should never happen"); 
      }
    }

  /* add the hook next to the declaration of the constructor's sort */
  private GomModuleList addConstructorBlockHook
    (GomModuleList ml, String cons, String code) {
      String sort = getGomEnvironment().getSymbolTable().getSort(cons);
      IdKind kind = `KindOperator();
      // if the constructor is a generated cons or nil, add a 'future' hook
      if(getGomEnvironment().getSymbolTable().isGeneratedCons(cons)) { 
        kind = `KindFutureOperator(FutureCons());
        cons = getGomEnvironment().getSymbolTable().getBaseName(cons);
      } else if (getGomEnvironment().getSymbolTable().isGeneratedNil(cons)) { 
        kind = `KindFutureOperator(FutureNil());
        cons = getGomEnvironment().getSymbolTable().getBaseName(cons);
      }
      Production hook = `Hook(kind,cons,HookKind("block"),
          ConcArg(),code,OptionList());
      try { 
        return (GomModuleList)
          `OnceTopDown(AddHook(sort,hook)).visitLight(ml); 
      } catch (VisitFailure e) { 
        throw new RuntimeException("Should never happen"); 
      }
    }

  /* add the hook next to the declaration of the constructor's sort */
  private GomModuleList addRawConstructorBlockHook
    (GomModuleList ml, String cons, String code) {
      String sort = getGomEnvironment().getSymbolTable().getSort(cons);
      IdKind kind = `KindOperator();
      // if the constructor is a generated cons or nil, add a 'future' hook
      if(getGomEnvironment().getSymbolTable().isGeneratedCons(cons)) { 
        kind = `KindFutureOperator(FutureCons());
        cons = getGomEnvironment().getSymbolTable().getBaseName(cons);
      } else if (getGomEnvironment().getSymbolTable().isGeneratedNil(cons)) { 
        kind = `KindFutureOperator(FutureNil());
        cons = getGomEnvironment().getSymbolTable().getBaseName(cons);
      }
      Production hook = 
        `Hook(kind,getGomEnvironment().getSymbolTable().rawCons(cons),HookKind("block"),
            ConcArg(),code,OptionList());
      try { 
        return (GomModuleList)
          `OnceTopDown(AddHook(getGomEnvironment().getSymbolTable().rawCons(sort),hook)).visitLight(ml); 
      }
      catch (VisitFailure e) { 
        throw new RuntimeException("Should never happen"); 
      }
    }

  /* add the hook next to the sort declaration */
  private GomModuleList addSortInterfaceHook
    (GomModuleList ml, String sort, String code) {
      Production hook = 
        `Hook(KindSort(),sort,HookKind("interface"),
            ConcArg(),code,OptionList());
      try { 
        return (GomModuleList)
          `OnceTopDown(AddHook(sort,hook)).visitLight(ml); 
      }
      catch (VisitFailure e) { 
        throw new RuntimeException("Should never happen"); 
      }
    }

  /* add the hook next to the declaration of the constructor's sort */
  private GomModuleList addConstructorMappingHook
    (GomModuleList ml, String cons, String code) {
      String sort = getGomEnvironment().getSymbolTable().getSort(cons);
      IdKind kind = `KindOperator();
      // if the constructor is a generated cons or nil, add a 'future' hook
      if(getGomEnvironment().getSymbolTable().isGeneratedCons(cons)) { 
        kind = `KindFutureOperator(FutureCons());
        cons = getGomEnvironment().getSymbolTable().getBaseName(cons);
      } else if (getGomEnvironment().getSymbolTable().isGeneratedNil(cons)) { 
        kind = `KindFutureOperator(FutureNil());
        cons = getGomEnvironment().getSymbolTable().getBaseName(cons);
      }
      Production hook = `Hook(kind,cons,HookKind("mapping"),
          ConcArg(),code,OptionList());
      try { 
        return (GomModuleList)
          `OnceTopDown(AddHook(sort,hook)).visitLight(ml); 
      } catch (VisitFailure e) { 
        throw new RuntimeException("Should never happen"); 
      }
    }

  /* -- sort hooks -- */

  private GomModuleList addSortHooks(GomModuleList ml) {
    for(String s: getGomEnvironment().getSymbolTable().getSorts()) 
      if(getGomEnvironment().getSymbolTable().isExpressionType(s) || getGomEnvironment().getSymbolTable().isPatternType(s))
        ml = addSortBlockHook(ml,s,sortBlockHookString(s));
    return ml;
  }


  private String sortBlockHookString(String sort) {
    String alphamapargs = alphamapArgList(sort);
    String newalphamaps = newAlphamapList(sort);
    String exportmapargs = exportmapArgList(sort);
    String newexportmaps = newExportmapList(sort);
    String rawsortid = getGomEnvironment().getSymbolTable().qualifiedRawSortId(sort);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    String res = "{";
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      res += %[ 
        public abstract @sortid@ 
          rename@a@(java.util.Hashtable<@aid@,@aid@> map); 
      ]%;
    }

    res += %[
     /**
      * alpha equivalence 
      */
      public boolean equals(@sortid@ o) {
        try { 
          alpha(o @newalphamaps@); 
          return true;
        } catch (tom.library.freshgom.AlphaMap.AlphaException e) {
          return false;
        }
      }

      public abstract void alpha (@sortid@ o @alphamapargs@)
        throws tom.library.freshgom.AlphaMap.AlphaException;

     /**
      * exportation (term -> raw term) 
      */
      public @rawsortid@ export() {
        return _export(@newexportmaps@);
      }

      public abstract @rawsortid@ _export(@exportmapargs@);
    ]%;

    if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
      String newhashtables = newHashtableList(sort);
      String hashtableargs = hashtableArgList(sort);
      res += %[ 
        public @sortid@ refresh() {
          return _refresh(@newhashtables@);
        }

        public abstract @sortid@ _refresh(@hashtableargs@); 
      ]%;

      for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(sort)) {
        String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
        res += %[
          public java.util.Set<@aid@> getBound@a@() {
            java.util.HashSet<@aid@> res = new java.util.HashSet<@aid@>();
            getBound@a@(res);
            return res;
          }

          public abstract void getBound@a@(java.util.Set<@aid@> atoms); 

          public abstract void getBound@a@2(
              @sortid@ o, tom.library.freshgom.AlphaMap<@aid@> m)
              throws tom.library.freshgom.AlphaMap.AlphaException;
 
        ]%;
      }
    }

    return res + "}";
  }

  /* -- raw sort hooks -- */

  private GomModuleList addRawSortHooks(GomModuleList ml) {
    for(String s: getGomEnvironment().getSymbolTable().getSorts()) 
      if(getGomEnvironment().getSymbolTable().isExpressionType(s) || getGomEnvironment().getSymbolTable().isPatternType(s))
        ml = addSortBlockHook(ml,getGomEnvironment().getSymbolTable().rawSort(s),rawSortBlockHookString(s));
    return ml;
  }


  private String rawSortBlockHookString(String sort) {
    String convertmapargs = convertmapArgList(sort);
    String newconvertmaps = newConvertmapList(sort);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);

    String res = %[{
     /**
      * importation (raw term -> term) 
      */
      public @sortid@ convert() {
        return _convert(@newconvertmaps@);
      }

      public abstract @sortid@ _convert(@convertmapargs@);
    ]%;

    if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
      for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(sort)) {
        String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
        res += %[
          public tom.library.freshgom.ConvertMap<@aid@> getBound@a@Map() {
            tom.library.freshgom.ConvertMap<@aid@> res = 
              new tom.library.freshgom.ConvertMap<@aid@>();
            getBound@a@Map(res);
            return res;
          }

          public abstract void 
            getBound@a@Map(tom.library.freshgom.ConvertMap<@aid@> m);
        ]%;
      }
    }
    return res + "}";
  }

  /* -- non variadic constructor hooks -- */

  private GomModuleList addConstructorHooks(GomModuleList ml) {
    for(String s: getGomEnvironment().getSymbolTable().getSorts()) 
      if(getGomEnvironment().getSymbolTable().isExpressionType(s) || getGomEnvironment().getSymbolTable().isPatternType(s))
        for(String c: getGomEnvironment().getSymbolTable().getConstructors(s))
          if(! getGomEnvironment().getSymbolTable().isVariadic(c))
            ml = addConstructorBlockHook(ml,c,constructorBlockHookString(c));
    return ml;
  }

  private String renameRecursiveCalls(String c, String atomSort) {
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(getGomEnvironment().getSymbolTable().getSort(c));
    String res = %[@sortid@ res = this;]%; 
    String atomSortId = getGomEnvironment().getSymbolTable().getFullSortClassName(atomSort);
    for(String f: getGomEnvironment().getSymbolTable().getFields(c)) {
      String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
      if (getGomEnvironment().isBuiltin(fsort)) continue;
      if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
        if (fsort.equals(atomSort))
          res += %[ 
            @atomSortId@ n_@f@ = map.get(get@f@());
        if (n_@f@ != null) res = res.set@f@(n_@f@); 
        ]%;
      } else {
        if (!getGomEnvironment().getSymbolTable().getAccessibleAtoms(fsort).contains(atomSort)) continue;
        res += %[ res = res.set@f@(get@f@().rename@atomSort@(map)); ]%;
      }
    }
    return res + "return res;";
  }

  private String refreshRecursiveCalls(String c) {
    String sort = getGomEnvironment().getSymbolTable().getSort(c);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    String res = %[@sortid@ res = this;]%; 
    for(String f: getGomEnvironment().getSymbolTable().getPatternFields(c)) {
      String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
      if (getGomEnvironment().isBuiltin(fsort)) continue;
      if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
        String fsortid = getGomEnvironment().getSymbolTable().getFullSortClassName(fsort);
        res += %[
          @fsortid@ @f@ = get@f@();
          if (@fsort@Map.containsKey(@f@))
            res = res.set@f@(@fsort@Map.get(@f@));
          else {
            @fsortid@ fresh_@f@ = @fsortid@.fresh@fsort@(@f@);
            @fsort@Map.put(@f@,fresh_@f@);
            res = res.set@f@(fresh_@f@);
          }
        ]%;
      } else {
        String arglist = hashtableRecursiveCall(sort); 
        res += %[res = res.set@f@(get@f@()._refresh(@arglist@));]%;
      }
    }
    for(String f: getGomEnvironment().getSymbolTable().getInnerFields(c)) {
      String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
      if (getGomEnvironment().isBuiltin(fsort)) continue;
      for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(sort)) {
        if (getGomEnvironment().getSymbolTable().getAccessibleAtoms(fsort).contains(a))
          res += %[res = res.set@f@(res.get@f@().rename@a@(@a@Map));]%;
      }
    }
    return res + "return res;";
  }

  private String alphaRecursiveCalls(String c) {
    String sort = getGomEnvironment().getSymbolTable().getSort(c);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    String res = ""; 
    /* if c is a constructor in expression position
       the args are of the form atomsort1Map, atomsort2Map .. */
    if(getGomEnvironment().getSymbolTable().isExpressionType(sort)) {
      for(String f: getGomEnvironment().getSymbolTable().getFields(c)) {
        String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
        if (getGomEnvironment().isBuiltin(fsort)) {
          res += %[
            if (get@f@() != o.get@f@()) 
              throw new tom.library.freshgom.AlphaMap.AlphaException();
           ]%;
        } else {
          String fsortid = getGomEnvironment().getSymbolTable().getFullSortClassName(fsort);
          String rawfsortid = getGomEnvironment().getSymbolTable().qualifiedRawSortId(fsort);
          if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
            res += %[
              if (!@fsort@Map.equal(get@f@(),o.get@f@()))
                throw new tom.library.freshgom.AlphaMap.AlphaException();
            ]%;
          } else if (getGomEnvironment().getSymbolTable().isExpressionType(fsort)) {
            res += %[get@f@().alpha(o.get@f@() @alphaRecCall1(fsort)@);]%;
          } else if (getGomEnvironment().getSymbolTable().isPatternType(fsort)) {
            res += %[
              { /* to limit declared variables scope */
              @fsortid@ @f@ = get@f@();;
              @fsortid@ o_@f@ = o.get@f@();
            ]%;
            for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(fsort)) {
              String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
              res += %[
                tom.library.freshgom.AlphaMap<@aid@> @f@_bound@a@
                  = new tom.library.freshgom.AlphaMap<@aid@>();
                @f@.getBound@a@2(o_@f@, @f@_bound@a@);
                tom.library.freshgom.AlphaMap<@aid@> @a@InnerMap 
                  = @a@Map.combine(@f@_bound@a@);
              ]%;
            }
            res += %[@f@.alpha(o.get@f@() @alphaRecCall2(fsort)@);]%;
            res += %[}]%;
          }
        }
      }
    /* if c is a constructor in pattern position --
       the args are of the form 
        as1OuterMap, as1InnerMap, as2Map, as3OuterMap, as3InnerMap .. */
    } else if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
      for(String f: getGomEnvironment().getSymbolTable().getFields(c)) {
        String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
        if (getGomEnvironment().isBuiltin(fsort)) {
          res += %[
            if (get@f@() != o.get@f@()) 
              throw new tom.library.freshgom.AlphaMap.AlphaException();
          ]%;
        } else {
          String fsortid = getGomEnvironment().getSymbolTable().getFullSortClassName(fsort);
          String rawfsortid = getGomEnvironment().getSymbolTable().qualifiedRawSortId(fsort);
          if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
            if (getGomEnvironment().getSymbolTable().isBound(c,f)) 
              res += %[
                if (!@fsort@InnerMap.equal(get@f@(),o.get@f@()))
                  throw new tom.library.freshgom.AlphaMap.AlphaException();
            ]%;
                else
                  res += %[
                    if (!@fsort@Map.equal(get@f@(),o.get@f@()))
                      throw new tom.library.freshgom.AlphaMap.AlphaException();
            ]%;
          } else if (getGomEnvironment().getSymbolTable().isInner(c,f)) /* must be expression type */ {
            res += %[get@f@().alpha(o.get@f@() @alphaRecCall3(fsort,sort)@);]%;
          } else if (getGomEnvironment().getSymbolTable().isOuter(c,f)) /* must be expression type */ {
            res += %[get@f@().alpha(o.get@f@() @alphaRecCall4(fsort,sort)@);]%;
          } else if (getGomEnvironment().getSymbolTable().isNeutral(c,f)) /* must be expression type */ {
            res += %[get@f@().alpha(o.get@f@() @alphaRecCall5(fsort)@);]%;
          } else /* must be pattern type */ {
            res += %[get@f@().alpha(o.get@f@() @alphaRecCall6(fsort)@);]%;
          }
        }
      }
    }
    return res;
  }

  private String exportRecursiveCalls(String c) {
    String sort = getGomEnvironment().getSymbolTable().getSort(c);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    String res = ""; 
    /* if c is a constructor in expression position
       the args are of the form atomsort1Map, atomsort2Map .. */
    if(getGomEnvironment().getSymbolTable().isExpressionType(sort)) {
      for(String f: getGomEnvironment().getSymbolTable().getFields(c)) {
        String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
        if (getGomEnvironment().isBuiltin(fsort)) continue;
        String fsortid = getGomEnvironment().getSymbolTable().getFullSortClassName(fsort);
        String rawfsortid = getGomEnvironment().getSymbolTable().qualifiedRawSortId(fsort);
        if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
          res += %[String raw_@f@ = @fsort@Map.get(get@f@());]%;
        } else if (getGomEnvironment().getSymbolTable().isExpressionType(fsort)) {
          res += %[@rawfsortid@ raw_@f@ 
            = get@f@()._export(@recCall1(fsort)@);]%;
        } else if (getGomEnvironment().getSymbolTable().isPatternType(fsort)) {
          res += %[
            /* declaration before scoping */
            @rawfsortid@ raw_@f@ = null; ]%;
          res += %[{ /* to limit declared variables scope */]%;
          res += %[@fsortid@ @f@ = get@f@();]%;
          for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(fsort)) {
            String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
            res += %[
              java.util.Set<@aid@> @f@_bound@a@ = @f@.getBound@a@();
              tom.library.freshgom.ExportMap<@aid@> @a@InnerMap 
                = @a@Map.addSet(@f@_bound@a@);
            ]%;
          }
          res += %[raw_@f@ = @f@._export(@recCall2(fsort)@);]%;
          res += %[}]%;
        }
      }
    /* if c is a constructor in pattern position --
       the args are of the form 
        as1OuterMap, as1InnerMap, as2Map, as3OuterMap, as3InnerMap .. */
    } else if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
      for(String f: getGomEnvironment().getSymbolTable().getFields(c)) {
        String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
        if (getGomEnvironment().isBuiltin(fsort)) continue;
        String fsortid = getGomEnvironment().getSymbolTable().getFullSortClassName(fsort);
        String rawfsortid = getGomEnvironment().getSymbolTable().qualifiedRawSortId(fsort);
        if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
          if (getGomEnvironment().getSymbolTable().isBound(c,f))
            res += %[String raw_@f@ = @fsort@InnerMap.get(get@f@());]%;
          else
            res += %[String raw_@f@ = @fsort@Map.get(get@f@());]%;
        } else if (getGomEnvironment().getSymbolTable().isInner(c,f)) /* must be expression type */ {
          res += %[@rawfsortid@ raw_@f@ 
            = get@f@()._export(@recCall3(fsort,sort)@);]%;
        } else if (getGomEnvironment().getSymbolTable().isOuter(c,f)) /* must be expression type */ {
          res += %[@rawfsortid@ raw_@f@ 
            = get@f@()._export(@recCall4(fsort,sort)@);]%;
        } else if (getGomEnvironment().getSymbolTable().isNeutral(c,f)) /* must be expression type */ {
          res += %[@rawfsortid@ raw_@f@ 
            = get@f@()._export(@recCall5(fsort)@);]%;
        } else /* must be pattern type */ {
          res += %[@rawfsortid@ raw_@f@ 
            = get@f@()._export(@recCall6(fsort)@);]%;
        }
      }
    }
    return res + %[return `@getGomEnvironment().getSymbolTable().rawCons(c)@(@rawArgList(c)@);]%;
  }

  private String getBoundRecursiveCalls(String c, String atomSort) {
    String sort = getGomEnvironment().getSymbolTable().getSort(c);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    String res = ""; 
    for(String f: getGomEnvironment().getSymbolTable().getPatternFields(c)) {
      String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
      if (getGomEnvironment().isBuiltin(fsort)) continue;
      if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
        if (fsort.equals(atomSort)) {
          res += %[atoms.add(get@f@());]%;
        }
      } else if (getGomEnvironment().getSymbolTable().getBoundAtoms(fsort).contains(atomSort)) {
        res += %[get@f@().getBound@atomSort@(atoms);]%;
      }
    }
    return res;
  }

  private String getBound2RecursiveCalls(String c, String a) {
    String sort = getGomEnvironment().getSymbolTable().getSort(c);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
    String res = ""; 
    for(String f: getGomEnvironment().getSymbolTable().getPatternFields(c)) {
      String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
      if (getGomEnvironment().isBuiltin(fsort)) continue;
      if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
        if (fsort.equals(a)) {
          res += %[
            @aid@ @f@ = get@f@();
            m.put(@f@,o.get@f@(),@aid@.fresh@a@(@f@));
          ]%;
        }
      } else if (getGomEnvironment().getSymbolTable().getBoundAtoms(fsort).contains(a)) {
        res += %[get@f@().getBound@a@2(o.get@f@(),m);]%;
      }
    }
    return res;
  }

  private String constructorBlockHookString(String c) {
    String cid = getGomEnvironment().getSymbolTable().getFullConstructorClassName(c);
    String sort = getGomEnvironment().getSymbolTable().getSort(c);
    String alphamapargs = alphamapArgList(sort);
    String exportmapargs = exportmapArgList(sort);
    String rawsortid = getGomEnvironment().getSymbolTable().qualifiedRawSortId(sort);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    String res = "{";
    for(String a: getGomEnvironment().getSymbolTable().getAccessibleAtoms(sort)) {
      String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
      res += %[
        public @sortid@ rename@a@(java.util.Hashtable<@aid@,@aid@> map) {
          @renameRecursiveCalls(c,a)@
        }]%;
    }

    res += %[
      /**
       * alpha equivalence 
       */
      public void alpha (@sortid@ o @alphamapargs@) 
        throws tom.library.freshgom.AlphaMap.AlphaException {
          if (! (o instanceof @cid@))
            throw new tom.library.freshgom.AlphaMap.AlphaException();
          @alphaRecursiveCalls(c)@
        };

    /**
     * exportation (term -> raw term) 
     */
    public @rawsortid@ _export(@exportmapargs@) {
      @exportRecursiveCalls(c)@
    }
    ]%;

    if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
      res += %[ 
        public @sortid@ _refresh(@hashtableArgList(sort)@) {
          @refreshRecursiveCalls(c)@
        }
      ]%;

      for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(sort)) {
        String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
        res += %[ 
          public void getBound@a@(java.util.Set<@aid@> atoms) {
            @getBoundRecursiveCalls(c,a)@
          }

          public void getBound@a@2(
              @sortid@ o, tom.library.freshgom.AlphaMap<@aid@> m) 
              throws tom.library.freshgom.AlphaMap.AlphaException {
                if (! (o instanceof @cid@))
                  throw new tom.library.freshgom.AlphaMap.AlphaException();
                @getBound2RecursiveCalls(c,a)@
              }

        ]%;
      }
    }

    return res + "}";
  }

  /* -- non variadic raw constructor hooks -- */

  private GomModuleList addRawConstructorHooks(GomModuleList ml) {
    for(String s: getGomEnvironment().getSymbolTable().getSorts()) 
      if(getGomEnvironment().getSymbolTable().isExpressionType(s) || getGomEnvironment().getSymbolTable().isPatternType(s))
        for(String c: getGomEnvironment().getSymbolTable().getConstructors(s))
          if(! getGomEnvironment().getSymbolTable().isVariadic(c))
            ml = addRawConstructorBlockHook(
                ml,c,rawConstructorBlockHookString(c));
    return ml;
  }

  private String getBoundMapRecursiveCalls(String c, String atomSort) {
    String sort = getGomEnvironment().getSymbolTable().getSort(c);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(atomSort);
    String res = ""; 
    for(String f: getGomEnvironment().getSymbolTable().getPatternFields(c)) {
      String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
      if (getGomEnvironment().isBuiltin(fsort)) continue;
      if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
        if (fsort.equals(atomSort)) {
          res += %[
            String @f@ = @getGomEnvironment().getSymbolTable().rawGetter(c,f)@;
            m.put(@f@,@aid@.fresh@atomSort@(@f@));
          ]%;
        }
      } else if (getGomEnvironment().getSymbolTable().getBoundAtoms(fsort).contains(atomSort)) {
        res += %[@getGomEnvironment().getSymbolTable().rawGetter(c,f)@.getBound@atomSort@Map(m);]%;
      }
    }
    return res;
  }

  private String convertRecursiveCalls(String c) {
    String sort = getGomEnvironment().getSymbolTable().getSort(c);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    String res = ""; 
    /* if c is a constructor in expression position
       the args are of the form atomsort1Map, atomsort2Map .. */
    if(getGomEnvironment().getSymbolTable().isExpressionType(sort)) {
      for(String f: getGomEnvironment().getSymbolTable().getFields(c)) {
        String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
        if (getGomEnvironment().isBuiltin(fsort)) continue;
        String fsortid = getGomEnvironment().getSymbolTable().getFullSortClassName(fsort);
        String rawfsortid = getGomEnvironment().getSymbolTable().qualifiedRawSortId(fsort);
        if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
          res += %[@fsortid@ @f@ = @fsort@Map.get(@getGomEnvironment().getSymbolTable().rawGetter(c,f)@);]%;
        } else if (getGomEnvironment().getSymbolTable().isExpressionType(fsort)) {
          res += %[@fsortid@ @f@ 
            = @getGomEnvironment().getSymbolTable().rawGetter(c,f)@._convert(@recCall1(fsort)@);]%;
        } else if (getGomEnvironment().getSymbolTable().isPatternType(fsort)) {
          res += %[
            /* declaration before scoping */
            @fsortid@ @f@ = null;
          ]%;
          res += %[{ /* to limit declared variables scope */]%;
          res += %[@rawfsortid@ raw_@f@ = @getGomEnvironment().getSymbolTable().rawGetter(c,f)@;]%;
          for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(fsort)) {
            String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
            res += %[
              tom.library.freshgom.ConvertMap<@aid@> raw_@f@_bound@a@ 
                = raw_@f@.getBound@a@Map();
              tom.library.freshgom.ConvertMap<@aid@> @a@InnerMap 
                = @a@Map.combine(raw_@f@_bound@a@);
            ]%;
          }
          res += %[@f@ = raw_@f@._convert(@recCall2(fsort)@);]%;
          res += %[}]%;
        }
      }
    /* if c is a constructor in pattern position --
       the args are of the form 
        as1OuterMap, as1InnerMap, as2Map, as3OuterMap, as3InnerMap .. */
    } else if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
      for(String f: getGomEnvironment().getSymbolTable().getFields(c)) {
        String fsort = getGomEnvironment().getSymbolTable().getSort(c,f);
        if (getGomEnvironment().isBuiltin(fsort)) continue;
        String fsortid = getGomEnvironment().getSymbolTable().getFullSortClassName(fsort);
        if (getGomEnvironment().getSymbolTable().isAtomType(fsort)) {
          if (getGomEnvironment().getSymbolTable().isBound(c,f))
            res += %[@fsortid@ @f@ = @fsort@InnerMap.get(@getGomEnvironment().getSymbolTable().rawGetter(c,f)@);]%;
          else
            res += %[@fsortid@ @f@ = @fsort@Map.get(@getGomEnvironment().getSymbolTable().rawGetter(c,f)@);]%;
        } else if (getGomEnvironment().getSymbolTable().isInner(c,f)) /* must be expression type */ {
          res += %[@fsortid@ @f@ 
            = @getGomEnvironment().getSymbolTable().rawGetter(c,f)@._convert(@recCall3(fsort,sort)@);]%;
        } else if (getGomEnvironment().getSymbolTable().isOuter(c,f)) /* must be expression type */ {
          res += %[@fsortid@ @f@ 
            = @getGomEnvironment().getSymbolTable().rawGetter(c,f)@._convert(@recCall4(fsort,sort)@);]%;
        } else if (getGomEnvironment().getSymbolTable().isNeutral(c,f)) /* must be expression type */ {
          res += %[@fsortid@ @f@ 
            = @getGomEnvironment().getSymbolTable().rawGetter(c,f)@._convert(@recCall5(fsort)@);]%;
        } else /* must be pattern type */ {
          res += %[@fsortid@ @f@ 
            = @getGomEnvironment().getSymbolTable().rawGetter(c,f)@._convert(@recCall6(fsort)@);]%;
        }
      }
    }
    return res + %[return `@c@(@convertArgList(c)@);]%;
  }

  private String rawConstructorBlockHookString(String c) {
    String sort = getGomEnvironment().getSymbolTable().getSort(c);
    String convertmapargs = convertmapArgList(sort);
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);

    String res = %[{
      /**
       * importation (raw term -> term) 
       */
      public @sortid@ _convert(@convertmapargs@) {
        @convertRecursiveCalls(c)@
      }
    ]%;

    if(getGomEnvironment().getSymbolTable().isPatternType(sort)) {
      for(String a: getGomEnvironment().getSymbolTable().getBoundAtoms(sort)) {
        String aid = getGomEnvironment().getSymbolTable().getFullSortClassName(a);
        res += %[
          public void 
            getBound@a@Map(tom.library.freshgom.ConvertMap<@aid@> m) {
              @getBoundMapRecursiveCalls(c,a)@;
            }
        ]%;
      }
    }

    return res + "}";
  }

  /* -- atom hooks -- */

  private GomModuleList addAtomHooks(GomModuleList ml) {
    for(String s: getGomEnvironment().getSymbolTable().getAtoms()) {
      ml = addSortBlockHook(ml,s,atomBlockHookString(s));
      ml = addSortInterfaceHook(ml,s,atomInterfaceHookString);
    }
    return ml;
  }

  private String atomBlockHookString(String sort) {
    String sortid = getGomEnvironment().getSymbolTable().getFullSortClassName(sort);
    return %[{
      private static int counter = 0;

      public static @sortid@ 
        fresh@sort@(@sortid@ hint) { 
          return fresh@sort@(hint.gethint()); 
        }

      public static @sortid@ 
        fresh@sort@(String hint) { 
          return `@sort@(++counter,hint.split("[0-9]")[0]); 
        }

      public boolean equals(@sort@ o) {
        return this.getn() == o.getn();
      }

      public String getRepresentation(int n) {
        return gethint() + (n==0 ? "" : n);
      }
    }]%;
  }

  private static final String atomInterfaceHookString 
    = "{ tom.library.freshgom.Atom }";

  /* -- rawification -- */

  private GomModuleList addRawSortsAndConstructors(GomModuleList res) {
    try { return (GomModuleList) `TopDown(AddRaw(getGomEnvironment().getSymbolTable(),this)).visitLight(res); }
    catch(VisitFailure e) { 
      throw new RuntimeException("should never happen"); 
    }
  }

  %strategy AddRaw(st:SymbolTable,fe:FreshExpander) extends Identity() {
    visit Grammar {
      g@Grammar[ProductionList=pl] -> { 
        return `g.setProductionList(fe.addRaw(st,`pl)); 
      }
    }
  }

  /**
   * match pl with
   *  | p::ps -> if fresh p && (not atom p) 
   *             then p::(rawify p)::(addraw pl) 
   *             else p::(addraw pl)
   *  | [] -> []
   **/
  private ProductionList addRaw(SymbolTable st, ProductionList pl) {
    %match(pl) {
      ConsConcProduction(p,ps) -> {
        ProductionList nps = addRaw(st,`ps);
        %match(p) {
          SortType[Type=GomType[Name=n]] -> {
            if (st.isFreshType(`n) && !st.isAtomType(`n)) 
              return `ConcProduction(p,rawify(p,st),nps*);
          }
        }
        return `ConcProduction(p,nps*);
      }
      e@EmptyConcProduction() -> { return `e; }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  %strategy Rawify(st:SymbolTable,fe:FreshExpander) extends Identity() {
    visit GomType { 
      gt@GomType[Name=n] -> { 
        if (st.isAtomType(`n)) return `gt.setName("String");
        else if (!fe.getGomEnvironment().isBuiltin(`n)) return `gt.setName(st.rawSort(`n));
      }
    }
    visit Production { 
      p@Production[Name=n] -> { return `p.setName(st.rawCons(`n)); }
    }
  }

  private Production rawify(Production p, SymbolTable st) {
    try { return (Production) `TopDown(Rawify(st,this)).visitLight(p); }
    catch(VisitFailure f) { 
      throw new RuntimeException("should never happen"); 
    }
  }

  /* -- tweaked mappings generation */

  private GomModuleList addMappingHooks(GomModuleList ml) {
    for(String c: getGomEnvironment().getSymbolTable().getFreshConstructors()) 
      if(getGomEnvironment().getSymbolTable().containsRefreshPoint(c)) 
        ml = addConstructorMappingHook(ml,c,mappingString(c));
    return ml;
  }


  /**
  * generates "x:A,y:B" for f(x:A,y:B)
  **/
  private String typedConsArgList(String c) {
    StringBuffer buf = new StringBuffer();
    boolean first=true;
    for(String arg: getGomEnvironment().getSymbolTable().getFields(c)) {
      if (!first) buf.append(",");
      else first = false;
      buf.append(arg+":"+getGomEnvironment().getSymbolTable().getSort(c,arg));
    }
    return buf.toString();
  }

  /**
  * generates "x,y" for f(x:A,y:B)
  **/
  private String consArgList(String c) {
    StringBuffer buf = new StringBuffer();
    boolean first=true;
    for(String arg: getGomEnvironment().getSymbolTable().getFields(c)) {
      if (!first) buf.append(",");
      else first = false;
      buf.append(arg);
    }
    return buf.toString();
  }

  /**
  * generates "$x,$y" for f(x:A,y:B)
  **/
  private String dollarConsArgList(String c) {
    StringBuffer buf = new StringBuffer();
    boolean first=true;
    for(String arg: getGomEnvironment().getSymbolTable().getFields(c)) {
      if (!first) buf.append(",");
      else first = false;
      buf.append("$" + arg);
    }
    return buf.toString();
  }

  /**
  * generates "get_slot(x,t) { $t.getx() }
  *            get_slot(y,t) { $t.gety() }"
  * for f(x:A,y:B)
  **/
  private String slotDescriptions(String c) {
    StringBuffer buf = new StringBuffer();
    boolean first=true;
    for(String arg: getGomEnvironment().getSymbolTable().getFields(c)) {
      if(getGomEnvironment().getSymbolTable().isRefreshPoint(c,arg))
        buf.append(%[get_slot(@arg@,t) { $t.get@arg@().refresh() }]% + "\n");
      else
        buf.append(%[get_slot(@arg@,t) { $t.get@arg@() }]% + "\n");
    }
    return buf.toString();
  }

  private String mappingString(String c) {
    String s = getGomEnvironment().getSymbolTable().getSort(c);
    if (getGomEnvironment().getSymbolTable().isVariadic(c)) {
      String codomain = getGomEnvironment().getSymbolTable().getCoDomain(c);
      String domain = getGomEnvironment().getSymbolTable().getDomain(c);
      String consid = getGomEnvironment().getSymbolTable().getFullConstructorClassName("Cons" + c);
      String nilid = getGomEnvironment().getSymbolTable().getFullConstructorClassName("Empty" + c);
      String gethead = null;
      if(getGomEnvironment().getSymbolTable().isRefreshPoint(c)) gethead = %[getHead@c@().refresh()]%;
      else gethead = %[getHead@c@()]%;
      return %[{
        %oplist @codomain@ @c@(@domain@*) {
          is_fsym(t) { (($t instanceof @consid@) || ($t instanceof @nilid@)) }
          make_empty() { @nilid@.make() }
          make_insert(e,l) { @consid@.make($e,$l) }
          get_head(l) { $l.@gethead@ }
          get_tail(l) { $l.getTail@c@() }
          is_empty(l) { $l.isEmpty@c@() }
        }
      }]%;
    } else {
      String cid = getGomEnvironment().getSymbolTable().getFullConstructorClassName(c);
      return %[{
        %op @s@ @c@(@typedConsArgList(c)@) {
          is_fsym(t) { ($t instanceof @cid@) }
          @slotDescriptions(c)@
          make(@consArgList(c)@) { @cid@.make(@dollarConsArgList(c)@) }
       }
      }]%;
    }
  }

  /* -- atom expansion --**/

  %strategy ExpandAtoms(list:ArrayList) extends Identity() {
    visit Production {
      AtomDecl[ Name=name ] -> {
        list.add(`name);
        return `SortType(
            GomType(AtomType(),name),
            ConcAtom(),
            ConcProduction(Production(
                name,
                ConcField(
                  NamedField(None(),"n",GomType(ExpressionType(),"int")),
                  NamedField(None(),"hint",GomType(ExpressionType(),"String"))),
                GomType(AtomType(),name),Origin(-1)))); 
      }
    }
  }

  /* -- update expressiontype -> atomtype for atom sorts --**/

  %strategy UpdateSpecialization(list:ArrayList) extends Identity() {
    visit GomType {
      type@GomType[Name=name] -> {
        if (list.contains(`name)) {
          return `type.setSpecialization(`AtomType());
        }
      }
    }
  }
}
