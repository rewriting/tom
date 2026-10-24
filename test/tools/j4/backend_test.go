package j4

import (
	"bytes"
	"fmt"
	"os"
	"path/filepath"
	"regexp"
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

// backendCorpus are the sources whose bundles mage j4 runs through the Go
// backend: queryCorpus plus the F1 sources.
var backendCorpus = append(append([]string(nil), queryCorpus...), f1Corpus...)

// Floors of TestBackendCorpus: the number of bundles whose Go output is
// identical to Java's, raised by each family task of J4.1b (plan J4.1b,
// « Compteur P2 et familles »); never lowered. F1 is 4 of 5 for now:
// gom/TestTermgraph.t stops on buildGetHeadDecl (Task 5); F2 is 32 of 33 (sl/TestImportStrat.t stops on buildGetHeadDecl too).
const (
	minIdenticalJ4     = 7 // F1 2, F2 1, F3 2, F4 2
	minIdenticalBridge = 110 // F1 5, F2 33, F3 47, F4 25
)

// backendFamilies are the families of the P2 counter (plan J4.1b), in the
// order they are ported. A bundle belongs to the last family one of whose
// operators its code uses; declarations are not markers (the backend only
// emits those of used symbols).
var backendFamilies = []struct {
	name string
	ops  []string
}{
	{"F1 squelette", nil},
	{"F2 filtrage", []string{"CompiledMatch", "Let", "LetRef", "Assign", "If", "DoWhile", "WhileDo",
		"NamedBlock", "UnamedBlock", "Return", "EqualTerm", "EqualBQTerm", "IsFsym", "IsSort", "GetSlot",
		"BuildTerm", "FunctionCall", "Conditional", "Cast", "Bottom", "Negation", "And", "Or", "AddOne",
		"SubstractOne", "Substract", "GreaterThan", "LessThan", "GreaterOrEqualThan", "LessOrEqualThan",
		"Integer", "RawAction"}},
	{"F3 listes", []string{"BuildConsList", "BuildEmptyList", "BuildAppendList", "BuildConsArray",
		"BuildEmptyArray", "BuildAppendArray", "GetHead", "GetTail", "IsEmptyList", "IsEmptyArray",
		"GetSize", "GetElement", "GetSliceList", "GetSliceArray", "AssignArray"}},
	{"F4 stratégies", []string{"Class", "MethodDef", "FunctionDef", "IntrospectorClass"}},
}

var familyMarkers = func() []*regexp.Regexp {
	res := make([]*regexp.Regexp, len(backendFamilies))
	for k, f := range backendFamilies {
		if len(f.ops) > 0 {
			res[k] = regexp.MustCompile(`[(,](` + strings.Join(f.ops, "|") + `)\(`)
		}
	}
	return res
}()

// backendFamily is the index in backendFamilies of the family of a bundle
// whose code line (Code.String()) is code.
func backendFamily(code string) int {
	fam := 0
	for k, re := range familyMarkers {
		if re != nil && re.MatchString(code) {
			fam = k
		}
	}
	return fam
}

// expectedOutput is the file Java's backend wrote for the compilation of a
// bundle: <output>.generated next to it under TOMGO_DUMPS (mage
// bridgeCheck), else the output file itself, in the same directory.
func expectedOutput(bundlePath string) string {
	base := strings.TrimSuffix(bundlePath, ".tfix.bridge")
	if _, err := os.Stat(base + ".generated"); err == nil {
		return base + ".generated"
	}
	return base
}

// TestBackendCorpus is the in-process P2 counter (plan J4.1b): the Go
// backend on every bundle of backendCorpus (mage j4) or of T1 + smoke
// (mage bridgeCheck), compared byte for byte with Java's output, counted
// per family; it fails under the floors and on a vacuous corpus.
func TestBackendCorpus(t *testing.T) {
	dist := harness.NewDist(t)
	paths, floor := bundlesOf(t, dist, backendCorpus)
	min := minIdenticalJ4
	if os.Getenv(harness.DumpsEnv) != "" {
		min = minIdenticalBridge
	}
	if len(paths) < floor {
		t.Fatalf("%d bundles, want at least %d", len(paths), floor)
	}
	type count struct{ total, same int }
	counts := make([]count, len(backendFamilies))
	stops := map[string]int{} // first stub hit -> bundles
	same := 0
	for _, p := range paths {
		want := expectedOutput(p)
		if info, err := os.Stat(want); err != nil || info.Size() == 0 {
			t.Fatalf("%s: no Java output %s", p, want)
		}
		fam := backendFamily(fmt.Sprint(readBundle(t, p).Code))
		counts[fam].total++
		ok, detail := backendCompare(t, p, want)
		if ok {
			same++
			counts[fam].same++
			continue
		}
		if i := strings.Index(detail, "backend: "); i >= 0 {
			stops[strings.Fields(detail[i+len("backend: "):])[0]]++
		}
		t.Logf("%s [%s]: %.400s", filepath.Base(filepath.Dir(p))+"/"+filepath.Base(p), backendFamilies[fam].name, detail)
	}
	for k, f := range backendFamilies {
		t.Logf("P2 %s : paquets %d, identiques %d, différents %d", f.name, counts[k].total, counts[k].same, counts[k].total-counts[k].same)
	}
	t.Logf("P2 total : paquets %d, identiques %d, différents %d ; bouchons atteints %v", len(paths), same, len(paths)-same, stops)
	if same < min {
		t.Errorf("%d identical outputs, want at least %d", same, min)
	}
}

// TestBackendCompareDetectsDifference: a byte changed, a missing or an
// extra line in the expected output make backendCompare report a
// difference (the counter cannot pass vacuously).
func TestBackendCompareDetectsDifference(t *testing.T) {
	dist := harness.NewDist(t)
	paths, _ := bundlesOf(t, dist, []string{"Test.t"})
	want, err := os.ReadFile(expectedOutput(paths[0]))
	if err != nil {
		t.Fatal(err)
	}
	if same, detail := backendCompare(t, paths[0], expectedOutput(paths[0])); !same {
		t.Fatalf("unaltered output differs: %s", detail)
	}
	for name, alter := range map[string]func([]byte) []byte{
		"byte":  func(b []byte) []byte { c := bytes.Clone(b); c[len(c)/2] ^= 1; return c },
		"less":  func(b []byte) []byte { return b[:bytes.LastIndexByte(b[:len(b)-1], '\n')+1] },
		"more":  func(b []byte) []byte { return append(bytes.Clone(b), "x\n"...) },
		"empty": func([]byte) []byte { return nil },
	} {
		alt := filepath.Join(t.TempDir(), "alt.go")
		if err := os.WriteFile(alt, alter(want), 0o644); err != nil {
			t.Fatal(err)
		}
		if same, _ := backendCompare(t, paths[0], alt); same {
			t.Errorf("%s: difference not detected", name)
		}
	}
}
