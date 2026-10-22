package main

// Java long literals (32l, 7L), in patterns and backquotes, lose their
// suffix in the Go code (GoGenerator.generateBQTerm).

import "fmt"

%include { long.tom }

%op long L(x:long) {
  is_fsym(t) { true }
  get_slot(x, t) { $t }
  make(x) { $x }
}

func classify(v int64) string {
  %match(long v) {
    32l -> { return "32l" }
    1234567890L -> { return "1234567890L" }
    L(-5l) -> { return "-5l" }
  }
  return "other"
}

func main() {
  fmt.Println(classify(32), classify(1234567890), classify(-5), classify(7))
  var l int64 = `L(40l)
  fmt.Println(l)
}
