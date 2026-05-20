package backend_test

import (
	"testing"

	"tom/tomgo/internal/tomast"
)

// The 9 hook prologues in src/tom/engine/adt/*.gom must change the
// canonical form of certain TOM terms at construction time. This file
// pins down their observable effects so any drift in the backend
// (or in the underlying .gom sources) is caught immediately.
//
// The tests live under internal/backend/ rather than under
// internal/tomast/ so they survive a full regeneration of the tomast
// package (`rm -rf internal/tomast/*.go` keeps this file intact).

// --- TomConstraint.gom — AndConstraint:AU() { TrueConstraint() } ----------

func TestHook_AndConstraint_EmptyReturnsTrueConstraint(t *testing.T) {
	got := tomast.MakeAndConstraint()
	want := tomast.MakeTrueConstraint()
	if got != want {
		t.Fatalf("MakeAndConstraint() should return TrueConstraint(), got %T %v", got, got)
	}
}

func TestHook_AndConstraint_AssociativityFlattens(t *testing.T) {
	// Use FalseConstraint as the test payload — TrueConstraint is the unit
	// of AndConstraint and would be absorbed (cf. unit-absorption test below).
	b := tomast.MakeFalseConstraint()
	inner := tomast.MakeAndConstraint(b, b)
	outer := tomast.MakeAndConstraint(inner, b)
	// AndConstraint(AndConstraint(b,b), b) flattens to AndConstraint(b, b, b).
	flat := tomast.MakeAndConstraint(b, b, b)
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
	u := tomast.MakeTrueConstraint()
	b := tomast.MakeFalseConstraint()
	if tomast.MakeAndConstraint(b, u) != b {
		t.Fatalf("AndConstraint(b, unit) must collapse to b")
	}
	if tomast.MakeAndConstraint(u, b) != b {
		t.Fatalf("AndConstraint(unit, b) must collapse to b")
	}
	if tomast.MakeAndConstraint(u, u, u) != u {
		t.Fatalf("AndConstraint(unit, unit, unit) must collapse to the unit")
	}
	// 2-element AC with a unit dropped → 1-element → bare element.
	if tomast.MakeAndConstraint(u, b, u) != b {
		t.Fatalf("AndConstraint(unit, b, unit) must collapse to b")
	}
	// 3-element AC, two distinct non-units → preserved.
	threeNonUnit := tomast.MakeAndConstraint(b, b, b)
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
	u := tomast.MakeFalseConstraint()
	b := tomast.MakeTrueConstraint()
	if tomast.MakeOrConstraint(b, u) != b {
		t.Fatalf("OrConstraint(b, unit) must collapse to b")
	}
	if tomast.MakeOrConstraint(u, b) != b {
		t.Fatalf("OrConstraint(unit, b) must collapse to b")
	}
}

// --- TomConstraint.gom — OrConstraint:AU() { FalseConstraint() } ----------

func TestHook_OrConstraint_EmptyReturnsFalseConstraint(t *testing.T) {
	got := tomast.MakeOrConstraint()
	want := tomast.MakeFalseConstraint()
	if got != want {
		t.Fatalf("MakeOrConstraint() should return FalseConstraint(), got %T %v", got, got)
	}
}

// --- TomConstraint.gom — OrConstraintDisjunction:AU() { } (no unit) ------

func TestHook_OrConstraintDisjunction_NoUnit_FlattensOnly(t *testing.T) {
	a := tomast.MakeTrueConstraint()
	b := tomast.MakeFalseConstraint()
	empty := tomast.MakeOrConstraintDisjunction()
	if empty == tomast.MakeTrueConstraint() || empty == tomast.MakeFalseConstraint() {
		t.Fatalf("OrConstraintDisjunction() must not collapse to True/False — no unit declared")
	}
	inner := tomast.MakeOrConstraintDisjunction(a, b)
	outer := tomast.MakeOrConstraintDisjunction(inner, a)
	flat := tomast.MakeOrConstraintDisjunction(a, b, a)
	if outer != flat {
		t.Fatalf("OrConstraintDisjunction must flatten nested instances")
	}
}

// --- Code.gom — module:rules() InstructionToCode ↔ CodeToInstruction -----

func TestHook_CodeRules_InstructionToCode_CancelsInverse(t *testing.T) {
	tl := tomast.MakeITL("body")
	codeT := tomast.MakeTargetLanguageToCode(tl)
	wrap := tomast.MakeCodeToInstruction(codeT)
	got := tomast.MakeInstructionToCode(wrap)
	if got != codeT {
		t.Fatalf("InstructionToCode∘CodeToInstruction didn't cancel\n  got=%v\n  want=%v", got, codeT)
	}
}

func TestHook_CodeRules_CodeToInstruction_CancelsInverse(t *testing.T) {
	tl := tomast.MakeITL("body")
	codeT := tomast.MakeTargetLanguageToCode(tl)
	inst := tomast.MakeCodeToInstruction(codeT)
	got := tomast.MakeCodeToInstruction(tomast.MakeInstructionToCode(inst))
	if got != inst {
		t.Fatalf("CodeToInstruction∘InstructionToCode didn't cancel\n  got=%v\n  want=%v", got, inst)
	}
}

// --- TomExpression.gom — BQTermToExpression ↔ ExpressionToBQTerm ---------

func TestHook_ExpressionRules_BQTermToExpression_CancelsInverse(t *testing.T) {
	bq := tomast.MakeBQDefault()
	expr := tomast.MakeBQTermToExpression(bq)
	got := tomast.MakeExpressionToBQTerm(expr)
	if got != bq {
		t.Fatalf("ExpressionToBQTerm∘BQTermToExpression didn't cancel\n  got=%v\n  want=%v", got, bq)
	}
}

func TestHook_ExpressionRules_ExpressionToBQTerm_CancelsInverse(t *testing.T) {
	bq := tomast.MakeBQDefault()
	expr := tomast.MakeBQTermToExpression(bq)
	roundtrip := tomast.MakeExpressionToBQTerm(expr) // = bq
	got := tomast.MakeBQTermToExpression(roundtrip)
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
	badType := tomast.MakeType(tomast.MakeConcTypeOption(), "unknown type", tomast.MakeEmptyTargetLanguageType())
	_ = tomast.MakeCast(badType, tomast.MakeBQTermToExpression(tomast.MakeBQDefault()))
}

func TestHook_Cast_AcceptsKnownType(t *testing.T) {
	okType := tomast.MakeType(tomast.MakeConcTypeOption(), "int", tomast.MakeEmptyTargetLanguageType())
	got := tomast.MakeCast(okType, tomast.MakeBQTermToExpression(tomast.MakeBQDefault()))
	if _, ok := got.(*tomast.CastExpression); !ok {
		t.Fatalf("MakeCast with known type should return *CastExpression, got %T", got)
	}
}

// --- TomInstruction.gom — concInstruction:make_insert splices AbstractBlock --

func TestHook_ConcInstruction_FlattensAbstractBlock(t *testing.T) {
	a := tomast.MakeNop()
	b := tomast.MakeNop()
	c := tomast.MakeNop()
	inner := tomast.MakeConcInstruction(a, b)
	block := tomast.MakeAbstractBlock(inner)
	withBlock := tomast.MakeConcInstruction(block, c)
	want := tomast.MakeConcInstruction(a, b, c)
	if withBlock != want {
		t.Fatalf("concInstruction make_insert did not splice AbstractBlock\n  got=%v\n  want=%v", withBlock, want)
	}
}

// --- TomName.gom — NameNumber:make simplifies PositionName(concTomNumber([Position])) --

func TestHook_NameNumber_SimplifiesNestedPosition(t *testing.T) {
	pos := tomast.MakePosition(7)
	wrapped := tomast.MakePositionName(tomast.MakeConcTomNumber(pos))
	got := tomast.MakeNameNumber(wrapped)
	if got != pos {
		t.Fatalf("NameNumber:make didn't simplify to inner Position\n  got=%v\n  want=%v", got, pos)
	}
}

func TestHook_NameNumber_DoesNotSimplifyOtherShapes(t *testing.T) {
	multi := tomast.MakePositionName(tomast.MakeConcTomNumber(tomast.MakePosition(1), tomast.MakePosition(2)))
	got := tomast.MakeNameNumber(multi)
	if _, ok := got.(*tomast.NameNumberTomNumber); !ok {
		t.Fatalf("NameNumber with >1 inner positions should stay a NameNumber, got %T", got)
	}
}

// --- TomName.gom — concTomNumber:make_insert splices NameNumber-wrapped ---

func TestHook_ConcTomNumber_FlattensNestedNameNumber(t *testing.T) {
	inner := tomast.MakeConcTomNumber(tomast.MakePosition(1), tomast.MakePosition(2))
	wrapped := tomast.MakePositionName(inner)
	nn := tomast.MakeNameNumber(wrapped) // does not simplify (≠1 inner)
	if _, ok := nn.(*tomast.NameNumberTomNumber); !ok {
		t.Fatalf("expected wrapped value to be a NameNumber, got %T", nn)
	}
	out := tomast.MakeConcTomNumber(nn, tomast.MakePosition(3))
	want := tomast.MakeConcTomNumber(tomast.MakePosition(1), tomast.MakePosition(2), tomast.MakePosition(3))
	if out != want {
		t.Fatalf("concTomNumber make_insert did not splice nested NameNumber\n  got=%v\n  want=%v", out, want)
	}
}
