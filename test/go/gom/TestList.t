package gom

// Port of test/gom/TestList.t: the Java Gom API of a list operator.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testlist.list.types.*;`; the Java class
// conc (`import gom.testlist.list.types.list.conc;`, the list terms of
// the operator) is the Go struct ConcList, ((conc)l) is l.(*ConcList).
// port: the Java Gom methods length, toArray, append, reverse and the
// static conc.fromArray are Length, ToArray, Append, Reverse and
// ConcFromArray; an IllegalArgumentException is a panic (recovered by
// catchIllegalArgument); Element[] is []Element, array.length is
// len(array).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module list
    abstract syntax
    Element = a()
            | b()
    List = conc(Element*)
  }

func TestException(t *testing.T) {
    var a Element = `a()
    if catchIllegalArgument(func() {
      a.Length()
      fail(t, "length should raise an IllegalArgumentException")
    }) {
      return
    }
}

func TestZeroLength(t *testing.T) {
    var l List = `conc()
    assertEquals(t, getListLength(l),l.Length())
}

func TestOneLength(t *testing.T) {
    var l List = `conc(a())
    assertEquals(t, getListLength(l),l.Length())
}

func TestTwoLength(t *testing.T) {
    var l List = `conc(a(),b())
    assertEquals(t, getListLength(l),l.Length())
}
func TestNLength(t *testing.T) {
    n := 15
    var l List = `conc()
    for i := 0; i<n; i++ {
      l = `conc(a(),b(),l*)
    }
    assertEquals(t, getListLength(l),l.Length())
}

func TestZeroArray(t *testing.T) {
    var l List = `conc()
    assertEquals(t, 0,len(l.(*ConcList).ToArray()))
}

func TestNArray(t *testing.T) {
    n := 15
    var l List = `conc()
    for i := 0; i<n; i++ {
      l = `conc(a(),b(),l*)
    }
    var array1 []Element = getListArray(l)
    var array2 []any = l.(*ConcList).ToArray() 
    assertEquals(t, len(array1),len(array2))
    for i:=0; i<len(array1); i++ {
      assertEquals(t, array1[i],array2[i])
    }
}

func TestFromZeroArray(t *testing.T) {
    var array []Element = []Element{}
    var l List = ConcFromArray(array)
    assertEquals(t, 0,l.Length())
}

func TestFromToZeroArray(t *testing.T) {
    var array []Element = []Element{}
    var l List = ConcFromArray(array)
    assertEquals(t, 0,len(l.(*ConcList).ToArray()))
}

func TestFromNArray(t *testing.T) {
    n := 15
    var array []Element = make([]Element, 15)
    for i := 0; i<n; i++ {
      if (0 == i%2) {
        array[i] = `a()
      } else {
        array[i] = `b()
      }
    }
    var list List = ConcFromArray(array)
    assertEquals(t, list.Length(),n)
    var l List = `conc()
    for i := 0; i<n; i++ {
      if (0 == i%2) {
        l = `conc(l*,a())
      } else {
        l = `conc(l*,b())
      }
    }
    assertEquals(t, l,list)
}

func TestZeroAdd(t *testing.T) {
    var l List = `conc()
    assertEquals(t, `conc(a()),l.(*ConcList).Append(`a()))
}

func TestOneAdd(t *testing.T) {
    var l List = `conc(a())
    assertEquals(t, `conc(a(),b()),l.(*ConcList).Append(`b()))
}

func TestZeroReverse(t *testing.T) {
    var l List = `conc()
    assertEquals(t, getListReverse(l),l.Reverse())
}

func TestOneReverse(t *testing.T) {
    var l List = `conc(a())
    assertEquals(t, getListReverse(l),l.Reverse())
}

func TestTwoReverse(t *testing.T) {
    var l List = `conc(a(),b())
    assertEquals(t, getListReverse(l),l.Reverse())
}

func TestNReverse(t *testing.T) {
    n := 15
    var l List = `conc()
    for i := 0; i<n; i++ {
      l = `conc(a(),b(),l*)
    }
    assertEquals(t, getListReverse(l),l.Reverse())
}

  func getListReverse(l List) List {
    %match(List l) {
      conc(h,t*) -> {
        var nt List = getListReverse(`t*)
        return `conc(nt*,h)
      }
    }
    return l
  }

  func getListArray(l List) []Element {
    length := getListLength(`l)
    var array []Element = make([]Element, length)
    %match(List l) {
      conc(h,t*) -> {
        array[0]=`h
        var tailArray []Element = getListArray(`t*)
        for i:=0;i<length-1;i++ {
          array[i+1]=tailArray[i]
        }
      }
    }
    return array
  }

  func getListLength(l List) int {
    %match(List l) {
      conc() -> { return 0 }
      conc(_,t*) -> { return 1+getListLength(`t*) }
    }
    return 0
  }

// catchIllegalArgument runs f and reports whether it threw an
// IllegalArgumentException (a panic of the Java Gom API).
func catchIllegalArgument(f func()) (caught bool) {
  defer func() {
    if r := recover(); r != nil {
      caught = true
    }
  }()
  f()
  return false
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

// org.junit.Assert.fail(message)
func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
