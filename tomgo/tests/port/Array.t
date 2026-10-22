package main

import "fmt"

// Term is the element type — same minimal shape as Peano.go's, just
// reused here since Array operates over generic E-typed atoms.
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

// equalsList compares two []*Term element-wise.
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

// sort1 — bubble sort by element name. For each pair (x, y) in
// wrong order, swap (with full surrounding sublists preserved) and
// recurse.
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

// sort2 — same idea, equivalent shape via direct rebuild.
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

// double1 — remove duplicate adjacent / non-adjacent doubles via a
// non-linear pattern (`x` repeats), exercising
// GeneralPurposePropagator.mergeDuplicateVars + TestVar in the
// generator.
func double1(l []*Term) []*Term {
	%match(L l) {
		conc(X1*, x, X2*, x, X3*) -> {
			return double1(`conc(X1*, X2*, x, X3*))
		}
	}
	return l
}

func main() {
	ta := `a()
	tb := `b()
	tc := `c()

	l := []*Term{ta, tb, tc, ta, tb, tc}
	res := []*Term{ta, ta, tb, tb, tc, tc}

	if !equalsList(sort1(l), res) {
		fmt.Println("FAIL: sort1 didn't sort.")
		return
	}
	if !equalsList(sort2(l), res) {
		fmt.Println("FAIL: sort2 didn't sort.")
		return
	}
	// double1 removes ONE occurrence per duplicate pair until the
	// list is duplicate-free. Each rewrite step keeps the matched
	// `x` once, so [a, b, c, a, b, c] reduces to [a, b, c].
	if got := double1([]*Term{ta, tb, tc, ta, tb, tc}); !equalsList(got, []*Term{ta, tb, tc}) {
		fmt.Printf("FAIL: double1 = %v, want [a, b, c]\n", got)
		return
	}
	fmt.Println("Array test has successfully terminated.")
}

// String shows Term as its Name (so the FAIL print is readable).
