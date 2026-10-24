package tools

import (
	"fmt"
	"strings"
	"testing"

	"tom/engine/adt"
	"tom/engine/bridge"
	"tom/engine/platform"
)

const (
	typeT  = `Type(concTypeOption(),"T",TLType("t"))`
	symA   = `Symbol(Name("a"),TypesToType(concTomType(),` + typeT + `),concPairNameDecl(),concOption())`
	symB   = `Symbol(Name("b"),TypesToType(concTomType(),` + typeT + `),concPairNameDecl(),concOption())`
	tableB = `Table(concTomEntry(Entry("b",` + symB + `),Entry("a",` + symA + `)))`
)

func TestBuiltinTypes(t *testing.T) {
	st := NewSymbolTable()
	for _, c := range []struct {
		got  adt.TomType
		want string
	}{
		{st.GetIntType(), `Type(concTypeOption(),"int",TLType("int64"))`},
		{st.GetIntArrayType(), `Type(concTypeOption(),"intarray",TLType("[]int64"))`},
		{st.GetLongType(), `Type(concTypeOption(),"long",TLType("int64"))`},
		{st.GetFloatType(), `Type(concTypeOption(),"float",TLType("float32"))`},
		{st.GetCharType(), `Type(concTypeOption(),"char",TLType("rune"))`},
		{st.GetDoubleType(), `Type(concTypeOption(),"double",TLType("float64"))`},
		{st.GetBooleanType(), `Type(concTypeOption(),"boolean",TLType("bool"))`},
		{st.GetStringType(), `Type(concTypeOption(),"String",TLType("string"))`},
		{st.GetUniversalType(), `Type(concTypeOption(),"universal",TLType("any"))`},
		{st.GetVoidType(), `Type(concTypeOption(),"void",TLType(""))`},
	} {
		if fmt.Sprint(c.got) != c.want {
			t.Errorf("got %v, want %s", c.got, c.want)
		}
	}
	if st.GetIntArrayOp() != "concInt" {
		t.Fatal("GetIntArrayOp")
	}
}

func TestTypePredicates(t *testing.T) {
	st := NewSymbolTable()
	for _, n := range []string{"int", "long", "char", "String", "boolean", "double", "float"} {
		if !st.IsBuiltinType(n) || st.GetBuiltinType(n) == nil {
			t.Errorf("%s is builtin", n)
		}
	}
	for _, n := range []string{"intarray", "void", "universal", "T"} {
		if st.IsBuiltinType(n) {
			t.Errorf("%s is not builtin", n)
		}
	}
	if !st.IsUnknownType("unknown type") || st.IsUnknownType("int") {
		t.Fatal("IsUnknownType")
	}
	if st.GetBuiltinType("String") != st.GetStringType() {
		t.Fatal("GetBuiltinType is not maximally shared")
	}
	defer func() {
		e, ok := recover().(*platform.RuntimeError)
		if !ok || e.Msg != "getBuiltinType error on term: Nat" {
			t.Fatalf("GetBuiltinType(Nat): %v", e)
		}
	}()
	st.GetBuiltinType("Nat")
}

func TestSymbolsAndTerm(t *testing.T) {
	st := NewSymbolTable()
	st.FromTerm(adt.TomSymbolTableFromString(tableB))
	if got := strings.Join(st.KeySymbols(), " "); got != "a b" {
		t.Fatalf("KeySymbols %q", got)
	}
	if fmt.Sprint(st.GetSymbolFromName("a")) != symA || st.GetSymbolFromName("c") != nil {
		t.Fatal("GetSymbolFromName")
	}
	if got := fmt.Sprint(st.ToTerm()); got != tableB {
		t.Fatalf("ToTerm %s", got)
	}
	if st.GetType("T") != nil || fmt.Sprint(st.GetType("int")) != fmt.Sprint(st.GetIntType()) {
		t.Fatal("GetType before PutType")
	}
	ty := adt.TomTypeFromString(typeT)
	st.PutType("T", ty)
	if st.GetType("T") != ty {
		t.Fatal("GetType")
	}
}

func TestUsedMarks(t *testing.T) {
	st := NewSymbolTable()
	st.FromTerm(adt.TomSymbolTableFromString(tableB))
	st.PutType("T", adt.TomTypeFromString(typeT))
	a := st.GetSymbolFromName("a")
	st.SetUsedSymbolConstructor(a)
	st.SetUsedSymbolDestructorName("b")
	st.SetUsedSymbolDestructorName("nosuch")
	st.SetUsedTypeName("T")
	st.SetUsedTypeName("int")
	st.SetUsedTypeName("nosuch")
	if !st.IsUsedSymbolConstructor(a) || st.IsUsedSymbolDestructor(a) || !st.IsUsedSymbolConstructorName("a") ||
		!st.IsUsedSymbolDestructorName("b") || st.IsUsedSymbolConstructorName("nosuch") || st.IsUsedSymbolAC(a) {
		t.Fatal("symbol marks")
	}
	if !st.IsUsedTypeName("T") || !st.IsUsedType(st.GetIntType()) || st.IsUsedTypeName("long") {
		t.Fatal("type marks")
	}
	var got []string
	for _, ke := range st.GetUsedKeyEntries() {
		got = append(got, fmt.Sprint(ke))
	}
	want := []string{
		`UsedSymbolConstructor(` + symA + `)`,
		`UsedSymbolDestructor(` + symB + `)`,
		`UsedType(` + typeT + `)`,
		`UsedType(Type(concTypeOption(),"int",TLType("int64")))`,
	}
	if strings.Join(got, "\n") != strings.Join(want, "\n") {
		t.Fatalf("GetUsedKeyEntries:\n%s", strings.Join(got, "\n"))
	}
}

func TestInliners(t *testing.T) {
	st := NewSymbolTable()
	if _, ok := st.GetIsFsym("f"); ok {
		t.Fatal("no inliner yet")
	}
	st.PutIsFsym("f", "isf")
	st.PutGetSlot("f", "x", "gx")
	st.PutResolveGetSlot("g", "y")
	st.PutMake("empty_list_l", "m")
	if c, ok := st.GetIsFsym("f"); !ok || c != "isf" {
		t.Fatal("GetIsFsym")
	}
	if c, ok := st.GetGetSlot("f", "x"); !ok || c != "gx" {
		t.Fatal("GetGetSlot")
	}
	if c, ok := st.GetResolveGetSlot("g", "y"); !ok || c != "" {
		t.Fatal("GetResolveGetSlot: present, empty")
	}
	// Java's keys are prefix+name in one map: make_ + empty_list_l is make_empty_list_ + l
	if c, ok := st.GetMakeEmptyList("l"); !ok || c != "m" {
		t.Fatal("shared inliner keys")
	}
}

func TestFromBundle(t *testing.T) {
	text := strings.Join([]string{
		"tom-bridge 1",
		"from Backend",
		"input /x/X.t",
		`inputname "X.t"`,
		"output /x/X.go",
		"option goCode bool true",
		"used UsedType(" + typeT + ")",
		"type " + typeT,
		"table " + tableB,
		"code Tom(concCode())",
		"end",
	}, "\n") + "\n"
	b, err := bridge.Read(strings.NewReader(text))
	if err != nil {
		t.Fatal(err)
	}
	st := FromBundle(b)
	if st.ToTerm() != b.Table || fmt.Sprint(st.GetType("T")) != typeT || !st.IsUsedTypeName("T") {
		t.Fatal("FromBundle")
	}
	if len(st.GetUsedKeyEntries()) != 1 || st.GetUsedKeyEntries()[0] != b.Used[0] ||
		len(st.GetTypeEntries()) != 1 || st.GetTypeEntries()[0] != b.Types[0] {
		t.Fatal("marks and types")
	}
}
