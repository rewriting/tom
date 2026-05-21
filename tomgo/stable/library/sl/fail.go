package sl

import "fmt"

// Fail is the strategy that always returns [ErrVisitFailure]. Optional
// message attached for diagnostics — exposed via wrapping so callers
// can [errors.Unwrap] or [errors.Is(err, ErrVisitFailure)].
//
// Equivalent to Java's tom.library.sl.Fail.
type Fail struct {
	abstractCombinator
	message string
}

func NewFail() Strategy { return NewFailWith("") }

func NewFailWith(message string) Strategy {
	f := &Fail{message: message}
	f.initSubterm()
	return f
}

func (f *Fail) VisitLight(_ any, _ Introspector) (any, error) {
	if f.message == "" {
		return nil, ErrVisitFailure
	}
	return nil, fmt.Errorf("%w: %s", ErrVisitFailure, f.message)
}
