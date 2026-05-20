package gom

import (
	"os"
	"path/filepath"
	"strings"
	"testing"
)

func TestHasHookContent_NoHook(t *testing.T) {
	src := `module Minimal
abstract syntax
Nop = EmptyNop()
    | UnaryNop(slot:Nop)
    | BinaryNop(ls:Nop,rs:Nop)
    | Vary(Nop*)
`
	got, err := HasHookContent(strings.NewReader(src))
	if err != nil {
		t.Fatal(err)
	}
	if got {
		t.Fatal("expected no hook, got one")
	}
}

func TestHasHookContent_PlainMakeHook(t *testing.T) {
	src := `module gom.Bool
abstract syntax
Bool = True() | False()
Not:make(b) {
  return b;
}
`
	got, err := HasHookContent(strings.NewReader(src))
	if err != nil {
		t.Fatal(err)
	}
	if !got {
		t.Fatal("expected hook, got none")
	}
}

func TestHasHookContent_ScopedHooks(t *testing.T) {
	// Per the Gom grammar, after the optional scope keyword (sort|module|
	// operator), pointCut is a simple ID. A dotted name like `gom.X` is the
	// MODULE declaration at the top of a file, not a hook scope.
	cases := []string{
		"sort Bool:tom() {\n}\n",
		"module Bar:do() {\n}\n",
		"operator Op:make(a,b) {\n}\n",
	}
	for _, src := range cases {
		got, err := HasHookContent(strings.NewReader(src))
		if err != nil {
			t.Fatal(err)
		}
		if !got {
			t.Errorf("expected hook for %q", src)
		}
	}
}

func TestHasHookContent_ModuleDeclarationIsNotAHook(t *testing.T) {
	src := "module gom.Bool\nabstract syntax\nBool = T() | F()\n"
	got, err := HasHookContent(strings.NewReader(src))
	if err != nil {
		t.Fatal(err)
	}
	if got {
		t.Fatal("module declaration `module gom.Bool` must not be flagged as hook")
	}
}

func TestHasHookContent_IgnoresSlotFieldsInAlternatives(t *testing.T) {
	// b:Bool is a slot field inside an alternative, not a hook.
	src := `module gom.Foo
abstract syntax
Foo = Bar(b:Bool, l:Foo)
    | Baz(x:Foo)
`
	got, err := HasHookContent(strings.NewReader(src))
	if err != nil {
		t.Fatal(err)
	}
	if got {
		t.Fatal("slot field misclassified as hook")
	}
}

func TestHasHookContent_IgnoresLineComment(t *testing.T) {
	src := `module gom.C
abstract syntax
Foo = A() | B()
// EmptyTerm:make() { … } -- this is in a comment, must be ignored
`
	got, err := HasHookContent(strings.NewReader(src))
	if err != nil {
		t.Fatal(err)
	}
	if got {
		t.Fatal("hook inside line comment must not be detected")
	}
}

func TestScanDir(t *testing.T) {
	dir := t.TempDir()
	mustWrite := func(rel, body string) {
		full := filepath.Join(dir, rel)
		if err := os.MkdirAll(filepath.Dir(full), 0o755); err != nil {
			t.Fatal(err)
		}
		if err := os.WriteFile(full, []byte(body), 0o644); err != nil {
			t.Fatal(err)
		}
	}
	mustWrite("a/Plain.gom", "module a.Plain\nabstract syntax\nFoo = X()\n")
	mustWrite("b/WithHook.gom", "module b.W\nabstract syntax\nFoo = X()\nFoo:make(){\n}\n")
	mustWrite("c/skip.txt", "not a gom file")
	// Should be skipped by directory filter.
	mustWrite("build/Generated.gom", "module bad\nFoo:make(){\n}\n")

	rep, err := ScanDir(dir)
	if err != nil {
		t.Fatal(err)
	}
	total, noHooks, withHooks := rep.Totals()
	if total != 2 || noHooks != 1 || withHooks != 1 {
		t.Fatalf("totals: total=%d no=%d with=%d (want 2/1/1) — rep=%+v", total, noHooks, withHooks, rep)
	}
	if rep.NoHooks[0] != filepath.Join("a", "Plain.gom") {
		t.Errorf("unexpected NoHooks: %v", rep.NoHooks)
	}
	if rep.WithHooks[0] != filepath.Join("b", "WithHook.gom") {
		t.Errorf("unexpected WithHooks: %v", rep.WithHooks)
	}
}
