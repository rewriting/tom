/*
 * Copyright (c) 2004-2006, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this conc of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this conc of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the INRIA nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
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

package termgraph;

import tom.library.strategy.mutraveler.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;

import termgraph.term.*;
import termgraph.term.types.*;
import termgraph.term.strategy.term.*;
import termgraph.term.types.term.posTerm;

public class GraphTest {

  //%include {mustrategy.tom }
  %include {term/term.tom}
  %include {term/_term.tom}

  %strategy Print() extends Identity() {
    visit Term {
      x@_ -> { System.out.println(`x); }
    }
  }

  %strategy DummyStrat() extends Identity() {
    visit Term {
      a() -> { return `b(); }
      b() -> { return `a(); }
    }
  }

  %strategy MatchGraph() extends Identity() {
    visit Term {
      graph@g(g(x@a(),b()),f(mRef(x)))-> {
        System.out.println("graph pattern found at "+getPosition());
      }
    }
  }

  static Term root = null;
  %op Term mRef(ptr:Term) {
    is_fsym(t) { (t!=null) && (t instanceof posTerm) }
    get_slot(ptr, t) { (Term)(new Position(((posTerm)t).toArray())).getSubterm().apply(root) }
  }

  public static void main(String[] args){
    Term subject =
      `expTerm(f(g(g(labTerm("l1",a()),labTerm("l2",b())),f(refTerm("l1")))));
    System.out.println("Initial subject: "+subject);
    Term newSubject = (Term) `TopDown(Ref(subject,DummyStrat())).apply(subject);
    System.out.println("Subject after dummystrat: "+newSubject);
    root = subject;
    `TopDown(Ref(subject,MatchGraph())).apply(root);

    // Problems to resolve
    System.out.println("Examples of strange behaviour of pattern-matching when  using mRef\n");
    System.out.println("1. when the subject is modified inside a match");
    subject = `expTerm(g(labTerm("a",a()),refTerm("a")));
    System.out.println("%match "+subject);
    root=subject;
    %match(Term subject){
      g(x@a(),mRef(x)) -> {
        System.out.println("\tmatched with g(x@a(),mRef(x))");
        subject = `expTerm(g(labTerm("a",b()),refTerm("a")));
        System.out.println("\t\tChange subject: "+subject);
      }
      g(x@a(),mRef(x)) -> {
        System.out.println("\tmatched with g(x@a(),mRef(x))");
      }
    }

    System.out.println("\n2. in case of transitive references");
    subject =`expTerm(g(labTerm("a",a()),g(labTerm("ref",refTerm("a")),refTerm("ref"))));
    System.out.println("%match "+subject);
    root=subject;
    %match(Term subject){
      g(x@a(),g(y@mRef(x),mRef(y))) -> {
        System.out.println("\tmatched with g(x@a(),g(y@mRef(x),mRef(y)))");
      }
      /*
         !g(x@a(),g(mRef(x),mRef(x))) -> {
         System.out.println("\tnot matched with g(x@a(),g(mRef(x),mRef(x)))");
         }
       */
    }

    System.out.println("\n3. in case of identical subterms at different positions");
    subject=`expTerm(g(a(),g(refTerm("a"),labTerm("a",a()))));
    System.out.println("%match "+subject);
    root=subject;
    %match(Term subject){
      g(x@a(),g(mRef(x),a())) -> {
        System.out.println("\tmatched with g(x@a(),g(mRef(x),a()))");
      }
    }

    System.out.println("\nApply dummyStrat with relative positions");
    subject= `g(a(),g(posTerm(1,2),a()));
    System.out.println("Initial Subject: "+subject);
    root=subject;
    subject= (Term) `TopDown(RelativeRef(subject,DummyStrat())).apply(subject);
    System.out.println("After DummyStrat: "+subject);
    System.out.println("Try to reproduce the bug");
    subject = `expTerm(g(f(f(refTerm("toto"))),labTerm("toto",a())));
    try {
      MuTraveler.init(`_g(_f(One(RelativeRef(subject,Identity()))),Identity())).visit(subject);
    } catch(VisitFailure e) {
      System.out.println("Failure");
    }

  }
}
