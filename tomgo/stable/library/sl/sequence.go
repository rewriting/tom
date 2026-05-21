package sl

// Sequence applies First then Then. If First returns [ErrVisitFailure],
// the failure propagates without invoking Then.
//
//	Sequence(v1,v2)[t] = v2[v1[t]] if v1 succeeds, otherwise fails.
type Sequence struct{ abstractCombinator }

// NewSequence returns a Sequence combinator, with the Java reference's
// null-tail shortcut: if then is nil, returns first directly (the
// generated `tom_make_*` helpers rely on this to terminate strategy
// lists).
func NewSequence(first, then Strategy) Strategy {
	if then == nil {
		return first
	}
	s := &Sequence{}
	s.initSubterm(first, then)
	return s
}

func (s *Sequence) VisitLight(subject any, intro Introspector) (any, error) {
	v, err := s.args[0].VisitLight(subject, intro)
	if err != nil {
		return nil, err
	}
	return s.args[1].VisitLight(v, intro)
}
