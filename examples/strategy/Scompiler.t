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

package strategy;

import strategy.scompiler.term.*;
import strategy.scompiler.term.types.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import jjtraveler.Visitable;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;

import java.util.*;

public class Scompiler {

  %gom {
    module Term
    abstract syntax
    Term = a()
         | b()
         | c()
         | d()
         | f(s1:Term, s2:Term)
         | g(s1:Term)
   }

  %typeterm Collection {
    implement { Collection }
  }
  %include { mutraveler.tom }

  public final static void main(String[] args) {
    Scompiler test = new Scompiler();
    test.run();
    test.benchStrat();
    test.benchCompiledStrat();
  }

  public void run() {
    Term subject = `f(f(a(),b()),f(c(),d()));
    List c = new ArrayList();
    try {
      System.out.println("subject       = " + subject);
      VisitableVisitor S1 = `BottomUp(GetPosition(c));
      S1.visit(subject);
      VisitableVisitor S2 = new CompiledBottomUp(`GetPosition(c));
      S2.visit(subject);
      System.out.println("set[pos] = " + c);
    } catch(VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }
  }

  public void benchStrat() {
    Term subject = baobab(20);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<10; i++) {
      List c = new ArrayList();
      try {
        VisitableVisitor S1 = `BottomUp(GetPosition(c));
        S1.visit(subject);
      } catch(VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("baobab " + (stopChrono-startChrono) + " ms");
  }

  public void benchCompiledStrat() {
    Term subject = baobab(20);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<10; i++) {
      List c = new ArrayList();
      try {
        VisitableVisitor S2 = new CompiledBottomUp(`GetPosition(c));
        S2.visit(subject);
      } catch(VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("compiled baobab " + (stopChrono-startChrono) + " ms");
  }

  %strategy GetPosition(c:Collection) extends `Identity() { 
    visit Term {
      x -> { c.add(`x); }
    }
  }

  //BottomUp(v) = `mu(MuVar("x"),Sequence(All(MuVar("x")),v))
  class CompiledBottomUp extends AbstractVisitableVisitor {
    protected final static int ARG = 0;
    public CompiledBottomUp(VisitableVisitor v) {
      initSubterm(v);
    }
    public Visitable visit(Visitable any) throws VisitFailure {
      // Compile All("x")
      int childCount = any.getChildCount();
      Visitable result = any;
      for (int i = 0; i < childCount; i++) {
        Visitable newChild = this.visit(result.getChildAt(i));
        result = result.setChildAt(i, newChild);
      }
      
      // Compile v
      return getArgument(ARG).visit(result);
    }
  }

  Term baobab(int height) {
    if (height < 1) {
      return `g(a());
    } else {
      Term sub = baobab(height-1);
      return `f(sub,sub);
    }
  }

}
