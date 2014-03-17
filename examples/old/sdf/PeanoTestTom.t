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

import aterm.*;
import aterm.pure.*;
import java.util.*;

public class PeanoTestTom {
  private ATermFactory factory;

  private AFun fzero, fsuc, fplus,ffib;
  public ATermAppl tzero;

  %typeterm Nat {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op Nat Nat_ConsZero() {
    fsym { fzero }
    make() { factory.makeAppl(fzero) }
  }
  
  %op Nat Nat_ConsSuc(pred:Nat) {
    fsym { fsuc }
    make(t) { factory.makeAppl(fsuc,t) }
  }

  public PeanoTestTom(ATermFactory factory) {
    this.factory = factory;

    fzero = factory.makeAFun("zero", 0, false);
    fsuc  = factory.makeAFun("suc" , 1, false);
    tzero = factory.makeAppl(fzero);

  }

  public ATermFactory getPeanoFactory() {
    return factory;
  }
  
  public void run(int loop, int n) {
    ATerm N = `Nat_ConsZero();
    for(int i=0 ; i<n ; i++) {
      N = `Nat_ConsSuc(N);
    }

    long start = System.currentTimeMillis();
    ATerm res = null;
    for(int i=0 ; i<loop; i++) {
      res = fib(N);
    }
    long end   = System.currentTimeMillis();

    System.out.println(loop + " x fib(" + n + ") in  " + (end-start) + " ms");
    // System.out.println(res);
    System.out.println(factory);
  }

  public final static void main(String[] args) {
    PeanoTestTom test = new PeanoTestTom(new PureFactory());
    System.err.println("beginning");
    test.run(10,17);
  }

  public ATerm plus(ATerm t1, ATerm t2) {
    %match(Nat t1, Nat t2) {
      x,Nat_ConsZero         -> { return x; }
      x,Nat_ConsSuc[pred=y]  -> { return `Nat_ConsSuc(plus(x,y)); }
    }
    return null;
  }

  public ATerm fib(ATerm t) {
    %match(Nat t) {
      Nat_ConsZero     -> { return`Nat_ConsSuc(Nat_ConsZero); }
      pred@Nat_ConsSuc[pred=Nat_ConsZero]   -> { return pred; }
      Nat_ConsSuc[pred=pred@Nat_ConsSuc[pred=x]] -> { return plus(fib(x),fib(pred)); }
    }
    return null;
  }
 
}

