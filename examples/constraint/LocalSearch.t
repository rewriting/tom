import aterm.*;
import aterm.pure.*;
import java.util.*;
import jtom.runtime.*;
import adt.localsearch.*;

public class LocalSearch {
  

  private TermFactory factory;
  private GenericTraversal traversal;

// ------------------------------------------------------------  
  %include { adt/localsearch/term.tom }
// ------------------------------------------------------------  
 
  public LocalSearch(TermFactory factory) {
    this.factory = factory;
    this.traversal = new GenericTraversal();
  }

  public final TermFactory getTermFactory() {
      return factory;
  }
  


  Tuple complement(Tuple t, int n) {
    %match(Tuple t, int n) {
      vector(x1,x2,x3,x4), 1 -> { return `vector(comp(x1),x2,x3,x4); }
      vector(x1,x2,x3,x4), 2 -> { return `vector(x1,comp(x2),x3,x4); }
      vector(x1,x2,x3,x4), 3 -> { return `vector(x1,x2,comp(x3),x4); }
      vector(x1,x2,x3,x4), 4 -> { return `vector(x1,x2,x3,comp(x4)); }
    }
    return `empty();
  }

  Tuple selectFirstFeasibleNeighborg(TupleList tl, Tuple z, int v) {
    %match(TupleList tl) {
      (x,l*) -> {
		if(eval(x,z) > v) {
 		  return x;
 	    } else {
		  return selectFirstFeasibleNeighborg(l,z,v);
	    }
      }

	  () -> { return `empty(); }
    }
    return `empty();
  } 

  TupleList selectFeasibleNeighborg(TupleList tl, TupleList lc, IntList lrhs) {
    %match(TupleList tl) {
      (x,ln*) -> {
        TupleList select = selectFeasibleNeighborg(ln,lc,lrhs);
		if(satisfySet(x,lc,lrhs)) {
 		  return `concTuple(x,select*);
 	    } else {
		  return select;
	    }
      }

	  () -> { return tl; }
    }
    return `concTuple();
  } 

  public boolean satisfy(Tuple x, Tuple c, int rhs) {
    if(eval(x,c) < rhs) {
      return true;
    } else {
      return false;
    }
  }

  public boolean satisfySet(Tuple x, TupleList tl, IntList il) {
    %match(TupleList tl, IntList il) {
      concTuple(c,l1*), concInt(i(rhs),l2*) -> { return satisfy(x,c,rhs) && satisfySet(x,l1,l2); }
      (), () -> { return true; } 
    }
    return false;
  }

  public int comp(int x) {
	%match(int x) {
      0 -> { return 1; }
      1 -> { return 0; }
    }
    return -1;
  }

  public int eval(Tuple t1, Tuple t2) {
	%match(Tuple t1, Tuple t2) {
      vector(x1,x2,x3,x4), vector(c1,c2,c3,c4) -> { return x1*x1 + x2*c2 + x3*c3 + x4*c4; }
    }
    return -1;
  }

  public void run() {
 
  }

  
  public final static void main(String[] args) {
    LocalSearch test = new LocalSearch(new TermFactory(new PureFactory()));
    
    
    test.run();
  }
}
