package sl

/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the Inria nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

// Port of test/sl/TestReflective.t: strategies matched and rewritten as
// terms.
// port: `import sl.testsl.types.*;` (implicit in the original through
// the testsl mapping) is the dot import of the Go package that gomgo
// generates from testsl.gom (sub-directory testsl).
// port: tom.library.sl.Strategy is sl.Strategy; Java's visit/visitLight
// throw VisitFailure, the Go Visit/VisitLight return it: visit below
// fails the test on it, as the exception escaping the JUnit test does.
// port: string concatenation with a strategy ("…"+s) is fmt.Sprint.
// port: testcountAll1/3/4/5 are TestCountAll1/3/4/5: go test only runs
// functions whose name continues with an upper case after Test.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "testing"

  . "fixture/testsl"
  "tom/library/sl"
)

  %include { sl.tom }
  %include { testsl/testsl.tom }

func TestMatchId(t *testing.T) {
    var s sl.Strategy = `Identity()
    %match(s) {
      Identity() -> { return }
    }
    fail(t, "Identity should have matched")
  }

func TestMatchIdFail(t *testing.T) {
    var s sl.Strategy = `Fail()
    %match(s) {
      Identity() -> { fail(t, "Fail should not match") }
    }
  }

func TestMatchS1(t *testing.T) {
    var s sl.Strategy = `S1()
    %match(s) {
      S1() -> { return }
    }
    fail(t, "S1 should have matched")
  }

func TestMatchS1Fail(t *testing.T) {
    var s sl.Strategy = `Fail()
    %match(s) {
      S1() -> { fail(t, "Fail should not match") }
    }
  }

func TestMatchSequence1(t *testing.T) {
    var s sl.Strategy = `Sequence(Identity(), Fail())
    %match(s) {
      Sequence(Identity(), Fail()) -> { return }
    }
    fail(t, "match should success")
  }

func TestMatchSequence2(t *testing.T) {
    var s sl.Strategy = `Sequence(Identity(), S1())
    %match(Strategy s) {
      Sequence(Identity(), Fail()) -> { fail(t, "should not match") }
    }
  }

func TestMatchSequence3(t *testing.T) {
    var s sl.Strategy = `Sequence(Identity(), S1())
    %match(s) {
      Sequence(Identity(), S1()) -> { return }
    }
    fail(t, "match should success")
  }

func TestMatchSequence4(t *testing.T) {
    var s sl.Strategy = `Sequence(Identity(), Fail())
    %match(s) {
      Sequence(Identity(), S1()) -> { fail(t, "match should not success") }
    }
  }

func TestMatchSequenceVar(t *testing.T) {
    var s sl.Strategy = `Sequence(Identity(), Fail())
    %match(s) {
      Sequence(s1, s2) -> { 
        %match(s1, s2) {
          Identity(), Fail() -> {
            return
          }
        }
      }
    }
    fail(t, "match should success")
  }

func TestSequenceVarS1(t *testing.T) {
    var s sl.Strategy = `Sequence(Identity(), S1())
    %match(s) {
      Sequence(s1, s2) -> {
        %match(s1, s2) {
          Identity(), S1() -> { return }
        }
      }
    }
    fail(t, "match should success")
  }

func TestMatchS2(t *testing.T) {
    var s sl.Strategy = `S2(0, "msg", Identity())
    %match(s) {
      S2(i, str, _s1) -> {
        if (`i == 0) {
          // port: Java's String.equals is ==
          if(`str == "msg") {
            return
          }
        }
      }
    }
    fail(t, "match should success")
  }

func TestMatchS2in(t *testing.T) {
    var s sl.Strategy = `S2(1, "msg", Identity())
    %match(s) {
      S2(1, "msg", s1) -> {
        %match(s1) {
          Identity() -> {
            return
          }
        }
      }
    }
    fail(t, "match should success")
  }

func TestMatchS2Fail(t *testing.T) {
    var s sl.Strategy = `S2(1, "msg", S1())
    %match(s) {
      S2(1, "msg", Identity()) -> {
        fail(t, "match should not succeed")
      }
    }
  }

func TestMatchAll1(t *testing.T) {
    var s sl.Strategy = `Identity()
    %match(s) {
      All(All(_x)) -> { fail(t, "no !") }
      All(_x) -> { fail(t, "no !") }
    }
  }

func TestMatchAll2(t *testing.T) {
    var s sl.Strategy = `All(Identity())
    %match(s) {
      All(All(_x)) -> { fail(t, "no !") }
      All(_x) -> { return }
    }
    fail(t, "no !")
  }

func TestMatchAll3(t *testing.T) {
    var s sl.Strategy = `All(All(Identity()))
    %match(s) {
      All(All(_x)) -> { return }
      All(_x) -> { fail(t, "no !") }
    }
    fail(t, "no !")
  }

func TestS1Id(t *testing.T) {
    var s sl.Strategy = visit(t, `S1(), `Identity()).(sl.Strategy)
    %match(s) {
      Identity() -> { fail(t, "Id should rewrite to fail") }
      Fail() -> { return }
    }
    fail(t, "should not be here")
  }

func TestS1Fail(t *testing.T) {
    var s sl.Strategy = visit(t, `S1(), `Fail()).(sl.Strategy)
    %match(s) {
      Fail() -> { fail(t, "Fail should rewrite to Identity") }
      Identity() -> { return }
    }
    fail(t, "should not be here")
  }

func TestS3Id(t *testing.T) {
    var s sl.Strategy = visit(t, `S3(), `Identity()).(sl.Strategy)
    %match(s) {
      Identity() -> { return }
    }
    fail(t, "should not be here")
  }

func TestS3All(t *testing.T) {
    var s sl.Strategy = visit(t, `S3(), `All(Identity())).(sl.Strategy)
    %match(s) {
      All[] -> { return }
    }
    fail(t, "should not be here")
  }

func TestS3AllAllvisit(t *testing.T) {
    if r, err := `S3().VisitLight(`All(All(Identity())), sl.VisitableIntrospector{}); err == nil {
      var s sl.Strategy = r.(sl.Strategy)
      %match(s) {
        All(All(Identity())) -> { fail(t, "S3 did not rewrite s") }
        All(Identity()) -> { return }
      }
    } else {
      fail(t, "should not catch exception")
    }
    fail(t, "should not be here")
  }

func TestS3AllAll(t *testing.T) {
    var s sl.Strategy = visit(t, `S3(), `All(All(Identity()))).(sl.Strategy)
    %match(s) {
      All(All(Identity())) -> { fail(t, "S3 did not rewrite s") }
      All(Identity()) -> { return }
    }
    fail(t, "should not be here with "+fmt.Sprint(s))
  }

func TestS3Allllll(t *testing.T) {
    var s sl.Strategy = visit(t, `S3(), `All(All(All(All(Identity()))))).(sl.Strategy)
    %match(s) {
      All(Identity()) -> { fail(t, "should not be here") }
    }
  }

func TestAlllllBU(t *testing.T) {
    var s sl.Strategy = visit(t, `BottomUp(S3()), `All(All(All(All(Identity()))))).(sl.Strategy)
    %match(s) {
      All(Identity()) -> { return }
    }
    fail(t, "should not be here with "+fmt.Sprint(s))
  }

func TestCountAll1(t *testing.T) {
    var s sl.Strategy = `All(S2(0, "", All(All(Identity()))))
    assertEquals(t, "countAll should return 3 with "+fmt.Sprint(s), 3, countAll(t, s))
  }

func TestCountAll2(t *testing.T) {
    var s sl.Strategy = `All(S2(0, "", All(All(Identity()))))
    assertEquals(t,
        "countAll should return 2 with `BottomUp(S3()).visit(`"+fmt.Sprint(s)+")", 2,
        countAll(t, visit(t, `BottomUp(S3()), s).(sl.Strategy)))
  }

func TestStratNoStrat(t *testing.T) {
    assertEquals(t, "S3 on something that is not a strat fails back to Id",
        `a(),visit(t, `S3(), `a()))
  }

func TestStratNoStratFail(t *testing.T) {
    if _, err := `S4(S1(),0,S1()).Visit(`a(), sl.VisitableIntrospector{}); err == nil {
      fail(t, "This application should fail")
    } else {
      return
    }
    fail(t, "The Exception should have been catched")
  }

func TestCountAll3(t *testing.T) {
    var s sl.Strategy = `All(S4(All(All(Identity())), 0, All(All(All(Identity())))))
    assertEquals(t, "countAll should return 6 with `"+fmt.Sprint(s), 6, countAll(t, s))
  }

func TestCountAll4(t *testing.T) {
    var s sl.Strategy = `All(S4(All(All(Identity())), 0, All(All(All(Identity())))))
    assertEquals(t,
        "countAll should return 3 with `BottomUp(S3()).visit("+fmt.Sprint(s)+")", 3,
        countAll(t, visit(t, `BottomUp(S3()), s).(sl.Strategy)))
  }

func TestCountAll5(t *testing.T) {
    var s sl.Strategy = `S4(Identity(), 0, Fail())
    assertEquals(t,
        "countAll should return 2 with `BottomUp(S5()).visit(`"+fmt.Sprint(s)+")", 2,
        countAll(t, visit(t, `BottomUp(S5()), s).(sl.Strategy)))
  }

// port: unsupported testMake: gomgo represents the variadic operator
// l(Term*) of testsl.gom as a flat list, without the Consl/Emptyl
// constructors of Java Gom, hence without Make_Consl/Make_Emptyl.
//func TestMake(t *testing.T) {
//    var s sl.Strategy = `Make_Consl(Make_a(),Make_Emptyl())
//    %match(s) {
//      Make_Consl(x,Make_Emptyl()) -> { return }
//    }
//    fail(t, "match should success")
//  }

func TestMakeWithBuiltin(t *testing.T) {
    var s sl.Strategy = `Make_m(1)
    %match(s) {
      Make_m(_) -> { return }
    }  
    fail(t, "match should success")
  }

  %strategy S1() extends `Identity() {
    visit Strategy {
      Identity() -> { return `Fail(); }
      Fail() -> { return `Identity(); }
    }
  }

  %strategy S2(i:int, str:String, s:Strategy) extends `Identity() {
    visit Strategy {
      Identity() -> { return `Fail(); }
    }
  }

  %strategy S3() extends `Identity() {
    visit Strategy {
      All(All(x)) -> { return `All(x); }
    }
  }

  %strategy S4(s1:Strategy, i:int, s2:Strategy) extends `Fail() {
    visit Strategy {
      Identity() -> { return `s1; }
      Fail() -> { return `All(s2); }
    }
  }

  %strategy S5() extends `Identity() {
    visit Strategy {
      Fail() -> { return `All(Identity()); }
    }
  }

  // count the number of All nodes
  type Counter struct{ count int }
  %typeterm Counter {
    implement { *Counter }
    is_sort(t) { func() bool { _, ok := (t).(*Counter); return ok }() }
  }
  // port: countAll is a function (the original's is an instance method)
  // that fails the test on a VisitFailure (the original throws it).
  func countAll(t *testing.T, s sl.Strategy) int {
    var c *Counter = &Counter{}
    visit(t, `TopDown(incAll(c)), s)
    return c.count
  }

  %strategy incAll(c:Counter) extends `Identity() {
    visit Strategy {
      // port: host code reads the strategy parameter as a field of the
      // receiver (c.count++ in Java).
      All(_) -> { tom__strat.c.count++ }
    }
  }

// s.visit(subject), whose VisitFailure the original lets escape the test.
func visit(t *testing.T, s sl.Strategy, subject any) any {
  t.Helper()
  r, err := s.Visit(subject, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  return r
}

// org.junit.Assert.assertEquals(message, expected, actual)
func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}

// org.junit.Assert.fail(message)
func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
