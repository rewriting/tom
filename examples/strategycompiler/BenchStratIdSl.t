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

package strategycompiler;

import strategycompiler.benchstratidsl.term.*;
import strategycompiler.benchstratidsl.term.types.*;
import tom.library.sl.*;
import java.util.*;

public class BenchStratIdSl {

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

  %include { sl.tom }

  public final static void main(String[] args) {
    BenchStratIdSl test = new BenchStratIdSl();

    int heightmax_ex = 0;
    int heightmax_id = 0;
    try {
      heightmax_ex = Integer.parseInt(args[0]);
      heightmax_id = Integer.parseInt(args[1]);
    } catch (Exception e) {
      System.out.println("Usage: java strategycompiler.BenchStratId <heightmax_ex> <heightmax_id>");
      return;
    }
   
    System.out.println("Bench with the Sl library\n");
    System.out.println("RepeatId(Sequence(Innermost(RedFail()),Innermost(RedFail2())))");
    System.out.println("height\ttime");
    for (int i = 2; i<=heightmax_ex; i++) {
      test.benchInnermost(i);
    }

    System.out.println();
    System.out.println("RepeatId(Sequence(InnermostId(RedId()),InnermostId(RedId2())))");
    System.out.println("height\ttime");
    for (int i = 2; i<=heightmax_id; i++) {
      test.benchInnermostId(i);
    }
  }

  public void benchInnermost(int baobabHeight) {
    Term subject = baobab(baobabHeight);
    
    long startChrono = System.currentTimeMillis();
    try{
      `RepeatId(Sequence(Innermost(RedFail()),Innermost(RedFail2()))).visit(subject);
    }catch(VisitFailure e){
      System.out.println("Strategy failed !");
    }
    long stopChrono = System.currentTimeMillis();

    System.out.println(baobabHeight + "\t" + (stopChrono-startChrono)/1000.);
  }

  public void benchInnermostId(int baobabHeight) {
    Term subject = baobab(baobabHeight);

    long startChrono = System.currentTimeMillis();    
    try{
      `RepeatId(Sequence(InnermostId(RedId()),InnermostId(RedId2()))).visit(subject);
    }catch(VisitFailure e){
      System.out.println("Strategy failed !");
    }
    long stopChrono = System.currentTimeMillis();

    System.out.println(baobabHeight + "\t" + (stopChrono-startChrono)/1000.);
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

  %strategy RedFail2() extends `Fail() {
    visit Term {
      f(c(),c()) -> { return `g(a()); }
    }
  }

  %strategy RedId() extends `Identity() {
    visit Term { 
      a() -> { return `b(); }
      b() -> { return `c(); }
      g(c()) -> { return `c(); }
    }
  }

  %strategy RedId2() extends `Identity() {
    visit Term {
      f(c(),c()) -> { return `g(a()); }
    }
  }
}
