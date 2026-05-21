package aterm

import (
	"fmt"
	"testing"
)

// Test2_Dict — port of Test2.testDict: insert key0..key4 and check
// that key3 maps to value3.
func Test2_Dict(t *testing.T) {
	f := NewFactory()
	dict := f.MakeListEmpty()
	for i := 0; i < 5; i++ {
		key := f.Parse(fmt.Sprintf("key%d", i))
		value := f.Parse(fmt.Sprintf("value%d", i))
		dict = dict.DictPut(key, value)
	}
	got := dict.DictGet(f.Parse("key3"))
	want := f.Parse("value3")
	if got != want {
		t.Errorf("dictGet(key3) = %v, want %v", got, want)
	}
}

// Test2_Annos — port of Test2.testAnnos: stack 5 annotations on a
// term, look one up, remove it, and confirm it's gone.
func Test2_Annos(t *testing.T) {
	f := NewFactory()
	term := f.Parse("f").(*ATermAppl)
	var current ATerm = term
	for i := 0; i < 5; i++ {
		key := f.Parse(fmt.Sprintf("key%d", i))
		value := f.Parse(fmt.Sprintf("value%d", i))
		appl, ok := current.(*ATermAppl)
		if !ok {
			t.Fatalf("expected appl at step %d", i)
		}
		current = appl.SetAnnotation(key, value)
	}
	annoOwner := current.(*ATermAppl)

	key := f.Parse("key3")
	value := f.Parse("value3")
	if annoOwner.GetAnnotation(key) != value {
		t.Errorf("getAnnotation(key3) = %v, want %v", annoOwner.GetAnnotation(key), value)
	}
	after := annoOwner.RemoveAnnotation(key)
	if after.(*ATermAppl).GetAnnotation(key) != nil {
		t.Error("removed annotation should not be retrievable")
	}
}

// Test2_AnnosShared — annotated terms still share. Building the same
// `f{[k,v]}` twice in a row should yield the same hash-consed
// pointer.
func Test2_AnnosShared(t *testing.T) {
	f := NewFactory()
	term := f.Parse("f").(*ATermAppl)
	key := f.Parse("k")
	value := f.Parse("v")
	a := term.SetAnnotation(key, value)
	b := term.SetAnnotation(key, value)
	if a != b {
		t.Errorf("equal annotation-bearing applications must share: %v vs %v", a, b)
	}
}

// Test2_Parser — every case from Test2.testParser must parse without
// panicking. (We deliberately don't pin String() shapes here — most
// of these cases test that the parser accepts them at all.)
func Test2_Parser(t *testing.T) {
	f := NewFactory()
	cases := []string{
		"f",
		"f(1)",
		"f(1,2)",
		"[]",
		"[1]",
		"[1,2]",
		"<x>",
		"3.14",
		`f("x y z"(),<abc(31)>,[])`,
		`home([<name("",String)>,<phone("",PhoneNumber)>])`,
		"[ a , b ]",
		"f(a){[x,y],[1,2]}",
		"[(),(a)]",
	}
	for _, src := range cases {
		t.Run(src, func(t *testing.T) {
			defer func() {
				if r := recover(); r != nil {
					t.Fatalf("parse(%q) panicked: %v", src, r)
				}
			}()
			term := f.Parse(src)
			if term == nil {
				t.Fatalf("parse(%q) returned nil", src)
			}
		})
	}
}

// Test2_List — exercises remove / replace / append on lists.
func Test2_List(t *testing.T) {
	f := NewFactory()

	list := f.Parse("[1,2,3]").(*ATermList)
	got := list.Remove(f.Parse("2"))
	want := f.Parse("[1,3]")
	if ATerm(got) != want {
		t.Errorf("remove(2) = %v, want %v", got, want)
	}

	list = f.Parse("[1,2,3]").(*ATermList)
	got = list.Replace(f.Parse("99"), 1)
	want = f.Parse("[1,99,3]")
	if ATerm(got) != want {
		t.Errorf("replace(99,1) = %v, want %v", got, want)
	}

	empty := f.MakeListEmpty()
	got = empty.Append(f.Parse("1"))
	want = f.Parse("[1]")
	if ATerm(got) != want {
		t.Errorf("append(1) on empty = %v, want %v", got, want)
	}

	if ATerm(f.MakeListEmpty()) != f.Parse("[]") {
		t.Error("parsed [] should be the empty list singleton")
	}
}

// Test2_Match — node-shape pattern with multiple captures (mix of
// types).
func Test2_Match(t *testing.T) {
	f := NewFactory()
	term := f.Parse(`node("Pico-eval",box,182,21,62,26)`)
	result := term.Match(`node(<str>,<fun>,<int>,<int>,<int>,<int>)`)
	if result == nil {
		t.Fatal("node match returned nil")
	}
	if len(result) != 6 {
		t.Fatalf("node match captured %d values, want 6", len(result))
	}
	if result[0] != "Pico-eval" {
		t.Errorf("capture[0] = %v, want \"Pico-eval\"", result[0])
	}
	if result[1] != "box" {
		t.Errorf("capture[1] = %v, want \"box\"", result[1])
	}
	for i, expected := range []int64{182, 21, 62, 26} {
		if result[2+i] != expected {
			t.Errorf("capture[%d] = %v, want %d", 2+i, result[2+i], expected)
		}
	}
}
