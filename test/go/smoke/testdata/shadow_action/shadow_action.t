package main

// The action declares its own s (:=) while the outer s is only a subject:
// the outer s still needs "_ = s" (the declaration in the action must not
// make the compiler believe it is read).

import "fmt"

%include { string.tom }

func main() {
  var s string = "a"
  %match(String s) {
    _ -> { s := "b"; fmt.Println(s) }
  }
}
