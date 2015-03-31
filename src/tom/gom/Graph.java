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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.Set;

public class Graph<T> {

  private class Node {
    public boolean mark = false;
    public T value = null;
    public HashSet<Node> neighbors = new HashSet<Node>();
    public Node(T v) { value = v; }
  }

  private HashMap<T,Node> table = new HashMap<T,Node>();

  public void addLink(T a, T b) {
    if (!table.containsKey(a)) { table.put(a,new Node(a)); }
    if (!table.containsKey(b)) { table.put(b,new Node(b)); }
    table.get(a).neighbors.add(table.get(b));
    table.get(b).neighbors.add(table.get(a));
  }

  private void reset() {
    for(Node n: table.values()) {
      n.mark = false;
    }
  }

  public void clear() {
    table.clear();
  }

  public Set<T> connected(Collection<T> c) {
    reset();
    HashSet<Node> set = new HashSet<Node>();
    for(T v: c) set.add(table.get(v));
    HashSet<T> result = new HashSet<T>();
    collectConnected(set,result);
    return result;
  }

  private void collectConnected(Collection<Node> c, Set<T> collected) {
    for(Node n: c) {
      if(n.mark) {
        continue;
      } else {
        n.mark = true;
        collected.add(n.value);
        collectConnected(n.neighbors,collected);
      }
    }
  }
}

