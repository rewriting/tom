// Port of test/cfib1.t, a C program (test/build.xml leaves it out: it
// needs tom -c): its C host code is Go; the oracle is the original
// compiled by legacy/'s tom -c -np and a C compiler (port_test.go, C).
// port: the #include lines are Go imports, the #define constants Go
// constants, the test_assert macro a function; struct term is a Go struct
// whose subterms are a slice (malloc is a composite literal), and the
// C mapping's ((void*)$t->fs == (void*)ZERO) is $t.fs == ZERO.
// port: a C function may end without return (a %match that matches
// nothing): the Go functions end with panic("no rule applies").
// port: printf/fprintf/fflush are fmt.Printf/fmt.Fprintf/os.Stdout.Sync,
// exit(1) is os.Exit(1).
/*
 * This is an example on computing n-th fib number on peano arithmetics
 */
package main

import (
  "fmt"
  "os"
)

const (
  ZERO = 0
  SUC = 1
  PLUS = 2
  FIB = 3
)

type term struct {
  fs int
  arity int
  subt []*term
}

var s_zero term = term{ZERO, 0, nil}
var zero *term = &s_zero

func suc(x *term) *term {
  var res *term
  res = &term{}
  res.fs = SUC
  res.arity = 1
  res.subt = make([]*term, 1)
  res.subt[0] = x
  return(res)
}

%typeterm term {
  implement { *term }
  is_sort(t) { (0==0) }
}

%op term zero() {
  is_fsym(t) { $t.fs == ZERO }
}
  
%op term suc(s1:term) {
  is_fsym(t) { $t.fs == SUC }
  get_slot(s1,t) { $t.subt[0] }
}

%op term plus(s1:term,s2:term) {
  is_fsym(t) { $t.fs == PLUS }
  get_slot(s1,t) { $t.subt[0] }
  get_slot(s2,t) { $t.subt[1] }
}

%op term fib(s1:term) {
  is_fsym(t) { $t.fs == FIB }
  get_slot(s1,t) { $t.subt[0] }
}

func plus(t1 *term, t2 *term) *term {
  %match(term t1, term t2) {
    x,zero()   -> { return `(x) }
    x,suc(y) -> { return (suc(plus(`x,`y))) }
  }
  panic("no rule applies")
}

func fib(t *term) *term {
  %match(term t) {
    zero()        -> { return(suc(zero)) }
    suc(zero())   -> { return(suc(zero)) }
    suc(suc(x)) -> { return(plus(fib(`x),fib(suc(`x)))) }
  }
  panic("no rule applies")
}

func count(t *term) int {
  %match(term t) {
    zero()        -> { return 0 }
    suc(x)   -> { return 1 + count(`x) }
  }
  panic("no rule applies")
}

func printTerm(tt *term) {
  var i int
  fsnames := []string{"zero", "suc", "plus", "fib"}
  fmt.Printf("%s", fsnames[tt.fs])
  if tt.arity!=0 {
    fmt.Printf("(")
    for i=0; i<tt.arity; i++ {
      printTerm(tt.subt[i])
      if i+1 != tt.arity { fmt.Printf(",") }
    }
    fmt.Printf(")")
  }
}

func buildPeano(n int) *term {
  var i int
  var t *term
  t = zero
  for i=0; i<n; i++ {
    t = suc(t)
  }
  return t
}

func symbolicFib( n int ) {
  fmt.Printf("fib %d == ", n)
  printTerm(fib(buildPeano(n)))
  fmt.Printf("\n")
  os.Stdout.Sync()
}

func test_failed(category string) {
  fmt.Fprintf(os.Stderr, "test %s failed!\n", category)
  os.Exit(1)
}
func test_assert(cat string, cond bool) { if !(cond) { test_failed(cat) } }


func main() {
    /*printTerm(fib(buildPeano(10)));*/
  test_assert("fib(10)", (count(fib(buildPeano(10))) == 89) )
}
