package gom

// Port of test/gom/TestCollection.t: the elements of Gom lists seen as
// Java collections.
// port: the generated %gom types live in this package, so there is no
// counterpart to the imports of gom.testcollection.m.types.*; the Java
// classes f, g, cons and consint of the list terms are the Go structs
// FT, GT, ConsL and ConsintL (`(f) t` is t.(*FT)).
// port: the java.util.Collection API of a Gom list is ported to Go
// slices of its elements: l.toArray() is l.ToArray() (Object[] is
// []any); l.contains(x) is contains(l.ToArray(), x); an Iterator loop
// and a for-each loop are a range over l.ToArray(); getCollection() (a
// mutable collection holding the elements of the immutable list) is a
// copy of ToArray(), to which col.add(x) appends; col.toArray(T[]) fills
// a []T; Java's int elements (Integer) are int64.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module M
    imports int
    abstract syntax
      T = a() | b() | c()
        | f(T*)
        | g(T*)
      L = cons(T*)
        | consint(int*)

      g:AU() {}
  }

// contains is java.util.Collection.contains (equals: == on the shared
// terms and the int64 values).
func contains(col []any, o any) bool {
  for _, x := range col {
    if x == o {
      return true
    }
  }
  return false
}

func Test1(t *testing.T) {
    var l *FT = `f(a(),b()).(*FT)
    assertTrue(t, contains(l.ToArray(), `a()))
    assertTrue(t, contains(l.ToArray(), `b()))
    a:=false
    b:=false
    for _, o := range l.ToArray() {
      if(o==`a()) { a = true }
      if(o==`b()) { b = true }
    }
    assertTrue(t, a)
    assertTrue(t, b)
    for _, x := range l.ToArray() {
      assertTrue(t, contains(l.ToArray(), x))
    }

    var col []any = l.ToArray()
    col = append(col, `c())
    c := false
    for _, o := range col {
      if(o==`c()) { c = true }
    }
    assertTrue(t, c)

    for _, x := range col {
      assertTrue(t, contains(col, x))
    }
}

func Test2(t *testing.T) {
    var l *ConsL = `cons(a(),b()).(*ConsL)
    assertTrue(t, contains(l.ToArray(), `a()))
    assertTrue(t, contains(l.ToArray(), `b()))
    a:=false
    b:=false
    for _, o := range l.ToArray() {
      if(o==`a()) { a = true }
      if(o==`b()) { b = true }
    }
    assertTrue(t, a)
    assertTrue(t, b)

    for _, x := range l.ToArray() {
      assertTrue(t, contains(l.ToArray(), x))
    }

    var col []any = l.ToArray()
    col = append(col, `c())
    c := false
    for _, o := range col {
      if(o==`c()) { c = true }
    }
    assertTrue(t, c)

    for _, x := range col {
      assertTrue(t, contains(col, x))
    }
}

func Test3(t *testing.T) {
    var l *ConsintL = `consint(1,2).(*ConsintL)
    assertTrue(t, contains(l.ToArray(), int64(1)))
    assertTrue(t, contains(l.ToArray(), int64(2)))
    a:=false
    b:=false
    for _, o := range l.ToArray() {
      if(o.(int64)==1) { a = true }
      if(o.(int64)==2) { b = true }
    }
    assertTrue(t, a)
    assertTrue(t, b)

    for _, x := range l.ToArray() {
      assertTrue(t, contains(l.ToArray(), x))

    }
    var col []any = l.ToArray()
    col = append(col, int64(3))
    c := false
    for _, o := range col {
      if(o.(int64)==3) { c = true }
    }
    assertTrue(t, c)

    for _, x := range col {
      assertTrue(t, contains(col, x))
    }
}

func Test4(t *testing.T) {
    var l *FT = `f(a(),b(),c()).(*FT)
    var array []any = l.ToArray()
    assertTrue(t, array[0]==`a())
    assertTrue(t, array[1]==`b())
    assertTrue(t, array[2]==`c())

    var col []any = l.ToArray()
    var colarray []any = col
    assertTrue(t, colarray[0]==`a())
    assertTrue(t, colarray[1]==`b())
    assertTrue(t, colarray[2]==`c())

}

func Test5(t *testing.T) {
    var l *GT = `g(a(),b()).(*GT)
    assertTrue(t, contains(l.ToArray(), `a()))
    assertTrue(t, contains(l.ToArray(), `b()))
    a:=false
    b:=false
    for _, o := range l.ToArray() {
      if(o==`a()) { a = true }
      if(o==`b()) { b = true }
    }
    assertTrue(t, a)
    assertTrue(t, b)
    for _, x := range l.ToArray() {
      assertTrue(t, contains(l.ToArray(), x))
    }

    var col []any = l.ToArray()
    col = append(col, `c())
    c := false
    for _, o := range col {
      if(o==`c()) { c = true }
    }
    assertTrue(t, c)

    for _, x := range col {
      assertTrue(t, contains(col, x))
    }
}

func Test6(t *testing.T) {
    var l *GT = `g(a(),b(),c()).(*GT)
    var array []any = l.ToArray()
    assertEquals(t, `a(),array[0])
    assertEquals(t, `b(),array[1])
    assertEquals(t, `c(),array[2])

    var col []any = l.ToArray()
    var colarray []any = col
    assertEquals(t, `a(),colarray[0])
    assertEquals(t, `b(),colarray[1])
    assertEquals(t, `c(),colarray[2])

    var tcolarray []T = make([]T, 3)
    for i, o := range col {
      tcolarray[i] = o.(T)
    }
    var myarray []T = tcolarray
    assertEquals(t, `a(),myarray[0])
    assertEquals(t, `b(),myarray[1])
    assertEquals(t, `c(),myarray[2])

    var revl *GT = l.Reverse().(*GT)
    var revarray []any = revl.ToArray()
    assertEquals(t, `c(),revarray[0])
    assertEquals(t, `b(),revarray[1])
    assertEquals(t, `a(),revarray[2])
}

// org.junit.Assert.assertTrue(condition)
func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
