package astcmp

import "testing"

func TestParse_RoundtripSimple(t *testing.T) {
	cases := []string{
		`Foo()`,
		`Foo(Bar())`,
		`Foo(Bar(),Baz())`,
		`Name("x")`,
		`OriginTracking(Name("t"),9,"foo.t")`,
		`Tom(concCode(TargetLanguageToCode(TL("hello\n",TextPosition(1,1),TextPosition(2,1)))))`,
	}
	for _, want := range cases {
		t.Run(want, func(t *testing.T) {
			n, err := Parse(want)
			if err != nil {
				t.Fatalf("parse: %v", err)
			}
			if got := n.String(); got != want {
				t.Errorf("round-trip mismatch\n  want: %s\n  got:  %s", want, got)
			}
		})
	}
}

func TestEqual_LiteralMatch(t *testing.T) {
	eq, err := Equal(`Foo(Bar("x"))`, `Foo(Bar("x"))`)
	if err != nil || !eq {
		t.Errorf("identical inputs should be equal (err=%v)", err)
	}
}

func TestEqual_DifferentLiteral(t *testing.T) {
	eq, _ := Equal(`Foo(Bar("x"))`, `Foo(Bar("y"))`)
	if eq {
		t.Errorf("different string literals should differ")
	}
}

// TestSimplify_CompositeUnwrap pins the Composite(CompositeBQTerm(t))
// → t rule used by [DefaultRules].
func TestSimplify_CompositeUnwrap(t *testing.T) {
	cases := []struct {
		java, goSide string
	}{
		// Loulou's actual diff: Java wraps the first arg of plus(x,y).
		{
			java: `concBQTerm(Composite(CompositeBQTerm(BQVariable(Name("x")))),BQVariable(Name("y")))`,
			goSide: `concBQTerm(BQVariable(Name("x")),BQVariable(Name("y")))`,
		},
		// Same shape but on a single arg.
		{
			java:   `concBQTerm(Composite(CompositeBQTerm(BQVariable(Name("x")))))`,
			goSide: `concBQTerm(BQVariable(Name("x")))`,
		},
	}
	for _, tc := range cases {
		eq, err := Equal(tc.java, tc.goSide)
		if err != nil {
			t.Errorf("parse error on (%q,%q): %v", tc.java, tc.goSide, err)
			continue
		}
		if !eq {
			java, _ := Parse(tc.java)
			goSide, _ := Parse(tc.goSide)
			t.Errorf("expected equal after Composite-unwrap\n  java(simpl): %s\n  go  (simpl): %s",
				Simplify(java).String(), Simplify(goSide).String())
		}
	}
}

func TestSimplify_NonCompositeNotUnwrapped(t *testing.T) {
	// `Composite(CompositeBQTerm(a), CompositeBQTerm(b))` is multi-
	// element; the rule must NOT fire — those carry information.
	eq, _ := Equal(
		`Composite(CompositeBQTerm(Foo()),CompositeBQTerm(Bar()))`,
		`Foo()`,
	)
	if eq {
		t.Errorf("multi-element Composite should NOT collapse")
	}
}
