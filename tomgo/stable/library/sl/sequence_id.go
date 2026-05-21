package sl

// SequenceId chains two strategies, but only fires Then if First
// actually *changed* the subject (referential inequality). Used to
// build "fixed-point" walks where Then should be re-tried only after
// First makes progress.
//
//	SequenceId(v1,v2)[t] = v2[v1[t]] if v1[t] != t, else v1[t]
type SequenceId struct{ abstractCombinator }

func NewSequenceId(first, then Strategy) Strategy {
	if then == nil {
		return first
	}
	s := &SequenceId{}
	s.initSubterm(first, then)
	return s
}

func (s *SequenceId) VisitLight(subject any, intro Introspector) (any, error) {
	v, err := s.args[0].VisitLight(subject, intro)
	if err != nil {
		return nil, err
	}
	if v != subject {
		return s.args[1].VisitLight(v, intro)
	}
	return v, nil
}
