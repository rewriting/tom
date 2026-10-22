package sl

// Identity is the strategy that returns its subject unchanged. It
// always succeeds. Equivalent to Java's tom.library.sl.Identity.
type Identity struct {
	abstractCombinator
}

// NewIdentity returns the identity strategy. Allocations: one struct
// per call. Callers that hot-loop construction may keep a single
// instance and reuse it.
func NewIdentity() Strategy {
	i := &Identity{}
	i.initSubterm()
	return i
}

func (Identity) VisitLight(subject any, _ Introspector) (any, error) {
	return subject, nil
}

// VisitEnv always succeeds and leaves the subject unchanged.
func (i *Identity) VisitEnv(Introspector) int { return StatusSuccess }

func (i *Identity) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(i, subject, intro)
}

func (i *Identity) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(i, env, intro)
}
