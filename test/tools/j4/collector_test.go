package j4

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"testing"
	"time"

	"tom/engine/backend"
	"tomtools/harness"
)

// Floors of TestCollectorState: the number of state lines (used marks and
// inliners) over all bundles, measured at Task 1 of J4.1b and rounded
// down; a Collector that marks nothing must not pass.
const (
	collectorMinLinesJ4     = 350  // measured: 355
	collectorMinLinesBridge = 8300 // measured: 8311
)

// TestCollectorState compares the state the Collector of the backend
// leaves (used marks, inliners: what decides which mapping functions are
// emitted, and how) in Go (backend.CollectorDump) and in Java
// (CollectorState.java), on the bridge bundles of queryCorpus (mage j4) or
// of T1 + smoke (mage bridgeCheck).
func TestCollectorState(t *testing.T) {
	dist := harness.NewDist(t)
	cp := javaHelpers(t, dist)
	paths, floor := bundles(t, dist)
	if len(paths) < floor {
		t.Fatalf("%d bundles, want at least %d", len(paths), floor)
	}
	outDir := t.TempDir()
	args := append([]string{"-cp", cp, "CollectorState", outDir}, paths...)
	if msg, err := harness.RunInTimeout(outDir, 10*time.Minute, nil, "java", args...); err != nil {
		t.Fatalf("java CollectorState: %v\n%s", err, msg)
	}
	same, differ, lines := 0, 0, 0
	for k, p := range paths {
		want, err := os.ReadFile(filepath.Join(outDir, fmt.Sprint(k)+".txt"))
		if err != nil {
			t.Fatal(err)
		}
		b := readBundle(t, p)
		// A bundle whose table is empty (Test.t, TestFromTerm.t,
		// TestPosition.t of bridgeCheck) has nothing to mark: an empty state
		// is legitimate there only.
		if len(want) == 0 && b.Table.GetEntryList().Length() > 0 {
			t.Errorf("%s: empty Java state for a table of %d symbols", p, b.Table.GetEntryList().Length())
		}
		got := backend.CollectorDump(b)
		lines += strings.Count(got, "\n")
		if got == string(want) {
			same++
			continue
		}
		differ++
		t.Errorf("%s: %s", p, firstLineDiff(got, string(want)))
	}
	t.Logf("état post-Collector : paquets %d, identiques %d, différents %d, lignes %d", len(paths), same, differ, lines)
	min := collectorMinLinesJ4
	if os.Getenv(harness.DumpsEnv) != "" {
		min = collectorMinLinesBridge
	}
	if lines < min {
		t.Errorf("%d state lines, want at least %d", lines, min)
	}
}
