package plugins_test

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/stable/platform"
	"tom/tomgo/stable/platform/plugins"
)

// TestPlatformParser drives the Parser plugin through the Platform on a
// known-stable fixture. It does not compare with Java directly (the
// cross-language harness lives in stable/tom/parser/equiv) — instead it
// pins the byte-equivalent string the parser already produces. The point
// is to assert the Platform wiring is correct: Parser.Run goes through
// platform.Platform.Run, reads the source, calls into the hand-rolled
// parser, and threads the AST through State.
func TestPlatformParser_Match0b(t *testing.T) {
	repo, err := os.Getwd()
	if err != nil {
		t.Fatal(err)
	}
	// stable/platform/plugins/ → tomgo/ is 3 levels up.
	abs, err := filepath.Abs(filepath.Join(repo, "..", "..", "..", "testdata", "parse", "match0b", "scenario.t"))
	if err != nil {
		t.Fatal(err)
	}

	p := platform.New(plugins.Parser{})
	final, err := p.Run(platform.State{Filename: abs})
	if err != nil {
		t.Fatalf("platform: %v", err)
	}
	if final.Code == nil {
		t.Fatal("Parser produced nil Code")
	}
	got := fmt.Sprintf("%v", final.Code)
	got = strings.ReplaceAll(got, abs, "__INPUT__")
	got = strings.ReplaceAll(got, filepath.Dir(abs), "__DIR__")

	const expected = `Tom(concCode(TargetLanguageToCode(TL("public class Match0 {\n  public void f(Object t) {\n    ",TextPosition(1,1),TextPosition(3,5))),InstructionToCode(Match(concConstraintInstruction(ConstraintInstruction(MatchConstraint(Variable(concOption(),EmptyName(),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),concConstraint()),BQVariable(concOption(OriginTracking(Name("t"),3,"__INPUT__"),ModuleName("default")),Name("t"),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())),RawAction(If(TrueTL(),AbstractBlock(concInstruction()),Nop())),concOption(OriginTracking(Name("ConstraintAction"),4,"__INPUT__")))),concOption(OriginTracking(Name("Match"),3,"__INPUT__"),ModuleName("default")))),TargetLanguageToCode(TL("\n  }\n\n}\n",TextPosition(5,6),TextPosition(8,1)))))`
	if got != expected {
		t.Errorf("Platform/Parser output mismatch on match0b\n--- expected ---\n%s\n--- got ---\n%s\n", expected, got)
	}
}

// TestPlatformParser_Source verifies that providing State.Source directly
// (without a real file read) bypasses the os.ReadFile path. Useful for
// future plugins that synthesise sources in memory.
func TestPlatformParser_Source(t *testing.T) {
	src := []byte(`public class X { %typeterm Foo {} }`)
	final, err := platform.New(plugins.Parser{}).Run(platform.State{
		Filename: "in-memory",
		Source:   src,
	})
	if err != nil {
		t.Fatalf("platform: %v", err)
	}
	if final.Code == nil {
		t.Fatal("nil Code")
	}
	if !strings.Contains(fmt.Sprintf("%v", final.Code), `TypeTermDecl(Name("Foo")`) {
		t.Errorf("expected TypeTermDecl(Name(\"Foo\")) in output, got: %v", final.Code)
	}
}

// TestPlatformParser_MissingFile asserts that a non-existent input
// surfaces as a clean error wrapped with the plugin's Name.
func TestPlatformParser_MissingFile(t *testing.T) {
	_, err := platform.New(plugins.Parser{}).Run(platform.State{
		Filename: "/path/that/does/not/exist.t",
	})
	if err == nil {
		t.Fatal("expected error on missing file, got nil")
	}
	if !strings.Contains(err.Error(), "plugin Parser:") {
		t.Errorf("expected 'plugin Parser:' prefix in error, got: %v", err)
	}
}
