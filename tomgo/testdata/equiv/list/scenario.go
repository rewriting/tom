// Equivalence scenario for List.gom (variadic of built-in int).
package main

import (
	"fmt"

	"tomgen/list"
)

func main() {
	a := list.MakeConc(1, 2, 3)
	b := list.MakeConc(1, 2, 3)
	c := list.MakeConc(1, 2, 4)

	fmt.Printf("a=%v\n", a)
	fmt.Printf("shared=%v\n", a == b)
	fmt.Printf("diff=%v\n", a != c)

	// Empty list — built via MakeConc with no args on the Go side.
	empty := list.MakeConc()
	fmt.Printf("empty=%v\n", empty)
	fmt.Printf("shared-empty=%v\n", empty == list.MakeConc())

	// Length via the slice field length (Go) — we just print it.
	fmt.Printf("len=%d\n", len(a.(*list.ConcList).Slots))
}
