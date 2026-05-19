package gom

import (
	"fmt"
	"path/filepath"
	"reflect"
	"testing"

	"tom/tomgo/internal/gomast"
)

// modulesEqualIgnoreSortLine returns the difference (as a human-readable
// string) between two *Module values, ignoring `SortDecl.Line` because
// the V2 SortType node has no Option slot to round-trip it through. All
// other fields (including Alternative.Line and GomHook.Line) are
// compared with reflect.DeepEqual.
func modulesEqualIgnoreSortLine(a, b *Module) string {
	if a == nil || b == nil {
		return fmt.Sprintf("nil module: a=%v b=%v", a, b)
	}
	if !reflect.DeepEqual(a.Name, b.Name) {
		return fmt.Sprintf("Name: %q vs %q", a.Name, b.Name)
	}
	// Imports may be nil-vs-empty across the bridge — normalise.
	if len(a.Imports) != len(b.Imports) {
		return fmt.Sprintf("Imports len: %d vs %d (a=%v b=%v)", len(a.Imports), len(b.Imports), a.Imports, b.Imports)
	}
	for i := range a.Imports {
		if a.Imports[i] != b.Imports[i] {
			return fmt.Sprintf("Imports[%d]: %q vs %q", i, a.Imports[i], b.Imports[i])
		}
	}
	if len(a.Sorts) != len(b.Sorts) {
		return fmt.Sprintf("Sorts len: %d vs %d", len(a.Sorts), len(b.Sorts))
	}
	for i := range a.Sorts {
		sa, sb := a.Sorts[i], b.Sorts[i]
		if sa.Name != sb.Name {
			return fmt.Sprintf("Sorts[%d].Name: %q vs %q", i, sa.Name, sb.Name)
		}
		if len(sa.Alternatives) != len(sb.Alternatives) {
			return fmt.Sprintf("Sorts[%d](%s).Alternatives len: %d vs %d", i, sa.Name, len(sa.Alternatives), len(sb.Alternatives))
		}
		for j := range sa.Alternatives {
			if !reflect.DeepEqual(sa.Alternatives[j], sb.Alternatives[j]) {
				return fmt.Sprintf("Sorts[%d](%s).Alternatives[%d] differs:\n  a=%#v\n  b=%#v",
					i, sa.Name, j, sa.Alternatives[j], sb.Alternatives[j])
			}
		}
	}
	if len(a.Hooks) != len(b.Hooks) {
		return fmt.Sprintf("Hooks len: %d vs %d", len(a.Hooks), len(b.Hooks))
	}
	for i := range a.Hooks {
		if !reflect.DeepEqual(a.Hooks[i], b.Hooks[i]) {
			return fmt.Sprintf("Hooks[%d] differs:\n  a=%#v\n  b=%#v", i, a.Hooks[i], b.Hooks[i])
		}
	}
	return ""
}

// zeroSortDeclLines returns a deep copy of m with every SortDecl.Line
// set to 0. This is used to compare a freshly parsed module with one
// that has been round-tripped through the bridge.
func zeroSortDeclLines(m *Module) *Module {
	if m == nil {
		return nil
	}
	cp := &Module{Name: append([]string(nil), m.Name...)}
	cp.Imports = append(cp.Imports, m.Imports...)
	for _, s := range m.Sorts {
		s.Line = 0
		cp.Sorts = append(cp.Sorts, s)
	}
	cp.Hooks = append(cp.Hooks, m.Hooks...)
	return cp
}

// allCorpusFiles aggregates the .gom files used to drive the
// round-trip tests: the 10 Phase-1 corpus files plus the 5 ADT files
// from src/tom/gom/adt/.
func allCorpusFiles(t *testing.T) []string {
	t.Helper()
	out := []string{}
	for _, p := range []string{
		"Builtin.gom",
		"Dotted.gom",
		"Imported.gom",
		"Importing.gom",
		"Leaf.gom",
		"List.gom",
		"Minimal.gom",
		"Yang.gom",
		"Ying.gom",
		"fromterm/foo.gom",
	} {
		out = append(out, filepath.Join("..", "..", "testdata", "corpus", "gom-nohooks", p))
	}
	for _, p := range []string{
		"Code.gom", "Gom.gom", "Objects.gom", "Rule.gom", "SymbolTable.gom",
	} {
		out = append(out, filepath.Join("..", "..", "..", "src", "tom", "gom", "adt", p))
	}
	return out
}

// TestBridge_RoundTrip parses each of the 15 .gom files, lifts to V2,
// lowers back to V1, and compares with the original (modulo
// SortDecl.Line).
func TestBridge_RoundTrip(t *testing.T) {
	for _, path := range allCorpusFiles(t) {
		path := path
		t.Run(filepath.Base(path), func(t *testing.T) {
			m, err := ParseFile(path)
			if err != nil {
				t.Fatal(err)
			}
			v2 := Lift(m)
			back, err := Lower(v2)
			if err != nil {
				t.Fatalf("Lower returned error: %v", err)
			}
			origCmp := zeroSortDeclLines(m)
			if diff := modulesEqualIgnoreSortLine(origCmp, back); diff != "" {
				t.Fatalf("round-trip mismatch:\n%s", diff)
			}
		})
	}
}

// TestBridge_LiftIsDeterministic asserts that Lifting the same module
// twice returns the SAME shared GomModule (sharedobjects identity).
// This catches a class of bugs where Lift uses non-deterministic
// ordering or fails to canonicalise sub-terms.
func TestBridge_LiftIsDeterministic(t *testing.T) {
	for _, path := range allCorpusFiles(t)[:5] {
		path := path
		t.Run(filepath.Base(path), func(t *testing.T) {
			m, err := ParseFile(path)
			if err != nil {
				t.Fatal(err)
			}
			a := Lift(m)
			b := Lift(m)
			if a != b {
				t.Fatalf("Lift not idempotent: two lifts of the same V1 module returned distinct pointers (%p vs %p)", a, b)
			}
		})
	}
}

// TestBridge_LowerStability: Lifting and Lowering an arbitrary V1,
// then re-Lifting that V1, must yield the SAME V2 GomModule (pointer
// identity, thanks to sharing).
func TestBridge_LowerStability(t *testing.T) {
	for _, path := range allCorpusFiles(t)[:5] {
		path := path
		t.Run(filepath.Base(path), func(t *testing.T) {
			m, err := ParseFile(path)
			if err != nil {
				t.Fatal(err)
			}
			v2a := Lift(m)
			back, err := Lower(v2a)
			if err != nil {
				t.Fatal(err)
			}
			v2b := Lift(back)
			if v2a != v2b {
				t.Fatalf("Lift∘Lower∘Lift not stable for %s", path)
			}
		})
	}
}

// TestBridge_HooksPreserved exercises specifically the hook code path
// on Objects.gom — the only file in the corpus carrying a real hook.
func TestBridge_HooksPreserved(t *testing.T) {
	m, err := ParseFile("../../../src/tom/gom/adt/Objects.gom")
	if err != nil {
		t.Fatal(err)
	}
	if len(m.Hooks) != 1 {
		t.Fatalf("Objects.gom: expected 1 hook, got %d", len(m.Hooks))
	}
	v2 := Lift(m)
	back, err := Lower(v2)
	if err != nil {
		t.Fatal(err)
	}
	if len(back.Hooks) != 1 {
		t.Fatalf("after round-trip: 1 hook expected, got %d", len(back.Hooks))
	}
	orig, got := m.Hooks[0], back.Hooks[0]
	if orig.Scope != got.Scope || orig.PointCut != got.PointCut || orig.Kind != got.Kind {
		t.Fatalf("hook header drift: orig=%+v got=%+v", orig, got)
	}
	if orig.Body != got.Body {
		t.Fatalf("hook body drift:\nwant=%q\ngot =%q", orig.Body, got.Body)
	}
}

// TestBridge_KnownShape verifies the lifted V2 structure of a small,
// hand-checked example so that silent regressions in the mapping
// (e.g. swapping NamedField/StarredField) are caught immediately.
func TestBridge_KnownShape(t *testing.T) {
	src := `module M
imports int
abstract syntax
Foo = A() | B(x:int) | C(Foo*)
`
	m, err := ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	v2 := Lift(m)
	gm, ok := v2.(*gomast.GomModuleGomModule)
	if !ok {
		t.Fatalf("Lift did not return GomModuleGomModule: %T", v2)
	}
	if name := gm.ModuleName.(*gomast.GomModuleNameGomModuleName).Name; name != "M" {
		t.Errorf("module name = %q want %q", name, "M")
	}
	sectionList := gm.SectionList.(*gomast.ConcSectionSectionList)
	if got := len(sectionList.Slots); got != 2 {
		t.Fatalf("want 2 sections (imports+public), got %d", got)
	}
	imports := sectionList.Slots[0].(*gomast.ImportsSection).ImportList.(*gomast.ConcImportedModuleImportList)
	if len(imports.Slots) != 1 || imports.Slots[0].(*gomast.GomModuleNameGomModuleName).Name != "int" {
		t.Errorf("imports = %v", imports.Slots)
	}
	public := sectionList.Slots[1].(*gomast.PublicSection).ProductionList.(*gomast.ConcProductionProductionList)
	if len(public.Slots) != 1 {
		t.Fatalf("want 1 production, got %d", len(public.Slots))
	}
	sortType := public.Slots[0].(*gomast.SortTypeProduction)
	if name := sortType.Type.(*gomast.GomTypeGomType).Name; name != "Foo" {
		t.Errorf("sort name = %q", name)
	}
	alts := sortType.AlternativeList.(*gomast.ConcAlternativeAlternativeList).Slots
	if len(alts) != 3 {
		t.Fatalf("want 3 alternatives, got %d", len(alts))
	}
	// A() has no fields.
	if got := len(alts[0].(*gomast.AlternativeAlternative).DomainList.(*gomast.ConcFieldFieldList).Slots); got != 0 {
		t.Errorf("A() should have 0 fields, got %d", got)
	}
	// B(x:int) has one NamedField.
	bFields := alts[1].(*gomast.AlternativeAlternative).DomainList.(*gomast.ConcFieldFieldList).Slots
	if len(bFields) != 1 {
		t.Fatalf("B should have 1 field, got %d", len(bFields))
	}
	if _, ok := bFields[0].(*gomast.NamedFieldField); !ok {
		t.Errorf("B's field should be NamedField, got %T", bFields[0])
	}
	// C(Foo*) has one StarredField.
	cFields := alts[2].(*gomast.AlternativeAlternative).DomainList.(*gomast.ConcFieldFieldList).Slots
	if len(cFields) != 1 {
		t.Fatalf("C should have 1 field, got %d", len(cFields))
	}
	if _, ok := cFields[0].(*gomast.StarredFieldField); !ok {
		t.Errorf("C's field should be StarredField, got %T", cFields[0])
	}
}
