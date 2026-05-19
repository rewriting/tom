package gom

import (
	"path/filepath"
	"strings"
	"testing"
)

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
	if mod.QualifiedName() != "Minimal" {
		t.Fatalf("module name = %q", mod.QualifiedName())
	}
	if len(mod.Sorts) != 1 || mod.Sorts[0].Name != "Nop" {
		t.Fatalf("expected one sort Nop, got %+v", mod.Sorts)
	}
	alts := mod.Sorts[0].Alternatives
	if len(alts) != 4 {
		t.Fatalf("expected 4 alternatives, got %d", len(alts))
	}
	if alts[0].Op != "EmptyNop" || len(alts[0].Args) != 0 {
		t.Errorf("EmptyNop: %+v", alts[0])
	}
	if alts[2].Op != "BinaryNop" || len(alts[2].Args) != 2 {
		t.Errorf("BinaryNop should have 2 slots: %+v", alts[2])
	}
	if alts[2].Args[0] != (Arg{Name: "ls", Type: "Nop"}) {
		t.Errorf("BinaryNop arg[0] = %+v", alts[2].Args[0])
	}
	if !alts[3].Variadic || len(alts[3].Args) != 1 || alts[3].Args[0].Type != "Nop" {
		t.Errorf("Vary should be variadic with one Nop arg: %+v", alts[3])
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
	if mod.QualifiedName() != "gom.b.u.i.l.t.i.n.Builtin" {
		t.Errorf("qualified name = %q", mod.QualifiedName())
	}
	for i, w := range want {
		if mod.Name[i] != w {
			t.Errorf("name[%d] = %q want %q", i, mod.Name[i], w)
		}
	}
	if len(mod.Imports) != 3 || mod.Imports[0] != "int" || mod.Imports[2] != "String" {
		t.Errorf("imports = %v", mod.Imports)
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
	if got := len(mod.Sorts[0].Alternatives); got != 2 {
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
	if len(mod.Sorts) != 2 {
		t.Fatalf("want 2 sorts, got %d", len(mod.Sorts))
	}
	if !mod.Sorts[0].Alternatives[0].Variadic {
		t.Errorf("conc(int*) should be variadic")
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
	if len(mod.Hooks) != 1 {
		t.Fatalf("expected 1 hook, got %d", len(mod.Hooks))
	}
	h := mod.Hooks[0]
	if h.PointCut != "Foo" || h.Kind != "make" {
		t.Errorf("unexpected hook header: %+v", h)
	}
	if h.Scope != "" {
		t.Errorf("expected empty scope, got %q", h.Scope)
	}
	if !strings.Contains(h.Body, "return null") {
		t.Errorf("body did not capture content: %q", h.Body)
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
	if len(mod.Hooks) != 1 {
		t.Fatalf("expected 1 hook, got %d", len(mod.Hooks))
	}
	h := mod.Hooks[0]
	if h.Scope != "sort" || h.PointCut != "Foo" || h.Kind != "block" {
		t.Errorf("unexpected hook: %+v", h)
	}
	// Parsing must continue past the hook: Bar = C() should be picked
	// up as a regular sort declaration.
	if len(mod.Sorts) != 2 || mod.Sorts[1].Name != "Bar" {
		t.Errorf("post-hook sort decl missed: %+v", mod.Sorts)
	}
	// Body must contain the nested braces from `return 42; }`.
	if !strings.Contains(h.Body, "return 42") {
		t.Errorf("body lost nested content: %q", h.Body)
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
	if len(mod.Hooks[0].Args) != 2 || mod.Hooks[0].Args[0] != "x" || mod.Hooks[0].Args[1] != "y" {
		t.Errorf("args = %+v", mod.Hooks[0].Args)
	}
}

func TestParse_ObjectsGomReal(t *testing.T) {
	// The real Objects.gom from the repo carries one sort-scoped hook
	// and a non-trivial body containing %match braces.
	mod, err := ParseFile("../../../src/tom/gom/adt/Objects.gom")
	if err != nil {
		t.Fatal(err)
	}
	if len(mod.Hooks) != 1 {
		t.Fatalf("expected 1 hook, got %d", len(mod.Hooks))
	}
	h := mod.Hooks[0]
	if h.Scope != "sort" || h.PointCut != "HookList" || h.Kind != "block" {
		t.Errorf("Objects hook header = %+v", h)
	}
	if !strings.Contains(h.Body, "containsTomCode") || !strings.Contains(h.Body, "HasTomCode") {
		t.Errorf("body did not capture expected tokens: %q", h.Body)
	}
}

func TestParse_Corpus(t *testing.T) {
	// Each file in the corpus must parse cleanly.
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
			if len(mod.Sorts) == 0 {
				t.Fatal("no sorts parsed")
			}
		})
	}
}
