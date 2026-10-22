package list_test

import (
	"testing"
)

// Term — same minimal element type as the other Go fixtures.
type Term struct {
	Name string
	Args []*Term
}

func (t *Term) Equals(o *Term) bool {
	if t == o {
		return true
	}
	if t == nil || o == nil || t.Name != o.Name || len(t.Args) != len(o.Args) {
		return false
	}
	for i := range t.Args {
		if !t.Args[i].Equals(o.Args[i]) {
			return false
		}
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

// listInsert prepends an element to a slice — the canonical
// `make_insert(e, l)` body for the list hook. We define it as a
// helper so the `%oplist` body stays a single expression.
func listInsert(e *Term, l []*Term) []*Term {
	out := make([]*Term, 0, len(l)+1)
	out = append(out, e)
	out = append(out, l...)
	return out
}

%typeterm L {
	implement     { []*Term }
	is_sort(t)    { $t != nil }
	equals(l1,l2) { equalsList($l1, $l2) }
}

// %oplist hooks: list-style (vs Array's get_element/get_size). The
// matcher will iterate via get_tail until is_empty fires; each
// match-step "consumes" one head element.
%oplist L conc( E* ) {
	is_fsym(t)       { $t != nil }
	get_head(l)      { ($l)[0] }
	get_tail(l)      { ($l)[1:] }
	is_empty(l)      { len($l) == 0 }
	make_empty()     { []*Term{} }
	make_insert(e,l) { listInsert($e, $l) }
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

%op E f(s1:E) {
	is_fsym(t)     { ($t).Name == "f" }
	get_slot(s1,t) { ($t).Args[0] }
	make(s)        { &Term{Name: "f", Args: []*Term{$s}} }
}

// classifyTag pattern-matches a list against several shapes and
// returns a short tag for the first pattern that fires. Mirrors
// (a subset of) Java's Match1.match1 — non-parameterized, just the
// patterns I want to exercise.
func classifyTag(l []*Term) string {
	%match(L l) {
		conc()                            -> { return "empty" }
		conc(f(a()))                      -> { return "singleton_fa" }
		conc(_X1*, f(a()))                -> { return "ends_fa" }
		conc(_X1*, f(a()), _X2*, f(b()))  -> { return "fa_then_fb" }
		conc(f(b()), _X2*, f(b()), _X3*)  -> { return "starts_fb_then_fb" }
	}
	return "other"
}

// firstHead returns the first element via a list pattern, or nil.
// Mirrors get_head but goes through `%match`.
func firstHead(l []*Term) *Term {
	%match(L l) {
		conc(head, _Tail*) -> { return `head }
	}
	return nil
}

// countElements walks the list with a tail-recursive %match.
func countElements(l []*Term) int {
	%match(L l) {
		conc()             -> { return 0 }
		conc(_h, t*)       -> { return 1 + countElements(`t) }
	}
	return 0
}

// Helpers to make tests readable.
func ta() *Term { return `a() }
func tb() *Term { return `b() }
func tc() *Term { return `c() }
func fa() *Term { return `f(a()) }
func fb() *Term { return `f(b()) }

func TestEmpty(t *testing.T) {
	if got := classifyTag([]*Term{}); got != "empty" {
		t.Errorf("classifyTag([]) = %q, want empty", got)
	}
}

func TestSingletonFA(t *testing.T) {
	if got := classifyTag([]*Term{fa()}); got != "singleton_fa" {
		t.Errorf("classifyTag([f(a)]) = %q, want singleton_fa", got)
	}
}

func TestEndsFA(t *testing.T) {
	if got := classifyTag([]*Term{ta(), tb(), fa()}); got != "ends_fa" {
		t.Errorf("classifyTag([a,b,f(a)]) = %q, want ends_fa", got)
	}
}

func TestFAthenFB(t *testing.T) {
	if got := classifyTag([]*Term{ta(), fa(), tb(), fb()}); got != "fa_then_fb" {
		t.Errorf("classifyTag([a,f(a),b,f(b)]) = %q, want fa_then_fb", got)
	}
}

func TestStartsFBthenFB(t *testing.T) {
	if got := classifyTag([]*Term{fb(), fb()}); got != "starts_fb_then_fb" {
		t.Errorf("classifyTag([f(b),f(b)]) = %q, want starts_fb_then_fb", got)
	}
}

func TestFirstHead(t *testing.T) {
	if got := firstHead([]*Term{ta(), tb(), tc()}); !got.Equals(ta()) {
		t.Errorf("firstHead([a,b,c]) = %v, want a", got)
	}
	if got := firstHead([]*Term{}); got != nil {
		t.Errorf("firstHead([]) = %v, want nil", got)
	}
}

func TestCount(t *testing.T) {
	if got := countElements([]*Term{}); got != 0 {
		t.Errorf("countElements([]) = %d, want 0", got)
	}
	if got := countElements([]*Term{ta(), tb(), tc()}); got != 3 {
		t.Errorf("countElements([a,b,c]) = %d, want 3", got)
	}
}
