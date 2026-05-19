// Scenario for the Minimal.gom equivalence test. Prints a stable,
// line-oriented summary of operations on Nop terms. Both the Java
// reference scenario (scenario.java) and this Go scenario must emit
// byte-identical stdout.
package main

import (
	"fmt"

	"tomgen/minimal"
)

func main() {
	t1 := minimal.MakeEmptyNop()
	t2 := minimal.MakeUnaryNop(minimal.MakeEmptyNop())
	t3 := minimal.MakeBinaryNop(minimal.MakeEmptyNop(), minimal.MakeUnaryNop(minimal.MakeEmptyNop()))

	fmt.Printf("t1=%v\n", t1)
	fmt.Printf("t2=%v\n", t2)
	fmt.Printf("t3=%v\n", t3)

	// Sharing: building the same canonical term twice must return the
	// same in-memory instance.
	fmt.Printf("shared-emptynop=%v\n", minimal.MakeEmptyNop() == t1)
	a := minimal.MakeBinaryNop(minimal.MakeEmptyNop(), minimal.MakeEmptyNop())
	b := minimal.MakeBinaryNop(minimal.MakeEmptyNop(), minimal.MakeEmptyNop())
	fmt.Printf("shared-binarynop=%v\n", a == b)

	// Distinct compound terms must NOT share.
	diff := minimal.MakeBinaryNop(minimal.MakeEmptyNop(), minimal.MakeEmptyNop()) !=
		minimal.MakeBinaryNop(minimal.MakeEmptyNop(), minimal.MakeUnaryNop(minimal.MakeEmptyNop()))
	fmt.Printf("different-terms=%v\n", diff)

	// Slot accessors: ls/rs of a freshly built BinaryNop must round-trip
	// to the same canonical sub-terms (we print their toString).
	bn := minimal.MakeBinaryNop(minimal.MakeEmptyNop(), minimal.MakeUnaryNop(minimal.MakeEmptyNop())).(*minimal.BinaryNopNop)
	fmt.Printf("binarynop-ls=%v\n", bn.Ls)
	fmt.Printf("binarynop-rs=%v\n", bn.Rs)
}
