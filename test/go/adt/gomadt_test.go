package adt_test

import (
	"fmt"
	"testing"

	gomast "tom/gom/adt"
)

// TestSmoke_Construction exercises the simplest path through the
// generated package tom/gom/adt: build a GomModule, check sharing, walk a sort.
// It also acts as a regression check that the regeneration of this
// package produced compilable, hash-consing code.
func TestSmoke_Construction(t *testing.T) {
	name := gomast.MakeGomModuleName("Foo")
	if gomast.MakeGomModuleName("Foo") != name {
		t.Fatal("GomModuleName('Foo') should be shared")
	}

	// A trivial module with no sections at all.
	empty := gomast.MakeConcSection()
	mod := gomast.MakeGomModule(name, empty)
	if gomast.MakeGomModule(name, empty) != mod {
		t.Fatal("structurally equal GomModule should share")
	}

	// Build a tiny SortType production: `Nat = zero() | suc(pred:Nat)`.
	gomType := gomast.MakeGomType(gomast.MakeExpressionType(), "Nat")
	emptyFields := gomast.MakeConcField()
	emptyOption := gomast.MakeOptionList()
	zero := gomast.MakeAlternative("zero", emptyFields, gomType, emptyOption)

	predField := gomast.MakeNamedField("pred", gomType, gomast.MakeNone())
	predFields := gomast.MakeConcField(predField)
	suc := gomast.MakeAlternative("suc", predFields, gomType, emptyOption)

	alts := gomast.MakeConcAlternative(zero, suc)
	sort := gomast.MakeSortType(gomType, gomast.MakeConcAtom(), alts)
	prods := gomast.MakeConcProduction(sort)
	public := gomast.MakePublic(prods)
	sections := gomast.MakeConcSection(public)
	natModule := gomast.MakeGomModule(gomast.MakeGomModuleName("Nat"), sections)

	// Sharing across the whole tree.
	gomType2 := gomast.MakeGomType(gomast.MakeExpressionType(), "Nat")
	zero2 := gomast.MakeAlternative("zero", emptyFields, gomType2, emptyOption)
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

	// ContainsTomCode (the HookList:block hook of Objects.gom) is
	// false on an empty hook list.
	emptyHookList := gomast.MakeConcHook()
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

// ContainsTomCode is the block hook (in Go) of HookList (Objects.gom):
// Java's containsTomCode() of the HookList sort.
func TestContainsTomCode_NoTomCode(t *testing.T) {
	codeProg := gomast.MakeCode("// trivial Go body")
	list := gomast.MakeConcHook(gomast.MakeBlockHook(codeProg, false), gomast.MakeInterfaceHook(codeProg))
	if list.ContainsTomCode() {
		t.Fatal("expected ContainsTomCode() = false, got true")
	}
}

func TestContainsTomCode_FromBlockHook(t *testing.T) {
	list := gomast.MakeConcHook(gomast.MakeBlockHook(gomast.MakeCode("// pretend this contains %match …"), true))
	if !list.ContainsTomCode() {
		t.Fatal("expected ContainsTomCode() = true on BlockHook with HasTomCode=true")
	}
}

func TestContainsTomCode_FromMakeHook(t *testing.T) {
	list := gomast.MakeConcHook(gomast.MakeMakeHook(gomast.MakeConcSlotField(), gomast.MakeCode("// body"), true))
	if !list.ContainsTomCode() {
		t.Fatal("expected ContainsTomCode() = true on MakeHook")
	}
}

func TestContainsTomCode_FromMakeBeforeHook(t *testing.T) {
	code := gomast.MakeCode("// body")
	list := gomast.MakeConcHook(gomast.MakeInterfaceHook(code), gomast.MakeMakeBeforeHook(gomast.MakeConcSlotField(), code, true))
	if !list.ContainsTomCode() {
		t.Fatal("expected ContainsTomCode() = true on MakeBeforeHook (second element)")
	}
}

func TestContainsTomCode_NonHasTomCodeKind(t *testing.T) {
	// InterfaceHook / ImportHook / MappingHook have no HasTomCode
	// field, so they must never contribute to ContainsTomCode.
	code := gomast.MakeCode("hi")
	list := gomast.MakeConcHook(gomast.MakeInterfaceHook(code), gomast.MakeImportHook(code), gomast.MakeMappingHook(code))
	if list.ContainsTomCode() {
		t.Fatal("hooks without HasTomCode must not be reported")
	}
}
