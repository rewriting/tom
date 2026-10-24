package tools

import (
	"fmt"
	"testing"

	"tom/engine/adt"
)

func TestNewType(t *testing.T) {
	got := NewType(adt.MakeConcTypeOption(), "int", "int64")
	if want := `Type(concTypeOption(),"int",TLType("int64"))`; fmt.Sprint(got) != want {
		t.Fatalf("got %v, want %s", got, want)
	}
	if got != adt.MakeType(adt.MakeConcTypeOption(), "int", adt.MakeTLType("int64")) {
		t.Fatal("NewType is not maximally shared")
	}
}

func TestMakeSingleLineCode(t *testing.T) {
	if got := MakeSingleLineCode("a\n\tb\r", false); got != "a  b " {
		t.Fatalf("got %q", got)
	}
	if got := MakeSingleLineCode("a\n\tb\r", true); got != "a\n\tb\r" {
		t.Fatalf("pretty: got %q", got)
	}
}
