/*
 * Gom
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

package tom.library.freshgom;

import java.util.Hashtable;

/* stack for exportation (term -> raw term) */

public class AlphaMap<T extends Atom> {

  public static class AlphaException extends Exception {
    private static final long serialVersionUID = 1L;
  } 

  private Hashtable<T,T> table1 = new Hashtable<T,T>();
  private Hashtable<T,T> table2 = new Hashtable<T,T>();

  public AlphaMap() { }

  public AlphaMap(AlphaMap<T> o) {
    this.table1 = new Hashtable<T,T>(o.table1);
    this.table2 = new Hashtable<T,T>(o.table2);
  }

  public void put(T a1, T a2, T fresh) {
    table1.put(a1,fresh);
    table2.put(a2,fresh);
  }

  public boolean equal(T a1, T a2) {
    return table1.get(a1).equals(table2.get(a2));
  }

  public AlphaMap<T> combine(AlphaMap<T> m) {
    AlphaMap<T> res = new AlphaMap<T>(this);
    res.table1.putAll(m.table1);
    res.table2.putAll(m.table2);
    return res;
  }
}


