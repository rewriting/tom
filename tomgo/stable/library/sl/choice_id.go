package sl

// ChoiceId tries First; if it succeeds *and changed the subject*,
// returns that. Otherwise tries Then on the original subject. This is
// the "identity-aware" sibling of [Choice]: it treats "First succeeded
// but did nothing" as failure-equivalent for branching purposes,
// which is critical for one-pass top-down walks like
// TopDownIdStopOnSuccess.
//
//	ChoiceId(v1,v2)[t] = v1[t] if v1[t] != t, else v2[t]
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
	if err == nil && v != subject {
		return v, nil
	}
	// First either failed outright or returned the subject unchanged;
	// both cases fall through to Then (subject not v, mirroring the
	// Java reference's "restore the subject" semantics).
	return c.args[1].VisitLight(subject, intro)
}
