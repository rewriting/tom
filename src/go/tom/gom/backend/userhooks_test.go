package backend

import (
	"bytes"
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"testing"

	gomast "tom/gom/adt"
	gom "tom/gom/parser"
)

// A `//` comment of a hook body becomes a block comment in the mapping,
// whose host code Tom emits on a single line.
func TestMappingHostCode(t *testing.T) {
	for in, want := range map[string]string{
		"a() -> { return l; } // keep\nb()": "a() -> { return l; } /* keep */\nb()",
		`s := "http://x" // c`:              `s := "http://x" /* c */`,
		"x := '/' // a */ b":                "x := '/' /* a * / b */",
		"/* already */ y // z":              "/* already */ y /* z */",
		"no comment":                        "no comment",
		"`f(x) // c\n":                      "`f(x) /* c */\n",
	} {
		if got := mappingHostCode(in); got != want {
			t.Errorf("mappingHostCode(%q) = %q, want %q", in, got, want)
		}
	}
}

// With GoHooks (gomgo --gohooks), the hooks are plain Go written into
// the generated Go file, not into the mapping: a construction hook sets
// its Hook<Make> variable at init, a block hook is Go declarations, and
// the methods a sort block declares on every alternative of the sort are
// added to the sort's interface (Java: methods of the sort class), those
// declared on some alternatives only are not.
func TestGoHooks(t *testing.T) {
	src := `module m.M
abstract syntax
T = a() | f(x:T)
L = conc(T*)
sort T:block() {
  func (t *AT) Size() int { return 1 }
  func (t *FT) Size() int { return 1 + t.X.Size() }
  func (t *FT) Only() bool { return true }
  func helper() int { return 0 }
}
f:make(x) {
  if x.Isf() {
    return x.Getx()
  }
}
conc:make_insert(e,l) {
  if e.Isf() {
    e = e.Getx()
  }
}
`
	mod, err := gom.ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	var buf bytes.Buffer
	if err := Generate(mod, Options{PackageName: "m", GoHooks: true}, &buf); err != nil {
		t.Fatal(err)
	}
	code := buf.String()
	iface := code[strings.Index(code, "type T interface {"):]
	iface = iface[:strings.Index(iface, "\n}\n")]
	if !strings.Contains(iface+"\n", "\tSize() int\n") || strings.Contains(iface, "Only()") {
		t.Errorf("interface T:\n%s", iface)
	}
	// (gofmt aligns the bodies of consecutive one-line functions)
	flat := strings.Join(strings.Fields(code), " ")
	for _, want := range []string{
		"func (t *FT) Size() int { return 1 + t.X.Size() }",
		"func helper() int { return 0 }",
		"HookMakeF = func(x T) (T, T) {",
		"HookMakeInsertConc = func(e T, l L) (L, T, L) {",
	} {
		if !strings.Contains(flat, want) {
			t.Errorf("generated code lacks %q", want)
		}
	}
	if m := string(EmitTomMappingGo(mod, Options{GoHooks: true})); strings.Contains(m, "Size") || strings.Contains(m, "HookMake") {
		t.Errorf("the hooks must not be written into the mapping:\n%s", m)
	}
	// without GoHooks, they are Tom+Go code of the mapping
	if m := string(EmitTomMappingGo(mod, Options{})); !strings.Contains(m, "HookMakeF = func(") {
		t.Errorf("without GoHooks the make hook belongs to the mapping:\n%s", m)
	}
}

// With GoHooks, a hook holds Go code without Tom code, and the rules
// (translated into Tom code) are refused.
func TestGoHooksRejectsTomCode(t *testing.T) {
	for _, src := range []string{
		"module m.M\nabstract syntax\nT = a()\nsort T:block() {\n  func f(t T) bool { %match(t) { a() -> { return true } }; return false }\n}\n",
		"module m.M\nabstract syntax\nT = a() | g(x:T)\ng:make(x) {\n  %match(x) { a() -> { return `a(); } }\n}\n",
		"module M\nabstract syntax\nT = a() | g(x:T)\nmodule M:rules() {\n  g(a()) -> a()\n}\n",
	} {
		mod, err := gom.ParseBytes([]byte(src))
		if err != nil {
			t.Fatal(err)
		}
		var buf bytes.Buffer
		if err := Generate(mod, Options{PackageName: "m", GoHooks: true}, &buf); err == nil || !strings.Contains(err.Error(), "--gohooks") {
			t.Errorf("Generate(GoHooks) = %v, want a --gohooks error, for\n%s", err, src)
		}
		if err := CheckHooks(mod); err != nil {
			t.Errorf("without GoHooks the hooks are Tom+Go code: CheckHooks = %v", err)
		}
	}
}

// The rules of a module apply to the operators of the other modules of
// the package (Java Gom's RuleExpander works on the module list): a
// rule of A on an operator of B is a hook of B's operator.
func TestRulesAcrossPackageModules(t *testing.T) {
	a, err := gom.ParseBytes([]byte("module A\nimports B\nabstract syntax\nS = s(b:U)\nmodule A:rules() {\n  w(v()) -> v()\n}\n"))
	if err != nil {
		t.Fatal(err)
	}
	b, err := gom.ParseBytes([]byte("module B\nabstract syntax\nU = v() | w(u:U)\n"))
	if err != nil {
		t.Fatal(err)
	}
	opts := Options{PackageName: "p", PackageModules: []gomast.GomModule{b, a}, ImportedSorts: map[string]bool{"S": true}}
	var buf bytes.Buffer
	if err := GenerateGenerated(b, opts, true, &buf); err != nil {
		t.Fatal(err)
	}
	if !strings.Contains(buf.String(), "var HookMakeW func(U) (U, U)") {
		t.Errorf("w of B has no make hook from the rule of A:\n%s", buf.String())
	}
	if m := string(EmitTomMappingGo(b, opts)); !strings.Contains(m, "HookMakeW = func(") {
		t.Errorf("the mapping of B does not set HookMakeW:\n%s", m)
	}
	// alone, B has no hook
	buf.Reset()
	if err := Generate(b, Options{PackageName: "p"}, &buf); err != nil {
		t.Fatal(err)
	}
	if strings.Contains(buf.String(), "HookMakeW") {
		t.Error("B alone must not get the rule of A")
	}
}

// Two operators of a package with the same Go name (subterm and Subterm,
// of Il.gom and Code.gom) get the names derived from them suffixed with
// their sort; the other operators keep theirs.
func TestGenerateBatchGoNameCollision(t *testing.T) {
	a, _ := gom.ParseBytes([]byte("module A\nabstract syntax\nS = subterm() | s(S*)\n"))
	b, _ := gom.ParseBytes([]byte("module B\nabstract syntax\nU = Subterm() | u()\n"))
	dir, err := GenerateBatchToDir([]gomast.GomModule{a, b}, Options{PackageName: "p"}, t.TempDir())
	if err != nil {
		t.Fatal(err)
	}
	code := ""
	for _, f := range []string{"a.go", "b.go", "p.tom"} {
		bs, err := os.ReadFile(filepath.Join(dir, f))
		if err != nil {
			t.Fatal(err)
		}
		code += string(bs)
	}
	for _, want := range []string{"func MakeSubtermS() S", "func MakeSubtermU() U", "type IsSubtermS struct", "func MakeU() U", "func ConsS(", "make()       { MakeSubtermS() }"} {
		if !strings.Contains(code, want) {
			t.Errorf("generated code lacks %q", want)
		}
	}
	cmd := exec.Command("go", "build", "./...")
	cmd.Dir = dir
	if out, err := cmd.CombinedOutput(); err != nil {
		t.Fatalf("go build: %v\n%s", err, out)
	}
}
