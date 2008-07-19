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

public class FreshExpander {

  %include { ../adt/gom/Gom.tom}
  %include { util/ArrayList.tom }
  %include { sl.tom }  

  %typeterm SymbolTable { implement { tom.gom.SymbolTable } }

  private SymbolTable st = new SymbolTable();

  public GomModuleList expand(GomModuleList m) {
    try {
      ArrayList list = new ArrayList();
      GomModuleList res = (GomModuleList) 
        `Sequence(
            TopDown(ExpandAtoms(list)),
            TopDown(UpdateSpecialization(list))).visitLight(m);
      st.fill(res);
      //System.out.println(st);
      res = addRawSortsAndConstructors(res);
      return res;
    } catch (SymbolTable.SortException e) {
      System.out.println(e);
      throw new RuntimeException("sort exception");
    } catch (VisitFailure e) {
      throw new RuntimeException("should not happen");
    }
  }
  
  private static String raw(String s) { return "Raw" + s; }

  /* -- rawification --**/

  private GomModuleList addRawSortsAndConstructors(GomModuleList res) {
    Set<String> fsorts = st.getFreshSorts();
    System.out.println("fresh sorts : " + fsorts);
    try { return (GomModuleList) `TopDown(AddRaw(fsorts)).visitLight(res); }
    catch(VisitFailure e) { 
      throw new RuntimeException("should never happen"); 
    }
  }

  %typeterm StringSet { implement { Set<String> } }
  %strategy AddRaw(sorts:StringSet) extends Identity() {
    visit ProductionList {
      l@ConcProduction(p@SortType[Type=GomType[Name=n]],ps*) -> {
        if (sorts.contains(`n)) {
          sorts.remove(`n);
          return `ConcProduction(p,rawify(p),ps*);
        }
        else return `l;
      }
    }
  }

  %strategy Rawify() extends Identity() {
    visit GomType { 
      gt@GomType[Name=n] -> { return `gt.setName(raw(`n)); }
    }
    visit Production { 
      p@Production[Name=n] -> { return `p.setName(raw(`n)); }
    }
  }

  private static Production rawify(Production p) {
    try { return (Production) `TopDown(Rawify()).visitLight(p); }
    catch(VisitFailure f) { 
      throw new RuntimeException("should never happen"); 
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
