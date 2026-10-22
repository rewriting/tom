package main

// Several make hooks of one operator run in order, as in Java Gom
// (OperatorTemplate.generateMakeHooks): an argument reassigned by a hook
// goes on to the next hook (renamed when the next hook names it
// differently) and to the construction; a variadic operator over a
// builtin sort (int*) inserts int64 elements. Java semantics: the bodies
// are inlined in order, a later hook's differently named argument is
// declared from the previous one (`int j = i;`), and the arguments then
// go to realMake (test/gom/TestMultiHook.t expects (10+1)*2 = 22).

import (
  "fmt"

  _ "tom/library/sl" // the generated sig_gen.go needs the Go runtime module
)

%gom {
  module sig
  imports int
  abstract syntax
  W = Int(i:int)
    | OInt(i:int)
    | Pair(x:int, y:int)
  L = conc(int*)
  Int:make(i) {
    i = i+1;
  }
  Int:make(j) {
    j = j*2;
  }
  OInt:make(i) {
    i = i+1;
  }
  OInt:make(i) {
    i = i*2;
  }
  Pair:make(a,b) {
    if (a == b) { return `Int(a); }
  }
  Pair:make(p,q) {
    p = p*10;
  }
}

func main() {
  fmt.Println(`Int(10))
  fmt.Println(`OInt(10))
  fmt.Println(`Pair(3,3))
  fmt.Println(`Pair(1,2))
  var l L = `conc(1,2,3)
  fmt.Println(`conc(0,l*,4))
}
