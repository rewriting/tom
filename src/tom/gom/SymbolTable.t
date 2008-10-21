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

package tom.gom;

import java.util.logging.Level;
import tom.gom.GomStreamManager;
import tom.gom.adt.symboltable.types.*;
import tom.gom.adt.symboltable.*;
import tom.gom.adt.gom.types.*;
import tom.library.sl.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import tom.gom.tools.GomEnvironment;

public class SymbolTable {

  %include { adt/gom/Gom.tom}
  %include { adt/symboltable/SymbolTable.tom}
  %include { util/ArrayList.tom }
  %include { sl.tom }  


  /** map sort-name -> SortDescription */
  private Hashtable<String,SortDescription> sorts = 
    new Hashtable<String,SortDescription>();

  /** map constructor-name -> ConstructorDescription */
  private Hashtable<String,ConstructorDescription> constructors =  
    new Hashtable<String,ConstructorDescription>();
  
  protected String packageName = "";

  private Graph<String> sortDependences = new Graph<String>();
  
  public void fill(GomModuleList gml) {
    %match(gml) {
      ConcGomModule(_*,m,_*) -> { `fillFromGomModule(m); }
    }
    computeSortDependences();
    isolateFreshSorts();
    //System.out.println("sorts concerned by freshgom: " + getFreshSorts());
    //fillRefreshPoints();
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
    return new HashSet(constructors.keySet());
  }

  /**
   * returns the set of all sort symbols
   **/
  public Set<String> getSorts() {
    return new HashSet(sorts.keySet());
  }

  /**
   * clear all entries (sorts and constructors)
   */
  public void clear() {
    sorts.clear();
    constructors.clear();
  }

  public String getFullSortClassName(String sort) {
    SortDescription desc = sorts.get(sort);
    if(desc==null) {
      throw new UndeclaredSortException(sort);
    }
    %match(desc) {
      SortDescription[ModuleSymbol=m] -> { 
        return (packageName.equals("") ? "" : packageName + ".") 
          + `m.toLowerCase() + ".types." + sort; 
      }
    }
    throw new RuntimeException("non exhaustive match");
  }

  public String getFullConstructorClassName(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    if(desc==null) {
      throw new UndeclaredConstructorException(cons);
    }
    %match(desc) {
      ConstructorDescription[SortSymbol=s] -> {
        return getFullSortClassName(`s).toLowerCase() + "." + cons; 
      }
    }
    throw new RuntimeException("non exhaustive match");
  }

  public boolean isBuiltin(String sort) {
    %match(sort) {
      "boolean" | "String" | "int" -> { return true; }
    }
    return false;
  }

  public void setPackage(String packageName) {
    this.packageName = packageName;
  }

  public void generateConsAndNils() {
    for(String c: getConstructors()) {
      if(!isVariadic(c)) continue;
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
      throw new UndeclaredConstructorException(cons);
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
      throw new UndeclaredConstructorException(cons);
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
      throw new UndeclaredConstructorException(cons);
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
      throw new UndeclaredConstructorException(cons);
    }
  }

  public String toString() {
    StringBuffer buf = new StringBuffer();
    for(Map.Entry<String,SortDescription> e: sorts.entrySet()) {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try {
        tom.library.utils.Viewer.toTree(e.getValue(),swriter); 
      } catch(java.io.IOException ex) {
        ex.printStackTrace(); 
      }
      buf.append("sort " + e.getKey() + ":\n" + swriter + "\n");
    }
    for(Map.Entry<String,ConstructorDescription> e: constructors.entrySet()) {
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
        SectionList=(_*,Public[GrammarList=(_*,g,_*)],_*)] -> {
          `fillFromGrammar(n,g);
        }
    }
  }

  private void fillFromGrammar(String moduleName, Grammar g) {
    %match(g) {
      //Sorts[TypeList=(_*,ty,_*)] -> { `fill(moduleName,ty); }
      Grammar[ProductionList=(_*,p,_*)] -> { `fillFromProduction(moduleName,p); }
    }
  }

  private static StringList getConstructors(ProductionList pl) {
    StringList res = `StringList();
    %match(pl) {
      ConcProduction(_*,Production[Name=n],_*) -> { 
        res = `ConsStringList(n,res); 
      }
    }
    return res;
  }

  /* Methods only used by freshgom */

  /**
   * returns only sorts concerned by freshGom
   **/
  public Set<String> getFreshSorts() {
    Set<String> res = getSorts();
    Iterator<String> it = res.iterator();
    while(it.hasNext()) { 
      if(!isFreshType(it.next())) {
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
    HashSet<String> res = new HashSet();
    for(String c: constructors.keySet()) {
      if(isFreshType(getSort(c))) {
        res.add(c);
      }
    }
    return res;
  }


  public static String rawSort(String s) {
    return "Raw" + s; 
  }

  public String rawCons(String c) { 
    if(isGenerated(c)) {
      %match(getGenerated(c)) {
        GenCons(suffix) ->  { return "ConsRaw" + `suffix; }
        GenNil(suffix) -> { return "EmptyRaw" + `suffix; }
      }
    }
    return "Raw" + c; 
  }

  public String qualifiedRawSortId(String sort) {
    SortDescription desc = sorts.get(sort);
    if(desc==null) {
      throw new UndeclaredSortException(sort);
    }
    %match(desc) {
      SortDescription[ModuleSymbol=m] -> { 
        return (packageName.equals("") ? "" : packageName + ".") 
          + `m.toLowerCase() + ".types." + rawSort(sort); 
      }
    }
    throw new RuntimeException("non exhaustive match");
  }

  public String qualifiedRawConstructorId(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    if(desc==null) {
      throw new UndeclaredConstructorException(cons);
    }
    %match(desc) {
      ConstructorDescription[SortSymbol=s] -> {
        return getFullSortClassName(`s) + "." + `rawCons(cons); 
      }
    }
    throw new RuntimeException("non exhaustive match");
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
        Binds=boundAtoms,ProductionList=pl] -> {
          // filling sorts (except AccessibleAtoms)
          StringList cons = `getConstructors(pl);
          StringList bound = `convertBoundAtoms(boundAtoms);
          StringList empty = `StringList();
          FreshSortInfo info = null;
          %match(spe) {
            ExpressionType[] -> { info = `ExpressionTypeInfo(empty); }
            PatternType[] -> { info = `PatternTypeInfo(bound,empty,false); }
            AtomType[] -> { info = `AtomTypeInfo(); }
          }
          sorts.put(`n,`SortDescription(cons,moduleName,info));
          // filling constructors
          %match(pl) {
            ConcProduction(_*,pr,_*) -> { `fillCons(n,pr); }
          }
        }
    }
  }

  private void fillCons(String codom, Production p)  {
    %match(p) {
      Production[Name=n,DomainList=dl] -> {
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
    if(GomEnvironment.getInstance().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      %match(i) { ExpressionTypeInfo[] -> { return true; } }
      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredSortException(sort);
    }
  }

  public boolean isPatternType(String sort) {
    if(GomEnvironment.getInstance().isBuiltin(sort)) {
      return false; 
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      %match(i) { PatternTypeInfo[] -> { return true; } }
      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredSortException(sort);
    }
  }

  public boolean isAtomType(String sort) {
    if(GomEnvironment.getInstance().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      %match(i) { AtomTypeInfo[] -> { return true; } }
      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredSortException(sort);
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
      throw new UndeclaredSortException(sort);
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

  private void fillRefreshPoints() {
    for(Map.Entry<String,ConstructorDescription> e: constructors.entrySet()) {
      String c = e.getKey();
      ConstructorDescription cd = e.getValue();
      String codom = cd.getSortSymbol();
      try {
        ConstructorDescription ncd = (ConstructorDescription)
          `TopDown(SetRefreshPoints(this,codom)).visit(cd);
        constructors.put(c,ncd);
      } catch(Exception ex) { throw new RuntimeException(); }
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
      throw new UndeclaredConstructorException(constructor);
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
    } catch(NullPointerException e) {
      throw new UndeclaredConstructorException(constructor);
    }
    throw new RuntimeException("Cannot access to the "+omega+"ith child of the constructor "+constructor);
  }

  public tom.gom.adt.symboltable.types.stringlist.StringList 
    getAccessibleAtoms(String sort) {
      try {
        return (tom.gom.adt.symboltable.types.stringlist.StringList)
          sorts.get(sort).getFreshInfo().getAccessibleAtoms();
      } catch(NullPointerException e) {
        throw new UndeclaredSortException(sort);
      }
    }

  public tom.gom.adt.symboltable.types.stringlist.StringList getBoundAtoms(String sort) {
    return (tom.gom.adt.symboltable.types.stringlist.StringList)
      sorts.get(sort).getFreshInfo().getBoundAtoms();
  }

  private FieldDescriptionList getFieldList(String constructor) {
    return constructors.get(constructor).getFields();
  }

  public ArrayList<String> getFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    %match(l) {
      concFieldDescription(_*,FieldDescription[FieldName=n],_*) -> {
        result.add(`n);
      }
    }
    return result;
  }

  public ArrayList<String> getNeutralFields(String constructor) {
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

  public ArrayList<String> getPatternFields(String constructor) {
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
    if(isGenerated(cons)) {
      String suffix = getBaseName(cons);
      if(field.startsWith("Head")) {
        return "getHeadRaw" + suffix + "()";
      } else if (field.startsWith("Tail")) {
        return "getTailRaw" + suffix + "()";
      }
    }
    return "get" + field + "()";
  }

  public ArrayList<String> getNonPatternFields(String constructor) {
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

  public ArrayList<String> getOuterFields(String constructor) {
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

  public ArrayList<String> getInnerFields(String constructor) {
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
        if (`n.equals(field)) return `sort;
      }
    }
    throw new RuntimeException("sould never happen");
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
        if(`n.equals(field)) {
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
        if(`n.equals(field)) {
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
        if(`n.equals(field)) {
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
    throw new RuntimeException("not a variadic operator");
  }

  /**
   * for variadic operators 
   */
  public String getCoDomain(String cons) {
    ConstructorDescription d = constructors.get(cons);
    %match(d) {
      VariadicConstructorDescription[SortSymbol=s] -> { return `s; }
    }
    throw new RuntimeException("not a variadic operator");
  }


  public boolean isRefreshPoint(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    %match(l) {
      concFieldDescription(_*,
          FieldDescription[FieldName=n,StatusValue=SRefreshPoint()],_*) -> {
        if(`n.equals(field)) {
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
    while(it.hasNext()) {
      if(!isAtomType(it.next())) {
        it.remove();
      }
    }
    return res;
  }

  private StringList getAccessibleAtoms
    (String sort, HashSet<String> visited)  {
      if(GomEnvironment.getInstance().isBuiltin(sort) || visited.contains(sort)) {
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
            Fields=(_*,FieldDescription[Sort=ty],_*)] -> {
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
      if (GomEnvironment.getInstance().isBuiltin(sort)) continue;
      for(String c: getConstructors(sort)) {
        ConstructorDescription cd = constructors.get(c);
        %match(cd) {
          VariadicConstructorDescription[Domain=ty] -> {
            if(!GomEnvironment.getInstance().isBuiltin(`ty)) {
              sortDependences.addLink(sort,`ty);
            }
          }
          ConstructorDescription[
            Fields=(_*,FieldDescription[Sort=ty],_*)] -> {
              if(!GomEnvironment.getInstance().isBuiltin(`ty)) {
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
    for(String s: sorts) { setFreshSortInfo(s,`NoFreshSort()); }
  }

  /* TODO: use GomMessage and the logger instead of RutimeException */

  public class SortException extends RuntimeException {
    protected String sortName;

    public SortException(String sortName) {
      super();
      this.sortName = sortName;
    }
    public String getSortName() { return sortName; } 
    public String toString() {
      return "sort exception: " + sortName;
    }
  }

  public class UndeclaredSortException extends SortException {
    public UndeclaredSortException(String n) { super(n); }
    public String toString() {
      return "undeclared sort: " + sortName;
    }
  }

  public class ConstructorException extends RuntimeException {
    protected String constructorName;
    public ConstructorException(String constructorName) {
      super();
      this.constructorName = constructorName;
    }
    public String getConstructorName() { return constructorName; } 
    public String toString() {
      return "constructor exception: " + constructorName;
    }
  }

  public class UndeclaredConstructorException extends ConstructorException {
    public UndeclaredConstructorException(String n) { super(n); }
  }

  public class InvalidConstructorException extends ConstructorException {
    public InvalidConstructorException(String n) { super(n); }
  }


}
