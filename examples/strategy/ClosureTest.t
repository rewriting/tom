/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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

package strategy;

import strategy.term.*;
import strategy.term.types.*;

import tom.library.sl.*;


public class ClosureTest {

  %include { term/term.tom }
  %include{ sl.tom }

  %strategy RewriteSystem() extends `Identity() {
    visit Term { 
        a() -> { return `b(); }
        b() -> { return `c(); }
        g(c(),c()) -> { return `c(); }
      }
    }

  %strategy Print() extends `Identity() {
    visit Strategy {
      x -> { System.out.println(`x); }
    }
  }

  public static void main(String[] args) {

    Term subject = `f(g(g(a(),b()),g(a(),a())));
    Strategy closure_topdown = new ClosureMu( new StrategyClosure() {
        public Strategy getStrategy(Strategy x) {
        return `Sequence(RewriteSystem(), All(x));
        }
    });
    try {
      
      System.out.println("subject       = " + subject);
      System.out.println("closure_topdown = " + closure_topdown.visitLight(subject));
      System.out.println("topdown = " + `TopDown(RewriteSystem()).visitLight(subject));

      // make the ClosureMu visitable
      System.out.println("visit the term closure_topdown"); 
      `TopDown(Print()).visitLight(closure_topdown);

    } catch(VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }
 
  }

}
