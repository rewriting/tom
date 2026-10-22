package testvarvarstar

import "testing"

%gom {
	module M
	abstract syntax
		A = a(s1:BList, s2:BList)
		B = b()
		BList = conc(B*)
}

// test1 — empty/empty: l binds to s1 = conc(), then conc(l*) on
// s2 = conc() splats zero elements. Rule fires.
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

// test2 — singleton/singleton: l = conc(b()), then conc(l*)
// on s2 expects s2 = conc(b()) (same as l). Rule fires.
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

// test3 — singleton/pair: l = conc(b()) but s2 = conc(b(),b())
// has 2 elements. Rule does NOT fire.
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
