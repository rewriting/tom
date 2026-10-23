package main

import (
	"strings"
	"testing"
)

func TestFilePrefix(t *testing.T) {
	for in, want := range map[string]string{
		"acmatching_TestAC.go":         "tom_TestAC_",
		"gom_fromterm_TestFromTerm.go": "tom_TestFromTerm_",
		"cfib1.go":                     "tom_cfib1_",
		"a-b.go":                       "tom_a_b_",
	} {
		if got := filePrefix(in); got != want {
			t.Errorf("filePrefix(%q) = %q, want %q", in, got, want)
		}
	}
}

const pfx = "tom_X_"

func TestCheckPair(t *testing.T) {
	old := `package main; func tom_make_a() int { return 1 }; func main() { _ = tom_make_a(); var tom_a int; _ = tom_a }`
	cases := []struct {
		name, old, new string
		ok             bool
	}{
		{"rename", old,
			`package main; func tom_X_make_a() int { return 1 }; func main() { _ = tom_X_make_a(); var tom_a int; _ = tom_a }`, true},
		{"local renamed too", old,
			`package main; func tom_X_make_a() int { return 1 }; func main() { _ = tom_X_make_a(); var tom_X_a int; _ = tom_X_a }`, false},
		{"unrenamed", old, old, false},
		{"literal changed", old,
			`package main; func tom_X_make_a() int { return 2 }; func main() { _ = tom_X_make_a(); var tom_a int; _ = tom_a }`, false},
		{"closure",
			`package main; func tom_stringHead(s string) rune { for _, c := range s { return c }; return 0 }; func main() { _ = tom_stringHead("x") }`,
			`package main; func main() { _ = func(s string) rune { for _, c := range s { return c }; return 0 }("x") }`, true},
		{"closure altered",
			`package main; func tom_stringHead(s string) rune { for _, c := range s { return c }; return 0 }; func main() { _ = tom_stringHead("x") }`,
			`package main; func main() { _ = func(s string) rune { for _, c := range s { return c }; return 1 }("x") }`, false},
		{"helper and underscores",
			`package main; func next_minimal_extract() {}; func __h_f() {}; func tom__strat() {}; func tom_new_S() {}; func main() { next_minimal_extract(); __h_f(); tom__strat(); tom_new_S() }`,
			`package main; func tom_X_next_minimal_extract() {}; func tom_X___h_f() {}; func tom__strat() {}; func tom_new_S() {}; func main() { tom_X_next_minimal_extract(); tom_X___h_f(); tom__strat(); tom_new_S() }`, true},
	}
	for _, c := range cases {
		err := checkPair([]byte(c.old), []byte(c.new), pfx)
		if (err == nil) != c.ok {
			t.Errorf("%s: err = %v, want ok=%v", c.name, err, c.ok)
		}
	}
}

func TestErrorContext(t *testing.T) {
	err := checkPair([]byte(`package main; var x = 1`), []byte(`package main; var x = 2`), pfx)
	if err == nil || !strings.Contains(err.Error(), "1") || !strings.Contains(err.Error(), "2") {
		t.Fatalf("unexpected error %v", err)
	}
}
