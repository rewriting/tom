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

public class List1 {
  private static ATermFactory factory = SingletonFactory.getInstance();

  %typeterm TomList {
    implement { ATermList }
    is_sort(t) { $t instanceof ATermList }
    equals(l1,l2) { $l1==$l2 }
  }

  %oplist TomList conc( TomTerm* ) {
    is_fsym(t) { $t instanceof ATermList }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { $l.insert($e) }
    get_head(l) { (ATermAppl)$l.getFirst() }
    get_tail(l) { $l.getNext() }
    is_empty(l) { $l.isEmpty() }
  }
  
  %typeterm TomTerm {
    implement { ATermAppl }
    is_sort(t) { $t instanceof ATermAppl }
    equals(t1, t2) { $t1==$t2 }
  }

  %op TomTerm a() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "a" }
    make() { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }
  
  %op TomTerm b() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "b" }
    make() { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }

  %op TomTerm c() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "c" }
    make() { factory.makeAppl(factory.makeAFun("c", 0, false)) }
  }
  
  public ATermList swapSort(ATermList l) {
    %match(TomList l) {
      conc(X1*,x,X2*,y,X3*) -> {
        String xname = `x.getName();
        String yname = `y.getName();
        if(xname.compareTo(yname) > 0) {
          return `swapSort(conc(X1*,y,X2*,x,X3*));
        }
      }
    }
		return l; 
  }

  public ATermList removeDouble(ATermList l) {
    %match(TomList l) {
      conc(X1*,x,x,X2*) -> {
        return `removeDouble(conc(X1*,x,X2*));
      }
    }
		return l; 
  }

	public ATermList makeSubject() {
		return `conc(a(),b(),c(),a(),b(),c(),a());
	}

  public void run() {
    ATermList l = `conc(a(),b(),c(),a(),b(),c(),a());
    ATermList res1 = swapSort(l);
    ATermList res2 = removeDouble(res1);
    System.out.println(" l       = " + l);
    System.out.println("sorted l = " + res1);
    System.out.println("single l = " + res2);
  }

  public final static void main(String[] args) {
    List1 test = new List1();
    test.run();
  }

}

