package main

// Port of test/Peano.t: the Java aterm library is replaced by its Go
// subset tomtest/aterm (same API, maximal sharing).

import (
	"fmt"

	"tomtest/aterm"
)

var factory *aterm.PureFactory
var fzero, fsuc, fplus *aterm.AFun
var tzero *aterm.ATermAppl

%typeterm term {
  implement { aterm.ATerm }
  is_sort(t) { $t != nil }
  equals(t1, t2)      { $t1 == $t2}
}

%typeterm appl {
  implement { *aterm.ATermAppl }
  is_sort(t) { $t != nil }
  equals(t1, t2)      { $t1 == $t2}
}

%op term zero() {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetAFun() == fzero }
  make() { factory.MakeAppl(fzero) }
}

%op term suc(pred:term) {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetAFun() == fsuc }
  get_slot(pred,t) { any($t).(*aterm.ATermAppl).GetArgument(0)  }
  make(t) { factory.MakeAppl(fsuc,$t) }
}

%op term plus1(s1:term,s2:term) {
  make(t1,t2) { plus1($t1,$t2) }
}

%op term plus2(s1:term,s2:term) {
  make(t1,t2) { plus2($t1,$t2) }
}

%op appl term2appl(s1:term) {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "term2appl" }
  get_slot(s1,t) { any($t).(*aterm.ATermAppl).GetArgument(0) }
  make(t) { factory.MakeAppl(factory.MakeAFun("term2appl",1,false),$t) }
}

func newPeano(f *aterm.PureFactory) {
  factory = f

  fzero = factory.MakeAFun("zero", 0, false)
  fsuc  = factory.MakeAFun("suc" , 1, false)
  fplus = factory.MakeAFun("plus", 2, false)
  tzero = factory.MakeAppl(fzero)
}

func run() {
  for i:=0 ; i<100 ; i++ {
    N := int2peano(i)
    assertTrue( peano2int(plus1(N,N)) == (i+i) )
    assertTrue( peano2int(plus2(N,N)) == (i+i) )
    assertTrue( peano2int(plus3(N,N)) == (i+i) )
    assertTrue( peano2int(plus4(N,N)) == (i+i) )
  }
  fmt.Println("First loop has successfully terminated.")

  for i:=0 ; i<15 ; i++ {
    N := int2peano(i)
    assertTrue( peano2int(fib3(N)) == fibint(i) )
    assertTrue( peano2int(fib4(N)) == fibint(i) )
  }
  fmt.Println("Second loop has successfully terminated.")

}

func main() {
  newPeano(aterm.NewPureFactory(16))
  run()
  fmt.Println("Peano test has successfully terminated.")
}

func plus1(t1 aterm.ATerm, t2 aterm.ATerm) aterm.ATerm {
  %match(term t1, term t2) {
    x,(zero|zero)[]   -> { return `x }
    x,suc(y) -> { return suc(plus1(`x,`y)) }
  }
  //@ assert false;
  return nil
}

func plus2(t1 aterm.ATerm, t2 aterm.ATerm) aterm.ATerm {
  %match(term t1, term t2) {
    x,zero()   -> { return `x }
    x,suc[pred=y] -> { return suc(plus2(`x,`y)) }
  }
  return nil
}

func plus3(t1 aterm.ATerm, t2 aterm.ATerm) aterm.ATerm {
  %match(term t1, term t2) {
    x,zero()   -> { return `x }
    x@_,suc[pred=y@z] -> { return suc(plus3(`x,`y)) }
  }
  return nil
}

  func plus4(t1 aterm.ATerm, t2 aterm.ATerm) aterm.ATerm {
  %match(term t1, term t2) {
    x,(zero|zero)[]   -> { return `x }
    x,(suc|suc)[pred=y] -> { return suc(plus1(`x,`y)) }
  }
  return nil
}

func fib3(t aterm.ATerm) aterm.ATerm {
  %match(term t) {
    zero()             -> { return `suc(zero()) }
    suc[pred=x@zero()] -> { return `suc(x) }
    suc(suc(x))      -> { return plus3(fib3(`x),fib3(suc(`x))) }
  }
  return nil
}

func fib4(t aterm.ATerm) aterm.ATerm {
  %match(term t) {
    zero()      -> { return `suc(zero()) }
    suc(zero()) -> { return `suc(zero()) }
    suc(suc(x)) -> { return plus3(fib4(`x),fib4(suc(`x))) }
  }
  return nil
}

func suc(t aterm.ATerm) aterm.ATerm {
  return factory.MakeAppl(fsuc,t)
}

func fibint(n int) int {
  if n<=1 {
    return 1
  } else {
    return fibint(n-1)+fibint(n-2)
  }
}

func int2peano(n int) aterm.ATerm {
  var N aterm.ATerm = tzero
  for i:=0 ; i<n ; i++ {
    N = suc(N)
  }
  return N
}

func peano2int(N aterm.ATerm) int {
  %match(term N) {
    zero() -> { return 0 }
    suc(x) -> {return 1 +peano2int(`x) }
  }
  return 0
}

func assertTrue(condition bool) {
  if !condition {
    panic("assertion failed.")
  }
}
