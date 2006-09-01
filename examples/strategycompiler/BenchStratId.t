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

package strategycompiler;

import strategycompiler.benchstratid.term.*;
import strategycompiler.benchstratid.term.types.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import jjtraveler.Visitable;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.AbstractMuStrategy;

import tom.library.strategy.mutraveler.MuStrategy;

import java.util.*;

public class BenchStratId {

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
  %include { mustrategy.tom }

  public final static void main(String[] args) {
    BenchStratId test = new BenchStratId();

    int heightmax = 0;
    try {
      heightmax = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java gombench.BenchStratId <heightmax>");
      return;
    }

    MuStrategy s1 = `Innermost(RedFail());
    MuStrategy s2 = `InnermostId(RedId());
    MuStrategy c_s1 = StrategyCompiler.compile(s1, "c_s1");
    MuStrategy c_s2 = StrategyCompiler.compile(s2, "c_s2");

    System.out.println("Innermost(RedFail()):");
    System.out.println("\tnot compiled\tcompiled\n");
    for (int i = 2; i<=heightmax; i++) {
      test.bench(i,10, s1, c_s1);
    }
    System.out.println();

    System.out.println("InnermostId(RedId()):");
    System.out.println("\tnot compiled\tcompiled\n");
    for (int i = 2; i<=heightmax; i++) {
      test.bench(i,10, s2, c_s2);
    }
    System.out.println();
  }

  public void bench(int baobabHeight, int count, MuStrategy s, MuStrategy compiledS) {
    Term subject = baobab(baobabHeight);
    System.out.print(baobabHeight);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      s.apply(subject);
    }
		long stopChrono = System.currentTimeMillis();

    System.out.print("\t" + ((stopChrono-startChrono)/1000.));

		startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      compiledS.apply(subject);
    }
		stopChrono = System.currentTimeMillis();

		System.out.println("\t\t" + (stopChrono-startChrono)/1000.);
  }

  Term baobab(int height) {
    if (height < 1) {
      return `g(a());
    } else {
      Term sub = baobab(height-1);
      return `f(sub,sub);
    }
  }

  %strategy RedFail() extends `Fail() {
    visit Term { 
      a() -> { return `b(); }
      b() -> { return `c(); }
      g(c()) -> { return `c(); }
    }
  }

  %strategy RedId() extends `Identity() {
    visit Term { 
      a() -> { return `b(); }
      b() -> { return `c(); }
      g(c()) -> { return `c(); }
    }
  }
}
