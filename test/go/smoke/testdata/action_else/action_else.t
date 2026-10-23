package main

// An if/else chain, composite literals and a comment before else in the
// host code of a %match action. The -np parser makes each { … } of the
// host code a block; written as a block of the Go backend ("{ … };"), it
// ends the if statement before "else" and breaks every expression
// around a literal (T{}, f(T{1, 2}), T{}.m(), []T{{…}, {…}}, []T{…}[0],
// T{…} == T{…}, func() { … }()). Under -go, AstBuilder keeps these braces
// as host text.
// Same output as the Tom+Java twin (twin/) compiled by legacy/.

import "fmt"

%include { int.tom }

type pair struct{ a, b int64 }

type outer struct{ p pair }

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
        } /* c */ else if `x == 3 {
          ps := []pair{{`x, 1}, {2, 3}}
          o := outer{pair{1, 2}}
          v := []int64{`x, 1}[0]
          eq := pair{1, 2} == pair{1, 2}
          if eq { return ps[1].b + o.p.b + v + 200 }
          return 0
        } else {
          if i > 5 { return 50 } else { return 30 }
        }
    }
  }
  return 0
}

func main() { fmt.Println(f(0), f(1), f(2), f(3), f(4), f(6)) }
