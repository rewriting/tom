/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
 *  - Neither the name of the INRIA nor the names of its
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
package strategycompiler;

import strategycompiler.benchgomgc.spec.types.*;

import tom.library.sl.*;
import strategycompiler.StrategyCompiler;

class BenchGomGC {

  %include { sl.tom }
  %gom {
    module spec
    abstract syntax
    Nat = | Zero()
          | Suc(n:Nat)
          | C(n1:Nat,n2:Nat)
          | F(n1:Nat,n2:Nat,n3:Nat,n4:Nat,n5:Nat)
          | M()
          | N()
          | P()
  }

  public static Nat base = `Zero();

  %strategy Rewrite() extends Identity() {
    visit Nat {
      M() -> { return `Suc(Zero()); }
      N() -> { return `Suc(Suc(Suc(Zero()))); }
      P() -> { return strategycompiler.BenchGomGC.base; }
      //N() -> { return `Suc(Suc(Suc(Zero()))); }
      //P() -> { return `Suc(Suc(Suc(Suc(Zero())))); }
      C(Zero(),y) -> { return `y; }
      C(Suc(x),y) -> { return `Suc(C(x,y)); }
      F(x,y,Suc(z),t,u) -> { return `F(x,y,z,C(t,t),u); } 
      F(x,Suc(y),Zero(),t,u) -> { return `F(x,y,P(),t,t); } 
      F(Suc(x),Zero(),Zero(),u,u) -> {
        return `F(x,N(),P(),Suc(Zero()),Zero());
      }
      F(Zero(),Zero(),Zero(),t,_) -> { return `t; } 

    }
  }

  public static void main(String[] args) {
    
    int intbase = 0;
    int count = 0;
    try {
      intbase = Integer.parseInt(args[0]);
      count = Integer.parseInt(args[1]);
    } catch (Exception e) {
      System.out.println("Usage: java strategycompiler.BenchGomGC <base> <count>");
      return;
    }
    base = buildInt(intbase);

    s = `BottomUp(Rewrite());
    long startChrono = System.currentTimeMillis();
    cs = StrategyCompiler.compile(s, "cs");
    long stopChrono = System.currentTimeMillis();

    System.out.println("base:\t" + intbase + "\nstrategy compilation time:\t" + (stopChrono-startChrono)/1000.);

    run(`F(M(),N(),P(),Zero(),Suc(Zero())), count);
    run(`F(M(),N(),F(N(),P(),Zero(),N(),Zero()),Zero(),Suc(Zero())), count);
    run(`C(F(N(), Zero(), C(M(), N()), P(), Suc(N())), C(N(), P())), count);
    run(`F(C(M(), C(N(), Suc(Suc(N())))), N(), C(P(), Suc(Zero())), Suc(Suc(Zero())), Zero()), count);
    run(`F(N(), M(), Suc(C(Suc(P()), N())), M(), C(Suc(N()), N())), count);
    run(`F(P(), Suc(P()), Zero(), N(), P()), count);
  }

  private static Strategy s = null;
  private static Strategy cs = null;

  private static void run(Nat subject, int count) {
    System.out.println("\n" + subject);

    System.out.println("mutraveler not compiled:");
    for(int i = 0; i < count; ++i) {
      Nat pre = null;
      Nat post = subject;

      long startChrono = System.currentTimeMillis();
      while (post != pre) {
        pre = post;
        try {
          post = (Nat)s.visitLight(post);
        } catch(VisitFailure e) {}
      }
      long stopChrono = System.currentTimeMillis();

      System.out.println((stopChrono-startChrono)/1000.);
    }

    System.out.println("mutraveler compiled:");
    for(int i = 0; i < count; ++i) {
      Nat pre = null;
      Nat post = subject;

      long startChrono = System.currentTimeMillis();
      while (post != pre) {
        pre = post;
        try {
          post = (Nat)cs.visitLight(post);
        } catch(VisitFailure e) {}
      }
      long stopChrono = System.currentTimeMillis();

      System.out.println((stopChrono-startChrono)/1000.);
    }
  }

  public static Nat buildInt(int i) {
    Nat res = `Zero();
    for(int j=0; j<i; j++) {
      res = `Suc(res);
    }
    return res;
  }
}
