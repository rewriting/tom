package gom

// Port of test/gom/TestRuleSortList.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testrulesortlist.sorted.types.*;`; the
// Mix:import hook therefore has nothing to import (a Go import hook must
// be empty: imports belong to the importing Go file).
// port: the Mix:block hook (a static method of the Java class of Mix)
// holds the Go function greaterThan; compareToLPO is CompareToLPO; the
// statements of a hook body end with `;` (Tom emits the host code of the
// generated mapping on a single line).
// port: getHeadMinMax is GetHeadMinMax (an int64: Java's int is the Go
// int64 of Gom's int).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module Sorted
      imports int String
      abstract syntax
      Element = Int(i:int)
              | Str(s:String)
      Sorted = MinMax(int*)
             | MaxMin(int*)
             | NoDup(int*)
             | Mix(Element*)
      module Sorted:rules() {
        MinMax(x,X1*,y,X2*) -> MinMax(y,X1*,x,X2*) if x > y
        MaxMin(x,X1*,y,X2*) -> MaxMin(y,X1*,x,X2*) if x < y
        NoDup(x,X1*,y,X2*) -> NoDup(X1*,x,X2*) if x == y
        Mix(x,X1*,y,X2*) -> Mix(y,X1*,x,X2*) if greaterThan(x,y)==true
      }

      Mix:import() {
        // port: nothing to import, the types are in this package
      }

      Mix:block() {
        func greaterThan(o1 Element, o2 Element) bool {
          if (o1.CompareToLPO(o2)>0) {
            return true
          };
          return false;
        }
      }
  }

func TestSorta(t *testing.T) {
    var l Sorted = `MinMax(4,6,2,0,-2,3)
    assertEquals(t, int64(-2),l.GetHeadMinMax())
}

func TestSortMaxMin(t *testing.T) {
    var l Sorted = `MaxMin(4,6,2,0,-2,3)
    assertEquals(t, int64(6),l.GetHeadMaxMin())
}

func TestSortNoDup(t *testing.T) {
    var l Sorted = `NoDup(4,6,2,0,-2,3)
    var dup Sorted = `NoDup(l*,l*)
    assertEquals(t, l,dup)
}

func TestSortMix(t *testing.T) {
    var l1 Sorted = `Mix(Int(3),Str("a"),Int(6),Str("foo"),Str("bar"))
    var l2 Sorted = `Mix(Str("foo"),Str("bar"),Int(6),Str("a"),Int(3))
    assertEquals(t, l1,l2)
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
