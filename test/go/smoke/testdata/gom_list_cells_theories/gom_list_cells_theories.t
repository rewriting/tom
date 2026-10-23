package main

// The theories of a variadic operator (FL, AU with and without neutral,
// AC, ACU) on gomgo's shared cons cells (gom/backend/cells.go), run as
// Java Gom's HookTypeExpander hooks on each Cons<op>: flattening, the
// AU lists ending with their last element (not a list), the list API on
// such lists (Length, ToArray, Reverse, Append, GetTail), and the AC/ACU
// lists kept sorted by CompareTo. The order of the elements of an AC/ACU
// term depends on the hash values (it differs from Java's): only
// order-independent facts are printed. Same output as the Tom+Java twin
// (twin/) compiled by legacy/.

import (
  "fmt"
  "sort"
  "strings"

  _ "tom/library/sl" // the generated th_gen.go needs the Go runtime module
)

%gom {
  module th
  abstract syntax
  T = a() | b() | c() | d()
    | fl(T*)
    | au(T*)
    | nau(T*)
    | ac(T*)
    | acu(T*)
  au:AU() {}
  nau:AU() { `d() }
  ac:AC() {}
  acu:ACU() {}
}

func main() {
  var f T = `fl(a(), fl(b(), c()), fl())
  fmt.Println("FL", f, f.Length(), f.GetTailfl() == `fl(b(), c()), f.GetTailfl().GetTailfl().GetTailfl() == `fl(), f.Reverse())

  var u T = `au(a(), au(b(), c()), au())
  fmt.Println("AU", u, u.Length(), u.GetTailau().GetTailau() == `c(), len(u.(*AuT).ToArray()), u.Reverse(), u.(*AuT).Append(`d()))
  fmt.Println("AU small", `au(a()), `au(), `au(a(), b()).GetTailau(), `au(a(), b()).Length())
  fmt.Println("AU neutral", `nau(a(), d(), b()), `nau(d()), `nau(), `nau(d(), d()), `nau(a(), nau(b(), d())))

  var x T = `ac(b(), ac(a(), c()), a(), ac())
  fmt.Println("AC", x == `ac(a(), a(), b(), c()), x == `ac(c(), ac(b(), a(), a())), x.Length(), canon(x), sorted(x), `ac(a()) == `a())
  var y T = `acu(c(), acu(a(), b()), acu())
  fmt.Println("ACU", y == `acu(a(), b(), c()), y.Length(), canon(y), sorted(y), `acu(a()) == `a(), `acu())
}

// canon prints a list with its elements sorted by their text.
func canon(t T) string {
  var elems []string
  for _, e := range toArray(t) {
    elems = append(elems, fmt.Sprint(e))
  }
  sort.Strings(elems)
  return t.SymbolName() + "[" + strings.Join(elems, ",") + "]"
}

// sorted reports whether the elements of an AC/ACU list are in the
// order of CompareTo.
func sorted(t T) bool {
  elems := toArray(t)
  for i := 0; i+1 < len(elems); i++ {
    if elems[i].(T).CompareTo(elems[i+1]) > 0 {
      return false
    }
  }
  return true
}

func toArray(t T) []any {
  if l, ok := t.(*AcT); ok {
    return l.ToArray()
  }
  return t.(*AcuT).ToArray()
}
