package sl

import "errors"

// Not succeeds, leaving the subject unchanged, when its argument fails
// on it, and fails when its argument succeeds.
//
// Equivalent to Java's tom.library.sl.Not.
type Not struct{ abstractCombinator }

// NewNot builds Not(s).
func NewNot(s Strategy) Strategy {
	n := &Not{}
	n.initSubterm(s)
	return n
}

func (n *Not) VisitLight(subject any, intro Introspector) (any, error) {
	_, err := n.args[0].VisitLight(subject, intro)
	if err == nil {
		return nil, ErrVisitFailure
	}
	if errors.Is(err, ErrVisitFailure) {
		return subject, nil
	}
	return nil, err
}

func (n *Not) VisitEnv(intro Introspector) int {
	env := n.environment
	subject := env.GetSubject()
	status := n.args[0].VisitEnv(intro)
	// restore the subject: only the status of the argument matters
	env.SetSubject(subject)
	if status != StatusSuccess {
		return StatusSuccess
	}
	return StatusFailure
}

func (n *Not) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(n, subject, intro)
}

func (n *Not) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(n, env, intro)
}
