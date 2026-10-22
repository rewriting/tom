package testpeano

// Port of test/TestPeano.t: the Java aterm library is replaced by its Go
// subset tomtest/aterm (same API, maximal sharing).

import (
  "testing"

  "tomtest/aterm"
)

var factory = aterm.GetInstance()
var fzero, fsuc, fplus *aterm.AFun
var tzero *aterm.ATermAppl

func init() {
  fzero = factory.MakeAFun("zero", 0, false)
  fsuc  = factory.MakeAFun("suc" , 1, false)
  fplus = factory.MakeAFun("plus", 2, false)
  tzero = factory.MakeAppl(fzero)
}

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

func TestPlus1(t *testing.T) {
  for i:=0 ; i<100 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing plus1 with N ="+N.String()+": ",
	  peano2int(plus1(N,N)) == (i+i) )
  }
}

func TestPlus2(t *testing.T) {
  for i:=0 ; i<100 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing plus2 with N ="+N.String()+": ",
	  peano2int(plus2(N,N)) == (i+i) )
  }
}

func TestPlus3(t *testing.T) {
  for i:=0 ; i<100 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing plus3 with N ="+N.String()+": ",
	  peano2int(plus3(N,N)) == (i+i) )
  }
}

func TestPlus4(t *testing.T) {
  for i:=0 ; i<100 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing plus4 with N ="+N.String()+": ",
	  peano2int(plus4(N,N)) == (i+i) )
  }
}

func TestFib3(t *testing.T) {
  for i:=0 ; i<15 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing fib3 with N ="+N.String()+": ",
	  peano2int(fib3(N)) == fibint(i) )
  }
}

func TestFib4(t *testing.T) {
  for i:=0 ; i<15 ; i++ {
    N := int2peano(i)
    assertTrue(t, "Testing fib4 with N ="+N.String()+": ",
	  peano2int(fib4(N)) == fibint(i) )
  }
}

func plus1(t1 aterm.ATerm, t2 aterm.ATerm) aterm.ATerm {
  %match(term t1, term t2) {
    x,(zero|zero)[]   -> { return `x }
    x,suc(y) -> { return suc(plus1(`x,`y)) }
  }
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

func TestInt2p(t *testing.T) {
  assertEquals(t, "int2peano should return sssss(tzero) for 5",
	int2peano(5),`suc(suc(suc(suc(suc(tzero))))))
}

func int2peano(n int) aterm.ATerm {
  var N aterm.ATerm = tzero
  for i:=0 ; i<n ; i++ {
    N = suc(N)
  }
  return N
}

func TestPeano2int(t *testing.T) {
  assertEquals(t, "peano2int should return 5 for sssss(tzero)",
	peano2int(`suc(suc(suc(suc(suc(tzero)))))),5)
}

func peano2int(N aterm.ATerm) int {
  %match(term N) {
    zero() -> { return 0 }
    suc(x) -> {return 1+peano2int(`x) }
  }
  return 0
}

// org.junit.Assert.assertTrue / assertEquals
func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}

func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
