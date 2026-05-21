package tom_test

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/stable/platform"
	tomparseq "tom/tomgo/tests/tom/parser/equiv"
	"tom/tomgo/tests/tom/parser/equiv/astcmp"
	tomparser "tom/tomgo/stable/tom/parser/parser"
	"tom/tomgo/stable/tom/starter"
	"tom/tomgo/stable/tom/parser"
	"tom/tomgo/stable/tom/transformer"
	"tom/tomgo/stable/tom/syntaxchecker"
	"tom/tomgo/stable/tom/desugarer"
	"tom/tomgo/stable/tom/typer"
	"tom/tomgo/tests/tom/javatc"
)

// TestCorpus_ParityWithJava walks every .t file under /tom/test/ and
// reports a progress dashboard: parse-ok count, java-ok count, and
// byte-parity-with-java count. The test is intentionally non-failing
// for now — the corpus is large and several plugins (Typer's full
// constraint solver, Desugarer's BQRecordAppl pass, parser's
// %typeterm/%op body parsing into the SymbolTable) are still being
// ported. As each gap closes the parity count rises; once it reaches
// the parse-ok count this assertion will be tightened to a true
// equality check.
//
// Skipped entirely without JDK / stable/dist/lib.
func TestCorpus_ParityWithJava(t *testing.T) {
	repoRoot, tc, ok := javatc.Resolve(t)
	if !ok {
		return
	}
	// Wire the standard Tom include directory so `%include { sl.tom }`
	// etc. resolve. Java's TomStreamManager.getImportList walks
	// `$TOM_HOME/share/tom/<lang>` first; match that order so the
	// OriginTracking paths in the AST come out identical.
	// Mirror Java's destdir for inline %gom expansion: the test
	// build directs everything to `test/gen`, regardless of the
	// fixture's depth under test/. Without this our parser defaults
	// to `<source-dir>/gen` which mismatches OriginTracking paths
	// for fixtures in subdirs (test/gom/, test/sl/, …).
	tomparser.GomDestDir = filepath.Join(repoRoot, "test", "gen")

	tomparser.IncludeSearchPath = []string{
		// Java's tom CLI configures user-import-list to include
		// destdir first (where Gom drops its generated .tom files).
		// Mirror that so `%include { bool/Bool.tom }` resolves to
		// the test-build output before falling back to the dev
		// share/tom mappings.
		filepath.Join(repoRoot, "test", "gen"),
		filepath.Join(repoRoot, "src", "dist", "share", "tom", "java"),
		filepath.Join(repoRoot, "src", "dist", "share", "tom"),
		filepath.Join(repoRoot, "utils", "eclipse-plugin", "plugin", "include", "java"),
		filepath.Join(repoRoot, "utils", "eclipse-plugin", "plugin", "include"),
		filepath.Join(repoRoot, "tomgo", "tests", "share", "tom-mappings"),
		filepath.Join(repoRoot, "tomgo", "tests", "share", "tom-mappings", "gom"),
	}

	corpusDir := filepath.Join(repoRoot, "test")
	var entries []string
	if err := filepath.Walk(corpusDir, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if !info.IsDir() && filepath.Ext(path) == ".t" {
			rel, _ := filepath.Rel(corpusDir, path)
			// Skip error/ — these are designed-to-fail and would
			// drag the dashboard down without signaling real work.
			if strings.HasPrefix(rel, "error/") {
				return nil
			}
			entries = append(entries, rel)
		}
		return nil
	}); err != nil {
		t.Fatal(err)
	}
	var (
		total      int
		parseOK    []string
		parseFail  []string
		javaFail   []string
		parityOK   []string
		parityDiff []string
	)
	for _, rel := range entries {
		total++
		name := rel
		input := filepath.Join(corpusDir, rel)
		src, err := os.ReadFile(input)
		if err != nil {
			continue
		}
		if _, err := tomparser.Parse(string(src), input); err != nil {
			parseFail = append(parseFail, name)
			continue
		}
		parseOK = append(parseOK, name)

		javaOut, err := tc.DumpJavaPhase(input, tomparseq.PhaseTyped)
		if err != nil {
			javaFail = append(javaFail, name)
			continue
		}
		goState, err := platform.New(
			starter.Plugin{},
			parser.Plugin{},
			transformer.Plugin{},
			syntaxchecker.Plugin{},
			desugarer.Plugin{},
			typer.Plugin{},
		).Run(platform.State{Filename: input})
		if err != nil {
			parityDiff = append(parityDiff, name)
			continue
		}
		goOut := fmt.Sprintf("%v", goState.Code)
		goOut = strings.ReplaceAll(goOut, input, "__INPUT__")
		goOut = strings.ReplaceAll(goOut, filepath.Dir(input), "__DIR__")
		// Compare via astcmp so equivalence-preserving Java-side
		// wrappers (Composite(CompositeBQTerm(t)) → t, …) get
		// folded out before the structural check.
		eq, err := astcmp.Equal(javaOut, goOut)
		if err != nil {
			parityDiff = append(parityDiff, name+" (parse-err: "+err.Error()+")")
			continue
		}
		if !eq {
			parityDiff = append(parityDiff, name)
			continue
		}
		parityOK = append(parityOK, name)
	}
	t.Logf("test/* corpus dashboard: total=%d parseOK=%d (parseFail=%d) javaFail=%d parityOK=%d parityDiff=%d",
		total, len(parseOK), len(parseFail), len(javaFail), len(parityOK), len(parityDiff))
	if len(parityOK) > 0 {
		t.Logf("  parity OK: %v", parityOK)
	}
	if len(parityDiff) > 0 {
		t.Logf("  parity diff: %v", parityDiff)
	}
	if len(parseFail) > 0 {
		t.Logf("  parse fail: %v", parseFail)
	}
}
