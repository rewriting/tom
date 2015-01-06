/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
package gomterm;

import gomterm.gomgc.spec.types.*;

class GomGC {

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
      P() -> { return gomterm.GomGC.base; }
      //N() -> { return `Suc(Suc(Suc(Zero()))); }
      //P() -> { return `Suc(Suc(Suc(Suc(Zero())))); }
      C(Zero(),y) -> { return `y; }
      C(Suc(x),y) -> { return `Suc(C(x,y)); }
      F(x,y,Suc(z),t,u) -> { return `F(x,y,z,C(t,t),u); } 
      F(x,Suc(y),Zero(),t,u) -> { return `F(x,y,P(),t,t); } 
      F(Suc(x),Zero(),Zero(),t,u) -> {
        return `F(x,N(),P(),Suc(Zero()),Zero());
      }
      F(Zero(),Zero(),Zero(),t,_) -> { return `t; } 

    }
  }

  public static void main(String[] args) throws tom.library.sl.VisitFailure {
    
    int intbase = 0;
    try {
      intbase = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java gom.GomGC <base>");
      return;
    }
    base = buildInt(intbase);
    long startChrono = System.currentTimeMillis();
    Nat pre = null;
    Nat post = `F(M(),N(),P(),Zero(),Suc(Zero()));
    while (post != pre) {
      pre = post;
      post = (Nat) `BottomUp(Rewrite()).visit(post);
    }
    System.out.println(intbase+" \t "+
        (System.currentTimeMillis()-startChrono)/1000.);
  }

  public static Nat buildInt(int i) {
    Nat res = `Zero();
    for(int j=0; j<i; j++) {
      res = `Suc(res);
    }
    return res;
  }
}
