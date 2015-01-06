/*
 * 
 * TOM - To One Matching Compiler
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package gasel;
import org._3pq.jgrapht.*;
import org._3pq.jgrapht.edge.*;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;
import org._3pq.jgrapht.graph.SimpleGraph;

import java.util.*;

import aterm.pure.*;
import gasel.gasel1.data.*;
import gasel.gasel1.data.types.*;

public final class Gasel1 {
  private static Graph globalGraph;
  private  static Map labelMap = new HashMap();
  
  
  %gom {
  module data
  imports int
	abstract syntax		
		Atom = C(n:int)
		     | arC(n:int)
		     | O(n:int)
		     | arO(n:int)
		     | H(n:int)
		     | e(n:int)
		
		IntList = concInt( int* )

		Link = none() 
          | simpleLink() 
          | doubleLink() 
          | tripleLink() 
          | aromLink()
  }

  %typeterm Radical {
    implement { Object }
    is_sort(t) { $t instanceof Object }
    equals(t1,t2) { $t1.equals($t2) } 
  }
 
  %typeterm LinkRadicalList {
    implement { List }
    is_sort(t) { $t instanceof List }
    equals(t1,t2) { $t1.equals($t2) } 
  }

  %typeterm LinkRadical {
    implement { LinkRadical }
    is_sort(t) { $t instanceof LinkRadical }
    equals(t1,t2) { $t1.equals($t2) } 
  }
 
  %op LinkRadical linkrad(link:Link,rad:Radical) {
    is_fsym(t)  { $t instanceof LinkRadical }
    get_slot(link,t) { ((LinkRadical)$t).getLink() }
    get_slot(rad,t)  { ((LinkRadical)$t).getRadical() }
  }

  %op Radical rad(atom:Atom,subterm:LinkRadicalList) {
    is_fsym(t)  { $t instanceof Atom }
    get_slot(atom,t) { (Atom)$t }
    get_slot(subterm,t)  { computeSuccessors(getGraph(), $t) }
  }
 
  %oparray LinkRadicalList conc( LinkRadical* ) {
    is_fsym(t)       { $t instanceof List }
    make_empty(n)    { new ArrayList($n)       }
    make_append(e,l) { myAdd($e,(ArrayList)$l)   }
    get_element(l,n) { (LinkRadical)$l.get($n)        }
    get_size(l)      { $l.size()                }
  }

  private static List myAdd(Object e,List l) {
    l.add(e);
    return l;
  }

  private static Link getLink(Edge e) {
    Link label = (Link)labelMap.get(e);
    Link res;
    if(label==null) {
      res = `none();
    } else {
      res = label;
    }
    System.out.println("label( " + e + " ) = " + res); 
    return res;
  }

  private static List computeSuccessors(Graph g, Object v) {
    List edges = g.edgesOf(v);
    List res = new LinkedList();
    for(Iterator it=edges.iterator() ; it.hasNext() ; ) {
      Edge e = (Edge)it.next();
      res.add(new LinkRadical(getLink(e),(Atom)e.oppositeVertex(v)));
    }
    return res;
  }
  
  
  public static void main( String[] args ) {
    globalGraph = new SimpleGraph();
    run();
  }

  private  static Graph getGraph() {
    return globalGraph;
  }
 

  private static  void addSimpleLink(Object v1, Object v2) {
    Edge e = new UndirectedEdge(v1,v2);
    labelMap.put(e,`simpleLink());
    System.out.println("add label( " + e + " ) = " + labelMap.get(e)); 
    getGraph().addEdge(e);
  }

  public static void run() {
    Graph g = getGraph();


    Atom v1 = `e(1);
    Atom v2 = `C(2);
    Atom v3 = `C(3);
    Atom v4 = `C(4);
    Atom v5 = `C(5);

    // add the vertices
    g.addVertex( v1 );
    g.addVertex( v2 );
    g.addVertex( v3 );
    g.addVertex( v4 );
    g.addVertex( v5 );

    // add edges to create a circuit
    addSimpleLink( v1, v2 );
    addSimpleLink( v2, v3 );
    addSimpleLink( v3, v4 );
    addSimpleLink( v4, v5 );
    addSimpleLink( v5, v3 );

    System.out.println("g = " + g);
    System.out.println("edges of C3 = " + g.edgesOf(v3));
    System.out.println("successors of C3 = " + computeSuccessors(g,v3));
    
    %match(Radical v1) {
      // e C
      rad(e[], conc(linkrad(simpleLink(), rad(C[],subterm)) )) -> {
        System.out.println("Bingo 1: " + `subterm);
      }
      
      // e C C
      rad(e[], conc(_*, linkrad(simpleLink(),rad(C[],
                          conc(_*, linkrad(simpleLink(),rad(C[],subterm)),_*))),_*)) -> {
        System.out.println("Bingo 2: " + `subterm);
      }
      
      // e C C C
      rad(e[], conc(_*, linkrad(simpleLink(),rad(C[],
                          conc(_*, linkrad(simpleLink(),rad(C[],
                          conc(_*, linkrad(simpleLink(),rad(C[],subterm)),_*))),_*))),_*)) -> {
        System.out.println("Bingo 3: " + `subterm);
      }
      
    }

  }
}

class LinkRadical {
  private Link link;
  private Atom radical;

  public LinkRadical(Link link, Atom rad) {
    this.link = link;
    this.radical = rad;
  }

  public Link getLink() {
    return link;
  }

  public Atom getRadical() {
    return radical;
  }

  public String toString() {
    return "(" + getLink() + "," + getRadical() + ")";
  }
}

