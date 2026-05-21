package backend_java

import (
	"strings"
	"testing"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// TestEmitArrayHelpers exercises the `%oparray` helper emission path
// for a hand-built `conc` symbol shaped like the one Array.t parses
// to. We hand `emitSymbolHelpers` a synthetic `usedSymbols` set and
// inspect the buffered output for the expected method signatures
// and body fragments.
//
// Helpers checked: tom_is_fun_sym_conc, tom_empty_array_conc,
// tom_cons_array_conc, tom_get_element_conc_L, tom_get_size_conc_L,
// tom_get_slice_conc, tom_append_array_conc. The latter two are
// generic — they inline the operator's own bodies.
func TestEmitArrayHelpers(t *testing.T) {
	st := &tom.SymbolTable{
		Sorts:   map[string]string{"L": "ArrayList", "E": "ATerm"},
		Symbols: map[string]tomast.TomSymbol{},
	}
	concSym := buildConcSymbol()
	st.Symbols["conc"] = concSym

	e := &emitter{
		symbols:     st,
		usedSymbols: map[string]bool{"conc": true},
	}
	if err := e.emitSymbolHelpers(tomast.MakeName("conc")); err != nil {
		t.Fatalf("emitSymbolHelpers: %v", err)
	}
	out := e.buf.String()

	mustContain := []string{
		"tom_is_fun_sym_conc(ArrayList t)",
		"tom_empty_array_conc(int n)",
		" new ArrayList(n) ",
		"tom_cons_array_conc(ATerm e, ArrayList l)",
		" myAdd(e,(ArrayList)l) ",
		"tom_get_element_conc_L(ArrayList l, int n)",
		" ((ATerm)((ArrayList)l).get(n)) ",
		"tom_get_size_conc_L(ArrayList l)",
		" ((ArrayList)l).size() ",
		"tom_get_slice_conc",
		// generic slice body: inlines make_empty(end-begin),
		// make_append(get_element(subject,begin), result).
		" new ArrayList(end-begin) ",
		" myAdd( ((ATerm)((ArrayList)subject).get(begin)) ,(ArrayList)result) ",
		"tom_append_array_conc",
		// generic append body: inlines get_size for l1/l2,
		// make_empty(size1+size2), make_append loops.
		" ((ArrayList)l1).size() ",
		" ((ArrayList)l2).size() ",
		" new ArrayList(size1+size2) ",
		" myAdd( ((ATerm)((ArrayList)l1).get(size1-index)) ,(ArrayList)result) ",
		" myAdd( ((ATerm)((ArrayList)l2).get(size2-index)) ,(ArrayList)result) ",
	}
	for _, want := range mustContain {
		if !strings.Contains(out, want) {
			t.Errorf("missing %q in emitted helpers:\n%s", want, out)
		}
	}
}

// buildConcSymbol assembles the `conc` symbol the way the parser does
// when it sees `%oparray L conc(E*) { ... }`. We bypass the parser to
// keep the test independent of parser changes; the shape is the
// public contract emitSymbolHelpers reads.
func buildConcSymbol() tomast.TomSymbol {
	mkOT := tomast.MakeOriginTracking(tomast.MakeName("conc"), 0, "test")

	codomain := tomast.MakeType(
		tomast.MakeConcTypeOption(),
		"L",
		tomast.MakeEmptyTargetLanguageType(),
	)
	elemType := tomast.MakeType(
		tomast.MakeConcTypeOption(),
		"E",
		tomast.MakeEmptyTargetLanguageType(),
	)

	mkBQ := func(name, ttype string) tomast.BQTerm {
		return tomast.MakeBQVariable(
			tomast.MakeConcOption(),
			tomast.MakeName(name),
			tomast.MakeType(
				tomast.MakeConcTypeOption(),
				ttype,
				tomast.MakeEmptyTargetLanguageType(),
			),
		)
	}

	isFsym := tomast.MakeIsFsymDecl(
		tomast.MakeName("conc"),
		mkBQ("t", "L"),
		tomast.MakeCode(" ({0} instanceof ArrayList) "),
		mkOT,
	)
	makeEmpty := tomast.MakeMakeEmptyArray(
		tomast.MakeName("conc"),
		mkBQ("n", "int"),
		tomast.MakeExpressionToInstruction(tomast.MakeCode(" new ArrayList({0}) ")),
		mkOT,
	)
	makeAppend := tomast.MakeMakeAddArray(
		tomast.MakeName("conc"),
		mkBQ("e", "L"),
		mkBQ("l", "L"),
		tomast.MakeExpressionToInstruction(tomast.MakeCode(" myAdd({0},(ArrayList){1}) ")),
		mkOT,
	)
	getElement := tomast.MakeGetElementDecl(
		tomast.MakeName("conc"),
		mkBQ("l", "L"),
		mkBQ("n", "int"),
		tomast.MakeCode(" ((ATerm)((ArrayList){0}).get({1})) "),
		mkOT,
	)
	getSize := tomast.MakeGetSizeDecl(
		tomast.MakeName("conc"),
		mkBQ("l", "L"),
		tomast.MakeCode(" ((ArrayList){0}).size() "),
		mkOT,
	)
	options := tomast.MakeConcOption(
		tomast.MakeDeclarationToOption(isFsym),
		tomast.MakeDeclarationToOption(makeEmpty),
		tomast.MakeDeclarationToOption(makeAppend),
		tomast.MakeDeclarationToOption(getElement),
		tomast.MakeDeclarationToOption(getSize),
	)

	return tomast.MakeSymbol(
		tomast.MakeName("conc"),
		tomast.MakeTypesToType(
			tomast.MakeConcTomType(elemType),
			codomain,
		),
		tomast.MakeConcPairNameDecl(),
		options,
	)
}

// TestEmitIRLowering pins the IR-shape lowering: build a Let / If /
// IsSort / IsFsym / Cast / Return / BQTermToExpression / BuildTerm
// tree by hand and verify each fragment lands in the emitted source
// with the expected Java text. Mirrors what the new compiler will
// emit for a tiny Peano-style fixture.
func TestEmitIRLowering(t *testing.T) {
	mkType := func(sort, tl string) tomast.TomType {
		return tomast.MakeType(
			tomast.MakeConcTypeOption(),
			sort,
			tomast.MakeTLType(tl),
		)
	}
	termT := mkType("term", " ATerm ")
	mkBQVar := func(name string, ttype tomast.TomType) tomast.BQTerm {
		return tomast.MakeBQVariable(
			tomast.MakeConcOption(),
			tomast.MakeName(name),
			ttype,
		)
	}

	// Build: Let(t1, BQTermToExpression(tzero),
	//          If(IsSort(term, t1),
	//             If(IsFsym(zero, Cast(term, BQTermToExpression(t1))),
	//                Return(t1),
	//                Nop),
	//             Nop))
	inner := tomast.MakeIf(
		tomast.MakeIsFsym(
			tomast.MakeName("zero"),
			tomast.MakeExpressionToBQTerm(
				tomast.MakeCast(termT,
					tomast.MakeBQTermToExpression(mkBQVar("t1", termT)),
				),
			),
		),
		tomast.MakeReturn(mkBQVar("t1", termT)),
		tomast.MakeNop(),
	)
	tree := tomast.MakeLet(
		mkBQVar("t1", termT),
		tomast.MakeBQTermToExpression(mkBQVar("tzero", termT)),
		tomast.MakeIf(
			tomast.MakeIsSort(termT, mkBQVar("t1", termT)),
			inner,
			tomast.MakeNop(),
		),
	)

	e := &emitter{}
	if err := e.emitInstruction(tree); err != nil {
		t.Fatalf("emit: %v", err)
	}
	out := e.buf.String()
	for _, want := range []string{
		"ATerm t1 = tzero;",
		"if (tom_is_sort_term(t1))",
		"tom_is_fun_sym_zero(",
		"(( ATerm )t1)",
		"return t1;",
	} {
		if !strings.Contains(out, want) {
			t.Errorf("missing %q in:\n%s", want, out)
		}
	}
}

// TestRenderTomNamePositionName pins the rendering of position-typed
// fresh variables. Java's Compiler builds variable names like
// `PositionName(concTomNumber(MatchNumber(1), NameNumber(Name("_0"))))`
// which Java's PrettyPrinter renders as `tomMatch1_0`.
func TestRenderTomNamePositionName(t *testing.T) {
	name := tomast.MakePositionName(
		tomast.MakeConcTomNumber(
			tomast.MakeMatchNumber(1),
			tomast.MakeNameNumber(tomast.MakeName("_0")),
		),
	)
	got := renderTomName(name)
	if got != "tomMatch1_0" {
		t.Fatalf("renderTomName: got %q, want %q", got, "tomMatch1_0")
	}

	name2 := tomast.MakePositionName(
		tomast.MakeConcTomNumber(
			tomast.MakeMatchNumber(1),
			tomast.MakeNameNumber(tomast.MakeName("_freshSubject_0")),
		),
	)
	got2 := renderTomName(name2)
	if got2 != "tomMatch1_freshSubject_0" {
		t.Fatalf("renderTomName: got %q, want %q", got2, "tomMatch1_freshSubject_0")
	}
}

// TestScanReferencesNewPrefixes pins the dead-helper filter so it picks
// up the variadic-array helper-call shapes that the (future) variadic
// compiler will emit into TL chunks.
func TestScanReferencesNewPrefixes(t *testing.T) {
	text := `
		tom_is_fun_sym_conc(x);
		tom_empty_array_conc(7);
		tom_cons_array_conc(a, b);
		tom_get_element_conc_L(l, 0);
		tom_get_size_conc_L(l);
		tom_get_slice_conc(l, 0, 3);
		tom_append_array_conc(l2, l1);
	`
	used := map[string]bool{}
	scanReferences(text, used)
	if !used["conc"] {
		t.Fatalf("expected `conc` in used set, got %v", used)
	}
}
