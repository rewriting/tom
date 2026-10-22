// Equivalence scenario for Leaf.gom (built-in String slot).
package main

import (
	"fmt"

	"tomgen/leaf"
)

func main() {
	t1 := leaf.MakeLeaf()
	t2 := leaf.MakeLabel("hello")
	t3 := leaf.MakeLabel("hello")
	t4 := leaf.MakeLabel("world")

	fmt.Printf("t1=%v\n", t1)
	fmt.Printf("t2=%v\n", t2)
	fmt.Printf("shared-label=%v\n", t2 == t3)
	fmt.Printf("diff-labels=%v\n", t2 != t4)

	// Accessor: extract the String slot from a Label and print it back.
	lab := t2.(*leaf.LabelLeaf)
	fmt.Printf("label-s=%v\n", lab.S)
}
