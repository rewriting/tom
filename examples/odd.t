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

ATerm odd(ATerm t) {
  %match(term t) {
    zero        -> { return ATtrue; }
    suc(zero)   -> { return ATfalse; }
    suc(x)      -> { return not(odd(x)); }
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

int main(int argc, char **argv) {
  ATerm     bottomOfStack;
  int n;
  ATerm res;
  int i;

  ATinit(argc, argv, &bottomOfStack);
  
  f_zero = ATmakeAFun("zero", 0, ATfalse);
  f_suc  = ATmakeAFun("suc", 1, ATfalse);

  ATprotectAFun(f_zero);
  ATprotectAFun(f_suc);

  res = odd(buildPeano(10));
  ATprintf("odd(%d) = %t\n", res);
}
