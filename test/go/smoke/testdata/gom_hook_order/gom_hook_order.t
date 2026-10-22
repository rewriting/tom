package main

// The hooks of a variadic operator run in declaration order in Java Gom
// (checked on the code legacy/'s Gom generates): a theory declared first
// (FL, AU), then the make_insert hook, then the rules. The Go backend
// runs the theory first and refuses another order. The expected output
// is the one of the same program in Tom+Java compiled by legacy/.

import (
  "fmt"

  _ "tom/library/sl" // the generated sig_gen.go needs the Go runtime module
)

%gom {
  module sig
  abstract syntax
  T = a() | b() | c() | conc(T*) | au(T*)
  conc:FL() {}
  conc:make_insert(head,tail) {
    %match(head) { c() -> { return tail; } }
  }
  au:AU() {}
  module sig:rules() {
    conc(a(), x*) -> conc(b(), x*)
    au(c(), x*) -> au(x*)
  }
}

func main() {
  fmt.Println(`conc(a(), c(), conc(a(), b())), `conc(c(), a()), `au(c(), a(), au(c(), b())), `au(a(), c()))
}
