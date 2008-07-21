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

/* stack for importation (raw term -> term) */

public class ConvertMap<T extends Atom> {
  private class Pair { 
    public String x;
    public T a;
    public Pair(String x, T a) {
      this.x = x;
      this.a = a;
    } 
  }
  private LinkedList<Pair> ctx = new LinkedList<Pair>();
  private Stack<Integer> counters = new Stack<Integer>();
  private int counter = 0;
  public void push() {
    counters.push(counter);
    counter = 0;
  }
  public void add(String x, T a) {
    ctx.addFirst(new Pair(x,a)); 
    counter++;
  }
  public void pop() { 
    for(int i=0; i<counter; i++)
      ctx.removeFirst(); 
    counter = counters.pop();
  }
  public T get(String x) { 
    for (Pair p: ctx) {
      if (p.x.equals(x)) return p.a;
    }
    throw new RuntimeException(x + " is not bound");
  }
  public T getInScope(String x) {
    for(int i=0; i<counter; i++)
      if (ctx.get(i).x.equals(x)) return ctx.get(i).a;
    return null;
  }
}

