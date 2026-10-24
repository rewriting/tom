package j4

import (
	"fmt"
	"io/fs"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"testing"
	"time"

	"tom/engine/adt"
	"tom/engine/bridge"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/sharedobjects"
	"tomtools/harness"
)

// queryCorpus are the sources (under test/go) whose bundles mage j4
// queries; mage bridgeCheck queries those of T1 and of the smoke tests.
var queryCorpus = []string{"Peano.t", "TestArray.t", "acmatching/TestAC.t", "TestStrategy.t", "gom/TestList.t"}

var queryTypeNames = []string{"int", "long", "float", "char", "double", "boolean", "String",
	"universal", "void", "intarray", "unknown type", "nosuchtype"}

type queries struct {
	st    *tools.SymbolTable
	out   strings.Builder
	index int
}

func (q *queries) emit(s string) {
	q.out.WriteString(s)
	q.out.WriteByte('\n')
}

// try is q() of SymbolTableQueries.java: the Java text of the value,
// "null" for nil, "!error" for a runtime exception.
func try(f func() any) string {
	var v any
	if !platform.CatchRuntime(func() { v = f() }) {
		return "!error"
	}
	return platform.JavaString(v)
}

// goQueries writes, for a bundle, the lines SymbolTableQueries.java
// writes in Java.
func goQueries(b *bridge.Bundle) string {
	q := &queries{st: tools.FromBundle(b)}
	st := q.st
	q.symbols()
	q.types()
	q.emit("builtins " + strings.Join([]string{
		fmt.Sprint(st.GetIntType()), fmt.Sprint(st.GetIntArrayType()), fmt.Sprint(st.GetLongType()),
		fmt.Sprint(st.GetFloatType()), fmt.Sprint(st.GetCharType()), fmt.Sprint(st.GetDoubleType()),
		fmt.Sprint(st.GetBooleanType()), fmt.Sprint(st.GetStringType()), fmt.Sprint(st.GetUniversalType()),
		fmt.Sprint(st.GetVoidType()), st.GetIntArrayOp(),
	}, " "))
	for _, ke := range st.GetUsedKeyEntries() {
		q.emit("usedEntry " + fmt.Sprint(ke))
	}
	for _, ty := range st.GetTypeEntries() {
		q.emit("typeEntry " + fmt.Sprint(ty))
	}
	q.emit("table " + fmt.Sprint(st.ToTerm()))
	q.walk(b.Code)
	q.emit("prefix " + tools.GoDeclPrefix(b.Input) + " " + tools.GoDeclPrefix(b.InputName))
	return q.out.String()
}

func (q *queries) symbols() {
	for _, name := range q.st.KeySymbols() {
		s := q.st.GetSymbolFromName(name)
		codomain := tools.GetSymbolCodomain(s)
		q.emit("symbol " + name)
		q.emit("  codomain " + platform.JavaString(codomain))
		q.emit("  domain " + platform.JavaString(tools.GetSymbolDomain(s)))
		q.emit("  tomType " + try(func() any {
			n, ok := tools.GetTomType(codomain)
			if !ok {
				return nil
			}
			return n
		}))
		q.emit("  tlType " + try(func() any { return tools.GetTLType(codomain) }))
		q.emit(fmt.Sprintf("  used %v %v %v %v %v", q.st.IsUsedSymbolConstructor(s), q.st.IsUsedSymbolDestructor(s),
			q.st.IsUsedSymbolAC(s), q.st.IsUsedSymbolConstructorName(name), q.st.IsUsedSymbolDestructorName(name)))
		n := s.GetPairNameDeclList().Length()
		for i := 0; i <= n; i++ {
			q.emit(fmt.Sprintf("  slotName %d %s", i, try(func() any { return tools.GetSlotName(s, i) })))
		}
		for l := s.GetPairNameDeclList(); !l.IsEmptyconcPairNameDecl(); l = l.GetTailconcPairNameDecl() {
			p := l.GetHeadconcPairNameDecl()
			q.emit(fmt.Sprintf("  slot %s index %d type %s", platform.JavaString(p.GetSlotName()),
				tools.GetSlotIndex(s, p.GetSlotName()), try(func() any { return tools.GetSlotType(s, p.GetSlotName()) })))
		}
	}
}

func (q *queries) types() {
	set := map[string]bool{}
	for _, n := range queryTypeNames {
		set[n] = true
	}
	add := func(t adt.TomType) {
		if t.IsType() {
			set[t.GetTomType()] = true
		}
	}
	for _, t := range q.st.GetTypeEntries() {
		add(t)
	}
	for _, name := range q.st.KeySymbols() {
		add(tools.GetSymbolCodomain(q.st.GetSymbolFromName(name)))
	}
	names := make([]string, 0, len(set))
	for n := range set {
		names = append(names, n)
	}
	sort.Strings(names)
	st := q.st
	for _, n := range names {
		q.emit(fmt.Sprintf("type %s get %s used %v builtin %v unknown %v builtinType %s is %v %v %v %v %v %v %v",
			n, try(func() any { return st.GetType(n) }), st.IsUsedTypeName(n), st.IsBuiltinType(n), st.IsUnknownType(n),
			try(func() any { return st.GetBuiltinType(n) }),
			st.IsIntType(n), st.IsLongType(n), st.IsFloatType(n), st.IsCharType(n),
			st.IsStringType(n), st.IsBooleanType(n), st.IsDoubleType(n)))
	}
}

// gomTerm is a term of the engine ADT; the other children (strings,
// numbers: Java's VisitableBuiltin) are skipped, as in Java.
type gomTerm interface {
	sharedobjects.Term
	ChildCount() int
	ChildAt(i int) any
}

func (q *queries) walk(node any) {
	v, ok := node.(gomTerm)
	if !ok {
		return
	}
	i := q.index
	q.index++
	if t, ok := node.(adt.TomTerm); ok {
		q.emit(fmt.Sprintf("termType T %d %s", i, try(func() any { return tools.GetTermType(t, q.st) })))
	}
	if t, ok := node.(adt.BQTerm); ok {
		q.emit(fmt.Sprintf("termType B %d %s", i, try(func() any { return tools.GetTermTypeBQ(t, q.st) })))
	}
	if t, ok := node.(adt.Expression); ok {
		q.emit(fmt.Sprintf("termType E %d %s", i, try(func() any { return tools.GetTermTypeExpr(t, q.st) })))
	}
	if l, ok := node.(adt.TomNumberList); ok {
		q.emit(fmt.Sprintf("numberList %d %s", i, try(func() any { return tools.TomNumberListToString(l) })))
	}
	if l, ok := node.(adt.OptionList); ok {
		q.emit(fmt.Sprintf("moduleName %d %s", i, try(func() any {
			m, ok := tools.GetModuleName(l)
			if !ok {
				return nil
			}
			return m
		})))
	}
	for k := 0; k < v.ChildCount(); k++ {
		q.walk(v.ChildAt(k))
	}
}

// bundles returns the bridge bundles of queryCorpus (see bundlesOf).
func bundles(t *testing.T, dist harness.Dist) ([]string, int) {
	return bundlesOf(t, dist, queryCorpus)
}

// bundlesOf returns the bridge bundles to query: every *.tfix.bridge of
// TOMGO_DUMPS when set (mage bridgeCheck), otherwise those of corpus,
// compiled here with --goFrom Backend --goCheck --intermediate.
func bundlesOf(t *testing.T, dist harness.Dist, corpus []string) (paths []string, floor int) {
	t.Helper()
	collect := func(dir string) {
		filepath.WalkDir(dir, func(p string, d fs.DirEntry, err error) error {
			if err == nil && !d.IsDir() && strings.HasSuffix(p, ".tfix.bridge") {
				paths = append(paths, p)
			}
			return nil
		})
	}
	if dumps := os.Getenv(harness.DumpsEnv); dumps != "" {
		collect(dumps)
		sort.Strings(paths)
		return paths, 110
	}
	root := harness.RepoRoot(t)
	out := t.TempDir()
	for k, rel := range corpus {
		dir := filepath.Join(out, fmt.Sprint(k))
		src := filepath.Join(harness.GoTestDir(root), filepath.FromSlash(rel))
		if msg, err := dist.Compile(t, src, dir, "-go", "-np", "--goFrom", "Backend", "--goCheck", "--intermediate"); err != nil {
			t.Fatalf("tom -go %s: %v\n%s", rel, err, msg)
		}
		collect(dir)
	}
	sort.Strings(paths)
	return paths, len(corpus)
}

// readBundle reads the bridge bundle at path.
func readBundle(t *testing.T, path string) *bridge.Bundle {
	t.Helper()
	f, err := os.Open(path)
	if err != nil {
		t.Fatal(err)
	}
	defer f.Close()
	b, err := bridge.Read(f)
	if err != nil {
		t.Fatalf("%s: %v", path, err)
	}
	return b
}

// TestSymbolTableQueries compares the answers of the Go SymbolTable and
// TomBase (tom/engine/tools) with those of Java on bridge bundles: the
// outputs of goQueries and SymbolTableQueries.java must be identical.
func TestSymbolTableQueries(t *testing.T) {
	dist := harness.NewDist(t)
	cp := javaHelpers(t, dist)
	paths, floor := bundles(t, dist)
	if len(paths) < floor {
		t.Fatalf("%d bundles, want at least %d", len(paths), floor)
	}
	outDir := t.TempDir()
	args := append([]string{"-cp", cp, "SymbolTableQueries", outDir}, paths...)
	if msg, err := harness.RunInTimeout(outDir, 10*time.Minute, nil, "java", args...); err != nil {
		t.Fatalf("java SymbolTableQueries: %v\n%s", err, msg)
	}
	same, differ, totalData := 0, 0, 0
	for k, p := range paths {
		want, err := os.ReadFile(filepath.Join(outDir, fmt.Sprint(k)+".txt"))
		if err != nil {
			t.Fatal(err)
		}
		b := readBundle(t, p)
		got := goQueries(b)
		nsym := b.Table.GetEntryList().Length()
		if err := checkBundleData(nsym, string(want)); err != nil {
			t.Errorf("%s: Java output: %v", p, err)
		}
		if err := checkBundleData(nsym, got); err != nil {
			t.Errorf("%s: Go output: %v", p, err)
		}
		totalData += dataLines(string(want))
		if msg := firstDiff(got, string(want)); msg != "" {
			differ++
			t.Errorf("%s: %s", p, msg)
			continue
		}
		same++
	}
	dfloor := minDataLinesJ4
	if os.Getenv(harness.DumpsEnv) != "" {
		dfloor = minDataLinesBridge
	}
	t.Logf("lignes de donnée (symbol, usedEntry, termType, used true) : total %d sur %d paquets, plancher %d", totalData, len(paths), dfloor)
	if err := checkDataTotal(totalData, dfloor); err != nil {
		t.Error(err)
	}
	t.Logf("requêtes SymbolTable/TomBase : paquets %d, identiques %d, différents %d", len(paths), same, differ)
}

// Floors of the data-dependent lines of the Java answers (see dataLines).
// The fixed lines of an output (12 builtin `type` lines, builtins, table,
// prefix) are there for an empty bundle too, so they prove nothing; the
// data lines depend on the bundle. Measured (TestSymbolTableQueries log):
// 2750 on the 5 bundles of mage j4, 37850 on the 110 of
// bridgeCheck; the floors are a little under them.
const (
	minDataLinesPerBundle = 1 // for a bundle whose table is not empty
	minDataLinesJ4        = 2600
	minDataLinesBridge    = 36000
)

// dataLines counts the lines of a query output that depend on the bundle:
// `symbol` lines, `usedEntry` lines, `termType` answers that are not
// !error and `used` lines (the used marks of a symbol) with a true.
func dataLines(out string) int {
	n := 0
	for _, l := range strings.Split(out, "\n") {
		switch {
		case strings.HasPrefix(l, "symbol "), strings.HasPrefix(l, "usedEntry "):
			n++
		case strings.HasPrefix(l, "termType "):
			if !strings.HasSuffix(l, " !error") {
				n++
			}
		case strings.HasPrefix(l, "  used "):
			if strings.Contains(l, "true") {
				n++
			}
		}
	}
	return n
}

// checkBundleData fails when a bundle whose table has symbols yields an
// output without any data line (a vacuous comparison).
func checkBundleData(symbols int, out string) error {
	if n := dataLines(out); symbols > 0 && n < minDataLinesPerBundle {
		return fmt.Errorf("%d data lines for a table of %d symbols, want at least %d (vacuous comparison)", n, symbols, minDataLinesPerBundle)
	}
	return nil
}

// checkDataTotal fails when the data lines of all the bundles together
// are under the floor.
func checkDataTotal(total, floor int) error {
	if total < floor {
		return fmt.Errorf("%d data lines over all the bundles, want at least %d (vacuous comparison)", total, floor)
	}
	return nil
}

// firstDiff describes the first differing line of two query outputs ("" when identical).
func firstDiff(got, want string) string {
	if got == want {
		return ""
	}
	if msg := firstLineDiff(got, want); msg != "" {
		return msg
	}
	return "outputs differ"
}

// firstLineDiff names the first line where got and want differ ("" when
// they are equal).
func firstLineDiff(got, want string) string {
	g, w := strings.Split(got, "\n"), strings.Split(want, "\n")
	for i := 0; i < len(g) || i < len(w); i++ {
		var gl, wl string
		if i < len(g) {
			gl = g[i]
		}
		if i < len(w) {
			wl = w[i]
		}
		if gl != wl {
			return fmt.Sprintf("line %d differs\nGo   %.300s\nJava %.300s", i+1, gl, wl)
		}
	}
	return ""
}

// TestQueryComparisonDetectsDifference is the counter-proof of
// TestSymbolTableQueries: a wrong answer, a missing line or an extra line
// must be reported, and an identical output must not.
func TestQueryComparisonDetectsDifference(t *testing.T) {
	want := "a 1\nb 2\nc 3\n"
	if firstDiff(want, want) != "" {
		t.Fatal("identical outputs reported as different")
	}
	for name, got := range map[string]string{
		"wrong value": "a 1\nb 9\nc 3\n",
		"missing":     "a 1\nb 2\n",
		"extra":       "a 1\nb 2\nc 3\nd 4\n",
		"empty":       "",
	} {
		if firstDiff(got, want) == "" {
			t.Errorf("%s: difference not detected", name)
		}
	}
	// An empty bundle: only the fixed lines (builtin types, builtins, table,
	// prefix), no data. It must fail both floors.
	fixed := "type int get null\ntype void get null\nbuiltins x\ntable Table(concTomEntry())\nprefix tom_x_ tom_x_\n"
	if dataLines(fixed) != 0 {
		t.Errorf("fixed lines counted as data: %d", dataLines(fixed))
	}
	if checkBundleData(3, fixed) == nil {
		t.Error("a non-empty table with no data line passed")
	}
	if checkDataTotal(dataLines(fixed), minDataLinesJ4) == nil || checkDataTotal(dataLines(fixed), minDataLinesBridge) == nil {
		t.Error("an empty set of bundles passed the totals")
	}
	if dataLines("symbol a\n  used true false false false false\ntermType T 1 !error\ntermType T 2 int\nusedEntry k\n") != 4 {
		t.Error("dataLines miscounts")
	}
}
