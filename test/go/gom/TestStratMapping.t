package gom

// Port of test/gom/TestStratMapping.t: the congruence strategies of
// %gom(--withCongruenceStrategies).
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.teststratmapping.m.types.*;`.
// port: JUnit's ErrorCollector (@Rule collector) is the *testing.T
// itself: collector.checkThat(x, is(y)) and collector.addError(e)
// report an error and let the test go on, as t.Errorf does.
// port: tom.library.sl.Strategy is sl.Strategy; Java's visit throws
// VisitFailure, the Go Visit returns it (the try/catch blocks test it),
// and t is only assigned when there is no failure, as in Java.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  "tom/library/sl"
)

  %include { sl.tom }
  %gom(--withCongruenceStrategies) {
    module M
    abstract syntax
    S = f(h:S,t:S)
      | a() | b()

    S2 = g(s:S2) | c()
  }

  %strategy Strat() extends Identity() {
    visit S {
      f(x,_y) -> { return `x; }
    }
  }

func TestCongruenceConstant(collector *testing.T) {
    var t S = `f(f(a(),b()),f(b(),a()))
    var s1 sl.Strategy = `_f(Strat(),Strat())
    var s2 sl.Strategy = `When_f(Strat())
    var s3 sl.Strategy = `Is_a()
    var s4 sl.Strategy = `Is_f()
    var s5 sl.Strategy = `Make_f(Make_a(),Make_c())
    var s6 sl.Strategy = `Make_a()
    if r, ee := s1.Visit(t, sl.VisitableIntrospector{}); ee == nil {
      t = r.(S)
      checkThat(collector, t, is(`f(a(),b())))
    } else {
      addError(collector, "s1 failed")
    }

    if r, ee := s2.Visit(t, sl.VisitableIntrospector{}); ee == nil {
      t = r.(S)
      checkThat(collector, t,is(`a()))
    } else {
      addError(collector, "s2 failed")
    }

    if r, ee := s3.Visit(t, sl.VisitableIntrospector{}); ee == nil {
      t = r.(S)
    } else {
      addError(collector, "s3 failed")
    }

    if r, ee := s4.Visit(t, sl.VisitableIntrospector{}); ee == nil {
      t = r.(S)
      addError(collector, "_f() has not failed on a()")
    } else {
    }

    if r, ee := s5.Visit(t, sl.VisitableIntrospector{}); ee == nil {
      t = r.(S)
      addError(collector, "Make_f(Make_a(),Make_c()) is not well formed and has not failed")
    } else {
    }

    if r, ee := s6.Visit(t, sl.VisitableIntrospector{}); ee == nil {
      t = r.(S)
      checkThat(collector, t,is(`a()))
    } else {
      addError(collector, "Make_a() failed")
    }
}

// is(s) is org.hamcrest.CoreMatchers.is(s): equality with s (the terms
// are maximally shared, so equals is ==).
func is(s S) func(S) bool {
  return func(x S) bool { return x == s }
}

// ErrorCollector.checkThat(value, matcher)
func checkThat(collector *testing.T, value S, matcher func(S) bool) {
  collector.Helper()
  if !matcher(value) {
    collector.Errorf("got: <%v>", value)
  }
}

// ErrorCollector.addError(new Throwable(message))
func addError(collector *testing.T, message string) {
  collector.Helper()
  collector.Error(message)
}
