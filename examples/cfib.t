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

static AFun f_zero;
static AFun f_suc;
static AFun f_plus;
static AFun f_fib;

#define make_zero() ((ATerm)ATmakeAppl0(f_zero))
#define make_suc(x) ((ATerm)ATmakeAppl1(f_suc, x))

%typeterm term {
  implement           { ATerm }
  get_fun_sym(t)      { ATgetAFun(t) }
  cmp_fun_sym(s1,s2)  { ATisEqualAFun(s1,s2) }
  get_subterm(t, n)   { ATgetArgument(t,n) }
}

%op term zero {
  fsym { f_zero }
}
  
%op term suc(term) {
  fsym { f_suc }
}

%op term plus(term,term) {
  fsym { f_plus }
}

%op term fib(term) {
  fsym { f_fib }
}

ATerm plus(ATerm t1, ATerm t2) {
  %match(term t1, term t2) {
    x,zero   -> { return(x); }
    x,suc(suc(suc(suc(suc(y))))) -> {
      return(make_suc(make_suc(make_suc(make_suc(make_suc(plus(x,y)))))));
    }
    x,suc(y) -> { return(make_suc(plus(x,y))); }
  }
}

ATerm fib(ATerm t) {
  %match(term t) {
    zero        -> { return(make_suc(make_zero())); }
    suc(zero)   -> { return(make_suc(make_zero())); }
    suc(suc(x)) -> { return(plus(fib(x),fib(make_suc(x)))); }
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
    //ATprintf("fib %d = %t\n", n, res);
}
