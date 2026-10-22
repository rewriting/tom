package tomparser

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"testing"
)

// parseFixture reads tomgo/tests/testdata/parse/<name>/scenario.t and parses it via
// the hand-rolled Go parser, returning the canonical String() form with all
// absolute paths normalised: scenario.t → __INPUT__, its containing directory
// → __DIR__. Same convention as the tomparseq cross-language harness.
func parseFixture(t *testing.T, name string) string {
	t.Helper()
	path := "../../../../tests/testdata/parse/" + name + "/scenario.t"
	abs, err := filepath.Abs(path)
	if err != nil {
		t.Fatalf("abs %s: %v", path, err)
	}
	src, err := os.ReadFile(abs)
	if err != nil {
		t.Fatalf("read %s: %v", abs, err)
	}
	code, err := Parse(string(src), abs)
	if err != nil {
		t.Fatalf("Parse %s: %v", abs, err)
	}
	got := fmt.Sprintf("%v", code)
	got = strings.ReplaceAll(got, abs, "__INPUT__")
	got = strings.ReplaceAll(got, filepath.Dir(abs), "__DIR__")
	return got
}

func TestParseSkeleton(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Skeleton {\n  ",TextPosition(1,1),TextPosition(2,3))),DeclarationToCode(TypeTermDecl(Name("Foo"),concDeclaration(),OriginTracking(Name("Foo"),2,"__INPUT__"))),TargetLanguageToCode(TL("\n}\n",TextPosition(4,4),TextPosition(5,1)))))`
	got := parseFixture(t, "skeleton")
	if got != expected {
		t.Errorf("AST mismatch on skeleton.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseOpNoargs(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class OpNoargs {\n  ",TextPosition(1,1),TextPosition(2,3))),DeclarationToCode(TypeTermDecl(Name("Sort"),concDeclaration(),OriginTracking(Name("Sort"),2,"__INPUT__"))),DeclarationToCode(SymbolDecl(Name("foo"))),TargetLanguageToCode(TL("\n}\n",TextPosition(6,4),TextPosition(7,1)))))`
	got := parseFixture(t, "op_noargs")
	if got != expected {
		t.Errorf("AST mismatch on op_noargs.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseTypetermExtends(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class TypetermExtends {\n  ",TextPosition(1,1),TextPosition(2,3))),DeclarationToCode(TypeTermDecl(Name("Base"),concDeclaration(),OriginTracking(Name("Base"),2,"__INPUT__"))),DeclarationToCode(TypeTermDecl(Name("Derived"),concDeclaration(),OriginTracking(Name("Derived"),5,"__INPUT__"))),TargetLanguageToCode(TL("\n}\n",TextPosition(7,4),TextPosition(8,1)))))`
	got := parseFixture(t, "typeterm_extends")
	if got != expected {
		t.Errorf("AST mismatch on typeterm_extends.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseOpSlots(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class OpSlots {\n  ",TextPosition(1,1),TextPosition(2,3))),DeclarationToCode(TypeTermDecl(Name("Nat"),concDeclaration(),OriginTracking(Name("Nat"),2,"__INPUT__"))),DeclarationToCode(SymbolDecl(Name("zero"))),DeclarationToCode(SymbolDecl(Name("succ"))),DeclarationToCode(SymbolDecl(Name("plus"))),TargetLanguageToCode(TL("\n}\n",TextPosition(10,4),TextPosition(11,1)))))`
	got := parseFixture(t, "op_slots")
	if got != expected {
		t.Errorf("AST mismatch on op_slots.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseOplistOparray(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class OplistOparray {\n  ",TextPosition(1,1),TextPosition(2,3))),DeclarationToCode(TypeTermDecl(Name("E"),concDeclaration(),OriginTracking(Name("E"),2,"__INPUT__"))),DeclarationToCode(TypeTermDecl(Name("L"),concDeclaration(),OriginTracking(Name("L"),5,"__INPUT__"))),DeclarationToCode(TypeTermDecl(Name("A"),concDeclaration(),OriginTracking(Name("A"),8,"__INPUT__"))),DeclarationToCode(ListSymbolDecl(Name("conslist"))),DeclarationToCode(ArraySymbolDecl(Name("consarray"))),TargetLanguageToCode(TL("\n}\n",TextPosition(14,4),TextPosition(15,1)))))`
	got := parseFixture(t, "oplist_oparray")
	if got != expected {
		t.Errorf("AST mismatch on oplist_oparray.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseIncludeLocal(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class IncludeLocal {\n  ",TextPosition(1,1),TextPosition(2,3))),TomInclude(concCode(InstructionToCode(AbstractBlock(concInstruction(CodeToInstruction(DeclarationToCode(TypeTermDecl(Name("Included"),concDeclaration(),OriginTracking(Name("Included"),1,"__DIR__/inc.tom"))))))))),TargetLanguageToCode(TL("\n}\n",TextPosition(2,23),TextPosition(3,1)))))`
	got := parseFixture(t, "include_local")
	if got != expected {
		t.Errorf("AST mismatch on include_local.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseWaterMulti(t *testing.T) {
	const expected = `Tom(concCode(DeclarationToCode(TypeTermDecl(Name("A"),concDeclaration(),OriginTracking(Name("A"),1,"__INPUT__"))),TargetLanguageToCode(TL("\nb\n\nc\n\n\n\nd\n",TextPosition(3,2),TextPosition(8,1))),DeclarationToCode(TypeTermDecl(Name("E"),concDeclaration(),OriginTracking(Name("E"),8,"__INPUT__")))))`
	got := parseFixture(t, "water_multi")
	if got != expected {
		t.Errorf("AST mismatch on water_multi.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0b(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0 {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),EmptyName(),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0b")
	if got != expected {
		t.Errorf("AST mismatch on match0b.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0cNamed(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0c {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0c_named")
	if got != expected {
		t.Errorf("AST mismatch on match0c_named.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0dAppl(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0d {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(TermAppl(concOption(),concTomName(Name("Foo")),concTomTerm(),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0d_appl")
	if got != expected {
		t.Errorf("AST mismatch on match0d_appl.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0eApplArgs(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0e {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(TermAppl(concOption(),concTomName(Name("Foo")),concTomTerm(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),TermAppl(concOption(),concTomName(Name("Bar")),concTomTerm(),concConstraint())),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0e_appl_args")
	if got != expected {
		t.Errorf("AST mismatch on match0e_appl_args.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0fMulti(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0f {\n  public void f(Object a, Object b) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(AndConstraint(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("a"),3,"__INPUT__"),ModuleName("default")),Name("a"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),MatchConstraint(Variable(concOption(),Name("y"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("b"),3,"__INPUT__"),ModuleName("default")),Name("b"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()))),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0f_multi")
	if got != expected {
		t.Errorf("AST mismatch on match0f_multi.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0gRules(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0g {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),EmptyName(),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__"))),ConstraintInstruction(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),5,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(6,6),TextPosition(9,1)))))`
	got := parseFixture(t, "match0g_rules")
	if got != expected {
		t.Errorf("AST mismatch on match0g_rules.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0hBody(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0h {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),EmptyName(),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction(CodeToInstruction(TargetLanguageToCode(TL(" doSomething(); ",TextPosition(4,13),TextPosition(4,29)))))),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0h_body")
	if got != expected {
		t.Errorf("AST mismatch on match0h_body.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0iExplicit(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0i {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(TermAppl(concOption(),concTomName(Name("Foo")),concTomTerm(),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),4,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0i_explicit")
	if got != expected {
		t.Errorf("AST mismatch on match0i_explicit.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0jStar(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0j {\n  public void f(Object a, Object b) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(AndConstraint(MatchConstraint(VariableStar(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("a"),3,"__INPUT__"),ModuleName("default")),Name("a"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),MatchConstraint(VariableStar(concOption(),EmptyName(),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("b"),3,"__INPUT__"),ModuleName("default")),Name("b"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()))),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0j_star")
	if got != expected {
		t.Errorf("AST mismatch on match0j_star.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0kAnnot(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0k {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(TermAppl(concOption(),concTomName(Name("Foo")),concTomTerm(Variable(concOption(),Name("a"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint(AliasTo(Variable(concOption(OriginTracking(Name("x"),0,"unknown file")),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()))))),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0k_annot")
	if got != expected {
		t.Errorf("AST mismatch on match0k_annot.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0lAnti(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0l {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(AntiTerm(TermAppl(concOption(),concTomName(Name("Foo")),concTomTerm(),concConstraint())),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0l_anti")
	if got != expected {
		t.Errorf("AST mismatch on match0l_anti.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0mOr(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0m {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(TermAppl(concOption(),concTomName(Name("Foo"),Name("Bar")),concTomTerm(),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0m_or")
	if got != expected {
		t.Errorf("AST mismatch on match0m_or.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0nBqAppl(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0n {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQAppl(concOption(OriginTracking(Name("Foo"),4,"__INPUT__"),ModuleName("default")),Name("Foo"),concBQTerm()),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0n_bqappl")
	if got != expected {
		t.Errorf("AST mismatch on match0n_bqappl.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0oBqBody(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0o {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction(BQTermToInstruction(BQVariable(concOption(OriginTracking(Name("x"),4,"__INPUT__"),ModuleName("default")),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()))))),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0o_bqbody")
	if got != expected {
		t.Errorf("AST mismatch on match0o_bqbody.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0pBqBodyWater(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0p {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction(CodeToInstruction(TargetLanguageToCode(TL(" return ",TextPosition(4,13),TextPosition(4,21)))),BQTermToInstruction(BQVariable(concOption(OriginTracking(Name("x"),4,"__INPUT__"),ModuleName("default")),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()))),CodeToInstruction(TargetLanguageToCode(TL("; ",TextPosition(4,23),TextPosition(4,25)))))),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0p_bqbody_water")
	if got != expected {
		t.Errorf("AST mismatch on match0p_bqbody_water.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0qBqApplBody(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0q {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction(CodeToInstruction(TargetLanguageToCode(TL(" return ",TextPosition(4,13),TextPosition(4,21)))),BQTermToInstruction(BQAppl(concOption(OriginTracking(Name("Foo"),4,"__INPUT__"),ModuleName("default")),Name("Foo"),concBQTerm(BQVariable(concOption(OriginTracking(Name("x"),4,"__INPUT__"),ModuleName("default")),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()))))),CodeToInstruction(TargetLanguageToCode(TL("; ",TextPosition(4,28),TextPosition(4,30)))))),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0q_bqappl_body")
	if got != expected {
		t.Errorf("AST mismatch on match0q_bqappl_body.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0rBqMulti(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0r {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction(CodeToInstruction(TargetLanguageToCode(TL(" return ",TextPosition(4,13),TextPosition(4,21)))),BQTermToInstruction(BQAppl(concOption(OriginTracking(Name("Foo"),4,"__INPUT__"),ModuleName("default")),Name("Foo"),concBQTerm(Composite(CompositeTL(ITL("\140")),CompositeBQTerm(BQVariable(concOption(OriginTracking(Name("a"),4,"__INPUT__"),ModuleName("default")),Name("a"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())))),Composite(CompositeTL(ITL("\140")),CompositeBQTerm(BQVariable(concOption(OriginTracking(Name("b"),4,"__INPUT__"),ModuleName("default")),Name("b"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()))))))),CodeToInstruction(TargetLanguageToCode(TL("; ",TextPosition(4,33),TextPosition(4,35)))))),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0r_bq_multi")
	if got != expected {
		t.Errorf("AST mismatch on match0r_bq_multi.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseStrategy0Minimal(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Strat0 {\n  ",TextPosition(1,1),TextPosition(2,3))),InstructionToCode(AbstractBlock(concInstruction(CodeToInstruction(DeclarationToCode(Strategy(Name("MyStrat"),BQAppl(concOption(OriginTracking(Name("Identity"),2,"__INPUT__"),ModuleName("default")),Name("Identity"),concBQTerm()),concTomVisit(),concDeclaration(),OriginTracking(Name("MyStrat"),2,"__INPUT__")))),CodeToInstruction(DeclarationToCode(SymbolDecl(Name("MyStrat"))))))),TargetLanguageToCode(TL("\n}\n",TextPosition(3,4),TextPosition(4,1)))))`
	got := parseFixture(t, "strategy0_minimal")
	if got != expected {
		t.Errorf("AST mismatch on strategy0_minimal.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseStrategy1Visit(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Strat1 {\n  ",TextPosition(1,1),TextPosition(2,3))),InstructionToCode(AbstractBlock(concInstruction(CodeToInstruction(DeclarationToCode(Strategy(Name("MyStrat"),BQAppl(concOption(OriginTracking(Name("Identity"),2,"__INPUT__"),ModuleName("default")),Name("Identity"),concBQTerm()),concTomVisit(VisitTerm(Type(concTypeOption(),"Sort",EmptyTargetLanguageType()),concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),EmptyName(),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(ModuleName("default")),Name("tom__arg"),Type(concTypeOption(),"Sort",EmptyTargetLanguageType())),Type(concTypeOption(),"Sort",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("VisitTerm"),3,"__INPUT__")))),concDeclaration(),OriginTracking(Name("MyStrat"),2,"__INPUT__")))),CodeToInstruction(DeclarationToCode(SymbolDecl(Name("MyStrat"))))))),TargetLanguageToCode(TL("\n}\n",TextPosition(6,4),TextPosition(7,1)))))`
	got := parseFixture(t, "strategy1_visit")
	if got != expected {
		t.Errorf("AST mismatch on strategy1_visit.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMeta0Minimal(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Meta0 {\n  String s = ",TextPosition(1,1),TextPosition(2,14))),InstructionToCode(AbstractBlock(concInstruction(CodeToInstruction(TargetLanguageToCode(TL("\"\\n    hello\\n  \"",TextPosition(2,14),TextPosition(2,31))))))),TargetLanguageToCode(TL(";\n}\n",TextPosition(4,5),TextPosition(6,1)))))`
	got := parseFixture(t, "meta0_minimal")
	if got != expected {
		t.Errorf("AST mismatch on meta0_minimal.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseStrategy2VisitBody(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Strat2 {\n  ",TextPosition(1,1),TextPosition(2,3))),InstructionToCode(AbstractBlock(concInstruction(CodeToInstruction(DeclarationToCode(Strategy(Name("Walk"),BQAppl(concOption(OriginTracking(Name("Identity"),2,"__INPUT__"),ModuleName("default")),Name("Identity"),concBQTerm()),concTomVisit(VisitTerm(Type(concTypeOption(),"Term",EmptyTargetLanguageType()),concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(ModuleName("default")),Name("tom__arg"),Type(concTypeOption(),"Term",EmptyTargetLanguageType())),Type(concTypeOption(),"Term",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction(CodeToInstruction(TargetLanguageToCode(TL(" return ",TextPosition(4,13),TextPosition(4,21)))),BQTermToInstruction(BQAppl(concOption(OriginTracking(Name("f"),4,"__INPUT__"),ModuleName("default")),Name("f"),concBQTerm(BQVariable(concOption(OriginTracking(Name("x"),4,"__INPUT__"),ModuleName("default")),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()))))),CodeToInstruction(TargetLanguageToCode(TL("; ",TextPosition(4,26),TextPosition(4,28)))))),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("VisitTerm"),3,"__INPUT__")))),concDeclaration(),OriginTracking(Name("Walk"),2,"__INPUT__")))),CodeToInstruction(DeclarationToCode(SymbolDecl(Name("Walk"))))))),TargetLanguageToCode(TL("\n}\n",TextPosition(6,4),TextPosition(7,1)))))`
	got := parseFixture(t, "strategy2_visit_body")
	if got != expected {
		t.Errorf("AST mismatch on strategy2_visit_body.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

func TestParseMatch0sBqStar(t *testing.T) {
	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0s {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction(CodeToInstruction(TargetLanguageToCode(TL(" return ",TextPosition(4,13),TextPosition(4,21)))),BQTermToInstruction(BQAppl(concOption(OriginTracking(Name("Foo"),4,"__INPUT__"),ModuleName("default")),Name("Foo"),concBQTerm(Composite(CompositeTL(ITL("\140")),CompositeBQTerm(BQVariableStar(concOption(OriginTracking(Name("x"),4,"__INPUT__"),ModuleName("default")),Name("x"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()))))))),CodeToInstruction(TargetLanguageToCode(TL("; ",TextPosition(4,30),TextPosition(4,32)))))),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	got := parseFixture(t, "match0s_bqstar")
	if got != expected {
		t.Errorf("AST mismatch on match0s_bqstar.t\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}
