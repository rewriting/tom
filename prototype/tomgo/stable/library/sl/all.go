// Frozen prototype copy — the maintained version is src/go/tom/library/sl (module tom).

package sl

// All applies its inner strategy to every child of the subject. If
// any single child fails, the whole All fails. Applied to a leaf
// (zero children), it succeeds and returns the subject unchanged.
//
//	All(s)[f(t1,...,tn)] = f(s[t1],...,s[tn])
//	All(s)[c]            = c   if c is a leaf
//
// Note: this is the *parallel* All — every child is visited before
// reassembling the parent. See Java's AllSeq for sequential variant.
type All struct{ abstractCombinator }

func NewAll(s Strategy) Strategy {
	a := &All{}
	a.initSubterm(s)
	return a
}

func (a *All) VisitLight(subject any, intro Introspector) (any, error) {
	count := intro.GetChildCount(subject)
	var dup []any
	for i := range count {
		oldChild := intro.GetChildAt(subject, i)
		newChild, err := a.args[0].VisitLight(oldChild, intro)
		if err != nil {
			return nil, err
		}
		if dup != nil {
			dup[i] = newChild
		} else if newChild != oldChild {
			dup = intro.GetChildren(subject)
			dup[i] = newChild
		}
	}
	if dup == nil {
		return subject, nil
	}
	return intro.SetChildren(subject, dup), nil
}

func (a *All) VisitEnv(intro Introspector) int {
	env := a.environment
	env.SetIntrospector(intro)
	subject := env.GetSubject()
	count := intro.GetChildCount(subject)
	var children []any
	for i := range count {
		oldChild := intro.GetChildAt(subject, i)
		env.Down(i + 1)
		if status := a.args[0].VisitEnv(intro); status != StatusSuccess {
			env.UpLocal()
			return status
		}
		newChild := env.GetSubject()
		if children != nil {
			children[i] = newChild
		} else if newChild != oldChild {
			children = intro.GetChildren(subject)
			children[i] = newChild
		}
		env.UpLocal()
	}
	if children != nil {
		env.SetSubject(intro.SetChildren(subject, children))
	}
	return StatusSuccess
}

func (a *All) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(a, subject, intro)
}

func (a *All) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(a, env, intro)
}
