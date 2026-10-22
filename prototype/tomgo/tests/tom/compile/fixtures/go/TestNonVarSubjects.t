package testnonvarsubjects

import "testing"

%gom {
	module M
		abstract syntax
		Term =
		| a()
		| b()
		| c()
		| list(Term*)
		| f(l:TermList)
		| g(t:Term)

		TermList = termList(Term*)
}

// test1 — %match on a backquote-built subject (not just a bare
// var). `_x` matches anything; the rule fires unconditionally on
// `list(a())`. If %match silently fell through, t.Errorf below
// would trigger.
func TestNonVarSubject(t *testing.T) {
	matched := false
	%match(list(a())) {
		_x -> { matched = true }
	}
	if !matched {
		t.Errorf("%%match(list(a())) { _x -> ... } should fire")
	}
}
