/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
 * 	- Neither the name of the INRIA nor the names of its
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

package list;

import aterm.*;
import aterm.pure.SingletonFactory;

public class BenchList {
  private static ATermFactory factory = SingletonFactory.getInstance();

  %include { int.tom }

  %typeterm TomList {
    implement { ATermList }
    is_sort(t) { $t instanceof ATermList }
    equals(l1,l2) { $l1==$l2 }
  }

  %oplist TomList conc( int* ) {
    is_fsym(t) { $t instanceof ATermList }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { $l.insert(factory.makeInt($e)) }
    get_head(l)   { ((ATermInt)$l.getFirst()).getInt() }
    get_tail(l)   { $l.getNext() }
    is_empty(l)   { $l.isEmpty() }
  }
  
  %typeterm TomTerm {
    implement { ATermAppl }
    is_sort(t) { $t instanceof ATermAppl }
    equals(t1, t2)     { $t1==$t2 }
  }

  public ATermList genere(int n) {
    if(n>2) {
      ATermList l = genere(n-1);
      return `conc(n,l*);
    } else {
      return `conc(2);
    }
  }

  public ATermList elim(ATermList l) {
    %match(TomList l) {
      conc(x*,e1,y*,e2,z*) -> {
        if(`e2%`e1 == 0) {
          return `elim(conc(x*,e1,y*,z*));
        }
      }
    }
    return l;
  }

  public void run(int max) {
    //System.out.println(" l = " + genere(100));
    long startChrono = System.currentTimeMillis();

    //System.out.println(" l = " + elim(genere(100).reverse()));
    elim(genere(max).reverse());

    System.out.println(max + " " + (System.currentTimeMillis()-startChrono));
    System.out.println(factory);
  }

  public final static void main(String[] args) {
    BenchList test = new BenchList();
    int max = 100;
    try {
      max = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java list.BenchList <max>");
      return;
    }
    test.run(max);
  }

}
