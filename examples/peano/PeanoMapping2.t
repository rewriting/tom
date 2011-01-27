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

package peano;

import aterm.*;
import aterm.pure.*;

public class PeanoMapping2 {

  static AFun fzero = SingletonFactory.getInstance().makeAFun("zero", 0, false);
  static AFun fsuc = SingletonFactory.getInstance().makeAFun("suc" , 1, false);
  static ATermAppl tzero = SingletonFactory.getInstance().makeAppl(fzero);

  %typeterm term {
    implement { ATerm }
    is_sort(t) { $t instanceof ATerm }
  }

  %op term zero() {
    is_fsym(t) { (((ATermAppl)$t).getAFun())==fzero }
  }
  
  %op term suc(p:term) {
    is_fsym(t) { (((ATermAppl)$t).getAFun())==fsuc }
    get_slot(p,t) { (((ATermAppl)$t).getArgument(0)) }
  }

  public static ATermAppl suc(ATermAppl t) {
    return SingletonFactory.getInstance().makeAppl(fsuc,t);
  }
  
  public static ATermAppl plus(ATermAppl t1, ATermAppl t2) {
    ATermAppl x =t1;
    %match(t2) {
      zero() -> { return (ATermAppl) `x; }
      suc(y) -> { return suc(plus((ATermAppl) `x,(ATermAppl) `y)); }
    }
    return null;
  }

  public final static void main(String[] args) {
    int n = 10;
    ATermAppl N = tzero;
    for(int i=0 ; i<n ; i++) {
      N = suc(N);
    }
    ATermAppl res = plus(N,N);
    System.out.println("plus(" + n + "," + n + ") = " + res);
  }
}
