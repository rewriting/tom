package main

// The inner %match has a subject named like the variable x bound by the
// outer pattern.

import "fmt"

%include { int.tom }

func main() {
  var i int64 = 3
  %match(int i) {
    x -> {
      %match(int x) {
        _ -> { fmt.Println("in") }
      }
    }
  }
}
