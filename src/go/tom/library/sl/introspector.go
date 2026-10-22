package sl

// Introspector decouples a [Strategy] from the concrete type of the
// AST it traverses. The Java reference uses this same indirection to
// avoid forcing every visited type to implement Visitable directly.
//
// Implementations are expected to be cheap and side-effect-free: a
// strategy may call GetChildAt/GetChildCount many times during a
// traversal. SetChildAt / SetChildren build a *new* node when any
// child changed, so the returned value may have a different identity
// than the input even though it is the same type.
type Introspector interface {
	GetChildCount(subject any) int
	GetChildAt(subject any, i int) any
	SetChildAt(subject any, i int, child any) any
	GetChildren(subject any) []any
	SetChildren(subject any, children []any) any
}

// Visitable is a marker interface for objects that can self-describe
// their children. Unlike the Java equivalent, our Strategy methods take
// [any] (not Visitable), so an Introspector is enough to traverse
// types that do not implement this interface. It exists to give an
// idiomatic Go-side hook for AST types that *can* expose children
// directly — useful for tests and ad-hoc traversal helpers.
type Visitable interface {
	Children() []any
	SetChildren(children []any) any
}

// VisitableIntrospector is an [Introspector] that defers to a
// [Visitable] implementation on each subject. Used by tests that
// instantiate a toy tree implementing Visitable.
type VisitableIntrospector struct{}

func (VisitableIntrospector) GetChildren(subject any) []any {
	if v, ok := subject.(Visitable); ok {
		return v.Children()
	}
	return nil
}

func (vi VisitableIntrospector) GetChildCount(subject any) int {
	return len(vi.GetChildren(subject))
}

func (vi VisitableIntrospector) GetChildAt(subject any, i int) any {
	return vi.GetChildren(subject)[i]
}

func (vi VisitableIntrospector) SetChildAt(subject any, i int, child any) any {
	children := vi.GetChildren(subject)
	dup := make([]any, len(children))
	copy(dup, children)
	dup[i] = child
	return vi.SetChildren(subject, dup)
}

func (VisitableIntrospector) SetChildren(subject any, children []any) any {
	if v, ok := subject.(Visitable); ok {
		return v.SetChildren(children)
	}
	return subject
}
