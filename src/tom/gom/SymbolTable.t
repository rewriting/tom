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
import java.util.HashSet;


public class SymbolTable {

  %include { adt/gom/Gom.tom}
  %include { adt/symboltable/SymbolTable.tom}
  %include { util/ArrayList.tom }
  %include { sl.tom }  

  public class SortException extends Exception {
    protected String sortName;
    public SortException(String sortName, String message) {
      super(message);
      this.sortName = sortName;
    }
    public String getSortName() { return sortName; } 
    public String toString() {
      return "sort exception : " + sortName;
    }
  }

  public class UndeclaredSortException extends SortException {
    public UndeclaredSortException(String n, String m) { super(n,m); }
    public String toString() {
      return "undeclared sort : " + sortName;
    }
  }

  public class ConstructorException extends Exception {
    protected String constructorName;
    public ConstructorException(String constructorName, String message) {
      super(message);
      this.constructorName = constructorName;
    }
    public String getConstructorName() { return constructorName; } 
  }

  public class UndeclaredConstructorException extends ConstructorException {
    public UndeclaredConstructorException(String n, String m) { super(n,m); }
  }
  public class InvalidConstructorException extends ConstructorException {
    public InvalidConstructorException(String n, String m) { super(n,m); }
  }

  protected Hashtable<String,SortDescription> sorts = 
    new Hashtable<String,SortDescription>();

  protected Hashtable<String,ConstructorDescription> constructors =  
    new Hashtable<String,ConstructorDescription>();

  public String toString() {
    StringBuffer buf = new StringBuffer();
    for (Map.Entry<String,SortDescription> e: sorts.entrySet()) {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try { tom.library.utils.Viewer.toTree(e.getValue(),swriter); }
      catch(java.io.IOException ex) { ex.printStackTrace(); }
      buf.append(e.getKey() + ":\n" + swriter + "\n");
    }
    for(Map.Entry<String,ConstructorDescription> e: constructors.entrySet()){
      java.io.StringWriter swriter = new java.io.StringWriter();
      try { tom.library.utils.Viewer.toTree(e.getValue(),swriter); }
      catch(java.io.IOException ex) { ex.printStackTrace(); }
      buf.append(e.getKey() + ":\n" + swriter + "\n");
    }
    return buf.toString();
  }

  public String qualifiedSortId(String sort) 
    throws UndeclaredSortException {
      try {
        SortDescription desc = sorts.get(sort);
        %match(desc) {
          SortDescription[ModuleSymbol=m] -> { 
            return `m.toLowerCase() + ".types." + sort; 
          }
        }
        throw new RuntimeException("non exhaustive match");
      } catch (Exception e) {
        throw new UndeclaredSortException(
            sort, "sort " + sort + "is undeclared");
      }
    }

  public String qualifiedConstructorId(String cons) 
    throws UndeclaredConstructorException, InvalidConstructorException {
      try {
        ConstructorDescription desc = constructors.get(cons);
        %match(desc) {
          ConstructorDescription[SortSymbol=s] -> {
            return qualifiedSortId(`s) + "." + `cons;
          }
        }
        throw new RuntimeException("non exhaustive match");
      } catch (UndeclaredSortException e) {
        throw new InvalidConstructorException(
            cons, `cons + " has undeclared sort " + e.getSortName());
      } catch (Exception e) {
        throw new UndeclaredConstructorException(
            cons, cons + " is not declared");
      }
    }

  public void fill(GomModuleList gml) throws SortException {
    %match(gml) {
      ConcGomModule(_*,m,_*) -> { `fill(m); }
    }
    fillRefreshPoints();
    fillAccessibleAtoms();
  }

  private void fill(GomModule m) throws SortException {
    %match(m) {
      GomModule[ModuleName=GomModuleName[Name=n],
        SectionList=(_*,Public[GrammarList=(_*,g,_*)],_*)] -> {
          `fill(n,g);
        }
    }
  }

  private void fill(String moduleName, Grammar g) throws SortException {
    %match(g) {
      //Sorts[TypeList=(_*,ty,_*)] -> { `fill(moduleName,ty); }
      Grammar[ProductionList=(_*,p,_*)] -> { `fill(moduleName,p); }
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

  private static StringList convertBoundAtoms(AtomList al) {
    StringList res = `StringList();
    %match(al) {
      ConcAtom(_*,s,_*) -> { res = `ConsStringList(s,res); }
    }
    return res;
  }

  private void fill(String moduleName, Production p) throws SortException {
    %match(p) {
      SortType[Type=GomType[Specialization=spe,Name=n],
        Binds=boundAtoms,ProductionList=pl] -> {
          // filling sorts (except AccessibleAtoms and ContainsRefreshPoint)
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


  private void fillCons(String codom, Production p) 
    throws UndeclaredSortException {
      %match(p) {
        Production[Name=n,DomainList=dl] -> {
          %match(dl) {
            ConcField(StarredField[FieldType=GomType[Name=dom]]) -> {
              constructors.put(`n,
                  `VariadicConstructorDescription(codom,dom,false));
              return; 
            }
            _ /* not variadic */ -> { 
              FieldDescriptionList fl = `getFields(codom,dl);
              constructors.put(`n,`ConstructorDescription(codom,fl));
            } 
          }
        }
      }
    }

  private FieldDescriptionList getFields(String codom, FieldList dl)
    throws UndeclaredSortException {
      FieldDescriptionList res = `concFieldDescription();
      %match(dl) {
        ConcField(_*,
            NamedField[Specifier=spe,Name=fn,FieldType=GomType[Name=ty]],
            _*) -> {
          Status st = null;
          %match(spe) {
            Outer[] -> { st = `SOuter(); }
            Inner[] -> { st = `SInner(); }
            None[] -> { st = `SNeutral(); }
          }
          FieldDescription desc = `FieldDescription(fn,ty,st);
          res = `ConsconcFieldDescription(desc,res);
        }
      }
      return res;
    }

  private void setSortContainsRefreshPoint(String sort, boolean b) 
    throws UndeclaredSortException {
      SortDescription desc = sorts.get(sort);
      SortDescription ndesc = null;
      %match(desc) {
        SortDescription[FreshInfo=i@PatternTypeInfo[]] -> {
          ndesc = desc.setFreshInfo(`i.setContainsRefreshPoint(b));
        }
      }
      sorts.put(sort,ndesc);
    }

  private void setAccessibleAtoms(String sort, StringList atoms) 
    throws UndeclaredSortException {
      SortDescription desc = sorts.get(sort);
      SortDescription ndesc = null;
      %match(desc) {
        SortDescription[FreshInfo=i] -> {
          ndesc = desc.setFreshInfo(`i.setAccessibleAtoms(atoms));
        }
      }
      sorts.put(sort,ndesc);
    }

  private void setFreshSortInfo(String sort, FreshSortInfo i) 
    throws UndeclaredSortException {
      SortDescription desc = sorts.get(sort);
      sorts.put(sort,desc.setFreshInfo(i));
    }

  public boolean isExpressionType(String sort) 
    throws UndeclaredSortException {
      try {
        FreshSortInfo i = sorts.get(sort).getFreshInfo();
        %match(i) { ExpressionTypeInfo[] -> { return true; } }
        return false;
      } catch (Exception e) {
        throw new UndeclaredSortException(sort,null);
      }
    }

  public boolean isPatternType(String sort) 
    throws UndeclaredSortException {
      try {
        FreshSortInfo i = sorts.get(sort).getFreshInfo();
        %match(i) { PatternTypeInfo[] -> { return true; } }
        return false;
      } catch (Exception e) {
        throw new UndeclaredSortException(sort,null);
      }
    }

  public boolean isAtom(String sort) 
    throws UndeclaredSortException {
      try {
        FreshSortInfo i = sorts.get(sort).getFreshInfo();
        %match(i) { AtomTypeInfo[] -> { return true; } }
        return false;
      } catch (Exception e) {
        throw new UndeclaredSortException(sort,null);
      }
    }

  public static boolean isBuiltin(String sort) {
    %match(sort) {
      "bool" | "String" | "int" -> { return true; }
    }
    return false;
  }

  %typeterm SymbolTable { implement { tom.gom.SymbolTable } }
  %strategy 
    SetRefreshPoints(st:SymbolTable,codom:String) extends Identity() {
      visit FieldDescription {
        fd@FieldDescription[Sort=ty] -> {
          if(isBuiltin(`ty)) return `fd;
          try { 
            if(st.isExpressionType(codom) && st.isPatternType(`ty)) {
              st.setSortContainsRefreshPoint(`ty,true);
              return `fd.setStatusValue(`SRefreshPoint());
            }
          } catch(Exception e) { throw new RuntimeException(); }
        }
      }
      visit ConstructorDescription {
        vd@VariadicConstructorDescription[Domain=ty] -> {
          if(isBuiltin(`ty)) return `vd;
          try { 
            if(st.isExpressionType(codom) && st.isPatternType(`ty)) {
              st.setSortContainsRefreshPoint(`ty,true);
              return `vd.setIsRefreshPoint(true);
            }
          } catch(Exception e) { throw new RuntimeException(); }
        }
      }
    }

  private void fillRefreshPoints() {
    for(Map.Entry<String,ConstructorDescription> e: 
        constructors.entrySet()) {
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

  public StringList getConstructors(String sort) 
    throws UndeclaredSortException {
      return sorts.get(sort).getConstructors();
    }

  public String getSort(String constructor) 
    throws UndeclaredConstructorException {
      return constructors.get(constructor).getSortSymbol();
    }

  public FieldDescriptionList getFields(String constructor) 
    throws UndeclaredConstructorException {
      return constructors.get(constructor).getFields();
    }

  public Set<String> getSorts() {
    return sorts.keySet();
  }

  private StringList getAccessibleAtoms
    (String sort, HashSet<String> visited) 
    throws UndeclaredSortException, UndeclaredConstructorException {
      if (isBuiltin(sort) || visited.contains(sort)) return `StringList();
      visited.add(sort);
      if (isAtom(sort)) return `StringList(sort);
      StringList res = `StringList();
      for(String c: (tom.gom.adt.symboltable.types.stringlist.StringList) 
          getConstructors(sort)) {
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
    try {
      for(String s: getSorts()) {
        if(isExpressionType(s) || isPatternType(s)) {
          StringList atoms = getAccessibleAtoms(s,new HashSet<String>());
          %match(StringList atoms) {
            () -> { setFreshSortInfo(s,`NoFreshSort()); }
            (_,_*)  -> { setAccessibleAtoms(s,atoms); }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}

