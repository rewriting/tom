package tomparseq

import (
	"fmt"
	"os"
	"testing"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom/parser/parser"
)

// skeletonGoString builds, by hand, the same AST that the Java parser yields
// for testdata/parse/skeleton/scenario.t. The OriginTracking filename is left
// as "__INPUT__" so the cross-language harness can substitute the actual path
// at comparison time.
func skeletonGoString() string {
	tl1 := tomast.MakeTargetLanguageToCode(tomast.MakeTL(
		"public class Skeleton {\n  ",
		tomast.MakeTextPosition(1, 1),
		tomast.MakeTextPosition(2, 3),
	))
	decl := tomast.MakeDeclarationToCode(tomast.MakeTypeTermDecl(
		tomast.MakeName("Foo"),
		tomast.MakeConcDeclaration(),
		tomast.MakeOriginTracking(
			tomast.MakeName("Foo"),
			2,
			"__INPUT__",
		),
	))
	tl2 := tomast.MakeTargetLanguageToCode(tomast.MakeTL(
		"\n}\n",
		tomast.MakeTextPosition(4, 4),
		tomast.MakeTextPosition(5, 1),
	))
	return fmt.Sprintf("%v", tomast.MakeTom(tomast.MakeConcCode(tl1, decl, tl2)))
}

// TestSkeletonGoDump is a sanity check on hand-built Go AST construction.
// Always runs (no JDK required).
func TestSkeletonGoDump(t *testing.T) {
	got := skeletonGoString()
	if got == "" {
		t.Fatal("skeletonGoString returned empty")
	}
	t.Logf("Go-side skeleton AST:\n%s", got)
}

// TestSkeletonAgainstJava — hand-built Go AST ⇄ Java parser, sanity-checks the
// equivalence harness (Phase 4.C). Skipped without JDK / stable/dist/lib.
func TestSkeletonAgainstJava(t *testing.T) {
	tc, inputAbs := resolveOrSkip(t, "skeleton")
	if err := tc.AssertParityWithGo(inputAbs, skeletonGoString()); err != nil {
		t.Fatal(err)
	}
}

// TestGoParserAgainstJava is the real end-to-end equivalence test for the
// Go parser (Phase 4.D+): for each fixture under testdata/parse/<name>/, it
// parses with the Go parser and asserts that the resulting Code term is
// byte-identical to what the Java reference parser produces on the same file.
// Skipped without JDK / stable/dist/lib.
func TestGoParserAgainstJava(t *testing.T) {
	fixtures := []string{
		"skeleton",         // Phase 4.D MVP
		"op_noargs",        // Phase 4.E.1 — %op without slots
		"op_slots",         // Phase 4.E.2 — %op with slots
		"typeterm_extends", // Phase 4.E.3 — %typeterm X extends Y
		"oplist_oparray",   // Phase 4.E.4 — %oplist / %oparray
		"include_local",    // Phase 4.E.5 — %include
		"water_multi",      // Phase 4.F.0 — water with several visibles between islands (validates ANTLR-fidèle tokeniser)
		"match0b",          // Phase 4.F.1 — %match minimal (_ → { } with one subject)
		"match0c_named",    // Phase 4.F.2 — %match with named pattern variable (x → { })
		"match0d_appl",     // Phase 4.F.3 — %match with nullary pattern application (Foo() → { })
		"match0e_appl_args", // Phase 4.F.4 — %match with pattern application + sub-patterns (Foo(x, Bar()) → { })
		"match0f_multi",     // Phase 4.F.5 — multi-subject %match with N patterns (x, y → { } over %match(a, b))
		"match0g_rules",     // Phase 4.F.6 — multiple rules in one %match (_ → {} x → {})
		"match0h_body",      // Phase 4.F.7 — non-empty action body (_ → { doSomething(); })
		"match0i_explicit",  // Phase 4.F.8 — explicit-subject constraint (Foo() << t → { })
		"match0j_star",      // Phase 4.F.9 — variable-star patterns (x*, _* → { })
		"match0k_annot",     // Phase 4.F.10 — annotated patterns (Foo(x@a) → { })
		"match0l_anti",      // Phase 4.F.11 — anti-pattern (!Foo() → { })
		"match0m_or",        // Phase 4.F.12 — OR-pattern ((Foo|Bar)() → { })
		"match0n_bqappl",    // Phase 4.F.13 — backquote constant on << RHS (x << `Foo() → { })
		"match0o_bqbody",    // Phase 4.F.14 — backquote variable in action body (x → { `x })
		"match0p_bqbody_water", // Phase 4.F.15 — backquote with surrounding host-code water ({ return `x; })
		"match0q_bqappl_body",  // Phase 4.F.16 — backquote application in body ({ return `Foo(x); })
		"match0r_bq_multi",     // Phase 4.F.17 — multiple backquotes in body ({ return `Foo(`a, `b); })
	}
	for _, name := range fixtures {
		t.Run(name, func(t *testing.T) {
			tc, inputAbs := resolveOrSkip(t, name)
			src, err := os.ReadFile(inputAbs)
			if err != nil {
				t.Fatal(err)
			}
			// Pass the absolute path as the filename so the Go parser can
			// resolve %include directives relative to the input. The
			// AssertParityWithGo normaliser strips the absolute path back to
			// __INPUT__ / __DIR__ placeholders before comparison.
			code, err := tomparser.Parse(string(src), inputAbs)
			if err != nil {
				t.Fatalf("Go parser: %v", err)
			}
			goSide := fmt.Sprintf("%v", code)
			if err := tc.AssertParityWithGo(inputAbs, goSide); err != nil {
				t.Fatal(err)
			}
		})
	}
}

// resolveOrSkip locates the Java toolchain, the repo root and the absolute
// path of the given fixture's scenario.t. It skips the test cleanly when the
// toolchain is unavailable (no JDK or no stable/dist/lib).
func resolveOrSkip(t *testing.T, fixtureName string) (JavaToolchain, string) {
	t.Helper()
	cwd, err := os.Getwd()
	if err != nil {
		t.Fatal(err)
	}
	repoRoot, err := FindRepoRoot(cwd)
	if err != nil {
		t.Skipf("tomparseq: %v", err)
	}
	tc, err := Resolve(repoRoot)
	if err != nil {
		t.Skipf("tomparseq: %v", err)
	}
	inputAbs := repoRoot + "/tomgo/testdata/parse/" + fixtureName + "/scenario.t"
	if _, err := os.Stat(inputAbs); err != nil {
		t.Fatalf("fixture missing: %s (%v)", inputAbs, err)
	}
	return tc, inputAbs
}
