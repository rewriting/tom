package sl

// Strategy is the central abstraction of the library: a tree-rewrite
// function. A strategy reads a subject (of any type), traverses it
// through an [Introspector], and returns a (possibly new) subject —
// or [ErrVisitFailure] to indicate a controlled failure that the
// surrounding combinator (typically [Choice]) is expected to recover
// from.
//
// Beyond VisitLight, each strategy exposes its sub-strategies through
// ChildCount/ChildAt/SetChildAt. The traversal-style introspection
// here mirrors [Visitable] but for the strategy tree itself; [Mu]
// relies on it to splice a recursive reference into every [MuVar] node
// in its body.
type Strategy interface {
	// VisitLight applies the strategy to subject and returns the
	// (possibly new) subject. A returned [ErrVisitFailure] means a
	// controlled failure recoverable by an enclosing combinator; any
	// other non-nil error is fatal and propagates up.
	VisitLight(subject any, intro Introspector) (any, error)

	// ChildCount, ChildAt, SetChildAt expose the strategy's argument
	// slots for the [Mu]-expansion graph walk. Leaf strategies
	// (Identity, Fail, MuVar) return 0 from ChildCount.
	ChildCount() int
	ChildAt(i int) Strategy
	SetChildAt(i int, s Strategy)
}

// abstractCombinator is the shared base for every multi-argument
// combinator (Sequence/Choice/All/…). It owns the args slice and
// implements the Strategy "tree" methods; concrete combinators embed
// it and provide their own VisitLight.
type abstractCombinator struct {
	args []Strategy
}

func (a *abstractCombinator) ChildCount() int           { return len(a.args) }
func (a *abstractCombinator) ChildAt(i int) Strategy    { return a.args[i] }
func (a *abstractCombinator) SetChildAt(i int, s Strategy) { a.args[i] = s }

// initSubterm initialises the argument slots of an embedded
// abstractCombinator — the Go analogue of AbstractStrategyCombinator.
// initSubterm in Java. Variadic so callers can pass 0, 1, or 2 sub-
// strategies without juggling slice literals.
func (a *abstractCombinator) initSubterm(args ...Strategy) {
	a.args = args
}
