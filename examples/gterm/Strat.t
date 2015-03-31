/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
package gterm;

import tom.library.sl.*;

public class Strat {
  %include { int.tom }
  %include { sl.tom }

  %typeterm TomList {
    implement { List }
    is_sort(t) { t instanceof List }
    equals(l1,l2) { l1==l2 }
  }

  %oplist TomList conc( int* ) {
    is_fsym(t) { t instanceof ConsInt || t instanceof Empty }
    make_empty()  { Empty.make() }
    make_insert(e,l) { ConsInt.make(e,l) }
    get_head(l)   { l.getHeadInt() }
    get_tail(l)   { l.getTail() }
    is_empty(l)   { l.isEmpty() }
  }
  
  public List genere(int n) {
    if(n>2) {
      List l = genere(n-1);
      return `conc(n,l*);
    } else {
      return `conc(2);
    }
  }

  public void run(int max) {
		List subject = genere(max);
    AbstractStrategyBasic rule = new RewriteSystem();
		try {
      System.out.println("subject       = " + subject);
      System.out.println("onceBottomUp  = " + `OnceBottomUp(rule).visitLight(subject));
      System.out.println("BottomUp  = " + `BottomUp(Try(rule)).visitLight(subject));
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    } catch (Exception e) {
			System.out.println(e);
		}
  }

  public final static void main(String[] args) {
    Strat test = new Strat();
    try {
      int max = Integer.parseInt(args[0]);
			test.run(max);
    } catch (Exception e) {
      System.out.println("Usage: java list.Strat <max>");
      return;
    }
  }
  
	class RewriteSystem extends AbstractStrategyBasic {
    public RewriteSystem() {
      super(`Fail());
    }
    
    public List visit_List(List arg) throws VisitFailure { 
      %match(TomList arg) {
				conc(h,t*) -> {
					int v = `h+1;
					return `conc(v,t*);
				}
      }
      return (List)`Fail().visitLight(arg);
    }
  }

}

