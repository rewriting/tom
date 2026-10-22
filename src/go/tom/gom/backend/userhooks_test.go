package backend

import "testing"

// A `//` comment of a hook body becomes a block comment in the mapping,
// whose host code Tom emits on a single line.
func TestMappingHostCode(t *testing.T) {
	for in, want := range map[string]string{
		"a() -> { return l; } // keep\nb()": "a() -> { return l; } /* keep */\nb()",
		`s := "http://x" // c`:              `s := "http://x" /* c */`,
		"x := '/' // a */ b":                "x := '/' /* a * / b */",
		"/* already */ y // z":              "/* already */ y /* z */",
		"no comment":                        "no comment",
		"`f(x) // c\n":                      "`f(x) /* c */\n",
	} {
		if got := mappingHostCode(in); got != want {
			t.Errorf("mappingHostCode(%q) = %q, want %q", in, got, want)
		}
	}
}
