/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;
import org._3pq.jgrapht.graph.SimpleGraph;

import java.util.*;

import aterm.pure.*;
import aterm.*;

import gasel.gasel2.data.*;
import gasel.gasel2.data.types.*;

public final class Gasel2 {
  private static Graph globalGraph;

  private  static Graph getGraph() {
    return globalGraph;
  }
 
  public static void main( String[] args ) {
    globalGraph = new SimpleGraph();
    run();
  }

  %gom {
  module data
    imports int
	abstract syntax		
		Atom = empty()
		     | C(n:int)
		     | arC(n:int)
		     | O(n:int)
		     | arO(n:int)
		     | H(n:int)
		     | e(n:int)
		
    Bond = bond(bondType:BondType,source:Atom,target:Atom)

    BondList = concBond( Bond* )

		BondType = none()
             | simpleLink() 
             | doubleLink() 
             | tripleLink()
             | aromLink()
  }

  %typeterm StateList {
    implement { List }
    is_sort(t) { $t instanceof List }
    equals(t1,t2) { $t1.equals($t2) } 
  }

  %typeterm State {
    implement { State }
    is_sort(t) { $t instanceof State }
    equals(t1,t2) { $t1.equals($t2) } 
  }

  %op State rad(bondtype:BondType,atom:Atom,subterm:StateList) {
    is_fsym(t)  { $t instanceof State }
    get_slot(bondtype,t) { $t.getBondType() }
    get_slot(atom,t) { $t.getAtom() }
    get_slot(subterm,t)  { computeSuccessors(getGraph(), $t) }
    make(bondType,atom,subterm) { createState(getGraph(), $bondType, $atom, $subterm) }
  }
 
  %oparray StateList conc( State* ) {
    is_fsym(t)       { $t instanceof List }
    make_empty(n)    { new ArrayList($n)       }
    make_append(e,l) { myAdd($e,(ArrayList)$l)  }
    get_element(l,n) { ((State)($l.get($n)))        }
    get_size(l)      { $l.size()               }
  }

  private static List myAdd(Object e,List l) {
    l.add(e);
    return l;
  }
  
  private static Map labelMap = new HashMap();

  /*
   * add a simpleLink bond to the graph
   * the label is stored in a hashmap
   */
  private static void addBond(Atom v1, Atom v2, BondType bondType) {
    if(!bondType.isnone()) {
      org._3pq.jgrapht.Edge e = new org._3pq.jgrapht.edge.UndirectedEdge(v1,v2);
      labelMap.put(e,`bond(bondType,v1,v2));
      //System.out.println("add label( " + e + " ) = " + labelMap.get(e)); 
      getGraph().addEdge(e);
    }
  }

  private static Bond getBond(org._3pq.jgrapht.Edge e) {
    Bond bond = (Bond)labelMap.get(e);
    if(bond==null) {
      throw new RuntimeException("no associated bond to: " + e); 
    }
    //System.out.println("label( " + e + " ) = " + bond); 
    return bond;
  }

  private static BondType getBondType(org._3pq.jgrapht.Edge e) {
    return getBond(e).getbondType();
  }

  /*
   * This creates a graph by side-effect
   */
  private static State createState(Graph g, BondType bondType, Atom atom, List subterm) {
    if(!atom.isempty()) {
      g.addVertex(atom);
    }
    for(Iterator it = subterm.iterator() ; it.hasNext() ; ) {
      State state = (State)it.next();
      addBond(atom, state.getAtom(), state.getBondType());
    }
    return new State(`concBond(),bondType, atom);
  }
  
  /*
   * given a node, compute all its immediate successors with the bond information
   */
  private static List computeSuccessors(Graph g, State state) {
    Atom atom = state.getAtom();
    BondList path = state.getPath();
    List res = new LinkedList();
    for(Iterator it=g.edgesOf(atom).iterator() ; it.hasNext() ; ) {
      org._3pq.jgrapht.Edge e = (org._3pq.jgrapht.Edge)it.next();
      Bond b = getBond(e);
      if(contains(b,path)) { // bond does not occur in path
        Atom successor = (Atom)e.oppositeVertex(atom);
        res.add(new State(`concBond(b,path*),getBondType(e),successor));
      }
    }
    return res;
  }

  private static boolean contains(Bond e,BondList list){
    if (list.isEmptyconcBond()) return false;
    return list.getHeadconcBond().equals(e) ||contains(e,list.getTailconcBond());
  }

  public static void run() {
    Graph g = getGraph();

    /*
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
    addBond( v1, v2, `simpleLink() );
    addBond( v2, v3, `simpleLink() );
    addBond( v3, v4, `simpleLink() );
    addBond( v4, v5, `simpleLink() );
    addBond( v5, v3, `simpleLink() );

    System.out.println("g = " + g);
    System.out.println("edges of C3 = " + g.edgesOf(v3));
    System.out.println("successors of C3 = " + computeSuccessors(g,new State(`concBond(bond(simpleLink(),v2,v3)),`simpleLink(),v3)));
   
    State state = new State(`emptyBondList(),`none(),v1);
    */

    State state = `rad(none(), e(1), conc(rad(simpleLink(), C(2),
                                     conc(rad(simpleLink(), C(3),
                                     conc(rad(simpleLink(), C(4),conc(rad(simpleLink(), C(5),conc()))),
                                          rad(simpleLink(), C(5),conc())))))));
/*
    State state = `rad(none(), e(1), conc(rad(simpleLink(), C(2),
                                     conc(
                                      rad(simpleLink(),C(6),conc(rad(simpleLink(),C(7),conc(rad(simpleLink(),C(3),conc()))))), 
                                       rad(simpleLink(), C(3),
                                     conc(rad(simpleLink(), C(4),conc(rad(simpleLink(), C(5),conc()))),
                                          rad(simpleLink(), C(5),conc())))))));
  */
    System.out.println("g = " + g);

    %match(State state) {
      // e C
      rad(_, e[], conc(_*,
      rad(simpleLink(), C[],subterm),
      _*)) -> {
        System.out.println("Bingo 1: " + `subterm);
      }
      
      // e C C
      rad(_,e[], conc(_*, 
      rad(simpleLink(),C[], conc(_*, 
      rad(simpleLink(),C[],subterm),
      _*)),_*)) -> {
        System.out.println("Bingo 2: " + `subterm);
      }
      
      // e C C C
      rad(_,e[], conc(_*, 
      rad(simpleLink(),C[], conc(_*,
      rad(simpleLink(),C[], conc(_*, 
      rad(simpleLink(),C[],subterm),
      _*)),_*)),_*)) -> {
        System.out.println("Bingo 3: " + `subterm);
      }
      
      // e C C C C C with a cycle
      rad(_,e[], conc(_*, 
      rad(b,C[], conc(_*,
      rad(b,x@C[], conc(_*, 
      rad(b,C[], conc(_*, 
      rad(b,C[], conc(_*, 
      rad(b,y@C[],subterm),
      _*)),_*)),_*)),_*)),_*)) -> {
        System.out.println("x = " + `x + " y = " + `y);
        if(`x.equals(`y)) {
          System.out.println("Bingo 4: " + `x);
        }
      }
    }

  }
}

/*
 * This classe stores an atom and the path from the root to this atom
 */
class State {
  private BondList path;
  private Atom atom;
  private BondType bondType;

  public State(BondList path, BondType bondType, Atom atom) {
    this.path = path; 
    this.bondType = bondType;
    this.atom = atom;
  }

  public BondType getBondType() {
    return bondType;
  }

    public BondList getPath() {
      return path;
  }

  public Atom getAtom() {
    return atom;
  }

  public String toString() {
    return "(" + getPath() + "," + getBondType() + "," + getAtom() + ")";
  }
}

