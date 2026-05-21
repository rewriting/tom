package testvarvarstar

import "testing"

// Term — runtime representation for A, B, BList. Hand-written since
// the Go pipeline doesn't expand inline %gom yet.
//
//   - `A` carries two BList children in `.List` (a wraps two BLists).
//   - `B` is a bare atom (`.List == nil`).
//   - `BList` is a list (`.Name == "conc"`).
//
// All three share the same struct — sort distinction is encoded in
// `.Name` (`"a"` for A, `"b"` for B, `"conc"` for BList).
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

%typeterm A {
	implement     { *Term }
	is_sort(t)    { $t != nil && $t.Name == "a" }
	equals(t1,t2) { ($t1).Equals($t2) }
}

%typeterm B {
	implement     { *Term }
	is_sort(t)    { $t != nil && $t.Name == "b" }
	equals(t1,t2) { ($t1).Equals($t2) }
}

%typeterm BList {
	implement     { *Term }
	is_sort(t)    { $t != nil && $t.Name == "conc" }
	equals(t1,t2) { ($t1).Equals($t2) }
}

%op A a(s1:BList, s2:BList) {
	is_fsym(t)     { ($t).Name == "a" }
	get_slot(s1,t) { ($t).List[0] }
	get_slot(s2,t) { ($t).List[1] }
	make(s1,s2)    { &Term{Name: "a", List: []*Term{$s1, $s2}} }
}

%op B b() {
	is_fsym(t) { ($t).Name == "b" }
	make()     { &Term{Name: "b"} }
}

%oplist BList conc(B*) {
	is_fsym(t)        { ($t).Name == "conc" }
	make_empty()      { &Term{Name: "conc", List: []*Term{}} }
	make_insert(e, l) { &Term{Name: "conc", List: append([]*Term{$e}, ($l).List...)} }
	get_head(l)       { ($l).List[0] }
	get_tail(l)       { &Term{Name: "conc", List: ($l).List[1:]} }
	is_empty(l)       { len(($l).List) == 0 }
}

// test1 — empty/empty: l = conc() matches s1, conc(l*) on s2 also
// equals conc(). Rule fires.
func TestEmptyEmpty(t *testing.T) {
	a := `a(conc(), conc())
	matched := false
	%match(A a) {
		a(l, conc(l*)) -> { matched = true }
	}
	if !matched {
		t.Errorf("a(conc(),conc()) should match a(l, conc(l*))")
	}
}

// test2 — singleton/singleton: l = conc(b()) matches s1, then
// conc(l*) on s2 expects s2 = conc(b()) too. Rule fires.
func TestSingletonSingleton(t *testing.T) {
	a := `a(conc(b()), conc(b()))
	matched := false
	%match(A a) {
		a(l, conc(l*)) -> { matched = true }
	}
	if !matched {
		t.Errorf("a(conc(b()),conc(b())) should match a(l, conc(l*))")
	}
}

// test3 — singleton/pair: l = conc(b()), but s2 = conc(b(),b())
// which has 2 elements, not 1. Rule does NOT fire.
func TestMismatch(t *testing.T) {
	a := `a(conc(b()), conc(b(), b()))
	matched := false
	%match(A a) {
		a(l, conc(l*)) -> { matched = true }
	}
	if matched {
		t.Errorf("a(conc(b()),conc(b(),b())) should NOT match a(l, conc(l*))")
	}
}
