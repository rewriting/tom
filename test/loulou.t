#include<stdio.h>
#include<stdlib.h>

#define ZERO  0
#define SUC   1

%typeterm Nat {
  implement { int }
}

%op Nat zero {
  is_fsym(t) { t==0 }
}

%op Nat suc(p:Nat) {
  is_fsym(t) { t>0 }
  get_slot(p,t) { t-1 }
  make(t) { t+1 }
}

int plus(int t1, int t2) {
  %match(Nat t1, Nat t2) {
    x,zero()   -> { return `x; }
    x,suc(y) -> { return `suc(plus(x,y)); }
  }
}

void test_failed(char *category) {
  fprintf(stderr, "test '%s' failed!\n", category);
  exit(1);
}
#define test_assert(cat,cond) if(!(cond)) test_failed(cat)

int main() {

    // printf("res = %d\n", plus(10,10) );
  
  test_assert("1+1 = 2", (plus(1,1) == 2));
  test_assert("10+10 = 20", (plus(10,10) == 20));
  
  return(0);
}
