package sl

import "errors"

// IfThenElse applies its condition to the subject and, depending on
// whether it succeeds, its true or its false case to the original
// subject (the condition's result is discarded).
//
// Equivalent to Java's tom.library.sl.IfThenElse.
type IfThenElse struct{ abstractCombinator }

// Child indexes of an IfThenElse (Java CONDITION, TRUE_CASE, FALSE_CASE).
const (
	IfThenElseCondition = 0
	IfThenElseTrueCase  = 1
	IfThenElseFalseCase = 2
)

// NewIfThenElse builds IfThenElse(c, t, f).
func NewIfThenElse(c, t, f Strategy) Strategy {
	s := &IfThenElse{}
	s.initSubterm(c, t, f)
	return s
}

func (s *IfThenElse) VisitLight(subject any, intro Introspector) (any, error) {
	_, err := s.args[IfThenElseCondition].VisitLight(subject, intro)
	if err != nil && !errors.Is(err, ErrVisitFailure) {
		return nil, err
	}
	if err == nil {
		return s.args[IfThenElseTrueCase].VisitLight(subject, intro)
	}
	return s.args[IfThenElseFalseCase].VisitLight(subject, intro)
}

func (s *IfThenElse) VisitEnv(intro Introspector) int {
	env := s.environment
	subject := env.GetSubject()
	status := s.args[IfThenElseCondition].VisitEnv(intro)
	// reset the modifications of the condition: only its status matters
	env.SetSubject(subject)
	if status == StatusSuccess {
		return s.args[IfThenElseTrueCase].VisitEnv(intro)
	}
	return s.args[IfThenElseFalseCase].VisitEnv(intro)
}

func (s *IfThenElse) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(s, subject, intro)
}

func (s *IfThenElse) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(s, env, intro)
}
