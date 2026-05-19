package gomast

import (
	"fmt"
	"testing"
)

// TestSmoke_Construction exercises the simplest path through the
// generated package: build a GomModule, check sharing, walk a sort.
// It also acts as a regression check that the regeneration of this
// package produced compilable, hash-consing code.
func TestSmoke_Construction(t *testing.T) {
	name := MakeGomModuleName("Foo")
	if MakeGomModuleName("Foo") != name {
		t.Fatal("GomModuleName('Foo') should be shared")
	}

	// A trivial module with no sections at all.
	empty := MakeConcSection()
	mod := MakeGomModule(name, empty)
	if MakeGomModule(name, empty) != mod {
		t.Fatal("structurally equal GomModule should share")
	}

	// Build a tiny SortType production: `Nat = zero() | suc(pred:Nat)`.
	gomType := MakeGomType(MakeExpressionType(), "Nat")
	emptyFields := MakeConcField()
	emptyOption := MakeOptionList()
	zero := MakeAlternative("zero", emptyFields, gomType, emptyOption)

	predField := MakeNamedField("pred", gomType, MakeNone())
	predFields := MakeConcField(predField)
	suc := MakeAlternative("suc", predFields, gomType, emptyOption)

	alts := MakeConcAlternative(zero, suc)
	sort := MakeSortType(gomType, MakeConcAtom(), alts)
	prods := MakeConcProduction(sort)
	public := MakePublic(prods)
	sections := MakeConcSection(public)
	natModule := MakeGomModule(MakeGomModuleName("Nat"), sections)

	// Sharing across the whole tree.
	gomType2 := MakeGomType(MakeExpressionType(), "Nat")
	zero2 := MakeAlternative("zero", emptyFields, gomType2, emptyOption)
	if zero != zero2 {
		t.Fatal("two zero alternatives should share")
	}
	if gomType != gomType2 {
		t.Fatal("two GomType('Nat') should share")
	}

	// Smoke-check the printed form mentions the sort name and both
	// constructor names. The marker interface does not declare
	// String() — we go through fmt.Sprint, which dispatches to the
	// concrete struct's method.
	s := fmt.Sprint(natModule)
	for _, kw := range []string{"GomModule", "GomModuleName", "Nat", "zero", "suc", "pred"} {
		if !contains(s, kw) {
			t.Errorf("expected %q in stringified module:\n%s", kw, s)
		}
	}

	// The HookList:block-derived ContainsTomCode should always be
	// false on a module without hooks.
	emptyHookList := MakeConcHook()
	if emptyHookList.ContainsTomCode() {
		t.Fatal("empty HookList must not report ContainsTomCode")
	}
}

func contains(s, sub string) bool {
	for i := 0; i+len(sub) <= len(s); i++ {
		if s[i:i+len(sub)] == sub {
			return true
		}
	}
	return false
}
