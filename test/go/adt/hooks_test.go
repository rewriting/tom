package adt_test

import (
	"testing"

	adt "tom/engine/adt"
)

// The hooks of src/go/tom/engine/adt/*.gom (AU theories, the rules of
// the Java originals written as make hooks, make and make_insert hooks,
// all in Go) must change the canonical form of certain Tom terms at
// construction time. This file pins down their observable effects so any
// drift in the backend (or in the .gom sources) is caught immediately.

// --- TomConstraint.gom — AndConstraint:AU() { TrueConstraint() } ----------

func TestHook_AndConstraint_EmptyReturnsTrueConstraint(t *testing.T) {
	got := adt.MakeAndConstraint()
	want := adt.MakeTrueConstraint()
	if got != want {
		t.Fatalf("MakeAndConstraint() should return TrueConstraint(), got %T %v", got, got)
	}
}

func TestHook_AndConstraint_AssociativityFlattens(t *testing.T) {
	// Use FalseConstraint as the test payload — TrueConstraint is the unit
	// of AndConstraint and would be absorbed (cf. unit-absorption test below).
	b := adt.MakeFalseConstraint()
	inner := adt.MakeAndConstraint(b, b)
	outer := adt.MakeAndConstraint(inner, b)
	// AndConstraint(AndConstraint(b,b), b) flattens to AndConstraint(b, b, b).
	flat := adt.MakeAndConstraint(b, b, b)
	if outer != flat {
		t.Fatalf("AndConstraint(AndConstraint(b,b), b) did not flatten:\n  got=%v\n  want=%v", outer, flat)
	}
}

// AU hooks absorb the unit element at make-time, matching Java's
// HookTypeExpander behaviour ("if (head == userNeutral) return tail;
// if (tail == userNeutral) return head;"). After absorption a 0-element
// list collapses to the unit, and a 1-element list collapses to the
// bare element.
func TestHook_AndConstraint_AbsorbsTrueConstraintUnit(t *testing.T) {
	u := adt.MakeTrueConstraint()
	b := adt.MakeFalseConstraint()
	if adt.MakeAndConstraint(b, u) != b {
		t.Fatalf("AndConstraint(b, unit) must collapse to b")
	}
	if adt.MakeAndConstraint(u, b) != b {
		t.Fatalf("AndConstraint(unit, b) must collapse to b")
	}
	if adt.MakeAndConstraint(u, u, u) != u {
		t.Fatalf("AndConstraint(unit, unit, unit) must collapse to the unit")
	}
	// 2-element AC with a unit dropped → 1-element → bare element.
	if adt.MakeAndConstraint(u, b, u) != b {
		t.Fatalf("AndConstraint(unit, b, unit) must collapse to b")
	}
	// 3-element AC, two distinct non-units → preserved.
	threeNonUnit := adt.MakeAndConstraint(b, b, b)
	asAndCons, ok := threeNonUnit.(interface{ String() string })
	if !ok {
		t.Fatalf("3-element AC has unexpected concrete type %T", threeNonUnit)
	}
	want := "AndConstraint(FalseConstraint(),FalseConstraint(),FalseConstraint())"
	if got := asAndCons.String(); got != want {
		t.Fatalf("3-element AC: got %q want %q", got, want)
	}
}

func TestHook_OrConstraint_AbsorbsFalseConstraintUnit(t *testing.T) {
	u := adt.MakeFalseConstraint()
	b := adt.MakeTrueConstraint()
	if adt.MakeOrConstraint(b, u) != b {
		t.Fatalf("OrConstraint(b, unit) must collapse to b")
	}
	if adt.MakeOrConstraint(u, b) != b {
		t.Fatalf("OrConstraint(unit, b) must collapse to b")
	}
}

// --- TomConstraint.gom — OrConstraint:AU() { FalseConstraint() } ----------

func TestHook_OrConstraint_EmptyReturnsFalseConstraint(t *testing.T) {
	got := adt.MakeOrConstraint()
	want := adt.MakeFalseConstraint()
	if got != want {
		t.Fatalf("MakeOrConstraint() should return FalseConstraint(), got %T %v", got, got)
	}
}

// --- TomConstraint.gom — OrConstraintDisjunction:AU() { } (no unit) ------

func TestHook_OrConstraintDisjunction_NoUnit_FlattensOnly(t *testing.T) {
	a := adt.MakeTrueConstraint()
	b := adt.MakeFalseConstraint()
	empty := adt.MakeOrConstraintDisjunction()
	if empty == adt.MakeTrueConstraint() || empty == adt.MakeFalseConstraint() {
		t.Fatalf("OrConstraintDisjunction() must not collapse to True/False — no unit declared")
	}
	inner := adt.MakeOrConstraintDisjunction(a, b)
	outer := adt.MakeOrConstraintDisjunction(inner, a)
	flat := adt.MakeOrConstraintDisjunction(a, b, a)
	if outer != flat {
		t.Fatalf("OrConstraintDisjunction must flatten nested instances")
	}
}

// --- Code.gom — module:rules() InstructionToCode ↔ CodeToInstruction -----

func TestHook_CodeRules_InstructionToCode_CancelsInverse(t *testing.T) {
	tl := adt.MakeITL("body")
	codeT := adt.MakeTargetLanguageToCode(tl)
	wrap := adt.MakeCodeToInstruction(codeT)
	got := adt.MakeInstructionToCode(wrap)
	if got != codeT {
		t.Fatalf("InstructionToCode∘CodeToInstruction didn't cancel\n  got=%v\n  want=%v", got, codeT)
	}
}

func TestHook_CodeRules_CodeToInstruction_CancelsInverse(t *testing.T) {
	tl := adt.MakeITL("body")
	codeT := adt.MakeTargetLanguageToCode(tl)
	inst := adt.MakeCodeToInstruction(codeT)
	got := adt.MakeCodeToInstruction(adt.MakeInstructionToCode(inst))
	if got != inst {
		t.Fatalf("CodeToInstruction∘InstructionToCode didn't cancel\n  got=%v\n  want=%v", got, inst)
	}
}

// --- TomExpression.gom — BQTermToExpression ↔ ExpressionToBQTerm ---------

func TestHook_ExpressionRules_BQTermToExpression_CancelsInverse(t *testing.T) {
	bq := adt.MakeBQDefault()
	expr := adt.MakeBQTermToExpression(bq)
	got := adt.MakeExpressionToBQTerm(expr)
	if got != bq {
		t.Fatalf("ExpressionToBQTerm∘BQTermToExpression didn't cancel\n  got=%v\n  want=%v", got, bq)
	}
}

func TestHook_ExpressionRules_ExpressionToBQTerm_CancelsInverse(t *testing.T) {
	bq := adt.MakeBQDefault()
	expr := adt.MakeBQTermToExpression(bq)
	roundtrip := adt.MakeExpressionToBQTerm(expr) // = bq
	got := adt.MakeBQTermToExpression(roundtrip)
	if got != expr {
		t.Fatalf("BQTermToExpression∘ExpressionToBQTerm didn't cancel")
	}
}

// --- TomExpression.gom — Cast:make rejects "unknown type" ----------------

func TestHook_Cast_RejectsUnknownType(t *testing.T) {
	defer func() {
		r := recover()
		if r == nil {
			t.Fatalf("MakeCast with Type(_, \"unknown type\", _) should panic")
		}
		msg, ok := r.(string)
		if !ok || msg != "bad cast" {
			t.Fatalf("unexpected panic value: %v", r)
		}
	}()
	badType := adt.MakeType(adt.MakeConcTypeOption(), "unknown type", adt.MakeEmptyTargetLanguageType())
	_ = adt.MakeCast(badType, adt.MakeBQTermToExpression(adt.MakeBQDefault()))
}

func TestHook_Cast_AcceptsKnownType(t *testing.T) {
	okType := adt.MakeType(adt.MakeConcTypeOption(), "int", adt.MakeEmptyTargetLanguageType())
	got := adt.MakeCast(okType, adt.MakeBQTermToExpression(adt.MakeBQDefault()))
	if _, ok := got.(*adt.CastExpression); !ok {
		t.Fatalf("MakeCast with known type should return *CastExpression, got %T", got)
	}
}

// --- TomInstruction.gom — concInstruction:make_insert splices AbstractBlock --

func TestHook_ConcInstruction_FlattensAbstractBlock(t *testing.T) {
	a := adt.MakeNop()
	b := adt.MakeNop()
	c := adt.MakeNop()
	inner := adt.MakeConcInstruction(a, b)
	block := adt.MakeAbstractBlock(inner)
	withBlock := adt.MakeConcInstruction(block, c)
	want := adt.MakeConcInstruction(a, b, c)
	if withBlock != want {
		t.Fatalf("concInstruction make_insert did not splice AbstractBlock\n  got=%v\n  want=%v", withBlock, want)
	}
}

// --- TomName.gom — NameNumber:make simplifies PositionName(concTomNumber([Position])) --

func TestHook_NameNumber_SimplifiesNestedPosition(t *testing.T) {
	pos := adt.MakePosition(7)
	wrapped := adt.MakePositionName(adt.MakeConcTomNumber(pos))
	got := adt.MakeNameNumber(wrapped)
	if got != pos {
		t.Fatalf("NameNumber:make didn't simplify to inner Position\n  got=%v\n  want=%v", got, pos)
	}
}

func TestHook_NameNumber_DoesNotSimplifyOtherShapes(t *testing.T) {
	multi := adt.MakePositionName(adt.MakeConcTomNumber(adt.MakePosition(1), adt.MakePosition(2)))
	got := adt.MakeNameNumber(multi)
	if _, ok := got.(*adt.NameNumberTomNumber); !ok {
		t.Fatalf("NameNumber with >1 inner positions should stay a NameNumber, got %T", got)
	}
}

// --- TomName.gom — concTomNumber:make_insert splices NameNumber-wrapped ---

func TestHook_ConcTomNumber_FlattensNestedNameNumber(t *testing.T) {
	inner := adt.MakeConcTomNumber(adt.MakePosition(1), adt.MakePosition(2))
	wrapped := adt.MakePositionName(inner)
	nn := adt.MakeNameNumber(wrapped) // does not simplify (≠1 inner)
	if _, ok := nn.(*adt.NameNumberTomNumber); !ok {
		t.Fatalf("expected wrapped value to be a NameNumber, got %T", nn)
	}
	out := adt.MakeConcTomNumber(nn, adt.MakePosition(3))
	want := adt.MakeConcTomNumber(adt.MakePosition(1), adt.MakePosition(2), adt.MakePosition(3))
	if out != want {
		t.Fatalf("concTomNumber make_insert did not splice nested NameNumber\n  got=%v\n  want=%v", out, want)
	}
}
