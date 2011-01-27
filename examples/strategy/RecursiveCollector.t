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

package strategy;

import strategy.term.*;
import strategy.term.types.*;
import tom.library.sl.*;

public class RecursiveCollector {

  %include { term/term.tom }
  %include { sl.tom }
  
  public final static void main(String[] args) {
    //Term subject = `g(g(a(),b()),g(c(),d()));
    Term subject = `h(g(a(),b()),a(),g(c(),d()));

    try {
      System.out.println("subject          = " + subject);
      System.out.println("collect all nodes, except those under the first subterm of g(...)");
      `mu(MuVar("x"),TopDownCollect(Collector(MuVar("x")))).visitLight(subject);
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }

  }
 
  /*
   * under a g(), collect only in second subterm
   * 2 solutions
   * - use getRoot
   * - perform mu-expansion in user defined strategies [TODO]
   */

  %strategy Collector(current:Strategy) extends `Identity() {
    visit Term {
      g(_,t2) -> {
      //MuTraveler.getRoot(this).visitLight(`t2);
      //MuTraveler.init(`TopDownCollect(Collector())).visitLight(`t2);
      current.visitLight(`t2);
      throw new VisitFailure(); 
      }

      x -> { System.out.println("found " + `x); }
    }
  }
}
