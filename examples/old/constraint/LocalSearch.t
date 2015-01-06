/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the Inria nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import aterm.*;
import aterm.pure.*;
import tom.library.traversal.*;
import adt.localsearch.*;
import adt.localsearch.types.*;

public class LocalSearch {
  
  private GenericTraversal traversal;

// ------------------------------------------------------------  
  %include { term/localsearch.tom }
// ------------------------------------------------------------  
 
  public LocalSearch() {
    this.traversal = new GenericTraversal();
  }

  Csop createFirstNeighborg(Csop t) {
    %match(Csop t) {
      state(empty(),v,lc,lrhs,z,ln,lfn) -> {
        Tuple x2 = `origin(empty());
        Csop res = `state(empty(),v,lc,lrhs,z,concTuple(x2,ln*),lfn);
		System.out.println("createFirstNeighborg: " + res);
		return res;
      }
    } 
    return `fail();
  }

 Csop createNeighborgs(Csop t) {
    %match(Csop t) {
      state(x,v,lc,lrhs,z,ln,lfn) -> {
        Tuple nx1 = `complement(x,1);
        Tuple nx2 = `complement(x,2);
        Tuple nx3 = `complement(x,3);
        Tuple nx4 = `complement(x,4);
        Csop res = `state(x,v,lc,lrhs,z,concTuple(nx1,nx2,nx3,nx4,ln*),lfn);
		System.out.println("createNeighborgs:     " + res);
		return res;
      }
    } 
    return `fail();
  }

 Csop selectNeighborg(Csop t) {
    %match(Csop t) {
      state(x,v,lc,lrhs,z,ln,lfn) -> {
        Tuple nx = `selectFirstFeasibleNeighborg(lfn,z,v);
        Csop res = `state(nx,eval(nx,z),lc,lrhs,z,ln,concTuple());
		System.out.println("selectNeighborg:      " + res);
		return res;
      }
    } 
    return `fail();
  }

 Csop verifySatisfiability(Csop t) {
    %match(Csop t) {
      state(x,v,lc,lrhs,z,ln,lfn) -> {
        TupleList lfn1 = `selectFeasibleNeighborg(ln,lc,lrhs);
        Csop res = `state(x,v,lc,lrhs,z,concTuple(),lfn1);
		System.out.println("verifySatisfiability: " + res);
		return res;
      }
    } 
    return `fail();
  }

  Tuple origin(Tuple x) {
    return `vector(0,0,0,0);
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
      concTuple(c,l1*), concInteg(i(rhs),l2*) -> { return satisfy(x,c,rhs) && satisfySet(x,l1,l2); }
      (), () -> { return true; } 
    }
    return false;
  }

  public int comp(int x) {
    return 1-x;
    //return (x==0)?1:0;
/*
	%match(int x) {
      0 -> { return 1; }
      1 -> { return 0; }
    }
    return -1;
*/
  }

  public int eval(Tuple t1, Tuple t2) {
	%match(Tuple t1, Tuple t2) {
      vector(x1,x2,x3,x4), vector(c1,c2,c3,c4) -> { return x1*c1 + x2*c2 + x3*c3 + x4*c4; }
    }
    return -1;
  }

  public Csop hillClimbing(Csop t) {
    Csop res = t;
	res = createFirstNeighborg(res);
	res = verifySatisfiability(res);
	res = selectNeighborg(res);
	Csop oldRes;
    do {
	  oldRes = res;
      res = createNeighborgs(res);
	  res = verifySatisfiability(res);
	  res = selectNeighborg(res);
    } while(res != oldRes);
    return res;
  }

  public void run() {
    Csop query = `state(empty(),
                        -10,
                        concTuple(vector(10,30,10,20),vector(5,20,20,10),vector(20,10,27,40),vector(0,10,10,20)),
						concInteg(i(50),i(45),i(70),i(40)),
						vector(40,70,80,100),
						concTuple(),
						concTuple());
	Csop result = hillClimbing(query);
	System.out.println("result = " + result);
  }
  
  public final static void main(String[] args) {
    LocalSearch test = new LocalSearch();
    
    test.run();
  }
}
