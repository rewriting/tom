package sl

// Omega applies its argument to the indexPosition-th child (1-based)
// of the subject; 0 means the subject itself. It fails when the
// subject has no such child.
//
// Equivalent to Java's tom.library.sl.Omega.
type Omega struct {
	abstractCombinator
	indexPosition int
}

// NewOmega returns Omega(indexPosition, v).
func NewOmega(indexPosition int, v Strategy) Strategy {
	o := &Omega{indexPosition: indexPosition}
	o.initSubterm(v)
	return o
}

// GetPos returns the child number Omega goes to.
func (o *Omega) GetPos() int { return o.indexPosition }

func (o *Omega) VisitLight(subject any, intro Introspector) (any, error) {
	if o.indexPosition == 0 {
		return o.args[0].VisitLight(subject, intro)
	}
	if o.indexPosition > 0 && o.indexPosition <= intro.GetChildCount(subject) {
		childNumber := o.indexPosition - 1
		newChild, err := o.args[0].VisitLight(intro.GetChildAt(subject, childNumber), intro)
		if err != nil {
			return nil, err
		}
		return intro.SetChildAt(subject, childNumber, newChild), nil
	}
	return nil, ErrVisitFailure
}

func (o *Omega) VisitEnv(intro Introspector) int {
	if o.indexPosition == 0 {
		return o.args[0].VisitEnv(intro)
	}
	if o.indexPosition > 0 && o.indexPosition <= intro.GetChildCount(o.environment.GetSubject()) {
		o.environment.Down(o.indexPosition)
		status := o.args[0].VisitEnv(intro)
		o.environment.Up()
		return status
	}
	return StatusFailure
}

func (o *Omega) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(o, subject, intro)
}

func (o *Omega) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(o, env, intro)
}
