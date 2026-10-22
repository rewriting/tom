package main

import "fmt"

%include { int.tom }

func classify(n int64) string {
  %match(n) {
    0 -> { return "zero" }
    x -> { return fmt.Sprint("n=", `x) }
  }
  return "unreachable"
}

func main() { fmt.Println(classify(0), classify(7)) }
