package main

// An if/else chain and composite literals in the host code of a %match
// action: the -np parser makes each { … } of the host code an unamed
// block, which the Go backend writes "{ … };" — but "};" before "else"
// ends the if statement, and inside an expression (T{}, f(T{1, 2}),
// T{}.m(), func() { … }()) breaks it: such a block ends with "}"
// (GoGenerator.buildUnamedBlock).
// Same output as the Tom+Java twin (twin/) compiled by legacy/.

import "fmt"

%include { int.tom }

type pair struct{ a, b int64 }

func (p pair) sum() int64 { return p.a + p.b }

func add(p pair) int64 { return p.a + p.b }

func f(i int64) int64 {
  %match(int i) {
    x -> {
        if `x == 0 {
          return 10
        } else if `x == 1 {
          return `x + 20
        } else if `x == 2 {
          return add(pair{`x, 1}) + pair{}.sum() + func() int64 { return 100 }()
        } else {
          if i > 5 { return 50 } else { return 30 }
        }
    }
  }
  return 0
}

func main() { fmt.Println(f(0), f(1), f(2), f(3), f(6)) }
