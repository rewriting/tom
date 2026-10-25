package backend

import (
	"os"
	"strings"
	"testing"

	. "tom/engine/adt"
	"tom/engine/bridge"
	"tom/engine/platform"
	"tom/engine/tools"
)

// newTestGenerator is a generator writing on a string, with the options
// of a -go compilation.
func newTestGenerator(t *testing.T) (*goGenerator, *strings.Builder) {
	t.Helper()
	var sb strings.Builder
	opts := platform.NewOptions([]bridge.Option{
		{Name: "aCode", Kind: "bool", Value: "false"},
		{Name: "cCode", Kind: "bool", Value: "false"},
		{Name: "goCode", Kind: "bool", Value: "true"},
		{Name: "inline", Kind: "bool", Value: "false"},
		{Name: "inlineplus", Kind: "bool", Value: "false"},
		{Name: "jCode", Kind: "bool", Value: "false"},
		{Name: "noDeclaration", Kind: "bool", Value: "false"},
		{Name: "pCode", Kind: "bool", Value: "false"},
		{Name: "pretty", Kind: "bool", Value: "false"},
	})
	return newGoGenerator(tools.NewOutputCode(&sb, opts), opts, tools.NewSymbolTable()), &sb
}

func TestBuildComment(t *testing.T) {
	g, sb := newTestGenerator(t)
	g.buildComment(1, "a */ b")
	if want := "/* a * / b */ "; sb.String() != want {
		t.Fatalf("got %q, want %q", sb.String(), want)
	}
}

func TestDeclName(t *testing.T) {
	g, _ := newTestGenerator(t)
	g.setDeclPrefix("tom_Peano_")
	if got := g.declName("tom_make_suc"); got != "tom_Peano_make_suc" {
		t.Fatalf("declName(tom_make_suc) = %q", got)
	}
	if got := g.declName("next_minimal_extract"); got != "tom_Peano_next_minimal_extract" {
		t.Fatalf("declName(next_minimal_extract) = %q", got)
	}
}

func TestGenerateExpressionTrimsCode(t *testing.T) {
	g, sb := newTestGenerator(t)
	g.generateExpression(1, MakeCode(" \n x \t"), "default")
	if sb.String() != "x" {
		t.Fatalf("got %q", sb.String())
	}
}

func TestBuildIfForms(t *testing.T) {
	g, sb := newTestGenerator(t)
	g.generateInstruction(1, MakeIf(MakeTrueTL(), MakeNop(), MakeNop()), "default")
	if want := "if  true  {  }; "; sb.String() != want {
		t.Fatalf("If: got %q, want %q", sb.String(), want)
	}
	g, sb = newTestGenerator(t)
	g.buildDoWhile(1, MakeNop(), MakeFalseTL(), "default")
	if want := "for {  if !( false ) { break; }; }; "; sb.String() != want {
		t.Fatalf("DoWhile: got %q, want %q", sb.String(), want)
	}
	g, sb = newTestGenerator(t)
	g.buildNamedBlock(1, "matchlab", MakeConcInstruction(), "default")
	if want := "matchlab: for {  break matchlab; }; "; sb.String() != want {
		t.Fatalf("NamedBlock: got %q, want %q", sb.String(), want)
	}
}

func TestBuildExpConditionalType(t *testing.T) {
	g, sb := newTestGenerator(t)
	s := "int64"
	g.generateTypedExpression(1, &s, MakeConditional(MakeTrueTL(), MakeCode("1"), MakeCode("2")), "default")
	if want := "func() int64 { if  true  { return 1 }; return 2 }()"; sb.String() != want {
		t.Fatalf("got %q, want %q", sb.String(), want)
	}
	g, sb = newTestGenerator(t)
	g.buildExpConditional(1, MakeTrueTL(), MakeCode("1"), MakeCode("2"), "default")
	if want := "func() any { if  true  { return 1 }; return 2 }()"; sb.String() != want {
		t.Fatalf("got %q, want %q", sb.String(), want)
	}
}

func TestGenDeclArrayNeedsUsedDestructor(t *testing.T) {
	g, sb := newTestGenerator(t)
	g.genDeclArray("concX", "default") // not used: nothing (GoGenerator.t:678)
	if sb.String() != "" {
		t.Fatalf("got %q", sb.String())
	}
}

func TestTransformationRefused(t *testing.T) {
	g, _ := newTestGenerator(t)
	defer func() {
		e, ok := recover().(*platform.RuntimeError)
		if !ok || e.Msg != "%transformation is not supported with -go" {
			t.Fatalf("panic %v", e)
		}
	}()
	g.buildResolveClass("w", "t", "e", "default")
}

// portInventory are the Java methods of the inventory of plan J4.1b
// (column « Portée »): each must be named in a comment of the package.
var portInventory = []string{
	"BackendPlugin.t:110", "BackendPlugin.t:237", "BackendPlugin.t:264", "BackendPlugin.t:272",
	"BackendPlugin.t:282", "BackendPlugin.t:292", "BackendPlugin.t:301",
	"GoGenerator.t:91", "AbstractGenerator.t:67", "AbstractGenerator.t:74", "AbstractGenerator.t:78",
	"AbstractGenerator.t:82", "AbstractGenerator.t:86", "AbstractGenerator.t:90",
	"GoGenerator.t:96", "GoGenerator.t:101", "GoGenerator.t:106", "GoGenerator.t:113", "GoGenerator.t:121",
	"GoGenerator.t:131", "GoGenerator.t:153", "GoGenerator.t:170", "GoGenerator.t:183",
	"AbstractGenerator.t:104", "AbstractGenerator.t:150", "GoGenerator.t:213", "AbstractGenerator.t:166",
	"GoGenerator.t:835", "AbstractGenerator.t:216", "GoGenerator.t:840", "AbstractGenerator.t:237",
	"GoGenerator.t:198", "AbstractGenerator.t:258", "AbstractGenerator.t:433", "GoGenerator.t:291",
	"AbstractGenerator.t:461", "AbstractGenerator.t:596", "AbstractGenerator.t:618", "AbstractGenerator.t:635",
	"AbstractGenerator.t:908", "AbstractGenerator.t:914", "AbstractGenerator.t:922", "AbstractGenerator.t:930",
	"AbstractGenerator.t:942", "AbstractGenerator.t:950",
	"GoGenerator.t:246", "GoGenerator.t:346", "GenericGenerator.t:141", "GoGenerator.t:563",
	"GenericGenerator.t:587", "GoGenerator.t:537", "GoGenerator.t:557",
	"GenericGenerator.t:85", "GenericGenerator.t:135", "GenericGenerator.t:206", "GenericGenerator.t:270",
	"GenericGenerator.t:277", "GenericGenerator.t:154", "GenericGenerator.t:161", "GenericGenerator.t:168",
	"GenericGenerator.t:175", "GenericGenerator.t:284", "GenericGenerator.t:300", "GenericGenerator.t:315",
	"GenericGenerator.t:333", "GenericGenerator.t:409", "GenericGenerator.t:990", "GenericGenerator.t:995",
	"GenericGenerator.t:417", "GenericGenerator.t:467", "GenericGenerator.t:537", "GenericGenerator.t:886",
	"GoGenerator.t:227", "GoGenerator.t:250", "GoGenerator.t:258", "GoGenerator.t:266", "GoGenerator.t:274",
	"GoGenerator.t:308", "GoGenerator.t:313", "GoGenerator.t:326", "GoGenerator.t:332", "GoGenerator.t:340",
	"GoGenerator.t:354", "GoGenerator.t:358", "GoGenerator.t:362", "GoGenerator.t:383", "GoGenerator.t:389",
	"GoGenerator.t:397", "GoGenerator.t:411", "GoGenerator.t:432", "GoGenerator.t:444", "GoGenerator.t:449",
	"GoGenerator.t:473", "GoGenerator.t:512", "GoGenerator.t:520", "GoGenerator.t:529", "GoGenerator.t:573",
	"GenericGenerator.t:182", "GenericGenerator.t:340", "GenericGenerator.t:350", "GenericGenerator.t:360",
	"GenericGenerator.t:370", "GenericGenerator.t:387", "GenericGenerator.t:398", "GenericGenerator.t:609",
	"GenericGenerator.t:646", "GenericGenerator.t:685", "GenericGenerator.t:721", "GoGenerator.t:663",
	"GenericGenerator.t:759", "GenericGenerator.t:797", "GenericGenerator.t:817", "GoGenerator.t:238",
	"GoGenerator.t:618", "GoGenerator.t:677",
	"GoGenerator.t:703", "GoGenerator.t:707", "GoGenerator.t:711", "GoGenerator.t:756", "GoGenerator.t:850",
	"GenericGenerator.t:447", "GenericGenerator.t:508", "GoGenerator.t:860", "GoGenerator.t:864",
	"GoGenerator.t:868", "GoGenerator.t:872", "GoGenerator.t:876", "GoGenerator.t:880", "GoGenerator.t:884",
	"GoGenerator.t:888", "GoGenerator.t:892", "GoGenerator.t:896", "GoGenerator.t:900",
}

// packageSources is the text of the hand-written sources of the package
// (.t and .go, generated .t.go and tests excluded).
func packageSources(t *testing.T) string {
	t.Helper()
	entries, err := os.ReadDir(".")
	if err != nil {
		t.Fatal(err)
	}
	var sb strings.Builder
	for _, e := range entries {
		n := e.Name()
		if strings.HasSuffix(n, ".t.go") || strings.HasSuffix(n, "_test.go") ||
			!(strings.HasSuffix(n, ".t") || strings.HasSuffix(n, ".go")) {
			continue
		}
		b, err := os.ReadFile(n)
		if err != nil {
			t.Fatal(err)
		}
		sb.Write(b)
	}
	return sb.String()
}

// TestPortInventory: every method of the inventory is ported, with its
// Java reference in a comment (rule R2).
func TestPortInventory(t *testing.T) {
	src := packageSources(t)
	for _, ref := range portInventory {
		if !strings.Contains(src, "("+ref+")") && !strings.Contains(src, "("+ref+",") && !strings.Contains(src, " "+ref) {
			t.Errorf("%s: no comment names it", ref)
		}
	}
}

// TestNoStubLeft: no method of the backend is a stub any more.
func TestNoStubLeft(t *testing.T) {
	if strings.Contains(packageSources(t), "notPorted(") {
		t.Fatal("a stub (notPorted) is left in package backend")
	}
}
