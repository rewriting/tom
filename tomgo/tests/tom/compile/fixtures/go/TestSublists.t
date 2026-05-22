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

// test1 — match the whole subject as a contiguous range.
// Pattern `list(_*, list(a,b,c), _*)` on subject `list(a,b,c)`:
// begin = subject, end = empty → slice = list(a,b,c) → matches.
func TestSublistWhole(t *testing.T) {
	var res Term = `list(a(), b(), c())
	matched := false
	%match(res) {
		list(_*, list(a(), b(), c()), _*) -> { matched = true }
	}
	if !matched {
		t.Errorf("list(_*, list(a,b,c), _*) should match list(a,b,c) as full range")
	}
}

// test2 — same but with annotation `_p@list(a,b,c)`. Inner pattern
// is wrapped in an alias annotation; the range-match still fires.
func TestSublistAnnotated(t *testing.T) {
	var res Term = `list(a(), b(), c())
	matched := false
	%match(res) {
		list(_*, _p@list(a(), b(), c()), _*) -> { matched = true }
	}
	if !matched {
		t.Errorf("annotated sublist pattern should still match")
	}
}

// test3 — anchored sublist between X* and Y* var-stars on a
// 4-element list. The pattern `list(X*, p@list(b,b), Y*)` finds
// the contiguous [b, b] sub-range, with X=[a] and Y=[c]. The
// annotation `p@` binds p to the matched sublist.
func TestSublistAnchored(t *testing.T) {
	var res Term = `list(a(), b(), b(), c())
	matched := false
	%match(res) {
		list(X*, p@list(b(), b()), Y*) -> { matched = true; _, _, _ = X, p, Y }
	}
	if !matched {
		t.Errorf("anchored sublist pattern should match")
	}
}
