#include<stdio.h>
#include<stdlib.h>

#include "Peano.h"

%include { Peano.t }


Nat plus(Nat t1, Nat t2) {
  %match(Nat t1, Nat t2) {
    x,consZero         -> { return x; }
    x,consSuc[pred=y]  -> { return `consSuc(plus(x,y)); }
  }
}

Nat fib(Nat t) {
  %match(Nat t) {
    consZero                           -> { return `consSuc(consZero); }
    pred@consSuc[pred=consZero]        -> { return pred; }
    consSuc[pred=pred@consSuc[pred=x]] -> { return plus(fib(x),fib(pred)); }
  }
}

void run(int loop, int n) {
  int i;
  Nat res;
  Nat N = makeNatConsZero();
  for(i=0 ; i<n ; i++) {
    N = makeNatConsSuc(N);
  }
  
  for(i=0 ; i<loop; i++) {
    res = fib(N);
  }

  ATprintf("res = %t\n",NatToTerm(res));
}

int main(int argc, char **argv) {
  ATerm bottomOfStack;

  ATinit(argc, argv, &bottomOfStack);
  initPeanoApi();

  run(1,10);
  
  return 0;
}
