/*
 * Copyright (c) 2004-2006, INRIA
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
import java.util.*;

public class List2 {
  private ATermFactory factory;

  public List2(ATermFactory factory) {
    this.factory = factory;
  }

	public ATermFactory getFactory() {
		return factory;
	}

  %typeterm TomList {
    implement { ArrayList }
    equals(l1,l2)      { l1.equals(l2) }
  }

  %oparray TomList conc( TomTerm* ) {
    is_fsym(t)       { t instanceof ArrayList }
    make_empty(n)   { myEmpty(n) }
    make_append(e,l) { myAdd(e,(ArrayList)l) }
    get_element(l,n)   { (ATermAppl)l.get(n) }
    get_size(l)        { l.size() }
  }

  private ArrayList myAdd(Object e,ArrayList l) {
    l.add(e);
    return l;
  }
  
  private ArrayList myEmpty(int n) {
    ArrayList res = new ArrayList(n);
    return res;
  }
  
  %typeterm TomTerm {
    implement { ATermAppl }
    equals(t1, t2)     { t1==t2 }
  }

  %op TomTerm a() {
    is_fsym(t) { ((ATermAppl)t).getName() == "a" }
    make() { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }
  
  %op TomTerm b() {
    is_fsym(t) { ((ATermAppl)t).getName() == "b" }
    make() { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }

  %op TomTerm c() {
    is_fsym(t) { ((ATermAppl)t).getName() == "c" }
    make() { factory.makeAppl(factory.makeAFun("c", 0, false)) }
  }
  
  public ArrayList swapSort(ArrayList l) {
    %match(TomList l) {
      conc(X1*,x,X2*,y,X3*) -> {
        String xname = `x.getName();
        String yname = `y.getName();
        if(xname.compareTo(yname) > 0) 
            return `swapSort(conc(X1*,y,X2*,x,X3*));     
      }
      _ -> { return l; }
    }
  }

  public ArrayList removeDouble(ArrayList l) {
    %match(TomList l) {
      conc(X1*,x,x,X2*) -> {
        return `removeDouble(conc(X1*,x,X2*));
        
      }

      _ -> { return l; }
    }
  }

	public ArrayList makeSubject() {
		return `conc(a,b,c,a,b,c,a);
	}

  public void run() {
    ArrayList l    = `conc(a,b,c,a,b,c,a,c);
    ArrayList res1 = swapSort(l);
    ArrayList res2 = removeDouble(res1);
    System.out.println(" l       = " + l);
    System.out.println("sorted l = " + res1);
    System.out.println("single l = " + res2);
  }

  public final static void main(String[] args) {
    List2 test = new List2(SingletonFactory.getInstance());
    test.run();
  }

}

