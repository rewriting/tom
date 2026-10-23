package backend

import (
	"bytes"
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

// A javablock hook (Java Gom: host code without Tom code) is plain Go,
// written into the generated Go file; the methods a sort javablock
// declares on every alternative of the sort are added to the sort's
// interface (Java: methods of the sort class), those declared on some
// alternatives only are not.
func TestJavaBlock(t *testing.T) {
	src := `module m.M
abstract syntax
T = a() | f(x:T)
sort T:javablock() {
  func (t *AT) Size() int { return 1 }
  func (t *FT) Size() int { return 1 + t.X.Size() }
  func (t *FT) Only() bool { return true }
  func helper() int { return 0 }
}
`
	mod, err := gom.ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	if err := CheckHooks(mod); err != nil {
		t.Fatal(err)
	}
	var buf bytes.Buffer
	if err := Generate(mod, Options{PackageName: "m"}, &buf); err != nil {
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
	for _, want := range []string{"func (t *FT) Size() int { return 1 + t.X.Size() }", "func helper() int { return 0 }"} {
		if !strings.Contains(flat, want) {
			t.Errorf("generated code lacks %q", want)
		}
	}
	if strings.Contains(string(EmitTomMappingGo(mod, Options{})), "Size") {
		t.Error("a javablock must not be written into the mapping")
	}
}

// A javablock holds Go declarations, without Tom code.
func TestJavaBlockRejectsTomCode(t *testing.T) {
	src := "module m.M\nabstract syntax\nT = a()\nsort T:javablock() {\n  func f(t T) bool { %match(t) { a() -> { return true } }; return false }\n}\n"
	mod, err := gom.ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	if err := CheckHooks(mod); err == nil || !strings.Contains(err.Error(), "javablock") {
		t.Fatalf("CheckHooks = %v, want a javablock error", err)
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

// Two operators with the same Go name cannot share a Go package.
func TestGenerateBatchRejectsGoNameCollision(t *testing.T) {
	a, _ := gom.ParseBytes([]byte("module A\nabstract syntax\nS = subterm()\n"))
	b, _ := gom.ParseBytes([]byte("module B\nabstract syntax\nU = Subterm()\n"))
	_, err := GenerateBatchToDir([]gomast.GomModule{a, b}, Options{PackageName: "p"}, t.TempDir())
	if err == nil || !strings.Contains(err.Error(), "same Go name Subterm") {
		t.Fatalf("GenerateBatchToDir = %v, want a Go name collision error", err)
	}
}
