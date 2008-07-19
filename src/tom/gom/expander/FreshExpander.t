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

public class FreshExpander {

  %include { ../adt/gom/Gom.tom}
  %include { util/ArrayList.tom }
  %include { sl.tom }  

  public static GomModuleList expand(GomModuleList m) {
    try {
      ArrayList list = new ArrayList();
      GomModuleList res = 
        (GomModuleList) `Sequence(TopDown(ExpandAtoms(list)),TopDown(UpdateSpecialization(list))).visitLight(m);
      SymbolTable st = new SymbolTable();
      st.fill(res);
      System.out.println(st);
      return res;
    } catch (SymbolTable.SortException e) {
      System.out.println(e);
      throw new RuntimeException("Unexpected failures during freshgom expansion");
    } catch (VisitFailure e) {
      throw new RuntimeException("Unexpected failures during freshgom expansion");
    }
  }

  %strategy ExpandAtoms(list:ArrayList) extends Identity() {
    visit Production {
      AtomDecl[ Name=name ] -> {
        list.add(`name);
        return `SortType(
            GomType(AtomType(),name),
            ConcAtom(),
            ConcProduction(Production(name,ConcField(NamedField(None(),"n",GomType(ExpressionType(),"int")),NamedField(None(),"hint",GomType(ExpressionType(),"String"))),GomType(AtomType(),name),Origin(-1))));  
      }
    }
  }

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
