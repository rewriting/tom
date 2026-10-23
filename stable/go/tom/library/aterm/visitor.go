package aterm

// Visitor is the analogue of `jjtraveler.Visitor` from Java's
// aterm.jjtraveler — a single-method callback applied to a term.
// Returning a non-nil error short-circuits the traversal.
type Visitor interface {
	Visit(t ATerm) (ATerm, error)
}

// VisitorFunc adapts an ordinary function to the Visitor interface,
// so callers can pass plain closures (the common case).
type VisitorFunc func(t ATerm) (ATerm, error)

// Visit forwards to the underlying function.
func (f VisitorFunc) Visit(t ATerm) (ATerm, error) { return f(t) }

// TopDown applies v to the term, then to each child (recursing).
// Mirrors `jjtraveler.TopDown(v).visit(term)` from VisitorBenchmark.
// The traversal threads the visitor's return through children so a
// substitution-style rewrite is expressible.
func TopDown(v Visitor, t ATerm) (ATerm, error) {
	t, err := v.Visit(t)
	if err != nil {
		return t, err
	}
	switch x := t.(type) {
	case *ATermAppl:
		if x.fun.arity == 0 {
			return t, nil
		}
		newArgs := make([]ATerm, len(x.args))
		changed := false
		for i, a := range x.args {
			na, err := TopDown(v, a)
			if err != nil {
				return t, err
			}
			newArgs[i] = na
			if na != a {
				changed = true
			}
		}
		if !changed {
			return t, nil
		}
		return x.factory.makeApplAnno(x.fun, newArgs, x.annos), nil
	case *ATermList:
		if x.IsEmpty() {
			return t, nil
		}
		newHead, err := TopDown(v, x.head)
		if err != nil {
			return t, err
		}
		newTailAny, err := TopDown(v, x.tail)
		if err != nil {
			return t, err
		}
		newTail := newTailAny.(*ATermList)
		if newHead == x.head && newTail == x.tail {
			return t, nil
		}
		return x.factory.MakeList(newHead, newTail), nil
	case *ATermPlaceholder:
		newInner, err := TopDown(v, x.placeholderType)
		if err != nil {
			return t, err
		}
		if newInner == x.placeholderType {
			return t, nil
		}
		return x.factory.MakePlaceholder(newInner), nil
	}
	return t, nil
}

// BottomUp applies v to children first, then to the term itself.
// Common companion to TopDown for rewriting passes that need leaf
// values to settle before the parent looks at them.
func BottomUp(v Visitor, t ATerm) (ATerm, error) {
	switch x := t.(type) {
	case *ATermAppl:
		if x.fun.arity > 0 {
			newArgs := make([]ATerm, len(x.args))
			changed := false
			for i, a := range x.args {
				na, err := BottomUp(v, a)
				if err != nil {
					return t, err
				}
				newArgs[i] = na
				if na != a {
					changed = true
				}
			}
			if changed {
				t = x.factory.makeApplAnno(x.fun, newArgs, x.annos)
			}
		}
	case *ATermList:
		if !x.IsEmpty() {
			newHead, err := BottomUp(v, x.head)
			if err != nil {
				return t, err
			}
			newTailAny, err := BottomUp(v, x.tail)
			if err != nil {
				return t, err
			}
			newTail := newTailAny.(*ATermList)
			if newHead != x.head || newTail != x.tail {
				t = x.factory.MakeList(newHead, newTail)
			}
		}
	case *ATermPlaceholder:
		newInner, err := BottomUp(v, x.placeholderType)
		if err != nil {
			return t, err
		}
		if newInner != x.placeholderType {
			t = x.factory.MakePlaceholder(newInner)
		}
	}
	return v.Visit(t)
}

// NodeCounter is the Visitor implementation from
// VisitorBenchmark.java — a counter that touches every node exactly
// once during the traversal. Useful for benchmarks and sanity
// checks on the visitor wiring.
type NodeCounter struct {
	Count int
}

// Visit increments the counter and returns the term unchanged.
func (c *NodeCounter) Visit(t ATerm) (ATerm, error) {
	c.Count++
	return t, nil
}
