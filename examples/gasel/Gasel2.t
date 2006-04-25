import org._3pq.jgrapht.Graph;
//import org._3pq.jgrapht.edge.*;
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
    equals(t1,t2) { t1.equals(t2) } 
  }

  %typeterm State {
    implement { State }
    equals(t1,t2) { t1.equals(t2) } 
  }

  %op State rad(bondtype:BondType,atom:Atom,subterm:StateList) {
    is_fsym(t)  { t instanceof State }
    get_slot(bondtype,t) { t.getBondType() }
    get_slot(atom,t) { t.getAtom() }
    get_slot(subterm,t)  { computeSuccessors(getGraph(), t) }
    make(bondType,atom,subterm) { createState(getGraph(), bondType, atom, subterm) }
  }
 
  %oparray StateList conc( State* ) {
    is_fsym(t)       { t instanceof List }
    make_empty(n)    { new ArrayList(n)       }
    make_append(e,l) { myAdd(e,(ArrayList)l)  }
    get_element(l,n) { (State)l.get(n)        }
    get_size(l)      { l.size()               }
  }

  private List myAdd(Object e,List l) {
    l.add(e);
    return l;
  }
  
  private Map labelMap = new HashMap();

  /*
   * add a simpleLink bond to the graph
   * the label is stored in a hashmap
   */
  private void addBond(Atom v1, Atom v2, BondType bondType) {
    if(!bondType.isNone()) {
      org._3pq.jgrapht.Edge e = new org._3pq.jgrapht.edge.UndirectedEdge(v1,v2);
      labelMap.put(e,`bond(bondType,v1,v2));
      //System.out.println("add label( " + e + " ) = " + labelMap.get(e)); 
      getGraph().addEdge(e);
    }
  }

  private Bond getBond(org._3pq.jgrapht.Edge e) {
    Bond bond = (Bond)labelMap.get(e);
    if(bond==null) {
      throw new RuntimeException("no associated bond to: " + e); 
    }
    //System.out.println("label( " + e + " ) = " + bond); 
    return bond;
  }

  private BondType getBondType(org._3pq.jgrapht.Edge e) {
    return getBond(e).getBondType();
  }

  /*
   * This creates a graph by side-effect
   */
  private State createState(Graph g, BondType bondType, Atom atom, List subterm) {
    if(!atom.isEmpty()) {
      g.addVertex(atom);
    }
    for(Iterator it = subterm.iterator() ; it.hasNext() ; ) {
      State state = (State)it.next();
      addBond(atom, state.getAtom(), state.getBondType());
    }
    return new State(`emptyBondList(),bondType, atom);
  }
  
  /*
   * given a node, compute all its immediate successors with the bond information
   */
  private List computeSuccessors(Graph g, State state) {
    Atom atom = state.getAtom();
    BondList path = state.getPath();
    List res = new LinkedList();
    for(Iterator it=g.edgesOf(atom).iterator() ; it.hasNext() ; ) {
      org._3pq.jgrapht.Edge e = (org._3pq.jgrapht.Edge)it.next();
      Bond b = getBond(e);
      if(path.indexOf(b,0) < 0) { // bond does not occur in path
        Atom successor = (Atom)e.oppositeVertex(atom);
        res.add(new State(`manyBondList(b,path),getBondType(e),successor));
      }
    }
    return res;
  }

  public void run() {
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
        System.out.println("Bingo 1: " + subterm);
      }
      
      // e C C
      rad(_,e[], conc(_*, 
      rad(simpleLink(),C[], conc(_*, 
      rad(simpleLink(),C[],subterm),
      _*)),_*)) -> {
        System.out.println("Bingo 2: " + subterm);
      }
      
      // e C C C
      rad(_,e[], conc(_*, 
      rad(simpleLink(),C[], conc(_*,
      rad(simpleLink(),C[], conc(_*, 
      rad(simpleLink(),C[],subterm),
      _*)),_*)),_*)) -> {
        System.out.println("Bingo 3: " + subterm);
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
        if(x.equals(y)) {
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

