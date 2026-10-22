package main

// Strategies as terms: the Strategy-typed parameters of a %strategy are
// its children after the default strategy (as in Java), so a traversal
// over a strategy reaches them; All/One/Mu expose their slots to %match.

import (
  "fmt"

  "tom/library/sl"
)

%include { sl.tom }

%typeterm Counter {
  implement { *int }
  is_sort(t) { func() bool { _, ok := (t).(*int); return ok }() }
}

%strategy Count(c:Counter) extends Identity() {
  visit Strategy {
    All(_) -> { *tom__strat.c++ }
  }
}

%strategy P(s1:Strategy, i:int, s2:Strategy) extends Fail() {
  visit Strategy {
    Identity() -> { return `s1; }
  }
}

%strategy Unwrap() extends Identity() {
  visit Strategy {
    All(All(x)) -> { return `All(x); }
  }
}

func count(s sl.Strategy) int {
  n := 0
  if _, err := `TopDown(Count(&n)).Visit(s, sl.VisitableIntrospector{}); err != nil {
    panic(err)
  }
  return n
}

func main() {
  p := `P(All(All(Identity())), 0, All(Identity()))
  fmt.Println(p.ChildCount(), count(`All(p)))
  r, err := `BottomUp(Unwrap()).Visit(`All(p), sl.VisitableIntrospector{})
  fmt.Println(count(r.(sl.Strategy)), err)
  // as in Java, the rewrite updated the strategies in place: p's first
  // parameter is now All(Identity())
  %match(p) {
    P(All(x), 0, _) -> { fmt.Println("P(All(x),0,_)", sl.IsAll(`x)) }
  }
  %match(`mu(MuVar("v"), All(MuVar("v")))) {
    Mu(MuVar(n), All(_)) -> { fmt.Println("Mu", `n) }
  }
}
