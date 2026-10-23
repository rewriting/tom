package backend_test

import (
	"bytes"
	"fmt"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"testing"

	gomast "tom/gom/adt"
	"tom/gom/backend"
	gomparser "tom/gom/parser"
)

// The compiler's ADTs: src/go/tom/<dir>/*.gom are the sources (the Go
// copies of the Java compiler's ADT .gom files, hooks in Go),
// stable/go/tom/<dir> the committed generation (the amorce), from which
// `mage -d magefiles -w . bootstrap` generates src/go/tom/<dir> locally.
var compilerADTs = []struct{ dir, pkg string }{
	{"gom/adt", "adt"},
	{"engine/adt", "adt"},
}

// repoRoot is the root of the repository, from this package.
func repoRoot(t *testing.T) string {
	t.Helper()
	root, err := filepath.Abs(filepath.Join("..", "..", "..", "..", ".."))
	if err != nil {
		t.Fatal(err)
	}
	if _, err := os.Stat(filepath.Join(root, "build.xml")); err != nil {
		t.Fatalf("repository root not found at %s", root)
	}
	return root
}

// gomFiles returns the .gom files of dir, sorted (the generation order
// of mage bootstrap).
func gomFiles(t *testing.T, dir string) []string {
	t.Helper()
	files, err := filepath.Glob(filepath.Join(dir, "*.gom"))
	if err != nil || len(files) == 0 {
		t.Fatalf("no .gom file in %s: %v", dir, err)
	}
	sort.Strings(files)
	return files
}

// TestSelfBootstrap regenerates each of the compiler's ADTs from its .gom
// files (src/go/tom/<dir>) with the current backend, as mage bootstrap
// does (gomgo gom-batch --gohooks), and requires the amorce
// stable/go/tom/<dir> to be byte-identical: gom/adt — the AST gomgo is
// built on — is a fixpoint of its own generation, and the amorce of
// engine/adt is up to date. After a deliberate change of the backend or
// of a .gom file, run `mage -d magefiles -w . promoteAdt`, then bootstrap.
func TestSelfBootstrap(t *testing.T) {
	root := repoRoot(t)
	for _, a := range compilerADTs {
		t.Run(a.dir, func(t *testing.T) {
			var mods []gomast.GomModule
			for _, f := range gomFiles(t, filepath.Join(root, "src", "go", "tom", filepath.FromSlash(a.dir))) {
				m, err := gomparser.ParseFile(f)
				if err != nil {
					t.Fatal(err)
				}
				mods = append(mods, m)
			}
			out := t.TempDir()
			if _, err := backend.GenerateBatchToDir(mods, backend.Options{PackageName: a.pkg, GoHooks: true}, out); err != nil {
				t.Fatal(err)
			}
			stable := filepath.Join(root, "stable", "go", "tom", filepath.FromSlash(a.dir))
			list := func(dir string) []string {
				entries, err := os.ReadDir(dir)
				if err != nil {
					t.Fatal(err)
				}
				var names []string
				for _, e := range entries {
					if n := e.Name(); !e.IsDir() && n != "go.mod" && n != ".DS_Store" {
						names = append(names, n)
					}
				}
				return names
			}
			gen, amorce := list(out), list(stable)
			if strings.Join(gen, " ") != strings.Join(amorce, " ") {
				t.Fatalf("generated files %v, amorce %s has %v", gen, stable, amorce)
			}
			for _, n := range gen {
				x, _ := os.ReadFile(filepath.Join(out, n))
				y, _ := os.ReadFile(filepath.Join(stable, n))
				if !bytes.Equal(x, y) {
					t.Errorf("%s: the regeneration differs from the amorce stable/go/tom/%s/%s — if deliberate, run `mage -d magefiles -w . promoteAdt`", n, a.dir, n)
				}
			}
		})
	}
}

// signature describes a Gom module without its hooks: its name, its
// imports and, for every sort, its operators with their slots (name and
// sort, `*` for a variadic slot).
func signature(m gomast.GomModule) string {
	var b strings.Builder
	fmt.Fprintf(&b, "module %s\nimports %s\n", gomparser.QualifiedName(m), strings.Join(gomparser.Imports(m), " "))
	for _, prod := range gomparser.Sorts(m) {
		fmt.Fprintf(&b, "sort %s\n", gomparser.SortName(prod))
		for _, a := range prod.AlternativeList.(*gomast.ConcAlternativeAlternativeList).ToArray() {
			alt := a.(*gomast.AlternativeAlternative)
			var slots []string
			for _, f := range alt.DomainList.(*gomast.ConcFieldFieldList).ToArray() {
				switch fld := f.(type) {
				case *gomast.NamedFieldField:
					slots = append(slots, fld.Name+":"+fld.FieldType.(*gomast.GomTypeGomType).Name)
				case *gomast.StarredFieldField:
					slots = append(slots, fld.FieldType.(*gomast.GomTypeGomType).Name+"*")
				default:
					slots = append(slots, fmt.Sprint(f))
				}
			}
			fmt.Fprintf(&b, "  %s(%s) : %s\n", alt.Name, strings.Join(slots, ", "), alt.Codomain.(*gomast.GomTypeGomType).Name)
		}
	}
	return b.String()
}

// TestSignatureParity checks that the Go copies of the ADT .gom files
// (src/go/tom/gom/adt, src/go/tom/engine/adt) declare exactly the sorts,
// operators, slots and imports of the Java compiler's originals
// (src/tom/gom/adt, src/tom/engine/adt): only the hooks differ
// (rewritten in Go). An intended evolution of the Go ASTs will update
// this test.
func TestSignatureParity(t *testing.T) {
	root := repoRoot(t)
	for _, a := range compilerADTs {
		t.Run(a.dir, func(t *testing.T) {
			javaFiles := gomFiles(t, filepath.Join(root, "src", "tom", filepath.FromSlash(a.dir)))
			goFiles := gomFiles(t, filepath.Join(root, "src", "go", "tom", filepath.FromSlash(a.dir)))
			base := func(fs []string) string {
				var out []string
				for _, f := range fs {
					out = append(out, filepath.Base(f))
				}
				return strings.Join(out, " ")
			}
			if base(javaFiles) != base(goFiles) {
				t.Fatalf("the .gom files differ: Java %s, Go %s", base(javaFiles), base(goFiles))
			}
			for i := range javaFiles {
				jm, err := gomparser.ParseFile(javaFiles[i])
				if err != nil {
					t.Fatal(err)
				}
				gm, err := gomparser.ParseFile(goFiles[i])
				if err != nil {
					t.Fatal(err)
				}
				if js, gs := signature(jm), signature(gm); js != gs {
					t.Errorf("%s: the signature of the Go copy differs from the Java original's\n--- Java\n%s--- Go\n%s", filepath.Base(goFiles[i]), js, gs)
				}
			}
		})
	}
}
