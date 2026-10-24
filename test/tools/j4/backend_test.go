package j4

import (
	"bytes"
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"testing"

	"tom/engine/backend"
	"tom/engine/platform"
	"tomtools/harness"
)

// f1Corpus are sources (under test/go) of family F1 (plan J4.1b):
// host code and type declarations only.
var f1Corpus = []string{"Test.t", "sl/TestPosition.t"}

// backendCompare runs the Go backend (backend.Generate) on a bridge bundle
// and compares its output with the file Java's backend wrote for the same
// compilation. detail names the first differing line, or the panic of the
// Go backend (a method not ported yet).
func backendCompare(t *testing.T, bundlePath, expectedPath string) (same bool, detail string) {
	t.Helper()
	want, err := os.ReadFile(expectedPath)
	if err != nil {
		t.Fatalf("expected output: %v", err)
	}
	b := readBundle(t, bundlePath)
	var out, diag bytes.Buffer
	ok := func() (ok bool) {
		defer func() {
			if v := recover(); v != nil {
				detail = fmt.Sprint("panic: ", v)
				ok = false
			}
		}()
		return backend.Generate(b, &out, platform.NewReporter(&diag, false))
	}()
	if detail != "" {
		return false, detail
	}
	if !ok {
		return false, "errors: " + diag.String()
	}
	if bytes.Equal(out.Bytes(), want) {
		return true, ""
	}
	return false, firstLineDiff(out.String(), string(want))
}

// TestBackendF1 compares the Go backend with Java's on the F1 sources.
func TestBackendF1(t *testing.T) {
	dist := harness.NewDist(t)
	paths, _ := bundlesOf(t, dist, f1Corpus)
	if len(paths) != len(f1Corpus) {
		t.Fatalf("%d bundles, want %d", len(paths), len(f1Corpus))
	}
	for _, p := range paths {
		if same, detail := backendCompare(t, p, strings.TrimSuffix(p, ".tfix.bridge")); !same {
			t.Errorf("%s: %s", filepath.Base(p), detail)
		}
	}
}
