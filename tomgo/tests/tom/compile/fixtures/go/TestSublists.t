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
func TestSublist1(t *testing.T) {
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
func TestSublist2(t *testing.T) {
	var res Term = `list(a(), b(), c())
	matched := false
	%match(res) {
		list(_*, _p@list(a(), b(), c()), _*) -> { matched = true }
	}
	if !matched {
		t.Errorf("annotated sublist pattern should still match")
	}
}

/*
test3 — anchored sub-range between X-star / Y-star var-stars on a 4-element
list; annotation p@ binds p to the matched sublist.

  var res Term = `list(a(), b(), b(), c())
  %match(res) {
    list(X*, p@list(b(), b()), Y*) -> {
      // p == `list(b,b), X == `list(a), Y == `list(c)
    }
  }

test4 — anti-pattern inside sub-range: list(_X*, p@!list(_*,!b(),_*), _Y*).
test5 — termList outer / list inner (different sort): rule must NOT fire.
test6 — termList outer / list inner, subject has a list element.
test7 — termList outer / termList inner (same head, codomain TermList).
test8 — anti-pattern on termList sub-range.
test9 — alias on same-head termList sub-range.
test10 — multi-pattern alias with anti: p@(b(), !b()).
test11 — nested termList: termList(termList(a,b)) matched by
         _z@termList(termList(a,b)).

These 9 tests are commented out in the upstream Java fixture
(/tom/test/TestSublists.t lines 71-189) and reproduced here as a
prose summary rather than code. To activate them, our backend
needs the following gaps closed:

  - Anti-pattern inside sub-range — needs Negate-aware
    pregenerator/backend lowering (tests 4, 8, 10).
  - Strict Go interface typing for cross-sort sub-range
    (tests 5, 6 emit `Term = TermList` casts that Go rejects).
  - Nested same-head where inner is the COMPLETE value (test 11
    is a type mismatch even in the upstream — TermList nested in
    TermList without an intervening Term).
*/
