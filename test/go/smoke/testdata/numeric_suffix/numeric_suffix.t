package main

// Java long literals (32l, 7L, octal 017L), in patterns and backquotes, lose their
// suffix in the Go code (GoGenerator.generateBQTerm); a Go 017 is octal
// too. (A hexadecimal 0x1Fl is not parsed as a Tom constant by the
// island parser, in legacy/ either.)

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
    017L -> { return "017L" }
  }
  return "other"
}

func main() {
  fmt.Println(classify(32), classify(1234567890), classify(-5), classify(7), classify(15))
  var l int64 = `L(040L)
  fmt.Println(l)
}
