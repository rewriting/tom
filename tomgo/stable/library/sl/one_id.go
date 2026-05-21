package sl

import "errors"

// OneId applies its inner strategy to the first child that *changes*.
// Unlike [One], a child that "succeeds but is unchanged" doesn't count
// — we keep looking. If no child changes (or the subject is a leaf),
// OneId returns the subject unchanged (success, no progress).
//
//	OneId(s)[T(t1,...,ti,...,tn)] = T(t1,...,s[ti],...,tn)
//	  where ti is the first child for which s[ti] != ti.
//	OneId(s)[c] = c for any leaf c (success without progress).
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
			if errors.Is(err, ErrVisitFailure) {
				continue
			}
			return nil, err
		}
		if newChild != oldChild {
			return intro.SetChildAt(subject, i, newChild), nil
		}
	}
	return subject, nil
}
