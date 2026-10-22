package main

// Environment-based visit: a rule reads its position through
// tom__strat.GetEnvironment() (Java getEnvironment()), a strategy runs
// another one at its own position (Java s.visit(getEnvironment())),
// and Visit/VisitLight agree on rewrites.

import (
  "fmt"

  "tom/tomgo/stable/library/sl"
)

%include { sl.tom }
%gom {
  module sig
  abstract syntax
  T1 = a() | b() | c()
    | f(x:T1)
    | g(x:T1, y:T1)
}

%typeterm Positions {
  implement { *[]string }
}

%strategy Record(out:Positions) extends Identity() {
  visit T1 {
    a() -> { *tom__strat.out = append(*tom__strat.out, tom__strat.GetEnvironment().GetPosition().String()) }
  }
}

%strategy Rename() extends Identity() {
  visit T1 {
    a() -> b()
  }
}

// at a g(_,_), rewrite its first child in place, through the
// environment of this strategy
%strategy AtG() extends Identity() {
  visit T1 {
    g(_,_) -> {
      env := tom__strat.GetEnvironment()
      env.Down(1)
      r, err := `Rename().VisitWith(env, introspector)
      env.Up()
      if err == nil { fmt.Println("AtG", env.GetPosition(), "rewrote", r) }
      return env.GetSubject().(T1)
    }
  }
}

func main() {
  intro := sl.VisitableIntrospector{}
  subject := `g(f(a()), g(a(), b()))

  var positions []string
  res, err := `BottomUp(Record(&positions)).Visit(subject, intro)
  fmt.Println(res, err, positions)

  light, err1 := `TopDown(Rename()).VisitLight(subject, intro)
  env, err2 := `TopDown(Rename()).Visit(subject, intro)
  fmt.Println(light, err1, env, err2, light == env)

  res, err = `TopDown(AtG()).Visit(subject, intro)
  fmt.Println(res, err)

  p := sl.MakePositionFromArray([]int{2, 1})
  res, err = p.GetReplace(`c()).Visit(subject, intro)
  fmt.Println(p, res, err)
  _, err = `Omega(3, Identity()).Visit(subject, intro)
  fmt.Println(err)
}
