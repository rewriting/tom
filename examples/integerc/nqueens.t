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
#include <choice.h>

static AFun f_zero;
static AFun f_suc;
static AFun f_nqueens;
static AFun f_empty;
static AFun f_cons;

static ATermInt tzero;
static ATermInt tone;

%typeterm Nat {
  implement           { ATermInt }
  is_sort(t) { (0==0) }
}

%op Nat zero() {
  is_fsym(t)     { t==tzero }
}
  
%op Nat suc(sl:Nat) {
  is_fsym(t)     { t!=tzero }
  get_slot(sl,t) { ATmakeInt(ATgetInt(t)-1) }
}

%typeterm Term {
  implement           { ATerm }
  is_sort(t) { (0==0) }
}

%op Term nqueens(sl:Nat,slt:Term) {
  is_fsym(t)          { (ATgetAFun(t) == f_nqueens) }
  get_slot(sl, t)     { ATgetArgument(t,0) }
  get_slot(slt, t)    { ATgetArgument(t,1) }
}

%op Term empty() {
  is_fsym(t) { (ATgetAFun(t) == f_empty) }
}

%op Term cons(sl:Nat,slt:Term) {
  is_fsym(t) { (ATgetAFun(t) == f_cons) }
  get_slot(sl, t)     { ATgetArgument(t,0) }
  get_slot(slt, t)    { ATgetArgument(t,1) }
}

#define empty()             ((ATerm)ATmakeAppl0(f_empty))
#define cons(e,l)           ((ATerm)ATmakeAppl2(f_cons,e,l))
#define nqueens(n,s)        ((ATerm)ATmakeAppl2(f_nqueens,(ATerm)n,s))

#define TRUE                (0==0)
#define FALSE               (!TRUE)
#define i2t(n)              ((ATerm)ATmakeInt(n))
#define t2i(t)              (ATgetInt((ATermInt)t))
#define plus(t1,t2)         i2t(t2i(t1)+t2i(t2))
#define minus(t1,t2)        i2t(t2i(t1)-t2i(t2))
#define neq(t1,t2)          ((ATerm)t1 != (ATerm)t2)


ATerm range_rule(ATerm arg0);
ATerm range_strat(ATerm arg0);
ATerm nqueens_rule(ATerm arg0);
ATerm nqueens_strat(ATerm arg0);
int   noattack(ATerm arg0, ATerm arg1, ATerm arg2);

//------------------------------------------------------------

int noattack(ATerm arg0, ATerm arg1, ATerm arg2) {
  int res = FALSE;
  
  %match(Term arg0, Term arg1, Term arg2) {
    _,_,empty() -> {
      res = TRUE;
      goto end;
    }
    
    N1,N2,cons(X,LX) -> {
      res =
        neq(`N2,`X) &&
        neq(minus(`N2,`X),`N1) &&
        neq(minus(`X,`N2),`N1) &&
        noattack(plus(`N1,tone),`N2,`LX);
      goto end;
    }
  }
  end:
  return res;
}


ATerm range_rule(ATerm arg0) {
  ATerm res = arg0;
  
  %match(Nat res) {
    N -> {
      if(t2i(`N) > 1) {
        res = minus(`N,tone);
        goto end;
      }
    }
  }
  fail();
  end:
  return res;
}

ATerm nqueens_rule(ATerm arg0) {
  ATerm res = arg0;
  ATerm X;
  ATerm LX;

  %match(Term res) {
    nqueens(zero(),_) -> {
      res = empty();
      goto end;
    }

    nqueens(N,size) -> {
      if(localSetChoicePoint()) {
          /* local evaluations failed, try next rule */
        goto myend;
      }

      if(t2i(`N) > 0) {
        LX = nqueens_strat(nqueens(minus(`N,tone),`size));
        X  = (ATerm)range_strat((ATerm)`size);
      
        if(noattack((ATerm)tone,X,LX)) {
          res = cons(X,LX);
          goto end;
        }
      }
    }
  }
myend:
  fail();
  end:
  return res;
} 

ATerm range_strat(ATerm arg0) {
  ATerm res = arg0;
  
  /* iterate(dk(range-rule)) */
  while(1) {
    if(!setChoicePoint()) {
      break;
    }
    res = range_rule(res);
  }
  end:
  return res;
}

ATerm nqueens_strat(ATerm arg0) {
  ATerm res = arg0;

    /* dk(nqueens-rule-0,nqueens-rule-N) */
  res = nqueens_rule(res);
  end:
  return res;
} 

#define start_nqueens(N) nqueens_strat(nqueens(i2t(N),i2t(N)))

//------------------------------------------------------------

void *at_malloc_protect(int size) {  
  void *start = malloc(size);  
  ATprotectMemory(start,size);
  return start;
}

void *at_realloc_protect(char *old,int size) {
  void *start = realloc(old,size);
  ATunprotectMemory(old);
  ATprotectMemory(start,size);
  return start;
}


int main(int argc, char **argv) {
  ATerm     bottomOfStack;
  int n;
  ATerm res;
  
  ATinit(argc, argv, &bottomOfStack);

  CPL_init_malloc_protect(at_malloc_protect);
  CPL_init_malloc(malloc);
  CPL_init_realloc_protect(at_realloc_protect);
  CPL_init_realloc(realloc);
  choice_init((long*)&bottomOfStack);
 
  f_zero    = ATmakeAFun("zero", 0, ATfalse);
  f_suc     = ATmakeAFun("suc", 1, ATfalse);
  f_nqueens = ATmakeAFun("nqueens", 2, ATfalse);
  f_empty   = ATmakeAFun("empty", 0, ATfalse);
  f_cons    = ATmakeAFun("cons", 2, ATfalse);
  
  ATprotectAFun(f_zero);
  ATprotectAFun(f_suc);
  ATprotectAFun(f_nqueens);
  ATprotectAFun(f_empty);
  ATprotectAFun(f_cons);

  tzero = ATmakeInt(0);
  tone  = ATmakeInt(1);
  ATprotect((ATerm*)&tzero);
  ATprotect((ATerm*)&tone);

  if(!setChoicePoint()) {

    //res = range_strat((ATerm)ATmakeInt(4));
    res = start_nqueens(12);
    //ATprintf("res = %t\n", res);

    fail();
  }

  printf("The End\n");
  
}
