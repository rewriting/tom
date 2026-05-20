package equivtest

import (
	"os"
	"path/filepath"
	"testing"
)

// TestEquivalence exercises the Go/Java behavioural equivalence on a
// hand-picked subset of the Phase 1 corpus. Skips cleanly if the JDK
// or the reference jars are not available on this host.
//
// For each target we run the pipeline:
//
//	.gom ─┬─ tomgo backend  → Go package → go run    → stdoutGo
//	      └─ tom.gom.Gom    → Java pkg   → javac+java → stdoutJava
//	            diff(stdoutGo, stdoutJava) must be empty.
//
// The persistent per-target stdout snapshots are written under
// tomgo/reports/equiv-<target>/ for easy after-the-fact inspection.
func TestEquivalence(t *testing.T) {
	repo, err := LocateRepoRoot()
	if err != nil {
		t.Skipf("repo root not found: %v", err)
	}
	tc, err := Resolve(repo)
	if err != nil {
		t.Skipf("Java toolchain unavailable: %v", err)
	}

	cases := []struct {
		name    string
		gomFile string // relative to corpus dir
		dir     string // relative to testdata/equiv
	}{
		{"minimal", "Minimal.gom", "minimal"},
		{"leaf", "Leaf.gom", "leaf"},
		{"list", "List.gom", "list"},
	}
	for _, c := range cases {
		c := c
		t.Run(c.name, func(t *testing.T) {
			gomFile := filepath.Join(repo, "tomgo", "testdata", "corpus", "gom-nohooks", c.gomFile)
			scnGo := filepath.Join(repo, "tomgo", "testdata", "equiv", c.dir, "scenario.go")
			scnJava := filepath.Join(repo, "tomgo", "testdata", "equiv", c.dir, "Scenario.java")
			workDir := filepath.Join("..", "..", "reports", "equiv-"+c.name)
			if err := CompareTarget(tc, gomFile, scnGo, scnJava, workDir); err != nil {
				t.Fatal(err)
			}
			if data, err := os.ReadFile(filepath.Join(workDir, "go-stdout.txt")); err == nil {
				t.Logf("identical stdout (both sides):\n%s", string(data))
			}
		})
	}
}
