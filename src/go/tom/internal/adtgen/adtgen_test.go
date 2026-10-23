package adtgen

import (
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
	"sort"
	"strings"
	"testing"

	gomast "tom/gom/adt"
	gomparser "tom/gom/parser"
)

// moduleRoot is the root of module tom (src/go/tom), from this package.
func moduleRoot(t *testing.T) string {
	t.Helper()
	root, err := filepath.Abs(filepath.Join("..", ".."))
	if err != nil {
		t.Fatal(err)
	}
	return root
}

// tools returns the Tom distribution that compiles the hooks: $TOM_HOME,
// else the repository's src/dist. When it (or java) is missing the test
// is skipped, or fails when TOMGO_REQUIRE_TOOLS=1 (as the harness of
// test/go does).
func tools(t *testing.T) Tools {
	t.Helper()
	missing := func(format string, args ...any) {
		t.Helper()
		msg := fmt.Sprintf(format, args...)
		if os.Getenv("TOMGO_REQUIRE_TOOLS") == "1" {
			t.Fatalf("%s (TOMGO_REQUIRE_TOOLS=1: required tools must be present)", msg)
		}
		t.Skip(msg)
	}
	if _, err := exec.LookPath("java"); err != nil {
		missing("java not found in PATH")
	}
	home := os.Getenv("TOM_HOME")
	if home == "" {
		home = filepath.Join(moduleRoot(t), "..", "..", "dist")
	}
	home, err := filepath.Abs(home)
	if err != nil {
		t.Fatal(err)
	}
	if _, err := os.Stat(filepath.Join(home, "bin", "tom")); err != nil {
		missing("no Tom distribution at %s (build it: ./build.sh src.dist, or set TOM_HOME)", home)
	}
	return Tools{TomHome: home}
}

// TestSelfBootstrap regenerates each of the compiler's ADTs from its
// .gom files with the current gomgo backend (and, for the hooks written
// in Tom+Go, tom -go) and requires the committed package to be
// byte-identical: the generated code is never edited by hand, and gom/adt
// — the AST gomgo itself is built on — is a fixpoint of its own
// generation. After an intentional change of the backend or of a .gom
// file, regenerate with `mage -d magefiles -w . regenAdt`.
func TestSelfBootstrap(t *testing.T) {
	root := moduleRoot(t)
	for _, a := range ADTs {
		t.Run(a.OutDir(), func(t *testing.T) {
			var tl Tools
			if a.Hooks != "" {
				tl = tools(t)
			}
			files, err := Generate(root, a, tl, t.TempDir())
			if err != nil {
				t.Fatal(err)
			}
			diff, err := Diff(filepath.Join(root, a.OutDir()), files)
			if err != nil {
				t.Fatal(err)
			}
			for _, d := range diff {
				t.Errorf("%s/%s differs from its regeneration: regenerate with `mage -d magefiles -w . regenAdt`", a.OutDir(), d)
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
// (gom/adt, engine/adt) declare exactly the sorts, operators, slots and
// imports of the Java compiler's originals (src/tom/gom/adt,
// src/tom/engine/adt): only the hooks differ (rewritten in Go). An
// intended evolution of the Go ASTs will update this test.
func TestSignatureParity(t *testing.T) {
	root := moduleRoot(t)
	repo := filepath.Join(root, "..", "..", "..")
	for _, dir := range []string{"gom/adt", "engine/adt"} {
		t.Run(dir, func(t *testing.T) {
			javaDir := filepath.Join(repo, "src", "tom", filepath.FromSlash(dir))
			goDir := filepath.Join(root, filepath.FromSlash(dir))
			javaFiles, _ := filepath.Glob(filepath.Join(javaDir, "*.gom"))
			goFiles, _ := filepath.Glob(filepath.Join(goDir, "*.gom"))
			base := func(fs []string) []string {
				var out []string
				for _, f := range fs {
					out = append(out, filepath.Base(f))
				}
				sort.Strings(out)
				return out
			}
			if j, g := base(javaFiles), base(goFiles); strings.Join(j, " ") != strings.Join(g, " ") || len(j) == 0 {
				t.Fatalf("the .gom files differ: Java %v, Go %v", j, g)
			}
			for _, name := range base(javaFiles) {
				jm, err := gomparser.ParseFile(filepath.Join(javaDir, name))
				if err != nil {
					t.Fatal(err)
				}
				gm, err := gomparser.ParseFile(filepath.Join(goDir, name))
				if err != nil {
					t.Fatal(err)
				}
				if js, gs := signature(jm), signature(gm); js != gs {
					t.Errorf("%s: the signature of the Go copy differs from the Java original's\n--- Java\n%s--- Go\n%s", name, js, gs)
				}
			}
		})
	}
}
