// Frozen prototype copy — the maintained version is src/go/tom/gom/cmd/gomgo (module tom).

package main

import (
	"os"
	"path/filepath"
	"strings"
	"testing"

	gomparser "tom/tomgo/stable/gom/parser"
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
	tomPath, err := writeGomBlock(mod, "fixture", out)
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
	tomPath, err := writeGomBlock(mod, "fixture", filepath.Join(dir, "out"))
	if err != nil {
		t.Fatal(err)
	}
	mapping, _ := os.ReadFile(tomPath)
	if !strings.HasPrefix(string(mapping), "%include { string.tom }\n%include { int.tom }\n\n") {
		t.Fatalf("builtin includes missing:\n%s", mapping)
	}
}
