package backend

// The Java Gom API of the generated terms (every module gomgo generates:
// gom, gom-block, gom-batch), after Java Gom's SortTemplate,
// OperatorTemplate and VariadicOperatorTemplate:
//
//   - on every sort (interface methods, implemented by every
//     alternative): SymbolName, Is<op> (IsCons<op>/IsEmpty<op> for a
//     variadic operator), Get<slot> (GetHead<op>/GetTail<op>), Length,
//     Reverse, CompareTo, CompareToLPO;
//   - for a variadic operator op of sort S over E: the list
//     constructors of Java's Cons<op>/Empty<op> classes, through the
//     construction hooks and the theory (FL, AC, AU, ACU, Free):
//     Empty<Op>() S, Cons<Op>(head E, tail S) S, RealMakeCons<Op>
//     (insertion without hook), Raw<Op>(elems…) (the term holding
//     exactly elems), Make<Op>(elems…) (Java's fromArray, Tom's
//     `op(elems…)`), <Op>FromArray, and on the list struct ToArray and
//     Append.
//
// The Go term of a variadic operator is a maximally-shared cons cell, as
// in Java (cells.go): Cons<Op>, GetHead<Op>, GetTail<Op> are O(1), and a
// list may end with a tail that is not a list (Java's Cons<op>(e, x)
// with x not a Cons<op>/Empty<op>: the normal form of the AU/ACU lists
// of length 2 or more, or the result of a make_empty hook).

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

// slotField is a slot of a non-variadic alt: its Gom name and the name
// of its Go field.
type slotField struct {
	gom, field string
	f          gomast.Field
}

// slotFieldNames is the single source of the Go field names of a
// non-variadic alt (struct, Java API, mapping get_slot): the exported
// slot name, Slot<i> for an empty name, a "_" suffix when the name is a
// method of the alt struct (reservedFieldName), a number for a
// duplicate. Unnamed fields are skipped.
func slotFieldNames(alt *gomast.AlternativeAlternative) []slotField {
	var out []slotField
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
		// Avoid colliding with the struct's own methods (String,
		// Hash, Equivalent, Duplicate…): a Gom slot literally named
		// `String:String` (as in TomName.Name) would otherwise
		// produce `field and method with the same name`.
		if reservedFieldName[name] {
			name = name + "_"
		}
		if c := seen[name]; c > 0 {
			name = fmt.Sprintf("%s%d", name, c+1)
		}
		seen[name]++
		out = append(out, slotField{gom: nf.Name, field: name, f: f})
	}
	return out
}

// namedSlots lists the slots of a non-variadic alt as emitAlternative
// names them.
func (g *gen) namedSlots(alt *gomast.AlternativeAlternative) []apiSlot {
	var out []apiSlot
	for _, s := range slotFieldNames(alt) {
		out = append(out, apiSlot{gomName: s.gom, field: s.field, goType: g.goType(fieldTypeName(s.f), false), gomType: fieldTypeName(s.f)})
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
	st := opBase(alt) + api.name
	variadic := alternativeIsVariadic(alt)
	w := func(format string, args ...any) { fmt.Fprintf(&g.buf, format, args...) }

	// SymbolName
	if variadic {
		w("// SymbolName is the symbol of the Java term: Cons%s or Empty%s.\n", alt.Name, alt.Name)
		w("func (t *%s) SymbolName() string {\n\tif t.empty {\n\t\treturn %q\n\t}\n\treturn %q\n}\n\n", st, "Empty"+alt.Name, "Cons"+alt.Name)
	} else {
		w("func (t *%s) SymbolName() string { return %q }\n\n", st, alt.Name)
	}
	// Is<op>, IsCons<op>, IsEmpty<op>
	for _, other := range api.alts {
		if alternativeIsVariadic(other) {
			if other == alt {
				w("func (t *%s) IsCons%s() bool { return !t.empty }\n", st, other.Name)
				w("func (t *%s) IsEmpty%s() bool { return t.empty }\n", st, other.Name)
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
			w("func (t *%s) GetHead%s() %s {\n\tif t.empty {\n\t\tpanic(&aterm.GomError{Msg: %q})\n\t}\n\treturn t.head\n}\n", st, v.Name, elem, "This "+api.name+" has no Head"+v.Name)
			w("func (t *%s) GetTail%s() %s {\n\tif t.empty {\n\t\tpanic(&aterm.GomError{Msg: %q})\n\t}\n\treturn t.tail\n}\n", st, v.Name, api.name, "This "+api.name+" has no Tail"+v.Name)
		} else {
			w("func (t *%s) GetHead%s() %s {\n\tpanic(&aterm.GomError{Msg: %q})\n}\n", st, v.Name, elem, "This "+api.name+" has no Head"+v.Name)
			w("func (t *%s) GetTail%s() %s {\n\tpanic(&aterm.GomError{Msg: %q})\n}\n", st, v.Name, api.name, "This "+api.name+" has no Tail"+v.Name)
		}
	}
	w("\n")
	// Length, Reverse
	if variadic {
		g.emitListAPICells(api.name, alt, st)
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
	variadic := alternativeIsVariadic(alt)
	if variadic {
		// Java compares the slots of a Cons cell, head then tail: the
		// cells of the two lists, one after the other
		w("\tfor {\n")
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
	if variadic {
		unable := "\tpanic(&aterm.GomError{Msg: \"Unable to compare\"})\n"
		w("\ttco := ao.(*%s)\n", st)
		w("\tif t.empty {\n%s\t}\n", unable)
		cmp(g.elemGoType(alt), "t.head", "tco.head")
		w("\tnext, ok := t.tail.(*%s)\n\tif !ok {\n\t\tif c := t.tail.%s(tco.tail); c != 0 {\n\t\t\treturn c\n\t\t}\n%s\t}\n", st, name, unable)
		w("\tt, o = next, tco.tail\n\t}\n}\n\n")
		return
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
func neutralGo(pkg []gomast.GomModule, mod gomast.GomModule, th opTheory) (string, error) {
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
				return "Make" + newOpNames(pkg).of(alt, sortName(prod)) + "()", nil
			}
		}
	}
	return "", fmt.Errorf("neutral element %q: not a constant of the module", th.neutral)
}

// emitVariadicJava writes the list constructors of a variadic operator
// (see the package comment above). The lists are cons cells (cells.go); Cons<Op> runs the
// theory and the construction hooks as Java Gom's Cons<op>.make does
// (HookTypeExpander), then RealMakeCons<Op> builds the shared cell.
func (g *gen) emitVariadicJava(sortNm string, alt *gomast.AlternativeAlternative, structName, elem string) {
	w := func(format string, args ...any) { fmt.Fprintf(&g.buf, format, args...) }
	op := alt.Name
	opGo := g.opNames.of(alt, sortNm)
	hooks := opHooks(g.pkgMods, g.mod, alt)
	th := theoryOf(g.pkgMods, g.mod, alt)
	neutral, _ := neutralGo(g.pkgMods, g.mod, th) // CheckHooks reported the error

	w("// RealMakeCons%s is Java Gom's Cons%s.realMake: the shared cell of head\n// and tail, without hook.\n", opGo, op)
	w("func RealMakeCons%s(head %s, tail %s) %s {\n", opGo, elem, sortNm, sortNm)
	w("\tproto := &%s{head: head, tail: tail, hash: sharedobjects.MixSymbol(sharedobjects.StringHash(%q), []uint32{%s, tail.Hash()})}\n", structName, "Cons"+op, g.hashOfElem(elem, "head"))
	g.emitReturnProto(structName)
	w("}\n\n")
	w("// realMakeEmpty%s is Java Gom's Empty%s.realMake: the shared empty list,\n// without hook.\n", opGo, op)
	w("func realMakeEmpty%s() %s {\n", opGo, sortNm)
	w("\tproto := &%s{empty: true, hash: sharedobjects.MixSymbol(sharedobjects.StringHash(%q), nil)}\n", structName, "Empty"+op)
	g.emitReturnProto(structName)
	w("}\n\n")

	w("// Raw%s builds the %s list holding exactly args, without construction\n// hook.\n", opGo, op)
	w("func Raw%s(args ...%s) %s {\n\tl := realMakeEmpty%s()\n\tfor i := len(args) - 1; i >= 0; i-- {\n\t\tl = RealMakeCons%s(args[i], l)\n\t}\n\treturn l\n}\n\n", opGo, elem, sortNm, opGo, opGo)

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
		w("\treturn realMakeEmpty%s()\n", opGo)
	}
	w("}\n\n")

	insertHooks := len(hooksOfKind(hooks, "make_insert")) > 0
	if insertHooks {
		w("// HookMakeInsert%s runs the make_insert hooks (and the rules) of the Gom\n// module: it returns their result (nil if none returned) and the\n// arguments they may have reassigned. The Tom mapping generated with\n// this file sets it.\n", opGo)
		w("var HookMakeInsert%s func(%s, %s) (%s, %s, %s)\n\n", opGo, elem, sortNm, sortNm, elem, sortNm)
	}
	w("// Cons%s is Java Gom's Cons%s.make(head, tail): head inserted in front of\n// tail, after the construction hooks (theory %q first).\n", opGo, op, th.kind)
	w("func Cons%s(head %s, tail %s) %s {\n", opGo, elem, sortNm, sortNm)
	// the theory hooks of Java's HookTypeExpander (the theories apply to
	// operators whose domain is their codomain: head has the list API)
	flatten := func() {
		w("\tif head.IsCons%s() {\n\t\treturn Cons%s(head.GetHead%s(), Cons%s(head.GetTail%s(), tail))\n\t}\n", op, opGo, op, opGo, op)
	}
	sorted := func(acu bool) {
		// Java's hook: tail = `op(tmpHd, tail.getTail<op>()), the tail of
		// tail being an element of the backquoted list (it is flattened
		// by Cons(tail.getTail<op>(), Empty<op>.make()))
		w("\tif tail.IsCons%s() {\n\t\tif 0 < head.CompareTo(tail.GetHead%s()) {\n\t\t\ttmpHd := head\n\t\t\thead = tail.GetHead%s()\n\t\t\ttail = Cons%s(tmpHd, Cons%s(tail.GetTail%s(), Empty%s()))\n\t\t}\n", op, op, op, opGo, opGo, op, opGo)
		if acu {
			w("\t} else {\n\t\tif 0 < head.CompareTo(tail) {\n\t\t\ttmpHd := head\n\t\t\thead = tail\n\t\t\ttail = tmpHd\n\t\t}\n")
		}
		w("\t}\n")
	}
	switch th.kind {
	case "FL", "AC":
		// makeFLHookList / makeACHookList: flattening, the lists end with
		// the empty list (then, for AC, the elements are kept sorted)
		w("\t// FL: flattened list\n\tif head.IsEmpty%s() {\n\t\treturn tail\n\t}\n", op)
		flatten()
		end := "Cons" + opGo
		if th.kind == "AC" {
			end = "RealMakeCons" + opGo
		}
		w("\tif !tail.IsCons%s() && !tail.IsEmpty%s() {\n\t\treturn Cons%s(head, %s(tail, Empty%s()))\n\t}\n", op, op, opGo, end, opGo)
		if th.kind == "AC" {
			w("\t// AC: the elements are kept sorted (CompareTo)\n")
			sorted(false)
		}
	case "ACU", "AU":
		// Java Gom's order (HookTypeExpander prepends each hook, so the
		// emitted Cons<op>.make runs them in reverse of makeAUHookList /
		// makeACUHookList): neutral removal, AU flattening, then, for
		// ACU, the swap that keeps the elements sorted (compareTo).
		if neutral != "" {
			w("\t// %s: neutral element\n\tif head == %s {\n\t\treturn tail\n\t}\n\tif tail == %s {\n\t\treturn head\n\t}\n", th.kind, neutral, neutral)
		}
		w("\t// %s: flattening, the empty list is neutral\n", th.kind)
		w("\tif head.IsEmpty%s() {\n\t\treturn tail\n\t}\n", op)
		w("\tif tail.IsEmpty%s() {\n\t\treturn head\n\t}\n", op)
		flatten()
		if th.kind == "ACU" {
			w("\t// ACU: the elements are kept sorted (CompareTo)\n")
			sorted(true)
		}
	}
	if insertHooks {
		w("\tif HookMakeInsert%s != nil {\n\t\tvar hookResult %s\n\t\thookResult, head, tail = HookMakeInsert%s(head, tail)\n\t\tif hookResult != nil {\n\t\t\treturn hookResult\n\t\t}\n\t}\n", opGo, sortNm, opGo)
	}
	w("\treturn RealMakeCons%s(head, tail)\n}\n\n", opGo)

	w("// Make%s builds the list of args as Tom's `%s(args…)` does: from Empty%s,\n// by Cons%s, through the construction hooks (Java's %s.fromArray).\n", opGo, op, opGo, opGo, op)
	w("func Make%s(args ...%s) %s {\n\tvar l %s = Empty%s()\n\tfor i := len(args) - 1; i >= 0; i-- {\n\t\tl = Cons%s(args[i], l)\n\t}\n\treturn l\n}\n\n", opGo, elem, sortNm, sortNm, opGo, opGo)
	w("// %sFromArray is Java Gom's %s.fromArray(array).\n", opGo, op)
	w("func %sFromArray(array []%s) %s { return Make%s(array...) }\n\n", opGo, elem, sortNm, opGo)
}

// emitVariadicMappingGo writes the mapping of a variadic operator: the Empty<op>/Cons<op> operators (as Java Gom
// declares them) and the %oplist, built by Empty<Op>/Cons<Op> through
// the construction hooks.
func emitVariadicMappingGo(buf *bytes.Buffer, sortNm string, alt *gomast.AlternativeAlternative, structName, opGo, elemSort string) {
	op := alt.Name
	fmt.Fprintf(buf, "%%op %s Empty%s() {\n", sortNm, op)
	fmt.Fprintf(buf, "  is_fsym(t) { ($t).IsEmpty%s() }\n", op)
	fmt.Fprintf(buf, "  make() { Empty%s() }\n", opGo)
	fmt.Fprintf(buf, "}\n\n")
	fmt.Fprintf(buf, "%%op %s Cons%s(Head%s:%s, Tail%s:%s) {\n", sortNm, op, op, elemSort, op, sortNm)
	fmt.Fprintf(buf, "  is_fsym(t) { ($t).IsCons%s() }\n", op)
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
		// Java's toStringBuilder: the elements, then the tail when it is
		// not a list
		w("\tvar b strings.Builder\n\tb.WriteString(%q)\n", alt.Name+"(")
		w("\tvar cur %s = t\n\tfor {\n\t\tc, ok := cur.(*%s)\n\t\tif !ok {\n\t\t\tb.WriteString(\",\" + fmt.Sprint(cur))\n\t\t\tbreak\n\t\t}\n\t\tif c.empty {\n\t\t\tbreak\n\t\t}\n", altCodomain(g.mod, alt), structName)
		w("\t\tif c != t {\n\t\t\tb.WriteString(\",\")\n\t\t}\n\t\tb.WriteString(%s)\n\t\tcur = c.tail\n\t}\n", g.javaSlotString(elem, "c.head"))
		w("\tb.WriteString(\")\")\n\treturn b.String()\n}\n\n")
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
	opGo := g.opNames.of(alt, sortNm)
	if alternativeIsVariadic(alt) {
		elem := fieldTypeName(alternativeFields(alt)[0])
		elemGo := g.goType(elem, false)
		// AU and ACU absorb the empty list (Cons(x, Empty) is x, and the
		// neutral, if any): their lists end with an element (Java's
		// toATerm, a cast of the tail to ATermList, throws
		// ClassCastException there; deliberate extension, doc/go/README.md).
		th := theoryOf(g.pkgMods, g.mod, alt)
		endsWithElement := (th.kind == "AU" || th.kind == "ACU") && elem == sortNm
		if endsWithElement {
			w("// ToATerm is Java's toATerm: the list of the elements. The list (theory\n// %s) may end with an element instead of the empty list: it is its last\n// element (Java: ClassCastException).\n", th.kind)
		} else {
			w("// ToATerm is Java's toATerm: the list of the elements, ending with the\n// ATerm list of the tail (a tail that is not a list is Java's\n// ClassCastException).\n")
		}
		w("func (t *%s) ToATerm() aterm.ATerm {\n\tf := aterm.GetInstance()\n\tvar heads []%s\n\tvar cur %s = t\n", structName, elemGo, sortNm)
		w("\tfor {\n\t\tc, ok := cur.(*%s)\n\t\tif !ok || c.empty {\n\t\t\tbreak\n\t\t}\n\t\theads = append(heads, c.head)\n\t\tcur = c.tail\n\t}\n", structName)
		if endsWithElement {
			// after the loop, cur is the empty cell or the last element
			w("\tif _, ok := cur.(*%s); !ok {\n\t\theads = append(heads, cur)\n\t}\n\tl := f.MakeListEmpty()\n", structName)
		} else {
			w("\tl := f.MakeListEmpty()\n\tif c, ok := cur.(*%s); !ok || !c.empty {\n", structName)
			w("\t\ttl, isList := cur.ToATerm().(*aterm.ATermList)\n\t\tif !isList {\n\t\t\tpanic(&aterm.GomError{Msg: fmt.Sprintf(\"ClassCastException: the ATerm of %%v is not an ATermList\", cur)})\n\t\t}\n\t\tl = tl\n\t}\n")
		}
		w("\tfor i := len(heads) - 1; i >= 0; i-- {\n\t\tl = f.MakeList(%s, l)\n\t}\n\treturn l\n}\n\n", g.javaSlotATerm(elem, "heads[i]"))
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
		w("\tif r, ok := fromTerm%s(converted, conv); ok {\n\t\tresults = append(results, r)\n\t}\n", opBase(alt)+s)
	}
	w("\tif len(results) == 0 {\n\t\tpanic(&aterm.GomError{Msg: fmt.Sprintf(\"%%v is not a %s\", trm), IllegalArgument: true})\n\t}\n\treturn results[0]\n}\n\n", s)
	w("// %sFromString is Java Gom's %s.fromString(s).\n", s, s)
	w("func %sFromString(s string) %s { return %sFromTerm(aterm.GetInstance().Parse(s)) }\n\n", s, s, s)
	w("// %sFromStream is Java Gom's %s.fromStream(stream) (an IOException is\n// the error).\n", s, s)
	w("func %sFromStream(stream io.Reader) (%s, error) {\n\tat, err := aterm.GetInstance().ReadFromTextFile(stream)\n\tif err != nil {\n\t\treturn nil, err\n\t}\n\treturn %sFromTerm(at), nil\n}\n\n", s, s, s)
}
