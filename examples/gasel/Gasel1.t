
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

  %gom {
  module data
	abstract syntax		
		Atom = C(n:int)
		     | arC(n:int)
		     | O(n:int)
		     | arO(n:int)
		     | H(n:int)
		     | e(n:int)
		
		IntList = concInt( int* )

		Link = none | simple | double | triple | arom

		//Radical = rad(link:Link,symbol:Symbol,radList:RadicalList)
		//RadicalList = concRad( Radical* )
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
    implement { LinkRadical }
    equals(t1,t2) { t1.equals(t2) } 
  }
 
  %op LinkRadical linkrad(link:Link,rad:Radical) {
    is_fsym(t)  { t instanceof LinkRadical }
    get_slot(link,t) { ((LinkRadical)t).getLink() }
    get_slot(rad,t)  { ((LinkRadical)t).getRadical() }
  }

  %op Radical rad(atom:Atom,subterm:LinkRadicalList) {
    is_fsym(t)  { t instanceof Atom }
    get_slot(atom,t) { (Atom)t }
    get_slot(subterm,t)  { computeSuccessors(getGraph(), t) }
  }
 
  %oparray LinkRadicalList conc( LinkRadical* ) {
    is_fsym(t)       { t instanceof List }
    make_empty(n)    { new ArrayList(n)       }
    make_append(e,l) { myAdd(e,(ArrayList)l)   }
    get_element(l,n) { (LinkRadical)l.get(n)        }
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
      res.add(new LinkRadical(getLink(e),(Atom)e.oppositeVertex(v)));
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
    Edge e = new UndirectedEdge(v1,v2);
    labelMap.put(e,`simple());
    System.out.println("add label( " + e + " ) = " + labelMap.get(e)); 
    getGraph().addEdge(e);
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
    System.out.println("successors of C3 = " + computeSuccessors(g,v3));
    
    %match(Radical v1) {
      // e C
      rad(e[], conc(linkrad(simple(), rad(C[],subterm)) )) -> {
        System.out.println("Bingo 1: " + subterm);
      }
      
      // e C C
      rad(e[], conc(_*, linkrad(simple(),rad(C[],
                          conc(_*, linkrad(simple(),rad(C[],subterm)),_*))),_*)) -> {
        System.out.println("Bingo 2: " + subterm);
      }
      
      // e C C C
      rad(e[], conc(_*, linkrad(simple(),rad(C[],
                          conc(_*, linkrad(simple(),rad(C[],
                          conc(_*, linkrad(simple(),rad(C[],subterm)),_*))),_*))),_*)) -> {
        System.out.println("Bingo 3: " + subterm);
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

