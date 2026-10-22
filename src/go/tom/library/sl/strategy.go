package sl

import "errors"

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

	// Visit is the environment-based entry point (Java
	// visit(T any, Introspector)): it gives the strategy tree a fresh
	// [Environment] rooted at subject, runs [Strategy.VisitEnv] and
	// returns the (possibly new) root, or [ErrVisitFailure]. Every
	// strategy implements it as `return VisitStrategy(self, subject,
	// intro)`.
	Visit(subject any, intro Introspector) (any, error)

	// VisitWith runs the strategy in an existing environment (Java
	// visit(Environment, Introspector)) — what a user strategy uses to
	// run another strategy at its own position — and returns the new
	// current subject, or [ErrVisitFailure]. Every strategy implements
	// it as `return VisitStrategyWith(self, env, intro)`.
	VisitWith(env *Environment, intro Introspector) (any, error)

	// VisitEnv applies the strategy to the current subject of its
	// environment, which it reads and updates in place (Java
	// int visit(Introspector)). It returns [StatusSuccess], or another
	// status (normally [StatusFailure]) on failure.
	VisitEnv(intro Introspector) int

	// SetEnvironment and GetEnvironment give access to the environment
	// of the strategy. GetEnvironment panics when none was set (Java:
	// RuntimeException "environment not initialized").
	SetEnvironment(env *Environment)
	GetEnvironment() *Environment

	// ChildCount, ChildAt, SetChildAt expose the strategy's argument
	// slots for the [Mu]-expansion graph walk and for [Init]. Leaf
	// strategies (Identity, Fail, MuVar) return 0 from ChildCount.
	ChildCount() int
	ChildAt(i int) Strategy
	SetChildAt(i int, s Strategy)
}

// AbstractStrategy holds the environment of a strategy — Java's
// AbstractStrategy. Every strategy of this package embeds it (through
// abstractCombinator); a strategy written outside the package embeds
// it (or [AbstractStrategyBasic]) and implements VisitLight, VisitEnv,
// Visit and VisitWith.
type AbstractStrategy struct {
	environment *Environment
}

// SetEnvironment sets the environment of the strategy (not of its
// children: see [Init]).
func (a *AbstractStrategy) SetEnvironment(env *Environment) { a.environment = env }

// GetEnvironment returns the environment of the strategy; it panics
// when none was set, as Java throws "environment not initialized".
// Inside the host code of a %strategy rule, the environment is
// `tom__strat.GetEnvironment()`.
func (a *AbstractStrategy) GetEnvironment() *Environment {
	if a.environment == nil {
		panic("environment not initialized")
	}
	return a.environment
}

// HasEnvironment reports whether an environment was set — the Go
// spelling of Java's `environment != null` test in generated code.
func (a *AbstractStrategy) HasEnvironment() bool { return a.environment != nil }

func (a *AbstractStrategy) environmentOrNil() *Environment { return a.environment }

// GetRoot, SetRoot, GetSubject, SetSubject, GetPosition and
// GetAncestor are shortcuts to the same methods of the environment.
func (a *AbstractStrategy) GetRoot() any           { return a.GetEnvironment().GetRoot() }
func (a *AbstractStrategy) SetRoot(root any)       { a.GetEnvironment().SetRoot(root) }
func (a *AbstractStrategy) GetSubject() any        { return a.GetEnvironment().GetSubject() }
func (a *AbstractStrategy) SetSubject(subject any) { a.GetEnvironment().SetSubject(subject) }
func (a *AbstractStrategy) GetPosition() *Position { return a.GetEnvironment().GetPosition() }
func (a *AbstractStrategy) GetAncestor() any       { return a.GetEnvironment().GetAncestor() }

// environmentHolder is implemented by every type embedding
// AbstractStrategy (Java's `instanceof AbstractStrategy`).
type environmentHolder interface{ environmentOrNil() *Environment }

// Init gives env to s and, recursively, to all its sub-strategies
// (Java AbstractStrategy.init(Strategy, Environment)). A strategy that
// already holds env stops the walk, which also stops it on the cycles
// that [Mu] expansion creates.
func Init(s Strategy, env *Environment) {
	if h, ok := s.(environmentHolder); ok && h.environmentOrNil() == env {
		return
	}
	s.SetEnvironment(env)
	for i := 0; i < s.ChildCount(); i++ {
		if child := s.ChildAt(i); child != nil {
			Init(child, env)
		}
	}
}

// VisitStrategy is the body of every Visit method (Java
// AbstractStrategy.visit(T, Introspector)): it initialises s with a
// fresh environment rooted at subject, runs s.VisitEnv and returns the
// root, or [ErrVisitFailure].
func VisitStrategy(s Strategy, subject any, intro Introspector) (any, error) {
	env := NewEnvironmentWith(intro)
	Init(s, env)
	env.SetRoot(subject)
	if s.VisitEnv(intro) != StatusSuccess {
		return nil, ErrVisitFailure
	}
	return env.GetRoot(), nil
}

// VisitStrategyWith is the body of every VisitWith method (Java
// AbstractStrategy.visit(Environment, Introspector)): it initialises s
// with env, runs s.VisitEnv and returns the current subject, or
// [ErrVisitFailure].
func VisitStrategyWith(s Strategy, env *Environment, intro Introspector) (any, error) {
	Init(s, env)
	if s.VisitEnv(intro) != StatusSuccess {
		return nil, ErrVisitFailure
	}
	return env.GetSubject(), nil
}

// VisitBasic is the VisitEnv of a strategy defined by its VisitLight
// (Java AbstractStrategyBasic.visit(Introspector)): it applies
// s.VisitLight to the current subject and stores the result. A
// failure gives [StatusFailure]; any other error is fatal and panics,
// as the Java RuntimeException propagates.
func VisitBasic(s Strategy, intro Introspector) int {
	env := s.GetEnvironment()
	v, err := s.VisitLight(env.GetSubject(), intro)
	if err != nil {
		if errors.Is(err, ErrVisitFailure) {
			return StatusFailure
		}
		panic(err)
	}
	env.SetSubject(v)
	return StatusSuccess
}

// abstractCombinator is the shared base for every multi-argument
// combinator (Sequence/Choice/All/…) — Java's
// AbstractStrategyCombinator. It owns the args slice and the
// environment and implements the Strategy "tree" methods; concrete
// combinators embed it and provide VisitLight, VisitEnv, Visit and
// VisitWith.
type abstractCombinator struct {
	AbstractStrategy
	args []Strategy
}

func (a *abstractCombinator) ChildCount() int              { return len(a.args) }
func (a *abstractCombinator) ChildAt(i int) Strategy       { return a.args[i] }
func (a *abstractCombinator) SetChildAt(i int, s Strategy) { a.args[i] = s }

// initSubterm initialises the argument slots of an embedded
// abstractCombinator — the Go analogue of AbstractStrategyCombinator.
// initSubterm in Java. Variadic so callers can pass 0, 1, or 2 sub-
// strategies without juggling slice literals.
func (a *abstractCombinator) initSubterm(args ...Strategy) {
	a.args = args
}

// VisitCongruence is the VisitEnv of a congruence strategy generated
// by Gom (`_f(s1, …, sn)`, Go `VisitF`) once the current subject is
// known to be an f: it applies args[i] to the i-th child (args[0] to
// every element when variadic) inside the environment, then rebuilds
// the subject iff a child changed. Port of the visit(Introspector) of
// Gom's SOpTemplate.
func VisitCongruence(env *Environment, intro Introspector, args []Strategy, variadic bool) int {
	if variadic && len(args) == 0 {
		return StatusSuccess
	}
	subject := env.GetSubject()
	count := intro.GetChildCount(subject)
	var children []any
	for i := range count {
		arg := args[0]
		if !variadic {
			arg = args[i]
		}
		oldChild := intro.GetChildAt(subject, i)
		env.Down(i + 1)
		if status := arg.VisitEnv(intro); status != StatusSuccess {
			env.UpLocal()
			return status
		}
		newChild := env.GetSubject()
		if children != nil {
			children[i] = newChild
		} else if !same(newChild, oldChild) {
			children = intro.GetChildren(subject)
			children[i] = newChild
		}
		env.UpLocal()
	}
	if children != nil {
		env.SetSubject(intro.SetChildren(subject, children))
	}
	return StatusSuccess
}
