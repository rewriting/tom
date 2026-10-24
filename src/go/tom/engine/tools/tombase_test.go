package tools

import (
	"fmt"
	"testing"

	"tom/engine/adt"
	"tom/engine/platform"
)

const (
	typeA = `Type(concTypeOption(),"A",TLType("a"))`
	typeB = `Type(concTypeOption(),"B",TLType("b"))`
	// f(x:A, y:B):T, the slot y declared by a get_slot named yy
	symF = `Symbol(Name("f"),TypesToType(concTomType(` + typeA + `,` + typeB + `),` + typeT + `),` +
		`concPairNameDecl(PairNameDecl(Name("x"),EmptyDeclaration()),` +
		`PairNameDecl(Name("y"),GetSlotDecl(Name("f"),Name("yy"),BQVariable(concOption(),Name("t"),EmptyType()),Code("c"),ModuleName("m")))),concOption())`
)

func tableF() *SymbolTable {
	st := NewSymbolTable()
	st.PutSymbol("f", adt.TomSymbolFromString(symF))
	return st
}

// runtimeMsg returns the message of the *platform.RuntimeError f panics with.
func runtimeMsg(t *testing.T, f func()) (msg string) {
	t.Helper()
	defer func() {
		e, ok := recover().(*platform.RuntimeError)
		if !ok {
			t.Fatalf("want a *platform.RuntimeError, got %v", e)
		}
		msg = e.Msg
	}()
	f()
	return ""
}

func TestGoDeclPrefix(t *testing.T) {
	for in, want := range map[string]string{
		"Peano.t": "tom_Peano_", "dir/my-file.t": "tom_my_file_", "": "tom_stdin_", "-": "tom_stdin_",
		".t": "tom__t_", "a.b.t": "tom_a_b_", "été.t": "tom__t__",
	} {
		if got := GoDeclPrefix(in); got != want {
			t.Errorf("GoDeclPrefix(%q) = %q, want %q", in, got, want)
		}
	}
}

func TestTypes(t *testing.T) {
	if n, ok := GetTomType(adt.TomTypeFromString(typeT)); !ok || n != "T" {
		t.Fatal("GetTomType(Type)")
	}
	for _, s := range []string{`EmptyType()`, `TypeVar("X",1)`} {
		if _, ok := GetTomType(adt.TomTypeFromString(s)); ok {
			t.Errorf("GetTomType(%s): ok", s)
		}
	}
	if m := runtimeMsg(t, func() { GetTomType(adt.TomTypeFromString(`Codomain("x")`)) }); m != `getTomType error on term: Codomain("x")` {
		t.Fatalf("GetTomType(Codomain): %q", m)
	}
	if GetTLType(adt.TomTypeFromString(typeT)) != "t" {
		t.Fatal("GetTLType")
	}
	if m := runtimeMsg(t, func() { GetTLType(adt.TomTypeFromString(`Type(concTypeOption(),"T",EmptyTargetLanguageType())`)) }); m != "getTLCode error on term: EmptyTargetLanguageType()" {
		t.Fatalf("GetTLType(empty TL): %q", m)
	}
	if fmt.Sprint(GetSymbolCodomain(nil)) != "EmptyType()" || fmt.Sprint(GetSymbolDomain(nil)) != "concTomType()" {
		t.Fatal("codomain and domain of nil")
	}
	f := adt.TomSymbolFromString(symF)
	if fmt.Sprint(GetSymbolCodomain(f)) != typeT || fmt.Sprint(GetSymbolDomain(f)) != `concTomType(`+typeA+`,`+typeB+`)` {
		t.Fatal("codomain and domain of f")
	}
}

func TestSlots(t *testing.T) {
	f := adt.TomSymbolFromString(symF)
	if fmt.Sprint(GetSlotName(f, 0)) != `Name("x")` || fmt.Sprint(GetSlotName(f, 1)) != `Name("yy")` {
		t.Fatal("GetSlotName")
	}
	if m := runtimeMsg(t, func() { GetSlotName(f, 2) }); m != "getSlotName: bad index error" {
		t.Fatalf("GetSlotName(2): %q", m)
	}
	y, yy := adt.TomNameFromString(`Name("y")`), adt.TomNameFromString(`Name("yy")`)
	if GetSlotIndex(f, y) != 1 || GetSlotIndex(f, yy) != -1 {
		t.Fatal("GetSlotIndex")
	}
	if fmt.Sprint(GetSlotType(f, y)) != typeB || GetSlotType(nil, y) != nil {
		t.Fatal("GetSlotType")
	}
	if m := runtimeMsg(t, func() { GetSlotType(f, yy) }); m != "illegal list index: -1" {
		t.Fatalf("GetSlotType(yy): %q", m)
	}
}

func TestNumberListAndModule(t *testing.T) {
	l := adt.TomNumberListFromString(`concTomNumber(Position(1),MatchNumber(2),NameNumber(Name("x")),RenamedVar(PositionName(concTomNumber(Begin(3)))))`)
	if got := TomNumberListToString(l); got != "Position1Match2xRenamedVarBegin3" {
		t.Fatalf("TomNumberListToString: %q", got)
	}
	if m, ok := GetModuleName(adt.OptionListFromString(`concOption(ModuleName("m"))`)); !ok || m != "m" {
		t.Fatal("GetModuleName")
	}
	if _, ok := GetModuleName(adt.OptionListFromString(`concOption()`)); ok {
		t.Fatal("GetModuleName of no option")
	}
}

func TestTermTypes(t *testing.T) {
	st := tableF()
	for s, want := range map[string]string{
		`Variable(concOption(),Name("x"),` + typeA + `,concConstraint())`:                        typeA,
		`TermAppl(concOption(),concTomName(Name("f")),concTomTerm(),concConstraint())`:           typeT,
		`TermAppl(concOption(),concTomName(AntiName(Name("f"))),concTomTerm(),concConstraint())`: typeT,
		`RecordAppl(concOption(),concTomName(Name("g")),concSlot(),concConstraint())`:            `EmptyType()`,
		`AntiTerm(Variable(concOption(),Name("x"),` + typeA + `,concConstraint()))`:              typeA,
	} {
		if got := fmt.Sprint(GetTermType(adt.TomTermFromString(s), st)); got != want {
			t.Errorf("GetTermType(%s) = %s, want %s", s, got, want)
		}
	}
	if m := runtimeMsg(t, func() { GetTermType(adt.TomTermFromString(`TruePattern()`), st) }); m != "getTermType error on term: TruePattern()" {
		t.Fatalf("TruePattern: %q", m)
	}
	for s, want := range map[string]string{
		`BQVariable(concOption(),Name("v"),` + typeB + `)`:   typeB,
		`FunctionCall(Name("g"),` + typeA + `,concBQTerm())`: typeA,
		`Subterm(Name("f"),Name("y"),BQDefault())`:           typeB,
		`BuildTerm(Name("f"),concBQTerm(),"m")`:              typeT,
		`BQAppl(concOption(),Name("g"),concBQTerm())`:        `EmptyType()`,
		`ExpressionToBQTerm(Cast(` + typeA + `,Code("c")))`:  typeA,
	} {
		if got := fmt.Sprint(GetTermTypeBQ(adt.BQTermFromString(s), st)); got != want {
			t.Errorf("GetTermTypeBQ(%s) = %s, want %s", s, got, want)
		}
	}
	if m := runtimeMsg(t, func() { GetTermTypeBQ(adt.BQTermFromString(`BQDefault()`), st) }); m != "getTermType error on term: BQDefault()" {
		t.Fatalf("BQDefault: %q", m)
	}
	if got := fmt.Sprint(GetTermTypeExpr(adt.ExpressionFromString(`Integer(3)`), st)); got != fmt.Sprint(st.GetIntType()) {
		t.Fatalf("Integer: %s", got)
	}
	if m := runtimeMsg(t, func() { GetTermTypeExpr(adt.ExpressionFromString(`Code("c")`), st) }); m != `getTermType error on term: Code("c")` {
		t.Fatalf("Code: %q", m)
	}
	if GetSymbolFromName("f", st) != st.GetSymbolFromName("f") {
		t.Fatal("GetSymbolFromName")
	}
}
