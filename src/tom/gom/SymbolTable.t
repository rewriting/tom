/*
 * Gom
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

package tom.gom;

import tom.gom.adt.symboltable.types.*;
import tom.gom.adt.symboltable.*;
import tom.gom.adt.gom.types.*;
import tom.library.sl.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import tom.gom.tools.GomEnvironment;

public class SymbolTable {

  %include { adt/gom/Gom.tom }
  %include { adt/symboltable/SymbolTable.tom }
  %include { ../library/mapping/java/sl.tom }

  /** map sort-name -> SortDescription */
  private Map<String,SortDescription> sorts =
    new HashMap<String,SortDescription>();

  /** map constructor-name -> ConstructorDescription */
  private Map<String,ConstructorDescription> constructors =
    new HashMap<String,ConstructorDescription>();

  private Graph<String> sortDependences = new Graph<String>();

  private GomEnvironment gomEnvironment;

  public SymbolTable(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void fill(GomModuleList gml) {
    %match(gml) {
      ConcGomModule(_*,m,_*) -> { `fillFromGomModule(m); }
    }
    computeSortDependences();
    isolateFreshSorts();
    fillAccessibleAtoms();
    generateConsAndNils();
  }

  public void addConstructor(String symbol, String codomain, FieldDescriptionList fields) {
    constructors.put(symbol,`ConstructorDescription(codomain,fields,No()));
    SortDescription s = sorts.get(codomain);
    StringList l = s.getConstructors();
    sorts.put(codomain,s.setConstructors(`StringList(l*,symbol)));
  }

  public void addVariadicConstructor(String symbol, String domain, String codomain) {
    constructors.put(symbol,`VariadicConstructorDescription(codomain,domain,false));
    SortDescription s = sorts.get(codomain);
    StringList l = s.getConstructors();
    sorts.put(codomain,s.setConstructors(`StringList(l*,symbol)));
  }

  /**
   * returns the set of all constructor symbols
   **/
  public Set<String> getConstructors() {
    return new HashSet<String>(constructors.keySet());
  }

  /**
   * returns the set of all sort symbols
   **/
  public Set<String> getSorts() {
    return new HashSet<String>(sorts.keySet());
  }

  /**
   * clear all entries (sorts and constructors)
   */
  public void clear() {
    sorts.clear();
    constructors.clear();
  }

  public String getFullModuleName(String moduleName) {
    String packageName = gomEnvironment.getStreamManager().getPackagePath(moduleName);
    return (packageName.equals("") ? "" : packageName + ".")+moduleName.toLowerCase();
  }

  public String getFullAbstractTypeClassName(String moduleName) {
    return getFullModuleName(moduleName)+"."+moduleName+"AbstractType";
  }

  public String getFullSortClassName(String sort) {
    SortDescription desc = sorts.get(sort);
    if (null == desc) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException,
          sort);
      return null;
    }
    %match(desc) {
      SortDescription[ModuleSymbol=m] -> {
        String packageName = gomEnvironment.getStreamManager().getPackagePath(`m);
        return (packageName.equals("") ? "" : packageName + ".")
          + `m.toLowerCase() + ".types." + sort;
      }
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.nonExhaustiveMatch);
    return null;
  }

  public String getFullConstructorClassName(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    if (null == desc) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException, cons);
      return null;
    }
    %match(desc) {
      ConstructorDescription[SortSymbol=s] -> {
        return getFullSortClassName(`s).toLowerCase() + "." + cons;
      }
       VariadicConstructorDescription[SortSymbol=s] -> {
        return getFullSortClassName(`s).toLowerCase() + "." + cons;
      }
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.nonExhaustiveMatch);
    return null;
  }

  public void generateConsAndNils() {
    for(String c: getConstructors()) {
      if(!isVariadic(c)) { continue; }
      String dom = getDomain(c);
      String codom = getCoDomain(c);
      FieldDescriptionList ConsFields = null; // head and tail
      FieldDescriptionList NilFields = `concFieldDescription();
      /* codom binds X = ... | c(dom*) | ...
         => codom binds X = ... | Consc(Headc:dom,Tailc:codom) | ... */
      if(isPatternType(codom)) {
        ConsFields = `concFieldDescription(
            FieldDescription("Head"+c,dom,SPattern()),
            FieldDescription("Tail"+c,codom,SPattern()));
        /* codom = ... | c(<dom>*) | ...
           => codom = ... | Consc(Headc:<dom>,Tailc:codom) | ... */
      } else if(isRefreshPoint(c)) {
        ConsFields = `concFieldDescription(
            FieldDescription("Head"+c,dom,SRefreshPoint()),
            FieldDescription("Tail"+c,codom,SNone()));
        /* codom = ... | c(dom*) | ...
           => codom = ... | Consc(Headc:dom,Tailc:codom) | ... */
      } else {
        ConsFields = `concFieldDescription(
            FieldDescription("Head"+c,dom,SNone()),
            FieldDescription("Tail"+c,codom,SNone()));
      }

      // add the new constructors to constructors map
      String nilc = "Empty" + c;
      String consc = "Cons" + c;
      constructors.put(nilc,
          `ConstructorDescription(codom,NilFields,GenNil(c)));
      constructors.put(consc,
          `ConstructorDescription(codom,ConsFields,GenCons(c)));

      // modify the codomain description
      SortDescription sd = sorts.get(codom);
      StringList conslist = sd.getConstructors();
      conslist = `StringList(conslist*,nilc,consc);
      sd = sd.setConstructors(conslist);
      sorts.put(codom,sd);
    }
  }

  public boolean isVariadic(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      %match(desc) { VariadicConstructorDescription[] -> { return true; } }
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException,
          cons);
      return false;
    }
  }

  public boolean isGenerated(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      %match(desc) {
        ConstructorDescription[Generated=!No()] -> {
          return `true;
        }
      }
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException,
          cons);
      return false;
    }
  }

  public boolean isGeneratedCons(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      %match(desc) {
        ConstructorDescription[Generated=GenCons[]] -> {
          return `true;
        }
      }
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException,
          cons);
      return false;
    }
  }

  public boolean isGeneratedNil(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      %match(desc) {
        ConstructorDescription[Generated=GenNil[]] -> {
          return `true;
        }
      }
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException, cons);
      return false;
    }
  }

  public String toString() {
    StringBuffer buf = new StringBuffer();
    for (Map.Entry<String,SortDescription> e: sorts.entrySet()) {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try {
        tom.library.utils.Viewer.toTree(e.getValue(),swriter);
      } catch (java.io.IOException ex) {
        ex.printStackTrace();
      }
      buf.append("sort " + e.getKey() + ":\n" + swriter + "\n");
    }
    for (Map.Entry<String,ConstructorDescription> e: constructors.entrySet()) {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try {
        tom.library.utils.Viewer.toTree(e.getValue(),swriter);
      } catch(java.io.IOException ex) {
        ex.printStackTrace();
      }
      buf.append("constructor " + e.getKey() + ":\n" + swriter + "\n");
    }
    return buf.toString();
  }

  private void fillFromGomModule(GomModule m) {
    %match(m) {
      GomModule[ModuleName=GomModuleName[Name=n],
        SectionList=ConcSection(_*,Public[ProductionList=ConcProduction(_*,p,_*)],_*)] -> {
          `fillFromProduction(n,p);
        }
    }
  }

  private static StringList getConstructors(AlternativeList al) {
    StringList res = `StringList();
    %match(al) {
      ConcAlternative(_*,Alternative[Name=n],_*) -> {
        res = `ConsStringList(n,res);
      }
    }
    return res;
  }

  /* Methods only used by freshgom */

  /**
   * returns only sorts concerned by freshGom
   */
  public Set<String> getFreshSorts() {
    Set<String> res = getSorts();
    Iterator<String> it = res.iterator();
    while (it.hasNext()) {
      if (!isFreshType(it.next())) {
        it.remove();
      }
    }
    return res;
  }


  /**
   * returns the set of all constructor symbols
   * concerned by freshgom
   **/
  public Set<String> getFreshConstructors() {
    HashSet<String> res = new HashSet<String>();
    for (String c: constructors.keySet()) {
      if (isFreshType(getSort(c))) {
        res.add(c);
      }
    }
    return res;
  }


  public static String rawSort(String s) {
    return "Raw" + s;
  }

  public String rawCons(String c) {
    if (isGenerated(c)) {
      %match(getGenerated(c)) {
        GenCons(suffix) ->  { return "ConsRaw" + `suffix; }
        GenNil(suffix) -> { return "EmptyRaw" + `suffix; }
      }
    }
    return "Raw" + c;
  }

  public String qualifiedRawSortId(String sort) {
    SortDescription desc = sorts.get(sort);
    if (null == desc) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return null;
    }
    %match(desc) {
      SortDescription[ModuleSymbol=m] -> {
        String packageName = gomEnvironment.getStreamManager().getPackagePath(`m);
        return (packageName.equals("") ? "" : packageName + ".")
          + `m.toLowerCase() + ".types." + rawSort(sort);
      }
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.nonExhaustiveMatch);
    return null;
  }

  public String qualifiedRawConstructorId(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    if (null == desc) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException, cons);
      return null;
    }
    %match(desc) {
      ConstructorDescription[SortSymbol=s] -> {
        return qualifiedRawSortId(`s).toLowerCase() + "." + `rawCons(cons);
      }
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.nonExhaustiveMatch);
    return null;
  }

  private static StringList convertBoundAtoms(AtomList al) {
    StringList res = `StringList();
    %match(al) {
      ConcAtom(_*,s,_*) -> { res = `ConsStringList(s,res); }
    }
    return res;
  }

  private void fillFromProduction(String moduleName, Production p) {
    %match(p) {
      SortType[Type=GomType[Specialization=spe,Name=n],
        Binds=boundAtoms,AlternativeList=al] -> {
          // filling sorts (except AccessibleAtoms)
          StringList cons = `getConstructors(al);
          StringList bound = `convertBoundAtoms(boundAtoms);
          StringList empty = `StringList();
          FreshSortInfo info = null;
          %match(spe) {
            ExpressionType[] -> { info = `ExpressionTypeInfo(empty); }
            PatternType[] -> { info = `PatternTypeInfo(bound,empty); }
            AtomType[] -> { info = `AtomTypeInfo(); }
          }
          sorts.put(`n,`SortDescription(cons,moduleName,info));
          // filling constructors
          %match(al) {
            ConcAlternative(_*,alt,_*) -> { `fillCons(n,alt); }
          }
        }
    }
  }

  private void fillCons(String codom, Alternative p)  {
    %match(p) {
      Alternative[Name=n,DomainList=dl] -> {
        %match(dl) {
          ConcField(StarredField[
              FieldType=GomType[Name=dom],
              Specifier=spec]) -> {
            constructors.put(`n,
                `VariadicConstructorDescription(codom,dom,spec.isRefresh()));
            return;
          }
          _ /* not variadic */ -> {
            FieldDescriptionList fl = `getFieldList(codom,dl);
            constructors.put(`n,`ConstructorDescription(codom,fl,No()));
          }
        }
      }
    }
  }

  private FieldDescriptionList getFieldList(String codom, FieldList dl) {
    FieldDescriptionList res = `concFieldDescription();
    %match(dl) {
      ConcField(_*,
          NamedField[Specifier=spe,Name=fn,FieldType=GomType[Name=ty]],
          _*) -> {
        Status st = null;
        %match(spe) {
          Outer[] -> { st = `SOuter(); }
          Inner[] -> { st = `SInner(); }
          Neutral[] -> { st = `SNeutral(); }
          None[] -> { st = isPatternType(codom) ? `SPattern() : `SNone(); }
          Refresh[] -> { st = `SRefreshPoint(); }
        }
        FieldDescription desc = `FieldDescription(fn,ty,st);
        res = `concFieldDescription(res*,desc);
      }
    }
    return res;
  }

  private void setAccessibleAtoms(String sort, StringList atoms) {
    SortDescription desc = sorts.get(sort);
    SortDescription ndesc = null;
    %match(desc) {
      SortDescription[FreshInfo=i] -> {
        ndesc = desc.setFreshInfo(`i.setAccessibleAtoms(atoms));
      }
    }
    sorts.put(sort,ndesc);
  }

  private void setFreshSortInfo(String sort, FreshSortInfo i) {
    SortDescription desc = sorts.get(sort);
    sorts.put(sort,desc.setFreshInfo(i));
  }

  private GenerationInfo getGenerated(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    return desc.getGenerated();
  }

  /**
   * precondition : isGenerated(cons)
   */
  public String getBaseName(String cons) {
    return getGenerated(cons).getBaseName();
  }

  public boolean isExpressionType(String sort) {
    if (getGomEnvironment().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      %match(i) { ExpressionTypeInfo[] -> { return true; } }
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return false;
    }
  }

  public boolean isPatternType(String sort) {
    if (getGomEnvironment().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      %match(i) { PatternTypeInfo[] -> { return true; } }
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return false;
    }
  }

  public boolean isAtomType(String sort) {
    if (getGomEnvironment().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      %match(i) { AtomTypeInfo[] -> { return true; } }
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return false;
    }
  }

  /**
   * true if sort is concerned by freshgom
   **/
  public boolean isFreshType(String sort) {
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      %match(i) { !NoFreshSort[] -> { return true; } }
      return false;
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredSortException, sort);
      return false;
    }
  }

  %typeterm SymbolTable { implement { tom.gom.SymbolTable } }
  %strategy SetRefreshPoints(st:SymbolTable,codom:String) extends Identity() {
    visit FieldDescription {
      fd@FieldDescription[Sort=ty] -> {
        if(st.isExpressionType(codom) && st.isPatternType(`ty)) {
          return `fd.setStatusValue(`SRefreshPoint());
        }
      }
    }
    visit ConstructorDescription {
      vd@VariadicConstructorDescription[Domain=ty] -> {
        if(st.isExpressionType(codom) && st.isPatternType(`ty)) {
          return `vd.setIsRefreshPoint(true);
        }
      }
    }
  }

  public tom.gom.adt.symboltable.types.stringlist.StringList
    getConstructors(String sort) {
      return (tom.gom.adt.symboltable.types.stringlist.StringList)
        sorts.get(sort).getConstructors();
    }

  public String getSort(String constructor) {
    try {
      return constructors.get(constructor).getSortSymbol();
    } catch(NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException, constructor);
      return null;
    }
  }

  public String getChildSort(String constructor,int omega) {
    try {
      ConstructorDescription c = constructors.get(constructor);
      int count=1;

      %match(c) {
        VariadicConstructorDescription[Domain=Domain] -> {
          return `Domain;
        }

        ConstructorDescription[Fields=concFieldDescription(_*,FieldDescription[Sort=Sort],_*)] -> {
          if (count==omega) {
            return `Sort;
          } else {
            count++;
          }
        }
      }
    } catch (NullPointerException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.undeclaredConstructorException,
          constructor);
      return null;
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.cannotAccessToChildConstructor,
        new Object[] {omega,constructor});
    return null;
  }

  public tom.gom.adt.symboltable.types.stringlist.StringList
    getAccessibleAtoms(String sort) {
      try {
        return (tom.gom.adt.symboltable.types.stringlist.StringList)
          sorts.get(sort).getFreshInfo().getAccessibleAtoms();
      } catch(NullPointerException e) {
        GomMessage.error(getLogger(),null,0,
            GomMessage.undeclaredSortException, sort);
        return null;
      }
    }

  public tom.gom.adt.symboltable.types.stringlist.StringList getBoundAtoms(String sort) {
    return (tom.gom.adt.symboltable.types.stringlist.StringList)
      sorts.get(sort).getFreshInfo().getBoundAtoms();
  }

  private FieldDescriptionList getFieldList(String constructor) {
    return constructors.get(constructor).getFields();
  }

  public List<String> getFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    %match(l) {
      concFieldDescription(_*,FieldDescription[FieldName=n],_*) -> {
        result.add(`n);
      }
    }
    return result;
  }

  public List<String> getNeutralFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SNeutral()],_*) -> {
        result.add(`n);
      }
    }
    return result;
  }

  public List<String> getPatternFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SPattern()],_*) -> {
        result.add(`n);
      }
    }
    return result;
  }

  /**
   * generates 'getfield()' except for HeadC and TailC
   * where it generates 'getHeadRawC()' and 'getTailRawC()'
   **/
  public String rawGetter(String cons, String field) {
    if (isGenerated(cons)) {
      String suffix = getBaseName(cons);
      if (field.startsWith("Head")) {
        return "getHeadRaw" + suffix + "()";
      } else if (field.startsWith("Tail")) {
        return "getTailRaw" + suffix + "()";
      }
    }
    return "get" + field + "()";
  }

  public List<String> getNonPatternFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=!SPattern()],_*) -> {
        result.add(`n);
      }
    }
    return result;
  }

  public List<String> getOuterFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SOuter()],_*) -> {
        result.add(`n);
      }
    }
    return result;
  }

  public List<String> getInnerFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SInner()],_*) -> {
        result.add(`n);
      }
    }
    return result;
  }

  public String getSort(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    %match(l) {
      concFieldDescription(_*,FieldDescription[FieldName=n,Sort=sort],_*) -> {
        if (`n.equals(field)) { return `sort; }
      }
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.shouldNeverHappen,
        new Object[]{cons,field});
    return null;
  }

  public boolean containsRefreshPoint(String cons) {
    ConstructorDescription d = constructors.get(cons);
    %match(d) {
      VariadicConstructorDescription[IsRefreshPoint=rp] -> {
        return `rp;
      }
      ConstructorDescription[Fields=
        concFieldDescription(_*,
            FieldDescription[StatusValue=SRefreshPoint()],_*)] -> {
          return true;
        }
    }
    return false;
  }

  public boolean isOuter(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SOuter()],_*) -> {
        if (`n.equals(field)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isPattern(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SPattern()],_*) -> {
        if (`n.equals(field)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isInner(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SInner()],_*) -> {
        if (`n.equals(field)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isNeutral(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SNeutral()],_*) -> {
        if(`n.equals(field)) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * for variadic operators
   */
  public boolean isRefreshPoint(String cons) {
    ConstructorDescription d = constructors.get(cons);
    %match(d) {
      VariadicConstructorDescription[IsRefreshPoint=r] -> {
        return `r;
      }
    }
    return false;
  }

  /**
   * for variadic operators
   */
  public String getDomain(String cons) {
    ConstructorDescription d = constructors.get(cons);
    %match(d) {
      VariadicConstructorDescription[Domain=dom] -> { return `dom; }
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.nonVariadicOperator);
    return null;
  }

  /**
   * for variadic operators
   */
  public String getCoDomain(String cons) {
    ConstructorDescription d = constructors.get(cons);
    %match(d) {
      VariadicConstructorDescription[SortSymbol=s] -> { return `s; }
    }
    GomMessage.error(getLogger(),null,0,
        GomMessage.nonVariadicOperator);
    return null;
  }


  public boolean isRefreshPoint(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SRefreshPoint()],_*) -> {
        if (`n.equals(field)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isBound(String cons, String field) {
    String sort = getSort(cons);
    return getBoundAtoms(sort).contains(getSort(cons,field));
  }


  public Set<String> getAtoms() {
    Set<String> res = getSorts();
    Iterator<String> it = res.iterator();
    while (it.hasNext()) {
      if (!isAtomType(it.next())) {
        it.remove();
      }
    }
    return res;
  }

  private StringList getAccessibleAtoms
    (String sort, HashSet<String> visited)  {
      if(getGomEnvironment().isBuiltin(sort) || visited.contains(sort)) {
        return `StringList();
      }
      visited.add(sort);
      if(isAtomType(sort)) {
        return `StringList(sort);
      }
      StringList res = `StringList();
      for(String c: getConstructors(sort)) {
        ConstructorDescription cd = constructors.get(c);
        %match(cd) {
          VariadicConstructorDescription[Domain=ty] -> {
            StringList tyatoms = getAccessibleAtoms(`ty,visited);
            res = `StringList(tyatoms*,res*);
          }
          ConstructorDescription[
            Fields=concFieldDescription(_*,FieldDescription[Sort=ty],_*)] -> {
              StringList tyatoms = getAccessibleAtoms(`ty,visited);
              res = `StringList(tyatoms*,res*);
            }
        }
      }
      return res;
    }

  private void fillAccessibleAtoms() {
    for(String s: getSorts()) {
      if(isExpressionType(s) || isPatternType(s)) {
        StringList atoms = getAccessibleAtoms(s,new HashSet<String>());
        setAccessibleAtoms(s,atoms);
      }
    }
  }

  private void computeSortDependences() {
    sortDependences.clear();
    for(String sort: getSorts()) {
      if (getGomEnvironment().isBuiltin(sort)) { continue; }
      for(String c: getConstructors(sort)) {
        ConstructorDescription cd = constructors.get(c);
        %match(cd) {
          VariadicConstructorDescription[Domain=ty] -> {
            if(!getGomEnvironment().isBuiltin(`ty)) {
              sortDependences.addLink(sort,`ty);
            }
          }
          ConstructorDescription[
            Fields=concFieldDescription(_*,FieldDescription[Sort=ty],_*)] -> {
              if(!getGomEnvironment().isBuiltin(`ty)) {
                sortDependences.addLink(sort,`ty);
              }
            }
        }
      }
    }
  }

  /**
   * sets sorts that are not connected to any atom at NoFreshSort
   **/
  private void isolateFreshSorts() {
    Set<String> atoms = getAtoms();
    Set<String> sorts = getSorts();
    Set<String> connected = sortDependences.connected(atoms);
    sorts.removeAll(connected);
    for(String s: sorts) {
      setFreshSortInfo(s,`NoFreshSort());
    }
  }

  public Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
