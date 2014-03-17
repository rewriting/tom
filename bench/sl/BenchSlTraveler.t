/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
package sl;

import sl.term.types.*;
import tom.library.sl.*;

class BenchSlTraveler {

  %include { sl.tom }
  %include {term/term.tom}

  %strategy Rewrite(base:Nat) extends Identity() {
    visit Nat {
      M() -> Suc(Zero())
      N() -> Suc(Suc(Suc(Zero())))
      P() -> base
      C(Zero(),y) -> y
      C(Suc(x),y) -> Suc(C(x,y))
      F(x,y,Suc(z),t,u) -> F(x,y,z,C(t,t),u)
      F(x,Suc(y),Zero(),t,u) -> F(x,y,P(),t,t)
      F(Suc(x),Zero(),Zero(),t,u) -> F(x,N(),P(),Suc(Zero()),Zero())
      F(Zero(),Zero(),Zero(),t,_) -> t
    }
  }

  public static void run(Nat subject, int version,  int count, Nat base) {
    System.out.print("sl [visit light] : ");
    try {
      Strategy s1 = `RepeatId(BottomUp(Rewrite(base)));
      Strategy s2 = `InnermostId(Rewrite(base));
      long startChrono = System.currentTimeMillis();
      for(int i = 0; i < count; ++i) {
        if(version==1) {
          s1.visitLight(subject);
          //System.out.println(s1.visitLight(subject));
        } else {
          s2.visitLight(subject);
          //System.out.println(s2.visitLight(subject));
        }
      }
      long stopChrono = System.currentTimeMillis();
      System.out.println((stopChrono-startChrono)/1000.);
    } catch(VisitFailure e) {
      System.out.println("failure");
    }
  }

}
