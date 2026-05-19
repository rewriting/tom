package gom

import (
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/internal/gomast"
)

// firstSort returns the first SortType production carried by m. It
// fails the test if the module has none.
func firstSort(t *testing.T, m gomast.GomModule) *gomast.SortTypeProduction {
	t.Helper()
	ss := Sorts(m)
	if len(ss) == 0 {
		t.Fatalf("module has no SortType production")
	}
	return ss[0]
}

// altsOf returns the alternatives of a SortType production.
func altsOf(prod *gomast.SortTypeProduction) []*gomast.AlternativeAlternative {
	altList := prod.AlternativeList.(*gomast.ConcAlternativeAlternativeList)
	out := make([]*gomast.AlternativeAlternative, 0, len(altList.Slots))
	for _, a := range altList.Slots {
		out = append(out, a.(*gomast.AlternativeAlternative))
	}
	return out
}

// fieldsOf returns the fields of an alternative.
func fieldsOf(alt *gomast.AlternativeAlternative) []gomast.Field {
	return alt.DomainList.(*gomast.ConcFieldFieldList).Slots
}

func TestParse_Minimal(t *testing.T) {
	src := `module Minimal
abstract syntax
Nop = EmptyNop()
    | UnaryNop(slot:Nop)
    | BinaryNop(ls:Nop,rs:Nop)
    | Vary(Nop*)
`
	mod, err := ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	if QualifiedName(mod) != "Minimal" {
		t.Fatalf("module name = %q", QualifiedName(mod))
	}
	if CountSorts(mod) != 1 || SortName(firstSort(t, mod)) != "Nop" {
		t.Fatalf("expected one sort Nop")
	}
	alts := altsOf(firstSort(t, mod))
	if len(alts) != 4 {
		t.Fatalf("expected 4 alternatives, got %d", len(alts))
	}
	if alts[0].Name != "EmptyNop" || len(fieldsOf(alts[0])) != 0 {
		t.Errorf("EmptyNop: name=%q fields=%d", alts[0].Name, len(fieldsOf(alts[0])))
	}
	if alts[2].Name != "BinaryNop" || len(fieldsOf(alts[2])) != 2 {
		t.Errorf("BinaryNop should have 2 slots: name=%q fields=%d", alts[2].Name, len(fieldsOf(alts[2])))
	}
	ls := fieldsOf(alts[2])[0].(*gomast.NamedFieldField)
	if ls.Name != "ls" || ls.FieldType.(*gomast.GomTypeGomType).Name != "Nop" {
		t.Errorf("BinaryNop arg[0] = name=%q type=%q", ls.Name, ls.FieldType.(*gomast.GomTypeGomType).Name)
	}
	// Vary(Nop*): the single field is StarredField, no Name, type Nop.
	if len(fieldsOf(alts[3])) != 1 {
		t.Fatalf("Vary should have 1 field, got %d", len(fieldsOf(alts[3])))
	}
	sf, ok := fieldsOf(alts[3])[0].(*gomast.StarredFieldField)
	if !ok {
		t.Fatalf("Vary's field should be StarredField, got %T", fieldsOf(alts[3])[0])
	}
	if sf.FieldType.(*gomast.GomTypeGomType).Name != "Nop" {
		t.Errorf("Vary star type = %q", sf.FieldType.(*gomast.GomTypeGomType).Name)
	}
}

func TestParse_DottedModuleAndImports(t *testing.T) {
	src := `module gom.b.u.i.l.t.i.n.Builtin
imports int boolean String
abstract syntax
W = Int(i:int)
`
	mod, err := ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	want := []string{"gom", "b", "u", "i", "l", "t", "i", "n", "Builtin"}
	if QualifiedName(mod) != "gom.b.u.i.l.t.i.n.Builtin" {
		t.Errorf("qualified name = %q", QualifiedName(mod))
	}
	parts := NameParts(mod)
	for i, w := range want {
		if parts[i] != w {
			t.Errorf("name[%d] = %q want %q", i, parts[i], w)
		}
	}
	imps := Imports(mod)
	if len(imps) != 3 || imps[0] != "int" || imps[2] != "String" {
		t.Errorf("imports = %v", imps)
	}
}

func TestParse_LeadingPipeBeforeFirstAlternative(t *testing.T) {
	src := `module test.W
abstract syntax
Wrapper = | Int(i:int)
          | Name(n:String)
`
	mod, err := ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	if got := len(altsOf(firstSort(t, mod))); got != 2 {
		t.Fatalf("want 2 alternatives, got %d", got)
	}
}

func TestParse_MultipleSorts(t *testing.T) {
	src := `module gom.List
imports int
abstract syntax
List = conc(int*)
Pair = pair(l:List,r:List)
`
	mod, err := ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	if CountSorts(mod) != 2 {
		t.Fatalf("want 2 sorts, got %d", CountSorts(mod))
	}
	listSort := Sorts(mod)[0]
	concAlt := altsOf(listSort)[0]
	if _, ok := fieldsOf(concAlt)[0].(*gomast.StarredFieldField); !ok {
		t.Errorf("conc(int*) should be variadic, got %T", fieldsOf(concAlt)[0])
	}
}

func TestParse_AcceptsSimpleHook(t *testing.T) {
	src := `module gom.X
abstract syntax
Foo = A() | B()
Foo:make() {
  return null;
}
`
	mod, err := ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	hooks := Hooks(mod)
	if len(hooks) != 1 {
		t.Fatalf("expected 1 hook, got %d", len(hooks))
	}
	h := hooks[0]
	if h.Name != "Foo" || HookKind(h) != "make" {
		t.Errorf("unexpected hook header: name=%q kind=%q", h.Name, HookKind(h))
	}
	// Default scope on unscoped hooks is "operator" per the ANTLR grammar.
	if HookScope(h) != "operator" {
		t.Errorf("expected operator scope, got %q", HookScope(h))
	}
	if !strings.Contains(HookBody(h), "return null") {
		t.Errorf("body did not capture content: %q", HookBody(h))
	}
}

func TestParse_AcceptsScopedHook(t *testing.T) {
	src := `module gom.X
abstract syntax
Foo = A() | B()
sort Foo:block() {
  public int answer() { return 42; }
}
Bar = C()
`
	mod, err := ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	hooks := Hooks(mod)
	if len(hooks) != 1 {
		t.Fatalf("expected 1 hook, got %d", len(hooks))
	}
	h := hooks[0]
	if HookScope(h) != "sort" || h.Name != "Foo" || HookKind(h) != "block" {
		t.Errorf("unexpected hook: scope=%q name=%q kind=%q", HookScope(h), h.Name, HookKind(h))
	}
	// Parsing must continue past the hook: Bar = C() should be picked
	// up as a regular sort declaration.
	sorts := Sorts(mod)
	if len(sorts) != 2 || SortName(sorts[1]) != "Bar" {
		t.Errorf("post-hook sort decl missed")
	}
	// Body must contain the nested braces from `return 42; }`.
	if !strings.Contains(HookBody(h), "return 42") {
		t.Errorf("body lost nested content: %q", HookBody(h))
	}
}

func TestParse_HookWithArgs(t *testing.T) {
	src := `module gom.X
abstract syntax
Foo = A()
Foo:make(x,y) {}
`
	mod, err := ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	h := Hooks(mod)[0]
	argList := h.Args.(*gomast.ConcArgArgList)
	if len(argList.Slots) != 2 {
		t.Fatalf("want 2 args, got %d", len(argList.Slots))
	}
	if argList.Slots[0].(*gomast.ArgArg).Name != "x" || argList.Slots[1].(*gomast.ArgArg).Name != "y" {
		t.Errorf("args = %v", argList.Slots)
	}
}

func TestParse_ObjectsGomReal(t *testing.T) {
	// The real Objects.gom from the repo carries one sort-scoped hook
	// and a non-trivial body containing %match braces.
	mod, err := ParseFile("../../../src/tom/gom/adt/Objects.gom")
	if err != nil {
		t.Fatal(err)
	}
	hooks := Hooks(mod)
	if len(hooks) != 1 {
		t.Fatalf("expected 1 hook, got %d", len(hooks))
	}
	h := hooks[0]
	if HookScope(h) != "sort" || h.Name != "HookList" || HookKind(h) != "block" {
		t.Errorf("Objects hook header: scope=%q name=%q kind=%q", HookScope(h), h.Name, HookKind(h))
	}
	if !strings.Contains(HookBody(h), "containsTomCode") || !strings.Contains(HookBody(h), "HasTomCode") {
		t.Errorf("body did not capture expected tokens: %q", HookBody(h))
	}
}

func TestParse_Corpus(t *testing.T) {
	// Each file in the corpus must parse cleanly to a gomast.GomModule
	// carrying at least one SortType production.
	for _, name := range []string{
		"Builtin.gom",
		"Dotted.gom",
		"Imported.gom",
		"Importing.gom",
		"Leaf.gom",
		"List.gom",
		"Minimal.gom",
		"Yang.gom",
		"Ying.gom",
		"fromterm/foo.gom",
	} {
		path := filepath.Join("..", "..", "testdata", "corpus", "gom-nohooks", name)
		t.Run(name, func(t *testing.T) {
			mod, err := ParseFile(path)
			if err != nil {
				t.Fatal(err)
			}
			if CountSorts(mod) == 0 {
				t.Fatal("no sorts parsed")
			}
		})
	}
}

// TestParse_SharingAcrossFiles asserts that two equal GomType
// sub-terms built by parsing two different inputs end up sharing the
// same canonical instance — this is the auto-bootstrap pay-off: the
// parser itself benefits from hash-consing.
func TestParse_SharingAcrossFiles(t *testing.T) {
	srcA := `module A
abstract syntax
Foo = X()
`
	srcB := `module B
abstract syntax
Foo = X()
`
	a, err := ParseBytes([]byte(srcA))
	if err != nil {
		t.Fatal(err)
	}
	b, err := ParseBytes([]byte(srcB))
	if err != nil {
		t.Fatal(err)
	}
	fooTypeA := Sorts(a)[0].Type
	fooTypeB := Sorts(b)[0].Type
	if fooTypeA != fooTypeB {
		t.Fatal("GomType('Foo') built from two different modules should be the same shared instance")
	}
}
