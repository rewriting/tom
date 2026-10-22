package main

// Port of test/rule/Array.t (the same program as test/Array.t, with the
// older hook syntax: hook parameters written without $): the Java aterm
// library is replaced by its Go port tom/library/aterm,
// java.util.ArrayList by a slice.

import (
  "strings"

  "tom/library/aterm"
)

var factory = aterm.GetInstance()

var fzero, fsuc, fplus, ffib *aterm.AFun
var tzero *aterm.ATermAppl

%typeterm L {
  implement { []aterm.ATerm }
  is_sort(t) { t != nil }
  equals(l1,l2)    { listEquals(l1, l2) }
}

%oparray L conc( E* ) {
  is_fsym(t)       { t != nil }
  make_empty(n)    { make([]aterm.ATerm, 0, n) }
  make_append(e,l) { myAdd(e,l) }
  get_element(l,n) { l[n] }
  get_size(l)      { len(l) }
}

func myAdd(e aterm.ATerm, l []aterm.ATerm) []aterm.ATerm {
  return append(l, e)
}

// java.util.ArrayList.equals
func listEquals(l1, l2 []aterm.ATerm) bool {
  if len(l1) != len(l2) {
    return false
  }
  for i := range l1 {
    if l1[i] != l2[i] {
      return false
    }
  }
  return true
}

%typeterm E {
  implement           { aterm.ATerm }
  is_sort(t) { false }
  equals(t1, t2)      { (t1 == t2) }
}

%op E a() {
  is_fsym(t) { any(t).(*aterm.ATermAppl).GetName() == "a" }
  make() { factory.MakeAppl(factory.MakeAFun("a", 0, false)) }
}

%op E b() {
  is_fsym(t) { any(t).(*aterm.ATermAppl).GetName() == "b" }
  make() { factory.MakeAppl(factory.MakeAFun("b", 0, false)) }
}

%op E c() {
  is_fsym(t) { any(t).(*aterm.ATermAppl).GetName() == "c" }
  make() { factory.MakeAppl(factory.MakeAFun("c", 0, false)) }
}

/*
  %op L double3(s1:L) {
    is_fsym(t) { ((ATermAppl)t).getName() == "double3" }
    //get_slot(s1,t) { return null; }
    make(l) { double3(l) }
  }

  %rule {
    double3(conc(X1*,x,X2*,x,X3*)) -> double3(conc(X1*,X2*,x,X3*))
    double3(conc(X*)) -> conc(X*)
  } 
*/

func main() {
  testArray1()
}

func testArray1() {
  var ta aterm.ATerm = factory.MakeAppl(factory.MakeAFun("a", 0, false))
  var tb aterm.ATerm = factory.MakeAppl(factory.MakeAFun("b", 0, false))
  var tc aterm.ATerm = factory.MakeAppl(factory.MakeAFun("c", 0, false))
  l := []aterm.ATerm{}
  l = append(l, ta)
  l = append(l, tb)
  l = append(l, tc)
  l = append(l, ta)
  l = append(l, tb)
  l = append(l, tc)

  res := []aterm.ATerm{}
  res = append(res, ta)
  res = append(res, ta)
  res = append(res, tb)
  res = append(res, tb)
  res = append(res, tc)
  res = append(res, tc)

  assertTrue(listEquals(sort1(l), res))
  assertTrue(listEquals(sort2(l), res))
}

func testArray2() {
  var l   []aterm.ATerm = `conc(a(),b(),c(),a(),b(),c(),a())
  var res []aterm.ATerm = `conc(a(),b(),c())

  assertTrue(listEquals(double1(sort1(l)), res))
  assertTrue(listEquals(double2(sort2(l)), res))
  //assertTrue(double3(sort2(l)).equals(res));
  assertTrue(listEquals(double4(sort2(l)), res))
  assertTrue(listEquals(double5(sort2(l)), res))
}

func sort1(l []aterm.ATerm) []aterm.ATerm {
  %match(L l) {
    conc(X1*,x,X2*,y,X3*) -> {
      xname := any(`x).(*aterm.ATermAppl).GetName()
      yname := any(`y).(*aterm.ATermAppl).GetName()
      if strings.Compare(xname, yname) > 0 {
        result := `X1
        result = append(result, `y)
        result = append(result, (`X2)...)
        result = append(result, `x)
        result = append(result, (`X3)...)
        return sort1(result)
      }
    }
  }
	return l
}

func double1(l []aterm.ATerm) []aterm.ATerm {
  %match(L l) {
    conc(X1*,x,X2*,x,X3*) -> {
      result := `X1
      result = append(result, (`X2)...)
      result = append(result, `x)
      result = append(result, (`X3)...)
      return double1(result)
    }
  }
	return l
}

func sort2(l []aterm.ATerm) []aterm.ATerm {
  %match(L l) {
    conc(X1*,x,X2*,y,X3*) -> {
      xname := any(`x).(*aterm.ATermAppl).GetName()
      yname := any(`y).(*aterm.ATermAppl).GetName()
      if strings.Compare(xname, yname) > 0 {
        return `sort2(conc(X1*,y,X2*,x,X3*))
      }
    }
  }
	return l
}

func double2(l []aterm.ATerm) []aterm.ATerm {
  %match(L l) {
    conc(X1*,x,X2*,x,X3*) -> {
      return `double2(conc(X1*,X2*,x,X3*))
    }
  }
	return l
}


func double4(l []aterm.ATerm) []aterm.ATerm {
  %match(L l) {
    conc(X1*,x@_,X2@_*,x,X3@_*) -> { return `double4(conc(X1*,X2*,x,X3*)) }
  }
	return l
}

func double5(l []aterm.ATerm) []aterm.ATerm {
  %match(L l) {
    conc(X1*,x@a(),X2*,x@a(),X3*) -> { return `double5(conc(X1*,X2*,x,X3*)) }
    conc(X1*,x@_,X2*,x@_,X3*) -> { return `double5(conc(X1*,X2*,x,X3*)) }
    conc(X1*,x@y,X2*,y@x,X3*) -> { return `double5(conc(X1*,X2*,x,X3*)) }
  }
	return l
}

func assertTrue(condition bool) {
  if !condition {
    panic("assertion failed.")
  }
}
