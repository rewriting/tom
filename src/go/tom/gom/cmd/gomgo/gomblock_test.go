package main

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"testing"

	"tom/gom/backend"
	gomparser "tom/gom/parser"
)

func TestWriteGomBlock(t *testing.T) {
	dir := t.TempDir()
	src := filepath.Join(dir, "in.gom")
	body := "module M\nabstract syntax\nT = a() | f(x:T)\n"
	if err := os.WriteFile(src, []byte(body), 0o644); err != nil {
		t.Fatal(err)
	}
	mod, err := gomparser.ParseFile(src)
	if err != nil {
		t.Fatal(err)
	}
	out := filepath.Join(dir, "out")
	tomPath, err := writeGomBlock(mod, "fixture", out, backend.Options{})
	if err != nil {
		t.Fatal(err)
	}
	if want := filepath.Join(out, "M.tom"); tomPath != want {
		t.Fatalf("mapping path = %s, want %s", tomPath, want)
	}
	goSrc, err := os.ReadFile(filepath.Join(out, "m_gen.go"))
	if err != nil || !strings.HasPrefix(string(goSrc), "package fixture") && !strings.Contains(string(goSrc), "\npackage fixture") {
		t.Fatalf("m_gen.go missing or wrong package: %v", err)
	}
	mapping, _ := os.ReadFile(tomPath)
	if !strings.Contains(string(mapping), "%op T f(") || strings.Contains(string(mapping), "instanceof") {
		t.Fatalf("mapping is not the Go-host-code mapping:\n%s", mapping)
	}
}

// Builtin imports bring their mapping in, as Java Gom does.
func TestWriteGomBlockBuiltinIncludes(t *testing.T) {
	dir := t.TempDir()
	src := filepath.Join(dir, "in.gom")
	body := "module M\nimports int String\nabstract syntax\nT = f(x:int, s:String)\n"
	if err := os.WriteFile(src, []byte(body), 0o644); err != nil {
		t.Fatal(err)
	}
	mod, err := gomparser.ParseFile(src)
	if err != nil {
		t.Fatal(err)
	}
	tomPath, err := writeGomBlock(mod, "fixture", filepath.Join(dir, "out"), backend.Options{})
	if err != nil {
		t.Fatal(err)
	}
	mapping, _ := os.ReadFile(tomPath)
	if !strings.HasPrefix(string(mapping), "%include { string.tom }\n%include { int.tom }\n\n") {
		t.Fatalf("builtin includes missing:\n%s", mapping)
	}
}

// --withCongruenceStrategies adds the congruence strategy operators
// (Java Gom's StratMappingTemplate), and only then.
func TestRunGomBlockCongruenceStrategies(t *testing.T) {
	dir := t.TempDir()
	src := filepath.Join(dir, "in.gom")
	body := "module M\nabstract syntax\nT = a() | f(x:T, y:T) | l(T*)\n"
	if err := os.WriteFile(src, []byte(body), 0o644); err != nil {
		t.Fatal(err)
	}
	for _, with := range []bool{false, true} {
		out := filepath.Join(dir, fmt.Sprint(with))
		args := []string{"-o", out, "--pkg", "fixture"}
		if with {
			args = append(args, "--withCongruenceStrategies")
		}
		if err := runGomBlock(append(args, src)); err != nil {
			t.Fatal(err)
		}
		mapping, _ := os.ReadFile(filepath.Join(out, "M.tom"))
		goSrc, _ := os.ReadFile(filepath.Join(out, "m_gen.go"))
		if strings.Contains(string(goSrc), "type BuildF struct") != with {
			t.Errorf("with=%v: BuildF (Make_f) present=%v", with, !with)
		}
		for _, want := range []string{
			"%op Strategy _f(arg0:Strategy, arg1:Strategy) {",
			"make(arg0, arg1) { NewVisitF($arg0, $arg1) }",
			"%op Strategy When_a(s:Strategy) {",
			"make(s) { sl.NewSequence(&IsA{}, $s) }",
			"%op Strategy Is_f() {",
			"%op Strategy _l(sub:Strategy) {",
			"%op Strategy Make_f(Make_farg0:Strategy, Make_farg1:Strategy) {",
			"make(a0, a1) { NewBuildF($a0, $a1) }",
		} {
			if strings.Contains(string(mapping), want) != with {
				t.Errorf("with=%v: %q present=%v\n%s", with, want, !with, mapping)
			}
		}
	}
	if err := runGomBlock([]string{"-o", dir, "--pkg", "fixture", "--termpointer", src}); err == nil {
		t.Error("--termpointer accepted, want an error (not supported)")
	}
}

// A user `op:make` hook: the Go constructor consults hookMake<Op>, which
// the mapping's host code sets from the (Tom+Go) hook body.
func TestWriteGomBlockUserMakeHook(t *testing.T) {
	dir := t.TempDir()
	src := filepath.Join(dir, "in.gom")
	body := "module M\nabstract syntax\nT = a() | f(x:T, y:T)\nf:make(l,r) {\n  %match(l) { a() -> { return `a(); } }\n}\n"
	if err := os.WriteFile(src, []byte(body), 0o644); err != nil {
		t.Fatal(err)
	}
	mod, err := gomparser.ParseFile(src)
	if err != nil {
		t.Fatal(err)
	}
	out := filepath.Join(dir, "out")
	tomPath, err := writeGomBlock(mod, "fixture", out, backend.Options{})
	if err != nil {
		t.Fatal(err)
	}
	goSrc, _ := os.ReadFile(filepath.Join(out, "m_gen.go"))
	for _, want := range []string{"var hookMakeF func(T, T) T", "if r := hookMakeF(x, y); r != nil {"} {
		if !strings.Contains(string(goSrc), want) {
			t.Errorf("m_gen.go lacks %q", want)
		}
	}
	mapping, _ := os.ReadFile(tomPath)
	for _, want := range []string{"hookMakeF = func(l T, r T) T {", "%match(l) { a() -> { return `a(); } }", "; return nil;"} {
		if !strings.Contains(string(mapping), want) {
			t.Errorf("mapping lacks %q:\n%s", want, mapping)
		}
	}
}
