package aterm

import "testing"

// Java's Double.toString / Float.toString (values checked with a JVM).
func TestJavaDouble(t *testing.T) {
	for v, want := range map[float64]string{
		20.3: "20.3", 21: "21.0", 1e10: "1.0E10", 1e-5: "1.0E-5", -2.5: "-2.5",
		1234567: "1234567.0", 1e7: "1.0E7", 0.001: "0.001", 123456789.5: "1.234567895E8",
		0: "0.0",
	} {
		if got := JavaDouble(v); got != want {
			t.Errorf("JavaDouble(%v) = %q, want %q", v, got, want)
		}
	}
	for v, want := range map[float32]string{20.3: "20.3", 21: "21.0", 1e10: "1.0E10", 3.4028235e38: "3.4028235E38"} {
		if got := JavaFloat(v); got != want {
			t.Errorf("JavaFloat(%v) = %q, want %q", v, got, want)
		}
	}
}

// The builtin slot conversions round-trip through the ATerm of the slot.
func TestGomConversions(t *testing.T) {
	id := IdConverter{}
	if GomInt(MakeGomInt(42), id) != 42 || GomLong(MakeGomLong(1<<40), id) != 1<<40 ||
		GomDouble(MakeGomReal(20.3), id) != 20.3 || GomFloat(MakeGomReal(float64(float32(20.3))), id) != float32(20.3) ||
		!GomBoolean(MakeGomBoolean(true), id) || GomBoolean(MakeGomBoolean(false), id) ||
		GomChar(MakeGomChar('a'), id) != 'a' || GomString(MakeGomString("x\ty"), id) != "x\ty" {
		t.Error("a builtin conversion does not round-trip")
	}
	if GomChar(GetInstance().Parse("49"), id) != 'a' || GomString(GetInstance().Parse(`"Hello"`), id) != "Hello" {
		t.Error("Char(49) / \"Hello\" not read back")
	}
	defer func() {
		if e, ok := recover().(*GomError); !ok || e.Msg != "Not an Int : 4.5" || e.IllegalArgument {
			t.Errorf("panic = %v", e)
		}
	}()
	GomInt(GetInstance().Parse("4.5"), id)
}

func TestMakeListFromSlice(t *testing.T) {
	f := GetInstance()
	if got := f.MakeListFromSlice([]ATerm{f.MakeInt(1), f.MakeInt(2)}); got != f.Parse("[1,2]") {
		t.Errorf("MakeListFromSlice = %v", got)
	}
}

// \f and \b in a quoted symbol are a form feed and a backspace, as in
// Java's aterm reader (Gom's toString writes them so).
func TestParseQuotedFormFeed(t *testing.T) {
	at := GetInstance().Parse(`"a\fb\bc\td"`)
	if got := GomString(at, IdConverter{}); got != "a\fb\bc\td" {
		t.Errorf("parsed %q", got)
	}
}
