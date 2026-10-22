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

// A user `op:make` hook: the Go constructor consults HookMake<Op>, which
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
	for _, want := range []string{"var HookMakeF func(T, T) (T, T, T)", "hookResult, x, y = HookMakeF(x, y)", "if hookResult != nil {"} {
		if !strings.Contains(string(goSrc), want) {
			t.Errorf("m_gen.go lacks %q", want)
		}
	}
	mapping, _ := os.ReadFile(tomPath)
	for _, want := range []string{"HookMakeF = func(l T, r T) (T, T, T) {", "%match(l) { a() -> { return `a(); } }", "return nil;", "return tom__r, l, r;"} {
		if !strings.Contains(string(mapping), want) {
			t.Errorf("mapping lacks %q:\n%s", want, mapping)
		}
	}
}

// Several make hooks of one operator run in order, as in Java Gom
// (OperatorTemplate.generateMakeHooks): an argument reassigned by a hook
// reaches the next one, renamed when it names it differently.
func TestWriteGomBlockMultipleMakeHooks(t *testing.T) {
	dir := t.TempDir()
	src := filepath.Join(dir, "in.gom")
	body := "module M\nimports int\nabstract syntax\nW = Int(i:int)\nInt:make(i) {\n  i = i+1;\n}\nInt:make(j) {\n  j = j*2;\n}\n"
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
	want := "HookMakeInt = func(i int64) (W, int64) {\n    var j int64;\n    tom__r := func() W {\n    {\n\n  i = i+1;\n\n    };\n    j = i;\n    {\n\n  j = j*2;\n\n    };\n    return nil;\n    }();\n    return tom__r, j;\n"
	if !strings.Contains(string(mapping), want) {
		t.Errorf("mapping lacks\n%s\n---\n%s", want, mapping)
	}
}

// A module that imports user modules (Java Gom finds M.gom next to the
// importing file) is generated with them in one Go package — Gom modules
// may import each other cyclically, Go packages may not — and its
// mapping declares their sorts and operators too.
func TestRunGomBlockImports(t *testing.T) {
	dir := t.TempDir()
	files := map[string]string{
		"Ying.gom": "module gom.Ying\nimports Yang\nabstract syntax\nMoon = ping(sl:Sun)\n",
		"Yang.gom": "module gom.Yang\nimports Ying String\nabstract syntax\nSun = pong(st:Moon) | shi() | name(s:String)\n",
	}
	for name, body := range files {
		if err := os.WriteFile(filepath.Join(dir, name), []byte(body), 0o644); err != nil {
			t.Fatal(err)
		}
	}
	out := filepath.Join(dir, "out")
	if err := runGomBlock([]string{"-o", out, "--pkg", "ying", filepath.Join(dir, "Ying.gom")}); err != nil {
		t.Fatal(err)
	}
	ying, _ := os.ReadFile(filepath.Join(out, "ying_gen.go"))
	yang, _ := os.ReadFile(filepath.Join(out, "yang_gen.go"))
	if !strings.Contains(string(ying), "Sl   Sun") && !strings.Contains(string(ying), "Sl Sun") {
		t.Errorf("ying_gen.go: slot sl is not typed by the Go interface Sun:\n%s", ying)
	}
	if !strings.Contains(string(yang), "St   Moon") && !strings.Contains(string(yang), "St Moon") {
		t.Errorf("yang_gen.go: slot st is not typed by the Go interface Moon")
	}
	if strings.Count(string(ying)+string(yang), "var factory = ") != 1 {
		t.Errorf("the package must declare its factory exactly once")
	}
	mapping, _ := os.ReadFile(filepath.Join(out, "Ying.tom"))
	for _, want := range []string{"%include { string.tom }", "%typeterm Sun {", "%typeterm Moon {", "%op Sun pong(st:Moon) {", "%op Moon ping(sl:Sun) {"} {
		if !strings.Contains(string(mapping), want) {
			t.Errorf("mapping lacks %q:\n%s", want, mapping)
		}
	}
}

// A hook the Go backend cannot lower is an explicit error naming the
// hook and the module, not silently dropped.
func TestWriteGomBlockUnsupportedHook(t *testing.T) {
	dir := t.TempDir()
	src := filepath.Join(dir, "in.gom")
	for _, tc := range []struct{ body, want string }{
		// term-graph rewriting (Java Gom's --termgraph) is not implemented
		{"module M\nabstract syntax\nT = a() | k(x:T)\nsort T:graphrules(R,Identity) {\n  k(x) -> x\n}\n", "sort T:graphrules"},
		// Java Gom: the theory of a list operator with hooks must be given
		{"module M\nabstract syntax\nT = a() | l(T*)\nl:make_insert(e,l) {\n  return `l(e,l*);\n}\n", "no theory"},
		// Java runs a theory declared after rules in declaration order;
		// the Go backend runs the theory first: refused
		{"module M\nabstract syntax\nT = a() | b() | l(T*)\nmodule M:rules() {\n  l(a(), x*) -> l(b(), x*)\n}\nl:FL() {}\n", "must be declared before"},
		// the neutral element must be a constant of the module
		{"module M\nabstract syntax\nT = a() | l(T*)\nl:AU() { `b() }\n", "l:AU"},
	} {
		if err := os.WriteFile(src, []byte(tc.body), 0o644); err != nil {
			t.Fatal(err)
		}
		mod, err := gomparser.ParseFile(src)
		if err != nil {
			t.Fatal(err)
		}
		_, err = writeGomBlock(mod, "fixture", filepath.Join(dir, "out"), backend.Options{})
		if err == nil || !strings.Contains(err.Error(), tc.want) || !strings.Contains(err.Error(), "module M") {
			t.Errorf("err = %v, want an error naming %q and module M", err, tc.want)
		}
	}
}
