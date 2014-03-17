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

package gom;

import gom.stratidsl.term.*;
import gom.stratidsl.term.types.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import jjtraveler.Visitable;
import tom.library.sl.*;

import java.util.*;

public class StratIdSl {

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
  %include { sl.tom }

  public final static void main(String[] args) {
    StratIdSl test = new StratIdSl();

    int heightmax = 0;
    try {
      heightmax = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java gom.StratId <heightmax>");
      return;
    }
    System.out.println("visit()");
    for (int i = 2; i<=heightmax; i++) {
      test.benchInnermostOld(i,10);
    }
    System.out.println();
    System.out.println("fire() with failure");
    for (int i = 2; i<=heightmax; i++) {
      test.benchInnermost(i,10);
    }
    System.out.println();
    System.out.println("fire() with id");
    for (int i = 2; i<=heightmax; i++) {
      test.benchInnermostId(i,10);
    }
  }

  public void benchInnermostOld(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print(baobabHeight);
    long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      try{
        `Innermost(RedFail()).visit(subject);
      }catch(jjtraveler.VisitFailure e){
        System.out.println("failure");
      }   
    }
    long stopChrono = System.currentTimeMillis();
    System.out.println("\t" + (stopChrono-startChrono)/1000.);
  }


  public void benchInnermost(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print(baobabHeight);
    long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      `Innermost(RedFail()).fire(subject);
    }
    long stopChrono = System.currentTimeMillis();
    System.out.println("\t" + (stopChrono-startChrono)/1000.);
  }

  public void benchInnermostId(int baobabHeight, int count) {
    Term subject = baobab(baobabHeight);
    System.out.print(baobabHeight);
    long startChrono = System.currentTimeMillis();
    for(int i=0; i<count; i++) {
      `InnermostId(RedId()).fire(subject);
    }
    long stopChrono = System.currentTimeMillis();
    System.out.println("\t" + (stopChrono-startChrono)/1000.);
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
