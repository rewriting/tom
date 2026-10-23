package main

import "fmt"

%include{ int.tom }
%include{ intarray.tom }
%include{ string.tom }
%include{ Term.tom }

// AC helpers and the int/string mappings in two files of one package.
// X and Y are named and used: with f??(_*,a(),_*) the compiler inlines
// the solution array of the AC loop (a fresh array at each
// next_minimal_extract call, also in Java), which never terminates.
func countB(t T) int {
  n := 0
  %match(t) {
    f??(X*,b(),Y*) -> { _, _ = `X, `Y; n++ }
  }
  return n
}

func headB(s string) string {
  %match(s) {
    concString(x, _*) -> { return fmt.Sprintf("%c", `x) }
  }
  return ""
}

func main() {
  var t T = `f(a(),b(),c())
  fmt.Println("A", countA(t), headA("hello"))
  fmt.Println("B", countB(t), headB("world"))
}
