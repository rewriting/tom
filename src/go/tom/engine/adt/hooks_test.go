package adt

import (
	"testing"
)

// The hooks of the .gom files of this directory (AU theories, rules,
// make and make_insert hooks) must change the canonical form of certain
// Tom terms at construction time. This file pins down their observable
// effects so any drift in the backend (or in the .gom sources, or in
// hooks.go, which installs the Tom+Go hooks) is caught immediately.

// --- TomConstraint.gom — AndConstraint:AU() { TrueConstraint() } ----------

func TestHook_AndConstraint_EmptyReturnsTrueConstraint(t *testing.T) {
	got := MakeAndConstraint()
	want := MakeTrueConstraint()
	if got != want {
		t.Fatalf("MakeAndConstraint() should return TrueConstraint(), got %T %v", got, got)
	}
}

func TestHook_AndConstraint_AssociativityFlattens(t *testing.T) {
	// Use FalseConstraint as the test payload — TrueConstraint is the unit
	// of AndConstraint and would be absorbed (cf. unit-absorption test below).
	b := MakeFalseConstraint()
	inner := MakeAndConstraint(b, b)
	outer := MakeAndConstraint(inner, b)
	// AndConstraint(AndConstraint(b,b), b) flattens to AndConstraint(b, b, b).
	flat := MakeAndConstraint(b, b, b)
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
	u := MakeTrueConstraint()
	b := MakeFalseConstraint()
	if MakeAndConstraint(b, u) != b {
		t.Fatalf("AndConstraint(b, unit) must collapse to b")
	}
	if MakeAndConstraint(u, b) != b {
		t.Fatalf("AndConstraint(unit, b) must collapse to b")
	}
	if MakeAndConstraint(u, u, u) != u {
		t.Fatalf("AndConstraint(unit, unit, unit) must collapse to the unit")
	}
	// 2-element AC with a unit dropped → 1-element → bare element.
	if MakeAndConstraint(u, b, u) != b {
		t.Fatalf("AndConstraint(unit, b, unit) must collapse to b")
	}
	// 3-element AC, two distinct non-units → preserved.
	threeNonUnit := MakeAndConstraint(b, b, b)
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
	u := MakeFalseConstraint()
	b := MakeTrueConstraint()
	if MakeOrConstraint(b, u) != b {
		t.Fatalf("OrConstraint(b, unit) must collapse to b")
	}
	if MakeOrConstraint(u, b) != b {
		t.Fatalf("OrConstraint(unit, b) must collapse to b")
	}
}

// --- TomConstraint.gom — OrConstraint:AU() { FalseConstraint() } ----------

func TestHook_OrConstraint_EmptyReturnsFalseConstraint(t *testing.T) {
	got := MakeOrConstraint()
	want := MakeFalseConstraint()
	if got != want {
		t.Fatalf("MakeOrConstraint() should return FalseConstraint(), got %T %v", got, got)
	}
}

// --- TomConstraint.gom — OrConstraintDisjunction:AU() { } (no unit) ------

func TestHook_OrConstraintDisjunction_NoUnit_FlattensOnly(t *testing.T) {
	a := MakeTrueConstraint()
	b := MakeFalseConstraint()
	empty := MakeOrConstraintDisjunction()
	if empty == MakeTrueConstraint() || empty == MakeFalseConstraint() {
		t.Fatalf("OrConstraintDisjunction() must not collapse to True/False — no unit declared")
	}
	inner := MakeOrConstraintDisjunction(a, b)
	outer := MakeOrConstraintDisjunction(inner, a)
	flat := MakeOrConstraintDisjunction(a, b, a)
	if outer != flat {
		t.Fatalf("OrConstraintDisjunction must flatten nested instances")
	}
}

// --- Code.gom — module:rules() InstructionToCode ↔ CodeToInstruction -----

func TestHook_CodeRules_InstructionToCode_CancelsInverse(t *testing.T) {
	tl := MakeITL("body")
	codeT := MakeTargetLanguageToCode(tl)
	wrap := MakeCodeToInstruction(codeT)
	got := MakeInstructionToCode(wrap)
	if got != codeT {
		t.Fatalf("InstructionToCode∘CodeToInstruction didn't cancel\n  got=%v\n  want=%v", got, codeT)
	}
}

func TestHook_CodeRules_CodeToInstruction_CancelsInverse(t *testing.T) {
	tl := MakeITL("body")
	codeT := MakeTargetLanguageToCode(tl)
	inst := MakeCodeToInstruction(codeT)
	got := MakeCodeToInstruction(MakeInstructionToCode(inst))
	if got != inst {
		t.Fatalf("CodeToInstruction∘InstructionToCode didn't cancel\n  got=%v\n  want=%v", got, inst)
	}
}

// --- TomExpression.gom — BQTermToExpression ↔ ExpressionToBQTerm ---------

func TestHook_ExpressionRules_BQTermToExpression_CancelsInverse(t *testing.T) {
	bq := MakeBQDefault()
	expr := MakeBQTermToExpression(bq)
	got := MakeExpressionToBQTerm(expr)
	if got != bq {
		t.Fatalf("ExpressionToBQTerm∘BQTermToExpression didn't cancel\n  got=%v\n  want=%v", got, bq)
	}
}

func TestHook_ExpressionRules_ExpressionToBQTerm_CancelsInverse(t *testing.T) {
	bq := MakeBQDefault()
	expr := MakeBQTermToExpression(bq)
	roundtrip := MakeExpressionToBQTerm(expr) // = bq
	got := MakeBQTermToExpression(roundtrip)
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
	badType := MakeType(MakeConcTypeOption(), "unknown type", MakeEmptyTargetLanguageType())
	_ = MakeCast(badType, MakeBQTermToExpression(MakeBQDefault()))
}

func TestHook_Cast_AcceptsKnownType(t *testing.T) {
	okType := MakeType(MakeConcTypeOption(), "int", MakeEmptyTargetLanguageType())
	got := MakeCast(okType, MakeBQTermToExpression(MakeBQDefault()))
	if _, ok := got.(*CastExpression); !ok {
		t.Fatalf("MakeCast with known type should return *CastExpression, got %T", got)
	}
}

// --- TomInstruction.gom — concInstruction:make_insert splices AbstractBlock --

func TestHook_ConcInstruction_FlattensAbstractBlock(t *testing.T) {
	a := MakeNop()
	b := MakeNop()
	c := MakeNop()
	inner := MakeConcInstruction(a, b)
	block := MakeAbstractBlock(inner)
	withBlock := MakeConcInstruction(block, c)
	want := MakeConcInstruction(a, b, c)
	if withBlock != want {
		t.Fatalf("concInstruction make_insert did not splice AbstractBlock\n  got=%v\n  want=%v", withBlock, want)
	}
}

// --- TomName.gom — NameNumber:make simplifies PositionName(concTomNumber([Position])) --

func TestHook_NameNumber_SimplifiesNestedPosition(t *testing.T) {
	pos := MakePosition(7)
	wrapped := MakePositionName(MakeConcTomNumber(pos))
	got := MakeNameNumber(wrapped)
	if got != pos {
		t.Fatalf("NameNumber:make didn't simplify to inner Position\n  got=%v\n  want=%v", got, pos)
	}
}

func TestHook_NameNumber_DoesNotSimplifyOtherShapes(t *testing.T) {
	multi := MakePositionName(MakeConcTomNumber(MakePosition(1), MakePosition(2)))
	got := MakeNameNumber(multi)
	if _, ok := got.(*NameNumberTomNumber); !ok {
		t.Fatalf("NameNumber with >1 inner positions should stay a NameNumber, got %T", got)
	}
}

// --- TomName.gom — concTomNumber:make_insert splices NameNumber-wrapped ---

func TestHook_ConcTomNumber_FlattensNestedNameNumber(t *testing.T) {
	inner := MakeConcTomNumber(MakePosition(1), MakePosition(2))
	wrapped := MakePositionName(inner)
	nn := MakeNameNumber(wrapped) // does not simplify (≠1 inner)
	if _, ok := nn.(*NameNumberTomNumber); !ok {
		t.Fatalf("expected wrapped value to be a NameNumber, got %T", nn)
	}
	out := MakeConcTomNumber(nn, MakePosition(3))
	want := MakeConcTomNumber(MakePosition(1), MakePosition(2), MakePosition(3))
	if out != want {
		t.Fatalf("concTomNumber make_insert did not splice nested NameNumber\n  got=%v\n  want=%v", out, want)
	}
}
