import org._3pq.jgrapht.*;
import org._3pq.jgrapht.edge.*;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;
import org._3pq.jgrapht.graph.SimpleGraph;

import java.util.*;

import aterm.pure.*;
import gasel2.data.*;
import gasel2.data.types.*;

public final class Gasel2 {
  private dataFactory factory;
  private Graph globalGraph;

  public Gasel2(dataFactory factory) {
    this.factory = factory;
    this.globalGraph = new SimpleGraph();
  }

  public dataFactory getDataFactory() {
    return factory;
  }
  
  private Graph getGraph() {
    return globalGraph;
  }
 
  public static void main( String[] args ) {
    Gasel2 t = new Gasel2(dataFactory.getInstance(new PureFactory()));
    t.run();
  }

  %vas {
  module data
	imports public

	sorts Atom IntList Link 
      
	abstract syntax	
    empty      -> Atom
		C(n:int)   -> Atom
		arC(n:int) -> Atom
		O(n:int)   -> Atom
		arO(n:int) -> Atom
		H(n:int)   -> Atom
		e(n:int)   -> Atom
		
		concInt( int* ) -> IntList  

		none   -> Link
		simple -> Link
		double -> Link
		triple -> Link
		arom   -> Link
  }

  %typeterm StateList {
    implement { List }
    equals(t1,t2) { t1.equals(t2) } 
  }

  %typeterm State {
    implement { State }
    equals(t1,t2) { t1.equals(t2) } 
  }

  /*
  %op State state(source:Atom,link:Link,atom:Atom) {
    is_fsym(t)  { t instanceof State }
    get_slot(source,t) { ((State)t).getSource() }
    get_slot(link,t) { ((State)t).getLink() }
    get_slot(atom,t)  { ((State)t).getAtom() }
  }
*/

  %op State rad(link:Link,atom:Atom,subterm:StateList) {
    is_fsym(t)  { t instanceof State }
    get_slot(link,t) { t.getLink() }
    get_slot(atom,t) { t.getAtom() }
    get_slot(subterm,t)  { computeSuccessors(getGraph(), t) }
  }
 
  %oparray StateList conc( State* ) {
    is_fsym(t)       { t instanceof List }
    make_empty(n)    { new ArrayList(n)       }
    make_append(e,l) { myAdd(e,(ArrayList)l)   }
    get_element(l,n) { (State)l.get(n)        }
    get_size(l)      { l.size()                }
  }

  private List myAdd(Object e,List l) {
    l.add(e);
    return l;
  }
  
  private Map labelMap = new HashMap();

  /*
   * add a simple link to the graph
   * the label is stored in a hashmap
   */
  private void addSimpleLink(Object v1, Object v2) {
    Edge e = new UndirectedEdge(v1,v2);
    labelMap.put(e,`simple());
    //System.out.println("add label( " + e + " ) = " + labelMap.get(e)); 
    getGraph().addEdge(e);
  }

  private Link getLink(Edge e) {
    Link label = (Link)labelMap.get(e);
    Link res;
    if(label==null) {
      System.out.println("edge without label: " + e); 
      res = `none();
    } else {
      res = label;
    }
    //System.out.println("label( " + e + " ) = " + res); 
    return res;
  }

  /*
   * given a node, compute all its immediate successors with the link information
   */
  private List computeSuccessors(Graph g, State state) {
    Atom atom = state.getAtom();
    List res = new LinkedList();
    for(Iterator it=g.edgesOf(atom).iterator() ; it.hasNext() ; ) {
      Edge e = (Edge)it.next();
      Atom successor = (Atom)e.oppositeVertex(atom);
      if(!successor.equals(state.getSource())) {
        res.add(new State(atom,getLink(e),successor));
      }
    }
    return res;
  }

  public void run() {
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
    System.out.println("successors of C3 = " + computeSuccessors(g,new State(v2,`simple(),v3)));
   
    State state = new State(`empty(),`none(),v1);

    %match(State state) {
      // e C
      rad(_, e[], conc(_*,rad(simple(), C[],subterm),_*)) -> {
        System.out.println("Bingo 1: " + subterm);
      }
      
      // e C C
      rad(_,e[], conc(_*, rad(simple(),C[],
                          conc(_*, rad(simple(),C[],subterm),_*)),_*)) -> {
        System.out.println("Bingo 2: " + subterm);
      }
      
      // e C C C
      rad(_,e[], conc(_*, rad(simple(),C[],
                          conc(_*, rad(simple(),C[],
                          conc(_*, rad(simple(),C[],subterm),_*)),_*)),_*)) -> {
        System.out.println("Bingo 3: " + subterm);
      }
      
    }

  }
}

class State {
  private Atom source;
  private Atom atom;
  private Link link;

  public State(Atom source, Link link, Atom atom) {
    this.source = source; 
    this.link = link;
    this.atom = atom;
  }

  public Link getLink() {
    return link;
  }

  public Atom getSource() {
    return source;
  }

  public Atom getAtom() {
    return atom;
  }

  public String toString() {
    return "(" + getSource() + "," + getLink() + "," + getAtom() + ")";
  }
}

