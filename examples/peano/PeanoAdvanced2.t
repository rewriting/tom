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

package peano;

import aterm.*;
import aterm.pure.*;

public class PeanoAdvanced2 {
  ATermFactory factory;
  public PeanoAdvanced2(ATermFactory factory) {
    this.factory = factory;
  }

  %typeterm term {
    implement           { ATermAppl }
  }

  %op term zero {
    is_fsym(t) { t.getAFun() == factory.makeAFun("zero",0,false) }
    make       { factory.makeAppl(factory.makeAFun("zero",0,false)) }
  }
  
  %op term suc(pred:term) {
    is_fsym(t)       { t.getAFun() == factory.makeAFun("suc",1,false) }
    get_slot(pred,t) { (ATermAppl)t.getArgument(0) }
    make(t)          { factory.makeAppl(factory.makeAFun("suc",1,false),t) }
  }

  public ATermAppl plus(ATermAppl t1, ATermAppl t2) {
    %match(term t1, term t2) {
      x,zero()      -> { return `x; }
      x,suc[pred=y] -> { return `suc(plus(x,y)); }
    }
    return null;
  }

  public ATermAppl fib(ATermAppl t) {
    %match(term t) {
      y@zero ()          -> { return `suc(y); }
      y@suc(zero())      -> { return `y; }
      suc[pred=y@suc(x)] -> { return `plus(fib(x),fib(y)); }
    }
    return null;
  }
  
  public void run(int n) {
    ATermAppl N = `zero();
    for(int i=0 ; i<n ; i++) {
      N = `suc(N);
    }
    ATermAppl res = fib(N);
    System.out.println("fib(" + n + ") = " + res);
  }

  public final static void main(String[] args) {
    PeanoAdvanced2 test = new PeanoAdvanced2(new PureFactory());
    test.run(10);
  }
 

}

