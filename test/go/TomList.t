// Port of test/TomList.t, a main program: the Java aterm library is
// replaced by its Go port tom/library/aterm.
// port: the instance methods are functions (the instance holds no state:
// the factory is static), and the constructor TomList(factory) is
// newTomList, which sets it; ATermAppl casts are type assertions,
// String.compareTo is strings.Compare.
// port: a method chain broken over lines keeps its dots at the end of the
// lines (Go inserts a semicolon after `X1 at the end of a line).
// port: assertTrue panics instead of throwing a RuntimeException.
package main

import (
  "fmt"
  "strings"

  "tom/library/aterm"
)

var factory *aterm.PureFactory

  %typeterm E {
    implement { aterm.ATerm }
    is_sort(t) { $t != nil }
    equals(t1, t2) { ($t1 == $t2) }
  }

  %typeterm L {
    implement { *aterm.ATermList }
    is_sort(t) { $t != nil }
    equals(l1,l2)  { $l1 == $l2 }
  }

  %oplist L conc( E* ) {
    is_fsym(t) { $t != nil }
    get_head(l)    { any($l).(*aterm.ATermList).GetFirst() }
    get_tail(l)    { any($l).(*aterm.ATermList).GetNext() }
    is_empty(l)    { any($l).(*aterm.ATermList).IsEmpty() }
    make_empty()  { factory.MakeListEmpty() }
    make_insert(e,l) { any($l).(*aterm.ATermList).Insert($e) }
  }
  
  %op E a() {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "a" }
    make() { factory.MakeAppl(factory.MakeAFun("a", 0, false)) }
  }
  
  %op E b() {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "b" }
    make() { factory.MakeAppl(factory.MakeAFun("b", 0, false)) }
  }

  %op E c() {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "c" }
    make() { factory.MakeAppl(factory.MakeAFun("c", 0, false)) }
  }

  %op L id(s1:L) {
    is_fsym(t) { false }
    make(l) { $l }
  }

type TomList struct{}

func newTomList(f *aterm.PureFactory) *TomList {
    factory = f
    return &TomList{}
  }

func main() {
    newTomList(aterm.NewPureFactory(16))
    testList1()
    testList2()
    fmt.Println("TomList test has successfully terminated.")
  }

func sort1(l *aterm.ATermList) *aterm.ATermList {
    %match(L l) {
      conc(X1*,x,X2*,y,X3*) -> {
        xname := (`x).(*aterm.ATermAppl).GetName()
        yname := (`y).(*aterm.ATermAppl).GetName()
        
        if strings.Compare(xname, yname) > 0 {
          return sort1(
            `X1.
            Append(`y).
            Concat(`X2).
            Append(`x).
            Concat(`X3),
            )
        }
      }
    }
		return l 
  }

func double1(l *aterm.ATermList) *aterm.ATermList {
    %match(L l) {
      conc(X1*,x,X2*,x,X3*) -> {
        return double1(
          `X1.
          Concat(`X2).
          Append(`x).
          Concat(`X3),
          )
      }
    }
		return l 
  }

func sort2(l *aterm.ATermList) *aterm.ATermList {
    %match(L l) {
      conc(X1*,x,y,X2*) -> {
        xname := (`x).(*aterm.ATermAppl).GetName()
        yname := (`y).(*aterm.ATermAppl).GetName()
        if strings.Compare(xname, yname) > 0 {
          return `sort2(conc(X1*,y,x,X2*))
        }
      }
    }
		return l 
  }

func double2(l *aterm.ATermList) *aterm.ATermList {
    %match(L l) {
      conc(X1*,x,X2*,x,X3*) -> {
        return `double2(id( conc(X1*,X2*,x,X3*) ))
      }
    }
		return l 
  }

func double4(l *aterm.ATermList) *aterm.ATermList {
    %match(L l) {
      conc(X1*,x@_,X2@_*,x,X3@_*) -> { return `double4(conc(X1*,X2*,x,X3*)) }
    }
		return l 
  }

func double5(l *aterm.ATermList) *aterm.ATermList {
    %match(L l) {
      conc(X1*,x@a(),X2*,x@a(),X3*) -> { return `double5(conc(X1*,X2*,x,X3*)) }
      conc(X1*,x@_,X2*,x@_,X3*) -> { return `double5(conc(X1*,X2*,x,X3*)) }
      conc(X1*,x@y,X2*,y@x,X3*) -> { return `double5(conc(X1*,X2*,x,X3*)) }
    }
		return l 
  }

func testList1() {
    var ta aterm.ATerm = factory.MakeAppl(factory.MakeAFun("a", 0, false))
    var tb aterm.ATerm = factory.MakeAppl(factory.MakeAFun("b", 0, false))
    var tc aterm.ATerm = factory.MakeAppl(factory.MakeAFun("c", 0, false))
    l := factory.MakeListEmpty().
      Append(ta).Append(tb).Append(tc).
      Append(ta).Append(tb).Append(tc).
      Append(ta)

    res := factory.MakeListEmpty().
      Append(ta).Append(ta).Append(ta).
      Append(tb).Append(tb).Append(tc).
      Append(tc)
    
    assertTrue(sort1(l) == res)
    assertTrue(sort2(l) == res)
    fmt.Println("testList1 has successfully terminated.")
  }
 
func testList2() {
    var l *aterm.ATermList   = `conc(a(),b(),c(),a(),b(),c(),a())
    var res *aterm.ATermList = `conc(a(),b(),c())
    
    assertTrue(double1(sort1(l)) == res)

    assertTrue(double2(sort2(l)) == res)
    assertTrue(double4(sort2(l)) == res)
    assertTrue(double5(sort2(l)) == res)


      //System.out.println("l        = " + l);
      //System.out.println("sorted l = " + sort2(l));
      //System.out.println("double l = " + double2(sort2(l)));
    fmt.Println("testList2 has successfully terminated.")
  }
  
func assertTrue(condition bool) {
    if !condition {
      panic("assertion failed.")
    }
  }
