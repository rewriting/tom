package backend

import (
	"strings"
	"testing"
)

// The Tom code generated for rules is RuleExpander's (Java Gom).
func TestRuleHookCode(t *testing.T) {
	rules, err := parseRules(`
    g(x,y) -> a() if x == a() && y == b()
    // a comment
    f(x) -> a() if x == a() || x == b()
    h(x,y,z) -> a() if ((x == a() && y != b()) || (y != c() && z == a()))
    Fib1(res@Suc(Zero())) -> res
    k(_*,a(),_*) -> "s"
    conc() -> conc(a())
    conc(C1*,f(b()),C2*) -> conc(C1*,f(c()),C2*) if x >= 12
  `)
	if err != nil {
		t.Fatal(err)
	}
	if len(rules) != 7 {
		t.Fatalf("%d rules, want 7", len(rules))
	}
	got := ruleHookCode([]string{"Term", "Term"}, rules[:1])
	want := "    %match(Term arg_1, Term arg_2) {\nx, y && (x == a() && y == b()) -> { return `a(); }\n    }\n"
	if got != want {
		t.Errorf("g:\n%q\nwant\n%q", got, want)
	}
	got = ruleHookCode([]string{"Term"}, rules[1:2])
	if want := "    %match(Term arg_1) {\nx && (x == a() || x == b()) -> { return `a(); }\n    }\n"; got != want {
		t.Errorf("f:\n%q\nwant\n%q", got, want)
	}
	got = ruleHookCode([]string{"Term", "Term", "Term"}, rules[2:3])
	if !strings.Contains(got, "x, y, z && ((x == a() && y != b()) || (y != c() && z == a())) -> { return `a(); }") {
		t.Errorf("h: %q", got)
	}
	if got = ruleHookCode([]string{"Term"}, rules[3:4]); !strings.Contains(got, "res@Suc(Zero()) -> { return `res; }") {
		t.Errorf("Fib1: %q", got)
	}
	if got = ruleVariadicHookCode("RealMakeConsK", rules[4:5]); got != "    %match(RealMakeConsK(head,tail)) {\nk(_*, a(), _*) -> { return `\"s\"; }\n    }\n" {
		t.Errorf("k: %q", got)
	}
	if got = ruleHookCode(nil, rules[5:6]); got != "    return `conc(a());\n" {
		t.Errorf("conc(): %q", got)
	}
	if got = ruleVariadicHookCode("RealMakeConsConc", rules[6:]); !strings.Contains(got, "conc(C1*, f(b()), C2*) && x >= 12 -> { return `conc(C1*, f(c()), C2*); }") {
		t.Errorf("conc: %q", got)
	}
	for _, r := range rules {
		if ruleHead(r) == "" {
			t.Errorf("no head for %+v", r.lhs)
		}
	}
	if _, err := parseRules("f(x) -> "); err == nil {
		t.Error("a truncated rule is accepted")
	}
}
