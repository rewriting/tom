package main

// The action of a %match is duplicated for each branch of a disjunction
// (||); the copies that cannot be written at their source line are put on
// a single line, which must keep the semicolons Go inserts at the end of
// each line (after an identifier, a literal, ++, a closing bracket or
// return).

import (
  "fmt"

  _ "tom/library/sl" // the generated m_gen.go needs the Go runtime module
)

%gom {
  module M
  abstract syntax
  T = a() | b() | f(x:T, y:T)
}

func count(s T) int {
  cnt := 0
  %match(s) {
    f(x,_) || f(_,x) << s -> {
      var tmp T = `x
      _ = tmp
      cnt++
      fmt.Println("branch", tmp)
      if cnt > 5 {
        return -1
      }
    }
  }
  return cnt
}

func first(s T) string {
  %match(s) {
    f(a(),_) || f(_,a()) << s -> {
      msg := "has a"
      return msg
    }
  }
  return "no a"
}

func main() {
  fmt.Println(count(`f(a(),b())))
  fmt.Println(first(`f(b(),a())))
  fmt.Println(first(`f(b(),b())))
}
