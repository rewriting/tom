// Package port holds the Tom+Go ports of test/*.t and checks them:
// T1 (generated Go is pinned by golden files and builds) and, from
// Task 8, T2 (same behaviour as the original under legacy/).
package port

import (
	"bufio"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"errors"
	"flag"
	"fmt"
	"io"
	"os"
	"os/exec"
	"path/filepath"
	"regexp"
	"sort"
	"strconv"
	"strings"
	"testing"

	gomparser "tom/gom/parser"
	"tomtest/harness"
)

var update = flag.Bool("update", false, "rewrite testdata/golden/*.go from the current compiler")

type portCase struct {
	Rel    string // path relative to test/ (and to test/go/)
	GoTest bool   // run with `go test` (JUnit-style) rather than `go run`
	// Unsupported lists the original tests (Go-capitalised name ->
	// reason) that the port keeps commented out under a
	// `// port: unsupported` note because the Go runtime cannot express
	// them. T2 exempts them from the legacy pass set, and fails if one
	// of them exists and passes in Go (stale entry).
	Unsupported map[string]string
	// FullyUnsupported marks a port all of whose tests are Unsupported:
	// T2 then only checks that what remains compiles and that no test
	// passes, instead of reporting a vacuous oracle; the dashboard status
	// is "non supporté".
	FullyUnsupported bool
	// Gom lists the standalone .gom files test/build.xml compiles with
	// Gom before the case, whose generated mappings the case includes.
	Gom []gomFile
	// Support lists other .t files (relative to test/) compiled with the
	// case: the original needs their classes (same Java package). In Go
	// each one is compiled into its own package, in the sub-directory
	// supportDir(rel) of the case's module, because two Tom-generated Go
	// files cannot share a package (each defines the tom_* helpers of the
	// mappings it includes).
	Support []string
	// TomArgs are the extra options test/build.xml gives tom (both
	// compilers) for an input of the case: the case itself or a support
	// file (key: path relative to test/).
	TomArgs map[string][]string
	// JavaSupport lists plain Java files (relative to test/) that the
	// original compiles with the case (copied next to the generated Java
	// for the oracle); the Go port holds their translation.
	JavaSupport []string
	// OracleDeps lists other originals (.t, relative to test/) that
	// legacy/ compiles with the case for the oracle only: the original's
	// host code names their classes (e.g. a main launching JUnit on
	// another test class), which test/build.xml finds because it compiles
	// the whole directory. The port drops that reference (no main), so the
	// Go side does not compile them.
	OracleDeps []string
	// Suite: the original is a JUnit Suite (@SuiteClasses) of the
	// JavaSupport classes. Its tests are their @Test methods, named
	// "Class.method" (two classes may declare the same method); the Go
	// test of the method m of class C is TestC_m.
	Suite bool
	// Regress: the original is a non-regression test of tom's
	// diagnostics (test/build.xml, target regress: tom must reject it;
	// test/regress/<name>.t.nrt lists the expected errors). Nothing is
	// run: the oracle is legacy/'s RegressFormatter log on the original
	// (oracle.Diagnostics), and the port, compiled by tom -go with the
	// same formatter, must be rejected with the same log. Dashboard
	// status "OK (diagnostics)".
	Regress bool
	// C: the original is a C program (test/cfib1.t, test/loulou.t, which
	// test/build.xml leaves out): the port is a Go main program, and the
	// oracle is the output and exit status of the original compiled by
	// legacy/'s tom -c -np (the default parser is tailored for Java) and
	// the C compiler cc (cOracle).
	C bool
}

// gomFile is a standalone .gom file of test/ (Rel, relative to test/);
// the port directory keeps an identical copy at test/go/<Rel>
// (TestGomCopiesIdentical), from which gomgo generates the Go side.
type gomFile struct {
	Rel string
	// JavaArgs are the options of test/build.xml's <gom.preset> call,
	// given to legacy/'s Gom for the oracle.
	JavaArgs []string
	// GoArgs are the options given to `gomgo gom-block`.
	GoArgs []string
	// Dir overrides the directory (relative to the Go module root, or to
	// the -I root for legacy/) that receives the module: Java Gom's
	// package path of the module below the case's own package, e.g.
	// b/u/i/l/t/i/n/builtin for gom.b.u.i.l.t.i.n.Builtin included from
	// package gom. Default: the lower-cased base name of Rel.
	Dir string
	// Deps lists the other .gom files of test/ the module imports (Java
	// Gom finds them next to Rel); gomgo reads the identical copies next
	// to the port's copy and generates them in the same Go package.
	Deps []string
	// PortedHooks: the hook bodies of Rel are host code (Java), ported to
	// Go in the copy; the rest of the copy (the signature, the hooks'
	// names, kinds and arguments) must still equal the original.
	PortedHooks bool
	// OracleOnly: compiled for the oracle only (by legacy/'s Gom), because
	// only OracleDeps include its mapping; the Go side skips it.
	OracleOnly bool
}

// testslGom is test/sl/testsl.gom, compiled by test/build.xml with
// package=sl, termpointer=true and withCongruenceStrategies=true.
// gomgo does not implement --termpointer (term pointers: the
// VarTerm/RefTerm/PathTerm/LabTerm operators and expand()); only
// sl/TestRef.t relies on them.
var testslGom = gomFile{
	Rel:      "sl/testsl.gom",
	JavaArgs: []string{"--package", "sl", "--termpointer", "--withCongruenceStrategies"},
	GoArgs:   []string{"--withCongruenceStrategies"},
}

// importingGom is test/gom/Importing.gom, which imports Imported.gom,
// which imports Leaf.gom.
var importingGom = gomFile{Rel: "gom/Importing.gom", Deps: []string{"gom/Imported.gom", "gom/Leaf.gom"}}

// builtinGom is test/gom/Builtin.gom, module gom.b.u.i.l.t.i.n.Builtin:
// Java Gom generates it in gom/b/u/i/l/t/i/n/builtin.
var builtinGom = gomFile{Rel: "gom/Builtin.gom", Dir: "b/u/i/l/t/i/n/builtin"}

// gomModuleDir is the directory, relative to the case's output
// directory (the Go module root, or the -I root for legacy/), that
// receives the code generated for g: Gom's <module>/<module>.tom layout,
// so that `%include { testsl/testsl.tom }` resolves against -I.
func gomModuleDir(g gomFile) string {
	if g.Dir != "" {
		return g.Dir
	}
	return strings.ToLower(strings.TrimSuffix(filepath.Base(g.Rel), ".gom"))
}

// gomPackage is the Go package name of the code generated for g: the
// last element of its directory.
func gomPackage(g gomFile) string { return filepath.Base(gomModuleDir(g)) }

// supportDir is the Go package directory (relative to the case's module
// root) of a support file: its lower-cased base name.
func supportDir(rel string) string {
	return strings.ToLower(strings.TrimSuffix(filepath.Base(rel), ".t"))
}

var cases = []portCase{
	{Rel: "Peano.t"},
	{Rel: "TestPeano.t", GoTest: true},
	{Rel: "Array.t"},
	{Rel: "TestArray.t", GoTest: true},
	{Rel: "TestList.t", GoTest: true},
	{Rel: "TestBackQuote.t", GoTest: true},
	{Rel: "TestVarVarStar.t", GoTest: true},
	{Rel: "TestNonVarSubjects.t", GoTest: true},
	{Rel: "TestSublists.t", GoTest: true},
	{Rel: "numeric/TestNumericConditions.t", GoTest: true},
	{Rel: "TestAlgebraic.t", GoTest: true},
	{Rel: "TestStrategy.t", GoTest: true},
	{Rel: "sl/TestEnvironment.t", GoTest: true},
	{Rel: "sl/TestPosition.t", GoTest: true},
	{Rel: "sl/TestOnceTopDownId.t", GoTest: true},
	{Rel: "sl/TestGetSubterm.t", GoTest: true},
	{Rel: "sl/TestImportStrat.t", GoTest: true, Gom: []gomFile{testslGom}, Support: []string{"sl/CommonStrat.t"}},
	{Rel: "sl/TestReduceAll.t", GoTest: true},
	{Rel: "sl/TestSLModes.t", GoTest: true, Gom: []gomFile{testslGom}},
	{Rel: "sl/TestSL.t", GoTest: true, Gom: []gomFile{testslGom},
		Support: []string{"sl/OldBehaviour.t", "sl/NewBehaviour.t", "sl/IntrospectorBehaviour.t"},
		TomArgs: map[string][]string{"sl/IntrospectorBehaviour.t": {"--genIntrospector"}}},
	{Rel: "sl/TestReflective.t", GoTest: true, Gom: []gomFile{testslGom}},
	{Rel: "sl/TestRef.t", GoTest: true, Gom: []gomFile{testslGom}, FullyUnsupported: true, Unsupported: map[string]string{
		"TestRef1": testRefUnsupported, "TestRef2": testRefUnsupported, "TestRef3": testRefUnsupported, "TestRef4": testRefUnsupported,
	}},
	{Rel: "sl/TestHandWrittenMapping.t", GoTest: true, TomArgs: map[string][]string{"sl/TestHandWrittenMapping.t": {"--genIntrospector"}}},
	{Rel: "gom/TestDotted.t", GoTest: true, Gom: []gomFile{{Rel: "gom/Dotted.gom"}}},
	{Rel: "gom/BenchConsGom.t", Gom: []gomFile{{Rel: "gom/List.gom"}}},
	{Rel: "gom/TestBool.t", GoTest: true, Gom: []gomFile{{Rel: "gom/Bool.gom"}}},
	{Rel: "gom/TestMultiHook.t", GoTest: true, Gom: []gomFile{{Rel: "gom/MultiHook.gom"}}},
	{Rel: "gom/TestTomHook.t", GoTest: true},
	{Rel: "gom/TestStratMapping.t", GoTest: true},
	{Rel: "gom/TestGomMultithread.t", GoTest: true},
	{Rel: "gom/TestGomWithoutMaximalSharing.t", GoTest: true},
	{Rel: "gom/TestImport.t", GoTest: true, Gom: []gomFile{importingGom}},
	{Rel: "gom/TestRuleBool.t", GoTest: true, Gom: []gomFile{{Rel: "gom/RuleBool.gom"}}},
	{Rel: "gom/TestRuleCond.t", GoTest: true},
	{Rel: "gom/TestPeanoRule.t", GoTest: true},
	{Rel: "gom/TestMakeEmpty.t", GoTest: true},
	{Rel: "gom/TestJavaHook.t", GoTest: true, Gom: []gomFile{{Rel: "gom/JavaHook.gom", PortedHooks: true}}},
	{Rel: "gom/TestFL.t", GoTest: true},
	{Rel: "gom/TestAU.t", GoTest: true},
	{Rel: "gom/TestACU.t", GoTest: true},
	{Rel: "gom/TestList.t", GoTest: true},
	{Rel: "gom/TestRuleList.t", GoTest: true, Gom: []gomFile{{Rel: "gom/RuleList.gom"}}},
	{Rel: "gom/TestRuleSortList.t", GoTest: true},
	{Rel: "gom/TestFrom.t", GoTest: true, Gom: []gomFile{importingGom}},
	{Rel: "gom/TestBuiltin.t", GoTest: true, Gom: []gomFile{builtinGom}},
	{Rel: "gom/TestFromMethods.t", GoTest: true, Gom: []gomFile{builtinGom}},
	{Rel: "gom/TestCollection.t", GoTest: true},
	{Rel: "gom/TestSortHook.t", GoTest: true},
	{Rel: "gom/TestTermgraph.t", GoTest: true, FullyUnsupported: true, Unsupported: unsupportedAll(termgraphUnsupported,
		"TestExpand", "TestExpandCycle", "TestGraphRules1", "TestGraphRules2", "TestGraphRules3", "TestGraphRules4",
		"TestGraphRules51", "TestGraphRules52", "TestGraphRules6", "TestGraphRules7", "TestGraphRules8", "TestGraphRules9")},
	{Rel: "gom/fromterm/TestFromTerm.t", GoTest: true, Suite: true, Gom: []gomFile{{Rel: "gom/fromterm/foo.gom"}},
		JavaSupport: []string{"gom/fromterm/TypeString.java", "gom/fromterm/TypeInt.java", "gom/fromterm/TypeBoolean.java",
			"gom/fromterm/TypeChar.java", "gom/fromterm/TypeLong.java", "gom/fromterm/TypeDouble.java", "gom/fromterm/TypeFloat.java",
			"gom/fromterm/FooConverter.java"}},
	{Rel: "gom/TestYingYang.t", GoTest: true, Gom: []gomFile{{Rel: "gom/Ying.gom", Deps: []string{"gom/Yang.gom"}}}},
	{Rel: "antipatterns/TestAntiPattern2.t", GoTest: true},
	{Rel: "antipatterns/TestAntiPattern3.t", GoTest: true},
	{Rel: "antipatterns/TestAntiPattern.t", GoTest: true},
	{Rel: "antipatterns/TestAntiPatternAssociative.t", GoTest: true},
	{Rel: "antipatterns/TestDoubleList.t", GoTest: true},
	{Rel: "andor/TestAndOr.t", GoTest: true},
	{Rel: "andor/TestAndOrConstraintOnly.t", GoTest: true},
	// The mains of the backquote tests launch JUnit on TestDefaultValue.
	{Rel: "backquote/TestBackquoteGom.t", GoTest: true, OracleDeps: []string{"backquote/TestDefaultValue.t"},
		Gom: []gomFile{{Rel: "backquote/term.gom", OracleOnly: true}}},
	{Rel: "backquote/TestDefaultValue.t", GoTest: true, Gom: []gomFile{{Rel: "backquote/term.gom"}}},
	{Rel: "rule/Array.t"},
	{Rel: "acmatching/TestAC.t", GoTest: true},
	{Rel: "acmatching/TestBool3.t", GoTest: true},
	{Rel: "backquote/TestBackquoteWithBracket.t", GoTest: true, OracleDeps: []string{"backquote/TestDefaultValue.t"},
		Gom: []gomFile{{Rel: "backquote/term.gom"}}},
	// test/regress: tom must reject each one, with the errors of legacy/
	// (Include.t and TestNonLinearity.t: legacyFails).
	{Rel: "regress/BadBackquote.t", Regress: true},
	{Rel: "regress/BadOp.t", Regress: true},
	{Rel: "regress/Disjunction1.t", Regress: true},
	{Rel: "regress/Disjunction2.t", Regress: true},
	{Rel: "regress/IncludeSl.t", Regress: true},
	{Rel: "regress/SuccessiveErrors.t", Regress: true},
	{Rel: "regress/UndeclaredExplicityType.t", Regress: true},
	{Rel: "regress/UnknownSymbol1.t", Regress: true},
	{Rel: "regress/UnknownSymbol2.t", Regress: true},
	{Rel: "regress/VariableStar.t", Regress: true},
	{Rel: "regress/WrongSymbol.t", Regress: true},
	// remaining files of test/
	{Rel: "Test.t", GoTest: true},
	{Rel: "TestOptimizer.t", GoTest: true, TomArgs: map[string][]string{"TestOptimizer.t": {"--optimize"}}},
	{Rel: "TestMatchInference.t", GoTest: true},
	{Rel: "TestArrayNonLinear.t", GoTest: true},
	{Rel: "TestArray2.t", GoTest: true},
	{Rel: "TestListNonLinear.t", GoTest: true},
	{Rel: "TestList2.t", GoTest: true},
	{Rel: "TestMatch.t", GoTest: true},
	{Rel: "TestBuiltin.t", GoTest: true},
	{Rel: "TestConstraintCombination.t", GoTest: true},
	{Rel: "TestConstraintCombinationWithSubtype.t", GoTest: true},
	{Rel: "TestReflectiveStrategy.t", GoTest: true},
	{Rel: "TomList.t"},
	{Rel: "cfib1.t", C: true},
	{Rel: "loulou.t", C: true},
}

// legacyFails lists the originals (relative to test/) that legacy/
// cannot compile: no oracle is possible, so they are not ported (dashboard
// status "échoue dans legacy"); TestLegacyFails checks that they still
// fail, as recorded. test/build.xml excludes rule/ from its compilation.
var legacyFails = map[string]legacyFailure{
	"rule/Peano.t":     ruleFailure(ruleLegacyFails),
	"rule/TestArray.t": ruleFailure(ruleLegacyFails),
	"rule/TestPeano.t": ruleFailure(ruleLegacyFails),
	"rule/TestWhen.t": {
		Reason:  ruleLegacyFails + "; its %match rules also use the obsolete `when` guards, which -np leaves as host code too (javac stops on the first one, before the %rule)",
		Default: failsIn{Stage: "tom", Output: []string{"java.lang.NullPointerException"}},
		NP:      failsIn{Stage: "javac", Output: []string{"%match(term t) {"}},
	},
	"rule/TomList.t": ruleFailure(ruleLegacyFails),
	// test/build.xml's regress target expects includedFileNotFound
	// (Include.t.nrt).
	"regress/Include.t": {
		Reason:  "includes the nonexistent titi.tom on purpose (non-regression test of the includedFileNotFound error), but legacy/'s tom crashes instead of reporting it (TomRuntimeException \"convert: strange term: Cst_IncludeFile\" after printing the TomIncludeException), with both parsers",
		Default: failsIn{Stage: "tom", Output: []string{"Included file 'titi.tom' not found", "convert: strange term: Cst_IncludeFile"}},
		NP:      failsIn{Stage: "tom", Output: []string{"Included file 'titi.tom' not found", "convert: strange term: Cst_IncludeFile"}},
	},
	// test/build.xml's regress target (with --newtyper, without effect)
	// expects incompatibleTypes, incompatibleTypes, cannotGuessMatchType,
	// incompatibleTypes (TestNonLinearity.t.nrt).
	// test/build.xml excludes them ("called with a special option": the
	// commented-out preset gives them --lazyType, which legacy/'s tom no
	// longer knows).
	"Record.t":     recordFailure,
	"TestRecord.t": recordFailure,
	"regress/TestNonLinearity.t": {
		Reason:  "non-regression test of typing errors, but legacy/'s typer crashes (TyperPlugin: Unhandled exception, findOriginTracking: not found: concOption()) with both parsers; a fix of the typer belongs to J4",
		Default: failsIn{Stage: "tom", Output: []string{"TyperPlugin: Unhandled exception", "findOriginTracking:  not found: concOption()"}},
		NP:      failsIn{Stage: "tom", Output: []string{"TyperPlugin: Unhandled exception", "findOriginTracking:  not found: concOption()"}},
	},
}

// legacyFailure records why and how legacy/ fails on an original.
type legacyFailure struct {
	Reason string
	// Default and NP: the failure with the default parser and with -np.
	Default, NP failsIn
}

// failsIn is a failure signature: the stage that fails ("tom": legacy/'s
// tom rejects or crashes; "javac": tom succeeds and javac rejects its
// output) and substrings of that stage's output.
type failsIn struct {
	Stage  string
	Output []string
}

// recordFailure is the failure of Record.t and TestRecord.t: the typer
// (the default, new one) rejects their %op hierarchy of Java classes
// (BinaryOperator of sort TomBinaryOperator used where TomExp is
// expected), with both parsers. With --oldtyper, tom accepts them but
// javac rejects its output (incompatible types: Exp cannot be converted
// to CstExp, UnaryOperator, BinaryOperator).
var recordFailure = legacyFailure{
	Reason:  "the typer rejects the %op hierarchy of Java classes (Incompatible types 'TomBinaryOperator' and 'TomExp' for symbol 'BinaryOperator', Cannot guess the type of 'e1'), with both parsers; with --oldtyper, javac rejects the generated Java (Exp cannot be converted to CstExp); test/build.xml excludes it (its commented-out --lazyType option no longer exists)",
	Default: failsIn{Stage: "tom", Output: []string{"Incompatible types 'TomBinaryOperator' and 'TomExp' for symbol 'BinaryOperator'"}},
	NP:      failsIn{Stage: "tom", Output: []string{"Incompatible types 'TomBinaryOperator' and 'TomExp' for symbol 'BinaryOperator'"}},
}

// ruleFailure is the failure of the %rule tests of rule/.
func ruleFailure(reason string) legacyFailure {
	return legacyFailure{
		Reason:  reason,
		Default: failsIn{Stage: "tom", Output: []string{"java.lang.NullPointerException"}},
		NP:      failsIn{Stage: "javac", Output: []string{"%rule {"}},
	}
}

// ruleLegacyFails is why legacy/ cannot compile the %rule tests of rule/.
const ruleLegacyFails = "obsolete %rule construct: legacy/'s tom crashes on it (NullPointerException in the CstBuilder of the default parser), and with -np it leaves %rule as host code, which javac rejects"

// termgraphUnsupported is why the tests of gom/TestTermgraph.t are not
// ported.
const termgraphUnsupported = "needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo"

// unsupportedAll maps each test name to the same reason.
func unsupportedAll(reason string, names ...string) map[string]string {
	m := map[string]string{}
	for _, n := range names {
		m[n] = reason
	}
	return m
}

// testRefUnsupported is why the tests of sl/TestRef.t are not ported.
const testRefUnsupported = "needs Gom's --termpointer (RefTerm/LabTerm/PathTerm/VarTerm, expand()) and the term-graph strategies of sl/graph.tom (DeRef, StrictDeRef, AllSeq), not implemented by gomgo and the Go sl library"

func base(c portCase) string { return strings.TrimSuffix(filepath.Base(c.Rel), ".t") }

func compileCase(t *testing.T, c portCase) string {
	t.Helper()
	dir := t.TempDir()
	if c.Regress {
		log, out, err := regressDiagnostics(t, c, dir)
		if err != nil {
			t.Fatalf("%v\n%s", err, out)
		}
		path := filepath.Join(dir, base(c)+".log")
		if err := os.WriteFile(path, []byte(log), 0o644); err != nil {
			t.Fatal(err)
		}
		checkGolden(t, c.Rel, path)
		return dir
	}
	gens, out, err := generateGo(t, c, dir)
	if err != nil {
		t.Fatalf("%v\n%s", err, out)
	}
	for rel, gen := range gens {
		checkGolden(t, rel, gen)
	}
	harness.SetupGoModule(t, dir)
	return dir
}

// generateGo fills dir (the future Go module root) as test/build.xml
// would for the Java original: gomgo first compiles the case's
// standalone .gom copies into dir/<module>/, then tom -go (with -I dir)
// compiles the support files into dir/<supportDir>/ and the case into
// dir. It returns the generated file of each .t (relative to test/).
func generateGo(t *testing.T, c portCase, dir string) (map[string]string, string, error) {
	t.Helper()
	dist := harness.NewDist(t)
	args := []string{"-go", "-np"}
	if len(c.Gom) > 0 {
		args = append(args, "-I", dir)
	}
	for _, g := range c.Gom {
		if g.OracleOnly {
			continue
		}
		src, err := filepath.Abs(g.Rel)
		if err != nil {
			t.Fatal(err)
		}
		mod := gomModuleDir(g)
		gargs := append(append([]string{"gom-block", "-o", filepath.Join(dir, mod), "--pkg", gomPackage(g)}, g.GoArgs...), src)
		if out, err := harness.RunIn(dir, nil, harness.GomgoBin(t), gargs...); err != nil {
			return nil, out, fmt.Errorf("gomgo %v failed: %v", gargs, err)
		}
	}
	gens := map[string]string{}
	for _, rel := range c.Support {
		src, err := filepath.Abs(rel)
		if err != nil {
			t.Fatal(err)
		}
		sub := filepath.Join(dir, supportDir(rel))
		if out, err := dist.Compile(t, src, sub, append(append([]string(nil), args...), c.TomArgs[rel]...)...); err != nil {
			return nil, out, fmt.Errorf("tom -go failed on %s: %v", rel, err)
		}
		gens[rel] = filepath.Join(sub, strings.TrimSuffix(filepath.Base(rel), ".t")+".go")
	}
	src, err := filepath.Abs(c.Rel)
	if err != nil {
		t.Fatal(err)
	}
	if out, err := dist.Compile(t, src, dir, append(args, c.TomArgs[c.Rel]...)...); err != nil {
		return nil, out, fmt.Errorf("tom -go failed: %v", err)
	}
	gen := filepath.Join(dir, base(c)+".go")
	if c.GoTest {
		if err := os.Rename(gen, filepath.Join(dir, base(c)+"_test.go")); err != nil {
			return nil, "", err
		}
		gen = filepath.Join(dir, base(c)+"_test.go")
	}
	gens[c.Rel] = gen
	return gens, "", nil
}

// regressFormatter are the tom options that log each error as
// "REGRESS: <message name>" into log (test/build.xml, regress.compile).
func regressFormatter(log string) []string {
	return []string{"-Dtom.platform.error.formatter=tom.platform.RegressFormatter", "-Dtom.platform.error.logfile=" + log}
}

// regressDiagnostics compiles the port of a Regress case with tom -go
// (-np) and the RegressFormatter, in dir, and returns the error log; it
// is an error if tom accepts the port.
func regressDiagnostics(t *testing.T, c portCase, dir string) (string, string, error) {
	t.Helper()
	src, err := filepath.Abs(c.Rel)
	if err != nil {
		t.Fatal(err)
	}
	log := filepath.Join(t.TempDir(), "regress.log")
	args := append(append([]string{"-go", "-np"}, regressFormatter(log)...), c.TomArgs[c.Rel]...)
	out, err := harness.NewDist(t).Compile(t, src, dir, args...)
	if err == nil {
		return "", out, fmt.Errorf("tom -go accepted %s, a non-regression test that it must reject", c.Rel)
	}
	data, rerr := os.ReadFile(log)
	if rerr != nil {
		return "", out, fmt.Errorf("tom -go failed without an error log: %v", rerr)
	}
	return string(data), out, nil
}

// checkGolden compares gen (generated Go, or the diagnostics log of a
// Regress case) with testdata/golden/<rel>.go (.log).
func checkGolden(t *testing.T, rel, gen string) {
	t.Helper()
	got, err := os.ReadFile(gen)
	if err != nil {
		t.Fatal(err)
	}
	golden := filepath.Join("testdata", "golden", strings.ReplaceAll(strings.TrimSuffix(rel, ".t"), "/", "_")+filepath.Ext(gen))
	if *update {
		if err := os.WriteFile(golden, got, 0o644); err != nil {
			t.Fatal(err)
		}
		return
	}
	want, err := os.ReadFile(golden)
	if err != nil {
		t.Fatalf("missing golden %s (run with -update): %v", golden, err)
	}
	if string(want) != string(got) {
		t.Fatalf("generated Go differs from %s (run with -update if intended)", golden)
	}
}

// runCase runs the compiled case; for go-test cases it returns the set
// of test names that passed.
func runCase(t *testing.T, c portCase, dir string) (map[string]bool, string, error) {
	t.Helper()
	if !c.GoTest {
		out, err := harness.RunIn(dir, nil, "go", "run", ".")
		return nil, out, err
	}
	out, err := harness.RunIn(dir, nil, "go", "test", "-json", "-count=1", ".")
	passed := map[string]bool{}
	sc := bufio.NewScanner(strings.NewReader(out))
	for sc.Scan() {
		var ev struct{ Action, Test string }
		if json.Unmarshal(sc.Bytes(), &ev) == nil && ev.Test != "" && ev.Action == "pass" {
			passed[ev.Test] = true
		}
	}
	return passed, out, err
}

func TestGoBackend(t *testing.T) {
	for _, c := range cases {
		c := c
		t.Run(c.Rel, func(t *testing.T) {
			dir := compileCase(t, c)
			if c.Regress {
				return // rejected by tom, nothing to run
			}
			if _, out, err := runCase(t, c, dir); err != nil {
				t.Fatalf("running %s failed: %v\n%s", c.Rel, err, out)
			}
		})
	}
}

// ---------------------------------------------------------------------
// T2: behaviour of each port vs the original run by legacy/.

var (
	updateOracle   = flag.Bool("update-oracle", false, "recompute testdata/oracle/*.json with legacy/")
	writeDashboard = flag.Bool("write-dashboard", false, "rewrite ../../doc/go/port-dashboard.md")
)

// oracle is what the original test/<Rel>, compiled by legacy/ and run
// under the JVM, did.
type oracle struct {
	// Passed lists the JUnit tests that passed, Go-capitalised
	// (testFoo -> TestFoo). A @Parameterized test counts as passed only
	// if every parameter row passed (JUnit names rows testFoo[i]; the Go
	// ports loop over the rows inside one TestFoo).
	Passed []string `json:"passed"`
	// Failed lists the JUnit tests that failed (same naming), for the
	// record: they are not required to pass in Go.
	Failed []string `json:"failed,omitempty"`
	Stdout string   `json:"stdout"` // stdout+stderr of a main program
	ExitOK bool     `json:"exitOK"`
	// Diagnostics (Regress cases): the error log of legacy/'s tom -np on
	// the original with tom.platform.RegressFormatter ("REGRESS: <message
	// name>" per error), -np being the parser of the Go target.
	Diagnostics string `json:"diagnostics,omitempty"`
	// DiagnosticsDefaultParser: the same log with the default parser, for
	// the record (not compared).
	DiagnosticsDefaultParser string `json:"diagnosticsDefaultParser,omitempty"`
	// SourceSHA256 is the hash of test/<Rel> the oracle was computed
	// from; a mismatch means the oracle is stale.
	SourceSHA256 string `json:"source_sha256"`
}

func oraclePath(c portCase) string {
	return filepath.Join("testdata", "oracle", strings.ReplaceAll(strings.TrimSuffix(c.Rel, ".t"), "/", "_")+".json")
}

// capitalise gives the Go name of a JUnit test: testFoo -> TestFoo, and
// testfoo -> TestFoo too, since go test only runs the functions whose
// name continues with an upper case (or a digit) after Test; a JUnit test
// whose name does not start with test gets the prefix: match4 ->
// TestMatch4.
func capitalise(name string) string {
	if name == "" {
		return name
	}
	if strings.HasPrefix(name, "test") && len(name) > 4 {
		return "Test" + strings.ToUpper(name[4:5]) + name[5:]
	}
	return "Test" + strings.ToUpper(name[:1]) + name[1:]
}

// junitListener runs JUnitCore on one class and reports each executed
// test on stdout as "@@T2 started|failed <method>" (a parameterised row
// is "testFoo[3]"). Reading the runner's own events, rather than
// JUnitCore's text report, sees exactly the tests JUnit ran.
const junitListener = `import org.junit.runner.*;
import org.junit.runner.notification.*;

public class T2JUnitListener extends RunListener {
  public void testStarted(Description d) { System.out.println("@@T2 started " + d.getMethodName() + " " + d.getClassName()); }
  public void testFailure(Failure f) { System.out.println("@@T2 failed " + f.getDescription().getMethodName() + " " + f.getDescription().getClassName()); }
  public void testAssumptionFailure(Failure f) { System.out.println("@@T2 failed " + f.getDescription().getMethodName() + " " + f.getDescription().getClassName()); }
  public static void main(String[] args) throws Exception {
    JUnitCore core = new JUnitCore();
    core.addListener(new T2JUnitListener());
    Result r = core.run(Class.forName(args[0]));
    System.exit(r.wasSuccessful() ? 0 : 1);
  }
}
`

// junitEvent matches a listener line; the parameter index of a
// @Parameterized row ("[3]") is stripped by the second group.
var junitEvent = regexp.MustCompile(`^@@T2 (started|failed) ([A-Za-z_$][\w$]*)(\[[^\]]*\])?(?: ([\w$.]+))?$`)

// runJUnit runs the compiled original under JUnit and fills o.Passed /
// o.Failed. A parameterised test passes only if all its rows pass.
func runJUnit(t *testing.T, c portCase, src, dir, cp, class string, o *oracle) {
	t.Helper()
	lsrc := filepath.Join(dir, "T2JUnitListener.java")
	if err := os.WriteFile(lsrc, []byte(junitListener), 0o644); err != nil {
		t.Fatal(err)
	}
	if out, err := harness.RunIn(dir, nil, "javac", "-nowarn", "-cp", cp, "-d", dir, lsrc); err != nil {
		t.Fatalf("javac listener failed: %v\n%s", err, out)
	}
	out, err := harness.RunIn(dir, nil, "java", "-cp", cp, "T2JUnitListener", class)
	o.ExitOK = err == nil
	started, failed := map[string]bool{}, map[string]bool{}
	var order []string
	for _, line := range strings.Split(out, "\n") {
		m := junitEvent.FindStringSubmatch(strings.TrimSpace(line))
		if m == nil {
			continue
		}
		if c.Suite {
			// Class.method, with the simple name of the class
			cls := m[4]
			if i := strings.LastIndex(cls, "."); i >= 0 {
				cls = cls[i+1:]
			}
			m[2] = cls + "." + m[2]
		}
		if m[1] == "failed" {
			failed[m[2]] = true
			continue
		}
		if !started[m[2]] {
			order = append(order, m[2])
		}
		started[m[2]] = true
	}
	for name := range failed {
		if !started[name] {
			t.Fatalf("JUnit failure outside a test (%s) in %s\n%s", name, c.Rel, out)
		}
	}
	if err != nil && len(failed) == 0 {
		t.Fatalf("JUnit failed without a test failure: %v\n%s", err, out)
	}
	// Cross-check: JUnit ran exactly the active @Test methods of the source.
	declared, err := declaredTests(c, src)
	if err != nil {
		t.Fatal(err)
	}
	if strings.Join(sortedCopy(declared), ",") != strings.Join(sortedCopy(order), ",") {
		t.Fatalf("JUnit ran %v but %s declares %v\n%s", order, c.Rel, declared, out)
	}
	for _, m := range declared {
		if failed[m] {
			o.Failed = append(o.Failed, goTestName(m))
		} else {
			o.Passed = append(o.Passed, goTestName(m))
		}
	}
	if len(declared) == 0 {
		t.Fatalf("no @Test found in %s", src)
	}
}

func sortedCopy(xs []string) []string {
	ys := append([]string(nil), xs...)
	sort.Strings(ys)
	return ys
}

// stripComments blanks Java /* */ and // comments (outside string and
// char literals), keeping line breaks so line structure survives.
func stripComments(src string) string {
	var b strings.Builder
	for i := 0; i < len(src); i++ {
		switch {
		case src[i] == '"' || src[i] == '\'':
			q := src[i]
			b.WriteByte(q)
			for i++; i < len(src) && src[i] != q && src[i] != '\n'; i++ {
				if src[i] == '\\' && i+1 < len(src) {
					b.WriteByte(src[i])
					i++
				}
				b.WriteByte(src[i])
			}
			if i < len(src) {
				b.WriteByte(src[i])
			}
		case strings.HasPrefix(src[i:], "//"):
			for i < len(src) && src[i] != '\n' {
				i++
			}
			if i < len(src) {
				b.WriteByte('\n')
			}
		case strings.HasPrefix(src[i:], "/*"):
			end := strings.Index(src[i+2:], "*/")
			if end < 0 {
				end = len(src) - i - 2
			}
			b.WriteString(strings.Repeat("\n", strings.Count(src[i:i+2+end], "\n")))
			i += 2 + end + 1
		default:
			b.WriteByte(src[i])
		}
	}
	return b.String()
}

// legacyOracle compiles and runs the ORIGINAL test/<Rel> with the
// human-written compiler (javac + JUnit or java main) and caches what
// it observed in testdata/oracle/.
func legacyOracle(t *testing.T, c portCase) oracle {
	t.Helper()
	if !*updateOracle {
		path := oraclePath(c)
		data, err := os.ReadFile(path)
		if err != nil {
			t.Fatalf("missing %s (run with -update-oracle)", path)
		}
		var o oracle
		if err := json.Unmarshal(data, &o); err != nil {
			t.Fatal(err)
		}
		srcs := sourcesOf(harness.RepoRoot(t), c)
		if err := oracleStale(c, o, srcs[0], srcs[1:]...); err != nil {
			t.Fatal(err)
		}
		return o
	}
	if c.Regress {
		return writeOracle(t, c, regressOracle(t, c))
	}
	if c.C {
		return writeOracle(t, c, cOracle(t, c))
	}
	legacy := harness.LegacyDist(t)
	root := harness.RepoRoot(t)
	src := filepath.Join(root, "test", c.Rel)
	dir := t.TempDir()
	// As test/build.xml: Gom first, then tom with -I towards the
	// generated mappings of the input's package, on the case and its
	// support files.
	var targs []string
	if len(c.Gom) > 0 {
		gen := t.TempDir()
		for _, g := range c.Gom {
			gargs := append(append([]string{"--destdir", gen}, g.JavaArgs...), filepath.Join(root, "test", g.Rel))
			if out, err := harness.RunIn(gen, []string{"TOM_HOME=" + legacy.Home}, filepath.Join(legacy.Home, "bin", "gom"), gargs...); err != nil {
				t.Fatalf("legacy gom %v failed: %v\n%s", gargs, err, out)
			}
		}
		inc := gen
		if pkg := javaPackage(t, src); pkg != "" {
			inc = filepath.Join(gen, strings.ReplaceAll(pkg, ".", string(filepath.Separator)))
		}
		targs = append([]string{"-I", inc}, targs...)
		// Gom's own Java sources belong to the program.
		if err := copyTree(gen, dir); err != nil {
			t.Fatal(err)
		}
	}
	for _, rel := range append(append(append([]string(nil), c.Support...), c.OracleDeps...), c.Rel) {
		if out, err := legacy.Compile(t, filepath.Join(root, "test", rel), dir, append(append([]string(nil), targs...), c.TomArgs[rel]...)...); err != nil {
			t.Fatalf("legacy tom failed on %s: %v\n%s", rel, err, out)
		}
	}
	for _, rel := range c.JavaSupport {
		data, err := os.ReadFile(filepath.Join(root, "test", rel))
		if err != nil {
			t.Fatal(err)
		}
		if err := os.WriteFile(filepath.Join(dir, filepath.Base(rel)), data, 0o644); err != nil {
			t.Fatal(err)
		}
	}
	cp := strings.Join([]string{
		filepath.Join(legacy.Home, "lib", "tom-runtime-full.jar"),
		filepath.Join(root, "legacy", "stable", "lib", "compiletime", "junit.jar"),
		dir,
	}, string(os.PathListSeparator))
	var javaFiles []string
	filepath.Walk(dir, func(p string, info os.FileInfo, err error) error {
		if err == nil && strings.HasSuffix(p, ".java") {
			javaFiles = append(javaFiles, p)
		}
		return err
	})
	if out, err := harness.RunIn(dir, nil, "javac", append([]string{"-nowarn", "-encoding", "UTF-8", "-cp", cp, "-d", dir}, javaFiles...)...); err != nil {
		t.Fatalf("javac failed: %v\n%s", err, out)
	}
	class := base(c)
	if pkg := javaPackage(t, src); pkg != "" {
		class = pkg + "." + class
	}
	var o oracle
	sum, err := sourceSHA256(sourcesOf(root, c)...)
	if err != nil {
		t.Fatal(err)
	}
	o.SourceSHA256 = sum
	if c.GoTest {
		runJUnit(t, c, src, dir, cp, class, &o)
	} else {
		out, err := harness.RunIn(dir, nil, "java", "-cp", cp, class)
		o.Stdout, o.ExitOK = out, err == nil
	}
	return writeOracle(t, c, o)
}

// writeOracle stores o as the oracle of c and returns it.
func writeOracle(t *testing.T, c portCase, o oracle) oracle {
	t.Helper()
	path := oraclePath(c)
	data, _ := json.MarshalIndent(o, "", "  ")
	if err := os.MkdirAll(filepath.Dir(path), 0o755); err != nil {
		t.Fatal(err)
	}
	if err := os.WriteFile(path, append(data, '\n'), 0o644); err != nil {
		t.Fatal(err)
	}
	return o
}

// cOracle compiles the C original of a C case with legacy/'s tom -c -np
// and cc, runs it and records its output and exit status. Since C
// compilers reject the int/pointer conversions of the C mappings by
// default nowadays (clang 15+), cc runs with -Wno-int-conversion (and -w:
// the warnings are not the test's business).
func cOracle(t *testing.T, c portCase) oracle {
	t.Helper()
	cc, err := exec.LookPath("cc")
	if err != nil {
		t.Skip("cc not found in PATH")
	}
	legacy := harness.LegacyDist(t)
	root := harness.RepoRoot(t)
	dir := t.TempDir()
	if out, err := legacy.Compile(t, filepath.Join(root, "test", c.Rel), dir, append([]string{"-c", "-np"}, c.TomArgs[c.Rel]...)...); err != nil {
		t.Fatalf("legacy tom -c failed on %s: %v\n%s", c.Rel, err, out)
	}
	cFiles, _ := filepath.Glob(filepath.Join(dir, "*.c"))
	if len(cFiles) == 0 {
		t.Fatalf("legacy tom -c produced no C file for %s", c.Rel)
	}
	prog := filepath.Join(dir, base(c))
	if out, err := harness.RunIn(dir, nil, cc, append([]string{"-w", "-Wno-int-conversion", "-o", prog}, cFiles...)...); err != nil {
		t.Fatalf("cc failed: %v\n%s", err, out)
	}
	var o oracle
	sum, err := sourceSHA256(sourcesOf(root, c)...)
	if err != nil {
		t.Fatal(err)
	}
	o.SourceSHA256 = sum
	out, err := harness.RunIn(dir, nil, prog)
	o.Stdout, o.ExitOK = out, err == nil
	return o
}

// regressOracle compiles the original of a Regress case with legacy/'s
// tom and the RegressFormatter, with -np (the parser of the Go target)
// and with the default parser: tom must reject it, with a non-empty log.
func regressOracle(t *testing.T, c portCase) oracle {
	t.Helper()
	legacy := harness.LegacyDist(t)
	root := harness.RepoRoot(t)
	src := filepath.Join(root, "test", c.Rel)
	var o oracle
	sum, err := sourceSHA256(sourcesOf(root, c)...)
	if err != nil {
		t.Fatal(err)
	}
	o.SourceSHA256 = sum
	for _, np := range []bool{true, false} {
		log := filepath.Join(t.TempDir(), "regress.log")
		args := regressFormatter(log)
		if np {
			args = append(args, "-np")
		}
		out, err := legacy.Compile(t, src, t.TempDir(), append(args, c.TomArgs[c.Rel]...)...)
		data, _ := os.ReadFile(log)
		if np {
			if err == nil || len(data) == 0 {
				t.Fatalf("legacy tom -np does not reject %s with an error log (err %v): not a Regress case\n%s", c.Rel, err, out)
			}
			o.Diagnostics = string(data)
		} else {
			o.DiagnosticsDefaultParser = string(data)
		}
	}
	return o
}

// declaredTests lists the JUnit tests of the original src of c: its
// active @Test methods, or, for a Suite, the "Class.method" of the
// @Test methods of its JavaSupport classes.
func declaredTests(c portCase, src string) ([]string, error) {
	if !c.Suite {
		data, err := os.ReadFile(src)
		if err != nil {
			return nil, err
		}
		return junitTestNames(string(data)), nil
	}
	testDir := strings.TrimSuffix(src, filepath.FromSlash(c.Rel))
	var names []string
	for _, rel := range c.JavaSupport {
		data, err := os.ReadFile(filepath.Join(testDir, rel))
		if err != nil {
			return nil, err
		}
		cls := strings.TrimSuffix(filepath.Base(rel), ".java")
		for _, m := range junitTestNames(string(data)) {
			names = append(names, cls+"."+m)
		}
	}
	return names, nil
}

// goTestName is the Go test of a JUnit test: testFoo -> TestFoo, and
// Class.method (a Suite) -> TestClass_method.
func goTestName(name string) string {
	if cls, m, ok := strings.Cut(name, "."); ok {
		return "Test" + cls + "_" + m
	}
	return capitalise(name)
}

// junitTests lists the active @Test methods of a JUnit .t source;
// commented-out tests (// or /* */) are skipped.
func junitTests(t *testing.T, src string) []string {
	t.Helper()
	data, err := os.ReadFile(src)
	if err != nil {
		t.Fatal(err)
	}
	return junitTestNames(string(data))
}

func junitTestNames(source string) []string {
	var names []string
	lines := strings.Split(stripComments(source), "\n")
	for i, line := range lines {
		s := strings.TrimSpace(line)
		// a JUnit Theories runner runs the @Theory methods as tests; the
		// annotation may be qualified (@org.junit.Test, test/Test.t)
		for _, pkg := range []string{"@org.junit.experimental.theories.", "@org.junit."} {
			if strings.HasPrefix(s, pkg) {
				s = "@" + strings.TrimPrefix(s, pkg)
				break
			}
		}
		if s != "@Test" && !strings.HasPrefix(s, "@Test(") && s != "@Theory" && !strings.HasPrefix(s, "@Theory(") {
			continue
		}
		for _, next := range lines[i+1:] {
			if j := strings.Index(next, "void "); j >= 0 {
				rest := next[j+5:]
				if k := strings.Index(rest, "("); k > 0 {
					names = append(names, strings.TrimSpace(rest[:k]))
				}
				break
			}
		}
	}
	return names
}

func javaPackage(t *testing.T, src string) string {
	t.Helper()
	data, _ := os.ReadFile(src)
	for _, line := range strings.Split(string(data), "\n") {
		if s := strings.TrimSpace(line); strings.HasPrefix(s, "package ") {
			return strings.TrimSuffix(strings.TrimSpace(strings.TrimPrefix(s, "package ")), ";")
		}
	}
	return ""
}

// sourceSHA256 hashes the concatenation of the given files (for a
// single file, the hash of that file).
func sourceSHA256(paths ...string) (string, error) {
	h := sha256.New()
	for _, path := range paths {
		f, err := os.Open(path)
		if err != nil {
			return "", err
		}
		_, err = io.Copy(h, f)
		f.Close()
		if err != nil {
			return "", err
		}
	}
	return hex.EncodeToString(h.Sum(nil)), nil
}

// sourcesOf lists the original files whose content determines the
// oracle of c: test/<Rel>, then its support files and .gom files.
func sourcesOf(root string, c portCase) []string {
	paths := []string{filepath.Join(root, "test", c.Rel)}
	for _, rel := range append(append(append([]string(nil), c.Support...), c.JavaSupport...), c.OracleDeps...) {
		paths = append(paths, filepath.Join(root, "test", rel))
	}
	for _, g := range c.Gom {
		for _, rel := range append([]string{g.Rel}, g.Deps...) {
			paths = append(paths, filepath.Join(root, "test", rel))
		}
	}
	return paths
}

// copyTree copies the regular files of src into dst (same layout).
func copyTree(src, dst string) error {
	return filepath.Walk(src, func(p string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() {
			return err
		}
		rel, _ := filepath.Rel(src, p)
		data, err := os.ReadFile(p)
		if err != nil {
			return err
		}
		if err := os.MkdirAll(filepath.Join(dst, filepath.Dir(rel)), 0o755); err != nil {
			return err
		}
		return os.WriteFile(filepath.Join(dst, rel), data, 0o644)
	})
}

// oracleStale checks, without Java, that a cached oracle still matches
// the original source src.
func oracleStale(c portCase, o oracle, src string, extra ...string) error {
	sum, err := sourceSHA256(append([]string{src}, extra...)...)
	if err != nil {
		return err
	}
	if sum != o.SourceSHA256 {
		return fmt.Errorf("oracle stale: %s (or its support/.gom files) changed since the oracle was computed (run with -update-oracle)", src)
	}
	if !c.GoTest {
		return nil
	}
	data, err := os.ReadFile(src)
	if err != nil {
		return err
	}
	_ = data
	names, err := declaredTests(c, src)
	if err != nil {
		return err
	}
	var declared []string
	for _, m := range names {
		declared = append(declared, goTestName(m))
	}
	recorded := append(append([]string(nil), o.Passed...), o.Failed...)
	if strings.Join(sortedCopy(declared), ",") != strings.Join(sortedCopy(recorded), ",") {
		return fmt.Errorf("oracle stale: %s declares %v but the oracle records %v (run with -update-oracle)",
			src, sortedCopy(declared), sortedCopy(recorded))
	}
	return nil
}

// compareRegress returns the T2 divergences of the diagnostics of a
// Regress port against the legacy oracle.
func compareRegress(want oracle, log string) []string {
	if want.Diagnostics == "" {
		return []string{"vacuous oracle: no legacy diagnostics recorded"}
	}
	if log != want.Diagnostics {
		return []string{"diagnostics differ\nlegacy:\n" + want.Diagnostics + "\ngo:\n" + log}
	}
	return nil
}

// compareWithOracle returns the T2 divergences of one run against the
// legacy oracle (empty when the port behaves like the original).
func compareWithOracle(c portCase, want oracle, passed map[string]bool, out string, runErr error) []string {
	var diffs []string
	if !c.GoTest {
		if (runErr == nil) != want.ExitOK || out != want.Stdout {
			diffs = append(diffs, "main program differs\nlegacy (ok="+strconv.FormatBool(want.ExitOK)+"):\n"+want.Stdout+
				"\ngo (ok="+strconv.FormatBool(runErr == nil)+"):\n"+out)
		}
		return diffs
	}
	legacyPassed := map[string]bool{}
	for _, name := range want.Passed {
		legacyPassed[name] = true
	}
	topPassed := map[string]bool{}
	for name := range passed {
		if !strings.Contains(name, "/") { // subtests are the port's business
			topPassed[name] = true
		}
	}
	required := 0
	for _, name := range want.Passed {
		if _, unsupported := c.Unsupported[name]; !unsupported {
			required++
		}
	}
	switch {
	case required == 0 && !c.FullyUnsupported:
		diffs = append(diffs, "vacuous oracle: no legacy-passing test remains required once Unsupported is removed")
	case required > 0 && c.FullyUnsupported:
		diffs = append(diffs, "FullyUnsupported, but "+strconv.Itoa(required)+" legacy-passing test(s) are not Unsupported")
	}
	if runErr != nil && len(topPassed) == 0 {
		diffs = append(diffs, "go test failed and no Go test passed ("+runErr.Error()+")")
	}
	for _, name := range want.Passed {
		if _, unsupported := c.Unsupported[name]; unsupported {
			continue
		}
		if !topPassed[name] {
			diffs = append(diffs, name+" passes under legacy/ but not in Go")
		}
	}
	for name := range c.Unsupported {
		if topPassed[name] {
			diffs = append(diffs, name+" is listed as unsupported but passes in Go (stale Unsupported entry)")
		}
		if !legacyPassed[name] {
			diffs = append(diffs, name+" is listed as unsupported but is not a passing test of the original")
		}
	}
	for name := range topPassed {
		if !legacyPassed[name] {
			diffs = append(diffs, name+" passes in Go but does not pass (or does not exist) under legacy/")
		}
	}
	sort.Strings(diffs)
	return diffs
}

func TestSameBehaviourAsLegacy(t *testing.T) {
	for _, c := range cases {
		c := c
		t.Run(c.Rel, func(t *testing.T) {
			want := legacyOracle(t, c)
			if c.Regress {
				log, out, err := regressDiagnostics(t, c, t.TempDir())
				if err != nil {
					t.Fatalf("%v\n%s", err, out)
				}
				for _, d := range compareRegress(want, log) {
					t.Error(d)
				}
				return
			}
			dir := compileCase(t, c)
			passed, out, err := runCase(t, c, dir)
			diffs := compareWithOracle(c, want, passed, out, err)
			for _, d := range diffs {
				t.Error(d)
			}
			if len(diffs) > 0 && c.GoTest {
				t.Logf("go test output:\n%s", goTestOutput(out))
				if vout, verr := harness.RunIn(dir, nil, "go", "vet", "./..."); verr != nil {
					t.Logf("go vet ./... (build diagnostics):\n%s", vout)
				}
			}
		})
	}
}

// goTestOutput extracts the human-readable output from `go test -json`.
func goTestOutput(jsonOut string) string {
	var b strings.Builder
	sc := bufio.NewScanner(strings.NewReader(jsonOut))
	for sc.Scan() {
		var ev struct{ Action, Output string }
		if json.Unmarshal(sc.Bytes(), &ev) != nil {
			b.WriteString(sc.Text() + "\n")
		} else if ev.Action == "output" {
			b.WriteString(ev.Output)
		}
	}
	return b.String()
}

// TestUnsupportedMarkers keeps the Unsupported maps in step with the
// `// port: unsupported` notes in the ports.
func TestUnsupportedMarkers(t *testing.T) {
	for _, c := range cases {
		data, err := os.ReadFile(c.Rel)
		if err != nil {
			t.Fatal(err)
		}
		marked := strings.Contains(string(data), "// port: unsupported")
		if marked != (len(c.Unsupported) > 0) {
			t.Errorf("%s: `// port: unsupported` note present=%v but Unsupported has %d entries", c.Rel, marked, len(c.Unsupported))
		}
		for name := range c.Unsupported {
			lower := strings.ToLower(name[:1]) + name[1:]
			if !strings.Contains(string(data), lower) {
				t.Errorf("%s: unsupported test %s (%s) not mentioned in the port", c.Rel, name, lower)
			}
		}
	}
}

// TestDashboard classifies every test/**/*.t (outside error/) and, with
// -write-dashboard, rewrites doc/go/port-dashboard.md.
func TestDashboard(t *testing.T) {
	if !*writeDashboard {
		t.Skip("run with -write-dashboard")
	}
	root := harness.RepoRoot(t)
	ported := map[string]portCase{}
	supportOf := map[string]string{} // support file -> its case
	for _, c := range cases {
		ported[c.Rel] = c
		for _, rel := range c.Support {
			supportOf[rel] = c.Rel
		}
	}
	var rows []string
	counts := map[string]int{}
	filepath.Walk(filepath.Join(root, "test"), func(p string, info os.FileInfo, err error) error {
		// test/go holds the ports themselves, not original cases.
		if err == nil && info.IsDir() && p == filepath.Join(root, "test", "go") {
			return filepath.SkipDir
		}
		if err != nil || info.IsDir() || !strings.HasSuffix(p, ".t") {
			return err
		}
		rel, _ := filepath.Rel(filepath.Join(root, "test"), p)
		rel = filepath.ToSlash(rel)
		if strings.HasPrefix(rel, "error/") {
			return nil
		}
		status, note := "non porté", ""
		if f, ok := legacyFails[rel]; ok {
			status, note = "échoue dans legacy", f.Reason
		} else if main, ok := supportOf[rel]; ok {
			// A support file has no test of its own: its classes are
			// exercised, in Java and in Go, by the case that uses them.
			status, note = "support", "compilé avec `"+main+"`, couvert par son statut"
		} else if c, ok := ported[rel]; ok {
			status = dashboardStatus(t, c)
			if c.Regress {
				note = regressNote(root, c)
			}
			var names []string
			for name := range c.Unsupported {
				names = append(names, name)
			}
			sort.Strings(names)
			for _, name := range names {
				if note != "" {
					note += " ; "
				}
				note += "`" + name + "` non supporté : " + c.Unsupported[name]
			}
		}
		counts[status]++
		rows = append(rows, "| `"+rel+"` | "+status+" | "+note+" |")
		return nil
	})
	var b strings.Builder
	b.WriteString("# Tableau de bord du portage Tom+Go (T2)\n\n")
	b.WriteString("Généré par `mage portGo` (`go test ./ -args -write-dashboard` dans `test/go`).\n")
	b.WriteString("Chaque `test/**/*.t` hors `error/` ; un cas porté est OK quand son port\n")
	b.WriteString("Tom+Go, compilé par `tom -go`, se comporte comme l'original compilé par\n")
	b.WriteString("`legacy/` (mêmes tests JUnit réussis, ou même sortie et même statut).\n")
	b.WriteString("Un fichier « support » n'a pas de test propre : ses classes sont compilées\n")
	b.WriteString("avec le cas qui les utilise (comme `test/build.xml` compile tout le\n")
	b.WriteString("répertoire), et c'est le statut de ce cas qui les valide.\n")
	b.WriteString("Un original « échoue dans legacy » ne se compile pas avec `legacy/` :\n")
	b.WriteString("pas d'oracle possible, il n'est pas porté (`TestLegacyFails` le vérifie).\n")
	b.WriteString("Un cas « OK (diagnostics) » est un test de non-régression des diagnostics\n")
	b.WriteString("(`test/regress/`) : `tom -go` rejette le port avec les mêmes erreurs que\n")
	b.WriteString("`legacy/` sur l'original ; rien n'est exécuté (la couverture T3 ne le\n")
	b.WriteString("compte pas).\n\n")
	b.WriteString("- total : " + strconv.Itoa(len(rows)) + "\n")
	for _, s := range []string{"OK", "OK (diagnostics)", "divergence", "échec go test", "échec go build", "échec tom", "oracle invalide", "non supporté", "support", "échoue dans legacy", "non porté"} {
		b.WriteString("- " + s + " : " + strconv.Itoa(counts[s]) + "\n")
	}
	b.WriteString("\n| Cas | Statut | Notes |\n|---|---|---|\n" + strings.Join(rows, "\n") + "\n")
	if err := os.WriteFile(filepath.Join(root, "doc", "go", "port-dashboard.md"), []byte(b.String()), 0o644); err != nil {
		t.Fatal(err)
	}
}

// regressNote compares, for the dashboard, the diagnostics of legacy/
// recorded in the oracle of a Regress case with the errors that
// test/build.xml's regress target expects (test/<Rel>.nrt).
func regressNote(root string, c portCase) string {
	var o oracle
	data, err := os.ReadFile(oraclePath(c))
	if err != nil || json.Unmarshal(data, &o) != nil {
		return ""
	}
	nrt, err := os.ReadFile(filepath.Join(root, "test", c.Rel+".nrt"))
	if err != nil {
		return "pas de `.nrt`"
	}
	names := func(log string) string {
		return strings.Join(strings.Fields(strings.ReplaceAll(log, "REGRESS:", "")), ", ")
	}
	note := "erreurs : " + names(o.Diagnostics)
	if o.Diagnostics != string(nrt) {
		note += " ; le `.nrt` attend " + names(string(nrt)) + " (diffère déjà dans `legacy/`)"
	}
	if o.DiagnosticsDefaultParser != o.Diagnostics {
		d := names(o.DiagnosticsDefaultParser)
		if d == "" {
			d = "aucune"
		}
		note += " ; parser par défaut de `legacy/` : " + d
	}
	return note
}

// dashboardStatus re-runs one case and classifies it without failing
// the dashboard test.
func dashboardStatus(t *testing.T, c portCase) string {
	t.Helper()
	// Subtests are allowed to fail: their outcome is the status. The
	// oracle is loaded first so that a missing or stale oracle is not
	// mistaken for a Tom failure.
	var want oracle
	if !t.Run("oracle/"+c.Rel, func(t *testing.T) { want = legacyOracle(t, c) }) {
		return "oracle invalide"
	}
	status := "échec tom"
	if c.Regress {
		t.Run("status/"+c.Rel, func(t *testing.T) {
			log, _, err := regressDiagnostics(t, c, t.TempDir())
			switch {
			case err != nil:
				status = "échec tom" // accepted, or no log
			case len(compareRegress(want, log)) > 0:
				status = "divergence"
			default:
				status = "OK (diagnostics)"
			}
		})
		return status
	}
	t.Run("status/"+c.Rel, func(t *testing.T) {
		dir := t.TempDir()
		if _, _, err := generateGo(t, c, dir); err != nil {
			return
		}
		harness.SetupGoModule(t, dir)
		buildArgs := []string{"build", "."}
		if c.GoTest {
			buildArgs = []string{"test", "-c", "-o", os.DevNull, "."}
		}
		if _, err := harness.RunIn(dir, nil, "go", buildArgs...); err != nil {
			status = "échec go build"
			return
		}
		passed, out, err := runCase(t, c, dir)
		switch {
		case c.GoTest && err != nil && len(passed) == 0:
			status = "échec go test"
		case len(compareWithOracle(c, want, passed, out, err)) > 0:
			status = "divergence"
		case c.FullyUnsupported:
			status = "non supporté"
		default:
			status = "OK"
		}
	})
	return status
}

// TestCompareWithOracle pins the T2 rules on synthetic runs.
func TestCompareWithOracle(t *testing.T) {
	c := portCase{Rel: "X.t", GoTest: true, Unsupported: map[string]string{"TestU": "why"}}
	want := oracle{Passed: []string{"TestA", "TestU"}, Failed: []string{"TestF"}}
	set := func(xs ...string) map[string]bool {
		m := map[string]bool{}
		for _, x := range xs {
			m[x] = true
		}
		return m
	}
	for _, tc := range []struct {
		passed map[string]bool
		ndiff  int
	}{
		{set("TestA"), 0},
		{set("TestA", "TestA/row1"), 0},
		{set(), 1},                   // TestA missing
		{set("TestA", "TestU"), 1},   // stale unsupported entry
		{set("TestA", "TestF"), 1},   // fails under legacy
		{set("TestA", "TestNew"), 1}, // absent from the original
	} {
		if got := compareWithOracle(c, want, tc.passed, "", nil); len(got) != tc.ndiff {
			t.Errorf("passed=%v: got %v, want %d divergence(s)", tc.passed, got, tc.ndiff)
		}
	}
	main := portCase{Rel: "M.t"}
	if d := compareWithOracle(main, oracle{Stdout: "x\n", ExitOK: true}, nil, "x\n", nil); len(d) != 0 {
		t.Errorf("identical main: %v", d)
	}
	if d := compareWithOracle(main, oracle{Stdout: "x\n", ExitOK: true}, nil, "x", nil); len(d) != 1 {
		t.Errorf("stdout differs: %v", d)
	}
	if got := junitTests(t, writeTemp(t, "/* @Test\npublic void testA() {} */\n// @Test\n@Test\npublic void testB() { String s = \"/*\"; }\n@Test\npublic void testC() {}\n")); strings.Join(got, ",") != "testB,testC" {
		t.Errorf("junitTests = %v", got)
	}
	if got := junitTestNames("@Theory\npublic void testT(W w) {}\n@DataPoints\npublic static W[] data() {}\n"); strings.Join(got, ",") != "testT" {
		t.Errorf("junitTestNames(@Theory) = %v", got)
	}
	if got := junitTestNames("@org.junit.Test\npublic void test1() {}\n"); strings.Join(got, ",") != "test1" {
		t.Errorf("junitTestNames(@org.junit.Test) = %v", got)
	}
}

func writeTemp(t *testing.T, content string) string {
	p := filepath.Join(t.TempDir(), "x.t")
	if err := os.WriteFile(p, []byte(content), 0o644); err != nil {
		t.Fatal(err)
	}
	return p
}

// TestOracleGuards pins the vacuity and staleness guards (fix round 1).
func TestOracleGuards(t *testing.T) {
	hasDiff := func(diffs []string, sub string) bool {
		for _, d := range diffs {
			if strings.Contains(d, sub) {
				return true
			}
		}
		return false
	}
	junit := portCase{Rel: "X.t", GoTest: true}
	if d := compareWithOracle(junit, oracle{}, map[string]bool{}, "", nil); !hasDiff(d, "vacuous") {
		t.Errorf("empty oracle vs empty Go run: %v, want a vacuous-oracle divergence", d)
	}
	allUnsupported := portCase{Rel: "X.t", GoTest: true, Unsupported: map[string]string{"TestU": "why"}}
	if d := compareWithOracle(allUnsupported, oracle{Passed: []string{"TestU"}}, map[string]bool{}, "", nil); !hasDiff(d, "vacuous") {
		t.Errorf("all-unsupported oracle: %v, want a vacuous-oracle divergence", d)
	}
	fully := allUnsupported
	fully.FullyUnsupported = true
	if d := compareWithOracle(fully, oracle{Passed: []string{"TestU"}}, map[string]bool{}, "", nil); len(d) != 0 {
		t.Errorf("FullyUnsupported port: %v, want no divergence", d)
	}
	if d := compareWithOracle(fully, oracle{Passed: []string{"TestU", "TestA"}}, map[string]bool{}, "", nil); !hasDiff(d, "FullyUnsupported") {
		t.Errorf("FullyUnsupported with a required test: %v, want a divergence", d)
	}
	if d := compareWithOracle(junit, oracle{Passed: []string{"TestA"}}, map[string]bool{}, "build failed", errors.New("exit status 1")); !hasDiff(d, "no Go test passed") {
		t.Errorf("failed go test with zero passes: %v, want an explicit report", d)
	}

	if d := compareRegress(oracle{}, ""); !hasDiff(d, "vacuous") {
		t.Errorf("Regress case with an empty oracle: %v, want a vacuous-oracle divergence", d)
	}
	if d := compareRegress(oracle{Diagnostics: "REGRESS: unknownSymbol\n"}, "REGRESS: unknownType\n"); !hasDiff(d, "diagnostics differ") {
		t.Errorf("Regress case with other diagnostics: %v, want a divergence", d)
	}
	if d := compareRegress(oracle{Diagnostics: "REGRESS: unknownSymbol\n"}, "REGRESS: unknownSymbol\n"); len(d) != 0 {
		t.Errorf("Regress case with the same diagnostics: %v, want none", d)
	}

	src := writeTemp(t, "@Test\npublic void testA() {}\n/* @Test\npublic void testZ() {} */\n@Test\npublic void testB() {}\n")
	sum, err := sourceSHA256(src)
	if err != nil {
		t.Fatal(err)
	}
	fresh := oracle{Passed: []string{"TestA"}, Failed: []string{"TestB"}, SourceSHA256: sum}
	if err := oracleStale(junit, fresh, src); err != nil {
		t.Errorf("fresh oracle reported stale: %v", err)
	}
	missing := fresh
	missing.Failed = nil
	if err := oracleStale(junit, missing, src); err == nil {
		t.Error("oracle missing testB not reported stale")
	}
	extra := fresh
	extra.Passed = []string{"TestA", "TestZ"}
	if err := oracleStale(junit, extra, src); err == nil {
		t.Error("oracle with commented-out testZ not reported stale")
	}
	edited := fresh
	edited.SourceSHA256 = "0000"
	if err := oracleStale(portCase{Rel: "M.t"}, edited, src); err == nil {
		t.Error("main oracle with a wrong source hash not reported stale")
	}
	if err := oracleStale(portCase{Rel: "M.t"}, oracle{SourceSHA256: sum, ExitOK: true}, src); err != nil {
		t.Errorf("fresh main oracle reported stale: %v", err)
	}
}

// TestCasesCoverPorts keeps cases, the ports and the oracle cache in step.
func TestCasesCoverPorts(t *testing.T) {
	inCases := map[string]bool{}
	oracles := map[string]bool{}
	for _, c := range cases {
		inCases[c.Rel] = true
		for _, rel := range c.Support {
			inCases[rel] = true
		}
		for _, g := range c.Gom {
			inCases[g.Rel] = true
			for _, rel := range g.Deps {
				inCases[rel] = true
			}
		}
		oracles[filepath.Base(oraclePath(c))] = true
	}
	filepath.Walk(".", func(p string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if info.IsDir() && (p == "testdata" || p == "harness" || p == "javaparity" || p == "smoke" || p == "coverage") {
			return filepath.SkipDir
		}
		if !info.IsDir() && (strings.HasSuffix(p, ".t") || strings.HasSuffix(p, ".gom")) && !inCases[filepath.ToSlash(p)] {
			t.Errorf("port %s is not listed in cases (as Rel, Support or Gom)", p)
		}
		return nil
	})
	files, _ := filepath.Glob(filepath.Join("testdata", "oracle", "*.json"))
	for _, f := range files {
		if !oracles[filepath.Base(f)] {
			t.Errorf("oracle %s belongs to no case", f)
		}
	}
	isCase := map[string]bool{}
	for _, c := range cases {
		isCase[c.Rel] = true
	}
	for _, c := range cases {
		for _, rel := range append([]string{c.Rel}, c.Support...) {
			if _, err := os.Stat(rel); err != nil {
				t.Errorf("case %s has no port %s: %v", c.Rel, rel, err)
			}
		}
		// an oracle-only dependency is an original with its own case
		for _, rel := range c.OracleDeps {
			if !isCase[rel] {
				t.Errorf("case %s: OracleDeps %s is not a case", c.Rel, rel)
			}
		}
	}
}

// TestGomCopiesIdentical: a standalone .gom is an algebraic signature,
// not host code, so the copy the Go side compiles must equal the original.
func TestGomCopiesIdentical(t *testing.T) {
	root := harness.RepoRoot(t)
	for _, c := range cases {
		for _, g := range c.Gom {
			for _, rel := range append([]string{g.Rel}, g.Deps...) {
				orig, err := os.ReadFile(filepath.Join(root, "test", rel))
				if err != nil {
					t.Fatal(err)
				}
				cp, err := os.ReadFile(rel)
				if err != nil {
					t.Errorf("%s: missing copy of test/%s: %v", c.Rel, rel, err)
					continue
				}
				if rel == g.Rel && g.PortedHooks {
					o, err1 := gomWithoutHookBodies(orig)
					c, err2 := gomWithoutHookBodies(cp)
					if err1 != nil || err2 != nil {
						t.Errorf("%s: parsing: %v %v", rel, err1, err2)
					} else if o != c {
						t.Errorf("test/go/%s differs from test/%s outside its hook bodies:\n%s\n%s", rel, rel, o, c)
					}
					continue
				}
				if string(orig) != string(cp) {
					t.Errorf("test/go/%s differs from test/%s", rel, rel)
				}
			}
		}
	}
}

// hookCode matches the code of a hook in the printed Gom AST, origin a
// line number (which the ported hook bodies may shift).
var (
	hookCode = regexp.MustCompile(`HookCode\("(?:[^"\\]|\\.)*"\)`)
	origin   = regexp.MustCompile(`Origin\([0-9]+\)`)
)

// gomWithoutHookBodies is the printed AST of a .gom with its hook code
// blanked (hook names, kinds and arguments are kept).
func gomWithoutHookBodies(src []byte) (string, error) {
	mod, err := gomparser.ParseBytes(src)
	if err != nil {
		return "", err
	}
	return origin.ReplaceAllString(hookCode.ReplaceAllString(fmt.Sprint(mod), `HookCode("")`), "Origin(0)"), nil
}

// TestLegacyFails checks that legacy/ still fails on the originals of
// legacyFails as recorded, with the default parser and with -np (tom, then
// javac on its output, as for an oracle), and that none of them is also a
// case.
func TestLegacyFails(t *testing.T) {
	for _, c := range cases {
		if _, ok := legacyFails[c.Rel]; ok {
			t.Errorf("%s is both a case and in legacyFails", c.Rel)
		}
	}
	legacy := harness.LegacyDist(t)
	root := harness.RepoRoot(t)
	cp := strings.Join([]string{
		filepath.Join(legacy.Home, "lib", "tom-runtime-full.jar"),
		filepath.Join(root, "legacy", "stable", "lib", "compiletime", "junit.jar"),
	}, string(os.PathListSeparator))
	var rels []string
	for rel := range legacyFails {
		rels = append(rels, rel)
	}
	sort.Strings(rels)
	for _, rel := range rels {
		f := legacyFails[rel]
		src := filepath.Join(root, "test", rel)
		if _, err := os.Stat(src); err != nil {
			t.Errorf("legacyFails %s: %v", rel, err)
			continue
		}
		for _, mode := range []struct {
			name string
			args []string
			want failsIn
		}{{"default", nil, f.Default}, {"np", []string{"-np"}, f.NP}} {
			t.Run(rel+"/"+mode.name, func(t *testing.T) {
				if mode.want.Stage != "tom" && mode.want.Stage != "javac" || len(mode.want.Output) == 0 {
					t.Fatalf("invalid failure signature %+v", mode.want)
				}
				dir := t.TempDir()
				out, err := legacy.Compile(t, src, dir, mode.args...)
				if mode.want.Stage == "tom" {
					if err == nil {
						t.Fatalf("legacy/'s tom accepts %s, expected it to fail with %q\n%s", rel, mode.want.Output, out)
					}
					checkFailureOutput(t, "tom", out, mode.want.Output)
					return
				}
				if err != nil {
					t.Fatalf("legacy/'s tom fails on %s, expected javac to fail on its output: %v\n%s", rel, err, out)
				}
				var javaFiles []string
				filepath.Walk(dir, func(p string, info os.FileInfo, err error) error {
					if err == nil && strings.HasSuffix(p, ".java") {
						javaFiles = append(javaFiles, p)
					}
					return err
				})
				if len(javaFiles) == 0 {
					t.Fatalf("legacy/'s tom produced no Java for %s\n%s", rel, out)
				}
				args := append([]string{"-nowarn", "-encoding", "UTF-8", "-cp", cp, "-d", dir}, javaFiles...)
				jout, err := harness.RunIn(dir, nil, "javac", args...)
				if err == nil {
					t.Fatalf("legacy/ compiles %s (tom and javac succeed): port it instead\n%s", rel, jout)
				}
				checkFailureOutput(t, "javac", jout, mode.want.Output)
			})
		}
	}
}

// checkFailureOutput requires each of want in the output of a failed
// stage.
func checkFailureOutput(t *testing.T, stage, out string, want []string) {
	t.Helper()
	for _, w := range want {
		if !strings.Contains(out, w) {
			t.Errorf("%s fails, but its output lacks %q (another failure?)\n%s", stage, w, out)
		}
	}
}

// TestCapitalise pins the JUnit -> Go test names.
func TestCapitalise(t *testing.T) {
	for in, want := range map[string]string{
		"testFoo": "TestFoo", "testfoo": "TestFoo", "test1": "Test1",
		"testcountAll1": "TestCountAll1", "foo": "TestFoo", "match4": "TestMatch4",
	} {
		if got := capitalise(in); got != want {
			t.Errorf("capitalise(%q) = %q, want %q", in, got, want)
		}
	}
}
