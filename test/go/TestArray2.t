// Port of test/TestArray2.t.
// port: java.util.List / ArrayList are []any; the class Element is the Go
// struct *Element (GetObject, String), `instanceof` a type assertion;
// Element does not override equals, so equals is == on the pointers;
// myAdd appends in place (Go's append) and returns the list.
// port: java.util.logging at Level.FINE (not printed by default) is
// log/slog at LevelDebug (not printed by the default logger); "…" + x
// is "…" + fmt.Sprint(x).
// port: @BeforeClass/@AfterClass become TestMain; Assert.assertTrue is
// the helper assertTrue.
// port: no main — `go test` runs the tests (the original main only launched JUnit).
package testarray2

import (
  "context"
  "fmt"
  "log/slog"
  "os"
  "testing"
)

var logger *slog.Logger
var level slog.Level = slog.LevelDebug

  %typeterm jtList {
    implement { []any }
    is_sort(t) { $t != nil }
    equals(l1,l2) { listEquals($l1, $l2) }
  }

  %oparray jtList conc(jtElement*) {
    is_fsym(t) { $t != nil }
    make_empty(n)    { make([]any, 0, $n) }
    make_append(e,l) { myAdd($e,$l) }
    get_element(l,n) { $l[$n].(*Element) }
    get_size(l)      { len($l) }
  }

func myAdd(e any, l []any) []any {
    l = append(l, e)
    return l
  }

  %typeterm jtElement {
    implement { *Element }
    is_sort(t) { ($t != nil) }
    equals(t1,t2) { $t1 == $t2 }
  }

  %op jtElement ListElement(v:jtList) {
    is_fsym(t) { ($t != nil) && isList($t.GetObject()) }
    get_slot(v,e)  { $e.GetObject().([]any) }
    make(v) { NewElement($v) }
  }

  %op jtElement a() {
    is_fsym(t) { ($t != nil) && $t.GetObject() == "a" }
    make() { NewElement("a") }
  }

  %op jtElement b() {
    is_fsym(t) { ($t != nil) && $t.GetObject() == "b" }
    make() { NewElement("b") }
  }

func TestMain(m *testing.M) {
  setUp()
  code := m.Run()
  tearDown()
  os.Exit(code)
}

func setUp() {
    logger = slog.Default()
  }

func tearDown() {
    logger = nil
  }

func TestVariableStar1(t *testing.T) {
    nbSol := 0
    var l []any =  `conc(ListElement(conc(a(),b())),a(),b())
    %match(jtList l) {
      conc(ListElement(conc(R*,T*)),X1*,u,X2*) -> {
        nbSol++
        logger.Log(context.Background(), level,
            "R = " + fmt.Sprint(`R*) + " T = " + fmt.Sprint(`T*)+" X1 = " + fmt.Sprint(`X1*) + " u = " + fmt.Sprint(`u) + " X2 = " + fmt.Sprint(`X2*))
      }
    }
    assertTrue(t, "TestVariableStar1",nbSol==6)
  }

type Element struct {
    object any
  }

func NewElement(o any) *Element {
      return &Element{object: o}
    }

func (e *Element) GetObject() any {
      return e.object
    }

func (e *Element) String() string {
      if isList(e.GetObject()) {
        return "ListElement(" + fmt.Sprint(e.GetObject()) + ")"
      } else {
        return fmt.Sprint(e.GetObject())
      }
    }

// isList is `o instanceof List`.
func isList(o any) bool { _, ok := o.([]any); return ok }

// listEquals is List.equals: the elements compared with equals (here ==).
func listEquals(l1, l2 []any) bool {
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

// org.junit.Assert.assertTrue(message, condition)
func assertTrue(t *testing.T, msg string, cond bool) {
  t.Helper()
  if !cond {
    t.Fatal(msg)
  }
}
