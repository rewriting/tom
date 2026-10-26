package main

// Compiled with --inline -go: the hooks (is_fsym, get_slot...) are not
// inlined by the Go backend.

import "fmt"

%include { int.tom }

%typeterm Nat { implement { int64 } is_sort(t) { true } equals(a,b) { a == b } }
%op Nat zero() { is_fsym(t) { t == 0 } make() { int64(0) } }
%op Nat suc(p:Nat) {
  is_fsym(t) { t > 0 }
  get_slot(p,t) { t - 1 }
  make(t) { t + 1 }
}

func main() {
  var n int64 = 2
  %match(Nat n) {
    suc(suc(zero())) -> { fmt.Println("two") }
    _ -> { fmt.Println("other") }
  }
  fmt.Println(`suc(suc(zero())) == n)
}
