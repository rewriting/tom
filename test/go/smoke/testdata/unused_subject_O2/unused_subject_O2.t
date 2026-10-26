package main

// Same as unused_subject, compiled with -O2: host subjects must stay visible
// in the automata (IsSort tests that are statically true included).
// A host variable that is only the subject of a %match whose patterns
// need not inspect it (_, x@y on a sort whose is_sort is true): the
// compiled automata never reference it (in Java neither), and Go rejects
// an unused local variable. Under -go the compiler writes "_ = s" for each
// variable subject (Compiler.collectHostSubjects), not for a subject bound
// by a pattern of the same rule (y below) and with the Go name of a
// variable of an enclosing action (x). Same output as the Tom+Java twin
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
