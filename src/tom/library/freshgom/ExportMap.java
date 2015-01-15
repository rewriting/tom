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

package tom.library.freshgom;

import java.util.Hashtable;
import java.util.Collection;

/* stack for exportation (term -> raw term) */

public class ExportMap<T extends Atom> extends Hashtable<T,String> {

  private Hashtable<String,Integer> nums = new Hashtable<String,Integer>();


  public ExportMap() {
    super();
  }

  public ExportMap(ExportMap<T> o) {
    super(o);
    this.nums = new Hashtable<String,Integer>(o.nums);
  }

  public ExportMap<T> addSet(Collection<T> atoms) {
    ExportMap<T> res = new ExportMap<T>(this);
    for (T a: atoms) {
      String basename = a.gethint();
      int n = res.nums.containsKey(basename) ? res.nums.get(basename) : 0;
      if (n == 0) res.put(a,basename);
      else res.put(a,basename + n);
      res.nums.put(basename,n+1);
    }
    return res;
  }
}


