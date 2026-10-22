package aterm

import "testing"

func TestSharingAndParse(t *testing.T) {
	f := NewPureFactory(16)
	a := f.MakeAppl(f.MakeAFun("a", 0, false))
	fa := f.MakeAppl(f.MakeAFun("f", 1, false), a)
	if f.Parse("f(a)") != ATerm(fa) {
		t.Fatal("parse is not shared with make")
	}
	l := f.Parse("[a, f(a)]").(*ATermList)
	if l != f.MakeList().Insert(fa).Insert(a) {
		t.Fatal("lists are not shared")
	}
	if l.String() != "[a,f(a)]" || l.GetLength() != 2 || !l.GetNext().GetNext().IsEmpty() {
		t.Fatalf("bad list %v", l)
	}
	if f.Parse("[]") != ATerm(f.MakeList()) {
		t.Fatal("empty list not shared")
	}
}
