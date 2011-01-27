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

package strategy.graph;

import strategy.graph.rewrite1.term.*;
import strategy.graph.rewrite1.term.types.*;

import tom.library.sl.*;

public class Rewrite1 {

%include { string.tom }
%include { sl.tom }
%gom(--withCongruenceStrategies) {  
  module term
  abstract syntax
  Term = a() | b() | c() | d()
       | f(arg1:Term)
       | g(arg1:Term, arg2:Term)
       | h(arg1:Term, arg2:Term, arg3:Term)
}
//%include { rewrite1/term/_term.tom }

public final static void main(String[] args) {
  try {
    Visitable subject = `f(a());
    Strategy s = `Identity();
    subject = s.visit(subject);
    System.out.println("root = " + subject);

    s = `(R1());
    subject = `s.visit(subject);
    System.out.println("root = " + subject);

    s = `One((R1()));
    subject = `s.visit(subject);
    System.out.println("root = " + subject);

    System.out.println("----------");
    s = `OnceBottomUp((R2()));
    subject = `s.visit(`g(f(a()),b()));
    System.out.println("root1 = " + subject);
    subject = `s.visit(subject);
    System.out.println("root2 = " + subject);
    subject = `s.visit(subject);
    System.out.println("root3 = " + subject);

    System.out.println("----------");
    s = `All((R2()));
    subject = `s.visit(`g(f(a()),b()));
    System.out.println("root1 = " + subject);

    System.out.println("----------");
    s = `Sequence(All(R2()),All(All(R1())));
    subject = `s.visit(`g(f(a()),b()));
    System.out.println("root1 = " + subject);

    System.out.println("----------Omega(1)");
    s = `Omega(1,R2());
    subject = `s.visit(`g(f(a()),b()));
    System.out.println("root1 = " + subject);

    System.out.println("----------Omega(2)");
    s = `Omega(2,R2());
    subject = `s.visit(`g(f(a()),b()));
    System.out.println("root1 = " + subject);

    System.out.println("----------IfThenElse");
    s = `IfThenElse(R2(),Builda(),Buildb());
    subject = `s.visit(`f(b()));
    System.out.println("root1 = " + subject);
    subject = `s.visit(`f(a()));
    System.out.println("root1 = " + subject);

    System.out.println("----------When_f()");
    s = `When_f(R2());
    subject = `s.visit(`f(a()));
    System.out.println("root1 = " + subject);

    System.out.println("----------IfThenElse...Make_?");
    s = `IfThenElse(R2(),Make_a(),Make_b());
    subject = `s.visit(`f(b()));
    System.out.println("root1 = " + subject);
    subject = `s.visit(`f(a()));
    System.out.println("root1 = " + subject);

    System.out.println("----------_f()");
    s = `_g(R2(),R1());
    subject = `s.visit(`g(f(a()),b()));
    System.out.println("root1 = " + subject);
  } catch(VisitFailure e) {
    System.out.println("Failure");
  }
}

  %strategy R1() extends `Identity() {
    visit Term {
      f(a()) -> { return `f(b()); }
      b() -> { return `c(); }
    }
  }

  %strategy R2() extends `Fail() {
    visit Term {
      f(a()) -> { return `f(b()); }
      b() -> { return `c(); }
    }
  }
  %strategy Builda() extends `Identity() {
    visit Term {
      _ -> { return `a(); }
    }
  }
  %strategy Buildb() extends `Identity() {
    visit Term {
      _ -> { return `b(); }
    }
  }
} 
