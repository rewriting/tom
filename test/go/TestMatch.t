// Port of test/TestMatch.t: the Java aterm library is replaced by its Go
// port tom/library/aterm (SingletonFactory.getInstance() is
// aterm.GetInstance()); ATermAppl casts are type assertions.
// port: @BeforeClass/@AfterClass become TestMain; the inner classes
// TestData/TestData2 are Go structs; assertSame/assertTrue are helpers
// (assertSame compares the terms with ==, Java's identity).
// port: no main — `go test` runs the tests (the original main only launched JUnit).
package testmatch

import (
  "os"
  "testing"

  "tom/library/aterm"
)

var ok, fail aterm.ATerm
var pattern1, pattern2, pattern3, pattern4, pattern5 aterm.ATerm
 
var factory *aterm.PureFactory = aterm.GetInstance()

func TestMain(m *testing.M) {
  setUp()
  code := m.Run()
  clean()
  os.Exit(code)
}
	
func setUp() {
    ok   = factory.Parse("ok")
    fail = factory.Parse("fail")
    pattern1 = factory.Parse("pattern1")
    pattern2 = factory.Parse("pattern2")
    pattern3 = factory.Parse("pattern3")
    pattern4 = factory.Parse("pattern4")
    pattern5 = factory.Parse("pattern5")
  }

func clean() {
    ok   = nil
    fail = nil
    pattern1 = nil
    pattern2 = nil
    pattern3 = nil
    pattern4 = nil
    pattern5 = nil
  }

  %typeterm L {
    implement { *aterm.ATermList }
    is_sort(t) { $t != nil }
    equals(l1,l2)  { $l1 == $l2 }
  }

  %oplist L conc( E* ) {
    is_fsym(t)     { $t != nil }
    make_empty()   { factory.MakeListEmpty() }
    make_insert(e,l) { any($l).(*aterm.ATermList).Insert($e) }
    get_head(l)    { any($l).(*aterm.ATermList).GetFirst() }
    get_tail(l)    { any($l).(*aterm.ATermList).GetNext() }
    is_empty(l)    { any($l).(*aterm.ATermList).IsEmpty() }
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

  %op E d() {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "d" }
  }

  %op E f(s1:E) {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "f" }
    get_slot(s1,t) { any($t).(*aterm.ATermAppl).GetArgument(0)  }
  }

  %op E ff(s1:E) {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "ff" }
    get_slot(s1,t) { any($t).(*aterm.ATermAppl).GetArgument(0)  }
  }

  %op E g(s1:E, s2:E) {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "g" }
    get_slot(s1,t) { any($t).(*aterm.ATermAppl).GetArgument(0)  }
    get_slot(s2,t) { any($t).(*aterm.ATermAppl).GetArgument(1)  }
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

  %op E k(s2:E,s1:E) {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "k" }
    get_slot(s2,t) { any($t).(*aterm.ATermAppl).GetArgument(0)  }
    get_slot(s1,t) { any($t).(*aterm.ATermAppl).GetArgument(1)  }
  }

type TestData struct {
		question string
		answer string
	}

type TestData2 struct {
		question1 string
		question2 string
		answer string
	}

 func Test1(t *testing.T) {
		TEST := []TestData {
			{"g(a,a)"                     ,"pattern1"},
			{"g(a,b)"                     ,"fail    "},
			{"h(f(b),f(b))"               ,"pattern2"},
			{"h(f(a),f(b))"               ,"fail    "},
			{"h(f(f(g(a,b))),g(b,g(a,b)))","pattern3"},
			{"h(f(f(g(a,b))),g(b,g(b,b)))","fail    "},
			{"h(b,g(b,b))"                ,"pattern4"},
			{"h(a,g(b,b))"                ,"fail    "},
		};

		for i := 0; i<len(TEST);i++ {
			assertSame(t,
				"TestMatch1 expected "+TEST[i].answer+" for match1("+TEST[i].question+")",
				match1(factory.Parse(TEST[i].question)),
				factory.Parse(TEST[i].answer),
				)
			assertSame(t,
				"TestMatch1 expected "+TEST[i].answer+" for match1bis("+TEST[i].question+")",
				match1bis(factory.Parse(TEST[i].question)),
				factory.Parse(TEST[i].answer),
				)
		}
  }

  func match1(t aterm.ATerm) aterm.ATerm {
    res := fail
    %match(E t) {
      g(x,x)            -> { return pattern1 }
      h(f(x),f(x))      -> { return pattern2 }
      h(f(f(x)),g(_y,x)) -> { return pattern3 }
      h(x,g(x,x))       -> { return pattern4 }
    }
    return res
  }

  func match1bis(t aterm.ATerm) aterm.ATerm {
    res := fail
    %match {
      g(x,x) << t             -> { return pattern1 }
      h(f(x),f(x)) << t       -> { return pattern2 }
      h(f(f(x)),g(_y,x)) << t -> { return pattern3 }
      h(x,g(x,x)) << t        -> { return pattern4 }
      _ << t                  -> { return res }
    }
    return res
  }

 func Test2(t *testing.T) {
		TEST := []TestData2 {
			{"g(a,b)"      ,"f(a)","pattern1 "},
			{"g(b,b)"      ,"f(a)","fail     "},
			{"g(f(a),f(a))","f(a)","pattern2 "},
			{"g(f(b),f(a))","f(a)","fail     "},
		};

		for i := 0; i<len(TEST);i++ {
			assertSame(t,
				"TestMatch2 expected "+TEST[i].answer+" for match2("+TEST[i].question1+","+TEST[i].question2+")",
				match2(factory.Parse(TEST[i].question1),factory.Parse(TEST[i].question2)),
				factory.Parse(TEST[i].answer),
				)
		}
  }

  func match2(t1, t2 aterm.ATerm) aterm.ATerm {
    res := fail
    %match(E t1, E t2) {
      g(x,_y), f(x)       -> { return pattern1 }
      g(f(x),f(x)), f(x) -> { return pattern2 }
    }
    return res
  }

func Test3(t *testing.T) {
		TEST := []TestData {
			{"h(l([a,b,a,b]),a)"               ,"pattern1"},
			{"h(l([a,b,a,b]),l([a,a,b,a]))"    ,"fail    "},
			{"h(l([a,b,a,b]),l([a,a,b,a,b]))"  ,"pattern2"},
			{"l([b,b,l([a]),a,l([a,c]),b,c])"  ,"fail    "},
			{"l([a,b,l([a]),a,l([a,c]),b,c])"  ,"pattern3"},
			{"l([a,b,l([c]),a,b,l([a,c]),b,c])","fail    "},
			{"l([a,b,l([c]),a,c,l([a,c]),b,c])","pattern3"},
		};

		for i := 0; i<len(TEST);i++ {
			assertSame(t,
				"TestMatch3 expected "+TEST[i].answer+" for match3("+TEST[i].question+")",
				match3(factory.Parse(TEST[i].question)),
				factory.Parse(TEST[i].answer),
				)
		}
  }

  func match3(t aterm.ATerm) aterm.ATerm {
    res := fail
    %match(E t) {
      h(l(conc(_X1*,_x,y,_X2*)),y)                -> { return pattern1 }
      h(l(conc(_X1*,_x,X2*)),l(conc(_Y1*,_y,X2*))) -> { if !(`X2).IsEmpty() { return pattern2 } }
      l(conc(_X1*,Y1*,_X2*,l(conc(Y1*)),_X3*))    -> { return pattern3 }
    }
    return res
  }

 func Test4(t *testing.T) {
		TEST := []TestData {
			{"h(a, l([a,b,f(b),a]))",     "pattern1"},
			{"h(a, l([a,b,f(a),b]))",     "fail    "},
			{"h(b, l([a,b,f(a),a]))",     "pattern2"},
			{"h(b, l([b,b,f(a),b]))",     "fail    "},
			{"h(c, l([a,f(a),f(a),a]))",  "pattern3"},
			{"h(c, l([a,f(a),f(b),a]))",  "fail    "},
			{"h(d, l([f(a),f(b),f(a)]))", "pattern4"},
		  {"h(d, l([a,f(a),f(b),a]))",  "fail    "},
		};

		for i := 0; i<len(TEST);i++ {
			assertSame(t,
				"TestMatch4 expected "+TEST[i].answer+" for match4("+TEST[i].question+")",
				match4(factory.Parse(TEST[i].question)),
				factory.Parse(TEST[i].answer),
				)
		}
  }

  func match4(t aterm.ATerm) aterm.ATerm {
    res := fail
    %match(E t) {
      h(a(), l(conc(_X1*,x,f(x),_X2*)))       -> { return pattern1 }
      h(b(), l(conc(_X1*,x,_X2*,f(x),_X3*)))   -> { return pattern2 }
      h(c(), l(conc(_X1*,x,x@f(_),_X2*)))     -> { return pattern3 }
      h(d(), l(conc(_X1*,x,_X2*,x@f(_),_X3*))) -> { return pattern4 }
    }
    return res
  }

 func Test5(t *testing.T) {
    assertTrue(t, pattern1 == match5(factory.Parse("h(a, l([f(a),f(b),f(a),f(c)]))")))
    assertTrue(t, fail == match5(factory.Parse("h(a, l([f(a),f(b),f(c)]))")))

    assertTrue(t, pattern2 == match5(factory.Parse("h(b, l([ff(f(a)),f(b),ff(f(a)),f(c)]))")))
    assertTrue(t, fail == match5(factory.Parse("h(b, l([ff(f(a)),f(b),ff(f(b)),f(c)]))")))
  }

  func match5(t aterm.ATerm) aterm.ATerm {
    res := fail
    %match(E t) {
      h(a(), l(conc(_X1*,vx@f(_),_X2*,vx@f(_),_X3*))) -> { return pattern1 }
      h(b(), l(conc(_X1*,ff(vx@f(_x)),_X2*,ff(vx@f(_y)),_X3*))) -> { return pattern2 }
    }
    return res
  }

 func Test6(t *testing.T) {
    res := match6(factory.Parse("h(a(),b())"))
    assertTrue2(t, "slot s1 of \"h\" is a(), it should match, but got "+res.String(),
        pattern2 == res)
    res = match6(factory.Parse("k(a(),b())"))
    /* -- test for the disjunction bug (TOM-50) --*/
    assertTrue2(t, "slot s1 of \"k\" is b(), it should not match, but got "+res.String(),
        fail     == res)
    res = match6(factory.Parse("k(b(),a())"))
    assertTrue2(t, "slot s1 of \"k\" is a(), it should match, but got "+res.String(),
        pattern3 == res)
  }

  func match6(t aterm.ATerm) aterm.ATerm {
    res := fail
    %match(E t) {
      (h|k)[s1=a()] -> {
        %match(E t) {
          h[s1=a()]     -> { return pattern2 }
          k[s1=a()]     -> { return pattern3 }
        }
        return pattern1
      }
    }
    return res
  }

// org.junit.Assert.assertSame(message, expected, actual): the same term
// (Java's identity, == in Go)
func assertSame(t *testing.T, message string, expected, actual aterm.ATerm) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected same:<%v> was not:<%v>", message, expected, actual)
  }
}

// org.junit.Assert.assertTrue(condition)
func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Error()
  }
}

// org.junit.Assert.assertTrue(message, condition)
func assertTrue2(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}
