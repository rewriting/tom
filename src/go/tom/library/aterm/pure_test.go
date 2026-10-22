package aterm

import "testing"

func TestGetInstanceIsSingleton(t *testing.T) {
	if GetInstance() != GetInstance() {
		t.Fatal("GetInstance must return the same factory")
	}
	f := GetInstance()
	a := f.MakeAppl(f.MakeAFun("a", 0, false))
	if f.Parse("a") != ATerm(a) {
		t.Fatal("singleton factory must share a")
	}
}

func TestNewPureFactoryIsFresh(t *testing.T) {
	f := NewPureFactory(16)
	if f == GetInstance() || f == NewPureFactory(16) {
		t.Fatal("NewPureFactory must return a fresh factory")
	}
	if f.MakeListEmpty() == nil || !f.MakeListEmpty().IsEmpty() {
		t.Fatal("fresh factory has an empty list")
	}
}

func TestApplGetName(t *testing.T) {
	f := NewPureFactory(16)
	a := f.Parse("f(a,b)").(*ATermAppl)
	if a.GetName() != "f" || a.GetName() != a.Name() || a.GetAFun().Name() != "f" {
		t.Fatalf("GetName = %q", a.GetName())
	}
}
