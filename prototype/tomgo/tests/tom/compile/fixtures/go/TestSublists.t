package testsublists

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

		TermList = termList(Term*)
}

func TestSublist1(t *testing.T) {
	var res Term = `list(a(), b(), c())
	matched := false
	%match(res) {
		list(_*, list(a(), b(), c()), _*) -> { matched = true }
	}
	if !matched {
		t.Errorf("test1: list(_*, list(a,b,c), _*) should match")
	}
}

func TestSublist2(t *testing.T) {
	var res Term = `list(a(), b(), c())
	matched := false
	%match(res) {
		list(_*, _p@list(a(), b(), c()), _*) -> { matched = true }
	}
	if !matched {
		t.Errorf("test2: annotated sublist pattern should match")
	}
}

func TestSublist3(t *testing.T) {
	var res Term = `list(a(), b(), b(), c())
	matched := false
	bindings_ok := false
	%match(res) {
		list(X*, p@list(b(), b()), Y*) -> {
			matched = true
			if p.Equivalent(`list(b(), b())) && X.Equivalent(`list(a())) && Y.Equivalent(`list(c())) {
				bindings_ok = true
			}
		}
	}
	if !matched {
		t.Errorf("test3: anchored sublist should match")
	}
	if !bindings_ok {
		t.Errorf("test3: p/X/Y bindings wrong")
	}
}

/*
test4 — anti-pattern in sub-range. Anti-pattern compilation under
DoWhile-as-Expression is not yet implemented in our backend.

  list(_X*, p@!list(_*, !b(), _*), _Y*) ->
    p should equal list(b,b) | list(b) | list()
*/

// domain != codomain

func TestSublist5(t *testing.T) {
	var res TermList = `termList(a(), b(), b(), c())
	%match(res) {
		termList(_X*, _p@list(_*, b(), _*), _Y*) -> {
			t.Errorf("test5: inner list pattern shouldn't match atom elements")
		}
	}
}

func TestSublist6(t *testing.T) {
	var res TermList = `termList(a(), list(b(), b()), c())
	matched := false
	%match(res) {
		termList(_X*, list(_*, b(), _*), _Y*) -> { matched = true }
	}
	if !matched {
		t.Errorf("test6: inner list pattern should match the list element")
	}
}

func TestSublist7(t *testing.T) {
	var res TermList = `termList(a(), b(), b(), c())
	matched := false
	%match(res) {
		termList(_X*, termList(_*, b(), _*), _Y*) -> { matched = true }
	}
	if !matched {
		t.Errorf("test7: termList sub-range should match")
	}
}

/*
test8 — anti-pattern on termList sub-range. Same gap as test4.
*/

func TestSublist9(t *testing.T) {
	var res TermList = `termList(a(), b(), b(), c())
	matched := false
	binding_ok := false
	%match(res) {
		termList(_X*, p@termList(b(), b()), _Y*) -> {
			matched = true
			if p.Equivalent(`termList(b(), b())) {
				binding_ok = true
			}
		}
	}
	if !matched {
		t.Errorf("test9: annotated termList sub-range should match")
	}
	if !binding_ok {
		t.Errorf("test9: p binding wrong")
	}
}

/*
test10 — multi-pattern alias p@(b(),!b()). Composite-pattern
syntax our parser doesn't unfold.
*/

func TestSublist11(t *testing.T) {
	var u TermList = `termList(termList(a(), b()))
	matched := false
	%match(u) {
		_z@termList(termList(a(), b())) -> { matched = true }
	}
	if !matched {
		t.Errorf("test11: nested termList pattern should match")
	}
}
