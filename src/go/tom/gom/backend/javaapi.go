package backend

// The Java Gom API of the generated terms, for the modules gomgo
// generates outside the compiler's own ADT (gom-block, gom: Generate),
// after Java Gom's SortTemplate, OperatorTemplate and
// VariadicOperatorTemplate:
//
//   - on every sort (interface methods, implemented by every
//     alternative): SymbolName, Is<op> (IsCons<op>/IsEmpty<op> for a
//     variadic operator), Get<slot> (GetHead<op>/GetTail<op>), Length,
//     Reverse, CompareTo, CompareToLPO;
//   - for a variadic operator op of sort S over E: the list
//     constructors of Java's Cons<op>/Empty<op> classes, through the
//     construction hooks and the theory (FL, AU, ACU, Free):
//     Empty<Op>() S, Cons<Op>(head E, tail S) S, RealMakeCons<Op>
//     (insertion without hook), Raw<Op>(elems…) (the term holding
//     exactly elems), Make<Op>(elems…) (Java's fromArray, Tom's
//     `op(elems…)`), <Op>FromArray, and on the list struct ToArray and
//     Append.
//
// The Go term of a variadic operator is a flat slice of elements. A
// list of an AU/ACU operator ending with an element that is not a list
// (Java's Cons<op>(e, x) with x not a Cons<op>/Empty<op>, the normal
// form of lists of length 2 or more) is the flat slice of all its
// elements: GetTail<op> of a 2-element list returns its last element.

import (
	"bytes"
	"fmt"
	"regexp"
	"strings"

	gomast "tom/gom/adt"
)

// sortAPI describes the members of a sort that the Java API needs.
type sortAPI struct {
	name     string
	alts     []*gomast.AlternativeAlternative
	slots    []apiSlot // named slots of the non-variadic operators, by first appearance
	variadic []*gomast.AlternativeAlternative
}

type apiSlot struct {
	gomName string // slot name in the .gom
	field   string // Go field name
	goType  string
	gomType string // Gom sort of the slot (int, long, String, a sort, …)
}

func (g *gen) sortAPIOf(prod *gomast.SortTypeProduction) sortAPI {
	api := sortAPI{name: sortName(prod), alts: sortAlternatives(prod)}
	seen := map[string]bool{}
	for _, alt := range api.alts {
		if alternativeIsVariadic(alt) {
			api.variadic = append(api.variadic, alt)
			continue
		}
		for _, s := range g.namedSlots(alt) {
			if !seen[s.gomName] {
				seen[s.gomName] = true
				api.slots = append(api.slots, s)
			}
		}
	}
	return api
}

// namedSlots lists the slots of a non-variadic alt as emitAlternative
// names them.
func (g *gen) namedSlots(alt *gomast.AlternativeAlternative) []apiSlot {
	var out []apiSlot
	seen := map[string]int{}
	for i, f := range alternativeFields(alt) {
		nf, ok := f.(*gomast.NamedFieldField)
		if !ok {
			continue
		}
		name := exportedField(nf.Name)
		if name == "" {
			name = fmt.Sprintf("Slot%d", i)
		}
		if reservedFieldName[name] {
			name = name + "_"
		}
		if c := seen[name]; c > 0 {
			name = fmt.Sprintf("%s%d", name, c+1)
		}
		seen[name]++
		out = append(out, apiSlot{gomName: nf.Name, field: name, goType: g.goType(fieldTypeName(f), false), gomType: fieldTypeName(f)})
	}
	return out
}

func (g *gen) elemGoType(alt *gomast.AlternativeAlternative) string {
	return g.goType(fieldTypeName(alternativeFields(alt)[0]), false)
}

// emitJavaAPIInterface writes the Java API method set of the sort.
func (g *gen) emitJavaAPIInterface(api sortAPI) {
	fmt.Fprintln(&g.buf, "\t// Java Gom API (see the package's generated methods)")
	fmt.Fprintln(&g.buf, "\tSymbolName() string")
	for _, alt := range api.alts {
		if alternativeIsVariadic(alt) {
			fmt.Fprintf(&g.buf, "\tIsCons%s() bool\n", alt.Name)
			fmt.Fprintf(&g.buf, "\tIsEmpty%s() bool\n", alt.Name)
			fmt.Fprintf(&g.buf, "\tGetHead%s() %s\n", alt.Name, g.elemGoType(alt))
			fmt.Fprintf(&g.buf, "\tGetTail%s() %s\n", alt.Name, api.name)
		} else {
			fmt.Fprintf(&g.buf, "\tIs%s() bool\n", alt.Name)
		}
	}
	for _, s := range api.slots {
		fmt.Fprintf(&g.buf, "\tGet%s() %s\n", s.gomName, s.goType)
	}
	fmt.Fprintln(&g.buf, "\tLength() int")
	fmt.Fprintf(&g.buf, "\tReverse() %s\n", api.name)
	fmt.Fprintln(&g.buf, "\tCompareTo(o any) int")
	fmt.Fprintln(&g.buf, "\tCompareToLPO(o any) int")
	fmt.Fprintln(&g.buf, "\tToATerm() aterm.ATerm")
}

// emitJavaAPIMethods writes the Java API methods of one alternative of
// the sort.
func (g *gen) emitJavaAPIMethods(api sortAPI, alt *gomast.AlternativeAlternative) {
	opGo := exportedField(alt.Name)
	if opGo == "" {
		opGo = "Op"
	}
	st := opGo + api.name
	variadic := alternativeIsVariadic(alt)
	w := func(format string, args ...any) { fmt.Fprintf(&g.buf, format, args...) }

	// SymbolName
	if variadic {
		w("// SymbolName is the symbol of the Java term: Cons%s or Empty%s.\n", alt.Name, alt.Name)
		w("func (t *%s) SymbolName() string {\n\tif len(t.Slots) == 0 {\n\t\treturn %q\n\t}\n\treturn %q\n}\n\n", st, "Empty"+alt.Name, "Cons"+alt.Name)
	} else {
		w("func (t *%s) SymbolName() string { return %q }\n\n", st, alt.Name)
	}
	// Is<op>, IsCons<op>, IsEmpty<op>
	for _, other := range api.alts {
		if alternativeIsVariadic(other) {
			if other == alt {
				w("func (t *%s) IsCons%s() bool { return len(t.Slots) > 0 }\n", st, other.Name)
				w("func (t *%s) IsEmpty%s() bool { return len(t.Slots) == 0 }\n", st, other.Name)
			} else {
				w("func (t *%s) IsCons%s() bool { return false }\n", st, other.Name)
				w("func (t *%s) IsEmpty%s() bool { return false }\n", st, other.Name)
			}
		} else {
			w("func (t *%s) Is%s() bool { return %v }\n", st, other.Name, other == alt)
		}
	}
	w("\n")
	// Get<slot>
	own := map[string]apiSlot{}
	if !variadic {
		for _, s := range g.namedSlots(alt) {
			own[s.gomName] = s
		}
	}
	for _, s := range api.slots {
		if o, ok := own[s.gomName]; ok {
			w("func (t *%s) Get%s() %s { return t.%s }\n", st, s.gomName, s.goType, o.field)
		} else {
			w("func (t *%s) Get%s() %s {\n\tpanic(&aterm.GomError{Msg: %q})\n}\n", st, s.gomName, s.goType, "This "+api.name+" has no "+s.gomName)
		}
	}
	// GetHead<op>, GetTail<op>
	for _, v := range api.variadic {
		elem := g.elemGoType(v)
		if v == alt {
			w("func (t *%s) GetHead%s() %s {\n\tif len(t.Slots) == 0 {\n\t\tpanic(&aterm.GomError{Msg: %q})\n\t}\n\treturn t.Slots[0]\n}\n", st, v.Name, elem, "This "+api.name+" has no Head"+v.Name)
			w("func (t *%s) GetTail%s() %s {\n\tif len(t.Slots) == 0 {\n\t\tpanic(&aterm.GomError{Msg: %q})\n\t}\n", st, v.Name, api.name, "This "+api.name+" has no Tail"+v.Name)
			if th := theoryOf(g.mod, v).kind; th == "AU" || th == "ACU" {
				w("\t// AU: the last tail of a list is its last element\n")
				w("\tif len(t.Slots) == 2 {\n\t\treturn t.Slots[1]\n\t}\n")
			}
			w("\treturn Raw%s(t.Slots[1:]...)\n}\n", exportedField(v.Name))
		} else {
			w("func (t *%s) GetHead%s() %s {\n\tpanic(&aterm.GomError{Msg: %q})\n}\n", st, v.Name, elem, "This "+api.name+" has no Head"+v.Name)
			w("func (t *%s) GetTail%s() %s {\n\tpanic(&aterm.GomError{Msg: %q})\n}\n", st, v.Name, api.name, "This "+api.name+" has no Tail"+v.Name)
		}
	}
	w("\n")
	// Length, Reverse
	if variadic {
		vGo := exportedField(alt.Name)
		w("// Length is the number of elements of the list.\nfunc (t *%s) Length() int { return len(t.Slots) }\n\n", st)
		w("// Reverse is the reversed list, built by Cons%s (through the hooks).\n", vGo)
		w("func (t *%s) Reverse() %s {\n\tif len(t.Slots) == 0 {\n\t\treturn t\n\t}\n\tvar rev %s = Empty%s()\n\tfor _, e := range t.Slots {\n\t\trev = Cons%s(e, rev)\n\t}\n\treturn rev\n}\n\n", st, api.name, api.name, vGo, vGo)
		w("// ToArray returns the elements of the list (Java's Object[] toArray()).\n")
		w("func (t *%s) ToArray() []any {\n\tout := make([]any, len(t.Slots))\n\tfor i, e := range t.Slots {\n\t\tout[i] = e\n\t}\n\treturn out\n}\n\n", st)
		w("// Append returns the list with element added at its end, built by\n// Cons%s (through the hooks).\n", vGo)
		w("func (t *%s) Append(element %s) %s {\n\tvar res %s = Cons%s(element, Empty%s())\n\tfor i := len(t.Slots) - 1; i >= 0; i-- {\n\t\tres = Cons%s(t.Slots[i], res)\n\t}\n\treturn res\n}\n\n",
			st, g.elemGoType(alt), api.name, api.name, vGo, vGo, vGo)
	} else {
		w("func (t *%s) Length() int {\n\tpanic(&aterm.GomError{Msg: %q, IllegalArgument: true})\n}\n\n", st, "This "+api.name+"."+alt.Name+" is not a list")
		w("func (t *%s) Reverse() %s {\n\tpanic(&aterm.GomError{Msg: %q, IllegalArgument: true})\n}\n\n", st, api.name, "This "+api.name+"."+alt.Name+" is not a list")
	}
	// CompareTo, CompareToLPO
	g.emitCompare(api, alt, st, "CompareTo", true)
	g.emitCompare(api, alt, st, "CompareToLPO", false)
	// ToATerm, fromTerm
	g.emitJavaATerm(api.name, alt, st)
}

// emitCompare writes Java Gom's compareTo (a total order: hash, symbol,
// then children) or compareToLPO (symbol, then children).
func (g *gen) emitCompare(api sortAPI, alt *gomast.AlternativeAlternative, st, name string, byHash bool) {
	w := func(format string, args ...any) { fmt.Fprintf(&g.buf, format, args...) }
	w("func (t *%s) %s(o any) int {\n", st, name)
	if g.noSharing && byHash {
		// Java Gom without maximal sharing: compareTo is not implemented
		w("\tpanic(&aterm.GomError{Msg: \"Unable to compare\"})\n}\n\n")
		return
	}
	w("\tao := o.(%s)\n", api.name)
	if g.noSharing {
		w("\tif t.DeepEquals(ao) {\n\t\treturn 0\n\t}\n")
	} else {
		w("\tif ao == %s(t) {\n\t\treturn 0\n\t}\n", api.name)
	}
	if byHash {
		w("\tif t.Hash() != ao.Hash() {\n\t\tif t.Hash() < ao.Hash() {\n\t\t\treturn -1\n\t\t}\n\t\treturn 1\n\t}\n")
	}
	w("\tif c := strings.Compare(t.SymbolName(), ao.SymbolName()); c != 0 {\n\t\treturn c\n\t}\n")
	cmp := func(goType, a, b string) {
		switch {
		case g.isShared(goType):
			w("\tif c := %s.%s(%s); c != 0 {\n\t\treturn c\n\t}\n", a, name, b)
		case goType == "string":
			w("\tif c := strings.Compare(%s, %s); c != 0 {\n\t\treturn c\n\t}\n", a, b)
		case goType == "bool":
			w("\tif %s != %s {\n\t\tif !%s && %s {\n\t\t\treturn -1\n\t\t}\n\t\treturn 1\n\t}\n", a, b, a, b)
		case goType == "any" || goType == "aterm.ATerm" || goType == "*aterm.ATermList":
			// Java Gom: "Inefficient total order on ATerm" (their texts)
			w("\tif c := strings.Compare(fmt.Sprint(%s), fmt.Sprint(%s)); c != 0 {\n\t\treturn c\n\t}\n", a, b)
		default:
			w("\tif %s != %s {\n\t\tif %s < %s {\n\t\t\treturn -1\n\t\t}\n\t\treturn 1\n\t}\n", a, b, a, b)
		}
	}
	if alternativeIsVariadic(alt) {
		w("\ttco := ao.(*%s)\n", st)
		w("\tfor i := 0; i < len(t.Slots) && i < len(tco.Slots); i++ {\n")
		cmp(g.elemGoType(alt), "t.Slots[i]", "tco.Slots[i]")
		w("\t}\n")
		w("\tif len(t.Slots) != len(tco.Slots) {\n\t\tif len(t.Slots) < len(tco.Slots) {\n\t\t\treturn -1\n\t\t}\n\t\treturn 1\n\t}\n")
	} else if slots := g.namedSlots(alt); len(slots) > 0 {
		w("\ttco := ao.(*%s)\n", st)
		for _, s := range slots {
			cmp(s.goType, "t."+s.field, "tco."+s.field)
		}
	}
	w("\tpanic(&aterm.GomError{Msg: \"Unable to compare\"})\n}\n\n")
}

// neutralPattern is the AU/ACU neutral element the Go backend can
// evaluate: a backquoted constant of the module (`aa()).
var neutralPattern = regexp.MustCompile("^`?\\s*([A-Za-z_][A-Za-z0-9_]*)\\s*\\(\\s*\\)\\s*;?$")

// neutralGo returns the Go expression of the neutral element of an
// AU/ACU theory ("" for the default neutral, the empty list).
func neutralGo(mod gomast.GomModule, th opTheory) (string, error) {
	if th.neutral == "" {
		return "", nil
	}
	m := neutralPattern.FindStringSubmatch(th.neutral)
	if m == nil {
		return "", fmt.Errorf("neutral element %q: only a constant of the module (`op()) is supported", th.neutral)
	}
	for _, prod := range moduleSorts(mod) {
		for _, alt := range sortAlternatives(prod) {
			if alt.Name == m[1] && !alternativeIsVariadic(alt) && len(altSlotSorts(alt)) == 0 {
				return "Make" + exportedField(alt.Name) + "()", nil
			}
		}
	}
	return "", fmt.Errorf("neutral element %q: not a constant of the module", th.neutral)
}

// emitVariadicJava writes the list constructors of a variadic operator
// (see the package comment above), replacing the plain Make<Op> of the
// ADT backend.
func (g *gen) emitVariadicJava(sortNm string, alt *gomast.AlternativeAlternative, structName, elem string) {
	w := func(format string, args ...any) { fmt.Fprintf(&g.buf, format, args...) }
	op := alt.Name
	opGo := exportedField(op)
	hooks := opHooks(g.mod, alt)
	th := theoryOf(g.mod, alt)
	neutral, _ := neutralGo(g.mod, th) // CheckHooks reported the error
	sameSort := sameSortVariadic(g.mod, alt)

	w("// Raw%s builds the canonical (shared) %s list holding exactly args,\n// without construction hook.\n", opGo, op)
	w("func Raw%s(args ...%s) %s {\n", opGo, elem, sortNm)
	w("\thashes := make([]uint32, 0, len(args))\n\tfor _, a := range args {\n")
	if g.isShared(elem) {
		w("\t\thashes = append(hashes, a.Hash())\n")
	} else {
		w("\t\thashes = append(hashes, sharedobjects.StringHash(fmt.Sprintf(\"%%v\", a)))\n")
	}
	w("\t}\n")
	w("\tproto := &%s{Slots: args, hash: sharedobjects.MixSymbol(sharedobjects.StringHash(%q), hashes)}\n", structName, op)
	g.emitReturnProto(structName)
	w("}\n\n")

	if len(hooksOfKind(hooks, "make_empty")) > 0 {
		w("// HookMakeEmpty%s runs the make_empty hooks (and the rules for %s()) of\n// the Gom module; the Tom mapping generated with this file sets it.\n", opGo, op)
		w("var HookMakeEmpty%s func() %s\n\n", opGo, sortNm)
	}
	w("// Empty%s is Java Gom's Empty%s.make(): the empty %s list, after the\n// construction hooks.\n", opGo, op, op)
	w("func Empty%s() %s {\n", opGo, sortNm)
	if neutral != "" {
		w("\t// %s: the empty list is the neutral element\n\treturn %s\n", th.kind, neutral)
	} else {
		if len(hooksOfKind(hooks, "make_empty")) > 0 {
			w("\tif HookMakeEmpty%s != nil {\n\t\tif r := HookMakeEmpty%s(); r != nil {\n\t\t\treturn r\n\t\t}\n\t}\n", opGo, opGo)
		}
		w("\treturn Raw%s()\n", opGo)
	}
	w("}\n\n")

	insertHooks := len(hooksOfKind(hooks, "make_insert")) > 0
	if insertHooks {
		w("// HookMakeInsert%s runs the make_insert hooks (and the rules) of the Gom\n// module: it returns their result (nil if none returned) and the\n// arguments they may have reassigned. The Tom mapping generated with\n// this file sets it.\n", opGo)
		w("var HookMakeInsert%s func(%s, %s) (%s, %s, %s)\n\n", opGo, elem, sortNm, sortNm, elem, sortNm)
	}
	w("// Cons%s is Java Gom's Cons%s.make(head, tail): head inserted in front of\n// tail, after the construction hooks (theory %q first).\n", opGo, op, th.kind)
	w("func Cons%s(head %s, tail %s) %s {\n", opGo, elem, sortNm, sortNm)
	isEmpty := func(v string) string {
		return fmt.Sprintf("func() bool { c, ok := any(%s).(*%s); return ok && len(c.Slots) == 0 }()", v, structName)
	}
	flattenHead := func() {
		w("\tif h, ok := any(head).(*%s); ok && len(h.Slots) > 0 {\n\t\treturn Cons%s(h.Slots[0], Cons%s(h.GetTail%s(), tail))\n\t}\n", structName, opGo, opGo, op)
	}
	switch th.kind {
	case "FL":
		w("\t// FL: flattened list\n")
		w("\tif %s {\n\t\treturn tail\n\t}\n", isEmpty("head"))
		flattenHead()
		w("\tif _, ok := any(tail).(*%s); !ok {\n\t\treturn Cons%s(head, Cons%s(tail, Empty%s()))\n\t}\n", structName, opGo, opGo, opGo)
	case "ACU", "AU":
		// Java Gom's order (HookTypeExpander prepends each hook, so the
		// emitted Cons<op>.make runs them in reverse of makeAUHookList /
		// makeACUHookList): neutral removal, AU flattening, then, for
		// ACU, the swap that keeps the elements sorted (compareTo).
		if neutral != "" {
			w("\t// %s: neutral element\n\tif head == %s {\n\t\treturn tail\n\t}\n\tif tail == %s {\n\t\treturn head\n\t}\n", th.kind, neutral, neutral)
		}
		w("\t// %s: flattening, the empty list is neutral\n", th.kind)
		w("\tif %s {\n\t\treturn tail\n\t}\n", isEmpty("head"))
		w("\tif %s {\n\t\treturn head\n\t}\n", isEmpty("tail"))
		flattenHead()
		if th.kind == "ACU" {
			w("\t// ACU: the elements are kept sorted (CompareTo)\n")
			w("\tif t, ok := any(tail).(*%s); ok && len(t.Slots) > 0 {\n", structName)
			w("\t\tif 0 < head.CompareTo(t.Slots[0]) {\n\t\t\ttmpHd := head\n\t\t\thead = t.Slots[0]\n\t\t\ttail = Cons%s(tmpHd, Cons%s(t.GetTail%s(), Empty%s()))\n\t\t}\n", opGo, opGo, op, opGo)
			w("\t} else {\n\t\tif 0 < head.CompareTo(tail) {\n\t\t\ttmpHd := head\n\t\t\thead = tail\n\t\t\ttail = tmpHd\n\t\t}\n\t}\n")
		}
	}
	if insertHooks {
		w("\tif HookMakeInsert%s != nil {\n\t\tvar hookResult %s\n\t\thookResult, head, tail = HookMakeInsert%s(head, tail)\n\t\tif hookResult != nil {\n\t\t\treturn hookResult\n\t\t}\n\t}\n", opGo, sortNm, opGo)
	}
	w("\treturn RealMakeCons%s(head, tail)\n}\n\n", opGo)

	w("// RealMakeCons%s is Java Gom's Cons%s.realMake: the insertion without hook.\n", opGo, op)
	w("func RealMakeCons%s(head %s, tail %s) %s {\n", opGo, elem, sortNm, sortNm)
	w("\tif c, ok := tail.(*%s); ok {\n\t\treturn Raw%s(append([]%s{head}, c.Slots...)...)\n\t}\n", structName, opGo, elem)
	if sameSort {
		w("\t// a list ending with an element that is not a list (Java's Cons%s(head, tail))\n", op)
		w("\treturn Raw%s(head, tail)\n}\n\n", opGo)
	} else {
		// Java Gom builds Cons<op>(head, tail) with a tail of the list sort
		// that is not a list (e.g. the result of a make_empty hook); the
		// flat slice of elements of another sort cannot hold it.
		w("\tpanic(&aterm.GomError{Msg: fmt.Sprintf(\"Cons%s: the tail %%v is not a %s list (not representable in Go: the elements are not of its sort)\", tail)})\n}\n\n", op, op)
	}

	w("// Make%s builds the list of args as Tom's `%s(args…)` does: from Empty%s,\n// by Cons%s, through the construction hooks (Java's %s.fromArray).\n", opGo, op, opGo, opGo, op)
	w("func Make%s(args ...%s) %s {\n\tvar l %s = Empty%s()\n\tfor i := len(args) - 1; i >= 0; i-- {\n\t\tl = Cons%s(args[i], l)\n\t}\n\treturn l\n}\n\n", opGo, elem, sortNm, sortNm, opGo, opGo)
	w("// %sFromArray is Java Gom's %s.fromArray(array).\n", opGo, op)
	w("func %sFromArray(array []%s) %s { return Make%s(array...) }\n\n", opGo, elem, sortNm, opGo)
}

// emitVariadicMappingGo writes the mapping of a variadic operator for
// the Java API backend: the Empty<op>/Cons<op> operators (as Java Gom
// declares them) and the %oplist, built by Empty<Op>/Cons<Op> through
// the construction hooks.
func emitVariadicMappingGo(buf *bytes.Buffer, sortNm string, alt *gomast.AlternativeAlternative, structName, elemSort string) {
	op := alt.Name
	opGo := exportedField(op)
	fmt.Fprintf(buf, "%%op %s Empty%s() {\n", sortNm, op)
	fmt.Fprintf(buf, "  is_fsym(t) { func() bool { c, ok := ($t).(*%s); return ok && len(c.Slots) == 0 }() }\n", structName)
	fmt.Fprintf(buf, "  make() { Empty%s() }\n", opGo)
	fmt.Fprintf(buf, "}\n\n")
	fmt.Fprintf(buf, "%%op %s Cons%s(Head%s:%s, Tail%s:%s) {\n", sortNm, op, op, elemSort, op, sortNm)
	fmt.Fprintf(buf, "  is_fsym(t) { func() bool { c, ok := ($t).(*%s); return ok && len(c.Slots) > 0 }() }\n", structName)
	fmt.Fprintf(buf, "  get_slot(Head%s, t) { ($t).GetHead%s() }\n", op, op)
	fmt.Fprintf(buf, "  get_slot(Tail%s, t) { ($t).GetTail%s() }\n", op, op)
	fmt.Fprintf(buf, "  make(t0, t1) { Cons%s($t0, $t1) }\n", opGo)
	fmt.Fprintf(buf, "}\n\n")
	fmt.Fprintf(buf, "%%oplist %s %s( %s* ) {\n", sortNm, op, elemSort)
	fmt.Fprintf(buf, "  is_fsym(t)       { func() bool { _, ok := ($t).(*%s); return ok }() }\n", structName)
	fmt.Fprintf(buf, "  make_empty()     { Empty%s() }\n", opGo)
	fmt.Fprintf(buf, "  make_insert(e,l) { Cons%s($e,$l) }\n", opGo)
	fmt.Fprintf(buf, "  get_head(l)      { ($l).GetHead%s() }\n", op)
	fmt.Fprintf(buf, "  get_tail(l)      { ($l).GetTail%s() }\n", op)
	fmt.Fprintf(buf, "  is_empty(l)      { ($l).IsEmpty%s() }\n", op)
	fmt.Fprintf(buf, "}\n\n")
}

// javaSlotString is the Go expression of the text of a slot value x of
// Gom sort gomType in a term's string, as Java Gom's toStringBuilder
// writes it: long with an "l", char as its code minus '0', boolean as
// 1/0, double/float as Java's toString, String quoted and escaped.
func (g *gen) javaSlotString(gomType, x string) string {
	switch gomType {
	case "int":
		return "fmt.Sprint(" + x + ")"
	case "long":
		return "fmt.Sprint(" + x + `) + "l"`
	case "double":
		return "aterm.JavaDouble(" + x + ")"
	case "float":
		return "aterm.JavaFloat(" + x + ")"
	case "char":
		return "fmt.Sprint(int(" + x + ") - '0')"
	case "boolean":
		return "func() string { if " + x + " { return \"1\" }; return \"0\" }()"
	case "String":
		return "sharedobjects.JavaEscape(" + x + ")"
	}
	return "fmt.Sprint(" + x + ")"
}

// emitJavaString writes String (Java Gom's toString).
func (g *gen) emitJavaString(alt *gomast.AlternativeAlternative, structName string) {
	w := func(format string, args ...any) { fmt.Fprintf(&g.buf, format, args...) }
	w("func (t *%s) String() string {\n", structName)
	if alternativeIsVariadic(alt) {
		elem := fieldTypeName(alternativeFields(alt)[0])
		w("\tparts := make([]string, len(t.Slots))\n\tfor i, v := range t.Slots {\n\t\tparts[i] = %s\n\t}\n", g.javaSlotString(elem, "v"))
		w("\treturn %q + \"(\" + strings.Join(parts, \",\") + \")\"\n}\n\n", alt.Name)
		return
	}
	slots := g.namedSlots(alt)
	if len(slots) == 0 {
		w("\treturn %q\n}\n\n", alt.Name+"()")
		return
	}
	var parts []string
	for _, sl := range slots {
		parts = append(parts, g.javaSlotString(sl.gomType, "t."+sl.field))
	}
	w("\treturn %q + %s + \")\"\n}\n\n", alt.Name+"(", strings.Join(parts, " + \",\" + "))
}

// javaSlotATerm is the Go expression of the ATerm of a slot value x of
// Gom sort gomType (Java Gom's toATermSlotField).
func (g *gen) javaSlotATerm(gomType, x string) string {
	switch gomType {
	case "int":
		return "aterm.MakeGomInt(" + x + ")"
	case "long":
		return "aterm.MakeGomLong(" + x + ")"
	case "double":
		return "aterm.MakeGomReal(" + x + ")"
	case "float":
		return "aterm.MakeGomReal(float64(" + x + "))"
	case "char":
		return "aterm.MakeGomChar(" + x + ")"
	case "boolean":
		return "aterm.MakeGomBoolean(" + x + ")"
	case "String":
		return "aterm.MakeGomString(" + x + ")"
	case "ATerm", "ATermList":
		return x
	}
	if !g.ownSort[gomType] {
		// a sort of another module generated separately (slot of type any)
		return x + ".(interface{ ToATerm() aterm.ATerm }).ToATerm()"
	}
	return x + ".ToATerm()"
}

// javaSlotFromATerm is the Go expression of the slot value of Gom sort
// gomType read back from the ATerm a (Java Gom's fromATermSlotField).
func (g *gen) javaSlotFromATerm(gomType, a string) string {
	switch gomType {
	case "int":
		return "aterm.GomInt(" + a + ", conv)"
	case "long":
		return "aterm.GomLong(" + a + ", conv)"
	case "double":
		return "aterm.GomDouble(" + a + ", conv)"
	case "float":
		return "aterm.GomFloat(" + a + ", conv)"
	case "char":
		return "aterm.GomChar(" + a + ", conv)"
	case "boolean":
		return "aterm.GomBoolean(" + a + ", conv)"
	case "String":
		return "aterm.GomString(" + a + ", conv)"
	case "ATerm":
		return a
	case "ATermList":
		return a + ".(*aterm.ATermList)"
	}
	if g.ownSort[gomType] {
		return gomType + "FromTermConv(" + a + ", conv)"
	}
	return a
}

// emitJavaATerm writes ToATerm and the fromTerm function of one
// alternative (Java Gom's toATerm and <op>.fromTerm).
func (g *gen) emitJavaATerm(sortNm string, alt *gomast.AlternativeAlternative, structName string) {
	w := func(format string, args ...any) { fmt.Fprintf(&g.buf, format, args...) }
	opGo := exportedField(alt.Name)
	if alternativeIsVariadic(alt) {
		elem := fieldTypeName(alternativeFields(alt)[0])
		elemGo := g.goType(elem, false)
		w("// ToATerm is the ATerm of the term: the list of its elements.\n")
		w("func (t *%s) ToATerm() aterm.ATerm {\n\tf := aterm.GetInstance()\n\tl := f.MakeListEmpty()\n", structName)
		w("\tfor i := len(t.Slots) - 1; i >= 0; i-- {\n\t\tl = f.MakeList(%s, l)\n\t}\n\treturn l\n}\n\n", g.javaSlotATerm(elem, "t.Slots[i]"))
		w("// fromTerm%s is Java Gom's %s.fromTerm: an application of %s, or a\n// list (of ToATerm), of convertible elements.\n", structName, alt.Name, alt.Name)
		w("func fromTerm%s(trm aterm.ATerm, conv aterm.ATermConverter) (res %s, ok bool) {\n", structName, sortNm)
		w("\ttrm = conv.Convert(trm)\n")
		w("\tif appl, isAppl := trm.(*aterm.ATermAppl); isAppl && appl.GetName() == %q {\n", alt.Name)
		w("\t\telems := make([]%s, appl.GetArity())\n\t\tfor i, a := range appl.GetArguments() {\n\t\t\telems[i] = %s\n\t\t}\n\t\treturn Make%s(elems...), true\n\t}\n", elemGo, g.javaSlotFromATerm(elem, "a"), opGo)
		w("\tif list, isList := trm.(*aterm.ATermList); isList {\n")
		w("\t\t// an element that cannot be converted: not a %s (Java returns null)\n", alt.Name)
		w("\t\tdefer func() {\n\t\t\tif recover() != nil {\n\t\t\t\tres, ok = nil, false\n\t\t\t}\n\t\t}()\n")
		w("\t\tvar elems []%s\n\t\tfor !list.IsEmpty() {\n\t\t\telems = append(elems, %s)\n\t\t\tlist = list.GetNext()\n\t\t}\n\t\treturn Make%s(elems...), true\n\t}\n", elemGo, g.javaSlotFromATerm(elem, "list.GetFirst()"), opGo)
		w("\treturn nil, false\n}\n\n")
		return
	}
	slots := g.namedSlots(alt)
	var args, from []string
	for i, sl := range slots {
		args = append(args, g.javaSlotATerm(sl.gomType, "t."+sl.field))
		from = append(from, g.javaSlotFromATerm(sl.gomType, fmt.Sprintf("appl.GetArgument(%d)", i)))
	}
	w("// ToATerm is the ATerm of the term: an application of %s.\n", alt.Name)
	w("func (t *%s) ToATerm() aterm.ATerm {\n\tf := aterm.GetInstance()\n\treturn f.MakeAppl(f.MakeAFun(%q, %d, false)", structName, alt.Name, len(slots))
	for _, a := range args {
		w(", %s", a)
	}
	w(")\n}\n\n")
	w("// fromTerm%s is Java Gom's %s.fromTerm: an unquoted application of\n// %s whose arguments are convertible (as in Java, the arity is not\n// checked: a missing argument is an index out of range).\n", structName, alt.Name, alt.Name)
	w("func fromTerm%s(trm aterm.ATerm, conv aterm.ATermConverter) (%s, bool) {\n", structName, sortNm)
	w("\ttrm = conv.Convert(trm)\n")
	w("\tif appl, ok := trm.(*aterm.ATermAppl); ok && appl.GetName() == %q && !appl.GetAFun().IsQuoted() {\n", alt.Name)
	w("\t\treturn Make%s(%s), true\n\t}\n\treturn nil, false\n}\n\n", opGo, strings.Join(from, ", "))
}

// emitJavaFromTerm writes the sort-level conversions from ATerms
// (Java Gom's static <Sort>.fromTerm/fromString/fromStream).
func (g *gen) emitJavaFromTerm(api sortAPI) {
	w := func(format string, args ...any) { fmt.Fprintf(&g.buf, format, args...) }
	s := api.name
	w("// %sFromTerm is Java Gom's %s.fromTerm(trm).\n", s, s)
	w("func %sFromTerm(trm aterm.ATerm) %s { return %sFromTermConv(trm, aterm.IdConverter{}) }\n\n", s, s, s)
	w("// %sFromTermConv is Java Gom's %s.fromTerm(trm, atConv): the term of\n// the first operator of the sort that converts the ATerm; it panics\n// (IllegalArgumentException) when there is none.\n", s, s)
	w("func %sFromTermConv(trm aterm.ATerm, conv aterm.ATermConverter) %s {\n\tconverted := conv.Convert(trm)\n\tvar results []%s\n", s, s, s)
	ordered := []*gomast.AlternativeAlternative{}
	for _, alt := range api.alts {
		if !alternativeIsVariadic(alt) {
			ordered = append(ordered, alt)
		}
	}
	ordered = append(ordered, api.variadic...)
	for _, alt := range ordered {
		opGo := exportedField(alt.Name)
		if opGo == "" {
			opGo = "Op"
		}
		w("\tif r, ok := fromTerm%s(converted, conv); ok {\n\t\tresults = append(results, r)\n\t}\n", opGo+s)
	}
	w("\tif len(results) == 0 {\n\t\tpanic(&aterm.GomError{Msg: fmt.Sprintf(\"%%v is not a %s\", trm), IllegalArgument: true})\n\t}\n\treturn results[0]\n}\n\n", s)
	w("// %sFromString is Java Gom's %s.fromString(s).\n", s, s)
	w("func %sFromString(s string) %s { return %sFromTerm(aterm.GetInstance().Parse(s)) }\n\n", s, s, s)
	w("// %sFromStream is Java Gom's %s.fromStream(stream) (an IOException is\n// the error).\n", s, s)
	w("func %sFromStream(stream io.Reader) (%s, error) {\n\tat, err := aterm.GetInstance().ReadFromTextFile(stream)\n\tif err != nil {\n\t\treturn nil, err\n\t}\n\treturn %sFromTerm(at), nil\n}\n\n", s, s, s)
}
