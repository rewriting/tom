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
func TestSublist1(t *testing.T) {
	var res Term = `list(a(), b(), c())
	matched := false
	%match(res) {
		list(_*, list(a(), b(), c()), _*) -> { matched = true }
	}
	if !matched {
		t.Errorf("list(_*, list(a,b,c), _*) should match")
	}
}

// test2 — same with annotation `_p@`.
func TestSublist2(t *testing.T) {
	var res Term = `list(a(), b(), c())
	matched := false
	%match(res) {
		list(_*, _p@list(a(), b(), c()), _*) -> { matched = true }
	}
	if !matched {
		t.Errorf("annotated sublist pattern should match")
	}
}

/*

The upstream Java fixture (/tom/test/TestSublists.t lines 71-189)
contains tests 3-11 inside a comment block authored by the original
Tom developers. We mirror that here: those tests live in this
block until our backend grows the features they exercise.

test3 — anchored sub-range with annotation 'p@list(b(),b())',
        plus bindings checks (X, p, Y equality).

test4 — anti-pattern inside sub-range:
        list(_X*, p@!list(_*, !b(), _*), _Y*).
        Requires Negate-aware emission of DoWhile cursor loops
        (Java's reference lifts them to flag-statements).

tests 5, 6, 7, 9 — cross-sort or same-head where the slice type
        differs from the element type (termList outer / list
        inner, or termList outer / termList inner). Compiles in
        Java via Object-erasure casts; Go's strict interface
        typing rejects 'Term = TermList' assignments.

test8 — anti-pattern on termList sub-range; combines 4 + 7's
        complications.

test10 — multi-pattern alias 'p@(b(), !b())' — composite
        pattern syntax our parser doesn't yet expand.

test11 — TermList nested in TermList (upstream type mismatch
        accepted by Java erasure, rejected by Go interface
        typing).

*/
