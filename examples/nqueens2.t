/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2001  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
    Christophe Ringeissen	e-mail: Christophe.Ringeissen@loria.fr
    Marian Vittek		e-mail: vittek@guma.ii.fmph.uniba.sk

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

%typeterm Nat {
  implement           { ATermInt }
  get_fun_sym(t)      { ((t==tzero)?f_zero:f_suc) }
  cmp_fun_sym(s1,s2)  { s1 == s2 }
  get_subterm(t, n)   { ATmakeInt(ATgetInt(t)-1) }
}

%op Nat zero {
  fsym { f_zero }
}
  
%op Nat suc(Nat) {
  fsym { f_suc }
}

%typeterm Term {
  implement           { ATerm }
  get_fun_sym(t)      { ATgetAFun(t) }
  cmp_fun_sym(s1,s2)  { s1 == s2 }
  get_subterm(t, n)   { ATgetArgument(t,n) }
}

%op Term nqueens(Nat,Term) {
  fsym { f_nqueens }
}

%op Term empty {
  fsym { f_empty }
}

%op Term cons(Nat,Term) {
  fsym { f_cons }
}

#define empty()             ((ATerm)ATmakeAppl0(f_empty))
#define cons(e,l)           ((ATerm)ATmakeAppl2(f_cons,e,l))
#define TRUE                (0==0)
#define FALSE               (!TRUE)
#define make_nqueens(n,s)   ((ATerm)ATmakeAppl2(f_nqueens,(ATerm)n,s))
#define i2t(n)              ((ATerm)ATmakeInt(n))

ATerm range_strat(ATerm subject) {
  ATerm res=subject;
  
  /* iterate(dk(range-rule)) */
  while(1) {
    if(!setChoicePoint()) {
      break;
    }
    /* Apply the strategy */
    %match(Nat subject) {
      suc(N) -> {
        if(ATgetInt(N) > 0) {
          res = (ATerm)N;
          goto stratLab;
        }
      }
    }
    fail();
    stratLab:;
    subject=res;
  }
  end:
  return res;
}

int noattack(ATerm arg0, ATerm arg1, ATerm arg2) {
  int n1,n2,x;
  int res = FALSE;
  
  %match(Term arg0, Term arg1, Term arg2) {
    N1,N2,empty -> {
      res = TRUE;
      goto end;
    }
    
    N1,N2,cons(X,LX) -> {
      n1 = ATgetInt((ATermInt)N1);
      n2 = ATgetInt((ATermInt)N2);
      x  = ATgetInt((ATermInt)X);
      res = n2!=x && n2-x!=n1 && x-n2!=n1 && noattack(i2t(n1+1),i2t(n2),LX);
      goto end;
    }
  }
  end:
  return res;
}

ATerm nqueens_strat(ATerm arg0) {
  ATerm v0=arg0;
  ATerm res=v0;
  ATerm X;
  ATerm LX;

    /* dk(nqueens-rule-0,nqueens-rule-N) */
  %match(Term v0) {
    nqueens(zero,size) -> {
      res = empty();
      goto stratLab;
    }

    nqueens(suc(N),size) -> {
      if(localSetChoicePoint()) {
          /* local evaluations failed, try next rule */
        goto myend;
      }

      LX = nqueens_strat(make_nqueens(N,size));
      X  = (ATerm)range_strat((ATerm)size);
      
      if(noattack(i2t(1),X,LX)) {
        res = cons(X,LX);
        goto stratLab;
      } else {
        fail();
      }
      myend:
    }
  }
  fail();
  stratLab:;

  end:
  return res;
} 
 
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

#define start_nqueens(N) nqueens_strat(make_nqueens(i2t(N),i2t(N)))

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
  
  tzero  = ATmakeInt(0);
  ATprotectAFun(f_zero);
  ATprotectAFun(f_suc);
  ATprotectAFun(f_nqueens);
  ATprotectAFun(f_empty);
  ATprotectAFun(f_cons);

  ATprotect((ATerm*)&tzero);

  if(!setChoicePoint()) {

      //res = range_strat((ATerm)ATmakeInt(4));
    res = start_nqueens(8);
    ATprintf("res = %t\n", res);

    fail();
  }

  printf("The End\n");
  
}
