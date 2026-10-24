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

// bundles returns the bridge bundles to query: every *.tfix.bridge of
// TOMGO_DUMPS when set (mage bridgeCheck), otherwise those of queryCorpus,
// compiled here with --goFrom Backend --goCheck --intermediate.
func bundles(t *testing.T, dist harness.Dist) (paths []string, floor int) {
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
	for k, rel := range queryCorpus {
		dir := filepath.Join(out, fmt.Sprint(k))
		src := filepath.Join(harness.GoTestDir(root), filepath.FromSlash(rel))
		if msg, err := dist.Compile(t, src, dir, "-go", "-np", "--goFrom", "Backend", "--goCheck", "--intermediate"); err != nil {
			t.Fatalf("tom -go %s: %v\n%s", rel, err, msg)
		}
		collect(dir)
	}
	sort.Strings(paths)
	return paths, len(queryCorpus)
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
	same, differ := 0, 0
	for k, p := range paths {
		want, err := os.ReadFile(filepath.Join(outDir, fmt.Sprint(k)+".txt"))
		if err != nil {
			t.Fatal(err)
		}
		f, err := os.Open(p)
		if err != nil {
			t.Fatal(err)
		}
		b, err := bridge.Read(f)
		f.Close()
		if err != nil {
			t.Fatalf("%s: %v", p, err)
		}
		got := goQueries(b)
		if got == string(want) {
			same++
			continue
		}
		differ++
		g, w := strings.Split(got, "\n"), strings.Split(string(want), "\n")
		for i := 0; i < len(g) || i < len(w); i++ {
			var gl, wl string
			if i < len(g) {
				gl = g[i]
			}
			if i < len(w) {
				wl = w[i]
			}
			if gl != wl {
				t.Errorf("%s: line %d differs\nGo   %.300s\nJava %.300s", p, i+1, gl, wl)
				break
			}
		}
	}
	t.Logf("requêtes SymbolTable/TomBase : paquets %d, identiques %d, différents %d", len(paths), same, differ)
}
