package testnumericconditions

import "testing"

%include { int.tom }

%gom {
	module M
		imports int
		abstract syntax
		Term =
		| a()
		| b()
		| c()
		| list(Term*)
		| f(t1:Term, t2:int)
		| h(l:TermList)
		| g(t:Term)

		TermList = termList(Term*)
}

// test1 — y > 5 fires.
func TestGreater(t *testing.T) {
	matched := false
	%match(f(a(), 6)) {
		f(_x, y) && y > 5 -> { matched = true }
	}
	if !matched {
		t.Errorf("f(a(),6) && y > 5 should fire")
	}
}

// test2 — y >= 6 fires.
func TestGreaterOrEqual(t *testing.T) {
	matched := false
	%match(f(a(), 6)) {
		f(_x, y) && y >= 6 -> { matched = true }
	}
	if !matched {
		t.Errorf("f(a(),6) && y >= 6 should fire")
	}
}

// test3 — y < 6 fires on y=3.
func TestLess(t *testing.T) {
	matched := false
	%match(f(a(), 3)) {
		f(_x, y) && y < 6 -> { matched = true }
	}
	if !matched {
		t.Errorf("f(a(),3) && y < 6 should fire")
	}
}

// test5 — y != 4 with y=4 should NOT fire.
func TestNotEqual(t *testing.T) {
	matched := false
	%match(f(a(), 4)) {
		f(_x, y) && y != 4 -> { matched = true }
	}
	if matched {
		t.Errorf("f(a(),4) && y != 4 should NOT fire")
	}
}

// test6 — y == 4 with y=4 fires.
func TestEqual(t *testing.T) {
	matched := false
	%match(f(a(), 4)) {
		f(_x, y) && y == 4 -> { matched = true }
	}
	if !matched {
		t.Errorf("f(a(),4) && y == 4 should fire")
	}
}

// test7 — chained y > 2 && y < 5 with y=4 fires.
func TestChained(t *testing.T) {
	matched := false
	%match(f(a(), 4)) {
		f(_x, y) && y > 2 && y < 5 -> { matched = true }
	}
	if !matched {
		t.Errorf("f(a(),4) && y > 2 && y < 5 should fire")
	}
}
