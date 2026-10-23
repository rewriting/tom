package adt_test

import (
	"fmt"
	"testing"

	adt "tom/engine/adt"
	"tom/library/aterm"
)

// An AU list whose unit (or empty list) is absorbed ends with an element
// (Cons(x, y)): its ATerm is the list of all the elements (Java's toATerm
// throws ClassCastException here: deliberate extension,
// doc/go/README.md). An ATerm list does not name its operator: as in
// Java, ConstraintFromTerm reads it with the first variadic operator of
// the sort (AndConstraint), so only AndConstraint lists convert back to
// the same term.
func TestToATerm_AUListEndingWithElement(t *testing.T) {
	x := adt.MakeNegate(adt.MakeTrueConstraint())
	y := adt.MakeFalseConstraint()
	for _, tc := range []struct {
		c         adt.Constraint
		n         int
		roundTrip bool
	}{
		{adt.MakeAndConstraint(x, y), 2, true},
		{adt.MakeAndConstraint(x, y, x), 3, true},
		{adt.MakeOrConstraint(x, adt.MakeTrueConstraint()), 2, false},
		// AU without neutral: the empty list is absorbed too
		{adt.MakeOrConstraintDisjunction(x, y), 2, false},
	} {
		l, ok := tc.c.ToATerm().(*aterm.ATermList)
		if !ok {
			t.Fatalf("ToATerm(%v) is not an ATermList", tc.c)
		}
		if l.GetLength() != tc.n {
			t.Errorf("ToATerm(%v) = %v: %d elements, want %d", tc.c, l, l.GetLength(), tc.n)
		}
		if got := adt.ConstraintFromTerm(l); tc.roundTrip && got != tc.c {
			t.Errorf("ConstraintFromTerm(ToATerm(%v)) = %v", tc.c, got)
		}
	}
}

// The last element of an AU list is an element even when it is itself a
// list (of another operator of the sort): its ATerm is nested, as in the
// ATerm of the same elements built by a cons list ending with the empty
// list.
func TestToATerm_AUListEndingWithListElement(t *testing.T) {
	x := adt.MakeNegate(adt.MakeTrueConstraint())
	inner := adt.MakeOrConstraint(adt.MakeNegate(adt.MakeFalseConstraint()), x)
	c := adt.MakeAndConstraint(x, inner)
	l, ok := c.ToATerm().(*aterm.ATermList)
	if !ok {
		t.Fatalf("ToATerm(%v) is not an ATermList", c)
	}
	if n := l.GetLength(); n != 2 {
		t.Fatalf("ToATerm(%v) = %v: %d elements, want 2", c, l, n)
	}
	if _, ok := l.GetLast().(*aterm.ATermList); !ok {
		t.Errorf("ToATerm(%v) = %v: the last element is not the list of %v", c, l, inner)
	}
}

// Such a term, read back from its string (as a dump before the compiler
// phase), round trips through ATerm.
func TestToATerm_CodeWithAndConstraint(t *testing.T) {
	c := adt.MakeAndConstraint(adt.MakeNegate(adt.MakeTrueConstraint()), adt.MakeFalseConstraint())
	s := adt.ConstraintFromString(fmt.Sprint(c))
	if adt.ConstraintFromTerm(s.ToATerm()) != c {
		t.Fatalf("round trip of %v failed", c)
	}
}
