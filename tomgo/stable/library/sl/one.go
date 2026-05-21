package sl

import "errors"

// One applies its inner strategy to exactly *one* child of the
// subject — the first one that succeeds. If every child fails (or
// the subject is a leaf), One itself fails.
//
//	One(s)[f(t1,...,ti,...,tn)] = f(t1,...,s[ti],...,tn)
//	                              where ti is the first child s succeeds on.
//	One(s)[c] fails for any leaf c.
type One struct{ abstractCombinator }

func NewOne(s Strategy) Strategy {
	o := &One{}
	o.initSubterm(s)
	return o
}

func (o *One) VisitLight(subject any, intro Introspector) (any, error) {
	count := intro.GetChildCount(subject)
	for i := range count {
		newChild, err := o.args[0].VisitLight(intro.GetChildAt(subject, i), intro)
		if err == nil {
			return intro.SetChildAt(subject, i, newChild), nil
		}
		if !errors.Is(err, ErrVisitFailure) {
			return nil, err
		}
	}
	return nil, ErrVisitFailure
}
