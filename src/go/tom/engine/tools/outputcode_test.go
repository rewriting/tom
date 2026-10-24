package tools

import (
	"bufio"
	"errors"
	"strings"
	"testing"

	"tom/engine/bridge"
	"tom/engine/platform"
)

// goOptions are the options of a -go compilation, pretty or not.
func goOptions(pretty bool) *platform.Options {
	p := "false"
	if pretty {
		p = "true"
	}
	return platform.NewOptions([]bridge.Option{
		{Name: "aCode", Kind: "bool", Value: "false"},
		{Name: "cCode", Kind: "bool", Value: "false"},
		{Name: "goCode", Kind: "bool", Value: "true"},
		{Name: "pCode", Kind: "bool", Value: "false"},
		{Name: "pretty", Kind: "bool", Value: p},
	})
}

func newGo(pretty bool) (*OutputCode, *strings.Builder) {
	var sb strings.Builder
	return NewOutputCode(&sb, goOptions(pretty)), &sb
}

func TestWriteTLLineSync(t *testing.T) {
	o, sb := newGo(false)
	o.WriteTL(0, "f()", 3, 0)                // padded to line 3
	o.WriteTL(0, "a := 1\nreturn a\n", 3, 2) // at its line
	o.WriteTL(0, "x\ny", 4, 1)               // line 4 is behind: single line
	if want := "\n\nf()a := 1\nreturn a\nx; y"; sb.String() != want {
		t.Fatalf("got %q, want %q", sb.String(), want)
	}
}

func TestSingleLineContinuesOutputLine(t *testing.T) {
	o, sb := newGo(false)
	o.Write("f(x)")
	o.SetSingleLine()
	o.WriteTL(0, "\ny", 1, 1)
	o.UnsetSingleLine()
	if want := "f(x); y"; sb.String() != want {
		t.Fatalf("got %q, want %q", sb.String(), want)
	}
	o, sb = newGo(false)
	o.SetSingleLine()
	o.WriteTL(0, "a\tb\r\nc", 1, 1)
	if want := "a b ; c"; sb.String() != want {
		t.Fatalf("got %q, want %q", sb.String(), want)
	}
}

func TestGoSingleLine(t *testing.T) {
	cases := []struct{ before, s, want string }{
		{"", "x\ny", "x; y"},
		{"", "return\nx", "return; x"},
		{"", "else\nx", "else x"},
		{"", "go\nx", "go x"},
		{"", "{\nx", "{ x"},
		{"", "a++\nb", "a++; b"},
		{"", "a\n;b", "a ;b"},
		{"", "a\n\n;b", "a  ;b"},
		{"", "s := \"x\"\nt", "s := \"x\"; t"},
		{"", "m[i]\n}", "m[i]; }"},
		{"", "x  \t\ny", "x  \t; y"},
		{"", "case 1:\nx", "case 1: x"},
		{"", "x\n", "x; "},
		{"", "été\nx", "été; x"},
		{"return a +", "\nb", " b"},
		{"f(x)", "\ny", "; y"},
	}
	for _, c := range cases {
		if got := goSingleLine(c.before, c.s); got != c.want {
			t.Errorf("goSingleLine(%q, %q) = %q, want %q", c.before, c.s, got, c.want)
		}
	}
}

func TestStringOutputCode(t *testing.T) {
	o := NewStringOutputCode()
	o.WriteTL(0, "x\ny", 0, 1) // no goCode: newlines become spaces
	o.WriteInt(-42)
	o.WriteOpenBrace()
	o.WriteComa()
	o.WriteCloseBrace()
	o.WriteDeep(3, "z")
	o.Writeln() // not pretty: nothing
	if got := o.StringDump(); got != "x y-42(,)z" {
		t.Fatalf("got %q", got)
	}
}

func TestPretty(t *testing.T) {
	for _, c := range []struct{ s, want string }{
		{"  a\n\n  b\n\n", "a\n\nb\n"},
		{"\n\n", ""},
		{"", "\n"},
	} {
		o, sb := newGo(true)
		o.WriteTL(2, c.s, 1, int64(strings.Count(c.s, "\n")))
		if sb.String() != c.want {
			t.Errorf("WriteTL(%q) = %q, want %q", c.s, sb.String(), c.want)
		}
	}
	o, sb := newGo(true)
	o.Writeln()
	if sb.String() != "\n" {
		t.Fatalf("Writeln = %q", sb.String())
	}
}

type failingWriter struct{ n int }

func (w *failingWriter) Write(p []byte) (int, error) {
	w.n++
	return 0, errors.New("disk full")
}

func TestCloseAndErrors(t *testing.T) {
	var sb strings.Builder
	bw := bufio.NewWriter(&sb)
	o := NewOutputCode(bw, goOptions(false))
	o.Write("abc")
	if err := o.Close(); err != nil || sb.String() != "abc" {
		t.Fatalf("Close: %v, %q", err, sb.String())
	}
	fw := &failingWriter{}
	o = NewOutputCode(fw, goOptions(false))
	o.Write("a")
	o.Write("b")
	if err := o.Close(); err == nil || err.Error() != "disk full" || fw.n != 1 {
		t.Fatalf("Close: %v after %d writes", err, fw.n)
	}
	defer func() {
		if _, ok := recover().(*platform.RuntimeError); !ok {
			t.Fatal("StringDump of a non-string OutputCode must panic with a *platform.RuntimeError")
		}
	}()
	o.StringDump()
}
