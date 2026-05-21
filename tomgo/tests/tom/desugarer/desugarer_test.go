package desugarer_test

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
	"tom/tomgo/tests/tom/javatc"
)

// TestDesugarer_FreshVariableRewrite is the unit-level smoke test:
// build a Variable with EmptyName by hand, run it through the
// Desugarer plugin via the Platform, and assert the result is a
// Variable carrying Name("_f_r_e_s_h_v_a_r_1") — matching the Java
// pre-increment convention.
func TestDesugarer_FreshVariableRewrite(t *testing.T) {
	cwd, _ := os.Getwd()
	abs, _ := filepath.Abs(filepath.Join(cwd, "..", "..", "..", "tests", "testdata", "parse", "match0b", "scenario.t"))
	parserState, err := platform.New(parser.Plugin{}).Run(platform.State{Filename: abs})
	if err != nil {
		t.Fatalf("parse: %v", err)
	}

	state, err := desugarer.Plugin{}.Run(platform.State{Code: parserState.Code})
	if err != nil {
		t.Fatalf("desugarer: %v", err)
	}
	got := fmt.Sprintf("%v", state.Code)
	if !strings.Contains(got, `Name("_f_r_e_s_h_v_a_r_1")`) {
		t.Errorf("expected fresh-var rewrite of EmptyName, got:\n%s", got)
	}
	if strings.Contains(got, "EmptyName()") {
		t.Errorf("Desugarer should have replaced EmptyName(), still present:\n%s", got)
	}
}

// TestDesugarer_CounterResetsPerRun confirms the Desugarer's fresh
// counter is per-Run, not global: two consecutive runs of the same
// input both emit `_f_r_e_s_h_v_a_r_1`, not 1 then 2.
func TestDesugarer_CounterResetsPerRun(t *testing.T) {
	cwd, _ := os.Getwd()
	abs, _ := filepath.Abs(filepath.Join(cwd, "..", "..", "..", "tests", "testdata", "parse", "match0b", "scenario.t"))
	parserState, err := platform.New(parser.Plugin{}).Run(platform.State{Filename: abs})
	if err != nil {
		t.Fatalf("parse: %v", err)
	}

	first, err := desugarer.Plugin{}.Run(platform.State{Code: parserState.Code})
	if err != nil {
		t.Fatal(err)
	}
	second, err := desugarer.Plugin{}.Run(platform.State{Code: parserState.Code})
	if err != nil {
		t.Fatal(err)
	}
	if fmt.Sprintf("%v", first.Code) != fmt.Sprintf("%v", second.Code) {
		t.Errorf("counter leaked across runs:\nfirst:\n%s\nsecond:\n%s", first.Code, second.Code)
	}
}

// TestDesugarer_ParityWithJava is the regression net: for every
// fixture in testdata/parse/, run the Go pipeline (Parser → Desugarer)
// and compare the resulting Code against Java's PhaseDesugared dump.
// Both sides go through the same path-normalisation (__INPUT__ /
// __DIR__) so byte-equality is meaningful.
//
// Skipped without JDK / stable/dist/lib. The fixtures the Java
// Desugarer leaves unchanged are also covered: parsed == desugared
// on the Java side, parsed == desugared on the Go side, so we still
// assert byte-equality.
func TestDesugarer_ParityWithJava(t *testing.T) {
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
			// Java reference: AST after the Desugarer phase, with
			// absInput / dirname collapsed to __INPUT__ / __DIR__.
			javaOut, err := tc.DumpJavaPhase(input, tomparseq.PhaseDesugared)
			if err != nil {
				t.Fatalf("java: %v", err)
			}
			// Go: Platform(Starter, Parser, Desugarer) on the same input.
			goState, err := platform.New(
				starter.Plugin{},
				parser.Plugin{},
				desugarer.Plugin{},
			).Run(platform.State{Filename: input})
			if err != nil {
				t.Fatalf("go: %v", err)
			}
			goOut := fmt.Sprintf("%v", goState.Code)
			goOut = strings.ReplaceAll(goOut, input, "__INPUT__")
			goOut = strings.ReplaceAll(goOut, filepath.Dir(input), "__DIR__")
			if goOut != javaOut {
				t.Errorf("AST mismatch on %s after Desugarer\n--- java ---\n%s\n--- go ---\n%s\n",
					name, javaOut, goOut)
			}
		})
	}
}

