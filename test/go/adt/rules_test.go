package adt_test

import (
	"testing"

	adt "tom/engine/adt"
)

// The four rewrite rules inherited from the Java rules() modules
// (Code.gom, TomExpression.gom), written in the Go copies as make hooks:
//
//	InstructionToCode(CodeToInstruction(t))   -> t
//	CodeToInstruction(InstructionToCode(t))   -> t
//	BQTermToExpression(ExpressionToBQTerm(t)) -> t
//	ExpressionToBQTerm(BQTermToExpression(t)) -> t
//
// Every test checks the result with == (pointer identity of the
// hash-consed term), which also proves maximal sharing.

const stackDepth = 6

func someCode() adt.Code { return adt.MakeTargetLanguageToCode(adt.MakeITL("body")) }

// An Instruction that is not a CodeToInstruction, so that
// CodeToInstruction(InstructionToCode(t)) exercises the hook of
// CodeToInstruction itself (not only the one of InstructionToCode).
func someInstruction() adt.Instruction { return adt.MakeNop() }

func someBQTerm() adt.BQTerm { return adt.MakeBQDefault() }

// An Expression that is not a BQTermToExpression, so that
// BQTermToExpression(ExpressionToBQTerm(t)) exercises the hook of
// BQTermToExpression itself.
func someExpression() adt.Expression {
	return adt.MakeTomInstructionToExpression(adt.MakeNop())
}

func TestRule_InstructionToCode_CodeToInstruction(t *testing.T) {
	c := someCode()
	if got := adt.MakeInstructionToCode(adt.MakeCodeToInstruction(c)); got != c {
		t.Fatalf("InstructionToCode(CodeToInstruction(t)) != t:\n got=%v\nwant=%v", got, c)
	}
}

func TestRule_CodeToInstruction_InstructionToCode(t *testing.T) {
	i := someInstruction()
	if got := adt.MakeCodeToInstruction(adt.MakeInstructionToCode(i)); got != i {
		t.Fatalf("CodeToInstruction(InstructionToCode(t)) != t:\n got=%v\nwant=%v", got, i)
	}
}

func TestRule_BQTermToExpression_ExpressionToBQTerm(t *testing.T) {
	e := someExpression()
	if got := adt.MakeBQTermToExpression(adt.MakeExpressionToBQTerm(e)); got != e {
		t.Fatalf("BQTermToExpression(ExpressionToBQTerm(t)) != t:\n got=%v\nwant=%v", got, e)
	}
}

func TestRule_ExpressionToBQTerm_BQTermToExpression(t *testing.T) {
	b := someBQTerm()
	if got := adt.MakeExpressionToBQTerm(adt.MakeBQTermToExpression(b)); got != b {
		t.Fatalf("ExpressionToBQTerm(BQTermToExpression(t)) != t:\n got=%v\nwant=%v", got, b)
	}
}

// Instructions and expressions that are themselves wrapped values still
// round-trip.
func TestRule_WrappedValues(t *testing.T) {
	i := adt.MakeCodeToInstruction(someCode())
	if adt.MakeCodeToInstruction(adt.MakeInstructionToCode(i)) != i {
		t.Fatalf("CodeToInstruction(InstructionToCode(CodeToInstruction(c))) != CodeToInstruction(c)")
	}
	e := adt.MakeBQTermToExpression(someBQTerm())
	if adt.MakeBQTermToExpression(adt.MakeExpressionToBQTerm(e)) != e {
		t.Fatalf("BQTermToExpression(ExpressionToBQTerm(BQTermToExpression(b))) != BQTermToExpression(b)")
	}
}

// Nested stacks collapse bottom-up: each wrapper pair added on top of the
// previous result disappears, so the final term is t itself.

func TestRule_Stack_InstructionToCode_CodeToInstruction(t *testing.T) {
	c := someCode()
	x := c
	for k := 0; k < stackDepth; k++ {
		x = adt.MakeInstructionToCode(adt.MakeCodeToInstruction(x))
		if x != c {
			t.Fatalf("depth %d: stack did not collapse to t: %v", k+1, x)
		}
	}
	// the same stack written as one nested expression
	y := adt.MakeInstructionToCode(adt.MakeCodeToInstruction(adt.MakeInstructionToCode(adt.MakeCodeToInstruction(adt.MakeInstructionToCode(adt.MakeCodeToInstruction(c))))))
	if y != c {
		t.Fatalf("nested expression did not collapse to t: %v", y)
	}
}

func TestRule_Stack_CodeToInstruction_InstructionToCode(t *testing.T) {
	i := someInstruction()
	x := i
	for k := 0; k < stackDepth; k++ {
		x = adt.MakeCodeToInstruction(adt.MakeInstructionToCode(x))
		if x != i {
			t.Fatalf("depth %d: stack did not collapse to t: %v", k+1, x)
		}
	}
	y := adt.MakeCodeToInstruction(adt.MakeInstructionToCode(adt.MakeCodeToInstruction(adt.MakeInstructionToCode(adt.MakeCodeToInstruction(adt.MakeInstructionToCode(i))))))
	if y != i {
		t.Fatalf("nested expression did not collapse to t: %v", y)
	}
}

func TestRule_Stack_BQTermToExpression_ExpressionToBQTerm(t *testing.T) {
	e := someExpression()
	x := e
	for k := 0; k < stackDepth; k++ {
		x = adt.MakeBQTermToExpression(adt.MakeExpressionToBQTerm(x))
		if x != e {
			t.Fatalf("depth %d: stack did not collapse to t: %v", k+1, x)
		}
	}
	y := adt.MakeBQTermToExpression(adt.MakeExpressionToBQTerm(adt.MakeBQTermToExpression(adt.MakeExpressionToBQTerm(adt.MakeBQTermToExpression(adt.MakeExpressionToBQTerm(e))))))
	if y != e {
		t.Fatalf("nested expression did not collapse to t: %v", y)
	}
}

func TestRule_Stack_ExpressionToBQTerm_BQTermToExpression(t *testing.T) {
	b := someBQTerm()
	x := b
	for k := 0; k < stackDepth; k++ {
		x = adt.MakeExpressionToBQTerm(adt.MakeBQTermToExpression(x))
		if x != b {
			t.Fatalf("depth %d: stack did not collapse to t: %v", k+1, x)
		}
	}
	y := adt.MakeExpressionToBQTerm(adt.MakeBQTermToExpression(adt.MakeExpressionToBQTerm(adt.MakeBQTermToExpression(adt.MakeExpressionToBQTerm(adt.MakeBQTermToExpression(b))))))
	if y != b {
		t.Fatalf("nested expression did not collapse to t: %v", y)
	}
}

// Sharing: a term built twice from scratch is the same pointer, before
// and after the rewriting.

func TestRule_Sharing_TermBuiltTwice(t *testing.T) {
	build := func() (adt.Code, adt.Instruction, adt.BQTerm, adt.Expression) {
		c := adt.MakeTargetLanguageToCode(adt.MakeITL("body"))
		b := adt.MakeBQDefault()
		return adt.MakeInstructionToCode(adt.MakeCodeToInstruction(c)),
			adt.MakeCodeToInstruction(adt.MakeInstructionToCode(adt.MakeCodeToInstruction(c))),
			adt.MakeExpressionToBQTerm(adt.MakeBQTermToExpression(b)),
			adt.MakeBQTermToExpression(adt.MakeExpressionToBQTerm(adt.MakeBQTermToExpression(b)))
	}
	c1, i1, b1, e1 := build()
	c2, i2, b2, e2 := build()
	if c1 != c2 || i1 != i2 || b1 != b2 || e1 != e2 {
		t.Fatalf("a term built twice is not shared")
	}
	if c1 != someCode() || i1 != adt.MakeCodeToInstruction(someCode()) || b1 != someBQTerm() ||
		e1 != adt.MakeBQTermToExpression(someBQTerm()) {
		t.Fatalf("rewritten terms are not pointer-equal to the original terms")
	}
}
