#include<stdio.h>
#include<stdlib.h>

#define ZERO  0
#define SUC   1

%typeterm Nat {
  implement { int }
  get_fun_sym(i)      { (i==0)?ZERO:SUC }
  cmp_fun_sym(t1,t2)  { (t1 == t2) }
  get_subterm(i, n)   { (i-1) }
}

%op Nat zero {
  fsym { ZERO }
}

%op Nat suc(Nat) {
  fsym { SUC }
}

int suc(int t) {
  return t+1;
}

int plus(int t1, int t2) {
  %match(Nat t1, Nat t2) {
    x,zero   -> { return x; }
    x,suc(y) -> { return suc(plus(x,y)); }
  }
}

int main() {
  printf("res = %d\n", plus(10,10) );
}
