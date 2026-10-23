// Port of test/TestMatchInference.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import testmatchinference.test.types.*;`.
// port: org.junit.Assert.fail is t.Fatal.
// port: the instance method ff is a function.
// port: no main — `go test` runs the tests (the original main only launched JUnit).
package testmatchinference

import "testing"
  
%gom {
  module test
  imports int String
  abstract syntax
  Term = a()
       | f(s:Term)
}

func Test1(t *testing.T) {
    %match(int 5) {
      4 -> { t.Fatal("4 does not match") }
      5 -> { return }
      6 -> { t.Fatal("6 does not match") }
    }
    t.Fatal("5 should match")
  }

func Test2(t *testing.T) {
    %match(5,"foo") {
      5,"goo" -> { t.Fatal("5,goo does not match") }
      5,"bar" -> { t.Fatal("5,bar does not match") }
      5,"foo" -> { return }
      6,"foo" -> { t.Fatal("6,foo does not match") }
    }
    t.Fatal("5,foo should match")
  }

func ff(x Term) Term { return `f(x) }
func Test3(t *testing.T) {
    %match(f(a()),ff(a())) {
      f(a()),a() -> { t.Fatal() }
      a(),f(a()) -> { t.Fatal() }
      f(a()),f(_x) -> { return }
    }
    t.Fatal("a,f(a) hould match")
  }
