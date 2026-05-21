package typer_test

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/stable/platform"
	tomparseq "tom/tomgo/tests/tom/parser/equiv"
	"tom/tomgo/stable/tom/starter"
	"tom/tomgo/stable/tom/parser"
	"tom/tomgo/stable/tom/desugarer"
	"tom/tomgo/stable/tom/typer"
	"tom/tomgo/tests/tom/javatc"
)

// TestTyper_ParityWithJava is the regression net for the Typer:
// Parser → Desugarer → Typer in Go must match Java's PhaseTyped dump
// for every fixture in testdata/parse/. Skipped without JDK /
// stable/dist/lib.
func TestTyper_ParityWithJava(t *testing.T) {
	repoRoot, tc, ok := javatc.Resolve(t)
	if !ok {
		return
	}
	fixturesDir := filepath.Join(repoRoot, "tomgo", "tests", "testdata", "parse")
	entries, err := os.ReadDir(fixturesDir)
	if err != nil {
		t.Fatal(err)
	}
	for _, e := range entries {
		if !e.IsDir() {
			continue
		}
		name := e.Name()
		t.Run(name, func(t *testing.T) {
			input := filepath.Join(fixturesDir, name, "scenario.t")
			if _, err := os.Stat(input); err != nil {
				t.Skipf("no scenario.t in %s", name)
			}
			javaOut, err := tc.DumpJavaPhase(input, tomparseq.PhaseTyped)
			if err != nil {
				t.Fatalf("java: %v", err)
			}
			goState, err := platform.New(
				starter.Plugin{},
				parser.Plugin{},
				desugarer.Plugin{},
				typer.Plugin{},
			).Run(platform.State{Filename: input})
			if err != nil {
				t.Fatalf("go: %v", err)
			}
			goOut := fmt.Sprintf("%v", goState.Code)
			goOut = strings.ReplaceAll(goOut, input, "__INPUT__")
			goOut = strings.ReplaceAll(goOut, filepath.Dir(input), "__DIR__")
			if goOut != javaOut {
				t.Errorf("AST mismatch on %s after Typer\n--- java ---\n%s\n--- go ---\n%s\n",
					name, javaOut, goOut)
			}
		})
	}
}
