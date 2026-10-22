package testlist

// Port of test/TestList.t: the Java aterm library is replaced by its Go
// port tom/library/aterm. The JUnit suite becomes one Go test
// per @Test method (TestMatch1 … TestMatch6); a @Parameterized class
// loops over its data(); @BeforeClass/@AfterClass become TestMain.

import (
  "os"
  "testing"

  "tom/library/aterm"
)

var ok, fail aterm.ATerm
var factory *aterm.PureFactory = nil

func TestMain(m *testing.M) {
  setUp()
  code := m.Run()
  tearDown()
  os.Exit(code)
}

func setUp() {
  factory = aterm.GetInstance()
  ok      = factory.Parse("ok")
  fail    = factory.Parse("fail")
}

func tearDown() {
  factory = nil
  ok = nil
  fail = nil
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
  make_empty()   { factory.MakeListEmpty() }
  make_insert(e,l) { any($l).(*aterm.ATermList).Insert($e) }
}

%typeterm E {
  implement { aterm.ATerm }
  is_sort(t) { $t != nil }
  equals(t1, t2) { ($t1 == $t2) }
}

%op E a() {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "a" }
}

%op E b() {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "b" }
}

%op E c() {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "c" }
}

%op E f(s1:E) {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "f" }
  get_slot(s1,t) { any($t).(*aterm.ATermAppl).GetArgument(0)  }
}

%op E g(s1:E) {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "g" }
  get_slot(s1,t) { any($t).(*aterm.ATermAppl).GetArgument(0)  }
}

%op E l(sl:L) {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "l" }
  get_slot(sl,t) { any($t).(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermList)  }
}

%op E h(s1:E,s2:E) {
  is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "h" }
  get_slot(s1,t) { any($t).(*aterm.ATermAppl).GetArgument(0)  }
  get_slot(s2,t) { any($t).(*aterm.ATermAppl).GetArgument(1)  }
}

// ---------------------------------------------------------------- Match1

func TestMatch1(t *testing.T) {
  for _, d := range match1Data() {
    question, answer := d[0], d[1]
    assertSame(t,
      "TestMatch1 : expected "+answer+" for term "+question +"",
      match1(factory.Parse(question)),
      factory.Parse(answer))
  }
}

func match1Data() [][2]string {
  return [][2]string {
      { "[f(a)]",                  "pattern1" },
      { "[a,b,f(a)]",              "pattern2" },
      { "[a,f(a),b,f(b)]",         "pattern3" },
      { "[a,f(a),f(b)]",           "pattern3" },
      { "[f(a),f(b)]",             "pattern3" },
      { "[a,f(a),b,f(b),c]",       "pattern4" },
      { "[a,f(a),f(b),c]",         "pattern4" },
      { "[f(a),f(b),c]",           "pattern4" },
      { "[f(b),f(b)]",             "pattern5" },
      { "[f(b),f(b),c]",           "pattern5" },
      { "[f(b),a,f(b)]",           "pattern5" },
      { "[f(b),a,f(b),c]",         "pattern5" },
      { "[f(c),f(b)]",             "pattern6" },
      { "[f(a),f(c),a,f(c)]",          "fail" },
      { "[f(a),f(c),a,f(c),f(c)]", "pattern6" },
      { "[]",                      "pattern7" },
  }
}

func match1(t aterm.ATerm) aterm.ATerm {
  res := fail
  l := t.(*aterm.ATermList)
  %match(L l) {
    conc(f(a()))                           -> { return factory.Parse("pattern1") }
    conc(_X1*,f(a()))                      -> { return factory.Parse("pattern2") }
    conc(_X1*,f(a()),_X2*,f(b()))      -> { return factory.Parse("pattern3") }
    conc(_X1*,f(a()),_X2*,f(b()),_X3*) -> { return factory.Parse("pattern4") }
    conc(f(b()),_X2*,f(b()),_X3*)      -> { return factory.Parse("pattern5") }
    conc(_X1*,f(c()),f(_x),_X3*)       -> { return factory.Parse("pattern6") }
    conc()                             -> { return factory.Parse("pattern7") }
  }
  return res
}

// ---------------------------------------------------------------- Match2

func TestMatch2(t *testing.T) {
  for _, d := range match2Data() {
    question, answer := d[0], d[1]
    assertSame(t,
      "TestMatch2 : expected "+answer+" for term "+question +"",
      match2(factory.Parse(question)),
      factory.Parse(answer))
  }
}

func match2Data() [][2]string {
  return [][2]string {
      { "h(a,l([f(a)]))"                  , "pattern1" },
      { "h(l([f(a)]),a)"                  , "pattern2" },
      { "h(l([a,b,a,b]),a)"               , "pattern3" },
      { "h(l([a,b,a,b]),l([a,a,b,a]))"    , "fail"     },
      { "h(l([a,b,a,b]),l([a,a,b,a,b]))"  , "pattern4" },
      { "l([b,b,l([a]),a,l([a,c]),b,c])"  , "fail"     },
      { "l([a,b,l([a]),a,l([a,c]),b,c])"  , "pattern5" },
      { "l([a,b,l([c]),a,b,l([a,c]),b,c])", "fail"     },
      { "l([a,b,l([c]),a,c,l([a,c]),b,c])", "pattern5" },
  }
}

func match2(t aterm.ATerm) aterm.ATerm {
  res := fail
  %match(E t) {
    h(a(),l(conc(f(a()))))                       -> { return factory.Parse("pattern1") }
    h(l(conc(f(a()))),a())                       -> { return factory.Parse("pattern2") }
    h(l(conc(_X1*,_x,y,_X2*)),z)                 -> { if `y==`z { return factory.Parse("pattern3") } }
    h(l(conc(_X1*,_x,X2*)),l(conc(_Y1*,_y,Y2*))) -> { if `X2==`Y2 && !(`X2).IsEmpty() {
                  return factory.Parse("pattern4") } }
    l(conc(_X1*,Y1*,_X2*,l(conc(Y2*)),_X3*))     -> { if `Y1==`Y2 { return factory.Parse("pattern5") } }
  }
  return res
}

// ---------------------------------------------------------------- Match3

func TestMatch3(t *testing.T) {
  for _, d := range match3Data() {
    question, answer := d[0], d[1]
    assertSame(t,
      "TestMatch3 : expected "+answer+" for term "+question +"",
      match3(factory.Parse(question)),
      factory.Parse(answer))
  }
}

func match3Data() [][2]string {
  return [][2]string {
      { "l([f(a)])"                     , "pattern1" },
      { "l([f(a),f(b),f(a),f(c)])"      , "pattern2" },
      { "l([g(f(a)),f(b),g(f(a)),f(c)])", "pattern3" },
  }
}

func match3(t aterm.ATerm) aterm.ATerm {
  res := fail
  %match(E t) {
    l(vl@conc(_x)) -> { if `vl==factory.Parse("[f(a)]") { return factory.Parse("pattern1") } }
    l(conc(_X1*,vx@f(_x),_X2*,vy@f(_y),_X3*)) -> { if `vx==`vy { return factory.Parse("pattern2") } }
    l(conc(_X1*,g(vx@f(_x)),_X2*,g(vy@f(_y)),_X3*)) -> { if `vx==`vy { return factory.Parse("pattern3") } }
  }
  return res
}

// ---------------------------------------------------------------- MatchOther

func TestMatch4(t *testing.T) {
  nbSol := 0
  l := factory.Parse("[a,b]").(*aterm.ATermList)
    %match(L l) {
      conc(_X1*,_X2*,_X3*) -> {
      nbSol++
      //System.out.println("X1 = " + `X1* + " X2 = " + `X2*+ " X3 = " + `X3*);
    }
  }
  assertTrue(t, "TestMatch4",nbSol==6)
}

func TestMatch5(t *testing.T) {
  nbSol := 0
  l := factory.Parse("[l([a,b]),a,b]").(*aterm.ATermList)
  %match(L l) {
    conc(l(conc(_R*,_T*)),_X1*,_u,_X2*) -> {
      nbSol++
      //System.out.println("R = " + `R* + " T = " + `T*+" X1 = " + `X1* + " X2 = " + `X2*+ " u = " + `u);
    }
  }
  assertTrue(t, "TestMatch5",nbSol==6)
}

// ---------------------------------------------------------------- Match6

func TestMatch6(t *testing.T) {
  for _, d := range match6Data() {
    question, answer := d[0], d[1]
    assertSame(t,
      "TestMatch6 : expected "+answer+" for term "+question +"",
      match6(factory.Parse(question)),
      factory.Parse(answer))
  }
}

func match6Data() [][2]string {
  return [][2]string {
      { "[]"                         , "fail" },
      { "[f(a)]"                     , "ok"   },
      { "[f(a),f(b),f(a),f(c)]"      , "ok"   },
      { "[g(f(a)),f(b),g(f(a)),f(c)]", "ok"   },
  }
}

func match6(l aterm.ATerm) aterm.ATerm {
  ll := l.(*aterm.ATermList)
  %match(L ll) {
    conc(_,_*) -> {
      return factory.Parse("ok")
    }
  }
  return factory.Parse("fail")
}

// org.junit.Assert.assertTrue / assertSame
func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}

func assertSame(t *testing.T, message string, expected, actual aterm.ATerm) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected same:<%v> was not:<%v>", message, expected, actual)
  }
}
