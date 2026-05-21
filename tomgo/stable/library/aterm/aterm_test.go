package aterm

import (
	"testing"
)

// Test1_MakeInt — port of Test1.testMakeInt: makeInt sharing + type +
// toString + match("3") + match("<int>").
func Test1_MakeInt(t *testing.T) {
	f := NewFactory()
	a := f.MakeInt(3)
	b := f.MakeInt(3)
	if a != b {
		t.Fatal("makeInt(3) should be shared")
	}
	if a.Type() != INT {
		t.Errorf("Type() = %d, want INT", a.Type())
	}
	if a.GetInt() != 3 {
		t.Errorf("GetInt() = %d, want 3", a.GetInt())
	}
	if a.String() != "3" {
		t.Errorf("String() = %q, want %q", a.String(), "3")
	}
	result := a.Match("3")
	if result == nil || len(result) != 0 {
		t.Errorf("match(\"3\") = %v, want empty list", result)
	}
	result = a.Match("<int>")
	if result == nil || len(result) != 1 || result[0] != int64(3) {
		t.Errorf("match(\"<int>\") = %v, want [3]", result)
	}
}

func Test1_MakeLong(t *testing.T) {
	f := NewFactory()
	a := f.MakeLong(3)
	b := f.MakeLong(3)
	if a != b {
		t.Fatal("makeLong(3) should be shared")
	}
	if a.Type() != LONG {
		t.Errorf("Type() = %d, want LONG", a.Type())
	}
	if a.GetLong() != 3 {
		t.Errorf("GetLong() = %d, want 3", a.GetLong())
	}
	if a.String() != "3" {
		t.Errorf("String() = %q, want %q", a.String(), "3")
	}
	result := a.Match("3L")
	if result == nil || len(result) != 0 {
		t.Errorf("match(\"3L\") = %v, want empty list", result)
	}
	result = a.Match("<long>")
	if result == nil || len(result) != 1 || result[0] != int64(3) {
		t.Errorf("match(\"<long>\") = %v, want [3]", result)
	}
}

func Test1_MakeReal(t *testing.T) {
	f := NewFactory()
	pi := 3.141592653589793
	a := f.MakeReal(pi)
	b := f.MakeReal(pi)
	if a != b {
		t.Fatal("makeReal(pi) should be shared")
	}
	if a.Type() != REAL {
		t.Errorf("Type() = %d, want REAL", a.Type())
	}
	if a.GetReal() != pi {
		t.Errorf("GetReal() = %v", a.GetReal())
	}
	result := a.Match("<real>")
	if result == nil || len(result) != 1 || result[0] != pi {
		t.Errorf("match(\"<real>\") = %v", result)
	}
}

func Test1_MakeAppl(t *testing.T) {
	f := NewFactory()
	f0 := f.MakeAFun("f0", 0, false)
	f1 := f.MakeAFun("f1", 1, false)
	f6 := f.MakeAFun("f6", 6, false)
	f10 := f.MakeAFun("f10", 10, false)

	a0 := f.MakeAppl(f0)
	a1 := f.MakeAppl(f1, a0)
	a2 := f.MakeAppl(f1, a1)
	a3 := f.MakeAppl(f1, a0)
	_ = f.MakeAppl(f6, a0, a0, a1, a0, a0, a1)
	_ = f.MakeAppl(f10, a0, a1, a0, a1, a0, a1, a0, a1, a0, a1)
	a6 := a2.SetArgument(a0, 0)

	if !a6.IsEqual(a1) {
		t.Error("a6 (setArgument) should equal a1")
	}
	if !a1.IsEqual(a3) {
		t.Error("a1 should equal a3 (hash-cons sharing)")
	}
	if a2.IsEqual(a1) {
		t.Error("a2 should not equal a1")
	}
	if a2.IsEqual(a6) {
		t.Error("a2 should not equal a6")
	}
}

func Test1_Parser(t *testing.T) {
	f := NewFactory()
	// Each entry is a case the parser must accept; the trailing
	// boolean indicates whether parse→String→parse should be a
	// fixed-point. Cases like `(1)` produce an unnamed appl whose
	// printer collapses the head to `""` — those can't round-trip,
	// matching the Java reference behaviour.
	type tcase struct {
		src       string
		roundTrip bool
	}
	cases := []tcase{
		{"g", true},
		{"f()", false},
		{"f(1)", true},
		{`"f"(1)`, true},
		{`"subject"(<str>)`, true},
		{"f(1,2,<int>)", true},
		{"[]", true},
		{"[1]", true},
		{"[1,2]", true},
		{"[1,3.5,4e6,123.21E-3,-12]", false},
		{"[1,a,f(1)]", true},
		{"(1)", false},
		{"[()]", false},
		{`["f"()]`, false},
	}
	for _, tc := range cases {
		t.Run(tc.src, func(t *testing.T) {
			term := f.Parse(tc.src)
			if term == nil {
				t.Fatalf("parse(%q) returned nil", tc.src)
			}
			if tc.roundTrip {
				term2 := f.Parse(term.String())
				if term != term2 {
					t.Errorf("round-trip failed:\n  %q → %s\n  re-parse → %s",
						tc.src, term.String(), term2.String())
				}
			}
		})
	}
}

func Test1_ParseError(t *testing.T) {
	f := NewFactory()
	defer func() {
		r := recover()
		if r == nil {
			t.Fatal("expected ParseError on unterminated quote")
		}
		e, ok := r.(*ParseError)
		if !ok {
			t.Fatalf("expected *ParseError, got %T: %v", r, r)
		}
		if e.Message != "Unterminated quoted function symbol" {
			t.Errorf("error message = %q, want 'Unterminated quoted function symbol'", e.Message)
		}
	}()
	f.Parse(`f("`)
}

func Test1_MakeList(t *testing.T) {
	f := NewFactory()
	t0 := f.Parse("[0,1,2,3,4,5,4,3,2,1]")
	ts0 := t0.(*ATermList)

	t1 := f.Parse("[]")
	ts1 := f.MakeListEmpty()
	if t1 != ATerm(ts1) {
		t.Fatal("empty list should be shared with f.MakeListEmpty()")
	}

	ts2 := f.Parse("[1,2,3]").(*ATermList)
	ts3 := f.Parse("[4,5,6]").(*ATermList)
	ts4 := f.Parse("[1,2,3,4,5,6]").(*ATermList)
	ts5 := f.Parse("[1,2,3,4,5,6,7]").(*ATermList)

	if ts0.GetLength() != 10 {
		t.Errorf("getLength = %d, want 10", ts0.GetLength())
	}
	if ts0.IndexOf(f.MakeInt(2), 0) != 2 {
		t.Errorf("indexOf(2) = %d, want 2", ts0.IndexOf(f.MakeInt(2), 0))
	}
	if ts0.IndexOf(f.MakeInt(10), 0) != -1 {
		t.Errorf("indexOf(10) = %d, want -1", ts0.IndexOf(f.MakeInt(10), 0))
	}
	if ts0.IndexOf(f.MakeInt(0), 0) != 0 {
		t.Errorf("indexOf(0) = %d", ts0.IndexOf(f.MakeInt(0), 0))
	}
	if ts0.IndexOf(f.MakeInt(5), 0) != 5 {
		t.Errorf("indexOf(5) = %d", ts0.IndexOf(f.MakeInt(5), 0))
	}

	if ts0.LastIndexOf(f.MakeInt(1), -1) != 9 {
		t.Errorf("lastIndexOf(1) = %d", ts0.LastIndexOf(f.MakeInt(1), -1))
	}
	if ts0.LastIndexOf(f.MakeInt(0), -1) != 0 {
		t.Errorf("lastIndexOf(0) = %d", ts0.LastIndexOf(f.MakeInt(0), -1))
	}
	if ts0.LastIndexOf(f.MakeInt(10), -1) != -1 {
		t.Errorf("lastIndexOf(10) = %d", ts0.LastIndexOf(f.MakeInt(10), -1))
	}

	if ts2.Concat(ts3) != ts4 {
		t.Errorf("concat([1,2,3],[4,5,6]) != [1,2,3,4,5,6]")
	}
	if ts0.Concat(f.MakeListEmpty()) != ts0 {
		t.Errorf("concat with empty should be no-op")
	}
	if ts4.Append(f.MakeInt(7)) != ts5 {
		t.Errorf("append-1")
	}

	insLast := ts3.Insert(f.Parse("3"))
	insLast = insLast.Insert(f.Parse("2"))
	insLast = insLast.Insert(f.Parse("1"))
	if insLast != ts4 {
		t.Errorf("insert chain != [1,2,3,4,5,6]\n  got: %s\n  want: %s",
			insLast.String(), ts4.String())
	}

	if f.MakeListEmpty().Insert(f.Parse("1")) != f.Parse("[1]") {
		t.Errorf("insert-2")
	}
	if ts4.InsertAt(f.Parse("7"), ts4.GetLength()) != ts5 {
		t.Errorf("insertAt-end")
	}

	if ts5.GetPrefix() != ts4 {
		t.Errorf("prefix-1")
	}
	if ts5.GetLast() != f.Parse("7") {
		t.Errorf("last-1")
	}
}

func Test1_PatternMatch(t *testing.T) {
	f := NewFactory()
	t0 := f.Parse("f(1,2,3)")
	t1 := f.Parse("[1,2,3]")
	t2 := f.Parse(`f(a,"abc",2.3,<abc>)`)
	t3 := f.Parse("f(a,[])")

	if t0.Match("f(1,2,3)") == nil {
		t.Error("match f(1,2,3) ↔ f(1,2,3) should succeed")
	}

	r := t1.Match("<term>")
	if r == nil || r[0] != t1 {
		t.Errorf("<term> capture wrong: %v", r)
	}

	r = t1.Match("[<list>]")
	if r == nil || r[0] != t1 {
		t.Errorf("[<list>] capture wrong: %v", r)
	}

	r = t1.Match("[<int>,<list>]")
	if r == nil || r[0] != int64(1) || r[1] != f.Parse("[2,3]") {
		t.Errorf("[<int>,<list>] = %v", r)
	}

	r = f.Parse("f(a)").Match("f(<term>)")
	if r == nil || r[0] != f.Parse("a") {
		t.Errorf("f(<term>) capture: %v", r)
	}

	r = f.Parse("f(a)").Match("<term>")
	if r == nil || r[0] != f.Parse("f(a)") {
		t.Errorf("<term> capture of f(a): %v", r)
	}

	r = f.Parse("f(a)").Match("<fun(<term>)>")
	if r == nil || r[0] != "f" || r[1] != f.Parse("a") {
		t.Errorf("<fun(<term>)>: %v", r)
	}

	r = f.Parse("a").Match("<fun>")
	if r == nil || r[0] != "a" {
		t.Errorf("<fun>: %v", r)
	}

	r = t0.Match("f(1,<int>,3)")
	if r == nil || len(r) != 1 || r[0] != int64(2) {
		t.Errorf("f(1,<int>,3): %v", r)
	}

	r = t2.Match("f(<term>,<term>,<real>,<placeholder>)")
	if r == nil || len(r) != 4 {
		t.Errorf("f(<term>,<term>,<real>,<placeholder>): %v", r)
	} else {
		if r[0] != f.Parse("a") {
			t.Errorf("match4b: %v", r[0])
		}
		if r[1] != f.Parse(`"abc"`) {
			t.Errorf("match4c: %v", r[1])
		}
		if r[2] != 2.3 {
			t.Errorf("match4d: %v", r[2])
		}
	}

	r = t3.Match("f(<term>,[<list>])")
	if r == nil || len(r) != 2 || r[0] != f.Parse("a") {
		t.Errorf("f(<term>,[<list>]): %v", r)
	}
}

func Test1_PatternMake(t *testing.T) {
	f := NewFactory()

	r := f.Make("23", nil)
	if r != f.MakeInt(23) {
		t.Errorf("make(\"23\") = %v", r)
	}

	r = f.Make("3.14", nil)
	if r != f.MakeReal(3.14) {
		t.Errorf("make(\"3.14\") = %v", r)
	}

	r = f.Make("[1,2,3]", nil)
	if r != f.Parse("[1,2,3]") {
		t.Errorf("make(\"[1,2,3]\") = %v", r)
	}

	r = f.Make("<int>", []any{1})
	if r != f.MakeInt(1) {
		t.Errorf("make(\"<int>\", [1]) = %v", r)
	}

	r = f.Make("<real>", []any{3.14})
	if r != f.MakeReal(3.14) {
		t.Errorf("make(\"<real>\", [3.14]) = %v", r)
	}

	fab := f.Parse("f(a,b,c)")
	r = f.Make("<term>", []any{fab})
	if r != fab {
		t.Errorf("make(\"<term>\", [f(a,b,c)]) = %v", r)
	}

	r = f.Make("[<term>,<list>]", []any{
		f.Parse("1"),
		f.Parse("[]"),
	})
	if rl, ok := r.(*ATermList); !ok || rl.GetFirst() != f.Parse("1") || rl.GetLength() != 1 {
		t.Errorf("make-list: %v", r)
	}
}

// Test1_FactorySharing — pins the big invariant of aterm: every term
// built twice with the same shape ends up at the same Go pointer.
func Test1_FactorySharing(t *testing.T) {
	f := NewFactory()
	fun := f.MakeAFun("g", 2, false)
	a := f.MakeAppl(fun, f.MakeInt(1), f.MakeInt(2))
	b := f.MakeAppl(fun, f.MakeInt(1), f.MakeInt(2))
	if a != b {
		t.Errorf("equal applications must share")
	}
	c := f.Parse("g(1,2)")
	if c != a {
		t.Errorf("parsed g(1,2) must share with constructed g(1,2)")
	}
}
