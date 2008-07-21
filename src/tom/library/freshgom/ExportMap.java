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

package tom.library.freshgom;

import java.util.LinkedList;
import java.util.Stack;

/* stack for exportation (term -> raw term) */

public class ExportMap<T extends Atom> {
  private class Pair { 
    public T a;
    public int i;
    public Pair(T a, int i) {
      this.a = a;
      this.i = i;
    } 
  }
  private LinkedList<Pair> ctx = new LinkedList<Pair>();
  private Stack<Integer> counters = new Stack<Integer>();

  private int counter = 0;
  public void push() {
    counters.push(counter);
    counter = 0;
  }

  public int add(T a) {
    int i = 0;
    for (Pair p: ctx) {
      if (p.a.gethint().equals(a.gethint())) { 
        i = p.i+1;
        break;
      }
    }
    ctx.addFirst(new Pair(a,i)); 
    counter++;
    return i;
  }
  public void pop() { 
    for(int i=0; i<counter; i++)
      ctx.removeFirst(); 
    counter = counters.pop();
  }
  public int get(T a) { 
    for (Pair p: ctx) {
      if (p.a.equals(a)) return p.i;
    }
    throw new RuntimeException(a + " is not bound");
  }
  public int getInScope(T a) {
    for(int i=0; i<counter; i++)
      if (ctx.get(i).a.equals(a)) return ctx.get(i).i;
    return -1;
  }
}


