package array_test

import (
	"testing"
)

type Term struct {
	Name string
	Args []*Term
}

func (t *Term) Equals(o *Term) bool {
	if t == o {
		return true
	}
	if t == nil || o == nil || t.Name != o.Name {
		return false
	}
	return true
}

func equalsList(a, b []*Term) bool {
	if len(a) != len(b) {
		return false
	}
	for i := range a {
		if !a[i].Equals(b[i]) {
			return false
		}
	}
	return true
}

%typeterm L {
	implement     { []*Term }
	is_sort(t)    { $t != nil }
	equals(l1,l2) { equalsList($l1, $l2) }
}

%oparray L conc( E* ) {
	is_fsym(t)       { $t != nil }
	make_empty(n)    { make([]*Term, 0, $n) }
	make_append(e,l) { append($l, $e) }
	get_element(l,n) { ($l)[$n] }
	get_size(l)      { len($l) }
}

%typeterm E {
	implement     { *Term }
	is_sort(t)    { $t != nil }
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

// sort1 — bubble sort by element name. For each pair (x, y) in wrong
// order, swap with full surrounding sublists preserved and recurse.
func sort1(l []*Term) []*Term {
	%match(L l) {
		conc(X1*, x, X2*, y, X3*) -> {
			if `x.Name > `y.Name {
				return sort1(`conc(X1*, y, X2*, x, X3*))
			}
		}
	}
	return l
}

// sort2 — same algorithm, equivalent shape via direct rebuild.
func sort2(l []*Term) []*Term {
	%match(L l) {
		conc(X1*, x, X2*, y, X3*) -> {
			if `x.Name > `y.Name {
				return sort2(`conc(X1*, y, X2*, x, X3*))
			}
		}
	}
	return l
}

// double1 — remove one occurrence of every double via non-linear `x`.
func double1(l []*Term) []*Term {
	%match(L l) {
		conc(X1*, x, X2*, x, X3*) -> {
			return double1(`conc(X1*, X2*, x, X3*))
		}
	}
	return l
}

// double2 — same as double1, alternate rewrite shape.
func double2(l []*Term) []*Term {
	%match(L l) {
		conc(X1*, x, X2*, x, X3*) -> {
			return double2(`conc(X1*, X2*, x, X3*))
		}
	}
	return l
}

// double4 — non-linear pattern with aliased VariableStar children.
func double4(l []*Term) []*Term {
	%match(L l) {
		conc(X1*, x@_, X2@_*, x, X3@_*) -> {
			return double4(`conc(X1*, X2*, x, X3*))
		}
	}
	return l
}

// double5 — three rules; the FIRST match wins.
func double5(l []*Term) []*Term {
	%match(L l) {
		conc(X1*, x@a(), X2*, x@a(), X3*) -> { return double5(`conc(X1*, X2*, x, X3*)) }
		conc(X1*, x@_,   X2*, x@_,   X3*) -> { return double5(`conc(X1*, X2*, x, X3*)) }
	}
	return l
}

// makeFixtures — analog of @BeforeClass that builds the per-test
// inputs. Called from each test to avoid the cross-test mutation
// the Java fixture suffers from (sort1 modifies the input list in
// place; we keep tests independent by always rebuilding).
func makeFixtures() (unsorted, sorted, withDoubles, withoutDoubles []*Term) {
	ta := `a()
	tb := `b()
	tc := `c()
	unsorted = []*Term{ta, tb, tc, ta, tb, tc}
	sorted = []*Term{ta, ta, tb, tb, tc, tc}
	withDoubles = []*Term{ta, tb, tc, ta, tb, tc, ta}
	withoutDoubles = []*Term{ta, tb, tc}
	return
}

func TestSort1(t *testing.T) {
	u, s, _, _ := makeFixtures()
	if got := sort1(u); !equalsList(got, s) {
		t.Errorf("sort1 = %v, want %v", got, s)
	}
}

func TestSort2(t *testing.T) {
	u, s, _, _ := makeFixtures()
	if got := sort2(u); !equalsList(got, s) {
		t.Errorf("sort2 = %v, want %v", got, s)
	}
}

func TestDouble1(t *testing.T) {
	_, _, wd, wod := makeFixtures()
	if got := double1(sort1(wd)); !equalsList(got, wod) {
		t.Errorf("double1 = %v, want %v", got, wod)
	}
}

func TestDouble2(t *testing.T) {
	_, _, wd, wod := makeFixtures()
	if got := double2(sort2(wd)); !equalsList(got, wod) {
		t.Errorf("double2 = %v, want %v", got, wod)
	}
}

func TestDouble4(t *testing.T) {
	_, _, wd, wod := makeFixtures()
	if got := double4(sort2(wd)); !equalsList(got, wod) {
		t.Errorf("double4 = %v, want %v", got, wod)
	}
}

func TestDouble5(t *testing.T) {
	_, _, wd, wod := makeFixtures()
	if got := double5(sort2(wd)); !equalsList(got, wod) {
		t.Errorf("double5 = %v, want %v", got, wod)
	}
}

func TestVariableStar1(t *testing.T) {
	nbSol := 0
	l := []*Term{`a(), `b()}
	%match(L l) {
		conc(X1*, X2*, X3*) -> {
			nbSol++
		}
	}
	if nbSol != 6 {
		t.Errorf("VariableStar split count = %d, want 6", nbSol)
	}
}
