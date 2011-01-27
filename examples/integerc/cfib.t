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

/*
 * This is an example on computing n-th fib number on peano arithmetics
 */

#include<stdio.h>
#include<stdlib.h>

#define ZERO 0
#define SUC 1
#define PLUS 2
#define FIB 3

struct term {
  int fs;
  int arity;
  struct term **subt;
};

struct term s_zero = {ZERO, 0, NULL};
struct term *zero  = &s_zero;

struct term *suc(struct term *x) {
  struct term *res;
  res = malloc(sizeof(struct term));
  res->fs = SUC;
  res->arity = 1;
  res->subt = malloc(sizeof(struct term *));
  res->subt[0] = x;
  return(res);
}

%typeterm term {
  implement { struct term* }
  is_sort(t) { (0==0) }
}

%op term zero() {
  is_fsym(t) { t->fs == ZERO }
}
  
%op term suc(sl:term) {
  is_fsym(t)      { t->fs == SUC }
  get_slot(sl, t) { t->subt[0] }
}

%op term plus(sl1:term,sl2:term) {
  is_fsym(t)       { t->fs == PLUS }
  get_slot(sl1, t) { t->subt[0] }
  get_slot(sl2, t) { t->subt[1] }
}

%op term fib(sl:term) {
  is_fsym(t)      { t->fs == FIB }
  get_slot(sl, t) { t->subt[0] }
}

struct term *plus(struct term *t1, struct term *t2) {
  %match(term t1, term t2) {
    x,zero() -> { return `x; }
    x,suc(y) -> { return suc(plus(`x,`y)); }
  }
}

struct term *fib(struct term *t) {
  %match(term t) {
    zero()      -> { return suc(zero); }
    suc(zero()) -> { return suc(zero); }
    suc(suc(x)) -> { return plus(fib(`x),fib(suc(`x))); }
  }
}

struct term *buildPeano(int n) {
  int i;
  struct term  *t;
  t = zero;
  for(i=0; i<n; i++) {
    t = suc(t);
  }
  return t;
}

void printTerm(struct term *tt) {
  int i;
  static char * fsnames[] = {"zero", "suc", "plus", "fib"};
  printf("%s", fsnames[tt->fs]);
  if (tt->arity!=0) {
    printf("(");
    for(i=0; i<tt->arity; i++) {
      printTerm(tt->subt[i]);
      if (i+1 != tt->arity) printf(",");
    }
    printf(")");
  }
}

int main() {
  printTerm(fib(buildPeano(10)));
  printf("\n");
}
