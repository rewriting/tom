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
  get_fun_sym(t)      { t->fs }
  cmp_fun_sym(t1,t2)  { t1 == t2 }
  get_subterm(t, n)   { t->subt[n] }
}

%op term zero {
  fsym { ZERO }
}
  
%op term suc(term) {
  fsym { SUC }
}

%op term plus(term,term) {
  fsym { PLUS }
}

%op term fib(term) {
  fsym { FIB }
}

struct term *plus(struct term *t1, struct term *t2) {
  %match(term t1, term t2) {
    x,zero   -> { return x; }
    x,suc(y) -> { return suc(plus(x,y)); }
  }
}

struct term *fib(struct term *t) {
  %match(term t) {
    zero        -> { return suc(zero); }
    suc(zero)   -> { return suc(zero); }
    suc(suc(x)) -> { return plus(fib(x),fib(suc(x))); }
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
