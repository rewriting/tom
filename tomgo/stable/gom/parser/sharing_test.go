package gom

import (
	"path/filepath"
	"testing"
)

// TestParse_DeterministicSharing asserts that parsing the same file
// twice returns the exact same shared GomModule pointer — proof that
// the parser builds canonical (hash-consed) terms.
func TestParse_DeterministicSharing(t *testing.T) {
	corpus := []string{
		"Builtin.gom", "Dotted.gom", "Imported.gom", "Importing.gom",
		"Leaf.gom", "List.gom", "Minimal.gom", "Yang.gom", "Ying.gom",
		"fromterm/foo.gom",
	}
	for _, name := range corpus {
		name := name
		t.Run(name, func(t *testing.T) {
			path := filepath.Join("..", "..", "..", "tests", "testdata", "corpus", "gom-nohooks", name)
			a, err := ParseFile(path)
			if err != nil {
				t.Fatal(err)
			}
			b, err := ParseFile(path)
			if err != nil {
				t.Fatal(err)
			}
			if a != b {
				t.Fatalf("two parses of %s should return the same shared GomModule (%p vs %p)",
					path, a, b)
			}
		})
	}
}

// TestParse_ADTSharing does the same for the five ADT files, where
// cross-module references (e.g. Code:Code in Objects.gom) make the
// hash-consed sharing far more impressive — every `GomType("String")`,
// every `KindSort()`, every `MakeNone()` etc. is one canonical node.
func TestParse_ADTSharing(t *testing.T) {
	adt := []string{"Code.gom", "Gom.gom", "Objects.gom", "Rule.gom", "SymbolTable.gom"}
	for _, name := range adt {
		name := name
		t.Run(name, func(t *testing.T) {
			path := filepath.Join("..", "..", "..", "src", "gom", "adt", name)
			a, err := ParseFile(path)
			if err != nil {
				t.Fatal(err)
			}
			b, err := ParseFile(path)
			if err != nil {
				t.Fatal(err)
			}
			if a != b {
				t.Fatalf("two parses of %s should be the same shared GomModule", path)
			}
		})
	}
}
