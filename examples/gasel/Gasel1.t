
import org._3pq.jgrapht.*;
import org._3pq.jgrapht.edge.*;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;
import org._3pq.jgrapht.graph.SimpleGraph;

import java.util.*;

import aterm.pure.*;
import gasel1.data.*;
import gasel1.data.types.*;

public final class Gasel1 {
  private dataFactory factory;
  private Graph globalGraph;

  %vas {
  module data
	imports public

	sorts Atom IntList Symbol Link 
        // Radical RadicalList
      
	abstract syntax		
		C   -> Atom
		arC -> Atom
		O   -> Atom
		arO -> Atom
		H   -> Atom
		e   -> Atom
		
		concInt( int* ) -> IntList  
		symb(atom:Atom, number:Int) -> Symbol

		none   -> Link
		simple -> Link
		double -> Link
		triple -> Link
		arom   -> Link

		//rad(link:Link,symbol:Symbol,radList:RadicalList) -> Radical
		//concRad( Radical* ) -> RadicalList
  }

  %typeterm Radical {
    implement { Object }
    equals(t1,t2) { t1.equals(t2) } 
  }
 
  %typeterm LinkRadicalList {
    implement { List }
    equals(t1,t2) { t1.equals(t2) } 
  }

  %typeterm LinkRadical {
    implement { Edge }
    equals(t1,t2) { t1.equals(t2) } 
  }
 
  %op LinkRadical linkrad(link:Link,rad:Radical) {
    is_fsym(t)  { t instanceof DirectedEdge }
    get_slot(link,t) { getLink(t) }
    get_slot(rad,t)  { t.getTarget() }
  }

  %op Radical rad(symb:Symbol,subterm:LinkRadicalList) {
    is_fsym(t)  { t instanceof Symbol }
    get_slot(symb,t) { (Symbol)t }
    get_slot(subterm,t)  { computeSuccessors(getGraph(), t) }
  }
 
  %oparray LinkRadicalList conc( LinkRadical* ) {
    is_fsym(t)       { t instanceof List }
    make_empty(n)    { new ArrayList(n)       }
    make_append(e,l) { myAdd(e,(ArrayList)l)   }
    get_element(l,n) { (Edge)l.get(n)        }
    get_size(l)      { l.size()                }
  }

  private List myAdd(Object e,List l) {
    l.add(e);
    return l;
  }

  private Link getLink(Edge e) {
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

  private List computeSuccessors(Graph g, Object v) {
    List edges = g.edgesOf(v);
    List res = new LinkedList();
    for(Iterator it=edges.iterator() ; it.hasNext() ; ) {
      Edge e = (Edge)it.next();
      res.add(new DirectedEdge(v,e.oppositeVertex(v)));
    }
    return res;
  }

  public Gasel1(dataFactory factory) {
    this.factory = factory;
    this.globalGraph = new SimpleGraph();
  }

  public dataFactory getDataFactory() {
    return factory;
  }
  
  public static void main( String[] args ) {
    Gasel1 t = new Gasel1(dataFactory.getInstance(new PureFactory()));
    t.run();
  }

  private Graph getGraph() {
    return globalGraph;
  }
 
  private Map labelMap = new HashMap();

  private void addSimpleLink(Object v1, Object v2) {
    /*
    Edge e = new UndirectedEdge(v1,v2);
    labelMap.put(e,`simple());
    System.out.println("add label( " + e + " ) = " + labelMap.get(e)); 
    getGraph().addEdge(e);
    */

    getGraph().addEdge(v1,v2);
    labelMap.put(new DirectedEdge(v1,v2),`simple());
    labelMap.put(new DirectedEdge(v2,v1),`simple());
    
    Edge e = new DirectedEdge(v1,v2);
    System.out.println("add label( " + e + " ) = " + labelMap.get(e)); 

  }

  public void run() {
    Graph g = getGraph();


    Symbol v1 = `symb(e,1);
    Symbol v2 = `symb(C,2);
    Symbol v3 = `symb(C,3);
    Symbol v4 = `symb(C,4);
    Symbol v5 = `symb(C,5);

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
      rad(symb(e(),_), conc(linkrad(varsimple, rad(symb(C(),_),subterm)) )) -> {
        System.out.println("Bingo 1: " + subterm);
      }
     /* 
      // e C C
      rad(_, symb(e(),_), concRad(_*, rad(simple(),symb(C(),_),
                          concRad(_*, rad(simple(),symb(C(),_),subterm),_*)),_*)) -> {
        System.out.println("Bingo 2: " + subterm);
      }
      
      // e C C C
      rad(_, symb(e(),_), concRad(_*, rad(simple(),symb(C(),_),
                          concRad(_*, rad(simple(),symb(C(),_),
                          concRad(_*, rad(simple(),symb(C(),_),subterm),_*)),_*)),_*)) -> {
        System.out.println("Bingo 3: " + subterm);
      }
      */
    }

  }
}
