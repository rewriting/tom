package main

import "fmt"

// A backquote term ending a line, directly followed by a %match: the
// whitespace between them yields no host block, so the backend must
// separate the two Go statements itself.
type E struct{ name string }

%typeterm E {
  implement { *E }
  is_sort(t) { $t != nil }
  equals(a,b) { $a.name == $b.name }
}

%op E e(n:String) {
  is_fsym(t) { $t != nil }
  get_slot(n,t) { $t.name }
  make(n) { &E{name: $n} }
}

%include { string.tom }

func main() {
  x := `e("a")
  %match(x) {
    e(n) -> { fmt.Println("matched", `n) }
  }
}
