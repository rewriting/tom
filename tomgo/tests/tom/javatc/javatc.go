// Package javatc resolves the Java reference toolchain for tests
// that compare the Go pipeline's output against `tom --intermediate`.
// Lives under tests/ so it stays out of the stable/ build tree.
package javatc

import (
	"os"
	"testing"

	tomparseq "tom/tomgo/tests/tom/parser/equiv"
)

// Resolve walks up from the calling test's working directory until
// it finds `tomgo/go.mod`, then tries to locate the JDK + reference
// jars. Returns ok=false (with a t.Skip already issued) when any
// prerequisite is missing, so the caller can simply early-return.
func Resolve(t *testing.T) (string, tomparseq.JavaToolchain, bool) {
	t.Helper()
	cwd, err := os.Getwd()
	if err != nil {
		t.Fatal(err)
	}
	repoRoot, err := tomparseq.FindRepoRoot(cwd)
	if err != nil {
		t.Skipf("repo root not found: %v", err)
		return "", tomparseq.JavaToolchain{}, false
	}
	tc, err := tomparseq.Resolve(repoRoot)
	if err != nil {
		t.Skipf("java toolchain: %v", err)
		return "", tomparseq.JavaToolchain{}, false
	}
	return repoRoot, tc, true
}
