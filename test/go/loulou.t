// Port of test/loulou.t, a C program (test/build.xml leaves it out: it
// needs tom -c): its C host code is Go; the oracle is the original
// compiled by legacy/'s tom -c -np and a C compiler (port_test.go, C).
// port: the #include lines are Go imports, the #define constants Go
// constants, the test_assert macro a function; Nat is implemented by
// Go's int.
// port: a C function may end without return (a %match that matches
// nothing): plus ends with panic("no rule applies").
// port: fprintf is fmt.Fprintf, exit(1) is os.Exit(1).
package main

import (
  "fmt"
  "os"
)

const (
  ZERO = 0
  SUC = 1
)

%typeterm Nat {
  implement { int }
  is_sort(t) { (0==0) }
}

%op Nat zero() {
  is_fsym(t) { $t==0 }
}

%op Nat suc(p:Nat) {
  is_fsym(t) { $t>0 }
  get_slot(p,t) { $t-1 }
  make(t) { $t+1 }
}

func plus(t1 int, t2 int) int {
  %match(Nat t1, Nat t2) {
    x,zero()   -> { return `x }
    x,suc(y) -> { return `suc(plus(x,y)) }
  }
  panic("no rule applies")
}

func test_failed(category string) {
  fmt.Fprintf(os.Stderr, "test '%s' failed!\n", category)
  os.Exit(1)
}
func test_assert(cat string, cond bool) { if !(cond) { test_failed(cat) } }

func main() {

    // printf("res = %d\n", plus(10,10) );
  
  test_assert("1+1 = 2", (plus(1,1) == 2))
  test_assert("10+10 = 20", (plus(10,10) == 20))
}
