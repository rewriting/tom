package backend

// Variadic operators of the user modules (the Java API backend, see
// javaapi.go) are represented as Java Gom represents them: maximally
// shared cons cells. For an operator op of sort S over E, the Go type
// <Op><S> (e.g. ConcList) is Java's abstract class op: a value is either
// a cell — Java's Cons<op>, holding a head (E) and a tail (any S) — or
// the empty list — Java's Empty<op>. Cells are built through the
// package's shared-objects factory, so equal lists are the same pointer
// and every list shares its tails: Cons<Op>, GetTail<Op>, IsEmpty<Op>
// are O(1), as in Java.
//
// A tail that is not a list of op (Java's Cons<op>(e, x) with x of sort
// S built by another operator: the normal form of the AU/ACU lists of
// two elements or more, or the result of a make_empty hook) ends the
// list; the list API treats it as Java does (ToArray, Length, String,
// Reverse, Append, ToATerm; the Visitable children panic with a GomError
// when its sort is not the element sort, as Java's typed toArray throws
// ArrayStoreException).
//
// The Visitable children of a list are its elements (Java's getChildren
// of a variadic operator: toArray, computed once and cached in the term,
// as Java does), so a strategy such as All visits each element.

import (
	"fmt"
	"strings"

	gomast "tom/gom/adt"
)

// emitVariadicCells writes the term type of a variadic operator of a
// user module and its sharedobjects/Visitable methods (the list
// constructors are written by emitVariadicJava, the Java list API by
// emitJavaAPIMethods).
func (g *gen) emitVariadicCells(sortNm string, alt *gomast.AlternativeAlternative, structName, elem string) {
	w := func(format string, args ...any) { fmt.Fprintf(&g.buf, format, args...) }
	op := alt.Name
	opGo := g.opNames.of(alt, sortNm)

	w("// %s is the term type of the variadic operator `%s(...)` of sort %s\n", structName, op, sortNm)
	w("// (Java's abstract class %s): a maximally-shared cons cell (Java's\n", op)
	w("// Cons%s: a head and a tail) or the empty list (Java's Empty%s).\n", op, op)
	w("type %s struct {\n", structName)
	w("\thead  %s\n", elem)
	w("\ttail  %s\n", sortNm)
	w("\tempty bool\n")
	w("\thash  uint32\n")
	w("\t// children caches the Visitable children (Java's children field)\n")
	w("\tchildren atomic.Pointer[[]any]\n")
	w("}\n\n")

	w("func (*%s) is%s() {}\n\n", structName, sortNm)
	w("func (t *%s) Hash() uint32 { return t.hash }\n\n", structName)

	// Equivalent: a cell is shared once its head and tail are (O(1)).
	w("func (t *%s) Equivalent(other sharedobjects.Term) bool {\n", structName)
	w("\to, ok := other.(*%s)\n", structName)
	w("\treturn ok && t.empty == o.empty && t.head == o.head && t.tail == o.tail\n}\n\n")

	if g.noSharing {
		headNeq := "c.head != d.head"
		if g.isShared(elem) {
			headNeq = "!c.head.DeepEquals(d.head)"
		}
		w("func (t *%s) DeepEquals(other any) bool {\n", structName)
		w("\td, ok := other.(*%s)\n\tif !ok {\n\t\treturn false\n\t}\n", structName)
		w("\tc := t\n\tfor {\n")
		w("\t\tif c.empty || d.empty {\n\t\t\treturn c.empty == d.empty\n\t\t}\n")
		w("\t\tif %s {\n\t\t\treturn false\n\t\t}\n", headNeq)
		w("\t\tct, ok1 := c.tail.(*%s)\n\t\tdt, ok2 := d.tail.(*%s)\n", structName, structName)
		w("\t\tif !ok1 || !ok2 {\n\t\t\treturn c.tail.DeepEquals(d.tail)\n\t\t}\n")
		w("\t\tc, d = ct, dt\n\t}\n}\n\n")
	}

	w("func (t *%s) Duplicate() sharedobjects.Term {\n", structName)
	w("\treturn &%s{head: t.head, tail: t.tail, empty: t.empty, hash: t.hash}\n}\n\n", structName)

	// Visitable: the elements of the list (Java's toArray), cached.
	cast := "c"
	if elem != "any" {
		cast = "c.(" + elem + ")"
	}
	w("// elements returns the cached Visitable children of the list: its\n// elements (Java's getChildren, i.e. toArray).\n")
	if !sameSortVariadic(g.mod, alt) {
		w("// Java's getChildren is the typed toArray(new %s[]{}): a last tail\n// that is not a list (of sort %s) cannot be stored in it\n// (ArrayStoreException, a *aterm.GomError here).\n", elem, sortNm)
	}
	w("func (t *%s) elements() []any {\n", structName)
	w("\tif c := t.children.Load(); c != nil {\n\t\treturn *c\n\t}\n")
	if !sameSortVariadic(g.mod, alt) {
		w("\tvar cur %s = t\n\tfor {\n\t\tc, ok := cur.(*%s)\n\t\tif !ok || c.empty {\n\t\t\tbreak\n\t\t}\n\t\tcur = c.tail\n\t}\n", sortNm, structName)
		w("\tif c, ok := cur.(*%s); !ok || !c.empty {\n", structName)
		w("\t\tpanic(&aterm.GomError{Msg: fmt.Sprintf(\"ArrayStoreException: the tail %%v of a %s list is not a %s\", cur)})\n\t}\n", alt.Name, elem)
	}
	w("\tc := t.ToArray()\n\tt.children.Store(&c)\n\treturn c\n}\n\n")
	w("func (t *%s) ChildCount() int { return len(t.elements()) }\n\n", structName)
	w("func (t *%s) ChildAt(i int) any { return t.elements()[i] }\n\n", structName)
	w("func (t *%s) SetChildAt(i int, child any) any {\n", structName)
	w("\tdup := t.Children()\n\tdup[i] = child\n\treturn t.SetChildren(dup)\n}\n\n")
	w("func (t *%s) Children() []any { return append([]any(nil), t.elements()...) }\n\n", structName)
	w("// SetChildren is Java's setChildren of a list: %sFromArray(children).\n", opGo)
	w("func (t *%s) SetChildren(children []any) any {\n", structName)
	w("\targs := make([]%s, len(children))\n\tfor i, c := range children {\n\t\targs[i] = %s\n\t}\n", elem, cast)
	w("\treturn Make%s(args...)\n}\n\n", opGo)
}

// hashOfElem is the Go expression of the hash of a list element x of Go
// type elem: a term's own hash (an own sort, or an ATerm), or the hash of
// the builtin value computed directly (no formatting).
func (g *gen) hashOfElem(elem, x string) string {
	switch {
	case g.isShared(elem), elem == "aterm.ATerm", elem == "*aterm.ATermList":
		return x + ".Hash()"
	case elem == "string":
		return "sharedobjects.StringHash(" + x + ")"
	case elem == "int64":
		return "sharedobjects.IntHash(" + x + ")"
	case elem == "rune":
		return "sharedobjects.IntHash(int64(" + x + "))"
	case elem == "bool":
		return "sharedobjects.BoolHash(" + x + ")"
	case elem == "float64":
		return "sharedobjects.Float64Hash(" + x + ")"
	case elem == "float32":
		return "sharedobjects.Float32Hash(" + x + ")"
	}
	return fmt.Sprintf("sharedobjects.StringHash(fmt.Sprintf(\"%%v\", %s))", x)
}

// expandCells substitutes the placeholders of a code template: @ST@ the
// term type, @S@ the sort, @E@ the Go element type, @Op@ the exported
// operator name, @op@ the operator name.
func (g *gen) expandCells(tmpl, st, sortNm, elem string, alt *gomast.AlternativeAlternative) string {
	return strings.NewReplacer("@ST@", st, "@S@", sortNm, "@E@", elem, "@Op@", g.opNames.of(alt, sortNm), "@op@", alt.Name).Replace(tmpl)
}

// listAPITemplate is Java Gom's list API of a variadic operator
// (VariadicOperatorTemplate: length, toArray, reverse, append), on cells.
const listAPITemplate = `// Length is Java's length(): the number of elements of the list (a
// tail that is not a @op@ list counts as one element).
func (t *@ST@) Length() int {
	res := 0
	var tl @S@ = t
	for {
		c, ok := tl.(*@ST@)
		if !ok || c.empty {
			return res
		}
		res++
		tl = c.tail
		if _, ok := tl.(*@ST@); !ok {
			res++
		}
	}
}

// ToArray returns the elements of the list (Java's Object[] toArray()),
// ending with its tail when that is not a @op@ list.
func (t *@ST@) ToArray() []any {
	out := make([]any, 0, t.Length())
	var cur @S@ = t
	for {
		c, ok := cur.(*@ST@)
		if !ok || c.empty {
			break
		}
		out = append(out, c.head)
		cur = c.tail
	}
	if c, ok := cur.(*@ST@); !ok || !c.empty {
		out = append(out, cur)
	}
	return out
}

`

// reverseTemplate is Java's reverse(): the reversed list, built by
// Cons<Op> (through the hooks); @LAST@ handles the tail that is not a
// list.
const reverseTemplate = `// Reverse is Java's reverse(): the reversed list, built by Cons@Op@
// (through the hooks).
func (t *@ST@) Reverse() @S@ {
	if t.empty {
		return t
	}
	var cur @S@ = t
	var rev @S@ = Empty@Op@()
	for {
		c, ok := cur.(*@ST@)
		if !ok || c.empty {
			break
		}
		rev = Cons@Op@(c.head, rev)
		cur = c.tail
	}
@LAST@	return rev
}

`

// appendTemplate is Java's append(element) — Cons<op>.make(head,
// tail.append(element)) down the list — written as a loop (the same Cons
// calls, innermost first).
const appendTemplate = `// Append is Java's append(element): the list with element added at its
// end, built by Cons@Op@ (through the hooks).
func (t *@ST@) Append(element @E@) @S@ {
	var heads []@E@
	var cur @S@ = t
	for {
		c, ok := cur.(*@ST@)
		if !ok || c.empty {
			break
		}
		heads = append(heads, c.head)
		cur = c.tail
	}
	var res @S@
	if c, ok := cur.(*@ST@); ok && c.empty {
		res = Cons@Op@(element, cur)
	} else {
@NONLIST@	}
	for i := len(heads) - 1; i >= 0; i-- {
		res = Cons@Op@(heads[i], res)
	}
	return res
}

`

// emitListAPICells writes the list API of the variadic operator alt.
func (g *gen) emitListAPICells(sortNm string, alt *gomast.AlternativeAlternative, st string) {
	elem := g.elemGoType(alt)
	same := sameSortVariadic(g.mod, alt)
	g.buf.WriteString(g.expandCells(listAPITemplate, st, sortNm, elem, alt))
	last := ""
	if same {
		// domain = codomain: the last tail is the first element
		last = "\tif c, ok := cur.(*@ST@); !ok || !c.empty {\n\t\trev = Cons@Op@(cur, rev)\n\t}\n"
	}
	g.buf.WriteString(g.expandCells(strings.Replace(reverseTemplate, "@LAST@", last, 1), st, sortNm, elem, alt))
	nonList := "\t\tres = Cons@Op@(element, cur)\n"
	if same {
		nonList = "\t\tres = Cons@Op@(cur, element)\n"
	}
	g.buf.WriteString(g.expandCells(strings.Replace(appendTemplate, "@NONLIST@", nonList, 1), st, sortNm, elem, alt))
}
