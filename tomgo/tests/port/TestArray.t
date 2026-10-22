package testarray

// Port of test/TestArray.t: the Java aterm library is replaced by its Go
// subset tom/tomgo/tests/port/aterm, java.util.ArrayList by a slice,
// java.util.logging by log/slog (FINE = Debug), @BeforeClass/@AfterClass
// by TestMain and @Before by a setUp() call at the start of each test.

import (
  "context"
  "fmt"
  "log/slog"
  "os"
  "strings"
  "testing"

  "tom/tomgo/tests/port/aterm"
)

var factory = aterm.GetInstance()

var fzero, fsuc, fplus, ffib *aterm.AFun
var tzero *aterm.ATermAppl
var unsortedlist []aterm.ATerm
var sortedlist []aterm.ATerm
var listwithdoubles []aterm.ATerm
var listwithoutdoubles []aterm.ATerm
var logger *slog.Logger
var level = slog.LevelDebug

%typeterm L {
  implement { []aterm.ATerm }
  is_sort(t) { $t != nil }
  equals(l1,l2)    { listEquals($l1, $l2) }
}

%oparray L conc( E* ) {
  is_fsym(t)       { $t != nil }
  get_element(l,n) { $l[$n] }
  get_size(l)      { len($l) }
  make_empty(n)    { make([]aterm.ATerm, 0, $n) }
  make_append(e,l) { myAdd($e,$l) }
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
  is_sort(t) { $t != nil }
  equals(t1, t2)      { ($t1 == $t2) }
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

func TestMain(m *testing.M) {
  staticSetUp()
  code := m.Run()
  staticTearDown()
  os.Exit(code)
}

func staticSetUp() {
  logger = slog.Default().With("class", "TestArray")
}

func setUp() {
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
  unsortedlist = l

  res := []aterm.ATerm{}
  res = append(res, ta)
  res = append(res, ta)
  res = append(res, tb)
  res = append(res, tb)
  res = append(res, tc)
  res = append(res, tc)
  sortedlist = res

  listwithdoubles = `conc(a(),b(),c(),a(),b(),c(),a())
  listwithoutdoubles = `conc(a(),b(),c())
}

func staticTearDown() {
  logger = nil
}

func TestSort1(t *testing.T) {
  setUp()
  assertEquals(t,
      "sort1 should return a sorted list",
      sort1(unsortedlist), sortedlist)
}

func TestSort2(t *testing.T) {
  setUp()
  assertEquals(t,
      "sort2 should return a sorted list",
      sort2(unsortedlist), sortedlist)
}

func TestDouble1(t *testing.T) {
  setUp()
  assertEquals(t,
      "double1 should remove all double element in a list",
      double1(sort1(listwithdoubles)),listwithoutdoubles)
}

func TestDouble2(t *testing.T) {
  setUp()
  assertEquals(t,
      "double2 should remove all double element in a list",
      double2(sort2(listwithdoubles)),listwithoutdoubles)
}

func TestDouble4(t *testing.T) {
  setUp()
  assertEquals(t,
      "double4 should remove all double element in a list",
      double4(sort2(listwithdoubles)),listwithoutdoubles)
}

func TestDouble5(t *testing.T) {
  setUp()
  assertEquals(t,
      "double5 should remove all double element in a list",
      double5(sort2(listwithdoubles)),listwithoutdoubles)
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

func TestVariableStar1(t *testing.T) {
  setUp()
  nbSol := 0
  var l []aterm.ATerm = `conc(a(),b())
  %match(L l) {
    conc(X1*,X2*,X3*) -> {
      nbSol++
      logger.Log(context.Background(), level, "X1 = " + fmt.Sprint(`X1*) + " X2 = " + fmt.Sprint(`X2*)+ " X3 = " + fmt.Sprint(`X3*))
    }
  }
  assertTrue(t, "TestVariableStar1",nbSol==6)
}

// org.junit.Assert.assertTrue / assertEquals (ArrayList.equals)
func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}

func assertEquals(t *testing.T, message string, expected, actual []aterm.ATerm) {
  t.Helper()
  if !listEquals(expected, actual) {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
