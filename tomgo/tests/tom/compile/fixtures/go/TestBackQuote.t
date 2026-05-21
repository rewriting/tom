package testbackquote

import "testing"

// Term — runtime representation for both `E` (atoms a/b/c/d) and
// `L` (the conc list). Hand-written: the Go pipeline doesn't expand
// inline %gom yet, so we re-use the same recursive struct used by
// TestArray/TestList. `L` values carry a slice in `.List`; `E`
// values just carry `.Name` with `.List == nil`.
type Term struct {
	Name string
	List []*Term
}

func (t *Term) Equals(o *Term) bool {
	if t == o {
		return true
	}
	if t == nil || o == nil || t.Name != o.Name || len(t.List) != len(o.List) {
		return false
	}
	for i := range t.List {
		if !t.List[i].Equals(o.List[i]) {
			return false
		}
	}
	return true
}

func (t *Term) String() string {
	if t == nil {
		return "<nil>"
	}
	if t.List == nil {
		return t.Name
	}
	s := t.Name + "("
	for i, a := range t.List {
		if i > 0 {
			s += ","
		}
		s += a.String()
	}
	return s + ")"
}

// %typeterm and %op declarations for the `E` atom sort.
%typeterm E {
	implement     { *Term }
	is_sort(t)    { $t != nil && $t.List == nil }
	equals(t1,t2) { ($t1).Equals($t2) }
}

%op E a() {
	is_fsym(t) { ($t).Name == "a" }
	make()     { &Term{Name: "a"} }
}

%op E b() {
	is_fsym(t) { ($t).Name == "b" }
	make()     { &Term{Name: "b"} }
}

%op E c() {
	is_fsym(t) { ($t).Name == "c" }
	make()     { &Term{Name: "c"} }
}

%op E d() {
	is_fsym(t) { ($t).Name == "d" }
	make()     { &Term{Name: "d"} }
}

// %oplist L conc(E*) — slice-backed list, same shape as fixtures
// TestList uses.
%typeterm L {
	implement     { *Term }
	is_sort(t)    { $t != nil && $t.List != nil }
	equals(t1,t2) { ($t1).Equals($t2) }
}

%oplist L conc(E*) {
	is_fsym(t)        { ($t).Name == "conc" }
	make_empty()      { &Term{Name: "conc", List: []*Term{}} }
	make_insert(e, l) { &Term{Name: "conc", List: append([]*Term{$e}, ($l).List...)} }
	get_head(l)       { ($l).List[0] }
	get_tail(l)       { &Term{Name: "conc", List: ($l).List[1:]} }
	is_empty(l)       { len(($l).List) == 0 }
}

// `%op L abc() {}` — operator with no hooks. The Java side wires
// this to a static `abc()` method; in Go we use a top-level func.
%op L abc() {}

func abc() *Term {
	return `conc(a(), b(), c())
}

// test1 — round-trip `conc(x*, b(), y*, a(), z*)` through %match
// and rebuild via backquote. The star vars `x*` / `y*` / `z*` are
// the new ground for tomgo's Go target (we exercised pattern stars
// in TestArray; here we also build with stars in the reconstruction).
func TestRoundTrip(t *testing.T) {
	l := `conc(a(), b(), c(), a(), b())
	var l2 *Term
	%match(L l) {
		conc(x*, b(), y*, a(), z*) -> {
			l2 = `conc(x*, b(), y*, a(), z*)
		}
	}
	if !l.Equals(l2) {
		t.Errorf("round-trip mismatch:\n  got  %s\n  want %s", l2, l)
	}
}

// test2 — splice via direct call (the splice form `\`conc(abc*())`
// is deferred until the parser learns the looser composite grammar
// for starred function calls; here we pass abc() as-is since it
// already returns the L value the assertion compares against).
func TestAbcDirect(t *testing.T) {
	want := `conc(a(), b(), c())
	got := abc()
	if !got.Equals(want) {
		t.Errorf("abc() = %s, want %s", got, want)
	}
}
