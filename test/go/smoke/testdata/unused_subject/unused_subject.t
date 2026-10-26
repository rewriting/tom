package main

// A host variable that is only the subject of a %match whose patterns
// need not inspect it (_, x@y on a sort whose is_sort is true): the
// compiled automata never reference it (in Java neither), and Go rejects
// an unused local variable. Under -go the Go backend (GoGenerator,
// readsInstruction) writes "_ = s" before the automaton for each variable
// subject it does not surely read, not for a subject bound by a pattern
// of the same rule (y below) nor for one read in the automaton or in an
// action (x). The compiler (Compiler.t) is the same as for Java. Same output as the Tom+Java twin
// (twin/) compiled by legacy/.

import "fmt"

%include { string.tom }
%include { int.tom }

func main() {
  var s string = "a"
  n := 0
  %match(String s) {
    _ -> { n++ }
    a@b -> { n += 10 }
  }
  var i int64 = 3
  %match(int i) {
    x -> {
      var k int64 = 4
      %match(int x, int k) {
        _, _ -> { n += 100 }
      }
    }
  }
  var j int64 = 5
  %match {
    y << int j && _ << int y -> { n += 1000 }
  }
  fmt.Println(n)
}
