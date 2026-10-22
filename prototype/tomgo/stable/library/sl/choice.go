// Frozen prototype copy — the maintained version is src/go/tom/library/sl (module tom).

package sl

import "errors"

// Choice tries First; if it fails with [ErrVisitFailure], tries Then on
// the original subject. Note: side effects of a failing First are not
// undone — this is consistent with the Java reference.
//
//	Choice(v1,v2) = v1 if v1 succeeds, otherwise v2.
type Choice struct{ abstractCombinator }

// NewChoice returns a Choice combinator, with the same null-tail
// shortcut as [NewSequence].
func NewChoice(first, then Strategy) Strategy {
	if then == nil {
		return first
	}
	c := &Choice{}
	c.initSubterm(first, then)
	return c
}

func (c *Choice) VisitLight(subject any, intro Introspector) (any, error) {
	v, err := c.args[0].VisitLight(subject, intro)
	if err == nil {
		return v, nil
	}
	if errors.Is(err, ErrVisitFailure) {
		return c.args[1].VisitLight(subject, intro)
	}
	return nil, err
}

func (c *Choice) VisitEnv(intro Introspector) int {
	subject := c.environment.GetSubject()
	status := c.args[0].VisitEnv(intro)
	if status == StatusSuccess {
		return status
	}
	// reset the modifications from FIRST
	c.environment.SetSubject(subject)
	return c.args[1].VisitEnv(intro)
}

func (c *Choice) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(c, subject, intro)
}

func (c *Choice) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(c, env, intro)
}
