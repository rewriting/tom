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

#include<stdio.h>
#include<stdlib.h>

#include <aterm1.h>
#include <aterm2.h>

static AFun f_zero;
static AFun f_suc;
static AFun f_plus;
static AFun f_fib;

#define make_zero() ((ATerm)ATmakeAppl0(f_zero))
#define make_suc(x) ((ATerm)ATmakeAppl1(f_suc, x))

%typeterm term {
  implement           { ATerm }
  is_sort(t) { (0==0) }
}

%op term zero() {
  is_fsym(t)     { ATisEqualAFun(ATgetAFun(t),f_zero) }
  make           { make_zero() }
}
  
%op term suc(sl:term) {
  is_fsym(t)     { ATisEqualAFun(ATgetAFun(t),f_suc) }
  get_slot(sl,t) { ATgetArgument(t,0) }
  make(t1)       { make_suc(t1) }
}

ATerm plus(ATerm t1, ATerm t2);
%op term plus(sl1:term,sl2:term) {
  is_fsym(t) { ATisEqualAFun(ATgetAFun(t),f_plus) }
  get_slot(sl1,t) { ATgetArgument(t,0) }
  get_slot(sl2,t) { ATgetArgument(t,1) }
  make(t1,t2)     { plus(t1,t2) }
}

ATerm fib(ATerm t);
%op term fib(sl:term) {
  is_fsym(t) { ATisEqualAFun(ATgetAFun(t),f_fib) }
  get_slot(sl,t) { ATgetArgument(t,0) }
  make(t1)       { fib(t1) }
}

ATerm plus(ATerm t1, ATerm t2) {
  %match(term t1, term t2) {
    x,zero() -> { return(`x); }
    x,suc(suc(suc(suc(suc(y))))) -> {
      return(`suc(suc(suc(suc(suc(plus(x,y)))))));
    }
    x,suc(y) -> { return(`suc(plus(x,y))); }
  }
}

ATerm fib(ATerm t) {
  %match(term t) {
    zero()      -> { return(`suc(zero())); }
    suc(zero()) -> { return(`suc(zero())); }
    suc(suc(x)) -> { return(`plus(fib(x),fib(suc(x)))); }
  }
}

ATerm buildPeano(int n) {
  int i;
  ATerm t;
  t = make_zero();
  for(i=0; i<n; i++) {
    t = make_suc(t);
  }
  return t;
}

static void symbolicFib(int n) {
  ATprintf("fib %d = %t\n", n, fib(buildPeano(n)));
}

int main(int argc, char **argv) {
  ATerm     bottomOfStack;
  int n;
  ATerm res;
  int i;

  ATinit(argc, argv, &bottomOfStack);
  
  f_zero = ATmakeAFun("zero", 0, ATfalse);
  f_suc  = ATmakeAFun("suc", 1, ATfalse);
  f_fib  = ATmakeAFun("fib", 1, ATfalse);
  f_plus = ATmakeAFun("plus", 2, ATfalse);

  ATprotectAFun(f_zero);
  ATprotectAFun(f_suc);
  ATprotectAFun(f_fib);
  ATprotectAFun(f_plus);

  for(i=0 ; i<30 ; i++) {
    n = 25;
    res = fib(buildPeano(n));
  }
   // ATprintf("fib %d = %t\n", n, res);
}
