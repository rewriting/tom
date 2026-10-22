package sl

// ChoiceId (Java tom.library.sl.ChoiceId) applies First. A failure of
// First propagates (Then is NOT tried). If First succeeded and
// returned the subject unchanged (identity), Then is applied to it;
// otherwise First's result is returned. Strategies combined through
// ChoiceId (e.g. TopDownIdStopOnSuccess) must therefore not fail,
// typically by wrapping them in [MakeTry].
//
//	ChoiceId(v1,v2)[t] = v1[t] if v1[t] != t, else v2[t]; fails if v1[t] fails
type ChoiceId struct{ abstractCombinator }

func NewChoiceId(first, then Strategy) Strategy {
	if then == nil {
		return first
	}
	c := &ChoiceId{}
	c.initSubterm(first, then)
	return c
}

func (c *ChoiceId) VisitLight(subject any, intro Introspector) (any, error) {
	v, err := c.args[0].VisitLight(subject, intro)
	if err != nil {
		return nil, err
	}
	if v == subject {
		return c.args[1].VisitLight(v, intro)
	}
	return v, nil
}

// VisitEnv is the Java ChoiceId.visit(Introspector); same semantics as
// VisitLight.
func (c *ChoiceId) VisitEnv(intro Introspector) int {
	subject := c.environment.GetSubject()
	status := c.args[0].VisitEnv(intro)
	if status == StatusSuccess && c.environment.GetSubject() != subject {
		return status
	}
	// restore the subject: we are just interested in the status
	c.environment.SetSubject(subject)
	if status == StatusSuccess {
		return c.args[1].VisitEnv(intro)
	}
	return status
}

func (c *ChoiceId) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(c, subject, intro)
}

func (c *ChoiceId) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(c, env, intro)
}
