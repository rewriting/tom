// Frozen prototype copy — the maintained version is src/go/tom/library/sl (module tom).

package sl

// OneId applies its inner strategy to the first child that *changes*.
// Unlike [One], a child that "succeeds but is unchanged" doesn't count
// — we keep looking. If no child changes (or the subject is a leaf),
// OneId returns the subject unchanged (success, no progress).
//
//	OneId(s)[T(t1,...,ti,...,tn)] = T(t1,...,s[ti],...,tn)
//	  where ti is the first child for which s[ti] != ti.
//	OneId(s)[c] = c for any leaf c (success without progress).
//
// A failure of s on a child propagates from VisitLight (Java
// OneId.visitLight does not catch it). Java's environment-based
// visit(Introspector), mirrored by VisitEnv, is lenient and skips
// failing children: that Java-internal inconsistency is kept as is.
type OneId struct{ abstractCombinator }

func NewOneId(s Strategy) Strategy {
	o := &OneId{}
	o.initSubterm(s)
	return o
}

func (o *OneId) VisitLight(subject any, intro Introspector) (any, error) {
	count := intro.GetChildCount(subject)
	for i := range count {
		oldChild := intro.GetChildAt(subject, i)
		newChild, err := o.args[0].VisitLight(oldChild, intro)
		if err != nil {
			// Java OneId.visitLight does not catch VisitFailure.
			return nil, err
		}
		if newChild != oldChild {
			return intro.SetChildAt(subject, i, newChild), nil
		}
	}
	return subject, nil
}

func (o *OneId) VisitEnv(intro Introspector) int {
	env := o.environment
	count := intro.GetChildCount(env.GetSubject())
	for i := range count {
		env.Down(i + 1)
		oldSubject := env.GetSubject()
		status := o.args[0].VisitEnv(intro)
		if status == StatusSuccess && env.GetSubject() != oldSubject {
			env.Up()
			return StatusSuccess
		}
		env.UpLocal()
	}
	return StatusSuccess
}

func (o *OneId) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(o, subject, intro)
}

func (o *OneId) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(o, env, intro)
}
