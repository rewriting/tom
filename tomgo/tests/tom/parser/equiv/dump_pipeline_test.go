package tomparseq

import (
	"os"
	"path/filepath"
	"strings"
	"testing"
)

// TestPipelineDump_PhasesAdvance exercises TomPipelineDump end-to-end on
// the byte-stable match0b fixture and asserts that:
//
//  1. each phase produces a non-empty AST,
//  2. parsed → transformed is a pass-through (no transformations apply),
//  3. desugared renames the parser's `EmptyName()` (the `_` pattern) to a
//     fresh `Name("_f_r_e_s_h_v_a_r_1")` — the Desugarer's canonical first
//     rewrite — so the dump differs from `parsed`.
//
// This is the smoke test that the new pipeline harness works at all; the
// per-plugin equivalence suites will live next to each Go plugin under
// stable/platform/plugins/.
func TestPipelineDump_PhasesAdvance(t *testing.T) {
	cwd, err := os.Getwd()
	if err != nil {
		t.Fatal(err)
	}
	repoRoot, err := FindRepoRoot(cwd)
	if err != nil {
		t.Skip("repo root not found: " + err.Error())
	}
	tc, err := Resolve(repoRoot)
	if err != nil {
		SkipIfNoJava(t, err)
	}

	fixture := filepath.Join(repoRoot, "tomgo", "tests", "testdata", "parse", "match0b", "scenario.t")

	parsed, err := tc.DumpJavaPhase(fixture, PhaseParsed)
	if err != nil {
		t.Fatalf("parsed: %v", err)
	}
	if !strings.HasPrefix(parsed, "Tom(") || !strings.Contains(parsed, "EmptyName()") {
		t.Errorf("parsed dump missing expected anchors:\n%s", parsed)
	}

	transformed, err := tc.DumpJavaPhase(fixture, PhaseTransformed)
	if err != nil {
		t.Fatalf("transformed: %v", err)
	}
	if transformed != parsed {
		t.Errorf("transformed should equal parsed on match0b (no transformations declared)\nparsed:\n%s\ntransformed:\n%s", parsed, transformed)
	}

	desugared, err := tc.DumpJavaPhase(fixture, PhaseDesugared)
	if err != nil {
		t.Fatalf("desugared: %v", err)
	}
	if !strings.Contains(desugared, `Name("_f_r_e_s_h_v_a_r_1")`) {
		t.Errorf("desugared dump should rename EmptyName → fresh var:\n%s", desugared)
	}
	if strings.Contains(desugared, "EmptyName()") {
		t.Errorf("desugared dump should not still contain EmptyName():\n%s", desugared)
	}
}
