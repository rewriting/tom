/*
 * Copyright (c) 2004-2007, INRIA
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


import VisitFailure;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.AbstractMuStrategy;

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
    is_sort(t)     { t instanceof Collection }
  }
  %include { mutraveler.tom }

  public final static void main(String[] args) {
    Scompiler test = new Scompiler();
    test.run();

    test.benchBottomUp(10,10);
    test.benchCompiledBottomUp(10,10);
    test.benchTopDown(10,10);
    test.benchCompiledTopDown(10,10);
    test.benchInnermost(10,10);
    test.benchCompiledInnermost(10,10);

    test.benchBottomUp(10,200);
    test.benchCompiledBottomUp(10,200);
    test.benchTopDown(10,200);
    test.benchCompiledTopDown(10,200);
    test.benchInnermost(10,200);
    test.benchCompiledInnermost(10,200);

    test.benchBottomUp(12,10);
    test.benchCompiledBottomUp(12,10);
    test.benchTopDown(12,10);
    test.benchCompiledTopDown(12,10);
    test.benchInnermost(12,10);
    test.benchCompiledInnermost(12,10);

    test.benchBottomUp(17,10);
    test.benchCompiledBottomUp(17,10);
    test.benchTopDown(17,10);
    test.benchCompiledTopDown(17,10);
    test.benchHardCompiledInnermost(17,10);
    test.benchInnermost(17,10);
    test.benchCompiledInnermost(17,10);

  }

  public void run() {
    Term subject = `f(f(a(),b()),f(c(),d()));
    List c = new ArrayList();
    try {
      System.out.println("subject       = " + subject);
      VisitableVisitor S1 = `BottomUp(GetPosition(c));
      S1.visitLight(subject);
      VisitableVisitor S2 = new CompiledBottomUp(`GetPosition(c));
      S2.visitLight(subject);
      System.out.println("set[pos] = " + c);

      c = new ArrayList();
      S1 = `TopDown(GetPosition(c));
      S1.visitLight(subject);
      S2 = new CompiledTopDown(`GetPosition(c));
      S2.visitLight(subject);
      System.out.println("set[pos] = " + c);

      S1 = `Innermost(Reduce());
      System.out.println("Innermost(Reduce()) : "+S1.visitLight(subject));
    } catch(VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }
  }

  public void benchBottomUp(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print("Running benchBottomUp with "+baobabHeight+" "+count);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      List c = new ArrayList();
      try {
        VisitableVisitor S1 = `BottomUp(GetPosition(c));
        S1.visitLight(subject);
      } catch(VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("...\t" + (stopChrono-startChrono) + " ms");
  }

  public void benchTopDown(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print("Running benchTopDown with "+baobabHeight+" "+count);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      List c = new ArrayList();
      try {
        VisitableVisitor S1 = `TopDown(GetPosition(c));
        S1.visitLight(subject);
      } catch(VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("...\t" + (stopChrono-startChrono) + " ms");
  }

  public void benchCompiledBottomUp(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print("Running benchCompiledBottomUp with "+baobabHeight+" "+count);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      List c = new ArrayList();
      try {
        VisitableVisitor S2 = new CompiledBottomUp(`GetPosition(c));
        S2.visitLight(subject);
      } catch(VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("...\t" + (stopChrono-startChrono) + " ms");
  }

  public void benchInnermost(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print("Running benchInnermost with "+baobabHeight+" "+count);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      List c = new ArrayList();
      try {
        VisitableVisitor S1 = `Innermost(Reduce());
        S1.visitLight(subject);
      } catch(VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("...\t" + (stopChrono-startChrono) + " ms");
  }

  public void benchCompiledInnermost(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print("Running benchCompiledInnermost with "+baobabHeight+" "+count);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      List c = new ArrayList();
      try {
        VisitableVisitor S1 = new CompiledInnermost(`Reduce());
        S1.visitLight(subject);
      } catch(VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("...\t" + (stopChrono-startChrono) + " ms");
  }

  public void benchHardCompiledInnermost(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print("Running benchHardCompiledInnermost with "+baobabHeight+" "+count);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      List c = new ArrayList();
      try {
        VisitableVisitor S1 = new HardCompiledInnermost();
        S1.visitLight(subject);
      } catch(VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("...\t" + (stopChrono-startChrono) + " ms");
  }
  public void benchCompiledTopDown(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print("Running benchCompiledTopDown with "+baobabHeight+" "+count);
		long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      List c = new ArrayList();
      try {
        VisitableVisitor S2 = new CompiledTopDown(`GetPosition(c));
        S2.visitLight(subject);
      } catch(VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("...\t" + (stopChrono-startChrono) + " ms");
  }

  %strategy GetPosition(c:Collection) extends `Identity() { 
    visit Term {
      x -> { c.add(`x); }
    }
  }

  //BottomUp(v) = `mu(MuVar("x"),Sequence(All(MuVar("x")),v))
  class CompiledBottomUp extends AbstractMuStrategy {
    protected final static int ARG = 0;
    public CompiledBottomUp(VisitableVisitor v) {
      initSubterm(v);
    }
    public Visitable visit(Visitable any) throws VisitFailure {
      // Compile All("x")
      int childCount = any.getChildCount();
      Visitable result = any;
      for (int i = 0; i < childCount; i++) {
        Visitable newChild = this.visitLight(result.getChildAt(i));
        result = result.setChildAt(i, newChild);
      }
      
      // Compile v
      return getArgument(ARG).visitLight(result);
    }
  }

  //TopDown(v) = `mu(MuVar("x"),Sequence(v,All(MuVar("x"))))
  class CompiledTopDown extends AbstractMuStrategy {
    protected final static int ARG = 0;
    public CompiledTopDown(VisitableVisitor v) {
      initSubterm(v);
    }
    public Visitable visit(Visitable any) throws VisitFailure {
      Visitable result = any;
      // Compile v
      result = getArgument(ARG).visitLight(result);

      // Compile All(MuVar("x"))
      int childCount = any.getChildCount();
      for (int i = 0; i < childCount; i++) {
        Visitable newChild = this.visitLight(result.getChildAt(i));
        result = result.setChildAt(i, newChild);
      }

      return result; 
    }
  }

  //Innermost(v) = `mu(MuVar("x"),Sequence(All(MuVar("x")),Try(Sequence(v,MuVar("x")))))
  class CompiledInnermost extends AbstractMuStrategy {
    protected final static int ARG = 0;
    public CompiledInnermost(VisitableVisitor v) {
      initSubterm(v);
    }
    public Visitable visit(Visitable any) throws VisitFailure {
      Visitable result = any;
      // Compile All("x")
      int childCount = any.getChildCount();
      for (int i = 0; i < childCount; i++) {
        Visitable newChild = this.visitLight(result.getChildAt(i));
        result = result.setChildAt(i, newChild);
      }
      // compile Try(Sequence(v,MuVar("x"))) =
      // Choice(Sequence(v,MuVar("x")),Identity())
      try {
        // Compile Sequence(v,MuVar("x"))
        result = getArgument(ARG).visitLight(result);
        result = this.visitLight(result);
      } catch (VisitFailure f) {
        // Compile Identity()
        return result;
      }
      return result; 
    }
  }

  class HardCompiledInnermost implements jjtraveler.reflective.VisitableVisitor {
    private VisitableVisitor v = new Reduce();
    public HardCompiledInnermost() { }
    public Visitable visit(Visitable any) throws VisitFailure {
      Visitable result = any;
      // Compile All("x")
      int childCount = any.getChildCount();
      for (int i = 0; i < childCount; i++) {
        Visitable newChild = this.visitLight(result.getChildAt(i));
        result = result.setChildAt(i, newChild);
      }
      // compile Try(Sequence(v,MuVar("x"))) =
      // Choice(Sequence(v,MuVar("x")),Identity())
      try {
        // Compile Sequence(v,MuVar("x"))
        result = v.visitLight(result);
        result = this.visitLight(result);
      } catch (VisitFailure f) {
        // Compile Identity()
        return result;
      }
      return result; 
    }
    public int getChildCount() { return 0; }
    public Visitable getChildAt(int i) { return null; }
    public Visitable setChildAt(int i, Visitable child) { return null; }
  }

  Term baobab(int height) {
    if (height < 1) {
      return `g(a());
    } else {
      Term sub = baobab(height-1);
      return `f(sub,sub);
    }
  }

  %strategy Reduce() extends `Fail() {
    visit Term { 
        a() -> { return `b(); }
        b() -> { return `c(); }
        g(c()) -> { return `c(); }
        f(c(),c()) -> { return `c(); }
      }
    }
}
