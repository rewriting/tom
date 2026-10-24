package backend

import (
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
