package aterm

// matchPattern parses `pattern` against the receiver's factory and
// tries to match it against `subject`. Returns a fresh []any of the
// captured values on success (preserving placeholder order
// left-to-right) or nil on failure.
//
// Placeholder taxonomy supported (mirrors Java aterm.pure):
//
//   - <int>         → captures an int64
//   - <long>        → captures an int64
//   - <real>        → captures a float64
//   - <str>         → captures a string (quoted appl name)
//   - <appl>        → captures a string (function-symbol name)
//   - <fun>         → same as <appl>
//   - <term>        → captures the matching ATerm verbatim
//   - <list>        → captures the matching ATermList (or the tail)
//   - <placeholder> → captures the matched placeholder term
//   - <fun(args)>   → composite: captures the function name AS WELL AS
//                     every placeholder inside `args`
//   - <ident()>     → like <fun(<>)>, fixed-name predicate (no name capture)
//
// Patterns are parsed once (via the term parser) so placeholders are
// `*ATermPlaceholder` carrying an inner type term. The inner term is
// then dispatched here.
func matchPattern(subject ATerm, pattern string) []any {
	f := subject.Factory()
	pat := f.Parse(pattern)
	captured := []any{}
	if !matchTerm(subject, pat, &captured) {
		return nil
	}
	return captured
}

// matchTerm walks the pattern term and subject simultaneously,
// recording captured values into *out. Returns true on success.
func matchTerm(subject, pat ATerm, out *[]any) bool {
	// Same-instance shortcut.
	if subject == pat {
		return true
	}
	// Placeholder dispatch.
	if ph, ok := pat.(*ATermPlaceholder); ok {
		return matchPlaceholder(subject, ph, out)
	}
	// Same-shape, same-type structural recursion.
	switch p := pat.(type) {
	case *ATermInt:
		s, ok := subject.(*ATermInt)
		return ok && s.value == p.value
	case *ATermLong:
		s, ok := subject.(*ATermLong)
		return ok && s.value == p.value
	case *ATermReal:
		s, ok := subject.(*ATermReal)
		return ok && s.value == p.value
	case *ATermAppl:
		s, ok := subject.(*ATermAppl)
		if !ok {
			return false
		}
		if s.fun != p.fun || len(s.args) != len(p.args) {
			return false
		}
		for i := range p.args {
			if !matchTerm(s.args[i], p.args[i], out) {
				return false
			}
		}
		return true
	case *ATermList:
		s, ok := subject.(*ATermList)
		if !ok {
			return false
		}
		return matchList(s, p, out)
	}
	return false
}

func matchList(subject, pat *ATermList, out *[]any) bool {
	for {
		if pat.IsEmpty() {
			return subject.IsEmpty()
		}
		// <list> as tail-of-list is a special "swallow everything"
		// placeholder.
		if ph, ok := pat.head.(*ATermPlaceholder); ok {
			if appl, ok := ph.placeholderType.(*ATermAppl); ok && appl.fun.name == "list" && appl.fun.arity == 0 {
				if !pat.tail.IsEmpty() {
					return false
				}
				*out = append(*out, subject)
				return true
			}
		}
		if subject.IsEmpty() {
			return false
		}
		if !matchTerm(subject.head, pat.head, out) {
			return false
		}
		subject = subject.tail
		pat = pat.tail
	}
}

func matchPlaceholder(subject ATerm, ph *ATermPlaceholder, out *[]any) bool {
	inner := ph.placeholderType
	appl, ok := inner.(*ATermAppl)
	if !ok {
		return false
	}
	switch {
	case appl.fun.name == "int" && appl.fun.arity == 0:
		if s, ok := subject.(*ATermInt); ok {
			*out = append(*out, int64(s.value))
			return true
		}
		return false
	case appl.fun.name == "long" && appl.fun.arity == 0:
		if s, ok := subject.(*ATermLong); ok {
			*out = append(*out, s.value)
			return true
		}
		return false
	case appl.fun.name == "real" && appl.fun.arity == 0:
		if s, ok := subject.(*ATermReal); ok {
			*out = append(*out, s.value)
			return true
		}
		return false
	case appl.fun.name == "str" && appl.fun.arity == 0:
		if s, ok := subject.(*ATermAppl); ok && s.fun.quoted && s.fun.arity == 0 {
			*out = append(*out, s.fun.name)
			return true
		}
		return false
	case (appl.fun.name == "appl" || appl.fun.name == "fun") && appl.fun.arity == 0:
		if s, ok := subject.(*ATermAppl); ok && s.fun.arity == 0 {
			*out = append(*out, s.fun.name)
			return true
		}
		return false
	case appl.fun.name == "term" && appl.fun.arity == 0:
		*out = append(*out, subject)
		return true
	case appl.fun.name == "list" && appl.fun.arity == 0:
		if s, ok := subject.(*ATermList); ok {
			*out = append(*out, s)
			return true
		}
		return false
	case appl.fun.name == "placeholder" && appl.fun.arity == 0:
		if _, ok := subject.(*ATermPlaceholder); ok {
			*out = append(*out, subject)
			return true
		}
		return false
	case appl.fun.arity >= 0:
		// `<fname(<...>)>` — composite placeholder. Subject must
		// be an appl with name == fname (or open-name when fname
		// is `appl`/`fun`/`term`) and matching arity.
		s, ok := subject.(*ATermAppl)
		if !ok || len(s.args) != len(appl.args) {
			return false
		}
		captureName := false
		switch appl.fun.name {
		case "appl", "fun":
			captureName = true
		case "term":
			// `<term(<...>)>` — capture the whole appl too? Java
			// captures just inner subterms; we follow.
		default:
			if s.fun.name != appl.fun.name {
				return false
			}
		}
		if captureName {
			*out = append(*out, s.fun.name)
		}
		for i := range appl.args {
			if !matchTerm(s.args[i], appl.args[i], out) {
				return false
			}
		}
		return true
	}
	return false
}

// ---------------------------------------------------------------------------
// make
// ---------------------------------------------------------------------------

// makePattern is the back-pointer used by every term's Make(args)
// helper. Most callers should reach for factory.Make("pattern", args)
// directly.
func makePattern(t ATerm, args []any) ATerm {
	r := &makeRunner{factory: t.Factory(), args: args}
	return r.makeFrom(t)
}

type makeRunner struct {
	factory *Factory
	args    []any
	pos     int
}

func (r *makeRunner) next() any {
	if r.pos >= len(r.args) {
		panic("aterm: makePattern ran out of arguments")
	}
	v := r.args[r.pos]
	r.pos++
	return v
}

func (r *makeRunner) makeFrom(t ATerm) ATerm {
	switch x := t.(type) {
	case *ATermPlaceholder:
		return r.fillPlaceholder(x)
	case *ATermAppl:
		// Recurse into args.
		newArgs := make([]ATerm, len(x.args))
		for i, a := range x.args {
			newArgs[i] = r.makeFrom(a)
		}
		return r.factory.MakeAppl(x.fun, newArgs...)
	case *ATermList:
		if x.IsEmpty() {
			return x
		}
		// Handle <list> at head: splice the entire arg.
		if ph, ok := x.head.(*ATermPlaceholder); ok {
			if appl, ok := ph.placeholderType.(*ATermAppl); ok && appl.fun.name == "list" && appl.fun.arity == 0 {
				v := r.next()
				if l, ok := v.(*ATermList); ok {
					return l.Concat(r.makeFrom(x.tail).(*ATermList))
				}
				panic("aterm: <list> placeholder expects an ATermList argument")
			}
		}
		head := r.makeFrom(x.head)
		tail := r.makeFrom(x.tail).(*ATermList)
		return r.factory.MakeList(head, tail)
	}
	return t
}

func (r *makeRunner) fillPlaceholder(ph *ATermPlaceholder) ATerm {
	inner, ok := ph.placeholderType.(*ATermAppl)
	if !ok {
		return ph
	}
	switch inner.fun.name {
	case "int":
		v := r.next()
		switch x := v.(type) {
		case int:
			return r.factory.MakeInt(x)
		case int32:
			return r.factory.MakeInt(int(x))
		case int64:
			return r.factory.MakeInt(int(x))
		}
		panic("aterm: <int> placeholder expects an int arg")
	case "long":
		v := r.next()
		switch x := v.(type) {
		case int:
			return r.factory.MakeLong(int64(x))
		case int64:
			return r.factory.MakeLong(x)
		}
		panic("aterm: <long> placeholder expects an int64 arg")
	case "real":
		v := r.next()
		if x, ok := v.(float64); ok {
			return r.factory.MakeReal(x)
		}
		panic("aterm: <real> placeholder expects a float64 arg")
	case "str":
		v := r.next()
		if s, ok := v.(string); ok {
			fun := r.factory.MakeAFun(s, 0, true)
			return r.factory.MakeAppl(fun)
		}
		panic("aterm: <str> placeholder expects a string arg")
	case "appl", "fun":
		// Composite or plain: composite if inner has args.
		if len(inner.args) > 0 {
			v := r.next()
			name, ok := v.(string)
			if !ok {
				panic("aterm: <fun(...)> head expects a string arg")
			}
			subArgs := make([]ATerm, len(inner.args))
			for i, a := range inner.args {
				subArgs[i] = r.makeFrom(a)
			}
			fun := r.factory.MakeAFun(name, len(subArgs), false)
			return r.factory.MakeAppl(fun, subArgs...)
		}
		v := r.next()
		if s, ok := v.(string); ok {
			fun := r.factory.MakeAFun(s, 0, false)
			return r.factory.MakeAppl(fun)
		}
		panic("aterm: <appl> placeholder expects a string arg")
	case "term":
		// Composite placeholder (e.g. `<term()>`) means: subject must
		// match name pattern at this slot; for `make`, the next arg
		// IS the term to splice in.
		v := r.next()
		if t, ok := v.(ATerm); ok {
			return t
		}
		panic("aterm: <term> placeholder expects an ATerm arg")
	case "list":
		v := r.next()
		if l, ok := v.(*ATermList); ok {
			return l
		}
		panic("aterm: <list> placeholder expects an ATermList arg")
	case "placeholder":
		v := r.next()
		if t, ok := v.(ATerm); ok {
			return t
		}
		panic("aterm: <placeholder> expects an ATerm arg")
	}
	// Default — pattern with custom head e.g. `<foo>` is a no-op.
	return ph
}
