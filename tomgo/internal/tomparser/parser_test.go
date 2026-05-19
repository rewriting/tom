package tomparser

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"testing"
)

// parseFixture reads tomgo/testdata/parse/<name>/scenario.t and parses it via
// the hand-rolled Go parser, returning the canonical String() form with all
// absolute paths normalised: scenario.t → __INPUT__, its containing directory
// → __DIR__. Same convention as the tomparseq cross-language harness.
func parseFixture(t *testing.T, name string) string {
	t.Helper()
	path := "../../testdata/parse/" + name + "/scenario.t"
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
